package com.mercurio.lms.tabelaprecos.model.service;

import java.io.Serializable;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TarifaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.TarifaPrecoDAO;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tabelaprecos.tarifaPrecoService"
 */
public class TarifaPrecoService extends CrudService<TarifaPreco, Long> {
	
	/**
	 * Através do cdTarifaPreco passada por parametro
	 * 
	 * @param cdTarifaPreco
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findByCdTarifaPreco(String cdTarifaPreco) {
		return getTarifaPrecoDAO().findByCdTarifaPreco(cdTarifaPreco);
	}

	/**
	 * Método utilizado pela Integração
	 * @author Andre Valadas
	 * 
	 * @param idEmpresa
	 * @param cdTarifaPreco
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findTarifaPreco(Long idEmpresa, String cdTarifaPreco) {
		return getTarifaPrecoDAO().findTarifaPreco(idEmpresa, cdTarifaPreco);
	}

	/**
	 * Através da km passada por parametro obtem a tarifa preco atual
	 * 
	 * @param nrKm
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findTarifaPrecoAtual(Long nrKm) {
		return getTarifaPrecoDAO().findTarifaPrecoAtual(nrKm);
	}	
	
	/**
	 * Através da km passada por parametro 
	 * 
	 * @param nrKm
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findTarifaPrecoAntiga(Long nrKm) {
		return getTarifaPrecoDAO().findTarifaPrecoAntiga(nrKm);
	}		
	
	/**
	 * Busca Tarifa das Rotas.
	 * 
	 * @param idTabelaPreco
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return <TarifaPreco>
	 */
	public TarifaPreco findTarifaPreco(Long idTabelaPreco, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		return getTarifaPrecoDAO().findTarifaPreco(idTabelaPreco, restricaoRotaOrigem, restricaoRotaDestino);
	}

	/**
	 * Recupera uma instância de <code>TarifaPreco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
	public TarifaPreco findById(java.lang.Long id) {
		return (TarifaPreco)super.findById(id);
	}

	public Long findByNrKm(Long nrKm){
		return getTarifaPrecoDAO().findByNrKm(nrKm);
	}
	
	public List<TarifaPreco> findTarifaPrecoParaMarkup(String cdTarifaPreco, Long idTabelaPreco, boolean minimoProgressivo) {
		return getTarifaPrecoDAO().findTarifaPrecoParaMarkup(cdTarifaPreco, idTabelaPreco, minimoProgressivo);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
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
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public Serializable store(TarifaPreco bean) {
		return super.store(bean);
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected TarifaPreco beforeStore(TarifaPreco bean) {
		Empresa empresa = (Empresa) SessionContext.get(SessionKey.EMPRESA_KEY);
		bean.setEmpresa(empresa) ;
		return super.beforeStore(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setTarifaPrecoDAO(TarifaPrecoDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private TarifaPrecoDAO getTarifaPrecoDAO() {
		return (TarifaPrecoDAO) getDao();
	}

}