/**
 * @author Xi Lin
 */

package com.wanted.ws.remote;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.wanted.database.Database;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.entities.Recruiter;
import com.wanted.entities.Role;
import com.wanted.entities.Seeker;
import com.wanted.entities.User;

public class DefaultSocketClient extends Thread implements SocketClientInterface, SocketClientConstants {

	private ObjectInputStream reader;
    private ObjectOutputStream writer;
    private Socket socket;

    public DefaultSocketClient(Socket socket) {
    	this.setSocket(socket);
    }
    
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
    
	public void run() {
		if (openConnection()){
			handleSession();
			closeSession();
		}
	}
    
	/**
	 * Create a socket and initialize reader and writer
	 */
	@Override
	public boolean openConnection() {
		try {
			writer = new ObjectOutputStream(socket.getOutputStream());
			reader = new ObjectInputStream(socket.getInputStream());
		}
		catch (Exception e){
		   if (DEBUG) System.err.println("Unable to obtain stream to/from client");
		   return false;
		}
		return true;

	}

	/**
	 * Receive messages from client and handle them
	 */
	@Override
	public void handleSession() {
		try {
			Object object;
			while ((object = reader.readObject()) != null)
				handleInput(object);
			sendOutput("Bye bye!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			System.out.println("Server is processing...");
		} catch (EOFException e) {
			System.out.println("Server is processing...");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * According to client input, do the corresponding jobs
	 */
	private void handleInput(Object obj) throws IOException {
		Pack request = (Pack) obj;
		if (request.getInfo() == Information.REGISTER) {
			handleRegister(request);
		}
		
	}

	/**
	 * Handle register request
	 * @param request
	 * @throws IOException
	 */
	private void handleRegister(Pack request) {
		Pack response;
		Database database = new Database();
		User user = null;
		int id;
		try {
			user = (User)(request.getContent());
			if (user.getRole() == Role.SEEKER)
				id = database.insertSeeker((Seeker)user);
			else
				id = database.insertRecruiter((Recruiter)user);
			if (id == -1) {
				System.out.println("Existed email or username!");
				response = new Pack(Information.FAIL, null);
				sendOutput(response);
				return;
			}
			user.setId(id);
			response = new Pack(Information.SUCCESS, user);
			sendOutput(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send message to client
	 */
	private void sendOutput(Object obj) throws IOException {
		writer.writeObject(obj);
		writer.flush();
	}

	/**
	 * Close the connection and set writer and reader to be null 
	 */
	@Override
	public void closeSession() {
		try {
			writer = null;
			reader = null;
			socket.close();
		} catch (IOException e) {
			if (DEBUG) System.err.println("Error closing socket to client");
		}
		
	}


}
