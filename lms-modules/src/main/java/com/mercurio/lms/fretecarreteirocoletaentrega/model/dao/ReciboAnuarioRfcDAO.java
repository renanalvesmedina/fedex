package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ReciboAnuarioRfc;
import com.mercurio.lms.fretecarreteiroviagem.model.ReciboFreteCarreteiro;

public class ReciboAnuarioRfcDAO extends BaseCrudDao<ReciboAnuarioRfc, Long>{

	@Override
	@SuppressWarnings("rawtypes")
	protected Class getPersistentClass() {
		return ReciboAnuarioRfc.class;
	}

	public boolean hasAnuarioVinculado(ReciboFreteCarreteiro recibofreteCarreteiro) {
		boolean existe = true;
		
		StringBuilder hql = new StringBuilder();    
		hql.append(" SELECT anuario ");
		hql.append("   FROM ");
		hql.append(ReciboAnuarioRfc.class.getName()).append(" anuario ");		
		hql.append("  WHERE anuario.idReciboFreteCarreteiro = ").append(recibofreteCarreteiro.getIdReciboFreteCarreteiro());
		
		List<ReciboAnuarioRfc> listaAgrupado = this.getAdsmHibernateTemplate().find(hql.toString());

		if (listaAgrupado.size() == 0){
			existe = false;
		}
		
		return existe;	
	}
}