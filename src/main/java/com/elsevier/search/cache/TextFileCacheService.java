package com.elsevier.search.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author Ravi Boddu
 * 20180215
 */
@Component
@ConfigurationProperties("filesearch")
public class TextFileCacheService {
		
	private  String extn;  // get this from properties file.
	
	private String root;
	
	private Map<String,Set<String>> fileNameCache;

	/*TextFileCacheService(){
		
	};*/
	
	@Autowired
	TextFileCacheService(@Value("${root}") String root,@Value("${extn}") String extn){
	 	this.root=root;
		this.extn=extn;
		buildCache();		
	}
	
	@PostConstruct
    public void init() {
        System.out.println("================== " + root + "========= "+extn);
    }
	/**
	 * @return 
	 */
	public List<Path> getFilePaths(){
		List<Path> filePaths=new ArrayList<>();
		System.out.println(root + " XX"+ extn);
		try (Stream<Path> paths = Files.walk(Paths.get(root))) {
		    paths
		        .filter(Files::isRegularFile)
		        .filter(path -> path.toString().endsWith(extn))
		        .forEach(path->filePaths.add(path));
		} catch (IOException e) {
				e.printStackTrace();
		} 
		
		return filePaths;		
	}
	

	/**
	 * @param filePath
	 * @return Set of words
	 */
	/*private Set<String> getWordSet(Path filePath){
		Set<String> wordSet=new HashSet<>();
		try {
		      Stream<String> fileLines = Files.lines(filePath,Charset.defaultCharset()); //close this
		      Stream<String> stringStream = fileLines.flatMap(x -> Arrays.stream(x.split(DELIMITTER)));
		      stringStream.forEach(s-> wordSet.add(s));
		      //stringStream.forEach(System.out::println);
		    } catch (IOException ioException) {
		    	System.out.println(" Exception is "+ioException.getMessage());
		      ioException.printStackTrace();
		    }
		 
		return wordSet;
	}
	*/
	private void buildCache(){
		List<Path> pathList= getFilePaths();
		fileNameCache =new HashMap<>();
		//System.out.println(pathList);
		
		//Get ExecutorService from Executors utility class, thread pool size is 10
        ExecutorService executor = Executors.newFixedThreadPool(100);
        //create a list to hold the Future object associated with Callable
        List<Future<HashMap<String,Set<String>>>> list = new ArrayList<>();
        //Create MyCallable instance
               
        pathList.forEach(path->{
        	Callable<HashMap<String,Set<String>>> callable = new CallableGetData(path);
        	Future<HashMap<String,Set<String>>> future = executor.submit(callable);
            //add Future to the list, we can get return value using Future
            list.add((Future<HashMap<String,Set<String>>>) future);
        	
        });

        for(Future<HashMap<String, Set<String>>> fut : list){
            try {
            	HashMap<String, Set<String>> map=fut.get();
            	//System.out.println( " Map "+map);
            	Entry<String, Set<String>> entry = map.entrySet().iterator().next();
            	 String key = entry.getKey();
            	 Set<String> value = entry.getValue();            	
            	if(key!=null)
            		fileNameCache.put(key, value);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //shut down the executor service now
        executor.shutdown();
        System.out.println(" Completed building cache");		
		//pathList.parallelStream().forEach(path -> fileNameCache.put(path.toString(), getWordSet(path)));		
	}

	

	/**
	 * @return the cache
	 */
	public Map<String,Set<String>> getCache(){
		return  fileNameCache;
	}	
	
}
