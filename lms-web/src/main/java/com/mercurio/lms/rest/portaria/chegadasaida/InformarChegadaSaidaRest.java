package com.mercurio.lms.rest.portaria.chegadasaida;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.portaria.model.service.ConfiguracaoAuditoriaService;
import com.mercurio.lms.portaria.model.service.NewInformarChegadaService;
import com.mercurio.lms.portaria.model.service.NewInformarSaidaService;
import com.mercurio.lms.portaria.model.service.utils.ConstantesPortaria;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.FrotaPlacaChegadaSaidaSuggestDTO;
import com.mercurio.lms.rest.portaria.chegadasaida.dto.InformarChegadaSaidaDTO;

@Path("portaria/informarChegadaSaida")
public class InformarChegadaSaidaRest {
	
	@InjectInJersey
	private NewInformarChegadaService newInformarChegadaService;
	
	@InjectInJersey
	private NewInformarSaidaService newInformarSaidaService;

	@InjectInJersey
	private ConfiguracaoAuditoriaService configuracaoAuditoriaService;
	
	@POST
	@Path("/validarEBuscarDados")
	public Response findDados(FrotaPlacaChegadaSaidaSuggestDTO dto) {
		if (ConstantesPortaria.TIPO_CHEGADA.equals(dto.getTipo())) {
			return Response.ok(newInformarChegadaService.validateAndFindDados(dto.toMap())).build();
		} else if (ConstantesPortaria.TIPO_SAIDA.equals(dto.getTipo())) {
			return findDadosSaida(dto.toMap());
		}
		return Response.noContent().build();
	}

	private Response findDadosSaida(TypedFlatMap parametros) {
		newInformarSaidaService.validateManifestoEmTransito(parametros);
		String msgErro = executeVerificarMeioTransporteParaAuditoriaCodigoMensagem(parametros);
		if (msgErro != null) {
			throw new BusinessException(msgErro);
		}
		return Response.ok(newInformarSaidaService.executeFindDados(parametros)).build();
	}

	private String executeVerificarMeioTransporteParaAuditoriaCodigoMensagem(TypedFlatMap parametros) {
		if (parametros.containsKey("idControleCarga")) {
			return configuracaoAuditoriaService.executeVerificarMeioTransporteParaAuditoriaCodigoMensagem(parametros.getLong("idFilial"), parametros.getLong("idMeioTransporte"));
		}
		return null;
	}

	@POST
	@Path("/salvar")
	public Response salvar(InformarChegadaSaidaDTO dto) {
		if (ConstantesPortaria.TIPO_CHEGADA.equals(dto.getTipo())) {
			newInformarChegadaService.executeSalvar(dto.toMapChegada());
		} else if (ConstantesPortaria.TIPO_SAIDA.equals(dto.getTipo())) {
			newInformarSaidaService.executeSalvar(dto.toMapSaida());
		}
		return Response.ok().build();
	}
	
}