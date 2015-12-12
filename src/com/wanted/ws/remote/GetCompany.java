// The servlet that deals with getting the company record from the database
package com.wanted.ws.remote;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Company;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.util.ServletUtil;

/**
 * Servlet implementation class GetCompany
 */
public class GetCompany extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCompany() {
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
		Pack pack = null;
		Integer companyID;
		
		try (ObjectInputStream ois = new ObjectInputStream(request.getInputStream()); 
			ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream())) {
			
			// get data
			Class.forName("com.mysql.jdbc.Driver");
			pack = (Pack) ois.readObject();
			companyID = (Integer) pack.getContent();
			
			// write result back
			Company company = database.getCompany(companyID);
			oos.writeObject(new Pack(Information.SUCCESS, company));
			oos.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
