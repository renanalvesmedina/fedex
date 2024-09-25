package com.mercurio.lms.expedicao.model.service;

import br.com.tntbrasil.integracao.domains.edi.EdiConembComplementoDto;
import br.com.tntbrasil.integracao.domains.edi.EdiConembDTO;
import br.com.tntbrasil.integracao.domains.edi.EdiConembImpostoDto;
import br.com.tntbrasil.integracao.domains.edi.EdiConembNotaDto;
import br.com.tntbrasil.integracao.domains.expedicao.DoctoServicoSaltoDMN;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoEnvioEmailAverbacao;
import br.com.tntbrasil.integracao.domains.expedicao.DocumentoRetornoAverbacao;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.*;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.CarregamentoDescargaService;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.EventoControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.entrega.model.service.AgendamentoDoctoServicoService;
import com.mercurio.lms.entrega.model.service.AgendamentoEntregaService;
import com.mercurio.lms.expedicao.dto.EVersaoXMLCTE;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.expedicao.model.dao.ConhecimentoDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.DataUtil;
import com.mercurio.lms.integracao.model.service.MunicipioVinculoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServico;
import com.mercurio.lms.seguros.model.AverbacaoDoctoServicoMdfe;
import com.mercurio.lms.seguros.model.service.AverbacaoDoctoServicoMdfeService;
import com.mercurio.lms.seguros.model.service.AverbacaoDoctoServicoService;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.LocalizacaoMercadoria;
import com.mercurio.lms.sim.model.dao.LMComplementoDAO;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.service.LocalizacaoMercadoriaService;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.*;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.dto.DadosEmbarquePipelineDTO;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.vol.model.service.VolDadosSessaoService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.joda.time.format.DateTimeFormat;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 *
 * @spring.bean id="lms.expedicao.conhecimentoService"
 *
 */
public class ConhecimentoService extends CrudService<Conhecimento, Long> {
	private static final Logger LOGGER = LogManager.getLogger(ConhecimentoService.class);
    
	public static final Short NO_TERMINAL = Short.valueOf("24");
	public static final Short EM_DESCARGA = Short.valueOf("34");
	public static final Short MERCADORIA_RETORNADA_AGUARDANDO_DESCARGA = Short.valueOf("37");
	public static final Short AGUARDANDO_DESCARGA = Short.valueOf("38");
	public static final Short EM_VIAGEM = Short.valueOf("3");
	private static final String WEB_SERVICE_CTE_AVERBACAO = "averbaCTe";
	private static final String WEB_SERVICE_MDFE_DECLARACAO = "declaraMDFe";
	private static final String TP_ATEM = "ATEM";
    
	private static final String ID_USUARIO_PADRAO_INTEGRACAO = "ID_USUARIO_PADRAO_INTEGRACAO";
	private ConfiguracoesFacade configuracoesFacade;
	private LMComplementoDAO lmComplementoDao;
	private EnderecoPessoaService enderecoPessoaService;
	private ParcelaDoctoServicoService parcelaDoctoServicoService;
	private FilialService filialService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private DadosComplementoService dadosComplementoService;
	private PessoaService pessoaService;
	private ConhecimentoCancelarService conhecimentoCancelarService;
	private ParametroGeralService parametroGeralService;
	private LiberacaoDocServService liberacaoDocServService;
	private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private TipoTributacaoService tipoTributacaoService;
	private MunicipioVinculoService municipioVinculoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private PerfilUsuarioService perfilUsuarioService;
	private EmitirDocumentoService emitirDocumentoService;
	private MonitoramentoDocEletronicoService monitoramentoDocEletronicoService;
	private VolumeNotaFiscalService volumeNotaFiscalService;
	private ContingenciaService contingenciaService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosService;
	private DevedorDocServFatService devedorDocServFatService;
	private LiberacaoNotaNaturaService liberacaoNotaNaturaService;
	private MoedaService moedaService;
	private PaisService paisService;
	private DensidadeService densidadeService;
	private InscricaoEstadualService inscricaoEstadualService;
	private DoctoServicoService doctoServicoService;
	private ConfiguracoesFacadeImpl configuracoesFacadeImpl;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private DevedorDocServService devedorDocServService;
	private GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService;
	private IntegracaoNDDigitalService integracaoNDDigitalService;
	private MunicipioService municipioService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;
	private LocalizacaoMercadoriaService localizacaoMercadoriaService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private CarregamentoDescargaService carregamentoDescargaService;
	private ManifestoService manifestoService;
	private EventoControleCargaService eventoControleCargaService;
	private ControleCargaService controleCargaService;
	private ManifestoEletronicoService manifestoEletronicoService;
	private UsuarioService usuarioService;
	private VolDadosSessaoService volDadosSessaoService;
	private AverbacaoDoctoServicoMdfeService averbacaoDoctoServicoMdfeService;
	private AverbacaoDoctoServicoService averbacaoDoctoServicoService;
    private AgendamentoDoctoServicoService agendamentoDoctoServicoService;
	private AgendamentoEntregaService agendamentoEntregaService;
	
 
	/**
	 * Recupera o Conhecimento apartir nrConhecimento, idFilial e
	 * tpDocumentoServico.
	 *
	 * @param nrConhecimento
	 *            Numero do conhecimento
	 * @param idFilial
	 *            ID da filial do conhecimento
	 * @param tpDocumentoServico
	 *            tipo de documento de servico
	 * @return com.mercurio.lms.municipios.model.Conhecimento
	 */
	public Conhecimento findConhecimentoByNrConhecimentoIdFilial(
			Long nrConhecimento, Long idFilial, String tpDocumentoServico) {
		return getConhecimentoDAO().findConhecimentoByNrConhecimentoIdFilial(
				nrConhecimento, idFilial, tpDocumentoServico);
	}
	
	/**
	 * Recupera lista do IdConhecimento apartir nrConhecimento, idFilial e
	 * tpDocumentoServico.
	 *
	 * @param params com lista de nrConhecimento e idFilial ou tpDocumentoServico
	 * @return List<Long>
	 */
	public List<Map<String, Object>> findConhecimentoByNrConhecimentoIdFilial(Map<String, List<Long>> params){
		return getConhecimentoDAO().findConhecimentoByNrConhecimentoIdFilial(params);
	}
	/**
	 * Busca Conhecimentos a partir do Meio de Transporte, Filial do Usuario
	 * Logado e pelo Tipo Situacao Conhecimento
	 *
	 * @param nrFrota
	 * @param idFilialUsuarioLogado
	 * @param tpSituacaoConhecimento
	 * @return
	 */
	public List findByNrFrotaByIdFilialUsuario(String nrFrota,
			Long idFilialUsuarioLogado, String tpSituacaoConhecimento) {
		List list = getConhecimentoDAO().findByNrFrotaByIdFilialUsuario(
				nrFrota, idFilialUsuarioLogado, tpSituacaoConhecimento);
		return list;
	}

	public List findIdConhecimentoByNrCodigoBarras(Long nrCodigoBarras){
		return getConhecimentoDAO().findIdConhecimentoByNrCodigoBarras(
				nrCodigoBarras);
	}
	public List findConhecimentosByTpSituacao(String sgFilial,Long idControleCarga,String tpSituacaoConhecimento){
		return getConhecimentoDAO().findConhecimentosByTpSituacao(sgFilial,idControleCarga,tpSituacaoConhecimento);
	}
	public List findConhecimentosViagemByTpSituacao(String sgFilial,Long idControleCarga,String tpSituacaoConhecimento){
		return getConhecimentoDAO().findConhecimentosViagemByTpSituacao(sgFilial,idControleCarga,tpSituacaoConhecimento);
	}

	/**
	 * Busca o conhecimento associado ao codigo de barras recebido.
	 * @param nrCodigoBarras numro de codigo de barras para buscar
	 * @return o codigo de barras encontrado
	 * @author Luis Carlos Poletto
	 */
	public Conhecimento findByNrCodigoBarras(Long nrCodigoBarras) {
		List<Conhecimento> conhecimentos = getConhecimentoDAO().findByNrCodigoBarras(nrCodigoBarras);
		// VERIFICA SE RETORNOU ALGUM CONHECIMENTO, CASO CONTRARIO LANÇA EXCEPTION
		if (conhecimentos.size() == 0) {
			throw new BusinessException("LMS-26106");
		}
		// VERIFICA SE EXISTE MAIS DE 1 CONHECIMENTO VINCULADO AO BARCODE RECEBIDO
		if (conhecimentos.size() > 1) {
			throw new BusinessException("LMS-05197");
		}

		return conhecimentos.get(0);
		}
	public Conhecimento findByNrChave(String nrCodigoBarras){
		
		Conhecimento conhecimento = null;
		
		List<MonitoramentoDocEletronico> monitoramentoDocEletronico = monitoramentoDocEletronicoService.findListByNrChave(nrCodigoBarras);
		
		if (monitoramentoDocEletronico.isEmpty()) {
			throw new BusinessException("LMS-26106");
		}

		if (monitoramentoDocEletronico.size() > 1) {
			throw new BusinessException("LMS-05197");
		}else{
			//não usa o findById por causa do fetch
			conhecimento = getConhecimentoDAO().findByNrChaveId(monitoramentoDocEletronico.get(0).getDoctoServico().getIdDoctoServico());
			if (conhecimento == null) {
				throw new BusinessException("LMS-26106");
			}else{
		return conhecimento;
	}
		}
	}
	
	public Conhecimento findByIdDoctoServico(Long idDoctoServico){
		return getConhecimentoDAO().findById(idDoctoServico); 
	}
	
	public Conhecimento findByNrChaveDocEletronico(String nrChave) {
		return getConhecimentoDAO().findByNrChaveDocEletronico(nrChave);
	}
	
	
	public Conhecimento findByNrChaveRpsFedex(String chaveDocumento) {
		return getConhecimentoDAO().findByNrChaveRpsFedex(chaveDocumento);
	}

	public void validateInformarConhecimentoManifesto(final Conhecimento conhecimento, final Long idManifesto, final Long nrPreManifesto){
	/**
		 * LMS-2505
		 */
		if(!(SessionUtils.getFilialSessao().getBlSorter())){
			if (!ConhecimentoUtils.isLiberaEtiquetaEdi(conhecimento)) {
				throw new BusinessException("LMS-05034");
			}
		}else{
			// FILIAL SORTER

			// POSSUI OU NÃO CONTINGENCIA VALIDA PARA A FILIAL EM QUESTÃO (1)
			Boolean hasContingenciaValidaByFilial = contingenciaService.hasContingenciaValidaByFilial(SessionUtils.getFilialSessao().getIdFilial());

			// FOI DIGITADO OU NÃO O CAMPO PRÉ MANIFESTO 
			Boolean hasPreManifesto = nrPreManifesto != null ? true : false;

			// DEFAULT FALSE -  NO CASO DE NÃO SER DIGITADO O PRÉ MANIFESTO
			Boolean hasVolumesNaoCarregados = false;

			if(hasPreManifesto){
				// SENDO QUE FOI DIGITADO PRÉ MANIFESTO
				// SE HÁ OU NÃO VOLUMES CARREGADOS DO DOCUMENTO DE SERVIÇO (3)
				hasVolumesNaoCarregados = volumeNotaFiscalService.hasVolumesCarregados(idManifesto , conhecimento.getIdDoctoServico()); 				
			}

			// SE !(1) E !(2) E !(3) = SE (1) OU (2) OU (3)
			if (!hasContingenciaValidaByFilial && !ConhecimentoUtils.isLiberaEtiquetaEdi(conhecimento) && !hasVolumesNaoCarregados) {
				throw new BusinessException("LMS-05034");
			}
		}
	}

	/**
	 * Recupera uma instância de <code>Conhecimento</code> a partir do ID.
	 *
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public Conhecimento findById(java.lang.Long id) {
		return (Conhecimento) super.findById(id);
	}

	@Override
	public Conhecimento findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (Conhecimento)super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}

	/**
	 * Busca os dados do conhecimento fazendo fetch joins para evitar o problema
	 * de N+1 selects no banco.
	 *
	 * @param idConhecimento
	 *            identificador do conhecimento a ser buscado
	 * @return conhecimento e dados relevantes
	 */
	public Conhecimento findByIdEager(Long idConhecimento) {
		return getConhecimentoDAO().findByIdEager(idConhecimento);
	}

	public Conhecimento findByIdJoinLocalizacaoMercadoria(Long idConhecimento) {
		return getConhecimentoDAO().findByIdJoinLocalizacaoMercadoria(idConhecimento);
	}

	public Conhecimento loadConhecimentosSubstituto(Long idDoctoServico) {
		return getConhecimentoDAO().loadConhecimentosSubstituto(idDoctoServico);
	}

	/**
	 * Retorna o tpFrete do conhecimento informado.
	 *
	 * @author Mickaël Jalbert
	 * @since 28/06/2006
	 *
	 * @param idConhecimento
	 * @return DomainValue
	 * */
	public DomainValue findTpFreteByIdConhecimento(Long idConhecimento){
		return getConhecimentoDAO().findTpFreteByIdConhecimento(idConhecimento);
	}

	/**
	 * Retorna o tpFrete e tpSituacao do conhecimento informado.
	 *
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 *
	 * @param idConhecimento
	 * @return map(tpFrete, tpSituacaoConhecimento, idFilial)
	 * */
	public Map findTpFreteTpSituacaoByIdConhecimento(Long idConhecimento){
		return getConhecimentoDAO()
				.findTpFreteTpSituacaoIdFilialByIdConhecimento(idConhecimento);
	}

	/**
	 * Retorna o tpConhecimento do conhecimento informado.
	 *
	 * @author Salete
	 * @since 06/09/2006
	 *
	 * @param idConhecimento
	 * @return DomainValue
	 * */
	public DomainValue findTpConhecimentoByIdConhecimento(Long idConhecimento){
		return getConhecimentoDAO().findTpConhecimentoByIdConhecimento(
				idConhecimento);
	}

	public Integer updateQtEtiquetasEmitidasByQtEtiquetas(Long idConhecimento,
			Integer qtEtiquetas) {
		return getConhecimentoDAO().updateQtEtiquetasEmitidasByQtEtiquetas(
				idConhecimento, qtEtiquetas);
	}

	/**
	 * Retorna o número do conhecimento pelo ID.<BR>
	 *
	 * @param idConhecimento
	 * @return número do conhecimento
	 */
	public Long findNrConhecimentoById(Long idConhecimento) {
		return getConhecimentoDAO().findNrConhecimentoById(idConhecimento);
	}

	public ResultSetPage findPaginatedDevedorDocServFat(Map map) {
		return getConhecimentoDAO().findPaginatedDevedorDocServFat(map,
				FindDefinition.createFindDefinition(map));
	}

	public Integer getRowCountDevedorDocServFat(Map criteria) {
		return getConhecimentoDAO().getRowCountDevedorDocServFat(criteria);
	}

	public List findConhecimentoByDevedorDocServFat(Map map) {
		return getConhecimentoDAO().findConhecimentoDevedorDocServFat(map);
	}

	public List<Conhecimento> findByNrConhecimentoByFilial(Long nrConhecimento,
			Long idFilial, String tpDocumentoServico) {
		return getConhecimentoDAO().findByNrConhecimentoByFilial(
				nrConhecimento, idFilial, tpDocumentoServico);
	}

	public List findDocumentosServico(Long idConhecimento,
			String tpDocumentoServico) {
		return getConhecimentoDAO().findDocumentosServico(idConhecimento,
				tpDocumentoServico);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removePreCtrcById(java.lang.Long id) {
		getConhecimentoDAO().removeById(id, true);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 *
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(Conhecimento bean) {
		return super.store(bean);
	}

	@Override
	protected Conhecimento beforeStore(Conhecimento conhecimento) {
		//Gera numero do documento
		emitirDocumentoService.generateProximoNumero(conhecimento);
		return super.beforeStore(conhecimento);
	}

	public void flushConhecimento() {
		super.getDao().getAdsmHibernateTemplate().flush();
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 *
	 * @param dao
	 */
	public void setConhecimentoDAO(ConhecimentoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	public ConhecimentoDAO getConhecimentoDAO() {
		return (ConhecimentoDAO) getDao();
	}

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		return getConhecimentoDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public List findLookupDocumentNumberCTR(Long nrConhecimento, Long idFilial,
			String tpDoctoServico) {
		return getConhecimentoDAO().findByNrConhecimentoByFilial(
				nrConhecimento, idFilial, tpDoctoServico);
	}

	/**
	 * Busca os documentos de servico (Conhecimentos - CTR) a partir do id do
	 * Manifesto de viagem nacional.
	 */
	public List findConhecimentosByIdManifestoViagemNacional(Long idManifesto,
			Long idFilialOrigem) {
		return getConhecimentoDAO()
				.findConhecimentosByIdManifestoViagemNacional(idManifesto,
						idFilialOrigem);
	}

	public List findConhecimentosCalculoFreteTabelaCheiaByRownum(Long rownum) {
		return getConhecimentoDAO()
				.findConhecimentosCalculoFreteTabelaCheiaByRownum(rownum);
	}

	/**
	 * Retorna N Conhecimentos limitados pelo rownum a serem calculados Frete
	 *
	 * @param rownum
	 * @return
	 */
	public List<Long> findConhecimentosCalculoFreteTabelaDiferenciadaByRownum(
			Long rownum) {
		return getConhecimentoDAO()
				.findConhecimentosCalculoFreteTabelaDiferenciadaByRownum(rownum);
	}

	public Conhecimento findByDadosComplemento(String dsValorCampo, Long idInformacaoDoctoCliente) {
		return getConhecimentoDAO().findByDadosComplemento(dsValorCampo, idInformacaoDoctoCliente);
	}

	/**
	 * Busca os documentos de servico (Conhecimentos - CTR) a partir do id do
	 * Manifesto de entrega.
	 */
	public List findConhecimentosByIdManifestoEntrega(Long idManifesto) {
		return getConhecimentoDAO().findConhecimentosByIdManifestoEntrega(
				idManifesto);
	}

	/**
	 * Busca os documentos de servico (Notas Fiscais Transporte - NFT) a partir
	 * do id do Manifesto.
	 */
	public List findNotasFiscaisTransporteByIdManifestoEntrega(Long idManifesto) {
		return getConhecimentoDAO()
				.findNotasFiscaisTransporteByIdManifestoEntrega(idManifesto);
	}

	public List findLookupByNrConhecimentoByFilialOrigem(Long nrConhecimento,
			Long idFilial, Integer dvConhecimento, String tpDocumentoServico) {
		return getConhecimentoDAO().findLookupByNrConhecimentoByFilialOrigem(
				nrConhecimento, idFilial, dvConhecimento, tpDocumentoServico);
	}

	public Conhecimento findByIdPreAwb(Long idDoctoServico) {
		return getConhecimentoDAO().findByIdPreAwb(idDoctoServico);
	}

	/**
	 * Calculo do peso aforado, baseado numa lista de dimensões e no modal: -
	 * Somar as Multiplicações dos valores informados (Altura, Largura,
	 * Comprimento e Quantidade) nas dimensões; - Se o modal do serviço que foi
	 * selecionado no campo Serviço for Rodoviário (R) dividir o valor obtido
	 * PARAMETRO_GERAL.DS_CONTEUDO para o fator peso x metragem cúbica
	 * (PARAMETRO_GERAL.NM_PARAMETRO_GERAL = "PESO_METRAGEM_
	 * CUBICA_RODOVIARIO"). - Se o modal do serviço que foi selecionado no campo
	 * Serviço for Aéreo (A) dividir o valor obtido PARAMETRO_GERAL.DS_CONTEUDO
	 * para o fator peso x metragem cúbica (PARAMETRO_GERAL.NM_PARAMETRO_GERAL =
	 * "PESO_METRAGEM_ CUBICA_AEREO"). - O resultado das operações acima será o
	 * peso aforado.
	 */
	public BigDecimal calculaPsAforado(String tpModal, List dimensoes) {
		return calculaPsAforado(tpModal, dimensoes, null);
	}
	public BigDecimal calculaPsAforado(String tpModal, List dimensoes, CalculoFrete calculoFrete) {
		BigDecimal psAforado = null;
		if (dimensoes != null && !dimensoes.isEmpty()) {
			double somaTotal = 0.0;
			for (Iterator iter = dimensoes.iterator(); iter.hasNext();) {
				Dimensao d = (Dimensao) iter.next();
				somaTotal += d.getNrAltura().doubleValue()
						* d.getNrLargura().doubleValue()
						* d.getNrComprimento().doubleValue()
						* d.getNrQuantidade().doubleValue();
			}
			BigDecimal vlTotalVolume = new BigDecimal(somaTotal);

			/*Verifica se o cliente é especial*/
			if(calculoFrete != null
				&& calculoFrete.getClienteBase() != null
				&& calculoFrete.getClienteBase().getTpCliente() != null
				&& ClienteUtils.isParametroClienteEspecial(calculoFrete.getClienteBase().getTpCliente().getValue())){

				/*Obtem a tabela divisao cliente*/
				TabelaDivisaoCliente tdc = tabelaDivisaoClienteService.findTabelaDivisaoCliente(calculoFrete.getIdDivisaoCliente(), calculoFrete.getIdServico());

				/*Verifica se o atributo NrFatorCubagem possue valor e cálcula
				 * o pesoAforado. Valor obtido no volumes X nrFatorCubagem / 1000000 */
				if(tdc != null && BigDecimalUtils.hasValue(tdc.getNrFatorCubagem())){
					psAforado = BigDecimalUtils.divide(vlTotalVolume.multiply(tdc.getNrFatorCubagem()), new BigDecimal("1000000"));
				}else{
					BigDecimal metrageCubica = null;
					if (ConstantesExpedicao.MODAL_AEREO.equalsIgnoreCase(tpModal)) {
						metrageCubica = BigDecimalUtils.getBigDecimal(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO));
					} else {
						metrageCubica = BigDecimalUtils.getBigDecimal(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_RODOVIARIO));
					}
					psAforado = BigDecimalUtils.divide(vlTotalVolume, metrageCubica);
				}
			}else{
				BigDecimal metrageCubica = null;
				if (ConstantesExpedicao.MODAL_AEREO.equalsIgnoreCase(tpModal)) {
					metrageCubica = BigDecimalUtils.getBigDecimal(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_AEREO));
				} else {
					metrageCubica = BigDecimalUtils.getBigDecimal(configuracoesFacade.getValorParametro(ConstantesExpedicao.NM_PARAMETRO_PESO_METRAGEM_CUBICA_RODOVIARIO));
				}

				psAforado = BigDecimalUtils.divide(vlTotalVolume, metrageCubica);
			}
		}
		return psAforado;
	}

	public List<Map<String, Object>> findByNrConhecimentoIdFilialOrigem(Long nrConhecimento, Long idFilial, String tpSituacaoConhecimento, String tpDocumentoServico) {
		List<Map<String, Object>> result = getConhecimentoDAO().findByNrConhecimentoIdFilialOrigem(nrConhecimento, idFilial, tpDocumentoServico, tpSituacaoConhecimento);
		for(Map<String, Object> item : result) {
			Timestamp timestamp = (Timestamp)item.remove("dhEmissao");
			DateTime dateTime = JodaTimeUtils.toDateTime(getConhecimentoDAO().getAdsmHibernateTemplate(), timestamp);
			item.put("dhEmissao", dateTime);

			item.put("blTemReemissao", getConhecimentoDAO().existeReentrega((Long) item.get("idDoctoServico")) ? "SIM" : "NAO");
		}
		return result;
	}

	public void validateCrtCancelado(List conhecimentos) {
		if(conhecimentos != null && conhecimentos.size() > 0) {
			Map conhecimento = (Map) conhecimentos.get(0);
			if ("C".equals(MapUtils.getString(conhecimento,
					"tpSituacaoConhecimento"))) {
				throw new BusinessException("LMS-04146");
			}
		}
	}

	/**
	 * Verifica se existe algum registro na tabela de CONHECIMENTO relacionado
	 * com MEIO_DE_TRANSPORTE
	 *
	 * @param nrFrota
	 * @param idFilialUsuarioLogado
	 * @param tpSituacaoConhecimento
	 */
	public void validateMeioTransporteConhecimento(String nrFrota,
			Long idFilialUsuarioLogado, String tpSituacaoConhecimento) {
		List list = getConhecimentoDAO().findByNrFrotaByIdFilialUsuario(
				nrFrota, idFilialUsuarioLogado, tpSituacaoConhecimento);
		if (list == null || list.size() <= 0) {
			throw new BusinessException("LMS-04220");
		}
	}

	public List findEnderecosClientesConhecimentos(List enderecos) {
		if (enderecos.isEmpty()) {
			return enderecos;
		}
		/** Maps de referencia */
		Map conhecimento = (Map) enderecos.get(0);
		Map destinatario = (Map) conhecimento.get("destinatario");
		Map remetente = (Map) conhecimento.get("remetente");

		/** Seta Enderecos nas Referencias da Lista */
		EnderecoPessoa enderecoPessoa = enderecoPessoaService
				.findEnderecoPessoaPadrao(MapUtilsPlus.getLong(remetente,
						"idPessoa"));
		remetente
				.put("endereco", PessoaUtils.getEnderecoPessoa(enderecoPessoa));
		remetente.put("nrIdentificacaoFormatado", FormatUtils
				.formatIdentificacao(remetente));

		enderecoPessoa = enderecoPessoaService
				.findEnderecoPessoaPadrao(MapUtilsPlus.getLong(destinatario,
						"idPessoa"));
		destinatario.put("endereco", PessoaUtils
				.getEnderecoPessoa(enderecoPessoa));
		destinatario.put("nrIdentificacaoFormatado", FormatUtils
				.formatIdentificacao(destinatario));
		return enderecos;
	}

	public void evict(Object o) {
		getConhecimentoDAO().evict(o);
	}

	@Override
	public void flush() {
		getConhecimentoDAO().flush();
	}

	/**
	 * Método que será executado para verificar se o usuário tem permissão para
	 * liberar o embarque.
	 * @param usuario
	 *
	 * @return
	 */
	public Boolean validateLiberacaoEmbarque(Usuario usuario) {


		Object valor = configuracoesFacade.getValorParametro("PERFIL_LIBERACAO_EMBARQUE");

		List<PerfilUsuario> perfis = perfilUsuarioService.findByIdUsuarioPerfilUsuario(usuario.getIdUsuario());
		for (PerfilUsuario perfilUsuario : perfis) {
			if(perfilUsuario.getPerfil().getDsPerfil().equals(valor)){
		return Boolean.TRUE;
	}
		}
		return Boolean.FALSE;
	}

	public ResultSetPage findConhecimentoPaginated(TypedFlatMap criteria) {
		YearMonthDay dhInicial = criteria.getYearMonthDay("dtEmissaoInicial");
		YearMonthDay dhFinal = criteria.getYearMonthDay("dtEmissaoFinal");

		if (dhInicial != null && dhFinal != null) {
			/** Valida Datas: Periodo nao eh mais REQUIRED */
			if(dhFinal.isBefore(dhInicial)) {
				throw new BusinessException("LMS-00008");
			}
			if(dhFinal.isAfter(dhInicial.plusDays(15))) {
				throw new BusinessException("LMS-04126");
			}
		}

		return getConhecimentoDAO().findConhecimentoPaginated(criteria);
	}

	public Integer getRowCountConhecimento(TypedFlatMap criteria) {
		return getConhecimentoDAO().getRowCountConhecimento(criteria);
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedExcluirPreCtrc(TypedFlatMap criteria) {
		ResultSetPage rs = getConhecimentoDAO().findPaginatedExcluirPreCtrc(criteria);
		if((rs == null || rs.getList() == null || rs.getList().size() == 0) && StringUtils.isBlank((String) criteria.get("nrPlaca")) && StringUtils.isBlank((String) criteria.get("nrFrota"))){
			criteria.put("_currentPage", "1");
			criteria.put("_pageSize", "17");
			return getConhecimentoDAO().findPaginatedExcluirPreCtrcOrfao(criteria);
		} else {
			return rs;
	}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findByIdExcluirPreCtrc(Long id) {
		List result = getConhecimentoDAO().findByIdExcluirPreCtrc(id);
		Map ctrc = null;
		if(!result.isEmpty()) {
			ctrc = (Map)result.get(0);
			ctrc.put("nrIdentificacaoRemetente", FormatUtils
					.formatIdentificacao((String) ((Map) ctrc
							.get("tpIdentificacaoRemetente")).get("value"),
							(String) ctrc.get("nrIdentificacaoRemetente")));
			ctrc.put("nrIdentificacaoDestinatario", FormatUtils
					.formatIdentificacao((String) ((Map) ctrc
							.get("tpIdentificacaoDestinatario")).get("value"),
							(String) ctrc.get("nrIdentificacaoDestinatario")));
		}
		return ctrc;
	}

	public Integer getRowCountByDoctoServicoOriginal(
			Long idDoctoServicoOriginal, String tpDocumentoServico,
			String tpConhecimento) {
		return getConhecimentoDAO().getRowCountByDoctoServicoOriginal(
				idDoctoServicoOriginal, tpDocumentoServico, tpConhecimento);
	}

	public Integer getRowCountExcluirPreCtrc(TypedFlatMap criteria) {
		return getConhecimentoDAO().getRowCountExcluirPreCtrc(criteria);
	}

	public List findByTerminalNotInAwb(TypedFlatMap criteria) {
		return getConhecimentoDAO().findByTerminalNotInAwb(criteria);
	}
	
	public List findByCriteriaNotInAwb(TypedFlatMap criteria) {
		return getConhecimentoDAO().findByCriteriaNotInAwb(criteria);
	}

	public List findComplementosOutrosIndicadorCooperacao(Long idDoctoServico){
		return lmComplementoDao
				.findComplementosOutrosIndicadorCooperacao(idDoctoServico);
	}

	public void validateBlPesoAferidoByMeioTransporteColeta(String nrFrota,
			Long idFilialOrigem, Boolean blPesoAferido) {
		List list = getConhecimentoDAO()
				.findBlPesoAferidoByMeioTransporteColeta(nrFrota,
						idFilialOrigem, blPesoAferido);
		if (list != null && list.size() > 0) {
			throw new BusinessException("LMS-04225");
		}
	}

	public void updateConhecimentosDuplicadosSom(final List<Long> idsDuplicados) {
		if(idsDuplicados != null && idsDuplicados.size() > 0) {
			getConhecimentoDAO()
					.updateConhecimentosDuplicadosSom(idsDuplicados);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map findDataToSOM(Long idConhecimento, String status) {
		Map<String, Object> data = getConhecimentoDAO().findDataToSOM(
				idConhecimento);

		Long idDoctoServico = (Long) data.get("idDoctoServico");
		DomainValue tpModal = (DomainValue) data.get("tpModal");
		data.put("statusConh", status);
		if(tpModal != null && "A".equals(tpModal.getValue())) {
			BigDecimal vlParcelas = parcelaDoctoServicoService.findSumVlParcelasByIdDoctoServico(idDoctoServico);
			data.put("vlParcelaPrecoCat", vlParcelas);
		}
		String sgRedFilialOrigem = filialService
				.findSgFilialLegadoByIdFilial((Long) data.get("idFilialOrigem"));
		String sgRedFilialDestino = filialService
				.findSgFilialLegadoByFilialTntOuCooperada((Long) data
						.get("idFilialDestino"));
		String sgRedFilialDevedor = filialService
				.findSgFilialLegadoByFilialTntOuCooperada((Long) data
						.get("idFilialDevedor"));
		Long idFilialDestinoResp = (Long) data.get("idFilialDestinoResp");
		if(idFilialDestinoResp == null) {
			idFilialDestinoResp = (Long) data.get("idFilialDestino");
		}
		String sgRedFilialDestinoResp = filialService
				.findSgFilialLegadoByFilialTntOuCooperada(idFilialDestinoResp);
		data.put("sgRedFilialOrigem", sgRedFilialOrigem);
		data.put("sgRedFilialDestino", sgRedFilialDestino);
		data.put("sgRedFilialDevedor", sgRedFilialDevedor);
		data.put("sgRedFilialDestinoResp", sgRedFilialDestinoResp);

		data.put("vlsParcelaPreco", parcelaDoctoServicoService
				.findVlParcelasByIdDoctoServicoEParcelaPreco(idDoctoServico));

		data = findDadosComplementosByConhecimento(data, idDoctoServico);

		List<Map<String, Object>> notasFiscais = notaFiscalConhecimentoService
				.findNFByIdDoctoServico(idDoctoServico);

		if(notasFiscais != null) {
			for (Map<String, Object> map : notasFiscais) {
				Long idNotaFiscalConhecimento = (Long) map
						.get("idNotaFiscalConhecimento");
				getDadosComplementosParaSOM(map, idNotaFiscalConhecimento,
						idDoctoServico, "Grupo de Transporte Grendene",
						"grupoTranspGrendene");
				getDadosComplementosParaSOM(map, idNotaFiscalConhecimento,
						idDoctoServico, "Filial Grendene", "filialGrendene");
				getDadosComplementosParaSOM(map, idNotaFiscalConhecimento,
						idDoctoServico, "Número CTRC GM", "numeroCtrcGM");
				getDadosComplementosParaSOM(map, idNotaFiscalConhecimento,
						idDoctoServico, "Data/Hora Emissão CTRC GM",
						"dhEmissaoCtrcGM");
				getDadosComplementosParaSOM(map, idNotaFiscalConhecimento,
						idDoctoServico, "Identificador NF GM",
						"identificadorNfGM");
				getDadosComplementosParaSOM(map, idNotaFiscalConhecimento,
						idDoctoServico, "DPE GM", "dpeGM");

				String dateStr = (String) map.get("dhEmissaoCtrcGM");
				if (dateStr != null && dateStr.length() > 0
						&& dateStr.indexOf('-') > 0) {
					DateTime dhEmissaoCtrcGM = DateTimeFormat.forPattern(
							"yyyy-MM-dd'T'HH:mm:ss.SSSZ").withZone(
							SessionUtils.getFilialSessao().getDateTimeZone())
							.parseDateTime(dateStr);
					map.put("dhEmissaoCtrcGM", dhEmissaoCtrcGM);
				}
				dateStr = (String) map.get("dpeGM");
				if (dateStr != null && dateStr.length() > 0
						&& dateStr.indexOf('-') > 0) {
					DateTime dpeGM = DateTimeFormat.forPattern("yyyy-MM-dd")
							.withZone(
									SessionUtils.getFilialSessao()
											.getDateTimeZone()).parseDateTime(
									dateStr);
					map.put("dpeGM", dpeGM);
				}
			}
		}
		data.put("notasFiscaisConh", notasFiscais);

		if (notasFiscais != null && notasFiscais.size() > 0
				&& notasFiscais.get(0) != null) {
			data.put("nrNotaFiscal", notasFiscais.get(0).get("nrNotaFiscal"));
			data.put("qtdNotasFiscais", notasFiscais.size());
		} else {
			data.put("qtdNotasFiscais", 0);
		}

		data.put("cepMunicipioEntrega", municipioVinculoService
				.findCepMunicipioSOMByIdMunicipio((Long) data
						.get("idMunicipioEntrega")));

		Map pessoaRemetente = pessoaService.findPessoaSOMById((Long) data
				.get("idPessoaRemetente"));
		Map pessoaDestinatario = pessoaService.findPessoaSOMById((Long) data
				.get("idPessoaDestinatario"));

		if (pessoaRemetente != null
				&& pessoaRemetente.get("idPessoa") != null
				&& pessoaDestinatario != null
				&& pessoaDestinatario.get("idPessoa") != null
				&& ((Long) pessoaRemetente.get("idPessoa")).longValue() == ((Long) pessoaDestinatario
						.get("idPessoa")).longValue()) {
			pessoaDestinatario = new HashMap();
			pessoaDestinatario.put("nrIdentificacao", pessoaService
					.gerarNrIdentificacao99());
			pessoaDestinatario.put("nmPessoa", pessoaRemetente.get("nmPessoa"));
			pessoaDestinatario.put("tpPessoa", "J");
			pessoaDestinatario.put("idInscricaoEstadual", pessoaRemetente
					.get("idInscricaoEstadual"));
			pessoaDestinatario.put("nrInscricaoEstadual", pessoaRemetente
					.get("nrInscricaoEstadual"));
			pessoaDestinatario.put("nrCep", data.get("nrCepEntrega"));
			pessoaDestinatario.put("dsEndereco", data.get("dsEnderecoEntrega"));
			pessoaDestinatario.put("nrEndereco", null);
			pessoaDestinatario.put("dsComplemento", null);
			pessoaDestinatario.put("dsBairro", data.get("dsBairroEntrega"));
			pessoaDestinatario.put("idMunicipio", pessoaRemetente
					.get("idMunicipio"));
			pessoaDestinatario.put("dsTipoLogradouro", null);
			pessoaDestinatario.put("idFlAtendeComercial", null);
			pessoaDestinatario.put("tpSituacaoTributaria", pessoaRemetente
					.get("tpSituacaoTributaria"));
			pessoaDestinatario.put("idFlCobranca", null);
			pessoaDestinatario.put("sgRedFlAtendeComercial", data
					.get("sgRedFilialDestino"));
			pessoaDestinatario.put("sgRegFlCobranca", data
					.get("sgRedFilialDestino"));
			pessoaDestinatario.put("dtCadastro", pessoaRemetente
					.get("dtCadastro"));
			pessoaDestinatario.put("cepMunicipio", data
					.get("cepMunicipioEntrega"));
		}

		data.put("pessoaDevedor", pessoaService.findPessoaSOMById((Long) data
				.get("idPessoaDevedor")));
		data.put("pessoaRemetente", pessoaRemetente);
		data.put("pessoaDestinatario", pessoaDestinatario);
		data.put("pessoaRedespacho", pessoaService
				.findPessoaSOMById((Long) data.get("idPessoaRedespacho")));
		data.put("pessoaConsignatario", pessoaService
				.findPessoaSOMById((Long) data.get("idPessoaConsignatario")));

		ParametroGeral parametroGeral = parametroGeralService
				.findByNomeParametro("ID_TIPO_TRIBUTACAO_ST", false);
		if(parametroGeral != null) {
			data.put("idTipoTributacaoSt", Long.valueOf(parametroGeral
					.getDsConteudo()));
		}
		List<LiberacaoDocServ> list = liberacaoDocServService
				.getLiberacaoDocServByIdDocServ(idDoctoServico);
		if(list != null && list.size() > 0) {
			LiberacaoDocServ liberacaoDocServ = list.get(0);
			data.put("obLiberacao", liberacaoDocServ.getObLiberacao());
			data.put("loginUsuarioLiberacao", liberacaoDocServ.getUsuario()
					.getLogin());
		}

		List<ObservacaoDoctoServico> obsDocServ = observacaoDoctoServicoService
				.findByIdDoctoServico(idDoctoServico);
		List<String> obs = new LinkedList<String>();
		for(int i = 0; obsDocServ != null && i < obsDocServ.size() && i < 2; i++) {
			obs.add(obsDocServ.get(i).getDsObservacaoDoctoServico());
		}
		data.put("obsDocServList", obs);

		data.put("cdEmbLegalMastersaf", observacaoDoctoServicoService
				.findCdEmbMastersafByDocServico(idDoctoServico));

		if((String) data.get("dsTipoTributacaoIcms") == null) {
			data.put("tipoTributacaoIcmsSom", BigDecimalUtils.ZERO);
		} else {
			data.put("tipoTributacaoIcmsSom", tipoTributacaoService
					.findDeParaTipoTributacao((String) data
							.get("dsTipoTributacaoIcms")));
		}
		DateTime dtEmissaoDocServ = (DateTime) data.get("dtEmissaoDocServ");

			data.put("dtEmissaoDocServAlterada", dtEmissaoDocServ);

		Boolean blSpecialService = (Boolean) data.get("specialService");
		data.put("specialService",
				blSpecialService != null && blSpecialService ? "4" : "0");

		return data;
	}

	private Map findDadosComplementosByConhecimento(Map<String, Object> data,
			Long idDoctoServico) {
		Map<String, String> mapDadosCompl = new HashMap<String, String>();
							//nome do campo, nome da propriedade
		mapDadosCompl.put("Tipo NF Grendene", "tipoNfGrendene");
		mapDadosCompl.put("Tipo de Produto Fleury", "tipoProdFleury");
		mapDadosCompl.put("Número de Protocolo Fleury", "nrProtocoloFleury");
		mapDadosCompl.put("Número de AWB Fleury", "nrAwbFleury");
		mapDadosCompl.put("Número do Pedido 3M", "nrPedido3M");
		mapDadosCompl.put("Número do Pedido Agener", "nrPedidoAgener");
		mapDadosCompl.put("Número do Conhecimento AGV", "nrConhAGV");
		mapDadosCompl.put("Número do Pedido Akzo", "nrPedidoAkzo");
		mapDadosCompl.put("Número do Pedido Nextel", "nrPedidoNextel");
		mapDadosCompl.put("Quantidade de Aparelhos Nextel",
				"qtdAparelhosNextel");
		mapDadosCompl.put("Tipo de Venda Nextel", "tpVendaNextel");
		mapDadosCompl.put("Supervisor Retira Nextel", "supervRetiraNextel");
		mapDadosCompl.put("Número do Pedido Bayer", "nrPedidoBayer");
		mapDadosCompl.put("Número do Pedido BCP", "nrPedidoBCP");
		mapDadosCompl.put("Número do Pedido", "nrPedido");
		mapDadosCompl.put("Número do Pedido Danisco", "nrPedidoDanisco");
		mapDadosCompl.put("Local de Expedição MWM", "localExpedicaoMwm");
		mapDadosCompl.put("Número da Minuta/Pedido Natura", "nrMinutaNatura");
		mapDadosCompl.put("Natureza Siemens", "naturezaSiemens");
		mapDadosCompl.put("Número de Ordem Siemens", "nrOrdemSiemens");
		mapDadosCompl.put("Conhecimento Exata", "conhecimentoExata");
		mapDadosCompl.put("Seguro Volvo", "seguroVolvo");
		mapDadosCompl.put("Origem NF West Coast", "origemNfWestCoast");
		mapDadosCompl.put("Representante", "representanteGrendene");
		mapDadosCompl.put("Vendedor", "vendedorGrendene");
		mapDadosCompl.put("Nº AWB TNT", "nroAwbTnt");
		mapDadosCompl.put("Cod Conf Natura", "codConfNatura");

		mapDadosCompl.put("Cod Agente Polimport", "codAgentePolimport");
		mapDadosCompl.put("Cod Empresa Polimport", "codEmpresaPolimport");
		mapDadosCompl.put("Pedido Polimport", "pedidoPolimport");

		List<String> dsCampos = new LinkedList<String>();
		for (Iterator iterator = mapDadosCompl.keySet().iterator(); iterator
				.hasNext();) {
			String dsCampo = (String) iterator.next();
			dsCampos.add(dsCampo);
		}
		List<Map<String, Object>> listDadosCompl = dadosComplementoService
				.findByIdConhecimentoDocCliente(idDoctoServico, dsCampos);
		if(listDadosCompl != null) {
			for (Map<String, Object> dadoCompl : listDadosCompl) {
				data.put(mapDadosCompl.get(dadoCompl.get("key")), dadoCompl
						.get("value"));
			}
		}
		return data;
	}

	private Map getDadosComplementosParaSOM(Map map,
			Long idNotaFiscalConhecimento, Long idDoctoServico, String dsCampo,
			String property) {
		DadosComplemento dc = null;
		dc = dadosComplementoService.findByIdNFDocCliente(
				idNotaFiscalConhecimento, dsCampo);
		if(dc == null) {
			dc = dadosComplementoService.findByIdConhecimentoDocCliente(
					idDoctoServico, dsCampo);
		}
		map.put(property, dc != null ? dc.getDsValorCampo() : null);
		return map;
	}

	public List findConhecimentosPendentesLMS(Long idFilial) {
		if(idFilial == null)
			throw new BusinessException("O idFilial nao pode ser nulo na busca por conhecimentos pendentes do LMS");
		List result = getConhecimentoDAO().findConhecimentosPendentesLMS(idFilial);
		for (int i = 0; i < result.size(); i++) {
			String filial = filialService.findSgFilialLegadoByIdFilial((Long)((Map)result.get(i)).get("idFilialDestino"));
			((Map)result.get(i)).put("sgRedFilialDestino", filial);
	}
		return result;
	}

	public void updateTpSituacaoAtualizacaoSOM(final List<Long> docsServ, final String tpSituacaoAtualizacao) {
		Conhecimento conhecimento = null;
		for (Long id : docsServ) {
			conhecimento = findByIdInitLazyProperties(id, false);
			if(conhecimento != null) {
				conhecimento.setTpSituacaoAtualizacao(tpSituacaoAtualizacao);
				store(conhecimento);
			}
		}
	}

	/**
	 * Busca o conhecimento que possui a nota fiscal recebida como parametro.
	 *
	 * @param idNotaFiscal
	 * @return
	 */
	public Conhecimento findConhecimentoByNotaFiscal(Long idNotaFiscal) {
		return getConhecimentoDAO().findConhecimentoByNotaFiscal(idNotaFiscal);
	}

	/**
	 * Remove objetos relacionados com conhecimento Objectos removidos:
	 * MONITORAMENTO_DESCARGA; DEVEDOR_DOC_SERV_FAT; OBSERVACAO_DOCTO_SERVICO;
	 * DEVEDOR_DOC_SERV; NOTA_FISCAL_CONHECIMENTO; VOLUME_NOTA_FISCAL;
	 * 	DADOS_COMPLEMENTO
	 *
	 * @param idConhecimento
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void removeAllObjectsRelatedWithConhecimento(Long idConhecimento) {
		getConhecimentoDAO().removeAllObjectsRelatedWithConhecimento(
				idConhecimento);
	}

	/**
	 * Busca os dados do Conhecimento para enviar para enviar para a NDDigital, gerando dessa forma a nota fiscal de serviço
	 * eletronica - ( NTE - Nota de transporte eletronica)
	 * @param idConhecimento
	 * @return
	 */
	public Conhecimento findDadosNteToNotaFiscalEletronica(Long idConhecimento){
		return getConhecimentoDAO().findDadosNteToNotaFiscalEletronica(idConhecimento);
	}

	/**
	 * Retorna todos os conhecimentos que foram emitidos através de uma tabela Y
	 *
	 * @return Lista com id dos documentos
	 */
	public List<Long> findConhecimentosTabelaY(Long rownum){
		return getConhecimentoDAO().findConhecimentosTabelaY(rownum);
	}

	public Conhecimento findConhecimentoByIdVolumeNotaFiscal(
			Long idVolumeNotaFiscal) {
		return getConhecimentoDAO().findConhecimentoByIdVolumeNotaFiscal(
				idVolumeNotaFiscal);
    }

	public List<Long> findDadosConhecimentosToSOM(Long idFilial,List<Long> nrConhecimentosSOM, DateTime date) {
		return getConhecimentoDAO().findDadosConhecimentosToSOM(idFilial, nrConhecimentosSOM, date);
	}

	public List<Long> findConhecimentosVNFPendentesToSOM(Long idFilial, DateTime date){
		return getConhecimentoDAO().findConhecimentosVNFPendentesToSOM(idFilial, date);
	}

	public Long getRowCountCarregadosSemPreManifestoDocumento(long idControleCarga) {
		return getConhecimentoDAO().getRowCountCarregadosSemPreManifestoDocumento(idControleCarga);
	}

	public Long getRowCountCarregadosIncompletos(long idControleCarga) {
		return getConhecimentoDAO().getRowCountCarregadosIncompletos(idControleCarga);
	}

	public Conhecimento findConhecimentoReentregaByDoctoServicoOriginal(Long idDoctoServicoOriginal) {
		return getConhecimentoDAO().findConhecimentoReentregaByDoctoServicoOriginal(idDoctoServicoOriginal);
	}
	public Conhecimento findConhecimentoAtualById(Long idConhecimento) {
		return getConhecimentoDAO().findConhecimentoAtualById(idConhecimento);
	}
	
	public Conhecimento findConhecimentoAtualByIdWithCriteria(Long idConhecimento) {
		return getConhecimentoDAO().findConhecimentoAtualByIdWithCriteria(idConhecimento);
	}
	
	public Conhecimento findConhecimentoAtualByIdSorter(Long idConhecimento) {
		return getConhecimentoDAO().findConhecimentoAtualByIdSorter(idConhecimento);
	}
	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService) {
		this.perfilUsuarioService = perfilUsuarioService;
	}
	public Long getRowCountByCodigoBarras(Long nrCodigoBarrasInicial, Long nrCodigoBarrasFinal){
		if(nrCodigoBarrasInicial != null && nrCodigoBarrasFinal != null){
			return getConhecimentoDAO().getRowCountByCodigoBarras(nrCodigoBarrasInicial, nrCodigoBarrasFinal);
		}
		return 0l;
	}

	/**
	 * Busca todos conhecimentos EDI do Monitoramento Descarga
	 * @author André Valadas
	 *
	 * @param idMonitoramentoDescarga
	 * @param isLabeling
	 * @return
	 */
	public List<Conhecimento> findDoctoServicoEDI(final Long idMonitoramentoDescarga, final Boolean isLabeling) {
		List<Conhecimento> result = getConhecimentoDAO().findDoctoServicoEDI(idMonitoramentoDescarga, isLabeling);
		if (result.size() > 0) {
			for (Conhecimento c : result) {
				String sgFilial = filialService.findSgFilialLegadoByIdFilial(c.getFilialByIdFilialDestino().getIdFilial());
				c.getFilialByIdFilialDestino().setSgFilial(sgFilial);
	}
		}

		return result;
	}

	/**
	 * Busca os conhecimento do monitoramento descarga
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public List<Conhecimento> findDoctoServico(Long idMonitoramentoDescarga) {
		List<Conhecimento> result = getConhecimentoDAO().findDoctoServico(idMonitoramentoDescarga);
		if (result.size() > 0) {
			for (Conhecimento c : result) {
				String sgFilial = filialService.findSgFilialLegadoByIdFilial(c.getFilialByIdFilialDestino().getIdFilial());
				c.getFilialByIdFilialDestino().setSgFilial(sgFilial);
			}
		}

		return result;
	}

	/**
	 * Recupera o Conhecimento apartir nrConhecimento, idFilial
	 * 
	 * @param nrConhecimento
	 * 
	 * @param idFilial
	 * 
	 * @return Conhecimento
	 */
	public Conhecimento findConhecimentoByNrConhecimentoIdFilial( Long nrConhecimento, Long idFilial) {
		return getConhecimentoDAO().findConhecimentoByNrConhecimentoIdFilial( nrConhecimento, idFilial);
	}
	
	public List findByListVolumes(List<VolumeNotaFiscal> volumes){
		return getConhecimentoDAO().findByListVolumes(volumes);
	}

	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}
	public void setLmComplementoDao(LMComplementoDAO lmComplementoDao) {
		this.lmComplementoDao = lmComplementoDao;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setParcelaDoctoServicoService(ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setLiberacaoDocServService(LiberacaoDocServService liberacaoDocServService) {
		this.liberacaoDocServService = liberacaoDocServService;
	}
	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}
	public void setTipoTributacaoService(TipoTributacaoService tipoTributacaoService) {
		this.tipoTributacaoService = tipoTributacaoService;
	}
	public void setMunicipioVinculoService(MunicipioVinculoService municipioVinculoService) {
		this.municipioVinculoService = municipioVinculoService;
	}
	public void setMonitoramentoDocEletronicoService(MonitoramentoDocEletronicoService monitoramentoDocEletronicoService) {
		this.monitoramentoDocEletronicoService = monitoramentoDocEletronicoService;
	}
	public void setVolumeNotaFiscalService(VolumeNotaFiscalService volumeNotaFiscalService) {
		this.volumeNotaFiscalService = volumeNotaFiscalService;
	}
	public void setContingenciaService(ContingenciaService contingenciaService) {
		this.contingenciaService = contingenciaService;
	}
	
	public void setIncluirEventosService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosService) {
		this.incluirEventosService = incluirEventosService;
	}
	
	public void setLiberacaoNotaNaturaService(LiberacaoNotaNaturaService liberacaoNotaNaturaService) {
		this.liberacaoNotaNaturaService = liberacaoNotaNaturaService;
	}

	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	
	public void setDensadideService(DensidadeService densidadeService) {
		this.densidadeService = densidadeService;
	}
	
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public List<Conhecimento> findConhecimentosEmProcessamentoEDIByIdMonitoramentoDescarga(Long idMonitoramentoDescarga) {
		 return getConhecimentoDAO().findConhecimentosEmProcessamentoEDIByIdMonitoramentoDescarga(idMonitoramentoDescarga);
	}
	
	/**
	 * Retorna uma lista com conhecimentos de Serviço da nota fiscal importada no EDI 
	 * @param nrIdentificacaoReme
	 * @param nrNotaFiscal
	 * @param isMonitoramentoNotaZerada
	 * @return
	 */
	public List<Conhecimento> findConhecimentosEmProcessamentoEDI(String nrIdentificacaoReme, String nrNotaFiscal, boolean isMonitoramentoNotaZerada){
	    if (isMonitoramentoNotaZerada){
		return getConhecimentoDAO().findConhecimentosEmProcessamentoEDI(nrIdentificacaoReme,nrNotaFiscal);
	    }else{
	        return getConhecimentoDAO().findConhecimentosEmProcessamentoEDINotaCheia(nrIdentificacaoReme,nrNotaFiscal);
	}
	}

	/**
	 * LMS-2505
	 * @param nrCodigoBarrasCTR
	 * @return Conhecimento
	 */
	public Conhecimento findConhecimentoByCodigoBarras(Long nrCodigoBarrasCTR) {
		List<Conhecimento> list = getConhecimentoDAO().findListConhecimentoByCodigoBarras(nrCodigoBarrasCTR);
		if(list.isEmpty()){
			throw new BusinessException("LMS-10040"); 
		}
		if(list.size() > 1){
			throw new BusinessException("LMS-05197");
		}
		return list.get(0);
	}

	/**
	 * LMS-2505
	 * @param nrCodigoBarras
	 * @return Conhecimento
	 */
	public Conhecimento findConhecimentoByNrChaveCTE(String nrCodigoBarras) {

		Conhecimento conhecimento = null;

		List<MonitoramentoDocEletronico> monitoramentoDocEletronico = monitoramentoDocEletronicoService.findListByNrChave(nrCodigoBarras);

		if (monitoramentoDocEletronico.isEmpty()) {
			throw new BusinessException("LMS-10040");
		}

		if (monitoramentoDocEletronico.size() > 1) {
			throw new BusinessException("LMS-05197");
		}else{
			conhecimento = getConhecimentoDAO().findById(monitoramentoDocEletronico.get(0).getDoctoServico().getIdDoctoServico());
			if (conhecimento == null) {
				throw new BusinessException("LMS-10040");
			}else{
				return conhecimento;
			}
		}
	}
	
	public DadosEmbarquePipelineDTO findConhecimentosEmitidosByListTabelaPreco(List<TabelaPreco> lstTabelasPreco) {
		return getConhecimentoDAO().findConhecimentosEmitidosByListTabelaPreco(lstTabelasPreco);
	}

	public DadosEmbarquePipelineDTO findConhecimentosEmitidosByListSimulacao(List simulacoes) {
		return getConhecimentoDAO().findConhecimentosEmitidosByListSimulacao(simulacoes);
	}

	/**
	 * Gera eventos para diversos tipos de conhecimento
	 * 
	 * @param conhecimento
	 * @param idFilial
	 */
	public void storeEventos(Conhecimento conhecimento, Long idFilial) {
		
		/*Nao grava evento para Nota Fiscal de Servico*/
		String tpDoctoServico = conhecimento.getTpDoctoServico().getValue();
		if(ConstantesExpedicao.NOTA_FISCAL_SERVICO.equals(tpDoctoServico)) {
			return;
		}

		Filial filial = filialService.findById(idFilial);
		/*Inclui eventos para o Conhecimento Novo*/
		Short cdEvento = null;
		Long idDoctoServico = conhecimento.getIdDoctoServico();
		String tpDocumento = conhecimento.getTpDocumentoServico().getValue();
		String nrCtrc = ConhecimentoUtils.formatConhecimento(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrConhecimento());

		/*Documento emitido*/
		String tpConhecimento = conhecimento.getTpConhecimento().getValue();
		String dsObservacao = filial.getSgFilial() + " - " + filial.getPessoa().getNmFantasia();
		if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(tpConhecimento) || ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(tpConhecimento) || ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equals(tpConhecimento)) {
			cdEvento = ConstantesSim.EVENTO_DOCUMENTO_EMITIDO_SEM_LOCALIZACAO;
			incluirEventosService.generateEventoDocumento(cdEvento ,idDoctoServico ,idFilial ,nrCtrc ,null ,null, dsObservacao ,tpDocumento);
		} else {
			cdEvento = ConstantesSim.EVENTO_DOCUMENTO_EMITIDO;
			if(Boolean.TRUE.equals(conhecimento.getBlExecutarVerificacaoDocumentoManifestado())){
				incluirEventosService.generateEventoDocumento(cdEvento ,idDoctoServico ,idFilial ,nrCtrc ,null ,null, dsObservacao ,tpDocumento, false, true);
			} else {
				incluirEventosService.generateEventoDocumento(cdEvento ,idDoctoServico ,idFilial ,nrCtrc ,null ,null, dsObservacao ,tpDocumento);
		}
		}
		
		/*Data prevista*/
		cdEvento = ConstantesSim.EVENTO_DATA_PREVISTA;
		DateTime dhPrevisaoEntrega = JTDateTimeUtils.yearMonthDayToDateTime(conhecimento.getDtPrevEntrega(), conhecimento.getFilialOrigem().getDateTimeZone());
		incluirEventosService.generateEventoDocumento(cdEvento, idDoctoServico, idFilial, nrCtrc, dhPrevisaoEntrega, null, null, tpDocumento);

		/* Inclui eventos para o Conhecimento Original */
		cdEvento = null;
		if(ConstantesExpedicao.CONHECIMENTO_REENTREGA.equals(tpConhecimento)) {
			cdEvento = ConstantesSim.EVENTO_DOCUMENTO_REENTREGA_EMITIDO; /*Documento de reentrega emitido*/
		} else if(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(tpConhecimento) || ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO_PARCIAL.equals(tpConhecimento)) {
			cdEvento = ConstantesSim.EVENTO_DOCUMENTO_DEVOLUCAO_EMITIDO; /*Documento de devolucao emitido*/
		} else if(ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_FRETE.equals(tpConhecimento) || ConstantesExpedicao.CONHECIMENTO_COMPLEMENTO_ICMS.equals(tpConhecimento)) {
			cdEvento = ConstantesSim.EVENTO_DOCUMENTO_COMPLEMENTO_EMITIDO; /*Documento de complemento emitido*/
		} else if(ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO.equals(tpConhecimento)) {
			cdEvento = ConstantesSim.EVENTO_DOCUMENTO_REFATURA_EMITIDO; /*Documento de refatura emitido*/
		} else if(ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equals(tpConhecimento)) {
			cdEvento = ConstantesSim.EVENTO_CONHECIMENTO_SUBSTITUTO_EMITIDO;
		}

		if(cdEvento != null) {
			idDoctoServico = conhecimento.getDoctoServicoOriginal().getIdDoctoServico();

			incluirEventosService.generateEventoDocumento(cdEvento, idDoctoServico, idFilial, nrCtrc, null, null, null, tpDocumento);
		}
		
		/*Se for uma emissão de CTRC cancelado*/
		storeEventoCancelamentoConhecimento(conhecimento, idFilial);
		
	}
	
	public void storeEventoCancelamentoConhecimento(final Conhecimento conhecimento, final Long idFilial) {
		if("C".equals(conhecimento.getTpSituacaoConhecimento().getValue())){

			incluirEventosService.generateEventoDocumento(ConstantesSim.EVENTO_DOCUMENTO_CANCELADO, conhecimento.getIdDoctoServico(), idFilial, ConhecimentoUtils.formatConhecimento(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrConhecimento()), JTDateTimeUtils.getDataHoraAtual(), null, null, conhecimento.getTpDocumentoServico().getValue());
			notaFiscalConhecimentoService.removeByIdConhecimento(conhecimento.getIdDoctoServico());
			//jira LMS-448
			liberacaoNotaNaturaService.atualizaTerraNaturaCancelado(conhecimento.getIdDoctoServico());
			
			DevedorDocServFat devedorDocServFat = devedorDocServFatService.findDevedorDocServFatByIdDoctoServico(conhecimento.getIdDoctoServico());
			if(devedorDocServFat != null) {
				if(devedorDocServFat.getTpSituacaoCobranca().getValue().equals("P") || devedorDocServFat.getTpSituacaoCobranca().getValue().equals("C")) {
					devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
					devedorDocServFat.setDtLiquidacao(JTDateTimeUtils.getDataAtual());
					devedorDocServFatService.store(devedorDocServFat);
				} else {
					throw new BusinessException("LMS-04115");
	}
			} 
		}
	}
	
	public void executeEmissaoCTEAutorizado(Long idConhecimento){
		Conhecimento conhecimento = findById(idConhecimento);
		if( conhecimento.getTpSituacaoConhecimento() != null && "P".equals(conhecimento.getTpSituacaoConhecimento().getValue()) ){
			conhecimento.setTpSituacaoConhecimento(new DomainValue("E"));
			conhecimento.setBlExecutarVerificacaoDocumentoManifestado(Boolean.TRUE);
			storeEventos(conhecimento, conhecimento.getFilialOrigem().getIdFilial());
			conhecimento.setFilialLocalizacao(conhecimento.getFilialOrigem());
			getConhecimentoDAO().store(conhecimento);

			Contingencia contingencia = emContingencia(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
			if (contingencia != null) {
				contingenciaService.updateQtUtilizacoes(contingencia);
		}

			// LMS-4068
			if (ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO.equalsIgnoreCase(conhecimento.getTpConhecimento().getValue())) {
				DevedorDocServFat devedorDocServFat = devedorDocServFatService.findDevedorDocServFatByIdDoctoServico(conhecimento.getDoctoServicoOriginal().getIdDoctoServico());
				devedorDocServFat.setTpSituacaoCobranca(new DomainValue("L"));
				YearMonthDay data = new YearMonthDay(conhecimento.getDhEmissao().getYear(), conhecimento.getDhEmissao().getMonthOfYear(), conhecimento.getDhEmissao().getDayOfMonth());
				devedorDocServFat.setDtLiquidacao(data);
				devedorDocServFatService.store(devedorDocServFat);
	}
	}
	}
	
	/**
	 * Gera evento de cancelamento de documento caso o documento não seja
	 * validado nas regras de bloqueio de valores (vlMercadoria, vlFrete e
	 * percentual do segundo em relação ao primeiro).
	 * 
	 * @param conhecimento
	 */
	public Boolean executeCancelamentoPorBloqueioValores(Conhecimento conhecimento, Boolean utilizarFilialOrigemConhecimento) {
		Boolean valido = Boolean.TRUE;
		TypedFlatMap map = new TypedFlatMap();
		map.put("lancarExcecao", false);
		map.put("buscarMsgValidacao", true);
		map.put("vlFrete", conhecimento.getVlTotalDocServico());
		map.put("vlMercadoria", conhecimento.getVlMercadoria());

		//LMS-5176
		if(utilizarFilialOrigemConhecimento != null && utilizarFilialOrigemConhecimento){
			map.put("filialOrigem", conhecimento.getFilialByIdFilialOrigem());
		}

		map = doctoServicoService.executeValidacaoLimiteValorFrete(map);
		if (!map.getBoolean("vlValido")) {
			conhecimentoCancelarService.storeEventoCancelamentoBloqueioValores(conhecimento, map.getString("msgValidacao"));
			valido = Boolean.FALSE;
			
		} else {
			
			map = doctoServicoService.executeValidacaoPercentualValorMercadoria(map);
			if (!map.getBoolean("vlValido")) {
				conhecimentoCancelarService.storeEventoCancelamentoBloqueioValores(conhecimento, map.getString("msgValidacao"));
				valido = Boolean.FALSE;
				
			} else {
				
				map = doctoServicoService.executeValidacaoLimiteValorMercadoria(map);
				if (!map.getBoolean("vlValido")) {
					conhecimentoCancelarService.storeEventoCancelamentoBloqueioValores(conhecimento, map.getString("msgValidacao"));
					valido = Boolean.FALSE;
				}
			}
		}
		
		return valido;
	}
	
	
	/**
	 * Verifica se há uma Contingência ativa para a filial
	 * 
	 * @param idFilial
	 * @return {@link Contingencia}
	 */
	private Contingencia emContingencia(long idFilial) {
		return contingenciaService.findByFilial(idFilial, "A", "C");
	}
	
    public List<Conhecimento> findCTEByIdFatura(Long idFatura){
    	return getConhecimentoDAO().findCTEByIdFatura(idFatura);
    }
	
	public List<Conhecimento> findPreConhecimentoByIdControleCarga(long idControleCarga){
		return getConhecimentoDAO().findPreConhecimentoByIdControleCarga(idControleCarga);
	}
	
	public Conhecimento findTpDocumentoServicoByCodBarEmbarque(Map criteria){
		return getConhecimentoDAO().findTpDocumentoServicoByCodBarEmbarque(criteria);
	}
	
	public Map<String,Object> findComplementosXMLCTE(final Long idConhecimento){
		return getConhecimentoDAO().findComplementosXMLCTE(idConhecimento);
	}
	
	public void generateInutilizadoSalto(DoctoServicoSaltoDMN docto){
		Moeda moeda = moedaService.findMoedaPadraoBySiglaPais(ConstantesAmbiente.SG_PAIS_BRASIL);
		Pais pais = paisService.findPaisBySgPais(ConstantesAmbiente.SG_PAIS_BRASIL);
		String localMerc = configuracoesFacadeImpl.getValorParametro(ConstantesExpedicao.ID_LOCA_DOC_CANCELADO).toString();
		String natProd = (String) configuracoesFacadeImpl.getValorParametro(ConstantesExpedicao.ID_NATUREZA_PRODUTO_PADRAO).toString();
		Densidade densidade = densidadeService.findByTpDensidade(ConstantesExpedicao.TP_DENSIDADE_PADRAO);
		
		Map<Long, InscricaoEstadual> inscricoes = new HashMap<Long, InscricaoEstadual>();		
		inscricoes.put(docto.getIdFilial(), inscricaoEstadualService.findByPessoaIndicadorPadrao(docto.getIdFilial(), true));
		
		this.generateInutilizadoSalto(docto, moeda, pais, Long.valueOf(localMerc), Long.valueOf(natProd), densidade, inscricoes);
	}
	
	public void generateInutilizadoSalto(DoctoServicoSaltoDMN docto, Moeda moeda, Pais pais, Long localMerc, Long natProd, Densidade densidade, Map<Long, InscricaoEstadual> inscricoes){
			
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
			Filial filial = filialService.findById(docto.getIdFilial());
			
			Long idUsuario = Long.valueOf(configuracoesFacadeImpl.getValorParametro(ID_USUARIO_PADRAO_INTEGRACAO).toString());
			Usuario usuario = new Usuario();
			usuario.setIdUsuario(idUsuario);
			
			SessionContext.setUser(usuario);			
			SessionContext.set(SessionKey.FILIAL_KEY, filial);
			
			//"ID_USUARIO_PADRAO_INTEGRACAO";

			//CONHECIMENTO
			Conhecimento conhecimento = this.generateConhecimento(filial, docto.getNrDoctoServico(), moeda, pais, localMerc, natProd, densidade, inscricoes);

			//EVENTO
			String nrCtrcFormatado = ConhecimentoUtils.formatConhecimento(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrDoctoServico(), null);
			incluirEventosService.generateEventoDocumento(
					ConstantesSim.EVENTO_DOCUMENTO_CANCELADO,
					conhecimento.getIdDoctoServico(),
					docto.getIdFilial(),
					nrCtrcFormatado,
					dataHoraAtual,
					null,
					null,
					conhecimento.getTpDocumentoServico().getValue());

			//DEVEDOR DOC SERV
			getDevedorDocServService().generateDevedorDocServ(conhecimento);

			//DEVEDOR DOC SERV FAT
			devedorDocServFatService.generateDevedorDocServFat(conhecimento);

			//TBDATABASEINPUT_CTE e MONITORAMENTO DOC ELETRONICO
			MonitoramentoDocEletronico mde = new MonitoramentoDocEletronico();

			mde.setDoctoServico(conhecimento);
			mde.setTpSituacaoDocumento(new DomainValue("I"));

			Integer random =getGerarConhecimentoEletronicoXMLService().getRandom(conhecimento);

			String chave = getGerarConhecimentoEletronicoXMLService().gerarChaveAcesso(conhecimento, random);
			mde.setNrChave(chave.substring(3));

			//LMS-4210
			Filial filialUsuarioLogado = SessionUtils.getFilialSessao();
			String conteudoParametro = String.valueOf(getConteudoParametroFilialService().findConteudoByNomeParametroWithException(filialUsuarioLogado.getIdFilial(), "Versão_XML_CTe", false));

			boolean versao300a = conteudoParametro != null && conteudoParametro.equals(EVersaoXMLCTE.VERSAO_300a.getConteudoParametro());

			if(versao300a) {

				String xml = gerarXMLInutCTE(chave);
				mde.setTpSituacaoDocumento(new DomainValue("H"));
				TBDatabaseInputCTE tbDatabaseInput = integracaoNDDigitalService.generateIntegracaoInutilizacao(mde, xml);
				mde.setIdEnvioDocEletronicoI(tbDatabaseInput.getId());

			}

			monitoramentoDocEletronicoService.store(mde);		
	}

	//LMS-4210
	private String gerarXMLInutCTE(String chave) {
		StringBuilder sb = new StringBuilder();
		sb.append("<cteCancInut>");
		sb.append("<chCTe>");
		sb.append(chave);
		sb.append("</chCTe>");
		sb.append("<xMotivoInut>");
		sb.append(parametroGeralService.findSimpleConteudoByNomeParametro(ConstantesExpedicao.JUST_INUT_CTE));
		sb.append("</xMotivoInut>");
		sb.append("</cteCancInut>");

		return sb.toString();
	}
	
	public Conhecimento generateConhecimento(Filial filial, Long nrDoctoServico, Moeda moeda, Pais pais, Long localMerc, Long natProd, Densidade densidade, Map<Long, InscricaoEstadual> inscricoes){
		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
		Conhecimento conhecimento = new Conhecimento();
		
		conhecimento.setMoeda(moeda);
		
		Usuario usuarioInclusao = new Usuario();
		usuarioInclusao.setIdUsuario(Long.valueOf(6995));
		conhecimento.setUsuarioByIdUsuarioInclusao(usuarioInclusao);
		
		conhecimento.setFilialByIdFilialOrigem(filial);
		conhecimento.setFilialByIdFilialDestino(filial);
		conhecimento.setVlTotalDocServico(BigDecimal.ZERO);
		conhecimento.setVlTotalParcelas(BigDecimal.ZERO);
		conhecimento.setVlTotalServicos(BigDecimal.ZERO);
		conhecimento.setVlImposto(BigDecimal.ZERO);
		
		//Busca próximo Docto_Servico existente para atribuir a DH_EMISSAO ao que está sendo criado
		Long nextIdDoctoServico = doctoServicoService.findDoctoServicoByTpDoctoByIdFilialOrigemByNrDoctoSalto(ConstantesExpedicao.CONHECIMENTO_ELETRONICO, filial.getIdFilial(), nrDoctoServico);
		DoctoServico nextDoctoServico = doctoServicoService.findById(nextIdDoctoServico);
		conhecimento.setDhEmissao(nextDoctoServico.getDhEmissao());
		
		conhecimento.setDhInclusao(dataHoraAtual);
		conhecimento.setDhAlteracao(dataHoraAtual);
		conhecimento.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		conhecimento.setBlBloqueado(false);
		
		conhecimento.setPaisOrigem(pais);
		
		LocalizacaoMercadoria localizacaoMercadoria = new LocalizacaoMercadoria();
		localizacaoMercadoria.setIdLocalizacaoMercadoria(localMerc); 
		conhecimento.setLocalizacaoMercadoria(localizacaoMercadoria);
		
		conhecimento.setNrDoctoServico(nrDoctoServico);
		conhecimento.setFilialLocalizacao(filial);
		
		conhecimento.setVlLiquido(BigDecimal.ZERO);
		
		Cliente cliente = new Cliente();
		cliente.setIdCliente(conhecimento.getFilialByIdFilialOrigem().getIdFilial());		
		conhecimento.setClienteByIdClienteRemetente(cliente);
		conhecimento.setClienteByIdClienteDestinatario(cliente);
		
		conhecimento.setInscricaoEstadualRemetente(inscricoes.get(conhecimento.getFilialByIdFilialOrigem().getIdFilial()));
		conhecimento.setInscricaoEstadualDestinatario(inscricoes.get(conhecimento.getFilialByIdFilialOrigem().getIdFilial()));
		
		NaturezaProduto naturezaProduto = new NaturezaProduto();
		naturezaProduto.setIdNaturezaProduto(natProd);
		conhecimento.setNaturezaProduto(naturezaProduto);
		
		conhecimento.setFilialOrigem(conhecimento.getFilialByIdFilialOrigem());
		conhecimento.setMunicipioByIdMunicipioColeta(filial.getPessoa().getEnderecoPessoa().getMunicipio());
		
		conhecimento.setDensidade(densidade);
		
		conhecimento.setMunicipioByIdMunicipioEntrega(conhecimento.getMunicipioByIdMunicipioColeta());
		conhecimento.setNrConhecimento(conhecimento.getNrDoctoServico());
		conhecimento.setNrCepEntrega(filial.getPessoa().getEnderecoPessoa().getNrCep());
		conhecimento.setNrCepColeta(filial.getPessoa().getEnderecoPessoa().getNrCep());
		conhecimento.setDsEspecieVolume(ConstantesExpedicao.DS_ESPECIE_VOLUME);
		conhecimento.setTpFrete(new DomainValue(ConstantesExpedicao.TP_FRETE_CIF));
		conhecimento.setTpConhecimento(new DomainValue(ConstantesExpedicao.CONHECIMENTO_NORMAL));
		conhecimento.setBlIndicadorEdi(false);
		conhecimento.setBlIndicadorFretePercentual(false);
		conhecimento.setTpSituacaoConhecimento(new DomainValue(ConstantesExpedicao.TP_STATUS_CANCELADO));
		conhecimento.setBlPermiteTransferencia(false);
		conhecimento.setBlReembolso(false);
		conhecimento.setTpDevedorFrete(new DomainValue(ConstantesExpedicao.TP_DEVEDOR_REMETENTE));
		conhecimento.setBlSeguroRr(false);
		conhecimento.setTpDoctoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		
		store(conhecimento);	
		
		return conhecimento;
	}
	
	public List<Conhecimento> findConhecimentosByControleCarga(Long idControleCarga) {
		return getConhecimentoDAO().findConhecimentosByControleCarga(idControleCarga);
	}
	
	public Conhecimento findByIdJoinFiliais(final Long idConhecimento) {
		return getConhecimentoDAO().findByIdJoinFiliais(idConhecimento);
	}

	//LMS-3054
	public List<Long> findCtesAntigosRemover(DateTime olderThan) {
		return getConhecimentoDAO().findCtesAntigosRemover(olderThan);
	}



	public void setConhecimentoCancelarService(ConhecimentoCancelarService conhecimentoCancelarService) {
		this.conhecimentoCancelarService = conhecimentoCancelarService;
	}
	
	public List findConhecimentosSemAWB(TypedFlatMap criteria) {
		List result = getConhecimentoDAO().findConhecimentosSemAWB(criteria);
		
		for(Iterator it = result.iterator(); it.hasNext();) {
			TypedFlatMap map = (TypedFlatMap) it.next();
			
			String tpDocumento = map.getDomainValue("tpDocumentoServico.value").getValue();
			String nrCtrc = new String();
			
			if(tpDocumento.equals(ConstantesExpedicao.CONHECIMENTO_NACIONAL)){
				nrCtrc = ConhecimentoUtils.formatConhecimento(map.getString("filialOrigem.sgFilial"),
															  map.getLong("nrConhecimento"),
															  map.getInteger("dvConhecimento"));
			} else {
				nrCtrc = ConhecimentoUtils.formatConhecimento(map.getString("filialOrigem.sgFilial"),
															  map.getLong("nrConhecimento"),
															  null);
			}
			
			map.put("nrDocumento", nrCtrc);
			map.put("qtVolumesFormatado", FormatUtils.formatDecimalLocale("###,##0", 
					(Integer) map.get("doctoServico.qtVolumes")));			
		}
		
		return result;		
	}

	public List findDimensoes(TypedFlatMap criteria) {
		List result = getConhecimentoDAO().findDimensoes(criteria);	
		List<TypedFlatMap> dados = new ArrayList<TypedFlatMap>();
		
		for(Iterator it = result.iterator();it.hasNext();) {
			TypedFlatMap item = (TypedFlatMap) it.next();
			TypedFlatMap map = new TypedFlatMap();
			
			map.put("nrDimensao1", FormatUtils.formatDecimalLocale("###,##0", (Integer) item.get("nrDimensao1")));
			map.put("nrDimensao2", FormatUtils.formatDecimalLocale("###,##0", (Integer) item.get("nrDimensao2")));
			map.put("nrDimensao3", FormatUtils.formatDecimalLocale("###,##0", (Integer) item.get("nrDimensao3")));
			
			dados.add(map);
		}
		
		return dados;
	}
	

	public TypedFlatMap findTotalizadores(TypedFlatMap criteria) {
		TypedFlatMap dadosTotalizadores = getConhecimentoDAO().findTotalizadores(criteria);	
		TypedFlatMap dados = new TypedFlatMap();
		
		if (dadosTotalizadores != null){
			dados.put("qtdDocumentosTotal", FormatUtils.formatDecimalLocale(
					"###,##0", (Long) dadosTotalizadores.get("qtdDocumentosTotal")));
			
			dados.put("vlMercadoriaTotal", FormatUtils.formatDecimalLocale(
					 "###,###,##0.00", (BigDecimal) dadosTotalizadores.get("vlMercadoriaTotal")));
			
			dados.put("psRealTotal", FormatUtils.formatDecimalLocale(
					"#,###,##0.000", (BigDecimal) dadosTotalizadores.get("psRealTotal")));
			
			dados.put("qtVolumesTotal", FormatUtils.formatDecimalLocale(
					"###,##0", (Long) dadosTotalizadores.get("qtVolumesTotal")));
			
			dados.put("psCubadoTotal", FormatUtils.formatDecimalLocale(
					"#,###,##0.000", (BigDecimal) dadosTotalizadores.get("psCubadoTotal")));			
		}
		
		return dados;
	}

	public EdiConembDTO findDadosEnvioConhecimento(Long idConhecimento){
	    Conhecimento conhecimento = findById(idConhecimento);
	    
	    
	    EdiConembDTO dto = new EdiConembDTO();

        dto.setNrDocumento(conhecimento.getNrConhecimento().toString());
        
        dto.setDtEmissao(new Timestamp(conhecimento.getDhEmissao().getMillis()));
        
        dto.setIdClienteRemetente(conhecimento.getClienteByIdClienteRemetente().getIdCliente());
        dto.setIdClienteDestinatario(conhecimento.getClienteByIdClienteDestinatario().getIdCliente());
        
        dto.setIdFilialOrigem(conhecimento.getFilialByIdFilialOrigem().getIdFilial());
        dto.setIdFilialDestino(conhecimento.getFilialByIdFilialDestino().getIdFilial());
        
        dto.setCnpjEmitente(conhecimento.getFilialOrigem().getPessoa().getNrIdentificacao());
        
        Pessoa clienteTomador = pessoaService.findById(conhecimento.getDevedorDocServFats().get(0).getCliente().getIdCliente());
        dto.setCnpjTomador(clienteTomador.getNrIdentificacao());
        dto.setTpConhecimento(conhecimento.getTpConhecimento().getValue());
        dto.setTpDocumento(conhecimento.getTpDoctoServico().getValue());
        
        dto.setCd_ibge_remetente(municipioService.findCdIbgeByPessoa(conhecimento.getClienteByIdClienteRemetente().getIdCliente()));
        dto.setCd_ibge_destinatario(municipioService.findCdIbgeByPessoa(conhecimento.getClienteByIdClienteDestinatario().getIdCliente()));
        
        dto.setVlFreteLiquido(conhecimento.getVlLiquido().subtract(conhecimento.getVlImposto()));
        dto.setVlImposto(conhecimento.getVlImposto());
        
        dto.setComplementos(new ArrayList<EdiConembComplementoDto>());

        List<DadosComplemento> complementosConhecimento= dadosComplementoService.findByConhecimento(conhecimento.getIdDoctoServico()); 
        for(DadosComplemento complemento: complementosConhecimento){
            String dsCampo = null;
            if (complemento.getInformacaoDoctoCliente() != null){
                dsCampo = complemento.getInformacaoDoctoCliente().getDsCampo();
            }else if(complemento.getInformacaoDocServico() != null){
                dsCampo = complemento.getInformacaoDocServico().getDsCampo();
            }
            if (dsCampo != null){
                dto.getComplementos().add(new EdiConembComplementoDto(complemento.getDsValorCampo(), dsCampo));
            }
        }
        
        dto.setNotas(new ArrayList<EdiConembNotaDto>());
        for(NotaFiscalConhecimento notaFiscalConhecimento: conhecimento.getNotaFiscalConhecimentos()){
            Timestamp dtEmissao = new Timestamp(notaFiscalConhecimento.getDtEmissao().toDateTimeAtMidnight().getMillis());
            dto.getNotas().add(new EdiConembNotaDto(notaFiscalConhecimento.getNrChave(), dtEmissao));
        }
        
        MonitoramentoDocEletronico mde = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(idConhecimento);
        if (mde != null && mde.getDsDadosDocumento()!= null){
            dto.setDsDadosDocumento(new String(mde.getDsDadosDocumento(),Charset.forName("UTF-8")));
            dto.setChaveCte(mde.getNrChave());
        }
        
        dto.setImpostos(new ArrayList<EdiConembImpostoDto>());
        for (ImpostoServico impostoServico: conhecimento.getImpostoServicos()){
            EdiConembImpostoDto dtoImposto = new EdiConembImpostoDto();
            dtoImposto.setTpImposto(impostoServico.getTpImposto().getValue());
            dtoImposto.setPcAliquota(impostoServico.getPcAliquota());
            dtoImposto.setVlBaseCalculo(impostoServico.getVlBaseCalculo());
            dtoImposto.setVlImposto(impostoServico.getVlImposto());
            dto.getImpostos().add(dtoImposto);
        }
        
        return dto;
	}
	
	public List<Conhecimento> findConhecimentoSubstitutoByIdConhecimentoOrigem(Long idConhecimentoOrigem) {
		return getConhecimentoDAO().findConhecimentoSubstitutoByIdConhecimentoOrigem(idConhecimentoOrigem);
	}

	public List<Conhecimento> findByCriteria(Map<String, Object> criteria) {
		return getConhecimentoDAO().findByCriteria(criteria);
	}

	public List<Conhecimento> findByCalculoAgendamento(YearMonthDay dtEvento) {
		return this.getConhecimentoDAO().findByCalculoAgendamento(dtEvento);
	}
	
	public List<Conhecimento> findByCalculoDiarioPermanencia(YearMonthDay dtBloqueio, YearMonthDay dtEntrega) {
		return getConhecimentoDAO().findByCalculoDiarioPermanencia(dtBloqueio, dtEntrega);
	}
	
	public List<Conhecimento> findByCalculoMensalPermanencia(YearMonthDay dtBloqueio, YearMonthDay dtInicio) {
		return getConhecimentoDAO().findByCalculoMensalPermanencia(dtBloqueio, dtInicio);
	}
	
	public List<Conhecimento> findByCalculoFatPerdidoPermanencia(YearMonthDay dtBloqueio, YearMonthDay dtEntrega) {
		return getConhecimentoDAO().findByCalculoFatPerdidoPermanencia(dtBloqueio, dtEntrega);
	}		
	
	public List<Conhecimento> findByCalculoDiarioPermanenciaAgendamento(YearMonthDay dtEvento) {
		return this.getConhecimentoDAO().findByCalculoDiarioPermanenciaAgendamento(dtEvento);
	}
	
   	public List<Conhecimento> findByCalculoMensalPermanenciaAgendamento(YearMonthDay dtEvento) {
   		return this.getConhecimentoDAO().findByCalculoMensalPermanenciaAgendamento(dtEvento);
   	}
   	
   	public List<Map<String, Object>> findByDecursoPrazo(Map<String, Object> parameters) {
   		return this.getConhecimentoDAO().findByDecursoPrazo(parameters);
   	}
   	
   	public Map<String, Object> findConhecimentoAberbacao(Long idConhecimento) {
   		return this.getConhecimentoDAO().findConhecimentoAberbacao(idConhecimento);
   	}

	public List<Conhecimento> findByIdPedidoColeta(Long idPedidoColeta) {
		return this.getConhecimentoDAO().findByIdPedidoColeta(idPedidoColeta);
	}
 
	public List<Map<String,Object>> findPendentesPorCalculo(Long idMonitoramento){
		return this.getConhecimentoDAO().findPendentesPorCalculo(idMonitoramento);
	}

	public Map<String, Object> findDadosLocalizacaoDoctoServico(Long idDoctoServico) {
		return getConhecimentoDAO().findDadosLocalizacaoDoctoServico(idDoctoServico);
	}

	public List<Map<String, Object>> findConhecimentosNaoComissionaveis(Long idExecutivo, Long idCliente, YearMonthDay dtCompeInicio, YearMonthDay dtCompeFim,
			Map<String, Object> paginationInfoMap) {

		paginationInfoMap.put("idExecutivo", idExecutivo);
		paginationInfoMap.put("idCliente", idCliente);
		if (dtCompeInicio != null) {
			paginationInfoMap.put("dtCompeInicio", dtCompeInicio.toString("yyyy-MM-dd"));
		}
		if (dtCompeFim != null) {
			paginationInfoMap.put("dtCompeFim", dtCompeFim.toString("yyyy-MM-dd"));
		}
		
		return getConhecimentoDAO().findConhecimentosNaoComissionaveis(paginationInfoMap);
	}
	
	public Integer findCountConhecimentosNaoComissionaveis(Long idExecutivo, Long idCliente, YearMonthDay dtCompeInicio, YearMonthDay dtCompeFim) {

		Map<String, Object> criteriaMap = new HashMap<String, Object>();

		criteriaMap.put("idExecutivo", idExecutivo);
		criteriaMap.put("idCliente", idCliente);
		if (dtCompeInicio != null) {
			criteriaMap.put("dtCompeInicio", dtCompeInicio.toString("yyyy-MM-dd"));
		}
		if (dtCompeFim != null) {
			criteriaMap.put("dtCompeFim", dtCompeFim.toString("yyyy-MM-dd"));
		}
		
		return getConhecimentoDAO().findCountConhecimentosNaoComissionaveis(criteriaMap);
	}

	
	public Conhecimento executeTratamentoManifestoAutomatico(Conhecimento conhecimento, Filial filialRomaneio, String cdFilialFedexRomaneio){
	    
	    Session session = getConhecimentoDAO().getAdsmHibernateTemplate().getSessionFactory().openSession();
        session.beginTransaction();

        Usuario usuario = usuarioService.findById(5000l);

        Pais pais = paisService.findByIdPessoa(filialRomaneio.getIdFilial());           
        volDadosSessaoService.executeDadosSessaoBanco(usuario, filialRomaneio, pais);
	    
	    conhecimento = findById(conhecimento.getIdDoctoServico());
	    if (conhecimento.getBlBloqueado()){
	        
	        String nmParametroOcorrencia = "OCORRENCIA_LIBERACAO_FEDEX";
	        
	        BigDecimal cdOcorrenciaBloqueio = (BigDecimal)parametroGeralService.findConteudoByNomeParametroWithoutException(nmParametroOcorrencia, false);
	        OcorrenciaPendencia ocorrencia = ocorrenciaPendenciaService.findByCodigoOcorrencia(Short.valueOf(cdOcorrenciaBloqueio.toString()));
	        ocorrenciaDoctoServicoService.executeBloquearLiberarDoctoServicoByOcorrenciaPendencia(conhecimento.getIdDoctoServico(), ocorrencia.getIdOcorrenciaPendencia(), null, JTDateTimeUtils.getDataHoraAtual(), null);
	    }
	    
	    Short cdLocalizacaoConhecimento = conhecimento.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria(); 
	    Filial filialLocalizacaoConhecimento = conhecimento.getFilialLocalizacao();
	    
	    if (EM_DESCARGA.equals(cdLocalizacaoConhecimento)){
	        LocalizacaoMercadoria localizacao = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(NO_TERMINAL);
            conhecimento.setLocalizacaoMercadoria(localizacao);
	    }else if (AGUARDANDO_DESCARGA.equals(cdLocalizacaoConhecimento) && filialLocalizacaoConhecimento.equals(filialRomaneio)){
	        ManifestoViagemNacional manifestoViagemNacional = manifestoNacionalCtoService.findManifestoViagemAbertoByDoctoServico(conhecimento, filialRomaneio);
	        if (manifestoViagemNacional!= null){
                Manifesto manifesto = manifestoViagemNacional.getManifesto();
                ControleCarga controleCarga = manifesto.getControleCarga();
                
                criarAtualizarCarregamentoDescargaIntegracao(filialRomaneio, controleCarga);

                manifesto.setTpStatusManifesto(new DomainValue("FE"));
                manifestoService.storeManifesto(manifesto);
                
                eventoControleCargaService.storeEventoControleCarga(controleCarga, "ID");
                eventoControleCargaService.storeEventoControleCarga(controleCarga, "FD");
                
                LocalizacaoMercadoria localizacaoNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(NO_TERMINAL);
                List<ManifestoNacionalCto> manifestoNacionalCtos = manifestoNacionalCtoService.findConhecimentos(manifestoViagemNacional.getIdManifestoViagemNacional());
                for (ManifestoNacionalCto manifestoNacionalCto : manifestoNacionalCtos){
                    Conhecimento conhecimentoManifesto = manifestoNacionalCto.getConhecimento();
                    conhecimentoManifesto.setLocalizacaoMercadoria(localizacaoNoTerminal);
                    store(conhecimento);
                }
                controleCarga.setTpStatusControleCarga(new DomainValue("FE"));
                controleCargaService.store(controleCarga);
            }
	        
	        
	    }else if (EM_VIAGEM.equals(cdLocalizacaoConhecimento)){
	        ManifestoViagemNacional manifestoViagemNacional = manifestoNacionalCtoService.findManifestoViagemAbertoByDoctoServico(conhecimento, filialRomaneio);
            if (manifestoViagemNacional!= null && manifestoViagemNacional.getManifesto().getFilialByIdFilialDestino().equals(filialRomaneio)){
                Manifesto manifesto = manifestoViagemNacional.getManifesto();
                ControleCarga controleCarga = manifesto.getControleCarga();
                
                manifestoEletronicoService.encerrarMdfesAutorizados(controleCarga.getIdControleCarga());
                
                eventoControleCargaService.storeEventoControleCarga(controleCarga, "CP");
                eventoControleCargaService.storeEventoControleCarga(controleCarga, "ID");
                eventoControleCargaService.storeEventoControleCarga(controleCarga, "FD");
                
                criarAtualizarCarregamentoDescargaIntegracao(filialRomaneio, controleCarga);

                manifesto.setTpStatusManifesto(new DomainValue("FE"));
                manifestoService.storeManifesto(manifesto);
                
                LocalizacaoMercadoria localizacaoNoTerminal = localizacaoMercadoriaService.findLocalizacaoMercadoriaByCodigo(Short.valueOf("24"));
                List<ManifestoNacionalCto> manifestoNacionalCtos = manifestoNacionalCtoService.findConhecimentos(manifestoViagemNacional.getIdManifestoViagemNacional());
                for (ManifestoNacionalCto manifestoNacionalCto : manifestoNacionalCtos){
                    Conhecimento conhecimentoManifesto = manifestoNacionalCto.getConhecimento();
                    conhecimentoManifesto.setLocalizacaoMercadoria(localizacaoNoTerminal);
                    conhecimentoManifesto.setFilialLocalizacao(filialRomaneio);
                    store(conhecimentoManifesto);
                }
                
                List<Manifesto> manifestosEmAberto = manifestoService.findManifestoEmAbertoByIdControleCarga(controleCarga.getIdControleCarga());
                if (manifestosEmAberto == null || manifestosEmAberto.isEmpty()){
                    controleCarga.setTpStatusControleCarga(new DomainValue("FE"));
                    controleCargaService.store(controleCarga);
                }else{
                    controleCarga.setTpStatusControleCarga(new DomainValue("PO"));
                    controleCargaService.store(controleCarga);
                }
            }
	    }else if (NO_TERMINAL.equals(cdLocalizacaoConhecimento)){
	        
	        List<Filial> filiaisParceiras = filialService.findFiliaisByCdFilialFedex(cdFilialFedexRomaneio);
	        if (filiaisParceiras.contains(conhecimento.getFilialLocalizacao())){
	            conhecimento.setFilialLocalizacao(filialRomaneio);
	            store(conhecimento);
	        }
	        
	    }
	    
	    try {
            session.getTransaction().commit();
        } catch (Exception ex) {
            throw new IllegalStateException(ex.getMessage());
        } finally {
            if (session != null) {
                session.flush();
                session.close();
            }
        }
	    
	    return conhecimento;
	}

	private void criarAtualizarCarregamentoDescargaIntegracao(Filial filialRomaneio, ControleCarga controleCarga) {
	    CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findByControleCargaAndFilialRomaneio(controleCarga, filialRomaneio);
	    if (carregamentoDescarga == null) {
	        carregamentoDescarga = new CarregamentoDescarga();
	        carregamentoDescarga.setDhInicioOperacao(JTDateTimeUtils.getDataHoraAtual());
	        carregamentoDescarga.setDhFimOperacao(JTDateTimeUtils.getDataHoraAtual());
	        carregamentoDescarga.setControleCarga(controleCarga);
	        carregamentoDescarga.setFilial(filialRomaneio);
	        carregamentoDescarga.setTpOperacao(new DomainValue("D"));
	        carregamentoDescarga.setTpStatusOperacao(new DomainValue("F"));
	        carregamentoDescargaService.store(carregamentoDescarga);
	    }
	}
        
        public List<DocumentoEnvioAverbacao> generateDadosAverbacaoConhecimento() throws Exception {
            List<DocumentoEnvioAverbacao> listEnvio = new ArrayList<>();
            
            // Averbação de CT-e
            listEnvio.addAll(this.monitoramentoDocEletronicoService.generateDadosMonitoramentoDocEletronico());

            // Declaração de MDF-e
            listEnvio.addAll(this.averbacaoDoctoServicoMdfeService.generateDadosManifestoEletronico());
            
            return listEnvio;
        }
	
		public void storeDadosRetornoAverbacaoConhecimento(List<DocumentoRetornoAverbacao> retorno) {
			Map<Long, DocumentoRetornoAverbacao> ctes = new HashMap<Long, DocumentoRetornoAverbacao>();
			Map<Long, DocumentoRetornoAverbacao> mdfes = new HashMap<Long, DocumentoRetornoAverbacao>();

			preencherRetornoCtesEMdfes(retorno, ctes, mdfes);
			storeRetornoCtes(ctes);
			storeRetornoMdfes(mdfes);
		}

	private void preencherRetornoCtesEMdfes(List<DocumentoRetornoAverbacao> retorno, Map<Long, DocumentoRetornoAverbacao> ctes, Map<Long, DocumentoRetornoAverbacao> mdfes) {
            for (DocumentoRetornoAverbacao averbacao : retorno) {
			if (WEB_SERVICE_CTE_AVERBACAO.equals(averbacao.getWebService())) {
				ctes.put(Long.valueOf(averbacao.getIdentificador()), averbacao);
				continue;
			}
			if (WEB_SERVICE_MDFE_DECLARACAO.equals(averbacao.getWebService())) {
				mdfes.put(Long.valueOf(averbacao.getIdentificador()), averbacao);
				continue;
			}
				LOGGER.error("Webservice não mapeado: " + averbacao.getWebService());

            }
        }

		private void storeRetornoCtes(Map<Long, DocumentoRetornoAverbacao> ctes) {
			List<AverbacaoDoctoServico> ctesParaAverbar = averbacaoDoctoServicoService.findByIds(ctes.keySet());

			for (AverbacaoDoctoServico cte : ctesParaAverbar) {
				DocumentoRetornoAverbacao averbacao = ctes.get(cte.getIdAverbacaoDoctoServico());
				averbacaoDoctoServicoService.storeDadosRetornoAverbacaoCte(averbacao, cte);
			}
		}

		private void storeRetornoMdfes(Map<Long, DocumentoRetornoAverbacao> mdfes) {
			List<AverbacaoDoctoServicoMdfe> mdfesParaAverbar = averbacaoDoctoServicoMdfeService.findByIds(mdfes.keySet());

			for (AverbacaoDoctoServicoMdfe mdfe : mdfesParaAverbar) {
				DocumentoRetornoAverbacao averbacao = mdfes.get(mdfe.getIdAverbacaoDoctoServicoMdfe());
				averbacaoDoctoServicoMdfeService.storeDadosRetornoAverbacaoMdfe(averbacao, mdfe);
			}
		}

        public void generateAverbacoesSemRetornoParaEnvioEmail(List<DocumentoEnvioEmailAverbacao> listEnvio){
            String numMaxReenvio = parametroGeralService.findSimpleConteudoByNomeParametro("QTD_LIMITE_ENV_AVERBACAO");
            String rownumEnvio = parametroGeralService.findSimpleConteudoByNomeParametro("QTD_ROWNUM_ENV_AVERBACAO");
            List<AverbacaoDoctoServico> listAverb = averbacaoDoctoServicoService.findAverbacoesSemRetornoParaEnvioEmail(Integer.valueOf(numMaxReenvio) + 1, Integer.valueOf(rownumEnvio));
        
            for (AverbacaoDoctoServico averb : listAverb) {        	
                DocumentoEnvioEmailAverbacao docEnvioEmail = new DocumentoEnvioEmailAverbacao();        	
                try {
                    Conhecimento conhecimento = findById(averb.getDoctoServico().getIdDoctoServico()); 
                    docEnvioEmail.setNrConhecimento(conhecimento.getNrConhecimento().toString());
                    docEnvioEmail.setNrEnvio(numMaxReenvio);
                    docEnvioEmail.setDataEnvio(DataUtil.formatarData(averb.getDhEnvio()));
                    docEnvioEmail.setTempoAgRetorno(DataUtil.subtraiDataDataAtual(averb.getDhEnvio()).toString());
                    docEnvioEmail.setSgFilialOrigem(conhecimento.getFilialOrigem().getSgFilial());
                    averbacaoDoctoServicoService.updateDataEnvio(averb.getIdAverbacaoDoctoServico(), averb.getNrEnvio() +1);
                    listEnvio.add(docEnvioEmail);
                } catch (Exception e) {
                    LOGGER.error("Erro ao gerar dados para envio de E-mail das averbacoes pendentes de retorno.", e);
                }
            }
        }
        
        public void generateAverbacoesParaReenvio(List<DocumentoEnvioAverbacao> listEnvio){
            String numMaxReenvio = parametroGeralService.findSimpleConteudoByNomeParametro("QTD_LIMITE_ENV_AVERBACAO");
            String rownumReenvio = parametroGeralService.findSimpleConteudoByNomeParametro("QTD_ROWNUM_REENV_AVERBACAO");
            List<AverbacaoDoctoServico> listAverb = averbacaoDoctoServicoService.findAverbacoesParaReenvio(Integer.valueOf(numMaxReenvio), Integer.valueOf(rownumReenvio));
        
            for (AverbacaoDoctoServico averb : listAverb) {
                try {
                    Map<String, Object> dados = findConhecimentoAberbacao(averb.getDoctoServico().getIdDoctoServico());
                    if (MapUtils.isNotEmpty(dados)) {
                        MonitoramentoDocEletronico monitoramentoDocEletronico = monitoramentoDocEletronicoService.findMonitoramentoDocEletronicoByIdDoctoServico(averb.getDoctoServico().getIdDoctoServico());                   
                        DocumentoEnvioAverbacao documentoEnvioAverbacao = DocumentoEnvioAverbacaoHelper.createDocumentoEnvioAverbacao(averb.getIdAverbacaoDoctoServico(), averb.getTpWebservice(), dados);
                        if(averb.getTpDestino() == null){
                            averb.setTpDestino(TP_ATEM);
                        }
                        if (TP_ATEM.equals(averb.getTpDestino())) {
                            documentoEnvioAverbacao.setXml(new String(monitoramentoDocEletronico.getDsDadosDocumento(), Charset.forName("UTF-8")));
                        }
                        documentoEnvioAverbacao.setTpDestino(averb.getTpDestino());
                        listEnvio.add(documentoEnvioAverbacao);
                        
                        Integer nrEnvio = averb.getNrEnvio();
                        
                        if (nrEnvio == null) {
                            nrEnvio = 1;
                        } else {
                            nrEnvio++;
                        }
                        
                        averbacaoDoctoServicoService.updateDataEnvio(averb.getIdAverbacaoDoctoServico(), nrEnvio);
                    }
                } catch (Exception e) {
                    LOGGER.error("Erro ao gerar dados para reenvio averbweb", e);
                }
            }
        }
        
    public boolean validateDoctoServicoIsTpSubContratacaoIdDoctoServico(Long idDoctoServico) {
    	return getConhecimentoDAO().validateDoctoServicoIsTpSubContratacaoIdDoctoServico(idDoctoServico);
    }
    
    public List<Long> findConhecimentoToValidateLiberacaoConhecimentoReentrega(Long idDoctoServico) {
    	return getConhecimentoDAO().findConhecimentoToValidateLiberacaoConhecimentoReentrega(idDoctoServico);
    }
	
    public Map findBlProdutoPerigosoControladoByIdConhecimento(Long idConhecimento){
        return getConhecimentoDAO().findBlProdutoPerigosoControladoByIdConhecimento(idConhecimento);
	} 	public Boolean validateAgendamentoObrigatorio(Long idDoctoServico) {
		Conhecimento conhecimento = findByIdDoctoServico(idDoctoServico);
		return conhecimento.getBlObrigaAgendamento() != null ? conhecimento.getBlObrigaAgendamento() : Boolean.FALSE;
    }    
	
	public Boolean validateAgendamentoObrigatorioEsemAgendamentoDoctoServicoAtivo(Long idDoctoServico){
    	Boolean toReturn = Boolean.FALSE;
    	Conhecimento conhecimento = findByIdDoctoServico(idDoctoServico);
		Boolean agendamentoObrigatorio = conhecimento.getBlObrigaAgendamento() != null ? conhecimento.getBlObrigaAgendamento() : Boolean.FALSE;
		
		if (agendamentoObrigatorio){
			AgendamentoEntrega agEntrega = agendamentoEntregaService.findAgendamentoAbertoDoctoServico(idDoctoServico);
						
			if (agEntrega == null){
				toReturn = Boolean.TRUE;
				return toReturn;
			} else { 
					if(!"A".equals(agEntrega.getTpSituacaoAgendamento().getValue())){
						toReturn = Boolean.TRUE;
						return toReturn;
					}
				}
			}
		return toReturn;
	}

	public Conhecimento findConhecimentoByNrChave(String nrChave) {
		return getConhecimentoDAO().findConhecimentoByNrChave(nrChave);
	}
	
	public void setConfiguracoesFacadeImpl(ConfiguracoesFacadeImpl configuracoesFacadeImpl) {
		this.configuracoesFacadeImpl = configuracoesFacadeImpl;
	}

	public ConteudoParametroFilialService getConteudoParametroFilialService() {
		return conteudoParametroFilialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public DevedorDocServService getDevedorDocServService() {
		return devedorDocServService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public GerarConhecimentoEletronicoXMLService getGerarConhecimentoEletronicoXMLService() {
		return gerarConhecimentoEletronicoXMLService;
	}

	public void setGerarConhecimentoEletronicoXMLService(
			GerarConhecimentoEletronicoXMLService gerarConhecimentoEletronicoXMLService) {
		this.gerarConhecimentoEletronicoXMLService = gerarConhecimentoEletronicoXMLService;
	}

	public IntegracaoNDDigitalService getIntegracaoNDDigitalService() {
		return integracaoNDDigitalService;
	}

	public void setIntegracaoNDDigitalService(IntegracaoNDDigitalService integracaoNDDigitalService) {
		this.integracaoNDDigitalService = integracaoNDDigitalService;
	}

    public void setMunicipioService(MunicipioService municipioService) {
        this.municipioService = municipioService;
}

    public void setOcorrenciaDoctoServicoService(OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
        this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
    }

    public void setOcorrenciaPendenciaService(OcorrenciaPendenciaService ocorrenciaPendenciaService) {
        this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
    }

    public void setManifestoNacionalCtoService(ManifestoNacionalCtoService manifestoNacionalCtoService) {
        this.manifestoNacionalCtoService = manifestoNacionalCtoService;
    }

    public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
        this.carregamentoDescargaService = carregamentoDescargaService;
    }

    public void setEventoControleCargaService(EventoControleCargaService eventoControleCargaService) {
        this.eventoControleCargaService = eventoControleCargaService;
    }

    public void setManifestoEletronicoService(ManifestoEletronicoService manifestoEletronicoService) {
        this.manifestoEletronicoService = manifestoEletronicoService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
        this.volDadosSessaoService = volDadosSessaoService;
    }

    public void setManifestoService(ManifestoService manifestoService) {
        this.manifestoService = manifestoService;
    }

    public void setLocalizacaoMercadoriaService(LocalizacaoMercadoriaService localizacaoMercadoriaService) {
        this.localizacaoMercadoriaService = localizacaoMercadoriaService;
    }

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public AverbacaoDoctoServicoMdfeService getAverbacaoDoctoServicoMdfeService() {
        return averbacaoDoctoServicoMdfeService;
    }

    public void setAverbacaoDoctoServicoMdfeService(AverbacaoDoctoServicoMdfeService averbacaoDoctoServicoMdfeService) {
        this.averbacaoDoctoServicoMdfeService = averbacaoDoctoServicoMdfeService;
    }

    public AverbacaoDoctoServicoService getAverbacaoDoctoServicoService() {
        return averbacaoDoctoServicoService;
    }

    public void setAverbacaoDoctoServicoService(AverbacaoDoctoServicoService averbacaoDoctoServicoService) {
        this.averbacaoDoctoServicoService = averbacaoDoctoServicoService;
    }
    
    public AgendamentoDoctoServicoService getAgendamentoDoctoServicoService() {
		return agendamentoDoctoServicoService;
}

	public void setAgendamentoDoctoServicoService(
			AgendamentoDoctoServicoService agendamentoDoctoServicoService) {
		this.agendamentoDoctoServicoService = agendamentoDoctoServicoService;
	}
	
	public AgendamentoEntregaService getAgendamentoEntregaService() {
		return agendamentoEntregaService;
	}

	public void setAgendamentoEntregaService(AgendamentoEntregaService agendamentoEntregaService) {
		this.agendamentoEntregaService = agendamentoEntregaService;
	}
}
