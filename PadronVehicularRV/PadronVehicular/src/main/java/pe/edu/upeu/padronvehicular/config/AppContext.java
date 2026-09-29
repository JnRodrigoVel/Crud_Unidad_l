package pe.edu.upeu.padronvehicular.config;

import pe.edu.upeu.padronvehicular.controller.*;
import pe.edu.upeu.padronvehicular.repository.*;
import pe.edu.upeu.padronvehicular.service.*;
import pe.edu.upeu.padronvehicular.service.impl.*;
import java.util.HashMap;
import java.util.Map;

public class AppContext {


    private static AppContext instance;

    public static synchronized AppContext getInstance() {
        if (instance == null) instance = new AppContext();
        return instance;
    }


    private final Map<Class<?>, Object> contenedor = new HashMap<>();

    private AppContext() {
        registrarRepositorios();
        registrarServicios();
        registrarControladores();
    }


    private void registrarRepositorios() {
        registrar(VehiculoRepository.class, new VehiculoRepository());
    }


    private void registrarServicios() {
        registrar(IVehiculoService.class, new VehiculoServiceImp(getBean(VehiculoRepository.class)));
    }


    private void registrarControladores() {
        registrar(MainGuiController.class, new MainGuiController());
        registrar(VehiculoController.class, new VehiculoController(getBean(IVehiculoService.class)));
    }

    private void registrar(Class<?> tipo, Object bean) {
        contenedor.put(tipo, bean);
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> tipo) {
        Object bean = contenedor.get(tipo);
        if (bean == null) {
            bean = contenedor.values().stream()
                    .filter(b -> tipo.isAssignableFrom(b.getClass()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(
                            "Bean no encontrado: " + tipo.getName() +
                                    "\n→ ¿Lo registraste en AppContext?"));
        }
        return (T) bean;
    }
}
