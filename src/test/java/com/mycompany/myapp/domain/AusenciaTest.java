package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AusenciaTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ausencia.class);
        Ausencia ausencia1 = new Ausencia();
        ausencia1.setId(1L);
        Ausencia ausencia2 = new Ausencia();
        ausencia2.setId(ausencia1.getId());
        assertThat(ausencia1).isEqualTo(ausencia2);
        ausencia2.setId(2L);
        assertThat(ausencia1).isNotEqualTo(ausencia2);
        ausencia1.setId(null);
        assertThat(ausencia1).isNotEqualTo(ausencia2);
    }
}
