package pe.edu.upeu.padronvehicular.enums;

import lombok.Getter;

@Getter
public enum ColorVehiculo {
    BLANCO("Blanco"),
    NEGRO("Negro"),
    GRIS("Gris"),
    PLATA("Plata"),
    ROJO("Rojo"),
    AZUL("Azul"),
    VERDE("Verde"),
    AMARILLO("Amarillo"),
    OTRO("Otro");

    String descripcion;
    ColorVehiculo(String descripcion){
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
