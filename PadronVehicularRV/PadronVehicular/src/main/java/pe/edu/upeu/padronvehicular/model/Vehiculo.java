package pe.edu.upeu.padronvehicular.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.padronvehicular.enums.ColorVehiculo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehiculo {


    @NotBlank(message = "La placa es obligatoria")
    @Pattern(regexp = "^[A-Z0-9-]{5,8}$",
            message = "La placa debe tener entre 5 y 8 caracteres (letras, números o guion)")
    private String placa;
    @NotBlank(message = "La marca es obligatoria")
    private String marca;
    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;
    @NotNull(message = "El año es obligatorio y debe ser numérico")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900")
    @Max(value = 2100, message = "El año debe ser menor o igual a 2100")
    private Integer anio;
    @NotNull(message = "El color es obligatorio")
    private ColorVehiculo color;
    @NotBlank(message = "El nombre del propietario es obligatorio")
    private String propietario;
}
