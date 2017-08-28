package ba.restinterface;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

public class Consumer {
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		String line = sc.nextLine();
		Properties prop = new Properties();
		
		prop.put("client.id", "line");
		prop.put("bootstrap.servers", "localhost:9092");
		prop.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		prop.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		org.apache.kafka.clients.consumer.Consumer<String, String> consumer = new KafkaConsumer<>(prop);
		consumer.subscribe(Arrays.asList(line));
		try {
			while (!line.equals("stop")) {
				ConsumerRecords<String, String> rec = consumer.poll(100);
				for (ConsumerRecord<String, String> recs : rec){
					System.out.println(recs.value());
				}
			}
		} catch (WakeupException e) {
			// TODO: handle exception
		}finally {
			consumer.close();
			sc.close();
		}

	}
}
