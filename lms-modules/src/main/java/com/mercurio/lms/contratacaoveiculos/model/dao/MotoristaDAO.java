package com.mercurio.lms.contratacaoveiculos.model.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.TelefoneContato;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.contratacaoveiculos.model.AnexoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.BloqueioMotoristaProp;
import com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspProprietario;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.municipios.model.RotaViagem;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class MotoristaDAO extends BaseCrudDao<Motorista, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return Motorista.class;
	}

	protected void initFindByIdLazyProperties(Map fetchModes) {		
		fetchModes.put("pessoa", FetchMode.JOIN);
		fetchModes.put("usuario", FetchMode.JOIN);
		fetchModes.put("usuario.vfuncionario", FetchMode.JOIN);		
		fetchModes.put("usuarioMotorista", FetchMode.JOIN);
		fetchModes.put("usuarioMotorista.vfuncionario", FetchMode.JOIN);
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);	
		fetchModes.put("municipioNaturalidade", FetchMode.JOIN);
		fetchModes.put("municipioNaturalidade.unidadeFederativa.pais", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa", FetchMode.JOIN);
		fetchModes.put("unidadeFederativa.pais", FetchMode.JOIN);
		fetchModes.put("localEmissaoIdentidade", FetchMode.JOIN);
		fetchModes.put("fotoMotoristas", FetchMode.SELECT);
	} 
 
	protected void initFindLookupLazyProperties(Map fetchModes) {		
		fetchModes.put("pessoa", FetchMode.JOIN);
		fetchModes.put("usuario", FetchMode.JOIN);
		fetchModes.put("usuario.vfuncionario", FetchMode.JOIN);		
		fetchModes.put("usuarioMotorista", FetchMode.JOIN);	
		fetchModes.put("usuarioMotorista.vfuncionario", FetchMode.JOIN);
		fetchModes.put("filial", FetchMode.JOIN);
		fetchModes.put("filial.pessoa", FetchMode.JOIN);
		
	}

	protected void initFindListLazyProperties(Map map) {
		map.put("pessoa", FetchMode.JOIN);
	}
	
	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuery(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;
	}
	
	public void removeMotoristaComplete(Long id) {
		Motorista motorista = (Motorista)super.findById(id);
		motorista.getFotoMotoristas().clear();
		getAdsmHibernateTemplate().delete(motorista);
	}
	
	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDef) {
		SqlTemplate sql = montaQuery(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());		
		return rsp;
	}

	public List findLookupAsPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = montaQuery(criteria);
		ResultSetPage rsp = getAdsmHibernateTemplate()
				.findPaginated(sql.getSql(true), Integer.valueOf(1), Integer.valueOf(2), sql.getCriteria());		
		return rsp.getList();
	}
		
	/**
	 * Retorna um map com as referencias pessoais e profissionais do cadastro do motorista
	 * @param motorista
	 * @return
	 */
	public TypedFlatMap findReferencias(TypedFlatMap motorista){
		Iterator referencias = findReferenciasByIdMotorista((Long)motorista.get("idMotorista")).iterator();

		int i = 1;
		boolean alteradoReferencia = false;
		while (referencias.hasNext()){		
			Object[] linha = (Object[]) referencias.next();
			
			Contato contato = (Contato) linha[0];
			if("RS".equals(contato.getTpContato().getValue()) && !alteradoReferencia){
				i=4;
				alteradoReferencia = true;
			}
			TelefoneContato telefoneContato = (TelefoneContato) linha[1];
			TelefoneEndereco telefoneEndereco = telefoneContato.getTelefoneEndereco();

			motorista.put("ref_idContato_" + i, contato.getIdContato()); 		 
			motorista.put("ref_nome_" + i, contato.getNmContato());
			motorista.put("ref_email_" + i, contato.getDsEmail()); 

			motorista.put("ref_idTelefone_" + i, telefoneEndereco.getIdTelefoneEndereco());			
			motorista.put("ref_tpTelefone_" + i, telefoneEndereco.getTpTelefone().getValue());
			motorista.put("ref_uso_" + i, telefoneEndereco.getTpUso().getValue());
			motorista.put("ref_nrDdi_" + i, telefoneEndereco.getNrDdi());
			motorista.put("ref_nrDdd_" + i, telefoneEndereco.getNrDdd());
			motorista.put("ref_nrTelefone_" + i, telefoneEndereco.getNrTelefone());

			motorista.put("ref_idTelefoneContato_" + i, telefoneContato.getIdTelefoneContato());
			i++;
		}
		return motorista;
	}

	/**
	 * Retorna uma lista com as referencias pessoais e profissionais do cadastro do motorista
	 * @param motorista
	 * @return
	 */
	public List findReferenciasByIdMotorista(Long idMotorista){
		String hql = montaQueryReferencias(idMotorista);

		return this.getAdsmHibernateTemplate().find(hql);
	}
	
	public List findReferenciasByIdMeioTransporte(Long idMeioTransporte){
		String hql = montaQueryMeioTransporte(idMeioTransporte);
		return this.getAdsmHibernateTemplate().find(hql);
	}

	private String montaQueryReferencias(Long idMotorista){
		StringBuffer sql = new StringBuffer();

		sql.append("  from Contato c");
		sql.append("  left join c.telefoneContatos "); 
		sql.append(" where c.pessoa.idPessoa = " + idMotorista);
		sql.append("   and c.tpContato IN ('RS','RP')");
		sql.append(" order by c.idContato");

		return sql.toString();
	}
	
	private String montaQueryMeioTransporte(Long idMeioTranspore){
		StringBuffer sql = new StringBuffer();

		sql.append(" select mot from MeioTranspRodoMotorista mm ");
		sql.append(" join mm.meioTransporteRodoviario mtr ");
		sql.append(" join mtr.meioTransporte mt ");
		sql.append(" join mm.motorista mot");
		sql.append(" where mt.idMeioTransporte = " + idMeioTranspore);
		return sql.toString();
	}

	private SqlTemplate montaQuery(TypedFlatMap criteria){
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("new Map(m.idMotorista", "idMotorista");
		sql.addProjection("m.filial.sgFilial", "filial_sgFilial");
		sql.addProjection("m.filial.pessoa.nmFantasia", "filial_pessoa_nmFantasia"); 
		sql.addProjection("m.pessoa.tpIdentificacao", "pessoa_tpIdentificacao");
		sql.addProjection("m.pessoa.nrIdentificacao", "pessoa_nrIdentificacao");
		sql.addProjection("m.pessoa.nmPessoa", "pessoa_nmPessoa");
		sql.addProjection("m.usuarioMotorista.nrMatricula", "usuarioMotorista_nrMatricula");
		sql.addProjection("m.tpVinculo", "tpVinculo");
		sql.addProjection("m.tpSituacao", "tpSituacao)");

		StringBuilder sqlFrom = new StringBuilder()
		.append(Motorista.class.getName()).append(" m ")
		.append("inner join m.pessoa ")
		.append("left join m.usuarioMotorista ")
		.append("inner join m.filial ")
		.append("inner join m.filial.pessoa ");
		
		String cdCargo = criteria.getString("cdCargo");
		if (StringUtils.isNotEmpty(cdCargo)) {
			sqlFrom.append(" join m.usuarioMotorista.vfuncionario func ");
			sql.addCriteria("func.cdCargo", "=", cdCargo);
		}
		
		List<String> cdFuncoes = criteria.getList("cdFuncoes");
		if (cdFuncoes != null && !cdFuncoes.isEmpty()) {
			sql.addCriteriaIn("func.cdFuncao", cdFuncoes);
		}
		
		sql.addFrom(sqlFrom.toString());

		sql.addCriteria("m.filial.idFilial", "=", criteria.getLong("filial.idFilial"));
		sql.addCriteria("m.tpVinculo", "=", criteria.getString("tpVinculo"));
		sql.addCriteria("m.tpSituacao", "=", criteria.getString("tpSituacao"));
		sql.addCriteria("m.pessoa.tpIdentificacao", "=", criteria.getString("pessoa.tpIdentificacao"));
		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if (StringUtils.isNotEmpty(nrIdentificacao)) {
			sql.addCriteria("lower(m.pessoa.nrIdentificacao)", "like", nrIdentificacao.toLowerCase());
		}
		String nmPessoa = criteria.getString("pessoa.nmPessoa");
		if (nmPessoa != null) {
			sql.addCriteria("lower(m.pessoa.nmPessoa)", "like", nmPessoa.toLowerCase());
		}

		sql.addCriteria("m.dtVencimentoHabilitacao", ">=", criteria.getYearMonthDay("dtVencimentoHabilitacaoInicial"));
 		sql.addCriteria("m.dtVencimentoHabilitacao", "<=", criteria.getYearMonthDay("dtVencimentoHabilitacaoFinal"));
 		sql.addCriteria("m.dtAtualizacao", ">=", criteria.getYearMonthDay("dtAtualizacaoInicial"));
		sql.addCriteria("m.dtAtualizacao", "<=", criteria.getYearMonthDay("dtAtualizacaoFinal"));

		YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
		Long idRotaViagem = criteria.getLong("idRotaViagem");
		if (idRotaViagem != null) {
			sql.addCustomCriteria(new StringBuilder()
			.append("exists (from ").append(RotaViagem.class.getName()).append(" as rv ")
			.append(" inner join rv.motoristaRotaViagems mrv inner join mrv.motorista mv ")
			.append(" where mv.idMotorista = m.idMotorista ")
			.append("   and mrv.dtVigenciaInicial <= ? ")
			.append("   and mrv.dtVigenciaFinal >= ? ")
			.append("   and rv.idRotaViagem = ?)")
			.toString());
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(idRotaViagem);
		}

		Boolean blBloqueado = criteria.getBoolean("blBloqueado");
		if(blBloqueado != null) {
			DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();

			if(blBloqueado) {
				sql.addCustomCriteria("exists (from BloqueioMotoristaProp b where b.motorista.idMotorista = m.idMotorista");
				sql.addCriteria("b.dhVigenciaInicial.value", "<=", dataHoraAtual);
				sql.addCustomCriteria("b.dhVigenciaFinal.value >= ?)", dataHoraAtual);
			} else {
				sql.addCustomCriteria("not exists (from BloqueioMotoristaProp b where b.motorista.idMotorista = m.idMotorista");
				sql.addCriteria("b.dhVigenciaInicial.value", "<=", dataHoraAtual);
				sql.addCustomCriteria("b.dhVigenciaFinal.value >= ?)", dataHoraAtual);
			}
		}

		Long idProprietario = criteria.getLong("proprietario.idProprietario");
		if (idProprietario != null) {
			sql.addCustomCriteria(new StringBuilder()
			.append("exists (from ")
			.append(MeioTranspRodoMotorista.class.getName()).append(" MTM, ")
			.append(MeioTranspProprietario.class.getName()).append(" MTP ")
			.append(" where MTM.meioTransporteRodoviario.id = MTP.meioTransporte.id ")
			.append("   and MTM.motorista.id = m.id ")
			.append("   and MTM.dtVigenciaInicial <= ? ")
			.append("   and MTM.dtVigenciaFinal >= ? ")
			.append("   and MTP.dtVigenciaInicial <= ? ")
			.append("   and MTP.dtVigenciaFinal >= ? ")
			.append("   and MTP.proprietario.id = ?)")
			.toString());
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(dataAtual);
			sql.addCriteriaValue(idProprietario);
		}

		sql.addOrderBy("case when m.tpVinculo = 'F' then 0 when m.tpVinculo = 'A' then 1 when m.tpVinculo = 'E' then 2 end");
		sql.addOrderBy("m.pessoa.nmPessoa");

		return sql;
	}

	/**
	 * Consulta a existencia de registros de bloqueios vigentes para o motorista
	 * @param idMotorista
	 * @return TRUE se existem bloqueios e FALSE caso contrario
	 */
	public boolean verificaBloqueio(Long idMotorista){
		DetachedCriteria dc = DetachedCriteria.forClass(BloqueioMotoristaProp.class);

		DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
		dc.add(Restrictions.eq("motorista.idMotorista", idMotorista));
		dc.add(Restrictions.le("dhVigenciaInicial", dataHoraAtual));
		dc.add(Restrictions.ge("dhVigenciaFinal", dataHoraAtual));
		dc.setProjection(Projections.rowCount());

		Integer result = (Integer)getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	/**
	 * Consulta se existe registro de liberacao para o motorista
	 * @param idMotorista
	 * @return TRUE se existem registros de liberacao e FALSE caso contrario
	 */
	public boolean verificaLiberacaoMotorista(Long idMotorista){
		DetachedCriteria dc = DetachedCriteria.forClass(LiberacaoReguladora.class);
		dc.add(Restrictions.eq("motorista.idMotorista", idMotorista));
		dc.add(Restrictions.ge("dtVencimento", JTDateTimeUtils.getDataAtual()));
		dc.setProjection(Projections.count("id"));

		Integer result = (Integer)getAdsmHibernateTemplate().findUniqueResult(dc);
		return (result.intValue() > 0);
	}

	/**
	 * Consulta registro de controle de carga aberto associado ao motorista
	 * @param idMotorista
	 * @return
	 */
	public List<ControleCarga> consultaControleCarga(Long idMotorista) {
		DetachedCriteria dc = DetachedCriteria.forClass(ControleCarga.class, "cc");
		dc.add(Restrictions.eq("cc.motorista.id", idMotorista));
		dc.add(Restrictions.not(Restrictions.in("cc.tpStatusControleCarga", new String[]{"CA", "FE"})));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public List<String> carregaListaProjecao() {
		List<String> campos = new ArrayList<String>();
		campos.add("idMotorista");
		campos.add("tpSituacao");
		campos.add("tpVinculo");
		campos.add("filial.siglaNomeFilial");
		campos.add("pessoa.nrIdentificacao");
		campos.add("pessoa.nrIdentificacaoFormatado");
		campos.add("pessoa.tpPessoa");
		campos.add("pessoa.nmPessoa");
		campos.add("pessoa.tpIdentificacao");		
		campos.add("usuarioMotorista.nrMatricula");

		return campos;
	}

	//regra 3.24 - dois motoristas nao podem ter o mesmo numero de matricula
	public boolean findUsuarioMotoristaById(Long idMotorista, Long idUsuarioMotorista){
		DetachedCriteria dc = createDetachedCriteria();
		if(idMotorista != null){
			dc.add(Restrictions.ne("idMotorista",idMotorista));
		}
		dc.add(Restrictions.eq("usuarioMotorista.idUsuario",idUsuarioMotorista));
		return findByDetachedCriteria(dc).size()>0;
	}

	public List findLookupPersonalizado(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		List<Object> paramValues = new ArrayList<Object>(5);

		hql.append(" select new Map(")
				.append("pes.nmPessoa as pessoa_nmPessoa, ")
				.append("pes.tpIdentificacao as pessoa_tpIdentificacao, ")
				.append("mot.idMotorista as idMotorista, ")
				.append("mot.tpVinculo as tpVinculo, ")
				.append("pes.nrIdentificacao as pessoa_nrIdentificacao) ");

		hql.append(" from ").append(Motorista.class.getName()).append(" mot ")
				.append(" join mot.pessoa pes ");

		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if (nrIdentificacao != null) {
			if (nrIdentificacao.contains("%")) {
				hql.append("where pes.nrIdentificacao like ?");
			} else {
				hql.append("where pes.nrIdentificacao = ?");
			}
		}
		paramValues.add(nrIdentificacao);

		Long idProprietario = criteria.getLong("proprietario.idProprietario");
		if (idProprietario != null) {
			hql.append(" and exists (from ")
			.append(MeioTranspRodoMotorista.class.getName()).append(" MTM, ")
			.append(MeioTranspProprietario.class.getName()).append(" MTP ")
			.append(" where MTM.meioTransporteRodoviario.id = MTP.meioTransporte.id ")
			.append("   and MTM.motorista.id = mot.id ")
			.append("   and MTM.dtVigenciaInicial <= ? ")
			.append("   and MTM.dtVigenciaFinal >= ? ")
			.append("   and MTP.dtVigenciaInicial <= ? ")
			.append("   and MTP.dtVigenciaFinal >= ? ")
			.append("   and MTP.proprietario.id = ?)");
			YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();
			paramValues.add(dtToday);
			paramValues.add(dtToday);
			paramValues.add(dtToday);
			paramValues.add(dtToday);
			paramValues.add(idProprietario);
		}

		return getAdsmHibernateTemplate().find(hql.toString(), paramValues.toArray(), "+ leading(pessoa1_) Index(pessoa1_) use_nl(pessoa1_,motorista0_)");
	}
	
	public List findLookupInstrutorPersonalizado(TypedFlatMap criteria) {
		final String cdCargoInstrutores = "025";
		final String cdFuncaoInstrutorMotorista = "025.0002";
		final String cdFuncaoMonitorMotorista = "025.0004";
		final String cdFuncaoInstrutorTreinamentoMotorista = "025.0015";
		
		StringBuilder hql = new StringBuilder();
		List<Object> paramValues = new ArrayList<Object>(5);

		hql.append(" select new Map(")
				.append("pes.nmPessoa as pessoa_nmPessoa, ")
				.append("pes.tpIdentificacao as pessoa_tpIdentificacao, ")
				.append("mot.idMotorista as idMotorista, ")
				.append("mot.tpVinculo as tpVinculo, ")
				.append("pes.nrIdentificacao as pessoa_nrIdentificacao) ");

		hql.append(" from ").append(Motorista.class.getName()).append(" mot ")
				.append(" join mot.pessoa pes ")
				.append(" join mot.usuarioMotorista.vfuncionario func ");

		String nrIdentificacao = criteria.getString("pessoa.nrIdentificacao");
		if (nrIdentificacao != null) {
			if (nrIdentificacao.contains("%")) {
				hql.append("where pes.nrIdentificacao like ?");
			} else {
				hql.append("where pes.nrIdentificacao = ?");
			}
		}
		paramValues.add(nrIdentificacao);
		
		// Somente cargo de INSTRUTORES
		hql.append(" and func.cdCargo = ?");
		paramValues.add(cdCargoInstrutores);
		
		// Somente Instrutor de Motorista e Monitor de Motorista
		hql.append(" and func.cdFuncao in (?,?,?)");
		paramValues.add(cdFuncaoInstrutorMotorista);
		paramValues.add(cdFuncaoMonitorMotorista);
		paramValues.add(cdFuncaoInstrutorTreinamentoMotorista);

		Long idProprietario = criteria.getLong("proprietario.idProprietario");
		if (idProprietario != null) {
			hql.append(" and exists (from ")
			.append(MeioTranspRodoMotorista.class.getName()).append(" MTM, ")
			.append(MeioTranspProprietario.class.getName()).append(" MTP ")
			.append(" where MTM.meioTransporteRodoviario.id = MTP.meioTransporte.id ")
			.append("   and MTM.motorista.id = mot.id ")
			.append("   and MTM.dtVigenciaInicial <= ? ")
			.append("   and MTM.dtVigenciaFinal >= ? ")
			.append("   and MTP.dtVigenciaInicial <= ? ")
			.append("   and MTP.dtVigenciaFinal >= ? ")
			.append("   and MTP.proprietario.id = ?)");
			YearMonthDay dtToday = JTDateTimeUtils.getDataAtual();
			paramValues.add(dtToday);
			paramValues.add(dtToday);
			paramValues.add(dtToday);
			paramValues.add(dtToday);
			paramValues.add(idProprietario);
		}

		return getAdsmHibernateTemplate().find(hql.toString(), paramValues.toArray(), "+ leading(pessoa1_) Index(pessoa1_) use_nl(pessoa1_,motorista0_)");
	}

	/**
	 * @see isMotorista(Long idPessoa, String tpVinculo)
	 */
	public boolean isMotorista(Long idPessoa){
		return isMotorista(idPessoa, null);
	}

	/**
	 * Retorna 'true' se a pessoa informada é um motorista ativo diferente do vinculo informado senão, retorna 'false'.
	 * 
	 * @author Mickaël Jalbert
	 * @since 21/08/2006
	 * 
	 * @param Long idPessoa
	 * @return boolean
	 */
	public boolean isMotorista(Long idPessoa, String tpVinculo){

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "mo");
		dc.setProjection(Projections.rowCount());
		dc.add(Restrictions.eq("mo.id", idPessoa));
		dc.add(Restrictions.eq("mo.tpSituacao", "A"));
		dc.add(Restrictions.ne("mo.tpVinculo", tpVinculo));

		Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);

		return (result.intValue() > 0);
	}
	
	
	/**
	 * faz o update do blTermoCompromisso para true quando é emitido o pdf de "Termo Compromisso"
	 * @param idMotorista
	 * @param blTermoComp
	 */
	public void updateBlTermoComp(Long idMotorista, boolean blTermoComp) {
		getAdsmHibernateTemplate().bulkUpdate("update "+Motorista.class.getName()+" moto " +
												" set moto.blTermoComp = ? where moto.idMotorista = ?",
												new Object[] {blTermoComp, idMotorista});
	}
	
	public void removeByIdsAnexoMotorista(List<Long> ids) {
		getAdsmHibernateTemplate().removeByIds("DELETE FROM " + AnexoMotorista.class.getName() + " WHERE idAnexoMotorista IN (:id)", ids);
	}

	public AnexoMotorista findAnexoMotoristaById(Long idAnexoMotorista) {
		return (AnexoMotorista) getAdsmHibernateTemplate().load(AnexoMotorista.class, idAnexoMotorista);
	}
	
	@SuppressWarnings("unchecked")
	public ResultSetPage<AnexoMotorista> findPaginatedAnexoMotorista(PaginatedQuery paginatedQuery) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT new Map(");
		hql.append(" anexo.idAnexoMotorista AS idAnexoMotorista,");
		hql.append(" anexo.nmArquivo AS nmArquivo,");
		hql.append(" anexo.dsAnexo AS dsAnexo,");
		hql.append(" anexo.dhCriacao AS dhInclusao,");
		hql.append(" usuario.usuarioADSM.nmUsuario as nmUsuario)");
		hql.append(" FROM AnexoMotorista AS anexo");
		hql.append("  INNER JOIN anexo.motorista motorista");
		hql.append("  INNER JOIN anexo.usuario usuario");
		hql.append(" WHERE motorista.idMotorista = :idMotorista");
		hql.append(" ORDER BY anexo.dhCriacao.value DESC ");
		return getAdsmHibernateTemplate().findPaginated(paginatedQuery, hql.toString());
	}
	
	public Integer getRowCountAnexoMotorista(TypedFlatMap criteria) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM anexo_motorista WHERE id_motorista = ?");
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{criteria.get("idMotorista")});
	}
	
	/**
	 * Define a data e o usuário de alteração do registro. 
	 * 
	 * @param idMotorista
	 * @param idUsuario
	 */
	public void updateMotoristaAlteracao(Long idMotorista, Long idUsuario) {
		StringBuilder hql = new StringBuilder();
		hql.append("UPDATE Motorista AS motorista");
		hql.append(" SET motorista.usuarioAlteracao.idUsuario = ?, motorista.dtAtualizacao = CURRENT_DATE()");
		hql.append(" WHERE motorista.idMotorista = ?");
		
		List<Object> parametersValues = new ArrayList<Object>();		
		parametersValues.add(idUsuario);
		parametersValues.add(idMotorista);
		
		executeHql(hql.toString(), parametersValues);
	}

	@Override
	protected ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select mot.id_motorista, pes.tp_identificacao, pes.nr_identificacao, pes.nm_pessoa ");
		sql.append("from motorista  mot, pessoa  pes ");
		sql.append("where pes.id_pessoa = mot.id_motorista ");
		
		String value = filter.get("value").toString();
		if (value.matches("[\\p{Digit}\\p{Punct}]+")) {
			filter.put("value", value.replaceAll("[^\\p{Digit}]", "") + "%");
			sql.append("AND pes.nr_identificacao LIKE :value ");
		} else {
			filter.put("value", "%" + value.replaceAll("[^\\p{Alnum}]+", "%").toUpperCase() + "%");
			sql.append("AND UPPER(pes.nm_pessoa) LIKE :value ");
		}
		
		return new ResponseSuggest(sql.toString(), filter);
	}

}