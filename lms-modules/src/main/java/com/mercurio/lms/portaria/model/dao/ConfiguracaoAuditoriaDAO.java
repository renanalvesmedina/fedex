package com.mercurio.lms.portaria.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.model.hibernate.JodaTimeTimeOfDayUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.contratacaoveiculos.model.EventoMeioTransporte;
import com.mercurio.lms.portaria.model.ConfiguracaoAuditoria;
import com.mercurio.lms.portaria.model.ConfiguracaoAuditoriaFil;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ConfiguracaoAuditoriaDAO extends BaseCrudDao<ConfiguracaoAuditoria, Long>
{

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}

	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
	}
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ConfiguracaoAuditoria.class;
    }
    
	public boolean isThereConfiguracaoVigente(Long idConfiguracaoAuditoria, Long idFilial, DomainValue tpOperacao, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, TimeOfDay hrConfiguracaoInicial, TimeOfDay hrConfiguracaoFinal) {
		StringBuffer s = new StringBuffer()
			.append(" from " + ConfiguracaoAuditoria.class.getName() + " ca \n")
			.append(" join ca.filial fi \n")
			.append(" where fi.id = :idFilial \n")
			.append(idConfiguracaoAuditoria != null ? 
					"   and ca.id <> :idConfiguracaoAuditoria \n" : "")
			.append("   and ca.dtVigenciaInicial <= :vigenciaFinal \n")
			.append("   and ca.dtVigenciaFinal  >= :vigenciaInicial  \n")
			.append(!"A" .equals(tpOperacao.getValue()) ? 
					"   and (ca.tpOperacao = :tpOperacao or ca.tpOperacao = :tpAmbos ) \n" : " ")
			.append("   and ca.hrConfiguracaoInicial <= :hrFinal \n")
			.append("   and ca.hrConfiguracaoFinal   >= :hrInicial \n");
			
		
		Map map = new HashMap(); 
		map.put("idFilial", idFilial);
		if (idConfiguracaoAuditoria != null)
			map.put("idConfiguracaoAuditoria", idConfiguracaoAuditoria);
		if (!"A" .equals(tpOperacao.getValue())) {		
 			map.put("tpOperacao", tpOperacao.getValue());
 			map.put("tpAmbos", "A");
 		}
		map.put("vigenciaInicial", dtVigenciaInicial);
		map.put("vigenciaFinal", JTDateTimeUtils.maxYmd(dtVigenciaFinal));
		map.put("hrInicial", hrConfiguracaoInicial);
		map.put("hrFinal", hrConfiguracaoFinal);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(s.toString(), map).intValue() > 0;
	}
	
	/**
	 * Método que retorna o count(*) de todas as auditorias já realizadas, de acordo com os parâmetros.
	 * @param idFilial Filial de onde a consulta está sendo realizada.
	 * @param dtConsulta Data em que a contagem de auditorias deve ser realizada.
	 * @param hrInicioAuditoria Horário em que a auditoria foi iniciada.
	 * @param hrFinalAuditoria Horário em que a auditoria será finalizada.
	 * @param somenteProprios Se true, somente serão levados em consideracao, veiculos próprios. 
	 * @param somenteTerceiros Se true, somente serão levados em consideracao, veiculos de terceiros.
	 * @param tpOperacao Tipo de operacao que o meio de transporte efetuava no momento da auditoria.
	 * @return Contagem de auditorias realizadas.
	 * @author luisfco
	 */
	public Integer contarAuditorias(Long idFilial, DateTime dtConsulta, TimeOfDay hrInicioAuditoria, TimeOfDay hrFinalAuditoria, 
									boolean somenteProprios, boolean somenteTerceiros, String tpOperacao) {
		
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("idFilial", idFilial);
		parametros.put("dtConsultaInicial",  JTDateTimeUtils.formatDateTimeToString(JTDateTimeUtils.getFirstHourOfDay(dtConsulta), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN));	
		parametros.put("dtConsultaFinal", JTDateTimeUtils.formatDateTimeToString(JTDateTimeUtils.getLastHourOfDay(dtConsulta), JTDateTimeUtils.DATETIME_WITH_SECONDS_PATTERN));	
		parametros.put("hrInicioAuditoria", hrInicioAuditoria);
		parametros.put("hrFinalAuditoria", hrFinalAuditoria);
		parametros.put("tpSituacaoMeioTransporte", "EADT");
		parametros.put("dtAtual", JTDateTimeUtils.getDataAtual());
			
		String sql = montaSqlRegistroAuditoria(somenteProprios, somenteTerceiros, tpOperacao, false);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(sql, parametros);
	}

	
	public DateTime getDataUltimaAuditoriaRealizada(Long idFilial, DateTime dtConsulta, TimeOfDay hrInicioAuditoria, TimeOfDay hrFinalAuditoria, 
												    boolean somenteProprios, boolean somenteTerceiros, String tpOperacao) {
		 
		String sql = montaSqlRegistroAuditoria(somenteProprios, somenteTerceiros, tpOperacao, true);
		 
		List result = getAdsmHibernateTemplate().findByNamedParam(sql,
																  new String[]{"tpSituacaoMeioTransporte","idFilial", "hrInicioAuditoria","hrFinalAuditoria","dtAtual"},
																  new Object[]{"EADT",idFilial, hrInicioAuditoria, hrFinalAuditoria, JTDateTimeUtils.getDataAtual()});
		
		return (result.isEmpty() ? null :  JodaTimeUtils.toDateTime(getAdsmHibernateTemplate(), result.get(0)));
	}
	 

	private String montaSqlRegistroAuditoria(boolean somenteProprios, boolean somenteTerceiros, String tpOperacao, boolean isMaxData){
		
		StringBuffer sb = new StringBuffer();
		if (isMaxData) {
			sb.append(" Select max(emt.dhInicioEvento.value) from " + EventoMeioTransporte.class.getName() + " emt \n");
		} else {
			sb.append(" from " + EventoMeioTransporte.class.getName() + " emt \n");
		}
		
		  sb.append(" join emt.controleCarga cc \n")
			.append(" join emt.meioTransporte mt \n")			
			.append(" left join cc.rotaIdaVolta ridCC")
			.append(" left join ridCC.rota rotaCC")
			.append(" left join rotaCC.filialRotas filRotaCC")
			.append(" left join filRotaCC.filial filialCC")
			.append(" where emt.tpSituacaoMeioTransporte = :tpSituacaoMeioTransporte \n")
			.append("   and (filRotaCC.id is null or filRotaCC.blOrigemRota = 'S')  \n")
			.append("   and emt.filial.id = :idFilial \n");
			
		  if (!isMaxData) {
			sb.append("   and SYS_EXTRACT_UTC(emt.dhInicioEvento.value) >= :dtConsultaInicial \n");
			sb.append("   and SYS_EXTRACT_UTC(emt.dhInicioEvento.value) <= :dtConsultaFinal \n");
		  }
		
		  	sb.append("   and to_char(emt.dhInicioEvento.value, 'HH24MISS') BETWEEN to_char(:hrInicioAuditoria, 'HH24MISS') AND to_char(:hrFinalAuditoria, 'HH24MISS') \n")
		  		.append("   and not exists ( \n")
				.append(" from " + ConfiguracaoAuditoriaFil.class.getName() + " caf \n")
				.append(" 		left join caf.rotaIdaVolta ridCaf")
				.append(" 		left join ridCaf.rota rotaCaf")
				.append(" 		left join rotaCaf.filialRotas filRotaCaf")
				.append(" 		left join filRotaCaf.filial filialCaf")
				.append(" where (filRotaCaf.id is null or filRotaCaf.blOrigemRota = 'S')  \n")
				.append("    and (to_char(emt.dhInicioEvento.value, 'HH24MISS')  between to_char(caf.hrAuditoriaInicial,'HH24MISS') and TO_CHAR(caf.hrAuditoriaFinal,'HH24MISS')) \n")
				.append("    and (caf.meioTransporteRodoviario.id is null or caf.meioTransporteRodoviario.id = cc.meioTransporteByIdTransportado.id) \n")
				.append("    and (caf.rotaColetaEntrega.id  is null or       caf.rotaColetaEntrega.id = cc.rotaColetaEntrega.id)  \n")
				.append("    and (caf.rotaIdaVolta.id is null or (ridCaf.nrRota = ridCC.nrRota and filialCaf.id = filialCC.id))  \n")
		  		.append("    and caf.dtVigenciaFinal >= :dtAtual  ) \n");		
						
			if ("E" .equals(tpOperacao)) {
				sb.append("   and cc.tpControleCarga = 'C' \n");
				
			} else if ("V" .equals(tpOperacao)) {
				sb.append("   and cc.tpControleCarga = 'V' \n"); 
			} 
				
			if (somenteProprios || somenteTerceiros) {
				if (somenteProprios)
					sb.append("   and mt.tpVinculo = 'P'  \n");
				else if (somenteTerceiros)
					sb.append("   and mt.tpVinculo <> 'P'  \n");
			}

		return sb.toString();
		
	}
	
	public List findEventosMeioTransporteByIdMeioTransporte(Long idFilial, Long idMeioTransporte) {
		StringBuffer sb = new StringBuffer()
			.append(" select emt ")
			.append(" from " + EventoMeioTransporte.class.getName() + " emt \n")
			.append(" where emt.filial.id = ? \n")			
			.append("   and emt.meioTransporte.id = ? \n")
			.append(" order by emt.dhGeracao.value ");
		
		Object [] parametros = new Object[]{idFilial, idMeioTransporte};
		return getAdsmHibernateTemplate().find(sb.toString(), parametros);
	}
	
	public boolean isThereConfiguracaoAuditoriaFilVigenteParaMeioTransporte(Long idMeioTransporte) {
		Map parametros = new HashMap();
		parametros.put("hoje", JTDateTimeUtils.getDataAtual());
		parametros.put("hora", JTDateTimeUtils.getHorarioAtual());
		parametros.put("idMeioTransporte", idMeioTransporte);
				
		StringBuffer sb = new StringBuffer()
			.append(" from " + ConfiguracaoAuditoriaFil.class.getName() + " caf \n")
			.append(" 		left join caf.rotaIdaVolta ridCaf")
			.append(" 		left join ridCaf.rota rotaCaf")
			.append(" 		left join rotaCaf.filialRotas filRotaCaf")
			.append(" 		left join filRotaCaf.filial filialCaf,")
			.append(ControleCarga.class.getName() + " cc ")
			.append("  		left join cc.rotaIdaVolta ridCC")
			.append(" 		left join ridCC.rota rotaCC")
			.append(" 		left join rotaCC.filialRotas filRotaCC")
			.append(" 		left join filRotaCC.filial filialCC")
			.append(" where caf.dtVigenciaInicial <= :hoje \n")
			.append("   and (filRotaCaf.id is null or filRotaCaf.blOrigemRota = 'S')  \n")
			.append("   and (filRotaCC.id is null or filRotaCC.blOrigemRota = 'S')  \n")
			.append("   and caf.dtVigenciaFinal  >= :hoje  \n")
			.append("   and caf.hrAuditoriaInicial <= :hora  \n")
			.append("   and caf.hrAuditoriaFinal  >= :hora  \n")
			.append("	and caf.filial.id = cc.filialByIdFilialOrigem.id")	
			.append("	and cc.meioTransporteByIdTransportado.id = :idMeioTransporte")
			.append("	and cc.tpStatusControleCarga in ('AE', 'AV') ")
			.append("	and (caf.tpOperacao = 'A'")       
            .append(" 		 or (caf.tpOperacao = 'E' and cc.tpControleCarga = 'C')")
            .append("        or (caf.tpOperacao = cc.tpControleCarga))")
            .append("    and (")
            .append("			(caf.meioTransporteRodoviario.id = cc.meioTransporteByIdTransportado.id")
            .append("			 and caf.rotaColetaEntrega.id is null")
            .append("			 and caf.rotaIdaVolta.id is null")
            .append("		 	 )")
            .append("		 or ((caf.meioTransporteRodoviario.id is null or caf.meioTransporteRodoviario.id = cc.meioTransporteByIdTransportado.id)")
            .append("			  AND ((cc.rotaColetaEntrega.id is not null and caf.rotaColetaEntrega.id = cc.rotaColetaEntrega.id)")
            .append("				   OR (cc.rotaIdaVolta.id is not null and ridCaf.nrRota = ridCC.nrRota and filialCaf.id = filialCC.id))")
            .append("			 )                                   ")
            .append("		 )");

		return (getAdsmHibernateTemplate().getRowCountForQuery(sb.toString(), parametros).intValue() > 0);
	}
	
	public DomainValue getTipoOperacaoControleCargaDoMeioTransporte(Long idMeioTransporte) {
		StringBuffer sb = new StringBuffer()
			.append("select cc.tpControleCarga from " + ControleCarga.class.getName() + " cc ")
			.append(                 " where cc.tpStatusControleCarga in ('AV', 'AE') ")
			.append(                 "   and cc.meioTransporteByIdTransportado.id = :idMeioTransporte ");
		
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString());		
		q.setParameter("idMeioTransporte", idMeioTransporte);
		List<DomainValue> result = (List<DomainValue>)q.list();
		return result!=null?result.get(0):null;
	}
	
	public List getConfiguracaoAuditoriaVigenteParaTpOperacao(String tpOperacao, Long idFilial) {
		StringBuffer sb = new StringBuffer()
			.append(" from " + ConfiguracaoAuditoria.class.getName() + " ca ")
			.append("where nvl(ca.qtVeiculosProprios, 0) + nvl(ca.qtVeiculosTerceiros, 0) > 0")
			.append("C" .equals(tpOperacao) ? 
					"  and (ca.tpOperacao = :tpRotaColetaEntrega or ca.tpOperacao = :tpAmbos)" : 
					"  and (ca.tpOperacao = :tpRotaViagem        or ca.tpOperacao = :tpAmbos)")
			.append("  and trunc(ca.dtVigenciaInicial) <= :hoje ")
			.append("  and trunc(ca.dtVigenciaFinal)  >= :hoje ")
			.append("  and ca.hrConfiguracaoInicial    <= :agora ")
			.append("  and ca.hrConfiguracaoFinal      >= :agora ")
			.append("  and ca.filial.id      = :idFilial "); 

		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString());
		
		if ("C" .equals(tpOperacao))
			q.setParameter("tpRotaColetaEntrega", "E");

		else if ("V" .equals(tpOperacao))
			q.setParameter("tpRotaViagem", "V");
		
		q.setParameter("tpAmbos", "A");
		q.setParameter("hoje", JTDateTimeUtils.getDataAtual(), Hibernate.custom(JodaTimeYearMonthDayUserType.class));
		q.setParameter("agora", JTDateTimeUtils.getHorarioAtual(), Hibernate.custom(JodaTimeTimeOfDayUserType.class));
		q.setParameter("idFilial", idFilial);
		
		return q.list();
	}
	
	public Long getIdControleCargaByIdMeioTransporte(Long idMeioTransporte) {
		StringBuffer s = new StringBuffer()
			.append("select cc.id from " + ControleCarga.class.getName() + " cc \n")
			.append(" where cc.meioTransporteByIdTransportado.id = ? \n")
			.append("   and cc.tpStatusControleCarga in ('AV', 'AE') \n");
		
		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s.toString());
		q.setParameter(0, idMeioTransporte);
		

		return (Long) q.uniqueResult();
	}
   


}