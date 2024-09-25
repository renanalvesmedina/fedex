package com.mercurio.lms.carregamento.model.dao;

import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.carregamento.model.DispCarregIdentificado;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DispCarregIdentificadoDAO extends BaseCrudDao<DispCarregIdentificado, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DispCarregIdentificado.class;
    }
    
	private DetachedCriteria getDetachedCriteria(Long idCarregamentoDescarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(DispCarregIdentificado.class);
		dc.add(Restrictions.eq("carregamentoDescarga.id", idCarregamentoDescarga));
		
		return dc;
	}
	
	public List findDispCarregIdentificadoByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
		return super.findByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga));		
	}	
	
	public Integer getRowCountDispCarregIdentificadoByIdCarregamentoDescarga(Long idCarregamentoDescarga) {
		return getAdsmHibernateTemplate()
		.getRowCountByDetachedCriteria(getDetachedCriteria(idCarregamentoDescarga)
				.setProjection(Projections.rowCount()));
	}	
	
    /**
     * Salva os DispCarregIdentificado de um CarregamentoDescarga
     * 
     * @param itemsDispCarregIdentificado
     */
    public void storeDispCarregIdentificado(List itemsDispCarregIdentificado) {
		getAdsmHibernateTemplate().saveOrUpdateAll(itemsDispCarregIdentificado);
	}	
	
    /**
     * Localiza os registros DispCarregIdentificado a partir de um idManifesto.
     * @param idManifesto
     * @return
     */
    public List findDispCarregIdentificadoByIdManifesto(Long idManifesto){
    	DetachedCriteria dc = DetachedCriteria.forClass(DispCarregIdentificado.class, "dc");
    	dc.createAlias("dc.carregamentoPreManifesto", "cpm");
    	dc.createAlias("cpm.manifesto", "man");
    	dc.createAlias("dc.carregamentoDescarga", "cad"); //Utilizado para fazer fetch
    	dc.createAlias("dc.dispositivoUnitizacao", "dis"); //Utilizado para fazer fetch
    	dc.add(Restrictions.eq("man.id", idManifesto));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }
    
    /**
     * Método que verifica se o dispositivo de unitização passado por parâmetro faz parte do controle de carga
     * @param idControleCarga
     * @param idDispositivoUnitizacao
     * @return
     */
    public DispCarregIdentificado findByDispositivoUnitizacaoAndControleCarga(Long idControleCarga, Long idDispositivoUnitizacao) {
    	DetachedCriteria dc = DetachedCriteria.forClass(DispCarregIdentificado.class);
    	dc.createAlias("carregamentoDescarga.controleCarga", "controleCarga");
    	dc.setFetchMode("carregamentoDescarga", FetchMode.JOIN);
    	dc.setFetchMode("controleCarga", FetchMode.JOIN);
    	dc.add(Restrictions.eq("dispositivoUnitizacao.id", idDispositivoUnitizacao));
    	dc.add(Restrictions.eq("controleCarga.id", idControleCarga));
    	return (DispCarregIdentificado) getAdsmHibernateTemplate().findUniqueResult(dc);
    }
    
    
    /**
     * Busca a lista de dispositivos carregados
     * @param idControleCarga
     * @return
     */
	public List<DispCarregIdentificado> findListDispCarregIdentificadoByControleCarga(Long idControleCarga) {
		DetachedCriteria dc = DetachedCriteria.forClass(DispCarregIdentificado.class)
		.setFetchMode("carregamentoDescarga", FetchMode.JOIN)
		.createAlias("carregamentoDescarga.controleCarga", "controleCarga")
		.setFetchMode("controleCarga", FetchMode.JOIN)
		.setFetchMode("dispositivoUnitizacao", FetchMode.JOIN)
		.createAlias("dispositivoUnitizacao.tipoDispositivoUnitizacao", "tipoDispositivoUnitizacao")
		.setFetchMode("tipoDispositivoUnitizacao", FetchMode.JOIN)
		.createAlias("dispositivoUnitizacao.empresa", "empresa")
		.setFetchMode("empresa", FetchMode.JOIN)
		.createAlias("empresa.pessoa", "pessoa")
		.setFetchMode("pessoa", FetchMode.JOIN)

		.add(Restrictions.eq("controleCarga.id", idControleCarga));
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}
	
}