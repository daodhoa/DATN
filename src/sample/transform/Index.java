package sample.transform;

import com.dataflow.Components;
import com.dataflow.DataFlow;
import com.dataflow.components.DerivedColumn;
import com.dataflow.components.SourceInterface;
import com.dataflow.components.SqlServerSource;
import com.enums.DataType;
import com.expression.ExpressionEnums;
import com.expression.ExpressionHelper;
import com.model.Column;
import com.services.ListHelper;
import com.xml.XmlHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Session;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Index implements Initializable {
    @FXML
    private ListView listView;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtSize;
    @FXML
    private TextArea txtExpression;
    @FXML
    private ComboBox cbDataType;

    ObservableList observableList = FXCollections.observableArrayList();
    private List<Column> listColumns = new ArrayList<>();

    private List<String> listColumnNames = new ArrayList<>();
    private List<Column> inputColumns = new ArrayList<>();
    private List<Column> outputColumns = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFlow dataFlow = Session.getDataFlow();
        SourceInterface source = dataFlow.getExecutables().getPineline().getComponents().getSource();
        List<Column> listCurrentColumns = source.getOutputColumns();
        listCurrentColumns.forEach(column -> {
            listColumns.add(column);
        });
        fillToListView(listCurrentColumns);

        Map<DataType, String> mapDataType = Session.getMapDataType();
        for (Map.Entry<DataType, String> entry : mapDataType.entrySet()) {
            cbDataType.getItems().add(entry.getKey());
        }
        cbDataType.getSelectionModel().select(DataType.STRING);
    }

    private void fillToListView(List<Column> listColumns) {
        observableList.clear();
        listView.getItems().clear();
        listColumns.forEach(column -> {
            listColumnNames.add(column.getName());
            observableList.add(column.getName());
        });
        listView.getItems().addAll(observableList);
        listView.refresh();
    }

    private boolean validate() {
        String name = txtName.getText().trim();
        String length = txtSize.getText().trim();
        String expression = txtExpression.getText().trim();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (name.isEmpty() || length.isEmpty() || expression.isEmpty()) {
            alert.setContentText("Please fill full");
            alert.show();
            return false;
        }
        try {
            int columnLength = Integer.valueOf(length);
            if (columnLength <= 0) {
                alert.setContentText("Size must be positive!");
                alert.show();
                return false;
            }
        } catch (Exception e) {
            alert.setContentText("Size must be numeric!");
            alert.show();
            return false;
        }
        return true;
    }

    @FXML
    private void saveColumn() {
        if (!validate()) {
            return;
        }
        ExpressionHelper expressionHelper = new ExpressionHelper();
        ExpressionEnums match = expressionHelper.checkRegex(txtExpression.getText().trim());
        if (match == ExpressionEnums.NONE) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Wrong Expression");
            alert.show();
            return;
        }
        saveToListView();
    }

    private void saveToListView() {
        Map<String, Integer> mapCurrentName = new HashMap<>();
        for (int i = 0; i < listColumnNames.size(); i++) {
            mapCurrentName.put(listColumnNames.get(i), i);
        }

        List<String> nameInExpress = ExpressionHelper.getListColumnNameInExpress(txtExpression.getText().trim());
        nameInExpress.forEach(name -> {
            if (!mapCurrentName.containsKey(name)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Column in expression is not found!");
                alert.show();
                return;
            }
        });

        String columnName = nameInExpress.get(0);
        Column inputColumn = ListHelper.searchListColumn(listColumns, columnName);

        String linearId = inputColumn.getId();
        inputColumns.add(inputColumn);

        Column column = new Column();
        column.setName(txtName.getText().trim());
        column.setId("DataFlow.DerivedColumn.Outputs[DerivedColumnOutput].Columns["+ column.getName() + "]");
        column.setDataType((DataType) cbDataType.getSelectionModel().getSelectedItem());
        column.setLength(Integer.valueOf(txtSize.getText().trim()));
        column.setLinearId(linearId);
        column.setExpression(txtExpression.getText());

        if (nameInExpress.size() == 2) {
            String currentLinearId = column.getLinearId();
            Column input2 = ListHelper.searchListColumn(listColumns, nameInExpress.get(1));
            column.setLinearId(currentLinearId + "|" + input2.getId());
            inputColumns.add(input2);
        }

        listColumns.add(column);
        outputColumns.add(column);
        fillToListView(listColumns);

        reset();
    }

    @FXML
    private void reset() {
        txtName.setText("");
        txtExpression.setText("");
        txtSize.setText("");
    }

    @FXML
    private void showExpressionTutorial() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("expression_tutorial.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Expression Tutorial");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void saveAndClose() {
        DataFlow dataFlow = Session.getDataFlow();
        Components components = dataFlow.getExecutables().getPineline().getComponents();

        DerivedColumn derivedColumn = new DerivedColumn();
        derivedColumn.setInputColumns(inputColumns);
        derivedColumn.setOutputColumns(outputColumns);
        components.setDerivedColumn(derivedColumn);

        dataFlow.getExecutables().getPineline().setComponents(components);
        XmlHelper.Dataflow2Xml(dataFlow);
        closeStage();
    }

    @FXML
    private void closeStage() {
        Stage stage = (Stage) listView.getScene().getWindow();
        stage.close();
    }
}
