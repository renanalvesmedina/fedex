package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.IntervaloCep;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.dao.IntervaloCepDAO;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.intervaloCepService"
 */
public class IntervaloCepService extends CrudService<IntervaloCep, Long> {
	MunicipioService municipioService;
	private PaisService paisService;

	public ResultSetPage findPaginated(Map criteria) {
		List<String> included = new ArrayList<String>();
		included.add("idIntervaloCep");
		included.add("municipio.nmMunicipio");
		included.add("municipio.nrCep");
		included.add("municipio.unidadeFederativa.sgUnidadeFederativa");
		included.add("municipio.unidadeFederativa.pais.nmPais");
		included.add("municipio.unidadeFederativa.pais.sgPais");
		included.add("municipio.blDistrito");
		included.add("municipio.municipioDistrito.nmMunicipio");
		included.add("nrCepInicial");
		included.add("nrCepFinal");	
		included.add("tpSituacao");
 
		ResultSetPage rsp = getIntervaloCepDAO().findPaginated(criteria);
		rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

		return rsp;
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#find(java.util.Map)
	 */
	public List find(Map criteria) {
		return super.find(criteria);
	}

	protected IntervaloCep beforeStore(IntervaloCep bean) {
		IntervaloCep intervaloCep = (IntervaloCep)bean;
		Municipio municipio = municipioService.findById(intervaloCep.getMunicipio().getIdMunicipio());
		Pais pais = municipio.getUnidadeFederativa().getPais();
		String nrCepInicial = intervaloCep.getNrCepInicial();
		String nrCepFinal = intervaloCep.getNrCepFinal();

		if(!pais.getBlCepAlfanumerico()) {
			if(!StringUtils.isNumeric(nrCepInicial) || !StringUtils.isNumeric(nrCepFinal)) {
				throw new BusinessException("LMS-29121");
			}
			if(CompareUtils.gt(LongUtils.getLong(nrCepInicial), LongUtils.getLong(nrCepFinal))) {
				throw new BusinessException("LMS-29013");
			}
		}

		if(!pais.getBlCepDuplicado()) {
			List<String> intervalosCep = getIntervaloCepDAO().verificaIntervaloCep(
					nrCepInicial,
					nrCepFinal,
					intervaloCep.getMunicipio().getIdMunicipio(),
					intervaloCep.getIdIntervaloCep()
			);
			if (intervalosCep.size() > 0) {
				StringBuilder municipios = new StringBuilder("\n");
				for(String valor : intervalosCep) {
					municipios.append("- ").append(valor).append("\n");
				}
				municipios.delete(municipios.length() - 1, municipios.length());
				throw new BusinessException("LMS-00029", new Object[]{municipios.toString()});
			}
		}

		if(getIntervaloCepDAO().verificaIntervaloCepUF(intervaloCep.getNrCepInicial(), intervaloCep.getNrCepFinal(), intervaloCep.getMunicipio().getIdMunicipio()))
			throw new BusinessException("LMS-29016");

		return super.beforeStore(bean);
	}

	/**
	 * Recupera uma instância de <code>IntervaloCep</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public IntervaloCep findById(java.lang.Long id) {
		return (IntervaloCep) super.findById(id);
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
	public java.io.Serializable store(IntervaloCep bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setIntervaloCepDAO(IntervaloCepDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private IntervaloCepDAO getIntervaloCepDAO() {
		return (IntervaloCepDAO) getDao();
	}

	public List findByMunicipio(Map criteria) {
		List<IntervaloCep> list = new ArrayList<IntervaloCep>(1);
		IntervaloCep intervaloCep = new IntervaloCep("99900","89090",null,null);
		list.add(intervaloCep);
		return list;
	}

	/**
     * CQPRO00023601
     * Valida se o cep é válido no terrítório nacional
     *
     * @return true ou false.
     */   
    public boolean validateCep(String nrCep) {
          String sgPais = "BRA";
          Pais pais = getPaisService().findPaisBySgPais(sgPais);
          return getIntervaloCepDAO().validateCep(FormatUtils.fillNumberWithZero(nrCep, 8), pais.getIdPais());
    }
	
	/**
	 * Valida se o intervalo de ceps informado esta dentro de algum intervalo de ceps do municipio
	 * @param nrCepInicial
	 * @param nrCepFinal
	 * @param idMunicipio
	 * @return TRUE se o intervalo esta dentro de algum intervalo do municipio, FALSE caso contrario
	 */
	public boolean validaIntervalo(String nrCepInicial, String nrCepFinal, Long idMunicipio) {
		return getIntervaloCepDAO().validaIntervalo(nrCepInicial, nrCepFinal, idMunicipio);
	}

	public List findIntervaloCepByMunicipio(Long idMunicipio){
		return getIntervaloCepDAO().findIntervaloCepByMunicipio(idMunicipio);
	}

	public List findByCep(String nrCep, String sgPais){
		return getIntervaloCepDAO().findByCep(nrCep, sgPais);
	}
	
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public PaisService getPaisService() {
		return paisService;
	}
}