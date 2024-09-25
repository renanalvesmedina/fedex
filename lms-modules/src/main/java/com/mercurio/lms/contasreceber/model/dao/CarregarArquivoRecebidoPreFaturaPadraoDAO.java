package com.mercurio.lms.contasreceber.model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.util.IntegerUtils;


/**
 * DAO pattern: Carregar Arquivo Recebido de Pré-Faturas.
 * Interface de carga das pré-faturas com o layout "Proceda"
 *  
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class CarregarArquivoRecebidoPreFaturaPadraoDAO extends BaseCrudDao{

	@Override
	protected Class getPersistentClass() {
		return null;
	}

	
	/**
	 * Obtem a filial e sigla da filial através da fialial de origem 
	 * relacionada a um centro de custo
	 * 
	 * @param sgFilialOrigem
	 * @return List
	 */
	public List<Object[]> findFilialUnidadeNegocio(String sgFilialOrigem, Long idEmpresa) {
		
		StringBuilder query = new StringBuilder();
		  
		query.append(" SELECT "); 
		query.append(" F.ID_FILIAL  AS IDFILIAL, ");
		query.append(" F.SG_FILIAL  AS SGFILIAL  ");
		query.append(" FROM UNIDADES_NEGOCIOS U, FILIAL F  ");
		query.append(" WHERE  ");
		query.append(" F.SG_FILIAL  = U.CENTRO_CUSTO_JDE AND  ");
		query.append(" F.ID_EMPRESA = ? AND  ");
		query.append(" U.SIGLA = ?           "); 	
		
		ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
	  			sqlQuery.addScalar("IDFILIAL",Hibernate.LONG);
	  			sqlQuery.addScalar("SGFILIAL",Hibernate.STRING);
			}
		};	
		
		return getAdsmHibernateTemplate().findPaginatedBySql(query.toString(),IntegerUtils.ONE,IntegerUtils.ONE, 
				new Object[]{idEmpresa,sgFilialOrigem},csq).getList();
		
	}	
	
	
    /**
     * 
     * Obtem os dados de DOCTO_SERVICO, SERVICO, DEVEDOR_DOC_SERV_FAT, PESSOA 
     * para regras de negocio na importacao do arquivo pré faturas xsl
     * 
     * @param  sgFilialOrigem
     * @param  tpDoctoServico
     * @param  nrDoctoServico
     * @param  dtEmissao
     * @return List
     */
    public List<Object[]> findDataDocServicoPreFaturaPadrao(String sgFilialOrigem, String tpDoctoServico, Long nrDoctoServico, 
    		Long idEmpresa, Long idCliente, YearMonthDay dtEmissao) {
    	
    	StringBuilder query = new StringBuilder();
    	    	
    	query.append(" SELECT "); 
  		query.append(" S.TP_MODAL AS TPMODAL, "); 
    	query.append(" S.TP_ABRANGENCIA AS TPABRANGENCIA, "); 
    	query.append(" DS.ID_DOCTO_SERVICO AS IDDOCTOSERVICO,  ");
    	query.append(" P.NR_IDENTIFICACAO AS IDENTIFICACAO,   ");
    	query.append(" DS.NR_DOCTO_SERVICO AS NRDOCTOSERVICO,   ");
    	query.append(" DS.DH_EMISSAO AS DHEMISSAO,    ");
    	query.append(" DEV.ID_DEVEDOR_DOC_SERV_FAT AS IDDEVEDORDOCTOSERVICOSERVFAT, ");
    	query.append(" DEV.VL_DEVIDO AS VLDEVIDO,   ");
    	query.append(" DEV.TP_SITUACAO_COBRANCA AS TPSITUACAOCOBRANCA, ");
    	query.append(" DEV.ID_FILIAL AS IDFILIALDEVDOCFAT, ");
    	query.append(" MDE.TP_SITUACAO_DOCUMENTO AS TPSITUACAODOCUMENTO ");

    	query.append(" FROM ");
    	query.append(" DOCTO_SERVICO DS, "); 
    	query.append(" SERVICO S, ");
    	query.append(" DEVEDOR_DOC_SERV_FAT DEV, "); 
    	query.append(" PESSOA P, "); 
    	query.append(" FILIAL F, ");
    	query.append(" MONITORAMENTO_DOC_ELETRONICO MDE ");
    	
    	query.append(" WHERE ");
    	query.append(" DS.ID_SERVICO = S.ID_SERVICO AND ");
    	query.append(" DS.ID_FILIAL_ORIGEM = F.ID_FILIAL AND ");
    	query.append(" DS.ID_DOCTO_SERVICO = DEV.ID_DOCTO_SERVICO AND ");
    	query.append(" DEV.ID_CLIENTE = P.ID_PESSOA AND ");
    	query.append(" DS.ID_DOCTO_SERVICO = MDE.ID_DOCTO_SERVICO(+) AND ");
    	query.append(" DEV.ID_CLIENTE = :idCliente AND ");    	    	
    	query.append(" F.SG_FILIAL = :sgFilial AND ");
    	query.append(" F.ID_EMPRESA = :idEmpresa AND ");
    	query.append(" DS.TP_DOCUMENTO_SERVICO = :tpDoctoServico AND ");
    	query.append(" DS.NR_DOCTO_SERVICO = :nrDoctoServico AND ");
    	query.append(" TRUNC(DS.DH_EMISSAO) = :dtEmissao ");  
    	    	
    	Map<String, Object> criteria = new HashMap<String, Object>();
    	criteria.put("idCliente", idCliente);
    	criteria.put("sgFilial", sgFilialOrigem);
    	criteria.put("idEmpresa", idEmpresa);
    	criteria.put("tpDoctoServico", tpDoctoServico);
    	criteria.put("nrDoctoServico", nrDoctoServico);
    	criteria.put("dtEmissao",dtEmissao);
    	    	    
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
	  			sqlQuery.addScalar("TPMODAL",Hibernate.STRING);
	  			sqlQuery.addScalar("TPABRANGENCIA",Hibernate.STRING);
	  			sqlQuery.addScalar("IDDOCTOSERVICO",Hibernate.LONG);
	  			sqlQuery.addScalar("IDENTIFICACAO",Hibernate.STRING);
	  			sqlQuery.addScalar("NRDOCTOSERVICO",Hibernate.LONG);
	  			sqlQuery.addScalar("DHEMISSAO",Hibernate.TIMESTAMP);
	  			sqlQuery.addScalar("IDDEVEDORDOCTOSERVICOSERVFAT",Hibernate.LONG);
	  			sqlQuery.addScalar("VLDEVIDO",Hibernate.BIG_DECIMAL);
	  			sqlQuery.addScalar("TPSITUACAOCOBRANCA",Hibernate.STRING);
	  			sqlQuery.addScalar("IDFILIALDEVDOCFAT",Hibernate.LONG);
	  			sqlQuery.addScalar("TPSITUACAODOCUMENTO",Hibernate.STRING);
			}
		};

		return getAdsmHibernateTemplate().findBySql(query.toString(), criteria, csq);
    }
    
    /**
     * Obtem a situacao da nota de débito através do documento de serviço
     * @param idDocumento
     * @return
     */
    public String findTpSituacaoNotaDebito(Long idDocumento){
    	
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT TP_SITUACAO_NOTA_DEBITO_NAC AS TPSITUACAO");
    	query.append(" FROM NOTA_DEBITO_NACIONAL  ");
    	query.append(" WHERE  ");
    	query.append(" ID_NOTA_DEBITO_NACIONAL = ? ");
    	
    	return findTpSituacao(query,idDocumento);
    	
    }
    
    /**
     * Obtem a situação da nota de serviço através do id do documento
     * 
     * @param idDocumento
     * @return
     */
    public String findTpSituacaoNotaServico(Long idDocumento){
    	
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT TP_SITUACAO_NF AS TPSITUACAO");
    	query.append(" FROM NOTA_FISCAL_SERVICO  ");
    	query.append(" WHERE  ");
    	query.append(" ID_NOTA_FISCAL_SERVICO = ? ");
    	
    	return findTpSituacao(query,idDocumento);
    	
    }
    
    /**
     * Obtem a situação do conhecimento através do documento
     * 
     * @param idDocumento
     * @return
     */
    public String findTpSituacaoConhecimento(Long idDocumento){
    	
    	StringBuilder query = new StringBuilder();
    	query.append(" SELECT TP_SITUACAO_CONHECIMENTO AS TPSITUACAO");
    	query.append(" FROM CONHECIMENTO  ");
    	query.append(" WHERE  ");
    	query.append(" ID_CONHECIMENTO = ? ");
    
    	return findTpSituacao(query,idDocumento);
    }
	
    /**
     * Efetua a pesquisa da situação de um documento 
     * 
     * @param query
     * @param idDocumento
     * @return
     */
    private String findTpSituacao(StringBuilder query, Long idDocumento){
    	
    	ConfigureSqlQuery csq = new ConfigureSqlQuery() {
			public void configQuery(org.hibernate.SQLQuery sqlQuery) {
	  			
	  			sqlQuery.addScalar("TPSITUACAO",Hibernate.STRING);
			}
		};    	
    	
		List list = getAdsmHibernateTemplate().findPaginatedBySql(query.toString(),IntegerUtils.ONE,IntegerUtils.ONE, 
				new Object[]{idDocumento},csq).getList();
		
		if(list.isEmpty()){
			return null;
		}

		return (String)list.get(0);    	
    } 
    
}
