package com.mercurio.lms.municipios.model.dao;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.municipios.model.Mcd;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class McdDAO extends BaseCrudDao<Mcd, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Mcd.class;
    }

    /**
     * Retorna o ultimo MCD gerado (ainda vigente) e ainda nao finalizado
     * @return
     */
    public Mcd findUltimoMcdVigente(){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("idMcd");
    	sql.addProjection("dtVigenciaInicial");
    	sql.addFrom(getPersistentClass().getName());    	
    	sql.addCustomCriteria("dtVigenciaInicial = (select max(dtVigenciaInicial) from Mcd)");
    	
    	List result = getAdsmHibernateTemplate().find(sql.getSql(true));
    	
    	Mcd mcd = new Mcd();
    	if (!result.isEmpty()){
    		Object[] obj = (Object[]) result.get(0);
    		mcd.setIdMcd((Long) obj[0]);
    		mcd.setDtVigenciaInicial((YearMonthDay)obj[1]);
    	}
    	
    	return mcd;   
    	
    }
   
    public List findIdMcdsFuturosByVigenciaInicial(YearMonthDay dtVigencia){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("m.id");
    	sql.addFrom(getPersistentClass().getName(), "m");
    	sql.addCriteria("m.dtVigenciaInicial", ">=", dtVigencia);
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria()); 	
    }
    
    public Mcd findMcdByVigenciaInicial(YearMonthDay dtVigencia){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("m");
    	sql.addFrom(getPersistentClass().getName(), "m");
    	sql.addCriteria("m.dtVigenciaInicial", "=", dtVigencia);
    	
    	return (Mcd)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(), sql.getCriteria()); 	
    }
    
    public List findMcdVigente(YearMonthDay dtVigencia){
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("m");
    	sql.addFrom(getPersistentClass().getName(), "m");
    	sql.addCriteria("m.dtVigenciaInicial", "<=", dtVigencia);
    	sql.addCriteria("m.dtVigenciaFinal", ">=", JTDateTimeUtils.maxYmd(dtVigencia));
    	
    	return getAdsmHibernateTemplate().find(sql.getSql(true), sql.getCriteria());
    }
    
    
    /**
     * Verifica fluxos não cadastrados no sistema.
     * Primeiramente consulta todos as filiais que atendem algum município com "BL_PADRAO_MCD" verdadeiro.
     * O atendimento deve estar vigente.
     * Após ter as filiais, realiza um plano cartesiano com as filiais em operação que atendem algum
     * município com "BL_PADRAO_MCD" verdadeiro e que estejam vigentes.
     * Todos os pares do plano cartesiano devem refletir em um Fluxo de Cargas de Filiais (uma tupla na tabela
     * FLUXO_FILIAL com fluxo de origem e destino de acordo com as filiais do par de cada linha do plano
     * cartesiano).
     *  
     * @param dtVigencia
     * @return List contendo um ou nenhum Object[] contendo nos índices:
     *   0: Long com id da filial de origem do primeiro fluxo inexistente.
     *   1: String com sigla da filial de origem do primeiro fluxo inexistente.
     *   2: Long com id da filial de destino do primeiro fluxo inexistente.
     *   3: String com sigla da filial de destino do primeiro fluxo inexistente. 
     */
    public List validateGeracaoFluxosInexistentes(final YearMonthDay dtVigencia) {
    	StringBuilder sql = new StringBuilder()
		    	.append("SELECT					")
		    	.append("	FA.ID_FILIAL AS ID_FILIAL_ORIGEM,		")
		    	.append("	FA.SG_FILIAL AS SG_FILIAL_ORIGEM,		")
		    	.append("	FO.ID_FILIAL AS ID_FILIAL_DESTINO,		")
		    	.append("	FO.SG_FILIAL AS SG_FILIAL_DESTINO		")
		    	.append("FROM					") 
	
		    	.append("(SELECT									")
		    	.append("	DISTINCT mf.id_filial AS ID_FILIAL,		")
		    	.append("	f.SG_filial AS SG_FILIAL				")
		    	.append("FROM										")
		    	.append("	municipio_filial mf,					")
		    	.append("	filial f								")
		    	.append("WHERE mf.id_filial = f.id_filial			")
		    	.append("  AND mf.bl_padrao_mcd = :blPadraoMcdValue	")
		    	.append("  AND mf.dt_vigencia_inicial <= :dtVigencia		")
		    	.append("  AND mf.dt_vigencia_final >= :dtVigencia			")
		    	.append(") FA,					")
	
		    	.append("(SELECT							")
		    	.append("	f.id_filial AS ID_FILIAL,		")
		    	.append("	f.SG_filial AS SG_FILIAL		")
		    	.append("FROM								")
		    	.append("	historico_filial hf,			")
		    	.append("	filial f						")
		    	.append("WHERE hf.dt_real_operacao_inicial <= :dtVigencia		")
		    	.append("  AND hf.dt_real_operacao_final >= :dtVigencia			")
		    	.append("  AND hf.id_filial = f.id_filial			")
		    	.append("  AND f.id_empresa IN (					")
		    	.append("	SELECT DISTINCT(f.id_empresa)			")
		    	.append("	FROM									")
		    	.append("		municipio_filial mf,				")
		    	.append("		filial f							")
		    	.append("	WHERE mf.id_filial = f.id_filial		")
		    	.append("	  AND bl_padrao_mcd = :blPadraoMcdValue	")
		    	.append("	  AND mf.dt_vigencia_inicial <= :dtVigencia		")
		    	.append("	  AND mf.dt_vigencia_final >= :dtVigencia		")
		    	.append("  )												")
				.append("  AND EXISTS (										")
				.append("    SELECT											")
				.append("      mf.id_filial									")
				.append("    FROM											")
				.append("      municipio_filial mf							")
				.append("    WHERE mf.id_filial = f.id_filial				")
				.append("      AND mf.bl_padrao_mcd = 'S'					")
				.append("      AND mf.dt_vigencia_inicial <= :dtVigencia	")
				.append("      AND mf.dt_vigencia_final >= :dtVigencia		")
				.append("  )		")
		    	.append(") FO		")
	
		    	.append("WHERE NOT EXISTS (									")
		    	.append("	SELECT * FROM FLUXO_FILIAL FF					")
		    	.append("	WHERE FF.ID_FILIAL_ORIGEM = FA.ID_FILIAL		")
		    	.append("	  AND FF.ID_FILIAL_DESTINO = FO.ID_FILIAL		")
		    	.append(")													");
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
    		
    		public void configQuery(org.hibernate.SQLQuery sqlQuery) {
    			sqlQuery
	    			.addScalar("ID_FILIAL_ORIGEM",Hibernate.LONG)
	    			.addScalar("SG_FILIAL_ORIGEM",Hibernate.STRING)
	    			.addScalar("ID_FILIAL_DESTINO",Hibernate.LONG)
	    			.addScalar("SG_FILIAL_DESTINO",Hibernate.STRING);
    		};
    		
    	};
    	    	
    	HashMap params = new HashMap();
    	params.put("blPadraoMcdValue","S");
    	params.put("dtVigencia",dtVigencia);
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),1,1,params,csq).getList();
    }
    
    /**
     * Verifica se existe frequência de operação de coleta.
     * Primeiramente consulta todos as filiais que atendem algum município com "BL_PADRAO_MCD" verdadeiro.
     * O atendimento deve estar vigente.
     * Após, verifica se há operação com serviço nulo cadastrada para todos os serviços.
     * A operação deve estar vigente e ser do tipo 'Coleta' ou 'Ambas'.
     * 
     * @param dtVigencia
     * @return List contendo um ou nenhum Object[] contendo nos índices:
     *   0: Long com id do primeiro município sem operação de coleta.
     *   1: String com nome do primeiro município sem operação de coleta. 
     */
    public List validateGeracaoMunicipiosSemOperacaoColeta(final YearMonthDay dtVigencia) {
    	StringBuilder sql = new StringBuilder()
    			.append("SELECT								")
				.append("  MF_SEM_COLETA.ID_MF_SEM_COLETA,	")
				.append("  M.NM_MUNICIPIO					")
				.append("FROM								")
				.append("  (SELECT   										")
				.append("    MF.ID_MUNICIPIO_FILIAL AS ID_MF_SEM_COLETA,	")
				.append("    MF.ID_MUNICIPIO								")
				.append("  FROM												")
				.append("    municipio_filial mf							")
				.append("  WHERE mf.bl_padrao_mcd = :blPadraoMcdValue		")
				.append("    AND mf.dt_vigencia_inicial <= :dtVigencia		")
				.append("    AND mf.dt_vigencia_final >= :dtVigencia		")
				.append("    AND   				")
				.append("      NOT EXISTS (		")
				.append("        SELECT *   	")
				.append("        FROM			")
				.append("          operacao_servico_localiza OSL							")
				.append("        WHERE OSL.ID_MUNICIPIO_FILIAL = MF.ID_MUNICIPIO_FILIAL		")
				.append("          AND OSL.dt_vigencia_inicial <= :dtVigencia				")
				.append("          AND OSL.dt_vigencia_final >= :dtVigencia					")
				.append("          AND (OSL.TP_OPERACAO = :tpAmbos OR OSL.TP_OPERACAO = :tpColeta)		")
				.append("          AND (			")
				.append("            NOT EXISTS (   ")
				.append("              SELECT ID_SERVICO FROM SERVICO   		")
				.append("              WHERE bl_gera_mcd = :blPadraoMcdValue		")
				.append("                AND ID_SERVICO NOT IN (				")
				.append("                  SELECT OSL2.ID_SERVICO FROM OPERACAO_SERVICO_LOCALIZA OSL2					")
				.append("                  WHERE OSL.ID_OPERACAO_SERVICO_LOCALIZA = OSL2.ID_OPERACAO_SERVICO_LOCALIZA   ")
				.append("                )									")
				.append("            )										")
				.append("            OR OSL.ID_SERVICO IS NULL				")
				.append("          )										")
				.append("      )											")
				.append("  )												")
				.append("MF_SEM_COLETA, MUNICIPIO M   						")
				.append("WHERE MF_SEM_COLETA.ID_MUNICIPIO = M.ID_MUNICIPIO	");

		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery
					.addScalar("ID_MF_SEM_COLETA",Hibernate.LONG)
					.addScalar("NM_MUNICIPIO",Hibernate.STRING);
			};
			
		};
		
		HashMap params = new HashMap();
    	params.put("dtVigencia",dtVigencia);		
    	params.put("tpAmbos","A");
    	params.put("tpColeta","C");
    	params.put("blPadraoMcdValue","S");
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),1,1,params,csq).getList();
    }
    
    /**
     * Verifica se existe frequência de operação de entrega.
     * Primeiramente consulta todos as filiais que atendem algum município com "BL_PADRAO_MCD" verdadeiro.
     * O atendimento deve estar vigente.
     * Após, verifica se há operação com serviço nulo cadastrada para todos os serviços.
     * A operação deve estar vigente e ser do tipo 'Entrega' ou 'Ambas'.
     * 
     * @param dtVigencia
     * @return List contendo um ou nenhum Object[] contendo nos índices:
     *   0: Long com id do primeiro município sem operação de entrega.
     *   1: String com nome do primeiro município sem operação de entrega. 
     */
    public List validateGeracaoMunicipiosSemOperacaoEntrega(final YearMonthDay dtVigencia) {
    	StringBuilder sql = new StringBuilder()
				.append("SELECT								")
				.append("  MF_SEM_ENTREGA.ID_MF_SEM_ENTREGA,	")
				.append("  M.NM_MUNICIPIO					")
				.append("FROM								")
				.append("  (SELECT   										")
				.append("    MF.ID_MUNICIPIO_FILIAL AS ID_MF_SEM_ENTREGA,	")
				.append("    MF.ID_MUNICIPIO								")
				.append("  FROM												")
				.append("    municipio_filial mf							")
				.append("  WHERE mf.bl_padrao_mcd = :blPadraoMcdValue		")
				.append("    AND mf.dt_vigencia_inicial <= :dtVigencia		")
				.append("    AND mf.dt_vigencia_final >= :dtVigencia		")
				.append("    AND   				")
				.append("      NOT EXISTS (		")
				.append("        SELECT *   	")
				.append("        FROM			")
				.append("          operacao_servico_localiza OSL							")
				.append("        WHERE OSL.ID_MUNICIPIO_FILIAL = MF.ID_MUNICIPIO_FILIAL		")
				.append("          AND OSL.dt_vigencia_inicial <= :dtVigencia				")
				.append("          AND OSL.dt_vigencia_final >= :dtVigencia					")
				.append("          AND (OSL.TP_OPERACAO = :tpAmbos OR OSL.TP_OPERACAO = :tpEntrega)		")
				.append("          AND (			")
				.append("            NOT EXISTS (   ")
				.append("              SELECT ID_SERVICO FROM SERVICO   		")
				.append("              WHERE bl_gera_mcd = :blPadraoMcdValue		")
				.append("                AND ID_SERVICO NOT IN (				")
				.append("                  SELECT OSL2.ID_SERVICO FROM OPERACAO_SERVICO_LOCALIZA OSL2					")
				.append("                  WHERE OSL.ID_OPERACAO_SERVICO_LOCALIZA = OSL2.ID_OPERACAO_SERVICO_LOCALIZA   ")
				.append("                )									")
				.append("            )										")
				.append("            OR OSL.ID_SERVICO IS NULL				")
				.append("          )										")
				.append("      )											")
				.append("  )												")
				.append("MF_SEM_ENTREGA, MUNICIPIO M   						")
				.append("WHERE MF_SEM_ENTREGA.ID_MUNICIPIO = M.ID_MUNICIPIO	");
		
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
				sqlQuery
					.addScalar("ID_MF_SEM_ENTREGA",Hibernate.LONG)
					.addScalar("NM_MUNICIPIO",Hibernate.STRING);
			};
			
		};
		    
		HashMap params = new HashMap();
    	params.put("dtVigencia",dtVigencia);		
    	params.put("tpAmbos","A");
    	params.put("tpEntrega","E");
    	params.put("blPadraoMcdValue","S");
		return getAdsmHibernateTemplate().findPaginatedBySql(sql.toString(),1,1,params,csq).getList();
    }
    
    /**
     * Retorna o MCD vigente.
     * O MCD vigente é aquele que possui a data de vigência final setada no 'infinito'.
     * @return Mcd uma instância de Mcd.
     */
    public Mcd findMcdVigente() {
    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("MCD");
    	sql.addFrom(Mcd.class.getName(),"MCD");
    	sql.addCustomCriteria("MCD.dtVigenciaFinal = ?",JTDateTimeUtils.MAX_YEARMONTHDAY);
    	
    	return (Mcd)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
    }
}