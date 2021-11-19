package top.piao888.RabbitMQ.DelaysQueue.threeplugins;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import top.piao888.RabbitMQ.Utill.ConnectionUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个延时队列需要依赖第三方插件实现，可以去rabbitmq官网下载rabbitmq_delayed_message_exchange-3.9.0.ez 插件
 * 
 * @Author： hongzhi.xu
 * @Date: 2021/11/18 10:44 下午
 * @Version 1.0
 */
public class Send {
    static final String EXCHANGE = "delays_exchange";
    static final String delaysQuery = "delays_query";
    static final String delays_query_routing_key = "delays_query_query_key";
    static Connection connection;
    static Channel channel;

    static {
        // 获取到连接以及mq通道
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            //创建一个支持延迟的路由
            Map<String, Object> args = new HashMap<>();
            args.put("x-delayed-type", "direct");
            channel.exchangeDeclare(EXCHANGE, "x-delayed-message", true, false, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        delaysQueueQueue();
        bind();
    }

    /**
     * 这就是一个普通队列
     */
    public static void delaysQueueQueue() {
        try {
            channel.queueDeclare(delaysQuery, true, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bind() {
        try {
            channel.queueBind(delaysQuery, EXCHANGE, delays_query_routing_key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {
        Class c = Recv.class;
        c.newInstance();
        //验证死信队列实现延迟队列的缺陷
        //先发送一个6秒的消息
        long six = 6000;
        byte[] sixMessageBodyBytes = "six second delayed".getBytes("UTF-8");
        Map<String, Object> sixHeaders = new HashMap<String, Object>();
        sixHeaders.put("x-delay", six);
        AMQP.BasicProperties.Builder sixProps = new AMQP.BasicProperties.Builder().headers(sixHeaders);
        channel.basicPublish(EXCHANGE, delays_query_routing_key, sixProps.build(), sixMessageBodyBytes);
        System.out.println(six + "毫秒的消息，先发送成功");

        //再发送一个三秒的消息，但是处理者只会处理6秒，三秒的在他发送时，会判断该消息已经失效所以不会被消费
        long three = 3000;
        byte[] threeMessageBodyBytes = "three second delayed".getBytes("UTF-8");
        Map<String, Object> threeHeaders = new HashMap<String, Object>();
        threeHeaders.put("x-delay", three);
        AMQP.BasicProperties.Builder threeProps = new AMQP.BasicProperties.Builder().headers(threeHeaders);
        channel.basicPublish(EXCHANGE, delays_query_routing_key, threeProps.build(), threeMessageBodyBytes);
        System.out.println(three + "毫秒的消息，后发送成功");

    }
}
