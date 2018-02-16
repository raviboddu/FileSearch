package com.elsevier.search.cache;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;


@PropertySource("classpath:application.properties")
public class CallableGetData implements Callable{
	Path filePath;
	//@Value("${delim}")
    private String delim;

	CallableGetData(@Value("${delim}") String delim){
		this.delim=delim;
	}
	
	public CallableGetData(Path filePath) {
		super();
		this.filePath = filePath;
	}
	@Override
	public Map<String,Set<String>> call() throws Exception {
			Set<String> wordSet=new HashSet<>();
			try {
			      Stream<String> fileLines = Files.lines(filePath,Charset.defaultCharset()); //close this
			      Stream<String> stringStream = fileLines.flatMap(x -> Arrays.stream(x.split(delim)));
			      stringStream.forEach(s-> wordSet.add(s));
			      //stringStream.forEach(System.out::println);
			    } catch (IOException ioException) {
			    	System.out.println(" Exception is "+ioException.getMessage());
			      ioException.printStackTrace();
			    }	
			Map<String,Set<String>> result=new HashMap<>();
			//System.out.println(" st size "+wordSet.size());
			if(wordSet.size()>=1){
				result.put(filePath.toString(), wordSet);
			}else{
				result.put(filePath.toString(), null);
			}
			return result;		
	}

}
