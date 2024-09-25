package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.contratacaoveiculos.model.ItChecklistTpMeioTransp;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ItChecklistTpMeioTranspDAO extends BaseCrudDao<ItChecklistTpMeioTransp, Long>{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ItChecklistTpMeioTransp.class;
    }
    protected void initFindByIdLazyProperties(Map fetchMode) {
    	fetchMode.put("tipoMeioTransporte",FetchMode.JOIN);
    	fetchMode.put("itemCheckList",FetchMode.JOIN);
    	super.initFindByIdLazyProperties(fetchMode);
    }
    protected void initFindPaginatedLazyProperties(Map fetchMode) {
    	fetchMode.put("tipoMeioTransporte",FetchMode.JOIN);
    	fetchMode.put("itemCheckList",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchMode);
    }
    
    public boolean findBeforeStore(ItChecklistTpMeioTransp bean){
    	DetachedCriteria dc = createDetachedCriteria();
    					 dc.createAlias("tipoMeioTransporte","TMT");
    					 dc.createAlias("itemCheckList","ICL");
    					 
    	if(bean.getIdItChecklistTpMeioTransp() != null)
    		dc.add(Restrictions.ne("idItChecklistTpMeioTransp",bean.getIdItChecklistTpMeioTransp()));
    	
    	dc.add(Restrictions.eq("TMT.idTipoMeioTransporte",bean.getTipoMeioTransporte().getIdTipoMeioTransporte()));
    	dc.add(Restrictions.eq("ICL.idItemCheckList",bean.getItemCheckList().getIdItemCheckList()));
    	
    	dc = JTVigenciaUtils.getDetachedVigencia(dc,bean.getDtVigenciaInicial(),bean.getDtVigenciaFinal());
    	return findByDetachedCriteria(dc).size()>0;
    }
    
    public List findItChecklistByIdTipoMeioTransporteSolicitacao(Long idTipoMeioTransporte, Long idChecklistMeioTransporte){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("new Map(tmt.tpMeioTransporte as tpMeioTransporte, " +
    			"tmt.dsTipoMeioTransporte as dsTipoMeioTransporte, " +
    			"icl.dsItemCheckList as dsItemCheckList, " +
    			"it.idItChecklistTpMeioTransp as idItChecklistTpMeioTransp, " +
    			"it.blObrigatorioAprovacao as blObrigatorioAprovacao) " );
    	    		
    	
    	hql.addFrom(ItChecklistTpMeioTransp.class.getName(),new StringBuffer("it ")
    			.append("join it.tipoMeioTransporte tmt ")
    			.append("join it.itemCheckList icl ")
    			.toString());
    	
    	    	
    	hql.addCriteria("tmt.idTipoMeioTransporte","=",idTipoMeioTransporte);
    	hql.addCriteria("it.tpItChecklistTpMeioTransp","=","T");
    	
    	hql.addCriteria("it.dtVigenciaInicial", "<=", JTDateTimeUtils.getDataAtual());
    	
    	hql.addCustomCriteria("(it.dtVigenciaFinal is null or it.dtVigenciaFinal >= ?)",JTDateTimeUtils.getDataAtual());

    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    
    public List findRespostasCheckList(Long idTipoMeioTransporte, Long idChecklistMeioTransporte){
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("new Map(tmt.tpMeioTransporte as tpMeioTransporte, " +
    			"tmt.dsTipoMeioTransporte as dsTipoMeioTransporte, " +
    			"icl.dsItemCheckList as dsItemCheckList, " +
    			"it.idItChecklistTpMeioTransp as idItChecklistTpMeioTransp, " +
    			"resp.idRespostaChecklist as idRespostaChecklist, " +
    			"resp.blAprovado as apr, " +
    			"it.blObrigatorioAprovacao as blObrigatorioAprovacao)");
    	    	
    	hql.addFrom(ItChecklistTpMeioTransp.class.getName(),new StringBuffer("it ")
    			.append("join it.tipoMeioTransporte tmt ")
    			.append("join it.itemCheckList icl ")
    			.append("left outer join it.respostaChecklists resp ")
    			.append("left outer join resp.checklistMeioTransporte check ")
    			.toString());
    	
    	hql.addCriteria("tmt.idTipoMeioTransporte","=",idTipoMeioTransporte);
    	hql.addCriteria("it.tpItChecklistTpMeioTransp","=","T");
    	
    	hql.addCriteria("check.idChecklistMeioTransporte", "=", idChecklistMeioTransporte);
    	
        hql.addCriteria("it.dtVigenciaInicial", "<=", JTDateTimeUtils.getDataAtual());
    	
    	hql.addCustomCriteria("(it.dtVigenciaFinal is null or it.dtVigenciaFinal >= ?)",JTDateTimeUtils.getDataAtual());

    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    
    
    
    
    public List findItChecklistMotByIdTipoMeioTransporteSolicitacao(Long idTipoMeioTransporte, String idMotorista ){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map(icl.dsItemCheckList as dsItemCheckList, " +
    			"it.idItChecklistTpMeioTransp as idItChecklistTpMeioTransp, " +
    			"it.blObrigatorioAprovacao as blObrigatorioAprovacao) ");
    			
    	hql.addFrom(ItChecklistTpMeioTransp.class.getName(),new StringBuffer("it ")
    			.append("join it.itemCheckList icl ")
    			.append("join it.tipoMeioTransporte tmt ")
    			.toString());
    	
    	
    	hql.addCriteria("tmt.idTipoMeioTransporte","=",idTipoMeioTransporte);
    	hql.addCriteria("it.tpItChecklistTpMeioTransp","=","M");
    	
    	hql.addCriteria("it.dtVigenciaInicial", "<=", JTDateTimeUtils.getDataAtual());
    	
    	hql.addCustomCriteria("(it.dtVigenciaFinal is null or it.dtVigenciaFinal >= ?)",JTDateTimeUtils.getDataAtual());
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    
    
    public List findRespostasMotorista(Long idTipoMeioTransporte, Long idChecklistMeioTransporte, String idMotorista ){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("new Map(icl.dsItemCheckList as dsItemCheckList, " +
    			"it.idItChecklistTpMeioTransp as idItChecklistTpMeioTransp, "
    			/*
    			+ "'"+tpIdentificacao+"' as tipoIdentificacao, " 
    			+ "'"+nrIdentificacaoFormatado+"' as nrIdentificacaoFormatado, " 
    			+ "'"+nmPessoa+"' as nmPessoa, " 
    			+ "'"+idMotorista+"' as idMotorista, " */
    			
    			+ "resp.idRespostaChecklist as idRespostaChecklist, " 
    			+ "resp.blAprovado as apr, " 
    			+ "it.blObrigatorioAprovacao as blObrigatorioAprovacao)");
    	
    	
    	hql.addFrom(ItChecklistTpMeioTransp.class.getName(),new StringBuffer("it ")
    			.append("join it.itemCheckList icl ")
    			.append("join it.tipoMeioTransporte tmt ")
    			.append("left outer join it.respostaChecklists resp ")
    			.append("left outer join resp.pessoa pes ")
    			.append("left outer join resp.checklistMeioTransporte check ")
    			.toString());
    	
    	
    	hql.addCriteria("tmt.idTipoMeioTransporte","=",idTipoMeioTransporte);
    	hql.addCriteria("it.tpItChecklistTpMeioTransp","=","M");
    	
    	
    	if(!idMotorista.equals(""))
    		hql.addCustomCriteria("(pes.idPessoa = ? or pes.idPessoa is null)",Long.valueOf(idMotorista));
    	
    	hql.addCriteria("check.idChecklistMeioTransporte", "=", idChecklistMeioTransporte);
    	
    	hql.addCriteria("it.dtVigenciaInicial", "<=", JTDateTimeUtils.getDataAtual());
    	
    	hql.addCustomCriteria("(it.dtVigenciaFinal is null or it.dtVigenciaFinal >= ?)",JTDateTimeUtils.getDataAtual());
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }
    
    
    
}