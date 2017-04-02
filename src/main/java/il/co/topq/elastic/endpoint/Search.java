package il.co.topq.elastic.endpoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import il.co.topq.elastic.ESRest;
import il.co.topq.elastic.response.query.SearchResponse;
import il.co.topq.elastic.response.query.SearchResponseHandler;


public class Search {

	private final ESRest client;
	
	private final String indexName;

	private final String documentName;

	private final int size;

	private final boolean scroll;

	public Search(ESRest client, String indexName, String documentName, int size, boolean scroll) {
		this.client = client;
		this.indexName = indexName;
		this.documentName = documentName;
		this.size = size;
		this.scroll = scroll;
	}
	
	public SearchResponseHandler byQuery(String query) throws IOException{
		return null;
	}

	public SearchResponseHandler byTerm(String filterTermKey, String filterTermValue) throws IOException {
		String requestBody = String.format("{\"size\":%d,\"query\": {\"term\" : { \"%s\" : \"%s\" }  } }", size,
				filterTermKey, filterTermValue);
		
		SearchResponse response = client.post("/" + indexName + "/" + documentName + "/_search?scroll=1m", requestBody,
				SearchResponse.class, true);

		final List<SearchResponse> responses = new ArrayList<SearchResponse>();
		while (response.getHits().getHits().size() > 0) {
			responses.add(response);
			response = scroll(response.getScrollId());
		}
		return new SearchResponseHandler(responses);
	}

	private SearchResponse scroll(String scrollId) throws IOException {
		return client.post("/_search/scroll", String.format("{\"scroll\":\"1m\",\"scroll_id\":\"%s\"}", scrollId),
				SearchResponse.class, true);
	}

	public Search query(int size) {
		return new Search(client, indexName, documentName, size, scroll);
	}

	public Search query(boolean scroll) {
		return new Search(client, indexName, documentName, size, scroll);
	}

}