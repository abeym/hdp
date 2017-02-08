package com.ms;
//import hortonworks.hdp.apputil.storm.StormTopologyParams;

public class TopologyMainClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StormTopologyParams topologyParams = new StormTopologyParams();
		topologyParams.setUpload(true);	
		StormService stormService = new StormService();
		stormService.deployStormTopology(topologyParams);

	}

}

