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
@Table(name = "optical_slottype")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalSlottype.findAll", query = "SELECT o FROM OpticalSlottype o")
    , @NamedQuery(name = "OpticalSlottype.findById", query = "SELECT o FROM OpticalSlottype o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalSlottype.findByName", query = "SELECT o FROM OpticalSlottype o WHERE o.name = :name")
    , @NamedQuery(name = "OpticalSlottype.findByCode", query = "SELECT o FROM OpticalSlottype o WHERE o.code = :code")
    , @NamedQuery(name = "OpticalSlottype.findBySide", query = "SELECT o FROM OpticalSlottype o WHERE o.side = :side")})
public class OpticalSlottype implements Serializable {

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
    @Size(max = 32)
    @Column(name = "code")
    private String code;
    @Size(max = 64)
    @Column(name = "side")
    private String side;
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalPlatform platformId;
    @OneToMany(mappedBy = "slottypeId")
    private Collection<OpticalSlot> opticalSlotCollection;

    public OpticalSlottype() {
    }

    public OpticalSlottype(Integer id) {
        this.id = id;
    }

    public OpticalSlottype(Integer id, String name) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public OpticalPlatform getPlatformId() {
        return platformId;
    }

    public void setPlatformId(OpticalPlatform platformId) {
        this.platformId = platformId;
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
        if (!(object instanceof OpticalSlottype)) {
            return false;
        }
        OpticalSlottype other = (OpticalSlottype) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalSlottype[ id=" + id + " ]";
    }
    
}
