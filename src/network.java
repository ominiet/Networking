import java.net.*;
import java.io.*;
import java.util.*;

public class network {
	public static Random r;
	public static volatile Packet sent;
	public static volatile String received;
	public static volatile boolean Stahp = false;
	public final static Object obj = new Object();
	
	public static int getAction(Random r){	//for random action
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
	public static void main(String[] args) {	//create two strings, run and finish
		int socket = Integer.parseInt(args[0]);
		boolean running = true;
		int action;

		try {
			r = new Random();
			ServerSocket serverSocket = new ServerSocket(1704);
			new senderHandler(serverSocket.accept(), r).start();
			new receiverHandler(serverSocket.accept(), r).start();
			serverSocket.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}

	public static class senderHandler extends Thread{	//handles network/sender relationship
		public static final String [] acts = {"PASS", "DROP", "CORRUPT"};
		private Socket senderSocket;
		private PrintWriter out;
		private BufferedReader in;
		private Random r;

		senderHandler(Socket senderSocket, Random r){
			this.senderSocket = senderSocket;
			this.r = r;
		}
		public int getAction(){
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
		public Packet getPacket(String message) throws IOException{	//reads and creates usable packet from string
			Scanner scan = new Scanner(message);
			
			int sequenceNum = scan.nextInt();
			int id = scan.nextInt();
			int sum = scan.nextInt();
			String data = scan.next();

			Packet temp = new Packet(sequenceNum, id, sum, data);
			return temp;
		}
		void doAction(Packet p, int action){	//seemed like a better idea before adding threading
			if (action == 0){ //PASS
				sent = p;
			}
			else if (action == 1){ //DROP

			}
			else if (action == 2){ //CORRUPT
				p.corrupt();
				sent = p;
			}
		}
		void giveResponse(int action){ //don't know why I didn't place in run method
			if (action == 1){
				out.println("ACK2");
			}
			else out.println(received);
		}

		public void run(){
			boolean running = true;
			int action;
			synchronized(obj){
				try{
					out = new PrintWriter(senderSocket.getOutputStream(), true);	//initialize socket connection
					in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
					obj.wait();	//wait for receiver to connect
					while (running){
						String message = "";
						while (message.equals("")){
							message = in.readLine();	//read in stuff
						}
						if(message.equals("-1")){		//if sender is done signal close to receiver
							running = false;
							Stahp = true;
							obj.notify();
							break;
						}
						Packet p = getPacket(message);	//do stuff with input
						action = getAction();
						doAction(p, action);
						System.out.println("Recieved: Packet " +
						p.getseqNum() + ", " + p.getID() + ", " + acts[action]);
						if (action != 1){
							
							obj.notify();	//let receiver thread start working
							obj.wait();
							out.println(received);	//get response when receiver is done
						}
						else out.println("ACK2");
						
					}
					stopThread();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}

		public void stopThread() throws IOException{	//modularity is key ;)
			out.close();
			in.close();
			senderSocket.close();
		}
	}
	 public static class receiverHandler extends Thread{	//new thread old stuff
		public static final String [] acts = {"PASS", "DROP", "CORRUPT"};
		private Socket receiverSocket;
		private PrintWriter out;
		private BufferedReader in;
		private Random r;

		receiverHandler(Socket receiverSocket, Random r){
			this.receiverSocket = receiverSocket;
			this.r = r;
		}
		public int getAction(){
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
		public Packet getPacket(String message) throws IOException{
			Scanner scan = new Scanner(message);
			
			int sequenceNum = scan.nextInt();
			int id = scan.nextInt();
			int sum = scan.nextInt();
			String data = scan.next();

			Packet temp = new Packet(sequenceNum, id, sum, data);
			return temp;
		}
		void doAction(Packet p, int action){	//basically all the same as other thread but not
			if (action == 0){ //PASS
				out.println("ACK" + p.getseqNum());
			}
			else if (action == 1){ //DROP
				out.println("ACK2");
			}
			else if (action == 2){ //CORRUPT
				p.corrupt();
				out.println("ACK" + ((p.getseqNum() == 0) ? 1 : 0));
			}
		}

		public void run(){
			boolean running = true;
			int action;
			synchronized(obj){
			try{
				out = new PrintWriter(receiverSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()));
				obj.notify();		//let sender thread now you've connected
				obj.wait();			//wait for it to do it's thing
				while (running){
					if(Stahp){		//quit if the message is done
						out.println("-1");
						running = false;
						break;
					}
					out.println(sent.getseqNum() + " " + sent.getID() + " " +	//send message to receiver
						sent.getCheckSum() + " " + sent.getData());

					String message = "";
					while (message.equals("")){
						message = in.readLine();	//get response
						
					}
					
					action = getAction();
					received = message;				//pass response back to sender thread
					System.out.println("Recieved:" + received + ", " + acts[action]);
					obj.notify();					//let sender thread do its thing
					obj.wait();						//wait for sender thread to finish its task
				}
				stopThread();						//close when done
			} catch (Exception e){
				e.printStackTrace();
			}
			}
		}

		public void stopThread() throws IOException{
			out.close();
			in.close();
			receiverSocket.close();
		}
	}
}