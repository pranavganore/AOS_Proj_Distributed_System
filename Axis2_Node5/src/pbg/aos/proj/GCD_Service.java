package pbg.aos.proj;

import java.math.BigInteger;

public class GCD_Service {
	public int GCD(int a, int b) {
/*		BigInteger gcd;
		gcd = a.gcd(b);
		return gcd;
		*/
    	//Lets take two numbers 55 and 121 and find their GCD
        //int num1 = 55, num2 = 121;
		
		
		int gcd = 1;

        /* loop is running from 1 to the smallest of both numbers
         * In this example the loop will run from 1 to 55 because 55
         * is the smaller number. All the numbers from 1 to 55 will be 
         * checked. A number that perfectly divides both numbers would
         * be stored in variable "gcd". By doing this, at the end, the 
         * variable gcd will have the largest number that divides both
         * numbers without remainder.
         */
        for(int i = 1; i <= a && i <= b; i++)
        {
            if(a%i==0 && b%i==0)
                gcd = i;
        }
        return gcd;

        //System.out.printf("GCD of %d and %d is: %d", num1, num2, gcd);
		
	}

}
