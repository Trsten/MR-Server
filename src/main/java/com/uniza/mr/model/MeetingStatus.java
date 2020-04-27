package com.uniza.mr.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MEETING_STATUS")

public class MeetingStatus {

    @Id
    @NotNull(message="MeetingStatus id cannot be null.")
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "STATUS")
    @Size(min = 1, max = 20)
    @NotNull (message="MeetingStatus status cannot be null")
    private String status;

    @Column(name = "DESCRIPTION")
    @NotNull (message="MeetingStatus description cannot be null")
    private String description;

    public MeetingStatus() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
