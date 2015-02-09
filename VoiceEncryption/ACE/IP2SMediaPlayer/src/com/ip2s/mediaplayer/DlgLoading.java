/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

/**
 *
 * @author ark-insingths
 */
import javax.swing.*;
import java.awt.*;
import java.net.URL;

class DlgLoading extends JDialog {

    JFrame frame;

    DlgLoading() {
        frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setBackground(new java.awt.Color(204, 255, 255));
        URL url = getClass().getResource("loader.gif");
        Icon icon = new ImageIcon(url);
        JLabel lbLoading = new JLabel(icon);
        panel.add(lbLoading);
        frame.getContentPane().add(panel);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(this);
        frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);

    }

    public boolean showLoad() {
        frame.setVisible(true);        
        return true;
    }

    public void closeLoad() {
        frame.dispose();
    }

    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE);
        DlgLoading dlg = new DlgLoading();
        dlg.showLoad();
        //  dlg.closeLoad();
    }
}
