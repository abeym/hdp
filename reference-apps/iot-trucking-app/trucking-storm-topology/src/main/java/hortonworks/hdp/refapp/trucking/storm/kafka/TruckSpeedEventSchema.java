package hortonworks.hdp.refapp.trucking.storm.kafka;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.storm.spout.Scheme;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import org.apache.storm.utils.Utils;


public class TruckSpeedEventSchema extends BaseTruckEventSchema {

	private static final long serialVersionUID = -2990121166902741545L;
	private static final Logger LOG = LoggerFactory.getLogger(TruckSpeedEventSchema.class);
	

	@Override
	public List<Object> deserialize(ByteBuffer buffer) {
		try {
			String[] pieces = deserializeRawString(buffer);
		        LOG.info("Creating truck speed schema");	
			Timestamp eventTime = Timestamp.valueOf(pieces[1]);
			String streamSource = pieces[2];
			int truckId = Integer.valueOf(pieces[3]);
			int driverId = Integer.valueOf(pieces[4]);
			String driverName = pieces[5];
			int routeId = Integer.valueOf(pieces[6]);
			String routeName = pieces[7];
			int speed = Integer.valueOf(pieces[8]);
			
			if(LOG.isTraceEnabled()) {
				LOG.trace("Creating a Truck Scheme with driverId["+driverId + "], driverName["+driverName+"], routeId["+routeId+"], routeName["+ routeName +"], "
						+ "and speed["+speed +"]");				
			}

			
			return new Values(driverId, truckId, eventTime, driverName, routeId, routeName, speed);
			
		} catch (Exception e) {
			LOG.error("Error serializeing truck event in Kafka Spout",  e);
			return null;
		}
		
	}

	


	@Override
	public Fields getOutputFields() {
		return new Fields("driverId", "truckId", "eventTime", "driverName", "routeId", "routeName", "truckSpeed");
		
	}
	


}
