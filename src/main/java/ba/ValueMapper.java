package ba;

import java.util.Map;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

import org.json.JSONObject;

public class ValueMapper {
	public JsonObject mapValues(String line) {
		
		
		
		String[] array = data.get(i).split(";");
		StringBuilder sb = new StringBuilder(array[3]);
		sb.insert(10, " ");
		sb.append(":00");
		long time = java.sql.Timestamp.valueOf(sb.toString()).getTime();
		
		
		
		
		Map<String, Object> config = null;
		JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
		JsonObject object = factory.createObjectBuilder().add("timestamp", time).add("station", array[1])
				.add("lat", "").add("lng", "").add("heigth", "").add("temp", array[4]).add("lux", "")
				.add("no2", "")
//				.add("so2", "").add("ozn", "")
				.build();
		return object;
	}	
	public JsonObject mapValues(JSONObject test) {
		
		
		
		JSONObject properties = obj.getJSONObject("properties");

		String rawtime = properties.getString("timestamp").replace(".", "-");
		String[] t = rawtime.split("-");

		String[] clock = t[2].split("�");
		String timeString = clock[0] + "-" + t[1] + "-" + t[0] + " " + clock[1] + ":00";
		long time = java.sql.Timestamp.valueOf(timeString).getTime();
		
		
		
		Map<String, Object> config = null;
		JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
		JsonObject object = factory.createObjectBuilder().add("timestamp", time).add("station", array[1])
				.add("lat", "").add("lng", "").add("heigth", "").add("temp", array[4]).add("lux", "")
				.add("no2", "")
//				.add("so2", "").add("ozn", "")
				.build();
		return object;
	}
}
