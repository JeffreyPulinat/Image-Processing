import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Average {
	static int numRows;
	static int numCols;
	static int minVal;
	static int maxVal;
	static int newMin;
	static int newMax;
	static int[][] mirrorFramedAry;
	static int []neighborAry = new int [9];
	static int[][] tempAry;
	
	public static void main(String[] argv){

		loadImage(argv[0]);
		File output = new File(argv[1]);
		loadNeighbors();
		try {
		BufferedWriter out = new BufferedWriter(new FileWriter(output));
			out.write(numRows+" "+numCols+" "+newMin+" "+newMax);
			out.newLine();
			for(int row = 1; row<=numRows; row++){		//print out from [1][1] w/o border
				for(int col = 1; col<=numCols; col++){
					int value = tempAry[row][col];
					out.write(value+" ");
				}
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void loadImage(String argv) {
		File input = new File(argv);
		Scanner sc;
		try {
			sc = new Scanner(input);
			//assign params
			numRows=sc.nextInt();
			numCols=sc.nextInt();
			minVal=sc.nextInt();
			maxVal=sc.nextInt();
		
			mirrorFramed(sc);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void mirrorFramed(Scanner sc){
		mirrorFramedAry = new int [numRows+2][numCols+2];
		for(int row = 1; row<=numRows; row++){
			for(int col = 1; col<=numCols; col++){
				int value =sc.nextInt();
				mirrorFramedAry[row][col] = value;
				if(row == 1) mirrorFramedAry[0][col] = value; //mirrors top row
				if(row == numRows) mirrorFramedAry[row+1][col] = value;	//mirrors bottom row
			}
		}
		for(int side=0; side<=numRows+1; side++){ //mirroring sides
			mirrorFramedAry[side][0] = mirrorFramedAry[side][1];  //left side
			mirrorFramedAry[side][numCols+1] = mirrorFramedAry[side][numCols]; //right side
		}
		sc.close();
	}
	
	private static void loadNeighbors() {	//load 3x3 neighbors
		tempAry = new int [numRows+2][numCols+2];
		int count =1;
		for(int row = 1; row<=numRows; row++){
			for(int col = 1; col<=numCols; col++){
				neighborAry[0]=mirrorFramedAry[row-1][col-1];
				neighborAry[1]=mirrorFramedAry[row-1][col];
				neighborAry[2]=mirrorFramedAry[row-1][col+1];
				
				neighborAry[3]=mirrorFramedAry[row][col-1];
				neighborAry[4]=mirrorFramedAry[row][col];
				neighborAry[5]=mirrorFramedAry[row][col+1];
				
				neighborAry[6]=mirrorFramedAry[row+1][col-1];
				neighborAry[7]=mirrorFramedAry[row+1][col];
				neighborAry[8]=mirrorFramedAry[row+1][col+1];
				
				AVG3X3(row,col, count);
				count++;
			}
		}
		
	}

	private static void AVG3X3(int row, int col, int count) {
		int sum = 0;
		for(int index =0; index<=8; index++){
			sum= sum +neighborAry[index];
		}
		int average = sum / 9;
		
		if(count==1){ //first iteration
			newMin = average;
			newMax = average;
		} else{  //compare and change min and max
			if(newMin>average)newMin = average;
			if(newMax<average)newMax=average;
		}
		
		tempAry[row][col] = average;
	}

	
		
}
