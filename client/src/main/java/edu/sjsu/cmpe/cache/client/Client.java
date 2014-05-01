package edu.sjsu.cmpe.cache.client;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.collect.Lists;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Client {
	public static HashFunction hashFunction;
	 public static int numberOfReplicas;
	  	public final static SortedMap<Integer, String> circle = new TreeMap<Integer, String>();
	  	
    public static void main(String[] args) throws Exception {
    
    	String server1="http://localhost:3000";
    	String server2="http://localhost:3001";
    	String server3="http://localhost:3002";
    	List<String> servers = Lists.newArrayList(server1, server2, server3);
    	
    	int i;
    	int s1=0,s2=0,s3=0;
    	for(i=0;i<servers.size();i++)
   	  	{
    		System.out.println("Adding to the server");
   	  		add(servers.get(i));
   	  	
   	  	}
   	  	
   		for(i=0;i<10;i++){
    		
    		    int bucket = Hashing.consistentHash(Hashing.md5().hashLong(i), servers.size());
    		    System.out.println("Second time routed to: " + servers.get(bucket));
    		    String server=servers.get(bucket);
    		    System.out.println("Server::"+i+"::"+server);
    		    CacheServiceInterface cache = new DistributedCacheService(server);
    		    
    		    cache.put(i+1, ((char)(i+65))+"");
    		    if(server.equalsIgnoreCase("http://localhost:3000"))
    		    s1++;
    		    if(server.equalsIgnoreCase("http://localhost:3001"))
        		s2++;
    		    if(server.equalsIgnoreCase("http://localhost:3002"))
        		s3++;
    		    	cache.get(i+1);   
    		    	System.out.println("i::"+i+"=>"+((char)(i+65)));
        		    
    		    System.out.println("data in server running on 3000 "+s1);
    		    System.out.println("data in server running on 3001 "+s2);
    		    System.out.println("data in server running on 3002 "+s3);
    		    
    		    System.out.println("removing server"+server);
    		    remove(server);
    		    }
   		
     }
    	
    	 public static void add(String node) {
    		      circle.put(Hashing.md5().hashCode(),node);
    		    }
    		  

    		  public static void remove(String node) {
    		      circle.remove(Hashing.md5().hashCode());
    		    }
    		  public String get(Object key) {
    			    if (circle.isEmpty()) {
    			      return null;
    			    }
    			    int hash = Hashing.md5().hashLong((Integer) key).asInt();
    			    if (!circle.containsKey(hash)) {
    			      SortedMap<Integer, String> tailMap = circle.tailMap(hash);
    			      hash = tailMap.isEmpty() ?
    			             circle.firstKey() : tailMap.firstKey();
    			    }
    			    return circle.get(hash);
    			  } 
    	    }
    



