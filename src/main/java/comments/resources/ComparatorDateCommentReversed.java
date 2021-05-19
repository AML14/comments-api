package comments.resources;

import comments.model.Comment;

import java.util.Comparator;

public class ComparatorDateCommentReversed implements Comparator<Comment> {

    @Override
    public int compare(Comment c1, Comment c2) {
        // TODO Auto-generated method stub
        return c2.getDate().compareTo(c1.getDate());
    }

}
