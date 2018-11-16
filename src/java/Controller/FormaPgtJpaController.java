/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Controller.exceptions.NonexistentEntityException;
import Controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entidades.Assinatura;
import Entidades.FormaPgt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alissonfernando
 */
public class FormaPgtJpaController implements Serializable {

    public FormaPgtJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(FormaPgt formaPgt) throws PreexistingEntityException, Exception {
        if (formaPgt.getAssinaturaCollection() == null) {
            formaPgt.setAssinaturaCollection(new ArrayList<Assinatura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Assinatura> attachedAssinaturaCollection = new ArrayList<Assinatura>();
            for (Assinatura assinaturaCollectionAssinaturaToAttach : formaPgt.getAssinaturaCollection()) {
                assinaturaCollectionAssinaturaToAttach = em.getReference(assinaturaCollectionAssinaturaToAttach.getClass(), assinaturaCollectionAssinaturaToAttach.getIdAssinatura());
                attachedAssinaturaCollection.add(assinaturaCollectionAssinaturaToAttach);
            }
            formaPgt.setAssinaturaCollection(attachedAssinaturaCollection);
            em.persist(formaPgt);
            for (Assinatura assinaturaCollectionAssinatura : formaPgt.getAssinaturaCollection()) {
                FormaPgt oldIdFormaPgtOfAssinaturaCollectionAssinatura = assinaturaCollectionAssinatura.getIdFormaPgt();
                assinaturaCollectionAssinatura.setIdFormaPgt(formaPgt);
                assinaturaCollectionAssinatura = em.merge(assinaturaCollectionAssinatura);
                if (oldIdFormaPgtOfAssinaturaCollectionAssinatura != null) {
                    oldIdFormaPgtOfAssinaturaCollectionAssinatura.getAssinaturaCollection().remove(assinaturaCollectionAssinatura);
                    oldIdFormaPgtOfAssinaturaCollectionAssinatura = em.merge(oldIdFormaPgtOfAssinaturaCollectionAssinatura);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findFormaPgt(formaPgt.getIdFormaPgt()) != null) {
                throw new PreexistingEntityException("FormaPgt " + formaPgt + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(FormaPgt formaPgt) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            FormaPgt persistentFormaPgt = em.find(FormaPgt.class, formaPgt.getIdFormaPgt());
            Collection<Assinatura> assinaturaCollectionOld = persistentFormaPgt.getAssinaturaCollection();
            Collection<Assinatura> assinaturaCollectionNew = formaPgt.getAssinaturaCollection();
            Collection<Assinatura> attachedAssinaturaCollectionNew = new ArrayList<Assinatura>();
            for (Assinatura assinaturaCollectionNewAssinaturaToAttach : assinaturaCollectionNew) {
                assinaturaCollectionNewAssinaturaToAttach = em.getReference(assinaturaCollectionNewAssinaturaToAttach.getClass(), assinaturaCollectionNewAssinaturaToAttach.getIdAssinatura());
                attachedAssinaturaCollectionNew.add(assinaturaCollectionNewAssinaturaToAttach);
            }
            assinaturaCollectionNew = attachedAssinaturaCollectionNew;
            formaPgt.setAssinaturaCollection(assinaturaCollectionNew);
            formaPgt = em.merge(formaPgt);
            for (Assinatura assinaturaCollectionOldAssinatura : assinaturaCollectionOld) {
                if (!assinaturaCollectionNew.contains(assinaturaCollectionOldAssinatura)) {
                    assinaturaCollectionOldAssinatura.setIdFormaPgt(null);
                    assinaturaCollectionOldAssinatura = em.merge(assinaturaCollectionOldAssinatura);
                }
            }
            for (Assinatura assinaturaCollectionNewAssinatura : assinaturaCollectionNew) {
                if (!assinaturaCollectionOld.contains(assinaturaCollectionNewAssinatura)) {
                    FormaPgt oldIdFormaPgtOfAssinaturaCollectionNewAssinatura = assinaturaCollectionNewAssinatura.getIdFormaPgt();
                    assinaturaCollectionNewAssinatura.setIdFormaPgt(formaPgt);
                    assinaturaCollectionNewAssinatura = em.merge(assinaturaCollectionNewAssinatura);
                    if (oldIdFormaPgtOfAssinaturaCollectionNewAssinatura != null && !oldIdFormaPgtOfAssinaturaCollectionNewAssinatura.equals(formaPgt)) {
                        oldIdFormaPgtOfAssinaturaCollectionNewAssinatura.getAssinaturaCollection().remove(assinaturaCollectionNewAssinatura);
                        oldIdFormaPgtOfAssinaturaCollectionNewAssinatura = em.merge(oldIdFormaPgtOfAssinaturaCollectionNewAssinatura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = formaPgt.getIdFormaPgt();
                if (findFormaPgt(id) == null) {
                    throw new NonexistentEntityException("The formaPgt with id " + id + " no longer exists.");
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
            FormaPgt formaPgt;
            try {
                formaPgt = em.getReference(FormaPgt.class, id);
                formaPgt.getIdFormaPgt();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The formaPgt with id " + id + " no longer exists.", enfe);
            }
            Collection<Assinatura> assinaturaCollection = formaPgt.getAssinaturaCollection();
            for (Assinatura assinaturaCollectionAssinatura : assinaturaCollection) {
                assinaturaCollectionAssinatura.setIdFormaPgt(null);
                assinaturaCollectionAssinatura = em.merge(assinaturaCollectionAssinatura);
            }
            em.remove(formaPgt);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<FormaPgt> findFormaPgtEntities() {
        return findFormaPgtEntities(true, -1, -1);
    }

    public List<FormaPgt> findFormaPgtEntities(int maxResults, int firstResult) {
        return findFormaPgtEntities(false, maxResults, firstResult);
    }

    private List<FormaPgt> findFormaPgtEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(FormaPgt.class));
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

    public FormaPgt findFormaPgt(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(FormaPgt.class, id);
        } finally {
            em.close();
        }
    }

    public int getFormaPgtCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<FormaPgt> rt = cq.from(FormaPgt.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
