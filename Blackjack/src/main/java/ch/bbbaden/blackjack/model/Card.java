/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.blackjack.model;

import javafx.scene.image.Image;

public class Card {
    private final String[] color;
    private final String abbreviation;
    private final int value;

    public Card(String[] color, String abbreviation, int value) {
        this.color = color;
        this.abbreviation = abbreviation;
        this.value = value;
    }

    public String[] getColor() {
        return color;
    }

    public int getValue() {
        return value;
    }
    
    /**
     * Loads the image for a card based on value and color
     * @return 
     */
    public Image getImage() {
        Image img = new Image(getClass().getResourceAsStream("/images/cards/" + abbreviation + color[1] +".png"));
        return img;
    }

}
