
public class sender {

	public static void main(String[] args) {
		
		Packet pack = new Packet("abcd");
		pack.print();
		pack = new Packet("cd");
		pack.print();
		pack.corrupt();
		pack.print();
	}

}
