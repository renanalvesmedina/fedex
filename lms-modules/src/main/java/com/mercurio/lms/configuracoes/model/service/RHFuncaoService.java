package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.RHFuncao;
import com.mercurio.lms.configuracoes.model.dao.RHFuncaoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.RHFuncaoService"
 */
public class RHFuncaoService extends CrudService<RHFuncao, Long> {


	public ResultSetPage findPaginated(Map criteria) {
		criteria.put("codigo",criteria.get("idCodigo"));
		criteria.remove("idCodigo");
		return super.findPaginated(criteria);
	}
	
	public Integer getRowCount(Map criteria) {
		criteria.remove("idCodigo");
		return super.getRowCount(criteria);
	}

	/**
	 * Recupera uma inst�ncia de <code>Funcionario</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public RHFuncao findById(java.lang.Long id) {
        return (RHFuncao)super.findById(id);
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
    public java.io.Serializable store(RHFuncao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRHFuncaoDAO(RHFuncaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private RHFuncaoDAO getRHFuncaoDAO() {
        return (RHFuncaoDAO) getDao();
    }

	public List findLookup(Map criteria) {
		criteria.put("codigo",criteria.get("idCodigo"));
		criteria.remove("idCodigo");
		return super.findLookup(criteria);
	}
   }