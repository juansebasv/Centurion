/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.nodepoller.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ECF3758A
 */
@Entity
@Table(name = "optical_nodealarm")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalNodealarm.findAll", query = "SELECT o FROM OpticalNodealarm o")
    , @NamedQuery(name = "OpticalNodealarm.findById", query = "SELECT o FROM OpticalNodealarm o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalNodealarm.findByCreated", query = "SELECT o FROM OpticalNodealarm o WHERE o.created = :created")
    , @NamedQuery(name = "OpticalNodealarm.findByAck", query = "SELECT o FROM OpticalNodealarm o WHERE o.ack = :ack")
    , @NamedQuery(name = "OpticalNodealarm.findByNodeId", query = "SELECT o FROM OpticalNodealarm o WHERE o.nodeId = :nodeId")
    , @NamedQuery(name = "OpticalNodealarm.findByRegionName", query = "SELECT o FROM OpticalNodealarm o WHERE o.regionName = :regionName")
    , @NamedQuery(name = "OpticalNodealarm.findByCityName", query = "SELECT o FROM OpticalNodealarm o WHERE o.cityName = :cityName")
    , @NamedQuery(name = "OpticalNodealarm.findBySdsName", query = "SELECT o FROM OpticalNodealarm o WHERE o.sdsName = :sdsName")
    , @NamedQuery(name = "OpticalNodealarm.findBySlotName", query = "SELECT o FROM OpticalNodealarm o WHERE o.slotName = :slotName")
    , @NamedQuery(name = "OpticalNodealarm.findByPlatformName", query = "SELECT o FROM OpticalNodealarm o WHERE o.platformName = :platformName")
    , @NamedQuery(name = "OpticalNodealarm.findByChasisName", query = "SELECT o FROM OpticalNodealarm o WHERE o.chasisName = :chasisName")
    , @NamedQuery(name = "OpticalNodealarm.findByProcessId", query = "SELECT o FROM OpticalNodealarm o WHERE o.processId = :processId")})
public class OpticalNodealarm implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Column(name = "ack")
    private Integer ack;
    @Size(max = 11)
    @Column(name = "node_id")
    private String nodeId;
    @Size(max = 32)
    @Column(name = "region_name")
    private String regionName;
    @Size(max = 32)
    @Column(name = "city_name")
    private String cityName;
    @Size(max = 32)
    @Column(name = "sds_name")
    private String sdsName;
    @Size(max = 32)
    @Column(name = "slot_name")
    private String slotName;
    @Size(max = 32)
    @Column(name = "platform_name")
    private String platformName;
    @Size(max = 32)
    @Column(name = "chasis_name")
    private String chasisName;
    @Column(name = "process_id")
    private Integer processId;
    @JoinColumn(name = "alarmtype_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalAlarmtype alarmtypeId;
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalLevel levelId;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalState stateId;

    public OpticalNodealarm() {
    }

    public OpticalNodealarm(Integer id) {
        this.id = id;
    }

    public OpticalNodealarm(Integer id, Date created) {
        this.id = id;
        this.created = created;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Integer getAck() {
        return ack;
    }

    public void setAck(Integer ack) {
        this.ack = ack;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getSdsName() {
        return sdsName;
    }

    public void setSdsName(String sdsName) {
        this.sdsName = sdsName;
    }

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getChasisName() {
        return chasisName;
    }

    public void setChasisName(String chasisName) {
        this.chasisName = chasisName;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public OpticalAlarmtype getAlarmtypeId() {
        return alarmtypeId;
    }

    public void setAlarmtypeId(OpticalAlarmtype alarmtypeId) {
        this.alarmtypeId = alarmtypeId;
    }

    public OpticalLevel getLevelId() {
        return levelId;
    }

    public void setLevelId(OpticalLevel levelId) {
        this.levelId = levelId;
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
        if (!(object instanceof OpticalNodealarm)) {
            return false;
        }
        OpticalNodealarm other = (OpticalNodealarm) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalNodealarm[ id=" + id + " ]";
    }
    
}
