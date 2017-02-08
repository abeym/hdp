package com.ms;

//import hortonworks.hdp.refapp.trucking.install.service.Autowired;
//import hortonworks.hdp.refapp.trucking.install.service.HDPServiceRegistry;
//import hortonworks.hdp.refapp.trucking.install.service.StormTopology;
//import hortonworks.hdp.refapp.trucking.install.service.StormTopologyParams;
//import hortonworks.hdp.refapp.trucking.install.service.StormUtils;
//import hortonworks.hdp.refapp.trucking.install.service.TruckEventProcessorKafkaTopology;

import java.util.Properties;

public class StormService {
	
	private HDPServiceRegistry registry = new HDPServiceRegistry();	

	/*@Autowired
	public StormService(HDPServiceRegistry serviceRegistry) {
		
		this.registry = serviceRegistry;
	}*/
	
	/*
	 * Uploads and deploys a Storm Topology Jar
	 */
	public void deployStormTopology(StormTopologyParams topologyParams) throws Exception {
		
		Properties topologyConfig = constructStormTopologyConfig();
		
		StormTopology topology = createTopology(topologyConfig);
		
		topologyParams.setTopology(topology);
		topologyParams.setTopologyName(registry.getCustomValue("trucking.topology.name"));
		topologyParams.setNumberOfWorkers(Integer.valueOf(registry.getCustomValue("trucking.storm.trucker.topology.workers")));
		topologyParams.setEventLogExecutors(Integer.valueOf(registry.getCustomValue("trucking.storm.topology.eventlogger.executors")));
		topologyParams.setTopologyMessageTimeoutSecs(Integer.valueOf(topologyConfig.getProperty("trucking.storm.topology.message.timeout.secs")));

		String stormTopologyJarLocation = registry.getCustomValue("trucking.storm.topology.jar");
		LOG.info("Storm Topology Jar Location is: " + stormTopologyJarLocation);
		topologyParams.setTopologyJarLocation(stormTopologyJarLocation);
				
		StormUtils stormUtils = new StormUtils(registry);
		stormUtils.deployStormTopology(topologyParams);
	
	}
	
	/*
	 * Kills the Storm Topology if its up
	 */
	public void killStormTopology() throws Exception {
		StormUtils stormUtils = new StormUtils(registry);
		stormUtils.killStormTopology(registry.getCustomValue("trucking.topology.name"));
	}	
	
	

	private StormTopology createTopology(Properties topologyConfig) throws Exception {
		
		/* Construct the Topology */
		StormTopology topology = buildTopology(topologyConfig);
		return topology;
	}
	
	private Properties constructStormTopologyConfig()   {
		Properties topologyConfig = new Properties();
		
		// Dump everything from registry into the Topology Config
		for(String key: registry.getRegistry().keySet()) {
			String value = registry.getRegistry().get(key);
			if(value != null) {
				topologyConfig.put(key, value );
			} else {
				LOG.info("Populate Storm Topologogy Config from registry, key["+key + "] had null value");
			}
			
		}
		
		//Only extra we need to store is the kafka zookeeper connection string		
		String zookeeperHostPort = registry.getKafkaZookeeperHost() + ":" + registry.getKafkaZookeeperClientPort();
		topologyConfig.put("kafka.zookeeper.host.port", zookeeperHostPort);
	
		return topologyConfig;
	}	

	private StormTopology buildTopology(Properties topologyConfig) throws Exception {
		TruckEventProcessorKafkaTopology truckTopology = new TruckEventProcessorKafkaTopology(topologyConfig);
		StormTopology topology = truckTopology.buildTopology();
		return topology;
	}	

}

