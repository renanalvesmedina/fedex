package com.mercurio.lms.sim.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.entrega.model.ReciboReembolso;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class LMFreteDAO extends AdsmDao {


	public List findPaginatedParcelasPreco(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map("+PropertyVarcharI18nProjection.createProjection("pp.nmParcelaPreco")+" as nmParcelaPreco, " +
				"pds.vlParcela as vlParcela, " +
				"ds.vlDesconto as vlDesconto, " +
				"mo.sgMoeda||' '||mo.dsSimbolo as moeda, " +
				"mo.dsSimbolo as dsSimbolo, " +
				"mo.sgMoeda as sgMoeda, " +
				"ds.vlTotalParcelas as vlTotalParcelas)");

		hql.addFrom(ParcelaDoctoServico.class.getName(),"pds " +
				"join pds.parcelaPreco pp " +
				"join pds.doctoServico ds " +
				"join ds.moeda mo ");

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		hql.addCriteria("pp.tpParcelaPreco","!=", "S");

		hql.addOrderBy(""+PropertyVarcharI18nProjection.createProjection("pp.nmParcelaPreco")+"");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public Map findValorTotalParcelas(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(ds.vlTotalParcelas as vlTotalParcelas, mo.dsSimbolo as dsSimbolo)");

		hql.addFrom(ParcelaDoctoServico.class.getName(),"pds " +
				"join pds.parcelaPreco pp " +
				"join pds.doctoServico ds " +
				"join ds.moeda mo ");

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		hql.addCriteria("pp.tpParcelaPreco","!=", "S");

		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public Map findTotaisCalculoServico(Long idDoctoServico){
		if (idDoctoServico == null) {
			throw new BusinessException("LMS-21053");
		}
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(ds.vlTotalDocServico as vlTotalDocServico, " +
				"ds.vlTotalServicos as vlTotalServicos, " +
				"mo.dsSimbolo as dsSimbolo, " +
				"ds.vlIcmsSubstituicaoTributaria as vlICMSST, ds.vlTotalParcelas as vlTotalParcelas," +
				"ds.vlLiquido as vlLiquido)");

		hql.addFrom(DoctoServico.class.getName(),"ds " +
				"join ds.moeda mo ");

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);

		return(Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public List findPaginatedCalculoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(pp.nmParcelaPreco as nmParcelaPreco, " +
				"pds.vlParcela as vlParcela, " +
				"ds.vlTotalServicos as vlTotalServicos, "+
				"mo.dsSimbolo as dsSimbolo, " +
				"mo.sgMoeda as sgMoeda )");

		hql.addFrom(ParcelaDoctoServico.class.getName(),"pds " +
				"join pds.parcelaPreco pp " +
				"join pds.doctoServico ds " +
				"join ds.moeda mo ");

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		hql.addCriteria("pp.tpParcelaPreco","=", "S");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findTipoTributacaoIcms(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(tti.dsTipoTributacaoIcms as dsTipoTributacaoIcms, " +
				"conh.nrCfop as nrCfop, " +
				"conh.vlImpostoDifal as vlImpostoDifal, " +
				"conh.tpSituacaoPendencia as tpSituacaoPendencia, " +
				"moeda.dsSimbolo as dsSimbolo) ");
		hql.addFrom(Conhecimento.class.getName()," conh " +
				"left outer join conh.moeda moeda " +
				"left outer join conh.tipoTributacaoIcms tti ");

		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findPaginatedImpostos(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(iser.tpImposto as tpImposto, " +
				"iser.vlBaseCalculo as vlBaseCalculo, " +
				"iser.pcAliquota as pcAliquota, "+
				"iser.vlImposto as vlImpostoServico, " +
				"conh.vlBaseCalcImposto as vlBaseCalcImposto, " +
				"conh.pcAliquotaIcms as pcAliquotaIcms, " +
				"conh.vlImposto as vlImposto, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"moeda.sgMoeda as sgMoeda)");

		hql.addFrom(ImpostoServico.class.getName()," iser " +
				"left outer join iser.conhecimento conh " +
				"left outer join conh.moeda moeda ");

		hql.addCriteria("conh.idDoctoServico","=",idDoctoServico);


		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public Map findIcmsDoctoServico(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(ds.vlImposto as vlImposto, " +
				"ds.vlBaseCalcImposto as vlBaseCalcImposto, " +
				"ds.pcAliquotaIcms as pcAliquotaIcms, " +
				"moeda.dsSimbolo as dsSimbolo, " +
				"moeda.sgMoeda as sgMoeda )");

		hql.addFrom(DoctoServico.class.getName(),"ds " +
				"join ds.moeda moeda " );

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);


		return (Map)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
	}

	public List findDadosCalculoFrete(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(" +
				"ttp.tpTipoTabelaPreco as tpTipoTabelaPreco, " +
				"ttp.nrVersao as nrVersao, " +
				"stp.tpSubtipoTabelaPreco as tpSubtipoTabelaPreco, " +
				"ds.tpCalculoPreco as tpCalculoPreco, " +
				"dc.dsDivisaoCliente as dsDivisaoCliente, " +
				"nvl2(filCot.sgFilial, filCot.sgFilial||' - '||cot.nrCotacao,'') as cotacao)");

		hql.addFrom(DoctoServico.class.getName()," ds " +
				"left outer join ds.pedidoColeta pc " +
				"left outer join pc.cotacao cot " +
				"left outer join cot.filial filCot " +
				"left outer join ds.divisaoCliente dc " +
				"left outer join ds.tabelaPreco tp " +
				"left outer join tp.tipoTabelaPreco ttp " +
				"left outer join tp.subtipoTabelaPreco stp ");

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findDadosCalculoDoctoServico(Long idDoctoServico){
		SqlTemplate hqlConhecimento = new SqlTemplate();
		hqlConhecimento.addProjection("new Map(conh.vlMercadoria as vlMercadoria, " +
				"conh.psReal as psReal, " +
				"conh.psAferido as psAferido, " +
				"conh.nrFatorCubagem as nrFatorCubagem, " +
				"conh.tpPesoCalculo as tpPesoCalculo, " +
				"conh.blUtilizaPesoEdi as blUtilizaPesoEdi, " +
				"conh.nrFatorDensidade as nrFatorDensidade, " +
				"conh.nrFatorCubagemCliente as nrFatorCubagemCliente, " +
				"conh.nrFatorCubagemSegmento as nrFatorCubagemSegmento, " +
				"conh.nrCubagemEstatistica as nrCubagemEstatistica, " +
				"conh.psCubadoReal as psCubadoReal, " +
				"conh.psEstatistico as psEstatistico, " +
				"conh.nrCubagemDeclarada as nrCubagemDeclarada, " +
				"conh.nrCubagemAferida as nrCubagemAferida, " +
				"conh.psCubadoDeclarado as psCubadoDeclarado, " +
				"conh.psCubadoAferido as psCubadoAferido, " +
				"conh.blPesoCubadoPorDensidade as blPesoCubadoPorDensidade, " +
				"conh.blPesoFatPorCubadoAferido as blPesoFatPorCubadoAferido, " +
				"conh.idDoctoServico as idConhecimento, " +
				"conh.psAforado as psAforado, " +
				"conh.psReal as psReal, " +
				"conh.psReferenciaCalculo as psReferenciaCalculo, " +
				"conh.qtVolumes as qtVolumes, " +
				"m.dsSimbolo as dsSimbolo, " +
				"count(nfc.idNotaFiscalConhecimento) as qtNF)");

		hqlConhecimento.addFrom(Conhecimento.class.getName()+ " conh " +
				"left outer join conh.notaFiscalConhecimentos nfc " +
				"left outer join conh.moeda m ");

		hqlConhecimento.addGroupBy("conh.vlMercadoria");
		hqlConhecimento.addGroupBy("conh.psReal");
		hqlConhecimento.addGroupBy("conh.psAferido");
		hqlConhecimento.addGroupBy("conh.nrFatorCubagem");
		hqlConhecimento.addGroupBy("conh.tpPesoCalculo");
		hqlConhecimento.addGroupBy("conh.blUtilizaPesoEdi");
		hqlConhecimento.addGroupBy("conh.nrFatorDensidade");
		hqlConhecimento.addGroupBy("conh.nrFatorCubagemCliente");
		hqlConhecimento.addGroupBy("conh.nrFatorCubagemSegmento");
		hqlConhecimento.addGroupBy("conh.nrCubagemEstatistica");
		hqlConhecimento.addGroupBy("conh.psCubadoReal");
		hqlConhecimento.addGroupBy("conh.psEstatistico");
		hqlConhecimento.addGroupBy("conh.nrCubagemDeclarada");
		hqlConhecimento.addGroupBy("conh.nrCubagemAferida");
		hqlConhecimento.addGroupBy("conh.psCubadoDeclarado");
		hqlConhecimento.addGroupBy("conh.psCubadoAferido");
		hqlConhecimento.addGroupBy("conh.blPesoCubadoPorDensidade");
		hqlConhecimento.addGroupBy("conh.blPesoFatPorCubadoAferido");
		hqlConhecimento.addGroupBy("conh.idDoctoServico");
		hqlConhecimento.addGroupBy("conh.psAforado");
		hqlConhecimento.addGroupBy("conh.psReal");
		hqlConhecimento.addGroupBy("conh.psReferenciaCalculo");
		hqlConhecimento.addGroupBy("conh.qtVolumes");
		hqlConhecimento.addGroupBy("m.dsSimbolo");

		hqlConhecimento.addCriteria("conh.idDoctoServico","=",idDoctoServico);
		return getAdsmHibernateTemplate().find(hqlConhecimento.getSql(),hqlConhecimento.getCriteria());
	}

	public List findDadosCalculoDoctoServicoInternacional(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(conhI.vlMercadoria as vlMercadoriaI, " +
				"conhI.vlFreteExterno as vlFreteExterno, " +
				"conhI.psReal as psRealI, " +
				"conhI.psLiquido as psLiquido, " +
				"conhI.vlVolume as vlVolume, " +
				"conhI.idDoctoServico as idConhInter, " +
				"moeda.dsSimbolo as dsSimbolo)");

		hql.addFrom(CtoInternacional.class.getName()+ " conhI " +
				"join conhI.moeda moeda " );

		hql.addCriteria("conhI.idDoctoServico","=",idDoctoServico);
		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findValorReciboReembolso(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(rr.vlReembolso as vlMercadoriaReemb , " +
									"mo.dsSimbolo as dsSimbolo)");

		hql.addFrom(ReciboReembolso.class.getName()+ " rr " +
				"left outer join rr.doctoServicoByIdDoctoServReembolsado rrs " +
				"left outer join rrs.moeda mo " );

		hql.addCriteria("rrs.idDoctoServico","=",idDoctoServico);
		hql.addCriteria("rrs.tpDocumentoServico","=","CTR");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public List findValorServAdicional(Long idDoctoServico){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(sadc.vlMercadoria as vlMercadoriaReemb, mo.dsSimbolo  as dsSimbolo)");

		hql.addFrom(ServAdicionalDocServ.class.getName()+ " sadc " +
				"left outer join sadc.doctoServico  ds " +
				"join sadc.servicoAdicional sa " +
				"left outer join ds.moeda mo " );

		hql.addCriteria("ds.idDoctoServico","=",idDoctoServico);
		hql.addCriteria("ds.tpDocumentoServico","=","CTR");
		hql.addCustomCriteria("sa.idServicoAdicional = (Select sap.idServicoAdicional from ParcelaPreco pp join pp.servicoAdicional sap where upper(pp.cdParcelaPreco) = 'IDREEMBOLSO')");

		return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
	}

	public BigDecimal somaISSDoctoServico(Long idDoctoServico) {
		final StringBuilder sql = new StringBuilder(" select nvl(sum(ise.vl_imposto),0) as vl_imposto ");
		sql.append(" from imposto_servico ise ");
		sql.append(" left join conhecimento c on c.id_conhecimento = ise.id_conhecimento ");
		sql.append(" left join nota_fiscal_servico nfs on nfs.id_nota_fiscal_servico = ise.id_nota_fiscal_servico ");
		sql.append(" where (c.id_conhecimento = ").append(idDoctoServico).append(" or nfs.id_nota_fiscal_servico = ").append(idDoctoServico).append(") ");
		sql.append(" and ise.tp_imposto = '"+ConstantesExpedicao.CD_ISS+"' ");
		sql.append(" and ise.BL_RETENCAO_TOMADOR_SERVICO = 'S' ");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("vl_imposto",Hibernate.BIG_DECIMAL);
}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);
				return query.list();
			}
		};
		List list = getHibernateTemplate().executeFind(hcb);
		return ((list == null || list.isEmpty()) ? BigDecimal.ZERO : (BigDecimal) list.get(0));



	}

}	
