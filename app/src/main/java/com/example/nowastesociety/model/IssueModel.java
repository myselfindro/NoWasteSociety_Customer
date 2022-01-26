package com.example.nowastesociety.model;

public class IssueModel {

    private String id, issueType;

    public IssueModel(String id, String issueType) {
        this.id = id;
        this.issueType = issueType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
}
