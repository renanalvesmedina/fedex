package com.mercurio.lms.prestcontasciaaerea.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.prestcontasciaaerea.model.IntervaloAwb;
import com.mercurio.lms.prestcontasciaaerea.model.dao.IntervaloAwbDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.prestcontasciaaerea.intervaloAwbService"
 */
public class IntervaloAwbService extends CrudService<IntervaloAwb, Long> {


	/**
	 * Recupera uma instância de <code>IntervaloAwb</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public IntervaloAwb findById(java.lang.Long id) {
        return (IntervaloAwb)super.findById(id);
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
    public java.io.Serializable store(IntervaloAwb bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setIntervaloAwbDAO(IntervaloAwbDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private IntervaloAwbDAO getIntervaloAwbDAO() {
        return (IntervaloAwbDAO) getDao();
    }
    
    /**
     * Busca todas as AWBs da Prestacao de Contas.<BR>
     *@author Robson Edemar Gehl
     * @param criterions
     * @return
     */
    public ResultSetPage findPaginatedIntervalosAwb(Map criterions){
    	return getIntervaloAwbDAO().findPaginatedIntervalosAwb(criterions);
    }
    
    /**
     * Remove os registros de intervalos de awbs da prestação em questão
     * @param idPrestacaoConta
     */
    public void removeDesmarcarPrestacaoConta(Long idPrestacaoConta){
    
    	getIntervaloAwbDAO().removeDesmarcarPrestacaoConta(idPrestacaoConta);

    }

}