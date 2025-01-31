package org.rachid.etudiant_javafx_app_hibernate.services;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.rachid.etudiant_javafx_app_hibernate.dao.ClasseDao;
import org.rachid.etudiant_javafx_app_hibernate.entities.Classe;

import java.net.URL;
import java.util.ResourceBundle;

public class ClasseController implements Initializable {

    @FXML
    private TextField field_libelle, field_effectif;
    @FXML
    private TableView<Classe> table_view;
    @FXML
    private TableColumn<Classe, Long> col_id;
    @FXML
    private TableColumn<Classe, String> col_libelle;
    @FXML
    private TableColumn<Classe, Integer> col_effectif;

    private ClasseDao classeDao;

    public ClasseController() {
        this.classeDao = new ClasseDao();
    }


    @FXML
    void getClasse(MouseEvent event) {
        Classe classe = table_view.getSelectionModel().getSelectedItem();
        if (classe != null) {
            field_libelle.setText(classe.getLibelle());
            field_effectif.setText(String.valueOf(classe.getEffectif()));
        }
    }

    private void clear() {
        field_libelle.clear();
        field_effectif.clear();
        table_view.getSelectionModel().clearSelection();
    }

    @FXML
    void clearClasse(MouseEvent event) {
        clear();
    }

    @FXML
    void createOrUpdateClasse(MouseEvent event) {
        Classe selectedClasse = table_view.getSelectionModel().getSelectedItem();
        Classe classe = (selectedClasse != null) ? selectedClasse : new Classe();

        classe.setLibelle(field_libelle.getText());
        classe.setEffectif(Integer.parseInt(field_effectif.getText()));

        classeDao.createOrUpdate(classe);
        showClasse();
        clear();
    }

    @FXML
    void deleteClasse(MouseEvent event) {
        Classe classe = table_view.getSelectionModel().getSelectedItem();
        if (classe != null) {
            classeDao.delete(classe.getId());
            showClasse();
            clear();
        }
    }

    @FXML
    void searchClasse(MouseEvent event) {

    }


    private void showClasse() {
        try {
            ObservableList<Classe> classeList = classeDao.findAll();
            table_view.setItems(classeList);
            table_view.refresh();

            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_libelle.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            col_effectif.setCellValueFactory(new PropertyValueFactory<>("effectif"));
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des classes : " + e.getMessage());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showClasse();
        clear();
    }
}
