    package co.edu.uniquindio.clinicauq.controladores;
    import java.net.URL;
    import java.util.List;
    import java.util.ResourceBundle;
    import co.edu.uniquindio.clinicauq.modelo.Clinica;
    import co.edu.uniquindio.clinicauq.modelo.Paciente;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.fxml.FXML;
    import javafx.scene.control.Button;
    import javafx.scene.control.TableColumn;
    import javafx.scene.control.TableView;
    import javafx.scene.control.cell.PropertyValueFactory;

    public class ListaPacientesControlador {

        @FXML private TableView<Paciente> tablaPacientes;
        @FXML private TableColumn<Paciente, String> colNombre;
        @FXML private TableColumn<Paciente, String> colCedula;
        @FXML private TableColumn<Paciente, String> colCorreo;
        @FXML private TableColumn<Paciente, String> colTelefono;
        @FXML private TableColumn<Paciente, String> colSuscripcion;

        // Lista observable de pacientes
        private ObservableList<Paciente> pacienteObservableList;
        private final Clinica clinica;

        // Constructor
        public ListaPacientesControlador() {
            clinica = Clinica.getInstance(); // Usar la instancia única de Clinica
        }

        @FXML
        public void initialize() {
            // Inicializar la lista de pacientes
            pacienteObservableList = FXCollections.observableArrayList(clinica.listarPacientes());

            // Configurar las columnas de la tabla con las propiedades del objeto Paciente
            colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            colCedula.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCedula()));
            colCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreo()));
            colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
            colSuscripcion.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSuscripcion().getTipo()));


            // Asignar la lista de pacientes a la tabla
            tablaPacientes.setItems(pacienteObservableList);
            cargarPacientes();
        }

        // Método para recargar los pacientes en la tabla si se actualiza la lista
        public void cargarPacientes() {
            try {
                List<Paciente> todosLosPacientes = clinica.listarPacientes(); // Obtener la lista de pacientes
                pacienteObservableList.setAll(todosLosPacientes); // Actualizar la lista observable
            } catch (Exception e) {
                e.printStackTrace(); // Imprimir el error en la consola para depuración
            }
        }


    }