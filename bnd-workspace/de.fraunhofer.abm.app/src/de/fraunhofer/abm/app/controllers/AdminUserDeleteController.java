package de.fraunhofer.abm.app.controllers;

import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.useradmin.UserAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.abm.app.EmailConfigInterface;
import de.fraunhofer.abm.app.auth.Authorizer;
import de.fraunhofer.abm.collection.dao.UserDao;
import de.fraunhofer.abm.collection.dao.CollectionDao;
import de.fraunhofer.abm.collection.dao.FilterPinDao;
import osgi.enroute.configurer.api.RequireConfigurerExtender;
import osgi.enroute.rest.api.REST;
import osgi.enroute.rest.api.RESTRequest;
import osgi.enroute.webserver.capabilities.RequireWebServerExtender;

@RequireWebServerExtender
@RequireConfigurerExtender
@Component(name = "de.fraunhofer.abm.rest.adminDeleteUsers")
public class AdminUserDeleteController extends AbstractController implements REST {

	private static final transient Logger logger = LoggerFactory.getLogger(CollectionController.class);

	@Reference
	private UserDao userDao;
	
	@Reference
	private CollectionDao collectionDao;
	
	@Reference
	private FilterPinDao filterPinDao;
	
	@Reference
	private UserAdmin userAdmin;
	
	@Reference
	private Authorizer authorizer;

	@Reference
	private EmailConfigInterface config;

	interface AccountRequest extends RESTRequest {
		Map<String, String> _body();
	}

	/**
	 * Delete current user
	 * 
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public void deleteAdminDeleteUsers(String userlist) throws Exception {
		authorizer.requireRole("UserAdmin");
		String[] deleteUsers = userlist.split(",");
		for (String user: deleteUsers) {
			logger.debug("Deleting user {}", user);
	        try {
	        	if ( userDao.checkExists(user) ) {
	        		// delete all pinned collection entry by the user
	        		collectionDao.deleteUserPinnedCollections(user);
	        		// update created by to demo for public collections by this user
	        		collectionDao.updateUserPublicCollections(user);
	        		// Delete users private collections
	        		collectionDao.deleteUserPrivateCollections(user);
	        		// delete any entry for the user in filterPin table
	        		List<String> pinList = filterPinDao.findPins(user);
	        		for (String pinId : pinList) {
	        			filterPinDao.dropPin(user, pinId);
	        		}
	        		// delete any entry for the user in reset_token table
	        		userDao.deleteUserResetToken(user);
	        		// Delete user info from user table
	        		userDao.deleteUser(user);
	            }
	        } catch (Exception e) {
	        	logger.info("Exception");
	        }
	    }
	}

	@Override
	Logger getLogger() {
		return logger;
	}

}