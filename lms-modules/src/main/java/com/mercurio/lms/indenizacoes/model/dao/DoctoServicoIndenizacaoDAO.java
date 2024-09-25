package com.mercurio.lms.indenizacoes.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.report.RelatorioIndenizacoesFranqueadosQuery;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class DoctoServicoIndenizacaoDAO extends BaseCrudDao<DoctoServicoIndenizacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return DoctoServicoIndenizacao.class;
    }

   
    /**
     * Método de pesquisa customizado para buscar os doctosServicos baseado no idReciboIndenizacao.
     * Utilizado nas telas de RIM 
     * @param idReciboIndenizacao
     * @param fd
     * @return
     */
    public ResultSetPage findDoctoServicosByIdReciboIndenizacao(Long idReciboIndenizacao, FindDefinition fd) {
    	StringBuffer hql = new StringBuffer(); 
    	hql.append(" select new map(");
    	hql.append(" ds.tpDocumentoServico as doctoServico_tpDocumentoServico,");
    	hql.append(" ds.nrDoctoServico as doctoServico_nrDoctoServico,");
    	hql.append(" destino.sgFilial as doctoServico_filialByIdFilialDestino_sgFilial,");
    	hql.append(" origem.sgFilial as doctoServico_filialByIdFilialOrigem_sgFilial,");
    	hql.append(" remetente.pessoa.nmPessoa as doctoServico_clienteByIdClienteRemetente_pessoa_nmPessoa,");
    	hql.append(" destinatario.pessoa.nmPessoa as doctoServico_clienteByIdClienteDestinatario_pessoa_nmPessoa");
    	hql.append(" )");
    	hql.append(addJoinsTofindDoctoServicosByIdReciboIndenizacao());
    	hql.append(" order by");
    	hql.append(" ds.tpDocumentoServico, ");
    	hql.append(" origem.sgFilial, ");
    	hql.append(" ds.nrDoctoServico ");
    	
		List param = new ArrayList();
		param.add(idReciboIndenizacao);
		
		ResultSetPage rsp = this.getAdsmHibernateTemplate().findPaginated(hql.toString(), fd.getCurrentPage(), fd.getPageSize(),param.toArray());
    	List retorno = AliasToTypedFlatMapResultTransformer.getInstance().transformListResult(rsp.getList());
    	rsp.setList(retorno); 
    	return rsp;
    }
    
    /**
     * Row count customizado para buscar o total de doctosServicos baseado no idReciboIndenizacao.
     * Utilizado nas telas de RIM. 
     * @param idReciboIndenizacao
     * @return
     */
    public Integer getRowCountDoctoServicosByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	StringBuffer hql = new StringBuffer(); 
    	hql.append("select count(*) ");
    	hql.append(addJoinsTofindDoctoServicosByIdReciboIndenizacao());
    	
		List param = new ArrayList();
		param.add(idReciboIndenizacao);
		Long result = (Long)this.getAdsmHibernateTemplate().findUniqueResult(hql.toString(), param.toArray());
		
    	return result.intValue();
    }
    
    /**
     * Método que gera os joins para a pesquisa findDoctoServicosByIdReciboIndenizacao 
     * @return
     */
    private String addJoinsTofindDoctoServicosByIdReciboIndenizacao() {
    	StringBuffer hql = new StringBuffer(); 

    	hql.append(" from "+DoctoServicoIndenizacao.class.getName()+" dsi ");
    	hql.append(" join dsi.doctoServico ds");
    	hql.append(" join dsi.reciboIndenizacao ri");
    	hql.append(" left join ds.filialByIdFilialDestino destino");
    	hql.append(" join ds.filialByIdFilialOrigem origem");
    	hql.append(" join ds.clienteByIdClienteRemetente remetente");
    	hql.append(" left join ds.clienteByIdClienteDestinatario destinatario");
    	hql.append(" where ");
    	hql.append(" ri.idReciboIndenizacao = ? ");
    	
    	
    	return hql.toString();
    	
    }
    
    /**
     * Verifica se existe registros em docto_servico_indenizacao com o docto_servico passado por parametro
     * e que o status do recibo indenizacao dos respectivos docto_servico_indenizacao esteja diferente de cancelado. 
     * @param idDoctoServico
     * @return
     */
    public Integer getRowCountDoctoServicoIndenizacaoNaoCanceladoByIdDoctoServico(Long idDoctoServico) {
    	return getAdsmHibernateTemplate().getRowCountForQuery("from "+DoctoServicoIndenizacao.class.getName()+" dsi where dsi.doctoServico.id = ? and dsi.reciboIndenizacao.tpStatusIndenizacao <> 'C'", new Object[]{idDoctoServico});
    }
    
    /**
     * Find que busca apenas a entidade DoctoServicoIndenizacao (sem fetch) a partir do idReciboIndenizacao
     * @param idReciboIndenizacao
     * @return
     */
    public List findDoctoServicoIndenizacaoByIdReciboIndenizacao(Long idReciboIndenizacao){
    	String sql = new StringBuffer()
    	.append("from "+DoctoServicoIndenizacao.class.getName()+" dsi ")
    	.append("where dsi.reciboIndenizacao.id = ?").toString();  	
    	return getAdsmHibernateTemplate().find(sql, idReciboIndenizacao);
    }
    
    /**
     * Find que busca entidade DoctoServicoIndenizacao (com vários fetch) a partir do idReciboIndenizacao
     * @param idReciboIndenizacao
     * @return
     */
    
    public List findByIdReciboIndenizacao(Long idReciboIndenizacao) {
    	String sql = new StringBuffer()
    	.append("select dsi ")
    	.append("from "+DoctoServicoIndenizacao.class.getName()+" dsi ")
    	.append("inner join fetch dsi.doctoServico ds ")
    	.append("inner join fetch ds.filialByIdFilialOrigem ")
    	.append("left  join fetch ds.filialByIdFilialDestino  ")
    	.append("left  join fetch ds.clienteByIdClienteRemetente cliRem ")
    	.append("left  join fetch ds.clienteByIdClienteDestinatario cliDest ")
    	.append("inner join fetch ds.moeda ")
    	.append("left  join fetch cliRem.pessoa ")
    	.append("left  join fetch cliDest.pessoa ")
    	.append("inner join fetch dsi.moeda ")
    	.append("left  join fetch dsi.produto ")

    	.append("left join fetch ds.sinistroDoctoServicos sds ")
    	
    	/* já se tipo de indenizacao = 'nc' então obtém o manifesto da primeira nao conformidade
    	   isso é tratado na service do método */
    	
    	.append("where (sds.tpPrejuizo IS NULL OR sds.tpPrejuizo <> 'S' OR" +
    			" (sds.tpPrejuizo = 'S' and NOT EXISTS(select 1 from "+SinistroDoctoServico.class.getName() +
    			" sds1 where sds1.doctoServico.id = ds.id and sds1.tpPrejuizo <> 'S') " +
    			"and sds.id = (select min (sds1.id) from "+SinistroDoctoServico.class.getName() +
    			" sds1 where sds1.doctoServico.id = ds.id))) and dsi.reciboIndenizacao.id = ?").toString();
    	
    	return getAdsmHibernateTemplate().find(sql, idReciboIndenizacao);
    }
    
    public Integer getRowCountByIdReciboIndenizacao(Long idReciboIndenizacao) {    	
    	return getAdsmHibernateTemplate().getRowCountForQuery("from "+DoctoServicoIndenizacao.class.getName()+" dsi where dsi.reciboIndenizacao.id = ?", new Object[]{idReciboIndenizacao});
    }
    
    public List<DoctoServicoIndenizacao> findByIdDoctoServico(Long idDoctoServico) {
    	List<DoctoServicoIndenizacao> documentosIndenizados = getAdsmHibernateTemplate().find("from "+DoctoServicoIndenizacao.class.getName()+" dsi join fetch dsi.reciboIndenizacao where dsi.reciboIndenizacao.tpStatusIndenizacao != 'C' and dsi.doctoServico.id = ?", new Object[]{idDoctoServico});
    	return documentosIndenizados; 
    }

    public List<DoctoServicoIndenizacao> findByIdDoctoServicoTodosStatus(Long idDoctoServico) {
    	List<DoctoServicoIndenizacao> documentosIndenizados = getAdsmHibernateTemplate().find("from "+DoctoServicoIndenizacao.class.getName()+" dsi join fetch dsi.reciboIndenizacao where dsi.doctoServico.id = ?", new Object[]{idDoctoServico});
    	return documentosIndenizados; 
    }
    
    @SuppressWarnings("unchecked")
	public List<DoctoServicoIndenizacao> findByIdDoctoServicoParaLocalizacaoMercadoria(Long idDoctoServico, String statusDiferente) {
		StringBuilder query = new StringBuilder("from ");
		query.append(DoctoServicoIndenizacao.class.getName());
		query.append(" dsi join fetch dsi.reciboIndenizacao ri where dsi.doctoServico.id = ? ");

		Object[] param = null;
		if (statusDiferente != null) {
			query.append("and ri.tpStatusIndenizacao != ? ");
			param = new Object[] { idDoctoServico, statusDiferente };
		} else {
			param = new Object[] { idDoctoServico };
		}
		
		query.append("order by ri.dtEmissao desc, ri.idReciboIndenizacao desc");

		return  getAdsmHibernateTemplate().find(query.toString(), param);
	}

    public List<Map<String, Object>> findRelatorioIndenizacoesFranqueados(Map<String,Object> parameters){
		return getAdsmHibernateTemplate().findBySqlToMappedResult(RelatorioIndenizacoesFranqueadosQuery.getQuery(parameters), 
																	parameters, 
																	RelatorioIndenizacoesFranqueadosQuery.createConfigureSql());
	}


	public List findDoctoServicoIndenizacaoReciboIndenizacaoNaoCancelado(Long idDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("dsi");
		sql.addFrom(DoctoServicoIndenizacao.class.getName(), "dsi");
		sql.addFrom(ReciboIndenizacao.class.getName(), "ri");
		sql.addJoin("dsi.reciboIndenizacao", "ri");
		sql.addCriteria("dsi.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteria("ri.tpStatusIndenizacao", "!=", new DomainValue("C"));
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

}