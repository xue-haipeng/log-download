package com.example.utils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class JschSshUtil {
	private static final int PORT = 22;
	private static JSch jsch = null;
	private static Session session = null;
	private static Channel channel = null;
	private static InputStream inputStream = null;
	private static OutputStream outputStream = null;
	public static String out = null;	

	public static void sshConn(String ip, String username, String passwd, List<String> cmd) throws Exception {

		jsch = new JSch();
		session = jsch.getSession(username, ip, PORT);

		if (session == null) {
			throw new Exception("Unable to create session ...");
		}

		session.setPassword(passwd);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect(10000);

		try {
			channel = (Channel) session.openChannel("shell");
			channel.connect(1000);

			inputStream = channel.getInputStream();
			outputStream = channel.getOutputStream();

			for (String command : cmd) {
				command += "\n";
				outputStream.write(command.getBytes());
				outputStream.flush();
				Thread.sleep(500);

				if (inputStream.available() > 0) {
					byte[] data = new byte[inputStream.available()];
					int nLen = inputStream.read(data);

					if (nLen < 0) {
						throw new Exception("Network Error, Unable to Get InputStream ...");
					}
					out = new String(data, 0, nLen, "UTF-8");
					System.out.println(out);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void sshDisconn() {
		
		try {
			outputStream.close();
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			session.disconnect();
			channel.disconnect();
		}		
	}

}
