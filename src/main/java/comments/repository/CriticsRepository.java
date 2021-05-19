package comments.repository;

import comments.model.Comment;
import comments.model.Profile;

import java.util.List;


public interface CriticsRepository {
	
	/*Comments*/
	public List<Comment> getAllComments();
	public void addComment(Comment c);
	public Comment getComment(String commentId);
	public void deleteComment(String commentId);
	public void updateComment(Comment c);
	
	/*Profiles*/
	public List<Profile> getAllProfiles();
	public void addProfile(Profile p);
	public Profile getProfile(String username);
	public void deleteProfile(String username);
	
}
