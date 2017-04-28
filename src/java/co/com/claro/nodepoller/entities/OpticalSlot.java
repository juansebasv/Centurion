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
@Table(name = "optical_slot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalSlot.findAll", query = "SELECT o FROM OpticalSlot o")
    , @NamedQuery(name = "OpticalSlot.findById", query = "SELECT o FROM OpticalSlot o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalSlot.findByName", query = "SELECT o FROM OpticalSlot o WHERE o.name = :name")
    , @NamedQuery(name = "OpticalSlot.findByLabel1", query = "SELECT o FROM OpticalSlot o WHERE o.label1 = :label1")
    , @NamedQuery(name = "OpticalSlot.findByLabel2", query = "SELECT o FROM OpticalSlot o WHERE o.label2 = :label2")
    , @NamedQuery(name = "OpticalSlot.findByTitle", query = "SELECT o FROM OpticalSlot o WHERE o.title = :title")
    , @NamedQuery(name = "OpticalSlot.findByPlatformId", query = "SELECT o FROM OpticalSlot o WHERE o.platformId = :platformId")
    , @NamedQuery(name = "OpticalSlot.findByAlarmtypeId", query = "SELECT o FROM OpticalSlot o WHERE o.alarmtypeId = :alarmtypeId")})
public class OpticalSlot implements Serializable {

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
    @Size(max = 64)
    @Column(name = "label1")
    private String label1;
    @Size(max = 64)
    @Column(name = "label2")
    private String label2;
    @Size(max = 128)
    @Column(name = "title")
    private String title;
    @Column(name = "platform_id")
    private Integer platformId;
    @Column(name = "alarmtype_id")
    private Integer alarmtypeId;
    @OneToMany(mappedBy = "fwdslotId")
    private Collection<OpticalNode> opticalNodeCollection;
    @OneToMany(mappedBy = "retslotId")
    private Collection<OpticalNode> opticalNodeCollection1;
    @JoinColumn(name = "chasis_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalChasis chasisId;
    @JoinColumn(name = "slottype_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalSlottype slottypeId;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalState stateId;

    public OpticalSlot() {
    }

    public OpticalSlot(Integer id) {
        this.id = id;
    }

    public OpticalSlot(Integer id, String name) {
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

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getAlarmtypeId() {
        return alarmtypeId;
    }

    public void setAlarmtypeId(Integer alarmtypeId) {
        this.alarmtypeId = alarmtypeId;
    }

    @XmlTransient
    public Collection<OpticalNode> getOpticalNodeCollection() {
        return opticalNodeCollection;
    }

    public void setOpticalNodeCollection(Collection<OpticalNode> opticalNodeCollection) {
        this.opticalNodeCollection = opticalNodeCollection;
    }

    @XmlTransient
    public Collection<OpticalNode> getOpticalNodeCollection1() {
        return opticalNodeCollection1;
    }

    public void setOpticalNodeCollection1(Collection<OpticalNode> opticalNodeCollection1) {
        this.opticalNodeCollection1 = opticalNodeCollection1;
    }

    public OpticalChasis getChasisId() {
        return chasisId;
    }

    public void setChasisId(OpticalChasis chasisId) {
        this.chasisId = chasisId;
    }

    public OpticalSlottype getSlottypeId() {
        return slottypeId;
    }

    public void setSlottypeId(OpticalSlottype slottypeId) {
        this.slottypeId = slottypeId;
    }

    public OpticalState getStateId() {
        return stateId;
    }

    public void setStateId(OpticalState stateId) {
        this.stateId = stateId;
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
        if (!(object instanceof OpticalSlot)) {
            return false;
        }
        OpticalSlot other = (OpticalSlot) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalSlot[ id=" + id + " ]";
    }
    
}
