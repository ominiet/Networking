import java.net.*;
import java.io.*;
import java.util.*;

public class network {
	private ServerSocket serverSocket;
  private Socket senderSocket;
  private Socket receiverSocket;
  private PrintWriter out;
  private BufferedReader in;
	private Random r;

	network(){
		r = new Random();
	}

  public network(int port) throws IOException{
  		startServer(port);
  }
  public void startServer(int port) throws IOException {	//open server socket
      serverSocket = new ServerSocket(port);
      startClient();
  }

  public void startClient() throws IOException {		//open client socket
  	senderSocket = serverSocket.accept();
      out = new PrintWriter(senderSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
      out.println("Hello!");
  }

  public void stopClient() throws IOException {//close only client
  		in.close();
  		out.close();
  		senderSocket.close();
  }


	int getAction(){
		double prob = r.nextDouble();
		if (prob < 0.5){
			return 0;	//PASS
		}
		else if (prob < 0.75){
			return 1; //CORRUPT
		}
		else if (prob < 1.0){
			return 2; //DROP
		}
		else return -1;//ERROR
	}
	Package getPackage(){
		byte seqNum = in.readLine();
		byte id = in.readLine();
		int sum = in.readLine();
		String data = in.readLine();

		System.out.println("Printing Package: " );


		Package temp = new Package(seqNum, id, sum, data);
		temp.print();
		return temp;
	}
	void doAction(Package p, int action){
		if (action == 0){ //PASS
			System.out.println("PASS");
		}
		else if (action == 1){ //DROP
			System.out.println("DROP");
		}
		else if (action == 2){ //CORRUPT
			System.out.println("CORRUPT");
		}
		System.out.println();
	}


	public static void main(String[] args) {
		int socket = /*args[0]*/ 1704;
		boolean running = true;
		int action;

		try {
			network net = new network(1704);
			while (running){
				Package p = net.getPackage();
				action = net.getAction();
				net.doAction(p, action);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

	// 	Random r = new Random();
	// 	network network = new network();
	// 	int passes = 0;
	// 	int corrupts = 0;
	// 	int drops = 0;
	//
	// 	for (int i = 0;i < 100 ; i++) {
	// 		int temp = network.getAction();
	// 		switch(temp){
	// 			case 0:
	// 				passes ++;
	// 				System.out.println("PASS");
	// 				break;
	// 			case 1:
	// 				System.out.println("CORRUPT");
	// 				corrupts ++;
	// 				break;
	// 			case 2:
	// 				drops ++;
	// 				System.out.println("DROP");
	// 				break;
	// 			default:
	// 				System.out.println("Error");
	// 		}
	// 	}
	// 	System.out.println("\nPasses: " + passes + " CORRUPTS: " + corrupts + " DROPS: " + drops);
	//
	 }

}
