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
public class UserAspect {

    private String userID;
    private int workGroupID;
    private String workGroupName;
    private List<WorkGroup> workgroup;

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setWorkGroup(int id, String name) {
        this.workGroupID = id;
        this.workGroupName = name;
    }

    public void addWorkGroup(WorkGroup workgroup) {
        if (this.workgroup == null) {
            this.workgroup = new ArrayList<WorkGroup>();
        }
        this.workgroup.add(workgroup);
    }

    public List getWorkGrooup() {
        return this.workgroup;
    }

    public List getAllAgent() {
        List<Agent> allAgent = new ArrayList<Agent>();
        for (WorkGroup workgroup1 : workgroup) {
            List agent = workgroup1.getAgent();
            for (Object agent1 : agent) {
                Agent ag = (Agent) agent1;
                allAgent.add(ag);
            }
        }
        return allAgent;
    }
}
