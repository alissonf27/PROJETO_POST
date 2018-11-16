/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author alissonfernando
 */
@Entity
@Table(name = "tags")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tags.findAll", query = "SELECT t FROM Tags t")
    , @NamedQuery(name = "Tags.findByIdTags", query = "SELECT t FROM Tags t WHERE t.idTags = :idTags")
    , @NamedQuery(name = "Tags.findByDescription", query = "SELECT t FROM Tags t WHERE t.description = :description")})
public class Tags implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tags")
    private Integer idTags;
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "tagsCollection")
    private Collection<Post> postCollection;

    public Tags() {
    }

    public Tags(Integer idTags) {
        this.idTags = idTags;
    }

    public Integer getIdTags() {
        return idTags;
    }

    public void setIdTags(Integer idTags) {
        this.idTags = idTags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Post> getPostCollection() {
        return postCollection;
    }

    public void setPostCollection(Collection<Post> postCollection) {
        this.postCollection = postCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTags != null ? idTags.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tags)) {
            return false;
        }
        Tags other = (Tags) object;
        if ((this.idTags == null && other.idTags != null) || (this.idTags != null && !this.idTags.equals(other.idTags))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Tags[ idTags=" + idTags + " ]";
    }
    
}
