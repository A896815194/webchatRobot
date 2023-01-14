package com.web.webchat.config.cacheQueue;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TSysCacheEvent entity. @author MyEclipse Persistence Tools
 */
@Table(name="t_sys_cache_event", schema="susong51")
public class TSysCacheEvent implements java.io.Serializable {

    private static final long serialVersionUID = 7380205593792310749L;

    /**事件ID  */
    @Id
    @Column(name = "c_id")
    private String CId;

    /**缓存ID  */
    @Column(name = "c_cache_id")
    private String CCacheId;

    /**描述  */
    @Column(name = "c_desc")
    private String CDesc;

    /**版本  */
    @Column(name = "n_version")
    private Long NVersion;

    /**更新时间  */
    @Column(name = "d_update")
    private Timestamp DUpdate;

    // Constructors

    /** default constructor */
    public TSysCacheEvent() {
    }

    /** minimal constructor */
    public TSysCacheEvent(String CId) {
        this.CId = CId;
    }

    /** full constructor */
    public TSysCacheEvent(String CId, String CCacheId, String CDesc, Long NVersion, Timestamp DUpdate) {
        this.CId = CId;
        this.CCacheId = CCacheId;
        this.CDesc = CDesc;
        this.NVersion = NVersion;
        this.DUpdate = DUpdate;
    }

    // Property accessors

    /**
     * 返回事件ID 
     *
     * @return  事件ID
     */
    public String getCId() {
        return this.CId;
    }

    /**
     * 设置事件ID 
     *
     * @param CId 事件ID
     */
    public void setCId(String CId) {
        this.CId = CId;
    }

    /**
     * 返回缓存ID 
     *
     * @return  缓存ID
     */
    public String getCCacheId() {
        return this.CCacheId;
    }

    /**
     * 设置缓存ID 
     *
     * @param CCacheId 缓存ID
     */
    public void setCCacheId(String CCacheId) {
        this.CCacheId = CCacheId;
    }

    /**
     * 返回描述 
     *
     * @return  描述
     */
    public String getCDesc() {
        return this.CDesc;
    }

    /**
     * 设置描述 
     *
     * @param CDesc 描述
     */
    public void setCDesc(String CDesc) {
        this.CDesc = CDesc;
    }

    /**
     * 返回版本 
     *
     * @return  版本
     */
    public Long getNVersion() {
        return this.NVersion;
    }

    /**
     * 设置版本 
     *
     * @param NVersion 版本
     */
    public void setNVersion(Long NVersion) {
        this.NVersion = NVersion;
    }

    /**
     * 返回更新时间 
     *
     * @return  更新时间
     */
    public Timestamp getDUpdate() {
        return this.DUpdate;
    }

    /**
     * 设置更新时间 
     *
     * @param DUpdate 更新时间
     */
    public void setDUpdate(Timestamp DUpdate) {
        this.DUpdate = DUpdate;
    }

}
