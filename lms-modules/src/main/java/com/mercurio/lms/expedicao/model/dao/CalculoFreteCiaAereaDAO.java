package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
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
 * @spring.bean id="lms.expedicao.calculoFreteCiaAereaDAO"
 */
public class CalculoFreteCiaAereaDAO extends CalculoServicoDAO {

	public TabelaPreco findTabelaPreco(Long idCiaAerea, Long idExpedidor, YearMonthDay dtEmissaoAwb, String tpServico) {
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();
		dtEmissaoAwb = dtEmissaoAwb == null ? JTDateTimeUtils.getDataAtual() : JTDateTimeUtils.getDataAtual();
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tp.idTabelaPreco"), "idTabelaPreco");
		projectionList.add(Projections.property("tp.dsDescricao"), "dsDescricao");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPreco.class, "tp");
		dc.setProjection(projectionList);

		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");
		dc.createAlias("ttp.empresaByIdEmpresaCadastrada", "e");

		dc.add(Restrictions.eq("e.idEmpresa", idCiaAerea));
		if(idExpedidor == null) {
			dc.add(Restrictions.isNull("ttp.cliente.idCliente"));
		} else {
			dc.add(Restrictions.eq("ttp.cliente.idCliente", idExpedidor));
		}
		dc.add(Restrictions.or(		
					Restrictions.and(Restrictions.le("tp.dtVigenciaInicial", dtVigencia), Restrictions.ge("tp.dtVigenciaFinal", dtVigencia)),
					Restrictions.and(Restrictions.le("tp.dtVigenciaInicial", dtEmissaoAwb), Restrictions.ge("tp.dtVigenciaFinal", dtEmissaoAwb))
				));
		dc.add(Restrictions.eq("tp.tpServico", tpServico));
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", "C")); //Tabela de Cia Aerea
		dc.add(Restrictions.eq("tp.blEfetivada", Boolean.TRUE));
		dc.add(Restrictions.eq("stp.tpSubtipoTabelaPreco", "L")); //Tabela Liquida

		dc.setResultTransformer(new AliasToBeanResultTransformer(TabelaPreco.class));

		return (TabelaPreco) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	public List<TabelaPreco> findCiasAereasRota(RestricaoRota origem, RestricaoRota destino, Long idCiaAerea, String tpCaracteristicaServico) {
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("tp.idTabelaPreco"), "idTabelaPreco");
		projectionList.add(Projections.property("tp.dsDescricao"), "dsDescricao");
		projectionList.add(Projections.property("tp.tpServico"), "tpServico");
		projectionList.add(Projections.property("ttp.cliente"), "tipoTabelaPreco.cliente");
		projectionList.add(Projections.property("p.idPessoa"), "tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.idPessoa");
		projectionList.add(Projections.property("p.nmPessoa"), "tipoTabelaPreco.empresaByIdEmpresaCadastrada.pessoa.nmPessoa");

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPreco.class, "tp");
		dc.setProjection(Projections.distinct(projectionList));

		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");
		dc.createAlias("ttp.empresaByIdEmpresaCadastrada", "e");
		dc.createAlias("e.pessoa", "p");
		dc.createAlias("tp.tabelaPrecoParcelas", "tbp");
		dc.createAlias("tbp.parcelaPreco", "pp");
		dc.createAlias("tbp.faixaProgressivas", "fp");
		dc.createAlias("fp.valoresFaixasProgressivas", "vfp");
		dc.createAlias("vfp.rotaPreco", "rp");

		dc.add(Restrictions.eq("pp.cdParcelaPreco", ConstantesExpedicao.CD_FRETE_PESO));
		dc.add(Restrictions.eq("ttp.tpTipoTabelaPreco", "C")); //Tabela de Cia Aerea
		dc.add(Restrictions.eq("tp.blEfetivada", Boolean.TRUE));
		dc.add(Restrictions.eq("stp.tpSubtipoTabelaPreco", "L")); //Tabela Liquida
		dc.add(Restrictions.le("tp.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("tp.dtVigenciaFinal", dtVigencia));
		
		if(idCiaAerea != null){
			dc.add(Restrictions.eq("p.idPessoa", idCiaAerea));
		}
		
		if(tpCaracteristicaServico != null){
			dc.add(Restrictions.eq("tp.tpServico", tpCaracteristicaServico));
		}

		RotaPrecoUtils.getRotaPrecoRestricaoRota(dc, origem, destino);

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(TabelaPreco.class));

		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}
	
	public RestricaoRota findRestricaoRotaByAeroporto(Long idAeroporto) {
		ProjectionList projections = Projections.projectionList()
		.add(Projections.property("a.idAeroporto"), "idAeroporto")
		.add(Projections.property("uf.idUnidadeFederativa"), "idUnidadeFederativa")
		.add(Projections.property("pais.idPais"), "idPais")
		.add(Projections.property("z.idZona"), "idZona");

		DetachedCriteria dc = DetachedCriteria.forClass(Aeroporto.class, "a")
		.setProjection(projections)
		.createAlias("a.pessoa", "p")
		.createAlias("p.enderecoPessoa", "ep")
		.createAlias("ep.municipio", "m")
		.createAlias("m.unidadeFederativa", "uf")
		.createAlias("uf.pais", "pais")
		.createAlias("pais.zona", "z")
		.add(Restrictions.eq("a.idAeroporto", idAeroporto))
		.setResultTransformer(new AliasToNestedBeanResultTransformer(RestricaoRota.class));

		return (RestricaoRota) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

}