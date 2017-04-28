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
@Table(name = "optical_state")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalState.findAll", query = "SELECT o FROM OpticalState o")
    , @NamedQuery(name = "OpticalState.findById", query = "SELECT o FROM OpticalState o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalState.findByName", query = "SELECT o FROM OpticalState o WHERE o.name = :name")})
public class OpticalState implements Serializable {

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
    @OneToMany(mappedBy = "stateId")
    private Collection<OpticalNodealarm> opticalNodealarmCollection;
    @OneToMany(mappedBy = "stateId")
    private Collection<OpticalNode> opticalNodeCollection;
    @OneToMany(mappedBy = "stateId")
    private Collection<OpticalSlot> opticalSlotCollection;

    public OpticalState() {
    }

    public OpticalState(Integer id) {
        this.id = id;
    }

    public OpticalState(Integer id, String name) {
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
    public Collection<OpticalNodealarm> getOpticalNodealarmCollection() {
        return opticalNodealarmCollection;
    }

    public void setOpticalNodealarmCollection(Collection<OpticalNodealarm> opticalNodealarmCollection) {
        this.opticalNodealarmCollection = opticalNodealarmCollection;
    }

    @XmlTransient
    public Collection<OpticalNode> getOpticalNodeCollection() {
        return opticalNodeCollection;
    }

    public void setOpticalNodeCollection(Collection<OpticalNode> opticalNodeCollection) {
        this.opticalNodeCollection = opticalNodeCollection;
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
        if (!(object instanceof OpticalState)) {
            return false;
        }
        OpticalState other = (OpticalState) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalState[ id=" + id + " ]";
    }
    
}
