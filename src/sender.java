import java.net.*;
import java.io.*;
import java.util.Scanner;

public class sender {
	private Socket senderSocket;
	private PrintWriter out;
	private BufferedReader in;

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
		// String URL = args[0];
		// int port = Integer.parseInt(args[1]);
		String fileName = /*args[2]*/"message.txt";
		//FileReader fileReader;
		File file = new File(fileName);
		Packet p;
		String res;

		try {
			sender s = new sender("localhost",1704);
			Scanner input = new Scanner(file);
			while(input.hasNext()){
				p = new Packet(input.next());
				s.sendMessage(p);
				res = s.in.readLine();
				System.out.println(res);
				//p.print();

			}
			s.stopSender();
		}
		catch(Exception e){
			e.printStackTrace();

		}
		finally {

		}

		// System.out.println(fileName);


		// Packet pack = new Packet("abcd");
		// pack.print();
		// pack = new Packet("cd");
		// pack.print();
		// pack.corrupt();
		// pack.print();
	}

}
