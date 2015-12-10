package com.wanted.ws.remote;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wanted.database.Database;
import com.wanted.entities.Company;
import com.wanted.entities.Information;
import com.wanted.entities.Pack;
import com.wanted.util.FileUtil;
import com.wanted.util.ServletUtil;
import com.wanted.util.StringUtil;

public class UpdateCompany extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * Default constructor. 
     */
    public UpdateCompany() {
    	super();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			              throws ServletException, IOException {
		Database database = new ServletUtil().getDatabase(getServletContext());
		Pack pack = null;
		byte[] image;
		byte[] hash;
		String path;
		Company company = null;
		int id;
		
		try (ObjectInputStream ois = new ObjectInputStream(request.getInputStream()); 
			ObjectOutputStream oos = new ObjectOutputStream(response.getOutputStream())) {
			
			Class.forName("com.mysql.jdbc.Driver");
			pack = (Pack) ois.readObject();
			image = pack.getImage();
			company = (Company) pack.getContent();
			hash = getHashFromImage(image);
			path = database.getBannerByHash(hash);
			if (path == null)
				path = storeImage(image);
			company.setBanner(path);
			company.setHash(hash);
			id = database.insertCompany(company);
			company.setId(id);
			
			oos.writeObject(new Pack(Information.SUCCESS, company));
			oos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 */
	private String storeImage(byte[] image) {
		String filename = new FileUtil().getUniqueImageFilename();
		String path = new StringUtil().getImageFolder() + File.separator + filename;
		OutputStream out;
		try {
			out = new BufferedOutputStream(new FileOutputStream(path));
			out.write(image);
			
	    } catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filename;
	}

	/**
	 * 
	 * @param image
	 * @return
	 */
	private byte[] getHashFromImage(byte[] image) {
		byte[] hash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			hash = md.digest(image);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hash;
	}

}
