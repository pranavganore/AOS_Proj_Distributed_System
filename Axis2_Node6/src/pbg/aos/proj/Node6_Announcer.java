package pbg.aos.proj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Node6_Announcer
 */
public class Node6_Announcer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static byte flag1 = 0;
	private static byte flag2 = 0;
	private static byte flag3 = 0;
	private static String RS_URL = "http://localhost:8080/Registery_Server/hearing/post/register";	//URL of main Registery Server
	private static String RS_R1_URL = "http://localhost:8180/Registery_Server_Replica_1/hearing/post/register";	//URL of Registery Server-Replica-1
	private static String RS_R2_URL = "http://localhost:8280/Registery_Server_Replica_2/hearing/post/register";	//URL of Registery Server-Replica-2
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Node6_Announcer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		init_announce_RS_main();
		init_announce_RS_R_1();
		init_announce_RS_R_2();
		
		System.out.println("Flag 1: "+flag1);
		System.out.println("Flag 2: "+flag2);
		System.out.println("Flag 3: "+flag3);
		
	    new java.util.Timer().schedule(new java.util.TimerTask() {
	        @Override
	        public void run() {
	        	int counter1=0;
	        	if (flag1 == 1)
	        		counter1 = 10;
	    		while(flag1!=1 || counter1 != 10) {
	    			try {
	    				System.out.println("Node6_Announcer >> Main Registery server NOT reachable! Will try again after 20 Seconds!!");
	    				Thread.sleep(1000*20);

	    				System.out.println("Trying to announce again after 20sec");
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			counter1++;
	    			
	    			init_announce_RS_main();
	    			
	    			System.out.println("finished calling init now");
	    		}
	        }
	    }, 
	    1000 * 20   
		);
		
	    
	    new java.util.Timer().schedule(new java.util.TimerTask() {
	        @Override
	        public void run() {
	    		int counter2=0;
	        	if (flag2 == 1)
	        		counter2 = 10;
	    		while(flag2!=1 || counter2 != 10) {
	    			try {
	    				System.out.println("Node6_Announcer >> Registery Server Replica 1 NOT reachable! Will try again after 20 Seconds!!");
	    				Thread.sleep(1000*20);
	    				//init_announce();
	    				System.out.println("Trying to announce again after 20sec");
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			System.out.println("calling init! now");
	    			counter2++;
	    			
	    			init_announce_RS_R_1();
	    			
	    			System.out.println("finished calling init now");
	    		}
	        }
	    }, 
	    1000 * 20   
		);
		
	    new java.util.Timer().schedule(new java.util.TimerTask() {
	        @Override
	        public void run() {
	    		int counter3=0;
	        	if (flag3 == 1)
	        		counter3 = 10;
	    		while(flag3!=1 || counter3 != 10) {
	    			try {
	    				System.out.println("Node6_Announcer >> Registery Server Replica 2 NOT reachable! Will try again after 20 Seconds!!");
	    				Thread.sleep(1000*20);
	    				//init_announce();
	    				System.out.println("Trying to announce again after 20sec");
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			System.out.println("calling init! now");
	    			counter3++;
	    			
	    			init_announce_RS_R_2();
	    			
	    			System.out.println("finished calling init now");
	    		}
	        }
	    }, 
	    1000 * 20   //Seconds after which the load would be scheduled to be reduced (Notional Load cmechanism)
		);
	    
	    new java.util.Timer().schedule(new java.util.TimerTask() {
	        @Override
	        public void run() {
	        	if(flag1 == 0 && flag2 == 0 && flag3 == 0) {
	        		System.out.println("Node6_Announcer >> No Registery servers are reachable!! Thus ShuttingDown!!");
	        	}
	        	
	        	//Load_Balancer LB = new Load_Balancer();
	        	//Load_Balancer.reduce_Load(node_name);
	    		
	    		}
	        
	    }, 
	    1000 * 300   //Seconds after which the load would be scheduled to be reduced (Notional Load cmechanism)
		);

	}
	
	public void init_announce_RS_main() {
		announcetoallRS(RS_URL);	//announce to Registery Server that this Node is up and running and which services this node has.
		//System.out.println("inside init_announce");
	}
	
	public void init_announce_RS_R_1() {
		announcetoallRS(RS_R1_URL);

	}
	
	public void init_announce_RS_R_2() {
		announcetoallRS(RS_R2_URL);
	}	
	//For replication
	public void announcetoallRS(String RSURL) {
		
		//POST CLIENT
		try {	
			
			//String hostname = InetAddress.getLocalHost().getHostName();
			String IPAddr = InetAddress.getLocalHost().getHostAddress();
			
			URL RS_url = new URL(RSURL);
			
			HttpURLConnection conn = (HttpURLConnection) RS_url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json"); // text/plain or application/json
			
			//Constructing the JSON string with Server_name,Services and their WSDL's
			String msg =  "{\"Server_Name\": \"Node6\", \"Services\": {\"Count\": \"http://"+IPAddr+":8086/Axis2_Node6/services/Count_Service?wsdl\",\"Subtraction\": \"http://"+IPAddr+":8086/Axis2_Node6/services/Subtraction_Service?wsdl\",\"Division\": \"http://"+IPAddr+":8086/Axis2_Node6/services/Division_Service?wsdl\"}}";
			
			OutputStream os = conn.getOutputStream();
			os.write(msg.getBytes());	//write the msg into outputstream
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				
			}/*else {
				flag = 1;
			}*/

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {	//Receive the response from Registery Server
				System.out.println(output);
			}
			
			if(RSURL == RS_URL) {
				flag1 = 1;
			}else if(RSURL == RS_R1_URL) {
				flag2 = 1;
			}else if(RSURL == RS_R2_URL) {
				flag3 = 1;
			}

			conn.disconnect();

		  } catch (MalformedURLException e) {

			//e.printStackTrace();
			  System.out.println("Node6_Announcer >> "+RSURL+" NOT Reachable!!!");
			  //No need to do anything here as we are anyhow notifying all registry servers.

		  } catch (IOException e) {

			//e.printStackTrace();
			  System.out.println("Node6_Announcer >> "+RSURL+" NOT Reachable!!!");
			  //No need to do anything here as we are anyhow notifying all registry servers.
			
		 }
		
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {		
		// TODO Auto-generated method stub
		
		//Notifies all the registry servers - that I am about to gracefully shutdown.
		String RS1URL = "http://localhost:8080/Registery_Server/hearing/post/destroylistener";
		String RS2URL = "http://localhost:8180/Registery_Server_Replica_1/hearing/post/destroylistener";
		String RS3URL = "http://localhost:8280/Registery_Server_Replica_2/hearing/post/destroylistener";
		notifyShutdown_to_RS(RS1URL);
		notifyShutdown_to_RS(RS2URL);
		notifyShutdown_to_RS(RS3URL);
				
	}
	
	public void notifyShutdown_to_RS(String RS_URL) {
		
		//POST CLIENT
		try {					
			//String hostname = InetAddress.getLocalHost().getHostName();
			String IPAddr = InetAddress.getLocalHost().getHostAddress();
			
			URL RS_url = new URL(RS_URL);
			// Here I can create a construct for replication
			HttpURLConnection conn = (HttpURLConnection) RS_url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/plain"); // text/plain or application/json
			
			String msg = "Node6";
			OutputStream os = conn.getOutputStream();
			os.write(msg.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}
			conn.disconnect();

		  } catch (MalformedURLException e) {

			e.printStackTrace();

		  } catch (IOException e) {

			e.printStackTrace();
			
		 }
		
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
