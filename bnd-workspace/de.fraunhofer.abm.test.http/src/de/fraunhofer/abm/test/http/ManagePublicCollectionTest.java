package de.fraunhofer.abm.test.http;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import de.fraunhofer.abm.http.client.HttpResponse;
import de.fraunhofer.abm.http.client.HttpUtils;

public class ManagePublicCollectionTest extends AbstractHttpTest{

	public static final int NUM200 = 200;
	public static final int NUM403 = 403;
	public static final int NUM401 = 401;
	public static final int NUM10000 = 10000;


	@Test
    public void testCollectionById() throws IOException {
        // create
        Map<String, String> headers = login();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        String uri = baseUri + "/rest/collection";
        String payload = "{\"name\":\"slf4j\",\"description\":\"Simple Logging Facade 4 Java\",\"versions\":[{\"number\":1,\"creationDate\":\""
        		+ "2017-02-22T14:45:04.028Z\",\"comment\":\"Initial Version\",\"commits\":[{\"commitId\":\"HEAD\",\"repository\":{\"commits\":null,\""
        		+ "commitsUrl\":\"https://api.github.com/repos/qos-ch/slf4j/commits{/sha}\",\"contentsUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/contents/{+path}\",\"contributorsUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/contributors\",\"creationDate\":\"2009-08-20T16:25:49\",\"defaultBranch\":\"master\",\""
        		+ "description\":\"Simple Logging Facade for Java\",\"forks\":347,\"hasDownloads\":false,\"hasWiki\":false,\"htmlUrl\":\""
        		+ "https://github.com/qos-ch/slf4j\",\"id\":\"6b945e0c-b859-377c-b611-bcc7aa584272\",\"isPrivate\":false,\"issuesUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/issues{/number}\",\"latestUpdate\":\"2017-02-21T13:56:39Z\",\"license\":\"\",\"name\":\""
        		+ "slf4j\",\"openIssues\":62,\"owner\":\"qos-ch\",\"ownerType\":\"Organization\",\"properties\":[],\"releasesUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/releases{/id}\",\"remoteId\":283187,\"repositoryType\":\"git\",\"repositoryUrl\":\""
        		+ "https://github.com/qos-ch/slf4j.git\",\"score\":126,\"size\":9915,\"starred\":637,\"watched\":637},\"branchId\":\"master\"}]}]}";
        HttpResponse response = HttpUtils.post(uri, headers, payload.getBytes(charset), charset);
        Assert.assertEquals("", response.getContent());
        Assert.assertEquals(NUM200, response.getResponseCode());


        payload = "{\"name\":\"slf4j1\",\"description\":\"Simple Logging Facade 4 Java\",\"versions\":[{\"number\":1,\"creationDate\":\""
        		+ "2017-02-22T14:45:04.028Z\",\"comment\":\"Initial Version\",\"commits\":[{\"commitId\":\"HEAD\",\"repository\":{\"commits\":null,\""
        		+ "commitsUrl\":\"https://api.github.com/repos/qos-ch/slf4j/commits{/sha}\",\"contentsUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/contents/{+path}\",\"contributorsUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/contributors\",\"creationDate\":\"2009-08-20T16:25:49\",\"defaultBranch\":\"master\",\""
        		+ "description\":\"Simple Logging Facade for Java\",\"forks\":347,\"hasDownloads\":false,\"hasWiki\":false,\"htmlUrl\":\""
        		+ "https://github.com/qos-ch/slf4j\",\"id\":\"6b945e0c-b859-377c-b611-bcc7aa584272\",\"isPrivate\":false,\"issuesUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/issues{/number}\",\"latestUpdate\":\"2017-02-21T13:56:39Z\",\"license\":\"\",\"name\":\""
        		+ "slf4j\",\"openIssues\":62,\"owner\":\"qos-ch\",\"ownerType\":\"Organization\",\"properties\":[],\"releasesUrl\":\""
        		+ "https://api.github.com/repos/qos-ch/slf4j/releases{/id}\",\"remoteId\":283187,\"repositoryType\":\"git\",\"repositoryUrl\":\""
        		+ "https://github.com/qos-ch/slf4j.git\",\"score\":126,\"size\":9915,\"starred\":637,\"watched\":637},\"branchId\":\"master\"}]}]}";
        response = HttpUtils.post(uri, headers, payload.getBytes(charset), charset);

        // get collections for useradmin
        String isAdmin = "true";
        uri = baseUri + "/rest/collection?isAdmin=" + isAdmin;
        String collections = HttpUtils.get(uri, headers, charset);
        JSONArray array = new JSONArray(collections);
        Assert.assertTrue(array.length()>0);

		}
	
		@Test 
		public void testDeletePublicCollection() throws IOException { 
		
		  final int num200 = 200; 
		
		  Map<String, String> headers = new HashMap<>();
		  headers.put("Content-Type", "application/json;charset=UTF-8");
		  String uri = baseUri + "/rest/login";
		  String payload = "{\"username\": \""+USER+"\", \"password\": \""+PASSWORD+"\"}";
		  HttpResponse response = HttpUtils.post(uri, headers, payload.getBytes(), charset);
		  Assert.assertEquals(num200, response.getResponseCode());
		  String sessionCookie = HttpUtils.getHeaderField(response.getHeader(), "Set-Cookie");
		  Assert.assertTrue(sessionCookie.contains("JSESSIONID"));
		  // try to get a secured resource
		  headers.put("Cookie", sessionCookie);
		      
		         
		      String collectionId = testPutCollection(); 
		      
		   // get collections
		  uri = baseUri + "/rest/collection?user=" + USER;
		  String collections = HttpUtils.get(uri, headers, charset);
		  JSONArray array = new JSONArray(collections);
		  JSONObject collection = (JSONObject) array.get(0);
		  String id = collection.getString("id");
		  // get collection by id
		  uri = baseUri + "/rest/collection/" + id;
		  String json = HttpUtils.get(uri, headers, charset);
		  collection = new JSONObject(json);
		  
		  testDeleteCollection(collection.getString("id")); 
		}
	
		private void testDeleteCollection(String collectionId) throws IOException { 
	        Map<String, String> headers = login(); 
	          headers.put("Content-Type", "application/json;charset=UTF-8"); 
	       
	          String payload = collectionId; 
	          String uri = baseUri + "/rest/publiccollection/"+collectionId; 
	          HttpResponse collections = HttpUtils.delete(uri, headers, charset); 
	          Assert.assertEquals(NUM200, collections.getResponseCode()); 
	       
	    } 
		
		private String testPutCollection() throws IOException { 
		      Map<String, String> headers = login(); 
		          headers.put("Content-Type", "application/json;charset=UTF-8"); 
		          String uri = baseUri + "/rest/collection"; 
		          String payload = "{\"name\":\"slf4j\",\"description\":\"Simple Logging Facade 4 Java\",\"versions\":[{\"number\":1,\"creationDate\":\"" 
		              + "2017-02-22T14:45:04.028Z\",\"comment\":\"Initial Version\",\"commits\":[{\"commitId\":\"HEAD\",\"repository\":{\"commits\":null,\"" 
		              + "commitsUrl\":\"https://api.github.com/repos/qos-ch/slf4j/commits{/sha}\",\"contentsUrl\":\"" 
		              + "https://api.github.com/repos/qos-ch/slf4j/contents/{+path}\",\"contributorsUrl\":\"" 
		              + "https://api.github.com/repos/qos-ch/slf4j/contributors\",\"creationDate\":\"2009-08-20T16:25:49\",\"defaultBranch\":\"master\",\"" 
		              + "description\":\"Simple Logging Facade for Java\",\"forks\":347,\"hasDownloads\":false,\"hasWiki\":false,\"htmlUrl\":\"" 
		              + "https://github.com/qos-ch/slf4j\",\"id\":\"6b945e0c-b859-377c-b611-bcc7aa584272\",\"isPrivate\":false,\"issuesUrl\":\"" 
		              + "https://api.github.com/repos/qos-ch/slf4j/issues{/number}\",\"latestUpdate\":\"2017-02-21T13:56:39Z\",\"license\":\"\",\"name\":\"" 
		              + "slf4j\",\"openIssues\":62,\"owner\":\"qos-ch\",\"ownerType\":\"Organization\",\"properties\":[],\"releasesUrl\":\"" 
		              + "https://api.github.com/repos/qos-ch/slf4j/releases{/id}\",\"remoteId\":283187,\"repositoryType\":\"git\",\"repositoryUrl\":\"" 
		              + "https://github.com/qos-ch/slf4j.git\",\"score\":126,\"size\":9915,\"starred\":637,\"watched\":637},\"branchId\":\"master\"}]}]}"; 
		          HttpResponse response = HttpUtils.post(uri, headers, payload.getBytes(charset), charset); 
		          Assert.assertEquals("", response.getContent()); 
		          Assert.assertEquals(NUM200, response.getResponseCode()); 
		 
		          String id = testGetCollection(uri, headers, charset); 
		        return id; 
		    }
		
		private String testGetCollection(String uri, Map<String, String> headers, String charset) throws IOException { 
	        uri = baseUri + "/rest/collection?user=" + USER; 
	          String collections = HttpUtils.get(uri, headers, charset); 
	          JSONArray array = new JSONArray(collections); 
	          //Assert.assertEquals(1, array.length()); 
	          JSONObject collection = (JSONObject) array.get(0); 
	          String id = collection.getString("id"); 
	          String name = collection.getString("name"); 
	          String description = collection.getString("description"); 
	          //Assert.assertEquals("slf4j", name); 
	          //Assert.assertEquals("Simple Logging Facade 4 Java", description); 
	          return id; 
	    } 
}