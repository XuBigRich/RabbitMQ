package top.piao888.RabbitMQ;

/**
 * Hello world!
 *
 */
public class App {
	public static int sjz=16;
    public static void main( String[] args ){
    	Integer ejz=Integer.valueOf(32);
    	Integer zhh=ejz>>4;
    	System.out.println(zhh&2);
    }
    public static byte getBitValue(byte source, int pos) {  
        return (byte) ((source >> pos) & 1);  
    }  
}
