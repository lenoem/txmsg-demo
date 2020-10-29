package com.linewell.msgdemo.entity;

import java.io.Serializable;

public class ApasInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String projid;

    private String serviceName;

    private String handstates;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getHandstates() {
        return handstates;
    }

    public void setHandstates(String handstates) {
        this.handstates = handstates;
    }

    public String getProjid() {
        return projid;
    }

    public void setProjid(String projid) {
        this.projid = projid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
