package com.mercurio.lms.expedicao.model.dao;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean id="lms.expedicao.calculoNFServicoDAO"
 */
public class CalculoNFServicoDAO extends CalculoServicoDAO {


	public TabelaPreco findTabelaPreco(Long idDivisaoCliente, Long idServico, Long idParcelaPreco, Long idCotacao) {
		TabelaPreco tabelaPreco = null;
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tp.idTabelaPreco"), "idTabelaPreco");
		projectionList.add(Projections.property("tp.psMinimo"), "psMinimo");
		projectionList.add(Projections.property("ttp.tpTipoTabelaPreco"), "tipoTabelaPreco.tpTipoTabelaPreco");
		projectionList.add(Projections.property("stp.tpSubtipoTabelaPreco"), "subtipoTabelaPreco.tpSubtipoTabelaPreco");

		DetachedCriteria dc = DetachedCriteria.forClass(ServicoAdicionalCliente.class, "sac");
		dc.setProjection(projectionList);

		dc.createAlias("sac.tabelaDivisaoCliente", "tdc");
		dc.createAlias("tdc.tabelaPreco", "tp");
		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");

		dc.add(Restrictions.eq("tdc.divisaoCliente.id", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.id", idServico));
		dc.add(Restrictions.eq("sac.parcelaPreco.id", idParcelaPreco));
		if(idCotacao != null) {
			dc.add(Restrictions.eq("sac.cotacao.idCotacao", idCotacao));
		}
		dc.add(Restrictions.le("tp.blEfetivada", Boolean.TRUE));
		dc.add(Restrictions.le("tp.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.gt("tp.dtVigenciaFinal", dtVigencia));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(TabelaPreco.class));
		return (TabelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}