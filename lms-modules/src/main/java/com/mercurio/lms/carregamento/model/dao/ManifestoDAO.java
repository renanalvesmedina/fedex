package com.mercurio.lms.carregamento.model.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.joda.time.DateTime;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleTrecho;
import com.mercurio.lms.carregamento.model.EventoManifesto;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.carregamento.model.PreManifestoVolume;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ManifestoDAO extends BaseCrudDao<Manifesto, Long> {	
	
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Manifesto.class;
	}

	protected void initFindByIdLazyProperties(Map map) {
		map.put("controleCarga.filialByIdFilialDestino", FetchMode.JOIN);
		map.put("controleCarga", FetchMode.JOIN);
		map.put("controleCarga.meioTransporteByIdTransportado", FetchMode.JOIN);
		map.put("controleCarga.meioTransporteByIdSemiRebocado", FetchMode.JOIN);
		map.put("controleCarga.rotaColetaEntrega", FetchMode.JOIN);
		map.put("manifestoEntrega", FetchMode.JOIN);
		map.put("manifestoViagemNacional", FetchMode.JOIN);
		map.put("manifestoInternacional",  FetchMode.JOIN);
		map.put("filialByIdFilialDestino", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem", FetchMode.JOIN);
	}

	protected void initFindLookupLazyProperties(Map lazyFindLookup) {
		lazyFindLookup.put("filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindLookup.put("filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.filialByIdFilialOrigem", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.filialByIdFilialDestino", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.rotaIdaVolta", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.rotaIdaVolta.rota", FetchMode.JOIN);
		lazyFindLookup.put("controleCarga.rotaColetaEntrega", FetchMode.JOIN);
	}
	
	/**
	 * Recupera uma instância de <code>Manifesto</code> a partir do ID.
	 * 
	 * @param idManifesto representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public Manifesto findManifestoById(Long idManifesto) {
		SqlTemplate sql = new SqlTemplate();

		sql.addFrom(Manifesto.class.getName() + " m left join fetch m.controleCarga cc " +
												" left join fetch cc.filialByIdFilialOrigem ccfo " +
												" left join fetch ccfo.pessoa ccfoPe " +
												" left join fetch cc.rota r " +
												" left join fetch cc.rotaIdaVolta riv " +
												" left join fetch riv.rota rivr " +
												" left join fetch cc.rotaColetaEntrega rce " +
												" join fetch m.filialByIdFilialOrigem fo " +
												" join fetch fo.pessoa fop " +
												" join fetch m.filialByIdFilialDestino fd " +
												" join fetch fd.pessoa fdp " +
												" left join fetch m.cliente cli " +
												" left join fetch cli.pessoa cp " +
												" join fetch m.moeda moe " +
												" left join fetch m.solicitacaoRetirada sr " +
												" left join fetch sr.filial srf " +
												" left join fetch srf.pessoa srfp " +
												" left join fetch m.controleTrecho ct ");
		
		sql.addCriteria("m.id", "=", idManifesto);

		return (Manifesto) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
	}

	/**
	 * Retorna o Manifesto apartir de um volumeNotaFiscal
	 * @param idVolumeNotaFiscal
	 * @return
	 */
	public Manifesto findManifestoByVolumeNotaFiscal(Long idVolumeNotaFiscal) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(Manifesto.class.getName() + " m join fetch m.manifestoViagemNacional mvn " +
												" join fetch mvn.manifestoNacionalVolumes mnv " +
												" join fetch mnv.volumeNotaFiscal vnf ");

			sql.addCriteria("vnf.idVolumeNotaFiscal", "=", idVolumeNotaFiscal);

			return (Manifesto) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());	
	}

	/**
	 * ComboBox para emissao do Manifesto.
	 * @author Andre Valadas
	 * @param criteria
	 * @return
	 */
	public List findComboPreManifestoInit(TypedFlatMap criteria){
		SqlTemplate hql = new SqlTemplate();

		hql.setDistinct();

		/** SELECT */
		hql.addProjection("new Map(m.idManifesto", "idManifesto");
		hql.addProjection("m.nrPreManifesto", "nrPreManifesto");
		hql.addProjection("m.tpModal", "tpModal");
		hql.addProjection("fo.sgFilial", "filialByIdFilialOrigem_sgFilial");
		hql.addProjection("fd.idFilial", "filialByIdFilialDestino_idFilial");
		hql.addProjection("fd.sgFilial", "filialByIdFilialDestino_sgFilial");
		hql.addProjection("fdp.nmFantasia", "filialByIdFilialDestino_pessoa_nmFantasia)");

		/** FROM */
		StringBuffer from = new StringBuffer()
			.append(Manifesto.class.getName()+" as m").append("\n")
			.append("left outer join m.controleCarga.eventoControleCargas as ecc").append("\n")
			.append("left outer join m.filialByIdFilialOrigem as fo").append("\n")
			.append("left outer join m.filialByIdFilialDestino as fd").append("\n")
			.append("left outer join fd.pessoa as fdp").append("\n");
		hql.addFrom(from.toString());

		/** WHERE */
		hql.addCustomCriteria("m.filialByIdFilialOrigem.id = ecc.filial.id");
		hql.addCriteria("m.nrPreManifesto", "=", criteria.getLong("nrPreManifesto"));
		hql.addCriteria("m.filialByIdFilialOrigem.idFilial", "=", criteria.getLong("idFilial"));
		hql.addCriteria("m.tpManifesto", "=", criteria.getString("tpManifesto"));
		hql.addCriteria("m.tpStatusManifesto", "=", criteria.getString("tpStatusManifesto"));
		hql.addCriteria("ecc.tpEventoControleCarga", "=", criteria.getString("tpEventoControleCarga"));
		hql.addCriteria("m.tpAbrangencia", "=", criteria.getString("tpAbrangencia"));

		/** ORDER BY */
		hql.addOrderBy("m.nrPreManifesto", "asc");
		hql.addOrderBy("fd.sgFilial", "asc");

		AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(Manifesto.class);
   		return transformer.transformListResult(getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria()));
	}

	/**
	 * Monta a consulta utilizada nas rotinas de 'getRowCountManifesto' e 'findPaginatedManifesto'
	 * @param criteria
	 * @return
	 */
	private  SqlTemplate getQueryManifesto(TypedFlatMap criteria){

		// PROJEÇÃO
		StringBuffer projecao = new StringBuffer();
		projecao.append("new map(manifesto.idManifesto as idManifesto, ");
		projecao.append("filialByIdFilialOrigem.idFilial as idFilialOrigem, ");
		projecao.append("filialByIdFilialOrigem.sgFilial as sgFilialOrigem, ");
		projecao.append("filialByIdFilialDestino.sgFilial as sgFilialDestino, ");
		projecao.append("manifesto.nrPreManifesto as nrPreManifesto, ");
		projecao.append("manifesto.dhGeracaoPreManifesto as dhGeracaoPreManifesto, ");		
		projecao.append("filialOrigemControleCarga.sgFilial as sgFilialOrigemControleCarga, ");
		projecao.append("controleCarga.nrControleCarga as nrControleCarga, ");		
		projecao.append("rota.dsRota as dsRotaIdaVolta, ");
		projecao.append("rotaColetaEntrega.dsRota as dsRotaColetaEntrega, ");		
		projecao.append("manifesto.tpManifesto as tpManifesto, ");
		projecao.append("manifesto.tpModal as tpModal, ");
		projecao.append("manifesto.tpStatusManifesto as tpStatusManifesto) ");
		  		
		SqlTemplate sql = new SqlTemplate();
		
		sql.setDistinct();
		sql.addProjection(projecao.toString());
   		
		// JOINS
		sql.addFrom(Manifesto.class.getName() + " as manifesto " +
				"join manifesto.filialByIdFilialOrigem as filialByIdFilialOrigem " +
				"left join manifesto.filialByIdFilialDestino as filialByIdFilialDestino " +
				"left join manifesto.controleCarga as controleCarga " +
				"left join controleCarga.filialByIdFilialOrigem as filialOrigemControleCarga " +
				"left join controleCarga.rotaIdaVolta as rotaIdaVolta " +
				"left join rotaIdaVolta.rota as rota " +
				"left join controleCarga.rotaColetaEntrega as rotaColetaEntrega ");
		
		// CRITERIOS
		if (criteria.getString("tpManifesto") != null) {
			if (criteria.getString("tpManifesto").equals("V")) {
				sql.addCriteria("manifesto.tpManifesto", "=", criteria.getString("tpManifesto"));
				sql.addCriteria("manifesto.tpManifestoViagem", "=", criteria.getString("tpPreManifesto"));
			}
			
			if (criteria.getString("tpManifesto").equals("E")) {
				sql.addCriteria("manifesto.tpManifesto", "=", criteria.getString("tpManifesto"));
				sql.addCriteria("manifesto.tpManifestoEntrega", "=", criteria.getString("tpPreManifesto"));
			}
		}

		if (criteria.getLong("controleCarga.filialByIdFilialOrigem.idFilial") != null) {
			sql.addCriteria("manifesto.controleCarga.filialByIdFilialOrigem.id", "=", criteria.getLong("controleCarga.filialByIdFilialOrigem.idFilial"));
		}
		
		if (criteria.getLong("controleCarga.idControleCarga") != null) {
			sql.addCriteria("manifesto.controleCarga.id", "=", criteria.getLong("controleCarga.idControleCarga"));
		}
		
		if (criteria.getLong("controleCarga.rotaIdaVolta.idRotaIdaVolta") != null) {
			sql.addCriteria("manifesto.controleCarga.rotaIdaVolta.id", "=", criteria.getLong("controleCarga.rotaIdaVolta.idRotaIdaVolta"));
		}
		
		if (criteria.getLong("controleCarga.rotaColetaEntrega.idRotaColetaEntrega") != null) {
			sql.addCriteria("manifesto.controleCarga.rotaColetaEntrega.id", "=", criteria.getLong("controleCarga.rotaColetaEntrega.idRotaColetaEntrega"));
		}
		
		if (criteria.getLong("filialByIdFilialDestino.idFilial") != null) {
			sql.addCriteria("manifesto.filialByIdFilialDestino.id", "=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		}
		
		if (criteria.getLong("cliente.idCliente") != null) {
			sql.addCriteria("manifesto.preManifestoDocumentos.doctoServico.clienteByIdClienteConsignatario.id", "=", criteria.getLong("cliente.idCliente"));
		}

		if (criteria.getLong("solicitacaoRetirada.filialRetirada.idFilial") != null) {
			sql.addCriteria("manifesto.solicitacaoRetirada.filialRetirada.id", "=", criteria.getLong("solicitacaoRetirada.filialRetirada.idFilial"));
		}
		
		if (criteria.getLong("solicitacaoRetirada.idSolicitacaoRetirada") != null) {
			sql.addCriteria("manifesto.solicitacaoRetirada.id", "=", criteria.getLong("solicitacaoRetirada.idSolicitacaoRetirada"));
		}
				
		if (criteria.getLong("nrPreManifesto") != null) {
			sql.addCriteria("manifesto.nrPreManifesto", "=", criteria.getLong("nrPreManifesto"));
		}
				
		// Somente busca os manifestos que possuem a Data de Emissão NULA.
		sql.addCustomCriteria("manifesto.dhEmissaoManifesto.value is null");
		
		// Somente busca os manifestos que não estejam Cancelados.
		sql.addCustomCriteria("manifesto.tpStatusManifesto <> 'CA'");
		sql.addCustomCriteria("manifesto.tpStatusManifesto in ('PM', 'EC', 'CC')");
		
		// Somente busca os manifestos que possuem a filial de origem na filial do usuário logado.
		sql.addCriteria("manifesto.filialByIdFilialOrigem.idFilial", "=", SessionUtils.getFilialSessao().getIdFilial()); 
						
		sql.addOrderBy("filialByIdFilialOrigem.sgFilial", "asc");
		sql.addOrderBy("manifesto.nrPreManifesto", "asc");
		
		return sql;
	}

	/**
	 * Método que retorna a queantidade de manifestos no ResultSetPage.
	 * @param criteria
	 * @param findDefinition
	 * @return
	 */
	public Integer getRowCountManifesto(TypedFlatMap criteria) {
		SqlTemplate sql = this.getQueryManifesto(criteria);
		Integer rowCount = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCount;
	}

	/**
	 * Método que retorna o ResultSetPage de Manifestos. 
	 * @param criteria
	 * @param findDefinition
	 * @return
	 */
	public ResultSetPage findPaginatedManifesto(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sql = this.getQueryManifesto(criteria);
		return this.getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(),sql.getCriteria());
	}
 
	/**
	 * Retorna uma list de registros de Manifesto com o ID do Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public List<Manifesto> findManifestoByIdControleCarga(Long idControleCarga, Long idFilialOrigem, String tpStatusManifesto, String tpManifesto) {
		DetachedCriteria dc = DetachedCriteria.forClass(Manifesto.class);
		dc.createAlias("filialByIdFilialDestino", "filialDestino");
		dc.add( Restrictions.eq("controleCarga.id", idControleCarga));

		if (idFilialOrigem != null) {
			dc.add(Restrictions.eq("filialByIdFilialOrigem.id", idFilialOrigem));
		}
		if (!StringUtils.isBlank(tpStatusManifesto)) {
			dc.add(Restrictions.eq("tpStatusManifesto", tpStatusManifesto));
		}
		if (!StringUtils.isBlank(tpManifesto)) {
			dc.add(Restrictions.eq("tpManifesto", tpManifesto));
		}

		dc.setFetchMode("filialByIdFilialOrigem", FetchMode.JOIN);
		return super.findByDetachedCriteria(dc);
	}

	/**
	 * Find para cargas em viagem 
	 * @author Rodrigo Antunes
	 * @param fd
	 * @return
	 */
	public List findReportCargasEmViagem(TypedFlatMap tfm) {
		StringBuffer hql = new StringBuffer("");
		hql.append( addProjectionsToCargasViagem() );
		
		SqlTemplate hqlCriteria = addCriteriaToFindCargasEmViagem(tfm);
		hql.append( hqlCriteria.getSql() );
		
		List list = getAdsmHibernateTemplate().find(hql.toString(), hqlCriteria.getCriteria() );
		List retorno = new AliasToNestedBeanResultTransformer(Manifesto.class).transformListResult(list);
		return retorno;
	}

	/**
	 * Find paginated para cargas em viagem 
	 * @author Rodrigo Antunes
	 * @param fd
	 * @return
	 */
	public ResultSetPage findCargasEmViagem(FindDefinition fd, TypedFlatMap tfm) {
		StringBuffer hql = new StringBuffer("");
		hql.append( addProjectionsToCargasViagem() );

		SqlTemplate hqlCriteria = addCriteriaToFindCargasEmViagem(tfm);
		hql.append( hqlCriteria.getSql() );

		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(hql.toString(), fd.getCurrentPage(), fd.getPageSize(), hqlCriteria.getCriteria() );
		List retorno = new AliasToNestedBeanResultTransformer(Manifesto.class).transformListResult(rsp.getList());

		rsp.setList( retorno );
		return rsp;
	}

	private String addProjectionsToCargasViagem() {
	   	StringBuffer hqlProjections = new StringBuffer("");
		   
	   	hqlProjections.append(" select new map(");
	   	hqlProjections.append("  manifesto.idManifesto as idManifesto, ");
	   	hqlProjections.append("  manifesto.tpManifesto as tpManifesto,");
	   	hqlProjections.append("  manifesto.dhEmissaoManifesto as dhEmissaoManifesto,");
	   	hqlProjections.append("  manifesto.vlTotalManifesto as vlTotalManifesto,");
	   	hqlProjections.append("  manifesto.tpAbrangencia as tpAbrangencia,");
	   	hqlProjections.append("  manifesto.vlTotalManifesto as vlTotalManifesto,");
	   	
	   	hqlProjections.append("  mvn.nrManifestoOrigem as manifestoViagemNacional_nrManifestoOrigem,");
	   	hqlProjections.append("  mi.nrManifestoInt as manifestoInternacional_nrManifestoInt,");
	   	hqlProjections.append("  me.nrManifestoEntrega as manifestoEntrega_nrManifestoEntrega,");
	   	
	   	hqlProjections.append("  ct.idControleTrecho as controleTrecho_idControleTrecho,");
	   	hqlProjections.append("  ct.dhPrevisaoChegada as controleTrecho_dhPrevisaoChegada,");
	   	hqlProjections.append("  ct.dhSaida as controleTrecho_dhSaida,");
	   	hqlProjections.append("  ct.nrTempoViagem as controleTrecho_nrTempoViagem,");
	   	
	   	hqlProjections.append("  filialOrigem.idFilial as filialByIdFilialOrigem_idFilial,");
	   	hqlProjections.append("  filialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial,");
	   	
	   	hqlProjections.append("  filialDestino.idFilial as filialByIdFilialDestino_idFilial,");	   	
	   	hqlProjections.append("  filialDestino.sgFilial as filialByIdFilialDestino_sgFilial,");
	   	
	   	hqlProjections.append("  filialOrigemCC.idFilial as controleCarga_filialByIdFilialOrigem_idFilial,");
	   	hqlProjections.append("  filialOrigemCC.sgFilial as controleCarga_filialByIdFilialOrigem_sgFilial,");
	   	hqlProjections.append("  cc.nrControleCarga as controleCarga_nrControleCarga,");
	   	hqlProjections.append("  cc.idControleCarga as controleCarga_idControleCarga,");
	   	hqlProjections.append("  cc.psTotalFrota as controleCarga_psTotalFrota,");
	   	hqlProjections.append("  cc.pcOcupacaoCalculado as controleCarga_pcOcupacaoCalculado,");
	   	hqlProjections.append("  cc.pcOcupacaoCalculado as controleCarga_pcOcupacaoAforadoCalculado,");
	   	hqlProjections.append("  cc.pcOcupacaoInformado as controleCarga_pcOcupacaoInformado,");
	   	hqlProjections.append("  transportado.nrIdentificador as controleCarga_meioTransporteByIdTransportado_nrIdentificador,");
	   	hqlProjections.append("  transportado.nrFrota as controleCarga_meioTransporteByIdTransportado_nrFrota,");
	   	hqlProjections.append("  transportado.nrCapacidadeKg as controleCarga_meioTransporteByIdTransportado_nrCapacidadeKg,");
	   	
	   	hqlProjections.append("  moeda.sgMoeda as moeda_sgMoeda,");
	   	hqlProjections.append("  moeda.dsSimbolo as moeda_dsSimbolo,");
	   	
	   	hqlProjections.append("  semiRebocado.nrIdentificador as controleCarga_meioTransporteByIdSemiRebocado_nrIdentificador,");
	   	hqlProjections.append("  semiRebocado.nrFrota as controleCarga_meioTransporteByIdSemiRebocado_nrFrota ");
	   	
	   	hqlProjections.append(" )");
	   	
	   	return hqlProjections.toString();
	}

	private SqlTemplate addCriteriaToFindCargasEmViagem(TypedFlatMap tfm) {
		Long idFilialOrigem = tfm.getLong("filialOrigem.idFilial");
		Long idFilialUsuarioLogado = SessionUtils.getFilialSessao().getIdFilial();
		Long idServico = tfm.getLong("servico.idServico");
		Long idControleCarga = tfm.getLong("controleCarga.idControleCarga");
		Long idVeiculo = tfm.getLong("controleCarga.meioTransporteByIdTransportado.idMeioTransporte");
		Long idManifesto = tfm.getLong("manifesto.idManifesto");
		
		StringBuffer hqlQuery = new StringBuffer("");
		hqlQuery.append(" "+Manifesto.class.getName()+" manifesto ");
		hqlQuery.append(" join manifesto.controleCarga cc");
		hqlQuery.append(" left join manifesto.controleTrecho ct");
		hqlQuery.append(" join manifesto.filialByIdFilialOrigem filialOrigem ");
		hqlQuery.append(" join manifesto.filialByIdFilialDestino filialDestino ");
		hqlQuery.append(" left join cc.meioTransporteByIdTransportado transportado ");
		hqlQuery.append(" left join cc.meioTransporteByIdSemiRebocado semiRebocado ");
		hqlQuery.append(" join cc.filialByIdFilialOrigem filialOrigemCC ");
		hqlQuery.append(" join manifesto.moeda moeda");
		
		hqlQuery.append(" left join manifesto.manifestoViagemNacional mvn");
		hqlQuery.append(" left join manifesto.manifestoInternacional mi");
		hqlQuery.append(" left join manifesto.manifestoEntrega me");
		
		SqlTemplate hqlCriteria = new SqlTemplate();
		hqlCriteria.addFrom(hqlQuery.toString());

		hqlCriteria.addCustomCriteria("manifesto.tpManifesto = 'V'");
		hqlCriteria.addCustomCriteria("(manifesto.tpStatusManifesto = 'EV' OR manifesto.tpStatusManifesto = 'EF')");

		// Revisado com o usuario chave, a pesquisa sempre será baseada no manifesto destino 
		// igual ao do usuario logado.
		hqlCriteria.addCriteria("filialDestino.idFilial", "=", idFilialUsuarioLogado);

		// se o campo filial origem da tela foi informado ....
		if (idFilialOrigem!=null) {
			hqlCriteria.addCriteria("filialOrigem.idFilial", "=", idFilialOrigem);
		}
		
		if (idServico!=null) {
			StringBuffer subHql = new StringBuffer()
			.append("exists (")
			.append("select 1 from ").append(PreManifestoDocumento.class.getName()).append(" as pmd ")
			.append("inner join pmd.doctoServico as ds ")
			.append("where ")
			.append("pmd.manifesto.id = manifesto.id ")
			.append("and ds.servico.id = ?) ");

			hqlCriteria.addCustomCriteria(subHql.toString(), idServico);
		}
		
		// Adicionado esta restrição, para que não carregue os manifestos 
		// com controle de cargas igual a "FE" e "CA" 
		hqlCriteria.addCriteria("cc.tpStatusControleCarga", "<>", "FE");
		hqlCriteria.addCriteria("cc.tpStatusControleCarga", "<>", "CA");
		
		if (idControleCarga!=null) {
			hqlCriteria.addCriteria("cc.idControleCarga", "=", idControleCarga);
		}
		
		if (idVeiculo!=null) {
			hqlCriteria.addCriteria("transportado.idMeioTransporte", "=", idVeiculo);
		}
		
		if (idManifesto!=null) {
			hqlCriteria.addCriteria("manifesto.idManifesto", "=", idManifesto);
		}
		
		DateTime chegadaPrevistaInicial = tfm.getDateTime("chegadaPrevistaInicial");
		DateTime chegadaPrevistaFinal = tfm.getDateTime("chegadaPrevistaFinal");
		
		if (chegadaPrevistaInicial!=null) {
			hqlCriteria.addCriteria("ct.dhPrevisaoChegada.value", ">=", chegadaPrevistaInicial);
		}
		
		if (chegadaPrevistaFinal!=null) {
			hqlCriteria.addCriteria("ct.dhPrevisaoChegada.value", "<=", chegadaPrevistaFinal);
		}

		return hqlCriteria;
	}

	/**
	 * 
	 * @param idDoctoServico
	 * @param idFilialUsuario
	 * @return
	 */
	public List findManifestoByDataEmissao(Long idDoctoServico, Long idFilialUsuario,String tpManifesto) {
		Object[] parametersValue = {idDoctoServico, idFilialUsuario}; 
		String[] parametersName = {"idDoctoServico", "idFilialUsuario"};
		List result;
		
		if( tpManifesto == null || "VN".equals(tpManifesto) ){
			// Busca Manifesto de Viagem Nacional através do conhecimento
			StringBuffer sqlVN = new StringBuffer()
			.append("select m from " + Manifesto.class.getName() + " as m ")
			.append("left join m.manifestoViagemNacional as mvn ")
			.append("left join mvn.manifestoNacionalCtos as mnc ")
			.append("left join mnc.conhecimento as cto ")
		.append("where ")
			.append("cto.id = :idDoctoServico and ") 
			.append("(m.filialByIdFilialOrigem.id = :idFilialUsuario or m.filialByIdFilialDestino.id = :idFilialUsuario) and ")
			.append("m.dhEmissaoManifesto.value = ( ")
			.append("select max(m2.dhEmissaoManifesto.value) ") 
			.append("from " + Manifesto.class.getName() + " as m2 ")
			.append("left join m2.manifestoViagemNacional as mvn2 ")
			.append("left join mvn2.manifestoNacionalCtos as mnc2 ")
			.append("left join mnc2.conhecimento as cto2 ")
			.append("where ")
			.append("cto2.id = :idDoctoServico and ")
			.append("(m2.filialByIdFilialOrigem.id = :idFilialUsuario or m2.filialByIdFilialDestino.id = :idFilialUsuario) ") 
		.append(") ");
			result = getAdsmHibernateTemplate().findByNamedParam(sqlVN.toString(), parametersName, parametersValue);
			if( result!= null && result.size() > 0 ){
				return result;
			}
		}

		if( tpManifesto == null || "EN".equals(tpManifesto) ){
			// Busca Manifesto de Entrega através do conhecimento
			StringBuffer sqlEN = new StringBuffer()
			.append("select m from " + Manifesto.class.getName() + " as m ")
			.append("left join m.manifestoEntrega as me ")
			.append("left join me.manifestoEntregaDocumentos as med ")
			.append("left join med.doctoServico as cto ")
			.append("where ")
			.append("cto.id = :idDoctoServico and ") 
			.append("(m.filialByIdFilialOrigem.id = :idFilialUsuario or m.filialByIdFilialDestino.id = :idFilialUsuario) and ")
			.append("m.dhEmissaoManifesto.value = ( ")
			.append("select max(m2.dhEmissaoManifesto.value) ") 
			.append("from " + Manifesto.class.getName() + " as m2 ")
			.append("left join m2.manifestoEntrega as me2 ")
			.append("left join me2.manifestoEntregaDocumentos as med2 ")
			.append("left join med2.doctoServico as cto2 ")
			.append("where ")
			.append("cto2.id = :idDoctoServico and ")
			.append("(m2.filialByIdFilialOrigem.id = :idFilialUsuario or m2.filialByIdFilialDestino.id = :idFilialUsuario) ") 
			.append(") ");
	
			result = getAdsmHibernateTemplate().findByNamedParam(sqlEN.toString(), parametersName, parametersValue);
			if( result!= null && result.size() > 0 ){
		return result;
	}
		}
	
		if( tpManifesto == null || "VI".equals(tpManifesto) ){
			// Busca Manifesto de Viagem Internacional através do conhecimento
			StringBuffer sqlVI = new StringBuffer()
			.append("select m from " + Manifesto.class.getName() + " as m ")
			.append("inner join m.manifestoInternacional as mi ")
			.append("inner join mi.manifestoInternacCtos as mic ")
			.append("inner join mic.ctoInternacional as cto ")
			.append("where ")
			.append("cto.id = :idDoctoServico and ") 
			.append("(m.filialByIdFilialOrigem.id = :idFilialUsuario or m.filialByIdFilialDestino.id = :idFilialUsuario) and ")
			.append("m.dhEmissaoManifesto.value = ( ")
			.append("select max(m2.dhEmissaoManifesto.value) ") 
			.append("from " + Manifesto.class.getName() + " as m2 ")
			.append("inner join m2.manifestoInternacional as mi2 ")
			.append("inner join mi2.manifestoInternacCtos as mic2 ")
			.append("inner join mic2.ctoInternacional as cto2 ")
			.append("where ")
			.append("cto2.id = :idDoctoServico and ")
			.append("(m2.filialByIdFilialOrigem.id = :idFilialUsuario or m2.filialByIdFilialDestino.id = :idFilialUsuario) ") 
			.append(") ");
	
			result = getAdsmHibernateTemplate().findByNamedParam(sqlVI.toString(), parametersName, parametersValue);
			
			if( result!= null && result.size() > 0 ){
				return result;
			}
		}
		return new ArrayList<Manifesto>();
	}
	
	/**
	 * 
	 * @param idDoctoServico
	 * @param idFilialUsuario
	 * @return
	 */
	public List findManifestoViagemFilialDestino(Long idDoctoServico, Long idFilialUsuario) {
		Object[] parametersValue = {idDoctoServico, idFilialUsuario}; 
		String[] parametersName = {"idDoctoServico", "idFilialUsuario"};
		List result;

		// Busca Manifesto de Viagem Nacional através do conhecimento
		StringBuffer sqlVN = new StringBuffer()
		.append("select m from " + Manifesto.class.getName() + " as m ")
		.append("left join m.manifestoViagemNacional as mvn ")
		.append("left join mvn.manifestoNacionalCtos as mnc ")
		.append("left join mnc.conhecimento as cto ")
		.append("where ")
		.append("cto.id = :idDoctoServico and ") 
		.append("m.filialByIdFilialDestino.id = :idFilialUsuario");
		
		result = getAdsmHibernateTemplate().findByNamedParam(sqlVN.toString(), parametersName, parametersValue);
		return result;
	}
	
	/**
	 * 
	 * @param idDoctoServico
	 * @param idFilialUsuario
	 * @return
	 */
	public Manifesto findManifestoViagemByIdDoctoServicoFilialEvento(Long idDoctoServico, Long idFilialEvento) {
		Object[] parametersValue = {idDoctoServico, idFilialEvento}; 
		String[] parametersName = {"idDoctoServico", "idFilialEvento"};

		StringBuffer sqlVN = new StringBuffer()
		.append("select m from " + Manifesto.class.getName() + " as m ")
		.append("left join m.manifestoViagemNacional as mvn ")
		.append("left join m.controleCarga as cc ")
		.append("left join cc.proprietario as pro ")
		.append("left join pro.pessoa as p ")
		.append("left join mvn.manifestoNacionalCtos as mnc ")
		.append("left join mnc.conhecimento as cto ")
		.append("where ")
		.append("cto.id = ? and ") 
		.append("m.filialByIdFilialOrigem.id = ? ")
		.append("order by m.id desc ");
		
		List result = getAdsmHibernateTemplate().find(sqlVN.toString(), new Object[]{idDoctoServico,idFilialEvento});
		if(result != null && !result.isEmpty()){
			return (Manifesto)result.get(0);
		}
		
		return null;
	}

	// LMSA-6253
	public Manifesto findLastManifestoViagemByIdDoctoServico(Long idDoctoServico) {
		Object[] parametersValue = {idDoctoServico}; 
		String[] parametersName = {"idDoctoServico"};

		StringBuffer sqlVN = new StringBuffer()
				.append("select m from " + Manifesto.class.getName() + " as m ")
				.append("left join m.manifestoViagemNacional as mvn ")
				.append("left join m.controleCarga as cc ")
				.append("left join cc.proprietario as pro ")
				.append("left join pro.pessoa as p ")
				.append("left join mvn.manifestoNacionalCtos as mnc ")
				.append("left join mnc.conhecimento as cto ")
				.append("where ")
				.append("cto.id = ? ")
				.append("order by m.dhEmissaoManifesto.value desc ");
		
		List result = getAdsmHibernateTemplate().find(sqlVN.toString(), new Object[]{idDoctoServico});
		
		if(result != null && !result.isEmpty()){
			return (Manifesto)result.get(0);
		}
		
		return null;
	}

	public List findManifestoEntregaFilialOrigem(Long idDoctoServico, Long idFilialUsuario) {
		Object[] parametersValue = {idDoctoServico, idFilialUsuario}; 
		String[] parametersName = {"idDoctoServico", "idFilialUsuario"};
		List result;

		// Busca Manifesto de Entrega através do conhecimento
		StringBuffer sqlEN = new StringBuffer(600)
		.append("select m from " + Manifesto.class.getName() + " as m ")
		.append("left join m.manifestoEntrega as me ")
		.append("left join me.manifestoEntregaDocumentos as med ")
		.append("left join med.doctoServico as cto ")
		.append("left join m.controleCarga as cg ")
		.append("left join cg.motorista as mot ")
		.append("left join mot.usuarioMotorista as usu ")
		.append("left join cg.meioTransporteByIdTransportado as tr ")
		.append("where ")
		.append("cto.id = :idDoctoServico and ") 
		.append("m.filialByIdFilialOrigem.id = :idFilialUsuario");

		result = getAdsmHibernateTemplate().findByNamedParam(sqlEN.toString(), parametersName, parametersValue);
		return result;
	}
	
	public List<Manifesto> findManifestoEntregaAereoFilialOrigem(Long idDoctoServico, Long idManifesto) {
		StringBuilder sql = new StringBuilder();
		
		sql
		.append("select m from " + Manifesto.class.getName() + " as m ")
		.append("left join m.manifestoEntrega as me ")
		.append("left join me.manifestoEntregaDocumentos as med ")
		.append("left join med.doctoServico as ds ")
		.append("left join ds.filialByIdFilialOrigem as f ")
		.append("where ")
		.append("ds.id = :idDoctoServico ") 
		.append("and m.idManifesto = :idManifesto ")
		.append("and med.awb.id is not null ")
		;
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("idManifesto", idManifesto);
		params.put("idDoctoServico", idDoctoServico);
		
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}
	
	/**
	 * 
	 * @param idManifesto
	 * @return
	 */
	public List findManifestoByRNC(Long idManifesto) {
		StringBuffer sql = new StringBuffer()
		.append("select ")  
		.append("new map(filialDestino.idFilial as filialByIdFilialDestino_idFilial, ") 
		.append("empresa.tpEmpresa as filialByIdFilialDestino_empresa_tpEmpresa, ")
		.append("filialOrigem.idFilial as filialByIdFilialOrigem_idFilial, ")
		.append("controleCarga.idControleCarga as controleCarga_idControleCarga, ")
		.append("controleCarga.nrControleCarga as controleCarga_nrControleCarga, ")
		.append("filialControleCarga.idFilial as controleCarga_filialByIdFilialOrigem_idFilial, ")
		.append("filialControleCarga.sgFilial as controleCarga_filialByIdFilialOrigem_sgFilial, ")
		.append("transportado.idMeioTransporte as controleCarga_meioTransporteByIdTransportado_idMeioTransporte, ")
		.append("transportado.nrFrota as controleCarga_meioTransporteByIdTransportado_nrFrota, ")
		.append("transportado.nrIdentificador as controleCarga_meioTransporteByIdTransportado_nrIdentificador, ")
		.append("rebocado.idMeioTransporte as controleCarga_meioTransporteByIdSemiRebocado_idMeioTransporte, ")
		.append("rebocado.nrFrota as controleCarga_meioTransporteByIdSemiRebocado_nrFrota, ")
		.append("rebocado.nrIdentificador as controleCarga_meioTransporteByIdSemiRebocado_nrIdentificador) ")
		.append("from " + Manifesto.class.getName() + " as manifesto ")
		.append("inner join manifesto.filialByIdFilialDestino as filialDestino ")
		.append("inner join filialDestino.empresa as empresa ")
		.append("inner join manifesto.filialByIdFilialOrigem as filialOrigem ")
		.append("inner join manifesto.controleCarga as controleCarga ")
		.append("inner join controleCarga.filialByIdFilialOrigem as filialControleCarga ")
		.append("left join controleCarga.meioTransporteByIdTransportado as transportado ")
		.append("left join controleCarga.meioTransporteByIdSemiRebocado as rebocado ")
		.append("where ")
		.append("manifesto.id = :idManifesto ");

		String parameterName = "idManifesto";
		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parameterName, idManifesto);
		return result;
	}
	
	public Long findManifestosByIdControleCarga(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT count(*) ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("where man.controleCarga.idControleCarga = ? ");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga});
	
		return result;
	}

	public List<Map<String, Object>> findManifestosEmCarregamentoEConcluidoByIdControleCarga(Long idControleCarga) {
		StringBuilder sql = new StringBuilder()
		.append("Select new map(man.nrPreManifesto as nrPreManifesto, filialOrigem.sgFilial as sgFilial, man.tpStatusManifesto as tpStatusManifesto) ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("join man.filialByIdFilialOrigem filialOrigem ")
		.append("where man.controleCarga.id = ? ")
		.append("and man.tpStatusManifesto in ('CC','EC','PM') ");
	
		List<Map<String, Object>> result = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga});
	
		return result;
	}
	
	public List<Manifesto> findByIds(List<Long> ids) {
		Criteria criteria = createCriteria();
		criteria.add(Restrictions.in("idManifesto", ids));
		return criteria.list();
	}
	
	/**
	 * Verifica se todos os manifestos relacionados ao controle de carga foram emitidos.
	 * @param idControleCarga
	 * @return TRUE se existe, FALSE caso contrário
	 */
	public Boolean findVerificaExisteManifestoNaoEmitido(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("select count(*) ")  
		.append("from " + Manifesto.class.getName() + " as manifesto ")
		.append("where ")
		.append("manifesto.dhEmissaoManifesto.value is null ")
		.append("and manifesto.controleCarga.id = ? ")
		.append("and manifesto.tpStatusManifesto <> ? ");

		Object obj[] = new Object[2];
		obj[0] = idControleCarga;
		obj[1] = "CA";
		List result = getAdsmHibernateTemplate().find(sql.toString(), obj);
		return Boolean.valueOf( ((Long)result.get(0)).longValue() > 0 );
	}

	/**
	 * Verifica se há manifestos relacionados ao controle de carga.
	 * @param idControleCarga
	 * @return TRUE se existe, FALSE caso contrário
	 */
	public Boolean findVerificaManifestosAssociados(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("select count(*) ")  
		.append("from " + Manifesto.class.getName() + " as manifesto ")
		.append("where ")
		.append("manifesto.dhEmissaoManifesto.value is not null ")
		.append("and manifesto.controleCarga.id = ? ");

		Object obj[] = new Object[1];
		obj[0] = idControleCarga;
		List result = getAdsmHibernateTemplate().find(sql.toString(), obj);
		return Boolean.valueOf( ((Long)result.get(0)).longValue() > 0 );
	}

	/**
	 * Altera a situacao do manifesto.
	 * Caso o parâmetro blUpdateDhEmissao seja true, atualiza também o valor de dhEmissaoManifesto.
	 * 
	 * @param idManifesto
	 * @param tpSituacao
	 * @param blUpdateDhEmissao
	 * @param dhEmissaoManifesto
	 */
	private void updateSituacaoManifesto(final Long idManifesto, final String tpSituacao,
			final boolean blUpdateDhEmissao, final DateTime dhEmissaoManifesto, final boolean blLimpaRetirada){
		
		HibernateCallback updateSituacao = new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuilder update = new StringBuilder() 
				.append(" update ")
				.append(Manifesto.class.getName()).append(" m")
				.append(" set m.tpStatusManifesto = :parTpStatus");
				if (blUpdateDhEmissao) {
					update.append(", m.dhEmissaoManifesto.value = :parDhEmissaoManifesto");
				}
				if( blLimpaRetirada ){
					update.append(", m.solicitacaoRetirada = null");
				}

				update.append(" where m.id = :parId ");

				Query query = session.createQuery(update.toString())
						.setString("parTpStatus", tpSituacao)
						.setLong("parId", idManifesto.longValue());

				if (blUpdateDhEmissao) {
					query = query.setParameter("parDhEmissaoManifesto", dhEmissaoManifesto, Hibernate.custom(JodaTimeDateTimeUserType.class));
				}

				query.executeUpdate();

				return null;
			}
		};

		getAdsmHibernateTemplate().execute(updateSituacao);
	}

	/**
	 * Altera a situacao do manifesto
	 * @param idManifesto
	 * @param tpSituacao
	 */
	public void updateSituacaoManifesto(Long idManifesto, String tpSituacao,boolean limpaRetirada){
		this.updateSituacaoManifesto(idManifesto,tpSituacao,false,null,limpaRetirada);
	}
	
	/**
	 * Altera a situação do manifesto e a data de emissão.
	 * @param idManifesto
	 * @param tpSituacao
	 * @param dhEmissaoManifesto
	 */
	public void updateSituacaoManifesto(Long idManifesto, String tpSituacao, DateTime dhEmissaoManifesto){
		this.updateSituacaoManifesto(idManifesto,tpSituacao,true,dhEmissaoManifesto,false);
	}

	/**
	 * Verifica se o manifesto se o controle de carga possui data de saida de coleta/entrega
	 * @param idManifesto
	 * @return
	 */
	public boolean validateEmissaoSaidaManifesto(Long idManifesto){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("count(*)");
		sql.addInnerJoin(getPersistentClass().getName(), "m");
		sql.addInnerJoin("m.controleCarga", "cc");

		sql.addCriteria("m.id", "=", idManifesto);
		sql.addCustomCriteria("cc.dhSaidaColetaEntrega.value is not null");
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return (result.intValue() > 0);
	}

	/**
	 * Obtém um lista de Manifestos relacionados ao Controle de Carga em questão.<br>
	 * Filtra os Manifestos com tpStatusManifesto: CC(Carregamento Concluido) ou EF(Em Escala da Filial).
	 * @param idControleCarga
	 * @return
	 */
	public List findManifestosCCorEFByIdControleCarga(Long idControleCarga) {
		StringBuffer sb = new StringBuffer()
		.append("from "+Manifesto.class.getName()+" man ")
		.append("where man.controleCarga.id = ? ")
		.append("and man.tpStatusManifesto in (?, ?) ");
		return getAdsmHibernateTemplate().find(sb.toString(), new Object[] {idControleCarga, "CC", "EF"});
	}

	public List findManifestosTrechoByIdControleCarga(Long idControleCarga, Long idFilialOrigemTrecho, Long idFilialDestinoTrecho, Byte nrOrdemOrigem, Byte nrOrdemDestino) {
		StringBuffer sb = new StringBuffer()
		.append("select ")
		.append("man as manifesto ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("where man.controleCarga.idControleCarga = ? ")
		.append("and ( ")
		.append("  man.filialByIdFilialOrigem.idFilial = ? ")
		.append("  or man.filialByIdFilialDestino.idFilial = ? ")
		.append("  or ( exists (")
		.append("         select 1 ")
		.append("          from " + ControleTrecho.class.getName() + " as cotr ")
		.append("    inner join cotr.controleCarga as controleCarga ")
		.append("    inner join controleCarga.filialRotaCcs as filialRota ")
		.append("    inner join filialRota.filial as filial ")
		.append("    where controleCarga.idControleCarga = ? ")
		.append("      and filial.idFilial = cotr.filialByIdFilialOrigem.idFilial ")		
		.append("      and cotr.blTrechoDireto = ? ")		
		.append("      and cotr.filialByIdFilialOrigem.idFilial = man.filialByIdFilialOrigem.idFilial ")		
		.append("      and filialRota.nrOrdem < ?  ")		
		.append("     ) ")
		.append("  and exists (")
		.append("          select 1 ")
		.append("           from " + ControleTrecho.class.getName() + " as cotr ")
		.append("     inner join cotr.controleCarga as controleCarga ")
		.append("     inner join controleCarga.filialRotaCcs as filialRota ")
		.append("     inner join filialRota.filial as filial ")
		.append("     where controleCarga.idControleCarga = ? ")
		.append("       and filial.idFilial = cotr.filialByIdFilialOrigem.idFilial ")
		.append("       and cotr.blTrechoDireto = ? ")		
		.append("       and cotr.filialByIdFilialDestino.idFilial = man.filialByIdFilialDestino.idFilial ")		
		.append("       and filialRota.nrOrdem > ? ")		
		.append("      ) ")
		.append("    ) ")
		.append("  ) ");

		List list = getAdsmHibernateTemplate().find(sb.toString(), new Object[] {idControleCarga, idFilialOrigemTrecho, idFilialDestinoTrecho, idControleCarga, Boolean.TRUE, nrOrdemOrigem, idControleCarga, Boolean.TRUE, nrOrdemDestino});
		return list;
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param tpControleCarga
	 * @return
	 */
	public List findPaginatedManifestoByControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("SELECT * FROM ( ")
		.append("select ")
		.append("manifesto.ID_MANIFESTO as ID_MANIFESTO, ")
		.append("filialOrigem.SG_FILIAL as SG_FILIAL, ")
		.append("(case ")
		.append("when manifesto.DH_EMISSAO_MANIFESTO IS NULL then manifesto.NR_PRE_MANIFESTO ")
		.append("when manifesto.TP_MANIFESTO = 'E' then me.NR_MANIFESTO_ENTREGA ")
		.append("when manifesto.TP_MANIFESTO = 'V' and manifesto.TP_ABRANGENCIA = 'N' then mvn.NR_MANIFESTO_ORIGEM ")
		.append("when manifesto.TP_MANIFESTO = 'V' and manifesto.TP_ABRANGENCIA = 'I' then mi.NR_MANIFESTO_INT ")
		.append("end ")
		.append(") as NR_MANIFESTO, ")
		.append("filialdestino.sg_filial 	as SG_FILIAL_DESTINO, ")
		.append("manifesto.TP_MANIFESTO as TP_OPERACAO, ")
		.append("manifesto.TP_MODAL as TP_MODAL, ")
		.append("(case ")
		.append("when manifesto.TP_MANIFESTO = 'E' then manifesto.TP_MANIFESTO_ENTREGA ")
		.append("when manifesto.TP_MANIFESTO = 'V' then manifesto.TP_MANIFESTO_VIAGEM ")
		.append("end ")
		.append(") as TP_MANIFESTO, ")
		.append("manifesto.TP_STATUS_MANIFESTO as STATUS, ")
		.append("moeda.ID_MOEDA as ID_MOEDA, ")
		.append("moeda.SG_MOEDA as SG_MOEDA, ")
		.append("moeda.DS_SIMBOLO as DS_SIMBOLO, ")
		.append(" (CASE WHEN manifesto.TP_MANIFESTO = 'V' THEN (SELECT SUM(D.vl_mercadoria) ")
		.append("  FROM DOCTO_SERVICO D,MANIFESTO_NACIONAL_CTO MNC WHERE ")
		.append(" MNC.ID_CONHECIMENTO = D.ID_DOCTO_SERVICO  ")
		.append(" AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL) ")
		.append(" WHEN manifesto.TP_MANIFESTO = 'E' THEN (SELECT SUM(D.vl_mercadoria) ")
		.append(" FROM DOCTO_SERVICO D,PRE_MANIFESTO_DOCUMENTO PMD WHERE ")
		.append(" PMD.ID_DOCTO_SERVICO = D.ID_DOCTO_SERVICO  ")
		.append(" AND PMD.ID_MANIFESTO = manifesto.ID_MANIFESTO)END) AS VL_MERCADORIA, ")
		.append(" (CASE WHEN manifesto.TP_MANIFESTO = 'V' THEN (SELECT SUM(D.PS_REAL) ")
		.append(" FROM DOCTO_SERVICO D,MANIFESTO_NACIONAL_CTO MNC WHERE ")
		.append(" MNC.ID_CONHECIMENTO = D.ID_DOCTO_SERVICO  ")
		.append(" AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL) ")
		.append(" WHEN manifesto.TP_MANIFESTO = 'E' THEN (SELECT SUM(D.PS_REAL) ")
		.append(" FROM DOCTO_SERVICO D,PRE_MANIFESTO_DOCUMENTO PMD WHERE ")
		.append(" PMD.ID_DOCTO_SERVICO = D.ID_DOCTO_SERVICO  ")
		.append(" AND PMD.ID_MANIFESTO = manifesto.ID_MANIFESTO)END) AS PS_TOTAL_MANIFESTO, ")
		.append(" (CASE WHEN manifesto.TP_MANIFESTO = 'V' THEN (SELECT SUM(D.ps_aforado) ")
		.append(" FROM DOCTO_SERVICO D,MANIFESTO_NACIONAL_CTO MNC WHERE ")
		.append(" MNC.ID_CONHECIMENTO = D.ID_DOCTO_SERVICO ")
		.append(" AND MNC.ID_MANIFESTO_VIAGEM_NACIONAL = MVN.ID_MANIFESTO_VIAGEM_NACIONAL) ")
		.append(" WHEN manifesto.TP_MANIFESTO = 'E' THEN (SELECT SUM(D.ps_aforado) ")
		.append(" FROM DOCTO_SERVICO D,PRE_MANIFESTO_DOCUMENTO PMD WHERE ")
		.append(" PMD.ID_DOCTO_SERVICO = D.ID_DOCTO_SERVICO  ")
		.append(" AND PMD.ID_MANIFESTO = manifesto.ID_MANIFESTO)END) AS PS_TOTAL_AFORADO_MANIFESTO ")
		.append("from ")
		.append("controle_carga cc ")
		.append("inner join manifesto on (manifesto.ID_CONTROLE_CARGA = cc.ID_CONTROLE_CARGA) ")
		.append("inner join filial filialOrigem on (filialOrigem.ID_FILIAL = manifesto.ID_FILIAL_ORIGEM) ")
		.append("INNER JOIN filial filialDestino ON (filialDestino.ID_FILIAL = manifesto.ID_FILIAL_DESTINO) ")
		.append("left join moeda on (moeda.ID_MOEDA = manifesto.ID_MOEDA) ")
		.append("left join manifesto_entrega me on (me.ID_MANIFESTO_ENTREGA = manifesto.ID_MANIFESTO) ") 
		.append("left join manifesto_viagem_nacional mvn on (mvn.ID_MANIFESTO_VIAGEM_NACIONAL = manifesto.ID_MANIFESTO) ")
		.append("left join manifesto_internacional mi on (mi.ID_MANIFESTO_INTERNACIONAL = manifesto.ID_MANIFESTO) ")
		.append("where ")
		.append("manifesto.TP_STATUS_MANIFESTO <> 'CA' ")
		.append("AND cc.ID_CONTROLE_CARGA = ? ")

		.append("UNION ")

		.append("select ")
		.append("mc.ID_MANIFESTO_COLETA as ID_MANIFESTO, ")
		.append("filialOrigem.SG_FILIAL as SG_FILIAL, ")
		.append("mc.NR_MANIFESTO, ")
		.append("NULL AS SG_FILIAL_DESTINO, ")
		.append("'C' as TP_OPERACAO, ")
		.append("NULL as TP_MODAL, ")
		.append("NULL as TP_MANIFESTO, ")
		.append("mc.TP_STATUS_MANIFESTO_COLETA as STATUS, ")
		.append("NULL as ID_MOEDA, ")
		.append("NULL as SG_MOEDA, ")
		.append("NULL as DS_SIMBOLO, ")
		.append("(select sum(pc.vl_Total_Verificado) as valor from ")
		.append("manifesto_coleta mc1 ")
		.append("inner join pedido_coleta pc on (pc.ID_MANIFESTO_COLETA = mc1.ID_MANIFESTO_COLETA) ")
		.append("where mc1.ID_MANIFESTO_COLETA = mc.ID_MANIFESTO_COLETA) ")
		.append("as VL_MERCADORIA, ")
		.append("(select sum(pc.ps_total_verificado) as valor from ")
		.append("manifesto_coleta mc1 ")
		.append("inner join pedido_coleta pc on (pc.ID_MANIFESTO_COLETA = mc1.ID_MANIFESTO_COLETA) ")
		.append("where mc1.ID_MANIFESTO_COLETA = mc.ID_MANIFESTO_COLETA) ")
		.append("as PS_TOTAL_MANIFESTO, ")
		.append("(select sum(pc.ps_total_aforado_verificado) as valor from ")
		.append("manifesto_coleta mc1 ")
		.append("inner join pedido_coleta pc on (pc.ID_MANIFESTO_COLETA = mc1.ID_MANIFESTO_COLETA) ")
		.append("where mc1.ID_MANIFESTO_COLETA = mc.ID_MANIFESTO_COLETA) ")
		.append("as PS_TOTAL_AFORADO_MANIFESTO ")

		.append("from ")
		.append("controle_carga cc ")
		.append("inner join manifesto_coleta mc on (mc.ID_CONTROLE_CARGA = cc.ID_CONTROLE_CARGA) ")
		.append("inner join filial filialOrigem on (filialOrigem.ID_FILIAL = mc.ID_FILIAL_ORIGEM) ")
		.append("where ")
		.append("mc.TP_STATUS_MANIFESTO_COLETA <> 'CA' ")
		.append("AND cc.ID_CONTROLE_CARGA = ? ")

		.append(") ")
		.append("ORDER BY ")
		.append("SG_FILIAL, NR_MANIFESTO ");
		
		ConfigureSqlQuery confSql = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_MANIFESTO", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL", Hibernate.STRING);
				sqlQuery.addScalar("NR_MANIFESTO", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("TP_OPERACAO", Hibernate.STRING);
				sqlQuery.addScalar("TP_MODAL", Hibernate.STRING);
				sqlQuery.addScalar("TP_MANIFESTO", Hibernate.STRING);
				sqlQuery.addScalar("STATUS", Hibernate.STRING);
				sqlQuery.addScalar("ID_MOEDA", Hibernate.LONG);
				sqlQuery.addScalar("SG_MOEDA", Hibernate.STRING);
				sqlQuery.addScalar("DS_SIMBOLO", Hibernate.STRING);
				sqlQuery.addScalar("VL_MERCADORIA", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("PS_TOTAL_MANIFESTO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("PS_TOTAL_AFORADO_MANIFESTO", Hibernate.BIG_DECIMAL);
			}
		};

		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(idControleCarga);

		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(), Integer.valueOf(1), Integer.valueOf(10000), param.toArray(), confSql).getList();
	}

	/**
	 * Busca todos os ids dos manifestos que tenham um documento de servico com o servico passado por parâmetro.
	 * @param idServico
	 * @return
	 */
	public List findIdsManifestosByIdServico(Long idServico){
		StringBuffer sql = new StringBuffer();
		sql.append("select manifesto.id from Manifesto as manifesto, ");
		sql.append("PreManifestoDocumento as preManifestoDocumento, ");
		sql.append("DoctoServico as doctoServico ");
		sql.append("where preManifestoDocumento.doctoServico.id = doctoServico.id ");
		sql.append("and preManifestoDocumento.manifesto.id = manifesto.id ");
		sql.append("and doctoServico.servico.id = :idServico");
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),"idServico", idServico);
	}

	/**
	 * Busca todos os pre-manifestos (PM) que tenham a filial origem e filial destino igual as filiais 
	 *  passada por parâmetro associados a um controle de carga com a rotaIdaVolta igual ao id passado por parâmetro.
	 *  Ordenados em ordem decresente de DH_GERACAO do manifesto.
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @param idRotaIdaVolta
	 * @return
	 */
	public List findPreManifestosByFilialOrigemByFilialDestinoByRotaIdaVolta(Long idFilialOrigem, Long idFilialDestino, Long idRotaIdaVolta) {
		StringBuffer sql = new StringBuffer();
		sql.append("select manifesto from Manifesto as manifesto ");
		sql.append("join fetch manifesto.filialByIdFilialOrigem ");
		sql.append("join manifesto.controleCarga mcc ");
		sql.append("where manifesto.filialByIdFilialOrigem.id = :idFilialOrigem ");
		sql.append("and manifesto.filialByIdFilialDestino.id = :idFilialDestino ");
		sql.append("and manifesto.tpStatusManifesto = 'PM' ");
		sql.append("and mcc.rotaIdaVolta.id = :idRotaIdaVolta ");
		sql.append("order by manifesto.dhGeracaoPreManifesto asc ");
		String params[] = {"idFilialOrigem", "idFilialDestino", "idRotaIdaVolta"};
		Object values[] = {idFilialOrigem, idFilialDestino, idRotaIdaVolta};
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),params, values);
	}
	
	/**
	 * Verifica se existe algum manifesto com status diferente de CA e com tpEventoManifesto = "EM" 
	 * que esteja vinculado ao controle de carga recebido por parâmetro.
	 *
	 * @param idControleCarga
	 * @return True se encontrar algum registro, caso contrário, False.
	 */
	 public Boolean validateManifestoByCancelamentoControleCarga(Long idControleCarga){
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(Manifesto.class.getName()).append(" as m ")
		.append("where ")
		.append("m.controleCarga.id = ? ")
		.append("and m.tpStatusManifesto <> 'CA' ")
		.append("and exists (")
			.append(" select 1 from ")
			.append(EventoManifesto.class.getName()).append(" as em ")
			.append("where em.manifesto.id = m.id ")
			.append("and em.tpEventoManifesto = 'EM') ");

		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga});
		return Boolean.valueOf(!rows.equals(0));
	}
	 
	 /**
	  * Método que busca uma lista de manifesto a partir do Id da Solicitação de Retirada.
	  * @param idSolicitacaoRetirada
	  * @return
	  */
	 public List findManifestoByIdSolicitacaoRetirada(Long idSolicitacaoRetirada) {
		 StringBuffer hql = new StringBuffer();
		 hql.append("from " + Manifesto.class.getName() + " as m ");
		 hql.append("where ");
		 hql.append("m.solicitacaoRetirada.id = ? ");		 

		 List result = getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idSolicitacaoRetirada});
		 return result;
	 }
	 
	/**
	 * Alterar o campo TP_STATUS_MANIFESTO para "CA" (Cancelado) na tabela MANIFESTO para o Controle de Carga 
	 * em questão.
	 * 
	 * @param idControleCarga
	 */
	public void updateSituacaoManifestoByIdControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
			.append("update ")
			.append(Manifesto.class.getName()).append(" as m ")
			.append(" set m.tpStatusManifesto = 'CA' ")
			.append("where m.controleCarga.id = ? ");

		List param = new ArrayList();
		param.add(idControleCarga);

		executeHql(sql.toString(), param);
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public List findManifestoNacionalByIdControleCarga(Long idControleCarga, Long idFilial) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(")
		.append("doctoServico.idDoctoServico as idDoctoServico, ")
		.append("doctoServico.tpDocumentoServico as tpDoctoServico, ")
		.append("doctoServico.nrDoctoServico as nrDoctoServico, ")
		.append("filialOrigemDoctoServico.sgFilial as sgFilialOrigemDoctoServico, ")
		.append("ct.dhPrevisaoSaida as dhPrevisaoSaida, ")
		.append("ct.dhPrevisaoChegada as dhPrevisaoChegada, ")
		.append("ct.nrTempoViagem as nrTempoViagem, ")
		.append("filialDestinoManifesto.sgFilial as sgFilialDestinoManifesto, ")
		.append("pessoaFilialDestinoManifesto.nmFantasia as nmFilialDestinoManifesto) ")
		.append("from ")
		.append(Manifesto.class.getName()).append(" manifesto ")
		.append("inner join manifesto.manifestoViagemNacional mvn ")
		.append("inner join mvn.manifestoNacionalCtos mncto ")
		.append("inner join mncto.conhecimento doctoServico ")
		.append("inner join doctoServico.filialByIdFilialOrigem filialOrigemDoctoServico ")
		.append("inner join manifesto.controleTrecho ct ")
		.append("inner join manifesto.filialByIdFilialDestino filialDestinoManifesto ")
		.append("inner join filialDestinoManifesto.pessoa pessoaFilialDestinoManifesto ")
		.append("where ")
		.append("manifesto.controleCarga.id = ? ")
		.append("and manifesto.filialByIdFilialOrigem.id = ? ")
		.append("and manifesto.tpManifesto = 'V' ")
		.append("and manifesto.tpAbrangencia = 'N' ");
		
		List result = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga, idFilial});
		return result;
	}

	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public List findManifestoInternacionalByIdControleCarga(Long idControleCarga, Long idFilial) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(")
		.append("doctoServico.idDoctoServico as idDoctoServico, ")
		.append("doctoServico.tpDocumentoServico as tpDoctoServico, ")
		.append("doctoServico.nrDoctoServico as nrDoctoServico, ")
		.append("filialOrigemDoctoServico.sgFilial as sgFilialOrigemDoctoServico, ")
		.append("ct.dhPrevisaoSaida as dhPrevisaoSaida, ")
		.append("ct.dhPrevisaoChegada as dhPrevisaoChegada, ")
		.append("ct.nrTempoViagem as nrTempoViagem, ")
		.append("filialDestinoManifesto.sgFilial as sgFilialDestinoManifesto, ")
		.append("pessoaFilialDestinoManifesto.nmFantasia as nmFilialDestinoManifesto) ")		
		.append("from ")
		.append(Manifesto.class.getName()).append(" manifesto ")
		.append("inner join manifesto.manifestoInternacional mi ")
   		.append("inner join mi.manifestoInternacCtos micto ")
   		.append("inner join micto.ctoInternacional doctoServico ")
		.append("inner join doctoServico.filialByIdFilialOrigem filialOrigemDoctoServico ")
		.append("inner join manifesto.controleTrecho ct ")
		.append("inner join manifesto.filialByIdFilialDestino filialDestinoManifesto ")
		.append("inner join filialDestinoManifesto.pessoa pessoaFilialDestinoManifesto ")		
		.append("where ")
		.append("manifesto.controleCarga.id = ? ")
		.append("and manifesto.filialByIdFilialOrigem.id = ? ")
		.append("and manifesto.tpManifesto = 'V' ")
		.append("and manifesto.tpAbrangencia = 'I' ");
		
		List result = getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idControleCarga, idFilial});
		return result;
	}
	
	/**
	 * Solicitação da Integração - CQPRO00005515
	 * Criar um método na classe ManifestoService o qual retorne uma instancia da classe Manifesto conforme os parametros especificados.
	 * Nome do metodo: findManifesto(long nrPreManifesto, long idFilialByIdFilialOrigem, String tpManifesto ) : Manifesto
	 * OBS: O parametro tpManifesto é opcional, ou seja o método deve considerar que o valor deste parametro venha setado como null
	 * @param idFilialOrigem
	 * @param nrPreManifesto
	 * @param tpManifesto
	 * @return
	 */
	public Manifesto findManifesto(Long nrPreManifesto, Long idFilialOrigem, String tpManifesto){
   		SqlTemplate hql = new SqlTemplate();		
   		hql.addFrom(Manifesto.class.getName() + " man");
   		hql.addCriteria("man.nrPreManifesto", "=", nrPreManifesto);
   		hql.addCriteria("man.filialByIdFilialOrigem.id", "=", idFilialOrigem);
   		hql.addCriteria("man.tpManifesto", "=", tpManifesto);
   		return (Manifesto)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @return True, se não achou registros, caso contrário, False.
	 */
	public Boolean findManifestoWithDocumentosEntreguesByControleCarga(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(Manifesto.class.getName())
		.append(" as manifesto ")
		.append("inner join manifesto.preManifestoDocumentos as pmd ")
		.append("inner join pmd.doctoServico as ds ")
		.append("inner join ds.localizacaoMercadoria lm ")
		.append("where ")
		.append("manifesto.controleCarga.id = ? ")
		.append("and manifesto.tpManifesto = 'V' ")
		.append("and manifesto.tpManifestoViagem = 'ED' ")
		.append("and not exists (")
		 	.append("select 1 ")
		 	.append("from ")
			.append(EventoDocumentoServico.class.getName()).append(" as eds ")
			.append("inner join eds.evento as evento ")
			.append("where ")
			.append("evento.cdEvento = 21 ")
			.append("and eds.blEventoCancelado = 'N' ")
			.append("and eds.doctoServico.id = ds.id) ");

		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga});
		return Boolean.valueOf(rows.intValue() == 0);
	}
	
	/**
	 * Busca os manifestos para um determinado trecho.
	 * 
	 * @param tpManifesto
	 * @param tpStatusManifesto
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public List findManifestosByTrecho(String tpManifesto, String tpStatusManifesto, Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		this.flush();
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT manifesto ");
		sql.append("FROM ");
		sql.append(Manifesto.class.getName()).append(" manifesto ");
		sql.append("WHERE manifesto.tpManifesto = ? ");
		sql.append("AND manifesto.tpStatusManifesto = ?");
		sql.append("AND manifesto.controleCarga.id = ? ");
		sql.append("AND manifesto.filialByIdFilialOrigem.id = ? ");
		
		if (!"E".equals(tpManifesto))
			sql.append("AND manifesto.filialByIdFilialDestino.id = ? ");

		List parametros = new ArrayList();
		parametros.add(tpManifesto);
		parametros.add(tpStatusManifesto);
		parametros.add(idControleCarga);
		parametros.add(idFilialOrigem);
		
		if (!"E".equals(tpManifesto))
			parametros.add(idFilialDestino);

		return getAdsmHibernateTemplate().find(sql.toString(), parametros.toArray());
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @return
	 */
	public Integer findManifestoByFinalizarCarregamento(Long idControleCarga, Long idFilialOrigem) {
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(Manifesto.class.getName())
		.append(" as manifesto ")
		.append("where ")
		.append("manifesto.controleCarga.id = ? ")
		.append("and manifesto.tpStatusManifesto not in ('CC', 'CA', 'FE') ")
		.append("and manifesto.filialByIdFilialOrigem.id = ? ");
		
		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga, idFilialOrigem});
		return rows;
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idFilialOrigem
	 * @param idFilialDestino
	 * @return
	 */
	public Boolean findExisteManifestoEmitidoByControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(Manifesto.class.getName()).append(" as m ")
		.append("where ")
		.append("m.controleCarga.id = ? ")
		.append("and m.filialByIdFilialOrigem.id = ? ")
		.append("and m.filialByIdFilialDestino.id = ? ")
		.append("and m.tpStatusManifesto in ('ME', 'FE') ");

		List param = new ArrayList();
		param.add(idControleCarga);
		param.add(idFilialOrigem);
		param.add(idFilialDestino);

		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
		return Boolean.valueOf(rows.intValue() > 0);
	}
	
	/**
	 * 
	 * @param idControleCarga
	 * @param idFilial
	 * @return
	 */
	public Boolean validateExisteManifestoNaoFinalizado(Long idControleCarga,Long idFilial) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT count(*) ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("where man.controleCarga.idControleCarga = ? ")
		.append("and man.filialByIdFilialDestino.idFilial = ? ")
		.append("and man.tpStatusManifesto not in ('FE', 'CA') ");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idControleCarga,idFilial,});
		return (result.intValue() > 0);
	}
	public List<Manifesto> findManifestoByIdControleCargaByFilialDestino(Long idControleCarga, Long idFilial) {
		DetachedCriteria dc = DetachedCriteria.forClass(Manifesto.class, "manifesto");
		dc.add(Restrictions.ne("manifesto.tpStatusManifesto", "ED"));
		dc.add(Restrictions.ne("manifesto.tpStatusManifesto", "CA"));
		dc.add(Restrictions.eq("manifesto.controleCarga.id", idControleCarga));
		dc.add(Restrictions.eq("manifesto.filialByIdFilialDestino.id", idFilial));
		return findByDetachedCriteria(dc);
	}

	public List findManifestosEntregaByIdControleCarga(Long idControleCarga, String tpManifesto) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append("SELECT mani ");
		sql.append("from Manifesto mani ");
		sql.append("where mani.controleCarga.idControleCarga = ? ");
		params.add(idControleCarga);
		if(StringUtils.isNotBlank(tpManifesto)) {
			sql.append("and mani.tpStatusManifesto = ? ");
			params.add(tpManifesto);
		}
		return getAdsmHibernateTemplate().find(sql.toString(), params.toArray());
	}
	public Boolean hasManifestosEntregaParceiraByIdControleCargaAndIdFilialOrigem(Long idControleCarga, Long idFilialOrigem) {
		String sql = new StringBuilder()
		.append("FROM Manifesto ")
		.append("WHERE tpManifestoEntrega = 'EP' ")
		.append("AND filialByIdFilialOrigem.id = ? ")
		.append("AND controleCarga.id = ? ")
		.toString();
		
		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql, new Object[]{idFilialOrigem, idControleCarga});
		
		return Boolean.valueOf(rows.intValue() > 0);
	}
	public Boolean findExisteManifestoTipoEntregaByControleCargaAndTpManifestoEntregaIgualEntregaParceira(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("from Manifesto as mani ")
		.append("where mani.controleCarga.id = ? ")
		.append("and mani.tpManifesto = 'E' ")
		.append("and mani.tpManifestoEntrega = 'EP' ")
		.append("and mani.tpStatusManifesto <> 'CA' ");

		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga});
		return Boolean.valueOf(rows.intValue() > 0);
	}
	
	public Boolean findExisteManifestoTipoEntregaByControleCargaAndTpManifestoEntregaDifEntregaParceira(Long idControleCarga) {
		StringBuffer sql = new StringBuffer()
		.append("from Manifesto as mani ")
		.append("where mani.controleCarga.id = ? ")
		.append("and mani.tpManifesto = 'E' ")
		.append("and mani.tpManifestoEntrega <> 'EP' ")
		.append("and mani.tpStatusManifesto <> 'CA' ");

		Integer rows = getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), new Object[]{idControleCarga});
		return Boolean.valueOf(rows != null && rows.intValue() > 0);
	}

	public Boolean findManifestoVolumeSobra(Long idControleCarga, Long idVolumeNotaFiscal, Long idFilial, String tpManifesto) {
		Object[] parametersValue = {idControleCarga, idVolumeNotaFiscal, idFilial, tpManifesto}; 
		String[] parametersName = {"idControleCarga", "idVolumeNotaFiscal", "idFilial", "tpManifesto"};
		StringBuffer sql = new StringBuffer();
		
		if("V".equals(tpManifesto)){
			sql.append("select m from " + Manifesto.class.getName() + " as m ")
			.append("left join m.manifestoViagemNacional as mvn ")
			.append("left join mvn.manifestoNacionalVolumes as mnv ")
			.append("where ")
			.append("mnv.volumeNotaFiscal.id = :idVolumeNotaFiscal and ") 
			.append("m.filialByIdFilialDestino.id = :idFilial and ")
			.append("m.controleCarga.id = :idControleCarga and ")
			.append("m.tpManifesto = :tpManifesto and ")
			.append("m.tpStatusManifesto <> 'CA' ");
		} else if("E".equals(tpManifesto)){
			sql.append("select m from " + Manifesto.class.getName() + " as m ")
			.append("left join m.manifestoEntrega as me ")
			.append("left join me.manifestoEntregaVolumes as mev ")
			.append("where ")
			.append("mev.volumeNotaFiscal.id = :idVolumeNotaFiscal and ") 
			.append("m.filialByIdFilialDestino.id = :idFilial and ")
			.append("m.controleCarga.id = :idControleCarga and ")
			.append("m.tpManifesto = :tpManifesto and ")
			.append("m.tpStatusManifesto <> 'CA' ");
		}
		
		Integer result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parametersName, parametersValue).size();
		return Boolean.valueOf(result.intValue() > 0);
	}
	
	/** 
     * Método responsável por retornar a soma do valor do frete dos documentos de serviço relacionado ao manifesto de Entrega com o id informado
     * LMS-2212
     * 
     * @param idControleCarga
     * @return
     */
    public BigDecimal findSomaVlTotalFreteByIdManifestoEntrega(Long idManifesto) {
    	StringBuilder sql = new StringBuilder();
        
    	sql.append(" select SUM(ds.vlLiquido) ")
        	.append(" from Manifesto m ")
        	.append(" join m.manifestoEntrega me ")
        	.append(" join me.manifestoEntregaDocumentos med ")
        	.append(" join med.doctoServico ds ")
        	.append(" where m.idManifesto = ? ");
        
        Object result = getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {idManifesto});
        return result == null ? BigDecimal.ZERO : (BigDecimal)result;
    }
    
    /**
     * Método responsável por retornar a soma do valor do frete dos documentos de serviço relacionado ao manifesto de Viagem com o id informado
     * LMS-2212
     * @param idControleCarga
     * @return
     */
    public BigDecimal findSomaVlTotalFreteByIdManifestoViagem(Long idManifesto) {
    	StringBuilder sql = new StringBuilder();
        
    	sql.append(" select SUM(c.vlLiquido) ")
	    	.append(" from Manifesto m ")
    		.append(" join m.manifestoViagemNacional mvn ")
    		.append(" join mvn.manifestoNacionalCtos mnc ")
    		.append(" join mnc.conhecimento c  ")
    		.append(" where m.idManifesto = ? ");
        
        Object result = getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[] {idManifesto});
        return result == null ? BigDecimal.ZERO : (BigDecimal)result;
    }
    
    /**
     * LMS-5444
     * @param controleCarga
     * @param idFilialLogada
     * @return
     */
    public List<Manifesto> findManifestoByControleCargaAndStatusAndFilialDestino(ControleCarga controleCarga, Long idFilialLogada) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(Manifesto.class.getName(), "manifesto");
		sql.addCriteria("manifesto.controleCarga.idControleCarga", "=", controleCarga.getIdControleCarga());
		sql.addCriteria("manifesto.tpStatusManifesto", "!=", "CA");
		sql.addCriteria("manifesto.filialByIdFilialDestino.idFilial", "=", idFilialLogada);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
    
    
    /**
	 * LMS 5184 - Busca os manifestos pertencentes ao controle de carga informado 
	 * @param controleCarga
	 * @return
	 */
    public List<Manifesto> findManifestosByControleCarga (ControleCarga controleCarga){
    	SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(Manifesto.class.getName(), "manifesto");
		sql.addCriteria("manifesto.controleCarga.idControleCarga", "=", controleCarga.getIdControleCarga());
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
    
	public DomainValue findTpModalByIdControleCargaIdManifesto(Long idControleCarga, Long idManifesto, boolean idManifestoDiferente) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		StringBuilder sql = new StringBuilder();
        
    	sql.append(" select m.tpModal ");
    	sql.append(" from Manifesto m ");
    	sql.append(" where m.tpStatusManifesto <> 'CA' ");
    	sql.append(" and m.controleCarga.id = :idControleCarga ");
    	params.put("idControleCarga", idControleCarga);
    	
    	if(idManifesto != null){
    		if(idManifestoDiferente){
    			sql.append(" and m.id <> :idManifesto ");
    		}else{
    			sql.append(" and m.id = :idManifesto ");
    		}
        	params.put("idManifesto", idManifesto);
    	}
        
        List<DomainValue> result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
        return result != null && !result.isEmpty() ? (DomainValue)result.get(0) : null ;
	}
	
	/**
	 * Retorna uma list de registros de Manifesto com o ID do Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public List<Manifesto> findManifestoByIdControleCargaAndTpStatusManifesto(Long idControleCarga, List<String> tpStatusManifesto, boolean useNotInStatus) {
		StringBuffer hql = new StringBuffer();
		hql.append("from " + Manifesto.class.getName() + " man")
			.append(" where man.controleCarga.id = :idControleCarga");
		
		if(useNotInStatus){
			hql.append(" and man.tpStatusManifesto not in (:statusManifesto)");
		}else{
			hql.append(" and man.tpStatusManifesto in (:statusManifesto)");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("statusManifesto", tpStatusManifesto);
		
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
	}
	
	/**
	 * Busca quantidades de manifestos que se enquadram na regra de manifestos ok para
	 * encerramento MDFE. 
	 * 
	 * Código parte do processo de otimzação do processo de saída da portaria.
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public Integer findManifestosValidosEncerramentoMDFE(Long idControleCarga) {
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from " + Manifesto.class.getName() + " man")
			.append(" where man.controleCarga.id = :idControleCarga")
			.append(" and (man.tpManifesto <> :tipoManifesto or man.tpStatusManifesto = :statusManifesto )");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("statusManifesto", ConstantesExpedicao.TP_STATUS_MANIFESTO_CANCELADO);
		params.put("tipoManifesto", ConstantesExpedicao.TP_MANIFESTO_ENTREGA_ENTREGA_PARCEIRA);
		
		return getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), params);			
	}
	
	/**
	 * LMS-5261
	 * @param idManifesto
	 * @param idDocumento
	 * @param idFilialDestino
	 * @return
	 */
	public List findDocsManifestadosMesmoDestino(Long idManifesto, Long idDocumento, Long idFilialDestino) {    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("idManifesto", idManifesto);
    	params.put("idDocumento", idDocumento);
    	params.put("idFilialDestino", idFilialDestino);
    	
    	StringBuilder hql = new StringBuilder();
    	hql.append("select m.idManifesto, ds.filialByIdFilialOrigem, ds.nrDoctoServico ");
    	hql.append("from ").append(ManifestoNacionalCto.class.getName()).append(" as mnc, ");
    	hql.append(Manifesto.class.getName()).append(" as m, ");
    	hql.append(DoctoServico.class.getName()).append(" as ds ");
    	hql.append("where m.idManifesto = mnc.manifestoViagemNacional.idManifestoViagemNacional ");
    	hql.append("and mnc.conhecimento.id = ds.idDoctoServico ");
    	hql.append("and m.tpManifesto = 'V' ");
    	hql.append("and m.tpStatusManifesto != 'CA' ");
    	hql.append("and m.idManifesto != :idManifesto ");
    	hql.append("and mnc.conhecimento.id = :idDocumento ");
    	hql.append("and m.filialByIdFilialDestino.idFilial = :idFilialDestino ");
    	
    	return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
    }
	
  public Manifesto findByControleCargaAndDoctoServico(Long idControleCarga, Long idDoctoServico){
    	Criteria criteria = getSession(false).createCriteria(Manifesto.class, "manife");
    	criteria.add(Restrictions.eq("controleCarga.id", idControleCarga));
    	
    	DetachedCriteria sub = DetachedCriteria.forClass(PreManifestoVolume.class, "premani");
    	sub.setProjection(Projections.id());
    	sub.add(Restrictions.eq("doctoServico.id", idDoctoServico));
    	sub.add(Restrictions.eqProperty("premani.manifesto.idManifesto", "manife.idManifesto"));
        
    	criteria.add(Subqueries.exists(sub));
    	List<Manifesto> manifestos = criteria.list();
    	if (manifestos != null && !manifestos.isEmpty()) {
    		return manifestos.get(0);
    	}
    	return null;
    }
	public List<Manifesto> findManifestoByIdControleCargaAndStatusAndFilialOrigem(Long idControleCarga, Long idFilialOrigem) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addFrom(Manifesto.class.getName(), "manifesto");
		sql.addCriteria("manifesto.controleCarga.idControleCarga", "=", idControleCarga);
		sql.addCriteria("manifesto.tpStatusManifesto", "!=", "FE");
		sql.addCriteria("manifesto.tpStatusManifesto", "!=", "CA");
		sql.addCriteria("manifesto.filialByIdFilialOrigem.idFilial", "=", idFilialOrigem);
		
		return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
	}
	
	
	public List<Manifesto> findManifestoEmAbertoByIdControleCarga(Long idControleCarga) {
        SqlTemplate sql = new SqlTemplate();
        
        sql.addFrom(Manifesto.class.getName(), "manifesto");
        sql.addCriteria("manifesto.controleCarga.idControleCarga", "=", idControleCarga);
        sql.addCriteria("manifesto.tpStatusManifesto", "!=", "FE");
        sql.addCriteria("manifesto.tpStatusManifesto", "!=", "CA");
        return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }
	
	public List<Long> findClientesRemetenteByIdManifesto(Long idManifesto){
		StringBuilder query = new StringBuilder()

		.append(" SELECT DISTINCT ds.id_cliente_remetente ")
		.append(" FROM manifesto m, ")
		.append(" 	   pre_manifesto_documento pmd, ")
		.append(" 	   docto_servico ds ")
		.append(" WHERE m.id_manifesto = pmd.id_manifesto ")
		.append(" AND pmd.id_docto_servico = ds.id_docto_servico ")
		.append(" AND m.id_manifesto = "+ idManifesto );
		
		return getSession().createSQLQuery(query.toString()).addScalar("id_cliente_remetente", Hibernate.LONG).list();
	}
	
	public Boolean validateExisteManifestoComProdutoPerigoso(Long idManifesto) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT count(*) ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("inner join man.manifestoViagemNacional mvn ")
		.append("inner join mvn.manifestoNacionalCtos mncto ")
		.append("inner join mncto.conhecimento con ")
		.append("where man.idManifesto = ? ")
		.append("and con.blProdutoPerigoso = 'S' ");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idManifesto});
		return (result.intValue() > 0);
	}
	
	public Boolean validateExisteManifestoComProdutoControlado(Long idManifesto) {
		StringBuilder sql = new StringBuilder()
		.append("SELECT count(*) ")
		.append("from "+Manifesto.class.getName()+" man ")
		.append("inner join man.manifestoViagemNacional mvn ")
		.append("inner join mvn.manifestoNacionalCtos mncto ")
		.append("inner join mncto.conhecimento con ")
		.append("where man.idManifesto = ? ")
		.append("and con.blProdutoControlado = 'S' ");
		
		Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idManifesto});
		return (result.intValue() > 0);
	}
	
	public List<Long> findManifestoEntregaParceira(Long idDoctoServico, Long idFilialOrigemManifesto){
		StringBuilder query = new StringBuilder()

		.append(" select m.id_manifesto from manifesto m ")
		.append(" where trunc(cast(m.DH_EMISSAO_MANIFESTO as date)) = trunc(sysdate) ")
		.append(" and m.TP_MANIFESTO_ENTREGA = 'EP' ")
		.append(" and m.ID_FILIAL_ORIGEM = "+ idFilialOrigemManifesto +" ")
		.append(" and m.TP_STATUS_MANIFESTO = 'TC' ")
		.append(" and m.ID_USUARIO = 5000 ")
		.append(" and not EXISTS( ")
		.append("         select 1 from manifesto_entrega_documento med  ")
		.append("        where m.ID_MANIFESTO = med.ID_MANIFESTO_ENTREGA ")
		.append("        and med.id_docto_servico = "+ idDoctoServico +") ");
		
		return getSession().createSQLQuery(query.toString()).addScalar("id_manifesto", Hibernate.LONG).list();
	}
	
	public Long findIdUltimoManifestoEntregaOrViagemEntregaDireta(final Long idDoctoServico) {
		final StringBuilder sql = new StringBuilder()
		.append(" SELECT MAN.id_manifesto ")
		.append("   FROM manifesto MAN")
		.append("  WHERE MAN.id_manifesto IN ( ")
		.append("    SELECT MNC.id_manifesto_viagem_nacional ")
		.append("      FROM manifesto_nacional_cto MNC, manifesto M ")
		.append("     WHERE MNC.id_manifesto_viagem_nacional = M.id_manifesto ")		
		.append("       AND M.tp_manifesto_viagem = 'ED' ")
		.append("       AND MNC.id_conhecimento = :idDoctoServico ")		
		.append("     UNION ")
		.append("    SELECT MED.id_manifesto_entrega ")
		.append("      FROM manifesto_entrega_documento MED, manifesto M ")
		.append("     WHERE MED.id_manifesto_entrega = M.id_manifesto ")
		.append("       AND MED.id_docto_servico = :idDoctoServico ")
		.append("       AND M.tp_manifesto_entrega IN ('EN', 'EP', 'ED') ")
		.append(" ) ")
		.append(" ORDER BY MAN.dh_emissao_manifesto DESC ");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.setLong("idDoctoServico", idDoctoServico);
				sqlQuery.addScalar("id_manifesto", Hibernate.LONG);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);
				return query.list();
			}
		};
		
		List<Long> retorno = (List<Long>) getHibernateTemplate().execute(hcb);
		return retorno.isEmpty() ? null : retorno.get(0);
	}
}