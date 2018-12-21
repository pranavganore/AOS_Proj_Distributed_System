package pbg.aos.proj;

public class LCM_Service {
	public int LCM(int a, int b) {
		int max, step, lcm = 0;
		
		if(a > b){
	        max = step = a;
	     }
	     else{
	        max = step = b;
	     }

	     while(a!= 0) {
	        if(max % a == 0 && max % b == 0) {
	           lcm = max;
	           break;
	        }
	        max += step;
	     }
		return lcm;
	}


}
