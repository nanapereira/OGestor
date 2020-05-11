package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Empregado} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.EmpregadoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /empregados?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class EmpregadoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter matricula;

    private StringFilter nome;

    private StringFilter cpf;

    private StringFilter email;

    private StringFilter telefone;

    private StringFilter ramal;

    private LongFilter lotacaoId;

    private LongFilter competenciasId;

    private LongFilter projetosId;

    private LongFilter listaCompetenciasId;

    private LongFilter listaProjetosId;

    public EmpregadoCriteria() {
    }

    public EmpregadoCriteria(EmpregadoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.matricula = other.matricula == null ? null : other.matricula.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.cpf = other.cpf == null ? null : other.cpf.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.telefone = other.telefone == null ? null : other.telefone.copy();
        this.ramal = other.ramal == null ? null : other.ramal.copy();
        this.lotacaoId = other.lotacaoId == null ? null : other.lotacaoId.copy();
        this.competenciasId = other.competenciasId == null ? null : other.competenciasId.copy();
        this.projetosId = other.projetosId == null ? null : other.projetosId.copy();
        this.listaCompetenciasId = other.listaCompetenciasId == null ? null : other.listaCompetenciasId.copy();
        this.listaProjetosId = other.listaProjetosId == null ? null : other.listaProjetosId.copy();
    }

    @Override
    public EmpregadoCriteria copy() {
        return new EmpregadoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getMatricula() {
        return matricula;
    }

    public void setMatricula(StringFilter matricula) {
        this.matricula = matricula;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getCpf() {
        return cpf;
    }

    public void setCpf(StringFilter cpf) {
        this.cpf = cpf;
    }

    public StringFilter getEmail() {
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getTelefone() {
        return telefone;
    }

    public void setTelefone(StringFilter telefone) {
        this.telefone = telefone;
    }

    public StringFilter getRamal() {
        return ramal;
    }

    public void setRamal(StringFilter ramal) {
        this.ramal = ramal;
    }

    public LongFilter getLotacaoId() {
        return lotacaoId;
    }

    public void setLotacaoId(LongFilter lotacaoId) {
        this.lotacaoId = lotacaoId;
    }

    public LongFilter getCompetenciasId() {
        return competenciasId;
    }

    public void setCompetenciasId(LongFilter competenciasId) {
        this.competenciasId = competenciasId;
    }

    public LongFilter getProjetosId() {
        return projetosId;
    }

    public void setProjetosId(LongFilter projetosId) {
        this.projetosId = projetosId;
    }

    public LongFilter getListaCompetenciasId() {
        return listaCompetenciasId;
    }

    public void setListaCompetenciasId(LongFilter listaCompetenciasId) {
        this.listaCompetenciasId = listaCompetenciasId;
    }

    public LongFilter getListaProjetosId() {
        return listaProjetosId;
    }

    public void setListaProjetosId(LongFilter listaProjetosId) {
        this.listaProjetosId = listaProjetosId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final EmpregadoCriteria that = (EmpregadoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(matricula, that.matricula) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(cpf, that.cpf) &&
            Objects.equals(email, that.email) &&
            Objects.equals(telefone, that.telefone) &&
            Objects.equals(ramal, that.ramal) &&
            Objects.equals(lotacaoId, that.lotacaoId) &&
            Objects.equals(competenciasId, that.competenciasId) &&
            Objects.equals(projetosId, that.projetosId) &&
            Objects.equals(listaCompetenciasId, that.listaCompetenciasId) &&
            Objects.equals(listaProjetosId, that.listaProjetosId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        matricula,
        nome,
        cpf,
        email,
        telefone,
        ramal,
        lotacaoId,
        competenciasId,
        projetosId,
        listaCompetenciasId,
        listaProjetosId
        );
    }

    @Override
    public String toString() {
        return "EmpregadoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (matricula != null ? "matricula=" + matricula + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (cpf != null ? "cpf=" + cpf + ", " : "") +
                (email != null ? "email=" + email + ", " : "") +
                (telefone != null ? "telefone=" + telefone + ", " : "") +
                (ramal != null ? "ramal=" + ramal + ", " : "") +
                (lotacaoId != null ? "lotacaoId=" + lotacaoId + ", " : "") +
                (competenciasId != null ? "competenciasId=" + competenciasId + ", " : "") +
                (projetosId != null ? "projetosId=" + projetosId + ", " : "") +
                (listaCompetenciasId != null ? "listaCompetenciasId=" + listaCompetenciasId + ", " : "") +
                (listaProjetosId != null ? "listaProjetosId=" + listaProjetosId + ", " : "") +
            "}";
    }

}
