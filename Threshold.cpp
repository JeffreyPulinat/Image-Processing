
#include <iostream>
#include <fstream>
#include <string>
using namespace std;

static int numRows;
static int numCols;
static int minVal;
static int maxVal;
static int thrValue;

void thrPrint(int colCount, ifstream& input, ofstream& output){
	minVal=0; maxVal=1; //changes max and min to reflect threshold values.
	output<<numRows<<" "<<numCols<<" "<<minVal<<" "<<maxVal<<endl;
	int pixel_val;
	while(input >> pixel_val){
		if(colCount>=numCols){
	   		colCount=0;
	   		output<< endl;
	   }
		if(pixel_val >= thrValue){
        	output <<"1 ";	//if more than threshold value output 1
        	colCount++;
	   	}else  {
	   		output <<"0 ";
	   		colCount++;

	   	}
	}
}

//remove extension from filename
void remExten(string& fname) {
	int extension = fname.find_last_of(".");
	fname = fname.substr(0, extension);
}

int main(int argc, char *argv[]) {
	//Get user threshold
	cout<<"Enter a Threshold value: ";
	cin>>thrValue;
	
	ifstream input(argv[1]);
	string arg1 =argv[1];
	remExten(arg1);
	
	//making output file
	string thresh = thrValue+"";
	string outName = arg1 + "_thr_" + thresh +".txt";
	ofstream output(outName.c_str());
	
	//getting image parameters
	int param;
	input >> param;
	numRows = param;
	input >> param;
	numCols = param;
	input >> param;
	minVal = param;
	input >> param;
	maxVal = param;
	
	int colCount=0;
	thrPrint(colCount,input, output);
	input.close();
	output.close();
}
	

