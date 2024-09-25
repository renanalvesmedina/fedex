package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.RegiaoColetaEntregaFil;
import com.mercurio.lms.municipios.model.RegiaoFilialRotaColEnt;
import com.mercurio.lms.municipios.model.RotaIntervaloCep;
import com.mercurio.lms.municipios.model.dao.MunicipioDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.municipios.municipioService"
 */
public class MunicipioService extends CrudService<Municipio, Long> {
	private static final int CD_IBGE_SIZE = 5;
	private RegiaoColetaEntregaFilService regiaoColetaEntregaFilService;
	private PaisService paisService;
	private ConfiguracoesFacade configuracoesFacade;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		ResultSetPage rsp = getMunicipioDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));		
		List<Map<String, Object>> list = AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList());
		rsp.setList(list);
		return rsp;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> findZonaPaisUFByIdMunicipio(Long idMunicipio){
		return getMunicipioDAO().findZonaPaisUFByIdMunicipio(idMunicipio);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getMunicipioDAO().getRowCount(criteria);
		return rowCountCustom;
	}

	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedConsultaMunicipios(TypedFlatMap criteria) {
		ResultSetPage rs = getMunicipioDAO().findPaginatedConsultarMunicipios(criteria, FindDefinition.createFindDefinition(criteria));
		return rs;
	}

	public Integer getRowCountConsultaMunicipios(TypedFlatMap criteria) {
		Integer rowCountCustom = this.getMunicipioDAO().getRowCountConsultaMunicipios(criteria);
		return rowCountCustom;
	}

	protected Municipio beforeStore(Municipio bean) {
		Municipio municipio = (Municipio)bean;

		Long idMunicipio = municipio.getIdMunicipio();

		Pais pais = paisService.findById(municipio.getUnidadeFederativa().getPais().getIdPais());

		String nrCep = municipio.getNrCep();

		if(StringUtils.isBlank(nrCep)) {
			if(!pais.getBlCepOpcional()) {
				throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("cep")});
			}
		} else {
			if(!pais.getBlCepDuplicado()) {
				if(this.getMunicipioDAO().findCepByPais(municipio.getUnidadeFederativa().getPais().getIdPais(), nrCep, idMunicipio)) {
					throw new BusinessException("LMS-29167");
				}
			}
			if(!pais.getBlCepAlfanumerico()) {
				if(!StringUtils.isNumeric(nrCep)) {
						throw new BusinessException("LMS-29121");
				}
				if (!this.getMunicipioDAO().validaIntervaloCep(municipio.getUnidadeFederativa().getIdUnidadeFederativa(), nrCep)) {
					throw new BusinessException("LMS-29015");
				}
			}
		}

		if (("BRA".equals(pais.getSgPais())) && ((municipio.getCdIbge() == null))) {
			throw new BusinessException("LMS-29108");
		}

		if ("BRA".equals(pais.getSgPais()) && municipio.getCdSiafi() == null) {
			throw new BusinessException("LMS-29184");
		}

		if(municipio.getMunicipioDistrito() != null
			&& !municipio.getBlDistrito()) {
			municipio.setBlDistrito(Boolean.TRUE);
		}

		if(municipio.getBlDistrito()) {
			if(municipio.getMunicipioDistrito() == null) {
				throw new BusinessException("requiredField", new Object[]{configuracoesFacade.getMensagem("municDistrito")});
			}
			if(idMunicipio != null
				&& municipio.getIdMunicipio().equals(municipio.getMunicipioDistrito().getIdMunicipio())
			) {
				throw new BusinessException("LMS-29049");
			}
		}

		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>Municipio</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Municipio findById(java.lang.Long id) {
		return (Municipio) super.findById(id);
	}
	
	public Municipio findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (Municipio)super.findByIdInitLazyProperties(id, initializeLazyProperties);
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
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
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
	 */
	public java.io.Serializable store(Municipio bean) {
		return super.store(bean);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findByIdCustom(Long idMunicipio){
		return getMunicipioDAO().findByIdCustom(idMunicipio);
	}

	@SuppressWarnings("unchecked")
	public String findCdIbgeByPessoa(Long idPessoa){
	    Map<String,Object> mapIbge = (Map<String, Object>) getMunicipioDAO().findCdIbgeByPessoa(idPessoa).get(0);
	    if (mapIbge == null || !mapIbge.containsKey("cdIbgeMunicipio") ||!mapIbge.containsKey("cdIbgeUF")){
	        return null;
	    }
	    
	    try{
	        return mapIbge.get("cdIbgeUF").toString().concat(FormatUtils.fillNumberWithZero(mapIbge.get("cdIbgeMunicipio").toString(),CD_IBGE_SIZE));
	    }catch (Exception e){
	    	log.warn(e.getMessage());
	        return null;
	    }
	}
	
	/**
	 * Retorna o id do municipio do endereço padrão (pessoa.enderecoPessoa) da pessoa informada
	 * 
	 * @author Mickaël Jalbert
	 * @since 14/08/2006
	 * 
	 * @param Long idPessoa
	 * @return Long
	 */	
	public Long findIdMunicipioByPessoa(Long idPessoa){
		return getMunicipioDAO().findIdMunicipioByPessoa(idPessoa);
	}	

	public boolean isMunicipioAtivo(Long idMunicipio) {
		return getMunicipioDAO().isMunicipioAtivo(idMunicipio);
	}

	@SuppressWarnings("rawtypes")
	public Map findDadosMunicipioById(Long id) {
		return getMunicipioDAO().findDadosMunicipioById(id);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMunicipioLookup(Map<String, Object> criteria){
		String nmMunicipio = (String)criteria.get("nmMunicipio");
		if(StringUtils.isNotBlank(nmMunicipio))
			return getMunicipioDAO().findByNmMunicipioTpSituacao(nmMunicipio, "A");
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findByNmMunicipioTpSituacao(String nmMunicipio, String tpSituacao) {
		return getMunicipioDAO().findByNmMunicipioTpSituacao(nmMunicipio,tpSituacao);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findByNmMunicipioBySgUfByTpSituacao(String nmMunicipio, String sgUnidadeFederativa, String tpSituacao){
		return getMunicipioDAO().findByNmMunicipioBySgUfByTpSituacao(nmMunicipio, sgUnidadeFederativa, tpSituacao);
	}
	
	public List<Municipio> findByIdUnidadeFederativa(Long idUnidadeFederativa){
		return getMunicipioDAO().findByIdUnidadeFederativa(idUnidadeFederativa);
	}
	
	public List<Municipio> findByIdUnidadeFederativaComDistrito(Long idUnidadeFederativa){
		return getMunicipioDAO().findByIdUnidadeFederativaComDistrito(idUnidadeFederativa);
	}
	
	public Municipio findByNmMunicipioAndUf(String nmMunicipio, String sgUnidadeFederativa){
		return getMunicipioDAO().findByNmMunicipioAndUf(nmMunicipio, sgUnidadeFederativa);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findByNmMunicipioTpSituacaoPaisUf(String nmMunicipio, Long idPais, Long idUf, String tpSituacao) {
		return getMunicipioDAO().findByNmMunicipioTpSituacaoPaisUf(nmMunicipio,idPais,idUf,tpSituacao);
	}

	@SuppressWarnings("unchecked")
	public List<Municipio> findByMunicipioFilial(Map<String, Object> criteria) {
		Long idFilial = MapUtils.getLong(MapUtils.getMap(criteria, "filial"), "idFilial");
		if (idFilial != null)
			return getMunicipioDAO().findByMunicipioFilial(idFilial);
		else return null;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findLookupMunicipio(TypedFlatMap criteria){
		return getMunicipioDAO().findLookupMunicipio(criteria);
	}
	
	public Municipio findMunicipioByFilial(Long idFilial){
		return getMunicipioDAO().findMunicipioByFilial(idFilial);
	}

	/**
	 * Método para buscar os municpios a partir da regiaoColetaEntregaFil 
	 * @param idRegiaoColeta
	 * @return List com os municipios da regiao informada
	 * @author Rodrigo Antunes
	 */
	@SuppressWarnings("unchecked")
	public List<Municipio> findMunicipiosByRegiaoColetaEntregaFil(Long idRegiaoColetaEntregaFil) {
		List<Municipio> municipios = new ArrayList<Municipio>();
		// carrega a RegiaoColetaEntregaFil
		RegiaoColetaEntregaFil regiaoColetaEntregaFil = regiaoColetaEntregaFilService.findById(idRegiaoColetaEntregaFil);
		//pega a lista de RegiaoFilialRotaColEnts
		List<RegiaoFilialRotaColEnt> regioesFilialRotaColetaEntrega = regiaoColetaEntregaFil.getRegiaoFilialRotaColEnts();
		if(regioesFilialRotaColetaEntrega != null) {
			for (RegiaoFilialRotaColEnt regiaoFilialRotaColEnt : regioesFilialRotaColetaEntrega) {
				// busca as rotasIntervaloCeps, aonde irá pegar o municipio de cada rotaIntervaloCep
				List<RotaIntervaloCep> rotaIntervaloCepList = regiaoFilialRotaColEnt.getRotaColetaEntrega().getRotaIntervaloCeps();
				municipios = findMunicipiosFromRotaIntervaloCepList(rotaIntervaloCepList);
			}
		}
		return municipios;
	}

	/**
	 * Método para buscar os municipios a partir da RotaColetaEntrega
	 * @param idRotaColetaEntrega
	 * @return List com os municipios da RotaColetaEntrega informada
	 * @author Rodrigo Antunes
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findMunicipiosByRotaColetaEntrega(Long idRotaColetaEntrega) {
		return getMunicipioDAO().findMunicipiosByRotaColetaEntrega(idRotaColetaEntrega);
	}

	/**
	 * Método para buscar os municipios a partir da lista de rotaIntervaloCep
	 * @param rotaIntervaloCepList
	 * @return List com os municipios da lista de rotaIntervaloCepList
	 */
	private List<Municipio> findMunicipiosFromRotaIntervaloCepList(List<RotaIntervaloCep> rotaIntervaloCepList) {
		List<Municipio> municipios = new ArrayList<Municipio>();
		if(rotaIntervaloCepList != null) {
			for (RotaIntervaloCep rotaIntervaloCep : rotaIntervaloCepList) {
				municipios.add(rotaIntervaloCep.getMunicipio());
			}
		}
		return municipios;
	}

	/**
	 * @see getMunicipioDAO().findMunicipioByCep(nrCep)
	 * @param findMunicipioByCep
	 * @return
	 */
	public Municipio findMunicipioByCep(String nrCep){
		return getMunicipioDAO().findMunicipioByCep(nrCep);
	}
	
	/**
	 * Método utilizado pela integração
	 * CQPRO00006784
	 * 
	 * @param idFiliais
	 * @param tpOperacoes
	 * @param idTipoLocalizacaoMunicipio
	 * @return Lista de municípios
	 */
	public List<Municipio> findMunicipiosByFiliais(
			List<Long> idFiliais,
			List<String> tpOperacoes,
			Long idTipoLocalizacaoMunicipio 
	) {
		return getMunicipioDAO().findMunicipiosByFiliais(idFiliais, tpOperacoes, idTipoLocalizacaoMunicipio);
	}

	/**
	 * Método utilizado pela Integração (DBI)
	 * Consulta o município através de um intervalo de cep e Unidade Federativa.
	 * Retorna o município que abranger o cep passado como parâmetro. 
	 * @param nrCep
	 * @param idUnidadeFederativa
	 * @return Municipio
	 */
	public Municipio findByIntervaloCepByUf(String nrCep, Long idUnidadeFederativa){
		return getMunicipioDAO().findByIntervaloCepByUf(nrCep, idUnidadeFederativa);
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMunicipioDAO(MunicipioDAO dao) {
		setDao( dao );
	}
	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private final MunicipioDAO getMunicipioDAO() {
		return (MunicipioDAO) getDao();
	}
	public void setRegiaoColetaEntregaFilService(RegiaoColetaEntregaFilService regiaoColetaEntregaFilService) {
		this.regiaoColetaEntregaFilService = regiaoColetaEntregaFilService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	/**
	 * Efetua pesquisa para a suggest de município.
	 * 
	 * @param idPais
	 * @param idUnidadeFederativa
	 * @param nmMunicipio
	 * @param limiteRegistros
	 * 
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> findMunicipioSuggest(Long idPais, Long idUnidadeFederativa, String nmMunicipio, Integer limiteRegistros) {
		return getMunicipioDAO().findMunicipioSuggest(idPais, idUnidadeFederativa, nmMunicipio, limiteRegistros);
	}
	
	public Long findIdMunicipioByIdFilial(Long idFilial){
		return getMunicipioDAO().findIdMunicipioByIdFilial(idFilial);
	}
	
}