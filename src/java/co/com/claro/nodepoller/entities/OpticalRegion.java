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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ECF3758A
 */
@Entity
@Table(name = "optical_region")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalRegion.findAll", query = "SELECT o FROM OpticalRegion o")
    , @NamedQuery(name = "OpticalRegion.findById", query = "SELECT o FROM OpticalRegion o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalRegion.findByName", query = "SELECT o FROM OpticalRegion o WHERE o.name = :name")})
public class OpticalRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "regionId")
    private Collection<OpticalCity> opticalCityCollection;

    public OpticalRegion() {
    }

    public OpticalRegion(Integer id) {
        this.id = id;
    }

    public OpticalRegion(Integer id, String name) {
        this.id = id;
        this.name = name;
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
    public Collection<OpticalCity> getOpticalCityCollection() {
        return opticalCityCollection;
    }

    public void setOpticalCityCollection(Collection<OpticalCity> opticalCityCollection) {
        this.opticalCityCollection = opticalCityCollection;
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
        if (!(object instanceof OpticalRegion)) {
            return false;
        }
        OpticalRegion other = (OpticalRegion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalRegion[ id=" + id + " ]";
    }
    
}
