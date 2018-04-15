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
    System.out.print("     0    " + (startingNode + 1) + "       ");
    for(int i = 1; i < size; i++) System.out.print("  ");
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
      //System.out.print(w);
      if (w == -1) return;   //Something's wrong
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
      printStepResults(i,dist,prev,nPrime);


      //if all have been checked, finish (satisfied by for loop)
    }
  }
  static boolean isShorterPath(int i,int j, int[] dist,int[][] arr){
    if (arr[i][j] == -1) return false;//if -1(infinity) then there is no link so dont bother comparing
    else if(dist[j] == -1)return true;//if current distance is infinity than anything else must be faster
    else if(dist[j] > dist[i] + arr[i][j])return true;

    return false;// if not, its current path must be faster

  }
  static void printStepResults(int step, int[] dist, int[] prev, int[] nPrime) {
    System.out.print("     " + step + "    ");
    int numOfSpaces = 0;
    for (int i = 0; i < nPrime.length; i ++){
      if (nPrime[i] != 0 )
        System.out.print((i + 1) + ",");
      else numOfSpaces ++;
    }

    while(numOfSpaces > 0){
      System.out.print("  ");
      numOfSpaces--;
    }
    for(int i = 1; i < dist.length; i++){
      if (dist[i] !=-1)
        System.out.print("   " + dist[i] + ", " + (prev[i] + 1) + "    ");
      else
        System.out.print("    Inf    ");
    }
    System.out.println();
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
