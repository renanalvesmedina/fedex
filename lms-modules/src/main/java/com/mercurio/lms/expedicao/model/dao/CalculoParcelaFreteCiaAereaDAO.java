package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.tabelaprecos.model.PrecoFrete;
import com.mercurio.lms.tabelaprecos.model.RotaPreco;
import com.mercurio.lms.tabelaprecos.model.ValorFaixaProgressiva;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @author Claiton Grings 
 * @spring.bean id="lms.expedicao.calculoParcelaFreteCiaAereaDAO"
 */
public class CalculoParcelaFreteCiaAereaDAO extends CalculoParcelaServicoDAO {

	public RotaPreco findRotaPrecoFretePeso(Long idTabelaPreco, Long idParcelaPreco, Long idAeroportoOrigem, Long idAeroportoDestino) {
		RotaPreco rotaPreco = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("rp.idRotaPreco"), "idRotaPreco");
		projectionList.add(Projections.property("rp.aeroportoByIdAeroportoOrigem.idAeroporto"), "aeroportoByIdAeroportoOrigem.idAeroporto");
		projectionList.add(Projections.property("rp.aeroportoByIdAeroportoDestino.idAeroporto"), "aeroportoByIdAeroportoDestino.idAeroporto");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.setProjection(projectionList);

		dc.createAlias("vfp.faixaProgressiva", "fp");
		dc.createAlias("fp.tabelaPrecoParcela", "tpp");
		dc.createAlias("vfp.rotaPreco", "rp");
		dc.createAlias("rp.aeroportoByIdAeroportoOrigem", "ao");
		dc.createAlias("rp.aeroportoByIdAeroportoDestino", "ad");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Restrictions.eq("ao.idAeroporto", idAeroportoOrigem));
		dc.add(Restrictions.eq("ad.idAeroporto", idAeroportoDestino));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(RotaPreco.class));

		List<RotaPreco> result = findByDetachedCriteria(dc);
		if(result.size() > 0) {
			rotaPreco = result.get(0);
		}
		return rotaPreco;
	}

	public RotaPreco findRotaPrecoTaxaMinima(Long idTabelaPreco, Long idParcelaPreco, Long idAeroportoOrigem, Long idAeroportoDestino,Long idUFOrigem,Long idUFDestino) {
		RotaPreco rotaPreco = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("rp.idRotaPreco"), "idRotaPreco");
		projectionList.add(Projections.property("rp.aeroportoByIdAeroportoOrigem.idAeroporto"), "aeroportoByIdAeroportoOrigem.idAeroporto");
		projectionList.add(Projections.property("rp.aeroportoByIdAeroportoDestino.idAeroporto"), "aeroportoByIdAeroportoDestino.idAeroporto");

		DetachedCriteria dc = DetachedCriteria.forClass(PrecoFrete.class, "pf");
		dc.setProjection(projectionList);
		dc.createAlias("pf.tabelaPrecoParcela", "tpp");
		dc.createAlias("pf.rotaPreco", "rp");
		dc.createAlias("rp.aeroportoByIdAeroportoOrigem", "ao");
		dc.createAlias("rp.aeroportoByIdAeroportoDestino", "ad");

		dc.createAlias("rp.unidadeFederativaByIdUfOrigem", "ufo");
		dc.createAlias("rp.unidadeFederativaByIdUfDestino", "ufd");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Restrictions.eq("ao.idAeroporto", idAeroportoOrigem));
		dc.add(Restrictions.eq("ad.idAeroporto", idAeroportoDestino));

		//acrecentado
		dc.add(Restrictions.eq("ufo.idUnidadeFederativa", idUFOrigem));
		//acrecentado
		dc.add(Restrictions.eq("ufd.idUnidadeFederativa", idUFDestino));
		
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(RotaPreco.class));

		return (RotaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public RotaPreco findRotaPrecoTaxaCombustivel(Long idTabelaPreco, Long idParcelaPreco, Long idAeroportoOrigem, Long idUFOrigem, Long idAeroportoDestino, Long idUFDestino) {
		RotaPreco rotaPreco = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("rp.idRotaPreco"), "idRotaPreco");
		projectionList.add(Projections.property("rp.aeroportoByIdAeroportoOrigem.idAeroporto"), "aeroportoByIdAeroportoOrigem.idAeroporto");
		projectionList.add(Projections.property("rp.aeroportoByIdAeroportoDestino.idAeroporto"), "aeroportoByIdAeroportoDestino.idAeroporto");

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.setProjection(projectionList);
		dc.createAlias("vfp.faixaProgressiva", "fp");
		dc.createAlias("fp.tabelaPrecoParcela", "tpp");
		dc.createAlias("vfp.rotaPreco", "rp");
		dc.createAlias("rp.unidadeFederativaByIdUfOrigem", "ufo");
		dc.createAlias("rp.unidadeFederativaByIdUfDestino", "ufd");
		
		if (idAeroportoOrigem != null)
		dc.createAlias("rp.aeroportoByIdAeroportoOrigem", "ao");
		
		if (idAeroportoDestino != null)
		dc.createAlias("rp.aeroportoByIdAeroportoDestino", "ad");
			
		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Restrictions.eq("ufo.idUnidadeFederativa", idUFOrigem));
		if(idAeroportoOrigem == null) {
			// Id de Aereoporto nunca será null, pois é chave primária
			//dc.add(Restrictions.isNull("ao.idAeroporto"));
		} else {
			dc.add(Restrictions.eq("ao.idAeroporto", idAeroportoOrigem));
		}
		dc.add(Restrictions.eq("ufd.idUnidadeFederativa", idUFDestino));
		if(idAeroportoDestino == null) {
			//Id de Aereoporto nunca será null, pois é chave primária
			//dc.add(Restrictions.isNull("ad.idAeroporto"));
		} else {
			dc.add(Restrictions.eq("ad.idAeroporto", idAeroportoDestino));
		}

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(RotaPreco.class));

		List list = getAdsmHibernateTemplate().findByCriteria(dc);
		if (list != null && !list.isEmpty()){
			return (RotaPreco) list.get(0);
	}
		return null;
	}

	public PrecoFrete findPrecoFrete(Long idTabelaPreco, Long idParcelaPreco, Long idRotaPreco) {
		PrecoFrete precoFrete = null;

		DetachedCriteria dc = DetachedCriteria.forClass(PrecoFrete.class, "pf");
		dc.setProjection(Projections.alias(Projections.property("pf.vlPrecoFrete"), "vlPrecoFrete"));

		dc.createAlias("pf.tabelaPrecoParcela", "tpp");

		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Restrictions.eq("rotaPreco.idRotaPreco", idRotaPreco));

		dc.setResultTransformer(new AliasToBeanResultTransformer(PrecoFrete.class));

		return (PrecoFrete) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Busca ValorFaixaProgressiva pelo idFaixaProgressiva
	 * @param idFaixaProgressiva
	 * @return
	 */
	public ValorFaixaProgressiva findValorFaixaProgressiva(Long idFaixaProgressiva) {
		ValorFaixaProgressiva valorFaixaProgressiva = null;

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.setProjection(Projections.alias(Projections.property("vfp.vlFixo"), "vlFixo"));

		dc.add(Expression.eq("vfp.faixaProgressiva.idFaixaProgressiva", idFaixaProgressiva));
		dc.setResultTransformer(new AliasToBeanResultTransformer(ValorFaixaProgressiva.class));

		return (ValorFaixaProgressiva) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Busca ValorFaixaProgressiva por ProdutoEspecifico e RotaPreco
	 * @param idTabelaPreco
	 * @param idParcelaPreco
	 * @param idProdutoEspecifico
	 * @param idRotaPreco
	 * @return
	 */
	public ValorFaixaProgressiva findValorFaixaProgressivaProdutoEspecifico(Long idTabelaPreco, Long idParcelaPreco, Long idProdutoEspecifico, Long idRotaPreco) {
		ValorFaixaProgressiva valorFaixaProgressiva = null;

		DetachedCriteria dc = DetachedCriteria.forClass(ValorFaixaProgressiva.class, "vfp");
		dc.createAlias("vfp.faixaProgressiva", "fp");
		dc.createAlias("fp.tabelaPrecoParcela", "tpp");
		dc.createAlias("fp.produtoEspecifico", "pe");
		dc.createAlias("vfp.rotaPreco", "rp");
		dc.add(Restrictions.eq("tpp.tabelaPreco.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tpp.parcelaPreco.idParcelaPreco", idParcelaPreco));
		dc.add(Expression.eq("pe.idProdutoEspecifico", idProdutoEspecifico));
		dc.add(Expression.eq("rp.idRotaPreco", idRotaPreco));

		return (ValorFaixaProgressiva) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public FilialCiaAerea findFilialCiaAerea(Long idAeroporto, Long idEmpresa) {
		FilialCiaAerea filialCiaAerea = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("fca.blTaxaTerrestre"), "blTaxaTerrestre");

		DetachedCriteria dc = DetachedCriteria.forClass(FilialCiaAerea.class, "fca");
		dc.setProjection(projectionList);

		dc.add(Restrictions.eq("fca.aeroporto.idAeroporto", idAeroporto));
		dc.add(Restrictions.eq("fca.empresa.idEmpresa", idEmpresa));
		
		dc.add(Restrictions.le("fca.dtVigenciaInicial",JTDateTimeUtils.getDataAtual()));
		dc.add(Restrictions.ge("fca.dtVigenciaFinal",JTDateTimeUtils.getDataAtual()));
		
		dc.setResultTransformer(new AliasToBeanResultTransformer(FilialCiaAerea.class));

		return (FilialCiaAerea) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}
