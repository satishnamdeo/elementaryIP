/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eip.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "url_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UrlMaster.findAll", query = "SELECT u FROM UrlMaster u"),
    @NamedQuery(name = "UrlMaster.findByUrlId", query = "SELECT u FROM UrlMaster u WHERE u.urlId = :urlId"),
    @NamedQuery(name = "UrlMaster.findByUrl", query = "SELECT u FROM UrlMaster u WHERE u.url = :url"),
    @NamedQuery(name = "UrlMaster.findByUrlName", query = "SELECT u FROM UrlMaster u WHERE u.urlName = :urlName")})
public class UrlMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "url_id")
    private Integer urlId;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;
    @Column(name = "url_name")
    private String urlName;
    @JoinTable(name = "role_x_url_master", joinColumns = {
        @JoinColumn(name = "url_id", referencedColumnName = "url_id")}, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    @ManyToMany
    private List<RoleMaster> roleMasterList;

    public UrlMaster() {
    }

    public UrlMaster(Integer urlId) {
        this.urlId = urlId;
    }

    public UrlMaster(Integer urlId, String url) {
        this.urlId = urlId;
        this.url = url;
    }

    public Integer getUrlId() {
        return urlId;
    }

    public void setUrlId(Integer urlId) {
        this.urlId = urlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlName() {
        return urlName;
    }

    public void setUrlName(String urlName) {
        this.urlName = urlName;
    }

    @XmlTransient
    public List<RoleMaster> getRoleMasterList() {
        return roleMasterList;
    }

    public void setRoleMasterList(List<RoleMaster> roleMasterList) {
        this.roleMasterList = roleMasterList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (urlId != null ? urlId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UrlMaster)) {
            return false;
        }
        UrlMaster other = (UrlMaster) object;
        if ((this.urlId == null && other.urlId != null) || (this.urlId != null && !this.urlId.equals(other.urlId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.otig.entity.UrlMaster[ urlId=" + urlId + " ]";
    }
    
}
