package com.mercurio.lms.municipios.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RegionalFilial;
import com.mercurio.lms.municipios.model.SubstAtendimentoFilial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 * <p>
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 *
 * @spring.bean 
 */
public class SubstAtendimentoFilialDAO extends BaseCrudDao<SubstAtendimentoFilial, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	
    public static final int UF = 0;
    public static final int REGIONAL = 1;
    public static final int CLIENTE = 2;
    public static final int FILIAL = 3;
    public static final int FILIAL_SUBST = 4;
    public static final int FILIAL_SUBST_ONLY = 5;
	
    private static final String SAF_DT_VIGENCIA_INICIAL = "SAF.dtVigenciaInicial";
    private static final String SAF_DT_VIGENCIA_FINAL = "SAF.dtVigenciaFinal";
	
    protected final Class getPersistentClass() {
        return SubstAtendimentoFilial.class;
    }

    protected void initFindPaginatedLazyProperties(Map fetchMode) {
    	fetchMode.put("filialByIdFilialDestino",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialDestino.pessoa",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialOrigem",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		fetchMode.put("filialByIdFilialDestinoSubstituta",FetchMode.JOIN);
		fetchMode.put("filialByIdFilialDestinoSubstituta.pessoa",FetchMode.JOIN);
    	fetchMode.put("unidadeFederativa",FetchMode.JOIN);
		fetchMode.put("regional",FetchMode.JOIN);
		fetchMode.put("municipio",FetchMode.JOIN);
		fetchMode.put("servico",FetchMode.JOIN);
		fetchMode.put("naturezaProduto",FetchMode.JOIN);
		fetchMode.put("cliente",FetchMode.JOIN);
    	super.initFindPaginatedLazyProperties(fetchMode);
    }

    protected void initFindByIdLazyProperties(Map fetchMode) {
    	fetchMode.put("filialByIdFilialDestino",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialDestino.pessoa",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialOrigem",FetchMode.JOIN);
    	fetchMode.put("filialByIdFilialOrigem.pessoa",FetchMode.JOIN);
		fetchMode.put("filialByIdFilialDestinoSubstituta",FetchMode.JOIN);
		fetchMode.put("filialByIdFilialDestinoSubstituta.pessoa",FetchMode.JOIN);
    	fetchMode.put("unidadeFederativa",FetchMode.JOIN);
		fetchMode.put("regional",FetchMode.JOIN);
		fetchMode.put("municipio",FetchMode.JOIN);
		fetchMode.put("servico",FetchMode.JOIN);
		fetchMode.put("naturezaProduto",FetchMode.JOIN);
		fetchMode.put("cliente",FetchMode.JOIN);
		super.initFindByIdLazyProperties(fetchMode);
    }

    public boolean isVigenciaValida(SubstAtendimentoFilial saf) {
    	DetachedCriteria dc = createDetachedCriteria();
        if (saf.getIdSubstAtendimentoFilial() != null) {
    		dc.add(Restrictions.ne("idSubstAtendimentoFilial",saf.getIdSubstAtendimentoFilial()));
        }
    	dc.add(Restrictions.eq("filialByIdFilialDestino.idFilial",saf.getFilialByIdFilialDestino().getIdFilial()));
    	dc.add(Restrictions.eq("filialByIdFilialDestinoSubstituta.idFilial",saf.getFilialByIdFilialDestinoSubstituta().getIdFilial()));
        if (saf.getFilialByIdFilialOrigem() != null && saf.getFilialByIdFilialOrigem().getIdFilial() != null) {
    		dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial",saf.getFilialByIdFilialOrigem().getIdFilial()));
        }
    	
    	JTVigenciaUtils.getDetachedVigencia(dc,saf.getDtVigenciaInicial(),saf.getDtVigenciaInicial());
    	List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
        return rs == null || rs.isEmpty();
    }
    
    /**
     * Verifica se o cliente possui endereco na UF de origem
     *
     * @param saf
     * @return TRUE se o cliente possui endereco, FALSE caso contrario
     */    
    public boolean verificaRegistroEndereco(SubstAtendimentoFilial saf){
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(EnderecoPessoa.class);
    	
    	dc.createAlias("municipio", "m");
    	
    	dc.add( Restrictions.eq("m.unidadeFederativa.idUnidadeFederativa", saf.getUnidadeFederativa().getIdUnidadeFederativa()));
    	dc.add( Restrictions.eq("pessoa.idPessoa", saf.getCliente().getIdCliente()));    	
    	dc.setProjection(Projections.count("id"));
    	
    	List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);    	
    	Integer count = (Integer)rs.get(0);
    	
    	return count.intValue() > 0;
    }
    
    /**
     * Verifica se a filial de operacao do cliente eh atendida pela regional de origem
     *
     * @param saf
     * @return TRUE se a filial eh atendida, FALSE caso contrario
     */    
    public boolean verificaRegionalCliente(SubstAtendimentoFilial saf){
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(RegionalFilial.class);
    	
    	dc.createAlias("filial", "fil");
    	dc.createAlias("fil.clientesByIdFilialAtendeOperacional", "filCli");
    	
    	dc.add(Restrictions.eq("filCli.idCliente", saf.getCliente().getIdCliente()));
    	dc.add(Restrictions.eq("regional.idRegional", saf.getRegional().getIdRegional()));
    	
    	dc.add(Restrictions.le("dtVigenciaInicial", saf.getDtVigenciaInicial()));
    	
    	if (saf.getDtVigenciaFinal() != null){
    		dc.add( Restrictions.or(
    					Restrictions.isNull("dtVigenciaFinal"),
    					Restrictions.ge("dtVigenciaFinal", saf.getDtVigenciaFinal())
    				));    		
        } else {
    		dc.add(Restrictions.isNull("dtVigenciaFinal"));
        }
		
    	
    	dc.setProjection(Projections.count("id"));
    	
    	List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);    	
    	Integer count = (Integer)rs.get(0);
    	
    	return count.intValue() > 0;
    }
    
    /**
     * Verifica se a filial do cliente eh atendida pela filial de origem
     *
     * @param saf
     * @return TRUE se a filial eh atendida, FALSE caso contrario
     */
    public boolean verificaFilialCliente(SubstAtendimentoFilial saf){ 
    	
    	DetachedCriteria dc = DetachedCriteria.forClass(Filial.class);
    	
    	dc.createAlias("clientesByIdFilialAtendeOperacional", "cli");
    	
    	dc.add(Restrictions.eq("idFilial", saf.getFilialByIdFilialOrigem().getIdFilial()));
    	dc.add(Restrictions.eq("cli.idCliente", saf.getCliente().getIdCliente()));
    	
    	dc.setProjection(Projections.count("id"));
    	
    	List rs = getAdsmHibernateTemplate().findByDetachedCriteria(dc);    	
    	Integer count = (Integer)rs.get(0);
    	
    	return count.intValue() > 0;
    }
    
    //verifica se existe um mesmo registro vigente
    public boolean findSubstAtendimentoVigente(SubstAtendimentoFilial substAtendimentoFilial){
    	DetachedCriteria dc = createDetachedCriteria();
        if (substAtendimentoFilial.getIdSubstAtendimentoFilial() != null) {
    		dc.add(Restrictions.ne("idSubstAtendimentoFilial",substAtendimentoFilial.getIdSubstAtendimentoFilial()));
        }
    	dc.add(Restrictions.eq("filialByIdFilialDestino.idFilial", substAtendimentoFilial.getFilialByIdFilialDestino().getIdFilial()));
    	dc.add(Restrictions.eq("filialByIdFilialDestinoSubstituta.idFilial",substAtendimentoFilial.getFilialByIdFilialDestinoSubstituta().getIdFilial()));
        if (substAtendimentoFilial.getUnidadeFederativa() == null) {
    		dc.add(Restrictions.isNull("unidadeFederativa.idUnidadeFederativa"));
        } else {
    		dc.add(Restrictions.eq("unidadeFederativa.idUnidadeFederativa",substAtendimentoFilial.getUnidadeFederativa().getIdUnidadeFederativa()));
        }
        if (substAtendimentoFilial.getRegional() == null) {
    		dc.add(Restrictions.isNull("regional.idRegional"));
        } else {
    		dc.add(Restrictions.eq("regional.idRegional",substAtendimentoFilial.getRegional().getIdRegional()));
        }
        if (substAtendimentoFilial.getFilialByIdFilialOrigem() == null) {
    		dc.add(Restrictions.isNull("filialByIdFilialOrigem.idFilial"));
        } else {
    		dc.add(Restrictions.eq("filialByIdFilialOrigem.idFilial",substAtendimentoFilial.getFilialByIdFilialOrigem().getIdFilial()));
        }
        if (substAtendimentoFilial.getMunicipio() == null) {
    		dc.add(Restrictions.isNull("municipio.idMunicipio"));
        } else {
    		dc.add(Restrictions.eq("municipio.idMunicipio",substAtendimentoFilial.getMunicipio().getIdMunicipio()));
        }
        if (substAtendimentoFilial.getServico() == null) {
    		dc.add(Restrictions.isNull("servico.idServico"));
        } else {
    		dc.add(Restrictions.eq("servico.idServico",substAtendimentoFilial.getServico().getIdServico()));
        }
        if (substAtendimentoFilial.getNaturezaProduto() == null) {
    		dc.add(Restrictions.isNull("naturezaProduto.idNaturezaProduto"));
        } else {
    		dc.add(Restrictions.eq("naturezaProduto.idNaturezaProduto",substAtendimentoFilial.getNaturezaProduto().getIdNaturezaProduto()));
        }
        if (substAtendimentoFilial.getCliente() == null) {
    		dc.add(Restrictions.isNull("cliente.idCliente"));
        } else {
    		dc.add(Restrictions.eq("cliente.idCliente",substAtendimentoFilial.getCliente().getIdCliente()));
        }
    	dc = JTVigenciaUtils.getDetachedVigencia(dc,substAtendimentoFilial.getDtVigenciaInicial(),substAtendimentoFilial.getDtVigenciaFinal());
    	
        return !findByDetachedCriteria(dc).isEmpty();
    }
    
    public Integer getRowCountBy(Long idSubstAtendimentoFilial, Long idFilialDestino, Long id, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, int option) {
        StringBuilder sql = null;
    	if (option == SubstAtendimentoFilialDAO.CLIENTE) {
    		sql = createSqlByCliente();
    	}else if (option == SubstAtendimentoFilialDAO.FILIAL) {
    		sql = createSqlByFilial();
    	}else if (option == SubstAtendimentoFilialDAO.REGIONAL) {
    		sql = createSqlByRegional();
    	}else if (option == SubstAtendimentoFilialDAO.UF) {
    		sql = createSqlByUF();
    	}else if (option == SubstAtendimentoFilialDAO.FILIAL_SUBST_ONLY) {
            sql = new StringBuilder("SAF.ID_FILIAL_DESTINO_SUBSTITUTA = :id AND SAF.ID_CLIENTE IS NULL AND SAF.ID_FILIAL_ORIGEM IS NULL AND SAF.ID_UNIDADE_FEDERATIVA IS NULL AND SAF.ID_REGIONAL IS NULL AND SAF.ID_NATUREZA_PRODUTO IS NULL AND ");
    	}else if (option == SubstAtendimentoFilialDAO.FILIAL_SUBST) {
            sql = new StringBuilder("SAF.ID_FILIAL_DESTINO_SUBSTITUTA = :id AND ");
    	}
    	Map criteria = new HashMap();
    	criteria.put("id",id);
    	criteria.put("dataBase",dtVigenciaInicial);
    	criteria.put("dtI",dtVigenciaInicial);
    	criteria.put("dtF",dtVigenciaFinal);
    	
    	sql.insert(0,"FROM (SELECT ID_SUBST_ATENDIMENTO_FILIAL FROM SUBST_ATENDIMENTO_FILIAL SAF WHERE \n");
    	
    	if (idSubstAtendimentoFilial != null) {
    	 	sql.append("SAF.ID_SUBST_ATENDIMENTO_FILIAL <> :idSubst AND ");
    		criteria.put("idSubst",idSubstAtendimentoFilial);
    	}
    	
    	sql.append("SAF.ID_FILIAL_DESTINO = :idDest AND ");
    	criteria.put("idDest",idFilialDestino);
    	
        if (dtVigenciaFinal == null) {
    		sql.append("(SAF.DT_VIGENCIA_INICIAL <= :dtI AND SAF.DT_VIGENCIA_FINAL >= :dtI OR SAF.DT_VIGENCIA_INICIAL > :dtI)");
        } else {
    		sql.append("((SAF.DT_VIGENCIA_INICIAL <= :dtI AND SAF.DT_VIGENCIA_FINAL >= :dtI)")
    		.append(" OR (SAF.DT_VIGENCIA_INICIAL >= :dtI AND SAF.DT_VIGENCIA_INICIAL <= :dtF))");
    	}
    		
    	sql.append(")");
    	return getAdsmHibernateTemplate().getRowCountBySql(sql.toString(),criteria);
    }

    public List findVigenteEm(YearMonthDay dtConsulta, Long idFilialDestino, DomainValue tpDesvioCarga) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("SAF");
    	hql.addFrom(new StringBuffer(getPersistentClass().getName()).append(" AS SAF ")
    					.append("INNER JOIN SAF.filialByIdFilialDestino AS F ").toString());
        hql.addCriteria(SAF_DT_VIGENCIA_INICIAL, "<=", dtConsulta);
        hql.addCriteria(SAF_DT_VIGENCIA_FINAL, ">=", dtConsulta);
    	hql.addCriteria("F.id","=",idFilialDestino);
    	if(tpDesvioCarga != null){
    		hql.addCriteria("SAF.tpDesvioCarga","=",tpDesvioCarga.getValue());
    	}

    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria());
    }

    public boolean findRegionalFilial(Long idRegional, Long idFilial, YearMonthDay dtConsulta) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("count(*)");
    	hql.addFrom(RegionalFilial.class.getName(),"RF");
    	hql.addCriteria("RF.filial.id","=",idFilial);
    	hql.addCriteria("RF.regional.id","=",idRegional);
    	hql.addCriteria("RF.dtVigenciaInicial","<=",dtConsulta);
    	hql.addCriteria("RF.dtVigenciaFinal",">=",dtConsulta);

    	Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(),hql.getCriteria());
        return result.intValue() > 0;
    }

    private StringBuilder createSqlByUF() {
        return new StringBuilder("(SAF.ID_CLIENTE IN (SELECT DISTINCT ID_CLIENTE FROM CLIENTE C \n")
    		.append("INNER JOIN FILIAL F ON C.ID_FILIAL_ATENDE_OPERACIONAL = F.ID_FILIAL \n")
    		.append("INNER JOIN ENDERECO_PESSOA EP ON EP.ID_PESSOA = F.ID_FILIAL AND EP.DT_VIGENCIA_INICIAL <= :dataBase AND EP.DT_VIGENCIA_FINAL >= :dataBase \n")
    		.append("INNER JOIN MUNICIPIO M ON M.ID_MUNICIPIO = EP.ID_MUNICIPIO \n")
    		.append("WHERE M.ID_UNIDADE_FEDERATIVA = :id) OR \n")
    		.append("SAF.ID_FILIAL_ORIGEM IN (SELECT DISTINCT ID_FILIAL FROM FILIAL F \n")
    		.append("INNER JOIN ENDERECO_PESSOA EP ON EP.ID_PESSOA = F.ID_FILIAL AND EP.DT_VIGENCIA_INICIAL <= :dataBase AND EP.DT_VIGENCIA_FINAL >= :dataBase \n")
    		.append("INNER JOIN MUNICIPIO M ON M.ID_MUNICIPIO = EP.ID_MUNICIPIO \n")
    		.append("WHERE M.ID_UNIDADE_FEDERATIVA = :id) OR \n")
    		.append("SAF.ID_REGIONAL IN (SELECT DISTINCT R.ID_REGIONAL FROM REGIONAL R \n")
    		.append("INNER JOIN REGIONAL_FILIAL RF ON RF.ID_REGIONAL = R.ID_REGIONAL AND  RF.DT_VIGENCIA_INICIAL <= :dataBase AND RF.DT_VIGENCIA_FINAL >= :dataBase \n")
    		.append("INNER JOIN ENDERECO_PESSOA EP ON EP.ID_PESSOA = RF.ID_FILIAL AND EP.DT_VIGENCIA_INICIAL <= :dataBase AND EP.DT_VIGENCIA_FINAL >= :dataBase \n")
    		.append("INNER JOIN MUNICIPIO M ON M.ID_MUNICIPIO = EP.ID_MUNICIPIO \n")
    		.append("WHERE M.ID_UNIDADE_FEDERATIVA = :id)) AND \n");
    }
    
    private StringBuilder createSqlByFilial() {
        return new StringBuilder("(SAF.ID_CLIENTE IN (SELECT DISTINCT ID_CLIENTE FROM CLIENTE C \n")
    			.append("WHERE C.ID_FILIAL_ATENDE_OPERACIONAL = :id) OR \n")
    			.append("SAF.ID_UNIDADE_FEDERATIVA IN (SELECT DISTINCT M.ID_UNIDADE_FEDERATIVA FROM MUNICIPIO M \n")
    			.append("INNER JOIN ENDERECO_PESSOA EP ON EP.ID_MUNICIPIO = M.ID_MUNICIPIO AND EP.DT_VIGENCIA_INICIAL <= :dataBase AND EP.DT_VIGENCIA_FINAL >= :dataBase \n")
    			.append("WHERE EP.ID_PESSOA = :id) OR \n")
    			.append("SAF.ID_REGIONAL IN (SELECT DISTINCT R.ID_REGIONAL FROM REGIONAL R \n")
    			.append("INNER JOIN REGIONAL_FILIAL RF ON RF.ID_REGIONAL = R.ID_REGIONAL AND  RF.DT_VIGENCIA_INICIAL <= :dataBase AND RF.DT_VIGENCIA_FINAL >= :dataBase \n")
    			.append("WHERE RF.ID_FILIAL = :id)) AND \n");
    }
    
    private StringBuilder createSqlByRegional() {
        return new StringBuilder("(SAF.ID_CLIENTE IN (SELECT DISTINCT ID_CLIENTE FROM CLIENTE C \n")
    			.append("INNER JOIN REGIONAL_FILIAL RF ON C.ID_FILIAL_ATENDE_OPERACIONAL = RF.ID_FILIAL AND  RF.DT_VIGENCIA_INICIAL <= :dataBase AND RF.DT_VIGENCIA_FINAL >= :dataBase \n")
    			.append("WHERE RF.ID_REGIONAL = :id) OR \n")
    			.append("SAF.ID_FILIAL_ORIGEM IN (SELECT DISTINCT F.ID_FILIAL FROM FILIAL F \n")
    			.append("INNER JOIN REGIONAL_FILIAL RF ON F.ID_FILIAL = RF.ID_FILIAL AND  RF.DT_VIGENCIA_INICIAL <= :dataBase AND RF.DT_VIGENCIA_FINAL >= :dataBase \n")
    			.append("WHERE RF.ID_REGIONAL = :id) OR \n")
    			.append("SAF.ID_UNIDADE_FEDERATIVA IN (SELECT DISTINCT M.ID_UNIDADE_FEDERATIVA FROM REGIONAL R \n")
    			.append("INNER JOIN REGIONAL_FILIAL RF ON RF.ID_REGIONAL = R.ID_REGIONAL AND  RF.DT_VIGENCIA_INICIAL <= :dataBase AND RF.DT_VIGENCIA_FINAL >= :dataBase \n")
    			.append("INNER JOIN ENDERECO_PESSOA EP ON EP.ID_PESSOA = RF.ID_FILIAL AND EP.DT_VIGENCIA_INICIAL <= :dataBase AND EP.DT_VIGENCIA_FINAL >= :dataBase \n")
    			.append("INNER JOIN MUNICIPIO M ON M.ID_MUNICIPIO = EP.ID_MUNICIPIO \n")
    			.append("WHERE R.ID_REGIONAL = :id))  AND \n");
    }
    
    private StringBuilder createSqlByCliente() {
        return new StringBuilder("(SAF.ID_FILIAL_ORIGEM IN (SELECT DISTINCT ID_FILIAL_ATENDE_OPERACIONAL FROM CLIENTE C \n")
    		.append("WHERE C.ID_CLIENTE = :id) OR \n")
    		.append("SAF.ID_UNIDADE_FEDERATIVA IN (SELECT DISTINCT M.ID_UNIDADE_FEDERATIVA FROM MUNICIPIO M \n")
    		.append("INNER JOIN ENDERECO_PESSOA EP ON EP.ID_MUNICIPIO = M.ID_MUNICIPIO AND EP.DT_VIGENCIA_INICIAL <= :dataBase AND EP.DT_VIGENCIA_FINAL >= :dataBase \n")
    		.append("INNER JOIN CLIENTE C ON C.ID_FILIAL_ATENDE_OPERACIONAL = EP.ID_PESSOA \n")
    		.append("WHERE C.ID_CLIENTE = :id) OR \n")
    		.append("SAF.ID_REGIONAL IN (SELECT DISTINCT R.ID_REGIONAL FROM REGIONAL R \n")
    		.append("INNER JOIN REGIONAL_FILIAL RF ON RF.ID_REGIONAL = R.ID_REGIONAL AND  RF.DT_VIGENCIA_INICIAL <= :dataBase AND RF.DT_VIGENCIA_FINAL >= :dataBase \n")
    		.append("INNER JOIN CLIENTE C ON C.ID_FILIAL_ATENDE_OPERACIONAL = RF.ID_FILIAL \n")
    		.append("WHERE C.ID_CLIENTE = :id)) AND \n");
    }
    
    /**
     * Método que retorna uma lista de SubstAtendimentoFilial pelo ID da Filial de Destino. 
     *
     * @param idFilialDestino
     * @return
     */
    public List findSubstAtendimentoFilialByIdFilialDestino(Long idFilialDestino, DomainValue tpDesvioCarga) {
        StringBuilder hql = new StringBuilder();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		hql.append(" select saf.id ");	
		hql.append(" from ").append(SubstAtendimentoFilial.class.getName()).append(" saf");		
		hql.append(" where saf.filialByIdFilialDestino.id = ?");
		hql.append(" 	   and ? between saf.dtVigenciaInicial and saf.dtVigenciaFinal ");
		
		if(tpDesvioCarga != null){
			hql.append(" and saf.tpDesvioCarga = '").append(tpDesvioCarga.getValue()).append("'");
		}
		
		List param = new ArrayList();
		param.add(idFilialDestino);
		param.add(JTDateTimeUtils.getDataAtual());
		
		
		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
    }    
    
    /**
     * Método que retorna SubstAtendimentoFilial pelo ID da Filial de Destino.
     */
    public Filial findMatchSubstAtendimentoFilialByIdFilialDestino(DomainValue tpDesvioCarga, Long idFilialDestino, Long idNaturezaProduto, Long idServico, Long idUFOrigem, Long idRegionalOrigem, Long idFilialOrigem, Long idClienteRemetente, Long idMunicipioDestino) {
    	SqlTemplate hql = new SqlTemplate();
    
    	hql.addProjection("saf");
    	hql.addFrom(SubstAtendimentoFilial.class.getName(), "saf");

        if (tpDesvioCarga != null) {
    		hql.addCriteria("saf.tpDesvioCarga", "=", tpDesvioCarga.getValue());
        }

        if (idNaturezaProduto != null) {
    		hql.addCustomCriteria("nvl(saf.naturezaProduto.idNaturezaProduto, " + idNaturezaProduto + ") = " + idNaturezaProduto);
        }

        if (idServico != null) {
    		hql.addCustomCriteria("nvl(saf.servico.idServico, " + idServico + ") = " + idServico);
        }

        if (idUFOrigem != null) {
    		hql.addCustomCriteria("nvl(saf.unidadeFederativa.idUnidadeFederativa, " + idUFOrigem + ") = " + idUFOrigem);
        }

        if (idRegionalOrigem != null) {
    		hql.addCustomCriteria("nvl(saf.regional.idRegional, " + idRegionalOrigem + ") = " + idRegionalOrigem);
        }

        if (idFilialOrigem != null) {
    		hql.addCustomCriteria("nvl(saf.filialByIdFilialOrigem.idFilial, " + idFilialOrigem + ") = " + idFilialOrigem);
        }

        if (idClienteRemetente != null) {
    		hql.addCustomCriteria("nvl(saf.cliente.idCliente, " + idClienteRemetente + ") = " + idClienteRemetente);
        }

        if (idMunicipioDestino != null) {
    		hql.addCustomCriteria("nvl(saf.municipio.idMunicipio, " + idMunicipioDestino + ") = " + idMunicipioDestino);
        }

        if (idFilialDestino != null) {
    		hql.addCriteria("saf.filialByIdFilialDestino.id", "=", idFilialDestino);
        }

    	hql.addCustomCriteria("trunc(sysdate) BETWEEN trunc(saf.dtVigenciaInicial) and trunc(saf.dtVigenciaFinal)");

    	hql.addOrderBy("(" +
			"decode(saf.naturezaProduto.idNaturezaProduto, null, 0, 1) + " +
			"decode(saf.servico.idServico, null, 0, 1) + " +
			"decode(saf.unidadeFederativa.idUnidadeFederativa, null, 0, 1) + " +
			"decode(saf.regional.idRegional, null, 0, 1) + " +
			"decode(saf.filialByIdFilialOrigem.idFilial, null, 0, 1) + " +
			"decode(saf.cliente.idCliente, null, 0, 1) + " +
			"decode(saf.municipio.idMunicipio, null, 0, 1)" +
		")", "desc");
    	hql.addOrderBy("saf.idSubstAtendimentoFilial");

    	List<SubstAtendimentoFilial> result = (List<SubstAtendimentoFilial>)getAdsmHibernateTemplate().find(hql.toString(), hql.getCriteria());
        return result != null && !result.isEmpty() ? result.get(0).getFilialByIdFilialDestinoSubstituta() : null;
    }


    /**
     * Retorna a filial substituta para o cliente e destino informados.
     *
     * @param idFilialDestino
     * @param idCliente
     * @return
     */
    public Filial findFilialSubstitutaByFilialDestinoAndCliente(Long idFilialDestino, Long idCliente) {
    	DetachedCriteria dc = DetachedCriteria.forClass(SubstAtendimentoFilial.class);
    	
    	dc.setFetchMode("filialByIdFilialDestinoSubstituta", FetchMode.JOIN);
    	
    	dc.add(Restrictions.le("dtVigenciaInicial", JTDateTimeUtils.getDataAtual()));
		dc.add(Restrictions.ge("dtVigenciaFinal", JTDateTimeUtils.getDataAtual()));
		dc.add(Restrictions.eq("filialByIdFilialDestino.id", idFilialDestino));
		dc.add(Restrictions.or(
				Restrictions.eq("cliente.id", idCliente), 
				Restrictions.eq("cliente.id", null)
		));
		dc.addOrder(Order.desc("cliente.id"));
		
		List<SubstAtendimentoFilial> result = this.findByDetachedCriteria(dc);
        if (result != null && !result.isEmpty()) {
			return result.get(0).getFilialByIdFilialDestinoSubstituta();			
		} else {
			return null;
		}		
    }
    
    /**
     * Método que retorna uma lista de SubstAtendimentoFilial pelo ID da Filial Substituta. 
     *
     * @param idFilialSubstituta
     * @return
     */
    public List findSubstAtendimentoFilialByIdFilialSubstituta(Long idFilialSubstituta) {
        StringBuilder hql = new StringBuilder();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		hql.append(" select distinct saf.filialByIdFilialDestino.id ");	
		hql.append(" from ").append(SubstAtendimentoFilial.class.getName()).append(" saf");		
		hql.append(" where saf.filialByIdFilialDestinoSubstituta.id = ?");
		hql.append(" 	   and ? between saf.dtVigenciaInicial and saf.dtVigenciaFinal ");
		
		List param = new ArrayList();
		param.add(idFilialSubstituta);
		param.add(JTDateTimeUtils.getDataAtual());
		
		return getAdsmHibernateTemplate().find(hql.toString(), param.toArray());
    }
    
    /**
     * Método que retorna um SubstAtendimentoFilial pelo ID da Filial de Destino e ID da Filial Substituta. 
     *
     * @param idFilialDestino
     * @return
     */
    public List<SubstAtendimentoFilial> findSubstAtendimentoFilialByIdFilialDestinoAndFilialSubstituta(Long idFilialDestino, Long idFilialSubstituta) {
    	SqlTemplate hql = new SqlTemplate();

		// Se houver necessidade de joins, colocar no FROM tomando cuidado com os campos nullable.
		hql.addFrom( SubstAtendimentoFilial.class.getName(),"saf");		
    	hql.addCriteria("saf.filialByIdFilialDestino.id","=",idFilialDestino);
    	hql.addCriteria("saf.filialByIdFilialDestinoSubstituta.id","=",idFilialSubstituta);
    	hql.addCriteria("saf.dtVigenciaInicial","<=",JTDateTimeUtils.getDataAtual());
    	hql.addCriteria("saf.dtVigenciaFinal",">=",JTDateTimeUtils.getDataAtual());
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
    } 
    
    public Integer getRowCountEqualsWithCliente(SubstAtendimentoFilial bean) {
        SqlTemplate hql = new SqlTemplate();
        hql.addFrom(getPersistentClass().getName(), "SAF");
        hql.addCriteria("SAF.id", "<>", bean.getIdSubstAtendimentoFilial());
        
        // LMSA-5724
    	hql.addCriteria("SAF.filialByIdFilialDestino.id","=",bean.getFilialByIdFilialDestino().getIdFilial());
        
        hql.addCriteria(SAF_DT_VIGENCIA_INICIAL, "<=", bean.getDtVigenciaInicial());
        hql.addCriteria(SAF_DT_VIGENCIA_FINAL, ">", bean.getDtVigenciaInicial());

        if (bean.getMunicipio() != null) {
            hql.addCriteria("SAF.municipio.id", "=", bean.getMunicipio().getIdMunicipio());
        }

        if (bean.getNaturezaProduto() != null) {
            hql.addCriteria("SAF.naturezaProduto.id", "=", bean.getNaturezaProduto().getIdNaturezaProduto());
        }

        if (bean.getServico() != null) {
            hql.addCriteria("SAF.servico.id", "=", bean.getServico().getIdServico());
        }

        hql.addCriteria("SAF.cliente.id", "=", bean.getCliente().getIdCliente());

        return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());

    }

    public Integer getRowCountEquals(SubstAtendimentoFilial bean) {
    
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addFrom(getPersistentClass().getName(),"SAF");
    	
    	hql.addCriteria("SAF.filialByIdFilialDestino.id","=",bean.getFilialByIdFilialDestino().getIdFilial());
    	hql.addCriteria("SAF.id","<>",bean.getIdSubstAtendimentoFilial());
    	
        hql.addCriteria(SAF_DT_VIGENCIA_INICIAL, "<=", bean.getDtVigenciaInicial());
        hql.addCriteria(SAF_DT_VIGENCIA_FINAL, ">", bean.getDtVigenciaInicial());

		if (bean.getMunicipio() != null && bean.getServico() != null && bean.getNaturezaProduto() != null) {

            municipioServicoENaturezaNotNullCriteria(bean, hql);
			    		
        } else if (bean.getNaturezaProduto() != null) {
			    
            naturezaProdutoNotNullCriteria(bean, hql);
			    		
        } else if (bean.getServico() != null) {
			
            servicoNotNullCriteria(bean, hql);
			
        } else if (bean.getMunicipio() != null) {
			
            municipioNotNullCriteria(bean, hql);
    			
        }
				
        return getAdsmHibernateTemplate().getRowCountForQuery(hql.getSql(), hql.getCriteria());
    }

    private void municipioNotNullCriteria(SubstAtendimentoFilial bean, SqlTemplate hql) {
        if (bean.getNaturezaProduto() != null || bean.getServico() != null) {
					
            if (bean.getNaturezaProduto() != null) {

                hql.addCustomCriteria("((SAF.municipio.id = ? AND SAF.naturezaProduto.id IS NULL) " +
                        "OR (SAF.naturezaProduto.id = ? AND SAF.municipio.id IS NULL) OR " +
                        "(SAF.naturezaProduto.id = ? AND SAF.municipio.id = ?))");

                hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
					hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
                hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
					hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());

				}else{
					
                hql.addCustomCriteria("((SAF.municipio.id = ? AND SAF.servico.id IS NULL) " +
                        "OR (SAF.servico.id = ? AND SAF.municipio.id IS NULL) OR " +
                        "(SAF.servico.id = ? AND SAF.municipio.id = ?))");
					
                hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
					hql.addCriteriaValue(bean.getServico().getIdServico());
					hql.addCriteriaValue(bean.getServico().getIdServico());
                hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
					
				}
					
			}else{
            hql.addCriteria("SAF.municipio.id", "=", bean.getMunicipio().getIdMunicipio());
			}
    }
    	
    private void servicoNotNullCriteria(SubstAtendimentoFilial bean, SqlTemplate hql) {
			if (bean.getMunicipio() != null || bean.getNaturezaProduto() != null) {
				
				if (bean.getMunicipio() != null) {

					hql.addCustomCriteria("((SAF.servico.id = ? AND SAF.municipio.id IS NULL) " +
							"OR (SAF.municipio.id = ? AND SAF.servico.id IS NULL) OR " +
							"(SAF.municipio.id = ? AND SAF.servico.id = ?))");

					hql.addCriteriaValue(bean.getServico().getIdServico());
					hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
					hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
					hql.addCriteriaValue(bean.getServico().getIdServico());
				}else{
					
					hql.addCustomCriteria("((SAF.servico.id = ? AND SAF.naturezaProduto.id IS NULL) " +
							"OR (SAF.naturezaProduto.id = ? AND SAF.servico.id IS NULL) OR " +
							"(SAF.naturezaProduto.id = ? AND SAF.servico.id = ?))");

					hql.addCriteriaValue(bean.getServico().getIdServico());
					hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
					hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
					hql.addCriteriaValue(bean.getServico().getIdServico());
					
				}
					
			}else{
				hql.addCriteria("SAF.servico.id","=", bean.getServico().getIdServico());
			}
    }
    	
    private void naturezaProdutoNotNullCriteria(SubstAtendimentoFilial bean, SqlTemplate hql) {
        if (bean.getMunicipio() != null || bean.getServico() != null) {
    			
            if (bean.getMunicipio() != null) {
				
                hql.addCustomCriteria("((SAF.naturezaProduto.id = ? AND SAF.municipio.id IS NULL) " +
                        "OR (SAF.municipio.id = ? AND SAF.naturezaProduto.id IS NULL) OR " +
                        "(SAF.municipio.id = ? AND SAF.naturezaProduto.id = ?))");

                hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
					hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
                hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
					hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());

				}else{
					
                hql.addCustomCriteria("((SAF.naturezaProduto.id = ? AND SAF.servico.id IS NULL) " +
                        "OR (SAF.servico.id = ? AND SAF.naturezaProduto.id IS NULL) OR " +
                        "(SAF.servico.id = ? AND SAF.naturezaProduto.id = ?))");
					
                hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
					hql.addCriteriaValue(bean.getServico().getIdServico());
					hql.addCriteriaValue(bean.getServico().getIdServico());
                hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
					
				}
					
			}else{
            hql.addCriteria("SAF.naturezaProduto.id", "=", bean.getNaturezaProduto().getIdNaturezaProduto());
			}
		}
    		
    private void municipioServicoENaturezaNotNullCriteria(SubstAtendimentoFilial bean, SqlTemplate hql) {
        hql.addCustomCriteria(" ((SAF.municipio.id = ? AND ((SAF.naturezaProduto.id IS NULL OR SAF.servico.id IS NULL) " +
                "OR (SAF.naturezaProduto.id = ? OR SAF.servico.id = ?))) OR " +

                "  (SAF.servico.id = ? AND ((SAF.naturezaProduto.id IS NULL OR SAF.municipio.id IS NULL) " +
                "OR (SAF.naturezaProduto.id = ? OR SAF.municipio.id = ?))) OR " +

                "  (SAF.naturezaProduto.id = ? AND ((SAF.servico.id IS NULL OR SAF.municipio.id IS NULL) " +
                "OR (SAF.servico.id = ? OR SAF.municipio.id = ?)))) ");

        hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
        hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
        hql.addCriteriaValue(bean.getServico().getIdServico());

        hql.addCriteriaValue(bean.getServico().getIdServico());
        hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
        hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());

        hql.addCriteriaValue(bean.getNaturezaProduto().getIdNaturezaProduto());
        hql.addCriteriaValue(bean.getServico().getIdServico());
        hql.addCriteriaValue(bean.getMunicipio().getIdMunicipio());
    }
    
}