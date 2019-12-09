package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("init.fxml"));
        primaryStage.setTitle("DH ETL Stack");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws IOException {
        System.out.println("Close all tab");
        FileUtils.cleanDirectory(new File("config/"));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
