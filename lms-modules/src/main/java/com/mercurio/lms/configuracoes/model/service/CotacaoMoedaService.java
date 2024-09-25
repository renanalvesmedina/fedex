package com.mercurio.lms.configuracoes.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.CotacaoMoeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.dao.CotacaoMoedaDAO;
import com.mercurio.lms.configuracoes.model.dao.MoedaPaisDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.moedaCotacaoService"
 */
public class CotacaoMoedaService extends CrudService<CotacaoMoeda, Long> {
	private MoedaPaisService moedaPaisService;
	private MoedaPaisDAO moedaPaisDAO;

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected CotacaoMoeda beforeStore(CotacaoMoeda bean) {
		MoedaPais mp = getMoedaPaisDAO().findByUniqueKey(bean.getMoedaPais().getMoeda().getIdMoeda(), bean.getMoedaPais().getPais().getIdPais());
		if (mp == null){
			throw new BusinessException("LMS-00020");
		}

		bean.setMoedaPais(mp);
		return bean;
	}

	/**
	 * Recupera uma inst�ncia de <code>MoedaCotacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public CotacaoMoeda findById(java.lang.Long id) {
		return getCotacaoMoedaDAO().findById(id);
	}

	/**
	 * Retorna a cota��o de uma data especifica para facilitar c�lculos.
	 *
	 * @param Long idPais
	 * @param Long idMoeda
	 * @param Date dtCotacao
	 * @return BigDecimal
	 */
	public BigDecimal findVlCotacaoMoedaByPaisMoeda(Long idPais, Long idMoeda, YearMonthDay dtCotacao) {
		List<BigDecimal> list = this.getCotacaoMoedaDAO().findVlCotacaoMoedaByPaisMoeda(idPais,idMoeda,dtCotacao);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Retorna o valor da cota��o da MoedaPais informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 02/10/2006
	 * 
	 * @param Long idMoedaPais
	 * @param YearMonthDay dtCotacao
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal findVlCotacaoByMoedaPais(Long idMoedaPais, YearMonthDay dtCotacao) {
		List<BigDecimal> list = this.getCotacaoMoedaDAO().findVlCotacaoByMoedaPais(idMoedaPais, dtCotacao);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			MoedaPais moedaPais = moedaPaisService.findById(idMoedaPais);
			throw new BusinessException("LMS-36199", new Object[]{moedaPais.getPais().getNmPais(), moedaPais.getMoeda().getSgMoeda(), dtCotacao});
		}
	}

	/**
	 * Retorna a cota��o de uma data especifica para facilitar c�lculos.
	 *
	 * @param Long idPais
	 * @param Long idMoedaOrigem
	 * @param Long idMoedaDestino
	 * @param Date dtCotacao
	 * @return BigDecimal
	 */
	public BigDecimal findCotacaoMoedaComPais(Long idPais, Long idMoedaOrigem, Long idMoedaDestino, YearMonthDay dtCotacao) {
		List<BigDecimal> list = this.getCotacaoMoedaDAO().findVlCotacaoMoedaByPaisMoeda(idPais, idMoedaOrigem, idMoedaDestino, dtCotacao);
		if (!list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
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
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(CotacaoMoeda bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setMoedaCotacaoDAO(CotacaoMoedaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CotacaoMoedaDAO getCotacaoMoedaDAO() {
		return (CotacaoMoedaDAO) getDao();
	}

	/**
	 * @return Returns the moedaPaisDAO.
	 */
	public MoedaPaisDAO getMoedaPaisDAO() {
		return moedaPaisDAO;
	}
	/**
	 * @param moedaPaisDAO The moedaPaisDAO to set.
	 */
	public void setMoedaPaisDAO(MoedaPaisDAO moedaPaisDAO) {
		this.moedaPaisDAO = moedaPaisDAO;
	}

	public void setMoedaPaisService(MoedaPaisService moedaPaisService) {
		this.moedaPaisService = moedaPaisService;
	}

	@Override
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage resultSetPage = super.findPaginated(criteria);
		return resultSetPage;
	}
}