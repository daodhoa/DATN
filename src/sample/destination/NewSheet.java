package sample.destination;

import com.connection.ExcelConnection;
import com.model.Excel;
import com.xml.XmlHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewSheet implements Initializable {
    @FXML
    private TextField txtSheetName;

    private Excel excel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        excel = XmlHelper.readExcelXml("config/excel_temp.xml");
    }

    @FXML
    private void closeStage() {
        Stage stage = (Stage) txtSheetName.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void save() throws IOException {
        String sheetName = txtSheetName.getText().trim();
        if (sheetName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Sheet name is required!");
            alert.show();
            return;
        }
        ExcelConnection excelConnection = new ExcelConnection(excel.getFilePath());
        excelConnection.insertSheet(sheetName);
        closeStage();
    }
}
