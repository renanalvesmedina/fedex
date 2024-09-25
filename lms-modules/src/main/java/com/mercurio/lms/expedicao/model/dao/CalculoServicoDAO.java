package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

public class CalculoServicoDAO extends AdsmDao {

	public TabelaPreco findTabelaPreco(Long idServico, String tpTipoTabelaPreco, String tpSubtipoTabelaPreco) {
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = getTabelaPrecoProjection();

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPreco.class, "tp");
		dc.setProjection(projectionList);

		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");
		dc.createAlias("tp.moeda", "m");

		/*CQPRO00025223*/
		if("M".equals(tpTipoTabelaPreco)){			
			dc.add(Restrictions.or(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco), Restrictions.eq("ttp.tpTipoTabelaPreco", "T")));
		}else{
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", tpTipoTabelaPreco));
		}
		
		dc.add(Restrictions.eq("ttp.servico.idServico", idServico));
		dc.add(Restrictions.eq("stp.tpSubtipoTabelaPreco", tpSubtipoTabelaPreco));
		dc.add(Restrictions.eq("tp.blEfetivada", Boolean.TRUE));
		dc.add(Restrictions.le("tp.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("tp.dtVigenciaFinal", dtVigencia));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(TabelaPreco.class));
		return (TabelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
                
	}

	protected static final ProjectionList getTabelaPrecoProjection() {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tp.idTabelaPreco"), "idTabelaPreco");
		projectionList.add(Projections.property("tp.psMinimo"), "psMinimo");
		projectionList.add(Projections.property("tp.dsDescricao"), "dsDescricao");
		projectionList.add(Projections.property("tp.blIcmsDestacado"), "blIcmsDestacado");
		projectionList.add(Projections.property("tp.tpCalculoFretePeso"), "tpCalculoFretePeso");
		projectionList.add(Projections.property("tp.tpCalculoPedagio"), "tpCalculoPedagio");		
		projectionList.add(Projections.property("tp.dtVigenciaInicial"), "dtVigenciaInicial");		
		projectionList.add(Projections.property("ttp.tpTipoTabelaPreco"), "tipoTabelaPreco.tpTipoTabelaPreco");
		projectionList.add(Projections.property("ttp.nrVersao"), "tipoTabelaPreco.nrVersao");
		projectionList.add(Projections.property("stp.tpSubtipoTabelaPreco"), "subtipoTabelaPreco.tpSubtipoTabelaPreco");
		projectionList.add(Projections.property("stp.idSubtipoTabelaPreco"), "subtipoTabelaPreco.idSubtipoTabelaPreco");
		projectionList.add(Projections.property("m.idMoeda"), "moeda.idMoeda");
		projectionList.add(Projections.property("m.sgMoeda"), "moeda.sgMoeda");
		projectionList.add(Projections.property("m.dsMoeda"), "moeda.dsMoeda");
		projectionList.add(Projections.property("m.dsSimbolo"), "moeda.dsSimbolo");
		return projectionList;
	}

	public Cliente findCliente(Long idCliente) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("c.idCliente"), "idCliente");
		projectionList.add(Projections.property("c.tpCliente"), "tpCliente");
		projectionList.add(Projections.property("p.tpPessoa"), "pessoa.tpPessoa");
		projectionList.add(Projections.property("c.tpFormaArredondamento"), "tpFormaArredondamento");
		projectionList.add(Projections.property("c.tpPesoCalculo"), "tpPesoCalculo");		
		projectionList.add(Projections.property("c.nrCasasDecimaisPeso"), "nrCasasDecimaisPeso");
		projectionList.add(Projections.property("c.blPesoAforadoPedagio"), "blPesoAforadoPedagio");
		projectionList.add(Projections.property("c.segmentoMercado"), "segmentoMercado");

		DetachedCriteria dc = DetachedCriteria.forClass(Cliente.class, "c");
		dc.setProjection(projectionList);

		dc.createAlias("c.pessoa", "p");

		dc.add(Restrictions.eq("c.idCliente", idCliente));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Cliente.class));

		return (Cliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}
