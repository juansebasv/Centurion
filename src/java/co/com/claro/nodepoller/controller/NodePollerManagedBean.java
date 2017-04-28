package co.com.claro.nodepoller.controller;

import co.com.claro.nodepoller.bussines.Nodes;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author juan.vega
 */
@ManagedBean
@SessionScoped
public class NodePollerManagedBean implements Serializable {

    private static final long serialVersionUID = -527664281573828121L;
    
    private Nodes node;

    @PostConstruct
    public void init() {
        node = Nodes.getInstance();
        //node.buildTree();
        node.loadAlarms();
    }

    public Nodes getNode() {
        return node;
    }

    public void setNode(Nodes node) {
        this.node = node;
    }

}
