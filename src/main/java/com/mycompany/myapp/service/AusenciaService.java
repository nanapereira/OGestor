package com.mycompany.myapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycompany.myapp.domain.Ausencia;
import com.mycompany.myapp.repository.AusenciaRepository;

@Component
public class AusenciaService {
	
	@Autowired
	public AusenciaRepository ausenciaRepository;
	
//	@Autowired
//	public EmpregadoRepository empregadoRepository;
	
	
	public List<Ausencia> findAllAusenciaWithEmpregadoProjetos() {
		return ausenciaRepository.findAllWithEagerRelationships();
	}
	
	public List<Ausencia> findAllAusenciaWithEmpregadoProjetosByProjeto(Long idProjeto) {
		return ausenciaRepository.findAllWithEagerRelationshipsByIdProjeto(idProjeto);
	}
	
	
}
