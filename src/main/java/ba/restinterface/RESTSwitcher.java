package ba.restinterface;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RESTSwitcher extends Thread {
	public Properties prop;
	private static RESTSwitcher instance;
	private Map<String, String[]> map;

	private RESTSwitcher() {
		prop = new Properties();
		map = new HashMap<>();
		start();
	}

	public void run() {
		while (true) {
			BufferedInputStream bi = null;
			try {
				bi = new BufferedInputStream(
						new FileInputStream("./src/main/java/ba/restinterface/RestService.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				prop.load(bi);
				System.out.println("Loaded new Prop");
				System.out.println(prop.getProperty("lupo"));
				Set<String> test =  prop.stringPropertyNames();
				for (String i : test) {
					map.put(i, prop.getProperty(i).split(","));				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(3600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static RESTSwitcher getInstance() {
		if (RESTSwitcher.instance == null) {
			RESTSwitcher.instance = new RESTSwitcher();
		}
		return RESTSwitcher.instance;

	}

	public String[] getJsonFormat(String in) {

//		String[] out = prop.getProperty(in.split("-")[0]).split(",");
		String[] out = map.get(in.split("-")[0]);
		return out;
	}
}
