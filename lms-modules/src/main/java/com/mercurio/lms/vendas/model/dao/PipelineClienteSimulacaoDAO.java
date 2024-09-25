package com.mercurio.lms.vendas.model.dao;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.vendas.dto.PipelineClienteSimulacaoDTO;
import com.mercurio.lms.vendas.model.PipelineCliente;
import com.mercurio.lms.vendas.model.PipelineClienteSimulacao;

public class PipelineClienteSimulacaoDAO extends BaseCrudDao<PipelineClienteSimulacao, Long> {

	@Override
	protected Class<?> getPersistentClass() {
		return PipelineClienteSimulacao.class;
	}
	
	public Boolean findExistenciaTabelaOuSimulacaoByPipelineCliente(PipelineCliente pipelineCliente) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select count(pics) ");
		hql.append("from PipelineClienteSimulacao pics ");
		hql.append("left join pics.tabelaPreco tapr ");
		hql.append("left join pics.simulacao simu ");
		hql.append("where pics.pipelineCliente = ? ");
		hql.append("and (tapr.blEfetivada = 'S' or simu.dtEfetivacao is not null or simu.blEfetivada = 'S') ");
		params.add(pipelineCliente);
		
		Long value = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
		
		return (value != null && value > 0);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria, FindDefinition findDefinition) {
		List result1 = null;
		List result2 = null;
		
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		params.add(criteria.getLong("idPipelineCliente"));
		
		hql.append("select new Map(tapr.dtVigenciaInicial as dataPropostaSimulacao ");
		hql.append("      ,'' as numeroProposta ");
		hql.append("      ,'' as nmPessoaCliente ");
		hql.append("      ,'' as divisao ");
		hql.append("      ,(select ttp.tpTipoTabelaPreco || ttp.nrVersao || '-' || stp.tpSubtipoTabelaPreco ");
		hql.append("          		from TipoTabelaPreco ttp, SubtipoTabelaPreco stp ");
		hql.append("            	where ttp.idTipoTabelaPreco = tapr.tipoTabelaPreco.id ");
		hql.append("            	and   stp.idSubtipoTabelaPreco = tapr.subtipoTabelaPreco.id) as tabela ");
		hql.append("      ,'' as situacao ");
		hql.append("      ,tapr.blEfetivada as blEfetivada" );
		hql.append("      ,pcsi.idPipelineClienteSimulacao as idPipelineClienteSimulacao)  ");
		hql.append("from PipelineClienteSimulacao pcsi ");
		hql.append("left join pcsi.tabelaPreco as tapr ");
		hql.append("where tapr.idTabelaPreco is not null and pcsi.pipelineCliente.idPipelineCliente = ? ");

		result1 = getAdsmHibernateTemplate().findPaginated(hql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), params.toArray()).getList();
		
		hql = new StringBuffer();
		hql.append("select new Map(simu.dtSimulacao as dataPropostaSimulacao ");
		hql.append("      ,fill.sgFilial || ' ' || simu.nrSimulacao as numeroProposta ");
		hql.append("      ,clpe.nmFantasia as nmPessoaCliente ");
		hql.append("      ,dicl.dsDivisaoCliente as divisao ");
		hql.append("      ,(select ttp.tpTipoTabelaPreco || ttp.nrVersao || '-' || stp.tpSubtipoTabelaPreco ");
		hql.append("                	from TipoTabelaPreco ttp, SubtipoTabelaPreco stp, TabelaPreco tp ");
		hql.append("                	where tp.idTabelaPreco = simu.tabelaPreco ");
		hql.append("                	and   ttp.idTipoTabelaPreco = tp.tipoTabelaPreco.id ");
		hql.append("                	and   stp.idSubtipoTabelaPreco = tp.subtipoTabelaPreco.id) as tabela ");
		hql.append("      ,simu.tpSituacaoAprovacao as situacao ");
		hql.append("      ,simu.blEfetivada as blEfetivada ");
		hql.append("      ,pcsi.idPipelineClienteSimulacao as idPipelineClienteSimulacao)  ");
		hql.append("from PipelineClienteSimulacao pcsi ");
		hql.append("left join pcsi.simulacao as simu ");
		hql.append("left join simu.filial as fill ");
		hql.append("left join simu.clienteByIdCliente as clie ");
		hql.append("left join clie.pessoa clpe ");
		hql.append("left join simu.divisaoCliente as dicl ");
		hql.append("where simu.tabelaPreco.id is not null and pcsi.pipelineCliente.idPipelineCliente = ? ");
		result2 = getAdsmHibernateTemplate().findPaginated(hql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), params.toArray()).getList();
		result1.addAll(result2);
		return new ResultSetPage(1, result1); 
	}

	public Integer getRowCountCustom(TypedFlatMap criteria) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select count(pcsi.idPipelineClienteSimulacao) ");
		hql.append("from PipelineClienteSimulacao pcsi ");
		hql.append("where pcsi.pipelineCliente.cliente.idCliente = ? ");
		params.add(criteria.getLong("cliente.idCliente"));

		Long result = (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), params.toArray());
		
		return (result != null ? result.intValue() : 0);  
	}

	public PipelineClienteSimulacaoDTO findToEdit(Long idPipelineClienteSimulacao) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		hql.append("select new Map(fill.idFilial as idFilial ");
		hql.append("      ,fill.sgFilial as sgFilial ");
		hql.append("      ,pefi.nmFantasia as nmFantasiaFilial");
		hql.append("      ,simu.nrSimulacao as nrProposta ");
		hql.append("      ,tapr.idTabelaPreco as idTabelaPreco ");
		hql.append("      ,(titp.tpTipoTabelaPreco  || titp.nrVersao || '-' || sutp.tpSubtipoTabelaPreco) as tabelaPrecoString ");
		hql.append("      ,tapr.dsDescricao as dsDescricaoTabelaPreco ");
		hql.append("      ,pcsi.idPipelineClienteSimulacao as idPipelineClienteSimulacao)  ");
		hql.append("from PipelineClienteSimulacao pcsi ");
		hql.append("left join pcsi.tabelaPreco as tapr ");
		hql.append("left join tapr.tipoTabelaPreco as titp ");
		hql.append("left join tapr.subtipoTabelaPreco as sutp ");
		hql.append("left join pcsi.simulacao as simu ");
		hql.append("left join simu.filial as fill ");
		hql.append("left join fill.pessoa as pefi ");
		hql.append("where pcsi.idPipelineClienteSimulacao = ? ");
		params.add(idPipelineClienteSimulacao);
		
		List result = getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
		if (result != null && !result.isEmpty()) {
			AliasToNestedBeanResultTransformer transformer = new AliasToNestedBeanResultTransformer(PipelineClienteSimulacaoDTO.class);
			List transformed = transformer.transformListResult(result);
			return (PipelineClienteSimulacaoDTO) transformed.get(0);
		}
		return null;
	}

	public List findPipelineEtapaByIdPipelineCliente(Long idPipelineCliente) {
		StringBuffer hql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		hql.append("select pics ");
		hql.append("from PipelineClienteSimulacao pics ");
		hql.append("join pics.pipelineCliente as picl ");
		hql.append("where picl.idPipelineCliente = ? ");
		params.add(idPipelineCliente);
		return getAdsmHibernateTemplate().find(hql.toString(), params.toArray());
	}
	
}
