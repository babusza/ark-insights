/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

import com.ip2s.aspect.Disposition;
import com.ip2s.aspect.Service;
import com.ip2s.aspect.Tenant;
import com.ip2s.aspect.UserAspect;
import com.ip2s.aspect.Utility;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;

public class FrmFilter {

    private javax.swing.JButton btClear;
    private javax.swing.JButton btExport;
    private javax.swing.JButton btPlay;
    private javax.swing.JButton btSearch;
    private javax.swing.JComboBox cbAgent;
    private javax.swing.JComboBox cbDispostion;
    private javax.swing.JComboBox cbRate;
    private javax.swing.JComboBox cbServiceName;
    private javax.swing.JComboBox cbWorkGroup;
    private datechooser.beans.DateChooserCombo dfEndDate;
    private datechooser.beans.DateChooserCombo dfStartDate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbCustomerID;
    private javax.swing.JLabel lbDispostion;
    private javax.swing.JLabel lbEndDate;
    private javax.swing.JLabel lbEndTime;
    private javax.swing.JLabel lbRate;
    private javax.swing.JLabel lbReferenceID;
    private javax.swing.JLabel lbStartDate;
    private javax.swing.JLabel lbStartTime;
    private javax.swing.JLabel lbTimeSec;
    private javax.swing.JLabel lbWorkGroup;
    private javax.swing.ButtonGroup rdTimeVoice;
    private javax.swing.JRadioButton rdTimeVoiceLess;
    private javax.swing.JRadioButton rdTimeVoiceMore;
    private javax.swing.JTable tbDetail;
    private javax.swing.JTextField tfCallRefID;
    private javax.swing.JTextField tfCustomerID;
    private javax.swing.JFormattedTextField tfPhoneNumber;
    private javax.swing.JTextField tfTrackID;

    private static final Dimension MAIN_SIZE = new Dimension(900, 500);

    private JPanel mainPanel;
    private JPanel filterPanel;
    private DefaultTableModel tbModel;
    private JScrollPane jScrollPane0;
    JSpinner spStartTime;
    JSpinner spEndTime;
    JSpinner spVoice;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss aa");
    SimpleDateFormat voicTimeFormat = new SimpleDateFormat("hh:mm:ss");
    Tenant tenant;
    UserAspect userAspect;
    Disposition disposition;
    Service service;
    Vector vData;
    Utility utility;
    boolean enc;
    Date date = new Date();
    JFileChooser fcFile;
    IP2sMediaPlayer arkPlayer;

    public FrmFilter() {
        initComponents();
    }

    private void initComponents() {
        setupTable();
        jScrollPane0 = new JScrollPane(tbDetail);
         filterPanel = new JPanel();
//        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        filterPanel.add(Box.createHorizontalStrut(10));
        filterPanel.setBackground(new java.awt.Color(204, 255, 255));
        initFilter();

        int gap = 10;
        mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createEmptyBorder(gap, gap, gap, gap));
        mainPanel.setPreferredSize(MAIN_SIZE);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        mainPanel.add(jScrollPane0, BorderLayout.CENTER);
    }

    private void initFilter() {
        lbStartDate = new javax.swing.JLabel();
        lbStartDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbStartDate.setText("Start Date");
        lbStartDate.setName("lbStartData");
        filterPanel.add(lbStartDate);
     //   lbStartDate.setBounds(20, 20, 71, 25);
        lbStartDate.getAccessibleContext().setAccessibleName("");

        dfStartDate = new datechooser.beans.DateChooserCombo();
        dfStartDate.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
                new datechooser.view.appearance.ViewAppearance("custom",
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(0, 0, 255),
                                true,
                                true,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 255),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(128, 128, 128),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.LabelPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.LabelPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(255, 0, 0),
                                false,
                                false,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        (datechooser.view.BackRenderer) null,
                        false,
                        true)));

        dfStartDate.setCalendarBackground(new java.awt.Color(204, 255, 255));
        dfStartDate.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, null));
        dfStartDate.setNothingAllowed(false);
        dfStartDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        dfStartDate.setLocale(new java.util.Locale("en", "GB", ""));
        dfStartDate.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        filterPanel.add(dfStartDate);
      //  dfStartDate.setBounds(110, 20, 127, 25);

        lbStartTime = new javax.swing.JLabel();
        lbStartTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbStartTime.setText("Start Time ");
        lbStartTime.setName("lbStartTime"); // NOI18N
        filterPanel.add(lbStartTime);
       // lbStartTime.setBounds(250, 20, 76, 25);

        lbEndDate = new javax.swing.JLabel();
        lbEndDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbEndDate.setText("End Date");
        lbEndDate.setName("tfEndDate"); // NOI18N
        filterPanel.add(lbEndDate);
       // lbEndDate.setBounds(440, 20, 63, 25);
        lbEndDate.getAccessibleContext().setAccessibleName("EndDate");

        dfEndDate = new datechooser.beans.DateChooserCombo();
        dfEndDate.setCurrentView(new datechooser.view.appearance.AppearancesList("Light",
                new datechooser.view.appearance.ViewAppearance("custom",
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(0, 0, 255),
                                true,
                                true,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 255),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(128, 128, 128),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.LabelPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(0, 0, 255),
                                false,
                                true,
                                new datechooser.view.appearance.swing.LabelPainter()),
                        new datechooser.view.appearance.swing.SwingCellAppearance(new java.awt.Font("Tahoma", java.awt.Font.PLAIN, 11),
                                new java.awt.Color(0, 0, 0),
                                new java.awt.Color(255, 0, 0),
                                false,
                                false,
                                new datechooser.view.appearance.swing.ButtonPainter()),
                        (datechooser.view.BackRenderer) null,
                        false,
                        true)));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9); // 24 == 12 PM == 00:00:00
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());
        spStartTime = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spStartTime, "HH:mm:ss");
        DateFormatter formatter = (DateFormatter) editor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); // this makes what you want
        formatter.setOverwriteMode(true);
        spStartTime.setEditor(editor);
        filterPanel.add(spStartTime);

        dfEndDate.setNothingAllowed(false);
        dfEndDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        dfEndDate.setLocale(new java.util.Locale("en", "GB", ""));
        dfEndDate.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
        filterPanel.add(dfEndDate);
       // dfEndDate.setBounds(520, 20, 127, 25);

        lbEndTime = new javax.swing.JLabel();
        lbEndTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbEndTime.setText("End Time");
        lbEndTime.setName("lbEndTime"); // NOI18N
        filterPanel.add(lbEndTime);
       // lbEndTime.setBounds(660, 20, 64, 25);

//        lbDispostion = new javax.swing.JLabel();
//        lbDispostion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
//        lbDispostion.setText("Dispostion");
//        filterPanel.add(lbDispostion);
//       // lbDispostion.setBounds(20, 60, 72, 25);
//
//        cbDispostion = new javax.swing.JComboBox();
//        cbDispostion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
//        cbDispostion.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Item 1", "Item 2", "Item 3", "Item 4"}));
//        filterPanel.add(cbDispostion);
//       // cbDispostion.setBounds(130, 60, 260, 25);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 23); // 24 == 12 PM == 00:00:00
        calendar1.set(Calendar.MINUTE, 59);
        calendar1.set(Calendar.SECOND, 59);
        SpinnerDateModel model1 = new SpinnerDateModel();
        model1.setValue(calendar1.getTime());
        spEndTime = new JSpinner(model1);

        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spEndTime, "HH:mm:ss");
        DateFormatter formatter1 = (DateFormatter) editor1.getTextField().getFormatter();
        formatter1.setAllowsInvalid(false); // this makes what you want
        formatter1.setOverwriteMode(true);
        spEndTime.setEditor(editor1);
        filterPanel.add(spEndTime);
        

    }

    @SuppressWarnings("serial")
    private void setupTable() {
        tbDetail = new JTable();
        tbModel = new DefaultTableModel(new Object[][]{{null, null,
            null,},}, new String[]{
            "No", "Date Time", "Duration", "Agent", "Disposition",
            "Customer ID", "Called Number",
            "ReferenceID", "Service", "Track ID",}) {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 2) {
                    return Integer.class;
                } else {
                    return String.class;
                }
            }
        };
        tbDetail.setModel(tbModel);
        tbDetail.setAutoCreateRowSorter(true);
        tbDetail.setFillsViewportHeight(true);
    }

    public JComponent getMainPanel() {
        return mainPanel;
    }

    private static void createAndShowUI() {
        FrmFilter gcDisk = new FrmFilter();
        JFrame frame = new JFrame("Ark-Insights Recording Player");
        frame.getContentPane().add(gcDisk.getMainPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                createAndShowUI();
            }
        });
    }
}
