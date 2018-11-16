/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alissonfernando
 */
@Entity
@Table(name = "assinatura")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Assinatura.findAll", query = "SELECT a FROM Assinatura a")
    , @NamedQuery(name = "Assinatura.findByIdAssinatura", query = "SELECT a FROM Assinatura a WHERE a.idAssinatura = :idAssinatura")
    , @NamedQuery(name = "Assinatura.findByDesconto", query = "SELECT a FROM Assinatura a WHERE a.desconto = :desconto")
    , @NamedQuery(name = "Assinatura.findByDataInicial", query = "SELECT a FROM Assinatura a WHERE a.dataInicial = :dataInicial")
    , @NamedQuery(name = "Assinatura.findByDataFinal", query = "SELECT a FROM Assinatura a WHERE a.dataFinal = :dataFinal")
    , @NamedQuery(name = "Assinatura.findByStatus", query = "SELECT a FROM Assinatura a WHERE a.status = :status")})
public class Assinatura implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_assinatura")
    private Integer idAssinatura;
    @Column(name = "desconto")
    private Integer desconto;
    @Column(name = "data_inicial")
    @Temporal(TemporalType.DATE)
    private Date dataInicial;
    @Column(name = "data_final")
    @Temporal(TemporalType.DATE)
    private Date dataFinal;
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "id_forma_pgt", referencedColumnName = "id_forma_pgt")
    @ManyToOne
    private FormaPgt idFormaPgt;
    @JoinColumn(name = "id_plano", referencedColumnName = "id_plano")
    @ManyToOne
    private Plano idPlano;
    @JoinColumn(name = "id_user", referencedColumnName = "id_user")
    @ManyToOne
    private User idUser;

    public Assinatura() {
    }

    public Assinatura(Integer idAssinatura) {
        this.idAssinatura = idAssinatura;
    }

    public Integer getIdAssinatura() {
        return idAssinatura;
    }

    public void setIdAssinatura(Integer idAssinatura) {
        this.idAssinatura = idAssinatura;
    }

    public Integer getDesconto() {
        return desconto;
    }

    public void setDesconto(Integer desconto) {
        this.desconto = desconto;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public FormaPgt getIdFormaPgt() {
        return idFormaPgt;
    }

    public void setIdFormaPgt(FormaPgt idFormaPgt) {
        this.idFormaPgt = idFormaPgt;
    }

    public Plano getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(Plano idPlano) {
        this.idPlano = idPlano;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAssinatura != null ? idAssinatura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Assinatura)) {
            return false;
        }
        Assinatura other = (Assinatura) object;
        if ((this.idAssinatura == null && other.idAssinatura != null) || (this.idAssinatura != null && !this.idAssinatura.equals(other.idAssinatura))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Assinatura[ idAssinatura=" + idAssinatura + " ]";
    }
    
}
