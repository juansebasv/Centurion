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
@Table(name = "optical_level")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalLevel.findAll", query = "SELECT o FROM OpticalLevel o")
    , @NamedQuery(name = "OpticalLevel.findById", query = "SELECT o FROM OpticalLevel o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalLevel.findByName", query = "SELECT o FROM OpticalLevel o WHERE o.name = :name")})
public class OpticalLevel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 64)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "levelId")
    private Collection<OpticalNodealarm> opticalNodealarmCollection;

    public OpticalLevel() {
    }

    public OpticalLevel(Integer id) {
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
    public Collection<OpticalNodealarm> getOpticalNodealarmCollection() {
        return opticalNodealarmCollection;
    }

    public void setOpticalNodealarmCollection(Collection<OpticalNodealarm> opticalNodealarmCollection) {
        this.opticalNodealarmCollection = opticalNodealarmCollection;
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
        if (!(object instanceof OpticalLevel)) {
            return false;
        }
        OpticalLevel other = (OpticalLevel) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalLevel[ id=" + id + " ]";
    }
    
}
