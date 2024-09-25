package com.mercurio.lms.municipios.model.service;

import static java.lang.Boolean.FALSE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.OperacaoServicoLocaliza;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.dao.OperacaoServicoLocalizaDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.TimeUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.operacaoServicoLocalizacaoService"
 */
public class OperacaoServicoLocalizaService extends CrudService<OperacaoServicoLocaliza, Long> {
	private MunicipioFilialService municipioFilialService;
	private MunicipioFilialIntervCepService municipioFilialIntervCepService;
	private FilialService filialService;
	private VigenciaService vigenciaService;
	private AtendimentoClienteService atendimentoClienteService;

	/**
	 * Recupera uma instância de <code>OperacaoServicoLocalizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public OperacaoServicoLocaliza findById(java.lang.Long id) {
		return (OperacaoServicoLocaliza)super.findById(id);
	}

	/** 
	 * FindByIdDetalhado customizado
	 * @param id
	 * @return
	 */
	public Map findByIdDetalhado(java.lang.Long id) {
		OperacaoServicoLocaliza osl = (OperacaoServicoLocaliza)super.findById(id);
		TypedFlatMap item = bean2map(osl);		

		// relendo os tempos salvos em minutos para horas
		Integer nrTempoColeta = null;
		Integer nrTempoEntrega = null;

		if (osl.getNrTempoColeta() != null) nrTempoColeta = Integer.valueOf(((Long)item.remove("nrTempoColeta")).intValue());
		if (osl.getNrTempoEntrega()!= null) nrTempoEntrega = Integer.valueOf(((Long)item.remove("nrTempoEntrega")).intValue());

		item.put("nrTempoEntrega", TimeUtils.minutesToHoursInteger(nrTempoEntrega));
		item.put("nrTempoColeta", TimeUtils.minutesToHoursInteger(nrTempoColeta));
		item.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(osl));
		return item;
	}

	private TypedFlatMap bean2map(OperacaoServicoLocaliza osl){
		TypedFlatMap map = new TypedFlatMap();
		map.put("idOperacaoServicoLocaliza", osl.getIdOperacaoServicoLocaliza());
		map.put("blAceitaFreteFob", osl.getBlAceitaFreteFob());
		map.put("blAtendimentoGeral", osl.getBlAtendimentoGeral());
		map.put("blCobraTaxaFluvial", osl.getBlCobraTaxaFluvial());
		map.put("blDomingo", osl.getBlDomingo());
		map.put("blQuarta", osl.getBlQuarta());
		map.put("blQuinta", osl.getBlQuinta());
		map.put("blSabado", osl.getBlSabado());
		map.put("blSegunda", osl.getBlSegunda());
		map.put("blSexta", osl.getBlSexta());
		map.put("blTerca", osl.getBlTerca());
		map.put("dtVigenciaInicial", osl.getDtVigenciaInicial());
		map.put("dtVigenciaFinal", osl.getDtVigenciaFinal());
		map.put("nrTempoColeta", osl.getNrTempoColeta());
		map.put("nrTempoEntrega", osl.getNrTempoEntrega());
		map.put("tpOperacao.value", osl.getTpOperacao().getValue());
		map.put("tpOperacao.id", osl.getTpOperacao().getId());
		MunicipioFilial municipioFilial = osl.getMunicipioFilial();
		Filial filial = municipioFilial.getFilial();
		Empresa empresa = filial.getEmpresa();
		map.put("filial.empresa.idEmpresa", empresa.getIdEmpresa());
		map.put("filial.empresa.pessoa.nmPessoa", empresa.getPessoa().getNmPessoa());
		map.put("filial.empresa.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(empresa.getPessoa()));
		if (osl.getServico() != null) {
			map.put("servico.idServico", osl.getServico().getIdServico());
		}
		map.put("tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio", osl.getTipoLocalizacaoMunicipio().getIdTipoLocalizacaoMunicipio());
		map.put("tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio", osl.getTipoLocalizacaoMunicipio().getDsTipoLocalizacaoMunicipio().getValue());
		map.put("tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio", osl.getTipoLocalizacaoMunicipioFob().getIdTipoLocalizacaoMunicipio());
		map.put("tipoLocalizacaoMunicipioFob.dsTipoLocalizacaoMunicipio", osl.getTipoLocalizacaoMunicipioFob().getDsTipoLocalizacaoMunicipio().getValue());
		Municipio municipio = municipioFilial.getMunicipio();
		Municipio municipioDistrito = municipio.getMunicipioDistrito();
		UnidadeFederativa unidadeFederativa = municipio.getUnidadeFederativa();
		Pais pais = unidadeFederativa.getPais();
		map.put("municipioFilial.idMunicipioFilial", municipioFilial.getIdMunicipioFilial());
		map.put("municipioFilial.municipio.idMunicipio", municipio.getIdMunicipio());
		map.put("municipioFilial.municipio.nmMunicipio", municipio.getNmMunicipio());
		map.put("municipioFilial.municipio.blDistrito", municipio.getBlDistrito());		
		map.put("municipioFilial.municipio.unidadeFederativa.idUnidadeFederativa", unidadeFederativa.getIdUnidadeFederativa());
		map.put("municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa", unidadeFederativa.getNmUnidadeFederativa());
		map.put("municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa", unidadeFederativa.getSgUnidadeFederativa());
		map.put("municipioFilial.municipio.unidadeFederativa.pais.idPais", pais.getIdPais());
		map.put("municipioFilial.municipio.unidadeFederativa.pais.nmPais", pais.getNmPais());
		map.put("municipioFilial.filial.idFilial", filial.getIdFilial());
		map.put("municipioFilial.filial.sgFilial", filial.getSgFilial());
		map.put("municipioFilial.filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		map.put("municipioFilial.filial.siglaNomeFilial", filial.getSgFilial() + " - " + filial.getPessoa().getNmFantasia());
		map.put("municipioFilial.municipio.unidadeFederativa.siglaDescricao", unidadeFederativa.getSgUnidadeFederativa() + " - " + unidadeFederativa.getNmUnidadeFederativa());

		if (municipioDistrito != null) {
			map.put("municipioFilial.municipio.municipioDistrito.idMunicipio", municipioDistrito.getIdMunicipio());
			map.put("municipioFilial.municipio.municipioDistrito.nmMunicipio", municipioDistrito.getNmMunicipio());
		}

		return map;
	}

	/**
	 * FindPaginated Customizado
	 * @see getOperacaoServicoLocalizaDAO().findPaginated()
	 */
	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		ResultSetPage rsp = getOperacaoServicoLocalizaDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));		
		List list = AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList());
		rsp.setList(list);
		return rsp;
	}

	/**
	 * GetRowCount Customizado
	 * @see getOperacaoServicoLocalizaDAO().getRowCountCustom()
	 */
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return this.getOperacaoServicoLocalizaDAO().getRowCountCustom(criteria);
	}

	/**
	 * Busca os operações de serviço localização por municipio
	 * @param criteria
	 * @return
	 */
	public List findOperacaoServicoPorMunicipio(TypedFlatMap criteria){
		
		Long idMunicipio = criteria.getLong("municipioFilial.municipio.idMunicipio");
		Long idFilial = criteria.getLong("municipioFilial.filial.idFilial");
		
		if (idMunicipio != null && idFilial != null) {
			List retorno = getOperacaoServicoLocalizaDAO().findOperacaoServicoPorMunicipio(idMunicipio,idFilial);
			return AliasToNestedMapResultTransformer.getInstance().transformListResult(retorno);
		}
		return null;
	}

	/**
	 * Retorna registro de Operacao X Servico X Localizacao vigente para o Municipio X Filial
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public OperacaoServicoLocaliza findOperacaoServicoPorMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getOperacaoServicoLocalizaDAO().findOperacaoServicoPorMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
	}
	
	/**
	 * Busca as frequencias de atendimento por municipio filial
	 * @param idMunicipioFilial
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 */
	public Object[] findFrequenciaPorMunicipioFilial(Long idMunicipioFilial, Long idServico, YearMonthDay dtVigencia){
		return getOperacaoServicoLocalizaDAO().findFrequenciaPorMunicipioFilial(idMunicipioFilial, idServico, dtVigencia);
	}
	
	/**
	 * Metodo que retorno operacaoServicoLocaliza dos municipios filiais de origem ou destino
	 * @param idMunicipioFilialOrigem
	 * @param idMunicipioFilialDestino
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 */
	public List findOperacaoServicoLocalizacao(Long idMunicipioFilialOrigem, Long idMunicipioFilialDestino, Long idServico, YearMonthDay dtVigencia){
		return getOperacaoServicoLocalizaDAO().findOperacaoServicoLocalizacao(idMunicipioFilialOrigem, idMunicipioFilialDestino, idServico, dtVigencia);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
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
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	public java.io.Serializable store(OperacaoServicoLocaliza bean) {
		OperacaoServicoLocaliza operacaoServicoLocalizacao = (OperacaoServicoLocaliza)bean;
		vigenciaService.validaVigenciaBeforeStore(operacaoServicoLocalizacao);

		return super.store(operacaoServicoLocalizacao);
	}

	protected OperacaoServicoLocaliza beforeStore(OperacaoServicoLocaliza osl) {		
		// os tempos são lidos em horas mas são salvos no banco em minutos
		if(osl.getNrTempoColeta() != null) {		
			osl.setNrTempoColeta( Long.valueOf(TimeUtils.HoursToMinutesInteger(osl.getNrTempoColeta().intValue())));
		}

		if(osl.getNrTempoEntrega() != null) {
			osl.setNrTempoEntrega( Long.valueOf(TimeUtils.HoursToMinutesInteger(osl.getNrTempoEntrega().intValue())));
		}

		// Testando se existe um municipioFilial e servico vigentes
		if(getOperacaoServicoLocalizaDAO().findOperacaoServicoLocalizaVigente(osl)) { 
			throw new BusinessException("LMS-00003"); 
		}

		// Testando LMS-29022
		if (osl.getMunicipioFilial() != null) {
			municipioFilialService.validateVigenciaAtendimento(osl.getMunicipioFilial().getIdMunicipioFilial(), osl.getDtVigenciaInicial(), osl.getDtVigenciaFinal());
		}

		// Testando LMS-29020
		if (!verificaPeloMenosUmDiaChecado(osl)) {
			throw new BusinessException("LMS-29020");
		}

		//regra 3.10 - Se blrestricao="S" e se esse municipioFilial possui relacao com outras tabelas
		if(municipioFilialService.findRestricaoAtendimentoERelacoesByMunicipioFilial(osl.getMunicipioFilial().getIdMunicipioFilial())) {
			throw new BusinessException("LMS-29089");
		}

		YearMonthDay dtVigenciaFinal;
		if(osl.getDtVigenciaFinal() != null) {
			dtVigenciaFinal = osl.getDtVigenciaFinal();
		} else {
			dtVigenciaFinal = osl.getDtVigenciaInicial();
		}

		Long idServico = osl.getServico() != null ? osl.getServico().getIdServico() : null;

		//Regra 3.11
		if(osl.getBlAtendimentoGeral().booleanValue()
			&& !getOperacaoServicoLocalizaDAO().verificaAtendimentoRestricoesServicoOperacaoVigente(osl.getIdOperacaoServicoLocaliza(), osl.getMunicipioFilial().getIdMunicipioFilial(), osl.getTpOperacao().getValue(), idServico, osl.getDtVigenciaInicial(), dtVigenciaFinal)
		) {
			throw new BusinessException("LMS-29087");
		}

		MunicipioFilial municipioFilial = municipioFilialService.findById(osl.getMunicipioFilial().getIdMunicipioFilial());

		//Regra 3.12
		if ((osl.getTpOperacao().getValue().equals("A") || osl.getTpOperacao().getValue().equals("E"))) {
			List municipioFilialIntervalosCep = municipioFilialIntervCepService.findIntervCepVigenteByMunicipioFilial(osl.getMunicipioFilial().getIdMunicipioFilial(), osl.getDtVigenciaInicial(), osl.getDtVigenciaFinal());
			if(!(!municipioFilialIntervalosCep.isEmpty()
				|| !municipioFilial.getBlRestricaoAtendimento().booleanValue()
				|| !osl.getBlAtendimentoGeral().booleanValue())
			) {
				throw new BusinessException("LMS-29109");
			}
		}

		//regra 3.14
		if(municipioFilial.getBlPadraoMcd().booleanValue()) {
			if(!osl.getBlAtendimentoGeral().booleanValue()) {
				throw new BusinessException("LMS-29126");
			}
		}
		
		// Regra 3.15
		if (osl.getDtVigenciaFinal() != null) {
			if (FALSE.equals(osl.getBlAtendimentoGeral())) {
				if (atendimentoClienteService.validateExisteAtendimentoCliente(osl.getIdOperacaoServicoLocaliza(), JTDateTimeUtils.getDataAtual())) {
					throw new BusinessException("LMS-29129", new Object[]{new DefaultMessageSourceResolvable("atendimentosClientes")});
				}
			}
		}

		filialService.verificaExistenciaHistoricoFilial(osl.getMunicipioFilial().getFilial().getIdFilial(), osl.getDtVigenciaInicial(), osl.getDtVigenciaFinal());

		return super.beforeStore(osl);
	}
	
	/**
	 * Verifica se pelo menos um dia da semana foi checado.
	 * @param bean Um objeto.
	 * @return True se pelo menos um dia da semana 
	 * foi checado ou false em caso contrario.
	 */
	public boolean verificaPeloMenosUmDiaChecado(Object bean) {
		OperacaoServicoLocaliza osl = (OperacaoServicoLocaliza) bean;
		
		return !((osl.getBlDomingo().equals(Boolean.FALSE)) 
			&& (osl.getBlSegunda().equals(Boolean.FALSE))
			&& (osl.getBlTerca().equals(Boolean.FALSE))
			&& (osl.getBlQuarta().equals(Boolean.FALSE))
			&& (osl.getBlQuinta().equals(Boolean.FALSE))
			&& (osl.getBlSexta().equals(Boolean.FALSE))
			&& (osl.getBlSabado().equals(Boolean.FALSE))
		);
	}
	
	/**
	 * Verifica se o intervalo de datas é vigente
	 * @param osl OperacaoServicoLocaliza
	 * @return True, se for vigente.
	 */
	public boolean validaVigenciaAtendimento(Long idOperacaoServicoLocaliza, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getOperacaoServicoLocalizaDAO().validaVigenciaAtendimento(idOperacaoServicoLocaliza, dtVigenciaInicial, dtVigenciaFinal);
	}
	
	
	/**
	 * Verifica se existe algum registro de OperacaoXServicoXLocalizacao vigente no dia atual ou no dia atual + 1 para o 
	 * MunicipioXFilial informado
	 * @param idMunicipioFilialTroca
	 * @return TRUE se existe algum registro, FALSE caso contrario
	 */
	public boolean verificaExisteOperacaoServicoParaFilialTroca(Long idMunicipioFilialTroca){
		return getOperacaoServicoLocalizaDAO().verificaExisteOperacaoServicoParaFilialTroca(idMunicipioFilialTroca);
	}
	
	/**
	 * Método que retorna a lista de OperacaoServicoLocaliza
	 * onde OperacaoServicoLocaliza.tpOperacao seja 'A' ou 'E' 
	 * o idMunicipio da municipioFilial relacionado a operacaoServicoLocaliza seja igual 
	 * idMunicipioEntrega passado como parametro
	 * a municipioFilial.idFilial igual ao idFilial passado como prametro 
	 * a data ocorrencia passada esteja entre as datas de vigencia do municipioFilial
	 * e municipioFilial.blPadraoMcd = true
	 * 
	 * @param idFilial
	 * @param idMunicipioEntrega
	 * @param dhOcorrencia
	 * @return
	 */
	
	/***
	 * LMS - 5273
	 * 
	 */
	public List<OperacaoServicoLocaliza> findOperacaoServicoLocalizaByIdFilialDestinoEIdMunicipioEntregaDoctoServico(Long idFilial, Long idMunicipioEntrega, DateTime dhOcorrencia, Servico servico) {
		return getOperacaoServicoLocalizaDAO().findOperacaoServicoLocalizaByIdFilialDestinoEIdMunicipioEntregaDoctoServico(idFilial, idMunicipioEntrega, dhOcorrencia, servico);
	}
	
	protected void beforeRemoveByIds(List ids) {
		OperacaoServicoLocaliza bean = null;
		for(Iterator ie = ids.iterator(); ie.hasNext();) {
			bean = findById((Long)ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		List list = new ArrayList();
		list.add(id);
		beforeRemoveByIds(list);
	}

	public List findOperacaoServicoLocalVigenteByMunFilial(Long idMunicipioFilial){
		return getOperacaoServicoLocalizaDAO().findOperacaoServicoLocalVigenteByMunFilial(idMunicipioFilial);
	}

	public List<OperacaoServicoLocaliza> findOperacaoServicoLocaliza(Long idMunicipio, Boolean blIndicadorColeta, Long idServico, YearMonthDay dtVigencia) {
		return getOperacaoServicoLocalizaDAO().findOperacaoServicoLocaliza(idMunicipio, blIndicadorColeta, idServico, dtVigencia);
	}

	public void setOperacaoServicoLocalizacaoDAO(OperacaoServicoLocalizaDAO dao) {
		setDao( dao );
	}
	private final OperacaoServicoLocalizaDAO getOperacaoServicoLocalizaDAO() {
		return (OperacaoServicoLocalizaDAO) getDao();
	}
	public void setMunicipioFilialIntervCepService(MunicipioFilialIntervCepService municipioFilialIntervCepService) {
		this.municipioFilialIntervCepService = municipioFilialIntervCepService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

	public void setAtendimentoClienteService(
			AtendimentoClienteService atendimentoClienteService) {
		this.atendimentoClienteService = atendimentoClienteService;
	}

}