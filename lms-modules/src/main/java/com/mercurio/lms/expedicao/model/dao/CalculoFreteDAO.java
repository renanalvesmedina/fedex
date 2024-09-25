package com.mercurio.lms.expedicao.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.YearMonthDay;

import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.ParametroCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.util.ParametroClienteUtils;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean id="lms.expedicao.calculoFreteDAO"
 */
public class CalculoFreteDAO extends CalculoServicoDAO {

	private DetachedCriteria getDetachedCriteria(){

		DetachedCriteria dc = DetachedCriteria.forClass(ParametroCliente.class, "pc");
		dc.createAlias("pc.tabelaDivisaoCliente", "tdc",Criteria.LEFT_JOIN);
		dc.createAlias("tdc.divisaoCliente", "dc",Criteria.LEFT_JOIN);
		dc.createAlias("tdc.tabelaPreco", "tp",Criteria.LEFT_JOIN);
		dc.createAlias("tp.moeda", "m",Criteria.LEFT_JOIN);
		dc.createAlias("tp.tipoTabelaPreco", "ttp",Criteria.LEFT_JOIN);
		dc.createAlias("tp.subtipoTabelaPreco", "stp",Criteria.LEFT_JOIN);
		
		dc.setFetchMode("tdc", FetchMode.JOIN)
			.setFetchMode("dc", FetchMode.JOIN)
			.setFetchMode("tp", FetchMode.JOIN)
			.setFetchMode("m", FetchMode.JOIN)
			.setFetchMode("ttp", FetchMode.JOIN)
			.setFetchMode("stp", FetchMode.JOIN);

		return dc;
	}
	
	@SuppressWarnings("unchecked")
	public List<ParametroCliente> findParametroClienteByRota(Long idDivisaoCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
		
		DetachedCriteria dc = getDetachedCriteria();
		
		dc.add(Restrictions.eq("dc.id", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.id", idServico));

		ParametroClienteUtils.addParametroClienteRestricaoRota(dc, restricaoRotaOrigem, restricaoRotaDestino);

		return getAdsmHibernateTemplate().findByCriteria(dc);
		}
	
	public ParametroCliente findParametroCliente(Long idDivisaoCliente, Long idServico, RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino) {
			
		List<ParametroCliente> list = findParametroClienteByRota(idDivisaoCliente, idServico, restricaoRotaOrigem, restricaoRotaDestino);		
		if(list != null && !list.isEmpty()) {
			return list.get(0);
	}

		return null;
	}

	public Cotacao findCotacao(Long idCotacao) {
		Cotacao cotacao = null;

		DetachedCriteria dc = DetachedCriteria.forClass(Cotacao.class, "c");

		dc.createAlias("c.tabelaPreco", "tp");

		dc.add(Restrictions.eq("c.idCotacao", idCotacao));

		List<Cotacao> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			cotacao = result.get(0);
		}
		return cotacao;
	}

	public ParametroCliente findParametroClienteByCotacao(Long idCotacao) {
		ParametroCliente parametroCliente = null;
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		DetachedCriteria dc = DetachedCriteria.forClass(ParametroCliente.class, "pc");

		dc.add(Restrictions.eq("pc.cotacao.id", idCotacao));
		dc.add(Restrictions.le("pc.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("pc.dtVigenciaFinal", dtVigencia));

		List<ParametroCliente> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			parametroCliente = result.get(0);
		}
		return parametroCliente;
	}

	public MunicipioFilial findMunicipioFilial(Long idMunicipio, Long idFilial) {
		MunicipioFilial municipioFilial = null;
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("mf.id"), "idMunicipioFilial");
		projectionList.add(Projections.property("mf.blRestricaoTransporte"), "blRestricaoTransporte");
		projectionList.add(Projections.property("mf.blDificuldadeEntrega"), "blDificuldadeEntrega");
		projectionList.add(Projections.property("mf.nrDistanciaAsfalto"), "nrDistanciaAsfalto");
		projectionList.add(Projections.property("mf.nrDistanciaChao"), "nrDistanciaChao");

		DetachedCriteria dc = DetachedCriteria.forClass(MunicipioFilial.class, "mf");
		dc.setProjection(projectionList);

		dc.add(Restrictions.eq("mf.municipio.idMunicipio", idMunicipio));
		dc.add(Restrictions.eq("mf.filial.idFilial", idFilial));
		dc.add(Restrictions.le("mf.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("mf.dtVigenciaFinal", dtVigencia));

		dc.setResultTransformer(new AliasToBeanResultTransformer(MunicipioFilial.class));

		List<MunicipioFilial> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			municipioFilial = result.get(0);
		}
		return municipioFilial;
	}

	public TabelaPreco findTabelaPreco(Long idTabelaPreco) {
		TabelaPreco tabelaPreco = null;
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = getTabelaPrecoProjection();

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaPreco.class, "tp");
		dc.setProjection(projectionList);

		dc.createAlias("tp.tipoTabelaPreco", "ttp");
		dc.createAlias("tp.subtipoTabelaPreco", "stp");
		dc.createAlias("tp.moeda", "m");

		dc.add(Restrictions.eq("tp.idTabelaPreco", idTabelaPreco));
		dc.add(Restrictions.eq("tp.blEfetivada", Boolean.TRUE));
		dc.add(Restrictions.le("tp.dtVigenciaInicial", dtVigencia));
		dc.add(Restrictions.ge("tp.dtVigenciaFinal", dtVigencia));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(TabelaPreco.class));

		List<TabelaPreco> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			tabelaPreco = result.get(0);
		}
		return tabelaPreco;
	}

	public TabelaPreco findTabelaPrecoTipoSubtipo(Long idDivisaoCliente, Long idServico, String tpFrete) {
		TabelaPreco tabelaPreco = null;
		YearMonthDay dtVigencia = JTDateTimeUtils.getDataAtual();

		ProjectionList projectionList = getTabelaPrecoProjection();

		DetachedCriteria dc = DetachedCriteria.forClass(TabelaDivisaoCliente.class, "tdc");
		dc.setProjection(projectionList);

		if(ConstantesExpedicao.TP_FRETE_FOB.equals(tpFrete)){
			dc.createAlias("tdc.tabelaPrecoFob", "tp");
			dc.createAlias("tdc.tabelaPrecoFob.tipoTabelaPreco", "ttp");
			dc.createAlias("tdc.tabelaPrecoFob.subtipoTabelaPreco", "stp");
		}else{
			dc.createAlias("tdc.tabelaPreco", "tp");
			dc.createAlias("tdc.tabelaPreco.tipoTabelaPreco", "ttp");
			dc.createAlias("tdc.tabelaPreco.subtipoTabelaPreco", "stp");
		}
		
		dc.createAlias("tdc.divisaoCliente.cliente", "c");
		dc.createAlias("tdc.divisaoCliente", "dc");
		dc.createAlias("tp.moeda", "m");

		dc.add(Restrictions.eq("dc.idDivisaoCliente", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.idServico", idServico));
		dc.add(Restrictions.eq("tp.blEfetivada", Boolean.TRUE));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(TabelaPreco.class));

		List<TabelaPreco> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			tabelaPreco = result.get(0);
		}
		return tabelaPreco;
	}

	public Conhecimento findConhecimento(Long idDoctoServico) {
		Conhecimento conhecimento = null;

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.property("c.idDoctoServico"), "idDoctoServico");
		projectionList.add(Projections.property("c.vlTotalParcelas"), "vlTotalParcelas");
		projectionList.add(Projections.property("c.servico.idServico"), "servico.idServico");
		projectionList.add(Projections.property("c.tarifaPreco.idTarifaPreco"), "tarifaPreco.idTarifaPreco");
		projectionList.add(Projections.property("c.divisaoCliente.idDivisaoCliente"), "divisaoCliente.idDivisaoCliente");
		projectionList.add(Projections.property("c.filialByIdFilialOrigem.idFilial"), "filialByIdFilialOrigem.idFilial");
		projectionList.add(Projections.property("c.filialByIdFilialDestino.idFilial"), "filialByIdFilialDestino.idFilial");
		projectionList.add(Projections.property("c.nrCepColeta"), "nrCepColeta");
		projectionList.add(Projections.property("c.nrCepEntrega"), "nrCepEntrega");
		projectionList.add(Projections.property("c.municipioByIdMunicipioColeta.idMunicipio"), "municipioByIdMunicipioColeta.idMunicipio");
		projectionList.add(Projections.property("c.municipioByIdMunicipioEntrega.idMunicipio"), "municipioByIdMunicipioEntrega.idMunicipio");

		DetachedCriteria dc = DetachedCriteria.forClass(Conhecimento.class, "c");
		dc.setProjection(projectionList);

		dc.add(Restrictions.eq("c.idDoctoServico", idDoctoServico));

		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Conhecimento.class));

		List<Conhecimento> result = findByDetachedCriteria(dc);
		if(result.size() == 1) {
			conhecimento = result.get(0);
		}
		return conhecimento;
	}

	public Integer getRowCountParametroCliente(Long idTabelaPreco, Long idDivisaoCliente, Long idServico) {
		DetachedCriteria dc = DetachedCriteria.forClass(ParametroCliente.class, "pc");
		dc.setProjection(Projections.rowCount());
		dc.createAlias("pc.tabelaDivisaoCliente", "tdc");
		dc.add(Restrictions.eq("tdc.tabelaPreco.id", idTabelaPreco));
		dc.add(Restrictions.eq("tdc.divisaoCliente.id", idDivisaoCliente));
		dc.add(Restrictions.eq("tdc.servico.id", idServico));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	/**
	 * Verifica se existe um ParametroCLiente para a Rota informada
	 * 
	 * @param restricaoRotaOrigem
	 * @param restricaoRotaDestino
	 * @return
	 */
	public boolean checkParametroClienteByRestricaoRotaOrigemDestino(Long idDivisaoCliente,RestricaoRota restricaoRotaOrigem, RestricaoRota restricaoRotaDestino){	
		StringBuilder sql = new StringBuilder();
		Map<String, Object> criteria = new HashMap<String, Object>();
		sql.append("from ").append(ParametroCliente.class.getName()).append(" pc ");
		sql.append(" , ").append(TabelaDivisaoCliente.class.getName()).append(" tdc "); 
		sql.append(" where tdc.id = pc.tabelaDivisaoCliente.id");
		sql.append(" and pc.tpSituacaoParametro = 'A' ");
		sql.append(" and (pc.dtVigenciaInicial <= sysdate and pc.dtVigenciaFinal >= sysdate ) ");

		sql.append(" and tdc.divisaoCliente.id = :idDivisaoCliente ");
		criteria.put("idDivisaoCliente", idDivisaoCliente);
		
		//Origem Rota
		if (restricaoRotaOrigem.getIdZona() != null){
			sql.append(" and pc.zonaByIdZonaOrigem.id = :idZonaOrigem");
			criteria.put("idZonaOrigem", restricaoRotaOrigem.getIdZona());
		}
		if(restricaoRotaOrigem.getIdPais()!= null){
			sql.append(" and (pc.paisByIdPaisOrigem.id =:idPaisOrigem OR pc.paisByIdPaisOrigem is null)");
			criteria.put("idPaisOrigem", restricaoRotaOrigem.getIdPais());
		}
		if(restricaoRotaOrigem.getIdUnidadeFederativa()!= null){
			sql.append(" and (pc.unidadeFederativaByIdUfOrigem.id =:idUfOrigem OR pc.unidadeFederativaByIdUfOrigem is null)");
			criteria.put("idUfOrigem", restricaoRotaOrigem.getIdUnidadeFederativa());
		}
		if(restricaoRotaOrigem.getIdTipoLocalizacao()!= null){		
			sql.append(" and (pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.id =:idTipoLocalizacaoOrigem OR pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem is null)");
			criteria.put("idTipoLocalizacaoOrigem", restricaoRotaOrigem.getIdTipoLocalizacao());
		}
		
		if(restricaoRotaOrigem.getIdFilial()!= null){		
			sql.append(" and (pc.filialByIdFilialOrigem.id = :idFilialOrigem OR pc.filialByIdFilialOrigem is null)");
			criteria.put("idFilialOrigem", restricaoRotaOrigem.getIdFilial());
		}

		if(restricaoRotaOrigem.getIdMunicipio()!= null){		
			sql.append(" and (pc.municipioByIdMunicipioOrigem.id = :idMunicipioOrigem OR pc.municipioByIdMunicipioOrigem is null)");
			criteria.put("idMunicipioOrigem", restricaoRotaOrigem.getIdMunicipio());
		}
		
		if(restricaoRotaDestino.getIdTipoLocalizacao()!= null){		
			sql.append(" and pc.tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.id =:idTipoLocalizacaoDestino  ");
			criteria.put("idTipoLocalizacaoDestino", restricaoRotaDestino.getIdTipoLocalizacao());
		}
		
		if(restricaoRotaDestino.getIdZona()!= null){		
			sql.append(" and pc.zonaByIdZonaDestino.id = :idZonaDestino");
			criteria.put("idZonaDestino", restricaoRotaDestino.getIdZona());
		}
		
		if(restricaoRotaDestino.getIdPais()!= null){		
			sql.append(" and (pc.paisByIdPaisDestino.id =:idPaisDestino  OR pc.paisByIdPaisDestino is null)");
			criteria.put("idPaisDestino", restricaoRotaDestino.getIdPais());
		}
		
		if(restricaoRotaDestino.getIdUnidadeFederativa()!= null){		
			sql.append(" and (pc.unidadeFederativaByIdUfDestino.id  =:idUFDestino OR pc.unidadeFederativaByIdUfDestino is null)");
			criteria.put("idUFDestino", restricaoRotaDestino.getIdUnidadeFederativa());
		}
		
		if(restricaoRotaDestino.getIdFilial()!= null){		
			sql.append(" and (pc.filialByIdFilialDestino.id = :idFilialDestino OR pc.filialByIdFilialDestino is null)");
			criteria.put("idFilialDestino", restricaoRotaDestino.getIdFilial());
		}
		
		if(restricaoRotaDestino.getIdMunicipio()!= null){		
			sql.append(" and (pc.municipioByIdMunicipioDestino.id = :idMunicipioDestino OR pc.municipioByIdMunicipioDestino is null)");
			criteria.put("idMunicipioDestino", restricaoRotaDestino.getIdMunicipio());
		}
		
		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), criteria);
		
		return result != null && !result.isEmpty();
	}

}