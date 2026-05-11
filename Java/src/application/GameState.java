package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameState
{
	private static final int CARDS_PER_PLAYER = 7;
	private static final int SEQUENCES_TO_WIN = 4;

	private Board board;
	private Deck deck;
	private List<Player> players;
	private Player[][] owners;
	private int currentPlayerIndex;
	private boolean gameOver;
	private String lastMoveError;
	private String lastMoveDescription;

	public GameState(List<Player> players)
	{
		this(players, null);
	}

	public GameState(List<Player> players, Set<String> unusedDictionary)
	{
		this.players = players;
		this.board = new Board();
		this.deck = new Deck();
		this.owners = new Player[Board.ROWS][Board.COLS];
		this.currentPlayerIndex = 0;
		this.gameOver = false;
		this.lastMoveError = "";
		this.lastMoveDescription = "No moves yet.";

		for (Player player : players)
		{
			drawCards(player, CARDS_PER_PLAYER);
		}
	}

	public Player getCurrentPlayer()
	{
		return players.get(currentPlayerIndex);
	}

	public Board getBoard()
	{
		return board;
	}

	public List<Player> getPlayers()
	{
		return players;
	}

	public int getDeckSize()
	{
		return deck.size();
	}

	public Player getOwner(int row, int col)
	{
		return owners[row][col];
	}

	public String getBoardCard(int row, int col)
	{
		return board.getCard(row, col);
	}

	public boolean isGameOver()
	{
		return gameOver;
	}

	public String getLastMoveError()
	{
		return lastMoveError;
	}

	public String getLastMoveDescription()
	{
		return lastMoveDescription;
	}

	public boolean playCard(Card card, int row, int col)
	{
		lastMoveError = "";

		if (gameOver)
		{
			lastMoveError = "The game is already over.";
			return false;
		}

		if (card == null || getCurrentPlayer().getHand().find(card.getRank(), card.getSuit()) == null)
		{
			lastMoveError = "Choose a card from your hand first.";
			return false;
		}

		if (!isInsideBoard(row, col) || owners[row][col] != null)
		{
			lastMoveError = "That space is already occupied.";
			return false;
		}

		String boardCard = board.getCard(row, col);
		boolean jack = isJack(card);

		if (jack && playerHasCard(getCurrentPlayer(), boardCard))
		{
			lastMoveError = "You already have " + boardCard + ". Play that card instead of using a Jack.";
			return false;
		}

		if (!jack && !boardCard.equals(card.toString()))
		{
			int[] cardLocation = board.findCard(card.toString());
			if (cardLocation == null)
			{
				lastMoveError = "That card is not on the board.";
			}
			else
			{
				lastMoveError = "That card belongs at row " + (cardLocation[0] + 1)
						+ ", column " + (cardLocation[1] + 1) + ".";
			}
			return false;
		}

		Player player = getCurrentPlayer();
		player.getHand().remove(card);
		owners[row][col] = player;

		if (jack)
		{
			removeDeadCard(boardCard, player);
			lastMoveDescription = player.getName() + " played " + card + " as a wild card on " + boardCard + ".";
		}
		else
		{
			lastMoveDescription = player.getName() + " played " + card + ".";
		}

		drawCards(player, 1);
		updateSequenceCounts();

		if (player.getSequenceCount() >= SEQUENCES_TO_WIN)
		{
			gameOver = true;
			return true;
		}

		if (deck.size() == 0 && allHandsEmpty())
		{
			gameOver = true;
			return true;
		}

		nextTurn();
		return true;
	}

	public void endGame()
	{
		updateSequenceCounts();
		gameOver = true;
	}

	public Player getWinner()
	{
		List<Player> winners = getWinners();
		if (winners.size() == 1)
		{
			return winners.get(0);
		}

		return null;
	}

	public List<Player> getWinners()
	{
		updateSequenceCounts();
		List<Player> winners = new ArrayList<>();
		int currentHigh = players.get(0).getSequenceCount();

		for (Player player : players)
		{
			if (player.getSequenceCount() > currentHigh)
			{
				currentHigh = player.getSequenceCount();
			}
		}

		for (Player player : players)
		{
			if (player.getSequenceCount() == currentHigh)
			{
				winners.add(player);
			}
		}

		return winners;
	}

	public String getEndMessage()
	{
		List<Player> winners = getWinners();
		if (winners.size() == 1)
		{
			Player winner = winners.get(0);
			return winner.getName() + " wins with " + formatSequenceCount(winner.getSequenceCount()) + "!";
		}

		return "The game ended in a tie.";
	}

	public void recallTiles()
	{
		// Kept for older screen code; Sequence has no tentative placements.
	}

	private void nextTurn()
	{
		currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
	}

	private boolean isInsideBoard(int row, int col)
	{
		return row >= 0 && row < Board.ROWS && col >= 0 && col < Board.COLS;
	}

	private boolean isJack(Card card)
	{
		return card.getRank() == Card.JACK;
	}

	private void drawCards(Player player, int count)
	{
		for (int i = 0; i < count; i++)
		{
			Card card = deck.draw();
			if (card != null)
			{
				player.getHand().add(card);
			}
		}
	}

	private void removeDeadCard(String cardName, Player currentPlayer)
	{
		Card deadCard = new Card(cardName);
		deck.remove(deadCard);

		for (Player player : players)
		{
			if (player == currentPlayer)
			{
				continue;
			}

			Card cardInHand = player.getHand().find(cardName);
			if (cardInHand != null)
			{
				player.getHand().remove(cardInHand);
				drawCards(player, 1);
			}
		}
	}

	private boolean playerHasCard(Player player, String cardName)
	{
		return player.getHand().find(cardName) != null;
	}

	private boolean allHandsEmpty()
	{
		for (Player player : players)
		{
			if (!player.getCards().isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	private void updateSequenceCounts()
	{
		for (Player player : players)
		{
			player.setSequenceCount(countSequences(player));
		}
	}

	private int countSequences(Player player)
	{
		int count = 0;
		int[][] directions = {
				{0, 1},
				{1, 0},
				{1, 1},
				{1, -1}
		};

		for (int row = 0; row < Board.ROWS; row++)
		{
			for (int col = 0; col < Board.COLS; col++)
			{
				for (int[] direction : directions)
				{
					if (hasSequenceFrom(player, row, col, direction[0], direction[1]))
					{
						count++;
					}
				}
			}
		}

		return count;
	}

	private boolean hasSequenceFrom(Player player, int row, int col, int rowStep, int colStep)
	{
		for (int i = 0; i < 4; i++)
		{
			int nextRow = row + rowStep * i;
			int nextCol = col + colStep * i;

			if (!isInsideBoard(nextRow, nextCol) || owners[nextRow][nextCol] != player)
			{
				return false;
			}
		}

		return true;
	}

	private String formatSequenceCount(int count)
	{
		return count + (count == 1 ? " sequence" : " sequences");
	}
}
