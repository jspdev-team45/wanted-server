// This class deals with the login request from the client
package com.wanted.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.entities.Recruiter;
import com.wanted.entities.Role;
import com.wanted.entities.Seeker;
import com.wanted.util.ServletUtil;
import com.wanted.util.StringUtil;

/**
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Database database = new ServletUtil().getDatabase(getServletContext());
		
		try (ObjectInputStream ois = new ObjectInputStream(request.getInputStream()); 
			 ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream())) {
			Class.forName("com.mysql.jdbc.Driver");
			Pack pack = (Pack) ois.readObject();
			String[] info = ((String) pack.getContent()).split(":");
			String email = info[0];
			String MD5Pass = new StringUtil().MD5(info[1]);
			int uid = database.findUserIDByEmail(email);
			if (uid == -1)
				pack = new Pack(Information.USER_NOT_EXIST, null);
			else {
				int role = database.getRoleAndCheckPass(uid, MD5Pass);
				if (role == -1)
					pack = new Pack(Information.WRONG_PWD, null);
				else {
					if (role == Role.SEEKER) {
						Seeker seeker = database.getSeeker(uid);
						pack = new Pack(Information.SUCCESS, seeker);
					}
					else {
						Recruiter recruiter = database.getRecruiter(uid);
						pack = new Pack(Information.SUCCESS, recruiter);
					}
				}
			}
			oos.writeObject(pack);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
