/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ip2s.aspect;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ark-insingths
 */
public class Service {

    public String getServiceName(String id) {
        for (Object data : service) {
            String[] s = (String[]) data;
            if (s[0].equals(id)) {
                return s[1];
            }
        }
        return null;
    }

    public String getServiceID(String name) {
        for (Object data : service) {
            String[] s = (String[]) data;
            if (s[1].equals(name)) {
                return s[0];
            }
        }
        return null;
    }

    public void addService(String dispositionID, String dispositionName) {
        if (service == null) {
            service = new ArrayList<String[]>();
        }
        service.add(new String[]{dispositionID, dispositionName});
    }
    List service;

}
