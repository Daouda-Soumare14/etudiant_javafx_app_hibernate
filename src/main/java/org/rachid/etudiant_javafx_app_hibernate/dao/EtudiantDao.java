package org.rachid.etudiant_javafx_app_hibernate.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.rachid.etudiant_javafx_app_hibernate.JPAUtil;
import org.rachid.etudiant_javafx_app_hibernate.entities.Etudiant;

public class EtudiantDao
{
    private EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();

    public void createOrUpdate(Etudiant etudiant)
    {
        EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            if (etudiant.getId() == 0) {
                entityManager.persist(etudiant);
            } else {
                entityManager.merge(etudiant);
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
            Etudiant etudiant = entityManager.find(Etudiant.class, id);
            if (etudiant != null) {
                entityManager.remove(etudiant);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            e.printStackTrace();
        }
    }

    public ObservableList<Etudiant> findAll()
    {
        try {
            return FXCollections.observableArrayList(
                    entityManager.createQuery("FROM Etudiant", Etudiant.class).getResultList()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FXCollections.observableArrayList();
    }
}
