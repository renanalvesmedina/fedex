package com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.dto.FiltroPaginacaoDto;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboConsultaRimRestConstants;
import com.mercurio.lms.rest.fretecarreteirocoletaentrega.recibo.constants.ReciboRestConstants;
import com.mercurio.lms.util.FormatUtils;

@Path("/fretecarreteirocoletaentrega/manterRecibo")
public class ReciboConsultaRimRest {
	
	@InjectInJersey ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	@InjectInJersey ReciboIndenizacaoService reciboIndenizacaoService;
	
	@POST
	@Path("/findRimDados")
	public Response findReciboIndenizacaoDados(FiltroPaginacaoDto filtro) {
		Long idReciboFreteCarreteiro = getIdFreteCarreteiro(filtro);
		ReciboFreteCarreteiro rfc = reciboFreteCarreteiroService.findByIdCustomColeta(idReciboFreteCarreteiro);

    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	retorno.put(ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue(), rfc.getIdReciboFreteCarreteiro());
    	
    	Filial filial = rfc.getFilial();
		retorno.put("filialidFilial",filial.getIdFilial());
    	retorno.put("filialsgFilial",filial.getSgFilial());

    	ReciboFreteCarreteiro reciboComplementado = rfc.getReciboComplementado();
    	String nrRecibo = FormatUtils.formatLongWithZeros(rfc.getNrReciboFreteCarreteiro(),"0000000000");
    	retorno.put("nrReciboFreteCarreteiro",nrRecibo);
    	retorno.put("nrReciboFreteCarreteiro2",	nrRecibo + (reciboComplementado != null ? "C" : ""));
    	
    	DomainValue tpSituacaoRecibo = rfc.getTpSituacaoRecibo();
		retorno.put("tpSituacaoRecibovalue",tpSituacaoRecibo.getValue());
    	retorno.put("tpSituacaoRecibodescription",tpSituacaoRecibo.getDescription().toString());
    	
    	Pessoa proprietarioPessoa = rfc.getProprietario().getPessoa();
    	retorno.put("proprietarioidProprietario",proprietarioPessoa.getIdPessoa());
    	retorno.put("proprietariopessoanmPessoa",proprietarioPessoa.getNmPessoa());
    	
    	String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(proprietarioPessoa.getTpIdentificacao(),proprietarioPessoa.getNrIdentificacao());
    	retorno.put("proprietariopessoanrIdentificacaoFormatado",nrIdentificacaoProprietario);
    	
    	MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();
    	retorno.put("meioTransporteidMeioTransporte",meioTransporte.getIdMeioTransporte());
    	retorno.put("meioTransportenrFrota",meioTransporte.getNrFrota());
    	retorno.put("meioTransportenrIdentificador",meioTransporte.getNrIdentificador());
  
    	return Response.ok(retorno).build();
    }
	
	@POST
	@Path("/findRim")		
	public ResultSetPage findReciboIndenizacao(FiltroPaginacaoDto filtro){
		Long idReciboFreteCarreteiro = getIdFreteCarreteiro(filtro);
	    ResultSetPage result = reciboIndenizacaoService.findReciboIndenizacaoByIdRecidoFreteCarreteiro(idReciboFreteCarreteiro);
	    
	    final List<Object[]> listIndenizacoes = result.getList();
		final List<TypedFlatMap> listRetorno = new ArrayList<TypedFlatMap>(listIndenizacoes.size());

		for (Object[] objResult : listIndenizacoes) {
			TypedFlatMap mapResult = mountMapByObject(objResult);
			listRetorno.add(mapResult);
		}

		result.setList(listRetorno);
		
		return result;
    }

    private TypedFlatMap mountMapByObject(Object[] objResult) {
    	TypedFlatMap mapResult = new TypedFlatMap();
    	mapResult.put("doctoServicotpDocumentoServico",objResult[ReciboConsultaRimRestConstants.TP_DOCUMENTO_SERVICO.getValue()]);
    	mapResult.put("doctoServiconrDoctoServico",objResult[ReciboConsultaRimRestConstants.NR_DOCTO_SERVICO.getValue()]);    	
  	    mapResult.put("doctoServicofilialByIdFilialOrigemSgFilial",objResult[ReciboConsultaRimRestConstants.SG_DOCTO_SERVICO.getValue()]);  	    
  	    mapResult.put("doctoServicodhEmissao",objResult[ReciboConsultaRimRestConstants.DH_DOCTO_SERVICO.getValue()]);
  	    mapResult.put("doctoServicomoeda",objResult[ReciboConsultaRimRestConstants.MOEDA_DOCTO_SERVICO.getValue()]);
  	    mapResult.put("doctoServicovlMercadoria",objResult[ReciboConsultaRimRestConstants.VL_DOCTO_SERVICO.getValue()]);
  	    mapResult.put("reciboIndenizacaofilial",objResult[ReciboConsultaRimRestConstants.RI_FILIAL.getValue()]);
  	    mapResult.put("reciboIndenizacaonrReciboIndenizacao",objResult[ReciboConsultaRimRestConstants.RI_NR.getValue()]);
  	    mapResult.put("reciboIndenizacaodhEmissao",objResult[ReciboConsultaRimRestConstants.RI_DH.getValue()]);  	    
  	    mapResult.put("reciboIndenizacaomoeda",objResult[ReciboConsultaRimRestConstants.RI_MOEDA.getValue()]);
  	    mapResult.put("reciboIndenizacaonrValorIndenizacaoReal",objResult[ReciboConsultaRimRestConstants.RI_VL.getValue()]);
  	    mapResult.put("idReciboIndenizacao",objResult[ReciboConsultaRimRestConstants.RI_ID.getValue()]);  	    
  	    mapResult.put("urlRecibo",getParametrosUrl(objResult));
  	    
		return mapResult;
	}
    
	private String getParametrosUrl(Object[] objResult) {
		StringBuilder url = new StringBuilder();
		
		url.append("&idReciboIndenizacao=");
		url.append(objResult[ReciboConsultaRimRestConstants.RI_ID.getValue()]);
		
		url.append("&filial.sgFilial=");
		url.append(objResult[ReciboConsultaRimRestConstants.RI_FILIAL.getValue()]);
				
		url.append("&nrReciboIndenizacao=");
		url.append(objResult[ReciboConsultaRimRestConstants.RI_NR.getValue()]);
		
		url.append("&filial.idFilial=");
		url.append(objResult[ReciboConsultaRimRestConstants.RI_FILIAL_ID.getValue()]);
		
		url.append("&filial.pessoa.nmFantasia=");
		url.append(objResult[ReciboConsultaRimRestConstants.RI_FILIAL_NOME.getValue()]);		
		
		return url.toString();
	}

	public Integer getRowCountFindReciboIndenizacao(TypedFlatMap map){
		Long idReciboFreteCarreteiro = map.getLong(ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue());
    	return reciboIndenizacaoService.getRowCountgetReciboIndenizacaoByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
    }
		
	private Long getIdFreteCarreteiro(FiltroPaginacaoDto filtro) {
		return Long.valueOf(filtro.getFiltros().get(ReciboRestConstants.ID_RECIBO_FRETE_CARRETEIRO.getValue()).toString());
	}
}