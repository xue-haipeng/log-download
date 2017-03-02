package com.example.utils;

import com.example.domain.Response;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class JschSshUtil {
	private static final Logger logger = LoggerFactory.getLogger(JschSshUtil.class);
	private static final int PORT = 22;
	private static JSch jsch = null;
	private static Session session = null;
	private static Channel channel = null;
	private static InputStream inputStream = null;
	private static OutputStream outputStream = null;
	public static String out = null;

	public static void sshConn(String ip, String username, String passwd, List<String> cmd, SimpMessagingTemplate simpMessagingTemplate) throws Exception {

		jsch = new JSch();
		session = jsch.getSession(username, ip, PORT);

		if (session == null) {
			throw new Exception("Unable to create session ...");
		}

		session.setPassword(passwd);
		session.setConfig("StrictHostKeyChecking", "no");
		session.connect(5000);

		try {
			channel = (Channel) session.openChannel("shell");
			channel.connect(1000);

			inputStream = channel.getInputStream();
			outputStream = channel.getOutputStream();

			for (String command : cmd) {
				command += "\n";
				outputStream.write(command.getBytes());
				outputStream.flush();

				boolean flag = true;
				int count = 0;
				int stuck = 0;
				while (flag) {
					Thread.sleep(200);
					if (inputStream.available() == count) {
						stuck += 1;
					}
					if (stuck > 50) {
						logger.warn("*********  TIMEOUT  ***************");
						break;
					}
					count = inputStream.available();
					int offset = 0;
					if (count > 0) {
						byte[] data = new byte[count];
						int nLen = inputStream.read(data);

						if (nLen < 0) {
							throw new Exception("Network Error, Unable to Get InputStream ...");
						}
						out = new String(data, offset, nLen - offset, "UTF-8");
						if (out.endsWith("]# ") || out.endsWith("]$ ")) {
							flag = false;
						}
						if (simpMessagingTemplate != null) {
							simpMessagingTemplate.convertAndSend("/topic/output", new Response(
									out.replaceAll(System.getProperty("line.separator"), "<br/>").replaceAll(" ", "&nbsp;")
							.replaceAll("\t", "&emsp;").replaceAll("\u001B\\[[\\d;]*[^\\d;]","")));
						}
						System.out.println(out);
					}
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
