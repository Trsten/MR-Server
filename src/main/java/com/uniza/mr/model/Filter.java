package com.uniza.mr.model;

import java.util.Date;

public class Filter {

    private Long userId;
    private Long meetingStatusId;
    private Long meetingScheduleId;
    private Long attandantStatusId;
    private Date startDate;
    private Date endDate;
    private boolean isOwner;
    private boolean isAttendant;

    public Filter() {
    }

    public Long getUserId() {
        return userId;
    }

    public Long getMeetingStatusId() {
        return meetingStatusId;
    }

    public Long getAttandantStatusId() {
        return attandantStatusId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setMeetingStatusId(Long meetingStatusId) {
        this.meetingStatusId = meetingStatusId;
    }

    public void setAttandantStatusId(Long attandantStatusId) {
        this.attandantStatusId = attandantStatusId;
    }

    public Date getStartDate() { return startDate; }

    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }

    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Long getMeetingScheduleId() { return meetingScheduleId; }

    public void setMeetingScheduleId(Long meetingScheduleId) { this.meetingScheduleId = meetingScheduleId; }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public boolean isAttendant() {
        return isAttendant;
    }

    public void setAttendant(boolean attendant) {
        isAttendant = attendant;
    }
}
