package com.ja0ck5.plain;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Created by Ja0ck5 on 2017/8/21.
 */
public class PlainOioServer {

    public void serve(int port) throws IOException {
        // 将服务器绑定到制定端口
        final ServerSocket socket = new ServerSocket(port);

        for(;;){
            // 接收连接
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from :" + clientSocket);
            new Thread(() ->{
                OutputStream out;
                try {
                    out = clientSocket.getOutputStream();
                    // 消息写给已连接的客户端
                    out.write("Hi,Yesterday I said Maybe tomorrow.".getBytes(Charset.forName("UTF-8")));
                    out.flush();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

}
