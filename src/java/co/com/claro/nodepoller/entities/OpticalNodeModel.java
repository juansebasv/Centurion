/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.nodepoller.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ECF3758A
 */
@Entity
@Table(name = "optical_node_model")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalNodeModel.findAll", query = "SELECT o FROM OpticalNodeModel o")
    , @NamedQuery(name = "OpticalNodeModel.findById", query = "SELECT o FROM OpticalNodeModel o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalNodeModel.findByName", query = "SELECT o FROM OpticalNodeModel o WHERE o.name = :name")})
public class OpticalNodeModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "name")
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "modelId")
    private Collection<OpticalNode> opticalNodeCollection;

    public OpticalNodeModel() {
    }

    public OpticalNodeModel(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<OpticalNode> getOpticalNodeCollection() {
        return opticalNodeCollection;
    }

    public void setOpticalNodeCollection(Collection<OpticalNode> opticalNodeCollection) {
        this.opticalNodeCollection = opticalNodeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OpticalNodeModel)) {
            return false;
        }
        OpticalNodeModel other = (OpticalNodeModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalNodeModel[ id=" + id + " ]";
    }
    
}
