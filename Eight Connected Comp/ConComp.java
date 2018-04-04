
public class ConComp {
	int label;
	int numbpixels = 0;
	int minRow = Integer.MAX_VALUE;
	int minCol = Integer.MAX_VALUE;;
	int maxRow = 0;
	int maxCol = 0;

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public int getNumbpixels() {
		return numbpixels;
	}

	public void incNumbpixels() {
		numbpixels++;
	}

	public int getMinRow() {
		return minRow;
	}

	public void setMinRow(int minRow) {
		this.minRow = minRow;
	}

	public int getMinCol() {
		return minCol;
	}

	public void setMinCol(int minCol) {
		this.minCol = minCol;
	}

	public int getMaxRow() {
		return maxRow;
	}

	public void setMaxRow(int maxRow) {
		this.maxRow = maxRow;
	}

	public int getMaxCol() {
		return maxCol;
	}

	public void setMaxCol(int maxCol) {
		this.maxCol = maxCol;
	}
    
 
}