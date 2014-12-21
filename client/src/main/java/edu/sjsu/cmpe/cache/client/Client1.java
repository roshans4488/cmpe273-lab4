package edu.sjsu.cmpe.cache.client;



public class Client1 {

	
	
    public static void main(String[] args) throws Exception {
    	 
    	
        System.out.println("Starting Cache Client...");
        CacheServiceInterface cache1 = new DistributedCacheService(
                "http://localhost:3000");
        CacheServiceInterface cache2 = new DistributedCacheService(
                "http://localhost:3001");
        CacheServiceInterface cache3 = new DistributedCacheService(
                "http://localhost:3002");

        int response1= cache1.put(1, "a");
        System.out.println("response1: " +response1);
        int response2= cache2.put(1, "a");
        System.out.println("response2: " +response2);
        int response3= cache3.put(1, "a");
        System.out.println("response3: " +response3);
    
        
        if(response1 != 200 && response2 != 200){
        	System.out.println("Written only to cache3. Rolling back.. ");
        	cache3.delete(1);
       	cache1.put(1, "a");
           cache2.put(1, "a");
           cache3.put(1, "a");
           
       }else if(response1 != 200 && response3 != 200){
    	   System.out.println("Written only to cache2. Rolling back.. ");
        	cache2.delete(1);
        	cache1.put(1, "a");
       	cache2.put(1, "a");
     	cache3.put(1, "a");
      	
      }else if(response2 != 200 && response3 != 200){
    	  System.out.println("Written only to cache1. Rolling back.. ");
      	cache1.delete(1);
       	cache1.put(1, "a");
        	cache2.put(1, "a");
      	cache3.put(1, "a");
     	
    }     
        System.out.println("cache1  : " +cache1.get(1));
        System.out.println("cache2  : " +cache2.get(1));
        System.out.println("cache3  : " +cache3.get(1));
              
        System.out.println("Existing Cache Client...");
    }
    
    
	

}
