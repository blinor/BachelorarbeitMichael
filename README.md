# Bachelorarbeit 
This is the KafkaProducer for the Weatherstream provided by Rest-Interface
#Deployment
You need to modify in your Consul-Instance the url field in the restporducer path (looks sth. like this SP/V1/PE/ORG.STREAMPIPES.BIGGIS.PE.BA.RESTINTERFACE/) and add the desired URL and its configuration 

* 	{yoururl};{timebetweenrequest};{#ofrequest(-1 for no limit)},{yoururl};{timebetweenrequest};{#ofrequest(-1 for no limit)},...

		//
			restproducer:
				image: laus.fzi.de:8201/weatherba/restproducer
				depends_on:
					- "consul"
				ports:
					- "8092:8085"
				volumes:
					- ./data/restproducer:/src/main/java/ba/restinterface
				networks:
					spnet:
