package com.mercurio.lms.expedicao.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.expedicao.model.TBIntegration;
import com.mercurio.lms.expedicao.model.dao.NotaFiscalEletronicaRetornoDAO;

/**
 * Classe resposável por recuperar o retorno das Prefeituras na solução NDDigital
 * @author lucianos
 *
 */
public class NotaFiscalEletronicaRetornoService extends CrudService<TBIntegration, Long>{
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(TBIntegration notaFiscalEletronicaRetorno) {
		return super.store(notaFiscalEletronicaRetorno);
	}
	
	
	public List<TBIntegration> findNotaFiscalEletronicaRetornoNaoProcessadoByRpsNumber(Long rpsNumber){
		return getNotaFiscalEletronicaRetornoDAO().findNotaFiscalEletronicaRetornoNaoProcessadoByRpsNumber(rpsNumber); 
	}
	
	public List<TBIntegration> findNotaFiscalEletronicaRetornoByDocStatus(Integer docStatus){
		return getNotaFiscalEletronicaRetornoDAO().findNotaFiscalEletronicaRetornoByDocStatus( docStatus );
	}
	
	public String findDocData(Long id) {
		return getNotaFiscalEletronicaRetornoDAO().findDocData(id);
	}
	
	public NotaFiscalEletronicaRetornoDAO getNotaFiscalEletronicaRetornoDAO() {
		return (NotaFiscalEletronicaRetornoDAO)getDao();
	}

	public void setNotaFiscalEletronicaRetornoDAO(NotaFiscalEletronicaRetornoDAO notaFiscalEletronicaRetornoDAO) {
		setDao(notaFiscalEletronicaRetornoDAO);
	}

}
