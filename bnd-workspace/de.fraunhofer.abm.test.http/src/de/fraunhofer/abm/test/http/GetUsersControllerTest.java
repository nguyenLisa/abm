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
	public class GetUsersControllerTest extends AbstractHttpTest {
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
	        testNonApprovedUsers();
	        testApprovedUsers();
	    }	   
	    
		protected Map<String, String> login() throws IOException {
	        login(USER, PASSWORD);
	        Map<String, String> headers = HttpUtils.createFirefoxHeader();
	        addSessionCookie(headers);
	        return headers;
	    }
	    
		private void testNonApprovedUsers() throws IOException {
			//HttpResponse response;
			Map<String, String> headers = login();
	        headers.put("Content-Type", "application/json;charset=UTF-8");
	        String payload = "{\"approved\":1}";
	        headers.put("params", payload);
	        String uri = baseUri + "/rest/userList?approved=1";
	        String collections = HttpUtils.get(uri, headers, charset);
	        JSONArray array = new JSONArray(collections);
	        //System.out.println("not approved: "+array);
	        if (array.length()>0) {
		        assertEquals(false, array.getJSONObject(0).get("approved"));				
			}
		}
		
		private void testApprovedUsers() throws IOException {
			//HttpResponse response;
			Map<String, String> headers = login();
	        headers.put("Content-Type", "application/json;charset=UTF-8");
	        String payload = "{\"approved\":0}";
	        headers.put("params", payload);
	        String uri = baseUri + "/rest/userList?approved=0";
	        String collections = HttpUtils.get(uri, headers, charset);
	        JSONArray array = new JSONArray(collections);
	        //System.out.println("approved: "+array);
	        if (array.length()>0) {
		        assertEquals(true, array.getJSONObject(0).get("approved"));				
			}
		}
		
	}