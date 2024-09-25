package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NotaFiscalCtoCooperada;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalCtoCooperadaDAO;
import com.mercurio.lms.sim.model.dao.LMParceriaDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.notaFiscalCtoCooperadaService"
 */
public class NotaFiscalCtoCooperadaService extends CrudService<NotaFiscalCtoCooperada, Long> {
	
	private LMParceriaDAO lmParceriaDao;	

	public void setLmParceriaDao(LMParceriaDAO lmParceriaDao) {
		this.lmParceriaDao = lmParceriaDao;
	}

	/**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setNotaFiscalCtoCooperadaDAO(NotaFiscalCtoCooperadaDAO dao) {
        setDao( dao );
    }

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
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private NotaFiscalCtoCooperadaDAO getNotaFiscalServicoDAO() {
        return (NotaFiscalCtoCooperadaDAO) getDao();
    }
    
	public List findByIdCtoCtoCooperada(Long idCtoCtoCooperada) {
		return getNotaFiscalServicoDAO().findByIdCtoCtoCooperada(idCtoCtoCooperada);
	}
	
	public List findNotaFiscalByIdConhecimento(Long idDoctoServico){
		return lmParceriaDao.findNotaFiscalByIdConhecimento(idDoctoServico);
	}
	
	public List findNotaFiscalByIdCooperada(Long idCooperada){
		return lmParceriaDao.findNotaFiscalByIdCooperada(idCooperada);
	}
}