package com.mercurio.lms.rest.configuracoes.inscricaoestadual;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.rest.LmsBaseCrudReportRest;
import com.mercurio.lms.rest.configuracoes.inscricaoestadual.dto.InscricaoEstadualDTO;
import com.mercurio.lms.rest.configuracoes.inscricaoestadual.dto.InscricaoEstadualFilterDTO;
import com.mercurio.lms.rest.configuracoes.inscricaoestadual.helper.InscricaoEstadualRestPopulateHelper;

/**
 * Rest responsável pelo controle da tela manter inscrição estadual.
 * 
 */
@Path("/configuracoes/manterInscricaoEstadual")
public class InscricaoEstadualRest
		extends
		LmsBaseCrudReportRest<InscricaoEstadualDTO, InscricaoEstadualDTO, InscricaoEstadualFilterDTO> {

	@InjectInJersey
	private InscricaoEstadualService inscricaoEstadualService;

	@InjectInJersey
	private ParametroGeralService parametroGeralService;

	@Override
	protected List<InscricaoEstadualDTO> find(InscricaoEstadualFilterDTO filter) {	
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);	
		
		TypedFlatMap filterMap = InscricaoEstadualRestPopulateHelper.getFilterMap(criteria, filter);
		
		return InscricaoEstadualRestPopulateHelper.getListInscricaoEstadualDTO(inscricaoEstadualService.findPaginatedCustom(filterMap));
	}
	
	@Override
	protected Integer count(InscricaoEstadualFilterDTO filter) {
		TypedFlatMap criteria = super.getTypedFlatMapWithPaginationInfo(filter);
		
		TypedFlatMap filterMap = InscricaoEstadualRestPopulateHelper.getFilterMap(criteria, filter);
		
		return inscricaoEstadualService.getRowCountCustom(filterMap);
	}
	
	@Override
	protected List<Map<String, String>> getColumns() {
		// Estrutura pré-gerada para migração futura da tela.
		return null;
	}

	@Override
	protected List<Map<String, Object>> findDataForReport(
			InscricaoEstadualFilterDTO filter) {
		// Estrutura pré-gerada para migração futura da tela.
		return null;
	}

	@Override
	protected InscricaoEstadualDTO findById(Long arg0) {
		// Estrutura pré-gerada para migração futura da tela.
		return null;
	}

	@Override
	protected void removeById(Long arg0) {
		// Estrutura pré-gerada para migração futura da tela.
	}

	@Override
	protected Long store(InscricaoEstadualDTO arg0) {
		// Estrutura pré-gerada para migração futura da tela.
		return null;
	}
	@Override
	protected void removeByIds(List<Long> arg0) {
		// Estrutura pré-gerada para migração futura da tela.
	}
}