package com.eip.domain;

import java.util.ArrayList;
import java.util.List;

public class PatentFieldsDo {
	String id;
	String US_Patent_Number;
	String PublicationDate;
	String FilingDate;
	String Inventors;
	String ClassesUS;
	String Title;
	String Abstract;
	String Description;
	List<String> claimIndependentList=new ArrayList<>();
	List<String> claimDependentList=new ArrayList<>();
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUS_Patent_Number() {
		return US_Patent_Number;
	}
	public void setUS_Patent_Number(String uS_Patent_Number) {
		US_Patent_Number = uS_Patent_Number;
	}
	public String getPublicationDate() {
		return PublicationDate;
	}
	public void setPublicationDate(String publicationDate) {
		PublicationDate = publicationDate;
	}
	public String getFilingDate() {
		return FilingDate;
	}
	public void setFilingDate(String filingDate) {
		FilingDate = filingDate;
	}
	public String getInventors() {
		return Inventors;
	}
	public void setInventors(String inventors) {
		Inventors = inventors;
	}
	public String getClassesUS() {
		return ClassesUS;
	}
	public void setClassesUS(String classesUS) {
		ClassesUS = classesUS;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getAbstract() {
		return Abstract;
	}
	public void setAbstract(String abstract1) {
		Abstract = abstract1;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public List<String> getClaimIndependentList() {
		return claimIndependentList;
	}
	public void setClaimIndependentList(List<String> claimIndependentList) {
		this.claimIndependentList = claimIndependentList;
	}
	public List<String> getClaimDependentList() {
		return claimDependentList;
	}
	public void setClaimDependentList(List<String> claimDependentList) {
		this.claimDependentList = claimDependentList;
	}
	
	

}
