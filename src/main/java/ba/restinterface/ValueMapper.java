package ba.restinterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//import javax.json.JsonObject;
import org.json.JSONObject;

/**
 * @author Michael Jahns
 * Class to Match the Data by the server to the format StreamPipes must have
 */
public class ValueMapper {

	/**
	 * @return JsonObject to send
	 * WIll be used for CSV files
	 */
	public JSONObject mapValues(String line, long requestTime) {
		JSONBuilder jb = new JSONBuilder();
		String[] array = line.split(";");
		StringBuilder sb = new StringBuilder(array[3]);
		sb.insert(10, " ");
		sb.append(":00");
		long time = java.sql.Timestamp.valueOf(sb.toString()).getTime();
		int temp = (int) Double.parseDouble(array[4]);

		return jb.buildJson(time, requestTime, array[0], 0, 0, 0, temp, 0, 0, 0, 0);
	}

	/**
	 * @param values the "Buildlog" of the Rest-Interface data
	 * @return JsonObject to send
	 * Will be used for REST-Interface data
	 */
	public JSONObject mapValues(JSONObject test, String bl, String[] values, long requestTime) {
		JSONBuilder jb = new JSONBuilder();
		SimpleDateFormat sd = null;
		long time = 0;
		switch (bl) {
		case ("by"):
			sd = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			break;
		case ("bw"):
			sd = new SimpleDateFormat("dd.MM.yyyy�HH:mm");
			break;
		default:
			sd = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			break;
		}
		sd.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
		JSONObject properties = test.getJSONObject(values[1]);
		// System.out.println(sd);
		// System.out.println(properties.getString("timestamp"));
		Date d = null;
		try {
			d = sd.parse(properties.getString("timestamp"));
			time = d.getTime();
			// System.out.println( d);

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
		int pm10 = 0;
		try {
			pm10 = Integer.parseInt(properties.getString(values[12]));
		} catch (Exception e) {
		}
		String kennung = "";
		try {

			kennung = properties.getString(values[4]);
		} catch (Exception e) {
			kennung = properties.getString(values[3]);
		}
		return jb.buildJson(time, requestTime, kennung, properties.getDouble(values[5]),
				properties.getDouble(values[6]), properties.getInt(values[7]), ozon, lux, no2, so2, pm10);
	}
}
