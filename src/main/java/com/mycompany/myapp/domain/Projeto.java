package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Projeto.
 */
@Entity
@Table(name = "projeto")
public class Projeto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "data_inicio")
    private String dataInicio;

    @Column(name = "data_fim")
    private String dataFim;

    @ManyToOne
    @JsonIgnoreProperties("projetos")
    private Empregado gestor;

    @ManyToMany
    @JoinTable(name = "projeto_empregados",
               joinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "empregados_id", referencedColumnName = "id"))
    private Set<Empregado> empregados = new HashSet<>();

    @ManyToMany(mappedBy = "projetos")
    @JsonIgnore
    private Set<Empregado> listaempregados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Projeto nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Projeto descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public Projeto dataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
        return this;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public Projeto dataFim(String dataFim) {
        this.dataFim = dataFim;
        return this;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }

    public Empregado getGestor() {
        return gestor;
    }

    public Projeto gestor(Empregado empregado) {
        this.gestor = empregado;
        return this;
    }

    public void setGestor(Empregado empregado) {
        this.gestor = empregado;
    }

    public Set<Empregado> getEmpregados() {
        return empregados;
    }

    public Projeto empregados(Set<Empregado> empregados) {
        this.empregados = empregados;
        return this;
    }

    public Projeto addEmpregados(Empregado empregado) {
        this.empregados.add(empregado);
        empregado.getListaProjetos().add(this);
        return this;
    }

    public Projeto removeEmpregados(Empregado empregado) {
        this.empregados.remove(empregado);
        empregado.getListaProjetos().remove(this);
        return this;
    }

    public void setEmpregados(Set<Empregado> empregados) {
        this.empregados = empregados;
    }

    public Set<Empregado> getListaempregados() {
        return listaempregados;
    }

    public Projeto listaempregados(Set<Empregado> empregados) {
        this.listaempregados = empregados;
        return this;
    }

    public Projeto addListaempregado(Empregado empregado) {
        this.listaempregados.add(empregado);
        empregado.getProjetos().add(this);
        return this;
    }

    public Projeto removeListaempregado(Empregado empregado) {
        this.listaempregados.remove(empregado);
        empregado.getProjetos().remove(this);
        return this;
    }

    public void setListaempregados(Set<Empregado> empregados) {
        this.listaempregados = empregados;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Projeto)) {
            return false;
        }
        return id != null && id.equals(((Projeto) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Projeto{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", dataInicio='" + getDataInicio() + "'" +
            ", dataFim='" + getDataFim() + "'" +
            "}";
    }
}
