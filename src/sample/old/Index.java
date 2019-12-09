package sample.old;

import com.connection.SqlServerConnection;
import com.dataflow.ConnectionManager;
import com.dataflow.DataFlow;
import com.dataflow.components.SourceInterface;
import com.dataflow.components.SqlServerSource;
import com.model.SqlServer;
import com.services.ExecuteDataflow;
import com.services.SqlServerService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sample.Session;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

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
                listPrimaryKeys.forEach(key -> {
                    System.out.println(key);
                });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*ExecuteDataflow executeDataflow = new ExecuteDataflow();
        executeDataflow.execute();*/
    }
}