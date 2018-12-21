package pbg.aos.proj;

public class Reverse_Service {
	
	public String Reverse (String str) {		
		str = new StringBuilder(str).reverse().toString();
		return str;
	}

}
