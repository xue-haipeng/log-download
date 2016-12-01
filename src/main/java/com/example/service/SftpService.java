package com.example.service;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

/**
 * Created by Xue on 11/30/16.
 */
public class SftpService {
    /**
     * 利用JSch包实现SFTP下载、上传文件
     * @param ip 主机IP
     * @param user 主机登陆用户名
     * @param psw  主机登陆密码
     * @param port 主机ssh2登陆端口，如果取默认值，传-1
     */
    public static void sshSftp(String ip, String user, String psw ,int port) throws Exception{
        Session session = null;
        Channel channel = null;

        JSch jsch = new JSch();

        if(port <=0){
            //连接服务器，采用默认端口
            session = jsch.getSession(user, ip);
        }else{
            //采用指定的端口连接服务器
            session = jsch.getSession(user, ip ,port);
        }

        //如果服务器连接不上，则抛出异常
        if (session == null) {
            throw new Exception("session is null");
        }

        //设置登陆主机的密码
        session.setPassword(psw);//设置密码
        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");
        //设置登陆超时时间
        session.connect(30000);

        try {
            //创建sftp通信通道
            channel = (Channel) session.openChannel("sftp");
            channel.connect(1000);
            ChannelSftp sftp = (ChannelSftp) channel;

            //进入服务器指定的文件夹
            sftp.cd("domains");

            //列出服务器指定的文件列表
            Vector v = sftp.ls("*.txt");
            for(int i=0;i<v.size();i++){
                System.out.println(v.get(i));
            }

            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了
            OutputStream outputStream = sftp.put("1.txt");
            InputStream inputStream = new FileInputStream(new File("c:/print.txt"));

            byte b[] = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.disconnect();
            channel.disconnect();
        }
    }
}
