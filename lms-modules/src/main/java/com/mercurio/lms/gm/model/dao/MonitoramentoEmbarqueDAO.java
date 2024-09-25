package com.mercurio.lms.gm.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.hibernate.Hibernate;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;
import com.mercurio.lms.carregamento.model.RotaEmbarque;
import com.mercurio.lms.carregamento.model.Volume;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.util.JTDateTimeUtils;


/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MonitoramentoEmbarqueDAO extends BaseCrudDao<CabecalhoCarregamento, Long> {
	@Override
	protected Class getPersistentClass() {
		 return CabecalhoCarregamento.class;
	}
	
	
	@Override
	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = this.getDetachedCriteria(criteria);
		List rs = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		
		
		SqlTemplate sql2 = this.getDetachedCriteriaAberto(criteria);
		List rs2 = getAdsmHibernateTemplate().find(sql2.getSql(),sql2.getCriteria());
		Integer i = 0;
		if( rs != null ){
			if(rs2!= null){
				rs.addAll(rs2);
			i = rs.size();
				return i;
	}
			i = rs.size();
		}
		return i;
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
	 
		SqlTemplate sql = this.getDetachedCriteria(criteria);
		
		List listResultSetPage = getAdsmHibernateTemplate().find(sql.toString(),sql.getCriteria());
		 
		List listResultado = new ArrayList();
		
		List rs2 = null;
		
		if(criteria.get("statusCarregamento").equals("") || criteria.get("statusCarregamento").equals("1") || criteria.get("statusCarregamento").equals("5")){
			if(criteria.get("mapaCarregamento").equals("")){
				if(criteria.get("dataDisponivel").equals("")){
					SqlTemplate sql2 = this.getDetachedCriteriaAberto(criteria);
					rs2 = getAdsmHibernateTemplate().find(sql2.toString(),sql2.getCriteria());
			
					
	}
			}	
		}
		
		
		boolean temCodigoVolume = (!"".equals(criteria.get("codigoVolume")) && listResultSetPage.isEmpty());
		  
		if(temCodigoVolume){ 
			if(criteria.get("statusCarregamento").equals("") && criteria.get("rotaCarregamento").equals("") && criteria.get("idMeioTransporte").equals("") && criteria.get("matriculaChefia").equals("")){
				listResultSetPage = getAdsmHibernateTemplate().find(this.getDetachedCriteriaDetalhe(criteria).toString(),getDetachedCriteriaDetalhe(criteria).getCriteria());
				listResultado.addAll(listResultSetPage);
			}			
			
		}
		
		if(!temCodigoVolume){
		
			if(rs2!=null){
				listResultado.addAll(rs2);
			}
			
			listResultado.addAll(listResultSetPage);
			
			if(criteria.get("statusCarregamento").equals("") && criteria.get("rotaCarregamento").equals("") && criteria.get("idMeioTransporte").equals("") && criteria.get("matriculaChefia").equals("")){
				SqlTemplate sql3 = this.getDetachedCriteriaMapa(criteria);
				List rs3 = getAdsmHibernateTemplate().find(sql3.toString(), sql3.getCriteria());
			
				listResultado.addAll(rs3);
			}
			
		}
		
		return buildResultSetFromList(listResultado, findDef);
	}
		
	
	private ResultSetPage buildResultSetFromList(List list, FindDefinition findDef){
		if (findDef.getCurrentPage() == -1){
			int currentPage = list.size() / findDef.getPageSize();
			if (list.size() % findDef.getPageSize() != 0){
				currentPage++;
			}
			findDef = new FindDefinition(currentPage, findDef.getPageSize(), findDef.getOrder());
		}
		
		int fim = findDef.getPageSize().intValue() * findDef.getCurrentPage().intValue() -1;
		int inicio = fim - (findDef.getPageSize().intValue()-1);
		
		int total  = list.size() - 1;			
		List pagina = new ArrayList();
		if(fim > total) {
			fim = total;		
		}
		
		for(int i=inicio; i <= fim ; i++) {
			pagina.add(list.get(i));
		}

		boolean hasNextPage = true;
		if(total == fim) {
			hasNextPage = false;
	}
	
		boolean hasPriorPage = true;
		if(inicio == 0) {
			hasPriorPage = false;
		}
		
		return new ResultSetPage(findDef.getCurrentPage(), hasPriorPage, hasNextPage, pagina, Long.valueOf(list.size()));
	}
	
	@SuppressWarnings("deprecation")
	private SqlTemplate getDetachedCriteria(Map criteria) {
		
		StringBuffer projecao = new StringBuffer();
		projecao.append("new map( ")
		.append("count(vo.codigoVolume) as qtdVolumes, ")
		//.append("ca.totalVolumes as qtdVolumes, ")
		.append("ca.idCarregamento as idCarregamento, ")
		.append("ca.rotaCarregamento as rotaCarregamento, ")
		.append("ca.tipoCarregamento as tipoCarregamento, ")
		.append("cc.mapaCarregamento as nrMapaCarregamento, ")
		.append("cc.mapaCarregamento as mapaCarregamento, ")
		.append("ca.codigoStatus as codigoStatus,")
		.append("ca.matriculaChefia as matriculaChefia,")
		.append("ca.frotaVeiculo as frotaVeiculo,")
		.append("cc.dataCriacao as dataCriacao,")
		.append("cc.dataDisponivel as dataDisponivel, ")
		.append("tc.totalVolume as totalVolume,")
		.append("tc.totalCubagem as totalCubagem,")
		.append("tc.totalPeso as totalPeso,")
		.append("ca.dtInicio as dtInicio,")
		.append("ca.dtFim as dtFim, ")
		.append("u.nrMatricula as nrMatricula, ")
		.append("re.horarioCorte as horarioCorte, ")
		.append("ca.placaVeiculo as placaVeiculo ")
		.append(")");
	
		StringBuffer from = new StringBuffer();
		from.append(CabecalhoCarregamento.class.getName() + " as cc ")
		.append(" left join cc.totalCarregamentos as tc ")
		.append(" left join cc.volumes as vo ")
		.append(" left join vo.carregamento as ca,  ")
		.append(RotaEmbarque.class.getName() + " as re, ")
		.append(Usuario.class.getName() + " as u");
		
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		
		sql.addCustomCriteria("re.siglaRota = ca.rotaCarregamento");
		sql.addCustomCriteria("u.idUsuario = ca.matriculaChefia");
		sql.addCustomCriteria("vo.codigoStatus not in ('4')");
		
		
		if(!"".equals(criteria.get("codigoVolume"))){
			
			SqlTemplate sqlVol = new SqlTemplate();
			sqlVol.addProjection("c.idCarregamento");
			sqlVol.addFrom(Volume.class.getName() + " as v left join v.carregamento as c");
			sqlVol.addCustomCriteria("v.codigoStatus not in ('4')");
			sqlVol.addCustomCriteria("c.codigoStatus not in ('5')");
			sqlVol.addCriteria("v.codigoVolume", "like", criteria.get("codigoVolume"));
			
			Object o = getAdsmHibernateTemplate().findUniqueResult(sqlVol.toString(),sqlVol.getCriteria());
			if (o == null) {
				sql.addCriteria("vo.codigoVolume", "like", criteria.get("codigoVolume"));
			} else {
				sql.addCriteria("ca.idCarregamento", "=", o);
			}
			
		}
		
		if(criteria.get("rotaCarregamento")!= null && !(criteria.get("rotaCarregamento").equals(""))){
			sql.addCriteria("upper(ca.rotaCarregamento)", "like", criteria.get("rotaCarregamento").toString().toUpperCase());
		}
		
		if(criteria.get("idMeioTransporte")!= null && !(criteria.get("idMeioTransporte").equals(""))){
			sql.addCriteria("ca.frotaVeiculo", "=", criteria.get("idMeioTransporte"));
		}
		
		if(criteria.get("mapaCarregamento")!= null && !(criteria.get("mapaCarregamento").equals(""))){
			Long mapaCarregamento = Long.parseLong((String) criteria.get("mapaCarregamento"));
			sql.addCriteria("cc.mapaCarregamento", "=", mapaCarregamento);
		}
		
		
		
		
		if((criteria.get("dataDisponivel")!= null) && (!criteria.get("dataDisponivel").equals(""))){
			YearMonthDay dt = JTDateTimeUtils.convertDataStringToYearMonthDay((String)criteria.get("dataDisponivel"),"yyyy-MM-dd");
			DateTime dtInit = dt.toDateTimeAtMidnight();
			DateTime dtFim = dtInit.plusHours(23).plusMinutes(59);
			sql.addCriteria("cc.dataDisponivel.value",">=",dtInit);
			sql.addCriteria("cc.dataDisponivel.value","<=",dtFim);
		}
		
		if(criteria.get("matriculaChefia")!= null && !(criteria.get("matriculaChefia").equals(""))){
			Long matriculaChefia = Long.parseLong((String) criteria.get("matriculaChefia"));
			sql.addCriteria("ca.matriculaChefia", "=", matriculaChefia);
		}
		
		if(criteria.get("statusCarregamento")!= null && !(criteria.get("statusCarregamento").equals(""))){
			sql.addCriteria("ca.codigoStatus", "=", criteria.get("statusCarregamento"));
		}else if(criteria.get("statusCarregamento").equals("") && 
				criteria.get("mapaCarregamento").equals("") && 
				criteria.get("dataDisponivel").equals("") && 
				criteria.get("rotaCarregamento").equals("") && 
				criteria.get("idMeioTransporte").equals("") && 
				criteria.get("matriculaChefia").equals("")){
					
			sql.addCustomCriteria("ca.codigoStatus in ('1','2')");
		} 
 
		sql.addOrderBy("ca.frotaVeiculo");
		sql.addOrderBy("cc.mapaCarregamento");
		
		sql.addGroupBy("ca.idCarregamento");
		sql.addGroupBy("ca.rotaCarregamento");
		sql.addGroupBy("ca.tipoCarregamento");
		sql.addGroupBy("ca.totalVolumes");
		sql.addGroupBy("cc.mapaCarregamento");
		sql.addGroupBy("ca.codigoStatus");
		sql.addGroupBy("ca.matriculaChefia");
		sql.addGroupBy("ca.frotaVeiculo");
		sql.addGroupBy("cc.dataCriacao");
		sql.addGroupBy("cc.dataDisponivel");
		sql.addGroupBy("tc.totalVolume");
		sql.addGroupBy("tc.totalCubagem");
		sql.addGroupBy("tc.totalPeso");
		sql.addGroupBy("ca.dtInicio");
		sql.addGroupBy("ca.dtFim");
		sql.addGroupBy("u.nrMatricula");
		sql.addGroupBy("re.horarioCorte");
		sql.addGroupBy("ca.placaVeiculo");
		
		return sql;
	}

	private SqlTemplate getDetachedCriteriaAberto(Map criteria) {

		StringBuffer projecao = new StringBuffer();
		projecao.append("new map( ")
		.append("ca.idCarregamento as idCarregamento, ")
		.append("ca.rotaCarregamento as rotaCarregamento, ")
		.append("ca.tipoCarregamento as tipoCarregamento, ")
		.append("ca.codigoStatus as codigoStatus, ")
		.append("ca.matriculaChefia as matriculaChefia, ")
		.append("ca.frotaVeiculo as frotaVeiculo, ")
		.append("ca.dtInicio as dtInicio, ")
		.append("ca.dtFim as dtFim, ")
		.append("u.nrMatricula as nrMatricula, ")
		.append("re.horarioCorte as horarioCorte ")
		.append(")");

		StringBuffer from = new StringBuffer();
		from.append(Carregamento.class.getName() + " as ca, ")
		.append(RotaEmbarque.class.getName() + " as re, ")
		.append(Usuario.class.getName() + " as u");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		sql.addCustomCriteria("u.idUsuario = ca.matriculaChefia");
		sql.addCustomCriteria("re.siglaRota = ca.rotaCarregamento");

		if(criteria.get("statusCarregamento").toString().equals("")){
			sql.addCriteria("ca.codigoStatus", "=","1");
		}else{
			sql.addCriteria("ca.codigoStatus", "=", criteria.get("statusCarregamento").toString());
		}
		
		if(criteria.get("rotaCarregamento")!= null && !(criteria.get("rotaCarregamento").equals(""))){
			sql.addCriteria("upper(ca.rotaCarregamento)", "like", criteria.get("rotaCarregamento").toString().toUpperCase());
	}
	
		if(criteria.get("idMeioTransporte")!= null && !(criteria.get("idMeioTransporte").equals(""))){
			sql.addCriteria("ca.frotaVeiculo", "=", criteria.get("idMeioTransporte"));
		}
		
		if(criteria.get("matriculaChefia")!= null && !(criteria.get("matriculaChefia").equals(""))){
			Long matriculaChefia = Long.parseLong((String) criteria.get("matriculaChefia"));
			sql.addCriteria("ca.matriculaChefia", "=", matriculaChefia);
		}

		sql.addOrderBy("ca.frotaVeiculo");
		
		return sql;
	}
	
	private SqlTemplate getDetachedCriteriaMapa(Map criteria) {
		
		StringBuffer projecao = new StringBuffer();
		projecao.append("new map( ")
		.append("cc.mapaCarregamento as nrMapaCarregamento, ")
		.append("cc.mapaCarregamento as mapaCarregamento, ")
		.append("cc.dataCriacao as dataCriacao, ")
		.append("cc.dataDisponivel as dataDisponivel, ")
		.append("tc.totalVolume as totalVolume, ")
		.append("tc.totalCubagem as totalCubagem, ")
		.append("tc.totalPeso as totalPeso ")
		.append(")");

		StringBuffer from = new StringBuffer();
		from.append(CabecalhoCarregamento.class.getName() + " as cc ")
		.append(" left join cc.totalCarregamentos as tc ")
		.append(" left join cc.volumes as vo ")
		.append(" left join vo.carregamento as ca ");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		
		if(!"".equals(criteria.get("codigoVolume"))){
			
			
			SqlTemplate sqlVol = new SqlTemplate();
			sqlVol.addProjection("c.idCarregamento");
			sqlVol.addFrom(Volume.class.getName() + " as v left join v.carregamento as c");
			sqlVol.addCustomCriteria("v.codigoStatus not in ('4')");
			sqlVol.addCustomCriteria("c.codigoStatus not in ('5')");
			sqlVol.addCriteria("v.codigoVolume", "like", criteria.get("codigoVolume"));
			
			Object o = getAdsmHibernateTemplate().findUniqueResult(sqlVol.toString(),sqlVol.getCriteria());
			if (o == null) {
				sql.addCriteria("vo.codigoVolume", "like", criteria.get("codigoVolume"));
			} else {
				sql.addCriteria("ca.idCarregamento", "=", o);
			}
			
			
			
		}
		
		if(criteria.get("mapaCarregamento")!= null && !(criteria.get("mapaCarregamento").equals(""))){
			Long mapaCarregamento = Long.parseLong((String) criteria.get("mapaCarregamento"));
			sql.addCriteria("cc.mapaCarregamento", "=", mapaCarregamento);
		}else{
			sql.addCustomCriteria("(vo.idVolume is null or vo.codigoStatus = 4 or ca.codigoStatus = 5)");
			
		}
		sql.addCustomCriteria("not exists (select vc.idVolume from " + Volume.class.getName() + " as vc " +
				  " where vc.codigoStatus != 4 " +
				  " and vc.cabecalhoCarregamento.idCabecalhoCarregamento = cc.idCabecalhoCarregamento)");

		if(criteria.get("dataDisponivel")!= null && !(criteria.get("dataDisponivel").equals(""))){
			YearMonthDay dt = JTDateTimeUtils.convertDataStringToYearMonthDay((String)criteria.get("dataDisponivel"),"yyyy-MM-dd");
			DateTime dtInit = dt.toDateTimeAtMidnight();
			DateTime dtFim = dtInit.plusHours(23).plusMinutes(59);
			sql.addCriteria("cc.dataDisponivel.value",">=",dtInit);
			sql.addCriteria("cc.dataDisponivel.value","<=",dtFim);
		}

		sql.addOrderBy("cc.mapaCarregamento");
		
		return sql;
	}
	
	@SuppressWarnings("deprecation")
	private SqlTemplate getDetachedCriteriaDetalhe(Map criteria) {
		
		StringBuffer projecao = new StringBuffer();
		projecao.append("new map( ")
		.append("count(det.codigoVolume) as qtdVolume, ")
		.append("det.rotaDestino as rotaCarregamento, ")
		.append("cc.mapaCarregamento as nrMapaCarregamento, ")
		.append("cc.mapaCarregamento as mapaCarregamento, ")
		.append("cc.dataCriacao as dataCriacao,")
		.append("cc.dataDisponivel as dataDisponivel, ")
		.append("tc.totalVolume as totalVolume,")
		.append("tc.totalCubagem as totalCubagem,")
		.append("tc.totalPeso as totalPeso")
		.append(")");
		
	
		StringBuffer from = new StringBuffer();
		from.append(CabecalhoCarregamento.class.getName() + " as cc ")
		.append(" left join cc.totalCarregamentos as tc ")
		.append(" left join cc.detalhesCarregamentos as det ")
		.append(" left join cc.detalhesCarregamentos as detal ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		
		if(criteria.get("mapaCarregamento")!= null && !(criteria.get("mapaCarregamento").equals(""))){
			Long mapaCarregamento = Long.parseLong((String) criteria.get("mapaCarregamento"));
			sql.addCriteria("cc.mapaCarregamento", "=", mapaCarregamento);
		}
		
		if((criteria.get("dataDisponivel")!= null) && (!criteria.get("dataDisponivel").equals(""))){
			YearMonthDay dt = JTDateTimeUtils.convertDataStringToYearMonthDay((String)criteria.get("dataDisponivel"),"yyyy-MM-dd");
			DateTime dtInit = dt.toDateTimeAtMidnight();
			DateTime dtFim = dtInit.plusHours(23).plusMinutes(59);
			sql.addCriteria("cc.dataDisponivel.value",">=",dtInit);
			sql.addCriteria("cc.dataDisponivel.value","<=",dtFim);
		}
		
		if(!"".equals(criteria.get("codigoVolume"))){
			   
			SqlTemplate sqlVol = new SqlTemplate();
			sqlVol.addProjection("cc.idCabecalhoCarregamento");
			sqlVol.addFrom(DetalheCarregamento.class.getName() + " as det");
			sqlVol.addFrom(CabecalhoCarregamento.class.getName() + " as cc");
			sqlVol.addJoin("det.idCabecalhoCarregamento", "cc.idCabecalhoCarregamento");
			sqlVol.addCriteria("det.codigoVolume", "like", criteria.get("codigoVolume"));
			
			Object o = getAdsmHibernateTemplate().findUniqueResult(sqlVol.toString(),sqlVol.getCriteria());
			if (o == null) {
				sql.addCriteria("upper(det.codigoVolume)", "like", criteria.get("codigoVolume").toString().toUpperCase());	
			} else {
				sql.addCriteria("cc.idCabecalhoCarregamento", "=", o);
			}
			
			 
			
		}
		
		sql.addOrderBy("cc.mapaCarregamento");
		
		sql.addGroupBy("cc.mapaCarregamento");
		sql.addGroupBy("cc.dataCriacao");
		sql.addGroupBy("cc.dataDisponivel");
		sql.addGroupBy("tc.totalVolume");
		sql.addGroupBy("tc.totalCubagem");
		sql.addGroupBy("tc.totalPeso");
		sql.addGroupBy("det.rotaDestino");
			
		
		
		return sql;
	}
	
  private SqlTemplate getDetachedCriteriaMapaDetalhe(Map criteria) {
		
		StringBuffer projecao = new StringBuffer();
		projecao.append("new map( ")
		.append("count(det.codigoVolume) as qtdVolume, ")
		.append("cc.mapaCarregamento as nrMapaCarregamento, ")
		.append("cc.mapaCarregamento as mapaCarregamento, ")
		.append("cc.dataCriacao as dataCriacao, ")
		.append("cc.dataDisponivel as dataDisponivel, ")
		.append("tc.totalVolume as totalVolume, ")
		.append("tc.totalCubagem as totalCubagem, ")
		.append("tc.totalPeso as totalPeso ")
		.append(")");

		StringBuffer from = new StringBuffer();
		from.append(CabecalhoCarregamento.class.getName() + " as cc ")
		.append(" left join cc.totalCarregamentos as tc ")
		.append(" left join cc.detalhesCarregamentos as det ");
	
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projecao.toString());
		sql.addFrom(from.toString());
		
		if(criteria.get("mapaCarregamento")!= null && !(criteria.get("mapaCarregamento").equals(""))){
			Long mapaCarregamento = Long.parseLong((String) criteria.get("mapaCarregamento"));
			sql.addCriteria("cc.mapaCarregamento", "=", mapaCarregamento);
		}
		if(!"".equals(criteria.get("codigoVolume"))){
			SqlTemplate sqlVol = new SqlTemplate();
			sqlVol.addProjection("cc.idCabecalhoCarregamento");
			sqlVol.addFrom(DetalheCarregamento.class.getName() + " as det");
			sqlVol.addFrom(CabecalhoCarregamento.class.getName() + " as cc");			
			sqlVol.addJoin("det.idCabecalhoCarregamento", "cc.idCabecalhoCarregamento");
			sqlVol.addCriteria("det.codigoVolume", "like", criteria.get("codigoVolume"));
			
			Object o = getAdsmHibernateTemplate().findUniqueResult(sqlVol.toString(),sqlVol.getCriteria());
			if (o == null) {
				sql.addCriteria("upper(det.codigoVolume)", "like", criteria.get("codigoVolume").toString().toUpperCase());	
			} else {
				sql.addCriteria("cc.idCabecalhoCarregamento", "=", o);
			}
			
		}
	
		if(criteria.get("dataDisponivel")!= null && !(criteria.get("dataDisponivel").equals(""))){
			YearMonthDay dt = JTDateTimeUtils.convertDataStringToYearMonthDay((String)criteria.get("dataDisponivel"),"yyyy-MM-dd");
			DateTime dtInit = dt.toDateTimeAtMidnight();
			DateTime dtFim = dtInit.plusHours(23).plusMinutes(59);
			sql.addCriteria("cc.dataDisponivel.value",">=",dtInit);
			sql.addCriteria("cc.dataDisponivel.value","<=",dtFim);
		}

		sql.addOrderBy("cc.mapaCarregamento");
		
		sql.addGroupBy("cc.mapaCarregamento");
		sql.addGroupBy("cc.dataCriacao");
		sql.addGroupBy("cc.dataDisponivel");
		sql.addGroupBy("tc.totalVolume");
		sql.addGroupBy("tc.totalCubagem");
		sql.addGroupBy("tc.totalPeso");
		sql.addGroupBy("det.rotaDestino");
		return sql;
	}

}