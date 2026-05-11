package application;

public class Card
{
	// These constants represent the possible suits and 
	// can be used to index into the suits array to get
	// their string representation.
    public static final int HEARTS = 0;
    public static final int DIAMONDS = 1;
    public static final int SPADES = 2;
    public static final int CLUBS = 3;
    
    // These constants represent the ranks of the non-number
    // cards, or cards above 10. To maintain the ordering after
    // 2-10, the integer values are 11, 12, 13, and 14 and 
    // also allow us to index into the ranks array to get their
    // String representation.
    public static final int JACK = 11;
    public static final int QUEEN = 12;
    public static final int KING = 13;
    public static final int ACE = 14;

    // public static final String RED = "\u001B[31m";
    // public static final String YELLOW = "\033[0;33m";
    // public static final String BLUE = "\033[0;34m";
	// public static final String RESET = "\u001B[0m";

    // Instance variables
    
    // This represents the rank of the card, the value from 2 to Ace.
    private int rank;
    
    // This represents the suit of the card, one of hearts, diamonds, spades or clubs.
    private int suit;
    
    // This represents whether the card is dead (based on previous use of a Wildcard - see rules)
    private boolean isDead;
    
    // This String array allows us to easily get the String value of a Card from its rank.
    // There are two Xs in the front to provide padding so numbers have their String representation
    // at the corresponding index. For example, the String for 2 is at index 2.
    public static String[] ranks = {"X", "X", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
    
    // The String array allow us to easily get the String value of the Card for its suit. This
    // is the same order as the suits above so we can index into this array.
    public static String[] suits = {"H", "D", "S", "C"};
    
    /**
     * This is the constructor to create a new Card. To create a new card
     * we pass in its rank and its suit.
     * 
     * @param r	The rank of the card, as an int.
     * @param s	The suit of the card, as an int.
     */
    public Card(int r, int s)
    {
        rank = r;
        suit = s;
    }
    
    public Card(String card)
    {
        rank = Deck.findIndex(ranks, card.substring(0, card.length() - 1));
        suit = Deck.findIndex(suits, card.substring(card.length() - 1));
    }
    
    // Getter Methods
    
    /**
     * This returns the rank of the card as an integer.
     * 
     * @return rank of card as an int.
     */
    public int getRank()
    {
        return rank;
    }
    
    /**
     * This returns the suit of the card as an integer.
     * 
     * @return suit of card as an int.
     */
    public int getSuit()
    {
        return suit;
    }
    
    public boolean isDead()
    {
    	return isDead;
    }
    
    public void setDead(boolean val)
    {
    	isDead = val;
    }
    
    /**
     * This utility method converts from a rank integer to a String.
     * 
     * @param r	The rank.
     * @return	String version of rank.
     */
    public String rankToString(int r)
    {
        return ranks[r];
    }
    
    /**
     * This utility method converts from a suit integer to a String.
     * 
     * @param s	The suit.
     * @return	String version of suit.
     */
    public String suitToString(int s)
    {
        return suits[s];
    }
    
    /**
     * This returns the String representation of a card which 
     * will be two characters. For example, the two of hearts would
     * return 2H. Face cards have a short string so the ace of
     * spades would return AS.
     * 
     * @return String representation of Card.
     */
    public String toString()
    {
        // Get a string for rank
        String rankString = ranks[rank];
        
        // Get a string for the suit
        String suitString = suits[suit];
        
        // Combine those
        return rankString + suitString;
    }
}
