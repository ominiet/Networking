import java.net.*;
import java.io.*;
import java.util.*;


public class network {
	public static Random r;
	
	public static int getAction(Random r){
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
	public static void main(String[] args) {
		int socket = /*args[0]*/ 1704;
		boolean running = true;
		int action;

		try {
			r = new Random();
			ServerSocket serverSocket = new ServerSocket(1704);
			new senderHandler(serverSocket.accept(), r).start();
			new receiverHandler(serverSocket.accept(), r).start();	
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
}

class senderHandler extends Thread{
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
	public Packet getPacket(String message) throws IOException{
		Scanner scan = new Scanner(message);
		
		int sequenceNum = scan.nextInt();
		int id = scan.nextInt();
		int sum = scan.nextInt();
		String data = scan.next();

		Packet temp = new Packet(sequenceNum, id, sum, data);
		return temp;
	}
	void doAction(Packet p, int action){
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
		try{
			out = new PrintWriter(senderSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
			while (running){
				String message = "";
				while (message.equals("")){
					message = in.readLine();
				}
				if(message.equals("-1")){
					running = false;
					break;
				}
				Packet p = getPacket(message);
				action = getAction();
				doAction(p, action);
				System.out.println("Recieved: Packet " +
				p.getseqNum() + ", " + p.getID() + ", " + acts[action]);
			}
			stopThread();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void stopThread() throws IOException{
		out.close();
		in.close();
		senderSocket.close();
	}
}
class receiverHandler extends Thread{
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
	void doAction(Packet p, int action){
		// if (action == 0){ //PASS
		// 	out.println("ACK" + p.getseqNum());
		// }
		// else if (action == 1){ //DROP
		// 	out.println("ACK2");
		// }
		// else if (action == 2){ //CORRUPT
		// 	p.corrupt();
		// 	out.println("ACK" + ((p.getseqNum() == 0) ? 1 : 0));
		// }
	}

	public void run(){
		boolean running = true;
		int action;
		try{
			out = new PrintWriter(receiverSocket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()));
			while (running){
				// String message = "";
				// while (message.equals("")){
				// 	message = in.readLine();
				// }
				// if(message.equals("-1")){
				// 	running = false;
				// 	break;
				// }
				// Packet p = getPacket(message);
				// action = getAction();
				// doAction(p, action);
				// System.out.println("Recieved: Packet " +
				// p.getseqNum() + ", " + p.getID() + ", " + acts[action]);
			}
			stopThread();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void stopThread() throws IOException{
		out.close();
		in.close();
		receiverSocket.close();
	}
}