package ba.restinterface;

import java.net.*;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;

/**
 * @author Michael Jahns Class to get the data from REST-Interfaces and Manages
 *         the processing of the data send by the Server
 */
public class GetData implements Runnable{
	org.apache.kafka.clients.producer.Producer<String, Object> producer;
	String topic;
	String url;
	long hours;
	int number;
	Properties prop;

	/**
	 * @param prop
	 *            Kafka Properties
	 * @param topic
	 *            Kafka Topic
	 * @param url
	 *            URL to start request
	 * @param input
	 *            time to wait between requests
	 * @param n
	 *            Number how often the URL will be requested
	 */
	public GetData(Properties prop, String topic, String url, String input, String n) {
		this.producer = new KafkaProducer<>(prop);
		this.topic = topic;
		this.url = url;
		this.hours = Long.parseLong(input) * 10000;
		this.number = Integer.parseInt(n);
		this.prop = prop;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run() This will just manage the waiting time
	 */
	public void run() {
		while (number != 0) {
			if(url.contains(".csv")){
				CsvReader cr = new CsvReader(prop, topic);
				cr.sendData(url);
			}else{	
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
	}}

	/**
	 * @throws Exception
	 *             Can be thrown by trying to convert the URL and try to open
	 *             the conenction
	 * 
	 * 
	 */
	public void getData() throws Exception {

		RESTSwitcher rs = RESTSwitcher.getInstance();
		String[] values = rs.getJsonFormat(url.replaceAll("http://", "").replaceAll("[.].", ""));

		ValueMapper vm = new ValueMapper();
		String bl = url.split("land=")[1].split("&")[0];

		String data = "";
		StringBuilder sb = new StringBuilder();
		URL getUrl = new URL(url);
		HttpURLConnection con = (HttpURLConnection) getUrl.openConnection();
		// con.setUseCaches(false);
		con.setRequestMethod("GET");
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		while ((data = br.readLine()) != null) {
			sb.append(data);
		}
		br.close();
		// con.disconnect();

		long timeRequest = System.currentTimeMillis();
		JSONObject json = new JSONObject(sb.toString()); // Serveranswer to JSON
		JSONArray array = json.getJSONArray(values[0]);
		int i = 0;
		for (i = 0; i < array.length(); i++) {
			JSONObject obj = array.getJSONObject(i);
			JSONObject object = vm.mapValues(obj, bl, values, timeRequest); // vm
																			// to
																			// build
																			// the
																			// JSON
																			// to
																			// sen
			byte[] bytes = null;
			try {
				bytes = object.toString().getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			producer.send(new ProducerRecord<String, Object>(topic, bytes));
			producer.flush();
		}
		System.out.println("Send " + i + " objects from " + url.replaceAll("http://", "").replaceAll("[.].*", "") + " "
				+ bl + " in " + (System.currentTimeMillis() - timeRequest) + "ms");

	}
}
