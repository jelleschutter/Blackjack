
package ch.bbbaden.blackjack.model;

import ch.bbbaden.blackjack.controller.BlackjackEndViewController;
import java.util.ArrayList;
import java.util.Collections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class BlackjackLogic {
    
    private Player player;
    
    private final ArrayList<Card> dealerHand = new ArrayList<>();
    private final ArrayList<Card> playerHand = new ArrayList<>();
    
    private int bet = 0;
    private boolean insurancePlaced = false;
    private int insurance = 0;
    private boolean inGame = false;

    
    private ArrayList<Card> deck = new ArrayList<>();
    
    private static final String[][] COLORS = {{"Clubs","C"},{"Hearts", "H"},{"Spades","S"},{"Diamonds","D"}};
    private static final Object[][] CARDS = {{"1",1},{"2",2},{"3",3},{"4",4},{"5",5},{"6",6},{"7",7},{"8",8},{"9",9},{"10",10},{"J",10},{"Q",10},{"K",10}};
    
    private static final int NUMBER_OF_DECKS = 6;
    
    private ArrayList<Card> getDeck() {
        if (deck == null || deck.size() < 26 * NUMBER_OF_DECKS) {
            createDeck();
        }
        return deck;
    }

    public BlackjackLogic() {
        createDeck();
        player = Player.getInstance();
    }
    
    public void start() {
        if (!inGame && bet > 0) {
            dealerHand.clear();
            playerHand.clear();
            
            inGame = true;
            
            hit();
            hit();

            dealerHand.add(getCard());
            String[] color = {"0","0"};
            dealerHand.add(new Card(color,"0",0));
        } else if (bet == 0) {
            alert("You need to place a bet!", AlertType.ERROR);
        }
    }
    
    /**
     * Draws a new card.
     */
    public void hit() {
        if (inGame) {
            playerHand.add(getCard());
            checkHit();
        }
    }
    
    /**
     * Stops drawing new cards.
     */
    public void stand() {
        if (inGame) {
            if (BlackjackLogic.getValue(playerHand) != 21) {
                dealerHand.set(1, getCard());
                while (BlackjackLogic.getValue(dealerHand) < 17 && BlackjackLogic.getValue(dealerHand) <= BlackjackLogic.getValue(playerHand)) {
                    dealerHand.add(getCard());
                }
            }
            end();
            inGame = false;
            bet = 0;
            insurance = 0;
            insurancePlaced = false;
        }
    }
    
    /**
     * doubles the bet.
     */
    public void doublebet() {
        if (inGame && (8 < BlackjackLogic.getValue(playerHand) && BlackjackLogic.getValue(playerHand) < 12) && playerHand.size() == 2) {
            if (player.getAccountBalance() >= bet) {
                player.setAccountBalance(-bet);
                bet *= 2;
                hit();
                stand();
            }
        }
    }
    
    /**
     * Insures the bet, if allowed.
     */
    public void insurance() {
        if (inGame && dealerHand.get(0).getValue() == 1 && dealerHand.size() == 2 && !insurancePlaced) {
            insurance += bet;
            bet /= 2;
        }
    }
    
    /**
     * Ends the game and pays out the bet
     */
    public void end() {
        String header = "";
        String msg = "";
        boolean payout = false;
        double payMultiplier = 0.0;
        boolean insuranceValid = false;
        if (BlackjackLogic.getValue(playerHand) > 21) {
            payMultiplier = 0.0;
            header = "You got busted!";
            msg = "You lost " + bet + " CHF!";
        } else if (BlackjackLogic.getValue(playerHand) == 21) {
            payMultiplier = 1.5;
            header = "Blackjack!";
            msg = "You won " + (bet * payMultiplier) + " CHF!";
        } else if (BlackjackLogic.getValue(dealerHand) > 21) {
            payMultiplier = 2.0;
            header = "Dealer got busted!";
            msg = "You won " + (bet * payMultiplier) + " CHF!";
        } else if (BlackjackLogic.getValue(dealerHand) < BlackjackLogic.getValue(playerHand)) {
            payMultiplier = 2.0;
            header = "You have won!";
            msg = "You won " + (bet * payMultiplier) + " CHF!";
        } else if (BlackjackLogic.getValue(dealerHand) == BlackjackLogic.getValue(playerHand)) {
            payMultiplier = 1.0;
            header = "It's a draw!";
            msg = "You got your bet back! You got " + bet + " CHF!";
        } else if (BlackjackLogic.getValue(dealerHand) == 21) {
            payMultiplier = 0.0;
            header = "Dealer has Blackjack!";
            if (!insurancePlaced) {
                msg = "You lost " + bet + " CHF!";
            } else {
                payMultiplier = 1.0;
                msg = "You got your bet back! You got " + bet + " CHF!";
            }
        } else {
            payMultiplier = 0.0;
            header = "You have lost!";
            msg = "You have lost " + bet + " CHF!";
        }
        
        bet *= payMultiplier;
        updateBalance(bet);
        
        BlackjackEndViewController.setHeader(header);
        BlackjackEndViewController.setMsg(msg);
    }
    
    /**
     * Checks if the game should be ended
     */
    private void checkHit() {
        if (getValue(playerHand) >= 21) {
            stand();
        }
    }
    
    /**
     * Saves the balance to the database
     * @param amount 
     */
    private void updateBalance(int amount) {
        player.setAccountBalance(amount);
    }
    
    /**
     * Creates a new deck
     */
    private void createDeck() {
        ArrayList<Card> newDeck = new ArrayList<>();
        String abbreviation;
        int value;
        for (int i = 0; i < NUMBER_OF_DECKS; i++) {
            for (String[] color : COLORS) {
                for (Object[] card : CARDS) {
                    abbreviation = (String) card[0];
                    value = (int) card[1];
                    newDeck.add(new Card(color, abbreviation, value));
                }
            }
        }
        Collections.shuffle(newDeck);
        deck = newDeck;
    }
    
    /**
     * Draws one card from the deck
     * @return 
     */
    public Card getCard() {
        Card card = getDeck().get(deck.size() - 1);
        deck.remove(deck.size() - 1);
        return card;
    }

    /**
     * Updates the bet
     * @param bet 
     */
    public void addBet(int bet) {
        if (!inGame && player.getAccountBalance() >= bet) {
            this.bet += bet;
            updateBalance(-bet);
        } else {
            alert("You don't have enough money!", AlertType.ERROR);
        }
    }
    
    /**
     * Removes money the bet
     * @param bet 
     */
    public void removeBet(int bet) {
        this.bet -= bet;
        updateBalance(bet);
    }

    public int getBet() {
        return bet;
    }    
    
    public ArrayList<Card> getDealerHand() {
        return dealerHand;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    public boolean isInGame() {
        return inGame;
    }

    public boolean isInsurancePlaced() {
        return insurancePlaced;
    }
    
    /**
     * Calculates the value of the hands
     * @param hand
     * @return 
     */
    public static int getValue(ArrayList<Card> hand) {
        int value = 0;
        int aces = 0;
        for (Card card : hand) {
            if (card.getValue() == 1) {
                value++;
                aces++;
            } else {
                value += card.getValue();
            }
        }
        
        while (value + 10 <= 21 && aces > 0) {
            value += 10;
            aces--;
        }        
        return value;
    }
    
    public void alert(String output, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(output);
        alert.show();
    }

}
