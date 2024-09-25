package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.model.util.AdsmHibernateUtils;
import com.mercurio.adsm.framework.model.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.CarregamentoPreManifesto;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.DescargaManifesto;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;
import com.mercurio.lms.carregamento.model.Equipe;
import com.mercurio.lms.carregamento.model.EquipeOperacao;
import com.mercurio.lms.carregamento.model.EventoControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.carregamento.model.dao.ManifestoDAO;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.TipoServico;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.EventoMeioTransporteService;
import com.mercurio.lms.entrega.ConstantesEntrega;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaDocumento;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.entrega.model.service.NotaFiscalOperadaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoInternacional;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.ManifestoInternacionalService;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.HorarioPrevistoSaidaRota;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.SubstAtendimentoFilial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.OperacaoServicoLocalizaService;
import com.mercurio.lms.municipios.model.service.SubstAtendimentoFilialService;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sgr.model.SmpManifesto;
import com.mercurio.lms.sgr.model.service.SmpManifestoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.SolicitacaoRetirada;
import com.mercurio.lms.sim.model.service.EventoDispositivoUnitizacaoService;
import com.mercurio.lms.sim.model.service.EventoVolumeService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.sim.model.service.SolicitacaoRetiradaService;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.VersaoDescritivoPceService;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.manifestoService"
 */
public class ManifestoService extends CrudService<Manifesto, Long> {
	private DomainValueService domainValueService;
	private DoctoServicoService doctoServicoService;
	private PedidoColetaService pedidoColetaService;
	private PreManifestoDocumentoService preManifestoDocumentoService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private ConfiguracoesFacade configuracoesFacade;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private EventoManifestoService eventoManifestoService;
	private SubstAtendimentoFilialService substAtendimentoFilialService;
	private ManifestoEntregaService manifestoEntregaService;
	private ManifestoInternacionalService manifestoInternacionalService;
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoColetaService manifestoColetaService;
	private VersaoDescritivoPceService versaoDescritivoPceService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private CarregamentoPreManifestoService carregamentoPreManifestoService;
	private SmpManifestoService smpManifestoService;
	private PreManifestoVolumeService preManifestoVolumeService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ConhecimentoService conhecimentoService;
	private DispCarregIdentificadoService dispCarregIdentificadoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private SolicitacaoRetiradaService solicitacaoRetiradaService;
	private ControleCargaService controleCargaService;
	private ClienteService clienteService;
	private AgendamentoEntregaService agendamentoEntregaService;
	private EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService;
	private DescargaManifestoService descargaManifestoService;
	private EquipeOperacaoService equipeOperacaoService;
	private EquipeService equipeService;
	private EventoMeioTransporteService eventoMeioTransporteService;
	private EventoControleCargaService eventoControleCargaService;
	private EventoVolumeService eventoVolumeService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private FilialService filialService;
	private NotaFiscalOperadaService notaFiscalOperadaService;

	private static final String SCAN_LMS = "LM";
	private static final String SIM = "Sim";
	private static final String NAO = "Não";

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEventoDispositivoUnitizacaoService(
			EventoDispositivoUnitizacaoService eventoDispositivoUnitizacaoService) {
		this.eventoDispositivoUnitizacaoService = eventoDispositivoUnitizacaoService;
	}

	public boolean validateManifestoParceira(Manifesto manifesto) {
		return (new DomainValue("EP")).equals(manifesto.getTpManifestoEntrega())
				&& !(new DomainValue("CA").equals(manifesto.getTpStatusManifesto()));
	}

	public boolean validateManifestoAberto(Manifesto manifesto) {
		return !(new DomainValue("FE")).equals(manifesto.getTpStatusManifesto());
	}

	public boolean validateManifestoFechado(Manifesto manifesto) {
		return (new DomainValue("FE")).equals(manifesto.getTpStatusManifesto());
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
	public void setPreManifestoVolumeService(PreManifestoVolumeService preManifestoVolumeService) {
		this.preManifestoVolumeService = preManifestoVolumeService;
	}
	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setOperacaoServicoLocalizaService(OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setPreManifestoDocumentoService(PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}
	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
	public void setEventoManifestoService(EventoManifestoService eventoManifestoService) {
		this.eventoManifestoService = eventoManifestoService;
	}
	public void setSubstAtendimentoFilialService(SubstAtendimentoFilialService substAtendimentoFilialService) {
		this.substAtendimentoFilialService = substAtendimentoFilialService;
	}
	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}
	public void setManifestoInternacionalService(ManifestoInternacionalService manifestoInternacionalService) {
		this.manifestoInternacionalService = manifestoInternacionalService;
	}
	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	public void setVersaoDescritivoPceService(VersaoDescritivoPceService versaoDescritivoPceService) {
		this.versaoDescritivoPceService = versaoDescritivoPceService;
	}
	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
	public void setDispositivoUnitizacaoService(DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}
	public void setCarregamentoPreManifestoService(CarregamentoPreManifestoService carregamentoPreManifestoService) {
		this.carregamentoPreManifestoService = carregamentoPreManifestoService;
	}


	public void setSmpManifestoService(SmpManifestoService smpManifestoService) {
		this.smpManifestoService = smpManifestoService;
	}
	/**
	 * Recupera uma instância de <code>Manifesto</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Manifesto findById(java.lang.Long id) {
		return (Manifesto)super.findById(id);
	}

	public Manifesto findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (Manifesto)super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}

	/**
	 * Recupera uma instância de <code>Manifesto</code> a partir do ID.
	 *
	 * @param idManifesto representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Manifesto findManifestoById(Long idManifesto) {
		return this.getManifestoDAO().findManifestoById(idManifesto);
	}

	/**
	 * ComboBox para emissao do Manifesto.
	 * @author Andre Valadas
	 * @param criteria
	 * @return
	 */
	public List findComboPreManifestoInit(TypedFlatMap criteria) {

		AdsmHibernateUtils.setFlushModeToManual(this.getManifestoDAO().getAdsmHibernateTemplate());
		List comboPreManifestoList = this.getManifestoDAO().findComboPreManifestoInit(criteria);
		AdsmHibernateUtils.setFlushModeToAuto(this.getManifestoDAO().getAdsmHibernateTemplate());

		return comboPreManifestoList;

	}

	public Long findManifestosByIdControleCarga(Long idControleCarga) {
		return this.getManifestoDAO().findManifestosByIdControleCarga(idControleCarga);
	}

	public List<Map<String, Object>> findManifestosEmCarregamentoEConcluidoByIdControleCarga(Long idControleCarga) {
		return this.getManifestoDAO().findManifestosEmCarregamentoEConcluidoByIdControleCarga(idControleCarga);
	}

	/**
	 * Método que retorna o ResultSetPage de Manifestos.
	 * @param criteria
	 * @return
	 */
	public ResultSetPage findPaginatedManifesto(TypedFlatMap criteria) {
		return this.getManifestoDAO().findPaginatedManifesto(criteria, FindDefinition.createFindDefinition(criteria));
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findManifestosEntregaColetaViagem(String tpDocumento, Long idFilial, Integer nrManifesto) {
		List<Map<String, Object>> lista = new ArrayList<Map<String,Object>>();
		if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_VIAGEM.equals(tpDocumento)) {
			List<ManifestoViagemNacional> manifestos = manifestoViagemNacionalService.findByNrManifestoOrigemByFilial(nrManifesto, idFilial);
			for (ManifestoViagemNacional manifestoViagemNacional : manifestos) {
				Map<String, Object> mapManifesto = new HashMap<String, Object>();
				mapManifesto.put("idManifesto", manifestoViagemNacional.getIdManifestoViagemNacional());
				mapManifesto.put("nrManifesto", manifestoViagemNacional.getNrManifestoOrigem());
				lista.add(mapManifesto);
			}
		} else if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_ENTREGA.equals(tpDocumento)) {
			List<ManifestoEntrega> manifestos = manifestoEntregaService.findByNrManifestoByFilial(nrManifesto, idFilial);
			for (ManifestoEntrega manifestoEntrega : manifestos) {
				Map<String, Object> mapManifesto = new HashMap<String, Object>();
				mapManifesto.put("idManifesto", manifestoEntrega.getIdManifestoEntrega());
				mapManifesto.put("nrManifesto", manifestoEntrega.getNrManifestoEntrega());
				lista.add(mapManifesto);
			}
		} else if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_COLETA.equals(tpDocumento)) {
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("filial.idFilial", idFilial);
			parametros.put("nrManifesto", nrManifesto);
			List<ManifestoColeta> manifestos = manifestoColetaService.find(parametros);
			for (ManifestoColeta manifestoColeta : manifestos) {
				Map<String, Object> mapManifesto = new HashMap<String, Object>();
				mapManifesto.put("idManifesto", manifestoColeta.getIdManifestoColeta());
				mapManifesto.put("nrManifesto", manifestoColeta.getNrManifesto());
				lista.add(mapManifesto);
			}
		}
		if (lista.isEmpty()) {
			throw new BusinessException("LMS-00061");
		}
		//Conforme o DB estas buscas são em unique, por isso Get(0).
		return lista.get(0);
	}

	/**
	 * Método que retorna a queantidade de manifestos no ResultSetPage.
	 * @param criteria
	 * @return
	 */
	public Integer getRowCountManifesto(TypedFlatMap criteria) {
		return this.getManifestoDAO().getRowCountManifesto(criteria);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param idManifesto indica a entidade que deverá ser removida.
	 */
	public void removeById(Long idManifesto) {
		Manifesto manifesto = findManifestoById(idManifesto);
		boolean blGeraEventoVolume = verificaGeraEventoVolume(manifesto);

		if("E".equals(manifesto.getTpManifesto().getValue()) && "CR".equals(manifesto.getTpManifestoEntrega().getValue())){
			SolicitacaoRetirada solicitacao = manifesto.getSolicitacaoRetirada();
			if(solicitacao != null){
				solicitacao.setTpSituacao(new DomainValue("A"));
				solicitacaoRetiradaService.store(solicitacao);
			}
		}

		if (manifesto.getControleCarga() != null) {
			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(manifesto.getControleCarga().getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial(), "F", "C");
			if (carregamentoDescarga != null) {
				// Valida se e viavel esta remocao...
				if (carregamentoDescarga.getDhFimOperacao() != null) {
					throw new BusinessException("LMS-05146");
				}
			}
		}

		// Remove os Eventos do manifesto.
		eventoManifestoService.removeByIdManifesto(manifesto.getIdManifesto());

		List<SmpManifesto> smpsManifesto = manifesto.getSmpManifestos();
		if(smpsManifesto != null) {
			for(SmpManifesto smpManifesto : smpsManifesto) {
				smpManifestoService.removeById(smpManifesto.getIdSmpManifesto());
			}
		}

		//Gera evento para os volumes do PréManifestoVolume e remove o PreManifestoVolume
		List<PreManifestoVolume> preManifestoVolumeList = preManifestoVolumeService.findbyIdManifesto(idManifesto);
		final LocalizacaoMercadoria localizacaoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(ConstantesSim.CD_MERCADORIA_NO_TERMINAL);
		for(PreManifestoVolume preManifestoVolume : preManifestoVolumeList ){
			final VolumeNotaFiscal volumeNotaFiscal = preManifestoVolume.getVolumeNotaFiscal();
			if(blGeraEventoVolume){
				volumeNotaFiscalService.generateEventoParaVolumeRemovidoDoPreManifestoVolume(idManifesto,preManifestoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal(), SCAN_LMS );
			}
			volumeNotaFiscalService.executeReleaseVolume(volumeNotaFiscal, localizacaoTerminal);
			preManifestoVolumeService.removePreManifestoVolume(preManifestoVolume);

		}

		//Gera evento para os dispositivos do DispCarregIdentificado e remove o DispCarregIdentificado
		List<DispCarregIdentificado> listaDispositivo = dispCarregIdentificadoService.findDispCarregIdentificadoByIdManifesto(idManifesto);
		for(DispCarregIdentificado disp : listaDispositivo) {
			dispositivoUnitizacaoService.generateEventoParaDispositivoRemovidoDoPreManifestoVolume(idManifesto, disp.getDispositivoUnitizacao().getIdDispositivoUnitizacao(), SCAN_LMS);

			dispositivoUnitizacaoService.removeById(disp.getIdDispCarregIdentificado());
		}

		List<PreManifestoDocumento> listPreManifestoDocumento = preManifestoDocumentoService.findPreManifestoDocumentoByIdManifesto(idManifesto);
		for(PreManifestoDocumento preManifestoDocumento : listPreManifestoDocumento) {
			// Gera um evento doctoServico para o doctoServico em questao...
			preManifestoDocumentoService.generateEventoDoctoServicoByIdManifestoByIdDoctoServico(
					manifesto.getIdManifesto(), preManifestoDocumento.getDoctoServico().getIdDoctoServico());

			// Remove a relação do Pré-Manifesto com o Documento.
			preManifestoDocumentoService.removeById(preManifestoDocumento.getIdPreManifestoDocumento());
		}

		List<PreManifestoVolume> listVolumesSemPreManifDocto = preManifestoVolumeService.findVolumesSemPreManifDoctoByManifesto(idManifesto);
		if(listVolumesSemPreManifDocto != null) {
			preManifestoVolumeService.generateEventoVolumes(listVolumesSemPreManifDocto);
		}

		// Dispositivos retornam para a Filial.
		dispositivoUnitizacaoService.executeVoltarDispositivosUnitizacaoCarregados(idManifesto);

		// Remove todos os carregamentos pre manifestos
		for(CarregamentoPreManifesto carregamentoPreManifesto : manifesto.getCarregamentoPreManifestos()) {
			carregamentoPreManifestoService.removeById(carregamentoPreManifesto.getIdCarregamentoPreManifesto());
		}

		if (manifesto.getManifestoEntrega() != null) {
			// Remove o ManifestoEntrega e seu relacionamento com manifesto...
			ManifestoEntrega manifestoEntrega = manifesto.getManifestoEntrega();
			manifestoEntrega.setManifesto(null);
			manifesto.setManifestoEntrega(null);

			manifestoEntregaService.removeById(manifestoEntrega.getIdManifestoEntrega());
		} else if (manifesto.getManifestoInternacional() != null) {
			// Remove o ManifestoInternacional e seu relacionamento com manifesto...
			ManifestoInternacional manifestoInternacional = manifesto.getManifestoInternacional();
			manifestoInternacional.setManifesto(null);
			manifesto.setManifestoInternacional(null);

			manifestoInternacionalService.removeById(manifestoInternacional.getIdManifestoInternacional());
		} else if (manifesto.getManifestoViagemNacional() != null){
			// Remove o ManifestoInternacional e seu relacionamento com manifesto...
			ManifestoViagemNacional manifestoViagemNacional = manifesto.getManifestoViagemNacional();
			manifestoViagemNacional.setManifesto(null);
			manifesto.setManifestoViagemNacional(null);

			manifestoViagemNacionalService.removeById(manifestoViagemNacional.getIdManifestoViagemNacional());
		}

		super.removeById(idManifesto);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids) {
			this.removeById(id);
		}
	}

	//LMSA-3449
	public boolean verificaGeraEventoVolume(Manifesto manifesto) {
		if(manifesto.getIdManifesto() == null) return true;
		boolean blClienteNoLabelingPostoAvancado = hasClienteNoLabelingPostoAvancado(manifesto);
		boolean blViagem = manifesto.getControleCarga() != null && manifesto.getControleCarga().getTpControleCarga().getValue().equals("V");
		return !(blClienteNoLabelingPostoAvancado && blViagem);
	}

	private boolean hasClienteNoLabelingPostoAvancado(Manifesto manifesto) {
		boolean retorno = false;
		List<Long> idsClientes = findClientesRemetenteByIdManifesto(manifesto.getIdManifesto());
		if(CollectionUtils.isNotEmpty(idsClientes) && idsClientes.size() == 1){
			Cliente cliente = clienteService.findById(idsClientes.get(0));
			if((cliente.getBlLiberaEtiquetaEdi() != null && cliente.getBlLiberaEtiquetaEdi()) &&
					(cliente.getBlClientePostoAvancado() != null && cliente.getBlClientePostoAvancado()) &&
					(manifesto.getControleCarga() != null && cliente.getFilialByIdFilialAtendeOperacional().getIdFilial().equals(manifesto.getControleCarga().getFilialByIdFilialOrigem().getIdFilial()))){
				retorno = true;
			}
		}
		return retorno;
	}


	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public TypedFlatMap storeAll(Manifesto bean, ItemList itemsPreManifestoDocumento, ItemListConfig itemsConfigPreManifestoDocumento) {
		return this.storeAll(bean, itemsPreManifestoDocumento, itemsConfigPreManifestoDocumento, null, null);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param manifesto entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public TypedFlatMap storeAll(Manifesto manifesto, ItemList itemsPreManifestoDocumento, ItemListConfig itemsConfigPreManifestoDocumento,
								 ItemList itemsPreManifestoVolume, ItemListConfig itemsConfigPreManifestoVolume) {
		return storeAll(manifesto, itemsPreManifestoDocumento, itemsConfigPreManifestoDocumento, itemsPreManifestoVolume, itemsConfigPreManifestoVolume, null);
	}

	public TypedFlatMap storeAll(Manifesto manifesto, ItemList itemsPreManifestoDocumento, ItemListConfig itemsConfigPreManifestoDocumento,
								 ItemList itemsPreManifestoVolume, ItemListConfig itemsConfigPreManifestoVolume, List<Long> idsConferenciaEmLinha) {
		boolean rollbackMasterId = manifesto.getIdManifesto() == null;
		try {
			// Verifica se o tipo do manifesto de viagem é 'RV'.
			// Caso for, permite que o sistem prossiga sem incluir documentos.
			boolean blItem = true;
			if(hasRetornoVazio(manifesto)){
				blItem = false;
			}

			if ( (itemsPreManifestoDocumento == null || !itemsPreManifestoDocumento.hasItems()) && blItem ) {
				throw new BusinessException("LMS-05041");
			}

			if (manifesto.getControleCarga() != null) {
			    if(manifesto.getControleCarga().getTpControleCarga().getValue().equals("V")){
			        validateManifestoNoControleCarga(manifesto);
			        validateSolicitacaoContratacaoManifestoControleCarga(manifesto);
			    }
			}
			
			Integer ordem = Integer.valueOf(0);
			Long idManifesto = manifesto.getIdManifesto();
			if (idManifesto != null) {
				ordem = preManifestoDocumentoService.findMaxNrOrdemByIdManifesto(idManifesto);
			}

			// Regra incluída no dia 28 de setembro de 2006, conforme item 2.8 da DF 05.01.01.07.
			for(Iterator iter = itemsPreManifestoDocumento.iterator(manifesto.getIdManifesto(), itemsConfigPreManifestoDocumento); iter.hasNext();) {
				PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();

				DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();

				if(null == idsConferenciaEmLinha || !idsConferenciaEmLinha.contains(doctoServico.getIdDoctoServico())) {
					TypedFlatMap doctoValidoMap = validateDoctoServicoPreManifesto(preManifestoDocumento);
					if (doctoValidoMap != null) {
						itemsPreManifestoDocumento.rollbackItemsState();
						return doctoValidoMap;
					}
				}

				if (preManifestoDocumento.getIdPreManifestoDocumento().compareTo( Long.valueOf(0) ) < 0) {
					preManifestoDocumento.setNrOrdem(Integer.valueOf(++ordem));
				}

				if (doctoServico.getFilialByIdFilialDestino().getIdFilial().equals(manifesto.getFilialByIdFilialDestino().getIdFilial())){
					doctoServico.setFilialDestinoOperacional(doctoServico.getFilialByIdFilialDestino());
				} else {
					/*
					 * Corrigido erro de unique result, pois estava buscando mais de 1 resultado na consulta(hotfix)
					 */
					List<SubstAtendimentoFilial> substAtendimentoFilial = substAtendimentoFilialService.findSubstAtendimentoFilialByIdFilialDestinoAndFilialSubstituta(
							doctoServico.getFilialByIdFilialDestino().getIdFilial(),
							manifesto.getFilialByIdFilialDestino().getIdFilial());
					if (substAtendimentoFilial != null && !substAtendimentoFilial.isEmpty()) {
						Filial filialDestinoSubstituta = substAtendimentoFilialService.findFilialDestinoDoctoServico(doctoServico.getIdDoctoServico(), null, null, null);
						doctoServico.setFilialDestinoOperacional(filialDestinoSubstituta);
					} else {
						doctoServico.setFilialDestinoOperacional(doctoServico.getFilialByIdFilialDestino());
					}
				}
				if(!executarLiberacaoAgendamento(doctoServico)){
					doctoServicoService.store(doctoServico);
				}
			}

			// Pega o ultimo numero de coleta no banco
			Long nrPreManifestoFinal = null;
			if(manifesto.getNrPreManifesto() == null) {
				nrPreManifestoFinal = configuracoesFacade.incrementaParametroSequencial(manifesto.getFilialByIdFilialOrigem().getIdFilial(), "NR_PRE_MANIFESTO", true);

				manifesto.setNrPreManifesto(nrPreManifestoFinal);
			} else {
				nrPreManifestoFinal = manifesto.getNrPreManifesto();
			}

			//LMSA-2900
			if(hasRetornoVazio(manifesto) || !validateDocumentosDuplicadosControleCarga(itemsPreManifestoDocumento.getNewOrModifiedItems())){
				manifesto = storeManifesto(manifesto);

				/**
				 * trata os PreManifestoDocumento que estão na sessão
				 */
				geraEventoAndStoreListPreManifestoDocumentos(itemsPreManifestoDocumento.getNewOrModifiedItems(), manifesto);
			}

			geraEventoAndRemovePreManifestoDocumentos(itemsPreManifestoDocumento.getRemovedItems(), manifesto);

			/**
			 * trata os PreManifestoVolume que estão na sessão
			 */
			geraEventoAndStorePreManifestoVolume(itemsPreManifestoVolume.getNewOrModifiedItems());

			if(manifesto.getIdManifesto() == null){
				List<Manifesto> manifestos = this.findManifestoByIdControleCargaAndStatusAndFilialOrigem(manifesto.getControleCarga().getIdControleCarga(), SessionUtils.getFilialSessao().getIdFilial());
				if(CollectionUtils.isNotEmpty(manifestos)){
					manifesto = manifestos.get(0);
				}
			}

			removeItensNaoPersistidos(itemsPreManifestoDocumento, manifesto);

			TypedFlatMap mapBeanStored = new TypedFlatMap();
			mapBeanStored.put("idManifesto", manifesto.getIdManifesto());
			mapBeanStored.put("filialByIdFilialOrigem.idFilial", manifesto.getFilialByIdFilialOrigem().getIdFilial());
			mapBeanStored.put("filialByIdFilialOrigem.sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());
			mapBeanStored.put("nrPreManifesto", nrPreManifestoFinal);
			mapBeanStored.put("dhGeracaoPreManifesto", manifesto.getDhGeracaoPreManifesto());
			mapBeanStored.put("tpManifesto", manifesto.getTpManifesto().getValue());
			mapBeanStored.put("tpPreManifesto", manifesto.getTpManifestoEntrega() != null ? manifesto.getTpManifestoEntrega().getValue() : null);
			return mapBeanStored;
		} catch (RuntimeException e) {
			this.rollbackMasterState(manifesto, rollbackMasterId, e);
            
			if (itemsPreManifestoDocumento != null) {
			itemsPreManifestoDocumento.rollbackItemsState();
            }
            
            if (itemsPreManifestoVolume != null) {
			itemsPreManifestoVolume.rollbackItemsState();
            }
			
			throw e;
		}
	}

    private void validateSolicitacaoContratacaoManifestoControleCarga(Manifesto manifesto) {
        if(manifesto.getControleCarga().getSolicitacaoContratacao() != null){
            if(!manifesto.getControleCarga().getSolicitacaoContratacao().getTpModal().getValue().equals(manifesto.getTpModal().getValue())){
                throw new BusinessException("LMS-05423");
            }
        }
    }

	private boolean hasRetornoVazio(Manifesto manifesto) {
		return manifesto.getTpManifestoViagem() != null && manifesto.getTpManifestoViagem().getValue().equals("RV");
	}

	private boolean validateDocumentosDuplicadosControleCarga(List newOrModifiedItems) {

		if(newOrModifiedItems.isEmpty()){
			return false;
		}

		Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();
		int qtdExistentes = 0;

		for (Iterator iter = newOrModifiedItems.iterator(); iter.hasNext();) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();
			Manifesto manifesto = preManifestoDocumento.getManifesto();
			ControleCarga controleCarga = manifesto.getControleCarga();

			if (controleCarga != null && controleCargaService.validateExistePreManifestoDocumentoPreManifestoVolume(controleCarga.getIdControleCarga(), doctoServico.getIdDoctoServico(), idFilialUsuarioLogado)){
				qtdExistentes++;
			}
		}

		return qtdExistentes == newOrModifiedItems.size();
	}

	private void removeItensNaoPersistidos(ItemList itemsPreManifestoDocumento, Manifesto manifesto) {

		List<Object> listRemover = new ArrayList<Object>();
		for (Object item : itemsPreManifestoDocumento.getItems()) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) item;
			if(preManifestoDocumento.getIdPreManifestoDocumento() == null){
				listRemover.add(item);
			}
		}

		for (Object item : listRemover) {
			itemsPreManifestoDocumento.remove(item);
		}

	}

	private void validateManifestoNoControleCarga(Manifesto manifesto) {
		DomainValue tpModal = null;

		if (manifesto.getIdManifesto() == null) {
			tpModal = findTpModalByIdControleCargaIdManifesto(manifesto.getControleCarga().getIdControleCarga(), null, false);
		} else {
			tpModal = findTpModalByIdControleCargaIdManifesto(manifesto.getControleCarga().getIdControleCarga(), manifesto.getIdManifesto(), true);
		}

		if (tpModal != null && !tpModal.getValue().equals(manifesto.getTpModal().getValue())) {
			throw new BusinessException("LMS-05393", new Object[] { tpModal.getDescription().toString() });
		}
	}

	public TypedFlatMap validateDoctoServicoPreManifesto(
			PreManifestoDocumento preManifestoDocumento) {
		if( preManifestoDocumento.getIdPreManifestoDocumento() < 0 ){
			DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();
			String tbManifesto = preManifestoDocumento.getManifesto().getTpManifesto().getValue();
			BusinessException msg = preManifestoDocumentoService.validateLocalizacaoMercadoria(doctoServico, tbManifesto, null);
			if( msg != null ){
				TypedFlatMap mapBeanStored = new TypedFlatMap();
				mapBeanStored.put("idPreManifestoDocumento",preManifestoDocumento.getIdPreManifestoDocumento());
				mapBeanStored.put("errorMessage", msg.getMessageKey());
				mapBeanStored.put("documento", preManifestoDocumento.getDoctoServico()
						.getFilialByIdFilialOrigem().getSgFilial()
						+ " "
						+ preManifestoDocumento.getDoctoServico()
						.getNrDoctoServico().toString());
				return mapBeanStored;
			}
		}
		return null;
	}
	private boolean executarLiberacaoAgendamento(DoctoServico doctoServico) {
		if(doctoServico.getBlBloqueado() != null &&  doctoServico.getBlBloqueado()){
			OcorrenciaDoctoServico ocorrenciaDoctoServico =  ocorrenciaDoctoServicoService.findLastOcorrenciaDoctoServicoByIdDoctoServico(doctoServico.getIdDoctoServico());
			OcorrenciaPendencia ocorrenciaPendencia =  ocorrenciaDoctoServico.getOcorrenciaPendenciaByIdOcorBloqueio();
			if(ocorrenciaPendencia.getCdOcorrencia() == (short)203){

				OcorrenciaPendencia ocorrenciaPendenciaLiberacao =  ocorrenciaPendenciaService.findByCodigoOcorrencia((short)204);

				TypedFlatMap typedFlatMap = new TypedFlatMap();
				typedFlatMap.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
				typedFlatMap.put("ocorrenciaPendencia.blApreensao", ocorrenciaPendenciaLiberacao.getBlApreensao());
				typedFlatMap.put("ocorrenciaPendencia.idOcorrenciaPendencia",ocorrenciaPendenciaLiberacao.getIdOcorrenciaPendencia());
				typedFlatMap.put("ocorrenciaPendencia.evento.idEvento",ocorrenciaPendenciaLiberacao.getEvento().getIdEvento());
				typedFlatMap.put("ocorrenciaPendencia.tpOcorrencia",ocorrenciaPendenciaLiberacao.getTpOcorrencia().getValue());
				ocorrenciaDoctoServicoService.executeRegistrarOcorrenciaDoctoServico(typedFlatMap);

				return true;
			}else{
				throw new BusinessException("LMS-05050");
			}
		}
		return false;
	}

	/**
	 * Gera evento para o VolumeNotaFiscal e faz o store do PreManifestoVolume
	 */
	private void geraEventoAndStorePreManifestoVolume( List<PreManifestoVolume> listPreManifestoVolume){
		Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();
		
		for( PreManifestoVolume preManifestoVolume : listPreManifestoVolume ){

			DoctoServico doctoServico = preManifestoVolume.getDoctoServico();
			Manifesto manifesto = preManifestoVolume.getManifesto();
			ControleCarga controleCarga = manifesto.getControleCarga();

			if (controleCarga != null && 
			    controleCargaService.validateExistePreManifestoDocumentoPreManifestoVolume(
			                controleCarga.getIdControleCarga(), 
			                doctoServico.getIdDoctoServico(), 
			                idFilialUsuarioLogado) &&
			    !notaFiscalOperadaService.validateExistePreManifestoDocumentoPreManifestoVolume(
			                doctoServico.getIdDoctoServico(),
			                preManifestoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal())){
				continue;
			}

			Conhecimento conhecimento = conhecimentoService.findConhecimentoByIdVolumeNotaFiscal( preManifestoVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal() );
			preManifestoVolume.setDoctoServico(conhecimento);
			preManifestoVolume.setTpScan( new DomainValue(SCAN_LMS) );
			preManifestoVolumeService.store(preManifestoVolume);
			volumeNotaFiscalService.generateEventoParaVolumeAdicionadoNoPreManifestoVolume(preManifestoVolume,  SCAN_LMS);
		}
	}

	/**
	 * Método que salva um Manifesto
	 *
	 * @param manifesto
	 * @return Manifesto
	 */
	public Manifesto storeManifesto(Manifesto manifesto) {
		super.store(manifesto);
		super.flush();
		return manifesto;
	}

	/**
	 * Salva lista de PreManifestoDocumentos.
	 *
	 * @param newOrModifiedItems
	 */
	private void geraEventoAndStoreListPreManifestoDocumentos(List newOrModifiedItems, Manifesto manifestoAux) {

		boolean blGeraEventoVolume = verificaGeraEventoVolume(newOrModifiedItems, manifestoAux);

		Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();

		for (Iterator iter = newOrModifiedItems.iterator(); iter.hasNext();) {
			PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();
			Manifesto manifesto = preManifestoDocumento.getManifesto();
			String nrPreManifesto = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(manifesto.getNrPreManifesto().toString(), 8, '0');
			ControleCarga controleCarga = manifesto.getControleCarga();

			if (controleCarga != null && controleCargaService.validateExistePreManifestoDocumentoPreManifestoVolume(controleCarga.getIdControleCarga(), doctoServico.getIdDoctoServico(), idFilialUsuarioLogado)){
				continue;
			}

			if(manifesto.getTpManifesto().getValue().equals("V")) {
				Short[] cdEvento = null;
				if (manifesto.getTpStatusManifesto().getValue().equals("PM")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("EC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO, Short.valueOf("25")};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("CC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_VIAGEM_EMITIDO, Short.valueOf("25"), Short.valueOf("26")};
				}
				for (int i = 0; i < cdEvento.length; i++) {
					incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
							cdEvento[i], doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(),
							nrPreManifesto, JTDateTimeUtils.getDataHoraAtual(), null, SessionUtils.getFilialSessao().getSiglaNomeFilial(),
							"PMV");
				}
			} else if(manifesto.getTpManifesto().getValue().equals("E")) {
				Short[] cdEvento = null;
				if (manifesto.getTpStatusManifesto().getValue().equals("PM")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("EC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO, Short.valueOf("24")};
				} else if (manifesto.getTpStatusManifesto().getValue().equals("CC")) {
					cdEvento = new Short[]{ConstantesSim.EVENTO_PRE_MANIFESTO_ENTREGA_EMITIDO, Short.valueOf("24"), Short.valueOf("27")};
				}
				for (int i = 0; i < cdEvento.length; i++) {
					incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
							cdEvento[i], doctoServico.getIdDoctoServico(), SessionUtils.getFilialSessao().getIdFilial(),
							nrPreManifesto, JTDateTimeUtils.getDataHoraAtual(), null, SessionUtils.getFilialSessao().getSiglaNomeFilial(),
							"PME");
				}
			}

			preManifestoDocumentoService.storePreManifestoDocumento(preManifestoDocumento);

			/**
			 * gera evento para o VolumeNotaFiscal e faz store do PreManifestoVolume
			 */
			geraEventoAndStorePreManifestoVolume(preManifestoDocumento, blGeraEventoVolume);
		}
	}

	/**
	 * Para cada volume do documento gera evento para o VolumeNotaFisca e faz store para o PreManifestoVolume
	 * @param preManifestoDocumento
	 */
	private void geraEventoAndStorePreManifestoVolume(PreManifestoDocumento preManifestoDocumento, boolean blGeraEventoVolume){
		DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();

		Class classe = Hibernate.getClass(doctoServico);

		/*CQPRO00027518 - Se o documento desciçoes servico for reentrega  então considerar o docto de servico original*/
		if(Conhecimento.class.isAssignableFrom(classe)){
			Conhecimento conh = conhecimentoService.findByIdInitLazyProperties(doctoServico.getIdDoctoServico(), false);
			if(conh != null && "RE".equals(conh.getTpConhecimento().getValue())){
				doctoServico = conh.getDoctoServicoOriginal();
			}
		}

		List<VolumeNotaFiscal> volumeNotaFiscalList = volumeNotaFiscalService.findByIdConhecimento(preManifestoDocumento.getDoctoServico().getIdDoctoServico());
		List<PreManifestoVolume> preManifestoVolumesExistentes = preManifestoVolumeService.findByDoctoServicoAndManifesto(doctoServico.getIdDoctoServico(), preManifestoDocumento.getManifesto().getIdManifesto());

		for(VolumeNotaFiscal volumeNotaFiscal : volumeNotaFiscalList ){
			if(!volumeNotaFiscal.getTpVolume().equals(ConstantesExpedicao.TP_VOLUME_MESTRE) && 
			        !notaFiscalOperadaService.validateExistePreManifestoDocumentoPreManifestoVolume(
			                doctoServico.getIdDoctoServico() ,
			                volumeNotaFiscal.getIdVolumeNotaFiscal())) {
				PreManifestoVolume preManifestoVolumeToStore = null;

				for (PreManifestoVolume preManifestoVolume:preManifestoVolumesExistentes){
					if (preManifestoVolume.getVolumeNotaFiscal().equals(volumeNotaFiscal)){
						preManifestoVolumeToStore = preManifestoVolume;
						preManifestoVolumeToStore.setPreManifestoDocumento(preManifestoDocumento);
						break;
					}
				}

				if (preManifestoVolumeToStore == null){
					preManifestoVolumeToStore = new PreManifestoVolume();
					preManifestoVolumeToStore.setDoctoServico(doctoServico);
					preManifestoVolumeToStore.setPreManifestoDocumento(preManifestoDocumento);
					preManifestoVolumeToStore.setVolumeNotaFiscal(volumeNotaFiscal);
					preManifestoVolumeToStore.setManifesto(preManifestoDocumento.getManifesto());
					preManifestoVolumeToStore.setTpScan( new DomainValue(SCAN_LMS) ); /* CQPRO00024567 - Corrigido, faltava o Tipo Scan 'LM'*/
				}

				preManifestoVolumeService.store(preManifestoVolumeToStore);

				if(blGeraEventoVolume){
					volumeNotaFiscalService.generateEventoParaVolumeAdicionadoNoPreManifestoVolume( preManifestoVolumeToStore, SCAN_LMS);
				}
			}
		}
	}




	/**
	 * Remove uma lista de PreManifestoDocumentos.
	 *
	 * @param removeItems
	 */
	private void geraEventoAndRemovePreManifestoDocumentos(List removeItems, Manifesto manifestoAux) {

		boolean blGeraEventoVolume = verificaGeraEventoVolume(removeItems, manifestoAux);

		for (Iterator iter = removeItems.iterator(); iter.hasNext();) {
			final PreManifestoDocumento preManifestoDocumento = (PreManifestoDocumento) iter.next();
			final Manifesto manifesto = preManifestoDocumento.getManifesto();
			final DoctoServico doctoServico = preManifestoDocumento.getDoctoServico();

			/**
			 * antes de remover os pre manifesto documento remove e gera evento para os pre manifesto volume
			 */
			geraEventoAndRemovePremanifestoVolume(doctoServico.getIdDoctoServico(), manifesto.getIdManifesto(), blGeraEventoVolume);

			preManifestoDocumentoService.generateEventoDoctoServicoByIdManifestoByIdDoctoServico( manifesto.getIdManifesto(), doctoServico.getIdDoctoServico());
			preManifestoDocumentoService.removePreManifestoDocumento(preManifestoDocumento);
		}
	}

	//LMSA-3449
	public boolean verificaGeraEventoVolume(List items, Manifesto manifesto) {
		boolean blClienteNoLabelingPostoAvancado = false;
		blClienteNoLabelingPostoAvancado = hasClienteNoLabelingPostoAvancado(items, manifesto);
		boolean blViagem = manifesto.getControleCarga() != null && manifesto.getControleCarga().getTpControleCarga().getValue().equals("V");
		return !(blClienteNoLabelingPostoAvancado && blViagem);
	}

	private boolean hasClienteNoLabelingPostoAvancado(List items, Manifesto manifesto) {
		if(CollectionUtils.isEmpty(items)){
			return true;
		}
		Cliente cliente = null;
		Set<Long> idsClientes = new HashSet<Long>();

		for (Object item : items) {
			DoctoServico doctoServico = ((PreManifestoDocumento) item).getDoctoServico();
			idsClientes.add(doctoServico.getClienteByIdClienteRemetente().getIdCliente());
			cliente = doctoServico.getClienteByIdClienteRemetente();
		}
		cliente = clienteService.findById(cliente.getIdCliente());

		if(CollectionUtils.isNotEmpty(idsClientes) && idsClientes.size() == 1){
			if((cliente.getBlLiberaEtiquetaEdi() != null && cliente.getBlLiberaEtiquetaEdi()) &&
					(cliente.getBlClientePostoAvancado() != null && cliente.getBlClientePostoAvancado()) &&
					(manifesto.getControleCarga() != null && cliente.getFilialByIdFilialAtendeOperacional().getIdFilial().equals(manifesto.getControleCarga().getFilialByIdFilialOrigem().getIdFilial()))){
				return true;
			}
		}
		return false;
	}

	private void geraEventoAndRemovePremanifestoVolume(Long idDoctoServico, Long idManifesto, boolean blGeraEventoVolume){
		final List<PreManifestoVolume> preManifestoVolumeList = preManifestoVolumeService.findByDoctoServicoAndManifesto(idDoctoServico, idManifesto);
		for( PreManifestoVolume preManifestoVolume : preManifestoVolumeList ){
			final VolumeNotaFiscal volumeNotaFiscal = preManifestoVolume.getVolumeNotaFiscal();

			if(blGeraEventoVolume){
				volumeNotaFiscalService.generateEventoParaVolumeRemovidoDoPreManifestoVolume(idManifesto, volumeNotaFiscal.getIdVolumeNotaFiscal(), SCAN_LMS);
			}

			preManifestoVolumeService.removePreManifestoVolume(preManifestoVolume);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * Este store nao possui regras de negocio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable storeBasic(Manifesto bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param dao Instância do DAO.
	 */
	public void setManifestoDAO(ManifestoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private ManifestoDAO getManifestoDAO() {
		return (ManifestoDAO) getDao();
	}

	public void evict(Manifesto manifesto){
		getDao().getSessionFactory().getCurrentSession().evict(manifesto);
	}

	/**
	 * Localiza o último registro de Manifesto relacionado ao DoctoServico passado por parâmetro
	 *
	 * @return Instância de Manifesto
	 */
	public List<Manifesto> findManifestoByDataEmissao(Long idDoctoServico, Long idFilialUsuario) {
		List<Manifesto> lista = getManifestoDAO().findManifestoByDataEmissao(idDoctoServico, idFilialUsuario,null);
		return lista;
	}

	public List<Manifesto> findManifestoByDataEmissao(Long idDoctoServico, Long idFilialUsuario,String tpManifesto) {
		List<Manifesto> lista = getManifestoDAO().findManifestoByDataEmissao(idDoctoServico, idFilialUsuario,tpManifesto);
		return lista;
	}

	public List<Manifesto> findManifestoViagemFilialDestino(Long idDoctoServico, Long idFilialUsuario) {
		List<Manifesto> lista = getManifestoDAO().findManifestoViagemFilialDestino(idDoctoServico, idFilialUsuario);
		return lista;
	}

	public List<Manifesto> findManifestoEntregaFilialOrigem(Long idDoctoServico, Long idFilialUsuario) {
		List<Manifesto> lista = getManifestoDAO().findManifestoEntregaFilialOrigem(idDoctoServico, idFilialUsuario);
		return lista;
	}

	public List<Manifesto> findManifestoEntregaAereoFilialOrigem(Long idDoctoServico, Long idManifesto) {
		return getManifestoDAO().findManifestoEntregaAereoFilialOrigem(idDoctoServico, idManifesto);
	}

	/**
	 * Busca personalizada para o findById
	 *
	 * @param idManifesto
	 * @return
	 */
	public List<Map<String, Object>> findManifestoByRNC(Long idManifesto) {
		List<Map<String, Object>> lista = getManifestoDAO().findManifestoByRNC(idManifesto);
		if (lista.isEmpty()) {
			return null;
		}
		return AliasToNestedMapResultTransformer.getInstance().transformListResult(lista);
	}

	/**
	 * Retorna uma list de registros de Manifesto com o ID do Controle de Carga
	 *
	 * @param idControleCarga
	 * @return
	 */
	public List<Manifesto> findManifestoByIdControleCarga(Long idControleCarga, Long idFilialOrigem, String tpStatusManifesto, String tpManifesto) {
		return this.getManifestoDAO().findManifestoByIdControleCarga(idControleCarga, idFilialOrigem, tpStatusManifesto, tpManifesto);
	}

	
	public List<Manifesto> findManifestoEmAbertoByIdControleCarga(Long idControleCarga) {
	    return this.getManifestoDAO().findManifestoEmAbertoByIdControleCarga(idControleCarga);
	}

	public List<Manifesto> findManifestoByIdControleCargaAndTpStatusManifesto(Long idControleCarga, List<String> tpStatusManifesto) {
		return this.getManifestoDAO().findManifestoByIdControleCargaAndTpStatusManifesto(idControleCarga, tpStatusManifesto, false);
	}
	public List<Manifesto> findManifestoByIdControleCargaAndNotInTpStatusManifesto(Long idControleCarga, List<String> tpStatusManifesto) {
		return this.getManifestoDAO().findManifestoByIdControleCargaAndTpStatusManifesto(idControleCarga, tpStatusManifesto, true);
	}

	public Integer findManifestosValidosEncerramentoMDFE(Long idControleCarga) {
		return this.getManifestoDAO().findManifestosValidosEncerramentoMDFE(idControleCarga);
	}

	/**
	 * row count para cargas em viagem
	 * @param tfm
	 * @return
	 */
	public Integer getRowCountCargasEmViagem(TypedFlatMap tfm) {
		// busca os manifestos
		List<Manifesto> list = this.getManifestoDAO().findReportCargasEmViagem(tfm);
		List<TypedFlatMap> retorno = executeRulesToCargasEmViagem(tfm, list, false, true);
		return Integer.valueOf(retorno.size());
	}

	/**
	 * Método de pesquisa da tela de cargas em viagem
	 * @param fd
	 * @return
	 * @author Rodrigo Antunes
	 */
	public ResultSetPage<TypedFlatMap> findCargasEmViagem(FindDefinition fd, TypedFlatMap tfm) {
		Long idFilialOrigem = tfm.getLong("filialOrigem.idFilial");
		Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();
		// Se a filial origem for igual a filial do usuario logado, não permitir a pesquisa
		if (idFilialOrigem!=null) {
			if (idFilialOrigem.compareTo(idFilialUsuarioLogado) ==0 ) {
				throw new BusinessException("LMS-03012");
			}
		}

		// busca os manifestos
		List<Manifesto> listaManifestos = this.getManifestoDAO().findReportCargasEmViagem(tfm);
		List<TypedFlatMap> retorno = executeRulesToCargasEmViagem(tfm, listaManifestos, false, false);

		return generateResultSetPage(fd, retorno);
	}

	/**
	 *
	 * @param fd
	 * @param lista
	 * @return
	 */
	private ResultSetPage<TypedFlatMap> generateResultSetPage(FindDefinition fd, List<TypedFlatMap> lista) {
		if (lista.isEmpty())
			return new ResultSetPage<TypedFlatMap>(Integer.valueOf(1), false, false, lista);

		int sizeLista = lista.size();
		int toIndex = fd.getCurrentPage().intValue() * fd.getPageSize().intValue();
		int fromIndex = toIndex - fd.getPageSize().intValue();
		if (toIndex > sizeLista)
			toIndex = sizeLista;

		lista = lista.subList(fromIndex, toIndex);

		return new ResultSetPage<TypedFlatMap>(fd.getCurrentPage(), fd.getCurrentPage().intValue()>1, sizeLista > toIndex, lista);
	}

	/**
	 * Método para o relatorio de cargas em viagem
	 * @param tfm
	 * @return
	 * @author Rodrigo Antunes
	 */
	public List<TypedFlatMap> findReportCargasEmViagem(TypedFlatMap tfm) {
		// busca os manifestos
		List<Manifesto> list = this.getManifestoDAO().findReportCargasEmViagem(tfm);
		return executeRulesToCargasEmViagem(tfm, list, true, false);
	}

	/**
	 * Método responsavel por executar as regras associadas a cargas em viagem
	 * @param criteria
	 * @param list
	 * @return
	 *
	 * @deprecated
	 * by André Valadas:
	 * 		Método repleto de "LÓGICAS" desnecessárias consumindo um caminhão de processamento e memória.
	 * 		As regras aqui descritas devem ser incorporadas direto na consulta dos Manifestos e não mais
	 * 		existir o processo ilógico de "exclusao" para os manifestos que nao atendem a regra... =/
	 */
	private List<TypedFlatMap> executeRulesToCargasEmViagem(TypedFlatMap criteria, List<Manifesto> list, boolean isReport, boolean isRowCount) {
		// ordena a list, baseando-se na sigla e no nr do manifesto
		Collections.sort(list, new Comparator<Manifesto>() {
			public int compare(Manifesto o1, Manifesto o2) {
				String sg1 = o1.getFilialByIdFilialOrigem().getSgFilial();
				String sg2 = o2.getFilialByIdFilialOrigem().getSgFilial();

				int retorno = sg1.compareToIgnoreCase(sg2);
				if (retorno == 0) {
					Long nrManifesto1 = getNumeroManifesto(o1);
					Long nrManifesto2 = getNumeroManifesto(o2);
					retorno = nrManifesto1.compareTo(nrManifesto2);
				}
				return retorno;
			}
		});

		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
		for(Manifesto manifesto : list) {
			DateTime dhChegadaEstimadaInicial = criteria.getDateTime("chegadaEstimadaInicial");
			DateTime dhChegadaEstimadaFinal = criteria.getDateTime("chegadaEstimadaFinal");

			ControleTrecho controleTrecho = manifesto.getControleTrecho();
			DateTime dhChegadaEstimada = null;
			if (controleTrecho!=null && controleTrecho.getDhSaida()!=null) {
				dhChegadaEstimada = controleTrecho.getDhSaida().plusMinutes(controleTrecho.getNrTempoViagem().intValue());
			}

			// Validação dos campos chegadaEstimadaInicial e chegadaEstimadaFinal
			if (dhChegadaEstimadaInicial!=null || dhChegadaEstimadaFinal!=null) {
				boolean addManifesto = false;

				if (dhChegadaEstimada!=null) {
					if (dhChegadaEstimadaInicial!=null && dhChegadaEstimadaFinal!=null) {
						if( dhChegadaEstimada.compareTo(dhChegadaEstimadaInicial)>=0 && dhChegadaEstimada.compareTo(dhChegadaEstimadaFinal)<=0 ) {
							addManifesto = true;
						}
					} else {
						if(dhChegadaEstimadaInicial!=null) {
							if( dhChegadaEstimada.compareTo(dhChegadaEstimadaInicial)>=0 ) {
								addManifesto = true;
							}
						}
						if(dhChegadaEstimadaFinal!=null) {
							if( dhChegadaEstimada.compareTo(dhChegadaEstimadaFinal)<=0 ) {
								addManifesto = true;
							}
						}
					}
				}

				if (!addManifesto) {
					continue;
				}
			}

			Long idFilialDestino = criteria.getLong("filialDestino.idFilial");
			Boolean emAtraso = criteria.getBoolean("emAtraso");
			Boolean naData = criteria.getBoolean("naData");
			Boolean emDia = criteria.getBoolean("emDia");
			DateTime chegadaPrevista = null;
			if (controleTrecho != null) {
				chegadaPrevista = controleTrecho.getDhPrevisaoChegada();
			}

			// Verifica se a filial destino é igual a filial de destino do doctoServico
			if (LongUtils.hasValue(idFilialDestino)) {
				/** Otimização LMS-788 */
				final List<Criterion> criterion = new ArrayList<Criterion>();
				criterion.add(Restrictions.eq("ds.filialByIdFilialDestino.id", idFilialDestino));
				final Boolean isValid = doctoServicoService.verifyDoctoServicoByIdManifesto(manifesto.getIdManifesto(), criterion);
				if (!isValid) {
					continue;
				}
			}

			if (Boolean.TRUE.equals(emAtraso)
					|| Boolean.TRUE.equals(naData)
					|| Boolean.TRUE.equals(emDia)) {

				if (chegadaPrevista!=null || dhChegadaEstimada!=null) {
					// busca os documentos de servico pelo manifesto
					YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
					boolean addElement = false;

					/** Busca Documentos */
					List<DoctoServico> doctoServicoList = new ArrayList<DoctoServico>();
					if (Boolean.TRUE.equals(emAtraso) || Boolean.TRUE.equals(naData) || LongUtils.hasValue(idFilialDestino)) {
						/** Otimização LMS-788 */
						final ProjectionList projection = Projections.projectionList()
								.add(Projections.property("ds.id"), "idDoctoServico")
								.add(Projections.property("ds.dtPrevEntrega"), "dtPrevEntrega")
								.add(Projections.property("fd.id"), "filialByIdFilialDestino.idFilial")
								.add(Projections.property("mpd.id"), "clienteByIdClienteDestinatario.pessoa.enderecoPessoa.municipio.idMunicipio")
								.add(Projections.property("ds.rotaColetaEntregaByIdRotaColetaEntregaSugerid"), "rotaColetaEntregaByIdRotaColetaEntregaSugerid");

						final Map<String, String> alias = new HashMap<String, String>();
						alias.put("ds.filialByIdFilialDestino", "fd");
						alias.put("ds.clienteByIdClienteDestinatario", "cd");
						alias.put("cd.pessoa", "pd");
						alias.put("pd.enderecoPessoa", "epd");
						alias.put("epd.municipio", "mpd");
						doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(manifesto.getIdManifesto(), projection, alias);
					}

					for(DoctoServico doctoServico : doctoServicoList) {
						Long idMunicipioCliente = Long.valueOf(-1);
						Long idFilialDestinoDoctoServico = Long.valueOf(-1);

						Cliente clienteDestinatario = doctoServico.getClienteByIdClienteDestinatario();
						if(clienteDestinatario!=null) {
							if (clienteDestinatario.getPessoa().getEnderecoPessoa()!=null) {
								idMunicipioCliente = clienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
							}
						}

						if(doctoServico.getFilialByIdFilialDestino()!=null) {
							idFilialDestinoDoctoServico = doctoServico.getFilialByIdFilialDestino().getIdFilial();
						}
						Map<String, Object> operacaoServicoLocalizaMap = new HashMap<String, Object>(1);
						Map<String, Object> municipioFilialMap = new HashMap<String, Object>(2);
						Map<String, Object> municipioMap = new HashMap<String, Object>(1);
						Map<String, Object> filialMap = new HashMap<String, Object>(1);

						filialMap.put("idFilial", idFilialDestinoDoctoServico);
						municipioMap.put("idMunicipio", idMunicipioCliente);
						municipioFilialMap.put("filial", filialMap);
						municipioFilialMap.put("municipio", municipioMap);
						operacaoServicoLocalizaMap.put("municipioFilial", municipioFilialMap);

						OperacaoServicoLocaliza operacaoServicoLocaliza = null;
						List<OperacaoServicoLocaliza> operacaoServicoLocalizaList = operacaoServicoLocalizaService.find(operacaoServicoLocalizaMap);

						if(operacaoServicoLocalizaList != null && !operacaoServicoLocalizaList.isEmpty()) {
							operacaoServicoLocaliza = operacaoServicoLocalizaList.get(0);
						}

						int diaSemanaChegadaEstimada = -1;
						TimeOfDay horaChegadaEstimada  = null;
						// os dois campos são opcionais, caso não haja nenhum dos dois, vai dar erro
						// foi falado com os analistas e eles disseram q sempre terá dados.
						if (dhChegadaEstimada!=null) {
							diaSemanaChegadaEstimada = dhChegadaEstimada.getDayOfWeek();
							horaChegadaEstimada = dhChegadaEstimada.toTimeOfDay();
						} else {
							diaSemanaChegadaEstimada = chegadaPrevista.getDayOfWeek();
							horaChegadaEstimada = chegadaPrevista.toTimeOfDay();
						}

						YearMonthDay dpe =  doctoServico.getDtPrevEntrega();

						if (emAtraso.booleanValue()) {
							if( (dpe!=null) && (JTDateTimeUtils.comparaData(dpe, dataAtual ) < 0)  ) {
								addElement = true;
							} else if ( dpe!=null && JTDateTimeUtils.comparaData(dpe, dataAtual ) == 0 ) {
								boolean diaSemanaOk = false;
								if (operacaoServicoLocaliza!=null) {
									switch (diaSemanaChegadaEstimada) {
										case 1:
											if ( operacaoServicoLocaliza.getBlSegunda().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
										case 2:
											if ( operacaoServicoLocaliza.getBlTerca().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
										case 3:
											if ( operacaoServicoLocaliza.getBlQuarta().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
										case 4:
											if ( operacaoServicoLocaliza.getBlQuinta().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
										case 5:
											if ( operacaoServicoLocaliza.getBlSexta().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
										case 6:
											if ( operacaoServicoLocaliza.getBlSabado().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
										case 7:
											if ( operacaoServicoLocaliza.getBlDomingo().booleanValue() ) {
												diaSemanaOk = true;
											}
											break;
									}

									if (diaSemanaOk) {
										RotaColetaEntrega rotaColetaEntrega =  doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid();
										if (rotaColetaEntrega!=null) {
											List<HorarioPrevistoSaidaRota> horariosPrevistos = rotaColetaEntrega.getHorarioPrevistoSaidaRotas();
											if(horariosPrevistos!=null && !horariosPrevistos.isEmpty() && horariosPrevistos.size() > 0) {
												HorarioPrevistoSaidaRota horarioPrevistoSaidaRota = horariosPrevistos.get( horariosPrevistos.size()-1 );
												TimeOfDay hrPrevista = horarioPrevistoSaidaRota.getHrPrevista();
												if (hrPrevista!=null && horaChegadaEstimada != null) {
													if ( horaChegadaEstimada.compareTo( hrPrevista ) > 0) {
														addElement = true;
													}
												}
											}
										}
									}
								}
							}
						}

						if (naData.booleanValue()) {
							if (dpe!=null && JTDateTimeUtils.comparaData(dpe,dataAtual) == 0) {
								boolean diaSemanaOk = false;
								if (operacaoServicoLocaliza!=null) {
									switch (diaSemanaChegadaEstimada) {
										case 1:
											if (operacaoServicoLocaliza.getBlSegunda())
												diaSemanaOk = true;
											break;
										case 2:
											if (operacaoServicoLocaliza.getBlTerca())
												diaSemanaOk = true;
											break;
										case 3:
											if (operacaoServicoLocaliza.getBlQuarta())
												diaSemanaOk = true;
											break;
										case 4:
											if (operacaoServicoLocaliza.getBlQuinta())
												diaSemanaOk = true;
											break;
										case 5:
											if (operacaoServicoLocaliza.getBlSexta())
												diaSemanaOk = true;
											break;
										case 6:
											if (operacaoServicoLocaliza.getBlSabado())
												diaSemanaOk = true;
											break;
										case 7:
											if (operacaoServicoLocaliza.getBlDomingo())
												diaSemanaOk = true;
											break;
									}

									if (diaSemanaOk) {
										RotaColetaEntrega rotaColetaEntrega =  doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaSugerid();
										if (rotaColetaEntrega!=null) {
											List<HorarioPrevistoSaidaRota> horariosPrevistos = rotaColetaEntrega.getHorarioPrevistoSaidaRotas();
											if(horariosPrevistos!=null && !horariosPrevistos.isEmpty() && horariosPrevistos.size() > 0) {
												HorarioPrevistoSaidaRota horarioPrevistoSaidaRota = horariosPrevistos.get( horariosPrevistos.size()-1 );
												TimeOfDay hrPrevista = horarioPrevistoSaidaRota.getHrPrevista();
												if (hrPrevista!=null && horaChegadaEstimada != null) {
													if (horaChegadaEstimada.compareTo( hrPrevista ) < 0) {
														addElement = true;
													}
												}
											}
										}
									}
								}
							}
						}
						if (emDia && dpe!=null && JTDateTimeUtils.comparaData(dpe,dataAtual) > 0) {
							addElement = true;
						}
					}

					if(!addElement)
						continue;
				}
			}

			TypedFlatMap dadosRetorno = new TypedFlatMap();
			dadosRetorno.put("manifesto.idManifesto", manifesto.getIdManifesto());
			dadosRetorno.put("manifesto.filialByIdFilialOrigem.sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());
			dadosRetorno.put("manifesto.dhEmissaoManifesto", manifesto.getDhEmissaoManifesto());
			dadosRetorno.put("manifesto.filialByIdFilialDestino.sgFilial", manifesto.getFilialByIdFilialDestino().getSgFilial());

			// adiciona no typedFlatmap o nrManifesto
			dadosRetorno.put("manifesto.nrManifesto", this.getNumeroManifesto(manifesto));

			ControleCarga controleCarga = manifesto.getControleCarga();
			if (controleCarga!=null) {
				dadosRetorno.put("manifesto.controleCarga.filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
				dadosRetorno.put("manifesto.controleCarga.nrControleCarga", controleCarga.getNrControleCarga());

				MeioTransporte transportado = controleCarga.getMeioTransporteByIdTransportado();

				if (transportado!=null) {
					dadosRetorno.put("manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador", transportado.getNrIdentificador());
					dadosRetorno.put("manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota", transportado.getNrFrota());
				}
				// *************************************************************
				// CAMPOS ESPECIFICOS DO RELATORIO *****************************
				// *************************************************************
				if ( isReport ) {
					dadosRetorno.put("manifesto.controleCarga.idControleCarga", controleCarga.getIdControleCarga());
					dadosRetorno.put("manifesto.controleCarga.psTotalFrota", controleCarga.getPsTotalFrota());
					dadosRetorno.put("manifesto.controleCarga.pcOcupacaoCalculado", controleCarga.getPcOcupacaoCalculado());
					dadosRetorno.put("manifesto.controleCarga.pcOcupacaoAforadoCalculado", controleCarga.getPcOcupacaoAforadoCalculado());
					dadosRetorno.put("manifesto.controleCarga.pcOcupacaoInformado", controleCarga.getPcOcupacaoInformado());
					dadosRetorno.put("manifesto.vlTotalManifesto", manifesto.getVlTotalManifesto());

					dadosRetorno.put("manifesto.moeda.siglaSimbolo", manifesto.getMoeda().getSiglaSimbolo());

					if (transportado!=null) {
						dadosRetorno.put("manifesto.controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg", transportado.getNrCapacidadeKg());
					}

					MeioTransporte semiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado();

					if (semiRebocado!=null) {
						dadosRetorno.put("manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", semiRebocado.getNrIdentificador());
						dadosRetorno.put("manifesto.controleCarga.meioTransporteByIdSemiRebocado.nrFrota", semiRebocado.getNrFrota());
					}

					if ("E".equals( manifesto.getTpManifesto().getValue()) ) {
						dadosRetorno.put("manifesto.tpManifesto", manifesto.getTpManifesto().getDescription().getValue());
					} else if ("V".equals( manifesto.getTpManifesto().getValue()) ) {
						String abrangencia = manifesto.getTpAbrangencia()!=null?manifesto.getTpAbrangencia().getDescription().getValue():null;
						if (abrangencia!=null) {
							dadosRetorno.put("manifesto.tpManifesto", manifesto.getTpManifesto().getDescription().getValue() +" " + abrangencia.toLowerCase());
						} else {
							// não é para entrar nesse if, pois se tpManifesto==V obrigatoriamente terá uma abrangencia
							dadosRetorno.put("manifesto.tpManifesto", manifesto.getTpManifesto().getDescription().getValue());
						}
					}
				}
				// *************************************************************
				// FIM CAMPOS DO RELATORIO *************************************
				// *************************************************************
			}

			if (!isRowCount){
				findDadosControleTrecho(manifesto, dadosRetorno);
				dadosRetorno.put("quantidadeDoctos", this.getQuantidadeDoctosByManifesto(manifesto, false));
				
				dadosRetorno.put("blProdutoPerigoso", this.validateExisteManifestoComProdutoPerigoso(manifesto.getIdManifesto()) ? SIM : NAO);
				dadosRetorno.put("blProdutoControlado", this.validateExisteManifestoComProdutoControlado(manifesto.getIdManifesto()) ? SIM : NAO);
			}
			retorno.add(dadosRetorno);
		}
		return retorno;
	}

	/**
	 * Busca o nrManifesto conforme o tpManifesto (E || V).
	 * Para o tpManifesto == V verificar o tpAbrangencia(I || N)
	 * @param manifesto
	 * @return Long numero manifesto
	 * @author Rodrigo Antunes
	 */
	public Long getNumeroManifesto(Manifesto manifesto) {
		String tpManifesto = manifesto.getTpManifesto().getValue();
		Long retorno = null;

		if ("V".equalsIgnoreCase(tpManifesto)) {
			DomainValue domainTpAbrangencia = manifesto.getTpAbrangencia();

			if (domainTpAbrangencia!=null) {

				if("N".equalsIgnoreCase(domainTpAbrangencia.getValue())) {
					ManifestoViagemNacional mvn = manifesto.getManifestoViagemNacional();

					if (mvn!=null) {
						retorno = Long.valueOf(mvn.getNrManifestoOrigem().intValue());
					}
				} else if("I".equalsIgnoreCase(domainTpAbrangencia.getValue())) {
					ManifestoInternacional mi = manifesto.getManifestoInternacional();

					if (mi!=null) {
						retorno = mi.getNrManifestoInt();
					}
				}
			}
		} else if ("E".equalsIgnoreCase(tpManifesto)) {
			ManifestoEntrega me = manifesto.getManifestoEntrega();
			if (me!=null) {
				retorno = Long.valueOf(me.getNrManifestoEntrega().intValue());
			}
		}
		return retorno;
	}

	/**
	 * Busca os ids de manifestos, baseado no id do serviço
	 * @param idServico
	 * @return
	 */
	public List<Long> findIdManifestosByIdServico(Long idServico) {
		List<Long> idsManifestosList = new ArrayList<Long>();
		idsManifestosList.add(Long.valueOf(-1));

		if (idServico != null) {
			Map<String, Long> idServicoMap = new HashMap<String, Long>(1);
			Map<String, Map<String, Long>> servicoMap = new HashMap<String, Map<String, Long>>(1);
			idServicoMap.put("idServico", idServico);
			servicoMap.put("servico", idServicoMap);

			List<DoctoServico> doctoServicoList = doctoServicoService.find(servicoMap);
			for(DoctoServico doctoServico : doctoServicoList) {
				for(PreManifestoDocumento preManifestoDocumento : doctoServico.getPreManifestoDocumentos()) {
					if (preManifestoDocumento.getManifesto() != null) {
						idsManifestosList.add(preManifestoDocumento.getManifesto().getIdManifesto());
					}
				}
			}
		}
		return idsManifestosList;
	}

	/**
	 *
	 * @param idManifesto
	 * @return
	 */
	public TypedFlatMap findDadosControleCargaDoManifesto(Long idManifesto) {
		TypedFlatMap tfm = null;

		if (idManifesto!=null) {
			tfm = new TypedFlatMap();
			Manifesto manifesto = this.findByIdInitLazyProperties(idManifesto, false);
			this.findDadosControleTrecho(manifesto, tfm);
			ControleCarga controleCarga =  manifesto.getControleCarga();

			if (controleCarga!=null) {
				tfm.put("controleCarga.filialByIdFilialOrigem.sgFilial", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
				tfm.put("controleCarga.nrControleCarga", controleCarga.getNrControleCarga());
				MeioTransporte transportado = controleCarga.getMeioTransporteByIdTransportado();

				if (transportado!=null) {
					tfm.put("controleCarga.meioTransporteByIdTransportado.nrIdentificador", transportado.getNrIdentificador());
					tfm.put("controleCarga.meioTransporteByIdTransportado.nrFrota", transportado.getNrFrota());
					tfm.put("controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg", transportado.getNrCapacidadeKg());
				} else {
					// este else é apenas para não ter que tratar no javascript
					tfm.put("controleCarga.meioTransporteByIdTransportado.nrIdentificador", "");
					tfm.put("controleCarga.meioTransporteByIdTransportado.nrFrota", "");
					tfm.put("controleCarga.meioTransporteByIdTransportado.nrCapacidadeKg", "");
				}
				MeioTransporte semiRebocado = controleCarga.getMeioTransporteByIdSemiRebocado();

				if (semiRebocado!=null) {
					tfm.put("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", semiRebocado.getNrIdentificador());
					tfm.put("controleCarga.meioTransporteByIdSemiRebocado.nrFrota", semiRebocado.getNrFrota());
				} else {
					// este else é apenas para não ter que tratar no javascript
					tfm.put("controleCarga.meioTransporteByIdSemiRebocado.nrIdentificador", "");
					tfm.put("controleCarga.meioTransporteByIdSemiRebocado.nrFrota", "");
				}

				tfm.put("controleCarga.psTotalFrota", controleCarga.getPsTotalFrota());
				tfm.put("controleCarga.pcOcupacaoCalculado", controleCarga.getPcOcupacaoCalculado());
				tfm.put("controleCarga.pcOcupacaoAforadoCalculado", controleCarga.getPcOcupacaoAforadoCalculado());
				tfm.put("controleCarga.psOcupacaoVisual", controleCarga.getPcOcupacaoInformado());
				Motorista motorista = controleCarga.getMotorista();

				if (motorista!=null) {
					tfm.put("controleCarga.motorista.nrCarteiraHabilitacao", motorista.getNrCarteiraHabilitacao());
					tfm.put("controleCarga.motorista.pessoa.nmPessoa", motorista.getPessoa().getNmPessoa());
				} else {
					// este else é apenas para não ter que tratar no javascript
					tfm.put("controleCarga.motorista.nrCarteiraHabilitacao", "");
					tfm.put("controleCarga.motorista.pessoa.nmPessoa", "");
				}

				if (controleCarga.getFilialByIdFilialDestino()!=null) {
					tfm.put("controleCarga.filialByIdFilialDestino.sgFilial", controleCarga.getFilialByIdFilialDestino().getSgFilial());
				} else {
					tfm.put("controleCarga.filialByIdFilialDestino.sgFilial", "");
				}
			}

			if ("E".equals( manifesto.getTpManifesto().getValue()) ) {
				tfm.put("manifesto.tpManifesto", manifesto.getTpManifesto().getDescription().getValue());
			} else if ("V".equals( manifesto.getTpManifesto().getValue()) ) {
				String abrangencia = manifesto.getTpAbrangencia()!=null?manifesto.getTpAbrangencia().getDescription().getValue():null;
				if (abrangencia!=null) {
					tfm.put("manifesto.tpManifesto", manifesto.getTpManifesto().getDescription().getValue() +" " + abrangencia.toLowerCase());
				} else {
					// não é para entrar nesse if, pois se tpManifesto==V obrigatoriamente terá uma abrangencia
					tfm.put("manifesto.tpManifesto", manifesto.getTpManifesto().getDescription().getValue());
				}
			}

			tfm.put("manifesto.filialByIdFilialOrigem", manifesto.getFilialByIdFilialOrigem().getSgFilial());

			// adiciona no typedFlatmap o nrManifesto
			tfm.put("manifesto.nrManifesto", this.getNumeroManifesto(manifesto));

			tfm.put("manifesto.dhEmissaoManifesto", manifesto.getDhEmissaoManifesto());
			tfm.put("manifesto.siglaSimboloVlTotalManifesto", manifesto.getMoeda().getSiglaSimbolo());
			tfm.put("manifesto.vlTotalManifesto", manifesto.getVlTotalManifesto());
			tfm.put("quantidadeDoctos", this.getQuantidadeDoctosByManifesto(manifesto, false));
		}

		return tfm;
	}

	/**
	 *
	 * @param manifesto
	 * @param tfm
	 * @author Rodrigo Antunes
	 */
	private void findDadosControleTrecho(Manifesto manifesto, TypedFlatMap tfm) {
		ControleTrecho controleTrecho = manifesto.getControleTrecho();
		if (controleTrecho!=null) {
			DateTime chegadaEstimada = controleTrecho.getDhSaida();
			DateTime chegadaProgramada = controleTrecho.getDhPrevisaoChegada();
			Integer nrTempoViagem = controleTrecho.getNrTempoViagem();
			String atraso = null;
			if (chegadaEstimada!=null && nrTempoViagem!=null) {
				chegadaEstimada = chegadaEstimada.plusMinutes(nrTempoViagem.intValue());
			}
			if (chegadaEstimada!=null && chegadaProgramada!=null){
				// deve ser colocado a data que é a menor primeiro
				atraso = JTDateTimeUtils.calculaDiferencaEmHoras(chegadaProgramada, chegadaEstimada);
			}
			tfm.put("chegadaProgramada", chegadaProgramada);
			tfm.put("chegadaEstimada", chegadaEstimada);
			tfm.put("atraso", atraso);
		}
	}

	/**
	 * Busca o nro total de documentos de serviços associados ao manifesto
	 * @param manifesto
	 * @return
	 */
	public Integer getQuantidadeDoctosByManifesto(Manifesto manifesto) {
		return getQuantidadeDoctosByManifesto(manifesto, true);
	}

	/**
	 * Busca o nro total de documentos de serviços associados ao manifesto
	 * @param manifesto
	 * @param filtrarTipoDocumento
	 * @return
	 */
	public Integer getQuantidadeDoctosByManifesto(Manifesto manifesto, boolean filtrarTipoDocumento) {
		int nroDoctos = 0;
		if (manifesto!=null) {
			Integer r = doctoServicoService.findNrDoctosServicoByIdManifesto(manifesto, filtrarTipoDocumento);
			if (r!=null ) {
				nroDoctos = r.intValue();
			}
		}
		return Integer.valueOf(nroDoctos);
	}

	/**
	 * Busca os dados de documento de servico para a popup de documentos
	 * da tela consulta cargas em viagem.
	 * @param idManifesto
	 * @param df
	 * @return
	 * @author Rodrigo Antunes
	 */
	public ResultSetPage<TypedFlatMap> findDocumentosConsultarCargasViagem(Long idManifesto, FindDefinition df) {
		List<TypedFlatMap> retorno = executeGetDataDocumentosCargasViagem(idManifesto);
		return generateResultSetPage(df, retorno);
	}

	public List<TypedFlatMap> findReportDocumentosConsultarCargasViagem(Long idManifesto) {
		return executeGetDataDocumentosCargasViagem(idManifesto);
	}

	/**
	 * @param idManifesto
	 * @return
	 */

	private List<TypedFlatMap> executeGetDataDocumentosCargasViagem(Long idManifesto) {
		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();

		if (idManifesto!=null) {
			/** Otimização LMS-788 */
			final ProjectionList projection = Projections.projectionList()
					.add(Projections.property("ds.id"), "idDoctoServico")
					.add(Projections.property("ser.dsServico"), "servico.dsServico")
					.add(Projections.property("ser.tpModal"), "servico.tpModal")
					.add(Projections.property("ser.tipoServico"), "servico.tipoServico")
					.add(Projections.property("fo.sgFilial"), "filialByIdFilialOrigem.sgFilial")
					.add(Projections.property("ds.tpDocumentoServico"), "tpDocumentoServico")
					.add(Projections.property("ds.nrDoctoServico"), "nrDoctoServico")
					.add(Projections.property("fd.sgFilial"), "filialByIdFilialDestino.sgFilial")
					.add(Projections.property("ds.dhEmissao.value"), "dhEmissao.value")
					.add(Projections.property("ds.dhEmissao.timezoneregion"), "dhEmissao.timezoneregion")
					.add(Projections.property("pr.nmPessoa"), "clienteByIdClienteRemetente.pessoa.nmPessoa")
					.add(Projections.property("pd.nmPessoa"), "clienteByIdClienteDestinatario.pessoa.nmPessoa")
					.add(Projections.property("ds.qtVolumes"), "qtVolumes")
					.add(Projections.property("ds.psReal"), "psReal")
					.add(Projections.property("ds.vlMercadoria"), "vlMercadoria")
					.add(Projections.property("ds.dtPrevEntrega"), "dtPrevEntrega")
					.add(Projections.property("ds.vlTotalDocServico"), "vlTotalDocServico")
					.add(Projections.property("m.sgMoeda"), "moeda.sgMoeda")
					.add(Projections.property("m.dsSimbolo"), "moeda.dsSimbolo")
					.add(Projections.property("rcer.nrRota"), "rotaColetaEntregaByIdRotaColetaEntregaReal.nrRota")
					.add(Projections.property("rcer.dsRota"), "rotaColetaEntregaByIdRotaColetaEntregaReal.dsRota");

			final Map<String, String> alias = new HashMap<String, String>();
			alias.put("ds.servico", "ser");
			alias.put("ds.filialByIdFilialOrigem", "fo");
			alias.put("ds.filialByIdFilialDestino", "fd");
			alias.put("ds.clienteByIdClienteRemetente", "cr");
			alias.put("cr.pessoa", "pr");
			alias.put("ds.clienteByIdClienteDestinatario", "cd");
			alias.put("cd.pessoa", "pd");
			alias.put("ds.moeda", "m");
			alias.put("ds.rotaColetaEntregaByIdRotaColetaEntregaReal", "rcer");

			List<DoctoServico> l = doctoServicoService.findDoctoServicoByIdManifesto(idManifesto, projection, alias);
			for(DoctoServico doctoServico : l) {
				TypedFlatMap tfm = new TypedFlatMap();

				if (doctoServico.getServico()!=null) {
					tfm.put("doctoServico.servico.dsServico", doctoServico.getServico().getDsServico().getValue());
				}
				tfm.put("doctoServico.filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
				tfm.put("doctoServico.tpDoctoServico", doctoServico.getTpDocumentoServico().getDescription().getValue());
				tfm.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());

				if (doctoServico.getFilialByIdFilialDestino()!=null) {
					tfm.put("doctoServico.filialByIdFilialDestino.sgFilial", doctoServico.getFilialByIdFilialDestino().getSgFilial());
				}

				tfm.put("doctoServico.dhEmissao", doctoServico.getDhEmissao());

				if (doctoServico.getClienteByIdClienteRemetente()!=null) {
					tfm.put("doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", doctoServico.getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
				}

				if (doctoServico.getClienteByIdClienteDestinatario()!=null) {
					tfm.put("doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", doctoServico.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
				}

				if(doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal()!=null){
					tfm.put("doctoServico.rotaColetaEntregaByIdRotaColetaEntregaReal.nrRota", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal().getNrRota());
					tfm.put("doctoServico.rotaColetaEntregaByIdRotaColetaEntregaReal.dsRota", doctoServico.getRotaColetaEntregaByIdRotaColetaEntregaReal().getDsRota());
				}

				AgendamentoEntrega agendamentoEntrega = agendamentoEntregaService.findAgendamentoAbertoDoctoServico(doctoServico.getIdDoctoServico());
				if(agendamentoEntrega!= null){
					tfm.put("doctoServico.agendamentoEntrega.dtAgendamento", agendamentoEntrega.getDtAgendamento());
				}

				tfm.put("doctoServico.qtVolumes", doctoServico.getQtVolumes());
				tfm.put("doctoServico.psReal", doctoServico.getPsReal());
				tfm.put("doctoServico.vlMercadoria", doctoServico.getVlMercadoria());
				tfm.put("doctoServico.dtPrevEntrega", doctoServico.getDtPrevEntrega());

				checkChangeColorItem(tfm, doctoServico);
				tfm.put("doctoServico.vlTotalDocServico", doctoServico.getVlTotalDocServico());
				tfm.put("doctoServico.moeda.siglaSimbolo", doctoServico.getMoeda().getSiglaSimbolo());

				Map mapConhecimento = conhecimentoService.findBlProdutoPerigosoControladoByIdConhecimento(doctoServico.getIdDoctoServico());
				tfm.put("conhecimento.blProdutoPerigoso", BooleanUtils.isTrue((Boolean)mapConhecimento.get("blProdutoPerigoso")) ? SIM : NAO);
				tfm.put("conhecimento.blProdutoControlado", BooleanUtils.isTrue((Boolean)mapConhecimento.get("blProdutoControlado")) ? SIM : NAO);
				
				retorno.add(tfm);
			}
		}
		return retorno;
	}

	/**
	 * Verifica os dados da dpe, se é um servico  de aereo, se é um tipo de
	 * servico emergencial e passa um parametro para no jsp/relatorio mudar a cor do item.
	 * @param tfm
	 * @param doctoServico
	 */
	private void checkChangeColorItem(TypedFlatMap tfm, DoctoServico doctoServico) {
		boolean mudaCorItem = false;

		Servico servico = doctoServico.getServico();
		if (servico!=null) {
			if ("A".equalsIgnoreCase(servico.getTpModal().getValue())) {
				mudaCorItem = true;
			}

			TipoServico tipoServico = servico.getTipoServico();
			if (tipoServico!=null) {
				if(tipoServico.getBlPriorizar().booleanValue() ) {
					mudaCorItem = true;
				}
			}
		}

		if (doctoServico.getDtPrevEntrega()!=null) {
			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();

			if (doctoServico.getDtPrevEntrega().compareTo( dataAtual )==-1 ) {
				mudaCorItem = true;
			}
		}
		tfm.put("mudaCorItem", Boolean.valueOf(mudaCorItem));
	}

	/**
	 * Verifica se todos os manifestos relacionados ao controle de carga foram emitidos.
	 *
	 * @param idControleCarga
	 * @return TRUE se existe, FALSE caso contrário
	 */
	public Boolean findVerificaExisteManifestoNaoEmitido(Long idControleCarga) {
		return getManifestoDAO().findVerificaExisteManifestoNaoEmitido(idControleCarga);
	}

	/**
	 * Retorna o DPE a partir do documento de serviço e manifesto de entrega.
	 *
	 * @param idDoctoServico id do documento de serviço.
	 * @param idManifesto id do manifesto.
	 *
	 * @return String com o DPE encontrado.
	 */
	public String findDpe(Long idDoctoServico, Long idManifesto) {
		return this.findDpe(idDoctoServico, idManifesto, null, JTFormatUtils.DEFAULT);
	}

	/**
	 * Retorna o DPE a partir do documento de serviço e manifesto de entrega.
	 *
	 * @param idDoctoServico id do documento de serviço.
	 * @param idManifesto id do manifesto.
	 * @param dhBase se for informado, não carrega o manifesto.
	 * @param style mascara da data a ser retornada.
	 * @return String com o DPE encontrado.
	 */
	public String findDpe(Long idDoctoServico, Long idManifesto, DateTime dhBase, int style) {
		if (idDoctoServico == null) {
			throw new IllegalArgumentException("Parâmetro idDocumentoServico é obrigatório.");
		}
		if (!(idManifesto == null && dhBase != null || idManifesto != null && dhBase == null)) {
			throw new IllegalArgumentException("Informe idManifesto OU dhBase, exclusivamente.");
		}

		final DoctoServico doctoServico = doctoServicoService.findById(idDoctoServico);
		if (dhBase == null) {
			final Manifesto manifesto = (Manifesto) findByIdInitLazyProperties(idManifesto, false);
			dhBase = manifesto.getDhEmissaoManifesto();
		}
		return this.findDpe(doctoServico,dhBase,style);
	}

	/**
	 * Retorna o DPE a partir do documento de serviço e manifesto de entrega.
	 *
	 * @param doctoServico entidade persistente de documento de serviço.
	 * @param dhBase data/hora base para comparação.
	 * @param style mascara da data a ser retornada.
	 * @return String com o DPE encontrado.
	 */
	@SuppressWarnings("deprecation")
	private String findDpe(DoctoServico doctoServico, DateTime dhBase, int style) {
		YearMonthDay dtPrevEntrega = doctoServico.getDtPrevEntrega();

		if (dtPrevEntrega == null || dhBase == null)
			return "";

		int intComparacao = JTDateTimeUtils.comparaData(dtPrevEntrega,dhBase.toYearMonthDay());
		if (intComparacao < 0)
			return JTFormatUtils.format(dtPrevEntrega,style);

		if (intComparacao == 0)
			return configuracoesFacade.getMensagem("hoje").toUpperCase();

		return " ";
	}

	/**
	 * Retorna true caso o manifesto informado é de viagem nacional
	 * @param idManifesto
	 * @return
	 */
	public boolean isManifestoViagemNacional(Long idManifesto){
		Manifesto manifesto = this.findByIdInitLazyProperties(idManifesto, false);
		if (manifesto!=null){
			String tpManifesto = manifesto.getTpManifesto().getValue();

			//tpAbrangencia nao e um campo "not null" decorrente disto deve ser validado
			if (manifesto.getTpAbrangencia()!=null) {
				String tpAbrangencia = manifesto.getTpAbrangencia().getValue();
				if (tpManifesto.equalsIgnoreCase("V") && tpAbrangencia.equalsIgnoreCase("N")){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Retorna true caso o manifesto informado é de viagem internacional
	 * @param idManifesto
	 * @return
	 */
	public boolean isManifestoViagemInternacional(Long idManifesto){
		Manifesto manifesto = this.findByIdInitLazyProperties(idManifesto, false);
		if (manifesto!=null){
			String tpManifesto = manifesto.getTpManifesto().getValue();

			//tpAbrangencia nao e um campo "not null" decorrente disto deve ser validado
			if (manifesto.getTpAbrangencia()!=null) {
				String tpAbrangencia = manifesto.getTpAbrangencia().getValue();
				if (tpManifesto.equalsIgnoreCase("V") && tpAbrangencia.equalsIgnoreCase("I")){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Retorna true caso o manifesto informado é de viagem internacional
	 * @param idManifesto
	 * @return
	 */
	public boolean isManifestoEntrega(Long idManifesto){
		return isManifestoEntrega(findByIdInitLazyProperties(idManifesto, false));
	}

	private boolean isManifestoEntrega(Manifesto manifesto) {
		if (manifesto!=null){
			return ConstantesEntrega.DM_MANIFESTO_ENTREGA.equals(manifesto.getTpManifesto());
		}

		return false;
	}

	public boolean validateIfIsEntregaNormal(Manifesto manifesto) {
		if (manifesto != null) {
			return ConstantesEntrega.DM_MANIFESTO_ENTREGA_NORMAL.equals(manifesto.getTpManifestoEntrega());
		}

		return false;
	}

	public boolean validateIfIsEntregaDireta(Manifesto manifesto) {
		if (manifesto != null) {
			return ConstantesEntrega.DM_MANIFESTO_ENTREGA_DIRETA.equals(manifesto.getTpManifestoEntrega());
		}

		return false;
	}

	public boolean validateIfIsEntregaParceira(Manifesto manifesto) {
		if (manifesto != null) {
			return ConstantesEntrega.DM_MANIFESTO_ENTREGA_PARCEIRA.equals(manifesto.getTpManifestoEntrega());
		}

		return false;
	}

	private boolean isManifestoCancelado(Manifesto manifesto) {
		if (manifesto != null) {
			return ConstantesEntrega.DM_MANIFESTO_CANCELADO.equals(manifesto.getTpStatusManifesto());
		}

		return false;
	}

	public boolean validateIfIsManifestoEntregaRealizada(Manifesto manifesto) {
		if (manifesto != null) {
			return isManifestoEntrega(manifesto) && !isManifestoCancelado(manifesto)
					&& manifestoEntregaService.validateIfIsEntregaRealizada(manifesto.getManifestoEntrega());
		}

		return false;
	}

	public boolean validateIfIsManifestoEntregaNormalParceiraOuDireta(Manifesto manifesto) {
		return validateIfIsManifestoEntregaRealizada(manifesto) && (validateIfIsEntregaNormal(manifesto)
				|| validateIfIsEntregaParceira(manifesto) || validateIfIsEntregaDireta(manifesto));
	}

	public boolean validateIfManifestoEntregaValidoParaCliente(Cliente cliente, Manifesto manifesto) {
		return validateIfIsManifestoEntregaNormalParceiraOuDireta(manifesto)
				|| isManifestoEntregaCliente(cliente, manifesto);
	}

	private boolean isManifestoEntregaCliente(Cliente cliente, Manifesto manifesto) {
		for (ManifestoEntregaDocumento documento : manifesto.getManifestoEntrega().getManifestoEntregaDocumentos()) {
			if (cliente.equals(documento.getDoctoServico().getDevedorDocServs().get(0))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Altera a situacao do manifesto
	 * @param idManifesto
	 */
	public void updateSituacaoManifestoParaCancelado(final Long idManifesto){
		getManifestoDAO().updateSituacaoManifesto(idManifesto,ConstantesEntrega.STATUS_MANIFESTO_CANCELADO,true);
	}

	/**
	 * Altera a situacao do manifesto
	 * @param idManifesto
	 * @param tpSituacao
	 */
	public void updateSituacaoManifesto(final Long idManifesto, final String tpSituacao){
		getManifestoDAO().updateSituacaoManifesto(idManifesto, tpSituacao,false);
	}

	/**
	 * Altera a situação do manifesto e a data de emissão.
	 * @param idManifesto
	 * @param tpSituacao
	 * @param dhEmissaoManifesto
	 */
	public void updateSituacaoManifesto(final Long idManifesto, final String tpSituacao, final DateTime dhEmissaoManifesto){
		getManifestoDAO().updateSituacaoManifesto(idManifesto, tpSituacao, dhEmissaoManifesto);
	}

	/**
	 * Obtém um lista de Manifestos relacionados ao Controle de Carga em questão.<br>
	 * Filtra os Manifestos com tpStatusManifesto: CC(Carregamento Concluido) ou EF(Em Escala da Filial).
	 * @param idControleCarga
	 * @return
	 */
	public List findManifestosCCorEFByIdControleCarga(Long idControleCarga) {
		return getManifestoDAO().findManifestosCCorEFByIdControleCarga(idControleCarga);
	}

	/**
	 * Verifica se o manifesto esta emitido e se o controle de carga possui data de saida de coleta/entrega
	 * @param idManifesto
	 * @return
	 */
	public boolean validateEmissaoSaidaManifesto(Long idManifesto){
		return getManifestoDAO().validateEmissaoSaidaManifesto(idManifesto);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public List findManifestosTrechoByIdControleCarga(Long idControleCarga, Long idFilialOrigemTrecho, Long idFilialDestinoTrecho, Byte nrOrdemOrigem, Byte nrOrdemDestino) {
		return getManifestoDAO().findManifestosTrechoByIdControleCarga(idControleCarga, idFilialOrigemTrecho, idFilialDestinoTrecho, nrOrdemOrigem, nrOrdemDestino);
	}

	public Map<String, Object> findManifestoComControleCargas(Long idDoctoServico) {
		Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
		List<Manifesto> manifestos = this.findManifestoByDataEmissao(idDoctoServico, filialUsuarioLogado.getIdFilial());
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("manifestoCount", manifestos.size());
		if(manifestos.size() == 1 ){
			Manifesto manifesto = manifestos.get(0);
			if (manifesto.getTpManifesto().getValue().equals("E")) {
				map.put("idFilialManifesto", manifesto.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getIdFilial());
				map.put("sgFilialManifesto", manifesto.getManifestoEntrega().getManifesto().getFilialByIdFilialOrigem().getSgFilial());
				map.put("nrManifesto", manifesto.getManifestoEntrega().getNrManifestoEntrega());
				map.put("tpManifesto", "EN");
			} else if (manifesto.getTpManifesto().getValue().equals("V") && manifesto.getTpAbrangencia().getValue().equals("N")) {
				map.put("idFilialManifesto", manifesto.getManifestoViagemNacional().getManifesto().getFilialByIdFilialOrigem().getIdFilial());
				map.put("sgFilialManifesto", manifesto.getManifestoViagemNacional().getManifesto().getFilialByIdFilialOrigem().getSgFilial());
				map.put("nrManifesto", manifesto.getManifestoViagemNacional().getNrManifestoOrigem());
				map.put("tpManifesto", "VN");
			} else if (manifesto.getTpManifesto().getValue().equals("V") && manifesto.getTpAbrangencia().getValue().equals("I")) {
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
	 *
	 * @param idControleCarga
	 * @return
	 */
	public List<TypedFlatMap> findPaginatedManifestoByControleCarga(Long idControleCarga) {
		List<Object[]> list = getManifestoDAO().findPaginatedManifestoByControleCarga(idControleCarga);
		String labelColeta = configuracoesFacade.getMensagem("coleta");

		Moeda moeda = SessionUtils.getMoedaSessao();

		
		/*
		0 		ID_MANIFESTO
		1		SG_FILIAL
		2		NR_MANIFESTO
		3		SG_FILIAL_DESTINO
		4		TP_OPERACAO
		5		TP_MODAL
		6		TP_MANIFESTO
		7		STATUS
		8		ID_MOEDA
		9		SG_MOEDA
		10		DS_SIMBOLO
		11		VL_MERCADORIA
		12		PS_TOTAL_MANIFESTO
		13		PS_TOTAL_AFORADO_MANIFESTO
		 * */

		List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
		for(Object[] obj : list) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("sgFilial", obj[1]);
			tfm.put("nrManifesto", obj[2]);
			tfm.put("sgFilialDestino", obj[3]);
			String status = (String)obj[7];
			if ( ((String)obj[4]).equals("C") ) {
				tfm.put("idManifesto", obj[0]);
				tfm.put("tpOperacao.description", labelColeta);
				tfm.put("tpOperacao.value", "C");
				tfm.put("tpManifesto.description", labelColeta);
				tfm.put("status", domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO_COLETA", status));
				BigDecimal valorSomatorio = pedidoColetaService.findSumVlTotalVerificadoByManifestoColeta((Long)obj[0]);
				if (valorSomatorio.compareTo(BigDecimalUtils.ZERO) != 0) {
					tfm.put("idMoeda", moeda.getIdMoeda());
					tfm.put("sgMoeda", moeda.getSgMoeda());
					tfm.put("dsSimbolo", moeda.getDsSimbolo());
					tfm.put("vlMercadoria", valorSomatorio);
				}
				tfm.put("sgMoedaFrete",moeda.getSgMoeda());
				tfm.put("dsSimboloFrete", moeda.getDsSimbolo());
				tfm.put("vlFrete", BigDecimal.ZERO);
			} else {
				tfm.put("idManifesto", obj[0]);
				tfm.put("tpOperacao", domainValueService.findDomainValueByValue("DM_TIPO_MANIFESTO", (String)obj[4]));
				tfm.put("tpModal", domainValueService.findDomainValueByValue("DM_MODAL", (String)obj[5]));

				if (obj[6] != null && tfm.getDomainValue("tpOperacao").getValue().equals("E")) {
					tfm.put("tpManifesto", domainValueService.findDomainValueByValue("DM_TIPO_MANIFESTO_ENTREGA", (String)obj[6]));
					BigDecimal valorTotalFrete = getManifestoDAO().findSomaVlTotalFreteByIdManifestoEntrega((Long)obj[0]);
					tfm.put("vlFrete", valorTotalFrete);
				}
				else
				if (obj[6] != null && tfm.getDomainValue("tpOperacao").getValue().equals("V")) {
					tfm.put("tpManifesto", domainValueService.findDomainValueByValue("DM_TIPO_MANIFESTO_VIAGEM", (String)obj[6]));
					BigDecimal valorTotalFrete = getManifestoDAO().findSomaVlTotalFreteByIdManifestoViagem((Long)obj[0]);
					tfm.put("vlFrete", valorTotalFrete);
				}

				tfm.put("status", domainValueService.findDomainValueByValue("DM_STATUS_MANIFESTO", status));
				tfm.put("idMoeda", obj[8]);
				tfm.put("sgMoeda", obj[9]);
				tfm.put("dsSimbolo", obj[10]);
				tfm.put("vlMercadoria", obj[11]);
				tfm.put("sgMoedaFrete", obj[9]);
				tfm.put("dsSimboloFrete", obj[10]);
			}
			tfm.put("psTotal", obj[12]);
			tfm.put("psTotalAforado", obj[13]);
			retorno.add(tfm);
		}
		return retorno;
	}

	/**
	 * Busca todos os ids dos manifestos que tenham um documento de servico com o servico passado por parâmetro.
	 * @param idServico
	 * @return
	 */
	public List<Long> findIdsManifestosByIdServico(Long idServico){
		List<Long> idsManifestosList = new ArrayList<Long>();
		idsManifestosList.add(Long.valueOf(-1));
		if(idServico != null) {
			idsManifestosList.addAll(getManifestoDAO().findIdsManifestosByIdServico(idServico));
		}
		return idsManifestosList;
	}

	/**
	 * Busca todos os pre-manifestos (PM) que tenham a filial origem e filial destino igual as filiais
	 *  passada por parâmetro associados a um controle de carga com a rotaIdaVolta igual ao id passado por parâmetro.
	 *  Ordenados em ordem decresente de DH_GERACAO do manifesto.
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idRotaIdaVolta
	 * @return
	 */
	public List<Manifesto> findPreManifestosByFilialOrigemByFilialDestinoByRotaIdaVolta(Long idFilialOrigem, Long idFilialDestino, Long idRotaIdaVolta){
		return getManifestoDAO().findPreManifestosByFilialOrigemByFilialDestinoByRotaIdaVolta(idFilialOrigem, idFilialDestino, idRotaIdaVolta);
	}

	/**
	 * Verifica se existe algum manifesto com status diferente de CA e com tpEventoManifesto = "EM"
	 * que esteja vinculado ao controle de carga recebido por parâmetro.
	 *
	 * @param idControleCarga
	 * @return True se encontrar algum registro, caso contrário, False.
	 */
	public Boolean validateManifestoByCancelamentoControleCarga(Long idControleCarga) {
		return getManifestoDAO().validateManifestoByCancelamentoControleCarga(idControleCarga);
	}

	/**
	 * Retorna uma List contendo os códigos dos PCEs
	 * @param doctosServicos
	 * @return List contendo objetos Long
	 */
	public List<Long> validatePCE(List<DoctoServico> doctosServicos) {
		Set<Long> set = new HashSet<Long>();
		for(DoctoServico doctoServico : doctosServicos) {
			if (doctoServico.getClienteByIdClienteRemetente()!=null) {
				Long codigoPceRemetente = versaoDescritivoPceService.findCodigoVersaoDescritivoPceByCriteria(
						doctoServico.getClienteByIdClienteRemetente().getIdCliente(),
						Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_CARREGAMENTO),
						Long.valueOf(EventoPce.ID_EVENTO_PCE_GERACAO_PRE_MANIF),
						Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_NO_TERMINAL_GERAC_PRE_MANIF));
				if (codigoPceRemetente != null){
					set.add(codigoPceRemetente);
				}
			}

			if (doctoServico.getClienteByIdClienteDestinatario()!=null) {
				Long codigoPceDestinatario = versaoDescritivoPceService.findCodigoVersaoDescritivoPceByCriteria(
						doctoServico.getClienteByIdClienteDestinatario().getIdCliente(),
						Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_CARREGAMENTO),
						Long.valueOf(EventoPce.ID_EVENTO_PCE_GERACAO_PRE_MANIF),
						Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_NO_TERMINAL_GERAC_PRE_MANIF));
				if (codigoPceDestinatario != null){
					set.add(codigoPceDestinatario);
				}
			}
		}

		return new ArrayList<Long>(set);
	}

	/**
	 * Método que busca uma lista de manifesto a partir do Id da Solicitação de Retirada.
	 * @param idSolicitacaoRetirada
	 * @return
	 */
	public List<Manifesto> findManifestoByIdSolicitacaoRetirada(Long idSolicitacaoRetirada) {
		return this.getManifestoDAO().findManifestoByIdSolicitacaoRetirada(idSolicitacaoRetirada);
	}

	/**
	 * Carrega os dados basicos para a tela de adicionar docto servico pre-manifesto
	 * pop-up de carregarVeiculo.
	 *
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap findManifestoToCarregamentoPopUp(TypedFlatMap criteria) {
		Long idManifesto = criteria.getLong("idManifesto");
		Manifesto manifesto = this.findById(idManifesto);

		TypedFlatMap result = new TypedFlatMap();
		result.put("manifesto.idManifesto", manifesto.getIdManifesto());
		result.put("manifesto.tpManifesto", manifesto.getTpManifesto().getValue());
		result.put("manifesto.tpAbrangencia", manifesto.getTpAbrangencia().getValue());
		result.put("manifesto.nrPreManifesto", manifesto.getNrPreManifesto());
		result.put("manifesto.filialByIdFilialOrigem.sgFilial", manifesto.getFilialByIdFilialOrigem().getSgFilial());
		result.put("manifesto.filialByIdFilialDestino.idFilial", manifesto.getFilialByIdFilialDestino().getIdFilial());
		result.put("manifesto.tpModal", manifesto.getTpModal().getValue());

		//Solicitacao retirada...
		if (manifesto.getSolicitacaoRetirada()!=null) {
			result.put("solicitacaoRetirada.idSolicitacaoRetirada", manifesto.getSolicitacaoRetirada().getIdSolicitacaoRetirada());
			result.put("solicitacaoRetirada.filialRetirada.sgFilial", manifesto.getSolicitacaoRetirada().getFilialRetirada().getSgFilial());
			result.put("solicitacaoRetirada.nrSolicitacaoRetirada", manifesto.getSolicitacaoRetirada().getNrSolicitacaoRetirada());

			//Consignatario...
			if (manifesto.getSolicitacaoRetirada().getConsignatario()!=null) {
				result.put("consignatario.idConsignatario", manifesto.getSolicitacaoRetirada().getConsignatario().getIdCliente());
				result.put("consignatario.nrIdentificacaoConsignatario",
						FormatUtils.formatIdentificacao(
								manifesto.getSolicitacaoRetirada().getConsignatario().getPessoa().getTpIdentificacao().getValue(),
								manifesto.getSolicitacaoRetirada().getConsignatario().getPessoa().getNrIdentificacao()));
				result.put("consignatario.nmConsignatario", manifesto.getCliente().getPessoa().getNmPessoa());
			}
		}

		if (manifesto.getTpManifesto().getValue().equals("V")) {
			result.put("manifesto.tpPreManifesto", manifesto.getTpManifestoViagem().getValue());
		} else {
			result.put("manifesto.tpPreManifesto", manifesto.getTpManifestoEntrega().getValue());

			//Destino...
			Filial filial = SessionUtils.getFilialSessao();
			result.put("filialSessao.idFilialDestino", filial.getIdFilial());
			result.put("filialSessao.sgFilialDestino", filial.getSgFilial());
			result.put("filialSessao.nmFilialDestino", filial.getPessoa().getNmFantasia());

			//Rota..
			if (manifesto.getControleCarga().getRotaColetaEntrega() != null) {
				result.put("rotaColetaEntrega.idRotaColetaEntrega", manifesto.getControleCarga().getRotaColetaEntrega().getIdRotaColetaEntrega());
				result.put("rotaColetaEntrega.nrRota", manifesto.getControleCarga().getRotaColetaEntrega().getNrRota());
				result.put("rotaColetaEntrega.dsRota", manifesto.getControleCarga().getRotaColetaEntrega().getDsRota());
			}
		}

		return result;
	}

	/**
	 *  Retorna o Manifesto apartir do volumeNotaFiscal informado
	 */
	public Manifesto findManifestoByVolumeNotaFiscal(Long idVolumeNotaFiscal){
		return this.getManifestoDAO().findManifestoByVolumeNotaFiscal(idVolumeNotaFiscal);
	}

	/**
	 * Altera a situação dos manifestos relacionados ao controle de carga.
	 *
	 * @param idControleCarga
	 */
	public void updateSituacaoManifestoByIdControleCarga(Long idControleCarga){
		getManifestoDAO().updateSituacaoManifestoByIdControleCarga(idControleCarga);
	}

	/**
	 * Método que retorna um número do manifesto de acordo a abrangência e tipo do manifesto.
	 * O ideal seria que a tabela MANIFESTO possuisse um campo de número o qual tivesse a informação
	 * independente de qual fosse o manifesto. (ERRO DE MODELAGEM)
	 * @param manifesto
	 * @return
	 */
	public String findNrManifestoByManifesto(Manifesto manifesto){
		if (manifesto.getTpManifesto().getValue().equals("V")){
			if (manifesto.getTpAbrangencia().getValue().equals("N")){
				return manifestoViagemNacionalService.findById(manifesto.getIdManifesto()).getNrManifestoOrigem().toString();
			} else {
				return manifestoInternacionalService.findById(manifesto.getIdManifesto()).getNrManifestoInt().toString();
			}
		} else {
			return manifestoEntregaService.findById(manifesto.getIdManifesto()).getNrManifestoEntrega().toString();
		}
	}

	/**
	 *
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public List<TypedFlatMap> findDoctoServicoVinculadosManifestoByIdControleCarga(Long idControleCarga, Long idFilial) {
		List<Map<String, Object>> result = getManifestoDAO().findManifestoNacionalByIdControleCarga(idControleCarga, idFilial);
		result.addAll(getManifestoDAO().findManifestoInternacionalByIdControleCarga(idControleCarga, idFilial));
		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}

	/**
	 * Solicitação da Integração - CQPRO00005515
	 * Criar um método na classe ManifestoService o qual retorne uma instancia da classe Manifesto conforme os parametros especificados.
	 * Nome do metodo: findManifesto(long nrPreManifesto, long idFilialByIdFilialOrigem, String tpManifesto ) : Manifesto
	 * OBS: O parametro tpManifesto é opcional, ou seja o método deve considerar que o valor deste parametro venha setado como null
	 * @param idFilialOrigem
	 * @param nrPreManifesto
	 * @param tpManifesto
	 * @return
	 */
	public Manifesto findManifesto(Long nrPreManifesto, Long idFilialOrigem, String tpManifesto){
		return getManifestoDAO().findManifesto(nrPreManifesto, idFilialOrigem, tpManifesto);
	}

	/**
	 *
	 * @param idControleCarga
	 * @return True, se não achou registros, caso contrário, False.
	 */
	public Boolean findManifestoWithDocumentosEntreguesByControleCarga(Long idControleCarga) {
		return getManifestoDAO().findManifestoWithDocumentosEntreguesByControleCarga(idControleCarga);
	}

	/**
	 *
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public Boolean validateExisteManifestoNaoFinalizado(Long idControleCarga,Long idFilial){
		return getManifestoDAO().validateExisteManifestoNaoFinalizado(idControleCarga,idFilial);
	}

	public Boolean findManifestoVolumeSobra(Long idControleCarga, Long idVolumeNotaFiscal, Long idFilial, String tpManifesto){
		return getManifestoDAO().findManifestoVolumeSobra(idControleCarga, idVolumeNotaFiscal, idFilial, tpManifesto);
	}

	/**
	 * Busca os manifestos para um determinado trecho.
	 *
	 * @param tpManifesto
	 * @param tpStatusManifesto
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public List<Manifesto> findManifestosByTrecho(String tpManifesto, String tpStatusManifesto, Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		return getManifestoDAO().findManifestosByTrecho(tpManifesto, tpStatusManifesto, idControleCarga, idFilialOrigem, idFilialDestino);
	}

	/**
	 * Verifica se existe algum manifesto que não esteja com o Carregamento Concluído na filial do usuário logado.
	 * @param idControleCarga
	 * @return True se encontrar algum manifesto que não esteja com o Carregamento Concluído, caso contrário, False.
	 */
	public Boolean validateExisteManifestoSemCarregamentoConcluido(Long idControleCarga) {
		Integer rows = getManifestoDAO().findManifestoByFinalizarCarregamento(idControleCarga, SessionUtils.getFilialSessao().getIdFilial());
		return rows.intValue() > 0;
	}
	public List<Manifesto> findManifestoByIdControleCargaByFilialDestino(Long idControleCarga, Long idFilial) {
		return getManifestoDAO().findManifestoByIdControleCargaByFilialDestino(idControleCarga , idFilial);
	}

	public List getManifestosTipoEntregaByIdControleCarga(Long idControleCarga) {
		return getManifestoDAO().findManifestosEntregaByIdControleCarga(idControleCarga, "E");
	}

	/**
	 * Verifica se exite algum manifesto emitido para o controle de carga recebido por parâmetro. Se sim, retorna True,
	 * caso contrário, False.
	 *
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public Boolean findExisteManifestoEmitidoByControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		return getManifestoDAO().findExisteManifestoEmitidoByControleCarga(idControleCarga, idFilialOrigem, idFilialDestino);
	}

	public void validateInclusaoManifestosControleCarga(Long idControleCarga, String tpManifestoEntrega) {
		Boolean existe = Boolean.FALSE;
		if("EP".equals(tpManifestoEntrega)) {
			existe = getManifestoDAO().findExisteManifestoTipoEntregaByControleCargaAndTpManifestoEntregaDifEntregaParceira(idControleCarga);
		} else {
			existe = getManifestoDAO().findExisteManifestoTipoEntregaByControleCargaAndTpManifestoEntregaIgualEntregaParceira(idControleCarga);
		}
		if (existe.booleanValue()) {
			throw new BusinessException("LMS-05346");
		}
	}

	public Boolean hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(Long idControleCarga,Long idFilialOrigem){
		return getManifestoDAO().hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(idControleCarga, idFilialOrigem);
	}

	public Integer getRowCountDocumentosConsultarCargasViagem(TypedFlatMap tfm) {
		Long idManifesto = tfm.getLong("idManifesto");
		Manifesto manifestoLoaded = findById(idManifesto);
		return getQuantidadeDoctosByManifesto(manifestoLoaded, false);
	}
	
	
	public void generateEventosDocumentosManifesto(Manifesto manifesto, Short eventoDocumento, Filial filialEvento, Filial filialOrigem, DateTime dhEvento){
		manifesto = findById(manifesto.getIdManifesto());
		eventoManifestoService.generateEventoManifesto(manifesto, filialEvento, "ID",dhEvento);
		if (filialEvento.equals(manifesto.getFilialByIdFilialDestino())){
			manifesto.setTpStatusManifesto(new DomainValue("ED"));
		}else{
			if (!"FE".equals(manifesto.getTpStatusManifesto().getValue())){
				manifesto.setTpStatusManifesto(new DomainValue("EF"));
			}
		}
		
		List<ManifestoNacionalCto> manifestoCtos = manifestoNacionalCtoService.findManifestoNacionalCtosByIdManifestoViagemNacional(manifesto.getIdManifesto());

		Filial filialObservacao = filialService.findByIdJoinPessoa(filialEvento.getIdFilial());
		
		String observacao = filialObservacao.getSgFilial().concat(" - ").concat(filialObservacao.getPessoa().getNmFantasia());
		
		for (ManifestoNacionalCto manifestoCto: manifestoCtos){
			DoctoServico doctoServico = doctoServicoService.findById(manifestoCto.getConhecimento().getIdDoctoServico());
			String nrDocumento = manifesto.getFilialByIdFilialOrigem().getSgFilial() + " " + 
			FormatUtils.formataNrDocumento(manifesto.getManifestoViagemNacional().getNrManifestoOrigem().toString(),"MVN");
			if (!Short.valueOf("1").equals(doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria())
					&& "V".equals(manifesto.getControleCarga().getTpControleCarga().getValue())){
				incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(Short.valueOf("29"),doctoServico.getIdDoctoServico(),filialEvento.getIdFilial(),nrDocumento, dhEvento,null,observacao,"MAV");
			}
			
			List<VolumeNotaFiscal> volumesDocto = volumeNotaFiscalService.findByIdConhecimento(doctoServico.getIdDoctoServico());
			eventoVolumeService.storeEventoVolumeDhOcorrencia(volumesDocto, Short.valueOf("29"), "LM", null, SessionUtils.getUsuarioLogado(), dhEvento);
		}
		
		eventoDispositivoUnitizacaoService.generateEventoByManifesto(manifesto.getIdManifesto(), Short.valueOf("29"), ConstantesSim.TP_SCAN_LMS, null);
		
		store(manifesto);
	}
	
	
	public void generateStatusInicioDescarga(Manifesto manifesto, Filial filialFedex, Filial filialOrigem, Usuario usuario, DateTime dhEvento){
		ControleCarga controleCarga = null;
		manifesto = findById(manifesto.getIdManifesto());
		controleCarga = controleCargaService.findById(manifesto.getControleCarga().getIdControleCarga());
		if (!"AD".equals(manifesto.getTpStatusManifesto().getValue()) ||
				!manifesto.getFilialByIdFilialDestino().equals(filialFedex)){
			throw new BusinessException("LMS-03025", new Object[]{filialOrigem.getSgFilial(),
					FormatUtils.formataNrDocumento(manifesto.getManifestoViagemNacional().getNrManifestoOrigem().toString(), "MVN"),
					filialFedex.getSgFilial()});
		}
		
		
		generateEventosDocumentosManifesto(manifesto, Short.valueOf("29"), filialFedex, filialOrigem, dhEvento);
		
		if(filialFedex.equals(controleCarga.getFilialByIdFilialDestino())){
			controleCarga.setTpStatusControleCarga(new DomainValue("ED"));
		}else{
			controleCarga.setTpStatusControleCarga(new DomainValue("EP"));
		}
		controleCargaService.store(controleCarga);
		
		String nmEquipe = (String) configuracoesFacade.getValorParametro(filialFedex.getIdFilial(), "NOME_EQUIPE_FEDEX");
		
		Equipe equipe = equipeService.findByDsEquipeFilial(nmEquipe, filialFedex);
		
		List<CarregamentoDescarga> carregamentosControleCarga = carregamentoDescargaService.findCarregamentoDescarga(controleCarga.getIdControleCarga(), filialFedex.getIdFilial());
		if (carregamentosControleCarga != null && !carregamentosControleCarga.isEmpty()){
			for (CarregamentoDescarga carregamentoDescarga : carregamentosControleCarga){
				if ("D".equals(carregamentoDescarga.getTpOperacao().getValue()) && 
						("P".equals(carregamentoDescarga.getTpStatusOperacao())
							|| "C".equals(carregamentoDescarga.getTpStatusOperacao()))){
					carregamentoDescarga.setDhInicioOperacao(dhEvento);
					carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
					carregamentoDescarga.setBox(null);
					carregamentoDescargaService.store(carregamentoDescarga);
					
					List<DescargaManifesto> descargaManifestos = descargaManifestoService.findByCarregamentoDescarga(carregamentoDescarga);
					if (descargaManifestos != null && !descargaManifestos.isEmpty()){
						for (DescargaManifesto descargaManifesto : descargaManifestos) {
							descargaManifesto.setDhInicioDescarga(dhEvento);
							descargaManifestoService.store(descargaManifesto);
						}
					}
					break;
				}
			}
		}else{
			CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
			carregamentoDescarga.setDhInicioOperacao(dhEvento);
			carregamentoDescarga.setTpStatusOperacao(new DomainValue("I"));
			carregamentoDescarga.setTpOperacao(new DomainValue("D"));
			carregamentoDescarga.setBox(null);
			carregamentoDescarga.setFilial(filialFedex);
			carregamentoDescarga.setControleCarga(controleCarga);
			carregamentoDescargaService.store(carregamentoDescarga);
			
			EquipeOperacao equipeOperacao = new EquipeOperacao();
			equipeOperacao.setCarregamentoDescarga(carregamentoDescarga);
			equipeOperacao.setControleCarga(controleCarga);
			equipeOperacao.setEquipe(equipe);
			equipeOperacao.setDhInicioOperacao(dhEvento);
			equipeOperacaoService.store(equipeOperacao);

			DescargaManifesto descargaManifesto = new DescargaManifesto();
			descargaManifesto.setCarregamentoDescarga(carregamentoDescarga);
			descargaManifesto.setDhInicioDescarga(dhEvento);
			descargaManifesto.setManifesto(manifesto);
			descargaManifesto.setEquipeOperacao(equipeOperacao);
			descargaManifestoService.store(descargaManifesto);
		}
		
		//Gera evento meio transporte 
		EventoMeioTransporte eventoMeioTransporte = new EventoMeioTransporte();
		eventoMeioTransporte.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
		eventoMeioTransporte.setTpSituacaoMeioTransporte(new DomainValue("EMDE"));
		eventoMeioTransporte.setDhInicioEvento(dhEvento);
		eventoMeioTransporte.setFilial(filialFedex);
		eventoMeioTransporte.setUsuario(usuario);
		eventoMeioTransporte.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
		eventoMeioTransporte.setControleCarga(controleCarga);
		eventoMeioTransporteService.store(eventoMeioTransporte);
		
		if(controleCarga.getMeioTransporteByIdSemiRebocado() != null){
		    EventoMeioTransporte eventoSemiReboque = new EventoMeioTransporte();
		    eventoSemiReboque.setMeioTransporte(controleCarga.getMeioTransporteByIdSemiRebocado());
		    eventoSemiReboque.setTpSituacaoMeioTransporte(new DomainValue("EMDE"));
		    eventoSemiReboque.setDhInicioEvento(dhEvento);
		    eventoSemiReboque.setFilial(filialFedex);
		    eventoSemiReboque.setUsuario(usuario);
		    eventoSemiReboque.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
		    eventoSemiReboque.setControleCarga(controleCarga);
		    eventoMeioTransporteService.store(eventoSemiReboque);
		}
		
		//Gera evento controleCarga
		EventoControleCarga eventoControleCarga = new EventoControleCarga();
		eventoControleCarga.setControleCarga(controleCarga);
		eventoControleCarga.setDhEvento(dhEvento);
		eventoControleCarga.setUsuario(usuario);
		eventoControleCarga.setTpEventoControleCarga(new DomainValue("ID"));
		eventoControleCarga.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
		eventoControleCarga.setDsEvento("Em descarga");
		eventoControleCarga.setFilial(filialFedex);
		eventoControleCargaService.store(eventoControleCarga);
	}
	
	public Boolean validateExisteManifestoComProdutoPerigoso(Long idManifesto) {
		return getManifestoDAO().validateExisteManifestoComProdutoPerigoso(idManifesto);
	}
	
	public Boolean validateExisteManifestoComProdutoControlado(Long idManifesto) {
		return getManifestoDAO().validateExisteManifestoComProdutoControlado(idManifesto);
	}

	// LMS-5261
	public List findDocsManifestadosMesmoDestino(Long idManifesto, Long idDocumento) {
		Manifesto manifesto = findById(idManifesto);
		return getManifestoDAO().findDocsManifestadosMesmoDestino(idManifesto, idDocumento, manifesto.getFilialByIdFilialDestino().getIdFilial());
	}

	public Manifesto findByControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico){
		return getManifestoDAO().findByControleCargaAndDoctoServico(idControleCarga, idDoctoServico);
	}
	
	public Manifesto findManifestoViagemByIdDoctoServicoFilialEvento(Long idDoctoServico, Long idFilialEvento){
		return getManifestoDAO().findManifestoViagemByIdDoctoServicoFilialEvento(idDoctoServico, idFilialEvento);
	}

	// LMSA-6253
	public Manifesto findLastManifestoViagemByIdDoctoServico(Long idDoctoServico) {
		return getManifestoDAO().findLastManifestoViagemByIdDoctoServico(idDoctoServico);
	}

	public List<Long> findManifestoEntregaParceira(Long idDoctoServico, Long idFilialOrigemManifesto){
		return getManifestoDAO().findManifestoEntregaParceira(idDoctoServico, idFilialOrigemManifesto);
	}

	public void setDispCarregIdentificadoService(DispCarregIdentificadoService dispCarregIdentificadoService) {
		this.dispCarregIdentificadoService = dispCarregIdentificadoService;
	}
	public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}
	public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
	public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
		this.localizacaoMercadoriaService = localizacaoMercadoriaService;
	}
	public void setAgendamentoEntregaService(AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}
	public void setSolicitacaoRetiradaService(
			SolicitacaoRetiradaService solicitacaoRetiradaService) {
		this.solicitacaoRetiradaService = solicitacaoRetiradaService;
	}

	public Boolean findVerificaManifestosAssociados(Long idControleCarga){
		return this.getManifestoDAO().findVerificaManifestosAssociados(idControleCarga);
	}

	public ManifestoColetaService getManifestoColetaService() {
		return manifestoColetaService;
	}

	public void setManifestoColetaService(
			ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
	}

	public List<Manifesto> findManifestosByControleCarga(ControleCarga controleCarga) {
		return getManifestoDAO().findManifestosByControleCarga(controleCarga);
	}

	public DomainValue findTpModalByIdControleCargaIdManifesto(Long idControleCarga, Long idManifesto, boolean idManifestoDiferente){
		return getManifestoDAO().findTpModalByIdControleCargaIdManifesto(idControleCarga, idManifesto, idManifestoDiferente);
	}

	public List<Manifesto> findManifestoByControleCargaAndStatusAndFilialDestino(ControleCarga controleCarga, Long idFilialLogada) {
		return getManifestoDAO().findManifestoByControleCargaAndStatusAndFilialDestino(controleCarga, idFilialLogada);
	}

	public List<Manifesto> findManifestoByIdControleCargaAndStatusAndFilialOrigem(Long idControleCarga, Long idFilialOrigem) {
		return getManifestoDAO().findManifestoByIdControleCargaAndStatusAndFilialOrigem(idControleCarga, idFilialOrigem);
	}

	public List<Manifesto> findByIds(List<Long> ids) {
		return getManifestoDAO().findByIds(ids);
	}

	public List<Long> findClientesRemetenteByIdManifesto(Long idManifesto){
		return getManifestoDAO().findClientesRemetenteByIdManifesto(idManifesto);
	}

	public Long findIdUltimoManifestoEntregaOrViagemEntregaDireta(Long idDoctoServico){
		return this.getManifestoDAO().findIdUltimoManifestoEntregaOrViagemEntregaDireta(idDoctoServico);
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}


	public void flushModeParaCommit(){
		AdsmHibernateUtils.setFlushModeToCommit(this.getDao().getAdsmHibernateTemplate());
	}

	public void flushModeParaManual(){
		AdsmHibernateUtils.setFlushModeToManual(this.getDao().getAdsmHibernateTemplate());
	}

	public void flushModeParaAuto(){
		AdsmHibernateUtils.setFlushModeToAuto(this.getDao().getAdsmHibernateTemplate());
	}

	public void flushModeParaAutoComFlushEClear(){
		AdsmHibernateUtils.setFlushModeToAutoComFlushEClear(this.getDao().getAdsmHibernateTemplate());
	}

	public void setDescargaManifestoService(
			DescargaManifestoService descargaManifestoService) {
		this.descargaManifestoService = descargaManifestoService;
	}

	public void setEquipeOperacaoService(EquipeOperacaoService equipeOperacaoService) {
		this.equipeOperacaoService = equipeOperacaoService;
	}

	public void setEquipeService(EquipeService equipeService) {
		this.equipeService = equipeService;
	}

	public void setEventoControleCargaService(
			EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}

	public void setEventoMeioTransporteService(
			EventoMeioTransporteService eventoMeioTransporteService) {
		this.eventoMeioTransporteService = eventoMeioTransporteService;
	}

	public void setEventoVolumeService(EventoVolumeService eventoVolumeService) {
		this.eventoVolumeService = eventoVolumeService;
	}

	public void setManifestoNacionalCtoService(
			ManifestoNacionalCtoService manifestoNacionalCtoService) {
		this.manifestoNacionalCtoService = manifestoNacionalCtoService;
	}
	
	public void setNotaFiscalOperadaService(NotaFiscalOperadaService notaFiscalOperadaService) {
        this.notaFiscalOperadaService = notaFiscalOperadaService;
    }
}