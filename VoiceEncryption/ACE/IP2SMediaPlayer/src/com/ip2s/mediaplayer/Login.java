/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

import com.ip2s.aspect.Agent;
import com.ip2s.aspect.Tenant;
import com.ip2s.aspect.UserAspect;
import com.ip2s.aspect.Utility;
import com.ip2s.aspect.WorkGroup;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 *
 * @author TeleData
 */
public class Login extends javax.swing.JFrame {

    /**
     * Creates new form Login
     */
    List tenant;
    Utility utility;

    public Login() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.png")));
        try {
            utility = new Utility();
            deleteOldFile();
        } catch (Exception e) {
        }
        initComponents();
        tfUserID.setEnabled(false);
        tfUserID.setText(System.getProperty("user.name"));
       // tfUserID.setText("paween");
//        tfUserID.setText("agent11");
        setTanant();
        this.getContentPane().setBackground(utility.bgColor);
        cbTenant.requestFocus();
    }

    private void deleteOldFile() throws Exception {
        Utility utility = new Utility();
        File dir = new File(utility.outputPath);
        File[] f = dir.listFiles();
        for (File f1 : f) {
            try {
                f1.delete();
            } catch (Exception e) {
                f1.deleteOnExit();
            }
        }
    }

    private void setTanant() {
        tenant = new ArrayList<Tenant>();
        Tenant tenant1 = new Tenant();
        tenant1.setTenantName("ACE1");
        tenant1.setDatabase("VoiceRecord1");
        tenant1.setVoicePath("VoiceRecord1");
        tenant.add(tenant1);

        Tenant tenant2 = new Tenant();
        tenant2.setTenantName("ACE2");
        tenant2.setDatabase("VoiceRecord2");
        tenant2.setVoicePath("VoiceRecord2");
        tenant.add(tenant2);

        Tenant tenant3 = new Tenant();
        tenant3.setTenantName("ACE3");
        tenant3.setDatabase("VoiceRecord3");
        tenant3.setVoicePath("VoiceRecord3");
        tenant.add(tenant3);

        for (Object ten1 : tenant) {
            Tenant ten = (Tenant) ten1;
            cbTenant.addItem(ten.getTenantName()
            );
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

        label1 = new java.awt.Label("Center");
        tfUserID = new java.awt.TextField();
        button1 = new java.awt.Button();
        label3 = new java.awt.Label();
        cbTenant = new java.awt.Choice();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ark-Insights Recording Player");
        setBounds(new java.awt.Rectangle(300, 150, 0, 0));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        label1.setAlignment(java.awt.Label.CENTER);
        label1.setBackground(new java.awt.Color(204, 255, 255));
        label1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label1.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        label1.setForeground(new java.awt.Color(51, 63, 72));
        label1.setText("User ID");

        tfUserID.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tfUserID.setName(""); // NOI18N

        button1.setActionCommand("btLogin");
        button1.setBackground(new java.awt.Color(0, 153, 102));
        button1.setFont(new java.awt.Font("Dialog", 3, 18)); // NOI18N
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setLabel("Login");
        button1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button1ActionPerformed(evt);
            }
        });

        label3.setAlignment(java.awt.Label.CENTER);
        label3.setBackground(new java.awt.Color(204, 255, 255));
        label3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        label3.setFont(new java.awt.Font("Tahoma", 3, 14)); // NOI18N
        label3.setForeground(new java.awt.Color(51, 63, 72));
        label3.setText("Tenant");

        cbTenant.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        cbTenant.setName("cbTanant"); // NOI18N
        cbTenant.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbTenantKeyPressed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(51, 63, 72));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(" Recording Player(on-site)");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/ip2s/mediaplayer/logo_ark.jpg"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addComponent(jLabel2)
                .addContainerGap(166, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbTenant, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(75, 75, 75))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfUserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbTenant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addComponent(button1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        cbTenant.getAccessibleContext().setAccessibleName("");
        jLabel1.getAccessibleContext().setAccessibleName("ARK Recording Player(on-site)");

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void button1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button1ActionPerformed
        // TODO add your handling code here:
        checkLogin();
    }//GEN-LAST:event_button1ActionPerformed
    private void checkLogin() {
        try {

            int selTenant = cbTenant.getSelectedIndex();
            String user = tfUserID.getText();
            Tenant tenant = (Tenant) this.tenant.get(selTenant);
            String[] data = utility.getUserAspect(user, tenant.getDatabase());
            if (data == null) {
                JOptionPane.showMessageDialog(this, "Login failed");
                return;
            }
            UserAspect userAspect = new UserAspect();
            userAspect.setUserID(data[0]);
            userAspect.setWorkGroup(Integer.parseInt(data[1]), data[2]);
            Vector vWorkGroup = utility.getSupervisorByGroupID(tenant.getDatabase(), userAspect.getUserID());
            for (Object vWorkGroup1 : vWorkGroup) {
                String[] sWorkGroup = (String[]) vWorkGroup1;
                WorkGroup wg = new WorkGroup();
                wg.setWorkGroup(Integer.parseInt(sWorkGroup[0]), sWorkGroup[1]);
                Vector vAgent = utility.getAgents(tenant.getDatabase(), Integer.parseInt(sWorkGroup[0]));
                for (Object vAgent1 : vAgent) {
                    Agent agent = new Agent();
                    agent.setAgent((String) vAgent1);
                    wg.addAgent(agent);
                }
                userAspect.addWorkGroup(wg);
            }
            this.dispose();
            new FrmMediaPlayer(tenant, userAspect);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error : " + e.getMessage());
        }
    }

    private void cbTenantKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbTenantKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            checkLogin();
        }
    }//GEN-LAST:event_cbTenantKeyPressed

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
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button button1;
    private java.awt.Choice cbTenant;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private java.awt.Label label1;
    private java.awt.Label label3;
    private java.awt.TextField tfUserID;
    // End of variables declaration//GEN-END:variables
}
