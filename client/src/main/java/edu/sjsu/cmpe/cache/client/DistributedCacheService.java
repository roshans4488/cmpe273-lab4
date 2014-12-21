package edu.sjsu.cmpe.cache.client;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

/**
 * Distributed cache service
 * 
 */
public class DistributedCacheService implements CacheServiceInterface {
    private final String cacheServerUrl;
    int code=0;
    
    public DistributedCacheService(String serverUrl) {
        this.cacheServerUrl = serverUrl;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#get(long)
     */
    @Override
    public String get(long key) {
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(this.cacheServerUrl + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key)).asJson();
        } catch (UnirestException e) {
            System.err.println(e);
        }
        String value = null;
        if(response.getBody().getObject().getString("value") != null){
        	 value = response.getBody().getObject().getString("value");
        }
        

        return value;
    }

    /**
     * @see edu.sjsu.cmpe.cache.client.CacheServiceInterface#put(long,
     *      java.lang.String)
     */
    @Override
    public int put(long key, String value)throws InterruptedException, ExecutionException {
    	Future<HttpResponse<JsonNode>> response = null;
    	
            response = Unirest
                    .put(this.cacheServerUrl + "/cache/{key}/{value}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .routeParam("value", value).asJsonAsync(new Callback<JsonNode>() {

            		    public void failed(UnirestException e) {
            		        System.out.println("The request has failed");
            		        code=0;
            		        
            		    }

            		    public void completed(HttpResponse<JsonNode> response) {
            		         code = response.getStatus();
            		         Map<String, List<String>> headers = response.getHeaders();
            		         JsonNode body = response.getBody();
            		         InputStream rawBody = response.getRawBody();
            		         System.out.println("Request successful>"+ code);
            		    }

            		    public void cancelled() {
            		        System.out.println("The request has been cancelled");
            		        code=0;
            		    }

            		});
            Thread.sleep(1000 * 2);
            return code;
      
    }
    
    @Override
    public void delete(long key) {
    	Future<HttpResponse<JsonNode>> response = null;
        
            response = Unirest
                    .delete(this.cacheServerUrl + "/cache/{key}")
                    .header("accept", "application/json")
                    .routeParam("key", Long.toString(key))
                    .asJsonAsync(new Callback<JsonNode>() {

            		    public void failed(UnirestException e) {
            		        System.out.println("The request has failed");
            		    }

            		    public void completed(HttpResponse<JsonNode> response) {
            		         int code = response.getStatus();
            		         Map<String, List<String>> headers = response.getHeaders();
            		         JsonNode body = response.getBody();
            		         InputStream rawBody = response.getRawBody();
            		         System.out.println("Request successful"+ code);
            		    }

            		    public void cancelled() {
            		        System.out.println("The request has been cancelled");
            		    }

            		});


     
    }

	
}
