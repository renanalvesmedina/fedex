package com.mercurio.lms.recepcaodescarga.action;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.*;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.*;
import com.mercurio.lms.carregamento.model.service.*;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.constantes.entidades.ConsControleCarga;
import com.mercurio.lms.entrega.model.ManifestoEntrega;
import com.mercurio.lms.entrega.model.ManifestoEntregaVolume;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaVolumeService;
import com.mercurio.lms.entrega.model.service.OcorrenciaEntregaService;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.VolumeNotaFiscalService;
import com.mercurio.lms.fretecarreteiroviagem.model.service.ReciboFreteCarreteiroService;
import com.mercurio.lms.fretecarreteiroviagem.report.EmitirReciboService;
import com.mercurio.lms.gm.report.RelatorioDiscrepanciaService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.EventoPce;
import com.mercurio.lms.vendas.model.OcorrenciaPce;
import com.mercurio.lms.vendas.model.ProcessoPce;

import java.io.Serializable;
import java.util.*;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.recepcaodescarga.finalizarDescargaAction"
 */
public class FinalizarDescargaAction extends MasterDetailAction {

	private static final String TP_CONTROLE_CARGA = "tpControleCarga";
	private static final String ID_CONTROLE_CARGA = "idControleCarga";
	private static final String DISP_CARREG_DESC_QTDE = "dispCarregDescQtde";
	private static final String DISP_CARREG_IDENTIFICADO = "dispCarregIdentificado";
	private static final String ID_DISP_CARREG_DESC_QTDE = "idDispCarregDescQtde";
	private static final String LMS_03004 = "LMS-03004";
	private static final String ID_DISP_CARREG_IDENTIFICADO = "idDispCarregIdentificado";
	private static final String ID_MANIFESTO_ENTREGA_VOLUME = "idManifestoEntregaVolume";
	private static final String ID_MANIFESTO_NACIONAL_VOLUME = "idManifestoNacionalVolume";
	private static final String ID_VOLUME_NOTA_FISCAL = "idVolumeNotaFiscal";
	private static final String VOLUME_NOTA_FISCAL = "volumeNotaFiscal";
	private static final String MANIFESTO_ENTREGA_VOLUME = "manifestoEntregaVolume";
	private static final String MANIFESTO_NACIONAL_VOLUME = "manifestoNacionalVolume";
	private static final String TOT_DOC_CARREGADO_CONFERIDO = "totDocCarregadoConferido";
	private static final String TOT_DOC_CARREGADO_NAO_CONFERIDO = "totDocCarregadoNaoConferido";
	private static final String TOT_DOC_NAO_CARREGADO_CONFERIDO = "totDocNaoCarregadoConferido";
	private static final String ID_FILIAL = "idFilial";
	private static final String TOT_DOC_CARREGADO_SEM_DOC = "totDocCarregadoSemDoc";
	private static final String TOT_DOC_CARREGADO_INCOMPLETO = "totDocCarregadoIncompleto";
	private static final String EXIST_EVENTO_CONTROLE_CARGA = "existEventoControleCarga";
	private static final String DU_TD_UDT_DISPOSITIVO_UNITIZACAO = "dispositivoUnitizacao.tipoDispositivoUnitizacao.dsTipoDispositivoUnitizacao";
	private static final String RELATORIO_DISCREPANCIA_SERVICE = "relatorioDiscrepanciaService";

	private EmpresaService empresaService;
	private DispositivoUnitizacaoService dispositivoUnitizacaoService;
	private TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService;
	private DispCarregDescQtdeService dispCarregDescQtdeService;
	private DispCarregIdentificadoService dispCarregIdentificadoService;
	private ReciboFreteCarreteiroService reciboFreteCarreteiroService;
	private EmitirReciboService emitirReciboService;
	private ControleCargaService controleCargaService;
	private DoctoServicoService doctoServicoService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ManifestoEntregaVolumeService manifestoEntregaVolumeService;
	private ManifestoNacionalVolumeService manifestoNacionalVolumeService;
	private ControleCargaConhScanService controleCargaConhScanService;
	private ConhecimentoService conhecimentoService;
	private OcorrenciaEntregaService ocorrenciaEntregaService;
	private ConfiguracoesFacade configuracoesFacade;
	private RelatorioDiscrepanciaService relatorioDiscrepanciaService;
	private EventoControleCargaService eventoControleCargaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private VolumeSobraService volumeSobraService;

	/**
	 * Declaração serviço principal da Action.
	 */

	public EventoControleCargaService getEventoControleCargaService() {
		return eventoControleCargaService;
	}

	public void setEventoControleCargaService(
			EventoControleCargaService eventoControleCargaService) {
		this.eventoControleCargaService = eventoControleCargaService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}

	public DispositivoUnitizacaoService getDispositivoUnitizacaoService() {
		return dispositivoUnitizacaoService;
	}

	public void setDispositivoUnitizacaoService(
			DispositivoUnitizacaoService dispositivoUnitizacaoService) {
		this.dispositivoUnitizacaoService = dispositivoUnitizacaoService;
	}

	public TipoDispositivoUnitizacaoService getTipoDispositivoUnitizacaoService() {
		return tipoDispositivoUnitizacaoService;
	}

	public void setTipoDispositivoUnitizacaoService(
			TipoDispositivoUnitizacaoService tipoDispositivoUnitizacaoService) {
		this.tipoDispositivoUnitizacaoService = tipoDispositivoUnitizacaoService;
	}

	public DispCarregDescQtdeService getDispCarregDescQtdeService() {
		return dispCarregDescQtdeService;
	}

	public void setDispCarregDescQtdeService(
			DispCarregDescQtdeService dispCarregDescQtdeService) {
		this.dispCarregDescQtdeService = dispCarregDescQtdeService;
	}

	public DispCarregIdentificadoService getDispCarregIdentificadoService() {
		return dispCarregIdentificadoService;
	}

	public void setDispCarregIdentificadoService(
			DispCarregIdentificadoService dispCarregIdentificadoService) {
		this.dispCarregIdentificadoService = dispCarregIdentificadoService;
	}

	public EmitirReciboService getEmitirReciboService() {
		return emitirReciboService;
	}

	public void setEmitirReciboService(EmitirReciboService emitirReciboService) {
		this.emitirReciboService = emitirReciboService;
	}

	public ReciboFreteCarreteiroService getReciboFreteCarreteiroService() {
		return reciboFreteCarreteiroService;
	}

	public void setReciboFreteCarreteiroService(
			ReciboFreteCarreteiroService reciboFreteCarreteiroService) {
		this.reciboFreteCarreteiroService = reciboFreteCarreteiroService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public VolumeNotaFiscalService getVolumeNotaFiscalService() {
		return volumeNotaFiscalService;
	}

	public void setVolumeNotaFiscalService(
			VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}

	public ManifestoEntregaVolumeService getManifestoEntregaVolumeService() {
		return manifestoEntregaVolumeService;
	}

	public void setManifestoEntregaVolumeService(
			ManifestoEntregaVolumeService manifestoEntregaVolumeService) {
		this.manifestoEntregaVolumeService = manifestoEntregaVolumeService;
	}

	public ManifestoNacionalVolumeService getManifestoNacionalVolumeService() {
		return manifestoNacionalVolumeService;
	}

	public void setManifestoNacionalVolumeService(
			ManifestoNacionalVolumeService manifestoNacionalVolumeService) {
		this.manifestoNacionalVolumeService = manifestoNacionalVolumeService;
	}

	public ControleCargaConhScanService getControleCargaConhScanService() {
		return controleCargaConhScanService;
	}

	public void setControleCargaConhScanService(
			ControleCargaConhScanService controleCargaConhScanService) {
		this.controleCargaConhScanService = controleCargaConhScanService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public OcorrenciaEntregaService getOcorrenciaEntregaService() {
		return ocorrenciaEntregaService;
	}

	public void setOcorrenciaEntregaService(
			OcorrenciaEntregaService ocorrenciaEntregaService) {
		this.ocorrenciaEntregaService = ocorrenciaEntregaService;
	}

	public void setRelatorioDiscrepanciaService(RelatorioDiscrepanciaService relatorioDiscrepanciaService) {
		this.relatorioDiscrepanciaService = relatorioDiscrepanciaService;
	}

	/**
	 * Chamadas para metodos diversos da tela
	 */
	public List findTipoDispositivoUnitizacaoByQuantidade(Map criteria) {
		return this.getTipoDispositivoUnitizacaoService().findTipoDispositivoByQuantidade(criteria);
	}

	public List findTipoDispositivoUnitizacaoByIdentificacao(Map criteria) {
		return this.getTipoDispositivoUnitizacaoService().findTipoDispositivoByIdentificacao(criteria);
	}

	public List findLookupEmpresa(Map criteria) {
		return this.getEmpresaService().findLookup(criteria);
	}

	public List findLookupDispositivoUnitizacao(Map criteria) {
		return this.getDispositivoUnitizacaoService().findLookup(criteria);
	}

	public List findLookupVolumeNotaFiscal(Map criteria) {
		return this.getVolumeNotaFiscalService().findLookup(criteria);
	}

	/**
	 * Pega a data atual do sistema
	 */
	public TypedFlatMap getDadosSessao() {
		TypedFlatMap map = new TypedFlatMap();

		Empresa empresa = SessionUtils.getEmpresaSessao();
		Pessoa pessoa = empresa.getPessoa();

		map.put("empresa.idEmpresa", empresa.getIdEmpresa());
		map.put("empresa.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(pessoa));
		map.put("empresa.pessoa.nmPessoa", pessoa.getNmPessoa());

		map.put("dataHoraAtual", JTDateTimeUtils.getDataHoraAtual());

		map.put("blRncAutomaticaNovo", getParametroRncAutomaticaNovo(SessionUtils.getFilialSessao().getIdFilial()));

		return map;
	}

	private boolean getParametroRncAutomaticaNovo(Long idFilialUsuario) {
		String parametroRncAutomaticaNovo = (String) conteudoParametroFilialService.findConteudoByNomeParametro(idFilialUsuario, "RNC_AUTOMATICA_NOVO", false);
		return "S".equals(parametroRncAutomaticaNovo);
	}

	/**
	 * #######################################
	 * # Inicio dos métodos para tela de DF2 #
	 * #######################################
	 */
	public TypedFlatMap store(TypedFlatMap parameters) throws Exception {

		MasterEntry entry = getMasterFromSession(parameters.getLong("idCarregamentoDescarga"), true);

		TypedFlatMap retorno = carregamentoDescargaService.storeFimDescarga(parameters);

		Map email = (Map) parameters.get(RELATORIO_DISCREPANCIA_SERVICE);
		if (email != null && !email.isEmpty()) {
			this.relatorioDiscrepanciaService.executeReport(email);
		}

		updateMasterInSession(entry);

		Long idControleCarga = parameters.getLong(ConsControleCarga.CONTROLE_CARGA_ID_CONTROLE_CARGA);
		carregamentoDescargaService.executeEnviarFinalizarIntegracaoSmp(idControleCarga);
		return retorno;

	}

	/**
	 * Salva a referencia do objeto Master na sessão.
	 * não devem ser inicializadas as coleções que representam os filhos
	 * já que o usuário pode vir a não utilizar a aba de filhos, evitando assim
	 * a carga desnecessária de objetos na sessão e a partir do banco de dados.
	 *
	 * @param id
	 */
	public Object findById(java.lang.Long id) {
		Object masterObj = this.carregamentoDescargaService.findById(id);
		putMasterInSession(masterObj);
		return masterObj;
	}

	/**
	 * Remoção de um conjunto de registros Master.
	 */
	public void removeByIds(List ids) {
		this.carregamentoDescargaService.removeByIds(ids);
	}

	/**
	 * Remoção de um registro Master.
	 */
	public void removeById(java.lang.Long id) {
		this.carregamentoDescargaService.removeById(id);
		newMaster();
	}

	/**
	 * Salva um item na sessão.
	 */
	public Serializable saveDispCarregDescQtde(Map parameters) {
		// verificar se o item ja foi cadastrado
		Long masterId = (Long) getMasterId(parameters);

		// Guarda o id do registro, caso ele esteja sendo editado
		Long idDispCarregDescQtde = parameters.get(ID_DISP_CARREG_DESC_QTDE) == null ||
				"".equals((String) parameters.get(ID_DISP_CARREG_DESC_QTDE)) ? null : Long.valueOf((String)parameters.get(ID_DISP_CARREG_DESC_QTDE));


		MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList items = getItemsFromSession(entry, DISP_CARREG_DESC_QTDE);
		if (items != null) {
			ItemListConfig config = getMasterConfig().getItemListConfig(DISP_CARREG_DESC_QTDE);
			for (Iterator iter = items.iterator(getMasterId(parameters), config); iter.hasNext();) {
				DispCarregDescQtde dispCarregDescQtde = (DispCarregDescQtde) iter.next();
				// Se já tem um dispositivo cadastrado lança uma exception
				Map tipoDispositivoUnitizacao = (Map)parameters.get("tipoDispositivoUnitizacao");
				Map empresa = (Map)parameters.get("empresa");

				if ((dispCarregDescQtde.getTipoDispositivoUnitizacao() != null &&
						dispCarregDescQtde.getTipoDispositivoUnitizacao().getIdTipoDispositivoUnitizacao().compareTo(
								Long.valueOf((String) tipoDispositivoUnitizacao.get("idTipoDispositivoUnitizacao"))) == 0) &&
						(dispCarregDescQtde.getEmpresa() != null &&
								dispCarregDescQtde.getEmpresa().getIdEmpresa().compareTo(
										Long.valueOf((String) empresa.get("idEmpresa"))) == 0) && idDispCarregDescQtde == null) {
					throw new BusinessException(LMS_03004);
				}
			}
		}

		return saveItemInstance(parameters, DISP_CARREG_DESC_QTDE);
	}

	public ResultSetPage findPaginatedDispCarregDescQtde(TypedFlatMap parameters) {
		return findPaginatedItemList(parameters, DISP_CARREG_DESC_QTDE);
	}

	public Integer getRowCountDispCarregDescQtde(TypedFlatMap parameters){
		return getRowCountItemList(parameters, DISP_CARREG_DESC_QTDE);
	}

	public Object findByIdDispCarregDescQtde(MasterDetailKey key) {
		return (DispCarregDescQtde) findItemById(key, DISP_CARREG_DESC_QTDE);
	}

	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsDispCarregDescQtde(List ids) {
		super.removeItemByIds(ids, DISP_CARREG_DESC_QTDE);
	}

	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		/**
		 * Declaracao da classe pai
		 */
		MasterEntryConfig config = masterFactory.createMasterEntryConfig(CarregamentoDescarga.class, true);

		/**
		 * Esta classe e reponsavel por ordenar a List dos filhos que estao
		 * em memoria de acordo com as regras de negocio
		 */
		Comparator descComparator1 = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				DispCarregDescQtde dispCarregDescQtde1 = (DispCarregDescQtde)obj1;
				DispCarregDescQtde dispCarregDescQtde2 = (DispCarregDescQtde)obj2;
				return dispCarregDescQtde1.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().toString()
						.compareTo(dispCarregDescQtde2.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().toString());
			}
		};

		Comparator descComparator2 = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				DispCarregIdentificado dispCarregIdentificado1 = (DispCarregIdentificado)obj1;
				DispCarregIdentificado dispCarregIdentificado2 = (DispCarregIdentificado)obj2;
				return dispCarregIdentificado1.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().toString()
						.compareTo(dispCarregIdentificado2.getDispositivoUnitizacao().getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao().toString());
			}
		};

		Comparator descComparator3 = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				ManifestoEntregaVolume manifestoEntregaVolume1 = (ManifestoEntregaVolume)obj1;
				ManifestoEntregaVolume manifestoEntregaVolume2 = (ManifestoEntregaVolume)obj2;
				return manifestoEntregaVolume1.getVolumeNotaFiscal().getTpVolume()
						.compareTo(manifestoEntregaVolume2.getVolumeNotaFiscal().getTpVolume());
			}
		};

		Comparator descComparator4 = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				ManifestoNacionalVolume manifestoNacionalVolume1 = (ManifestoNacionalVolume)obj1;
				ManifestoNacionalVolume manifestoNacionalVolume2 = (ManifestoNacionalVolume)obj2;
				return manifestoNacionalVolume1.getVolumeNotaFiscal().getTpVolume()
						.compareTo(manifestoNacionalVolume2.getVolumeNotaFiscal().getTpVolume());
			}
		};


		/**
		 * Esta instancia é responsavel por carregar os
		 * items filhos na sessão a partir do banco de dados.
		 */
		/**
		 * ItemConfig para DispositivoUnitizacao Sem Identificacao
		 */
		ItemListConfig itemInit = getItemListConfigDispositivoUnitizacaoSemIdentificacao();


		/**
		 * ItemConfig para DispositivoUnitizacao Com Identificacao
		 */
		ItemListConfig itemInit2 = getItemListConfigDispositivoUnitizacaoComIdentificacao();


		/**
		 * ItemConfig para Volumes
		 */
		ItemListConfig itemInit3 = getItemListConfigManifestoEntregaVolumes();


		/**
		 * ItemConfig para Volumes
		 */
		ItemListConfig itemInit4 = getItemListConfigManifestoNacionalVolumes();

		/**
		 * Seta as configuracoes do filho.
		 */
		config.addItemConfig(DISP_CARREG_DESC_QTDE, DispCarregDescQtde.class, itemInit, descComparator1);
		config.addItemConfig(DISP_CARREG_IDENTIFICADO, DispCarregIdentificado.class, itemInit2, descComparator2);
		config.addItemConfig(MANIFESTO_ENTREGA_VOLUME, ManifestoEntregaVolume.class, itemInit3, descComparator3);
		config.addItemConfig(MANIFESTO_NACIONAL_VOLUME, ManifestoNacionalVolume.class, itemInit4, descComparator4);
		return config;
	}

	private ItemListConfig getItemListConfigManifestoNacionalVolumes() {
		return new ItemListConfig() {

			public List initialize(Long masterId, Map parameters) {
				return getManifestoNacionalVolumeService().findManifestoNacionalVolumes(masterId);
			}

			public Integer getRowCount(Long masterId, Map parameters) {
				return getManifestoNacionalVolumeService().getRowCountManifestoNacionalVolumes(masterId);
			}

			/**
			 * Seta um pai para o itemConfig de Volume
			 */
			public void setMasterOnItem(Object master, Object itemBean) {
				((ManifestoNacionalVolume) itemBean).setManifestoViagemNacional(new ManifestoViagemNacional());
			}

			public void modifyItemValues(Object newBean, Object bean) {
				ManifestoNacionalVolume idsNew = (ManifestoNacionalVolume) newBean;
				ManifestoNacionalVolume idsOld = (ManifestoNacionalVolume) bean;

				idsNew.setVolumeNotaFiscal(idsOld.getVolumeNotaFiscal());
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				ManifestoNacionalVolume manifestoNacionalVolume = (ManifestoNacionalVolume) bean;

				if(!"".equals((String) parameters.get(ID_MANIFESTO_NACIONAL_VOLUME))) {
					manifestoNacionalVolume.setIdManifestoNacionalVolume(Long.valueOf((String)parameters.get(ID_MANIFESTO_NACIONAL_VOLUME)));
				}

				VolumeNotaFiscal volume = getVolumeNotaFiscalService().findById(Long.valueOf((String)((Map) parameters.get(VOLUME_NOTA_FISCAL))
						.get(ID_VOLUME_NOTA_FISCAL)));
				manifestoNacionalVolume.setVolumeNotaFiscal(volume);


				return manifestoNacionalVolume;
			}

		};
	}

	private ItemListConfig getItemListConfigManifestoEntregaVolumes() {
		return new ItemListConfig() {

			public List initialize(Long masterId, Map parameters) {
				return getManifestoEntregaVolumeService().findManifestoEntregaVolumes(masterId);
			}

			public Integer getRowCount(Long masterId, Map parameters) {
				return getManifestoEntregaVolumeService().getRowCountManifestoEntregaVolumes(masterId);
			}

			/**
			 * Seta um pai para o itemConfig de Volume
			 */
			public void setMasterOnItem(Object master, Object itemBean) {
				((ManifestoEntregaVolume) itemBean).setManifestoEntrega(new ManifestoEntrega());
			}

			public void modifyItemValues(Object newBean, Object bean) {
				ManifestoEntregaVolume idsNew = (ManifestoEntregaVolume) newBean;
				ManifestoEntregaVolume idsOld = (ManifestoEntregaVolume) bean;

				idsNew.setVolumeNotaFiscal(idsOld.getVolumeNotaFiscal());
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				ManifestoEntregaVolume manifestoEntregaVolume = (ManifestoEntregaVolume) bean;

				if(!"".equals((String) parameters.get(ID_MANIFESTO_ENTREGA_VOLUME))) {
					manifestoEntregaVolume.setIdManifestoEntregaVolume(Long.valueOf((String)parameters.get(ID_MANIFESTO_ENTREGA_VOLUME)));
				}

				VolumeNotaFiscal volume = getVolumeNotaFiscalService().findById(Long.valueOf((String)((Map) parameters.get(VOLUME_NOTA_FISCAL))
						.get(ID_VOLUME_NOTA_FISCAL)));
				manifestoEntregaVolume.setVolumeNotaFiscal(volume);

				return manifestoEntregaVolume;
			}

		};
	}

	private ItemListConfig getItemListConfigDispositivoUnitizacaoComIdentificacao() {
		return new ItemListConfig() {

			public List initialize(Long masterId, Map parameters) {
				return getDispCarregIdentificadoService().findDispCarregIdentificadoByIdCarregamentoDescarga(masterId);
			}

			public Integer getRowCount(Long masterId, Map parameters) {
				return getDispCarregIdentificadoService().getRowCountDispCarregIdentificadoByIdCarregamentoDescarga(masterId);
			}

			/**
			 * Seta um pai para o itemConfig de DispositivoUnitizacao
			 */
			public void setMasterOnItem(Object master, Object itemBean) {
				CarregamentoDescarga carregamentoDescarga = new CarregamentoDescarga();
				((DispCarregIdentificado) itemBean).setCarregamentoDescarga(carregamentoDescarga);
			}

			public void modifyItemValues(Object newBean, Object bean) {
				DispCarregIdentificado idsNew = (DispCarregIdentificado) newBean;
				DispCarregIdentificado idsOld = (DispCarregIdentificado) bean;

				idsNew.setDispositivoUnitizacao(idsOld.getDispositivoUnitizacao());
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) bean;

				if(!"".equals((String) parameters.get(ID_DISP_CARREG_IDENTIFICADO))) {
					dispCarregIdentificado.setIdDispCarregIdentificado(Long.valueOf((String)parameters.get(ID_DISP_CARREG_IDENTIFICADO)));
				}

				DispositivoUnitizacao dispositivoUnitizacao = getDispositivoUnitizacaoService().findById(Long.valueOf((String)((Map) parameters.get("dispositivoUnitizacao"))
						.get("idDispositivoUnitizacao")));
				dispCarregIdentificado.setDispositivoUnitizacao(dispositivoUnitizacao);

				dispCarregIdentificado.setCarregamentoDescarga(null);
				dispCarregIdentificado.setCarregamentoPreManifesto(null);

				return dispCarregIdentificado;
			}

		};
	}

	private ItemListConfig getItemListConfigDispositivoUnitizacaoSemIdentificacao() {
		return new ItemListConfig() {

			/**
			 * Find paginated do filho
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 *
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */
			public List initialize(Long masterId, Map parameters) {
				return new ArrayList();
			}

			/**
			 * Busca rowCount da grid da tela filha
			 * Passa por este ponto apenas na primeira vez em que a list filha e chamada.
			 * Apos a primeira vez ela e carregada da memoria
			 *
			 * @param masterId id do pai
			 * @param parameters todos os parametros vindo da tela pai
			 */
			public Integer getRowCount(Long masterId, Map parameters) {
				return 0;
			}

			/**
			 * Chama esta funcao depois de editar um item da grid filho
			 * E retira atributos desnecessarios para o filho
			 *
			 * @param newBean
			 * @param bean
			 */
			public void modifyItemValues(Object newBean, Object bean) {
				Set ignore = new HashSet();
				ignore.add(ID_DISP_CARREG_DESC_QTDE); // id do filho
				ignore.add("versao");
				ignore.add("carregamentoDescarga"); // classe pai

				ReflectionUtils.syncObjectProperties(bean, newBean, ignore);
			}

			/**
			 * Todos os dados a serem carregados na grid pelo form passam antes por este
			 * metodo. Para se fazer uma validacao...
			 * Recomenda-se que o bean em questao seja gerado nesta classe a partir dos
			 * parametros enviados da tela para se evitar um 'ReflectionUtils'
			 *
			 * @param parameters
			 * @param bean a ser istanciado
			 * @return Object bean instanciado
			 */
			public Object populateNewItemInstance(Map parameters, Object bean) {
				DispCarregDescQtde dispCarregDescQtde = (DispCarregDescQtde) bean;

				if(!"".equals((String) parameters.get(ID_DISP_CARREG_DESC_QTDE))) {
					dispCarregDescQtde.setIdDispCarregDescQtde(Long.valueOf((String)parameters.get(ID_DISP_CARREG_DESC_QTDE)));
				}

				dispCarregDescQtde.setQtDispositivo(Integer.valueOf((String) parameters.get("qtDispositivo")));

				Long idTipoDispositivoUnitizacao = Long.valueOf((String)((Map) parameters.get("tipoDispositivoUnitizacao")).get("idTipoDispositivoUnitizacao"));
				TipoDispositivoUnitizacao tipoDispositivoUnitizacao = getTipoDispositivoUnitizacaoService().findById(idTipoDispositivoUnitizacao);
				dispCarregDescQtde.setTipoDispositivoUnitizacao(tipoDispositivoUnitizacao);

				Long idEmpresa = Long.valueOf((String)((Map) parameters.get("empresa")).get("idEmpresa"));
				Empresa empresa = getEmpresaService().findById(idEmpresa);
				dispCarregDescQtde.setEmpresa(empresa);

				dispCarregDescQtde.setCarregamentoDescarga(null);
				dispCarregDescQtde.setCarregamentoPreManifesto(null);

				return dispCarregDescQtde;
			}

		};
	}

	/**
	 * ####################################
	 * # Fim dos métodos para tela de DF2 #
	 * ####################################
	 */


	/**
	 * ########################################################################
	 * # Inicio dos métodos para tela de DF2 de Dispositivo Com Identificação #
	 * ########################################################################
	 */

	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	public Serializable saveDispCarregIdentificado(Map parameters) {
		// verificar se o item ja foi cadastrado
		Long masterId = (Long) getMasterId(parameters);

		// Guarda o id do registro, caso ele esteja sendo editado
		Long idDispCarregIdentificado = parameters.get(ID_DISP_CARREG_IDENTIFICADO) == null ||
				"".equals((String) parameters.get(ID_DISP_CARREG_IDENTIFICADO)) ? null : Long.valueOf((String)parameters.get(ID_DISP_CARREG_IDENTIFICADO));

		MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList items = getItemsFromSession(entry, DISP_CARREG_IDENTIFICADO);
		if (items != null) {
			ItemListConfig config = getMasterConfig().getItemListConfig(DISP_CARREG_IDENTIFICADO);
			for (Iterator iter = items.iterator(getMasterId(parameters), config); iter.hasNext();) {
				DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) iter.next();
				// Se já tem um dispositivo cadastrado lança uma exception
				Map dispositivoUnitizacao = (Map)parameters.get("dispositivoUnitizacao");

				if ((dispCarregIdentificado.getDispositivoUnitizacao() != null &&
						dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao().compareTo(
								Long.valueOf((String) dispositivoUnitizacao.get("idDispositivoUnitizacao"))) == 0) &&
						idDispCarregIdentificado == null) {
					throw new BusinessException(LMS_03004);
				}
			}
		}

		return saveItemInstance(parameters, DISP_CARREG_IDENTIFICADO);
	}



	public ResultSetPage findPaginatedDispCarregIdentificado(TypedFlatMap parameters) {
		TypedFlatMap parameters2 = new TypedFlatMap(parameters);

		ResultSetPage result = findPaginatedItemList(parameters, DISP_CARREG_IDENTIFICADO);

		List listDispositivosComIdentificacao = new ArrayList();
		/**
		 * Busca do Banco se existe dispositivos já adicionados a esse controle de carga
		 */

		Long idCarregamentoDescarga = Long.parseLong(parameters.get("idCarregamentoDescarga").toString());

		ControleCarga controleCarga = controleCargaService.findByIdCarregamentoDescarga(idCarregamentoDescarga);


		if(controleCarga!=null){
			Long idControleCarga = controleCarga.getIdControleCarga();
			List<DispCarregIdentificado> listDispCarregIdentificado = getDispCarregIdentificadoService().findListDispCarregIdentificadoByControleCarga(idControleCarga);
			for (DispCarregIdentificado dispCarregIdentificado : listDispCarregIdentificado) {
				//Se o result já esta populado não salva na instancia as informações do banco.
				manterDispCarregadoIdentificado(parameters, result, idCarregamentoDescarga, dispCarregIdentificado);
			}
		}


		for(int i=0; i < result.getList().size(); i++) {
			DispCarregIdentificado dispCarregIdentificado = (DispCarregIdentificado) result.getList().get(i);

			Map mapDispIdent = new HashMap();
			mapDispIdent.put(ID_DISP_CARREG_IDENTIFICADO, dispCarregIdentificado.getIdDispCarregIdentificado());
			DispositivoUnitizacao dispositivoUnitizacao = dispCarregIdentificado.getDispositivoUnitizacao();
			mapDispIdent.put(DU_TD_UDT_DISPOSITIVO_UNITIZACAO, dispositivoUnitizacao.getTipoDispositivoUnitizacao().getDsTipoDispositivoUnitizacao());
			mapDispIdent.put("dispositivoUnitizacao.empresa.pessoa.nmPessoa", dispositivoUnitizacao.getEmpresa().getPessoa().getNmPessoa());
			mapDispIdent.put("dispositivoUnitizacao.nrIdentificacao", dispositivoUnitizacao.getNrIdentificacao());

			listDispositivosComIdentificacao.add(mapDispIdent);
		}
		result = findPaginatedItemList(parameters2, DISP_CARREG_IDENTIFICADO);

		return result;
	}

	private void manterDispCarregadoIdentificado(TypedFlatMap parameters, ResultSetPage result, Long idCarregamentoDescarga, DispCarregIdentificado dispCarregIdentificado) {
		if(!result.getList().isEmpty()){
			for(int i=0; i < result.getList().size(); i++) {
				DispCarregIdentificado dispCarreg = (DispCarregIdentificado) result.getList().get(i);
				if (!dispCarregIdentificado.getDispositivoUnitizacao().getIdDispositivoUnitizacao().equals(dispCarreg.getDispositivoUnitizacao().getIdDispositivoUnitizacao())
						&& dispCarregIdentificado.getIdDispCarregIdentificado().equals(dispCarreg.getIdDispCarregIdentificado())) {
					Long masterId = (Long) getMasterId(parameters);
					CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findById(idCarregamentoDescarga);
					dispCarregIdentificado.setCarregamentoDescarga(carregamentoDescarga);
					saveItemInstanceOnSession(masterId, dispCarregIdentificado, DISP_CARREG_IDENTIFICADO);
				}
			}
		}else{
			Long masterId = (Long) getMasterId(parameters);
			CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findById(idCarregamentoDescarga);
			dispCarregIdentificado.setCarregamentoDescarga(carregamentoDescarga);
			saveItemInstanceOnSession(masterId, dispCarregIdentificado, DISP_CARREG_IDENTIFICADO);
		}
	}

	public Integer getRowCountDispCarregIdentificado(TypedFlatMap parameters){
		return getRowCountItemList(parameters, DISP_CARREG_IDENTIFICADO);
	}

	public Object findByIdDispCarregIdentificado(MasterDetailKey key) {
		return (DispCarregIdentificado) findItemById(key, DISP_CARREG_IDENTIFICADO);
	}

	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsDispCarregIdentificado(List ids) {
		super.removeItemByIds(ids, DISP_CARREG_IDENTIFICADO);
	}

	/**
	 * #####################################################################
	 * # Fim dos métodos para tela de DF2 de Dispositivo Com Identificação #
	 * #####################################################################
	 */

	public TypedFlatMap verificaPossibilidadeCriacaoRNCautomatica(TypedFlatMap parameters){
		TypedFlatMap retorno = new TypedFlatMap();
		if(SessionUtils.getFilialSessao().getBlRncAutomaticaDescarga() && "V".equals(parameters.getString(TP_CONTROLE_CARGA))){
			Long idControleCarga = parameters.getLong(ID_CONTROLE_CARGA);
			Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
			List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = ocorrenciaNaoConformidadeService.executeVerificacaoPossibilidadeCriacaoRNCautomaticaDescarga(idControleCarga, idFilial);
			if(listaDocVolmRNCcriacaoAutomatica != null){
				retorno.put("rncAutomaticaMessage", ocorrenciaNaoConformidadeService.getRncAutomaticaMessage(listaDocVolmRNCcriacaoAutomatica));
				retorno.put("listaDocVolmRNCcriacaoAutomatica", listaDocVolmRNCcriacaoAutomatica);
			}
		}
		return retorno;
	}

	public TypedFlatMap abreRNCautomatica(TypedFlatMap parameters){
		TypedFlatMap retorno = new TypedFlatMap();
		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = parameters.getList("listaDocVolmRNCcriacaoAutomatica");
		Long idControleCarga = parameters.getLong(ID_CONTROLE_CARGA);
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();

		if(listaDocVolmRNCcriacaoAutomatica != null){
			ocorrenciaNaoConformidadeService.executeCriacaoRNCautomaticaCarregamento(idControleCarga, idFilial, listaDocVolmRNCcriacaoAutomatica, true);
		}
		return retorno;
	}

	/**
	 * Faz a validacao do PCE.
	 *
	 * @param criteria
	 * @return
	 */
	public TypedFlatMap validatePCE(TypedFlatMap criteria) {
		Long idControleCarga = criteria.getLong(ID_CONTROLE_CARGA);
		Long cdProcesso;
		Long cdEvento;
		Long cdOcorrencia;

		if ("V".equals(criteria.getString(TP_CONTROLE_CARGA)) &&
				this.ocorrenciaEntregaService.validateVolEtiquetadoGMDireto(idControleCarga)) {
			new WarningCollector(configuracoesFacade.getMensagem("LMS-46005"));
			//throw new BusinessException("LMS-46005"); // Exceção de negocio
		}
		if ("C".equals(criteria.getString(TP_CONTROLE_CARGA))) {
			cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_RECEPCAO);
			cdEvento = Long.valueOf(EventoPce.ID_EVENTO_PCE_TERMINO_DESCARGA_RECEPCAO);
			cdOcorrencia = Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_TERM_DESCARGA_TERMINO_DESCARGA_RECEPCAO);
		} else {
			cdProcesso = Long.valueOf(ProcessoPce.ID_PROCESSO_PCE_DESCARGA);
			cdEvento = Long.valueOf(EventoPce.ID_EVENTO_PCE_TERMINO_DESCARGA);
			cdOcorrencia = Long.valueOf(OcorrenciaPce.ID_OCORR_PCE_TERM_DESCARGA_TERMINO_DESCARGA);
		}

		TypedFlatMap mapRetorno = new TypedFlatMap();
		mapRetorno.put("codigos", this.carregamentoDescargaService.validatePCE(idControleCarga, cdProcesso, cdEvento, cdOcorrencia));
		WarningCollectorUtils.putAll(mapRetorno);
		return mapRetorno;
	}



	/**
	 * ########################################################################
	 * # Inicio dos métodos para tela de DF2 de Volumes Manifesto Entrega	  #
	 * ########################################################################
	 */

	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	public Serializable saveManifestoEntregaVolume(Map parameters) {
		// verificar se o item ja foi cadastrado
		Long masterId = (Long) getMasterId(parameters);

		// Guarda o id do registro, caso ele esteja sendo editado
		Long idManifestoEntregaVolume = parameters.get(ID_MANIFESTO_ENTREGA_VOLUME) == null ||
				"".equals((String) parameters.get(ID_MANIFESTO_ENTREGA_VOLUME)) ? null : Long.valueOf((String)parameters.get(ID_MANIFESTO_ENTREGA_VOLUME));

		MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList items = getItemsFromSession(entry, MANIFESTO_ENTREGA_VOLUME);
		if (items != null) {
			ItemListConfig config = getMasterConfig().getItemListConfig(MANIFESTO_ENTREGA_VOLUME);
			for (Iterator iter = items.iterator(getMasterId(parameters), config); iter.hasNext();) {
				ManifestoEntregaVolume manifestoEntregaVolume = (ManifestoEntregaVolume) iter.next();
				// Se já tem um volume cadastrado lança uma exception
				Map volumeNotaFiscal = (Map)parameters.get(VOLUME_NOTA_FISCAL);

				if ((manifestoEntregaVolume.getVolumeNotaFiscal() != null &&
						manifestoEntregaVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal().compareTo(
								Long.valueOf((String) volumeNotaFiscal.get(ID_VOLUME_NOTA_FISCAL))) == 0)
						&& idManifestoEntregaVolume == null) {
					throw new BusinessException(LMS_03004);
				}
			}
		}

		return saveItemInstance(parameters, MANIFESTO_ENTREGA_VOLUME);
	}

	public ResultSetPage findPaginatedManifestoEntregaVolume(TypedFlatMap parameters) {
		return findPaginatedItemList(parameters, MANIFESTO_ENTREGA_VOLUME);
	}

	public Integer getRowCountManifestoEntregaVolume(TypedFlatMap parameters){
		return getRowCountItemList(parameters, MANIFESTO_ENTREGA_VOLUME);
	}

	public Object findByIdManifestoEntregaVolume(MasterDetailKey key) {
		return (DispCarregIdentificado) findItemById(key, MANIFESTO_ENTREGA_VOLUME);
	}

	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsManifestoEntregaVolume(List ids) {
		super.removeItemByIds(ids, MANIFESTO_ENTREGA_VOLUME);
	}

	/**
	 * #####################################################################
	 * # Fim dos métodos para tela de DF2 de Volumes Manifesto Entrega	   #
	 * #####################################################################
	 */



	/**
	 * ########################################################################
	 * # Inicio dos métodos para tela de DF2 de Volumes Manifesto Nacional	  #
	 * ########################################################################
	 */

	/**
	 * Salva um item Descrição Padrão na sessão.
	 */
	public Serializable saveManifestoNacionalVolume(Map parameters) {
		// verificar se o item ja foi cadastrado
		Long masterId = (Long) getMasterId(parameters);

		// Guarda o id do registro, caso ele esteja sendo editado
		Long idManifestoNacionalVolume = parameters.get(ID_MANIFESTO_NACIONAL_VOLUME) == null ||
				"".equals((String) parameters.get(ID_MANIFESTO_NACIONAL_VOLUME)) ? null : Long.valueOf((String)parameters.get(ID_MANIFESTO_NACIONAL_VOLUME));

		MasterEntry entry = getMasterFromSession(masterId, true);
		ItemList items = getItemsFromSession(entry, MANIFESTO_NACIONAL_VOLUME);
		if (items != null) {
			ItemListConfig config = getMasterConfig().getItemListConfig(MANIFESTO_NACIONAL_VOLUME);
			for (Iterator iter = items.iterator(getMasterId(parameters), config); iter.hasNext();) {
				ManifestoNacionalVolume manifestoNacionalVolume = (ManifestoNacionalVolume) iter.next();
				// Se já tem um volume cadastrado lança uma exception
				Map volumeNotaFiscal = (Map)parameters.get(VOLUME_NOTA_FISCAL);

				if ((manifestoNacionalVolume.getVolumeNotaFiscal() != null &&
						manifestoNacionalVolume.getVolumeNotaFiscal().getIdVolumeNotaFiscal().compareTo(
								Long.valueOf((String) volumeNotaFiscal.get(ID_VOLUME_NOTA_FISCAL))) == 0) &&
						idManifestoNacionalVolume == null) {
					throw new BusinessException(LMS_03004);
				}
			}
		}

		return saveItemInstance(parameters, MANIFESTO_NACIONAL_VOLUME);
	}

	public ResultSetPage findPaginatedManifestoNacionalVolume(TypedFlatMap parameters) {
		return findPaginatedItemList(parameters, MANIFESTO_NACIONAL_VOLUME);
	}

	public Integer getRowCountManifestoNacionalVolume(TypedFlatMap parameters){
		return getRowCountItemList(parameters, MANIFESTO_NACIONAL_VOLUME);
	}

	public Object findByIdManifestoNacionalVolume(MasterDetailKey key) {
		return (DispCarregIdentificado) findItemById(key, MANIFESTO_NACIONAL_VOLUME);
	}

	/***
	 * Remove uma lista de registros items.
	 *
	 * @param ids ids dos desciçoes item a serem removidos.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIdsManifestoNacionalVolume(List ids) {
		super.removeItemByIds(ids, MANIFESTO_NACIONAL_VOLUME);
	}

	/**
	 * #####################################################################
	 * # Fim dos métodos para tela de DF2 de Volumes Manifesto Nacional	   #
	 * #####################################################################
	 */
	public TypedFlatMap totalConhecimentos(final TypedFlatMap criteria){
		final String tpOperacao = "D";//Descarregar
		final Long idControleCarga = criteria.getLong(ID_CONTROLE_CARGA);
		final Long idFilial = criteria.getLong(ID_FILIAL);
		final TypedFlatMap map = new TypedFlatMap();

		/** Valida Parametros */
		if(LongUtils.hasValue(idControleCarga) && LongUtils.hasValue(idFilial)) {
			final CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findCarregamentoDescarga(idControleCarga, idFilial, tpOperacao);
			if(carregamentoDescarga != null) {
				final Long idCarregamentoDescarga = carregamentoDescarga.getIdCarregamentoDescarga();
				map.put(TOT_DOC_CARREGADO_CONFERIDO, controleCargaConhScanService.getRowCountDocCarregadoConferido(idControleCarga, idCarregamentoDescarga));
				map.put(TOT_DOC_CARREGADO_NAO_CONFERIDO, controleCargaConhScanService.getRowCountDocCarregadoNaoConferido(idControleCarga, idCarregamentoDescarga));
				map.put(TOT_DOC_NAO_CARREGADO_CONFERIDO, controleCargaConhScanService.getRowCountDocNaoCarregadoConferido(idControleCarga, idCarregamentoDescarga));
			} else {
				map.put(TOT_DOC_CARREGADO_CONFERIDO, LongUtils.ZERO);
				map.put(TOT_DOC_CARREGADO_NAO_CONFERIDO, LongUtils.ZERO);
				map.put(TOT_DOC_NAO_CARREGADO_CONFERIDO, LongUtils.ZERO);
			}
			map.put(TOT_DOC_CARREGADO_SEM_DOC, conhecimentoService.getRowCountCarregadosSemPreManifestoDocumento(idControleCarga));
			map.put(TOT_DOC_CARREGADO_INCOMPLETO, conhecimentoService.getRowCountCarregadosIncompletos(idControleCarga));
		} else {
			map.put(TOT_DOC_CARREGADO_CONFERIDO, LongUtils.ZERO);
			map.put(TOT_DOC_CARREGADO_NAO_CONFERIDO, LongUtils.ZERO);
			map.put(TOT_DOC_NAO_CARREGADO_CONFERIDO, LongUtils.ZERO);
			map.put(TOT_DOC_CARREGADO_SEM_DOC,  LongUtils.ZERO);
			map.put(TOT_DOC_CARREGADO_INCOMPLETO,  LongUtils.ZERO);
		}
		return map;
	}

	public TypedFlatMap existEventoControleCarga(TypedFlatMap criteria){
		criteria.put(ID_FILIAL, SessionUtils.getFilialSessao().getIdFilial());
		boolean existEventoControleCarga = this.eventoControleCargaService.existEventoControleCargaByIdCCAndIdFilial(criteria);

		TypedFlatMap typedFlatMap = new TypedFlatMap();
		typedFlatMap.put(EXIST_EVENTO_CONTROLE_CARGA, existEventoControleCarga);

		return typedFlatMap;

	}

	public TypedFlatMap findVolumesSobraAutomatica(TypedFlatMap criteria) {

		TypedFlatMap retorno = new TypedFlatMap();

		final Long idControleCarga = criteria.getLong(ID_CONTROLE_CARGA);
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();

		List sobras = this.volumeSobraService.findVolumesSobraByIdControleCarga(idControleCarga, idFilial);

		retorno.put("qtVolumesSobra", sobras.size());
		retorno.put("rncVolumesSobra", sobras);

		return retorno;

	}

	public void abrirRncSobraAutomatica(TypedFlatMap criteria) {

		final Long idControleCarga = criteria.getLong(ID_CONTROLE_CARGA);
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();

		List<Map<String, Object>> mapVolumesSobra =  criteria.getList("rncVolumesSobra");

		if(!mapVolumesSobra.isEmpty()) {

			List<Map<String, Object>> listaVolumeSobraRncAutomatica = this.gerarListaVolumeSobraRncAutomatica(mapVolumesSobra);

			this.ocorrenciaNaoConformidadeService.executeCriacaoRNCautomaticaDescarregamentoComSobra(idControleCarga, idFilial, listaVolumeSobraRncAutomatica);
		}
	}

	private List<Map<String, Object>> gerarListaVolumeSobraRncAutomatica(List<Map<String, Object>> volumesSobra) {

		List<Map<String, Object>> listaDocVolmRNCcriacaoAutomatica = new ArrayList<>();

		for (Map<String, Object> volumeSobra: volumesSobra) {

			Map<String, Object> novoVolumeSobra  = new HashMap<>();

			novoVolumeSobra.put("ID_DOCTO_SERVICO", volumeSobra.get("idDocumentoServico"));
			novoVolumeSobra.put("NR_DOCTO_SERVICO", volumeSobra.get("nrDoctoServico"));
			novoVolumeSobra.put("TP_DOCUMENTO_SERVICO", volumeSobra.get("tpDocumentoServico"));
			novoVolumeSobra.put("SG_FILIAL", volumeSobra.get("sgFilial"));
			novoVolumeSobra.put("NR_SEQUENCIA", volumeSobra.get("nrSequencia"));
			novoVolumeSobra.put("QT_VOLUMES", volumeSobra.get("qtVolumes"));

			listaDocVolmRNCcriacaoAutomatica.add(novoVolumeSobra);
		}

		return listaDocVolmRNCcriacaoAutomatica;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.setMasterService(carregamentoDescargaService);
		this.carregamentoDescargaService = carregamentoDescargaService;
	}

	public void setOcorrenciaNaoConformidadeService(OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}

	public void setVolumeSobraService(VolumeSobraService volumeSobraService) {
		this.volumeSobraService = volumeSobraService;
	}
}
