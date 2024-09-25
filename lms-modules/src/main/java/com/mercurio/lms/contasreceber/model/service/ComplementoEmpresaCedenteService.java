package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.ComplementoEmpresaCedente;
import com.mercurio.lms.contasreceber.model.dao.ComplementoEmpresaCedenteDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.complementoEmpresaCedenteService"
 */
public class ComplementoEmpresaCedenteService extends CrudService<ComplementoEmpresaCedente, Long> {


	/**
	 * Recupera uma instância de <code>ComplementoEmpresaCedente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public ComplementoEmpresaCedente findById(java.lang.Long id) {
        return (ComplementoEmpresaCedente)super.findById(id);
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
	 * Antes de inserir, verifica se o intervalo que esta sendo cadastrado não está dentro de outro intervalo já cadastrado para
	 * o mesmo cedente e a mesma empresa.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
    public java.io.Serializable store(ComplementoEmpresaCedente bean) {
    	
    	/*
    	 	Não permitir cadastrar um intervalo dentro de outro já existente para o mesmo cedente e a mesma empresa.
    	*/	    	
    	
       	boolean temCadastro  = getComplementoEmpresaCedenteDAO().possuiIntervalo(
    	    	bean.getEmpresa().getIdEmpresa(),
    	    	bean.getCedente().getIdCedente(),
    	    	bean.getNrIntervaloInicialBoleto(),
    	    	bean.getNrIntervaloFinalBoleto(),
    	    	bean.getIdComplementoEmpresaCedente()
    	); 
    	
    	if ( temCadastro ){
    		throw new BusinessException("LMS-36072");
    	}

    	return super.store(bean);
    }
    
    
    public Long generateNextNrBoleto(Long id){
    	return getComplementoEmpresaCedenteDAO().generateNrUltimoBoleto(id);
    }
	/**
	 * Retorna o último número de Boleto a partir da empresa e do cedente informado.
	 * 
	 * @author Mickaël Jalbert 20/04/2006
	 * @param Long idEmpresa
	 * @param Long idCedente
	 * @return Long
	 */
	public ComplementoEmpresaCedente findLastNrBoleto(Long idEmpresa, Long idCedente) {		
		return getComplementoEmpresaCedenteDAO().findByEmpresaCedente(idEmpresa, idCedente);
	}    

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setComplementoEmpresaCedenteDAO(ComplementoEmpresaCedenteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ComplementoEmpresaCedenteDAO getComplementoEmpresaCedenteDAO() {
        return (ComplementoEmpresaCedenteDAO) getDao();
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return getComplementoEmpresaCedenteDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    }

}