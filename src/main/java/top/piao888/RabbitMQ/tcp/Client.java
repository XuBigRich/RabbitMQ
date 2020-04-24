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
