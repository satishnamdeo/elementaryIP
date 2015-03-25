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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Milind
 */
@Entity
@Table(name = "search_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SearchHistory.findAll", query = "SELECT s FROM SearchHistory s"),
    @NamedQuery(name = "SearchHistory.findById", query = "SELECT s FROM SearchHistory s WHERE s.id = :id"),
    @NamedQuery(name = "SearchHistory.findByUserId", query = "SELECT s FROM SearchHistory s WHERE s.userId = :userId"),
    @NamedQuery(name = "SearchHistory.findBySearchCount", query = "SELECT s FROM SearchHistory s WHERE s.searchCount = :searchCount"),
    @NamedQuery(name = "SearchHistory.findBySearchTime", query = "SELECT s FROM SearchHistory s WHERE s.searchTime = :searchTime"),
    @NamedQuery(name = "SearchHistory.findBySearchKeyword", query = "SELECT s FROM SearchHistory s WHERE s.searchKeyword = :searchKeyword")})
public class SearchHistory implements Serializable,Comparable<SearchHistory> {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "search_count")
    private Integer searchCount;
    @Column(name = "search_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date searchTime;
    @Basic(optional = false)
    @Column(name = "search_keyword")
    private String searchKeyword;

    public SearchHistory() {
    }

    public SearchHistory(Integer id) {
        this.id = id;
    }

    public SearchHistory(Integer id, int userId, String searchKeyword) {
        this.id = id;
        this.userId = userId;
        this.searchKeyword = searchKeyword;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(Integer searchCount) {
        this.searchCount = searchCount;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
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
        if (!(object instanceof SearchHistory)) {
            return false;
        }
        SearchHistory other = (SearchHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public int compareTo(SearchHistory o) {
		// TODO Auto-generated method stub
		return this.getSearchTime().compareTo(o.getSearchTime());
	}

    @Override
    public String toString() {
        return "javaapplication3.SearchHistory[ id=" + id + " ]";
    }

	
    
}
