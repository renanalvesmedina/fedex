package com.mercurio.lms.expedicao.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.vendas.model.ParcelaCotacao;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ServAdicionalDocServDAO extends BaseCrudDao<ServAdicionalDocServ, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ServAdicionalDocServ.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("servicoAdicional", FetchMode.JOIN);
	}

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		String sql = "select pojo.idServAdicionalDocServ " +
		"from "+ ServAdicionalDocServ.class.getName() + " as  pojo " +
		"join pojo.doctoServico as ds " +
		"where ds.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql, "idDoctoServico", idDoctoServico);
	}

	public List findByCotacao(Long idCotacao) {
		ProjectionList pl = Projections.projectionList()
		.add(Projections.property("d.idServAdicionalDocServ"), "idServAdicionalDocServ")
		.add(Projections.property("pp.cdParcelaPreco"), "cdParcelaPreco")
		.add(Projections.property("d.qtPaletes"), "qtPaletes")
		.add(Projections.property("d.nrKmRodado"), "nrKmRodado")
		.add(Projections.property("d.qtColetas"), "qtColetas")
		.add(Projections.property("d.qtDias"), "qtDias")
		.add(Projections.property("d.qtSegurancasAdicionais"), "qtSegurancasAdicionais")
		.add(Projections.property("d.dtPrimeiroCheque"), "dtPrimeiroCheque")
		.add(Projections.property("d.vlMercadoria"), "vlMercadoria")
		.add(Projections.property("d.vlFrete"), "vlFrete")
		.add(Projections.property("d.qtCheques"), "qtCheques")
		.add(Projections.property("sa.idServicoAdicional"), "servicoAdicional_idServicoAdicional")
		.add(Projections.property("pp.dsParcelaPreco"), "servicoAdicional_dsServicoAdicional");
		DetachedCriteria dc = DetachedCriteria.forClass(ParcelaCotacao.class, "pc")
			.setProjection(pl)
			.createAlias("pc.parcelaPreco", "pp")
			.createAlias("pp.servicoAdicional", "sa")
			.createAlias("sa.servAdicionalDocServs", "d")
			.add(Restrictions.eq("d.cotacao.id", idCotacao))
			.add(Restrictions.eq("pc.cotacao.id", idCotacao))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		
		List<Map> result = findByDetachedCriteria(dc);
		List<ServAdicionalDocServ> servicos = new ArrayList<ServAdicionalDocServ>();
		if( result != null ){
			for(Map map : result ){
				ServAdicionalDocServ servico = new ServAdicionalDocServ();
				servico.setIdServAdicionalDocServ(MapUtils.getLong(map, "idServAdicionalDocServ"));
				servico.setCdParcelaPreco(MapUtils.getString(map, "cdParcelaPreco"));
				servico.setQtPaletes(MapUtils.getInteger(map, "qtPaletes"));
				servico.setNrKmRodado(MapUtils.getInteger(map, "nrKmRodado"));
				servico.setQtColetas(MapUtils.getInteger(map, "qtColetas"));
				servico.setQtDias(MapUtils.getInteger(map, "qtDias"));
				servico.setQtSegurancasAdicionais(MapUtils.getInteger(map, "qtSegurancasAdicionais"));
				servico.setDtPrimeiroCheque((YearMonthDay)map.get("dtPrimeiroCheque"));
				servico.setVlMercadoria((BigDecimal)map.get("vlMercadoria"));
				servico.setVlFrete((BigDecimal)map.get("vlFrete"));
				servico.setQtCheques(MapUtils.getInteger(map, "qtCheques"));
				
				Map map2 = (Map)map.get("servicoAdicional");
				if( map2 != null ){
					ServicoAdicional sa = new ServicoAdicional();
					sa.setIdServicoAdicional(MapUtils.getLong(map2,"idServicoAdicional"));
					sa.setDsServicoAdicional((VarcharI18n)map2.get("dsServicoAdicional"));
					servico.setServicoAdicional(sa);
				}
				servicos.add(servico);
			}
		}
		return servicos;
	}

	public List findNFServicoAdicionalValores(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new map(" +
				"sa.dsServicoAdicional as dsServicoAdicional, " +
				"ds.vlTotalServicos as vlTotalServicos, " +
				"ds.vlImposto as vlImposto, " +
				"ds.vlTotalDocServico as vlTotalDocServico, " +
				"ds.vlLiquido as vlLiquido " +
				") ");

		sql.addFrom(ServAdicionalDocServ.class.getName() + " as sads " +
				"left join sads.doctoServico ds " +
				"left join sads.servicoAdicional sa");

		sql.addCriteria("ds.id", "=", idDoctoServico, Long.class);
		
		return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
	}

	public ServAdicionalDocServ findByIdDoctoServico(Long idDoctoServico) {
		List l = findServAdicionaisDocServByIdDoctoServico(idDoctoServico);
		if(!l.isEmpty()) {
			return (ServAdicionalDocServ)l.get(0);
		}
		return null;
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("d.cotacao.id", idCotacao));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public ResultSetPage findPaginatedByIdCotacao(Long idCotacao, FindDefinition def) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("d.idServAdicionalDocServ"), "idServAdicionalDocServ")
			.add(Projections.property("s.dsServicoAdicional"), "servicoAdicional_dsServicoAdicional");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.createAlias("servicoAdicional", "s")
			.setProjection(pl)
			.add(Restrictions.eq("d.cotacao.id", idCotacao))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findPaginatedByDetachedCriteria(dc, def.getCurrentPage(), def.getPageSize());
	}

	public Map findServById(Long id) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("d.idServAdicionalDocServ"), "idServAdicionalDocServ")
			.add(Projections.property("d.qtPaletes"), "qtPaletes")
			.add(Projections.property("d.nrKmRodado"), "nrKmRodado")
			.add(Projections.property("d.qtColetas"), "qtColetas")
			.add(Projections.property("d.qtDias"), "qtDias")
			.add(Projections.property("d.qtSegurancasAdicionais"), "qtSegurancasAdicionais")
			.add(Projections.property("d.dtPrimeiroCheque"), "dtPrimeiroCheque")
			.add(Projections.property("d.vlMercadoria"), "vlMercadoria")
			.add(Projections.property("d.vlFrete"), "vlFrete")
			.add(Projections.property("d.qtCheques"), "qtCheques")
			.add(Projections.property("d.servicoAdicional.idServicoAdicional"), "servicoAdicional_idServicoAdicional");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "d")
			.setProjection(pl)
			.add(Restrictions.idEq(id))
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return (Map)findByDetachedCriteria(dc).get(0);
	}

	public BigDecimal findVlMercadoriaReembolsoByDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("sad.vlMercadoria");
		sql.addFrom("ServAdicionalDocServ sad");
		sql.addCriteria("sad.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCustomCriteria("sad.servicoAdicional.idServicoAdicional = (SELECT pp.servicoAdicional.idServicoAdicional FROM ParcelaPreco pp WHERE upper(pp.cdParcelaPreco) = ?)",
								ConstantesExpedicao.CD_REEMBOLSO.toUpperCase());
		
		List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		
		return (result.isEmpty()) ? null : (BigDecimal)result.get(0); 
				
	}
	
	public Integer findQtChequesReembolsoByDoctoServico(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("sad.qtCheques");
		sql.addFrom("ServAdicionalDocServ sad");
		sql.addCriteria("sad.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCustomCriteria("sad.servicoAdicional.idServicoAdicional = (SELECT pp.servicoAdicional.idServicoAdicional FROM ParcelaPreco pp WHERE upper(pp.cdParcelaPreco) = ?)",
								ConstantesExpedicao.CD_REEMBOLSO.toUpperCase());

		List result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		return (result.isEmpty()) ? null : (Integer)result.get(0); 
	}

	public List findServAdicionaisDocServByIdDoctoServico(Long idDoctoServico){

		StringBuilder hql = new StringBuilder()
		.append("from	").append(getPersistentClass().getName()).append(" as sads\n")
		.append("left 	join fetch sads.doctoServico ds\n")
		.append("left 	join fetch sads.servicoAdicional sa\n")
		.append("where	ds.id = ").append(idDoctoServico)
		;

		return getAdsmHibernateTemplate().find(hql.toString());
	}
}