import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

public class linkstate{
  static void printArray(String[] arr){

    for (int i =0; i < arr.length; i++){
      System.out.print(arr[i] + " ");

    }
    System.out.println();
  }
  static void print2D(int[][] arr){
    for (int i = 0;i< arr.length;i++){
      for (int j = 0; j < arr[i].length;j++){
        System.out.print((arr[i][j] == -1)? "I " : arr[i][j] + " ");
      }
      System.out.println();
    }

  }
  static void dijkstra (int[][] arr){//from our assignment we will always start from node 0
    dijkstra(arr, 0);
  }
  static void printLineSpacer(int size){
    for (int i = 0;i < size ; i++) {
      System.out.print("-------------");
    }
    System.out.println("--");
  }
  //TODO:Finish Algorithm

  static void dijkstra(int[][] arr, int startingNode){//allows you to start from any node
    //initialize

    //dimensions of 2d array are n*n
    boolean running = false;
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

    printLineSpacer(size);

    System.out.print("   step    ");
    for(int i = 0; i < size; i++) System.out.print(" ");
    System.out.print("N'");
    for(int i = 0; i < size; i++) System.out.print(" ");
    for (int i = 1; i < size; i++) {
      System.out.print(" D(" + (i+1) + "),p(" + (i+1) + ") ");
      if (i == (size-1)) System.out.println();
    }

    printLineSpacer(size);
    System.out.print("     0           " + (startingNode + 1) + "       ");
    for(int i = 1; i < size; i++){
      if (dist[i] !=-1)
        System.out.print("   " + dist[i] + ", " + (prev[i] + 1) + "    ");
      else
        System.out.print("    Inf    ");
    }
    nPrime[startingNode] = 1;//1 in nPrime Array == has been checked
    running = true;
    System.out.println();
    //LOOP of algorithm
    for (int i = 1; i<size;i++){
      printLineSpacer(size);

      //find the unchecked node with the minimum distance
      for (int j = 0; j < nPrime.length; j++){

      }
      //add w to N'

      //update D(j) for each neighbror of i that isn't in N'
      //  D(j) = min (D(v), D(w) + c(w,v))


      //if all have been checked, finish (satisfied by for loop)
    }
  }

  public static void main(String[] args){

    File nw;
    Scanner input;
    //ArrayList<ArrayList<Integer>> myList = new ArrayList<ArrayList<Integer>>();
    int[][] myList =new int[1][1];
    try {
      String line;
      nw = new File(args[0]);
      input = new Scanner(nw);
      boolean initFlag = false;

      int i = 0;

      //read file
      while(input.hasNext()){
        line = input.next();
        if (line.equals("EOF.")) break; //don't bother adding in EOF
        String [] stuff = line.split(",");
        stuff[stuff.length-1] = stuff[stuff.length-1].substring(0,stuff[stuff.length-1].length() -1);

        if(!initFlag) {//initialize on first loop only
          myList = new int[stuff.length][stuff.length];
          initFlag = true;
        }

        for (int j = 0; j < stuff.length;j++){
          if(stuff[j].equals("N")) myList[i][j] = -1;//assume -1 to be infinity
                                                    //because alg can't use negative numbers

          else {
            myList[i][j] = Integer.parseInt(stuff[j]);

          }
        }
        i++;
        //printArray(stuff);
      }


      //print2D(myList);
      //send to Dijkstra's

      dijkstra(myList);
    }
    catch(Exception e){

      e.printStackTrace();
    }
    System.exit(0);
  }
}
