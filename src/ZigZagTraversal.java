
import java.util.LinkedList;
import java.util.List;

public class ZigZagTraversal {
	/*
	 * when moving upward diagonal we will increase the value of column and decrease the value of row
	 * when moving downward diagonal we will decrease the value of column and increase the value of row
	 * when we cross the borders of the image(<0 or >height or width) that when we have to move to
	 * next column
	 * when moving to next column we will move to the right by increasing the value of the column
	 * unless we are at the edge(column == 0 or column == width then we will move down by 
	 * increasing the value of row
	 * 
	 * @params (int row, int column, int digonalMove, boolean timeTomoveToNextCol, int imageHeight, int imageWidth)
	 * @return List containing the coordinates of the next pixel(0->row,1->column,2->diagonalMove,3->timeTomoveToNextCol)
	 * 
	 */
	public List<Object> zigZagTraversal(int row, int column, int digonalMove, boolean timeTomoveToNextCol, int imageHeight, int imageWidth) {
		if (timeTomoveToNextCol) {
			if (((column + row) != 0 && row != (imageHeight - 1))
					&& (column == 0 || column == (imageWidth - 1))) {
				row++;
			} else {
				column++;
			}
			timeTomoveToNextCol = false;
		} else {
			column += digonalMove;
			row += (digonalMove * -1);
			if ((column + digonalMove) >= imageWidth || (column + digonalMove) < 0
					|| (row + (digonalMove * -1)) >= imageHeight || (row + (digonalMove * -1)) < 0) {
				timeTomoveToNextCol = true;
				digonalMove *= -1;
			}
		}
		LinkedList<Object> list = new LinkedList<>();
        list.add(row);
        list.add(column);
        list.add(digonalMove);
        list.add(timeTomoveToNextCol);
        return list;
	}
	
}

