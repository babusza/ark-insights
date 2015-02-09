/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.aspect;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author TeleData
 */
public class Utility {

    public String connection;
    public String voice;
    final public String inputPath;
    final public String outputPath;
    final public Color bgColor;

    public Utility() throws Exception {
        this.bgColor = new Color(204, 255, 255);
        Properties prop = new Properties();
//                  String path = "file   :\\C:\\Program Files\\Ark-Insights Recording Player(on-site)\\app";
        String path = getClass().getResource("Utility.class").toString();
        path = path.replaceAll("%20", " ");
        path = path.substring(9, path.indexOf("Ark-Insights Recording Player(on-site)")) + "Ark-Insights Recording Player(on-site)\\";
        InputStream input = new FileInputStream(path + "\\app\\database.properties");
        prop.load(input);
        connection = prop.getProperty("connection");
        voice = prop.getProperty("voiceserver");
        inputPath = System.getProperty("java.io.tmpdir") + "/YPRISVGMUPO/";
        outputPath = System.getProperty("java.io.tmpdir") + "/YPRISVGMUPO/";

        File customDir = new File(inputPath);

        if (customDir.exists()) {
            System.out.println(customDir + " already exists");
        } else if (customDir.mkdirs()) {
            System.out.println(customDir + " was created");
        } else {
            System.out.println(customDir + " was not created");
        }

    }

    public String[] getPermission(String userID, String database) throws Exception {
        String[] permission = null;
        String command = "Sel_VoicePermission @UserID='" + userID + "'";
        ResultSet rs = getData(command, database);
        System.out.println(rs);
        while (rs.next()) {
            return new String[]{rs.getString("Play"), rs.getString("Export")};
        }
        return permission;
    }

    public Vector getService(String database) throws Exception {
        Vector service = new Vector();
        ResultSet rs = getData("Sel_Service @WorkGroup=1", database);
        while (rs.next()) {
            service.add(new String[]{rs.getString("id"), rs.getString("name")});
        }
        return service;
    }

    public boolean seveExport(String userID, int trackID, String database) throws Exception {
        String command = "Save_ExportHistory @UserID='" + userID + "', @TrackID=" + trackID;
        return saveData(command, database);
    }

    public String[] getUserAspect(String userID, String database) throws Exception {
        String[] userType = null;
        String command = "Sel_UserAspect @UserID='" + userID + "'";
        ResultSet rs = getData(command, database);
        while (rs.next()) {
            return new String[]{rs.getString("UserID"), rs.getString("WorkGroupID"), rs.getString("WorkGroupName")};
        }
        return userType;
    }

    public Vector getAgents(String database, int workgroup) throws Exception {
        Vector agents = new Vector();
        ResultSet rs = getData("Sel_Agent @WorkGroup=" + workgroup, database);
        while (rs.next()) {
            agents.add(rs.getString("UserID"));
        }
        return agents;
    }

    public Vector getDispostion(String database) throws Exception {
        Vector dispostion = new Vector();
        ResultSet rs = getData("Sel_Dispostion", database);
        while (rs.next()) {
            dispostion.add(new String[]{rs.getString("id"), rs.getString("code"), rs.getString("name")});
        }
        return dispostion;
    }

    public Vector getSupervisorByGroupID(String database, String userID) throws Exception {
        Vector vWorkGroup = new Vector();
        ResultSet rs = getData("Sel_SupervisorByGroupID @UserID='" + userID + "'", database);
        while (rs.next()) {
            vWorkGroup.add(new String[]{rs.getString("WorkGroupID"), rs.getString("WorkGroupName")});
        }
        return vWorkGroup;
    }

    public static ResultSet getRecord(Object[] para, String database) throws Exception {
        Object[] obj = para;
        String startDate = (String) para[0];
        String endDate = (String) para[1];
        Integer serviceID = (Integer) para[2];
        String agent = (String) para[3];
        String phoneNumber = (String) para[4];
        String customerName = (String) para[5];
        Integer called = (Integer) para[6];
        Integer mobile = (Integer) para[7];
        Integer home = (Integer) para[8];
        Integer office = (Integer) para[9];
        String callRefID = (String) para[10];
        String userID = (String) para[11];
        Integer trackID = (Integer) para[12];
        String customerID = (String) para[13];
        Integer dispostion = (Integer) para[14];
        Integer recordSec = (Integer) para[15];
        String recordFact = (String) para[16];
        Integer workGroupID = (Integer) para[17];
        Integer lastTrack = (Integer) para[18];
        Integer flageBat = (Integer) para[19];
        Integer searchFlage = (Integer) para[20];
        String command = "Sel_VoiceRecord "
                + "@StartDate='" + startDate
                + "', @EndDate='" + endDate
                + "', @ServiceID=" + serviceID
                + ", @Agent='" + agent
                + "', @PhoneNo='" + phoneNumber
                + "', @CustomerName='" + customerName
                + "', @Called=" + called
                + ", @Mobile=" + mobile
                + ", @Home=" + home
                + ", @Office=" + office
                + ", @CallRefID='" + callRefID
                + "', @UserID='" + userID
                + "', @TrackID=" + trackID
                + ", @CustomerID='" + customerID
                + "', @Disposition=" + dispostion
                + ", @RecordingSec=" + recordSec
                + ", @SecFact='" + recordFact
                + "', @WorkGroup=" + workGroupID
                + ", @TrackShow=" + lastTrack
                + ", @FlageBat=" + flageBat
                + ", @SearchFlage=" + searchFlage;

        Utility utility = new Utility();
        ResultSet rs = utility.getData(command, database);
        return rs;

    }

    public ResultSet getData(String command, String database) throws Exception {
        //  String connection = "jdbc:sqlserver://172.16.20.30;user=voiceenc;password=p@ssw0rd;";
        System.out.println("command : " + command);
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connection + "database=" + database);
        } catch (Exception e) {
            throw new Exception("Can not connect database");
        }
        Statement sta = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = sta.executeQuery(command);
        return rs;
    }

    public boolean saveData(String command, String database) throws Exception {
        // String connection = "jdbc:sqlserver://172.16.20.30;user=voiceenc;password=p@ssw0rd;";
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connection + "database=" + database);
        } catch (Exception e) {
            throw new Exception("Can not connect database");
        }
        Statement sta = conn.createStatement();
        return sta.execute(command);
    }

}
