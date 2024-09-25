package com.mercurio.lms.sim.swt.action;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_ARMAZENAGEM;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_ESTADIA_VEICULO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CD_PALETIZACAO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.RECIBO_REEMBOLSO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.PreManifestoDocumentoService;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoServicoService;
import com.mercurio.lms.contasreceber.model.service.ConsultarDadosCobrancaDocumentoServicoService;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.Mir;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.entrega.model.service.ChequeReembolsoService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.MirService;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoCtoCooperadaService;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ImpostoServicoService;
import com.mercurio.lms.expedicao.model.service.ItemNfCtoService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalCtoCooperadaService;
import com.mercurio.lms.expedicao.model.service.ObservacaoDoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ParcelaCtoCooperadaService;
import com.mercurio.lms.expedicao.model.service.ParcelaDoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ServicoEmbalagemService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.EventoRimService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.ppd.model.PpdRecibo;
import com.mercurio.lms.ppd.model.PpdStatusRecibo;
import com.mercurio.lms.ppd.model.service.PpdReciboService;
import com.mercurio.lms.ppd.model.service.PpdStatusReciboService;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.rnc.report.EmitirRNCService;
import com.mercurio.lms.sim.model.service.ClienteUsuarioCCTService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.report.EmitirLocalizacaoMercadoriaService;
import com.mercurio.lms.sim.report.EmitirRelatorioDadosVolumesDocumentoService;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.InformacaoDoctoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sim.swt.consultarLocalizacoesMercadoriasAction"
 */

public class ConsultarLocalizacoesMercadoriasAction extends CrudAction {
    private EmpresaService empresaService;
    private TipoServicoService tipoServicoService;
    private FilialService filialService;
    private ClienteService clienteService;
    private ControleCargaService controleCargaService;
    private ManifestoColetaService manifestoColetaService;
    private DomainValueService domainValueService;
    private MirService mirService;
    private AwbService awbService;
    private PedidoColetaService pedidoColetaService;
    private ManifestoEntregaService manifestoEntregaService;
    private ReciboReembolsoService reciboReembolsoService;
    private ConhecimentoService conhecimentoService;
    private InformacaoDoctoClienteService informacaoDoctoClienteService;
    private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
    private DevedorDocServFatService devedorDocServFatService;
    private ServicoEmbalagemService servicoEmbalagemService;
    private ObservacaoDoctoServicoService observacaoDoctoServicoService;
    private ChequeReembolsoService chequeReembolsoService;
    private DadosComplementoService dadosComplementoService;
    private DoctoServicoService doctoServicoService;
    private AgendamentoEntregaService agendamentoEntregaService;
    private ParcelaDoctoServicoService parcelaDoctoServicoService;
    private ImpostoServicoService impostoServicoService;
    private ItemNfCtoService itemNfCtoService;
    private ManifestoViagemNacionalService manifestoViagemNacionalService;
    private EventoDocumentoServicoService eventoDocumentoServicoService;
    private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
    private ManifestoInternacionalService manifestoInternacionalService;
    private NaoConformidadeService naoConformidadeService;
    private CtoCtoCooperadaService ctoCtoCooperadaService;
    private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
    private ConsultarDadosCobrancaDocumentoServicoService consultarDadosCobrancaDocumentoServicoService;
    private NotaFiscalCtoCooperadaService notaFiscalCtoCooperadaService;
    private ParcelaCtoCooperadaService parcelaCtoCooperadaService;
    private EmitirLocalizacaoMercadoriaService emitirLocalizacaoMercadoriaService;
    private EmitirRelatorioDadosVolumesDocumentoService emitirRelatorioDadosVolumesDocumentoService;
    private ConfiguracoesFacade configuracoesFacade;
    private EmitirRNCService emitirRNCService;
    private PreManifestoDocumentoService preManifestoDocumentoService;
    private EnderecoPessoaService enderecoPessoaService;
    private ParcelaPrecoService parcelaPrecoService;
    private ReportExecutionManager reportExecutionManager;
    private VolumeNotaFiscalService volumeNotaFiscalService;
    private EventoVolumeService eventoVolumeService;
    private PpdReciboService ppdReciboService;
    private PpdStatusReciboService ppdStatusReciboService;
    private ProprietarioService proprietarioService;
    private EventoRimService eventoRimService;
    private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
    private ClienteUsuarioCCTService clienteUsuarioCCTService;
	
    public String executeExportacaoCsv(TypedFlatMap criteria) throws Exception {
    	TypedFlatMap novoTypedFlatMap = montaTypedFlatMapListagem(criteria);
    	return reportExecutionManager.generateReportLocator(doctoServicoService.executeExportacaoCsv(novoTypedFlatMap, reportExecutionManager.generateOutputDir()));
    }
    
	public List findPaginatedComplEmbalagens(TypedFlatMap criteria){
		return servicoEmbalagemService.findPaginatedComplEmbalagens(criteria.getLong("idDoctoServico"));
	}
	
	public List findPaginatedComplObservacoes(TypedFlatMap criteria){
		return observacaoDoctoServicoService.findPaginatedComplObservacoes(criteria.getLong("idDoctoServico"));
	}
	
	public Map findReembolsoByIdReembolsado(Long idDoctoServico){
		return reciboReembolsoService.findReembolsoByIdReembolsado(idDoctoServico);
	}
	
	public List findPaginatedChequesByIdReembolso(Map criteria){
		return chequeReembolsoService.findPaginatedChequesByIdReembolso((Long)criteria.get("idDoctoServico"));
	}
	
	public List findComboEmpresa(Map criteria){
		criteria = new HashMap();
		criteria.put("tpEmpresa", "M");
		List<Map> empresas =  empresaService.findComboEmpresa(criteria);
		if (empresas != null) {
			for(Map empresa : empresas){
				Map pessoa = (Map) empresa.remove("pessoa");
				empresa.put("nrIdentificacao", FormatUtils.formatIdentificacao(empresa));
				if (pessoa != null) {
					empresa.put("pessoaNmPessoa", pessoa.get("nmPessoa"));
				}
			}
		}
		return empresas;
	}
	
	public List findComboTipoServico(Map criteria){
		return tipoServicoService.find(criteria);
	}
	
	public List findLookupFilial(Map criteria) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Filial> filiais = filialService.findLookup(criteria);
		if (filiais != null) {
			for (Filial filial : filiais) {
				Map<String, Object> mapFilial = new HashMap<String, Object>();
				mapFilial.put("sgFilial", filial.getSgFilial());
				mapFilial.put("idFilial", filial.getIdFilial());
				mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
				result.add(mapFilial);
			}
		}
		return result;
	}
	
	public List findClienteCCTByUsuario(Map criteria){
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Usuario usuario = SessionUtils.getUsuarioLogado();
		criteria = clienteUsuarioCCTService.findClienteCCTByUsuario(usuario.getIdUsuario());
		if(criteria.containsKey("nrIdentificacao")){
			result = findLookupCliente(criteria) ;
		}
		return result;
	}
	
	public List findLookupCliente(Map criteria) {
		Map<String, Object> pessoa = new HashMap<String, Object>();
		pessoa.put("nrIdentificacao", criteria.remove("nrIdentificacao"));
		pessoa.put("nmFantasia", criteria.remove("nmFantasia"));
		criteria.put("pessoa", pessoa);
	    List<Cliente> clientes = clienteService.findLookup(criteria);
		if (clientes != null && !clientes.isEmpty()) {
			List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
			for(Cliente cliente : clientes) {
				Map<String, Object> mapCliente = new HashMap<String, Object>();
				mapCliente.put("idCliente",cliente.getIdCliente());
				mapCliente.put("nmPessoa", cliente.getPessoa().getNmPessoa());
				// já vem formatado do clienteService.
				mapCliente.put("nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
				mapCliente.put("nmFantasia", cliente.getPessoa().getNmFantasia());
				mapCliente.put("nrConta", cliente.getNrConta());
				result.add(mapCliente);
			}
			return result;
		}
		return null;
	}

	
	public List findLookupControleCarga(TypedFlatMap criteria){
		criteria.put("filialByIdFilialOrigem.idFilial", criteria.get("idFilial"));
		List lista = controleCargaService.findLookup(criteria);
		List listaNova = null;
		if(!lista.isEmpty() && lista.size()==1){
			listaNova = new ArrayList();
			Map map = new HashMap();
			ControleCarga controleCarga = (ControleCarga)lista.get(0);
			map.put("idControleCarga", controleCarga.getIdControleCarga());
			map.put("nrControleCarga", controleCarga.getNrControleCarga());
			listaNova.add(map);
		}
		
		return listaNova;
	}
	
	public List findLookupManifestoColeta(TypedFlatMap criteria){
		criteria.put("filial.idFilial", criteria.get("idFilial"));
		List lista =  manifestoColetaService.findLookup(criteria);
		List listaNova = null;
		if(!lista.isEmpty() && lista.size()==1){
			listaNova = new ArrayList();
			Map map = new HashMap();
			ManifestoColeta manifestoColeta = (ManifestoColeta)lista.get(0);
			map.put("idManifestoColeta", manifestoColeta.getIdManifestoColeta());
			map.put("nrManifesto", manifestoColeta.getNrManifesto());
			listaNova.add(map);
		}
		
		return listaNova;
	}
	
	public List findLookupManifestoEntrega(TypedFlatMap criteria){
		if (criteria.get("idFilial") != null) {
			criteria.put("filial.idFilial", criteria.get("idFilial"));
		}
		if (criteria.get("idControleCarga") != null) {
			criteria.put("controleCarga.idControleCarga", criteria.get("idControleCarga"));
		}
		
		List manifestos = manifestoEntregaService.findLookup(criteria);
		if (!manifestos.isEmpty() && manifestos.size() == 1){
			List<Map> result = new ArrayList<Map>();
			Map mapManifesto = new HashMap();
			ManifestoEntrega manifestoEntrega = (ManifestoEntrega)manifestos.get(0);
			mapManifesto.put("idManifestoEntrega",manifestoEntrega.getIdManifestoEntrega() );
			mapManifesto.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
			
			result.add(mapManifesto);
			return result;
		}
		return null;
	}
	
	public List findLookupManifestoEntregaCustom(TypedFlatMap criteria) {
		if (criteria.get("idFilial") != null) {
			criteria.put("filial.idFilial", criteria.get("idFilial"));
		}
		if (criteria.get("idControleCarga") != null) {
			criteria.put("controleCarga.idControleCarga", criteria.get("idControleCarga"));
		}
		
		List manifestos = manifestoEntregaService.find(criteria);
		if (!manifestos.isEmpty() && manifestos.size() == 1){
			List<Map> result = new ArrayList<Map>();
			Map mapManifesto = new HashMap();
			ManifestoEntrega manifestoEntrega = (ManifestoEntrega)manifestos.get(0);
			mapManifesto.put("idManifestoEntrega",manifestoEntrega.getIdManifestoEntrega() );
			mapManifesto.put("nrManifestoEntrega", manifestoEntrega.getNrManifestoEntrega());
			mapManifesto.put("manifestoEntrega", manifestoEntrega);
			mapManifesto.put("manifesto", manifestoEntrega.getManifesto());
			mapManifesto.put("controleCarga", manifestoEntrega.getManifesto().getControleCarga());
			
			ControleCarga cc = controleCargaService.findById(manifestoEntrega.getManifesto().getControleCarga().getIdControleCarga());
			
			mapManifesto.put("proprietario", cc.getProprietario());
			mapManifesto.put("filialOrigem", cc.getFilialByIdFilialOrigem());
			
			cc.getProprietario().getPessoa().setNrIdentificacao(FormatUtils.formatIdentificacao(cc.getProprietario().getPessoa()));
			
			mapManifesto.put("pessoa", cc.getProprietario().getPessoa());
			mapManifesto.put("meioTransporte", cc.getMeioTransporteByIdTransportado());
			result.add(mapManifesto);
			return result;
		}
		return null;
		   
	}
	
	/**
     * Método usado para definir os tipos de manifestos usados
     * @param criteria
     * @return
     */
    public List findTipoManifesto(Map criteria) {
        List dominiosValidos = new ArrayList();
        dominiosValidos.add("VN");
        List retorno = domainValueService.findByDomainNameAndValues("DM_TAG_MANIFESTO", dominiosValidos);
        return retorno;
    }
    
    public List findLookupMir(TypedFlatMap criteria){
    	criteria.put("filialByIdFilialOrigem.idFilial", criteria.get("idFilial"));
		List lista = mirService.findLookup(criteria);
		List listaNova = null;
		if(!lista.isEmpty() && lista.size()==1){
			listaNova = new ArrayList();
			Map map = new HashMap();
			Mir mir = (Mir)lista.get(0);
			
			DomainValue tpDocumentoMir = (DomainValue)mir.getTpDocumentoMir();
			
			if(tpDocumentoMir!= null)
				map.put("tpDocumentoMir", tpDocumentoMir.getValue());
			
			map.put("idMir",mir.getIdMir());
			map.put("nrMir", mir.getNrMir());
			listaNova.add(map);
		}
		
		return listaNova;
	}
    
    public List findLookupAwb(TypedFlatMap criteria){
		List lista = awbService.findLookup(criteria);
		List listaNova = null;
		if(!lista.isEmpty() && lista.size()==1){
			listaNova = new ArrayList();
			Map map = new HashMap();
			Awb awb = (Awb)lista.get(0);
			map.put("idAwb",awb.getIdAwb());
			map.put("nrAwb", awb.getNrAwb());
			listaNova.add(map);
		}
		
		return listaNova;
	}
    
    public List findTipoDocumentoServico(Map criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("CTR");
    	dominiosValidos.add("CRT");
    	dominiosValidos.add("NFT");
    	dominiosValidos.add("MDA");
    	dominiosValidos.add("CTE");
    	dominiosValidos.add("NTE");
        return domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
  }
    
	public List findLookupPedidoColeta(Map criteria) {
		//monta os criterias aninhados
		Map idFilial = new HashMap();
		idFilial.put("idFilial", criteria.get("idFilial"));
		criteria.put("filialByIdFilialResponsavel",idFilial);
		criteria.remove("idFilial");
		
	    List listPedidosColeta = pedidoColetaService.findLookup(criteria);
	    
	    if(!listPedidosColeta.isEmpty() && listPedidosColeta.size()==1){
	    	PedidoColeta pedidocoleta = (PedidoColeta)listPedidosColeta.get(0);
	    	Map map = new HashMap();
	    	map.put("idPedidoColeta", pedidocoleta.getIdPedidoColeta());
	    	map.put("nrColeta", pedidocoleta.getNrColeta());
	    	listPedidosColeta.add(map);
	    	listPedidosColeta.remove(pedidocoleta);
	    }
	    return listPedidosColeta;
	  
	}
	
     //manifesto de viagem
    public List findLookupManifestoDocumentFilialVN(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }
	 	
    public List findLookupManifestoDocumentFilialVI(Map criteria) {
    	return findLookupFilialByManifesto(criteria);
    }
	    
    public List findLookupFilialByManifesto(Map criteria) {
		List list = filialService.findLookup(criteria);
		List retorno = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Filial filial = (Filial) iter.next();
			TypedFlatMap typedFlatMap = new TypedFlatMap();
			typedFlatMap.put("idFilial", filial.getIdFilial());
			typedFlatMap.put("sgFilial", filial.getSgFilial());
			typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
			retorno.add(typedFlatMap);
		}
		return retorno;
	}
	    
    public List findLookupManifestoDocumentNumberVN(Map criteria) {
    	if(criteria.get("idFilialOrigem")!= null){
    		Map mapFilial = new HashMap();
    		mapFilial.put("idFilial",criteria.get("idFilialOrigem"));
    		criteria.put("filial",mapFilial);
    	}
    	List list = manifestoViagemNacionalService.findLookup(criteria);
    	
    	return list;
    }

    public List findLookupManifestoDocumentNumberVI(Map criteria) {
    	List list = manifestoInternacionalService.findLookup(criteria);
    	return list;
    }
    
    public List findLookupDoctoServico(TypedFlatMap criteria) {
    	String tpDocumentoServico = (String) criteria.get("tpDocumentoServico");
    	if (!RECIBO_REEMBOLSO.equals(tpDocumentoServico)) {
			List<TypedFlatMap> doctos = doctoServicoService.findLookupDoctoServico(montaTypedFlatMapDoctoServico(criteria));
	    	if (doctos != null) {
	    		doctos = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(doctos);
	    		List<Map> result = new ArrayList<Map>();
	    		for (TypedFlatMap docto : doctos) {
					result.add(convertMapLocalizacaoByDoctoServico(docto));
				}
	    		return result;
	    	}
    	} else {
    		List<TypedFlatMap> doctos = doctoServicoService.findLookupCustomReemb(criteria);
	    	if (doctos != null && !doctos.isEmpty()) {
	    		doctos = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(doctos);
	    		TypedFlatMap docto = doctos.get(0);
	    		List<Long> reembolsados = reciboReembolsoService.findReembolsadoByIdReembolso(docto.getLong("doctoServico.idDoctoServico"));
	    		if (reembolsados != null && !reembolsados.isEmpty()) {
	    			docto.put("idDoctoServico", reembolsados.get(0));
	    		}
	    		return doctos;
	    	}
    	}
    	return null;
    }
	 
	public Map findEmpresaUsuarioLogado(){
		Map map = new HashMap();
		map.put("dtInicial", JTDateTimeUtils.getDataAtual().minusDays(15));
		map.put("dtFinal", JTDateTimeUtils.getDataAtual());
		return map;
	}
	 
	 public List<Map<String, Object>> findComboDoctoCliente(TypedFlatMap criteria){
		 criteria.put("cliente.idCliente", criteria.remove("idCliente"));
		 List<InformacaoDoctoCliente> informacoes = informacaoDoctoClienteService.find(criteria);
		 if (informacoes != null) {
			 List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			 for (InformacaoDoctoCliente doctoCliente : informacoes) {
				 Map<String, Object> map = new HashMap<String, Object>();
				 map.put("idInformacaoDoctoCliente", doctoCliente.getIdInformacaoDoctoCliente());
				 map.put("dsCampo", doctoCliente.getDsCampo());
				 map.put("tpCampo", doctoCliente.getTpCampo().getValue());
				 map.put("dsFormatacao", doctoCliente.getDsFormatacao());
				 map.put("nrTamanho", doctoCliente.getNrTamanho());
				 map.put("blOpcional", doctoCliente.getBlOpcional());
				 map.put("dsValorPadrao", doctoCliente.getDsValorPadrao());
				 map.put("blValorFixo", doctoCliente.getBlValorFixo());
				 result.add(map);
			 }
			 
			 return result;
		 }
		 return null;
	 }
	
	public TypedFlatMap montaTypedFlatMapListagem(TypedFlatMap criteria){
		
		//foi implementado desta forma pois o metodo do DAO recebe como string
		if(criteria.get("modal")== null)
			criteria.put("modal","");
		
		if(criteria.get("abrangencia")== null)
			criteria.put("abrangencia","");
		
		if(criteria.get("tpDocumentoMir")== null)
			criteria.put("tpDocumentoMir","");
				
		if(criteria.get("idEmpresa")!= null)
			criteria.put("empresa.idEmpresa",criteria.getLong("idEmpresa"));
		
		if(criteria.get("idTipoServico")!= null)
			criteria.put("tipoServico.idTipoServico",criteria.getLong("idTipoServico"));
		
		if(criteria.get("idFilialOrigem")!= null)
			criteria.put("filialOrigem.idFilial",criteria.getLong("idFilialOrigem"));
		
		if(criteria.get("idFilialDestino")!= null)
			criteria.put("filialDestino.idFilial",criteria.getLong("idFilialDestino"));
		
		if(criteria.get("idFilialCotacao")!= null)
			criteria.put("filial.idFilial",criteria.getLong("idFilialCotacao"));
		
		if(criteria.get("nrCotacaoFilialCotacao")!= null)
			criteria.put("nrCotacao",criteria.getInteger("nrCotacaoFilialCotacao"));
		
		if(criteria.get("idPedidoColeta")!= null)
			criteria.put("pedidoColeta.idPedidoColeta",criteria.getLong("idPedidoColeta"));
		
		if(criteria.get("nfCliente")!= null)
			criteria.put("nfCliente",criteria.getInteger("nfCliente"));
		
		if(criteria.get("periodoInicial")!= null)
			criteria.put("periodoInicial",criteria.getYearMonthDay("periodoInicial"));
		
		if(criteria.get("periodoFinal")!= null)
			criteria.put("periodoFinal",criteria.getYearMonthDay("periodoFinal"));
		
		if(criteria.get("idClienteRemetente")!= null)
			criteria.put("remetente.idCliente",criteria.getLong("idClienteRemetente"));
		
		if(criteria.get("idInformacaoDoctoCliente")!= null)
			criteria.put("informacaoDoctoCliente.idInformacaoDoctoCliente",criteria.getLong("idInformacaoDoctoCliente"));
		
		if(criteria.get("nrDocumentoCliente")!= null)
			criteria.put("nrDocumentoCliente", "" + criteria.get("nrDocumentoCliente"));
		else
			criteria.put("nrDocumentoCliente","");
		
		if(criteria.get("idClienteDestinatario")!= null)
			criteria.put("destinatario.idCliente",criteria.getLong("idClienteDestinatario"));
		
		if(criteria.get("idClienteConsignatario")!= null)
			criteria.put("consignatario.idCliente",criteria.getLong("idClienteConsignatario"));
		
		if(criteria.get("idClienteRedespacho")!= null)
			criteria.put("redespacho.idCliente",criteria.getLong("idClienteRedespacho"));
		
		if(criteria.get("idClienteResponsavelFrete")!= null)
			criteria.put("responsavelFrete.idCliente",criteria.getLong("idClienteResponsavelFrete"));
		
		if(criteria.get("idControleCarga")!= null)
			criteria.put("controleCarga.idControleCarga",criteria.getLong("idControleCarga"));
		
		if(criteria.get("idManifestoColeta")!= null)
			criteria.put("manifestoColeta.idManifestoColeta",criteria.getLong("idManifestoColeta"));
		
		if(criteria.getLong("idManifestoViagemNacional")!= null)
			criteria.put("manifesto.idManifesto",criteria.getLong("idManifestoViagemNacional"));
		
		if(criteria.get("idManifestoEntrega")!= null)
			criteria.put("manifestoEntrega.idManifestoEntrega",criteria.getLong("idManifestoEntrega"));
		
		if(criteria.get("idMir")!= null)
			criteria.put("mir.idMir",criteria.getLong("idMir"));
		
		if(criteria.get("idAwb")!= null)
			criteria.put("awb.idAwb",criteria.getLong("idAwb"));
		
		// LMS 2782
		if(criteria.get("volumeColeta") != null)
			criteria.put("volumeColeta", criteria.get("volumeColeta"));
		
		return criteria;
	}
	
	public ResultSetPage findPaginatedConsultaLocalizacaoMercadoria(TypedFlatMap criteria) {
		TypedFlatMap novoTypedFlatMap = montaTypedFlatMapListagem(criteria);
		ResultSetPage rs = doctoServicoService.findPaginatedConsultaLocalizacaoMercadoria(novoTypedFlatMap);
		for (Object item : rs.getList()) {
			if (item instanceof Map) {
				String cor = null;
				if (((Map)item).get("corBola") != null)
					cor = ((Map)item).get("corBola").toString();
				if (cor != null && cor.length() > 0) {
					((Map)item).put("corBola", "/image/bola_" + cor.toLowerCase() + ".gif"); 
				}
			}
		}
		return rs;
	}
	
	
	public Integer getRowCountConsultaLocalizacaoMercadoria(TypedFlatMap criteria) {
		TypedFlatMap novoTypedFlatMap = montaTypedFlatMapListagem(criteria);
		if(criteria.getLong("idDoctoServicoReembolsado")!= null) {
			criteria.put("idDoctoServico",criteria.getLong("idDoctoServicoReembolsado"));
		}
		return doctoServicoService.getRowCountConsultaLocalizacaoMercadoria(novoTypedFlatMap);
	}
	
	public Map findByIdDetalhamento(TypedFlatMap criteria){
		Map map = (Map)doctoServicoService.findByIdDSByLocalizacaoMercadoria(criteria);
		
		List<PpdRecibo> recibos = ppdReciboService.findByIdConhecimento(criteria.getLong("idDoctoServico"));
		Long idRecibo = null;
		if(recibos != null && recibos.size() > 0) {
			idRecibo = recibos.get(0).getIdRecibo();
		}
		map.put("idReciboPpd", idRecibo);
		
		DoctoServicoIndenizacao doctoServicoIndenizacao = doctoServicoIndenizacaoService.findByIdDoctoServicoParaLocalizacaoMercadoria(criteria.getLong("idDoctoServico"));
		
		Long idDoctoServicoIndenizacao = null;
		Long idReciboIndenizacao = null;
		if (doctoServicoIndenizacao != null) {
			idDoctoServicoIndenizacao =  doctoServicoIndenizacao.getIdDoctoServicoIndenizacao();
			idReciboIndenizacao =  doctoServicoIndenizacao.getReciboIndenizacao().getIdReciboIndenizacao();
		}
		map.put("idDoctoServicoIndenizacao", idDoctoServicoIndenizacao);
		map.put("idReciboIndenizacao", idReciboIndenizacao);
		
	   	return  map;
	}
	
	public Map findByIdDetailAbaPrincipal(TypedFlatMap criteria){
		Map map = (Map)doctoServicoService.findByIdDSByLocalizacaoMercadoriaPrincipal(criteria);
		map.put("nrIdentificacaoRem", FormatUtils.formatIdentificacao((DomainValue)map.get("tpIdentificacaoRem"),(String)map.get("nrIdentificacaoRem")));
		map.put("nrIdentificacaoDest",FormatUtils.formatIdentificacao((DomainValue)map.get("tpIdentificacaoDest"),(String)map.get("nrIdentificacaoDest")));
		YearMonthDay dtBaixa = null;
		if(map.get("dhBaixa")!= null){
			dtBaixa = ((DateTime)map.get("dhBaixa")).toYearMonthDay();
			map.put("dhBaixa", JTFormatUtils.format((DateTime)map.get("dhBaixa")));
		}	
		Integer qtdediasUteis = doctoServicoService.findQtdeDiasUteisEntregaDocto(criteria.getLong("idDoctoServico"),dtBaixa);
		if(qtdediasUteis!= null){
			map.put("qtdediasUteis",qtdediasUteis);
		}	
		
		String progrDtTurno= "";
		if(map.get("dtAgendamento")!= null){
			YearMonthDay data = (YearMonthDay)map.get("dtAgendamento");
			progrDtTurno = JTFormatUtils.format(data);
			if(map.get("dsTurno")!= null)
				progrDtTurno = progrDtTurno+" - "+map.get("dsTurno").toString();
		}else if(map.get("dsTurno")!= null)
			progrDtTurno = map.get("dsTurno").toString();
		
		String progrHorario= "";
		if(map.get("hrPreferenciaInicial")!= null){
			TimeOfDay hrPreferenciaInicial = (TimeOfDay)map.get("hrPreferenciaInicial");
			progrHorario = JTFormatUtils.format(hrPreferenciaInicial);
			if(map.get("hrPreferenciaFinal")!= null){
				TimeOfDay hrPreferenciaFinal = (TimeOfDay)map.get("hrPreferenciaFinal");
				progrHorario = progrHorario+" às "+JTFormatUtils.format(hrPreferenciaFinal);
			}	
		}else if(map.get("hrPreferenciaFinal")!= null){
			TimeOfDay hrPreferenciaFinal = (TimeOfDay)map.get("hrPreferenciaFinal");
			progrHorario = JTFormatUtils.format(hrPreferenciaFinal);
		}
		if (StringUtils.isNotEmpty(progrHorario)) {
			map.put("programacao", progrDtTurno +" "+ progrHorario);
		} else { 
			map.put("programacao", progrDtTurno);
		}
		
		if(map.get("psAferido") == null){
			map.put("psAferido", FormatUtils.formatDecimal("#,###,###,###,##0.000", BigDecimal.ZERO));
		}
		
		return  map;
	}
	
	public List findNotasFiscaisByConhecimento(TypedFlatMap criteria){
		List lista = notaFiscalConhecimentoService.findNFByIdDoctoServico(criteria.getLong("idDoctoServico"));
		for(Iterator iter = lista.iterator(); iter.hasNext();){
			Map map = (Map)iter.next();
			map.put("popup","/image/popup.gif");
		}
		return lista;
	}
	
	public List findPaginatedIntegrantes(TypedFlatMap criteria) {
		List<Map<String, Object>> doctoServicos =  doctoServicoService.findPaginatedIntegrantes(criteria.getLong("idDoctoServico"));
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Long idRegistro = LongUtils.ZERO;
		if (doctoServicos != null && !doctoServicos.isEmpty()){
			Map clientes = doctoServicos.get(0);
			if (clientes.get("nmPessoaRem") != null){
				Map<String, Object> registro = new HashMap<String, Object>();
				idRegistro = LongUtils.incrementValue(idRegistro);
				registro.put("tipoCliente", configuracoesFacade.getMensagem("remetente"));
				registro.put("nmPessoa", clientes.get("nmPessoaRem"));
				registro.put("municipio", clientes.get("municipioRem"));
				registro.put("idCliente", clientes.get("idClienteRem"));
				registro.put("idRegistro", idRegistro);
				registro.put("popup","/image/popup.gif");
				registro.put("tpIdentificacao", clientes.get("tpIdentificacaoRem"));
				registro.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)clientes.get("tpIdentificacaoRem"),clientes.get("nrIdentificacaoRem").toString()));
				result.add(registro);
			}
			if(clientes.get("nmPessoaDest") != null){
				Map<String, Object> map = new HashMap<String, Object>();
				idRegistro = LongUtils.incrementValue(idRegistro);
				map.put("tipoCliente", configuracoesFacade.getMensagem("destinatario"));
				map.put("tpIdentificacao", clientes.get("tpIdentificacaoDest"));
				if(clientes.get("nrIdentificacaoDest") != null){
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)clientes.get("tpIdentificacaoDest"), clientes.get("nrIdentificacaoDest").toString()));
				}
				map.put("nmPessoa", clientes.get("nmPessoaDest"));
				map.put("municipio", clientes.get("municipioDest"));
				map.put("idCliente", clientes.get("idClienteDest"));
				map.put("idRegistro", idRegistro);
				map.put("popup","/image/popup.gif");
				result.add(map);
			}
			if (clientes.get("nmPessoaCons") != null){
				Map<String, Object> map = new HashMap<String, Object>();
				idRegistro = LongUtils.incrementValue(idRegistro);
				map.put("tipoCliente", configuracoesFacade.getMensagem("consignatario"));
				map.put("tpIdentificacao", clientes.get("tpIdentificacaoCons"));
				if(clientes.get("nrIdentificacaoCons") != null){
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)clientes.get("tpIdentificacaoCons"), clientes.get("nrIdentificacaoCons").toString()));
				}
				map.put("nmPessoa", clientes.get("nmPessoaCons"));
				map.put("municipio", clientes.get("municipioCons"));
				map.put("idCliente", clientes.get("idClienteCons"));
				map.put("idRegistro", idRegistro);
				map.put("popup","/image/popup.gif");
				result.add(map);
			}
			if (clientes.get("nmPessoaRedes") != null){
				Map map = new HashMap();
				idRegistro = idRegistro + Long.valueOf(1);
				map.put("tipoCliente", configuracoesFacade.getMensagem("redespacho"));
				map.put("tpIdentificacao", clientes.get("tpIdentificacaoRedes"));
				if(clientes.get("nrIdentificacaoRedes") != null) {
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)clientes.get("tpIdentificacaoRedes"), clientes.get("nrIdentificacaoRedes").toString()));
				}
				map.put("nmPessoa", clientes.get("nmPessoaRedes"));
				map.put("municipio", clientes.get("municipioRedes"));
				map.put("idCliente", clientes.get("idClienteRedes"));
				map.put("idRegistro", idRegistro);
				map.put("popup","/image/popup.gif");
				result.add(map);
			}
			
		}
		List<Map> devedores = devedorDocServFatService.findDevedoresByIdDoctoServico(criteria.getLong("idDoctoServico"));
		if(devedores != null) {
			for (Map devedor : devedores) {
				idRegistro = LongUtils.incrementValue(idRegistro);
				devedor.put("nrIdentificacao",FormatUtils.formatIdentificacao((DomainValue)devedor.get("tpIdentificacao"),devedor.get("nrIdentificacao").toString()));
				devedor.put("idRegistro", idRegistro);
				devedor.put("tipoCliente", configuracoesFacade.getMensagem("responsavelFrete"));
				devedor.put("popup","/image/popup.gif");
				result.add(devedor);
			}
		}
		return result;
		
	}
	
	public List findPaginatedDadosCompl(TypedFlatMap criteria){
		return dadosComplementoService.findPaginatedDadosCompl(criteria.getLong("idDoctoServico"));
	}
	
	public List findPaginatedComplNF(TypedFlatMap criteria){
		return notaFiscalConhecimentoService.findPaginatedComplNF(criteria.getLong("idDoctoServico"));
	}
	
	
	public List findPaginatedRastreamento(TypedFlatMap criteria){
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		List lista = new ArrayList();
		List listaMercManEnt = reciboReembolsoService.findRastreamentoME(idDoctoServico);
		if(!listaMercManEnt.isEmpty()){
			Map map = (Map)listaMercManEnt.get(0);
			lista.add(map);
		}
		List listaReemDest = reciboReembolsoService.findRastreamentoReembDest(idDoctoServico);
		if(!listaReemDest.isEmpty()){
			Map map = (Map)listaReemDest.get(0);
			lista.add(map);
		}
		List listaMirEntAdm = reciboReembolsoService.findRastreamentoMirEntregaAdm(idDoctoServico);
		if(!listaMirEntAdm.isEmpty()){
			Map map = (Map)listaMirEntAdm.get(0);
			lista.add(map);
			if(map.get("dhRecebimento")!= null){
				Map mapMirRecebida = new HashMap();
				mapMirRecebida.put("evento","Recebimento da MIR no administrativo");
				mapMirRecebida.put("sgFilial",map.get("sgFilial"));
				mapMirRecebida.put("nrDocto", map.get("nrDocto"));
				mapMirRecebida.put("dhEnvio", map.get("dhEnvio"));
				mapMirRecebida.put("origem", map.get("origem"));
				mapMirRecebida.put("dhRecebimento", map.get("dhRecebimento"));
				mapMirRecebida.put("destino", map.get("destino"));
				map.put("dhRecebimento","");
				lista.add(mapMirRecebida);
			}
		}
		List listaMirDestOr = reciboReembolsoService.findRastreamentoMirDestOri(idDoctoServico);
		if(!listaMirDestOr.isEmpty()){
			Map map = (Map)listaMirDestOr.get(0);
			lista.add(map);
			if(map.get("dhRecebimento")!= null){
				Map mapMirDestOr = new HashMap();
				mapMirDestOr.put("evento","Recebimento da MIR na origem");
				mapMirDestOr.put("sgFilial",map.get("sgFilial"));
				mapMirDestOr.put("nrDocto", map.get("nrDocto"));
				mapMirDestOr.put("dhEnvio", map.get("dhEnvio"));
				mapMirDestOr.put("origem", map.get("origem"));
				mapMirDestOr.put("dhRecebimento", map.get("dhRecebimento"));
				mapMirDestOr.put("destino", map.get("destino"));
				lista.add(mapMirDestOr);
			}
			
		}
		List listaMirEnt = reciboReembolsoService.findRastreamentoMirEnt(idDoctoServico);
		if(!listaMirEnt.isEmpty()){
			Map map = (Map)listaMirEnt.get(0);
			lista.add(map);
			if(map.get("dhRecebimento")!= null){
				Map mapMirEnt = new HashMap();
				mapMirEnt.put("evento","Recebimento da MIR na origem");
				mapMirEnt.put("sgFilial",map.get("sgFilial"));
				mapMirEnt.put("nrDocto", map.get("nrDocto"));
				mapMirEnt.put("dhEnvio", map.get("dhEnvio"));
				mapMirEnt.put("origem", map.get("origem"));
				mapMirEnt.put("dhRecebimento", map.get("dhRecebimento"));
				mapMirEnt.put("destino", map.get("destino"));
				lista.add(mapMirEnt);
			}
			
		}
		return lista;
		
	}
	
	public Map findAgendamentosByDoctoServico(TypedFlatMap criteria){
		Map map = null;
		List lista = agendamentoEntregaService.findAgendamentosByDoctoServico(criteria.getLong("idDoctoServico"));
		if(!lista.isEmpty()){
			map =(Map) lista.get(0);
			
			DomainValue tipoAgend = (DomainValue) map.get("tpAgendamento");
			map.put("tpAgendamento", tipoAgend.getDescription().toString());
			
			DomainValue situAgend = (DomainValue) map.get("tpSituacaoAgendamento");
			map.put("tpSituacaoAgendamento", situAgend.getDescription().toString());
			
			if(map.get("nrDdd")!= null)
				map.put("nrTelefone","("+map.get("nrDdd")+")"+" "+map.get("nrTelefone"));
			
			if(map.get("dhContato")!= null)
				map.put("dhContato",JTFormatUtils.format((DateTime)map.get("dhContato")));
			
			if(map.get("dhCancelamento")!= null)
				map.put("dhCancelamento",JTFormatUtils.format((DateTime)map.get("dhCancelamento")));
			
					
		}
		return map;
	}
	
	public Map findAgendamentoByIdAgendamento(Long idAgendamento){
		Map map = null;
		List lista = agendamentoEntregaService.findAgendamentoByIdAgendamento(idAgendamento);
		if(!lista.isEmpty()){
			map = (Map)lista.get(0);
			DomainValue tipoAgend = (DomainValue) map.get("tpAgendamento");
			map.put("tpAgendamento", tipoAgend.getDescription());
			DomainValue situAgend = (DomainValue) map.get("tpSituacaoAgendamento");
			map.put("tpSituacaoAgendamento", situAgend.getDescription());
			if(map.get("nrDdd")!= null)
				map.put("nrTelefone","("+map.get("nrDdd")+")"+" "+map.get("nrTelefone"));
			if(map.get("dhContato")!= null)
				map.put("dhContato",JTFormatUtils.format((DateTime)map.get("dhContato")));
				
		}	
		return map;
    }
	
	public ResultSetPage findPaginatedAgendamentosByDoctoServico(TypedFlatMap criteria){
		return agendamentoEntregaService.findPaginatedAgendamentosByDoctoServico(criteria);
		
	}
	
	public Integer getRowCountAgendamentosByDoctoServico(TypedFlatMap criteria){
		return agendamentoEntregaService.getRowCountAgendamentosByDoctoServico(criteria);
		
	}
	
	public List findPaginatedParcelaPreco(TypedFlatMap criteria){
		List lista = parcelaDoctoServicoService.findPaginatedParcelasPreco(criteria.getLong("idDoctoServico"));
		for(Iterator iter = lista.iterator();iter.hasNext();){
			Map map = (HashMap)iter.next();
			
			BigDecimal vlTotalParcelas = (BigDecimal)map.get("vlTotalParcelas");
			if(vlTotalParcelas.intValue()> 0){
				BigDecimal vlParcela = (BigDecimal)map.get("vlParcela");
				BigDecimal analise = vlParcela.divide(vlTotalParcelas, 4, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100));
				map.put("analise",analise);
			}
			
			String vlTotalParcelas2 = map.get("dsSimbolo") +" "+FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("vlTotalParcelas"));
			map.put("vlTotalParcelas",vlTotalParcelas2);
		}
		if(lista!= null && !lista.isEmpty()){
			Map map = (Map)lista.get(0);
			if(map.get("vlDesconto")!= null){
				Map mapDesconto = new HashMap();
				mapDesconto.put("nmParcelaPreco",configuracoesFacade.getMensagem("desconto"));
				mapDesconto.put("vlParcela",map.get("vlDesconto"));
				mapDesconto.put("analise","");
				mapDesconto.put("dsSimbolo",map.get("dsSimbolo"));
				mapDesconto.put("sgMoeda",map.get("sgMoeda"));
				lista.add(mapDesconto);
			}
		}	
			
		return lista;
		
	}
	
	public List findPaginatedCalculoServico(TypedFlatMap criteria){
		List lista = parcelaDoctoServicoService.findPaginatedCalculoServico(criteria.getLong("idDoctoServico"));
		for(Iterator iter = lista.iterator();iter.hasNext();){
			Map map = (Map)iter.next();
			BigDecimal vlTotalServicos = (BigDecimal)map.get("vlTotalServicos");
			if(vlTotalServicos.intValue()> 0){
				BigDecimal vlParcela = (BigDecimal)map.get("vlParcela");
				BigDecimal analise = vlParcela.divide(vlTotalServicos, 4, RoundingMode.HALF_DOWN).multiply(new BigDecimal(100));
				map.put("analise",analise);
			}
		}
		return lista;
		
	}
	
	public Map findTotaisCalculoServico(Long idDoctoServico){
		Map map = doctoServicoService.findTotaisCalculoServico(idDoctoServico);
		if(map != null){
			if(map.get("vlTotalServicos")!= null){
				String vlTotalServicos = map.get("dsSimbolo") +" "+FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("vlTotalServicos"));
				map.put("vlTotalServicos",vlTotalServicos);
			}
			if(map.get("vlTotalDocServico") != null){
				String vlTotalCTRC = map.get("dsSimbolo") +" "+FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("vlTotalDocServico"));
				map.put("vlTotalDocServico",vlTotalCTRC);
			}
			if(map.get("vlICMSST") != null){
				String vlICMSST = map.get("dsSimbolo") +" "+FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("vlICMSST"));
				map.put("vlICMSST",vlICMSST);
			}
			if(map.get("vlLiquido") != null){
				String vlLiquido = map.get("dsSimbolo") +" "+FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("vlLiquido"));
				map.put("vlLiquido",vlLiquido);
			}
			if(map.get("vlTotalParcelas")!= null){
				String vlTotalParcelas = map.get("dsSimbolo") +" "+FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("vlTotalParcelas"));
				map.put("vlTotalParcelas",vlTotalParcelas);
			}	
		}
		return map;
	}
	
	public Map findTipoTributacaoIcms(Long idDoctoServico){
		Map map = (Map)impostoServicoService.findTipoTributacaoIcms(idDoctoServico).get(0);
	    return  map;
	}
	
	public List findPaginatedImpostos(TypedFlatMap criteria){
		List lista = impostoServicoService.findPaginatedImpostos(criteria.getLong("idDoctoServico"));
		Map mapIcmsDoctoServico = impostoServicoService.findIcmsDoctoServico(criteria.getLong("idDoctoServico"));
		if(mapIcmsDoctoServico != null){
			if(mapIcmsDoctoServico.get("vlImposto")!= null){
				Map mapImposto = new HashMap();
				DomainValue dv = new DomainValue();
				dv.setValue("ICMS");
				dv.setDescription( new VarcharI18n("ICMS"));
		    	mapImposto.put("tpImposto",dv);
				mapImposto.put("vlBaseCalculo",mapIcmsDoctoServico.get("vlBaseCalcImposto"));
				mapImposto.put("pcAliquota",mapIcmsDoctoServico.get("pcAliquotaIcms"));
				mapImposto.put("vlImpostoServico",mapIcmsDoctoServico.get("vlImposto"));
				mapImposto.put("dsSimbolo",mapIcmsDoctoServico.get("dsSimbolo"));
				mapImposto.put("sgMoeda",mapIcmsDoctoServico.get("sgMoeda"));
				lista.add(mapImposto);
			}
		}
		return lista;			
	}
	
	public Map<String, Object> findDadosCalculoFrete(Long idDoctoServico) {
		Map<String, Object> result = new HashMap<String, Object>();

		List<Map<String, Object>> dadosFrete = parcelaDoctoServicoService.findDadosCalculoFrete(idDoctoServico);
		if (dadosFrete != null && !dadosFrete.isEmpty()) {
			result.putAll(dadosFrete.get(0));
			DomainValue dvTipoTabelaPreco = (DomainValue) result.get("tpTipoTabelaPreco");
			if (dvTipoTabelaPreco != null && result.get("tpSubtipoTabelaPreco") != null && result.get("nrVersao")!= null) {
				Integer nrVersao = (Integer) result.get("nrVersao");
				String tabelaPreco = dvTipoTabelaPreco.getDescription()+String.valueOf(nrVersao.intValue())+"-"+result.get("tpSubtipoTabelaPreco").toString();
				result.put("tabelaPreco", tabelaPreco);
			}	
			DomainValue dvCalculoPreco = (DomainValue)result.get("tpCalculoPreco");
			if (dvCalculoPreco!=null) {
				result.put("tpCalculoPreco", dvCalculoPreco.getDescription());
			}
		}

		List<Map<String, Object>> dadosDoctoServico = parcelaDoctoServicoService.findDadosCalculoDoctoServico(idDoctoServico);
		if (dadosDoctoServico != null && !dadosDoctoServico.isEmpty()) {
			List<Map<String, Object>> dadosReembolso = parcelaDoctoServicoService.findValorMercadoriaReembolso(idDoctoServico);
			if (dadosReembolso != null && !dadosReembolso.isEmpty()) {
				Map reembolso = dadosReembolso.get(0);
				if (reembolso.get("vlMercadoriaReemb") != null) {
					reembolso.put("vlMercadoriaReemb", reembolso.get("dsSimbolo")+ " " + FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal)reembolso.get("vlMercadoriaReemb")));
				}
				result.putAll(reembolso);
			}
			Map<String, Object> doctoServico = dadosDoctoServico.get(0);
			if (doctoServico.get("vlMercadoria") != null) {
				doctoServico.put("vlMercadoria", doctoServico.get("dsSimbolo")+ " " + FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) doctoServico.get("vlMercadoria")));
			}
			
			if(doctoServico.get("psAferido") == null){
				doctoServico.put("psAferido", FormatUtils.formatDecimal("#,###,###,###,##0.000", BigDecimal.ZERO));
			}
			
			if(doctoServico.get("tpPesoCalculo") != null){
				doctoServico.put("dsTpPesoCalculo", ((DomainValue) doctoServico.get("tpPesoCalculo")).getDescription());
			}
			
			result.putAll(doctoServico);
		}	

		List<Map<String, Object>> dadosInternacional = parcelaDoctoServicoService.findDadosCalculoDoctoServicoInternacional(idDoctoServico);
		if (dadosInternacional != null && !dadosInternacional.isEmpty()) {
			Map internacional = dadosInternacional.get(0);
			if(internacional.get("vlMercadoriaI")!= null) {
				internacional.put("vlMercadoriaI", internacional.get("dsSimbolo")+ " "+ FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) internacional.get("vlMercadoriaI")));
			}
			if(internacional.get("vlFreteExterno")!= null) {
				internacional.put("vlFreteExterno",internacional.get("dsSimbolo")+ " "+ FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) internacional.get("vlFreteExterno")));
			}
			result.putAll(internacional);
		}
		return result;
	}
	
	public Map findComplementosOutros(Long idDoctoServico){
		List lista = doctoServicoService.findComplementosOutros(idDoctoServico);
		Map map = null;
		Map mapConh = null;
		if(!lista.isEmpty()){
			map = (Map)lista.get(0);
			
			if(map.get("dhInclusao")!= null)
				map.put("dhInclusao", JTFormatUtils.format((DateTime)map.get("dhInclusao")));
			
			if(map.get("dhAlteracao")!= null)
				map.put("dhAlteracao", JTFormatUtils.format((DateTime)map.get("dhAlteracao")));
			
			List listaConh = conhecimentoService.findComplementosOutrosIndicadorCooperacao(idDoctoServico);
			if(!listaConh.isEmpty()){
				
				mapConh = (Map)listaConh.get(0);
				
				DomainValue dv = (DomainValue)mapConh.get("tpCtrcParceria");
				if(dv != null)
					map.put("indicadorCooperacao",dv.getDescription().toString());
				
				if(mapConh.get("idConhecimento")!= null){
					map.put("idConhecimento", Integer.valueOf(mapConh.get("idConhecimento").toString()));
				}	
				
				map.put("indicadorEDICliente",mapConh.get("blIndicadorEdi"));
			}	
		}
		return map;
		
	}
	
	public Map findRespostaAbas(Long idDoctoServico){
		Map mapAbas = new HashMap();
		boolean abaReemb = reciboReembolsoService.findReembolsoAba(idDoctoServico);
		mapAbas.put("abaReemb", abaReemb);
		
		boolean abaAgend = agendamentoEntregaService.findAgendamentosAba(idDoctoServico);
		mapAbas.put("abaAgend",abaAgend);
		
		boolean abaEmb =  servicoEmbalagemService.findEmbalagensAba(idDoctoServico);
		mapAbas.put("abaEmb",abaEmb);
		
		boolean abaDados = dadosComplementoService.findDadosComplAba(idDoctoServico);
		mapAbas.put("abaDados",abaDados);
		
		return mapAbas;
	}
	
	public Map findUrlWorkImage(Map criteria){
		Map map = new HashMap(); 
		map.put("urlWorkImage", (String) configuracoesFacade.getValorParametro("URL_WORKIMAGE"));
		return map;
	}
	
	public Map findRespostaAbasDetalhamento(Long idDoctoServico){
		Map mapAbas = new HashMap(); 
		
		boolean abaRNC = naoConformidadeService.findNCByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaRNC", abaRNC);
		
		boolean abaBloqueio = ocorrenciaDoctoServicoService.findOcorDSByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaBloqueio",abaBloqueio);
		
		boolean abaParceiras =  ctoCtoCooperadaService.findCoopByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaParceiras",abaParceiras);
		
		boolean abaCC =  preManifestoDocumentoService.findCCByIdDoctoServico(idDoctoServico);
		if(abaCC == false)
			abaCC =  doctoServicoService.findCCByIdDoctoServico(idDoctoServico);
		mapAbas.put("abaCC",abaCC);
		
		mapAbas.put("blIndenizacoes", (String) configuracoesFacade.getValorParametro("BL_INDENIZACOES_LMS"));
		
		return mapAbas;
	}
	
	public List findPaginatedItemNFC(TypedFlatMap criteria){
		return itemNfCtoService.findPaginatedItemNFC(criteria.getLong("idNotaFiscalConhecimento"));
	}
	
	public List findRetornoPopPupDoctoServico(TypedFlatMap criteria){
		List<TypedFlatMap> lista = doctoServicoService.findLookupDoctoServico(criteria);
		if(!lista.isEmpty()){
			 lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
			final TypedFlatMap map = lista.get(0);
			final List<Map> result = new ArrayList<Map>();
			result.add(convertMapLocalizacaoByDoctoServico(map));
			return result;
		}		
		return null;
	}
	
	public List findPaginatedControleCarga(TypedFlatMap criteria){
		return controleCargaService.findPaginatedControleCargaByLocalizacoMerc(criteria.getLong("idDoctoServico"));
	}
	
	public Map findManifestoColetaByIdDoctoServico(Long idDoctoServico){
		Map map =  manifestoColetaService.findManifestoColetaByIdDoctoServico(idDoctoServico);
		if(map != null){
			if(map.get("tpModoPedidoColeta")!= null){
				DomainValue tpModoPedidoColeta = (DomainValue)map.get("tpModoPedidoColeta");
				map.put("tpModoPedidoColeta",tpModoPedidoColeta.getDescription().toString());
			}
			if(map.get("tpPedidoColeta")!= null){
				DomainValue tpPedidoColeta = (DomainValue)map.get("tpPedidoColeta");
				map.put("tpPedidoColeta", tpPedidoColeta.getDescription().toString());
			}
			
			if(map.get("dhEmissao")!= null)
				map.put("dhEmissao", JTFormatUtils.format((DateTime)map.get("dhEmissao")));
			
			if(map.get("dhColetaDisponivel")!= null)
				map.put("dhColetaDisponivel", JTFormatUtils.format((DateTime)map.get("dhColetaDisponivel")));
		}
		return map;
	}
	
	public ResultSetPage findPaginatedManifestoViagem(TypedFlatMap criteria){
		return manifestoViagemNacionalService.findPaginatedManifestosViagemByLocalizacaoMercadoria(criteria.getLong("idDoctoServico"));
	}
	public ResultSetPage findPaginatedManifestoEntrega(TypedFlatMap criteria){
		return manifestoEntregaService.findPaginatedManifestoEntregaByIdDoctoServico(criteria.getLong("idDoctoServico"));
	}
	public ResultSetPage findPaginatedEventos(TypedFlatMap criteria){
		return eventoDocumentoServicoService.findPaginatedEventosByIdDoctoServico(criteria.getLong("idDoctoServico"));
	}
	public ResultSetPage findPaginatedBloqueiosLiberacoes(TypedFlatMap criteria){
		return ocorrenciaDoctoServicoService.findPaginatedBloqueiosLiberacoesByIdDoctoServ(criteria.getLong("idDoctoServico"));
	}
	
	//ABA RNC
	public Map findByIdDetailAbaRNC(TypedFlatMap criteria){
		Map map = new HashMap();
		List lista = naoConformidadeService.findNaoConformidadeByIdDoctoServicoLocMerc(criteria.getLong("idDoctoServico"));
		if(!lista.isEmpty()){
			map = (Map)lista.get(0);
			DomainValue dv = (DomainValue)map.get("tpStatusNaoConformidade");
			map.put("tpStatusNaoConformidade",dv.getDescription());
			
			if(map.get("dhEmissao")!= null)
				map.put("dhEmissao", JTFormatUtils.format((DateTime)map.get("dhEmissao")));
		}
		return map;
	}
	
	public List findPaginatedOcorrenciaNaoConformidade(Map criteria){
		return ocorrenciaNaoConformidadeService.findPaginatedOcorrenciaNaoConformidade((Long)criteria.get("idNaoConformidade"));
	}
	
	//ABA COBRANÇA
	 /**
     * 
     * @param criteria
     * @return
     */
    public List findPaginatedDevedorDocServFatByDoctoServico(TypedFlatMap criteria){
    	return consultarDadosCobrancaDocumentoServicoService.findDevedorDocServFatByDoctoServico(criteria);
    }
    
    /**
     * 
     * @param criteria
     * @return Map
     */
    public Map findDevedorDocServFatDetail(TypedFlatMap criteria){
    	Map map = consultarDadosCobrancaDocumentoServicoService.findDevedorDocServFatDetail(criteria);
    	
    	if(map.get("nmPais")!= null)
    		map.put("nmPais", map.get("nmPais").toString());
    	
    	if(map.get("tpSituacaoCobranca")!= null)
    		map.put("tpSituacaoCobranca", map.get("tpSituacaoCobranca").toString());
    	
    	if(map.get("tpMotivoDesconto")!= null)
    		map.put("tpMotivoDesconto", map.get("tpMotivoDesconto").toString());
    	
    	if(map.get("tpSituacaoAprovacao")!= null)
    		map.put("tpSituacaoAprovacao", map.get("tpSituacaoAprovacao").toString());
    	
    	return map;
    }
    
    //ABA PARCEIRAS
    
    public Map<String, Object> findCooperadaByIdConhecimento(Long idDoctoServico){
    	List<Map> ctoCooperadas = ctoCtoCooperadaService.findCooperadaByIdConhecimento(idDoctoServico);
    	
    	if (ctoCooperadas != null && !ctoCooperadas.isEmpty()) {
    		Map<String, Object> result = ctoCooperadas.get(0);
    		
    		Integer nrCtoCooperada = (Integer) result.remove("nrCtoCooperada");
    		if (nrCtoCooperada != null) {
    			result.put("nrCtoCooperada", FormatUtils.fillNumberWithZero(nrCtoCooperada.toString(), 8));
    		}
    		
    		if( result.get("idCtoCtoCooperada") != null) {
				List<Map> notasFiscais = notaFiscalCtoCooperadaService.findNotaFiscalByIdCooperada((Long)result.get("idCtoCtoCooperada"));
				if(notasFiscais != null && !notasFiscais.isEmpty()) {
					Map notaFiscal = notasFiscais.get(0);
					result.put("volumes", notaFiscal.get("qtVolumes"));
					result.put("pesoReal", notaFiscal.get("psMercadoria"));
					if (notaFiscal.get("vlTotal") != null) {
						result.put("valorMercadoria", result.get("dsSimboloMoeda") + " " + FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) notaFiscal.get("vlTotal")));
					}
				}
			}
    		if (result.get("tpIdentificacaoCooperada") != null) {
				 DomainValue dvCoop = (DomainValue) result.get("tpIdentificacaoCooperada");
				 result.put("nrIdentificacaoCooperada", FormatUtils.formatIdentificacao(dvCoop.getValue(), result.get("nrIdentificacaoCooperada").toString()));
			}	
    		if (result.get("tpConhecimento") != null) {
				 DomainValue dvConh = (DomainValue)result.get("tpConhecimento");
				 result.put("tpConhecimento", dvConh.getDescription().toString());
			}	
			if (result.get("tpIdentificacaoRem") != null) {
				 DomainValue dvRem = (DomainValue)result.get("tpIdentificacaoRem");
				 result.put("nrIdentificacaoRem", FormatUtils.formatIdentificacao(dvRem.getValue(), result.get("nrIdentificacaoRem").toString()));
			}	
			if (result.get("tpIdentificacaoDest") != null) {
				 DomainValue dvDest = (DomainValue)result.get("tpIdentificacaoDest");
				 result.put("nrIdentificacaoDest", FormatUtils.formatIdentificacao(dvDest.getValue(),result.get("nrIdentificacaoDest").toString()));
			}
			if (result.get("vlFrete") != null) {
				result.put("vlFrete", result.get("dsSimboloMoeda")+ " "+ FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal)result.get("vlFrete")));
			}
			if (result.get("dhEmissao") != null) {
				result.put("dhEmissao", JTFormatUtils.format((DateTime)result.get("dhEmissao")));
			}
			if (result.get("dtEntrega") != null) {
				result.put("dtEntrega", JTFormatUtils.format((DateTime)result.get("dtEntrega")));
			}
			return result;
    	}	
    	return null;
    }
    
    public List findPaginatedIntegrantesAbaParcerias(TypedFlatMap criteria){
    	List listaClientes =  ctoCtoCooperadaService.findPaginatedIntegrantes(criteria.getLong("idDoctoServico"));
		List listaNova = null;
		Long idRegistro =Long.valueOf(0);
		if(!listaClientes.isEmpty()){
			listaNova = new ArrayList();
			Map mapClientes = (HashMap)listaClientes.get(0);
			if(mapClientes.get("nmPessoaRem")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + Long.valueOf(1);
				map.put("tipoCliente", configuracoesFacade.getMensagem("remetente"));
				map.put("tpIdentificacao", mapClientes.get("tpIdentificacaoRem"));
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoRem"),mapClientes.get("nrIdentificacaoRem").toString()));
				map.put("nmPessoa", mapClientes.get("nmPessoaRem"));
				map.put("municipio", mapClientes.get("municipioRem"));
				map.put("idCliente", mapClientes.get("idClienteRem"));
				map.put("idRegistro", idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get("nmPessoaDest")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + Long.valueOf(1);
				map.put("tipoCliente", configuracoesFacade.getMensagem("destinatario"));
				map.put("tpIdentificacao", mapClientes.get("tpIdentificacaoDest"));
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoDest"), mapClientes.get("nrIdentificacaoDest").toString()));
				map.put("nmPessoa", mapClientes.get("nmPessoaDest"));
				map.put("municipio", mapClientes.get("municipioDest"));
				map.put("idCliente", mapClientes.get("idClienteDest"));
				map.put("idRegistro", idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get("nmPessoaConsi")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + Long.valueOf(1);
				map.put("tipoCliente", configuracoesFacade.getMensagem("consignatario"));
				map.put("tpIdentificacao", mapClientes.get("tpIdentificacaoConsi"));
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoConsi"), mapClientes.get("nrIdentificacaoConsi").toString()));
				map.put("nmPessoa", mapClientes.get("nmPessoaConsi"));
				map.put("municipio", mapClientes.get("municipioConsi"));
				map.put("idCliente", mapClientes.get("idClienteConsi"));
				map.put("idRegistro", idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get("nmPessoaRedes")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + Long.valueOf(1);
				map.put("tipoCliente", configuracoesFacade.getMensagem("redespacho"));
				map.put("tpIdentificacao", mapClientes.get("tpIdentificacaoRedes"));
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoRedes"), mapClientes.get("nrIdentificacaoRedes").toString()));
				map.put("nmPessoa", mapClientes.get("nmPessoaRedes"));
				map.put("municipio", mapClientes.get("municipioRedes"));
				map.put("idCliente", mapClientes.get("idClienteRedes"));
				map.put("idRegistro", idRegistro);
				listaNova.add(map);
			}
			if(mapClientes.get("nmPessoaDev")!= null){
				Map map = new HashMap();
				idRegistro = idRegistro + Long.valueOf(1);
				map.put("tipoCliente", configuracoesFacade.getMensagem("responsavelFrete"));
				map.put("tpIdentificacao", mapClientes.get("tpIdentificacaoDev"));
				map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)mapClientes.get("tpIdentificacaoDev"), mapClientes.get("nrIdentificacaoDev").toString()));
				map.put("nmPessoa", mapClientes.get("nmPessoaDev"));
				map.put("municipio", mapClientes.get("municipioDev"));
				map.put("idCliente", mapClientes.get("idClienteDev"));
				map.put("idRegistro", idRegistro);
				listaNova.add(map);
			}
			
		}
		return listaNova;
    }
    
    public List findPaginatedNotaFiscalAbaParcerias(TypedFlatMap criteria){
    	return notaFiscalCtoCooperadaService.findNotaFiscalByIdConhecimento(criteria.getLong("idDoctoServico"));
    }
    
    public Map findDadosCalculoByIdConhecimento(Long idDoctoServico){
    	Map map = ctoCtoCooperadaService.findDadosCalculoByIdConhecimento(idDoctoServico);
    	if(map.get("valorMercadoria")!= null)
    		map.put("valorMercadoria",map.get("dsSimbolo").toString()+ " " +FormatUtils.formatDecimal("#,###,###,###,##0.00",(BigDecimal)map.get("valorMercadoria")));
    	return map;
    }
    
    public Map findOutrosByIdConhecimento(Long idDoctoServico){
    	Map map = ctoCtoCooperadaService.findOutrosByIdConhecimento(idDoctoServico);
    	
    	if(map.get("dhInclusao")!= null)
    		map.put("dhInclusao", JTFormatUtils.format((DateTime)map.get("dhInclusao")));
    		
    	if(map.get("tpModal")!= null){
    		DomainValue dv= (DomainValue)map.get("tpModal");
    		map.put("tpModal",dv.getDescription().toString());
    	}
    	return map;
    }
    
    public List findPaginatedDadosFrete(TypedFlatMap criteria){
       	return parcelaCtoCooperadaService.findPaginatedDadosFrete(criteria.getLong("idDoctoServico"));
    }
    
    public Map<String, Object> findVlFreteParceriasDadosFrete(Long idDoctoServico){
    	Map<String, Object> result = parcelaCtoCooperadaService.findVlFreteDadosFrete(idDoctoServico);
    	if(result != null && result.get("vlFrete") != null) {
    		StringBuilder totalFrete = new StringBuilder()
    		.append(result.get("moeda")).append(" ")
    		.append(FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) result.get("vlFrete")));
    		result.put("totalFrete", totalFrete.toString());
    	}
		return result;
    }
    
    public List findServicosAdicionaisFrete(Long idDoctoServico) {
		List lista = parcelaPrecoService.findServicosAdicionaisFrete(idDoctoServico);
		for(Iterator iter= lista.iterator();iter.hasNext();) {
			Map mapa = (Map)iter.next();
			
			if (CD_ARMAZENAGEM.equals(mapa.get("cdParcelaPreco"))) {
				mapa.put("qtDiasArmazenagem", mapa.get("qtDias"));
				mapa.put("qtPaletesArmazenagem", mapa.get("qtPaletes"));
			} else if (CD_ESTADIA_VEICULO.equals(mapa.get("cdParcelaPreco"))) {
				mapa.put("qtDiasEstadiaVeiculo", mapa.get("qtDias"));
			} else if (CD_PALETIZACAO.equals(mapa.get("cdParcelaPreco"))) {
				mapa.put("qtPaletesPaletizacao", mapa.get("qtPaletes"));
			}
			if (mapa.get("dtPrimeiroCheque") != null) {
				mapa.put("dtPrimeiroCheque", JTFormatUtils.format((YearMonthDay) mapa.get("dtPrimeiroCheque")));
			}
			if (mapa.get("vlMercadoria") != null) {
				mapa.put("vlMercadoria", FormatUtils.formatDecimal("#,###,###,###,##0.00", (BigDecimal) mapa.get("vlMercadoria")));
			}
		}
		return lista;
	}

	public String execute(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirLocalizacaoMercadoriaService, parameters);
	}
	
	public String executeDadosVolumesDocumento(TypedFlatMap parameters) throws Exception {
		return reportExecutionManager.generateReportLocator(emitirRelatorioDadosVolumesDocumentoService, parameters);
	}
	
	public String executeRNC(TypedFlatMap parameters) throws Exception {
		parameters.put("naoConformidade.idNaoConformidade", parameters.remove("idNaoConformidade"));
		parameters.put("naoConformidade.nrNaoConformidade", parameters.remove("nrNaoConformidade"));
		parameters.put("naoConformidade.filial.sgFilial", parameters.remove("sgFilial"));
		return reportExecutionManager.generateReportLocator(emitirRNCService, parameters);
	}
	
	public List findPaginatedEnderecoIntegrante(Map mapIntegrante){
		List<Map> enderecos = enderecoPessoaService.findEnderecoPessoaByIdPessoa((Long) mapIntegrante.get("idIntegrante"));
    	
		if (enderecos != null) {
			for (Map map : enderecos) {
				String dsTipoLogradouro = ((VarcharI18n) map.get("dsTipoLogradouro")).getValue();
				String dsEndereco = (String) map.get("dsEndereco");
				String nrEndereco = (String) map.get("nrEndereco");
				String dsComplemento = (String) map.get("dsComplemento");
	    		map.put("enderecoCompleto", FormatUtils.formatEnderecoPessoa(dsTipoLogradouro, dsEndereco, nrEndereco, dsComplemento));
	    		map.put("tpEndereco", map.get("tpEndereco"));
	    		map.put("endPes","/image/fone.gif");
			}
		}
    	return enderecos;
    }
	
	/*
	 * METODOS PRIVADOS
	 */
	private TypedFlatMap montaTypedFlatMapDoctoServico(TypedFlatMap criteria){
		 criteria.put("servico.tipoServico.idTipoServico", criteria.getLong("idTipoServico"));
		 criteria.put("servico.tpModal", criteria.getString("tpModal"));
		 criteria.put("servico.tpAbrangencia", criteria.getString("tpAbrangencia"));
		 criteria.put("filialByIdFilialOrigem.idFilial", criteria.getLong("idFilialOrigem"));
		 criteria.put("filialByIdFilialDestino.idFilial", criteria.getLong("idFilialDestino"));
		 if (criteria.get("idFilialDoctoSer") != null) {
			 criteria.put("idFilialDoctoSer", String.valueOf(criteria.get("idFilialDoctoSer")));
		 }
		 return criteria;
	 }
	
	private Map convertMapLocalizacaoByDoctoServico(final TypedFlatMap localizacao){
		localizacao.put("abrangencia", localizacao.remove("abrangencia.value"));
		localizacao.put("modal", localizacao.remove("modal.value"));
		localizacao.put("tpDocumentoServico", localizacao.get("tpDocumentoServico.value"));
		localizacao.put("tpDoctoServico", localizacao.get("tpDocumentoServico"));
		 
		if (localizacao.get("tpDocumentoServico") != null){
			if (ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(localizacao.get("tpDocumentoServico"))) {
				localizacao.put("finalidade",localizacao.remove("tpConhecimento.value"));
				List menorNF = notaFiscalConhecimentoService.findNFByIdConhecimento(localizacao.getLong("idDoctoServico"));
				 if(!menorNF.isEmpty()){
					 localizacao.put("nfCliente", (Integer)menorNF.get(0));
				 }
			 }
		 }
		 
		if (localizacao.getVarcharI18n("tpIdentificacaoRem.description")!= null) {
			localizacao.put("nrIdentificacaoRemetente", FormatUtils.formatIdentificacao(new DomainValue(localizacao.getVarcharI18n("tpIdentificacaoRem.description").getValue()), localizacao.getString("nrIdentificacaoRemetente")));
		 }	 
		if (localizacao.getVarcharI18n("tpIdentificacaoDest.description")!= null) {
			localizacao.put("nrIdentificacaoDestinatario", FormatUtils.formatIdentificacao(new DomainValue(localizacao.getVarcharI18n("tpIdentificacaoDest.description").getValue()), localizacao.getString("nrIdentificacaoDestinatario")));
		 } 
		 
		if (localizacao.get("dhEmissao") != null) {
			localizacao.put("periodoInicial", localizacao.getDateTime("dhEmissao").toYearMonthDay());
			localizacao.put("periodoFinal", localizacao.getDateTime("dhEmissao").toYearMonthDay());
		 
		 }
		 return localizacao;
	 }
	
	public ResultSetPage<Map<String, Object>> findPaginatedVolumes(Map<String, Object> criteria) {
    	return volumeNotaFiscalService.findPaginatedMap(new PaginatedQuery(criteria));
    }
	
    public Integer getRowCountVolumes(Map<String,Object> criteria) {
    	return volumeNotaFiscalService.getRowCount(criteria);
    }
	
    public ResultSetPage<Map<String, Object>> findPaginatedEventosVolume(Map<String, Object> criteria) {
    	return eventoVolumeService.findPaginatedMap(new PaginatedQuery(criteria));
    }
    
    public Integer getRowCountEventosVolume(Map<String,Object> criteria) {
    	return eventoVolumeService.getRowCount(criteria);    	
    }
        
    public ResultSetPage<Map<String, Object>> findPaginatedEventosReciboPpd(Map<String, Object> criteria) {
		ResultSetPage rsp = ppdStatusReciboService.findPaginated(new PaginatedQuery(criteria));					
		
		List<PpdStatusRecibo> listStatus = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(listStatus.size());
		
		for(int i=0;i<listStatus.size();i++) {
			PpdStatusRecibo status = listStatus.get(i);
			retorno.add(status.getMapped());	
		}
				
		rsp.setList(retorno);
		return rsp;		
	}
    
    public ResultSetPage<Map<String, Object>> findPaginatedEventosRim(Map<String, Object> criteria) {
		ResultSetPage rsp = eventoRimService
				.findDoctoServicosByIdReciboIndenizacao(
						Long.valueOf(criteria.get("idReciboIndenizacao").toString()),
						Integer.valueOf(criteria.get("_currentPage").toString()),
						Integer.valueOf(criteria.get("_pageSize").toString()));

		List<Map<String, Object>> listStatus = rsp.getList();		
		List<Map<String, Object>> retorno = new ArrayList<Map<String,Object>>(listStatus.size());
		
		for(int i=0;i<listStatus.size();i++) {
			Map<String, Object> eventoRim = listStatus.get(i);
			DomainValue domainValue = (DomainValue) eventoRim.get("eventoRim_tpEventoIndenizacao");
			eventoRim.put("eventoRim_tpEventoIndenizacao", domainValue.getDescription().getValue());
			retorno.add(eventoRim);	
		}
		Collections.sort(retorno , new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return ((DateTime) o1.get("eventoRim_dhEventoRim")).compareTo((DateTime) o2.get("eventoRim_dhEventoRim"));
			}
		});
		rsp.setList(retorno);
		return rsp;		
	}
    public Integer getRowCountEventosRim(TypedFlatMap criteria) {
    	return eventoRimService.getRowCountEventoRimByIdReciboIndenizacao(Long.valueOf(criteria.get("idReciboIndenizacao").toString()));
    }
    
    
    public Map<String, Object> findPpdReciboById(Long id) {    	
    	Map<String,Object> bean = ppdReciboService.findById(id).getMapped();    	
    	return bean;
    }
    
    public Map<String, Object> findIndenizacaoById(TypedFlatMap criteria) {    	
    	return doctoServicoIndenizacaoService.findMapParaLocalizacaoMercadoria(criteria.getLong("idDoctoServicoIndenizacao"));
    }
    
    public List<Map<String, Object>> findPaginatedFilialDebitada(TypedFlatMap criteria){
    	return doctoServicoIndenizacaoService.findFilialDebitadaByIdDoctoServicoIndenizacao(criteria.getLong("idDoctoServicoIndenizacao"));
    }
    
	/*
	 * GETTERS E SETTERS
	 */
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setEmitirRNCService(EmitirRNCService emitirRNCService) {
		this.emitirRNCService = emitirRNCService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public EmitirLocalizacaoMercadoriaService getEmitirLocalizacaoMercadoriaService() {
		return emitirLocalizacaoMercadoriaService;
	}
	public void setEmitirLocalizacaoMercadoriaService(EmitirLocalizacaoMercadoriaService emitirLocalizacaoMercadoriaService) {
		this.emitirLocalizacaoMercadoriaService = emitirLocalizacaoMercadoriaService;
	}
	public void setParcelaCtoCooperadaService(ParcelaCtoCooperadaService parcelaCtoCooperadaService) {
		this.parcelaCtoCooperadaService = parcelaCtoCooperadaService;
	}
    public void setNotaFiscalCtoCooperadaService(NotaFiscalCtoCooperadaService notaFiscalCtoCooperadaService) {
		this.notaFiscalCtoCooperadaService = notaFiscalCtoCooperadaService;
	}
	public void setConsultarDadosCobrancaDocumentoServicoService(ConsultarDadosCobrancaDocumentoServicoService consultarDadosCobrancaDocumentoServicoService) {
		this.consultarDadosCobrancaDocumentoServicoService = consultarDadosCobrancaDocumentoServicoService;
	}
	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	public void setCtoCtoCooperadaService(CtoCtoCooperadaService ctoCtoCooperadaService) {
		this.ctoCtoCooperadaService = ctoCtoCooperadaService;
	}
	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}
	public void setManifestoInternacionalService(ManifestoInternacionalService manifestoInternacionalService) {
		this.manifestoInternacionalService = manifestoInternacionalService;
	}
	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	public void setItemNfCtoService(ItemNfCtoService itemNfCtoService) {
		this.itemNfCtoService = itemNfCtoService;
	}
	public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
		this.impostoServicoService = impostoServicoService;
	}
	public void setParcelaDoctoServicoService(ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}
	public void setAgendamentoEntregaService(AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}
	public void setChequeReembolsoService(ChequeReembolsoService chequeReembolsoService) {
		this.chequeReembolsoService = chequeReembolsoService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}
	public void setMirService(MirService mirService) {
		this.mirService = mirService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setManifestoColetaService(
			ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setTipoServicoService(TipoServicoService tipoServicoService) {
		this.tipoServicoService = tipoServicoService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}
	public void setServicoEmbalagemService(ServicoEmbalagemService servicoEmbalagemService) {
		this.servicoEmbalagemService = servicoEmbalagemService;
	}
	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}
	public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}
	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}

	public void setReportExecutionManager(
			ReportExecutionManager reportExecutionManager) {
		this.reportExecutionManager = reportExecutionManager;
	}	
	
	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setPpdReciboService(PpdReciboService ppdReciboService) {
		this.ppdReciboService = ppdReciboService;
	}

	public void setPpdStatusReciboService(
			PpdStatusReciboService ppdStatusReciboService) {
		this.ppdStatusReciboService = ppdStatusReciboService;
	}

	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}	
	
	public void setEmitirRelatorioDadosVolumesDocumentoService(EmitirRelatorioDadosVolumesDocumentoService emitirRelatorioDadosVolumesDocumentoService) {
		this.emitirRelatorioDadosVolumesDocumentoService = emitirRelatorioDadosVolumesDocumentoService;
}
	
	public void setEventoRimService(EventoRimService eventoRimService) {
		this.eventoRimService = eventoRimService;
}

	public void setDoctoServicoIndenizacaoService(
			DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}
	
	public ClienteUsuarioCCTService getClienteUsuarioCCTService() {
		return clienteUsuarioCCTService;
}

	public void setClienteUsuarioCCTService(ClienteUsuarioCCTService clienteUsuarioCCTService) {
		this.clienteUsuarioCCTService = clienteUsuarioCCTService;
	}
	
	/**
	 * LMSA-2280 - Verifica parâmetro para bloqueio de tela obsoleta SIM >
	 * Consultas e Relatórios > Consultar Localizações de Mercadoria. Lança
	 * {@link BusinessException} de código LMS-27129 caso exista o parâmetro e o
	 * valor seja {@code "S"}.
	 * 
	 * FIXME código temporário
	 */
	public void validTelaObsoleta() {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Object valorParametro = configuracoesFacade.getValorParametro(idFilial, "TELA_OBSOL_LMSA-2280");
		if ("S".equals(valorParametro)) {
			throw new BusinessException("LMS-27129");
}
	}

}
