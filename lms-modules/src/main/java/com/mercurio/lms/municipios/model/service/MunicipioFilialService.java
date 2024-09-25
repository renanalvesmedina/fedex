package com.mercurio.lms.municipios.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialDAO;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialJdbcDAO;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.municipios.municipioFilialService"
 */
public class MunicipioFilialService extends CrudService<MunicipioFilial, Long> {
	private FilialService filialService;
	private EmpresaService empresaService;
	private MunicipioFilialSegmentoService municipioFilialSegmentoService;
	private MunicipioFilialIntervCepService municipioFilialIntervCepService;
	private MunicipioFilialFilOrigemService municipioFilialFilOrigemService;
	private MunicipioFilialCliOrigemService municipioFilialCliOrigemService;
	private MunicipioFilialUFOrigemService municipioFilialUFOrigemService;
	private MunicipioService municipioService;
	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private VigenciaService vigenciaService;
	private ConfiguracoesFacade configuracoesFacade;

	private MunicipioFilialJdbcDAO municipioFilialJdbcDAO;

	/**
	 * Recupera uma instância de <code>MunicipioFilial</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return um Map.
	 */
	public List findByIdCustomizado(java.lang.Long id) {
		List municipioFilial = getMunicipioFilialDAO().findByIdCustomizado(id);
		return municipioFilial;
	}
	
	/**
	 * Recupera uma instância de <code>MunicipioFilial</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return um Map.
	 */
	public MunicipioFilial findById(java.lang.Long id) {
		MunicipioFilial municipioFilial = (MunicipioFilial) super.findById(id);
		return municipioFilial;
	}
	
	private TypedFlatMap transformTupleFindPaginated(Map map) {
		AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();

		DomainValue tpIdentificacao = (DomainValue)map.get("filial_empresa_pessoa_tpIdentificacao");
		String nrIdentificacao = (String)map.get("filial_empresa_pessoa_nrIdentificacao");

		TypedFlatMap retorno = a.transformeTupleMap(map);

		if (tpIdentificacao != null && StringUtils.isNotBlank(nrIdentificacao)) {
			retorno.put("filial.empresa.pessoa.nrIdentificacao",
					FormatUtils.formatIdentificacao(tpIdentificacao.getValue(),nrIdentificacao));
		}

		return retorno;
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		ResultSetPage rsp = getMunicipioFilialDAO().findPaginatedCustom(criteria,FindDefinition.createFindDefinition(criteria));

		FilterResultSetPage frsp = new FilterResultSetPage(rsp) {
			@Override
			public Map filterItem(Object item) {
				return transformTupleFindPaginated((Map)item);
			}
		};

		return (ResultSetPage)frsp.doFilter();
	}
	
	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getMunicipioFilialDAO().getRowCountCustom(criteria);
	}
	
	public List findLookupAsPaginated(TypedFlatMap criteria) {
		List l = getMunicipioFilialDAO().findLookupAsPaginated(criteria);
		if (l.size() == 1) {
			TypedFlatMap newRow = transformTupleFindPaginated((Map)l.get(0));
			l.set(0,newRow);
		}
		return l;
	}
	
	
	public List findMunicipioFilialByFilial(Long idFilial,YearMonthDay dtVigenciaFinal) {
		return getMunicipioFilialDAO().findMunicipioFilialByFilial(idFilial,dtVigenciaFinal);
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

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 * verifica a BL_RESTRICAO_ATENDIMENTO para o município (tabela MUNICIPIO_FILIAL) e que esteja vigente. 
	 * Regra 3.2. Se exitir as seguintes excessoes deverao ser lancadas:LMS-29087,LMS-29088
	 */
	public java.io.Serializable store(MunicipioFilial municipioFilial, Boolean blVerificaRestricaoAtendimento) {
		vigenciaService.validaVigenciaBeforeStore(municipioFilial);

		if (getMunicipioFilialDAO().verificaVigencia(municipioFilial)){
			throw new BusinessException("LMS-00003");
		}

		if(Boolean.TRUE.equals(blVerificaRestricaoAtendimento)) {
			if(Boolean.TRUE.equals(municipioFilial.getBlRestricaoAtendimento())) {
				if(getMunicipioFilialDAO().findMunicipioFilialByBlRestricaoAtendimento(municipioFilial)) {
					throw new BusinessException("LMS-29088");
				}
			}
		}

		Municipio municipio = municipioService.findById(municipioFilial.getMunicipio().getIdMunicipio());
		Pais pais = municipio.getUnidadeFederativa().getPais();
		if(pais.getBlCepOpcional() || pais.getBlCepDuplicado() || pais.getBlCepAlfanumerico()) {
			throw new BusinessException("LMS-29010");
		}

		return super.store(municipioFilial);
	}
	
	// criado para manter compatibilidade com comportamento
	// implementado na classe atualizarTrocaFilial que 
	// acabava chamando o método da CrudService 'protected store'
	// por estar no mesmo pacote, isso garante q os método beforeStore...
	// serão chamados
	protected Serializable store(MunicipioFilial mf) {
		return super.store(mf);
	}

	@Override
	protected MunicipioFilial beforeUpdate(MunicipioFilial bean) {
		MunicipioFilial municipioFilial = (MunicipioFilial)bean;

		if(municipioFilial.getDtVigenciaFinal() != null)
			verificaAtendimentosEspecificosNaoFinalizados(municipioFilial.getIdMunicipioFilial(), municipioFilial.getDtVigenciaFinal());

		if(municipioFilial.getBlRestricaoAtendimento().equals(Boolean.FALSE)) {
			Long idMunicipioFilial = municipioFilial.getIdMunicipioFilial();
			boolean uf = municipioFilialUFOrigemService.findUfByMunFil(idMunicipioFilial);
			if(findRelacoesByMunicipioFilial(idMunicipioFilial) || uf){
				throw new BusinessException("LMS-29093");
			}
		}

		return super.beforeUpdate(bean);
	}

	/**
	 * Verifica se existe alguma restricao do atendimento com vigencia superior a vigencia final informada
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal
	 */
	private void verificaAtendimentosEspecificosNaoFinalizados(Long idMunicipioFilial, YearMonthDay dtVigenciaFinal) {
		String chave = null;

		if (municipioFilialIntervCepService.existsMunicipioFilialIntervaloCepVigente(idMunicipioFilial, dtVigenciaFinal)) {
			chave = "cepAtendidos";
		} else if (municipioFilialCliOrigemService.existsMunicipioFilialClienteOrigemVigente(idMunicipioFilial, dtVigenciaFinal)) {
			chave = "remetentesAtendidos";
		} else if (municipioFilialFilOrigemService.validateExisteMunicipioFilialVigente(idMunicipioFilial, dtVigenciaFinal)) {
			chave = "filiaisOrigem";
		} else if (municipioFilialUFOrigemService.validateExisteMunicipioFilialVigente(idMunicipioFilial, dtVigenciaFinal)) {
			chave = "ufsOrigem";
		} else if (municipioFilialSegmentoService.validateExisteMunicipioFilialVigente(idMunicipioFilial, dtVigenciaFinal)) {
			chave = "segmentosAtendidos";
		} else if (operacaoServicoLocalizaService.findOperacaoServicoPorMunicipioFilial(idMunicipioFilial, dtVigenciaFinal, null) != null) {
			chave = "frequencias";
		}

		if (chave != null) {
			throw new BusinessException("LMS-29105", new Object[]{configuracoesFacade.getMensagem(chave)});
		}	
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMunicipioFilialDAO(MunicipioFilialDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private MunicipioFilialDAO getMunicipioFilialDAO() {
		return (MunicipioFilialDAO) getDao();
	}

	protected MunicipioFilial beforeStore(MunicipioFilial bean) {
		MunicipioFilial municipioFilial = (MunicipioFilial) bean;

		filialService.verificaExistenciaHistoricoFilial(
				municipioFilial.getFilial().getIdFilial(),
				municipioFilial.getDtVigenciaInicial(),
				municipioFilial.getDtVigenciaFinal());

		if (municipioFilial.getBlPadraoMcd().equals(Boolean.TRUE)) {
			if (getMunicipioFilialDAO().verificaMCDVigente(municipioFilial)) {
				throw new BusinessException("LMS-29038");
			}
		}
		return super.beforeStore(bean);
	}

	/**
	 * Estoura exceção quando atendimento não for vigente.
	 * 
	 * @param idMunicipioFilial
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 */
	public void validateVigenciaAtendimento(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		if (!isMunicipioFilialVigente(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal))
			throw new BusinessException("LMS-29022");
	}

	/**
	 * Verifica se existe algum atendimento numa vigencia futura para os paramentros inforamdos
	 * @param idMunicipio
	 * @param idFilial
	 * @return TRUE se existe algum atendimento, FALSE caso contrario
	 */
	public boolean verificaExisteMunicipioFilialVigenciaFutura(Long idMunicipio, Long idFilial) {
		return getMunicipioFilialDAO().verificaExisteMunicipioFilialVigenciaFutura(idMunicipio, idFilial);
	}

	/**
	 * A função abaixo executa a mesma rotina que a função acima, porem sem dar
	 * o bussinesException, deixando assim mais flexivel.
	 * 
	 * @author Samuel Herrmann
	 * @param idMunicipioFilial Id que voce deseja verificar se sua vigencia esta valída
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return boolean (true se estiver vigente e false se não)
	 */
	public boolean isMunicipioFilialVigente(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getMunicipioFilialDAO().isMunicipioFilialVigente(
				idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
	}

	protected void beforeRemoveByIds(List ids) {
		MunicipioFilial bean = null;
		for (Iterator ie = ids.iterator(); ie.hasNext();) {
			bean = findById((Long) ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);			
		}
		super.beforeRemoveByIds(ids);
	}

	protected void beforeRemoveById(Long id) {
		List list = new ArrayList();
		list.add(id);
		beforeRemoveByIds(list);
	}

	/**
	 * Retorna o MunicipioFilial usando o Id do Municipio
	 * @param idMunicipio
	 */
	public List findMunicipioFilialByIdMunicipioByIdFilial(Long idMunicipio, Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(MunicipioFilial.class);
		dc.add( Restrictions.eq("municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));
		
		return getMunicipioFilialDAO().getAdsmHibernateTemplate().findByDetachedCriteria(dc);	
	}

	/**
	 * Retorna o MunicipioFilial usando o Id do Municipio
	 * @param idMunicipio
	 */
	public MunicipioFilial findMunicipioFilial(Long idMunicipio, Long idFilial) {
		return getMunicipioFilialDAO().findMunicipioFilial(idMunicipio, idFilial);	
	}

	/**
	 * Retorna lista de municipios atendidos vigentes (ou com vigencia futura) a partir do municipio informado
	 * @param idMunicipio
	 * @return
	 */
	public List findMunicipioFilialVigenteByMunicipio(Long idMunicipio){
		return getMunicipioFilialDAO().findMunicipioFilialVigenteByMunicipio(idMunicipio);
	}

	/**
	 * Retorna o municipio filial vigente 
	 * 
	 * @param idMunicipio
	 * @param idFilial
	 * @return
	 */
	public List findMunicipioFilialVigente(Long idMunicipio, Long idFilial){
		return getMunicipioFilialDAO().findMunicipioFilialVigente(idMunicipio,idFilial);
	}	

	/**
	 * Retorna lista de municipioFilial (pojos) atendidos vigentes na data atual a partir do nome do municipio informado
	 * @param nmMunicipio
	 * @return
	 */
	public List findMunicipioFilialVigenteByNmMunicipio(String nmMunicipio, boolean pesquisaExata){
		return getMunicipioFilialDAO().findMunicipioFilialVigenteByNmMunicipio(nmMunicipio, pesquisaExata);
	}

	/**
	 * Retorna lista de municipioFilial (pojos) atendidos vigentes na data atual a partir do id do municipio informado.
	 * @param idMunicipio
	 * @return
	 */
	public List findMunicipioFilialVigenteByIdMunicipio(Long idMunicipio){
		return getMunicipioFilialDAO().findMunicipioFilialVigenteByIdMunicipio(idMunicipio);
	}

	/**
	 * Retorna uma lista de Municípios vigentes na data atual ou com vigencia futura para a filial informada
	 * @param idMunicipio
	 * @return
	 */
	public List findMunicipioFilialVigenteByFilial(Long idFilial){
		List result = getMunicipioFilialDAO().findMunicipioFilialVigenteByFilial(idFilial);
		List newResult = new ArrayList();
		String label = configuracoesFacade.getMensagem("atendimentoRestrito");

		if (!result.isEmpty()) {
			for(Iterator it = result.iterator(); it.hasNext();) {
				Map municipioFilial = (Map) it.next();
				String siglaNmMunicipio = (String) municipioFilial.get("nmMunicipio");

				Boolean blRestricaoAtendimento = (Boolean) municipioFilial.get("blRestricaoAtendimento");
				Boolean blAtendimentoGeral = (Boolean) municipioFilial.get("blAtendimentoGeral");

				if (blRestricaoAtendimento.booleanValue() || !blAtendimentoGeral.booleanValue()) {
					siglaNmMunicipio += " - ("+label+")";
				}

				municipioFilial.put("siglaMnMunicipio",siglaNmMunicipio);
				newResult.add(municipioFilial);
			}
		}
		return newResult;
	}

	public Integer findDistanciaTotalMunicipioFilial(Long idMunicipioFilial){
		return getMunicipioFilialDAO().findDistanciaTotalMunicipioFilial(idMunicipioFilial);
	}

	/*verifica se municipioFilial possui registro nas seguintes tabelas:MUNICIPIO_FILIAL_INTERV_CEP, MUNICIPIO_FILIAL_FIL_ORIGEM, MUNICIPIO_FILIAL_SEGMENTO, MUNICIPIO_FILIAL_CLI_ORIGEM
	 * se existir relacionamento em alguma delas retorna true senao retorna false
	 */ 

	public boolean findRelacoesByMunicipioFilial(Long idMunicipioFilial) {
		boolean cliente = municipioFilialCliOrigemService.existsMunicipioFilialClienteOrigem(idMunicipioFilial);
		boolean segmento = municipioFilialSegmentoService.findSegmentoByMunFil(idMunicipioFilial);
		boolean filial = municipioFilialFilOrigemService.findFilialByMunFil(idMunicipioFilial);
		boolean intervCep = municipioFilialIntervCepService.existsMunicipioFilialIntervaloCep(idMunicipioFilial);
		boolean uf = municipioFilialUFOrigemService.findUfByMunFil(idMunicipioFilial);
		if(cliente || segmento || filial || intervCep || uf)
			return true;
		return false;
			
	}
	/*
	 * verifica se o municipioFilial tem restricao de atendimento = "S"
	 * se possui relacionamento
	 */
	public boolean findRestricaoAtendimentoERelacoesByMunicipioFilial(Long idMunicipioFilial){
		boolean blRestricao = getMunicipioFilialDAO().findMunicipioFilialByBlRestricaoAtendimento(idMunicipioFilial);
		boolean relacoes = findRelacoesByMunicipioFilial(idMunicipioFilial);
		if(blRestricao && !relacoes)
			return true;
		return false;
	}
	
	/**
	 * Consulta atendimentos vigentes na data corrente a partir dos parametros informados
	 * @param idMunicipioOrigem
	 * @param idServico
	 * @param tpOperacao
	 * @return
	 */
	public List findAtendimentosVigentesByMunicipioServicoOperacao(Long idMunicipioOrigem, Long idServico, String tpOperacao){
		return getMunicipioFilialDAO().findAtendimentosVigentesByMunicipioServicoOperacao(idMunicipioOrigem, idServico, tpOperacao);
	}
	
	/**
	 * Consulta os atendimentos a partir dos parametros informados 
	 * @param idMunicipio
	 * @param indicadorColeta
	 * @param idServico
	 * @param dtVigencia
	 * @return
	 * @deprecated
	 * @see OperacaoServicoLocalizaService.findOperacaoServicoLocaliza(Long idServico, Boolean blIndicadorColeta);
	 */
	public List findAtendimentosVigentesByMunicipioServicoOperacao(Long idMunicipio, Boolean indicadorColeta, Long idServico, YearMonthDay dtVigencia){
		return getMunicipioFilialDAO().findAtendimentosVigentesByMunicipioServicoOperacao(idMunicipio, indicadorColeta, idServico, dtVigencia);
	}

	/**
	 * Retorna o id do atendimento padrao MCD vigente para o municipio informado
	 * @param idMunicipio
	 * @return o id do MunicipioXFilial encontrado ou null caso nao seja encontrado
	 */
	public Object[] findMunicipioFilialVigenteByMunicipioPadraoMCD(Long idMunicipio){
		return getMunicipioFilialDAO().findMunicipioFilialVigenteByMunicipioPadraoMCD(idMunicipio);
	}
	
	public Map findFilialByMunicipioNrCep(Long idMunicipio, String nrCep) {
		return getMunicipioFilialDAO().findFilialByMunipioNrCep(idMunicipio, nrCep);
	}
	
	public List<MunicipioFilial> findFilialMercurioByIdMunipio(Long idMunicipio) {					
		Empresa empresa = empresaService.findEmpresaMercurio();
		return getMunicipioFilialDAO().findMunicipioFilialByIdMunipioIdEmpresa(idMunicipio,empresa.getIdEmpresa());		
	}
	
	/**
	 * Chama o método respectivo do DAO
	 * 
	 * autor Pedro Henrique Jatobá
	 * 26/12/2005
	 * @param idMunicipio
	 * @param nrCep
	 * @return
	 */
	public Map findFilialDadosByMunicipioNrCep(Long idMunicipio, String nrCep) {
		return getMunicipioFilialDAO().findFilialDadosByMunipioNrCep(idMunicipio, nrCep);
	}
		
	/**
	 * Consulta a menor data de vigencia inicial e a maior data de vigencia dos atendimentos existentes para o municipio 
	 * 
	 * @param idMunicipio
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public Map findIntervaloVigenciaByMunicipio(Long idMunicipio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		return getMunicipioFilialDAO().findIntervaloVigenciaByMunicipio(idMunicipio, dtVigenciaInicial, dtVigenciaFinal);
	}


	/**
	 * Retorna o Municipio da filial encontrado através do idFranquia/Filial, idMunicipioColetaEntrega e data de vigência
	 * 
	 * @param idFranquia
	 * @param idMunicipioColetaEntrega
	 * @param dtInicioCompetencia
	 * @return MunicipioFilial
	 */
	public MunicipioFilial findByIdFranquiaMunicipioVigencia(long idFranquia, long idMunicipioColetaEntrega, YearMonthDay dtInicioCompetencia) {
		return getMunicipioFilialDAO().findByIdFranquiaMunicipioVigencia(idFranquia, idMunicipioColetaEntrega, dtInicioCompetencia);
	}
	
	// LMSA-6786
	public Municipio findIdMunicipioFromViewV_CEP(String numeroCEP) {
		Municipio result = null;
		Long idMunicipio = municipioFilialJdbcDAO.findIdMunicipioFromViewV_CEP(numeroCEP);
		if (idMunicipio != null) {
			result = municipioService.findById(idMunicipio);
		}
		return result;
	}
	
	
	/**
	 * Consulta todos os atendimentos padrao MCD vigentes na data informada, fazendo 
	 * um produto cartesiano com todos os servicos que geram MCD
	 * @param dtVigencia
	 * @return
	 */
	public List findMunicipiosFilialDestinoMCD(YearMonthDay dtVigencia, Long idServico){
		return getMunicipioFilialDAO().findMunicipiosFilialDestinoMCD(dtVigencia, idServico);
	}

	public Integer getRowCountByIdMunicipioByIdFilial(Long idMunicipio, Long idFilial) {
		return getMunicipioFilialDAO().getRowCountByIdMunicipioByIdFilial(idMunicipio, idFilial); 
	}


	public Integer getRowCountByIdMunicipioBlRecebeColeta(Long idMunicipio, Boolean blRecebeColetaEventual) {
		return getMunicipioFilialDAO().getRowCountByIdMunicipioBlRecebeColeta(idMunicipio, blRecebeColetaEventual);
	}

	public Integer getRowCountMunicipioFilial(Long idMunicipio, Boolean blRecebeColetaEventual, Boolean blPadraoMcd, YearMonthDay dtVigencia) {
		return getMunicipioFilialDAO().getRowCountMunicipioFilial(idMunicipio, blRecebeColetaEventual, blPadraoMcd, dtVigencia);
	}

	public List findMunicipioFilialVigenteAtualByMunicipio(Long idMunicipio){
		return getMunicipioFilialDAO().findMunicipioFilialVigenteAtualByMunicipio(idMunicipio);
	}

	//metodos de paginação da Tela Consultar Municipios
	public ResultSetPage findPaginatedMunicipioFilialByMunicipio(Map criteria) {
		return getMunicipioFilialDAO().findPaginatedMunicipioFilialVigenteByMunicipio(criteria,FindDefinition.createFindDefinition(criteria));
	}
	
	public Integer getRowCountMunicipioFilialByMunicipio(Long idMunicipio) {
		return getMunicipioFilialDAO().getRowCountMunicipioFilialVigenteByMunicipio(idMunicipio);
	}

	/**verifica se o municipio esta vigente na data atual de acordo com o idFilial e idMunicipio
	 * 
	 * @param idFilial,idMunicipio
	 * @return boolean - Retorna true se a filial estiver vigente para o municipio em questao na data atual, caso contrario 
	 * retorna false
	 */
	public boolean verificaVigenciaMunicipioByFilialMunicipio(Long idFilial,Long idMunicipio){
		return getMunicipioFilialDAO().verificaVigenciaMunicipioByFilialMunicipio(idFilial,idMunicipio);
	}

	/**
	 * Verifica se Municipio passado pertence a alguma Filial da Matriz.
	 * @param idMunicipio
	 * @return
	 */
	public Boolean isMunicipioFilialMatriz(Long idMunicipio) {
		return getMunicipioFilialDAO().isMunicipioFilialMatriz(idMunicipio);
	}

	public List findListMunicipiosFilialComUF(){
		return getMunicipioFilialDAO().findListMunicipiosFilialComUF();
	}
	
	public List findMunicipiosComRestricaoTransporte(TypedFlatMap criteria){
		return getMunicipioFilialDAO().findMunicipiosComRestricaoTransporte(criteria);
	}

	public MunicipioFilial findMunicipioByJdbcForFranqueado(long idFranquia, long idMunicipioColetaEntrega, YearMonthDay dtInicioCompetencia) {
		return municipioFilialJdbcDAO.findMunicipioByJdbcForFranqueado(idFranquia, idMunicipioColetaEntrega, dtInicioCompetencia);
	}
	
	public List<MunicipioFilial> findMunicipioByJdbcForFranqueado(long idFranquia, YearMonthDay dtInicioCompetencia) {
		return municipioFilialJdbcDAO.findMunicipioByJdbcForFranqueado(idFranquia, dtInicioCompetencia);
	}

	public List<Municipio> findMunicipioByIdFilialParaImportador(Long idFilial) {
		return getMunicipioFilialDAO().findMunicipioByIdFilialParaImportador(idFilial);
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	public void setMunicipioFilialCliOrigemService(MunicipioFilialCliOrigemService municipioFilialCliOrigemService) {
		this.municipioFilialCliOrigemService = municipioFilialCliOrigemService;
	}
	public void setMunicipioFilialFilOrigemService(MunicipioFilialFilOrigemService municipioFilialFilOrigemService) {
		this.municipioFilialFilOrigemService = municipioFilialFilOrigemService;
	}
	public void setMunicipioFilialIntervCepService(MunicipioFilialIntervCepService municipioFilialIntervCepService) {
		this.municipioFilialIntervCepService = municipioFilialIntervCepService;
	}
	public void setMunicipioFilialSegmentoService(MunicipioFilialSegmentoService municipioFilialSegmentoService) {
		this.municipioFilialSegmentoService = municipioFilialSegmentoService;
	}
	public void setMunicipioFilialUFOrigemService(MunicipioFilialUFOrigemService municipioFilialUFOrigemService) {
		this.municipioFilialUFOrigemService = municipioFilialUFOrigemService;
	}
	public MunicipioService getMunicipioService() {
		return this.municipioService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setOperacaoServicoLocalizaService(OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setMunicipioFilialJdbcDAO(MunicipioFilialJdbcDAO municipioFilialJdbcDAO) {
		this.municipioFilialJdbcDAO = municipioFilialJdbcDAO;
	}
}