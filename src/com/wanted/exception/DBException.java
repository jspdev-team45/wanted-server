// The custom exception class that deals with database exception
package com.wanted.exception;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wanted.exception.ErrType;

public class DBException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String logFilename = "src/log/logfile";
	private int errNo;
	private String errMsg;
	
	public DBException(ErrType type) {
		setErrNo(type.ordinal());
		setErrMsg(type.toString());
	}
	
	public int getErrNo() {
		return errNo;
	}

	public void setErrNo(int errNo) {
		this.errNo = errNo;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public void handle() {
		switch (errNo) {
		case 0:
			log("Write duplicate records into database.");
			break;
		default:
			log("Unknow error.");
		}
	}
	
	/**
	 * Write the information to a log file
	 */
	private void log(String message) {
		try {
			FileWriter file = new FileWriter(logFilename, true);
			BufferedWriter writer = new BufferedWriter(file);
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String dateString = dateFormat.format(now);
			
			writer.write(dateString + " ");
			writer.write(message);
			writer.write("\n");
			writer.flush();
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
