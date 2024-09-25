package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.Frete;
import com.mercurio.lms.expedicao.model.RecalculoFrete;
import com.mercurio.lms.expedicao.model.RecalculoFreteArquivoDTO;
import com.mercurio.lms.util.BigDecimalUtils;

public class RecalculoFreteDAO extends BaseCrudDao<RecalculoFrete, Long>{

	@Override
	protected Class getPersistentClass() {
		return RecalculoFrete.class;
	}

	@Override
	public RecalculoFrete findById(Long id) {
		return super.findById(id);
	}

	public ResultSetPage<RecalculoFrete> findPaginated(PaginatedQuery paginatedQuery) {

		Map<String, Object> criteria = paginatedQuery.getCriteria();

		StringBuilder hql = new StringBuilder();
		hql.append("from RecalculoFrete where 1=1 ");

		if(MapUtils.getString(criteria, "dsProcesso") != null){
			hql.append(" and dsProcesso like :dsProcesso ");
		}
		if(MapUtils.getString(criteria, "nrProcesso") != null){
			hql.append(" and nrProcesso = :nrProcesso ");
		}
		if(MapUtils.getString(criteria, "dtInicial") != null){
			hql.append(" and dtInicial = :dtInicial ");
		}
		if(MapUtils.getString(criteria, "dtFinal") != null){
			hql.append(" and dtFinal = :dtFinal ");
		}
		if(MapUtils.getString(criteria, "tpSituacao") != null){
			hql.append(" and tpSituacaoProcesso = :tpSituacao ");
		}
		
		hql.append(" order by nrProcesso desc ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery,hql.toString());
	}

	public Long findIdPessoa(String nrIdentificacao) {

		String sql = "SELECT ID_PESSOA FROM PESSOA WHERE NR_IDENTIFICACAO = "+nrIdentificacao;

		SQLQuery query = getSession().createSQLQuery(sql);

		Object data = query.uniqueResult();
		if(data != null){
			return Long.valueOf(data.toString());
		}

		return null;
	}

	public Long findIdMunicipio(Long cdIBGE, Long idUF) {

		String sql = "SELECT ID_MUNICIPIO FROM MUNICIPIO WHERE CD_IBGE = :cdIbge AND ID_UNIDADE_FEDERATIVA = :idUF ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("ID_MUNICIPIO", Hibernate.LONG);

		query.setLong("cdIbge", cdIBGE);
		query.setLong("idUF", idUF);

		Object data = query.uniqueResult();
		if(data != null){
			return Long.valueOf(data.toString());
		}

		return null;
	}

	public Long findIdFilial(Long idMunicipio) {

		String sql = "SELECT ID_FILIAL FROM MUNICIPIO_FILIAL WHERE ID_MUNICIPIO = :idMunicipio ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("ID_FILIAL", Hibernate.LONG);

		query.setLong("idMunicipio", idMunicipio);

		Object data = query.uniqueResult();
		if(data != null){
			return Long.valueOf(data.toString());
		}

		return null;
	}

	public List findDocRecalculo(Long nrDocumento){

		String sql = "SELECT NR_DOCUMENTO FROM DOCTO_RECALCULO WHERE NR_DOCUMENTO = :nrDocumento ";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.addScalar("NR_DOCUMENTO", Hibernate.LONG);

		query.setLong("nrDocumento", nrDocumento);

		return query.list();
	}

	public List<Object[]> findDocRecalculoByRecalculoFrete(Long idRecalculoFrete){

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ");
		sql.append(" DOC.SG_FILIAL_ORIGEM, ");
		sql.append(" DOC.NR_DOCUMENTO, ");
		sql.append(" DOC.NM_MUNICIPIO_DESTINATARIO, ");
		sql.append(" DOC.NR_IDENTIFICACAO_REMETENTE, ");
		sql.append(" DOC.NM_REMETENTE, ");
		sql.append(" DOC.PS_DECLARADO, ");
		sql.append(" DOC.PS_CUBADO,    ");
		sql.append(" DOC.PS_AFERIDO,    ");
		sql.append(" DOC.PS_REFERENCIA_CALCULO, ");
		sql.append(" DOC.QT_VOLUMES, ");
		sql.append(" DOC.VL_MERCADORIA,   ");
		sql.append(" DOC.VL_TOTAL_FRETE, ");
		sql.append(" PR.VL_PARCELA, ");
		sql.append(" PP.CD_PARCELA_PRECO     ");
		sql.append(" FROM ");
		sql.append(" PARCELA_RECALCULO PR, DOCTO_RECALCULO DOC, PARCELA_PRECO PP ");
		sql.append(" WHERE PR.ID_DOCTO_RECALCULO  = DOC.ID_DOCTO_RECALCULO ");
		sql.append(" AND PR.ID_PARCELA_PRECO = PP.ID_PARCELA_PRECO ");
		sql.append(" AND DOC.ID_RECALCULO_FRETE = :idRecalculoFrete ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());

		query.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
		query.addScalar("NR_DOCUMENTO", Hibernate.LONG);
		query.addScalar("NM_MUNICIPIO_DESTINATARIO", Hibernate.STRING);
		query.addScalar("NR_IDENTIFICACAO_REMETENTE", Hibernate.STRING);
		query.addScalar("NM_REMETENTE", Hibernate.STRING);
		query.addScalar("PS_DECLARADO", Hibernate.BIG_DECIMAL);
		query.addScalar("PS_CUBADO", Hibernate.BIG_DECIMAL);
		query.addScalar("PS_AFERIDO", Hibernate.BIG_DECIMAL);
		query.addScalar("PS_REFERENCIA_CALCULO", Hibernate.BIG_DECIMAL);
		query.addScalar("QT_VOLUMES", Hibernate.LONG);
		query.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
		query.addScalar("VL_TOTAL_FRETE", Hibernate.BIG_DECIMAL);
		query.addScalar("VL_PARCELA", Hibernate.BIG_DECIMAL);
		query.addScalar("CD_PARCELA_PRECO", Hibernate.STRING);

		query.setLong("idRecalculoFrete", idRecalculoFrete);

		return query.list();
	}
	
	public Long storeDoctoRecalculo(final RecalculoFrete recalculoFrete, final Frete frete, final Conhecimento conhecimento){
		return storeDoctoRecalculo(conhecimento.getNrDoctoServico(), 
				conhecimento.getFilialByIdFilialOrigem().getSgFilial(), 
				conhecimento.getPsAforado(),
				conhecimento.getPsReal(),
				conhecimento.getPsAferido(), 
				conhecimento.getPsReferenciaCalculo(), 
				conhecimento.getVlMercadoria(), 
				conhecimento.getQtVolumes(), 
				conhecimento.getClienteByIdClienteRemetente().getPessoa().getNrIdentificacao(), 
				conhecimento.getInscricaoEstadualRemetente() == null ? null : conhecimento.getInscricaoEstadualRemetente().getNrInscricaoEstadual(), 
				conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNrIdentificacao(), 
				conhecimento.getInscricaoEstadualDestinatario() == null ? null : conhecimento.getInscricaoEstadualDestinatario().getNrInscricaoEstadual(), 
				conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio(), 
				frete.getTpFrete(), 
				conhecimento.getVlTotalParcelas(), 
				BigDecimalUtils.defaultBigDecimal(frete.getCalculoFrete().getTributo().getVlImposto()),
				BigDecimalUtils.defaultBigDecimal(frete.getCalculoFrete().getTributo().getPcAliquota()), 
				conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmPessoa(), 
				conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa(), 
				String.valueOf(conhecimento.getClienteByIdClienteRemetente().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge()), 
				String.valueOf(conhecimento.getClienteByIdClienteDestinatario().getPessoa().getEnderecoPessoa().getMunicipio().getCdIbge()),
				conhecimento.getIdDoctoServico(),
				recalculoFrete.getIdRecalculoFrete());
	}
	
	public Long storeDoctoRecalculo(final RecalculoFrete recalculoFrete, final Frete frete, final RecalculoFreteArquivoDTO recalculoFreteArquivo){
		return storeDoctoRecalculo(recalculoFreteArquivo.getNrDocumento(), 
				frete.getConhecimento().getFilialByIdFilialOrigem().getSgFilial(), 
				recalculoFreteArquivo.getPsCubado(),
				recalculoFreteArquivo.getPsReal(),
				recalculoFreteArquivo.getPsAferido(),
				frete.getConhecimento().getPsReferenciaCalculo(),
				recalculoFreteArquivo.getVlMercadoria(),
				recalculoFreteArquivo.getQtVolumes(),
				recalculoFreteArquivo.getNrIdentificacaoRemetente(),
				recalculoFreteArquivo.getNrIERemetente(),
				recalculoFreteArquivo.getNrIdentificacaoDestinatario(),
				recalculoFreteArquivo.getNrIEDestinatario(),
				recalculoFreteArquivo.getNmMunicipioDestino(),
				frete.getTpFrete(),
				frete.getCalculoFrete().getVlTotalParcelas(),
				BigDecimalUtils.defaultBigDecimal(frete.getCalculoFrete().getTributo().getVlImposto()),
				BigDecimalUtils.defaultBigDecimal(frete.getCalculoFrete().getTributo().getPcAliquota()), 
				recalculoFreteArquivo.getNmRemetente(), 
				recalculoFreteArquivo.getNmDestinatario(), 
				recalculoFreteArquivo.getCdIBGEMunicipioOrigemOriginal(), 
				recalculoFreteArquivo.getCdIBGEMunicipioDestinoOriginal(),
				null,
				recalculoFrete.getIdRecalculoFrete());
	}


	private Long storeDoctoRecalculo(final Long nrDocumento, final String sgFilialOrigem, final BigDecimal psCubado, final BigDecimal psDeclarado, final BigDecimal psAferido, final BigDecimal psReferenciaCalculo, final BigDecimal vlMercadoria, final Integer qtVolumes, final String nrIdentificacaoRemetente, final String iERemetente, final String nrIdentificacaoDestinatario, final String iEDestinatario, final String nmMunicipioDestinatario, final String tpFrete, final BigDecimal vlTotalFrete, final BigDecimal vlTotalImposto, final BigDecimal vlAliquota, final String nmRemetente, final String nmDestinatario, final String cdIbgeOrigem, final String cdIbgeDestino, final Long idDoctoServico, final Long idRecalculoFrete){
		
		Long idDoctoRecalculo = findSequenceDoctoServico();

		StringBuilder sql = new StringBuilder()
		.append("INSERT INTO DOCTO_RECALCULO ( ")
		.append(" ID_DOCTO_RECALCULO, ")
		.append(" NR_DOCUMENTO, ")
		.append(" SG_FILIAL_ORIGEM, ")
		.append(" PS_CUBADO, ")
		.append(" PS_DECLARADO, ")
		.append(" PS_AFERIDO, ")
		.append(" PS_REFERENCIA_CALCULO, ")
		.append(" VL_MERCADORIA, ")
		.append(" QT_VOLUMES, ")
		.append(" NR_IDENTIFICACAO_REMETENTE, ")
		.append(" IE_REMETENTE, ")
		.append(" NR_IDENTIFICACAO_DESTINATARIO, ")
		.append(" IE_DESTINATARIO, ")
		.append(" NM_MUNICIPIO_DESTINATARIO, ")
		.append(" TP_FRETE, ")
		.append(" VL_TOTAL_FRETE, ")
		.append(" VL_TOTAL_IMPOSTO, ")
		.append(" VL_ALIQUOTA,")
		.append(" NM_REMETENTE,")
		.append(" NM_DESTINATARIO,")
		.append(" CD_IBGE_ORIGEM,")
		.append(" CD_IBGE_DESTINO,")
		.append(" ID_DOCTO_SERVICO,")
		.append(" ID_RECALCULO_FRETE")
		.append(") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		SQLQuery query = getSession().createSQLQuery(sql.toString());
		query.setParameter(0, idDoctoRecalculo);
		query.setParameter(1, nrDocumento);
		query.setParameter(2, sgFilialOrigem);
		query.setParameter(3, psCubado);
		query.setParameter(4, psDeclarado);
		query.setParameter(5, psAferido);
		query.setParameter(6, psReferenciaCalculo);
		query.setParameter(7, vlMercadoria);
		query.setParameter(8, qtVolumes);
		query.setParameter(9, nrIdentificacaoRemetente);
		query.setParameter(10, iERemetente);
		query.setParameter(11, nrIdentificacaoDestinatario);
		query.setParameter(12, iEDestinatario);
		query.setParameter(13, nmMunicipioDestinatario);
		query.setParameter(14, tpFrete);
		query.setParameter(15, vlTotalFrete);
		query.setParameter(16, vlTotalImposto);
		query.setParameter(17, vlAliquota);
		query.setParameter(18, nmRemetente);
		query.setParameter(19, nmDestinatario);
		query.setParameter(20, cdIbgeOrigem);
		query.setParameter(21, cdIbgeDestino);
		query.setParameter(22, idDoctoServico);
		query.setParameter(23, idRecalculoFrete);

		query.executeUpdate();
		
		return idDoctoRecalculo;
	}

	public Long findSequence(){
		Object data = getSession().createSQLQuery("select DOCTO_RECALCULO_SQ.nextval from dual").uniqueResult();
		if(data != null){
			return  Long.valueOf(data.toString());
		}
		return null;
	}

	public List findIdsDivisaoCliente(Long idCliente){

		String sql = "SELECT ID_DIVISAO_CLIENTE FROM DIVISAO_CLIENTE WHERE id_cliente = :idCliente AND TP_SITUACAO = 'A' order by case when upper(DS_DIVISAO_CLIENTE) LIKE upper('Centro-Oeste e Norte%') then 0 else 1 end";

		SQLQuery query = getSession().createSQLQuery(sql);
		query.setLong("idCliente", idCliente);

		return query.list();
	}

	public List<RecalculoFrete> findInProcess(RecalculoFrete recalculoFrete) {

		DetachedCriteria dc = createDetachedCriteria()
			.add(Restrictions.eq("tpSituacaoProcesso", "I"))
			.add(Restrictions.ne("idRecalculoFrete", recalculoFrete.getIdRecalculoFrete()));

		return findByDetachedCriteria(dc);
	}
	
	private Long findSequenceDoctoServico(){
		return Long.valueOf(getSession().createSQLQuery("select DOCTO_RECALCULO_SQ.nextval from dual").uniqueResult().toString());
	}

}