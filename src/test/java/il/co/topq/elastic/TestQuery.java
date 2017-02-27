package il.co.topq.elastic;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import il.co.topq.elastic.model.Post;

@Ignore
public class TestQuery extends AbstractTestCase {

	@Before
	public void setUp() throws IOException {
		if (!client.index(INDEX).isExists()) {
			client.index(INDEX).create(SETTINGS);
		}
	}

	public void tearDown() throws IOException {
		if (client.index(INDEX).isExists()) {
			client.index(INDEX).delete();
		}
	}

	@Test
	public void testGetResultAsMap() throws IOException {
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post0);
		List<Post> posts = client.index(INDEX).document(DOC).query().byTerm("id", "555").asClass(Post.class);
		Assert.assertNotNull(posts);
		Assert.assertEquals(1, posts.size());
		Post post1 = posts.get(0);
		Assert.assertEquals(post0.getId(), post1.getId());
		Assert.assertEquals(post0.getPoints(), post1.getPoints());
		Assert.assertEquals(post0.getComments(), post1.getComments());
		Assert.assertEquals(post0.getSubreddit(), post1.getSubreddit());

	}
	
	@Test
	public void testGetResultAsString() throws IOException {
		Post post0 = new Post();
		post0.setId(555);
		post0.setOp("Itai");
		post0.setPoints(100);
		post0.setSubreddit("all");
		client.index(INDEX).document(DOC).add().single("100", post0);
		String posts = client.index(INDEX).document(DOC).query().byTerm("id", "555").asString();
		Assert.assertTrue(posts.contains("\"id\":555,\"op\":\"Itai\",\"subreddit\":\"all\",\"points\":100"));

	}


}