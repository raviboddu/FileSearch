package com.elsevier.search.service;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import com.elsevier.search.text.IFileSearch;
//rest service 

@Path("/api") //http:localhost:8080/search-services/webapi/find
public class FileSearchService {
	
	@Autowired
	private IFileSearch fileSearch;
	
	//search
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/search/{params}") //http:localhost:8080/search/James Bond
	public List<String> findinfiles(@PathParam ("params") String searchString) {
		List<String> searchResults = fileSearch.search(searchString);
		return searchResults;
	}	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Path("/hello") //http:localhost:8080/
	public String welcome() {		
		return "Welcome to FileSearch";
	}	
	
}
