package com.xml;

import com.dataflow.ConnectionManager;
import com.dataflow.DataFlow;
import com.model.Column;
import com.model.Excel;
import com.model.MySql;
import com.model.SqlServer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XmlHelper {
    public static Excel readExcelXml(String filePath) {
        File xmlFile = new File(filePath);
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(Excel.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Excel excel = (Excel) jaxbUnmarshaller.unmarshal(xmlFile);
            return excel;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void SqlServerToXml(SqlServer sqlServer) {
        File file = new File("config/sql_server_temp.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SqlServer.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(sqlServer, file);
        } catch (JAXBException e) {
            System.out.println("Loi khoi tao");
            e.printStackTrace();
        }
    }

    public static SqlServer XmlToSqlServer(String filePath) {
        File sqlFile = new File(filePath);
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(SqlServer.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            SqlServer sqlServer = (SqlServer) jaxbUnmarshaller.unmarshal(sqlFile);
            return sqlServer;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void Mysql2Xml(MySql mySql) {
        File file = new File("config/mysql_temp.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MySql.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(mySql, file);
        } catch (JAXBException e) {
            System.out.println("Loi khoi tao");
            e.printStackTrace();
        }
    }

    public static MySql Xml2Mysql(String filePath) {
        File sqlFile = new File(filePath);
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(MySql.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            MySql mySql = (MySql) jaxbUnmarshaller.unmarshal(sqlFile);
            return mySql;
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void Dataflow2Xml(DataFlow dataFlow) {
        File file = new File("config/dataflow.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DataFlow.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(dataFlow, file);
        } catch (JAXBException e) {
            System.out.println("Loi khoi tao");
            e.printStackTrace();
        }
    }

    public static void excelTemp2Xml(Excel excel) {
        File file = new File("config/excel_temp.xml");
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Excel.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(excel, file);
        } catch (JAXBException e) {
            System.out.println("Loi khoi tao");
            e.printStackTrace();
        }
    }

    public static void saveDataFlow(DataFlow dataFlow, String path) {
        File file = new File(path);
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(DataFlow.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(dataFlow, file);
        } catch (JAXBException e) {
            System.out.println("Loi khoi tao");
            e.printStackTrace();
        }
    }

    public static DataFlow xml2DataFlow(String filePath) {
        File sqlFile = new File(filePath);
        JAXBContext jaxbContext;
        try
        {
            jaxbContext = JAXBContext.newInstance(DataFlow.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (DataFlow) jaxbUnmarshaller.unmarshal(sqlFile);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
