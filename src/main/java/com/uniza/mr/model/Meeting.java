package com.uniza.mr.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MEETING")
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meeting_generator")
    @SequenceGenerator(name="meeting_generator", sequenceName = "meeting_seq", allocationSize = 1)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id;

    @Column(name = "ID_USER")
    @NotNull (message="Meeting start date cannot be null")
    private Long userId;

    @Column(name = "ID_MEETING_STATUS")
    @NotNull (message="Meeting start date cannot be null")
    private Long meetingStatusId;

    @Column(name = "ID_MEETING_SCHEDULE")
    @NotNull (message="Meeting start date cannot be null")
    private Long meetingScheduleId;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendant> attendants;

    @Column(name = "ID_PARENT")
    private Long parentId;

    @Column(name = "DATE")
    @NotNull (message="Meeting start date cannot be null")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "SHORT_TITLE")
    @NotNull (message="Meeting title cannot be null")
    @Size(min = 1, max = 50)
    private String shortTitle;

    @Column(name = "END_DATE")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "PLACE")
    @Size(min = 1, max = 255)
    @NotNull (message="Meeting place cannot be null")
    private String place;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "TOPIC")
    private String topic;

    public Meeting() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMeetingStatusId() {
        return meetingStatusId;
    }

    public void setMeetingStatusId(Long meetingStatusId) {
        this.meetingStatusId = meetingStatusId;
    }

    public Long getMeetingScheduleId() {
        return meetingScheduleId;
    }

    public void setMeetingScheduleId(Long meetingScheduleId) {
        this.meetingScheduleId = meetingScheduleId;
    }

    public List<Attendant> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<Attendant> attendants) {
        this.attendants = attendants;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getShortTitle() {
        return shortTitle;
    }

    public void setShortTitle(String shortTitle) {
        this.shortTitle = shortTitle;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}