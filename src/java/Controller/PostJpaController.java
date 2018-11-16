/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.User;
import Entidades.Category;
import Entidades.Post;
import Entidades.Tags;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alissonfernando
 */
public class PostJpaController implements Serializable {

    public PostJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Post post) {
        if (post.getTagsCollection() == null) {
            post.setTagsCollection(new ArrayList<Tags>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User idAuthor = post.getIdAuthor();
            if (idAuthor != null) {
                idAuthor = em.getReference(idAuthor.getClass(), idAuthor.getIdUser());
                post.setIdAuthor(idAuthor);
            }
            Category idCategory = post.getIdCategory();
            if (idCategory != null) {
                idCategory = em.getReference(idCategory.getClass(), idCategory.getIdCategory());
                post.setIdCategory(idCategory);
            }
            Collection<Tags> attachedTagsCollection = new ArrayList<Tags>();
            for (Tags tagsCollectionTagsToAttach : post.getTagsCollection()) {
                tagsCollectionTagsToAttach = em.getReference(tagsCollectionTagsToAttach.getClass(), tagsCollectionTagsToAttach.getIdTags());
                attachedTagsCollection.add(tagsCollectionTagsToAttach);
            }
            post.setTagsCollection(attachedTagsCollection);
            em.persist(post);
            if (idAuthor != null) {
                idAuthor.getPostCollection().add(post);
                idAuthor = em.merge(idAuthor);
            }
            if (idCategory != null) {
                idCategory.getPostCollection().add(post);
                idCategory = em.merge(idCategory);
            }
            for (Tags tagsCollectionTags : post.getTagsCollection()) {
                tagsCollectionTags.getPostCollection().add(post);
                tagsCollectionTags = em.merge(tagsCollectionTags);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Post post) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Post persistentPost = em.find(Post.class, post.getIdPost());
            User idAuthorOld = persistentPost.getIdAuthor();
            User idAuthorNew = post.getIdAuthor();
            Category idCategoryOld = persistentPost.getIdCategory();
            Category idCategoryNew = post.getIdCategory();
            Collection<Tags> tagsCollectionOld = persistentPost.getTagsCollection();
            Collection<Tags> tagsCollectionNew = post.getTagsCollection();
            if (idAuthorNew != null) {
                idAuthorNew = em.getReference(idAuthorNew.getClass(), idAuthorNew.getIdUser());
                post.setIdAuthor(idAuthorNew);
            }
            if (idCategoryNew != null) {
                idCategoryNew = em.getReference(idCategoryNew.getClass(), idCategoryNew.getIdCategory());
                post.setIdCategory(idCategoryNew);
            }
            Collection<Tags> attachedTagsCollectionNew = new ArrayList<Tags>();
            for (Tags tagsCollectionNewTagsToAttach : tagsCollectionNew) {
                tagsCollectionNewTagsToAttach = em.getReference(tagsCollectionNewTagsToAttach.getClass(), tagsCollectionNewTagsToAttach.getIdTags());
                attachedTagsCollectionNew.add(tagsCollectionNewTagsToAttach);
            }
            tagsCollectionNew = attachedTagsCollectionNew;
            post.setTagsCollection(tagsCollectionNew);
            post = em.merge(post);
            if (idAuthorOld != null && !idAuthorOld.equals(idAuthorNew)) {
                idAuthorOld.getPostCollection().remove(post);
                idAuthorOld = em.merge(idAuthorOld);
            }
            if (idAuthorNew != null && !idAuthorNew.equals(idAuthorOld)) {
                idAuthorNew.getPostCollection().add(post);
                idAuthorNew = em.merge(idAuthorNew);
            }
            if (idCategoryOld != null && !idCategoryOld.equals(idCategoryNew)) {
                idCategoryOld.getPostCollection().remove(post);
                idCategoryOld = em.merge(idCategoryOld);
            }
            if (idCategoryNew != null && !idCategoryNew.equals(idCategoryOld)) {
                idCategoryNew.getPostCollection().add(post);
                idCategoryNew = em.merge(idCategoryNew);
            }
            for (Tags tagsCollectionOldTags : tagsCollectionOld) {
                if (!tagsCollectionNew.contains(tagsCollectionOldTags)) {
                    tagsCollectionOldTags.getPostCollection().remove(post);
                    tagsCollectionOldTags = em.merge(tagsCollectionOldTags);
                }
            }
            for (Tags tagsCollectionNewTags : tagsCollectionNew) {
                if (!tagsCollectionOld.contains(tagsCollectionNewTags)) {
                    tagsCollectionNewTags.getPostCollection().add(post);
                    tagsCollectionNewTags = em.merge(tagsCollectionNewTags);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = post.getIdPost();
                if (findPost(id) == null) {
                    throw new NonexistentEntityException("The post with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Post post;
            try {
                post = em.getReference(Post.class, id);
                post.getIdPost();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The post with id " + id + " no longer exists.", enfe);
            }
            User idAuthor = post.getIdAuthor();
            if (idAuthor != null) {
                idAuthor.getPostCollection().remove(post);
                idAuthor = em.merge(idAuthor);
            }
            Category idCategory = post.getIdCategory();
            if (idCategory != null) {
                idCategory.getPostCollection().remove(post);
                idCategory = em.merge(idCategory);
            }
            Collection<Tags> tagsCollection = post.getTagsCollection();
            for (Tags tagsCollectionTags : tagsCollection) {
                tagsCollectionTags.getPostCollection().remove(post);
                tagsCollectionTags = em.merge(tagsCollectionTags);
            }
            em.remove(post);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Post> findPostEntities() {
        return findPostEntities(true, -1, -1);
    }

    public List<Post> findPostEntities(int maxResults, int firstResult) {
        return findPostEntities(false, maxResults, firstResult);
    }

    private List<Post> findPostEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Post.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Post findPost(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Post.class, id);
        } finally {
            em.close();
        }
    }

    public int getPostCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Post> rt = cq.from(Post.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
