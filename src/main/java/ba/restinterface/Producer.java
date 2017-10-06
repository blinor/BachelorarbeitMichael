package ba.restinterface;

import org.apache.kafka.clients.producer.*;

//import java.io.BufferedInputStream;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
import java.util.Properties;

/**
 * @author Michael Jahns
 * This is the MainClass which will set the Configurations. 
 * It will initialize  the Config an manage the Threads 
 */
public class Producer {
	final static int maxthreads = 100;
	long programStartTime;

	public static void main(String[] args) throws Exception {
		Producer p = new Producer();
		p.push();

	}

	/**
	 * @throws Exception Will catch  InterruptedException when the Thread tries to sleep
	 * 
	 * Here we read the Configfile and start and stop the Threads.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public void push() throws Exception {

		programStartTime = System.currentTimeMillis();
		Thread[] threads = new Thread[maxthreads];
		String topic = Config.INSTANCE.getTopic();
		String server = Config.INSTANCE.getKafkaHost() + ":" + Config.INSTANCE.getKafkaPort();

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
		props.put(ProducerConfig.RETRIES_CONFIG, "3");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 200);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.ByteArraySerializer");
		String[] oldInput = new String[0];
		while (true) {

			String[] urlInput = Config.INSTANCE.getUrl().split(",");
			if (oldInput.length >= urlInput.length) {
				for (int i = 0; i < oldInput.length; i++) {
					int j = i;
					if (urlInput.length < (j + 1)) {
						threads[i].stop();
						System.out.println("Deleted Thread " + i);
					} else if (!oldInput[i].equals(urlInput[i])) {
						threads[i].stop();

						String[] input = urlInput[i].split(";");
						threads[i] = new Thread(new GetData(props, topic, input[0], input[1], input[2]));
						threads[i].start();
						System.out.println("Updated Thread " + i + " with " + urlInput[i]);
					}
				}

			} else {
				for (int i = oldInput.length; i < urlInput.length; i++) {
					System.out.println(urlInput[i]);
					String[] input = urlInput[i].split(";");
					threads[i] = new Thread(new GetData(props, topic, input[0], input[1], input[2]));
					threads[i].start();
				}
			}
			oldInput = urlInput;
			Thread.sleep(60000);
		}
	}
}
