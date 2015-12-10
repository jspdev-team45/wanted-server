package com.wanted.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Experience;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;

/**
 * Servlet implementation class AddExperience
 */
public class AddExperience extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddExperience() {
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
			Experience exp = (Experience)(pack.getContent());
			database.insertExperience(exp);
			pack = new Pack(Information.SUCCESS, null);
			oos.writeObject(pack);
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
