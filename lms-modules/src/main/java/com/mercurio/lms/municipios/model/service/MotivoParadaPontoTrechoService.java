package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.municipios.model.MotivoParadaPontoTrecho;
import com.mercurio.lms.municipios.model.PontoParadaTrecho;
import com.mercurio.lms.municipios.model.dao.MotivoParadaPontoTrechoDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.municipios.motivoParadaPontoTrechoService"
 */
public class MotivoParadaPontoTrechoService extends CrudService<MotivoParadaPontoTrecho, Long> {

	private VigenciaService vigenciaService;
	
	/**
	 * Recupera uma inst�ncia de <code>MotivoParadaPontoTrecho</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
	public MotivoParadaPontoTrecho findById(java.lang.Long id) {
		return (MotivoParadaPontoTrecho)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(MotivoParadaPontoTrecho bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setMotivoParadaPontoTrechoDAO(MotivoParadaPontoTrechoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private MotivoParadaPontoTrechoDAO getMotivoParadaPontoTrechoDAO() {
		return (MotivoParadaPontoTrechoDAO) getDao();
	}

	protected MotivoParadaPontoTrecho beforeStore(MotivoParadaPontoTrecho bean) {
		MotivoParadaPontoTrecho pojo = (MotivoParadaPontoTrecho)bean;

		vigenciaService.validaVigenciaBeforeStore(pojo);

		Long idPontoParadaTrecho = pojo.getPontoParadaTrecho().getIdPontoParadaTrecho();
		if (getMotivoParadaPontoTrechoDAO().validateDuplicatedMotivo(pojo.getIdMotivoParadaPontoTrecho(),
				pojo.getDtVigenciaInicial(),
				pojo.getDtVigenciaFinal(),
				pojo.getMotivoParada().getIdMotivoParada(),
				idPontoParadaTrecho))
			throw new BusinessException("LMS-00003");

		PontoParadaTrecho ppt = new PontoParadaTrecho();
		ppt.setIdPontoParadaTrecho(idPontoParadaTrecho);
		if (!vigenciaService.validateEntidadeVigente(ppt,
				pojo.getDtVigenciaInicial(),
				pojo.getDtVigenciaFinal()))
			throw new BusinessException("LMS-29151");

		return super.beforeStore(bean);
	}

	/**
	 * Valida a remo��o de um registro de acordo com o padr�o de comportamento de vig�ncias.
	 * @param id Id do registro a ser validado.
	 */
	private void validaRemoveById(Long id) {
		MotivoParadaPontoTrecho motivoParadaPontoTrecho = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(motivoParadaPontoTrecho);
	}

	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long)id);
		super.beforeRemoveById(id);
	}

	/**
	 *
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	protected void beforeRemoveByIds(List<Long> ids) {
		for(Long id : ids)
			validaRemoveById(id);
		super.beforeRemoveByIds(ids);
	}

	public List<Map<String, Object>> findToConsultarRotas(Long idPontoParadaTrecho) {
		return getMotivoParadaPontoTrechoDAO().findToConsultarRotas(idPontoParadaTrecho);
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
}