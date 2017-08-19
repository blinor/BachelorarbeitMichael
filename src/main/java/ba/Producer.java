package ba;

import org.apache.kafka.clients.producer.KafkaProducer;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.Scanner;

public class Producer {
	final static int maxthreads = 10;

	public static void main(String[] args) throws Exception {
		// if (args.length != 0) {
		// System.out.println("Enter topic name");
		// return;
		// }
		Producer p = new Producer();
		p.push();
	}

	@SuppressWarnings("deprecation")
	public void push() throws Exception {
		Thread[] threads = new Thread[maxthreads];
		int counter = 0;
		String topic = "test";
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Give Kafka Topic");
		topic = sc.nextLine();
		System.out.println("Give Bootstrap-Server");
		String server = "localhost:9092";
		server = sc.nextLine();
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
		props.put(ProducerConfig.RETRIES_CONFIG, "3");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "none");
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, 200);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.ByteArraySerializer");
		org.apache.kafka.clients.producer.Producer<String, Object> producer = new KafkaProducer<>(props);
		CsvReader reader = new CsvReader(producer, topic);
		String in = "";
		while ((in = sc.nextLine()) != "stop") {
			switch (in.split(";")[0]) {
			case ("csv"):
				if(in.split(";").length != 2){
				reader.sendData("src/main/resources/test.csv");
				}else{
					reader.sendData(in.split(";")[1]);
				}
				break;
			case ("http"):
				if (counter < maxthreads) {

					System.out.println("Please give URL;<Time in Hours to pull data>;Numbers of Pulls");

					String[] input = in.split(";");
					threads[counter] = new Thread(new GetData(props, topic, input[1], input[2], input[3]));
					threads[counter].start();
					counter++;
				} else {
					System.out.println("MaxThreads reached");
				}

				break;
			case ("stop"):
				System.out.println("Exiting");
				for (int i = 0; i < threads.length; i++) {
					if(threads[1] != null)
					threads[i].stop();
				}
				sc.close();
				producer.close();
				return;
			default:
				System.out.println("<csv;<PathToCSV>> will push csv values to kafka");
				System.out.println("<http;<URL>;<Time to wait between pulls>;<Number of pulls(-1=∞ )>> will start a new pullservice in a new thread. Max 10 Threads");
				System.out.println("<stop> will obviously stop");

			}

		}

	}
}
