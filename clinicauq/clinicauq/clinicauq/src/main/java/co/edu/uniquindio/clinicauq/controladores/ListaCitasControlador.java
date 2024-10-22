package co.edu.uniquindio.clinicauq.controladores;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import co.edu.uniquindio.clinicauq.modelo.Clinica;
import co.edu.uniquindio.clinicauq.modelo.Cita;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ListaCitasControlador {

    @FXML
    private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button btncancelarCita;
    @FXML private TableColumn<Cita, Integer> colID;
    @FXML private TableColumn<Cita, String> colFecha;
    @FXML private TableColumn<Cita, String> colPaciente;
    @FXML private TableColumn<Cita, String> colServicio;
    @FXML private TableColumn<Cita, Double> colTotal;
    @FXML private TableView<Cita> tablaCitas;

    // Lista observable de citas
    private ObservableList<Cita> citaObservableList;
    private final Clinica clinica;

    // Constructor
    public ListaCitasControlador() {
        clinica = Clinica.getInstance(); // Usar la instancia única de Clinica
    }

    @FXML
    void initialize() {
        // Inicializar la lista de citas
        citaObservableList = FXCollections.observableArrayList(clinica.listarCitas());

        // Formato de la fecha
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Configurar las columnas de la tabla con las propiedades del objeto Cita
        colID.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getID()));
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFecha().format(formatter)));
        colPaciente.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaciente().getNombre()));
        colServicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getServicio().getNombre()));
        colTotal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getFactura().getTotal()));

        // Asignar la lista de citas a la tabla
        tablaCitas.setItems(citaObservableList);
        cargarCitas();
    }


    // Método para recargar los pacientes en la tabla si se actualiza la lista
    public void cargarCitas() {
        try {
            List<Cita> todasLasCitas = clinica.listarCitas(); // Obtener la lista de citas
            citaObservableList.setAll(todasLasCitas); // Actualizar la lista observable
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el error en la consola para depuración
        }
    }

    // Método para cancelar una cita
    @FXML
    void cancelarCita(ActionEvent event) {
        // Obtener la cita seleccionada
        Cita citaSeleccionada = tablaCitas.getSelectionModel().getSelectedItem();

        if (citaSeleccionada != null) {
            // Eliminar la cita de la lista de citas en la clinica
            clinica.cancelarCita(citaSeleccionada);

            // Actualizar la tabla después de la eliminación
            cargarCitas();

            // Mostrar mensaje de éxito
            mostrarAlerta("Éxito", "Cita cancelada correctamente.");
        } else {
            // Mostrar alerta si no se seleccionó ninguna cita
            mostrarAlerta("Error", "Debe seleccionar una cita para cancelar.");
        }
    }

    // Método para mostrar alertas
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
