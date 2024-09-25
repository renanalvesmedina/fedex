package com.mercurio.lms.portaria.model.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.web.taglibs.datatype.JTDateDataType;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.portaria.model.ControleEntSaidaTerceiro;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SaidaChegadaDAO extends AdsmDao {
	

	/**
	 * Consulta a grid VIAGEM da tela de saidas
	 * @param idFilial
	 * @return
	 */
	public List findGridViagem(Long idFilial){
			
			SqlTemplate hql = new SqlTemplate();
			
			hql.addProjection("new Map(cc.idControleCarga|| '|V'", "idControle");					
			hql.addProjection("cc.meioTransporteByIdTransportado.idMeioTransporte", "idMeioTransporte");
			hql.addProjection("mt.nrFrota", "nrFrota");
			hql.addProjection("mt.nrIdentificador", "nrIdentificador");
			hql.addProjection("reb.nrFrota", "nrFrotaReboque");
			hql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");
			hql.addProjection("cc.meioTransporteByIdSemiRebocado.idMeioTransporte", "idSemiReboque");
			hql.addProjection("cc.nrControleCarga", "nrControleCarga");
			hql.addProjection("riv.nrRota", "nrRota");
			hql.addProjection("nvl(r.dsRota,rota.dsRota)", "dsRota");		
			hql.addProjection("f_destino.sgFilial", "sgFilial");
			hql.addProjection("filcc.sgFilial","sgFilialCC");
			hql.addProjection("ct.dhPrevisaoSaida", "dhPrevisaoSaida)");
										 
			hql.addInnerJoin(ControleCarga.class.getName(), "cc");
			hql.addInnerJoin("cc.filialByIdFilialOrigem", "filcc");
			hql.addInnerJoin("cc.meioTransporteByIdTransportado", "mt");
			hql.addLeftOuterJoin("cc.rotaIdaVolta", "riv");
			hql.addLeftOuterJoin("cc.meioTransporteByIdSemiRebocado", "reb");
			hql.addLeftOuterJoin("riv.rota", "r");
			hql.addInnerJoin("cc.controleTrechos", "ct");
			hql.addInnerJoin("ct.filialByIdFilialDestino", "f_destino");
			hql.addLeftOuterJoin("cc.rota", "rota");
							
			hql.addJoin("ct.filialByIdFilialOrigem.id", "cc.filialByIdFilialAtualizaStatus.id");				
			hql.addCustomCriteria("cc.tpStatusControleCarga = 'AV'");
			hql.addCustomCriteria("ct.blTrechoDireto = 'S'");		
			hql.addCriteria("cc.filialByIdFilialAtualizaStatus.idFilial", "=", idFilial);
			hql.addCustomCriteria("exists elements(cc.manifestos)");											
			hql.addOrderBy("mt.nrFrota");
			
			List result = getAdsmHibernateTemplate().find(hql.getSql(true), hql.getCriteria());
			
			return result;
			
	 }
	
	/**
	 * Consulta as saidas com controle de carga
	 * @param idFilial
	 * @return
	 */
	public List findColetaEntregaByControleCargas(Long idFilial){
		 SqlTemplate sql = new SqlTemplate();
		 
		sql.addProjection("new Map( cc.meioTransporteByIdTransportado.idMeioTransporte", "idMeioTransporte");
		sql.addProjection("cc.idControleCarga||'|C'", "idControle");
		sql.addProjection("mt.nrFrota", "nrFrota");
		sql.addProjection("mt.nrIdentificador", "nrIdentificador");		   
		sql.addProjection("cc.meioTransporteByIdSemiRebocado.idMeioTransporte", "idSemiReboque");
		sql.addProjection("reb.nrFrota", "nrFrotaReboque");
		sql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");	
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("cc.nrControleCarga", "nrControleCarga");
		sql.addProjection("rce.nrRota", "nrRota");
		sql.addProjection("rce.dsRota", "dsRota)");		 
		 
		sql.addInnerJoin("ControleCarga", "cc");
		sql.addInnerJoin("cc.filialByIdFilialOrigem", "f");
		sql.addInnerJoin("cc.meioTransporteByIdTransportado", "mt");
		sql.addLeftOuterJoin("cc.meioTransporteByIdSemiRebocado", "reb"); 
		sql.addLeftOuterJoin("cc.rotaColetaEntrega", "rce");
		     
		sql.addCustomCriteria("cc.tpStatusControleCarga = 'AE'");
		sql.addCriteria("cc.filialByIdFilialAtualizaStatus.idFilial", "=", idFilial);
		sql.addOrderBy("mt.nrFrota");
		sql.addOrderBy("mt.nrIdentificador");
				
		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		
	 }
	 
	/**
	 * Consulta as saidas com ordem de saida
	 * @param idFilial
	 * @return
	 */
	public List findColetaEntregaByOrdemSaida(Long idFilial){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new Map (os.meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte", "idMeioTransporte"); 
		sql.addProjection("os.idOrdemSaida||'|O'", "idControle");
		sql.addProjection("mt.nrFrota", "nrFrota");
		sql.addProjection("mt.nrIdentificador", "nrIdentificador ");
		sql.addProjection("os.meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte", "idSemiReboque");						  
		sql.addProjection("reb.nrFrota", "nrFrotaReboque");
		sql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");
		sql.addProjection("''", "nrControleCarga");
		sql.addProjection("''", "nrRota)");
		
		sql.addInnerJoin("OrdemSaida", "os");
		sql.addInnerJoin("os.meioTransporteRodoviarioByIdMeioTransporte", "mtr");
		sql.addInnerJoin("mtr.meioTransporte", "mt");
		sql.addLeftOuterJoin("os.meioTransporteRodoviarioByIdSemiReboque", "rebr");
		sql.addLeftOuterJoin("rebr.meioTransporte", "reb");
		
		sql.addCustomCriteria("os.dhSaida.value is null"); 
	    sql.addCriteria("os.filialByIdFilialOrigem.idFilial", "=", idFilial);
	    sql.addOrderBy("mt.nrFrota");
		sql.addOrderBy("mt.nrIdentificador");
		
		List result = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		return result;
	 }
	 
	/**
	 * Consulta as saidas de terceiros
	 * @return
	 */
	public List findColetaEntregaByTerceiros(Long idFilial, Long idPortaria){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new Map(ct.meioTransporteTerceiro.idMeioTransporteTerceiro", "idMeioTransporte");
		sql.addProjection("ct.idControleEntSaidaTerceiro||'|T'", "idControle");  
		sql.addProjection("mt.nrIdentificao", "nrIdentificador"); 		  
		sql.addProjection("''", "idSemiReboque");
		sql.addProjection("''", "nrFrotaReboque");
		sql.addProjection("ct.nrIdentificacaoSemiReboque", "nrIdentificadorReboque");
		sql.addProjection("''", "nrControleCarga");
		sql.addProjection("''", "nrRota)");
	
		sql.addFrom(new StringBuilder()
				.append(ControleEntSaidaTerceiro.class.getName()).append(" ct ")
				.append(" inner join ct.meioTransporteTerceiro mt ")
				.append(" inner join ct.portaria p ")
				.append(" inner join p.terminal t ")
				.toString());
		sql.addCriteria("ct.portaria.id", "=", idPortaria);
		sql.addCriteria("t.filial.id", "=", idFilial);
		sql.addCustomCriteria("ct.dhSaida.value is null");		
		sql.addOrderBy("mt.nrIdentificao");
			
		return  getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());		
	 }
	
	
	public List findGridViagemChegada(Long idFilial){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("new Map(ct.idControleTrecho || '|V'", "idControle");					
		hql.addProjection("cc.meioTransporteByIdTransportado.idMeioTransporte", "idMeioTransporte");
		hql.addProjection("mt.nrFrota", "nrFrota");
		hql.addProjection("mt.nrIdentificador", "nrIdentificador");
		hql.addProjection("reb.nrFrota", "nrFrotaReboque");
		hql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");
		hql.addProjection("cc.meioTransporteByIdSemiRebocado.idMeioTransporte", "idSemiReboque");
		hql.addProjection("fil.sgFilial", "sgFilial");
		hql.addProjection("cc.nrControleCarga", "nrControleCarga");
		hql.addProjection("riv.nrRota", "nrRota");
		hql.addProjection("nvl(r.dsRota, rota.dsRota)", "dsRota");
		hql.addProjection("ct.dhPrevisaoChegada", "dhPrevisaoChegada)");
		
		hql.addInnerJoin("ControleCarga", "cc");
		hql.addInnerJoin("cc.meioTransporteByIdTransportado", "mt");
		hql.addLeftOuterJoin("cc.rotaIdaVolta", "riv");
		hql.addLeftOuterJoin("riv.rota", "r");
		hql.addLeftOuterJoin("cc.meioTransporteByIdSemiRebocado", "reb");
		hql.addInnerJoin("cc.controleTrechos", "ct");
		hql.addInnerJoin("cc.filialByIdFilialOrigem", "fil");
		hql.addLeftOuterJoin("cc.rota", "rota");
		
		hql.addJoin("ct.filialByIdFilialOrigem.idFilial", "cc.filialByIdFilialAtualizaStatus.idFilial");
		hql.addCustomCriteria(
				"(select frcc.nrOrdem from " + FilialRotaCc.class.getName() + " as frcc " +
				" where frcc.controleCarga.id = cc.id" +
				"   and frcc.filial.id = cc.filialByIdFilialAtualizaStatus.id) " +
				"<" +
				" (select nvl(frcc2.nrOrdem,0) from " + FilialRotaCc.class.getName() + " as frcc2 " +
				" 	   where frcc2.controleCarga.id = cc.id " +
				"		 and frcc2.filial.id = ?)",idFilial);
		
		hql.addCustomCriteria("cc.tpStatusControleCarga = 'EV'");
		hql.addCustomCriteria("ct.blTrechoDireto = 'S'");		
		
		hql.addOrderBy("mt.nrFrota"); 
		
		List result = getAdsmHibernateTemplate().find(hql.getSql(true), hql.getCriteria());
		return result;
	}
	
	public List findControleCargaChegada(Long idFilial){
		
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map(cc.idControleCarga || '|C'", "idControle");
		sql.addProjection("mt.idMeioTransporte", "idMeioTransporte");
		sql.addProjection("mt.nrFrota", "nrFrota");
		sql.addProjection("mt.nrIdentificador", "nrIdentificador ");
		sql.addProjection("reb.idMeioTransporte", "idSemiReboque");
		sql.addProjection("reb.nrFrota", "nrFrotaReboque");
		sql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");
		sql.addProjection("fil.sgFilial", "sgFilial");
		sql.addProjection("cc.nrControleCarga", "nrControleCarga");
		sql.addProjection("rce.nrRota", "nrRota");
		sql.addProjection("rce.dsRota", "dsRota)");
				
		sql.addInnerJoin("ControleCarga", "cc");
		sql.addInnerJoin("cc.meioTransporteByIdTransportado", "mt");
		sql.addLeftOuterJoin("cc.meioTransporteByIdSemiRebocado", "reb");
		sql.addInnerJoin("cc.eventoControleCargas", "ecc");
		sql.addInnerJoin("cc.rotaColetaEntrega", "rce");
		sql.addInnerJoin("cc.filialByIdFilialOrigem", "fil");
		
		sql.addCriteria("cc.filialByIdFilialAtualizaStatus.idFilial", "=", idFilial); 
		
		sql.addCustomCriteria("ecc.tpEventoControleCarga = 'SP'");
		
		sql.addCustomCriteria("ecc.dhEvento.value < ( to_date('" +sf.format(new Date())+ "' ,'DD/MM/YYYY hh24:mi:ss') - (2/1440)) ");
		sql.addCustomCriteria("cc.tpStatusControleCarga = ?");
		sql.addCriteriaValue("TC");
	    sql.addOrderBy("mt.nrFrota");
		sql.addOrderBy("mt.nrIdentificador");
		
		
		
		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		
	}
	
	public List findOrdemSaidaChegada(Long idFilial){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new Map (os.idOrdemSaida || '|O'", "idControle");
		sql.addProjection("os.meioTransporteRodoviarioByIdMeioTransporte.idMeioTransporte", "idMeioTransporte");		  
		sql.addProjection("mt.nrFrota", "nrFrota");
		sql.addProjection("mt.nrIdentificador", "nrIdentificador ");
		sql.addProjection("os.meioTransporteRodoviarioByIdSemiReboque.idMeioTransporte", "idSemiReboque");						  
		sql.addProjection("reb.nrFrota", "nrFrotaReboque");
		sql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque ");
		sql.addProjection("''", "nrControleCarga");
		sql.addProjection("''", "nrRota");
		sql.addProjection("''", "dsRota)");
		
		sql.addInnerJoin("OrdemSaida", "os");
		sql.addInnerJoin("os.meioTransporteRodoviarioByIdMeioTransporte", "mtr");
		sql.addInnerJoin("mtr.meioTransporte", "mt");
		sql.addLeftOuterJoin("os.meioTransporteRodoviarioByIdSemiReboque", "rebr");
		sql.addLeftOuterJoin("rebr.meioTransporte", "reb");
		
		sql.addCustomCriteria("os.dhChegada.value is null");
		sql.addCustomCriteria("os.dhSaida.value is not null"); 
		
		sql.addCustomCriteria("os.blSemRetorno = 'N'");
	    sql.addCriteria("os.filialByIdFilialOrigem.idFilial", "=", idFilial);
	    sql.addOrderBy("mt.nrFrota");
		sql.addOrderBy("mt.nrIdentificador");
		
		List result = getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
		return result;
	}
	 
	public List findGridChegadaSaida(Long idMeioTransporteTerceiro){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new Map(f.sgFilial || ' - ' || pes.nmPessoa as siglaNomeFilial," +
								  "ep.nmPessoa as nmEmpresa," +
								  "ct.dhSaida as dhSaida," +
								  "ct.dhEntrada as dhEntrada," +
								  "mot.nmMotorista as nmMotorista," +
								  "mot.nrCpf as nrCpf," +
								  "mot.nrRg as nrRg," +
								  "mot.nrCnh as nrCnh" +
								  ")");
		
		sql.addFrom("ControleEntSaidaTerceiro ct " +
								"inner join ct.meioTransporteTerceiro mtt "+
								"inner join ct.motoristaTerceiro mot "+
								"inner join ct.portaria p " +
								"inner join p.terminal t " +
								"inner join t.filial f " +
								"inner join f.empresa e inner join e.pessoa ep " +
								"inner join f.pessoa pes"
					);
		sql.addCriteria("mtt.idMeioTransporteTerceiro", "=", idMeioTransporteTerceiro);
		
		sql.addOrderBy("ep.nmPessoa");
		sql.addOrderBy("ct.dhEntrada.value");
		
		return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
	}
}