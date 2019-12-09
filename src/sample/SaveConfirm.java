package sample;

import com.connection.SqlServerConnection;
import com.dataflow.DataFlow;
import com.dataflow.SqlServerManager;
import com.model.SqlServer;
import com.xml.XmlHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;

public class SaveConfirm {
    @FXML
    private TextField txtName;

    @FXML
    private void save() {
        String name = txtName.getText().trim();
        if (name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill Name field!");
            alert.show();
            return;
        }
        String temp = name + ".xml";
        boolean check = new File("save", temp).exists();
        if (check) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File already exists!");
            alert.show();
        } else {
            DataFlow dataFlow = Session.getDataFlow();
            String path = "save/" + temp;
            XmlHelper.saveDataFlow(dataFlow, path);
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Save successfully");
            success.show();
            cancel();
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
