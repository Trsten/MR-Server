package com.uniza.mr.dao;

import com.uniza.mr.model.User;
import com.uniza.mr.exception.MRException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserPersistence {

    @PersistenceContext(unitName="aaa")
    private EntityManager entityManager;

    public List<User> findAllUsers() throws MRException {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = builder.createQuery(User.class);
        CriteriaQuery<User> select = cq.select(cq.from(User.class));

        List<User> users = this.entityManager.createQuery(select).getResultList();

        if (users.isEmpty()) {
            throw new MRException("This list is empty.");
        }

        return users;
    }

    public User findUser(String email) throws MRException {

        List<User> users = this.findAllUsers();

        for ( User user: users ) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        throw new MRException("Entity with entered email doesnt exist.");
    }

    private User findUser( Long id )  throws MRException {
        User user = this.entityManager.find(User.class, id);

        if (user == null) {
            throw new MRException("Cant find an entity with entered ID.");
        }
        return user;
    }

    public void persistUser(User paUser) throws MRException {

        User user = this.entityManager.find(User.class, paUser.getId());

        if (user != null) {
            throw new MRException("Entity with entered ID already exists.");
        }

        this.entityManager.persist(paUser);
    }

    public void updateUser(User paUser) throws MRException {

        User user = this.findUser(paUser.getId());
        this.entityManager.merge(paUser);
    }

    public void deleteUser(Long id) throws MRException {

        User user = this.findUser(id);
        this.entityManager.remove(user);
    }

    public User logIn(User user) throws MRException {
        User pomUser = this.findUser(user.getEmail());

        if (!user.getPassword().equals(pomUser.getPassword())) {
            throw new MRException("wrong password");
        }
        return pomUser;
    }

}
