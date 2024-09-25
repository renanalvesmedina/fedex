package com.mercurio.lms.contasreceber.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.contasreceber.model.ItemOcorrenciaPreFatura;
import com.mercurio.lms.contasreceber.model.dao.ItemOcorrenciaPreFaturaDAO;

/**
 * @author José Rodrigo Moraes
 * @since  28/04/2006
 * 
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.itemOcorrenciaPreFaturaService"
 */
public class ItemOcorrenciaPreFaturaService extends CrudService<ItemOcorrenciaPreFatura, Long> {
	
	/**
	 * Recupera uma instância de <code>OcorrenciaPreFatura</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ItemOcorrenciaPreFatura findById(Long id) {
        return (ItemOcorrenciaPreFatura) super.findById(id);
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
    public java.io.Serializable store(ItemOcorrenciaPreFatura bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setItemOcorrenciaPreFaturaDAO(ItemOcorrenciaPreFaturaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ItemOcorrenciaPreFaturaDAO getItemOcorrenciaPreFaturaDAO() {
        return (ItemOcorrenciaPreFaturaDAO) getDao();
    }
    
    /**
     * Realiza a consulta utilizando os filtros padrões recebidos da tela,
     * gera um mapeamento dos campos da tela com a classe de persistencia
     * gerenciada pelo DAO.
     * 
     * @param criteria Mapa de campos da tela.
     * @return Resultado paginado.
     */
    public ResultSetPage findPaginated(Map criteria) {
    	return super.findPaginated(criteria);
    }
    
    /**
     * Retorna o total de paginas para o resultado de acordo com os filtros especificados.
     * Este método é utilizado em conjunto com o findPaginated para obter o total
     * de linhas que a consulta retornaria e calcular com isso o total de paginas.
     * @param criteria
     * @return Quantidade de itens a serem exibidos na grid
     */
    public Integer getRowCount(Map criteria) {    	
    	return super.getRowCount(criteria);
    }
    
}
