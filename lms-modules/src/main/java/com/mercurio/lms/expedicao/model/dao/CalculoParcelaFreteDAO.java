package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.FaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.Generalidade;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.tabelaprecos.model.ValorTaxa;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.tributos.model.TaxaSuframa;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.TaxaCliente;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean id="lms.expedicao.calculoParcelaFreteDAO"
 */
public class CalculoParcelaFreteDAO extends CalculoParcelaServicoDAO {

	public ValorFaixaProgressiva findValorFaixaProgressivaRotaPreco(String cdParcelaPreco, Long idTabelaPreco, Long idParcelaPreco, Long idFaixaProgressiva, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		ValorFaixaProgressiva valorFaixaProgressiva = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("vfp.vlFixo"), "vlFixo");
		
		/*Regra solicitada pelo Eri em 09/10/2009 para que a parcela Taxa Terrestre
		não adicione restrições de rotapreço*/
		if(!ConstantesExpedicao.CD_TAXA_TERRESTRE.equals(cdParcelaPreco)){
		projectionList.add(Projections.property("rp.idRotaPreco"), "rotaPreco.idRotaPreco");
		}

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.setProjection(projectionList);

		if(!ConstantesExpedicao.CD_TAXA_TERRESTRE.equals(cdParcelaPreco)){
		dc.createAlias("vfp.rotaPreco", "rp");
		}
		dc.createAlias("vfp.faixaProgressiva", "fp");
		dc.createAlias("fp.tabelaPrecoParcela", "tpp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco",  idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Restrictions.eq("fp.idFaixaProgressiva", idFaixaProgressiva));

		if(!ConstantesExpedicao.CD_TAXA_TERRESTRE.equals(cdParcelaPreco)){
		RotaPrecoUtils.addRotaPrecoRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);
		}

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ValorFaixaProgressiva.class));

		List<ValorFaixaProgressiva> result = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE).getList();
		if(!result.isEmpty()) {
			valorFaixaProgressiva = result.get(0);
		}
		return valorFaixaProgressiva;
	}

	public ValorFaixaProgressiva findValorFaixaProgressiva(
			Long idFaixaProgressiva,
			Long idRotaPreco,
			Long idTarifaPreco,
			Long idProdutoEspecifico,
			YearMonthDay dtVigenciaPromocao
	) {
		ValorFaixaProgressiva valorFaixaProgressiva = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("vfp.vlFixo"), "vlFixo");
		projectionList.add(Projections.property("vfp.nrFatorMultiplicacao"), "nrFatorMultiplicacao");
		projectionList.add(Projections.property("vfp.pcDesconto"), "pcDesconto");
		projectionList.add(Projections.property("vfp.vlAcrescimo"), "vlAcrescimo");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.setProjection(projectionList);

		dc.createAlias("vfp.faixaProgressiva", "fp");

		if(idFaixaProgressiva != null)
			dc.add(Restrictions.eq("fp.idFaixaProgressiva", idFaixaProgressiva));
		if(idProdutoEspecifico != null)
			dc.add(Restrictions.eq("fp.produtoEspecifico.idProdutoEspecifico", idProdutoEspecifico));
		if(idRotaPreco != null)
			dc.add(Restrictions.eq("vfp.rotaPreco.idRotaPreco", idRotaPreco));
		if(idTarifaPreco != null)
			dc.add(Restrictions.eq("vfp.tarifaPreco.idTarifaPreco", idTarifaPreco));
		if(dtVigenciaPromocao == null) {
			dc.add(Restrictions.isNull("dtVigenciaPromocaoInicial"));
			dc.add(Restrictions.or(
					Restrictions.eq("dtVigenciaPromocaoFinal", JTDateTimeUtils.MAX_YEARMONTHDAY),
					Restrictions.isNull("dtVigenciaPromocaoFinal")
				)
			);
		} else {
			dc.add(Restrictions.le("dtVigenciaPromocaoInicial", dtVigenciaPromocao));
			dc.add(Restrictions.ge("dtVigenciaPromocaoFinal", dtVigenciaPromocao));
		}

		dc.setResultTransformer(new AliasToBeanResultTransformer(ValorFaixaProgressiva.class));

		List<ValorFaixaProgressiva> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			valorFaixaProgressiva = result.get(0);
		}
		return valorFaixaProgressiva;
	}

	/**
	 * Localiza ValorFaixaProgressiva pela Rota e Produto Específico
	 * @param idTabelaPreco
	 * @param idParcelaPreco
	 * @param idRotaPreco
	 * @param idProdutoEspecifico
	 * @return ValorFaixaProgressiva
	 */
	public ValorFaixaProgressiva findValorFaixaProgressiva(
			Long idTabelaPreco,
			Long idParcelaPreco,
			Long idRotaPreco,
			Long idProdutoEspecifico
	) {
		ValorFaixaProgressiva valorFaixaProgressiva = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("vfp.vlFixo"), "vlFixo");
		projectionList.add(Projections.property("vfp.nrFatorMultiplicacao"), "nrFatorMultiplicacao");
		projectionList.add(Projections.property("vfp.pcDesconto"), "pcDesconto");
		projectionList.add(Projections.property("vfp.vlAcrescimo"), "vlAcrescimo");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.setProjection(projectionList);

		dc.createAlias("vfp.faixaProgressiva", "fp");
		dc.createAlias("fp.tabelaPrecoParcela", "tpp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.id", idParcelaPreco));
		dc.add(Restrictions.eq("fp.produtoEspecifico.idProdutoEspecifico", idProdutoEspecifico));
		dc.add(Restrictions.eq("vfp.rotaPreco.idRotaPreco", idRotaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ValorFaixaProgressiva.class));

		List<ValorFaixaProgressiva> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			valorFaixaProgressiva = result.get(0);
		}
		return valorFaixaProgressiva;
	}

	public TaxaCliente findTaxaCliente(Long idTabelaPreco, Long idParcelaPreco, Long idDivisaoCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		TaxaCliente taxaCliente = null;

		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("tc.tpTaxaIndicador"), "tpTaxaIndicador")
			.add(Projections.property("tc.psMinimo"), "psMinimo")
			.add(Projections.property("tc.vlTaxa"), "vlTaxa")
			.add(Projections.property("tc.vlExcedente"), "vlExcedente")
			.add(Projections.property("tc.idTaxaCliente"), "idTaxaCliente");
		DetachedCriteria dc = DetachedCriteria.forClass(TaxaCliente.class, "tc");
		dc.setProjection(projectionList);

		dc.createAlias("tc.parametroCliente", "pc");
		dc.createAlias("pc.tabelaDivisaoCliente", "tdc");
		dc.createAlias("tc.parcelaPreco", "pp");
		
		dc.add(Restrictions.eq("tdc.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tdc.divisaoCliente.idDivisaoCliente", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.idServico", idServico));
		dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));

		ParametroClienteUtils.addParametroClienteRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);

		dc.setResultTransformer(new AliasToBeanResultTransformer(TaxaCliente.class));

		List<TaxaCliente> result = findPaginatedByDetachedCriteria(dc, IntegerUtils.ONE, IntegerUtils.ONE).getList();
		if(!result.isEmpty()) {
			taxaCliente = result.get(0);
		}
		return taxaCliente;
	}

	public TaxaCliente findTaxaClienteByParametro(Long idParametroCliente, Long idParcelaPreco) {
		TaxaCliente taxaCliente = null;

		ProjectionList projectionList = Projections.projectionList()
			.add(Projections.property("tc.tpTaxaIndicador"), "tpTaxaIndicador")
			.add(Projections.property("tc.psMinimo"), "psMinimo")
			.add(Projections.property("tc.vlTaxa"), "vlTaxa")
			.add(Projections.property("tc.vlExcedente"), "vlExcedente")
			.add(Projections.property("tc.idTaxaCliente"), "idTaxaCliente");
		DetachedCriteria dc = DetachedCriteria.forClass(TaxaCliente.class, "tc");
		dc.setProjection(projectionList);

		dc.createAlias("tc.parametroCliente", "pc");
		dc.createAlias("tc.parcelaPreco", "pp");
		
		dc.add(Restrictions.eq("pc.idParametroCliente", idParametroCliente));
		dc.add(Restrictions.eq("pp.idParcelaPreco", idParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(TaxaCliente.class));

		return (TaxaCliente)getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public PrecoFrete findFretePeso(Long idTabelaPreco, String cdParcelaPreco, Long idTarifaPreco, Long idRotaPreco) {
		PrecoFrete precoFrete = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pf.idPrecoFrete"), "idPrecoFrete");
		projectionList.add(Projections.property("pf.vlPrecoFrete"), "vlPrecoFrete");
		projectionList.add(Projections.property("pp.idParcelaPreco"), "tabelaPrecoParcela.parcelaPreco.idParcelaPreco");
		projectionList.add(Projections.property("pp.cdParcelaPreco"), "tabelaPrecoParcela.parcelaPreco.cdParcelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(PrecoFrete.class, "pf");
		dc.setProjection(projectionList);

		dc.createAlias("pf.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("tpp.tabelaPreco", "tabp");

		dc.add(Restrictions.eq("tabp.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		if(idTarifaPreco != null)
			dc.add(Restrictions.eq("pf.tarifaPreco.idTarifaPreco", idTarifaPreco));
		if(idRotaPreco != null)
			dc.add(Restrictions.eq("pf.rotaPreco.idTarifaPreco", idRotaPreco));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(PrecoFrete.class));

		List<PrecoFrete> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			precoFrete = result.get(0);
		}
		return precoFrete;
	}

	public BigDecimal findValorFretePesoMenor(Long idTabelaPreco, String cdParcelaPreco) {

		DetachedCriteria dc = DetachedCriteria.forClass(PrecoFrete.class, "pf");
		dc.setProjection(Projections.min("pf.vlPrecoFrete"));

		dc.createAlias("pf.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.tabelaPreco", "tp");
		dc.createAlias("tpp.parcelaPreco", "pp");

		dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		dc.add(Restrictions.isNotNull("pf.vlPrecoFrete"));

		List<BigDecimal> result = findByDetachedCriteria(dc);
		return result.get(0);
	}
	
	public BigDecimal findMenorPeso(Long idTabelaPreco, Long idTarifaPreco, String cdParcelaPreco) {
		DetachedCriteria dc = DetachedCriteria.forClass(PrecoFrete.class, "pf");
		dc.setProjection(Projections.min("pf.pesoMinimo"));

		dc.createAlias("pf.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.tabelaPreco", "tp");
		dc.createAlias("tpp.parcelaPreco", "pp");

		dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pf.tarifaPreco.idTarifaPreco", idTarifaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		dc.add(Restrictions.isNotNull("pf.vlPrecoFrete"));

		List<BigDecimal> result = findByDetachedCriteria(dc);
		return result.get(0);
	}

	public PrecoFrete findPrecoFrete(Long idTabelaPreco, String cdParcelaPreco, Long idRotaPreco) {
		PrecoFrete precoFrete = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pf.vlPrecoFrete"), "vlPrecoFrete");

		DetachedCriteria dc = DetachedCriteria.forClass(PrecoFrete.class, "pf");
		dc.setProjection(projectionList);

		dc.createAlias("pf.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.tabelaPreco", "tp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("pf.rotaPreco", "rp");

		dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));
		dc.add(Restrictions.eq("rp.idRotaPreco", idRotaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(PrecoFrete.class));

		List<PrecoFrete> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			precoFrete = result.get(0);
		}
		return precoFrete;
	}

	public List<Generalidade> findGeneralidades(Long idTabelaPreco, String[] cdsParcelaPreco) {

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

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("pp.tpParcelaPreco", "G"));
		dc.add(Restrictions.not(Restrictions.in("pp.cdParcelaPreco", cdsParcelaPreco)));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Generalidade.class));

		return findByDetachedCriteria(dc);
	}

	public ValorTaxa findValorTaxa(Long idTabelaPreco, String cdParcelaPreco) {
		ValorTaxa valorTaxa = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("vt.idValorTaxa"), "idValorTaxa");
		projectionList.add(Projections.property("vt.vlTaxa"), "vlTaxa");
		projectionList.add(Projections.property("vt.psTaxado"), "psTaxado");
		projectionList.add(Projections.property("vt.vlExcedente"), "vlExcedente");
		mountParcelaPrecoProjection(projectionList, "tabelaPrecoParcela.parcelaPreco.");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorTaxa.class, "vt");
		dc.setProjection(projectionList);

		dc.createAlias("vt.tabelaPrecoParcela", "tpp");
		dc.createAlias("tpp.parcelaPreco", "pp");
		dc.createAlias("tpp.tabelaPreco", "tabp");

		dc.add(Restrictions.eq("tabp.id", idTabelaPreco));
		dc.add(Restrictions.eq("pp.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ValorTaxa.class));

		List<ValorTaxa> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			valorTaxa = result.get(0);
		}
		return valorTaxa;
	}

	public TaxaSuframa findTaxaSuframa(BigDecimal vlMercadoria) {
		TaxaSuframa taxaSuframa = null;
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		DetachedCriteria dc = DetachedCriteria.forClass(TaxaSuframa.class, "ts");

		dc.add(Restrictions.le("ts.vlMercadoriaInicial", vlMercadoria));
		dc.add(Restrictions.ge("ts.vlMercadoriaFinal", vlMercadoria));

		dc.add(Restrictions.le("ts.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("ts.dtVigenciaFinal", dtVigencia));

		List<TaxaSuframa> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			taxaSuframa = result.get(0);
		}
		return taxaSuframa;
	}

	public ParcelaDoctoServico findParcelaDoctoServico(Long idDoctoServico, String cdParcelaPreco) {
		ParcelaDoctoServico parcelaDoctoServico = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pds.idParcelaDoctoServico"), "idParcelaDoctoServico");
		projectionList.add(Projections.property("pds.vlParcela"), "vlParcela");

		DetachedCriteria dc = DetachedCriteria.forClass(ParcelaDoctoServico.class, "pds");
		dc.setProjection(projectionList);

		dc.add(Restrictions.eq("pds.doctoServico.idDoctoServico", idDoctoServico));
		dc.add(Restrictions.eq("pds.parcelaPreco.cdParcelaPreco", cdParcelaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(ParcelaDoctoServico.class));

		List<ParcelaDoctoServico> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			parcelaDoctoServico = result.get(0);
		}
		return parcelaDoctoServico;
	}

	public List<ParcelaDoctoServico> findParcelasDoctoServico(Long idDoctoServico) {
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("pds.vlParcela"), "vlParcela");
		mountParcelaPrecoProjection(projectionList, "parcelaPreco.");

		DetachedCriteria dc = DetachedCriteria.forClass(ParcelaDoctoServico.class, "pds");
		dc.setProjection(projectionList);

		dc.createAlias("pds.parcelaPreco", "pp");

		dc.add(Restrictions.eq("pds.doctoServico.idDoctoServico", idDoctoServico));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(ParcelaDoctoServico.class));

		return findByDetachedCriteria(dc);
	}

	public Integer findCountFaixaProgressiva(Long idTabelaPreco, Long idParcelaPreco, BigDecimal psMinimo) {
		DetachedCriteria dc = DetachedCriteria.forClass(FaixaProgressiva.class, "fp");
		dc.setProjection(Projections.count("fp.idFaixaProgressiva"));

		dc.createAlias("fp.tabelaPrecoParcela", "tpp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.id", idParcelaPreco));
		dc.add(Restrictions.gt("fp.vlFaixaProgressiva", psMinimo));

		List<Integer> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			return result.get(0);
		}
		return IntegerUtils.ZERO;
	}
	
	public String findVlMercadoriaNatura(Long idCliente){
		String query = "select vcc.vlValor from ValorCampoComplementar vcc " +
				"join vcc.campoComplementar cc " +
				"where cc.nmCampoComplementar = ? " +
				"and vcc.cliente.id = ?";		

		String vlMercadoria = (String) getAdsmHibernateTemplate().findUniqueResult(query, new Object[]{"Valor Mercadoria Natura",idCliente});
		
		return vlMercadoria;
	}
	
	public Integer findQtdPostoPassagemEntreFiliais(Long idFilialOrigem, Long idFilialDestino, Long idServico, YearMonthDay dtConsulta){
		if( dtConsulta == null ){
			dtConsulta = JTDateTimeUtils.getDataAtual();
		}
		SQLQuery sql = getSession().createSQLQuery("select F_RETORNA_PEDAGIOS_FILIAIS(:idFilialOrigem,:idFilialDestino,sysdate,:idServico) from dual");
		sql.setLong("idFilialOrigem",idFilialOrigem);
		sql.setLong("idFilialDestino",idFilialDestino);
		sql.setLong("idServico",idServico);
		return Integer.valueOf(sql.uniqueResult().toString());
	}
}