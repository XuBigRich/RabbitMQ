package top.piao888.RabbitMQ.confirm.send;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import top.piao888.RabbitMQ.Utill.ConnectionUtil;

public class Asynchronous {
	private final static String QUEUE_NAME = "test_queue_confirm3";
	public static void main(String[] args) throws Exception {
		Connection connection=ConnectionUtil.getConnection();
		Channel channel=connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		//生产者调用confirmSelect 将channel设置为confirm模式   
		// 	注意：如果已经设置为 事务 模式 就不可以设置为confirm模式  如果这样干了 就会报异常
		channel.confirmSelect();
		//未开启的消息标识
		final SortedSet<Long> confirmSet=Collections.synchronizedSortedSet(new TreeSet<Long>());
		
		//通道添加监听
		channel.addConfirmListener(new ConfirmListener() {
			//没有问题的handleAck   multple表示 是否是多个  如果是多个 就批量 删除了
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				if(multiple) {
					System.out.println("----handleAck--multiple");
					confirmSet.headSet(deliveryTag+1).clear();
				}else {
					System.out.println("----handleAck--multiple");
					confirmSet.remove(deliveryTag);
				}
			}
			//如果失败了  就做相应业务  例如：失败重发 等操作
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				
			}
		});
		
		String message ="Hello World!";
		int a=0;
		for(;a<5;a++) {
			long seqNo=channel.getNextPublishSeqNo();
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			while(a==4) {
				int b=1/0;
			}
			confirmSet.add(seqNo);
		}
		channel.close();
		connection.close();
	}
}
