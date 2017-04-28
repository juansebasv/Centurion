package co.com.claro.nodepoller.boundary;

import co.com.claro.nodepoller.dao.NodesDAO;
import co.com.claro.nodepoller.dto.NodeAlarmDTO;
import co.com.claro.nodepoller.dto.NodeDateDTO;
import co.com.claro.nodepoller.dto.NodeDTO;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author juan.vega
 */
@Stateless
public class NodesBoundary {

    @EJB
    private NodesDAO nodesDAO;

    public NodeDTO findNodeByNameForward(String node) throws Exception {
        return this.nodesDAO.findNodeByNameForward(node);
    }

    public NodeDTO findNodeByNameRetorno(String node) throws Exception {
        return this.nodesDAO.findNodeByNameRetorno(node);
    }

    public ArrayList<NodeDateDTO> findDateByNameForward(String node) throws Exception {
        return this.nodesDAO.findDateByNameForward(node);
    }

    public ArrayList<NodeDateDTO> findDateByNameRetornoAurora(String node) throws Exception {
        return this.nodesDAO.findDateByNameRetornoAurora(node);
    }

    public ArrayList<NodeDateDTO> findDateByNameRetornoRosa(String node) throws Exception {
        return this.nodesDAO.findDateByNameRetornoRosa(node);
    }

    public List<Object[]> findTreeNodes() throws Exception {
        return this.nodesDAO.findTreeNodes();
    }

    public List<NodeAlarmDTO> findAllAlarms() throws Exception {
        return this.nodesDAO.findAlarm();
    }

    public void updateAlarm(NodeAlarmDTO nodeAlarmDTO) throws Exception {
        this.nodesDAO.updateAlarm(nodeAlarmDTO);
    }

    public int countNodesUp() throws Exception {
        return this.nodesDAO.countNodes("1");
    }

    public int coutnNodesDown() throws Exception {
        return this.nodesDAO.countNodes("2");
    }
}
