package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.FaixaPesoParcelaTabelaCE;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.FaixaPesoParcelaTabelaCEDAO;

/**
 * Classe de servi�o para CRUD:
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.faixaPesoParcelaTabelaCEService"
 */
public class FaixaPesoParcelaTabelaCEService extends CrudService<FaixaPesoParcelaTabelaCE, Long> {

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setFaixaPesoParcelaTabelaCEDAO(FaixaPesoParcelaTabelaCEDAO faixaPesoParcelaTabelaCEDAO) {
		setDao( faixaPesoParcelaTabelaCEDAO );
	}

	private FaixaPesoParcelaTabelaCEDAO getFaixaPesoParcelaTabelaCEDAO() {
		return (FaixaPesoParcelaTabelaCEDAO)getDao();
	}
	
	public List<FaixaPesoParcelaTabelaCE> findFaixaPesoByIdParcelaTabelaCE(Long idParcelaTabelaCe) {
		return getFaixaPesoParcelaTabelaCEDAO().findFaixaPesoByIdParcelaTabelaCE(idParcelaTabelaCe);
	}
	
	@Override
	public void removeById(Long id) {
		// TODO Auto-generated method stub
		super.removeById(id);
	}
	
	@Override
	public Serializable store(FaixaPesoParcelaTabelaCE bean) {
		// TODO Auto-generated method stub
		return super.store(bean);
	}

	public BigDecimal findValorFaixaPeso(Long idTabelaColetaEntrega, BigDecimal qtdTotalFretePeso) {
		return getFaixaPesoParcelaTabelaCEDAO().findValorFaixaPeso(idTabelaColetaEntrega, qtdTotalFretePeso);
	}
	
	public FaixaPesoParcelaTabelaCE findFaixaPeso(Long idTabelaColetaEntrega, BigDecimal qtdTotalFretePeso) {
		return getFaixaPesoParcelaTabelaCEDAO().findFaixaPeso(idTabelaColetaEntrega, qtdTotalFretePeso);
}

	public FaixaPesoParcelaTabelaCE findFaixaInicial(Long idTabelaColetaEntrega) {
		return getFaixaPesoParcelaTabelaCEDAO().findFaixaInicial(idTabelaColetaEntrega);
	}
	
	public List<FaixaPesoParcelaTabelaCE> findFaixaPesoByIdParcelaTabelaCE(Long idParcelaTabelaCe,boolean orderByPsInicial) {
		return getFaixaPesoParcelaTabelaCEDAO().findFaixaPesoByIdParcelaTabelaCE(idParcelaTabelaCe,orderByPsInicial);
	}

	
}
