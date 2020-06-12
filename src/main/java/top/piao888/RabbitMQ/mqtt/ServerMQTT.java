package top.piao888.RabbitMQ.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * 发送消息
 */
public class ServerMQTT {
    //tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://47.104.92.214:1883";
    //    public static final String HOST = "tcp://localhost:1883";
//    public static final String HOST = "tcp://192.168.1.114:1883";
    //定义一个主题
    public static final String TOPIC = "top";
    //往taxt主题上面发送消息
//    public static final String TOPIC = "taxt";
    //定义MQTT的ID，可以在MQTT服务配置中指定
    //MQTT建立一个名为server的队列
    private static final String clientid = "server";

    private MqttClient client;
    private MqttTopic topic11;
    //    private String userName = "test";  //非必须
//    private String passWord = "test";  //非必须
    private String userName = "BigRich";  //非必须
    private String passWord = "123";  //非必须
    private MqttMessage message;

    /**
     * 构造函数
     *
     * @throws MqttException
     */
    public ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为以内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    /**
     * 用来连接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);

            topic11 = client.getTopic(TOPIC);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException,
            MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! "
                + token.isComplete());

    }

    /**
     * 启动入口
     *
     * @param args
     * @throws MqttException
     */
    public static void main(String[] args) throws MqttException, InterruptedException {
        try {
            ServerMQTT server = new ServerMQTT();
            server.message = new MqttMessage();
            server.message.setQos(2);  //保证消息能到达一次
            server.message.setRetained(true);
            server.message.setPayload("abcde1".getBytes());
//        server.publish(server.topic11 , server.message);
//        Thread.sleep(300000);
            Thread.sleep(2000);
            server.message.setPayload("abcde2".getBytes());
//        server.publish(server.topic11 , server.message);
            int i = 0;
            for (; ; ) {
//            String message = "{\"DevNum\":1,\"SN_\":\"51911220010255\",\"Datetime\":1588993563,\"RealTime-Data\":{\"Ua\":220.1,\"Ub\":0,\"Uc\":0,\"Ia\":0.04,\"Ib\":0.04,\"Ic\":0.05,\"In\":0.08,\"P\":158.98,\"Q\":0,\"PF\":1,\"Wh\":20.67,\"Varh\":2.77,\"Ta\":6503.6,\"Tb\":6503.6,\"Tc\":6503.6,\"FWh\":19.08,\"BWh\":1954}} ";
//
                String message = "{\"DevNum\":1,\"SN_\":\"51911220010256\",\"Datetime\":1588993563,\"RealTime-Data\":{\"Ua\":220.1,\"Ub\":0,\"Uc\":0,\"Ia\":0.04,\"Ib\":0.04,\"Ic\":0.05,\"In\":0.08,\"P\":185.0,\"Q\":0,\"PF\":1,\"Wh\":20.67,\"Varh\":2.77,\"Ta\":6503.6,\"Tb\":6503.6,\"Tc\":6503.6,\"FWh\":19.08,\"BWh\":1954}} ";
                String message1 = "{\"DevNum\":2,\"SN_\":\"51911220010256\",\"Datetime\":1588993563,\"RealTime-Data\":{\"Ua\":220.1,\"Ub\":0,\"Uc\":0,\"Ia\":0.04,\"Ib\":0.04,\"Ic\":0.05,\"In\":0.08,\"P\":50,\"Q\":0,\"PF\":1,\"Wh\":20.67,\"Varh\":2.77,\"Ta\":6503.6,\"Tb\":6503.6,\"Tc\":6503.6,\"FWh\":19.08,\"BWh\":1954}} ";

                server.message.setPayload(message.getBytes());
                server.publish(server.topic11, server.message);
                server.message.setPayload(message1.getBytes());
                server.publish(server.topic11, server.message);
                i++;
                if (i == 100000) {
                    break;
                }
            }
        } catch (ArithmeticException e) {
        }
        System.exit(0);
//        System.out.println(server.message.isRetained() + "------ratained状态");
    }
}