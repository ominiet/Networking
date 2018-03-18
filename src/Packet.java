
public class Packet {
	byte sequenceNum;
	byte ID;
	int CheckSum;
	String data;
	static byte idCount = 1;

	Packet(String input){//for creating packets from the sender
		this.sequenceNum = 0;
		this.ID = idCount;
		this.CheckSum = sum(input);
		this.data = input;
		idCount ++;
	}
	Packet(byte seq, byte id, int sum, String data){//for creating packets by network and rec
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
	byte getID(){
		return ID;
	}
	int getCheckSum(){
		return CheckSum;
	}
	String getData() {
		return data;
	}
	byte getseqNum(){
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
