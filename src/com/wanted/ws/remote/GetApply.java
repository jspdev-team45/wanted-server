// The servlet that deals with getting the applicant list into the database
package com.wanted.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.entities.Seeker;
import com.wanted.util.ServletUtil;

/**
 * Servlet implementation class GetApply
 */
public class GetApply extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetApply() {
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
		Database database = new ServletUtil().getDatabase(getServletContext());
		try (ObjectInputStream ois = new ObjectInputStream(request.getInputStream()); 
			 ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream())) {
			Class.forName("com.mysql.jdbc.Driver");
			Pack pack = (Pack) ois.readObject();
			int pid = (int)(pack.getContent());
			List<Seeker> s = database.getApply(pid); 
			pack = new Pack(Information.SUCCESS, s);
			oos.writeObject(pack);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
