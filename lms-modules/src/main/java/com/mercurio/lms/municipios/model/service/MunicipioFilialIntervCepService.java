package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.MunicipioFilialIntervCep;
import com.mercurio.lms.municipios.model.dao.IntervaloCepDAO;
import com.mercurio.lms.municipios.model.dao.MunicipioFilialIntervCepDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.municipioFilialIntervCepService"
 */
public class MunicipioFilialIntervCepService extends CrudService<MunicipioFilialIntervCep, Long> {
	private MunicipioFilialService municipioFilialService; 
	private VigenciaService vigenciaService;
	private IntervaloCepDAO intervaloCepDAO;

	public ResultSetPage findPaginated(Map criteria) {
		List included = new ArrayList();
		included.add("idMunicipioFilialIntervCep");
		included.add("nrCepInicial");
		included.add("nrCepFinal");
		included.add("dtVigenciaInicial");
		included.add("dtVigenciaFinal");
		included.add("municipioFilial.municipio.unidadeFederativa.pais.sgPais");
 
		ResultSetPage rsp = super.findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));
		return rsp;
	}

	protected MunicipioFilialIntervCep beforeStore(MunicipioFilialIntervCep bean) {
		MunicipioFilialIntervCep munFilIntervaloCep = (MunicipioFilialIntervCep)bean;

		municipioFilialService.validateVigenciaAtendimento(munFilIntervaloCep.getMunicipioFilial().getIdMunicipioFilial(), munFilIntervaloCep.getDtVigenciaInicial(), munFilIntervaloCep.getDtVigenciaFinal());

		this.validaIntervaloCeps(munFilIntervaloCep.getNrCepInicial(), munFilIntervaloCep.getNrCepFinal());
		String cepInicialIntervalo = munFilIntervaloCep.getNrCepInicial();
		String cepFinalIntervalo = munFilIntervaloCep.getNrCepFinal();
		if(!intervaloCepDAO.verificaIntervaloCepsMunicipio(munFilIntervaloCep.getMunicipioFilial().getMunicipio().getIdMunicipio(),cepInicialIntervalo,cepFinalIntervalo))
			throw new BusinessException("LMS-29019");
		if(getMunicipioFilialIntervCepDAO().verificaIntervaloCepAtendido(munFilIntervaloCep))
			throw new BusinessException("LMS-00003");

		return super.beforeStore(bean);
	 }
	
	/**
	 * Consulta registros vigentes para o municipio X Filial informado
	 * @param idMunicipioFilial
	 * @param dtVigenciaFinal 
	 * @param dtVigenciaInicial 
	 * @return
	 */
	public List findIntervCepVigenteByMunicipioFilial(Long idMunicipioFilial, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		return getMunicipioFilialIntervCepDAO().findIntervCepVigenteByMunicipioFilial(idMunicipioFilial, dtVigenciaInicial, dtVigenciaFinal);
	}

	/**
	 * Verifica se existe registro para o atendimento e cep informados, dentro da vigencia informada
	 * @param idMunicipioFilial
	 * @param cep
	 * @param dtVigencia
	 * @return
	 */
	public boolean verificaExisteMunicipioFilialCep(Long idMunicipioFilial, String cep, YearMonthDay dtVigencia){
		return getMunicipioFilialIntervCepDAO().verificaExisteMunicipioFilialCep(idMunicipioFilial, cep, dtVigencia);
	}

	public boolean existsMunicipioFilialIntervaloCepVigente(Long idMunicipioFilial, YearMonthDay dtVigencia) {
		Integer nrRows = getMunicipioFilialIntervCepDAO().findRowCountMunicipioFilialIntervaloCep(idMunicipioFilial, dtVigencia);
		return CompareUtils.gt(nrRows, IntegerUtils.ZERO);
	}

	public boolean existsMunicipioFilialIntervaloCep(Long idMunicipioFilial) {
		Integer nrRows = getMunicipioFilialIntervCepDAO().findRowCountMunicipioFilialIntervaloCep(idMunicipioFilial, null);
		return CompareUtils.gt(nrRows, IntegerUtils.ZERO);
	}

	public boolean validateMunicipioVigenciaFutura(Long idMunicipio, Long idFilial, YearMonthDay dtVigencia) {
		return getMunicipioFilialIntervCepDAO().verificaMunicipioVigenciaFutura(idMunicipio, idFilial, dtVigencia);
	}

	/**
	 * Recupera uma instância de <code>MunFilIntervaloCep</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public MunicipioFilialIntervCep findById(java.lang.Long id) {
		return (MunicipioFilialIntervCep)super.findById(id);
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
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MunicipioFilialIntervCep bean) {
		return super.store(bean);
	}

	public Map storeMap(Map bean) {
		MunicipioFilialIntervCep municipioFilial = new MunicipioFilialIntervCep();
		ReflectionUtils.copyNestedBean(municipioFilial, bean);

		vigenciaService.validaVigenciaBeforeStore(municipioFilial);

		super.store(municipioFilial);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idMunicipioFilialIntervCep", municipioFilial.getIdMunicipioFilialIntervCep());
		retorno.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(municipioFilial));
		
		return retorno;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setMunicipioFilialIntervCepDAO(MunicipioFilialIntervCepDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private MunicipioFilialIntervCepDAO getMunicipioFilialIntervCepDAO() {
		return (MunicipioFilialIntervCepDAO) getDao();
	}

	public void validaIntervaloCeps(String cepInicial, String cepFinal) {
		if (cepFinal.compareTo(cepInicial) <0) 
			throw new BusinessException("LMS-29013");
	}

	protected void beforeRemoveByIds(List ids) {
		MunicipioFilialIntervCep bean = null;
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

	//busca todos os ceps vigentes de um municipio atendido 
	public List findCepAtendidoByMunicipioFilial(Long idMunicipioFilial) {
		return getMunicipioFilialIntervCepDAO().findCepAtendidoByMunicipioFilial(idMunicipioFilial);
	}


	//**************************GETTER AND SETTER*************************************//
	/**
	 * @param intervaloCepDAO The intervaloCepDAO to set.
	 */
	public void setIntervaloCepDAO(IntervaloCepDAO intervaloCepDAO) {
		this.intervaloCepDAO = intervaloCepDAO;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setMunicipioFilialService(MunicipioFilialService municipioFilialService) {
		this.municipioFilialService = municipioFilialService;
	}

}