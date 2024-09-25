package com.mercurio.lms.configuracoes.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.contratacaoveiculos.model.BeneficiarioProprietario;
import com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class ContaBancariaDAO extends BaseCrudDao<ContaBancaria, Long>
{
    
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return ContaBancaria.class;
    }
    
    /**
     * Estes fetches são necessários para o funcionamento básico de
     * ContaBancaria e DadosBancarios (GT1).
     * Dessa forma, foi criado um findPaginated em ContaBancariaService, 
     * para filtrar e não trazer todos os dados de Pessoa, o que aumentaria
     * muito o volume de dados a trafegar por XML.
     * Favor não remover pessoa, pessoa.beneficiario, ou pessoa.proprietario 
     * sem antes verificar com o GT1.
     * Obrigado,
     * @author luisfco
     */
    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {    	
    	lazyFindPaginated.put("agenciaBancaria",FetchMode.JOIN);
    	lazyFindPaginated.put("agenciaBancaria.banco",FetchMode.JOIN);
    	lazyFindPaginated.put("agenciaBancaria.banco.pais",FetchMode.JOIN);
    }
    
    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("agenciaBancaria",FetchMode.JOIN);
    	lazyFindById.put("agenciaBancaria.banco",FetchMode.JOIN);
    	lazyFindById.put("agenciaBancaria.banco.pais",FetchMode.SELECT);
    	lazyFindById.put("pessoa",FetchMode.JOIN);
    	lazyFindById.put("pessoa.beneficiario",FetchMode.JOIN);
    	lazyFindById.put("pessoa.proprietario",FetchMode.JOIN);
    }	
	
    /**
     * Valida intervalo de vigência.<BR>
     * @param contaBancaria
     * @return
     */
    public boolean validateIntervaloVigencia(ContaBancaria contaBancaria){

    	SqlTemplate sql = new SqlTemplate();
    	sql.addProjection("count(cb.id)");
    	sql.addFrom(getPersistentClass().getName(), "cb");

    	StringBuffer criteria = new StringBuffer();
    	
    	if (contaBancaria.getDtVigenciaFinal() == null){
    		
    		//Verificações para intervalo aberto

    		//Verifica se existe intervalo com data maior que a data inicial informada
    		criteria.append(" (cb.dtVigenciaInicial >= ? or cb.dtVigenciaFinal >= ?) ");
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaInicial());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaInicial());

    	}else{
    		
    		//Verificações para intervalo fechado
    		
    		//verifica se intervalo informado esta contido em intervalo cadastrado
    		criteria.append("( ( (cb.dtVigenciaInicial <= ? and cb.dtVigenciaFinal >= ?) ");
    		criteria.append(" or (cb.dtVigenciaInicial <= ? and cb.dtVigenciaFinal >= ?) ) ");
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaInicial());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaInicial());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaFinal());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaFinal());
    		
    		//verifica se intervalos cadastrados são sub-conjuntos do intervalo informado
    		criteria.append(" or ( cb.dtVigenciaInicial between ? and ? or cb.dtVigenciaFinal between ? and ? ) )");
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaInicial());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaFinal());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaInicial());
    		sql.addCriteriaValue(contaBancaria.getDtVigenciaFinal());
    	}
    	
    	sql.addCustomCriteria(criteria.toString());
    	//Ignora a validação em cime do arquivo corrente, se alterando
    	sql.addCriteria("cb.id", "<>", contaBancaria.getIdContaBancaria());

    	//Não pode conincidir vigencias para a mesma pessoa.
    	sql.addCriteria("cb.pessoa.id", "=", contaBancaria.getPessoa().getIdPessoa());
    	
    	Number count = (Number) getAdsmHibernateTemplate().find(sql.getSql(), sql.getCriteria()).get(0);
    	return (count.intValue() <= 0);
    }


    /**
     * Verifica se existe outra ContaBancaria com a vigência final em aberto.<BR>
     *
     * @author Hector Julian Esnaola Junior
     * @since 15/12/2006
     *
     * @param contaBancaria
     * @return
     *
     */
    public boolean validateVigenciaEmAberto(ContaBancaria contaBancaria){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.count("id"));
    	dc.add(Restrictions.eq("pessoa.id", contaBancaria.getPessoa().getIdPessoa()));
    	
		dc.add(Restrictions.eq("dtVigenciaFinal", JTDateTimeUtils.maxYmd(contaBancaria.getDtVigenciaFinal())));
		
    	if (contaBancaria.getIdContaBancaria() != null){
    		dc.add(Restrictions.ne("id", contaBancaria.getIdContaBancaria()));
    	}
    	Integer count = (Integer) findByDetachedCriteria(dc).get(0);
    	return (count.intValue() <= 0);
    }
    
    /**
     * Verifica se a vigencia final da Conta Bancaria esta igual ao dado no banco.<BR>
     * @param idContaBancaria
     * @return true se data da vigencia final for igual a informada
     */
    public boolean validateEqualsVigenciaFinal(ContaBancaria contaBancaria){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.setProjection(Projections.count("id"));
    	dc.add(Restrictions.eq("id", contaBancaria.getIdContaBancaria()));
    	if (contaBancaria.getDtVigenciaFinal() == null){
    		dc.add(Restrictions.eq("dtVigenciaFinal", JTDateTimeUtils.MAX_YEARMONTHDAY));
    	}else{
    		dc.add(Restrictions.eq("dtVigenciaFinal", contaBancaria.getDtVigenciaFinal()));
    	}
    	
    	Integer count = (Integer) findByDetachedCriteria(dc).get(0);
    	return (count.intValue() > 0);
    }
    
    /**
     * Retorna as contas bancárias de uma pessoa.
     * @param idPessoa
     * @return List de contas bancáriasda pessoa.
     * @author luisfco
     */
    public List findContasBancariasByPessoa(Long idPessoa) {
        StringBuffer hqlFindcontaBancariaPessoa = new StringBuffer()
			.append(" select cb from ContaBancaria cb ")
			.append(" join fetch cb.pessoa pes ")
			.append(" join fetch cb.agenciaBancaria ab ")
			.append(" join fetch ab.banco ")
			.append(" where pes.idPessoa = ? ");
    	return getAdsmHibernateTemplate().find(hqlFindcontaBancariaPessoa.toString(), idPessoa);
    }

	
    /**
     * Esta query será utilizada em dois métodos abaixo
     * @author luisfco
     */
	static final StringBuffer hqlFindContaBancariaVigentePessoa = new StringBuffer()
		.append(" from ContaBancaria cb")
		.append(" join fetch cb.pessoa pes ")
		.append(" where pes.idPessoa = ? ")
		.append(" and cb.dtVigenciaInicial <= ? ") 
		.append(" and cb.dtVigenciaFinal >= ?  ");

	
	
	/**
	 * Retorna as contas bancarias vigentes de uma pessoa.
	 * @param idPessoa 
	 * @return As contas bancárias vigentes da pessoa
	 * @author luisfco
	 */
    public List findContasBancariasVigentesByPessoa(Long idPessoa) {
    	StringBuffer hql = hqlFindContaBancariaVigentePessoa;
    	YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
    		
    	return  getAdsmHibernateTemplate().find( hql.toString(), new Object[]{idPessoa,dtAtual, dtAtual } );
    }
	
    /**
     * Verifica se existe alguma conta bancária vigente para a pessoa.
     * @param idPessoa
     * @return True, em caso afirmativo.
     * @author luisfco
     */
    public boolean existeContaBancariaVigentePessoa(Long idPessoa) {
    	YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();
    	Number n = (Number) getAdsmHibernateTemplate().getRowCountForQuery(hqlFindContaBancariaVigentePessoa.toString(), 
    			new Object[]{ idPessoa, dtAtual, dtAtual } );    	
    	return (n.longValue() > 0);
    }
    
    public boolean existeContaBancariaVigentePeriodo(Long idPessoa, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	DetachedCriteria dc = createDetachedCriteria();

    	dc.setProjection(Projections.rowCount());
    	dc.add(Restrictions.eq("pessoa.idPessoa", idPessoa));
    	JTVigenciaUtils.getDetachedVigencia(dc, dtVigenciaInicial, dtVigenciaFinal);    	

    	Integer result = (Integer) getAdsmHibernateTemplate().findUniqueResult(dc);
    	return (result > 0);
    }
    
    /**
     * Verifica a existencia de se o proprietário possui situacao 
     * incompleta (tpSituacao = N) bem como uma liberacao vigente  
     * @param idProprietario
     * @return True em caso afirmativo.
     * @author luisfco
     */
    public boolean existeProprietarioIncompletoComLiberacaoVigente(Long idProprietario) {
    	StringBuffer hql = new StringBuffer()
    		.append(" from " + LiberacaoReguladora.class.getName() + " lib")
    		.append(" left join lib.proprietario pro")
    		.append(" where pro.idProprietario = ?")    		
    		.append(" and pro.tpSituacao like ?")
			.append(" and lib.dtLiberacao <= ? ") 
			.append(" and (lib.dtVencimento >= ? or lib.dtVencimento = ?) ")
			;
    	Object[] criterios = new Object[]{
    			idProprietario,
    			"N",
    			JTDateTimeUtils.getDataAtual(),
    			JTDateTimeUtils.getDataAtual(),
    			JTDateTimeUtils.MAX_YEARMONTHDAY
    	};
    	Number num = (Number) getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), criterios);
    	return num.longValue() > 0 ;
    }
    
    /**
     * Busca todos os proprietarios de situacao incompleta (tpSituacao = N)
     * e possuidores de liberacao vigente, associados a um beneficiario específico  
     * @param idBeneficiario
     * @return List de Proprietario.
     * @author luisfco
     */
    public List findProprietariosIncompletosDoBeneficiarioComLiberacaoVigente(Long idBeneficiario) {
    	DetachedCriteria dc = DetachedCriteria.forClass(BeneficiarioProprietario.class)
    		.createAlias("proprietario", "pro")
    		.createAlias("beneficiario", "ben")
    		
    		.createAlias("pro.liberacaoReguladoras", "lib")

    		.setProjection(Projections.alias(Projections.property("proprietario"), "pro"))
	    	
    		.add(Restrictions.eq("ben.idBeneficiario", idBeneficiario))
	    	.add(Restrictions.eq("pro.tpSituacao", "N"))
	    	
	    	.add(Restrictions.le("dtVigenciaInicial", JTDateTimeUtils.getDataAtual() ))
	    	.add(Restrictions.or(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()),
	    							Restrictions.eq("dtVigenciaFinal", JTDateTimeUtils.MAX_YEARMONTHDAY)
	    			))
	    	.add(Restrictions.le("lib.dtLiberacao", JTDateTimeUtils.getDataAtual()))    			
	    	.add(Restrictions.or(Restrictions.ge("lib.dtVencimento", JTDateTimeUtils.getDataAtual() ),
								Restrictions.eq("lib.dtVencimento", JTDateTimeUtils.MAX_YEARMONTHDAY)    			
	    			));
    	return findByDetachedCriteria(dc);
    	
    }

    /**
     * Verifica se a pessoa em questão é um proprietário, caso afirmativo verifica se existe um 
     * beneficiário vigente no período informado. Existindo apresenta mensagem de erro
     * @param idPessoa Identificador da pessoa a qual queremos salvar os dados bancários
     * @param dtVigenciaInicial Data Inicial de vigência da conta bancária
     * @param dtVigenciaFinal Data Final de vigência da conta bancária
     * @return booleano indicando a existência ou não de beneficiários vigentes no período informado
     */
    public boolean existeProprietarioAssociadoEBeneficiarioVigente(Long idPessoa, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
        
        SqlTemplate sql = new SqlTemplate();
        
        sql.addProjection("pro");
        sql.addFrom(Proprietario.class.getName() + " pro ");
        sql.addCriteria("pro.id","=",idPessoa);
        
        List retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
        
        if( retorno == null || retorno.isEmpty() ){
            return true;
        }
        
        sql = new SqlTemplate();
        
        sql.addProjection("bp");
        
        sql.addFrom(BeneficiarioProprietario.class.getName() + " as bp " +
                    "   join bp.proprietario pro ");

        sql.addCriteria("pro.idProprietario","=", idPessoa);

        if (dtVigenciaFinal == null){
        	dtVigenciaFinal = JTDateTimeUtils.MAX_YEARMONTHDAY;
        }
        
        sql.addCustomCriteria( new StringBuffer()
        	.append(" (? between bp.dtVigenciaInicial and bp.dtVigenciaFinal ")
        	.append(" or ? between bp.dtVigenciaInicial and bp.dtVigenciaFinal ")
        	.append(" or bp.dtVigenciaInicial >= ? )")
        	.toString()
        );
        sql.addCriteriaValue(dtVigenciaInicial);
        sql.addCriteriaValue(dtVigenciaFinal);
        sql.addCriteriaValue(dtVigenciaInicial);

        retorno = getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());     
        
        return (retorno == null || retorno.isEmpty() );
    }
    
    public boolean findDadosBancariosByFilialCiaArea(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
    	DetachedCriteria dc = createDetachedCriteria();
    	dc.add(Restrictions.eq("pessoa.idPessoa",idFilialCiaAerea));
    	dc.add(Restrictions.or(
    			Restrictions.or(
	    			Restrictions.and(
	    					Restrictions.lt("dtVigenciaInicial",dataInicio),
	    					Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim))),
	    			Restrictions.and(
	    	    					Restrictions.gt("dtVigenciaInicial",dataInicio),
	    	    					Restrictions.gt("dtVigenciaFinal",JTDateTimeUtils.maxYmd(dataFim)))),
	    	   Restrictions.or(
	    			   Restrictions.lt("dtVigenciaInicial",dataInicio),
	    			   Restrictions.gt("dtVigenciaFinal",dataFim))));
		return findByDetachedCriteria(dc).size()>0;
	}
    
    /**
     * Retorna a lista de todas as contas bancarias vigente.
     * 
     * @author Mickaël Jalbert
     * @since 24/08/2006
     * 
     * @param Long idPessoa
     * @param YearMonthDay dtVigencia
     * @return List
     */
    public List findByPessoa(Long idPessoa, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("cb");
    	
    	hql.addInnerJoin(ContaBancaria.class.getName(), "cb");
    	
    	hql.addCriteria("cb.pessoa.id", "=", idPessoa);
    	
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "cb.dtVigenciaInicial", "cb.dtVigenciaFinal", dtVigencia);
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }    
    
    /**
     * Retorna as contas bancárias vigentes da pessoa, exceto a conta em questão
     *
     * @author Hector Julian Esnaola Junior
     * @since 29/09/2006
     *
     * @param idPessoa
     * @param dtVigencia
     * @return
     *
     */
    public List findContaBancariaVigenteByPessoa(Long idPessoa, Long idContaBancaria, YearMonthDay dtVigencia){
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("cb");
    	
    	hql.addInnerJoin(ContaBancaria.class.getName(), "cb");
    	
    	hql.addCriteria("cb.pessoa.id", "=", idPessoa);
    	hql.addCriteria("cb.id", "<>", idContaBancaria);
    	
    	JTVigenciaUtils.getHqlVigenciaNotNull(hql, "cb.dtVigenciaInicial", "cb.dtVigenciaFinal", dtVigencia);
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    }    
    
    /**
     * Carrega ContaBancaria de acordo com idPessoa, dtVigenciaInicial e dtVigenciaFinal.
     *
     * @author Hector Julian Esnaola Junior
     * @since 26/10/2007
     *
     * @param idPessoa
     * @param dtVigenciaInicial
     * @param dtVigenciaFinal
     * @return
     *
     */
    public ContaBancaria findContaBancaria(
    		Long idPessoa, 
    		YearMonthDay dtVigenciaInicial, 
    		YearMonthDay dtVigenciaFinal) {
    	
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("cb");
    	hql.addInnerJoin(ContaBancaria.class.getName(), "cb");
    	hql.addCriteria("cb.pessoa.id", "=", idPessoa);
    	hql.addCriteria("cb.dtVigenciaInicial", "=", dtVigenciaInicial);
    	hql.addCriteria("cb.dtVigenciaFinal", "=", dtVigenciaFinal);
    	
    	return (ContaBancaria)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }

	public boolean findIsContaBancariaProprietarioOuPostoConveniado(Long idContaBancaria) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select 1 from conta_bancaria ")
		.append(" where ( exists (select 1 from proprietario where conta_bancaria.id_pessoa = proprietario.id_proprietario) ")
		.append(" or exists (select 1 from POSTO_CONVENIADO where conta_bancaria.id_pessoa = POSTO_CONVENIADO.id_POSTO_CONVENIADO))")
		.append("  and conta_bancaria.id_conta_bancaria = ? ");
		
		return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(), new Object[]{idContaBancaria}) > 0 ;
}

	public List<Map<String, Object>> findDadosBancariosPessoa(Long idPessoa) {

		Validate.notNull(idPessoa, "idPessoa cannot be null");
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("distinct new Map( cb.idContaBancaria as idContaBancaria ");
		hql.addProjection("ba.nmBanco as nmBanco ");
		hql.addProjection("ab.nrAgenciaBancaria as nrAgenciaBancaria ");
		hql.addProjection("cb.nrContaBancaria || '-' || cb.dvContaBancaria as nrContaDvConta ");
		hql.addProjection("cb.tpConta as tpConta ");
		hql.addProjection("cb.dtVigenciaFinal as dtVigenciaFinal ");
		hql.addProjection("cb.dtVigenciaInicial as dtVigenciaInicial)  ");
		hql.addFrom(ContaBancaria.class.getName() + " cb " +
				" JOIN cb.agenciaBancaria ab " +
				" JOIN ab.banco ba " );

		hql.addCriteria("cb.pessoa.idPessoa", "=", idPessoa);

		List<Map<String,Object>> list = getHibernateTemplate().find(hql.getSql(), hql.getCriteria());
		
		return list;
	}
}
	