package com.uniza.mr.dao;

import com.uniza.mr.exception.MRException;
import com.uniza.mr.model.AttendantStatus;
import com.uniza.mr.model.MeetingSchedule;
import com.uniza.mr.model.MeetingStatus;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

public class RefDataPersistence {

    @PersistenceContext(unitName="aaa")
    private EntityManager entityManager;

    /*  MEETING STATUS  */
    public List<MeetingStatus> findAllMeetingStatuses() throws MRException {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MeetingStatus> cq = builder.createQuery(MeetingStatus.class);
        CriteriaQuery<MeetingStatus> select = cq.select(cq.from(MeetingStatus.class));

        List<MeetingStatus> msList = this.entityManager.createQuery(select).getResultList();

        if (msList.isEmpty()) {
            throw new MRException("This list is empty.");
        }

        return msList;
    }

    public MeetingStatus findMeetingStatus(Long id) throws MRException {

        MeetingStatus meetingStatus = this.entityManager.find(MeetingStatus.class, id);

        if (meetingStatus == null) {
            throw new MRException("Cannot find an entity with entered ID.");
        }

        return meetingStatus;
    }

    public void persistMeetingStatus(MeetingStatus status) throws MRException {

        MeetingStatus meetingStatus = this.entityManager.find(MeetingStatus.class, status.getId());

        if (meetingStatus != null) {
            throw new MRException("Entity with entered ID already exists.");
        }

        this.entityManager.persist(status);
    }

    public void updateMeetingStatus(MeetingStatus status) throws MRException {

        MeetingStatus meetingStatus = this.findMeetingStatus(status.getId());
        this.entityManager.merge(status);
    }

    public void deleteMeetingStatus(Long id) throws MRException {

        MeetingStatus meetingStatus = this.findMeetingStatus(id);

        this.entityManager.remove(meetingStatus);
    }

    /*  ATTENDANT STATUS  */
    public List<AttendantStatus> findAllAttendantStatuses() throws MRException {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<AttendantStatus> cq = builder.createQuery(AttendantStatus.class);
        CriteriaQuery<AttendantStatus> select = cq.select(cq.from(AttendantStatus.class));

        List<AttendantStatus> asList = this.entityManager.createQuery(select).getResultList();

        if (asList.isEmpty()) {
            throw new MRException("This list is empty.");
        }
        return asList;
    }

    public AttendantStatus findAttendantStatus(Long id) throws MRException {

        AttendantStatus attendantStatus = this.entityManager.find(AttendantStatus.class, id);

        if (attendantStatus == null) {
            throw new MRException("Cannot find an entity with entered ID.");
        }

        return attendantStatus;
    }

    public void persistAttendantStatus(AttendantStatus status) throws MRException {
        AttendantStatus attendantStatus = this.entityManager.find(AttendantStatus.class, status.getId());

        if (attendantStatus != null) {
            throw new MRException("Entity with entered ID already exists.");
        }

        this.entityManager.persist(status);
    }

    public void updateAttendantStatus(AttendantStatus status) throws MRException {

        AttendantStatus attendantStatus = this.findAttendantStatus(status.getId());
        this.entityManager.merge(status);
    }

    public void deleteAttendantStatus(Long id) throws MRException {

        AttendantStatus attendantStatus = this.findAttendantStatus(id);
        this.entityManager.remove(attendantStatus);
    }

    /*  MEETING SCHEDULE  */
    public List<MeetingSchedule> findAllMeetingSchedules() throws MRException {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<MeetingSchedule> cq = builder.createQuery(MeetingSchedule.class);
        CriteriaQuery<MeetingSchedule> select = cq.select(cq.from(MeetingSchedule.class));

        List<MeetingSchedule> msList = this.entityManager.createQuery(select).getResultList();

        if (msList.isEmpty()) {
            throw new MRException("This list is empty.");
        }
        return msList;
    }

    public MeetingSchedule findMeetingSchedule(Long id) throws MRException {

        MeetingSchedule meetingSchedule = this.entityManager.find(MeetingSchedule.class, id);

        if (meetingSchedule == null) {
            throw new MRException("Cannot find an entity with entered ID.");
        }

        return meetingSchedule;
    }

    public void persistMeetingSchedule(MeetingSchedule schedule) throws MRException {
        MeetingSchedule meetingSchedule = this.entityManager.find(MeetingSchedule.class, schedule.getId());

        if (meetingSchedule != null) {
            throw new MRException("Entity with entered ID already exists.");
        }

        this.entityManager.persist(schedule);
    }

    public void updateMeetingSchedule(MeetingSchedule schedule) throws MRException {

        MeetingSchedule meetingSchedule = this.findMeetingSchedule(schedule.getId());
        this.entityManager.merge(schedule);
    }

    public void deleteMeetingSchedule(Long id) throws MRException {

        MeetingSchedule meetingSchedule = this.findMeetingSchedule(id);
        this.entityManager.remove(meetingSchedule);
    }
}
