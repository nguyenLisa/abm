package de.fraunhofer.abm.test.http;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import de.fraunhofer.abm.http.client.HttpResponse;
import de.fraunhofer.abm.http.client.HttpUtils;
	public class UnlockUserControllerTest extends AbstractHttpTest {
	    @Test
	    public void testCollectionstatus() throws IOException {
	    	final int num200 = 200;
	        // send a login request
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
	        testUnlockUser();
	    }	   
	   
	    
		protected Map<String, String> login() throws IOException {
	        login(USER, PASSWORD);
	        Map<String, String> headers = HttpUtils.createFirefoxHeader();
	        addSessionCookie(headers);
	        return headers;
	    }
	    
		private void testUnlockUser() throws IOException {
			HttpResponse response;
			Map<String, String> headers = login();
	        headers.put("Content-Type", "application/json;charset=UTF-8");
	        	        
	        
	       String uri = baseUri + "/rest/userlockunlock";
	       String  payload = "{\"username\":\"demo\",\"isLock\":\"true\"}";
	        response = HttpUtils.post(uri, headers, payload.getBytes(charset), charset);
	        Assert.assertEquals(NUM200, response.getResponseCode());	
	        
	        
	        uri = baseUri + "/rest/username?username=" + "demo";
	        String result = HttpUtils.get(uri, headers, charset);
	        Assert.assertNotNull(result);
            JSONObject obj = new JSONObject(result);
		    assertEquals(true, obj.get("locked"));
		    
		     uri = baseUri + "/rest/userlockunlock";
		     payload = "{\"username\":\"demo\",\"isLock\":\"false\"}";
		     response = HttpUtils.post(uri, headers, payload.getBytes(charset), charset);
		     Assert.assertEquals(NUM200, response.getResponseCode());	
		        
		        uri = baseUri + "/rest/username?username=" + "demo";
		        result = HttpUtils.get(uri, headers, charset);
		        Assert.assertNotNull(result);
	            obj = new JSONObject(result);
			    assertEquals(false, obj.get("locked"));
	        
    
	}	
	        }