package comments.model;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Objects;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Comment {
	private String id;
	private String userName;
	private String text;
	private String date;
	private String type;		// Enum: Review, Request, Complain
	
	public Comment(){}
	
	public Comment(String userName, String text, String date, String type) {
		this.userName=userName;
		this.text = text;
		this.date = date;
		this.type = type;
	}
	
	public Comment(String id, String userName, String text, String date, String type) {
		this.id=id;
		this.userName=userName;
		this.text = text;
		this.date = date;
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.userName);
        hash = 29 * hash + Objects.hashCode(this.text);
        hash = 29 * hash + Objects.hashCode(this.date);
        hash = 29 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.text, other.text)) {
            return false;
        }
        if (!Objects.equals(this.date, other.date)) {
            return false;
        }
        if (!Objects.equals(this.type, other.type)) {
            return false;
        }
        return true;
    }	

}
