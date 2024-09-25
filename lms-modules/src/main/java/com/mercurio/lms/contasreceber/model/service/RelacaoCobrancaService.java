package com.mercurio.lms.contasreceber.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.RelacaoCobranca;
import com.mercurio.lms.contasreceber.model.dao.RelacaoCobrancaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.relacaoCobrancaService"
 */
public class RelacaoCobrancaService extends CrudService<RelacaoCobranca, Long> {

	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera os dados de RelacaoCobranca a partir do ID, uso especifico para a tela
	 * de Relacao Cobranca.
	 * Para contemplar a tela de Consultar Relações Cobranças
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Map que possui os atributos do id informado.
	 * @throws
	 * @see com.mercurio.lms.contasreceber.model.dao.findMapById
	 */
    public Map findMapById(java.lang.Long id) {
        return getRelacaoCobrancaDAO().findMapById(id);
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
    public java.io.Serializable store(RelacaoCobranca bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRelacaoCobrancaDAO(RelacaoCobrancaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RelacaoCobrancaDAO getRelacaoCobrancaDAO() {
        return (RelacaoCobrancaDAO) getDao();
    }

    
    
	/**Executa a consulta da grid para a tela de List
	 * @param TypedFlatMap */
    public ResultSetPage findPaginated(java.lang.Long idFilial, java.lang.Long relacaoCobranca,java.lang.Long idRedeco,FindDefinition fd) {
		return getRelacaoCobrancaDAO().findPaginated(idFilial,relacaoCobranca,idRedeco,fd);
	}
	
	
	/**Executa a contagem do numero de linhas da consulta feita para grid da tela de List
	 * @param TypedFlatMap */
	public Integer getRowCount(java.lang.Long idFilial, java.lang.Long relacaoCobranca, java.lang.Long idRedeco) {
		
		return getRelacaoCobrancaDAO().getRowCount(idFilial,relacaoCobranca,idRedeco);
	}
    
	/** 
	 * Usado na tela de Consultar Relacoes Cobrancas, usado para carregar o grid da terceira aba.
	 * Usa um map pois tem um uso especifico.
	 * 
	 * @author Diego Umpierre - LMS
	 * @param map
	 * @return Map
	 * */
	public ResultSetPage findPaginatedMapGridById(Long idRelacaoCobranca, FindDefinition findDef) {
		return getRelacaoCobrancaDAO().findPaginatedMapGridById(idRelacaoCobranca, findDef);
	}

	/**Usado na tela de Consultar Relacoes Cobrancas, numero de linhas retornado na consulta da grid para a terceira aba.
	 * 
	 * @author Diego Umpierre - LMS
	 * @param map
	 * @return
	 */
	public Integer getRowCountMapGrid(Long idRelacaoCobranca) {
		
		return getRelacaoCobrancaDAO().getRowCountMapGrid(idRelacaoCobranca);
	}
    
	
	/**Usado na tela de Consultar Relacoes Cobrancas, carrega os dados nos campos da terceira aba.
	 *
	 * @author Diego Umpierre - LMS
	 * @param map
	 * @return
	 */		   
	public Map findMapGridById(Long id) {
		return getRelacaoCobrancaDAO().findMapGridById(id);
	}

	/**
	 * Retorna, paginado, todas a relações de cobrança do redeco informado.
	 * 
	 * @author Mickaël Jalbert
	 * @since 05/07/2006
	 * 
	 * @param Long idRedeco
	 * @param FindDefinition findDef
	 * 
	 * @return ResultsetPage
	 */
	public List findByIdRedeco(Long idRedeco){
		return getRelacaoCobrancaDAO().findByIdRedeco(idRedeco);
	}	

    /**
	 * Retorna o novo número de Relação de cobrança a partir da filial informada.
	 * 
	 * @author Mickaël Jalbert 
	 * @since 17/07/2006
	 * 
	 * @param Long idFilial
	 * 
	 * @return Long
	 */
    public Long findNextNrRelacaoCobranca(Long idFilial){
    	return configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_RELACAO_COBRANCA", false);
    }

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}   	

	/**
	 * Metodo responsável por buscar RelacaoCobranca de acordo com o nrRelacaoCobrancaFilial
	 * 
	 * author Hector Julian Esnaola Junior
	 * @since 27/07/2006
	 * 
	 * @param Long nrRelacaoCobranca
	 * 
	 * @return List
	 */
	public List findRelacaoCobrancaByNrRelacaoCobranca(Long nrRelacaoCobranca, String sgFilial){
		return getRelacaoCobrancaDAO().findRelacaoCobrancaByNrRelacaoCobranca(nrRelacaoCobranca, sgFilial);
	}
	
	@Override
	public RelacaoCobranca findById(Long id) {
		return (RelacaoCobranca)super.findById(id);
	}

	/**
	 * FindPaginated em SQL desenvolvido para melhorar o desempenho da pesquisa
	 *
	 * @author José Rodrigo Moraes
	 * @since 26/01/2007
	 *
	 * @param idFilial
	 * @param nrRelacaoCobranca
	 * @param idRedeco
	 * @param fd FindDefinition
	 */
	public ResultSetPage findPaginatedBySql(Long idFilial, Long nrRelacaoCobranca, Long idRedeco, FindDefinition fd) {
		return getRelacaoCobrancaDAO().findPaginatedBySql(idFilial, nrRelacaoCobranca, idRedeco, fd);
	}

	/**
	 * getRowCount em SQL desenvolvido para melhorar o desempenho da pesquisa
	 * 
	 *
	 * @author José Rodrigo Moraes
	 * @since 26/01/2007
	 *
	 * @param idFilial
	 * @param nrRelacaoCobranca
	 * @param idRedeco
	 * @param fd FindDefinition
	 */
	public Integer getRowCountBySql(Long idFilial, Long nrRelacaoCobranca, Long idRedeco) {
		return getRelacaoCobrancaDAO().getRowCountBySql(idFilial, nrRelacaoCobranca, idRedeco);
	}

	/**
     * Metodo responsável por buscar RelacaoCobranca de acordo com o nrRelacaoCobrancaFilial e o idFilial
     *
     * @author Hector Julian Esnaola Junior
     * @since 31/05/2007
     *
     * @param nrRelacaoCobranca
     * @param sgFilial
     * @return
     *
     */
	public RelacaoCobranca findRelacaoCobrancaByNrRelacaoCobrancaFilialAndIdFilial(Long nrRelacaoCobranca, Long idFilial){
		return getRelacaoCobrancaDAO().findRelacaoCobrancaByNrRelacaoCobrancaFilialAndIdFilial(nrRelacaoCobranca, idFilial);
	}
	
	
}