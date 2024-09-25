package com.mercurio.lms.expedicao.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbOcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class AwbOcorrenciaPendenciaDAO extends BaseCrudDao<AwbOcorrenciaPendencia, Long> {

	@Override
	protected Class getPersistentClass() {
		return AwbOcorrenciaPendencia.class	;
	}
	
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("awb", FetchMode.JOIN);
		lazyFindLookup.put("ocorrenciaPendencia", FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}

	public List<AwbOcorrenciaPendencia> findAwbOcorrenciaByAwb(Long idAwb) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT aop ")
		.append(" FROM ")
		.append(getPersistentClass().getSimpleName()).append(" aop ")
		.append(" JOIN FETCH aop.awb aw")
		.append(" JOIN FETCH aop.ocorrenciaPendencia op ")
		.append(" JOIN FETCH aop.usuarioLms uLms ")
		.append(" JOIN FETCH uLms.usuarioADSM uAdsm ")
		.append(" WHERE ")
		.append(" aw.idAwb = ? ")
		.append(" ORDER BY aop.dhOcorrencia ");
		
		return getAdsmHibernateTemplate().find(sql.toString(), idAwb);
	}

	
}