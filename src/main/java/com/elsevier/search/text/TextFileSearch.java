	package com.elsevier.search.text;

	import java.util.Collections;
	import java.util.HashSet;
	import java.util.List;
	import java.util.Map;
	import java.util.Set;
	import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
	import com.elsevier.search.cache.TextFileCacheService;
	@ConfigurationProperties("filesearch")
	@Component
	public class TextFileSearch implements IFileSearch {
		
		//@Value("${delim}")
		private  String delim="\\s+";
		
		@Autowired
	    private TextFileCacheService textFileCacheService;    
		
		public List<String> search(String params) {
			// TODO Auto-generated method stub
			//call cache  TextFileCache.get(String params)		
			Map<String,Set<String>> fileNameCache= textFileCacheService.getCache();
			Set<String> searchSet=new HashSet<>();
			Collections.addAll(searchSet, params.split(delim));
			
			
			List<String> result=fileNameCache.entrySet()
						.stream()
						.filter(map-> map.getValue()!=null)
						.filter(map-> map.getValue().containsAll(searchSet))
						.map(map->map.getKey())
						.collect(Collectors.toList());
			return result;
		}

	}
