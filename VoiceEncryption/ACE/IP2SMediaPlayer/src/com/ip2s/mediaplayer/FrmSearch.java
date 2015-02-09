/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.text.DateFormatter;

/**
 *
 * @author ark-insingths
 */
public class FrmSearch extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    /**
     * Creates new form FrmSearch
     */
    JSpinner spStartTime;
    JSpinner spEndTime;
    JSpinner spVoice;
    JTable tbDetail;

    public FrmSearch() {
        initComponents();
        this.setExtendedState(FrmSearch.MAXIMIZED_BOTH);
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        this.getContentPane().setBackground(new java.awt.Color(204, 255, 255));
        dfStartDate.setBackground(new java.awt.Color(204, 255, 255));
        dfEndDate.setBackground(new java.awt.Color(204, 255, 255));
        initTime();
        initTable();
    }

    private void initTable() {
        tbDetail = new JTable();
        tbDetail.setAutoCreateRowSorter(true);
        tbDetail.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "No", "Date Time", "Time", "Agent", "Disposition",
                    "Customer ID", "CustomerName", "Called Number",
                    "Mobile", "Home", "Office",
                    "ReferenceID", "Service", "Track ID",}
        ) {
            @Override
            public Class getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 2) {
                    return Integer.class;
                } else {
                    return String.class;
                }
            }

            public boolean isCellEditable(int row, int column) {
                return false;
            }
        }
        );
        tbDetail.setAutoResizeMode(0);

        tbDetail.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbDetail.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbDetail.getColumnModel().getColumn(2).setPreferredWidth(70);
        tbDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(4).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(5).setPreferredWidth(150);
        tbDetail.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(7).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(8).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(9).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(10).setPreferredWidth(80);
        tbDetail.getColumnModel().getColumn(11).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(12).setPreferredWidth(150);
        tbDetail.getColumnModel().getColumn(13).setPreferredWidth(150);

        this.add(tbDetail).setBounds(50, 280, (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 150), this.getHeight() - 700);
    }

    private void initTime() {

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
        this.add(spStartTime).setBounds(340, 30, 80, 25);

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
        this.add(spEndTime).setBounds(340, 70, 80, 25);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 00); // 24 == 12 PM == 00:00:00
        calendar2.set(Calendar.MINUTE, 00);
        calendar2.set(Calendar.SECOND, 00);

        SpinnerDateModel model2 = new SpinnerDateModel();
        model2.setValue(calendar2.getTime());
        spVoice = new JSpinner(model2);
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spVoice, "HH:mm:ss");
        DateFormatter formatter2 = (DateFormatter) editor2.getTextField().getFormatter();
        formatter2.setAllowsInvalid(false); // this makes what you want
        formatter2.setOverwriteMode(true);
        spVoice.setEditor(editor2);
        this.add(spVoice).setBounds(650, 110, 80, 25);

        editor.setBackground(new java.awt.Color(204, 255, 255));
        editor1.setBackground(new java.awt.Color(204, 255, 255));
        editor2.setBackground(new java.awt.Color(204, 255, 255));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdgVoiceTime = new javax.swing.ButtonGroup();
        lbStartDate = new javax.swing.JLabel();
        lbStartTime = new javax.swing.JLabel();
        lbEndDate = new javax.swing.JLabel();
        lbEndTime = new javax.swing.JLabel();
        lbDispostion = new javax.swing.JLabel();
        lbWorkGroup = new javax.swing.JLabel();
        lbAgent = new javax.swing.JLabel();
        lbService = new javax.swing.JLabel();
        lbReferenceID = new javax.swing.JLabel();
        lbCustomerID = new javax.swing.JLabel();
        lbCalledNumber = new javax.swing.JLabel();
        lbTime = new javax.swing.JLabel();
        rdMord = new javax.swing.JRadioButton();
        rdLess = new javax.swing.JRadioButton();
        lbTrackID = new javax.swing.JLabel();
        dfStartDate = new datechooser.beans.DateChooserCombo();
        dfEndDate = new datechooser.beans.DateChooserCombo();
        tfCustomerID = new javax.swing.JTextField();
        tfReferenceID = new javax.swing.JTextField();
        tfCalledNumber = new javax.swing.JTextField();
        tfTrackID = new javax.swing.JTextField();
        cbDisposition = new javax.swing.JComboBox();
        cbWorkGroup = new javax.swing.JComboBox();
        cbAgent = new javax.swing.JComboBox();
        cbService = new javax.swing.JComboBox();
        btSearch = new javax.swing.JButton();
        cbClear = new javax.swing.JButton();
        btExport = new javax.swing.JButton();
        lbSpeed = new javax.swing.JLabel();
        cbSpeeds = new javax.swing.JComboBox();
        btPlay = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ark-Insights Recording Player");
        setMinimumSize(new java.awt.Dimension(860, 1024));
        getContentPane().setLayout(null);

        lbStartDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbStartDate.setText("Start Date");
        getContentPane().add(lbStartDate);
        lbStartDate.setBounds(30, 30, 71, 25);

        lbStartTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbStartTime.setText("Start Time");
        getContentPane().add(lbStartTime);
        lbStartTime.setBounds(260, 30, 72, 25);

        lbEndDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbEndDate.setText("End Date");
        getContentPane().add(lbEndDate);
        lbEndDate.setBounds(30, 70, 63, 25);

        lbEndTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbEndTime.setText("End Time");
        getContentPane().add(lbEndTime);
        lbEndTime.setBounds(260, 70, 64, 25);

        lbDispostion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbDispostion.setText("Dispostion");
        getContentPane().add(lbDispostion);
        lbDispostion.setBounds(30, 110, 72, 25);

        lbWorkGroup.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbWorkGroup.setText("WorkGroup");
        getContentPane().add(lbWorkGroup);
        lbWorkGroup.setBounds(30, 150, 80, 25);

        lbAgent.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbAgent.setText("Agent");
        getContentPane().add(lbAgent);
        lbAgent.setBounds(30, 190, 42, 25);

        lbService.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbService.setText("Service");
        getContentPane().add(lbService);
        lbService.setBounds(30, 230, 49, 25);

        lbReferenceID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbReferenceID.setText("Reference ID");
        getContentPane().add(lbReferenceID);
        lbReferenceID.setBounds(670, 30, 90, 25);

        lbCustomerID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbCustomerID.setText("Customer ID");
        getContentPane().add(lbCustomerID);
        lbCustomerID.setBounds(440, 30, 89, 25);

        lbCalledNumber.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbCalledNumber.setText("Called Number");
        getContentPane().add(lbCalledNumber);
        lbCalledNumber.setBounds(440, 70, 101, 20);

        lbTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbTime.setText("Recorded Time(HH:MM:SS)");
        getContentPane().add(lbTime);
        lbTime.setBounds(440, 110, 189, 25);

        rdgVoiceTime.add(rdMord);
        rdMord.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rdMord.setSelected(true);
        rdMord.setText("More Than");
        getContentPane().add(rdMord);
        rdMord.setBounds(750, 110, 99, 25);

        rdgVoiceTime.add(rdLess);
        rdLess.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        rdLess.setText("Less Than");
        getContentPane().add(rdLess);
        rdLess.setBounds(850, 110, 95, 25);

        lbTrackID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbTrackID.setText("Track ID");
        getContentPane().add(lbTrackID);
        lbTrackID.setBounds(670, 70, 59, 25);

        dfStartDate.setCalendarBackground(new java.awt.Color(204, 255, 255));
        dfStartDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        getContentPane().add(dfStartDate);
        dfStartDate.setBounds(120, 30, 120, 25);

        dfEndDate.setCalendarBackground(new java.awt.Color(204, 255, 255));
        dfEndDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
        getContentPane().add(dfEndDate);
        dfEndDate.setBounds(120, 70, 120, 25);

        tfCustomerID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(tfCustomerID);
        tfCustomerID.setBounds(550, 30, 100, 25);

        tfReferenceID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(tfReferenceID);
        tfReferenceID.setBounds(780, 30, 100, 25);

        tfCalledNumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(tfCalledNumber);
        tfCalledNumber.setBounds(550, 70, 100, 25);

        tfTrackID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        getContentPane().add(tfTrackID);
        tfTrackID.setBounds(780, 70, 100, 25);

        cbDisposition.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbDisposition.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cbDisposition);
        cbDisposition.setBounds(120, 110, 300, 25);

        cbWorkGroup.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbWorkGroup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cbWorkGroup);
        cbWorkGroup.setBounds(120, 150, 300, 25);

        cbAgent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cbAgent);
        cbAgent.setBounds(120, 190, 300, 25);

        cbService.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbService.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cbService);
        cbService.setBounds(120, 230, 300, 25);

        btSearch.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        btSearch.setText("Search");
        getContentPane().add(btSearch);
        btSearch.setBounds(520, 150, 100, 25);

        cbClear.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        cbClear.setText("Clear");
        getContentPane().add(cbClear);
        cbClear.setBounds(640, 150, 100, 25);

        btExport.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        btExport.setText("Export");
        getContentPane().add(btExport);
        btExport.setBounds(760, 150, 100, 25);

        lbSpeed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbSpeed.setText("Speeds player");
        getContentPane().add(lbSpeed);
        lbSpeed.setBounds(520, 200, 110, 25);

        cbSpeeds.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cbSpeeds.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cbSpeeds);
        cbSpeeds.setBounds(650, 200, 70, 25);

        btPlay.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btPlay.setText("Play");
        getContentPane().add(btPlay);
        btPlay.setBounds(760, 200, 100, 25);

        setSize(new java.awt.Dimension(983, 612));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmSearch.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmSearch().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btExport;
    private javax.swing.JButton btPlay;
    private javax.swing.JButton btSearch;
    private javax.swing.JComboBox cbAgent;
    private javax.swing.JButton cbClear;
    private javax.swing.JComboBox cbDisposition;
    private javax.swing.JComboBox cbService;
    private javax.swing.JComboBox cbSpeeds;
    private javax.swing.JComboBox cbWorkGroup;
    private datechooser.beans.DateChooserCombo dfEndDate;
    private datechooser.beans.DateChooserCombo dfStartDate;
    private javax.swing.JLabel lbAgent;
    private javax.swing.JLabel lbCalledNumber;
    private javax.swing.JLabel lbCustomerID;
    private javax.swing.JLabel lbDispostion;
    private javax.swing.JLabel lbEndDate;
    private javax.swing.JLabel lbEndTime;
    private javax.swing.JLabel lbReferenceID;
    private javax.swing.JLabel lbService;
    private javax.swing.JLabel lbSpeed;
    private javax.swing.JLabel lbStartDate;
    private javax.swing.JLabel lbStartTime;
    private javax.swing.JLabel lbTime;
    private javax.swing.JLabel lbTrackID;
    private javax.swing.JLabel lbWorkGroup;
    private javax.swing.JRadioButton rdLess;
    private javax.swing.JRadioButton rdMord;
    private javax.swing.ButtonGroup rdgVoiceTime;
    private javax.swing.JTextField tfCalledNumber;
    private javax.swing.JTextField tfCustomerID;
    private javax.swing.JTextField tfReferenceID;
    private javax.swing.JTextField tfTrackID;
    // End of variables declaration//GEN-END:variables
}
