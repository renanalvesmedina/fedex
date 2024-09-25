package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.util.AliasToNestedMapResultTransformer;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.SQLUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern. 
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ImpressoraFormularioDAO extends BaseCrudDao<ImpressoraFormulario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ImpressoraFormulario.class;
	}

	public Long findNrUltimoFormulario(Long idImpressora, String tpFormulario) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select imfo.nrUltimoFormulario ");
		sql.append("from ImpressoraFormulario as imfo, ");
		sql.append("Impressora as impr, ");
		sql.append("ControleFormulario as cofo ");
		sql.append("where impr.idImpressora = ? ");
		sql.append("and imfo.impressora.idImpressora = impr.idImpressora ");
		sql.append("and imfo.controleFormulario.idControleFormulario = cofo.idControleFormulario ");
		sql.append("and cofo.tpFormulario = ? ");
		sql.append("and cofo.filial.id = impr.filial.id ");
		sql.append("and cofo.tpSituacaoFormulario = 'A' ");
		return (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object [] {idImpressora, tpFormulario});
	}

	public boolean verificarExistenciaControleFormularioAtivo(Long idImpressora, String tpFormulario) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select 1 ");
		sql.append("from ImpressoraFormulario as imfo, ");
		sql.append("Impressora as impr, ");
		sql.append("ControleFormulario as cofo ");
		sql.append("where impr.idImpressora = ? ");
		sql.append("and imfo.impressora.idImpressora = impr.idImpressora ");
		sql.append("and imfo.controleFormulario.idControleFormulario = cofo.idControleFormulario ");
		sql.append("and cofo.tpFormulario = ? ");
		sql.append("and cofo.tpSituacaoFormulario = 'A' ");
		List list = getAdsmHibernateTemplate().find(sql.toString(), new Object [] {idImpressora, tpFormulario}); 
		return (list != null && list.size() > 0);
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "if");

		ProjectionList pl = Projections.projectionList();
		pl.add(Projections.property("if.id"), "idImpressoraFormulario");
		pl.add(Projections.property("if.nrFormularioInicial"), "nrFormularioInicial");
		pl.add(Projections.property("if.nrFormularioFinal"), "nrFormularioFinal");
		pl.add(Projections.property("if.nrUltimoFormulario"), "nrUltimoFormulario");
		pl.add(Projections.property("if.cdSerie"), "cdSerie");
		pl.add(Projections.property("if.nrSeloFiscalInicial"), "nrSeloFiscalInicial");
		pl.add(Projections.property("if.nrSeloFiscalFinal"), "nrSeloFiscalFinal");
		pl.add(Projections.property("if.nrUltimoSeloFiscal"), "nrUltimoSeloFiscal");
		pl.add(Projections.property("if.nrCodigoBarrasInicial"), "nrCodigoBarrasInicial");
		pl.add(Projections.property("if.nrCodigoBarrasFinal"), "nrCodigoBarrasFinal");
		pl.add(Projections.property("if.nrUltimoCodigoBarras"), "nrUltimoCodigoBarras");
		pl.add(Projections.property("i.dsLocalizacao"), "impressora_dsLocalizacao");
		pl.add(Projections.property("cf.nrFormularioInicial"), "controleFormulario_nrFormularioInicial");
		pl.add(Projections.property("cf.nrFormularioFinal"), "controleFormulario_nrFormularioFinal");
		pl.add(Projections.property("cf.tpFormulario"), "controleFormulario_tpFormulario");
		pl.add(Projections.property("f.sgFilial"), "controleFormulario_filial_sgFilial");

		dc.addOrder(Order.asc("f.sgFilial"));
		dc.addOrder(Order.asc("cf.nrFormularioInicial"));
		dc.addOrder(Order.asc("cf.nrFormularioFinal"));
		dc.addOrder(Order.asc("i.dsLocalizacao"));
		dc.addOrder(Order.asc("if.nrFormularioInicial"));
		dc.addOrder(Order.asc("if.nrFormularioInicial"));

		dc.setProjection(pl);
		dc.setResultTransformer(new AliasToNestedMapResultTransformer());
		mountPaginated(criteria, dc);

		return getAdsmHibernateTemplate().findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	private void mountPaginated(Map<String, Object> criteria, DetachedCriteria dc) {
		dc.createAlias("if.controleFormulario", "cf");
		dc.createAlias("if.impressora", "i");
		dc.createAlias("cf.filial", "f");

		Map<String, Object> impressora = (Map<String, Object>)criteria.get("impressora");
		if(impressora != null) {
			Long idImpressora = MapUtilsPlus.getLong(impressora, "idImpressora");
			if (idImpressora != null)	
				dc.add(Restrictions.eq("i.id", idImpressora));
		}

		Map<String, Object> controleFormulario = (Map<String, Object>)criteria.get("controleFormulario");
		if(controleFormulario != null) {
			Long idControleFormulario = MapUtilsPlus.getLong(controleFormulario, "idControleFormulario");
			if (idControleFormulario != null)
				dc.add(Restrictions.eq("cf.id", idControleFormulario));
		}

	}

	/**
	 * Busca as impressoras por controle de formulário ordenado por 
	 * número de formulário inicial.
	 * @param idControleFormulario PK de Controle de Formulário
	 * @return Collection de Impressoras com o mesmo estoque original
	 */
	public List findByControleFormulario(Long idControleFormulario){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("controleFormulario.idControleFormulario",idControleFormulario));
		dc.addOrder(Order.asc("nrFormularioInicial"));
		return findByDetachedCriteria(dc);
	}

	/**
	 * Verifica se há intersecção do intervalo de formulário informado com algum intervalo já cadastrado do mesmo estoque origem, 
	 * exceto o intervalo da ImpressoraFormulario informado.
	 * 
	 * Atualizada por José Rodrigo Moraes 
	 * em: 26/01/2007
	 * 
	 * @author Robson Edemar Gehl
	 * @param impressora
	 * @return true não há intersecção; false há intersecção.
	 */
	public boolean verificarIntervaloFormularios(ImpressoraFormulario impressora){
		SqlTemplate hql = new SqlTemplate();

		hql.addInnerJoin(ImpressoraFormulario.class.getName(),"ifo");
		hql.addInnerJoin("ifo.controleFormulario","cf");
		hql.addInnerJoin("ifo.impressora","i");

		LongUtils.getHqlValidaIntervalo(
				hql, 
				"ifo.nrFormularioInicial",
				"ifo.nrFormularioFinal", 
				impressora.getNrFormularioInicial(),
				impressora.getNrFormularioFinal()
		);

		hql.addCriteria("ifo.id","<>",impressora.getIdImpressoraFormulario());

		hql.addCriteria("cf.id","=",impressora.getControleFormulario().getIdControleFormulario());

		Integer ret = getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(),hql.getCriteria());

		return (ret != null && ret.intValue() > 0) ? true : false;
	}

	/**
	 * Consulta os vínculos com impressoras que contém o intervalo informado, retornando <bold>true</bold> se existir vínculos.<BR>
	 * A verificação considera apenas Controle de Formulário do mesmo Tipo de Documento e da mesma Empresa.<BR>
	 * Para empresas diferentes é permitido o mesmo intervalo.
	 * @author Robson Edemar Gehl
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 * @return existência de vínculos com impressoras
	 */
	public boolean verificaIntervaloForm(ControleFormulario cf){
		DetachedCriteria dc = createDetachedCriteria();

		dc.createAlias("controleFormulario", "cf");

		dc.add(
			Restrictions.and(
					Restrictions.and(
						Restrictions.le("nrFormularioInicial",cf.getNrFormularioInicial()),
						Restrictions.ge("nrFormularioFinal",cf.getNrFormularioInicial())),
					Restrictions.and(
						Restrictions.le("nrFormularioInicial",cf.getNrFormularioFinal()),
						Restrictions.ge("nrFormularioFinal",cf.getNrFormularioFinal()))
					));
		
		//Em caso de alteração, ele é exceção.
		if ( cf.getIdControleFormulario() != null ){
			dc.add(Restrictions.ne("cf.idControleFormulario",cf.getIdControleFormulario()));	
		}

		//Restrição para o mesmo tipo de formulário
		dc.add(Restrictions.eq("cf.tpFormulario", cf.getTpFormulario().getValue()));

		/*
		 * Atenção: no método ControleFormularioDAO.verificarIntervaloFormularios faz a mesma verificação (partindo do ControleFormulario)!
		 * Verifica apenas para a mesma empresa.
		 * Para empresas diferentes é permitido um mesmo intervalo.
		 * Para a mesma empresa, os intervalos só podem ser iguais quando tpSituacaoFormulario == E;
		 */
		dc.add(
			Restrictions.and(
				Restrictions.eq("cf.empresa.id", cf.getEmpresa().getIdEmpresa()),
				Restrictions.ne("cf.tpSituacaoFormulario", "E") //se nao for E, não pode ser o mesmo intervalo (para mesma empresa)
			)
		);

		/*
		 * Não pode ter o mesmo AIDF
		 */
		if(cf.getNrAidf() != null)
			dc.add(Restrictions.ilike("cf.nrAidf", cf.getNrAidf().toLowerCase()));

		dc.setProjection(Projections.rowCount());

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() <= 0);
	}

	/**
	 * Atualiza o cdSerie de todos os ImpressoraFormulario vinculadas ao ControleFormulario informado.
	 * @param cf
	 * @return
	 */
	public boolean updateCdSerie(ControleFormulario cf) {
		StringBuffer hql = new StringBuffer()
			.append("update ").append(ImpressoraFormulario.class.getName())
			.append(" set cdSerie = :newCdSerie where id_controle_formulario = :idCF");

		int update = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql.toString())
			.setString("newCdSerie", cf.getCdSerie())
			.setLong("idCF", cf.getIdControleFormulario().longValue())
			.executeUpdate();

		return (update > 0);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("impressora", FetchMode.JOIN);
		lazyFindPaginated.put("controleFormulario", FetchMode.JOIN);
		lazyFindPaginated.put("controleFormulario.filial", FetchMode.JOIN);
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("controleFormulario", FetchMode.JOIN);
		lazyFindById.put("controleFormulario.filial", FetchMode.JOIN);
		lazyFindById.put("impressora", FetchMode.JOIN);
	}

	/**
	 * Método responsável por buscar impressoraFormulario fora do intervalo de selos do controleFormulario correspondente
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 17/08/2006
	 * @param cf
	 * @return
	 */
	public List<ImpressoraFormulario> findImpressorasForaIntervaloSelosFormulario(ControleFormulario cfTela, ControleFormulario cfBanco) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" if ");

		hql.addFrom(getPersistentClass().getName() + " if " +
				" JOIN if.controleFormulario cf ");

		/** Filtra pelas impressoras que tenham intervalos de selos */
		if(cfTela.getNrSeloFiscalInicial() == null && cfBanco.getNrSeloFiscalInicial() != null){
			hql.addCustomCriteria(" ( if.nrSeloFiscalInicial is not null OR if.nrSeloFiscalInicial <> '' ) ");

		/** Filtra pelas impressoras que tenham intervalo de selos fora do intervalo de selos do controle de formulário */	
		} else if(cfTela.getNrSeloFiscalInicial() != null && cfTela.getNrSeloFiscalFinal() != null) {
			hql.addCustomCriteria(" ((not(if.nrSeloFiscalInicial between ? and ?)) OR " +
			" (not(if.nrSeloFiscalFinal between ? and ?)) OR" +
			" (if.nrSeloFiscalFinal is null)) ");

			hql.addCriteriaValue(cfTela.getNrSeloFiscalInicial());
			hql.addCriteriaValue(cfTela.getNrSeloFiscalFinal());
			hql.addCriteriaValue(cfTela.getNrSeloFiscalInicial());
			hql.addCriteriaValue(cfTela.getNrSeloFiscalFinal());
		}

		hql.addCriteria("cf.idControleFormulario", "=", cfTela.getIdControleFormulario());

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Método responsável por buscar impressoraFormulario de acordo com os idsImpressoraFormulario e com a filial da sessão do usuário 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 24/08/2006
	 *
	 * @param idsimpressoraFormulario
	 * @return
	 *
	 */
	public List findImpressoraFormularioComFilialIgualFilialSessao(List idsImpressoraFormulario){
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" if ");

		hql.addFrom(getPersistentClass().getName() + " if " +
				" INNER JOIN if.controleFormulario cf " +
				" INNER JOIN cf.filial f ");

		SQLUtils.joinExpressionsWithComma(idsImpressoraFormulario, hql, "if.idImpressoraFormulario");
		hql.addCriteria("f.idFilial", "=", SessionUtils.getFilialSessao().getIdFilial());

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

}