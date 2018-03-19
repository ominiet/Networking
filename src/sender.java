import java.net.*;
import java.io.*;
import java.util.Scanner;

public class sender {
	private Socket senderSocket;
	private PrintWriter out;
	private BufferedReader in;
	int count = 0;

	sender(String ip, int port) throws UnknownHostException, IOException{
		startSender(ip,port);
	}

	void startSender(String ip, int port) throws UnknownHostException, IOException{
		senderSocket = new Socket(ip,port);
		out = new PrintWriter(senderSocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(senderSocket.getInputStream()));
	}
	void stopSender() throws IOException{
		in.close();
		out.close();
		senderSocket.close();
	}
	void sendMessage(Packet p){
		out.println(p.getseqNum() + " " + p.getID() + " " +
			p.getCheckSum() + " " + p.getData());
	}

	public static void main(String[] args) {
		String url = args[0];
		int port = Integer.parseInt(args[1]);
		String fileName = args[2];
		File file = new File(fileName);
		Packet p;
		String res;

		try {
			sender s = new sender(url,port);
			Scanner input = new Scanner(file);
			while(input.hasNext()){
				p = new Packet(input.next());
				s.sendMessage(p);
				s.count ++;
				res = s.in.readLine();
				while (!res.equals("ACK" + p.getseqNum())){//resend if improper ACK is recieved
					if(res.equals("ACK2")){
						System.out.println("Waiting ACK" + p.getseqNum() + ", " + s.count + ", DROP, resend Packet" + p.getseqNum());
						s.sendMessage(p);
						s.count ++;
					}
					else{
						System.out.println("Waiting ACK" + p.getseqNum() + ", " + s.count + ", " + res + ", resend Packet" + p.getseqNum());
						s.sendMessage(p);
						s.count ++;
					}
					res = s.in.readLine();
				}
				if(p.getData().charAt(p.getData().length() - 1) == '.'){
					System.out.println("Waiting ACK" + p.getseqNum() + ", " + s.count + ", " + res + ", no more packets to send");
				}
				else{
					System.out.println("Waiting ACK" + p.getseqNum() + ", " + s.count + ", " + res + ", send Packet" + ((p.getseqNum() == 0) ? 1 : 0));
				}
			}
			s.out.println(-1);
			s.stopSender();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
