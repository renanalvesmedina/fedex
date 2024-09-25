package com.mercurio.lms.services.expedicao;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.tntbrasil.integracao.domains.ciot.CIOTAlteracaoIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTBuscaIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTBuscaIntegracaoPUDDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTCancelamentoIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTIntegracaoDTO;
import br.com.tntbrasil.integracao.domains.ciot.CIOTSolicitacaoIntegracaoDTO;

import com.mercurio.adsm.rest.BaseRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.expedicao.model.service.CIOTService;
 
@Path("/expedicao/ciotServiceRest") 
public class CIOTServiceRest extends BaseRest{
	
	@InjectInJersey 
	CIOTService ciotService;
	
	@InjectInJersey
	ControleCargaService controleCargaService;
	
	@POST
	@Path("findDados")
	public Response findDados(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO) {
		CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO = ciotService.executeBuscaDados(ciotBuscaIntegracaoDTO);
		return Response.ok(ciotSolicitacaoIntegracaoDTO).build();
	}
	
	@POST
	@Path("findDadosAlteracao")
	public Response findDadosAlteracao(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO) {
		CIOTAlteracaoIntegracaoDTO ciotAlteracaoIntegracaoDTO = ciotService.findDadosParaAlteracaoCiot(ciotBuscaIntegracaoDTO);
		return Response.ok(ciotAlteracaoIntegracaoDTO).build();
	}
	
	@POST
	@Path("findDadosCancelamento")
	public Response findDadosCancelamento(CIOTBuscaIntegracaoDTO ciotBuscaIntegracaoDTO) {
		CIOTCancelamentoIntegracaoDTO ciotCancelamentoIntegracaoDTO = ciotService.findDadosParaCancelamentoCiot(ciotBuscaIntegracaoDTO);
		return Response.ok(ciotCancelamentoIntegracaoDTO).build();
	}
	
	@POST
	@Path("findDadosAlteracaoPUD")
	public Response findDadosAlteracaoPUD(CIOTBuscaIntegracaoPUDDTO ciotBuscaIntegracaoPUDDTO) {
		ciotService.executeBuscaDadosAlteracaoPUD(ciotBuscaIntegracaoPUDDTO);
		return Response.ok().build();
	}
	
	@POST
	@Path("updateExigenciaCIOT")
	public Response updateExigenciaCIOT(CIOTSolicitacaoIntegracaoDTO ciotSolicitacaoIntegracaoDTO) {
		controleCargaService.updateExigenciaCIOTEnviadoIntegCIOT(ciotSolicitacaoIntegracaoDTO.getIdControleCarga(), ciotSolicitacaoIntegracaoDTO.getExigeCIOT(), null);
		return Response.ok(ciotSolicitacaoIntegracaoDTO).build();
	}
	
	@POST
	@Path("storeFromIntegracao")
	public Response storeFromIntegracao(CIOTIntegracaoDTO ciotIntegracaoDTO) {
		return Response.ok(ciotService.storeFromIntegracao(ciotIntegracaoDTO)).build();
	}
} 
