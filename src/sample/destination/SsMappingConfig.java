package sample.destination;

import com.connection.SqlServerConnection;
import com.dataflow.Components;
import com.dataflow.DataFlow;
import com.dataflow.SqlServerManager;
import com.dataflow.components.SqlServerDestination;
import com.model.Column;
import com.model.SqlServer;
import com.services.SqlServerService;
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
import java.util.*;

public class SsMappingConfig implements Initializable {
    @FXML
    private ListView mappingListView;
    @FXML
    private ListView destinationListView;

    List<Column> sourceColumns = new ArrayList<>();
    ObservableList observableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SqlServer sqlServer = XmlHelper.XmlToSqlServer("config/sql_server_temp.xml");
        System.out.println("hello");
        System.out.println(sqlServer.getHostname() + "ahaha" +sqlServer.getUsername());
        SqlServerConnection sqlServerConnection = new SqlServerConnection(sqlServer.getHostname(),
                sqlServer.getUsername(), sqlServer.getPassword());
        sqlServerConnection.setDatabaseName(sqlServer.getDatabase());
        try {
            Connection conn = sqlServerConnection.getConnection();
            SqlServerService sqlServerService = new SqlServerService(conn);
            Map<String, Integer> listDesColumns = sqlServerService.getListColumns(sqlServer.getTableName());

            for (Map.Entry<String, Integer> entry : listDesColumns.entrySet()){
                destinationListView.getItems().add(entry.getKey());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MyMappingConfig.bindToMappingListView(sourceColumns, destinationListView, observableList, mappingListView);
    }

    @FXML
    private void saveAndClose() {
        SqlServer sqlServer = XmlHelper.XmlToSqlServer("config/sql_server_temp.xml");
        SqlServerConnection sqlServerConnection = new SqlServerConnection(sqlServer.getHostname(),
                sqlServer.getUsername(), sqlServer.getPassword());
        sqlServerConnection.setDatabaseName(sqlServer.getDatabase());
        try {
            Connection conn = sqlServerConnection.getConnection();
            SqlServerService sqlServerService = new SqlServerService(conn);
            Map<String, Column> databaseColumns = sqlServerService.getMapColumns(sqlServer.getTableName());

            List<Column> listInputColumns = new ArrayList<>();

            for (int i = 0; i < destinationListView.getItems().size(); i ++) {
                String desName = (String) destinationListView.getItems().get(i);
                Column column = databaseColumns.get(desName);

                ComboBox mapBox = (ComboBox) mappingListView.getItems().get(i);
                int j = mapBox.getSelectionModel().getSelectedIndex();

                column.setLinearId(sourceColumns.get(j).getId());
                listInputColumns.add(column);
            }

            SqlServerDestination sqlServerDestination = new SqlServerDestination();
            SqlServerManager sqlServerManager = new SqlServerManager(sqlServer);
            sqlServerDestination.setConnectionManagerRefId(sqlServerManager.getRefId());
            sqlServerDestination.setTableName(sqlServer.getTableName());
            sqlServerDestination.setInputColumns(listInputColumns);

            DataFlow dataFlow = Session.getDataFlow();
            dataFlow.getConnectionManager().setSqlServerManagerDestination(sqlServerManager);
            dataFlow.getExecutables().getPineline().getComponents().setSqlServerDestination(sqlServerDestination);
            XmlHelper.Dataflow2Xml(dataFlow);
            handleCancel();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
