package comments.resources;

import comments.model.Comment;
import comments.repository.CriticsRepository;
import comments.repository.MapCriticsRepository;
//import org.jboss.resteasy.spi.BadRequestException;
//import org.jboss.resteasy.spi.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Path("/comments")
public class CommentResource {

    public static CommentResource _instance = null;
    CriticsRepository repository;

    private CommentResource() {
        repository = MapCriticsRepository.getInstance();
    }

    public static CommentResource getInstance() {
        if (_instance == null) {
            _instance = new CommentResource();
        }
        return _instance;
    }

    public void order(List<Comment> list) {
        // Comparator<Comment> c = Comparator.comparing(x -> x.getDate());
        Collections.sort(list, new ComparatorDateComment());
    }

    public List<Comment> length(List<Comment> list, Integer index) {
        List<Comment> sublist = list.subList(0, index);
        return sublist;
    }

    @GET
    @Produces("application/json")
    public Response getAll(@QueryParam("contains") String word, @QueryParam("type") String type, @QueryParam("order") String order,
            @QueryParam("offset") String offset, @QueryParam("limit") String limit) {

        if(type != null && !type.equals("Review") && !type.equals("Request") && !type.equals("Complain") && !type.equals("All"))
            return Response.status(400).type(MediaType.TEXT_PLAIN).entity("type must be one of Review, Request, Complain or All").build();
//            return Response.status(400).build();

        if(order != null && !order.equals("date") && !order.equals("-date") && !order.equals("+date"))
            return Response.status(400).type(MediaType.TEXT_PLAIN).entity("order must be one of date, -date or +date").build();
//            return Response.status(400).build();


        List<Comment> comments = new ArrayList<Comment>();
        Comment c = null;
        Iterator<Comment> iterator = repository.getAllComments().iterator();
        while (iterator.hasNext()) {
            c = iterator.next();
            if (wordFilter(c, word) && typeFilter(c, type)) {
                comments.add(c);
            }

        }

        if (order != null && (order.equals("date") || order.equals("+date"))) {

            Collections.sort(comments, new ComparatorDateComment());
        }
        if (order != null && order.equals("-date")) {
            Collections.sort(comments, new ComparatorDateCommentReversed());
        }
        Integer off = 0;
        Integer lim = comments.size();


        if (offset != null) {
            try {
                off = new Integer(offset);
                if (off < 0 || off > comments.size()) {
                    off = 0;
                }
            } catch(NumberFormatException e) {
                return Response.status(400).type(MediaType.TEXT_PLAIN).entity("The offset must be a number").build();
            }
        }
        if (limit != null) {
            try {
                lim = new Integer(limit);
                if (lim < 0 || lim + off > comments.size()) {
                    lim = comments.size();
                } else {
                    lim--;
                }
            } catch(NumberFormatException e) {
                return Response.status(400).type(MediaType.TEXT_PLAIN).entity("The limit must be a number").build();
            }

        }
        comments = comments.subList(off, lim + off > comments.size() ? lim : lim + off);

        return Response.ok(comments).build();

    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getComment(@PathParam("id") String commentId) {
        Comment comment = repository.getComment(commentId);
        if (comment == null) {
            return Response.status(404).type(MediaType.TEXT_PLAIN).entity("The comment with id" + commentId + " was not found").type(MediaType.TEXT_PLAIN).build();
//            return Response.status(404).build();
        }
        return Response.ok(comment).build();
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response deleteComment(@PathParam("id") String commentId) {
        Comment comment = repository.getComment(commentId);
        if (comment == null) {
            return Response.status(404).type(MediaType.TEXT_PLAIN).entity("The comment with id" + commentId + " was not found").build();
//            return Response.status(404).build();
        }
        return Response.noContent().build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response addComment(@Context UriInfo uriInfo, Comment c) {
        if (!validateBodyPost(c)) {
            return Response.status(400).type(MediaType.TEXT_PLAIN).entity("You must complete the username and text fields").build();
//            return Response.status(400).build();
        }
        
        if (c.getType() != null && !(c.getType().equals("Review") || c.getType().equals("Request") || c.getType().equals("Complain"))) {
            return Response.status(400).type(MediaType.TEXT_PLAIN).entity("Invalid value for type parameter").build();
//            return Response.status(400).build();
        }
        
        repository.addComment(c);
        UriBuilder ub = uriInfo.getAbsolutePathBuilder().path(this.getClass(), "getComment");
        URI uri = ub.build(c.getId());
        ResponseBuilder resp = Response.created(uri);
        resp.entity(c);
        return resp.build();

    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Response updateComment(Comment c) {
        if(!validateBodyPut(c))
            return Response.status(400).type(MediaType.TEXT_PLAIN).entity("You must complete all fields").build();
//            return Response.status(400).build();
        Comment oldComment = repository.getComment(c.getId());
        if (oldComment == null) {
            return Response.status(404).type(MediaType.TEXT_PLAIN).entity("The comment with id " + c.getId() + " was not found").build();
//            return Response.status(404).build();
        }
        repository.updateComment(c);
        return Response.ok(repository.getComment(c.getId())).build();
    }

    private boolean validateBodyPost(Comment c) {
        return c != null && c.getUserName() != null && !c.getUserName().equals("") && c.getText() != null
                && !c.getText().equals("") && !(c.getType() != null && !c.getType().equals("Review")
                && !c.getType().equals("Request") && !c.getType().equals("Complain"));
    }

    private boolean validateBodyPut(Comment c) {
        return c != null && c.getId() != null && !c.getId().equals("") && c.getUserName() != null
                && !c.getUserName().equals("") && c.getText() != null && !c.getText().equals("")
                && c.getId() != null && !c.getId().equals("") && c.getDate() != null && !c.getDate().equals("")
                && c.getType() != null && (c.getType().equals("Review") || c.getType().equals("Request")
                || c.getType().equals("Complain"));
    }

    // Returns true if the comment include word, or if the word is null or empty.
    private boolean wordFilter(Comment c, String word) {
    	return (word == null || word.equals("") || c.getText().contains(word));
    }
    
    // Returns true if the comment matches the input type.
    private boolean typeFilter(Comment c, String type) {
//    	return (type == null ||  type.equals("") || type.equals("All") || c.getType().equals(type));
        return type == null ||  type.equals("") || type.equals("All") || (c.getType() != null && c.getType().equals(type));
    }
    
    
}
