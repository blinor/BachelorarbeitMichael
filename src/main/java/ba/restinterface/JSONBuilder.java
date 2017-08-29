package ba.restinterface;

import java.util.Map;

import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;

public class JSONBuilder {
	public JsonObject buildJson(long time,long requestTime,  String station, double lat, double lng, int heigth, int ozon, int lux,
			int no2, int so2) {
		Map<String, Object> config = null;
		JsonBuilderFactory factory = javax.json.Json.createBuilderFactory(config);
		JsonObject object = factory.createObjectBuilder().add("timestamp", time).add("getRequestTimestamp",requestTime).add("station", station).add("latitude", lat)
				.add("longitude", lng).add("heigth", heigth).add("ozn", ozon).add("lux", lux).add("no2", no2).add("so2", so2)
				.build();
		return object;
	}
}
