package top.piao888.RabbitMQ.Work.recv;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.QueueingConsumer;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class Recv {

	private final static String QUEUE_NAME = "test_queue_work";

	public static void main(String[] argv) throws Exception {

		// 获取到连接以及mq通道
		Connection connection = ConnectionUtil.getConnection();
		final Channel channel = connection.createChannel();

		// 声明队列
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);

		// 同一时刻服务器只会发一条消息给消费者
		channel.basicQos(1);

		// 定义队列的消费者
		DefaultConsumer consumer = new DefaultConsumer(channel) {
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException{
				String msg;
				try {
					msg = new String(body, "utf-8");
					System.out.println(msg);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally {
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		// 监听队列，false表示手动返回完成状态，true表示自动
		channel.basicConsume(QUEUE_NAME, false, consumer);

	}
}
