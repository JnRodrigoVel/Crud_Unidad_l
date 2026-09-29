package pe.edu.upeu.padronvehicular.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import pe.edu.upeu.padronvehicular.components.ColumnInfo;
import pe.edu.upeu.padronvehicular.components.TableViewHelper;
import pe.edu.upeu.padronvehicular.components.Toast;
import pe.edu.upeu.padronvehicular.components.ToltipCustom;
import pe.edu.upeu.padronvehicular.dto.ComboBoxOption;
import pe.edu.upeu.padronvehicular.enums.ColorVehiculo;
import pe.edu.upeu.padronvehicular.exception.RegistroDuplicadoException;
import pe.edu.upeu.padronvehicular.model.Vehiculo;
import pe.edu.upeu.padronvehicular.service.IVehiculoService;

import java.util.*;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class VehiculoController {
    private final IVehiculoService vs;

    @FXML
    TextField txtPlaca, txtMarca, txtModelo, txtAnio, txtPropietario, txtFiltroDato;
    @FXML
    ComboBox<ComboBoxOption> cbxColor;
    @FXML
    Button btnGuardar;

    @FXML private TableView<Vehiculo> tableView;

    @FXML
    Label lbnMsg;
    @FXML private AnchorPane miContenedor;
    Stage stage;

    private Validator validator;
    private final ObservableList<Vehiculo> listarVehiculo = FXCollections.observableArrayList();
    private FilteredList<Vehiculo> listaFiltrada;
    Vehiculo formulario;

    String placaEnEdicion = null;

    private final ToltipCustom ttc = new ToltipCustom();


    @FXML
    public void initialize() {
        cbxColor.getItems().addAll(vs.listarColores());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();


        txtPlaca.setTextFormatter(new TextFormatter<String>(c -> {
            c.setText(c.getText().toUpperCase());
            return c;
        }));
        txtAnio.setTextFormatter(new TextFormatter<String>(c ->
                c.getControlNewText().matches("\\d{0,4}") ? c : null));

        TableViewHelper<Vehiculo> tableViewHelper = new TableViewHelper<>();

        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("Placa", new ColumnInfo("placa", 100.0));
        columns.put("Marca", new ColumnInfo("marca", 130.0));
        columns.put("Modelo", new ColumnInfo("modelo", 130.0));
        columns.put("Año", new ColumnInfo("anio", 70.0));
        columns.put("Color", new ColumnInfo("color", 100.0));
        columns.put("Propietario", new ColumnInfo("propietario", 200.0));

        Consumer<Vehiculo> updateAction = vehiculo -> { editForm(vehiculo); };
        Consumer<Vehiculo> deleteAction = vehiculo -> { eliminar(vehiculo); };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);


        listaFiltrada = new FilteredList<>(listarVehiculo, v -> true);
        SortedList<Vehiculo> listaOrdenada = new SortedList<>(listaFiltrada);
        listaOrdenada.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(listaOrdenada);
        txtFiltroDato.textProperty().addListener((obs, o, n) -> filtrarVehiculos(n));

        lbnMsg.setText("");
        listar();
    }


    public void listar() {
        try {
            listarVehiculo.setAll(vs.findAll());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    private void filtrarVehiculos(String texto) {
        String f = texto == null ? "" : texto.trim().toLowerCase();
        listaFiltrada.setPredicate(v -> f.isEmpty()
                || v.getPlaca().toLowerCase().contains(f)
                || v.getMarca().toLowerCase().contains(f)
                || v.getModelo().toLowerCase().contains(f)
                || v.getPropietario().toLowerCase().contains(f));
    }


    @FXML
    public void validarFormulario() {
        formulario = new Vehiculo();
        String placa = placaEnEdicion != null ? placaEnEdicion : txtPlaca.getText();
        formulario.setPlaca(placa == null ? "" : placa.trim().toUpperCase());
        formulario.setMarca(txtMarca.getText() == null ? "" : txtMarca.getText().trim());
        formulario.setModelo(txtModelo.getText() == null ? "" : txtModelo.getText().trim());
        formulario.setAnio(parseIntSafe(txtAnio.getText()));
        formulario.setPropietario(txtPropietario.getText() == null ? "" : txtPropietario.getText().trim());

        String idxC = cbxColor.getSelectionModel().getSelectedItem() == null ? ""
                : cbxColor.getSelectionModel().getSelectedItem().getKey();
        formulario.setColor(idxC.equals("") ? null : ColorVehiculo.valueOf(idxC));

        Set<ConstraintViolation<Vehiculo>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Vehiculo>> violacionesOrdenadas = violaciones.stream()
                .sorted(Comparator.comparing(v -> v.getPropertyPath().toString())).toList();

        if (violacionesOrdenadas.isEmpty()) {
            procesarFormulario();
        } else {
            mostrarErroresValidacion(violacionesOrdenadas);
        }
    }

    private Integer parseIntSafe(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        try { return Integer.parseInt(value.trim()); }
        catch (NumberFormatException e) { return null; }
    }


    @FXML
    public void clearForm() {
        txtPlaca.clear(); txtPlaca.setDisable(false);
        txtMarca.clear(); txtModelo.clear(); txtAnio.clear();
        cbxColor.getSelectionModel().clearSelection();
        txtPropietario.clear();
        placaEnEdicion = null;
        btnGuardar.setText("Guardar");
        tableView.getSelectionModel().clearSelection();
        lbnMsg.setText("");
        limpiarError();
    }


    public void editForm(Vehiculo vehiculo) {
        limpiarError();
        txtPlaca.setText(vehiculo.getPlaca());
        txtPlaca.setDisable(true);
        txtMarca.setText(vehiculo.getMarca());
        txtModelo.setText(vehiculo.getModelo());
        txtAnio.setText(String.valueOf(vehiculo.getAnio()));
        txtPropietario.setText(vehiculo.getPropietario());

        cbxColor.getSelectionModel().select(
                cbxColor.getItems().stream()
                        .filter(c -> c.getKey().equals(vehiculo.getColor().name()))
                        .findFirst().orElse(null));

        placaEnEdicion = vehiculo.getPlaca();
        btnGuardar.setText("Actualizar");
        lbnMsg.setText("");
    }



    private void eliminar(Vehiculo vehiculo) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar el vehículo con placa " + vehiculo.getPlaca() + "?",
                ButtonType.YES, ButtonType.NO);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.isPresent() && respuesta.get() == ButtonType.YES) {
            stage = (Stage) miContenedor.getScene().getWindow();
            vs.delete(vehiculo.getPlaca());
            if (vehiculo.getPlaca().equals(placaEnEdicion)) clearForm();
            double w = stage.getWidth() / 1.5, h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, w, h);
            listar();
        }
    }


    private void mostrarErroresValidacion(List<ConstraintViolation<Vehiculo>> violaciones) {
        limpiarError();
        Map<String, Control> campos = new LinkedHashMap<>();
        campos.put("placa", txtPlaca);
        campos.put("marca", txtMarca);
        campos.put("modelo", txtModelo);
        campos.put("anio", txtAnio);
        campos.put("color", cbxColor);
        campos.put("propietario", txtPropietario);

        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        final Control[] primerCtrl = {null};
        for (String campo : campos.keySet()) {
            violaciones.stream()
                    .filter(v -> v.getPropertyPath().toString().equals(campo))
                    .findFirst().ifPresent(v -> {
                        erroresOrdenados.put(campo, v.getMessage());
                        Control c = campos.get(campo);
                        if (c != null) ttc.marcarError(c, v.getMessage().trim());
                        if (primerCtrl[0] == null) primerCtrl[0] = c;
                    });
        }
        if (!erroresOrdenados.isEmpty()) {
            lbnMsg.setText(erroresOrdenados.entrySet().iterator().next().getValue());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            if (primerCtrl[0] != null) Platform.runLater(primerCtrl[0]::requestFocus);
        }
    }


    private void procesarFormulario() {
        stage = (Stage) miContenedor.getScene().getWindow();
        limpiarError();
        double w = stage.getWidth() / 1.5, h = stage.getHeight() / 2;
        try {
            if (placaEnEdicion != null) {
                vs.update(placaEnEdicion, formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, w, h);
            } else {
                vs.save(formulario);
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, w, h);
            }
            clearForm();
            listar();
        } catch (RegistroDuplicadoException e) {

            ttc.marcarError(txtPlaca, e.getMessage());
            lbnMsg.setText(e.getMessage());
            lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
            Platform.runLater(txtPlaca::requestFocus);
        }
    }

    public void limpiarError() {
        List.of(txtPlaca, txtMarca, txtModelo, txtAnio, cbxColor, txtPropietario)
                .forEach(c -> {
                    c.getStyleClass().remove("text-field-error");
                    ttc.limpiarCampo(c);
                });
    }

}
