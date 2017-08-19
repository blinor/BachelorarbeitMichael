package ba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.apache.kafka.clients.producer.ProducerRecord;

public class CsvReader {
	org.apache.kafka.clients.producer.Producer<String, Object> producer;
	String topic;

	public CsvReader(org.apache.kafka.clients.producer.Producer<String, Object> p, String topic) {
		producer = p;
		this.topic = topic;
	}

	public void sendData(String file) {
		ArrayList<String> data = new ArrayList<String>();
		String line;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				data.add(line);
			}
		} catch (IOException e) {
			// TODO: handle exception
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
			String[] array = data.get(i).split(";");
			StringBuilder sb = new StringBuilder(array[3]);
			sb.insert(10, " ");
			sb.append(":00");
			long time = java.sql.Timestamp.valueOf(sb.toString()).getTime();
			Map<String, Object> config = null;
			// config.put("javax.json.stream.JsonGenerator.prettyPrinting",
			// Boolean.valueOf(true));
			JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
			JsonObject object = factory.createObjectBuilder().add("timestamp", time).add("station", array[1])
					.add("lat", "").add("lng", "").add("heigth", "").add("temp", array[4]).add("lux", "")
					.add("no2", "")
//					.add("so2", "").add("ozn", "")
					.build();

			byte[] bytes = null;
			try {
				bytes = object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			producer.send(new ProducerRecord<String, Object>(topic, bytes));
			producer.flush();
			System.out.println("Send!" + object);

		}
		System.out.println("Finished");

	}

}
