package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorServicoAdicional;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.GeneralidadeCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

public class CalculoParcelaServicoDAO extends AdsmDao {

	public ParcelaPreco findParcelaPreco(Long idTabelaPreco, String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		mountParcelaPrecoProjection(projectionList, "");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPrecoParcela.class, "tpp");
		dc.setProjection(projectionList);

		dc.createAlias("parcelaPreco", "pp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ParcelaPreco.class));

		return (ParcelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ParcelaPreco findParcelaPreco(String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		mountParcelaPrecoProjection(projectionList, "");

		DetachedCriteria dc = DetachedCriteria.forClass(ParcelaPreco.class, "pp");
		dc.setProjection(projectionList);

		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ParcelaPreco.class));

		return (ParcelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ParcelaPreco findParcelaPreco(Long idTabelaPreco, Long idParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		mountParcelaPrecoProjection(projectionList, "");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPrecoParcela.class, "tpp");
		dc.setProjection(projectionList);

		dc.createAlias("parcelaPreco", "pp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ParcelaPreco.class));

		return (ParcelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	protected void mountParcelaPrecoProjection(ProjectionList projectionList, String transformerPrefix) {
		projectionList.add(Projections.property("pp.idParcelaPreco"), transformerPrefix + "idParcelaPreco");
		projectionList.add(Projections.property("pp.cdParcelaPreco"), transformerPrefix + "cdParcelaPreco");
		projectionList.add(Projections.property("pp.nmParcelaPreco"), transformerPrefix + "nmParcelaPreco");
		projectionList.add(Projections.property("pp.dsParcelaPreco"), transformerPrefix + "dsParcelaPreco");
		projectionList.add(Projections.property("pp.tpPrecificacao"), transformerPrefix + "tpPrecificacao");
		projectionList.add(Projections.property("pp.tpIndicadorCalculo"), transformerPrefix + "tpIndicadorCalculo");
	}

	public ParcelaPreco findParcelaPrecoServicoAdicional(Long idTabelaPreco, Long idServicoAdicional,String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		mountParcelaPrecoProjection(projectionList, "");
		projectionList.add(Projections.property("sa.idServicoAdicional"), "servicoAdicional.idServicoAdicional");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPrecoParcela.class, "tpp");
		dc.setProjection(projectionList);

		dc.createAlias("parcelaPreco", "pp");
		dc.createAlias("pp.servicoAdicional", "sa");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("sa.idServicoAdicional", idServicoAdicional));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ParcelaPreco.class));

		return (ParcelaPreco)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ValorServicoAdicional findValorServicoAdicional(Long idTabelaPreco, Long idServicoAdicional, String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pp.idParcelaPreco"), "tabelaPrecoParcela.parcelaPreco.idParcelaPreco");
		projectionList.add(Projections.property("vsa.vlServico"), "vlServico");
		projectionList.add(Projections.property("vsa.vlMinimo"), "vlMinimo");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorServicoAdicional.class, "vsa");
		dc.setProjection(projectionList);

		dc.createAlias("vsa.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.tabelaPreco", "tp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("pp.servicoAdicional", "sa");

		dc.add(Restrictions.eq("tp.id", idTabelaPreco));
		dc.add(Restrictions.eq("sa.id", idServicoAdicional));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ValorServicoAdicional.class));

		return (ValorServicoAdicional) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ValorServicoAdicional findValorServicoAdicional(Long idTabelaPreco, String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("vsa.vlServico"), "vlServico");
		projectionList.add(Projections.property("vsa.vlMinimo"), "vlMinimo");
		mountParcelaPrecoProjection(projectionList, "tabelaPrecoParcela.parcelaPreco.");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorServicoAdicional.class, "vsa");
		dc.setProjection(projectionList);

		dc.createAlias("vsa.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.tabelaPreco", "tp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("pp.servicoAdicional", "sa");

		dc.add(Restrictions.eq("tp.id", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ValorServicoAdicional.class));

		return (ValorServicoAdicional) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ServicoAdicionalCliente findServicoAdicionalCliente(Long idDivisaoCliente, Long idParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sac.tpIndicador"), "tpIndicador");
		projectionList.add(Projections.property("sac.idServicoAdicionalCliente"), "idServicoAdicionalCliente");
		projectionList.add(Projections.property("sac.vlValor"), "vlValor");
		projectionList.add(Projections.property("sac.vlMinimo"), "vlMinimo");

		DetachedCriteria dc = DetachedCriteria.forClass(ServicoAdicionalCliente.class, "sac");
		dc.setProjection(projectionList);

		dc.createAlias("sac.tabelaDivisaoCliente", "tdc");
		dc.createAlias("tdc.divisaoCliente", "dc");
		dc.createAlias("sac.parcelaPreco", "pp");

		dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ServicoAdicionalCliente.class));

		return (ServicoAdicionalCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public ServicoAdicionalCliente findServicoAdicionalCliente(Long idDivisaoCliente, String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("sac.tpIndicador"), "tpIndicador");
		projectionList.add(Projections.property("sac.idServicoAdicionalCliente"), "idServicoAdicionalCliente");
		projectionList.add(Projections.property("sac.vlValor"), "vlValor");
		projectionList.add(Projections.property("sac.vlMinimo"), "vlMinimo");
		projectionList.add(Projections.property("sac.tpUnidMedidaCalcCobr"), "tpUnidMedidaCalcCobr");
		
		DetachedCriteria dc = DetachedCriteria.forClass(ServicoAdicionalCliente.class, "sac");
		dc.setProjection(projectionList);
		
		dc.createAlias("sac.tabelaDivisaoCliente", "tdc");
		dc.createAlias("tdc.divisaoCliente", "dc");
		dc.createAlias("sac.parcelaPreco", "pp");
		
		dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		
		dc.setResultTransformer(new AliasToBeanResultTransformer(ServicoAdicionalCliente.class));

		return (ServicoAdicionalCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
	}
	
	public Generalidade findGeneralidade(Long idTabelaPreco, String cdParcelaPreco) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("g.idGeneralidade"), "idGeneralidade");
		projectionList.add(Projections.property("g.vlGeneralidade"), "vlGeneralidade");
		projectionList.add(Projections.property("g.vlMinimo"), "vlMinimo");
		projectionList.add(Projections.property("g.psMinimo"), "psMinimo");
		mountParcelaPrecoProjection(projectionList, "tabelaPrecoParcela.parcelaPreco.");
		projectionList.add(Projections.property("pp.blEmbuteParcela"), "tabelaPrecoParcela.parcelaPreco.blEmbuteParcela");

		DetachedCriteria dc = DetachedCriteria.forClass(Generalidade.class, "g");
		dc.setProjection(projectionList);

		dc.createAlias("g.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("tpp.tabelaPreco", "tabp");

		dc.add(Restrictions.eq("tabp.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Generalidade.class));

		return (Generalidade) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Busca ValorFaixaProgressiva pela <code>Tarifa, ProdutoEspecifico ou RotaPreco<code> conforme informado
	 * @param idTabelaPreco
	 * @param idParcelaPreco
	 * @param psReferencia
	 * @param idProdutoEspecifico
	 * @param idRotaPreco
	 * @param idTarifaPreco
	 * @param dtVigenciaPromocao
	 * @return
	 */
	public ValorFaixaProgressiva findValorFaixaProgressivaEnquadrada(Long idTabelaPreco, Long idParcelaPreco, BigDecimal psReferencia,
			Long idProdutoEspecifico, Long idRotaPreco, Long idTarifaPreco, YearMonthDay dtVigenciaPromocao, Boolean blTarifaCharter) {
		BigDecimal vlReferencia = getValorReferenciaFaixaProgressiva(idTabelaPreco, idParcelaPreco, psReferencia, idProdutoEspecifico, idRotaPreco,
				idTarifaPreco, blTarifaCharter, Boolean.FALSE);

		return findValorFaixaProgressiva(idTabelaPreco, idParcelaPreco, idProdutoEspecifico, idRotaPreco, idTarifaPreco, dtVigenciaPromocao, blTarifaCharter, vlReferencia);
	}

	public ValorFaixaProgressiva findValorFaixaProgressivaEnquadradaMaxima(BigDecimal vlReferencia, Long idTabelaPreco, Long idParcelaPreco, Long idRotaPreco, YearMonthDay dtVigenciaPromocao) {
		return findValorFaixaProgressiva(idTabelaPreco, idParcelaPreco, null, idRotaPreco, null, dtVigenciaPromocao, false, vlReferencia);
	}
	
	private ValorFaixaProgressiva findValorFaixaProgressiva(Long idTabelaPreco, Long idParcelaPreco, Long idProdutoEspecifico, Long idRotaPreco,
			Long idTarifaPreco, YearMonthDay dtVigenciaPromocao, Boolean blTarifaCharter, BigDecimal vlReferencia) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("vfpa.vlFixo"), "vlFixo");
		projectionList.add(Projections.property("vfpa.nrFatorMultiplicacao"), "nrFatorMultiplicacao");
		projectionList.add(Projections.property("vfpa.pcDesconto"), "pcDesconto");
		projectionList.add(Projections.property("vfpa.pcTaxa"), "pcTaxa");		
		projectionList.add(Projections.property("vfpa.vlAcrescimo"), "vlAcrescimo");
		projectionList.add(Projections.property("vfpa.vlTaxaFixa"), "vlTaxaFixa");
		projectionList.add(Projections.property("vfpa.vlKmExtra"), "vlKmExtra");
		projectionList.add(Projections.property("fpa.idFaixaProgressiva"), "faixaProgressiva.idFaixaProgressiva");
		projectionList.add(Projections.property("fpa.vlFaixaProgressiva"), "faixaProgressiva.vlFaixaProgressiva");
		projectionList.add(Projections.property("fpa.tpIndicadorCalculo"), "faixaProgressiva.tpIndicadorCalculo");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfpa");
		dc.setProjection(projectionList);

		dc.createAlias("vfpa.faixaProgressiva", "fpa");
		dc.createAlias("fpa.tabelaPrecoParcela", "tpp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.id", idParcelaPreco));
		if(idRotaPreco != null) {
			dc.add(Restrictions.eq("vfpa.rotaPreco.id", idRotaPreco));
		}
		if(idTarifaPreco != null) {
			dc.add(Restrictions.eq("vfpa.tarifaPreco.id", idTarifaPreco));
		}
		
		if(idProdutoEspecifico != null) {
			dc.add(Restrictions.eq("fpa.produtoEspecifico.id", idProdutoEspecifico));
		}

		dc.add(Restrictions.eq("fpa.vlFaixaProgressiva", vlReferencia));
		if(dtVigenciaPromocao == null) {
			dc.add(Restrictions.isNull("dtVigenciaPromocaoInicial"));
			
			dc.add(Restrictions.sqlRestriction("(DT_VIGENCIA_PROMOCAO_FINAL is null or DT_VIGENCIA_PROMOCAO_FINAL = '"+JTDateTimeUtils.MAX_YEARMONTHDAY.toString()+"')"));
			
		} else {
			
			dc.add(Restrictions.sqlRestriction("DT_VIGENCIA_PROMOCAO_INICIAL <= '"+dtVigenciaPromocao.toString()+"'"));
			dc.add(Restrictions.sqlRestriction("DT_VIGENCIA_PROMOCAO_FINAL >= '"+JTDateTimeUtils.maxYmd(dtVigenciaPromocao).toString()+"'"));

		}

		if( Boolean.TRUE.equals(blTarifaCharter)) {
			dc.add(Restrictions.eq("vfpa.blPromocional", Boolean.TRUE));
		}

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ValorFaixaProgressiva.class));

		return (ValorFaixaProgressiva) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public BigDecimal getValorReferenciaFaixaProgressiva(
			Long idTabelaPreco,
			Long idParcelaPreco,
			BigDecimal psReferencia,
			Long idProdutoEspecifico,
			Long idRotaPreco,
			Long idTarifaPreco,
			Boolean blTarifaCharter,
			Boolean isFindMax
	) {
		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");

		if(Boolean.TRUE.equals(isFindMax)){
			dc.setProjection(Projections.max("fp.vlFaixaProgressiva"));	
		} else {
			dc.setProjection(Projections.min("fp.vlFaixaProgressiva"));
		}

		dc.createAlias("vfp.faixaProgressiva", "fp");
		dc.createAlias("fp.tabelaPrecoParcela", "tpp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.id", idParcelaPreco));
		
		if(psReferencia != null){
			dc.add(Restrictions.ge("fp.vlFaixaProgressiva", psReferencia));
		}
		
		if(idRotaPreco != null) {
			dc.add(Restrictions.eq("vfp.rotaPreco.id", idRotaPreco));
		}
		if(idTarifaPreco != null) {
			dc.add(Restrictions.eq("vfp.tarifaPreco.id", idTarifaPreco));
		}
		if(idProdutoEspecifico != null) {
			dc.add(Restrictions.eq("fp.produtoEspecifico.id", idProdutoEspecifico));
		}
		if( Boolean.TRUE.equals(blTarifaCharter)) {
			dc.add(Restrictions.eq("vfp.blPromocional", Boolean.TRUE));
		}

		return (BigDecimal) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public GeneralidadeCliente findGeneralidadeCliente(Long idTabelaPreco, Long idParcelaPreco, Long idDivisaoCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		GeneralidadeCliente generalidadeCliente = null;

		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("gc.tpIndicador"), "tpIndicador")
			.add(Projections.property("gc.tpIndicadorMinimo"), "tpIndicadorMinimo")
			.add(Projections.property("gc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("gc.vlMinimo"), "vlMinimo")
			.add(Projections.property("gc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("gc.pcReajMinimo"), "pcReajMinimo");

		DetachedCriteria dc = DetachedCriteria.forClass(GeneralidadeCliente.class, "gc");
		dc.setProjection(projectionList);

		dc.createAlias("gc.parametroCliente", "pc");
		dc.createAlias("pc.tabelaDivisaoCliente", "tdc");
		dc.createAlias("gc.parcelaPreco", "pp");

		if(idTabelaPreco != null){
		dc.add(Restrictions.eq("tdc.tabelaPreco.idTabelaPreco", idTabelaPreco));
		}
		
		dc.add(Restrictions.eq("tdc.divisaoCliente.idDivisaoCliente", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.idServico", idServico));
		dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));

		ParametroClienteUtils.addParametroClienteRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);

		dc.setResultTransformer(new AliasToBeanResultTransformer(GeneralidadeCliente.class));

		List<GeneralidadeCliente> result = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE).getList();
		if(!result.isEmpty()) {
			generalidadeCliente = (GeneralidadeCliente) result.get(0);
		}
		return generalidadeCliente;
	}

	public GeneralidadeCliente findGeneralidadeClienteByParametro(Long idParametroCliente, Long idParcelaPreco) {
		GeneralidadeCliente generalidadeCliente = null;

		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("gc.tpIndicador"), "tpIndicador")
			.add(Projections.property("gc.tpIndicadorMinimo"), "tpIndicadorMinimo")
			.add(Projections.property("gc.vlGeneralidade"), "vlGeneralidade")
			.add(Projections.property("gc.vlMinimo"), "vlMinimo")
			.add(Projections.property("gc.idGeneralidadeCliente"), "idGeneralidadeCliente")
			.add(Projections.property("gc.pcReajMinimo"), "pcReajMinimo");

		DetachedCriteria dc = DetachedCriteria.forClass(GeneralidadeCliente.class, "gc");
		dc.setProjection(projectionList);

		dc.createAlias("gc.parametroCliente", "pc");
		dc.createAlias("gc.parcelaPreco", "pp");

		dc.add(Restrictions.eq("pc.idParametroCliente", idParametroCliente));
		dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(GeneralidadeCliente.class));
		generalidadeCliente = (GeneralidadeCliente) getAdsmHibernateTemplate().findUniqueResult(dc);
		return generalidadeCliente;
	}
}
