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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contasreceber.complementoEmpresaCedenteService"
 */
public class ComplementoEmpresaCedenteService extends CrudService<ComplementoEmpresaCedente, Long> {


	/**
	 * Recupera uma inst�ncia de <code>ComplementoEmpresaCedente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public ComplementoEmpresaCedente findById(java.lang.Long id) {
        return (ComplementoEmpresaCedente)super.findById(id);
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
	 * Antes de inserir, verifica se o intervalo que esta sendo cadastrado n�o est� dentro de outro intervalo j� cadastrado para
	 * o mesmo cedente e a mesma empresa.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
    public java.io.Serializable store(ComplementoEmpresaCedente bean) {
    	
    	/*
    	 	N�o permitir cadastrar um intervalo dentro de outro j� existente para o mesmo cedente e a mesma empresa.
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
	 * Retorna o �ltimo n�mero de Boleto a partir da empresa e do cedente informado.
	 * 
	 * @author Micka�l Jalbert 20/04/2006
	 * @param Long idEmpresa
	 * @param Long idCedente
	 * @return Long
	 */
	public ComplementoEmpresaCedente findLastNrBoleto(Long idEmpresa, Long idCedente) {		
		return getComplementoEmpresaCedenteDAO().findByEmpresaCedente(idEmpresa, idCedente);
	}    

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setComplementoEmpresaCedenteDAO(ComplementoEmpresaCedenteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private ComplementoEmpresaCedenteDAO getComplementoEmpresaCedenteDAO() {
        return (ComplementoEmpresaCedenteDAO) getDao();
    }

    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	return getComplementoEmpresaCedenteDAO().findPaginated(criteria,FindDefinition.createFindDefinition(criteria));
    }

}