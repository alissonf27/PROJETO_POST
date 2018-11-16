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
import Entidades.Role;
import Entidades.Assinatura;
import java.util.ArrayList;
import java.util.Collection;
import Entidades.Post;
import Entidades.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alissonfernando
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getAssinaturaCollection() == null) {
            user.setAssinaturaCollection(new ArrayList<Assinatura>());
        }
        if (user.getPostCollection() == null) {
            user.setPostCollection(new ArrayList<Post>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Role idRole = user.getIdRole();
            if (idRole != null) {
                idRole = em.getReference(idRole.getClass(), idRole.getIdRole());
                user.setIdRole(idRole);
            }
            Collection<Assinatura> attachedAssinaturaCollection = new ArrayList<Assinatura>();
            for (Assinatura assinaturaCollectionAssinaturaToAttach : user.getAssinaturaCollection()) {
                assinaturaCollectionAssinaturaToAttach = em.getReference(assinaturaCollectionAssinaturaToAttach.getClass(), assinaturaCollectionAssinaturaToAttach.getIdAssinatura());
                attachedAssinaturaCollection.add(assinaturaCollectionAssinaturaToAttach);
            }
            user.setAssinaturaCollection(attachedAssinaturaCollection);
            Collection<Post> attachedPostCollection = new ArrayList<Post>();
            for (Post postCollectionPostToAttach : user.getPostCollection()) {
                postCollectionPostToAttach = em.getReference(postCollectionPostToAttach.getClass(), postCollectionPostToAttach.getIdPost());
                attachedPostCollection.add(postCollectionPostToAttach);
            }
            user.setPostCollection(attachedPostCollection);
            em.persist(user);
            if (idRole != null) {
                idRole.getUserCollection().add(user);
                idRole = em.merge(idRole);
            }
            for (Assinatura assinaturaCollectionAssinatura : user.getAssinaturaCollection()) {
                User oldIdUserOfAssinaturaCollectionAssinatura = assinaturaCollectionAssinatura.getIdUser();
                assinaturaCollectionAssinatura.setIdUser(user);
                assinaturaCollectionAssinatura = em.merge(assinaturaCollectionAssinatura);
                if (oldIdUserOfAssinaturaCollectionAssinatura != null) {
                    oldIdUserOfAssinaturaCollectionAssinatura.getAssinaturaCollection().remove(assinaturaCollectionAssinatura);
                    oldIdUserOfAssinaturaCollectionAssinatura = em.merge(oldIdUserOfAssinaturaCollectionAssinatura);
                }
            }
            for (Post postCollectionPost : user.getPostCollection()) {
                User oldIdAuthorOfPostCollectionPost = postCollectionPost.getIdAuthor();
                postCollectionPost.setIdAuthor(user);
                postCollectionPost = em.merge(postCollectionPost);
                if (oldIdAuthorOfPostCollectionPost != null) {
                    oldIdAuthorOfPostCollectionPost.getPostCollection().remove(postCollectionPost);
                    oldIdAuthorOfPostCollectionPost = em.merge(oldIdAuthorOfPostCollectionPost);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getIdUser());
            Role idRoleOld = persistentUser.getIdRole();
            Role idRoleNew = user.getIdRole();
            Collection<Assinatura> assinaturaCollectionOld = persistentUser.getAssinaturaCollection();
            Collection<Assinatura> assinaturaCollectionNew = user.getAssinaturaCollection();
            Collection<Post> postCollectionOld = persistentUser.getPostCollection();
            Collection<Post> postCollectionNew = user.getPostCollection();
            if (idRoleNew != null) {
                idRoleNew = em.getReference(idRoleNew.getClass(), idRoleNew.getIdRole());
                user.setIdRole(idRoleNew);
            }
            if (assinaturaCollectionOld != null) {
                Collection<Assinatura> attachedAssinaturaCollectionNew = new ArrayList<Assinatura>();
                for (Assinatura assinaturaCollectionNewAssinaturaToAttach : assinaturaCollectionNew) {
                    assinaturaCollectionNewAssinaturaToAttach = em.getReference(assinaturaCollectionNewAssinaturaToAttach.getClass(), assinaturaCollectionNewAssinaturaToAttach.getIdAssinatura());
                    attachedAssinaturaCollectionNew.add(assinaturaCollectionNewAssinaturaToAttach);
                }
                assinaturaCollectionNew = attachedAssinaturaCollectionNew;
                user.setAssinaturaCollection(assinaturaCollectionNew);
                Collection<Post> attachedPostCollectionNew = new ArrayList<Post>();
                for (Post postCollectionNewPostToAttach : postCollectionNew) {
                    postCollectionNewPostToAttach = em.getReference(postCollectionNewPostToAttach.getClass(), postCollectionNewPostToAttach.getIdPost());
                    attachedPostCollectionNew.add(postCollectionNewPostToAttach);
                }
                postCollectionNew = attachedPostCollectionNew;
                user.setPostCollection(postCollectionNew);
                user = em.merge(user);
                if (idRoleOld != null && !idRoleOld.equals(idRoleNew)) {
                    idRoleOld.getUserCollection().remove(user);
                    idRoleOld = em.merge(idRoleOld);
                }
                if (idRoleNew != null && !idRoleNew.equals(idRoleOld)) {
                    idRoleNew.getUserCollection().add(user);
                    idRoleNew = em.merge(idRoleNew);
                }
                for (Assinatura assinaturaCollectionOldAssinatura : assinaturaCollectionOld) {
                    if (!assinaturaCollectionNew.contains(assinaturaCollectionOldAssinatura)) {
                        assinaturaCollectionOldAssinatura.setIdUser(null);
                        assinaturaCollectionOldAssinatura = em.merge(assinaturaCollectionOldAssinatura);
                    }
                }
                for (Assinatura assinaturaCollectionNewAssinatura : assinaturaCollectionNew) {
                    if (!assinaturaCollectionOld.contains(assinaturaCollectionNewAssinatura)) {
                        User oldIdUserOfAssinaturaCollectionNewAssinatura = assinaturaCollectionNewAssinatura.getIdUser();
                        assinaturaCollectionNewAssinatura.setIdUser(user);
                        assinaturaCollectionNewAssinatura = em.merge(assinaturaCollectionNewAssinatura);
                        if (oldIdUserOfAssinaturaCollectionNewAssinatura != null && !oldIdUserOfAssinaturaCollectionNewAssinatura.equals(user)) {
                            oldIdUserOfAssinaturaCollectionNewAssinatura.getAssinaturaCollection().remove(assinaturaCollectionNewAssinatura);
                            oldIdUserOfAssinaturaCollectionNewAssinatura = em.merge(oldIdUserOfAssinaturaCollectionNewAssinatura);
                        }
                    }
                }
                for (Post postCollectionOldPost : postCollectionOld) {
                    if (!postCollectionNew.contains(postCollectionOldPost)) {
                        postCollectionOldPost.setIdAuthor(null);
                        postCollectionOldPost = em.merge(postCollectionOldPost);
                    }
                }
                for (Post postCollectionNewPost : postCollectionNew) {
                    if (!postCollectionOld.contains(postCollectionNewPost)) {
                        User oldIdAuthorOfPostCollectionNewPost = postCollectionNewPost.getIdAuthor();
                        postCollectionNewPost.setIdAuthor(user);
                        postCollectionNewPost = em.merge(postCollectionNewPost);
                        if (oldIdAuthorOfPostCollectionNewPost != null && !oldIdAuthorOfPostCollectionNewPost.equals(user)) {
                            oldIdAuthorOfPostCollectionNewPost.getPostCollection().remove(postCollectionNewPost);
                            oldIdAuthorOfPostCollectionNewPost = em.merge(oldIdAuthorOfPostCollectionNewPost);
                        }
                    }
                }
            } else {
                //faz o update direto na mao aq
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getIdUser();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getIdUser();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Role idRole = user.getIdRole();
            if (idRole != null) {
                idRole.getUserCollection().remove(user);
                idRole = em.merge(idRole);
            }
            Collection<Assinatura> assinaturaCollection = user.getAssinaturaCollection();
            for (Assinatura assinaturaCollectionAssinatura : assinaturaCollection) {
                assinaturaCollectionAssinatura.setIdUser(null);
                assinaturaCollectionAssinatura = em.merge(assinaturaCollectionAssinatura);
            }
            Collection<Post> postCollection = user.getPostCollection();
            for (Post postCollectionPost : postCollection) {
                postCollectionPost.setIdAuthor(null);
                postCollectionPost = em.merge(postCollectionPost);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
