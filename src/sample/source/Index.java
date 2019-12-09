package sample.source;

import com.connection.ExcelConnection;
import com.connection.MysqlConnection;
import com.connection.SqlServerConnection;
import com.dataflow.*;
import com.dataflow.components.MySqlSource;
import com.dataflow.components.SqlServerSource;
import com.model.Column;
import com.model.Excel;
import com.model.MySql;
import com.model.SqlServer;
import com.services.MysqlService;
import com.services.SqlServerService;
import com.xml.XmlHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Session;

import javax.xml.bind.annotation.XmlElement;
import java.awt.peer.PanelPeer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Index implements Initializable {
    @FXML
    private Label hideLabel;
    @FXML
    private TextField txtFilePath;
    @FXML
    private ComboBox cbListSheetName;
    @FXML
    private CheckBox checkBox;
    @FXML
    private Pane buttonPane;

    @FXML
    private TextField ssHostnameTxt;
    @FXML
    private TextField ssUsernameTxt;
    @FXML
    private TextField ssDatabaseTxt;
    @FXML
    private PasswordField ssPasswordTxt;
    @FXML
    private ComboBox ssTableCb;

    @FXML
    private TextField myHostnameTxt;
    @FXML
    private TextField myUsernameTxt;
    @FXML
    private TextField myDatabaseTxt;
    @FXML
    private PasswordField myPasswordTxt;
    @FXML
    private ComboBox myTableCb;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int sheetIndex = cbListSheetName.getSelectionModel().getSelectedIndex();
        if (sheetIndex == -1) {
            buttonPane.setVisible(false);
        }
        ssTableCb.setVisible(false);
        myTableCb.setVisible(false);
    }

    public void closeStage() {
        Stage stage = (Stage) hideLabel.getScene().getWindow();
        stage.close();
    }

    /**
     * Excel section
     */
    @FXML
    private void selectExcelFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Excel (*.xlsx)",
                "*.xlsx");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String filePath = file.getPath();
            txtFilePath.setText(filePath);
            showComboSheet(filePath);
        }
    }

    private void showComboSheet(String filePath) {
        ExcelConnection excelConnection = new ExcelConnection(filePath);
        try {
            ArrayList<String> listSheetName = excelConnection.getListSheet();
            listSheetName.forEach(item -> cbListSheetName.getItems().add(item));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("File not found!");
            alert.show();
        }
    }

    @FXML
    private void showButtonPane() {
        buttonPane.setVisible(true);
    }

    @FXML
    public void showExcelColumnConfig() throws IOException {
        saveExcelToXml();
        Parent root = FXMLLoader.load(getClass().getResource("excel_column_config.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Source Configuration");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
        closeStage();
    }

    private void saveExcelToXml() {
        ConnectionManager connectionManager = new ConnectionManager();
        Excel excel = new Excel();
        excel.setFilePath(txtFilePath.getText());
        int sheetIndex = cbListSheetName.getSelectionModel().getSelectedIndex();
        boolean isFirstRow = checkBox.isSelected();
        excel.setSheetIndex(sheetIndex);
        excel.setFirstRow(isFirstRow);
        ExcelManager excelManager = new ExcelManager(excel);
        connectionManager.setExcelManager(excelManager);
        DataFlow dataFlow = Session.getDataFlow();
        dataFlow.setConnectionManager(null);
        dataFlow.setExecutables(null);
        dataFlow.setConnectionManager(connectionManager);
        XmlHelper.Dataflow2Xml(dataFlow);
    }

    /**
     * Sql server section
     */
    private boolean sqlServerValidate() {
        String hostName = ssHostnameTxt.getText();
        String userName = ssUsernameTxt.getText();
        String password = ssPasswordTxt.getText();
        String database = ssDatabaseTxt.getText();

        String errorContent = "";
        boolean isError = false;

        if (hostName.trim().isEmpty()) {
            isError = true;
            errorContent += "Hostname is required! ";
        }
        if (userName.trim().isEmpty()) {
            isError = true;
            errorContent += "Username is required! ";
        }
        if (password.trim().isEmpty()) {
            isError = true;
            errorContent += "Password is required! ";
        }
        if (database.trim().isEmpty()) {
            isError = true;
            errorContent += "Database is required";
        }

        if (isError) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(errorContent);
            alert.show();
        }
        return isError;
    }

    private Connection testSSConnection() {
        String hostName = ssHostnameTxt.getText().trim();
        String userName = ssUsernameTxt.getText().trim();
        String password = ssPasswordTxt.getText().trim();
        String database = ssDatabaseTxt.getText().trim();
        SqlServerConnection sqlServerConnection = new SqlServerConnection(hostName, userName, password);
        sqlServerConnection.setDatabaseName(database);
        Connection conn = null;
        try {
            conn = sqlServerConnection.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    private List<String> ssGetListTable() throws SQLException {
        Connection conn = testSSConnection();
        if (conn == null) {
            ssTableCb.setVisible(false);
            ssTableCb.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection failed!");
            alert.show();
            return null;
        }
        SqlServerService sqlServerService = new SqlServerService(conn);
        List<String> tableNames = sqlServerService.getListTableNames();
        return tableNames;
    }

    @FXML
    private void ssShowTableNameSelection() throws SQLException {
        if (sqlServerValidate()) {
            ssTableCb.setVisible(false);
            return;
        }

        List<String> listTableNames = ssGetListTable();
        listTableNames.forEach(name -> ssTableCb.getItems().add(name));
        ssTableCb.setVisible(true);
        ssTableCb.show();
    }

    @FXML
    private void ssHandleOk() throws SQLException {
        if (sqlServerValidate()) {
            ssTableCb.setVisible(false);
            return;
        }

        List<String> listTableNames = ssGetListTable();
        if (listTableNames == null) {
            return;
        }

        int tableIndex = ssTableCb.getSelectionModel().getSelectedIndex();
        if (tableIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please select table name");
            alert.show();
        }
        String tableName = (String) ssTableCb.getSelectionModel().getSelectedItem();
        System.out.println(tableName);
        if(!listTableNames.contains(tableName)) {
            return;
        }
        try {
            ssSaveSqlServerConfig(tableName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        closeStage();
    }

    private void ssSaveSqlServerConfig(String tableName) throws ClassNotFoundException {
        String hostName = ssHostnameTxt.getText().trim();
        String userName = ssUsernameTxt.getText().trim();
        String password = ssPasswordTxt.getText().trim();
        String database = ssDatabaseTxt.getText().trim();

        DataFlow dataFlow = Session.getDataFlow();
        dataFlow.refresh();

        ConnectionManager connectionManager = new ConnectionManager();
        SqlServer sqlServer = new SqlServer(hostName, userName, password, database);
        SqlServerManager sqlServerManager = new SqlServerManager(sqlServer);
        connectionManager.setSqlServerManager(sqlServerManager);
        dataFlow.setConnectionManager(connectionManager);

        SqlServerSource sqlServerSource = new SqlServerSource();
        sqlServerSource.setTableName(tableName);
        sqlServerSource.setConnectionManagerRefId(sqlServerManager.getRefId());

        SqlServerConnection sqlServerConnection = new SqlServerConnection(hostName, userName, password);
        sqlServerConnection.setDatabaseName(database);
        Connection conn = null;
        try {
            conn = sqlServerConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SqlServerService sqlServerService = new SqlServerService(conn);
        List<Column> outputColumns = null;
        try {
            outputColumns = sqlServerService.getListOutputColumns(tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        outputColumns.forEach(column -> {
            column.setId("DataFlow.SqlServerSource.Outputs[SqlServerSourceOutput].Columns["+ column.getName() +"]");
        });
        sqlServerSource.setOutputColumns(outputColumns);

        Components components = new Components();
        components.setSqlServerSource(sqlServerSource);
        Pineline pineline = new Pineline();
        pineline.setComponents(components);
        Executables executables = new Executables();
        executables.setPineline(pineline);
        dataFlow.setExecutables(executables);

        XmlHelper.Dataflow2Xml(dataFlow);
    }

    /**
     * Mysql section
     * */

    private boolean myValidate() {
        String hostName = myHostnameTxt.getText().trim();
        String userName = myUsernameTxt.getText().trim();
        String password = myPasswordTxt.getText().trim();
        String database = myDatabaseTxt.getText().trim();
        if (hostName.isEmpty() || userName.isEmpty() || password.isEmpty() || database.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill full!");
            alert.show();
            return false;
        }
        return true;
    }

    private Connection myTestConnection() {
        Connection connection;
        String hostName = myHostnameTxt.getText().trim();
        String userName = myUsernameTxt.getText().trim();
        String password = myPasswordTxt.getText().trim();
        String database = myDatabaseTxt.getText().trim();
        MysqlConnection mysqlConnection = new MysqlConnection(hostName, userName, password);
        mysqlConnection.setDatabaseName(database);
        try {
            connection = mysqlConnection.getConnection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return connection;
    }

    @FXML
    private void myShowTableSelected() throws SQLException, ClassNotFoundException {
        if (!myValidate()) {
            return;
        }

        String database = myDatabaseTxt.getText().trim();
        Connection connection = myTestConnection();
        if (connection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection failed!");
            alert.show();
            return;
        }
        myTableCb.setVisible(true);
        myTableCb.getItems().clear();

        MysqlService mysqlService = new MysqlService(connection);
        List<String> listTableNames = mysqlService.getListTableNames(database);
        listTableNames.forEach(table -> {
            myTableCb.getItems().add(table);
        });
    }

    @FXML
    private void myHandleOk() throws SQLException, ClassNotFoundException {
        Connection connection = myTestConnection();
        if (connection == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection failed!");
            alert.show();
            return;
        }

        int selectedIndex = myTableCb.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select table name!");
            alert.show();
            return;
        }

        String hostName = myHostnameTxt.getText().trim();
        String userName = myUsernameTxt.getText().trim();
        String password = myPasswordTxt.getText().trim();
        String database = myDatabaseTxt.getText().trim();

        DataFlow dataFlow = Session.getDataFlow();
        dataFlow.refresh();

        MySql mySql = new MySql(hostName, userName, password, database);
        MySqlManager mySqlManager = new MySqlManager(mySql);
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.setMySqlManager(mySqlManager);
        dataFlow.setConnectionManager(connectionManager);

        String tableName = (String) myTableCb.getSelectionModel().getSelectedItem();
        MySqlSource mySqlSource = new MySqlSource();
        mySqlSource.setTableName(tableName);
        mySqlSource.setConnectionManagerRefId(mySqlManager.getRefId());

        MysqlService mysqlService = new MysqlService(connection);
        List<Column> outputColumns  = mysqlService.getListOutputColumns(tableName, database);
        outputColumns.forEach(column -> {
            column.setId("DataFlow.MySqlSource.Outputs[MySqlSourceOutput].Columns["+ column.getName() +"]");
        });
        mySqlSource.setOutputColumns(outputColumns);
        Components components = new Components();
        components.setMySqlSource(mySqlSource);
        Pineline pineline = new Pineline();
        pineline.setComponents(components);
        Executables executables = new Executables();
        executables.setPineline(pineline);
        dataFlow.setExecutables(executables);

        XmlHelper.Dataflow2Xml(dataFlow);
        closeStage();
    }
}
