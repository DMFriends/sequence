package application;

public class Board
{
	public static final String[][] BOARD = {
		{"2D", "3D", "4D", "5D", "6D", "7D", "8D", "9D"},
		{"AS", "5C", "4C", "3C", "2C", "AH", "KH", "10D"},
		{"KS", "6C", "5H", "4H", "3H", "2H", "QH", "QD"},
		{"QS", "7C", "6H", "7H", "8H", "9H", "10H", "KD"},
		{"10S", "8C", "9C", "10C", "QC", "KC", "AC", "AD"},
		{"9S", "8S", "7S", "6S", "5S", "4S", "3S", "2S"}
	};

	public static final int ROWS = BOARD.length;
	public static final int COLS = BOARD[0].length;

	public String getCard(int row, int col)
	{
		return BOARD[row][col];
	}

	public int[] findCard(String card)
	{
		for (int row = 0; row < ROWS; row++)
		{
			for (int col = 0; col < COLS; col++)
			{
				if (BOARD[row][col].equals(card))
				{
					return new int[] {row, col};
				}
			}
		}

		return null;
	}
}
