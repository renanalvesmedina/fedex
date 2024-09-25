package com.mercurio.lms.seguros.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.util.LongUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class SinistroDoctoServicoDAO extends BaseCrudDao<SinistroDoctoServico, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return SinistroDoctoServico.class;
    }

    protected void initFindPaginatedLazyProperties(Map lazyFindPaginated) {
    	lazyFindPaginated.put("moeda", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.moeda", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.clienteByIdClienteRemetente", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.clienteByIdClienteRemetente.pessoa", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.clienteByIdClienteDestinatario", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.clienteByIdClienteDestinatario.pessoa", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.filialByIdFilialOrigem", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.filialByIdFilialDestino", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.filialByIdFilialDestino.pessoa", FetchMode.JOIN);
    	lazyFindPaginated.put("doctoServico.doctoServicoIndenizacoes", FetchMode.SELECT);
    	lazyFindPaginated.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao", FetchMode.SELECT);
    	lazyFindPaginated.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.filial", FetchMode.SELECT);
    	lazyFindPaginated.put("doctoServico.doctoServicoIndenizacoes.reciboIndenizacao.moeda", FetchMode.SELECT);
    }
    
    /**
     * FindPaginated da popup de Documentos de Servico 
     * da tela Manter Processos de Sinistro
     * @param idDoctoServico
     * @return
     */
    public List findPaginatedByIdDoctoServico(Long idDoctoServico) {
    	StringBuffer query = new StringBuffer()
    	.append(" select dsi ")
    	.append(" from "+DoctoServicoIndenizacao.class.getName()+" dsi")
    	.append("      join fetch dsi.reciboIndenizacao ri")
    	.append("      join fetch dsi.doctoServico ds")
    	.append(" where dsi.doctoServico.id = ?");
    	
    	Query q = getSessionFactory().getCurrentSession().createQuery(query.toString());
    	q.setParameter(0, idDoctoServico);
    	return q.list();
    }   
    
    /**
     * Verifica se o ProcessoSinistro em questão está contido em SinistroDoctoServico
     * @param idDoctoServico
     * @return true em caso afirmativo.
     */
    public boolean validateDoctoServicoInSinistroDoctoServico(Long idDoctoServico, Long idProcessoSinistro) {
    	Query q = getAdsmHibernateTemplate().getSessionFactory()
				.getCurrentSession().createQuery(
						"select count(*) from " + SinistroDoctoServico.class.getName()
								+ " sds where sds.doctoServico.id = ? "
								+ " and sds.processoSinistro.idProcessoSinistro = ?");
    	q.setParameter(0, idDoctoServico);
    	q.setParameter(1, idProcessoSinistro);
		return ((Long) q.uniqueResult()).longValue() > 0;
    }
    
    /**
     * GetRowCount da tela de 'emitir carta ocorrencia'.
     * 
     * @param idProcessoSinistro
     * @param findDefinition
     * @return
     */
    public Integer getRowCountCartaOcorrencia(List idsSinistroDoctoServico) {
    	return getRowCountCartaOcorrencia(null, null, null, null, null, null, null, false, false, idsSinistroDoctoServico);
    }
    
    /**
     * Busca o GetRowCount da grid de 'emitirCartaOcorrencia' e 'selecionarDocumentosCartaOcorrencia'.
     * 
     * @param idClienteDestinatario
     * @param idClienteRemetente
     * @param tpDocumentoServico
     * @param idDocumentoServico
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpPrejuizo
     * @param blNaoEnviados
     * @param semPrejuizo
     * @param idsSinistroDoctoServico
     * @return
     */
    public Integer getRowCountCartaOcorrencia(Long idClienteDestinatario, Long idClienteRemetente, String tpDocumentoServico, 
    		Long idDocumentoServico, Long idFilialOrigem ,Long idFilialDestino, String tpPrejuizo, boolean blNaoEnviados, 
    		boolean semPrejuizo, List idsSinistroDoctoServico) {
    	
    	SqlTemplate sql = new SqlTemplate();
		
    	sql.addProjection("count(*) as rowcount");
    	
    	sql.addFrom(SinistroDoctoServico.class.getName() + " as sinistroDoctoServico " +
				"left join sinistroDoctoServico.moeda as moeda " +
  				"left join sinistroDoctoServico.doctoServico as docto " +
				"left join docto.filialByIdFilialOrigem as filialByIdFilialOrigem " +
  				"left join docto.clienteByIdClienteDestinatario as clienteByIdClienteDestinatario " +
				"left join clienteByIdClienteDestinatario.pessoa as pessoaClienteDestinatario " +
				"left join docto.clienteByIdClienteRemetente as clienteByIdClienteRemetente " +
				"left join clienteByIdClienteRemetente.pessoa as pessoaClienteRemetente " +
				"left join docto.filialByIdFilialDestino as filialByIdFilialDestino " +
				"left join filialByIdFilialDestino.pessoa as pessoaFilialDestino");

		//Filtros...
    	sql.addCriteria("docto.nrDoctoServico", ">", LongUtils.ZERO);
    	if ((idsSinistroDoctoServico==null) || (idsSinistroDoctoServico.size()==0)) {
    	
    		if (idClienteDestinatario!=null) sql.addCriteria("clienteByIdClienteDestinatario.idCliente", "=", idClienteDestinatario);
			if (idClienteRemetente!=null) sql.addCriteria("clienteByIdClienteRemetente.idCliente", "=", idClienteRemetente);
			if (idFilialOrigem!=null) sql.addCriteria("filialByIdFilialOrigem.idFilial", "=", idFilialOrigem);
			if (idFilialDestino!=null) sql.addCriteria("filialByIdFilialDestino.idFilial", "=", idFilialDestino);
			if (tpDocumentoServico!=null) sql.addCriteria("docto.tpDocumentoServico", "=", tpDocumentoServico);
			if (idDocumentoServico!=null) sql.addCriteria("docto.idDoctoServico", "=", idDocumentoServico);
			
			if (tpPrejuizo!=null) {
	    		if (!tpPrejuizo.equals("")) {
	    			sql.addCriteria("sinistroDoctoServico.tpPrejuizo", "=", tpPrejuizo);
	    		} else if (semPrejuizo) {
	    			sql.addCriteria("sinistroDoctoServico.tpPrejuizo", "!=", "S");
	    		}
	    	}
				
			if (blNaoEnviados==true) {
				sql.addCustomCriteria("sinistroDoctoServico.dhEnvioEmailRetificacao.value IS NULL");    	
				sql.addCustomCriteria("sinistroDoctoServico.dhEnvioEmailOcorrencia.value IS NULL");    
	    	}
			
    	} else {
    		StringBuffer resultIds = new StringBuffer();
    		for (Iterator iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
				Long idSinistroDoctoServico = (Long) iter.next();
				resultIds.append(idSinistroDoctoServico.toString());
				if (iter.hasNext()) resultIds.append(", ");
			}    		
    		sql.addCustomCriteria("sinistroDoctoServico.idSinistroDoctoServico in (" + resultIds.toString()+ ")");
    	}
    	
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
    	
    	return result.intValue();
    }
    
    /**
     * FindPaginated da tela de 'emitir carta ocorrencia'.
     * 
     * @param idProcessoSinistro
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedCartaOcorrencia(List idsSinistroDoctoServico, FindDefinition findDefinition) {
    	return findPaginatedCartaOcorrencia(null, null, null, null, null, null, null, false, false, idsSinistroDoctoServico, findDefinition);
    }
    
    /**
     * Busca os dados da grid de 'emitirCartaOcorrencia' e 'selecionarDocumentosCartaOcorrencia'.
     * 
     * @param idProcessoSinistro
     * @param idManifesto
     * @param idFilial
     * @param idCliente
     * @param idDocumentoServico
     * @param tpDocumentoServico
     * @param idFilialOrigem
     * @param idFilialDestino
     * @param tpPrejuizo
     * @param blNaoEnviados
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedCartaOcorrencia(Long idClienteDestinatario, Long idClienteRemetente, String tpDocumentoServico, 
    		Long idDocumentoServico, Long idFilialOrigem ,Long idFilialDestino, String tpPrejuizo, boolean blNaoEnviados, 
    		boolean semPrejuizo, List idsSinistroDoctoServico,  FindDefinition findDefinition) {
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	StringBuffer projecao = new StringBuffer();
		projecao.append("new map(sinistroDoctoServico.idSinistroDoctoServico as idSinistroDoctoServico, ");
		projecao.append("filialByIdFilialOrigem.sgFilial as sgFilial, ");
		projecao.append("filialByIdFilialOrigem.idFilial as idFilialOrigem, ");
		projecao.append("docto.nrDoctoServico as nrDoctoServico, ");
		projecao.append("docto.tpDocumentoServico as tpDoctoServico, ");
		projecao.append("pessoaClienteDestinatario.idPessoa as idPessoaClienteDestinatario, ");
		projecao.append("pessoaClienteDestinatario.nmPessoa as nmPessoaClienteDestinatario, ");
		projecao.append("pessoaClienteRemetente.idPessoa as idPessoaClienteRemetente, ");
		projecao.append("pessoaClienteRemetente.nmPessoa as nmPessoaClienteRemetente, ");
		projecao.append("docto.qtVolumes as qtVolumes, ");
		projecao.append("docto.psReal as psReal, ");
		projecao.append("moeda.dsSimbolo as dsSimboloMoeda, ");
		projecao.append("docto.vlMercadoria as vlMercadoria, ");
		projecao.append("moeda.sgMoeda as sgMoeda, ");
		projecao.append("sinistroDoctoServico.vlPrejuizo as vlPrejuizo, ");
		projecao.append("sinistroDoctoServico.dhGeracaoCartaRetificacao as dhGeracaoCartaRetificacao, ");
		projecao.append("sinistroDoctoServico.dhEnvioEmailRetificacao as dhEnvioEmailRetificacao, ");
		projecao.append("sinistroDoctoServico.dhGeracaoCartaOcorrencia as dhGeracaoCartaOcorrencia, ");
		projecao.append("sinistroDoctoServico.dhEnvioEmailOcorrencia as dhEnvioEmailOcorrencia, ");
		projecao.append("sinistroDoctoServico.dhGeracaoFilialRim as dhGeracaoFilialRim, ");
		projecao.append("sinistroDoctoServico.dhEnvioEmailFilialRim as dhEnvioEmailFilialRim, ");
		projecao.append("filialByIdFilialDestino.idFilial as idFilialDestino, ");
		projecao.append("filialByIdFilialDestino.sgFilial as sgFilialDestino, ");
		projecao.append("pessoaFilialDestino.nmPessoa as nmPessoaFilialDestino) ");
		
		sql.addProjection(projecao.toString());
		
		sql.addFrom(SinistroDoctoServico.class.getName() + " as sinistroDoctoServico " +
				"left join sinistroDoctoServico.moeda as moeda " +
  				"left join sinistroDoctoServico.doctoServico as docto " +
				"left join docto.filialByIdFilialOrigem as filialByIdFilialOrigem " +
  				"left join docto.clienteByIdClienteDestinatario as clienteByIdClienteDestinatario " +
				"left join clienteByIdClienteDestinatario.pessoa as pessoaClienteDestinatario " +
				"left join docto.clienteByIdClienteRemetente as clienteByIdClienteRemetente " +
				"left join clienteByIdClienteRemetente.pessoa as pessoaClienteRemetente " +
				"left join docto.filialByIdFilialDestino as filialByIdFilialDestino " +
				"left join filialByIdFilialDestino.pessoa as pessoaFilialDestino");

		//Filtros...
		sql.addCriteria("docto.nrDoctoServico", ">", LongUtils.ZERO);
    	if ((idsSinistroDoctoServico==null) || (idsSinistroDoctoServico.size()==0)) {
    	
    		if (idClienteDestinatario!=null) sql.addCriteria("clienteByIdClienteDestinatario.idCliente", "=", idClienteDestinatario);
			if (idClienteRemetente!=null) sql.addCriteria("clienteByIdClienteRemetente.idCliente", "=", idClienteRemetente);
			if (idFilialOrigem!=null) sql.addCriteria("filialByIdFilialOrigem.idFilial", "=", idFilialOrigem);
			if (idFilialDestino!=null) sql.addCriteria("filialByIdFilialDestino.idFilial", "=", idFilialDestino);
			if (tpDocumentoServico!=null) sql.addCriteria("docto.tpDocumentoServico", "=", tpDocumentoServico);
			if (idDocumentoServico!=null) sql.addCriteria("docto.idDoctoServico", "=", idDocumentoServico);
			
			if (tpPrejuizo!=null) {
	    		if (!tpPrejuizo.equals("")) {
	    			sql.addCriteria("sinistroDoctoServico.tpPrejuizo", "=", tpPrejuizo);
	    		} else if (semPrejuizo) {
	    			sql.addCriteria("sinistroDoctoServico.tpPrejuizo", "!=", "S");
	    		}
	    	}
			
			if (blNaoEnviados==true) {
				sql.addCustomCriteria("sinistroDoctoServico.dhEnvioEmailRetificacao.value IS NULL");    	
				sql.addCustomCriteria("sinistroDoctoServico.dhEnvioEmailOcorrencia.value IS NULL");    
	    	}
			
			sql.addOrderBy("docto.tpDocumentoServico");
	    	sql.addOrderBy("filialByIdFilialOrigem.sgFilial");
	    	sql.addOrderBy("docto.nrDoctoServico");
			
    	} else {
    		StringBuffer resultIds = new StringBuffer();
    		for (Iterator iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
				Long idSinistroDoctoServico = (Long) iter.next();
				resultIds.append(idSinistroDoctoServico.toString());
				if (iter.hasNext()) resultIds.append(", ");
			}    		
    		sql.addCustomCriteria("sinistroDoctoServico.idSinistroDoctoServico in (" + resultIds.toString()+ ")");
    		
    		sql.addOrderBy("pessoaClienteRemetente.nmPessoa");
        	sql.addOrderBy("pessoaClienteDestinatario.nmPessoa");
    	}
		
        return getAdsmHibernateTemplate().findPaginated( sql.getSql(),findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
    }
    
    /**
     * LMS-6144
     * Busca os dados da grid de 'emitirCartaOcorrenciaEnviarEmail'.
     * 
     * @param idsSinistroDoctoServico
     * @param findDefinition
     * @return
     */
    public List findEmailCartaOcorrencia(List idsSinistroDoctoServico, String destinatarioCarta) {
    	List result = null;
    	
    	if ("D".equals(destinatarioCarta)) {
    		result = findEmailCartaOcorrenciaDestinatario(idsSinistroDoctoServico);
    	}
    	
    	if ("O".equals(destinatarioCarta)) {
    		result = findEmailCartaOcorrenciaFilialOrigem(idsSinistroDoctoServico);
    	}

    	if ("S".equals(destinatarioCarta)) {
    		result = findEmailCartaOcorrenciaFilialDestino(idsSinistroDoctoServico);
    	}
    	
    	if ("R".equals(destinatarioCarta)) {
    		result = findEmailCartaOcorrenciaRemetente(idsSinistroDoctoServico);
    	}
    	
    	return result;
    }    
    
    private List findEmailCartaOcorrenciaDestinatario(List idsSinistroDoctoServico) {
    	String ids = "";
		for(Iterator<Long> iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			ids += id.toString();
			if (iter.hasNext()) ids += ", ";
		}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
						
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT pessoa.ID_PESSOA AS idPessoa, ");
		sql.append("pessoa.NM_PESSOA AS nmPessoa, ");
		sql.append("pessoa.DS_EMAIL AS dsEmail ");
		
		sql.append("FROM SINISTRO_DOCTO_SERVICO sinistroDoctoServico, DOCTO_SERVICO doctoServico, PESSOA pessoa, CLIENTE cliente");
		
		sql.append(" WHERE sinistrodoctoservico.ID_DOCTO_SERVICO = doctoServico.ID_DOCTO_SERVICO ");
		sql.append(" AND sinistrodoctoservico.ID_SINISTRO_DOCTO_SERVICO IN ("+ids+") ");
		sql.append(" AND doctoServico.ID_CLIENTE_DESTINATARIO = cliente.ID_CLIENTE ");
		sql.append(" AND cliente.ID_CLIENTE = pessoa.ID_PESSOA ");

		sql.append("ORDER BY nmPessoa ASC");
		
		ConfigureSqlQuery configureSqlQuery = null;
		
		configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idPessoa", Hibernate.LONG);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
				sqlQuery.addScalar("dsEmail", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		return list.isEmpty() ? null : list;
		
    }
    
    private List findEmailCartaOcorrenciaFilialOrigem(List idsSinistroDoctoServico) {
    	String ids = "";
		for(Iterator<Long> iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			ids += id.toString();
			if (iter.hasNext()) ids += ", ";
		}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
						
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT pessoa.ID_PESSOA AS idPessoa, ");
		sql.append("pessoa.NM_FANTASIA AS nmPessoa, ");
		sql.append("NVL(contato.DS_EMAIL, pessoa.DS_EMAIL) AS dsEmail ");
		
		sql.append("FROM SINISTRO_DOCTO_SERVICO sinistroDoctoServico, DOCTO_SERVICO doctoServico, PESSOA pessoa, CONTATO contato");
		
		sql.append(" WHERE sinistrodoctoservico.ID_DOCTO_SERVICO = doctoServico.ID_DOCTO_SERVICO ");
		sql.append(" AND sinistrodoctoservico.ID_SINISTRO_DOCTO_SERVICO IN ("+ids+") ");
		sql.append(" AND doctoServico.ID_FILIAL_ORIGEM = pessoa.ID_PESSOA ");
		sql.append(" AND pessoa.ID_PESSOA = contato.ID_PESSOA(+) ");
		sql.append(" AND contato.TP_CONTATO(+) = 'SE' ");
		
		sql.append("ORDER BY nmPessoa ASC");
		
		ConfigureSqlQuery configureSqlQuery = null;
		
		configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idPessoa", Hibernate.LONG);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
				sqlQuery.addScalar("dsEmail", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		return list.isEmpty() ? null : list;
			
	}

    private List findEmailCartaOcorrenciaRemetente(List idsSinistroDoctoServico) {
    	String ids = "";
		for(Iterator<Long> iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			ids += id.toString();
			if (iter.hasNext()) {
				ids += ", ";
			}
		}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
						
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT pessoa.ID_PESSOA AS idPessoa, ");
		sql.append("pessoa.NM_PESSOA AS nmPessoa, ");
		sql.append("pessoa.DS_EMAIL AS dsEmail ");
		
		sql.append("FROM SINISTRO_DOCTO_SERVICO sinistroDoctoServico, DOCTO_SERVICO doctoServico, PESSOA pessoa, CLIENTE cliente");
		
		sql.append(" WHERE sinistrodoctoservico.ID_DOCTO_SERVICO = doctoServico.ID_DOCTO_SERVICO ");
		sql.append(" AND sinistrodoctoservico.ID_SINISTRO_DOCTO_SERVICO IN ("+ids+") ");
		sql.append(" AND doctoServico.ID_CLIENTE_REMETENTE = cliente.ID_CLIENTE ");
		sql.append(" AND cliente.ID_CLIENTE = pessoa.ID_PESSOA ");
		
		sql.append("ORDER BY nmPessoa ASC");
		
		ConfigureSqlQuery configureSqlQuery = null;
		
		configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idPessoa", Hibernate.LONG);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
				sqlQuery.addScalar("dsEmail", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		return list.isEmpty() ? null : list;

	}

	private List findEmailCartaOcorrenciaFilialDestino(List idsSinistroDoctoServico) {
		String ids = "";
		for(Iterator<Long> iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			ids += id.toString();
			if (iter.hasNext()) {
				ids += ", ";
			}
		}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
						
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT pessoa.ID_PESSOA AS idPessoa, ");
		sql.append("pessoa.NM_FANTASIA AS nmPessoa, ");
		sql.append("NVL(contato.DS_EMAIL, pessoa.DS_EMAIL) AS dsEmail ");
		
		sql.append("FROM SINISTRO_DOCTO_SERVICO sinistroDoctoServico, DOCTO_SERVICO doctoServico, PESSOA pessoa, CONTATO contato");
		
		sql.append(" WHERE sinistrodoctoservico.ID_DOCTO_SERVICO = doctoServico.ID_DOCTO_SERVICO ");
		sql.append(" AND sinistrodoctoservico.ID_SINISTRO_DOCTO_SERVICO IN ("+ids+") ");
		sql.append(" AND doctoServico.ID_FILIAL_DESTINO = pessoa.ID_PESSOA ");
		sql.append(" AND pessoa.ID_PESSOA = contato.ID_PESSOA(+) ");
		sql.append(" AND contato.TP_CONTATO(+) = 'SE' ");
		
		sql.append("ORDER BY nmPessoa ASC");
		
		ConfigureSqlQuery configureSqlQuery = null;
		
		configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idPessoa", Hibernate.LONG);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
				sqlQuery.addScalar("dsEmail", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		return list.isEmpty() ? null : list;
			
    }    
    
    /**
     * LMS-6144
     * Busca os dados da grid de 'emitirCartaOcorrenciaEnviarEmail'.
     * 
     * @param idsSinistroDoctoServico
     * @param findDefinition
     * @return
     */
	public List findEmailRim(List idsSinistroDoctoServico, String filial, boolean groupBy) {
    	
    	String ids = "";
		for(Iterator<Long> iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
			Long id = (Long) iter.next();
			ids += id.toString();
			if (iter.hasNext()) {
				ids += ", ";
			}
		}
    	
    	Map<String, Object> params = new HashMap<String, Object>();
		params.put("ids", ids);
				
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT DISTINCT pessoa.ID_PESSOA AS idPessoa,");
		sql.append(" pessoa.NM_FANTASIA AS nmPessoa,");
		sql.append(" nvl(contato.ds_email, pessoa.ds_email) AS dsEmailContato ");
		
		sql.append("FROM SINISTRO_DOCTO_SERVICO sinistroDoctoServico, DOCTO_SERVICO doctoServico, PESSOA pessoa, CONTATO contato ");
		
		sql.append("WHERE sinistroDoctoServico.ID_DOCTO_SERVICO = doctoServico.ID_DOCTO_SERVICO ");
		
		if(("origem").equals(filial)) {
			sql.append("AND doctoServico.ID_FILIAL_ORIGEM = pessoa.ID_PESSOA ");
		} else {
			sql.append("AND doctoServico.ID_FILIAL_DESTINO = pessoa.ID_PESSOA ");
		}
		
		sql.append("AND pessoa.ID_PESSOA = contato.ID_PESSOA(+) ");
		sql.append("AND contato.TP_CONTATO(+) = 'SE' ");
		sql.append("AND sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN ("+ids+") ");
		sql.append("ORDER BY nmPessoa ASC");
		
		ConfigureSqlQuery configureSqlQuery = new ConfigureSqlQuery() {
			public void configQuery(SQLQuery sqlQuery) {				
				sqlQuery.addScalar("idPessoa", Hibernate.LONG);
				sqlQuery.addScalar("nmPessoa", Hibernate.STRING);
				sqlQuery.addScalar("dsEmailContato", Hibernate.STRING);
			}
		};
		
		List<Object[]> list = getAdsmHibernateTemplate().findBySql(sql.toString(), params, configureSqlQuery);
    	
		return list.isEmpty() ? null : list;
    }
    
    public List findSinistroDoctoServicoByIdProcessoSinistro(Long idProcessoSinistro) {
    	
    	StringBuffer s = new StringBuffer()
    	.append("select sds from "+SinistroDoctoServico.class.getName()+" sds ")
    	.append("inner join fetch sds.doctoServico ds ")
    	.append("left  join fetch ds.doctoServicoIndenizacoes dsi ")
    	.append("left  join fetch dsi.reciboIndenizacao rim1 ")
    	.append("join sds.processoSinistro ps ")
    	.append("where ps.id = ? ")
    	.append("and not exists (from "+ReciboIndenizacao.class.getName()+" rim2 where rim2.id = rim1.id and rim2.tpStatusIndenizacao = ?)");
    	
    	return (List)getAdsmHibernateTemplate().getSessionFactory()
		    	.getCurrentSession().createQuery(s.toString())
		    	.setParameter(0, idProcessoSinistro)
		    	.setParameter(1, "C").list();
    }
    
    private String getFindPaginatedByIdProcessoSinistroQuery(boolean notInDoctos, boolean withProjection, String tpBeneficiarioIndenizacao, Boolean isFilialMatriz) {
    	
    	StringBuffer s = new StringBuffer();
    	
    	if (withProjection) {
	    	s.append("select new map(")
			.append("ds.idDoctoServico as idDoctoServico, ")
	    	.append("ds.tpDocumentoServico as tpDocumentoServico, ")
	    	.append("ds.nrDoctoServico as nrDoctoServico, ")
	    	.append("origem.sgFilial as sgFilialOrigem, ")
	    	.append("destino.sgFilial as sgFilialDestino, ")
	    	.append("rem.nmPessoa as nmRemetente, ")
	    	.append("dest.nmPessoa as nmDestinatario) ");
    	}
    	
    	s.append("from "+SinistroDoctoServico.class.getName()+" sds ")
    	.append("inner join sds.doctoServico ds ")
    	.append("inner join ds.clienteByIdClienteRemetente as remCliente ")
    	.append("inner join remCliente.pessoa as rem ")
    	.append(" left join ds.clienteByIdClienteDestinatario as destCliente ")
    	.append(" left join destCliente.pessoa as dest ")
    	.append("inner join ds.filialByIdFilialOrigem origem ")
    	.append(" left join ds.filialByIdFilialDestino destino ")
    	.append("inner join sds.processoSinistro ps ");  
    	

    	
    	s.append("where ps.idProcessoSinistro = :idProcessoSinistro" + (notInDoctos ? " and ds.id not in (:excetoDoctos)" : ""));
    	
    	if(!isFilialMatriz){
    		// LMS-6154 Se usuario estiver logado na filial MTZ não deverá validar essa regra
    		s.append("  and ds.id not in (select dsi.doctoServico.id from "+DoctoServicoIndenizacao.class.getName()+
   				 " dsi where dsi.doctoServico.id = ds.id and dsi.reciboIndenizacao.tpStatusIndenizacao != 'C' )");
	       	// LMS-6611 - Se usuário não estiver logado na filial MTZ, incluir validação para NÃO exibir registros de 
	       	//	SINISTRO_DOCTO_SERVICO.BL_PREJUIZO_PROPRIO = "S"
	   		s.append("  and sds.blPrejuizoProprio = 'N'");
    	}
    			
    	s.append("  and sds.tpPrejuizo != :tpPrejuizo ")
   		.append(" and(ds.clienteByIdClienteRemetente.id = :idCliente ")
    		.append(" or ds.clienteByIdClienteDestinatario.id = :idCliente ")
    		.append(" or ds.clienteByIdClienteConsignatario.id = :idCliente ")
    		.append(" or exists (from "+DevedorDocServ.class.getName()+" dds where dds.cliente.id = :idCliente and dds.doctoServico = ds))");
    	
    	return s.toString();
    }
    
    public ResultSetPage findPaginatedByIdProcessoSinistro(Long idProcessoSinistro, FindDefinition fd, List exceptDoctosServico, String tpBeneficiarioIndenizacao, Long idCliente, Boolean isFilialMatriz) {
    	String query  = getFindPaginatedByIdProcessoSinistroQuery(exceptDoctosServico.size() > 0, true, tpBeneficiarioIndenizacao, isFilialMatriz);
    	Map map = new HashMap();
    	map.put("idProcessoSinistro", idProcessoSinistro);
    	map.put("excetoDoctos", exceptDoctosServico);
    	map.put("tpPrejuizo", "S");
    	if (idCliente!=null)
    		map.put("idCliente", idCliente);
    	return getAdsmHibernateTemplate().findPaginated(query, fd.getCurrentPage(), fd.getPageSize(), map);
    }
    
    public Integer getRowCountByIdProcessoSinistro(Long idProcessoSinistro, List exceptDoctosServico, String tpBeneficiarioIndenizacao, Long idCliente, Boolean isFilialMatriz ) {
    	String query = getFindPaginatedByIdProcessoSinistroQuery(exceptDoctosServico.size() > 0, false, tpBeneficiarioIndenizacao, isFilialMatriz);
    	Map map = new HashMap();
    	map.put("idProcessoSinistro", idProcessoSinistro);
    	map.put("excetoDoctos", exceptDoctosServico);
    	map.put("tpPrejuizo", "S");    	
    	if (idCliente!=null)
    		map.put("idCliente", idCliente);
    	return getAdsmHibernateTemplate().getRowCountForQuery(query, map);
    }
    
    private String getFindPaginatedByIdProcessoSinistroQuery(boolean withProjection) {
    	
    	StringBuilder sql = new StringBuilder();
    	
    	if(withProjection) {
    		sql.append("SELECT sds.id_sinistro_docto_servico, ")
	    		.append("  ds.id_docto_servico, ")
	    		.append("  ds.tp_documento_servico, ")
	    		.append("  fo.sg_filial as sg_filial_origem, ")
	    		.append("  ds.nr_docto_servico, ")
	    		.append("  fd.sg_filial as sg_filial_destino, ")
	    		.append("  ds.dh_emissao, ")
		    	.append("  sds.tp_prejuizo, ")
		    	.append("  sds.vl_prejuizo, ")
		    	.append("  ds.vl_mercadoria, ")
		    	.append("  ds.qt_volumes, ")
	    		.append("  ds.ps_aferido, ")
	    		.append("  ds.ps_real, ")
		    	.append("  rem.nm_pessoa AS remetente, ")
		    	.append("  dest.nm_pessoa AS destinatario, ")
		    	.append("  dev.nm_pessoa AS devedor, ")
	    		.append("  (SELECT ri.dt_emissao ")
		    	.append("  FROM DOCTO_SERVICO_INDENIZACAO dsi, ")
		    	.append("    recibo_indenizacao ri ")
		    	.append("  WHERE dsi.id_docto_servico    = ds.id_docto_servico ")
		    	.append("  AND dsi.id_recibo_indenizacao = ri.id_recibo_indenizacao ")
		    	.append("  AND ri.tp_status_indenizacao <> 'C' ")
		    	.append("  AND rownum                   <= 1 ")
		    	.append("  ) AS dt_emissao_rim, ")
		    	.append("  (SELECT f.sg_filial ")
		    	.append("  FROM DOCTO_SERVICO_INDENIZACAO dsi, ")
		    	.append("    recibo_indenizacao ri, ")
		    	.append("    filial f ")
		    	.append("  WHERE dsi.id_docto_servico    = ds.id_docto_servico ")
		    	.append("  AND dsi.id_recibo_indenizacao = ri.id_recibo_indenizacao ")
		    	.append("  AND ri.id_filial              = f.id_filial ")
		    	.append("  AND ri.tp_status_indenizacao <> 'C' ")
		    	.append("  AND rownum                   <= 1 ")
	    		.append("  ) AS filial_rim, ")
	    		.append("  (SELECT ri.nr_recibo_indenizacao ")
	    		.append("  FROM DOCTO_SERVICO_INDENIZACAO dsi, ")
	    		.append("    recibo_indenizacao ri, ")
	    		.append("    filial f ")
	    		.append("  WHERE dsi.id_docto_servico    = ds.id_docto_servico ")
	    		.append("  AND dsi.id_recibo_indenizacao = ri.id_recibo_indenizacao ")
	    		.append("  AND ri.id_filial              = f.id_filial ")
	    		.append("  AND ri.tp_status_indenizacao <> 'C' ")
	    		.append("  AND rownum                   <= 1 ")
	    		.append("  ) AS nr_recibo_indenizacao, ")
	    		.append("  (SELECT tp_status_indenizacao ")
		    	.append("  FROM DOCTO_SERVICO_INDENIZACAO dsi, ")
		    	.append("    recibo_indenizacao ri, ")
	    		.append("    filial f ")
		    	.append("  WHERE dsi.id_docto_servico        = ds.id_docto_servico ")
		    	.append("  AND dsi.id_recibo_indenizacao     = ri.id_recibo_indenizacao ")
		    	.append("  AND ri.id_filial                  = f.id_filial ")
		    	.append("  AND ri.tp_status_indenizacao     <> 'C' ")
		    	.append("  AND rownum                       <= 1 ")
		    	.append("  ) AS situacao_rim, ")
		    	.append("  (SELECT ri.vl_indenizacao ")
		    	.append("  FROM DOCTO_SERVICO_INDENIZACAO dsi, ")
		    	.append("    recibo_indenizacao ri ")
		    	.append("  WHERE dsi.id_docto_servico    = ds.id_docto_servico ")
		    	.append("  AND dsi.id_recibo_indenizacao = ri.id_recibo_indenizacao ")
		    	.append("  AND ri.tp_status_indenizacao <> 'C' ")
		    	.append("  AND rownum                   <= 1 ")
		    	.append("  ) AS valor_rim, ")
	    		.append("  (SELECT ri.dt_pagamento_efetuado ")
		    	.append("  FROM DOCTO_SERVICO_INDENIZACAO dsi, ")
		    	.append("    recibo_indenizacao ri ")
		    	.append("  WHERE dsi.id_docto_servico    = ds.id_docto_servico ")
		    	.append("  AND dsi.id_recibo_indenizacao = ri.id_recibo_indenizacao ")
		    	.append("  AND ri.tp_status_indenizacao <> 'C' ")
		    	.append("  AND rownum                   <= 1 ")
		    	.append("  ) AS dt_pagamento_rim, ")
	    		.append("  sds.dh_geracao_carta_ocorrencia, ")
	    		.append("  sds.dh_envio_email_ocorrencia, ")
	    		.append("  sds.dh_geracao_carta_retificacao, ")
	    		.append("  sds.dh_envio_email_retificacao, ")
	    		.append("  sds.dh_geracao_filial_rim, ")
	    		.append("  sds.dh_envio_email_filial_rim, ")
	    		// LMS-6611
    			.append("  sds.bl_prejuizo_proprio ");
    	}
    	
    	sql.append("FROM SINISTRO_DOCTO_SERVICO sds, ")
	    	.append("  docto_servico ds, ")
	    	.append("  filial fo, ")
	    	.append("  filial fd, ")
	    	.append("  pessoa rem, ")
	    	.append("  pessoa dest, ")
	    	.append("  devedor_doc_serv dds, ")
	    	.append("  pessoa dev ")
	    	.append("WHERE sds.id_docto_servico         = ds.id_docto_servico ")
	    	.append("AND ds.id_filial_origem            = fo.id_filial ")
	    	.append("AND ds.id_filial_destino           = fd.id_filial(+) ")
	    	.append("AND ds.id_cliente_remetente        = rem.id_pessoa(+) ")
	    	.append("AND ds.id_cliente_destinatario     = dest.id_pessoa(+) ")
	    	.append("AND ds.id_docto_servico            = dds.id_docto_servico(+) ")
	    	.append("AND dds.id_cliente                 = dev.id_pessoa(+) ")
	    	.append("AND sds.id_processo_sinistro   = :idProcessoSinistro ");
    	
    	return sql.toString();
    }
    
    public List<Object[]> findByIdProcessoSinistro(Long idProcessoSinistro) {
    	String query  = getFindPaginatedByIdProcessoSinistroQuery(true);
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("idProcessoSinistro", idProcessoSinistro);
    	
    	return getAdsmHibernateTemplate().findBySql(query, params, new ConfigureSqlQuery() {
			
			@Override
			public void configQuery(SQLQuery sqlQuery) {
				
				sqlQuery.addScalar("id_sinistro_docto_servico",Hibernate.LONG);
				sqlQuery.addScalar("id_docto_servico",Hibernate.LONG);
				sqlQuery.addScalar("tp_documento_servico",Hibernate.STRING);
    			sqlQuery.addScalar("sg_filial_origem",Hibernate.STRING);
    			sqlQuery.addScalar("nr_docto_servico",Hibernate.LONG);
    			sqlQuery.addScalar("sg_filial_destino",Hibernate.STRING);
    			sqlQuery.addScalar("dh_emissao",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("tp_prejuizo",Hibernate.STRING);
    			sqlQuery.addScalar("vl_prejuizo",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("vl_mercadoria",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("qt_volumes",Hibernate.INTEGER);
    			sqlQuery.addScalar("ps_aferido",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("ps_real",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("remetente",Hibernate.STRING);
    			sqlQuery.addScalar("destinatario",Hibernate.STRING);
    			sqlQuery.addScalar("devedor",Hibernate.STRING);
    			sqlQuery.addScalar("dt_emissao_rim",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("filial_rim",Hibernate.STRING);
    			sqlQuery.addScalar("nr_recibo_indenizacao",Hibernate.INTEGER);
    			sqlQuery.addScalar("situacao_rim",Hibernate.STRING);
    			sqlQuery.addScalar("valor_rim",Hibernate.BIG_DECIMAL);
    			sqlQuery.addScalar("dt_pagamento_rim",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("dh_geracao_carta_ocorrencia",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("dh_envio_email_ocorrencia",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("dh_geracao_carta_retificacao",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("dh_envio_email_retificacao",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("dh_geracao_filial_rim",Hibernate.TIMESTAMP);
    			sqlQuery.addScalar("dh_envio_email_filial_rim",Hibernate.TIMESTAMP);
    			// LMS-6611
    			sqlQuery.addScalar("bl_prejuizo_proprio", Hibernate.STRING);
			}
		});
    }
    
    public Integer getRowCountByIdProcessoSinistro(Long idProcessoSinistro) {
    	String query  = getFindPaginatedByIdProcessoSinistroQuery(false);
    	
    	Map params = new HashMap();
    	params.put("idProcessoSinistro", idProcessoSinistro);
    	
    	return getAdsmHibernateTemplate().getRowCountBySql(query, params);
    }
    
    public void removeSinistroDoctoServicosByIdProcessoSinistro(Long idProcessoSinistro) {
    	String s = "delete "+SinistroDoctoServico.class.getName()+" where id in (select sds.id from SinistroDoctoServico sds where sds.processoSinistro.id = ?)";
    	
    	getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(s)
    	.setParameter(0, idProcessoSinistro).executeUpdate();
    }
    
    public void removeByIdProcessoSinistro(Long idProcessoSinistro){
    	List parameters = new ArrayList();
    	String sql = "delete "+ SinistroDoctoServico.class.getName() + " as sd " +
    			" where sd.processoSinistro.idProcessoSinistro = ? " +
    			" and sd.tpPrejuizo = 'S'";
    	
		parameters.add(idProcessoSinistro);
		
		executeHql(sql.toString(), parameters);
    }

    //LMS-6178
	public List findSomaValoresPrejuizo(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SUM(SDS.vlPrejuizo)");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
	
	//LMS-6611
	public List findSomaValoresPrejuizoProprio(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SUM(SDS.vlPrejuizo)");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		sql.addCriteria("SDS.blPrejuizoProprio", "=", "S");
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
    
	/**
	 * LMS-6140 - Método responsável por buscar todos os sinistros docto servico relacionados com
	 *  o id do processo de sinistro passado por parâmetro
	 * 
	 * @param idProcessoSinistro
	 * @return
	 */
	public List findSinistrosDoctoServicoByIdProcessoSinistro(Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SDS");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
	
	/**
	 * LMS-6155 - Método responsável por buscar os SinistroDoctoServico relacionados com o id do processo de sinistro
	 * e onde os idsDoctoServico não estejam na lista de ids passada por parâmetro
	 * 
	 * @param idProcessoSinistro
	 * @param idsSinistroDoctoServico
	 * @return
	 */
	public List findSinistroDoctoServicoByIdProcessoSinistroAndListIdsDoctoServico(Long idProcessoSinistro, List idsDoctoServico) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SDS");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.processoSinistro.idProcessoSinistro", "=", idProcessoSinistro);
		sql.addCriteria("SDS.tpPrejuizo", "<>", "S");
		sql.addCriteriaNotIn("SDS.doctoServico.idDoctoServico", idsDoctoServico);
		  
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());		
	}
    
	public List findDoctoServicoByIdSinistroDoctoServico(Long idSinistroDoctoServico){
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("SDS.doctoServico");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.idSinistroDoctoServico", "=", idSinistroDoctoServico);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

	public TypedFlatMap findClienteByIdsSinistroDoctoServico(TypedFlatMap criteria) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("new map(SDS.idSinistroDoctoServico", "idSinistroDoctoServico");
		sql.addProjection("SDS.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa", "nmPessoaClienteDestinatario");
		sql.addProjection("SDS.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa", "nmPessoaClienteRemetente)");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteriaIn("SDS.idSinistroDoctoServico", criteria.getList("idsSinistroDoctoServico"));
		
		List resultList = new ArrayList();
		
		resultList.addAll(getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria()));
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("sinistros", resultList);
		return result;
	}
	
	/**
	 * LMS-6154 - Retorna lista com os registros em SINISTRO_DOCTO_SERVICO que tenham prezuizo
	 * com base no id_docto_servico informado
	 */
	public List findIdsSinistroDoctoServicoByIdDoctoServicoComPrejuizo(long idDoctoServico){
		SqlTemplate sql = new SqlTemplate();
		  
		sql.addProjection("SDS.idSinistroDoctoServico");
		sql.addFrom(SinistroDoctoServico.class.getName(), "SDS");
		sql.addCriteria("SDS.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteria("SDS.tpPrejuizo", "<>", "S");
		  
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
	
	public List findDoctoServicoOutroProcessoSinistro(Long idDoctoServico, Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("sds");
		sql.addFrom(SinistroDoctoServico.class.getName(), "sds");
		sql.addCriteria("sds.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteria("sds.processoSinistro.idProcessoSinistro", "!=", idProcessoSinistro);
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}

	public List findDoctoServicoOutroProcessoSinistroComPrejuizo(Long idDoctoServico, Long idProcessoSinistro) {
		SqlTemplate sql = new SqlTemplate();
		
		sql.addProjection("sds");
		sql.addFrom(SinistroDoctoServico.class.getName(), "sds");
		sql.addCriteria("sds.doctoServico.idDoctoServico", "=", idDoctoServico);
		sql.addCriteria("sds.tpPrejuizo", "!=", new DomainValue("S"));
		
		if(idProcessoSinistro != null) {
			sql.addCriteria("sds.processoSinistro.idProcessoSinistro", "!=", idProcessoSinistro);
		}
		
		return getAdsmHibernateTemplate().find(sql.toString(), sql.getCriteria());
	}
	
} 