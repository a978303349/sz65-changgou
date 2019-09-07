package com.changgou.util;


import com.changgou.file.FastDFSFile;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtil {
    /**
     * 文件上传 读取Tracker服务配置
     */
    static {
        try {
            //获取tracker的配置文件fdfs_client.conf的位置
            String filePath = new ClassPathResource("fdfs_client.conf").getPath();
            //加载tracker配置信息
            ClientGlobal.init(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件上传
     * @param file
     * @return
     * @throws Exception
     */
    public static String[] upload(FastDFSFile file) throws Exception {
        String[] uploadResults = null;
        try {
            //获取文件作者
            NameValuePair[] meta_list = new NameValuePair[1];
            meta_list[0]=new NameValuePair("author",file.getAuthor());

            //获取StorageClient
            StorageClient storageClient=getStorageClient();

            uploadResults = storageClient.upload_appender_file(file.getContent(), file.getExt(), meta_list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uploadResults;
    }

    /**
     * 获取文件信息
     * @param groupName 组名
     * @param remoteFileName 文件存储完整名
     * @return
     * @throws Exception
     */
    public static FileInfo getFile(String groupName,String remoteFileName) throws Exception {
        try {
            StorageClient storageClient = getStorageClient();
            return storageClient.get_file_info(groupName,remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件下载
     * @param groupName
     * @param remoteFileName
     * @return
     * @throws Exception
     */
    public static InputStream downFile(String groupName,String remoteFileName) throws Exception {
        try {
            TrackerServer trackerServer = getTrackerServer();
            //通过TrackerServer创建StorageClient
            StorageClient storageClient=new StorageClient(trackerServer,null);
            //通过StorageClient下载文件
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(fileByte);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件删除
     * @param groupName  组名
     * @param remoteFileName 文件存储完整名
     * @throws Exception
     */
    public static void deleteFile(String groupName,String remoteFileName) throws Exception {
        try {
            TrackerServer trackerServer = getTrackerServer();
            StorageClient storageClient = new StorageClient(trackerServer,null);
            //通过StorageClient删除文件
            storageClient.delete_file(groupName,remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取组名信息
     * @param groupName
     * @return
     * @throws IOException
     */
    public static StorageServer getStorages(String groupName) throws IOException {
        try {
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient获取TrackerServer对象
            TrackerServer trackerServer = trackerClient.getConnection();
            return trackerClient.getStoreStorage(trackerServer,groupName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据文件组名和文件存储路径获取Storage服务的IP、端口信息
     * @param groupName 组名
     * @param remoteFileName 文件存储完整名
     * @return
     */
    public static ServerInfo[] getServerInfo(String groupName, String remoteFileName){
        try {
            //创建TrackerClient对象
            TrackerClient trackerClient = new TrackerClient();
            //通过TrackerClient获取TrackerServer对象
            TrackerServer trackerServer = trackerClient.getConnection();
            //获取服务信息
            return trackerClient.getFetchStorages(trackerServer,groupName,remoteFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 服务地址
     * @return
     * @throws Exception
     */
    public static String getTrackerUrl() throws Exception {
        TrackerServer trackerServer = getTrackerServer();
        //获取Tracker地址
        return
                "http//"+ trackerServer.getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port();
    }

    /**
     * 获取StorageClient对象
     * @return
     * @throws Exception
     */
    private static StorageClient getStorageClient()throws Exception {
        //获取TrackerServer
        TrackerServer trackerServer=getTrackerServer();

        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     *  获取TrackerServer对象
     * @return
     */
    private static TrackerServer getTrackerServer() throws IOException {
        //创建一个TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();

        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerServer;
    }
    public static void main(String[] args) throws Exception{
        //测试获取文件信息
        //FileInfo fileInfo = getFile("group1","M00/00/00/wKjThF07w0uANa_wAAnAAJuzIB4065.jpg");
        //System.out.println(fileInfo.getSourceIpAddr());
        //System.out.println(fileInfo.getFileSize());
        //System.out.println(fileInfo.getCrc32());

        //文件下载测试
        //InputStream is = downloadFile("group1", "M00/00/00/wKjThF07w0uANa_wAAnAAJuzIB4065.jpg");
        //文件输出流对象
        //FileOutputStream os = new FileOutputStream("D:/1.jpg");
        //创建缓冲区
        //byte[] buffer = new byte[1024];
        //while (is.read(buffer)!=-1){
        //    os.write(buffer);
        //}
        //os.flush();
        //os.close();
        //is.close();

        //删除文件测试
        //deleteFile("group1", "M00/00/00/wKjThF07w0uANa_wAAnAAJuzIB4065.jpg");

        //获取Storage信息
        //StorageServer storageServer = getStorage("group1");
        //System.out.println(storageServer.getInetSocketAddress().getHostString());
        //System.out.println(storageServer.getStorePathIndex());
        //System.out.println(storageServer.getInetSocketAddress().getPort());

        //获取StorageIP和端口信息
        //ServerInfo[] serverInfos = getServerInfo("group1", "M00/00/00/wKjThF07wzGAYFi8AAXz2mMp9oM88.jpeg");
        //for (ServerInfo serverInfo : serverInfos) {
        //    System.out.println("IP:"+serverInfo.getIpAddr()+",端口号："+serverInfo.getPort());
        //}

        //获取TrackerServer请求地址
        //String trackerUrl = getTrackerUrl();
        //System.out.println(trackerUrl);
    }
}
