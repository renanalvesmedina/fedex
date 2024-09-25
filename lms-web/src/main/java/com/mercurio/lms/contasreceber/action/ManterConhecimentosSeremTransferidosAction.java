package com.mercurio.lms.contasreceber.action;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.AgendaTransferencia;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.MotivoTransferencia;
import com.mercurio.lms.contasreceber.model.param.DevedorDocServFatLookupParam;
import com.mercurio.lms.contasreceber.model.param.DivisaoClienteParam;
import com.mercurio.lms.contasreceber.model.service.AgendaTransferenciaService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatLookUpService;
import com.mercurio.lms.contasreceber.model.service.MotivoTransferenciaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

import java.io.Serializable;
import java.util.*;

/**
 * Generated by: ADSM ActionGenerator
 * <p>
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 *
 * @spring.bean id="lms.contasreceber.manterConhecimentosSeremTransferidosAction"
 */

public class ManterConhecimentosSeremTransferidosAction extends CrudAction {
	
	private FilialService filialService;
	private DoctoServicoService doctoServicoService;
	private DivisaoClienteService divisaoClienteService;
	private ClienteService clienteService;
	private DevedorDocServFatLookUpService devedorDocServFatLookUpService;
	private ConfiguracoesFacade configuracoesFacade;
	private MotivoTransferenciaService motivoTransferenciaService;
	private DomainValueService domainValueService; 

	public TypedFlatMap validateMonitoramentoEletronicoAutorizado(TypedFlatMap map) {
		Long idDoctoServico  = map.getLong("data.idDoctoServico");
		if (idDoctoServico != null) {
			doctoServicoService.validateDoctoServicoComMonitoramentoEletronicoAutorizado(idDoctoServico);
		}
		return map;
	}

	
	/**
	 * Retorno os valores necess�rio para inicializar a tela de fatura
	 * 
	 * @return Map map de objetos para preencher os dados inicias da tela
     */
	public Map findInitialValue(){
		Map retorno = new HashMap();
		
		Filial filial = this.filialService.findFilialUsuarioLogado();			
		retorno.put("nmFantasia",filial.getPessoa().getNmFantasia());
		retorno.put("idFilial",filial.getIdFilial());
		retorno.put("sgFilial",filial.getSgFilial());
		retorno.put("dataAtual", JTDateTimeUtils.getDataAtual());
			
		return retorno;
	}	
	
	public Serializable store(TypedFlatMap map) {		
		AgendaTransferencia agendaTransferencia = mountAgendaTransferencia(map);
		return ((AgendaTransferenciaService)this.defaultService).store(agendaTransferencia);
	}
	
	/**
	 * Monta um objeto AgendaTransferencia a partir do TypedFlatMap da tela.
	 * 
     * @param map TypedFlatMap
     * @return AgendaTransferencia
	 * @author Micka�l Jalbert
	 * 07/03/2006
     */
	private AgendaTransferencia mountAgendaTransferencia(TypedFlatMap map){
		AgendaTransferencia agendaTransferencia = new AgendaTransferencia();
		
		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setIdDoctoServico(map.getLong("devedorDocServFat.doctoServico.idDoctoServico"));
		
		Filial filialDoctoServico = new Filial();
		filialDoctoServico.setIdFilial(map.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial"));
		
		doctoServico.setFilialByIdFilialOrigem(filialDoctoServico);
		
		Filial filialOrigem = new Filial();
		filialOrigem.setIdFilial(map.getLong("filialByIdFilialOrigem.idFilial"));
		
		if(map.getLong("divisaoCliente.idDivisaoCliente") != null){
			DivisaoCliente div = new DivisaoCliente();
			div.setIdDivisaoCliente(map.getLong("divisaoCliente.idDivisaoCliente"));
			agendaTransferencia.setDivisaoCliente(div);
		}
		
		
		DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
		devedorDocServFat.setIdDevedorDocServFat(map.getLong("devedorDocServFat.idDevedorDocServFat"));			
		devedorDocServFat.setDoctoServico(doctoServico);
		devedorDocServFat.setFilial(filialOrigem);
		
		Filial filialDestino = new Filial();
		filialDestino.setIdFilial(map.getLong("filialByIdFilialDestino.idFilial"));
		
		MotivoTransferencia motivoTransferencia = new MotivoTransferencia();
		motivoTransferencia.setIdMotivoTransferencia(map.getLong("motivoTransferencia.idMotivoTransferencia"));
		
		agendaTransferencia.setCliente(clienteService.findById(map.getLong("cliente.idCliente")));
		agendaTransferencia.setDevedorDocServFat(devedorDocServFat);
		agendaTransferencia.setFilialByIdFilialOrigem(filialOrigem);
		agendaTransferencia.setFilialByIdFilialDestino(filialDestino);
		agendaTransferencia.setMotivoTransferencia(motivoTransferencia);
		
		agendaTransferencia.setIdAgendaTransferencia(map.getLong("idAgendaTransferencia"));
		agendaTransferencia.setObAgendaTransferencia(map.getString("obAgendaTransferencia"));
		agendaTransferencia.setDtPrevistaTransferencia(map.getYearMonthDay("dtPrevistaTransferencia"));
		agendaTransferencia.setTpOrigem(map.getDomainValue("tpOrigem"));
		return agendaTransferencia;
	}
	
	
	public DevedorDocServFatLookupParam mountLookupParam(TypedFlatMap map){
		Long idFilial = map.getLong("doctoServico.filialByIdFilialOrigem.idFilial");	
		Long nrDocumento = map.getLong("doctoServico.nrDoctoServico");
		String tpDocumentoServico = map.getString("doctoServico.tpDocumentoServico");				

		DevedorDocServFatLookupParam devedorDocServFatLookupParam = new DevedorDocServFatLookupParam();
		
		devedorDocServFatLookupParam.setIdFilial(idFilial);
		devedorDocServFatLookupParam.setNrDocumentoServico(nrDocumento);
		devedorDocServFatLookupParam.setTpDocumentoServico(tpDocumentoServico);
		devedorDocServFatLookupParam.setTpSituacaoDevedorDocServFatValido(map.getInteger("tpSituacaoDevedorDocServFatValido"));
		
		return devedorDocServFatLookupParam;
	}
	
	/**
	 * Busca documento de servi�o sem a mensagem de valida��o caso o documento 
	 * n�o esteja pendente 
     *
	 * @param map
	 * @return
	 */
	public List findDevedorServDocFatNoMessage(TypedFlatMap map){
		DevedorDocServFatLookupParam devedorDocServFatLookupParam = mountLookupParam(map);
		return AliasToNestedMapResultTransformer.getInstance().transformListResult(
				this.devedorDocServFatLookUpService.findDevedorDocServFat(devedorDocServFatLookupParam,null));
	}
	
	public List findDevedorServDocFat(TypedFlatMap map){
		DevedorDocServFatLookupParam devedorDocServFatLookupParam = mountLookupParam(map);
		return AliasToNestedMapResultTransformer.getInstance().transformListResult(
				this.devedorDocServFatLookUpService.findDevedorDocServFat(devedorDocServFatLookupParam,"LMS-36006"));
	}	

	public List findLookupFilial(TypedFlatMap tfm){ 
		return this.filialService.findLookup(tfm);
	}
	 
	public List findLookupCliente(Map criteria) {		
		return this.clienteService.findLookup(criteria);
	}
	
	public List findLookupClienteAtivo(Map criteria) {	
		return this.clienteService.findLookup(criteria);
	}	
	
	public List findMotivoTransferencia(TypedFlatMap map){
		return this.motivoTransferenciaService.find(map);
	}
	
	public List findTipoDocumentoServico(Map criteria){
		String tpDocFat6 = (String)configuracoesFacade
				.getValorParametro("TP_DOCTO_FAT3");

		String[] dm = tpDocFat6.split(";");
        List dominiosValidos = Arrays.asList(dm);
        List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
        return retorno;
	}	

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
    	((AgendaTransferenciaService)this.defaultService).removeByIds(ids);
    }

    public Map findById(java.lang.Long id) {
    	
    	TypedFlatMap mapRet = new TypedFlatMap();
    	mapRet.putAll(AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap((Map)((AgendaTransferenciaService)this.defaultService).findByIdSpecific((Serializable)id)));
    	mapRet.put("devedorDocServFat.cliente.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(mapRet.getString("devedorDocServFat.cliente.pessoa.tpIdentificacao.value"),mapRet.getString("devedorDocServFat.cliente.pessoa.nrIdentificacao")));    	
    	mapRet.put("cliente.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(mapRet.getString("cliente.pessoa.tpIdentificacao.value"),mapRet.getString("cliente.pessoa.nrIdentificacao")));
    	
    	return mapRet;
    }
    
    public Integer getRowCount(TypedFlatMap map) {    	
    	Long idFilialOrigem = map.getLong("filialByIdFilialOrigem.idFilial");
    	Long idFilialDestino = map.getLong("filialByIdFilialDestino.idFilial"); 
    	Long idClienteOrigem = map.getLong("devedorDocServFat.cliente.idCliente");
    	Long idClienteDestino = map.getLong("cliente.idCliente");
    	String tpOrigem = map.getString("tpOrigem");
    	Long idMotivo = map.getLong("motivoTransferencia.idMotivoTransferencia"); 
    	Long idDevedorDocServFat = map.getLong("devedorDocServFat.idDevedorDocServFat");
    	String tpDocumentoServico = map.getString("devedorDocServFat.doctoServico.tpDocumentoServico");
    	Long idFilialOrigemDocumentoServico = map.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");    	
    	
    	return ((AgendaTransferenciaService)this.defaultService).getRowCount(idFilialOrigem, idFilialDestino, 
    			idClienteOrigem, idClienteDestino, tpOrigem, idMotivo, idDevedorDocServFat, tpDocumentoServico, idFilialOrigemDocumentoServico);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap map) {
    	
    	Long idFilialOrigem = map.getLong("filialByIdFilialOrigem.idFilial");
    	Long idFilialDestino = map.getLong("filialByIdFilialDestino.idFilial"); 
    	Long idClienteOrigem = map.getLong("devedorDocServFat.cliente.idCliente");
    	Long idClienteDestino = map.getLong("cliente.idCliente");
    	String tpOrigem = map.getString("tpOrigem");
    	Long idMotivo = map.getLong("motivoTransferencia.idMotivoTransferencia"); 
    	Long idDevedorDocServFat = map.getLong("devedorDocServFat.idDevedorDocServFat");
    	String tpDocumentoServico = map.getString("devedorDocServFat.doctoServico.tpDocumentoServico");
    	Long idFilialOrigemDocumentoServico = map.getLong("devedorDocServFat.doctoServico.filialByIdFilialOrigem.idFilial");
    	
    	
    	ResultSetPage rsp = ((AgendaTransferenciaService)this.defaultService).findPaginated(idFilialOrigem, idFilialDestino, 
    			idClienteOrigem, idClienteDestino, tpOrigem, idMotivo, idDevedorDocServFat, tpDocumentoServico, idFilialOrigemDocumentoServico, FindDefinition.createFindDefinition(map));
    	
    	List list = rsp.getList();
    	List listRet = new ArrayList();
    	
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		TypedFlatMap mapRet = new TypedFlatMap();    		
    		mapRet.putAll((Map)iter.next());  
    		mapRet.put("nrIdentificacaoClienteOrigem", FormatUtils.formatIdentificacao(mapRet.getDomainValue("tpIdentificacaoClienteOrigem").getValue(),mapRet.getString("nrIdentificacaoClienteOrigem")));
    		mapRet.put("nrIdentificacaoClienteDestino", FormatUtils.formatIdentificacao(mapRet.getDomainValue("tpIdentificacaoClienteDestino").getValue(),mapRet.getString("nrIdentificacaoClienteDestino")));    		
    		
    		mapRet.put("nrDocumentoServico", FormatUtils.formataNrDocumento(mapRet.getLong("nrDocumentoServico").toString(),mapRet.getDomainValue("tpDocumentoServico").getValue()));    		
    		listRet.add(mapRet);
    	}
    	
    	rsp.setList(listRet);
    	
    	return rsp;
    }    
	
	public List findComboDivisaoCliente(TypedFlatMap criteria){
		return this.divisaoClienteService.findByIdClienteMatriz(populateDivisaoClienteParam(criteria));
	}	
	
	public TypedFlatMap storeGeraAnalisePlanilha(TypedFlatMap criteria){
		return ((AgendaTransferenciaService)defaultService).storeGeraAnalisePlanilha(criteria);
	}
	
	
	/**
	 * Popula a DivisaoClienteparam para ser usado como filtro na busca por divisao
	 *
     * @param tfm
     * @return
	 * @author Hector Julian Esnaola Junior
	 * @since 24/01/2007
	 */
	public DivisaoClienteParam populateDivisaoClienteParam(TypedFlatMap tfm){
		
		DivisaoClienteParam dcp = new DivisaoClienteParam();
		
		dcp.setIdCliente(tfm.getLong("idCliente"));
		dcp.setIdDivisaoCliente(tfm.getLong("idDivisao"));
		dcp.setTpModal(tfm.getString("tpModal"));
		dcp.setTpAbrangencia(tfm.getString("tpAbrangencia"));
		dcp.setTpSituacao("A");
		
		return dcp;		
	}
	
	public void setService(AgendaTransferenciaService serviceService) {
		this.defaultService = serviceService;
	}

    public void removeById(java.lang.Long id) {
        ((AgendaTransferenciaService)defaultService).removeById(id);
    }    

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	
	public void setDevedorDocServFatLookUpService(
			DevedorDocServFatLookUpService devedorDocServFatLookUpService) {
		this.devedorDocServFatLookUpService = devedorDocServFatLookUpService;
	}  
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setDivisaoClienteService(
			DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setMotivoTransferenciaService(
			MotivoTransferenciaService motivoTransferenciaService) {
		this.motivoTransferenciaService = motivoTransferenciaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
}
