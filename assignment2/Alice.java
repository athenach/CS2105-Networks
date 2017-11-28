/*
Name: Shradheya Thakre
Student number: A0161476B
Is this a group submission (yes/no)? no

If it is a group submission:
Name of 2nd group member: THE_OTHER_NAME_HERE_PLEASE
Student number of 2nd group member: THE_OTHER_NO

*/


// Please DO NOT copy from the Internet (or anywhere else)
// Instead, if you see nice code somewhere try to understand it.
//
// After understanding the code, put it away, do not look at it,
// and write your own code.
// Subsequent exercises will build on the knowledge that
// you gain during this exercise. Possibly also the exam.
//
// We will check for plagiarism. Please be extra careful and
// do not share solutions with your friends.
//
// Good practices include
// (1) Discussion of general approaches to solve the problem
//     excluding detailed design discussions and code reviews.
// (2) Hints about which classes to use
// (3) High level UML diagrams
//
// Bad practices include (but are not limited to)
// (1) Passing your solution to your friends
// (2) Uploading your solution to the Internet including
//     public repositories
// (3) Passing almost complete skeleton codes to your friends
// (4) Coding the solution for your friend
// (5) Sharing the screen with a friend during coding
// (6) Sharing notes
//
// If you want to solve this assignment in a group,
// you are free to do so, but declare it as group work above.




import java.net.*;
import java.nio.*;
import java.util.Arrays;
import java.util.Scanner;
import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.nio.Buffer.*;


class Alice {
    private int seqNum = 0;
    private char globalType;
    private DatagramSocket socket;

    public static void main(String[] args) throws Exception {
        // Do not modify this method
        if (args.length != 2) {
            System.out.println("Usage: java Alice <host> <unreliNetPort>");
            System.exit(1);
        }
        InetAddress address = InetAddress.getByName(args[0]);
        new Alice(address, Integer.parseInt(args[1]));
    }

    public Alice(InetAddress address, int port) throws Exception {
        // Do not modify this method
        socket = new DatagramSocket();
        socket.setSoTimeout(100);

        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            handleLine(line, socket, address, port);
            // Sleep a bit. Otherwise (if we type very very fast)
            // sunfire might get so busy that it actually drops UDP packets.
            Thread.sleep(20);
        }
        socket.close();
    }

    public void handleLine(String line, DatagramSocket socket, InetAddress address, int port) throws Exception {
        // Do not modify this method
        if (line.startsWith("/send ")) {
            String path = line.substring("/send ".length());
            System.err.println("Sending file: " + path);
            try {
                File file = new File(path);
                if (!(file.isFile() && file.canRead())) {
                    System.out.println("Path is not a file or not readable: " + path);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Could not read " + path);
                return;
            }
            sendFile(path, socket, address, port);
            System.err.println("Sent file.");
        } else {
            sendMessage(line, socket, address, port);
        }
    }

    public long getCheckSum(byte[] file){
    	CRC32 crc = new CRC32();
    	crc.update(file);
    	return crc.getValue();
    }

    public void sendFile(String path, DatagramSocket socket, InetAddress address, int port) throws Exception {
        // Implement me!
    	globalType = 'F';
        FileInputStream fis;
        int numbytes=0;
		BufferedInputStream bis;

		byte[] buffer=new byte[494];

		try{
			fis=new FileInputStream(path);
			bis= new BufferedInputStream(fis);


			while(numbytes!=-1){
							
				numbytes=bis.read(buffer);
				if(numbytes==-1)
					break;
				
				ByteBuffer bbf = ByteBuffer.allocate(512);

				byte[] messageData = new byte[numbytes];
            	for (int j = 0; j < numbytes; j++) {
                	messageData[j] = buffer[j];
            	}

            	ByteBuffer temp = ByteBuffer.allocate(504);
		    	temp.putInt(seqNum);
		    	temp.putChar('F');
		        temp.putInt(messageData.length);
		        temp.put(messageData);

	        	//header
		        bbf.putInt(seqNum);
		        //System.out.println(numbytes);
		        bbf.putLong(getCheckSum(temp.array()));
		        //System.out.println(getCheckSum(message.getBytes()));
		        bbf.putChar('F'); // to denote message
		        bbf.putInt(numbytes);
		        bbf.put(buffer);

		        byte[] dataToSend = bbf.array();

		        DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, address, port);
		        socket.send(packetToSend);
		        waitForACK(path, socket, address, port, packetToSend);
			}

				ByteBuffer bbf = ByteBuffer.allocate(512);

				byte[] messageData = new byte[0];

            	ByteBuffer temp = ByteBuffer.allocate(504);
		    	temp.putInt(seqNum);
		    	temp.putChar('F');
		        temp.putInt(messageData.length);
		        temp.put(messageData);

	        	//header
		        bbf.putInt(seqNum);
		        //System.out.println(numbytes);
		        bbf.putLong(getCheckSum(temp.array()));
		        //System.out.println(getCheckSum(message.getBytes()));
		        bbf.putChar('F'); // to denote message
		        bbf.putInt(0);
		        bbf.put(messageData);

		        byte[] dataToSend = bbf.array();

		        DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, address, port);
		        socket.send(packetToSend);
		        waitForACK(path, socket, address, port, packetToSend);

			bis.close();
			fis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
    }


    public void sendMessage(String message, DatagramSocket socket, InetAddress address, int port) throws Exception {
        // Implement me!
    	globalType = 'M';
    	ByteBuffer temp = ByteBuffer.allocate(504);
    	temp.putInt(seqNum);
    	temp.putChar('M');
    	byte[] tempBytes = message.getBytes();
        temp.putInt(tempBytes.length);
        temp.put(message.getBytes());


        ByteBuffer bbf = ByteBuffer.allocate(512);

        //header
        bbf.putInt(seqNum);
        bbf.putLong(getCheckSum(temp.array()));
        //System.out.println(getCheckSum(message.getBytes()));
        bbf.putChar('M'); // to denote message
        byte[] messageInBytes = message.getBytes();
        bbf.putInt(messageInBytes.length);
        bbf.put(message.getBytes());

        byte[] dataToSend = bbf.array();

        DatagramPacket packetToSend = new DatagramPacket(dataToSend, dataToSend.length, address, port);
        socket.send(packetToSend);
        waitForACK(message, socket, address, port, packetToSend);
    }


    public void waitForACK(String line, DatagramSocket socket, InetAddress address, int port, DatagramPacket packetToSend) throws Exception {

    	char typeOfMessage = 'M';
    	try{
	    	socket.setSoTimeout(100);
	    	byte[] recievedACK = new byte[512];
	    	DatagramPacket packetReceived = new DatagramPacket(recievedACK, recievedACK.length);
	    	socket.receive(packetReceived);


	    	

	    	ByteBuffer bbfR = ByteBuffer.wrap(packetReceived.getData());
	    	
	    	int  receivedSeqNum = bbfR.getInt();
	    	typeOfMessage = bbfR.getChar();
	    	long recievedChecksum = bbfR.getLong();

	    	ByteBuffer temp = ByteBuffer.allocate(6);
       	 	temp.putInt(seqNum);
        	temp.putChar(typeOfMessage);

        	long calculatedCheckSum = getCheckSum(temp.array());

	    	


	    	if(calculatedCheckSum != recievedChecksum){
	    		if(globalType == 'M')
	    			sendMessage(line, socket, address, port);
	    		else{
	    				socket.send(packetToSend);
	    				waitForACK(line, socket, address, port, packetToSend);
	    		
	    		}
	    	}

	    	else{
	    		if(seqNum == receivedSeqNum){
	    			if(seqNum == 0)
	    				seqNum = 1;
	    			else
	    				seqNum = 0;
	    		}
	    		else{
	    			if(globalType == 'M')
	    				sendMessage(line, socket, address, port);
	    			else{
	    				//System.out.println(calculatedCheckSum + " " + recievedChecksum  + " " + seqNum + " " + receivedSeqNum);

	    				socket.send(packetToSend);
	    				waitForACK(line, socket, address, port, packetToSend);
	    			}
	    		}
	    	}
    	}catch(Exception e){
    		if(globalType == 'M')
	    		sendMessage(line, socket, address, port);
	    	else{
	    		//System.out.println("timeout");
	    		socket.send(packetToSend);
	    		waitForACK(line, socket, address, port, packetToSend);
	    		
    		}
	    }
    	
    }
}