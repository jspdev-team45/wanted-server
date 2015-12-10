package com.wanted.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.entities.Post;
import com.wanted.util.ServletUtil;

/**
 * Servlet implementation class GetPost
 */
public class GetPost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetPost() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
			String s = (String)(pack.getContent());
			int postNum = 4;
			int splitIndex = s.indexOf(":");
			String major = s.substring(0, splitIndex);
			int cursor = Integer.parseInt(s.substring(splitIndex+1, s.length()));
			List<Post> p = new ArrayList<Post>(); 
			if (cursor == -1)
				p = database.getNewPost(postNum, major);
			else
				p = database.getMorePost(cursor, postNum, major);
			pack = new Pack(Information.SUCCESS, p);
			oos.writeObject(pack);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
