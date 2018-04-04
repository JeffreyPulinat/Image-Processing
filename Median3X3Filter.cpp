#include <iostream>
#include <fstream>
#include <string>
#include <stdlib.h>

using namespace std;

	static int numRows;
	static int numCols;
	static int minVal;
	static int maxVal;
	static int newMin;
	static int newMax;
	static int neighborAry [9];
	static int** mirrorFramedAry;
	static int** tempAry;

void mirrorFramed(ifstream& input){
	mirrorFramedAry = new int *[numRows+2];
	for(int i=0; i<numRows+2; ++i){
		mirrorFramedAry[i] = new int[numCols+2];
	}
      
	for(int row = 1; row<= numRows; row++){
		for(int col = 1; col<= numCols; col++){
			int value;
			input >> value;
			mirrorFramedAry[row][col] = value;
			if(row == 1) mirrorFramedAry[0][col] = value; //mirrors top row
			if(row == numRows) mirrorFramedAry[row+1][col] = value;	//mirrors bottom row
		}
	}

	for(int side=0; side<=numRows+1; side++){ //mirroring sides
		mirrorFramedAry[side][0] = mirrorFramedAry[side][1];  //left side
		mirrorFramedAry[side][numCols+1] = mirrorFramedAry[side][numCols]; //right side
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

	mirrorFramed(input);
}

void MEDIAN3X3(int row, int col, int count) {
	 int i, j, min_idx;
 
    // Selection sort
    //stop after the fifth smallest item is found
    for (i = 0; i <=4; i++)
    {
        // find min index
        min_idx = i;
        for (j = i+1; j < 9; j++){
        	if (neighborAry[j] < neighborAry[min_idx])
        	min_idx = j;
        }
 
        // Swap the found minimum element with the first element
		int temp = neighborAry[min_idx];
    	neighborAry[min_idx] = neighborAry[i];
    	neighborAry[i] = temp;
    }

	int median = neighborAry[4];
		
	if(count==1){ //first iteration
		newMin = median;
		newMax = median;
	} else{  //compare and change min and max			
		if(newMin>median)newMin = median;
		if(newMax<median)newMax = median;
	}
			
	tempAry[row][col] = median;
}

void loadNeighbors() {	//load 3x3 neighbors

	tempAry = new int *[numRows+2];
	for(int i=0; i<numRows+2; ++i){
		tempAry[i]= new int[numCols+2];
	}

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
			
			MEDIAN3X3(row,col, count);
			count++;
		}
	}
}



int main(int argc, char *argv[]) {
	ifstream input(argv[1]);
	string arg1 =argv[1];
	
	loadImage(input);
	loadNeighbors();

	
	//making output file
	ofstream output(argv[2]);
	//new params
	output<<numRows<<" "<<numCols<<" "<<newMin<<" "<<newMax<<endl;
	for(int row = 1; row<=numRows; row++){		//print out from [1][1] w/o border
		for(int col = 1; col<=numCols; col++){
			int value = tempAry[row][col];
			output<<value<<" ";
		}
		output<<endl;
	}
	
	for (int i = 0; i < numRows; ++i )delete [] tempAry[i];
	delete [] tempAry;
	for (int i = 0; i < numRows; ++i )delete [] mirrorFramedAry[i];
	delete [] mirrorFramedAry;
	input.close();
	output.close();
}
