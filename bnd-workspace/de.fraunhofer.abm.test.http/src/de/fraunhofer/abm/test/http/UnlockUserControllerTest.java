package de.fraunhofer.abm.test.http;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
	
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import de.fraunhofer.abm.http.client.Base64.InputStream;
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
	        
	        
	        uri = baseUri + "/rest/username?username=" + USER;
	        String result = HttpUtils.get(uri, headers, charset);
	        Assert.assertNotNull(result);
	        System.out.println(result.toString());
            JSONObject obj = new JSONObject(result);
		    assertEquals(true, obj.get("locked"));
		    
		       uri = baseUri + "/rest/userlockunlock";
		       payload = "{\"username\":\"demo\",\"isLock\":\"false\"}";
		        response = HttpUtils.post(uri, headers, payload.getBytes(charset), charset);
		        Assert.assertEquals(NUM200, response.getResponseCode());	
		        
		        uri = baseUri + "/rest/username?username=" + USER;
		        result = HttpUtils.get(uri, headers, charset);
		        Assert.assertNotNull(result);
		        System.out.println(result.toString());
	            obj = new JSONObject(result);
			    assertEquals(false, obj.get("locked"));
	        
    
	}	
	        }