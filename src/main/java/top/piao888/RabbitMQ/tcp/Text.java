package top.piao888.RabbitMQ.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Text {
    public static void main(String[] args) throws IOException {
        ServerSocket socket=new ServerSocket(8888);
        Socket s=socket.accept();
        InputStream inputStream=s.getInputStream();
        StringBuffer stringBuffer=new StringBuffer();
        while ((inputStream.read()) !=-1) {
            byte[] temp = new byte[1024];
            inputStream.read(temp,0,temp.length);
            String str=new String(temp);
            stringBuffer.append(str);
            System.out.println(str);
        }
        System.out.println(stringBuffer);
    }
}
