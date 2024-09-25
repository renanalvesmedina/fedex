package com.mercurio.lms.portaria.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.FilialRotaCc;


public class OperacoesPortuariasDAO extends AdsmDao {
	

	public List findGridEntrega(Long idFilial){
			
			SqlTemplate hql = montarSQLPadrao();

			hql.addCustomCriteria("cc.tpStatusControleCarga = 'TP'");
			hql.addCriteria("cc.filialByIdFilialAtualizaStatus.idFilial", "=", idFilial);
			hql.addOrderBy("mt.nrFrota");
			
			List result = getAdsmHibernateTemplate().find(hql.getSql(true), hql.getCriteria());
			
			return result;
			
	 }
	
	public List findGridColeta(Long idFilial){
		SqlTemplate hql = montarSQLPadrao();

		hql.addCustomCriteria(
				"(select frcc.nrOrdem from " + FilialRotaCc.class.getName() + " as frcc " +
				" where frcc.controleCarga.id = cc.id" +
				"   and frcc.filial.id = cc.filialByIdFilialAtualizaStatus.id) " +
				"=" +
				" (select nvl(frcc2.nrOrdem,0)-1 from " + FilialRotaCc.class.getName() + " as frcc2 " +
				" 	   where frcc2.controleCarga.id = cc.id " +
				"		 and frcc2.filial.id = ?)",idFilial);
		
		hql.addCustomCriteria("cc.tpStatusControleCarga = 'TF'");
		
		hql.addOrderBy("reb.nrFrota"); 
		
		List result = getAdsmHibernateTemplate().find(hql.getSql(true), hql.getCriteria());
		return result;
	}

	private SqlTemplate montarSQLPadrao() {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("new Map(cc.idControleCarga", "idControle");					
		hql.addProjection("cc.meioTransporteByIdTransportado.idMeioTransporte", "idMeioTransporte");
		hql.addProjection("mt.nrFrota", "nrFrota");
		hql.addProjection("mt.nrIdentificador", "nrIdentificador");
		hql.addProjection("reb.nrFrota", "nrFrotaReboque");
		hql.addProjection("reb.nrIdentificador", "nrIdentificadorReboque");
		hql.addProjection("cc.meioTransporteByIdSemiRebocado.idMeioTransporte", "idSemiReboque");
		hql.addProjection("filcc.sgFilial", "sgFilial");
		hql.addProjection("cc.nrControleCarga", "nrControleCarga");
		hql.addProjection("riv.nrRota", "nrRota");
		hql.addProjection("nvl(r.dsRota,rota.dsRota)", "dsRota");		
		hql.addProjection("f_destino.sgFilial", "sgFilialDestino");
		hql.addProjection("filcc.sgFilial","sgFilialCC)");
									 
		hql.addInnerJoin(ControleCarga.class.getName(), "cc");
		hql.addInnerJoin("cc.filialByIdFilialOrigem", "filcc");
		hql.addLeftOuterJoin("cc.meioTransporteByIdTransportado", "mt");
		hql.addLeftOuterJoin("cc.rotaIdaVolta", "riv");
		hql.addLeftOuterJoin("cc.meioTransporteByIdSemiRebocado", "reb");
		hql.addLeftOuterJoin("riv.rota", "r");
		hql.addInnerJoin("cc.filialByIdFilialDestino", "f_destino");
		hql.addLeftOuterJoin("cc.rota", "rota");
		return hql;
	}

}