/*
Name: Shradheya Thakre
Student number: A0161476B
Is this a group submission (yes/no)? 

If it is a group submission: No
Name of 2nd group member: THE_OTHER_NAME_HERE_PLEASE
Student number of 2nd group member: THE_OTHER_NO

*/


import java.net.*;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.io.*;
import java.util.ArrayList;



public class WebServer {
    public static void main(String[] args) throws IOException {
        // dummy value that is overwritten below
        int port = 8080;
        try {
          port = Integer.parseInt(args[0]);
        } catch (Exception e) {
          System.out.println("Usage: java webserver <port> ");
          System.exit(0);
        }

        WebServer serverInstance = new WebServer();
        serverInstance.start(port);
    }

    private void start(int port) throws IOException{
      System.out.println("Starting server on port " + port);
      // START_HERE

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
      
      


      // NEEDS IMPLEMENTATION
      // You have to understand how sockets work and how to program
      // them in Java.
      // A good starting point is the socket tutorial from Oracle
      // http://docs.oracle.com/javase/tutorial/networking/sockets/
      // But there are a billion other resources on the Internet.
      //
      // Hints
      // 1. You should set up the socket(s) and then call handleClientSocket.


    ServerSocket welcomeSocket = new ServerSocket(port);
    System.out.println("Server listens at port " + port + "...");
    
    while (true) {  // server runs forever
      // Create a separate socket to connect to a client
      Socket connectionSocket = welcomeSocket.accept();
      //System.out.println(welcomeSocket.getLocalPort());


      handleClientSocket(connectionSocket);
    }
}

    /**
     * Handles requests sent by a client
     * @param  client Socket that handles the client connection
     */
    private void handleClientSocket(Socket client) throws IOException {
      // NEEDS IMPLEMENTATION
      // This function is supposed to handle the request
      // Things to do:
      // (1) Read the request from the socket 
      // (2) Parse the request and set variables of 
      //     the HttpRequest class (at the end of the file!)
      // (3) Form a response using formHttpResponse.
      // (4) Send a response using sendHttpResponse.
      //
      // A BufferedReader might be useful here, but you can also
      // solve this in many other ways.

       
       //client.setSoTimeout(2000);

       
      InputStreamReader is= new InputStreamReader(client.getInputStream());
      BufferedReader input = new BufferedReader(is);
      HttpRequest hr= new HttpRequest();
 	       
	  String takeInputString = input.readLine();
	  System.out.println(takeInputString);
	  System.out.println(client.getPort());
	  String s;
	  //int i=0;
	  while(true){
	  	s=input.readLine();
	  	System.out.println(s);
	  	if(s!=null&&!s.equals("")){
			hr.addHeader(s);
			//System.out.println(s);
	  	}
	  	else
	  		break;

	  }

      
      hr.parseRequest(takeInputString);



      byte[] data;
      data=formHttpResponse(hr);

      sendHttpResponse(client,data,hr);


      if(hr.isPersistent()){
      	try{
      	client.setSoTimeout(2000);
      	 handleClientSocket(client);
      }
      catch(Exception e){
      	System.out.println("TIMEOUT");
      	client.close();
      }

       
    }
      else{
          client.close();
      }



      
      client.close();

    }

    /**
     * Sends a response back to the client
     * @param  client Socket that handles the client connection
     * @param  response the response that should be send to the client
     */
    private void sendHttpResponse(Socket client, byte[] response, HttpRequest request) throws IOException {

    	String v;

    	if(request.isPersistent())
      	v="1.1";
      else
      	v="1.0";


      String r;

      if(request.getStatus())
      	r="200 OK";
      else
      	r="404 Not Found";


      StringBuilder sb= new StringBuilder();
	  sb.append("HTTP/");
	  sb.append(v+" ");
	  sb.append(r+"\r\n");
	  sb.append("Content-Length: "+response.length+"\r\n");
	  sb.append("\r");



      


BufferedOutputStream bos = new BufferedOutputStream(client.getOutputStream());
      bos.write(response);
      bos.flush();



      // NEEDS IMPLEMENTATION
    }

    /**
     * Form a response to an HttpRequest
     * @param  request the HTTP request
     * @return a byte[] that contains the data that should be send to the client
     */
    private byte[] formHttpResponse(HttpRequest request) throws IOException {
      // NEEDS IMPLEMENTATION
      // Make sure you follow the (modified) HTTP specification
      // in the assignment regarding header fields and newlines
      // You might want to use the concatenate method,
      // but you do not have to.
      // If you want to you can use a StringBuilder here
      // but it is possible to solve this in multiple different ways.
      byte[] buffer;

      String v;

    	if(request.isPersistent())
      	v="1.1";
      else
      	v="1.0";


      

      try{
      buffer=Files.readAllBytes(Paths.get(request.getFilePath().substring(1)));
	  request.setStatus(true);

	  String r;

      if(request.getStatus())
      	r="200 OK";
      else
      	r="404 Not Found";


      StringBuilder sb= new StringBuilder();
	  sb.append("HTTP/");
	  sb.append(v+" ");
	  sb.append(r+"\r\n");
	  sb.append("Content-Length: "+buffer.length+"\r\n");
	  sb.append("\r\n");

	  //System.out.print(sb.toString());


	  return concatenate(sb.toString().getBytes(),buffer);
	  }
	  catch(IOException e){
	  	request.setStatus(false);

	  	byte[] buffer2;
	  	buffer2=form404Response(request);

	  	String r;

      if(request.getStatus())
      	r="200 OK";
      else
      	r="404 Not Found";


      StringBuilder sb= new StringBuilder();
	  sb.append("HTTP/");
	  sb.append(v+" ");
	  sb.append(r+"\r\n");
	  sb.append("Content-Length: "+buffer2.length+"\r\n");
	  sb.append("\r\n");

	  //System.out.print(sb.toString());



	  return concatenate(sb.toString().getBytes(),buffer2);

	  }



    }


    /**
     * Form a 404 response for a HttpRequest
     * @param  request a HTTP request
     * @return a byte[] that contains the data that should be send to the client
     */
    private byte[] form404Response(HttpRequest request) {
        // NEEDS IMPLEMENTATION
        // This should return a 404 response
        // You should use it where appropriate
        // To get the content of the 404 response
        // call get404Content.
        // If you want to you can use a StringBuilder here
        // but it is possible to do this in multiple different ways.

        return get404Content(request.getFilePath().toString()).getBytes();
    }
    

    /**
     * Concatenates 2 byte[] into a single byte[]
     * This is a function provided for your convenience.
     * @param  buffer1 a byte array
     * @param  buffer2 another byte array
     * @return concatenation of the 2 buffers
     */
    private byte[] concatenate(byte[] buffer1, byte[] buffer2) {
        byte[] returnBuffer = new byte[buffer1.length + buffer2.length];
        System.arraycopy(buffer1, 0, returnBuffer, 0, buffer1.length);
        System.arraycopy(buffer2, 0, returnBuffer, buffer1.length, buffer2.length);
        return returnBuffer;
    }

    /**
     * Returns a string that represents a 404 error
     * You should use this string as the return website
     * for 404 errors.
     * @param  filePath path of the file that caused the 404
     * @return a String that represents a 404 error website
     */
    private String get404Content(String filePath) {
      // You should not change this function. Use it as it is.
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>");
        sb.append("404 Not Found");
        sb.append("</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h1>404 Not Found</h1> ");
        sb.append("<p>The requested URL <i>" + filePath + "</i> was not found on this server</p>");
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }
}



class HttpRequest {
    // NEEDS IMPLEMENTATION
    // This class should represent a HTTP request.
    // Feel free to add more attributes if needed.
    private String filePath;
   	boolean status=true; // true = 200 OK,   false= 404 Error
   	boolean isPersistent;
   	ArrayList<String> headers= new ArrayList<String>();


    String getFilePath() {
        return filePath;
    }

    public boolean getStatus(){
    	return status;
    }

    public boolean isPersistent(){
    	return isPersistent;
    }

    public void setStatus(boolean b){
    	status=b;
    }

    public void addHeader(String s){
    	headers.add(s);
    }


    public void parseRequest(String input){
    	String[] parts=input.split(" ");

    	if(!parts[0].equals("GET"))
    		status=false;

    	filePath=parts[1];

    	if(parts[2].substring(5).equals("1.1"))
    		isPersistent=true;
    	else
    		isPersistent=false;


    }


    // NEEDS IMPLEMENTATION
    // If you add more private variables, add you getter methods here
}