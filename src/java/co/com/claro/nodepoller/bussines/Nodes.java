package co.com.claro.nodepoller.bussines;

import co.com.claro.nodepoller.boundary.NodesBoundary;
import co.com.claro.nodepoller.dto.NodeAlarmDTO;
import co.com.claro.nodepoller.dto.NodeDTO;
import co.com.claro.nodepoller.dto.NodeDateDTO;
import co.com.claro.nodepoller.util.Constants;
import co.com.claro.nodepoller.util.CustomTask;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.model.TreeNode;

/**
 *
 * @author juan.vega
 */
public final class Nodes implements Serializable {

    NodesBoundary nodesBoundary = lookupNodesBoundaryBean();

    private static volatile Nodes node;
    private String nameNode;
    private String searchNameNode;
    private String fwdpwr;
    private String fwdinrf;
    private String retpwr;
    private String retarop;
    private NodeDTO nodeDtoR;
    private NodeDTO nodeDtoF;
    private ArrayList<NodeDateDTO> listNodeF;
    private ArrayList<NodeDateDTO> listNodeR;
    private ArrayList<NodeAlarmDTO> listAlarm;
    private TreeNode root;
    private TreeNode selectedNode;
    private List<NodeAlarmDTO> selectNodeAlarmDTO;
    private int nodesUp;
    private int nodesDown;
    private Timer timer;
    private CustomTask task;

    private Nodes() {
        nodeDtoF = new NodeDTO();
        nodeDtoR = new NodeDTO();
        listNodeF = new ArrayList<>();
        listNodeR = new ArrayList<>();
        listAlarm = new ArrayList<>();
        timer = new Timer();
        task = new CustomTask(180);
        timer.schedule(task, 0, 1000);
    }

    /**
     * Procedimiento crear una sola instancia (Singleton)
     *
     * @return
     */
    public static synchronized Nodes getInstance() {
        if (node == null) {
            node = new Nodes();
        }
        return node;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("No es posible clonar la instancia del singleton Nodes.\n");
    }

    /**
     * Proceso para recargar la vista cada 10 minutos (600 seg)
     */
    public void reLoad() {
        task.cancel();
        task = new CustomTask(180);
        timer.cancel();
        timer = new Timer();
        timer.schedule(task, 0, 1000);
        loadAlarms();
    }

    /**
     * Procedimiento para buscar los los datos de cada slot por su nombre
     */
    public void findNodeByName() {
        try {
            nodeDtoF = nodesBoundary.findNodeByNameForward(nameNode);
            nodeDtoR = nodesBoundary.findNodeByNameRetorno(nameNode);

            if (nodeDtoF.getCity().equals("---")) {
                nodeDtoF.setCity(nodeDtoR.getCity());
            }
            if (nodeDtoF.getRegion().equals("---")) {
                nodeDtoF.setRegion(nodeDtoR.getRegion());
            }
            if (nodeDtoF.getPlataforma().equals("---")) {
                nodeDtoF.setPlataforma(nodeDtoR.getPlataforma());
            }
            if (nodeDtoF.getSds().equals("---")) {
                nodeDtoF.setSds(nodeDtoR.getSds());
            }

            listNodeF = nodesBoundary.findDateByNameForward(nameNode);
            if ("aurora".equals(nodeDtoF.getPlataforma().toLowerCase())) {
                listNodeR = nodesBoundary.findDateByNameRetornoAurora(nameNode);
            } else {
                listNodeR = nodesBoundary.findDateByNameRetornoRosa(nameNode);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Codigo Seleccionado ", nameNode));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Código No Encontrado"));
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, new StringBuilder(Constants.BODY_EXCEPTION).append(getClass().getName()).append(": findNodeByName: Error buscando el nodo por el nombre: ").append(nameNode).toString(), e);
        }
    }

    /**
     * Procedimiento para exportar a excel la tabla de los valores del slot
     *
     * @param document
     */
    public void postProcessXLS(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        for (org.apache.poi.ss.usermodel.Row row : sheet) {
            for (org.apache.poi.ss.usermodel.Cell cell : row) {
                if (cell.getColumnIndex() == 9) {
                    cell.setCellValue("");
                } else if (cell.getStringCellValue().toUpperCase().contains("<CENTER>")) {
                    cell.setCellValue(cell.getStringCellValue().toUpperCase().substring(8, cell.getStringCellValue().length() - 9));
                } else {
                    cell.setCellValue(cell.getStringCellValue().toUpperCase());
                }
            }
        }
    }

    /**
     * Armar el arbol en la aplicación con los datos consultados
     */
    /*public void buildTree() {
        try {
            ArrayList<String> wordsRegion = new ArrayList<>();
            ArrayList<String> wordsCiudad = new ArrayList<>();
            ArrayList<String> wordsSds = new ArrayList<>();
            ArrayList<String> wordsChasis = new ArrayList<>();
            ArrayList<String> wordsNodo = new ArrayList<>();

            List<Object[]> list = nodesBoundary.findTreeNodes();
            this.root = new DefaultTreeNode(new Document("Nodo", "--", "--"), null);
            TreeNode regiones = null;
            TreeNode ciudades = null;
            TreeNode sds = null;
            TreeNode chasis = null;
            TreeNode nodes = null;
            for (Object[] obj : list) {
                if (!String.valueOf(obj[0]).contains("---") && !String.valueOf(obj[0]).contains("NDS")) {
                    if (!wordsRegion.contains(String.valueOf(obj[0]))) {
                        wordsRegion.add(String.valueOf(obj[0]));
                        if (String.valueOf(obj[8]).equals("1")) {
                            regiones = new DefaultTreeNode(new Document(String.valueOf(obj[0]), Constants.IMAGE_NODE_UP, "2"), this.root);
                        } else {
                            regiones = new DefaultTreeNode(new Document(String.valueOf(obj[0]), Constants.IMAGE_NODE_DOWN, "1"), this.root);
                        }
                    }
                    if (!wordsCiudad.contains(String.valueOf(obj[1]))) {
                        wordsCiudad.add(String.valueOf(obj[1]));
                        if (String.valueOf(obj[8]).equals("1")) {
                            ciudades = new DefaultTreeNode(new Document(String.valueOf(obj[1]), Constants.IMAGE_NODE_UP, "2"), regiones);
                        } else {
                            ciudades = new DefaultTreeNode(new Document(String.valueOf(obj[1]), Constants.IMAGE_NODE_DOWN, "1"), regiones);
                        }
                    }
                    if (!wordsSds.contains(String.valueOf(obj[2]))) {
                        wordsSds.add(String.valueOf(obj[2]));
                        if (String.valueOf(obj[9]).equals("1")) {
                            sds = new DefaultTreeNode(new Document(String.valueOf(obj[2]), Constants.IMAGE_NODE_UP, "2"), ciudades);
                        } else {
                            sds = new DefaultTreeNode(new Document(String.valueOf(obj[2]), Constants.IMAGE_NODE_DOWN, "1"), ciudades);
                        }
                        wordsChasis.clear();
                    }
                    if (!wordsChasis.contains(String.valueOf(obj[3]))) {
                        wordsChasis.add(String.valueOf(obj[3]));
                        if (String.valueOf(obj[10]).equals("1")) {
                            chasis = new DefaultTreeNode(new Document(String.valueOf(obj[3]), Constants.IMAGE_NODE_UP, "2"), sds);
                        } else {
                            chasis = new DefaultTreeNode(new Document(String.valueOf(obj[3]), Constants.IMAGE_NODE_DOWN, "1"), sds);
                        }
                        wordsNodo.clear();
                    }
                    if (!wordsNodo.contains(String.valueOf(obj[5]))) {
                        wordsNodo.add(String.valueOf(obj[5]));
                        if (String.valueOf(obj[6]).equals("1")) {
                            nodes = new DefaultTreeNode("nodo", new Document(String.valueOf(obj[5]), Constants.IMAGE_NODE_UP, "2"), chasis);
                        } else {
                            nodes = new DefaultTreeNode("nodo", new Document(String.valueOf(obj[5]), Constants.IMAGE_NODE_DOWN, "1"), chasis);
                        }
                    }
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cargando Arbol"));
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, new StringBuilder(Constants.BODY_EXCEPTION).append(getClass().getName()).append(": buildTree: Error armando el arbol de la aplicación").toString(), e);
        }
    }*/
    /**
     * Procedimiento para buscar la información del nodo
     *
     * @param tree
     */
    public void findInfoNode(String tree) {
        this.nameNode = tree;
        findNodeByName();
    }

    /**
     * Procedimiento para buscar la información del nodo
     */
    public void searchNode() {
        this.nameNode = searchNameNode.trim();
        findNodeByName();
        this.searchNameNode = "";
    }

    /**
     * Procedimiento para cargar todas las alarmas en la tabla
     */
    public void loadAlarms() {
        try {
            listAlarm.clear();
            listAlarm.addAll(nodesBoundary.findAllAlarms());
            nodesUp = nodesBoundary.countNodesUp();
            nodesDown = nodesBoundary.coutnNodesDown();
            Collections.sort(listAlarm, new Comparator<NodeAlarmDTO>() {
                @Override
                public int compare(NodeAlarmDTO p1, NodeAlarmDTO p2) {
                    SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
                    try {
                        Date date1 = sdf.parse(p1.getFecha());
                        Date date2 = sdf.parse(p2.getFecha());
                        if (date1.compareTo(date2) < 0) {
                            return 1;
                        }
                        if (date1.compareTo(date2) > 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(Nodes.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return 0;
                }
            });
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Cargando Alarmas"));
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, new StringBuilder(Constants.BODY_EXCEPTION).append(getClass().getName()).append(": loadAlarms: Error cargando las alarmas").toString(), e);
        }
    }

    /**
     * Procedimiento para eliminar alarmas de la tabla
     */
    public void deleteAlarms() {
        try {
            for (NodeAlarmDTO varNodeAlarmDTO : selectNodeAlarmDTO) {
                if (listAlarm.contains(varNodeAlarmDTO)) {
                    listAlarm.remove(varNodeAlarmDTO);
                    nodesBoundary.updateAlarm(varNodeAlarmDTO);
                }
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Exito", "Alarmas Eliminadas"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Eliminando Alarmnas"));
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, new StringBuilder(Constants.BODY_EXCEPTION).append(getClass().getName()).append(": deleteAlarms: Error al eliminar alarmas").toString(), e);
        }
    }

    /**
     * Procedimiento para validar el estado del nodo y regresar la imagen
     *
     * @param num
     * @return
     */
    public String stateNodes(int num) {
        switch (num) {
            case 1:
                return Constants.IMAGE_NODE_UP;
            case 2:
                return Constants.IMAGE_NODE_DOWN;
            default:
                return Constants.IMAGE_NODE_DOWN;
        }
    }

    /**
     * Procedimiento para buscar los niveles del nodo en tiempo real
     */
    public void findNodeInRealTime() {
        try {
            this.nameNode = searchNameNode.trim();
            this.fwdpwr = "---";
            this.fwdinrf = "---";
            this.retpwr = "---";
            this.retarop = "---";
            Pattern pat = Pattern.compile("([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|([^\\|]*)\\|");
            // System.out.println("ENTRO----1: " + nameNode);
            Matcher mat = pat.matcher(getLevels(this.nameNode));
            // System.out.println("ENTRO----2");
            if (mat.find()) {
                //    System.out.println("ENTRO----3");
                this.fwdpwr = mat.group(10);
                this.fwdinrf = mat.group(11);
                this.retpwr = mat.group(12);
                this.retarop = mat.group(13);
            }
            //System.out.println("--------------------------- ENTRO: " + this.searchNameNode);
            this.searchNameNode = "";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Consultando Niveles"));
            Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, new StringBuilder(Constants.BODY_EXCEPTION).append(getClass().getName()).append(": findNodeInRealTime: Error Consultando niveles en tiempo real").toString(), e);
        }
    }

    /*-------------------------------------------------------------------------------------------
    -------------------------------------| Métodos Get & Set |-----------------------------------
    --------------------------------------------------------------------------------------------*/
    public String getNameNode() {
        return nameNode;
    }

    public void setNameNode(String nameNode) {
        this.nameNode = nameNode;
    }

    public NodeDTO getNodeDtoR() {
        return nodeDtoR;
    }

    public void setNodeDtoR(NodeDTO nodeDtoR) {
        this.nodeDtoR = nodeDtoR;
    }

    public NodeDTO getNodeDtoF() {
        return nodeDtoF;
    }

    public void setNodeDtoF(NodeDTO nodeDtoF) {
        this.nodeDtoF = nodeDtoF;
    }

    public ArrayList<NodeDateDTO> getListNodeF() {
        return listNodeF;
    }

    public void setListNodeF(ArrayList<NodeDateDTO> listNodeF) {
        this.listNodeF = listNodeF;
    }

    public ArrayList<NodeDateDTO> getListNodeR() {
        return listNodeR;
    }

    public void setListNodeR(ArrayList<NodeDateDTO> listNodeR) {
        this.listNodeR = listNodeR;
    }

    public ArrayList<NodeAlarmDTO> getListAlarm() {
        return listAlarm;
    }

    public void setListAlarm(ArrayList<NodeAlarmDTO> listAlarm) {
        this.listAlarm = listAlarm;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<NodeAlarmDTO> getSelectNodeAlarmDTO() {
        return selectNodeAlarmDTO;
    }

    public void setSelectNodeAlarmDTO(List<NodeAlarmDTO> selectNodeAlarmDTO) {
        this.selectNodeAlarmDTO = selectNodeAlarmDTO;
    }

    public String getSearchNameNode() {
        return searchNameNode;
    }

    public void setSearchNameNode(String searchNameNode) {
        this.searchNameNode = searchNameNode;
    }

    public int getNodesUp() {
        return nodesUp;
    }

    public void setNodesUp(int nodesUp) {
        this.nodesUp = nodesUp;
    }

    public int getNodesDown() {
        return nodesDown;
    }

    public void setNodesDown(int nodesDown) {
        this.nodesDown = nodesDown;
    }

    public CustomTask getTask() {
        return task;
    }

    public void setTask(CustomTask task) {
        this.task = task;
    }

    public String getFwdpwr() {
        return fwdpwr;
    }

    public void setFwdpwr(String fwdpwr) {
        this.fwdpwr = fwdpwr;
    }

    public String getFwdinrf() {
        return fwdinrf;
    }

    public void setFwdinrf(String fwdinrf) {
        this.fwdinrf = fwdinrf;
    }

    public String getRetpwr() {
        return retpwr;
    }

    public void setRetpwr(String retpwr) {
        this.retpwr = retpwr;
    }

    public String getRetarop() {
        return retarop;
    }

    public void setRetarop(String retarop) {
        this.retarop = retarop;
    }

    /**
     * Procedimiento para consumir el EJB de la clase
     *
     * @return
     */
    private NodesBoundary lookupNodesBoundaryBean() {
        try {
            Context c = new InitialContext();
            return (NodesBoundary) c.lookup("java:global/NodePoller/NodesBoundary!co.com.claro.nodepoller.boundary.NodesBoundary");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    /**
     * Procedimiento para consumir el WebService que consulta los niveles del
     * nodo en tiempo rela
     *
     * @param node
     * @return
     */
    private static String getLevels(java.lang.String node) {
        co.com.claro.opticalws.OpticalWSService service = new co.com.claro.opticalws.OpticalWSService();
        co.com.claro.opticalws.OpticalWS port = service.getOpticalWS();
        return port.getLevels(node);
    }
}
