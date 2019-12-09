package sample.destination;

import com.connection.MysqlConnection;
import com.dataflow.Components;
import com.dataflow.DataFlow;
import com.model.Column;
import com.model.MySql;
import com.services.DataTypeConversion;
import com.services.MysqlService;
import com.xml.XmlHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.Session;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyNewTable implements Initializable {
    @FXML
    private TextArea txtNewTable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFlow dataFlow = Session.getDataFlow();
        List<Column> sourceColumns = new ArrayList<>();
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
        String columnInSqlString = "";
        for (int i = 0; i < sourceColumns.size(); i++) {
            Column column = sourceColumns.get(i);
            columnInSqlString += DataTypeConversion.buildColumnInSqlString(column);
        }

        columnInSqlString = columnInSqlString.substring(0, columnInSqlString.length()-3) + "\n";

        String sqlNewTable = "CREATE TABLE NewTable (\n"+ columnInSqlString +")";
        txtNewTable.setText(sqlNewTable);
    }

    @FXML
    private void closeStage() {
        Stage stage = (Stage) txtNewTable.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveAndClose() {
        String tableString = txtNewTable.getText().trim();
        MySql mySql = XmlHelper.Xml2Mysql("config/mysql_temp.xml");
        MysqlConnection mysqlConnection = new MysqlConnection(mySql.getHostname(),
                mySql.getUsername(), mySql.getPassword());
        mysqlConnection.setDatabaseName(mySql.getDatabase());
        try {
            Connection connection = mysqlConnection.getConnection();
            MysqlService mysqlService = new MysqlService(connection);
            boolean status = mysqlService.createTable(tableString);
            if (status) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Create successfully");
                alert.show();
                closeStage();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Wrong!");
                alert.show();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
