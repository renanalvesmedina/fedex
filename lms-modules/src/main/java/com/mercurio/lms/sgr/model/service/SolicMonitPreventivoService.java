package com.mercurio.lms.sgr.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.sgr.model.SolicMonitPreventivo;
import com.mercurio.lms.sgr.model.dao.SolicMonitPreventivoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.solicMonitPreventivoService"
 */
public class SolicMonitPreventivoService extends CrudService<SolicMonitPreventivo, Long> {


	/**
	 * Recupera uma instância de <code>SolicMonitPreventivo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public SolicMonitPreventivo findById(java.lang.Long id) {
        return (SolicMonitPreventivo)super.findById(id);
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
    public java.io.Serializable store(SolicMonitPreventivo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSolicMonitPreventivoDAO(SolicMonitPreventivoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SolicMonitPreventivoDAO getSolicMonitPreventivoDAO() {
        return (SolicMonitPreventivoDAO) getDao();
    }

    /**
     * Método de pesquisa para Manter SMP 
     * @param tfm
     * @param fd
     * @return
     * @author Rodrigo Antunes
     */
    public ResultSetPage findPaginatedSMP(TypedFlatMap tfm, FindDefinition fd) {
    	return this.getSolicMonitPreventivoDAO().findPaginatedSMP(tfm, fd);
    }
    
    /**
     * row count para Manter SMP 
     * @param tfm
     * @return
     */
    public Integer getRowCountSMP(TypedFlatMap tfm) {
        return this.getSolicMonitPreventivoDAO().getRowCountSMP(tfm);
    }
    
	/**
	 * findById customizado para SMP 
	 * @param idSMP
	 * @return
	 */
	public SolicMonitPreventivo findSMPById(Long idSMP) {
		return this.getSolicMonitPreventivoDAO().findSMPById(idSMP);
	}
	
	/**
	 * FindPaginated da Consulta de Conteúdo de Veiculos da SMP 
	 * @param tfm
	 * @param fd
	 * @return
	 */
    public ResultSetPage findConsultaConteudoVeiculos(TypedFlatMap tfm) {
    	return getSolicMonitPreventivoDAO().findConsultaConteudoVeiculos(tfm, FindDefinition.createFindDefinition(tfm));
    }
    
	/**
	 * RowCount da Consulta de Conteúdo de Veiculos da SMP 
	 * @param tfm
	 * @return
	 */
    public Integer getRowCountConsultaConteudoVeiculos(TypedFlatMap tfm) {
    	return getSolicMonitPreventivoDAO().getRowCountConsultaConteudoVeiculos(tfm);
    }

    /**
     * Obtém o SolicMonitPreventivo de acordo com o Controle de Cargas e a Filial recebidos.
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public SolicMonitPreventivo findByIdControleCargaAndFilial(Long idControleCarga, Long idFilial) {
    	return getSolicMonitPreventivoDAO().findByIdControleCargaAndFilial(idControleCarga, idFilial);
    }

    /**
     * Obtém o SolicMonitPreventivo de acordo com o Controle de Cargas 
     * @param idControleCarga
     * @param idFilial
     * @return
     */
    public SolicMonitPreventivo findByIdControleCarga(Long idControleCarga) {
    	return getSolicMonitPreventivoDAO().findByIdControleCarga(idControleCarga);
    }

    /**
     * 
     * @param idControleCarga
     * @return
     */
    public Long findSmpByControleCargaLocalTroca(Long idControleCarga) {
    	List result = getSolicMonitPreventivoDAO().findSmpByControleCargaLocalTroca(idControleCarga);
    	if (!result.isEmpty()) {
    		Map map = (Map)result.get(0);
    		return (Long)map.get("idSmp");
    	}
    	return null;
    }
    
    
    /**
     * Altera o status para o Controle de Carga em questão.
     * 
     * @param idControleCarga
     */
    public void updateStatusSMPByIdControleCarga(Long idControleCarga, String statusSMP) {
    	getSolicMonitPreventivoDAO().updateStatusSMPByIdControleCarga(idControleCarga, statusSMP);
    }

	

}
