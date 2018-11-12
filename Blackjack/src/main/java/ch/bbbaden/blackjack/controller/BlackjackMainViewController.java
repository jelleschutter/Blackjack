/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.blackjack.controller;

import ch.bbbaden.blackjack.MainApp;
import ch.bbbaden.blackjack.model.BlackjackLogic;
import ch.bbbaden.blackjack.model.Card;
import ch.bbbaden.blackjack.model.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class BlackjackMainViewController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label lblBalance;
    @FXML
    private Label lblBet;
    @FXML
    private ImageView startButton;
    @FXML
    private ImageView hitButton;
    @FXML
    private ImageView standButton;
    @FXML
    private ImageView doubleButton;
    @FXML
    private ImageView insuranceButton;
    @FXML
    private ImageView imgJetonTen;
    @FXML
    private ImageView imgJetonFIfty;
    @FXML
    private ImageView imgJetonHundred;
    @FXML
    private ImageView imgViewTutorial;

    private double x;
    private double y;
    
    private ImageView selectedImageView;
    
    private Stage stage;
    
    private Player player;
    
    private BlackjackLogic bj;
    
    private int bet = 0;
    private int index = 0;
    
    private ArrayList<ImageView> dealerCardArray = new ArrayList<>();
    private ArrayList<ImageView> playerCardArray = new ArrayList<>();
    
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stage = MainApp.getStage();
        this.player = Player.getInstance();
        this.bj = new BlackjackLogic();
        updateBalance();
        updateButtons();
    }    
    
    
    /**
     * Since we're using an undecorated stage, we had to do the window dragging
     * manually using the following two methods.
     */
    @FXML
    private void onPanePressed(MouseEvent event) {
        x = stage.getX() - event.getScreenX();
        y = stage.getY() - event.getScreenY();
    }

    @FXML
    private void onPaneDragged(MouseEvent event) {
        stage.setX(event.getScreenX() + x);
        stage.setY(event.getScreenY() + y);
    }
    
    /**
     * Hover effect for all the buttons.
     */
    @FXML
    private void highlightSquareButton(MouseEvent event) {
        selectedImageView = ((ImageView) event.getSource());
        selectedImageView.setImage(new Image("images/btn2_pressed.png"));
    }
    @FXML
    private void unHighlightSquareButton(MouseEvent event) {
        selectedImageView.setImage(new Image("images/btn2.png"));
    }

    @FXML
    private void highlightRoundButton(MouseEvent event) {
        selectedImageView = ((ImageView) event.getSource());
        selectedImageView.setImage(new Image("images/btn_toolbar_highlighted.png"));
    }
    
    @FXML
    private void unHighlightRoundButton(MouseEvent event) {
        selectedImageView.setImage(new Image("images/btn_toolbar.png"));
    }

    @FXML
    private void exit(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    private void minimize(MouseEvent event) {
        stage.setIconified(true);
    }
    
    /**
     * Starts the game.
     * @param event 
     */
    @FXML
    private void btnStart(MouseEvent event) {
        ImageView button = (ImageView) event.getSource();
        if (button.getOpacity() == 1) {
                bj.start();
                updateCards();
                updateBalance();
                if (bj.isInGame() == false && bj.getBet() != 0) {
                    endGame();
                }
        }
        updateButtons();
    }
    
    /**
     * Draws a new card.
     * @param event 
     */
    @FXML
    private void btnHit(MouseEvent event) {
        ImageView button = (ImageView) event.getSource();
        if (button.getOpacity() == 1) {
            bj.hit();
            updateCards();
            updateBalance();
            if (bj.isInGame() == false) {
                endGame();
            }
        }
        updateButtons();
    }
    
    
    /**
     * Stops drawing new cards and then it's the dealer's turn.
     * @param event 
     */
    @FXML
    private void btnStand(MouseEvent event) {
        ImageView button = (ImageView) event.getSource();
        if (button.getOpacity() == 1) {
            bj.stand();
            updateCards();
            updateBalance();
            if (bj.isInGame() == false) {
                endGame();
            }
        }
        updateButtons();
    }
    
    /**
     * Doubles the bet, if allowed.
     * @param event 
     */
    @FXML
    private void btnDouble(MouseEvent event) {
        ImageView button = (ImageView) event.getSource();
        if (button.getOpacity() == 1) {
            bj.doublebet();
            updateCards();
            updateBalance();
            if (bj.isInGame() == false) {
                endGame();
            }
        }
        updateButtons();
    }
    
    
    /**
     * Insures the bet, if the dealer's first card's an ace.
     * @param event 
     */
    @FXML
    private void btnInsurance(MouseEvent event) {
        ImageView button = (ImageView) event.getSource();
        if (button.getOpacity() == 1) {
            bj.insurance();
            updateBalance();
            if (bj.isInGame() == false) {
                endGame();
            }
        }
        updateButtons();
    }
    
    /**
     * Updates the opacity of the buttons.
     */
    private void updateButtons() {
        if (!bj.isInGame()) {
            startButton.setOpacity(1);
            hitButton.setOpacity(0.5);
            standButton.setOpacity(0.5);
            doubleButton.setOpacity(0.5);
            insuranceButton.setOpacity(0.5);
        } else if (8 < BlackjackLogic.getValue(bj.getPlayerHand()) && BlackjackLogic.getValue(bj.getPlayerHand()) < 12 && bj.getPlayerHand().size() == 2) {
            startButton.setOpacity(0.5);
            hitButton.setOpacity(1);
            standButton.setOpacity(1);
            doubleButton.setOpacity(1);
        } else {
            startButton.setOpacity(0.5);
            hitButton.setOpacity(1);
            standButton.setOpacity(1);
            doubleButton.setOpacity(0.5);
        }
        if (bj.isInGame() && !bj.isInsurancePlaced() && bj.getDealerHand().get(0).getValue() == 1 && BlackjackLogic.getValue(bj.getDealerHand()) == 11) {
            insuranceButton.setOpacity(1);
        } else {
            insuranceButton.setOpacity(0.5);
        }
    }
    
    
    /**
     * Updates the imageview's in which the cards are being displayed.
     */
    private void updateCards() {
        
        for (ImageView imgView : playerCardArray) {
            anchorPane.getChildren().remove(imgView);
        }

        playerCardArray.clear();
        
        ImageView cImgView;
        int x = 376;
        int y = 562;
        int width = 0;
        for (Card card : bj.getPlayerHand()) {
            cImgView = new ImageView(card.getImage());
            cImgView.setX(x);
            cImgView.setY(y);
            cImgView.setFitHeight(120);
            cImgView.setFitWidth(78);
            anchorPane.getChildren().add(cImgView);
            playerCardArray.add(cImgView);
            x += 88;
        }
        
        for (ImageView imgView : dealerCardArray) {
            anchorPane.getChildren().remove(imgView);
        }

        dealerCardArray.clear();
        
        x = 376;
        y = 205;
        width = 0;
        for (Card card : bj.getDealerHand()) {
            cImgView = new ImageView(card.getImage());
            cImgView.setLayoutX(x);
            cImgView.setLayoutY(y);
            cImgView.setFitHeight(120);
            cImgView.setFitWidth(78);
            anchorPane.getChildren().add(cImgView);
            dealerCardArray.add(cImgView);
            x += 88;
        }
    }
    
    /**
     * Adds 10 CHF to the bet.
     * @param event 
     */
    @FXML
    private void clickAddJeton10(MouseEvent event) {
        
        if(!bj.isInGame()) {
            bj.addBet(10);
            updateBalance();
        }
    }
    
    /**
     * Adds 50 CHF to the bet.
     * @param event 
     */
    @FXML
    private void clickAddJeton50(MouseEvent event) {
        if(!bj.isInGame()) {
            bj.addBet(50);
            updateBalance();
        }
    }
    
    /**
     * Adds 100 CHF to the bet.
     * @param event 
     */
    @FXML
    private void clickAddJeton100(MouseEvent event) {
        if(!bj.isInGame()) {
            bj.addBet(100);
            updateBalance();
        }
    }
    
    /**
     * Removes 10 CHF to the bet.
     * @param event 
     */
    @FXML
    private void clickRemoveJeton10(MouseEvent event) {
        if(!bj.isInGame() && bj.getBet() > 10) {
            bj.removeBet(10);
            updateBalance();
        }
    }
    
    /**
     * Removes 50 CHF to the bet.
     * @param event 
     */
    @FXML
    private void clickRemoveJeton50(MouseEvent event) {
        if(!bj.isInGame() && bj.getBet() > 50) {
            bj.removeBet(50);
            updateBalance();
        }
    }
    
    /**
     * Removes 100 CHF to the bet.
     * @param event 
     */
    @FXML
    private void clickRemoveJeton100(MouseEvent event) {
        if(!bj.isInGame() && bj.getBet() > 100) {
            bj.removeBet(100);
            updateBalance();
        }
    }
    
    /**
     * Updates the user balance.
     */
    private void updateBalance() {
        lblBalance.setText(player.getAccountBalance() + " CHF");
        lblBet.setText(bj.getBet() + " CHF");
    }
    
    /**
     * Handles the scene switching in the controller.
     *
     * @param fxml
     */
    private void sceneSwitch(String fxml, String title) {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource(fxml));
        } catch (IOException ex) {
            Logger.getLogger(BlackjackMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Switches back to the end view.
     */
    private void endGame() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/BlackjackEndView.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(BlackjackMainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        
        Stage endStage = new Stage(StageStyle.TRANSPARENT);

        endStage.setTitle("");
        endStage.setScene(scene);
        endStage.show();
    }

    /**
     * Handles the tutorial image switching.
     * @param event 
     */
    @FXML
    private void nextTutorialImage(MouseEvent event) {
        index++;
        try {
            imgViewTutorial.setVisible(true);
            imgViewTutorial.setImage(new Image("images/tutorial/" + index + ".png"));
        } catch(Exception ex) {
            index = 0;
            imgViewTutorial.setVisible(false);
        }
    }

}
