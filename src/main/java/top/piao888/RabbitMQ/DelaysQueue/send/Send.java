package top.piao888.RabbitMQ.DelaysQueue.send;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.impl.AMQImpl;
import sun.misc.Launcher;
import top.piao888.RabbitMQ.DelaysQueue.recv.Recv;
import top.piao888.RabbitMQ.Utill.ConnectionUtil;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 本包虽然是讲的延迟队列，但是其原理是运用死信队列。
 * 当普通队列中的消息过期后，依然没有消费者消费。
 * 那么这条消息就变成了死信，他将会被移入死信队列。
 * 然后我们建立一个专门的消费者，去读取死信队列中的消息，这样就达到了一个延迟队列的效果
 * <p>
 * 但是这样 原生的方案 是有问题的：
 * 那么，如何设置这个TTL值呢？有两种方式， 将消息过期时间设置为6s
 * 第一种：
 * 是在创建队列的时候设置队列的“x-message-ttl”属性，如下：
 * Map<String, Object> args = new HashMap<String, Object>();
 * args.put("x-message-ttl", 6000);
 * channel.queueDeclare(queueName, durable, exclusive, autoDelete, args);
 * 第二种：
 * 针对每条消息设置TTL，代码如下：
 * AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
 * builder.expiration("6000");
 * AMQP.BasicProperties properties = builder.build();
 * channel.basicPublish(exchangeName, routingKey, mandatory, properties, "msg body".getBytes());
 * <p>
 * 但这两种方式是有区别的，如果设置了队列的TTL属性，那么一旦消息过期，就会被队列丢弃，
 * 而第二种方式，消息即使过期，也不一定会被马上丢弃，因为消息是否过期是在即将投递到消费者之前判定的，
 * 如果当前队列有严重的消息积压情况，则已过期的消息也许还能存活较长时间。
 *
 * @Author： hongzhi.xu
 * @Date: 2021/11/17 10:45 下午
 * @Version 1.0
 */
public class Send {
    static final String EXCHANGE = "base_exchange";
    static final String common = "common_query";
    static final String common_routing_key = "comm_query_key";
    static final String deadLetter = "dead_letter_query";
    static final String deadLetter_routing_key = "dead_letter_query_key";
    static Connection connection;
    static Channel channel;

    static {
        // 获取到连接以及mq通道
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE, "topic", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        commonQueue();
        deadLetterQueue();
        bind();
    }


    public static AMQP.Queue.DeclareOk commonQueue() {
        try {
            Map<String, Object> args = new HashMap<String, Object>();
            //如果消息变为死信，那么就将消息发送到 生命好的死信队列
            args.put("x-dead-letter-exchange", EXCHANGE);
            args.put("x-dead-letter-routing-key", deadLetter_routing_key);
//            args.put("x-message-ttl", 6000);
            //exclusive 此队列排他性
            return channel.queueDeclare(common, true, true, false, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static AMQP.Queue.DeclareOk deadLetterQueue() {
        try {
            Map<String, Object> arguments = new HashMap<String, Object>();
            //exclusive 此队列排他性
            return channel.queueDeclare(deadLetter, true, false, false, arguments);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void bind() {
        try {
            channel.queueBind(common, EXCHANGE, common_routing_key);
            channel.queueBind(deadLetter, EXCHANGE, deadLetter_routing_key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        Class c = Recv.class;
        c.newInstance();
//        ClassLoader classLoader = c.getClassLoader();
//        classLoader.loadClass(c.getName());

        //验证死信队列实现延迟队列的缺陷
        //先发送一个6秒的消息
        int six = 10;
        AMQP.BasicProperties.Builder sixBuilder = new AMQP.BasicProperties.Builder();
        sixBuilder.expiration(six + "000");
        AMQP.BasicProperties sixProperties = sixBuilder.build();
        channel.basicPublish(EXCHANGE, common_routing_key, true, sixProperties, (six + "秒的消息").getBytes());
        System.out.println(six + "秒的消息，发送成功");

        //再发送一个三秒的消息，但是处理者只会处理6秒，三秒的在他发送时，会判断该消息已经失效所以不会被消费
        int three = 3;
        AMQP.BasicProperties.Builder threeBuilder = new AMQP.BasicProperties.Builder();
        threeBuilder.expiration(three + "000");
        AMQP.BasicProperties threeProperties = threeBuilder.build();
        channel.basicPublish(EXCHANGE, common_routing_key, true, threeProperties, (three + "秒的消息").getBytes());
        System.out.println(three + "秒的消息，发送成功");

        System.out.println("理论上 3秒的消息先被消费者消费");
        System.out.println("但是实际上，他会按照顺序，等十秒过期后，才去判断3秒的消息是否过期，移入死信队列，这样在常规业务流程中肯定是不行的");
    }
}
