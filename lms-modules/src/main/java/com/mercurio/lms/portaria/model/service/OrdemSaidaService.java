package com.mercurio.lms.portaria.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.portaria.model.OrdemSaida;
import com.mercurio.lms.portaria.model.dao.OrdemSaidaDAO;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.ordemSaidaService"
 */
public class OrdemSaidaService extends CrudService<OrdemSaida, Long> {

	/**
	 * Recupera uma instância de <code>OrdemSaida</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public OrdemSaida findById(java.lang.Long id) {
		return (OrdemSaida)super.findById(id);
	}

	protected void beforeRemoveById(Long id) {
		OrdemSaida ordemSaida = findById((Long)id);
		if(isOrdemSaidaEmAberto(ordemSaida)) {
			throw new BusinessException("LMS-06020");
		}
		super.beforeRemoveById(id);
	}

	protected void beforeRemoveByIds(List ids) {
		for (Iterator it = ids.iterator(); it.hasNext();){
			Long id = (Long) it.next();
			OrdemSaida ordemSaida = findById(id);
			if(isOrdemSaidaEmAberto(ordemSaida)) {
				throw new BusinessException("LMS-06020");
			}
		}
		super.beforeRemoveByIds(ids);
	}

	/**
	 * Verifica se a ordem de saida possui data de saida informada (utilizado no removeById)
	 * @param idOrdemSaida
	 * @return
	 */
	private boolean isOrdemSaidaEmAberto(OrdemSaida ordemSaida) {
		if( (Boolean.TRUE.equals(ordemSaida.getBlSemRetorno()) && ordemSaida.getDhSaida() == null)
				|| (Boolean.FALSE.equals(ordemSaida.getBlSemRetorno()) && ordemSaida.getDhChegada() == null)
		) {
			return true;
		}
		return false;
	}

	public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		ResultSetPage rsp = getOrdemSaidaDAO().findPaginatedCustom(criteria,
				FindDefinition.createFindDefinition(criteria)); 
		
		FilterResultSetPage frsp =  new FilterResultSetPage(rsp) {
			AliasToTypedFlatMapResultTransformer a = new AliasToTypedFlatMapResultTransformer();
			
			@Override
			public Map filterItem(Object item) {
				Map map = (Map)item;
				TypedFlatMap retorno = a.transformeTupleMap(map);
				
				String tpIdentificacao = retorno.getString("motorista.pessoa.tpIdentificacao.value");
				String nrIdentificacao = retorno.getString("motorista.pessoa.nrIdentificacao"); 
				if (tpIdentificacao != null && nrIdentificacao != null) {
					retorno.put("motorista.pessoa.tpIdentificacao",
							retorno.getVarcharI18n("motorista.pessoa.tpIdentificacao.description").getValue());
					retorno.put("motorista.pessoa.nrIdentificacao",
							FormatUtils.formatIdentificacao(tpIdentificacao,nrIdentificacao));
				}
				
				return retorno;
			}
			
		};
		
		return (ResultSetPage)frsp.doFilter();
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		return getOrdemSaidaDAO().getRowCountCustom(criteria);
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
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	protected OrdemSaida beforeStore(OrdemSaida bean) {
		OrdemSaida ordemSaida = (OrdemSaida) bean;
		if (!getOrdemSaidaDAO().verificaOrdemSaidaByMeioTransporte(ordemSaida.getIdOrdemSaida(), ordemSaida.getMeioTransporteRodoviarioByIdMeioTransporte().getIdMeioTransporte()))
			throw new BusinessException("LMS-06010");

		return super.beforeStore(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(OrdemSaida bean) {
		bean.setDhRegistro(JTDateTimeUtils.getDataHoraAtual());
		return super.store(bean);
	}

	public List findDadosChegadaSaida(Long idMeioTransporte){
		return getOrdemSaidaDAO().findDadosChegadaSaida(idMeioTransporte);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * @param Instância do DAO.
	 */
	public void setOrdemSaidaDAO(OrdemSaidaDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 * @return Instância do DAO.
	 */
	private OrdemSaidaDAO getOrdemSaidaDAO() {
		return (OrdemSaidaDAO) getDao();
	}

	/**
	 * Verifica se existe alguma ordem de saída aberta para o meio de transporte informado.
	 * @param map
	 * @return TRUE se existe alguma ordem de saída aberta para o meio de transporte informado, FALSE caso contrário  
	 */
	public List<OrdemSaida> findByMeioTransporteInOrdemSaida(Map map) {
		return getOrdemSaidaDAO().findByMeioTransporteInOrdemSaida(map);
	}

	/**
	 * Busca última ordem de saída do meio de transporte enviado por parâmetro com data
	 * maior que a data do controle de carga.
	 * @return
	 */
	public OrdemSaida findUltimaOrdemSaida(TypedFlatMap map){
		return getOrdemSaidaDAO().findUltimaOrdemSaida(map);
	}
}