package co.com.claro.nodepoller.dao;

import co.com.claro.nodepoller.dto.NodeAlarmDTO;
import co.com.claro.nodepoller.dto.NodeDateDTO;
import co.com.claro.nodepoller.dto.NodeDTO;
import co.com.claro.nodepoller.entities.OpticalNodealarm;
import co.com.claro.nodepoller.util.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 *
 * @author juan.vega
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class NodesDAO {
    
    @PersistenceContext(unitName = "NodePollerPU")
    private EntityManager em;
    
    @Resource
    private UserTransaction utx;
    
    public NodesDAO() {
    }
    
    public NodeDTO findNodeByNameForward(String node) throws Exception {
        List<Object[]> list;
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT n.name, s.name, sl.name, s.title, c.name, sd.name, oc.name, re.name, pl.name, n.state_id ")
                    .append(" FROM optical_node n INNER JOIN (optical_slot s INNER JOIN optical_slottype sl ON s.slottype_id = sl.id) ")
                    .append(" ON n.fwdslot_id = s.id INNER JOIN (optical_chasis c INNER JOIN optical_platform pl ON c.platform_id = pl.id) ")
                    .append(" ON s.chasis_id = c.id INNER JOIN optical_sds sd ON c.sds_id = sd.id INNER JOIN optical_city oc ON sd.city_id = oc.id INNER JOIN optical_region re ")
                    .append(" ON oc.region_id = re.id  WHERE n.name = '").append(node).append("'");
            Query query = em.createNativeQuery(consulta.toString());
            list = query.getResultList();
            NodeDTO nodeDto = new NodeDTO();
            for (Object[] obj : list) {
                nodeDto.setNodo(String.valueOf(obj[0]));
                nodeDto.setSlot(String.valueOf(obj[1]));
                nodeDto.setTypeSlot(String.valueOf(obj[2]));
                nodeDto.setMarcacion(String.valueOf(obj[3]));
                nodeDto.setChasis(String.valueOf(obj[4]));
                nodeDto.setSds(String.valueOf(obj[5]));
                nodeDto.setCity(String.valueOf(obj[6]));
                nodeDto.setRegion(String.valueOf(obj[7]));
                nodeDto.setPlataforma(String.valueOf(obj[8]));
                if ("1".equals(String.valueOf(obj[9]))) {
                    nodeDto.setEstado(Constants.IMAGE_NODE_UP);
                } else {
                    nodeDto.setEstado(Constants.IMAGE_NODE_DOWN);
                }
            }
            consulta.delete(0, consulta.length());
            consulta.append("SELECT tr.name,trm.name FROM optical_node nod INNER JOIN optical_transponder tr ON nod.transponder_id=tr.id "
                    + "INNER JOIN optical_transponder_model trm ON tr.model_id=trm.id WHERE nod.name = '").append(node).append("'");
            query = em.createNativeQuery(consulta.toString());
            list = query.getResultList();
            for (Object[] obj : list) {
                nodeDto.setTransponder(String.valueOf(obj[0]));
                nodeDto.setModelo(String.valueOf(obj[1]));
            }
            return nodeDto;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findNodeByNameForward: Error: " + e.getMessage());
        }
    }
    
    public NodeDTO findNodeByNameRetorno(String node) throws Exception {
        List<Object[]> list;
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT n.name AS Nodo, s.name AS Slot, sl.name AS TypeSlot, s.title AS Marcacion, ")
                    .append("c.name AS Chasis, sd.name AS SDS, oc.name AS City, re.name AS Region, pl.name AS Plataforma, n.state_id ")
                    .append("FROM optical_node n INNER JOIN (optical_slot s INNER JOIN optical_slottype sl ON s.slottype_id = sl.id) ")
                    .append("ON n.retslot_id = s.id INNER JOIN (optical_chasis c INNER JOIN optical_platform pl ON c.platform_id = pl.id) ")
                    .append("ON s.chasis_id = c.id INNER JOIN optical_sds sd ON c.sds_id = sd.id INNER JOIN optical_city oc ON sd.city_id = oc.id ")
                    .append("INNER JOIN optical_region re ON oc.region_id = re.id  WHERE n.name = '").append(node).append("'");
            Query query = em.createNativeQuery(consulta.toString());
            list = query.getResultList();
            NodeDTO nodeDto = new NodeDTO();
            for (Object[] obj : list) {
                nodeDto.setNodo(String.valueOf(obj[0]));
                nodeDto.setSlot(String.valueOf(obj[1]));
                nodeDto.setTypeSlot(String.valueOf(obj[2]));
                nodeDto.setMarcacion(String.valueOf(obj[3]));
                nodeDto.setChasis(String.valueOf(obj[4]));
                nodeDto.setSds(String.valueOf(obj[5]));
                nodeDto.setCity(String.valueOf(obj[6]));
                nodeDto.setRegion(String.valueOf(obj[7]));
                nodeDto.setPlataforma(String.valueOf(obj[8]));
                if ("1".equals(String.valueOf(obj[9]))) {
                    nodeDto.setEstado(Constants.IMAGE_NODE_UP);
                } else {
                    nodeDto.setEstado(Constants.IMAGE_NODE_DOWN);
                }
            }
            return nodeDto;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findNodeByNameRetorno: Error: " + e.getMessage());
        }
    }
    
    public ArrayList<NodeDateDTO> findDateByNameForward(String node) throws Exception {
        List<Object[]> list;
        ArrayList<NodeDateDTO> listNodeDateDto;
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT sl.created, sl.inrf, sl.outputpower, sl.power1, sl.power2, sl.arop, sl.edfapower ")
                    .append("FROM optical_node n INNER JOIN optical_slot s ON n.fwdslot_id = s.id ")
                    .append("INNER JOIN optical_slotdata sl ON s.id = sl.slot_id WHERE n.name = '").append(node).append("' ORDER BY sl.created DESC");
            Query query = em.createNativeQuery(consulta.toString());
            list = query.getResultList();
            listNodeDateDto = new ArrayList<>();
            for (Object[] obj : list) {
                NodeDateDTO nodeDateDto = new NodeDateDTO();
                nodeDateDto.setFecha(String.valueOf(obj[0]).trim());
                nodeDateDto.setInrf(String.valueOf(obj[1]).trim());
                nodeDateDto.setOurputpower(String.valueOf(obj[2]).trim());
                listNodeDateDto.add(nodeDateDto);
            }
            return listNodeDateDto;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findDateByNameForward: Error: " + e.getMessage());
        }
    }
    
    public ArrayList<NodeDateDTO> findDateByNameRetornoAurora(String node) throws Exception {
        List<Object[]> list;
        ArrayList<NodeDateDTO> listNodeDateDto;
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT sl.created, sl.inrf, sl.outputpower, sl.power1, sl.power2, sl.arop, sl.edfapower ")
                    .append("FROM optical_node n INNER JOIN optical_slot s ON n.retslot_id = s.id ")
                    .append("INNER JOIN optical_slotdata sl ON s.id = sl.slot_id WHERE n.name = '").append(node).append("' ORDER BY sl.created DESC");
            Query query = em.createNativeQuery(consulta.toString());
            list = query.getResultList();
            listNodeDateDto = new ArrayList<>();
            for (Object[] obj : list) {
                NodeDateDTO nodeDateDto = new NodeDateDTO();
                nodeDateDto.setFecha(String.valueOf(obj[0]).trim());
                nodeDateDto.setPower1(String.valueOf(obj[3]).trim());
                nodeDateDto.setPower2(String.valueOf(obj[5]).trim());
                listNodeDateDto.add(nodeDateDto);
            }
            return listNodeDateDto;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findDateByNameRetornoAurora: Error: " + e.getMessage());
        }
    }
    
    public ArrayList<NodeDateDTO> findDateByNameRetornoRosa(String node) throws Exception {
        List<Object[]> list;
        ArrayList<NodeDateDTO> listNodeDateDto;
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT sl.created, sl.inrf, sl.outputpower, sl.power1, sl.power2, sl.arop, sl.edfapower ")
                    .append("FROM optical_node n INNER JOIN optical_slot s ON n.retslot_id = s.id ")
                    .append("INNER JOIN optical_slotdata sl ON s.id = sl.slot_id WHERE n.name = '").append(node).append("' ORDER BY sl.created DESC");
            Query query = em.createNativeQuery(consulta.toString());
            list = query.getResultList();
            listNodeDateDto = new ArrayList<>();
            for (Object[] obj : list) {
                NodeDateDTO nodeDateDto = new NodeDateDTO();
                nodeDateDto.setFecha(String.valueOf(obj[0]).trim());
                nodeDateDto.setPower1(String.valueOf(obj[3]).trim());
                nodeDateDto.setPower2(String.valueOf(obj[4]).trim());
                listNodeDateDto.add(nodeDateDto);
            }
            return listNodeDateDto;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findDateByNameRetornoRosa: Error: " + e.getMessage());
        }
    }
    
    public List<Object[]> findTreeNodes() throws Exception {
        List<Object[]> list;
        try {
            String consulta = "SELECT * FROM optical_tree u";
            Query query = em.createNativeQuery(consulta);
            list = query.getResultList();
            return list;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findTreeNodes: Error: " + e.getMessage());
        }
    }
    
    public List<NodeAlarmDTO> findAlarm() throws Exception {
        List<OpticalNodealarm> list;
        List<NodeAlarmDTO> alarm = new ArrayList<>();
        try {
            list = findAll();
            for (OpticalNodealarm var : list) {
                if (var.getAck() == 2) {
                    NodeAlarmDTO nodeAlarmDTO = new NodeAlarmDTO();
                    nodeAlarmDTO.setFecha(toFormatDate(var.getCreated()));
                    if (var.getLevelId() != null) {
                        nodeAlarmDTO.setAlarma(String.valueOf(var.getLevelId().getName()));
                    }
                    nodeAlarmDTO.setNodo(String.valueOf(var.getNodeId()));
                    //nodeAlarmDTO.setId_node(var.getNodeId().getId());
                    nodeAlarmDTO.setChasis(var.getChasisName());
                    nodeAlarmDTO.setPlataforma(var.getPlatformName());
                    nodeAlarmDTO.setSds(var.getSdsName());
                    nodeAlarmDTO.setCiudad(var.getCityName());
                    nodeAlarmDTO.setRegional(var.getRegionName());
                    nodeAlarmDTO.setId_Entity(var.getId());
                    nodeAlarmDTO.setTypeAlarm(var.getAlarmtypeId().getName());
                    alarm.add(nodeAlarmDTO);
                }
            }
            return alarm;
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findAlarm: Error: " + e.getMessage());
        }
    }
    
    public void updateAlarm(NodeAlarmDTO nodeAlarmDTO) throws Exception {
        try {
            OpticalNodealarm opticalNodealarm = em.find(OpticalNodealarm.class, nodeAlarmDTO.getId_Entity());
            opticalNodealarm.setAck(1);
            utx.begin();
            em.merge(opticalNodealarm);
            utx.commit();
        } catch (Exception e) {
            String error;
            try {
                utx.rollback();
            } catch (SystemException e1) {
                error = e1.getMessage();
            }
            throw new IllegalStateException("Ocurrió un error al actualizar la entidad.");
        }
    }
    
    public int countNodes(String id) throws Exception {
        List<Object[]> list;
        try {
            StringBuilder consulta = new StringBuilder();
            consulta.append("SELECT COUNT(*), state_id FROM optical_node WHERE state_id = ").append(id).append(" GROUP BY state_id");
            list = em.createNativeQuery(consulta.toString()).getResultList();
            Object[] obj = list.get(0);
            return Integer.parseInt(String.valueOf(obj[0]));
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: countNodes: Error: " + e.getMessage());
        }
    }
    
    private String toFormatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT);
        return format.format(date);
    }
    
    private List<OpticalNodealarm> findAll() throws Exception {
        try {
            return em.createNamedQuery("OpticalNodealarm.findAll").getResultList();
        } catch (Exception e) {
            throw new Exception("Centurión: NodesDAO: findAll: Error: " + e.getMessage());
        }
    }
}
