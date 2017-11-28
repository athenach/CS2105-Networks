# CS2105-Networks
Programs Related to Introduction to Network

## View Assignemnts Directly 

* [Assignement 0- Basic Java Programs for Networking Concepts] (https://github.com/tshradheya/CS2105-Networks/blob/master/assignment0)

** [Checksum.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment0/Checksum.java)
** [Copier.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment0/Copier.java)
** [IPAddress.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment0/IPAddress.java)

* [Assignement 1- Web Server to handle HTTP Requests] (https://github.com/tshradheya/CS2105-Networks/blob/master/assignment1)

** [WebServer.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment1/WebServer.java)

* [Assignement 2 - Implementing Stop and Wait Reliable protocol on UDP] (https://github.com/tshradheya/CS2105-Networks/blob/master/assignment2)

** [Alice.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment2/Alice.java)
** [Bob.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment2/Bob.java)

* [Assignement 3- Security for sending messages using Ecryption] (https://github.com/tshradheya/CS2105-Networks/blob/master/assignment3)

** [Alice.java](https://github.com/tshradheya/CS2105-Networks/blob/master/assignment3/Alice.java)




## Warnings

* In assignment 1 the WebServer will not be able to handle multiple requests on the same connection. However it will work on browser as parallel connections will be made. 

  E.g. `GET /demo.html HTTP/1.1\r\nHost: localhost\r\n\r\nGET /demo.html HTTP/1.1\r\nHost: localhost\r\n\r\n" | netcat localhost    <port_your_server_is_running>` will only return 1 response when two are expected. 
  
  An easy fix would be to make the recursive function in the form of an iterative loop. Too lazy to fix now :P

* Copying the same code for Assignemnts in future could result in plagarism. Aim to use this code only for refernce.
