package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class CompetenciaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Competencia.class);
        Competencia competencia1 = new Competencia();
        competencia1.setId(1L);
        Competencia competencia2 = new Competencia();
        competencia2.setId(competencia1.getId());
        assertThat(competencia1).isEqualTo(competencia2);
        competencia2.setId(2L);
        assertThat(competencia1).isNotEqualTo(competencia2);
        competencia1.setId(null);
        assertThat(competencia1).isNotEqualTo(competencia2);
    }
}
