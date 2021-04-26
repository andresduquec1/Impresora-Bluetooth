package co.com.fuelcontrol.facturamanual.Models;


import static java.lang.Float.valueOf;

public class Factura {

    private String nombre;
    private String cedula;
    private String placa;
    private String kilometraje;
    private String horometro;
    private String volumen;
    private String ppu;
    private String producto;
    private int total;
    private String varios;

    public Factura() {
    }


    public Factura(String nombre, String cedula, String placa, String kilometraje, String horometro, String volumen, String ppu, String producto) {

        this.nombre = nombre;
        this.cedula = cedula;
        this.placa = placa;
        this.kilometraje = kilometraje;
        this.horometro = horometro;
        this.volumen = volumen;
        this.ppu = ppu;
        this.producto = producto;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getHorometro() {
        return horometro;
    }

    public void setHorometro(String horometro) {
        this.horometro = horometro;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getPpu() {
        return ppu;
    }

    public void setPpu(String ppu) {
        this.ppu = ppu;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getTotal() {
        try {
            int subtotal = (int) (valueOf(volumen) * valueOf(ppu));
            total = Math.round(subtotal);

        }catch (Exception ex){

        }
       return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getVarios() {
        varios = volumen+"--"+producto+"--"+ppu;
        return varios;
    }

    public void setVarios(String varios) {
        this.varios = varios;
    }
}
