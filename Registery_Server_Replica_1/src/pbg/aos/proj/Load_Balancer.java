package pbg.aos.proj;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class Load_Balancer {
	
	//Hashmap to store the loads for the node_servers.
	public static ConcurrentHashMap<String, Integer> load_Map = new ConcurrentHashMap<String, Integer>(){{
	    put("Node1", 0 );
	    put("Node2", 0 );
	    put("Node3", 0 );
	    put("Node4", 0 );
	    put("Node5", 0 );
	    put("Node6", 0 );
	}};
	

	//method to update the load for any node
	public static void increase_Load(String node_name) {
	
		//get the current value from hashmap for that node , add the new load (for reducing load we will get a value in -) 
		//update the hashmap key - node_name with new value
		int curr_value = load_Map.get(node_name);
		
		load_Map.put(node_name, (curr_value + 60) );  //setting the load to 60units i.e. loading that server(notionally by 60units)
		System.out.println("Load Increased of "+node_name + " from " + curr_value + " to + 60, so new load is : " + load_Map.get(node_name));
		System.out.println("Load_Map after increasing load : ");
		System.out.println(load_Map);
	}
	
	public static void reduce_Load(String node_name){
		
		//get the current value from hashmap for that node , add the new load (for reducing load we will get a value in -) 
		//update the hashmap key - node_name with new value
		int curr_value = load_Map.get(node_name);
		
		load_Map.put(node_name, (curr_value - 60) );  //setting the load to 60units i.e. loading that server(notionally by 60units)
		System.out.println("Load Decreased of "+node_name + " from " + curr_value + "to -60, so new load is : " + load_Map.get(node_name));
		System.out.println("Load_Map after reducing load : ");
		System.out.println(load_Map);
	}
	
	public static void destroy_Load(String node_name) {	// used to set the load of any node to 0 when that surver turns itself off
		load_Map.put(node_name,0);	//resetting the value of a node to 0 - when we get a notification from the node that server is being shut down.
		//return 1;
				
	}
	
	public static String compareLoads(Set<String> node_set) {
		//Set<String> node_set = Stream.of("Node1","Node2").collect(Collectors.toSet()); // temporarily creating the set for testing
		List<String> list = new ArrayList<String>(node_set); //converting the set to array
		String Node_1 = list.get(0);
		String Node_2 = list.get(1);		
		int Load_Node_a = load_Map.get(Node_1);
		int Load_Node_b = load_Map.get(Node_2);
		
	    if( (Load_Node_a < Load_Node_b) && (Load_Node_a < 10000000) ) {
	    	increase_Load(Node_1); // before returning the node name update the load(increase the load as this service is going to be used)
	    	return Node_1;  
	    	
	    }else if ( (Load_Node_a > Load_Node_b) && (Load_Node_b < 10000000)) {
	    	increase_Load(Node_2);// before returning the node name update the load(increase the load as this service is going to be used)
	    	return Node_2;
	    	
	    }else if((Load_Node_a == Load_Node_b) && (Load_Node_a < 10000000) ) {
	    	increase_Load(Node_1);// before returning the node name update the load(increase the load as this service is going to be used)
	    	return Node_1;
	    	
	    }else /*( (Load_Node_a > 10000000) && (Load_Node_b > 10000000))*/{
	    	System.out.println("( Load Balancer on [RS1] ) >> Loads on all nodes : "+Node_1+" & "+Node_2+" is more than 90%! Hence These nodes are currently unavailable for availing their services (As a Load Balancer failsafe measure!!)" ) ;
	    	return null;
				    	
	    }/*else {
	    	System.out.println("Inside final else\n\n\n\n");
	    	increase_Load(Node_1);
	    	return Node_1;
	    }*/

	}
	
	public static int getloadfor(String nodenm) {
		return load_Map.get(nodenm);
	}
}

