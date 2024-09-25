package com.mercurio.lms.sim.model.dao;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMEventoDAO extends AdsmDao {
	
	public ResultSetPage findPaginatedEventos(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(eds.tpDocumento as tpDocumento, " +
				"eds.nrDocumento as nrDocumento, " +
				"descEv.dsDescricaoEvento as dsDescricaoEvento, " +
				"eds.dhEvento as dhEvento, " +
				"eds.obComplemento as obComplemento, " +
				"eds.dhInclusao as dhInclusao, " +
				"eds.idEventoDocumentoServico as idEventoDocumentoServico, " +
				"eds.blEventoCancelado as blEventoCancelado , "+
				"(case when ocorrEnt.idOcorrenciaEntrega is not null then ocorrEnt.dsOcorrenciaEntrega else ocorrPen.dsOcorrencia end ) as ocorrencia)");
		hql.addFrom(EventoDocumentoServico.class.getName()+" eds " +
				"join eds.doctoServico ds " +
				"left outer join eds.evento ev " +
				"left outer join ev.descricaoEvento descEv "+
				"left outer join eds.ocorrenciaEntrega ocorrEnt " +
				"left outer join eds.ocorrenciaPendencia ocorrPen");
		
		hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
		
		hql.addOrderBy("eds.dhEvento.value");
		hql.addOrderBy("eds.id");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),Integer.valueOf(1), Integer.valueOf(1000), hql.getCriteria());
	}

}
