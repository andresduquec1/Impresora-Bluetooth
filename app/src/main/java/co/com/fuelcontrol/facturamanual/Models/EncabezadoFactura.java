package co.com.fuelcontrol.facturamanual.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class EncabezadoFactura extends RealmObject{

    @PrimaryKey
    private int id;
    private String nombreEmpresa;
    private String NombreEds;
    private String nitEds;
    private String telefono;
    private String direccion;
    private String ciudad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getNombreEds() {
        return NombreEds;
    }

    public void setNombreEds(String nombreEds) {
        NombreEds = nombreEds;
    }

    public String getNitEds() {
        return nitEds;
    }

    public void setNitEds(String nitEds) {
        this.nitEds = nitEds;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

}
