package com.mercurio.lms.municipios.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OrdemFilialFluxoDAO extends BaseCrudDao<OrdemFilialFluxo, Long> {

	
	protected Class getPersistentClass() {
		// TODO Auto-generated method stub
		return OrdemFilialFluxo.class;
	}
	
	/**
	 * Método que recebe como parâmetro o ID da Filial de Origem e da Filial de Destino 
	 * e retorna uma list de idFilialFluxo caso o nrOrdem da Filial de Destino no OrdemFilialFluxo
	 * seja maior que o nrOrdem da Filial de Origem no OrdemFilialFluxo.
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return List
	 */
    public List findIdFluxoFilialByIdFilialOrigemAndIdFilialDestino(Long idFilialOrigem, Long idFilialDestino) {    	
       	StringBuffer hql = new StringBuffer();
       	
    	hql.append(" select offo.fluxoFilial.id ");
       	hql.append(" from " + OrdemFilialFluxo.class.getName() + " offo, ");
       	hql.append(" 	  " + OrdemFilialFluxo.class.getName() + " offd ");
    	hql.append(" where offo.fluxoFilial = offd.fluxoFilial ");
        hql.append(" 	   and offd.nrOrdem > offo.nrOrdem ");
        hql.append(" 	   and offo.filial.id = ? ");
    	hql.append("	   and offd.filial.id = ? ");
    	
    	return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idFilialOrigem, idFilialDestino});
    } 
    
    public List findOrdensFilialFluxoByIdFilialOrigemAndIdFilialDestino(Long idFilialOrigem, Long idFilialDestino, YearMonthDay dtConsulta) {    	
       	StringBuffer hql = new StringBuffer();
       	
    	hql.append(" select orff ");
       	hql.append(" from " + OrdemFilialFluxo.class.getName() + " orff ");
       	hql.append("     join orff.fluxoFilial as ffl ");
    	hql.append(" where ffl.filialByIdFilialOrigem.id = ? ");
    	hql.append("       and ffl.filialByIdFilialDestino.id = ? ");
        hql.append(" 	   and ? BETWEEN ffl.dtVigenciaInicial AND ffl.dtVigenciaFinal ");
        hql.append(" order by orff.nrOrdem ");
    	return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idFilialOrigem, idFilialDestino, dtConsulta});
    } 
    
    /**
     * Obtém a quantidade de registros para uma pesquisa filtrando por filial origem, filial destino e documento de servico do fluxo filial.
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountFluxoFilialByFilialOrigemDestinoAndDoctoServico(Long idFilialOrigem, Long idFilialDestino, Long idDoctoServico) {
       	StringBuffer hql = new StringBuffer();
       	
       	hql.append(" from " + OrdemFilialFluxo.class.getName() + " offo, ");
       	hql.append(" 	  " + OrdemFilialFluxo.class.getName() + " offd ");
       	hql.append(" inner join offo.fluxoFilial as ff ");
       	hql.append(" inner join ff.doctoServicos as ds ");
    	hql.append(" where offo.fluxoFilial = offd.fluxoFilial ");
        hql.append(" 	   and offd.nrOrdem > offo.nrOrdem ");
        hql.append(" 	   and offo.filial.id = :filialOrigem ");
    	hql.append("	   and offd.filial.id = :filialDestino ");
    	hql.append("       and ds.id = :doctoServico ");

    	Map map = new HashMap();
    	map.put("doctoServico", idDoctoServico);
    	map.put("filialOrigem", idFilialOrigem);
    	map.put("filialDestino", idFilialDestino);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), map);
    }
    
	/**
	 * Método que recebe como parâmetro o ID do Fluxo Filial e o Número de Ordem e retorna um objeto de OrdemFilialFluxo.
	 * @param idFluxoFilial
	 * @param nrOrdem
	 * @return OrdemFilialFluxo
	 */
    public OrdemFilialFluxo findOrdemFilialFluxoByIdFluxoFilialByNrOrdem(Long idFluxoFilial, Byte nrOrdem) {    	
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(OrdemFilialFluxo.class.getName() + " off");
   		hql.addCriteria("off.fluxoFilial.id", "=", idFluxoFilial);
   		hql.addCriteria("off.nrOrdem", "=", nrOrdem);
   		return (OrdemFilialFluxo)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    } 
    
    public List findByIdFluxoFilial(Long idFluxoFilial) {
    	return getAdsmHibernateTemplate().getSessionFactory()
				.getCurrentSession().createCriteria(OrdemFilialFluxo.class)
				.add(Restrictions.eq("fluxoFilial.id", idFluxoFilial))
				.addOrder(Order.asc("nrOrdem"))
				.setFetchMode("filial",FetchMode.JOIN)
				.list();    	
    }

}
