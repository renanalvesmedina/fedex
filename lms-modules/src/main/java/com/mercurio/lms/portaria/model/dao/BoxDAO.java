package com.mercurio.lms.portaria.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.portaria.model.Box;
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
public class BoxDAO extends BaseCrudDao<Box, Long>
{
		
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("doca.terminal.filial", FetchMode.JOIN);
		lazyFindPaginated.put("doca", FetchMode.JOIN);
		lazyFindPaginated.put("doca.terminal", FetchMode.JOIN);
		lazyFindPaginated.put("doca.terminal.pessoa", FetchMode.JOIN);	
	}
	
	
	protected void initFindByIdLazyProperties(Map lazyFindById) {		
		lazyFindById.put("doca", FetchMode.JOIN);
		lazyFindById.put("doca.terminal", FetchMode.JOIN);		
		lazyFindById.put("doca.terminal.pessoa", FetchMode.JOIN);
		lazyFindById.put("doca.terminal.filial", FetchMode.JOIN);
		lazyFindById.put("doca.terminal.filial.pessoa", FetchMode.JOIN);
	}

    public List findCombo(Map<String, Object> criteria) {
    	DetachedCriteria dc = createDetachedCriteria();
    	ProjectionList pl = Projections.projectionList();
    		pl.add(Projections.property("nrBox"))
    			.add(Projections.property("dsBox"))
    			.add(Projections.property("idBox"));
    	dc.setProjection(pl)
    		.createAlias("doca","D")
    		.addOrder(Order.asc("nrBox"))
    		.addOrder(Order.asc("dsBox"));

    	if (criteria.get("doca.idDoca") != null)
    		dc.add(Restrictions.eq("D.idDoca",Long.valueOf((String)criteria.get("doca.idDoca"))));
    	return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    }

	public boolean verificaVigencia(Long idBox, Short nrBox, Long idDoca, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();

    	if (idBox != null)
    		dc.add(Restrictions.ne("idBox", idBox));

    	dc.add(Restrictions.eq("nrBox", nrBox));    	
    	dc.add(Restrictions.eq("doca.idDoca", idDoca));
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);

    	dc.setProjection(Projections.rowCount());

    	return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}

	/**
	 * Consulta as docas ou boxes preferenciais disponiveis para o meio de transporte na filial e doca informada
	 * @param idFilial
	 * @param idMeioTransporte
	 * @param idDoca
	 * @return
	 */
	public List<Map<String, Object>> findDocaBoxesDisponivelParaMeioTransporte(Long idFilial, Long idMeioTransporte, Long idDoca, String tpControleCarga){
		SqlTemplate sql = new SqlTemplate();

		TimeOfDay hrAtual = JTDateTimeUtils.getHorarioAtual();
		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

		if (idDoca != null){
			sql.addProjection("distinct new Map(b.idBox", "idBox");
			sql.addProjection("b.nrBox", "nrBox");
			sql.addProjection("b.dsBox", "dsBox)");
		} else {
			sql.addProjection("distinct new Map(d.idDoca", "idDoca");
			sql.addProjection("d.nrDoca", "nrDoca");
			sql.addProjection("d.dsDoca", "dsDoca)");	
		}		

		sql.addInnerJoin("Box", "b");
		sql.addInnerJoin("b.meioTransporteRodoBoxs", "mtr");
		sql.addInnerJoin("b.doca", "d");
		sql.addInnerJoin("d.terminal", "t");
		sql.addInnerJoin("b.boxFinalidades", "bf");
		sql.addInnerJoin("bf.finalidade", "f");	

		sql.addCustomCriteria("(f.tpControleCarga is null or f.tpControleCarga = ?)", tpControleCarga); 		
		sql.addCriteria("t.filial.idFilial", "=", idFilial);
		sql.addCriteria("mtr.meioTransporteRodoviario.idMeioTransporte", "=", idMeioTransporte);		
		sql.addCustomCriteria("f.blDescarga = 'S'");
		sql.addCustomCriteria("b.tpSituacaoBox = 'L'");
		sql.addCustomCriteria("d.tpSituacaoDoca = 'L'");
		sql.addCriteria("d.idDoca", "=", idDoca);

		sql.addCustomCriteria("? between mtr.dtVigenciaInicial and mtr.dtVigenciaFinal", dtAtual);
		sql.addCustomCriteria("? between b.dtVigenciaInicial and b.dtVigenciaFinal", dtAtual);
		sql.addCustomCriteria("? between d.dtVigenciaInicial and d.dtVigenciaFinal", dtAtual);

		sql.addCriteria("bf.hrInicial", "<=", hrAtual);
		sql.addCriteria("bf.hrFinal", ">=", hrAtual);

		if (idDoca != null){
			sql.addOrderBy("b.nrBox");
		} else {
			sql.addOrderBy("d.nrDoca");
		}

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		
	}

	/**
	 * Consulta as docas ou boxes nao-preferenciais disponiveis para o meio de transporte na filial e doca informada
	 * @param idFilial
	 * @param idMeioTransporte
	 * @param idDoca
	 * @return
	 */
	public List<Map<String, Object>> findBoxDisponivel(Long idFilial, Long idMeioTransporte, Long idDoca, String tpControleCarga){
		SqlTemplate sql = new SqlTemplate();
		
		TimeOfDay hrAtual = JTDateTimeUtils.getHorarioAtual();
		YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
		
		if (idDoca != null){
			sql.addProjection("distinct new Map(b.idBox", "idBox");
		    sql.addProjection("b.nrBox", "nrBox");
			sql.addProjection("b.dsBox", "dsBox)");
		} else {
			sql.addProjection("distinct new Map(d.idDoca", "idDoca");
			sql.addProjection("d.nrDoca", "nrDoca");
			sql.addProjection("d.dsDoca", "dsDoca)");	
		}
		 		
		sql.addInnerJoin("Box", "b");		
		sql.addInnerJoin("b.doca", "d");
		sql.addInnerJoin("d.terminal", "t");
		sql.addInnerJoin("b.boxFinalidades", "bf");
		sql.addInnerJoin("bf.finalidade", "f");		
		
		//Nao deve existir finalidade preferencial (neste caso, a doca/box jah foi selecionada na consulta anterior)
		sql.addCustomCriteria(new StringBuffer()
								.append("not exists (SELECT 1")
								.append("			 FROM MeioTransporteRodoBox mt")                            
								.append("			 WHERE mt.meioTransporteRodoviario.id = ?") 
								.append("					AND b.id = mt.box.id")
								.append("					AND ? between mt.dtVigenciaInicial")
								.append("								  and mt.dtVigenciaFinal)").toString(),
							new Object[]{idMeioTransporte, dtAtual});

		sql.addCustomCriteria("? between b.dtVigenciaInicial and b.dtVigenciaFinal", dtAtual);
		sql.addCustomCriteria("? between d.dtVigenciaInicial and d.dtVigenciaFinal", dtAtual);
		sql.addCustomCriteria("(f.tpControleCarga is null or f.tpControleCarga = ?)", tpControleCarga); 
		sql.addCriteria("t.filial.idFilial", "=", idFilial);		
		sql.addCustomCriteria("f.blDescarga = 'S'");
		sql.addCustomCriteria("b.tpSituacaoBox = 'L'");
		sql.addCustomCriteria("d.tpSituacaoDoca = 'L'");
		sql.addCriteria("d.idDoca", "=", idDoca);
		sql.addCriteria("bf.hrInicial", "<=", hrAtual);
		sql.addCriteria("bf.hrFinal", ">=", hrAtual);		

		if (idDoca != null){
			sql.addOrderBy("b.nrBox");
		} else {
			sql.addOrderBy("d.nrDoca");
		}

		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}
	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Box.class;
    }
    
    public Integer getRowCountBoxesVigenteByTerminal(Long idTerminal) {
        DetachedCriteria dc = createDetachedCriteria()
        						.setProjection(Projections.rowCount())
        						.createAlias("doca","DO")
        						.createAlias("DO.terminal","TE");
        					
   	   			
   	   YearMonthDay today = JTDateTimeUtils.getDataAtual();
   	   
   	   if (idTerminal != null)
   		   dc.add(Restrictions.eq("TE.idTerminal", idTerminal));
   	   
   	   Criterion vigencia = 
			   	Restrictions.and(
			   			Restrictions.le("dtVigenciaInicial",today),
			   			Restrictions.ge("dtVigenciaFinal",today));
   	   
   	   dc.add(vigencia);
   	   return (Integer)dc.getExecutableCriteria(getSession()).uniqueResult();
     }

    
	public boolean isBoxVigente(Long idBox, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
		DetachedCriteria dc = createDetachedCriteria();
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("idBox",idBox));
		dc.add(Restrictions.le("dtVigenciaInicial",dtVigenciaInicial));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dtVigenciaFinal)));
		
		return ((Integer)findByDetachedCriteria(dc).get(0)).intValue() > 0;
	}
	
    /**
     * Método que retorna uma combo de boxs vigentes para a filial passada por parâmetro.
     * @param criteria
     * @return
     */	
    public List<Map<String, Object>> findBoxVigentePorFilial(Long idFilial) {
		StringBuffer hql = new StringBuffer();

		hql.append("select new map(	box.idBox as idBox,  ");
		hql.append("				box.nrBox as nrBox,  ");
		hql.append("				box.dsBox as dsBox ) ");		
				
		hql.append(" from ").append(Box.class.getName()).append(" box ");		
		hql.append(" join box.doca as doca ");
		hql.append(" join doca.terminal as terminal ");
		hql.append(" join terminal.filial as filial ");

		hql.append(" where box.tpSituacaoBox in (:tpSituacaoBoxLiberado, :tpSituacaoBoxOcupado)  ");
		hql.append(" 	   and filial.id = :idFilial ");
		hql.append("	   and :dataAtual between box.dtVigenciaInicial and box.dtVigenciaFinal ");
		hql.append("	   and :dataAtual between doca.dtVigenciaInicial and doca.dtVigenciaFinal ");
		hql.append("	   and :dataAtual between terminal.dtVigenciaInicial and terminal.dtVigenciaFinal ");
		hql.append(" order by box.nrBox");
		
    	Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
    	query.setString("tpSituacaoBoxLiberado", "L");
    	query.setString("tpSituacaoBoxOcupado", "O"); // listo o box ocupado para o item aparecer na combo de descarga quando o box ja tiver sido selecionado na recepção
    	query.setLong("idFilial", idFilial);
    	query.setParameter("dataAtual", JTDateTimeUtils.getDataAtual(), Hibernate.custom(JodaTimeYearMonthDayUserType.class));
		
    	return query.list();
    }	
	
    
    public List<Box> findLiberadoVigenteByFilial(Long idFilial){
    	DetachedCriteria dc = DetachedCriteria.forClass(Box.class);
    	
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	
    	dc.setFetchMode("doca", FetchMode.JOIN);
    	dc.setFetchMode("dc.terminal", FetchMode.JOIN);
    	dc.setFetchMode("terminal.filial", FetchMode.JOIN);
    	
    	dc.createAlias("doca", "dc");
    	dc.createAlias("dc.terminal", "ter");
    	dc.createAlias("ter.filial", "fil");
    	
    	dc.add(Restrictions.le("dtVigenciaInicial",dataAtual));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataAtual)));
    	dc.add(Restrictions.le("dc.dtVigenciaInicial",dataAtual));
		dc.add(Restrictions.ge("dc.dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataAtual)));
    	dc.add(Restrictions.le("ter.dtVigenciaInicial",dataAtual));
		dc.add(Restrictions.ge("ter.dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataAtual)));
		dc.add(Restrictions.eq("tpSituacaoBox", "L"));
		
	   	dc.add(Restrictions.eq("fil.id", idFilial));
		
		dc.addOrder(Order.asc("nrBox"));
    	
    	return findByDetachedCriteria(dc);

    }


	public List<Box> findByNrBoxAndIdFilial(Short nrBox, Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(Box.class);
    	
    	YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
    	
    	dc.setFetchMode("doca", FetchMode.JOIN);
    	dc.setFetchMode("dc.terminal", FetchMode.JOIN);
    	dc.setFetchMode("terminal.filial", FetchMode.JOIN);
    	
    	dc.createAlias("doca", "dc");
    	dc.createAlias("dc.terminal", "ter");
    	dc.createAlias("ter.filial", "fil");
    	
    	dc.add(Restrictions.le("dtVigenciaInicial",dataAtual));
		dc.add(Restrictions.ge("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataAtual)));
    	dc.add(Restrictions.le("dc.dtVigenciaInicial",dataAtual));
		dc.add(Restrictions.ge("dc.dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataAtual)));
    	dc.add(Restrictions.le("ter.dtVigenciaInicial",dataAtual));
		dc.add(Restrictions.ge("ter.dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataAtual)));
		dc.add(Restrictions.eq("tpSituacaoBox", "L"));
    	
    	dc.add(Restrictions.eq("fil.id", idFilial));
    	dc.add(Restrictions.eq("nrBox", nrBox));
    	
    	return findByDetachedCriteria(dc);
	}
	
}