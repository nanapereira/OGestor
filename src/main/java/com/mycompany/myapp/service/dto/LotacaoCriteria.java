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
 * Criteria class for the {@link com.mycompany.myapp.domain.Lotacao} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.LotacaoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /lotacaos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LotacaoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter codigo;

    private StringFilter nome;

    private StringFilter descricao;

    public LotacaoCriteria() {
    }

    public LotacaoCriteria(LotacaoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.codigo = other.codigo == null ? null : other.codigo.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
    }

    @Override
    public LotacaoCriteria copy() {
        return new LotacaoCriteria(this);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LotacaoCriteria that = (LotacaoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(codigo, that.codigo) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        codigo,
        nome,
        descricao
        );
    }

    @Override
    public String toString() {
        return "LotacaoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (codigo != null ? "codigo=" + codigo + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
            "}";
    }

}
