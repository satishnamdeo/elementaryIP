/*
 * To change this template file, choose Tools | Templates
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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "role_master")
@XmlRootElement

public class RoleMaster implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "role_id")
    private Integer roleId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "role_code")
    private String roleCode;
    @ManyToMany(mappedBy = "roleMasterList")
    private List<UrlMaster> urlMasterList;
    @OneToMany(mappedBy = "roleId")
    private List<User> userList;

    public RoleMaster() {
    }

    public RoleMaster(Integer roleId) {
        this.roleId = roleId;
    }

    public RoleMaster(Integer roleId, String roleCode) {
        this.roleId = roleId;
        this.roleCode = roleCode;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    @XmlTransient
    public List<UrlMaster> getUrlMasterList() {
        return urlMasterList;
    }

    public void setUrlMasterList(List<UrlMaster> urlMasterList) {
        this.urlMasterList = urlMasterList;
    }
    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (roleId != null ? roleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoleMaster)) {
            return false;
        }
        RoleMaster other = (RoleMaster) object;
        if ((this.roleId == null && other.roleId != null) || (this.roleId != null && !this.roleId.equals(other.roleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eip.entity.RoleMaster[ roleId=" + roleId + " ]";
    }
    
}
