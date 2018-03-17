import java.net.*;
import java.io.*;
public class network {
	private ServerSocket serverSocket;
    private Socket senderSocket;
    private Socket receiverSocket;
    private PrintWriter out;
    private BufferedReader in;

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

	public static void main(String[] args) {
		
	}

}
