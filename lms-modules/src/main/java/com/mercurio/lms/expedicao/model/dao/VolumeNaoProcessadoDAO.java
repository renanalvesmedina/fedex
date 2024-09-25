package com.mercurio.lms.expedicao.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.expedicao.model.ControleEsteira;
import com.mercurio.lms.expedicao.model.VolumeNaoProcessado;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolumeNaoProcessadoDAO extends BaseCrudDao<VolumeNaoProcessado, Long> {
	
	
	public List<VolumeNaoProcessado> findVolumesByNrLote(String nrLote) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select vnp");
		sql.append("  from "+getPersistentClass().getSimpleName()+" vnp");
		sql.append("	where vnp.nrLote = ?");		
		List param = new ArrayList();		
		param.add(nrLote);
		
		return (List<VolumeNaoProcessado>)getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
	}
			
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return VolumeNaoProcessado.class;
	}
}
