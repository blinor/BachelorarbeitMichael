package ba.restinterface;

import org.apache.kafka.clients.producer.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Producer {
	final static int maxthreads = 100;
	long programStartTime;

	public static void main(String[] args) throws Exception {
		Producer p = new Producer();
		p.push();

	}

	public void push() throws Exception {
		Properties prop = new Properties();
		BufferedInputStream bi = null;
		try {
			bi = new BufferedInputStream(new FileInputStream("./src/main/java/ba/restinterface/prop.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			prop.load(bi);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] urlInput = prop.getProperty("url").split(",");
		programStartTime = System.currentTimeMillis();
		Thread[] threads = new Thread[maxthreads];
		String topic = "my.weather.lubw";
		// String topic = "test";
		// Scanner sc = new Scanner(System.in);
		String server = Config.INSTANCE.getKafkaHost() + ":" + Config.INSTANCE.getKafkaPort();
		// String server = "10.0.75.1:9092";

		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
		props.put(ProducerConfig.RETRIES_CONFIG, "3");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 200);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.ByteArraySerializer");
		for (int i = 0; i < urlInput.length; i++) {

			System.out.println(urlInput[i]);
			String[] input = urlInput[i].split(";");
			if (input[0].equals("http")) {
				threads[i] = new Thread(new GetData(props, topic, input[1], input[2], input[3]));
				threads[i].start();
			} else {
				CsvReader reader = new CsvReader(props, topic);
				reader.sendData("./src/main/java/ba/restinterface/" + input[1]);
			}
		}
	}
}
	// while ((in = sc.nextLine()) != "stop") {
	// switch (in.split(";")[0]) {
	// case ("csv"):
	// CsvReader reader = new CsvReader(props, topic);
	// if (in.split(";").length != 2) {
	// reader.sendData("src/main/resources/test.csv");
	// } else {
	// reader.sendData(in.split(";")[1]);
	// }
	// break;
	// case ("http"):
	// if (counter < maxthreads) {
	// input = in.split(";");
	// threads[counter] = new Thread(new GetData(props, topic, input[1],
	// input[2], input[3]));
	// threads[counter].start();
	// counter++;
	// } else {
	// System.out.println("MaxThreads reached");
	// }
	//
	// break;
	// case ("stop"):
	// System.out.println("Exiting");
	// for (int i = 0; i < threads.length; i++) {
	// if (threads[1] != null)
	// threads[i].stop();
	// }
	// sc.close();
	// return;
	// default:
	// System.out.println("<csv;<PathToCSV>> will push csv values to
	// kafka");
	// System.out.println(
	// "<http;<URL>;<Time to wait between pulls>;<Number of pulls(-1=∞ )>>
	// will start a new pullservice in a new thread. Max 10 Threads");
	// System.out.println("<stop> will obviously stop");
	//
	// }
	//
	// }


