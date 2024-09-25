package com.mercurio.lms.services.contasareceber;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import br.com.tntbrasil.integracao.domains.financeiro.ArquivoRemessaResponseDTO;
import br.com.tntbrasil.integracao.domains.financeiro.HistoricoBoletoDMN;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.contasreceber.model.service.HistoricoBoletoService;

@Path("/contasareceber/envioBancario") 
public class EnvioBancarioRest {
	
	@InjectInJersey	
	private HistoricoBoletoService historicoBoletoService;
	
	public class BoletosHistoricoResponse {
		private List<HistoricoBoletoDMN> historicoBoletoDTOList;
		private String statusRetorno;
		
		public List<HistoricoBoletoDMN> getHistoricoBoletoDTOList() {
			return historicoBoletoDTOList;
		}
		public void setHistoricoBoletoDTOList(
				List<HistoricoBoletoDMN> historicoBoletoDTOList) {
			this.historicoBoletoDTOList = historicoBoletoDTOList;
		}
		public String getStatusRetorno() {
			return statusRetorno;
		}
		public void setStatusRetorno(String statusRetorno) {
			this.statusRetorno = statusRetorno;
		}
	}
	
	@GET
	@Path("gerarBoletosHistorico")
	public Response gerarBoletosHistorico(){
		BoletosHistoricoResponse boletosHistoricoResponse = new BoletosHistoricoResponse();
		try {
			boletosHistoricoResponse.setHistoricoBoletoDTOList(historicoBoletoService.generateBoletosHistorico());
		} catch (Exception exception) {
			
			if (StringUtils.isNotBlank(exception.getMessage())) {
				boletosHistoricoResponse.setStatusRetorno(exception.getMessage());
			} else {
				boletosHistoricoResponse.setStatusRetorno(exception.getClass().toString());
			}
		}
		return Response.ok(boletosHistoricoResponse).build();
	}
	
	
	
	@POST
	@Path("updateHistoricoBoletoBradesco")
	public Response updateHistoricoBoletoBradesco(ArquivoRemessaResponseDTO dto){
		historicoBoletoService.updateHistoricoBoletoBradesco(dto.getIdHistoricoBoleto(),dto.getSeqRemessa());
		
		return Response.ok().build();
	}
	
}
