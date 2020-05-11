package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.TipoAusencia;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Ausencia} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.AusenciaResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /ausencias?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AusenciaCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TipoAusencia
     */
    public static class TipoAusenciaFilter extends Filter<TipoAusencia> {

        public TipoAusenciaFilter() {
        }

        public TipoAusenciaFilter(TipoAusenciaFilter filter) {
            super(filter);
        }

        @Override
        public TipoAusenciaFilter copy() {
            return new TipoAusenciaFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TipoAusenciaFilter tipo;

    private StringFilter descricao;

    private StringFilter dataInicio;

    private StringFilter dataFim;

    private LongFilter empregadoId;

    public AusenciaCriteria() {
    }

    public AusenciaCriteria(AusenciaCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tipo = other.tipo == null ? null : other.tipo.copy();
        this.descricao = other.descricao == null ? null : other.descricao.copy();
        this.dataInicio = other.dataInicio == null ? null : other.dataInicio.copy();
        this.dataFim = other.dataFim == null ? null : other.dataFim.copy();
        this.empregadoId = other.empregadoId == null ? null : other.empregadoId.copy();
    }

    @Override
    public AusenciaCriteria copy() {
        return new AusenciaCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TipoAusenciaFilter getTipo() {
        return tipo;
    }

    public void setTipo(TipoAusenciaFilter tipo) {
        this.tipo = tipo;
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

    public LongFilter getEmpregadoId() {
        return empregadoId;
    }

    public void setEmpregadoId(LongFilter empregadoId) {
        this.empregadoId = empregadoId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AusenciaCriteria that = (AusenciaCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(tipo, that.tipo) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(dataInicio, that.dataInicio) &&
            Objects.equals(dataFim, that.dataFim) &&
            Objects.equals(empregadoId, that.empregadoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        tipo,
        descricao,
        dataInicio,
        dataFim,
        empregadoId
        );
    }

    @Override
    public String toString() {
        return "AusenciaCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tipo != null ? "tipo=" + tipo + ", " : "") +
                (descricao != null ? "descricao=" + descricao + ", " : "") +
                (dataInicio != null ? "dataInicio=" + dataInicio + ", " : "") +
                (dataFim != null ? "dataFim=" + dataFim + ", " : "") +
                (empregadoId != null ? "empregadoId=" + empregadoId + ", " : "") +
            "}";
    }

}
