package co.edu.uniquindio.clinicauq.controladores;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.clinicauq.modelo.Cita;
import co.edu.uniquindio.clinicauq.modelo.Clinica;
import co.edu.uniquindio.clinicauq.modelo.Paciente;
import co.edu.uniquindio.clinicauq.modelo.Servicio;
import co.edu.uniquindio.clinicauq.modelo.factory.Suscripcion;
import co.edu.uniquindio.clinicauq.utils.EnvioEmail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class RegistroCitaControlador {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private DatePicker fechaDate;
    @FXML private Button btnconfirmarCita;
    @FXML private ComboBox<String> comboServicio;
    @FXML private TextField horatxt;
    @FXML private TextField pacientetxt;
    private final Clinica clinica;

    // Constructor que obtiene la instancia de Clinica
    public RegistroCitaControlador() {
        clinica = Clinica.getInstance();
    }


    @FXML
    void initialize() {
        assert fechaDate != null : "fx:id=\"fechaDate\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert btnconfirmarCita != null : "fx:id=\"btnconfirmarCita\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert comboServicio != null : "fx:id=\"comboServicio\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert horatxt != null : "fx:id=\"horatxt\" was not injected: check your FXML file 'registroCita.fxml'.";
        assert pacientetxt != null : "fx:id=\"pacientetxt\" was not injected: check your FXML file 'registroCita.fxml'.";
    }

    //Metodo de registar Cita del boton registrar
    @FXML
    void registrarCita(ActionEvent event) {
        try {

            String pacienteID = pacientetxt.getText();
            LocalDate fechaCita = fechaDate.getValue();
            LocalTime horaCita = LocalTime.parse(horatxt.getText());
            String servicioNombre = comboServicio.getSelectionModel().getSelectedItem();

            // Valida campos vacíos
            if (pacienteID.isEmpty() || fechaCita == null || horaCita == null || servicioNombre == null) {
                mostrarAlerta("Error", "Todos los campos deben estar llenos.", Alert.AlertType.ERROR);
                return;
            }

            // Crea una instancia de Paciente
            Paciente paciente = clinica.obtenerPacientePorCedula(pacienteID);
            if (paciente == null) {
                mostrarAlerta("Error", "El paciente no está registrado.", Alert.AlertType.ERROR);
                return;
            }

            // Obtiene la suscripción del paciente
            Suscripcion suscripcion = paciente.getSuscripcion();
            List<Servicio> serviciosDisponibles = clinica.getServiciosDisponibles(suscripcion);

            // Verifica si el servicio seleccionado está disponible
            Servicio servicio = null;
            for (Servicio s : serviciosDisponibles) {
                if (s.getNombre().equals(servicioNombre)) {
                    servicio = s;
                    break;
                }
            }

            if (servicio == null) {
                mostrarAlerta("Error", "El servicio no está disponible para su suscripción.", Alert.AlertType.ERROR);
                return;
            }

            // Crea la Cita
            Cita nuevaCita = Cita.builder()
                    .paciente(paciente)
                    .ID("ID_AUTOGENERADO")
                    .fecha(fechaCita.atTime(horaCita))
                    .servicio(servicio)
                    .factura(null) // Puedes generar la factura después
                    .build();


            clinica.registrarCita(nuevaCita);

            // Mostrar confirmación
            mostrarAlerta("Éxito", "La cita se ha registrado exitosamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }




}