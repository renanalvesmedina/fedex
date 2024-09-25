package com.mercurio.lms.entrega.model.dao;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.entrega.model.ChequeReembolso;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboReembolsoDAO extends BaseCrudDao<ReciboReembolso, Long> {

	public Map<String, Object> findForUpdate(Long id) {
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"rr.idDoctoServico as idDoctoServico, "+
				"rr.obRecolhimento as obRecolhimento, "+
				"rr.nrReciboReembolso as nrReciboReembolso, " +
				"rr.vlReembolso as vlReembolso, " +
				"rr.dhDigitacaoCheque as dhDigitacaoCheque, " +
				"rr.tpSituacaoRecibo as tpSituacaoRecibo, " +
				"rr.dhEmissao as dhEmissaoReciboReembolso, " +
				"filDestOper.sgFilial as sgFilialOper, " +
				"pesFilDestOper.nmFantasia as nmFantasiaOper, " +

				"fil.idFilial as filial_idFilial, " +		
				"fil.sgFilial as filial_sgFilial, " +
				"pesFil.nmFantasia as filial_pessoa_nmFantasia, " +
				"nvl2(moeda.sgMoeda, moeda.sgMoeda || ' ' ||moeda.dsSimbolo,'') as moeda_sgMoeda, " +

				"reembolsado.idDoctoServico as idDoctoServicoReembolsado, " +
				"reembolsado.nrDoctoServico as nrDoctoServico, " +
				"reembolsado.tpDocumentoServico as tpDocumentoServico, " +
				"filReembolsado.sgFilial as sgFilialDoc, " +

				"reembolsado.dhEmissao as dhEmissaoDoc, " +
				"reembolsado.nrDoctoServico as doctoServicoByIdDoctoServReembolsado_nrDoctoServico, " +
				"pesRem.nrIdentificacao as nrIdentificacaoRemetente, " +
				"pesRem.tpIdentificacao as tpIdentificacaoRemetente, " +
				"pesRem.nmPessoa as doctoServicoByIdDoctoServReembolsado_clienteByIdClienteRemetente_pessoa_nmPessoa, " +
				"pesDest.nrIdentificacao as nrIdentificacaoDestinatario, " +
				"pesDest.tpIdentificacao as tpIdentificacaoDestinatario, " +
				"pesDest.nmPessoa as doctoServicoByIdDoctoServReembolsado_clienteByIdClienteDestinatario_pessoa_nmPessoa)");

		hql.addFrom("ReciboReembolso rr " +
				"join rr.filial fil " +
				"join fil.pessoa pesFil " +
				"join rr.moeda moeda " +
				"join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"join reembolsado.filialByIdFilialOrigem filReembolsado " +

				"join rr.doctoServicoByIdDoctoServReembolsado reembolsado " +
				"join reembolsado.clienteByIdClienteRemetente reeRem " +
				"join reeRem.pessoa pesRem " +
				"left outer join reembolsado.clienteByIdClienteDestinatario reeDest " +
				"left outer join reeDest.pessoa pesDest " +
				"left outer join rr.filialDestinoOperacional filDestOper " +
				"left outer join filDestOper.pessoa pesFilDestOper " );

		hql.addCriteria("rr.idDoctoServico","=",id);

		List<Map<String, Object>> lista = getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
		return (Map<String, Object>)AliasToNestedMapResultTransformer.getInstance().transformListResult(lista).get(0);
	}

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ReciboReembolso.class;
	}

	/**
	 * Busca os documentos de servico (Recibos Reembolso - RRE)
	 * a partir do id do Manifesto de entrega.
	 */
	public List<ReciboReembolso> findRecibosReembolsoByIdManifestoEntrega(Long idManifesto){
		String sql = "select reciboReembolso from ReciboReembolso as reciboReembolso, "+
			"Manifesto as manifesto, "+
			"ManifestoEntrega as manifestoEntrega, "+
			"ManifestoEntregaDocumento as manifestoEntregaDocumento "+
			"where manifesto.id = manifestoEntrega.id "+
			"and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id "+
			"and manifestoEntregaDocumento.doctoServico.id = reciboReembolso.id "+
			"and reciboReembolso.tpDocumentoServico = 'RRE' "+
			"and manifesto.id = :idManifesto";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idManifesto", idManifesto);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate hql = montaQueryPaginated(criteria);
		ResultSetPage rs =  getAdsmHibernateTemplate().findPaginated(hql.getSql(),findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
		List<Map<String, Object>> result = rs.getList();
		for(Map<String, Object> map : result) {
			DateTime dataEmissao = (DateTime)map.get("dhEmissao");
			if(map.get("nrIdentificacaoRemetente")!= null)
				map.put("nrIdentificacaoRemetente",FormatUtils.formatIdentificacao((DomainValue)map.get("tpIdentificacaoRemetente"),map.get("nrIdentificacaoRemetente").toString()));
			if(map.get("nrIdentificacaoDestinatario")!= null)
				map.put("nrIdentificacaoDestinatario",FormatUtils.formatIdentificacao((DomainValue)map.get("tpIdentificacaoDestinatario"),map.get("nrIdentificacaoDestinatario").toString()));
			if (dataEmissao != null)
				map.put("dhEmissao",JTFormatUtils.format(dataEmissao, JTFormatUtils.CUSTOM, JTFormatUtils.YEARMONTHDAY));
		}
		return rs;
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate hql = montaQueryPaginated(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}  

	public SqlTemplate montaQueryPaginated(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();

		StringBuffer projecao = new StringBuffer("new Map(")
		.append("rotaSugerida.nrRota as nrRota, ")
		.append("rr.idDoctoServico as idDoctoServico, ")
		.append("rr.nrReciboReembolso as nrDoctoServico, ")
		.append("rr.nrReciboReembolso as nrReciboReembolso, ")
		.append("rr.dhEmissao as dhEmissao, ")
		.append("rr.tpSituacaoRecibo as tpSituacaoRecibo, ")
		.append("filOrigem.sgFilial as filialByIdFilialOrigem_sgFilial, ")
		.append("filOrigem.idFilial as filialByIdFilialOrigem_idFilial, ")
		.append("pesFilOr.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, ")
		.append("filDestino.sgFilial as filialByIdFilialDestino_sgFilial, ")
		.append("filDestino.idFilial as filialByIdFilialDestino_idFilial, ")
		.append("pesFilDes.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, ")
		.append("reembolsado.nrDoctoServico as nrDoctoServicoRembolsado, ")
		.append("reembolsado.idDoctoServico as idDoctoServicoReembolsado, ")
		.append("reembolsado.tpDocumentoServico as tpDocumentoServico, " )
		.append("filReembolsado.sgFilial as sgFilialDoc, " )

		.append("rem.idCliente as clienteByIdClienteRemetente_idCliente, " )
		.append("rem.nrConta as clienteByIdClienteRemetente_nrConta, " )
		.append("pesRem.nmPessoa as clienteByIdClienteRemetente_pessoa_nmPessoa, " )
		.append("pesRem.nmFantasia as clienteByIdClienteRemetente_pessoa_nmFantasia, " )
		.append("pesRem.nrIdentificacao as clienteByIdClienteRemetente_pessoa_nrIdentificacao, ")

		.append("dest.idCliente as clienteByIdClienteDestinatario_idCliente, " )
		.append("dest.nrConta as clienteByIdClienteDestinatario_nrConta, " )
		.append("pesDest.nmPessoa as clienteByIdClienteDestinatario_pessoa_nmPessoa, " )
		.append("pesDest.nmFantasia as clienteByIdClienteDestinatario_pessoa_nmFantasia, " )
		.append("pesDest.nrIdentificacao as clienteByIdClienteDestinatario_pessoa_nrIdentificacao, " )

		.append("pesRem.tpIdentificacao as tpIdentificacaoRemetente, " )
		.append("pesRem.nrIdentificacao as nrIdentificacaoRemetente, " )
		.append("pesRem.nmPessoa as nmPessoaRemetente, " )
		.append("pesDest.tpIdentificacao as tpIdentificacaoDestinatario, ") 
		.append("pesDest.nrIdentificacao as nrIdentificacaoDestinatario, " )
		.append("pesDest.nmPessoa as nmPessoaDestinatario, ")

		.append("filDestOper.sgFilial as sgFilialOper, " )
		.append("pesFilDestOper.nmFantasia as nmPessoaDestFilOper)");

		hql.addProjection(projecao.toString());

		StringBuffer from = new StringBuffer("ReciboReembolso rr ")
		.append("left outer join rr.rotaColetaEntregaByIdRotaColetaEntregaSugerid rotaSugerida ")
		.append("join rr.doctoServicoByIdDoctoServReembolsado reembolsado ")
		.append("join reembolsado.filialByIdFilialOrigem filReembolsado ")
		.append("join rr.filialByIdFilialOrigem filOrigem ")
		.append("join filOrigem.pessoa pesFilOr ")
		.append("left outer join rr.filialByIdFilialDestino filDestino ")
		.append("left outer join rr.filialDestinoOperacional filDestOper ")
		.append("left outer join filDestOper.pessoa pesFilDestOper ")
		.append("left outer join filDestino.pessoa pesFilDes  ")
		.append("left outer join rr.clienteByIdClienteRemetente rem ")
		.append("left outer join rem.pessoa pesRem ")
		.append("left outer join rr.clienteByIdClienteDestinatario dest ")
		.append("left outer join dest.pessoa pesDest ");

		hql.addCriteria("rr.clienteByIdClienteConsignatario.id","=",criteria.getLong("clienteByIdClienteConsignatario.idCliente"));

		hql.addFrom(from.toString());

		hql.addCriteria("filDestOper.idFilial", "=" ,criteria.getLong("filialDestino.idFilial"));
		hql.addCriteria("filOrigem.idFilial", "=" ,criteria.getLong("filialByIdFilialOrigem.idFilial"));

		hql.addCriteria("rr.nrReciboReembolso", "=" ,criteria.getInteger("nrReciboReembolso"));
		if(criteria.getLong("idDoctoReembolso")!= null)
			hql.addCriteria("reembolsado.idDoctoServico", "=" ,criteria.getLong("idDoctoReembolso"));
		else{
			hql.addCriteria("reembolsado.tpDocumentoServico", "=" ,criteria.getString("doctoServicoByIdDoctoServReembolsado.tpDocumentoServico"));
			hql.addCriteria("filReembolsado.idFilial", "=" ,criteria.getLong("doctoServicoByIdDoctoServReembolsado.filialByIdFilialOrigem.idFilial"));
		}
		hql.addCriteria("rem.idCliente", "=" ,criteria.getLong("clienteByIdClienteRemetente.idCliente"));

		hql.addCriteria("dest.idCliente", "=" ,criteria.getLong("clienteByIdClienteDestinatario.idCliente"));

		if(criteria.getYearMonthDay("periodoEmissaoInicial")!= null)
			hql.addCriteria("rr.dhEmissao.value", ">=" ,JTDateTimeUtils.yearMonthDayToDateTime(criteria.getYearMonthDay("periodoEmissaoInicial")));

		if(criteria.getYearMonthDay("periodoEmissaoFinal")!= null)
			hql.addCriteria("rr.dhEmissao.value", "<=" ,JTDateTimeUtils.createWithMaxTime(criteria.getYearMonthDay("periodoEmissaoFinal")));

		hql.addCriteria("rr.tpSituacaoRecibo", "=" ,criteria.getString("tpSituacaoRecibo"));

		hql.addOrderBy("rr.nrReciboReembolso");
		hql.addOrderBy("filOrigem.sgFilial");
		hql.addOrderBy("reembolsado.nrDoctoServico");
		return hql;
	}

	public Integer getRowCountCheques(TypedFlatMap map) {
		Long idDoctoServico = map.getLong("idDoctoServico");
		SqlTemplate hql = montaQueryCheques(idDoctoServico);
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(false),hql.getCriteria());
	}  

	public List<Map<String, Object>> findCheques(Long idDoctoServico) {
		SqlTemplate hql = montaQueryCheques(idDoctoServico);
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filialByIdFilialOrigem",FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		super.initFindLookupLazyProperties(lazyFindLookup);
	}

	public SqlTemplate montaQueryCheques(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"cheques.nrBanco as nrBanco, " +
				"cheques.nrAgencia as nrAgencia, " +
				"cheques.dvAgencia as dvAgencia, " +
				"cheques.nrCheque as nrCheque, " +
				"cheques.idChequeReembolso as idChequeReembolso, " +
				"cheques.dtCheque as data, " +
				"cheques.vlCheque as valorCheque)");
		hql.addFrom("ReciboReembolso rrem " +
				"join rrem.chequeReembolsos cheques ")	;
		hql.addCriteria("rrem.idDoctoServico", "=", idDoctoServico);

		return hql;
	}

	public List<ChequeReembolso> findChequesById(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("cheques");

		hql.addFrom("ReciboReembolso rr " +
				"left outer join rr.chequeReembolsos cheques ");

		hql.addCriteria("rr.idDoctoServico", "=", idDoctoServico);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public void removeRecibos(ReciboReembolso reciboReembolso){
		getAdsmHibernateTemplate().delete(reciboReembolso);
	}

	public int removeByIds(List<Long> ids) {
		return super.removeByIds(ids);
	}

	public List<ReciboReembolso> findRecibosNaoEmitidosByManifesto(Long idManifestoEntrega, Long idManifestoViagem, Long idConhecimento){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("rr");
		sql.addFrom("ReciboReembolso", "rr");
		sql.addCustomCriteria("rr.dhEmissao.value is null");
		sql.addCustomCriteria("rr.tpSituacaoRecibo = 'GE'");
		sql.addCriteria("rr.manifestoEntrega.id", "=", idManifestoEntrega);
		sql.addCriteria("rr.manifestoViagemNacional.id", "=", idManifestoViagem);
		sql.addCriteria("rr.id", "=", idConhecimento);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	public List<Map<String, Object>> findLookupCustom(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(rr.nrReciboReembolso as nrReciboReembolso, " +
				"rr.idDoctoServico as idDoctoServico, " +
				"rota.nrRota as nrRota, " +
				"rr.dhEntradaSetorEntrega as dhEntradaSetorEntrega, " +
				"empresa.idEmpresa as filialByIdFilialOrigem_empresa_idEmpresa, " +
				"filialOrigem.idFilial as filialByIdFilialOrigem_idFilial, " +
				"filialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial, " +
				"pesFilOrigem.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, " +
				
				"filialDestino.idFilial as filialByIdFilialDestino_idFilial, " +
				"filialDestino.sgFilial as filialByIdFilialDestino_sgFilial, " +
				"pesFilDestino.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, " +
				
				"filialResponsavel.sgFilial as pedidoColeta_filialByIdFilialResponsavel_sgFilial, " +
				"pedidoColeta.nrColeta as pedidoColeta_nrColeta, " +
				
				"clienteRemetente.idCliente as clienteByIdClienteRemetente_idCliente, " +
				"clienteRemetente.nrConta as clienteByIdClienteRemetente_nrConta, " +
				"pesCliRem.nmPessoa as clienteByIdClienteRemetente_pessoa_nmPessoa, " +
				"pesCliRem.nrIdentificacao as clienteByIdClienteRemetente_pessoa_nrIdentificacao, " +
				"pesCliRem.nmFantasia as clienteByIdClienteRemetente_pessoa_nmFantasia, " +
				
				"clienteDestinatario.idCliente as clienteByIdClienteDestinatario_idCliente, " +
				"clienteDestinatario.nrConta as clienteByIdClienteDestinatario_nrConta, " +
				"pesCliDest.nmPessoa as clienteByIdClienteDestinatario_pessoa_nmPessoa, " +
				"pesCliDest.nrIdentificacao as clienteByIdClienteDestinatario_pessoa_nrIdentificacao, " +
				"pesCliDest.nmFantasia as clienteByIdClienteDestinatario_pessoa_nmFantasia, " +
				
				"servico.tpModal as servico_tpModal, " +
				"servico.tpAbrangencia as servico_tpAbrangencia, " +
				"tipoServico.idTipoServico as servico_tipoServico_idTipoServico, " +
				"rr.blBloqueado as blBloqueado)" );
		
		hql.addFrom("ReciboReembolso rr " +
				"left outer join rr.rotaColetaEntregaByIdRotaColetaEntregaSugerid rota " +
				"left outer join rr.servico servico " +
				"left outer join servico.tipoServico tipoServico " +
				"left outer join rr.pedidoColeta pedidoColeta " +
				"left outer join rr.clienteByIdClienteRemetente clienteRemetente " +
				"left outer join clienteRemetente.pessoa pesCliRem " +
				"left outer join rr.clienteByIdClienteDestinatario clienteDestinatario " +
				"left outer join clienteDestinatario.pessoa pesCliDest " +
				"left outer join pedidoColeta.filialByIdFilialResponsavel filialResponsavel " +
				"join rr.filialByIdFilialOrigem filialOrigem " +
				"join filialOrigem.empresa empresa " +
				"join filialOrigem.pessoa pesFilOrigem " +
				"join rr.filialByIdFilialDestino filialDestino " +
				"join filialDestino.pessoa pesFilDestino ");
		
		hql.addCriteria("empresa.tpEmpresa", "=" , criteria.getString("filialByIdFilialOrigem.empresa.tpEmpresa"));
		hql.addCriteria("rr.nrReciboReembolso", "=" , criteria.getInteger("nrReciboReembolso"));
		Boolean blBloqueado = criteria.getBoolean("blBloqueado");
		if(blBloqueado == null) {
			blBloqueado = Boolean.FALSE;
		}
		hql.addCriteria("rr.blBloqueado", "=", blBloqueado);
		hql.addCriteria("servico.tpModal", "=", criteria.getString("servico.tpModal"));
		hql.addCriteria("servico.tpAbrangencia", "=", criteria.getString("servico.tpAbrangencia"));
		hql.addCriteria("tipoServico.idTipoServico", "=", criteria.getLong("servico.tipoServico.idTipoServico"));
		hql.addCriteria("filialOrigem.idFilial", "=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		hql.addCriteria("filialDestino.idFilial", "=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		/*
		 * José Rodrigo Moraes
		 * 31/05/2006
		 * Adicionado filtros por cliente remetente e destinatário
		 */
		hql.addCriteria("clienteRemetente.id","=",criteria.getLong("clienteByIdClienteRemetente.idCliente"));
		hql.addCriteria("clienteDestinatario.id","=",criteria.getLong("clienteByIdClienteDestinatario.idCliente"));
		
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * Busca 'Relacao de Recibos Ativos' pelo 'DoctoServico Informado'
	 * @param idDoctoServico
	 * @return
	 */
	public List<ReciboReembolso> findRecibosAtivos(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
			.append("FROM ").append(ReciboReembolso.class.getName()).append(" as rr ")
			.append("WHERE rr.doctoServicoByIdDoctoServReembolsado.idDoctoServico = ?")
			.append("  AND rr.tpSituacaoRecibo <> ?");

		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idDoctoServico, "CA"});
	}

	/**
	 * Find número de recibo de reembolso pela filial origem
	 * @author Andresa Vargas
	 * 
	 * @param nrReciboReembolso
	 * @param idFilial
	 * @return
	 */
	public List<ReciboReembolso> findByNrReciboReembolsoByFilialOrigem(Long nrReciboReembolso, Long idFilial){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("nrReciboReembolso", Integer.valueOf(nrReciboReembolso.intValue())));
		dc.add(Restrictions.eq("filial.idFilial", idFilial));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	/**
	 * Find documentos de servico
	 * @author Andresa Vargas
	 * 
	 * @param idReciboReembolso
	 * @return
	 */
	public List<ReciboReembolso> findDocumentosServico(Long idReciboReembolso){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("idDoctoServico", idReciboReembolso));
		dc.setProjection(Projections.property("idDoctoServico"));

		return findByDetachedCriteria(dc);	
	}

	public List<Long> findReembolsadoByIdReembolso(Long idReembolso){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("reembolsado.idDoctoServico as idDoctoServicoReembolsado");

		hql.addFrom(ReciboReembolso.class.getName()+" rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado reembolsado ");

		hql.addRequiredCriteria("rr.idDoctoServico","=", idReembolso);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	/**
	 * Grid da tela Consulta Posicao de Reembolsos
	 * @param parametros
	 * @param findDef
	 * @return
	 */
	public ResultSetPage findGridPosicaoReembolso(TypedFlatMap parametros, FindDefinition findDef){
		Object[] sql = montaSqlPosicaoReembolso(parametros, true);

		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {

				sqlQuery.addScalar("idReciboReembolso",Hibernate.LONG);

				Properties propertiesTpDocumentoServico = new Properties();
				propertiesTpDocumentoServico.put("domainName","DM_TIPO_DOCUMENTO_SERVICO");
				sqlQuery.addScalar("tpDocumentoServico",Hibernate.custom(DomainCompositeUserType.class,propertiesTpDocumentoServico));

				sqlQuery.addScalar("sgFilial", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServico", Hibernate.LONG);
				sqlQuery.addScalar("tpDocumentoServicoRR",Hibernate.custom(DomainCompositeUserType.class,propertiesTpDocumentoServico));
				sqlQuery.addScalar("sgFilialRR", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("nrDoctoServicoRR", Hibernate.LONG);
				sqlQuery.addScalar("dhEmissao", Hibernate.custom(JodaTimeDateTimeUserType.class));
				sqlQuery.addScalar("nmFilialOrigem", Hibernate.STRING);
				sqlQuery.addScalar("sgFilialDestino", Hibernate.STRING);
				sqlQuery.addScalar("nmFilialDestino", Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoRemetente", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoRemetente", Hibernate.STRING);
				sqlQuery.addScalar("nmRemetente", Hibernate.STRING);
				sqlQuery.addScalar("tpIdentificacaoDestinatario", Hibernate.STRING);
				sqlQuery.addScalar("nrIdentificacaoDestinatario", Hibernate.STRING);
				sqlQuery.addScalar("nmDestinatario", Hibernate.STRING);

				Properties propertiesTpSituacao = new Properties();
				propertiesTpSituacao.put("domainName","DM_STATUS_POSICAO_REEMBOLSO");
				sqlQuery.addScalar("tpSituacao",Hibernate.custom(DomainCompositeUserType.class,propertiesTpSituacao));   			

				sqlQuery.addScalar("vlReembolso",Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("sgMoeda", Hibernate.STRING);
				sqlQuery.addScalar("dsSimbolo", Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findPaginatedBySql((String)sql[0],findDef.getCurrentPage(), findDef.getPageSize(),(Object[])sql[1],csq);
	}

	/**
	 * Detalhamento da tela Consulta Posicao de Reembolsos
	 * @param parametros
	 * @param findDef
	 * @return
	 */
	public TypedFlatMap findByIdPosicaoReembolso(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(rr.id", "idReciboReembolso");
		sql.addProjection("dsReembolsado.id", "doctoServicoByIdDoctoServReembolsado_idDoctoServico");
		sql.addProjection("filOrigReemb.sgFilial", "doctoServicoByIdDoctoServReembolsado_filialByIdFilialOrigem_sgFilial");			
		sql.addProjection("filOrigReemb.idFilial", "doctoServicoByIdDoctoServReembolsado_filialByIdFilialOrigem_idFilial");			
		sql.addProjection("dsReembolsado.tpDocumentoServico", "doctoServicoByIdDoctoServReembolsado_tpDocumentoServico");
		sql.addProjection("dsReembolsado.nrDoctoServico", "doctoServicoByIdDoctoServReembolsado_nrDoctoServico");		
		sql.addProjection("rr.tpDocumentoServico", "tpDocumentoServico");
		sql.addProjection("rr.nrDoctoServico", "nrDoctoServico");
		sql.addProjection("filOrigRR.sgFilial", "filialByIdFilialOrigem_sgFilial");
		sql.addProjection("filOrigRR.idFilial", "filialByIdFilialOrigem_idFilial");
		sql.addProjection("filOrigRR.sgFilial", "filialByIdFilialOrigem2_sgFilial");
		sql.addProjection("pesFilOrigRR.nmFantasia", "filialByIdFilialOrigem_pessoa_nmFantasia");
		sql.addProjection("filDestRR.sgFilial", "filialByIdFilialDestino_sgFilial");
		sql.addProjection("pesFilDestRR.nmFantasia", "filialByIdFilialDestino_pessoa_nmFantasia");
		sql.addProjection("rr.dhEmissao", "dhEmissao");
		sql.addProjection("m.sgMoeda", "sgMoeda");
		sql.addProjection("m.dsSimbolo", "dsSimbolo");
		sql.addProjection("rr.vlReembolso", "vlReembolso");
		sql.addProjection("pessoaRemetente.tpIdentificacao", "doctoServicoByIdDoctoServReembolsado_clienteByIdClienteRemetente_pessoa_tpIdentificacao");
		sql.addProjection("pessoaRemetente.nrIdentificacao", "doctoServicoByIdDoctoServReembolsado_clienteByIdClienteRemetente_pessoa_nrIdentificacao");
		sql.addProjection("pessoaRemetente.nmPessoa", "doctoServicoByIdDoctoServReembolsado_clienteByIdClienteRemetente_pessoa_nmPessoa");
		sql.addProjection("pessoaDestinatario.tpIdentificacao", "doctoServicoByIdDoctoServReembolsado_clienteByIdClienteDestinatario_pessoa_tpIdentificacao");
		sql.addProjection("pessoaDestinatario.nrIdentificacao", "doctoServicoByIdDoctoServReembolsado_clienteByIdClienteDestinatario_pessoa_nrIdentificacao");
		sql.addProjection("pessoaDestinatario.nmPessoa", "doctoServicoByIdDoctoServReembolsado_clienteByIdClienteDestinatario_pessoa_nmPessoa");
		sql.addProjection("rr.tpSituacaoRecibo", "tpSituacaoRecibo)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");		
		sql.addInnerJoin("rr.doctoServicoByIdDoctoServReembolsado", "dsReembolsado");
		sql.addInnerJoin("dsReembolsado.filialByIdFilialOrigem", "filOrigReemb");		
		sql.addInnerJoin("rr.filialByIdFilialOrigem", "filOrigRR");
		sql.addInnerJoin("filOrigRR.pessoa", "pesFilOrigRR");
		sql.addInnerJoin("rr.filialByIdFilialDestino", "filDestRR");
		sql.addInnerJoin("filDestRR.pessoa", "pesFilDestRR");		
		sql.addInnerJoin("dsReembolsado.clienteByIdClienteRemetente", "remetente");
		sql.addInnerJoin("remetente.pessoa", "pessoaRemetente");		
		sql.addInnerJoin("dsReembolsado.clienteByIdClienteDestinatario", "destinatario");
		sql.addInnerJoin("destinatario.pessoa", "pessoaDestinatario");
		sql.addInnerJoin("rr.moeda", "m");

		sql.addCriteria("rr.id", "=", idReciboReembolso);

		Map<String, Object> result = (Map<String, Object>) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return AliasToTypedFlatMapResultTransformer.getInstance().transformeTupleMap(result);
	}

	/**
	 * RowCount da grid da tela Consulta Posicao de Reembolsos
	 * @param parametros
	 * @return
	 */
	public Integer getRowCountGridPosicaoReembolso(TypedFlatMap parametros){
		Object[] sql = montaSqlPosicaoReembolso(parametros, false);
		return getAdsmHibernateTemplate().getRowCountBySql((String)sql[0], (Object[])sql[1]);
	}

	/**
	 * SQL da grid da tela Consulta Posicao de Reembolsos. Foi utilizado SQL puro para poder descobrir a situacao do recibo (na projecao)
	 *  e utilizar esta situacao como criterio 
	 * @param parametros
	 * @param showProjection
	 * @return
	 */
	private Object[] montaSqlPosicaoReembolso(TypedFlatMap parametros, boolean showProjection){
		SqlTemplate sql = new SqlTemplate();		
		if (showProjection) {
			sql.addProjection("rr.id_recibo_reembolso", "idReciboReembolso");
			sql.addProjection("dsReembolsado.tp_documento_servico", "tpDocumentoServico");		
			sql.addProjection("filOrigemReemb.sg_filial", "sgFilial");
			sql.addProjection("dsReembolsado.nr_docto_servico", "nrDoctoServico");		
			sql.addProjection("ds.tp_documento_servico", "tpDocumentoServicoRR");
			sql.addProjection("filOrigem.sg_filial", "sgFilialRR");
			sql.addProjection("filOrigem.sg_filial", "sgFilialOrigem");
			sql.addProjection("ds.nr_docto_servico", "nrDoctoServicoRR");		
			sql.addProjection("ds.dh_emissao", "dhEmissao");		
			sql.addProjection("filOrigemPes.nm_fantasia", "nmFilialOrigem");		
			sql.addProjection("filDestino.sg_filial", "sgFilialDestino");
			sql.addProjection("filDestinoPes.nm_fantasia", "nmFilialDestino");		
			sql.addProjection("remetentePessoa.tp_identificacao", "tpIdentificacaoRemetente");
			sql.addProjection("remetentePessoa.nr_identificacao", "nrIdentificacaoRemetente");
			sql.addProjection("remetentePessoa.nm_pessoa", "nmRemetente");		
			sql.addProjection("destinatarioPessoa.tp_identificacao", "tpIdentificacaoDestinatario");
			sql.addProjection("destinatarioPessoa.nr_identificacao", "nrIdentificacaoDestinatario");
			sql.addProjection("destinatarioPessoa.nm_pessoa", "nmDestinatario");		
			sql.addProjection("rr.vl_reembolso", "vlReembolso");
			sql.addProjection("m.sg_moeda", "sgMoeda");
			sql.addProjection("m.ds_simbolo", "dsSimbolo");
		}

		StringBuffer existsAGCC = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from manifesto_entrega_documento med, \n")
		.append(" 			  manifesto_entrega me, \n")
		.append("			  manifesto m, \n")
		.append("			  ocorrencia_entrega oe \n")
		.append("		 	where med.id_docto_servico = dsReembolsado.id_docto_servico \n")
		.append("			   and med.id_manifesto_entrega = me.id_manifesto_entrega \n")
		.append("			   and me.id_manifesto_entrega = m.id_manifesto \n")
		.append("			   and med.id_ocorrencia_entrega = oe.id_ocorrencia_entrega \n")
		.append("		 	   and m.tp_status_manifesto <> 'CA' \n")
		.append("		 	   and oe.tp_ocorrencia = 'E') \n");

		StringBuffer existsMMPE = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from manifesto_entrega_documento med, \n")
		.append(" 				 manifesto_entrega me, \n")
		.append("				 manifesto m \n")
		.append("		 	where med.id_docto_servico = dsReembolsado.id_docto_servico \n")
		.append("			   and med.id_manifesto_entrega = me.id_manifesto_entrega \n")
		.append("			   and me.id_manifesto_entrega = m.id_manifesto \n")
		.append("		 	   and m.tp_status_manifesto <> 'CA' \n")
		.append("		 	   and med.id_ocorrencia_entrega is null) \n");		

		StringBuffer existsMEPA = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("			 from documento_mir dm, \n")
		.append("		 	  mir m \n")
		.append("			 where dm.id_recibo_reembolso = rr.id_recibo_reembolso \n")
		.append("			   and dm.id_mir = m.id_mir \n")
		.append("		 	   and m.tp_mir = 'EA' \n")				
		.append("		 	   and m.dh_envio is not null \n")
		.append("		 	   and m.dh_recebimento is null) \n");

		StringBuffer existsMRPA = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("			 from documento_mir dm, \n")
		.append("		 	  mir m \n")
		.append("		 	where dm.id_recibo_reembolso = rr.id_recibo_reembolso \n")
		.append("			   and dm.id_mir = m.id_mir \n")
		.append("		 	   and m.tp_mir = 'EA' \n")
		.append("		 	   and m.dh_envio is not null \n")
		.append("		 	   and m.dh_recebimento is not null) \n");

		StringBuffer existsMDPO = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from documento_mir dm, \n")
		.append("		 	  mir m \n")
		.append("		 	where dm.id_recibo_reembolso = rr.id_recibo_reembolso \n")
		.append("			   and dm.id_mir = m.id_mir \n")
		.append("		 	   and m.tp_mir = 'DO' \n")				
		.append("		 	   and m.dh_envio is not null \n")
		.append("		 	   and m.dh_recebimento is null) \n");

		StringBuffer existsMROR = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from documento_mir dm, \n")
		.append("		 	  mir m \n")
		.append("		 	where dm.id_recibo_reembolso = rr.id_recibo_reembolso \n")
		.append("			   and dm.id_mir = m.id_mir \n")
		.append("		 	   and m.tp_mir = 'DO' \n")				
		.append("		 	   and m.dh_envio is not null \n")
		.append("		 	   and m.dh_recebimento is not null) \n");

		StringBuffer existsMAEN = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from documento_mir dm, \n")
		.append("		 	  mir m \n")
		.append("		 	where dm.id_recibo_reembolso = rr.id_recibo_reembolso \n")
		.append("			   and dm.id_mir = m.id_mir \n")
		.append("		 	   and m.tp_mir = 'AE' \n")				
		.append("		 	   and m.dh_envio is not null \n")
		.append("		 	   and m.dh_recebimento is null) \n");

		StringBuffer existsMREN = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from documento_mir dm, \n")
		.append("		 	  mir m \n")
		.append("		 	where dm.id_recibo_reembolso = rr.id_recibo_reembolso \n")
		.append("			   and dm.id_mir = m.id_mir \n")
		.append("		 	   and m.tp_mir = 'AE' \n")				
		.append("		 	   and m.dh_envio is not null \n")
		.append("		 	   and m.dh_recebimento is not null) \n");

		StringBuffer existsRMEN = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from manifesto_entrega_documento med, \n")
		.append(" 			  manifesto_entrega me, \n")
		.append("			  manifesto m \n")		
		.append("		 	where med.id_docto_servico = ds.id_docto_servico \n")
		.append("			   and med.id_manifesto_entrega = me.id_manifesto_entrega \n")
		.append("			   and me.id_manifesto_entrega = m.id_manifesto \n")
		.append("			   and med.id_ocorrencia_entrega is null \n")
		.append("		 	   and m.tp_status_manifesto <> 'CA') \n");

		StringBuffer existsREEN = new StringBuffer()
		.append("	exists (select 1 \n")
		.append("		 	from manifesto_entrega_documento med, \n")
		.append(" 			  manifesto_entrega me, \n")
		.append("			  manifesto m, \n")
		.append("			  ocorrencia_entrega oe \n")
		.append("		 	where med.id_docto_servico = ds.id_docto_servico \n")
		.append("			   and med.id_manifesto_entrega = me.id_manifesto_entrega \n")
		.append("			   and me.id_manifesto_entrega = m.id_manifesto \n")
		.append("			   and med.id_ocorrencia_entrega = oe.id_ocorrencia_entrega \n")
		.append("		 	   and m.tp_status_manifesto <> 'CA' \n")
		.append("		 	   and oe.tp_ocorrencia = 'E') \n");

		StringBuffer existsMEDE = new StringBuffer()
		.append("	(rr.tp_situacao_recibo = 'CA' and \n")
		.append("	 exists  (select 1 \n")
		.append(" 	 	 	  from conhecimento c, \n")
		.append(" 	 	 	  	   docto_servico ds_ \n")
		.append("  	 	 	  where ds_.id_docto_servico_original = dsReembolsado.id_docto_servico \n")
		.append("        	  		and c.id_conhecimento = ds_.id_docto_servico \n")
		.append("        	  		and c.tp_conhecimento = 'DE')) \n");

		StringBuffer caseSituacao = new StringBuffer()		
		.append("case  when \n")
		//Mercadoria Devolvida
		.append(existsMEDE.toString()).append(" then 'MEDE' \n")

		//Recibo Cancelado
		.append("	  when rr.tp_situacao_recibo = 'CA' then 'RECA' \n")

		//Reembolso Entregue
		.append(" 	  when  \n")
		.append(existsREEN.toString()).append(" then 'REEN' \n")

		//Reembolso Manifestado para Entrega
		.append(" 	  when  \n")
		.append(existsRMEN.toString()).append(" then 'RMEN' \n")	

		//Em MIR recebida na entrega
		.append(" 	  when  \n")
		.append(existsMREN.toString()).append(" then 'MREN' \n")

		//Em MIR do administrativo para entrega
		.append(" 	  when  \n")
		.append(existsMAEN.toString()).append(" then 'MAEN' \n")

		//Em MIR recebida na origem
		.append(" 	  when  \n")
		.append(existsMROR.toString()).append(" then 'MROR' \n")

		//Em MIR do destino para origem
		.append(" 	  when  \n")
		.append(existsMDPO.toString()).append(" then 'MDPO' \n")

		//Em MIR recebida pelo administrativo
		.append(" 	  when  \n")
		.append(existsMRPA.toString()).append(" then 'MRPA' \n")

		//Em MIR da entrega para o administrativo
		.append(" 	  when  \n")
		.append(existsMEPA.toString()).append(" then 'MEPA' \n")

		//Cheques Digitados (Reembolso recebido)
		.append("     when rr.tp_situacao_recibo in ('CD') \n")
		.append(" then 'CHDI' \n")

		//Mercadoria Manifestada para Entrega
		.append(" 	  when  \n")
		.append(existsMMPE.toString()).append(" then 'MMPE' \n")

		//Aguardando Confirmação dos Cheques
		.append(" 	  when  \n")
		.append(existsAGCC.toString()).append(" then 'AGCC' end\n");

		
		sql.addProjection(caseSituacao.toString(), "tpSituacao");		

		sql.addFrom("recibo_reembolso", "rr");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("filial", "filOrigem");
		sql.addFrom("pessoa", "filOrigemPes");
		sql.addFrom("filial", "filDestino");
		sql.addFrom("pessoa", "filDestinoPes");
		sql.addFrom("docto_servico", "dsReembolsado");
		sql.addFrom("filial", "filOrigemReemb");
		sql.addFrom("cliente", "remetente");
		sql.addFrom("pessoa", "remetentePessoa");
		sql.addFrom("cliente", "destinatario");
		sql.addFrom("pessoa", "destinatarioPessoa");
		sql.addFrom("moeda", "m");

		sql.addJoin("rr.id_recibo_reembolso", "ds.id_docto_servico");
		sql.addJoin("ds.id_filial_origem", "filOrigem.id_filial");
		sql.addJoin("filOrigem.id_filial", "filOrigemPes.id_pessoa");
		sql.addJoin("ds.id_filial_destino", "filDestino.id_filial");		
		sql.addJoin("filDestino.id_filial", "filDestinoPes.id_pessoa");				
		sql.addJoin("rr.id_docto_serv_reembolsado", "dsReembolsado.id_docto_servico");		
		sql.addJoin("dsReembolsado.id_filial_origem", "filOrigemReemb.id_filial");		
		sql.addJoin("dsReembolsado.id_cliente_remetente", "remetente.id_cliente");		
		sql.addJoin("remetente.id_cliente", "remetentePessoa.id_pessoa");		
		sql.addJoin("dsReembolsado.id_cliente_destinatario", "destinatario.id_cliente");
		sql.addJoin("destinatario.id_cliente", "destinatarioPessoa.id_pessoa");
		sql.addJoin("ds.id_moeda", "m.id_moeda");

		sql.addCriteria("filOrigem.id_filial", "=", parametros.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("filDestino.id_filial", "=", parametros.getLong("filialByIdFilialDestino.idFilial"));		
		sql.addCriteria("remetente.id_cliente", "=", parametros.getLong("clienteByIdClienteRemetente.idCliente"));
		sql.addCriteria("destinatario.id_cliente", "=", parametros.getLong("clienteByIdClienteDestinatario.idCliente"));		
		sql.addCriteria("dsReembolsado.id_docto_servico", "=", parametros.getLong("idDoctoServico"));		
		sql.addCriteria("dsReembolsado.tp_documento_servico", "=", parametros.getString("doctoServico.tpDocumentoServico"));
		sql.addCriteria("dsReembolsado.id_filial_origem", "=", parametros.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("rr.id_recibo_reembolso", "=", parametros.getLong("reciboReembolso.idDoctoServico"));				
		sql.addCriteria("ds.id_filial_origem", "=", parametros.getLong("reciboReembolso.filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("trunc(cast(ds.dh_emissao as DATE))", ">=", parametros.getYearMonthDay("dtEmissaoInicial"));
		sql.addCriteria("trunc(cast(ds.dh_emissao as DATE))", "<=", parametros.getYearMonthDay("dtEmissaoFinal"));

		sql.addOrderBy("filOrigem.sg_filial");		
		sql.addOrderBy("rr.nr_recibo_reembolso");		

		StringBuffer superQuery = new StringBuffer();
		if (showProjection)
			superQuery.append("SELECT *  \n");

		superQuery.append("FROM ( \n")
				  .append(sql.getSql())
				  .append(") \n");

		Object[] criteria = sql.getCriteria();

		if (!StringUtils.isBlank(parametros.getString("tpSituacao"))) {
			superQuery.append(" where tpSituacao = ? \n");
			Object[] tmp = new Object[criteria.length + 1];
			System.arraycopy(criteria, 0, tmp, 0, criteria.length);
			criteria = tmp;
			criteria[criteria.length - 1] = parametros.getString("tpSituacao");			
		}			

		return new Object[]{superQuery.toString(), criteria};
	}

	/**
	 * Consulta posicao do recibo no evento 'Mercadoria no manifesto de entrega'
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReciboMercadoriaManifestoEntrega(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(m.dhEmissaoManifesto", "dhEmissaoManifesto");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("me.nrManifestoEntrega", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");
		sql.addInnerJoin("rr.doctoServicoByIdDoctoServReembolsado", "dsReembolsado");
		sql.addInnerJoin("dsReembolsado.manifestoEntregaDocumentos", "med");		
		sql.addInnerJoin("med.manifestoEntrega", "me");
		sql.addInnerJoin("me.filial", "f");
		sql.addInnerJoin("me.manifesto", "m");

		sql.addCustomCriteria("m.tpStatusManifesto <> 'CA'");
		sql.addCriteria("rr.id", "=", idReciboReembolso);
		sql.addOrderBy("m.dhEmissaoManifesto.value asc");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());		
	}

	/**
	 * Consulta posicao do recibo no evento 'Recebimento do reembolso no destinatario'
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReciboRecebimento(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(rr.dhDigitacaoCheque", "dhFechamento");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("rr.nrReciboReembolso", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");
		sql.addInnerJoin("rr.filial", "f");

		//sql.addCustomCriteria("med.tpSituacaoDocumento = 'FECH'");
		sql.addCriteria("rr.id", "=", idReciboReembolso);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Consulta posicao do recibo no evento 'Aguardando Confirmação dos Cheques'
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReciboAguardaCheques(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(med.dhOcorrencia", "dhBaixa");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("me.nrManifestoEntrega", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");
		sql.addInnerJoin("rr.doctoServicoByIdDoctoServReembolsado", "dsReembolsado");
		sql.addInnerJoin("dsReembolsado.manifestoEntregaDocumentos", "med");
		sql.addInnerJoin("med.manifestoEntrega", "me");		
		sql.addInnerJoin("me.filial", "f");
		sql.addInnerJoin("med.ocorrenciaEntrega", "oe");

		sql.addCustomCriteria("oe.tpOcorrencia = 'E'");
		sql.addCustomCriteria("med.tpSituacaoDocumento in ('FECH','PCOM', 'PREC', 'PRCO')");
		sql.addCriteria("rr.id", "=", idReciboReembolso);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Consulta posicao do recibo nos eventos relativos ao MIR
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReciboMIR(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(mir.dhEnvio", "dhEnvio");
		sql.addProjection("mir.dhRecebimento", "dhRecebimento");
		sql.addProjection("f.sgFilial", "sgFilial");		
		sql.addProjection("mir.tpMir", "tpMir");
		sql.addProjection("mir.nrMir", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");
		sql.addInnerJoin("rr.documentoMirs", "dm");
		sql.addInnerJoin("dm.mir", "mir");
		sql.addInnerJoin("mir.filialByIdFilialOrigem", "f");

		sql.addCustomCriteria("mir.tpMir in ('EA', 'DO', 'AE')");
		sql.addCriteria("rr.id", "=", idReciboReembolso);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Consulta posicao do recibo no evento 'Reembolso no Manifesto de Entrega'
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReciboManifestoEntrega(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(m.dhEmissaoManifesto", "dhEmissaoManifesto");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("me.nrManifestoEntrega", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");		
		sql.addInnerJoin("rr.manifestoEntregaDocumentos", "med");
		sql.addInnerJoin("med.manifestoEntrega", "me");
		sql.addInnerJoin("me.filial", "f");
		sql.addInnerJoin("me.manifesto", "m");
		sql.addLeftOuterJoin("med.ocorrenciaEntrega", "oe");

		sql.addCustomCriteria("(med.ocorrenciaEntrega.id is null or oe.tpOcorrencia = 'E')");
		sql.addCustomCriteria("m.tpStatusManifesto <> 'CA'");
		sql.addCriteria("rr.id", "=", idReciboReembolso);
		sql.addOrderBy("m.dhEmissaoManifesto.value asc");

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());		
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Entrega do reembolso ao cliente'
	 * 
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReciboEntregaCliente(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(med.dhOcorrencia", "dhBaixa");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("rr.nrReciboReembolso", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");		
		sql.addInnerJoin("rr.manifestoEntregaDocumentos", "med");		
		sql.addInnerJoin("rr.filial", "f");
		sql.addInnerJoin("med.ocorrenciaEntrega", "oe");

		sql.addCustomCriteria("oe.tpOcorrencia = 'E'");
		sql.addCriteria("rr.id", "=", idReciboReembolso);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());		
	}
	
	/**
	 * Consulta a posicao do reembolso no evento 'Reembolso Cancelado'
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosReembolsoCancelado(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(rr.dhCancelamento", "dhCancelamento");
		sql.addProjection("f.sgFilial", "sgFilial");
		sql.addProjection("rr.nrReciboReembolso", "nrDocumento)");

		sql.addInnerJoin(getPersistentClass().getName(), "rr");			
		sql.addInnerJoin("rr.filial", "f");		
		sql.addCriteria("rr.id", "=", idReciboReembolso);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());	
	}
	
	/**
	 * Consulta posicao do recibo no evento 'Mercadoria Devolvida'
	 * 
	 * @param idReciboReembolso
	 * @return
	 */
	public List<Map<String, Object>> findPosMercadoriaDevolvida(Long idReciboReembolso){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(ds.dhEmissao", "dhEmissao");
		sql.addProjection("f.sgFilial", "sgFilial");		
		sql.addProjection("ds.nrDoctoServico", "nrDocumento)");

		sql.addInnerJoin("DoctoServico", "ds");
		sql.addInnerJoin("ds.doctoServicoOriginal", "dsOriginal");
		sql.addInnerJoin("dsOriginal.reciboReembolsosByIdDoctoServReembolsado", "rr");	
		sql.addInnerJoin("dsOriginal.filialByIdFilialOrigem", "f");	

		sql.addCriteria("rr.id", "=", idReciboReembolso);

		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());	
	}
}