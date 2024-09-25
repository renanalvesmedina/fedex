package com.mercurio.lms.pendencia.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.pendencia.model.ItemMda;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class MdaDAO extends BaseCrudDao<Mda, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Mda.class;
    }

	protected void initFindLookupLazyProperties(Map map) {
		map.put("moeda", FetchMode.JOIN);
	}

	/**
	 * Método que salva um MDA e ItemMda
	 * 
	 * @param master
	 * @param items
	 * @param config
	 * @return Manifesto
	 */
    public Mda storeAll(Mda master, ItemList items, ItemListConfig config) {
		super.store(master);

		removeListPreManifestoDocumentos(items.getRemovedItems());
		storeListPreManifestoDocumentos(items.getNewOrModifiedItems());

    	getAdsmHibernateTemplate().flush();
        return master;
    }

    /**
     * Salva lista de ItemMda.
     * 
     * @param newOrModifiedItems
     */
	private void storeListPreManifestoDocumentos(List newOrModifiedItems) {
    	for (Iterator iter = newOrModifiedItems.iterator(); iter.hasNext();) {
    		ItemMda itemMda = (ItemMda) iter.next();

    		getAdsmHibernateTemplate().saveOrUpdate(itemMda);
    		getAdsmHibernateTemplate().saveOrUpdateAll(itemMda.getNfItemMdas());
		}
	}

	/**
	 * Remove uma lista de ItemMda.
	 * 
	 * @param removeItems
	 */
	private void removeListPreManifestoDocumentos(List removeItems) {
		getAdsmHibernateTemplate().deleteAll(removeItems);
	}

	/**
	 * Método que retorna um MDA usando como filtro o ID do DoctoServico
	 * 
	 * @param Long
	 *            idDoctoServico
	 * @return Mda
	 */
	public Mda findMdaByIdDoctoServico(Long idDoctoServico) {

		SqlTemplate sqlTemp = new SqlTemplate();
		sqlTemp.addFrom(Mda.class.getName() + " mda join fetch mda.moeda moe "
				+ " join fetch mda.filialByIdFilialOrigem fo "
				+ " join fetch fo.pessoa fop "
				+ " left join fetch mda.filialByIdFilialDestino fd "
				+ " left join fetch fd.pessoa fdp "
				+ " left join fetch mda.usuarioByIdUsuarioDestino ud "
				+ " left join fetch mda.setor s "
				+ " left join fetch mda.clienteByIdClienteRemetente cr "
				+ " left join fetch cr.pessoa crp "
				+ " left join fetch mda.enderecoRemetente er "
				+ " left join fetch er.municipio erm "
				+ " left join fetch erm.unidadeFederativa ermu "
				+ " left join fetch mda.clienteByIdClienteDestinatario cd "
				+ " left join fetch cd.pessoa cdp "
				+ " left join fetch mda.enderecoDestinatario ed "
				+ " left join fetch ed.municipio edm "
				+ " left join fetch edm.unidadeFederativa edmu "
				+ " left join fetch mda.clienteByIdClienteConsignatario cc "
				+ " left join fetch cc.pessoa ccp "
				+ " left join fetch mda.localizacaoMercadoria lm "
				+ " left join fetch mda.filialLocalizacao fl");

		sqlTemp.addCriteria("mda.idDoctoServico", "=", idDoctoServico);

		return (Mda) getAdsmHibernateTemplate().findUniqueResult(
				sqlTemp.getSql(), sqlTemp.getCriteria());
	}

	/**
	 * Método que monta o SQL para pesquisa de MDA
	 * 
	 * @param TypedFlatMap
	 *            criteria
	 * @return
	 */
	private SqlTemplate mountSqlTemp(TypedFlatMap criteria) {
		SqlTemplate sqlTemp = new SqlTemplate();

		sqlTemp.addFrom(Mda.class.getName()
				+ " mda join fetch mda.moeda moe"
				+ "	join fetch mda.filialByIdFilialOrigem fo "
				+
				// " join fetch fo.pessoa fop " + //Ainda esta sendo utilizado
				// na consulta de mda. Na verdade para a recebimento de mda
				" left join fetch mda.filialByIdFilialDestino fd "
				+
				// " left join fetch fd.pessoa fdp " + //Ainda esta sendo
				// utilizado na consulta de mda. Na verdade para a recebimento
				// de mda
				" left join fetch mda.setor s "
				+ " left join fetch mda.clienteByIdClienteRemetente cr "
				+ " left join fetch cr.pessoa crp "
				+ " left join fetch mda.clienteByIdClienteDestinatario cd "
				+ " left join fetch cd.pessoa cdp "
				+ " left join fetch mda.clienteByIdClienteConsignatario cc "
				+ " left join fetch cc.pessoa ccp " +
				// " left join fetch mda.usuarioByIdUsuarioRecebidoPor urp " +
				// //Ainda esta sendo utilizado na consulta de mda. Na verdade
				// para a recebimento de mda
											  " left join fetch mda.localizacaoMercadoria lm ");

		if (criteria.getYearMonthDay("dataInicial") != null) {
			sqlTemp.addCriteria("SYS_EXTRACT_UTC(mda.dhInclusao.value)", ">=",
					criteria.getYearMonthDay("dataInicial"));
		}

		if (criteria.getYearMonthDay("dataFinal") != null) {
			sqlTemp.addCriteria("SYS_EXTRACT_UTC(mda.dhInclusao.value)", "<",
					criteria.getYearMonthDay("dataFinal").plusDays(1));
		}

		if (criteria.getLong("nrDoctoServico") != null) {
			sqlTemp.addCriteria("mda.nrDoctoServico", "=",
					criteria.getLong("nrDoctoServico"));
	    }

		if (criteria.getString("tpRemetenteMda") != null
				&& !"".equals(criteria.getString("tpRemetenteMda"))) {
			sqlTemp.addCriteria("mda.tpRemetenteMda", "=",
					criteria.getString("tpRemetenteMda"));
	    }

		if (criteria.getLong("filialByIdFilialOrigem.idFilial") != null) {
			sqlTemp.addCriteria("fo.id", "=",
					criteria.getLong("filialByIdFilialOrigem.idFilial"));
		}

		if (criteria.getLong("clienteByIdClienteRemetente.idCliente") != null) {
			sqlTemp.addCriteria("cr.id", "=",
					criteria.getLong("clienteByIdClienteRemetente.idCliente"));
		}

		if (criteria.getString("tpDestinatarioMda") != null
				&& !"".equals(criteria.getString("tpDestinatarioMda"))) {
			sqlTemp.addCriteria("mda.tpDestinatarioMda", "=",
					criteria.getString("tpDestinatarioMda"));
		}

		if (criteria.getLong("filialByIdFilialDestino.idFilial") != null) {
			sqlTemp.addCriteria("fd.id", "=",
					criteria.getLong("filialByIdFilialDestino.idFilial"));
		}

		if (criteria.getLong("clienteByIdClienteDestinatario.idCliente") != null) {
			sqlTemp.addCriteria("cd.id", "=", criteria
					.getLong("clienteByIdClienteDestinatario.idCliente"));
		}

		if (criteria.getLong("clienteByIdClienteConsignatario.idCliente") != null) {
			sqlTemp.addCriteria("cc.id", "=", criteria
					.getLong("clienteByIdClienteConsignatario.idCliente"));
		}

		if (criteria.getLong("setor") != null) {
			sqlTemp.addCriteria("s.id", "=", criteria.getLong("setor"));
		}

		sqlTemp.addCriteria("fo.empresa.id", "=", SessionUtils.getFilialSessao()
				.getEmpresa().getIdEmpresa());

		sqlTemp.addCriteria("fd.empresa.id", "=", SessionUtils.getFilialSessao()
				.getEmpresa().getIdEmpresa());

		return sqlTemp;
	}

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
	 * @param TypedFlatMap
	 *            criteria
	 * @param FindDefinition
	 *            findDefinition
     * @return
	 */
	public ResultSetPage findPaginatedMda(TypedFlatMap criteria,
			FindDefinition findDefinition) {
		SqlTemplate sqlTemp = mountSqlTemp(criteria);

		sqlTemp.addProjection("mda");

		sqlTemp.addOrderBy("mda.dhEmissao.value");
		sqlTemp.addOrderBy("mda.tpDocumentoServico");
		sqlTemp.addOrderBy("mda.nrDoctoServico");

		return getAdsmHibernateTemplate().findPaginated(sqlTemp.getSql(true),
				findDefinition.getCurrentPage(), findDefinition.getPageSize(),
				sqlTemp.getCriteria());
    }

    /**
	 * Faz a consulta ao banco, retornando o numero de registros encontrados
     * para determinados parametros.
     * 
	 * @param TypedFlatMap
	 *            criteria
	 * @param FindDefinition
	 *            findDefinition
     * @return
	 */
	public Integer getRowCountMda(TypedFlatMap criteria,
			FindDefinition findDefinition) {
		SqlTemplate sqlTemp = mountSqlTemp(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(
				sqlTemp.getSql(false), sqlTemp.getCriteria());
	}

    /**
	 * Método que retorna um list com um objeto de MDA usando como filtro o
	 * nrDoctoServico e o idFilialOrigem.
     * 
	 * @param Long
	 *            nrDoctoServico
	 * @param Long
	 *            idFilialOrigem
     * @return List
     */
	public List findMdaByNrDoctoServicoByIdFilialOrigem(Long nrDoctoServico,
			Long idFilialOrigem) {
		SqlTemplate sqlTemp = new SqlTemplate();

		sqlTemp.addFrom(Mda.class.getName() + " mda join fetch mda.moeda moe"
				+ "	join fetch mda.filialByIdFilialOrigem fo "
				+ " join fetch fo.pessoa fop "
				+ " left join fetch mda.filialByIdFilialDestino fd "
				+ " left join fetch fd.pessoa fdp "
				+ " left join fetch mda.clienteByIdClienteRemetente cr "
				+ " left join fetch cr.pessoa crp "
				+ " left join fetch mda.clienteByIdClienteDestinatario cd "
				+ " left join fetch cd.pessoa cdp "
				+ " left join fetch mda.clienteByIdClienteConsignatario cc "
				+ " left join fetch mda.usuarioByIdUsuarioRecebidoPor urp "
				+ " left join fetch cc.pessoa ccp ");

		sqlTemp.addCriteria("mda.nrDoctoServico", "=", nrDoctoServico);
		sqlTemp.addCriteria("fo.id", "=", idFilialOrigem);

		return getAdsmHibernateTemplate().find(sqlTemp.getSql(),
				sqlTemp.getCriteria());
    }

    public List findSomaQtdVolumesItens(Long idDoctoServico) {
		DetachedCriteria detachedCriteria = DetachedCriteria
				.forClass(ItemMda.class, "itemMda")
		.setProjection(
						Projections
								.projectionList()
								.add(Projections.sum("itemMda.qtVolumes"),
										"sumQtVolumes")
								.add(Projections.sum("itemMda.psItem"),
										"sumPsItem"))
				.createAlias("itemMda.mda", "mda")
	 	.add(Restrictions.eq("mda.id", idDoctoServico))
	 	.setResultTransformer(new AliasToNestedMapResultTransformer());
    	return super.findByDetachedCriteria(detachedCriteria);
    }

	public List findByNrMdaByFilialOrigem(Long nrMda, Long idFilial) {
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("nrDoctoServico", nrMda));
    	dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial", idFilial));

    	return findByDetachedCriteria(dc);
    }

	public List findDocumentosServico(Long idMda) {
    	DetachedCriteria dc = createDetachedCriteria();

    	dc.add(Restrictions.eq("idDoctoServico", idMda));
    	dc.setProjection(Projections.property("idDoctoServico"));

		return findByDetachedCriteria(dc);
    }

	/**
	 * Busca os documentos de servico (Mdas - MDA) a partir do id do Manifesto
	 * de viagem nacional.
	 */
	public List findMdasByIdManifestoViagemNacional(Long idManifesto) {
		StringBuffer sql = new StringBuffer();
	   	sql.append("select mda from Mda as mda, ");
	   	sql.append("Manifesto as manifesto, ");
		sql.append("PreManifestoDocumento as preManifestoDocumento ");
	   	sql.append("where manifesto.id = preManifestoDocumento.manifesto.id ");
	   	sql.append("and preManifestoDocumento.id = mda.id ");
	   	sql.append("and mda.tpDocumentoServico = 'MDA' ");
	   	sql.append("and manifesto.id = :idManifesto");
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),
				"idManifesto", idManifesto);
	}

	/**
	 * Busca os documentos de servico (Mdas - MDA) a partir do id do Manifesto
	 * de entrega.
	 */
	public List findMdasByIdManifestoEntrega(Long idManifesto) {
		String sql = "select mda from Mda as mda, "
				+ "Manifesto as manifesto, "
				+ "ManifestoEntrega as manifestoEntrega, "
				+ "ManifestoEntregaDocumento as manifestoEntregaDocumento "
				+ "where manifesto.id = manifestoEntrega.id "
				+ "and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id "
				+ "and manifestoEntregaDocumento.doctoServico.id = mda.id "
				+ "and mda.tpDocumentoServico = 'MDA' "
				+ "and manifesto.id = :idManifesto";
		return getAdsmHibernateTemplate().findByNamedParam(sql, "idManifesto",
				idManifesto);
	}

	public boolean findDocumentoCancelado(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		Short cdLocalizacaoMercadoria = ConstantesSim.CD_MERCADORIA_CANCELADA;
		Boolean eventoCancelado = Boolean.FALSE;
		Long nrDoctoServico = criteria.getLong("nrDoctoServico");
		Long idFilialOrigem = criteria.getLong("idFilialOrigem");
		
		hql.append("SELECT doc FROM DOCTO_SERVICO doc");
		hql.append("  JOIN LOCALIZACAO_MERCADORIA lm on (lm.ID_LOCALIZACAO_MERCADORIA = doc.ID_LOCALIZACAO_MERCADORIA)");
		hql.append("  JOIN EVENTO_DOCUMENTO_SERVICO eds on (eds.ID_DOCTO_SERVICO = doc.ID_DOCTO_SERVICO)");
		hql.append("  JOIN FILIAL fo on (fo.ID_FILIAL = doc.ID_FILIAL_ORIGEM)");
		hql.append(" WHERE doc.NR_DOCTO_SERVICO = ? ");		
		hql.append("   AND fo.ID_FILIAL = ? ");
		hql.append("   AND lm.CD_LOCALIZACAO_MERCADORIA = ? ");
		hql.append("   AND eds.BL_EVENTO_CANCELADO = ? ");
		
		return getAdsmHibernateTemplate().getRowCountBySql(hql.toString(), 
				new Object[]{nrDoctoServico, 
							 idFilialOrigem, 
							 cdLocalizacaoMercadoria, 
							 eventoCancelado}) > 0 ;
		
	}
}