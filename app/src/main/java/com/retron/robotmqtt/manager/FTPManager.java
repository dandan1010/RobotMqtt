package com.retron.robotmqtt.manager;

import android.content.Context;

import com.retron.robotmqtt.mqtt.MqttHandleMsg;
import com.retron.robotmqtt.utils.SharedPrefUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FTPManager {
    private Context mContext;
    public FTPClient ftpClient = null;
    public static FTPManager ftpManager;

    public FTPManager(Context mContext) {
        this.mContext = mContext;
        ftpClient = new FTPClient();
    }

    public static FTPManager getInstance(Context context) {
        if (ftpManager == null) {
            synchronized (SharedPrefUtil.class) {
                if (ftpManager == null) {
                    ftpManager = new FTPManager(context);
                }
            }
        }
        return ftpManager;
    }

    // 连接到ftp服务器
    public synchronized boolean connect() {
        boolean bool = false;
        if (ftpClient.isConnected()) {//判断是否已登陆
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ftpClient.setDataTimeout(20000);//设置连接超时时间
        ftpClient.setControlEncoding("utf-8");
        ftpClient.enterLocalPassiveMode();
        try {
            ftpClient.connect("58.240.254.188", 10021);
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                if (ftpClient.login("njdc", "njdc")) {
                    bool = true;
                    System.out.println("ftp连接成功");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //createDirectory();
        return bool;
    }

    // 创建文件夹
    public boolean createDirectory(String path) {
        boolean bool = false;
        String directory = path.substring(0, path.lastIndexOf("/") + 1);
        int start = 0;
        int end = 0;
        if (directory.startsWith("/")) {
            start = 1;
        }
        end = directory.indexOf("/", start);
        while (true) {
            String subDirectory = directory.substring(start, end);
            try {
                if (!ftpClient.changeWorkingDirectory(subDirectory)) {
                    ftpClient.makeDirectory(subDirectory);
                    ftpClient.changeWorkingDirectory(subDirectory);
                    bool = true;
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                if (end == -1) {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bool;
    }

    // 实现上传文件的功能
    public synchronized boolean uploadFile(String localPath, String serverPath) {
        // 上传文件之前，先判断本地文件是否存在
        File localFile = new File(localPath);
        if (!localFile.exists()) {
            System.out.println("本地文件不存在");
            return false;
        }
        createDirectory(serverPath); // 如果文件夹不存在，创建文件夹
        System.out.println("服务器文件存放路径：" + serverPath + localFile.getName());
        String fileName = localFile.getName();
        // 如果本地文件存在，服务器文件也在，上传文件，这个方法中也包括了断点上传
        long localSize = localFile.length(); // 本地文件的长度
        FTPFile files;
        try {
            System.out.println("服务器文件----aaaaa" + fileName);
            files = ftpClient.mlistFile(fileName);
            long serverSize = 0;
            if (files == null || files.getSize() == 0) {
                System.out.println("服务器文件不存在");
                serverSize = 0;
            } else {
                System.out.println("服务器文件存在,删除文件," + fileName);
                if (ftpClient.deleteFile(fileName)) {
                    System.out.println("服务器文件存在,删除文件,开始重新上传");
                    serverSize = 0;
                }
//                serverSize = files[0].getSize(); // 服务器文件的长度
            }
//            if (localSize <= serverSize) {
//                if (ftpClient.deleteFile(fileName)) {
//                    System.out.println("服务器文件存在,删除文件,开始重新上传");
//                    serverSize = 0;
//                }
//            }
            RandomAccessFile raf = new RandomAccessFile(localFile, "r");
            // 进度
            long step = localSize / 100;
            long process = 0;
            long currentSize = 0;
            // 好了，正式开始上传文件
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setRestartOffset(serverSize);
            raf.seek(serverSize);
            OutputStream output = ftpClient.appendFileStream(fileName);
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = raf.read(b)) != -1) {
                output.write(b, 0, length);
                currentSize = currentSize + length;
                if (currentSize / step != process) {
                    process = currentSize / step;
                    if (process % 10 == 0) {
                        System.out.println("上传进度：" + process);
                    }
                }
            }
            output.flush();
            output.close();
            raf.close();
            if (ftpClient.completePendingCommand()) {
                System.out.println("文件上传成功");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("文件上传失败");

        return false;
    }

    // 实现下载文件功能，可实现断点下载
    public synchronized boolean downloadFile(String localPath, String serverPath, String fileName) {
        // 先判断服务器文件是否存在
        FTPFile files;
        try {
            files = ftpClient.mlistFile(serverPath);
            //files = ftpClient.listFiles(serverPath);
            if (files == null || files.getSize() == 0) {
                System.out.println("服务器文件不存在");
                return false;
            }
            System.out.println("远程文件存在,名字为：" + files.getName());
            // 接着判断下载的文件是否能断点下载
            long serverSize = files.getSize(); // 获取远程文件的长度
            System.out.println("文件长度 " + serverSize);
            File localFile = new File(localPath);
            long localSize = 0;
            if (localFile.exists()) {
                localSize = localFile.length(); // 如果本地文件存在，获取本地文件的长度
                if (localSize >= serverSize) {
                    System.out.println("文件已经下载完了");
                    File file = new File(localPath);
                    file.delete();
                    System.out.println("本地文件存在，删除成功，开始重新下载");
                }
            }
            // 进度
            long step = serverSize / 100;
            long process = 0;
            long currentSize = 0;
            // 开始准备下载文件
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            localFile = new File(localPath);
            OutputStream out = new FileOutputStream(localFile, true);
            ftpClient.setRestartOffset(0);
            System.out.println("远程文件地址 " + serverPath);
            ftpClient.enterLocalPassiveMode();
            System.out.println("远程文件流地址" + ",  " + serverPath);
            InputStream input = ftpClient.retrieveFileStream(serverPath);
            System.out.println("远程文件流大小 " + input.available());
            byte[] b = new byte[1024];
            int length = 0;
            while ((length = input.read(b)) != -1) {
                out.write(b, 0, length);
                currentSize = currentSize + length;
                if (currentSize / step != process) {
                    process = currentSize / step;
                    if (process % 10 == 0) {
                        System.out.println("下载进度：" + process);
                    }
                }
            }
            out.flush();
            out.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (ftpClient.completePendingCommand()) {
                System.out.println("文件下载成功");
                MqttHandleMsg.downloadMapResult(fileName);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("文件下载失败");
        return false;
    }

    // 如果ftp上传打开，就关闭掉
    public void closeFTP() {
        if (ftpClient.isConnected()) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
