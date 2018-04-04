#include <iostream>
#include <fstream>
#include <string>
#include <stdlib.h>
#include "Point.h"

using namespace std;

	static int numRows;
	static int numCols;
	static int minVal;
	static int maxVal;
	static int** imgAry;
	static int neighborCoord [8];

void zeroFramed(int **ary, int nRows, int nCols){	
	for(int col=0; col<=nCols+1; col++){
		ary[0][col] = 0;  //top
		ary[nRows+1][col] = 0; //bottom
	}
      
	for(int row=0; row<=nRows+1; row++){
		ary[row][0] = 0;  //left side
		ary[row][nCols+1] = 0; //right side
	}
}

void loadImage(ifstream& input){
	int param;
	input >> param;
	numRows = param;
	input >> param;
	numCols = param;
	input >> param;
	minVal = param;
	input >> param;
	maxVal = param;
	
	imgAry = new int *[numRows+2];
	for(int i=0; i<numRows+2; ++i){
		imgAry[i] = new int[numCols+2];
	}
	
	//fill image
	for(int row = 1; row<= numRows; row++){
		for(int col = 1; col<= numCols; col++){
			int value;
			input >> value;
			imgAry[row][col] = value;
		}
	}
}

void loadNeighborsCoord(int** ary, int row, int col) {	//load 3x3 neighbors
    neighborCoord[0] = ary[row][col+1];
    neighborCoord[1] = ary[row-1][col+1];
    neighborCoord[2] = ary[row-1][col];
    neighborCoord[3] = ary[row-1][col-1];
    neighborCoord[4] = ary[row][col-1];
    neighborCoord[5] = ary[row+1][col-1];
    neighborCoord[6] = ary[row+1][col];
    neighborCoord[7] = ary[row+1][col+1];
}

int getChainDir(Point currentP, int nextQ){
	for(int i =nextQ; i<=7; i++){
		if(neighborCoord[i]>0){
			return i;
		}
	}
	for(int i =0; i<nextQ; i++){
		if(neighborCoord[i]>0){
			return i;
		}
	}
}

int findNextP( Point currentP, int nextQ, Point *nextP){
	int row = currentP.getX();
	int col = currentP.getY();
	loadNeighborsCoord (imgAry, row, col );  //current non zero border pixel
	int chainDir = getChainDir(currentP, nextQ);
	
	//getting row from chainDir
	if(chainDir==1||chainDir==2||chainDir==3){
		row=row-1;
	}
	if(chainDir==5||chainDir==6||chainDir==7){
		row=row+1;
	}
	
	//getting col from chainDir
	if(chainDir==0||chainDir==1||chainDir==7){
		col=col+1;
	}
	if(chainDir==3||chainDir==4||chainDir==5){
		col=col-1;
	}
	nextP->setXY(row,col);//pass by refrence
	return chainDir;
}

void getChainCode(ofstream& output1, ofstream& output2){
	bool labelfound=false;
	for(int row = 1; row<= numRows; row++){
		for(int col = 1; col<= numCols; col++){
			if(imgAry[row][col]>0){
				labelfound=true;
				output1<<row<<" "<<col<<" "<<imgAry[row][col]<<" "; 
				output2<<row<<" "<<col<<" "<<imgAry[row][col]<<endl; 
				Point startP (row, col);
				Point currentP (row, col);
				Point nextP(0,0);
				int lastQ = 4;
				int count =0;
				do{
					int nextQ = (lastQ)%8;
					int PchainDir = findNextP(currentP, nextQ, &nextP);
					// nextP will be determined inside the findNextP method. 
					output1<<PchainDir<<" ";
	 		  		output2<<PchainDir<<" ";
	 		  		count++;
	 		  		if(count>=15){
	 		  			output2<<endl;
	 		  			count=0;
	 		  		}
	 		  		

					lastQ = PchainDir;
  					currentP.setXY( nextP.getX(), nextP.getY() ); // nextP was determined inside the findNextP method. 
				}while((currentP.equal(startP))==false); //repeat until currentP == startP
			}
			if(labelfound)break;
		}
		if(labelfound)break;
	}
}

void prettyPrint(int **ary, ofstream& output, bool zborder, bool iheader){
	if(iheader==true)output<<endl<<numRows<<" "<<numCols<<" "<<minVal<<" "<<maxVal; //prints image params
	output<<endl;
	if(zborder==false){
		for(int row = 1; row<=numRows; row++){		//print out from [1][1] w/o border
			for(int col = 1; col<=numCols; col++){
				int pixel_val = ary[row][col];
				if(pixel_val > 0) output << pixel_val <<" ";  //if not 0. output 1 space,
	   			else output << "  "; 						// if 0. output 2 space
			}
			output<<endl;
		}
	}
	if(zborder==true){
		for(int row = 0; row<=numRows+1; row++){		//print out from [0][0] with border
			for(int col = 0; col<=numCols+1; col++){
				int pixel_val= ary[row][col];
				if(pixel_val > 0) output << pixel_val <<" ";  //if not 0. output 1 space,
	   			else output << "  "; 						// if 0. output 2 space
			}
			output<<endl;
		}
	}
}

int main(int argc, char *argv[]) {
	ifstream input(argv[1]);
	ofstream output1(argv[2]);
	ofstream output2(argv[3]);
	
	loadImage(input);  //loads parameters andf fills array
	zeroFramed(imgAry, numRows, numCols);
	
	output1<<numRows<<" "<<numCols<<" "<<minVal<<" "<<maxVal<<endl;
	output2<<numRows<<" "<<numCols<<" "<<minVal<<" "<<maxVal<<endl;
	getChainCode(output1, output2);
	
	input.close();
	output1.close();
	output2.close();
	
	for (int i = 0; i < numRows+2; ++i )delete [] imgAry[i];
	delete [] imgAry;
	
	//delete []neighborCoord;
}



