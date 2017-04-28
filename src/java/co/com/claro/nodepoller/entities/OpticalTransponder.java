/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.nodepoller.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ECF3758A
 */
@Entity
@Table(name = "optical_transponder")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalTransponder.findAll", query = "SELECT o FROM OpticalTransponder o")
    , @NamedQuery(name = "OpticalTransponder.findById", query = "SELECT o FROM OpticalTransponder o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalTransponder.findByName", query = "SELECT o FROM OpticalTransponder o WHERE o.name = :name")
    , @NamedQuery(name = "OpticalTransponder.findByStatus", query = "SELECT o FROM OpticalTransponder o WHERE o.status = :status")})
public class OpticalTransponder implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 128)
    @Column(name = "name")
    private String name;
    @Size(max = 16)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "transponderId")
    private Collection<OpticalNode> opticalNodeCollection;
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalTransponderModel modelId;

    public OpticalTransponder() {
    }

    public OpticalTransponder(Integer id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<OpticalNode> getOpticalNodeCollection() {
        return opticalNodeCollection;
    }

    public void setOpticalNodeCollection(Collection<OpticalNode> opticalNodeCollection) {
        this.opticalNodeCollection = opticalNodeCollection;
    }

    public OpticalTransponderModel getModelId() {
        return modelId;
    }

    public void setModelId(OpticalTransponderModel modelId) {
        this.modelId = modelId;
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
        if (!(object instanceof OpticalTransponder)) {
            return false;
        }
        OpticalTransponder other = (OpticalTransponder) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalTransponder[ id=" + id + " ]";
    }
    
}
