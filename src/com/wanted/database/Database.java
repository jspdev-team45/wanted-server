// This class is used for performing database operations

package com.wanted.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import com.wanted.entities.Company;
import com.wanted.entities.Experience;
import com.wanted.entities.Post;
import com.wanted.entities.Recruiter;
import com.wanted.entities.Role;
import com.wanted.entities.Seeker;
import com.wanted.entities.User;
import com.wanted.util.StringUtil;

public class Database {
	private SQLCommand cmd;
	private String fileName = "src/data/db.properties";
	private String db;
	private String url;
	private String username;
	private String password;
	
	/**
	 * the default constructor that initialize the database
	 */
	public Database() {
		cmd = new SQLCommand();
		try (FileInputStream in = new FileInputStream(fileName)) {
			Properties prop = new Properties();
			prop.load(in);
			System.setProperty("jdbc.drivers", prop.getProperty("jdbc.drivers"));
			url = prop.getProperty("jdbc.url");
			db = prop.getProperty("jdbc.db");
			username = prop.getProperty("jdbc.username");
			password = prop.getProperty("jdbc.password");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * initialize the database with the database properties file path 
	 */
	public Database(String dbFile, String sqlFile) {
		this.fileName = dbFile;
		cmd = new SQLCommand(sqlFile);
		try (FileInputStream in = new FileInputStream(fileName)) {
			Properties prop = new Properties();
			prop.load(in);
			System.setProperty("jdbc.drivers", prop.getProperty("jdbc.drivers"));
			url = prop.getProperty("jdbc.url");
			db = prop.getProperty("jdbc.db");
			username = prop.getProperty("jdbc.username");
			password = prop.getProperty("jdbc.password");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the database
	 */
	public void initialize() {
		dropDB();
		createDB();
		createTables();
	}
	
	/**
	 * Get connection to the database
	 */
	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(url + "/" + db, username, password);
		return conn;
	}
	
	/**
	 * Create database
	 */
	private void createDB() {
		try (Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement()) {
			stmt.execute(cmd.createDB());
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	/**
	 * Drop the database
	 */
	private void dropDB() {
		try (Connection conn = DriverManager.getConnection(url, username, password);
			Statement stmt = conn.createStatement()) {
			stmt.execute(cmd.dropDB());
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Create all the tables
	 */
	private void createTables() {
		try (Connection conn = getConnection();
			Statement stmt = conn.createStatement()) {
	        stmt.execute(cmd.createTableUser());
	        stmt.execute(cmd.createTableSeeker());
	        stmt.execute(cmd.createTableCompany());
	        stmt.execute(cmd.createTableRecruiter());
	        stmt.execute(cmd.createTableExper());
	        stmt.execute(cmd.createTablePost());
	        stmt.execute(cmd.createTableFollow());
	        stmt.execute(cmd.createTableLike());
	        stmt.execute(cmd.createTableApply());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int findUserIDByUserName(String uname) {
		int id = -1;
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(cmd.getIDByUserName())) {
			pstmt.setString(1, uname);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next())
					id = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public int findUserIDByEmail(String email) {
		int id = -1;
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(cmd.getIDByEmail())) {
			pstmt.setString(1, email);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next())
					id = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	private int findCompanyIDByName(String companyName) {
		int id = -1;
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(cmd.getCompanyIDByName())) {
			pstmt.setString(1, companyName);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next())
					id = resultSet.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Insert user into database
	 * @param name
	 * @param password
	 * @param email
	 * @param role
	 * @return
	 * @throws SQLException
	 */
	private int insertUser(String name, String password, String email, Integer role) {
		int id = -1;
		if (findUserIDByEmail(email) != -1 || findUserIDByUserName(name) != -1) {
			System.out.println("Existed user name or email in database!");
			return -1;
		}
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(cmd.insertUser(), Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, name);
			pstmt.setString(2, new StringUtil().MD5(password));
			pstmt.setString(3, email);
			pstmt.setInt(4, role);
			pstmt.setString(5, null);
			pstmt.setString(6, "default.jpg");
			pstmt.setString(7, null);
			pstmt.executeUpdate();
			try (ResultSet result = pstmt.getGeneratedKeys()) {
				result.next();
				id = result.getInt(1);
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Insert seeker into database
	 * @param seeker
	 * @return
	 * @throws MySQLIntegrityConstraintViolationException
	 */

	public int insertSeeker(Seeker seeker) {
		int id = insertUser(seeker.getName(), seeker.getPassWord(), seeker.getEmail(), seeker.getRole());
		if (id == -1) {
			System.out.println("Seeker has invalid email or username existed in database!");
			return -1;
		}
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.insertSeeker())) {
			pstmt.setInt(1, id);
			pstmt.setString(2, null);
			pstmt.setString(3, null);
			pstmt.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Insert recruiter into database
	 * @param recruiter
	 * @return
	 * @throws MySQLIntegrityConstraintViolationException
	 */
	public int insertRecruiter(Recruiter recruiter) {
		int id = insertUser(recruiter.getName(), recruiter.getPassWord(), recruiter.getEmail(), recruiter.getRole());
		if (id == -1) {
			System.out.println("Recuriter has invalid email or username existed in database!");
			return -1;
		}
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.insertRecruiter())) {
			pstmt.setInt(1, id);
			pstmt.setInt(2, -1);
			pstmt.setString(3, null);
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * The function that insert company record into the database 
	 */
	public int insertCompany(Company company) {
		if (findCompanyIDByName(company.getName()) != -1) {
			System.out.println("Existed company in database!");
			//return -1;
		}
		int id = -2;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.insertCompany(), Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, company.getName());
			pstmt.setString(2, company.getBanner());
			pstmt.setString(3, company.getDescription());
			pstmt.setString(4, company.getLocation());
			pstmt.setBytes(5, company.getHash());
			pstmt.executeUpdate();
			try (ResultSet result = pstmt.getGeneratedKeys()) {
				result.next();
				id = result.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Insert the experience record into the database 
	 */
	public int insertExperience(Experience exper) {
		int id = -1;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.insertExper(), Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, exper.getUserID());
			pstmt.setString(2, exper.getStartDate());
			pstmt.setString(3, exper.getEndDate());
			pstmt.setString(4, exper.getDescription());
			pstmt.setByte(5, exper.getExType());
			pstmt.executeUpdate();
			try ( ResultSet result = pstmt.getGeneratedKeys()) {
				result.next();
				id = result.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Insert the Post record into the database 
	 */
	public int insertPost(Post post) {
		int id = -1;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.insertPost(), Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, post.getUid());
			pstmt.setString(2, post.getTitle());
			pstmt.setString(3, post.getDescription());
			pstmt.setString(4, post.getSuitMajor());
			pstmt.executeUpdate();
			try ( ResultSet result = pstmt.getGeneratedKeys()) {
				result.next();
				id = result.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	/**
	 * Delete user from database
	 * @param uid
	 */
	public void deleteUser(int uid) {
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(cmd.deleteUser())) {
			pstmt.setInt(1, uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Delete experience record from database
	 * @param eid
	 */
	public void deleteExperience(int eid) {
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.deleteExper())) {
			pstmt.setInt(1, eid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Delete post information from database
	 * @param pid
	 */
	public void deletePost(int pid) {
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.deletePost())) {
			pstmt.setInt(1, pid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Update user information
	 * @param uid
	 * @param avatarPath
	 * @param realName
	 */
	public void updateUser(int uid, String avatarPath, String realName, String phoneNum, byte[] hash) {
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.updateUser())) {
			pstmt.setString(1, avatarPath);
			pstmt.setString(2, realName);
			pstmt.setString(3, phoneNum);
			pstmt.setBytes(4, hash);
			pstmt.setInt(5, uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Update seeker information
	 * @param uid
	 * @param college
	 * @param major
	 */
	public void updateSeeker(int uid, String college, String major) {
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.updateSeeker())) {
			pstmt.setString(1, college);
			pstmt.setString(2, major);
			pstmt.setInt(3, uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Update recruiter information
	 * @param uid
	 * @param cid
	 * @param dept
	 */
	public void updateRecruiter(int uid, Integer cid, String dept) {
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.updateRecruiter())) {
			if (cid == null)
				cid = -1;
			pstmt.setInt(1, cid);
			pstmt.setString(2, dept);
			pstmt.setInt(3, uid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Update company information
	 */
	public void updateCompany(int cid, String cname, String bannerPath, String descript, String location) {
		if (cid == -1) {
			cid = insertCompany(new Company(cname, bannerPath, descript, location));
			return;
		}
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.updateCompany())) {
			pstmt.setString(1, cname);
			pstmt.setString(2, bannerPath);
			pstmt.setString(3, descript);
			pstmt.setString(4, location);
			pstmt.setInt(5, cid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Update Experience record information in the database
	 */
	public void updateExperience(int pid, String startTime, String endTime, String descript) {
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.updateExper())) {
			pstmt.setString(1, startTime);
			pstmt.setString(2, endTime);
			pstmt.setString(3, descript);
			pstmt.setInt(4, pid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Update Post information in the database
	 */
	public void updatePost(int pid, String title, String descript) {
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(cmd.updatePost())) {
			pstmt.setString(1, title);
			pstmt.setString(2, descript);
			pstmt.setInt(3, pid);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Given the user id and password, check if the user is valid 
	 */
	public int getRoleAndCheckPass(int uid, String password) {
		int role = -1;
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getUserById())) {
			pstmt.setInt(1, uid);
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				// if role is -1, the user password is wrong
				if (password.equals(set.getString(3))) {
					role = set.getInt(5);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return role;
	}
	
	/**
	 * Get seeker record by id
	 * @param uid
	 * @return
	 */
	public Seeker getSeeker(int uid) {
		Seeker seeker = new Seeker(null, null, null, null);
		seeker.setId(uid);
		try (Connection conn = getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(cmd.getUserById());
			pstmt.setInt(1, uid);
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				seeker.setName(set.getString(2));
				seeker.setEmail(set.getString(4));
				seeker.setRole(set.getInt(5));
				seeker.setPhone(set.getString(6));
				seeker.setAvatar(set.getString(7));
				seeker.setRealName(set.getString(8));
			}
			
			set.close();
			pstmt.close();
			pstmt = conn.prepareStatement(cmd.getSeeker());
			pstmt.setInt(1, uid);
			set = pstmt.executeQuery();
			if(set.next()) {
				seeker.setCollege(set.getString(2));
				seeker.setMajor(set.getString(3));
			}
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return seeker;
	}
	
	/**
	 * Get recruiter information by id
	 * @param uid
	 * @return
	 */
	public Recruiter getRecruiter(int uid) {
		Recruiter recruiter = new Recruiter(null, null, null, null);
		recruiter.setId(uid);
		try (Connection conn = getConnection()) {
			PreparedStatement pstmt = conn.prepareStatement(cmd.getUserById());
			pstmt.setInt(1, uid);
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				recruiter.setName(set.getString(2));
				recruiter.setEmail(set.getString(4));
				recruiter.setRole(set.getInt(5));
				recruiter.setPhone(set.getString(6));
				recruiter.setAvatar(set.getString(7));
				recruiter.setRealName(set.getString(8));
			}
			
			set.close();
			pstmt.close();
			pstmt = conn.prepareStatement(cmd.getRecruiter());
			pstmt.setInt(1, uid);
			set = pstmt.executeQuery();
			if(set.next()) {
				recruiter.setCompanyID(set.getInt(2));
				recruiter.setDepartment(set.getString(3));
			}
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return recruiter;
	}
	
	/**
	 * Check if the image existed in server given by the hash value of the image
	 * @param hash
	 * @return
	 */
	public String getAvatarByHash(byte[] hash) {
		String avatarPath = null;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.getAvatarByHash())) {
			pstmt.setBytes(1, hash);
			ResultSet set = pstmt.executeQuery();
			if (set.next()) {
				avatarPath = set.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return avatarPath;
	}
	
	/**
	 * Get company name by id
	 * @param cid
	 * @return
	 */
	public String getCompanyName(int cid) {
		String name = null;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.getCompanyById())) {
			pstmt.setInt(1, cid);
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				name = set.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return name;
	}
	
	/**
	 * Get company object by company id
	 */
	public Company getCompany(int cid) { 
		Company company = new Company();
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.getCompanyById())) {
			pstmt.setInt(1, cid);
			ResultSet set = pstmt.executeQuery();
			if(set.next()) {
				company.setName(set.getString(2));
				company.setBanner(set.getString(3));
				company.setDescription(set.getString(4));
				company.setLocation(set.getString(5));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return company;
	}
	
	/**
	 * Get company object by user id
	 */
	public Company getCompanyByUId(int uid) {
		Company company = null;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.getCompanyByUId())) {
			pstmt.setInt(1, uid);
			ResultSet set = pstmt.executeQuery();
			if (set.next()) {
				company = new Company(set.getString(1), set.getString(2), set.getString(3), set.getString(4));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return company;
	}

	/**
	 * Get banner path by the hash value of the image
	 */
	public String getBannerByHash(byte[] hash) {
		String bannerPath = null;
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.getBannerByHash())) {
			pstmt.setBytes(1, hash);
			ResultSet set = pstmt.executeQuery();
			if (set.next()) {
				System.out.println("Find same image!");
				bannerPath = set.getString(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bannerPath;
	}
	
	/**
	 * Get the experience list of a specific user 
	 */
	public List<Experience> getExperienceOfUser(int uid, int type) {
		List<Experience> expList = new ArrayList<Experience>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getExperByUId())) {
			pstmt.setInt(1, uid);
			pstmt.setInt(2, type);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					Experience exp = new Experience(set.getInt(2), set.getString(3), set.getString(4), set.getString(5), set.getByte(6));
					expList.add(exp);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return expList;
	}

	/**
	 * Get all the job posts published by a specific recruiter  
	 */
	public List<Post> getPostOfUser(int uid) {
		List<Post> postList = new ArrayList<Post>();
		try (Connection conn = getConnection(); 
			PreparedStatement pstmt = conn.prepareStatement(cmd.getPostByUId())) {
			pstmt.setInt(1, uid);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					Post post = new Post(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4), set.getString(5));
					postList.add(post);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		return postList;
	}
	
	/**
	 * Deal with the request from the client that asks to load more job posts 
	 */
	public List<Post> getMorePost(int cursor, int maxFetch, String major) {
		List<Post> postList = new ArrayList<Post>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getMorePost())) { 
			pstmt.setInt(1, cursor);
			pstmt.setString(2, major);
			pstmt.setInt(3, maxFetch);
			ResultSet set = pstmt.executeQuery();
			while (set.next()) {
				int uid = set.getInt(2);
				Post post = new Post(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4), set.getString(5));
				post.setCompany(getCompanyByUId(uid));
				post.setRecruiter(getRecruiter(uid));
				postList.add(post);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postList;
	}

	/**
	 * Deal with the request from the client that asks to load new job posts 
	 */
	public List<Post> getNewPost(int maxFetch, String major) {
		List<Post> postList = new ArrayList<Post>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getNewPost())) { 
			pstmt.setString(1, major);
			pstmt.setInt(2, maxFetch);
			ResultSet set = pstmt.executeQuery();
			while (set.next()) {
				int uid = set.getInt(2);
				Post post = new Post(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4), set.getString(5));
				post.setCompany(getCompanyByUId(uid));
				post.setRecruiter(getRecruiter(uid));
				postList.add(post);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postList;
	}
	
	/**
	 * Deal with the request from the client that asks to load more people to display
	 */
	public List<User> getMorePeople(int id, int cursor, int maxFetch) {
		List<User> userList = new ArrayList<User>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getMorePeople())) { 
			pstmt.setInt(1, cursor);
			pstmt.setInt(2, id);
			pstmt.setInt(3, maxFetch);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					int uid = set.getInt(1);
					int role = set.getInt(5);
					if (role == Role.SEEKER) {
						Seeker seeker = new Seeker(set.getInt(1), set.getString(2), set.getString(4), set.getInt(5), set.getString(6), set.getString(7), set.getString(8));
						try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getSeeker())) {
							pstmt1.setInt(1, uid);
							try (ResultSet set1 = pstmt1.executeQuery()) {
								if (set1.next()) {
									seeker.setCollege(set1.getString(2));
									seeker.setMajor(set1.getString(3));
								}
							}
						}
						userList.add(seeker);
					}
					else {
						Recruiter recruiter = new Recruiter(set.getInt(1), set.getString(2), set.getString(4), set.getInt(5), set.getString(6), set.getString(7), set.getString(8));
						try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getRecruiter())) {
							pstmt1.setInt(1, uid);
							try (ResultSet set1 = pstmt1.executeQuery()) {
								if (set1.next()) {
									recruiter.setCompanyID(set1.getInt(2));
									recruiter.setDepartment(set1.getString(3));
								}
							}
						}
						userList.add(recruiter);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;
	}
	
	/**
	 * Deal with the request from the client that asks to load new people to display
	 */
	public List<User> getNewPeople(int id, int maxFetch) {
		List<User> userList = new ArrayList<User>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getNewPeople())) { 
			pstmt.setInt(1, id);
			pstmt.setInt(2, maxFetch);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					int uid = set.getInt(1);
					int role = set.getInt(5);
					if (role == Role.SEEKER) {
						Seeker seeker = new Seeker(set.getInt(1), set.getString(2), set.getString(4), set.getInt(5), set.getString(6), set.getString(7), set.getString(8));
						try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getSeeker())) {
							pstmt1.setInt(1, uid);
							try (ResultSet set1 = pstmt1.executeQuery()) {
								if (set1.next()) {
									seeker.setCollege(set1.getString(2));
									seeker.setMajor(set1.getString(3));
								}
							}
						}
						userList.add(seeker);
					}
					else {
						Recruiter recruiter = new Recruiter(set.getInt(1), set.getString(2), set.getString(4), set.getInt(5), set.getString(6), set.getString(7), set.getString(8));
						try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getRecruiter())) {
							pstmt1.setInt(1, uid);
							try (ResultSet set1 = pstmt1.executeQuery()) {
								if (set1.next()) {
									recruiter.setCompanyID(set1.getInt(2));
									recruiter.setDepartment(set1.getString(3));
								}
							}
						}
						userList.add(recruiter);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;	
	}

	/**
	 * Deal with the request from the recruiter that asks to load new posts published by a specific recruiter
	 */
	public List<Post> getMyNewPost(int uid, int maxFetch) {
		List<Post> postList = new ArrayList<Post>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getMyNewPost())) { 
			pstmt.setInt(1, uid);
			pstmt.setInt(2, maxFetch);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					Post post = new Post(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4), set.getString(5));
					post.setCompany(getCompanyByUId(uid));
					post.setRecruiter(getRecruiter(uid));
					postList.add(post);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postList;
	}

	/**
	 * Deal with the request from the recruiter that asks to load more post published by a specific recruiter
	 */
	public List<Post> getMyMorePost(int uid, int cursor, int maxFetch) {
		List<Post> postList = new ArrayList<Post>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getMyMorePost())) { 
			pstmt.setInt(1, cursor);
			pstmt.setInt(2, uid);
			pstmt.setInt(3, maxFetch);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					Post post = new Post(set.getInt(1), set.getInt(2), set.getString(3), set.getString(4), set.getString(5));
					post.setCompany(getCompanyByUId(uid));
					post.setRecruiter(getRecruiter(uid));
					postList.add(post);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postList;
	}
	
	/**
	 * The function that inserts the following relationship between two users  
	 */
	public int insertFollow(int uid1, int uid2) {
		int fid = -1;
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.insertFollow(), Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, uid1);
			pstmt.setInt(2, uid2);
			fid = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fid;
	}
	
	/**
	 * Get the following list of a specific user 
	 */
	public List<User> getFollowing(int id) {
		List<User> userList = new ArrayList<User>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getFollowingById())) { 
			pstmt.setInt(1, id);
			ResultSet set = pstmt.executeQuery();
			while (set.next()) {
				try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getUserById())) {
					pstmt1.setInt(1, set.getInt(1));
					ResultSet set1 = pstmt1.executeQuery();
					while (set1.next()) {
						int role = set1.getInt(5);
						if (role == Role.SEEKER) {
							Seeker seeker = new Seeker(set1.getInt(1), set1.getString(2), set1.getString(4), set1.getInt(5), set1.getString(6), set1.getString(7), set1.getString(8));
							userList.add(seeker);
						}
						else {
							Recruiter recruiter = new Recruiter(set1.getInt(1), set1.getString(2), set1.getString(4), set1.getInt(5), set1.getString(6), set1.getString(7), set1.getString(8));
							userList.add(recruiter);
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;	
	}

	/**
	 * Get the follower list of a specific user 
	 */
	public List<User> getFollower(int id) {
		List<User> userList = new ArrayList<User>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getFollowerById())) { 
			pstmt.setInt(1, id);
			ResultSet set = pstmt.executeQuery();
			while (set.next()) {
				try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getUserById())) {
					pstmt1.setInt(1, set.getInt(1));
					ResultSet set1 = pstmt1.executeQuery();
					while (set1.next()) {
						int role = set1.getInt(5);
						if (role == Role.SEEKER) {
							Seeker seeker = new Seeker(set1.getInt(1), set1.getString(2), set1.getString(4), set1.getInt(5), set1.getString(6), set1.getString(7), set1.getString(8));
							userList.add(seeker);
						}
						else {
							Recruiter recruiter = new Recruiter(set1.getInt(1), set1.getString(2), set1.getString(4), set1.getInt(5), set1.getString(6), set1.getString(7), set1.getString(8));
							userList.add(recruiter);
						}
					}
					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userList;	
	}

	/**
	 * The function that inserts the favorite relationship between two users  
	 */
	public int insertLike(int uid, int pid) {
		int lid = -1;
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.findLike())) {
			pstmt.setInt(1, uid);
			pstmt.setInt(2, pid);
			try (ResultSet set = pstmt.executeQuery()) {
				if (set.next())
					return -1;
				else {
					try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.insertLike(), Statement.RETURN_GENERATED_KEYS)){
						pstmt1.setInt(1, uid);
						pstmt1.setInt(2, pid);
						pstmt1.executeUpdate();
						lid = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lid;	
	}
	
	/**
	 * Get the favorite list of a specific user 
	 */
	public List<Post> getLike(int id) {
		List<Post> postList = new ArrayList<Post>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getLikeById())) { 
			pstmt.setInt(1, id);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.getPostByPId())) {
						pstmt1.setInt(1, set.getInt(1));
						try (ResultSet set1 = pstmt1.executeQuery()) {
							if (set1.next()) {
								int uid = set1.getInt(2);
								Post post = new Post(set1.getInt(1), set1.getInt(2), set1.getString(3), set1.getString(4), set1.getString(5));
								post.setCompany(getCompanyByUId(uid));
								post.setRecruiter(getRecruiter(uid));
								postList.add(post);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postList;
	}
	
	/**
	 * Insert the application record into the database
	 */
	public int insertApply(int pid, int uid) {
		int aid = -1;
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.findApply())) {
			pstmt.setInt(1, pid);
			pstmt.setInt(2, uid);
			try (ResultSet set = pstmt.executeQuery()) {
				if (set.next())
					return -1;
				else {
					try (PreparedStatement pstmt1 = conn.prepareStatement(cmd.insertApply())){
						pstmt1.setInt(1, pid);
						pstmt1.setInt(2, uid);
						pstmt1.executeUpdate();
						aid = 1;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aid;	
	}
	
	/**
	 * Get the applicant list for a specific job post 
	 */
	public List<Seeker> getApply(int pid) {
		List<Seeker> seekerList = new ArrayList<Seeker>();
		try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(cmd.getApplyById())) { 
			pstmt.setInt(1, pid);
			try (ResultSet set = pstmt.executeQuery()) {
				while (set.next()) {
					Seeker seeker = getSeeker(set.getInt(1));
					seekerList.add(seeker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return seekerList;
	}
}
