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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ECF3758A
 */
@Entity
@Table(name = "optical_chasis")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalChasis.findAll", query = "SELECT o FROM OpticalChasis o")
    , @NamedQuery(name = "OpticalChasis.findById", query = "SELECT o FROM OpticalChasis o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalChasis.findByName", query = "SELECT o FROM OpticalChasis o WHERE o.name = :name")
    , @NamedQuery(name = "OpticalChasis.findByCommunity", query = "SELECT o FROM OpticalChasis o WHERE o.community = :community")
    , @NamedQuery(name = "OpticalChasis.findByManagementIp", query = "SELECT o FROM OpticalChasis o WHERE o.managementIp = :managementIp")})
public class OpticalChasis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "community")
    private String community;
    @Size(max = 16)
    @Column(name = "management_ip")
    private String managementIp;
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalPlatform platformId;
    @JoinColumn(name = "sds_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalSds sdsId;
    @OneToMany(mappedBy = "chasisId")
    private Collection<OpticalSlot> opticalSlotCollection;

    public OpticalChasis() {
    }

    public OpticalChasis(Integer id) {
        this.id = id;
    }

    public OpticalChasis(Integer id, String name, String community) {
        this.id = id;
        this.name = name;
        this.community = community;
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

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getManagementIp() {
        return managementIp;
    }

    public void setManagementIp(String managementIp) {
        this.managementIp = managementIp;
    }

    public OpticalPlatform getPlatformId() {
        return platformId;
    }

    public void setPlatformId(OpticalPlatform platformId) {
        this.platformId = platformId;
    }

    public OpticalSds getSdsId() {
        return sdsId;
    }

    public void setSdsId(OpticalSds sdsId) {
        this.sdsId = sdsId;
    }

    @XmlTransient
    public Collection<OpticalSlot> getOpticalSlotCollection() {
        return opticalSlotCollection;
    }

    public void setOpticalSlotCollection(Collection<OpticalSlot> opticalSlotCollection) {
        this.opticalSlotCollection = opticalSlotCollection;
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
        if (!(object instanceof OpticalChasis)) {
            return false;
        }
        OpticalChasis other = (OpticalChasis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalChasis[ id=" + id + " ]";
    }
    
}
