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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "plano")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plano.findAll", query = "SELECT p FROM Plano p")
    , @NamedQuery(name = "Plano.findByIdPlano", query = "SELECT p FROM Plano p WHERE p.idPlano = :idPlano")
    , @NamedQuery(name = "Plano.findByDescricao", query = "SELECT p FROM Plano p WHERE p.descricao = :descricao")
    , @NamedQuery(name = "Plano.findByPreco", query = "SELECT p FROM Plano p WHERE p.preco = :preco")})
public class Plano implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_plano")
    private Integer idPlano;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "preco")
    private String preco;
    @OneToMany(mappedBy = "idPlano")
    private Collection<Assinatura> assinaturaCollection;

    public Plano() {
    }

    public Plano(Integer idPlano) {
        this.idPlano = idPlano;
    }

    public Integer getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(Integer idPlano) {
        this.idPlano = idPlano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
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
        hash += (idPlano != null ? idPlano.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plano)) {
            return false;
        }
        Plano other = (Plano) object;
        if ((this.idPlano == null && other.idPlano != null) || (this.idPlano != null && !this.idPlano.equals(other.idPlano))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Plano[ idPlano=" + idPlano + " ]";
    }
    
}
