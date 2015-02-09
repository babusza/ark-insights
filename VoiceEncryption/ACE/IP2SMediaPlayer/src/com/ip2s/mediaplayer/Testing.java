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
import java.awt.event.*;

class Testing {

    public void buildGUI() {
        JPanel p = new JPanel(new BorderLayout());
        final JButton btn = new JButton("Do Some Long Task");
        p.add(btn, BorderLayout.SOUTH);
        final JFrame f = new JFrame();
        f.getContentPane().add(p);
        f.setSize(400, 300);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                final JDialog d = new JDialog();
                JPanel p1 = new JPanel(new GridBagLayout());
                p1.add(new JLabel("Please Wait..."), new GridBagConstraints());
                d.getContentPane().add(p1);
                d.setSize(100, 100);
                d.setLocationRelativeTo(f);
                d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
                d.setModal(true);

                Thread t = new Thread() {
                    public void run() {
                        for (int x = 0; x <= 100; x += 10) {
                            final int selection = x;
                            SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
                                public void run() {
                                    btn.setText("long task up to " + selection + "%");
                                }
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        SwingUtilities.invokeLater(new Runnable() {//do swing work on EDT  
                            public void run() {
                                d.dispose();
                                btn.setText("Do Some Long Task");
                            }
                        });
                    }
                };
                t.start();
                d.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Testing().buildGUI();
            }
        });
    }
}
