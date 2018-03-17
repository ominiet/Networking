
public class Packet {
	byte sequenceNum;
	byte ID;
	int CheckSum;
	String data;
	static byte idCount = 0;
	
	Packet(String input){
		this.sequenceNum = 0;
		this.ID = idCount;
		this.CheckSum = sum(input);
		this.data = input;
		idCount ++;
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
		return 0;
	}
	int getData() {
		return 0;
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
