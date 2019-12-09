package sample;

import com.dataflow.Components;
import com.dataflow.DataFlow;
import com.services.ExecuteDataflow;
import com.xml.XmlHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFlow dataFlow = Session.getDataFlow();
        XmlHelper.Dataflow2Xml(dataFlow);
    }

    @FXML
    public void openExtractSource() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("source/index.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Source Configuration");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    public void openTransform() throws IOException {
        if (!checkSourceExist()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select source to transform");
            alert.show();
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("transform/index.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Source Configuration");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    private boolean checkSourceExist() {
        DataFlow dataFlow = Session.getDataFlow();
        try {
            Components components = dataFlow.getExecutables().getPineline().getComponents();
            if (components.getSource() == null) {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @FXML
    private void openDestination() throws IOException {
        if (checkSourceExist() == false) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select source to transform");
            alert.show();
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("destination/index.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Destination Configuration");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void execute() {
        if (!checkDestinationExist()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You must select destination to transform");
            alert.show();
            return;
        }

        ExecuteDataflow executeDataflow = new ExecuteDataflow();
        if (executeDataflow.execute()) {
            try {
                showSaveDialog();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDestinationExist() {
        DataFlow dataFlow = Session.getDataFlow();
        try {
            Components components = dataFlow.getExecutables().getPineline().getComponents();
            if (components.getDestination() == null) {
                return false;
            }
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    private void showSaveDialog() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("save_confirm.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Save config");
        stage.showAndWait();
    }
}
