package com.mercurio.lms.tributos.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServ;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServMunic;
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
public class AliquotaContribuicaoServDAO extends BaseCrudDao<AliquotaContribuicaoServ, Long>
{
	
	/**
	 * Busca Aliquota vigente de acordo com parametros.<BR>
	 * Retorna <i>null</i> caso não encontrada.
	 *@author Robson Edemar Gehl
	 * @param idPessoa
	 * @param idServicoAdicional
	 * @param idServicoTributo
	 * @param dtBase
	 * @param tpImposto
	 * @return
	 */
	public AliquotaContribuicaoServ findByUnique(Long idPessoa, Long idServicoAdicional, 
			Long idServicoTributo, YearMonthDay dtBase, String tpImposto){

		DetachedCriteria dc = createDetachedCriteria();
		if (idServicoAdicional != null){
			dc.createAlias("servicoAdicional", "sa");	
		}
		if (idServicoTributo != null){
			dc.createAlias("servicoTributo", "st");	
		}
		
		dc.add(Restrictions.eq("tpImposto", tpImposto));
		
		JTVigenciaUtils.getDetachedVigencia(dc, dtBase, dtBase);
		
		if (idServicoAdicional != null){
			dc.add(Restrictions.eq("sa.id", idServicoAdicional));
		}
		
		if (idServicoTributo != null){
			dc.add(Restrictions.eq("st.id", idServicoTributo));
		}

		dc.add(Restrictions.or(
					Restrictions.isNull("pessoa.id"),
					Restrictions.eq("pessoa.id", idPessoa)
				));
		
		List list = findByDetachedCriteria(dc);
		if (!list.isEmpty()){
			return (AliquotaContribuicaoServ) list.get(0);
		}
		
		return null;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("servicoAdicional", FetchMode.JOIN);
		lazyFindById.put("servicoTributo", FetchMode.JOIN);
		lazyFindById.put("pessoa", FetchMode.JOIN);
		lazyFindById.put("aliquotasContribuicaoServMunic",FetchMode.SELECT);
	}

	protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
		lazyFindPaginated.put("servicoAdicional", FetchMode.JOIN);
		lazyFindPaginated.put("servicoTributo", FetchMode.JOIN);
		lazyFindPaginated.put("pessoa", FetchMode.JOIN);
	}
	
	
	public boolean verificaExisteVigencia(AliquotaContribuicaoServ aliquotaContribuicaoServ) {
		StringBuffer hql = new StringBuffer();
		ArrayList values = new ArrayList();

		hql.append("from AliquotaContribuicaoServ ac " +
                   "left join ac.servicoAdicional sa " +
                   "left join ac.servicoTributo st " +
                   "left join ac.pessoa p ");
		hql.append("where ac.tpImposto = ? ");

		values.add(aliquotaContribuicaoServ.getTpImposto().getValue());

		if (aliquotaContribuicaoServ.getIdAliquotaContribuico() != null) {
			hql.append("and ac.id != ? ");
			values.add(aliquotaContribuicaoServ.getIdAliquotaContribuico());
		}
        
        if(aliquotaContribuicaoServ.getServicoAdicional() != null){
            hql.append("and sa.idServicoAdicional = ? ");
            values.add(aliquotaContribuicaoServ.getServicoAdicional().getIdServicoAdicional());
        }
        
        if(aliquotaContribuicaoServ.getServicoTributo() != null){
            hql.append("and st.idServicoTributo = ? ");
            values.add(aliquotaContribuicaoServ.getServicoTributo().getIdServicoTributo());
        }
        
        if(aliquotaContribuicaoServ.getPessoa() != null){
            hql.append("and p.id = ? ");
            values.add(aliquotaContribuicaoServ.getPessoa().getIdPessoa());
        }

		if (aliquotaContribuicaoServ.getDtVigenciaFinal() != null) {
			values.add(aliquotaContribuicaoServ.getDtVigenciaInicial());
			values.add(JTDateTimeUtils.maxYmd(aliquotaContribuicaoServ.getDtVigenciaFinal()));
			hql.append("and not((ac.dtVigenciaFinal < ? ) "
					+ "or ac.dtVigenciaInicial > ? ) ");
			
		}else{
			values.add(aliquotaContribuicaoServ.getDtVigenciaInicial());
			hql.append("and not(ac.dtVigenciaFinal < ? ) ");
		}
		
		Integer retorno = this.getAdsmHibernateTemplate().getRowCountForQuery(hql.toString(), values.toArray());
		
		return retorno.intValue() > 0;
	}	


	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return AliquotaContribuicaoServ.class;
    }

	public void deleteFilhos(Long idAliquotaContribuico) {
		if (idAliquotaContribuico != null) {
			StringBuilder query = new StringBuilder();
			query.append("delete from ");
			query.append(AliquotaContribuicaoServMunic.class.getName());
			query.append(" acsm ");
			query.append("where acsm.aliquotaContribuicaoServ.idAliquotaContribuico in (:id) ");
		
			this.getAdsmHibernateTemplate().removeById(query.toString(), idAliquotaContribuico);
		}
	}
    
    /**
     * Busca quantos registros existem em aliquotaContribuicaoServ de acordo com os critérios passados por parâmetro
     * @param tpImposto Tipo do imposto (INSS, ISS, etc)
     * @param idServicoAdicional Identificador do Serviço Adicional
     * @param idServicoTributo Identificador do Serviço Tributo
     * @param idCliente Identificador do Cliente
     * @param dtBase Data Base
     * @return Quantidade de registro de AliquotaContribuicaoServ resultantes da pesquisa
     */
	public AliquotaContribuicaoServ findAliquotaContribuicaoServ(String tpImposto, Long idServicoAdicional, Long idServicoTributo, Long idCliente, YearMonthDay dtBase,
			Long idMunicipioIncidencia) {
        
		SqlTemplate hql = new SqlTemplate();

		hql.addProjection("acs");
        
		hql.addFrom(AliquotaContribuicaoServ.class.getName() + " as acs " 
				+ "inner join fetch acs.pessoa pes " 
				+ "left outer join fetch acs.servicoAdicional serva "
				+ "left outer join fetch acs.servicoTributo servt ");

		hql.addFrom(DomainValue.class.getName() + " as dv join dv.domain d");
		hql.addJoin("dv.value", "acs.tpImposto");
		hql.addCriteria("d.name", "=", "DM_TIPO_IMPOSTO");
        
		hql.addCriteria("acs.tpImposto", "=", tpImposto);
		hql.addCriteria("serva.id", "=", idServicoAdicional);
		hql.addCriteria("servt.id", "=", idServicoTributo);
		hql.addRequiredCriteria("pes.idPessoa", "=", idCliente);
        
		hql.addCustomCriteria("(acs.dtVigenciaInicial <= ? and acs.dtVigenciaFinal >= ?)");
		hql.addCriteriaValue(dtBase);
		hql.addCriteriaValue(dtBase);
        
		/*
		 * LMS-5109
		 * E (
		 * idMunicipioIncidencia = ( Buscar ENDERECO_PESSOA.ID_MUNICIPIO Da
		 * tabela PESSOA, ENDERECO_PESSOA Onde PESSOA.ID_PESSOA = idCliente E
		 * PESSOA.ID_ENDERECO_PESSOA = ENDERECO_PESSOA.ID_ENDERECO_PESSOA )
		 * 
		 * OU
		 * 
		 * idMunicipioIncidencia EM ( Buscar
		 * ALIQUOTA_CONTRIB_SERV_MUNIC.ID_MUNICIPIO Da tabela
		 * ALIQUOTA_CONTRIB_SERV_MUNIC Onde
		 * ALIQUOTA_CONTRIB_SERV_MUNIC.ID_ALIQUOTA_CONTRIBUICAO_SERV =
		 * ALIQUOTA_CONTRIBUICAO_SERV.ID_ALIQUOTA_CONTRIBUICAO_SERV )
		 * )
		 */
		hql.addCustomCriteria("( ? = (select m.idMunicipio from " + Pessoa.class.getName()
				+ " as p join p.enderecoPessoa ep join ep.municipio m where p.idPessoa = pes.idPessoa ) OR " 
				+ " ? in (select m2.idMunicipio from " + AliquotaContribuicaoServMunic.class.getName()
				+ " as acsm join acsm.municipio m2 where acsm.aliquotaContribuicaoServ.idAliquotaContribuico = acs.idAliquotaContribuico) )");
		hql.addCriteriaValue(idMunicipioIncidencia);
		hql.addCriteriaValue(idMunicipioIncidencia);
        
		List result = this.getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());

        if (result == null || result.isEmpty() || result.size() > 1) {
        	return null;
    }
		
        return (AliquotaContribuicaoServ) result.get(0);
    }
    
    /**
     * Busca as aliquotas contribuicao Servico de acordo com os critérios de pesquisa
     */
    public ResultSetPage findPaginated(TypedFlatMap map, FindDefinition findDef) {
        
        SqlTemplate sql = montaQueryListagem(map);
    	
    	sql.addProjection("acs");

        ResultSetPage rsp = getAdsmHibernateTemplate().findPaginated(sql.getSql(true), 
                                                                     findDef.getCurrentPage(), 
                                                                     findDef.getPageSize(),
                                                                     sql.getCriteria());
        
        return rsp;
    }
    
    /**
     * Utiliza a mesma query da listagem para contar quantos registros serão exibidos
     * @param tfm Critérios de pesquisa
     * @return Inteiro que representa o número de registros a serem exibidos
     */
    public Integer getRowCount(TypedFlatMap tfm) {    	
    	
    	SqlTemplate sql = montaQueryListagem(tfm);
    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(),sql.getCriteria());
    	
    }
    
    /**
     * Monta query para a listagem e para o rowCount
     * @param tfm Critérios de pesquisa 
     * @return SqlTemplate com a query e os parâmetros da pesquisa
     */
    private SqlTemplate montaQueryListagem(TypedFlatMap tfm){
    	
    	SqlTemplate sql = new SqlTemplate();
        
        sql.addFrom(AliquotaContribuicaoServ.class.getName() + " as acs " +
                    "left outer join fetch acs.pessoa pes " +
                    "left outer join fetch acs.servicoAdicional serva " +
                    "left outer join fetch acs.servicoTributo servt ");
        
        sql.addFrom(DomainValue.class.getName() + " as dv join dv.domain d");
        
        sql.addJoin("dv.value","acs.tpImposto");        
        
        sql.addCriteria("d.name","=","DM_TIPO_IMPOSTO");
       
        sql.addCriteria("acs.tpImposto","=",tfm.getString("tpImposto"));
        
        if(tfm.getYearMonthDay("dtVigencia") != null){
            sql.addCustomCriteria("( acs.dtVigenciaInicial <= ? " +
                                  "  and acs.dtVigenciaFinal >= ?)" );
     
            sql.addCriteriaValue(tfm.getYearMonthDay("dtVigencia"));             
            sql.addCriteriaValue(JTDateTimeUtils.maxYmd(tfm.getYearMonthDay("dtVigencia")));
        }
        
        sql.addCriteria("acs.vlPiso","=", tfm.getBigDecimal("vlPiso"));
        
        sql.addCriteria("acs.pcAliquota","=",tfm.getBigDecimal("pcAliquota"));
        
        sql.addCriteria("serva.id","=",tfm.getLong("servicoAdicional.idServicoAdicional"));
        
        sql.addCriteria("servt.id","=",tfm.getLong("servicoTributo.idServicoTributo"));
               
        sql.addCriteria("pes.idPessoa","=",tfm.getLong("pessoa.idPessoa"));
        
        sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description",LocaleContextHolder.getLocale()));
        sql.addOrderBy("acs.dtVigenciaInicial");
        sql.addOrderBy(OrderVarcharI18n.hqlOrder("serva.dsServicoAdicional", LocaleContextHolder.getLocale()));  
        sql.addOrderBy("servt.dsServicoTributo");
        sql.addOrderBy("pes.nmPessoa"); 
        
        return sql;
        
    }
}