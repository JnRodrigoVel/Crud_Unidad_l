package pe.edu.upeu.padronvehicular.service.impl;

import lombok.RequiredArgsConstructor;
import pe.edu.upeu.padronvehicular.dto.ComboBoxOption;
import pe.edu.upeu.padronvehicular.enums.ColorVehiculo;
import pe.edu.upeu.padronvehicular.exception.RegistroDuplicadoException;
import pe.edu.upeu.padronvehicular.model.Vehiculo;
import pe.edu.upeu.padronvehicular.repository.ICrudGenericoRepository;
import pe.edu.upeu.padronvehicular.repository.VehiculoRepository;
import pe.edu.upeu.padronvehicular.service.IVehiculoService;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class VehiculoServiceImp extends CrudGenericoServiceImp<Vehiculo, String>
        implements IVehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Override
    protected ICrudGenericoRepository<Vehiculo, String> getRepo() {
        return vehiculoRepository;
    }

    // la placa debe ser unica
    @Override
    public Vehiculo save(Vehiculo vehiculo) {
        if (vehiculoRepository.existsById(vehiculo.getPlaca())) {
            throw new RegistroDuplicadoException(
                    "Ya existe un vehículo registrado con la placa " + vehiculo.getPlaca());
        }
        return super.save(vehiculo);
    }

    @Override
    public List<Vehiculo> findAll() {
        if (vehiculoRepository.findAll().isEmpty()) {
            vehiculoRepository.seedData();
        }
        return vehiculoRepository.findAll();
    }

    @Override
    public List<ComboBoxOption> listarColores() {
        List<ComboBoxOption> listar = new ArrayList<>();
        for (ColorVehiculo c : ColorVehiculo.values()) {
            ComboBoxOption cb = new ComboBoxOption();
            cb.setKey(c.name());
            cb.setValue(c.getDescripcion());
            listar.add(cb);
        }
        return listar;
    }
}
