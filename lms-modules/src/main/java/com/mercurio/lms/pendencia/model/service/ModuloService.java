package com.mercurio.lms.pendencia.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.Modulo;
import com.mercurio.lms.pendencia.model.dao.ModuloDAO;
import com.mercurio.lms.portaria.model.service.TerminalService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.pendencia.moduloService"
 */
public class ModuloService extends CrudService<Modulo, Long> {

	private TerminalService terminalService;

	/**
	 * Recupera uma instância de <code>Modulo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public Modulo findById(java.lang.Long id) {
        return (Modulo)super.findById(id);
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
    public java.io.Serializable store(Modulo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setModuloDAO(ModuloDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ModuloDAO getModuloDAO() {
        return (ModuloDAO) getDao();
    }
    
    public TerminalService getTerminalService() {
		return terminalService;
	}

	public void setTerminalService(TerminalService terminalService) {
		this.terminalService = terminalService;
	}

	/**
     * Método para retornar uma coleção de Terminais ordenados por dsTerminal.
     * Utilizado em combobox.
     * @author Moacir Zardo Junior
     * @param idFilial
     * @return List ordenada pela descrição do setor
     */
    public List findTerminaisVigentesOrVigenciaFuturaByFilial(Long idFilial){
        return getTerminalService().findTerminalVigenteOrVigenciaFuturaByFilial(idFilial);
    }
    
    public ResultSetPage findPaginatedCustom(TypedFlatMap tfm) {
    	return getModuloDAO().findPaginatedCustom(tfm, FindDefinition.createFindDefinition(tfm));
    }
    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getModuloDAO().getRowCountCustom(tfm);
    }

    
}