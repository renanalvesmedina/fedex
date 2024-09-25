package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.NotaFiscalCtoCooperada;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalCtoCooperadaDAO;
import com.mercurio.lms.sim.model.dao.LMParceriaDAO;


/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.notaFiscalCtoCooperadaService"
 */
public class NotaFiscalCtoCooperadaService extends CrudService<NotaFiscalCtoCooperada, Long> {
	
	private LMParceriaDAO lmParceriaDao;	

	public void setLmParceriaDao(LMParceriaDAO lmParceriaDao) {
		this.lmParceriaDao = lmParceriaDao;
	}

	/**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setNotaFiscalCtoCooperadaDAO(NotaFiscalCtoCooperadaDAO dao) {
        setDao( dao );
    }

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
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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