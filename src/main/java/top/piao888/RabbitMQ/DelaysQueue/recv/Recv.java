package top.piao888.RabbitMQ.DelaysQueue.recv;

import com.rabbitmq.client.*;
import top.piao888.RabbitMQ.Utill.ConnectionUtil;

import java.io.IOException;

/**
 * @Author： hongzhi.xu
 * @Date: 2021/11/18 10:42 上午
 * @Version 1.0
 */
public class Recv {
    static final String EXCHANGE = "base_exchange";
    static final String deadLetter = "dead_letter_query";
    static Connection connection;
    static Channel channel;

    static {
        System.out.println("我被装载了");
        // 获取到连接以及mq通道
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE, "topic", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dispose();
    }

    public static void dispose() {
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            //当收到消息后 触发这个功能
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String msg = new String(body, "utf-8");
                System.out.println(msg);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        // 监听队列
        try {
            channel.basicConsume(deadLetter, false, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
