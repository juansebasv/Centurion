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
@Table(name = "optical_transponder_model")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalTransponderModel.findAll", query = "SELECT o FROM OpticalTransponderModel o")
    , @NamedQuery(name = "OpticalTransponderModel.findById", query = "SELECT o FROM OpticalTransponderModel o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalTransponderModel.findByName", query = "SELECT o FROM OpticalTransponderModel o WHERE o.name = :name")})
public class OpticalTransponderModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 128)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "modelId")
    private Collection<OpticalTransponder> opticalTransponderCollection;

    public OpticalTransponderModel() {
    }

    public OpticalTransponderModel(Integer id) {
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
    public Collection<OpticalTransponder> getOpticalTransponderCollection() {
        return opticalTransponderCollection;
    }

    public void setOpticalTransponderCollection(Collection<OpticalTransponder> opticalTransponderCollection) {
        this.opticalTransponderCollection = opticalTransponderCollection;
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
        if (!(object instanceof OpticalTransponderModel)) {
            return false;
        }
        OpticalTransponderModel other = (OpticalTransponderModel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalTransponderModel[ id=" + id + " ]";
    }
    
}
