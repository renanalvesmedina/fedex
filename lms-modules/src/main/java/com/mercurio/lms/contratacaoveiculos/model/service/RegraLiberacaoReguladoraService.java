package com.mercurio.lms.contratacaoveiculos.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.RegraLiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.dao.RegraLiberacaoReguladoraDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contratacaoveiculos.regraLiberacaoReguladoraService"
 */
public class RegraLiberacaoReguladoraService extends CrudService<RegraLiberacaoReguladora, Long> {
	private VigenciaService vigenciaService;

	/**
	 * Recupera uma instância de <code>RegraLiberacaoReguladora</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public RegraLiberacaoReguladora findById(java.lang.Long id) {
		return (RegraLiberacaoReguladora)super.findById(id);
	}

	public Map findByIdDetalhamento(java.lang.Long id) { 
		RegraLiberacaoReguladora regraLiberacao = (RegraLiberacaoReguladora)super.findById(id);
		TypedFlatMap retorno = new TypedFlatMap();

		retorno.put("idRegraLiberacaoReguladora",regraLiberacao.getIdRegraLiberacaoReguladora());
		retorno.put("reguladoraSeguro.nmServicoLiberacaoPrestado",regraLiberacao.getReguladoraSeguro().getNmServicoLiberacaoPrestado()  );
		retorno.put("tpVinculo",regraLiberacao.getTpVinculo().getValue()  );
		retorno.put("qtMesesValidade",regraLiberacao.getQtMesesValidade()  );
		retorno.put("blLiberacaoPorViagem",regraLiberacao.getBlLiberacaoPorViagem()  );
		retorno.put("qtViagensAnoLiberacao",regraLiberacao.getQtViagensAnoLiberacao()  );
		retorno.put("dtVigenciaInicial",regraLiberacao.getDtVigenciaInicial()  );
		retorno.put("dtVigenciaFinal",regraLiberacao.getDtVigenciaFinal()  );
		retorno.put("reguladoraSeguro.idReguladora", regraLiberacao.getReguladoraSeguro().getIdReguladora());
		retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(regraLiberacao));
		return retorno;
	}

	private void validaRemoveById(Long id) {
		RegraLiberacaoReguladora regraLiberacao = findById(id);
		JTVigenciaUtils.validaVigenciaRemocao(regraLiberacao);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(Long id) {
		validaRemoveById(id);
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids)
			validaRemoveById(id);
		super.removeByIds(ids);
	}

	@Override
	protected RegraLiberacaoReguladora beforeStore(RegraLiberacaoReguladora bean) {
		if (getRegraLiberacaoReguladoraDAO().verificaReguladoraVinculoVigente(bean)) {
			throw new BusinessException("LMS-00003");
		}
		return super.beforeStore(bean);
	}

	@Override
	protected void beforeRemoveByIds(List<Long> ids) {
		for(Long id : ids)
			validaRemoveById(id);
		super.beforeRemoveByIds(ids);
	}

	@Override
	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long) id);
		super.beforeRemoveById(id);
	}

	public List<RegraLiberacaoReguladora> findRegraLiberacaoByTpVinculoAndVigencia(String tpVinculo,Long idReguladoraSeguro, YearMonthDay dtVigencia) {
		return getRegraLiberacaoReguladoraDAO().findRegraLiberacaoByTpVinculoAndVigencia(tpVinculo,idReguladoraSeguro,dtVigencia);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(RegraLiberacaoReguladora bean) {
		return super.store(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public Serializable storeMap(Map<String, Object> bean) {
		RegraLiberacaoReguladora regraLiberacaoReguladora = new RegraLiberacaoReguladora();

		ReflectionUtils.copyNestedBean(regraLiberacaoReguladora,bean);

		vigenciaService.validaVigenciaBeforeStore(regraLiberacaoReguladora);
		super.store(regraLiberacaoReguladora);

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idRegraLiberacaoReguladora",regraLiberacaoReguladora.getIdRegraLiberacaoReguladora());
		Integer acaoVigenciaAtual = JTVigenciaUtils.getIntegerAcaoVigencia(regraLiberacaoReguladora);
		retorno.put("acaoVigenciaAtual",acaoVigenciaAtual);

		return retorno;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setRegraLiberacaoReguladoraDAO(RegraLiberacaoReguladoraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private RegraLiberacaoReguladoraDAO getRegraLiberacaoReguladoraDAO() {
		return (RegraLiberacaoReguladoraDAO) getDao();
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

}