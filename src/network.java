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
	public static final String [] acts = {"PASS", "DROP", "CORRUPT"};


	network(){
		r = new Random();
	}

  public network(int port) throws IOException{
  		startServer(port);
  }
  public void startServer(int port) throws IOException {	//open server socket

			r = new Random();
			serverSocket = new ServerSocket(port);
      startClient();
  }

  public void startClient() throws IOException {		//open client socket
  	senderSocket = serverSocket.accept();
    out = new PrintWriter(senderSocket.getOutputStream(), true);
    in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
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
	Packet getPacket() throws IOException{
		String message = "";
		while (message.equals("")){
			message = in.readLine();
		}
		System.out.println("not null");
		Scanner scan = new Scanner(message);
		System.out.println("Printing Package: " );

		int sequenceNum = scan.nextInt();
		int id = scan.nextInt();
		int sum = scan.nextInt();
		String data = scan.next();

		Packet temp = new Packet(sequenceNum, id, sum, data);
		temp.print();
		return temp;
	}
	void doAction(Packet p, int action){
		if (action == 0){ //PASS
			out.println("PASS");
		}
		else if (action == 1){ //DROP
			out.println("ACK2");
		}
		else if (action == 2){ //CORRUPT
			p.corrupt();
			out.println(p.getseqNum() + " " + p.getID() + " " +
				p.getCheckSum() + " " + p.getData());
			//out.println("CORRUPT");
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
				Packet p = net.getPacket();
				action = net.getAction();
				net.doAction(p, action);
				System.out.println("Recieved: Packet " +
					p.getseqNum() + ", " + p.getID() + ", " + acts[action]);
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
