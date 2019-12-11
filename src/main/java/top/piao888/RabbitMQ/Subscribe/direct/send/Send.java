package top.piao888.RabbitMQ.Subscribe.direct.send;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;


/*	1、1个生产者，多个消费者
	2、每一个消费者都有自己的一个队列
	3、生产者没有将消息直接发送到队列，而是发送到了交换机
	4、每个队列都要绑定到交换机
	5、生产者发送的消息，经过交换机，到达队列，实现，一个消息被多个消费者获取的目的
	注意：一个消费者队列可以有多个消费者实例，只有其中一个消费者实例会消费(但是如果有很多个消费队列 就可以将一条信息分发给多个消费者去处理了)
	*/
public class Send {
	//start
    private final static String EXCHANGE_NAME = "test_exchange_fanout";
    //end
    public static void main(String[] argv) throws Exception {
        // 获取到连接以及mq通道
        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        // 声明exchange  “fanout"是交换机类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 消息内容
        String message = "Hello World!";
        channel.basicPublish(EXCHANGE_NAME, "a", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
