package sample.destination;

import com.connection.ExcelConnection;
import com.connection.MysqlConnection;
import com.connection.SqlServerConnection;
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
    private Pane buttonPane;
    @FXML
    private TextField ssHostnameTxt;
    @FXML
    private TextField ssUsernameTxt;
    @FXML
    private PasswordField ssPasswordTxt;
    @FXML
    private TextField ssDatabaseTxt;
    @FXML
    private ComboBox ssTableCb;

    @FXML
    private TextField myHostnameTxt;
    @FXML
    private TextField myUsernameTxt;
    @FXML
    private PasswordField myPasswordTxt;
    @FXML
    private TextField myDatabaseTxt;
    @FXML
    private ComboBox myTableCb;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int sheetIndex = cbListSheetName.getSelectionModel().getSelectedIndex();
        if (sheetIndex == -1) {
            buttonPane.setVisible(false);
        }
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
        buttonPane.setVisible(false);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Excel (*.xlsx)",
                "*.xlsx");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            String filePath = file.getPath();
            txtFilePath.setText(filePath);
            showComboSheet();
        }
    }
    @FXML
    private void showComboSheet() {
        String filePath = txtFilePath.getText();
        ExcelConnection excelConnection = new ExcelConnection(filePath);
        try {
            cbListSheetName.getItems().clear();
            ArrayList<String> listSheetName = excelConnection.getListSheet();
            listSheetName.forEach(item -> cbListSheetName.getItems().add(item));
            showButtonPane();
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
    private void showNewSheet() throws IOException {
        Excel excel = new Excel(txtFilePath.getText());
        XmlHelper.excelTemp2Xml(excel);

        Parent parent = FXMLLoader.load(getClass().getResource("new_sheet.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Sheet");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void showExcelColumnSelect() throws IOException {
        Excel excel = new Excel(txtFilePath.getText());
        int sheetIndex = cbListSheetName.getSelectionModel().getSelectedIndex();
        if (sheetIndex == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select one sheet!");
            alert.show();
            return;
        }
        excel.setSheetIndex(sheetIndex);
        XmlHelper.excelTemp2Xml(excel);
        Parent parent = FXMLLoader.load(getClass().getResource("excel_column_config.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Select columns to load");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        closeStage();
    }

    /**
     * Sql Server Section
     *
     * */
    private boolean sqlServerValidate() {
        return validate(ssHostnameTxt, ssUsernameTxt, ssPasswordTxt, ssDatabaseTxt);
    }

    private Connection testSSConnection() {
        String userName;
        userName = ssUsernameTxt.getText().trim();
        String hostName;
        hostName = ssHostnameTxt.getText().trim();
        String password = ssPasswordTxt.getText().trim();
        String database = ssDatabaseTxt.getText().trim();
        SqlServerConnection sqlServerConnection = new SqlServerConnection(hostName, userName, password);
        sqlServerConnection.setDatabaseName(database);
        Connection conn = null;
        try {
            conn = sqlServerConnection.getConnection();
        } catch (ClassNotFoundException e) {
            System.out.println("Class not Found");
        } catch (SQLException e) {
            System.out.println("Ket noi khong thanh cong");
        }
        return conn;
    }

    private List<String> ssGetListTable() throws SQLException {
        Connection conn = testSSConnection();
        if (conn == null) {
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
    private void ssShowTableNames() throws SQLException {
        if (!sqlServerValidate()) {
            ssTableCb.getItems().clear();
            return;
        }
        ssTableCb.getItems().clear();
        List<String> listTableNames = ssGetListTable();
        listTableNames.forEach(name -> ssTableCb.getItems().add(name));
        ssTableCb.show();
    }

    @FXML
    private void ssNewTable() throws IOException {
        if (!sqlServerValidate()) {
            return;
        }

        if (testSSConnection() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection failed!");
            alert.show();
            return;
        }

        ssSaveTempConfig(null);
        Parent parent = FXMLLoader.load(getClass().getResource("ss_new_table.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Table");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    @FXML
    private void ssShowMappings() throws SQLException {
        if (!sqlServerValidate()) {
            ssTableCb.getItems().clear();
            return;
        }

        if (ssGetListTable() == null) {
            return;
        }

        if (ssTableCb.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please select table name");
            alert.show();
            return;
        }
        String tableName = (String) ssTableCb.getSelectionModel().getSelectedItem();
        ssSaveTempConfig(tableName);
        try {
            ssShowMappingConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ssSaveTempConfig(String tableName) {
        String userName = ssUsernameTxt.getText().trim();
        String hostName = ssHostnameTxt.getText().trim();
        String password = ssPasswordTxt.getText().trim();
        String database = ssDatabaseTxt.getText().trim();

        SqlServer sqlServer = new SqlServer(hostName, userName, password, database);
        sqlServer.setTableName(tableName);
        XmlHelper.SqlServerToXml(sqlServer);
    }

    private void ssShowMappingConfig() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("ss_mapping_config.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Mappings");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        closeStage();
    }

    /**
     * Mysql section
     * */

    private boolean myValidate() {
        return validate(myHostnameTxt, myUsernameTxt, myPasswordTxt, myDatabaseTxt);
    }

    private boolean validate(TextField myHostnameTxt, TextField myUsernameTxt, PasswordField myPasswordTxt, TextField myDatabaseTxt) {
        String hostName = myHostnameTxt.getText();
        String userName = myUsernameTxt.getText();
        String password = myPasswordTxt.getText();
        String database = myDatabaseTxt.getText();

        if (hostName.trim().isEmpty() || userName.trim().isEmpty() ||
                password.trim().isEmpty() || database.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill full information");
            alert.show();
            return false;
        }
        return true;
    }

    private Connection myTestConnection() {
        String hostName = myHostnameTxt.getText().trim();
        String userName = myUsernameTxt.getText().trim();
        String password = myPasswordTxt.getText().trim();
        String database = myDatabaseTxt.getText().trim();
        MysqlConnection mysqlConnection = new MysqlConnection(hostName, userName, password);
        mysqlConnection.setDatabaseName(database);
        Connection connection;
        try {
            connection = mysqlConnection.getConnection();
        } catch (ClassNotFoundException e) {
            return null;
        } catch (SQLException e) {
            return null;
        }
        return connection;
    }

    private List<String> myGetListTable() throws SQLException {
        String database = myDatabaseTxt.getText().trim();
        Connection conn = myTestConnection();
        if (conn != null) {
            MysqlService mysqlService = new MysqlService(conn);
            List<String> tableNames = mysqlService.getListTableNames(database);
            return tableNames;
        } else {
            myTableCb.getItems().clear();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection failed!");
            alert.show();
            return null;
        }
    }

    @FXML
    private void myShowTableNames() throws SQLException {
        if (!myValidate()) {
            myTableCb.getItems().clear();
            return;
        }
        myTableCb.getItems().clear();
        List<String> listTableNames = myGetListTable();
        listTableNames.forEach(name -> myTableCb.getItems().add(name));
        myTableCb.show();
    }

    @FXML
    private void myShowMappings() throws SQLException {
        if (!myValidate()) {
            myTableCb.getItems().clear();
            return;
        }

        if (myGetListTable() == null) {
            return;
        }

        if (myTableCb.getSelectionModel().getSelectedIndex() == -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please select table name");
            alert.show();
            return;
        }
        String tableName = (String) myTableCb.getSelectionModel().getSelectedItem();
        mySaveTempConfig(tableName);
        try {
            myShowMappingConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void mySaveTempConfig(String tableName) {
        String userName = myUsernameTxt.getText().trim();
        String hostName = myHostnameTxt.getText().trim();
        String password = myPasswordTxt.getText().trim();
        String database = myDatabaseTxt.getText().trim();

        MySql mySql = new MySql(hostName, userName, password, database);
        mySql.setTableName(tableName);
        XmlHelper.Mysql2Xml(mySql);
    }

    private void myShowMappingConfig() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("my_mapping_config.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Mappings");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        closeStage();
    }

    @FXML
    private void myNewTable() throws IOException {
        if (!myValidate()) {
            return;
        }

        if (myTestConnection() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection failed!");
            alert.show();
            return;
        }

        mySaveTempConfig(null);
        Parent parent = FXMLLoader.load(getClass().getResource("my_new_table.fxml"));
        Stage stage = new Stage();
        stage.setTitle("New Table");
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}
