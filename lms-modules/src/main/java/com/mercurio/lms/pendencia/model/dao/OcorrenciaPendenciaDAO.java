package com.mercurio.lms.pendencia.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class OcorrenciaPendenciaDAO extends BaseCrudDao<OcorrenciaPendencia, Long> {

	protected void initFindByIdLazyProperties(Map map) {
		map.put("evento", FetchMode.JOIN);
		map.put("evento.descricaoEvento", FetchMode.JOIN);		
		map.put("evento.localizacaoMercadoria", FetchMode.JOIN);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return OcorrenciaPendencia.class;
	}

	public boolean validateCdOcorrenciaInOcorrencia(Short cdOcorrencia) {
		Integer count = (Integer) getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(getPersistentClass())
		.setProjection(Projections.rowCount())
		.add(Restrictions.eq("cdOcorrencia", cdOcorrencia))
		.uniqueResult();
		return count.intValue() > 0; 
	}

	/**
	 * Método que monta o SQL para pesquisa de Ocorrencias de Liberacao a partir de um id de uma Ocorrencia de Bloqueio
	 * e mais alguns criterios da própria Ocorrencia de Liberação que se deseja buscar.
	 * @param idOcorrenciaBloqueio Ocorrencia de Bloqueio para a qual serão buscados as Ocorrencias de Liberacao (obrigatório).
	 * @param cdOcorrenciaLiberacao Código da Ocorrência de Liberação que se deseja buscar (opcional). 
	 * @param blMatriz Se TRUE, buscar permissoes da unidade com valores T e M (T - Matriz e Filial, M - Somente Matriz) para as ocorrências que se deseja buscar. Se FALSE, então buscar T e F (F - Filial).
	 * @param dsOcorrenciaLiberacao Descrição completa ou parte da descrição da Ocorrência de Liberação que se deseja buscar (opcional).
	 * @param blPermiteOcorrenciaParaManifesto Critério para busca de Ocorrências de Liberação.
	 * @param isRowCount true - caso estiver sendo chamada a partir de um método RowCount. false - caso estiver sendo chamada a partir de um findPaginated.
	 * @return
	 */
	//FIXME Ajustar os parametros da consulta (nao concatenar variaveis)
	private StringBuffer mountSqlOcorrenciasLiberacao(Long idDoctoServico, Long idOcorrenciaBloqueio, Integer cdOcorrenciaLiberacao, boolean blMatriz, String dsOcorrenciaLiberacao, Boolean blPermiteOcorrenciaParaManifesto, boolean isRowCount ) {
		//Caso esteja pesquisando as ocorrencias de liberacao a partir de uma ocorrencia de bloqueio...
		StringBuffer hql = new StringBuffer();
		if (!isRowCount){
			hql.append(" select ol ");
		}
		hql.append(" from OcorrenciaPendencia ol,");
		hql.append(" OcorrenciaPendencia ob,");
		hql.append(" FaseProcesso fp,");
		hql.append(" FaseOcorrencia fo,");
		hql.append(" LiberacaoBloqueio lb");
		hql.append(" join fetch ol.evento e ");	
		hql.append(" where lb.ocorrenciaPendenciaByIdOcorrenciaBloqueio = ob");
		hql.append(" and lb.ocorrenciaPendenciaByIdOcorrenciaLiberacao = ol");
		hql.append(" and ol.tpOcorrencia = 'L'");
		hql.append(" and ol.tpSituacao = 'A'");
		hql.append(" and fo.ocorrenciaPendencia = ol");
		hql.append(" and fo.faseProcesso = fp");		
		//Caso dê nullpointer, é porque não foi passado o idOcorrenciaBloqueio (obrigatório).
		//Nesse caso constituirá um erro de tela.
		hql.append(" and ob.id = "+ idOcorrenciaBloqueio.toString());
		
		hql.append(" and fp.idFaseProcesso = (select fp2.idFaseProcesso " +
				" from OcorrenciaDoctoServico ods " +
				" inner join ods.doctoServico ds" +
				" inner join ods.faseProcesso fp2 " +
				" where ds.idDoctoServico = " + idDoctoServico + 
				" and ods.ocorrenciaPendenciaByIdOcorLiberacao is null)");		
		
		if (cdOcorrenciaLiberacao!=null){
			hql.append(" and ol.cdOcorrencia = "+ cdOcorrenciaLiberacao.toString());
		}
		if (blMatriz){
			hql.append(" and ol.tpPermissaoUnidade <> 'F'");
		} else {
			hql.append(" and ol.tpPermissaoUnidade <> 'M'");
		}
		if (dsOcorrenciaLiberacao!=null && !dsOcorrenciaLiberacao.trim().equals("")){
			hql.append(" and lower(ol.dsOcorrencia) like '%"+dsOcorrenciaLiberacao.toLowerCase()+"%'");
		}
		if(blPermiteOcorrenciaParaManifesto!=null) {
			String strBlPermiteOcorrenciaParaManifesto = "N";
			if (blPermiteOcorrenciaParaManifesto) {
				strBlPermiteOcorrenciaParaManifesto = "S";
		}
			hql.append(" and ol.blPermiteOcorParaManif = '"+strBlPermiteOcorrenciaParaManifesto+"'");
		}
		hql.append(" order by ol.cdOcorrencia");
		return hql;
	}
	
	/**
	 * Método que monta o SQL para pesquisa de Ocorrencias de Bloqueio com criterios da própria
	 * Ocorrencia de Bloqueio que se deseja buscar.
	 * @param cdOcorrenciaBloqueio Código da Ocorrência de Bloqueio que se deseja buscar (opcional).
	 * @param blMatriz Se TRUE, buscar permissoes da unidade com valores T e M (T - Matriz e Filial, M - Somente Matriz) para as ocorrências que se deseja buscar. Se FALSE, então buscar T e F (F - Filial).
	 * @param dsOcorrenciaBloqueio Descrição completa ou parte da descrição da Ocorrência de Bloqueio que se deseja buscar (opcional).
	 * @param blPermiteOcorrenciaParaManifesto Critério para busca de Ocorrências de Bloqueio (opcional).
	 * @param isRowCount true - caso estiver sendo chamada a partir de um método RowCount. false - caso estiver sendo chamada a partir de um findPaginated (obrigatório).
	 * @return
	 */
	//FIXME Ajustar os parametros da consulta (nao concatenar variaveis)
	private StringBuffer mountSqlOcorrenciasBloqueio(Long idDoctoServico, Integer cdOcorrenciaBloqueio, boolean blMatriz, String dsOcorrenciaBloqueio, Boolean blPermiteOcorrenciaParaManifesto, boolean isRowCount ) {
		//Caso esteja pesquisando as ocorrencias de bloqueio a partir de uma ocorrencia de liberacao...
		StringBuffer hql = new StringBuffer();
		if (!isRowCount){
			hql.append(" select ob ");
		}
		hql.append(" from FaseProcesso fp,");
		hql.append(" FaseOcorrencia fo,");
		hql.append(" OcorrenciaPendencia ob");
		hql.append(" join fetch ob.evento e");		
		hql.append(" where ob.tpOcorrencia = 'B'");
		hql.append(" and ob.tpSituacao = 'A'");
		hql.append(" and fo.ocorrenciaPendencia = ob");
		hql.append(" and fo.faseProcesso = fp");
		hql.append(" and fp.idFaseProcesso = (select fp2.idFaseProcesso " +
											" from DoctoServico ds " +
											" inner join ds.localizacaoMercadoria lm " +
											" inner join lm.faseProcesso fp2 " +
											" where ds.idDoctoServico = " + idDoctoServico + ")");
		
		if (cdOcorrenciaBloqueio!=null){
			hql.append(" and ob.cdOcorrencia = "+ cdOcorrenciaBloqueio.toString());
		}
		if (blMatriz){
			hql.append(" and ob.tpPermissaoUnidade <> 'F'");
		} else {
			hql.append(" and ob.tpPermissaoUnidade <> 'M'");
		}
		if (dsOcorrenciaBloqueio!=null && !dsOcorrenciaBloqueio.trim().equals("")){
			hql.append(" and lower(ob.dsOcorrencia) like '%"+dsOcorrenciaBloqueio.toLowerCase()+"%'");
		}
		if(blPermiteOcorrenciaParaManifesto != null) {
			String strBlPermiteOcorrenciaParaManifesto = "N";
			if (blPermiteOcorrenciaParaManifesto) {
				strBlPermiteOcorrenciaParaManifesto = "S";
		}
			hql.append(" and ob.blPermiteOcorParaManif = '"+strBlPermiteOcorrenciaParaManifesto+"'");
		}
		hql.append(" order by ob.cdOcorrencia");
		return hql;
	}

	/**
	 * Retorna um map com os objetos a serem mostrados na grid.
	 * 
	 * @param TypedFlatMap criteria
	 * @param FindDefinition findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedToRegistrarOcorrenciasDoctosServico(TypedFlatMap criteria, FindDefinition findDefinition) {
		Boolean blPermiteOcorParaManif = criteria.getBoolean("blPermiteOcorParaManif");
		Boolean blMatriz = criteria.getBoolean("blMatriz");
		String tpOcorrencia = criteria.getString("tpOcorrencia");
		Integer cdOcorrencia = criteria.getInteger("cdOcorrencia"); 
		String dsOcorrencia = criteria.getString("dsOcorrencia");
		Long idOcorrenciaBloqueio = criteria.getLong("idOcorrenciaBloqueio");
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		StringBuffer hql = new StringBuffer();
		if (tpOcorrencia.equalsIgnoreCase("L")){
			hql = mountSqlOcorrenciasLiberacao(idDoctoServico, idOcorrenciaBloqueio, cdOcorrencia, blMatriz, dsOcorrencia, blPermiteOcorParaManif, false);	
		} else if (tpOcorrencia.equalsIgnoreCase("B")){
			hql = mountSqlOcorrenciasBloqueio(idDoctoServico, cdOcorrencia, blMatriz, dsOcorrencia, blPermiteOcorParaManif, false);
		}
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), new HashMap<String, Object>());
	}

	/**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados 
	 * para determinados parametros.
	 * 
	 * @param TypedFlatMap criteria
	 * @param FindDefinition findDefinition
	 * @return
	 */
	public Integer getRowCountToRegistrarOcorrenciasDoctosServico(TypedFlatMap criteria, FindDefinition findDefinition) {
		Boolean blPermiteOcorParaManif = criteria.getBoolean("blPermiteOcorParaManif");
		Boolean blMatriz = criteria.getBoolean("blMatriz").booleanValue();
		String tpOcorrencia = criteria.getString("tpOcorrencia");
		Integer cdOcorrencia = criteria.getInteger("cdOcorrencia"); 
		String dsOcorrencia = criteria.getString("dsOcorrencia");
		Long idOcorrenciaBloqueio = criteria.getLong("idOcorrenciaBloqueio");
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		StringBuffer hql = new StringBuffer();
		if (tpOcorrencia.equalsIgnoreCase("L")){
			hql = mountSqlOcorrenciasLiberacao(idDoctoServico, idOcorrenciaBloqueio, cdOcorrencia, blMatriz, dsOcorrencia, blPermiteOcorParaManif, true);	
		} else if (tpOcorrencia.equalsIgnoreCase("B")){
			hql = mountSqlOcorrenciasBloqueio(idDoctoServico, cdOcorrencia, blMatriz, dsOcorrencia, blPermiteOcorParaManif, true);
		}
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), criteria);
	}

	/**
	 * Retorna uma lista com os objetos.
	 * 
	 * @param TypedFlatMap criteria
	 * @return
	 */
	public List findLookupToRegistrarOcorrenciasDoctosServico(TypedFlatMap criteria) {
		Boolean blPermiteOcorParaManif = criteria.getBoolean("blPermiteOcorParaManif");
		Boolean blMatriz = criteria.getBoolean("blMatriz").booleanValue();
		String tpOcorrencia = criteria.getString("tpOcorrencia");
		Integer cdOcorrencia = criteria.getInteger("cdOcorrencia"); 
		String dsOcorrencia = criteria.getString("dsOcorrencia");
		Long idOcorrenciaBloqueio = criteria.getLong("idOcorrenciaBloqueio");
		Long idDoctoServico = criteria.getLong("idDoctoServico");
		StringBuffer hql = new StringBuffer();
		if (tpOcorrencia.equalsIgnoreCase("L")){
			hql = mountSqlOcorrenciasLiberacao(idDoctoServico, idOcorrenciaBloqueio, cdOcorrencia, blMatriz, dsOcorrencia, blPermiteOcorParaManif, false);	
		} else if (tpOcorrencia.equalsIgnoreCase("B")){
			hql = mountSqlOcorrenciasBloqueio(idDoctoServico, cdOcorrencia, blMatriz, dsOcorrencia, blPermiteOcorParaManif, false);
		}
		Query query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString());
		return query.list();
	}

	public OcorrenciaPendencia fingOcorrenciaByCdOcorrencia(java.lang.Short cdOcorrencia) {
			StringBuffer hql = new StringBuffer()
			.append(" select op ")
			.append(" from ").append(OcorrenciaPendencia.class.getName()).append(" as op ")
			.append(" where op.cdOcorrencia = ? ");
		
			List<OcorrenciaPendencia> lista = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{cdOcorrencia});
			if (lista.isEmpty())
				return null;

			return lista.get(0);
}

	public List<OcorrenciaPendencia> findOcorrenciaPendenciaAtiva() {
		// TODO Auto-generated method stub
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("SELECT op ")
		.append(" FROM ")
		.append(getPersistentClass().getSimpleName()).append(" AS op ")
		.append("WHERE ")
		.append(" op.tpSituacao = ?")
		.append(" ORDER BY op.cdOcorrencia");
		
		return getAdsmHibernateTemplate().find(sql.toString(), "A");
	}

	public Long fingEventoIdEventoByCdOcorrencia(Short cdOcorrencia) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select op.idOcorrenciaPendencia ");
		sql.append(" from ").append(OcorrenciaPendencia.class.getName()).append(" as op ");
		sql.append(" where op.tpSituacao = 'A' and op.cdOcorrencia = :cdOcorrencia ");

		Map criteria = new HashMap();
		criteria.put("cdOcorrencia", cdOcorrencia);

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), criteria);
	}
}
