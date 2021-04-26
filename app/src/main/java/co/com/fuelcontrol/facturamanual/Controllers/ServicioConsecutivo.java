package co.com.fuelcontrol.facturamanual.Controllers;

import co.com.fuelcontrol.facturamanual.Models.Consecutivo;
import io.realm.Realm;
import io.realm.RealmResults;

public class ServicioConsecutivo {

    private Realm realm;
    public ServicioConsecutivo(Realm realm){
        this.realm = realm;
    }

    public void crearConsecutivo(int id, int consecutivo){

        realm.beginTransaction();
        Consecutivo u = realm.createObject(Consecutivo.class,id);
        u.setConsecutivo(consecutivo);
        realm.commitTransaction();

    }

    public Consecutivo[] obtenerConsecutivos(){

        RealmResults<Consecutivo> results = realm.where(Consecutivo.class).findAll();
        return results.toArray(new Consecutivo[results.size()]);

    }

    public void actualizarConsecutivo(Consecutivo consecutivo, int numConsecutivo){

        realm.beginTransaction();
        consecutivo.setConsecutivo(numConsecutivo);
        realm.commitTransaction();

    }

    public Consecutivo obtenerConsecutivoPorId(int id){

        Consecutivo resultado = realm.where(Consecutivo.class).equalTo("id",id).findFirst();
        return resultado;

    }
}
