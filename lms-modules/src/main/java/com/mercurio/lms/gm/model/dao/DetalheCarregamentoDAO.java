package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DetalheCarregamentoDAO extends BaseCrudDao<DetalheCarregamento, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DetalheCarregamento.class;
	}
	
	/**
	 * Busca DetalheCarregamento pelo codigo do volume.
	 * 
	 * @author Samuel Alves
	 * @param String codigoVolume.
	 * @return DetalheCarregamento, nulo, caso negativo.
	 */
	public DetalheCarregamento findDetalheCarregamentoByCodigoVolume(String codigoVolume){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("codigoVolume",codigoVolume));
		List<DetalheCarregamento> listDetalheCarregamento = findByDetachedCriteria(dc);
		if(listDetalheCarregamento!= null && listDetalheCarregamento.size()>0){
			return (DetalheCarregamento) findByDetachedCriteria(dc).get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca DetalheCarregamento pelo detalheCarregamento.
	 * 
	 * @author Samuel Alves
	 * @param Long MapaCarregamento .
	 * @return List<DetalheCarregamento>, nulo, caso negativo.
	 */
	public List<DetalheCarregamento> findDetalheCarregamentoByMapaCarregamento(Long mapaCarregamento){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("mapaCarregamento",mapaCarregamento));
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Busca DetalheCarregamento pelo Mapa e CodigoDestino.
	 * 
	 * @author Samuel Alves
	 * @param Long MapaCarregamento.
	 * @param String CodigoDestino.
	 * @return List<DetalheCarregamento>, nulo, caso negativo.
	 */
	public List<DetalheCarregamento> findDetalheCarregamentoByMapaCarregamentoCodigoDestino(Long mapaCarregamento, String codigoDestino){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("mapaCarregamento",mapaCarregamento));
		dc.add(Restrictions.eq("codigoDestino",codigoDestino));
		return findByDetachedCriteria(dc);
	}
	
	public void store(DetalheCarregamento detalheCarregamento) {
		super.store(detalheCarregamento);
	}
	
	private void storeAll(List detalheCarregamentoList) {
		getAdsmHibernateTemplate().saveOrUpdateAll(detalheCarregamentoList);
		getAdsmHibernateTemplate().flush();
	}
}