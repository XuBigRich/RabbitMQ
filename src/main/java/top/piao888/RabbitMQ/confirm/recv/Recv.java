package top.piao888.RabbitMQ.confirm.recv;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class Recv {

	private final static String QUEUE_NAME = "test_queue_confirm";

	public static void main(String[] argv) throws Exception {
		// 获取到连接以及mq通道
		Connection connection = ConnectionUtil.getConnection();
		// 从连接中创建通道
		Channel channel = connection.createChannel();
		// 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 消息堵塞在这
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			// 当收到消息后 触发这个功能
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String msg = new String(body, "utf-8");
				System.out.println(msg);
			}
		};
		// 监听队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
	}
}
