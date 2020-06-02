<<<<<<< HEAD
package top.piao888.RabbitMQ.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket=new Socket("127.0.0.1",8888);
        OutputStream outputStream =socket.getOutputStream();
        byte a=-0x7f;
        byte[] bytes={1,0xf,3};
        outputStream.write(bytes);
    }
}
=======
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
>>>>>>> dadfd620691837c3346020c313c4399e209a3a5f
