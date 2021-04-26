package co.com.fuelcontrol.facturamanual.Controllers;

import co.com.fuelcontrol.facturamanual.Models.EncabezadoFactura;
import io.realm.Realm;
import io.realm.RealmResults;

public class ServicioFactura {

    private Realm realm;
    public ServicioFactura(Realm realm){
        this.realm = realm;
    }

    //Obtener facturas
    public EncabezadoFactura[] obtenerFacturas(){

        RealmResults<EncabezadoFactura> results = realm.where(EncabezadoFactura.class).findAll();
        return results.toArray(new EncabezadoFactura[results.size()]);
    }

    public EncabezadoFactura obtenerFacturaPorId(int id){

        EncabezadoFactura resultado = realm.where(EncabezadoFactura.class).equalTo("id",id).findFirst();
        return resultado;

    }

    //Actualizar
    public void actualizarFactura(EncabezadoFactura encabezadoFactura, String nombreEmpresa, String nombreEds, String nitEds, String telefono, String direccion, String ciudad){

        realm.beginTransaction();
        encabezadoFactura.setNombreEmpresa(nombreEmpresa);
        encabezadoFactura.setNombreEds(nombreEds);
        encabezadoFactura.setNitEds(nitEds);
        encabezadoFactura.setTelefono(telefono);
        encabezadoFactura.setDireccion(direccion);
        encabezadoFactura.setCiudad(ciudad);
        realm.commitTransaction();

    }

    //Guardar factura
    public void crearFactura(int id, String nombreEmpresa, String nombreEds, String nitEds, String telefono, String direccion, String ciudad){

        realm.beginTransaction();
        EncabezadoFactura u = realm.createObject(EncabezadoFactura.class,id);
        u.setNombreEmpresa(nombreEmpresa);
        u.setNombreEds(nombreEds);
        u.setNitEds(nitEds);
        u.setTelefono(telefono);
        u.setDireccion(direccion);
        u.setCiudad(ciudad);
        realm.commitTransaction();

    }


    //Eliminar factura
   /* public void eliminarFactura(int id){

        EncabezadoFactura encabezadoFactura = obtenerFacturaPorId(id);
        realm.beginTransaction();
        encabezadoFactura.deleteFromRealm();
        realm.commitTransaction();
    }*/

}
