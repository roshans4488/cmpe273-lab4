package edu.sjsu.cmpe.cache.client;



public class Client2 {

    
    
    public static void main(String[] args) throws Exception {
         
        String repairValue="";
        String cache1Value="";
        String cache2Value="";
        String cache3Value="";
        System.out.println("Starting Cache Client...");
        CacheServiceInterface cache1 = new DistributedCacheService(
                "http://localhost:3000");
        CacheServiceInterface cache2 = new DistributedCacheService(
                "http://localhost:3001");
        CacheServiceInterface cache3 = new DistributedCacheService(
                "http://localhost:3002");
         System.out.println("Inserting into cache nodes");
         cache1.put(1, "a");
         cache2.put(1, "a");
         cache3.put(1, "a");
     
        System.out.println("Step1 completed");
        System.out.println("cache1  : " +cache1.get(1));
        System.out.println("cache2  : " +cache2.get(1));
        System.out.println("cache3  : " +cache3.get(1));
        System.out.println("Sleep for 30 seconds:Take server A down");
        System.out.println("Initializing step 2");
        Thread.sleep(30000);
        System.out.println("Inserting into cache nodes");
       
        cache1.put(1, "b");
        cache2.put(1, "b");
        cache3.put(1, "b");
        
        System.out.println("Step2 completed");
        System.out.println("Sleep for 30 seconds:Bring server A up");
       Thread.sleep(30000);
        
        try
        {
            cache1Value=cache1.get(1);
            cache2Value=cache2.get(1);
            cache3Value=cache3.get(1);
            
            
        }
        catch(Exception e)
        {
            System.out.println("Read repair...");
            cache1.put(1, "b");
            cache2.put(1, "b");
            cache3.put(1, "b");
            
       } 
        
       
       
        if(!cache1.get(1).toString().equals(cache2.get(1).toString())   && cache2.get(1).toString().equals(cache3.get(1).toString())){
            System.out.println("Read repair");
             repairValue = cache2.get(1);
            
            cache1.put(1, repairValue);
        
           
       }
        
        else if(!cache1.get(1).equals(cache2.get(1))   && cache1.get(1).equals(cache3.get(1))  ){
          
           System.out.println("Read repair");
         repairValue = cache1.get(1);
        
        cache2.put(1, repairValue);
        }
        
        else if(!cache3.get(1).equals(cache2.get(1))   && cache1.get(1).equals(cache2.get(1))  ){
          
           System.out.println("Read repair");
             repairValue = cache1.get(1);
            
            cache3.put(1, repairValue);
         }
        
        
        
        
        
        System.out.println("cache1  : " +cache1.get(1));
        System.out.println("cache2  : " +cache2.get(1));
        System.out.println("cache3  : " +cache3.get(1));
      
       
              
        System.out.println("Existing Cache Client...");
    }
    
    
    

}

