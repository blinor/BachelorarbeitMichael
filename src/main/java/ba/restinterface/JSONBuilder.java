package ba.restinterface;

import org.json.*;

public class JSONBuilder {
	public JSONObject buildJson(long time,long requestTime,  String station, double lat, double lng, int heigth, int ozon, int lux,
			int no2, int so2, int pm10) {
		JSONObject object = new JSONObject().put("timestamp", time).put("getRequestTimestamp",requestTime).put("station", station).put("latitude", lat)
				.put("longitude", lng).put("heigth", heigth).put("ozn", ozon).put("lux", lux).put("no2", no2).put("so2", so2).put("pm10", pm10);
		return object;
	}
}
