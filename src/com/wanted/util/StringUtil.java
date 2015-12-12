// This class is used to generate the MD5 hashcode and return the image folder
package com.wanted.util;

public class StringUtil {

	public String MD5(String str) {
	   try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(str.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
	
	public String getImageFolder() {
		return "D:\\tomcat\\webapps\\java_jsphdev_Project2s\\images";
	}
}
