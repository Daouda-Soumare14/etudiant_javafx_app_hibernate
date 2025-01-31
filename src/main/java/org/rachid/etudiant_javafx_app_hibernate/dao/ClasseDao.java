package org.rachid.etudiant_javafx_app_hibernate.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.internal.TransactionImpl;
import org.rachid.etudiant_javafx_app_hibernate.JPAUtil;
import org.rachid.etudiant_javafx_app_hibernate.entities.Classe;

import java.util.List;

public class ClasseDao {
    private final EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    public void createOrUpdate(Classe classe)
    {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            if (classe.getId() == 0) {
                entityManager.persist(classe);
            } else {
                entityManager.merge(classe);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }

    public void delete(int id)
    {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Classe classe = entityManager.find(Classe.class, id);
            if (classe != null) {
                entityManager.remove(classe);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }

    public ObservableList<Classe> findAll()
    {
        try {
            return FXCollections.observableArrayList(
                    entityManager.createQuery("FROM Classe", Classe.class).getResultList()
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return FXCollections.observableArrayList();
    }
}
