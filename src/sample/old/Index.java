package sample.old;

import com.connection.SqlServerConnection;
import com.dataflow.ConnectionManager;
import com.dataflow.DataFlow;
import com.dataflow.components.DestinationInterface;
import com.dataflow.components.SourceInterface;
import com.dataflow.components.SqlServerSource;
import com.model.Column;
import com.model.SqlServer;
import com.services.ExecuteDataflow;
import com.services.SqlServerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.Session;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Index implements Initializable {
    @FXML
    private Label lblSourceName;
    @FXML
    private Label lblDestName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        DataFlow dataFlow = Session.getDataFlow();
        ConnectionManager connectionManager = dataFlow.getConnectionManager();
        lblSourceName.setText(connectionManager.getRefIdOfSource());
        lblDestName.setText(connectionManager.getRefIdOfDestination());
    }

    @FXML
    private void execute() {
        SourceInterface source = Session.getDataFlow().getExecutables().getPineline().getComponents().getSource();
        if (source instanceof SqlServerSource) {
            SqlServer sqlServer = Session.getDataFlow().getConnectionManager().getSqlServerManager().getSqlServer();
            SqlServerConnection sqlServerConnection = new SqlServerConnection(sqlServer.getHostname(), sqlServer.getUsername(),
                    sqlServer.getPassword());
            sqlServerConnection.setDatabaseName(sqlServer.getDatabase());
            try {
                Connection connection = sqlServerConnection.getConnection();
                SqlServerService sqlServerService = new SqlServerService(connection);
                List<String> listPrimaryKeys = sqlServerService.getPrimaryKey(((SqlServerSource) source).getTableName());
                List<Map<String, String>> listTableChanges = sqlServerService.getTableChange(((SqlServerSource) source).getTableName(), listPrimaryKeys);
                listTableChanges.forEach(change-> {
                    for (Map.Entry<String, String> entry : change.entrySet()) {
                        System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                    }
                    System.out.println("--");
                });

                DestinationInterface destinationInterface = Session.getDataFlow().getExecutables().getPineline().getComponents().getDestination();
                List<Column> listInputColumns = destinationInterface.getInputColumns();

                Map<String, Integer> mapLinearColumnName = new HashMap<>();
                System.out.println(listInputColumns.size());
                for (int i=0; i < listInputColumns.size(); i ++) {
                    System.out.println(listInputColumns.get(i).getLinearId());
                    String[] linearArr = listInputColumns.get(i).getLinearId().split("\\.");
                    String linearId = linearArr[linearArr.length - 1];
                    Pattern MY_PATTERN = Pattern.compile("\\[(.*?)\\]");
                    Matcher m = MY_PATTERN.matcher(linearId);
                    while (m.find()) {
                        String s = m.group(1);
                        mapLinearColumnName.put(s, i);
                    }
                }

                boolean isChangeTracking = true;
                for (int i =0; i < listPrimaryKeys.size(); i++) {
                    if (!mapLinearColumnName.containsKey(listPrimaryKeys.get(i))) {
                        isChangeTracking = false;
                    }
                }
                System.out.println(isChangeTracking);

                for (Map.Entry<String, Integer> entry : mapLinearColumnName.entrySet()) {
                    System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
                }
                System.out.println("--");


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*ExecuteDataflow executeDataflow = new ExecuteDataflow();
        executeDataflow.execute();*/
    }

    @FXML
    private void close() {
        Stage stage = (Stage) lblSourceName.getScene().getWindow();
        stage.close();
    }
}
