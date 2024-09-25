package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.PostoPassagemRotaColEnt;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PostoPassagemRotaColEntDAO extends BaseCrudDao<PostoPassagemRotaColEnt, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return PostoPassagemRotaColEnt.class;
    }
    
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    }
    
	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("rotaColetaEntrega", FetchMode.JOIN);
		fetchModes.put("rotaColetaEntrega.filial", FetchMode.JOIN);
		fetchModes.put("rotaColetaEntrega.filial.pessoa", FetchMode.JOIN);		
		fetchModes.put("postoPassagem", FetchMode.JOIN);
		fetchModes.put("postoPassagem.municipio", FetchMode.JOIN);    	
		fetchModes.put("postoPassagem.rodovia", FetchMode.JOIN);    	
		fetchModes.put("postoPassagem.concessionaria", FetchMode.JOIN);
		fetchModes.put("postoPassagem.concessionaria.pessoa", FetchMode.JOIN);		
	}

    public boolean isVigenciaValida(PostoPassagemRotaColEnt p) {
    	DetachedCriteria dc = createDetachedCriteria();
    	if (p.getIdPostoPassagemRotaColEnt() != null)
    		dc.add(Restrictions.ne("idPostoPassagemRotaColEnt",p.getIdPostoPassagemRotaColEnt()));
    	dc.add(Restrictions.eq("rotaColetaEntrega.id",p.getRotaColetaEntrega().getIdRotaColetaEntrega()));
    	dc.add(Restrictions.eq("postoPassagem.id",p.getPostoPassagem().getIdPostoPassagem()));
    	
    	JTVigenciaUtils.getDetachedVigencia(dc,p.getDtVigenciaInicial(),p.getDtVigenciaFinal());
    	List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (rs == null || rs.size() == 0)
    		return true;
    	return false;
    } 
    
    
	/**
	 * Andresa Vargas
	 * 
	 * Consulta padrao da tela
	 * 
	 * @param idFilial
	 * @param idPostoPassagem
	 * @param idRotaColetaEntrega
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(Long idFilial, Long idPostoPassagem, Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, FindDefinition findDef){
		SqlTemplate sql = getSqlTemplate(idFilial, idPostoPassagem, idRotaColetaEntrega, dtVigenciaInicial, dtVigenciaFinal);
		ResultSetPage findPaginated = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		return findPaginated;
	}
	
	/**
	 * Andresa Vargas
	 * 
	 * Retorno do numero de registros da tela
	 * 
	 * @param idFilial
	 * @param idPostoPassagem
	 * @param idRotaColetaEntrega
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */
	public Integer getRowCountCustom(Long idFilial, Long idPostoPassagem, Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
		SqlTemplate sql = getSqlTemplate(idFilial, idPostoPassagem, idRotaColetaEntrega, dtVigenciaInicial, dtVigenciaFinal);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}
	
	/**
	 * SQLTemplate da consulta de posto de passagem por rota coleta entrega
	 * @param idFilial
	 * @param idPostoPassagem
	 * @param idRotaColetaEntrega
	 * @param dtVigenciaInicial
	 * @param dtVigenciaFinal
	 * @return
	 */ 
	private SqlTemplate getSqlTemplate(Long idFilial, Long idPostoPassagem, Long idRotaColetaEntrega, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal){
    	SqlTemplate sql = new SqlTemplate(); 
    	sql.addProjection("new Map(filial.sgFilial as rotaColetaEntrega_filial_sgFilial, " +
    			"pessoa.nmFantasia as rotaColetaEntrega_filial_pessoa_nmFantasia, " +
    			"rotaCe.nrRota as rotaColetaEntrega_nrRota, " +
    			"rotaCe.dsRota as rotaColetaEntrega_dsRota, " +
    			
    			"posto.tpPostoPassagem as postoPassagem_tpPostoPassagem, " +
    			"municipio.nmMunicipio as postoPassagem_municipio_nmMunicipio, " +
    			"posto.tpSentidoCobranca as postoPassagem_tpSentidoCobranca, " +
    			"rodovia.sgRodovia as postoPassagem_rodovia_sgRodovia, " +
    			"posto.nrKm as postoPassagem_nrKm, " +
    			"postCe.idPostoPassagemRotaColEnt as idPostoPassagemRotaColEnt, " +
    			
    			"postCe.dtVigenciaInicial as dtVigenciaInicial, " +
    			"postCe.dtVigenciaFinal as dtVigenciaFinal)");
	    	
    	StringBuffer sb = new StringBuffer();
        sb.append(new StringBuffer(PostoPassagemRotaColEnt.class.getName()).append(" AS postCe ")
				  .append("INNER JOIN postCe.rotaColetaEntrega as rotaCe ")
				  .append("INNER JOIN postCe.postoPassagem as posto ")
				  .append("INNER JOIN rotaCe.filial as filial ")
				  .append("INNER JOIN filial.pessoa as pessoa ")
				  .append("INNER JOIN posto.municipio as municipio ")
				  .append("LEFT OUTER JOIN posto.rodovia as rodovia ")
	    		  );
    	 
    	sql.addFrom(sb.toString());
    	
    	sql.addCriteria("filial.idFilial","=",idFilial);
    	sql.addCriteria("posto.idPostoPassagem","=",idPostoPassagem);
    	sql.addCriteria("rotaCe.idRotaColetaEntrega","=",idRotaColetaEntrega);
    	sql.addCriteria("postCe.dtVigenciaInicial",">=",dtVigenciaInicial, YearMonthDay.class);
    	sql.addCriteria("postCe.dtVigenciaFinal","<=",dtVigenciaFinal, YearMonthDay.class);
    	
    	sql.addOrderBy("filial.sgFilial");
    	sql.addOrderBy("rotaCe.nrRota");
    	sql.addOrderBy("municipio.nmMunicipio");
    	sql.addOrderBy("rodovia.sgRodovia");
    	sql.addOrderBy("postCe.dtVigenciaInicial");
    	
    	return sql; 
	}



}