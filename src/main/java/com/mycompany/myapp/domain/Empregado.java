package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Empregado.
 */
@Entity
@Table(name = "empregado")
public class Empregado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "matricula")
    private String matricula;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "email")
    private String email;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "ramal")
    private String ramal;

    @ManyToOne
    @JsonIgnoreProperties("empregados")
    private Lotacao lotacao;

    @ManyToMany
    @JoinTable(name = "empregado_competencias",
               joinColumns = @JoinColumn(name = "empregado_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "competencias_id", referencedColumnName = "id"))
    private Set<Competencia> competencias = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "empregado_projetos",
               joinColumns = @JoinColumn(name = "empregado_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "projetos_id", referencedColumnName = "id"))
    private Set<Projeto> projetos = new HashSet<>();

    @ManyToMany(mappedBy = "empregados")
    @JsonIgnore
    private Set<Competencia> listaCompetencias = new HashSet<>();

    @ManyToMany(mappedBy = "empregados")
    @JsonIgnore
    private Set<Projeto> listaProjetos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public Empregado matricula(String matricula) {
        this.matricula = matricula;
        return this;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public Empregado nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public Empregado cpf(String cpf) {
        this.cpf = cpf;
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public Empregado email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Empregado telefone(String telefone) {
        this.telefone = telefone;
        return this;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRamal() {
        return ramal;
    }

    public Empregado ramal(String ramal) {
        this.ramal = ramal;
        return this;
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public Lotacao getLotacao() {
        return lotacao;
    }

    public Empregado lotacao(Lotacao lotacao) {
        this.lotacao = lotacao;
        return this;
    }

    public void setLotacao(Lotacao lotacao) {
        this.lotacao = lotacao;
    }

    public Set<Competencia> getCompetencias() {
        return competencias;
    }

    public Empregado competencias(Set<Competencia> competencias) {
        this.competencias = competencias;
        return this;
    }

    public Empregado addCompetencias(Competencia competencia) {
        this.competencias.add(competencia);
        competencia.getListaEmpregados().add(this);
        return this;
    }

    public Empregado removeCompetencias(Competencia competencia) {
        this.competencias.remove(competencia);
        competencia.getListaEmpregados().remove(this);
        return this;
    }

    public void setCompetencias(Set<Competencia> competencias) {
        this.competencias = competencias;
    }

    public Set<Projeto> getProjetos() {
        return projetos;
    }

    public Empregado projetos(Set<Projeto> projetos) {
        this.projetos = projetos;
        return this;
    }

    public Empregado addProjetos(Projeto projeto) {
        this.projetos.add(projeto);
        projeto.getListaempregados().add(this);
        return this;
    }

    public Empregado removeProjetos(Projeto projeto) {
        this.projetos.remove(projeto);
        projeto.getListaempregados().remove(this);
        return this;
    }

    public void setProjetos(Set<Projeto> projetos) {
        this.projetos = projetos;
    }

    public Set<Competencia> getListaCompetencias() {
        return listaCompetencias;
    }

    public Empregado listaCompetencias(Set<Competencia> competencias) {
        this.listaCompetencias = competencias;
        return this;
    }

    public Empregado addListaCompetencias(Competencia competencia) {
        this.listaCompetencias.add(competencia);
        competencia.getEmpregados().add(this);
        return this;
    }

    public Empregado removeListaCompetencias(Competencia competencia) {
        this.listaCompetencias.remove(competencia);
        competencia.getEmpregados().remove(this);
        return this;
    }

    public void setListaCompetencias(Set<Competencia> competencias) {
        this.listaCompetencias = competencias;
    }

    public Set<Projeto> getListaProjetos() {
        return listaProjetos;
    }

    public Empregado listaProjetos(Set<Projeto> projetos) {
        this.listaProjetos = projetos;
        return this;
    }

    public Empregado addListaProjetos(Projeto projeto) {
        this.listaProjetos.add(projeto);
        projeto.getEmpregados().add(this);
        return this;
    }

    public Empregado removeListaProjetos(Projeto projeto) {
        this.listaProjetos.remove(projeto);
        projeto.getEmpregados().remove(this);
        return this;
    }

    public void setListaProjetos(Set<Projeto> projetos) {
        this.listaProjetos = projetos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Empregado)) {
            return false;
        }
        return id != null && id.equals(((Empregado) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Empregado{" +
            "id=" + getId() +
            ", matricula='" + getMatricula() + "'" +
            ", nome='" + getNome() + "'" +
            ", cpf='" + getCpf() + "'" +
            ", email='" + getEmail() + "'" +
            ", telefone='" + getTelefone() + "'" +
            ", ramal='" + getRamal() + "'" +
            "}";
    }
}
