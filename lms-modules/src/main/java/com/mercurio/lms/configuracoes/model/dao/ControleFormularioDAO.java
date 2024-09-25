package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ControleFormulario;
import com.mercurio.lms.configuracoes.model.ImpressoraFormulario;
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
public class ControleFormularioDAO extends BaseCrudDao<ControleFormulario, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return ControleFormulario.class;
	}

	private SqlTemplate getSqlTemplateControleFormulario(Map<String, Object> criteria) {
		SqlTemplate sql = new SqlTemplate();

		sql.addCriteria("d1.name", "=", "DM_TIPO_FORMULARIO");
		sql.addCriteria("d2.name", "=", "DM_SITUACAO_FORMULARIO");
		sql.addCriteria("cf.tpFormulario", "=", MapUtils.getString(criteria, "tpFormulario"));
		sql.addCriteria("cf.tpSituacaoFormulario", "=", MapUtils.getString(criteria, "tpSituacaoFormulario"));

		sql.addCriteria("eo.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "controleFormulario"), "idControleFormulario"));

		sql.addCriteria("emp.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "empresa"), "idEmpresa"));

		sql.addCriteria("fil.id", "=", MapUtils.getLong(MapUtils.getMap(criteria, "filial"), "idFilial"));

		sql.addCriteria("cf.dtRecebimento",">=", criteria.get("dtRecebimentoInicial"), YearMonthDay.class);

		sql.addCriteria("cf.dtRecebimento","<=", criteria.get("dtRecebimentoFinal"), YearMonthDay.class);

		sql.addCriteria("cf.nrFormularioInicial","<=", criteria.get("nrFormulario"), Long.class);

		sql.addCriteria("cf.nrFormularioFinal",">=", criteria.get("nrFormulario"), Long.class);

		sql.addCriteria("cf.nrSeloFiscalInicial","<=", criteria.get("nrSeloFiscal"), Long.class);
		sql.addCriteria("cf.nrSeloFiscalFinal",">=", criteria.get("nrSeloFiscal"), Long.class);

		if (criteria.get("cdSerie") != null)
			sql.addCriteria("upper(cf.cdSerie)", "like", ((String)criteria.get("cdSerie")).toUpperCase(), String.class);

		sql.addCriteria("cf.nrAidf","like", criteria.get("nrAidf"), String.class);

		return sql;
	}

	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.BaseCrudDao#findPaginated(java.util.Map, com.mercurio.adsm.framework.model.FindDefinition)
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		
		SqlTemplate sql = getSqlTemplateControleFormulario(criteria);
		sql.addProjection(" cf ");
		
		sql.addFrom( DomainValue.class.getName() + " vd1 " );
		sql.addFrom( DomainValue.class.getName() + " vd2 ");
		
		StringBuffer joins = new StringBuffer()
			.append(" join vd1.domain d1 ")
			.append(" join vd2.domain d2 ")
			.append(" join fetch cf.empresa emp")
			.append(" join fetch emp.pessoa empP")
			.append(" left join cf.controleFormulario eo ")
			.append(" left join eo.controleFormulario eoc ")
			.append(" join fetch cf.filial fil ");
			
		sql.addFrom( getPersistentClass().getName() + " cf " + joins.toString() );

		sql.addJoin("vd1.value","cf.tpFormulario");
		sql.addJoin("vd2.value","cf.tpSituacaoFormulario");
		
		sql.addOrderBy("empP.nmPessoa");
		sql.addOrderBy("fil.sgFilial");
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd1.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy(OrderVarcharI18n.hqlOrder("vd2.description", LocaleContextHolder.getLocale()));
		sql.addOrderBy("cf.nrFormularioInicial");
		sql.addOrderBy("cf.nrFormularioFinal");
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());
		return rsp;
			
	}
	
	public Integer getRowCount(Map criteria) {
		SqlTemplate sql = getSqlTemplateControleFormulario(criteria);
		sql.addProjection(" count(cf.idControleFormulario) ");
		
		sql.addFrom( DomainValue.class.getName() + " vd1 " );
		sql.addFrom( DomainValue.class.getName() + " vd2 ");
		
		StringBuffer joins = new StringBuffer()
			.append(" join vd1.domain d1 ")
			.append(" join vd2.domain d2 ")
			.append(" join cf.empresa emp")
			.append(" join emp.pessoa empP")
			.append(" left join cf.controleFormulario eo ")
			.append(" left join eo.controleFormulario eoc ")
			.append(" join cf.filial fil ");
			
		sql.addFrom( getPersistentClass().getName() + " cf " + joins.toString() );

		sql.addJoin("vd1.value","cf.tpFormulario");
		sql.addJoin("vd2.value","cf.tpSituacaoFormulario");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	/**
	 * Verifica se o tipo de documento é igual ao informado.
	 *@author Robson Edemar Gehl
	 * @param cf
	 * @return true se o tipo de documento é o mesmo
	 */
	public boolean validateTipoDocumento(ControleFormulario cf){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("idControleFormulario", cf.getIdControleFormulario()));
		dc.add(Restrictions.eq("tpFormulario", cf.getTpFormulario().getValue()));
		dc.setProjection(Projections.count("idControleFormulario"));
		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return (count.intValue() > 0);
	}
	
	/**
	 * Verifica a existencia de filhos de um Controle de Formulário
	 *@author Robson Edemar Gehl
	 * @param idControleFormulario
	 * @return true se exite filho(s)
	 */
	public boolean isExisteFilhos(Long idControleFormulario){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("controleFormulario.id", idControleFormulario));
		dc.setProjection(Projections.count("id"));
		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return (count.intValue() > 0);
	}
	
	
	/**
	 * Verifica se a data recebimento do estoque de origem é inferior ou igual a data de recebimento do controle de formulário.
	 *@author Robson Edemar Gehl
	 * @param cf
	 * @return true se data do controle de estoque original é inferior ou igual
	 */
	public boolean validateDataRecebimentoEstoqueOriginal(ControleFormulario cf){

		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("id", cf.getControleFormulario().getIdControleFormulario()));
		dc.add(Restrictions.le("dtRecebimento",cf.getDtRecebimento()));
		dc.setProjection(Projections.count("idControleFormulario"));

		Integer count = (Integer) findByDetachedCriteria(dc).get(0); 
		return (count.intValue() > 0);
	}
	
	/**
	 * Valida o intervalo do formulário com o intervalo dos respectivos filhos, na atualização. <BR>
	 * Não é permitido um intervalo que não compreende um controle de formulário filho.<BR><BR>
	 * 
	 * Exemplo da consulta que busca intervalos que não estão contidos no estoque original:<BR>
	 * Para intervalo (X,Y), onde Z é o Controle Formulário Pai (estoque origem)<BR>
	 * select this_.* <BR> 
	 * from CONTROLE_FORMULARIO this_ <BR> 
	 * left outer join CONTROLE_FORMULARIO controlefo2_ on this_.ID_CONTROLE_ESTOQUE_ORIGINAL=controlefo2_.ID_CONTROLE_FORMULARIO <BR> 
	 * where this_.ID_CONTROLE_ESTOQUE_ORIGINAL=Z <BR>
	 *		and( 		(this_.NR_FORMULARIO_INICIAL>Y and this_.NR_FORMULARIO_FINAL>Y ) <BR> 
	 *				or 	(this_.NR_FORMULARIO_INICIAL<X and this_.NR_FORMULARIO_FINAL<X ) <BR>
	 *				or 	( this_.NR_FORMULARIO_INICIAL<X and this_.NR_FORMULARIO_FINAL>Y ) <BR>
	 *				or 	(this_.NR_FORMULARIO_INICIAL between X and Y and this_.NR_FORMULARIO_FINAL > Y) <BR>
	 *				or 	(this_.NR_FORMULARIO_FINAL between X and Y and this_.NR_FORMULARIO_INICIAL < X) <BR>
	 *		)
	 *
	 * Esta consulta seleciona um controle de formulário Z que não está contido no intervalo (X,Y) informado. 
	 * Não é permitido um intervalo que não esteja contido em um estoque original.
	 *
	 * @author Robson Edemar Gehl
	 * @param cf Controle de Formulário
	 * @return true: intervalo válido; false: intervalo inválido
	 */
	public boolean validarIntervaloFilhos(ControleFormulario cf){
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("controleFormulario", "estoqueOriginal");//Estoque Origem
		dc.add(Restrictions.eq("estoqueOriginal.id",cf.getIdControleFormulario()));

		//Restrição para buscar intervalos que não estão contido no intervalo Pai
		//Existem cinco possibilidades de um intervalo não estar contido no estoque original
		dc.add(
			Restrictions.or(
				Restrictions.and(
						Restrictions.gt("nrFormularioInicial", cf.getNrFormularioFinal()),
						Restrictions.gt("nrFormularioFinal", cf.getNrFormularioFinal())),
				Restrictions.or(
					Restrictions.and(
								Restrictions.lt("nrFormularioInicial", cf.getNrFormularioInicial()),
								Restrictions.lt("nrFormularioFinal", cf.getNrFormularioInicial())),
					Restrictions.or(
						Restrictions.and(
								Restrictions.lt("nrFormularioInicial", cf.getNrFormularioInicial()),
								Restrictions.gt("nrFormularioFinal", cf.getNrFormularioFinal())
						),
						Restrictions.or(
								Restrictions.and(
										Restrictions.between("nrFormularioInicial", cf.getNrFormularioInicial(), cf.getNrFormularioFinal()),
										Restrictions.gt("nrFormularioFinal", cf.getNrFormularioFinal())
								),
								Restrictions.and(
										Restrictions.between("nrFormularioFinal", cf.getNrFormularioInicial(), cf.getNrFormularioFinal()),
										Restrictions.lt("nrFormularioInicial", cf.getNrFormularioInicial())
								)
						)
					)
				)
			)
		);

		dc.setProjection(Projections.count("idControleFormulario"));
		//quantidade de intervalos filhos que não coincidem com intervalo do formulário
		int count = ((Integer) findByDetachedCriteria(dc).get(0)).intValue();
		return (count <= 0);
	}
	
	/**
	 * Verifica se existe algum Controle de Formulário filho (do ControleFormulario informado) que intersecciona o intervalo da Impressora.
	 * @author Robson Edemar Gehl
	 * @param impressora
	 * @return true se existe intervalo; false se não existe intervalo
	 */
	public boolean verificarIntervaloFilhosByImpressora(ImpressoraFormulario impressora){
		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("controleFormulario", "cf");
		dc.add(Restrictions.eq("cf.id", impressora.getControleFormulario().getIdControleFormulario()));
		dc.add(Restrictions.le("nrFormularioInicial", impressora.getNrFormularioFinal()));
		dc.add(Restrictions.ge("nrFormularioFinal", impressora.getNrFormularioInicial()));
		dc.setProjection(Projections.count("idControleFormulario"));
		//Quantidade de Controle de Formulario que interseccionam o intervalo da Impressora
		Integer count = (Integer) findByDetachedCriteria(dc).get(0);
		return (count.intValue() > 0);
	}
	
	/**
	 * A filial do Controle de Formulário não pode ser a mesma filial do Controle de Formulário pai (Estoque Origem).
	 * @author Robson Edemar Gehl
	 * @param cf Controle Formulário
	 * @return true: filial válida; false: filial inválida
	 */
	public boolean validarFilialControleFormulario(ControleFormulario cf){
		//Deve haver estoque original, caso contrário não é necessário passar por esta regra, ou seja, filial é válida.
		if (cf.getControleFormulario() == null){
			return true;
		}

		DetachedCriteria dc = createDetachedCriteria();
		dc.createAlias("filial", "f");
		dc.add(Restrictions.eq("idControleFormulario", cf.getControleFormulario().getIdControleFormulario()));
		dc.add(Restrictions.eq("f.id", cf.getFilial().getIdFilial()));
		dc.setProjection(Projections.count("idControleFormulario"));

		//retorna 1 se o controle formulário pai tem a mesma filial
		int count = ((Integer)findByDetachedCriteria(dc).get(0)).intValue();
		//Filial só é válida se filial não for igual
		return (count == 0);
	}
	
	
	
	/**
	 * Verifica se há intersecção do intervalo de formulário informado com algum intervalo já cadastrado.
	 * @author Robson Edemar Gehl 
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 * @return true se existir intersecção; false se não existir intersecção
	 */
	public boolean verificarIntervaloFormularios(ControleFormulario cf) {
		DetachedCriteria dc = createDetachedCriteria(); 

		dc.add(Restrictions.or(
				Restrictions.or(
						Restrictions.between("nrFormularioInicial", cf.getNrFormularioInicial(), cf.getNrFormularioFinal()),
						Restrictions.between("nrFormularioFinal", cf.getNrFormularioInicial(), cf.getNrFormularioFinal())
				),
				Restrictions.or(
					Restrictions.and(
							Restrictions.ge("nrFormularioInicial", cf.getNrFormularioInicial()),
							Restrictions.le("nrFormularioFinal", cf.getNrFormularioInicial())
					),
					Restrictions.and(
							Restrictions.le("nrFormularioInicial", cf.getNrFormularioInicial()),
							Restrictions.ge("nrFormularioFinal", cf.getNrFormularioInicial())
					)
				)
		));

		//Restrição para o mesmo tipo de formulário
		dc.add(Restrictions.eq("tpFormulario", cf.getTpFormulario().getValue()));

		/*
		 * Atenção: no método ImpressoraFormularioDAO.verificaIntervaloForm faz a mesma verificação (partindo de impressora)!
		 * Restrição para fomulário da mesma empresa (permite mesmo intervalo para empresas diferentes) 
		 * Para a mesma empresa, os intervalos só podem ser iguais quando tpSituacaoFormulario == E;
		 */
		dc.add(
				Restrictions.and(
						Restrictions.eq("empresa.id", cf.getEmpresa().getIdEmpresa()),
						Restrictions.ne("tpSituacaoFormulario", "E") //se nao for E, não pode ser o mesmo intervalo (para mesma empresa)
					)
				);

		/*
		 * Não pode ter o mesmo AIDF
		 */
		if(cf.getNrAidf() != null)
			dc.add(Restrictions.ilike("nrAidf", cf.getNrAidf().toLowerCase()));
		
		if (cf.getIdControleFormulario() != null){
			dc.add(Restrictions.ne("idControleFormulario", cf.getIdControleFormulario()));	
		}

		if (cf.getControleFormulario() == null){
			dc.add(Restrictions.isNull("controleFormulario"));
		}

		dc.setProjection(Projections.rowCount());

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	/**
	 * Consulta os controle de formulários do mesmo estoque origem que contem o intervalo informado, menos o Controle de Formulário em questão.
	 * @author Robson Edemar Gehl
	 * @param idControleFormularioPai
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 * @return existencia de intervalo no estoque de origem
	 */
	public boolean verificarIntervaloPaiOrigem(ControleFormulario cf){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.eq("controleFormulario.idControleFormulario", cf.getControleFormulario().getIdControleFormulario()));
		dc.add(Restrictions.or(
				Restrictions.or(
						Restrictions.between("nrFormularioInicial", cf.getNrFormularioInicial(), cf.getNrFormularioFinal()),
						Restrictions.between("nrFormularioFinal", cf.getNrFormularioInicial(), cf.getNrFormularioFinal())
				),
				Restrictions.and(
						Restrictions.ge("nrFormularioInicial", cf.getNrFormularioInicial()),
						Restrictions.le("nrFormularioFinal", cf.getNrFormularioInicial())
				)
			)
		);
		if (cf.getIdControleFormulario() != null){
			dc.add(Restrictions.ne("idControleFormulario",cf.getIdControleFormulario()));
		}
		dc.setProjection(Projections.rowCount());
		
		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		//Quantidade de registros que coincidem com o intervalo
		return (result.intValue() > 0);
	}
	
	/**
	 * Verifica se os Controle de Formulários (IDs) informados estão encerrados.<BR>
	 * @author Robson Edemar Gehl
	 * @param ids
	 * @return true, se nenhum está encerrado; false, pelo menos um encerrado
	 */
	public boolean validateControleFormularioEncerrado(List ids){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(Restrictions.in("id", ids));
		dc.add(Restrictions.eq("tpSituacaoFormulario","E"));
		dc.setProjection(Projections.rowCount());

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() <= 0);
	}

	/**
	 * Verifica se intervalo está contido no controle de formulário pai.
	 * @author Robson Edemar Gehl
	 * @param idControleFormularioPai
	 * @param nrFormularioInicial
	 * @param nrFormularioFinal
	 * @return intervalo de formulário válido
	 */
	public boolean verificarIntervaloPai(Long idControleFormularioPai, Long nrFormularioInicial, Long nrFormularioFinal){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("idControleFormulario", idControleFormularioPai));
		dc.add(Restrictions.and(
				Restrictions.and(
						Restrictions.le("nrFormularioInicial",nrFormularioInicial),
						Restrictions.ge("nrFormularioFinal",nrFormularioInicial)),
					Restrictions.and(
						Restrictions.le("nrFormularioInicial",nrFormularioFinal),
						Restrictions.ge("nrFormularioFinal",nrFormularioFinal))
				)
		);

		dc.setProjection(Projections.rowCount());

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() <= 0);
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("empresa", FetchMode.JOIN);
		fetchModes.put("empresa.pessoa", FetchMode.JOIN);
		fetchModes.put("controleFormulario", FetchMode.JOIN);
		fetchModes.put("controleFormulario.filial", FetchMode.JOIN);
	}
	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		fetchModes.put("empresa", FetchMode.JOIN);
		fetchModes.put("empresa.pessoa", FetchMode.JOIN);
		fetchModes.put("controleFormulario", FetchMode.JOIN);
	}
	
	public Long findFormularioNFServicoAtivoByNumeroFormulario(Long nrFormulario) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("cf.idControleFormulario");

		sql.addFrom(ControleFormulario.class.getName() + " as cf ");
		
		sql.addCriteria("cf.tpFormulario", "=", "NFS", String.class);
		sql.addCriteria("cf.tpSituacaoFormulario", "=", "A", String.class);
		sql.addCriteria("cf.nrFormularioInicial", "<=", nrFormulario, Long.class);
		sql.addCriteria("cf.nrFormularioFinal", ">=", nrFormulario, Long.class);

		List<Long> result = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
		if(!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Método responsável por buscar controleFormulario de acordo com os idsControleFormulario e com a filial da sessão do usuário 
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 23/08/2006
	 *
	 * @param idsControleFormulario
	 * @return
	 *
	 */
	public List<ControleFormulario> findControleFormularioComFilialIgualFilialSessao(List<Long> idsControleFormulario) {
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection(" cf ");

		hql.addFrom(getPersistentClass().getName() + " cf " +
				" INNER JOIN cf.filial f ");

		SQLUtils.joinExpressionsWithComma(idsControleFormulario, hql, "cf.idControleFormulario");
		hql.addCriteria("f.idFilial", "=", SessionUtils.getFilialSessao().getIdFilial());

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

}