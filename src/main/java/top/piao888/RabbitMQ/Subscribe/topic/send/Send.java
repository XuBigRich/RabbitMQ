package top.piao888.RabbitMQ.Subscribe.topic.send;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

/*
	这个地方是 topic 类型的 交换机类型  交换键具有通配功能
	*/
public class Send {
	//start
    private final static String EXCHANGE_NAME = "test_exchange_topic";
    //end
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange  “fanout"是交换机类型
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 消息内容
        String message1 = "Good.insert";
        String message2 = "Rich.Good.insert";
        String message3 = "Rich.Good.rabbit.insert";
        String message4 = "Good.rabbit.insert";
        String message5 = "rabbit.insert";
        channel.basicPublish(EXCHANGE_NAME, "Good.insert", null, message1.getBytes());
        channel.basicPublish(EXCHANGE_NAME, "Rich.Good.insert", null, message2.getBytes());
        channel.basicPublish(EXCHANGE_NAME, "Rich.Good.rabbit.insert", null, message3.getBytes());
        channel.basicPublish(EXCHANGE_NAME, "Good.rabbit.insert", null, message4.getBytes());
        channel.basicPublish(EXCHANGE_NAME, "rabbit.insert", null, message5.getBytes());

        channel.close();
        connection.close();
    }
}
