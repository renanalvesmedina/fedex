package com.mercurio.lms.contasreceber.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.ItemPreFatura;
import com.mercurio.lms.contasreceber.model.PreFatura;
import com.mercurio.lms.contasreceber.model.service.ItemPreFaturaService;
import com.mercurio.lms.contasreceber.model.service.PreFaturaService;
import com.mercurio.lms.util.FormatUtils;



/**
 *
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.consultarComplementosRomaneiosAction"
 */
public class ConsultarComplementosRomaneiosAction extends CrudAction {

	/** Service padr�o desta action*/
	private ItemPreFaturaService itemPreFaturaService;
	
	/** Utilizando outra service - DomainValueService */
	private DomainValueService domainValueService;
	
	/** Utilizando outra service - PreFaturaService */
	private PreFaturaService preFaturaService;
	
	/** Utilizando outra service - PreFaturaService */
	private MoedaService moedaService;
	
		
	
	/**
	 * Recuperda os dados da PreFatura, Fatura e Filial da Fatura para o idFatura especificado
	 * @author Diego Umpierre
	 *
	 *
	 * @param id da fatura 
	 * @return  dados a partir do id da fatura
	 */
	public Serializable findByIdComplemento(TypedFlatMap criteria) {
		
		Long idFatura = criteria.getLong("idFatura"); 
		
		
		if ( idFatura == null ){
			return null;
		}
	
		
		PreFatura preFat = (PreFatura) getPreFaturaService().findByIdFatura(idFatura);
 		
		TypedFlatMap nova = new TypedFlatMap();
		
		if (preFat != null){
			nova.put("numeroPreFatura", 	preFat.getNrPreFatura() );
			
			nova.put("sgFilialFatura", 		preFat.getFatura().getFilialByIdFilial().getSgFilial() );
			nova.put("nrFatura", 			FormatUtils.completaDados(preFat.getFatura().getNrFatura(),"0",10,0,true) );
			
			nova.put("codigoDeposito", 		preFat.getCdDeposito() );
			nova.put("cliente", 			preFat.getNmCliente() );
			nova.put("codigoTransportadora", preFat.getCdTransportadora() );
			nova.put("cnpjTransportadora", 	FormatUtils.formatCNPJ(preFat.getNrCnpjTransportadora() ));
			
			nova.put("tipoFrete", 			getDomainValueService().findDomainValueByValue("DM_FRETE_DHL",preFat.getTpFrete()).getDescription()  );
			nova.put("modalidadeFrete", 	getDomainValueService().findDomainValueByValue("DM_MODALIDADE_FRETE_DHL",preFat.getTpModalidadeFrete()).getDescription());
			if (preFat.getTpFreteUrbano().equals("I") || preFat.getTpFreteUrbano().equals("M")) { 
				nova.put("tipoFreteUrbano", 	getDomainValueService().findDomainValueByValue("DM_FRETE_URBANO_DHL",preFat.getTpFreteUrbano()).getDescription());
			} else {
				nova.put("tipoFreteUrbano", 	preFat.getTpFreteUrbano());				
			}
						
			nova.put("dataVencimento", 		preFat.getDtVencimento() );
			nova.put("valorFrete", 			preFat.getVlFrete() );
			
			nova.put("fechamentoIni", 		preFat.getDtInicioFechamento() );
			nova.put("fechamentoFim", 		preFat.getDtFinalFechamento() );
			
			nova.put("cnpjFornecedor", 		FormatUtils.formatCNPJ(preFat.getNrCnpjFornecedor() ));
			
			nova.put("valorBloqueio", 		preFat.getVlBloqueio() );
			nova.put("valorDesbloqueio", 	preFat.getVlDesbloqueio() );
			
			nova.put("moeda", 				preFat.getFatura().getMoeda().getSiglaSimbolo() );
			
			
		}
		return nova;
	}

	/**Executa a contagem do numero de linhas da consulta  para a grid de acordo com o idFatura
	 * @author Diego Umpierre
	 * 
	 * @param TypedFlatMap */
	public Integer getRowCountComplemento(TypedFlatMap criteria) {
		
		Long idFatura = criteria.getLong("idFatura"); 
		
		if ( idFatura == null ){
			return null;
		}
		
		return getItemPreFaturaService().getRowCountComplemento(idFatura);
	}
	
	
	/**Busca os dados para a grid de acordo com o idFatura
	 * @author Diego Umpierre
	 * 
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedComplemento(TypedFlatMap criteria) {
		
		Long idFatura = criteria.getLong("idFatura"); 

		if ( idFatura == null ){
			return null;
		}
		
		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		
		ResultSetPage retorno = getItemPreFaturaService().findPaginatedComplemento(idFatura, findDef);
 		
		List result = retorno.getList();
		Map nova = null;
		List novaLista = new ArrayList();
		
		for (Iterator iter = result.iterator(); iter.hasNext();) {
			
			ItemPreFatura itPreFat = (ItemPreFatura) iter.next();
			nova = new HashMap();
		
			nova.put("nrNotaFiscal", 		FormatUtils.completaDados(itPreFat.getNrNotaFiscal(),"0",10,0,true) );
			
			nova.put("cdSerieNotaFiscal", 	itPreFat.getCdSerieNotaFiscal() );
			nova.put("emissao", 			itPreFat.getDtEmissaoNotaFiscal() );
			nova.put("peso", 				itPreFat.getPsMercadoria() );
			nova.put("valor", 				itPreFat.getVlNotaFiscal() );
			
			//SEMPRE VAI SER EM REAIS
			nova.put("moedaValor", 			getMoedaService().findMoedaPadraoBySiglaPais("BRA").getSiglaSimbolo() );
			
			nova.put("tipoFrete", 			getDomainValueService().findDomainValueByValue("DM_FRETE_DHL",itPreFat.getTpFrete()).getDescription()  );
			nova.put("modalidadeFrete", 	getDomainValueService().findDomainValueByValue("DM_MODALIDADE_FRETE_DHL",itPreFat.getTpModalidadeFrete()).getDescription());
			if (itPreFat.getTpFreteUrbano().equals("I") || itPreFat.getTpFreteUrbano().equals("M")) { 
				nova.put("tipoFreteUrbano", 	getDomainValueService().findDomainValueByValue("DM_FRETE_URBANO_DHL",itPreFat.getTpFreteUrbano()).getDescription());
			} else {
				nova.put("tipoFreteUrbano", 	itPreFat.getTpFreteUrbano());				
			}
			
			nova.put("roteiro", 			itPreFat.getNrRoteiro());
			nova.put("protocolo", 			itPreFat.getNrProtocolo());
			nova.put("clienteDestino", 		FormatUtils.formatIdentificacao("CNPJ",itPreFat.getNrCnpjClienteDestino())  + " - " + itPreFat.getNmClienteDestino() );
			nova.put("volume", 				itPreFat.getPsAforado());
			nova.put("cidadeCliente", 		itPreFat.getNmCidadeClienteDestino());
			
			
			novaLista.add(nova);
		}
		
		retorno.setList(novaLista);
		
		return retorno;
	}

	public PreFaturaService getPreFaturaService() {
		return preFaturaService;
	}

	public void setPreFaturaService(PreFaturaService preFaturaService) {
		this.preFaturaService = preFaturaService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	
	/** Setando a service padr�o
	 * @param itemPreFaturaService
	 */
	public void setItemPreFaturaService(
		ItemPreFaturaService itemPreFaturaService) {
		this.itemPreFaturaService = itemPreFaturaService;
	}
	
	/**
	 * Retornando a Service
	 * @return
	 */
	public ItemPreFaturaService getItemPreFaturaService() {
		return itemPreFaturaService;
	}

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	
	
	
}
