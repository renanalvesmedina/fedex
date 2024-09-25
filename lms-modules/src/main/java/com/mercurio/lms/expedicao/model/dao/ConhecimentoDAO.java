package com.mercurio.lms.expedicao.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.DomainCompositeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeDateTimeUserType;
import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.ResponseSuggest;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.entrega.model.AgendamentoEntrega;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDescarga;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.model.NfDadosComp;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ServicoGeracaoAutomatica;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.expedicao.util.ConstantesAwb;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.OcorrenciaDoctoServico;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.dto.DadosEmbarquePipelineDTO;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;

/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean
 */
public class ConhecimentoDAO extends BaseCrudDao<Conhecimento, Long> {

	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected final Class getPersistentClass() {
		return Conhecimento.class;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateConhecimentosDuplicadosSom(final List<Long> idsDuplicados) {
		Map criteria = new HashMap<String, List>();
		criteria.put("id", idsDuplicados);

		List list = new LinkedList();
		DetachedCriteria dc = DetachedCriteria.forClass(Conhecimento.class, "conh")
		.add(Restrictions.in("conh.id", idsDuplicados));
		list.add(dc);
		List<Conhecimento> conhecimentos = findByDetachedCriteria(dc);
		for (Conhecimento conhecimento : conhecimentos) {
			conhecimento.setTpSituacaoConhecimento(new DomainValue("D"));
			store(conhecimento);
		}
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindLookupLazyProperties(Map map) {
		map.put("moeda", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		map.put("servico",FetchMode.JOIN);

	}

	/**
	 * Busca os dados do conhecimento fazendo fetch joins para evitar o problema
	 * de N+1 selects no banco.
	 *
	 * @param idConhecimento
	 *            identificador do conhecimento a ser buscado
	 * @return conhecimento e dados relevantes
	 */
	public Conhecimento findByIdEager(final Long idConhecimento) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select c from ");
		hql.append(Conhecimento.class.getName());
		hql.append(" c join fetch c.moeda m ");
		hql.append(" join fetch c.notaFiscalConhecimentos nfcs ");
		hql.append(" join fetch c.clienteByIdClienteRemetente cr ");
		hql.append(" where c.id = ? ");
		Conhecimento result = (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(),
				new Object[] { idConhecimento });
		return result;
	}

	
	
	public Conhecimento findByIdJoinLocalizacaoMercadoria(final Long idConhecimento) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select c from ");
		hql.append(Conhecimento.class.getName());
		hql.append(" c join fetch c.localizacaoMercadoria localizacao ");
		hql.append(" left outer join fetch c.servico as servico ");
		hql.append(" join fetch c.filialByIdFilialOrigem filialByIdFilialOrigem ");
		hql.append(" join fetch c.filialByIdFilialDestino filialByIdFilialDestino ");
		hql.append(" join fetch c.clienteByIdClienteRemetente remetente "); 
		hql.append(" join fetch remetente.pessoa pessoaRemetente ");
		hql.append(" join fetch c.clienteByIdClienteDestinatario destinatario ");
		hql.append(" join fetch destinatario.pessoa pessoaDestinatario ");
		hql.append(" join fetch c.pedidoColeta pedidoColeta ");
		hql.append(" where c.id = ? ");
		Conhecimento result = (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(),
				new Object[] { idConhecimento });
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResultSetPage findPaginatedDevedorDocServFat(Map criteria, FindDefinition findDef){
		SqlTemplate sql = getSqlConhecimentoByDevedorDocServFat(criteria);
		sql.addProjection( new StringBuffer()
				.append(" new Map(cto.nrConhecimento as nrDocumento, cto.filialByIdFilialOrigem.idFilial as filial_idFilial, ")
				.append(" cto.filialByIdFilialOrigem.sgFilial as filial_sgFilial, doc.cliente.pessoa.nmPessoa as cliente_nmCliente, ")
				.append(" doc.vlDevido as vlDevido, des.vlDesconto as vlDesconto, doc.tpSituacaoCobranca as tpSituacaoCobranca ")
				.append(" ) ")
				.toString()
			);
		ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(
				sql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), sql.getCriteria());
		List list = rsp.getList();
		list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		rsp.setList(list);
		return rsp;
	}

	public Integer getRowCountDevedorDocServFat(Map criteria) {
		SqlTemplate sql = getSqlConhecimentoByDevedorDocServFat(criteria);
		sql.addProjection("count(cto.id)");
		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
		return result.intValue();
	}

	
	public List<Conhecimento> findPreConhecimentoByIdControleCarga(long idControleCarga){
		String hql = "select c from PreManifestoDocumento pmd, Conhecimento c " +
				"	join pmd.manifesto m " +
				"	join pmd.doctoServico ds " +
				"	join m.controleCarga cc " +
				" where c.tpSituacaoConhecimento = 'P' " +
				"	and c.id = ds.id " +
				"	and cc.id = ?";
		return getAdsmHibernateTemplate().find(hql, new Object[]{idControleCarga});
	}

	/**
	 * Retorna o tpFrete do conhecimento informado.
	 *
	 * @author Mickaël Jalbert
	 * @since 28/06/2006
	 *
	 * @param Long idConhecimento
	 * @return DomainValue
	 * */
	public DomainValue findTpFreteByIdConhecimento(Long idConhecimento){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("c.tpFrete");
		hql.addInnerJoin(Conhecimento.class.getName(), "c");
		hql.addCriteria("c.id", "=", idConhecimento);

		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()){
			return (DomainValue)list.get(0);
		}
		return null;
	}

	/**
	 * Retorna o tpFrete e tpSituacao do conhecimento informado.
	 *
	 * @author Mickaël Jalbert
	 * @since 10/01/2007
	 *
	 * @param idConhecimento
	 * @return map(tpFrete, tpSituacaoConhecimento, idFilial)
	 * */
	public Map findTpFreteTpSituacaoIdFilialByIdConhecimento(Long idConhecimento){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("new Map(c.tpFrete as tpFrete, c.tpSituacaoConhecimento as tpSituacaoConhecimento, c.filialByIdFilialDestino.idFilial as idFilialDestino)");
		hql.addInnerJoin(Conhecimento.class.getName(), "c");
		hql.addCriteria("c.id", "=", idConhecimento);
		

		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()){
			return (Map)list.get(0);
		}
		return null;
	}

	public Integer updateQtEtiquetasEmitidasByQtEtiquetas(Long idConhecimento, Integer qtEtiquetas) {
		String sql = "select conh from Conhecimento as conh where conh.id = ?";
		Conhecimento conhecimento = (Conhecimento) getAdsmHibernateTemplate().findUniqueResultForUpdate(sql, new Object[]{idConhecimento}, "conh");
		Integer qtEtiquetasEmitidas = conhecimento.getQtEtiquetasEmitidas();
		conhecimento.setQtEtiquetasEmitidas(conhecimento.getQtEtiquetasEmitidas() + qtEtiquetas);

		store(conhecimento);

		return qtEtiquetasEmitidas;
	}

	/**
	 * Retorna o tpConhecimento do conhecimento informado.
	 *
	 * @author Salete
	 * @since 06/09/2006
	 *
	 * @param Long idConhecimento
	 * @return DomainValue
	 * */
	public DomainValue findTpConhecimentoByIdConhecimento(Long idConhecimento){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("c.tpConhecimento");
		hql.addInnerJoin(Conhecimento.class.getName(), "c");
		hql.addCriteria("c.id", "=", idConhecimento);

		List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		if (!list.isEmpty()){
			return (DomainValue)list.get(0);
		}
		return null;
	}

	public List findIdConhecimentoByNrCodigoBarras(Long nrCodigoBarras){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("c.id");
		hql.addInnerJoin(Conhecimento.class.getName(), "c");
		hql.addCriteria("c.nrCodigoBarras", "=", nrCodigoBarras);

		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}

	/**
	 * Busca o conhecimento associado ao codigo de barras recebido.
	 * @param nrCodigoBarras numro de codigo de barras para buscar
	 * @return o codigo de barras encontrado
	 * @author Luis Carlos Poletto
	 */
	public List<Conhecimento> findByNrCodigoBarras(Long nrCodigoBarras) {
		if (nrCodigoBarras == null) {
			return null;
		}
		StringBuilder hql = new StringBuilder("select c from ");
		hql.append(Conhecimento.class.getName());
		hql.append(" as c join fetch c.filialByIdFilialOrigem as fo ");
		hql.append(" join fetch fo.pessoa as pfo ");
		hql.append(" join fetch c.clienteByIdClienteRemetente as cl ");
		hql.append(" where c.nrCodigoBarras = ?");

		List<Conhecimento> result = getAdsmHibernateTemplate().find(hql.toString(), new Object[] {nrCodigoBarras});
		return result;
	}

	public Conhecimento findByNrChaveId(Long idConhecimento) {
		if (idConhecimento == null) {
			return null;
		}
		StringBuilder hql = new StringBuilder("select c from ");
		hql.append(Conhecimento.class.getName());
		hql.append(" as c join fetch c.filialByIdFilialOrigem as fo ");
		hql.append(" join fetch fo.pessoa as pfo ");
		hql.append(" join fetch c.clienteByIdClienteRemetente as cl ");
		hql.append(" where c.idDoctoServico = ?");

		Conhecimento result = (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[] {idConhecimento});
		
		return result;
	}

	public Conhecimento findByNrChaveDocEletronico(String nrChave) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT c ");
		hql.append("FROM " + getPersistentClass().getSimpleName() + " as c ");
		hql.append(" left outer join fetch c.servico as s");
		hql.append(" left outer join fetch c.pedidoColeta as pc");
		hql.append(" left outer join fetch c.localizacaoMercadoria as lm, ");
		hql.append(MonitoramentoDocEletronico.class.getSimpleName() + " mde ");
		hql.append("WHERE ");
		hql.append(" c.idDoctoServico = mde.doctoServico.idDoctoServico AND ");
		hql.append(" mde.nrChave = ?");
		
		return (Conhecimento)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{nrChave});
	}
	
	public Conhecimento findByNrChaveRpsFedex(String chaveDocumento) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT c ");
		hql.append("FROM " + getPersistentClass().getSimpleName() + " as c ");
		hql.append(" left outer join fetch c.servico as s");
		hql.append(" left outer join fetch c.pedidoColeta as pc");
		hql.append(" left outer join fetch c.localizacaoMercadoria as lm, ");
		hql.append(MonitoramentoDocEletronico.class.getSimpleName() + " mde ");
		hql.append("WHERE ");
		hql.append(" c.idDoctoServico = mde.doctoServico.idDoctoServico AND ");
		hql.append(" mde.nrChaveEletrRpstFdx = ?");
		
		return (Conhecimento)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{chaveDocumento});

	}
	

	/**
	 * Busca o nrConhecimentoById pelo idConhecimento informado.<BR>
	 * @param idConhecimento
	 * @return nrConhecimento where idConhecimento = :idConhecimento; <code>null</code> quando não encontrado.
	 */
	public Long findNrConhecimentoById(Long idConhecimento) {
		if(idConhecimento == null)
			return null;

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(getPersistentClass().getName(), " c ");
		sql.addProjection("c.nrConhecimento");
		sql.addCriteria("c.id","=",idConhecimento);

		List<Long> result = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (result.size() == 1){
			return result.get(0);
		}
		return null;
	}

	/**
	 * Busca o número do conhecimento pelo Documento de Serviço.<BR>
	 * @param criteria {filial.idFilial, desconto.tpSituacaoAprovacao, nrConhecimento}
	 * @return número do conhecimento
	 */
	@SuppressWarnings({ "rawtypes" })
	public List findConhecimentoDevedorDocServFat(Map criteria){
		SqlTemplate sql = getSqlConhecimentoByDevedorDocServFat(criteria);
		sql.addProjection("new map(cto.id as idDocumento, cto.nrConhecimento as nrDocumento, " +
				"cto.filialByIdFilialOrigem.sgFilial as filial_sgFilial, cto.filialByIdFilialOrigem.idFilial as filial_idFilial)");
		List list = getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
		if (!list.isEmpty()){
			list = AliasToNestedMapResultTransformer.getInstance().transformListResult(list);
		}
		return list;
	}
//Pode ser esse
	@SuppressWarnings({ "rawtypes" })
	private SqlTemplate getSqlConhecimentoByDevedorDocServFat(Map criteria){
		TypedFlatMap map = (TypedFlatMap) criteria;

		SqlTemplate sql = new SqlTemplate();
		sql.addFrom( Conhecimento.class.getName() + " as cto join cto.devedorDocServFats as doc join doc.descontos as des ");
		String tpDocumentoServico = map.getString("tpDocumentoServico");
		if (StringUtils.isNotEmpty(tpDocumentoServico))
			sql.addCriteria("cto.tpDocumentoServico", "=", tpDocumentoServico);

		Long idCliente = map.getLong("cliente.idCliente");
		if (idCliente != null)
			sql.addCriteria("","=",idCliente);

		Long idFilial = map.getLong("filial.idFilial");
		if (idFilial != null)
			sql.addCriteria("cto.filialByIdFilialOrigem.id","=",idFilial);

		String tpSituacaoAprovacao = map.getString("desconto.tpSituacaoAprovacao");
		if (StringUtils.isNotEmpty(tpSituacaoAprovacao))
			sql.addCriteria("des.tpSituacaoAprovacao","=",tpSituacaoAprovacao);

		String nrConhecimento = map.getString("nrDocumento");
		if (StringUtils.isNotEmpty(nrConhecimento))
			sql.addCriteria("cto.nrConhecimento", "=", Integer.valueOf(Integer.parseInt(nrConhecimento)) );

		return sql;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindByIdLazyProperties(Map map) {
		map.put("ctoAwbs", FetchMode.SELECT);
		map.put("dadosComplementos", FetchMode.SELECT);
		map.put("doctoServicoSeguros", FetchMode.SELECT);
		map.put("notaFiscalConhecimentos", FetchMode.SELECT);
		map.put("devedorDocServs", FetchMode.SELECT);
		map.put("devedorDocServs.cliente", FetchMode.SELECT);
		map.put("devedorDocServFats", FetchMode.SELECT);
		map.put("valorCustos", FetchMode.SELECT);
		map.put("parcelaDoctoServicos", FetchMode.SELECT);
		map.put("observacaoDoctoServicos", FetchMode.SELECT);
		map.put("liberacaoDocServs", FetchMode.SELECT);
		map.put("ctoCtoCooperadas", FetchMode.SELECT);
		map.put("dimensoes", FetchMode.SELECT);
		map.put("servAdicionalDocServs", FetchMode.SELECT);
		map.put("impostoServicos", FetchMode.SELECT);
		map.put("ctoInternacional", FetchMode.JOIN);
		map.put("municipioByIdMunicipioEntrega", FetchMode.JOIN);
		map.put("municipioByIdMunicipioEntrega.unidadeFederativa", FetchMode.JOIN);
		map.put("servico", FetchMode.JOIN);
		map.put("mda", FetchMode.JOIN);
		map.put("moeda", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem", FetchMode.JOIN);
		map.put("filialByIdFilialOrigem.pessoa", FetchMode.JOIN);
		map.put("filialByIdFilialDestino", FetchMode.JOIN);
		map.put("filialByIdFilialDestino.empresa", FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente", FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente.pessoa", FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente.pessoa.enderecoPessoa", FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente.pessoa.enderecoPessoa.municipio", FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente.pessoa.enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);
		map.put("clienteByIdClienteDestinatario", FetchMode.JOIN);
		map.put("clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN);
		map.put("clienteByIdClienteDestinatario.pessoa.enderecoPessoa", FetchMode.JOIN);
		map.put("clienteByIdClienteDestinatario.pessoa.enderecoPessoa.municipio", FetchMode.JOIN);
		map.put("clienteByIdClienteDestinatario.pessoa.enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);
		map.put("servico",FetchMode.JOIN);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("doctoServico", FetchMode.JOIN);
		lazyFindPaginated.put("servico",FetchMode.JOIN);
		super.initFindPaginatedLazyProperties(lazyFindPaginated);
	}

	@SuppressWarnings({ "unchecked" })
	public List<Conhecimento> findByNrConhecimentoByFilial(Long nrConhecimento, Long idFilial, String tpDocumentoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"con");
		dc.createAlias("con.filialByIdFilialOrigem","filialByIdFilialOrigem");
		dc.createAlias("filialByIdFilialOrigem.pessoa","pessoa");
		dc.add(Restrictions.eq("nrConhecimento", nrConhecimento));
		dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial", idFilial));
		dc.add(Restrictions.eq("tpDocumentoServico", tpDocumentoServico));
		return findByDetachedCriteria(dc);
	}

	@SuppressWarnings({ "rawtypes" })
	public List findDocumentosServico(Long idConhecimento, String tpDocumentoServico){
		DetachedCriteria dc = createDetachedCriteria();

		dc.add(Restrictions.eq("tpDocumentoServico", tpDocumentoServico));
		dc.add(Restrictions.eq("idDoctoServico", idConhecimento));
		dc.setProjection(Projections.property("idDoctoServico"));

		return findByDetachedCriteria(dc);
	}

	@SuppressWarnings({ "rawtypes" })
	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		String sql = "select x.idConhecimento " +
		"from "+ Conhecimento.class.getName() + " as pojo " +
		"where pojo.idDoctoServico = :idDoctoServico ";
		return getAdsmHibernateTemplate().findByNamedParam(sql,"idDoctoServico", idDoctoServico);
	}

	/**
	 * Busca os documentos de servico (Conhecimentos - CTR)
	 * a partir do id do Manifesto de viagem nacional.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findConhecimentosByIdManifestoViagemNacional(Long idManifesto, Long idFilialOrigem){
		StringBuffer sql = new StringBuffer("");
		sql.append("select conhecimento from Conhecimento as conhecimento, ");
		sql.append("Manifesto as manifesto, ");
		sql.append("ManifestoViagemNacional as manifestoViagemNacional, ");
		sql.append("ManifestoNacionalCto as manifestoNacionalCto ");
		sql.append("where manifestoViagemNacional.id = manifesto.id ");
		sql.append("and manifestoViagemNacional.id = manifestoNacionalCto.manifestoViagemNacional.id ");
		sql.append("and manifestoNacionalCto.conhecimento.id = conhecimento.id ");
		sql.append("and conhecimento.tpDoctoServico = 'CTR' ");
		sql.append("and conhecimento.nrConhecimento >= 0 ");
		sql.append("and manifesto.id = :idManifesto");
		if(idFilialOrigem != null){
			sql.append("and conhecimento.filialOrigem.id = :idFilialOrigem" );
		}

		Map parameters = new HashMap();
		parameters.put("idManifesto", idManifesto);
		parameters.put("idFilialOrigem", idFilialOrigem);
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),"idManifesto", idManifesto);
	}
/**
 * Procurar conhecimentos pelo tp_situacao_Conhecimento que estejam carregados
 * @param sgFilial
 * @param idControleCarga
 * @param tpSituacaoConhecimento
 * @return
 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findConhecimentosByTpSituacao(String sgFilial,Long idControleCarga,String tpSituacaoConhecimento){
		StringBuffer sql = new StringBuffer("");
		sql.append(" select c");
		sql.append(" from ControleCarga as cc,");
		sql.append("      Manifesto as ma,");
		sql.append("      PreManifestoVolume pmv,");
		sql.append("      Conhecimento c,");
		sql.append("      Filial f");
		sql.append(" WHERE cc = ma.controleCarga");
		sql.append(" and ma = pmv.manifesto");
		sql.append(" and pmv.doctoServico.idDoctoServico = c.idDoctoServico");
		sql.append(" and cc.filialByIdFilialOrigem = f");
		sql.append(" and cc.idControleCarga = :idControleCarga");
		sql.append(" and f.sgFilial = :sgFilial");
		sql.append(" and c.tpSituacaoConhecimento = 'P' ");


		Map parameters = new HashMap();
		parameters.put("sgFilial",sgFilial);
		parameters.put("idControleCarga", idControleCarga);


		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parameters);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findConhecimentosViagemByTpSituacao(String sgFilial,Long idControleCarga,String tpSituacaoConhecimento){
		StringBuffer sql = new StringBuffer("");

		sql.append(" select c");
		sql.append(" from ControleCarga as cc,");
		sql.append("      Manifesto as ma,");
		sql.append("      PreManifestoVolume pmv,");
		sql.append("      Conhecimento c,");
		sql.append("      Filial f");
		sql.append(" WHERE cc = ma.idControleCarga");
		sql.append(" and ma = pmv.manifesto");
		sql.append(" and pmv.doctoServico.idDoctoServico = c.idDoctoServico");
		sql.append(" and cc.filialByIdFilialOrigem = f");
		sql.append(" and cc.idControleCarga = :idControleCarga");
		sql.append(" and f.sgFilial = :sgFilial");
		sql.append(" and c.tpSituacaoConhecimento = 'P' ");

		Map parameters = new HashMap();
		parameters.put("sgFilial",sgFilial);
		parameters.put("idControleCarga", idControleCarga);
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), parameters);
	}
	/**
	 * Busca os documentos de servico (Conhecimentos - CTR)
	 * a partir do id do Manifesto de entrega.
	 */
	public List findConhecimentosByIdManifestoEntrega(Long idManifesto){
		StringBuffer sql = new StringBuffer("");
		sql.append("select conhecimento from Conhecimento as conhecimento, ");
		sql.append("Manifesto as manifesto, ");
		sql.append("ManifestoEntrega as manifestoEntrega, ");
		sql.append("ManifestoEntregaDocumento as manifestoEntregaDocumento ");
		sql.append("where manifesto.id = manifestoEntrega.id ");
		sql.append("and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id ");
		sql.append("and manifestoEntregaDocumento.doctoServico.id = conhecimento.id ");
		sql.append("and conhecimento.tpDoctoServico = 'CTR' ");
		sql.append("and conhecimento.nrConhecimento >= 0 ");
		sql.append("and manifesto.id = :idManifesto");
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),"idManifesto", idManifesto);
	}

	/**
	 * Busca os documentos de servico (Notas Fiscais Transporte - NFT)
	 * a partir do id do Manifesto de entrega.
	 */
	public List findNotasFiscaisTransporteByIdManifestoEntrega(Long idManifesto) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select conhecimento from Conhecimento as conhecimento, ");
		sql.append("Manifesto as manifesto, ");
		sql.append("ManifestoEntrega as manifestoEntrega, ");
		sql.append("ManifestoEntregaDocumento as manifestoEntregaDocumento ");
		sql.append("where manifesto.id = manifestoEntrega.id ");
		sql.append("and manifestoEntrega.id = manifestoEntregaDocumento.manifestoEntrega.id ");
		sql.append("and manifestoEntregaDocumento.doctoServico.id = conhecimento.id ");
		sql.append("and conhecimento.tpDoctoServico = 'NFT' ");
		sql.append("and conhecimento.nrConhecimento >= 0 ");
		sql.append("and manifesto.id = :idManifesto");
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(),"idManifesto", idManifesto);
	}

	/**
	 * Busca Conhecimentos a partir do Meio de Transporte, Filial do Usuario Logado e pelo Tipo Situacao Conhecimento
	 *
	 * @param nrFrota
	 * @param idFilialUsuarioLogado
	 * @param tpSituacaoConhecimento
	 * @return
	 */
	public List findByNrFrotaByIdFilialUsuario(String nrFrota, Long idFilialUsuarioLogado, String tpSituacaoConhecimento) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select conh from Conhecimento as conh, ");
		sql.append(" Filial as fili, ");
		sql.append(" NotaFiscalConhecimento as nofc, ");
		sql.append(" VolumeNotaFiscal as vonf, ");
		sql.append(" MonitoramentoDescarga as mode ");
		sql.append("where fili.idFilial = :idFilialUsuarioLogado ");
		sql.append(" and conh.filialOrigem.idFilial = fili.idFilial ");
		sql.append(" and conh.tpSituacaoConhecimento = :tpSituacaoConhecimento ");
		sql.append(" and nofc.conhecimento.idConhecimento = conh.idConhecimento ");
		sql.append(" and vonf.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento ");
		sql.append(" and mode.idMonitoramentoDescarga = vonf.monitoramentoDescarga.idMonitoramentoDescarga ");
		sql.append(" and mode.nrFrota = :nrFrota ");
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), new String [] {"idFilialUsuarioLogado", "tpSituacaoConhecimento", "nrFrota"}, new Object [] {idFilialUsuarioLogado, tpSituacaoConhecimento, nrFrota});
	}

	/**
	 * Busca por documentos de serviço do tipo CTRC e cujo conhecimento já foi emitido.
	 *
	 * autor Julio Cesar Fernandes Corrêa
	 * 07/02/2006
	 * @param nrConhecimento
	 * @param idFilial
	 * @param dvConhecimento
	 * @return
	 */
	public List findLookupByNrConhecimentoByFilialOrigem(Long nrConhecimento, Long idFilial, Integer dvConhecimento, String tpDocumentoServico) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.dvConhecimento"), "dvConhecimento")
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico");
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.setProjection(pl)
			.add(Restrictions.eq("c.nrConhecimento", nrConhecimento))
			.add(Restrictions.eq("c.filialByIdFilialOrigem.id", idFilial))
			.add(Restrictions.eq("c.tpDocumentoServico", tpDocumentoServico))
			.add(Restrictions.eq("c.tpSituacaoConhecimento", "E"))
			.setResultTransformer(DetachedCriteria.ALIAS_TO_ENTITY_MAP);

		if(dvConhecimento != null){
			dc.add(Restrictions.eq("c.dvConhecimento", dvConhecimento));
		}

		return findByDetachedCriteria(dc);
	}

	public List findByNrConhecimentoIdFilialOrigem(Long nrConhecimento, Long idFilial, String tpDocumentoServico, String tpSituacaoConhecimento) {
		ProjectionList pl = getProjectionListConhecimento();
		DetachedCriteria dc = getDetachedCriteriaConhecimento(nrConhecimento, idFilial, tpDocumentoServico, tpSituacaoConhecimento);
		dc.setProjection(pl).setResultTransformer(AliasToNestedMapResultTransformer.getInstance());
		return findByDetachedCriteria(dc);
	}

	private ProjectionList getProjectionListConhecimento() {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico")
			.add(Projections.property("c.nrConhecimento"), "nrConhecimento")
			.add(Projections.property("c.nrDoctoServico"), "nrDoctoServico")
			.add(Projections.property("c.dvConhecimento"), "dvConhecimento")
			.add(Projections.property("c.dhEmissao.value"), "dhEmissao")
			.add(Projections.property("c.tpConhecimento"), "tpConhecimento")
			.add(Projections.property("c.blPaletizacao"), "blPaletizacao")
			.add(Projections.property("c.tpSituacaoConhecimento"), "tpSituacaoConhecimento")
			.add(Projections.property("c.tpDocumentoServico"), "tpDocumentoServico")
			.add(Projections.property("c.psAforado"), "psAforado")
			.add(Projections.property("c.vlMercadoria"), "vlMercadoria")
			.add(Projections.property("c.blBloqueado"), "blBloqueado")
			.add(Projections.property("d.idDensidade"), "densidade_idDensidade")
			.add(Projections.property("d.vlFator"), "densidade_vlFator")
			.add(Projections.property("lm.cdLocalizacaoMercadoria"), "cdLocalizacaoMercadoria")
			.add(Projections.property("c.psReal"), "psReal")
			.add(Projections.property("c.tpDevedorFrete"), "tpDevedorFrete")
			.add(Projections.property("np.idNaturezaProduto"), "naturezaProduto_idNaturezaProduto")
			.add(Projections.property("c.tpFrete"), "tpFrete")
			.add(Projections.property("fo.idFilial"), "idFilialOrigem")
			.add(Projections.property("fo.sgFilial"), "sgFilialOrigem")
			.add(Projections.property("pfo.nmFantasia"), "nmFilialOrigem")
			.add(Projections.property("fd.idFilial"), "idFilialDestino")
			.add(Projections.property("fd.sgFilial"), "sgFilialDestino")
			.add(Projections.property("pfd.nmFantasia"), "nmFilialDestino")
			.add(Projections.property("c.vlTotalDocServico"), "vlTotalDocServico")
			.add(Projections.property("cr.idCliente"), "remetente_idCliente")
			.add(Projections.property("cr.tpCliente"), "remetente_tpCliente")
			.add(Projections.property("cd.idCliente"), "destinatario_idCliente")
			.add(Projections.property("cd.tpCliente"), "destinatario_tpCliente")
			.add(Projections.property("pr.idPessoa"), "remetente_idPessoa")
			.add(Projections.property("pd.idPessoa"), "destinatario_idPessoa")
			.add(Projections.property("pr.tpPessoa"), "remetente_tpPessoa")
			.add(Projections.property("pd.tpPessoa"), "destinatario_tpPessoa")
			.add(Projections.property("pr.tpIdentificacao"), "remetente_tpIdentificacao")
			.add(Projections.property("pd.tpIdentificacao"), "destinatario_tpIdentificacao")
			.add(Projections.property("pr.nmPessoa"), "remetente_nmPessoa")
			.add(Projections.property("pd.nmPessoa"), "destinatario_nmPessoa")
			.add(Projections.property("pr.nrIdentificacao"), "remetente_nrIdentificacao")
			.add(Projections.property("cr.blPermiteCte"), "remetente_blPermiteCte")
			.add(Projections.property("pd.nrIdentificacao"), "destinatario_nrIdentificacao")
			.add(Projections.property("crd.idCliente"), "redespacho_idCliente"); 
		return pl;
	}

	private DetachedCriteria getDetachedCriteriaConhecimento(Long nrConhecimento, Long idFilial, String tpDocumentoServico, String tpSituacaoConhecimento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.createAlias("c.clienteByIdClienteRemetente", "cr")
			.createAlias("c.clienteByIdClienteDestinatario", "cd")
			.createAlias("c.clienteByIdClienteRedespacho","crd", Criteria.LEFT_JOIN)
			.createAlias("cr.pessoa", "pr")
			.createAlias("cd.pessoa", "pd")
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.createAlias("c.filialByIdFilialDestino", "fd")
			.createAlias("c.naturezaProduto", "np")
			.createAlias("c.densidade", "d")
			.createAlias("c.localizacaoMercadoria", "lm", Criteria.LEFT_JOIN)
			.createAlias("fo.pessoa", "pfo")
			.createAlias("fd.pessoa", "pfd")
			.add(Restrictions.eq("c.nrConhecimento", nrConhecimento))
			.add(Restrictions.eq("fo.id", idFilial))
			.add(Restrictions.eq("c.tpDocumentoServico", tpDocumentoServico));

		if(StringUtils.isNotBlank(tpSituacaoConhecimento)){
			dc = dc.add(Restrictions.eq("c.tpSituacaoConhecimento", tpSituacaoConhecimento));
		}
		return dc;
	}

	@Override
	public void flush() {
		getAdsmHibernateTemplate().flush();
	}

	public void evict(Object o){
		if (o!=null) {
			getAdsmHibernateTemplate().evict(o);
		}
	}

	public Integer getRowCountConhecimento(TypedFlatMap criteria) {
		SqlTemplate sql = getQuery(criteria);
		return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
	}

	@SuppressWarnings({ "rawtypes" })
	public ResultSetPage findConhecimentoPaginated(TypedFlatMap criteria) {
		SqlTemplate sql = getQuery(criteria);
		sql.addProjection(new StringBuilder()
			.append("new map(c.id as idDoctoServico,")
			.append("c.filialByIdFilialOrigem.sgFilial as sgFilialOrigem, ")
			.append("c.filialByIdFilialOrigem.sgFilial as filialByIdFilialOrigem_sgFilial, ")
			.append("c.filialByIdFilialOrigem.id as filialByIdFilialOrigem_idFilial, ")
			.append("c.filialByIdFilialOrigem.pessoa.nmFantasia as filialByIdFilialOrigem_pessoa_nmFantasia, ")
			.append("c.filialByIdFilialOrigem.id as idFilialOrigem, ")
			.append("c.filialByIdFilialDestino.sgFilial as sgFilialDestino, ")
			.append("c.filialByIdFilialDestino.pessoa.nmFantasia as filialByIdFilialDestino_pessoa_nmFantasia, ")
			.append("c.clienteByIdClienteRemetente.pessoa.idPessoa as clienteByIdClienteRemetente_pessoa_idPessoa, ")
			.append("c.clienteByIdClienteRemetente.pessoa.nmPessoa as clienteByIdClienteRemetente_pessoa_nmPessoa, ")
			.append("c.clienteByIdClienteRemetente.pessoa.nrIdentificacao as clienteByIdClienteRemetente_pessoa_nrIdentificacao, ")
			.append("c.clienteByIdClienteRemetente.pessoa.tpIdentificacao as clienteByIdClienteRemetente_pessoa_tpIdentificacao, ")
			.append("c.clienteByIdClienteDestinatario.pessoa.idPessoa as clienteByIdClienteDestinatario_pessoa_idPessoa, ")
			.append("c.clienteByIdClienteDestinatario.pessoa.nmPessoa as clienteByIdClienteDestinatario_pessoa_nmPessoa, ")
			.append("c.clienteByIdClienteDestinatario.pessoa.nrIdentificacao as clienteByIdClienteDestinatario_pessoa_nrIdentificacao, ")
			.append("c.clienteByIdClienteDestinatario.pessoa.tpIdentificacao as clienteByIdClienteDestinatario_pessoa_tpIdentificacao, ")
			.append("pc.nrIdentificacao as clienteByIdClienteConsignatario_pessoa_nrIdentificacao, ")
			.append("pc.nmPessoa as clienteByIdClienteConsignatario_pessoa_nmPessoa, ")
			.append("c.nrConhecimento as nrConhecimento, ")
			.append("c.dvConhecimento as dvConhecimento, ")
			.append("c.tpSituacaoConhecimento as tpSituacaoConhecimento, ")
			.append("c.tpDocumentoServico as tpDocumentoServico, ")
			.append("c.vlTotalDocServico as vlTotalDocServico, ")
			.append("c.dhEmissao as dhEmissao)").toString());

		sql.addOrderBy("c.filialByIdFilialOrigem.sgFilial");
		sql.addOrderBy("c.nrConhecimento");
		sql.addOrderBy("c.dvConhecimento");
		sql.addOrderBy("c.dhEmissao.value");
		sql.addOrderBy("c.vlTotalDocServico");
		sql.addOrderBy("c.filialByIdFilialDestino.sgFilial");

		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(), def.getCurrentPage(), def.getPageSize(), sql.getCriteria());
	}

	private SqlTemplate getQuery(TypedFlatMap criteria) {
		/** FROM */
		StringBuilder from = new StringBuilder()
			.append(Conhecimento.class.getName()).append(" as c ")
			.append(" left join c.clienteByIdClienteConsignatario as cc")
			.append(" left join cc.pessoa as pc");
		SqlTemplate sql = new SqlTemplate();
		sql.addFrom(from.toString());

		/** WHERE */
		YearMonthDay dtEmissaoInicial = criteria.getYearMonthDay("dtEmissaoInicial");
		if (dtEmissaoInicial != null) {
			sql.addCriteria("TRUNC(CAST(c.dhEmissao.value AS date))", ">=", dtEmissaoInicial);
		}
		YearMonthDay dtEmissaoFinal = criteria.getYearMonthDay("dtEmissaoFinal");
		if (dtEmissaoFinal != null) {
			sql.addCriteria("TRUNC(CAST(c.dhEmissao.value AS date))", "<", dtEmissaoFinal.plusDays(1));
		}
		sql.addCriteria("c.filialByIdFilialOrigem.id","=", criteria.getLong("filialByIdFilialOrigem.idFilial"));
		sql.addCriteria("c.filialByIdFilialDestino.id","=", criteria.getLong("filialByIdFilialDestino.idFilial"));
		sql.addCriteria("c.clienteByIdClienteRemetente.id", "=", criteria.getLong("clienteByIdClienteRemetente.idCliente"));
		sql.addCriteria("c.clienteByIdClienteDestinatario.id", "=", criteria.getLong("clienteByIdClienteDestinatario.idCliente"));
		sql.addCriteria("cc.id", "=", criteria.getLong("clienteByIdClienteConsignatario.idCliente"));
		sql.addCriteria("c.nrConhecimento", "=", criteria.getLong("nrConhecimento"));
		sql.addCriteria("c.tpSituacaoConhecimento", "=", criteria.getString("tpSituacaoConhecimento"));
		sql.addCriteria("c.tpConhecimento", "=", criteria.getString("tpConhecimento"));
		if(criteria.getString("tpConhecimentoIn") != null){
			sql.addCustomCriteria("c.tpConhecimento in (" + criteria.getString("tpConhecimentoIn") + ")");
		}
		sql.addCriteria("c.tpSituacaoConhecimento", "<>", "P");
		sql.addCriteria("c.tpDocumentoServico", "=", criteria.getString("tpDocumentoServico"));
		sql.addCriteria("c.vlTotalDocServico", "=", criteria.getBigDecimal("vlTotalDocServico"));

		Integer nrNotaFiscal = criteria.getInteger("nrNotaFiscal");
		if(nrNotaFiscal != null) {
			sql.addCustomCriteria("c.id in (select nfc.conhecimento.id from NotaFiscalConhecimento nfc where nfc.conhecimento.id = c.id and nfc.nrNotaFiscal = " + nrNotaFiscal + ")");
		}
		
		Long nrDocumentoEletronico = criteria.getLong("nrDocumentoEletronico");
		if(nrDocumentoEletronico != null) {
			sql.addCustomCriteria("c.id in (select mon.doctoServico.id from MonitoramentoDocEletronico mon where mon.doctoServico.id = c.id and mon.nrDocumentoEletronico = " + nrDocumentoEletronico + ")");
		}
		
		return sql;
	}

	@SuppressWarnings({ "rawtypes" })
	public ResultSetPage findPaginatedExcluirPreCtrc(TypedFlatMap criteria) {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Integer nrNotaFiscal = criteria.getInteger("nrNotaFiscal");
		Long idClienteRemetente = criteria.getLong("idCliente");
		String nrPlaca = (String) criteria.get("nrPlaca");
		String nrFrota = (String) criteria.get("nrFrota");

		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico")
			.add(Projections.property("nfc.nrNotaFiscal"), "nrNotaFiscal")
			.add(Projections.property("md.nrFrota"), "nrFrota")
			.add(Projections.property("md.nrPlaca"), "nrPlaca")
			.add(Projections.property("fo.sgFilial"), "sgFilialOrigem")
			.add(Projections.property("c.dhInclusao.value"), "dhInclusao")
			.add(Projections.property("pr.nmPessoa"), "nmPessoaRemetente")
			.add(Projections.property("pd.nmPessoa"), "nmPessoaDestinatario")
			.add(Projections.property("c.vlMercadoria"), "vlMercadoria")
			.add(Projections.property("c.vlTotalDocServico"), "vlTotalDocServico");

		DetachedCriteria dc = DetachedCriteria.forClass(NotaFiscalConhecimento.class, "nfc")
			.createAlias("nfc.conhecimento", "c")
			.createAlias("nfc.volumeNotaFiscais", "vnf")
			.createAlias("vnf.monitoramentoDescarga", "md")
			.createAlias("c.clienteByIdClienteRemetente", "r")
			.createAlias("c.clienteByIdClienteDestinatario", "d")
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.createAlias("r.pessoa", "pr")
			.createAlias("d.pessoa", "pd")
			.setProjection(pl)
			.add(Restrictions.eq("c.tpSituacaoConhecimento", "P"))
			.add(Restrictions.eq("vnf.nrSequencia", Integer.valueOf(1)))
			.add(Restrictions.in("vnf.tpVolume", new Object[]{"M", "U"}))
			.add(Restrictions.isNull("vnf.nrConhecimento"))
			.add(Restrictions.or(
				Restrictions.eq("c.blPesoAferido", Boolean.FALSE),
				Restrictions.isNull("c.blPesoAferido")))
			.add(Restrictions.in("c.tpDoctoServico", new Object[]{"CTR","CTE","NFT","NTE"}))
			.add(Restrictions.eq("fo.id", idFilial))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		if (nrNotaFiscal != null)
			dc.add(Restrictions.eq("nfc.nrNotaFiscal", nrNotaFiscal));
		if (idClienteRemetente != null)
			dc.add(Restrictions.eq("c.clienteByIdClienteRemetente.id", idClienteRemetente));
		if (nrFrota != null && !"".equals(nrFrota))
			dc.add(Restrictions.eq("md.nrFrota", nrFrota));
		if(nrPlaca != null && !"".equals(nrPlaca))
			dc.add(Restrictions.eq("md.nrPlaca", nrPlaca));

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	@SuppressWarnings({ "rawtypes" })
	public ResultSetPage findPaginatedExcluirPreCtrcOrfao(TypedFlatMap criteria) {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Integer nrNotaFiscal = criteria.getInteger("nrNotaFiscal");
		Long idClienteRemetente = criteria.getLong("idCliente");

		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico")
			.add(Projections.property("nfc.nrNotaFiscal"), "nrNotaFiscal")
			.add(Projections.property("fo.sgFilial"), "sgFilialOrigem")
			.add(Projections.property("c.dhInclusao.value"), "dhInclusao")
			.add(Projections.property("pr.nmPessoa"), "nmPessoaRemetente")
			.add(Projections.property("pd.nmPessoa"), "nmPessoaDestinatario")
			.add(Projections.property("c.vlMercadoria"), "vlMercadoria")
			.add(Projections.property("c.vlTotalDocServico"), "vlTotalDocServico");

		DetachedCriteria dc = DetachedCriteria.forClass(NotaFiscalConhecimento.class, "nfc")
			.createAlias("nfc.conhecimento", "c")
			.createAlias("c.clienteByIdClienteRemetente", "r")
			.createAlias("c.clienteByIdClienteDestinatario", "d")
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.createAlias("r.pessoa", "pr")
			.createAlias("d.pessoa", "pd")
			.setProjection(pl)
			.add(Restrictions.eq("c.tpSituacaoConhecimento", "P"))
			.add(Restrictions.or(
				Restrictions.eq("c.blPesoAferido", Boolean.FALSE),
				Restrictions.isNull("c.blPesoAferido")))
			.add(Restrictions.in("c.tpDoctoServico", new Object[]{"CTR","CTE","NFT","NTE"}))
			.add(Restrictions.eq("fo.id", idFilial))
			.add(Restrictions.isEmpty("nfc.volumeNotaFiscais"))
			.setResultTransformer(AliasToTypedFlatMapResultTransformer.getInstance());

		if (nrNotaFiscal != null)
			dc.add(Restrictions.eq("nfc.nrNotaFiscal", nrNotaFiscal));
		if (idClienteRemetente != null)
			dc.add(Restrictions.eq("c.clienteByIdClienteRemetente.id", idClienteRemetente));

		FindDefinition findDef = FindDefinition.createFindDefinition(criteria);
		return findPaginatedByDetachedCriteria(dc, findDef.getCurrentPage(), findDef.getPageSize());
	}

	@SuppressWarnings({ "rawtypes" })
	public List findByIdExcluirPreCtrc(Long id) {
		ProjectionList pl = Projections.projectionList()
			.add(Projections.property("c.dvConhecimento"), "dvConhecimento")
			.add(Projections.property("c.idDoctoServico"), "idDoctoServico")
			.add(Projections.property("c.idDoctoServico"), "idConhecimento")
			.add(Projections.property("c.dhEmissao.value"), "dhEmissao")
			.add(Projections.property("fo.id"), "idFilialOrigem")
			.add(Projections.property("c.filialByIdFilialDestino.idFilial"), "idFilialDestino")
			.add(Projections.property("fo.sgFilial"), "sgFilialOrigem")
			.add(Projections.property("c.vlTotalDocServico"), "vlTotalDocServico")

			.add(Projections.property("cr.idCliente"), "idClienteRemetente")
			.add(Projections.property("pr.idPessoa"), "idPessoaRemetente")
			.add(Projections.property("pr.nrIdentificacao"), "nrIdentificacaoRemetente")
			.add(Projections.property("pr.tpIdentificacao"), "tpIdentificacaoRemetente")
			.add(Projections.property("pr.nmPessoa"), "nmPessoaRemetente")
			.add(Projections.property("prep.dsEndereco"), "dsEnderecoRemetente")
			.add(Projections.property("prep.nrEndereco"), "nrEnderecoRemetente")
			.add(Projections.property("prep.dsComplemento"), "dsComplementoRemetente")
			.add(Projections.property("prep.dsBairro"), "dsBairroRemetente")
			.add(Projections.property("prep.nrCep"), "nrCepRemetente")
			.add(Projections.property("prepm.idMunicipio"), "idMunicipioRemetente")
			.add(Projections.property("prepm.nmMunicipio"), "nmMunicipioRemetente")
			.add(Projections.property("prepmuf.idUnidadeFederativa"), "idUnidadeFederativaRemetente")
			.add(Projections.property("prepmuf.sgUnidadeFederativa"), "sgUnidadeFederativaRemetente")
			.add(Projections.property("prepmp.nmPais"), "nmPaisRemetente")
			.add(Projections.property("preptl.dsTipoLogradouro"), "dsTipoLogradouroRemetente")

			.add(Projections.property("cd.idCliente"), "idClienteDestinatario")
			.add(Projections.property("pd.idPessoa"), "idPessoaDestinatario")
			.add(Projections.property("pd.nrIdentificacao"), "nrIdentificacaoDestinatario")
			.add(Projections.property("pd.tpIdentificacao"), "tpIdentificacaoDestinatario")
			.add(Projections.property("pd.nmPessoa"), "nmPessoaDestinatario")
			.add(Projections.property("pdep.dsEndereco"), "dsEnderecoDestinatario")
			.add(Projections.property("pdep.nrEndereco"), "nrEnderecoDestinatario")
			.add(Projections.property("pdep.dsComplemento"), "dsComplementoDestinatario")
			.add(Projections.property("pdep.dsBairro"), "dsBairroDestinatario")
			.add(Projections.property("pdep.nrCep"), "nrCepDestinatario")
			.add(Projections.property("pdepm.idMunicipio"), "idMunicipioDestinatario")
			.add(Projections.property("pdepm.nmMunicipio"), "nmMunicipioDestinatario")
			.add(Projections.property("pdepmuf.idUnidadeFederativa"), "idUnidadeFederativaDestinatario")
			.add(Projections.property("pdepmuf.sgUnidadeFederativa"), "sgUnidadeFederativaDestinatario")
			.add(Projections.property("pdepmp.nmPais"), "nmPaisDestinatario")
			.add(Projections.property("pdeptl.dsTipoLogradouro"), "dsTipoLogradouroDestinatario");

		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
			.createAlias("c.clienteByIdClienteRemetente", "cr")
			.createAlias("c.clienteByIdClienteDestinatario", "cd")
			.createAlias("c.filialByIdFilialOrigem", "fo")
			.createAlias("cr.pessoa", "pr")
			.createAlias("cd.pessoa", "pd")
			.createAlias("pr.enderecoPessoa", "prep")
			.createAlias("prep.tipoLogradouro", "preptl")
			.createAlias("prep.municipio", "prepm")
			.createAlias("prepm.unidadeFederativa", "prepmuf")
			.createAlias("prepmuf.pais", "prepmp")
			.createAlias("pd.enderecoPessoa", "pdep")
			.createAlias("pdep.tipoLogradouro", "pdeptl")
			.createAlias("pdep.municipio", "pdepm")
			.createAlias("pdepm.unidadeFederativa", "pdepmuf")
			.createAlias("pdepmuf.pais", "pdepmp")

			.add(Restrictions.eq("c.idDoctoServico", id))

			.setProjection(pl)
			.setResultTransformer(AliasToNestedMapResultTransformer.getInstance());

		return findByDetachedCriteria(dc);
	}

	public Integer getRowCountExcluirPreCtrc(TypedFlatMap criteria) {
		Long idFilial = SessionUtils.getFilialSessao().getIdFilial();
		Integer nrNotaFiscal = criteria.getInteger("nrNotaFiscal");
		Long idClienteRemetente = criteria.getLong("idCliente");
		String nrPlaca = (String) criteria.get("nrPlaca");
		String nrFrota = (String) criteria.get("nrFrota");

		DetachedCriteria dc = DetachedCriteria.forClass(NotaFiscalConhecimento.class, "nfc")
			.createAlias("nfc.conhecimento", "c")
			.createAlias("nfc.volumeNotaFiscais", "vnf")
			.createAlias("vnf.monitoramentoDescarga", "md")
			.setProjection(Projections.rowCount())
			.add(Restrictions.eq("c.tpSituacaoConhecimento", "P"))
			.add(Restrictions.or(
				Restrictions.eq("c.blPesoAferido", Boolean.FALSE),
				Restrictions.isNull("c.blPesoAferido")))
			.add(Restrictions.in("c.tpDoctoServico", new Object[]{"CTR","CTE","NFT","NTE"}))
			.add(Restrictions.eq("vnf.nrSequencia", Integer.valueOf(1)))
			.add(Restrictions.in("vnf.tpVolume", new Object[]{"M", "U"}))
			.add(Restrictions.isNull("vnf.nrConhecimento"))
			.add(Restrictions.eq("c.filialByIdFilialOrigem.id", idFilial));
		if (nrNotaFiscal != null)
			dc = dc.add(Restrictions.eq("nfc.nrNotaFiscal", nrNotaFiscal));
		if (idClienteRemetente != null)
			dc = dc.add(Restrictions.eq("c.clienteByIdClienteRemetente.id", idClienteRemetente));
		if (nrFrota != null && !"".equals(nrFrota))
			dc.add(Restrictions.eq("md.nrFrota", nrFrota));
		if(nrPlaca != null && !"".equals(nrPlaca))
			dc.add(Restrictions.eq("md.nrPlaca", nrPlaca));
		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}
//Mais provavel
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Conhecimento findConhecimentoByNrConhecimentoIdFilial(Long nrConhecimento, Long idFilial, String tpDocumentoServico) {
		
		boolean isTpDocumentoServico = StringUtils.isNotBlank(tpDocumentoServico);
		Map m = new HashMap();
		
		StringBuilder sb = new StringBuilder()
		.append("  from Conhecimento as c join fetch c.filialByIdFilialOrigem as fo")
		.append("  left outer join fetch c.aeroportoByIdAeroportoDestino as a")
		.append("  left outer join fetch a.pessoa as p")
		.append("  left outer join fetch c.localizacaoMercadoria as lm")
		.append("  left outer join fetch c.filialLocalizacao as fl")
		.append("  left outer join fetch c.servico as s")
		.append("  left outer join fetch c.filialByIdFilialDestino as fd")
		.append("  left outer join fetch fd.aeroporto as fda")
		.append("  left outer join fetch fda.pessoa as fdap")
		.append("  left outer join fetch c.pedidoColeta as pc")
		.append(" where c.nrConhecimento = :nrConhecimento")
		.append("   and fo.id = :idFilial");
		
		m.put("nrConhecimento", nrConhecimento);
		m.put("idFilial", idFilial);
		
		if(isTpDocumentoServico) {
			sb.append("   and c.tpDocumentoServico = :tpDocumentoServico");
			m.put("tpDocumentoServico", tpDocumentoServico);
		}

		return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), m);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> findConhecimentoByNrConhecimentoIdFilial(Map<String, List<Long>> params) {
		
		Map<String, Object> parametersValues = new HashMap<>();
		parametersValues.put("nrConhecimento", params.get("nrConhecimento"));
		parametersValues.put("sgFilial", params.get("sgFilial"));
		parametersValues.put("tpDocumentoServico", params.get("tpDocumentoServico"));
		
		StringBuilder sql = new StringBuilder()
				.append("SELECT ")
				.append("  ds.ID_DOCTO_SERVICO as idDoctoServico,")
				.append("  c.NR_CONHECIMENTO as nrConhecimento,")
				.append("  f.SG_FILIAL              AS sgFilial,")
				.append("  ds.TP_DOCUMENTO_SERVICO       AS tpDocumentoServico")
				.append(" FROM ")
				.append("   CONHECIMENTO c")  
				.append("    INNER JOIN DOCTO_SERVICO ds")
				.append("      ON c.ID_CONHECIMENTO = ds.ID_DOCTO_SERVICO")
				.append("    INNER JOIN FILIAL f") 
				.append("      ON ds.ID_FILIAL_ORIGEM = f.ID_FILIAL")
				.append(" WHERE ")
				.append("  c.NR_CONHECIMENTO IN  (:nrConhecimento)")
				.append("  AND f.SG_FILIAL   IN (:sgFilial)")
				.append("  AND ds.TP_DOCUMENTO_SERVICO IN (:tpDocumentoServico)");
				
		return getAdsmHibernateTemplate()
					.findBySqlToMappedResult
						(
							sql.toString(), 
							parametersValues, 
							configureSqlQueryConhecimentoByNrConhecimentoIdFilial()
						);
	}
	
	/*
	 * Verifica se já existe um Conhecimento para o Documento de Servico Original informado.
	 */
	public Integer getRowCountByDoctoServicoOriginal(Long idDoctoServicoOriginal, String tpDocumentoServico, String tpConhecimento) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c");
		dc.setProjection(Projections.count("c.id"));

		dc.add(Restrictions.eq("c.tpDocumentoServico", tpDocumentoServico));
		dc.add(Restrictions.eq("c.tpConhecimento", tpConhecimento));
		dc.add(Restrictions.eq("c.doctoServicoOriginal.id", idDoctoServicoOriginal));
		dc.add(Restrictions.ne("c.tpSituacaoConhecimento", "C"));

		return getAdsmHibernateTemplate().getRowCountByDetachedCriteria(dc);
	}

	public Conhecimento findByIdPreAwb(Long idDoctoServico) {
		StringBuilder hql = new StringBuilder()
		.append("select new map(")
		.append("       c.id as idDoctoServico,")
		.append("       c.psReal as psReal,")
		.append("       c.psAforado as psAforado,")
		.append("       c.qtVolumes as qtVolumes,")
		.append("       c.nrConhecimento as nrConhecimento,")
		.append("       c.dvConhecimento as dvConhecimento,")
		.append("       c.vlMercadoria as vlMercadoria,")
		.append("       c.tpDoctoServico as tpDoctoServico,")
		.append("       fo.sgFilial as filialByIdFilialOrigem_sgFilial,")
		.append("       ad.sgAeroporto as aeroportoByIdAeroportoDestino_sgAeroporto,")
		.append("       fda.sgAeroporto as filialByIdFilialDestino_aeroporto_sgAeroporto,")
		.append("       s.tpModal as servico_tpModal)")
		.append("  from ").append(getPersistentClass().getName()).append(" c ")
		.append("       join c.filialByIdFilialOrigem fo")
		.append("       join c.servico s")
		.append("       left outer join c.aeroportoByIdAeroportoDestino ad")
		.append("       join c.municipioByIdMunicipioEntrega me")
		.append("       join c.filialByIdFilialDestino fd")
		.append("       left outer join fd.aeroporto fda")
		.append(" where c.id = ? ");

		List result = getAdsmHibernateTemplate().find(hql.toString(), idDoctoServico);
		if (result != null && !result.isEmpty()) {
			AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(getPersistentClass());
			List transformed = transformer.transformListResult(result);
			return (Conhecimento) transformed.get(0);
		}
		return null;
	}

	public List findBlPesoAferidoByMeioTransporteColeta(String nrFrota, Long idFilialOrigem, Boolean blPesoAferido) {
		StringBuffer sql = new StringBuffer("");
		sql.append("select conh from Conhecimento as conh, ");
		sql.append(" Filial as fili, ");
		sql.append(" NotaFiscalConhecimento as nofc, ");
		sql.append(" VolumeNotaFiscal as vonf, ");
		sql.append(" MonitoramentoDescarga as mode ");
		sql.append("where fili.idFilial = ? ");
		sql.append(" and conh.filialOrigem.idFilial = fili.idFilial ");
		sql.append(" and (conh.blPesoAferido = ? ");
		sql.append(" and nofc.conhecimento.idConhecimento = conh.idConhecimento ");
		sql.append(" and vonf.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento ");
		sql.append(" and mode.idMonitoramentoDescarga = vonf.monitoramentoDescarga.idMonitoramentoDescarga ");
		sql.append(" and mode.nrFrota = ? ");
		return getAdsmHibernateTemplate().find(sql.toString(), new Object [] {idFilialOrigem, (blPesoAferido.booleanValue() ? ("'" + 'S' + "')") : ("'" + 'N' + "'" + " or conh.blPesoAferido is null ") + ")"), nrFrota});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List findByTerminalNotInAwb(TypedFlatMap criteria) {
		
		StringBuilder hql = new StringBuilder()
		.append("select new map(")
		.append("       c.id as idConhecimento,")
		.append("       fo.sgFilial as filialOrigem_sgFilial,")
		.append("       c.nrConhecimento as nrConhecimento,")
		.append("       c.dvConhecimento as dvConhecimento,")
		.append("       ad.sgAeroporto as aeroportoDestino_sgAeroporto,")
		.append("       c.tpDocumentoServico as tpDocumentoServico,")
		.append("       c.psReal as doctoServico_psReal,")
		.append("       c.psAforado as doctoServico_psAforado,")
		.append("       c.qtVolumes as doctoServico_qtVolumes,")
		.append("       c.vlMercadoria as doctoServico_vlMercadoria,")
		.append("       d.nrAltura as dimensao_nrAltura,")
		.append("       d.nrLargura as dimensao_nrLargura,")
		.append("       d.nrComprimento as dimensao_nrComprimento)")
		.append("  from Conhecimento c")
		.append("       left outer join c.dimensoes d")
		.append("       join c.localizacaoMercadoria lm")
		.append("       join c.filialLocalizacao fl")
		.append("       join c.filialOrigem fo")
		.append("       join c.servico s")
		.append("       left outer join c.aeroportoByIdAeroportoDestino ad")
		.append("       join c.municipioByIdMunicipioEntrega me")
		.append("       join c.filialByIdFilialDestino fd")
		.append(" where ")
		.append("   fl.idFilial = :filialLocalizacao")
		.append("   and c.id not in (select ca.conhecimento.id")
		.append("                      from CtoAwb ca join ca.awb a where a.tpStatusAwb != 'C') ");
		
		String conhecimentos = criteria.getString("conhecimentos");
		if(conhecimentos != null && !"".equals(conhecimentos)){
			hql.append(" and c.id in (" + conhecimentos + ")");
		}else{
			hql.append(" and c.tpSituacaoConhecimento = '" + ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO + "' ");
		}
		
		hql.append("  and c.tpDocumentoServico in (:tpsDocumentosServicos)")
		.append("     and s.tpModal = :tpModal")
		.append("     and (d.id = (select min(dm.id) ")
		.append("                 from Dimensao dm")
		.append("                where dm.conhecimento.id = c.id) or")
		.append("       d is null)");

		Long idFilial = criteria.getLong("filial.idFilial");
		if(idFilial != null) {
			hql.append(" and fd.idFilial = " + idFilial);
		}

		Long idAeroporto = criteria.getLong("aeroporto.idAeroporto");
		if(idAeroporto != null) {
			hql.append(" and ad.id = " + idAeroporto);
		}

		Long idMunicipio = criteria.getLong("municipio.idMunicipio");
		if(idMunicipio != null) {
			hql.append(" and me.id = " + idMunicipio);
		}

		Long idManifestoViagem = criteria.getLong("manifestoViagem.idManifestoViagemNacional");
		if(idManifestoViagem != null) {
			hql.append(" and c.id in ")
			.append("		(select 	mnc.conhecimento.id " +
					"		 from 		ManifestoNacionalCto mnc" +
					"		 			join mnc.manifestoViagemNacional mvn" +
					"					where mvn.id = " + idManifestoViagem + ")");
		}
		criteria.getLong("manifestoViagem.idManifestoViagemNacional");
		List localizacoes = new ArrayList();
		
		Map<String, Object> paramValues = new HashMap<String, Object>();
		
		if(idManifestoViagem != null) {
			hql.append(" and lm.cdLocalizacaoMercadoria in (:localizacoes)");
			localizacoes.add(ConstantesSim.CD_MERCADORIA_AGUARDANDO_SAIDA_PORTARIA.shortValue());
			paramValues.put("localizacoes", localizacoes);
		}
		
		List lstTpsDocumentosServicos = Arrays.asList(ConstantesExpedicao.CONHECIMENTO_NACIONAL
				, ConstantesExpedicao.CONHECIMENTO_ELETRONICO);
		paramValues.put("tpsDocumentosServicos", lstTpsDocumentosServicos);

		Long idFilialLocalizacao = criteria.getLong("filialLocalizacao.idFilial");
		paramValues.put("filialLocalizacao", idFilialLocalizacao);

		paramValues.put("tpModal", ConstantesExpedicao.MODAL_AEREO);
		
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), paramValues);

		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}
	
	
	public List findByCriteriaNotInAwb(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder()
		.append("select new map(")
		.append("       c.id as idConhecimento,")
		.append("       fo.sgFilial as filialOrigem_sgFilial,")
		.append("       c.nrConhecimento as nrConhecimento,")
		.append("       c.dvConhecimento as dvConhecimento,")
		.append("       ad.sgAeroporto as aeroportoDestino_sgAeroporto,")
		.append("       c.tpDocumentoServico as tpDocumentoServico,")
		.append("       c.psReal as doctoServico_psReal,")
		.append("       c.psAforado as doctoServico_psAforado,")
		.append("       c.qtVolumes as doctoServico_qtVolumes,")
		.append("       c.vlMercadoria as doctoServico_vlMercadoria,")
		.append("       d.nrAltura as dimensao_nrAltura,")
		.append("       d.nrLargura as dimensao_nrLargura,")
		.append("       d.nrComprimento as dimensao_nrComprimento)")
		.append("  from Conhecimento c")
		.append("       left outer join c.dimensoes d")
		.append("       left outer join c.ctoAwbs cto")
		.append("       left outer join cto.awb awb")
		.append("       left outer join awb.ciaFilialMercurio cfm")
		.append("       join c.filialLocalizacao fl")
		.append("       join c.filialOrigem fo")
		.append("       left join c.servico s")
		.append("       left outer join c.aeroportoByIdAeroportoDestino ad")
		.append("       join c.municipioByIdMunicipioEntrega me")
		.append("       join c.filialByIdFilialDestino fd")
		.append(" where c.id not in ("+ getQueryCtoAwb()+") and")
		.append("       c.tpSituacaoConhecimento = :tpSituacaoConhecimento and")
		.append("       c.tpDocumentoServico in (:tpsDocumentosServicos) and")
		.append("       (d.id = (select min(dm.id) ")
		.append("                 from Dimensao dm")
		.append("                where dm.conhecimento.id = c.id) or")
		.append("       d is null)");

		Long idFilial = criteria.getLong("filial.idFilial");
		if(idFilial != null) {
			hql.append(" and fd.idFilial = " + idFilial);
		}

		Long idAeroporto = criteria.getLong("aeroporto.idAeroporto");
		if(idAeroporto != null) {
			hql.append(" and ad.id = " + idAeroporto);
		}

		Long idMunicipio = criteria.getLong("municipio.idMunicipio");
		if(idMunicipio != null) {
			hql.append(" and me.id = " + idMunicipio);
		}

		Long idManifestoViagem = criteria.getLong("manifestoViagem.idManifestoViagemNacional");
		if(idManifestoViagem != null) {
			hql.append(" and c.id in ")
			.append("		(select 	mnc.conhecimento.id " +
					"		 from 		ManifestoNacionalCto mnc" +
					"		 			join mnc.manifestoViagemNacional mvn" +
					"					where mvn.id = " + idManifestoViagem + ")");
		}
		
		List lstTpsDocumentosServicos = Arrays.asList(ConstantesExpedicao.CONHECIMENTO_NACIONAL
				, ConstantesExpedicao.CONHECIMENTO_ELETRONICO);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("tpSituacaoConhecimento", ConstantesExpedicao.DOCUMENTO_SERVICO_EMITIDO);
		params.put("tpsDocumentosServicos", lstTpsDocumentosServicos);
				
		Long idPreAwb = criteria.getLong("awb.idAwb");
		if(idPreAwb != null) {
			Long idFilialLogada = criteria.getLong("filialLocalizacao.idFilial");
			hql.append(" and cto.awb.id = :idPreAwb");
			hql.append(" and cfm.filial.id = :idFilialLogada");
			params.put("idPreAwb", idPreAwb);
			params.put("idFilialLogada", idFilialLogada);
		} else {
			hql.append(" and s.tpModal = :tpModal");
			params.put("tpModal", ConstantesExpedicao.MODAL_AEREO);
		}

		
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),params);

		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}

	private String getQueryCtoAwb() {
		StringBuilder subQuery= new StringBuilder()
		.append("select ca.conhecimento.id")
		.append(" from CtoAwb ca")
		.append(" join ca.awb awb ")
		.append("where awb.tpStatusAwb = '" + ConstantesExpedicao.TP_STATUS_AWB_EMITIDO + "' ");
		
		return subQuery.toString();
	}

	@SuppressWarnings({ "rawtypes" })
	public Map findDataToSOM(Long idConhecimento) {
		StringBuilder sql = new StringBuilder()
		.append("select new Map( conh.id as idDoctoServico")
		.append("	,conh.vlDesconto as vlDesconto")
		.append("	,fOrig.id as idFilialOrigem")
		.append("	,dds.filial.id as idFilialDevedor")
		.append("	,fOrig.sgFilial as sgFilialOrigem")
		.append("	,tti.id as idTipoTributacaoIcms")
		.append("	,tti.dsTipoTributacaoIcms as dsTipoTributacaoIcms")
		.append("	,(select uf.sgUnidadeFederativa from fOrig.pessoa p join p.enderecoPessoa ep join ep.municipio m join m.unidadeFederativa uf) as sgUfFilialOrigem")
		.append("	,conh.nrDoctoServico as nrDoctoServico,")
		.append("	conh.nrConhecimento as nrConhecimento,")
		.append("	conh.nrDiasPrevEntrega as nrDiasPrevEntrega,")
		.append("	conh.dhEmissao as dtEmissaoDocServ,")
		.append("	fDest.id as idFilialDestino,")
		.append("	fDestinoResp.id as idFilialDestinoResp,")
        .append("	conh.tpFrete as tpFreteCtrc,")
        .append("	conh.nrCepEntrega as nrCepEntrega,")
        .append("	conh.blSpecialService as blSpecialService,")
        .append("	conh.dsBairroEntrega as dsBairroEntrega,")
        .append("	conh.tpCalculoPreco as tpCalculoPreco,")
        .append("	conh.blSpecialService as specialService,")
        .append("	conh.dsEnderecoEntrega as dsEnderecoEntrega,")
        .append("	tf.cdTarifaPreco as cdTarifaPreco,")
        .append("	conh.vlLiquido as vlFreteLiquido,")
        .append("	stp.tpSubtipoTabelaPreco as tpSubtipoTabelaPreco,")
        .append("	pesDev.id as idPessoaDevedor,")
        .append("	pesRem.id as idPessoaRemetente,")
        .append("	pesDest.id as idPessoaDestinatario,")
        .append("	pesRedesp.id as idPessoaRedespacho,")
        .append("	pesConsig.id as idPessoaConsignatario,")
        .append("	conh.tpSituacaoConhecimento as tpSituacaoConhecimento,")
        .append("	conh.clienteByIdClienteConsignatario.id as idClienteConsignatario,")
        .append("	conh.clienteByIdClienteRedespacho.id as idClienteRedespacho,")
        .append("	"+PropertyVarcharI18nProjection.createProjection("np.dsNaturezaProduto")+" as dsNaturezaProduto,")
        .append("	conh.tpDevedorFrete as tpDevedorFrete,")
        .append("	conh.qtVolumes as qtVolumes,")
        .append("	conh.psReal as psReal,")
        .append("	conh.nrFormulario as nrFormulario,")
        .append("	conh.vlMercadoria as vlMercadoria,")
        .append("	conh.psAforado as psAforado,")
        .append("	conh.vlBaseCalcImposto as vlBaseCalcImposto,")
        .append("	conh.vlIcmsSubstituicaoTributaria as vlIcmsSt,")
        .append("	conh.psAferido as psAferido,")
        .append("	conh.tpConhecimento as tpConhecimento,")
        .append("	u.nrMatricula as nrMatriculaUsuarioIncl,")
        .append("	u.login as loginUsuarioInclusao,")
        .append("	conh.nrCfop as nrCfop,")
        .append("	conh.pcAliquotaIcms as pcAliquotaIcms,")
        .append("	conh.municipioByIdMunicipioEntrega.id as idMunicipioEntrega,")
        .append("	conh.psReferenciaCalculo as psReferenciaCalculo,")
        .append("	munCol.nmMunicipio as nmMunicipioCol,")
        .append("	munEnt.nmMunicipio as nmMunicipioEnt,")
        .append("	ttp.tpTipoTabelaPreco as tpTipoTabelaPreco,")
        .append("	ttp.nrVersao as nrVersao,")
    	.append("	conh.tpConhecimento as tpConhecimento,")
        .append("	sv.tpModal as tpModal,")
        .append("	dc.cdDivisaoCliente as cdDivisaoCliente,")
        .append("	(case")
        .append("	  when exists (select 1 from DoctoServicoSeguros dss where dss.conhecimento.id = conh.id)  then '1'")
        .append("	  else '0'")
        .append("	end) as indicadorSeguro,")
        .append("	conh.blIndicadorFretePercentual as blIndicadorFretePercentual,")
        .append("	conh.blIncideIcmsPedagio as blIncidenciaIcmsPedagio,")
        .append("	conh.vlImposto as vlImposto,")
        .append("	conh.vlTotalDocServico as vlTotalDocServico,")
        .append("	conh.dtPrevEntrega as dtPrevEntrega,")
        .append("	conh.blIndicadorEdi as blIndicadorEdi,")
        .append("	sads.vlMercadoria as vlMercadoriaServAdic,")
        .append("	(select sum(nvl(pds.vlParcela, 0)) from conh.parcelaDoctoServicos pds where pds.parcelaPreco.id not in (36,38,16,24,8,23,51,12,21,22,25,29,33,56,57,58,59)) as vlParcelaPrecoOutr,")
        .append("	obsConhDev.dsObservacaoConhecimento as dsObservacaoConhecimento,")
        .append("	ddsie.id as inscricaoEstadualDevDocServ,")
        .append("	ddsie.nrInscricaoEstadual as nrInscricaoEstadualDevDocServ,")
        .append("	ie.id as inscricaoEstadualDev)")
        .append(" from Conhecimento conh")
	    .append("   join conh.devedorDocServs dds")
	    .append("   left outer join dds.inscricaoEstadual ddsie")
	    .append("   left outer join conh.tarifaPreco tf")
	    .append("   left outer join conh.tabelaPreco tp")
	    .append("   left outer join tp.tipoTabelaPreco ttp")
	    .append("   left outer join tp.subtipoTabelaPreco stp")
	    .append("   join dds.cliente clienteDev")
	    .append("   left outer join conh.tipoTributacaoIcms tti")
	    .append("   join clienteDev.pessoa pesDev")
	    .append("   left outer join clienteDev.observacaoConhecimento obsConhDev")
	    .append("   join pesDev.inscricaoEstaduais ie")
	    .append("   join conh.clienteByIdClienteRemetente clienteRem")
	    .append("   join clienteRem.pessoa pesRem")
	    .append("   join conh.clienteByIdClienteDestinatario clienteDest")
	    .append("   join clienteDest.pessoa pesDest")
	    .append("   left outer join conh.clienteByIdClienteConsignatario clienteConsig")
	    .append("   left outer join clienteConsig.pessoa pesConsig")
	    .append("   left outer join conh.clienteByIdClienteRedespacho clienteRedesp")
	    .append("   left outer join clienteRedesp.pessoa pesRedesp")
	    .append("   join conh.naturezaProduto np")
	    .append("   left outer join conh.usuarioByIdUsuarioInclusao u")
	    .append("   join conh.municipioByIdMunicipioColeta munCol")
	    .append("   join conh.municipioByIdMunicipioEntrega munEnt")
	    .append("   left outer join conh.servico sv")
	    .append("   left outer join conh.divisaoCliente dc")
	    .append("   left outer join conh.servAdicionalDocServs sads")
	    .append("   left outer join sads.servicoAdicional sa")
	    .append("   left outer join conh.filialByIdFilialDestino fDest")
	    .append("   left outer join conh.filialByIdFilialOrigem fOrig")
	    .append("   left outer join fDest.filialByIdFilialResponsavel fDestinoResp")
	    .append(" where conh.id = :idConhecimento")
		.append("   and ie.blIndicadorPadrao = 'S'")
		.append("   and (sa.id = 26 or sa is null)");

		List result = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idConhecimento", idConhecimento);
		Map map = (Map) result.get(0);

		return map;
	}

	public List findConhecimentosPendentesLMS(Long idFilial) {
		StringBuffer sql = new StringBuffer()
		.append("select ")
		.append("	new map(vnf.nrConhecimento as nrConhecimento, ")
		.append("	fDest.idFilial as idFilialDestino, ")
		.append("	conh.dhEmissao as dhEmissao) ")
        .append(" from Conhecimento conh")
	    .append("   left outer join conh.filialByIdFilialOrigem fOrig ")
	    .append("   left outer join conh.filialByIdFilialDestino fDest ")
	    .append("   left outer join conh.notaFiscalConhecimentos nfc ")
	    .append("   left outer join nfc.volumeNotaFiscais vnf ")
	    .append("   left outer join vnf.monitoramentoDescarga md ")
	    .append(" where fOrig.id = :idFilial ")
		.append("   and conh.tpSituacaoConhecimento = 'P' ")
		.append("   and vnf.nrConhecimento is not null ")
		.append("   and vnf.nrSequencia = 1 ")
		.append("   and vnf.tpVolume in ('M', 'U') ")
		.append("   and md.tpSituacaoDescarga <> 'S' ")
		.append(" order by vnf.nrConhecimento ");
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), "idFilial", idFilial);
	}

	/**
	 * Retorna N Conhecimentos limitados pelo rownum a serem calculados Frete na Tabela Cheia
	 * @return
	 */
	public List findConhecimentosCalculoFreteTabelaCheiaByRownum(Long rownum) {
		StringBuffer sql = new StringBuffer()
		.append("select ")
		.append("	conh.id ")
        .append(" from Conhecimento conh")
	    .append("   inner join conh.clienteByIdClienteBaseCalculo clie ")
	    .append(" where conh.tpSituacaoConhecimento = 'E' ")
		.append("   and conh.tpConhecimento = 'NO' ")
		.append("   and conh.tpCalculoPreco = 'N' ")
		.append("   and conh.vlFreteTabelaCheia is null ")
		.append("   and (clie.tpCliente = 'S' or clie.tpCliente = 'F') ")
		.append("   and rownum <= " + (rownum == null ? 1 : rownum) );
		return getAdsmHibernateTemplate().find(sql.toString());
	}

	/**
	 * Retorna N Conhecimentos limitados pelo rownum a serem calculados Frete
	 * @return
	 */
	public List<Long> findConhecimentosCalculoFreteTabelaDiferenciadaByRownum(Long rownum) {
		StringBuffer sql = new StringBuffer()
		.append("select distinct ")
		.append("conh.id ")
		.append("from Conhecimento conh ")
		.append("inner join conh.tabelaPreco tp ")
		.append("inner join tp.tipoTabelaPreco ttp ")
	    .append("where ")
	    .append("conh.tpSituacaoConhecimento = 'E' ")
		.append("and conh.tpSituacaoAtualizacao = 'A' ")
		.append("and trunc(cast (conh.dhEmissao.value as date)) < to_date('07/07/2010', 'dd/mm/yyyy') ")
		.append("and conh.vlFreteTabelaCheia is null ")
		.append("and conh.usuarioByIdUsuarioInclusao.id <> 5000 ")
		.append("and tp.id not in (2465,2046,2047) ")
		.append("and ttp.tpTipoTabelaPreco = 'D' ");

		if (rownum != null) {
			sql.append("and rownum <= " + rownum.longValue());
		}
		sql.append(" order by conh.id asc ");

		return getAdsmHibernateTemplate().find(sql.toString());
	}

	/**
	 * Busca o conhecimento que possui a nota fiscal recebida como parametro.
	 *
	 * @param idNotaFiscal
	 * @return
	 */
	public Conhecimento findConhecimentoByNotaFiscal(Long idNotaFiscal) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "c")
		.createAlias("c.filialOrigem", "fo")
		.createAlias("fo.pessoa", "fop")
		.createAlias("c.notaFiscalConhecimentos","nf")
		.add(Restrictions.eq("nf.idNotaFiscalConhecimento", idNotaFiscal));

		return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(dc);
	}

	/**
	 * Remove objetos relacionados com conhecimento e que não tenham realizado pesagem e tao pouco reservado número de CTRC do SOM
	 * Objectos removidos: MONITORAMENTO_DESCARGA
	 * 	DEVEDOR_DOC_SERV_FAT
	 * 	OBSERVACAO_DOCTO_SERVICO
	 * 	DEVEDOR_DOC_SERV
	 * 	NOTA_FISCAL_CONHECIMENTO
	 * 	VOLUME_NOTA_FISCAL
	 * 	DADOS_COMPLEMENTO
	 * 	NF_DADOS_COMP
	 * @param idConhecimento
	 */
	public void removeAllObjectsRelatedWithConhecimento(Long idConhecimento) {

		// Antes de iniciar o processo de exclusao dos objetos, primeiro verifico se não foi reservado número de CTRC no SOM
		List<Long> existeReservaCTRC = null;
		StringBuffer sql = new StringBuffer("");
		sql.append("select vonf.idVolumeNotaFiscal from ");
		sql.append(" NotaFiscalConhecimento as nofc, ");
		sql.append(" Conhecimento as conh, ");
		sql.append(" VolumeNotaFiscal as vonf, ");
		sql.append(" MonitoramentoDescarga as modes ");
		sql.append("where conh.idDoctoServico = ? ");
		sql.append(" and conh.tpSituacaoConhecimento = 'P'");
		sql.append(" and nofc.conhecimento.idDoctoServico = conh.idDoctoServico ");
		sql.append(" and vonf.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento ");
		sql.append(" and ( vonf.psAferido is not null or vonf.nrConhecimento >= 0 ) ");
		sql.append(" and modes.idMonitoramentoDescarga = vonf.monitoramentoDescarga.idMonitoramentoDescarga ");

		existeReservaCTRC = getAdsmHibernateTemplate().find(sql.toString(), idConhecimento);

		// Caso não tiver reserva de CTRC do SOM, pode seguir processo de remoção dos objetos.
		if (existeReservaCTRC == null || existeReservaCTRC.size() <= 0) {

			// Busco todos monitoramentoDescarga relacionado com o VolumeNotaFiscal, para depois apaga-los
			List<Long> listMonitoramentoDescarga = null;
			sql = new StringBuffer("");
			sql.append("select distinct modes.idMonitoramentoDescarga from ");
			sql.append(" NotaFiscalConhecimento as nofc, ");
			sql.append(" VolumeNotaFiscal as vonf, ");
			sql.append(" MonitoramentoDescarga as modes ");
			sql.append("where nofc.conhecimento.idDoctoServico = ? ");
			sql.append(" and vonf.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento ");
			sql.append(" and modes.idMonitoramentoDescarga = vonf.monitoramentoDescarga.idMonitoramentoDescarga ");
			sql.append(" and modes.idMonitoramentoDescarga not in ");
			sql.append(" 	(select distinct vonf2.monitoramentoDescarga.idMonitoramentoDescarga ");
			sql.append(" 	 from VolumeNotaFiscal vonf2, ");
			sql.append(" 	  NotaFiscalConhecimento nofc2 ");
			sql.append(" 	 where nofc2.idNotaFiscalConhecimento = vonf2.notaFiscalConhecimento.idNotaFiscalConhecimento ");
			sql.append(" 	  and nofc2.conhecimento.idDoctoServico != ? ");
			sql.append(" 	  and vonf2.monitoramentoDescarga.idMonitoramentoDescarga is not null ");
			sql.append(" 	 ) ");

			listMonitoramentoDescarga = getAdsmHibernateTemplate().find(sql.toString(), new Object [] {idConhecimento, idConhecimento});

			//Apago todos volumeNotaFiscal relacionados com o Conhecimento
			String hqlDeleteVolumeNotaFiscal = "delete from " + VolumeNotaFiscal.class.getName() +
							   " where idVolumeNotaFiscal in (select vonf.idVolumeNotaFiscal from VolumeNotaFiscal vonf, " +
							   "             NotaFiscalConhecimento nofc " +
							   "             where vonf.notaFiscalConhecimento.idNotaFiscalConhecimento = nofc.idNotaFiscalConhecimento " +
							   "             and nofc.conhecimento.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteVolumeNotaFiscal, idConhecimento);

			//Apago todos nfDadosComp relacionados com o NotaFiscalConhecimento
			String hqlDeleteNfDadosComp = "delete from " + NfDadosComp.class.getName() +
							   " where idNfDadosComp in (select nfdc.idNfDadosComp from NfDadosComp nfdc " +
							   "             where nfdc.notaFiscalConhecimento.conhecimento.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteNfDadosComp, idConhecimento);

			//Apago todos notaFiscalConhecimento relacionados com o Conhecimento
			String hqlDeleteNotaFiscalConhecimento = "delete from " + NotaFiscalConhecimento.class.getName() +
							   " where idNotaFiscalConhecimento in (select nofc.idNotaFiscalConhecimento from NotaFiscalConhecimento nofc " +
							   "             where nofc.conhecimento.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteNotaFiscalConhecimento, idConhecimento);

			//Apago todos dadosComplemento relacionados com o Conhecimento
			String hqlDeleteDadosComplemento = "delete from " + DadosComplemento.class.getName() +
							   " where idDadosComplemento in (select daco.idDadosComplemento from DadosComplemento daco " +
							   "             where daco.conhecimento.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteDadosComplemento, idConhecimento);

			//Apago todos devedorDocServ relacionados com o Conhecimento
			String hqlDeleteDevedorDocServ = "delete from " + DevedorDocServ.class.getName() +
							   " where idDevedorDocServ in (select deds.idDevedorDocServ from DevedorDocServ deds " +
							   "             where deds.doctoServico.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteDevedorDocServ, idConhecimento);

			//Apago todos observacaoDoctoServico relacionados com o Conhecimento
			String hqlDeleteObservacaoDoctoServico = "delete from " + ObservacaoDoctoServico.class.getName() +
							   " where idObservacaoDoctoServico in (select obds.idObservacaoDoctoServico from ObservacaoDoctoServico obds " +
							   "             where obds.doctoServico.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteObservacaoDoctoServico, idConhecimento);

			//Apago todos devedorDocServFat relacionados com o Conhecimento
			String hqlDeleteDevedorDocServFat = "delete from " + DevedorDocServFat.class.getName() +
							   " where idDevedorDocServFat in (select ddsf.idDevedorDocServFat from DevedorDocServFat ddsf " +
							   "             where ddsf.doctoServico.idDoctoServico = :id )";
			getAdsmHibernateTemplate().removeById(hqlDeleteDevedorDocServFat, idConhecimento);

			//Apago o conhecimento e o serviço
			Conhecimento conhecimento = findById(idConhecimento);
			getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().delete(conhecimento);

			//Apago todos monitoramentoDescarga
			if (listMonitoramentoDescarga != null && listMonitoramentoDescarga.size() == 1) {
				for (Long idMonitoramentoDescarga : listMonitoramentoDescarga) {
					String hqlDeleteMonitoramentoDescarga = "delete from " + MonitoramentoDescarga.class.getName() +
					" where idMonitoramentoDescarga = :id )";
					getAdsmHibernateTemplate().removeById(hqlDeleteMonitoramentoDescarga, idMonitoramentoDescarga);
				}
			}

		} else {
			throw new BusinessException("LMS-04258");
		}
	}


    /**
     * Retorna o DoctoServico pelo idVolumeNotaFiscal
     * @param idVolumeNotaFiscal
     * @return
     */
    public Conhecimento findConhecimentoByIdVolumeNotaFiscal(Long idVolumeNotaFiscal){
		DetachedCriteria dc = DetachedCriteria.forClass(Conhecimento.class);
		dc.setFetchMode("notaFiscalConhecimentos", FetchMode.JOIN);
		dc.createAlias("notaFiscalConhecimentos", "notaFiscalConhecimentos");

		dc.setFetchMode("filialOrigem", FetchMode.JOIN);

		dc.setFetchMode("notaFiscalConhecimentos.volumeNotaFiscais", FetchMode.JOIN);
		dc.createAlias("notaFiscalConhecimentos.volumeNotaFiscais", "volumeNotaFiscais");

		dc.add(Restrictions.eq("volumeNotaFiscais.id", idVolumeNotaFiscal));

   		return (Conhecimento)getAdsmHibernateTemplate().findUniqueResult(dc);

    }

	public Long getRowCountCarregadosSemPreManifestoDocumento(long idControleCarga) {
		StringBuffer sql = new StringBuffer("select count(distinct con.id) from ")
		.append(Conhecimento.class.getName())
		.append(" con ")
		.append("	left join con.preManifestoVolumes pmv ")
		.append("	left join pmv.manifesto man ")
		.append("	left join man.controleCarga cc ")
		.append(" where cc.id = ?")
		.append(" 	and pmv.preManifestoDocumento is null");

		return (Long) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{Long.valueOf(idControleCarga)});
	}

	public List<Long> findDadosConhecimentosToSOM(Long idFilial, List<Long> nrConhecimentosSOM, DateTime date) {
		return getSession()
			.createSQLQuery("select id_docto_servico from DOCTO_SERVICO where id_filial_origem = :idFilial and dh_inclusao >= :date and (SUBSTR(LPAD(TO_CHAR(nr_docto_servico),10,'0'),5,6)) in (:nrConhecimentos) ")
				.addScalar("id_docto_servico", Hibernate.LONG)
				.setParameterList("nrConhecimentos", nrConhecimentosSOM)
				.setParameter("idFilial",idFilial)
				.setTimestamp("date", date.toDate())
					.list();
	}

	public Conhecimento loadConhecimentosSubstituto(Long idDoctoServico){
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT c ");
		hql.append(" FROM ");
		hql.append(Conhecimento.class.getSimpleName() + " c ");
		hql.append(" join fetch c.filialOrigem AS f ");
		hql.append(" WHERE ");
		hql.append(" c.tpDocumentoServico =? AND ");
		hql.append(" c.tpConhecimento =? AND ");
		hql.append(" c.doctoServicoOriginal.idDoctoServico =?");
		
		List<Conhecimento> l =  getAdsmHibernateTemplate().find(hql.toString(), new Object[]{"CTE", "SU", Long.valueOf(idDoctoServico)});		
		
		if(l.isEmpty())
			return null;
		
		return l.get(0);
		
	}
	
	public Long getRowCountCarregadosIncompletos(long idControleCarga) {
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("count (DISTINCT CON.NR_CONHECIMENTO)");
		
		sql.addFrom("CONHECIMENTO CON");
		sql.addFrom("PRE_MANIFESTO_DOCUMENTO PMD");
		sql.addFrom("DOCTO_SERVICO DS");
		sql.addFrom("MANIFESTO M");
		sql.addFrom("NOTA_FISCAL_CONHECIMENTO NFC");
		sql.addFrom("VOLUME_NOTA_FISCAL VNF");
		sql.addFrom("LOCALIZACAO_MERCADORIA LM");
		sql.addFrom("FILIAL FLA");
		sql.addFrom("FILIAL FL");
		sql.addFrom("FILIAL FM");
		sql.addFrom("FILIAL FC");
		sql.addFrom("PEDIDO_COLETA PC");
		sql.addFrom("CONTROLE_CARGA CC");
		sql.addFrom("PESSOA PS");
		sql.addFrom("MANIFESTO_COLETA MCC");
		sql.addFrom("CONTROLE_CARGA CCC");
		sql.addFrom("MEIO_TRANSPORTE MT");
		
		sql.addCustomCriteria("CON.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO");
		sql.addCustomCriteria("CON.ID_CONHECIMENTO             = PMD.ID_DOCTO_SERVICO");
		sql.addCustomCriteria("PMD.ID_MANIFESTO                = M.ID_MANIFESTO");
		sql.addCustomCriteria("CC.ID_CONTROLE_CARGA                = M.ID_CONTROLE_CARGA");
		sql.addCustomCriteria("NFC.ID_CONHECIMENTO               = CON.ID_CONHECIMENTO");
		sql.addCustomCriteria("NFC.ID_NOTA_FISCAL_CONHECIMENTO = VNF.ID_NOTA_FISCAL_CONHECIMENTO");
		sql.addCustomCriteria("VNF.ID_LOCALIZACAO_MERCADORIA   = LM.ID_LOCALIZACAO_MERCADORIA (+)");
		sql.addCustomCriteria("M.ID_FILIAL_ORIGEM        = FM.ID_FILIAL");
		sql.addCustomCriteria("CC.ID_FILIAL_ORIGEM        		 = FL.ID_FILIAL");
		sql.addCustomCriteria("CON.ID_FILIAL_ORIGEM        		 = FC.ID_FILIAL");
		sql.addCustomCriteria("DS.ID_PEDIDO_COLETA             = PC.ID_PEDIDO_COLETA (+)");
		sql.addCustomCriteria("PC.ID_CLIENTE                   = PS.ID_PESSOA (+)");
		sql.addCustomCriteria("PC.ID_MANIFESTO_COLETA          = MCC.ID_MANIFESTO_COLETA (+)");
		sql.addCustomCriteria("MCC.ID_CONTROLE_CARGA           = CCC.ID_CONTROLE_CARGA (+)");
		sql.addCustomCriteria("CCC.ID_TRANSPORTADO             = MT.ID_MEIO_TRANSPORTE (+)");
		sql.addCustomCriteria("M.ID_CONTROLE_CARGA             = "+idControleCarga);
		sql.addCustomCriteria("VNF.ID_LOCALIZACAO_FILIAL       = FLA.ID_FILIAL (+)");
		sql.addCustomCriteria("VNF.ID_VOLUME_NOTA_FISCAL NOT  IN (SELECT PMV.ID_VOLUME_NOTA_FISCAL FROM PRE_MANIFESTO_VOLUME PMV WHERE PMV.ID_MANIFESTO = M.ID_MANIFESTO)");
		sql.addCustomCriteria("VNF.TP_VOLUME IN ('U','D')");

		sql.addOrderBy("VNF.NR_SEQUENCIA_PALETE");
		
		try{
			Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
			PreparedStatement statement = connection.prepareStatement(sql.toString());
			ResultSet rs = statement.executeQuery();
			if(rs.next()){
				return rs.getLong(1);
	}
		}catch (Exception e) {
			log.error(e);
		}
		return null;
	}

    public Conhecimento findConhecimentoReentregaByDoctoServicoOriginal(Long idDoctoServicoOriginal) {
		StringBuilder hql = new StringBuilder();
		hql.append("from " + getPersistentClass().getName() + " as conhecimento ");
		hql.append("left join fetch  conhecimento.doctoServicoOriginal as doctoServicoOriginal ");
		hql.append("inner join fetch  conhecimento.filialByIdFilialOrigem as filialByIdFilialOrigem ");
		hql.append("where doctoServicoOriginal.id = ? ");
		hql.append("and conhecimento.tpConhecimento = ? ");
		hql.append("and conhecimento.tpSituacaoConhecimento = ? ");
		hql.append("and conhecimento.dhEmissao is not null ");
		hql.append("order by conhecimento.dhEmissao desc ");

		Object[] param = new Object[3];
		param[0] = idDoctoServicoOriginal;
		param[1] = ConstantesExpedicao.CONHECIMENTO_REENTREGA;
		param[2] = "E"; // EMITIDO

		List<Conhecimento> result = getAdsmHibernateTemplate().find(hql.toString(),param);

		if(result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
	}
	}
    
    public Conhecimento findConhecimentoAtualById(Long idConhecimento) {
		DetachedCriteria dc = DetachedCriteria.forClass(Conhecimento.class)
		.setFetchMode("clienteByIdClienteRemetente", FetchMode.JOIN)
		.setFetchMode("clienteByIdClienteDestinatario", FetchMode.JOIN)
		.setFetchMode("clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN)
		.setFetchMode("clienteByIdClienteRemetente.pessoa", FetchMode.JOIN)
		.setFetchMode("filialOrigem", FetchMode.JOIN)
		.setFetchMode("servico", FetchMode.JOIN)

		.add(Restrictions.or(Restrictions.eq("idDoctoServico", idConhecimento),
							 Restrictions.and(Restrictions.eq("doctoServicoOriginal.id", idConhecimento),
									 		  Restrictions.eq("tpConhecimento", ConstantesExpedicao.CONHECIMENTO_REENTREGA)
									 		  )
							)
			)
		.add(Restrictions.eq("tpSituacaoConhecimento", "E"))
		.add(Restrictions.isNotNull("dhEmissao"))
		.addOrder(Order.desc("idDoctoServico"));

		List<Conhecimento> result = super.findByDetachedCriteria(dc);
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
    
    public Conhecimento findConhecimentoAtualByIdWithCriteria(Long idConhecimento) {
		Criteria criteria = createCriteria();

		criteria.add(Restrictions.or(Restrictions.eq("idDoctoServico", idConhecimento),
							 Restrictions.and(Restrictions.eq("doctoServicoOriginal.id", idConhecimento),
									 		  Restrictions.eq("tpConhecimento", ConstantesExpedicao.CONHECIMENTO_REENTREGA)
									 		  )
							)
			)
		.add(Restrictions.eq("tpSituacaoConhecimento", "E"))
		.add(Restrictions.isNotNull("dhEmissao"))
		.addOrder(Order.desc("idDoctoServico"));

		List<Conhecimento> result = criteria.list();
		if(result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
    
    public Conhecimento findConhecimentoAtualByIdSorter(Long idConhecimento) {
		StringBuilder hql = new StringBuilder();

		DetachedCriteria dc = DetachedCriteria.forClass(Conhecimento.class)
		.setFetchMode("clienteByIdClienteRemetente", FetchMode.JOIN)
		.setFetchMode("clienteByIdClienteDestinatario", FetchMode.JOIN)
		.setFetchMode("clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN)
		.setFetchMode("clienteByIdClienteRemetente.pessoa", FetchMode.JOIN)
		.setFetchMode("filialOrigem", FetchMode.JOIN)
		.add(Restrictions.or(Restrictions.eq("idDoctoServico", idConhecimento),
							 Restrictions.and(Restrictions.eq("doctoServicoOriginal.id", idConhecimento),
									 		  Restrictions.eq("tpConhecimento", ConstantesExpedicao.CONHECIMENTO_REENTREGA)
									 		  )
							)
			)
		.add(Restrictions.or(Restrictions.eq("tpSituacaoConhecimento", "E"), Restrictions.eq("tpSituacaoConhecimento", "P")))
		.addOrder(Order.desc("idDoctoServico"));

		List<Conhecimento> result = super.findByDetachedCriteria(dc);

		if(result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	public List<Long> findConhecimentosVNFPendentesToSOM(Long idFilial, DateTime date){
		StringBuilder query = new StringBuilder()
		.append(" select distinct vonf.nrConhecimento as nrConhecimento ")
		.append(" from VolumeNotaFiscal vonf ")
		.append(" 	join vonf.notaFiscalConhecimento nofc ")
		.append(" 	join nofc.conhecimento conh ")
		.append(" 	join vonf.monitoramentoDescarga mode ")
		.append(" 	join conh.filialOrigem fior ")
		.append(" where conh.tpSituacaoConhecimento = 'P' ")
		.append(" and fior.idFilial = ? ")
		.append(" and vonf.tpVolume in ('M', 'U') ")
		.append(" and mode.dhInicioDescarga.value >= ? ")
		.append(" and mode.tpSituacaoDescarga in ('M', 'G', 'P', 'J') ");

		return getAdsmHibernateTemplate().find(query.toString(), new Object [] {idFilial, date});
	}

	public List<Long> findConhecimentosTabelaY(Long rownum){

		StringBuilder query = new StringBuilder()

		.append(" select DS.ID_DOCTO_SERVICO AS ID_DOCTO_SERVICO ")
		.append(" from DOCTO_SERVICO DS,  ")
		.append(" CONHECIMENTO C, ")
		.append(" FILIAL F, ")
		.append(" UNIDADES_NEGOCIOS UN, ")
		.append(" CONHECIMENTOS CO ")
		.append(" where C.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
		.append(" and C.TP_SITUACAO_CONHECIMENTO = 'E' ")
		.append(" and C.BL_EMITIDO_LMS = 'S' ")

		.append(" and DS.ID_TABELA_PRECO in (select TP.ID_TABELA_PRECO ")
		.append(" from TABELA_PRECO TP ")
		.append(" where TP.ID_SUBTIPO_TABELA_PRECO = 54 ")
		.append(" and TP.BL_EFETIVADA = 'S') ")

		.append(" and F.ID_FILIAL = C.ID_FILIAL_ORIGEM ")
		.append(" and UN.CENTRO_CUSTO_JDE = F.SG_FILIAL ")
		.append(" and CO.UNID_SIGLA_ORIGEM = UN.SIGLA ")
		.append(" and CO.NUMERO = C.NR_CONHECIMENTO    ")
		.append(" and CO.STATUS <> '9'  ")
		.append(" and trunc(cast (ds.DH_EMISSAO as date)) < to_date('28/11/2010', 'dd/mm/yyyy') ")
		.append(" and DS.VL_FRETE_TABELA_CHEIA IS NULL  ");

		if (rownum != null) {
			query.append(" and ROWNUM < " + rownum.longValue());
		}
		query.append(" order by DS.ID_DOCTO_SERVICO asc ");

		return getSession().createSQLQuery(query.toString()).addScalar("ID_DOCTO_SERVICO", Hibernate.LONG).list();
	}

	public Conhecimento findByDadosComplemento(String dsValorCampo, Long idInformacaoDoctoCliente) {
		StringBuilder hql = new StringBuilder()
			.append("select conhecimento ")
			.append("from " + this.getPersistentClass().getName() + " conhecimento ")
			.append("inner join fetch conhecimento.dadosComplementos dadosComplemento ")
			.append("left join fetch conhecimento.localizacaoMercadoria localizacaoMercadoria ")
			.append("inner join fetch conhecimento.filialOrigem filialOrigem ")
			.append("where dadosComplemento.dsValorCampo = :dsValorCampo ")
			.append("and dadosComplemento.informacaoDoctoCliente.id = :idInformacaoDoctoCliente ")
			.append("and conhecimento.tpSituacaoConhecimento <> :tpSituacaoCancelado ")
			.append("and conhecimento.tpSituacaoConhecimento <> :tpSituacaoPreConhecimento ");

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("dsValorCampo", dsValorCampo);
		params.put("idInformacaoDoctoCliente", idInformacaoDoctoCliente);
		params.put("tpSituacaoCancelado", "C");
		params.put("tpSituacaoPreConhecimento", "P");

		return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
	}

	public boolean existeReentrega(Long idDoctoServico){
		DetachedCriteria dc = DetachedCriteria.forClass(DoctoServico.class);
		dc.add(Restrictions.eq("doctoServicoOriginal.id", idDoctoServico));

		List lista = getHibernateTemplate().findByCriteria(dc, 0, 1);
		return !lista.isEmpty();

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Long getRowCountByCodigoBarras(Long nrCodigoBarrasInicial, Long nrCodigoBarrasFinal){
    	Map criteria = new HashMap();
    	criteria.put("nrCodigoBarrasInicial", nrCodigoBarrasInicial);
    	criteria.put("nrCodigoBarrasFinal", nrCodigoBarrasFinal);

    	return (Long) getAdsmHibernateTemplate().findUniqueResult("select count(*) from Conhecimento as conh where conh.nrCodigoBarras/10 >= :nrCodigoBarrasInicial and conh.nrCodigoBarras/10 <= :nrCodigoBarrasFinal ", criteria);
	}

	/**
	 * Busca todos conhecimentos EDI do Monitoramento Descarga
	 * @author André Valadas
	 *
	 * @param idMonitoramentoDescarga
	 * @param isLabeling
	 * @return
	 */
	public List<Conhecimento> findDoctoServicoEDI(final Long idMonitoramentoDescarga, final Boolean isLabeling) {
		final DetachedCriteria dc = DetachedCriteria.forClass(MonitoramentoDescarga.class, "mo");
		dc.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("c.id"), "idDoctoServico")
				.add(Projections.property("c.nrConhecimento"), "nrConhecimento")
				.add(Projections.property("c.dtPrevEntrega"), "dtPrevEntrega")
				.add(Projections.property("c.nrDiasPrevEntrega"), "nrDiasPrevEntrega")
				.add(Projections.property("c.tpDoctoServico"), "tpDoctoServico")
				.add(Projections.property("c.blRedespachoIntermediario"), "blRedespachoIntermediario")
				.add(Projections.property("ccr.idCliente"), "clienteByIdClienteRemetente.idCliente")
				.add(Projections.property("ccr.blLiberaEtiquetaEdiLinehaul"), "clienteByIdClienteRemetente.blLiberaEtiquetaEdiLinehaul")
				.add(Projections.property("ccr.blLiberaEtiquetaEdi"), "clienteByIdClienteRemetente.blLiberaEtiquetaEdi")
				.add(Projections.property("fo.id"), "filialByIdFilialOrigem.idFilial")
				.add(Projections.property("fd.id"), "filialByIdFilialDestino.idFilial")));

		dc.createAlias("mo.volumesNotaFiscal", "vnf");
		dc.createAlias("vnf.notaFiscalConhecimento", "nfc");
		dc.createAlias("nfc.conhecimento", "c");
		dc.createAlias("c.clienteByIdClienteRemetente", "ccr");
		dc.createAlias("c.filialByIdFilialOrigem", "fo");
		dc.createAlias("c.filialByIdFilialDestino", "fd");
		dc.add(Restrictions.eq("mo.id", idMonitoramentoDescarga));
		dc.add(Restrictions.lt("c.nrConhecimento", LongUtils.ZERO));
		if(Boolean.TRUE.equals(isLabeling)) {
		dc.add(Restrictions.eq("ccr.blLiberaEtiquetaEdi", Boolean.TRUE));
		}
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Conhecimento.class));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}


	/**
	 * Busca os conhecimento do monitoramento descarga
	 * @param idMonitoramentoDescarga
	 * @return
	 */
	public List<Conhecimento> findDoctoServico(Long idMonitoramentoDescarga) {
		final DetachedCriteria dc = DetachedCriteria.forClass(MonitoramentoDescarga.class, "mo");
		dc.setProjection(Projections.distinct(Projections.projectionList()
			.add(Projections.property("c.id"), "idDoctoServico")
			.add(Projections.property("c.nrConhecimento"), "nrConhecimento")
			.add(Projections.property("c.dtPrevEntrega"), "dtPrevEntrega")
			.add(Projections.property("c.nrDiasPrevEntrega"), "nrDiasPrevEntrega")
			.add(Projections.property("c.tpDoctoServico"), "tpDoctoServico")
			.add(Projections.property("fo.id"), "filialByIdFilialOrigem.idFilial")
			.add(Projections.property("fd.id"), "filialByIdFilialDestino.idFilial"))
		);

		dc.createAlias("mo.volumesNotaFiscal", "vnf");
		dc.createAlias("vnf.notaFiscalConhecimento", "nfc");
		dc.createAlias("nfc.conhecimento", "c");
		dc.createAlias("c.clienteByIdClienteRemetente", "ccr");
		dc.createAlias("c.filialByIdFilialOrigem", "fo");
		dc.createAlias("c.filialByIdFilialDestino", "fd");
		dc.add(Restrictions.eq("mo.id", idMonitoramentoDescarga));
		dc.add(Restrictions.lt("c.nrConhecimento", LongUtils.ZERO));
		dc.setResultTransformer(new AliasToNestedBeanResultTransformer(Conhecimento.class));
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}


	/**
	 * Busca os dados do Conhecimento para enviar para enviar para a NDDigital, gerando dessa forma a nota fiscal de serviço
	 * eletronica - ( NTE - Nota de transporte eletronica)
	 * @param idConhecimento
	 * @return
	 */
	public Conhecimento findDadosNteToNotaFiscalEletronica(Long idConhecimento){
		StringBuffer hql = new StringBuffer();

		hql.append("select conhecimento ")
			.append("from " + this.getPersistentClass().getName() + " conhecimento ")
			.append("inner join conhecimento.servAdicionalDocServ servAdicionalDocServ ")
			.append("inner join servAdicionalDocServ.servicoAdicionais servicoAdicionais")
			.append("inner join servicoAdicionais.servicoOficialTributo servicoOficialTributo " )
			.append("inner join conhecimento.municipioByIdMunicipioColeta municipioColeta ")
			.append("inner join conhecimento.impostoServicos impostoServicos ")
			.append("inner join conhecimento.devedorDocServs devedorDocServs ")
			.append("inner join devedorDocServs.cliente cliente ")
			.append("inner join cliente.pessoa pessoa ")
			.append("where conhecimento.id = :idConhecimento ");

		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idConhecimento", idConhecimento);

		return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params);
	}

	public List findByListVolumes(List<VolumeNotaFiscal> volumes) {
		List<Long> idsVolumes = new ArrayList<Long>();
		for (VolumeNotaFiscal volume:volumes){
			idsVolumes.add(volume.getIdVolumeNotaFiscal());
		}
		DetachedCriteria dc = DetachedCriteria.forClass(VolumeNotaFiscal.class, "vnf");
		dc.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.property("c.id"), "idDoctoServico")
				.add(Projections.property("cliente.blLiberaEtiquetaEdi"), "blLiberaEtiquetaEdi")
		));
		dc.createAlias("vnf.notaFiscalConhecimento", "nfc");
		dc.createAlias("nfc.conhecimento", "c");
		dc.createAlias("c.clienteByIdClienteBaseCalculo", "cliente");
		dc.add(Restrictions.in("vnf.idVolumeNotaFiscal", idsVolumes));
		dc.setResultTransformer(new AliasToNestedMapResultTransformer());
		return getAdsmHibernateTemplate().findByDetachedCriteria(dc);
	}

	public Conhecimento findConhecimentoByNrConhecimentoIdFilial(Long nrConhecimento, Long idFilial) {
		StringBuffer sb = new StringBuffer()
		.append(" from ").append(getPersistentClass().getName()).append(" as c")
		
		.append( " inner join fetch c.localizacaoMercadoria merc " )
		.append( " where c.nrConhecimento = ?")
		.append( " and c.filialOrigem.id = ?");

		return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(sb.toString(), new Object[]{nrConhecimento, idFilial});
	}
	
	@SuppressWarnings("unchecked")
	public List<Conhecimento> findConhecimentosEmProcessamentoEDIByIdMonitoramentoDescarga(Long idMonitoramentoDescarga) {
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT DISTINCT c ");
		hql.append("FROM " + this.getPersistentClass().getName() + " c "); 
		hql.append("INNER JOIN c.notaFiscalConhecimentos nfc ") ;
		hql.append("INNER JOIN nfc.volumeNotaFiscais vnf ");
		hql.append("INNER JOIN vnf.monitoramentoDescarga m ");
		hql.append("WHERE ");
		hql.append("m.idMonitoramentoDescarga = ? ");
		hql.append("AND m.tpSituacaoDescarga = ?");
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{idMonitoramentoDescarga, ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO});
	}
	
	public List<Conhecimento> findConhecimentosEmProcessamentoEDI(String nrIdentificacaoReme, String nrNotaFiscal) {
		
		StringBuffer hql = new StringBuffer();

        String subqueryConhecimento = "select n.conhecimento from NotaFiscalConhecimento n"
                + " inner join n.cliente cli "
                + " inner join cli.pessoa p "
                + "where "
                + " p.nrIdentificacao = ?"
                + " and n.nrNotaFiscal = ?";


		hql.append("select distinct coMo ")
		.append(" from " + this.getPersistentClass().getName() + " c ") 
		.append(" inner join c.notaFiscalConhecimentos nfc ") 
		.append(" inner join nfc.volumeNotaFiscais vnf ")
		.append(" inner join vnf.monitoramentoDescarga m ")
		.append(" inner join m.volumesNotaFiscal vnfMo ")
		.append(" inner join vnfMo.notaFiscalConhecimento nfcMo ")
		.append(" inner join nfcMo.conhecimento coMo ")
		.append(" where")
		.append(" c.id in ("+subqueryConhecimento+")")
		 
        .append(" and m.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO+"'");
		
		Integer nrNf = null;
		if( nrNotaFiscal != null || !nrNotaFiscal.isEmpty()){
			nrNf = IntegerUtils.getInteger(nrNotaFiscal);
		}
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{nrIdentificacaoReme, nrNf});
	}
	
	/**
	 * Busca os conhecientos de um monitoramento de descarga baseado na notaFiscal
	 * 
	 * @param nrIdentificacaoReme
	 * @param nrNotaFiscal
	 * @return
	 */
	public List<Conhecimento> findConhecimentosEmProcessamentoEDINotaCheia(String nrIdentificacaoReme, String nrNotaFiscal) {
	        
	    StringBuffer hql = new StringBuffer();

        hql.append("select distinct coMo ")
        .append(" from " + this.getPersistentClass().getName() + " c ") 
        .append(" inner join c.notaFiscalConhecimentos nfc ") 
        .append(" inner join nfc.volumeNotaFiscais vnf ")
        .append(" inner join vnf.monitoramentoDescarga m ")
        .append(" inner join m.volumesNotaFiscal vnfMo ")
        .append(" inner join vnfMo.notaFiscalConhecimento nfcMo ")
        .append(" inner join nfcMo.conhecimento coMo ")
		.append("inner join nfc.cliente cli " )
		.append("inner join cli.pessoa p ")
		.append(" where p.nrIdentificacao = ?")
		.append(" and nfc.nrNotaFiscal = ?")
		.append(" and m.tpSituacaoDescarga = '"+ConstantesExpedicao.TP_SITUACAO_DESCARGA_PROCESSAMENTO_EDI_INICIADO+"'");
		Integer nrNf = null;
		if( nrNotaFiscal != null || !nrNotaFiscal.isEmpty()){
			nrNf = IntegerUtils.getInteger(nrNotaFiscal);
}
		return getAdsmHibernateTemplate().find(hql.toString(), new Object[]{nrIdentificacaoReme, nrNf});
}

	/**
	 * LMS-2505
	 * @param nrCodigoBarrasCTR
	 * @return List<Conhecimento>
	 */
	public List<Conhecimento> findListConhecimentoByCodigoBarras(Long nrCodigoBarrasCTR) {
		if (nrCodigoBarrasCTR == null) {
			return null;
		}
		StringBuilder hql = new StringBuilder("select c from ");
		hql.append(Conhecimento.class.getName());
		hql.append(" as c join fetch c.filialByIdFilialOrigem as fo ");
		hql.append(" join fetch fo.pessoa as pfo ");
		hql.append(" where c.nrCodigoBarras = ? ");
		hql.append(" and c.tpDoctoServico in ('CTR', 'NTE') ");

		List<Conhecimento> result = getAdsmHibernateTemplate().find(hql.toString(), new Object[] {nrCodigoBarrasCTR});
		return result;
	}

	@Override
	public Conhecimento findById(Long id) {
		return super.findById(id);
	}

	@SuppressWarnings({ "rawtypes" })
	public DadosEmbarquePipelineDTO findConhecimentosEmitidosByListTabelaPreco(List<TabelaPreco> lstTabelasPreco) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		hql.append("select new Map(conh.nrConhecimento as nrConhecimento ");
		hql.append("              ,conh.dhEmissao as dhEmissao");
		hql.append("              ,fill.sgFilial as sgFilial) ");
		hql.append("from Conhecimento conh ");
		hql.append("join conh.filialOrigem as fill ");
		hql.append("join conh.tabelaPreco as tapr ");
		hql.append("where  tapr in (:lstTabelasPreco) ");
		hql.append("and conh.tpSituacaoConhecimento = 'E' ");
		hql.append("group by conh.nrConhecimento, conh.dhEmissao, fill.sgFilial ");
		hql.append("having conh.dhEmissao.value = (select min(conh1.dhEmissao.value) ");
		hql.append("						from Conhecimento as conh1 ");
		hql.append("						join conh1.tabelaPreco as tapr ");
		hql.append("						where tapr in (:lstTabelasPreco) ");
		hql.append("						and conh1.tpSituacaoConhecimento = 'E' ");
		hql.append("						) ");
		params.put("lstTabelasPreco", lstTabelasPreco);
		
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		if (result != null && !result.isEmpty()) {
			AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(DadosEmbarquePipelineDTO.class);
			List transformed = transformer.transformListResult(result);
			return (DadosEmbarquePipelineDTO) transformed.get(0);
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes" })
	public DadosEmbarquePipelineDTO findConhecimentosEmitidosByListSimulacao(List simulacoes) {
		
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		
		hql.append("select new Map(conh.nrConhecimento as nrConhecimento ");
		hql.append("              ,conh.dhEmissao as dhEmissao ");
		hql.append("              ,fill.sgFilial as sgFilial) ");
		hql.append("from Conhecimento conh ");
		hql.append("inner join conh.filialOrigem as fill ");
		hql.append("inner join conh.parametroCliente as pacl  ");
		hql.append("inner join pacl.simulacao as simu ");
		hql.append("where simu in (:lstSimulacao) ");
		hql.append("and conh.tpSituacaoConhecimento = 'E' ");
		hql.append("and pacl.tpSituacaoParametro = 'A' ");
		hql.append("and pacl.dtVigenciaInicial <= :dtInicial ");
		hql.append("and pacl.dtVigenciaFinal >= :dtFinal ");
		hql.append("group by conh.nrConhecimento, conh.dhEmissao, fill.sgFilial ");
		hql.append("having conh.dhEmissao.value = (select min(conh1.dhEmissao.value) ");
		hql.append("						from Conhecimento conh1 ");
		hql.append("						inner join conh1.parametroCliente as pacl1 ");
		hql.append("						inner join pacl1.simulacao as simu1");
		hql.append("						where simu1 in (:lstSimulacao) ");
		hql.append("						and conh1.tpSituacaoConhecimento = 'E' ");
		hql.append("						and pacl1.tpSituacaoParametro = 'A' ");
		hql.append("						) ");
		hql.append("order by conh.nrConhecimento desc ");
		
		params.put("lstSimulacao", simulacoes);
		params.put("dtInicial", JTDateTimeUtils.getDataAtual());
		params.put("dtFinal", JTDateTimeUtils.getDataAtual());

		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), params);
		
		if (result != null && !result.isEmpty()) {
			AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(DadosEmbarquePipelineDTO.class);
			List transformed = transformer.transformListResult(result);
			return (DadosEmbarquePipelineDTO) transformed.get(0);
		}
		return null;
	}
	
	public Conhecimento findTpDocumentoServicoByCodBarEmbarque(Map criteria){
		SqlTemplate hql = new SqlTemplate();
		hql.addProjection("c");

		hql.addFrom(" Conhecimento c " +
					" inner join c.notaFiscalConhecimentos nfc " +
					" inner join nfc.volumeNotaFiscais vnf ");
		
		hql.addCriteria("c.filialOrigem.id", "=", criteria.get("idFilialOrigem"));
		hql.addCriteria("c.nrDoctoServico", "=", criteria.get("nrDoctoServico"));
		hql.addCriteria("vnf.nrVolumeEmbarque", "=", criteria.get("codBarEmbarque"));
		
		return (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
	}

    public List<Conhecimento> findCTEByIdFatura(Long idFatura) {
    	StringBuffer sql = new StringBuffer()
    		.append("select cn ")
    		.append("from ")
    		.append(Conhecimento.class.getName() + " as cn, ")
    		.append(DoctoServico.class.getName() + " as ds, ")
    		.append(MonitoramentoDocEletronico.class.getName() + " as mde ")
    		.append(" inner join ds.devedorDocServFats as ddsf ")
    		.append(" inner join ddsf.itemFaturas as ifa ")
    		.append(" inner join ddsf.filial as f ")
    		.append(" where mde.doctoServico.id = ds.id ")
    		.append(" and ds.id = cn.id ")
    		.append(" and cn.tpSituacaoConhecimento = 'E' ")
    		.append(" and cn.tpDoctoServico = 'CTE' ")
    		.append(" and mde.tpSituacaoDocumento = 'A' ")
    		.append(" and ifa.fatura.id = ? ")
    		.append(" order by f.sgFilial ");
    	return getAdsmHibernateTemplate().find(sql.toString(), new Object[] {idFatura});
	}

	public Map<String,Object> findComplementosXMLCTE(final Long idConhecimento){
		final StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DC.DS_DIVISAO_CLIENTE ,DS.NR_FATOR_DENSIDADE, F.SG_FILIAL, C.NR_COTACAO, ")
		.append(" (SELECT NM_USUARIO FROM (" )
		.append(" SELECT U.NM_USUARIO ")
		.append(" FROM  LIBERACAO_DOC_SERV LDS ")
		.append(" LEFT JOIN USUARIO U ON U.ID_USUARIO = LDS.ID_USUARIO ")
		.append(" WHERE LDS.ID_DOCTO_SERVICO = :idConhecimento ORDER BY LDS.ID_LIBERACAO_DOC_SERV) WHERE ROWNUM = 1 ")
		.append(" ) AS NM_USUARIO, ")
		.append(" (SELECT VI18N(DS_VALOR_DOMINIO_I) ") 
		.append(" FROM DOMINIO D ")
		.append(" INNER JOIN VALOR_DOMINIO VD ON VD.ID_DOMINIO = D.ID_DOMINIO ")
		.append(" WHERE NM_DOMINIO = 'DM_TIPO_CLIENTE' ")
		.append(" AND VL_VALOR_DOMINIO = CL.TP_CLIENTE) TP_CLIENTE_TOMADOR ") 
		.append(" FROM DOCTO_SERVICO DS ")
		.append(" LEFT JOIN DIVISAO_CLIENTE DC ON DC.ID_DIVISAO_CLIENTE = DS.ID_DIVISAO_CLIENTE ")
		.append(" LEFT JOIN COTACAO C ON C.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
		.append(" LEFT JOIN FILIAL F ON C.ID_FILIAL = F.ID_FILIAL ")
		.append(" LEFT JOIN DEVEDOR_DOC_SERV DDS ON DDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ")
		.append(" LEFT JOIN CLIENTE CL ON CL.ID_CLIENTE = DDS.ID_CLIENTE ")
		.append(" WHERE DS.ID_DOCTO_SERVICO = :idConhecimento ");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("DS_DIVISAO_CLIENTE",Hibernate.STRING);
				sqlQuery.addScalar("NR_FATOR_DENSIDADE",Hibernate.STRING);
				sqlQuery.addScalar("SG_FILIAL",Hibernate.STRING);
				sqlQuery.addScalar("NR_COTACAO",Hibernate.STRING);
				sqlQuery.addScalar("NM_USUARIO",Hibernate.STRING);
				sqlQuery.addScalar("TP_CLIENTE_TOMADOR",Hibernate.STRING);
			}
		};
		
		final HibernateCallback hcb = new HibernateCallback() {
			
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
            	csq.configQuery(query);

				query.setParameter("idConhecimento",idConhecimento);
				return query.list();
			}
		};
			
		List list = getHibernateTemplate().executeFind(hcb);
		Map<String,Object> map = new HashMap<String, Object>();
		if( list != null && list.size() > 0 ){
			Object[] o = (Object[])list.get(0);
			map.put("dsDivisaoCliente", o[0]);
			if( o[1] != null ){
				map.put("nrFatorDensidade", o[1] );
			}
			String nrCotacao = "";
			if( o[2] != null ){
				nrCotacao = nrCotacao + o[2] + " "; 
			}
			if( o[3] != null ){
				nrCotacao = nrCotacao + o[3]; 
			}
			map.put("nrCotacao", nrCotacao);
			map.put("nmUsuarioLiberacao", o[4]);
			map.put("tpClienteTomador", o[5]);
		}
		return map;
	}

	public Conhecimento findByIdJoinFiliais(final Long idConhecimento) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select c from ");
		hql.append(DoctoServico.class.getName());
		hql.append(" c join fetch c.filialByIdFilialOrigem filialByIdFilialOrigem ");
		hql.append(" join fetch c.filialByIdFilialDestino filialByIdFilialDestino ");
		hql.append(" left join fetch c.rotaColetaEntregaByIdRotaColetaEntregaReal rotaColetaEntregaByIdRotaColetaEntregaReal ");
		hql.append(" where c.id = ? ");
		Conhecimento result = (Conhecimento) getAdsmHibernateTemplate().findUniqueResult(hql.toString(),
				new Object[] { idConhecimento });
		return result;
	}
	
    /**
     * LMS-3054
     * 
     * Busca os pré-CTEs anteriores ao valor do parâmetro geral
     * "TMP_MONIT_DESC_COL_EXC" (sysdate - "horas param")
     * para posterior exclusão via agendamento/batch.
     * 
     * @param olderThan
     * @return lista com os CTEs
     */
	@SuppressWarnings("unchecked")
	public List<Long> findCtesAntigosRemover(final DateTime olderThan) {

		final StringBuilder sql = new StringBuilder();
		sql.append("select distinct C.ID_CONHECIMENTO																			")
		.append("from DOCTO_SERVICO DS																							")
		.append("join CONHECIMENTO C on C.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO													")
		.append("where C.TP_SITUACAO_CONHECIMENTO = 'P'																			")
		.append("	AND ds.tp_documento_servico in ('CTE', 'NFT', 'NTE')														")
		.append("	and DH_INCLUSAO <= :olderThan															")
		.append("	and DS.DH_EMISSAO is null																					")
		.append("	AND EXISTS (																								")
		.append("		SELECT 1																								")
		.append("		FROM MONITORAMENTO_DESCARGA MD																			")
		.append("		JOIN VOLUME_NOTA_FISCAL VNF ON MD.ID_MONITORAMENTO_DESCARGA = VNF.ID_MONITORAMENTO_DESCARGA				")
		.append("		JOIN NOTA_FISCAL_CONHECIMENTO NFC ON VNF.ID_NOTA_FISCAL_CONHECIMENTO = NFC.ID_NOTA_FISCAL_CONHECIMENTO	")
		.append("		where NFC.ID_CONHECIMENTO = C.ID_CONHECIMENTO															")
		.append("			AND rownum = 1																						")
		.append("			AND MD.TP_SITUACAO_DESCARGA = 'S'																	")
		.append("		)																										")
		.append("	AND NOT EXISTS (																							")
		.append("		SELECT 1																								")
		.append("		FROM MONITORAMENTO_DOC_ELETRONICO MDE																	")
		.append("		WHERE MDE.ID_DOCTO_SERVICO = C.ID_CONHECIMENTO															")
		.append("			AND rownum = 1																						")
		.append("		)																										");

		final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("ID_CONHECIMENTO", Hibernate.LONG);
			}
		};

		final HibernateCallback hcb = new HibernateCallback() {

			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				SQLQuery query = session.createSQLQuery(sql.toString());
				csq.configQuery(query);

				query.setParameter("olderThan", olderThan.toDate());
				return query.list();
			}
		};

		return getHibernateTemplate().executeFind(hcb);
	} 

	public List findConhecimentosSemAWB(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT new map(");
		hql.append("       c.id as idConhecimento,");
		hql.append("       fo.sgFilial as filialOrigem_sgFilial,");
		hql.append("       c.nrConhecimento as nrConhecimento,");
		hql.append("       ao.sgAeroporto as aeroportoDestino_sgAeroporto,");
		hql.append("       c.tpDocumentoServico as tpDocumentoServico,");
		hql.append("       c.psReal as doctoServico_psReal,");
		hql.append("       c.psAforado as doctoServico_psAforado,");
		hql.append("       c.qtVolumes as doctoServico_qtVolumes,");
		hql.append("       c.vlMercadoria as doctoServico_vlMercadoria,");
		hql.append("	   c.dvConhecimento as dvConhecimento)");
		
		hql.append("  FROM Conhecimento c");
		hql.append("  JOIN c.localizacaoMercadoria lm");
		hql.append("  JOIN c.filialOrigem fo");
		hql.append("  JOIN c.filialLocalizacao fl");
		hql.append("  JOIN c.aeroportoByIdAeroportoDestino ao");
		hql.append("  JOIN c.servico s");
		
		Map<String, Object> paramValue = extractedConditionsDoctoSemAwb(criteria, hql);
		
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), paramValue);
		
		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}

	private Map<String, Object> extractedConditionsDoctoSemAwb(
			TypedFlatMap criteria, StringBuilder hql) {
		hql.append(" WHERE c.id NOT IN (SELECT ca.conhecimento.id");
		hql.append("                      FROM CtoAwb ca JOIN ca.awb a WHERE a.tpStatusAwb != :tpStatusAwb)");
		hql.append("   AND fl.id = :idFilialUsuarioLogado");
		hql.append("   AND s.tpModal = :tpModal ");
		
		hql.append("   AND c.tpSituacaoConhecimento = 'E' ");
		
		
		hql.append(" and not exists (select 1 from " ); 
		hql.append(EventoDocumentoServico.class.getName() + " eds ");
		hql.append(" join eds.evento e ");
		hql.append(" join eds.doctoServico d ");
		hql.append(" where ");
		hql.append(" e.cdEvento = 21 ");
		hql.append(" and eds.blEventoCancelado = 'N' ");
		hql.append(" and d.idDoctoServico = c.id )" );
		
		Long idFilialUsuarioLogado = criteria.getLong("filial.idFilial");
		
		Map<String, Object> paramValue = new HashMap<String, Object>();
		paramValue.put("tpStatusAwb", ConstantesAwb.TP_STATUS_CANCELADO);
		paramValue.put("idFilialUsuarioLogado", idFilialUsuarioLogado);
		paramValue.put("tpModal", ConstantesExpedicao.MODAL_AEREO);
		
		
		YearMonthDay dataInicial = criteria.getYearMonthDay("dataInicial");
		if(dataInicial != null) {
			hql.append("   AND trunc(cast (c.dhEmissao.value as date)) >= to_date(:dataInicial, 'yyyy-mm-dd') ");
			paramValue.put("dataInicial", dataInicial.toString());
		}
		
		YearMonthDay dataFinal = criteria.getYearMonthDay("dataFinal");
		if(dataFinal != null) {
			hql.append("   AND trunc(cast (c.dhEmissao.value as date)) <= to_date(:dataFinal, 'yyyy-mm-dd') ");
			paramValue.put("dataFinal", dataFinal.toString());
		}
		return paramValue;
	}

	public List findDimensoes(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("SELECT new map( ");
		hql.append("       NVL(vnf.nrDimensao1Sorter, vnf.nrDimensao1Cm) as nrDimensao1,");
		hql.append("       NVL(vnf.nrDimensao2Sorter, vnf.nrDimensao2Cm) as nrDimensao2,");
		hql.append("       NVL(vnf.nrDimensao3Sorter, vnf.nrDimensao3Cm) as nrDimensao3 )");
		
		hql.append("  FROM VolumeNotaFiscal vnf");
		hql.append("  JOIN vnf.notaFiscalConhecimento nfc");
		hql.append("  JOIN nfc.conhecimento c");
		
		hql.append(" WHERE (vnf.nrDimensao1Cm IS NOT NULL OR vnf.nrDimensao1Sorter IS NOT NULL) ");
		hql.append("   AND c.id = :idConhecimento ");
		
		Long idConhecimento = criteria.getLong("idConhecimento");
		
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(),
				new String[] {"idConhecimento"}, new Object[] {idConhecimento});
		
		return AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
	}

	public TypedFlatMap findTotalizadores(TypedFlatMap criteria) {
		StringBuilder hql = new StringBuilder();
		
		TypedFlatMap map = new TypedFlatMap();

		hql.append("SELECT new map(COUNT(c.idDoctoServico) as qtdDocumentosTotal,");
		hql.append("	   SUM(NVL(c.vlMercadoria, 0)) as vlMercadoriaTotal,");
		hql.append("	   SUM(NVL(c.psReal, 0)) as psRealTotal,");
		hql.append("       SUM(NVL(c.qtVolumes, 0)) as qtVolumesTotal,");
		hql.append("       SUM(NVL(c.psAforado, 0)) as psCubadoTotal)");
    	
    	hql.append("  FROM Conhecimento c");
		hql.append("  JOIN c.localizacaoMercadoria lm");
		hql.append("  JOIN c.filialOrigem fo");
		hql.append("  JOIN c.filialLocalizacao fl");
		hql.append("  JOIN c.aeroportoByIdAeroportoOrigem ao");
		hql.append("  JOIN c.servico s");
		
		Map<String, Object> param = extractedConditionsDoctoSemAwb(criteria, hql);
		
		List result = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), param);
		
		if (result != null && !result.isEmpty()){
			result = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(result);
			map = (TypedFlatMap) result.get(0);
    	}
		
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public List<Conhecimento> findConhecimentoSubstitutoByIdConhecimentoOrigem(Long idConhecimentoOrigem) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select c from Conhecimento c ");
		hql.append(" 	join fetch c.doctoServicoOriginal doctoServicoOriginal ");
		hql.append(" where doctoServicoOriginal.id = ? ");
		hql.append(" 	and c.tpDocumentoServico in ( ?, ? ) ");
		hql.append(" 	and c.tpConhecimento = ? ");
		hql.append(" 	and c.tpSituacaoConhecimento <> ? ");
		
		List<Conhecimento> result = getAdsmHibernateTemplate().find(hql.toString(), 
				new Object[] { 
			idConhecimentoOrigem,
			ConstantesExpedicao.CONHECIMENTO_NACIONAL, ConstantesExpedicao.CONHECIMENTO_ELETRONICO,
			ConstantesExpedicao.CONHECIMENTO_SUBSTITUTO, 
			ConstantesExpedicao.TP_STATUS_CANCELADO });
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<Conhecimento> findByCriteria(Map<String, Object> criteria) {
		StringBuffer sql = new StringBuffer()
			.append("select cn ")
			.append("from " + Conhecimento.class.getName() + " as cn ")
			.append(" where 1 = 1 ");
		
		List<Object> criterias = new ArrayList<Object>();
		
		if(criteria.get("nrDoctoServico") != null){
			sql.append("and cn.nrConhecimento = ? "  );
			criterias.add(criteria.get("nrDoctoServico"));
		}
		if(criteria.get("idFilial") != null){
			sql.append("and cn.filialOrigem.idFilial = ? "  );
			criterias.add(criteria.get("idFilial"));
		}
		if(criteria.get("tpDocumentoServico") != null){
			sql.append("and cn.tpDoctoServico = ? "  );
			criterias.add(criteria.get("tpDocumentoServico"));
		}
		if(criteria.get("tpSituacaoConhecimento") != null){
			sql.append("and cn.tpSituacaoConhecimento = ? "  );
			criterias.add(criteria.get("tpSituacaoConhecimento"));
		}
		if(criteria.get("tpConhecimento") != null){
			sql.append("and cn.tpConhecimento = ? "  );
			criterias.add(criteria.get("tpConhecimento"));
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), criterias.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public List<Conhecimento> findConhecimentosByControleCarga(Long idControleCarga) {

		StringBuffer sql = new StringBuffer()
		.append("select c ")
		.append(" from PreManifestoDocumento as pmd, ")
		.append(" Conhecimento as c ")
		.append(" inner join pmd.manifesto as m ")
		.append(" inner join pmd.doctoServico as ds ")
		.append(" where ")
		.append(" m.controleCarga.id = :idControleCarga ")
		.append(" and ds.id = c.id ")
		.append(" and m.tpStatusManifesto not in ('CA', 'FE') ")
		.append(" and ds.tpDocumentoServico in (:tipos)");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idControleCarga", idControleCarga);
		params.put("tipos", new String[] {"CTE","CTR"});
		
		return getAdsmHibernateTemplate().findByNamedParam(sql.toString(), params);
	}
	
	@SuppressWarnings("unchecked")
	public List<Conhecimento> findByCalculoAgendamento(YearMonthDay dtEvento) {
		
		StringBuilder sb = new StringBuilder();		
		sb.append(" from " + Conhecimento.class.getName() + " ds ");
		
		String join = " join fetch ds.agendamentoDoctoServicos ads ";
		join += " join fetch ads.agendamentoEntrega ae ";
		join += " left join fetch ds.clienteByIdClienteConsignatario cc ";
		join += " join fetch ds.clienteByIdClienteDestinatario cd ";
		sb.append(join);
		sb.append(" where ");
	
		String cdEventos = ConstantesSim.EVENTO_ENTREGA.toString() + ", " + ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM.toString() + ", " + ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM.toString();
		
		sb.append(" exists (select 1 from "
				+ EventoDocumentoServico.class.getName()
				+ " as eds inner join eds.evento as ev  where eds.doctoServico.idDoctoServico = ds.idDoctoServico and ev.cdEvento in ("
				+ cdEventos + ") and eds.blEventoCancelado != 'S' and eds.dhEvento.value >= :dtEvento ) ");
		
		sb.append(" and not exists(select 1 from "
				+ ServicoGeracaoAutomatica.class.getName()
				+ " as sga where sga.doctoServico.idDoctoServico = ds.idDoctoServico and sga.parcelaPreco.cdParcelaPreco = '" + ConstantesExpedicao.CD_AGENDAMENTO_COLETA + "' ) ");
		
		sb.append(" and ae.tpSituacaoAgendamento = 'F' ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtEvento", dtEvento);
		
		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
    }

	@SuppressWarnings("unchecked")
	public List<Conhecimento> findByCalculoDiarioPermanencia(YearMonthDay dtBloqueio, YearMonthDay dtEntrega) {
		StringBuilder sb = new StringBuilder();		
		sb.append(" from " + getPersistentClass().getName() + " doct ");
		sb.append(" inner join fetch doct.ocorrenciaDoctoServicos odoc ");
		sb.append(" inner join fetch odoc.ocorrenciaPendenciaByIdOcorBloqueio oblo ");
		sb.append(" where ");
		sb.append(" 	odoc.dhBloqueio.value >= :dtBloqueio ");
		sb.append(" and oblo.blDescontaDpe = 'S' ");
		/* Verifica se houve envio da carta de mercadoria a disposição */
		sb.append(" and exists ( ");		
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento = " + ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO + ") ");
		/* Verifica se foi entregue após a data parâmetro */
		sb.append(" and exists ( ");		
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento in (:cdEventosFinalizacao) ");
		sb.append(" 	and evdo.blEventoCancelado != 'S' ");
		sb.append(" 	and evdo.dhEvento.value >= :dtEntrega) ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da TNT" */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'N') ");
		/* Verifica se não existe um agendamento de entrega anterior a data de bloqueio */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + AgendamentoEntrega.class.getName() + " inn1 " );
		sb.append("		inner join inn1.agendamentoDoctoServicos inn2 ");
		sb.append("		inner join inn2.doctoServico inn3 ");
		sb.append(" 	where ");
		sb.append("			inn3.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and inn1.dhEnvio.value < odoc.dhBloqueio.value) ");
		/* Verifica se não existe registro finalizado em ServicoGeracaoAutomatica */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + ServicoGeracaoAutomatica.class.getName() + " inn1 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn1.parcelaPreco.cdParcelaPreco in (:cdParcelas) ");
		sb.append(" 	and inn1.blFinalizado = 'S') ");
				
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtBloqueio", dtBloqueio);
		params.put("dtEntrega", dtEntrega);
		params.put("cdEventosFinalizacao", Arrays.asList(
   				ConstantesSim.EVENTO_ENTREGA, 
   				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
   				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM ));
		params.put("cdParcelas", Arrays.asList(
				ConstantesExpedicao.CD_ARMAZENAGEM,
				ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO));
		
		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
}
	
	@SuppressWarnings("unchecked")
	public List<Conhecimento> findByCalculoMensalPermanencia(YearMonthDay dtBloqueio, YearMonthDay dtInicio) {
		StringBuilder sb = new StringBuilder();		
		sb.append(" from " + getPersistentClass().getName() + " doct ");
		sb.append(" inner join fetch doct.ocorrenciaDoctoServicos odoc ");
		sb.append(" inner join fetch odoc.ocorrenciaPendenciaByIdOcorBloqueio oblo ");
		sb.append(" where ");
		sb.append(" 	odoc.dhBloqueio.value >= :dtBloqueio ");
		sb.append(" and oblo.blDescontaDpe = 'S' ");
		/* Verifica se houve envio da carta de mercadoria a disposição */
		sb.append(" and exists ( ");
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento = " + ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO);
		sb.append(" 	and evdo.dhEvento.value >= :dtInicio) ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da TNT" */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'N') ");
		/* Verifica se não existe um agendamento de entrega anterior a data de bloqueio */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + AgendamentoEntrega.class.getName() + " inn1 " );
		sb.append("		inner join inn1.agendamentoDoctoServicos inn2 ");
		sb.append("		inner join inn2.doctoServico inn3 ");
		sb.append(" 	where ");
		sb.append("			inn3.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and inn1.dhEnvio.value < odoc.dhBloqueio.value) ");
		/* Verifica se não existe registro finalizado em ServicoGeracaoAutomatica */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + ServicoGeracaoAutomatica.class.getName() + " inn1 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn1.parcelaPreco.cdParcelaPreco in (:cdParcelas) ");
		sb.append(" 	and inn1.blFinalizado = 'S') ");
		/* Verifica se não existe evento finalizador */
		sb.append(" and not exists ( ");		
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento in (:cdEventosFinalizacao) ");
		sb.append(" 	and evdo.blEventoCancelado != 'S') ");		
		/* Verifica se a cobrança deve ser MENSAL para o cliente tomador */
		sb.append(" and exists ( ");
		sb.append(" 	from " + ServicoAdicionalCliente.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.tabelaDivisaoCliente inn2 " );
		sb.append(" 	inner join  inn1.parcelaPreco inn3 " );
		sb.append(" 	where ");
		sb.append("			inn1.tpFormaCobranca = 'M' ");
		sb.append("		and	inn2.divisaoCliente.idDivisaoCliente = doct.divisaoCliente.idDivisaoCliente ");
		sb.append("		and	inn2.tabelaPreco.idTabelaPreco = doct.tabelaPreco.idTabelaPreco ");
		sb.append("		and	inn3.cdParcelaPreco in (:cdParcelas)) ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtBloqueio", dtBloqueio);
		params.put("dtInicio", dtInicio);
		params.put("cdParcelas", Arrays.asList(
				ConstantesExpedicao.CD_ARMAZENAGEM,
				ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO));
		params.put("cdEventosFinalizacao", Arrays.asList(
   				ConstantesSim.EVENTO_ENTREGA, 
   				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
   				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM ));
				return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
    }
    
	 @SuppressWarnings("unchecked")
	public List<Conhecimento> findByCalculoFatPerdidoPermanencia(YearMonthDay dtBloqueio, YearMonthDay dtEntrega) {
		StringBuilder sb = new StringBuilder();		
		sb.append(" from " + getPersistentClass().getName() + " doct ");
		sb.append(" inner join fetch doct.ocorrenciaDoctoServicos odoc ");
		sb.append(" inner join fetch odoc.ocorrenciaPendenciaByIdOcorBloqueio oblo ");
		sb.append(" where ");
		sb.append(" 	odoc.dhBloqueio.value >= :dtBloqueio ");
		sb.append(" and oblo.blDescontaDpe = 'S' ");
		/* Verifica se não houve envio da carta de mercadoria a disposição */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento = " + ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO + ") ");
		/* Verifica se foi entregue após a data parâmetro */
		sb.append(" and exists ( ");		
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento in (:cdEventosFinalizacao) ");
		sb.append(" 	and evdo.blEventoCancelado != 'S' ");
		sb.append(" 	and evdo.dhEvento.value >= :dtEntrega) ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da TNT" */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'N') ");
		/* Verifica se não existe um agendamento de entrega anterior a data de bloqueio */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + AgendamentoEntrega.class.getName() + " inn1 " );
		sb.append("		inner join inn1.agendamentoDoctoServicos inn2 ");
		sb.append("		inner join inn2.doctoServico inn3 ");
		sb.append(" 	where ");
		sb.append("			inn3.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and inn1.dhEnvio.value < odoc.dhBloqueio.value) ");
		/* Verifica se não existe registro finalizado em ServicoGeracaoAutomatica */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + ServicoGeracaoAutomatica.class.getName() + " inn1 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn1.parcelaPreco.cdParcelaPreco in (:cdParcelas) ");
		sb.append(" 	and inn1.blFinalizado = 'S') ");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dtBloqueio", dtBloqueio);
		params.put("dtEntrega", dtEntrega);
		params.put("cdEventosFinalizacao", Arrays.asList(
   				ConstantesSim.EVENTO_ENTREGA, 
   				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
   				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM ));
		params.put("cdParcelas", Arrays.asList(
				ConstantesExpedicao.CD_ARMAZENAGEM,
				ConstantesExpedicao.CD_TAXA_FIEL_DEPOSITARIO));
		
		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
    }

    @SuppressWarnings("unchecked")
   	public List<Conhecimento> findByCalculoDiarioPermanenciaAgendamento(YearMonthDay dtEvento) {   		
   		StringBuilder sb = new StringBuilder();		
   		sb.append(" from " + Conhecimento.class.getName() + " doct ");   		
   		sb.append(" join fetch doct.agendamentoDoctoServicos agds ");
   		sb.append(" join fetch agds.agendamentoEntrega agen ");   		
   		sb.append(" where ");
   		/* Verifica se o agendamento está Fechado */
   		sb.append(" 	agen.tpSituacaoAgendamento = 'F' ");
   		/* Verifica se existe evento finalizador com data maior ou igual a data de ultima execução */
		sb.append(" and	exists ( ");		
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento in (:cdEventosFinalizacao) ");
		sb.append(" 	and evdo.blEventoCancelado != 'S' "); 
		sb.append(" 	and evdo.dhEvento.value >= :dtEvento) ");		
		/* Verifica se não existe registro finalizado em ServicoGeracaoAutomatica */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + ServicoGeracaoAutomatica.class.getName() + " inn1 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn1.parcelaPreco.cdParcelaPreco = :cdParcelaArmazenagem ");
		sb.append(" 	and inn1.blFinalizado = 'S') ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da TNT" */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'N') ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da cliente" anterior com data menor que o agendamento */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'S' ");
		sb.append("		and	inn1.dhBloqueio.value < agen.dhContato.value) ");			
		
   		Map<String, Object> params = new HashMap<String, Object>();
   		params.put("dtEvento", dtEvento);
   		params.put("cdEventosFinalizacao", Arrays.asList(
   				ConstantesSim.EVENTO_ENTREGA, 
   				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
   				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM ));
   		params.put("cdParcelaArmazenagem", ConstantesExpedicao.CD_ARMAZENAGEM);
   		
   		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
    }
    
    @SuppressWarnings("unchecked")
   	public List<Conhecimento> findByCalculoMensalPermanenciaAgendamento(YearMonthDay dtEvento) {
   		StringBuilder sb = new StringBuilder();		
   		sb.append(" from " + Conhecimento.class.getName() + " doct ");
   		sb.append(" join fetch doct.agendamentoDoctoServicos agds ");
   		sb.append(" join fetch agds.agendamentoEntrega agen ");
   		sb.append(" where ");   	   		  		   		
   		/* Considera apenas o primeiro agendamento diferente de Cancelado/Fechado */
   		sb.append(" 	agen.idAgendamentoEntrega = ( ");
   		sb.append(" 		select min(inn1.idAgendamentoEntrega) ");
   		sb.append(" 		from  " + AgendamentoEntrega.class.getName() + " inn1 ");
   		sb.append(" 		inner join inn1.agendamentoDoctoServicos inn2 ");
   		sb.append(" 		where ");
		sb.append(" 			inn2.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 		and	inn1.tpSituacaoAgendamento not in ('F','C')) ");   		
   		/* Verifica se a data do primeiro agendamento diferente de C seja maior ou igual a DT_EXEC_PERM_AG_M */
   		sb.append(" and agen.dhContato.value >= :dtEvento ");
   		/* Verifica se não existe evento finalizador */
		sb.append(" and not exists ( ");		
		sb.append(" 	from " + EventoDocumentoServico.class.getName() + " evdo " );
		sb.append(" 	inner join evdo.evento even " );
		sb.append(" 	where ");
		sb.append(" 		evdo.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append(" 	and even.cdEvento in (:cdEventosFinalizacao) ");
		sb.append(" 	and evdo.blEventoCancelado != 'S') ");		   		
		/* Verifica se não existe registro finalizado em ServicoGeracaoAutomatica */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + ServicoGeracaoAutomatica.class.getName() + " inn1 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn1.parcelaPreco.cdParcelaPreco = :cdParcelaArmazenagem ");		
		sb.append(" 	and inn1.blFinalizado = 'S') ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da cliente" anterior com data menor que o agendamento */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'S' ");
		sb.append("		and	inn1.dhBloqueio.value < agen.dhContato.value) ");
		/* Verifica se não existe ocorrência de bloqueio "culpa da TNT" */
		sb.append(" and not exists ( ");
		sb.append(" 	from " + OcorrenciaDoctoServico.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.ocorrenciaPendenciaByIdOcorBloqueio inn2 " );
		sb.append(" 	where ");
		sb.append("			inn1.doctoServico.idDoctoServico = doct.idDoctoServico ");
		sb.append("		and	inn2.blDescontaDpe = 'N') ");
		/* Verifica se a cobrança deve ser MENSAL para o cliente tomador */		
		sb.append(" and exists ( ");
		sb.append(" 	from " + ServicoAdicionalCliente.class.getName() + " inn1 " );
		sb.append(" 	inner join  inn1.tabelaDivisaoCliente inn2 " );
		sb.append(" 	inner join  inn1.parcelaPreco inn3 " );
		sb.append(" 	where ");
		sb.append("			inn1.tpFormaCobranca = 'M' ");
		sb.append("		and	inn2.divisaoCliente.idDivisaoCliente = doct.divisaoCliente.idDivisaoCliente ");
		sb.append("		and	inn2.tabelaPreco.idTabelaPreco = doct.tabelaPreco.idTabelaPreco ");
		sb.append("		and	inn3.cdParcelaPreco = :cdParcelaArmazenagem ) ");		
		
   		Map<String, Object> params = new HashMap<String, Object>();
   		params.put("dtEvento", dtEvento);
   		params.put("cdEventosFinalizacao", Arrays.asList(
   				ConstantesSim.EVENTO_ENTREGA, 
   				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
   				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM ));
   		params.put("cdParcelaArmazenagem", ConstantesExpedicao.CD_ARMAZENAGEM);
   		
   		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), params);
    }
	
	public List<Map<String, Object>> findByDecursoPrazo(Map<String, Object> parameters) {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlGeracaoPlanilhaDecursoPrazo(parameters));
		
		YearMonthDay dtVencimento = (YearMonthDay) parameters.get("dtVencimento");
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				/*00*/sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
				/*01*/sqlQuery.addScalar("tpDoctoServico", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_TIPO_DOCUMENTO_SERVICO"}));
				/*02*/sqlQuery.addScalar("sgFilialDoctoServico", Hibernate.STRING);
				/*03*/sqlQuery.addScalar("nrDoctoServico", Hibernate.STRING);
				/*04*/sqlQuery.addScalar("sgFilialDestino", Hibernate.STRING);	
				/*05*/sqlQuery.addScalar("tpFrete", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_TIPO_FRETE"}));
				/*06*/sqlQuery.addScalar("identificacaoRemetente", Hibernate.STRING);
				/*07*/sqlQuery.addScalar("nomeRemetente", Hibernate.STRING);
				/*08*/sqlQuery.addScalar("identificacaoTomador", Hibernate.STRING);
				/*09*/sqlQuery.addScalar("nomeTomador", Hibernate.STRING);
				/*10*/sqlQuery.addScalar("tpClienteTomador", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_TIPO_CLIENTE"}));
				/*11*/sqlQuery.addScalar("sgFilialCobranca", Hibernate.STRING);
				/*12*/sqlQuery.addScalar("sgRegionalExecucao", Hibernate.STRING);
				/*13*/sqlQuery.addScalar("identificacaoDestinatario", Hibernate.STRING);
				/*14*/sqlQuery.addScalar("nomeDestinatario", Hibernate.STRING);
				/*15*/sqlQuery.addScalar("municipio", Hibernate.STRING);
				/*16*/sqlQuery.addScalar("tpModal", Hibernate.custom(
						DomainCompositeUserType.class, new String[]{"domainName"}, new String[]{"DM_MODAL"}));
				/*17*/sqlQuery.addScalar("dhEmissao",Hibernate.custom(JodaTimeDateTimeUserType.class));
				/*18*/sqlQuery.addScalar("dhEmissaoTZR", Hibernate.STRING);
	
				/*19*/sqlQuery.addScalar("NF",Hibernate.INTEGER);
				/*20*/sqlQuery.addScalar("vlMercadoria", Hibernate.BIG_DECIMAL);
				/*21*/sqlQuery.addScalar("psCalculo", Hibernate.BIG_DECIMAL);
				/*22*/sqlQuery.addScalar("psReal", Hibernate.BIG_DECIMAL);
				/*23*/sqlQuery.addScalar("psAforado", Hibernate.BIG_DECIMAL);
				/*24*/sqlQuery.addScalar("psAferido", Hibernate.BIG_DECIMAL);
				/*25*/sqlQuery.addScalar("qtVolumes", Hibernate.INTEGER);
				/*26*/sqlQuery.addScalar("vlTotal", Hibernate.BIG_DECIMAL);
				/*27*/sqlQuery.addScalar("dtPrevisaoEntrega", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
				/*28*/sqlQuery.addScalar("decursoPrazo", Hibernate.INTEGER);
				/*29*/sqlQuery.addScalar("localizacaoMercadoria", Hibernate.STRING);
				/*30*/sqlQuery.addScalar("dsOcorrencia", Hibernate.STRING);
		
				/*31*/sqlQuery.addScalar("dhBloqueio",Hibernate.custom(JodaTimeDateTimeUserType.class));
				/*32*/sqlQuery.addScalar("dhBloqueioTZR", Hibernate.STRING);
				/*33*/sqlQuery.addScalar("dhEvento",Hibernate.custom(JodaTimeDateTimeUserType.class));
				/*34*/sqlQuery.addScalar("dhEventoTZR", Hibernate.STRING);
				/*35*/sqlQuery.addScalar("dhContato",Hibernate.custom(JodaTimeDateTimeUserType.class));
				/*36*/sqlQuery.addScalar("dhContatoTZR", Hibernate.STRING);
				/*37*/sqlQuery.addScalar("dtAgendamento", Hibernate.custom(JodaTimeYearMonthDayUserType.class));		
			}
		};
		
		List<Object[]> tuples = getAdsmHibernateTemplate().findBySql(sql.toString(), parameters, csq);
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		for(Object[] tuple : tuples) {
			Map<String, Object> item = new HashMap<String, Object>();
								
			DomainValue tpDoctoServico = (DomainValue)tuple[1];
			String nrDoctoServico = FormatUtils.formataNrDocumento((String)tuple[3], tpDoctoServico.getValue());
			item.put("documentoServico", tpDoctoServico.getDescriptionAsString()+" "+tuple[2]+" - "+nrDoctoServico);
			item.put("destino", tuple[4]);
			DomainValue tipoFrete = (DomainValue)tuple[5];
			item.put("tipoFrete", tipoFrete.getDescriptionAsString());
			item.put("idenficacaoRemetente", tuple[6]);
			item.put("remetente", tuple[7]);
			item.put("identificacaoTomador", tuple[8]);
			item.put("tomador", tuple[9]);
			DomainValue tipoCliente = (DomainValue)tuple[10];
			item.put("tipoCliente", tipoCliente.getDescriptionAsString());
			item.put("filialCobranca", tuple[11]);
			item.put("regional", tuple[12]);
			item.put("identificacaoDestinatario", tuple[13]);
			item.put("destinatario", tuple[14]);
			item.put("municipioEntrega", tuple[15]);
			DomainValue modal = (DomainValue)tuple[16];
			item.put("modal", modal.getDescriptionAsString());
			if (tuple[17] != null) {
				DateTime emissao = (DateTime)tuple[17];
				item.put("emissao", emissao.toString("dd/MM/yyyy"));
			}
			
			item.put("notasFiscais", tuple[19]);
			item.put("valorMercadoria", tuple[20]);
			item.put("pesoFaturado", tuple[21]);
			item.put("pesoDeclarado", tuple[22]);
			item.put("pesoCubado", tuple[23]);
			item.put("pesoAferido", tuple[24]);
			item.put("quantidade", tuple[25]);
			item.put("valorFrete", tuple[26]);
			if (tuple[27] != null) {
				YearMonthDay otd = (YearMonthDay)tuple[27];
				item.put("otd", otd.toString("dd/MM/yyyy"));
			}
			item.put("decursoPrazo", tuple[28]);
			item.put("ocorrencia", tuple[29]);
			item.put("localizacao", tuple[30]);
		
			DateTime dataBloqueio = (DateTime)tuple[31];
			item.put("dataBloqueio", dataBloqueio.toString("dd/MM/yyyy"));
		
			if (tuple[33] != null) {
				DateTime data = (DateTime)tuple[33];
				item.put("dataEmissaoCMD", data.toString("dd/MM/yyyy"));
			}
			YearMonthDay dtBloqueio = new YearMonthDay(dataBloqueio.getYear(), dataBloqueio.getMonthOfYear(), dataBloqueio.getDayOfMonth());
			item.put("diasBloqueio", Days.daysBetween(dtBloqueio, dtVencimento).getDays());
			if (tuple[35] != null) {
				DateTime data = (DateTime)tuple[35];
				item.put("dataBloqueioAgendamento", data.toString("dd/MM/yyyy"));
			}
			if (tuple[37] != null) {
				YearMonthDay data = (YearMonthDay)tuple[37];
				item.put("dataAgendamento", data.toString("dd/MM/yyyy"));
			}			
			
			result.add(item);
		}
		
		return result;
	}

	private String getSqlGeracaoPlanilhaDecursoPrazo(Map<String, Object> parameters) {
		StringBuilder sql = new StringBuilder();
		
		/* QUERY QUE RETORNA OS ITENS DE ORDEM DE SERVIÇO */
		sql.append("SELECT ");
		sql.append("    ds.ID_DOCTO_SERVICO as idDoctoServico,  ");
		sql.append("    DS.TP_DOCUMENTO_SERVICO as tpDoctoServico, ");
		sql.append("    fod.sg_filial as sgFilialDoctoServico, ");
		sql.append("    DS.NR_DOCTO_SERVICO as nrDoctoServico, ");
		sql.append("    FDD.sg_filial as sgFilialDestino, ");
		sql.append("    con.tp_frete as tpFrete, ");
		sql.append("    pre.nr_identificacao as identificacaoRemetente, ");
		sql.append("    pre.nm_pessoa as nomeRemetente, ");
		sql.append("    pto.nr_identificacao as identificacaoTomador , ");
		sql.append("    pto.nm_pessoa as nomeTomador, ");
		sql.append("    cto.tp_cliente as tpClienteTomador, ");
		sql.append("    fco.sg_filial as  sgFilialCobranca, ");
		sql.append("" + getSubqueryRegionalPlanilhaServicosAdicionais("REGI.SG_REGIONAL", "fco.id_filial") + " AS sgRegionalExecucao, ");
		sql.append("    pde.nr_identificacao as identificacaoDestinatario, ");
		sql.append("    pde.nm_pessoa as nomeDestinatario, ");
		sql.append("    mue.nm_municipio as municipio, ");
		sql.append("    ser.tp_modal as tpModal, ");
		sql.append("    ds.DH_EMISSAO as dhEmissao, ");
		sql.append("    ds.DH_EMISSAO_TZR as dhEmissaoTZR, ");
		sql.append("    ( ");
		sql.append("        SELECT MAX(NR_NOTA_FISCAL) ");
		sql.append("        FROM NOTA_FISCAL_CONHECIMENTO NOTA ");
		sql.append("        WHERE NOTA.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ");
		sql.append("    ) as NF, ");
		sql.append("    ds.VL_MERCADORIA as vlMercadoria, ");
		sql.append("    ds.PS_REFERENCIA_CALCULO as psCalculo, ");
		sql.append("    ds.PS_REAL as psReal, ");
		sql.append("    ds.PS_AFORADO as psAforado, ");
		sql.append("    ds.PS_AFERIDO as psAferido, ");
		sql.append("    ds.QT_VOLUMES as qtVolumes, ");
		sql.append("    ds.VL_TOTAL_DOC_SERVICO as vlTotal, ");
		sql.append("    ds.DT_PREV_ENTREGA as dtPrevisaoEntrega, ");
		sql.append("    NVL(SUBQ.NR_DECURSO_PRAZO,:decPrazoParam) as decursoPrazo, ");
		sql.append("    VI18N(lom.DS_LOCALIZACAO_MERCADORIA_I) AS localizacaoMercadoria, ");
		sql.append("    VI18N(ocpe.DS_OCORRENCIA_I) AS dsOcorrencia, ");
		sql.append("    odse.DH_BLOQUEIO as dhBloqueio, ");
		sql.append("    odse.DH_BLOQUEIO_TZR as dhBloqueioTZR, ");
		sql.append("    evds.DH_EVENTO as dhEvento, ");
		sql.append("    evds.DH_EVENTO_TZR as dhEventoTZR, ");
		sql.append("    SUBQ2.DH_CONTATO as dhContato, ");
		sql.append("    SUBQ2.DH_CONTATO_TZR as dhContatoTZR, ");
		sql.append("    SUBQ2.DT_AGENDAMENTO as dtAgendamento ");

		sql.append("FROM  ");
		sql.append("    DOCTO_SERVICO DS, ");
		sql.append("    ( ");
		sql.append("      SELECT ");
		sql.append("          SACL.NR_DECURSO_PRAZO, TADC.ID_DIVISAO_CLIENTE, TADC.ID_SERVICO ");
		sql.append("      FROM  ");
		sql.append("          SERVICO_ADICIONAL_CLIENTE SACL,  ");
		sql.append("          PARCELA_PRECO PARC,  ");
		sql.append("          TABELA_DIVISAO_CLIENTE TADC ");
		sql.append("      WHERE SACL.ID_PARCELA_PRECO = PARC.ID_PARCELA_PRECO ");
		sql.append("      AND SACL.ID_TABELA_DIVISAO_CLIENTE = TADC.ID_TABELA_DIVISAO_CLIENTE ");
		sql.append("      AND PARC.CD_PARCELA_PRECO = :cdParcelaPreco ");
		sql.append("    ) SUBQ, ");
		sql.append("    ( ");
		sql.append("        SELECT ");
		sql.append("            AGEN.DH_CONTATO, ");
		sql.append("            AGEN.DH_CONTATO_TZR, ");
		sql.append("            AGEN.DT_AGENDAMENTO, ");
		sql.append("            AGDS.ID_DOCTO_SERVICO ");
		sql.append("        FROM  ");
		sql.append("            AGENDAMENTO_ENTREGA AGEN, ");
		sql.append("            AGENDAMENTO_DOCTO_SERVICO AGDS ");
		sql.append("        WHERE  ");
		sql.append("            AGEN.ID_AGENDAMENTO_ENTREGA = AGDS.ID_AGENDAMENTO_ENTREGA ");
		sql.append("        AND AGEN.TP_SITUACAO_AGENDAMENTO = 'F' ");
		sql.append("    ) SUBQ2, ");
		sql.append("    FILIAL FOD, ");
		sql.append("    FILIAL FDD, ");
		sql.append("    CONHECIMENTO CON, ");
		sql.append("    PESSOA PRE, ");
		sql.append("    devedor_doc_serv dds, ");
		sql.append("    PESSOA PTO, ");
		sql.append("    cliente cto, ");
		sql.append("    FILIAL FCO, ");
		sql.append("    PESSOA pde, ");
		sql.append("    municipio mue, ");
		sql.append("    servico ser, ");
		sql.append("    LOCALIZACAO_MERCADORIA lom, ");
		sql.append("    OCORRENCIA_DOCTO_SERVICO odse, ");
		sql.append("    OCORRENCIA_PENDENCIA ocpe, ");
		sql.append("    EVENTO_DOCUMENTO_SERVICO evds ");
		sql.append("WHERE   ");
		 
		sql.append("ds.ID_SERVICO = SUBQ.ID_SERVICO(+) ");
		sql.append("AND ds.ID_DIVISAO_CLIENTE = SUBQ.ID_DIVISAO_CLIENTE(+) ");
		sql.append("AND ds.ID_DOCTO_SERVICO = SUBQ2.ID_DOCTO_SERVICO(+) ");
		sql.append("AND odse.id_OCORRENCIA_DOCTO_SERVICO = ( ");
		sql.append("                  select min(odsa.id_OCORRENCIA_DOCTO_SERVICO)  ");
		sql.append("                  from OCORRENCIA_DOCTO_SERVICO odsa  ");
		sql.append("                  where odsa.id_docto_servico = ds.id_docto_servico) ");
		sql.append("AND odse.ID_OCOR_BLOQUEIO = ocpe.ID_OCORRENCIA_PENDENCIA ");
		sql.append("and evds.ID_EVENTO_DOCUMENTO_SERVICO = ( ");
		sql.append("                  select min(edva.ID_EVENTO_DOCUMENTO_SERVICO)  ");
		sql.append("                  from EVENTO_DOCUMENTO_SERVICO edva, EVENTO evea  ");
		sql.append("                  where edva.id_evento =  evea.id_evento  ");
		sql.append("                  and evea.cd_evento = :cdEventoCarta  ");
		sql.append("                  and edva.id_docto_servico = ds.ID_DOCTO_SERVICO) ");
		sql.append("and FOD.id_filial = ds.id_filial_origem ");
		sql.append("and FDD.id_filial = ds.id_filial_destino ");
		sql.append("and con.id_conhecimento = DS.ID_DOCTO_SERVICO ");
		sql.append("and pre.id_pessoa = ds.id_cliente_remetente ");
		sql.append("and dds.id_docto_servico = ds.id_docto_servico ");
		sql.append("and pto.id_pessoa = dds.id_cliente ");
		sql.append("and cto.id_cliente = dds.id_cliente ");
		sql.append("and cto.id_filial_cobranca = fco.id_filial ");
		sql.append("and pde.id_pessoa = ds.id_cliente_destinatario ");
		sql.append("and con.id_municipio_entrega = mue.id_municipio ");
		sql.append("and ser.id_servico = ds.id_servico ");
		sql.append("and lom.id_localizacao_mercadoria = ds.id_localizacao_mercadoria ");

		sql.append("AND EXISTS ( ");
		sql.append("    SELECT 1 ");
		sql.append("    FROM  ");
		sql.append("        OCORRENCIA_DOCTO_SERVICO OCDS ");
		sql.append("    WHERE ");
		sql.append("        OCDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sql.append("    AND TRUNC(OCDS.DH_BLOQUEIO) > :dhBloqueio)  ");
		
		sql.append("AND NOT EXISTS ( ");
		sql.append("    SELECT 1  ");
		sql.append("    FROM  ");
		sql.append("        OCORRENCIA_DOCTO_SERVICO OCDS, ");
		sql.append("        OCORRENCIA_PENDENCIA OCPE ");
		sql.append("    WHERE  ");
		sql.append("          OCDS.ID_OCOR_BLOQUEIO = OCPE.ID_OCORRENCIA_PENDENCIA ");
		sql.append("    AND   OCDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sql.append("    AND   OCPE.BL_APREENSAO = 'S') ");
		
		sql.append("AND EXISTS ( ");
		sql.append("    SELECT 1  ");
		sql.append("    FROM  ");
		sql.append("        OCORRENCIA_DOCTO_SERVICO OCDS, ");
		sql.append("        OCORRENCIA_PENDENCIA OCPE ");
		sql.append("    WHERE  ");
		sql.append("          OCDS.ID_OCOR_BLOQUEIO = OCPE.ID_OCORRENCIA_PENDENCIA ");
		sql.append("    AND   OCDS.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sql.append("    AND   OCPE.BL_DESCONTA_DPE = 'S') ");
		
		sql.append("AND NOT EXISTS ( ");
		sql.append("    SELECT  1 ");
		sql.append("    FROM  ");
		sql.append("        EVENTO_DOCUMENTO_SERVICO EVDO, ");
		sql.append("        EVENTO EVEN ");
		sql.append("    WHERE  ");
		sql.append("        EVDO.ID_EVENTO = EVEN.ID_EVENTO ");
		sql.append("    AND EVEN.CD_EVENTO IN (:cdEventosFinalizacao) ");
		sql.append("    AND EVDO.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sql.append("    AND EVDO.BL_EVENTO_CANCELADO <> 'S' ");
		sql.append(") ");
		 
		 
		if (parameters.get("idRegional") != null) {
			 sql.append("AND " + 		
						getSubqueryRegionalPlanilhaServicosAdicionais("REGI.ID_REGIONAL", "fco.id_filial") + 
						" = :idRegional ");
		}
			
		if (parameters.get("idFiliais") != null) {
			sql.append(" and fco.id_filial in (:idFiliais) ");
		}
		
		if (parameters.get("idCliente") != null) {
			sql.append(" and cto.id_cliente = :idCliente ");
		}
			
		if (parameters.get("dtVencimento") != null) {
			sql.append("    AND DS.DT_PREV_ENTREGA+NVL(SUBQ.NR_DECURSO_PRAZO,:decPrazoParam) = :dtVencimento ");
		}
		
		parameters.put("cdEventoCarta", ConstantesSim.EVENTO_CARTA_MERCADORIA_DISPOSICAO);
		parameters.put("cdParcelaPreco", ConstantesExpedicao.CD_ARMAZENAGEM);
		parameters.put("cdEventosFinalizacao", Arrays.asList(
   				ConstantesSim.EVENTO_ENTREGA,
   				ConstantesSim.EVENTO_LIBERACAO_MERCADORIA_DEVOLVIDA_REM,
   				ConstantesSim.EVENTO_MERCADORIA_DEVOLVIDA_REM ));

		return sql.toString();
	}

	private String getSubqueryRegionalPlanilhaServicosAdicionais(String selectField, String idFilialField) {
		StringBuilder sql = new StringBuilder();
		sql.append("(");
		sql.append("	SELECT ");
		sql.append("" + 	selectField);
		sql.append("	FROM ");
		sql.append("    	REGIONAL_FILIAL REFI, ");
		sql.append("    	REGIONAL REGI ");
		sql.append("	WHERE ");
		sql.append("	" + idFilialField + " = REFI.ID_FILIAL ");
		sql.append("    AND REGI.ID_REGIONAL = REFI.ID_REGIONAL ");
		sql.append("    AND REFI.DT_VIGENCIA_INICIAL <= SYSDATE ");		
		sql.append("	AND (REFI.DT_VIGENCIA_FINAL >= SYSDATE OR REFI.DT_VIGENCIA_FINAL IS NULL) ");		
		sql.append(")");
		return sql.toString();
	}
		
    public Map<String, Object> findConhecimentoAberbacao(Long idConehcimento ){
		
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("idConhecimento", idConehcimento);
		
    	 ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {    		 
				@Override
				public void configQuery(SQLQuery sqlQuery) {	
					
					sqlQuery.addScalar("sgFilialOrigem",Hibernate.STRING);
					sqlQuery.addScalar("emitNome",Hibernate.STRING);
					sqlQuery.addScalar("emitCnpj",Hibernate.STRING);
					sqlQuery.addScalar("emitCidade",Hibernate.STRING);
					sqlQuery.addScalar("emitUf",Hibernate.STRING);
					sqlQuery.addScalar("tipoDocumento",Hibernate.STRING);
					sqlQuery.addScalar("nrDocumento",Hibernate.STRING);
					sqlQuery.addScalar("dhEmissao",Hibernate.custom(JodaTimeDateTimeUserType.class));
					sqlQuery.addScalar("tipoEmbarque",Hibernate.STRING);
					sqlQuery.addScalar("tipoAbrangencia",Hibernate.STRING);
					sqlQuery.addScalar("tipoMercadoria",Hibernate.STRING);
					sqlQuery.addScalar("valorMercadoria",Hibernate.STRING);
					sqlQuery.addScalar("remeNome",Hibernate.STRING);
					sqlQuery.addScalar("remeCnpj",Hibernate.STRING);
					sqlQuery.addScalar("remeCidade",Hibernate.STRING);
					sqlQuery.addScalar("remeUf",Hibernate.STRING);
					sqlQuery.addScalar("destNome",Hibernate.STRING);
					sqlQuery.addScalar("destCnpj",Hibernate.STRING);
					sqlQuery.addScalar("destCidade",Hibernate.STRING);
					sqlQuery.addScalar("destUf",Hibernate.STRING);
					sqlQuery.addScalar("consNome",Hibernate.STRING);
					sqlQuery.addScalar("consCnpj",Hibernate.STRING);
					sqlQuery.addScalar("consCidade",Hibernate.STRING);
					sqlQuery.addScalar("consUf",Hibernate.STRING);
					sqlQuery.addScalar("blOperacaoSpitFire",Hibernate.STRING);
				}
			};
			
		List<Map<String,Object>> list = getAdsmHibernateTemplate().findBySqlToMappedResult(montaSqlConhecimentoAberbacao(), params, configureSqlQuery);
		if( CollectionUtils.isNotEmpty(list) ){
			return list.get(0);
		}
        return null; 
    }
    
    private String montaSqlConhecimentoAberbacao() {
        StringBuilder sql = new StringBuilder();
		sql.append(" SELECT EMIT.NM_PESSOA                                      AS emitNome, ")
		.append("   EMIT.NR_IDENTIFICACAO                                       AS emitCnpj, ")
		.append("   MUNEMIT.NM_MUNICIPIO                                        AS emitCidade, ")
		.append("   UFEMIT.SG_UNIDADE_FEDERATIVA                                AS emitUf, ")
		.append("   DS.TP_DOCUMENTO_SERVICO                                     AS tipoDocumento, ")
		.append("   DS.NR_DOCTO_SERVICO                                         AS nrDocumento, ")
		.append("   DS.DH_EMISSAO                                               AS dhEmissao, ")
		.append("   S.TP_MODAL                                                  AS tipoEmbarque, ")
		.append("   S.TP_ABRANGENCIA                                            AS tipoAbrangencia, ")
		.append("   VI18N(NP.DS_NATUREZA_PRODUTO_I)                             AS tipoMercadoria, ")
		.append("   TRIM(TO_CHAR(DS.VL_MERCADORIA, '9G999G999G999G999G990D00')) AS valorMercadoria, ")
		.append("   REME.NM_PESSOA                                              AS remeNome, ")
		.append("   REME.NR_IDENTIFICACAO                                       AS remeCnpj, ")
		.append("   MUNREME.NM_MUNICIPIO                                        AS remeCidade, ")
		.append("   UFREME.SG_UNIDADE_FEDERATIVA                                AS remeUf, ")
		.append("   DEST.NM_PESSOA                                              AS destNome, ")
		.append("   DEST.NR_IDENTIFICACAO                                       AS destCnpj, ")
		.append("   MUNDEST.NM_MUNICIPIO                                        AS destCidade, ")
		.append("   UFDEST.SG_UNIDADE_FEDERATIVA                                AS destUf, ")
		.append("   CONS.NM_PESSOA                                              AS consNome, ")
		.append("   CONS.NR_IDENTIFICACAO                                       AS consCnpj, ")
		.append("   MUNCONS.NM_MUNICIPIO                                        AS consCidade, ")
		.append("   UFCONS.SG_UNIDADE_FEDERATIVA                                AS consUf, ")
		.append("   FO.SG_FILIAL                                                AS sgFilialOrigem, ")
		.append("   CONH.BL_SPITFIRE                                            AS blOperacaoSpitFire ")
		
		.append(" FROM DOCTO_SERVICO DS, ")
		.append("   CONHECIMENTO CONH, ")
		.append("   FILIAL FO, ")
		.append("   SERVICO S, ")
		.append("   NATUREZA_PRODUTO NP, ")
		//		EMISSOR 
		.append("   PESSOA EMIT, ")
		.append("   ENDERECO_PESSOA ENDEMIT, ")
		.append("   MUNICIPIO MUNEMIT, ")
		.append("   UNIDADE_FEDERATIVA UFEMIT, ")
		//		REMETENTE 
		.append("   PESSOA REME, ")
		.append("   ENDERECO_PESSOA ENDREME, ")
		.append("   MUNICIPIO MUNREME, ")
		.append("   UNIDADE_FEDERATIVA UFREME, ")
		//		DESTINATARIO
		.append("   PESSOA DEST, ")
		.append("   ENDERECO_PESSOA ENDDEST, ")
		.append("   MUNICIPIO MUNDEST, ")
		.append("   UNIDADE_FEDERATIVA UFDEST, ")
		.append("   PAIS PAISDEST, ")
		//		CONSIGNATARIO
		.append("   PESSOA CONS, ")
		.append("   ENDERECO_PESSOA ENDCONS, ")
		.append("   MUNICIPIO MUNCONS, ")
		.append("   UNIDADE_FEDERATIVA UFCONS ")
		.append(" WHERE DS.ID_DOCTO_SERVICO    = CONH.ID_CONHECIMENTO ")
		.append(" AND DS.ID_SERVICO            = S.ID_SERVICO ")
		.append(" AND CONH.ID_NATUREZA_PRODUTO = NP.ID_NATUREZA_PRODUTO ")
		.append(" AND DS.ID_FILIAL_ORIGEM           = FO.ID_FILIAL ")
		//		EMISSOR
		.append(" AND DS.ID_FILIAL_ORIGEM           = EMIT.ID_PESSOA ")
		.append(" AND EMIT.ID_ENDERECO_PESSOA       = ENDEMIT.ID_ENDERECO_PESSOA ")
		.append(" AND ENDEMIT.ID_MUNICIPIO          = MUNEMIT.ID_MUNICIPIO ")
		.append(" AND MUNEMIT.ID_UNIDADE_FEDERATIVA = UFEMIT.ID_UNIDADE_FEDERATIVA ")
		//			REMETENTE 
		.append(" AND DS.ID_CLIENTE_REMETENTE       = REME.ID_PESSOA(+) ")
		.append(" AND REME.ID_ENDERECO_PESSOA       = ENDREME.ID_ENDERECO_PESSOA(+) ")
		.append(" AND ENDREME.ID_MUNICIPIO          = MUNREME.ID_MUNICIPIO(+) ")
		.append(" AND MUNREME.ID_UNIDADE_FEDERATIVA = UFREME.ID_UNIDADE_FEDERATIVA(+) ")
		//		DESTINATARIO 
		.append(" AND DS.ID_CLIENTE_DESTINATARIO    = DEST.ID_PESSOA(+) ")
		.append(" AND DEST.ID_ENDERECO_PESSOA       = ENDDEST.ID_ENDERECO_PESSOA(+) ")
		.append(" AND ENDDEST.ID_MUNICIPIO          = MUNDEST.ID_MUNICIPIO(+) ")
		.append(" AND MUNDEST.ID_UNIDADE_FEDERATIVA = UFDEST.ID_UNIDADE_FEDERATIVA(+) ")
		.append(" AND UFDEST.ID_PAIS                = PAISDEST.ID_PAIS(+) ")
		//		DESTINATARIO
		.append(" AND DS.ID_CLIENTE_CONSIGNATARIO   = CONS.ID_PESSOA(+) ")
		.append(" AND CONS.ID_ENDERECO_PESSOA       = ENDCONS.ID_ENDERECO_PESSOA(+) ")
		.append(" AND ENDCONS.ID_MUNICIPIO          = MUNCONS.ID_MUNICIPIO(+) ")
		.append(" AND MUNCONS.ID_UNIDADE_FEDERATIVA = UFCONS.ID_UNIDADE_FEDERATIVA(+) ")
		.append(" AND CONH.ID_CONHECIMENTO          = :idConhecimento  ");
        
        return sql.toString();
    }

	public List<Conhecimento> findByIdPedidoColeta(Long idPedidoColeta) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT c ")
		.append(" FROM Conhecimento as c ")
		.append(" JOIN FETCH c.pedidoColeta as pc ")
		.append(" LEFT OUTER JOIN FETCH c.localizacaoMercadoria as lm ")
		.append(" LEFT OUTER JOIN FETCH c.servico as s ")
		.append(" WHERE ")
		.append("		pc.idPedidoColeta = :idPedidoColeta")
		.append(" AND	pc.tpPedidoColeta = 'DA'");

		Map m = new HashMap();
		m.put("idPedidoColeta", idPedidoColeta);

		return getAdsmHibernateTemplate().findByNamedParam(sb.toString(), m);
	}
	
	public List<Map<String,Object>> findPendentesPorCalculo( Long idMonitoramento ) {
		StringBuilder sb = new StringBuilder();
		sb.append(" WITH dados AS ") 
		.append("  (SELECT  ")
		.append(" ID_CONHECIMENTO, ")
		.append(" ID_MONITORAMENTO_DESCARGA, ")
		.append(" nr_conhecimento, ")
		.append(" qtd_volumes, ")
		.append(" qtd_pendente, ")
		.append(" qtd_volumes - qtd_pendente AS diff ")
		.append(" FROM ")
		.append(" (SELECT C.ID_CONHECIMENTO, ")
		.append(" V.ID_MONITORAMENTO_DESCARGA, ")
		.append(" v.nr_conhecimento, ")
		.append(" COUNT(*) AS qtd_volumes, ")
		.append(" (SELECT COUNT(*) ")
		.append(" FROM VOLUME_NOTA_FISCAL VNF, ")
		.append(" nota_fiscal_conhecimento nfc ")
		.append(" WHERE nfc.id_nota_fiscal_conhecimento = vnf.id_nota_fiscal_conhecimento ")
		.append(" AND VNF.PS_AFERIDO IS NOT NULL ")
		.append(" AND VNF.TP_VOLUME  IN ('M', 'U') ")
		.append(" AND nfc.id_conhecimento = c.id_conhecimento ) AS qtd_pendente ")
		.append(" FROM VOLUME_NOTA_FISCAL V ")
		.append(" INNER JOIN NOTA_FISCAL_CONHECIMENTO N ")
		.append(" ON N.ID_NOTA_FISCAL_CONHECIMENTO = V.ID_NOTA_FISCAL_CONHECIMENTO ")
		.append(" INNER JOIN CONHECIMENTO C ON C.ID_CONHECIMENTO = N.ID_CONHECIMENTO ")
		.append(" WHERE bl_peso_aferido            = 'N' ")
		.append(" AND c.tp_situacao_conhecimento   = 'P' ")
		.append(" AND v.ID_MONITORAMENTO_DESCARGA IN ")
		.append(" (SELECT ID_MONITORAMENTO_DESCARGA ")
		.append(" FROM MONITORAMENTO_DESCARGA MD2 ")
		.append(" INNER JOIN FILIAL F2 ON F2.ID_FILIAL                 = MD2.ID_FILIAL ")
		.append(" WHERE MD2.TP_SITUACAO_DESCARGA != 'S' ");
		if( idMonitoramento == null){
			sb.append(" AND MD2.DH_INICIO_DESCARGA      > sysdate - 5 ");
		}else{
			sb.append(" and MD2.id_monitoramento_descarga = :idMonitoramento ) ");
		}
		sb.append(" GROUP BY C.id_conhecimento,v.ID_MONITORAMENTO_DESCARGA,v.nr_conhecimento )) ")
		.append(" SELECT id_conhecimento as idConhecimento, nr_conhecimento as nrConhecimento FROM dados d WHERE diff = 0 ");
		
		Map<String,Object> m = new HashMap<String,Object>();
		if( idMonitoramento != null ){
			m.put("idMonitoramento", idMonitoramento);
		}

    	 ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {    		 
				@Override
				public void configQuery(SQLQuery sqlQuery) {	
					sqlQuery.addScalar("idConhecimento",Hibernate.LONG);
					sqlQuery.addScalar("nrConhecimento",Hibernate.LONG);
				}
			};
			
		List<Map<String,Object>> list = getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), m, configureSqlQuery);
		if( CollectionUtils.isNotEmpty(list) ){
			return list;
		}
		return null;
	}


	public Map<String, Object> findDadosLocalizacaoDoctoServico(Long idDoctoServico) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("select  ");
		sb.append("	pessoaorigem.id_pessoa as ID_FILIAL_ORIGEM, ");
		sb.append(" filialorigem.sg_filial as SG_FILIAL_ORIGEM, ");
		sb.append(" pessoaorigem.nm_fantasia as NM_FILIAL_ORIGEM, ");
		sb.append(" enderecopessoaorigem.nr_latitude_tmp as LATITUDE_ORIGEM, ");
		sb.append(" enderecopessoaorigem.nr_longitude_tmp as LONGITUDE_ORIGEM, ");
		sb.append(" pessoadestino.id_pessoa as ID_FILIAL_DESTINO, ");
		sb.append(" filialdestino.sg_filial as SG_FILIAL_DESTINO, ");
		sb.append(" pessoadestino.nm_fantasia as NM_FILIAL_DESTINO, ");
		sb.append(" enderecopessoadestino.nr_latitude_tmp as LATITUDE_DESTINO, ");
		sb.append(" enderecopessoadestino.nr_longitude_tmp as LONGITUDE_DESTINO, ");
		sb.append(" pessoaatual.ID_PESSOA AS ID_FILIAL_ATUAL, ");
		sb.append(" filialatual.SG_FILIAL AS SG_FILIAL_ATUAL, ");
		sb.append(" pessoaatual.NM_FANTASIA AS NM_FANTASIA_ATUAL, ");
		sb.append(" enderecopessoatual.NR_LATITUDE_TMP AS LATITUDE_ATUAL, ");
		sb.append(" enderecopessoatual.NR_LONGITUDE_TMP AS LONGITUDE_ATUAL ");
		sb.append(" from  ");
		sb.append(" docto_servico doctoservico ");

		sb.append(" join filial filialatual on filialatual.id_filial = doctoservico.ID_FILIAL_lOCALIZACAO  ");
		sb.append(" join pessoa pessoaatual on pessoaatual.id_pessoa = filialatual.id_filial ");
		sb.append(" join endereco_pessoa enderecopessoatual on enderecopessoatual.id_endereco_pessoa = pessoaatual.id_endereco_pessoa ");

		sb.append(" join pessoa pessoaorigem on pessoaorigem.id_pessoa = doctoservico.ID_FILIAL_ORIGEM ");
		sb.append(" join endereco_pessoa enderecopessoaorigem on enderecopessoaorigem.id_endereco_pessoa = pessoaorigem.id_endereco_pessoa ");
		sb.append(" join filial filialorigem on filialorigem.id_filial = pessoaorigem.id_pessoa ");
		
		sb.append(" join pessoa pessoadestino on pessoadestino.id_pessoa = doctoservico.ID_FILIAL_DESTINO ");
		sb.append(" join endereco_pessoa enderecopessoadestino on enderecopessoadestino.id_endereco_pessoa = pessoadestino.id_endereco_pessoa ");
		sb.append(" join filial filialdestino on filialdestino.id_filial = pessoadestino.id_pessoa ");
		sb.append(" where doctoservico.id_docto_servico = :idDoctoServico ");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idDoctoServico", idDoctoServico);

		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("ID_FILIAL_ORIGEM", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("NM_FILIAL_ORIGEM", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_ORIGEM", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("LONGITUDE_ORIGEM", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ID_FILIAL_DESTINO", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("NM_FILIAL_DESTINO", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_DESTINO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("LONGITUDE_DESTINO", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("ID_FILIAL_ATUAL", Hibernate.LONG);
				sqlQuery.addScalar("SG_FILIAL_ATUAL", Hibernate.STRING);
				sqlQuery.addScalar("NM_FANTASIA_ATUAL", Hibernate.STRING);
				sqlQuery.addScalar("LATITUDE_ATUAL", Hibernate.BIG_DECIMAL);
				sqlQuery.addScalar("LONGITUDE_ATUAL", Hibernate.BIG_DECIMAL);
			}
		};
		
		List<Map<String, Object>> result = 
				getAdsmHibernateTemplate().findBySqlToMappedResult(sb.toString(), params, configureSqlQuery);
		
		if (result != null && !result.isEmpty()) {
			return result.iterator().next();
		}
		
		return null;
	}

	private StringBuilder getConhecimentosNaoComissionaveisSQL(Map<String, Object> parameters) {
		StringBuilder sb = new StringBuilder();
		sb.append("  select DS.ID_DOCTO_SERVICO     IDDOCTOSERVICO ");
		sb.append("       , DS.NR_DOCTO_SERVICO     NRDOCTOSERVICO ");
		sb.append("       , DS.TP_DOCUMENTO_SERVICO TPDOCUMENTO ");
		sb.append("       , DS.VL_FRETE_LIQUIDO     VLFRETELIQUIDO ");
		sb.append("       , DS.DH_EMISSAO           DHEMISSAO ");
		sb.append("       , DDSF.DT_LIQUIDACAO      DTPAGAMENTO ");
		sb.append("       , case when CO.TP_FRETE = 'C' then 'CIF' else case when CO.TP_FRETE = 'F' then 'FOB' end end TPFRETE ");
		sb.append("       , case when S.TP_MODAL  = 'R' then 'RODOVIARIO' else case when S.TP_MODAL = 'A' then 'AEREO' end end TPMODAL ");
		sb.append("       , PR.NR_IDENTIFICACAO     CNPJREMETENTE ");
		sb.append("       , PR.NM_PESSOA            NMREMETENTE ");
		sb.append("       , PD.NR_IDENTIFICACAO     CNPJDESTINATARIO ");
		sb.append("       , PD.NM_PESSOA            NMDESTINATARIO ");
		sb.append("       , PRE.NR_IDENTIFICACAO    CNPJRESPONSAVEL ");		
		sb.append("       , PRE.NM_PESSOA           NMRESPONSAVEL ");
		sb.append("       , T.NM_TERRITORIO         NMTERRITORIO ");
		sb.append("       , ET.ID_USUARIO           IDEXECUTIVO ");
		sb.append("       , (select 'SIM' from DOCTO_SERVICO_SEM_COMISSAO DSSC where DSSC.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO) NAOCOMISSIONAVEL ");
		sb.append("    from DOCTO_SERVICO DS ");
		sb.append("    join CONHECIMENTO CO on CO.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ");
		sb.append("    join DEVEDOR_DOC_SERV_FAT DDSF on DDSF.ID_DOCTO_SERVICO = DS.ID_DOCTO_SERVICO ");
		sb.append("    join SERVICO S on S.ID_SERVICO = DS.ID_SERVICO ");
		sb.append("    join PESSOA PR on PR.ID_PESSOA = DS.ID_CLIENTE_REMETENTE ");
		sb.append("    join PESSOA PD on PD.ID_PESSOA = DS.ID_CLIENTE_DESTINATARIO ");
		sb.append("    join PESSOA PRE on PRE.ID_PESSOA = DDSF.ID_CLIENTE ");
		sb.append("    join CLIENTE_TERRITORIO CT on CT.ID_CLIENTE = DDSF.ID_CLIENTE ");
		sb.append("    join EXECUTIVO_TERRITORIO ET on ET.ID_TERRITORIO = CT.ID_TERRITORIO ");
		sb.append("    join TERRITORIO T on T.ID_TERRITORIO = ET.ID_TERRITORIO ");
		sb.append("   where CT.TP_SITUACAO = 'A' ");		
		sb.append("     and ET.TP_EXECUTIVO = 'E' ");
		sb.append("     and ET.TP_SITUACAO = 'A' ");

		Long idExecutivo = MapUtils.getLong(parameters, "idExecutivo");
		Long idCliente = MapUtils.getLong(parameters, "idCliente");
		String dtCompeInicio = MapUtils.getString(parameters, "dtCompeInicio");
		String dtCompeFim = MapUtils.getString(parameters, "dtCompeFim");

		if (dtCompeInicio != null) {
			sb.append("     and DDSF.DT_LIQUIDACAO >= to_date('" + dtCompeInicio + "', 'YYYY-MM-DD') ");
			sb.append("     and (CT.DT_FIM >= to_date('" + dtCompeInicio + "', 'YYYY-MM-DD') or CT.DT_FIM is null) ");
			sb.append("     and (ET.DT_FIM >= to_date('" + dtCompeInicio + "', 'YYYY-MM-DD') or ET.DT_FIM is null) ");
}
		
		if (dtCompeFim != null) {
			sb.append("     and DDSF.DT_LIQUIDACAO <= to_date('" + dtCompeFim + "', 'YYYY-MM-DD') ");
			sb.append("     and CT.DT_INICIO <= to_date('" + dtCompeFim + "', 'YYYY-MM-DD') ");
			sb.append("     and ET.DT_INICIO <= to_date('" + dtCompeFim + "', 'YYYY-MM-DD') ");
		}
		
		if (idExecutivo != null) {
			sb.append("     and ET.ID_USUARIO = :idExecutivo ");
		}
		
		if (idCliente != null) {
			sb.append("     and CT.ID_CLIENTE = :idCliente ");
		}
		
		return sb;
	}

	public List<Map<String, Object>> findConhecimentosNaoComissionaveis(Map<String, Object> parameters) {
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery.addScalar("IDDOCTOSERVICO"  , Hibernate.LONG);
				sqlQuery.addScalar("NRDOCTOSERVICO"  , Hibernate.LONG);
				sqlQuery.addScalar("TPDOCUMENTO"     , Hibernate.STRING);
				sqlQuery.addScalar("VLFRETELIQUIDO"  , Hibernate.DOUBLE);
				sqlQuery.addScalar("DHEMISSAO"       , Hibernate.DATE);
				sqlQuery.addScalar("DTPAGAMENTO"     , Hibernate.DATE);
				sqlQuery.addScalar("TPFRETE"         , Hibernate.STRING);
				sqlQuery.addScalar("TPMODAL"         , Hibernate.STRING);
				sqlQuery.addScalar("CNPJREMETENTE"   , Hibernate.STRING);
				sqlQuery.addScalar("NMREMETENTE"     , Hibernate.STRING);
				sqlQuery.addScalar("CNPJDESTINATARIO", Hibernate.STRING);
				sqlQuery.addScalar("NMDESTINATARIO"  , Hibernate.STRING);
				sqlQuery.addScalar("CNPJRESPONSAVEL" , Hibernate.STRING);
				sqlQuery.addScalar("NMRESPONSAVEL"   , Hibernate.STRING);
				sqlQuery.addScalar("NMTERRITORIO"    , Hibernate.STRING);
				sqlQuery.addScalar("IDEXECUTIVO"     , Hibernate.LONG);
				sqlQuery.addScalar("NAOCOMISSIONAVEL", Hibernate.STRING);
			}
		};
		return getAdsmHibernateTemplate().findPaginatedBySqlToMappedResult(getConhecimentosNaoComissionaveisSQL(parameters).toString(), parameters, csq).getList();
	}
	
	public Integer findCountConhecimentosNaoComissionaveis(Map<String, Object> parameters) {
		return getAdsmHibernateTemplate().getRowCountBySql("select * from ( " + getConhecimentosNaoComissionaveisSQL(parameters).toString() + " ) ", parameters);
	}

	@Override
	public ResponseSuggest findSuggestQuery(Map<String, Object> filter) {
		String nrCae = (String) filter.get("nrCae");
		String sgFilial = (String) filter.get("sgFilial");
		


		StringBuilder sql = new StringBuilder();
		sql.append(" select co.nr_cae, co.id_filial_origem, f.sg_filial from " + Conhecimento.class.getSimpleName() + " co "); 
		sql.append(" inner join " + Filial.class.getSimpleName() + " f on co.id_filial_origem = f.id_filial");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if (StringUtils.isNotBlank(nrCae) || StringUtils.isNotBlank(sgFilial)) {
			sql.append(" where co.nr_cae like :nrcae and f.sg_filial = :sgFilial");
			params.put("sgFilial", sgFilial);
			params.put("nrcae", nrCae + "%");
		}

		return new ResponseSuggest(sql.toString(), params);
	}

	public Map findBlProdutoPerigosoControladoByIdConhecimento(Long idConhecimento){
        SqlTemplate hql = new SqlTemplate();
        hql.addProjection("new Map(c.blProdutoPerigoso as blProdutoPerigoso, c.blProdutoControlado as blProdutoControlado)");
        hql.addInnerJoin(Conhecimento.class.getName(), "c");
        hql.addCriteria("c.id", "=", idConhecimento);
        
        List list = getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
        if (!list.isEmpty()){
            return (Map)list.get(0);
        }
        return null;
    }
	
	public boolean validateDoctoServicoIsTpSubContratacaoIdDoctoServico(Long idDoctoServico) {
        StringBuilder sql = new StringBuilder("");
        sql.append("select count(*) from Conhecimento as con ");
        sql.append("where con.id = :idDoctoServico ");
        sql.append("and con.nrCtrcSubcontratante is not null ");
        
        Map<String, Object> namedParams = new HashMap<String, Object>();
        namedParams.put("idDoctoServico", idDoctoServico);

        List list = getAdsmHibernateTemplate().findByNamedParam(sql.toString(), namedParams);
        return (Long) list.get(0) > 0;
    }
	
	public List<Long> findConhecimentoToValidateLiberacaoConhecimentoReentrega(Long idDoctoServico) {
        final StringBuilder sql = new StringBuilder();
        sql.append(" SELECT CONH.ID_CONHECIMENTO FROM  ");
        sql.append(" CONHECIMENTO CONH, ");
        sql.append(" DOCTO_SERVICO DS ");
        sql.append(" WHERE DS.ID_DOCTO_SERVICO = CONH.ID_CONHECIMENTO  ");
        sql.append(" AND DS.TP_DOCUMENTO_SERVICO IN ('CTE','CTR','NTE','NFT')  ");
        sql.append(" AND CONH.TP_CONHECIMENTO = 'RE' ");
        sql.append(" AND DS.ID_DOCTO_SERVICO_ORIGINAL = "+ idDoctoServico +" ");
        
        final ConfigureSqlQuery csq = new ConfigureSqlQuery() {
            @Override
            public void configQuery(org.hibernate.SQLQuery sqlQuery) {
                sqlQuery.addScalar("ID_CONHECIMENTO", Hibernate.LONG);
            }
        };

        final HibernateCallback hcb = new HibernateCallback() {

            @Override
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                SQLQuery query = session.createSQLQuery(sql.toString());
                csq.configQuery(query);
                return query.list();
            }
        };
        return getHibernateTemplate().executeFind(hcb);
    }
	
	private ConfigureSqlQuery configureSqlQueryConhecimentoByNrConhecimentoIdFilial(){
		return new ConfigureSqlQuery() {

			@Override
			public void configQuery(SQLQuery sqlQuery) {
				sqlQuery.addScalar("idDoctoServico", Hibernate.LONG);
		        sqlQuery.addScalar("nrConhecimento", Hibernate.LONG);
		        sqlQuery.addScalar("sgFilial", Hibernate.STRING);
		        sqlQuery.addScalar("tpDocumentoServico", Hibernate.STRING);
				
			}
			
		};
	}

	public Conhecimento findConhecimentoByNrChave(String nrChave) {
		StringBuilder hql = new StringBuilder();

		hql.append("SELECT c ");
		hql.append("FROM " + Conhecimento.class.getSimpleName() + " as c, ");
		hql.append(MonitoramentoDocEletronico.class.getSimpleName() + " mde ");
		hql.append("WHERE ");
		hql.append(" c.id = mde.doctoServico.idDoctoServico AND ");
		hql.append(" mde.nrChave = ?");

		return (Conhecimento)getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[]{nrChave});
	}

}
