package co.com.claro.nodepoller.util;

import java.awt.Color;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author juan.vega
 */
public class BaseManagedBean implements Serializable {

    private static final long serialVersionUID = 5565738647204144915L;

    public BaseManagedBean() {

    }

    /**
     * Lee Array de messages para que sean mostrados en el contexto
     *
     * @param messages
     */
    protected void showMessages(List<FacesMessage> messages) {
        for (FacesMessage message : messages) {
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    /**
     * Abre en la vista del usuario el dialog que sea enviado por parametro
     *
     * @param nameDialog
     */
    protected void openDialog(String nameDialog) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('" + nameDialog + "').show();");
    }

    /**
     * Adiciona un mensage para que sea mostrado en el contexto de la vista del
     * usuario
     *
     * @param type
     * @param summary
     * @param detail
     */
    protected void addMessage(FacesMessage.Severity type, String summary, String detail) {
        FacesMessage msg = new FacesMessage(type, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Devuelve una fecha en el formato definido en el metodo
     *
     * @param date
     * @return
     */
    public String getFormatDate(Date date) {
        SimpleDateFormat dtformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dtformat.format(date.getTime());
    }

    /**
     * Devuelve una parte de la fecha segun el parametro definido
     *
     * @param date
     * @param part
     * @return
     */
    public String getFormatPartDate(Date date, String part) {
        SimpleDateFormat dtformat;
        if (part.equals("hour")) {
            dtformat = new SimpleDateFormat("HH:mm:ss");
        } else {
            dtformat = new SimpleDateFormat("yyyy-MM-dd");
        }
        return dtformat.format(date.getTime());
    }

    /**
     * Generacion aleatoria de colores
     *
     * @return
     */
    public String generateColorHex() {
        int red = (int) ((Math.random() * 255) + 1);
        int green = (int) ((Math.random() * 255) + 1);
        int blue = (int) ((Math.random() * 255) + 1);
        Color color = new Color(red, green, blue);
        int RGB = color.getRGB(); //FF6973
        return Integer.toHexString(RGB).toUpperCase().substring(0, 6);
    }

}
