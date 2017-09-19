package ba.restinterface;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONObject;

public class CsvReader {
	org.apache.kafka.clients.producer.Producer<String, Object> producer;
	String topic;

	public CsvReader(Properties prop, String topic) {
		producer = new KafkaProducer<>(prop);
		this.topic = topic;
	}

	public void sendData(String file) {
		long timeRequest = System.currentTimeMillis();
		ValueMapper vm = new ValueMapper();
		ArrayList<String> data = new ArrayList<String>();
		String line;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				data.add(line);
			}
		} catch (IOException e) {
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (int i = 1; i < (data.size() / 10); i++) {

			JSONObject object = vm.mapValues(data.get(i), timeRequest);
			
			byte[] bytes = null;
			try {
				bytes = object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			producer.send(new ProducerRecord<String, Object>(topic, bytes));
			producer.flush();
			System.out.println("Send!" + object);

		}
		System.out.println("Finished");
		producer.close();
	}

}
