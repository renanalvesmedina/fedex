package com.mercurio.lms.seguros.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.CustoAdicionalSinistro;
import com.mercurio.lms.seguros.model.dao.CustoAdicionalSinistroDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.seguros.custoAdicionalSinistroService"
 */
public class CustoAdicionalSinistroService extends CrudService<CustoAdicionalSinistro, Long> {


	/**
	 * Recupera uma instância de <code>CustoAdicionalSinistro</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public CustoAdicionalSinistro findById(java.lang.Long id) {
        return (CustoAdicionalSinistro)super.findById(id);
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
    public java.io.Serializable store(CustoAdicionalSinistro bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCustoAdicionalSinistroDAO(CustoAdicionalSinistroDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CustoAdicionalSinistroDAO getCustoAdicionalSinistroDAO() {
        return (CustoAdicionalSinistroDAO) getDao();
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
    	return getCustoAdicionalSinistroDAO().findPaginatedCustom(FindDefinition.createFindDefinition(tfm), tfm);
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getCustoAdicionalSinistroDAO().getRowCountCustom(tfm);
    }
    
    public BigDecimal findSumVlCustoAdicionalByIdProcessoSinistro(Long idProcessoSinistro) {
  	   return getCustoAdicionalSinistroDAO().findSumVlCustoAdicionalByIdProcessoSinistro(idProcessoSinistro); 
     }

     public BigDecimal findSumVlReembolsadoByIdProcessoSinistro(Long idProcessoSinistro) {
     	return getCustoAdicionalSinistroDAO().findSumVlReembolsadoByIdProcessoSinistro(idProcessoSinistro); 	   
     }
     
     public List findByIdProcessoSinistro(Long idProcessoSinistro) {
     	return getCustoAdicionalSinistroDAO().findByIdProcessoSinistro(idProcessoSinistro);
     }

     //LMS-6178
     public List findSomaValoresReembolso(Long idProcessoSinistro) {
    	 return getCustoAdicionalSinistroDAO().findSomaValoresReembolso(idProcessoSinistro);
     }

     //LMS-6178
     public List findSomaValoresPrejuizo(Long idProcessoSinistro) {
    	 return getCustoAdicionalSinistroDAO().findSomaValoresPrejuizo(idProcessoSinistro);
     }
    
   }