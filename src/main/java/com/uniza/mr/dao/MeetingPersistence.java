package com.uniza.mr.dao;

import com.uniza.mr.exception.MRException;
import com.uniza.mr.model.Attendant;
import com.uniza.mr.model.Filter;
import com.uniza.mr.model.Meeting;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class MeetingPersistence {

    @PersistenceContext(unitName="aaa")
    private EntityManager entityManager;

    public List<Meeting> findAllMeetings() throws MRException {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Meeting> cq = builder.createQuery(Meeting.class);
        CriteriaQuery<Meeting> select = cq.select(cq.from(Meeting.class));

        List<Meeting> meetingList = this.entityManager.createQuery(select).getResultList();

        if (meetingList.isEmpty()) {
            throw new MRException("This list is empty.");
        }

        return meetingList;
    }

    public List<Meeting> findFilteredMeetings(Filter filter) throws MRException {
        if(filter.getUserId() == null) {
            throw new MRException("UserId is null.");
        }

        if (filter.isOwner() && filter.getAttandantStatusId() != null) {
            throw new MRException("You can use filter attendantStatusId if you are selecting only by attendant.");
        }
        String query = this.createQuery(filter);

        TypedQuery<Meeting> q = this.entityManager.createQuery(query, Meeting.class);

        if (filter.isAttendant() == filter.isOwner()) {
            q.setParameter("userIdAttendant", filter.getUserId());
            q.setParameter("userIdMeeting", filter.getUserId());
        } else if (filter.isAttendant()) {
            q.setParameter("userIdAttendant", filter.getUserId());
        } else {
            q.setParameter("userIdMeeting", filter.getUserId());
        }

        if(filter.isAttendant() && filter.getAttandantStatusId() != null) {
            q.setParameter("attendantStatusId",filter.getAttandantStatusId());
        }
        if (filter.getMeetingScheduleId() != null) {
            q.setParameter("meetingScheduleId",filter.getMeetingScheduleId());
        }
        if (filter.getMeetingStatusId() != null) {
            q.setParameter("meetingStatusId",filter.getMeetingStatusId());
        }
        if (filter.getStartDate() != null) {
            q.setParameter("startDate",filter.getStartDate());
        }
        if (filter.getEndDate() != null) {
            q.setParameter("endDate",filter.getEndDate());
         }

        //aktualizuj planovany -> uskutocneny ak po datume
        for ( Meeting mt : q.getResultList() ) {
            if ( mt.getMeetingStatusId() == 40L && mt.getDate().compareTo(new Date()) < 0) {
                mt.setMeetingStatusId(42L);
                this.entityManager.merge(mt);
            }
        }

        return q.getResultList();
    }

    public Meeting findMeeting(Long id) throws MRException {

        Meeting meeting = this.entityManager.find(Meeting.class, id);

        if (meeting == null) {
            throw new MRException("Cannot find an entity with entered ID.");
        }

        return meeting;
    }

    public Meeting persistMeeting(Meeting paMeeting) throws MRException {

        if (paMeeting.getMeetingScheduleId() == null) {
            throw new MRException("Meeting schedule is empty.");
        }
        if (paMeeting.getPlace() == null) {
            throw new MRException("Place is empty.");
        }

        if (paMeeting.getShortTitle() == null) {
            throw new MRException("Short title is empty.");
        }

        if(paMeeting.getMeetingStatusId() == null) {
            throw new MRException("Meeting status is empty.");
        }

        if(paMeeting.getAttendants().isEmpty()) {
            throw new MRException("Nobody was invited to meeting");
        } else {
            for ( Attendant attendant : paMeeting.getAttendants() ) {
                attendant.setMeeting(paMeeting);
            }
        }

        if( paMeeting.getMeetingScheduleId()  != null && paMeeting.getMeetingScheduleId()  == 50) {
            if (paMeeting.getEndDate() == null) {
                throw new MRException("Meeting end date is empty, cant be empty if you want recurrence meetings");
            }
        }

        this.entityManager.persist(paMeeting);
        return paMeeting;
    }

    public Meeting updateFamilyOfMeetings(Meeting paMeeting) throws MRException{

        this.updateMeeting(paMeeting);
        List<Meeting> resultList = getChildrenMeetings(paMeeting.getId());

        for (Meeting mt: resultList) {
            paMeeting.setId(mt.getId());
            paMeeting.setAttendants(new ArrayList<>(mt.getAttendants()));
            this.updateMeeting(paMeeting);
        }

        Meeting meeting = findMeeting(paMeeting.getId());

        //update parenta ak je planovany
        if ( meeting.getParentId() != null ) {
            Meeting parent = this.findMeeting(meeting.getParentId());
            if ( parent.getMeetingStatusId() == 40L  ) {
                paMeeting.setId(parent.getId());
                this.updateMeeting(paMeeting);
            }
        }
        return paMeeting;
    }

    /*
        najdem podla id meeting ktory chcem upravit a zmenim iba tie atributy ktore poslem
        tie ktore su null ostanu povodne
    */
    public Meeting updateMeeting(Meeting paMeeting) throws MRException {

        Meeting meeting = this.findMeeting(paMeeting.getId());

        Long planning = 50L;

        if ( paMeeting.getUserId() != null) {
            meeting.setUserId(paMeeting.getUserId());
        }
        if ( paMeeting.getMeetingStatusId() != null) {
            meeting.setMeetingStatusId(paMeeting.getMeetingStatusId());
        }
        if ( paMeeting.getParentId() != null) {
            meeting.setParentId(paMeeting.getParentId());
        }

        if ( paMeeting.getMeetingScheduleId() != null) {
            if (!meeting.getMeetingScheduleId().equals(planning)  && paMeeting.getMeetingScheduleId().equals(planning)) {
                //meeting bude samostanty a nebude patrit do skupiny opakujucich
                meeting.setParentId(null);
            }
            meeting.setMeetingScheduleId(paMeeting.getMeetingScheduleId());
        }
        if ( paMeeting.getDate() != null) {
            meeting.setDate(paMeeting.getDate());
        }
        if ( paMeeting.getShortTitle() != null) {
            meeting.setShortTitle(paMeeting.getShortTitle());
        }
        if ( paMeeting.getEndDate() != null) {
            meeting.setEndDate(paMeeting.getEndDate());
        }
        if ( paMeeting.getPlace() != null) {
            meeting.setPlace(paMeeting.getPlace());
        }
        if ( paMeeting.getDescription() != null) {
            meeting.setDescription(paMeeting.getDescription());
        }

        if (paMeeting.getAttendants() != null || !meeting.getAttendants().isEmpty()) {
            int index = 0;
            while (meeting.getAttendants().size() != index) {
                Long attUserId = meeting.getAttendants().get(index).getUserId();
                boolean found = false;

                for (int i = 0; i < paMeeting.getAttendants().size(); i++) {
                    Long paAttUserId = paMeeting.getAttendants().get(i).getUserId();

                    if (paAttUserId.equals(attUserId)) {
                        found = true;
                        meeting.getAttendants().get(index).setMeeting(meeting);
                        index++;
                        paMeeting.getAttendants().remove(i);
                        break;
                    }
                }
                if (!found) {
                    Attendant attendant = this.entityManager.find(Attendant.class, meeting.getAttendants().get(index).getId());
                    // nemalo by vyhodit vynimku pretoze prechadzam ucastnikov ktorý vlastni meeting a tie mazem, ak neexistuje tak ho nemozem ani vymazat
                    if ( attendant == null ) {
                        throw new MRException("Cant find attendant by ID of attendant");
                    }
                    this.entityManager.remove(attendant);
                    meeting.getAttendants().remove(index);
                }
            }

            for ( Attendant att: paMeeting.getAttendants() ) {
                att.setMeeting(meeting);
                meeting.getAttendants().add(att);
            }
        }

        if ( paMeeting.getTopic() != null) {
            meeting.setTopic(paMeeting.getTopic());
        }

        this.entityManager.merge(meeting);

        return meeting;
    }

    public ArrayList<Long> deleteMeeting(Long id, Boolean delChildrens) throws MRException {

        Meeting meeting = this.findMeeting(id);

        ArrayList<Long> removedMeetings = new ArrayList<>();
        if ( delChildrens ) {
            removedMeetings = this.deleteChildrenMeetings(meeting.getId());
        }

        this.entityManager.remove(meeting);
        return removedMeetings;
    }

    /*
    vymazem meeting s id
    vymazem meetingy ktore maju nastaveneho parenta ako id hlavneho meetingu
    vymazem iba ak je meeting planovany
 */
    private ArrayList<Long> deleteChildrenMeetings(Long id)  throws MRException {

        Meeting meeting = this.findMeeting(id);

        List<Meeting> resultList = getChildrenMeetings(id);

        ArrayList<Long> idOfRemovedMeetings = new ArrayList<>();

        while( !resultList.isEmpty() ) {
            idOfRemovedMeetings.add(resultList.get(0).getId());
            this.entityManager.remove(resultList.get(0));
            resultList.remove(0);
        }

        //odstranim aj parenta ak chcem vymazat vsetky na zaklade children
        if ( meeting.getParentId() != null ) {
            Meeting parent = this.findMeeting(meeting.getParentId());
            if ( parent.getMeetingStatusId() == 40L  ) {
                this.entityManager.remove(parent);
            }
        }
        return idOfRemovedMeetings;
    }

    private String createQuery(Filter filter) {
        String query;

        //triedim podla obidvoch
        if (filter.isOwner() == filter.isAttendant()) {
            query = "SELECT DISTINCT m FROM Meeting m LEFT OUTER JOIN m.attendants a " +
                    "WHERE  ((a.userId = :userIdAttendant OR m.userId = :userIdMeeting)";
            //triedim podla ucastnika
        } else if (filter.isAttendant()) {
            query = "SELECT DISTINCT m FROM Meeting m JOIN m.attendants a WHERE ((a.userId = :userIdAttendant)";
            if (filter.getAttandantStatusId() != null) {
                query+= " AND a.attendantStatusId = :attendantStatusId";
            }
            //triedim podla veduceho
        } else {
            query = "SELECT m FROM Meeting m WHERE ((m.userId = :userIdMeeting)";
        }

        if (filter.getMeetingScheduleId() != null) {
            query += " AND m.meetingScheduleId = :meetingScheduleId";
        }
        if (filter.getMeetingStatusId() != null) {
            query += " AND m.meetingStatusId = :meetingStatusId";
        }
        if (filter.getStartDate() != null) {
            query += " AND m.date >= :startDate";
        }
        if (filter.getEndDate() != null) {
            query += " AND m.date <= :endDate";
        }
        return query + ")" + "ORDER BY m.date";
    }

    private List<Meeting> getChildrenMeetings(Long id)  throws MRException {
        Meeting meeting = this.findMeeting(id);

        String query = "SELECT e FROM Meeting e WHERE e.parentId = :parentId AND e.meetingStatusId = :meetingStatusId";
        TypedQuery<Meeting> q = this.entityManager.createQuery(query, Meeting.class);

        Long planning = 40L;
        if ( meeting.getParentId() == null ) {
            q.setParameter("parentId", meeting.getId());
        } else {
            q.setParameter("parentId", meeting.getParentId());
        }

        q.setParameter( "meetingStatusId" , planning );
        return q.getResultList();
    }

}