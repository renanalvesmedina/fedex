package com.mercurio.lms.entrega.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.FilterList;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.entrega.model.service.ChequeReembolsoService;
import com.mercurio.lms.entrega.model.service.ConsultarPosicaoReembolsosService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.consultarPosicaoReembolsosAction"
 */

public class ConsultarPosicaoReembolsosAction extends CrudAction {
	
	private ConsultarPosicaoReembolsosService consultarPosicaoReembolsosService;
	private FilialService filialService;
	private ClienteService clienteService;
	private DomainValueService domainValueService;
	private ReciboReembolsoService reciboReembolsoService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private ConfiguracoesFacade configuracoesFacade;
	private ChequeReembolsoService chequeReembolsoService;
 		
	/**
	 * Detalhamento do recibo de reembolso
	 * 
	 * @param id
	 * @return
	 */
	public Serializable findByIdPosicaoReembolso(Long id) {
		TypedFlatMap recibo = consultarPosicaoReembolsosService.findByIdPosicaoReembolso((Long)id);
		
		if (recibo != null){
			recibo.put("doctoServicoByIdDoctoServReembolsado.clienteByIdClienteDestinatario.pessoa.nrIdentificacao",
				FormatUtils.formatIdentificacao(
						 recibo.getString("doctoServicoByIdDoctoServReembolsado.clienteByIdClienteDestinatario.pessoa.tpIdentificacao.value")					
						,recibo.getString("doctoServicoByIdDoctoServReembolsado.clienteByIdClienteDestinatario.pessoa.nrIdentificacao"))
			);
			
			recibo.put("doctoServicoByIdDoctoServReembolsado.clienteByIdClienteRemetente.pessoa.nrIdentificacao",
					FormatUtils.formatIdentificacao(
							 recibo.getString("doctoServicoByIdDoctoServReembolsado.clienteByIdClienteRemetente.pessoa.tpIdentificacao.value")					
							,recibo.getString("doctoServicoByIdDoctoServReembolsado.clienteByIdClienteRemetente.pessoa.nrIdentificacao"))
				);
			
			recibo.put("doctoServicoByIdDoctoServReembolsado.filialOrigem.sgFilial", recibo.getString("doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.sgFilial"));
		
			if (recibo.get("sgMoeda") != null) {
				StringBuffer moeda = new StringBuffer()
										.append(recibo.getString("sgMoeda"))
										.append(" ")
										.append(recibo.getString("dsSimbolo"));
				
				recibo.put("sgMoeda", moeda.toString());
			}
			
		}
					
		
		return recibo;
	}
	
	/**
	 * Monta grid de rastreamento de eventos do recibo
	 * @param parametros
	 * @return
	 */
	public List findGridEvento(TypedFlatMap parametros){
		List rastreamento = new ArrayList(10);
								
		Long idReciboReembolso = parametros.getLong("idReciboReembolso");
		
		//Linha 1 - Mercadoria no manifesto de entrega
		Map item = consultarPosicaoReembolsosService.findPosReciboMercadoriaManifestoEntrega(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "mercadoriaManifestoEntrega", "dhEmissaoManifesto"));
		
		//Lina 2 - Aguardando Confirma��o dos Cheques
		item = consultarPosicaoReembolsosService.findPosReciboAguardaCheques(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "aguardandoConfirmCheques", "dhBaixa"));
		
		//Linha 3 - Cheques Digitados (Recebimento do reembolso no destinat�rio)
		item = consultarPosicaoReembolsosService.findPosReciboRecebimento(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "chequesDigitados", "dhFechamento"));
				
		List posMir = consultarPosicaoReembolsosService.findPosReciboMIR(idReciboReembolso);
					 
		Map mir = getTpMir(posMir, "EA");
		//Linha 4 - MIR da entrega para o administrativo
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(mir, "MIREntregaAdministrativo", "dhEnvio"));
		//Linha 5 - Recebimento da MIR no administrativo
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(mir, "recebimentoMIRAdministrativo", "dhRecebimento"));
					
		mir = getTpMir(posMir, "DO");
		//Linha 6 - MIR do destino para origem
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(mir, "MIRDestinoOrigem", "dhEnvio"));
		//Linha 7 - Recebimento da MIR na origem
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(mir, "recebimentoMIROrigem", "dhRecebimento"));
			
		mir = getTpMir(posMir, "AE");
		//Linha 8 - MIR do administrativo para entrega
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(mir, "MIRAdministrativoEntrega", "dhEnvio"));
		//Linha 9 - Recebimento da MIR na entrega
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(mir, "recebimentoMIREntrega", "dhRecebimento"));			
		
		//Linha 10 - Reembolso no manifesto de entrega
		item = consultarPosicaoReembolsosService.findPosReciboManifestoEntrega(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "reembolsoManifestoEntrega", "dhEmissaoManifesto"));
		
		//Linha 11 - Entrega do reembolso ao cliente
		item = consultarPosicaoReembolsosService.findPosReciboEntregaCliente(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "entregaReembolsoCliente", "dhBaixa"));
			
		//Linha 12 - Reembolso Cancelado
		item = consultarPosicaoReembolsosService.findPosReembolsoCancelado(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "reembolsoCancelado", "dhCancelamento"));
		
		//Linha 13 - Mercadoria Devolvida
		item = consultarPosicaoReembolsosService.findPosMercadoriaDevolvida(idReciboReembolso);
		addLinhaRastreamento(rastreamento, criaLinhaRastreamento(item, "mercadoriaDevolvida", "dhEmissao"));
		
		return rastreamento;
	}
	
	/**
	 * Adiciona uma linha a grid de rastreamento de eventos da filial
	 * 
	 * @param rastreamento
	 * @param linha
	 */
	private void addLinhaRastreamento(List rastreamento, Map linha){
		if (linha != null){
			rastreamento.add(linha);
		}
	}
	
	/**
	 * Cria uma linha da grid de rastreamento de eventos do recibo
	 * 
	 * @param linha
	 * @param evento
	 * @param dhEvento
	 * @return
	 */
	private Map criaLinhaRastreamento(Map linha, String evento, String dhEvento){
		Map newLinha = new HashMap();				
		
		newLinha.put("dsEvento", configuracoesFacade.getMensagem(evento));
		
		if (linha != null && linha.get(dhEvento) != null) {
			newLinha.put("dhEvento", linha.get(dhEvento));
			newLinha.put("nrDocumento", linha.get("nrDocumento"));
			newLinha.put("sgFilial", linha.get("sgFilial"));			
		}
		
		return newLinha;
	}
	
	/**
	 * Retorna o map de um MIR de dentro da list
	 * 
	 * @param listMir
	 * @param tpMir
	 * @return
	 */
	private Map getTpMir(List listMir, String tpMir){
		if (listMir != null){
			for (Iterator iter = listMir.iterator(); iter.hasNext();) {
				Map mir = (Map) iter.next();
				if (((DomainValue) mir.get("tpMir")).getValue().equals(tpMir)){
					return mir;
				}			
			}
		}
		return null;
	}
	
	/**
	 * Monta grid de cheques do recibo
	 * @param parametros
	 * @return
	 */
	public List findGridCheque(TypedFlatMap parametros){
		Long idReciboReembolso = parametros.getLong("idReciboReembolso");
		return chequeReembolsoService.findPaginatedChequesByIdReembolso(idReciboReembolso);
	}
	
	/**
	 * Valida se o usuario tem acesso as filiais preenchidas na tela e as filiais de origem e operacional do documento de servico
	 * @param parametros
	 */
	private boolean validaAcessoUsuario(TypedFlatMap parametros){
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		if(!SessionUtils.isFilialSessaoMatriz()){
			Long idFilialOriginal = parametros.getLong("filialByIdFilialOrigem.idFilial");
			Long idFilialDestino = parametros.getLong("filialByIdFilialDestino.idFilial");
			if ((idFilialOriginal != null && idFilial.equals(idFilialOriginal))
		     	|| (idFilialDestino != null && idFilial.equals(idFilialDestino))){
				return true;
			}
		}else
			return true;
		
		return false;
	}
	
	
							
	public ResultSetPage findGridPosicaoReembolso(TypedFlatMap parametros){
		
		if (!validaAcessoUsuario(parametros)) {
			throw new BusinessException("LMS-09085");		
		}
		
		ResultSetPage rsp = consultarPosicaoReembolsosService.findPaginated(parametros);		
				
		FilterList filterResultSetPage = new FilterResultSetPage(rsp) {
			public Map filterItem(Object item) {
				Object[] linha = (Object[]) item;
				TypedFlatMap retorno = new TypedFlatMap();
				int i = 0;
				
				retorno.put("idReciboReembolso", linha[i++]);    			   			
				retorno.put("tpDocumentoServico", ((DomainValue)linha[i++]).getDescription());   	
				retorno.put("sgFilial", linha[i++]);
				retorno.put("nrDoctoServico", linha[i++]);
				retorno.put("tpDocumentoServicoRR", ((DomainValue)linha[i++]).getDescription());		 
				retorno.put("sgFilialRR", linha[i++]);
				retorno.put("sgFilialOrigem", linha[i++]);
				retorno.put("nrDoctoServicoRR", linha[i++]);
				retorno.put("dhEmissao", linha[i++]);
				retorno.put("nmFilialOrigem", linha[i++]);
				retorno.put("sgFilialDestino", linha[i++]);
				retorno.put("nmFilialDestino", linha[i++]);	    			
				retorno.put("tpIdentificacaoRemetente", linha[i++]);   			   
				retorno.put("nrIdentificacaoRemetente", FormatUtils.formatIdentificacao((String)linha[i-1], (String)linha[i++]));
				retorno.put("nmRemetente", linha[i++]);
				retorno.put("tpIdentificacaoDestinatario", linha[i++]); 
				retorno.put("nrIdentificacaoDestinatario", FormatUtils.formatIdentificacao((String)linha[i-1], (String)linha[i++]));				
				retorno.put("nmDestinatario", linha[i++]);
				if(linha[i]!= null)
					retorno.put("tpSituacao", ((DomainValue)linha[i++]).getDescription());
				retorno.put("vlReembolso", FormatUtils.formatDecimal("#,##0.00", (BigDecimal)linha[i++], true));															
				retorno.put("sgMoeda", linha[i++]);
				retorno.put("dsSimbolo", linha[i++]);
				return retorno;
			}
		};
		
		return (ResultSetPage)filterResultSetPage.doFilter();
		
	}
	
	public Integer getRowCountGridPosicaoReembolso(TypedFlatMap criteria) {	
		if (validaAcessoUsuario(criteria)) {
			return consultarPosicaoReembolsosService.getRowCount(criteria);
		}
		
		return Integer.valueOf(0);
	}

   /**
     * Consulta a filial pela sigla informada 
     * @param map
     * @return
     */
    public List findLookupFilial(Map map) {
    	FilterList filter = new FilterList(filialService.findLookup(map)) {
			public Map filterItem(Object item) {
				Filial filial = (Filial)item;
    			TypedFlatMap typedFlatMap = new TypedFlatMap();
	    		typedFlatMap.put("idFilial", filial.getIdFilial());
		    	typedFlatMap.put("sgFilial",  filial.getSgFilial());
		    	typedFlatMap.put("pessoa.nmFantasia",  filial.getPessoa().getNmFantasia());
				return typedFlatMap;
			}
    	};
    	return (List)filter.doFilter();
    }
	
    public TypedFlatMap findFilialSessao(){
    	Filial filial = SessionUtils.getFilialSessao();
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	retorno.put("idFilial", filial.getIdFilial());
    	retorno.put("sgFilial", filial.getSgFilial());
    	retorno.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    	
    	return retorno;
    }
    
	/**
	 * Busca os cliente de acordo com o n�mero de identifica��o informado
	 * @param tfm cliente.pessoa.nrIdentificacao N�mero de identifica��o do cliente
	 * @return Lista de clientes
	 */
	public List findLookupCliente(TypedFlatMap tfm){
		
		List clientes = clienteService.findLookupSimplificado(tfm.getString("pessoa.nrIdentificacao"),null);
		
		List retorno = new ArrayList();
			
		for (Iterator iter = clientes.iterator(); iter.hasNext();) {
				
			Cliente element = (Cliente) iter.next();
				
			TypedFlatMap map = new TypedFlatMap();
			map.put("pessoa.nrIdentificacao",element.getPessoa().getNrIdentificacao());
			map.put("idCliente",element.getIdCliente());
			map.put("pessoa.nmPessoa",element.getPessoa().getNmPessoa());
			map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(element.getPessoa().getTpIdentificacao(),
																					   element.getPessoa().getNrIdentificacao()));
				
			retorno.add(map);			
		}
		
		return retorno;			
	}
	
	/**
	 * COnsulta os tipos de documentos de servico da tag de docto servico
	 * @param criteria
	 * @return
	 */
	public List findTipoDocumentoServico(Map criteria) {
	    	List dominiosValidos = new ArrayList();
	    	dominiosValidos.add("CTR");	    	
	    	dominiosValidos.add("MDA");
	    	dominiosValidos.add("NFT");	    	    	
	    	List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
	    	return retorno;
	}
	
	public List findTipoDocumentoServicoRRE(Map criteria) {
    	List dominiosValidos = new ArrayList();    	
    	dominiosValidos.add("RRE");	    	    	
    	List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
    	return retorno;
	}
	/*
	 * Metodos de consulta da filial da tag documento de servico
	 */
	public List findLookupServiceDocumentFilialRRE(Map criteria){
		return findLookupFilialByDocumentoServico(criteria);
	}
		
	public List findLookupServiceDocumentFilialCTR(Map criteria) {
		return findLookupFilialByDocumentoServico(criteria);		
	}

	public List findLookupServiceDocumentFilialCRT(Map criteria) {
	   	return findLookupFilialByDocumentoServico(criteria);
	}

	public List findLookupServiceDocumentFilialMDA(Map criteria) {
	   	return findLookupFilialByDocumentoServico(criteria);
	}
	
	public List findLookupServiceDocumentFilialNFT(Map criteria) {
		return this.findLookupFilialByDocumentoServico(criteria);
	}
	
	
	public List findLookupFilialByDocumentoServico(Map criteria) {
    	List list = filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
	}
	 
	/*
	 * Metodos de consulta da lookup de documento de servico
	 */
	public List findLookupServiceDocumentNumberRRE(TypedFlatMap criteria){
		criteria.put("blBloqueado","N");
		List listaRecibo = reciboReembolsoService.findLookupCustom(criteria);
		AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();
		 return a.transformListResult(listaRecibo);
	}
	 
	public List findLookupServiceDocumentNumberCTR(Map criteria) {
		criteria.put("blBloqueado","N");
		return conhecimentoService.findLookup(criteria);	    
	}

	public List findLookupServiceDocumentNumberCRT(Map criteria) {
		criteria.put("blBloqueado","N");
	    return ctoInternacionalService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberMDA(Map criteria) {
		criteria.put("blBloqueado","N");
	    return mdaService.findLookup(criteria);
	}
				
	public List findLookupServiceDocumentNumberNFT(Map criteria) {
		criteria.put("blBloqueado","N");
		return conhecimentoService.findLookup(criteria);
	}

	
	/**
	 * @param consultarPosicaoReembolsosService The consultarPosicaoReembolsosService to set.
	 */
	public void setConsultarPosicaoReembolsosService(
			ConsultarPosicaoReembolsosService consultarPosicaoReembolsosService) {
		this.consultarPosicaoReembolsosService = consultarPosicaoReembolsosService;
	}


	/**
	 * @param filialService The filialService to set.
	 */
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * @param clienteService The clienteService to set.
	 */
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	/**
	 * @param conhecimentoService The conhecimentoService to set.
	 */
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	/**
	 * @param ctoInternacionalService The ctoInternacionalService to set.
	 */
	public void setCtoInternacionalService(
			CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}

	/**
	 * @param domainValueService The domainValueService to set.
	 */
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
	 * @param mdaService The mdaService to set.
	 */
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}

	/**
	 * @param reciboReembolsoService The reciboReembolsoService to set.
	 */
	public void setReciboReembolsoService(
			ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}

	/**
	 * @param configuracoesFacade The configuracoesFacade to set.
	 */
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * @param chequeReembolsoService The chequeReembolsoService to set.
	 */
	public void setChequeReembolsoService(
			ChequeReembolsoService chequeReembolsoService) {
		this.chequeReembolsoService = chequeReembolsoService;
	}


}
