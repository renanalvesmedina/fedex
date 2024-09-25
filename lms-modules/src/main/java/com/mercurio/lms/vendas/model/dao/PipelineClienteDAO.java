package com.mercurio.lms.vendas.model.dao;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.PipelineEtapa;


/**
 * DAO pattern.
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class PipelineClienteDAO extends BaseCrudDao<PipelineCliente, Long> {

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
	protected final Class getPersistentClass() {
		return PipelineCliente.class;
	}

	protected void initFindByIdLazyProperties(Map lazyFindById) {
		lazyFindById.put("cliente", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa.enderecoPessoa", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa.enderecoPessoa.municipio", FetchMode.JOIN);
		lazyFindById.put("cliente.pessoa.enderecoPessoa.municipio.unidadeFederativa", FetchMode.JOIN);

		lazyFindById.put("filial", FetchMode.JOIN);
		lazyFindById.put("filial.pessoa", FetchMode.JOIN);

		lazyFindById.put("usuarioByIdUsuario", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuario.vfuncionario", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuario.vfuncionario.filial", FetchMode.JOIN);
		lazyFindById.put("usuarioByIdUsuario.vfuncionario.filial.pessoa", FetchMode.JOIN);

		lazyFindById.put("usuarioByIdUsuarioVisto", FetchMode.JOIN);
		lazyFindById.put("segmentoMercado", FetchMode.JOIN);
		
		
	}

	/**
	 * Consulta visita para grid
	 * @param idVisita
	 * @return
	 */
	public ResultSetPage findPaginated(Map criteria, FindDefinition findDef) {
		SqlTemplate sql = montaSqlPaginated((TypedFlatMap) criteria);
		
		sql.addProjection(" distinct new Map(pc.idPipelineCliente", "idPipelineCliente");
		
		//dtPerda
		sql.addProjection("(select min(pec.dtEvento) from PipelineEtapa pec inner join pec.pipelineCliente pci where pci.idPipelineCliente=pc.idPipelineCliente ) ", "dtEvento");

		//dtUltimaAtualizacao
		sql.addProjection("(select max(pec.dtEvento) from PipelineEtapa pec inner join pec.pipelineCliente pci where pci.idPipelineCliente=pc.idPipelineCliente ) ", "dtUltimaAtualizacao");
		
		//cliente
		sql.addProjection("case when c.idCliente != null then p_c.nmPessoa else pc.nmCliente end ", "cliente_pessoa_nmPessoa");
		
		
		//usuario 
		sql.addProjection("usu.idUsuario", "usuarioByIdUsuario_idUsuario");
		sql.addProjection("usu.nmUsuario", "usuarioByIdUsuario_nmUsuario");
		
		//filial
		sql.addProjection("f.idFilial", "filial_idFilial");
		sql.addProjection("f.sgFilial", "filial_sgFilial)");
		
		sql.addOrderBy("2");
		
		return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDef.getCurrentPage(),findDef.getPageSize(),sql.getCriteria());
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		SqlTemplate sql = montaSqlPaginated(criteria);
		sql.addProjection("count(distinct pc.idPipelineCliente) ");
		Long rowCountQuery = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(),sql.getCriteria());
		return Integer.valueOf(rowCountQuery.toString());
	}

	/**
	 * Comando SQL de consulta de visitas
	 * @param idVisita
	 * @return
	 */
	private SqlTemplate montaSqlPaginated(TypedFlatMap parametros){
		SqlTemplate sql = new SqlTemplate();
		
		StringBuffer from = new StringBuffer(PipelineCliente.class.getName() + " pc ");
		from.append(" left outer join pc.filial f inner join pc.usuarioByIdUsuario usu inner join usu.vfuncionario vfun left outer join pc.listPipelineEtapas pe");
		from.append(" left outer join pc.cliente c left outer join c.pessoa p_c ");
		
		if(parametros.getLong("cliente.idCliente")!= null){
			sql.addCriteria("c.idCliente", "=", parametros.getLong("cliente.idCliente"));
		}else if(!parametros.getString("cliente.pessoa.nmPessoa").equals("")){
			sql.addCriteria("upper(pc.nmCliente)", "like", parametros.getString("cliente.pessoa.nmPessoa").toUpperCase());
		}
		
		if(!parametros.getDomainValue("tpAbrangencia").getValue().equals("")){
			from.append(" left outer join pc.listPipelineReceita pr ");
			sql.addCriteria("pr.tpAbrangencia", "=", parametros.getDomainValue("tpAbrangencia"));
		}
		
		if(parametros.getLong("cliente.idCliente")!= null){
			sql.addCriteria("c.idCliente", "=", parametros.getLong("cliente.idCliente"));
		}
		
	   if(parametros.getYearMonthDay("dtInicial")!= null && parametros.getYearMonthDay("dtFinal")!= null){
			sql.addCustomCriteria("pe.dtEvento >=(select min(pec.dtEvento) from "+ PipelineEtapa.class.getName() +" pec inner join pec.pipelineCliente pci where pci.idPipelineCliente=pc.idPipelineCliente) and pe.dtEvento >= ? and pe.dtEvento <= ? and (pe.tpPipelineEtapa='35' or pc.dtPerda <=?)");
		    sql.addCriteriaValue(parametros.getYearMonthDay("dtInicial"));
			sql.addCriteriaValue(parametros.getYearMonthDay("dtFinal"));
			sql.addCriteriaValue(parametros.getYearMonthDay("dtFinal"));
		}else if(parametros.getYearMonthDay("dtInicial")!= null){
			sql.addCustomCriteria("pe.dtEvento=(select min(pec.dtEvento) from "+ PipelineEtapa.class.getName() +" pec inner join pec.pipelineCliente pci where pci.idPipelineCliente=pc.idPipelineCliente) and pe.dtEvento >= ?",parametros.getYearMonthDay("dtInicial"));
		}else if(parametros.getYearMonthDay("dtFinal")!= null){
			sql.addCriteria("pe.tpPipelineEtapa", "=", "35");
			sql.addCustomCriteria(" pe.dtEvento <= ? OR pc.dtPerda <=? ",parametros.getYearMonthDay("dtFinal"));
			sql.addCriteriaValue(parametros.getYearMonthDay("dtFinal"));
		}
		
		sql.addFrom(from.toString());
		
		if(parametros.getLong("filial.idFilial")!= null){
			sql.addCriteria("f.idFilial", "=", parametros.getLong("filial.idFilial"));
		}
		
		if(parametros.getLong("usuarioByIdUsuario.idUsuario")!= null){
			sql.addCriteria("usu.idUsuario", "=", parametros.getLong("usuarioByIdUsuario.idUsuario"));
		}
	
		if (StringUtils.isNotBlank(parametros.getString("tpSituacao"))) {
			sql.addCriteria("pc.tpSituacao", "=", parametros.getString("tpSituacao"));
		}

		return sql;
	}

}
