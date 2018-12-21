package pbg.aos.proj;

public class Count_Service {
	public String Count (String str) {
		int count;
		count = str.length();
		String strcount = Integer.toString(count);
		return strcount;
	}

}
