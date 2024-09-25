
package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.ControleFormImpressora;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica��o
 * atrav�s do suporte ao Hibernate em conjunto com o Spring.
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ControleFormImpressoraDAO extends BaseCrudDao<ControleFormImpressora, Long> {

	/**
	 * Nome da classe que o DAO � respons�vel por persistir.
	 */
	protected final Class getPersistentClass() {
		return ControleFormImpressora.class;
	}

	/**
	 * 
	 * @param idControleFormulario
	 * @return
	 */
	public List findByControleFormulario(Long idControleFormulario){
		return findByDetachedCriteria(
				createDetachedCriteria().add(
						Restrictions.eq("controleFormulario.idControleFormulario",idControleFormulario)));
	}
	
	/**
	 * Consulta os v�nculos com impressoras que cont�m o intervalo informado, retornando <bold>true</bold> se existir v�nculos.
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 * @return exist�ncia de v�nculos com impressoras
	 */
	public boolean verificaIntervaloForm(Long nrFormularioInicial, Long nrFormularioFinal){

		DetachedCriteria dc = createDetachedCriteria();

		dc.add(
			Restrictions.and(
					Restrictions.and(
						Restrictions.le("nrFormularioInicial",nrFormularioInicial),
						Restrictions.ge("nrFormularioFinal",nrFormularioInicial)),
					Restrictions.and(
						Restrictions.le("nrFormularioInicial",nrFormularioFinal),
						Restrictions.ge("nrFormularioFinal",nrFormularioFinal))
					));

		dc.setProjection(Projections.count("idControleFormImpressora"));

		List list = findByDetachedCriteria(dc);

		int count = ((Long) list.get(0)).intValue();
		return ( count <= 0 );
	}
	

}