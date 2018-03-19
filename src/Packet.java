
public class Packet {
	int sequenceNum;
	int ID;
	int CheckSum;
	String data;
	static int idCount = 1;
	static int seqNumAssigner = 0;
	static int seqAdder = 1;

	Packet(String input){//for creating packets from the sender
		this.sequenceNum = seqNumAssigner;
		this.ID = idCount;
		this.CheckSum = sum(input);
		this.data = input;
		idCount ++;
		seqNumAssigner += seqAdder;
		seqAdder *= -1;
	}
	Packet(int seq, int id, int sum, String data){//for creating packets by network and rec
		this.sequenceNum = seq;
		this.ID = id;
		this.CheckSum = sum;
		this.data = data;
	}
	int sum(String input) {
		int sum = 0;
		for (int i = 0; i < input.length(); i ++) {
			sum += input.charAt(i);
		}
		return sum;
	}
	int getID(){
		return ID;
	}
	int getCheckSum(){
		return CheckSum;
	}
	String getData() {
		return data;
	}
	int getseqNum(){
		return sequenceNum;
	}
	void corrupt() {
		this.CheckSum += 1;
	}
	void print() {
		System.out.println("Sequence number: " + sequenceNum);
		System.out.println("ID: " + ID);
		System.out.println("Check Sum: " + CheckSum);
		System.out.println("Data: " + data);
	}
}
// public class Ack {
// 	private int sequenceNum;
// 	private int checkSum = 0;

// 	void corrupt(){
// 		this.checkSum ++;
// 	}

// }