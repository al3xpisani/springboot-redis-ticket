package com.example.oncallinvext.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String queueName;
    private String issueDescription;
    private String createdAt;
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public void setIssueDescription(String issueDescription) {
        this.issueDescription = issueDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ticketing{" +
                "id=" + id +
                ", queueName='" + queueName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", issueDescription='" + issueDescription + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        return id != null ? id.equals(ticket.id) : ticket.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
