package com.example.utils;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JschSftpUtil {

	private static final int PORT = 22;
	private static String lastLine = null;

	public static void sftp(String ip, String username, String passwd, String remoteFile) {

		try {
			JSch jsch = new JSch();
			Session session = jsch.getSession(username, ip, PORT);
			session.setPassword(passwd);
			session.setConfig("StrictHostKeyChecking", "no");
			System.out.println("Establishing Connection...");
			session.connect();
			System.out.println("Connection established.");
			System.out.println("Creating SFTP Channel.");
			ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
			sftpChannel.connect();
			System.out.println("SFTP Channel created.");
			InputStream out = null;
			out = sftpChannel.get(remoteFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(out));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.contains("successfully")) {
					String dateStr = line.substring(27, 55);
					System.out.println(dateStr);
					Date today = new Date();
					Date date = new Date();
					SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
					try {
						date = format.parse(dateStr);
						if (date.after(getPreviousDate(today))) {
							lastLine = "succeed";
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if (line.contains("ended in error")) {
					lastLine = "failed";
				}
			}
			br.close();
			sftpChannel.disconnect();
			session.disconnect();
		} catch (JSchException | SftpException | IOException e) {
			System.out.println(e);
		}
		if (lastLine == null || "".equals(lastLine)) {
			lastLine = "unknown";
		}
	}

	public static String getLastLine() {
		return lastLine;
	}

	public static Date getPreviousDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

}