module org.rachid.etudiant_javafx_app_hibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;

    // Exporte les packages utilisés par d'autres modules
    exports org.rachid.etudiant_javafx_app_hibernate;
    exports org.rachid.etudiant_javafx_app_hibernate.services;

    // Ouvre les packages pour des bibliothèques spécifiques
    opens org.rachid.etudiant_javafx_app_hibernate to javafx.fxml;
    opens org.rachid.etudiant_javafx_app_hibernate.entities to javafx.base, org.hibernate.orm.core;
    opens org.rachid.etudiant_javafx_app_hibernate.services to javafx.fxml;
}
