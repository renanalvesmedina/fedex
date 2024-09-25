package com.mercurio.lms.vendas.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.joda.time.TimeOfDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.OrderVarcharI18n;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.tabelaprecos.model.RestricaoRota;
import com.mercurio.lms.tabelaprecos.util.RotaPrecoUtils;
import com.mercurio.lms.vendas.model.HorarioCorteCliente;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class HorarioCorteClienteDAO extends BaseCrudDao<HorarioCorteCliente, Long> {

	private Logger log = LogManager.getLogger(this.getClass());

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return HorarioCorteCliente.class;
    }

    protected void initFindByIdLazyProperties(Map lazyFindById) {
    	lazyFindById.put("servico",FetchMode.JOIN);
    	lazyFindById.put("zonaOrigem",FetchMode.JOIN);
    	lazyFindById.put("paisOrigem",FetchMode.JOIN);    	
    	lazyFindById.put("unidadeFederativaOrigem",FetchMode.JOIN);
    	lazyFindById.put("filialOrigem",FetchMode.JOIN);    	
    	lazyFindById.put("filialOrigem.pessoa",FetchMode.JOIN);
    	lazyFindById.put("municipioOrigem",FetchMode.JOIN);
    	lazyFindById.put("enderecoPessoaOrigem",FetchMode.JOIN);
    	lazyFindById.put("zonaDestino",FetchMode.JOIN);
    	lazyFindById.put("paisDestino",FetchMode.JOIN);    	
    	lazyFindById.put("unidadeFederativaDestino",FetchMode.JOIN);
    	lazyFindById.put("filialDestino",FetchMode.JOIN);    	
    	lazyFindById.put("filialDestino.pessoa",FetchMode.JOIN);
    	lazyFindById.put("municipioDestino",FetchMode.JOIN);
    	lazyFindById.put("enderecoPessoaDestino",FetchMode.JOIN);
    }

    @Override
    public Integer getRowCount(Map criteria) {
    	SqlTemplate sql = getHqlBasico(criteria);    	
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(false),sql.getCriteria());
    }

    public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
        SqlTemplate sql = getHqlBasico(criteria);
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(true), findDef.getCurrentPage(), findDef.getPageSize(),sql.getCriteria());    	
    }

    private SqlTemplate getHqlBasico(Map criteria){
    	SqlTemplate sql = new SqlTemplate();	
    	try {
			sql.addProjection("hcc");
			sql.addFrom(HorarioCorteCliente.class.getName()
					+ " hcc left join fetch hcc.filialOrigem "
					+ " left join fetch hcc.filialDestino "
					+ " left join fetch hcc.filialOrigem.pessoa "
					+ " left join fetch hcc.filialDestino.pessoa "
					+ " join hcc.cliente "
					+ " left join fetch hcc.servico as se "
					+ " left join fetch hcc.municipioOrigem "
					+ " left join fetch hcc.unidadeFederativaOrigem "
					+ " left join fetch hcc.municipioDestino "
					+ " left join fetch hcc.unidadeFederativaDestino "
					);
			sql.addFrom(DomainValue.class.getName() + " dv join dv.domain do");		
			sql.addJoin("hcc.tpHorario","dv.value");  	

			sql.addCriteria("do.name","like","DM_TIPO_HORARIO_CORTE");
			sql.addCriteria("hcc.cliente.id","=", MapUtils.getLong(MapUtils.getMap(criteria,"cliente"), "idCliente"));
			sql.addCriteria("se.id","=", criteria.get("servico"));
			sql.addCriteria("hcc.tpHorario","like", criteria.get("tpHorario"));		   

			sql.addOrderBy(OrderVarcharI18n.hqlOrder("dv.description", LocaleContextHolder.getLocale()));
			sql.addOrderBy(OrderVarcharI18n.hqlOrder("hcc.servico.dsServico", LocaleContextHolder.getLocale()));
			sql.addOrderBy("hcc.unidadeFederativaOrigem.nmUnidadeFederativa");
			sql.addOrderBy("hcc.filialOrigem.pessoa.nmPessoa");
			sql.addOrderBy("hcc.municipioOrigem.nmMunicipio");		
			sql.addOrderBy("hcc.unidadeFederativaDestino.nmUnidadeFederativa");
			sql.addOrderBy("hcc.filialDestino.pessoa.nmPessoa");
			sql.addOrderBy("hcc.municipioDestino.nmMunicipio");	
        } catch (Exception e){
     	   	log.error(e);
        }
        return sql;
    }

    /**
	 * Busca o Horário de Corte de Coleta do Cliente
	 * @author Andre Valadas
	 * 
	 * @param idCliente
	 * @param idServico
	 * @param restricaoRota
	 * @return
	 */
    public List<HorarioCorteCliente> findHorarioCorteColeta(Long idCliente, Long idServico, RestricaoRota restricaoRota) {
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(), "hcc");

		/** Filtro */
		dc.add(Restrictions.eq("hcc.tpHorario", "C"));//Coleta
		dc.add(Restrictions.eq("hcc.cliente.id", idCliente));
		dc.add(Restrictions.or(Restrictions.eq("hcc.servico.id", idServico), Restrictions.isNull("hcc.servico.id")));
		//Rota
		dc.add(Restrictions.or(RotaPrecoUtils.getCriterion("hcc.municipioOrigem.id", restricaoRota.getIdMunicipio())
				, RotaPrecoUtils.getCriterion("hcc.municipioDestino.id", restricaoRota.getIdMunicipio())));
		dc.add(Restrictions.or(RotaPrecoUtils.getCriterion("hcc.filialOrigem.id", restricaoRota.getIdFilial())
				, RotaPrecoUtils.getCriterion("hcc.filialDestino.id", restricaoRota.getIdFilial())));
		dc.add(Restrictions.or(RotaPrecoUtils.getCriterion("hcc.unidadeFederativaOrigem.id", restricaoRota.getIdUnidadeFederativa())
				, RotaPrecoUtils.getCriterion("hcc.unidadeFederativaDestino.id", restricaoRota.getIdUnidadeFederativa())));
		dc.add(Restrictions.or(RotaPrecoUtils.getCriterion("hcc.paisOrigem.id", restricaoRota.getIdPais())
				, RotaPrecoUtils.getCriterion("hcc.paisDestino.id", restricaoRota.getIdPais())));
		dc.add(Restrictions.or(RotaPrecoUtils.getCriterion("hcc.zonaOrigem.id", restricaoRota.getIdZona())
				, RotaPrecoUtils.getCriterion("hcc.zonaDestino.id", restricaoRota.getIdZona())));

		List<HorarioCorteCliente> result = (List<HorarioCorteCliente>) super.findByDetachedCriteria(dc);
		return result;
	}

	/**
	 * Método que busca um HorarioCorteCliente a partir do ID do EnderecoPessoa e da Hora Atual.
	 * @param idEnderecoPessoa
	 * @param horaAtual
	 * @return HorarioCorteCliente
	 */
	public HorarioCorteCliente findHorarioCorteClienteByIdEnderecoPessoaByHoraAtual(Long idEnderecoPessoa, TimeOfDay horaAtual) {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select hcc ");
		sql.append(" from HorarioCorteCliente as hcc ");
		sql.append(" where (hcc.enderecoPessoaOrigem.id = :id OR hcc.enderecoPessoaDestino.id = :id) ");
		sql.append(" and hcc.hrFinal >= :hrAtual ");
		
		Map params = new HashMap<String, Object>();
		params.put("id", idEnderecoPessoa);
		params.put("hrAtual", horaAtual);
		
		return (HorarioCorteCliente) getAdsmHibernateTemplate().findUniqueResult(sql.toString(), params);
	}
	
	/**
	 * Remove todas os itens relacionados ao cliente informado.
	 * @param idCliente identificador do cliente
	 */
	public void removeByIdCliente(Long idCliente) {
    	StringBuilder hql = new StringBuilder()
    	.append(" DELETE ").append(getPersistentClass().getName())
    	.append(" WHERE cliente.id = :id");
    	
    	getAdsmHibernateTemplate().removeById(hql.toString(), idCliente);
    }
}
