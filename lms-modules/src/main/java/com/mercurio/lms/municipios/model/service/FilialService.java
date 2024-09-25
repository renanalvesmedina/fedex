package com.mercurio.lms.municipios.model.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.contasreceber.util.ConstantesAmbiente;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.ObjectNotFoundException;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ContatoService;
import com.mercurio.lms.configuracoes.model.service.EmpresaUsuarioService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.MoedaPaisService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contratacaoveiculos.model.PostoConveniado;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.PostoConveniadoService;
import com.mercurio.lms.municipios.ConstantesMunicipios;
import com.mercurio.lms.municipios.dto.FilialAtendimentoDto;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.AtendimentoFilial;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialServico;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.municipios.model.FotoFilial;
import com.mercurio.lms.municipios.model.GrupoClassificacaoFilial;
import com.mercurio.lms.municipios.model.HistoricoFilial;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.PostoAvancado;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;
import com.mercurio.lms.municipios.model.Regional;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.FilialDAO;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.tracking.Depot;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.PendenciaService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.filialService"
 */
public class FilialService extends CrudService<Filial, Long> {
	private static final String MAP_KEY_LIMITE_NFS_CTRC_IMPR = "LIMITE_NFS_CTRC_IMPR";
	private static final String MAP_KEY_LIMITE_NFS_CTRC_IMPRESSO = "LIMITE_NFS_CTRC_IMPRESSO";
	private static final String LMS_29128 = "LMS-29128";
	private static final String MAP_KEY_BL_LIMITA_NOTA_FISCAL_FORM = "blLimitaNotaFiscalForm";
	private static final String MAP_KEY_BL_SORTER = "blSorter";
	private static final String MAP_KEY_CODIGO_FILIAL = "codigoFilial";
	private static final String MAP_KEY_EMPRESA_ID_EMPRESA = "empresa.idEmpresa";
	private static final String MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO = "empresa.pessoa.nrIdentificacao";
	private static final String MAP_KEY_HR_CORTE = "hrCorte";
	private static final String MAP_KEY_ID_CONTATO = "idContato";
	private static final String MAP_KEY_ID_FILIAL = "idFilial";
	private static final String MAP_KEY_ID_PESSOA = "idPessoa";
	private static final String MAP_KEY_ID_POSTO_CONVENIADO = "idPostoConveniado";
	private static final String MAP_KEY_ID_USUARIO = "idUsuario";
	private static final String MAP_KEY_LAST_REGIONAL_DS_REGIONAL = "lastRegional.dsRegional";
	private static final String MAP_KEY_LAST_REGIONAL_SG_REGIONAL = "lastRegional.sgRegional";
	private static final String MAP_KEY_NR_FRANQUIA_PESO = "nrFranquiaPeso";
	private static final String MAP_KEY_NR_HR_COLETA = "nrHrColeta";
	private static final String MAP_KEY_NR_IDENTIFICACAO = "nrIdentificacao";
	private static final String MAP_KEY_PESSOA_ID_PESSOA = "pessoa.idPessoa";
	private static final String MAP_KEY_PESSOA_NR_IDENTIFICACAO = "pessoa.nrIdentificacao";
	private static final String MAP_KEY_PESSOA_TP_IDENTIFICACAO = "pessoa.tpIdentificacao";
	private static final String MAP_KEY_SG_FILIAL = "sgFilial";
	private static final String MAP_KEY_TP_ACESSO = "tpAcesso";
	private static final String MAP_KEY_TP_ORDEM_DOC = "tpOrdemDoc";
	private Logger log = LogManager.getLogger(this.getClass());
	private EnderecoPessoaService enderecoPessoaService;
	private PessoaService pessoaService;
	private RegionalFilialService regionalFilialService;
	private HistoricoFilialService historicoFilialService;
	private UsuarioService usuarioService;
	private ContatoService contatoService;
	private AcaoService acaoService;
	private WorkflowPendenciaService workflowPendenciaService;
	private MoedaPaisService moedaPaisService;
	private ConfiguracoesFacade configuracoesFacade;
	private EmpresaUsuarioService empresaUsuarioService;
	private InscricaoEstadualService inscricaoEstadualService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private EmpresaService empresaService;
	private MeioTransporteService meioTransporteService;
	private PendenciaService pendenciaService;
	private FluxoFilialService fluxoFilialService;
	private MunicipioFilialService municipioFilialService;
	private PostoAvancadoService postoAvancadoService;
	private PostoConveniadoService postoConveniadoService;
	private RegiaoGeograficaService regiaoGeograficaService;
	private UnidadeFederativaService unidadeFederativaService;
	private DomainValueService domainValueService;

	/**
	 * Método otimizado para acesso pelos aplicativos mobile.
	 * Não deve ser modificado/utilizado para outros fins.
	 * 
	 * @author RafaelKF
	 * @return
	 */
	public List<Depot> findDepot() {
		return getFilialDAO().findDepot();
	}
	
	public Depot findDepotByIdFilial(Long idFilial) {
		return getFilialDAO().findDepotByIdFilial(idFilial);
	}
	
	public List<Depot> findDepotByStateAndCity(TypedFlatMap tfm) {
		return getFilialDAO().findDepotByStateAndCity(tfm);
	}
	
	public List<Filial> findListFilial() {
		return getFilialDAO().findListFilial();
	}

	public void validateExisteCodFilial(Filial filial) {
		if(filial.getCodFilial() == null || filial.getCodFilial().compareTo(0) == 0) {
			throw new BusinessException("LMS-04296", new Object[]{filial.getSgFilial()});
		}
	}
	
	public boolean validateFilialCentroOesteNorteByNrIdentificacaoCliente(Long idUnidadeFederativa){
		UnidadeFederativa uf = unidadeFederativaService.findById(idUnidadeFederativa);
		if (uf != null){
			List check = regiaoGeograficaService.checkRegiaoCentroOesteNorteByIdUnidadeFederativa(uf);
			return (check != null && check.size() > 0);
		}
		
		return false;
	}
	
	public Map<String, Object> findForUpdate(Long idFilial) {
		Filial filial = get(idFilial);
		TypedFlatMap result = new TypedFlatMap();

		Empresa empresa = filial.getEmpresa();
		Pessoa pessoa = empresa.getPessoa();
		result.put("empresa.tpEmpresa", empresa.getTpEmpresa().getValue());
		result.put(MAP_KEY_EMPRESA_ID_EMPRESA, empresa.getIdEmpresa());
		result.put(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO, pessoa.getNrIdentificacao());
		result.put("empresa.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(),pessoa.getNrIdentificacao()));
		result.put("empresa.pessoa.nmPessoa", pessoa.getNmPessoa());

		pessoa = filial.getPessoa();
		if (pessoa.getTpIdentificacao() != null) {
			result.put(MAP_KEY_PESSOA_TP_IDENTIFICACAO, pessoa.getTpIdentificacao().getValue());
			if (pessoa.getNrIdentificacao() != null)
				result.put(MAP_KEY_PESSOA_NR_IDENTIFICACAO, FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(),pessoa.getNrIdentificacao()));
		}

		result.put(MAP_KEY_PESSOA_ID_PESSOA, idFilial);
		result.put("pessoa.dsEmail", pessoa.getDsEmail());
		result.put("pessoa.nmPessoa", pessoa.getNmPessoa());
		result.put("pessoa.nmFantasia", pessoa.getNmFantasia());
		result.put("pessoa.tpPessoa", pessoa.getTpPessoa().getValue());

		result.put(MAP_KEY_ID_FILIAL, idFilial);
		result.put(MAP_KEY_SG_FILIAL, filial.getSgFilial());
		result.put(MAP_KEY_HR_CORTE, filial.getHrCorte());
		result.put(MAP_KEY_CODIGO_FILIAL, filial.getCodFilial());
		result.put("dtImplantacaoLMS", filial.getDtImplantacaoLMS());
		result.put("obAprovacao", filial.getObAprovacao());
		result.put("dsHomepage", filial.getDsHomepage());
		result.put("obFilial", filial.getObFilial());
		result.put("nrAreaTotal", filial.getNrAreaTotal());
		result.put("nrAreaArmazenagem", filial.getNrAreaArmazenagem());
		result.put("blRecebeVeiculosSemColeta", filial.getBlRecebeVeiculosSemColeta());
		result.put("blInformaKmPortaria", filial.getBlInformaKmPortaria());
		result.put("blOrdenaEntregaValor", filial.getBlOrdenaEntregaValor());
		result.put("blObrigaBaixaEntregaOrdem", filial.getBlObrigaBaixaEntregaOrdem());
		result.put("blWorkflowKm", filial.getBlWorkflowKm());
		result.put(MAP_KEY_TP_ORDEM_DOC,filial.getTpOrdemDoc().getValue());
		result.put("blGeraContratacaoRetornoVazio", filial.getBlGeraContratacaoRetornoVazio());
		result.put("blValidaLocalVeiculo", filial.getBlValidaLocalVeiculo());
		result.put("blRncAutomaticaCarregamento", filial.getBlRncAutomaticaCarregamento());
		result.put("blRncAutomaticaDescarga", filial.getBlRncAutomaticaDescarga());
		result.put("blRncAutomaticaDescargaMww", filial.getBlRncAutomaticaDescargaMww());
		result.put("blRestringeCCVinculo", filial.getBlRestringeCCVinculo());
		result.put("nrDdr", filial.getNrDdr());
		result.put("nrDddAgenda", filial.getNrDddAgenda());
		result.put("nrTelefoneAgenda", filial.getNrTelefoneAgenda());
		result.put(MAP_KEY_NR_HR_COLETA, filial.getNrHrColeta());
		
		/* [CQ 27536]  */
		result.put("blColetorDadoScan", filial.getBlColetorDadoScan());
		
		/*JIRA 249 */
		result.put("blConfereDoctoDescarga", filial.getBlConfereDoctoDescarga());
		
		/*LMS-560*/
		result.put(MAP_KEY_BL_SORTER, filial.getBlSorter());
		
		result.put("blRestrEntrOutrasFiliais", filial.getBlRestrEntrOutrasFiliais());
		
		/* LMS-1239 */
		result.put(MAP_KEY_BL_LIMITA_NOTA_FISCAL_FORM, filial.getBlLimitaNotaFiscalForm());
		
		result.put("nrFranquiaKm", filial.getNrFranquiaKm());
		result.put(MAP_KEY_NR_FRANQUIA_PESO, filial.getNrFranquiaPeso());
		result.put("nrCentroCusto", filial.getNrCentroCusto());
		result.put("nrPrazoCobranca", filial.getNrPrazoCobranca());
		result.put("vlCustoReembarque", filial.getVlCustoReembarque());
		result.put("pcJuroDiario", filial.getPcJuroDiario());
		result.put("pcFreteCarreteiro", filial.getPcFreteCarreteiro());
		result.put("blEmiteBoletoFaturamento", filial.getBlEmiteBoletoFaturamento());
		result.put("blEmiteBoletoEntrega", filial.getBlEmiteBoletoEntrega());
		result.put("blEmiteReciboFrete", filial.getBlEmiteReciboFrete());
		result.put("blLiberaFobAereo", filial.getBlLiberaFobAereo());
		result.put("dsTimezone", filial.getDsTimezone());
		result.put(MAP_KEY_HR_CORTE,filial.getHrCorte());

		//LMS-3539
		result.put("blPagaDiariaExcedente", filial.getBlPagaDiariaExcedente());

		if (filial.getTpSistema() != null){
			result.put("tpSistema", filial.getTpSistema().getValue());			
		}

		Moeda moeda = filial.getMoeda();
		if (moeda != null)
			result.put("moeda.idMoeda", moeda.getIdMoeda());

		Aeroporto aeroporto = filial.getAeroporto();
		if (aeroporto != null) {
			result.put("aeroporto.idAeroporto", aeroporto.getIdAeroporto());
			result.put("aeroporto.sgAeroporto", aeroporto.getSgAeroporto());
			result.put("aeroporto.pessoa.nmPessoa", aeroporto.getPessoa().getNmPessoa());
		}

		PostoConveniado postoConveniado = filial.getPostoConveniado();
		if (postoConveniado != null) {
			result.put("postoConveniado.idPostoConveniado", postoConveniado.getIdPostoConveniado());
			result.put("postoConveniado.pessoa.nrIdentificacao", postoConveniado.getPessoa().getNrIdentificacao());
			result.put("postoConveniado.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(postoConveniado.getPessoa().getTpIdentificacao(), postoConveniado.getPessoa().getNrIdentificacao()));
			result.put("postoConveniado.pessoa.nmPessoa", postoConveniado.getPessoa().getNmPessoa());
		}

		Empresa franqueado = filial.getFranqueado();
		if (franqueado != null) {
			pessoa = franqueado.getPessoa();
			result.put("franqueado.idEmpresa", franqueado.getIdEmpresa());
			result.put("franqueado.pessoa.nrIdentificacao", pessoa.getNrIdentificacao());
			result.put("franqueado.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao()));
			result.put("franqueado.pessoa.nmPessoa", pessoa.getNmPessoa());
		}

		Filial filialResponsavel = filial.getFilialByIdFilialResponsavel();
		if (filialResponsavel != null) {
			result.put("filialByIdFilialResponsavel.idFilial", filialResponsavel.getIdFilial());
			result.put("filialByIdFilialResponsavel.sgFilial", filialResponsavel.getSgFilial());
			result.put("filialByIdFilialResponsavel.pessoa.nmFantasia", filialResponsavel.getPessoa().getNmFantasia());
		}

		Filial filialResponsavelAwb = filial.getFilialByIdFilialResponsavalAwb();
		if(filialResponsavelAwb != null) {
			result.put("filialByIdFilialResponsavalAwb.idFilial", filialResponsavelAwb.getIdFilial());
			result.put("filialByIdFilialResponsavalAwb.sgFilial", filialResponsavelAwb.getSgFilial());
			result.put("filialByIdFilialResponsavalAwb.pessoa.nmFantasia", filialResponsavelAwb.getPessoa().getNmFantasia());
		}

		if(filial.getCedenteByIdCedenteBloqueto() != null)
			result.put("cedenteByIdCedenteBloqueto.idCedente", filial.getCedenteByIdCedenteBloqueto().getIdCedente());

		if(filial.getCedenteByIdCedente() != null)
			result.put("cedenteByIdCedente.idCedente", filial.getCedenteByIdCedente().getIdCedente());

		if(filial.getPendencia() != null)
			result.put("pendencia.idPendencia", filial.getPendencia().getIdPendencia());

		Regional regional = regionalFilialService.findLastRegionalVigente(idFilial);
		if(regional != null) {
			result.put(MAP_KEY_LAST_REGIONAL_SG_REGIONAL, regional.getSgRegional());
			result.put(MAP_KEY_LAST_REGIONAL_DS_REGIONAL, regional.getDsRegional());
		}

		HistoricoFilial lastHistoricoFilial = historicoFilialService.findUltimoHistoricoFilial(idFilial);
		if(lastHistoricoFilial != null) {
			YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();

			result.put("lastHistoricoFilial.tpFilial", lastHistoricoFilial.getTpFilial().getValue());
			YearMonthDay dtRealOperacaoInicial = lastHistoricoFilial.getDtRealOperacaoInicial();
			result.put("lastHistoricoFilial.dtRealOperacaoInicial", dtRealOperacaoInicial);

			YearMonthDay dtPrevisaoOperacaoInicial = lastHistoricoFilial.getDtPrevisaoOperacaoInicial();
			result.put("lastHistoricoFilial.dtPrevisaoOperacaoInicial", dtPrevisaoOperacaoInicial);
			if( ( (dtPrevisaoOperacaoInicial == null) || (CompareUtils.gt(dtPrevisaoOperacaoInicial, dtToday)) )
					&& (dtRealOperacaoInicial == null)
			) {
				result.put("blDisablePrevisaoOperacaoInicial", Boolean.FALSE);
			} else {
				result.put("blDisablePrevisaoOperacaoInicial", Boolean.TRUE);
			}

			result.put("lastHistoricoFilial.dtPrevisaoOperacaoFinal", lastHistoricoFilial.getDtPrevisaoOperacaoFinal());

			YearMonthDay dtRealOperacaoFinal = lastHistoricoFilial.getDtRealOperacaoFinal();
			result.put("lastHistoricoFilial.dtRealOperacaoFinal", dtRealOperacaoFinal);
			if (dtRealOperacaoFinal != null) {
				result.put("blEnableReabrirFilial", Boolean.valueOf(CompareUtils.lt(dtRealOperacaoFinal, dtToday)).toString());
			} else {
				result.put("blEnableReabrirFilial", Boolean.FALSE.toString());
			}
		}

		List<Map<String, Object>> historicosFilial = new ArrayList<Map<String, Object>>();
		for(HistoricoFilial historicoFilial : filial.getHistoricoFiliais()) {
			Map<String, Object> historico = new HashMap<String, Object>();
			historico.put("idHistoricoFilial", historicoFilial.getIdHistoricoFilial());
			historico.put("dsTpFilial", historicoFilial.getTpFilial().getDescription().getValue());
			historico.put("dtPrevisaoOperacaoInicial", historicoFilial.getDtPrevisaoOperacaoInicial());
			historico.put("dtPrevisaoOperacaoFinal", historicoFilial.getDtPrevisaoOperacaoFinal());
			historico.put("dtRealOperacaoInicial", historicoFilial.getDtRealOperacaoInicial());
			historico.put("dtRealOperacaoFinal", historicoFilial.getDtRealOperacaoFinal());
			historicosFilial.add(historico);
		}
		result.put("historicosFilial", historicosFilial);

		result.put("meioTransporteProprioCount", meioTransporteService.getRowCountByType("P", idFilial));
		result.put("meioTransporteAgregadoCount", meioTransporteService.getRowCountByType("A", idFilial));

		if(filial.getFotoFiliais() != null && !filial.getFotoFiliais().isEmpty()) {
			result.put("imFilial", Base64Util.encode(((FotoFilial)filial.getFotoFiliais().get(0)).getImFilial()));
		}

		findContatoView(idFilial, "gerente", "GE", result, empresa.getTpEmpresa().getValue().equals("M"));
		findContatoView(idFilial, "comercial", "CC", result, empresa.getTpEmpresa().getValue().equals("M"));
		findContatoView(idFilial, "administrativo", "CA", result, empresa.getTpEmpresa().getValue().equals("M"));
		findContatoView(idFilial, "operacional", "CO", result, empresa.getTpEmpresa().getValue().equals("M"));

		result.put("numeroDocas", getRowCountDocasByFilial(idFilial));
		result.put("numeroBoxes", getRowCountBoxByFilial(idFilial));

		return result;
	}

	/**
     * Verifica se a filial de cobrança está com o LMS implantado na data atual
     * 
     * @param filial Filial de cobrança
     * @return <code>true</code> Se a filial fizer parte do LMS e <code>false</code> caso ainda não tenha sido implantado
     */
    public boolean lmsImplantadoFilial(Filial filial) {

    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	if( filial.getDtImplantacaoLMS() != null && (JTDateTimeUtils.comparaData(dataAtual,filial.getDtImplantacaoLMS()) >= 0 )){
    		return true;
    	}
		return false;
	}

	public boolean isFilial(Long idPessoa) {
		return getFilialDAO().isFilial(idPessoa);
	}

	/**
	 * Verifica se a filial esta vigente na data recebida por parâmetro.
	 * Método criado para customizar o retorno da validação de vigência.
	 * @param idFilial
	 * @param yearMonthDay
	 * @return
	 */
	public boolean isFilialVigente(Long idFilial, YearMonthDay yearMonthDay){
		try {
			this.verificaVigenciasEmHistoricoFilial(idFilial, yearMonthDay, null);
		} catch (BusinessException e) {
			return false;
		}
		return true;
	}

	//recebe duas datas de vigencia: inicial e final e verifica se as datas estão no intervalo de vigencia das datas reais em "Historico Filial"
	//caso nao estiverem nesse intervalo gera uma exceção	
	public void verificaVigenciasEmHistoricoFilial(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) throws BusinessException{
		if (!historicoFilialService.verificaVigenciasEmHistoricoFilial(idFilial,dtVigenciaInicial,dtVigenciaFinal))
			throw new BusinessException("LMS-29039"); 
	}

	/**
	 * recebe duas datas de vigencia: inicial e final e verifica se existem historicos filiais que abrangem a vigencia
	 * exemplo
	 * historico 1 : 01/01/2010 - 31/01/2010
	 * historico 1 : 01/02/2010 - 20/02/2010
	 * 
	 *   parametro 15/01/2010 - 15/02/2010 é válido
	 *   parametro 15/01/2010 - 21/02/2010 não é valido
	 * 
	 * @author DanielT
	 * @param idFilial
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @throws se nao estiverem nesse intervalo gera uma exceção
	 */
	public void verificaExistenciaHistoricoFilial(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) throws BusinessException{
		if (!historicoFilialService.verificaExistenciaHistoricoFilial(idFilial,dtVigenciaInicial,dtVigenciaFinal))
			throw new BusinessException("LMS-29039"); 
	}

	public void verificaVigenciasEmHistoricoFilial(Long idFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, String sigla) throws BusinessException{
		if (!historicoFilialService.verificaVigenciasEmHistoricoFilial(idFilial,dtVigenciaInicial,dtVigenciaFinal))
			throw new BusinessException("LMS-29086", new Object[]{sigla});
	}

	/**
	 * Recupera uma instância de <code>Filial</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public Filial findById(Long id) { 
		return (Filial)super.findById(id);
	} 
	public Filial findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (Filial)super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}
	
	/**
	 * Retorna se um cliente é uma filial 
	 *
	 * @param id de um cliente
	 * @return  
	 */
	public boolean findIsClienteFilial(Long id) { 
		try {
			Filial filial = findByIdInitLazyProperties(id, false);
		} catch (ObjectNotFoundException e) {
			return false;
		}
		return true;
	} 
	
	public Filial findByIdJoinPessoa(Long idFilial){
		return getFilialDAO().findByIdJoinPessoa(idFilial);
	}
	
	/**
	 * Busca uma entidade Filial a partir de seu id, sem fazer fetch em tabelas relacionadas.
	 * @param idFilial
	 * @return
	 */
	public Filial findByIdBasic(Long idFilial){
		return getFilialDAO().findByIdBasic(idFilial);
	}

	public String executeWorkflow(List<Long> idFilial, List<String> tpStituacao) {
		if (idFilial.size() != 1)
			throw new IllegalArgumentException("Você deve informar apenas uma filial");
		Long idF = idFilial.get(0);
		String tpStituacaoS = tpStituacao.get(0);
		Filial filial = findById(idF);

		if (tpStituacaoS.equalsIgnoreCase("A")) {
			HistoricoFilial historicoFilial = historicoFilialService.findUltimoHistoricoFilial(idF);

			if (historicoFilial.getDtPrevisaoOperacaoInicial() == null)
				throw new IllegalArgumentException("O Historico da filial não possui data Prevista de abertura, verificar se informou o id da filial correto");

			List<Contato> contatos = contatoService.findContatosByIdPessoa(idF);

			String key = historicoFilial.getTpFilial().getValue().equals("LO") ? "LMS-29162" : "LMS-29114";

			if (contatos == null || contatos.isEmpty()) {
				throw new BusinessException(key);
			} else if (enderecoPessoaService.findEnderecoPessoaPadrao(idF,historicoFilial.getDtPrevisaoOperacaoInicial()) == null) {
				throw new BusinessException(key);
			} else if (telefoneEnderecoService.findTelefoneEnderecoPadrao(idF) == null) {
				throw new BusinessException(key);
			} else if (inscricaoEstadualService.findByPessoa(idF).isEmpty() && !historicoFilial.getTpFilial().getValue().equals("LO")) {
				throw new BusinessException(key);
			}

			historicoFilial.setDtRealOperacaoInicial(historicoFilial.getDtPrevisaoOperacaoInicial());
			historicoFilialService.store(historicoFilial);

			StringBuffer sb = new StringBuffer(JTFormatUtils.format(historicoFilial.getDtPrevisaoOperacaoInicial()));

			Acao acao = getAcaoByFilial(idF,null,filial.getPendencia().getIdPendencia());
			if (acao != null && acao.getObAcao() != null)
				sb.append("\n").append(acao.getObAcao());
			if (filial.getObAprovacao() != null)
				sb.insert(0,"\n").insert(0,filial.getObAprovacao());
			filial.setObAprovacao(sb.toString());
			getFilialDAO().store(filial);
			HistoricoFilial historicoFilial2 = historicoFilialService.getPenultimoHistoricoFilial(idF);
			if (historicoFilial2 != null) {
				historicoFilial2.setDtRealOperacaoFinal(historicoFilial.getDtRealOperacaoInicial());
				historicoFilialService.store(historicoFilial2);
			}
			return configuracoesFacade.getMensagem("LMS-29113");
		} else if (tpStituacaoS.equalsIgnoreCase("R")) {
			HistoricoFilial historicoFilial = historicoFilialService.findUltimoHistoricoFilial(idF);

			YearMonthDay data = historicoFilial.getDtPrevisaoOperacaoInicial();

			Acao acao = getAcaoByFilial(idF,filial.getPendencia().getIdPendencia(),null);

			if (acao == null || data == null || acao.getDhLiberacao().toYearMonthDay().plusDays(3).compareTo(data) == 0)
				throw new BusinessException("LMS-29103");

			StringBuffer sb = new StringBuffer();
			sb.append(JTFormatUtils.format(acao.getDhLiberacao().plusDays(3).toYearMonthDay()));

			if (filial.getObAprovacao() != null)
				sb.insert(0,"\n").insert(0,filial.getObAprovacao());

			if (acao.getObAcao() != null)
				sb.append("\n").append(acao.getObAcao());
			filial.setObAprovacao(sb.toString());
			getFilialDAO().store(filial);
			return configuracoesFacade.getMensagem("LMS-29142");
		} else
			return null;
	}

	public void executeReabrirFilial(Long idFilial, DomainValue tpFilial, YearMonthDay dtPrevisaoOperacaoInicial) {
		if (JTDateTimeUtils.getDataAtual().compareTo(dtPrevisaoOperacaoInicial) > 0)
			throw new BusinessException(LMS_29128);

		Filial filial = (Filial)getFilialDAO().getAdsmHibernateTemplate().get(Filial.class,idFilial);
		Filial filialMatriz = historicoFilialService.findFilialMatriz(filial.getEmpresa().getIdEmpresa());

		HistoricoFilial historicoFilial = new HistoricoFilial();
		historicoFilial.setDtPrevisaoOperacaoInicial(dtPrevisaoOperacaoInicial);
		historicoFilial.setTpFilial(tpFilial);
		historicoFilial.setFilial(filial);

		getFilialDAO().getAdsmHibernateTemplate().save(historicoFilial);
		
		workflowPendenciaService.generatePendencia(filialMatriz.getIdFilial(), ConstantesWorkflow.NR2901_ABR_FILIAL,
			filial.getIdFilial(),
			(new StringBuffer(configuracoesFacade.getMensagem("aprovacaoAberturaFilial")))
			.append(" ").append(filial.getSgFilial()).append(" - ")
			.append(filial.getPessoa().getNmPessoa()).toString(),
			JTDateTimeUtils.yearMonthDayToDateTime(dtPrevisaoOperacaoInicial.minusDays(3))
		);
	}

	//FIXME Método deveria estar em AcaoService.
	public Acao getAcaoByFilial(Long idFilial, Long idPendenciaMaior, Long idPendenciaEquals) {
		if (idPendenciaEquals == null && idPendenciaMaior == null)
			throw new IllegalArgumentException("idPendenciaEquals and idPendenciaMaior cannot bee null.");

		List<Long> rs = getFilialDAO().getAcaoByFilial(idFilial,idPendenciaMaior,idPendenciaEquals);
		if (rs.size() > 0) {
			return acaoService.findById(rs.get(0));
		} else
			return null;
	}

	//FIXME Método deveria estar em AcaoService.
	public Acao getAcaoByFilial(Long idFilial, Long idPendenciaMaior) {
		return getAcaoByFilial(idFilial, idPendenciaMaior, Long.valueOf(1));
	}

	//FIXME Método deveria estar em MoedaPaisService.
	/**
	 * Pesquisa a moeda país da filial.
	 * @param idFilial
	 * @return MoedaPais ou null se endereçoPessoa da filial ou país não forem encontrados.
	 */
	public MoedaPais findMoedaPaisByIdFilial(Long idFilial) {
		// obtem a Filial
		Filial filial = (Filial)getFilialDAO().getAdsmHibernateTemplate().load(Filial.class,idFilial);

		Moeda moeda = filial.getMoeda();
		if (moeda == null)
			return null;

		// obtem o enderecopessoa padrao da filial
		EnderecoPessoa enderecoPessoa = this.enderecoPessoaService.findEnderecoPessoaPadrao(filial.getIdFilial());
		if (enderecoPessoa == null)
			return null;

		// obtem o pais via enderecopessoa
		Pais pais = enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais();
		// retorna a moeda padrao do pais
		return moedaPaisService.findByPaisAndMoeda(pais.getIdPais(),moeda.getIdMoeda());
	}

	public List findNmSgFilialByIdFilial(Long idFilial) {
		return getFilialDAO().findNmSgFilialById(idFilial);
	}

	public String findSgFilialByIdFilial(Long idFilial) {
		return getFilialDAO().findByIdBasic(idFilial).getSgFilial();
	}

	/**
	 * Retorna a filial indicada (com o list historicoFiliais), 
	 * caso esta possua apenas um histórico futuro 
	 * @param idFilial
	 * @return
	 * @author luisfco
	 */
	public Map<String, Object> findFilialComHistoricosFuturos(Long _idFilial) {
		List results = getFilialDAO().findFilialComHistoricosFuturos(_idFilial);
		Map<String, Object> map = new HashMap<String, Object>();

		if (results.size() > 0) {
			Object[] filial = (Object[])results.get(0);
			YearMonthDay date = (YearMonthDay)filial[0];
			Long idFilial = (Long)filial[1];
			String sgFilial = (String)filial[2];
			map.put("dtPrevisaoOperacaoInicial", JTFormatUtils.format(date));
			map.put(MAP_KEY_ID_FILIAL, idFilial);
			map.put(MAP_KEY_SG_FILIAL, sgFilial );
		}
		return map;
	}

	public void aprovarAberturaFilial(boolean aprovacao,Long idFilial,YearMonthDay data) {
		if (aprovacao) {
			HistoricoFilial hf = historicoFilialService.findUltimoHistoricoFilial(idFilial);
			hf.setDtRealOperacaoInicial(hf.getDtPrevisaoOperacaoInicial());
			getFilialDAO().getAdsmHibernateTemplate().update(hf);
			List<HistoricoFilial> rs = getFilialDAO().getAdsmHibernateTemplate().findByNamedParam("from com.mercurio.lms.municipios.model.HistoricoFilial where filial = :idFilial and id < :idHf ORDER BY id desc",new String[]{MAP_KEY_ID_FILIAL,"idHf"},new Object[]{idFilial,hf.getIdHistoricoFilial()});
			if (!rs.isEmpty()) {
				HistoricoFilial hf2 = rs.get(0);
				if (hf2.getDtRealOperacaoFinal() == null) {
					hf2.setDtRealOperacaoFinal(hf.getDtRealOperacaoInicial());
					getFilialDAO().getAdsmHibernateTemplate().update(hf2);
				}
			}
		} else {
			if (data != null) {
				HistoricoFilial hf = historicoFilialService.findUltimoHistoricoFilial(idFilial);
				hf.setDtPrevisaoOperacaoInicial(data);
				getFilialDAO().getAdsmHibernateTemplate().update(hf);
				List<HistoricoFilial> rs = getFilialDAO().getAdsmHibernateTemplate().findByNamedParam("from com.mercurio.lms.municipios.model.HistoricoFilial where filial = :idFilial and id < :idHf ORDER BY id desc",new String[]{MAP_KEY_ID_FILIAL,"idHf"},new Object[]{idFilial,hf.getIdHistoricoFilial()});
				if (!rs.isEmpty()){
					HistoricoFilial hf2 = rs.get(0);
					hf2.setDtPrevisaoOperacaoFinal(data);
					getFilialDAO().getAdsmHibernateTemplate().update(hf2);
				}
			}
		}
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		removeFilialById(id);
	}

	/**
	 * Método customizado em função da alteração da chamada na service.
	 * Tanto removeById, quanto RemoveByIds chamam este método.
	 * Remove a filial e tenta remover a pessoa.
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeFilialById(java.lang.Long id) {
		Filial filial = (Filial)getFilialDAO().getAdsmHibernateTemplate().load(Filial.class,id);
		filial.getHistoricoFiliais().clear();
		filial.getFotoFiliais().clear();
		if (filial.getPendencia() != null) {
			Long idPendencia = filial.getPendencia().getIdPendencia();
			Pendencia pendencia = pendenciaService.findById(idPendencia);
			if (pendencia.getTpSituacaoPendencia().getValue().equals("E")){
				workflowPendenciaService.cancelPendencia(pendencia);
		}
		}
		getFilialDAO().getAdsmHibernateTemplate().delete(filial);
		try {
			pessoaService.removeById(id);	
		} catch(Exception e) {
			//ignora de Pessoa
		}
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		for (Iterator<Long> iterIds = ids.iterator(); iterIds.hasNext();) {
			Long id = (Long) iterIds.next();
			removeFilialById(id);
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public void findContatoView(
			Long idPessoa,
			String baseName,
			String tpContato,
			Map<String, Object> map,
			boolean tpMercurio
	) {
		List<Contato> rs = getFilialDAO().findContato(idPessoa,tpContato,tpMercurio);
		if (rs.size() > 0) {
			Contato contato = rs.get(0);
			map.put(new StringBuffer(baseName).append(".idContato").toString(),contato.getIdContato());
			map.put(new StringBuffer(baseName).append(".nome").toString(),contato.getNmContato());
			if (contato.getUsuario() != null) {
				Usuario usuario = usuarioService.findById(contato.getUsuario().getIdUsuario());
				map.put(new StringBuffer(baseName).append(".idUsuario").toString(),usuario.getIdUsuario());
				map.put(new StringBuffer(baseName).append(".nrMatricula").toString(),usuario.getNrMatricula());
			}
		}
	}

	public List<Contato> findContato(Long idPessoa, String tpContato, boolean tpMercurio){
		return getFilialDAO().findContato(idPessoa,tpContato,tpMercurio);
	}

	private void updateContato(Map<String, Object> map, String tpEmpresa, String tpContato, Pessoa pessoa, Map<String, Object> newMap) {
		String nmContatoMap = selecionarContato(tpContato);

		Map<String, Object> contact = MapUtils.getMap(map, nmContatoMap);
		if(contact == null) {
			return;
		}

		boolean tpEmpresaMercurio = ConstantesMunicipios.TP_EMPRESA_MERCURIO.equals(tpEmpresa);

		String nmContato = MapUtils.getString(contact, "nome");
		Long idContato = MapUtils.getLong(contact, MAP_KEY_ID_CONTATO);
		
		if( (tpEmpresaMercurio && StringUtils.isBlank(MapUtils.getString(contact, MAP_KEY_ID_USUARIO)))
			|| StringUtils.isBlank(nmContato)
		) {
			return;
		}
		Long idUsuario = MapUtils.getLong(contact, MAP_KEY_ID_USUARIO);

		List<Contato> contatos = new ArrayList<Contato>();
		if (tpEmpresaMercurio && idUsuario != null) {
			contatos = contatoService.findContato(idUsuario, tpContato, pessoa.getIdPessoa());
		}
		if(contatos.isEmpty()) {
			Contato contato = prepareContato(tpContato, pessoa, tpEmpresaMercurio, nmContato, idContato, idUsuario);
			contatoService.store(contato);

			contact.put(MAP_KEY_ID_CONTATO, contato.getIdContato());
			newMap.put(nmContatoMap, contact);
		}
	}

	private Contato prepareContato(String tpContato, Pessoa pessoa, boolean tpEmpresaMercurio, String nmContato,
			Long idContato, Long idUsuario) {
		Contato contato;
		if(idContato == null) {
			contato = new Contato();
		} else {
			contato = contatoService.findById(idContato);
		}
		if(tpEmpresaMercurio && idUsuario != null) {
			contato.setUsuario(usuarioService.findById(idUsuario));
		}
		contato.setTpContato(new DomainValue(tpContato));
		contato.setNmContato(nmContato);
		contato.setPessoa(pessoa);
		return contato;
	}

	private String selecionarContato(String tpContato) {
		String nmMap = null;
		if("CC".equals(tpContato)) {
			nmMap = "comercial";
		} else if("CA".equals(tpContato)) {
			nmMap = "administrativo";
		} else if("CO".equals(tpContato)) {
			nmMap = "operacional";
		} else if("GE".equals(tpContato)) {
			nmMap = "gerente";
		}
		return nmMap;
	}

	public Map<String, Object> store(Map<String, Object> map) {
		Filial filial;
		Map pmap = (Map) map.get("postoConveniado");
		Long posto = (pmap.get(MAP_KEY_ID_POSTO_CONVENIADO).equals(""))?null:Long.valueOf((String)pmap.get(MAP_KEY_ID_POSTO_CONVENIADO));
		if (null != posto){
			Map mapa = postoConveniadoService.findDadosBancariosPostoConveniado(posto);
			if(mapa.get("nrContaBancaria") == null){
				throw new BusinessException("LMS-29179");
			}
		}
		
		if (StringUtils.isNotBlank((String)map.get(MAP_KEY_ID_FILIAL))) {
			filial = findByIdJoinPessoa(Long.valueOf((String)map.get(MAP_KEY_ID_FILIAL)));

			Map<String, Object> novaPessoa = (Map<String, Object>)map.get("pessoa"); 
			if (novaPessoa.get(MAP_KEY_NR_IDENTIFICACAO)!= null &&
				!((String)novaPessoa.get(MAP_KEY_NR_IDENTIFICACAO)).trim().equals("")) {
				
				String novoNrIdentificacao = (String)novaPessoa.get(MAP_KEY_NR_IDENTIFICACAO);
				
				// Se estiver sendo alterado o numero de identificacao (occore na troca de loja para parceira, filial, etc.)
				// Será alterado o objeto pessoa com os novos dados da tela (nrIdtificacao, etc.).
				if (!novoNrIdentificacao.equals(filial.getPessoa().getNrIdentificacao())) {
					novaPessoa.remove(MAP_KEY_ID_PESSOA);
 					novaPessoa.put(MAP_KEY_ID_PESSOA, filial.getPessoa().getIdPessoa());
				}			
			}
			
			filial.setNrHrColeta(null);
			
			if (map.get(MAP_KEY_NR_HR_COLETA)!=null && StringUtils.isNotBlank((String)map.get(MAP_KEY_NR_HR_COLETA))){
				if (Integer.parseInt(map.get(MAP_KEY_NR_HR_COLETA).toString()) > 0) {
					filial.setNrHrColeta(Integer.parseInt(map.get(MAP_KEY_NR_HR_COLETA).toString()));
				}
			}
				
			if (map.get(MAP_KEY_HR_CORTE)!=null && StringUtils.isNotBlank((String)map.get(MAP_KEY_HR_CORTE))){
				filial.setHrCorte(JTDateTimeUtils.convertDataStringToTimeOfDay((String)map.get(MAP_KEY_HR_CORTE)));
			}else{
				filial.setHrCorte(null);
			}

			if (map.get(MAP_KEY_HR_CORTE)!=null && StringUtils.isNotBlank((String)map.get(MAP_KEY_HR_CORTE))){
				filial.setHrCorte(JTDateTimeUtils.convertDataStringToTimeOfDay((String)map.get(MAP_KEY_HR_CORTE)));
			}else{
				filial.setHrCorte(null);
			}

			if (map.get(MAP_KEY_TP_ORDEM_DOC)!=null && StringUtils.isNotBlank((String)map.get(MAP_KEY_TP_ORDEM_DOC))){
				filial.setTpOrdemDoc(domainValueService.findDomainValueByValue("DM_TP_ORDEM_DOC", (String) map.get(MAP_KEY_TP_ORDEM_DOC)));
			}else{
				filial.setTpOrdemDoc(null);
			}
			
			getFilialDAO().getAdsmHibernateTemplate().save(filial);
			getFilialDAO().getAdsmHibernateTemplate().flush();
		} else {
			filial = new Filial();
		}

		TypedFlatMap mapNew = new TypedFlatMap();

		if (filial.getCdFilialFedex() != null){
		    map.put("cdFilialFedex", filial.getCdFilialFedex());
		}
		
		/** Copia todas as propriedades do mapa para o objeto Filial */
		if(filial.getIdFilial() == null){
		ReflectionUtils.copyNestedBean(filial, map);
		}else{
			com.mercurio.lms.util.LMSReflectionUtils.copyNestedBean(filial, map);
		}

		/** Converte entidades do mapa para filial, necessário para evitar erro do reflectionUtils */
		convertMapEntitiesToBean(map, filial);

		YearMonthDay dtImplantacaoLMS = MapUtilsPlus.getYearMonthDay(map, "dtImplantacaoLMS");
		filial.setDtImplantacaoLMS(dtImplantacaoLMS);

		Pessoa pessoa = filial.getPessoa();
		if(pessoa.getIdPessoa() == null && filial.getIdFilial() != null){
			pessoa.setIdPessoa(filial.getIdFilial()); 
		}

		getFilialDAO().getAdsmHibernateTemplate().evict(pessoa);
		
		pessoaService.store(filial);

		HistoricoFilial ultimoHistoricoFilial = filial.getLastHistoricoFilial();
		if (ultimoHistoricoFilial.getTpFilial().getValue().equals("MA")) {
			ultimoHistoricoFilial.setDtRealOperacaoInicial(ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial());
		}

		String tpEmpresa = filial.getEmpresa().getTpEmpresa().getValue();
		String tpFilial = null;

		if(filial != null && filial.getLastHistoricoFilial() != null && filial.getLastHistoricoFilial().getTpFilial() != null){
			tpFilial = filial.getLastHistoricoFilial().getTpFilial().getValue();
		}

		updateContato(map, tpEmpresa, "CC", pessoa, mapNew);
		updateContato(map, tpEmpresa, "CA", pessoa, mapNew);
		updateContato(map, tpEmpresa, "CO", pessoa, mapNew);
		updateContato(map, tpEmpresa, "GE", pessoa, mapNew);

		removeContatosExcluidos(map, tpEmpresa, pessoa, mapNew, tpFilial);

		HistoricoFilial hf = null;
		Pendencia pendencia = null;
		updateFoto(filial, (String)map.get("imFilial"));

		
		if (StringUtils.isNotBlank((String)map.get(MAP_KEY_CODIGO_FILIAL))) {			
			Integer codFilial = Integer.parseInt(map.get(MAP_KEY_CODIGO_FILIAL).toString());				
			Filial filialTmp = getFilialDAO().findFilialByCodigoFilial(codFilial);
			
			// Se idFilial != 0, então é um insert
			if (filial.getIdFilial() == null) {
				// Verifica se o código da filial a ser cadastrado já existe
				if (filialTmp != null)			
					throw new BusinessException("LMS-29177",new Object[]{configuracoesFacade.getMensagem(MAP_KEY_CODIGO_FILIAL)});
				else
					filial.setCodFilial(codFilial);
			} else { // Update
				// Verifica se o código da filial a ser cadastrado já existe
				if (filialTmp != null 
					&& filialTmp.getIdFilial() != filial.getIdFilial()	) {					
					throw new BusinessException("LMS-29177",new Object[]{configuracoesFacade.getMensagem(MAP_KEY_CODIGO_FILIAL)});
				} else {
					filial.setCodFilial(codFilial);
				}
			}
		}
		
		if (filial.getIdFilial() != null) {
			if (ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal() != null) {
				Object[][] validacoes = new Object[10][2];
				validacoes[0] = new Object[] {"horariosFuncionamento", AtendimentoFilial.class};
				validacoes[1] = new Object[] {"servicos", FilialServico.class};
				validacoes[2] = new Object[] {"gruposClassificacao", GrupoClassificacaoFilial.class};
				validacoes[3] = new Object[] {"ciasAereasUtilizadasFilial", CiaFilialMercurio.class};
				validacoes[4] = new Object[] {"postosAvancados", PostoAvancado.class};
				validacoes[5] = new Object[] {"regional", RegionalFilial.class};
				validacoes[6] = new Object[] {"regioesFilial", RegiaoColetaEntregaFil.class};
				validacoes[7] = new Object[] {"terminais", Terminal.class};
				validacoes[8] = new Object[] {"rotasColetaEntrega", RotaColetaEntrega.class};
				validacoes[9] = new Object[] {"municipiosAtendidos", MunicipioFilial.class};

				for(int x = 0; x < validacoes.length; x++) {
					Integer count = getFilialDAO().findEntidadeByIdFilial((Class)validacoes[x][1],filial.getIdFilial(),ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal());
					if (count.intValue() > 0)
						throw new BusinessException("LMS-29129",new Object[]{configuracoesFacade.getMensagem((String)validacoes[x][0])});
				}

				Integer count = getFilialDAO().findCepRotaColetaEntrega(filial.getIdFilial(),ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal());
				if (count.intValue() > 0)
					throw new BusinessException("LMS-29129",new Object[]{configuracoesFacade.getMensagem("cepRotaColetaEntrega")});
			}
			
			if(("M".equals(filial.getEmpresa().getTpEmpresa().getValue()) && "FI".equals(filial.getLastHistoricoFilial().getTpFilial())) && ((!map.get(MAP_KEY_NR_FRANQUIA_PESO).equals(""))?Integer.parseInt(map.get(MAP_KEY_NR_FRANQUIA_PESO).toString()):0) < 1){
				throw new BusinessException("LMS-29178");
			}
			
			
			/*
			 *A filial estar associada a um fluxo vigente (utilizar a tabela FLUXO_FILIAL):
			 *	Mostrar a mensagem LMS-29042 e abortar a operação.
			 *A filial estará associada ao atendimento de um município (utilizar a tabela MUNICIPIO_FILIAL):
			 *	Mostrar a mensagem LMS-29043 e abortar a operação.
			 *A filial possuir ao menos um posto avançado vigente (utilizar a tabela POSTO_AVANCADO):
			 *	Mostrar a mensagem LMS-29044 e abortar a operação.
			 */
			if(filial.getLastHistoricoFilial().getDtRealOperacaoFinal() != null){
				if(!fluxoFilialService.findFluxoFilialByFilial(filial.getIdFilial(), JTDateTimeUtils.getDataAtual()).isEmpty()){
					throw new BusinessException("LMS-29042");
				}
				
				if(!municipioFilialService.findMunicipioFilialVigenteByFilial(filial.getIdFilial()).isEmpty()){
					throw new BusinessException("LMS-29043");
				}
				
				if(!postoAvancadoService.findPostoAvancadoByFilial(filial.getIdFilial(), JTDateTimeUtils.getDataAtual()).isEmpty()){
					throw new BusinessException("LMS-29043");
				}
			}
			/**
			 * atualiza blSorter
			 */
			if(StringUtils.isNotBlank((String)map.get(MAP_KEY_BL_SORTER))){
				filial.setBlSorter(MapUtils.getBoolean(map, MAP_KEY_BL_SORTER));
			}
			/* LMS-1239 */
			if (StringUtils.isNotBlank((String) map.get(MAP_KEY_BL_LIMITA_NOTA_FISCAL_FORM))) {
				filial.setBlLimitaNotaFiscalForm(MapUtils.getBoolean(map, MAP_KEY_BL_LIMITA_NOTA_FISCAL_FORM));
			}
			getFilialDAO().store(filial);

			if (!"P".equals(tpEmpresa)) {
				hf = historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial());
				if (hf != null && !ultimoHistoricoFilial.getTpFilial().getValue().equals(hf.getTpFilial().getValue())) {
					hf.setDtPrevisaoOperacaoFinal(ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial());
					historicoFilialService.store(hf);
					hf = ultimoHistoricoFilial;
					pendencia = geraWorkFlowAbertura(filial, hf);
				} else {
					if (ultimoHistoricoFilial.getDtRealOperacaoInicial() != null && ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal() != null &&
							(hf != null && ((hf.getDtPrevisaoOperacaoFinal() != null && !hf.getDtPrevisaoOperacaoFinal().equals(ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal())) || hf.getDtPrevisaoOperacaoFinal() == null))) {
						
						hf.setDtPrevisaoOperacaoFinal(ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal());
						hf.setDtPrevisaoOperacaoInicial(ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial());
						//AQUI GERA O WORKFLOW DE FECHAMENTO
						pendencia = geraWorkFechamento(filial,hf);
					} else {
						boolean flag = (!ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial().equals(hf.getDtPrevisaoOperacaoInicial()));
						if (flag) {
							HistoricoFilial hfP = historicoFilialService.getPenultimoHistoricoFilial(filial.getIdFilial());
							if (hfP != null) {
								hfP.setDtPrevisaoOperacaoFinal(ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial());
								historicoFilialService.store(hfP);
							}
						}
						hf.setDtPrevisaoOperacaoFinal(ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal());
						hf.setDtPrevisaoOperacaoInicial(ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial());
						if (flag) {
							pendencia = geraWorkFlowAbertura(filial,hf);
						}
					}
				}
			} else {
				hf = (HistoricoFilial)filial.getHistoricoFiliais().get(0);
				hf.setTpFilial(ultimoHistoricoFilial.getTpFilial());
				hf.setDtRealOperacaoInicial(ultimoHistoricoFilial.getDtRealOperacaoInicial());
				hf.setDtRealOperacaoFinal(ultimoHistoricoFilial.getDtRealOperacaoFinal());
				
			}
		} else {
			filial.setPessoa(pessoa);
			getFilialDAO().store(filial);
			hf = ultimoHistoricoFilial;
			if("M".equals(tpEmpresa))
				pendencia = geraWorkFlowAbertura(filial, hf);
		}
		if (pendencia != null) {
			filial.setPendencia(pendencia);
			mapNew.put("pendencia.idPendencia",pendencia.getIdPendencia());
		}
		getFilialDAO().store(filial);
		if (hf != null) {
			hf.setFilial(filial);
			historicoFilialService.store(hf);
		}
		getFilialDAO().getAdsmHibernateTemplate().flush();
		mapNew.put("obAprovacao",filial.getObAprovacao());
		mapNew.put(MAP_KEY_ID_FILIAL,filial.getIdFilial());
		mapNew.put(MAP_KEY_PESSOA_ID_PESSOA,filial.getPessoa().getIdPessoa());
		mapNew.put("lastHistoricoFilial.dtPrevistaOperacaoInicial", ultimoHistoricoFilial.getDtPrevisaoOperacaoInicial());
		mapNew.put("lastHistoricoFilial.dtPrevistaOperacaoFinal", ultimoHistoricoFilial.getDtPrevisaoOperacaoFinal());
		return mapNew;
	}

	private void removeContatosExcluidos(Map<String, Object> map, String tpEmpresa, Pessoa pessoa, TypedFlatMap mapNew,
			String tpFilial) {
		removeContato(map, tpEmpresa, "CC", pessoa, mapNew, tpFilial);
		removeContato(map, tpEmpresa, "CA", pessoa, mapNew, tpFilial);
		removeContato(map, tpEmpresa, "CO", pessoa, mapNew, tpFilial);
		removeContato(map, tpEmpresa, "GE", pessoa, mapNew, tpFilial);
		
	}
	
	@SuppressWarnings("unchecked")
	private void removeContato(Map<String, Object> map, String tpEmpresa, String tpContato, Pessoa pessoa, TypedFlatMap newMap, String tpFilial) {
		String nmMap = selecionarContato(tpContato);

		Map<String, Object> contact = MapUtils.getMap(map, nmMap);
		if(contact == null) {
			return;
		}

		boolean tpEmpresaMercurio = ConstantesMunicipios.TP_EMPRESA_MERCURIO.equals(tpEmpresa);
		
		String nmContato = MapUtils.getString(contact, "nome");
		Long idContato = MapUtils.getLong(contact, MAP_KEY_ID_CONTATO);
		
		if( (tpEmpresaMercurio && StringUtils.isBlank(MapUtils.getString(contact, MAP_KEY_ID_USUARIO)))
			|| StringUtils.isBlank(nmContato)
		) {
			
			if(idContato != null){
				
				Contato contato = contatoService.findContato(idContato, pessoa.getIdPessoa(), tpContato);
				if( contato != null){
					if(isContatoSemTabelaVinculada(contato) ){
						HistoricoFilial historicoFilial = historicoFilialService.findUltimoHistoricoFilial(MapUtils.getLong(map, MAP_KEY_ID_FILIAL));
						if(historicoFilial != null && historicoFilial.getTpFilial() != null && tpFilial != null 
								&& !historicoFilial.getTpFilial().getValue().equalsIgnoreCase(tpFilial)){
							contatoService.removeSemValidacaoById( idContato );
						}else{
							contatoService.removeById( idContato );
						}
						
						//Para remover idContato da tela
						contact.put(MAP_KEY_ID_CONTATO, "");
					}else{
						if(StringUtils.isBlank((String)contact.get("nome"))){
							contact.put("nome", contato.getNmContato());
							if(contato.getUsuario() != null ){
								contact.put("nrMatricula", contato.getUsuario().getNrMatricula());
							}
						}
					}
					
					newMap.put(nmMap, contact);
				}
			}
		}
	}

	private boolean isContatoSemTabelaVinculada(Contato contato) {
		
		if( ( contato.getAgendaCobrancas() != null && !contato.getAgendaCobrancas().isEmpty() )
				|| ( contato.getContatoComunicacoes() != null && !contato.getContatoComunicacoes().isEmpty() ) 
				|| ( contato.getContatoCorrespondencias() != null && !contato.getContatoCorrespondencias().isEmpty() )
				|| ( contato.getTelefoneContatos() != null && !contato.getTelefoneContatos().isEmpty() )
				|| ( contato.getVersaoContatoPces() != null && !contato.getVersaoContatoPces().isEmpty() )
				|| ( contato.getVolFabricantes() != null && !contato.getVolFabricantes().isEmpty() ) 
				|| ( contato.getVolOperadorasTelefonias() != null && !contato.getVolOperadorasTelefonias().isEmpty() )
			){
			return false;
		}
		
		return true;
	}

	/**
	 * Converte entidades do mapa para filial, necessário para evitar erro do reflectionUtils
	 * @param map
	 * @param filial
	 */
	private void convertMapEntitiesToBean(Map<String, Object> map, Filial filial) {
		Long idMoeda = MapUtilsPlus.getLongOnMap(map, "moeda", "idMoeda");
		if (idMoeda != null) {
			Moeda moeda = new Moeda();
			moeda.setIdMoeda(idMoeda);
			filial.setMoeda(moeda);
		} else filial.setMoeda(null);

		Long idAeroporto = MapUtilsPlus.getLongOnMap(map, "aeroporto", "idAeroporto");
		if (idAeroporto != null) {
			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(idAeroporto);
			filial.setAeroporto(aeroporto);
		} else filial.setAeroporto(null);

		Long idPostoConveniado = MapUtilsPlus.getLongOnMap(map, "postoConveniado", MAP_KEY_ID_POSTO_CONVENIADO);
		if (idPostoConveniado != null) {
			PostoConveniado postoConveniado = new PostoConveniado();
			postoConveniado.setIdPostoConveniado(idPostoConveniado);
			filial.setPostoConveniado(postoConveniado);
		} else filial.setPostoConveniado(null);

		Long idFilialResponsavelAwb = MapUtilsPlus.getLongOnMap(map, "filialByIdFilialResponsavalAwb", MAP_KEY_ID_FILIAL);
		if (idFilialResponsavelAwb != null) {
			Filial filialResponsavelAwb = new Filial();
			filialResponsavelAwb.setIdFilial(idFilialResponsavelAwb);
			filial.setFilialByIdFilialResponsavalAwb(filialResponsavelAwb);
		} else filial.setFilialByIdFilialResponsavalAwb(null);

		Long idFilialResponsavel = MapUtilsPlus.getLongOnMap(map, "filialByIdFilialResponsavel", MAP_KEY_ID_FILIAL);
		if (idFilialResponsavel != null) {
			Filial filialResponsavel = new Filial();
			filialResponsavel.setIdFilial(idFilialResponsavel);
			filial.setFilialByIdFilialResponsavel(filialResponsavel);
		} else filial.setFilialByIdFilialResponsavel(null);

		Long idEmpresa = MapUtilsPlus.getLongOnMap(map, "empresa", "idEmpresa");
		if (idEmpresa != null) {
			filial.setEmpresa(empresaService.findById(idEmpresa));
		} else filial.setEmpresa(null);

		Long idCedente = MapUtilsPlus.getLongOnMap(map, "cedenteByIdCedente", "idCedente");
		if (idCedente != null) {
			Cedente cedente = new Cedente();
			cedente.setIdCedente(idCedente);
			filial.setCedenteByIdCedente(cedente);
		} else filial.setCedenteByIdCedente(null);

		Long idCedenteBloqueto = MapUtilsPlus.getLongOnMap(map, "cedenteByIdCedenteBloqueto", "idCedente");
		if (idCedenteBloqueto != null) {
			Cedente cedente = new Cedente();
			cedente.setIdCedente(idCedenteBloqueto);
			filial.setCedenteByIdCedenteBloqueto(cedente);
		} else filial.setCedenteByIdCedenteBloqueto(null);
	}

	private void updateFoto(Filial bean, String imFilial) {
		if (StringUtils.isNotBlank(imFilial)) {
			FotoFilial fotoFilial = new FotoFilial();
			fotoFilial.setFilial(bean);
			try {
				fotoFilial.setImFilial(Base64Util.decode(imFilial));
			} catch (IOException e) {
				log.error(e);
			}
			if (bean.getFotoFiliais() != null) {
				if (bean.getFotoFiliais().isEmpty())
					bean.getFotoFiliais().add(fotoFilial);
				else
					bean.getFotoFiliais().set(0,fotoFilial);
			} else {
				List<FotoFilial> fotosFilial = new ArrayList<FotoFilial>();
				fotosFilial.add(fotoFilial);
				bean.setFotoFiliais(fotosFilial);
			}
		} else {
			if (bean.getFotoFiliais() != null)
				bean.getFotoFiliais().clear();
			if (bean.getIdFilial() != null)
				getFilialDAO().deteleFotosFilial(bean.getIdFilial());
		}
	}

	private Pendencia geraWorkFlowAbertura(Filial filial, HistoricoFilial historicoFilial) {
		if(historicoFilial.getTpFilial().getValue().charAt(0) != 'M') {
			YearMonthDay dtPrevisaoOperacaoInicial = historicoFilial.getDtPrevisaoOperacaoInicial();
			if(CompareUtils.lt(dtPrevisaoOperacaoInicial, JTDateTimeUtils.getDataAtual()))
				throw new BusinessException(LMS_29128);

			dtPrevisaoOperacaoInicial = dtPrevisaoOperacaoInicial.minusDays(3);

			Filial filialMatriz = historicoFilialService.findFilialMatriz(filial.getEmpresa().getIdEmpresa());
			if (filialMatriz == null)
				throw new BusinessException("LMS-29107");

			if (filial.getIdFilial() != null) {
				Filial f2 = (Filial)getFilialDAO().getAdsmHibernateTemplate().load(Filial.class,filial.getIdFilial());
				if (f2.getPendencia() != null) {
					Long idPendencia = f2.getPendencia().getIdPendencia();
					Pendencia pendencia = pendenciaService.findById(idPendencia);
					if (pendencia.getTpSituacaoPendencia().getValue().equals("E")){
						workflowPendenciaService.cancelPendencia(pendencia);
			}
				}
			}

			StringBuilder dsProcesso = new StringBuilder();
			dsProcesso.append(configuracoesFacade.getMensagem("aprovacaoAberturaFilial"))
			.append(" ")
			.append(filial.getSgFilial())
			.append(" - ").
			append(filial.getPessoa().getNmPessoa());

			return workflowPendenciaService.generatePendencia(
				filialMatriz.getIdFilial(),
				ConstantesWorkflow.NR2901_ABR_FILIAL,
				((filial.getIdFilial() == null) ? filialMatriz.getIdFilial() : filial.getIdFilial()),
				dsProcesso.toString(),
				JTDateTimeUtils.yearMonthDayToDateTime(dtPrevisaoOperacaoInicial)
			);
		} else {
			historicoFilial.setDtRealOperacaoInicial(historicoFilial.getDtPrevisaoOperacaoInicial());
			return null;
		}
	}

	private Pendencia geraWorkFechamento(Filial filial,HistoricoFilial hf) {
		if (hf.getTpFilial().getValue().charAt(0) != 'M') {
			YearMonthDay data = hf.getDtPrevisaoOperacaoFinal();
			if (data.compareTo(JTDateTimeUtils.getDataAtual()) < 0)
				throw new BusinessException(LMS_29128);

			Filial filialMatriz = historicoFilialService.findFilialMatriz(filial.getEmpresa().getIdEmpresa());
			if (filialMatriz == null)
				throw new BusinessException("LMS-29107");

			data = data.minusDays(3);
			StringBuilder dsProcesso = new StringBuilder();
			dsProcesso.append(configuracoesFacade.getMensagem("aprovacaoFechamentoFilial"))
			.append(" ")
			.append(filial.getSgFilial())
			.append(" - ")
			.append(filial.getPessoa().getNmPessoa()).toString();

			return workflowPendenciaService.generatePendencia(
				filialMatriz.getIdFilial(),
				ConstantesWorkflow.NR2903_FEC_FILIAL,
				filial.getIdFilial(),
				dsProcesso.toString(),
				JTDateTimeUtils.yearMonthDayToDateTime(data)
			);
		} else {
			hf.setDtRealOperacaoFinal(hf.getDtPrevisaoOperacaoFinal());
			return null;
		}
	}

	protected Filial beforeStore(Filial bean) {
		Filial filial = (Filial)bean;
		HistoricoFilial lastHistoricoFilial = filial.getLastHistoricoFilial();
		if (lastHistoricoFilial != null && lastHistoricoFilial.getTpFilial() != null && lastHistoricoFilial.getTpFilial().getValue().equals("MA")) {
			List<HistoricoFilial> rs = historicoFilialService.findHistoricosFilialByEmpresa(filial.getEmpresa().getIdEmpresa(), "MA");
			if (rs != null && rs.size() > 0 && !rs.get(0).getFilial().getIdFilial().equals(((Filial)bean).getIdFilial())) {
				throw new BusinessException("LMS-29001");
			}
		}
		if (lastHistoricoFilial.getTpFilial().getValue().equals("PA") && filial.getEmpresa().getTpEmpresa().getValue().equals("M"))
			throw new BusinessException("LMS-29125");
		return super.beforeStore(bean);
	}

	/**
	 * Função chamada pelo xmit, na tela filial cad, para verificar se ja existe nrIdentificacao e tpIdentificacao em alguma pessoa, e se essa pessoa esta vinculada a alguma filial
	 * @author Samuel Herrmann
	 * @param map (campos do pojo Pessoa)
	 * @return Map com o pojo de Pessoa e mais os campos nrInscricaoEstadual e idInscricaoEstadual
	 */
	public List<TypedFlatMap> validateIdentificacao(Map<String, Object> criteria) {
		List<Pessoa> rs = pessoaService.findLookup(criteria);

		if (rs.size() == 1) {
			Pessoa pessoa = rs.get(0);
			if (configuracoesFacade.getPessoa(pessoa.getIdPessoa(),Filial.class,false) != null)
				throw new BusinessException("LMS-29000");

			TypedFlatMap result = new TypedFlatMap();
			result.put("nmPessoa",pessoa.getNmPessoa());
			result.put("nmFantasia",pessoa.getNmFantasia());
			result.put("dsEmail",pessoa.getDsEmail());
			result.put(MAP_KEY_NR_IDENTIFICACAO,FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(),pessoa.getNrIdentificacao()));
			result.put(MAP_KEY_ID_PESSOA,pessoa.getIdPessoa());

			List<TypedFlatMap> list = new ArrayList<TypedFlatMap>();
			list.add(result);

			return list;
		} else
			return null;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setFilialDAO(FilialDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private FilialDAO getFilialDAO() {
		return (FilialDAO) getDao();
	}

	public List<Filial> findFilialByArrayNrIdentificacaoPessoa(List<String> nrIdentificacao) {
		return getFilialDAO().findFilialByArrayNrIdentificacaoPessoa(nrIdentificacao);
	}

	public List<Map<String, Object>> findFilialByMatriculaUsuario(String nrMatricula) {
		return this.getFilialDAO().findFilialByMatriculaUsuario(nrMatricula);
	}

	/**
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {		
		ResultSetPage rsPage = getFilialDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
		List<TypedFlatMap> newList = new ArrayList<TypedFlatMap>();

		for(Iterator<Map<String, Object>> ie = rsPage.getList().iterator(); ie.hasNext();) {
			Map<String, Object> result = (Map<String, Object>)ie.next();
			TypedFlatMap newResult = new TypedFlatMap();
			for (Iterator<String> ie2 = result.keySet().iterator(); ie2.hasNext();) {
				String key = (String)ie2.next();
				newResult.put(key.replace('_','.'),result.get(key));
			}
			if (StringUtils.isNotBlank(newResult.getString(MAP_KEY_PESSOA_NR_IDENTIFICACAO)))
				newResult.put(MAP_KEY_PESSOA_NR_IDENTIFICACAO,FormatUtils.formatIdentificacao(newResult.getDomainValue(MAP_KEY_PESSOA_TP_IDENTIFICACAO).getValue(),newResult.getString(MAP_KEY_PESSOA_NR_IDENTIFICACAO)));

			if (StringUtils.isNotBlank(newResult.getString(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO)))
				newResult.put(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO,FormatUtils.formatIdentificacao(newResult.getDomainValue("empresa.pessoa.tpIdentificacao").getValue(),newResult.getString(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO)));

			if("S".equals(criteria.getString("findEndereco"))){
				fillEndereco(newResult);				
			}
			
			newList.add(newResult);
			Regional regional = regionalFilialService.findLastRegionalVigente(newResult.getLong(MAP_KEY_ID_FILIAL));
			if (regional != null) {
				newResult.put(MAP_KEY_LAST_REGIONAL_DS_REGIONAL,regional.getDsRegional());
				newResult.put("lastRegional.idRegional",regional.getIdRegional());
				newResult.put(MAP_KEY_LAST_REGIONAL_SG_REGIONAL,regional.getSgRegional());
			}
		}
		rsPage.setList(newList);
		return rsPage;
	}

	private void fillEndereco(TypedFlatMap newResult) {
		EnderecoPessoa ep = enderecoPessoaService.findByIdPessoa(newResult.getLong(MAP_KEY_PESSOA_ID_PESSOA));
		
		
		UnidadeFederativa uf = ep.getMunicipio().getUnidadeFederativa();
		Pais pais = uf.getPais();
		
		newResult.put("endereco.municipio.unidadeFederativa.pais.idPais", pais.getIdPais());
		newResult.put("endereco.municipio.unidadeFederativa.pais.nmPais", pais.getNmPais());
		newResult.put("endereco.municipio.unidadeFederativa.idUnidadeFederativa", uf.getIdUnidadeFederativa());
		newResult.put("endereco.municipio.unidadeFederativa.sgUnidadeFederativa", uf.getSgUnidadeFederativa());
		newResult.put("endereco.municipio.unidadeFederativa.nmUnidadeFederativa", uf.getNmUnidadeFederativa());
	}

	public Integer getRowCountDocasByFilial(Long idFilial) {
		return getFilialDAO().getRowCountDocasByFilial(idFilial);
	}

	public Integer getRowCountBoxByFilial(Long idFilial) {
		return getFilialDAO().getRowCountBoxByFilial(idFilial);
	}

	public Filial findFilialUsuarioLogado() {
		return getFilialUsuarioLogado();
	}

	public Filial getFilialUsuarioLogado() {
		Filial f = (Filial)SessionContext.get(SessionKey.FILIAL_KEY);
		return findById(f.getIdFilial());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getFilialDAO().getRowCountFilial(criteria);
	}

	/**
	 * 
	 * @param rs
	 * @return List
	 */
	private List formataRetorno(List rs) {
		for(int x = 0; x < rs.size(); x++) {
			Map<String, Object> oldMap = (Map<String, Object>)rs.get(x);
			Set<String> set = oldMap.keySet();
			Map<String, Object> newMap = new HashMap<String, Object>();

			oldMap.put("empresa_pessoa_nrIdentificacao",
						FormatUtils.formatIdentificacao(
													((DomainValue)oldMap.get("empresa_pessoa_tpIdentificacao")).getValue(),
													(String)oldMap.get("empresa_pessoa_nrIdentificacao")
						)
			);

			for(String key : set) {				
				ReflectionUtils.setNestedBeanPropertyValue(newMap,key.replace('_','.'),ReflectionUtils.getNestedBeanPropertyValue(oldMap,key));
			}

			Regional regional = regionalFilialService.findLastRegionalVigente((Long)newMap.get(MAP_KEY_ID_FILIAL));
			if (regional != null) {
				Map<String, Object> lastRegional = new HashMap<String, Object>();
				lastRegional.put("dsRegional",regional.getDsRegional());
				lastRegional.put("idRegional",regional.getIdRegional());
				lastRegional.put("sgNmFull", new StringBuffer(regional.getSgRegional()).append(" - ").append(regional.getDsRegional()).toString());
				lastRegional.put("sgRegional",regional.getSgRegional());
				newMap.put("lastRegional",lastRegional);
			}
			rs.set(x,newMap);
			if (x == 1)
				break;
		}
		return rs;		
	}

	public Filial findFilialPessoaBySgFilial(String sgFilial) {
		return findFilialPessoaBySgFilial(sgFilial,Boolean.FALSE);
	}

	public Filial findFilialPessoaBySgFilial(String sgFilial, Boolean isMercurio) {
		return getFilialDAO().findFilialPessoaBySgFilial(sgFilial,isMercurio);
	}

	/**
	 * Retorna a filial de cobrança do cliente informado.
	 * 
	 * @param Long idCliente
	 * @return Filial
	 * */	
	public Filial findFilialCobrancaByCliente(Long idCliente){
		List<Filial> result = getFilialDAO().findFilialCobrancaByCliente(idCliente);
		if (result.size() == 1) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna a filial centralizadora da filial informada por tpModal e tpAbrangencia.
	 * 
	 * @param Long idFilial
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * @return Filial
	 * */	
	public Filial findFilialCentralizadoraByFilial(Long idFilialCentralizada, String tpModal, String tpAbrangencia){
		return findFilialCentralizadoraByFilial(idFilialCentralizada, null, tpModal, tpAbrangencia);
	}	

	/**
	 * Retorna a filial centralizadora da filial informada por tpModal e tpAbrangencia.
	 * 
	 * @param Long idFilial
	 * @param String tpModal
	 * @param String tpAbrangencia
	 * @return Filial
	 * */	
	public Filial findFilialCentralizadoraByFilial(Long idFilialCentralizada, Long idFilialCentralizadora, String tpModal, String tpAbrangencia){
		List<Filial> result = getFilialDAO().findFilialCentralizadoraByFilial(idFilialCentralizada, idFilialCentralizadora, tpModal, tpAbrangencia);
		if (result.size() == 1) {
			return result.get(0);
		} else {
			return null;
		}
	}	

	/**
	 * Consulta as filiais de acordo com a sigla recebida por parâmetro.
	 * 
	 * @author Felipe Ferreira
	 * @param sgFilial String com a sigla da filial a consultar.
	 * @return Long id da Filial encontrada.
	 */
	public Long findIdFilialBySigla(String sgFilial) {
		return getFilialDAO().findIdFilialBySigla(sgFilial);
	}

	public Filial findFilialByIdEmpresaTpFilial(Long idEmpresa, String tpFilial) {
		return getFilialDAO().findFilialByIdEmpresaTpFilial(idEmpresa, tpFilial);
	}

	public List findFilialBySgEmpresaLookup(String sgFilial, Long idEmpresa) {
		return getFilialDAO().findBySgFilialEmpresa(sgFilial, idEmpresa);
	}

	public Map<String, Object> findSgFilialTpEmpresaByIdFilial(Long idFilial) {
		return getFilialDAO().findSgFilialTpEmpresaByIdFilial(idFilial); 
	}

	/**
	 * Busca o mapeamento de/para de código para sigla de filiais.
	 * Definido pela issue LMS-882.
	 * @return o mapeamento entre código e sigla de filiais.
	 * @author Luis Carlos Poletto
	 */
	public Map<String, String> findFilialMapping() {
		return getFilialDAO().findFilialMapping();
	}

	public Boolean findBlInformaKmPortaria(Long idFilial) {
		return getFilialDAO().findBlInformaKmPortaria(idFilial);
	}

	public Map<String, Object> findAeroportoFilial(Long idFilial) {
		return getFilialDAO().findAeroportoFilial(idFilial);
	}

	public List findFiliaisByUsuarioEmpresa(Usuario usuario, Empresa empresa, Map criteria) {
		List<Filial> filiais = getFilialDAO().findFiliaisByUsuarioEmpresa(usuario, empresa, criteria );

		List<Filial> l = getFilialDAO().findFiliaisByRegionais(usuario, empresa);
		if (null != l)
			for(Filial f : l) {
				if (!filiais.contains(f))
					filiais.add(f);
			}

		if (this.empresaUsuarioService.findAcessoIrrestritoFilial(usuario, empresa)) {
			l = getFilialDAO().findFiliaisByEmpresa(empresa);
			for(Filial f : l) {
				if (!filiais.contains(f))
					filiais.add(f);
			}
		}

		return filiais;
	}

	public Filial findFilialPadraoByUsuarioEmpresa(Usuario usuario, Empresa empresa) {
		return (Filial) getFilialDAO().findFilialPadraoByUsuarioEmpresa(usuario, empresa);
	}

	public Filial findFilialLogadoById(Long id) {
		return getFilialDAO().findFilialLogadoById(id);
	}

	public Filial findLocalizacaoFilialById(Long id) {
		Filial f = findFilialLogadoById(id);
		f.getMunicipioFiliais().get(0).getMunicipio().getUnidadeFederativa();

		return f;
	}
	
	public Object[] findLocalizacaoFilialAtual(Map<String, Object> criteria) {
		return getFilialDAO().findLocalizacaoFilialAtual(criteria);
	}
	
	public Object[] findLocalizacaoFilialOrigemAndDestino(Map<String, Object> criteria) {
		return getFilialDAO().findLocalizacaoFilialOrigemAndDestino(criteria);
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public List<Filial> findFiliaisByRegionais(List regionais) {
		if (regionais == null || regionais.isEmpty())
			return Collections.EMPTY_LIST;

		return getFilialDAO().findFiliaisByRegionais(regionais);
	}

	public List findLookupByUsuarioLogado(TypedFlatMap m) {
		return getFilialDAO().findLookupByUsuarioLogado(m);
	}

	public List findLookupByEmpresaUsuarioLogado(TypedFlatMap m) {
		return getFilialDAO().findLookupByEmpresaUsuarioLogado(m);
	}

	public ResultSetPage findPaginatedByEmpresaUsuarioLogado(TypedFlatMap m) {
		return getFilialDAO().findPaginatedByEmpresaUsuarioLogado(m);
	}


	public Integer getRowCountByEmpresaUsuarioLogado(TypedFlatMap m) {
		return getFilialDAO().getRowCountByEmpresaUsuarioLogado(m);
	}

	/**
	 * Find Combo das filiais do usuario logado
	 * @return
	 * @see getFilialDAO().findComboByUsuarioLogado()
	 */
	public List findComboByUsuarioLogado() {
		return getFilialDAO().findComboByUsuarioLogado();
	}

	/**
	 * 
	 * @param idRotaIdaVolta
	 * @param idRota
	 * @param idSolicitacaoContratacao
	 * @param blOrigemRota
	 * @param blDestinoRota
	 * @return
	 */
	public Filial findFilialOrigemDestinoToControleCarga(
			Long idRotaIdaVolta, 
			Long idRota,
			Long idSolicContratacao, 
			Boolean blOrigemRota, 
			Boolean blDestinoRota
	) {
		return getFilialDAO().findFilialOrigemDestinoToControleCarga(
				idRotaIdaVolta, idRota, idSolicContratacao, blOrigemRota, blDestinoRota);
	}

	/**
	 * Retorna o campo pcJuroDiario da filial informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 25/07/2006
	 * 
	 * @param Long idFilial
	 * @return BigDecimal
	 */
	public BigDecimal findPcJuroDiario(Long idFilial) {
		return getFilialDAO().findPcJuroDiario(idFilial);
	}

	//**********************************FIND LOOKUP DA FILIAL**********************************************//
	/**
	 * Find lookup padrão
	 */
	public List findLookup(Map criteria) {
		if (StringUtils.isNotBlank((String)criteria.get(MAP_KEY_TP_ACESSO))){
			/*TIPO DE ACESSO A- ABERTO (usa os critérios que a tela enviar), ou F- Filiais (não contemplado)*/
		} else {
			/*Sempre busca as filiais da empresa do usuário logado*/
			criteria.put(MAP_KEY_EMPRESA_ID_EMPRESA,SessionUtils.getEmpresaSessao().getIdEmpresa());
		}
		return super.findLookup(criteria);
	}

	public List<TypedFlatMap> findLookupAsPaginated(TypedFlatMap criteria) {
		List l = getFilialDAO().findLookupAsPaginated(criteria);
		if (l.size() == 1) {
			List<TypedFlatMap> newList = new ArrayList<TypedFlatMap>();

			Map<String, Object> result = (Map<String, Object>)l.get(0);
			TypedFlatMap newResult = new TypedFlatMap();
			for (Iterator ie2 = result.keySet().iterator(); ie2.hasNext();) {
				String key = (String)ie2.next();
				newResult.put(key.replace('_','.'),result.get(key));
			}

			if (StringUtils.isNotBlank(newResult.getString(MAP_KEY_PESSOA_NR_IDENTIFICACAO))) {
				newResult.put(MAP_KEY_PESSOA_NR_IDENTIFICACAO,FormatUtils.formatIdentificacao(
						newResult.getDomainValue(MAP_KEY_PESSOA_TP_IDENTIFICACAO).getValue(),
						newResult.getString(MAP_KEY_PESSOA_NR_IDENTIFICACAO)));
			}

			if (StringUtils.isNotBlank(newResult.getString(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO))) {
				newResult.put(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO,FormatUtils.formatIdentificacao(
						newResult.getDomainValue("empresa.pessoa.tpIdentificacao").getValue(),
						newResult.getString(MAP_KEY_EMPRESA_PESSOA_NR_IDENTIFICACAO)));
			}

			Regional regional = regionalFilialService.findLastRegionalVigente(newResult.getLong(MAP_KEY_ID_FILIAL));
			if (regional != null) {
				newResult.put(MAP_KEY_LAST_REGIONAL_DS_REGIONAL,regional.getDsRegional());
				newResult.put("lastRegional.idRegional",regional.getIdRegional());
				newResult.put(MAP_KEY_LAST_REGIONAL_SG_REGIONAL,regional.getSgRegional());
			}
			if("S".equals(criteria.getString("findEndereco"))){
				fillEndereco(newResult);				
			}

			newList.add(newResult);
			return newList;
		}

		return l;
	}

	public List<Map<String, Object>> findLookupFilial(Map criteria) {
		List<Map<String, Object>> rs = getFilialDAO().findLookupFilial(criteria);
		if( rs != null ){
			return formataRetorno(rs);
		}
		return rs;
	}

	/**
	 * Método sobrecarregado com mais um parâmetro "String tpEmpresa"
	 * 
	 * @param sgFilial
	 * @param tpAcesso
	 * @return
	 */
	public List findLookupBySgFilial(String sgFilial, String tpAcesso) {
		return findLookupBySgFilial(sgFilial, tpAcesso, null);
	}
	
	/**
	 * Busca o numero máximo de notas fiscais que podem ser incluidas em um CTRC
	 * de acordo com o identificador da filial.
	 * 
	 * @param idFilialDestino
	 *            identificador da filial de destino
	 * @param idFilial
	 *            identificador da filial que emitira o conhecimento
	 * @return numero máximo de notas
	 */
	public int findMaxNotasCtrc(Long idFilialDestino, Long idFilial) {
		Filial filialDestino = findByIdInitLazyProperties(idFilialDestino, false);
		
		BigDecimal result = null;
		
		if (Boolean.TRUE.equals(filialDestino.getBlLimitaNotaFiscalForm())) {
			result = (BigDecimal) configuracoesFacade.getValorParametro(idFilial, MAP_KEY_LIMITE_NFS_CTRC_IMPR);
			if (result == null) {
				result = (BigDecimal) configuracoesFacade.getValorParametro(MAP_KEY_LIMITE_NFS_CTRC_IMPRESSO);
			}
		}
		if (result == null) {
			result = new BigDecimal(-1);
		}
		
		return result.intValue();
	}
	
	public int findMaxNotasCtrcOrigemDestino(Long idFilialDestino, Long idFilial) {
		Filial filialDestino = findByIdInitLazyProperties(idFilialDestino, false);
		Filial filialOrigem = findByIdInitLazyProperties(idFilial, false);
		
		BigDecimal result = null;
		
		if (Boolean.TRUE.equals(filialOrigem.getBlLimitaNotaFiscalForm()) || Boolean.TRUE.equals(filialDestino.getBlLimitaNotaFiscalForm())) {
			result = (BigDecimal) configuracoesFacade.getValorParametro(idFilial, MAP_KEY_LIMITE_NFS_CTRC_IMPR);
			if (result == null) {
				result = (BigDecimal) configuracoesFacade.getValorParametro(MAP_KEY_LIMITE_NFS_CTRC_IMPRESSO);
			}
		}
		if (result == null) {
			result = new BigDecimal(-1);
		}
		
		return result.intValue();
	}
	
	public int findMaxNotasCtrcOrigem(Long idFilialOrigem) {
		Filial filialOrigem = findByIdInitLazyProperties(idFilialOrigem, false);
		
		BigDecimal result = null;
		
		if (Boolean.TRUE.equals(filialOrigem.getBlLimitaNotaFiscalForm())) {
			result = (BigDecimal) configuracoesFacade.getValorParametro(idFilialOrigem, MAP_KEY_LIMITE_NFS_CTRC_IMPR);
			if (result == null) {
				result = (BigDecimal) configuracoesFacade.getValorParametro(MAP_KEY_LIMITE_NFS_CTRC_IMPRESSO);
			}
		}
		if (result == null) {
			result = new BigDecimal(-1);
		}
		
		return result.intValue();
	}
	
	/**
	 * Find Lookup que pesquisa SOMENTE pela sigla da filial 
	 * 
	 * @author Andresa
	 * @param sgFilial
	 * @return "idFilial", "sgFilial", "pessoa_idPessoa",
	 *         pessoa_nmFantasia", "empresa_tpEmpresa"
	 */
	public List findLookupBySgFilial(String sgFilial, String tpAcesso, String tpEmpresa) {
		Long idEmpresa = null;
		/* Novo tipo de acesso (necessario para forcar a busca de filiais mercurio) */
		if("EM".equals(tpAcesso)) {
			idEmpresa = empresaService.findEmpresaMercurio().getIdEmpresa();
			tpAcesso = "";
		} else {
			idEmpresa = SessionUtils.getEmpresaSessao().getIdEmpresa();
		}
		return getFilialDAO().findLookupBySgFilial(sgFilial, tpAcesso, idEmpresa, tpEmpresa);
	}

	/**
	 * Find Lookup que pesquisa SOMENTE filiais da Mércurio 
	 *
	 * @author Andresa
	 * @param sgFilial
	 * @return "idFilial", "sgFilial", "pessoa_idPessoa", pessoa_nmFantasia", "empresa_tpEmpresa"
	 */
	public List findLookupFilialMercurio(String sgFilial, String tpAcesso) {
		Empresa empresa = empresaService.findEmpresaMercurio();
		Long idEmpresa = empresa.getIdEmpresa();
		return getFilialDAO().findLookupBySgFilial(sgFilial, tpAcesso, idEmpresa);
	}

	/**
	 * Utilizado em todas as lookups de Filial que devem retornar só os vigentes.<BR>
	 * @param criteria
	 * @return
	 */
	public List findLookupBySgFilialVigenteEm(TypedFlatMap criteria) {
		YearMonthDay date = null;

		if (criteria.getYearMonthDay("historicoFiliais.vigenteEm") != null)
			date = criteria.getYearMonthDay("historicoFiliais.vigenteEm");

		return getFilialDAO().findLookupBySgFilialVigenteEm(criteria.getString(MAP_KEY_SG_FILIAL), date, criteria.getString(MAP_KEY_TP_ACESSO));
	}

	/**
	 * Método sobrecarregado com mais o parâmetro "String tpEmpresa"
	 * @param sgFilial
	 * @param ymdVigente
	 * @param tpAcesso
	 * @return
	 */
	public List findLookupBySgFilialVigenteEm(String sgFilial, YearMonthDay ymdVigente, String tpAcesso) {
		return getFilialDAO().findLookupBySgFilialVigenteEm(sgFilial, ymdVigente, tpAcesso, null);
	}

	/**
	 * Esse método não serve para retornar as filial vigente mas 
	 * as filial onde o histórico da filial é vigente, é usado em casos 
	 * específicos e é informado na especificação da tela. 
	 * 
	 * CUIDADO!
	 * 
	 * @author Mickaël Jalbert
	 * @param String sgFilial
	 * @param YearMonthDay ymdVigente
	 * @return List
	 */
	public List findLookupBySgFilialVigenteEm(String sgFilial, YearMonthDay ymdVigente, String tpAcesso, String tpEmpresa) {
		return getFilialDAO().findLookupBySgFilialVigenteEm(sgFilial, ymdVigente, tpAcesso, tpEmpresa);
	}

	//********************************FIND DEPRECATED*****************************************//
	/**
	 * Find Lookup, favor não utilizar mais este método, fazer uso do findLookupBySgFilial(String sgFilial), 
	 * pois a diferença está somente na passagem de parâmetro, a pesquisa e retorno são os mesmos do 
	 * findLookupBySgFilial(String sgFilial)
	 * 
	 * @author Andresa
	 * @deprecated
	 * @param criteria
	 * @return
	 */
	public List findLookupBySgFilial(Map criteria) {
		return findLookupBySgFilial((String)criteria.get(MAP_KEY_SG_FILIAL), (String)criteria.get(MAP_KEY_TP_ACESSO));
	}

	/**
	 * Busca a filial de acordo com a empresa e a sigla
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 08/12/2006
	 *
	 * @param sgFilial
	 * @param idEmpresa
	 * @return Filial
	 */
	public Filial findFilial(Long idEmpresa, String sgFilial) {
		return getFilialDAO().findFilial(idEmpresa, sgFilial);
	}
	
	public String findSgFilialLegadoByIdFilial(Long idFilial) {
		return getFilialDAO().findSgFilialLegadoByIdFilial(idFilial);		
	}
	
	public String findSgFilialLegadoByIdFilialCooperada(Long idFilial) {
		return getFilialDAO().findSgFilialLegadoByIdFilialCooperada(idFilial);
	}
	
	public String findSgFilialLegadoByFilialTntOuCooperada(Long idFilial) {
		try {
			return getFilialDAO().findSgFilialLegadoByIdFilial(idFilial);
		} catch (Exception e) {
			return getFilialDAO().findSgFilialLegadoByIdFilialCooperada(idFilial);
		}
	}
	
	public Filial findFilialBySgFilialLegado(String sgFilialLegado) {
		Long idFilial = getFilialDAO().findIdFilialBySgFilialLegado(sgFilialLegado);
		if (idFilial == null)
			return null;
		return findById(idFilial);
	}
	
	public List<Filial> findFiliaisByCodsFilial(List<Integer> codsFilial) {
		return getFilialDAO().findFiliaisByCodsFilial(codsFilial);
	}
	
	/**
	 * Solicitação CQPRO00005943 / CQPRO00007109 da Integração.
	 * @param tpEmpresa
	 * @param vigenteEm
	 * @return
	 */
	public List<Filial> findByTpEmpresa(String tpEmpresa, YearMonthDay vigenteEm){
		return getFilialDAO().findByTpEmpresa(tpEmpresa, vigenteEm);
	}

	/**
	 * Verifica se a filial possui horário de corte e retorna a quantidade de horas a adiar.
	 * 
	 * @param filial
	 * @param compareTime
	 * @return
	 */
	public int validateHorarioCorteFilial(Filial filial, TimeOfDay compareTime ){
		if (compareTime == null){
			compareTime = JTDateTimeUtils.getHorarioAtual(); 
		}
		if (filial.getHrCorte()!=null && compareTime.isAfter(filial.getHrCorte())){
				return 24;
		}
		return 0;
	}
	
	public Boolean validateEnderecoPessoaDeUmaFilialByIdPessoa(Long idPessoa) {
		Integer countFilial = getFilialDAO().getRowCountFilialByIdPessoa(idPessoa);
		return IntegerUtils.hasValue(countFilial);
	}
	
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setWorkflowPendenciaService(WorkflowPendenciaService workflowPendenciaService) {
		this.workflowPendenciaService = workflowPendenciaService;
	}
	public void setAcaoService(AcaoService acaoService) {
		this.acaoService = acaoService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	public void setContatoService(ContatoService contatoService) {
		this.contatoService = contatoService;
	}
	public void setRegionalFilialService(RegionalFilialService regionalFilialService) {
		this.regionalFilialService = regionalFilialService;
	}
	public void setEmpresaUsuarioService(EmpresaUsuarioService empresaUsuarioService) {
		this.empresaUsuarioService = empresaUsuarioService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	
	public void setPendenciaService(PendenciaService pendenciaService) {
		this.pendenciaService = pendenciaService;
	}

	public void setFluxoFilialService(FluxoFilialService fluxoFilialService) {
		this.fluxoFilialService = fluxoFilialService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	/**
	 * verifica se o lms esta implantado na filial na data atual
	 * @param filial
	 * @return
	 */
	public boolean findIsFilialLMS(Filial filial) {
		return findIsFilialLMS(filial, JTDateTimeUtils.getDataAtual());
	}

	/**
	 * verifica se o lms esta implantado na filial na data do paranetro
	 * @param filial
	 * @param data
	 * @return
	 */
	public boolean findIsFilialLMS(Filial filial, YearMonthDay data) {
		filial = findById(filial.getIdFilial());
		if(filial.getDtImplantacaoLMS() != null && filial.getDtImplantacaoLMS().compareTo(data) <= 0){
    		return true;
    	}
		return false;
	}
	
	public void validateSelecaoFilialUsuarioEmpresa(Long idFilial, Empresa empresa, Usuario usuario) {
		boolean acessoIrrestritoFilial = empresaUsuarioService.findAcessoIrrestritoFilial(usuario, empresa);
		if (!acessoIrrestritoFilial) {
			Filial filial = getFilialDAO().findFilialByUsuarioAndEmpresaAndFilial(usuario, empresa, idFilial);
			if (filial == null) {
				throw new BusinessException("LMS-00063");
			}
		}
	}

	public String findSgFilialByIdDoctoServico(Long idDoctoServico) {
		return this.getFilialDAO().findSgFilialByIdDoctoServico(idDoctoServico);
	}

	public List findFiliaisEmpresa(){
		return getFilialDAO().findFiliaisEmpresa();
	}
	
	public List<Filial> findFiliaisDiferentesEntreFluxosFiliais(FluxoFilial fluxoFilialOrigem, FluxoFilial fluxoFilialClone) {
		return getFilialDAO().findFiliaisDiferentesEntreFluxosFiliais(fluxoFilialOrigem, fluxoFilialClone);
	}
	
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}
	public void setPostoAvancadoService(PostoAvancadoService postoAvancadoService) {
		this.postoAvancadoService = postoAvancadoService;
	}
	public PostoConveniadoService getPostoConveniadoService() {
		return postoConveniadoService;
	}
	public void setPostoConveniadoService(PostoConveniadoService postoConveniadoService) {
		this.postoConveniadoService = postoConveniadoService;
	}
	public void setRegiaoGeograficaService(RegiaoGeograficaService regiaoGeograficaService) {
		this.regiaoGeograficaService = regiaoGeograficaService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public List<Filial> findContatosFiliaisMercurio() {
		return getFilialDAO().findContatosFiliaisMercurio();
	}
	
	public Map<String,Object> findGerentesFiliaisMercurio() {
		return getFilialDAO().findGerentesFiliaisMercurio();
	}
				  
	public Filial findBySgFilialAndIdEmpresa(String sgFilial, Long idEmpresa) {
		return getFilialDAO().findBySgFilialAndIdEmpresa(sgFilial, idEmpresa);
	}
	
	public Filial findBySgFilialAndTpEmpresa(String sgFilial, String tpEmpresa) {
		return getFilialDAO().findBySgFilialAndTpEmpresa(sgFilial, tpEmpresa);
	}
	
	public Filial findFilialByCodFilial(Long idEmpresa, Integer codFilial) {
		return getFilialDAO().findFilialByCodFilial(idEmpresa, codFilial);
	}
	
	public Filial findFilialByCodigoFilial(Integer codFilial) {
		return getFilialDAO().findFilialByCodigoFilial(codFilial);
	}
	
	public Filial findByCliente(Long idCliente) {
		return getFilialDAO().findFilialByCliente(idCliente);
	}
		
	public Filial findByUsuario(Long idUsuario) {
		return getFilialDAO().findFilialByUsuario(idUsuario);
	}

	public Filial findByNrIdentificacaoAndIdEmpresa(String nrIdentificacaoExp, Long idEmpresa) {
		return getFilialDAO().findByNrIdentificacaoAndIdEmpresa(nrIdentificacaoExp, idEmpresa);
	}

	public Filial findFilialRespByNrIdentificacaoAndIdEmpresa(String nrIdentificacao, Long idEmpresa) {
		Filial filial = getFilialDAO().findByNrIdentificacaoAndIdEmpresa(nrIdentificacao, idEmpresa);
		/** LMS-8199: Não é uma filial, busca filial Responsável pelo cliente*/
		if(filial == null){
			filial = getFilialDAO().findFilialRespByNrIdentificacaoAndIdEmpresa(nrIdentificacao, idEmpresa);
		}
		return filial;
	}
		
	
	public FilialAtendimentoDto findFilialAtendimento(Long idFilialAtendimentoCliente) {
		return getFilialDAO().findFilialAtendimento(idFilialAtendimentoCliente);
	}
	
	
	public Filial findFilialByCdFilialFedex(String cdFilialFedex){
	    return getFilialDAO().findFilialByCdFilialFedex(cdFilialFedex);
	}

	public List<Filial> findFiliaisByCdFilialFedex(String cdFilialFedex) {
        return getFilialDAO().findFiliaisByCdFilialFedex(cdFilialFedex);    
    }

	public List<Map<String,Object>> findFiliaisByUsuario(Long idUsuario) {
		return getFilialDAO().findFiliaisByUsuario(idUsuario);
	}

	public Map<Long, String> findSiglaComDoisDigitos() {
		return getFilialDAO().findSiglaComDoisDigitos();
	}
	
	/**
	 * Efetua pesquisa para a suggest de FilialPerfil.
	 * 
	 * @return List<Map<String, Object>>
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findFilialPerfilSuggest(String sgFilial, String idEmpresa, Usuario usuario) {
		TypedFlatMap tfm = new TypedFlatMap();
		tfm.put(MAP_KEY_EMPRESA_ID_EMPRESA, idEmpresa);
		tfm.put(MAP_KEY_SG_FILIAL, sgFilial);
		tfm.put(ConstantesAmbiente.USUARIO_LOGADO, usuario);
	
		return getFilialDAO().findLookupByEmpresaUsuarioLogado(tfm);
	}
	
	public List<Integer> findCodFiliaisIntegraNotfis() {
		return getFilialDAO().findCodFiliaisIntegraNotfis();
	}

	public Filial findFilialBySgFilialAndIdEmpresa (String sgFilial, Long idEmpresa) {
		List filiais = getFilialDAO().findBySgFilialEmpresa(sgFilial, idEmpresa);

		if(filiais != null && !filiais.isEmpty()){
			Map<String,Object> filiaisMap = (Map<String, Object>) filiais.get(0);
			return findById((Long) filiaisMap.get("idFilial"));
		} else {
			return  null;
		}
	}
}
