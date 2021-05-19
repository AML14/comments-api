package comments.repository;

import comments.model.Comment;
import comments.model.Profile;

import java.time.LocalDateTime;
import java.util.*;

public class MapCriticsRepository implements CriticsRepository {

	Map<String, Comment> commentMap;
	Map<String, Profile> profileMap;
	private static MapCriticsRepository instance = null;
	private int index = 0;

	public static MapCriticsRepository getInstance() {
		if (instance == null) {
			instance = new MapCriticsRepository();
			instance.init();
		}

		return instance;

	}

	private void init() {
		commentMap = new HashMap<String, Comment>();
		profileMap = new HashMap<String, Profile>();
		
		// Create Comments
		Comment c1 = new Comment();
		c1.setUserName("johnSmith");
		c1.setText("Great book!");
		c1.setType("Review");
		c1.setDate("2017-02-16T20:44:53.950");
		addComment(c1);
		
		Comment c2 = new Comment();
		c2.setUserName("johnSmith");
		c2.setText("Original product but the quality is too low for the price");
		c2.setType("Review");
		c2.setDate("2017-06-16T20:44:53.950");
		addComment(c2);
		
		Comment c3 = new Comment();
		c3.setUserName("johnSmith");
		c3.setText("The product was delivered three days after the expected date.");
		c3.setType("Complain");
		c3.setDate("2017-06-16T20:48:53.950");
		addComment(c3);
		
		Comment c4 = new Comment();
		c4.setUserName("oliviaClark");
		c4.setText("I would love to see this product in pocket size");
		c4.setType("Request");
		c4.setDate("2015-01-16T20:44:53.950");
		addComment(c4);
		
		Comment c5 = new Comment();
		c5.setUserName("oliviaClark");
		c5.setText("Please, give me a call before the delivery");
		c5.setType("Request");
		c5.setDate("2015-06-22T20:12:45.950");
		addComment(c5);

		Comment c6 = new Comment();
		c6.setUserName("gloriaCavanni");
		c6.setText("I love the color and the shape of the cover");
		c6.setType("Review");
		c6.setDate("2020-11-16T20:44:53.950");
		addComment(c6);

		Comment c7 = new Comment();
		c7.setUserName("markSpecter");
		c7.setText("Adding a sim card slot would be a winner point!");
		c7.setType("Request");
		c7.setDate("1998-01-16T20:44:53.950");
		addComment(c7);

		Comment c8 = new Comment();
		c8.setUserName("markSpecter");
		c8.setText("Nice product, my kids love it.");
		c8.setType("Review");
		c8.setDate("2013-04-16T20:44:53.950");
		addComment(c8);
		
		Comment c9 = new Comment();
		c9.setUserName("markSpecter");
		c9.setText("Too bad, too late.");
		c9.setType("Complain");
		c9.setDate("2018-04-16T20:44:53.950");
		addComment(c9);
		
		Comment c10 = new Comment();
		c10.setUserName("markSpecter");
		c10.setText("It is bigger than it look in the image, but it does the work");
		c10.setType("Review");
		c10.setDate("2019-08-02T12:31:53.950");
		addComment(c10);

		Comment c11 = new Comment();
		c11.setUserName("testUser");
		c11.setText("This is a test comment");
		c11.setType(null);
		c11.setDate("2020-08-02T12:31:53.950");
		addComment(c11);

	
	}

	/* Comments */

	@Override
	public List<Comment> getAllComments() {
		List<Comment> comments = new ArrayList<Comment>(commentMap.values());
		return comments;
	}

	@Override
	public void addComment(Comment c) {
		String id = "c" + index++;
		if (profileMap.containsKey(c.getUserName())) {
			profileMap.get(c.getUserName()).addComment(c);
		} else {
			Profile p = new Profile();
			p.setUserName(c.getUserName());
			p.addComment(c);
			profileMap.put(p.getUserName(), p);
		}
		c.setId(id);
		if (c.getDate() != null) {
			c.setDate(c.getDate());
		} else {
			c.setDate(LocalDateTime.now().toString());
		}

		commentMap.put(id, c);
	}

	@Override
	public Comment getComment(String commentId) {
		return commentMap.get(commentId);
	}

	@Override
	public void deleteComment(String commentId) {

		Comment c = commentMap.get(commentId);
		commentMap.remove(commentId);
		profileMap.get(c.getUserName()).deleteComment(commentId);

	}

	@Override
	public void updateComment(Comment c) {
		Comment comment = commentMap.get(c.getId());
		comment.setText(c.getText());
		if (c.getDate() != null) {
			c.setDate(c.getDate());
		} else {
			c.setDate(LocalDateTime.now().toString());
		}

	}

	/* Profiles */

	@Override
	public List<Profile> getAllProfiles() {
		List<Profile> profiles = new ArrayList<Profile>(profileMap.values());
		return profiles;
	}

	@Override
	public void addProfile(Profile p) {
		p.setComments(new ArrayList<Comment>());
		for (Comment e : commentMap.values()) {
			if (e.getUserName().equals(p.getUserName())) {
				p.addComment(e);
			}
		}
		profileMap.put(p.getUserName(), p);

	}


	@Override
	public Profile getProfile(String userName) {
		return profileMap.get(userName);
	}

	@Override
	public void deleteProfile(String userName) {
		Set<Comment> comments = new HashSet<Comment>(commentMap.values());
		for (Comment e : comments) {
			if (e.getUserName().equals(userName)) {
				deleteComment(e.getId());
			}
		}
		profileMap.remove(userName);

	}

}
