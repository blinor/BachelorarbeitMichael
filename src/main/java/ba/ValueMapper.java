package ba;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import org.json.JSONObject;

public class ValueMapper {
	public JsonObject buildJson(long time, String station, double lat, double lng, int heigth, double temp, int ozon,
			int lux, int no2, int so2) {
		Map<String, Object> config = null;
		JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
		JsonObject object = factory.createObjectBuilder().add("timestamp", time).add("station", station).add("lat", lat)
				.add("lng", lng).add("heigth", heigth).add("temp", temp).add("ozn", ozon).add("lux", lux)
				.add("no2", no2).add("so2", so2).build();
		return object;
	}

	public JsonObject mapValues(String line) {

		String[] array = line.split(";");
		StringBuilder sb = new StringBuilder(array[3]);
		sb.insert(10, " ");
		sb.append(":00");
		long time = java.sql.Timestamp.valueOf(sb.toString()).getTime();
		double temp = Double.parseDouble(array[4]);

		return buildJson(time, array[0], 0, 0, 0, temp, 0, 0, 0, 0);
	}

	public JsonObject mapValues(JSONObject test, String bl) {
		SimpleDateFormat sd = null;
		long time = 0;
		switch(bl){
		case("by"):
			sd = new SimpleDateFormat("dd.mm.YYYY HH:MM");
			break;
		case("bw"):
			sd = new SimpleDateFormat("dd.mm.YYYY�HH:MM");
			break;
		case("st"):
			sd = new SimpleDateFormat("dd.mm. HH:MM");
		default:
			sd = new SimpleDateFormat("dd.mm.YYYY HH:MM");
			break;
		}

		JSONObject properties = test.getJSONObject("properties");
//		System.out.println(properties.getString("timestamp"));
		Date d = null;
		try {
			d = sd.parse(properties.getString("timestamp"));
			time = d.getTime();
		} catch (Exception e) {
			time = System.currentTimeMillis();
		}
		
		
		double temp = 0;
		try {
			temp = properties.getDouble("temp");
		} catch (Exception e) {
		}
		int ozon = 0;
		try {
			ozon = properties.getInt("ozon");
		} catch (Exception e) {
		}
		int lux = 0;
		try {
			lux = properties.getInt("luqx");
		} catch (Exception e) {
		}
		int no2 = 0;
		try {
			no2 = properties.getInt("no2kont");
		} catch (Exception e) {
		}
		int so2 = 0;
		try {
			so2 = Integer.parseInt(properties.getString("so2"));
		} catch (Exception e) {
		}
		String kennung="";
		try {
			
			kennung = properties.getString("kennung");
		} catch (Exception e) {
			kennung = properties.getString("station");
		}
		return buildJson(time,kennung , properties.getDouble("lat"),
				properties.getDouble("lng"), properties.getInt("hoehe"), temp, ozon, lux, no2, so2);
	}
}
