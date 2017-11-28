import java.util.*;
import java.lang.*;
import java.io.*;



public class Copier{
	public static void main(String[] args){

		String source=args[0];
		String destination=args[1];

		FileInputStream fis;
		FileOutputStream fos;
		int numbytes=0,total=0;
		BufferedInputStream bis;
		BufferedOutputStream bos;

		byte[] buffer=new byte[1024];
		try{
			fis=new FileInputStream(source);
			bis= new BufferedInputStream(fis);

			
			fos= new FileOutputStream(destination);
			bos= new BufferedOutputStream(fos);

			while(numbytes!=-1){
							
				numbytes=bis.read(buffer);
				if(numbytes==-1)
					break;
				bos.write(buffer,0,numbytes);
			}
			bis.close();
			bos.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		System.out.println(source+" is successfully copied to "+destination);
	}
}
