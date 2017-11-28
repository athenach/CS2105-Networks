import java.util.*;
import java.nio.file.*;
import java.util.zip.*;
import java.io.*;


public class Checksum{
	public static void main(String[] args){

		String src=args[0];

		try{
			byte[] bytes = Files.readAllBytes(Paths.get(src));

			CRC32 crc=new CRC32();
			crc.update(bytes);
			System.out.println(crc.getValue());
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
