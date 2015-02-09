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
public class WorkGroup {

    private int workGroupID;
    private String workGroupName;
    private List<Agent> agent;

    public void setWorkGroup(int id, String name) {
        this.workGroupID = id;
        this.workGroupName = name;
    }

    public String getWorkGroupName() {
        return this.workGroupName;
    }

    public int getWorkGroupID() {
        return this.workGroupID;
    }

    public void addAgent(Agent agent) {
        if (this.agent == null) {
            this.agent = new ArrayList<Agent>();
        }
        this.agent.add(agent);
    }

    public List getAgent() {
        return this.agent;
    }

}
