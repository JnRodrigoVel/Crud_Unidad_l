# Padrón Vehicular (Ejercicio 12) — JavaFX + Maven

Mismo patrón que **SysVentas**: `Model → Repository → Service → Controller + FXML`,
con inyección manual en `AppContext` y los componentes reutilizados
(`TableViewHelper`, `Toast`, `ToltipCustom`, `ComboBoxOption`).

## Campos
Placa (única y no editable al modificar), marca, modelo, año, color y propietario.

## CRUD
| Operación  | Dónde |
|------------|-------|
| Agregar    | Formulario → **Guardar** |
| Consultar  | Tabla + buscador (placa, marca, modelo o propietario) |
| Modificar  | Ícono de lápiz en la fila → **Actualizar** (la placa queda bloqueada) |
| Eliminar   | Ícono de basurero en la fila (pide confirmación) |

## Archivos nuevos respecto a SysVentas
`model/Vehiculo`, `enums/ColorVehiculo`, `exception/RegistroDuplicadoException`,
`repository/VehiculoRepository`, `service/IVehiculoService`, `service/impl/VehiculoServiceImp`,
`controller/VehiculoController`, `view/main_vehiculo.fxml`.
(`AppContext`, `MainGuiController` y `maingui.fxml` se adaptaron.)

## Ejecutar
```
./mvnw clean javafx:run        # Linux/Mac
mvnw.cmd clean javafx:run      # Windows
```
Requiere JDK 21. Los datos viven en memoria (se pierden al cerrar), igual que en SysVentas.
