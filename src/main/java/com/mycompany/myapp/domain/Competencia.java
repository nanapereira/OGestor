package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Competencia.
 */
@Entity
@Table(name = "competencia")
public class Competencia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private Integer codigo;

    @Column(name = "nome")
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToMany
    @JoinTable(name = "competencia_empregados",
               joinColumns = @JoinColumn(name = "competencia_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "empregados_id", referencedColumnName = "id"))
    private Set<Empregado> empregados = new HashSet<>();

    @ManyToMany(mappedBy = "competencias")
    @JsonIgnore
    private Set<Empregado> listaEmpregados = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public Competencia codigo(Integer codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public Competencia nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Competencia descricao(String descricao) {
        this.descricao = descricao;
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<Empregado> getEmpregados() {
        return empregados;
    }

    public Competencia empregados(Set<Empregado> empregados) {
        this.empregados = empregados;
        return this;
    }

    public Competencia addEmpregados(Empregado empregado) {
        this.empregados.add(empregado);
        empregado.getListaCompetencias().add(this);
        return this;
    }

    public Competencia removeEmpregados(Empregado empregado) {
        this.empregados.remove(empregado);
        empregado.getListaCompetencias().remove(this);
        return this;
    }

    public void setEmpregados(Set<Empregado> empregados) {
        this.empregados = empregados;
    }

    public Set<Empregado> getListaEmpregados() {
        return listaEmpregados;
    }

    public Competencia listaEmpregados(Set<Empregado> empregados) {
        this.listaEmpregados = empregados;
        return this;
    }

    public Competencia addListaEmpregados(Empregado empregado) {
        this.listaEmpregados.add(empregado);
        empregado.getCompetencias().add(this);
        return this;
    }

    public Competencia removeListaEmpregados(Empregado empregado) {
        this.listaEmpregados.remove(empregado);
        empregado.getCompetencias().remove(this);
        return this;
    }

    public void setListaEmpregados(Set<Empregado> empregados) {
        this.listaEmpregados = empregados;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Competencia)) {
            return false;
        }
        return id != null && id.equals(((Competencia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Competencia{" +
            "id=" + getId() +
            ", codigo=" + getCodigo() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            "}";
    }
}
