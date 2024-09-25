package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.CobrancaInadimplencia;
import com.mercurio.lms.contasreceber.model.DepositoCcorrente;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemDepositoCcorrente;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.Recibo;
import com.mercurio.lms.contasreceber.model.dao.DepositoCcorrenteDAO;
import com.mercurio.lms.contasreceber.model.param.DepositoCcorrenteParam;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.depositoCcorrenteService"
 */
public class DepositoCcorrenteService extends CrudService<DepositoCcorrente, Long> {
	
	private ConfiguracoesFacade configuracoesFacadeImpl;
	private ItemCobrancaService itemCobrancaService;
	private CobrancaInadimplenciaService cobrancaInadimplenciaService;
	private DevedorDocServFatService devedorDocServFatService;
	private FaturaService faturaService;
	private BoletoService boletoService;
	private ReciboService reciboService;
	
	/**
	 * Recupera uma instância de <code>DepositoCcorrente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public DepositoCcorrente findById(java.lang.Long id) {
		return (DepositoCcorrente)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		//Verificar se dá para modificar o registro
		validateDepositoCcorrente(findById(id));

		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			//Verificar se dá para modificar o registro
			validateDepositoCcorrente(findById(id));
		}
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Serializable store(DepositoCcorrente bean) {
		return super.store(bean);
	}

	public ResultSetPage findPaginated(DepositoCcorrenteParam depositoCcorrenteParam, FindDefinition definition) {
		return getDepositoCcorrenteDAO().findPaginated(depositoCcorrenteParam, definition);
	}

	public Integer getRowCount(DepositoCcorrenteParam depositoCcorrenteParam) {
		return getDepositoCcorrenteDAO().getRowCount(depositoCcorrenteParam);
	}

	@Override
	protected DepositoCcorrente beforeInsert(DepositoCcorrente bean) {
		bean.setDtCarga(JTDateTimeUtils.getDataAtual());

		//Inicializar o tipo de origem com 'Manual'
		if(bean.getTpOrigem() == null)
			bean.setTpOrigem(new DomainValue("M"));

		//Inicializar a situacao da relacao com false quando não for EDI
		if(!bean.getTpOrigem().getValue().equals("E")) {
			bean.setBlRelacaoFechada(Boolean.FALSE);
		}
		return bean;
	}

	protected DepositoCcorrente beforeStore(DepositoCcorrente bean, ItemList items, ItemListConfig itemListConfig) {
		// Validar se o registro é fechado
		// Retirado validação ao salvar. 17/05/1986
		// validateDepositoCcorrente(bean);

		validateDtDeposito(bean);
		
		BigDecimal vlTotalDocumentos = new BigDecimal("0.00");
		vlTotalDocumentos = vlTotalDocumentos.setScale(2, RoundingMode.HALF_UP);

		DepositoCcorrenteParam depositoCcorrenteParam = new DepositoCcorrenteParam();
		depositoCcorrenteParam.setIdCliente(bean.getCliente().getIdCliente());
		depositoCcorrenteParam.setIdDepositoCcorrente(bean.getIdDepositoCcorrente());

		//Calcular o valor total dos documentos filhos
		for(Iterator<ItemDepositoCcorrente> iter = items.iterator(bean.getIdDepositoCcorrente(), itemListConfig); iter.hasNext();) {
			ItemDepositoCcorrente itemDepositoCcorrente = (ItemDepositoCcorrente)iter.next();
			if (itemDepositoCcorrente.getFatura()!= null) {
					
				if(itemDepositoCcorrente.getFatura().getVlDesconto() != null) 					
					vlTotalDocumentos = vlTotalDocumentos.add(itemDepositoCcorrente.getFatura().getVlTotal().subtract(itemDepositoCcorrente.getFatura().getVlDesconto()));
				else
					vlTotalDocumentos = vlTotalDocumentos.add(itemDepositoCcorrente.getFatura().getVlTotal());
				
				verificaIgualdadeCliente(bean.getCliente().getIdCliente(),
						itemDepositoCcorrente.getFatura().getCliente().getIdCliente());
			} else {
				//Aparece o valor do documento com desconto se o desconto é 'Aprovado'
				if (itemDepositoCcorrente.getDevedorDocServFat().getDesconto() != null && itemDepositoCcorrente.getDevedorDocServFat().getDesconto().getTpSituacaoAprovacao().getValue().equals("A")){
					vlTotalDocumentos = vlTotalDocumentos.add(itemDepositoCcorrente.getDevedorDocServFat().getVlDevido().subtract(itemDepositoCcorrente.getDevedorDocServFat().getDesconto().getVlDesconto()));
				} else {
					vlTotalDocumentos = vlTotalDocumentos.add(itemDepositoCcorrente.getDevedorDocServFat().getVlDevido());
				}				
				//vlTotalDocumentos = vlTotalDocumentos.add(itemDepositoCcorrente.getDevedorDocServFat().getDoctoServico().getVlTotalDocServico());
				verificaIgualdadeCliente(bean.getCliente().getIdCliente(),
						itemDepositoCcorrente.getDevedorDocServFat().getCliente().getIdCliente());
			}
			
		}

		// Se o valor de documento é diferente do valor do depósito, lançar uma exception informando que não pode
		
		if (vlTotalDocumentos.compareTo(bean.getVlDeposito()) > 0){
			throw new BusinessException("LMS-36040");
		}	

		return (DepositoCcorrente)super.beforeStore(bean);
	}

	private void validateDtDeposito(DepositoCcorrente bean) {
		// A data do depósito deve ser menor ou igual a data atual.
		if (JTDateTimeUtils.comparaData(
				bean.getDtDeposito(), 
				JTDateTimeUtils.getDataAtual()) > 0) {
			throw new BusinessException("LMS-36245");
		}
	}  

	/**
	 * Validar se é possível modificar o depósito
	 * 
	 * @author Mickaël Jalbert
	 * @since 30/03/2006
	 * 
	 * @param DepositoCcorrente depositoCcorrente
	 * */
	private void validateDepositoCcorrente(DepositoCcorrente depositoCcorrente){
		// Se o depostito é fechado, e a filial do usuário logado
		// não é MTZ não permitir modificar o objeto
		if (depositoCcorrente.getBlRelacaoFechada().equals(Boolean.TRUE) &&
				!SessionUtils.isFilialSessaoMatriz()){
			throw new BusinessException("LMS-36078");
		}
		
		// Caso a relacao esteja com situação identificada, não permitir alterações.
		if ("I".equals(depositoCcorrente.getTpSituacaoRelacao().getValue())) {
			throw new BusinessException("LMS-36078");
		}
	}

	/**
	 * Método que salva as alterações feitas no mestre e nos detalhes
	 * 
	 * author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param DepositoCcorrente bean
	 * @param ItemList items
	 * @return id gerado para o mestre
	 */
	public DepositoCcorrente store(DepositoCcorrente bean, ItemList items, ItemListConfig config) {
		boolean rollbackMasterId = bean.getIdDepositoCcorrente() == null;
		try {
			if (!items.hasItems())
				throw new BusinessException("LMS-36038");
			
			beforeStore(bean, items, config);
			bean = getDepositoCcorrenteDAO().store(bean,items);
			
		} catch (RuntimeException e) {
			this.rollbackMasterState(bean, rollbackMasterId, e);			
			items.rollbackItemsState();
			throw e;			
		}
		return bean;
	}

	public DepositoCcorrente executeFecharDepositoCcorrente(DepositoCcorrente depositoCcorrente){
		//Validar se pode modificar
		// Retirado validação ao fechar.
		//validateDepositoCcorrente(depositoCcorrente);

		depositoCcorrente.setBlRelacaoFechada(Boolean.TRUE);
		getDepositoCcorrenteDAO().store(depositoCcorrente);

		return depositoCcorrente;
	}

	public List findItemDepositoCcorrente(Long masterId){
		return getDepositoCcorrenteDAO().findItemDepositoCcorrente(masterId);
	}

	/**
	 * Retorna a lista de item deposito conta corrente a partir do 
	 * id do deposito, serve para popular a grid (DF2) e ect.
	 * 
	 * author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param Long masterId
	 * @return List
	 */
	public List findPaginatedItemDepositoCcorrente(Long masterId){
		return getDepositoCcorrenteDAO().findItemDepositoCcorrente(masterId);
	}

	/**
	 * Retorna número de item deposito conta corrente a partir do 
	 * id do deposito, serve para popular a grid (DF2) e ect.
	 * 
	 * author Mickaël Jalbert
	 * @since 27/03/2006
	 * 
	 * @param Long masterId
	 * @return Integer
	 */
	public Integer getRowCountItemDepositoCcorrente(Long masterId){
		return getDepositoCcorrenteDAO().getRowCountItemDepositoCcorrente(masterId);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setDepositoCcorrenteDAO(DepositoCcorrenteDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private DepositoCcorrenteDAO getDepositoCcorrenteDAO() {
		return (DepositoCcorrenteDAO) getDao();
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since  11/10/2006
	 * 
	 * Verifica se os documentos inseridos nos itens pertencem ao mesmo cliente
	 * 
	 * @param ccorrente Dados do pai
	 * @param items Lista dos filhos
	 * @param config Configurador da lista de filhos
	 * @param itemDepositoCcorrente Item que está sendo salvo
	 */
	public void storeBeforeItemDepositoCcorrente(DepositoCcorrenteParam depositoCcorrenteParam, ItemList items, ItemListConfig config, ItemDepositoCcorrente itemDepositoCcorrenteSalvar) {
		for (Iterator<ItemDepositoCcorrente> iter = items.iterator(depositoCcorrenteParam.getIdDepositoCcorrente(),config); iter.hasNext();) {
			ItemDepositoCcorrente itemDepositoCcorrenteLista = iter.next();
			if( itemDepositoCcorrenteLista.getDevedorDocServFat() == null && itemDepositoCcorrenteLista.getFatura() != null ){
				verificaIgualdadeCliente(itemDepositoCcorrenteLista.getFatura().getCliente().getIdCliente(),
						 				 depositoCcorrenteParam.getIdCliente());
			} else {
				verificaIgualdadeCliente(itemDepositoCcorrenteLista.getDevedorDocServFat().getCliente().getIdCliente(),
										 depositoCcorrenteParam.getIdCliente());
			}
		}
	}

	/**
	 * @author José Rodrigo Moraes
	 * @since  11/10/2006
	 * 
	 * Compara dois ids de clientes para verificar se são iguais
	 * 
	 * @param idClientePai Identificador do Cliente informado na classe pai
	 * @param idClienteFilho Identificador do cliente de cobrança do documento de serviço (filho)
	 * @throws BusinessException("LMS-36177")
	 */
	private void verificaIgualdadeCliente(Long idClientePai, Long idClienteFilho) {		
		if( idClientePai.compareTo(idClienteFilho) != 0 ){
			throw new BusinessException("LMS-36177");
		}
	}

	@Override
	protected DepositoCcorrente beforeUpdate(DepositoCcorrente bean) {
		validateDepositoCcorrente(bean);
		return super.beforeUpdate(bean);
	}
	
	/**
	 * Atualiza o depósito e suas dependencias para identificado.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param idDepositoCcorrente
	 * @param dtDeposito
	 * @param items
	 * 
	 * void
	 *
	 */
	public void executeIdentificarDeposito (Long idDepositoCcorrente
			, YearMonthDay dtDeposito
			, ItemList items
			, ItemListConfig config) {
		
		DepositoCcorrente depositoCcorrente = this.findById(idDepositoCcorrente);
		depositoCcorrente.setTpSituacaoRelacao(new DomainValue("I"));
    	getDepositoCcorrenteDAO().store(depositoCcorrente);
		
    	if ("S".equals(configuracoesFacadeImpl.getValorParametro("BL_LMS_INTEGRADO_CORPORATIVO"))) {
    		
    		for (Iterator<ItemDepositoCcorrente> i = items.iterator(
					idDepositoCcorrente, config); i.hasNext();) {
    			liquidaDocumentoDeposio(i.next(), dtDeposito, items);
			}
		}
		
	}
	
	/**
	 * Liquida os documentos do depósito.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param item
	 *
	 * void
	 *
	 */
	public void liquidaDocumentoDeposio (ItemDepositoCcorrente item, YearMonthDay dtDeposito
			, ItemList items) {
		DevedorDocServFat devedorDocServFat = item.getDevedorDocServFat();
		Fatura fatura = item.getFatura();
		
		// Caso seja um documento de serviço.
		if (devedorDocServFat != null) {
			String tpDoc = devedorDocServFat.getDoctoServico().getTpDocumentoServico().getValue();
			if (!"CTR".equals(tpDoc) && !"CTE".equals(tpDoc)) {
				liquidaDevedorDocServFat(dtDeposito, devedorDocServFat);
			}
		// Caso seja uma fatura.
		} else if (fatura != null) {
			DoctoServico ds = getFirstDoctoServicoFromFatura(fatura);
			if( ds != null ){
				DomainValue tpDoc = ds.getTpDocumentoServico();
				if ( !"CTR".equals(tpDoc) && !"CTE".equals(tpDoc) ) {    
				liquidafatura(dtDeposito, fatura);
			}
		}
		}
		
	}

	/**
	 * Liquida o devedorDocServFat.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param dtDeposito
	 * @param devedorDocServFat
	 *
	 * void
	 *
	 */
	private void liquidaDevedorDocServFat(YearMonthDay dtDeposito,
			DevedorDocServFat devedorDocServFat) {
		// Liquida o documento.
		devedorDocServFat = devedorDocServFatService.findByIdInitLazyProperties(devedorDocServFat.getIdDevedorDocServFat(), false);
		devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
		devedorDocServFat.setDtLiquidacao(dtDeposito);
		devedorDocServFatService.store(devedorDocServFat);
	}

	/**
	 * Liquida a fatura.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param dtDeposito
	 * @param fatura
	 *
	 * void
	 *
	 */
	private void liquidafatura(YearMonthDay dtDeposito, Fatura fatura) {
		// Liquida a fatura e atualiza o valor total recebido
		fatura.setTpSituacaoFatura(new DomainValue("LI"));
		fatura.setDtLiquidacao(dtDeposito);
		fatura.setVlTotalRecebido(fatura.getVlTotal().subtract(fatura.getVlDesconto()));
		faturaService.storeBasic(fatura);
		// Liquida os documentos da fatura.
		liquidaDocumentosFatura(fatura.getItemFaturas(), dtDeposito);
		
		// Caso exista boleto, liquida o mesmo.
		Boleto boleto = fatura.getBoleto();
		if (boleto != null) {
			boleto.setTpSituacaoBoleto(new DomainValue("LI"));
			boletoService.storeBasic(boleto);
		}
		// Caso exista recibo, liquida o mesmo.
		Recibo recibo = fatura.getRecibo();
		if (recibo != null) {
			recibo.setTpSituacaoRecibo(new DomainValue("R"));
			reciboService.storeBasic(recibo); 
		}
		// Encerra a cobrança de inadimplencia.
		encerraCobrancaInadimplenciaByFatura(fatura);
	}
	
	/**
	 * Encerra a cobrança de inadimplencia.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param fatura
	 *
	 * void
	 *
	 */
	private void encerraCobrancaInadimplenciaByFatura (Fatura fatura) {
		Long nrItensLiquidados = itemCobrancaService.findItemCobrancaByIdFatura(
				fatura.getIdFatura()
				, new DomainValue("LI")
				, new DomainValue("CA")); 
		
		Long nrAllItens = itemCobrancaService.findItemCobrancaByIdFatura(
				fatura.getIdFatura()
				, new DomainValue[]{});
		
		// Caso todas as faturas da cobranca de inadimplencia
		// da fatura em questão sejam liquidadas, encerra a 
		// cobrança de inadimplencia.
		if (nrAllItens > 0 
				&& nrAllItens.equals(nrItensLiquidados)) {
			CobrancaInadimplencia cobranca = (CobrancaInadimplencia)cobrancaInadimplenciaService
					.findByIdFatura(fatura.getIdFatura(), null).get(0);
			cobranca.setBlCobrancaEncerrada(Boolean.TRUE);
			cobrancaInadimplenciaService.store(cobranca);
		}
	}
	
	/**
	 * Liquida os documentos da fatura.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param itens
	 * @param dtDeposito
	 *
	 * void
	 *
	 */
	private void liquidaDocumentosFatura (List<ItemFatura> itens, YearMonthDay dtDeposito) {
		DevedorDocServFat ddsf;
		// Liquida os documentos da fatura.
		for (ItemFatura item : itens) {
			liquidaDevedorDocServFat(dtDeposito, item.getDevedorDocServFat());
		}
	}
	
	/**
	 * Carrega o primeiro documento de serviço da fatura.
	 * 
	 * Hector Julian Esnaola Junior
	 * 29/02/2008
	 *
	 * @param fatura
	 * @return
	 *
	 * DoctoServico
	 *
	 */
	private DoctoServico getFirstDoctoServicoFromFatura (Fatura fatura) {
		fatura = faturaService.findById(fatura.getIdFatura());
		DoctoServico ds = null;
		if (fatura.getItemFaturas() != null) {	
			ds = ((ItemFatura)fatura.getItemFaturas().get(0))
					.getDevedorDocServFat().getDoctoServico();		
			faturaService.evict(fatura);
			faturaService.flush();
		}
		return ds;
	}

	public void setConfiguracoesFacadeImpl(
			ConfiguracoesFacade configuracoesFacadeImpl) {
		this.configuracoesFacadeImpl = configuracoesFacadeImpl;
	}

	public void setItemCobrancaService(ItemCobrancaService itemCobrancaService) {
		this.itemCobrancaService = itemCobrancaService;
	}

	public void setCobrancaInadimplenciaService(
			CobrancaInadimplenciaService cobrancaInadimplenciaService) {
		this.cobrancaInadimplenciaService = cobrancaInadimplenciaService;
	}

	public void setDevedorDocServFat(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setReciboService(ReciboService reciboService) {
		this.reciboService = reciboService;
	}

}