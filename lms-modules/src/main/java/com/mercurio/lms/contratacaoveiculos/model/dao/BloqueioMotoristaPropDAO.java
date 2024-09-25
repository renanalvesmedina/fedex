package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class BloqueioMotoristaPropDAO extends BaseCrudDao<BloqueioMotoristaProp, Long>
{
	private Logger log = LogManager.getLogger(this.getClass());
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return BloqueioMotoristaProp.class;
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("usuarioByIdFuncionarioBloqueio", FetchMode.JOIN);
		lazyFindPaginated.put("usuarioByIdFuncionarioDesbloqueio", FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialBloqueio", FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialBloqueio.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialDesbloqueado", FetchMode.JOIN);
		lazyFindPaginated.put("filialByIdFilialDesbloqueado.pessoa", FetchMode.JOIN);
		lazyFindPaginated.put("controleCarga", FetchMode.JOIN);
    }
    
    public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
    	Map parameters = new HashMap();
		
		StringBuffer sql = createSqlPaginated(criteria);
		
		sql.insert(0,"SELECT * FROM (").append(") ");
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idBloqueioMotoristaProp",Hibernate.LONG);
				sqlQuery.addScalar("obBloqueioMotoristaProp",Hibernate.STRING);				
				sqlQuery.addScalar("nmUsuarioBloqueio",Hibernate.STRING);
		    	sqlQuery.addScalar("nmUsuarioDesbloqueio",Hibernate.STRING);
		    	sqlQuery.addScalar("sgFilialBloqueio",Hibernate.STRING);
		    	sqlQuery.addScalar("nmFilialBloqueio",Hibernate.STRING);
		    	sqlQuery.addScalar("dtRegistroBloqueio", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
		    	sqlQuery.addScalar("sgFilialDesbloqueio",Hibernate.STRING);
		    	sqlQuery.addScalar("nmFilialDesbloqueio",Hibernate.STRING);
		    	sqlQuery.addScalar("dtRegistroDesbloqueio",Hibernate.custom(JodaTimeYearMonthDayUserType.class));
		    	sqlQuery.addScalar("dhVigenciaInicial",Hibernate.custom(JodaTimeDateTimeUserType.class));
		    	sqlQuery.addScalar("dhVigenciaFinal",Hibernate.custom(JodaTimeDateTimeUserType.class));
			}
			
		};
		if (! (criteria.getYearMonthDay("dtVigenciaInicial") == null)) {
			parameters.put("dtVigenciaInicial", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dtVigenciaInicial")));
		}
		if (! (criteria.getYearMonthDay("dtVigenciaFinal") == null)) {
			parameters.put("dtVigenciaFinal", JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("dtVigenciaFinal")));
		}
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), findDef.getCurrentPage(), findDef.getPageSize(), parameters, confSql);
		return rsp;
    }
    
    private StringBuffer createSqlPaginated(TypedFlatMap criteria) {
    	StringBuffer sql = new StringBuffer()
    	.append(" select BLOQ.id_Bloqueio_Motorista_Prop as idBloqueioMotoristaProp ")
    	.append("       ,BLOQ.OB_BLOQUEIO_MOTORISTA_PROP as obBloqueioMotoristaProp")
    	.append("       ,UB.nm_usuario as nmUsuarioBloqueio ")
    	.append("       ,UD.nm_usuario as nmUsuarioDesbloqueio ")
    	.append("       ,FB.sg_filial as sgFilialBloqueio")
    	.append("       ,FBP.nm_Fantasia as nmFilialBloqueio")
    	.append("       ,BLOQ.DT_REGISTRO_BLOQUEIO as dtRegistroBloqueio")
    	.append("       ,FD.sg_filial as sgFilialDesbloqueio")
    	.append("       ,FDP.nm_Fantasia as nmFilialDesbloqueio")
    	.append("       ,BLOQ.DT_REGISTRO_DESBLOQUEIO as dtRegistroDesbloqueio")
    	.append("       ,BLOQ.DH_VIGENCIA_INICIAL as dhVigenciaInicial")
    	.append("       ,BLOQ.DH_VIGENCIA_FINAL as dhVigenciaFinal")
    	.append(" from bloqueio_motorista_prop BLOQ left join ")
    	.append("      usuario UB on UB.ID_USUARIO = BLOQ.ID_USUARIO_BLOQUEIO left join ")
    	.append("      usuario UD on UD.ID_USUARIO = BLOQ.ID_USUARIO_DESBLOQUEIO left join ")
    	.append("      filial FB on FB.ID_FILIAL = BLOQ.ID_FILIAL_BLOQUEIO left join ")
    	.append("      pessoa FBP on FBP.ID_PESSOA = FB.ID_FILIAL left join ")
    	.append("      filial FD on FD.ID_FILIAL = BLOQ.ID_FILIAL_DESBLOQUEADO left join ")
    	.append("      pessoa FDP on FDP.ID_PESSOA = FD.ID_FILIAL ")
    	.append(" where 1 = 1");
		
    	try {
    		if (! (criteria.getLong("meioTransporte.idMeioTransporte") == null)) {
    			sql.append(" and BLOQ.id_Meio_Transporte=" + criteria.getLong("meioTransporte.idMeioTransporte"));
    		}
    		if (! (criteria.getLong("proprietario.idProprietario") == null)) {
    			sql.append(" and BLOQ.id_Proprietario=" + criteria.getLong("proprietario.idProprietario"));
    		}
    		if (! (criteria.getLong("motorista.idMotorista") == null)) {
    			sql.append(" and BLOQ.id_Motorista=" + criteria.getLong("motorista.idMotorista"));
    		}
    		if (! StringUtils.isEmpty(criteria.getString("obBloqueioMotoristaProp"))) {
    			sql.append(" and BLOQ.ob_Bloqueio_Motorista_Prop='" + criteria.getString("obBloqueioMotoristaProp")+"'");
    		}
    		if (! (criteria.getYearMonthDay("dtVigenciaInicial") == null)) {
    			sql.append(" and trunc(cast(BLOQ.dh_Vigencia_Inicial as date))>=trunc(:dtVigenciaInicial) ");
    	    			
    		}
    		if (! (criteria.getYearMonthDay("dtVigenciaFinal") == null)) {
    			sql.append(" and trunc(cast(BLOQ.dh_Vigencia_Final as date))<=trunc(:dtVigenciaFinal)");
    		}
    		
    	}catch (Exception e) {
			log.error(e);
		}
		return sql;
	}

	/**
     * Executa a consulta.
     * Verifica a existencia de bloqueios vigentes para o proprietario 
     * @param id Valor do id da entidade a verificar
     * @param context contexto do qual o idProperty faz parte 
     * @param idProperty String com o nome da propriedade de id do pojo 
     * @return
     */
    private boolean validateBloqueiosVigentes(Long id, String context, String idProperty) {
  	   	return (findBloqueiosVigentes(id, context, idProperty).size() > 0 );
     }

    private List findBloqueiosVigentes(Long id, String context, String idProperty){
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
        
    	if (! "".equals(context) ) {
    		
	    	dc.setFetchMode(context, FetchMode.JOIN);
	    	
	    	idProperty = context + "." + idProperty;
    	}
    	

    	dc.add(Restrictions.eq(idProperty, id));
    	dc.add(Restrictions.le("dhVigenciaInicial",JTDateTimeUtils.getDataHoraAtualPadrao()));
		dc.add( Restrictions.ge("dhVigenciaFinal", JTDateTimeUtils.getDataHoraAtualPadrao()));
		
		
		
    	return findByDetachedCriteria(dc);
    }
    
    private List findBloqueiosFuturos(Long id, String context, String idProperty) {
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass());
    
    	if (! "".equals(context) ) {
    		
	    	dc.setFetchMode(context, FetchMode.JOIN);
	    	
	    	idProperty = context + "." + idProperty;
    	}
    	
    	dc.add(Restrictions.eq(idProperty, id));
    	dc.add(Restrictions.gt("dhVigenciaInicial", JTDateTimeUtils.getDataHoraAtual()));    		
    	
    	return findByDetachedCriteria(dc);
    	
    }
    
    /**
     * Verifica a existencia de bloqueios vigentes para o proprietario
     * @param pro Entidade
     * @return True, se há bloqueios vigentes.
     */
    public boolean validateBloqueiosVigentes(Proprietario pro) {
    	return validateBloqueiosVigentes(pro.getIdProprietario(), "proprietario", "idProprietario");
    }
    
    /**
     * Verifica a existencia de bloqueios vigentes para o motorista
     * @param mot Entidade
     * @return True, se há bloqueios vigentes.
     */
    public boolean verificaBloqueiosVigentes(Motorista mot) {
    	return validateBloqueiosVigentes(mot.getIdMotorista(), "motorista", "idMotorista");
    }

    /**
     * Verifica a existencia de bloqueios vigentes para o meio de transporte
     * @param mt Entidade
     * @return True, se há bloqueios vigentes.
     */
    public boolean validateBloqueiosVigentes(MeioTransporte mt) {
    	return validateBloqueiosVigentes(mt.getIdMeioTransporte(), "meioTransporte", "idMeioTransporte");
    }
    
    public Long getRowCountQuantidadeViagens (ControleCarga controleCarga, MeioTransporte mt, final Integer nrDiasPeriodoBloqEventual) {

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("tpStatusControleCargaCancelado", "CA");
    	params.put("tpControleCargaViagem", "V");
    	if (controleCarga != null && controleCarga.getIdControleCarga() != null) {
    		params.put("idControleCarga", controleCarga.getIdControleCarga());
    	}
    	params.put("idMeioTransporte", mt.getIdMeioTransporte());
    	params.put("dhRestanteBloqEventual", JTDateTimeUtils.getDataHoraAtual().minusDays(nrDiasPeriodoBloqEventual));
		
    	final StringBuilder hql = new StringBuilder()
		.append("select count(*) ")
		.append(" from ").append(ControleCarga.class.getName())
		.append(" where tpStatusControleCarga <> :tpStatusControleCargaCancelado")
		.append(" and tpControleCarga = :tpControleCargaViagem")
		.append(" and (meioTransporteByIdTransportado.idMeioTransporte = :idMeioTransporte")
		.append("     or meioTransporteByIdSemiRebocado.idMeioTransporte = :idMeioTransporte)")
		.append(" and trunc(dhGeracao.value) >= trunc(:dhRestanteBloqEventual)");
    	
    	if (controleCarga != null && controleCarga.getIdControleCarga() != null) {
			hql.append(" and idControleCarga <> :idControleCarga");
		}
		
		
		return (Long)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params).get(0);
    } 

    public List findBloqueiosVigentesMeioTransporte(Long idMeioTransporte) {
    	return findBloqueiosVigentes(idMeioTransporte, "meioTransporte", "idMeioTransporte");
    }
    
    public List findBloqueiosVigentesMotorista(Long idMmotorista) {
    	return findBloqueiosVigentes(idMmotorista, "motorista", "idMotorista");
    }
    
    public List findBloqueiosVigentesProprietario(Long idProprietario) {
    	return findBloqueiosVigentes(idProprietario, "proprietario", "idProprietario");
    } 
    
    public List findBloqueiosFuturosMeioTransporte(Long idMeioTransporte ) {
    	return findBloqueiosFuturos(idMeioTransporte, "meioTransporte", "idMeioTransporte");
    }
    
    public List findBloqueiosFuturosMotorista(Long idMotorista) {
    	return findBloqueiosFuturos(idMotorista, "motorista", "idMotorista");
    }
    
    public List findBloqueiosFuturosProprietario(Long idProprietario) {
    	return findBloqueiosFuturos(idProprietario, "proprietario", "idProprietario");
    }
    
    /** valida se uma entidade pode ser salva no banco com a vigência setada.
     * @param bean
     */
    public void validaVigenciaBeforeStore(BloqueioMotoristaProp bean) {
    	DateTime dhVigenciaInicial = bean.getDhVigenciaInicial();
    	
    	if (bean.getIdBloqueioMotoristaProp() == null) {
    		validaVigenciaInsercao(dhVigenciaInicial);
    	} else {
    		BloqueioMotoristaProp beanOld = (BloqueioMotoristaProp)findById(bean.getIdBloqueioMotoristaProp());
    		
			DateTime dhVigenciaFinal = bean.getDhVigenciaFinal();
			DateTime dhVigenciaInicialOld = beanOld.getDhVigenciaInicial();
			
			getHibernateTemplate().evict(beanOld);
	    	
	    	validaVigenciaBeforeStore(dhVigenciaInicial,dhVigenciaInicialOld,dhVigenciaFinal);
    	}
    }

    /**
	 * Valida data de vigência inicial para inserir uma entidade.
	 * 
	 * @param DateTime dhVigenciaInicial.
	 * @return  
	 */
	public static void validaVigenciaInsercao(DateTime dhVigenciaInicial) {
		if (dhVigenciaInicial.withSecondOfMinute(0).withMillisOfSecond(0).compareTo(JTDateTimeUtils.getDataHoraAtual().withSecondOfMinute(0).withMillisOfSecond(0)) < 0)
			throw new BusinessException("LMS-00006");
	}

	/**
	 * Recebe data de vigência inicial e compara com data de vigencia inicial antes de ser alterado na tela.
	 * Após, verifica se as datas sao menores que a data do sistema.
	 * Caso a regra seja desrespeitada, gera exceção
	 * 
	 * @param dhInicial Data de Vigência incial a ser validada.
	 * @param dhInicialOld Data de vigência antes de ser alterada na tela.
	 * @param dhFinal Data de Vigência final a ser validada.  
	 */
	private void validaVigenciaBeforeStore(DateTime dhInicial, DateTime dhInicialOld, DateTime dhFinal) {
		if (dhInicialOld != null) {
			DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm");
			if(dhInicialOld.toString(format).compareTo(dhInicial.toString(format)) != 0){
				validaVigenciaInsercao(dhInicial);
			}
		}
		
		if(dhFinal != null){
			DateTime dhFimBase = JTDateTimeUtils.getDataHoraAtual();

			if (dhFinal.withSecondOfMinute(0).withMillisOfSecond(0).compareTo(dhFimBase.withSecondOfMinute(0).withMillisOfSecond(0)) < 0) {
				throw new BusinessException("LMS-00007");
			}
		}
	}
	
	public boolean verificaBloqueioViagemEventual(ControleCarga controleCarga, MeioTransporte mt, boolean isNewControleCarga) {
		
		Map<String, Object> params = new HashMap<String, Object>();
    	params.put("idMeioTransporte", mt.getIdMeioTransporte());
    	params.put("blBloqueiaViagemSim", true);
    	params.put("controleCargaNovo", isNewControleCarga);
    	params.put("idControleCarga", controleCarga.getIdControleCarga());
    	
    	final StringBuilder hql = new StringBuilder()
		.append("select count(*)")
		.append(" from ").append(getPersistentClass().getName())
		.append(" where meioTransporte.idMeioTransporte = :idMeioTransporte")
		.append(" and blBloqueiaViagem = :blBloqueiaViagemSim")
		.append(" and blControleCargaNovo = :controleCargaNovo")
		.append(" and controleCarga.idControleCarga = :idControleCarga");
		
		return (Long)getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params).get(0) == 0;
	}
}
