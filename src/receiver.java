
public class receiver {
	private Socket recieverSocket;
	private PrintWriter out;
	private BufferedReader in;

	reciever(String ip, int port) throws UnknownHostException, IOException{
		startreciever(ip,port);
	}

	void startreciever(String ip, int port) throws UnknownHostException, IOException{
		recieverSocket = new Socket(ip,port);
		out = new PrintWriter(recieverSocket.getOutputStream(),true);
		in = new BufferedReader(new InputStreamReader(recieverSocket.getInputStream()));
	}
	void stopreciever() throws IOException{
		in.close();
		out.close();
		recieverSocket.close();
	}

	public static void main(String[] args) {
		boolean running = true;
		try {
			receiver r = new receiver("localhost", 1704);
			while (running){

			}

		}catch( Exception e){
			e.printStackTrace();
		}
	}
}
