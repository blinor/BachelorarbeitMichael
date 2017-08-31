package ba.restinterface;

import java.net.*;
import java.util.Properties;

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
		this.hours = Long.parseLong(input) * 10000;
		this.number = Integer.parseInt(n);
	}

	public void run() {
		while (number != 0) {
			try {
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
		System.out.println("Stopped Request-Limit Reached");
		producer.close();
	}

	public void getData() throws Exception {

		long timeRequest = System.currentTimeMillis();
		RESTSwitcher rs = new RESTSwitcher();
		String[] values = rs.getJsonFormat(url.replaceAll("http://", "").replaceAll("[.].", ""));
		// System.out.println(url.replaceAll("http://",
		// "").replaceAll("[.].*",""));
	
		ValueMapper vm = new ValueMapper();
		String bl = url.split("land=")[1].split("&")[0];
		// System.out.println(bl);
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
		
		
		JSONObject json = new JSONObject(sb.toString());
		JSONArray array = json.getJSONArray(values[0]);
		int i = 0;
		for (i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			JsonObject object = vm.mapValues(obj, bl, values, timeRequest);
			byte[] bytes = null;
			try {
				bytes = object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			producer.send(new ProducerRecord<String, Object>(topic, bytes));
			producer.flush();
			// System.out.println("Send!" + object);
		}
		System.out.println("Send " + i + " objects from " + url.replaceAll("http://", "").replaceAll("[.].*", "") + " "
				+ bl + " in " + (System.currentTimeMillis() - timeRequest) + "ms");

	}
}
