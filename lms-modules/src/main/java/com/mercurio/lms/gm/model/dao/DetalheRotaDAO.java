package com.mercurio.lms.gm.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.DetalheRota;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DetalheRotaDAO extends BaseCrudDao<DetalheRota, Long> {
	/** 
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return DetalheRota.class;
	}
	
	/**
	 * Busca DetalheRota pelo ID.
	 * @param idDetalheRota
	 * @return
	 */
	public DetalheRota findDetalheRotaByid(Long idDetalheRota) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idDetalheRota",idDetalheRota));
		
		List<DetalheRota> listDetalheRota = findByDetachedCriteria(dc);
		if(listDetalheRota!=null && listDetalheRota.size()>0){
			return listDetalheRota.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * Busca DetalheRota pelo id da RotaEmbarque.
	 * 
	 * @author Samuel Alves
	 * @param String idRotaEmbarque.
	 * @return DetalheRota, nulo, caso negativo.
	 */
	public List<DetalheRota> findDetalheRotaByIdRotaEmbarque(Long idRotaEmbarque){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idRotaEmbarque",idRotaEmbarque));
		return findByDetachedCriteria(dc);
	}
	
	/**
	 * Busca DetalheRota pela sigla.
	 * 
	 * @author Samuel Alves
	 * @param String siglaRotaEmbarque.
	 * @return DetalheRota, nulo, caso negativo.
	 */
	public DetalheRota findDetalheRotaBySigla(String siglaRota){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("siglaRota",siglaRota).ignoreCase());
		List<DetalheRota> listDetalheRota = findByDetachedCriteria(dc);
		if(listDetalheRota!=null && listDetalheRota.size()>0){
			return listDetalheRota.get(0);
		}else{
			return null;
		}
	}
		
	/**
	 * Busca DetalheRota pela sigla da Rota.
	 * 
	 * @author Samuel Alves
	 * @param DetalheRota.
	 * @return DetalheRota, nulo, caso negativo.
	 */
	public DetalheRota findDetalheRotaByIdSigla(DetalheRota bean){
		DetachedCriteria dc = createDetachedCriteria();
		
		if (bean.getIdRotaEmbarque()!=null){
			dc.add(Restrictions.eq("idRotaEmbarque", bean.getIdRotaEmbarque()));
		}
		
		if (bean.getIdDetalheRota() != null){
			dc.add(Restrictions.ne("idDetalheRota", bean.getIdDetalheRota()));	
		}
		
		dc.add(Restrictions.eq("siglaRota", bean.getSiglaRota().toLowerCase()).ignoreCase());
		List<DetalheRota> listRotas = findByDetachedCriteria(dc);
		if(listRotas!=null && listRotas.size()>0){
			return listRotas.get(0);
		}else{
			return null;
		}
	}
}