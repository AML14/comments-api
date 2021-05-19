package comments.model;

import java.util.ArrayList;
import java.util.List;

public class Profile {
	private String userName;
	private List<Comment> comments;
	
	public Profile(){}
	
	public Profile(String userName){
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Comment> getComments() {
		return comments;
	}
	
	public void setComments(List<Comment> list){
		this.comments=list;
	}

	public Comment getComment(String id){
		if (comments==null)
			return null;
		
		Comment comment =null;
		for(Comment c: comments)
			if (c.getId().equals(id))
			{
				comment=c;
				break;
			}
		
		return comment;
	}
	
	public void addComment(Comment c){
		if(comments==null){
			comments = new ArrayList<Comment>();
		}
		comments.add(c);
	}
	
	public void deleteComment(Comment c){
		comments.remove(c);
	}
	
	public void deleteComment(String id){
		Comment c = getComment(id);
		if(c != null){
			comments.remove(c);
		}
		
	}
	public void deleteAllComments(){
		comments.removeAll(comments);
	}
	
	

}
