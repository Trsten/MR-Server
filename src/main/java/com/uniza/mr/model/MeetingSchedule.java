package com.uniza.mr.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "MEETING_SCHEDULE")
public class MeetingSchedule {

    @Id
    @NotNull(message="MeetingSchedule id cannot be null.")
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "RECURRENCE_TYPE")
    @Size(min = 1, max = 50)
    @NotNull (message="MeetingSchedule recurrence type cannot be null")
    private String recurrenceType;

    @Column(name = "DESCRIPTION")
    @NotNull (message="MeetingSchedule description cannot be null")
    private String description;

    public MeetingSchedule() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecurrenceType() {
        return recurrenceType;
    }

    public void setRecurrenceType(String recurrenceType) {
        this.recurrenceType = recurrenceType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
