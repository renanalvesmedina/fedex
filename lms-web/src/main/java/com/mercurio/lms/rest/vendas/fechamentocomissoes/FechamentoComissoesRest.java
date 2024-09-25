package com.mercurio.lms.rest.vendas.fechamentocomissoes;

import java.io.Serializable;
import java.util.List; 
import java.util.Map; 

import javax.ws.rs.POST;
import javax.ws.rs.Path; 

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.rest.LmsBaseCrudReportRest; 
import com.mercurio.lms.rest.municipios.dto.RegionalChosenDTO;
import com.mercurio.lms.vendas.model.FechamentoComissao;
import com.mercurio.lms.vendas.model.service.FechamentoComissoesService;
 
@Path("/vendas/fechamentoComissoes") 
public class FechamentoComissoesRest extends LmsBaseCrudReportRest<FechamentoComissoesDTO, FechamentoComissoesDTO, FechamentoComissoesFilterDTO> { 

	@InjectInJersey
	private FechamentoComissoesService fechamentoComissoesService;
	
	@Override 
	protected List<Map<String, String>> getColumns() { 
		return null; 
	} 
 
	@Override 
	protected List<Map<String, Object>> findDataForReport(FechamentoComissoesFilterDTO filter) { 
		return null; 
	} 
 
	@Override 
	protected FechamentoComissoesDTO findById(Long id) { 
		return null; 
	} 
 
	@Override 
	protected Long store(FechamentoComissoesDTO bean) { 
		return null; 
	} 
 
	@Override 
	protected void removeById(Long id) { 
	} 
 
	@Override 
	protected void removeByIds(List<Long> ids) { 
	} 
 
	@Override 
	protected List<FechamentoComissoesDTO> find(FechamentoComissoesFilterDTO filter) { 
		return null; 
	} 
 
	@Override 
	protected Integer count(FechamentoComissoesFilterDTO filter) { 
		return null; 
	} 

	@POST
	@Path("/findHasFechamento")
	public FechamentoComissoesDTO findHasFechamento() {
		FechamentoComissao fechamentoComissaoFechamento = fechamentoComissoesService.findHasAlreadyFechamentoMes("F");
		FechamentoComissao fechamentoComissaoAprovacao = fechamentoComissoesService.findHasAlreadyFechamentoMes("A");
		FechamentoComissao fechamentoComissaoEnvioRh = fechamentoComissoesService.findHasAlreadyFechamentoMes("E");
		
		if (fechamentoComissaoEnvioRh == null && fechamentoComissaoAprovacao == null && fechamentoComissaoFechamento == null) {
			return new FechamentoComissoesDTO(null, false, true, true);
		} else if (fechamentoComissaoEnvioRh == null && fechamentoComissaoAprovacao == null && fechamentoComissaoFechamento != null) {
			Long id = fechamentoComissaoFechamento.getIdFechamentoComissao();
			if ("N".equals(fechamentoComissaoFechamento.getBlRetorno())) {
				return new FechamentoComissoesDTO(id, true, true, true);
			} else {
				return new FechamentoComissoesDTO(id, true, false, true);
			}
		} else if (fechamentoComissaoEnvioRh == null && fechamentoComissaoAprovacao != null) {
			Long id = fechamentoComissaoAprovacao.getIdFechamentoComissao();
			Long idPendencia = fechamentoComissaoAprovacao.getPendenciaAprovacao().getIdPendencia();
			if ("E".equals(fechamentoComissaoAprovacao.getTpSituacaoAprovacao().getValue())) {
				return new FechamentoComissoesDTO(id, true, true, true, idPendencia);
			} else if ("A".equals(fechamentoComissaoAprovacao.getTpSituacaoAprovacao().getValue())) {
				return new FechamentoComissoesDTO(id, true, true, false, idPendencia);
			}
		} else if (fechamentoComissaoEnvioRh != null) {
			Long id = fechamentoComissaoAprovacao.getIdFechamentoComissao();
			Long idPendencia = fechamentoComissaoAprovacao.getPendenciaAprovacao().getIdPendencia();
			return new FechamentoComissoesDTO(id, true, true, true, idPendencia);
		}
		
		throw new IllegalArgumentException();
	}

	
	@POST
	@Path("/storeFechamento")
	public Long storeFechamento() {
		Serializable storedId = fechamentoComissoesService.storeFechamento();
		
		return (Long) storedId;
	}
	
	@POST
	@Path("/storeAprovacao")
	public Long storeAprovacao() {
		fechamentoComissoesService.storeAprovacao();
		
		return (Long) 0L;
	}

	@POST
	@Path("/storeEnvioRh")
	public Long storeEnvioRh() {
		fechamentoComissoesService.storeEnvioRh();
		
		return (Long) 0L;
	}

} 
