/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbbaden.blackjack.controller;

import ch.bbbaden.blackjack.MainApp;
import ch.bbbaden.blackjack.model.Player;
import java.io.IOException;
import java.net.URL;
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

public class BlackjackEndViewController implements Initializable {
    
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label lblBalance;
    @FXML
    private Label lblHeader;
    @FXML
    private Label lblMSG;
    
    private static String header;
    private static String msg;

    private double x;
    private double y;
    
    private ImageView selectedImageView;
    
    private Stage stage;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.stage = MainApp.getStage();
        lblBalance.setText(Player.getInstance().getAccountBalance() + " CHF");
        lblHeader.setText(header);
        lblMSG.setText(msg);
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
    private void minimize(MouseEvent event) {
        stage.setIconified(true);
    }
    
    @FXML
    private void playAgain(MouseEvent event) {
        sceneSwitch("/fxml/BlackjackMainView.fxml", "Blackjack");
        ImageView img = (ImageView) event.getSource();
        img.getScene().getWindow().hide();
    }
    
    @FXML
    private void exit(MouseEvent event) {
        System.exit(0);
    }

    public static void setHeader(String header) {
        BlackjackEndViewController.header = header;
    }

    public static void setMsg(String msg) {
        BlackjackEndViewController.msg = msg;
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
            Logger.getLogger(BlackjackEndViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }  
}
