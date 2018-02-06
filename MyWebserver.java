/* Rahul Janardhanan
	MyWebServer
*/

import java.net.*;			//Package containing standard Java_Networking Source, Library
import java.io.*;			// Package containing standard Input and Output Library

public class MyWebserver			// Primary class - webserver // also - class definition
{

	public static void main(String[] args) throws IOException		// main () with string as argument
	{
		int queue_length = 6;   //Decides the numb. of clients that can pick the specified server  // Does not need to change
		int port = 2540;        //port at which our webserver has to be connected - as mentioned in the class
		Socket sock;		// sock is the type of socket

		System.out.println(" Initialization of the My_Webserver ");		// default print statement
		System.out.println(" MyWebServer process has begun and being attentive at port Number : " + port);		//statement that appears in log

		ServerSocket servsock = new ServerSocket(port, queue_length); //creates a server socket bound / adding new label to actual class instance
		while(true)					// Checking the loop if true
				{
				 	sock = servsock.accept(); // accepting connection from type sock
					new WorkWebServer(sock).start();  //  worker thread is initialized to hold the client requests.
				 }                                //loop end
	}


}


	 class WorkWebServer extends Thread		// our worker class using the base class thread
	  {
	 	Socket sock;               // Class member, socket, local to Worker.
	 	WorkWebServer(Socket s) // This is an constructor(same name as class) // passing parameter
	 	{
			sock =s;		// variable s is assigned
		 }

					public void run(){			// run function 
						PrintStream out = null;		// starts to prints from the first, setting out
						BufferedReader in = null;		// reading in null
						try{			// start of the try -- block

							in = new BufferedReader(new InputStreamReader(sock.getInputStream()));     //invoking new i/p stream and reads from socket i/p stream  
							out = new PrintStream(sock.getOutputStream());    //invoking new o/p stream and reads from socket o/p stream 

							String Input = in.readLine();     // all the incoming request are stored in new string called Input
							System.out.println(Input);		// Shows / prints  the input returned in the log

							String[]temp;		// temp with undefined array is created 

								if (Input!=null&&Input.startsWith("GET")){             // the condition is made to check --> when the required input is not empty / null with prefix in it
								temp=Input.split(" ");  // the variable that was created before stores the input values that are made to split


								String Browserrequest  = temp[1];          // the first local variable gets stored in browser_request

					if(Browserrequest.equals("/") || Browserrequest.endsWith("/"))                //this is checking whether the required files are in the directory
						{
			           	   listfilesindirectory(Browserrequest,out);		// when it is true, this will shows the files
						}
						else if((Browserrequest.endsWith(".txt")|| Browserrequest.endsWith(".html")||Browserrequest.endsWith(".java")))  // this checks the suffix of the files, if so
						{
							readcontent(Browserrequest,out);		// returns the output

						}
						else if(Browserrequest.contains("fake-cgi"))              //this checks for the request that is for adding the numbers, if so
					{
						addnums(Browserrequest,out);		// returns the desired output
					}

						else{
								out.println("There is none of the files you are looking");				} // this gets prints, when the files that we are looking for is wrong

									sock.close();              // Temporarily the connection is closed ... browser connection won't be interrupted
								}
						}
						catch(IOException ioe){									System.out.println(ioe);              // exceptions are catched here.
								}

	}

					private void addnums(String Browserrequest, PrintStream out) {		// this is addnums function, will return the addition of two numbers. also gives the prints the output
						

						String s=Browserrequest;		//

						String[] s1=s.split("fake-cgi?");                //the incoming requests are first split and gets stored in an array of string

						System.out.println("HTTP/1.1 200 OK\r\n" +"Content-Type: " +"text/html" +"\r\n"+"Content-Length: " +"40000"+"\r\n" + "\r\n\r\n" );   //this print statement shows the type of file with the length

						String []temp=s1[1].split("&");	// undefined array of local variable temp splits the string 
				      String[] temp1=temp[1].split("=");
				       int num1=Integer.parseInt(temp1[1]);             // this takes one or two argument // gets casted into int
				       temp1=temp[2].split("=");
				       temp1=temp1[1].split(" ");
				       int num2=Integer.parseInt(temp1[0]);          // same like the previous function -- gets casted
				       temp1=temp[0].split("=");
				       String personName=temp1[1];		// new string person_name is used
				       int result=num1+num2;	// Returns the result of adding one number and other
   out.println("Hello "+personName+ "\r\n");		// this line prints the user's name
                                                                    
   out.println(" The outcome of the two numbers "+num1+" and "+num2+" is : "+result);	// shows the product of the added numbers.
					}

					private void listfilesindirectory(String Browserrequest, PrintStream out) {	 //new function for listing the current directory -- with two parameters
				
						String File;		// new string is created
						String fileType = "text/html";		//the type of file is assigned to html and text format
						out.println("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType +"\r\n"+"Content-Length: " +"40000"+"\r\n" + "\r\n\r\n" );		// this line prints the content with file_type as default parameter

						out.println("<html><head>\r\n</head>\r\n<body>\r\n");		// this produces the head and body of the content
						if(Browserrequest.equals("/"))
						{
							out.print("<h1> Copyrights_Rahul Directories-Files</h1>\r\n");	// this is what the title looks 
							File = "./";
						}
						else
						{
						File = Browserrequest .substring(1); // the subset of particular string will be accessed
						out.println("<h1>Files in the "+File+" directory</h1>\r\n");		// shows the list of files in the specified directory
						}

						File Directory = new File(File);                           // in the current directory, it is checking for the new file
						System.out.println("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType +"\r\n"+"Content-Length: " +"2356"+"\r\n" + "\r\n\r\n" );  //default print statement which produces --length of the file and what kind of file

						int fileLength = File.length();                            //checking the length of each files in folders
						File[] foldersList = Directory.listFiles();                // type file with array stores the list of files..
						String fileAddress;		// a new string with address if the file is created.
						for ( int a = 0 ; a < foldersList.length ; a ++ )    // this for_loop helps us to iterate each files and its length
							{

  								if ( foldersList[a].isDirectory ( ) )
  								{   
  									fileAddress=foldersList[a].getPath().substring(fileLength);  //getting each file address with using the default java fucntion get_path() 
  									out.print ( "<a href= "+fileAddress+"/"+">"+fileAddress+"</a><br>\r\n") ; // hotlinks will be used to send html type to browser
  								}
  								else if ( foldersList[a].isFile ( ) )                                         // this condition checks whether the file type is true
  								{

  									fileAddress=foldersList[a].getPath().substring(fileLength);               // this is same as above function -- path finder - address
									out.print (  "<a href="+fileAddress+">"+fileAddress+"</a><br>\r\n") ;		// address of the file is used..
									}
							}
							out.print("</body></html>");

					}

					private void readcontent(String Browserrequest, PrintStream out) {
					String	fileName = Browserrequest.substring(1);      //removes "/" from the request -- gets sotred in the particular file_name
					String fileType=null;		// the type is null value
						if(fileName.endsWith(".html"))                 // checks and assigns the file type. this checks how the appropriate file will end and be assigned
						{
							fileType = "text/html";	// condition 1
						}else
						{
							fileType = "text/plain";  // condition 2
						}

						try{
								FileReader filereader = new FileReader(fileName);        // a separate variable is created to store the contents that are read

								File file = new File(fileName);                   // new file is created -- the file_name itself is passed as an argument

								BufferedReader readfile=new BufferedReader(filereader);
								System.out.println("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType + "\r\n"+"Content-Length: " +file.length()+"\r\n"+"\r\n\r\n" ); // Prints / the exact info -- the details of the file.
								out.println("HTTP/1.1 200 OK\r\n" +"Content-Type: " +fileType + "\r\n"+"Content-Length: " +"40000"+file.length()+"\r\n"+"\r\n\r\n" ); // sends the file details to the browser // takes file_type default

								try {

					    	String line=readfile.readLine();                //first line is read and stored in String

        				       while (line!=null)		// the condition that checks the line should not be null or empty
        				       {
        				    	   out.println(line+ "\r\n");                      // prints each line
        				    	   line=readfile.readLine();               // it reads in sequence of line
        				       }                              //loop continues until line is null i.e no more content to display.
           					 }
    				         catch (IOException e)
    				         {
    				         	 System.out.println(e);                 // when exceptions raised, it will prints
    				           }
    				        }
							 catch(FileNotFoundException e)	 // Catches / solves the exception problem
								{                                        
									out.println("File not founded so exception raised ");
								}

					}




	  }
