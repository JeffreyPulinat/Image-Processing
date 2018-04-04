import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Histogram {
	static int numRows;
	static int numCols;
	static int minVal;
	static int maxVal;
	static int[] hist;
	
	public static void main(String[] argv){
		File input = new File(argv[0]);
		File output = new File(argv[1]);
		computeHistogram(input, output);
	}
	public static void computeHistogram(File input, File output){
		try{
			Scanner sc = new Scanner(input);
			//assign params
			numRows=sc.nextInt();
			numCols=sc.nextInt();
			minVal=sc.nextInt();
			maxVal=sc.nextInt();
			
			hist = new int [maxVal+1];
			int index =0;
			while(sc.hasNextInt()){ //fills the hist array
				index = sc.nextInt();
				hist[index]++;
			}
			sc.close();
			
			//writes output file
			BufferedWriter out = new BufferedWriter(new FileWriter(output));
			out.write(numRows+" "+numCols+" "+minVal+" "+maxVal);
			for(int i = 0; i <= maxVal; i++){
				out.newLine();
				out.write(i+"  "+hist[i]);
			}
			out.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
		
}

