package de.fraunhofer.abm.app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.abm.app.auth.Authorizer;
import de.fraunhofer.abm.collection.dao.CollectionDao;
import de.fraunhofer.abm.collection.dao.CollectionPinDao;
import de.fraunhofer.abm.collection.dao.FilterPinDao;
import de.fraunhofer.abm.domain.CollectionDTO;
import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.rest.api.REST;
import osgi.enroute.rest.api.RESTRequest;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name="de.fraunhofer.abm.rest.pin")
public class PinController extends AbstractController implements REST {

    private static final transient Logger logger = LoggerFactory.getLogger(PinController.class);

    @Reference
    private CollectionDao collectionDao;
    
    @Reference
    private CollectionPinDao collectionPinDao;

    @Reference
    private FilterPinDao filterPinDao;

    @Reference
    private Authorizer authorizer;
    
    public List<CollectionDTO> getPin(RESTRequest rr) {
    	try {
    		ArrayList<String> users = new ArrayList<String>();
        	users.add("RegisteredUser");
        	users.add("UserAdmin"); 
            authorizer.requireRoles(users);
            Map<String, String[]> params = rr._request().getParameterMap();
            authorizer.requireUser(params.get("user")[0]);
        	if(params.get("type")[0].equals("collection")){
        		ArrayList<CollectionDTO> collections = new ArrayList<CollectionDTO>();
        		List<String> ids = collectionPinDao.findPins(params.get("user")[0]);
        		for(int i=0; i<ids.size(); i++){
            		CollectionDTO nextCollection = collectionDao.findById(ids.get(i));
        			collections.add(nextCollection);
        		}
        		return collections;
        	} else {
        		//Put filter retrieval here
        		return null;
        	}
    	} catch (SecurityException e) {
    		logger.info("User is not logged in");
   		    return new ArrayList<CollectionDTO>();
    	}
    }
    
    public boolean getPin(String user, String id){
    	 try {
         	authorizer.requireUser(user);
         	return collectionPinDao.checkExists(user, id);
         }catch (SecurityException e){
         	logger.info("User is not logged in");
    		    return false;
         }
    }
    
    interface PinRequest extends RESTRequest{
    	PinRequestDTO _body();
    }
    
    public static class PinRequestDTO {
    	public String user;
    	public String id;
    	public String type;
    	public PinRequestDTO(){}
    }
    
    public void postPin(PinRequest pr) {
    	ArrayList<String> users = new ArrayList<String>();
  	  users.add("RegisteredUser");
  	  users.add("UserAdmin"); 
        authorizer.requireRoles(users);
        PinRequestDTO req = pr._body();
      
        authorizer.requireUser(req.user);
    	if(req.type.equals("collection")){
    		collectionPinDao.addPin(req.user, req.id);
    	} else {
    		filterPinDao.addPin(req.user, req.id);
    	}
    }

    public void deletePin(String type, String user , String id) {
    	System.out.println(type);
    	ArrayList<String> users = new ArrayList<String>();
  	  users.add("RegisteredUser");
  	  users.add("UserAdmin"); 
        authorizer.requireRoles(users);
//        PinRequestDTO req = pr._body();
        authorizer.requireUser(user);
    	if(type.equals("collection")){
    		collectionPinDao.dropPin(user, id);
    	} else {
    		filterPinDao.dropPin(user, id);
    	}
    }
    
    public void deletePin(PinRequest pr) {
    	ArrayList<String> users = new ArrayList<String>();
  	  users.add("RegisteredUser");
  	  users.add("UserAdmin"); 
        authorizer.requireRoles(users);
        PinRequestDTO req = pr._body();
        authorizer.requireUser(req.user);
    	if(req.type.equals("collection")){
    		collectionPinDao.dropPin(req.user, req.id);
    	} else {
    		filterPinDao.dropPin(req.user, req.id);
    	}
    }
    
    @Override
    Logger getLogger() {
        return logger;
    }
}
