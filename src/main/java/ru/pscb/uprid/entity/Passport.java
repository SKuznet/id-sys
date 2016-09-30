package ru.pscb.uprid.entity;

import javax.persistence.*;

/**
 * Column name equals names at id-sys service
 */
@Entity
@Table(name = "passport")
public class Passport {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "series")
    private String series;
    @Column(name = "number")
    private String number;
    @Column(name = "issue_date")
    private String issueDate;
    @Column(name = "state")
    private int state;
    @Column(name = "state_desc")
    private String stateDesc;

    public Passport() {
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}
