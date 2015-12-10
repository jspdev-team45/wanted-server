package com.wanted.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.entities.User;

/**
 * Servlet implementation class GetPeople
 */
public class GetPeople extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPeople() {
        super();
        // TODO Auto-generated constructor stub
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
		ServletContext context = getServletContext();
		String dbProFile = context.getRealPath("/WEB-INF/db.properties");
		String sqlProFile = context.getRealPath("/WEB-INF/sql.properties");
		Database database = new Database(dbProFile, sqlProFile);
		try (ObjectInputStream ois = new ObjectInputStream(request.getInputStream()); 
			 ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream())) {
			Class.forName("com.mysql.jdbc.Driver");
			Pack pack = (Pack) ois.readObject();
			String s = (String)(pack.getContent());
			int splitIndex = s.indexOf(":");
			int uid = Integer.parseInt(s.substring(0, splitIndex));
			int cursor = Integer.parseInt(s.substring(splitIndex+1, s.length()));
			int postNum = 4;
			List<User> p = new ArrayList<User>(); 
			if (cursor == -1)
				p = database.getNewPeople(uid, postNum);
			else
				p = database.getMorePeople(uid, cursor, postNum);
			pack = new Pack(Information.SUCCESS, p);
			oos.writeObject(pack);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
