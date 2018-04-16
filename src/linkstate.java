import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class linkstate{
	static void dijkstra (int[][] arr){//from our assignment we will always start from node 0
		dijkstra(arr, 0);
	}

	static void dijkstra(int[][] arr, int startingNode){//allows you to start from any node
		//initialize

		//dimensions of 2d array are n*n
		int size = arr.length;
		int[] nPrime = new int[size];//will hold if node is visited
		int[] prev = new int[size];//will hold node's previous hop on path
		int[] dist = new int[size];//holds each node's min distance

		nPrime[startingNode] = 1; //visit first node in table.
		for (int i = 0; i < size; i++){
			if(arr[startingNode][i] != -1)
				prev[i] = startingNode;
				else prev[i] = -1;
			dist[i] = arr[startingNode][i];
		}

		printHeader(size);
		printStepResults(0,dist,prev,nPrime,0);	//print first step's results
		nPrime[startingNode] = 1;//1 in nPrime Array == has been checked
		
		//LOOP of algorithm
		for (int i = 1; i<size;i++){
			int w = -1;

			//find the unchecked neighbor node with the minimum distance
			for (int j = 0; j < nPrime.length; j++){
				if (nPrime[j] == 0){  
					if(w == -1) w = j;  //set to first unchecked node then compare
					else {
						if (dist[j] != -1 && (dist[j] < dist[w])){//think about case where -1 (infinity) is lowest distance
							w = j;
						}
					}
				}
			}
			if (w == -1) return;   //Something's wrong if this happens

			//add w to N'
			nPrime[w] = 1;


			//update D(j) for each neighbor of w that isn't in N'
			//  D(j) = min (D(v), D(w) + c(w,v))
			for (int j = 0; j < size; j++){
				if(nPrime[j] != 1){  //not checked
					if(isShorterPath(w,j,dist,arr)){  //returns true if there is a newer shorter path
						prev[j] = w;
						dist[j] = arr[w][j] + dist[w];
					}
				}
			}

			//display Step results
			printStepResults(i,dist,prev,nPrime, w);

			//if all have been checked, finish (satisfied by for loop)
		}
	}

	static boolean isShorterPath(int i,int j, int[] dist,int[][] arr){
		if (arr[i][j] == -1) return false;//if -1(infinity) then there is no link so dont bother comparing
		else if(dist[j] == -1)return true;//if current distance is infinity than anything else must be faster
		else if(dist[j] > dist[i] + arr[i][j])return true;

		return false;// if not, its current path must be faster

	}

	static void printLineSpacer(int size){
		for (int i = 0;i < size ; i++) {
			System.out.print("-------------------");
		}
		System.out.println();
	}
	static void printHeader(int size){
		printLineSpacer(size);

		System.out.print("Step\t");
		for(int i = 0; i < size + ((size>10)?(size - 10) : (0)); i++) System.out.print(" ");
		System.out.print("N'");
		for(int i = 0; i < size+1; i++) System.out.print(" ");
		System.out.print("\t\t");
		for (int i = 1; i < size; i++) {
			System.out.print("D(" + (i+1) + "),p(" + (i+1) + ")\t");
			if (i == (size-1)) System.out.println();
		}
	}
	static void printStepResults(int step, int[] dist, int[] prev, int[] nPrime, int visited) {
		printLineSpacer(dist.length);
		int twoDigitNodes = dist.length - 9;
		System.out.print(step + "\t");
		int numOfSpaces = 0;
		boolean commaFlag = false;
		for (int i = 0; i < nPrime.length; i ++){	//Account for commas and extra spaces
			if (nPrime[i] != 0 ){
				if (commaFlag){
					System.out.print(",");
				}
				else commaFlag = true;
				System.out.print((i + 1));

				if((i + 1) > 9){
					twoDigitNodes --;
				}
			}
			else numOfSpaces ++;
		}
		

		int spaces = (dist.length * 2 + 1) - step * 2 - 1;
		for (int i = 0; i < spaces + twoDigitNodes; i++){

			System.out.print(" ");
		}
		System.out.print("\t");
		for(int i = 1; i < dist.length; i++){
			if (dist[i] !=-1)
				if(nPrime[i] == 0 ){
					System.out.print("\t" + dist[i] + ", " + (prev[i] + 1) + "\t");
				}
				else System.out.print("\t-\t");
			else
				System.out.print("\tInf\t");
		}
		System.out.println();
	}

	public static void main(String[] args){

		File nw;
		Scanner input;
		int[][] myList =new int[1][1];

		try {
			String line;
			nw = new File(args[0]);
			input = new Scanner(nw);
			boolean initFlag = false;

			int i = 0;

			//read file to get 2d array with the adjacency matrix
			while(input.hasNext()){
				line = input.next();
				if (line.equals("EOF.")) break; //don't bother adding in EOF
				String [] tokens = line.split(",");
				tokens[tokens.length-1] = tokens[tokens.length-1].substring(0,tokens[tokens.length-1].length() -1);

				if(!initFlag) {//initialize on first loop only
					myList = new int[tokens.length][tokens.length];
					initFlag = true;
				}

				for (int j = 0; j < tokens.length;j++){
					if(tokens[j].equals("N")) myList[i][j] = -1;//assume -1 to be infinity because alg can't use negative numbers
					else {
						myList[i][j] = Integer.parseInt(tokens[j]);
					}
				}
				i++;
			}

			//send to Dijkstra's

			dijkstra(myList);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		System.exit(0);
	}
}
