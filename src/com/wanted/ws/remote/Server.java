package com.wanted.ws.remote;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.wanted.ws.remote.DefaultSocketClient;

public class Server {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		try {
		    String ip = InetAddress.getLocalHost().getHostAddress();
			InetAddress addr = InetAddress.getByName(ip) ;
			ServerSocket serverSocket = new ServerSocket(SocketClientConstants.PORT, 10, addr);
		    System.out.println("Open server at address: " + ip + ":" + SocketClientConstants.PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				DefaultSocketClient socketThread = new DefaultSocketClient(socket);
				socketThread.start();
			}
		} catch (IOException socketError) {
			System.err.println("Unable to connect");
		}
	}
}
