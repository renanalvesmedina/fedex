package com.mercurio.lms.contasreceber.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.BaixaDevMerc;
import com.mercurio.lms.contasreceber.model.ItemBaixaDevMerc;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class BaixaDevMercDAO extends BaseCrudDao<BaixaDevMerc, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return BaixaDevMerc.class;
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("devedorDocServFat.doctoServico.doctoServicoOriginal", FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("devedorDocServFat.doctoServico.doctoServicoOriginal", FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}

	public BaixaDevMerc findBaixaDevMerc(TypedFlatMap tfm) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" b ");

		sql.addFrom(getPersistentClass().getName() + " b ");

		sql.addCriteria("b.filialEmissora.id", "=", tfm.getLong("filial.idFilial"));
		sql.addCriteria("b.nrBdm", "=", tfm.getLong("nrBdm"));

		List<BaixaDevMerc> result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if(!result.isEmpty()) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public ResultSetPage findPaginated(TypedFlatMap tfm) {
		FindDefinition findDef = FindDefinition.createFindDefinition(tfm);
		
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection(" new Map(b.idBaixaDevMerc as idBaixaDevMerc, fe.sgFilial as filialEmissora, fd.sgFilial as filialDestino, b.nrBdm as nrBdm, " +
				" b.dtEmissao as dtEmissao, b.tpSituacao as tpSituacao, p.tpIdentificacao as tpIdentificacao, p.nrIdentificacao as nrIdentificacao, p.nmPessoa as nmPessoa" +
				" ) ");
		sql.addFrom(getPersistentClass().getName() + " b " +
				" join b.filialEmissora fe " +
				" join b.filialDestino fd " +
				" join b.cliente c " +
				" left join c.pessoa p");

		sql.addCriteria("b.nrBdm", "=", tfm.getLong("nrBdm"));
		sql.addCriteria("fe.idFilial", "=", tfm.getLong("filialEmissora.idFilial"));
		sql.addCriteria("fd.idFilial", "=", tfm.getLong("filialDestino.idFilial"));
		sql.addCriteria("c.idCliente", "=", tfm.getLong("cliente.idCliente"));
		sql.addCriteria("b.dtEmissao", ">=", tfm.getYearMonthDay("dtEmissaoInicial"));
		sql.addCriteria("b.dtEmissao", "<=", tfm.getYearMonthDay("dtEmissaoFinal"));
		sql.addCriteria("b.tpSituacao", "=", tfm.getString("tpSituacao"));
		
		sql.addOrderBy("fe.sgFilial, b.nrBdm");
		
		return getAdsmHibernateTemplate().findPaginated(
				sql.getSql(), 
				findDef.getCurrentPage(),
				findDef.getPageSize(), 
				sql.getCriteria()
		);
	}

	public Integer getRowCount(TypedFlatMap tfm) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" count(b.idBaixaDevMerc) ");
		sql.addFrom(getPersistentClass().getName() + " b " +
				" join b.filialEmissora fe " +
				" join b.filialDestino fd " +
				" join b.cliente c " +
				" left join c.pessoa p");

		sql.addCriteria("b.nrBdm", "=", tfm.getLong("nrBdm"));
		sql.addCriteria("fe.idFilial", "=", tfm.getLong("filialEmissora.idFilial"));
		sql.addCriteria("fd.idFilial", "=", tfm.getLong("filialDestino.idFilial"));
		sql.addCriteria("c.idCliente", "=", tfm.getLong("cliente.idCliente"));
		sql.addCriteria("b.dtEmissao", ">=", tfm.getYearMonthDay("dtEmissaoInicial"));
		sql.addCriteria("b.dtEmissao", "<=", tfm.getYearMonthDay("dtEmissaoFinal"));
		sql.addCriteria("b.tpSituacao", "=", tfm.getString("tpSituacao"));

		sql.addOrderBy("fe.sgFilial, b.nrBdm");

		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	public BaixaDevMerc findById(Long idBaixaDevMerc) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" b ");
		sql.addFrom(getPersistentClass().getName() + " b " +
				" join fetch b.filialEmissora fe " +
				" left join fetch fe.pessoa fep " +
				" join fetch b.filialDestino fd " +
				" left join fetch fd.pessoa fdp " +
				" join fetch b.cliente c " +
				" left join fetch c.pessoa p");

		sql.addCriteria("b.id", "=", idBaixaDevMerc);
		return (BaixaDevMerc) getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0);
	}

	public ItemBaixaDevMerc findItemById(Long idItemBaixaDevMerc) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" b ");
		sql.addFrom(ItemBaixaDevMerc.class.getName() + " ib join fetch ib.devedorDocServFat d ");

		sql.addCriteria("ib.id", "=", idItemBaixaDevMerc);

		return (ItemBaixaDevMerc) getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria()).get(0);
	}

	public BaixaDevMerc findByItemBaixaDevMerc(Long idFilho){
		
		BaixaDevMerc baixaDevMerc = null;
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("itemBaixaDevMerc", "i");
		dc.add(Restrictions.eq("i.idItemBaixaDevMerc", idFilho));
		
		List list = findByDetachedCriteria(dc);

		if (!list.isEmpty()){
			baixaDevMerc = (BaixaDevMerc) list.get(0);
		}
		
		return baixaDevMerc;
	}

	public void removeById(Long id) {
		super.removeById(id, true);
	}

	public void removeByIdFlush(Long id) {
		super.removeById(id,false);
		getAdsmHibernateTemplate().flush();
	}

	public int removeByIds(List ids) {
		return super.removeByIds(ids, true);
	}

	public List findItemBaixaDevMercByBaixaDevMercId(Long idBaixaDevMerc) {
		return super.findByDetachedCriteria(getDetachedCriteria(idBaixaDevMerc));
	}

	public List findPaginatedByIdBaixaDevMerc(Long idBaixaDevMerc) {
		if (idBaixaDevMerc == null)
			return null;
		
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("ib");
		hql.addFrom(ItemBaixaDevMerc.class.getName(), " ib " +
				" join ib.baixaDevMerc b " +
				" left join ib.devedorDocServFat dev " +
				" left join dev.doctoServico doc " +
				" left join doc.doctoServicoOriginal doco " 
				);

		hql.addCriteria("b.id", "=", idBaixaDevMerc);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
	
	public Integer getRowCountByIdBaixaDevMerc(Long idBaixaDevMerc) {
		if (idBaixaDevMerc == null)
			return null;
		
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("count(ib.id)");
		hql.addFrom(ItemBaixaDevMerc.class.getName(), " ib " +
				" join ib.baixaDevMerc b "
				);

		hql.addCriteria("b.id", "=", idBaixaDevMerc);

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return result.intValue();
	}

	public Integer getRowCountItemBaixaDevMercByBaixaDevMercId(Long idBaixaDevMerc) {
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(getDetachedCriteria(idBaixaDevMerc).setProjection(Projections.rowCount()));
	}

	private DetachedCriteria getDetachedCriteria(Long idBaixaDevMerc) {
		return DetachedCriteria.forClass(ItemBaixaDevMerc.class).add(Restrictions.eq("baixaDevMerc.id", idBaixaDevMerc));
	}

	/**
	 * Carrega uma BDM de acordo com a filial de emissão e o número da BDM
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/03/2007
	 *
	 * @param nrBdm
	 * @param idFilialEmissora
	 * @return
	 *
	 */
	public BaixaDevMerc findBaixaDevMerc(Long nrBdm, Long idFilialEmissora) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection(" b ");

		sql.addInnerJoin(getPersistentClass().getName() + " b ");
		sql.addInnerJoin("FETCH b.itemBaixaDevMercs ");

		sql.addCriteria("b.filialEmissora.id", "=", idFilialEmissora);
		sql.addCriteria("b.nrBdm", "=", nrBdm);

		BaixaDevMerc bdm = (BaixaDevMerc)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return bdm;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findBMDByFranquiaAndCompetencia(Long idFranquia, YearMonthDay dataInicioCompetencia, YearMonthDay dataFimCompetencia){
		StringBuffer sql = new StringBuffer()
		.append("select new Map( ")
			.append(" dsf.vlParticipacao as vlParticipacao, ")
			.append(" fe.sgFilial as sgFilial, ")
			.append(" bdm.nrBdm as nrBDM, ")
			.append(" ds.idDoctoServico as idDoctoServico," )
			.append(" ds.tpDocumentoServico as tpDocumentoServico," )
			.append(" ds.filialByIdFilialOrigem.sgFilial as filialOrigem, ")
			.append(" ds.nrDoctoServico as nrDoctoServico ")
		.append(" ) ")
		
		.append(" from ")
		
			.append(BaixaDevMerc.class.getName()).append(" as bdm ")
			.append(" 	join bdm.itemBaixaDevMercs as tbdm ")
			.append(" 	join tbdm.devedorDocServFat as ddsf ")
			.append(" 	join bdm.filialEmissora as fe ")
			.append(" 	join ddsf.doctoServico as ds ")
			.append(" 	join ds.doctoServicoFranqueados as dsf ")
		
		.append(" where ")
			.append(" 	dsf.franquia.idFranquia = :idFranquia ")
			.append(" 	and bdm.tpSituacao = :tpSituacaoAprovacao ")
			.append(" 	and TRUNC(CAST(bdm.dtEmissao AS date )) >=  :dtIniCompetencia ")
			.append(" 	and TRUNC(CAST(bdm.dtEmissao AS date )) <= :dtFimCompetencia ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tpSituacaoAprovacao", ConstantesExpedicao.CD_EMISSAO);
		params.put("idFranquia", idFranquia);
		params.put("dtIniCompetencia", dataInicioCompetencia);
		params.put("dtFimCompetencia", dataFimCompetencia);
		
		return (List<Map<String, Object>>)getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}

}