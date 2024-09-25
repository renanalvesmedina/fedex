package com.mercurio.lms.fretecarreteirocoletaentrega.action;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.FormatUtils;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.ConsultarRimReciboAction"
 */

public class ConsultarRimReciboAction  {
	
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	
	private  static final int TP_DOCUMENTO_SERVICO = 0;
	private  static final int NR_DOCTO_SERVICO = 1;
	private  static final int SG_DOCTO_SERVICO = 2;
	private  static final int DH_DOCTO_SERVICO = 3;
	private  static final int MOEDA_DOCTO_SERVICO = 5;
	private  static final int VL_DOCTO_SERVICO = 6;
	private  static final int RI_FILIAL = 7;
	private  static final int RI_NR = 8;
	private  static final int RI_DH = 10;
	private  static final int RI_MOEDA = 12;
	private  static final int RI_VL = 13;
	private  static final int RI_ID = 14;
	private  static final int RI_FILIAL_ID = 15;
	private  static final int RI_FILIAL_NOME = 16;

	public TypedFlatMap findRim(TypedFlatMap map) {
		ReciboFreteCarreteiro rfc = reciboFreteCarreteiroService.findByIdCustomColeta(map.getLong("idReciboFreteCarreteiro"));

    	TypedFlatMap retorno = new TypedFlatMap();
    	
    	retorno.put("idReciboFreteCarreteiro",rfc.getIdReciboFreteCarreteiro());
    	
    	Filial filial = rfc.getFilial();
		retorno.put("filial.idFilial",filial.getIdFilial());
    	retorno.put("filial.sgFilial",filial.getSgFilial());

    	ReciboFreteCarreteiro reciboComplementado = rfc.getReciboComplementado();
    	String nrRecibo = FormatUtils.formatLongWithZeros(rfc.getNrReciboFreteCarreteiro(),"0000000000");
    	retorno.put("nrReciboFreteCarreteiro",nrRecibo);
    	retorno.put("nrReciboFreteCarreteiro2",	nrRecibo + (reciboComplementado != null ? "C" : ""));
    	
    	DomainValue tpSituacaoRecibo = rfc.getTpSituacaoRecibo();
		retorno.put("tpSituacaoRecibo.value",tpSituacaoRecibo.getValue());
    	retorno.put("tpSituacaoRecibo.description",tpSituacaoRecibo.getDescription());
    	
    	Pessoa proprietarioPessoa = rfc.getProprietario().getPessoa();
    	retorno.put("proprietario.idProprietario",proprietarioPessoa.getIdPessoa());
    	retorno.put("proprietario.pessoa.nmPessoa",proprietarioPessoa.getNmPessoa());
    	
    	String nrIdentificacaoProprietario = FormatUtils.formatIdentificacao(proprietarioPessoa.getTpIdentificacao(),proprietarioPessoa.getNrIdentificacao());
    	retorno.put("proprietario.pessoa.nrIdentificacaoFormatado",nrIdentificacaoProprietario);
    	
    	MeioTransporte meioTransporte = rfc.getMeioTransporteRodoviario().getMeioTransporte();
    	retorno.put("meioTransporte.idMeioTransporte",meioTransporte.getIdMeioTransporte());
    	retorno.put("meioTransporte.nrFrota",meioTransporte.getNrFrota());
    	retorno.put("meioTransporte.nrIdentificador",meioTransporte.getNrIdentificador());
  
    	return retorno;
    }
	
	
	public ResultSetPage findReciboIndenizacao(TypedFlatMap map){
	    
	    ResultSetPage result = reciboIndenizacaoService.findReciboIndenizacaoByIdRecidoFreteCarreteiro(map.getLong("idReciboFreteCarreteiro"));
	    
	    
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
    	mapResult.put("doctoServico.tpDocumentoServico",objResult[TP_DOCUMENTO_SERVICO]);
    	mapResult.put("doctoServico.nrDoctoServico",objResult[NR_DOCTO_SERVICO]);    	
  	    mapResult.put("doctoServico.filialByIdFilialOrigem.sgFilial",objResult[SG_DOCTO_SERVICO]);  	    
  	    mapResult.put("doctoServico.dhEmissao",objResult[DH_DOCTO_SERVICO]);
  	    mapResult.put("doctoServico.moeda",objResult[MOEDA_DOCTO_SERVICO]);
  	    mapResult.put("doctoServico.vlMercadoria",objResult[VL_DOCTO_SERVICO]);
  	    mapResult.put("reciboIndenizacao.filial",objResult[RI_FILIAL]);
  	    mapResult.put("reciboIndenizacao.nrReciboIndenizacao",objResult[RI_NR]);
  	    mapResult.put("reciboIndenizacao.dhEmissao",objResult[RI_DH]);  	    
  	    mapResult.put("reciboIndenizacao.moeda",objResult[RI_MOEDA]);
  	    mapResult.put("reciboIndenizacao.nrValorIndenizacaoReal",objResult[RI_VL]);
  	    mapResult.put("idReciboIndenizacao",objResult[RI_ID]);  	    
  	    mapResult.put("urlRecibo",getParametrosUrl(objResult));
  	    
		return mapResult;
	}


	private String getParametrosUrl(Object[] objResult) {
		return "'&idReciboIndenizacao="+objResult[RI_ID]+"&filial.sgFilial="+objResult[RI_FILIAL]+"&nrReciboIndenizacao="+objResult[RI_NR]+"&filial.idFilial="+objResult[RI_FILIAL_ID]+"&"+"filial.pessoa.nmFantasia="+objResult[RI_FILIAL_NOME]+"'";
	}


	public Integer getRowCountFindReciboIndenizacao(TypedFlatMap map){
		Long idReciboFreteCarreteiro = map.getLong("idReciboFreteCarreteiro");
    	return reciboIndenizacaoService.getRowCountgetReciboIndenizacaoByIdReciboFreteCarreteiro(idReciboFreteCarreteiro);
    }
	
	
	
	public ReciboFreteCarreteiroService getReciboFreteCarreteiroService() {
		return reciboFreteCarreteiroService;
	}


	public void setReciboFreteCarreteiroService(
			ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}
	
	public ReciboIndenizacaoService getReciboIndenizacaoService() {
		return reciboIndenizacaoService;
	}


	public void setReciboIndenizacaoService(
			ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}

}