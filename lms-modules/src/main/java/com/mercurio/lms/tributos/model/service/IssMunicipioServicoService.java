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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tributos.issMunicipioServicoService"
 */
public class IssMunicipioServicoService extends CrudService<IssMunicipioServico, Long> {


	/**
	 * Recupera uma inst�ncia de <code>IssMunicipioServico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public IssMunicipioServico findById(java.lang.Long id) {
        return (IssMunicipioServico)super.findById(id);
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
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(IssMunicipioServico bean) {
        return super.store(bean);
    }

    /**
     * M�todo sobrescrito para buscar os registros onde alguns Servi�os podem ser nulos
     * @param criteria Crit�rios de pesquisa
     * @param findDef Defini��es de pagina��o
     * @return ResultSetPage Resultado da pesquisa
     */
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return getIssMunicipioServicoDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
    }
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setIssMunicipioServicoDAO(IssMunicipioServicoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private IssMunicipioServicoDAO getIssMunicipioServicoDAO() {
        return (IssMunicipioServicoDAO) getDao();
    }
   }