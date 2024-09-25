package com.mercurio.lms.contasreceber.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.core.model.hibernate.JodaTimeYearMonthDayUserType;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.JodaTimeUtils;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.param.BoletoParam;
import com.mercurio.lms.util.FormatUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplica??o
 * atrav?s do suporte ao Hibernate em conjunto com o Spring.
 * N?o inserir documenta??o ap?s ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class BoletoDAO extends BaseCrudDao<Boleto, Long>
{
	
	private JdbcTemplate jdbcTemplate;
	private static MessageSource messageSource;
	
	/**
	 * Para que seja feito o uso do Fetch no findById padrao
	 */
	@Override
	protected void initFindByIdLazyProperties(Map lazyFindById) {
		
		lazyFindById.put("fatura",FetchMode.JOIN);
		lazyFindById.put("fatura.filialByIdFilial",FetchMode.JOIN);
		super.initFindByIdLazyProperties(lazyFindById);
	}
	
	/**
	 * Nome da classe que o DAO ? respons?vel por persistir.
	 */
    @Override
    protected final Class getPersistentClass() {
        return Boleto.class;
    }

    /**
     * Retorna o boleto ativo por id fatura
     * 
     * @param Long idFatura
     * @return List
     * */
    public List findByFatura(Long idFatura){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("bol");
    	sql.addFrom(Boleto.class.getName(), "bol " +
    										"join fetch bol.fatura as fat " +
    										"join fetch bol.cedente as ced " +
    										"join fetch ced.agenciaBancaria as agen " +
    										"join fetch agen.banco as ban ");
    	sql.addCriteria("bol.tpSituacaoBoleto","!=","CA");
    	sql.addCriteria("fat.id","=",idFatura);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    
    
    
    
    
    /**
     * Retorna o boletos ativos por id faturas
     * */
    public List<Boleto> findByFaturas(List<Long> idFaturas){
    	if(idFaturas.isEmpty()) {
    		return new ArrayList<Boleto>();
    	}
    	
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("bol");
    	sql.addFrom(Boleto.class.getName(), "bol " +
    										"join fetch bol.fatura as fat " +
    										"join fetch bol.cedente as ced " +
    										"join fetch ced.agenciaBancaria as agen " +
    										"join fetch agen.banco as ban ");
    	sql.addCriteria("bol.tpSituacaoBoleto","!=","CA");
    	sql.addCriteriaIn("fat.id", idFaturas);
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }

    
    /**
     * Retorna o boletos ativos por idRedeco
     * */
    public List<Boleto> findByRedeco(Long idRedeco){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("bol");
    	sql.addFrom(Boleto.class.getName(), "bol " +
    										"join fetch bol.fatura as fat " +
    										"join fetch fat.redeco as red " +
    										//"join fetch red.itemRedeco itr " +
    										"join fetch bol.cedente as ced " +
    										"join fetch ced.agenciaBancaria as agen " +
    										"join fetch agen.banco as ban ");
    	sql.addCriteria("bol.tpSituacaoBoleto","!=","CA");
    	sql.addCriteria("red.idRedeco", "=", idRedeco);
    	
    	return this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    }


	public Boleto findFirstByFaturaAndSituacaoBoleto(Long idFatura, String situacaoBoleto) {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("bol");
    	sql.addFrom(Boleto.class.getName(), "bol " +
    										"join fetch bol.fatura as fat " +
    										"join fetch bol.cedente as ced " +
    										"join fetch ced.agenciaBancaria as agen " +
    										"join fetch agen.banco as ban ");
    	sql.addCriteria("bol.tpSituacaoBoleto","=",situacaoBoleto);
    	sql.addCriteria("fat.id","=",idFatura);
    	sql.addCustomCriteria("rownum = 1");
    	return (Boleto) this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
    
    /**
     * Retorna a situa??o do boleto diferente de cancelado.
     * 
     * @author Micka?l Jalbert
     * @since 29/08/2006
     * 
     * @param Long idFatura
     * @return String
     * */
    public String findTpSituacaoByFatura(Long idFatura){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("bol.tpSituacaoBoleto");
    	
    	sql.addInnerJoin(Boleto.class.getName(), "bol");
    	sql.addInnerJoin("bol.fatura", "fat");
    	
    	sql.addCriteria("bol.tpSituacaoBoleto","!=","CA");
    	sql.addCriteria("fat.id","=",idFatura);

    	List lstBoleto = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	if (!lstBoleto.isEmpty()){
    		return ((DomainValue)lstBoleto.get(0)).getValue();
    	} else {
    		return null;
    	}
    }    
    
    public ResultSetPage findPaginated(BoletoParam boletoParam, FindDefinition findDef) {
    	SqlTemplate hql = mountHql(boletoParam);
    	
    	StringBuilder projection = new StringBuilder()
    	.append("  new map( \n")
    	.append("   bol.idBoleto as idBoleto, \n")
    	.append("	bol.tpSituacaoBoleto as tpSituacaoBoleto, \n")
    	.append("   bol.dtEmissao as dtEmissao, \n")
    	.append("   bol.dtVencimento as dtVencimento, \n")
    	.append("   fil.sgFilial as fatura_filialByIdFilial_sgFilial, \n")
    	.append("   fat.nrFatura as fatura_nrFatura, \n")
    	.append("   filcob.sgFilial as fatura_filialByIdFilialCobradora_sgFilial, \n")
    	.append("   pescli.nrIdentificacao as fatura_cliente_pessoa_nrIdentificacao, \n")
    	.append("   pescli.tpIdentificacao as fatura_cliente_pessoa_tpIdentificacao, \n")
    	.append("   pescli.nmPessoa as fatura_cliente_pessoa_nmPessoa, \n")
    	.append("   bol.vlTotal as vlTotal) \n");
    	
    	hql.addProjection(projection.toString());
    	hql.addOrderBy("fil.sgFilial");
    	hql.addOrderBy("fat.nrFatura");
    	
    	return getAdsmHibernateTemplate().findPaginated(hql.getSql(), findDef.getCurrentPage(), findDef.getPageSize(), hql.getCriteria());
    }
    
	public Integer getRowCount(BoletoParam boletoParam) {
		SqlTemplate hql = mountHql(boletoParam);
		hql.addProjection("count(bol.id)");

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
		return result.intValue();
	}

    private SqlTemplate mountHql(BoletoParam boletoParam) {
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(Boleto.class.getName(), "bol");
    	hql.addInnerJoin("bol.fatura", "fat");
    	hql.addInnerJoin("bol.cedente", "ced");
    	hql.addInnerJoin("fat.cliente", "cli");
    	hql.addInnerJoin("fat.filialByIdFilialCobradora", "filcob");
    	hql.addInnerJoin("fat.filialByIdFilial", "fil");
    	hql.addInnerJoin("cli.pessoa", "pescli");
    	hql.addInnerJoin("filcob.pessoa", "pes");
    	 
    	if (boletoParam.getIdDevedorDocServFat() != null) {
    		hql.addFrom(", " + DevedorDocServFat.class.getName(), "dev");
    		hql.addJoin("dev.fatura.id", "fat.id");
    		hql.addCriteria("dev.id", "=", boletoParam.getIdDevedorDocServFat());
    	}
    	
    	if(!boletoParam.getNrBoleto().isEmpty()){
    		hql.addCriteria("bol.nrBoleto", "like", boletoParam.getNrBoleto().substring(0, boletoParam.getNrBoleto().length() - 1));
    	}
    	hql.addCriteria("bol.nrSequenciaFilial", "=", boletoParam.getNrSequenciaFilial());
    	hql.addCriteria("bol.tpSituacaoBoleto", "=", boletoParam.getTpSituacaoBoleto());
    	hql.addCriteria("fat.id", "=", boletoParam.getIdFatura());
    	hql.addCriteria("cli.id", "=", boletoParam.getIdCliente());
    	hql.addCriteria("fat.filialByIdFilial.id", "=", boletoParam.getIdFilialFatura());    	
    	hql.addCriteria("fat.filialByIdFilialCobradora.id", "=", boletoParam.getIdFilialCobranca());    	
    	hql.addCriteria("bol.dtEmissao", ">=", boletoParam.getDtEmissaoInicial());
    	hql.addCriteria("bol.dtEmissao", "<=", boletoParam.getDtEmissaoFinal());
    	hql.addCriteria("bol.dtVencimento", ">=", boletoParam.getDtVencimentoInicial());
    	hql.addCriteria("bol.dtVencimento", "<=", boletoParam.getDtVencimentoFinal());    	
    	hql.addCriteria("fat.dtLiquidacao", ">=", boletoParam.getDtLiquidacaoInicial());
    	hql.addCriteria("fat.dtLiquidacao", "<=", boletoParam.getDtLiquidacaoFinal());
    	hql.addCriteria("ced.id", "=", boletoParam.getIdCedente());
    	
    	return hql;
    }
    
    /**
     * Retorna o boleto do id informado com relacionamento para a tela
     * 
     * @author Micka?l Jalbert
     * @since 19/04/2006
     * 
     * @param Long idBoleto
     * @return Boleto
     * */
    public Boleto findByIdTela(Long idBoleto){
    	SqlTemplate sql = mountHqlTela(idBoleto);
    	sql.addProjection("bol");

    	List lstBoleto = this.getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria());
    	
    	if (lstBoleto.size() == 1){
    		return (Boleto)lstBoleto.get(0);
    	} else {
    		return null;
    	}
    }    
    
    private SqlTemplate mountHqlTela(Long idBoleto){
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addInnerJoin(Boleto.class.getName(), "bol");
    	hql.addInnerJoin("fetch bol.fatura", "fat");
    	hql.addInnerJoin("fetch fat.filialByIdFilialCobradora", "filcob");
    	hql.addInnerJoin("fetch filcob.pessoa", "pesfilcob");
    	hql.addInnerJoin("fetch fat.filialByIdFilial", "fil");
    	hql.addInnerJoin("fetch fil.pessoa", "pesfil");
    	hql.addInnerJoin("fetch bol.cedente", "ced");
    	hql.addInnerJoin("fetch fat.cliente", "cli");
    	hql.addInnerJoin("fetch cli.pessoa", "pescli");    	
    	hql.addLeftOuterJoin("fetch fat.relacaoCobranca", "relcob");
    	hql.addLeftOuterJoin("fetch fat.divisaoCliente", "div");
    	hql.addLeftOuterJoin("fetch bol.usuario", "us");
    	hql.addLeftOuterJoin("fetch relcob.filial", "filrelcob");
    	hql.addLeftOuterJoin("fetch fat.cotacaoMoeda", "cotmoe");
    	hql.addLeftOuterJoin("fetch cotmoe.moedaPais", "moePa");
    	hql.addLeftOuterJoin("fetch moePa.moeda", "moe");

    	hql.addCriteria("bol.id","=",idBoleto);
    	
    	return hql;
    } 
    
    /**
     * Salva o boleto usando o hibernate puro para optimizar o tempo de inser??o 
     * 
     * @author Hector Junior
     * @since 07/08/2006
     * 
     * @param boleto
     * 
     * @return boleto
     */    
    public Boleto storeBasic(Boleto boleto){
    	getAdsmHibernateTemplate().saveOrUpdate(boleto);
		return boleto;
    }
    
	/**
	 * Atualiza o campo tpSituacaoAntBoleto com o campo tpSituacaoBoleto do boleto da fatura informada.
	 * 
	 * @author Micka?l Jalbert
	 * @since 23/10/2006
	 * 
	 * @param Long idFatura
	 */
	public void updateSituacaoBoleto(Long idFatura, String tpSituacaoBoleto){
		SqlTemplate hql = new SqlTemplate();
		
    	hql.addProjection("bol.id");
    	hql.addInnerJoin(Boleto.class.getName(), "bol");
    	hql.addCustomCriteria("bol.tpSituacaoBoleto != 'CA'");
    	hql.addCustomCriteria("bol.fatura.id = " + idFatura);
		
    	String update = "UPDATE " + Boleto.class.getName() + " \n " +
						"SET tpSituacaoAntBoleto = tpSituacaoBoleto, \n " +
						"    tpSituacaoBoleto = '"+tpSituacaoBoleto+"' \n " +
						"WHERE id in (" + hql.getSql() + ")";
    	
    	executeUpdate(update);
	}
	
	/**
	 * Atualiza o campo tpSituacaoAntBoleto com o campo tpSituacaoBoleto do boleto da fatura informada.
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 02/03/2007
	 *
	 * @param idBoleto
	 * @param tpSituacaoBoleto
	 *
	 */
	public void updateSituacaoBoletoByIdBoleto(Long idBoleto, String tpSituacaoBoleto){
		getSession().flush();
		
		Boleto boleto = findById(idBoleto);
		
		boleto.setTpSituacaoAntBoleto(boleto.getTpSituacaoBoleto());
		boleto.setTpSituacaoBoleto(new DomainValue(tpSituacaoBoleto));
		
		this.store(boleto);

	}
	
	/**
	 * Atualiza o campo tpSituacaoBoleto com o campo tpSituacaoAntBoleto do boelto da fatura informada.
	 * 
	 * @author Micka?l Jalbert
	 * @since 23/10/2006
	 * 
	 * @param Long idFatura
	 */
	public void updateSituacaoBoletoAnterior(Long idFatura){
		SqlTemplate hql = new SqlTemplate();
		
    	hql.addProjection("bol.id");
    	hql.addInnerJoin(Boleto.class.getName(), "bol");
    	hql.addCustomCriteria("bol.tpSituacaoBoleto != 'CA'");
    	hql.addCustomCriteria("bol.fatura.id = " + idFatura);
		
    	String update = "UPDATE " + Boleto.class.getName() + " \n " +
						"SET tpSituacaoBoleto = tpSituacaoAntBoleto \n " +
						"WHERE id in (" + hql.getSql() + ")";
    	
    	executeUpdate(update);
	} 	

	public void executeUpdate(final String hql){
		getAdsmHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (StringUtils.isNotBlank(hql)){
					query.executeUpdate();
				}
				
				return null;
			}
		});
	}	

	/**
	 * Busca a dtVencimeto e dtVencimentoNovo do boleto
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 28/02/2007
	 *
	 * @param idBoleto
	 * @return
	 *
	 */
	public Object[] findDtVencimentoBoleto(Long idBoleto){
		
		
		
		SqlTemplate sql = new SqlTemplate();
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			@Override
			public void configQuery(SQLQuery sqlQuery) {
                sqlQuery.addScalar("dtvencimento", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
                sqlQuery.addScalar("dtvencimentonovo", Hibernate.custom(JodaTimeYearMonthDayUserType.class));
			}
		};
    	
    	sql.addProjection("B.DT_VENCIMENTO", "dtvencimento");
    	sql.addProjection("B.DT_VENCIMENTO_NOVO", "dtvencimentonovo");
    	
    	sql.addFrom("BOLETO","B");
    	
    	sql.addCriteria("B.ID_BOLETO", "=", idBoleto);

    	
    	return   (Object[])getAdsmHibernateTemplate().findByIdBySql(sql.getSql(),sql.getCriteria(), csq);
	}
	
	/**
	 * Busca o boleto filtrando pelo idFilialOrigem da fatura e pelo nrSequenciaFilial
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 30/05/2007
	 *
	 * @param idFilialOrigem
	 * @param nrSequenciaFilial
	 * @return
	 *
	 */
	public Boleto findByIdFilialOrigemAndNrSequenciaFilial(Long idFilialOrigem, Long nrSequenciaFilial){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addProjection("bol");
    	
    	sql.addInnerJoin(Boleto.class.getName() + " bol ");
    	sql.addInnerJoin("fetch bol.fatura as fat ");
    	
    	sql.addCriteria("bol.nrSequenciaFilial", "=", nrSequenciaFilial);
    	sql.addCriteria("fat.filialByIdFilial.idFilial", "=", idFilialOrigem);
    	
    	return (Boleto) this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
	
	/**
	 * Busca o boleto filtrando pelo nrBoleto
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 21/06/2007
	 *
	 * @param nrBoleto
	 * @param tpSituacao
	 * @return
	 *
	 */
	public Boleto findByNrBoleto(String nrBoleto){
    	
		if (nrBoleto == null) {
			return null;
		
		}		
		SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("bol");
    	
    	sql.addInnerJoin(Boleto.class.getName() + " bol ");
    	sql.addInnerJoin("fetch bol.fatura as fat ");
    	
    	sql.addCriteria("bol.nrBoleto", "=", nrBoleto);
    		
    	return (Boleto) this.getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria());
    }
	
	public List<Long> findFaturasBoletoByCriteria(TypedFlatMap parameters) throws SQLException{
		SqlTemplate template = mountSqlBoleto(parameters);
		template.addCustomCriteria("cli.bl_emite_dacte_faturamento = 'S'");
		template.addCustomCriteria("fat.qt_documentos <= 12");

		Connection connection = getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().connection();
		PreparedStatement statement = connection.prepareStatement(template.getSql().toString());
		for (int i = 0; i < template.getCriteria().length; i++){
			statement.setObject(i+1, template.getCriteria()[i]);
			
		}
		
		ResultSet rs = statement.executeQuery();
		List<Long> idsFaturas = new ArrayList<Long>();
		while(rs.next()){
			idsFaturas.add(rs.getLong("ID_FATURA"));
			
		}
		return idsFaturas;
	}
	
	//------------------------------ Métodos relacionados a geração dos boletos (Report)
	
	/**
	 * Método responsável por montar o sql principal EMITIR BOLETO
	 * 
	 * @author HectorJ
	 * @since 16/06/2006
	 * 
	 * @param Map parameters
	 * @return SqlTemplate
	 * 
	 */
	public SqlTemplate mountSqlBoleto(TypedFlatMap tfm) {
		
		SqlTemplate sql = createSqlTemplate();

		sql.addProjection(new StringBuilder()
				  .append("fat.id_fatura as ID_FATURA, ")
				  .append("bol.id_boleto as ID_BOLETO, ")
				  .append("bol.tp_situacao_boleto as TP_SITUACAO_BOLETO, ")			
				  .append("b.nr_banco as NR_BANCO, ")
				  .append("fat.qt_documentos as QT_DOCUMENTOS").toString());

		sql.addFrom("boleto bol " +
					"inner join cedente c on c.id_cedente = bol.id_cedente " +
					"inner join agencia_bancaria ab on ab.id_agencia_bancaria = c.id_agencia_bancaria " +
					"inner join banco b on b.id_banco = ab.id_banco " +
					"inner join fatura fat on fat.id_fatura = bol.id_fatura " +
					"	and fat.qt_documentos > 0 " +
					"inner join cliente cli on fat.id_cliente = cli.id_cliente ");	
		sql.addCustomCriteria("(fat.tp_situacao_aprovacao = 'A' OR fat.tp_situacao_aprovacao is null)");
		// Chamado pelas telas de boleto
		
		if (tfm.getString("idFaturas") == null) {
			
			if (tfm.getLong("idManifesto") == null) {
				sql.addCriteria("fat.id_fatura", "=", tfm.getLong("fatura.idFatura"));
				sql.addCriteria("fat.id_filial", "=", tfm.getLong("filial.idFilial"));
				sql.addCriteria("bol.id_boleto", "=", tfm.getLong("idBoleto"));
	
				/**
				 * VALIDA SE É REEMISSÃO DE BOLETOS PARA ADICIONAR O FILTRO DE REEMISSÃO
				 */
				if(tfm.getBoolean("reemissao") != null && tfm.getBoolean("reemissao").equals(Boolean.TRUE)){
					sql.addCriteria("bol.bl_boleto_reemitido", "=", "N");
				} else {
					/** Testar intervalo de boletos somente na emissão */
					boolean inicialPreenchido 	= StringUtils.isNotBlank(tfm.getString("boletoInicial")); 
					boolean finalPreenchido 	= StringUtils.isNotBlank(tfm.getString("boletoFinal"));
					
					if( inicialPreenchido && finalPreenchido ){
						/** Caso o intervalo inteiro seja informado*/
						sql.addCustomCriteria("( bol.nr_boleto between ? and ? )");
						sql.addCriteriaValue(FormatUtils.completaDados(tfm.getString("boletoInicial"), "0", 13, 0, true));
						sql.addCriteriaValue(FormatUtils.completaDados(tfm.getString("boletoFinal"), "0", 13, 0, true));
					
					}else if(inicialPreenchido && !finalPreenchido) {
						sql.addCriteria("bol.nr_boleto", ">=", FormatUtils.completaDados(tfm.getString("boletoInicial"), "0", 13, 0, true));
					
					}else if(finalPreenchido && !inicialPreenchido){
						sql.addCriteria("bol.nr_boleto", "<=", FormatUtils.completaDados(tfm.getString("boletoFinal"), "0", 13, 0, true));
	
					}else if (tfm.getLong("idBoleto") == null && tfm.getLong("fatura.idFatura") == null){
						/** idBoleto é passado pela tela manter boletos  -  busca boletos digitados quando o intervalo de boletos e a fatura são nulos */
						sql.addCriteria("bol.tp_situacao_boleto", "=", "DI");
					}
				}
			// Chamado pelas telas de manifesto
			} else {
				// Manifesto de viagem
				if ("V".equals(tfm.getString("tpManifesto"))) {
					sql.addCriteria("fat.id_manifesto", "=", tfm.getLong("idManifesto"));
					
				// Manifesto de entrega
				} else if ("E".equals(tfm.getString("tpManifesto"))) {
					sql.addCriteria("fat.id_manifesto_entrega", "=", tfm.getLong("idManifesto"));
					
				}
			} 	
			sql.addCriteria("c.id_cedente", "=", tfm.getLong("cedente.idCedente"));

		} else {
			sql.addCustomCriteria("bol.ID_FATURA IN (" + tfm.getString("idFaturas") + ")");
			
		}
		
		/** Nunca busca boletos cancelados e liquidaddos */
		sql.addCriteria("bol.tp_situacao_boleto", "<>", "CA");
		sql.addCriteria("bol.tp_situacao_boleto", "<>", "LI");

		sql.addOrderBy("fat.nr_fatura ");

		return sql;
	}

	/**
	 * Itera cada fatura
	 * 
	 * @author Mickaël Jalbert
	 * @since 07/03/2007
	 * @param sql
	 * @return
	 */
	public List iteratorSelectBoleto(SqlTemplate sql) {			
		List lstFatura = getJdbcTemplate().query(sql.getSql(), JodaTimeUtils.jdbcPureParamConverter(getJdbcTemplate(),sql.getCriteria()), new RowMapper(){
			@Override
			@SuppressWarnings("unchecked")
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map mapBoleto = new HashMap();
				mapBoleto.put("NR_BANCO", rs.getShort("NR_BANCO")); 
				mapBoleto.put("ID_BOLETO", rs.getLong("ID_BOLETO"));
				mapBoleto.put("QT_DOCUMENTOS", rs.getLong("QT_DOCUMENTOS"));
				mapBoleto.put("TP_SITUACAO_BOLETO", rs.getString("TP_SITUACAO_BOLETO")); 
				return mapBoleto;
			}
		});

		return lstFatura;
	}	

	/**
	 * Método responsável por montar o sql do subRelatorio
	 * 
	 * @author HectorJ
	 * @since 16/06/2006
	 * 
	 * @param Map parameters
	 * @return SqlTemplate
	 * 
	 */
	public SqlTemplate mountSqlSubReportBoleto(Long idBoleto){
		SqlTemplate sql = new SqlTemplate();
		sql.addProjection("c.id_cedente as ID_CEDENTE, " +
						  "c.cd_cedente as CD_CEDENTE, " +
						  "bol.id_boleto as ID_BOLETO, " +
						  "b.nr_banco as NR_BANCO, " +
						  "ab.nr_agencia_bancaria as NR_AGENCIA_BANCARIA, " +
						  "ab.nr_digito as NR_DIGITO_AGENCIA_BANCARIA, " +						  
						  "c.nr_carteira as NR_CARTEIRA, " +
						  "c.nr_conta_corrente as NR_CONTA_CORRENTE, " +
						  "bol.dt_vencimento as DATA_VENCIMENTO, " +
						  "bol.nr_boleto as NOSSO_NUMERO, " +
						  "bol.vl_total as VALOR, " +
		  				  "pfilfat.nm_pessoa as CEDENTE_NOME, " +
						  "pfilfat.nm_fantasia as NM_FILIAL, " +
						  "pfilfat.tp_identificacao as TP_IDENTIFICACAO, " +
						  "pfilfat.nr_identificacao as NR_IDENTIFICACAO, " +
						  "bol.dt_emissao as DATA_DOCUMENTO, " + 
						  "filfaturamento.sg_filial as SG_FILIAL, " +
						  "fat.nr_fatura as NR_DOCUMENTO, " +
						  "fat.id_filial_cobradora as ID_FILIAL_COBRADORA, " +
						  "fat.id_cliente as ID_CLIENTE_FATURA, " +
						  "pclifat.nm_pessoa as SACADO_NOME, " +
						  
						  "epfilfat.ds_endereco as DS_ENDERECO_FIL_FAT, " +
						  "epfilfat.nr_endereco as NR_ENDERECO_FIL_FAT, " +
						  "upper("+PropertyVarcharI18nProjection.createProjection("tlfil.ds_tipo_logradouro_i")+") as DS_TIPO_LOG_FIL_FAT, " +
						  "epfilfat.ds_complemento as DS_COMPLEMENTO_FIL_FAT, " +
						  "epfilfat.ds_bairro as FIL_BAIRRO, " +
						  "epfilfat.nr_cep as FIL_NR_CEP, " +
						  "mfil.nm_municipio as FIL_NM_MUNICIPIO, " +
						  "uffil.sg_unidade_federativa as FIL_SG_UF, " +
						  
						  "(SELECT DECODE(TE.NR_DDI, null, '', '(' || TE.NR_DDI || ')') || DECODE(TE.NR_DDD, null, '', '(' || TE.NR_DDD || ')') || TE.NR_TELEFONE FROM TELEFONE_ENDERECO TE WHERE TE.ID_TELEFONE_ENDERECO = (SELECT MIN(TETMP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TETMP WHERE TETMP.TP_USO = 'FO' AND TETMP.ID_ENDERECO_PESSOA = F_BUSCA_ENDERECO_PESSOA(fat.ID_FILIAL_COBRADORA, 'COB', SYSDATE))) as TELEFONE_FIL_COB, " +
						  "(SELECT DECODE(TE.NR_DDI, null, '', '(' || TE.NR_DDI || ')') || DECODE(TE.NR_DDD, null, '', '(' || TE.NR_DDD || ')') || TE.NR_TELEFONE FROM TELEFONE_ENDERECO TE WHERE TE.ID_TELEFONE_ENDERECO = (SELECT MIN(TETMP.ID_TELEFONE_ENDERECO) FROM TELEFONE_ENDERECO TETMP WHERE TETMP.TP_USO = 'FO' AND TETMP.ID_ENDERECO_PESSOA = epclifat.id_endereco_pessoa)) as TELEFONE, " +
						  "epclifat.ds_endereco as DS_ENDERECO_CLIENTE_FAT, " +
						  "epclifat.nr_endereco as NR_ENDERECO_CLIENTE_FAT, " +
						  "upper("+PropertyVarcharI18nProjection.createProjection("tl.ds_tipo_logradouro_i")+") as DS_TIPO_LOG_CLIENTE_FAT, " +
						  "epclifat.ds_complemento as DS_COMPLEMENTO_CLIENTE_FAT, " +
						  "epclifat.ds_bairro as SACADO_BAIRRO, " +
						  "epclifat.nr_cep as NR_CEP, " +
						  "m.nm_municipio as NM_MUNICIPIO, " +
						  "uf.sg_unidade_federativa as SG_UF, " +
						  "pclifat.tp_identificacao as SACADO_TP_IDENTIFICACAO, " +
						  "pclifat.nr_identificacao as SACADO_NR_IDENTIFICACAO, " +
						  "( " +
						  "		select " +
						  "			min(da.ds_documento) " +
						  "		from " +
						  "			documento_anexo da, " +
						  "			docto_servico dstmp, " +
						  "			item_fatura ifat, " +
						  "			devedor_doc_serv_fat ddsf " +
						  "		where " +
						  "			da.id_anexo_docto_servico = 102 " +
						  "			and dstmp.id_docto_servico = da.id_cto_internacional " +
						  "			and ifat.id_fatura = fat.id_fatura " +
						  "			and ddsf.id_devedor_doc_serv_fat = ifat.id_devedor_doc_serv_fat " +
						  "			and dstmp.id_docto_servico = ddsf.id_docto_servico " +
						  ") as REF_BOSCH, " +
						  "bol.vl_juros_dia JUROS_DIA, " +
						  "( " +
						  "		select " +
						  "			sum(dstmp.vl_imposto) " +
						  "		from " +
						  "			docto_servico dstmp, " +
						  "			item_fatura ifat, " +
						  "			devedor_doc_serv_fat ddsf " +
						  "		where " +
						  "			ifat.id_fatura = fat.id_fatura " +
						  "			and ddsf.id_devedor_doc_serv_fat = ifat.id_devedor_doc_serv_fat " +
						  "			and dstmp.id_docto_servico = ddsf.id_docto_servico " +
						  ") as VL_IMPOSTOS, " +
						  "fat.vl_cotacao_moeda as VL_COTACAO_DOLAR, " +
						  "fat.VL_JURO_CALCULADO, " +
						  "( " +
						  "		DECODE(fat.id_manifesto_entrega, null, DECODE(fat.id_manifesto, null, '', " +
											  															"( " +
											  															"	select mvn.nr_manifesto_origem " +
											  															"	from manifesto_viagem_nacional mvn, fatura fattmp " +
											  															"	where fattmp.id_manifesto = mvn.id_manifesto_viagem_nacional " +
											  															"		and fattmp.id_fatura = fat.id_fatura " +
											  															") " +
						  													"),  " +
						  													"  ( " +
						  													"		select me.nr_manifesto_entrega " +
						  													"		from manifesto_entrega me, fatura fattmp " +
						  													"		where fattmp.id_manifesto_entrega = me.id_manifesto_entrega " +
						  													"			and fattmp.id_fatura = fat.id_fatura " +
						  													"  ) " +
						             ") " +
						  ") as NR_MANIFESTO, " +
						  "( " +
						  "		select sum(dstmp.vl_total_doc_servico) " +
						  "		from fatura fattmp " +
						  "			inner join item_fatura ifattmp on ifattmp.id_fatura = fattmp.id_fatura " +
						  "			inner join devedor_doc_serv_fat ddsftmp on ddsftmp.id_devedor_doc_serv_fat = ifattmp.id_devedor_doc_serv_fat " +
						  "			inner join docto_servico dstmp on dstmp.id_docto_servico = ddsftmp.id_docto_servico " +
						  "		where fattmp.id_fatura = fat.id_fatura " +
						  ") as FRETE, " +
						  "bol.vl_desconto as DESCONTO_VENCIMENTO ");
		
		sql.addFrom("boleto bol " +	
					"inner join cedente c on c.id_cedente = bol.id_cedente " +
					"inner join agencia_bancaria ab on ab.id_agencia_bancaria = c.id_agencia_bancaria " +
					"inner join banco b on b.id_banco = ab.id_banco " +
					"inner join fatura fat on fat.id_fatura = bol.id_fatura " +
					"inner join filial filfat on filfat.id_filial = fat.id_filial_cobradora " +
					"inner join filial filfaturamento on filfaturamento.id_filial = fat.id_filial " +
					"inner join pessoa pfilfat on pfilfat.id_pessoa = filfat.id_filial " +
					"inner join cliente clifat on clifat.id_cliente = fat.id_cliente " +
					"inner join pessoa pclifat on pclifat.id_pessoa = clifat.id_cliente, " +
					"endereco_pessoa epclifat " +
					"inner join tipo_logradouro tl on tl.id_tipo_logradouro = epclifat.id_tipo_logradouro " +
					"inner join municipio m on m.id_municipio = epclifat.id_municipio " +
					"inner join unidade_federativa uf on m.id_unidade_federativa = uf.id_unidade_federativa, " +
					"endereco_pessoa epfilfat " +
					"inner join tipo_logradouro tlfil on tlfil.id_tipo_logradouro = epfilfat.id_tipo_logradouro " +
					"inner join municipio mfil on mfil.id_municipio = epfilfat.id_municipio " +
					"inner join unidade_federativa uffil on mfil.id_unidade_federativa = uffil.id_unidade_federativa ");

		sql.addCriteria("bol.id_boleto", "=", idBoleto);
		sql.addCustomCriteria("epclifat.id_endereco_pessoa = F_BUSCA_ENDERECO_PESSOA(pclifat.ID_PESSOA, 'COB', SYSDATE)");
		sql.addCustomCriteria("epfilfat.id_endereco_pessoa = F_BUSCA_ENDERECO_PESSOA(pfilfat.ID_PESSOA, 'COB', SYSDATE)");

		long hash = System.currentTimeMillis();
		sql.addCustomCriteria(hash + "=" + hash);

		return sql;
	}

	/**
	 * Método responsável por iterar o resultSet do subReport
	 *
	 * @author Hector Julian Esnaola Junior
	 * @since 02/03/2007
	 *
	 * @param sql
	 * @return
	 *
	 */
	public List iteratorResultSetSubSubReportBoleto(String sql, Object[] criteria){
		return getJdbcTemplate().query(sql, criteria, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map map = new HashMap();
				map.put("FILIAL_DOCTO_SERVICO", rs.getString("FILIAL_DOCTO_SERVICO"));
				map.put("DOCTO_SERVICO", Long.valueOf(rs.getString("DOCTO_SERVICO")));
				map.put("TP_DOCTO_SERVICO", rs.getString("TP_DOCTO_SERVICO"));
				map.put("FRETE", rs.getString("FRETE") != null ? new Double(rs.getString("FRETE")) : null);
				map.put("DESCONTO", rs.getString("DESCONTO") != null ? new Double(rs.getString("DESCONTO")) : null);
				map.put("NOTA_FISCAL", rs.getString("NOTA_FISCAL") != null ? Long.valueOf(rs.getString("NOTA_FISCAL")) : null);
				map.put("MAIOR_DOZE", rs.getString("MAIOR_DOZE"));
				return map;
				
			}
		});
	}

	/**
	 * Método responsável por montar a clausula from do Sub - SubReport
	 *  
	 * tipo == 1 - Docto de seviço não são conhecimento
	 * tipo == 2 - Docto de serviço é conhecimento e a fatura tem mais de um documento
	 * tipo == 3 - Docto de serviço é conhecimento e a fatura tem um documento
	 * tipo == 4 - A fatura tem mais de um documento
	 * 
	 * @param sql
	 * @param tipo
	 * 
	 */
	private void mountFromSubSubReport(StringBuilder sb, int tipo){
		sb.append(" FROM ");
		sb.append("boleto bol ");
			sb.append("inner join cedente c on c.id_cedente = bol.id_cedente ");
			sb.append("inner join agencia_bancaria ab on ab.id_agencia_bancaria = c.id_agencia_bancaria ");
			sb.append("inner join banco b on b.id_banco = ab.id_banco ");
			sb.append("inner join fatura fat on fat.id_fatura = bol.id_fatura ");		

		/** Caso a fatura não tenha mais de 12 documentos */
		if(tipo != 4){
			if(tipo == 3){
				sb.append("and fat.qt_documentos = 1 ");
			} else {
				sb.append("and fat.qt_documentos <= 12 ");
			}
			sb.append("inner join devedor_doc_serv_fat ddsf on ddsf.id_fatura = fat.id_fatura ");
			sb.append("inner join docto_servico ds on ds.id_docto_servico = ddsf.id_docto_servico ");

			if(tipo == 1){
				sb.append("and ds.tp_documento_servico not in ('CTR', 'NFT' , 'CTE' , 'NTE') ");
			} else {
				sb.append("and ds.tp_documento_servico in ('CTR', 'NFT' , 'CTE' , 'NTE') ");
			}

			sb.append("inner join filial filds on filds.id_filial = ds.id_filial_origem ");
				sb.append("left join desconto des on des.id_devedor_doc_serv_fat = ddsf.id_devedor_doc_serv_fat ");

			if( tipo == 2 || tipo == 3 ){
				sb.append("left join nota_fiscal_conhecimento nfc on nfc.id_conhecimento = ds.id_docto_servico ");
					sb.append("and exists  ( ");
					sb.append("									select 1 ");
					sb.append("									from nota_fiscal_conhecimento nfctmp ");
					sb.append("										inner join conhecimento ctmp on ctmp.id_conhecimento = nfctmp.id_conhecimento ");
					sb.append("									where ctmp.id_conhecimento = ds.id_docto_servico ");
					sb.append("									and nfc.nr_nota_fiscal = nfctmp.nr_nota_fiscal ");
					sb.append("								) ");
			}
		/** Caso a fatura tenha mais de 12 documentos, deve exibir apenas os dados da fatura */
		} else {
			sb.append("and fat.qt_documentos > 12 ");
			sb.append("inner join filial filfat on fat.id_filial = filfat.id_filial ");	
		}
	}

	/**
	 * Método responsável por montar a projection do Sub - SubReport
	 *  
	 * tipo == 1 - Docto de seviço não são conhecimento
	 * tipo == 2 - Docto de serviço é conhecimento e a fatura tem mais de um documento
	 * tipo == 3 - Docto de serviço é conhecimento e a fatura tem um documento
	 * tipo == 4 - A fatura tem mais de doze documentos
	 * 
	 * @param sql
	 * @param tipo
	 * 
	 */
	private void mountProjectionSubSubReport(StringBuilder sb, int tipo){
		sb.append(" SELECT ");

		/** Busca os dados do doctoServico */
		if(tipo != 4){
			sb.append("filds.sg_filial as FILIAL_DOCTO_SERVICO, ");
			sb.append("ds.nr_docto_servico as DOCTO_SERVICO, ");
			sb.append("ds.tp_documento_servico as TP_DOCTO_SERVICO, ");
			sb.append("ddsf.vl_devido as FRETE, ");
			sb.append("des.vl_desconto as DESCONTO, ");
			sb.append(" 'false' as MAIOR_DOZE, ");
		/** Busca os dados da fatura */
		} else {
			sb.append("filfat.sg_filial as FILIAL_DOCTO_SERVICO, ");
			sb.append("fat.nr_fatura  as DOCTO_SERVICO, ");
			sb.append(" null as TP_DOCTO_SERVICO, ");
			sb.append(" null as FRETE, ");
			sb.append(" null as DESCONTO, ");
			sb.append(" 'true' as MAIOR_DOZE, ");
		}

		if(tipo == 1 || tipo == 4) {
			sb.append(" null as NOTA_FISCAL ");
			
		} else {
			sb.append("to_char(min(nfc.nr_nota_fiscal)) as NOTA_FISCAL ");
			
		}
	}

	/**
	 * Método responsável por montar os filtros do Sub - SubReport
	 *  
	 * tipo == 1 - Docto de seviço não são conhecimento
	 * tipo == 2 - Docto de serviço é conhecimento e a fatura tem mais de um documento
	 * tipo == 3 - Docto de serviço é conhecimento e a fatura tem um documento
	 * tipo == 4 - A fatura tem mais de doze documentos
	 *  
	 * @param sql
	 * @param tipo
	 * 
	 */
	private void mountFilterSubSubReport(StringBuilder sb, int tipo){
		sb.append(" WHERE ");

		sb.append("bol.id_boleto = ? ");
		long hash = System.currentTimeMillis();
		sb.append("AND " + hash + " = " + hash);
		if(tipo == 2 || tipo == 3) {
			sb.append(" group by filds.sg_filial, ds.nr_docto_servico, ds.tp_documento_servico, ddsf.vl_devido, des.vl_desconto, 'false' ");
			
		}		
	}

	/**
	 * Método responsável por montar o sql do subRelatorio
	 * 
	 * @author HectorJ
	 * @since 16/06/2006
	 * 
	 * @param Map parameters
	 * 
	 */
	public StringBuilder getSqlTemplateSubSubReportBoleto(Long idBoleto, Long qtDocumentos){
		StringBuilder sb = new StringBuilder();

		/** Caso a fatura tenha mais de 12 documentos */
		if(qtDocumentos.longValue() > 12){
			/** A FATURA TEM MAIS DE DOZE DOCUMENTO */
			this.mountProjectionSubSubReport(sb, 4);
			this.mountFromSubSubReport(sb, 4);
			this.mountFilterSubSubReport(sb, 4);
		} else {
			/** DOCTO SERVICO NÃO É CONHECIMENTO */
			this.mountProjectionSubSubReport(sb, 1);
			this.mountFromSubSubReport(sb, 1);
			this.mountFilterSubSubReport(sb, 1);

			sb.append("\n UNION \n");

			/** DOCTO SERVICO É CONHECIMENTO E A FATURA TEM MAIS DE UM DOCUMENTO */
			this.mountProjectionSubSubReport(sb, 2);
			this.mountFromSubSubReport(sb, 2);
			this.mountFilterSubSubReport(sb, 2);

			sb.append("\n UNION \n");

			/** DOCTO SERVICO É CONHECIMENTO E A FATURA TEM UM DOCUMENTO */
			this.mountProjectionSubSubReport(sb, 3);
			this.mountFromSubSubReport(sb, 3);
			this.mountFilterSubSubReport(sb, 3);
		}
		return sb;
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * Retorna uma instância de <code>com.mercurio.adsm.framework.util.SqlTemplate</code>, configurada
	 * para utilizar o <code>MessageSource</code> do Spring Framework.
	 * @return
	 */
	public static SqlTemplate createSqlTemplate() {
		SqlTemplate sqlTemplate = new SqlTemplate();
	    sqlTemplate.setMessageSource(messageSource);
	    
        return sqlTemplate;
        
	}

	//------------------------------ Métodos relacionados a geração dos boletos (Report)
	
}
