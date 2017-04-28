package co.com.claro.nodepoller.util;

/**
 *
 * @author juan.vega
 */
public class Constants {

    //public static final String NQ_NODEPOLLERFINDNODES = "SELECT n.name, s.name, s.title, c.name, sd.name, oc.name, re.name FROM optical_node n INNER JOIN optical_slot s ON n.fwdslot_id = s.id INNER JOIN optical_chasis c ON s.chasis_id = c.id INNER JOIN optical_sds sd ON c.sds_id = sd.id INNER JOIN optical_city oc ON sd.city_id = oc.id INNER JOIN optical_region re ON oc.region_id = re.id  WHERE n.name = :node";
    public static final String IMAGE_NODE_UP = "./resources/img/state_green.png";
    public static final String IMAGE_NODE_DOWN = "./resources/img/state_red.png";
    public static final String IMAGE_NODE_UNMANAGED = "./resources/img/state_grey.png";
    public static final String NUM_COM = "2";
    public static final String BODY_EXCEPTION = "Claro: Centurion: ";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
