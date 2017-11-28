/*
Name: Shradheya Thakre
Student number: A0161476B
Is this a group submission (yes/no)?

If it is a group submission:
Name of 2nd group member: THE_OTHER_NAME_HERE_PLEASE
Student number of 2nd group member: THE_OTHER_NO

*/


import java.net.*;
import java.nio.*;
import java.io.*;
import java.util.zip.*;
import java.nio.Buffer.*;


class Bob {
    private int seqNum = 1;
    DatagramSocket socket;

    public static void main(String[] args) throws Exception {
        // Do not modify this method
        if (args.length != 1) {
            System.out.println("Usage: java Bob <port>");
            System.exit(1);
        }
        new Bob(Integer.parseInt(args[0]));
    }

    public Bob(int port) throws Exception {
        // Implement me

        socket = new DatagramSocket(port);
        while(true) {
            extractData(socket);
        }
    }


    public void extractData(DatagramSocket socket) throws Exception {

        byte[] receivedData = new byte[512];
        //System.out.println("   ");
        DatagramPacket rcvPacket = new DatagramPacket(receivedData, receivedData.length);
        socket.receive(rcvPacket);

        ByteBuffer bbf = ByteBuffer.wrap(rcvPacket.getData());
        int receivedSeqNum = bbf.getInt(0);
        long rcvCheckSum = bbf.getLong(4);
        char typeOfData = bbf.getChar(12);
        int size = bbf.getInt(14);

                if(size > 494 || size<0){
                    sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'F');
                }
                else{
                byte[] messageData = new byte[size];
                //System.out.println(size);
                for (int j = 0; j < size; j++) {
                    messageData[j] = bbf.get(j+18); 
                }

                ByteBuffer temp = ByteBuffer.allocate(504);
                temp.putInt(receivedSeqNum);
                temp.putChar(typeOfData);
                temp.putInt(size);
                temp.put(messageData);

                long calculatedCheckSum = getCheckSum(temp.array());
            if(calculatedCheckSum == rcvCheckSum){
                if(typeOfData == 'M')
                    extractDataMessage(socket, bbf, receivedSeqNum, rcvCheckSum, typeOfData, size, rcvPacket);
                if(typeOfData == 'F' )
                    extractDataFile(socket, bbf, receivedSeqNum, rcvCheckSum, typeOfData, size, rcvPacket);
            }
            else
                sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'F');
        }
    }





    public void extractDataFile(DatagramSocket socket, ByteBuffer bbf, int receivedSeqNum, long rcvCheckSum,
     char typeOfData, int size, DatagramPacket rcvPacket) throws Exception {

        int i = 0;
        FileOutputStream fos;
        BufferedOutputStream bos;
       // System.out.println("new file");
      	
       if(size==0){
       		sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'F');
       		return;
       }


        fos= new FileOutputStream("output");
        bos= new BufferedOutputStream(fos);
        bos.flush();
        while(true) {

        
            byte[] receivedData;


            if(i != 0) {

                receivedData = new byte[512];

                rcvPacket = new DatagramPacket(receivedData, receivedData.length);
                socket.receive(rcvPacket);

                bbf = ByteBuffer.wrap(rcvPacket.getData());
                receivedSeqNum = bbf.getInt(0);
                rcvCheckSum = bbf.getLong(4);
                typeOfData = bbf.getChar(12);
                size = bbf.getInt(14);

            }
            //System.out.println(size);
            if(size>494 || size<0) {
                i++;
                //System.out.println(size + " if part");
                sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'F');
                continue;
            }

            else{
                byte[] messageData = new byte[size];
                //System.out.println(size);
                for (int j = 0; j < size; j++) {
                    messageData[j] = bbf.get(j+18); 
                }

                ByteBuffer temp = ByteBuffer.allocate(504);
                temp.putInt(receivedSeqNum);
                temp.putChar(typeOfData);
                temp.putInt(size);
                temp.put(messageData);

                long calculatedCheckSum = getCheckSum(temp.array());
                  //System.out.println(calculatedCheckSum + "  " + rcvCheckSum);

                  if(calculatedCheckSum == rcvCheckSum) {
                    if(seqNum != receivedSeqNum) {
                        bos.write(messageData,0,size);
                        //System.out.println(size + " received");
                        seqNum = receivedSeqNum;
                        if(size==0)
                            {
                                i++;
                                bos.close();
        						fos.close();
                                sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'F');                                
                               	break;
                            }
                        
                        //System.out.println(size+ " added");

                    }
                }

                  i++;
              
                  sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'F');
            }

        }
        



    }


    public void extractDataMessage(DatagramSocket socket, ByteBuffer bbf, int receivedSeqNum, long rcvCheckSum,
     char typeOfData, int size, DatagramPacket rcvPacket) throws Exception {


       // System.out.println("going to messageData");
        if(size > 494 || size<0){
            sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'M');
        }

        else{
        byte[] messageData = new byte[size];
         for (int i = 0; i < size; i++) {
          messageData[i] = bbf.get(i+18); 
      }

      //System.out.println(messageData.length);

      String message = new String(messageData);
      //System.out.println(message);


      ByteBuffer temp = ByteBuffer.allocate(504);
        temp.putInt(receivedSeqNum);
        temp.putChar(typeOfData);
        byte[] tempBytes = message.getBytes();
        temp.putInt(tempBytes.length);
        temp.put(message.getBytes());
      long calculatedCheckSum = getCheckSum(temp.array());
      //System.out.println(calculatedCheckSum + "  " + rcvCheckSum);

      if(calculatedCheckSum == rcvCheckSum) {
        if(seqNum != receivedSeqNum) {
            seqNum = receivedSeqNum;
            printMessage(message);

        }
      }

      sendACK(socket, rcvPacket.getAddress(), rcvPacket.getPort(), 'M');
  }
    }


    public void sendACK(DatagramSocket socket, InetAddress address, int port, char type) throws Exception {

        ByteBuffer temp = ByteBuffer.allocate(6);
        temp.putInt(seqNum);
        temp.putChar(type);

        ByteBuffer bbf = ByteBuffer.allocate(512);

        
        long calculatedCheckSum = getCheckSum(temp.array());

        
        bbf.putInt(seqNum);
        bbf.putChar(type);
        bbf.putLong(calculatedCheckSum);

        byte[] ack = bbf.array();

        DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, address, port);
        socket.send(ackPacket);

    }

    public void printMessage(String message) {
        // Do not modify this method
        // Call me to print out the messages!
        System.out.println(message);
    }

    public long getCheckSum(byte[] file){
        CRC32 crc = new CRC32();
        crc.update(file);
        return crc.getValue();
    }
}