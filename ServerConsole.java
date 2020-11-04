import java.io.*;
import java.util.Scanner;

import client.*;
import ocsf.server.*;
import common.*;

public class ServerConsole implements ChatIF {

	  //Class variables *************************************************
	  
	  /**
	   * The default port to connect on.
	   */
	  final public static int DEFAULT_PORT = 5555;
	  
	  //Instance variables **********************************************
	  
	  static EchoServer server;  
	  
	  private boolean checkServer = false;
	  /**
	   * Scanner to read from the console
	   */
	  Scanner fromConsole; 

	  
	  //Constructors ****************************************************

	  /**
	   * Constructs an instance of the ClientConsole UI.
	   *
	   * @param host The host to connect to.
	   * @param port The port to connect on.
	   */
	  public ServerConsole(int port) 
	  {
		  server = new EchoServer(port);
		  fromConsole = new Scanner(System.in);

	  }

	  
	  //Instance methods ************************************************
	  
	  /**
	   * This method waits for input from the console.  Once it is 
	   * received, it sends it to the client's message handler.
	   */
	  public void accept() 
	  {
	    try
	    {
	      String message;

	      while (true) 
	      {
	        message = fromConsole.nextLine();
	       handleMessageFromServerUI(message);
	      }
	    } 
	    catch (Exception ex) 
	    {
	    	System.out.println("Unexpected error while reading from console!");
	    }
	  }
	  
	  public void handleMessageFromServerUI(String message) throws IOException
	  {

	      String userInput[] = message.split(" ");
	      
	      String commandName = userInput[0];
			
	      if(commandName.charAt(0) == '#') {
			if(commandName.equals("#quit")) {
				
					System.out.println("Server is Closing");
					server.close();
					System.exit(0);
				 
			}  else if(commandName.equals("#setport") && checkServer) {
				
					System.out.println("Port is set to: " + Integer.parseInt(userInput[1]));
					server.setPort(Integer.parseInt(userInput[1]));
				
				
			}  else if(commandName.equals("#getport")) {
				
					System.out.println(server.getPort());
						
			} else if(commandName.equals("#stop")) {
				
				System.out.println("Server has stopped listening for new clients");
				server.stopListening();
				checkServer=true;
				
			}else if(commandName.equals("#close")) {
				
				System.out.println("Server is Closing");
				server.close();
				
				
			}else if(commandName.equals("#start")) {
				
				System.out.println("Server is starting");
				server.listen();
				
			}else {
				
					System.out.println("ERROR! Command not valid");
					
			}
	      }
	      else {
	    	  server.sendToAllClients(message);
	    	  }
	      }
	   
	  
	  
	  public void display(String message) 
	  {
	    System.out.println("> " + message);
	  }
	  
	  

	  
	  //Class methods ***************************************************
	  
	  /**
	   * This method is responsible for the creation of the Client UI.
	   *
	   * @param args[0] The host to connect to.
	 * @throws IOException 
	   */
	  public static void main(String[] args) throws IOException 
	  {
		  int port = 0; //Port to listen on

		    try
		    {
		      port = Integer.parseInt(args[0]); //Get port from command line
		    }
		    catch(Throwable e)
		    {
		      port = DEFAULT_PORT; //Set port to 5555
		    }

		    ServerConsole s = new ServerConsole(port);
		    server.listen(); //Start listening for connections
		    s.accept();
		  
	  }


}
