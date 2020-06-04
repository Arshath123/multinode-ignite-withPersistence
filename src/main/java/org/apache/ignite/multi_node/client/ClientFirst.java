package org.apache.ignite.multi_node.client;

import java.util.Collection;
import java.util.Collections;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;

public class ClientFirst {
	public static void main(String[] args) {
		  IgniteConfiguration cfg = new IgniteConfiguration();
		
		  cfg.setClientMode(true);
		
		  cfg.setPeerClassLoadingEnabled(true);
		
		  TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		  ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
		  cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));
		
		  Ignite ignite = Ignition.start(cfg);
	      
		  //activating the cluster
		  ignite.cluster().active(true);
	      
		  //adding all nodes to baseline topology
	      Collection<ClusterNode> nodes = ignite.cluster().forServers().nodes();
	      ignite.cluster().setBaselineTopology(nodes);
	      
	      ignite.cluster().baselineAutoAdjustEnabled(true);
	      
		  IgniteCache<Integer, String> cache = ignite.getOrCreateCache("myCache");
		 
		  for (int i = 101; i <= 10000; i++) {
			  cache.put(i, "Arshath");
		  }
//		  System.out.println("Getting values from cache");
		  long count = 0;
		  for (int i = 1; i <= 10000; i++) {
			  if(cache.get(i) != null) {
				  count++;
			  }
		  }
		  System.out.println(count);
		  ignite.close();
		}
}
