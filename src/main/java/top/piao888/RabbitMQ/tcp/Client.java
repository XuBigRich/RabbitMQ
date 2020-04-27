package top.piao888.RabbitMQ.tcp;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        String s = "hello world!";
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(s.getBytes());
        outputStream.flush();
        while(true);
      /*  InputStream inputStream = socket.getInputStream();
        BufferedInputStream buffer = new BufferedInputStream(inputStream);
        byte[] bytes = new byte[1024];
        int i;
        StringBuffer stringBuffer=new StringBuffer();
        while ((i = buffer.read()) != -1) {
            buffer.read(bytes,0,bytes.length);
            stringBuffer.append(new String(bytes));
        }
        System.out.println(stringBuffer);
    */
    }
}
