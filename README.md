# CS2105-Networks
:bookmark_tabs: :books: Programs Related to Introduction to Network


## Warnings

* In assignment 1 the WebServer will not be able to handle multiple requests on the same connection. However it will work on browser as parallel connections will be made. 

  E.g. `GET /demo.html HTTP/1.1\r\nHost: localhost\r\n\r\nGET /demo.html HTTP/1.1\r\nHost: localhost\r\n\r\n" | netcat localhost    <port_your_server_is_running>` will only return 1 response when two are expected. 
  
  An easy fix would be to make the recursive function in the form of an iterative loop. Too lazy to fix now :P

* Copying the same code for Assignemnts in future could result in plagarism. Aim to use this code only for refernce.
