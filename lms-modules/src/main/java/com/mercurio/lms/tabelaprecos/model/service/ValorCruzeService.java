package com.mercurio.lms.tabelaprecos.model.service;

import java.util.Iterator;
import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tabelaprecos.model.ValorCruze;
import com.mercurio.lms.tabelaprecos.model.dao.ValorCruzeDAO;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.tabelaprecos.valorCruzeService"
 */
public class ValorCruzeService extends CrudService<ValorCruze, Long> {

	private VigenciaService vigenciaService;

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		FindDefinition fd = FindDefinition.createFindDefinition(criteria);
		return getValorCruzeDAO().findPaginated(criteria, fd);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getValorCruzeDAO().getRowCount(criteria);
	}

	/**
	 * Recupera uma inst�ncia de <code>ValorCruze</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws //
	 *             
	 */
	public ValorCruze findById(java.lang.Long id) {
		return getValorCruzeDAO().findById(id);
	}

	protected void beforeRemoveById(Long id) {
		ValorCruze valorCruze = getValorCruzeDAO()
				.findById((java.lang.Long) id);

		vigenciaService.validaVigenciaBeforeRemove(valorCruze
				.getDtVigenciaInicial(), valorCruze.getDtVigenciaFinal());
		super.beforeRemoveById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 * 
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		ValorCruze valorCruze = null;
		for (Iterator i = ids.iterator(); i.hasNext();) {
			Long id = (Long) i.next();
			valorCruze = getValorCruzeDAO().findById(id);
			// Valida a data de vig�ncia inicial, pois esta n�o pode ser menor
			// que a data atual.
			vigenciaService.validaVigenciaBeforeRemove(valorCruze
					.getDtVigenciaInicial(), valorCruze.getDtVigenciaFinal());
		}
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 * 
	 * @param ids
	 *            lista com as entidades que dever�o ser removida.
	 * 
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	protected ValorCruze beforeStore(ValorCruze valorCruze) {

		// Valida a data de vig�ncia inicial, pois esta n�o pode ser menor que a
		// data atual.
		vigenciaService.validaVigenciaBeforeStore((ValorCruze) valorCruze);
		List list = getValorCruzeDAO().findSobreposicaoByFaixaPeso(
				(ValorCruze) valorCruze);
		if (list.size() > 0) {
			throw new BusinessException("LMS-30032");
		}
		return super.beforeStore(valorCruze);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(ValorCruze valorCruze) {
		return super.store(valorCruze);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
	public void setValorCruzeDAO(ValorCruzeDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private ValorCruzeDAO getValorCruzeDAO() {
		return (ValorCruzeDAO) getDao();
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public List findByVigencia(YearMonthDay dtVigencia){
		return getValorCruzeDAO().findByVigencia(dtVigencia);
	}
}