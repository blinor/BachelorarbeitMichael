package ba;

import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

public class GetData implements Runnable {
	org.apache.kafka.clients.producer.Producer<String, Object> producer;
	String topic;
	String url;
	long hours;
	int number;

	public GetData(Properties prop, String topic, String url, String input, String n) {
		this.producer = new KafkaProducer<>(prop);
		this.topic = topic;
		this.url = url;
		this.hours = Long.parseLong(input) * 3600000;
		this.number = Integer.parseInt(n);
	}

	public void run() {
		while (number != 0) {
			try {
				System.out.println(hours);
				getData();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Thread.sleep(hours);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			number--;

		}
	}

	public void getData() throws Exception {
		String data = "";
		StringBuilder sb = new StringBuilder();
		URL getUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) getUrl.openConnection();
		con.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((data = br.readLine()) != null) {
			sb.append(data);
		}
		br.close();
		// System.out.println(sb.toString());
		Map<String, Object> config = new HashMap<>();
		config.put("javax.json.stream.JsonGenerator.prettyPrinting", Boolean.valueOf(true));
		JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
		JSONObject json = new JSONObject(sb.toString());
		JSONArray array = json.getJSONArray("features");
		for (int i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			JSONObject properties = obj.getJSONObject("properties");

			String rawtime = properties.getString("timestamp").replace(".", "-");
			String[] t = rawtime.split("-");

			String[] clock = t[2].split("�");
			String timeString = clock[0] + "-" + t[1] + "-" + t[0] + " " + clock[1] + ":00";
			long time = java.sql.Timestamp.valueOf(timeString).getTime();
			JsonObject object = factory.createObjectBuilder().add("timestamp", time)
					.add("station", properties.getString("station")).add("lat", properties.getDouble("lat"))
					.add("lng", properties.getDouble("lng")).add("heigth", properties.getInt("hoehe")).add("temp", "")
//					.add("ozn", properties.getString("ozon"))
					.add("lux", properties.getString("luqx"))
					.add("no2", properties.getString("no2kont"))
//					.add("so2", properties.getString("so2"))
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
