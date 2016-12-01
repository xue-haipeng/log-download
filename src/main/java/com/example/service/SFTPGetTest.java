package com.example.service;

import com.jcraft.jsch.*;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Xue on 11/29/16.
 */

public class SFTPGetTest {
    public SFTPChannel getSFTPChannel() {
        return new SFTPChannel();
    }

    public static void sftpDownload(String ip, String username, String password, String logPath) throws Exception {
        SFTPGetTest test = new SFTPGetTest();
        Map<String, String> sftpDetails = new HashMap<String, String>();
        // 设置主机ip，端口，用户名，密码
        sftpDetails.put(SFTPConstants.SFTP_REQ_HOST, ip);
        sftpDetails.put(SFTPConstants.SFTP_REQ_USERNAME, username);
        sftpDetails.put(SFTPConstants.SFTP_REQ_PASSWORD, password);
        sftpDetails.put(SFTPConstants.SFTP_REQ_PORT, "22");

        SFTPChannel channel = test.getSFTPChannel();
        ChannelSftp chSftp = channel.getChannel(sftpDetails, 60000);

        SftpATTRS attr = chSftp.stat(logPath);
        long fileSize = attr.getSize();

        String logName = logPath.substring(logPath.lastIndexOf("/")+1);
        String dst = "/oracle/logs/" + logName;
        OutputStream out = new FileOutputStream(dst);
        try {

            chSftp.get(logPath, dst, new FileProgressMonitor(fileSize)); // 代码段1
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            chSftp.quit();
            channel.closeChannel();
        }
    }
}
