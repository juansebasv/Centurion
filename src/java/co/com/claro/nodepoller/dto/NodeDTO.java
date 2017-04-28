package co.com.claro.nodepoller.dto;

import java.io.Serializable;

/**
 *
 * @author juan.vega
 */
public class NodeDTO implements Serializable {

    private static final long serialVersionUID = 3287410169239063919L;

    private String nodo;
    private String slot;
    private String typeSlot;
    private String marcacion;
    private String chasis;
    private String sds;
    private String city;
    private String region;
    private String plataforma;
    private String estado;
    private String transponder;
    private String modelo;

    public NodeDTO() {
    }

    public NodeDTO(String nodo, String slot, String typeSlot, String marcacion, String chasis, String sds, String city, String region, String plataforma, String estado, String transponder, String modelo) {
        this.nodo = nodo;
        this.slot = slot;
        this.typeSlot = typeSlot;
        this.marcacion = marcacion;
        this.chasis = chasis;
        this.sds = sds;
        this.city = city;
        this.region = region;
        this.plataforma = plataforma;
        this.estado = estado;
        this.transponder = transponder;
        this.modelo = modelo;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getTypeSlot() {
        return typeSlot;
    }

    public void setTypeSlot(String typeSlot) {
        this.typeSlot = typeSlot;
    }

    public String getMarcacion() {
        return marcacion;
    }

    public void setMarcacion(String marcacion) {
        this.marcacion = marcacion;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getSds() {
        return sds;
    }

    public void setSds(String sds) {
        this.sds = sds;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTransponder() {
        return transponder;
    }

    public void setTransponder(String transponder) {
        this.transponder = transponder;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

}
