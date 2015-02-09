/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

import com.ip2s.aspect.Agent;
import com.ip2s.aspect.UserAspect;
import com.ip2s.aspect.Disposition;
import com.ip2s.aspect.Service;
import com.ip2s.aspect.Tenant;
import com.ip2s.aspect.Utility;
import com.ip2s.aspect.WorkGroup;
import ip2srsa.IP2sRSA;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DateFormatter;

/**
 *
 * @author TeleData
 */
public class FrmMediaPlayer extends javax.swing.JFrame {

    /**
     * Creates new form FrmMediaPlayer
     */
    JSpinner spStartTime;
    JSpinner spEndTime;
    JSpinner spVoice;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
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
    public String curTrack = "";
    ResultSet rs;
    int total;
    int page;
    JDialog dlgLoading;
    Object[] para;
    File lastDirectory;

    public FrmMediaPlayer(Tenant tenant, UserAspect userAspect) {
        try {
            utility = new Utility();
            System.out.println("FrmMediaplayer");
            fcFile = new JFileChooser();
            fcFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fcFile.setMultiSelectionEnabled(false);
            fcFile.setAcceptAllFileFilterUsed(false);
            setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
            System.out.println("setimg");
            disposition = new Disposition();
            System.out.println("disposition " + disposition);
            service = new Service();
            this.tenant = tenant;
            this.userAspect = userAspect;
            vData = new Vector();
            initComponents();
            this.setExtendedState(this.MAXIMIZED_BOTH);
            initTable();
            initData();
            initDate();
            initTime();
            initDialog();
            btPrevious.setVisible(false);
            btNext.setVisible(false);
            this.setVisible(true);
            try {
                File dir = new File(utility.outputPath);
                File[] f = dir.listFiles();
                for (File f1 : f) {
                    try {
                        f1.delete();
                    } catch (Exception e) {
                        f1.deleteOnExit();
                    }
                }
            } catch (Exception e) {
            }
            System.out.println("end delete");
            this.getContentPane().setBackground(new java.awt.Color(204, 255, 255));
            rdTimeVoiceMore.setSelected(true);
            dfStartDate.setBackground(new java.awt.Color(204, 255, 255));
            dfEndDate.setBackground(new java.awt.Color(204, 255, 255));
            btPlay.setEnabled(false);
            btExport.setVisible(false);
            String[] permission = utility.getPermission(userAspect.getUserID(), tenant.getDatabase());
            if (permission == null) {
                return;
            }
            if (permission[0].equals("Y")) {
                btPlay.setEnabled(true);
            }
            if (permission[1].equals("Y")) {
                btExport.setVisible(true);
            }
            arkPlayer = new IP2sMediaPlayer(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error : " + e.getMessage());
            System.exit(0);
        }
    }

    private void initDialog() {
        dlgLoading = new JDialog();
        dlgLoading.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        dlgLoading.setTitle("Please waiting");
        dlgLoading.setUndecorated(true);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new java.awt.Color(204, 255, 255));
        URL url = getClass().getResource("loader.gif");
        Icon icon = new ImageIcon(url);
        JLabel lbLoading = new JLabel(icon);
        panel.add(lbLoading, new GridBagConstraints());
        dlgLoading.getContentPane().add(panel);
        dlgLoading.setSize(300, 200);
        dlgLoading.setLocationRelativeTo(this);
        dlgLoading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dlgLoading.setModal(true);

    }

    private void initTable() {
        tbDetail.getColumnModel().getColumn(0).setPreferredWidth(50);
        tbDetail.getColumnModel().getColumn(1).setPreferredWidth(150);
        tbDetail.getColumnModel().getColumn(2).setPreferredWidth(80);
        tbDetail.getColumnModel().getColumn(3).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(4).setPreferredWidth(124);
        tbDetail.getColumnModel().getColumn(5).setPreferredWidth(80);
        tbDetail.getColumnModel().getColumn(6).setPreferredWidth(100);
        tbDetail.getColumnModel().getColumn(7).setPreferredWidth(80);
        tbDetail.getColumnModel().getColumn(8).setPreferredWidth(150);
        //     tbDetail.getColumnModel().getColumn(9).setPreferredWidth(100);
        alignRight(tbDetail, 0);
        alignRight(tbDetail, 2);
        alignRight(tbDetail, 9);

        tbDetail.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void alignRight(JTable table, int column) {
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.LEFT);
        table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);
    }

    public void initTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 24 == 12 PM == 00:00:00
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
        this.add(spStartTime).setBounds(335, 20, 80, 25);

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
        this.add(spEndTime).setBounds(740, 20, 80, 25);

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
        this.add(spVoice).setBounds(600, 140, 80, 25);

        editor.setBackground(new java.awt.Color(204, 255, 255));
        editor1.setBackground(new java.awt.Color(204, 255, 255));
        editor2.setBackground(new java.awt.Color(204, 255, 255));
    }

    public void initDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String sdate = sdf.format(date);
        String chkDate = sdate.substring(6);
        if (chkDate.compareTo("2500") >= 0) {
            int i = Integer.parseInt(chkDate);
            sdate = sdate.substring(0, 6) + (i - 543);
        }
        dfStartDate.setText(sdate);
        dfEndDate.setText(sdate);        
    }

    public void initData() throws Exception {
        cbWorkGroup.removeAllItems();
        if (userAspect.getWorkGrooup().size() > 1) {
            cbWorkGroup.addItem("All");
        }
        for (Object workGroup1 : userAspect.getWorkGrooup()) {
            WorkGroup workgroup = (WorkGroup) workGroup1;
            cbWorkGroup.addItem(workgroup.getWorkGroupName());
        }
        System.out.println("add wg");
        cbAgent.removeAllItems();
        List lAgent = userAspect.getAllAgent();
        if (lAgent.size() > 1) {
            cbAgent.addItem("All");
        }
        for (Object agent : lAgent) {
            cbAgent.addItem(((Agent) agent).getAgentName());
        }
        System.out.println("add ag");
        cbServiceName.removeAllItems();
        cbServiceName.addItem("All");
        Vector vService = utility.getService(tenant.getDatabase());
        for (Object service1 : vService) {
            String[] data = (String[]) service1;
            service.addService(data[0], data[1]);
            cbServiceName.addItem(data[1]);
        }
        System.out.println("add service");
        cbDispostion.removeAllItems();
        cbDispostion.addItem("All");
        Vector vDispostion = utility.getDispostion(tenant.getDatabase());
        for (Object vData1 : vDispostion) {
            String[] data = (String[]) vData1;
            cbDispostion.addItem(data[2]);
            disposition.setDisposition(data[0], data[2]);
        }
        System.out.println("add dis");
        cbRate.removeAllItems();
        float[] dRate = {1.0f, 1.2f, 1.5f, 1.7f, 2.0f, 2.2f};
        for (int i = 0; i < dRate.length; i++) {
            cbRate.addItem(dRate[i]);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdTimeVoice = new javax.swing.ButtonGroup();
        lbStartDate = new javax.swing.JLabel();
        lbStartTime = new javax.swing.JLabel();
        btSearch = new javax.swing.JButton();
        lbEndDate = new javax.swing.JLabel();
        lbEndTime = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbDetail = new javax.swing.JTable();
        btPlay = new javax.swing.JButton();
        btClear = new javax.swing.JButton();
        cbServiceName = new javax.swing.JComboBox();
        tfPhoneNumber = new javax.swing.JFormattedTextField();
        cbAgent = new javax.swing.JComboBox();
        lbTimeSec = new javax.swing.JLabel();
        rdTimeVoiceMore = new javax.swing.JRadioButton();
        rdTimeVoiceLess = new javax.swing.JRadioButton();
        cbDispostion = new javax.swing.JComboBox();
        lbDispostion = new javax.swing.JLabel();
        lbCustomerID = new javax.swing.JLabel();
        lbWorkGroup = new javax.swing.JLabel();
        cbWorkGroup = new javax.swing.JComboBox();
        cbRate = new javax.swing.JComboBox();
        lbRate = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lbReferenceID = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dfStartDate = new datechooser.beans.DateChooserCombo();
        dfEndDate = new datechooser.beans.DateChooserCombo();
        tfCallRefID = new javax.swing.JTextField();
        tfTrackID = new javax.swing.JTextField();
        tfCustomerID = new javax.swing.JTextField();
        btExport = new javax.swing.JButton();
        btNext = new javax.swing.JButton();
        btPrevious = new javax.swing.JButton();
        lbTotal = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ark-Insights Recording Player");
        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1020, 375));
        addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                formComponentRemoved(evt);
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentMoved(java.awt.event.ComponentEvent evt) {
                formComponentMoved(evt);
            }
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowStateListener(new java.awt.event.WindowStateListener() {
            public void windowStateChanged(java.awt.event.WindowEvent evt) {
                formWindowStateChanged(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowDeiconified(java.awt.event.WindowEvent evt) {
                formWindowDeiconified(evt);
            }
        });
        getContentPane().setLayout(null);

        lbStartDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbStartDate.setText("Start Date");
        lbStartDate.setName("lbStartData");
        getContentPane().add(lbStartDate);
        lbStartDate.setBounds(20, 20, 71, 25);
        lbStartDate.getAccessibleContext().setAccessibleName("");

        lbStartTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbStartTime.setText("Start Time ");
        lbStartTime.setName("lbStartTime"); // NOI18N
        getContentPane().add(lbStartTime);
        lbStartTime.setBounds(250, 20, 76, 25);

        btSearch.setBackground(new java.awt.Color(0, 153, 102));
        btSearch.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        btSearch.setForeground(new java.awt.Color(255, 255, 255));
        btSearch.setText("Search");
        btSearch.setName("btSearch"); // NOI18N
        btSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSearchActionPerformed(evt);
            }
        });
        getContentPane().add(btSearch);
        btSearch.setBounds(450, 180, 80, 25);

        lbEndDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbEndDate.setText("End Date");
        lbEndDate.setName("tfEndDate"); // NOI18N
        getContentPane().add(lbEndDate);
        lbEndDate.setBounds(440, 20, 63, 25);
        lbEndDate.getAccessibleContext().setAccessibleName("EndDate");

        lbEndTime.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbEndTime.setText("End Time");
        lbEndTime.setName("lbEndTime"); // NOI18N
        getContentPane().add(lbEndTime);
        lbEndTime.setBounds(660, 20, 64, 25);

        tbDetail.setAutoCreateRowSorter(true);
        tbDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No","Date Time", "Duration", "Agent","Disposition",
                "Customer ID", "Called Number",
                "ReferenceID", "Service", "Track ID",
            }
        )
        {
            @Override
            public Class getColumnClass(int columnIndex) {
                if(columnIndex==0 || columnIndex==2||columnIndex==9)
                return Integer.class;
                else
                return String.class;
            }
            public boolean isCellEditable(int row, int column){return false;}}
    );
    tbDetail.getTableHeader().setReorderingAllowed(false);
    tbDetail.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            tbDetailMouseClicked(evt);
        }
    });
    jScrollPane1.setViewportView(tbDetail);

    getContentPane().add(jScrollPane1);
    jScrollPane1.setBounds(20, 270, 1030, 310);

    btPlay.setBackground(new java.awt.Color(0, 153, 102));
    btPlay.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
    btPlay.setForeground(new java.awt.Color(255, 255, 255));
    btPlay.setText("Play");
    btPlay.setActionCommand("");
    btPlay.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btPlayActionPerformed(evt);
        }
    });
    getContentPane().add(btPlay);
    btPlay.setBounds(930, 170, 80, 40);

    btClear.setBackground(new java.awt.Color(255, 51, 51));
    btClear.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
    btClear.setForeground(new java.awt.Color(255, 255, 255));
    btClear.setText("Clear");
    btClear.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btClearActionPerformed(evt);
        }
    });
    getContentPane().add(btClear);
    btClear.setBounds(540, 180, 80, 25);

    cbServiceName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    cbServiceName.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    cbServiceName.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbServiceNameActionPerformed(evt);
        }
    });
    getContentPane().add(cbServiceName);
    cbServiceName.setBounds(130, 180, 260, 25);
    getContentPane().add(tfPhoneNumber);
    tfPhoneNumber.setBounds(560, 100, 100, 25);

    cbAgent.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    cbAgent.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    cbAgent.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbAgentActionPerformed(evt);
        }
    });
    getContentPane().add(cbAgent);
    cbAgent.setBounds(130, 140, 260, 25);

    lbTimeSec.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbTimeSec.setText("Duration (HH:MM:SS)");
    getContentPane().add(lbTimeSec);
    lbTimeSec.setBounds(440, 140, 160, 25);

    rdTimeVoice.add(rdTimeVoiceMore);
    rdTimeVoiceMore.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    rdTimeVoiceMore.setText("More than");
    rdTimeVoiceMore.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            rdTimeVoiceMoreActionPerformed(evt);
        }
    });
    getContentPane().add(rdTimeVoiceMore);
    rdTimeVoiceMore.setBounds(690, 140, 97, 25);

    rdTimeVoice.add(rdTimeVoiceLess);
    rdTimeVoiceLess.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    rdTimeVoiceLess.setText("Less than");
    getContentPane().add(rdTimeVoiceLess);
    rdTimeVoiceLess.setBounds(790, 140, 91, 25);

    cbDispostion.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    cbDispostion.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    getContentPane().add(cbDispostion);
    cbDispostion.setBounds(130, 60, 260, 25);

    lbDispostion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbDispostion.setText("Dispostion");
    getContentPane().add(lbDispostion);
    lbDispostion.setBounds(20, 60, 72, 25);

    lbCustomerID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbCustomerID.setText("Customer ID");
    getContentPane().add(lbCustomerID);
    lbCustomerID.setBounds(690, 60, 89, 25);

    lbWorkGroup.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbWorkGroup.setText("WorkGroup");
    getContentPane().add(lbWorkGroup);
    lbWorkGroup.setBounds(20, 100, 80, 25);

    cbWorkGroup.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    cbWorkGroup.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    cbWorkGroup.addItemListener(new java.awt.event.ItemListener() {
        public void itemStateChanged(java.awt.event.ItemEvent evt) {
            cbWorkGroupItemStateChanged(evt);
        }
    });
    cbWorkGroup.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            cbWorkGroupActionPerformed(evt);
        }
    });
    getContentPane().add(cbWorkGroup);
    cbWorkGroup.setBounds(130, 100, 260, 25);

    cbRate.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
    cbRate.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
    getContentPane().add(cbRate);
    cbRate.setBounds(840, 180, 68, 25);

    lbRate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbRate.setText("Speeds Player");
    getContentPane().add(lbRate);
    lbRate.setBounds(730, 180, 96, 25);

    jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel2.setText("Track ID");
    getContentPane().add(jLabel2);
    jLabel2.setBounds(690, 100, 70, 25);

    lbReferenceID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbReferenceID.setText("Reference ID");
    getContentPane().add(lbReferenceID);
    lbReferenceID.setBounds(440, 60, 90, 25);

    jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel1.setText("Called Number");
    getContentPane().add(jLabel1);
    jLabel1.setBounds(440, 100, 101, 25);
    jLabel1.getAccessibleContext().setAccessibleName("");

    jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel3.setText("Service Name");
    getContentPane().add(jLabel3);
    jLabel3.setBounds(20, 180, 93, 25);

    jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    jLabel4.setText("Agent");
    getContentPane().add(jLabel4);
    jLabel4.setBounds(20, 140, 42, 25);

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
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dfStartDate.setCalendarBackground(new java.awt.Color(204, 255, 255));
dfStartDate.setBorder(javax.swing.BorderFactory.createCompoundBorder(null,
    null));
    dfStartDate.setNothingAllowed(false);
    dfStartDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
    dfStartDate.setLocale(new java.util.Locale("en", "GB", ""));
    dfStartDate.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
    getContentPane().add(dfStartDate);
    dfStartDate.setBounds(110, 20, 127, 25);

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
            (datechooser.view.BackRenderer)null,
            false,
            true)));
dfEndDate.setNothingAllowed(false);
dfEndDate.setFieldFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
dfEndDate.setLocale(new java.util.Locale("en", "GB", ""));
dfEndDate.setBehavior(datechooser.model.multiple.MultyModelBehavior.SELECT_SINGLE);
getContentPane().add(dfEndDate);
dfEndDate.setBounds(520, 20, 127, 25);
getContentPane().add(tfCallRefID);
tfCallRefID.setBounds(560, 60, 100, 25);

tfTrackID.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyReleased(java.awt.event.KeyEvent evt) {
        tfTrackIDKeyReleased(evt);
    }
    });
    getContentPane().add(tfTrackID);
    tfTrackID.setBounds(790, 100, 100, 25);
    getContentPane().add(tfCustomerID);
    tfCustomerID.setBounds(790, 60, 100, 25);

    btExport.setBackground(new java.awt.Color(0, 153, 102));
    btExport.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
    btExport.setForeground(new java.awt.Color(255, 255, 255));
    btExport.setText("Export");
    btExport.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btExportActionPerformed(evt);
        }
    });
    getContentPane().add(btExport);
    btExport.setBounds(630, 180, 80, 25);

    btNext.setBackground(new java.awt.Color(0, 153, 102));
    btNext.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
    btNext.setForeground(new java.awt.Color(255, 255, 255));
    btNext.setText("Next Batch");
    btNext.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btNextActionPerformed(evt);
        }
    });
    getContentPane().add(btNext);
    btNext.setBounds(200, 230, 130, 25);

    btPrevious.setBackground(new java.awt.Color(0, 153, 102));
    btPrevious.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
    btPrevious.setForeground(new java.awt.Color(255, 255, 255));
    btPrevious.setText("Previous Batch");
    btPrevious.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btPreviousActionPerformed(evt);
        }
    });
    getContentPane().add(btPrevious);
    btPrevious.setBounds(40, 230, 150, 25);

    lbTotal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
    lbTotal.setForeground(new java.awt.Color(0, 153, 102));
    getContentPane().add(lbTotal);
    lbTotal.setBounds(340, 230, 540, 25);

    getAccessibleContext().setAccessibleDescription("");

    setSize(new java.awt.Dimension(1079, 639));
    setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSearchActionPerformed
        // TODO add your handling code here:        
        try {
            btPrevious.setVisible(false);
            btNext.setVisible(false);
            para = getPara("", 0, 0);

            Thread thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(300);
                        Object[] tPara = getPara("", 0, 1);
                        total = searchTotal(tPara);
                        lbTotal.setText("There are " + total + " recordings matching the filter criteria");
                        if (total > 500) {
                            btNext.setVisible(true);
                            btPrevious.setVisible(true);
                            btPrevious.setEnabled(false);
                            btNext.setEnabled(true);
                            setEditFilter(false);
                        }
                        page = 1;
                        searchData(para, 'S');
                    } catch (Exception ex) {
                       dlgLoading.dispose();
                        arkPlayer.setOntop(false);
                        arkPlayer.pause();
                        JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());                      
                    }
                    SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
                        public void run() {
                            dlgLoading.dispose();
                        }
                    });
                }
            };
            thread.start();
            dlgLoading.setVisible(true);
        } 
        catch (Exception e) {
            arkPlayer.setOntop(false);
             arkPlayer.pause();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_btSearchActionPerformed
    private String getDateFormat(String date) throws Exception {
        System.out.println("date : " + date);
        String[] s = date.split("/");
        for (int i = 0; i < s.length; i++) {
            if (s[i].length() == 1) {
                s[i] = "0" + s[i];
            }
        }
        s[2] = (Integer.parseInt(s[2]) + 2000) + "";
        return s[2] + "-" + s[1] + "-" + s[0];
    }

    private int searchTotal(Object[] para) throws Exception {

        rs = Utility.getRecord(para, tenant.getDatabase());
        while (rs.next()) {
            System.out.println("total : " + rs.getString("TOTAL"));
            return Integer.parseInt(rs.getString("TOTAL"));
        }
        return 0;
    }

    public Object[] getPara(String lastTrack, int flageBat, int searchFlage) throws Exception {
        String startDate = getDateFormat(dfStartDate.getText());
        //    String startTime = getTimeFormat(timeFormat.format(spStartTime.getValue()));
        String startTime = timeFormat.format(spStartTime.getValue());
        String endTime = timeFormat.format(spEndTime.getValue());
        startDate = startDate + " " + startTime;// + ".000";
        String endDate = getDateFormat(dfEndDate.getText());
        //   String endTime = getTimeFormat(timeFormat.format(spEndTime.getValue()));
        endDate = endDate + " " + endTime;//+ ".000";
        String chkStartDate = startDate.substring(0, 10).replace("-", "") + startTime.replace(":", "");
        String chkEndDate = endDate.substring(0, 10).replace("-", "") + endTime.replace(":", "");
        if (chkEndDate.compareTo(chkStartDate) < 0) {
            throw new Exception("Date Time Invalid");
        }
        Integer serviceID = 0;
        if (cbServiceName.getSelectedIndex() > 0) {
            String serviceName = (String) cbServiceName.getSelectedItem();
            serviceID = Integer.parseInt(service.getServiceID(serviceName));
        }
        String agent = "";
        if (cbAgent.getSelectedIndex() > 0) {
            agent = (String) cbAgent.getSelectedItem();
        }
        String phoneNo = tfPhoneNumber.getText().trim();

        String customerName = "";// tfCustomerName.getText().trim();
        Integer called = phoneNo.trim().length() > 0 ? 1 : 0;// cbCalledNumber.isSelected() ? 1 : 0;        
        Integer mobile = 0; //cbMobile.isSelected() ? 1 : 0;
        Integer home = 0;//cbHome.isSelected() ? 1 : 0;
        Integer office = 0;//= cbOffice.isSelected() ? 1 : 0;  */
        if (phoneNo.length() > 1) {
            if (phoneNo.length() < 4 || phoneNo.length() > 16) {
                throw new Exception("Called Number length Invalid");
            }
        }
        String callRefID = tfCallRefID.getText().trim();
        if (callRefID.trim().length() > 30) {
            // callRefID = callRefID.substring(0, 30);
            throw new Exception("Reference ID length invalid");
        }
        String userID = userAspect.getUserID();
        Integer trackID = null;
        if (tfTrackID.getText().trim().length() > 10) {
            throw new Exception("Track ID length invalid");
        }
        try {
            trackID = Integer.parseInt(tfTrackID.getText().length() == 0 ? "0" : tfTrackID.getText().trim());
        } catch (Exception e) {
            throw new Exception("Track ID invalid");
        }

        String customerID = tfCustomerID.getText().trim();
        if (customerID.trim().length() > 30) {
            throw new Exception("Customer length ID invalid");
        }
        Integer dispostionID = 0;
        if (cbDispostion.getSelectedIndex() > 0) {
            String dispostionName = (String) cbDispostion.getSelectedItem();
            dispostionID = Integer.parseInt(disposition.getDispositionID(dispostionName));
        }
        String voiceTime = voicTimeFormat.format(spVoice.getValue());
        int h = Integer.parseInt(voiceTime.substring(0, 2));
        if (h == 12) {
            h = 0;
        }
        int m = Integer.parseInt(voiceTime.substring(3, 5));
        m = m * 60;
        h = (h * 60) * 60;
        int s = Integer.parseInt(voiceTime.substring(6, 8));
        int total = m + h + s;
        Integer recordSec = total;
        String secFact = rdTimeVoiceMore.isSelected() ? "M" : "L";
        int workGroupID = 0;
        if (cbWorkGroup.getSelectedIndex() > 0) {
            String wgName = (String) cbWorkGroup.getSelectedItem();
            List list = userAspect.getWorkGrooup();
            for (Object ob : list) {
                WorkGroup wg = (WorkGroup) ob;
                if (wg.getWorkGroupName().equals(wgName)) {
                    workGroupID = wg.getWorkGroupID();
                    break;
                }
            }
        }
        Integer last = new Integer(0);
        if (lastTrack.length() > 0) {
            last = Integer.parseInt(lastTrack);
        }

        Integer fb = new Integer(flageBat);
        Integer sf = new Integer(searchFlage);
        return new Object[]{startDate, endDate, serviceID, agent, phoneNo, customerName, called, mobile, home, office, callRefID, userID, trackID, customerID, dispostionID, recordSec, secFact, workGroupID, last, fb, sf};
    }

    private void showData(char showFlage) throws Exception {
        if (rs == null) {
            throw new Exception("No Record");
        }
        while (tbDetail.getRowCount() > 0) {
            ((DefaultTableModel) tbDetail.getModel()).removeRow(0);
        }
        System.out.println("remove row");
        vData.removeAllElements();
        int count = 1;
        if (page > 1) {
            count = (page - 1) * 500;
            count++;
        }
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tbDetail.getModel());
        tbDetail.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(9, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();
        System.out.println("sorter");
        if (showFlage == 'P') {
            //   while (rs.next()) {
            for (boolean st = rs.last(); st; st = rs.previous()) {
                int tsecond = Integer.parseInt(rs.getString("RecordingSecs").substring(0, rs.getString("RecordingSecs").indexOf(".")));
                String sec = String.format("%02d:%02d:%02d", tsecond / 3600, (tsecond % 3600) / 60, tsecond % 60);
                Object[] data = new Object[]{
                    new Integer(count++),
                    rs.getString("CalledDate").substring(0, 19),
                    sec,
                    rs.getString("UserID"),
                    disposition.getDispositionName(rs.getString("DispositionID")),
                    rs.getString("Param1"),
                    rs.getString("PhoneNo"),
                    rs.getString("Param20"),
                    service.getServiceName(rs.getString("serviceID")),
                    new Integer(rs.getString("TrackID"))
                };
                ((DefaultTableModel) tbDetail.getModel()).addRow(data);
            }
        } else {
            while (rs.next()) {
                System.out.println("recordsec " + rs.getString("RecordingSecs"));
                int tsecond = Integer.parseInt(rs.getString("RecordingSecs").substring(0, rs.getString("RecordingSecs").indexOf(".")));
                System.out.println("tsecon : " + tsecond);
                String sec = String.format("%02d:%02d:%02d", tsecond / 3600, (tsecond % 3600) / 60, tsecond % 60);
                System.out.println("sec : " + sec);
                Object[] data = new Object[]{
                    new Integer(count++),
                    rs.getString("CalledDate").substring(0, 19),
                    sec,
                    rs.getString("UserID"),
                    disposition.getDispositionName(rs.getString("DispositionID")),
                    rs.getString("Param1"),
                    rs.getString("PhoneNo"),
                    rs.getString("Param20"),
                    service.getServiceName(rs.getString("serviceID")),
                    new Integer(rs.getString("TrackID"))
                };
                ((DefaultTableModel) tbDetail.getModel()).addRow(data);
            }
        }
        System.out.println("end showData");
    }

    public void searchData(Object[] para, char showFlage) throws Exception {
        rs = null;
        rs = Utility.getRecord(para, tenant.getDatabase());
        System.out.println("connected");
        showData(showFlage);
        if (tbDetail.getRowCount() == 0) {
            throw new Exception("No Record");
        }
    }

    private void btPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPlayActionPerformed
        // TODO add your handling code here:
        Thread t = new Thread() {
            public void run() {
                try {  
                    arkPlayer.setOntop(false);
                     arkPlayer.pause();
                    playMedia();                    
                } catch (Exception ex) {
                    dlgLoading.dispose();
                    arkPlayer.setOntop(false);
                     arkPlayer.pause();
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage());
                }
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        dlgLoading.dispose();
                    }
                });
            }
        };
        t.start();
        dlgLoading.setVisible(true);
    }//GEN-LAST:event_btPlayActionPerformed

    private void playMedia() {
        try {
            int row = tbDetail.getSelectedRow();
            System.out.println("row : " + row);
            if (row < 0) {
                throw new Exception("Please select row in table");
            }
            String trackID = tbDetail.getValueAt(row, 9).toString().trim();
            System.out.println("curTrack " + curTrack);
//            if (this.curTrack.equals(trackID)) {
//                arkPlayer.setOntop(false);
//                JOptionPane.showMessageDialog(this, "Voice : " + trackID + " is running");
//                arkPlayer.setOntop(true);
//                return;
//            }
            String time = tbDetail.getValueAt(row, 2).toString();
            if (time.equals("00:00:00")) {
                throw new Exception("No duration time");
            }
            String md5Name = getMedia(trackID, row);
            float rate = (float) cbRate.getSelectedItem();
            if (enc) {
                boolean dec = decrypt(md5Name);
            }
            arkPlayer.play(md5Name + ".temp", utility.outputPath, rate, trackID);
            curTrack = trackID;
            arkPlayer.setOntop(true);
        } catch (Exception e) {
            if (e.getMessage().trim().length() == 0) {
                System.out.println("Error");
            } else {
                arkPlayer.setOntop(false);
                 arkPlayer.pause();
                JOptionPane.showMessageDialog(this, e.getMessage());
            }
        }
    }

    private boolean checkConnection() {
        try {
            InetAddress add = InetAddress.getByName(utility.voice.replace("/", ""));
            return add.isReachable(2000);
        } catch (Exception e) {
            return false;
        }
    }

    private String getMedia(String trackID, int row) throws Exception {
        String pathVoice = getPath(trackID, row);
        Path path = FileSystems.getDefault().getPath(utility.voice + tenant.getVoicePath() + "\\" + pathVoice, trackID + ".enc");
        Path pathO = FileSystems.getDefault().getPath(utility.voice + tenant.getVoicePath() + "\\" + pathVoice, trackID);
        String md5Name = getMD5(trackID);
        String encryptPath = utility.outputPath + trackID;

        OutputStream out = null;
        enc = true;
        System.out.println(" be fore connected");

        if (!checkConnection()) {
            throw new Exception("Connection failed");
        }
        System.out.println("connected");
        boolean pathEnc = Files.exists(path);
        if (pathEnc) {
            out = new DataOutputStream(new FileOutputStream(utility.outputPath + md5Name));
            Files.copy(path, out);
        } else {
            boolean pathNenc = Files.exists(pathO);
            if (pathNenc) {
                out = new DataOutputStream(new FileOutputStream(utility.outputPath + md5Name + ".temp"));
                Files.copy(pathO, out);
                enc = false;
            } else {
                throw new Exception("File No Found");
            }
        }
        out.close();
        return md5Name;
    }

    private boolean decrypt(String fileName) throws Exception {
        IP2sRSA rsa = new IP2sRSA();
        rsa.setKeyGenerator();
        rsa.setSecretKey();
        rsa.getPrivateKey();
        rsa.setDataOutputDecrypt(utility.outputPath + fileName + ".temp");
        rsa.decrypt(utility.inputPath + fileName);
        return true;
    }

    private String getPath(String trackID, int row) throws Exception {
        String path = "";
        // Object[] obj = (Object[]) vData.get(row);
        String date = tbDetail.getValueAt(row, 1).toString();//(String) obj[1];
        path = date.substring(0, 7) + "//" + date.substring(0, 10) + "//";
        return path;
    }

    private String getMD5(String trackID) throws Exception {
        MessageDigest m = MessageDigest.getInstance("MD5");
        m.update(trackID.getBytes(), 0, trackID.length());
        String md5 = new BigInteger(1, m.digest()).toString(16);
        return md5;
    }

    private void btClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearActionPerformed
        // TODO add your handling code here:       
        try {
            setEditFilter(true);
            btPrevious.setVisible(false);
            btNext.setVisible(false);
            lbTotal.setText("");
            clearText();
            initData();
            initDate();
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 00); // 24 == 12 PM == 00:00:00
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            spVoice.setValue(calendar.getTime());
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 00); // 24 == 12 PM == 00:00:00
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            spStartTime.setValue(calendar.getTime());
            calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23); // 24 == 12 PM == 00:00:00
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            spEndTime.setValue(calendar.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btClearActionPerformed

    private void clearText() {
        tfCallRefID.setText("");
        tfCustomerID.setText("");
        tfPhoneNumber.setText("");
        rdTimeVoiceMore.setSelected(true);
        tfTrackID.setText("");
        while (tbDetail.getRowCount() > 0) {
            ((DefaultTableModel) tbDetail.getModel()).removeRow(0);
        }
    }
    private void cbAgentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbAgentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbAgentActionPerformed

    private void cbServiceNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbServiceNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbServiceNameActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        // TODO add your handling code here:
        try {
            File dir = new File(utility.outputPath);
            File[] f = dir.listFiles();
            for (File f1 : f) {
                try {
                    f1.delete();
                } catch (Exception e) {
                    f1.deleteOnExit();
                }
            }
        } catch (Exception e) {
            ;
        }
    }//GEN-LAST:event_formWindowClosed

    private void cbWorkGroupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbWorkGroupActionPerformed
        // TODO add your handling code here:
        String workgroupName = (String) cbWorkGroup.getSelectedItem();
        List workgroup = userAspect.getWorkGrooup();
        for (Object workgroup1 : workgroup) {
            WorkGroup wk = (WorkGroup) workgroup1;
            if (wk.getWorkGroupName().equals(workgroupName)) {
                cbAgent.removeAllItems();
                List lAgent = wk.getAgent();
                if (lAgent.size() > 1) {
                    cbAgent.addItem("All");
                }
                for (Object agent : lAgent) {
                    cbAgent.addItem(((Agent) agent).getAgentName());
                }
                break;
            }
        }
    }//GEN-LAST:event_cbWorkGroupActionPerformed

    private void tbDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDetailMouseClicked
        // TODO add your handling code here:
//        if (evt.getClickCount() == 2) {
//            Thread t = new Thread() {
//                public void run() {
//                    try {
//                        //Thread.sleep(300);
//                        playMedia();
//                    } catch (Exception ex) {
//                        dlgLoading.dispose();
//                        JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
//                    }
//                    SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
//                        public void run() {
//                            dlgLoading.dispose();
//                        }
//                    });
//                }
//            };
//            t.start();
//            dlgLoading.setVisible(true);
//        }
    }//GEN-LAST:event_tbDetailMouseClicked

    private void btExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExportActionPerformed
        // TODO add your handling code here:
        try {
            int row = tbDetail.getSelectedRow();
            if (row < 0) {
                throw new Exception("Please select row in table");
            }
            String t = tbDetail.getValueAt(row, 2).toString();
            System.out.println("time : " + t);
            if (t.equals("00:00:00")) {
                throw new Exception("No duration time");
            }
            String trackID = tbDetail.getValueAt(row, 9).toString();
            String md5Name = getMedia(trackID, row);
            String path = "C:\\Users\\" + System.getProperty("user.name") + "\\Documents\\";
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss_");
            String fileName = df.format(new Date());

            String pathVoice = getPath(trackID, row);
            Path pathN = FileSystems.getDefault().getPath(utility.voice + tenant.getVoicePath() + "\\" + pathVoice, trackID + ".enc");
            Path pathO = FileSystems.getDefault().getPath(utility.voice + tenant.getVoicePath() + "\\" + pathVoice, trackID);
            OutputStream outOri = new DataOutputStream(new FileOutputStream(utility.outputPath + md5Name));

            boolean pathEnc = Files.exists(pathN);
            if (pathEnc) {
                // Files.copy(pathN, outOri);
                IP2sRSA rsa = new IP2sRSA();
                rsa.setKeyGenerator();
                rsa.setSecretKey();
                rsa.getPrivateKey();
                rsa.setDataOutputDecrypt(utility.outputPath + md5Name);
                String p = utility.voice + tenant.getVoicePath() + "\\" + pathVoice + "\\" + trackID + ".enc";
                System.out.println("path : " + p);
                rsa.decrypt(p);
            } else {
                boolean pathNenc = Files.exists(pathO);
                if (pathNenc) {
                    Files.copy(pathO, outOri);
                } else {
                    throw new Exception("File No Found");
                }
            }
            KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
            pairgen.initialize(512, random);
            KeyPair keyPair = pairgen.generateKeyPair();

//            if (arkPlayer != null) {
            arkPlayer.setOntop(false);
             arkPlayer.pause();
//            }

            int returnVal = fcFile.showDialog(this, "Select Folder");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fcFile.getSelectedFile();
                path = file.getParent() + "\\" + file.getName() + "\\";
            } else {
//                if (arkPlayer != null) {
              //  arkPlayer.setOntop(true);
//                }
                return;
            }

            ExportRunnable myRunnable = new ExportRunnable(path, fileName, trackID, keyPair, md5Name);
            Thread thread = new Thread(myRunnable);
            thread.start();
            dlgLoading.setVisible(true);
            lastDirectory = fcFile.getSelectedFile();

        } catch (Exception e) {
//            if (arkPlayer == null) {
//                JOptionPane.showMessageDialog(this, e.getMessage());
//            } else {
            arkPlayer.setOntop(false);
             arkPlayer.pause();
            JOptionPane.showMessageDialog(this, e.getMessage());
          //  arkPlayer.setOntop(true);
//            }
        }
    }//GEN-LAST:event_btExportActionPerformed

    public class ExportRunnable implements Runnable {

        private String path;
        private String fileName;
        private String trackID;
        private KeyPair keyPair;
        private String md5Name;

        public ExportRunnable(String path, String fileName, String trackID, KeyPair keyPair, String md5Name) {
            this.path = path;
            this.fileName = fileName;
            this.trackID = trackID;
            this.keyPair = keyPair;
            this.md5Name = md5Name;
        }

        public void run() {
            try {
                Thread.sleep(300);
                utility.seveExport(userAspect.getUserID(), new Integer(trackID).intValue(), tenant.getDatabase());
                path += fileName + trackID;
                String keyName = path + ".key";
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(keyName));
                out.writeObject(keyPair.getPrivate());
                out.close();

                IP2sRSA rsa = new IP2sRSA();
                rsa.setKeyGenerator();
                rsa.setSecretKey();
                rsa.setPrivateKey(keyPair.getPublic());
                InputStream in = rsa.setFileOriginal(utility.outputPath + md5Name);
                String voiceName = path + ".enc";
                rsa.setDataOutputEncrypt(voiceName);
                rsa.encrypt();
                String message = "Export Complete\n" + keyName + "\n" + voiceName;
                dlgLoading.dispose();
//                if (arkPlayer == null) {
//                    JOptionPane.showMessageDialog(dlgLoading, message);
//                } else {
                arkPlayer.setOntop(false);
                 arkPlayer.pause();
                JOptionPane.showMessageDialog(dlgLoading, message);
            //    arkPlayer.setOntop(true);
//                }

            } catch (Exception ex) {
                dlgLoading.dispose();
//                if (arkPlayer == null) {
//                    JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
//                } else {
                arkPlayer.setOntop(false);
                 arkPlayer.pause();
                JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
             //   arkPlayer.setOntop(true);
//                }
            }
            SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
                public void run() {
                    dlgLoading.dispose();
                }
            });
        }

    }


    private void rdTimeVoiceMoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdTimeVoiceMoreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdTimeVoiceMoreActionPerformed

    private void tfTrackIDKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTrackIDKeyReleased
        // TODO add your handling code here:
//        int key = evt.getKeyCode();     
//        if (key < 48 || key > 57) {
//            tfTrackID.setText(tfTrackID.getText().substring(0, tfTrackID.getText().length() - 1));
//        }
    }//GEN-LAST:event_tfTrackIDKeyReleased

    private void formWindowStateChanged(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowStateChanged
        // TODO add your handling code here:
        jScrollPane1.setBounds(30, 270, this.getWidth() - 110, this.getHeight() - 300);
    }//GEN-LAST:event_formWindowStateChanged

    private void formWindowDeiconified(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowDeiconified
        // TODO add your handling code here:
        jScrollPane1.setBounds(30, 270, this.getWidth() - 80, this.getHeight() - 330);
    }//GEN-LAST:event_formWindowDeiconified

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // TODO add your handling code here:
        jScrollPane1.setBounds(30, 270, this.getWidth() - 80, this.getHeight() - 330);
    }//GEN-LAST:event_formComponentResized

    private void formComponentMoved(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentMoved
        // TODO add your handling code here:
        jScrollPane1.setBounds(30, 270, this.getWidth() - 80, this.getHeight() - 330);
    }//GEN-LAST:event_formComponentMoved

    private void formComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_formComponentRemoved
        // TODO add your handling code here:
        jScrollPane1.setBounds(30, 270, this.getWidth() - 80, this.getHeight() - 330);
    }//GEN-LAST:event_formComponentRemoved

    private void btNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNextActionPerformed
        // TODO add your handling code here:
        btNext.setEnabled(false);
        page++;
        if (page * 500 > total) {
            btNext.setEnabled(false);
        }
        btPrevious.setEnabled(true);
        int row = tbDetail.getRowCount() - 1;
//        
//        
        String trackID = tbDetail.getModel().getValueAt(row, 9).toString();
        //   System.out.println(""+tbDetail.getModel().getValueAt(tbDetail.getRowCount()-1, 9));
        try {
            //     System.out.println("next track : " + trackID);
            para[18] = new Integer(trackID);
            para[19] = new Integer(1);
            para[20] = new Integer(0);
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(300);
                        searchData(para, 'N');
                        if (tbDetail.getRowCount() < 500) {
                            btNext.setVisible(true);
                            btNext.setEnabled(false);
                        } else {
                            btNext.setEnabled(true);
                        }
                    } catch (Exception ex) {

                        dlgLoading.dispose();
//                        if (arkPlayer == null) {
//                            JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
//                        } else {
                        arkPlayer.setOntop(false);
                         arkPlayer.pause();
                        JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
                      //  arkPlayer.setOntop(true);
//                        }

                    }
                    SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
                        public void run() {
                            dlgLoading.dispose();
                        }
                    });
                }
            };
            t.start();
            dlgLoading.setVisible(true);

        } catch (Exception e) {
//            if (arkPlayer == null) {
//                JOptionPane.showMessageDialog(this, e.getMessage());
//            } else {
            arkPlayer.setOntop(false);
             arkPlayer.pause();
            JOptionPane.showMessageDialog(this, e.getMessage());
      //      arkPlayer.setOntop(true);
//            }
        }
    }//GEN-LAST:event_btNextActionPerformed

    private void btPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPreviousActionPerformed
        // TODO add your handling code here:
        btPrevious.setEnabled(false);
        btNext.setEnabled(true);
        page--;
        String trackID = tbDetail.getModel().getValueAt(0, 9).toString();
        try {
            // Object[] para = getPara(trackID, -1, 0);
            para[18] = new Integer(trackID);
            para[19] = new Integer(-1);
            para[20] = new Integer(0);
            Thread t = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(300);
                        searchData(para, 'P');
                    } catch (Exception ex) {
                        dlgLoading.dispose();
//                        if (arkPlayer == null) {
//                            JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
//                        } else {
                        arkPlayer.setOntop(false);
                         arkPlayer.pause();
                        JOptionPane.showMessageDialog(dlgLoading, ex.getMessage());
                      //  arkPlayer.setOntop(true);
//                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
                        public void run() {
                            dlgLoading.dispose();
                        }
                    });
                }
            };
            t.start();
            dlgLoading.setVisible(true);
            int count = 1;
            if (page > 1) {
                count = (page - 1) * 500;
                count++;
            }
            int row = tbDetail.getRowCount();
            for (int i = 0; i < row; i++) {
                tbDetail.setValueAt(count++, i, 0);
            }
            if (page == 1) {
                btPrevious.setEnabled(false);
                btNext.setEnabled(true);
                btNext.setVisible(true);
            } else {
                btPrevious.setEnabled(true);
            }
        } catch (Exception e) {
//            if (arkPlayer == null) {
//                JOptionPane.showMessageDialog(this, e.getMessage());
//            } else {
            arkPlayer.setOntop(false);
             arkPlayer.pause();
            JOptionPane.showMessageDialog(this, e.getMessage());
       //     arkPlayer.setOntop(true);
//            }
        }
    }//GEN-LAST:event_btPreviousActionPerformed

    private void cbWorkGroupItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbWorkGroupItemStateChanged
        // TODO add your handling code here:
        if (cbWorkGroup.getSelectedIndex() < 0) {
            return;
        }
        String workgroupName = (String) cbWorkGroup.getSelectedItem();
        List workgroup = userAspect.getWorkGrooup();
        if (workgroupName.trim().equals("All")) {
            cbAgent.removeAllItems();
            cbAgent.addItem("All");
            for (Object workgroup1 : workgroup) {
                WorkGroup wk = (WorkGroup) workgroup1;

                List lAgent = wk.getAgent();
                for (Object agent : lAgent) {
                    cbAgent.addItem(((Agent) agent).getAgentName());
                }
            }

        } else {
            System.out.println("else ");
            for (Object workgroup1 : workgroup) {
                WorkGroup wk = (WorkGroup) workgroup1;
                if (wk.getWorkGroupName().equals(workgroupName)) {
                    cbAgent.removeAllItems();
                    List lAgent = wk.getAgent();
                    if (lAgent.size() > 1) {
                        cbAgent.addItem("All");
                    }
                    for (Object agent : lAgent) {
                        cbAgent.addItem(((Agent) agent).getAgentName());
                    }
                    break;
                }
            }
        }
    }//GEN-LAST:event_cbWorkGroupItemStateChanged

    private void setEditFilter(boolean flage) {
//        dfStartDate.setEnabled(flage);
//        spStartTime.setEnabled(flage);
//        dfEndDate.setEnabled(flage);
//        spEndTime.setEnabled(flage);
//        cbDispostion.setEnabled(flage);
//        cbWorkGroup.setEnabled(flage);
//        cbAgent.setEnabled(flage);
//        cbServiceName.setEnabled(flage);
//        tfCallRefID.setEnabled(flage);
//        tfPhoneNumber.setEnabled(flage);
//        tfCustomerID.setEnabled(flage);
//        tfTrackID.setEnabled(flage);
//        rdTimeVoiceMore.setEnabled(flage);
//        rdTimeVoiceLess.setEnabled(flage);
//        spVoice.setEnabled(flage);
    }

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
            java.util.logging.Logger.getLogger(FrmMediaPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMediaPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMediaPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMediaPlayer.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tenant tenant1 = new Tenant();
                tenant1.setTenantName("ACE3");
                tenant1.setDatabase("VoiceRecord3");
                tenant1.setVoicePath("VoiceRecord3");
                UserAspect userAspect = new UserAspect();

                userAspect.setUserID("001");
                userAspect.setWorkGroup(Integer.parseInt("1"), "test");

                WorkGroup wg = new WorkGroup();
                wg.setWorkGroup(Integer.parseInt("1"), "test");
                Agent agent = new Agent();
                agent.setAgent("agent1");
                wg.addAgent(agent);

                userAspect.addWorkGroup(wg);

                new FrmMediaPlayer(tenant1, userAspect);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btClear;
    private javax.swing.JButton btExport;
    private javax.swing.JButton btNext;
    private javax.swing.JButton btPlay;
    private javax.swing.JButton btPrevious;
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
    private javax.swing.JLabel lbTotal;
    private javax.swing.JLabel lbWorkGroup;
    private javax.swing.ButtonGroup rdTimeVoice;
    private javax.swing.JRadioButton rdTimeVoiceLess;
    private javax.swing.JRadioButton rdTimeVoiceMore;
    private javax.swing.JTable tbDetail;
    private javax.swing.JTextField tfCallRefID;
    private javax.swing.JTextField tfCustomerID;
    private javax.swing.JFormattedTextField tfPhoneNumber;
    private javax.swing.JTextField tfTrackID;
    // End of variables declaration//GEN-END:variables
}
