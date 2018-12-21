package pbg.aos.proj;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;




@Path("/services_registery")
public class Services_Registery {
	String node_name = null;
	//String pbg ="0";
	
	@GET
	@Path("/servicesenquiry_wsdl/{service_name}")
	@Produces(MediaType.TEXT_PLAIN)
	public String service_enquiry(@PathParam("service_name") String service_name) throws IOException {   //client sends a get req here to invoke this method and in response gets the wsdl of that service.
		//RS_Listener RS_L = new RS_Listener();
		String WSDL = null;
		
		
		Set<String> Node_Server_Set = RS_Listener.getNodesofService(service_name); //getting all the nodes from registery which have the requested service.

		System.out.println("( Service_Registery on [RS1] ) >> Printing Node_Server_Set :: ");
		System.out.print(Node_Server_Set);
		
		if (Node_Server_Set.size() == 1 && Node_Server_Set != null){
			//String node_name = null;
			Iterator iterator = Node_Server_Set.iterator(); // get the node name from the node set
			while(iterator.hasNext()){
			  node_name = (String) iterator.next();
			}
			int load = Load_Balancer.getloadfor(node_name);
			if (load < 10000000) {
				WSDL = RS_Listener.getWSDLfromhashMap(service_name,node_name); //get the wsdl for that service_name and node_name from RS.
				//Load_Balancer LB = new Load_Balancer();
				Load_Balancer.increase_Load(node_name); //increase the load of that node to denote that the srvice is being used and server node is now loaded.
								
			}else {
				System.out.println("( Load Balancer on [RS1] ) >> Load on nodes : "+node_name+" is more than 90%! Hence This node is currently unavailable for availing its services (As a Load Balancer failsafe measure!!)");
				return WSDL;
			}
		}else if (Node_Server_Set.size() > 1){
			//call the loadbalancer class and give it this list to get the node with least load.
			//Load_Balancer LB = new Load_Balancer();
			node_name = Load_Balancer.compareLoads(Node_Server_Set); // send the nodeset to load balancer and get the node which has less load.
			WSDL = RS_Listener.getWSDLfromhashMap(service_name,node_name); // now get the wsdl for that node(returned from load balancer) from Registery server.
			//I need not increase the load from here as it is done by compareLoads in loadbalancer class.
			
			
		}
		//System.out.println(WSDL);
		//return "http://localhost:8081/Axis2_Node1/services/Addition_Service?wsdl";
			if(WSDL != null) {
		        new java.util.Timer().schedule( 
		    new java.util.TimerTask() {
		        @Override
		        public void run() {
		        	System.out.println("Inside Timer Task run");
		        	//Load_Balancer LB = new Load_Balancer();
		        	Load_Balancer.reduce_Load(node_name);
		        }
		    }, 
		    1000 * 60   //Seconds after which the load would be scheduled to be reduced (Notional Load cmechanism)
			);
		}
		return WSDL;
	}

/*	public static void main(String[] args) {
		// TODO Auto-generated method stub
		

	}*/

}
