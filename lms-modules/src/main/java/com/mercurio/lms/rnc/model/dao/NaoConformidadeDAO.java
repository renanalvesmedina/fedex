package com.mercurio.lms.rnc.model.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.NotaOcorrenciaNc;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NaoConformidadeDAO extends BaseCrudDao<NaoConformidade, Long>
{
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return NaoConformidade.class;
    }

    protected void initFindListLazyProperties(Map map) {
    	map.put("filial",FetchMode.JOIN);
		map.put("doctoServico",FetchMode.JOIN);
		map.put("doctoServico.conhecimento",FetchMode.JOIN);
		map.put("doctoServico.ctoInternacional",FetchMode.JOIN);
		map.put("doctoServico.mda",FetchMode.JOIN);
		map.put("doctoServico.moeda",FetchMode.JOIN);
    }
    
	protected void initFindPaginatedLazyProperties(Map map) {
		map.put("filial",FetchMode.JOIN);
		map.put("doctoServico",FetchMode.JOIN);
		map.put("doctoServico.filialByIdFilialOrigem",FetchMode.JOIN);
		map.put("doctoServico.conhecimento",FetchMode.JOIN);
		map.put("doctoServico.ctoInternacional",FetchMode.JOIN);
		map.put("doctoServico.mda",FetchMode.JOIN);
	}
    
	protected void initFindLookupLazyProperties(Map map) {
		map.put("filial",FetchMode.JOIN);
        // Adicionado para carregar a lookup de Nao Conformidade na tela de emitirRNC
        map.put("doctoServico",FetchMode.JOIN);
        map.put("doctoServico.filialByIdFilialOrigem",FetchMode.JOIN);
        map.put("doctoServico.moeda",FetchMode.JOIN);
        map.put("doctoServico.conhecimento",FetchMode.JOIN);
        map.put("doctoServico.ctoInternacional",FetchMode.JOIN);
        map.put("doctoServico.mda",FetchMode.JOIN);        
	}
	
	protected void initFindByIdLazyProperties(Map map) {
		map.put("clienteByIdClienteDestinatario",FetchMode.JOIN);
		map.put("clienteByIdClienteDestinatario.pessoa",FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente",FetchMode.JOIN);
		map.put("clienteByIdClienteRemetente.pessoa",FetchMode.JOIN);
		map.put("doctoServico",FetchMode.JOIN);
		map.put("doctoServico.moeda",FetchMode.JOIN);
		map.put("doctoServico.clienteByIdClienteDestinatario.pessoa",FetchMode.JOIN);
		map.put("doctoServico.clienteByIdClienteRemetente.pessoa",FetchMode.JOIN);
		map.put("doctoServico.filialByIdFilialOrigem",FetchMode.JOIN);
		map.put("filial",FetchMode.JOIN);
	}
	
    /**
     * Retorna um DetachedCriteria para a pesquisa "NaoConformidade".
     * 
     * @param map
     * @return
     */
    private StringBuffer getDetachedCriteriaByNaoConformidade(TypedFlatMap map, List param, boolean isRowCount) {
		StringBuffer sql = new StringBuffer();
		if (!isRowCount) {
			sql.append("select new map(nc.idNaoConformidade as idNaoConformidade, ")
			.append("filial.sgFilial as filial_sgFilial, ")
			.append("filial.idFilial as filial_idFilial, ")
			.append("nc.nrNaoConformidade as nrNaoConformidade, ")
			.append("doctoServico.idDoctoServico as doctoServico_idDoctoServico, ")
			.append("doctoServico.tpDocumentoServico as doctoServico_tpDocumentoServico, ")
			.append("doctoServico.filialByIdFilialOrigem.sgFilial as doctoServico_filialByIdFilialOrigem_sgFilial,")
			.append("doctoServico.nrDoctoServico as doctoServico_nrDoctoServico, ")			
			.append("doctoServico.qtVolumes as doctoServico_qtVolumes, ")
			.append("doctoServico.psReal as doctoServico_psReal, ")
			.append("doctoServico.moeda.idMoeda as doctoServico_moeda_idMoeda, ")
			.append("doctoServico.moeda.sgMoeda as doctoServico_moeda_sgMoeda, ")
			.append("doctoServico.moeda.dsSimbolo as doctoServico_moeda_dsSimbolo, ")
			.append("doctoServico.vlMercadoria as doctoServico_vlMercadoria, ")			
			.append("nc.tpStatusNaoConformidade as tpStatusNaoConformidade) ");
    	}
		sql.append("from ")
		.append(NaoConformidade.class.getName()).append(" as nc ")
		.append("inner join nc.filial as filial ")
		.append("left join nc.awb as awb ");
	 	Long idDoctoServico = map.getLong("doctoServico.idDoctoServico");
	 	Long idFilialOrigemDoctoServico = map.getLong("doctoServico.filialByIdFilialOrigem.idFilial");
	 	String tpDocumentoServico = map.getString("doctoServico.tpDocumentoServico");
	 	if (idDoctoServico != null || idFilialOrigemDoctoServico != null || !StringUtils.isBlank(tpDocumentoServico)) {
			sql.append("inner join nc.doctoServico as doctoServico ")
			.append("inner join doctoServico.filialByIdFilialOrigem as filialByIdFilialOrigem ")
			.append("inner join doctoServico.moeda as moeda ")
			.append("where 1=1 ");
			if (idDoctoServico != null) {
				sql.append("and doctoServico.id = ? ");
				param.add(idDoctoServico);
			}
			if (tpDocumentoServico != null) {
				sql.append("and doctoServico.tpDocumentoServico = ? ");
				param.add(tpDocumentoServico);
			}
			if (idFilialOrigemDoctoServico != null) {
				sql.append("and filialByIdFilialOrigem.id = ? ");
				param.add(idFilialOrigemDoctoServico);
			}
	 	}
	 	else {
			sql.append("left join nc.doctoServico as doctoServico ")
			.append("left join doctoServico.filialByIdFilialOrigem as filialByIdFilialOrigem ")
			.append("left join doctoServico.moeda as moeda ")
			.append("where 1=1 ");
	 	}
		if (map.getLong("filial.idFilial") != null) {
	 		sql.append("and filial.id = ? "); 
	 		param.add(map.getLong("filial.idFilial"));
	 	}
	 	if (map.getInteger("nrNaoConformidade") != null) {
	 		sql.append("and nc.nrNaoConformidade = ? "); 
	 		param.add( map.getInteger("nrNaoConformidade"));
	 	}
	 	if (map.getString("tpStatusNaoConformidade") != null && !map.getString("tpStatusNaoConformidade").equals("")) {
	 		sql.append("and nc.tpStatusNaoConformidade = ? "); 
	 		param.add( map.getString("tpStatusNaoConformidade"));
	 	}
	 	if (StringUtils.isNotBlank(map.getString("naoConformidade.tpModal"))) {
	 		sql.append("and nc.tpModal = ? "); 
	 		param.add( map.getString("naoConformidade.tpModal"));
	 	}
	 	if (map.getLong("awb.idAwb") != null) {
	 		sql.append("and awb.idAwb = ? "); 
	 		param.add( map.getLong("awb.idAwb"));
	 	}
	 	YearMonthDay dtInicial = map.getYearMonthDay("ocorrenciaNaoConformidade.dataInclusaoInicial");
	 	YearMonthDay dtFinal = map.getYearMonthDay("ocorrenciaNaoConformidade.dataInclusaoFinal");
	 	Long idFilialLegado = map.getLong("filialLegado.idFilial");
	 	Integer nrRncLegado = map.getInteger("nrRncLegado");
	 	if (dtInicial != null || dtFinal != null || idFilialLegado != null || nrRncLegado != null) {
	 		sql.append("and exists ( ")
	 		.append("select 1 from ")
	 		.append(OcorrenciaNaoConformidade.class.getName()).append(" as onc ")
	 		.append("where ")
	 		.append("onc.naoConformidade.id = nc.id ");
		 	if (dtInicial != null) {
		 		sql.append("and trunc(cast (onc.dhInclusao.value as date)) >= ? "); 
		 		param.add(dtInicial);
		 	}
		 	if (dtFinal != null) {
		 		sql.append("and trunc(cast (onc.dhInclusao.value as date)) <= ? "); 
		 		param.add(dtFinal);
		 	}
		 	if (idFilialLegado != null) {
		 		sql.append("and onc.filialByIdFilialLegado.id = ? "); 
		 		param.add(idFilialLegado);
		 	}
		 	if (nrRncLegado != null) {
		 		sql.append("and onc.nrRncLegado = ? "); 
		 		param.add(nrRncLegado);
		 	}
		 	sql.append(") ");
	 	}
	 	return sql;
    }

    /**
     * Retorna um map com os objetos a serem mostrados na grid.
     * 
     * @param TypedFlatMap map
     * @param FindDefinition findDefinition
     * @return ResultSetPage com os dados da grid.
     */
    public ResultSetPage findPaginatedByNaoConformidade(TypedFlatMap map, FindDefinition findDefinition){
    	List param = new ArrayList();
    	StringBuffer sql = getDetachedCriteriaByNaoConformidade(map, param, false);
		sql.append("order by filial.sgFilial, nc.nrNaoConformidade");
		return getAdsmHibernateTemplate().findPaginated(sql.toString(), findDefinition.getCurrentPage(), findDefinition.getPageSize(), param.toArray());	
    }


    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param map
     * @return Integer com o numero de registos com os dados da grid.
     */
    public Integer getRowCountByNaoConformidade(TypedFlatMap map) {
    	List param = new ArrayList();
    	StringBuffer sql = getDetachedCriteriaByNaoConformidade(map, param, true);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
    }
    
    /**
	 * retorna uma lista de notas fiscais conhecimento pelo id da nao
	 * conformidade
	 * 
	 */
	public List findNotaFiscalConhecimentoByIdNaoConformidade(Long idNaoConformidade) {
		SqlTemplate hql = new SqlTemplate();
		hql.setDistinct();
		hql.addProjection("nfc");
		hql.addFrom(NaoConformidade.class.getName()+  " nc " +
				" join nc.ocorrenciaNaoConformidades onc ");
		hql.addFrom(NotaOcorrenciaNc.class.getName(),  " nonc ");
		hql.addFrom(NotaFiscalConhecimento.class.getName(),  " nfc ");
		
		hql.addCriteria("nc.id", "=", idNaoConformidade);
		hql.addCustomCriteria("onc.id = nonc.ocorrenciaNaoConformidade.id");
		hql.addCustomCriteria("nonc.notaFiscalConhecimento.id = nfc.id");
		
		return getAdsmHibernateTemplate().find(hql.getSql(), hql.getCriteria());
	}
    
    /**
     * Método que retorna uma NaoConformidade a partir de um ID de DoctoServico.
     * 
     * @param idDoctoServico
     * @return
     */
    public NaoConformidade findNaoConformidadeByIdDoctoServico(Long idDoctoServico) {
		SqlTemplate hql = new SqlTemplate();		
		hql.addFrom(NaoConformidade.class.getName() + " nc join fetch nc.filial f " +
												   	  " join fetch f.pessoa fp " +
												   	  " left join fetch nc.doctoServico ds " +
												   	  " left join fetch ds.filialByIdFilialOrigem dsfo ");
		
		hql.addCriteria("ds.id", "=", idDoctoServico);
				
		return (NaoConformidade) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    } 
    
    public NaoConformidade findByIdDoctoServicoIdFilial(Long idDoctoServico, Long idFilial) {
        StringBuilder sql = new StringBuilder()
            .append(" select nc from "+NaoConformidade.class.getName()+" nc ")
            .append(" where nc.doctoServico.id = ? ")
            .append(" nc.filial.id= ? ")
            .append(" order by nc.id desc ");
        return (NaoConformidade)getAdsmHibernateTemplate().find(sql.toString(), new Object[]{idDoctoServico, idFilial}).get(0);
    }
    
    
    
    /**
     * Método que retorna uma NaoConformidade a partir de um ID de DoctoServico
     * sem fazer FETCH em nenhuma outra entidade.
     * 
     * @param idDoctoServico
     * @return
     */
    public NaoConformidade findByIdDoctoServico(Long idDoctoServico) {
		StringBuilder sql = new StringBuilder()
			.append("select nc from "+NaoConformidade.class.getName()+" nc ")
			.append("where nc.doctoServico.id = ?");
		return (NaoConformidade)getAdsmHibernateTemplate().findUniqueResult(sql.toString(), new Object[]{idDoctoServico});
    }  
    
    /**
     * GetRowCount da tela de <code>Consultar mercadorias em pendencia</code>.
     * 
     * @param idFilialAbertura
     * @param idFilialResponsavel
     * @param dhInclusao
     * @param idDoctoServico
     * @param idMotivoAberturaNC
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param dsOcorrenciaNC
     * @return
     */
    public Integer getRowCountNaoConformidadeRegistrarOcorrencia(Long idFilialAbertura, Long idFilialResponsavel, 
    		DateTime dhInclusaoInicial, DateTime dhInclusaoFinal,
    		String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, Long idMotivoAberturaNC, Long idClienteRemetente, Long idClienteDestinatario, String dsOcorrenciaNC) {

    	SqlTemplate sql = getSqlTemplateNaoConformidadeRegistrarOcorrencia(idFilialAbertura, 
    			idFilialResponsavel, dhInclusaoInicial, dhInclusaoFinal, tpDoctoServico, idFilialOrigem, idDoctoServico, 
    			idMotivoAberturaNC, idClienteRemetente, idClienteDestinatario, dsOcorrenciaNC);
    	sql.addProjection("count(*) as rowcount");
    	Long result = (Long)getAdsmHibernateTemplate().findUniqueResult(sql.getSql(true), sql.getCriteria());
    	return result.intValue();
    }
    
    /**
     * FindPaginated da tela de <code>Consultar mercadorias em pendencia</code>.
     * 
     * @param idFilialAbertura
     * @param idFilialResponsavel
     * @param dhInclusao
     * @param idDoctoServico
     * @param idMotivoAberturaNC
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param dsOcorrenciaNC
     * @return
     */
    public ResultSetPage findPaginatedNaoConformidadeRegistrarOcorrencia(Long idFilialAbertura, Long idFilialResponsavel, 
    		DateTime dhInclusaoInicial, DateTime dhInclusaoFinal, 
    		String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, Long idMotivoAberturaNC, Long idClienteRemetente, 
    		Long idClienteDestinatario, String dsOcorrenciaNC, FindDefinition findDefinition) {
    	
    	SqlTemplate sql = getSqlTemplateNaoConformidadeRegistrarOcorrencia(idFilialAbertura, 
    			idFilialResponsavel, dhInclusaoInicial, dhInclusaoFinal, tpDoctoServico, idFilialOrigem, 
    			idDoctoServico, idMotivoAberturaNC, idClienteRemetente, idClienteDestinatario, dsOcorrenciaNC);
    	
    	StringBuffer projecao = new StringBuffer();
		projecao.append("new map(naoConformidade.idNaoConformidade as idNaoConformidade, ");
		projecao.append("filialOrigem.sgFilial as sgFilialOrigem, ");
		projecao.append("doctoServico.nrDoctoServico as nrDoctoServico, ");
		projecao.append("doctoServico.idDoctoServico as idDoctoServico, ");
		projecao.append("doctoServico.tpDocumentoServico as tpDoctoServico, ");
		projecao.append("filialNaoConformidade.sgFilial as sgFilialNC, ");
		projecao.append("naoConformidade.nrNaoConformidade as nrNaoConformidade, ");
		projecao.append("ocorrenciaNaoConformidade.nrOcorrenciaNc as nrOcorrenciaNc, ");
		projecao.append("motivoAberturaNC.dsMotivoAbertura as dsMotivoAbertura, ");
		projecao.append("pessoaRemetente.nmPessoa as nmPessoaRemetente, ");
		projecao.append("pessoaDestinatario.nmPessoa as nmPessoaDestinatario, ");
		projecao.append("ocorrenciaNaoConformidade.dsOcorrenciaNc as dsOcorrenciaNc, ");
		projecao.append("ocorrenciaNaoConformidade.dhInclusao as dhInclusao) ");
		
		sql.addProjection(projecao.toString());
		
		
		
        return getAdsmHibernateTemplate().findPaginated(sql.getSql(),findDefinition.getCurrentPage(), findDefinition.getPageSize(), sql.getCriteria());
    }
    
    /**
     * Gera uma sql template para as funcoes getRowCountNaoConformidadeRegistrarOcorrencia
     * e findPaginatedNaoConformidadeRegistrarOcorrencia.
     * 
     * @param idFilialAbertura
     * @param idFilialResponsavel
     * @param dhInclusaoInicial
     * @param dhInclusaoFinal
     * @param idDoctoServico
     * @param idMotivoAberturaNC
     * @param idClienteRemetente
     * @param idClienteDestinatario
     * @param dsOcorrenciaNC
     * @return
     */
    private SqlTemplate getSqlTemplateNaoConformidadeRegistrarOcorrencia(Long idFilialAbertura, Long idFilialResponsavel, 
    		DateTime dhInclusaoInicial, DateTime dhInclusaoFinal, 
    		String tpDoctoServico, Long idFilialOrigem, Long idDoctoServico, Long idMotivoAberturaNC, Long idClienteRemetente, 
    		Long idClienteDestinatario, String dsOcorrenciaNC) {
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(NaoConformidade.class.getName() + " naoConformidade " +
				"left join naoConformidade.filial filialNaoConformidade " +
				"left join naoConformidade.ocorrenciaNaoConformidades ocorrenciaNaoConformidade " +
  				"left join ocorrenciaNaoConformidade.filialByIdFilialAbertura filialAbertura " +
				"left join ocorrenciaNaoConformidade.filialByIdFilialResponsavel filialResponsavel " +
  				"left join naoConformidade.doctoServico doctoServico " +
  				"left join doctoServico.filialByIdFilialOrigem filialOrigem " +
				"left join ocorrenciaNaoConformidade.motivoAberturaNc motivoAberturaNC " +
				"left join naoConformidade.clienteByIdClienteRemetente clienteRemetente " +
				"left join naoConformidade.clienteByIdClienteDestinatario clienteDestinatario " +
				"left join clienteDestinatario.pessoa pessoaDestinatario " +
				"left join clienteRemetente.pessoa pessoaRemetente");
			
		if (idFilialAbertura!=null) sql.addCriteria("filialAbertura.idFilial", "=", idFilialAbertura);
		if (idFilialResponsavel!=null) sql.addCriteria("filialResponsavel.idFilial", "=", idFilialResponsavel);
		
		if (dhInclusaoInicial!=null) sql.addCriteria("ocorrenciaNaoConformidade.dhInclusao.value",  ">=",  dhInclusaoInicial);
		if (dhInclusaoFinal!=null) sql.addCriteria("ocorrenciaNaoConformidade.dhInclusao.value", "<=", dhInclusaoFinal);
		
		if (idFilialOrigem!=null) sql.addCriteria("filialOrigem.idFilial", "=", idFilialOrigem);
		if (tpDoctoServico!=null) sql.addCriteria("doctoServico.tpDocumentoServico", "=", tpDoctoServico);
		if (idDoctoServico!=null) sql.addCriteria("doctoServico.idDoctoServico", "=", idDoctoServico);
		
		if (idMotivoAberturaNC!=null) sql.addCriteria("motivoAberturaNC.idMotivoAberturaNc", "=", idMotivoAberturaNC);
		if (idClienteRemetente!=null) sql.addCriteria("clienteRemetente.idCliente", "=", idClienteRemetente);
		if (idClienteDestinatario!=null) sql.addCriteria("clienteDestinatario.idCliente", "=", idClienteDestinatario);
		if ((dsOcorrenciaNC!=null) && (!dsOcorrenciaNC.equals(""))) sql.addCustomCriteria("lower(ocorrenciaNaoConformidade.dsOcorrenciaNc) like ('%" + dsOcorrenciaNC.toLowerCase() + "%') ");
		
		sql.addCriteria("naoConformidade.tpStatusNaoConformidade", "=", "RNC");
		sql.addCriteria("ocorrenciaNaoConformidade.tpStatusOcorrenciaNc", "=", "A");
		
		sql.addOrderBy("filialOrigem.sgFilial");
    	sql.addOrderBy("doctoServico.nrDoctoServico");
    	
    	return sql;
    	
    }
    
    /**
     * Obtém uma nao conformidade pelo id do documento de servico, fazendo join com suas ocorrencias e motivos de abertura. 
     * @param idDoctoServico
     * @return
     */
    public NaoConformidade findByIdDoctoServicoJoinOcorrencias(Long idDoctoServico) {
    	StringBuffer s = new StringBuffer()
    	.append("from "+NaoConformidade.class.getName()+" nc ")
    	.append(" left join fetch nc.ocorrenciaNaoConformidades onc")
    	.append(" join fetch nc.filial")
    	.append(" left join fetch onc.motivoAberturaNc ")
    	
    	.append(" where nc.doctoServico.id = ?");

    	return (NaoConformidade)getAdsmHibernateTemplate().findUniqueResult(s.toString(), new Object[]{idDoctoServico});
    }
    /**
     * verifica se existe uma nao conformidade para o documento de servico 
     * @param idDoctoServico
     * @return
     */
    public boolean findNCByIdDoctoServico(Long idDoctoServico) {
    	SqlTemplate hql = new SqlTemplate();
    	hql.addProjection("1");
    	hql.addFrom(NaoConformidade.class.getName()+ " nc " +
    			"left outer join nc.doctoServico ds");
    	hql.addCriteria("ds.idDoctoServico","=", idDoctoServico);
    	
    	return getAdsmHibernateTemplate().find(hql.getSql(),hql.getCriteria()).size()>0;
    	
    }
    
    /**
     * Carrega a entidade NaoConformidade de acordo com
     * o nrNaoConfornmidade e o idFilial.
     * 
     * Hector Julian Esnaola Junior
     * 22/02/2008
     *
     * @param nrNaoConformidade
     * @param idFilial
     * @return
     *
     * NaoConformidade
     *
     */
    public NaoConformidade findNaoConformidadeByNrNaoConformidadeAndIdFilial (
    		Integer nrNaoConformidade, 
    		Long idFilial) {
    	
    	SqlTemplate hql = new SqlTemplate();
    	hql.addFrom(NaoConformidade.class.getName() + " nc ");
    	hql.addCriteria("nc.nrNaoConformidade", "=", nrNaoConformidade);
    	hql.addCriteria("nc.filial.id", "=", idFilial);
    	
    	return (NaoConformidade)getAdsmHibernateTemplate()
    			.findUniqueResult(
    					hql.getSql(),
    					hql.getCriteria());
    }
    
    public void updateTpStatusNaoConformidade(Integer nrNaoConformidade, Long idFilial, String tpStatus) {
    	StringBuffer sql = new StringBuffer()
    	.append("update ")
    	.append(NaoConformidade.class.getName()).append(" as nc ")
    	.append(" set nc.tpStatusNaoConformidade = ?			")
    	.append(" where nc.filial.id = ? 	and					")
    	.append(" nc.nrNaoConformidade = ? 						");

    	List param = new ArrayList();
    	param.add(tpStatus);
    	param.add(idFilial);
    	param.add(nrNaoConformidade);


    	super.executeHql(sql.toString(), param);
    }
    
    /**
     * Método que retorna uma NaoConformidade a partir de um ID de DoctoServico e de um STATUS de NaoConformidade 
     * 
     * @param idDoctoServico
     * @param tpStatusNaoConformidade
     * @return
     */
    public NaoConformidade findByIdDoctoServicoAndStatusNaoConformidade(Long idDoctoServico, String tpStatusNaoConformidade) {
    	SqlTemplate hql = new SqlTemplate();
    	
    	hql.addProjection("nc");    	
    	hql.addFrom(NaoConformidade.class.getName() + " nc ");
    	hql.addCriteria("nc.doctoServico.id", "=", idDoctoServico);
    	hql.addCriteria("nc.tpStatusNaoConformidade", "!=", tpStatusNaoConformidade);

    	return (NaoConformidade)getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());
    }  
    
    /**
     * LMS - 4460
     * Buscar as Não Conformidades de acordo com uma lista de Ocorrências
     * @param listIdOcorrencia String com lista de ID´s de Ocorrências separadas por ','
     * @return
     */
    public List<NaoConformidade> findByOcorrenciasNaoConformidade(String listIdOcorrencia) {
    
    	StringBuffer sql = getSqlNaoConformidadeByOcorrencia(listIdOcorrencia);
    	
    	return (List<NaoConformidade>)getAdsmHibernateTemplate().find(sql.toString());
    }

	private StringBuffer getSqlNaoConformidadeByOcorrencia(	String listIdOcorrencia) {
    	StringBuffer sql = new StringBuffer()
    	.append("SELECT distinct(ocorrenciaNaoConformidade.naoConformidade) ")
    	.append("FROM ").append(OcorrenciaNaoConformidade.class.getName()).append(" as ocorrenciaNaoConformidade ")
    	.append("WHERE ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade IN (").append(listIdOcorrencia).append(")");
		return sql;
	}
    	
    public List<NaoConformidade> findByOcorrenciasNaoConformidadeWithOcorrenciaEmAberto(String listIdOcorrencia) {
        
    	StringBuffer sql = getSqlNaoConformidadeByOcorrencia(listIdOcorrencia)
    	.append(" AND exists (")
    	.append(" select 1 from " + OcorrenciaNaoConformidade.class.getName() + " onc ")
    	.append(" where onc.naoConformidade.idNaoConformidade = ocorrenciaNaoConformidade.naoConformidade.idNaoConformidade ")
    	.append(" and onc.tpStatusOcorrenciaNc = 'A' ) ");
    	
    	return (List<NaoConformidade>)getAdsmHibernateTemplate().find(sql.toString());
    }

    

	public ResultSetPage findPaginatedByAwb(TypedFlatMap typedFlatMap, FindDefinition createFindDefinition) {
		List param = new ArrayList();
    	StringBuilder sql = getDetachedCriteriaByAwb(typedFlatMap, param, false);
		sql.append("order by filial.sgFilial, nc.nrNaoConformidade");
		return getAdsmHibernateTemplate().findPaginated(sql.toString(), createFindDefinition.getCurrentPage(), createFindDefinition.getPageSize(), param.toArray());
	}
	
	public Integer getRowCountByAwb(TypedFlatMap map) {
    	List param = new ArrayList();
    	StringBuilder sql = getDetachedCriteriaByAwb(map, param, true);
    	return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
    }
	
	private StringBuilder getDetachedCriteriaByAwb(TypedFlatMap map, List param, boolean isRowCount) {
		StringBuilder sql = new StringBuilder();
		if (!isRowCount) {
			sql.append("select new map(nc.idNaoConformidade as idNaoConformidade, ")
			.append("filial.sgFilial as filial_sgFilial, ")
			.append("filial.idFilial as filial_idFilial, ")
			.append("nc.nrNaoConformidade as nrNaoConformidade, ")
			.append("doctoServico.idDoctoServico as doctoServico_idDoctoServico, ")
			.append("doctoServico.tpDocumentoServico as doctoServico_tpDocumentoServico, ")
			.append("doctoServico.filialByIdFilialOrigem.sgFilial as doctoServico_filialByIdFilialOrigem_sgFilial,")
			.append("doctoServico.nrDoctoServico as doctoServico_nrDoctoServico, ")			
			.append("doctoServico.qtVolumes as doctoServico_qtVolumes, ")
			.append("doctoServico.psReal as doctoServico_psReal, ")
			.append("doctoServico.moeda.idMoeda as doctoServico_moeda_idMoeda, ")
			.append("doctoServico.moeda.sgMoeda as doctoServico_moeda_sgMoeda, ")
			.append("doctoServico.moeda.dsSimbolo as doctoServico_moeda_dsSimbolo, ")
			.append("doctoServico.vlMercadoria as doctoServico_vlMercadoria, ")			
			.append("nc.tpStatusNaoConformidade as tpStatusNaoConformidade) ");
    	}
		sql.append("from ")
		.append(NaoConformidade.class.getName()).append(" as nc, ")
		.append(CtoAwb.class.getName()).append(" as ca ")
		.append("inner join ca.conhecimento co ")
		.append("inner join ca.awb awb ")
		.append("inner join nc.filial as filial ");
	
		sql.append("inner join nc.doctoServico as doctoServico ")
		.append("inner join doctoServico.filialByIdFilialOrigem as filialByIdFilialOrigem ")
		.append("inner join doctoServico.moeda as moeda ")
		.append("where 1=1 ")
		.append("and doctoServico.idDoctoServico = co.idDoctoServico ")
		.append("and awb.idAwb = ? ");
		param.add(map.getLong("awb.idAwb"));
	
		return sql;
	}
	
	public List executeRelatorioNaoConformidade(StringBuilder sql, Map parameters, ConfigureSqlQuery csq) {
		return getAdsmHibernateTemplateReadOnly().findBySql(sql.toString(), parameters, csq);
	}
}