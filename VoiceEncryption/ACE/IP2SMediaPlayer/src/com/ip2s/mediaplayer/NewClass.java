/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.mediaplayer;

import java.net.InetAddress;

/**
 *
 * @author ark-insingths
 */
public class NewClass {

    public static void main(String[] args) {
        try{
             
        InetAddress add = InetAddress.getByName("172.16.30.14");
        if (add.isReachable(3000)) {
            System.out.println("Yes");
        } else {
            System.out.println("No");
        }
        }catch(Exception e){
            System.out.println("Error : "+e.getMessage());
        }
    }

}
