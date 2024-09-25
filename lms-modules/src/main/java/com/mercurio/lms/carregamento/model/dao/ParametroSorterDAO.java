package com.mercurio.lms.carregamento.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ParametroSorter;

/**
 *   Código do Jira :Requisito LMS560
 *   Autor/Data:  Tairone Lopes / 20/07/2011
 *   Descrição: Geração e envio de arquivo xml para o sorter
 *
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class ParametroSorterDAO extends BaseCrudDao<ParametroSorter, Long> {


	@Override
	protected void initFindListLazyProperties(Map lazyFindList) {
		lazyFindList.put("filial", FetchMode.JOIN);
		lazyFindList.put("filial.pessoa", FetchMode.JOIN);
		super.initFindListLazyProperties(lazyFindList);
	}
	
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	
	@Override
	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filial", FetchMode.JOIN);
		lazyFindLookup.put("filial.pessoa", FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}
	
	@Override
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("filial", FetchMode.JOIN);
		lazyFindPaginated.put("filial.pessoa", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}
	
	@Override
	protected Class getPersistentClass() {
		return ParametroSorter.class;
	}

	public ResultSetPage findPaginatedParametroSorter(TypedFlatMap  criteria) {
		SqlTemplate sql = createHqlPaginated(criteria);
		FindDefinition findDefinition = FindDefinition.createFindDefinition(criteria);
		return this.getAdsmHibernateTemplate().findPaginated(sql.getSql(),
				findDefinition.getCurrentPage(),findDefinition.getPageSize(),sql.getCriteria());
	}
	
	public Integer getRowCountFilial(TypedFlatMap criteria) {
		SqlTemplate sql = createHqlPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
	}
	
	private SqlTemplate createHqlPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("new map( filial.idFilial","idFilial");
		sql.addProjection("parametroSorter.idParametroSorter", "idParametroSorter");
		sql.addProjection("parametroSorter.nmAgendamento", "nmAgendamento");
		sql.addProjection("parametroSorter.nmAereo", "nmAereo");
		sql.addProjection("pessoa.nmFantasia" , "pessoa_nmFantasia)");
		//From,
		StringBuffer froms = new StringBuffer();
		froms.append( ParametroSorter.class.getName() ).append(" as parametroSorter ")
		.append("inner join parametroSorter.filial as filial ")
		.append("inner join filial.pessoa as pessoa ");
		sql.addFrom(froms.toString());

		//Critérios
	
		Long idFilial  = MapUtils.getLong(criteria, "idFilial");
		if (idFilial!=null && idFilial.longValue() > 0) {
			sql.addCriteria("filial.idFilial", "=", idFilial);	
		}
		
		String sgFilial  = MapUtils.getString(criteria, "sgFilial");
		if (StringUtils.isNotBlank(sgFilial)) {
			sql.addCriteria("filial.sgFilial", "=", sgFilial);	
		}
		
		String nmFantasia  = MapUtils.getString(criteria, "nmFantasia");
		if (StringUtils.isNotBlank(nmFantasia)) {
			sql.addCriteria("filial.pessoa.nmFantasia", "=", nmFantasia);	
		}
		
		sql.addCriteria("filial.blSorter", "=", Boolean.TRUE);
		return sql;
	}
	
	/**
	 * Método responsável por encontrar os conhecimentos de agendamentos em aberto 
	 * @param parametroSorter
	 * @return
	 */
	private List findDocumentosDeAgendamentosAbertos(Long idFilial){
		//* por problema de performance da querye foi definido usar sql nativo enviada pelo DBA
		StringBuffer sql = new StringBuffer();
		
		sql.append("select vnf.nr_volume_embarque ");
		sql.append("from agendamento_entrega ae, ");
		sql.append("agendamento_docto_servico ads, ");
		sql.append("docto_servico ds, ");
		sql.append("ocorrencia_entrega oe, ");
		sql.append("evento e, ");
		sql.append("nota_fiscal_conhecimento nfc, ");
		sql.append("volume_nota_fiscal vnf ");
		sql.append("where ");
		sql.append("'A' = ae.tp_situacao_agendamento ");
		sql.append("AND ads.id_agendamento_entrega = ae.id_agendamento_entrega ");
		sql.append("AND ds.id_docto_servico = ads.id_docto_servico ");
		sql.append("AND "+idFilial+" = ds.id_filial_destino ");
		sql.append("AND 1 = oe.cd_ocorrencia_entrega ");
		sql.append("AND e.id_evento = oe.id_evento ");
		sql.append("AND nfc.id_conhecimento = ds.id_docto_servico ");
		sql.append("AND vnf.id_nota_fiscal_conhecimento = nfc.id_nota_fiscal_conhecimento ");
		sql.append("AND e.id_localizacao_mercadoria <> vnf.id_localizacao_mercadoria ");
		sql.append("AND ads.id_docto_servico = nfc.id_conhecimento ");
		sql.append("AND (e.id_localizacao_mercadoria > ds.id_localizacao_mercadoria ");
		sql.append("OR e.id_localizacao_mercadoria < ds.id_localizacao_mercadoria) ");
		
		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	
	}
	
	
	/**
	 * Método responsável por encontrar os conhecimentos com indicador de agendamentos 
	 * @param parametroSorter
	 * @return
	 */
	private  List findDocumentosResponsavelComIndicadorDeAgendamentos(Long idFilial){
		StringBuffer sql = new StringBuffer();
		
		sql.append("select vnf.nr_volume_embarque ");
		
		sql.append(" from docto_servico ds, conhecimento co,  devedor_doc_serv dds,   cliente c, ");
		sql.append(" ocorrencia_entrega oe, evento e, nota_fiscal_conhecimento nfc, volume_nota_fiscal vnf ");
		
		
		sql.append(" where ");
		sql.append(" ds.id_filial_destino = "+idFilial+" ");
		sql.append(" and ds.id_docto_servico = co.id_conhecimento ");
		sql.append(" and co.tp_situacao_conhecimento <> 'C' ");
		sql.append(" and ds.id_docto_servico = dds.id_docto_servico "); 
		sql.append(" and dds.id_cliente = c.id_cliente ");
		sql.append(" and (c.bl_agendamento_pessoa_fisica = 'S' "); 
		sql.append(" or c.bl_agendamento_pessoa_juridica = 'S') "); 
		sql.append(" and oe.cd_ocorrencia_entrega = 1 ");
		sql.append(" and oe.id_evento = e.id_evento ");
		sql.append(" and ds.id_localizacao_mercadoria <> e.id_localizacao_mercadoria "); 
		sql.append(" and ds.id_docto_servico = nfc.id_conhecimento ");
		sql.append(" and nfc.id_nota_fiscal_conhecimento = vnf.id_nota_fiscal_conhecimento "); 
		sql.append(" and vnf.id_localizacao_mercadoria <> e.id_localizacao_mercadoria ");
		
		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();

	}
	
	
	/**
	 * Método responsável por encontrar os conhecimentos com indicador de dificuldade de entrega
	 * @param parametroSorter
	 * @return
	 */
	private List findDocumentosDestinatarioComIndicadorDeDificuldadeDeEntrega(Long idFilial){
		StringBuffer sql = new StringBuffer();
		
		sql.append("select /*+ FIRST_ROWS(30) */ vnf.nr_volume_embarque ");
		sql.append(" from docto_servico ds, "); 
		sql.append("   conhecimento co, "); 
		sql.append("   cliente c, "); 
		sql.append("   ocorrencia_entrega oe, "); 
		sql.append("   evento e, "); 
		sql.append("   nota_fiscal_conhecimento nfc, "); 
		sql.append("   volume_nota_fiscal vnf ");
		sql.append(" where ");
		sql.append("    ds.id_filial_destino = "+idFilial+" "); 
		sql.append("    and ds.id_docto_servico = co.id_conhecimento "); 
		sql.append("    and co.tp_situacao_conhecimento <> 'C' ");
		sql.append("    and ds.id_cliente_destinatario = c.id_cliente "); 
		sql.append("    and c.bl_dificuldade_entrega = 'S' "); 
		sql.append(" 	and oe.cd_ocorrencia_entrega = 1 "); 
		sql.append("    and oe.id_evento = e.id_evento "); 
		sql.append("    and (ds.id_localizacao_mercadoria < e.id_localizacao_mercadoria "); 
		sql.append("          OR ds.id_localizacao_mercadoria > e.id_localizacao_mercadoria) "); 
		sql.append("    and ds.id_docto_servico = nfc.id_conhecimento "); 
		sql.append("    and nfc.id_nota_fiscal_conhecimento = vnf.id_nota_fiscal_conhecimento "); 
		sql.append("    and (vnf.id_localizacao_mercadoria < e.id_localizacao_mercadoria "); 
		sql.append("          OR vnf.id_localizacao_mercadoria > e.id_localizacao_mercadoria) ");
		return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		
	}
	public List findAgendamentos(Long idFilial){
		List retorno = new ArrayList();
		
		//1 findDocumentosDeAgendamentosAbertos
		retorno.addAll(findDocumentosDeAgendamentosAbertos(idFilial));

		//2 findDocumentosResponsavelComIndicadorDeAgendamentos
		retorno.addAll(findDocumentosResponsavelComIndicadorDeAgendamentos(idFilial));

		//3 findDocumentosDestinatarioComIndicadorDeDificuldadeDeEntrega
		retorno.addAll(findDocumentosDestinatarioComIndicadorDeDificuldadeDeEntrega(idFilial));

		return retorno;
	}
	
	/**
	 * Método responsável por encontrar os conhecimentos com serviço aéreo 
	 * @return
	 */
	public List<String> findDocumentosAereos(){
		StringBuffer sql = new StringBuffer();

		sql.append("select /*+ RULE */ vnf.nr_volume_embarque");  
		sql.append(" from docto_servico ds, ");
	    sql.append("   conhecimento co, ");
	    sql.append("   servico s, ");
	    sql.append("   ocorrencia_entrega oe, "); 
	    sql.append("   evento e, ");
	    sql.append("   nota_fiscal_conhecimento nfc, "); 
	    sql.append("   volume_nota_fiscal vnf ");
	    sql.append(" where ");
	    sql.append("   ds.id_servico = s.id_servico "); 
	    sql.append("   and ds.id_docto_servico = co.id_conhecimento "); 
	    sql.append("   and co.tp_situacao_conhecimento <> 'C' ");
	    sql.append("   and s.tp_modal = 'A' ");
	    sql.append("   and oe.cd_ocorrencia_entrega = 1 "); 
	    sql.append("   and oe.id_evento = e.id_evento ");
	    sql.append("   and ds.id_localizacao_mercadoria <> e.id_localizacao_mercadoria "); 
	    sql.append("   and ds.id_docto_servico = nfc.id_conhecimento ");
	    sql.append("   and nfc.id_nota_fiscal_conhecimento = vnf.id_nota_fiscal_conhecimento "); 
	    sql.append("   and vnf.id_localizacao_mercadoria <> e.id_localizacao_mercadoria ");
	    
	    return getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
	} 
}
