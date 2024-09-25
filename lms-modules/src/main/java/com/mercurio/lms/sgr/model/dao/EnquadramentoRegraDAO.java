package com.mercurio.lms.sgr.model.dao;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.BaseCompareVarcharI18n;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.FilialEnquadramento;
import com.mercurio.lms.sgr.model.MunicipioEnquadramento;
import com.mercurio.lms.sgr.model.PaisEnquadramento;
import com.mercurio.lms.sgr.model.UnidadeFederativaEnquadramento;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class EnquadramentoRegraDAO extends BaseCrudDao<EnquadramentoRegra, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return EnquadramentoRegra.class;
    }

   	protected void initFindByIdLazyProperties(Map fetchModes) {
		fetchModes.put("reguladoraSeguro", FetchMode.JOIN );   		
		fetchModes.put("naturezaProduto", FetchMode.JOIN );
		fetchModes.put("moeda", FetchMode.JOIN );
		// LMS-7285
		fetchModes.put("apoliceSeguro", FetchMode.JOIN);
		fetchModes.put("apoliceSeguro.seguradora", FetchMode.JOIN);
		fetchModes.put("apoliceSeguro.seguradora.pessoa", FetchMode.JOIN);
		fetchModes.put("seguroCliente", FetchMode.JOIN);
		fetchModes.put("seguroCliente.seguradora", FetchMode.JOIN);
		fetchModes.put("seguroCliente.seguradora.pessoa", FetchMode.JOIN);
	}
   
    protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("reguladoraSeguro", FetchMode.JOIN );   		
		fetchModes.put("naturezaProduto", FetchMode.JOIN );
		fetchModes.put("moeda", FetchMode.JOIN );
    }
    
    
	private SqlTemplate addSql(TypedFlatMap map) {
		SqlTemplate sql = new SqlTemplate();
   		sql.addProjection("enquadramentoRegra");

   		StringBuffer strFrom = new StringBuffer() 
   			.append(EnquadramentoRegra.class.getName())
   			.append(" enquadramentoRegra ")
			.append("left outer join fetch enquadramentoRegra.moeda as moeda ")
			.append("left outer join fetch enquadramentoRegra.naturezaProduto as naturezaProduto ")
			.append("left outer join fetch enquadramentoRegra.reguladoraSeguro as reguladoraSeguro ");
   		
   		sql.addFrom(strFrom.toString());
				
				
		if (map.getLong("cliente.idCliente") != null) {
			sql.addFrom(ClienteEnquadramento.class.getName() + " clienteEnquadramento left join clienteEnquadramento.enquadramentoRegra ");
			sql.addCriteria("clienteEnquadramento.cliente.idCliente", "=", map.getLong("cliente.idCliente"));
	        sql.addJoin("clienteEnquadramento.enquadramentoRegra.idEnquadramentoRegra", "enquadramentoRegra.idEnquadramentoRegra");			
		}
		
		if (map.getString("idControleCarga") != null && !map.getString("idControleCarga").equals("")) {
			sql.addCriteria("enquadramentoRegra.idControleCarga", "=", map.getString("idControleCarga"));
		}
		if (map.getString("blVerificacaoGeral") != null && !map.getString("blVerificacaoGeral").equals("")) {
			sql.addCriteria("enquadramentoRegra.blVerificacaoGeral", "=", map.getString("blVerificacaoGeral"));
		}
		if (map.getString("moeda.idPais") != null && !map.getString("moeda.idPais").equals("")) {
			sql.addCriteria("enquadramentoRegra.moeda.idPais", "=", map.getString("moeda.idPais"));
		}
		if (!map.getString("tpCriterioOrigem").equals("")) {
			sql.addCriteria("enquadramentoRegra.tpCriterioOrigem", "=", map.getString("tpCriterioOrigem"));
		}
		if (!map.getString("tpCriterioDestino").equals("")) {
			sql.addCriteria("enquadramentoRegra.tpCriterioDestino", "=", map.getString("tpCriterioDestino"));
		}
		if (map.getLong("naturezaProduto.idNaturezaProduto") != null) {
			sql.addCriteria("enquadramentoRegra.naturezaProduto.idNaturezaProduto", "=", map.getLong("naturezaProduto.idNaturezaProduto"));
		}
		if (map.getLong("moeda.idMoeda") != null) {
			sql.addCriteria("enquadramentoRegra.moeda.idMoeda", "=", map.getLong("moeda.idMoeda"));
		}
		if (!map.getString("tpOperacao").equals("")) {
			sql.addCriteria("enquadramentoRegra.tpOperacao", "=", map.getString("tpOperacao"));
		}
		if (!map.getString("tpAbrangencia").equals("")) {
			sql.addCriteria("enquadramentoRegra.tpAbrangencia", "=", map.getString("tpAbrangencia"));
		}
		// LMS-7253 - filtro por atributo de regra geral
		if (!map.getString("blRegraGeral").isEmpty()) {
			sql.addCriteria("enquadramentoRegra.blRegraGeral", "=", map.getBoolean("blRegraGeral"));
		}
		if (!map.getString("dsEnquadramentoRegra").equals("")) {
			sql.addCustomCriteria(BaseCompareVarcharI18n.hqlLike("enquadramentoRegra.dsEnquadramentoRegra", LocaleContextHolder.getLocale(), true), "%"+map.getString("dsEnquadramentoRegra"));
		}
        if (map.getString("dtVigencia") != null && !map.getString("dtVigencia").equals("")) {
        	String strDtRange = "(( ? BETWEEN enquadramentoRegra.dtVigenciaInicial AND enquadramentoRegra.dtVigenciaFinal) OR (enquadramentoRegra.dtVigenciaInicial <= ? AND enquadramentoRegra.dtVigenciaFinal IS NULL)";
        	strDtRange += " OR (enquadramentoRegra.dtVigenciaInicial IS NULL AND enquadramentoRegra.dtVigenciaFinal >= ?)";
        	strDtRange += " OR (enquadramentoRegra.dtVigenciaInicial IS NULL AND enquadramentoRegra.dtVigenciaFinal IS NULL))";
        	
        	YearMonthDay dtVigencia = (YearMonthDay)ReflectionUtils.toObject(map.getString("dtVigencia"), YearMonthDay.class);
			sql.addCustomCriteria(strDtRange, dtVigencia);
			sql.addCriteriaValue(dtVigencia);
			sql.addCriteriaValue(dtVigencia);
		}
    	sql.addOrderBy(OrderVarcharI18n.hqlOrder("enquadramentoRegra.dsEnquadramentoRegra", LocaleContextHolder.getLocale())); 
        return sql;
    }


	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDefinition) {
		SqlTemplate sql = new SqlTemplate();
		sql = addSql(criteria);
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
    }


    public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = addSql(criteria);	
		Integer rowCountQuery = getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
		return rowCountQuery;    
	}

	
	public List verificaEnquadramentoRegraUnica(EnquadramentoRegra enquadramentoRegra) {
		SqlTemplate sql = addSqlUnique(enquadramentoRegra);	
       	List result = getAdsmHibernateTemplate().find(sql.getSql(true),sql.getCriteria());
		return result;    
	}
	
	
	private SqlTemplate addSqlUnique(EnquadramentoRegra enquadramentoRegra) {
		SqlTemplate sql = new SqlTemplate();

		sql.addProjection("enquadramentoRegra"); 
   	
		sql.addFrom(EnquadramentoRegra.class.getName() + " enquadramentoRegra " +
				"left outer join enquadramentoRegra.moeda as moeda " +
				"left outer join enquadramentoRegra.naturezaProduto as naturezaProduto " +				
				"left outer join enquadramentoRegra.reguladoraSeguro as reguladoraSeguro ");   
		
	    sql.addCriteria("enquadramentoRegra.moeda.idMoeda","=",enquadramentoRegra.getMoeda().getIdMoeda());
	    sql.addCriteria("enquadramentoRegra.blSeguroMercurio","=", enquadramentoRegra.getBlSeguroMercurio());
	    sql.addCriteria("enquadramentoRegra.blRequerPassaporteViagem","=", enquadramentoRegra.getBlRequerPassaporteViagem());
    	sql.addCriteria("enquadramentoRegra.tpOperacao","=",enquadramentoRegra.getTpOperacao().getValue());
    	
        if (enquadramentoRegra.getClienteEnquadramentos() != null && enquadramentoRegra.getClienteEnquadramentos().size() > 0) {
    		sql.addFrom(ClienteEnquadramento.class.getName() + " clienteEnquadramento left outer join clienteEnquadramento.enquadramentoRegra ");
            sql.addJoin("clienteEnquadramento.enquadramentoRegra.idEnquadramentoRegra","enquadramentoRegra.idEnquadramentoRegra");			

            String clientePesq = " OR ( ";
 			int i=0;
	    	for(Iterator it=enquadramentoRegra.getClienteEnquadramentos().iterator();it.hasNext();){
				Cliente cliente = ((ClienteEnquadramento) it.next()).getCliente();
	    		if (i++!=0) {
		    		clientePesq += " OR ";	    			
	    		}
	    		clientePesq += " clienteEnquadramento.cliente.idCliente = '" + cliente.getIdCliente() + "'";
	    	}
    	    sql.add(clientePesq + ")");
		}
    	
        if (enquadramentoRegra.getFilialEnquadramentosOrigem() != null && enquadramentoRegra.getFilialEnquadramentosOrigem().size() > 0) {
    		sql.addFrom(FilialEnquadramento.class.getName() + " filialEnquadramento left outer join filialEnquadramento.enquadramentoRegra ");
            sql.addJoin("filialEnquadramento.enquadramentoRegra.idEnquadramentoRegra","enquadramentoRegra.idEnquadramentoRegra");			

            String filialPesq = " OR ( ";
 			int i=0;
	    	for(Iterator it=enquadramentoRegra.getFilialEnquadramentosOrigem().iterator();it.hasNext();){
	    		Filial filial = (Filial)it.next();
	    		if (i++!=0) {
	    			filialPesq += " OR ";	    			
	    		}
	    		filialPesq += " ( filialEnquadramento.filial.idFilial = '" + filial.getIdFilial() + "'";
	    		filialPesq += " AND filialEnquadramento.tpInfluenciaMunicipio = 'O')";	    		
	    	}
    	    sql.add(filialPesq + ")");
		}
        
        if (enquadramentoRegra.getIdEnquadramentoRegra() != null) {
			sql.addCriteria("enquadramentoRegra.idEnquadramentoRegra","<>",enquadramentoRegra.getIdEnquadramentoRegra());
		}

    	if (enquadramentoRegra.getDtVigenciaInicial() != null) {
	    	sql.addCriteria("enquadramentoRegra.dtVigenciaInicial","=",enquadramentoRegra.getDtVigenciaInicial());
		}
		if (enquadramentoRegra.getDtVigenciaFinal() != null) {
	    	sql.addCriteria("enquadramentoRegra.dtVigenciaFinal","=",enquadramentoRegra.getDtVigenciaFinal());			
		}

	    if (enquadramentoRegra.getNaturezaProduto() != null) {
        	sql.addCriteria("enquadramentoRegra.naturezaProduto.idNaturezaProduto","=",enquadramentoRegra.getNaturezaProduto().getIdNaturezaProduto());
    	}

    	if (enquadramentoRegra.getTpModal() != null) {
        	sql.addCriteria("enquadramentoRegra.tpModal","=",enquadramentoRegra.getTpModal().getValue());
    	}
    	if (enquadramentoRegra.getTpAbrangencia() != null) {
    		sql.addCriteria("enquadramentoRegra.tpAbrangencia","=",enquadramentoRegra.getTpAbrangencia().getValue());
    	}
    	if (enquadramentoRegra.getTpGrauRiscoColetaEntrega() != null) {
    		sql.addCriteria("enquadramentoRegra.tpGrauRiscoColetaEntrega","=",enquadramentoRegra.getTpGrauRiscoColetaEntrega().getValue());
    	}	
    	if (enquadramentoRegra.getReguladoraSeguro() != null) {
    		sql.addCriteria("enquadramentoRegra.reguladoraSeguro.idReguladora","=",enquadramentoRegra.getReguladoraSeguro().getIdReguladora());
    	}
    	if (enquadramentoRegra.getTpCriterioOrigem() != null) {
    		sql.addCriteria("enquadramentoRegra.tpCriterioOrigem","=",enquadramentoRegra.getTpCriterioOrigem().getValue());
    	}	
    	if (enquadramentoRegra.getTpCriterioDestino() != null) {
    		sql.addCriteria("enquadramentoRegra.tpCriterioDestino","=",enquadramentoRegra.getTpCriterioDestino().getValue());
    	}	
    	return sql;
    }
	
	
	/**
	 * @param tpOperacao
	 * @return
	 */
    public List findByExigenciasGerRisco(String tpOperacao, YearMonthDay dataVigente) {
    	DetachedCriteria dc = DetachedCriteria.forClass(EnquadramentoRegra.class)
	    	.setFetchMode("reguladoraSeguro", FetchMode.JOIN)
	    	.setFetchMode("naturezaProduto", FetchMode.JOIN)
	    	// LMS-6850 - filtro para tipo de operação
	    	.add(Restrictions.or(
	    			Restrictions.eq("tpOperacao", tpOperacao),
	    			Restrictions.isNull("tpOperacao")))
	    	.add(Restrictions.le("dtVigenciaInicial", dataVigente))
			.add(Restrictions.ge("dtVigenciaFinal", dataVigente));

    	List list = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	
    	//FIXME: Tirar regra de carregar o objeto desta DAO. Preparar a propria query para isto...
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		EnquadramentoRegra er = (EnquadramentoRegra)iter.next();
    		er.setClienteEnquadramentos(getClienteEnquadramento(er.getIdEnquadramentoRegra()));
    		er.setMunicipioEnquadramentosOrigem(getMunicipioEnquadramento(er.getIdEnquadramentoRegra(), "O"));
    		er.setMunicipioEnquadramentosDestino(getMunicipioEnquadramento(er.getIdEnquadramentoRegra(), "D"));
    		er.setPaisEnquadramentosOrigem(getPaisEnquadramento(er.getIdEnquadramentoRegra(), "O"));
    		er.setPaisEnquadramentosDestino(getPaisEnquadramento(er.getIdEnquadramentoRegra(), "D"));
    		er.setUnidadeFederativaEnquadramentosOrigem(getUnidadeFederativaEnquadramento(er.getIdEnquadramentoRegra(), "O"));
    		er.setUnidadeFederativaEnquadramentosDestino(getUnidadeFederativaEnquadramento(er.getIdEnquadramentoRegra(), "D"));
    		er.setFilialEnquadramentosOrigem(getFilialEnquadramento(er.getIdEnquadramentoRegra(), "O"));
    		er.setFilialEnquadramentosDestino(getFilialEnquadramento(er.getIdEnquadramentoRegra(), "D"));
    	}
    	return list;
    }

    private List getClienteEnquadramento(Long idEnquadramentoRegra) {
    	DetachedCriteria dc = DetachedCriteria.forClass(ClienteEnquadramento.class)
    		.setFetchMode("cliente", FetchMode.JOIN)
	    	.add(Restrictions.eq("enquadramentoRegra.id", idEnquadramentoRegra));
    	List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (lista.isEmpty())
    		return null;
    	return lista;
    }

    private List getMunicipioEnquadramento(Long idEnquadramentoRegra, String tpInfluenciaMunicipio) {
    	DetachedCriteria dc = DetachedCriteria.forClass(MunicipioEnquadramento.class)
	    	.add(Restrictions.eq("enquadramentoRegra.id", idEnquadramentoRegra))
	    	.add(Restrictions.eq("tpInfluenciaMunicipio", tpInfluenciaMunicipio));
    	List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (lista.isEmpty())
    		return null;
    	return lista;
    }

    private List getPaisEnquadramento(Long idEnquadramentoRegra, String tpInfluenciaMunicipio) {
    	DetachedCriteria dc = DetachedCriteria.forClass(PaisEnquadramento.class)
	    	.add(Restrictions.eq("enquadramentoRegra.id", idEnquadramentoRegra))
	    	.add(Restrictions.eq("tpInfluenciaMunicipio", tpInfluenciaMunicipio));
    	List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (lista.isEmpty())
    		return null;
    	return lista;
    }

    private List getUnidadeFederativaEnquadramento(Long idEnquadramentoRegra, String tpInfluenciaMunicipio) {
    	DetachedCriteria dc = DetachedCriteria.forClass(UnidadeFederativaEnquadramento.class)
	    	.add(Restrictions.eq("enquadramentoRegra.id", idEnquadramentoRegra))
	    	.add(Restrictions.eq("tpInfluenciaMunicipio", tpInfluenciaMunicipio));
    	List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (lista.isEmpty())
    		return null;
    	return lista;
    }

    private List getFilialEnquadramento(Long idEnquadramentoRegra, String tpInfluenciaMunicipio) {
    	DetachedCriteria dc = DetachedCriteria.forClass(FilialEnquadramento.class)
	    	.add(Restrictions.eq("enquadramentoRegra.id", idEnquadramentoRegra))
	    	.add(Restrictions.eq("tpInfluenciaMunicipio", tpInfluenciaMunicipio));
    	List lista = getAdsmHibernateTemplate().findByDetachedCriteria(dc);
    	if (lista.isEmpty())
    		return null;
    	return lista;
    }

	/**
	 * LMS-7285 - Busca número de identificação (CNPJ) do {@link Cliente}
	 * (<tr>PESSOA.NR_IDENTIFICACAO</tr>) relacionado ao {@link SeguroCliente}.
	 * 
	 * @param seguroCliente
	 *            {@link SeguroCliente} relacionando {@link Cliente}
	 * @return número de identificação (CNPJ) do {@link Cliente}
	 */
	public String findNrIdentificacao(SeguroCliente seguroCliente) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT sc.cliente.pessoa.nrIdentificacao ")
				.append("FROM SeguroCliente sc ")
				.append("WHERE sc = ?");
		return (String) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[] { seguroCliente });
	}

	/**
	 * LMS-7285 - Busca número de identificação (CNPJ's) dos {@link Cliente}s
	 * (<tr>PESSOA.NR_IDENTIFICACAO</tr>) relacionados aos
	 * {@link ClienteEnquadramento}s.
	 * 
	 * @param clienteEnquadramentos
	 *            {@link ClienteEnquadramento}s relacionando {@link Cliente}s
	 * @return números de identificação (CNPJ) dos {@link Cliente}s
	 */
	@SuppressWarnings("unchecked")
	public List<String> findNrIdentificacao(List<ClienteEnquadramento> clienteEnquadramentos) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT c.pessoa.nrIdentificacao ")
				.append("FROM Cliente c ")
				.append("WHERE c.idCliente IN (");
		for (ClienteEnquadramento clienteEnquadramento : clienteEnquadramentos) {
			Long idCliente = clienteEnquadramento.getCliente().getIdCliente();
			hql.append(idCliente).append(",");
		}
		hql.setLength(hql.length() - 1);
		hql.append(")");
		return getAdsmHibernateTemplate().find(hql.toString());
	}

}
