package pe.edu.upeu.padronvehicular.service;

import pe.edu.upeu.padronvehicular.dto.ComboBoxOption;
import pe.edu.upeu.padronvehicular.model.Vehiculo;

import java.util.List;

public interface IVehiculoService extends ICrudGenericoService<Vehiculo, String> {
    List<ComboBoxOption> listarColores();
}
