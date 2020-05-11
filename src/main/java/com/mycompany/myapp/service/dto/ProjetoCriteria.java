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
 * Criteria class for the {@link com.mycompany.myapp.domain.Projeto} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProjetoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /projetos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProjetoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter descricao;

    private StringFilter dataInicio;

    private StringFilter dataFim;

    private LongFilter gestorId;

    private LongFilter empregadosId;

    private LongFilter listaempregadoId;

    public ProjetoCriteria() {
    }

    public ProjetoCriteria(ProjetoCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nome = other.nome == null ? null : other.nome.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.dataInicio = other.dataInicio == null ? null : other.dataInicio.copy();
        this.dataFim = other.dataFim == null ? null : other.dataFim.copy();
        this.gestorId = other.gestorId == null ? null : other.gestorId.copy();
        this.empregadosId = other.empregadosId == null ? null : other.empregadosId.copy();
        this.listaempregadoId = other.listaempregadoId == null ? null : other.listaempregadoId.copy();
    }

    @Override
    public ProjetoCriteria copy() {
        return new ProjetoCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public StringFilter getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(StringFilter dataInicio) {
        this.dataInicio = dataInicio;
    }

    public StringFilter getDataFim() {
        return dataFim;
    }

    public void setDataFim(StringFilter dataFim) {
        this.dataFim = dataFim;
    }

    public LongFilter getGestorId() {
        return gestorId;
    }

    public void setGestorId(LongFilter gestorId) {
        this.gestorId = gestorId;
    }

    public LongFilter getEmpregadosId() {
        return empregadosId;
    }

    public void setEmpregadosId(LongFilter empregadosId) {
        this.empregadosId = empregadosId;
    }

    public LongFilter getListaempregadoId() {
        return listaempregadoId;
    }

    public void setListaempregadoId(LongFilter listaempregadoId) {
        this.listaempregadoId = listaempregadoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProjetoCriteria that = (ProjetoCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(dataInicio, that.dataInicio) &&
            Objects.equals(dataFim, that.dataFim) &&
            Objects.equals(gestorId, that.gestorId) &&
            Objects.equals(empregadosId, that.empregadosId) &&
            Objects.equals(listaempregadoId, that.listaempregadoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        nome,
        descricao,
        dataInicio,
        dataFim,
        gestorId,
        empregadosId,
        listaempregadoId
        );
    }

    @Override
    public String toString() {
        return "ProjetoCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (nome != null ? "nome=" + nome + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
                (dataInicio != null ? "dataInicio=" + dataInicio + ", " : "") +
                (dataFim != null ? "dataFim=" + dataFim + ", " : "") +
                (gestorId != null ? "gestorId=" + gestorId + ", " : "") +
                (empregadosId != null ? "empregadosId=" + empregadosId + ", " : "") +
                (listaempregadoId != null ? "listaempregadoId=" + listaempregadoId + ", " : "") +
            "}";
    }

}
