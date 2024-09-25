package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.HistoricoVolume;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HistoricoVolumeDAO extends BaseCrudDao<HistoricoVolume, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return HistoricoVolume.class;
	}

	/**
	 * Busca HistoricoVolume pelo codigo do volume.
	 * 
	 * @author Samuel Alves
	 * @param String codigoVolume.
	 * @return Volume, nulo, caso negativo.
	 */
	public HistoricoVolume findHistoricoVolumeByCodigoVolume(String codigoVolume){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("codigoVolume",codigoVolume));
		return (HistoricoVolume)findByDetachedCriteria(dc).get(0);
	}
	
	public void storeHistoricoVolume(HistoricoVolume historicoVolume) {
		store(historicoVolume);
	}
	
	/**
	 * Retorna a lista de historico volume onde o codigo_volume seja igual ao 
	 * passado como parametro
	 * @param codigoVolume
	 * @return
	 */
	public List<HistoricoVolume> findHistoricosVolumesByCodigoVolume(String codigoVolume){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("codigoVolume",codigoVolume));
		return findByDetachedCriteria(dc);
	}
	
}