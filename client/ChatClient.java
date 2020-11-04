// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI;
  private boolean checkLoggedIn = false;
  private static String userLogin;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, String userLogin, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.userLogin = userLogin;
    
    openConnection();
    
    
    sendToServer("#login "+ userLogin);
    checkLoggedIn=true;
  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      String userInput[] = message.split(" ");
      
      String commandName = userInput[0];
		
      if(commandName.charAt(0) == '#') {
		if(commandName.equals("#quit")) {
			
				System.out.println("Connection is Closing");
				quit();
			 
		} else if(commandName.equals("#logoff")) {
			
				checkLoggedIn = true;
				sendToServer("client has logged off");
				System.out.println("Connection is Closing");
				closeConnection();
			
		} else if(commandName.equals("#sethost") && checkLoggedIn) {
			
				System.out.println("Host is set to: " + userInput[1]);
				setHost(userInput[1]);
			
		} else if(commandName.equals("#setport") && checkLoggedIn) {
			
				System.out.println("Port is set to: " + Integer.parseInt(userInput[1]));
				setPort(Integer.parseInt(userInput[1]));
			
			
		} else if(commandName.equals("#login") && checkLoggedIn) {
			if(userInput.length == 2) {
				if(userInput[1].equals(userLogin)) {
						System.out.println("Opening connection to server");
						openConnection();
						sendToServer("#login " + userLogin);
						checkLoggedIn = false;
					} 
					
				
			} else {
				System.out.println("Command must be followed by loginID i.e.: #login <loginID>");
			}
				
			
		} else if(commandName.equals("#gethost")) {
			
				System.out.println(getHost());
			
		} else if(commandName.equals("#getport")) {
			
				System.out.println(getPort());
			
		} else {
			
				System.out.println("ERROR! Command not valid");
				
		}
			
		} else
			sendToServer(message);
  
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  
  @Override
  protected void connectionClosed() {
	  System.out.println("Server has shut down");
	}
  
  @Override
  protected void connectionException(Exception exception) {
	  
	  clientUI.display("WARNING - The server has stopped listening for connections\r\n" + 
	  		"SERVER SHUTTING DOWN! DISCONNECTING!\r\n" + 
	  		"");
	  quit();
  }
  
}


//End of ChatClient class
