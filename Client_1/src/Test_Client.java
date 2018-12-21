import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;

import org.glassfish.jersey.client.ClientConfig;

import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.xml.util.ResourceDownloadException;

public class Test_Client {

	
	
	public static final String RegisteryServer_URI = "http://localhost:8080/Registery_Server/";	//Main Registery Server URL
	public static final String RegisteryServer_R1_URI = "http://localhost:8180/Registery_Server_Replica_1/";	//Registery Server-Replica-1 URL
	public static final String RegisteryServer_R2_URI = "http://localhost:8280/Registery_Server_Replica_2/";	//Registery Server-Replica-2 URL
	//private static final String port
	public static String selected_RS_URL = null;
	private static byte flag = 0;
	private static byte flag1 = 0;
	private static byte flag2 = 0;
	private static byte flag3 = 0;
	
	
	public static void initialize_client_RS() {
		flag = 0 ;
		ping_all_RS(RegisteryServer_R1_URI);
		ping_all_RS(RegisteryServer_R2_URI);
		ping_all_RS(RegisteryServer_URI);
		if(flag == 0){
			System.out.println("All Registery Servers are Unreachable !! Hence Exiting !!");
			System.exit(0);
		}
		
		
		RS_Selector();
		
	}
	
	public static void RS_Selector() {
		if (flag == 1) {
			selected_RS_URL = RegisteryServer_URI;
		}else if (flag == 2) {
			selected_RS_URL = RegisteryServer_R1_URI;
		}else if (flag == 3) {
			selected_RS_URL = RegisteryServer_R2_URI;
		}else if(flag == 0)
			System.out.println("None of the Registery Servers available to select !");
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		initialize_client_RS();
		
		System.out.println("Currently availing services of Registery Server at : "+ selected_RS_URL);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		int choice = -1 ;
		int test_counter = 1;
		while(choice != 0 && test_counter != 101) {
				initialize_client_RS();
				System.out.println("Select the Service you want to avail from the list (Enter the Index Number of Service):: ");
				System.out.println("1. Addition \n2. Subtraction \n3. Multiplication \n4. Division \n5. Echo (returns Uppercased echo) "
						+ "\n6. Reverse \n7. Count \n8. GCD \n9. LCM");
				System.out.println("Enter your choice : ");
				//choice = Integer.parseInt(br.readLine());
				
				choice = 2;
				
						//choice = Integer.parseInt(br.readLine());
				
				if (choice == 1)	//Addition
				{

					//check if that service is available in registery or not - get the wsdl of the addition service
					String wsdl = getwsdlfromRS ("Addition");
					if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
						selected_RS_URL = RegisteryServer_URI;
						wsdl = getwsdlfromRS ("Addition");
						
					}
					if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
						selected_RS_URL = RegisteryServer_R1_URI;
						wsdl = getwsdlfromRS ("Addition");
						
					}
					if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
						selected_RS_URL = RegisteryServer_R2_URI;
						wsdl = getwsdlfromRS ("Addition");

					}
					
					//GET CLIENT
					System.out.println("Addition WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
					if(wsdl != null) {
						System.out.println("Loop Counter = "+ test_counter);
						System.out.println("Input Integer A :: ");
						//int a = Integer.parseInt(br.readLine());
						int a  = 8365;
						System.out.println("Input Integer B :: ");
						//int b = Integer.parseInt(br.readLine());
						int b = 789;
						test_counter ++;
						
						String add_result = dynamicSOAP_WSDLparser (wsdl,"Addition",a,b); 			
						
						System.out.println("Addition Service Result :: " + add_result);
						System.out.println("Thank You for using Addition service!! ");
						
					}else
						System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");

			
				}
				else if (choice == 2)	//Subtraction
						{
			
							//check if that service is available in registery or not - get the wsdl of the addition service
							String wsdl = getwsdlfromRS ("Subtraction");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("Subtraction");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("Subtraction");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("Subtraction");
							}
							
							//GET CLIENT
							System.out.println("Subtraction Service WSDL :: " + wsdl+ " || From RS :: " +selected_RS_URL);
							if(wsdl!=null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input Integer A :: ");
								//int a = Integer.parseInt(br.readLine());
								int a = 562;
								System.out.println("Input Integer B :: ");
								//int b = Integer.parseInt(br.readLine());
								int b = 62;
								test_counter ++;
								
								String sub_result = dynamicSOAP_WSDLparser (wsdl,"Subtraction",a,b); 
								
								System.out.println("Subtraction Service Result :: "+ sub_result);
								System.out.println("Thank You for using Subtraction service!! ");
								
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");

							
					
						}
				else if (choice == 3)	//Multiplication
						{
							//check if that service is available in registery or not - get the wsdl of the addition service
							String wsdl = getwsdlfromRS ("Multiplication");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("Multiplication");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("Multiplication");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("Multiplication");
							}
							//GET CLIENT
							System.out.println("Multiplication Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input Integer A :: ");
								int a = 586;//Integer.parseInt(br.readLine());
								System.out.println("Input Integer B :: ");
								int b = 2; //Integer.parseInt(br.readLine());
								test_counter ++;
								
								String mul_result = dynamicSOAP_WSDLparser (wsdl,"Multiplication",a,b); 
								
								System.out.println("Multiplication Service Result :: " + mul_result);
								System.out.println("Thank You for using Multiplication service!! ");
								
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");
					
						}
				else if (choice == 4)	//Division
						{
							//check if that service is available in registery or not - get the wsdl of the addition service
							String wsdl = getwsdlfromRS ("Division");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("Division");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("Division");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("Division");
							}
							//GET CLIENT
							System.out.println("Division Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input Integer A :: ");
								int a = 785; //Integer.parseInt(br.readLine());
								System.out.println("Input Integer B :: ");
								int b = 15; //Integer.parseInt(br.readLine());
								test_counter ++;
								
								String div_result = dynamicSOAP_WSDLparser (wsdl,"Division",a,b); 
						
								System.out.println("Division Service Result :: "+div_result);
								System.out.println("Thank You for using Division service!! ");
								
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");
					
						}
				else if (choice == 5)	//Echo
						{
							String wsdl = getwsdlfromRS ("Echo");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("Echo");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("Echo");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("Echo");
							}
							//GET CLIENT
							System.out.println("Echo Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input String :: ");
								String str = "HiEchoService"; //br.readLine();
								test_counter ++;
														
								String echo_result = dynamicSOAP_WSDLparser_str (wsdl,"Echo",str); 			
						
								System.out.println("Echo Service Result :: " + echo_result);
								System.out.println("Thank You for using Echo service!! ");
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");

						}
				else if (choice == 6)	//Reverse
						{
							String wsdl = getwsdlfromRS ("Reverse");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("Reverse");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("Reverse");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("Reverse");
							}
							//GET CLIENT
							System.out.println("Reverse Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input String :: ");
								String str = "IamReverse";//br.readLine();
								test_counter ++;
														
								String rev_result = dynamicSOAP_WSDLparser_str (wsdl,"Reverse",str); 	
							
								System.out.println("Reverse Service Result :: " + rev_result);
								System.out.println("Thank You for using Reverse service!! ");
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");
					
						}
				else if (choice == 7)	//Count
						{
							String wsdl = getwsdlfromRS ("Count");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("Count");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("Count");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("Count");
							}
							//GET CLIENT
							System.out.println("Count Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input String :: ");
								String str = "CountMeIshouldbe=19"; //br.readLine();
										test_counter ++;
														
								String count_result = dynamicSOAP_WSDLparser_str (wsdl,"Count",str);			
						
								System.out.println("Count Service Result :: " + count_result);
								System.out.println("Thank You for using Count service!! ");
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");

					
						}
				else if (choice == 8)	//GCD
						{
							//check if that service is available in registery or not - get the wsdl of the addition service
							String wsdl = getwsdlfromRS ("GCD");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("GCD");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("GCD");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("GCD");
							}
							//GET CLIENT
							System.out.println("GCD Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input Integer A :: ");
								int a = 88;//Integer.parseInt(br.readLine());
								System.out.println("Input Integer B :: ");
								int b = 8;//Integer.parseInt(br.readLine());
								test_counter ++;
								
								String gcd_result = dynamicSOAP_WSDLparser (wsdl,"GCD",a,b); 

				
								System.out.println("GCD Service Result :: "+ gcd_result);
								System.out.println("Thank You for using GCD service, Bye ");
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");
							
						}
				else if (choice == 9)	//LCM
						{
							//check if that service is available in registery or not - get the wsdl of the addition service
							String wsdl = getwsdlfromRS ("LCM");
							if (wsdl == null && selected_RS_URL != RegisteryServer_URI && flag1 == 1) {
								selected_RS_URL = RegisteryServer_URI;
								wsdl = getwsdlfromRS ("LCM");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R1_URI && flag2 == 1) {
								selected_RS_URL = RegisteryServer_R1_URI;
								wsdl = getwsdlfromRS ("LCM");
								
							}
							if (wsdl == null && selected_RS_URL != RegisteryServer_R2_URI && flag3 == 1) {
								selected_RS_URL = RegisteryServer_R2_URI;
								wsdl = getwsdlfromRS ("LCM");
							}
							//GET CLIENT
							System.out.println("LCM Service WSDL :: " + wsdl + " || From RS :: " +selected_RS_URL);
							if(wsdl != null) {
								System.out.println("Loop Counter = "+ test_counter);
								System.out.println("Input Integer A :: ");
								int a = 568;//Integer.parseInt(br.readLine());
								System.out.println("Input Integer B :: ");
								int b = 288;//Integer.parseInt(br.readLine());
								test_counter ++;
								
								String lcm_result = dynamicSOAP_WSDLparser (wsdl,"LCM",a,b); 
				
				
								System.out.println("LCM Service Result :: "+lcm_result);
								System.out.println("Thank You for using LCM service!!");
							}else
								System.out.println("OOPS!! Selected service currently unavailable! Please retry after sometime! \n (For more details - > please switch to Registery Server Console Output! \n Thank You! )");
							
						}
				
				
				else if (choice > 9 || choice < 0)
				{
						System.out.println("Invalid Entry!");			
				}			
					
		}//While loop ends
		
		System.out.println("Thank you for using our Distributed Services!!! ");		

	}
	
	//parser for services with 2 parameters of Integer type
	public static String dynamicSOAP_WSDLparser (String wsdl, String service_name, int param1, int param2) throws Exception { 
		//String wsdl,String service, int firstParam, int secondParam
		
				System.out.println("Call Services");
		    	//constructing SOAP request here
		    	String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:proj=\"http://proj.aos.pbg\">\r\n" +  
							  	 "   <soapenv:Header/>" +
							  	 "   <soapenv:Body>\r\n" +  
							  	 "      <proj:"+service_name+">\r\n" + 
							  	 "         <proj:a>"+param1+"</proj:a>\r\n" + 
							  	 "         <proj:b>"+param2+"</proj:b>\r\n" + 
							  	 "      </proj:"+service_name+">\r\n" + 
							  	 "   </soapenv:Body>" + 
							  	 "</soapenv:Envelope>";
		    	
    
		        WSDLParser parser = new WSDLParser();

		        Definitions defs = new Definitions();

		        try {
		        	defs = parser.parse(wsdl);
		        	
		        }catch(ResourceDownloadException e) {
		        	
		        	String error = e.getMessage();
		        	System.out.println("Exception Occured While Parsing the WSDL : "+error);
		        	return null;
		        }

		        String targetNameSpace = defs.getTargetNamespace();

		        QName serviceName = new QName(targetNameSpace, defs.getServices().get(0).getName());
		        QName portName = new QName(targetNameSpace, defs.getServices().get(0).getPorts().get(0).getName());
		        String endpointUrl = wsdl.replace("?wsdl", "");
		        String SOAPAction = defs.getBindings().get(0).getOperations().get(0).getOperation().getSoapAction();
		        
		        //invoking the method at target service and getting the result as a response after consuming that service
		        SOAPMessage response = invoke(serviceName, portName, endpointUrl, SOAPAction, request);
		        //System.out.println(response); 
		        
		        if(response == null) {
		        	System.out.println("No response receieved!!(NULL Received)");
		        	//return null;
		        }
		        SOAPBody body = response.getSOAPBody(); //getting the SOAP body from the response
		        String int_result = body.getFirstChild().getFirstChild().getTextContent(); //extractting the value from the SOAP body
		        System.out.println("Result:"+int_result);
		        return int_result;
		        
	}
	
	//parser for services with 2 parameters of String type
		public static String dynamicSOAP_WSDLparser_str (String wsdl, String service_name, String str_param) throws Exception { 
			//String wsdl,String service, int firstParam, int secondParam
			if (wsdl != null) {
				
				System.out.println("Call Services");
		    	//constructing request here
		    	String request = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:proj=\"http://proj.aos.pbg\">\r\n" +  
							  	 "   <soapenv:Header/>" +
							  	 "   <soapenv:Body>\r\n" +  
							  	 "      <proj:"+service_name+">\r\n" + 
							  	 "         <proj:str>"+str_param+"</proj:str>\r\n" + 
							  	 "      </proj:"+service_name+">\r\n" + 
							  	 "   </soapenv:Body>" + 
							  	 "</soapenv:Envelope>";

		    	  
		        WSDLParser parser = new WSDLParser();

		        Definitions defs = new Definitions();

		        System.out.println("starting PBG parsing");

		        try {
		        	defs = parser.parse(wsdl);
		        	
		        }catch(ResourceDownloadException e) {
		        	
		        	String error = e.getMessage();
		        	System.out.println("Exception Occured: "+error);
		        	return null;
		        }

		        String targetNameSpace = defs.getTargetNamespace();

		        QName serviceName = new QName(targetNameSpace, defs.getServices().get(0).getName());
		        QName portName = new QName(targetNameSpace, defs.getServices().get(0).getPorts().get(0).getName());
		        String endpointUrl = wsdl.replace("?wsdl", "");
		        String SOAPAction = defs.getBindings().get(0).getOperations().get(0).getOperation().getSoapAction();
		        
		        //invoking the method at targetted service and getting the result reponse after availing tht service
		        SOAPMessage response = invoke(serviceName, portName, endpointUrl, SOAPAction, request);
		        System.out.println(response); 
		        if(response == null) {
		        	System.out.println("No response receieved!!");
		        	//return null;
		        }
		        SOAPBody body = response.getSOAPBody(); //getting the SOAP body from the response
		        String str_result = body.getFirstChild().getFirstChild().getTextContent(); //extractting the value from the SOAP body
		        System.out.println("Result:"+str_result);
		        return str_result;
				
			}
					
			String null_result = null;
			return null_result;        
		}
	
	public static SOAPMessage invoke(QName service_name, QName port, String endpointURL, 
            String soapActionUri, String data) throws Exception {
    	
    	javax.xml.ws.Service service = javax.xml.ws.Service.create(service_name);
    	System.out.println("Step 1");
        service.addPort(port, javax.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING, endpointURL);

        Dispatch<SOAPMessage> dispatch = service.createDispatch(port, SOAPMessage.class, javax.xml.ws.Service.Mode.MESSAGE);

        dispatch.getRequestContext().put(Dispatch.SOAPACTION_USE_PROPERTY, true);
        dispatch.getRequestContext().put(Dispatch.SOAPACTION_URI_PROPERTY, soapActionUri);

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage message = messageFactory.createMessage();

        SOAPPart soapPart = message.getSOAPPart();

        StreamSource preppedMsgSrc = new StreamSource(new StringReader(data));
        soapPart.setContent(preppedMsgSrc);
        
        message.saveChanges();

        System.out.println(message.getSOAPBody().getFirstChild().getTextContent());
        //SOAPMessage response = (SOAPMessage) dispatch.invoke(message);
        SOAPMessage response = (SOAPMessage) dispatch.invoke(message);
        //System.out.println("SOAPMessage response in invoke"+response);
        if(response == null)
        	return null;

        return response;
    }
	
	public static String getwsdlfromRS(String service_name) {
		String wsdl = null;
		try{
			ClientConfig clientConfig = new ClientConfig();
			Client client = ClientBuilder.newClient(clientConfig);
			URI serviceURI = UriBuilder.fromUri(selected_RS_URL).build();
			WebTarget webTarget = client.target(serviceURI);

			wsdl = (webTarget.path("services_registery").path("servicesenquiry_wsdl").path(service_name).request()
					.accept(MediaType.TEXT_PLAIN).get(String.class));
		}catch (Exception e) {
			System.out.println("Selected Service not registered with the Registry Server!! \n(May be Server having the service you requested is not UP yet!)");
		}
		
		
		return wsdl;
	}
	
	
	public static void ping_all_RS(String RS_URL) {

		try{
			HttpURLConnection connection = (HttpURLConnection) new URL(RS_URL).openConnection();
		
		connection.setRequestMethod("HEAD");
		connection.setReadTimeout(2000);
		connection.setConnectTimeout(2000);
	
		int responseCode = connection.getResponseCode();
		if (responseCode != 200 && responseCode != 404) {
			System.out.println("Registery Server :"+RS_URL+" Not reachable !!");
			
			
		}else if(responseCode == 200 || responseCode == 404){
			//System.out.println("Is reachable!!");
			connection.disconnect();
			System.out.println("RSURL = "+ RS_URL);
			System.out.println("RegisteryServer_URI = "+RegisteryServer_URI);
			if(RS_URL == RegisteryServer_URI) {
				flag = 1;
				flag1 = 1;
				//System.out.println("Setting flag to 1");
				
			}else if(RS_URL == RegisteryServer_R1_URI ){
				flag = 2;
				flag2 = 1;
				//System.out.println("Setting flag to 2");
			
			}else if(RS_URL == RegisteryServer_R2_URI ){
				flag = 3;
				flag3 = 1;
				//System.out.println("Setting flag to 3");
				
			}
			
		}
		}catch(IOException e) {
			//e.printStackTrace();
			
			    // Not OK.
				System.out.println("Registery Server :"+RS_URL+" Not reachable !!");
				
		}
	}
	
}
