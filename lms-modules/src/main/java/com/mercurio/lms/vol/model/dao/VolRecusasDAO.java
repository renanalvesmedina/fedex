package com.mercurio.lms.vol.model.dao;


import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolRecusas;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class VolRecusasDAO extends BaseCrudDao<VolRecusas, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return VolRecusas.class;
    }
    
    
    /**
	 * retorna a o número do documento da recusa, nome do cliente e os contatos da recusa
	 * @param criteria
	 * @return 
	 */
    public List findRecusa(Long idRecusa){
    	SqlTemplate sql = this.createHQLRecusaCliente(idRecusa);
    	StringBuffer projecao = new StringBuffer()
    		.append("new Map (" )
    		.append("vr.dhTratativa as dhTratativa, ")
    		.append("vr.filial.idFilial as idFilial, ")
    		.append("vr.dhRecusa as dhRecusa, ")
    		.append("email.idEmailRecusa as idEmailRecusa, ")
    		.append("vcon.nmContato as nmContato, ")
    		.append("ds.nrDoctoServico as nrConhecimento, ")	
    		.append("oe.dsOcorrenciaEntrega as dsOcorrenciaEntrega, ")
    		.append("p.nrIdentificacao as nrIdentificacao, ")
    		.append("p.nmPessoa as destinatario)");
    	
    	sql.addProjection(projecao.toString());
    			
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    public SqlTemplate createHQLRecusaCliente(Long idRecusa){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolRecusas.class.getName(), "as vr INNER JOIN vr.ocorrenciaEntrega as oe " +
    	"LEFT JOIN vr.volEmailsRecusas as email " +
    	"LEFT JOIN email.volContatos as vcon " +		
    	"INNER JOIN vr.manifestoEntregaDocumento as med " +
    	"INNER JOIN med.doctoServico as ds " +
    	"INNER JOIN ds.clienteByIdClienteDestinatario as cli INNER JOIN cli.pessoa as p" );
    		
     	sql.addCriteria("vr.idRecusa", "=", idRecusa);
    	
    	return sql;
    }
    
    /**
     * Recupera os contatos relacionados com a recusa 
     * @param idRecusa
     * @return contatos
     */
    public List findContato(TypedFlatMap criteria){
    	SqlTemplate sql = this.createHQLContato(criteria);
    	
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new Map (" )
    		.append("email.idEmailRecusa as idEmailRecusa, ")
    		.append("vcon.nmContato as nmContato)");
    		
    	sql.addProjection(projecao.toString());
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }
    
    private SqlTemplate createHQLContato(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolRecusas.class.getName(), "as vr INNER JOIN vr.volEmailsRecusas as email INNER JOIN email.volContatos as vcon" );
    	sql.addCriteria("vr.idRecusa", "=", criteria.getLong("idRecusa"));
    	
    	return sql;
    }


    public ResultSetPage findPaginatedRecusaHistorico(TypedFlatMap criteria, FindDefinition fd){
    	SqlTemplate sql = this.createHQLRecusaHistorico(criteria);
    	
    	StringBuffer projecao = new StringBuffer();
    	projecao.append("new Map ( " )
    		//data
    		.append("vr.dhRecusa as dhRecusa, ")
    		.append("vr.dhEnvio as dhEnvio, ")
    		.append("vr.dhTratativa as dhTratativa, ")
    		.append("vr.dhResolucao as dhResolucao, ")
    		
    		//tipo
    		.append("vr.tpRecusa as tpRecusa, ")
    		
    		//responsável
    		.append("p.nmPessoa as motorista, ")
    		.append("uenv.nmUsuario as usuenv, ")
    		.append("vcon.nmContato as emailRecusa, ")
    		.append("vr.contato as contato, ")
    		.append("usu.nmUsuario as usu, ")
    		.append("resol.nmUsuario as resol, ")
    		
    		//observação
    		.append("oe.dsOcorrenciaEntrega as dsOcorrenciaEntrega, ")
    		.append("vr.obEnvio as obEnvio, ")
    		.append("vr.obTratativa as obTratativa, ")
    		.append("vr.obResolucao as obResolucao)");
    	
    	sql.addProjection(projecao.toString());
    	
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(),
  			   sql.getCriteria()); 
    }
    
    /**
	 * Recupera recusa de acordo com os parâmetros escolhidos
	 * @param criteria
	 */
    public ResultSetPage findPaginatedRecusa(TypedFlatMap criteria, FindDefinition fd){
    	SqlTemplate sql = this.createHQLRecusa(criteria);
    	sql.addProjection(createSqlRecusa());
    	sql.addOrderBy("vr.dhRecusa.value desc");
    	return getAdsmHibernateTemplate().findPaginated(sql.getSql(), fd.getCurrentPage(), fd.getPageSize(), sql.getCriteria()); 
    }
    
    public List findRecusa(TypedFlatMap criteria){
    	SqlTemplate sql = this.createHQLRecusa(criteria);
    	sql.addProjection(createSqlRecusa());
    	sql.addOrderBy("vr.dhRecusa.value desc");
    	return getAdsmHibernateTemplate().find(sql.getSql(),sql.getCriteria());
    }


	private String createSqlRecusa() {
		return new StringBuffer()
    		.append("new map (" )
			.append("vr.idRecusa as idRecusa, ")
			.append("ds.nrDoctoServico as nrConhecimento, ")
			.append("ds.idDoctoServico as idDoctoServico, ")
			.append("ds.tpDocumentoServico as tpDocumentoServico, ")
			.append("filial.sgFilial as sgFilial , ")
			.append("filial.idFilial as idFilial, ")
			.append("pDest.nmPessoa as destinatario, ")
			.append("pRem.nmPessoa as remetente, ")
			.append("ds.dtPrevEntrega as dpe, ")
			.append("med.dhOcorrencia as dhOcorrencia, ")
			.append("med.nmRecebedor as nmRecebedor, ")
			.append("vr.dhResolucao as dhResolucao, ")
			.append("vequip.dsNumero as numero, ")
			.append("mt.nrFrota as frota, ")
			.append("mt.nrIdentificador as nrIdentificador, ")
			.append("vr.dhRecusa as dhRecusa, ")
			.append("oe.dsOcorrenciaEntrega as dsOcorrenciaEntrega, ")
			.append("vr.tpRecusa as opcaoCliente) " )
			.toString();		
	}
    
    
    public Integer getRowCountRecusaHistorico(TypedFlatMap criteria){
        SqlTemplate sql = this.createHQLRecusaHistorico(criteria);
   	   return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
     }
    
    public Integer getRowCountRecusa(TypedFlatMap criteria){
       SqlTemplate sql = this.createHQLRecusa(criteria);
  	   return getAdsmHibernateTemplate().getRowCountForQuery(sql.getSql(), sql.getCriteria());
    }
    
    /**
     * SQL que recupera o histórico das recusas
     * @param criteria
     * @return histórico das recusas
     */
    private SqlTemplate createHQLRecusaHistorico(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolRecusas.class.getName(), "as vr INNER JOIN vr.ocorrenciaEntrega as oe " +
    			"LEFT JOIN vr.usuarioByIdEnviou as uenv " +
    			"LEFT JOIN vr.usuarioByIdUsuario as usu " +
    			"LEFT JOIN vr.usuarioByIdResolucao as resol " +
    			"LEFT JOIN vr.volEmailsRecusa as email " +
    			"LEFT JOIN email.volContatos as vcon " +
    			"INNER JOIN vr.manifestoEntregaDocumento as med " +
    			"INNER JOIN med.manifestoEntrega as me " +
    			"INNER JOIN med.doctoServico as ds " +
    			"INNER JOIN me.manifesto as m " +
    			"INNER JOIN m.controleCarga as c " +
    			"INNER JOIN c.motorista as mot " +
    			"INNER JOIN mot.pessoa as p");
    	sql.addCriteria("vr.idRecusa", "=", criteria.getLong("idRecusa"));
       	
    	return sql;
    }
    
    
    /**
     * SQL que recupera as recusas
     * @param criteria
     * @return volRecusas
     */
    private SqlTemplate createHQLRecusa(TypedFlatMap criteria){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolRecusas.class.getName(), "as vr INNER JOIN vr.ocorrenciaEntrega as oe " +
    			"INNER JOIN vr.manifestoEntregaDocumento as med " +
    			"INNER JOIN med.manifestoEntrega as me " +
    			"INNER JOIN me.manifesto as m " +
    			"INNER JOIN m.controleCarga as c " +
    			"INNER JOIN c.meioTransporteByIdTransportado as mt " +
    			"LEFT JOIN mt.volEquipamentos as vequip " +
    			"INNER JOIN med.doctoServico as ds " +
    			"INNER JOIN ds.clienteByIdClienteDestinatario as cliDest " +
    			"INNER JOIN ds.filialByIdFilialOrigem as filial " +
    			"INNER JOIN cliDest.pessoa as pDest " + 
    			"INNER JOIN ds.clienteByIdClienteRemetente as cliRem " +
    			"INNER JOIN cliRem.pessoa as pRem ");
    	
    	sql.addCriteria("me.filial.id", "=", criteria.getLong("filial.idFilial"));
    	sql.addCriteria("mt.idMeioTransporte","=",criteria.getLong("meioTransporte.idMeioTransporte"));
    	sql.addCriteria("cliRem.idCliente", "=", criteria.getLong("pessoa.idPessoa"));
        
        sql.addCustomCriteria("med.dhOcorrencia.value IS NOT NULL");
    	
    	if ((criteria.get("status")!= null) && (criteria.get("status").equals("M"))) { 
    		sql.addCustomCriteria("vr.dhResolucao.value IS NOT NULL");
    	} else {
    		sql.addCriteria("vr.tpRecusa", "=", criteria.getString("status"));
    	}
    	sql.addCriteria("vr.dhRecusa.value", ">=", criteria.getYearMonthDay("dataInicial"));
    	
    	if (criteria.getYearMonthDay("dataFinal") != null) {
			sql.addCriteria("vr.dhRecusa.value","<=", JTDateTimeUtils.createWithMaxTime(criteria.getYearMonthDay("dataFinal")));	
		}
    	
    	return sql;
    }
    
    
    /**
     * retorna VolRecusa pelo id do ManifestoEntregaDocumento
     * @param idManifestoEntregaDocumento
     * @return VolRecusas
     */
    public List findByIdManifestoEntregaDocumento(Long idManifestoEntregaDocumento){
    	SqlTemplate sql = this.createHQLManifestoEntregaDocumento(idManifestoEntregaDocumento);
    	
    	StringBuffer projecao = new StringBuffer()
    		.append("vr");
    	sql.addProjection(projecao.toString());
		 
		return getAdsmHibernateTemplate().find(sql.getSql(), idManifestoEntregaDocumento);
    }
    
    private SqlTemplate createHQLManifestoEntregaDocumento(Long idManifestoEntregaDocumento){
    	SqlTemplate sql = new SqlTemplate();
    	
    	sql.addFrom(VolRecusas.class.getName(), "as vr ");
    	sql.addCriteria("vr.manifestoEntregaDocumento.id", "=", idManifestoEntregaDocumento);
    	
    	return sql;
    }

}