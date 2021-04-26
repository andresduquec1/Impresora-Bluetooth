package co.com.fuelcontrol.facturamanual.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Consecutivo extends RealmObject {

    @PrimaryKey
    int id;
    int consecutivo;

    public Consecutivo() {
    }

    public Consecutivo(int id, int consecutivo) {
        this.id = id;
        this.consecutivo = consecutivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(int consecutivo) {
        this.consecutivo = consecutivo;
    }
}
