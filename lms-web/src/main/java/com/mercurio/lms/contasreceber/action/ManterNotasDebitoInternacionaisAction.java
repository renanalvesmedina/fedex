package com.mercurio.lms.contasreceber.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
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
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.service.CotacaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.FaturaRecibo;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.MotivoDesconto;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.contasreceber.model.service.DescontoService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatLookUpService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contasreceber.model.service.FaturaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.AgrupamentoCliente;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TipoAgrupamento;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.manterNotasDebitoInternacionaisAction"
 */

public class ManterNotasDebitoInternacionaisAction extends MasterDetailAction {
	
	private FilialService filialService;
	
	private ClienteService clienteService;
	
	private DivisaoClienteService divisaoClienteService;
	
	private CotacaoMoedaService cotacaoMoedaService;
	
	private DomainValueService domainValueService;
	
	private DevedorDocServFatService devedorDocServFatService;
	
	private DevedorDocServFatLookUpService devedorDocServFatLookUpService;
	
	private DescontoService descontoService;
	
	private MoedaService moedaService;

	public List findTipoDocumentoServico(Map criteria){
        List dominiosValidos = new ArrayList();
        dominiosValidos.add("CTR");
        dominiosValidos.add("CRT");
        dominiosValidos.add("NFS");
        dominiosValidos.add("NDN");
		dominiosValidos.add("NSE");
		dominiosValidos.add("NTE");
		dominiosValidos.add("CTE");
        List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
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
	public Map findInitialValue(){
		Map retorno = new HashMap();
		
		Filial filial = this.getFilialService().findFilialUsuarioLogado();			
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
	
	public Map findFilialCliente(Long idCliente){
		Map retorno = new HashMap();
		Filial filial = this.getFilialService().findFilialCobrancaByCliente(idCliente);
		
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
	public Serializable store(TypedFlatMap map) {
		
		MasterEntry entry = getMasterFromSession(map.getLong("idFatura"), true);		
		ItemList items = getItemsFromSession(entry, "itemFatura");		

							mountFatura((Fatura) entry.getMaster(), map);	
		
		Serializable retorno = null;
		items.resetItemsState(); 
    	updateMasterInSession(entry);
		return retorno;
	}
	
	/**
	 * M�todo que monta uma fatura a partir do map que vem da tela.
	 * 
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * 
	 * @param Fatura fatura 
	 * @param TypedFlatMap map
	 * @return Fatura
	 */	
	private Fatura mountFatura(Fatura fatura, TypedFlatMap map) {
		Filial filialByIdFilial = new Filial();
		filialByIdFilial.setIdFilial(map.getLong("filialByIdFilial.idFilial"));
		fatura.setFilialByIdFilial(filialByIdFilial);

		fatura.setCliente(clienteService.findById(map.getLong("cliente.idCliente")));	
		
		Filial filialByIdFilialCobradora = new Filial();
		filialByIdFilialCobradora.setIdFilial(map.getLong("filialByIdFilialCobradora.idFilial"));
		fatura.setFilialByIdFilialCobradora(filialByIdFilialCobradora);
		
		if (map.getLong("divisaoCliente.idDivisaoCliente") != null){
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(map.getLong("divisaoCliente.idDivisaoCliente"));
			fatura.setDivisaoCliente(divisaoCliente);
		}
		
		Cedente cedente = new Cedente();
		cedente.setIdCedente(map.getLong("cedente.idCedente"));
		fatura.setCedente(cedente);
		
		AgrupamentoCliente agrupamentoCliente = new AgrupamentoCliente();
		agrupamentoCliente.setIdAgrupamentoCliente(map.getLong("agrupamentoCliente.idAgrupamentoCliente"));
		fatura.setAgrupamentoCliente(agrupamentoCliente);
		
		TipoAgrupamento tipoAgrupamento = new TipoAgrupamento();
		tipoAgrupamento.setIdTipoAgrupamento(map.getLong("tipoAgrupamento.idTipoAgrupamento"));
		fatura.setTipoAgrupamento(tipoAgrupamento);
		
		if (map.getLong("moeda.idMoeda") != null) {
			Moeda moeda = new Moeda();
			moeda.setIdMoeda(map.getLong("moeda.idMoeda"));
			fatura.setMoeda(moeda);
		}
		
		if (map.getLong("cotacaoMoeda.idCotacaoMoeda") != null){
			CotacaoMoeda cotacaoMoeda = new CotacaoMoeda();
			cotacaoMoeda.setIdCotacaoMoeda(map.getLong("cotacaoMoeda.idCotacaoMoeda"));
			fatura.setCotacaoMoeda(cotacaoMoeda);
		} else {
			fatura.setCotacaoMoeda(null);
		}

		fatura.setIdFatura(map.getLong("idFatura"));
		fatura.setNrFatura(map.getLong("nrFatura"));
		fatura.setNrPreFatura(map.getString("nrPreFatura"));
		fatura.setTpModal(map.getDomainValue("tpModal"));
		fatura.setTpAbrangencia(map.getDomainValue("tpAbrangencia"));		
		fatura.setTpSituacaoFatura(map.getDomainValue("tpSituacaoFatura"));
		fatura.setTpSituacaoAprovacao(map.getDomainValue("tpSituacaoAprovacao"));
		fatura.setVlCotacaoMoeda(map.getBigDecimal("vlCotacaoMoeda"));
		fatura.setDtEmissao(map.getYearMonthDay("dtEmissao"));
		fatura.setDtVencimento(map.getYearMonthDay("dtVencimento"));
		fatura.setBlGerarBoleto(map.getBoolean("blGerarBoleto"));
		fatura.setBlGerarEdi(map.getBoolean("blGerarEdi"));
		fatura.setBlFaturaReemitida(map.getBoolean("blFaturaReemitida"));
		fatura.setBlIndicadorImpressao(map.getBoolean("blIndicadorImpressao"));		
		fatura.setDhTransmissao(map.getDateTime("dhTransmissao"));
		fatura.setDhReemissao(map.getDateTime("dhReemissao"));
		fatura.setTpFatura(map.getDomainValue("tpFatura"));
		fatura.setTpOrigem(map.getDomainValue("tpOrigem"));
		fatura.setObFatura(map.getString("obFatura"));
		
		fatura.setVlBaseCalcPisCofinsCsll(map.getBigDecimal("vlBaseCalcPisCofinsCsll"));
		fatura.setVlBaseCalcIr(map.getBigDecimal("vlBaseCalcIr"));
		fatura.setVlPis(map.getBigDecimal("vlPis"));
		fatura.setVlCofins(map.getBigDecimal("vlCofins"));
		fatura.setVlCsll(map.getBigDecimal("vlCsll"));
		fatura.setVlIva(map.getBigDecimal("vlIva"));
		fatura.setVlIr(map.getBigDecimal("vlIr"));
		fatura.setVlTotal(map.getBigDecimal("vlTotal"));
		fatura.setVlDesconto(map.getBigDecimal("vlDesconto"));
		fatura.setVlTotalRecebido(map.getBigDecimal("vlTotalRecebido"));
		fatura.setVlJuroCalculado(map.getBigDecimal("vlJuroCalculado"));
		fatura.setVlJuroRecebido(map.getBigDecimal("vlJuroRecebido"));

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
	private ItemFatura mountItemFatura(TypedFlatMap map){
		Cliente cliente = new Cliente();
		cliente.setIdCliente(map.getLong("idCliente"));
		DomainValue domainValue = null;
		List list = null;
		
		if (map.getBigDecimal("devedorDocServFat.desconto.vlDesconto") != null){
			MotivoDesconto motivoDesconto = new MotivoDesconto();
			motivoDesconto.setIdMotivoDesconto(map.getLong("devedorDocServFat.desconto.motivoDesconto.idMotivoDesconto"));
			
			Desconto desconto = new Desconto();
			desconto.setVlDesconto(map.getBigDecimal("devedorDocServFat.desconto.vlDesconto"));
			desconto.setObDesconto(map.getString("devedorDocServFat.desconto.obDesconto"));
			desconto.setMotivoDesconto(motivoDesconto);
			
			//Se j� existe um desconto, ele detalha
			if (map.getLong("devedorDocServFat.desconto.idDesconto") != null) {
				domainValue = new DomainValue();
				domainValue.setValue(map.getString("devedorDocServFat.desconto.tpSituacaoAprovacaoVal"));
				domainValue.setDescription(map.getVarcharI18n("devedorDocServFat.desconto.tpSituacaoAprovacaoDesc"));
				
				desconto.setIdDesconto(map.getLong("devedorDocServFat.desconto.idDesconto"));
				desconto.setTpSituacaoAprovacao(domainValue);
			}
			
			list = new ArrayList();
			
			list.add(desconto);		
		}

		
		Filial filial = this.filialService.findById(map.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"));
		
		Moeda moeda = this.moedaService.findById(map.getLong("devedorDocServFat.doctoServico.moeda.idMoeda"));
				
		domainValue = this.domainValueService.findDomainValueByValue("DM_TIPO_DOCUMENTO_SERVICO",map.getString("devedorDocServFat.doctoServico.tpDocumentoServico"));
		
		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico(map.getLong("devedorDocServFat.doctoServico.idDoctoServico"));
		doctoServico.setVlTotalDocServico(map.getBigDecimal("devedorDocServFat.doctoServico.vlTotalDocServico"));
		doctoServico.setTpDocumentoServico(domainValue);
		doctoServico.setNrDoctoServico(map.getLong("devedorDocServFat.doctoServico.nrDoctoServicoTmp"));		
		doctoServico.setFilialByIdFilialOrigem(filial);
		doctoServico.setMoeda(moeda);
		
		DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
		devedorDocServFat.setIdDevedorDocServFat(map.getLong("devedorDocServFat.idDevedorDocServFat"));
		devedorDocServFat.setDoctoServico(doctoServico);
		devedorDocServFat.setCliente(cliente);

		devedorDocServFat.setDescontos(list);
		
		ItemFatura itemFatura = new ItemFatura();

		itemFatura.setDevedorDocServFat(devedorDocServFat);
		itemFatura.setIdItemFatura(map.getLong("idItemFatura"));

		return itemFatura;
	}	
	
	/**
	 * Busca um mestre pelo seu id e armazena-o na sess�o do usu�rio
	 * 
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * 
	 * @param id
	 * @return Fatura, objeto mestre
	 */
	public Map findById(java.lang.Long id) {
		Object masterObj = this.getFaturaService().findByIdTela(id);
		putMasterInSession(masterObj);
		
		Fatura fatura = (Fatura)masterObj;
		
		TypedFlatMap map = new TypedFlatMap();
		map.put("idFatura", fatura.getIdFatura());	
		map.put("filialByIdFilial.idFilial", fatura.getFilialByIdFilial().getIdFilial());
		map.put("filialByIdFilial.sgFilial", fatura.getFilialByIdFilial().getSgFilial());
		map.put("filialByIdFilial.pessoa.nmFantasia", fatura.getFilialByIdFilial().getPessoa().getNmFantasia());	
		map.put("nrFatura", fatura.getNrFatura());
		map.put("cliente.pessoa.nrIdentificacao", fatura.getCliente().getPessoa().getNrIdentificacao());
		map.put("cliente.pessoa.nmPessoa", fatura.getCliente().getPessoa().getNmPessoa());		
		map.put("cliente.idCliente", fatura.getCliente().getIdCliente()); 	
		map.put("cliente.filialByIdFilialCobranca.idFilial", fatura.getCliente().getFilialByIdFilialCobranca().getIdFilial());
		map.put("cliente.filialByIdFilialCobranca.sgFilial", fatura.getCliente().getFilialByIdFilialCobranca().getSgFilial());
		map.put("cliente.filialByIdFilialCobranca.pessoa.nmFantasia", fatura.getCliente().getFilialByIdFilialCobranca().getPessoa().getNmFantasia());	
		map.put("filialByIdFilialCobradora.idFilial", fatura.getFilialByIdFilialCobradora().getIdFilial());
		map.put("filialByIdFilialCobradora.sgFilial", fatura.getFilialByIdFilialCobradora().getSgFilial());
		map.put("filialByIdFilialCobradora.pessoa.nmFantasia", fatura.getFilialByIdFilialCobradora().getPessoa().getNmFantasia());			
		map.put("nrPreFatura", fatura.getNrPreFatura());
		
		if (fatura.getDivisaoCliente() != null){
			map.put("divisaoCliente.idDivisaoCliente", fatura.getDivisaoCliente().getIdDivisaoCliente());
		}
		
		if (fatura.getCedente() != null) {
			map.put("cedente.idCedente", fatura.getCedente().getIdCedente());
		}
		
		map.put("tpSituacaoFatura", fatura.getTpSituacaoFatura().getValue());
		
		if (fatura.getTpSituacaoAprovacao() != null){
			map.put("tpSituacaoAprovacao", fatura.getTpSituacaoAprovacao().getValue());
		}
		
		if (fatura.getCotacaoMoeda() != null) {
			map.put("simboloMoedaPais", fatura.getCotacaoMoeda().getMoedaPais().getMoeda().getSiglaSimbolo()); 		
			map.put("dtCotacaoMoeda", fatura.getCotacaoMoeda().getDtCotacaoMoeda()); 
			map.put("cotacaoMoeda.idCotacaoMoeda", fatura.getCotacaoMoeda().getIdCotacaoMoeda());
			map.put("cotacaoMoeda.vlCotacaoMoeda", fatura.getCotacaoMoeda().getVlCotacaoMoeda());
		}
		
		map.put("vlCotacaoMoeda", fatura.getVlCotacaoMoeda());
		map.put("dtEmissao", fatura.getDtEmissao());
		map.put("dtVencimento", fatura.getDtVencimento());
		map.put("blGerarEdi", fatura.getBlGerarEdi());
		map.put("blGerarBoleto", fatura.getBlGerarBoleto());
		map.put("dhTransmissao", fatura.getDhTransmissao());

		if (fatura.getItemRedecos() != null && fatura.getItemRedecos().size() > 0) {
			map.put("redeco.idRedeco", ((ItemRedeco)fatura.getItemRedecos().get(0)).getRedeco().getIdRedeco());
			map.put("redeco.filial.sgFilial", ((ItemRedeco)fatura.getItemRedecos().get(0)).getRedeco().getFilial().getSgFilial());
			map.put("redeco.nrRedeco", ((ItemRedeco)fatura.getItemRedecos().get(0)).getRedeco().getNrRedeco());
		}
		
		if (fatura.getFaturaRecibos() != null && fatura.getFaturaRecibos().size() > 0) {
			map.put("recibo.idRecibo", ((FaturaRecibo)fatura.getFaturaRecibos().get(0)).getRecibo().getIdRecibo());
			map.put("recibo.filial.sgFilial", ((FaturaRecibo)fatura.getFaturaRecibos().get(0)).getRecibo().getFilialByIdFilialEmissora().getSgFilial());
			map.put("recibo.nrRedeco", ((FaturaRecibo)fatura.getFaturaRecibos().get(0)).getRecibo().getNrRecibo());
		}		
		
		map.put("dtLiquidacao", fatura.getDtLiquidacao());
		
		if (fatura.getRelacaoCobranca() != null) {
			map.put("relacaoCobranca.idRelacaoCobranca", fatura.getRelacaoCobranca().getIdRelacaoCobranca());
			map.put("relacaoCobranca.nrRelacaoCobrancaFilial", fatura.getRelacaoCobranca().getNrRelacaoCobrancaFilial());
			map.put("relacaoCobranca.filial.sgFilial", fatura.getRelacaoCobranca().getFilial().getSgFilial());
		}
		
		map.put("dhReemissao", fatura.getDhReemissao());
		
		if (fatura.getUsuario() != null){
			map.put("usuario.nmUsuario", fatura.getUsuario().getNmUsuario());
		}
		map.put("qtDocumentos", fatura.getQtDocumentos());
		map.put("vlBaseCalcPisCofinsCsll", fatura.getVlBaseCalcPisCofinsCsll());
		map.put("vlBaseCalcIr", fatura.getVlBaseCalcIr());
		map.put("vlPis", fatura.getVlPis());
		map.put("vlCofins", fatura.getVlCofins());
		map.put("vlCsll", fatura.getVlCsll());
		map.put("vlIva", fatura.getVlIva());
		map.put("vlIr", fatura.getVlIr());
		map.put("vlTotal", fatura.getVlTotal());
		map.put("vlDesconto", fatura.getVlDesconto());
		map.put("vlTotalRecebido", fatura.getVlTotalRecebido());
		map.put("vlJuroCalculado", fatura.getVlJuroCalculado());
		map.put("vlJuroRecebido", fatura.getVlJuroRecebido());
		map.put("tpFatura", fatura.getTpFatura().getValue()); 
		map.put("tpOrigem", fatura.getTpOrigem().getValue());			
		map.put("obFatura", fatura.getObFatura());
		map.put("moeda.idMoeda", fatura.getMoeda().getIdMoeda());
		return map;
	}
	
	public ResultSetPage findPaginated(Map filtros) {
		ResultSetPage rsp = super.findPaginated(filtros);
		List list = rsp.getList();
		List listRet = new ArrayList();
		TypedFlatMap mapRet = null;
		
		for (Iterator iter = list.iterator(); iter.hasNext();){
			Fatura fatura = (Fatura) iter.next();
			mapRet = new TypedFlatMap();

			mapRet.put("idFatura",fatura.getIdFatura());
			mapRet.put("filialByIdFilial.sgFilial",fatura.getFilialByIdFilial().getSgFilial());
			mapRet.put("nrFatura",fatura.getNrFatura());
			mapRet.put("filialByIdFilialCobradora.sgFilial",fatura.getFilialByIdFilialCobradora().getSgFilial());
			mapRet.put("cliente.pessoa.nmPessoa",fatura.getCliente().getPessoa().getNmPessoa());
			mapRet.put("dtEmissao",fatura.getDtEmissao());
			mapRet.put("dtVencimento",fatura.getDtVencimento());
			mapRet.put("dtLiquidacao",fatura.getDtLiquidacao());
			mapRet.put("vlTotal",fatura.getVlTotal());
			mapRet.put("vlDesconto",fatura.getVlDesconto());
			
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
	 * Remove uma listra de registros mestres
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
	 * Remove um registro mestre
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * @param id
	 */
	public void removeById(Long id) {	
		this.getFaturaService().removeById(id);
		newMaster();			
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
		
		//Validar se o usu�rio tem direito de modificar a fatura
		this.getFaturaService().validateFatura((Long)entry.getMasterId());
		
		super.removeItemByIds(ids, "itemFatura");
	}	
	
	
	/**
	 * Salva um registro detalhe/filho na sess�o.
	 * author Micka�l Jalbert
	 * 31/01/2006
	 * @param parameters Parametros utilizado para montar o detalhe
	 * @return id do detalhe (tempor�rio no caso de inser��o)
	 */
	public Serializable storeItemFatura(TypedFlatMap map) {
		return saveItemInstance(map, "itemFatura");
	}
	
	public Map findSomatorios(Long idFatura){
		Map map = new HashMap();

		MasterEntry entry = getMasterFromSession(idFatura, true);			
		ItemList items = getItemsFromSession(entry, "itemFatura");
		ItemListConfig config = getMasterConfig().getItemListConfig("itemFatura");
		
		BigDecimal vlDocumentoTotal = new BigDecimal("0.00");
		BigDecimal vlDescontoTotal = new BigDecimal("0.00");
		vlDocumentoTotal = vlDocumentoTotal.setScale(2, RoundingMode.HALF_UP);
		vlDescontoTotal = vlDescontoTotal.setScale(2, RoundingMode.HALF_UP);

		
		for (Iterator iter = items.iterator(idFatura, config); iter.hasNext();) {
			ItemFatura itemFatura = (ItemFatura)iter.next();
			vlDocumentoTotal = vlDocumentoTotal.add(itemFatura.getDevedorDocServFat().getDoctoServico().getVlTotalDocServico());
			
			if (itemFatura.getDevedorDocServFat().getDescontos() != null && itemFatura.getDevedorDocServFat().getDescontos().size() > 0){
				vlDescontoTotal = vlDescontoTotal.add(((Desconto)itemFatura.getDevedorDocServFat().getDescontos().get(0)).getVlDesconto());
			}
		}
		
		map.put("qtdeTotalDocumentos", Integer.valueOf(items.size()));
		map.put("valorTotalDocumentos",vlDocumentoTotal);
		map.put("valorTotalDesconto",vlDescontoTotal);	

		return map;
	}
	
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(Fatura.class);
		
		// Comparador para realizar a ordena��o dos items filhos de acordo com a regra de neg�cio.
		Comparator descComparator = new Comparator() {
			
			public int compare(Object o1, Object o2) {
				Collator collator = Collator.getInstance(LocaleContextHolder.getLocale());
				
				if ((o1 instanceof Comparable) && (o2 instanceof Comparable)) {
					return ((Comparable)o1).compareTo(o2);
				} else {
					ItemFatura d1 = (ItemFatura) o1;
					ItemFatura d2 = (ItemFatura) o2;
					
					return collator.compare(d1.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().toString(),
							d2.getDevedorDocServFat().getDoctoServico().getNrDoctoServico().toString());
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
				ItemFatura itemFaturaNew = mountItemFatura((TypedFlatMap)parameters);
				
				resolveDomainValueProperties(itemFaturaNew);
				ItemList items = getItemsFromSession(getMasterFromSession(getMasterId(parameters), true), "itemFatura");
				ItemListConfig config = getMasterConfig().getItemListConfig("itemFatura");
				
				MasterEntry entry = getMasterFromSession(getMasterId(parameters), true);
				
				//Regras de neg�cio
				getFaturaService().storeBeforeItemFatura((Fatura)entry.getMaster(), items, config, itemFaturaNew);				

				return itemFaturaNew;
			}
			
		};
		
		config.addItemConfig("itemFatura",ItemFatura.class, itemInit, descComparator);
		return config;
	}	
	
	public ResultSetPage findPaginatedItemFatura(Map parameters) {

		ResultSetPage rsp = findPaginatedItemList(parameters, "itemFatura");			
		List list = rsp.getList();			
		List listRet = new ArrayList();			
		Map map = null;
		
		for (Iterator iter = list.iterator(); iter.hasNext();){
			ItemFatura itemFatura = (ItemFatura)iter.next();		
			List listDescontos = itemFatura.getDevedorDocServFat().getDescontos();
			map = new TypedFlatMap();
			
			map.put("idItemFatura",itemFatura.getIdItemFatura());
			map.put("tpDocumentoServico",itemFatura.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico());
			map.put("sgFilial",itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
			map.put("nmFantasia",itemFatura.getDevedorDocServFat().getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNmFantasia());				
			map.put("nrDoctoServico",itemFatura.getDevedorDocServFat().getDoctoServico().getNrDoctoServico());
			map.put("vlTotalDocServico",itemFatura.getDevedorDocServFat().getDoctoServico().getVlTotalDocServico());
							
			if (listDescontos != null && listDescontos.size() == 1) {
				map.put("vlDesconto",((Desconto)listDescontos.get(0)).getVlDesconto());
			} else {
				map.put("vlDesconto",new BigDecimal("0"));
			}
			
			listRet.add(map);
			
			rsp.setList(listRet);
		}
		
		return rsp;
	}	
	
	public Integer getRowCountItemFatura(Map parameters){
		if (getMasterId(parameters) != null) {		
			return getRowCountItemList(parameters, "itemFatura");
		}
		return null;
	}
	
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
	
	
	
		
	public List findLookupFilial(TypedFlatMap map){
		return this.getFilialService().findLookup(map);
	}
	
	public List findLookupFilialCobradora(TypedFlatMap map){
		return this.getFilialService().findLookup(map);
	}
	
	public List findLookupCliente(Map criteria){
		return this.getClienteService().findLookup(criteria);
	}
	
	public List findComboDivisaoCliente(TypedFlatMap criteria){
		return this.getDivisaoClienteService().find(criteria);
	}	
	public List findLookupCotacaoMoeda(TypedFlatMap criteria){
		return this.getCotacaoMoedaService().findLookup(criteria);
	}
	
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public DivisaoClienteService getDivisaoClienteService() {
		return divisaoClienteService;
	}

	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public CotacaoMoedaService getCotacaoMoedaService() {
		return cotacaoMoedaService;
	}

	public void setCotacaoMoedaService(CotacaoMoedaService cotacaoMoedaService) {
		this.cotacaoMoedaService = cotacaoMoedaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		super.setMasterService(faturaService);
	}
	
	public FaturaService getFaturaService() {
		return (FaturaService)super.getMasterService();
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}		

	public DevedorDocServFatService getDevedorDocServFatService() {
		return devedorDocServFatService;
	}

	public void setDevedorDocServFatService(
			DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public DescontoService getDescontoService() {
		return descontoService;
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public DevedorDocServFatLookUpService getDevedorDocServFatLookUpService() {
		return devedorDocServFatLookUpService;
	}

	public void setDevedorDocServFatLookUpService(
			DevedorDocServFatLookUpService devedorDocServFatLookUpService) {
		this.devedorDocServFatLookUpService = devedorDocServFatLookUpService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}	

}
