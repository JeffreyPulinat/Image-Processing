#include <iostream>
#include <fstream>
#include <string>
using namespace std;

static int numRows;
static int numCols;
static int minVal;
static int maxVal;

void prettyPrint(int colCount, ifstream& input, ofstream& output){
	output<<numRows<<" "<<numCols<<" "<<minVal<<" "<<maxVal<<endl; //prints image params
	int pixel_val;
	while(input >> pixel_val){
		if(colCount>=numCols){
	   		colCount=0;
	   		output<< endl;
	   }
		if(pixel_val > 0){		//if not 0 stays the same.
        	output << pixel_val <<" ";	
        	colCount++;
	   	}else  {
	   		output << "  "; // if 0 ouput 2 blanks
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
	ifstream input(argv[1]);
	string arg1 =argv[1];
	remExten(arg1);
	
	//making output file
	string outName =arg1 + "_PP.txt";
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
	prettyPrint(colCount,input, output);
	
	input.close();
	output.close();
	
}







