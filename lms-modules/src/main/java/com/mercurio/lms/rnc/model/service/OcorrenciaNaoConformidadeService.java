package com.mercurio.lms.rnc.model.service;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.batch.AdsmNativeBatchSqlOperations;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.DescargaManifesto;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.DescargaManifestoService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.constantes.ConsGeral;
import com.mercurio.lms.constantes.entidades.*;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.CtoAwbService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.municipios.ConstantesMunicipios;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialRotaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rnc.model.*;
import com.mercurio.lms.rnc.model.dao.OcorrenciaNaoConformidadeDAO;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.dao.LMRNCDAO;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Hibernate;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.math.BigDecimal;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.rnc.ocorrenciaNaoConformidadeService"
 */
public class OcorrenciaNaoConformidadeService extends CrudService<OcorrenciaNaoConformidade, Long> {

	private static final long MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA = 37L;
	private static final long MERCADORIA_RETORNADA_EM_DESCARGA = 33L;
	private static final int NR_SEQUENCIA_MAX_SIZE = 930;
	private static final long ENTREGA_REALIZADA = 1L;
	private static final long AGUARDANDO_DESCARGA = 38L;
	private static final long EM_DESCARGA = 34L;
	private static final String DM_STATUS_OCORRENCIA_NC = "DM_STATUS_OCORRENCIA_NC";
	private static final int INT_VAL_POS_0 = 0;
	private static final int INT_VAL_POS_1 = 1;
	private static final int INT_VAL_POS_2 = 2;
	private static final int INT_VAL_POS_3 = 3;
    public static final String UPDATE_ONC = "updateONC";
    private static final String TP_RNC_AUTO_ENCERRAR = "TP_RNC_AUTO_ENCERRAR";
    private static final String ID_NOTA_FISCAL_CONHECIMENTO = "ID_NOTA_FISCAL_CONHECIMENTO";
    private static final String ID_MANIFESTO = "ID_MANIFESTO";
    private static final String NR_SEQUENCIA = "NR_SEQUENCIA";
    private static final String QT_VOLUMES = "QT_VOLUMES";
    private static final String TP_DOCUMENTO_SERVICO = "TP_DOCUMENTO_SERVICO";
    private static final String SG_FILIAL = "SG_FILIAL";
    private static final String NR_DOCTO_SERVICO = "NR_DOCTO_SERVICO";
    private static final String TEMPO_DESCARGA_RNC = "TEMPO_DESCARGA_RNC";

	private CausaNaoConformidadeService causaNaoConformidadeService;
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ControleCargaService controleCargaService;
	private DescargaManifestoService descargaManifestoService;
	private DescricaoPadraoNcService descricaoPadraoNcService;
	private DoctoServicoService doctoServicoService;
	private DomainValueService domainValueService;
	private EmpresaService empresaService;
	private FilialService filialService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ManifestoService manifestoService;
	private MoedaService moedaService;
	private MotivoAberturaNcService motivoAberturaNcService;
	private NaoConformidadeService naoConformidadeService;
	private NotaOcorrenciaNcService notaOcorrenciaNcService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private LMRNCDAO lmrncdao;
	private WorkflowPendenciaService workflowPendenciaService;
	private ParametroGeralService parametroGeralService;
	private FilialRotaService filialRotaService;
	private ConversaoMoedaService conversaoMoedaService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private ItemOcorrenciaNcService itemOcorrenciaNcService;
	private DisposicaoService disposicaoService; 
	private CtoAwbService ctoAwbService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public CausaNaoConformidadeService getCausaNaoConformidadeService() {
		return causaNaoConformidadeService;
	}

	public void setCausaNaoConformidadeService(CausaNaoConformidadeService causaNaoConformidadeService) {
		this.causaNaoConformidadeService = causaNaoConformidadeService;
	}

	public ClienteService getClienteService() {
		return clienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public DescargaManifestoService getDescargaManifestoService() {
		return descargaManifestoService;
	}

	public void setDescargaManifestoService(DescargaManifestoService descargaManifestoService) {
		this.descargaManifestoService = descargaManifestoService;
	}

	public DescricaoPadraoNcService getDescricaoPadraoNcService() {
		return descricaoPadraoNcService;
	}

	public void setDescricaoPadraoNcService(DescricaoPadraoNcService descricaoPadraoNcService) {
		this.descricaoPadraoNcService = descricaoPadraoNcService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public LMRNCDAO getLmrncdao() {
		return lmrncdao;
	}

	public void setLmrncdao(LMRNCDAO lmrncdao) {
		this.lmrncdao = lmrncdao;
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public MotivoAberturaNcService getMotivoAberturaNcService() {
		return motivoAberturaNcService;
	}

	public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
		this.motivoAberturaNcService = motivoAberturaNcService;
	}

	public NaoConformidadeService getNaoConformidadeService() {
		return naoConformidadeService;
	}

	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}

	public NotaOcorrenciaNcService getNotaOcorrenciaNcService() {
		return notaOcorrenciaNcService;
	}

	public void setNotaOcorrenciaNcService(
			NotaOcorrenciaNcService notaOcorrenciaNcService) {
		this.notaOcorrenciaNcService = notaOcorrenciaNcService;
	}

	public VersaoDescritivoPceService getVersaoDescritivoPceService() {
		return versaoDescritivoPceService;
	}

	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}

	public WorkflowPendenciaService getWorkflowPendenciaService() {
		return workflowPendenciaService;
	}

	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}

	public void setDisposicaoService(DisposicaoService disposicaoService) {
		this.disposicaoService = disposicaoService;
	}

	/**
	 * Recupera uma instï¿½ncia de <code>OcorrenciaNaoConformidade</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instï¿½ncia que possui o id informado.
	 * @throws
	 */
	public OcorrenciaNaoConformidade findById(java.lang.Long id) {
		
		return this.findByIdCustom(id);
	}
	
	
	/**
	 * Recupera uma instï¿½ncia de <code>OcorrenciaNaoConformidade</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instï¿½ncia que possui o id informado.
	 */
	public TypedFlatMap findOcorrenciaNaoConformidadeById(java.lang.Long id) {
		List list = getOcorrenciaNaoConformidadeDAO().findOcorrenciaNaoConformidadeById(id);
		List lista = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(list);
		TypedFlatMap map = (TypedFlatMap) lista.get(INT_VAL_POS_0);

		List listaNotas = notaOcorrenciaNcService.findNotaOcorrenciaNcByOcorrenciaNaoConformidade(id);
		map.put("notaOcorrenciaNcs", listaNotas);
		return map;
	}

	/**
	 * LMS-925: Verifica se ï¿½ possï¿½vel atribuir a responsabilidade da nï¿½o
	 * conformidade ï¿½ filial de origem do controle de carga.
	 * 
	 * @param controleCarga controle de carga
	 * @return filial responsï¿½vel ou <code>null</code> caso nï¿½o seja possï¿½vel
	 *         atribuir
	 * @author Luis Carlos Poletto
	 */
	public Filial findFilialControleCarga(ControleCarga controleCarga) {
		List<CarregamentoDescarga> descargas = controleCarga.getCarregamentoDescargas();
		if (descargas != null) {
			Filial filialSessao = SessionUtils.getFilialSessao();
			for (CarregamentoDescarga descarga : descargas) {
				if (filialSessao.getIdFilial().equals(descarga.getFilial().getIdFilial())) {
					DateTime fimOperacao = descarga.getDhFimOperacao();
					if (fimOperacao == null) {
						return controleCarga.getFilialByIdFilialOrigem();
					} else {
						// verifica o parametro da filial
						DateTime agora = JTDateTimeUtils.getDataHoraAtual();
						Duration duration = new Duration(agora, fimOperacao);
						int horas = (int) (duration.getMillis() / 3600000);
						int tempoDescarga = ((BigDecimal) configuracoesFacade.getValorParametro(
                                filialSessao.getIdFilial(), TEMPO_DESCARGA_RNC)).intValue();
						if (horas < tempoDescarga) {
							return controleCarga.getFilialByIdFilialOrigem();
						}
					}
					return null;
				}
			}
		}
		return null;
	}
	
	/**
	 * Apaga uma entidade atravï¿½s do Id.
	 * 
	 * @param id indica a entidade que deverï¿½ ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga vï¿½rias entidades atravï¿½s do Id.
	 * 
	 * @param ids lista com as entidades que deverï¿½o ser removida.
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrï¿½rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(OcorrenciaNaoConformidade bean) {
        return super.store(bean);
    }

	/**
	 * Atribui o DAO responsável por tratar a persistï¿½ncia dos dados deste serviï¿½o.
	 * 
	 * @param dao Instância do DAO.
	 */
	public void setOcorrenciaNaoConformidadeDAO(OcorrenciaNaoConformidadeDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviï¿½o que ï¿½ responsï¿½vel por tratar a persistï¿½ncia dos dados deste serviï¿½o.
	 * 
	 * @return Instï¿½ncia do DAO.
	 */
	private OcorrenciaNaoConformidadeDAO getOcorrenciaNaoConformidadeDAO() {
		return (OcorrenciaNaoConformidadeDAO) getDao();
	}
	

	public Map findManifestoComControleCargas(Long idDoctoServico) {
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		List<Manifesto> manifestos = manifestoService.findManifestoByDataEmissao(idDoctoServico, filialUsuarioLogado.getIdFilial());
		
		Map map = new HashMap();
		map.put("manifestoCount", manifestos.size());
		if (manifestos.size() == INT_VAL_POS_1) {
			Manifesto manifesto = manifestos.get(INT_VAL_POS_0);
		
			if ("E".equals(manifesto.getTpManifesto().getValue())) {
				map.put("idFilialManifesto", manifesto.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getIdFilial());
				map.put("sgFilialManifesto", manifesto.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getSgFilial());
				map.put("nrManifesto", manifesto.getManifestoEntrega().getNrManifestoEntrega());
				map.put("tpManifesto", "EN");
			} else if ("V".equals(manifesto.getTpManifesto().getValue()) && "N".equals(manifesto.getTpAbrangencia().getValue())) {
				map.put("idFilialManifesto", manifesto.getManifestoViagemNacional().getManifesto().getFilialByIdFilialOrigem().getIdFilial());
				map.put("sgFilialManifesto", manifesto.getManifestoViagemNacional().getManifesto().getFilialByIdFilialOrigem().getSgFilial());
				map.put("nrManifesto", manifesto.getManifestoViagemNacional().getNrManifestoOrigem());
				map.put("tpManifesto", "VN");
			} else if ("V".equals(manifesto.getTpManifesto().getValue()) && "I".equals(manifesto.getTpAbrangencia().getValue())) {
				map.put("idFilialManifesto", manifesto.getManifestoInternacional().getManifesto().getFilialByIdFilialOrigem().getIdFilial());
				map.put("sgFilialManifesto", manifesto.getManifestoInternacional().getManifesto().getFilialByIdFilialOrigem().getSgFilial());
				map.put("nrManifesto", manifesto.getManifestoInternacional().getNrManifestoInt());
				map.put("tpManifesto", "VI");
			}

			map.put("idManifesto", manifesto.getIdManifesto());
			map.put("tpStatusManifesto", manifesto.getTpStatusManifesto().getValue());
			map.put("sgFilialControleCarga", manifesto.getControleCarga().getFilialByIdFilialOrigem().getSgFilial());
			map.put("idControleCarga", manifesto.getControleCarga().getIdControleCarga());
			map.put("nrControleCarga", manifesto.getControleCarga().getNrControleCarga());
			
			if (manifesto.getControleCarga().getMeioTransporteByIdSemiRebocado() != null) {
				map.put("semiReboqueFrota", manifesto.getControleCarga().getMeioTransporteByIdSemiRebocado().getNrFrota());
				map.put("semiReboquePlaca", manifesto.getControleCarga().getMeioTransporteByIdSemiRebocado().getNrIdentificador());
			}

			if (manifesto.getControleCarga().getMeioTransporteByIdTransportado() != null) {
				map.put("veiculoFrota", manifesto.getControleCarga().getMeioTransporteByIdTransportado().getNrFrota());
				map.put("veiculoPlaca", manifesto.getControleCarga().getMeioTransporteByIdTransportado().getNrIdentificador());
			}
		}
		return map;
	}
	
	/**
	 * Retorna o maior nrOcorrenciaNc de uma NaoConformidade.
	 * 
	 * @param idNaoConformidade
	 * @return
	 */
    public Integer findMaxNrOcorrenciaNcByNaoConformidade(Long idNaoConformidade) {
    	return getOcorrenciaNaoConformidadeDAO().findMaxNrOcorrenciaNcByNaoConformidade(idNaoConformidade);
    }
    
    
    /**
     * @param idManifesto
     * @param idDoctoServico
     * @param idMotivoAberturaNc
     * @param idControleCarga
     * @param idEmpresa
     * @param idDescricaoPadraoNc
     * @param idFilialByFilialResponsavel
     * @param idCausaNaoConformidade
     * @param idMoeda
     * @param dsOcorrenciaNc
     * @param blCaixaReaproveitada
     * @param dsCaixaReaproveitada
     * @param dsCausaNc
     * @param vlOcorrenciaNc
     * @param qtVolumes
     * @param listaNotaOcorrenciaNcs
     * @param idNaoConformidade
     * @return
     */
	public OcorrenciaNaoConformidade storeOcorrenciaNaoConformidade(
			Long idManifesto, Long idDoctoServico, Long idMotivoAberturaNc, Long idControleCarga, 
			Long idEmpresa,Long idDescricaoPadraoNc, Long idFilialByFilialResponsavel, Long idCausaNaoConformidade,
			Long idMoeda, String dsOcorrenciaNc, Boolean blCaixaReaproveitada, String dsCaixaReaproveitada,
			String dsCausaNc, BigDecimal vlOcorrenciaNc, Integer qtVolumes, List listaNotaOcorrenciaNcs, Long idNaoConformidade) {
		validatePreConditionsByOcorrenciaNaoConformidade(idNaoConformidade, 
				dsOcorrenciaNc, idDoctoServico, qtVolumes, vlOcorrenciaNc, idManifesto, idMotivoAberturaNc);

		NaoConformidade naoConformidade = naoConformidadeService.findById(idNaoConformidade);

		boolean jaExisteRNCEmAberto = false; 
		if(naoConformidade.getOcorrenciaNaoConformidades()!=null){
			for(Object obj :  naoConformidade.getOcorrenciaNaoConformidades()){
				OcorrenciaNaoConformidade onc = (OcorrenciaNaoConformidade) obj;
				if("A".equals(onc.getTpStatusOcorrenciaNc().getValue())
						&& idMotivoAberturaNc.equals(onc.getMotivoAberturaNc().getIdMotivoAberturaNc()) ){
					jaExisteRNCEmAberto = true;
				}
			}
		}
		
		if(jaExisteRNCEmAberto){
			throw new BusinessException("LMS-21064"); //Tipo de ocorrï¿½ncia jï¿½ cadastrada para esta Nï¿½o-Conformidade.
		}else{
			
			boolean docServicoEstaEntregue = naoConformidade.getDoctoServico().getLocalizacaoMercadoria().getCdLocalizacaoMercadoria().intValue() == INT_VAL_POS_1;
			boolean rncNaoEstaAguargandoAprovacao = !"AGP".equals(naoConformidade.getTpStatusNaoConformidade().getValue());
			
			if(docServicoEstaEntregue && rncNaoEstaAguargandoAprovacao){
				
				//alterar a RNC para "Aguardando Aprovaï¿½ï¿½o" e...
				naoConformidade.setTpStatusNaoConformidade(new DomainValue("AGP"));
				
				//Chamar a rotina de aprovaï¿½ï¿½o via Workflow (39.01.03.01 ï¿½ Gerar Pendï¿½ncias) passando as seguintes informaï¿½ï¿½es:
				for(Object obj :  naoConformidade.getOcorrenciaNaoConformidades()){
					OcorrenciaNaoConformidade onc = (OcorrenciaNaoConformidade) obj;
					workflowPendenciaService.generatePendencia(
			    			SessionUtils.getFilialSessao().getIdFilial(), 
			    			ConstantesWorkflow.NR1202_APROVAR_RNC_PARA_DOCUMENTO_DE_SERVICO,
			    			onc.getIdOcorrenciaNaoConformidade(),
			    			String.format("A nï¿½o conformidade %s %08d ï¿½ referente a um documento de serviï¿½o jï¿½ entregue.", naoConformidade.getFilial().getSgFilial(), naoConformidade.getNrNaoConformidade()),
			    			JTDateTimeUtils.getDataHoraAtual());
				}
				
			}
		}

		Filial filialUsuario = SessionUtils.getFilialSessao();
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();

		int nrMaxRNC = configuracoesFacade.
				incrementaParametroSequencial(filialUsuario.getIdFilial(), "NR_NAO_CONFORMIDADE", true).intValue();
		Integer nrNaoConformidade = Integer.valueOf(nrMaxRNC);

		Integer nrOcorrenciaNc = findMaxNrOcorrenciaNcByNaoConformidade(idNaoConformidade);
		
		OcorrenciaNaoConformidade onc = new OcorrenciaNaoConformidade();
		onc.setBlCaixaReaproveitada(blCaixaReaproveitada);
		onc.setCausaNaoConformidade(causaNaoConformidadeService.findById(idCausaNaoConformidade));
		if (idControleCarga != null) {
			onc.setControleCarga(controleCargaService.findByIdInitLazyProperties(idControleCarga, false));
		}
		onc.setDescricaoPadraoNc(descricaoPadraoNcService.findById(idDescricaoPadraoNc));
		onc.setDhInclusao(dhAtual);
		onc.setDsCaixaReaproveitada(dsCaixaReaproveitada);
		onc.setDsCausaNc(dsCausaNc);
		onc.setDsOcorrenciaNc(dsOcorrenciaNc);
		if (idEmpresa != null) {
			onc.setEmpresa(empresaService.findById(idEmpresa));
		}
		onc.setFilialByIdFilialAbertura(filialUsuario);
		onc.setFilialByIdFilialLegado(filialUsuario);
		onc.setFilialByIdFilialResponsavel(filialService.findById(idFilialByFilialResponsavel));
		onc.setIdOcorrenciaNaoConformidade(null);
		if (idManifesto != null) {
			onc.setManifesto(manifestoService.findByIdInitLazyProperties(idManifesto, false));
		}
		onc.setMotivoAberturaNc(motivoAberturaNcService.findById(idMotivoAberturaNc));
		onc.setNaoConformidade(naoConformidade);
		if (listaNotaOcorrenciaNcs != null) {
			for (Iterator iter = listaNotaOcorrenciaNcs.iterator(); iter.hasNext();) {
				NotaOcorrenciaNc notaOcorrenciaNc = (NotaOcorrenciaNc)iter.next();
				notaOcorrenciaNc.setOcorrenciaNaoConformidade(onc);
			}
		}
		onc.setNotaOcorrenciaNcs(listaNotaOcorrenciaNcs);
		onc.setNrOcorrenciaNc(nrOcorrenciaNc);
		onc.setNrRncLegado(nrNaoConformidade);
		onc.setQtVolumes(qtVolumes);
		onc.setTpStatusOcorrenciaNc(domainValueService.findDomainValueByValue(DM_STATUS_OCORRENCIA_NC, "A"));
		onc.setUsuario(SessionUtils.getUsuarioLogado());
		if (vlOcorrenciaNc != null) {
			onc.setVlOcorrenciaNc(vlOcorrenciaNc);
			if (idMoeda != null) {
				onc.setMoeda(moedaService.findById(idMoeda));
			}
		}
		onc.setIdOcorrenciaNaoConformidade((Long)store(onc));
		
		return onc;
	}
	
	
	/**
	 * @param qtVolumesOcorNc
	 * @param vlOcorrenciaNc
	 * @param qtVolumesDoctoServico
	 * @param vlTotalDoctoServico
	 */
	private void validateQtdVolumesValor(Integer qtVolumesOcorNc, BigDecimal vlOcorrenciaNc, Integer qtVolumesDoctoServico, BigDecimal vlTotalDoctoServico) {
		if (qtVolumesOcorNc != null && qtVolumesDoctoServico != null && qtVolumesOcorNc.compareTo(qtVolumesDoctoServico) > INT_VAL_POS_0) {
				throw new BusinessException("LMS-12013");
		}
		if (vlOcorrenciaNc != null && vlOcorrenciaNc.compareTo(vlTotalDoctoServico) > INT_VAL_POS_0) {
				throw new BusinessException("LMS-12012");
		}
	}


	/**
	 * @param idNaoConformidade
	 * @param dsOcorrenciaNc
	 * @param idDoctoServico
	 * @param qtVolumes
	 * @param vlOcorrencias
	 * @param idManifesto
	 */
	public void validatePreConditionsByOcorrenciaNaoConformidade(Long idNaoConformidade, 
			String dsOcorrenciaNc, Long idDoctoServico, Integer qtVolumes, BigDecimal vlOcorrencias, 
																 Long idManifesto, Long idMotivoAberturaNc) {
		if (idDoctoServico != null) {
			TypedFlatMap mapDoctoServico = doctoServicoService.findDoctoServicoByTpDocumento(idDoctoServico);
			validateQtdVolumesValor(qtVolumes, vlOcorrencias, 
					mapDoctoServico.getInteger("qtVolumes"), mapDoctoServico.getBigDecimal("vlTotalDocServico"));
		}
		if (idManifesto != null) {
			Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
			validaManifesto(manifesto.getFilialByIdFilialDestino().getEmpresa().getTpEmpresa().getValue(), 
					manifesto.getTpManifesto().getValue(), manifesto.getTpAbrangencia(), manifesto.getTpStatusManifesto().getValue());
		}
		if (idMotivoAberturaNc != null && (getOcorrenciaNaoConformidadeDAO().validaTipoOcorrenciaJaUsado(idNaoConformidade, idMotivoAberturaNc))) {
				throw new BusinessException("LMS-21064");
	}
		}


	/**
	 * @param idDoctoServico
	 * @param qtVolumes
	 * @param vlOcorrencias
	 * @param idManifesto
	 */
	public void validatePreConditionsByRnc(Long idDoctoServico, Integer qtVolumes, BigDecimal vlOcorrencias, Long idManifesto) {
		if (idDoctoServico != null) {
			DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
			if (doctoServico instanceof Conhecimento) {
				validateQtdVolumesValor(qtVolumes, vlOcorrencias, ((Conhecimento)doctoServico).getQtVolumes(), 
									   ((Conhecimento)doctoServico).getVlMercadoria());
			}
		}
		if (idManifesto != null) {
			Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
			validaManifesto(manifesto.getFilialByIdFilialDestino().getEmpresa().getTpEmpresa().getValue(), 
				manifesto.getTpManifesto().getValue(), manifesto.getTpAbrangencia(), manifesto.getTpStatusManifesto().getValue());
		}
	}

	
	/**
	 * Validar se existe cotaï¿½ï¿½o configurada para a moeda
	 *
	 * @param idFilial
	 * @param idMoeda
	 * @param vlOcorrenciaNc
	 */
	public void validateMoedaCotacao(Long idFilial, Long idMoeda, BigDecimal vlOcorrenciaNc) {
		MoedaPais moedaPais = filialService.findMoedaPaisByIdFilial(idFilial);
		conversaoMoedaService.findConversaoMoeda(moedaPais.getPais().getIdPais(), idMoeda, SessionUtils.getPaisSessao().getIdPais(), SessionUtils.getMoedaSessao().getIdMoeda(), JTDateTimeUtils.getDataAtual(), vlOcorrenciaNc);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void validateManifestoPreenhidoRnc(Map parametros) {
		Integer nrManifesto = MapUtils.getInteger(parametros, "nrManifesto");
		Long idDoctoServico = MapUtils.getLong(parametros, "idDoctoServico");
		Long motivoAbertura = MapUtils.getLong(parametros, "idMotivoAberturaNc");
	
		if ((nrManifesto == null || nrManifesto == INT_VAL_POS_0) && idDoctoServico != null) {
			Boolean[] validacoes = new Boolean[5];
			
			Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
			DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);

			validacoes[INT_VAL_POS_0] = doctoServico != null && !doctoServico.getFilialByIdFilialOrigem().getIdFilial().equals(filialUsuarioLogado.getIdFilial());
			
			List manifestoDestino = manifestoService.findManifestoViagemFilialDestino(idDoctoServico, filialUsuarioLogado.getIdFilial());
			validacoes[INT_VAL_POS_1] = !CollectionUtils.isEmpty(manifestoDestino);

			MotivoAberturaNc motivoAberturaNc = motivoAberturaNcService.findById(motivoAbertura);
			validacoes[INT_VAL_POS_2] = "S".equals(motivoAberturaNc.getBlExigeDocServico());

			List manifestoOrigem = manifestoService.findManifestoEntregaFilialOrigem(idDoctoServico, filialUsuarioLogado.getIdFilial());
			validacoes[INT_VAL_POS_3] = manifestoOrigem == null || CollectionUtils.isEmpty(manifestoDestino);
			
			/* LMS-3240 */
			
			if (validacoes[INT_VAL_POS_0] && validacoes[INT_VAL_POS_1] && validacoes[INT_VAL_POS_2] && validacoes[INT_VAL_POS_3]) {
				throw new BusinessException("LMS-09087");
			}

		}
	}

	/**
	 * @param tpEmpresaByFilialDestinoManifesto
	 * @param tpManifesto
	 * @param tpAbrangencia
	 * @param tpStatusManifesto
	 */
	private void validaManifesto(String tpEmpresaByFilialDestinoManifesto, String tpManifesto, DomainValue tpAbrangencia, String tpStatusManifesto) {
		// se não for nulo, será N ou I.
		if (tpAbrangencia == null) {
			return;
		}

		if ("V".equals(tpManifesto) && !"P".equals(tpEmpresaByFilialDestinoManifesto)) {
			if ("FE".equals(tpStatusManifesto) || "ED".equals(tpStatusManifesto)) {
					return;
			}
				throw new BusinessException("LMS-12014");
			}
		}

	/**
	 * @param idManifesto
	 * @param idDoctoServico
	 * @param idMotivoAberturaNc
	 * @param idControleCarga
	 * @param idEmpresa
	 * @param idDescricaoPadraoNc
	 * @param idFilialByFilialResponsavel
	 * @param idCausaNaoConformidade
	 * @param idMoeda
	 * @param dsOcorrenciaNc
	 * @param blCaixaReaproveitada
	 * @param dsCaixaReaproveitada
	 * @param dsCausaNc
	 * @param vlOcorrenciaNc
	 * @param qtVolumes
	 * @param idClienteDestinatarioNc
	 * @param idClienteRemetenteNc
	 * @param listaNotaOcorrenciaNcs
	 * @return
	 */
	public java.io.Serializable storeRNC(Long idManifesto, Long idDoctoServico,
			Long idMotivoAberturaNc, Long idControleCarga, Long idEmpresa,
			Long idDescricaoPadraoNc, Long idFilialByFilialResponsavel, Long idCausaNaoConformidade,
			Long idMoeda, String dsOcorrenciaNc, Boolean blCaixaReaproveitada, String dsCaixaReaproveitada,
			String dsCausaNc, BigDecimal vlOcorrenciaNc, Integer qtVolumes, Long idClienteDestinatarioNc,
			Long idClienteRemetenteNc, List listaNotaOcorrenciaNcs,String dsMotivoAbertura, List itensNFe, 
			Long idAwb, String tpModal, OcorrenciaNaoConformidade onc, String causadorRnc,
			DateTime dhEvento) {
		validateMoedaCotacao(idFilialByFilialResponsavel, idMoeda, vlOcorrenciaNc);
		validatePreConditionsByRnc(idDoctoServico, qtVolumes, vlOcorrenciaNc, idManifesto);

		Filial filialUsuario = SessionUtils.getFilialSessao();
		DateTime dhAtual = JTDateTimeUtils.getDataHoraAtual();
		if (dhEvento != null) {
			dhAtual = dhEvento;
		}

		int nrMaxRNC = configuracoesFacade.
				incrementaParametroSequencial(filialUsuario.getIdFilial(), "NR_NAO_CONFORMIDADE", true).intValue();
		Integer nrNaoConformidade = Integer.valueOf(nrMaxRNC);
		
		if (onc == null){
			onc = new OcorrenciaNaoConformidade();
		}
		NaoConformidade naoConformidade = storeNaoConformidadeByRnc(idDoctoServico, 
				idClienteDestinatarioNc, idClienteRemetenteNc, filialUsuario, dhAtual, nrNaoConformidade,onc,dsMotivoAbertura, idAwb, tpModal, causadorRnc);

		onc.setBlCaixaReaproveitada(blCaixaReaproveitada);
		onc.setCausaNaoConformidade(causaNaoConformidadeService.findById(idCausaNaoConformidade));
		if (idControleCarga != null) {
			onc.setControleCarga(controleCargaService.findByIdInitLazyProperties(idControleCarga, false));
		}
		onc.setDescricaoPadraoNc(descricaoPadraoNcService.findById(idDescricaoPadraoNc));
		onc.setDhInclusao(dhAtual);
		onc.setDsCaixaReaproveitada(dsCaixaReaproveitada);
		onc.setDsCausaNc(dsCausaNc);
		onc.setDsOcorrenciaNc(dsOcorrenciaNc);
		if (idEmpresa != null) {
			onc.setEmpresa(empresaService.findById(idEmpresa));
		}
		onc.setFilialByIdFilialAbertura(filialUsuario);
		onc.setFilialByIdFilialLegado(filialUsuario);
		onc.setFilialByIdFilialResponsavel(filialService.findById(idFilialByFilialResponsavel));
		if (idManifesto != null) {
			onc.setManifesto(manifestoService.findByIdInitLazyProperties(idManifesto, false));
		}
		onc.setMotivoAberturaNc(motivoAberturaNcService.findById(idMotivoAberturaNc));
		/**
		*Regra para abertura de RNC (Troca, Falta e Avaria de mercadorias) - A RNC para estes 3 motivos sï¿½
		*pode ser aberta para as filiais envolvidas no percurso. Ex.: POA x CXJ x Sï¿½O
		*somente estas filiais que podem abrir a RNC e registrar, falta, avaria ou troca de mercadoria. 
		*Nos casos de RNC de sobra, qualquer filial pode abrir.
		* Onde:
		*O id 22 corresponde a 'Troca'
		*Os ids 13,16,17,23 correspondem a 'Falta' 
		*O id 15 corresponde a 'Avaria de Mercadoria'
		*Os ids 24 e 14 correspondem a 'Sobra'
		*/
		boolean isSobra= false;
		String[] idsMotivoAberturaNcSobra = (getParametroGeralService().findConteudoByNomeParametro("ID_MOTIVO_ABERTURA_NC_SOBRA", false)).toString().split(",");
		for (String idMotivoANc : idsMotivoAberturaNcSobra) {
				if(idMotivoAberturaNc == Integer.parseInt(idMotivoANc)){
					isSobra=true;
					break;
					//sempre que tiver idControleCargar busca a rota da filial
					//quando nï¿½o tiver somente a filial do conhecimento pode abrir RNC
				}		
		}
		if (!isSobra && idControleCarga != null) {
				ControleCarga cc = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
			if (cc.getTpControleCarga() != null && !"C".equals(cc.getTpControleCarga().getValue()) &&
					!getFilialRotaService().existsFilialRotaByIdControleCarga(idControleCarga, filialUsuario.getIdFilial())) {
					throw new BusinessException("LMS-05069");

				}
				}
		onc.setNaoConformidade(naoConformidade);
		if (listaNotaOcorrenciaNcs != null) {
			for (Iterator iter = listaNotaOcorrenciaNcs.iterator(); iter.hasNext();) {
				NotaOcorrenciaNc notaOcorrenciaNc = (NotaOcorrenciaNc)iter.next();
				notaOcorrenciaNc.setOcorrenciaNaoConformidade(onc);
			}
		}
		Integer nrMaxOcorrencia = this.findMaxNrOcorrenciaNcByNaoConformidade(naoConformidade.getIdNaoConformidade());
		onc.setNotaOcorrenciaNcs(listaNotaOcorrenciaNcs);
		onc.setNrOcorrenciaNc(nrMaxOcorrencia);
		onc.setNrRncLegado(nrNaoConformidade);
		onc.setQtVolumes(qtVolumes);

		DomainValue estadoAtualTPStatusOcorrenciaNc =
				onc.getTpStatusOcorrenciaNc() == null ?
						domainValueService.findDomainValueByValue(DM_STATUS_OCORRENCIA_NC, "A") :
						domainValueService.findDomainValueByValue(DM_STATUS_OCORRENCIA_NC, onc.getTpStatusOcorrenciaNc().getValue());

		onc.setTpStatusOcorrenciaNc(estadoAtualTPStatusOcorrenciaNc);
		onc.setUsuario(SessionUtils.getUsuarioLogado());
		if (vlOcorrenciaNc != null) {
			onc.setVlOcorrenciaNc(vlOcorrenciaNc);
			if (idMoeda != null) {
				onc.setMoeda(moedaService.findById(idMoeda));
			}
		}
		
		//Persiste a ocorrenciaNaoConformidade...
		Long idOcorrenciaNaoConformidade = (Long) store(onc);
		
		/*
		 * LMS-3240 
		 * Se motivo de NF (MOTIVO_ABERTURA_NC.ID_MOTIVO_ABERTURA_NC IN (30,31,32,33,34,35)): - Gerar registros na tabela ITEM_OCORRENCIA_NC,
		 * conforme os itens (aba Itens da NF-e) que possuam o campo 'Qtde. Informada' informado
		 */
		
		
		if (CollectionUtils.isNotEmpty(itensNFe)) {
				saveItemOcorrenciaNc(itensNFe, onc);
			}
		
		if("AGP".equals(naoConformidade.getTpStatusNaoConformidade().getValue())){
			List parametro1202 = new ArrayList();
			parametro1202.add(naoConformidade.getFilial().getSgFilial() + " " + 
                    FormatUtils.fillNumberWithZero(naoConformidade.getNrNaoConformidade().toString(), ConsGeral.PAD_SIZE));
			
			this.getWorkflowPendenciaService().generatePendencia(
	    			SessionUtils.getFilialSessao().getIdFilial(), 
	    			ConstantesWorkflow.NR1202_APROVAR_RNC_PARA_DOCUMENTO_DE_SERVICO,
	    			idOcorrenciaNaoConformidade,
	    			this.getConfiguracoesFacade().getMensagem("LMS-12020", parametro1202.toArray()),
	    			JTDateTimeUtils.getDataHoraAtual());
		}
		
		if (idDoctoServico != null) {
			DoctoServico doctoServico = doctoServicoService.findDoctoServicoById(idDoctoServico);

            String strDocumento = doctoServico.getFilialByIdFilialOrigem().getSgFilial() + " " +
                    StringUtils.leftPad(doctoServico.getNrDoctoServico().toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
			incluirEventosRastreabilidadeInternacionalService.
					generateEventoDocumento(ConstantesSim.EVENTO_RNC_ABERTA, 
											idDoctoServico, 
											filialUsuario.getIdFilial(), 
											strDocumento, 
											dhAtual, 
											null, 
											filialUsuario.getSiglaNomeFilial(), 
											doctoServico.getTpDocumentoServico().getValue());
		}
		
		//Gerar workflow...
		if (onc.getVlOcorrenciaNc()!=null) {	
			BigDecimal vlElevadoRNC = new BigDecimal(this.getConfiguracoesFacade().getValorParametro("VALOR_ELEVADO_RNC").toString());
			if (onc.getVlOcorrenciaNc().compareTo(vlElevadoRNC) > INT_VAL_POS_0) {
				List parametro = new ArrayList();
				
				//Captura parametros...
		    	parametro.add(naoConformidade.getFilial().getSgFilial() + " " + 
                        FormatUtils.fillNumberWithZero(naoConformidade.getNrNaoConformidade().toString(), ConsGeral.PAD_SIZE));
		    	
		    	parametro.add(
		    			onc.getMoeda().getSgMoeda() 
		    			+ " " +  
		    			onc.getMoeda().getDsSimbolo() 
		    			+ " " + 
		    			onc.getVlOcorrenciaNc());
				
				this.getWorkflowPendenciaService().generatePendencia(
		    			SessionUtils.getFilialSessao().getIdFilial(), 
		    			ConstantesWorkflow.NR1201_VALOR_ELEVADO_RNC,
		    			idOcorrenciaNaoConformidade,
		    			this.getConfiguracoesFacade().getMensagem("LMS-12018", parametro.toArray()),
		    			JTDateTimeUtils.getDataHoraAtual());
			}
		}
		
		if (isSobra) {
			String dsProcesso = naoConformidade.getFilial().getSgFilial()
					+ " "
					+ FormatUtils.fillNumberWithZero(naoConformidade
                    .getNrNaoConformidade().toString(), ConsGeral.PAD_SIZE);
	    	
			this.getWorkflowPendenciaService().generatePendencia(
	    			SessionUtils.getFilialSessao().getIdFilial(), 
	    			ConstantesWorkflow.NR1203_VALOR_ELEVADO_RNC,
	    			idOcorrenciaNaoConformidade,
	    			this.getConfiguracoesFacade().getMensagem("LMS-04535", new String[]{dsProcesso}),
	    			JTDateTimeUtils.getDataHoraAtual());
		}
		
		return idOcorrenciaNaoConformidade;
	}
	
	private void saveItemOcorrenciaNc(List<TypedFlatMap> itensNFe, OcorrenciaNaoConformidade onc) {
		Double qtNaoConformidade;
		Double vlNaoConformidade;
		List<ItemOcorrenciaNc> itemsItemOcorrenciaNcs = new ArrayList<ItemOcorrenciaNc>();
		
		if(!itensNFe.isEmpty()) {
			for(TypedFlatMap m : itensNFe) {
				if (m.getLong("itensNFe.qtdInformada") != null && m.getLong("itensNFe.qtdInformada") != INT_VAL_POS_0) {
					qtNaoConformidade = Double.valueOf(m.getString("itensNFe.qtdInformada"));
					vlNaoConformidade = Double.valueOf(m.getString("itensNFe.valor")) / Double.valueOf(m.getString("itensNFe.qtdeAnterior")) * qtNaoConformidade;
					
					ItemOcorrenciaNc itemOcorrenciaNc = new ItemOcorrenciaNc();
					itemOcorrenciaNc.setNrChave(m.getString("itensNFe.id"));
					itemOcorrenciaNc.setNrItem(m.getLong("itensNFe.item"));
					itemOcorrenciaNc.setQtInformada(qtNaoConformidade.longValue());
					itemOcorrenciaNc.setQtNaoConformidade(qtNaoConformidade.longValue());
					itemOcorrenciaNc.setVlNaoConformidade(vlNaoConformidade);
					itemOcorrenciaNc.setOcorrenciaNaoConformidade(onc);					
					itemsItemOcorrenciaNcs.add(itemOcorrenciaNc);
				}
			}
		}
		
		if(!itemsItemOcorrenciaNcs.isEmpty()) {
			itemOcorrenciaNcService.storeAll(itemsItemOcorrenciaNcs);
		}
	}
	
	/**
	 * Workflow utilizado no processo de abrir RNC e Manter Nï¿½o Conformidade. Nï¿½o realiza nenhuma tarefa.
	 * Workflow da mesma e utlizado apenas para visualizacao. 
	 * 
     * @param idsRNC              List
     * @param tpSituacoesWorkflow List
	 */
	public String executeWorkflow(List<Long> idsRNC, List<String> tpSituacoesWorkflow) {
		if (idsRNC != null && tpSituacoesWorkflow != null) {
			if (idsRNC.size() != tpSituacoesWorkflow.size()) {
				return configuracoesFacade.getMensagem("LMS-01110");
			}
			for (int i = INT_VAL_POS_0; i < idsRNC.size(); i++) {
				Long idRNC = idsRNC.get(i);
				String tpSituacaoAprovacao = tpSituacoesWorkflow.get(i);
				OcorrenciaNaoConformidade oco  = findById(idRNC);
				NaoConformidade nc = oco.getNaoConformidade();
				if ("A".equals(tpSituacaoAprovacao)) {
					nc.setTpStatusNaoConformidade(new DomainValue("RNC"));
					
				} else if("R".equals(tpSituacaoAprovacao)) {
					nc.setTpStatusNaoConformidade(new DomainValue("CAN"));
					
				}
				store(oco);
			}
		}
		return null;
	}
	

	/**
	 * @param idDoctoServico
	 * @param idClienteDestinatarioNc
	 * @param idClienteRemetenteNc
	 * @param filialUsuario
	 * @param dhAtual
	 * @param nrNaoConformidade
	 * @return
	 */
	private NaoConformidade storeNaoConformidadeByRnc(Long idDoctoServico, Long idClienteDestinatarioNc, 
			Long idClienteRemetenteNc, Filial filialUsuario, DateTime dhAtual, Integer nrNaoConformidade,OcorrenciaNaoConformidade onc,String dsMotivoAbertura,
													  Long idAwb, String tpModal, String causadorRnc) {
		NaoConformidade naoConformidade = null;
		
		if( idDoctoServico != null ){
			naoConformidade = naoConformidadeService.findByIdDoctoServico(idDoctoServico);
			if( naoConformidade != null ){
				this.setTpStatusNaoConformidade(naoConformidade, idDoctoServico);
				return naoConformidade;
			}
		}
		
		naoConformidade = new NaoConformidade();
		
		if (idClienteDestinatarioNc != null) {
			naoConformidade.setClienteByIdClienteDestinatario(clienteService.findByIdInitLazyProperties(idClienteDestinatarioNc, false));
		}
		if (idClienteRemetenteNc != null) {
			naoConformidade.setClienteByIdClienteRemetente(clienteService.findByIdInitLazyProperties(idClienteRemetenteNc, false));
		}
		naoConformidade.setDhEmissao(null);
		naoConformidade.setDhInclusao(dhAtual);
		naoConformidade.setFilial(filialUsuario);
		naoConformidade.setIdNaoConformidade(null);
		naoConformidade.setNrNaoConformidade(nrNaoConformidade);
		naoConformidade.setDsMotivoAbertura(dsMotivoAbertura);
		
		if(idAwb != null){
			Awb awb = new Awb();
			awb.setIdAwb(idAwb);
			naoConformidade.setAwb(awb);
		}
		
		DomainValue dv = new DomainValue(tpModal);
		naoConformidade.setTpModal(dv);
		
		
		if (StringUtils.isNotBlank(causadorRnc)) {
			naoConformidade.setCausadorRnc(new DomainValue(causadorRnc));
		}
		
		if (idDoctoServico != null) {
			this.setTpStatusNaoConformidade(naoConformidade, idDoctoServico);
		}else{
			naoConformidade.setTpStatusNaoConformidade(new DomainValue("RNC"));
		}
		
		naoConformidade.setIdNaoConformidade((Long)naoConformidadeService.store(naoConformidade));
		
		return naoConformidade;
	}

	
	private void setTpStatusNaoConformidade(NaoConformidade naoConformidade,
			Long idDoctoServico) {
		DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		naoConformidade.setDoctoServico(doctoServico);
		
		short locMercadoria = INT_VAL_POS_1;
		if(doctoServico.getLocalizacaoMercadoria() == null || CompareUtils.neNull(doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria(), locMercadoria)){
			naoConformidade.setTpStatusNaoConformidade(ConstantesRnc.DM_TP_STATUS_NAO_CONFORMIDADE_RECIBO);
		}else{
			naoConformidade.setTpStatusNaoConformidade(ConstantesRnc.DM_TP_STATUS_NAO_CONFORMIDADE_AGUARDANDO_APROVACAO);
		}
		
	}

	/**
	 * @param idManifesto
	 * @param idControleCarga
	 * @return
	 */
	public List<Filial> findFilialResponsavelNC(Long idManifesto, Long idControleCarga) {
		List<Filial> result = new ArrayList();
		Filial filialUsuario = SessionUtils.getFilialSessao();
		if (idManifesto != null && !LongUtils.ZERO.equals(idManifesto)) {
			Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, true);
			//Regra 1.3.1
			if ("P".equals(manifesto.getFilialByIdFilialDestino().getEmpresa().getTpEmpresa().getValue())) {
				result.add(manifesto.getFilialByIdFilialOrigem());
				result.add(manifesto.getFilialByIdFilialDestino());
				return result;
			}
			
			//Regra 1.3.2
			if (manifesto.getFilialByIdFilialOrigem().getIdFilial().equals(filialUsuario.getIdFilial())){
				result.add(filialUsuario);
				return result;
			}
			
			//Regra 1.3.3
			Long idFilialRetornada = getFilialOrigemResponsavel(idManifesto, filialUsuario.getIdFilial());
			if (idFilialRetornada != null) {
				Filial filialRetornada = filialService.findById(idFilialRetornada);
				if (!filialRetornada.getIdFilial().equals(filialUsuario.getIdFilial())) {
					result.add(filialRetornada);
			}
			}
		} else if (idControleCarga != null && !LongUtils.ZERO.equals(idControleCarga)) {
			ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, true);
			Filial filialDestino = controleCarga.getFilialByIdFilialDestino();
			Filial filialOrigem = controleCarga.getFilialByIdFilialOrigem();
			String tpEmpresaDestino = filialDestino.getEmpresa().getTpEmpresa().getValue();
			if (ConstantesMunicipios.TP_EMPRESA_PARCEIRO.equals(tpEmpresaDestino)) {
				Hibernate.initialize(filialOrigem);
				result.add(filialOrigem);
				result.add(filialDestino);
				return result;
		} 

			if (filialUsuario.getIdFilial().equals(filialOrigem.getIdFilial())) {
				result.add(filialUsuario);
				return result;
	}

			Filial filialControleCarga = findFilialControleCarga(controleCarga);
	
			if (filialControleCarga != null && !filialControleCarga.getIdFilial().equals(filialUsuario.getIdFilial())) {
				boolean adicionaFilialOrigem = isFilialOrigemResponsavelControleCarga(controleCarga.getFilialByIdFilialDestino().getIdFilial(),controleCarga.getIdControleCarga(),filialUsuario.getIdFilial());
				if(adicionaFilialOrigem){					
					result.add(filialControleCarga);
				}
			}
		}

		result.add(filialUsuario);
		return result;
	}
	
	private boolean isFilialOrigemResponsavelControleCarga(Long idFilialDestino, Long idControleCarga,Long idFilialOrigem) {
		List<CarregamentoDescarga> list = getCarregamentoDescargaService().findCarregamentoDescargaByIdFilialByIdControleCarga(idFilialDestino, idControleCarga);
		if (!CollectionUtils.isEmpty(list)) {
			CarregamentoDescarga carregamentoDescarga = list.get(INT_VAL_POS_0);
			if (carregamentoDescarga.getDhFimOperacao() == null) {
				return true;
			}

			long diferencaEmMillis = JTDateTimeUtils.getDataHoraAtual().getMillis() - carregamentoDescarga.getDhFimOperacao().getMillis();
			BigDecimal diferencaEmHoras = new BigDecimal(diferencaEmMillis);
			diferencaEmHoras = diferencaEmHoras.divide(new BigDecimal(3600000), 6, BigDecimal.ROUND_HALF_EVEN);
			
            BigDecimal tempoMaximo = (BigDecimal) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialOrigem, TEMPO_DESCARGA_RNC, false);
			if (diferencaEmHoras.compareTo(tempoMaximo) < INT_VAL_POS_0) {
				return true;
			}
		}
		return false;
	}
		
	
	/**
	 * Verifica se o RNC estï¿½ sendo aberto na descarga e, em caso positivo, verifica se ï¿½ possï¿½vel atribuir
	 * a responsabilidade da nï¿½o conformidade ï¿½ filial de origem do manifesto. Se for, retorna um idFilial caso contrï¿½rio,
	 * retorna nulo. 
	 * 
	 * @param idManifesto
	 * @param idFilialUsuario
	 * @return
	 */
	private Long getFilialOrigemResponsavel(Long idManifesto, Long idFilialUsuario) {
		Manifesto manifesto = manifestoService.findByIdInitLazyProperties(idManifesto, false);
		Long idFilialOrigemManifesto = manifesto.getFilialByIdFilialOrigem().getIdFilial();

		Map mapDescargaManifesto = new HashMap();
		Map mapCarregamentoDescarga = new HashMap();
		Map mapFilial = new HashMap();
		Map mapManifesto = new HashMap();

		mapFilial.put("idFilial", idFilialUsuario);
		mapCarregamentoDescarga.put("filial", mapFilial);
		mapManifesto.put("idManifesto", idManifesto);
		mapDescargaManifesto.put("carregamentoDescarga", mapCarregamentoDescarga);
		mapDescargaManifesto.put("manifesto", mapManifesto);

		List listaDescargaManifesto = descargaManifestoService.find(mapDescargaManifesto);
		if (listaDescargaManifesto.isEmpty()) {
			return null;
		}
		DescargaManifesto descargaManifesto = (DescargaManifesto) listaDescargaManifesto.get(INT_VAL_POS_0);
		if (descargaManifesto.getDhFimDescarga() == null) {
			return idFilialOrigemManifesto;
		}

		long diferencaEmMillis = JTDateTimeUtils.getDataHoraAtual().getMillis() - descargaManifesto.getDhFimDescarga().getMillis();
		BigDecimal diferencaEmHoras = new BigDecimal(diferencaEmMillis);
		diferencaEmHoras = diferencaEmHoras.divide(new BigDecimal(3600000), 6, BigDecimal.ROUND_HALF_EVEN);
		
        BigDecimal tempoMaximo = (BigDecimal) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, TEMPO_DESCARGA_RNC, false);
		if (diferencaEmHoras.compareTo(tempoMaximo) < INT_VAL_POS_0) {
			return idFilialOrigemManifesto;
		}
		return null;
	}

    /**
     * Retorna as ocorrencias de nao conformidade de acordo com o idNaoConformidade.
	 *
     * @param idNaoConformidade
     * @return
     * @author luisfco
     */
    public List findOcorrenciasByIdNaoConformidade(Long idNaoConformidade) {
    	return getOcorrenciaNaoConformidadeDAO().findOcorrenciasByIdNaoConformidade(idNaoConformidade, null);
    }
    
    /**
	 * LMS-3240
	 * <p>
	 * Busca todas as ocorrencias de acordo com o id da nao conformidade e os
	 * id's do motivo de abertura, que vï¿½o compor o IN da consulta
	 * 
	 * @param idNaoConformidade
	 * @param idMotivoAberturaNc
	 * @return
	 * @author WagnerFC
	 */
    public List<OcorrenciaNaoConformidade> findOcorreciasByIdNaoConformidadeAndMotivoAberturaNc(Long idNaoConformidade, Integer... idMotivoAberturaNc) {
    	return getOcorrenciaNaoConformidadeDAO().findOcorreciasByIdNaoConformidadeAndMotivoAberturaNc(idNaoConformidade, idMotivoAberturaNc);
    }
    
    /**
     * Solicitaï¿½ï¿½o CQPRO00004838 da integraï¿½ï¿½o.
     * Retorne uma instancia da classe OcorrenciaNaoConformidade conforme os parametros especificados.
	 *
     * @param nrRncLegado
     * @param filialByIdFilialLegado
     * @return
     */
    public OcorrenciaNaoConformidade findByNrRncLegadoIdFilialByIdFilialLegado( String nrRncLegado, String filialByIdFilialLegado ) {
    	return getOcorrenciaNaoConformidadeDAO().findByNrRncLegadoIdFilialByIdFilialLegado(Integer.valueOf(nrRncLegado), Long.valueOf(filialByIdFilialLegado));
    }
    
    
    /**
     * Localiza uma lista de resultados a partir dos critérios de busca
     * informados. Permite especificar regras de ordenação
     * 
     * @param criteria       Critérios de busca.
     * @param campoOrdenacao com criterios de ordenação. Deve ser uma java.lang.String no formato
     * 	<code>nomePropriedade:asc</code> ou <code>associacao_.nomePropriedade:desc</code>.
     *                       A utilizaçãoo de <code>asc</code> ou <code>desc</code> é opcional sendo o padrão <code>asc</code>.
     * @return Lista de resultados sem paginação
     */ 
    public List findListByCriteria(Map criteria, List campoOrdenacao) {
    	return getOcorrenciaNaoConformidadeDAO().findListByCriteria(criteria, campoOrdenacao);
    }

    /**
     * Obtï¿½m a menor Ocorrencia de Nï¿½o Conformidade relacionada ï¿½ nï¿½o conformidade 
	 *
     * @param idNaoConformidade
     * @return
     */
    public OcorrenciaNaoConformidade findFirstOcorrenciaByIdNaoConformidade(Long idNaoConformidade) {
    	return getOcorrenciaNaoConformidadeDAO().findFirstOcorrenciaByIdNaoConformidade(idNaoConformidade);
    }

    public Integer getRowCountOcorrenciasByIdNaoConformidade(Long idNaoConformidade) {
    	return getOcorrenciaNaoConformidadeDAO().getRowCountOcorrenciasByIdNaoConformidade(idNaoConformidade);    	
    }
    
    /**
     * seleciona todas as ocorrencias associadas a nao conformidade
	 *
     * @param idNaoConformidade
     * @return
     */
    public List findPaginatedOcorrenciaNaoConformidade(Long idNaoConformidade){
    	return lmrncdao.findPaginatedOcorrenciaNaoConformidade(idNaoConformidade);
    }
    
    /**
     * <p>Faz a validacao do PCE. Retorna uma <code>TypedFlatMap</code> contendo</p>
     * 
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @return
     */
    public TypedFlatMap validatePCE(Long idClienteRemetente, Long idClienteDestinatario) {
    	
		TypedFlatMap result = new TypedFlatMap();
		Cliente clienteRemetente = null;
		Cliente clienteDestinatario = null;
		
		if (idClienteRemetente!=null) {
			clienteRemetente = this.getClienteService().findByIdInitLazyProperties(idClienteRemetente, false);
		}
		if (idClienteDestinatario!=null) {
			clienteDestinatario = this.getClienteService().findByIdInitLazyProperties(idClienteDestinatario, false);
		}
		
		if (clienteRemetente!=null) {
			result.put("remetente", this.getVersaoDescritivoPceService()
					.validatePCE(clienteRemetente.getIdCliente(),
							Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_RNC), 
			    			Long.valueOf(EventoPce.ID_EVENTO_PCE_EMISSAO_RNC),
			    			Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_GERACAO_RNC_EMISSAO_RNC)));
		}
		
		if (clienteDestinatario!=null) {
			result.put("destinatario", this.getVersaoDescritivoPceService()
					.validatePCE(clienteDestinatario.getIdCliente(),
							Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_RNC), 
			    			Long.valueOf(EventoPce.ID_EVENTO_PCE_EMISSAO_RNC),
			    			Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_GERACAO_RNC_EMISSAO_RNC)));
		}
		
		return result;
    }
    
    
    public OcorrenciaNaoConformidade findByIdCustom(Long id) {
    	return getOcorrenciaNaoConformidadeDAO().findByIdCustom(id);
	}
    
    
    /**
     * Verifica se existe alguma ocorrï¿½ncia aberta para o idNaoConformidade recebido por parï¿½metro.
	 *
     * @param idNaoConformidade
     * @return True se existe, caso contrï¿½rio, False.
     */
    public Boolean validateExisteOcorrenciaAbertaByIdNaoConformidade(Long idNaoConformidade) {
    	return !getOcorrenciaNaoConformidadeDAO().findOcorrenciasByIdNaoConformidade(idNaoConformidade, "A").isEmpty();
    }
    
    
    public List findOcorrenciasNaoConformidadeAbertas(Long idNaoConformidade) {
    	return getOcorrenciaNaoConformidadeDAO().findOcorrenciasNaoConformidadeAbertas(idNaoConformidade);
    }
    
	/**
	 * Busca todas as Filiais responsï¿½veis pelas OcorrenciasNaoConformidade de um dado DoctoServico.
	 *
	 * @param idDoctoServico
	 * @return
	 */
    public List findFiliaisResponsaveisOcorrenciasNaoConformidade(Long idDoctoServico){
    	return getOcorrenciaNaoConformidadeDAO().findFiliaisResponsaveisOcorrenciasNaoConformidade(idDoctoServico);
    }
    
	/**
	 * Busca uma List de OcorrenciaNaoConformidade que possuem disposiï¿½ï¿½o
	 * que por sua vez possui um motivo com BL_SOMENTE_AUTOMATICO = 'S'
	 *
	 * @param idDoctoServico
	 * @return
	 */
	public List findOcorrenciasNaoConformidadeSomenteAutomatico(Long idDoctoServico){
		return getOcorrenciaNaoConformidadeDAO().findOcorrenciasNaoConformidadeSomenteAutomatico(idDoctoServico);
	}
	
	/**
	 * LMS - 4460
	 * Busca e retorna as Ocorrï¿½ncias de RNCs dos tipos Falta Total e Falta Parcial,
	 * existentes nos documentos oriundos do Controle de Carga passado por parï¿½metro
	 *
	 * @param idControleCarga Identificador do controle de carga
	 * @param tpScan Identificaï¿½ï¿½o do tipo de Picking dos volumes
	 * @return Lista de Ocorrï¿½ncias de RNCs
	 */
	public List<OcorrenciaNaoConformidade> buscaRNCsAptasEncerramento(Long idControleCarga, String[] tpScan) {
		String listaTipoRNC = null;
        Object parametros = parametroGeralService.findConteudoByNomeParametro(TP_RNC_AUTO_ENCERRAR, false);
		
		if(parametros != null) {
			listaTipoRNC = parametros.toString();
		}
		
		return getOcorrenciaNaoConformidadeDAO().buscaRNCsAptasEncerramento(idControleCarga, tpScan, listaTipoRNC);
	}
	
	public void fechaOcoRNC(List<OcorrenciaNaoConformidade> listaOcoRNCsAptasEncerramento, Long idControleCarga) {
        fechaOcoRNC(listaOcoRNCsAptasEncerramento, idControleCarga, null);
    }
		
    public void fechaOcoRNC(List<OcorrenciaNaoConformidade> listaOcoRNCsAptasEncerramento, Long idControleCarga, AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations) {

        //Otimizar com batch em lote

        if (parametroGeralService.findConteudoByNomeParametro(ConsParametroGeral.MOTIVO_FECHAMENTO_RNC_AUTOMATICA, false) == null) {
			return;
		}
		
        Long idMotivoFechamento = ((BigDecimal) parametroGeralService.findConteudoByNomeParametro(ConsParametroGeral.MOTIVO_FECHAMENTO_RNC_AUTOMATICA, false)).longValue();
		
			MotivoDisposicao motivoFechamento = new MotivoDisposicao();
			motivoFechamento.setIdMotivoDisposicao(idMotivoFechamento);
			
			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findDadosFechamentoOcorrenciaRNCs(idControleCarga);
        String tipoOperacao;
			
		if ("C".equals(carregamentoDescarga.getTpOperacao().getValue())) {
			if ("C".equals(carregamentoDescarga.getControleCarga().getTpControleCarga().getValue())) {
					tipoOperacao = "Carregamento de Coleta/Entrega";
				} else {
					tipoOperacao = "Carregamento de Viagem";
				}
			} else {
			if ("C".equals(carregamentoDescarga.getControleCarga().getTpControleCarga().getValue())) {
					tipoOperacao = "Descarga de Coleta/Entrega";
				} else {
					tipoOperacao = "Descarga de Viagem";
				}
			}
			
			StringBuilder ocorrenciaAuxiliar = new StringBuilder();
			
        String mensagemDisposicao = "Encerrada durante o processo de " + tipoOperacao + " pois foi identificado que todos os volumes foram lidos fisicamente, após a abertura da RNC.";

			for (OcorrenciaNaoConformidade ocorrenciaNaoConformidade : listaOcoRNCsAptasEncerramento) {
            storeDisposicaoEOcorrenciaNC(adsmNativeBatchSqlOperations, motivoFechamento, ocorrenciaAuxiliar, mensagemDisposicao, ocorrenciaNaoConformidade);
			}
			
		// Remove a ultima virgula
        ocorrenciaAuxiliar.deleteCharAt(ocorrenciaAuxiliar.length() - 1);
			
		List<NaoConformidade> listaNC = naoConformidadeService.findByOcorrenciasNaoConformidadeWithOcorrenciaEmAberto(ocorrenciaAuxiliar.toString());
			
			for (NaoConformidade naoConformidade : listaNC) {
            String nrNaoConformidade = naoConformidade.getFilial().getSgFilial() + " " + StringUtils.leftPad(naoConformidade.getNrNaoConformidade().toString(), ConsGeral.PAD_SIZE, ConsGeral.PAD_CHAR);
					if(naoConformidade.getDoctoServico() != null && naoConformidade.getDoctoServico().getIdDoctoServico() != null) {
				// Chamar a rotina Gerar Evento Documento Servico
                if (adsmNativeBatchSqlOperations != null) {
                    incluirEventosRastreabilidadeInternacionalService.generateEventoDocumentoComBatch(
                            ConstantesSim.EVENTO_RNC_ENCERRADA,
                            naoConformidade.getDoctoServico().getIdDoctoServico(),
                            SessionUtils.getFilialSessao().getIdFilial(),
                            nrNaoConformidade,
                            JTDateTimeUtils.getDataHoraAtual(),
                            "RNC",
                            adsmNativeBatchSqlOperations);
                } else {
						incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
								ConstantesSim.EVENTO_RNC_ENCERRADA,
								naoConformidade.getDoctoServico().getIdDoctoServico(),
								SessionUtils.getFilialSessao().getIdFilial(),
								nrNaoConformidade,
								JTDateTimeUtils.getDataHoraAtual(),
								"RNC");
					}
				}

			}
    }
	
    private void storeDisposicaoEOcorrenciaNC(AdsmNativeBatchSqlOperations adsmNativeBatchSqlOperations, MotivoDisposicao motivoFechamento, StringBuilder ocorrenciaAuxiliar, String mensagemDisposicao, OcorrenciaNaoConformidade ocorrenciaNaoConformidade) {
        ocorrenciaAuxiliar.append(ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade()).append(",");

        if (adsmNativeBatchSqlOperations != null) {
            Map<String, Object> divisaoKeyValueMap = new HashMap<String, Object>();
            divisaoKeyValueMap.put(ConsDisposicao.SQL_ID_OCORRENCIA_NAO_CONFORMIDADE, ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade());
            divisaoKeyValueMap.put(ConsDisposicao.SQL_ID_FILIAL, SessionUtils.getFilialSessao().getIdFilial());
            divisaoKeyValueMap.put(ConsDisposicao.SQL_ID_MOTIVO_DISPOSICAO, motivoFechamento.getIdMotivoDisposicao());
            divisaoKeyValueMap.put(ConsDisposicao.SQL_ID_USUARIO, SessionUtils.getUsuarioLogado().getIdUsuario());
            divisaoKeyValueMap.put(ConsDisposicao.SQL_DH_DISPOSICAO, JTDateTimeUtils.getDataHoraAtual());
            divisaoKeyValueMap.put(ConsDisposicao.SQL_DH_DISPOSICAO_TZR, SessionUtils.getFilialSessao().getDateTimeZone().getID());
            divisaoKeyValueMap.put(ConsDisposicao.SQL_DS_DISPOSICAO, mensagemDisposicao);

            adsmNativeBatchSqlOperations.addNativeBatchInsert(ConsDisposicao.SQL_DISPOSICAO, divisaoKeyValueMap);

            if (!adsmNativeBatchSqlOperations.checkIfCurrentUpdateTableExist(ConsOcorrenciaNaoConformidade.TN_OCORRENCIA_NAO_CONFORMIDADE, UPDATE_ONC)) {
                adsmNativeBatchSqlOperations.addBatchUpdateCriteriaValues(
                        ConsOcorrenciaNaoConformidade.TN_OCORRENCIA_NAO_CONFORMIDADE,
                        ConsOcorrenciaNaoConformidade.SQL_TP_STATUS_OCORRENCIA_NC,
                        ConsOcorrenciaNaoConformidade.DM_TP_STATUS_OCORRENCIA_NC_FECHADO,
                        UPDATE_ONC);
            }

            adsmNativeBatchSqlOperations.addBatchUpdateCriteriaByTableIdValues(
                    ConsOcorrenciaNaoConformidade.TN_OCORRENCIA_NAO_CONFORMIDADE,
                    ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade(),
                    UPDATE_ONC);

        } else {

            Disposicao disposicao = new Disposicao();
            disposicao.setOcorrenciaNaoConformidade(ocorrenciaNaoConformidade);
            disposicao.setFilial(SessionUtils.getFilialSessao());
            disposicao.setMotivoDisposicao(motivoFechamento);
            disposicao.setUsuario(SessionUtils.getUsuarioLogado());
            //LMS 6964
            disposicao.setDhDisposicao(JTDateTimeUtils.getDataHoraAtual());
            disposicao.setDsDisposicao(mensagemDisposicao);

            ocorrenciaNaoConformidade.setTpStatusOcorrenciaNc(new DomainValue(ConsOcorrenciaNaoConformidade.DM_TP_STATUS_OCORRENCIA_NC_FECHADO));
            // Inserir um registro na tabela DISPOSICAO
            disposicaoService.store(disposicao);
            // Realiza o fechamento da Ocorrencia
            this.store(ocorrenciaNaoConformidade);
        }
    }

	public List<Map<String, Object>> executeVerificacaoPossibilidadeCriacaoRNCautomatica(Long idControleCarga, Long idFilial){
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = new ArrayList<Map<String, Object>>();
        String[] tiposRNC = parametroGeralService.findConteudoByNomeParametro(TP_RNC_AUTO_ENCERRAR, false).toString().split(";");
		List<Map<String, Object>> maps = removeDuplicatedResults(getOcorrenciaNaoConformidadeDAO().buscaDocumentosComVolumesNaoCarregados(idControleCarga, SessionUtils.getFilialSessao().getIdFilial(), ENTREGA_REALIZADA, Arrays.asList(tiposRNC)));
		for (Map<String, Object> map : maps) {
            List<Map<String, Object>> volumesNaoManifestados = getOcorrenciaNaoConformidadeDAO().buscaVolumesNaoManifestados(getIdFromMap(ID_NOTA_FISCAL_CONHECIMENTO, map), idFilial, getIdFromMap(ID_MANIFESTO, map), Arrays.asList(ENTREGA_REALIZADA), false);
			listaDocVolmRNCcriacaoAutomatica.addAll(volumesNaoManifestados);
		}
		return listaDocVolmRNCcriacaoAutomatica;
	}
	
	public List<Map<String, Object>> executeVerificacaoPossibilidadeCriacaoRNCautomaticaDescarga(Long idControleCarga, Long idFilial){
        String[] tiposRNC = parametroGeralService.findConteudoByNomeParametro(TP_RNC_AUTO_ENCERRAR, false).toString().split(";");
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = getOcorrenciaNaoConformidadeDAO().buscaDocumentosComVolumesNaoDescarregados(idControleCarga, SessionUtils.getFilialSessao().getIdFilial(), Arrays.asList(AGUARDANDO_DESCARGA, EM_DESCARGA, MERCADORIA_RETORNADA_EM_DESCARGA, MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA), Arrays.asList(tiposRNC));
		return listaDocVolmRNCcriacaoAutomatica;
	}
	
	public String getRncAutomaticaMessage(List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica){
		if(listaDocVolmRNCcriacaoAutomatica != null && !listaDocVolmRNCcriacaoAutomatica.isEmpty()){
			StringBuilder messageBuilder = new StringBuilder();
			for (Map<String, Object> map : listaDocVolmRNCcriacaoAutomatica) {
				String doctoServico = buildDoctoServico(map);
				if (messageBuilder.indexOf(doctoServico) >= INT_VAL_POS_0) {
					messageBuilder.append(", ");
					buildQtVolumes(messageBuilder, map);
				}else {
					messageBuilder.append(doctoServico).append(" Vol: ");
					buildQtVolumes(messageBuilder, map);
				}
				messageBuilder.append(" ");
			}
			return messageBuilder.toString();
		}
		return null;
	}
	
	
	private String getStringFromMap(String key, Map<String, Object> map) {
		return map.get(key) != null ? (String)map.get(key) : "";
	}

	private void buildQtVolumes(StringBuilder messageBuilder, Map<String, Object> map) {
        messageBuilder.append(getIdFromMap(NR_SEQUENCIA, map)).append("/").append(getIdFromMap(QT_VOLUMES, map));
	}
	
	private String buildDoctoServico(Map<String, Object> map) {
        return getStringFromMap(TP_DOCUMENTO_SERVICO, map) + "-" + getStringFromMap(SG_FILIAL, map) + getIdFromMap(NR_DOCTO_SERVICO, map);
	}
	
	private Long getIdFromMap(String key, Map<String, Object> map){
		return map.get(key) != null ? (Long) map.get(key) : null;
	}
	
	private List<Map<String, Object>> removeDuplicatedResults(List<Map<String, Object>> maps) {
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomaticaNotDuplicated = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map: maps) {
            if (!checkIfExists(getIdFromMap(ID_NOTA_FISCAL_CONHECIMENTO, map), getIdFromMap(ID_MANIFESTO, map), listaDocVolmRNCcriacaoAutomaticaNotDuplicated)) {
				listaDocVolmRNCcriacaoAutomaticaNotDuplicated.add(map);
			}
		}
		return listaDocVolmRNCcriacaoAutomaticaNotDuplicated;
	}
	
	private void buildListNotaOcorrenciaNC(Map<String, ArrayList> mapListaNotaOcorrenciaNcs, List<Map<String, Object>> maps) {
	    List<Map<String, Object>> listaNotasDocRNCAutomatica = removeDuplicatedResults(maps);
        for (Map<String, Object> map: listaNotasDocRNCAutomatica) {
            Long idDoctoSerivco = (Long)map.get("ID_DOCTO_SERVICO");
            if (!mapListaNotaOcorrenciaNcs.containsKey(idDoctoSerivco.toString())) {
                mapListaNotaOcorrenciaNcs.put(idDoctoSerivco.toString(), new ArrayList<NotaFiscalConhecimento>());
            }
            
            NotaFiscalConhecimento nfConhecimento = notaFiscalConhecimentoService.findById(getIdFromMap(ID_NOTA_FISCAL_CONHECIMENTO, map));
            NotaOcorrenciaNc notaOcorrenciaNc = new NotaOcorrenciaNc();
            notaOcorrenciaNc.setNrNotaFiscal(nfConhecimento.getNrNotaFiscal());
            notaOcorrenciaNc.setNotaFiscalConhecimento(nfConhecimento);
            mapListaNotaOcorrenciaNcs.get(idDoctoSerivco.toString()).add(notaOcorrenciaNc);
        }
    }
	
	private boolean checkIfExists(Long idNotaFiscaConhecimento, Long idManifesto, List<Map<String, Object>> listaDocVolmRNCcriacaoAutomaticaNotDuplicated) {
		for (Map<String, Object> map : listaDocVolmRNCcriacaoAutomaticaNotDuplicated) {
            if (getIdFromMap(ID_NOTA_FISCAL_CONHECIMENTO, map).equals(idNotaFiscaConhecimento) && getIdFromMap(ID_MANIFESTO, map).equals(idManifesto)) {
				return true;
			}
		}
		return false;
	}

	public void executeCriacaoRNCautomaticaCarregamento(Long idControleCarga, Long idFilialResponsavel, List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica, boolean isDescarga){
        String[] motRncAutomaticaSplited = parametroGeralService.findByNomeParametro(ConsParametroGeral.CHAVES_RNC_AUTOMATICA).getDsConteudo().split(";");
		listaDocVolmRNCcriacaoAutomatica = putQtVolumesTotal(listaDocVolmRNCcriacaoAutomatica);
		listaDocVolmRNCcriacaoAutomatica = groupListByIdDoctoServico(listaDocVolmRNCcriacaoAutomatica);

		Map<String, ArrayList> mapListaNotaOcorrenciaNcs = getListNotaOcorrenciaNcs(idControleCarga, idFilialResponsavel);
        
        for (Map<String, Object> map : listaDocVolmRNCcriacaoAutomatica) {
            Long idDoctoServico = Long.parseLong(map.get(ConsDoctoServico.SQL_ID_DOCTO_SERVICO) + "");
			
			if(isDescarga){
                Manifesto manifesto = manifestoService.findById(Long.parseLong(getStringFromMap(ConsManifesto.SQL_ID_MANIFESTO, map)));
				idFilialResponsavel = manifesto.getFilialByIdFilialOrigem().getIdFilial();
			}
			
			storeRNC(null, idDoctoServico, Long.parseLong(motRncAutomaticaSplited[INT_VAL_POS_1]), idControleCarga,
					null, Long.parseLong(motRncAutomaticaSplited[INT_VAL_POS_0]), idFilialResponsavel, Long.parseLong(motRncAutomaticaSplited[INT_VAL_POS_2]), Long.parseLong(motRncAutomaticaSplited[INT_VAL_POS_3]),
					createDsOcorrenciaNc(map), Boolean.FALSE, null, null, new BigDecimal(INT_VAL_POS_0), getQtVolumes(map),
					null, null, mapListaNotaOcorrenciaNcs.get(idDoctoServico.toString()), parametroGeralService.findByNomeParametro("MOT_RNC_AUTOMATICA").getDsConteudo(), null, null, findTpModal(idDoctoServico), null,  ConstantesRnc.TP_MODAL_CAUSADOR_FILIAL, null);
		}
	}

    private Map<String, ArrayList> getListNotaOcorrenciaNcs(Long idControleCarga, Long idFilialResponsavel) {
        Map<String, ArrayList> mapListaNotaOcorrenciaNcs = new HashMap<String, ArrayList>();
        
        String[] tiposRNC = parametroGeralService.findConteudoByNomeParametro(TP_RNC_AUTO_ENCERRAR, false).toString().split(";");
        buildListNotaOcorrenciaNC(mapListaNotaOcorrenciaNcs, getOcorrenciaNaoConformidadeDAO().buscaDocumentosComVolumesNaoDescarregados(idControleCarga, idFilialResponsavel,Arrays.asList(AGUARDANDO_DESCARGA, EM_DESCARGA, MERCADORIA_RETORNADA_EM_DESCARGA, MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA), Arrays.asList(tiposRNC)));
        if(mapListaNotaOcorrenciaNcs.isEmpty()){
            buildListNotaOcorrenciaNC(mapListaNotaOcorrenciaNcs, getOcorrenciaNaoConformidadeDAO().buscaDocumentosComVolumesNaoCarregados(idControleCarga, idFilialResponsavel, ENTREGA_REALIZADA, Arrays.asList(tiposRNC)));
            if(mapListaNotaOcorrenciaNcs.isEmpty()){
                buildListNotaOcorrenciaNC(mapListaNotaOcorrenciaNcs, getOcorrenciaNaoConformidadeDAO().buscaDocumentosComVolumesSobra(idControleCarga));
            }
        }
        return mapListaNotaOcorrenciaNcs;
    }
	
	private String findTpModal(Long idDoctoServico) {
		String modal = "R";
		
		if(idDoctoServico != null){
			List<Awb> awbs = ctoAwbService.findCtoAwbBydDoctoServico(idDoctoServico);
			if(!awbs.isEmpty()){
				modal = "A";
			}
		}
		return modal;
	}
	
	public void executeCriacaoRNCautomaticaDescarregamentoComSobra(Long idControleCarga, Long idFilialResponsavel, List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica) {
        String[] motRncAutomaticaSplited = parametroGeralService.findByNomeParametro(ConsParametroGeral.CHAVES_RNC_AUTOMATICA).getDsConteudo().split(";");
		listaDocVolmRNCcriacaoAutomatica = putQtVolumesTotal(listaDocVolmRNCcriacaoAutomatica);
		listaDocVolmRNCcriacaoAutomatica = groupListByIdDoctoServico(listaDocVolmRNCcriacaoAutomatica);
		
		Map<String, ArrayList> mapListaNotaOcorrenciaNcs = getListNotaOcorrenciaNcs(idControleCarga, idFilialResponsavel);
		
		for (Map<String, Object> map : listaDocVolmRNCcriacaoAutomatica) {
            Long idDoctoServico = Long.parseLong(map.get(ConsDoctoServico.SQL_ID_DOCTO_SERVICO) + "");

			storeRNC(null, idDoctoServico, Long.parseLong(motRncAutomaticaSplited[5]), idControleCarga, 
					null, Long.parseLong(motRncAutomaticaSplited[4]), idFilialResponsavel, Long.parseLong(motRncAutomaticaSplited[6]), Long.parseLong(motRncAutomaticaSplited[INT_VAL_POS_3]),
					createDsOcorrenciaNc(map), Boolean.FALSE, null, null, new BigDecimal(INT_VAL_POS_0), getQtVolumes(map),
					null, null, mapListaNotaOcorrenciaNcs.get(idDoctoServico.toString()), parametroGeralService.findByNomeParametro("MOT_RNC_SOBRA_AUTOMATICA").getDsConteudo(), null, null, findTpModal(idDoctoServico), null,  ConstantesRnc.TP_MODAL_CAUSADOR_FILIAL, null);
		}
	}
	
	private int getQtVolumes(Map<String, Object> map){
        return StringUtils.countMatches(getStringFromMap(NR_SEQUENCIA, map), "/");
	}
	
	private List<Map<String, Object>> putQtVolumesTotal(List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica) {
		for (Map<String, Object> map : listaDocVolmRNCcriacaoAutomatica) {
            String nrSequenciaAndQtVolumes = map.get(NR_SEQUENCIA) + "/" + map.get(QT_VOLUMES);
            map.put(NR_SEQUENCIA, nrSequenciaAndQtVolumes);
		}
		return listaDocVolmRNCcriacaoAutomatica;
	}
	
	private List<Map<String, Object>> groupListByIdDoctoServico(List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica) {
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomaticaGrouped = new ArrayList<Map<String,Object>>();
		for (Map<String, Object> map : listaDocVolmRNCcriacaoAutomatica) {
			groupOnList(map, listaDocVolmRNCcriacaoAutomaticaGrouped);
		}
		return listaDocVolmRNCcriacaoAutomaticaGrouped;
	}
	
	private void groupOnList(Map<String, Object> map, List<Map<String, Object>> listaDocVolmRNCcriacaoAutomaticaGrouped) {
        String idDoctoServico = map.get(ConsDoctoServico.SQL_ID_DOCTO_SERVICO) + "";
		for (Map<String, Object> doctoServicoMap : listaDocVolmRNCcriacaoAutomaticaGrouped) {
            String idDoctoServicoMap = doctoServicoMap.get(ConsDoctoServico.SQL_ID_DOCTO_SERVICO) + "";
			if(idDoctoServicoMap.equals(idDoctoServico)){
                String nrSequenciaGrouped = doctoServicoMap.get(NR_SEQUENCIA) + ", " + map.get(NR_SEQUENCIA);
				if(nrSequenciaGrouped.length() >= NR_SEQUENCIA_MAX_SIZE){
					return;
				}
                doctoServicoMap.put(NR_SEQUENCIA, nrSequenciaGrouped);
				return;
			}
		}
		listaDocVolmRNCcriacaoAutomaticaGrouped.add(map);
	}
	
	private String createDsOcorrenciaNc(Map<String, Object> map) {
		StringBuilder dsOcorrenciaNc = new StringBuilder("Doc: ");
        dsOcorrenciaNc.append(getStringFromMap(TP_DOCUMENTO_SERVICO, map));
		dsOcorrenciaNc.append("-");
        dsOcorrenciaNc.append(getStringFromMap(SG_FILIAL, map));
        dsOcorrenciaNc.append(map.get(NR_DOCTO_SERVICO));
		dsOcorrenciaNc.append(" Vol(s): ");
        dsOcorrenciaNc.append(map.get(NR_SEQUENCIA));
		return dsOcorrenciaNc.toString();
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public void setFilialRotaService(FilialRotaService filialRotaService) {
		this.filialRotaService = filialRotaService;
	}

	public FilialRotaService getFilialRotaService() {
		return filialRotaService;
	}	

	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}	

	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}

	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}	
	
	public void setItemOcorrenciaNcService(ItemOcorrenciaNcService itemOcorrenciaNcService) {
		this.itemOcorrenciaNcService = itemOcorrenciaNcService;
	}

	public CtoAwbService getCtoAwbService() {
		return ctoAwbService;
	}

	public void setCtoAwbService(CtoAwbService ctoAwbService) {
		this.ctoAwbService = ctoAwbService;
	}

    public void setNotaFiscalConhecimentoService(
            NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
        this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
    }
}
