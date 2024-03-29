package top.piao888.RabbitMQ.OneVsOne.send;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class Send {

    private final static String QUEUE_NAME = "q_test_01";

    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        // 从连接中创建通道
        Channel channel = connection.createChannel();

        // 声明（创建）队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 消息内容
        String message = "Hello World!";
        while(true){
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            if(false){
                break;
            }
        }
        System.out.println(" [x] Sent '" + message + "'");
        //关闭通道和连接
        channel.close();
        connection.close();
    }
}
