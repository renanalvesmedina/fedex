package com.mercurio.lms.contasreceber.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.contasreceber.model.param.DivisaoClienteParam;
import com.mercurio.lms.contasreceber.model.param.DoctoServicoParam;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatLookUpService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.contasreceber.model.service.GerarFaturaPreFaturaService;
import com.mercurio.lms.contasreceber.model.service.MotivoDescontoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.AgrupamentoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TipoAgrupamento;
import com.mercurio.lms.vendas.model.service.AgrupamentoClienteService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.TipoAgrupamentoService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.manterPreFaturasAction"
 */

public class ManterPreFaturasAction extends MasterDetailAction {
	private AgrupamentoClienteService agrupamentoClienteService;
	private ClienteService clienteService;
	private ConhecimentoService conhecimentoService;
	private DivisaoClienteService divisaoClienteService;
	private DoctoServicoService doctoServicoService;
	private DomainValueService domainValueService;
	private DevedorDocServFatService devedorDocServFatService;
	private DevedorDocServFatLookUpService devedorDocServFatLookUpService;
	private FilialService filialService;
	private GerarFaturaPreFaturaService gerarFaturaPreFaturaService;
	private MotivoDescontoService motivoDescontoService;
	private MoedaService moedaService;
	private TipoAgrupamentoService tipoAgrupamentoService;
	
	public void setMotivoDescontoService(MotivoDescontoService motivoDescontoService) {
		this.motivoDescontoService = motivoDescontoService;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findTipoDocumentoServico(Map criteria){
        List dominiosValidos = new ArrayList();
        dominiosValidos.add("CTR");
        dominiosValidos.add("CRT");
        dominiosValidos.add("NFS");
        dominiosValidos.add("NFT");
        dominiosValidos.add("NDN");
		dominiosValidos.add("NSE");
		dominiosValidos.add("NTE");
		dominiosValidos.add("CTE");
        List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_PRE_FATURA", dominiosValidos);
        return retorno;
	}

	/**
	 * Retorna a moeda para o usario logado
	 * 
     * @author Diego Umpierre
     * 07/06/2006
     * 
	 * @return Map map com os a SgMoeda + SiglaSimbolo do brasil
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findMoedaBrasil(){
		Map retorno = new HashMap();
		Moeda moeda = moedaService.findMoedaPadraoBySiglaPais("BRA");
		retorno.put("moeda",moeda.getSiglaSimbolo());
		return retorno;
	}
	
	
	/**
	 * Retorno os valores necess�rio para inicializar a tela de fatura
	 * 
     * @author Micka�l Jalbert
     * 03/03/2006
     * 
	 * @return Map map de objetos para preencher os dados inicias da tela
	 * */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findInitialValue(){
		Map retorno = new HashMap();
		
		Filial filial = filialService.findFilialUsuarioLogado();			
		retorno.put("nmFantasia",filial.getPessoa().getNmFantasia());
		retorno.put("idFilial",filial.getIdFilial());
		retorno.put("sgFilial",filial.getSgFilial());
				
		Map item = null;
		DomainValue domainValue = null;
		List lstStatusFatura = this.getDomainValueService().findDomainValues("DM_STATUS_ROMANEIO");
		
		List lstStatusFaturaDesc = new ArrayList();
		
		for (Iterator iter = lstStatusFatura.iterator(); iter.hasNext();) {
			domainValue = (DomainValue) iter.next();
			item = new HashMap();
			
			item.put("value",domainValue.getValue());
			item.put("description",domainValue.getDescription());
			lstStatusFaturaDesc.add(item);
		}
		
		retorno.put("tpSituacaoFatura", lstStatusFaturaDesc);
			
		return retorno;
	}
	
	/**
	 * Retorno os valores necess�rio para inicializar a tela de fatura
	 * 
     * @author Micka�l Jalbert
     * 11/03/2006
	 * 
	 * @return Map map de objetos para preencher os dados inicias da tela
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findInitialValueItem(){
		Map item = null;
		DomainValue domainValue = null;
		List lstTpSituacaoAprovacao = this.getDomainValueService().findDomainValues("DM_STATUS_WORKFLOW");
		
		List lstTpSituacaoAprovacaoRet = new ArrayList();
		
		for (Iterator iter = lstTpSituacaoAprovacao.iterator(); iter.hasNext();) {
			domainValue = (DomainValue) iter.next();
			item = new HashMap();
			item.put("value",domainValue.getValue());
			item.put("description",domainValue.getDescription());
			lstTpSituacaoAprovacaoRet.add(item);
		}
			
		return lstTpSituacaoAprovacaoRet;
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map findFilialCliente(Long idCliente){
		Map retorno = new HashMap();
		Filial filial = filialService.findFilialCobrancaByCliente(idCliente);
		
		if (filial != null) {
			retorno.put("idFilial",filial.getIdFilial());
			retorno.put("sgFilial",filial.getSgFilial());
			retorno.put("nmFantasia",filial.getPessoa().getNmFantasia());
		}
		
		return retorno;
	}
	
	
	/**
	 * M�todo que salva as altera��es feitas no mestre e nos detalhes
	 * 
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * 
	 * @param tipoRegistroComplementoTela
	 * @return id gerado para o mestre
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Serializable store(TypedFlatMap map){
		MasterEntry entry = getMasterFromSession(map.getLong("idFatura"), true);		
		ItemList items = getItemsFromSession(entry, "itemFatura");		
		ItemListConfig config = getMasterConfig().getItemListConfig("itemFatura");

		Fatura fatura = mountFatura((Fatura)entry.getMaster(), map);	
		
		if (fatura.getIdFatura() == null){
			Cliente cliente = clienteService.findById(fatura.getCliente().getIdCliente());
			fatura = getFaturaService().beforeInsertPreFatura(fatura, items, cliente);
		}
		
		List lstAll = new ArrayList();
		for (Iterator iter = items.iterator(fatura.getIdFatura(), config); iter.hasNext();) {
			ItemFatura itemFatura = (ItemFatura)iter.next();
			lstAll.add(itemFatura);
		}		
		
		Serializable retorno = gerarFaturaPreFaturaService.storeFaturaWithItemFatura(fatura, lstAll, items.getNewOrModifiedItems(), items.getRemovedItems());
		items.resetItemsState(); 
    	updateMasterInSession(entry);
		return retorno;
	}
	
	/**
	 * M�todo que monta uma fatura a partir do map que vem da tela.
	 * 
	 * author Micka�l Jalbert
	 * 03/04/2006
	 * 
	 * @param Fatura fatura 
	 * @param TypedFlatMap map
	 * @return Fatura
	 */	
	private Fatura mountFatura(Fatura fatura, TypedFlatMap map) {
		fatura.setCliente(clienteService.findById(map.getLong("cliente.idCliente")));	
		
		if (map.getLong("divisaoCliente.idDivisaoCliente") != null){
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(map.getLong("divisaoCliente.idDivisaoCliente"));
			fatura.setDivisaoCliente(divisaoCliente);
		}
		
		if (map.getLong("agrupamentoCliente.idAgrupamentoCliente") != null){
			AgrupamentoCliente agrupamentoCliente = new AgrupamentoCliente();
			agrupamentoCliente.setIdAgrupamentoCliente(map.getLong("agrupamentoCliente.idAgrupamentoCliente"));
			fatura.setAgrupamentoCliente(agrupamentoCliente);
		}
		
		if (map.getLong("tipoAgrupamento.idTipoAgrupamento") != null){
			TipoAgrupamento tipoAgrupamento = new TipoAgrupamento();
			tipoAgrupamento.setIdTipoAgrupamento(map.getLong("tipoAgrupamento.idTipoAgrupamento"));
			fatura.setTipoAgrupamento(tipoAgrupamento);
		}
		
		fatura.setIdFatura(map.getLong("idFatura"));
		fatura.setNrFatura(map.getLong("nrFatura"));
		fatura.setTpModal(map.getDomainValue("tpModal"));
		fatura.setTpAbrangencia(map.getDomainValue("tpAbrangencia"));		
		fatura.setDtEmissao(map.getYearMonthDay("dtEmissao"));
		fatura.setDtVencimento(map.getYearMonthDay("dtVencimento"));		
		fatura.setVlIva(map.getBigDecimal("vlIva"));
		fatura.setVlTotal(map.getBigDecimal("vlTotal"));
		fatura.setVlTotalRecebido(map.getBigDecimal("vlTotalRecebido"));
		return fatura;
	}
	
	
	/**
	 * M�todo que monta uma item fatura a partir do map que vem da tela.
	 * 
	 * author Micka�l Jalbert
	 * 11/03/2006
	 * 
	 * @param TypedFlatMap map
	 * @return ItemFatura
	 */	
	private ItemFatura mountItemFatura(TypedFlatMap map, ItemFatura itemFatura){
		DevedorDocServFat devedorDocServFat = devedorDocServFatService.findById(map.getLong("idDevedorDocServFat"));

		itemFatura.setDevedorDocServFat(devedorDocServFat);

		return itemFatura;
	}	
	
	/**
	 * Busca um mestre pelo seu id e armazena-o na sess�o do usu�rio
	 * 
	 * author Micka�l Jalbert
	 * 03/04/2006
	 * 
	 * @param id
	 * @return Fatura, objeto mestre
	 */
	@SuppressWarnings("rawtypes")
	public Map findById(java.lang.Long id) {
		Object masterObj = this.getFaturaService().findByIdTela(id);
		putMasterInSession(masterObj);
		
		Fatura fatura = (Fatura)masterObj;
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("idFatura", fatura.getIdFatura());		
		map.put("nrFatura", fatura.getNrFatura());
		map.put("cliente.pessoa.nrIdentificacao", fatura.getCliente().getPessoa().getNrIdentificacao());
		map.put("cliente.pessoa.nrIdentificacaoFormatado",
				FormatUtils.formatIdentificacao(fatura.getCliente().getPessoa().getTpIdentificacao(), 
												fatura.getCliente().getPessoa().getNrIdentificacao()));    			
		
		
		
		map.put("cliente.pessoa.nmPessoa", fatura.getCliente().getPessoa().getNmPessoa());		
		map.put("cliente.idCliente", fatura.getCliente().getIdCliente());
		
		if (fatura.getDivisaoCliente() != null){
			map.put("divisaoCliente.idDivisaoClienteTmp", fatura.getDivisaoCliente().getIdDivisaoCliente());
		}
		
		map.put("tpModal", fatura.getTpModal().getValue());
		map.put("tpAbrangencia", fatura.getTpAbrangencia().getValue());
		
		if (fatura.getAgrupamentoCliente() != null){
			map.put("agrupamentoCliente.idAgrupamentoClienteTmp", fatura.getAgrupamentoCliente().getIdAgrupamentoCliente());
		}
		
		if (fatura.getTipoAgrupamento() != null){
			map.put("tipoAgrupamento.idTipoAgrupamentoTmp", fatura.getTipoAgrupamento().getIdTipoAgrupamento());
		}
		
		map.put("dtEmissao", fatura.getDtEmissao());
		map.put("dtVencimento", fatura.getDtVencimento());
		map.put("qtDocumentos", fatura.getQtDocumentos());
		map.put("vlIva", fatura.getVlIva());
		map.put("vlTotal", fatura.getVlTotal());		
		map.put("vlTotalRecebido", fatura.getVlTotalRecebido());
		
		//retornando a moeda do brasil
		Moeda moeda = moedaService.findMoedaPadraoBySiglaPais("BRA");
		map.put("moeda",moeda.getSiglaSimbolo());
		
		map.put("filialByIdFilial.sgFilial",fatura.getFilialByIdFilial().getSgFilial());
		
		
		//adicionado o tpSituacaoFatura para controlar o bloqueio ou n�o das operacoes de insert, delete e update
		map.put("tpSituacaoFatura",fatura.getTpSituacaoFatura().getValue());
		
		//adicionado o tpSituacaoFatura para controlar o bloqueio ou n�o da operacao de delete quando estiver em aprovacao
		if ( fatura.getTpSituacaoAprovacao() !=null ){
			map.put("tpSituacaoAprovacao",fatura.getTpSituacaoAprovacao().getValue());
		}else{
			map.put("tpSituacaoAprovacao","");
		}
		
		
		
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer getRowCount(Map filtros) {
		//Buscar s� as fatura que tem o tpOrigem = 'Pre-Fatura Web' (W)
		filtros.put("tpOrigem", "W");
		return super.getRowCount(filtros);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginated(Map filtros) {
		
		//Buscar s� as fatura que tem o tpOrigem = 'Pre-Fatura Web' (W)
		filtros.put("tpOrigem", "W");
		
		ResultSetPage rsp = super.findPaginated(filtros);
		List list = rsp.getList();
		List listRet = new ArrayList();
		TypedFlatMap mapRet = null;
		
		for (Iterator iter = list.iterator(); iter.hasNext();){
			Fatura fatura = (Fatura) iter.next();
			mapRet = new TypedFlatMap();

			mapRet.put("idFatura",fatura.getIdFatura());
			mapRet.put("nrFatura",fatura.getNrFatura());
			
			if (fatura.getDivisaoCliente() != null){
				mapRet.put("divisaoCliente.dsDivisaoCliente",fatura.getDivisaoCliente().getDsDivisaoCliente());
			}
			mapRet.put("tpModal",fatura.getTpModal());
			mapRet.put("tpAbrangencia",fatura.getTpAbrangencia());
			
			String nrIdentificacao = FormatUtils.formatIdentificacao(fatura.getCliente().getPessoa().getTpIdentificacao(), fatura.getCliente().getPessoa().getNrIdentificacao());
			
			if (StringUtils.isNotBlank(nrIdentificacao)){
				mapRet.put("cliente.pessoa.nrIdentificacao", nrIdentificacao);
			} else {
				mapRet.put("cliente.pessoa.nrIdentificacao", fatura.getCliente().getPessoa().getNrIdentificacao());
			}
			
			mapRet.put("cliente.pessoa.nmPessoa",fatura.getCliente().getPessoa().getNmPessoa());
			mapRet.put("dtEmissao",fatura.getDtEmissao());
			mapRet.put("dtVencimento",fatura.getDtVencimento());	
			mapRet.put("filialByIdFilial.sgFilial",fatura.getFilialByIdFilial().getSgFilial());
			
			
			listRet.add(mapRet);
		}
		rsp.setList(listRet);
		return rsp;
	}
	
	public Map cancelFatura(TypedFlatMap map){
		getFaturaService().cancelFatura(map.getLong("idFatura"));

		return findById(map.getLong("idFatura"));
	}
		
    /**
     * Busca a lista de motivos de desconto para a combo de motivos desconto
     * @param map Crit�rios da pesquisa
     * @return Lista de Motivos do Desconto
     */
    public List findComboMotivoDesconto(Map map){
        return this.motivoDescontoService.find(map);
    } 
	
	/**
	 * Remove uma lista de registros mestres
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * @param ids
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		this.getFaturaService().removeByIds(ids);
	}
	
	/**
	 * Remove um registro com os filhos
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * @param id
	 */
	public void removeById(Long id) {
		this.getFaturaService().removeById(id);
	}
    
	/***
	 * Remove uma lista de item fatura.
	 * 
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * @param List ids
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsItemFatura(List ids) {
		MasterEntry entry = (MasterEntry) SessionContext.get(getClass().getName());
		
		if (entry.getMasterId() != null){
			//Validar se o usu�rio tem direito de modificar a fatura
			this.getFaturaService().validateFatura((Long)entry.getMasterId());
		}
		
		super.removeItemByIds(ids, "itemFatura");
	}	
	
	
	/**
	 * Salva um registro detalhe/filho na sess�o.
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * @param parameters Parametros utilizado para montar o detalhe
	 * @return id do detalhe (tempor�rio no caso de inser��o)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Serializable storeItemFatura(TypedFlatMap map) {
		
		/** Caso a tpSituacaoFatura seja Emitida('EM') a pre-fatura n�o poder� ser alterada,
		 *  � lan�ada uma BusinessException */
		if("EM".equals(map.getString("tpSituacaoFatura")) && map.getLong("idFatura") != null){
			throw new BusinessException("LMS-36202");
		}
		
		List lstIdDevedor = map.getList("ids");
		List lstIdDevedorInserido = new ArrayList();
		Long idDevedorDocServFat = null;
		List lstItemApagar = new ArrayList();
		
		for (Iterator iter = lstIdDevedor.iterator(); iter.hasNext();) {
			Map mapItem = new HashMap(4);
			idDevedorDocServFat = Long.valueOf((String)iter.next());
			
			mapItem.put("idDevedorDocServFat", idDevedorDocServFat);
			mapItem.put("tpDocumentoServico", map.getString("tpDocumentoServico"));
			mapItem.put("tpDocumentoAnterior", map.getString("tpDocumentoAnterior"));			
			mapItem.put("masterId", map.getString("idFatura"));			
			
			//Mecanismo para fazer um 'rollback' de todos os itens que foram iserido na 
			//sess�o se um dos itens est� com erro
			try {
				saveItemInstance(mapItem, "itemFatura");	
			} catch (BusinessException e) {
				
	    		Long masterId = getMasterId(map);
	    		
				ItemList items = getItemsFromSession(getMasterFromSession(masterId, true), "itemFatura");				

				ItemListConfig itemListConfig = getMasterConfig().getItemListConfig("itemFatura");
				
				//Por cada item da sess�o
				for (Iterator iterator = items.iterator(masterId, itemListConfig); iterator.hasNext();) {

					ItemFatura itemFatura = (ItemFatura)iterator.next();
					
					//Por cada item que eu inseri na sess�o agora
					for (Iterator iterator2 = lstIdDevedorInserido.iterator(); iterator2.hasNext();) {
						Long idDevedorDocServFatOld = (Long) iterator2.next();
						
						//Se o id do devedor da sess�o � igual ao id do devedor dum dos itens que eu 
						//acabei de inserir, adicionar ele a lista de itens a paagar da sess�o
						if (itemFatura.getDevedorDocServFat().getIdDevedorDocServFat().equals(idDevedorDocServFatOld)){
							lstItemApagar.add(itemFatura.getIdItemFatura());
						}
					}
				}
				//Remover da sess�o todos 
				removeItemByIds(lstItemApagar, "itemFatura");
				throw e;
			}
			
			lstIdDevedorInserido.add(idDevedorDocServFat);			
		}
		return "";		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findSomatorios(Long idFatura){
		Map map = new HashMap();

		MasterEntry entry = getMasterFromSession(idFatura, true);			
		ItemList items = getItemsFromSession(entry, "itemFatura");	
		
		BigDecimal vlDocumentoTotal = new BigDecimal("0.00");
		BigDecimal vlDescontoTotal = new BigDecimal("0.00");
		vlDocumentoTotal = vlDocumentoTotal.setScale(2, RoundingMode.HALF_UP);
		vlDescontoTotal = vlDescontoTotal.setScale(2, RoundingMode.HALF_UP);
		
		ItemListConfig config = getMasterConfig().getItemListConfig("itemFatura");
		
		for (Iterator iter = items.iterator(idFatura, config); iter.hasNext();) {
			ItemFatura itemFatura = (ItemFatura)iter.next();
			
			vlDocumentoTotal = vlDocumentoTotal.add(itemFatura.getDevedorDocServFat().getVlDevido());
			
			if (itemFatura.getDevedorDocServFat().getDescontos() != null && itemFatura.getDevedorDocServFat().getDescontos().size() > 0){
				vlDescontoTotal = vlDescontoTotal.add(((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getVlDesconto());
			}
		}
		
		map.put("qtdeTotalDocumentos", Integer.valueOf(items.size()));
		map.put("valorTotalDocumentos",vlDocumentoTotal);
		map.put("valorTotalDesconto",vlDescontoTotal);	

		return map;
	}
	@SuppressWarnings({ "unchecked", "rawtypes", "serial" })
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(Fatura.class);
		
		// Comparador para realizar a ordena��o dos items filhos de acordo com a regra de neg�cio.
		Comparator descComparator = new Comparator() {
			
			public int compare(Object o1, Object o2) {
				
				if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
					return ((Comparable)o1).compareTo(o2);
				} else {
					ItemFatura d1 = (ItemFatura) o1;
					ItemFatura d2 = (ItemFatura) o2;
					
					return d1.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().compareTo(d2.getDevedorDocServFat().getDoctoServico().getNrDoctoServico());
				}
			}
			
		};
		
		// Esta instancia ser� responsavel por carregar os items filhos na sess�o a partir do banco de dados.
		ItemListConfig itemInit = new ItemListConfig() {
			
			//Chamado para carregar os filhos na sess�o
			public List initialize(Long masterId) {
				if (masterId == null) {
					return Collections.EMPTY_LIST;
				}
				return getFaturaService().findItemFatura(masterId);
			}

			public Integer getRowCount(Long masterId) {
				return getFaturaService().getRowCountItemFatura(masterId);
			}

			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet(2);
				ignore.add("idItemFatura");
				ignore.add("versao");
				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			public Map configItemDomainProperties() {
				Map props = new HashMap(1);
				props.put("tpDocumentoServico", "DM_TIPO_DOCUMENTO_SERVICO");
				return props;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				
				TypedFlatMap map = new TypedFlatMap();
				map.putAll(parameters);
				ItemFatura itemFaturaNew = (ItemFatura)bean;
				itemFaturaNew = mountItemFatura(map, itemFaturaNew);
	    		resolveDomainValueProperties(itemFaturaNew);

	    		Long masterId = getMasterId(parameters);
	    		
	    		MasterEntry entry = getMasterFromSession(masterId, true);
	    		
				ItemList items = getItemsFromSession(getMasterFromSession(masterId, true), "itemFatura");				
		    	
				ItemListConfig config = getMasterConfig().getItemListConfig("itemFatura");
				
				//Regras de neg�cio
				getFaturaService().storeBeforeItemFatura((Fatura)entry.getMaster(), items, config, itemFaturaNew);

				return itemFaturaNew;
			}
			
		};
		
		config.addItemConfig("itemFatura",ItemFatura.class, itemInit, descComparator);
		return config;
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public ResultSetPage findPaginatedItemFatura(Map parameters) {

		ResultSetPage rsp = findPaginatedItemList(parameters, "itemFatura");			
		List list = rsp.getList();			
		List listRet = new ArrayList();			
		Map map = null;
		
		for (Iterator iter = list.iterator(); iter.hasNext();){
			ItemFatura itemFatura = (ItemFatura)iter.next();		
			map = new TypedFlatMap();
			
			map.put("idItemFatura",itemFatura.getIdItemFatura());
			map.put("tpDocumentoServico",itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico());
			map.put("sgFilial",itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());				
			map.put("nrDoctoServico",FormatUtils.formataNrDocumento(itemFatura.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().toString(),itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue()));
			
			if (itemFatura.getDevedorDocServFat().getDoctoServico().getDhEmissao() != null) {
				map.put("dtEmissao",itemFatura.getDevedorDocServFat().getDoctoServico().getDhEmissao().toYearMonthDay());
			}
			
			map.put("vlMercadoria",itemFatura.getDevedorDocServFat().getDoctoServico().getVlMercadoria());			
			map.put("vlTotal",itemFatura.getDevedorDocServFat().getVlDevido());
			map.put("idMoeda",itemFatura.getDevedorDocServFat().getDoctoServico().getMoeda().getIdMoeda());
			map.put("idCliente",itemFatura.getDevedorDocServFat().getCliente().getIdCliente());

			if ( itemFatura.getDevedorDocServFat().getDesconto() != null  ){
				map.put("vlDesconto",itemFatura.getDevedorDocServFat().getDesconto().getVlDesconto());
			}else{
				map.put("vlDesconto","");
			}
			
			if (itemFatura.getDevedorDocServFat().getDoctoServico().getServico() != null){
				map.put("tpModal",itemFatura.getDevedorDocServFat().getDoctoServico().getServico().getTpModal().getValue());
				map.put("tpAbrangencia",itemFatura.getDevedorDocServFat().getDoctoServico().getServico().getTpAbrangencia().getValue());
				map.put("idServico",itemFatura.getDevedorDocServFat().getDoctoServico().getServico().getIdServico());
			}
			
			//Se � um CTRC, buscar o tipo de frete
			if (itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue().equals("CTR")
					|| itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue().equals("CTE")) {
				map.put("tpFrete", conhecimentoService.findTpFreteByIdConhecimento(itemFatura.getDevedorDocServFat().getDoctoServico().getIdDoctoServico()).getValue());
			}
			
			if (itemFatura.getDevedorDocServFat().getDivisaoCliente() != null){
				map.put("idDivisaoCliente", itemFatura.getDevedorDocServFat().getDivisaoCliente().getIdDivisaoCliente());
			}
			
			listRet.add(map);
		}
		
		rsp.setList(listRet);
		
		return rsp;
	}	
	
	public Integer getRowCountItemFatura(Map parameters){
		return getRowCountItemList(parameters, "itemFatura");
	}
	
	@SuppressWarnings("rawtypes")
	public Map findByIdItemFatura(MasterDetailKey key) {
		ItemFatura itemFatura = (ItemFatura)findItemById(key, "itemFatura");
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("idItemFatura", itemFatura.getIdItemFatura());
		map.put("idCliente",itemFatura.getDevedorDocServFat().getCliente().getIdCliente());
		map.put("devedorDocServFat.idDevedorDocServFat",itemFatura.getDevedorDocServFat().getIdDevedorDocServFat());
		map.put("devedorDocServFat.doctoServico.nrDoctoServico",itemFatura.getDevedorDocServFat().getDoctoServico().getNrDoctoServico());
		map.put("devedorDocServFat.doctoServico.idDoctoServico",itemFatura.getDevedorDocServFat().getDoctoServico().getIdDoctoServico());
		map.put("devedorDocServFat.doctoServico.nrDoctoServicoTmp",itemFatura.getDevedorDocServFat().getDoctoServico().getNrDoctoServico());
		map.put("devedorDocServFat.doctoServico.vlTotalDocServico",itemFatura.getDevedorDocServFat().getDoctoServico().getVlTotalDocServico());
		map.put("devedorDocServFat.doctoServico.tpDocumentoServico.description",itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getDescription());
		map.put("devedorDocServFat.doctoServico.tpDocumentoServico.value",itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico().getValue());
		map.put("devedorDocServFat.doctoServico.moeda.idMoeda",itemFatura.getDevedorDocServFat().getDoctoServico().getMoeda().getIdMoeda());		
		map.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial",itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getIdFilial());
		map.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.sgFilial",itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
		map.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia",itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());		
		if (itemFatura.getDevedorDocServFat().getDescontos().size() > 0) {
			
			if (((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getIdDesconto() != null){
				map.put("devedorDocServFat.desconto.tpSituacaoAprovacaoVal",((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getTpSituacaoAprovacao().getValue());
				map.put("devedorDocServFat.desconto.tpSituacaoAprovacaoDesc",((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getTpSituacaoAprovacao().getDescription());
				map.put("devedorDocServFat.desconto.idDesconto",((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getIdDesconto());				
			}
			
			map.put("devedorDocServFat.desconto.vlDesconto",((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getVlDesconto());
			map.put("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto",((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getMotivoDesconto().getIdMotivoDesconto());
			map.put("devedorDocServFat.desconto.obDesconto",((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getObDesconto());
		
		}
		return map;
	}	
	
	/**
	 * Find da tag Devedor
	 * */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findDevedorServDocFat(TypedFlatMap map){
		Long idFilial = map.getLong("doctoServico.filialByIdFilialOrigem.idFilial");	
		Long nrDocumento = map.getLong("doctoServico.nrDoctoServico");
		String tpDocumentoServico = map.getString("doctoServico.tpDocumentoServico");	
		Long idCliente = map.getLong("cliente.idCliente");		
		
		DevedorDocServFatLookupParam devedorDocServFatLookupParam = new DevedorDocServFatLookupParam();
		
		devedorDocServFatLookupParam.setIdFilial(idFilial);
		devedorDocServFatLookupParam.setNrDocumentoServico(nrDocumento);
		devedorDocServFatLookupParam.setTpDocumentoServico(tpDocumentoServico);
		devedorDocServFatLookupParam.setIdCliente(idCliente);
		
		List list = this.devedorDocServFatLookUpService.findDevedorDocServFat(
				devedorDocServFatLookupParam);
			
		List listRet = new ArrayList();			
		
		for (Iterator iter = list.iterator(); iter.hasNext();){
			Map mapRet = (Map)iter.next();		
			
			mapRet.put("devedorDocServFat.desconto.idDesconto",mapRet.get("idDesconto"));
			mapRet.put("devedorDocServFat.doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia",mapRet.get("nmFantasia"));
			
			listRet.add(mapRet);
		}
		
		return AliasToNestedMapResultTransformer.getInstance().transformListResult(listRet);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginatedDoctoServico(TypedFlatMap map){
		DoctoServicoParam doctoServicoParam = mountFiltroFindPaginatedDoctoServico(map);
		
		//Montar a lista de tpSituacaoCobranca, filtros para os documentos de servico
		List lstTpSituacaoDocumento = new ArrayList();		
		lstTpSituacaoDocumento.add("P");
		lstTpSituacaoDocumento.add("C");		
		
		return doctoServicoService.findPaginatedComConhecimento(doctoServicoParam, lstTpSituacaoDocumento, FindDefinition.createFindDefinition(map));		
	}

	/**
	 * @author Micka�l Jalbert
	 * @since 08/01/2007
	 * @param map
	 * @return
	 */
	private DoctoServicoParam mountFiltroFindPaginatedDoctoServico(TypedFlatMap map) {
		DoctoServicoParam doctoServicoParam = new DoctoServicoParam();
		
		doctoServicoParam.setNrNotaFiscalInicial(map.getLong("notaInicial"));
		doctoServicoParam.setNrNotaFiscalFinal(map.getLong("notaFinal"));
		
		doctoServicoParam.setNrDoctoServicoInicial(map.getLong("doctoServicoInicial"));
		doctoServicoParam.setNrDoctoServicoFinal(map.getLong("doctoServicoFinal"));
		
		doctoServicoParam.setTpFrete(map.getString("tpFrete"));
		doctoServicoParam.setIdCliente(map.getLong("idCliente"));
		doctoServicoParam.setTpModal(map.getString("tpModal"));
		doctoServicoParam.setTpAbrangencia(map.getString("tpAbrangencia"));
		doctoServicoParam.setIdServico(map.getLong("idServico"));
		doctoServicoParam.setIdDivisaoCliente(map.getLong("idDivisaoCliente"));
		
		if (map.getLong("doctoServico.notaFiscalServico.idDoctoServico") != null){
			doctoServicoParam.setIdDoctoServico(map.getLong("doctoServico.notaFiscalServico.idDoctoServico"));
		} else if (map.getLong("doctoServico.ctoInternacional.idDoctoServico") != null){
			doctoServicoParam.setIdDoctoServico(map.getLong("doctoServico.ctoInternacional.idDoctoServico"));
		} else if (map.getLong("doctoServico.conhecimento.idDoctoServico") != null){
			doctoServicoParam.setIdDoctoServico(map.getLong("doctoServico.conhecimento.idDoctoServico"));
		} else if (map.getLong("doctoServico.mda.idDoctoServico") != null){
			doctoServicoParam.setIdDoctoServico(map.getLong("doctoServico.mda.idDoctoServico"));
		} else if (map.getLong("doctoServico.reciboReembolsoByIdReciboReembolso.idDoctoServico") != null){
			doctoServicoParam.setIdDoctoServico(map.getLong("doctoServico.reciboReembolsoByIdReciboReembolso.idDoctoServico"));
		} else if (map.getLong("doctoServico.notaDebitoNacional.idDoctoServico") != null){
			doctoServicoParam.setIdDoctoServico(map.getLong("doctoServico.notaDebitoNacional.idDoctoServico"));
		}
		
		doctoServicoParam.setDtEmissaoFinal(map.getYearMonthDay("dtEmissaoFinal"));
		doctoServicoParam.setTpDocumentoServico(map.getString("doctoServico.tpDocumentoServico"));
		doctoServicoParam.setIdFilialorigem(map.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
		
		//Montar a lista de idDevedores para n�o incluir eles na pesquisa
		List lstDevedores = new ArrayList();
		ItemList items = getItemsFromSession(getMasterFromSession(map.getLong("idFatura"), true), "itemFatura");
		ItemListConfig config = getMasterConfig().getItemListConfig("itemFatura");
		
		for (Iterator iter = items.iterator(map.getLong("idFatura"), config); iter.hasNext();){
			ItemFatura itemFatura = (ItemFatura)iter.next();
			if (itemFatura.getDevedorDocServFat() != null){
				lstDevedores.add(itemFatura.getDevedorDocServFat().getIdDevedorDocServFat());
			}			
		}
		
		doctoServicoParam.setIdDevedores(lstDevedores);
		return doctoServicoParam;
	}
	
	/**
	 * M�todo respons�vel por fazer a valida��o para que todos os DoctoServico selecionados na grid sejam do mesmo tipo
	 * 
	 * @author HectorJ
	 * @since 06/06/2006
	 * 
	 * @param criteria
	 */
	public void validateDocumentoServico(TypedFlatMap criteria){
		
		/** Extrai os Ids do TypedFlatMap */
		List ids = criteria.getList("doctoServicos.ids");
		
		/** Vari�val que armazena o tipo de doctoServico da itera��o anterior */
		String tpDocServicoAnterior = "";
		
		/** Valida se a List n�o est� vazia */
		if(ids != null){
			
			/** Itera a lista que armazena os ids dos DoctoServico */
			for (int i = 0; i < ids.size(); i++) {
				
				/** Extrai da List o idDevedorDocServFat da itera��o atual */
				Long element = Long.valueOf((String)ids.get(i));
				
				/** Faz o findById do DoctoServico */
				DoctoServico ds = (devedorDocServFatService.findByIdSimplificado(element)).getDoctoServico();
				
				/** Caso o tipo de DoctoServico seja diferente do anterior, � lan�ada uma BusnessException */
				if(i != 0 && !ds.getTpDocumentoServico().getValue().equals(tpDocServicoAnterior)){
					throw new BusinessException("LMS-36008");
				}
				
				tpDocServicoAnterior = ds.getTpDocumentoServico().getValue();
			}
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer getRowCountDoctoServico(TypedFlatMap map){
		DoctoServicoParam doctoServicoParam = mountFiltroFindPaginatedDoctoServico(map);
		
		//Montar a lista de tpSituacaoCobranca, filtros para os documentos de servico
		List lstTpSituacaoDocumento = new ArrayList();		
		lstTpSituacaoDocumento.add("P");
		lstTpSituacaoDocumento.add("C");	
		
		return doctoServicoService.getRowCountComConhecimento(doctoServicoParam, lstTpSituacaoDocumento);		
	}	
	
	public List findLookupCliente(Map criteria){
		return clienteService.findLookup(criteria);
	}	
	
	public List findComboDivisaoCliente(TypedFlatMap criteria){
		return divisaoClienteService.findByIdClienteMatriz(populateDivisaoClienteParam(criteria));
	}	
	
	/**
	 * Popula a DivisaoClienteparam para ser usado como filtro na busca por divisao
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/01/2007
	 *
	 * @param tfm
	 * @return
	 *
	 */
	public DivisaoClienteParam populateDivisaoClienteParam(TypedFlatMap tfm){
		
		DivisaoClienteParam dcp = new DivisaoClienteParam();
		dcp.setIdCliente(tfm.getLong("idCliente"));
		dcp.setIdDivisaoCliente(tfm.getLong("idDivisao"));
		dcp.setTpSituacao("A");
		
		return dcp;		
	}
	
	public List findComboTipoAgrupamento(TypedFlatMap criteria){
		return tipoAgrupamentoService.find(criteria);
	}	
	
	public List findAgrupamentoCliente(TypedFlatMap criteria){
		return agrupamentoClienteService.find(criteria);
	}

	public void setFaturaService(FaturaService faturaService) {
		super.setMasterService(faturaService);
	}
	
	public FaturaService getFaturaService() {
		return (FaturaService)super.getMasterService();
	}

	public void setAgrupamentoClienteService(
			AgrupamentoClienteService agrupamentoClienteService) {
		this.agrupamentoClienteService = agrupamentoClienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setDevedorDocServFatLookUpService(
			DevedorDocServFatLookUpService devedorDocServFatLookUpService) {
		this.devedorDocServFatLookUpService = devedorDocServFatLookUpService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setTipoAgrupamentoService(
			TipoAgrupamentoService tipoAgrupamentoService) {
		this.tipoAgrupamentoService = tipoAgrupamentoService;
	}

	public void setGerarFaturaPreFaturaService(
			GerarFaturaPreFaturaService gerarFaturaPreFaturaService) {
		this.gerarFaturaPreFaturaService = gerarFaturaPreFaturaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
}