package purrinha;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    private final LanguageManager langManager = LanguageManager.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuessingGameGUI.fxml")); 
        Parent root = loader.load();

        primaryStage.setTitle(langManager.getString("game.title"));
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();

        langManager.getCurrentLocale().addListener((observable, oldValue, newValue) -> primaryStage.setTitle(langManager.getString("game.title")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
