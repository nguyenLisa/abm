package de.fraunhofer.abm.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import de.fraunhofer.abm.app.auth.SecurityContext;
import de.fraunhofer.abm.collection.dao.jpa.JpaProjectDao;
import de.fraunhofer.abm.domain.CollectionDTO;
import de.fraunhofer.abm.domain.CommitDTO;
import de.fraunhofer.abm.domain.ProjectObjectDTO;
import de.fraunhofer.abm.domain.VersionDTO;
import de.fraunhofer.abm.http.client.HttpResponse;
import de.fraunhofer.abm.http.client.HttpUtils;
import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.rest.api.REST;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name = "de.fraunhofer.abm.rest.delphi")
public class DelphiController implements REST {
    static Map<String, String> header = new HashMap<>();
    
    @Reference
    JpaProjectDao projectDao;

	private String featuresString;
	
	public String getDelphifeatures() throws Exception {
		String uri = "https://delphi.cs.uni-paderborn.de/api/features";   
		String resp = HttpUtils.get(uri,header,"UTF-8");   
		//System.out.println("delphi response /get"+resp);
		return resp;
	}
	
	public String postSearchquery(String features) throws Exception {
	    String body = features;
		header = new HashMap<>(); 
	    header.put("Content-type", "application/json"); 
	      String uri = "https://delphi.cs.uni-paderborn.de/api/search";   
	        HttpResponse resp = HttpUtils.post(uri, header, body.getBytes("UTF-8"), "UTF-8");   
	        JSONObject json = new JSONObject(resp); 
	        //System.out.println("delphi response"+json);
			JSONObject response = new JSONObject(resp);
			String responseArray = response.getString("content");
			//System.out.println("delphi response /search"+responseArray);
	        return responseArray;
	}
	
	public void postAddproject(String versionId,String projectId,String source) {
		ProjectObjectDTO project = new ProjectObjectDTO();
		project.id = UUID.randomUUID().toString();
        project.version_id = versionId;
        project.project_id = projectId;
        project.source = source;
        
        projectDao.save(project);
	}
	
	public void deleteRemoveproject(String projectId) {
		List<ProjectObjectDTO> result;
		result = projectDao.findproject(projectId);
			for(ProjectObjectDTO project:result)
			{		
				projectDao.delete(project);
			}
	}

}
