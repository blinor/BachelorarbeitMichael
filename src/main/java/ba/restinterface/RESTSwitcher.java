package ba.restinterface;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Jahns this will cache the REST data information to parse them
 *         correctly meant to be a singleton in a own thread, so there won't be
 *         instances for every RequestThread. It's a Thread to be able to set it
 *         to sleep without interfering with the other components
 */
public class RESTSwitcher extends Thread {
	private static RESTSwitcher instance;
	private Map<String, String[]> map;

	/**
	 * Private, because Singleton
	 */
	private RESTSwitcher() {
		map = new HashMap<>();
		start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			String[] services = Config.INSTANCE.getConfig().split(";");

			System.out.println("Loaded new Prop");

			for (int i = 0; i < services.length; i++) {
				String[] content = services[i].split("=");
				map.put(content[0], content[1].split(","));
			}

			try {
				Thread.sleep(600000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @return Instance(to be a Singleton )
	 */
	public static RESTSwitcher getInstance() {
		if (RESTSwitcher.instance == null) {
			RESTSwitcher.instance = new RESTSwitcher();
		}
		return RESTSwitcher.instance;

	}

	/**
	 * @param in
	 *            "Name" of the REST-Interface
	 * @return Config of the datas
	 */
	public String[] getJsonFormat(String in) {

		String[] out = map.get(in.split("-")[0]);
		return out;
	}
}
