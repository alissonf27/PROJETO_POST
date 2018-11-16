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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author alissonfernando
 */
@Entity
@Table(name = "forma_pgt")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormaPgt.findAll", query = "SELECT f FROM FormaPgt f")
    , @NamedQuery(name = "FormaPgt.findByIdFormaPgt", query = "SELECT f FROM FormaPgt f WHERE f.idFormaPgt = :idFormaPgt")
    , @NamedQuery(name = "FormaPgt.findByDescricao", query = "SELECT f FROM FormaPgt f WHERE f.descricao = :descricao")})
public class FormaPgt implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_forma_pgt")
    private Integer idFormaPgt;
    @Column(name = "descricao")
    private String descricao;
    @OneToMany(mappedBy = "idFormaPgt")
    private Collection<Assinatura> assinaturaCollection;

    public FormaPgt() {
    }

    public FormaPgt(Integer idFormaPgt) {
        this.idFormaPgt = idFormaPgt;
    }

    public Integer getIdFormaPgt() {
        return idFormaPgt;
    }

    public void setIdFormaPgt(Integer idFormaPgt) {
        this.idFormaPgt = idFormaPgt;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Assinatura> getAssinaturaCollection() {
        return assinaturaCollection;
    }

    public void setAssinaturaCollection(Collection<Assinatura> assinaturaCollection) {
        this.assinaturaCollection = assinaturaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormaPgt != null ? idFormaPgt.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormaPgt)) {
            return false;
        }
        FormaPgt other = (FormaPgt) object;
        if ((this.idFormaPgt == null && other.idFormaPgt != null) || (this.idFormaPgt != null && !this.idFormaPgt.equals(other.idFormaPgt))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.FormaPgt[ idFormaPgt=" + idFormaPgt + " ]";
    }
    
}
