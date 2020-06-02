package top.piao888.RabbitMQ.Subscribe.direct.send;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;


/*	
	*/
public class Send {
	//start
    private final static String EXCHANGE_NAME = "test_exchange_direct";
    //end
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange  “fanout"是交换机类型
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 消息内容
        String message = "Hello World!";
        channel.basicPublish(EXCHANGE_NAME, "error", null, message.getBytes());
        channel.basicPublish(EXCHANGE_NAME, "info", null, message.getBytes());
        channel.basicPublish(EXCHANGE_NAME, "warning", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
