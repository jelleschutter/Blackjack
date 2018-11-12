package ch.bbbaden.blackjack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainApp extends Application {

    private static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        MainApp.stage = stage;
        stage.getIcons().add(new Image("/images/blackjack_ico.png"));
        
        // removes the top bar of the application window. (undecorated stage)
        stage.initStyle(StageStyle.TRANSPARENT);
        
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/BlackjackMainView.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        
        stage.setTitle("Blackjack");
        stage.setScene(scene);
        stage.show();
        
        
    }

    
    /**
     * Getter for the stage to allow scene switching in other classes.
     * @return 
     */
    public static Stage getStage() {
        return stage;
    }
}

