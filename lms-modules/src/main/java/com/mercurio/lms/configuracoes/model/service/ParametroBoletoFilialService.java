package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ParametroBoletoFilial;
import com.mercurio.lms.configuracoes.model.dao.ParametroBoletoFilialDAO;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.configuracoes.parametroBoletoFilialService"
 */
public class ParametroBoletoFilialService extends CrudService<ParametroBoletoFilial, Long> {

	/**
	 * 
	 * 
	 * author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 * 
	 * @param id
	 * @return
	 * 
	 */
	public ParametroBoletoFilial findById(Long id) {
		return (ParametroBoletoFilial) super.findById(id);
	}

	/**
	 * 
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 * 
	 * @param id
	 * 
	 */
	public void removeById(Long id) {
		super.removeById(id);
	}

	/**
	 * 
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 * 
	 * @param ids
	 * 
	 */
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * 
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 * 
	 * @param bean
	 * @return
	 * 
	 */
	public java.io.Serializable store(ParametroBoletoFilial bean) {
		return super.store(bean);
	}

	/**
	 * 
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 * 
	 * @param bean
	 * @return
	 * 
	 */
	public ParametroBoletoFilial beforeStore(ParametroBoletoFilial bean) {
		ParametroBoletoFilial pbf = (ParametroBoletoFilial) bean;

		/** Busca ParametroBoletoFilial divergente */
		List list = this.findParametrosBoletoFilialVigenciaConflito(pbf
				.getDtVigenciaInicial(), pbf.getDtVigenciaFinal(), pbf
				.getFilial(), pbf.getIdParametroBoletoFilial());

		/**
		 * Caso tenha retornado algum ParametroBoletoFilial divergente, lan�a a
		 * exce��o
		 */
		if (list != null && !list.isEmpty()) {
			throw new BusinessException("LMS-00047");
		}

		return super.beforeStore(bean);
	}

	/**
	 * M�todo respons�vel por buscar ParametroBoletoFilial que tenham a mesma
	 * filial e que a vig�ncia esteja em conflito com o ParametroBoletoFilial
	 * que vem da view
	 * 
	 * @author Hector Julian Esnaola Junior
	 * @since 12/09/2006
	 * 
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @param filial
	 * @return
	 * 
	 */
	public List findParametrosBoletoFilialVigenciaConflito(
			YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal,
			Filial filial, Long idParametroBoletoFilial) {

		return getParametroBoletoFilialDAO()
				.findParametrosBoletoFilialVigenciaConflito(dtVigenciaInicial,
						dtVigenciaFinal, filial, idParametroBoletoFilial);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
	public void setParametroBoletoFilialDAO(ParametroBoletoFilialDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private ParametroBoletoFilialDAO getParametroBoletoFilialDAO() {
		return (ParametroBoletoFilialDAO) getDao();
	}

	/**
	 * @see findParametroBoletoFilialVigenteByFilial(Long idFilial, YearMonthDay
	 *      data, Boolean blValorLiquido)
	 */
	public List findParametroBoletoFilialVigenteByFilial(Long idFilial,
			YearMonthDay data) {
		return findParametroBoletoFilialVigenteByFilial(idFilial, data, null);
	}

	/**
	 * Busca os par�metros Boleto filial que estejam vigentes na data passada
	 * por par�metro, que sejam da filial informada
	 * 
	 * @author Jos� Rodrigo Moraes
	 * @since 13/09/2006
	 * 
	 * @param idFilial
	 *            Identificador da filial
	 * @param data
	 *            Data de vig�ncia
	 * @param blValorLiquido
	 * @return Lista de Par�metros Boleto Filial
	 */
	public List findParametroBoletoFilialVigenteByFilial(Long idFilial,
			YearMonthDay data, Boolean blValorLiquido) {
		return getParametroBoletoFilialDAO()
				.findParametroBoletoFilialVigenteByFilial(idFilial, data,
						blValorLiquido);
	}

	/**
	 * Busca os par�metros Boleto filial que estejam vigentes na data passada
	 * por par�metro, que sejam da filial informada
	 * 
	 * @author Micka�l Jalbert
	 * @since 13/09/2006
	 * 
	 * @param idFilial
	 *            Identificador da filial
	 * @param data
	 *            Data de vig�ncia
	 * @return ParametroBoletoFilial
	 */
	public ParametroBoletoFilial findParametroBoletoFilialVigenteByIdFilial(
			Long idFilial, YearMonthDay data) {
		ParametroBoletoFilial retorno = null;

		List lstParametro = findParametroBoletoFilialVigenteByFilial(idFilial,
				data);

		if (!lstParametro.isEmpty()) {
			retorno = (ParametroBoletoFilial) lstParametro.get(0);
		}

		return retorno;
	}

	/**
	 * Retorna um map onde cada filial � uma chave e onde o objeto � uma lista
	 * de array de YearMonthDay
	 * 
	 * @author Micka�l Jalbert
	 * @since 28/02/2007
	 * 
	 * @return map com idFilial como chave
	 */
	public Map findParametroBoletoFilial() {
		return getParametroBoletoFilialDAO().findMapParametroBoletoFilial();
	}

	/**
	 * M�todo respons�vel por buscar os dados da grid
	 * 
	 * @author Diego Umpierre
	 * @since 26/09/2006
	 * 
	 * @param criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedTela(TypedFlatMap criteria) {
		ResultSetPage rsp = ((ParametroBoletoFilialDAO) getDao())
				.findPaginatedTela(criteria, FindDefinition
						.createFindDefinition(criteria));
		return rsp;
	}

	/**
	 * M�todo respons�vel por buscar o numero de linhas da grid
	 * 
	 * @author Diego Umpierre
	 * @since 26/09/2006
	 * 
	 * @param criteria
	 * @return Integer
	 */
	public Integer getRowCountTela(TypedFlatMap criteria) {
		return ((ParametroBoletoFilialDAO) getDao()).getRowCountTela(criteria);
	}

	@Override
	protected ParametroBoletoFilial beforeInsert(ParametroBoletoFilial bean) {
		ParametroBoletoFilial pbf = (ParametroBoletoFilial) bean;

		// Caso a vig�ncia inicial seja menor que a data atual, lan�a a
		// exception.
		if (JTDateTimeUtils.comparaData(pbf.getDtVigenciaInicial(),
				JTDateTimeUtils.getDataAtual()) <= 0) {
			throw new BusinessException("LMS-30040");
		}

		return super.beforeInsert(bean);
	}

	@Override
	protected ParametroBoletoFilial beforeUpdate(ParametroBoletoFilial bean) {
		ParametroBoletoFilial pbf = (ParametroBoletoFilial) bean;
		ParametroBoletoFilial pbfSession = (ParametroBoletoFilial) this
				.findById(pbf.getIdParametroBoletoFilial());
		
		// Caso a vig�ncia final seja menor que a data atual, lan�a a exception.
		if (pbf.getDtVigenciaFinal() != null
				&& JTDateTimeUtils.comparaData(pbf.getDtVigenciaFinal(),
						JTDateTimeUtils.getDataAtual()) < 0) {
			throw new BusinessException("LMS-00007");
		}

		// Caso a vigencia inicial tenha diminuido, e seja menor ou igual a data atual, lan�a a exception.
		if (pbf.getDtVigenciaInicial().isBefore(pbfSession.getDtVigenciaInicial()) 
				&& JTDateTimeUtils.comparaData(pbf.getDtVigenciaInicial(), JTDateTimeUtils.getDataAtual()) <= 0) {
			throw new BusinessException("LMS-30040");
		}
		
		getParametroBoletoFilialDAO().evict(pbfSession);

		return super.beforeUpdate(bean);
	}

}
