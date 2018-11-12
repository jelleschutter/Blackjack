/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.blackjack.model;

public class Player {
    private static Player instance = null;
    
    private double accountBalance;
    
    
    /**
     * Private constructor due to the use of the singleton-pattern.
     */
    private Player() {
        accountBalance = 2500.0;
    }
    
    /**
     * If there is no player-object, it gets created, otherwise it just returns the aforementioned object.
     * @return Player object
     */
    public static Player getInstance() {
        if(instance == null) {
            instance = new Player();
        }
        return instance;
    }

    public double getAccountBalance() {
        return accountBalance;
    }
    
    /**
     * Adds the difference of balance to the player's account balance.
     * @param difference 
     */
    public void setAccountBalance(double difference) {
        this.accountBalance += difference;
    }
}
