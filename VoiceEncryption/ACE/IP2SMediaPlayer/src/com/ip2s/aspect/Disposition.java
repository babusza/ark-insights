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
public class Disposition {

    public String getDispositionName(String id) {
        for (Object data : disposition) {
            String[] s = (String[]) data;
            if (s[0].equals(id)) {
                return s[1];
            }
        }
        return null;
    }
     public String getDispositionID(String name) {
        for (Object data : disposition) {
            String[] s = (String[]) data;
            if (s[1].equals(name)) {
                return s[0];
            }
        }
        return null;
    }

    public void setDisposition(String dispositionID, String dispositionName) {
        if (disposition == null) {
            disposition = new ArrayList<String[]>();
        }
        disposition.add(new String[]{dispositionID, dispositionName});
    }
    List disposition;

}
