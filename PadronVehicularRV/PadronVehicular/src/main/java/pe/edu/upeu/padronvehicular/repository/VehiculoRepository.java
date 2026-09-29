package pe.edu.upeu.padronvehicular.repository;

import pe.edu.upeu.padronvehicular.enums.ColorVehiculo;
import pe.edu.upeu.padronvehicular.model.Vehiculo;

public class VehiculoRepository extends AbstractJpaRepository<Vehiculo, String> {
    private boolean sembrado = false;

    @Override
    protected String getId(Vehiculo entity) {
        return entity.getPlaca();
    }

    @Override
    protected void setId(Vehiculo entity, String id) {
        entity.setPlaca(id);
    }

    @Override
    protected String generateId() {
        // La placa la escribe el usuario, no se autogenera.
        throw new UnsupportedOperationException("La placa la define el usuario");
    }

    // Datos de ejemplo: se cargan una sola vez (aunque luego borres todo).
    public void seedData() {
        if (!sembrado && findAll().isEmpty()) {
            sembrado = true;
            save(new Vehiculo("ABC-123", "Toyota", "Yaris", 2019, ColorVehiculo.BLANCO, "Ana Quispe"));
            save(new Vehiculo("V4X-812", "Hyundai", "Accent", 2021, ColorVehiculo.GRIS, "Luis Mamani"));
            save(new Vehiculo("D2F-556", "Kia", "Rio", 2017, ColorVehiculo.ROJO, "Rosa Condori"));
        }
    }
}
