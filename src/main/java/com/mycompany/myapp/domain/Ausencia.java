package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;

import com.mycompany.myapp.domain.enumeration.TipoAusencia;

/**
 * A Ausencia.
 */
@Entity
@Table(name = "ausencia")
public class Ausencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoAusencia tipo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_inicio")
    private String dataInicio;

    @Column(name = "data_fim")
    private String dataFim;

    @ManyToOne
    @JsonIgnoreProperties("ausencias")
    private Empregado empregado;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoAusencia getTipo() {
        return tipo;
    }

    public Ausencia tipo(TipoAusencia tipo) {
        this.tipo = tipo;
        return this;
    }

    public void setTipo(TipoAusencia tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Ausencia descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public Ausencia dataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
        return this;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public Ausencia dataFim(String dataFim) {
        this.dataFim = dataFim;
        return this;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Empregado getEmpregado() {
        return empregado;
    }

    public Ausencia empregado(Empregado empregado) {
        this.empregado = empregado;
        return this;
    }

    public void setEmpregado(Empregado empregado) {
        this.empregado = empregado;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ausencia)) {
            return false;
        }
        return id != null && id.equals(((Ausencia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Ausencia{" +
            "id=" + getId() +
            ", tipo='" + getTipo() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            "}";
    }
}