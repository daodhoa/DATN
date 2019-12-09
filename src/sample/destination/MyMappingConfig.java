package sample.destination;

import com.connection.MysqlConnection;
import com.dataflow.Components;
import com.dataflow.DataFlow;
import com.dataflow.MySqlManager;
import com.dataflow.components.MysqlDestination;
import com.model.Column;
import com.model.MySql;
import com.services.MysqlService;
import com.xml.XmlHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.Session;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MyMappingConfig implements Initializable {
    @FXML
    private ListView mappingListView;
    @FXML
    private ListView destinationListView;

    List<Column> sourceColumns = new ArrayList<>();
    ObservableList observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MySql mySql = XmlHelper.Xml2Mysql("config/mysql_temp.xml");
        MysqlConnection mysqlConnection = new MysqlConnection(mySql.getHostname(), mySql.getUsername(), mySql.getPassword());
        mysqlConnection.setDatabaseName(mySql.getDatabase());

        try {
            Connection connection = mysqlConnection.getConnection();
            MysqlService mysqlService = new MysqlService(connection);
            Map<String, Integer> listDesColumns = mysqlService.getListColumns(mySql.getTableName(), mySql.getDatabase());
            for (Map.Entry<String, Integer> entry : listDesColumns.entrySet()){
                destinationListView.getItems().add(entry.getKey());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bindToMappingListView(sourceColumns, destinationListView, observableList, mappingListView);

    }

    static void bindToMappingListView(List<Column> sourceColumns, ListView destinationListView, ObservableList observableList, ListView mappingListView) {
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

        destinationListView.getItems().forEach(item -> {
            ComboBox comboBox = new ComboBox();
            comboBox.setMinWidth(130);
            comboBox.setMaxHeight(15);
            sourceColumns.forEach(column -> {
                comboBox.getItems().add(column.getName());
            });
            comboBox.getSelectionModel().select(0);
            observableList.add(comboBox);
        });

        mappingListView.getItems().addAll(observableList);
    }

    @FXML
    private void handleRemove() {
        int selectedIndex = destinationListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex != -1 && destinationListView.getItems().size() > 1) {
            destinationListView.getItems().remove(selectedIndex);
            mappingListView.getItems().remove(selectedIndex);
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) mappingListView.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void saveAndClose() {
        MySql mySql = XmlHelper.Xml2Mysql("config/mysql_temp.xml");
        MysqlConnection mysqlConnection = new MysqlConnection(mySql.getHostname(), mySql.getUsername(), mySql.getPassword());
        mysqlConnection.setDatabaseName(mySql.getDatabase());

        try {
            Connection connection = mysqlConnection.getConnection();
            MysqlService mysqlService = new MysqlService(connection);
            Map<String, Column> databaseColumns = mysqlService.getMapColumns(mySql.getTableName(), mySql.getDatabase());

            List<Column> listInputColumns = new ArrayList<>();

            for (int i = 0; i < destinationListView.getItems().size(); i ++) {
                String desName = (String) destinationListView.getItems().get(i);
                Column column = databaseColumns.get(desName);

                ComboBox mapBox = (ComboBox) mappingListView.getItems().get(i);
                int j = mapBox.getSelectionModel().getSelectedIndex();

                column.setLinearId(sourceColumns.get(j).getId());
                listInputColumns.add(column);
            }

            MysqlDestination mysqlDestination = new MysqlDestination();
            MySqlManager mySqlManager = new MySqlManager(mySql);
            mysqlDestination.setConnectionManagerRefId(mySqlManager.getRefId());
            mysqlDestination.setTableName(mySql.getTableName());
            mysqlDestination.setInputColumns(listInputColumns);

            DataFlow dataFlow = Session.getDataFlow();
            dataFlow.getConnectionManager().setMySqlManagerDestination(mySqlManager);
            dataFlow.getExecutables().getPineline().getComponents().setMysqlDestination(mysqlDestination);
            XmlHelper.Dataflow2Xml(dataFlow);
            handleCancel();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
