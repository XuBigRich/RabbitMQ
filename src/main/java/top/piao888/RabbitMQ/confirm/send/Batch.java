package top.piao888.RabbitMQ.confirm.send;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class Batch {
	private final static String QUEUE_NAME = "test_queue_confirm";
	public static void main(String[] args) throws Exception {
		Connection connection=ConnectionUtil.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//生产者调用confirmSelect 将channel设置为confirm模式   
		// 	注意：如果已经设置为 事务 模式 就不可以设置为confirm模式  如果这样干了 就会报异常
		channel.confirmSelect();
		String message ="Hello World!";
		int a=0;
		for(;a<5;a++) {
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			while(a==4) {
				int b=1/0;
			}
		}
		
		if(!channel.waitForConfirms()) {
			System.out.println("message send fail");
		}else {
			System.out.println("ok");
		}
		channel.close();
		connection.close();
	}
}
