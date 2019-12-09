package sample;

import com.dataflow.ConnectionManager;
import com.dataflow.DataFlow;
import com.dataflow.Executables;
import com.xml.XmlHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SavedConfigs implements Initializable {
    @FXML
    private ListView listView;
    ObservableList observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File folder = new File("save/");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                observableList.add(listOfFiles[i].getName());
            }
        }
        listView.getItems().addAll(observableList);
    }

    @FXML
    private void handleOk() {
        String fileName = (String) listView.getSelectionModel().getSelectedItem();
        try {
            DataFlow oldDataFlow = XmlHelper.xml2DataFlow("save/" + fileName);
            Executables executables = oldDataFlow.getExecutables();
            ConnectionManager connectionManager = oldDataFlow.getConnectionManager();
            DataFlow dataFlow = Session.getDataFlow();
            dataFlow.refresh();
            dataFlow.setExecutables(executables);
            dataFlow.setConnectionManager(connectionManager);
            showOldDataFlow();
            handleCancel();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("something went wrong! Please try again");
            alert.show();
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.close();
    }

    private void showOldDataFlow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("old/index.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Old Config");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
