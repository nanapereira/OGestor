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
 * Criteria class for the {@link com.mycompany.myapp.domain.Competencia} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CompetenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /competencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CompetenciaCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter codigo;

    private StringFilter nome;

    private StringFilter descricao;

    private LongFilter empregadosId;

    private LongFilter listaEmpregadosId;

    public CompetenciaCriteria() {
    }

    public CompetenciaCriteria(CompetenciaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.empregadosId = other.empregadosId == null ? null : other.empregadosId.copy();
        this.listaEmpregadosId = other.listaEmpregadosId == null ? null : other.listaEmpregadosId.copy();
    }

    @Override
    public CompetenciaCriteria copy() {
        return new CompetenciaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCodigo() {
        return codigo;
    }

    public void setCodigo(IntegerFilter codigo) {
        this.codigo = codigo;
    }

    public StringFilter getNome() {
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public LongFilter getEmpregadosId() {
        return empregadosId;
    }

    public void setEmpregadosId(LongFilter empregadosId) {
        this.empregadosId = empregadosId;
    }

    public LongFilter getListaEmpregadosId() {
        return listaEmpregadosId;
    }

    public void setListaEmpregadosId(LongFilter listaEmpregadosId) {
        this.listaEmpregadosId = listaEmpregadosId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CompetenciaCriteria that = (CompetenciaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(empregadosId, that.empregadosId) &&
            Objects.equals(listaEmpregadosId, that.listaEmpregadosId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        codigo,
        nome,
        descricao,
        empregadosId,
        listaEmpregadosId
        );
    }

    @Override
    public String toString() {
        return "CompetenciaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
                (empregadosId != null ? "empregadosId=" + empregadosId + ", " : "") +
                (listaEmpregadosId != null ? "listaEmpregadosId=" + listaEmpregadosId + ", " : "") +
            "}";
    }

}
