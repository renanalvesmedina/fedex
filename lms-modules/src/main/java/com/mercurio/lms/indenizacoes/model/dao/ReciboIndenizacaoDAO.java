package com.mercurio.lms.indenizacoes.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.vendas.model.Cliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ReciboIndenizacaoDAO extends BaseCrudDao<ReciboIndenizacao, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ReciboIndenizacao.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("processoSinistro", FetchMode.JOIN);
		lazyFindById.put("processoSinistro.tipoSeguro", FetchMode.JOIN);
		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("moeda", FetchMode.JOIN);
		lazyFindById.put("pessoaByIdBeneficiario", FetchMode.JOIN);
		lazyFindById.put("pessoaByIdFavorecido", FetchMode.JOIN);
		lazyFindById.put("banco", FetchMode.JOIN);
		lazyFindById.put("agenciaBancaria", FetchMode.JOIN);
		lazyFindById.put("doctoServicoIndenizacoes", FetchMode.SELECT);
	}

	/**
	 * Find de ReciboIndenizacao sem fazer nenhum fetch.
	 * @param idReciboIndenizacao
	 * @return
	 */
	public ReciboIndenizacao findReciboIndenizacaoById(Long idReciboIndenizacao) {
		StringBuffer s = new StringBuffer()
		.append("select rim from ReciboIndenizacao as rim ")
		.append("where rim.id = ?");
		return (ReciboIndenizacao) getAdsmHibernateTemplate().findUniqueResult(s.toString(), new Object[]{idReciboIndenizacao});
	} 

	public ReciboIndenizacao findReciboIndenizacaoByIdReciboIndenizacao(Long idReciboIndenizacao) {
		ReciboIndenizacao reciboIndenizacao = super.findByIdInitLazyProperties(idReciboIndenizacao, Boolean.TRUE);
		//Utilizado para incializar a propriedade do ReciboIndenizacao
		if (reciboIndenizacao.getPendencia() != null) {
			reciboIndenizacao.getPendencia().getIdPendencia();
		}
		return reciboIndenizacao;
	} 

	/**
	 * Solicitação CQPRO00006043 da Integração.
	 * Busca uma instância de ReciboIndenizacao com base na filial e número do mesmo.
	 * @param idFilial
	 * @param nrReciboIndenizacao
	 * @return
	 */
	public ReciboIndenizacao findByIdFilialNrReciboIndenizacao(Long idFilial, Integer nrReciboIndenizacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "ri");

		dc.setFetchMode("avisoPagoRim", FetchMode.JOIN);
		dc.add(Restrictions.eq("ri.filial.id", idFilial));
		dc.add(Restrictions.eq("ri.nrReciboIndenizacao", nrReciboIndenizacao));

		return (ReciboIndenizacao) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	
	/**
	 * Solicitação 21.06.01.02 Aviso de pagamento para Clientes
	 * 
	 * @param tpStatusIndenizacao
	 * @param dtPagamentoEfetuado
	 * @param blEmailPagto
	 * @return lista de Recibo Indenização de acordo com tpStatusIndenizacao, dtPagamentoEfetuado e blEmailPagto
	 */
	public List<ReciboIndenizacao> findReciboIndenizacaoByStatusIndenizacaoDtPagtoEfetuadoBlEmailPagto(String tpStatusIndenizacao,
					YearMonthDay dtPagamentoEfetuado, Boolean blEmailPagto){
		
		 StringBuffer hql = new StringBuffer();
		 hql.append( " select distinct reciboIndenizacao " )
		 	.append( " from " + ReciboIndenizacao.class.getName() + " as reciboIndenizacao " )
			.append( " inner join reciboIndenizacao.pessoaByIdFavorecido as favorecido " )
			.append( " inner join reciboIndenizacao.doctoServicoIndenizacoes as doctoServicoIndenizacoes ")
			.append( " inner join doctoServicoIndenizacoes.reciboIndenizacaoNfs as reciboIndenizacaoNfs ")
			.append( " inner join reciboIndenizacaoNfs.notaFiscalConhecimento as notaFiscalConhecimento ");
		
		 SqlTemplate sql = new SqlTemplate();
		 sql.addCriteria("reciboIndenizacao.tpStatusIndenizacao", "=", tpStatusIndenizacao);
		 sql.addCriteria("reciboIndenizacao.dtPagamentoEfetuado", "<=", dtPagamentoEfetuado);
		 sql.addCriteria("reciboIndenizacao.blEmailPgto", "=", blEmailPagto);
		 
		 hql.append(sql.getSql());
		 List<ReciboIndenizacao> reciboIndenizacaoList = getAdsmHibernateTemplate().find(hql.toString(), sql.getCriteria());
		 return reciboIndenizacaoList;						
	}
	
	

	/**
	 * Store da DF2
	 * @param reciboIndenizacao
	 * @param itemList
	 * @return
	 */
	public Long storeAll(ReciboIndenizacao reciboIndenizacao, ItemList itensDocto, ItemList itensMda, ItemList itensParcela, ItemList itensAnexoRim, ItemListConfig configDocto, List filiaisDebitadas) {
		store(reciboIndenizacao);
		getAdsmHibernateTemplate().saveOrUpdateAll(itensDocto.getNewOrModifiedItems());
		getAdsmHibernateTemplate().deleteAll(itensDocto.getRemovedItems());

		getAdsmHibernateTemplate().saveOrUpdateAll(itensMda.getNewOrModifiedItems());
		getAdsmHibernateTemplate().deleteAll(itensMda.getRemovedItems());

		getAdsmHibernateTemplate().saveOrUpdateAll(itensAnexoRim.getNewOrModifiedItems());
		getAdsmHibernateTemplate().deleteAll(itensAnexoRim.getRemovedItems());

		getAdsmHibernateTemplate().saveOrUpdateAll(itensParcela.getNewOrModifiedItems());
		getAdsmHibernateTemplate().deleteAll(itensParcela.getRemovedItems());

		for (Iterator it = itensDocto.iterator(reciboIndenizacao.getIdReciboIndenizacao(), configDocto); it.hasNext(); ) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			if (doctoServicoIndenizacao.getReciboIndenizacaoNfs()!=null)
				getAdsmHibernateTemplate().saveOrUpdateAll(doctoServicoIndenizacao.getReciboIndenizacaoNfs());
		}
		getAdsmHibernateTemplate().saveOrUpdateAll(filiaisDebitadas);
		getAdsmHibernateTemplate().flush();
		return reciboIndenizacao.getIdReciboIndenizacao();
	}

	/**
	 * Atualiza o status da indenização à partir do pagamento manual
	 * 
	 * @param idReciboIndenizacao
	 * @param tpStatusIndenizacao
	 */
	public void updateReciboIndenizacaRimPago(Long idReciboIndenizacao, String tpStatusIndenizacao) {

		StringBuffer hql = new StringBuffer()
			.append("UPDATE " + ReciboIndenizacao.class.getName()+ " as ri SET ri.tpStatusIndenizacao = ? ")
			.append("WHERE ri.idReciboIndenizacao = ? ");

		List param = new ArrayList();
		param.add(tpStatusIndenizacao);
		param.add(idReciboIndenizacao);

		executeHql(hql.toString(), param);
	}
	
	   public void updateReciboIndenizacaoByIdProcessoSinistro(Long idProcessoSinistro){
			List parameters = new ArrayList();
			StringBuilder sql = new StringBuilder()
	    	.append("update ")
	    	.append(ReciboIndenizacao.class.getName() + " as ri ")
	    	.append(" set ri.processoSinistro.idProcessoSinistro = null ")
			.append("where ri.processoSinistro.idProcessoSinistro = ? ")
			.append(" and ri.tpStatusIndenizacao = 'C' ");
			
			parameters.add(idProcessoSinistro);
			
			executeHql(sql.toString(), parameters);
	   }
	
	/**
	 * Obtém a contagem de ocorrências de não-conformidade para 
	 * cada não-conformidade dos documentos de um determinado RIM.
	 * @return
	 */
	public Map getRowCountOcorrenciasRIM(Long idReciboIndenizacao) {

		StringBuffer sb = new StringBuffer()
		.append("select new map(count(*) as rowCount, nc.id as idNaoConformidade) ")
		.append(" from "+ReciboIndenizacao.class.getName()+" rim ")
		.append(" join rim.doctoServicoIndenizacoes dsi ")
		.append(" join dsi.doctoServico ds ") 
		.append(" join ds.naoConformidades nc ")
		.append(" join nc.ocorrenciaNaoConformidades onc ")
		.append(" where rim.id = ? ")
		.append(" group by nc.id");

		Query q = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(sb.toString());
		q.setParameter(0, idReciboIndenizacao);
		return (Map)q.uniqueResult();
	}

	/**
	 * Solicitação CQPRO00006180 da Integração.
	 * Método que retorna uma lista de ReciboIndenizacao conforme o filtro.
	 * @param tpStatusIndenizacao
	 * @param dtProgramadaPagamentoLimite
	 * @param dtDevolucaoBancoLimite
	 * @return
	 */
	public List findReciboIndenizacao(String tpStatusIndenizacao, YearMonthDay dtProgramadaPagamento, YearMonthDay dtProgramadaPagamentoLimite, YearMonthDay dtDevolucaoBancoLimite){
		DetachedCriteria dc = DetachedCriteria.forClass(ReciboIndenizacao.class, "ri");
		dc.add(Restrictions.eq("ri.tpStatusIndenizacao", tpStatusIndenizacao));
		dc.add(Restrictions.eq("ri.dtProgramadaPagamento", dtProgramadaPagamentoLimite));
		dc.add(Restrictions.or(
				Restrictions.isNull("ri.dtDevolucaoBanco"),
				Restrictions.lt("ri.dtDevolucaoBanco", dtDevolucaoBancoLimite))
		);
		return findByDetachedCriteria(dc);
	}

	/**
	 * Método find customizado para busca de reciboIndenizacao. Utilizado na lookup de
	 * reciboIndenizacao na tela de liberar pagamento de indenização. 
	 * @param idFilial
	 * @param nrReciboIndenizacao
	 * @return
	 */
	public List findReciboIndenizacaoToProcessosRim(Long idFilial, Integer nrReciboIndenizacao, Long idReciboIndenizacao) {
		StringBuffer hql = new StringBuffer(); 
		hql.append("select new map(");
		hql.append(" ri.nrReciboIndenizacao as nrReciboIndenizacao, ");
		hql.append(" ri.idReciboIndenizacao as idReciboIndenizacao, ");
		hql.append(" ri.dtEmissao as dtEmissao, ");
		hql.append(" ri.vlIndenizacao as vlIndenizacao, ");
		hql.append(" ri.tpIndenizacao as tpIndenizacao, ");
		hql.append(" ri.tpFormaPagamento as tpFormaPagamento, ");
		hql.append(" ri.tpStatusIndenizacao as tpStatusIndenizacao, ");
		hql.append(" ri.tpSituacaoWorkflow as tpSituacaoWorkflow, ");
		hql.append(" ri.dtProgramadaPagamento as dtProgramadaPagamento, ");
		hql.append(" f.sgFilial as filial_sgFilial, ");
		hql.append(" f.idFilial as filial_idFilial, ");
		hql.append(" beneficiario.nrIdentificacao as pessoaByIdBeneficiario_nrIdentificacao, ");
		hql.append(" beneficiario.nmPessoa as pessoaByIdBeneficiario_nmPessoa, ");
		hql.append(" beneficiario.tpIdentificacao as pessoaByIdBeneficiario_tpIdentificacao, ");
		hql.append(" cliBeneficiario.blMtzLiberaRIM as pessoaByIdBeneficiario_blMtzLiberaRIM, ");
		hql.append(" favorecido.nrIdentificacao as pessoaByIdFavorecido_nrIdentificacao, ");
		hql.append(" favorecido.nmPessoa as pessoaByIdFavorecido_nmPessoa, ");
		hql.append(" favorecido.tpIdentificacao as pessoaByIdFavorecido_tpIdentificacao, ");
		hql.append(" cliFavorecido.blMtzLiberaRIM as pessoaByIdFavorecido_blMtzLiberaRIM, ");
		hql.append(" moeda.sgMoeda as moeda_sgMoeda, ");
		hql.append(" moeda.dsSimbolo as moeda_dsSimbolo, ");
		hql.append(" ps.nrProcessoSinistro as nrProcessoSinistro ");
		
		hql.append(" )");
		// from, joins and where 
		hql.append(" from "+ReciboIndenizacao.class.getName()+" ri ");
		hql.append(" join ri.pessoaByIdBeneficiario beneficiario ");
		hql.append(" join ri.filial f ");
		hql.append(" left join ri.processoSinistro ps ");
		hql.append(" left join ri.pessoaByIdFavorecido favorecido ");
		hql.append(" join ri.moeda moeda ");
		hql.append(" , ");
		hql.append(Cliente.class.getSimpleName()).append(" cliBeneficiario ");
		hql.append(" , ");
		hql.append(Cliente.class.getSimpleName()).append(" cliFavorecido ");
		
		SqlTemplate sql = new SqlTemplate();
		
		if (idFilial!=null) {
			sql.addCustomCriteria("f.idFilial = ? ");
			sql.addCriteriaValue(idFilial);
		}
		
		if (nrReciboIndenizacao!=null) {
			sql.addCriteriaValue(nrReciboIndenizacao);
			sql.addCustomCriteria("ri.nrReciboIndenizacao = ?");
		}
		
		if (idReciboIndenizacao!=null) {
			sql.addCustomCriteria("ri.idReciboIndenizacao = ?");
			sql.addCriteriaValue(idReciboIndenizacao);			
		}

		sql.addCustomCriteria("cliBeneficiario.idCliente = beneficiario.idPessoa ");
		sql.addCustomCriteria("cliFavorecido.idCliente = favorecido.idPessoa ");
		
		hql.append(sql.getSql());
		
		List l = getAdsmHibernateTemplate().find(hql.toString(), sql.getCriteria());
		List retorno = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(l);
		return retorno;
	}


	
	/**
	 * 
	 * @param tfm
	 * @param param
	 * @return
	 */
	private StringBuffer getFindPaginatedReciboIndenizacaoTemplate(TypedFlatMap tfm, List param) {
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(ReciboIndenizacao.class.getName()).append(" as rim ")
		.append("join fetch rim.moeda mo ")
		.append("join fetch rim.pessoaByIdBeneficiario ben ")
		.append("join fetch rim.filial fil ")
		.append("where 1=1 ");

		//LMS-666 ReciboIndenizacao.nrNotaFiscalDebitoCliente 
		if (tfm.getLong("nrNotaFiscalDebitoCliente") != null) {
			sql.append("and rim.nrNotaFiscalDebitoCliente = ? ");
			param.add(tfm.getLong("nrNotaFiscalDebitoCliente"));
		}
		//LMS-666  ReciboIndenizacao.blSegurado
		if (tfm.getBoolean("blSegurado") != null) {
			sql.append("and rim.blSegurado = ? ");
			param.add(tfm.getBoolean("blSegurado"));
		}
		
		if (tfm.getInteger("nrReciboIndenizacao") != null) {
			sql.append("and rim.nrReciboIndenizacao = ? ");
			param.add(tfm.getInteger("nrReciboIndenizacao"));
		}
		if (tfm.getLong("filial.idFilial") != null) {
			sql.append("and fil.id = ? ");
			param.add(tfm.getLong("filial.idFilial"));
		}
		if (tfm.getLong("filialDebitada.idFilial") != null) {
			sql.append("and exists (from ")
			.append(FilialDebitada.class.getName())
			.append(" fd where fd.reciboIndenizacao.id = rim.id and fd.filial.id = ?) ");
			param.add(tfm.getLong("filialDebitada.idFilial"));
		}
		if (!StringUtils.isBlank(tfm.getString("tpIndenizacao"))) {
			sql.append("and rim.tpIndenizacao = ? ");
			param.add(tfm.getString("tpIndenizacao"));
		}
		if (tfm.getLong("processoSinistro.idProcessoSinistro") != null) {
			sql.append("and rim.processoSinistro.id = ? ");
			param.add(tfm.getLong("processoSinistro.idProcessoSinistro"));
		}
		if (tfm.getLong("tipoSeguro.idTipoSeguro") != null) {
			sql.append("and rim.processoSinistro.tipoSeguro.id = ? ");
			param.add(tfm.getLong("tipoSeguro.idTipoSeguro"));
		}
		if (tfm.getYearMonthDay("periodoInicial") != null) {
			sql.append("and rim.dtGeracao >= ? ");
			param.add(tfm.getYearMonthDay("periodoInicial"));
		}
		if (tfm.getYearMonthDay("periodoFinal") != null) {
			sql.append("and rim.dtGeracao <= ? ");
			param.add(tfm.getYearMonthDay("periodoFinal"));
		}
		if (tfm.getLong("filialBeneficiada.idFilial") != null) {
			sql.append("and rim.pessoaByIdBeneficiario.id = ? ");
			param.add(tfm.getLong("filialBeneficiada.idFilial"));
		}
		if (tfm.getLong("clienteBeneficiario.idCliente") != null) {
			sql.append("and rim.pessoaByIdBeneficiario.id = ? ");
			param.add(tfm.getLong("clienteBeneficiario.idCliente"));
		}
		if (tfm.getLong("beneficiarioTerceiro.idPessoa") != null) {
			sql.append("and rim.pessoaByIdBeneficiario.id = ? ");
			param.add(tfm.getLong("beneficiarioTerceiro.idPessoa"));
		}
		if (tfm.getLong("filialFavorecida.idFilial") != null) {
			sql.append("and rim.pessoaByIdFavorecido.id = ? ");
			param.add(tfm.getLong("filialFavorecida.idFilial"));
		}
		if (tfm.getLong("clienteFavorecido.idCliente") != null) {
			sql.append("and rim.pessoaByIdFavorecido.id = ? ");
			param.add(tfm.getLong("clienteFavorecido.idCliente"));
		}
		if (tfm.getLong("favorecidoTerceiro.idPessoa") != null) {
			sql.append("and rim.pessoaByIdFavorecido.id = ? ");
			param.add(tfm.getLong("favorecidoTerceiro.idPessoa"));
		}
		if (tfm.getLong("filialFavorecida.idFilial")!=null || tfm.getLong("clienteFavorecido.idCliente")!=null || tfm.getLong("favorecidoTerceiro.idPessoa")!=null) {
			sql.append("and rim.tpFavorecidoIndenizacao is not null ");
		}
		if ("S".equals(tfm.getString("restringeCancelados"))) {
			sql.append("and rim.tpStatusIndenizacao <> 'C' ");
		}
		if (tfm.getDomainValue("tpBeneficiarioIndenizacao") != null && !tfm.getDomainValue("tpBeneficiarioIndenizacao").getValue().equals("")) {
			sql.append("and rim.tpBeneficiarioIndenizacao = ? ");
			param.add(tfm.getDomainValue("tpBeneficiarioIndenizacao").getValue());
		}
		if (tfm.getDomainValue("tpFavorecidoIndenizacao") != null && !tfm.getDomainValue("tpFavorecidoIndenizacao").getValue().equals("")) {
			sql.append("and rim.tpFavorecidoIndenizacao = ? ");
			param.add(tfm.getDomainValue("tpFavorecidoIndenizacao").getValue());
		}

		if (tfm.getDomainValue("tpStatusIndenizacao") != null && !"".equals(tfm.getDomainValue("tpStatusIndenizacao").getValue())) {
			sql.append("and rim.tpStatusIndenizacao = ? ");
			param.add(tfm.getDomainValue("tpStatusIndenizacao").getValue());
		}


		sql.append("and exists( select 1 from ")
		.append(DoctoServicoIndenizacao.class.getName()).append(" as doctoServicoIndenizacao ")
		.append("inner join doctoServicoIndenizacao.doctoServico as ds ");

		if (tfm.getLong("naoConformidade.idNaoConformidade") != null || tfm.getLong("ocorrenciaNaoConformidade.motivoAberturaNc.idMotivoAberturaNc") != null) {
			sql.append("inner join ds.naoConformidades as nc ");
			if (tfm.getLong("ocorrenciaNaoConformidade.motivoAberturaNc.idMotivoAberturaNc") != null) {
				sql.append("inner join nc.ocorrenciaNaoConformidades as onc ");
			}
		}

		sql.append("where ")
		.append("doctoServicoIndenizacao.reciboIndenizacao.id = rim.id ");

		if (tfm.getLong("naoConformidade.idNaoConformidade") != null) {
			sql.append("and nc.id = ? ");
			param.add(tfm.getLong("naoConformidade.idNaoConformidade"));
		}
		if (tfm.getLong("ocorrenciaNaoConformidade.motivoAberturaNc.idMotivoAberturaNc") != null) {
			sql.append("and onc.motivoAberturaNc.id = ? ");
			param.add(tfm.getLong("ocorrenciaNaoConformidade.motivoAberturaNc.idMotivoAberturaNc"));
		}
		if (!StringUtils.isBlank(tfm.getString("doctoServico.tpDocumentoServico"))) {
			sql.append("and ds.tpDocumentoServico = ? ");
			param.add(tfm.getString("doctoServico.tpDocumentoServico"));
		}
		if (tfm.getLong("doctoServico.idDoctoServico") != null) {
			sql.append("and ds.id = ? ");
			param.add(tfm.getLong("doctoServico.idDoctoServico"));
		}
		if (tfm.getLong("doctoServico.filialByIdFilialOrigem.idFilial") != null) {
			sql.append("and ds.filialByIdFilialOrigem.id = ? ");
			param.add(tfm.getLong("doctoServico.filialByIdFilialOrigem.idFilial"));
		}
		if (tfm.getLong("clienteRemetente.idCliente") != null) {
			sql.append("and ds.clienteByIdClienteRemetente.id = ? ");
			param.add(tfm.getLong("clienteRemetente.idCliente"));
		}
		if (tfm.getLong("clienteDestinatario.idCliente") != null) {
			sql.append("and ds.clienteByIdClienteDestinatario.id = ? ");
			param.add(tfm.getLong("clienteDestinatario.idCliente"));
		}
		sql.append(") ");
		
		sql.append("order by fil.sgFilial, rim.nrReciboIndenizacao ");
		return sql;
	}


	public ResultSetPage findPaginatedReciboIndenizacao(TypedFlatMap tfm, FindDefinition fd) {
		List param = new ArrayList();
		String sql = getFindPaginatedReciboIndenizacaoTemplate(tfm, param).toString();
		return getAdsmHibernateTemplate().findPaginated(sql, fd.getCurrentPage(), fd.getPageSize(), param.toArray());
	}

	public Integer getRowCountReciboIndenizacao(TypedFlatMap tfm) {
		List param = new ArrayList();
		String sql = getFindPaginatedReciboIndenizacaoTemplate(tfm, param).toString();
		return getAdsmHibernateTemplate().getRowCountForQuery(sql, param.toArray());
	}

	public ReciboIndenizacao findReciboIndenizacaoByIdDoctoServico(Long idDoctoServico) {
		StringBuffer s = new StringBuffer()
		.append("select rim ")
		.append("from "+DoctoServicoIndenizacao.class.getName()+" dsi ")
		.append("join dsi.reciboIndenizacao rim ")
		.append("where dsi.doctoServico.id = ?");
		return (ReciboIndenizacao) getAdsmHibernateTemplate().findUniqueResult(s.toString(), new Object[]{idDoctoServico});
	}

	public ReciboIndenizacao findReciboIndenizacaoByFilialNrReciboIndenizacaoDtEmissao(
			Filial filial, Integer nrReciboIndenizacao, YearMonthDay dtEmissao) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom("ReciboIndenizacao", "ri");
		sql.addFrom("Filial", "f");
		
		sql.addProjection("ri");
		
		sql.addCustomCriteria("ri.filial.id = f.id");
		
		sql.addCriteria("f.sgFilial", "=", filial.getSgFilial());
		sql.addCriteria("ri.nrReciboIndenizacao", "=", nrReciboIndenizacao);
		sql.addCriteria("ri.dtEmissao", "=", dtEmissao);
		
		return (ReciboIndenizacao)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		
	} 
	
	public Long findPendenciaByReciboIndenizacao(ReciboIndenizacao reciboIndenizacao) {
		StringBuffer s = new StringBuffer()
		.append("select rim.pendencia.idPendencia ")
		.append("from "+ReciboIndenizacao.class.getName()+" rim ")
		.append("where rim.idReciboIndenizacao = ?");
		return (Long) getAdsmHibernateTemplate().findUniqueResult(s.toString(), new Object[]{reciboIndenizacao.getIdReciboIndenizacao()});
	}
	
	
	public ResultSetPage findReciboIndenizacaoByIdRecidoFreteCarreteiro(Long idRecidoFreteCarreteiro){
			List param = new ArrayList();
			
			StringBuilder sql = getQueryReciboIndenizacaoByIdReciboFreteCarreteiro(false);
			
			param.add(idRecidoFreteCarreteiro);
			
			
			ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
				public void configQuery(SQLQuery sqlQuery) {				
					sqlQuery.addScalar("DOC_TP_DOCUMENTO_SERVICO", Hibernate.STRING);
					sqlQuery.addScalar("DOC_NR_DOCTO_SERVICO", Hibernate.LONG);
					sqlQuery.addScalar("DOC_DS_SIGLALMS", Hibernate.STRING);
					sqlQuery.addScalar("DOC_DH_EMISSAO",  Hibernate.STRING);
					sqlQuery.addScalar("DOC_SG_MOEDA", Hibernate.STRING);
					sqlQuery.addScalar("DOC_DS_SIMBOLO", Hibernate.STRING);
					sqlQuery.addScalar("DOC_VL_MERCADORIA", Hibernate.BIG_DECIMAL);
					
					sqlQuery.addScalar("RI_DS_SIGLALMS", Hibernate.STRING);
					sqlQuery.addScalar("RI_NR_RECIBO_INDENIZACAO", Hibernate.LONG);
					sqlQuery.addScalar("RI_NR_RECIBO_INDENIZACAO", Hibernate.LONG);
					sqlQuery.addScalar("RI_DT_EMISSAO",  Hibernate.STRING);
					sqlQuery.addScalar("RI_SG_MOEDA", Hibernate.STRING);
					sqlQuery.addScalar("RI_DS_SIMBOLO", Hibernate.STRING);
					sqlQuery.addScalar("RI_VL_INDENIZACAO", Hibernate.BIG_DECIMAL);		
					sqlQuery.addScalar("RI_ID_RECIBO_INDENIZACAO", Hibernate.LONG);
					sqlQuery.addScalar("RI_ID_FILIAL", Hibernate.LONG);
					sqlQuery.addScalar("FRI_DS_DESCRICAOSIGLA",Hibernate.STRING);
					
				}
			};
			return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), configureSqlQuery);	
			
	}

	private StringBuilder getQueryReciboIndenizacaoByIdReciboFreteCarreteiro(boolean count) {
		StringBuilder sql = new StringBuilder();			
		if(!count){
			sql.append(getProjection());			
		}
		sql.append("FROM RECIBO_FRETE_CARRETEIRO RFC ");
		sql.append("	INNER JOIN NOTA_CREDITO NC ON RFC.ID_RECIBO_FRETE_CARRETEIRO = NC.ID_RECIBO_FRETE_CARRETEIRO ");
		sql.append("	INNER JOIN MANIFESTO MA ON NC.ID_CONTROLE_CARGA = MA.ID_CONTROLE_CARGA ");
		sql.append("	INNER JOIN MANIFESTO_ENTREGA_DOCUMENTO ME ON MA.ID_MANIFESTO = ME.ID_MANIFESTO_ENTREGA ");
		sql.append("	INNER JOIN DOCTO_SERVICO DOC ON ME.ID_DOCTO_SERVICO = DOC.ID_DOCTO_SERVICO ");
		sql.append("	INNER JOIN DOCTO_SERVICO_INDENIZACAO DSI ON DOC.ID_DOCTO_SERVICO = DSI.ID_DOCTO_SERVICO ");
		sql.append("	INNER JOIN RECIBO_INDENIZACAO RI ON DSI.ID_RECIBO_INDENIZACAO  = RI.ID_RECIBO_INDENIZACAO ");
		sql.append("	INNER JOIN FILIAL FDOC ON DOC.ID_FILIAL_ORIGEM =  FDOC.ID_FILIAL ");
		sql.append("	INNER JOIN FILIAL FRI ON RI.ID_FILIAL =  FRI.ID_FILIAL ");
		sql.append("	LEFT JOIN  MOEDA MO ON MO.ID_MOEDA = DOC.ID_MOEDA ");
		sql.append("	LEFT JOIN  MOEDA MRI ON MRI.ID_MOEDA = RI.ID_MOEDA   ");
		sql.append("	INNER JOIN PESSOA PDOC  ON PDOC.ID_PESSOA =  FDOC.ID_FILIAL ");
		sql.append("	INNER JOIN PESSOA PRI   ON PRI.ID_PESSOA =  FRI.ID_FILIAL  ");
		sql.append("	INNER JOIN VALOR_DOMINIO VD ON VD.VL_VALOR_DOMINIO = DOC.TP_DOCUMENTO_SERVICO ");
		sql.append("	INNER JOIN DOMINIO DOM ON VD.ID_DOMINIO = DOM.ID_DOMINIO ");
		sql.append("WHERE RFC.ID_RECIBO_FRETE_CARRETEIRO = ? ");
		sql.append("  AND DOM.NM_DOMINIO = 'DM_TIPO_DOCUMENTO_SERVICO' ");
		
		return sql;
	}

	private StringBuilder getProjection() {
		StringBuilder sql = new StringBuilder();			
		sql.append("SELECT DISTINCT ");
		sql.append("	VI18N(VD.DS_VALOR_DOMINIO_I) AS DOC_TP_DOCUMENTO_SERVICO,"); 
		sql.append("	DOC.NR_DOCTO_SERVICO AS DOC_NR_DOCTO_SERVICO,	");
		sql.append("	FDOC.SG_FILIAL AS DOC_DS_SIGLALMS, ");
		sql.append("	TO_CHAR(DOC.DH_EMISSAO,'DD/MM/YYYY HH24:MI:SS') AS DOC_DH_EMISSAO,  ");
		sql.append("	MO.SG_MOEDA AS DOC_SG_MOEDA,  ");
		sql.append("	MO.DS_SIMBOLO AS DOC_DS_SIMBOLO, ");
		sql.append("	DOC.VL_MERCADORIA AS DOC_VL_MERCADORIA ,  	");
		sql.append("	FRI.SG_FILIAL AS RI_DS_SIGLALMS, ");
		sql.append("	RI.NR_RECIBO_INDENIZACAO AS RI_NR_RECIBO_INDENIZACAO, ");
		sql.append("	TO_CHAR(RI.DT_EMISSAO,'DD/MM/YYYY') AS RI_DT_EMISSAO,");
		sql.append("	MRI.SG_MOEDA  AS RI_SG_MOEDA,  ");
		sql.append("	MRI.DS_SIMBOLO AS RI_DS_SIMBOLO,  ");
		sql.append("	RI.VL_INDENIZACAO AS RI_VL_INDENIZACAO, ");
		sql.append("	RI.ID_RECIBO_INDENIZACAO AS RI_ID_RECIBO_INDENIZACAO, ");
		sql.append("	RI.ID_FILIAL AS RI_ID_FILIAL, ");
		sql.append("	PRI.NM_FANTASIA  AS FRI_DS_DESCRICAOSIGLA ");
		
		return sql;
	}
	
	public Integer getRowCountgetReciboIndenizacaoByIdReciboFreteCarreteiro(Long idRecidoFreteCarreteiro) {
		List param = new ArrayList();
		param.add(idRecidoFreteCarreteiro);		
		
		StringBuilder sql = new StringBuilder();			
		sql.append("SELECT COUNT(*) as rowCount ");
		sql.append(getQueryReciboIndenizacaoByIdReciboFreteCarreteiro(true));
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), param.toArray());
	}
	
	
}