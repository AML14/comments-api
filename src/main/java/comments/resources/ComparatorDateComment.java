package comments.resources;

import comments.model.Comment;

import java.util.Comparator;

public class ComparatorDateComment implements Comparator<Comment> {

    @Override
    public int compare(Comment c1, Comment c2) {
        // TODO Auto-generated method stub
        return c1.getDate().compareTo(c2.getDate());
    }

}
