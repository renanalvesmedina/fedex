package com.mercurio.lms.municipios.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.PontoParadaTrecho;
import com.mercurio.lms.municipios.model.RotaViagem;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.dao.PontoParadaTrechoDAO;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.pontoParadaTrechoService"
 */
public class PontoParadaTrechoService extends CrudService<PontoParadaTrecho, Long> {
	private TrechoRotaIdaVoltaService trechoRotaIdaVoltaService;
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>PontoParadaTrecho</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public PontoParadaTrecho findById(java.lang.Long id) {
		return (PontoParadaTrecho)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	public ResultSetPage findPaginatedWithLocalInformation(TypedFlatMap criteria) {
		return getPontoParadaTrechoDAO().findPaginatedWithLocalInformation(criteria,FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getPontoParadaTrechoDAO().getRowCountCustom(criteria);
	}

	/**
	 * Retorna informações extras populadas ao detalhar um registro de Ponto Parada.
	 * @param id
	 * @return
	 */
	public Map<String, Object> findByIdExtra(Long id) {
		Map<String, Object> result = getPontoParadaTrechoDAO().findByIdExtra(id);
		String sgRodovia = (String)result.remove("pontoParada_rodovia_sgRodovia");
		String dsRodovia = (String)result.remove("pontoParada_rodovia_dsRodovia");
		if(StringUtils.isNotBlank(sgRodovia)) {
			String sgDsRodovia = sgRodovia;
			if(StringUtils.isNotBlank(dsRodovia)) {
				sgDsRodovia = sgDsRodovia + " - " + dsRodovia;
			}
			result.put("pontoParada_rodovia_sgDsRodovia", sgDsRodovia);
		}
		return result;
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
	public java.io.Serializable store(PontoParadaTrecho bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setPontoParadaTrechoDAO(PontoParadaTrechoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private PontoParadaTrechoDAO getPontoParadaTrechoDAO() {
		return (PontoParadaTrechoDAO) getDao();
	}

	protected PontoParadaTrecho beforeStore(PontoParadaTrecho bean) {
		PontoParadaTrecho pojo = (PontoParadaTrecho)bean;

		vigenciaService.validaVigenciaBeforeStore(pojo);

		TrechoRotaIdaVolta trechoRotaIdaVolta = pojo.getTrechoRotaIdaVolta();
		if (getPontoParadaTrechoDAO().validateDuplicatedPonto(pojo.getIdPontoParadaTrecho(),
				pojo.getDtVigenciaInicial(),
				pojo.getDtVigenciaFinal(),
				pojo.getPontoParada().getIdPontoParada(),
				trechoRotaIdaVolta.getIdTrechoRotaIdaVolta())) {
			throw new BusinessException("LMS-00003");
		}

		TrechoRotaIdaVolta trivAux = (TrechoRotaIdaVolta)getPontoParadaTrechoDAO().getAdsmHibernateTemplate()
				.load(TrechoRotaIdaVolta.class,trechoRotaIdaVolta.getIdTrechoRotaIdaVolta());
		RotaViagem rotaViagem = new RotaViagem();
		rotaViagem.setIdRotaViagem(trivAux.getRotaIdaVolta().getRotaViagem().getIdRotaViagem());
		if (!vigenciaService.validateEntidadeVigente(rotaViagem,
				pojo.getDtVigenciaInicial(),
				pojo.getDtVigenciaFinal()))
			throw new BusinessException("LMS-29150");

		boolean blTodaRota = trechoRotaIdaVoltaService.validateTrechoFromWholeRota(trechoRotaIdaVolta.getIdTrechoRotaIdaVolta());
		if (blTodaRota && pojo.getNrOrdem() == null) {
			throw new BusinessException("LMS-29117");
		}

		return super.beforeStore(bean);
	}

	/**
	 * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
	 * @param id Id do registro a ser validado.
	 */
	private void validaRemoveById(Long id) {
		PontoParadaTrecho pontoParadaTrecho = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(pontoParadaTrecho);
	}

	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long)id);
		super.beforeRemoveById(id);
	}

	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	protected void beforeRemoveByIds(List ids) {
		for(Iterator<Long> i = ids.iterator() ; i.hasNext() ; )
			validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
	}

	public void validateTrechoFromWholeRota(Long idTrechoRotaIdaVolta) {
		if(!trechoRotaIdaVoltaService.validateTrechoFromWholeRota(idTrechoRotaIdaVolta))
			throw new BusinessException("LMS-29077");
	}

	public List findToConsultarRotas(Long idRotaIdaVolta) {
		return getPontoParadaTrechoDAO().findToConsultarRotas(idRotaIdaVolta);
	}

	/**
	 * 
	 * @param idRotaIdaVolta
	 * @return
	 */
	public List findToGerarControleCarga(Long idRotaIdaVolta) {
		List lista = getPontoParadaTrechoDAO().findToGerarControleCarga(idRotaIdaVolta);
		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(lista);
	}

	public Integer getRowCountByTrechoRotaIdaVolta(Long idTrechoRotaIdaVolta) {
		return getPontoParadaTrechoDAO().getRowCountByTrechoRotaIdaVolta(idTrechoRotaIdaVolta);
	}

	public void setTrechoRotaIdaVoltaService(TrechoRotaIdaVoltaService trechoRotaIdaVoltaService) {
		this.trechoRotaIdaVoltaService = trechoRotaIdaVoltaService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}