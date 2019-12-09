package sample.destination;

import com.dataflow.Components;
import com.dataflow.DataFlow;
import com.dataflow.ExcelManager;
import com.dataflow.components.ExcelDestination;
import com.model.Column;
import com.model.Excel;
import com.xml.XmlHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.Session;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ExcelColumnConfig implements Initializable {

    @FXML
    private ListView listView;

    List<Column> sourceColumns = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFlow dataFlow = Session.getDataFlow();
        Components components = dataFlow.getExecutables().getPineline().getComponents();
        components.getSource().getOutputColumns().forEach(column -> {
            sourceColumns.add(column);
        });
        if (components.getDerivedColumn() != null) {
            List<Column> derivedColumns = components.getDerivedColumn().getOutputColumns();
            derivedColumns.forEach(column -> {
                sourceColumns.add(column);
            });
        }

        listView.getItems().clear();

        sourceColumns.forEach(column -> {
            listView.getItems().add(column.getName());
        });
    }

    @FXML
    private void handleRemove() {
        if (sourceColumns.size() > 1) {
            int selectedIndex = listView.getSelectionModel().getSelectedIndex();
            if (selectedIndex == -1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select a column to remove");
                alert.show();
                return;
            }
            listView.getItems().remove(selectedIndex);
            sourceColumns.remove(selectedIndex);
        }
    }

    @FXML
    private void closeStage() {
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleOk() {
        Excel excel = XmlHelper.readExcelXml("config/excel_temp.xml");
        ExcelManager excelManager = new ExcelManager(excel);
        DataFlow dataFlow = Session.getDataFlow();
        dataFlow.getConnectionManager().setExcelManagerDestination(excelManager);

        ExcelDestination excelDestination = new ExcelDestination();
        excelDestination.setConnectionManagerRefId(excelManager.getRefId());
        excelDestination.setInputColumns(sourceColumns);

        dataFlow.getExecutables().getPineline().getComponents().setExcelDestination(excelDestination);
        XmlHelper.Dataflow2Xml(dataFlow);
        closeStage();
    }
}
