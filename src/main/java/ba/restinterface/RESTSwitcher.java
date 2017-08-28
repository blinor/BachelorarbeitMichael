package ba.restinterface;

public class RESTSwitcher {
	private String[] lupo = { "features", "properties", "timestamp", "kennung", "station", "lat", "lng", "hoehe",
			"ozon", "luqx", "no2kont", "so2" };

	public String[] getJsonFormat(String in) {
		String[] out= null;
		switch (in.split("-")[0]) {
		case "lupo":
			out = lupo;
			break;
		default:
			 break;
		}
return out;
	}
}
