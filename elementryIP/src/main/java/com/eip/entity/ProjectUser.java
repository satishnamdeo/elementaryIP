/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eip.entity;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author JAVA 5
 */
@Entity
@Table(name = "project_user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ProjectUser.findAll", query = "SELECT p FROM ProjectUser p"),
    @NamedQuery(name = "ProjectUser.findByCreationDate", query = "SELECT p FROM ProjectUser p WHERE p.creationDate = :creationDate"),
    @NamedQuery(name = "ProjectUser.findByLastModified", query = "SELECT p FROM ProjectUser p WHERE p.lastModified = :lastModified"),
    @NamedQuery(name = "ProjectUser.findByWritePermission", query = "SELECT p FROM ProjectUser p WHERE p.writePermission = :writePermission"),
    @NamedQuery(name = "ProjectUser.findByStaus", query = "SELECT p FROM ProjectUser p WHERE p.staus = :staus"),
    @NamedQuery(name = "ProjectUser.findByUserIndex", query = "SELECT p FROM ProjectUser p WHERE p.userIndex = :userIndex")})
public class ProjectUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "write_permission")
    private Boolean writePermission;
    @Size(max = 45)
    @Column(name = "staus")
    private String staus;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "user_index")
    private Integer userIndex;
    @JoinColumn(name = "colab_id", referencedColumnName = "user_id")
    @ManyToOne
    private User colabId;
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    @ManyToOne
    private ProjectManage projectId;

    public ProjectUser() {
    }

    public ProjectUser(Integer userIndex) {
        this.userIndex = userIndex;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Boolean getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(Boolean writePermission) {
        this.writePermission = writePermission;
    }

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public Integer getUserIndex() {
        return userIndex;
    }

    public void setUserIndex(Integer userIndex) {
        this.userIndex = userIndex;
    }

    public User getColabId() {
        return colabId;
    }

    public void setColabId(User colabId) {
        this.colabId = colabId;
    }

    public ProjectManage getProjectId() {
        return projectId;
    }

    public void setProjectId(ProjectManage projectId) {
        this.projectId = projectId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userIndex != null ? userIndex.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProjectUser)) {
            return false;
        }
        ProjectUser other = (ProjectUser) object;
        if ((this.userIndex == null && other.userIndex != null) || (this.userIndex != null && !this.userIndex.equals(other.userIndex))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.eip.entity.ProjectUser[ userIndex=" + userIndex + " ]";
    }
    
}
