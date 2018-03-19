import java.net.*;
import java.io.*;
import java.util.*;

public class receiver {
	private Socket receiverSocket;
	private PrintWriter out;
	private BufferedReader in;
	private ArrayList<String> list;
	private int waitingNum = 0;
	private int waitingAdder = 1;

	private int received = 0;

	receiver(String ip, int port) throws UnknownHostException, IOException{
		list = new ArrayList<String>();
		startreceiver(ip,port);
	}

	void startreceiver(String ip, int port) throws UnknownHostException, IOException{
		receiverSocket = new Socket(ip,port);
		out = new PrintWriter(receiverSocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(receiverSocket.getInputStream()));
	}
	void stopreceiver() throws IOException{
		in.close();
		out.close();
		receiverSocket.close();
	}
	boolean validatePacket(Packet p){
		if(p.getCheckSum() != p.sum(p.getData())) return false;
		else return true;
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
	boolean checkifWaitingFor(Packet p){	
		if(p.getseqNum() == waitingNum){
			return true;
		}
		else return false;
	}
	public static void main(String[] args) {
		String url = args[0];
		int port = Integer.parseInt(args[1]);
		boolean running = true;
		try {
			receiver r = new receiver(url, port);	//get connected to network
			while (running){
				String message = "";
				message = r.in.readLine();	//read from network input
				r.received ++;
				if(message.equals("-1")){	//handle input
					r.stopreceiver();
					break;
				}
				else if(message.equals("ACK2")){
					r.out.println("ACK" + ((r.waitingNum == 1) ? 0 : 1));
				}
				else{
					Packet p = r.getPacket(message);
					System.out.print("Waiting " + r.waitingNum + ", " + r.received + ", ");
					System.out.println(message);
					if(r.checkifWaitingFor(p)){	//if this is the sequence number I'm waiting for, check if the checksum is right
						if(r.validatePacket(p)){	//if so. send ack with same seq number and move forward
							r.out.println("ACK" + r.waitingNum);
							r.list.add(p.getData());
							r.waitingNum += r.waitingAdder;
							r.waitingAdder *= -1;
						}
						else{	//if not send ack with opposite sequence number
							r.out.println("ACK" + ((r.waitingNum == 1) ? 0 : 1));
						}
					}
					else{	//if this isnt what im waiting for send opposite ack to let sender know this isnt what I'm looking for
						r.out.println("ACK" + ((r.waitingNum == 1) ? 0 : 1));
					}
				}
			}
			for (int i = 0; i < r.list.size(); i++) {	// print contents of Array List at the end.
				System.out.print(r.list.get(i));
				System.out.print(((i == r.list.size() - 1) ? "\n" : " "));
			}
		} catch( Exception e){
			e.printStackTrace();
		}
	}
}
