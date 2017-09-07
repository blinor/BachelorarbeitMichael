# bacherlorabreitMichael
This is the KafkaProducer for the Weatherstream provided by Rest-Interface
#Deployment
You need to create the file prop.properties in the ordner ./data/restproducer in the folder where your .yml file is.
this file must contain your desired url in the format 

* 	url=http,{yoururl};{timebetweenrequest};{#ofrequest(-1 for no limit)},http;{yoururl};{timebetweenrequest};{#ofrequest(-1 for no limit)},...

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
