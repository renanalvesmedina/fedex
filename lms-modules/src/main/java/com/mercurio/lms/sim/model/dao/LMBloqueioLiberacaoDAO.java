package com.mercurio.lms.sim.model.dao;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.expedicao.model.DoctoServico;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class LMBloqueioLiberacaoDAO extends AdsmDao {
	
	public ResultSetPage findPaginatedBloqueiosLiberacoes(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(ods.dhBloqueio as dhBloqueio, " +
				"ods.dhLiberacao as dhLiberacao, " +
				"fb.sgFilial as sgFilialBloqueio, " +
				"fl.sgFilial as sgFilialLiberacao, " +
				"ub.nmUsuario as nmUsuarioBloqueio, " +
				"ul.nmUsuario as nmUsuarioLiberacao, " +
				""+PropertyVarcharI18nProjection.createProjection("opb.dsOcorrencia")+" as dsOcorrenciaBloqueio, " +
				""+PropertyVarcharI18nProjection.createProjection("opl.dsOcorrencia")+" as dsOcorrenciaLiberacao )");
		
		hql.addFrom(DoctoServico.class.getName()+ " ds " +
				"left outer join ds.ocorrenciaDoctoServicos ods " +
				"left outer join ods.ocorrenciaPendenciaByIdOcorBloqueio opb " +
				"left outer join ods.ocorrenciaPendenciaByIdOcorLiberacao opl " +
				"left outer join ods.usuarioBloqueio ub " +
				"left outer join ods.usuarioLiberacao ul " +
				"left outer join ods.filialByIdFilialLiberacao fl " +
				"left outer join ods.filialByIdFilialBloqueio fb ");
				
		
		hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
		
		hql.addOrderBy("ods.dhBloqueio.value");
		
		return getAdsmHibernateTemplate().findPaginated(hql.getSql(),Integer.valueOf(1), Integer.valueOf(1000), hql.getCriteria());
	}
}


