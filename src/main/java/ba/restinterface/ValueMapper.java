package ba.restinterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import org.json.JSONObject;

public class ValueMapper {
	public JsonObject buildJson(long time,long requestTime,  String station, double lat, double lng, int heigth, int ozon, int lux,
			int no2, int so2) {
		Map<String, Object> config = null;
		JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
		JsonObject object = factory.createObjectBuilder().add("timestamp", time).add("getRequestTimestamp",requestTime).add("station", station).add("latitude", lat)
				.add("longitude", lng).add("heigth", heigth).add("ozn", ozon).add("lux", lux).add("no2", no2).add("so2", so2)
				.build();
		return object;
	}

	public JsonObject mapValues(String line, long requestTime) {

		String[] array = line.split(";");
		StringBuilder sb = new StringBuilder(array[3]);
		sb.insert(10, " ");
		sb.append(":00");
		long time = java.sql.Timestamp.valueOf(sb.toString()).getTime();
		int temp = (int) Double.parseDouble(array[4]);

		return buildJson(time,requestTime,  array[0], 0, 0, 0, temp, 0, 0, 0);
	}

	public JsonObject mapValues(JSONObject test, String bl, String[] values, long requestTime) {
		SimpleDateFormat sd = null;
		long time = 0;
		switch (bl) {
		case ("by"):
			sd = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			break;
		case ("bw"):
			sd = new SimpleDateFormat("dd.MM.yyyy�HH:mm");
			break;
		case ("st"):
			sd = new SimpleDateFormat("dd.MM. HH:mm");
		default:
			sd = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			break;
		}
//		System.out.println(sd);
		JSONObject properties = test.getJSONObject(values[1]);
//		System.out.println(properties.getString("timestamp"));
		Date d = null;
		try {
			d = sd.parse(properties.getString("timestamp"));
			time = d.getTime();
//			System.out.println(  d);
			
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}

		int ozon = 0;
		try {
			ozon = properties.getInt(values[8]);
		} catch (Exception e) {
		}
		int lux = 0;
		try {
			lux = properties.getInt(values[9]);
		} catch (Exception e) {
		}
		int no2 = 0;
		try {
			no2 = properties.getInt(values[10]);
		} catch (Exception e) {
		}
		int so2 = 0;
		try {
			so2 = Integer.parseInt(properties.getString(values[11]));
		} catch (Exception e) {
		}
		String kennung = "";
		try {

			kennung = properties.getString(values[3]);
		} catch (Exception e) {
			kennung = properties.getString(values[4]);
		}
		return buildJson(time,requestTime, kennung, properties.getDouble(values[5]), properties.getDouble(values[6]),
				properties.getInt(values[7]), ozon, lux, no2, so2);
	}
}
