import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class EightConnectedComp {
	static int numRows;
	static int numCols;
	static int minVal;
	static int maxVal;
	static int newMin;
	static int newMax;
	static int newLabel = 0;
	static int[][] zeroFramedAry;
	static int []neighborAry = new int [5];
	static int []EQAry;
	static int[] Property;
	static int totalComp;
	
	public static void main(String[] argv) {
		// TODO Auto-generated method stub
		loadImage(argv[0]);
		EQAry = new int [(numRows*numCols)/2];
		for(int i=0; i<EQAry.length; i++){
			EQAry[i]=i;
		}
		try {
			File output1 = new File(argv[1]);
			BufferedWriter out = new BufferedWriter(new FileWriter(output1));
			
			out.newLine();
			ConnectCC_Pass1();
			out.write("PASS 1");
			out.newLine();
			prettyPrint(out);
			out.newLine();
			EQPrint(out);
			
			out.newLine();
			out.write("---------------");
			out.newLine();
			out.write("PASS 2");
			out.newLine();
			ConnectCC_Pass2();
			prettyPrint(out);
			out.newLine();
			EQPrint(out);
			
			out.newLine();
			out.write("---------------");
			out.newLine();
			manageEQAry();
			out.write("After Managed ");
			EQPrint(out);
			out.newLine();
			out.write("PASS 3");
			out.newLine();
			
			File output3 = new File(argv[3]);
			BufferedWriter out3 = new BufferedWriter(new FileWriter(output3));
			out3.write(numRows+" "+numCols+" "+newMin+" "+newMax);
			out3.newLine();
			ConnectCC_Pass3(out3);
			out3.close();
			
			prettyPrint(out);
			EQPrint(out);
			out.newLine();
			out.close();
			
			File output2 = new File(argv[2]);
			BufferedWriter out2 = new BufferedWriter(new FileWriter(output2));
			prettyPrint(out2);	
			out2.close();
			
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	

		private static void printCCProperty(BufferedWriter out, ConComp[] comp) {
		try{
			int tcomp= totalComp-1;
			out.write("Total Components: " +tcomp);
			out.newLine();
			for(int i=1; i<totalComp;i++){
				out.write("Label: "+comp[i].getLabel());
				out.newLine();
				out.write("Number of Pixels: "+comp[i].getNumbpixels());
				out.newLine();
				out.write("Upper Left pixel of Bounding Box: "+comp[i].getMinRow()+" "+comp[i].getMinCol());
				out.newLine();
				out.write("Lower Right pixel of Bounding Box: "+comp[i].getMaxRow()+" "+comp[i].getMaxCol());
				out.newLine();
				out.write("______");
				out.newLine();
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
		
		
	}


	private static void EQPrint(BufferedWriter out) {
		try{
			out.write("EQAry:");
			out.newLine();
			for(int i=0; i<=newLabel;i++){
				out.write(EQAry[i] + ", ");
			}
		} catch (IOException e) {
		e.printStackTrace();
		}
	}

	private static void manageEQAry() {
		// TODO Auto-generated method stub
		int []unique = new int [newLabel];
		totalComp=0;
		for(int i=0; i<=newLabel; i++){
			int j;
			for (j = 0; j < i; j++){
				if (EQAry[i] == EQAry[j])break;
			}
			//fill array with unique values
			if (i == j){
				unique[totalComp]=EQAry[i];
				totalComp++;
			}
		}
		
		for(int i =0; i<totalComp; i++){
			for(int j=0; j<=newLabel;j++){
				if(EQAry[j]==unique[i]){
					EQAry[j]=i;
				}
			}
		}
		newMin=0;
		newMax=totalComp-1;
	}


	private static void prettyPrint(BufferedWriter out) {
		try {
			out.write(numRows+" "+numCols+" "+newMin+" "+newMax);
			out.newLine();
			for(int row = 1; row<=numRows; row++){		//print out from [1][1] w/o border
				for(int col = 1; col<=numCols; col++){
					int value = zeroFramedAry[row][col];
					//int numSpaces = String.valueOf(value).length();
					if(value!=0){
						if(value>=10){
							out.write(value+" ");
						}else out.write(value+"  ");
					}else{						//when skipping 0's
						if(value>=10){
							out.write("  ");
						}else out.write("   ");
					}
				}
				out.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void ConnectCC_Pass3(BufferedWriter out) {
		ConComp[] comp = new ConComp[totalComp];
		for(int i=0; i<totalComp;i++){
			comp[i]= new ConComp();
			comp[i].setLabel(i);
		}
		for(int row = 1; row<=numRows; row++){
			for(int col = 1; col<=numCols; col++){
				int EQindex = zeroFramedAry[row][col];
				int EQvalue = EQAry[EQindex];
				zeroFramedAry[row][col]=EQvalue;
				if(zeroFramedAry[row][col]==comp[EQvalue].getLabel()){
					comp[EQvalue].incNumbpixels();
					if(row<comp[EQvalue].getMinRow())comp[EQvalue].setMinRow(row);
					if(col<comp[EQvalue].getMinCol())comp[EQvalue].setMinCol(col);
				
					if(row>comp[EQvalue].getMaxRow())comp[EQvalue].setMaxRow(row);
					if(col>comp[EQvalue].getMaxCol())comp[EQvalue].setMaxCol(col);
				}
			}
		}
		printCCProperty(out,comp);
		}
	
	private static void ConnectCC_Pass2() {
		int min;
		boolean anyzero = false;
		for(int row = numRows; row>=1; row--){
			for(int col = numCols; col>=1; col--){
				if(zeroFramedAry[row][col]>0){		//p(i,j) is not 0
					loadNeighbors(row,col,2);
					if(zeroNeighbors()==true){				//CASE 1: all neighbors are 0
						//do nothing
					}
					else if(equalNeighbors(4)!= -1){ 		//CASE 2: neighbors are equal including (i,j)
						//do nothing
					}else {									//CASE 3 neighbors are diff values
						min = Integer.MAX_VALUE;
						for(int i=0; i<neighborAry.length; i++) {
					        if(neighborAry[i] > 0 && neighborAry[i]<min) {
					            min = neighborAry[i];
					        }
					    }
						int index = zeroFramedAry[row][col];
						zeroFramedAry[row][col]=min;
						updateEQAry(index,min); //(index,value)
					}
				}else anyzero = true;		
			}
		}
		if(anyzero==false)newMin=1;
		else newMin=0;
		newMax=newLabel;
	}
		

	private static void ConnectCC_Pass1() {
		int min;
		boolean anyzero = false;
		for(int row = 1; row<=numRows; row++){
			for(int col = 1; col<=numCols; col++){
				if(zeroFramedAry[row][col]>0){		//p(i,j) is not 0
					loadNeighbors(row,col,1);
					if(zeroNeighbors()==true){				//CASE 1: all neighbors are 0
						newLabel++;
						zeroFramedAry[row][col]=newLabel;
					}
					else if(equalNeighbors(3)!= -1){ 		//CASE 2: neighbors are equal
						zeroFramedAry[row][col]=equalNeighbors(3);
					}else {									//CASE 3 neighbors are diff values
						min = Integer.MAX_VALUE;
						int max = 0;
						for(int i=0; i<=3; i++) {		 //doesnt compare p(i,j)
					        if(neighborAry[i] > 0 && neighborAry[i]<min) {
					            min = neighborAry[i];
					        }
					        if(neighborAry[i] > 0 && neighborAry[i]>max) {
					            max = neighborAry[i];
					        }
					    }
						zeroFramedAry[row][col]=min;
						updateEQAry(max,min); //(index,value)

					}	
				}else anyzero = true;
			}
		}
		if(anyzero==false)newMin=1;
		else newMin=0;
		newMax=newLabel;
	}
	
	private static void updateEQAry(int index, int value) {
		EQAry[index] = value;
	}
	
	private static boolean zeroNeighbors(){ //case1
		boolean allZero = true;
		for(int i=0; i<=3; i++){		//0 - 3 excludes p(i,j)
			if (neighborAry[i] != 0){
				allZero = false;
				break;
			}
		}
		return allZero;
		
	}

	private static int equalNeighbors(int bound){ //case 2
		boolean equal = true;
		int value = 0;
		int count = 1; // keeps track of first non zero element
		
		for(int i=0; i<=bound; i++){ //0-3 excludes p(i,j) 0-4 includes p(i,J)
			if (neighborAry[i] > 0 && count == 1 ){
				value = neighborAry[i];
				count++;
			}
			if(count==2 && neighborAry[i] != 0 && neighborAry[i] != value){
				equal = false;
				value = -1;
				break;
			}
		}
		//return equal;
		return value;
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
		
			zeroFramed(sc);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void zeroFramed(Scanner sc){
		zeroFramedAry = new int [numRows+2][numCols+2];
		for(int row = 1; row<=numRows; row++){
			for(int col = 1; col<=numCols; col++){
				int value =sc.nextInt();
				zeroFramedAry[row][col] = value;
				if(row == 1) zeroFramedAry[0][col] = 0; //zeros top row
				if(row == numRows) zeroFramedAry[row+1][col] = 0;	//zeros bottom row
			}
		}
		for(int side=0; side<=numRows+1; side++){ //zero sides
			zeroFramedAry[side][0] = 0;  //left side
			zeroFramedAry[side][numCols+1] = 0; //right side
		}
		sc.close();
	}
	
	private static void loadNeighbors(int row, int col, int pass) {
		/* a|b|c
		 * d|e|f		e = p(i,j)
		 * g|h|i
		 */
		if(pass==1){
			neighborAry[0]=zeroFramedAry[row-1][col-1]; //a
			neighborAry[1]=zeroFramedAry[row-1][col];	//b
			neighborAry[2]=zeroFramedAry[row-1][col+1];	//c	
			neighborAry[3]=zeroFramedAry[row][col-1];	//d
			neighborAry[4]=zeroFramedAry[row][col];		//e p(i,j)
		}
		
		if(pass==2){
			neighborAry[4]=zeroFramedAry[row][col];			//e  p(i,j)
			neighborAry[0]=zeroFramedAry[row][col+1];			//f
			neighborAry[1]=zeroFramedAry[row+1][col-1];			//g
			neighborAry[2]=zeroFramedAry[row+1][col];			//h
			neighborAry[3]=zeroFramedAry[row+1][col+1];			//i

		}

	}
}
