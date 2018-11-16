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
import Entidades.Assinatura;
import Entidades.Plano;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alissonfernando
 */
public class PlanoJpaController implements Serializable {

    public PlanoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plano plano) {
        if (plano.getAssinaturaCollection() == null) {
            plano.setAssinaturaCollection(new ArrayList<Assinatura>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Assinatura> attachedAssinaturaCollection = new ArrayList<Assinatura>();
            for (Assinatura assinaturaCollectionAssinaturaToAttach : plano.getAssinaturaCollection()) {
                assinaturaCollectionAssinaturaToAttach = em.getReference(assinaturaCollectionAssinaturaToAttach.getClass(), assinaturaCollectionAssinaturaToAttach.getIdAssinatura());
                attachedAssinaturaCollection.add(assinaturaCollectionAssinaturaToAttach);
            }
            plano.setAssinaturaCollection(attachedAssinaturaCollection);
            em.persist(plano);
            for (Assinatura assinaturaCollectionAssinatura : plano.getAssinaturaCollection()) {
                Plano oldIdPlanoOfAssinaturaCollectionAssinatura = assinaturaCollectionAssinatura.getIdPlano();
                assinaturaCollectionAssinatura.setIdPlano(plano);
                assinaturaCollectionAssinatura = em.merge(assinaturaCollectionAssinatura);
                if (oldIdPlanoOfAssinaturaCollectionAssinatura != null) {
                    oldIdPlanoOfAssinaturaCollectionAssinatura.getAssinaturaCollection().remove(assinaturaCollectionAssinatura);
                    oldIdPlanoOfAssinaturaCollectionAssinatura = em.merge(oldIdPlanoOfAssinaturaCollectionAssinatura);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plano plano) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Plano persistentPlano = em.find(Plano.class, plano.getIdPlano());
            Collection<Assinatura> assinaturaCollectionOld = persistentPlano.getAssinaturaCollection();
            Collection<Assinatura> assinaturaCollectionNew = plano.getAssinaturaCollection();
            Collection<Assinatura> attachedAssinaturaCollectionNew = new ArrayList<Assinatura>();
            for (Assinatura assinaturaCollectionNewAssinaturaToAttach : assinaturaCollectionNew) {
                assinaturaCollectionNewAssinaturaToAttach = em.getReference(assinaturaCollectionNewAssinaturaToAttach.getClass(), assinaturaCollectionNewAssinaturaToAttach.getIdAssinatura());
                attachedAssinaturaCollectionNew.add(assinaturaCollectionNewAssinaturaToAttach);
            }
            assinaturaCollectionNew = attachedAssinaturaCollectionNew;
            plano.setAssinaturaCollection(assinaturaCollectionNew);
            plano = em.merge(plano);
            for (Assinatura assinaturaCollectionOldAssinatura : assinaturaCollectionOld) {
                if (!assinaturaCollectionNew.contains(assinaturaCollectionOldAssinatura)) {
                    assinaturaCollectionOldAssinatura.setIdPlano(null);
                    assinaturaCollectionOldAssinatura = em.merge(assinaturaCollectionOldAssinatura);
                }
            }
            for (Assinatura assinaturaCollectionNewAssinatura : assinaturaCollectionNew) {
                if (!assinaturaCollectionOld.contains(assinaturaCollectionNewAssinatura)) {
                    Plano oldIdPlanoOfAssinaturaCollectionNewAssinatura = assinaturaCollectionNewAssinatura.getIdPlano();
                    assinaturaCollectionNewAssinatura.setIdPlano(plano);
                    assinaturaCollectionNewAssinatura = em.merge(assinaturaCollectionNewAssinatura);
                    if (oldIdPlanoOfAssinaturaCollectionNewAssinatura != null && !oldIdPlanoOfAssinaturaCollectionNewAssinatura.equals(plano)) {
                        oldIdPlanoOfAssinaturaCollectionNewAssinatura.getAssinaturaCollection().remove(assinaturaCollectionNewAssinatura);
                        oldIdPlanoOfAssinaturaCollectionNewAssinatura = em.merge(oldIdPlanoOfAssinaturaCollectionNewAssinatura);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = plano.getIdPlano();
                if (findPlano(id) == null) {
                    throw new NonexistentEntityException("The plano with id " + id + " no longer exists.");
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
            Plano plano;
            try {
                plano = em.getReference(Plano.class, id);
                plano.getIdPlano();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plano with id " + id + " no longer exists.", enfe);
            }
            Collection<Assinatura> assinaturaCollection = plano.getAssinaturaCollection();
            for (Assinatura assinaturaCollectionAssinatura : assinaturaCollection) {
                assinaturaCollectionAssinatura.setIdPlano(null);
                assinaturaCollectionAssinatura = em.merge(assinaturaCollectionAssinatura);
            }
            em.remove(plano);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Plano> findPlanoEntities() {
        return findPlanoEntities(true, -1, -1);
    }

    public List<Plano> findPlanoEntities(int maxResults, int firstResult) {
        return findPlanoEntities(false, maxResults, firstResult);
    }

    private List<Plano> findPlanoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plano.class));
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

    public Plano findPlano(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plano.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlanoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plano> rt = cq.from(Plano.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
