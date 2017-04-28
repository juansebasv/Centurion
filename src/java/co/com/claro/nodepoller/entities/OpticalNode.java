/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.claro.nodepoller.entities;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author ECF3758A
 */
@Entity
@Table(name = "optical_node")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpticalNode.findAll", query = "SELECT o FROM OpticalNode o")
    , @NamedQuery(name = "OpticalNode.findById", query = "SELECT o FROM OpticalNode o WHERE o.id = :id")
    , @NamedQuery(name = "OpticalNode.findByName", query = "SELECT o FROM OpticalNode o WHERE o.name = :name")
    , @NamedQuery(name = "OpticalNode.findByAdminchasisIp", query = "SELECT o FROM OpticalNode o WHERE o.adminchasisIp = :adminchasisIp")
    , @NamedQuery(name = "OpticalNode.findByFastcity", query = "SELECT o FROM OpticalNode o WHERE o.fastcity = :fastcity")
    , @NamedQuery(name = "OpticalNode.findByFastcityPhase", query = "SELECT o FROM OpticalNode o WHERE o.fastcityPhase = :fastcityPhase")
    , @NamedQuery(name = "OpticalNode.findByInRo", query = "SELECT o FROM OpticalNode o WHERE o.inRo = :inRo")
    , @NamedQuery(name = "OpticalNode.findByInSds", query = "SELECT o FROM OpticalNode o WHERE o.inSds = :inSds")
    , @NamedQuery(name = "OpticalNode.findByInTree", query = "SELECT o FROM OpticalNode o WHERE o.inTree = :inTree")
    , @NamedQuery(name = "OpticalNode.findByInCapital", query = "SELECT o FROM OpticalNode o WHERE o.inCapital = :inCapital")
    , @NamedQuery(name = "OpticalNode.findByFwdIdx", query = "SELECT o FROM OpticalNode o WHERE o.fwdIdx = :fwdIdx")
    , @NamedQuery(name = "OpticalNode.findByRetIdx", query = "SELECT o FROM OpticalNode o WHERE o.retIdx = :retIdx")
    , @NamedQuery(name = "OpticalNode.findByInBogota", query = "SELECT o FROM OpticalNode o WHERE o.inBogota = :inBogota")})
public class OpticalNode implements Serializable {

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
    @Size(max = 16)
    @Column(name = "adminchasis_ip")
    private String adminchasisIp;
    @Column(name = "fastcity")
    private Short fastcity;
    @Column(name = "fastcity_phase")
    private Short fastcityPhase;
    @Column(name = "in_ro")
    private Short inRo;
    @Column(name = "in_sds")
    private Short inSds;
    @Column(name = "in_tree")
    private Short inTree;
    @Column(name = "in_capital")
    private Short inCapital;
    @Column(name = "fwd_idx")
    private Short fwdIdx;
    @Column(name = "ret_idx")
    private Short retIdx;
    @Column(name = "in_bogota")
    private Short inBogota;
    @JoinColumn(name = "alarmtype_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalAlarmtype alarmtypeId;
    @JoinColumn(name = "fwdslot_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalSlot fwdslotId;
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private OpticalNodeModel modelId;
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalPlatform platformId;
    @JoinColumn(name = "retslot_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalSlot retslotId;
    @JoinColumn(name = "state_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalState stateId;
    @JoinColumn(name = "transponder_id", referencedColumnName = "id")
    @ManyToOne
    private OpticalTransponder transponderId;

    public OpticalNode() {
    }

    public OpticalNode(Integer id) {
        this.id = id;
    }

    public OpticalNode(Integer id, String name) {
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

    public String getAdminchasisIp() {
        return adminchasisIp;
    }

    public void setAdminchasisIp(String adminchasisIp) {
        this.adminchasisIp = adminchasisIp;
    }

    public Short getFastcity() {
        return fastcity;
    }

    public void setFastcity(Short fastcity) {
        this.fastcity = fastcity;
    }

    public Short getFastcityPhase() {
        return fastcityPhase;
    }

    public void setFastcityPhase(Short fastcityPhase) {
        this.fastcityPhase = fastcityPhase;
    }

    public Short getInRo() {
        return inRo;
    }

    public void setInRo(Short inRo) {
        this.inRo = inRo;
    }

    public Short getInSds() {
        return inSds;
    }

    public void setInSds(Short inSds) {
        this.inSds = inSds;
    }

    public Short getInTree() {
        return inTree;
    }

    public void setInTree(Short inTree) {
        this.inTree = inTree;
    }

    public Short getInCapital() {
        return inCapital;
    }

    public void setInCapital(Short inCapital) {
        this.inCapital = inCapital;
    }

    public Short getFwdIdx() {
        return fwdIdx;
    }

    public void setFwdIdx(Short fwdIdx) {
        this.fwdIdx = fwdIdx;
    }

    public Short getRetIdx() {
        return retIdx;
    }

    public void setRetIdx(Short retIdx) {
        this.retIdx = retIdx;
    }

    public Short getInBogota() {
        return inBogota;
    }

    public void setInBogota(Short inBogota) {
        this.inBogota = inBogota;
    }

    public OpticalAlarmtype getAlarmtypeId() {
        return alarmtypeId;
    }

    public void setAlarmtypeId(OpticalAlarmtype alarmtypeId) {
        this.alarmtypeId = alarmtypeId;
    }

    public OpticalSlot getFwdslotId() {
        return fwdslotId;
    }

    public void setFwdslotId(OpticalSlot fwdslotId) {
        this.fwdslotId = fwdslotId;
    }

    public OpticalNodeModel getModelId() {
        return modelId;
    }

    public void setModelId(OpticalNodeModel modelId) {
        this.modelId = modelId;
    }

    public OpticalPlatform getPlatformId() {
        return platformId;
    }

    public void setPlatformId(OpticalPlatform platformId) {
        this.platformId = platformId;
    }

    public OpticalSlot getRetslotId() {
        return retslotId;
    }

    public void setRetslotId(OpticalSlot retslotId) {
        this.retslotId = retslotId;
    }

    public OpticalState getStateId() {
        return stateId;
    }

    public void setStateId(OpticalState stateId) {
        this.stateId = stateId;
    }

    public OpticalTransponder getTransponderId() {
        return transponderId;
    }

    public void setTransponderId(OpticalTransponder transponderId) {
        this.transponderId = transponderId;
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
        if (!(object instanceof OpticalNode)) {
            return false;
        }
        OpticalNode other = (OpticalNode) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.claro.nodepoller.entities.OpticalNode[ id=" + id + " ]";
    }
    
}
