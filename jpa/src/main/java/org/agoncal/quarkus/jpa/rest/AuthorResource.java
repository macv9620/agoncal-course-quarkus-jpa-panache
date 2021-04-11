package org.agoncal.quarkus.jpa.rest;

import org.agoncal.quarkus.jpa.model.Author;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/jpa/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

  @Inject
  EntityManager em;
  @Inject
  Logger logger;


  @POST
  @Transactional
  public Response persistAuthor(Author author, @Context UriInfo uriInfo) {
    em.persist(author);
    URI uri = uriInfo.getAbsolutePathBuilder().path(Long.toString(author.getId())).build();
    logger.info("New author created with URI " + uri.toString());
    return Response.created(uri).build();
  }

  @GET
  @Path("/{id}")
  public Author findAuthorById(@PathParam("id") Long id) {
    return em.find(Author.class, id);
  }

  @GET
  public List<Author> findAuthors() {
    return em.createQuery("select a from Author a", Author.class).getResultList();
  }

  @GET
  @Path("/count")
  public Long countAuthors() {
    return em.createQuery("select count(a) from Author a", Long.class).getSingleResult();
  }

  @DELETE
  @Transactional
  @Path("/{id}")
  public void deleteAuthor(@PathParam("id") Long id) {
    em.remove(em.getReference(Author.class, id));
  }
}