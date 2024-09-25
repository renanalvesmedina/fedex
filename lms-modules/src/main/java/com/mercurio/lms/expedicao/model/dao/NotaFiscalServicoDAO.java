package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NotaFiscalServicoDAO extends BaseCrudDao<NotaFiscalServico, Long> {

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {		
		lazyFindLookup.put("filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialOrigem.empresa", FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NotaFiscalServico.class;
    }

    /**
     * Busca o nrNotaFiscalServico pelo id informado.
     * 
     * @param idNotaFiscalServico
     * @return nrNotaFiscalServico where idNotaFiscalServico = :idNotaFiscalServico
     */
    public Long findNrNotaFiscalServicoById(Long idNotaFiscalServico){
    	if (idNotaFiscalServico == null) return null;

    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(getPersistentClass().getName(), " nfs ");
    	sql.addProjection("nfs.nrNotaFiscalServico");
    	sql.addCriteria("nfs.id","=",idNotaFiscalServico);

    	List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	if (list.size() == 1){
    		return (Long) list.get(0);
    	}
    	return null;
    }

    public ResultSetPage findPaginatedDevedorDocServFat(Map map, FindDefinition findDef){
    	SqlTemplate sql = getSqlNotaFiscalServicoByDevedorDocServFat(map);
    	sql.addProjection( new StringBuffer()
    			.append(" new Map(cto.nrNotaFiscalServico as nrDocumento, doc.filial.idFilial as filial_idFilial, ")
    			.append(" cto.filial.sgFilial as filial_sgFilial, doc.cliente.pessoa.nmPessoa as cliente_nmCliente, ")
    			.append(" doc.vlDevido as vlDevido, des.vlDesconto as vlDesconto, doc.tpSituacaoCobranca as tpSituacaoCobranca ")
    			.append(" ) ")
    			.toString()
    		);
    	ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
    			sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
    	List list = rsp.getList();
    	list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
    	rsp.setList(list);
    	return rsp;
    }
    public Integer getRowCountDevedorDocServFat(Map criteria) {
    	SqlTemplate sql = getSqlNotaFiscalServicoByDevedorDocServFat(criteria);
    	sql.addProjection("count(cto.id)");
    	Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    	return result.intValue();
    }

    private SqlTemplate getSqlNotaFiscalServicoByDevedorDocServFat(Map criteria){
    	TypedFlatMap map = (TypedFlatMap)criteria;
    	SqlTemplate sql = new SqlTemplate();
    	sql.addFrom(NotaFiscalServico.class.getName() + " cto join cto.devedorDocServFats as doc join doc.descontos as des ");
    	
    	String tpDocumentoServico = map.getString("tpDocumentoServico");
    	if (StringUtils.isNotEmpty(tpDocumentoServico)){
    		sql.addCriteria("cto.tpDocumentoServico", "=", tpDocumentoServico);
    	}
    	Long idCliente = map.getLong("cliente.idCliente");
    	if (idCliente != null) {
    		sql.addCriteria("","=",idCliente);
    	}
    	Long idFilial = map.getLong("filial.idFilial");
    	if (idFilial != null) {
    		sql.addCriteria("cto.filial.id","=",idFilial);	
    	}
    	String tpSituacaoAprovacao = map.getString("desconto.tpSituacaoAprovacao");
    	if (StringUtils.isNotEmpty(tpSituacaoAprovacao)) {
    		sql.addCriteria("des.tpSituacaoAprovacao","=",tpSituacaoAprovacao);
    	}
    	String nrNotaFiscalServico = map.getString("nrDocumento");
    	if (StringUtils.isNotEmpty(nrNotaFiscalServico)) {
    		sql.addCriteria("cto.nrNotaFiscalServico", "=", LongUtils.getLong(nrNotaFiscalServico));
    	}
    	return sql;
    }

    public List findNotaFiscalServicoDevedorDocServFat(Map map){
    	SqlTemplate sql = getSqlNotaFiscalServicoByDevedorDocServFat(map);
    	sql.addProjection("new map(cto.id as idDocumento, cto.nrNotaFiscalServico as nrDocumento, " +
			"cto.filial.sgFilial as filial_sgFilial, cto.filial.idFilial as filial_idFilial, cto.vlTotalDocServico as vlDocumento)");
    	List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	if (!list.isEmpty()){
    		list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
    	}
    	return list;
    }

    public List findByNrnotaFiscalByFilial(Long nrNotaFiscal, Long idFilial){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("nrNotaFiscalServico", nrNotaFiscal));
    	dc.add(Restrictions.eq("filial.idFilial", idFilial));
    	return findByDetachedCriteria(dc);
    }

    public List findDocumentosServico(Long idNotaFiscalServico){
    	DetachedCriteria dc = createDetachedCriteria();    	    	
    	dc.add(Restrictions.eq("idDoctoServico", idNotaFiscalServico));
    	dc.setProjection(Projections.property("idDoctoServico"));
    	return findByDetachedCriteria(dc);	
    }

    /**
     * RowCount for findPaginatedNFS.
     * @param criteria
     * @return
     */
    public Integer getRowCountNFS(TypedFlatMap criteria) {
    	SqlTemplate sql = this.getSqlTemplatePaginatedNFS(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }

    /**
     * findPaginatedNFS.
     * @param criteria
     * @param findDef
     * @return
     */
    public ResultSetPage findPaginatedNFS(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = this.getSqlTemplatePaginatedNFS(criteria);

		sql.addProjection("new map(" +
				"nfs.id as idNotaFiscalServico, " +
				"nfs.id as idDoctoServico, " +
				"nfs.tpDocumentoServico as tpDocumentoServico, " +
				"fo.id as idFilialOrigem, " +
				"fo.sgFilial as sgFilialOrigem, " +
				"fo.sgFilial as filialByIdFilialOrigem_sgFilial, "+
				"fo.id as filialByIdFilialOrigem_idFilial, "+
				"pfo.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, " +								
				"fd.sgFilial as sgFilialDestino, "+
				"pcd.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, "+
				"pcd.idPessoa as filialByIdFilialDestino_pessoa_idPessoa, "+
				"pcd.nmPessoa as filialByIdFilialDestino_pessoa_nmPessoa, "+
				"pfo.nmFantasia as nmFantasiaFilial, " +
				"fo.id as filialByIdFilialOrigem_idFilial, " +
				"fo.sgFilial as filialByIdFilialOrigem_sgFilial, " +
				"nfs.nrNotaFiscalServico as nrNotaFiscalServico, " +
				"nfs.nrNotaFiscalServico as nrDoctoServico, " +
				"ds.dhEmissao as dhEmissao, " +
				"pcd.nmPessoa as nmPessoaCliente, " +
				"sa.idServicoAdicional as idServicoAdicional, " +
				"ds.vlTotalDocServico as valor");
		
		//Para funcionar com a lookup de documento de serviço
		sql.addProjection("fo.idFilial", "filial_idFilial");
		sql.addProjection("fo.sgFilial", "filial_sgFilial");
		sql.addProjection("pfo.nmFantasia", "filial_pessoa_nmFantasia)");				

		sql.addOrderBy("pfo.nmFantasia");
		sql.addOrderBy("nfs.nrNotaFiscalServico");
		
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));

		return rsp;
    }

    /**
     * SQL for findPaginatedNFS.
     * @param criteria
     * @return
     */
    private SqlTemplate getSqlTemplatePaginatedNFS(TypedFlatMap criteria) {

		/** FROM */
		StringBuilder strFrom = new StringBuilder();
		strFrom.append(ServAdicionalDocServ.class.getName()+" as sads");
		strFrom.append(" left join sads.doctoServico as ds ");
		strFrom.append(" left join ds.filialByIdFilialOrigem as fo ");
		strFrom.append(" left join ds.filialByIdFilialDestino as fd ");
		strFrom.append(" left join fo.pessoa as pfo ");
		strFrom.append(" left join sads.servicoAdicional as sa ");
		strFrom.append(" left join ds.clienteByIdClienteDestinatario as cd ");
		strFrom.append(" left join cd.pessoa as pcd, ");
		strFrom.append(NotaFiscalServico.class.getName()).append(" as nfs ");
		strFrom.append(" left join nfs.municipio as m ");

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(strFrom.toString());

		/** WHERE */
		sql.addCriteria("fo.id", "=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("nfs.nrNotaFiscalServico", "=", criteria.getLong("nrNotaFiscalServico"));

		YearMonthDay dtInicial = criteria.getYearMonthDay("dtInicial");
		if (dtInicial != null) {
			sql.addCriteria("ds.dhEmissao.value", ">=", dtInicial.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()), DateTime.class);
		}
		YearMonthDay dtFinal = criteria.getYearMonthDay("dtFinal");
		if (dtFinal != null) {
			sql.addCriteria("ds.dhEmissao.value", "<", dtFinal.toDateTimeAtMidnight(JTDateTimeUtils.getUserDtz()).plusDays(1), DateTime.class);
		}
		sql.addCriteria("m.id", "=", criteria.getLong("municipio.idMunicipio"), Long.class);
		sql.addCriteria("cd.id", "=", criteria.getLong("clienteByIdClienteDestinatario.idCliente"), Long.class);
		sql.addCriteria("sa.id", "=", criteria.getLong("servicoAdicional.idServicoAdicional"), Long.class);
		sql.addCustomCriteria("ds.id=nfs.id");
		sql.addCriteria("ds.filialByIdFilialOrigem.id", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"), Long.class);
		sql.addCriteria("nfs.tpNotaFiscalServico", "=", criteria.getString("tpDocumentoServico"));
		return sql;
    }

    public List findNotaFiscalServicoById(Long idNotaFiscalServico){
    	StringBuilder projections = new StringBuilder()
    		.append("new map(")
    		.append("nfs.tpCalculoPreco as tpCalculoPreco, ")
    		.append("cd.idCliente as idCliente, ")
    		.append("pcd.tpPessoa as tpPessoa, ")
    		.append("nfs.id as idNotaFiscalServico, ")
        	.append("nfs.tpSituacaoNf as tpSituacaoNf, ")
        	.append("fo.idFilial as filial_idFilial, ")
			.append("fo.sgFilial as filial_sgFilial, ")
			.append("pfo.nmFantasia as filial_pessoa_nmFantasia, ")
			.append("pfo.nrIdentificacao as filial_pessoa_nrIdentificacao, ")
			.append("nfs.nrNotaFiscalServico as nrNotaFiscalServico, ")
			.append("nfs.dhEmissao as dhEmissao, ")
			.append("nfs.tpDocumentoServico as tpDocumentoServico, ")
			.append("cd.idCliente as clienteByIdClienteDestinatario_idCliente, ")
			.append("pcd.nrIdentificacao as clienteByIdClienteDestinatario_pessoa_nrIdentificacao, ")
			.append("pcd.tpIdentificacao as clienteByIdClienteDestinatario_pessoa_tpIdentificacao, ")
			.append("pcd.nmPessoa as clienteByIdClienteDestinatario_pessoa_nmPessoa, ")
			.append("cd.tpCliente as clienteByIdClienteDestinatario_tpCliente, ")
			.append("m.idMunicipio as municipio_idMunicipio, ")     			
			.append("m.nmMunicipio as municipio_nmMunicipio, ")
			.append("dc.idDivisaoCliente as divisaoCliente_idDivisaoCliente, ")
			.append("dc.dsDivisaoCliente as divisaoCliente_dsDivisaoCliente, ")
	    	.append("s.idServico as servico_idServico, ") 
	    	.append("s.tpAbrangencia as servico_tpAbrangencia, ")
	    	.append("s.tpModal as servico_tpModal, ")
	    	.append("u.nrMatricula as nrIdentificacaoUsuarioLogado, ")
	    	.append("u.nmUsuario as nmPessoaUsuario, ")
	    	.append("u.idUsuario as idUsuarioLogado, ")
	    	.append("ied.idInscricaoEstadual as clienteByIdClienteDestinatario_idInscricaoEstadual, ")
	    	.append("ied.nrInscricaoEstadual as clienteByIdClienteDestinatario_nrInscricaoEstadual, ") 
	    	.append("nfs.dtInicial as dtInicial, ")
	    	.append("nfs.psReferenciaCalculo as servicoAdicional_psMercadoria, ")
	    	.append("nfs.dtFinal as dtFinal, ")
	    	.append("divc.idDivisaoCliente as divisaoCliente_idDivisaoCliente, ")
	    	.append("divc.dsDivisaoCliente as divisaoCliente_dsDivisaoCliente ")
			.append(") ");

    	StringBuilder from = new StringBuilder()
    		.append(NotaFiscalServico.class.getName() + " as nfs ")
			.append("left join nfs.inscricaoEstadualDestinatario ied ") 
			.append("left join nfs.divisaoCliente divc ")
			.append("left join nfs.divisaoCliente dc ")
			.append("left join nfs.filialByIdFilialOrigem as fo ")
			.append("left join fo.pessoa as pfo ")
			.append("left join nfs.clienteByIdClienteDestinatario as cd ")
			.append("left join cd.pessoa as pcd ")
			.append("left join nfs.municipio as m ")
			.append("left join nfs.servico as s ")
			.append("left join nfs.usuarioByIdUsuarioInclusao u ");

		SqlTemplate sql = new SqlTemplate();
		sql.addProjection(projections.toString());
		sql.addFrom(from.toString());

		sql.addCriteria("nfs.id", "=", idNotaFiscalServico, Long.class);

		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }

	/**
	 * Busca os documentos de servico (Notas Fiscais Servico - NFS)
	 * a partir do id do Manifesto.
	 */
    public List findNotasFiscaisServicoByIdManifesto(Long idManifesto){
    	StringBuffer query = new StringBuffer();
    	query.append(" select notaFiscalServico");
    	query.append(" from NotaFiscalServico as notaFiscalServico");
    	query.append("		,Manifesto as manifesto");
    	query.append("		,ManifestoEntrega as manifestoEntrega");
    	query.append("		,ManifestoEntregaDocumento as manifestoEntregaDocumento");
    	query.append(" where manifesto.id = manifestoEntrega.id");
    	query.append("	 and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id");
    	query.append("	 and manifestoEntregaDocumento.doctoServico.id = notaFiscalServico.id");
    	query.append("	 and manifesto.id = :idManifesto");

		return getAdsmHibernateTemplate().findByNamedParam(query.toString(),"idManifesto", idManifesto);
    }
    
	/**
	 * Retorna a situacao da nota fiscal de servico informado
	 * 
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 * 
	 * @param Long idNotaFiscalServico
	 * @return DomainValue
	 */
	public DomainValue findTpSituacaoById(Long idNotaFiscalServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("n.tpSituacaoNf");
		hql.addInnerJoin(NotaFiscalServico.class.getName(), "n");
		hql.addCriteria("n.id", "=", idNotaFiscalServico);

		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()){
			return (DomainValue)list.get(0);
		}
		return null;
	}	    
	
	
	/**
	 * Busca os dados da Nota Fiscal Servico para enviar para enviar para a NDDigital, gerando dessa forma a nota fiscal de serviço
	 * eletronica - NSE (Nota de serviço eletronica)
	 * @param idNotaFiscalServico
	 * @return
	 */
	public NotaFiscalServico findDadosNseToNotaFiscalEletronica(Long idNotaFiscalServico){
		StringBuffer hql = new StringBuffer();
		
		hql.append("select notaFiscalServico ")
			.append("from " + this.getPersistentClass().getName() + " notaFiscalServico ")	
			.append("inner join notaFiscalServico.servAdicionalDocServ servAdicionalDocServ ")
			.append("inner join servAdicionalDocServ.servicoAdicionais servicoAdicionais")
			.append("inner join servicoAdicionais.servicoOficialTributo servicoOficialTributo " )
			.append("inner join notaFiscalServico.municipio municipio ")
			.append("inner join notaFiscalServico.impostoServicos impostoServicos ") 
			.append("inner join notaFiscalServico.devedorDocServs devedorDocServs ")
			.append("inner join devedorDocServs.cliente cliente ")
			.append("where notaFiscalServico.id = :idNotaFiscalServico ");
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idNotaFiscalServico", idNotaFiscalServico);
		
		return (NotaFiscalServico) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
	}

	public List<Map<String, Object>> findByNrNotaFiscalServicoIdFilialOrigem(final Long nrNotaFiscalServico, final Long idFilial, final String tpDocumentoServico) {
		ProjectionList pl = getProjectionListNotaFiscalServico();
		DetachedCriteria dc = getDetachedCriteriaNotaFiscalServico(nrNotaFiscalServico, idFilial, tpDocumentoServico);
		dc.setProjection(pl).setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	private DetachedCriteria getDetachedCriteriaNotaFiscalServico(final Long nrNotaFiscalServico, final Long idFilial, final String tpDocumentoServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "n")
			.createAlias("n.clienteByIdClienteRemetente", "cr")
			.createAlias("n.clienteByIdClienteDestinatario", "cd")
			.createAlias("cr.pessoa", "pr")
			.createAlias("cd.pessoa", "pd")
			.createAlias("n.filialByIdFilialOrigem", "fo")
			.createAlias("fo.pessoa", "pfo")
			.add(Restrictions.eq("n.nrNotaFiscalServico", nrNotaFiscalServico))
			.add(Restrictions.eq("fo.id", idFilial))
			.add(Restrictions.eq("n.tpDocumentoServico", tpDocumentoServico));

		return dc;
	}

	private ProjectionList getProjectionListNotaFiscalServico() {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("n.idDoctoServico"), "idDoctoServico")
		.add(Projections.property("n.nrNotaFiscalServico"), "nrNotaFiscalServico")
		.add(Projections.property("n.nrDoctoServico"), "nrDoctoServico")
		.add(Projections.property("n.dhEmissao.value"), "dhEmissao")
		.add(Projections.property("n.tpDocumentoServico"), "tpDocumentoServico")
		.add(Projections.property("n.psReal"), "psReal")
		.add(Projections.property("fo.idFilial"), "idFilialOrigem")
		.add(Projections.property("fo.sgFilial"), "sgFilialOrigem")
		.add(Projections.property("pfo.nmFantasia"), "nmFilialOrigem")
		.add(Projections.property("n.vlTotalDocServico"), "vlTotalDocServico")
		.add(Projections.property("cr.idCliente"), "remetente_idCliente")
		.add(Projections.property("cr.tpCliente"), "remetente_tpCliente")
		.add(Projections.property("cd.idCliente"), "destinatario_idCliente")
		.add(Projections.property("cd.tpCliente"), "destinatario_tpCliente")
		.add(Projections.property("pr.idPessoa"), "remetente_idPessoa")
		.add(Projections.property("pd.idPessoa"), "destinatario_idPessoa")
		.add(Projections.property("pr.tpPessoa"), "remetente_tpPessoa")
		.add(Projections.property("pd.tpPessoa"), "destinatario_tpPessoa")
		.add(Projections.property("pr.tpIdentificacao"), "remetente_tpIdentificacao")
		.add(Projections.property("pd.tpIdentificacao"), "destinatario_tpIdentificacao")
		.add(Projections.property("pr.nmPessoa"), "remetente_nmPessoa")
		.add(Projections.property("pd.nmPessoa"), "destinatario_nmPessoa")
		.add(Projections.property("pr.nrIdentificacao"), "remetente_nrIdentificacao")
		.add(Projections.property("pd.nrIdentificacao"), "destinatario_nrIdentificacao"); 
	return pl;
	}
	
}