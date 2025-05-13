package com.hcmute.myanime.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name= "attempt_logs", schema = "hcmutemyanime")
public class AttemptLogEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String attemptType;
    private String ipAddress;
    @CreationTimestamp
    private Timestamp createAt;

    public AttemptLogEntity() {
    }

    public AttemptLogEntity(int id, String attemptType, String ipAddress, Timestamp createAt) {
        this.id = id;
        this.attemptType = attemptType;
        this.ipAddress = ipAddress;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAttemptType() {
        return attemptType;
    }

    public void setAttemptType(String attemptType) {
        this.attemptType = attemptType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }
}
