package com.lcb.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class BIOServer {

    public static void main(String[] args) throws Exception {

        //线程池机制

        //1创建一个线程池
        //2、有客户端连接，就创建一个线程池  与之通讯
        ExecutorService newCachedThreadPool= Executors.newCachedThreadPool();

        //创建ServerSocket
        ServerSocket serverSocket=new ServerSocket(6666);

        System.out.println("服务器端启动了");

        while (true){
            //监听 等待客户端连接
            final Socket socket=serverSocket.accept();
            System.out.println("连接到一个客户端");

            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    //可以和客户带通讯

                    handler(socket);
                }
            });
        }

    }


    //编写一个hander方法 和客户端tongxun
    public static void handler(Socket socket){
        byte[] bytes = new byte[1024];

        //通过Socket获取输入流
        try {
            InputStream inputStream = socket.getInputStream();

            //循环读取客户端发送的数据
            while (true){
                int read = inputStream.read(bytes);
                if(read!=-1){
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("关闭与客户端的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
