package com.eip.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.client.solrj.request.DirectXmlRequest;
import org.apache.solr.client.solrj.response.CoreAdminResponse;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.eip.domain.PatentDo;


public class SOLRUtil {

public static SolrServer server;
private static Properties properties;
	public static void createEmbeddedServer(String core){
		 try{
			 String address="http://localhost:8080/solr/"+core;
			 server= new HttpSolrServer(address);
			 readPropeties();
		 }catch (Exception ex){
			 ex.printStackTrace();
		 }
	}
	
	
	/*public static void indexFile(String fileName) {
		createEmbeddedServer();
		try{
			UpdateRequest update = new UpdateRequest();
		    update.add(getSolrInputDocumentListFromXmlFile(fileName));
		    update.process(server);
		    server.commit();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}*/
	public static String readFile(String path) throws IOException{
        StringBuilder sb = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader(path))){
			String sCurrentLine=null;
		    while ((sCurrentLine = br.readLine()) != null){
		        sb.append(sCurrentLine);
		    }
		}
	    return sb.toString();
	}
	public static void indexFile(String fileName) {
		createEmbeddedServer("core0");
		try{
			DirectXmlRequest update=new DirectXmlRequest("/update", readFile(fileName));
		    update.process(server);
		    server.commit();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
		
	public static SolrDocumentList searchInFile(String searchString){
		createEmbeddedServer("core0");
		
		SolrDocumentList results=null;
		
		try{
		
			//System.out.println("input keyterm : "+searchString);
			/*String[] fieldList= new String[]{
					properties.getProperty("patent.id"),properties.getProperty("patent.patent_Number"),properties.getProperty("patent.publicationDate"),
					properties.getProperty("patent.filingDate"),properties.getProperty("patent.title"),properties.getProperty("patent.abstract"),
					properties.getProperty("patent.claims_independent"),properties.getProperty("patent.claims_dependent"),properties.getProperty("patent.preamble"),
					properties.getProperty("patent.preambleAttribute"),properties.getProperty("patent.element"),properties.getProperty("patent.feedback"),
					properties.getProperty("patent.elementAttribute"),properties.getProperty("patent.comment"),properties.getProperty("patent.field1"),
					properties.getProperty("patent.field2"),properties.getProperty("patent.field3"),properties.getProperty("patent.field4"),
					properties.getProperty("patent.field5"),properties.getProperty("patent.field6"),properties.getProperty("patent.field7"),
					properties.getProperty("patent.field8"),properties.getProperty("patent.field9"),properties.getProperty("patent.field10")};*/
			SolrQuery query = new SolrQuery();
			//Analyzer analyzer = new KeywordAnalyzer();
			//MultiFieldQueryParser queryParser=new MultiFieldQueryParser(Version.LUCENE_4_9,fieldList, analyzer);
			//Query query1 = queryParser.parse(searchString);
			query.setRows(Integer.parseInt(properties.getProperty("patent.maximum.rowCount")));
		    QueryResponse response = server.query(query.setQuery(searchString));
		    results=response.getResults();
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return results;
	}
	
	public static SolrInputDocument getPatentById(String patentId){
		createEmbeddedServer("core0");
		SolrInputDocument solrInputDoc = null;
		try{
			SolrQuery query = new SolrQuery();
			query.setQuery(properties.getProperty("patent.id")+":"+patentId+" "+properties.getProperty("patent.patent_Number")+":"+patentId);
		    QueryResponse response = server.query(query);
		    SolrDocument solrDoc=response.getResults().get(0);
		    solrInputDoc = ClientUtils.toSolrInputDocument(solrDoc);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return solrInputDoc;
	}
	
	
	public static void updatePatentIndex(SolrInputDocument	solrInputDoc){
		createEmbeddedServer("core0");
		try{
		    server.add(solrInputDoc);
		    server.commit();
		    System.out.println("successfully index updated");
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static String updateXML(PatentDo patentDo){
		String patentId=patentDo.getPatentId();
		String fieldName=patentDo.getClaimIndependentNo();
		String keyName=patentDo.getKeyname();
		String keyValue=patentDo.getKeyvalue();
		String startOffset=patentDo.getStartOffset();
		String endOffset=patentDo.getEndOffset();
		String comment=patentDo.getComment();
		Element documentToUpdate=null;
		String key=null;
		Integer elmMaxCount=0;
		List<Element> removeElementList=new ArrayList<Element>();
  	  	try {
  	  		readPropeties();
	  		String filePath=properties.getProperty("patent.recordFile.path");
	  		SAXBuilder builder = new SAXBuilder();
	  		File xmlFile = new File(filePath);
	  		Document doc = (Document) builder.build(xmlFile);
	  		Element rootNode = doc.getRootElement();
	  		List<Element> DocList = rootNode.getChildren("doc");
	  		for(Element document:DocList){
	  			List<Element> nodes=document.getChildren("field");
	  			for(Element node:nodes){
	  				if(node.getAttributeValue("name").equals(properties.getProperty("patent.id")) && node.getValue().equals(patentId)){
	  					
	  					documentToUpdate=document;
	  				}
	  			}
	  		}
	  		//System.out.println(" ******************************************************* \n");
	  		List<Element> documentElementList=documentToUpdate.getChildren("field");
	  		for(Element node:documentElementList){
	  			if(/*node.getAttributeValue("name").startsWith(fieldName) && */node.getAttributeValue("name").endsWith(keyName)){
	  					String nodeName1=node.getAttributeValue("name");
	  					String[] nodeName=nodeName1.split("_");
	  					if(Integer.parseInt(nodeName[3])>elmMaxCount)
	  						elmMaxCount=Integer.parseInt(nodeName[3]);
	  			}
	  		}
	  		boolean found=false;
	  		Integer elementCount=elmMaxCount+1;
	  		key=patentDo.getCurrentUser()+"_"+patentId+"_"+fieldName+elementCount+"_"+keyName;
	  		
	  		for(Element node:documentElementList){
	  			//----Element list to be removed from document
	  			if(node.getAttributeValue("name").equals(patentDo.getOldTagName())||
	  				node.getAttributeValue("name").equals(patentDo.getOldTagName()+"_startOffset")||
	  				node.getAttributeValue("name").equals(patentDo.getOldTagName()+"_endOffset")||
	  				node.getAttributeValue("name").equals(patentDo.getOldTagName()+"_"+properties.getProperty("patent.comment"))){
	  					removeElementList.add(node);
	  			}
	  			//-----while creating new element,checking existence
	  			if(node.getAttributeValue("name").equals(key)){
	  				node.setText(keyValue);
	  				found=true;
	  			}
	  		}
	  		if(!found){
	  			if(patentDo.getOldTagName()!=null){
	  				for (Element element : removeElementList) {
	  					documentToUpdate.removeContent(element);	
	  				}
	  			}
	  			List<Element> elementList=new ArrayList<Element>();
				Element element= new Element("field");
				element.setAttribute("name",key);
				element.setText(keyValue);
				
				Element element1= new Element("field");
				element1.setAttribute("name",key+"_startOffset");
				element1.setText(startOffset);
				
				Element element2= new Element("field");
				element2.setAttribute("name",key+"_endOffset");
				element2.setText(endOffset);
				
				Element element3= new Element("field");
				element3.setAttribute("name",key+"_"+properties.getProperty("patent.comment"));
				element3.setText(comment);
				
				elementList.add(element);
				elementList.add(element1);
				elementList.add(element2);
				elementList.add(element3);
				
				documentToUpdate.addContent(elementList);
				System.out.println("element added by keyname"+key);
			}
	  		XMLOutputter xmlOutput = new XMLOutputter();
	  		xmlOutput.setFormat(Format.getPrettyFormat());
	  		xmlOutput.output(doc, new FileWriter(filePath));
	  		// xmlOutput.output(doc, System.out);
	  		System.out.println("\nFile updated!");
  	  } catch (IOException io) {
  		  io.printStackTrace();
  	  } catch (JDOMException e) {
  		  e.printStackTrace();
  	  }
	return key;
	}
	
	public static String removeSegment(PatentDo patentDo){
		String patentId=patentDo.getPatentId();
		String key=patentDo.getKeyname();
		List<Element> removeElementList=new ArrayList<Element>();
		Element documentToUpdate=null;
  	  	try {
  	  		readPropeties();
	  		String filePath=properties.getProperty("patent.recordFile.path");
	  		SAXBuilder builder = new SAXBuilder();
	  		File xmlFile = new File(filePath);
	  		Document doc = (Document) builder.build(xmlFile);
	  		Element rootNode = doc.getRootElement();
	  		List<Element> DocList = rootNode.getChildren("doc");
	  		for(Element document:DocList){
	  			List<Element> nodes=document.getChildren("field");
	  			for(Element node:nodes){
	  				if(node.getAttributeValue("name").equals(properties.getProperty("patent.id")) && node.getValue().equals(patentId)){
	  					documentToUpdate=document;
	  				}
	  			}
	  		}
	  		List<Element> documentElementList=documentToUpdate.getChildren("field");
	  		boolean found=false;
	  		for(Element node:documentElementList){
	  			if(node.getAttributeValue("name").equals(key)||	node.getAttributeValue("name").equals(key+"_startOffset")||
		  				node.getAttributeValue("name").equals(key+"_endOffset")||node.getAttributeValue("name").equals(key+"_"+properties.getProperty("patent.comment"))){
		  				removeElementList.add(node);
		  				found=true;
		  		}
	  		}
	  		if(found){
	  			for (Element element : removeElementList) {
  					documentToUpdate.removeContent(element);	
  				}
			}
	  		XMLOutputter xmlOutput = new XMLOutputter();
	  		xmlOutput.setFormat(Format.getPrettyFormat());
	  		xmlOutput.output(doc, new FileWriter(filePath));
	  		// xmlOutput.output(doc, System.out);
	  		System.out.println("\nFile updated!");
  	  } catch (IOException io) {
  		  io.printStackTrace();
  	  } catch (JDOMException e) {
  		  e.printStackTrace();
  	  }
	return key;
	}
	
	
	
	public static String updateCommentXML(PatentDo patentDo){
		String patentId=patentDo.getPatentId();
		String key=patentDo.getKeyname();
		String comment=patentDo.getComment();
		Element documentToUpdate=null;
  	  	try {
  	  		readPropeties();
	  		String filePath=properties.getProperty("patent.recordFile.path");
	  		SAXBuilder builder = new SAXBuilder();
	  		File xmlFile = new File(filePath);
	  		Document doc = (Document) builder.build(xmlFile);
	  		Element rootNode = doc.getRootElement();
	  		List<Element> DocList = rootNode.getChildren("doc");
	  		for(Element document:DocList){
	  			List<Element> nodes=document.getChildren("field");
	  			for(Element node:nodes){
	  				if(node.getAttributeValue("name").equals(properties.getProperty("patent.id")) && node.getValue().equals(patentId)){
	  					documentToUpdate=document;
	  				}
	  			}
	  		}
	  		List<Element> documentElementList=documentToUpdate.getChildren("field");
	  		boolean found=false;
	  		for(Element node:documentElementList){
	  			//-----while creating new element,checking existence
	  			if(node.getAttributeValue("name").equals(key)){
	  				node.setText(comment);
	  				found=true;
	  			}
	  		}
	  		if(!found){
				Element element= new Element("field");
				element.setAttribute("name",key);
				element.setText(comment);
								
				documentToUpdate.addContent(element);
				//System.out.println("Element Added");
			}
	  		XMLOutputter xmlOutput = new XMLOutputter();
	  		xmlOutput.setFormat(Format.getPrettyFormat());
	  		xmlOutput.output(doc, new FileWriter(filePath));
	  		// xmlOutput.output(doc, System.out);
	  		System.out.println("\nFile updated!");
  	  } catch (IOException io) {
  		  io.printStackTrace();
  	  } catch (JDOMException e) {
  		  e.printStackTrace();
  	  }
	return key;
	}
	
	public static  void readPropeties(){
		 try{
			 properties = new Properties();
			 InputStream resourceStream = SOLRUtil.class.getResourceAsStream("/application.properties");
			 properties.load(resourceStream);
		 }catch (Exception ex){
			 ex.printStackTrace();
		 }
	}
	
	public static  void createCore(Integer id){
		 try{
			 SolrServer aServer =  new HttpSolrServer("http://localhost:8080/solr");
			 CoreAdminResponse aResponse = CoreAdminRequest.getStatus("project_"+id, aServer);
			 /*System.out.println("+++++++++  create core size +++++++++"+aResponse.getCoreStatus().size());*/
			 if (aResponse.getCoreStatus().size() < 2)
			 {
				/* System.out.println("+++++++++ in If create core +++++++++"+aResponse.getCoreStatus());*/
				 
			     CoreAdminRequest.Create aCreateRequest = new CoreAdminRequest.Create();
			     aCreateRequest.setCoreName("project_"+id);
			     aCreateRequest.setInstanceDir("project_"+id);
			     aCreateRequest.setDataDir("data");
			     		
			     aCreateRequest.process(aServer);
			     /*CoreAdminRequest.unloadCore("project_5", aServer);
			     CoreAdminRequest.Unload unload=new CoreAdminRequest.Unload(true);
			     unload.isDeleteInstanceDir();*/
			     indexProjectFile(id,"c:/Solr/patents/"+id+".xml");
			 }
		 }catch (Exception ex){
			 ex.printStackTrace();
		 }
	}
	public static void indexProjectFile(Integer projectId,String fileName) {
		if(projectId==0){
			createEmbeddedServer("core0");
			
		}
		else{
			createEmbeddedServer("project_"+projectId);
		}
		try{
			DirectXmlRequest update=new DirectXmlRequest("/update", readFile(fileName));
		    update.process(server);
		    server.commit();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static SolrDocumentList searchInProjectFile(Integer projectId,String searchString){
		if(projectId==0){
			
			createEmbeddedServer("core0");
		}
		else{
			createEmbeddedServer("project_"+projectId);
		}
		
		SolrDocumentList results=null;
		
		try{
				
			SolrQuery query = new SolrQuery();
			
			query.setRows(Integer.parseInt(properties.getProperty("patent.maximum.rowCount")));
		    QueryResponse response = server.query(query.setQuery(searchString));
		    results=response.getResults();
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return results;
	}
	public static SolrDocumentList searchPatentById(String searchString){
		/*createEmbeddedServer();*/
		
		SolrDocumentList results=null;
		/*SolrDocumentList results1=null;*/
		try{
		
			SolrQuery query = new SolrQuery();
			//Analyzer analyzer = new KeywordAnalyzer();
			//MultiFieldQueryParser queryParser=new MultiFieldQueryParser(Version.LUCENE_4_9,fieldList, analyzer);
			//Query query1 = queryParser.parse(searchString);
			query.setRows(Integer.parseInt(properties.getProperty("patent.maximum.rowCount")));
		    QueryResponse response = server.query(query.setQuery(searchString));
		    results=response.getResults();
			/*query.setQuery("January");
		    query.addFilterQuery("Project_id:100");
		   query.setFields("id","PublicationDate","ReferencePatents","Title","Abstract");*/
		  /*  query.setStart(0);    */
		  /*  query.set("defType", "edismax");*/

		/*  QueryResponse response = server.query(query);
		 results1 = response.getResults();
		    System.out.println("++++++++++++ result SIZE ++++++++++++++  "+results1.size());
		    for (int i = 0; i < results1.size(); ++i) {
		      System.out.println(results1.get(i));
		    }*/
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return results;
	}
	public static boolean deleteCore(Integer id){
		/* boolean ans=false;*/
		try{
			/*System.out.println("+++++++++ in  delete core:util +++++++++");*/
			 SolrServer aServer =  new HttpSolrServer("http://localhost:8080/solr");
			
			 CoreAdminResponse aResponse = CoreAdminRequest.getStatus("project_"+id, aServer);
			
			 if (aResponse.getCoreStatus().size() < 2)
			 {
				
			     CoreAdminRequest.Unload unload=new CoreAdminRequest.Unload(true);
			     unload.setDeleteInstanceDir(true);
			     unload.setCoreName("project_"+id);
			     unload.process(aServer);
			     /*ans=unload.isDeleteDataDir();
			     System.out.println("+++++++++ in If delete core ans +++++++++ "+ans);  */
			     return true; 
			 }
		 }catch (Exception ex){
			 ex.printStackTrace();
		 }
		
		return false ;
	}
	
}
