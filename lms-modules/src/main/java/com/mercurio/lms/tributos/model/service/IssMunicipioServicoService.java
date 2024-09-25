package com.mercurio.lms.tributos.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.tributos.model.IssMunicipioServico;
import com.mercurio.lms.tributos.model.dao.IssMunicipioServicoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.issMunicipioServicoService"
 */
public class IssMunicipioServicoService extends CrudService<IssMunicipioServico, Long> {


	/**
	 * Recupera uma instância de <code>IssMunicipioServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public IssMunicipioServico findById(java.lang.Long id) {
        return (IssMunicipioServico)super.findById(id);
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
    public java.io.Serializable store(IssMunicipioServico bean) {
        return super.store(bean);
    }

    /**
     * Método sobrescrito para buscar os registros onde alguns Serviços podem ser nulos
     * @param criteria Critérios de pesquisa
     * @param findDef Definições de paginação
     * @return ResultSetPage Resultado da pesquisa
     */
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return getIssMunicipioServicoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setIssMunicipioServicoDAO(IssMunicipioServicoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private IssMunicipioServicoDAO getIssMunicipioServicoDAO() {
        return (IssMunicipioServicoDAO) getDao();
    }
   }