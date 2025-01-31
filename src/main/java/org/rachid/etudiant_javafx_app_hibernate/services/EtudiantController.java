package org.rachid.etudiant_javafx_app_hibernate.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rachid.etudiant_javafx_app_hibernate.dao.ClasseDao;
import org.rachid.etudiant_javafx_app_hibernate.dao.EtudiantDao;
import org.rachid.etudiant_javafx_app_hibernate.entities.Classe;
import org.rachid.etudiant_javafx_app_hibernate.entities.Etudiant;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class EtudiantController implements Initializable {

    @FXML
    private Button btn_add;

    @FXML
    private Button btn_clear;

    @FXML
    private Button btn_delete;

    @FXML
    private Button btn_form_classe;

    @FXML
    private Button btn_form_etudiant;

    @FXML
    private TableColumn<?, ?> col_classe;

    @FXML
    private TableColumn<?, ?> col_id;

    @FXML
    private TableColumn<?, ?> col_matricule;

    @FXML
    private TableColumn<?, ?> col_moyenne;

    @FXML
    private TableColumn<?, ?> col_nom;

    @FXML
    private TableColumn<?, ?> col_prenom;

    @FXML
    private ComboBox<Classe> comboBox_classe;

    @FXML
    private TextField field_matricule;

    @FXML
    private TextField field_moyenne;

    @FXML
    private TextField field_nom;

    @FXML
    private TextField field_prenom;

    @FXML
    private TextField field_search;

    @FXML
    private AnchorPane form_etudiant;

    @FXML
    private TableView<Etudiant> tableVeiw_etudiant;

    private EtudiantDao etudiantDao;

    public EtudiantController() {
        this.etudiantDao = new EtudiantDao();
    }

    @FXML
    void switchToClasseForm(ActionEvent event) {
        switchForm(event, "/org/rachid/etudiant_javafx_app_hibernate/resources/views/classe_hibernate.fxml");
    }

    private void switchForm(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane newPane = loader.load();
            Stage stage = (Stage) btn_form_classe.getScene().getWindow();
            Scene newScene = new Scene(newPane);
            stage.setScene(newScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void clearEtudiant(MouseEvent event) {
        clear();
    }

    @FXML
    void createOrUpdateEtudiant(MouseEvent event)
    {
        try {
            if (validateField()) {
                Etudiant etudiantSelectionner = tableVeiw_etudiant.getSelectionModel().getSelectedItem();
                Etudiant etudiant = (etudiantSelectionner != null) ? etudiantSelectionner : new Etudiant();

                String prenom = field_prenom.getText();
                String nom = field_nom.getText();
                String matricule = calculateMatricule(prenom, nom);

                etudiant.setMatricule(matricule);
                etudiant.setPrenom(prenom);
                etudiant.setNom(nom);
                etudiant.setMoyenne(Float.parseFloat(field_moyenne.getText()));
                etudiant.setClasse(comboBox_classe.getValue());

                etudiantDao.createOrUpdate(etudiant);
                showEtudiant();
                clear();

                if (etudiantSelectionner != null) {
                    showInformation("Message d'information", "Etudiant modifier avec succes !!");
                } else {
                    showInformation("Message d'information", "Etudiant ajouté avec succes !!");
                }
            }
        } catch (Exception e) {
            showError("Message d'erreur", "Erreur lors de l'insertion de l'etudiant" + e.getMessage());
        }
    }

    @FXML
    void deleteEtudiant(MouseEvent event)
    {
        try {
            Etudiant etudiantSelectionner = tableVeiw_etudiant.getSelectionModel().getSelectedItem();

            if (etudiantSelectionner != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Message de confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Êtes vous sure de vouloir supprimer l'etudiant selectionner ?");
                Optional<ButtonType> optional = alert.showAndWait();

                if (optional.get().equals(ButtonType.OK)) {
                    etudiantDao.delete(etudiantSelectionner.getId());
                    showEtudiant();
                    clear();
                    showInformation("Message d'information", "Etudiant supprimer avec succes !!!");
                }
            }
        } catch (Exception e) {
            showError("Message d'erreur", "Erreur lors de la suppression de l'etudiant" + e.getMessage());
        }
    }

    @FXML
    void getEtudiant(MouseEvent event)
    {
        Etudiant etudiantSelectionner = tableVeiw_etudiant.getSelectionModel().getSelectedItem();

        if (etudiantSelectionner != null) {
            field_matricule.setText(etudiantSelectionner.getMatricule());
            field_prenom.setText(etudiantSelectionner.getPrenom());
            field_nom.setText(etudiantSelectionner.getNom());
            field_moyenne.setText(String.valueOf(etudiantSelectionner.getMoyenne()));
            comboBox_classe.setValue(etudiantSelectionner.getClasse());
        }
    }

    @FXML
    void searchEtudiant(MouseEvent event)
    {

    }

    @FXML
    void switchForm(ActionEvent event) {

    }

    private boolean validateField()
    {
        if (field_prenom.getText().isEmpty()
            || field_nom.getText().isEmpty()
            || field_moyenne.getText().isEmpty()
            || comboBox_classe.getSelectionModel().isEmpty()) {
            showError("Message d'erreur", "tous les champs sont obligatoire !!!");
            return false;
        }
        return true;
    }

    private void showError(String tittle, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(tittle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformation(String tittle, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(tittle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showConfirmation(String tittle, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(tittle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clear()
    {
        field_matricule.clear();
        field_prenom.clear();
        field_nom.clear();
        field_moyenne.clear();
        comboBox_classe.getSelectionModel().clearSelection();
    }

    private void showEtudiant()
    {
        ObservableList<Etudiant> list = etudiantDao.findAll();
        tableVeiw_etudiant.setItems(list);
        tableVeiw_etudiant.refresh();

        col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        col_matricule.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        col_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        col_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        col_moyenne.setCellValueFactory(new PropertyValueFactory<>("moyenne"));
        col_classe.setCellValueFactory(new PropertyValueFactory<>("classeLibelle"));
    }

    private String calculateMatricule(String prenom, String nom)
    {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String dateTime = localDateTime.format(formatter);
        String premierLettrePrenom = prenom.substring(0, 1).toUpperCase();
        String premierLettreNom = nom.substring(0, 1).toUpperCase();

        return premierLettrePrenom + premierLettreNom + dateTime;
    }

    private void comboBoxClasse()
    {
        ClasseDao classeDao = new ClasseDao();
        comboBox_classe.setItems(FXCollections.observableArrayList(classeDao.findAll()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showEtudiant();
        comboBoxClasse();
        clear();
    }
}
