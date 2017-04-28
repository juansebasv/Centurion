package co.com.claro.nodepoller.dto;

import java.io.Serializable;

/**
 *
 * @author juan.vega
 */
public class NodeDateDTO implements Serializable {

    private static final long serialVersionUID = 1905087343346403917L;

    private String fecha;
    private String inrf;
    private String ourputpower;
    private String power1;
    private String power2;
    private String poar;
    private String podr;

    public NodeDateDTO() {
    }

    public NodeDateDTO(String fecha, String inrf, String ourputpower, String power1, String power2, String poar, String podr) {
        this.fecha = fecha;
        this.inrf = inrf;
        this.ourputpower = ourputpower;
        this.power1 = power1;
        this.power2 = power2;
        this.poar = poar;
        this.podr = podr;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getInrf() {
        return inrf;
    }

    public void setInrf(String inrf) {
        this.inrf = inrf;
    }

    public String getOurputpower() {
        return ourputpower;
    }

    public void setOurputpower(String ourputpower) {
        this.ourputpower = ourputpower;
    }

    public String getPower1() {
        return power1;
    }

    public void setPower1(String power1) {
        this.power1 = power1;
    }

    public String getPower2() {
        return power2;
    }

    public void setPower2(String power2) {
        this.power2 = power2;
    }

    public String getPoar() {
        return poar;
    }

    public void setPoar(String poar) {
        this.poar = poar;
    }

    public String getPodr() {
        return podr;
    }

    public void setPodr(String podr) {
        this.podr = podr;
    }

}
