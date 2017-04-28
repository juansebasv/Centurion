package co.com.claro.nodepoller.dto;

import java.io.Serializable;

/**
 *
 * @author juan.vega
 */
public class NodeAlarmDTO implements Serializable {

    private static final long serialVersionUID = -7019236761313764288L;

    private String fecha;
    private String alarma;
    private String nodo;
    private int id_node;
    private String chasis;
    private String plataforma;
    private String sds;
    private String ciudad;
    private String regional;
    private int id_Entity;
    private String typeAlarm;

    public NodeAlarmDTO() {
    }

    public NodeAlarmDTO(String fecha, String alarma, String nodo, int id_node, String chasis, String plataforma, String sds, String ciudad, String regional, int id_Entity, String typeAlarm) {
        this.fecha = fecha;
        this.alarma = alarma;
        this.nodo = nodo;
        this.id_node = id_node;
        this.chasis = chasis;
        this.plataforma = plataforma;
        this.sds = sds;
        this.ciudad = ciudad;
        this.regional = regional;
        this.id_Entity = id_Entity;
        this.typeAlarm = typeAlarm;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAlarma() {
        return alarma;
    }

    public void setAlarma(String alarma) {
        this.alarma = alarma;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public int getId_node() {
        return id_node;
    }

    public void setId_node(int id_node) {
        this.id_node = id_node;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getSds() {
        return sds;
    }

    public void setSds(String sds) {
        this.sds = sds;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getRegional() {
        return regional;
    }

    public void setRegional(String regional) {
        this.regional = regional;
    }

    public int getId_Entity() {
        return id_Entity;
    }

    public void setId_Entity(int id_Entity) {
        this.id_Entity = id_Entity;
    }

    public String getTypeAlarm() {
        return typeAlarm;
    }

    public void setTypeAlarm(String typeAlarm) {
        this.typeAlarm = typeAlarm;
    }

}
