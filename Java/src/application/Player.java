package application;

import java.util.ArrayList;

public class Player
{
	private String name;
	private Hand hand;
	private boolean isTurn;
	private int sequenceCount;
	
	public Player(String name)
	{
		this.name = name;
		this.hand = new Hand();
		this.isTurn = false;
		this.sequenceCount = 0;
	}
	
	public ArrayList<Card> getCards()
	{
	   return hand.getCards();
	}
	
	public Hand getHand()
	{
	    return hand;
	}
	
	public String getName()
	{
	    return name;
	}
	
	public boolean isTurn()
	{
		return isTurn;
	}
	
	public void setTurn(boolean isTurn)
	{
		this.isTurn = isTurn;
	}
	
	public boolean hasWon()
	{
	    return sequenceCount >= 2;
	}

	public int getScore()
	{
		return sequenceCount;
	}

	public int getSequenceCount()
	{
		return sequenceCount;
	}

	public void setSequenceCount(int sequenceCount)
	{
		this.sequenceCount = sequenceCount;
	}

    public void printCards()
	{
	    System.out.println(getCards());
	}
}
