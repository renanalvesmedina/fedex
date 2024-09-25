package com.mercurio.lms.expedicao.model.dao;

import java.io.InputStream;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.TBInputDocuments;

public class NotaFiscalEletronicaDAO extends AdsmDao {
	
	public TBInputDocuments createTBInputDocuments(final TBInputDocuments tbInputDocuments) {
		
		HibernateCallback hcb = new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = " select TBINPUTDOCUMENTS_SEQ.nextval as id from dual ";
				
				SQLQuery query = session.createSQLQuery(sql);
				
				query.addScalar("id", Hibernate.LONG);
				
				return (Long) query.uniqueResult();
			}
			
		};
		
		Long id = (Long) getAdsmHibernateTemplate().execute(hcb);
		
		tbInputDocuments.setId(id);
		
		hcb = new HibernateCallback() {
			
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				String sql = " insert into TBINPUTDOCUMENTS (ID,JOBKEY,INSERTDATE,DOCSTATUS,DOCKIND,DOCDATA) values (:ID,:JOBKEY,:INSERTDATE,:DOCSTATUS,:DOCKIND,:DOCDATA) ";
				
				SQLQuery query = session.createSQLQuery(sql);
				query.setLong("ID", tbInputDocuments.getId());
				query.setLong("JOBKEY", tbInputDocuments.getJobKey());
				query.setTimestamp("INSERTDATE", tbInputDocuments.getInsertDate().toDate());
				query.setInteger("DOCSTATUS", tbInputDocuments.getDocStatus());
				query.setInteger("DOCKIND", tbInputDocuments.getDocKind());
				query.setBinary("DOCDATA", tbInputDocuments.getDocData());

				query.executeUpdate();
				return null;
			}
			
		};
		
		getAdsmHibernateTemplate().execute(hcb);
		
		return tbInputDocuments;
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List findNotasEDINaoProcessadas(Integer qtLimiteNotas, Integer qtLimiteDias) {
		List retorno = new ArrayList();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct a.xml_table_idx from nfe_idx a ");
		List<String> tabelas = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();

		sql = new StringBuilder();
		sql.append("select a.xml_chave_nfe, a.nf_emitente, a.nf_destinatario ");
		sql.append("from nomeTabela a ");
		sql.append("where not exists ( ");
		sql.append("	select 1 ");
		sql.append("	from  nfe_processo_status b ");
		sql.append("	where b.id_processo_status = 1 ");
		sql.append("	and b.id_processo = a.xml_id ");
		sql.append("	and b.id_status = 'P' ");
		sql.append(") ");
		sql.append("and a.nf_emissao >= sysdate-").append(qtLimiteDias).append(" ");
		sql.append("and rownum <= ").append(qtLimiteNotas).append(" ");
		sql.append("order by a.nf_emitente, a.nf_destinatario");
		
		for (String nomeTabela : tabelas) {
			String sqlString = sql.toString();
			sqlString = sqlString.replace("nomeTabela", nomeTabela);
			retorno.addAll(getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlString).list());
		}

		return retorno;
	}
	
	/**
	 * 
	 * @param chaveNFe
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object findNfeItensByNrChave(String chaveNFe) {
		if (chaveNFe != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT XML_ID ");
			sql.append("FROM NFE_IDX ");
			sql.append("WHERE XML_CHAVE_NFE = '");
			sql.append(chaveNFe);
			sql.append("' AND ROWNUM <=1 ");

			List xmlId = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();

			if (xmlId != null && !xmlId.isEmpty()) {
				return xmlId.get(0);
			}
		}

		return null;
	}
	
	/**
	 * 
	 * @param xmlId
	 * @return
	 */
	public String findNfeItensByID(Object xmlId) {

		// Regra 1
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT xml_table_idx, xml_table_clb ");
		sql.append("FROM NFE_IDX ");
		sql.append("WHERE xml_id = ");
		sql.append(xmlId);
		List resultadoConsultaRegra1 = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();

		for (Object camposRegra1 : resultadoConsultaRegra1) {
			Object[] campo = (Object[]) camposRegra1;

			//  xml_table_idx
			Object xml_ns = null;
			Object xml_versao = null;
			if (campo[0] != null) {
				// Regra 2
				sql = new StringBuilder();
				sql.append("SELECT xml_ns, xml_versao ");
				sql.append("FROM ");
				sql.append(campo[0]);
				sql.append(" ");
				sql.append("WHERE XML_ID = ");
				sql.append(xmlId);
				List resultadoConsultaRegra2 = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
						.list();

				Object[] resultado = (Object[]) resultadoConsultaRegra2.get(0);
				xml_ns = "xmlns=\"" + resultado[0] + "\"";
				xml_versao = resultado[1];
			}

			// xml_table_clb
			if (campo[1] != null) {

				// Regra 3
				StringBuilder sqlXml = new StringBuilder();
				sqlXml.append("SELECT xml_data ");
				sqlXml.append("FROM ");
				sqlXml.append(campo[1]);
				sqlXml.append(" ");
				sqlXml.append("WHERE XML_ID = ");
				sqlXml.append(xmlId);

				final ConfigureSqlQuery csq1 = new ConfigureSqlQuery() {
					public void configQuery(org.hibernate.SQLQuery sqlQuery) {
						sqlQuery.addScalar("xml_data", Hibernate.TEXT);
					}
				};
				
				SQLQuery query1 = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sqlXml.toString());
				csq1.configQuery(query1);
				List retorno = query1.list();
				
				if(retorno != null && !retorno.isEmpty()){
					return (String) retorno.get(0);
				}
			}
		}

		return null;
	}
	
	public TypedFlatMap findNfeByNrChave(String chaveNFe) {
		
		if (chaveNFe != null) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT XML_ID ");
			sql.append("FROM NFE_IDX ");
			sql.append("WHERE XML_CHAVE_NFE ='");
			sql.append(chaveNFe);
			sql.append("' AND ROWNUM <=1 ");

			List xmlId = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();

			if (xmlId != null && !xmlId.isEmpty()) {
				return findNfeByID(xmlId.get(0));
			}
		}

		return null;
	}
	
	@SuppressWarnings({"rawtypes" })
	private TypedFlatMap findNfeByID(Object xmlId){
		TypedFlatMap retorno = new TypedFlatMap();

		//Regra 1
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT xml_table_idx, xml_table_clb ");
		sql.append("FROM NFE_IDX ");
		sql.append("WHERE xml_id = ");
		sql.append(xmlId);
		List resultadoConsultaRegra1 = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		
		for (Object camposRegra1 : resultadoConsultaRegra1) {
			Object[] campo = (Object[]) camposRegra1;

			//Campos: 0 = xml_table_idx, 1 = xml_table_clb
			if(campo[0] != null && campo[1] != null){
				sql = new StringBuilder();
				sql.append("SELECT ");
				sql.append("c.CHAVE_BD AS CHAVE_CAMPO, ");
				sql.append("g.Chave_Xml || c.chave_xml AS CHAVE_XML ");
				sql.append("FROM ");
				sql.append("NFE_LAYOUT L, ").append(campo[0]).append(" nfe, NFE_LAYOUT_GRUPO G, NFE_LAYOUT_CAMPO C "); 
				sql.append("WHERE L.ID_LAYOUT = G.ID_LAYOUT "); 
				sql.append("and C.ID_LAYOUT = L.ID_LAYOUT ");
				sql.append("and C.ID_GRUPO = G.ID_GRUPO ");
				sql.append("and l.id_layout in (");
				sql.append("	select max(id_layout) from NFE_LAYOUT nl where nl.versao = REPLACE(nfe.xml_versao, '''', '')");  
				sql.append("	and nl.xml_schema = 'xmlns=\"' || REPLACE(nfe.xml_ns, '''', '')  || '\"' ");
				sql.append(") ");
				sql.append("and nfe.XML_ID = :xmlId " );
				
				final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
					public void configQuery(org.hibernate.SQLQuery sqlQuery) {
						sqlQuery.addScalar("CHAVE_CAMPO", Hibernate.TEXT);
						sqlQuery.addScalar("CHAVE_XML", Hibernate.TEXT);
					}
				};
				
				SQLQuery query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
				csq.configQuery(query);
				query.setParameter("xmlId", xmlId);

            	List resultadoLeituraCamposXml = query.list();
            	
            	for (Object resultado : resultadoLeituraCamposXml) {
            		Object[] resultadoArray = (Object[]) resultado;
            		retorno.put((String) resultadoArray[0], (String) resultadoArray[1]);
				}
			}
		}
		
		return retorno;
	}	
			
	
	/**
	 * 
	 * @param chaveNFe
	 * @return
	 * @throws SQLException
	 */
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Clob> findNfeXMLFromEDISQL(String chaveNFe) throws SQLException {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT XML_TABLE_CLB, XML_ID ");
		sql.append("FROM NFE_IDX A  ");
		sql.append("WHERE A.XML_CHAVE_NFE='");
		sql.append(chaveNFe);
		sql.append("'");
		List consulta = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString()).list();
		
		if (consulta != null && !consulta.isEmpty()) {
			Object[] campos = (Object[]) consulta.get(0);
		
			sql = new StringBuilder();
			sql.append("SELECT XML_DATA FROM ");
			sql.append(campos[0]);
			sql.append(" WHERE XML_ID = :xmlId ");
		
			ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
				public void configQuery(SQLQuery sqlQuery) {
					sqlQuery.addScalar("XML_DATA", Hibernate.CLOB);
				}
			};
		
			SQLQuery query = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createSQLQuery(sql.toString());
			query.setParameter("xmlId", campos[1]);
			configureSqlQuery.configQuery(query);
			
			List<Clob> xml = query.list();
			return xml;
		}
		return new ArrayList<Clob>();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InputStream findNfeXMLFromEDI(String chaveNFe) throws SQLException {
		List<Clob> xml = findNfeXMLFromEDISQL(chaveNFe);
		if (xml != null) {
			if (!xml.isEmpty()) {
				return xml.get(0).getAsciiStream();
			}
			return null;
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String findNfeXMLFromEDIToString(String chaveNFe) throws SQLException {
		List<Clob> xml = findNfeXMLFromEDISQL(chaveNFe);
		if (xml != null) {
			if(!xml.isEmpty()){
				return xml.get(0).getSubString((long)1, (int) xml.get(0).length());
			}
			return null;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> findInfoRpstByDoctoServico(Long idDoctoServico) {
		StringBuilder sql = new StringBuilder()
		.append("select new map(")
				.append("municipio.nmMunicipio as municipioFilialEmissora, ")
				.append("filialOrigemDoctoServico.sgFilial as sgFilialEmissora, ")
				.append("empresa.dsHomePage as site, ")
				.append("doctoServico.nrDoctoServico as nrDoctoServico, ")
				.append("doctoServico.idDoctoServico as idDoctoServico, ")
				.append("rotaColetaEntregaSugerida.nrRota as nrRota, ")
				.append("doctoServico.dtPrevEntrega as dtPrevEntrega, ")
				
				.append("clienteRemetentePessoa.nmPessoa as nmRemetente, ")
				.append(PropertyVarcharI18nProjection.createProjection("tipoLogradouroRemetente.dsTipoLogradouro") +
						"|| ' ' || enderecoPessoaRemetente.dsEndereco " +
						"|| ', ' || enderecoPessoaRemetente.nrEndereco || ' ' || enderecoPessoaRemetente.dsComplemento" +
						"|| ' ' || enderecoPessoaRemetente.dsBairro as nmEnderecoRemetente, ")
				.append("enderecoPessoaRemetente.nrCep as nrCepRemetente, ")
				.append("municipioRemetente.nmMunicipio as nmMunicipioRemetente, ")
				.append("unidadeFederativaRemetente.sgUnidadeFederativa as sgUnidadeFederativaRemetente, ")
				.append("clienteRemetentePessoa.nrIdentificacao as nrIdentificacaoRemetente, ")
				.append("clienteRemetentePessoa.nrInscricaoMunicipal as nrInscricaoMunicipalRemetente, ")
				
				.append("clienteDestinatarioPessoa.nmPessoa as nmDestinatario, ")
				.append(PropertyVarcharI18nProjection.createProjection("tipoLogradouroDestinatario.dsTipoLogradouro") + 
						"|| ' ' || enderecoPessoaDestinatario.dsEndereco " +
						"|| ', ' || enderecoPessoaDestinatario.nrEndereco || ' ' || enderecoPessoaDestinatario.dsComplemento" +
						"|| ' ' || enderecoPessoaDestinatario.dsBairro as nmEnderecoDestinatario, ")
				.append("enderecoPessoaDestinatario.nrCep as nrCepDestinatario, ")
				.append("municipioDestinatario.nmMunicipio as nmMunicipioDestinatario, ")
				.append("unidadeFederativaDestinatario.sgUnidadeFederativa as sgUnidadeFederativaDestinatario, ")
				.append("clienteDestinatarioPessoa.nrIdentificacao as nrIdentificacaoDestinatario, ")
				.append("clienteDestinatarioPessoa.nrInscricaoMunicipal as nrInscricaoMunicipalDestinatario, ")
				
				.append("CASE "
						+ "WHEN trim(conhecimento.dsEnderecoEntrega) = NULL THEN '' "
						+ "ELSE concat(concat(concat(concat(conhecimento.dsEnderecoEntrega, ', '), conhecimento.nrEntrega), ' '), conhecimento.dsBairroEntrega ) "
					+ "END as dsLocalEntregaLinha1, ")
				.append("municipioEntregaConhecimento.nmMunicipio || ' ' ||  unidadeFederativaConhecimento.sgUnidadeFederativa as dsLocalEntregaLinha2, ")
				
				.append("doctoServico.psReal as psDeclarado, ")
				.append("doctoServico.psAferido as psAferido, ")
				.append("doctoServico.psAforado as psAforado, ")
				.append("doctoServico.psReferenciaCalculo as psReferenciaCalculo, ")
				.append("doctoServico.nrCubagemCalculo as nrCubagem, ")
				.append("doctoServico.qtVolumes as qtVolumes, ")
				.append(PropertyVarcharI18nProjection.createProjection("naturezaProduto.dsNaturezaProduto") + " as dsNaturezaProduto, ")
				.append("doctoServico.vlMercadoria as vlMercadoria, ")
				.append("impostoServicos.blRetencaoTomadorServico as blRetencaoTomadorServico, ")
				.append("doctoServico.vlLiquido as vlLiquido ")
				
			.append(") ")
		.append("from ")
			.append(DoctoServico.class.getName()).append(" as doctoServico, ")
			.append(Conhecimento.class.getName()).append(" as conhecimento ")
			.append("left join doctoServico.filialByIdFilialOrigem as filialOrigemDoctoServico ")
			.append("left join doctoServico.rotaColetaEntregaByIdRotaColetaEntregaSugerid as rotaColetaEntregaSugerida ")
		
			//Joins referentes ao municipio do docto de servico. 
			.append("left join filialOrigemDoctoServico.empresa as empresa ")
			.append("left join filialOrigemDoctoServico.pessoa as pessoa ")
			.append("left join pessoa.enderecoPessoa as enderecoPessoa ")
			.append("left join enderecoPessoa.municipio as municipio ")
			
			//Joins para cliente remetente
			.append("left join doctoServico.clienteByIdClienteRemetente as clienteRemetente ")
			.append("left join clienteRemetente.pessoa as clienteRemetentePessoa ")
			.append("left join clienteRemetentePessoa.enderecoPessoa as enderecoPessoaRemetente ")
			.append("left join enderecoPessoaRemetente.tipoLogradouro as tipoLogradouroRemetente ")
			.append("left join enderecoPessoaRemetente.municipio as municipioRemetente ")
			.append("left join municipioRemetente.unidadeFederativa as unidadeFederativaRemetente ")
			
			//Joins para cliente destinatario
			.append("left join doctoServico.clienteByIdClienteDestinatario as clienteDestinatario ")
			.append("left join clienteDestinatario.pessoa as clienteDestinatarioPessoa ")
			.append("left join clienteDestinatarioPessoa.enderecoPessoa as enderecoPessoaDestinatario ")
			.append("left join enderecoPessoaDestinatario.tipoLogradouro as tipoLogradouroDestinatario ")
			.append("left join enderecoPessoaDestinatario.municipio as municipioDestinatario ")
			.append("left join municipioDestinatario.unidadeFederativa as unidadeFederativaDestinatario ")
			
			
			.append("left join conhecimento.municipioByIdMunicipioEntrega as municipioEntregaConhecimento ")
			.append("left join municipioEntregaConhecimento.unidadeFederativa as unidadeFederativaConhecimento ")
			
			.append("left join conhecimento.naturezaProduto as naturezaProduto ")
			
			.append("left join conhecimento.impostoServicos as impostoServicos with ( impostoServicos.tpImposto = 'IS') ")
			
		.append("where ")
			.append("doctoServico.id = conhecimento.id ")
			.append("and doctoServico.id = ? ");

		List param = new ArrayList();
		param.add(idDoctoServico);
		
		List retornoQuery = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		
		Map mapRetorno = new HashMap(); 
		if(retornoQuery != null && retornoQuery.size() > 0)
			mapRetorno = (Map) retornoQuery.get(0);
		
		
		return mapRetorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> findInfoRpssByDoctoServico(Long idDoctoServico) {
		StringBuilder sql = new StringBuilder()
		.append("select new map(")
			.append("municipio.nmMunicipio as municipioFilialEmissora, ")
			.append("filialOrigemDoctoServico.sgFilial as sgFilialEmissora, ")
			.append("empresa.dsHomePage as site, ")
			.append("doctoServico.nrDoctoServico as nrDoctoServico, ")
			.append("doctoServico.idDoctoServico as idDoctoServico, ")
			
			.append("impostoServicos.blRetencaoTomadorServico as blRetencaoTomadorServico, ")
			
			.append("doctoServico.vlLiquido as vlLiquido ")
						
		.append(") ")
			.append("from ")
			.append(DoctoServico.class.getName()).append(" as doctoServico ")
			.append("left join doctoServico.filialByIdFilialOrigem as filialOrigemDoctoServico ")
			.append("left join doctoServico.impostoServicosNFS as impostoServicos with ( impostoServicos.tpImposto = 'IS') ")
			
			//Joins referentes ao municipio do docto de servico
			.append("left join filialOrigemDoctoServico.empresa as empresa ")
			.append("left join filialOrigemDoctoServico.pessoa as pessoa ")
			.append("left join pessoa.enderecoPessoa as enderecoPessoa ")
			.append("left join enderecoPessoa.municipio as municipio ")
			
		.append("where doctoServico.id = ? ");
		
		List param = new ArrayList();
		param.add(idDoctoServico);
		
		List retornoQuery = getAdsmHibernateTemplate().find(sql.toString(), param.toArray());
		
		Map mapRetorno = new HashMap(); 
		if(retornoQuery != null && retornoQuery.size() > 0)
			mapRetorno = (Map) retornoQuery.get(0);
		
		
		return mapRetorno;
	}

}
