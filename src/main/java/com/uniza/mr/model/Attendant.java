package com.uniza.mr.model;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ATTENDANT")
public class Attendant {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attendant_generator")
    @SequenceGenerator(name="attendant_generator", sequenceName = "attendant_seq", allocationSize = 1)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @JsonbTransient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MEETING", referencedColumnName = "ID")
    private Meeting meeting;

    @Column(name = "ID_ATTENDANT_STATUS")
    @NotNull(message="Meeting start date cannot be null")
    private Long attendantStatusId;

    @Column(name = "ID_USER")
    @NotNull (message="Meeting start date cannot be null")
    private Long userId;

    public Attendant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Long getAttendantStatusId() {
        return attendantStatusId;
    }

    public void setAttendantStatusId(Long attendantStatusId) {
        this.attendantStatusId = attendantStatusId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
