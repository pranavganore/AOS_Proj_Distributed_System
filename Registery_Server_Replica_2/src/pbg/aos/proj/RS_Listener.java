package pbg.aos.proj;
import javax.ws.rs.Consumes;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



@Path("/hearing")
public class RS_Listener {
	
	/*@GET
	@Path("/get")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		return "I am get !!! Hello World RESTful Jersey!PBG";
	}*/
	
	static ConcurrentHashMap<String, ConcurrentHashMap<String, String>> hashmap = new ConcurrentHashMap<String, ConcurrentHashMap<String, String>>();
	//HashMap as our Service_registery Mechanism 	
	
	@SuppressWarnings("unchecked")
	@POST
	@Path("/post/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response Listener( String data) {

		String result = "RS - Thanx Data Receieved !! ";
		
		System.out.println("I am RESTFUL Web Service Invoked by POST ");
		System.out.println("I receieved - " + data);
		
		JSONObject jobj = new JSONObject(data);
/*		System.out.println(jobj.get("Server_Name"));
		System.out.println(jobj.get("Services"));*/
		
		Map<String, Map<String, String>> updated_hashmap_temp;
		
		updated_hashmap_temp = addtohashmap(jobj); // adding the receieved data to hashmap and retrieveing the updated hashmap in response.

		System.out.println("Updated Hashmap:\n");
		System.out.println(updated_hashmap_temp);		

		return Response.status(201).entity(result).build();		
	}
	
	@POST
	@Path("/post/destroylistener")
	@Consumes(MediaType.TEXT_PLAIN)
	//@Produces(MediaType.TEXT_PLAIN)
	public void server_destroy_listener( String node_name) {
		//Here we recieve the notifications from the Nodes that are going to shut down gracefully!
		System.out.println("Notification about graceful shutdown receieved from : "+node_name);
		
		removefromhashmap(node_name);	//Thus first step is to remove the node from our Service_Registery hashmap
		//Load_Balancer LB = new Load_Balancer();
		Load_Balancer.destroy_Load(node_name); 	//Then reset the load inside the load balancer

		System.out.println("All the Cleaning activities done !!");
	}

	
	@SuppressWarnings("rawtypes")
	public static ConcurrentHashMap addtohashmap(JSONObject jdata) {
		@SuppressWarnings("unchecked")
		Iterator<String> keys = (jdata.getJSONObject("Services")).keys();//Used to iterate over JSON w.r.t keys
		while(keys.hasNext()) {// LinkedList (if key has next value)
		    String key = keys.next();       
		    //hashmap is the outer map and submap is the inner-map
			if(hashmap.get(key)!= null)// If hashmap is null it goes to else 
		    { // If service is already present add's the value to existing service
				ConcurrentHashMap<String, String> submap = hashmap.get(key);
				submap.put((jdata.get("Server_Name")).toString(), ((jdata.getJSONObject("Services")).get(key)).toString());
				hashmap.put(key, submap);
		    }
			else           // creates the service first in hashmap
			{
				ConcurrentHashMap<String, String> submap = new ConcurrentHashMap<String, String>();
				submap.put((jdata.get("Server_Name")).toString(), ((jdata.getJSONObject("Services")).get(key)).toString());
				hashmap.put(key, submap);
			}
		}
		
		//return "Item Addition Successfull";
		return hashmap;
	}
	
	public static void removefromhashmap(String node_name) {
		
		// Deletes only the value of the hashmap depending on the node name and deleteKey mentioned above
		//String server = "Node2";
		System.out.println("Removing from hasmap");
		for(String key : hashmap.keySet())
		{
			//if(key.equals(deleteKey))
			//{
				ConcurrentHashMap<String, String> deletemap = hashmap.get(key);
				for(String innerKey : deletemap.keySet())
				{
					if(innerKey.equals(node_name))
					{
						System.out.println("here");
						deletemap.remove(innerKey);
						hashmap.put(key, deletemap);
						
					}
					
				}
			//}
		}
		
		//return hashmap;
		
	}
	
	public static Set<String> getNodesofService(String service_name) throws IOException {
		
		//String Services = "Addition";
		//if(hashmap.get(Server) != null)
		//String Server = "Node1";
		
		String response = check_all_nodes();  //mechanism to check for any abdruptly failed servers without notifying
		if (response == "DONE"){
				//System.out.println(hashmap.get(service_name).keySet().toString());
				@SuppressWarnings("unused")
				String[] node_list = null;
				
				Set<String> node_set = null;
				for(String key : hashmap.keySet())
				{
					
					if(key.equals(service_name)) {
						System.out.println(hashmap.get(service_name).keySet().toString());
						
						node_set = hashmap.get(service_name).keySet();
						System.out.println(node_set);
						node_list = node_set.toArray(new String[node_set.size()]);
					}
					
				}
				return node_set;
		}else {
			System.out.println("No Node found in : "+service_name+" Service category.");
			return null;
		}
		//return null;
	}
	
	
	public static String getWSDLfromhashMap (String service_name, String node_name) {
		String wsdl = null;// = "http://localhost:8081/Axis2_Node1/services/Addition_Service?wsdl"; // currently hardcoding
		
		for(String key : hashmap.keySet())
		{
			if(key.equals(service_name))
			{
			
				Map<String, String> map = hashmap.get(key);
				System.out.println(map.get(service_name));
				for(String innerKey : map.keySet())
				{
					if(innerKey.equals(node_name)) {
						System.out.println(map.get(node_name));
						wsdl = map.get(node_name);
					}
						
				}
			}
		}

		return wsdl;
	}
	
	@SuppressWarnings("rawtypes")
	public static ConcurrentHashMap gethashmap() {
		
		return hashmap;
	}
	
	public static String check_all_nodes()  {
		
		ping_node("Node1");
		ping_node("Node2");
		ping_node("Node3");
		ping_node("Node4");
		ping_node("Node5");
		ping_node("Node6");
		
		System.out.println("New Updated Hashmap after ping :: ");
		System.out.println(hashmap);
	return "DONE";	
	}
	
	public static void ping_node(String node_name) {
		
		//Load_Balancer LB = new Load_Balancer();
		
		iterate_next_service:
		for(String key : hashmap.keySet()) //get the service names in the set 'key'
		{			
			if(hashmap.get(key).containsKey(node_name))
			{	
				System.out.println(node_name + " Found");
				System.out.println(hashmap.get(key).get(node_name));
			
				String url = hashmap.get(key).get(node_name).toString();
				try{
					HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
				
				connection.setRequestMethod("HEAD");
				connection.setReadTimeout(50000);
				connection.setConnectTimeout(50000);
			
				int responseCode = connection.getResponseCode();
				if (responseCode != 200) {
				    // Not OK.
					System.out.println("Not reachable");
					//now delete that node from whole hashmap
					removefromhashmap(node_name);
					System.out.println("Removed from map : " + node_name);
					Load_Balancer.destroy_Load(node_name); //reset the load for that node in load balancer
					System.out.println("Load Destroyed w.r.t. : "+node_name);
					connection.disconnect();
					break iterate_next_service;
					//reset the load for this node in load balancer.
				}else {
					System.out.println("Is reachable!!");
					connection.disconnect();
					break iterate_next_service;
				}
				}catch(IOException e) {
					//e.printStackTrace();
					
					    // Not OK.
						System.out.println("Not reachable(inside catch)");
						//now delete that node from whole hashmap
						removefromhashmap(node_name);
						System.out.println("Removed from map : " + node_name);
						Load_Balancer.destroy_Load(node_name); //reset the load for that node in load balancer
						System.out.println("Load Destroyed w.r.t. : "+node_name);
						
						break iterate_next_service;
						//reset the load for this node in load balancer.				
				}

					
				

			}
		}
		
		//return null;
		
	}
	
	public static void hashmap2json() {
		String hashmap_Str = hashmap.toString();
		System.out.println("hashmap in string format " + hashmap_Str);
	}
	
	
	

}
