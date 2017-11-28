import java.util.*;
import java.lang.*;

public class IPAddress{
	public static void main(String args[]){

		int[] address=new int[4];
		String[] s= new String[4];

		s[0]=args[0].substring(0,8);
		s[1]=args[0].substring(8,16);
		s[2]=args[0].substring(16,24);
		s[3]=args[0].substring(24);

		int sum=0;

		for(int i=0;i<4;i++)
		{
			sum=0;
			for(int j=0;j<s[i].length();j++)
			{
				sum+=(Character.getNumericValue(s[i].charAt(j))*Math.pow(2,7-j));
			}
			address[i]=sum;
		}

		System.out.println(address[0]+"."+address[1]+"."+address[2]+"."+address[3]);

	}
}
