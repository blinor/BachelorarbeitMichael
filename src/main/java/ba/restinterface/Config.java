package ba.restinterface;

import org.streampipes.config.SpConfig;

/**
 * @author Michael Jahns
 *
 *THis is the Config to communicate with Consul. 
 *We get the correct HOSTs and also the URLs to fetch from
 *
 */
public enum Config {
	INSTANCE;

	private SpConfig config;
	private final static String HOST = "host";
	private final static String URL = "url";
	private final static String CONFIG = "config";
	private final static String TOPIC = "topic";
	private final static String PORT = "port";
	private final static String KAFKA_HOST = "kafka_host";
	private final static String KAFKA_PORT = "kafka_port";
	private final static String ZOOKEEPER_HOST = "zookeeper_host";
	private final static String ZOOKEEPER_PORT = "zookeeper_port";

	public final static String serverUrl;
	public final static String iconBaseUrl;

	Config() {
		config = SpConfig.getSpConfig("pe/org.streampipes.biggis.pe.ba.restinterface");
		config.register(CONFIG, "lupo=features,properties,timestamp,station,debwnr,lat,lng,hoehe,ozon,luqx,no2kont,so2,pm10grav;test=1,2,3,4,5,6", "This will Set the Json IDs to parse for StreamPipes");
		config.register(URL,
				"http://lupo-messwerte.appspot.com/lupo_luft_query?land=bw&limit=500&format=gme;1;-1,http://lupo-messwerte.appspot.com/lupo_luft_query?land=by&limit=500&format=gme;600;-1",
				"The url's you want to call with this application <url>;<Time to wait between calls(in 10 Sec)>;<how often the call is done>,next");
		config.register(HOST, "restproducer", "Hostname for the pe templates project");
		config.register(PORT, 8087, "Port for the pe slack integration");
		config.register(KAFKA_HOST, "kafka", "Host for kafka of the pe sinks project");
		config.register(KAFKA_PORT, 9092, "Port for kafka of the pe sinks project");
		config.register(ZOOKEEPER_HOST, "zookeeper", "Host for zookeeper of the pe sinks project");
		config.register(ZOOKEEPER_PORT, 2181, "Port for zookeeper of the pe sinks project");
		config.register(TOPIC, "my.weather.lubw", "The topic like the Sourcemodul");
	}

	static {
		serverUrl = Config.INSTANCE.getHost() + ":" + Config.INSTANCE.getPort();
		iconBaseUrl = Config.INSTANCE.getHost() + ":" + Config.INSTANCE.getPort() + "/img";
	}

	public String getHost() {
		return config.getString(HOST);
	}
	public String getConfig() {
		return config.getString(CONFIG);
	}
	public String getTopic(){
		return config.getString(TOPIC);
	}

	public int getPort() {
		return config.getInteger(PORT);
	}
	public String getUrl(){
		return config.getString(URL);
	}
	public String getKafkaHost() {
		return config.getString(KAFKA_HOST);
	}

	public int getKafkaPort() {
		return config.getInteger(KAFKA_PORT);
	}

	public String getKafkaUrl() {
		return getKafkaHost() + ":" + getKafkaPort();
	}

	public String getZookeeperHost() {
		return config.getString(ZOOKEEPER_HOST);
	}

	public int getZookeeperPort() {
		return config.getInteger(ZOOKEEPER_PORT);
	}

}
