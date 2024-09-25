package com.mercurio.lms.vendas.model.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.CptComplexidade;
import com.mercurio.lms.vendas.model.CptValor;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class CptValorDAO extends BaseCrudDao<CptValor, Long> {

	protected final Class getPersistentClass() {
		return CptValor.class;
	}

	@Override
	protected void initFindByIdLazyProperties(Map map) {		
        map.put("cliente", FetchMode.JOIN);
        map.put("cliente.pessoa", FetchMode.JOIN);
	}
	
	private Long[] getDataMap(Map criteria){
		Map mapCliente = (Map)MapUtils.getObject(criteria,"cliente");		
		Map mapSegmentoMercado = (Map)MapUtils.getObject(criteria,"segmentoMercado");		
		
		Long[] data = new Long[3];
		
		data[0] = MapUtils.getLong(mapCliente, "idCliente");
		data[1] = MapUtils.getLong(mapSegmentoMercado, "idSegmentoMercado");
		data[2] = MapUtils.getLong(criteria, "cptTipoValor");
		
		return data;
	} 
	
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDefinition) {
		
		Long[] data = getDataMap(criteria);	
		
		List listaCptMedida  	 = new ArrayList();
		List listaCptValor   	 = new ArrayList();
		List listaCptVeiculo 	 = new ArrayList();
		List listaCptFuncionario = new ArrayList();
		if(data[1] != null){			
			listaCptMedida 	 = getAdsmHibernateTemplate().find(getSQLCPTMedida(data).toString());			
		}else{		
			if(data[2] == null){
				listaCptMedida 	 = getAdsmHibernateTemplate().find(getSQLCPTMedida(data).toString());
			}
			listaCptValor    = getAdsmHibernateTemplate().find(getSQLCPTValor(data).toString());			
			listaCptVeiculo  = getAdsmHibernateTemplate().find(getSQLCPTVeiculo(data).toString());			
			listaCptFuncionario = getAdsmHibernateTemplate().find(getSQLCPTFuncionario(data).toString());
		}
		
		if(!listaCptMedida.isEmpty()){
			buildComplexidade(listaCptMedida);
		}
		
					
		List list = new ArrayList();
		list.addAll(listaCptValor);
		list.addAll(listaCptMedida);
		list.addAll(listaCptVeiculo);
		list.addAll(listaCptFuncionario);
		
		return new ResultSetPage(findDefinition.getCurrentPage() ,list);
	}
	
	
	public void buildComplexidade(List<Map> list){
		CptComplexidade com = null;
		for(Map mp : list){
			com = findComplexidade(LongUtils.getLong(mp.get("medida")));
			mp.put("medida", com.getTpComplexidade().getDescriptionAsString() + " " + 
					com.getVlComplexidade() + " " + com.getTpMedidaComplexidade().getDescriptionAsString());
		}
	}
	
	/**
	 * Obtem a complexidade só para montar na grid 
	 * @param id
	 * @return
	 */
	public CptComplexidade findComplexidade(Long id){
		DetachedCriteria dc = DetachedCriteria.forClass(CptComplexidade.class)
		.add(Restrictions.eq("id", id));
		
		return (CptComplexidade)getAdsmHibernateTemplate().findUniqueResult(dc);
	}
		
	/**
	 * Retorno CPT_FUNCIONARIO
	 * @param criteria
	 * @return
	 */
	private StringBuilder getSQLCPTFuncionario(Long... data){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select new  map(");
		sql.append(" (cpt.idCptFuncionario || '_CptFuncionario')as identificador, ");
		sql.append(" pes.nmPessoa as nmcliente, ");
		sql.append(" cli.idCliente as idcliente, ");
		sql.append(" '' as segmento, ");
		sql.append(" tiv.dsTipoValor as tipovalor, ");
		sql.append(" '' as valor, ");
		sql.append(" '' as medida, ");
		sql.append(" '' as vlmedida, ");		
		sql.append(" '' as veiculo, ");
		sql.append(" cpt.nrMatricula as nrmatricula");
		sql.append(")");
		sql.append(" from CptFuncionario cpt ");
		sql.append(" inner join cpt.cptTipoValor as tiv ");
		sql.append(" inner join cpt.cliente as cli ");
		sql.append(" inner join cli.pessoa as pes ");
		
		addSQLFilter(sql,data);
		
		return sql;
	}
		
	/**
	 * Retorna dados CPT_VEICULO
	 * @param criteria
	 * @return
	 */
	private StringBuilder getSQLCPTVeiculo(Long... data){			
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select new  map(");
		sql.append(" (cpt.idCptVeiculo || '_CptVeiculo') as identificador, ");
		sql.append(" pes.nmPessoa as nmcliente, ");
		sql.append(" cli.idCliente as idcliente, ");
		sql.append(" '' as segmento, ");
		sql.append(" tiv.dsTipoValor as tipovalor, ");
		sql.append(" '' as valor, ");
		sql.append(" '' as medida, ");
		sql.append(" '' as vlmedida, ");
		sql.append(" cpt.nrFrota as veiculo, ");
		sql.append(" '' as nrmatricula");
		sql.append(")");
		sql.append(" from CptVeiculo cpt ");
		sql.append(" inner join cpt.cptTipoValor as tiv ");
		sql.append(" inner join cpt.cliente as cli ");
		sql.append(" inner join cli.pessoa as pes ");
		
		addSQLFilter(sql,data);
		
		return sql;
	}
	
	/**
	 * Retorn o SQL da tabela CPT_MEDIDA
	 * @param criteria
	 * @return
	 */
	private StringBuilder getSQLCPTMedida(Long... data){
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select new  map(");
		sql.append(" (cpt.idCptMedida || '_CptMedida')as identificador, ");
		sql.append(" pes.nmPessoa as nmcliente, ");
		sql.append(" cli.idCliente as idcliente, ");
		sql.append(" seg.dsSegmentoMercado as segmento, ");
		sql.append(" tiv.dsTipoValor as tipovalor, ");
		sql.append(" '' as valor, ");
		sql.append(" com.id as medida, ");
		sql.append(" cpt.vlMedida as vlmedida, ");
		sql.append(" '' as veiculo, ");
		sql.append(" '' as nrmatricula");
		sql.append(")");
		sql.append(" from CptMedida cpt ");
		sql.append(" left  join cpt.cliente as cli ");
		sql.append(" left  join cli.pessoa as pes ");		
		sql.append(" left  join cpt.segmentoMercado as seg ");
		sql.append(" inner join cpt.cptComplexidade as com ");				
		sql.append(" inner join com.cptTipoValor    as tiv ");				
		
		addSQLFilter(sql,data);
		
		return sql;
	}
	
	/**
	 * Retorna o sql da tabela CPT_VALOR
	 * @param criteria
	 * @return
	 */
	private StringBuilder getSQLCPTValor(Long... data){	
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select new  map(");
		sql.append(" (cpt.idCptValor || '_CptValor') as identificador, ");
		sql.append(" pes.nmPessoa as nmcliente, ");
		sql.append(" cli.idCliente as idcliente, ");
		sql.append(" '' as segmento, ");
		sql.append(" tiv.dsTipoValor as tipovalor, ");
		sql.append(" cpt.vlValor as valor, ");
		sql.append(" '' as medida, ");
		sql.append(" '' as vlmedida, ");
		sql.append(" '' as veiculo, ");
		sql.append(" '' as nrmatricula");
		sql.append(")");
		sql.append(" from CptValor cpt ");
		sql.append(" inner join cpt.cptTipoValor as tiv ");
		sql.append(" inner join cpt.cliente as cli ");
		sql.append(" inner join cli.pessoa as pes ");
		
		addSQLFilter(sql,data);
		
		return sql;		
	}
	
	private void addSQLFilter(StringBuilder sql,Long... data){
		
		Long idCliente = data[0];
		Long idSegmentoMercado = data[1];
		Long idCptTipoValor = data[2];
				
		String filters  = "";
		if(idCliente !=null || idCptTipoValor !=null || idSegmentoMercado != null){			
			int filterCount = 0;						
			if(idCliente != null){
				filters +=  "cli.idCliente = "+idCliente;
				filterCount++;
			}				
			if(idSegmentoMercado != null){
				if(filterCount > 0){
					filters += " and " ;
				}
				filters += " seg.idSegmentoMercado = "+idSegmentoMercado ;
				filterCount++;
			}else{								
				if(idCptTipoValor != null 
						&& !sql.toString().contains("CptMedida") 
							&& !sql.toString().contains("CptFuncionario")){
					if(filterCount > 0){
						filters += " and " ;
					}
					filters += " tiv.idCptTipoValor ="+idCptTipoValor ;					
				}		
			}
		}
		if(StringUtils.isNotBlank(filters)){
			sql.append(" WHERE ").append(filters);
		}
	}
	
	public Integer getRowCount(Map criteria) {
		
		Long[] data = getDataMap(criteria);	
		
		Integer totalMedida  = 0;
		Integer totalValor   = 0;
		Integer totalVeiculo = 0; 	 
		Integer totalFuncionario = 0; 
		if(data[1] != null){			
			totalMedida  	 = getAdsmHibernateTemplate().getRowCountForQuery(getSQLCPTMedida(data).toString());
		}else{
			totalMedida  	 = getAdsmHibernateTemplate().getRowCountForQuery(getSQLCPTMedida(data).toString());
			totalValor   	 = getAdsmHibernateTemplate().getRowCountForQuery(getSQLCPTValor(data).toString());
			totalVeiculo 	 = getAdsmHibernateTemplate().getRowCountForQuery(getSQLCPTVeiculo(data).toString());
			totalFuncionario = getAdsmHibernateTemplate().getRowCountForQuery(getSQLCPTFuncionario(data).toString());
		}
				
		return  totalValor + totalMedida + totalVeiculo + totalFuncionario;
	}

	public List findValorCliente(Long idCliente, BigDecimal valor) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass())
		.add(Restrictions.eq("cliente.id", idCliente))
		.add(Restrictions.eq("vlValor", valor));
		
		return getAdsmHibernateTemplate().findByCriteria(dc);
	}	
				
}