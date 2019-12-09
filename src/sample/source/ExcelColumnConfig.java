package sample.source;

import com.connection.ExcelConnection;
import com.dataflow.*;
import com.dataflow.components.ExcelSourceComponent;
import com.enums.DataType;
import com.model.Column;
import com.model.Excel;
import com.xml.XmlHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.Session;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ExcelColumnConfig implements Initializable {

    @FXML
    private ListView<String> listColumnView;

    @FXML
    private TextField txtColumnName, txtColumnLength;

    @FXML
    private ComboBox cbDataType;

    @FXML
    private Button cancelButton;

    ObservableList observableList = FXCollections.observableArrayList();
    private List<Column> listColumns;
    private int selectedIndex = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFlow dataFlow = Session.getDataFlow();
        Excel excel = dataFlow.getConnectionManager().getExcelManager().getExcel();
        String filePath = excel.getFilePath();
        int sheetIndex = excel.getSheetIndex();
        boolean isFirstRow = excel.isFirstRow();
        try {
            showContent(filePath, sheetIndex, isFirstRow);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<DataType, String> mapDataType = Session.getMapDataType();

        for (Map.Entry<DataType, String> entry : mapDataType.entrySet()) {
            cbDataType.getItems().add(entry.getKey());
        }
    }

    private void showContent(String filePath, int sheetIndex, boolean isFirstRow) throws IOException {
        ExcelConnection excelConnection = new ExcelConnection(filePath);
        List<Column> listColumns = excelConnection.getColumns(sheetIndex, isFirstRow);
        if (listColumns == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("This sheet is empty");
            alert.showAndWait();
            closeStage();
            return;
        }
        this.listColumns = listColumns;
        fillToListView();
    }

    private void fillToListView() {
        observableList.clear();
        listColumnView.getItems().clear();
        listColumns.forEach(column -> observableList.add(column.getName()));
        listColumnView.getItems().addAll(observableList);
        listColumnView.refresh();
    }

    @FXML
    private void showColumnDetail() {
        int index = listColumnView.getSelectionModel().getSelectedIndex();
        this.selectedIndex = index;
        if (selectedIndex == -1) {
            return;
        }
        Column column = listColumns.get(index);
        if (column != null) {
            txtColumnName.setText(column.getName());
            txtColumnLength.setText(String.valueOf(column.getLength()));
            cbDataType.getSelectionModel().select(column.getDataType());
        }
    }

    @FXML
    public void save() {
        if(!this.validate()){
            return;
        }
        String newName = txtColumnName.getText();
        String newLength = txtColumnLength.getText();
        DataType dataType = (DataType) cbDataType.getSelectionModel().getSelectedItem();
        int index = this.selectedIndex;
        if (index == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select column");
            alert.show();
            return;
        }
        Column column = new Column();
        column.setName(newName);
        column.setLength(Integer.valueOf(newLength));
        column.setDataType(dataType);

        listColumns.set(index, column);
        this.fillToListView();
    }

    private boolean validate() {
        String errorContent = "";
        boolean isError = false;
        if (txtColumnName.getText().trim().isEmpty()) {
            isError = true;
            errorContent += "Column Name is required! ";
        }
        if (txtColumnLength.getText().trim().isEmpty()) {
            isError = true;
            errorContent += "Column Length is required! ";
        } else {
            try {
                int length = Integer.valueOf(txtColumnLength.getText());
            } catch (Exception e) {
                isError = true;
                errorContent += "Column Length must be numeric! ";
            }
        }
        if (isError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errorContent);
            alert.show();
            return false;
        }
        return true;
    }

    @FXML
    private void saveExcelConfig() {
        DataFlow dataFlow = Session.getDataFlow();
        Excel excel = dataFlow.getConnectionManager().getExcelManager().getExcel();
        excel.setListColumns(listColumns);

        ExcelManager excelManager = dataFlow.getConnectionManager().getExcelManager();
        Executables executables = new Executables();
        Pineline pineline = new Pineline();
        ExcelSourceComponent excelSourceComponent = new ExcelSourceComponent(excelManager.getRefId());
        List<Column> outputColumns = new ArrayList<>();
        listColumns.forEach(column -> {
            Column column1 = new Column();
            column1.setName(column.getName());
            column1.setLength(column.getLength());
            column1.setDataType(column.getDataType());

            column1.setId("DataFlow.ExcelSource.Outputs[ExcelSourceOutput].Columns["+ column1.getName() +"]");
            outputColumns.add(column1);
        });

        excelSourceComponent.setOutputColumns(outputColumns);
        Components components = new Components();
        components.setExcelSourceComponent(excelSourceComponent);
        pineline.setComponents(components);
        executables.setPineline(pineline);
        dataFlow.setExecutables(executables);

        XmlHelper.Dataflow2Xml(dataFlow);
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
