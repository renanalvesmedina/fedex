 package com.mercurio.lms.fretecarreteirocoletaentrega.model.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.ParcelaTabelaCeCC;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntregaCC;
 
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ParcelaTabelaCeCCDAO extends BaseCrudDao<ParcelaTabelaCeCC, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
	   return ParcelaTabelaCeCC.class;
	}
	
	public void removeByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer();
		sql.append("select tcc.idTabelaColetaEntregaCC from ");
		sql.append(TabelaColetaEntregaCC.class.getName() + " as tcc ");
		sql.append("where tcc.controleCarga.idControleCarga = ? ");
		
		List param = new ArrayList();
    	param.add(idControleCarga);
		
		List idTabelasColetasEntregas = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		for (Object id : idTabelasColetasEntregas) {
			
			StringBuffer delete = new StringBuffer();
			delete.append("delete from " + ParcelaTabelaCeCC.class.getName()).append(" as p ");
			delete.append("where p.tabelaColetaEntregaCC.idTabelaColetaEntregaCC = :id ");
			
			getAdsmHibernateTemplate().removeById(delete.toString(), (Serializable)id);	
		}
		
		
		
		
	}


}