package com.mercurio.lms.sgr.model.dao;

import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_CRITERIO_ORIGEM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.mercurio.adsm.framework.model.AdsmDao;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.ConfigureSqlQuery;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.edi.enums.CampoNotaFiscalEdiComplementoFedex;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.ExigenciaFaixaValor;
import com.mercurio.lms.sgr.model.ExigenciaGerRisco;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.FilialEnquadramento;
import com.mercurio.lms.sgr.model.MunicipioEnquadramento;
import com.mercurio.lms.sgr.model.PaisEnquadramento;
import com.mercurio.lms.sgr.model.TipoExigenciaGerRisco;
import com.mercurio.lms.sgr.model.UnidadeFederativaEnquadramento;
import com.mercurio.lms.workflow.model.HistoricoWorkflow;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;

/**
 * LMS-6850 - DAO para o Plano de Gerenciamento de Risco.
 * 
 * @author fabiano.pinto@cwi.com.br (Fabiano da Silveira Pinto)
 * 
 */
public class PlanoGerenciamentoRiscoDAO extends AdsmDao {

	private String doctoServicoQuery;
    private String doctoServicoCTeRedespachoIntermediarioQuery;
	private ConfigureSqlQuery doctoServicoConfig;

	/*
	 * Documentos de Serviço
	 */

	/**
	 * Busca dados de todos os {@link DoctoServico}s associados a um {@link ControleCarga}.
	 * 
	 * @param idControleCarga Id do {@link ControleCarga}.
	 * @param pgr_regra_fedex Flag que indicara se deverao ser desconsiderados conhecimentos
	 *        de redespacho intermediario
	 * @return Dados dos {@link DoctoServico} associados a um {@link ControleCarga}.
	 */
	public List<Object[]> findDoctoServico(Long idControleCarga, boolean pgr_regra_fedex) {
		List<Object[]> result = null;
		Map<String, Object> parametersValues = new HashMap<String, Object>();
		parametersValues.put("id_controle_carga", idControleCarga);
		result = getAdsmHibernateTemplate().findBySql(getDoctoServicoQuery(pgr_regra_fedex),
				parametersValues, 
				getDoctoServicoConfig());
		
		// LMSA-6078 selecao de documentos de servico com CTe com redespacho intermediario
		if (pgr_regra_fedex) {
		    String _sql = getDoctoServicoCTeRedespachoIntermediarioQuery();
			result.addAll(
					getAdsmHibernateTemplate().findBySql(_sql, 
							parametersValues, 
							getDoctoServicoConfig()));
		}
		return result;
	}

	/**
	 * Prepara SQL para busca de {@link DoctoServico}s associados a um
	 * {@link ControleCarga}. Referencia as tabelas:
	 * <ul>
	 * <li>{@code CONHECIMENTO};
	 * <li>{@code CONTROLE_CARGA};
	 * <li>{@code DOCTO_SERVICO};
	 * <li>{@code ENDERECO_PESSOA};
	 * <li>{@code EVENTO};
	 * <li>{@code EVENTO_COLETA};
	 * <li>{@code EVENTO_DOCUMENTO_SERVICO};
	 * <li>{@code FILIAL};
	 * <li>{@code MANIFESTO};
	 * <li>{@code MANIFESTO_COLETA};
	 * <li>{@code MANIFESTO_ENTREGA};
	 * <li>{@code MANIFESTO_ENTREGA_DOCUMENTO};
	 * <li>{@code MANIFESTO_NACIONAL_CTO};
	 * <li>{@code MANIFESTO_VIAGEM_NACIONAL};
	 * <li>{@code MUNICIPIO};
	 * <li>{@code NATUREZA_PRODUTO};
	 * <li>{@code PAIS};
	 * <li>{@code PEDIDO_COLETA};
	 * <li>{@code PESSOA};
	 * <li>{@code PRE_MANIFESTO_DOCUMENTO};
	 * <li>{@code SERVICO};
	 * <li>{@code UNIDADE_FEDERATIVA}.
	 * </ul>
	 * 
	 * @param pgr_regra_fedex Flag que indica que QUERY devera desconsiderar
	 *        conhecimentos de redespacho intermediario
	 * @return {@link String} da query para busca de {@link DoctoServico}s
	 *         associados a um {@link ControleCarga}.
	 */
	private String getDoctoServicoQuery(boolean pgr_regra_fedex) {
		if (doctoServicoQuery == null) {
			StringBuilder stringBuilder = new StringBuilder()
					.append("SELECT                                                                                ")
					.append("    SUM(d.vl_mercadoria)                       AS vl_mercadoria,                      ")
					.append("    d.tp_operacao,                                                                    ")
					.append("    d.id_moeda,                                                                       ")
					.append("    d.id_pais,                                                                        ")
					.append("    d.id_cliente_remetente,                                                           ")
					.append("    p_r.nr_identificacao                       AS nr_identificacao_remetente,         ")
					.append("    p_r.nm_pessoa                              AS nm_pessoa_remetente,                ")
					.append("    d.id_cliente_destinatario,                                                        ")
					.append("    p_d.nr_identificacao                       AS nr_identificacao_destinatario,      ")
					.append("    p_d.nm_pessoa                              AS nm_pessoa_destinatario,             ")
					.append("    d.id_natureza_produto,                                                            ")
					.append("    VI18N(np.ds_natureza_produto_i)            AS ds_natureza_produto,                ")
					.append("    d.tp_abrangencia,                                                                 ")
					.append("    d.id_filial_origem,                                                               ")
					.append("    f_o.sg_filial                              AS sg_filial_origem,                   ")
					.append("    m_o.id_municipio                           AS id_municipio_origem,                ")
					.append("    m_o.nm_municipio                           AS nm_municipio_origem,                ")
					.append("    uf_o.id_unidade_federativa                 AS id_unidade_federativa_origem,       ")
					.append("    uf_o.sg_unidade_federativa                 AS sg_unidade_federativa_origem,       ")
					.append("    pa_o.id_pais                               AS id_pais_origem,                     ")
					.append("    VI18N(pa_o.nm_pais_i)                      AS nm_pais_origem,                     ")
					.append("    d.id_filial_destino,                                                              ")
					.append("    f_d.sg_filial                              AS sg_filial_destino,                  ")
					.append("    m_d.id_municipio                           AS id_municipio_destino,               ")
					.append("    m_d.nm_municipio                           AS nm_municipio_destino,               ")
					.append("    uf_d.id_unidade_federativa                 AS id_unidade_federativa_destino,      ")
					.append("    uf_d.sg_unidade_federativa                 AS sg_unidade_federativa_destino,      ")
					.append("    pa_d.id_pais                               AS id_pais_destino,                    ")
					.append("    VI18N(pa_d.nm_pais_i)                      AS nm_pais_destino,                    ")
					.append("    d.tp_pedido_coleta,                                                               ")
					.append("    d.id_awb                                                                          ")
					.append("FROM                                                                                  ")
					.append("    (                                                                                 ")
					
					.append("        SELECT                                                                        ")
					.append("            'E'                                AS tp_operacao,                        ")
					.append("            ds.vl_mercadoria,                                                         ")
					.append("            ds.id_moeda,                                                              ")
					.append("            ds.id_pais,                                                               ")
					.append("            ds.id_cliente_remetente,                                                  ")
					.append("            ds.id_cliente_destinatario,                                               ")
					.append("            c.id_natureza_produto,                                                    ")
					.append("            s.tp_abrangencia,                                                         ")
					.append("            cc.id_filial_origem,                                                      ")
					.append("            cc.id_filial_destino,                                                     ")
					.append("            NULL                               AS tp_pedido_coleta,                   ")
					.append("            med.id_awb                                                                ")
					.append("        FROM                                                                          ")
					.append("            controle_carga cc,                                                        ")
					.append("            manifesto m,                                                              ")
					.append("            manifesto_entrega me,                                                     ")
					.append("            manifesto_entrega_documento med,                                          ")
					.append("            docto_servico ds,                                                         ")
					.append("            conhecimento c,                                                           ")
					.append("            servico s                                                                 ")
					.append("        WHERE cc.id_controle_carga             = m.id_controle_carga                  ")
					.append("        AND   m.id_manifesto                   = me.id_manifesto_entrega              ")
					.append("        AND   me.id_manifesto_entrega          = med.id_manifesto_entrega             ")
					.append("        AND   med.id_docto_servico             = ds.id_docto_servico                  ")
					.append("        AND   ds.id_docto_servico              = c.id_conhecimento (+)                ")
					.append("        AND   ds.id_servico                    = s.id_servico (+)                     ")
					.append("        AND   cc.id_controle_carga             = :id_controle_carga                   ")
					.append("        AND   m.tp_status_manifesto            NOT IN ('CA', 'DC', 'ED', 'FE')        ")
					.append("        AND   ds.tp_documento_servico          IN ('CTR', 'CRT', 'NFT', 'CTE', 'NTE') ")
					.append("        AND   NOT EXISTS (                                                            ")
					.append("            SELECT 1                                                                  ")
					.append("            FROM                                                                      ")
					.append("                evento_documento_servico eds,                                         ")
					.append("                evento e                                                              ")
					.append("            WHERE eds.id_evento                = e.id_evento                          ")
					.append("            AND   e.cd_evento                  = 21                                   ")
					.append("            AND   eds.bl_evento_cancelado      = 'N'                                  ")
					.append("            AND   eds.id_docto_servico         = ds.id_docto_servico                  ")
					.append("        )                                                                             ");
					
			// LMSA-6078; inserido flag liga/desliga para condicional que ignora o redespacho intermediario
			if (pgr_regra_fedex) {
				stringBuilder.append(" AND C.BL_REDESPACHO_INTERMEDIARIO IS NULL ");						
			}
					
			stringBuilder
					.append("        UNION ALL                                                                     ")
					.append("        SELECT                                                                        ")
					.append("            'C',                                                                      ")
					.append("            dc.vl_mercadoria,                                                         ")
					.append("            pc.id_moeda,                                                              ")
					.append("            uf.id_pais,                                                               ")
					.append("            pc.id_cliente,                                                            ")
					.append("            dc.id_cliente,                                                            ")
					.append("            dc.id_natureza_produto,                                                   ")
					.append("            s.tp_abrangencia,                                                         ")
					.append("            cc.id_filial_origem,                                                      ")
					.append("            cc.id_filial_destino,                                                     ")
					.append("            pc.tp_pedido_coleta,                                                      ")
					.append("            NULL                                                                      ")
					.append("        FROM                                                                          ")
					.append("            controle_carga cc,                                                        ")
					.append("            manifesto_coleta mc,                                                      ")
					.append("            pedido_coleta pc,                                                         ")
					.append("            detalhe_coleta dc,                                                        ")
					.append("            filial f,                                                                 ")
					.append("            pessoa p,                                                                 ")
					.append("            endereco_pessoa ep,                                                       ")
					.append("            municipio m,                                                              ")
					.append("            unidade_federativa uf,                                                    ")
					.append("            servico s                                                                 ")
					.append("        WHERE cc.id_controle_carga             = mc.id_controle_carga                 ")
					.append("        AND   mc.id_manifesto_coleta           = pc.id_manifesto_coleta               ")
					.append("        AND   pc.id_pedido_coleta              = dc.id_pedido_coleta                  ")
					.append("        AND   pc.id_filial_responsavel         = f.id_filial                          ")
					.append("        AND   f.id_filial                      = p.id_pessoa                          ")
					.append("        AND   p.id_endereco_pessoa             = ep.id_endereco_pessoa                ")
					.append("        AND   ep.id_municipio                  = m.id_municipio                       ")
					.append("        AND   m.id_unidade_federativa          = uf.id_unidade_federativa             ")
					.append("        AND   dc.id_servico                    = s.id_servico (+)                     ")
					.append("        AND   cc.id_controle_carga             = :id_controle_carga                   ")
					.append("        AND   mc.tp_status_manifesto_coleta    NOT IN ('CA', 'FE', 'ED')              ")
					.append("        AND   EXISTS (                                                                ")
					.append("            SELECT 1                                                                  ")
					.append("            FROM evento_coleta ec                                                     ")
					.append("            WHERE ec.tp_evento_coleta          NOT IN ('EX', 'CA', 'FD', 'ID')        ")
					.append("            AND   ec.id_pedido_coleta          = pc.id_pedido_coleta                  ")
					.append("        )                                                                             ")
					.append("        UNION ALL                                                                     ")
					.append("        SELECT                                                                        ")
					.append("            'V',                                                                      ")
					.append("            ds.vl_mercadoria,                                                         ")
					.append("            ds.id_moeda,                                                              ")
					.append("            ds.id_pais,                                                               ")
					.append("            ds.id_cliente_remetente,                                                  ")
					.append("            ds.id_cliente_destinatario,                                               ")
					.append("            c.id_natureza_produto,                                                    ")
					.append("            s.tp_abrangencia,                                                         ")
					.append("            cc.id_filial_origem,                                                      ")
					.append("            cc.id_filial_destino,                                                     ")
					.append("            NULL,                                                                     ")
					.append("            NULL                                                                      ")
					.append("        FROM                                                                          ")
					.append("            controle_carga cc,                                                        ")
					.append("            manifesto m,                                                              ")
					.append("            manifesto_viagem_nacional mvn,                                            ")
					.append("            manifesto_nacional_cto mnc,                                               ")
					.append("            conhecimento c,                                                           ")
					.append("            docto_servico ds,                                                         ")
					.append("            servico s                                                                 ")
					.append("        WHERE cc.id_controle_carga             = m.id_controle_carga                  ")
					.append("        AND   m.id_manifesto                   = mvn.id_manifesto_viagem_nacional     ")
					.append("        AND   mvn.id_manifesto_viagem_nacional = mnc.id_manifesto_viagem_nacional     ")
					.append("        AND   mnc.id_conhecimento              = c.id_conhecimento                    ")
					.append("        AND   c.id_conhecimento                = ds.id_docto_servico                  ")
					.append("        AND   ds.id_servico                    = s.id_servico (+)                     ")
					.append("        AND   cc.id_controle_carga             = :id_controle_carga                   ")
					.append("        AND   m.tp_status_manifesto            NOT IN ('CA', 'DC', 'ED', 'FE', 'PM')  ")
					.append("        AND   c.tp_documento_servico           IN ('CTR', 'CTE', 'NFT', 'NTE')        ");
					
			// LMSA-6078: inserido flag liga/desliga para condicional que ignora o redespacho intermediario
			if (pgr_regra_fedex) {
				stringBuilder.append(" AND C.BL_REDESPACHO_INTERMEDIARIO IS NULL ");						
			}
					
			stringBuilder
					.append("        UNION ALL                                                                     ")
					.append("        SELECT                                                                        ")
					.append("            CASE WHEN m.tp_manifesto_entrega IS NOT NULL THEN 'E' ELSE 'V' END,       ")
					.append("            ds.vl_mercadoria,                                                         ")
					.append("            ds.id_moeda,                                                              ")
					.append("            ds.id_pais,                                                               ")
					.append("            ds.id_cliente_remetente,                                                  ")
					.append("            ds.id_cliente_destinatario,                                               ")
					.append("            c.id_natureza_produto,                                                    ")
					.append("            s.tp_abrangencia,                                                         ")
					.append("            cc.id_filial_origem,                                                      ")
					.append("            cc.id_filial_destino,                                                     ")
					.append("            NULL,                                                                     ")
					.append("            pmd.id_awb                                                                ")
					.append("        FROM                                                                          ")
					.append("            controle_carga cc,                                                        ")
					.append("            manifesto m,                                                              ")
					.append("            pre_manifesto_documento pmd,                                              ")
					.append("            docto_servico ds,                                                         ")
					.append("            conhecimento c,                                                           ")
					.append("            servico s                                                                 ")
					.append("        WHERE cc.id_controle_carga             = m.id_controle_carga                  ")
					.append("        AND   m.id_manifesto                   = pmd.id_manifesto                     ")
					.append("        AND   pmd.id_docto_servico             = ds.id_docto_servico                  ")
					.append("        AND   ds.id_docto_servico              = c.id_conhecimento                    ")
					.append("        AND   ds.id_servico                    = s.id_servico (+)                     ")
					.append("        AND   cc.id_controle_carga             = :id_controle_carga                   ")
					.append("        AND   m.tp_status_manifesto            NOT IN ('CA', 'DC', 'ED', 'FE')        ")
					.append("        AND   m.dh_emissao_manifesto           IS NULL                                ")
					.append("        AND   c.tp_documento_servico           IN ('CTR', 'CTE', 'NFT', 'NTE')        ");
					
			// LMSA-6078: inserido flag liga/desliga para condicional que ignora o redespacho intermediario
			if (pgr_regra_fedex) {
				stringBuilder.append(" AND C.BL_REDESPACHO_INTERMEDIARIO IS NULL ");						
			}

			// LMSA-6521: LMSA-6545
			stringBuilder
					.append("UNION ALL ")  
					.append("SELECT ")                                                                        
					.append("   'V' as tp_Operacao, ")                                                                      
					.append("    CFX.VL_MERCADORIA, ")                                                         
					.append("    to_number(CFX.tp_moeda) AS id_moeda, ") // real
					.append("    CFX.ID_PAIS id_pais, ") // brasil
					.append("    (SELECT PR.ID_PESSOA ")  
					.append("        FROM PESSOA PR ")
					.append("       WHERE PR.NR_IDENTIFICACAO = CFX.NR_CNPJ_REMETENTE ) AS id_cliente_remetente, ") 
					.append("    (SELECT PD.ID_PESSOA ")
					.append("        FROM PESSOA PD ")
					.append("       WHERE PD.NR_IDENTIFICACAO = CFX.NR_CNPJ_DESTINATARIO  ) AS id_cliente_destinatario, ") 
					.append("    (SELECT NP.ID_NATUREZA_PRODUTO ") 
					.append("        FROM NATUREZA_PRODUTO NP ") 
					.append("       WHERE UPPER(REPLACE(REPLACE(NP.DS_NATUREZA_PRODUTO_I, 'pt_BR»',''), '¦','')) = UPPER(CFX.DS_NATUREZA_PRODUTO) ) AS id_natureza_produto, ")
					.append("    'N' AS tp_abrangencia, ") // nacional 
					.append("    cc.id_filial_origem, ")
					.append("    cc.id_filial_destino, ") 
					.append("    NULL as tp_pedido_coleta, ")                                                                     
					.append("    NULL as id_awb ") 
					.append("FROM ")                                                                           
					.append("  controle_carga cc, ")                                                        
					.append("  SOLICITACAO_CONTRATACAO SC, ")
					.append("  CONHECIMENTO_FEDEX CFX ")                                            
					.append("WHERE cc.id_controle_carga             = :id_controle_carga ")
					.append("AND   SC.ID_SOLICITACAO_CONTRATACAO = CC.ID_SOLICITACAO_CONTRATACAO ")
					.append("AND   SC.TP_CARGA_COMPARTILHADA = 'C2' ")
					.append("AND   CFX.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA ")
					.append("AND   NVL(CFX.BL_CTE_DESCARREGADO, 'N') = 'N' ");
			
			stringBuilder
					.append(" UNION ALL ")
					.append(" SELECT distinct ")                                                                        
					.append(" CASE WHEN m.tp_manifesto_entrega IS NOT NULL THEN 'E' ELSE 'V' END as tp_Operacao, ")       
					.append(" ds.vl_mercadoria, ")                                                         
					.append(" ds.id_moeda, ")                                                              
					.append(" ds.id_pais, ")                                                               
					.append(" ds.id_cliente_remetente, ")                                                  
					.append(" ds.id_cliente_destinatario, ")                                               
					.append(" c.id_natureza_produto, ")                                                    
					.append(" s.tp_abrangencia, ")                                                         
					.append(" cc.id_filial_origem, ")                                                      
					.append(" cc.id_filial_destino, ")                                                     
					.append(" NULL AS tp_pedido_coleta, ")                                                                     
					.append(" null as id_awb ")                                                                
					.append(" FROM controle_carga cc, ")                                                        
					.append(" manifesto m, pre_manifesto_volume pmv, ")                                             
					.append(" docto_servico ds, conhecimento c, servico s ")                                                                 
					.append(" WHERE 1=1 ")
					.append(" and cc.id_controle_carga             = m.id_controle_carga ")
					.append(" and not exists (select 1 from pre_manifesto_documento pmd2 where pmd2.id_manifesto = m.id_manifesto) ")
					.append(" and pmv.id_manifesto = m.id_manifesto ")
					.append(" and pmv.tp_scan = 'SF' ")
					.append(" and ds.id_docto_servico = pmv.id_docto_servico ")               
					.append(" AND ds.id_docto_servico              = c.id_conhecimento ")                    
					.append(" AND ds.id_servico                    = s.id_servico ")                    
					.append(" AND cc.id_controle_carga             = :id_controle_carga ")                   
					.append(" AND m.tp_status_manifesto            in ('CC', 'EC') ")      
					.append(" AND m.dh_emissao_manifesto           IS NULL ")                                
					.append(" AND c.tp_documento_servico           IN ('CTR', 'CTE', 'NFT', 'NTE') ");
			// LMSA-6078: inserido flag liga/desliga para condicional que ignora o redespacho intermediario
			if (pgr_regra_fedex) {
				stringBuilder.append(" AND C.BL_REDESPACHO_INTERMEDIARIO IS NULL ");						
			}
				
			
			stringBuilder
					.append("    ) d,                                                                              ")
					.append("    pessoa p_r,                                                                       ")
					.append("    pessoa p_d,                                                                       ")
					.append("    natureza_produto np,                                                              ")
					.append("    filial f_o,                                                                       ")
					.append("    pessoa p_o,                                                                       ")
					.append("    endereco_pessoa ep_o,                                                             ")
					.append("    municipio m_o,                                                                    ")
					.append("    unidade_federativa uf_o,                                                          ")
					.append("    pais pa_o,                                                                        ")
					.append("    filial f_d,                                                                       ")
					.append("    pessoa p2_d,                                                                      ")
					.append("    endereco_pessoa ep_d,                                                             ")
					.append("    municipio m_d,                                                                    ")
					.append("    unidade_federativa uf_d,                                                          ")
					.append("    pais pa_d                                                                         ")
					.append("WHERE d.id_cliente_remetente                   = p_r.id_pessoa                        ")
					.append("AND   d.id_cliente_destinatario                = p_d.id_pessoa (+)                    ")
					.append("AND   d.id_natureza_produto                    = np.id_natureza_produto (+)           ")
					.append("AND   d.id_filial_origem                       = f_o.id_filial                        ")
					.append("AND   f_o.id_filial                            = p_o.id_pessoa                        ")
					.append("AND   p_o.id_endereco_pessoa                   = ep_o.id_endereco_pessoa              ")
					.append("AND   ep_o.id_municipio                        = m_o.id_municipio                     ")
					.append("AND   m_o.id_unidade_federativa                = uf_o.id_unidade_federativa           ")
					.append("AND   uf_o.id_pais                             = pa_o.id_pais                         ")
					.append("AND   d.id_filial_destino                      = f_d.id_filial (+)                    ")
					.append("AND   f_d.id_filial                            = p2_d.id_pessoa (+)                   ")
					.append("AND   p2_d.id_endereco_pessoa                  = ep_d.id_endereco_pessoa (+)          ")
					.append("AND   ep_d.id_municipio                        = m_d.id_municipio (+)                 ")
					.append("AND   m_d.id_unidade_federativa                = uf_d.id_unidade_federativa (+)       ")
					.append("AND   uf_d.id_pais                             = pa_d.id_pais (+)                     ")
					.append("GROUP BY                                                                              ")
					.append("    d.tp_operacao,                                                                    ")
					.append("    d.id_moeda,                                                                       ")
					.append("    d.id_pais,                                                                        ")
					.append("    d.id_cliente_remetente,                                                           ")
					.append("    p_r.nr_identificacao,                                                             ")
					.append("    p_r.nm_pessoa,                                                                    ")
					.append("    d.id_cliente_destinatario,                                                        ")
					.append("    p_d.nr_identificacao,                                                             ")
					.append("    p_d.nm_pessoa,                                                                    ")
					.append("    d.id_natureza_produto,                                                            ")
					.append("    VI18N(np.ds_natureza_produto_i),                                                  ")
					.append("    d.tp_abrangencia,                                                                 ")
					.append("    d.id_filial_origem,                                                               ")
					.append("    f_o.sg_filial,                                                                    ")
					.append("    m_o.id_municipio,                                                                 ")
					.append("    m_o.nm_municipio,                                                                 ")
					.append("    uf_o.id_unidade_federativa,                                                       ")
					.append("    uf_o.sg_unidade_federativa,                                                       ")
					.append("    pa_o.id_pais,                                                                     ")
					.append("    VI18N(pa_o.nm_pais_i),                                                            ")
					.append("    d.id_filial_destino,                                                              ")
					.append("    f_d.sg_filial,                                                                    ")
					.append("    m_d.id_municipio,                                                                 ")
					.append("    m_d.nm_municipio,                                                                 ")
					.append("    uf_d.id_unidade_federativa,                                                       ")
					.append("    uf_d.sg_unidade_federativa,                                                       ")
					.append("    pa_d.id_pais,                                                                     ")
					.append("    VI18N(pa_d.nm_pais_i),                                                            ")
					.append("    d.tp_pedido_coleta,                                                               ")
					.append("    d.id_awb                                                                          ");
			doctoServicoQuery = stringBuilder.toString().replaceAll("\\s{2,}", " ");
		}
		return doctoServicoQuery;
	}

	/**
	 * Recuperar documentos de serviço onde existir CT-e de despacho intermediario (LMSA-6078)
	 * @param pgr_regra_fedex
	 * @return
	 * @author ernani.brandao
	 */
	private String getDoctoServicoCTeRedespachoIntermediarioQuery() {
	    if (doctoServicoCTeRedespachoIntermediarioQuery == null) {
    		StringBuilder stringBuilder = new StringBuilder()
    				.append("SELECT SUM(vl_mercadoria) AS vl_mercadoria, ")
    				.append("       tipoOperacao as tp_operacao, ")
    				.append("       documentos.id_moeda, ")
    				.append("       documentos.id_pais, ")
    				.append("       id_cliente_remetente, ")
    				.append("       pRemetente.Nr_Identificacao AS nr_identificacao_remetente, ")
    				.append("       pRemetente.Nm_Pessoa        AS nm_pessoa_remetente, ")
    				.append("       id_cliente_destinatario, ")
    				.append("       pDestinatario.Nr_Identificacao AS nr_identificacao_destinatario, ")
    				.append("       pDestinatario.Nm_Pessoa        AS nm_pessoa_destinatario, ")
    				.append("       np.id_natureza_produto, ")
    				.append("       np.ds_natureza_produto_i AS ds_natureza_produto, ")
    				.append("       tp_abrangencia, ")
    				.append("       id_filial_origem, ")
    				.append("       fOrigem.Sg_Filial              AS sg_filial_origem, ")
    				.append("       mOrigem.Id_Municipio           AS id_municipio_origem, ")
    				.append("       mOrigem.Nm_Municipio           AS nm_municipio_origem, ")
    				.append("       ufOrigem.Id_Unidade_Federativa AS id_unidade_federativa_origem, ")
    				.append("       ufOrigem.Sg_Unidade_Federativa AS sg_unidade_federativa_origem, ")
    				.append("       pOrigem.Id_Pais                AS id_pais_origem, ")
    				.append("       pOrigem.Nm_Pais_i              AS nm_pais_origem, ")
    				.append("       id_filial_destino, ")
    				.append("       fdestino.Sg_Filial              AS sg_filial_destino, ")
    				.append("       mDestino.Id_Municipio           AS id_municipio_destino, ")
    				.append("       mDestino.Nm_Municipio           AS nm_municipio_destino, ")
    				.append("       ufDestino.Id_Unidade_Federativa AS id_unidade_federativa_destino, ")
    				.append("       ufDestino.Sg_Unidade_Federativa AS sg_unidade_federativa_destino, ")
    				.append("       pDestino.Id_Pais                AS id_pais_destino, ")
    				.append("       pDestino.Nm_Pais_i              AS nm_pais_destino, ")
    				.append("       tp_pedido_coleta, ")
    				.append("       id_awb ")
    				.append("  FROM (  ")
    				.append("       SELECT DISTINCT 'E' AS tipoOperacao, ") // --Entrega CT-e FedEx
    				.append("              NFC.VL_TOTAL AS vl_mercadoria, ")
    				.append("              ds.id_moeda, ")
    				.append("              ds.id_pais, ")
    				.append("              (SELECT p.id_pessoa ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      PESSOA P ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
    				.append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO          = '").append(CampoNotaFiscalEdiComplementoFedex.CNPJ_REMETENTE_CTE_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND P.NR_IDENTIFICACAO    = DC.DS_VALOR_CAMPO ")
    				.append("              ) AS id_cliente_remetente, ")
    				.append("              (SELECT p.id_pessoa ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      PESSOA P ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO            = '").append(CampoNotaFiscalEdiComplementoFedex.CNPJ_DESTINATARIO_CTE_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND P.NR_IDENTIFICACAO      = DC.DS_VALOR_CAMPO ")
    				.append("              ) AS id_cliente_destinatario, ")
    				.append("              (SELECT NP.ID_NATUREZA_PRODUTO ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      NATUREZA_PRODUTO NP ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO            = '").append(CampoNotaFiscalEdiComplementoFedex.NATUREZA_PRODUTO_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND UPPER(REPLACE(REPLACE(NP.DS_NATUREZA_PRODUTO_I, 'pt_BR»',''), '¦','')) = UPPER(DC.DS_VALOR_CAMPO) ")
    				.append("              ) AS id_natureza_produto, ")
    				.append("              s.tp_abrangencia, ")
    				.append("              cc.id_filial_origem, ")
    				.append("              cc.id_filial_destino, ")
    				.append("              NULL AS tp_pedido_coleta, ")
    				.append("              med.id_awb ")
    				.append("         FROM controle_carga cc ")
    				.append("        INNER JOIN manifesto m ON m.id_controle_carga = cc.id_controle_carga ")
    				.append("        INNER JOIN manifesto_entrega me ON me.id_manifesto_entrega = m.id_manifesto ")
    				.append("        INNER JOIN manifesto_entrega_documento med ON med.id_manifesto_entrega = me.id_manifesto_entrega ")
    				.append("        INNER JOIN docto_servico ds ON ds.id_docto_servico = med.id_docto_servico ")
    				.append("         LEFT JOIN conhecimento c ON c.id_conhecimento = ds.id_docto_servico ")
    				.append("        INNER JOIN NOTA_FISCAL_CONHECIMENTO NFC ON NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
    				.append("         LEFT JOIN servico s ON ds.id_servico = s.id_servico ")
    				.append("        WHERE cc.id_controle_carga = :id_controle_carga ")
    				.append("          AND ds.tp_documento_servico IN ('CTR', 'CRT', 'NFT', 'CTE', 'NTE') ")
    				.append("          AND m.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE') ")
    				.append("          AND NOT EXISTS (SELECT 1 ")
    				.append("                            FROM evento_documento_servico eds ")
    				.append("                           INNER JOIN evento e ON e.id_evento = eds.id_evento ")
    				.append("                           WHERE e.cd_evento             = 21 ")
    				.append("                             AND eds.bl_evento_cancelado = 'N' ")
    				.append("                             AND eds.id_docto_servico    = ds.id_docto_servico ")
    				.append("                         ) ")
    				.append("          AND C.BL_REDESPACHO_INTERMEDIARIO = 'S' ")
    				.append("        UNION ALL ")
    				.append("       SELECT DISTINCT 'V'  AS tipoOperacao, ") // -- Viagem CT-e FedEx 
    				.append("              NFC.VL_TOTAL  AS vl_mercadoria, ")
    				.append("              ds.id_moeda, ")
    				.append("              ds.id_pais, ")
    				.append("              (SELECT p.id_pessoa  ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      PESSOA P ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO           = '").append(CampoNotaFiscalEdiComplementoFedex.CNPJ_REMETENTE_CTE_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND P.NR_IDENTIFICACAO     = DC.DS_VALOR_CAMPO ")
    				.append("              ) AS id_cliente_remetente, ")
    				.append("              (SELECT p.id_pessoa ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      PESSOA P ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO             = '").append(CampoNotaFiscalEdiComplementoFedex.CNPJ_DESTINATARIO_CTE_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND P.NR_IDENTIFICACAO       = DC.DS_VALOR_CAMPO ")
    				.append("              ) AS id_cliente_destinatario, ")
    				.append("              (SELECT NP.ID_NATUREZA_PRODUTO ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      NATUREZA_PRODUTO NP ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO            = '").append(CampoNotaFiscalEdiComplementoFedex.NATUREZA_PRODUTO_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND UPPER(REPLACE(REPLACE(NP.DS_NATUREZA_PRODUTO_I, 'pt_BR»',''), '¦','')) = UPPER(DC.DS_VALOR_CAMPO) ")
    				.append("              ) AS id_natureza_produto, ")
    				.append("              s.tp_abrangencia, ")
    				.append("              cc.id_filial_origem, ")
    				.append("              cc.id_filial_destino, ")
    				.append("              NULL AS tp_pedido_coleta, ")
    				.append("              NULL AS id_awb ")
    				.append("         FROM controle_carga cc ")
    				.append("        INNER JOIN manifesto m ON m.id_controle_carga = cc.id_controle_carga ")
    				.append("        INNER JOIN manifesto_viagem_nacional mvn ON mvn.id_manifesto_viagem_nacional = m.id_manifesto ")
    				.append("        INNER JOIN manifesto_nacional_cto mnc ON mnc.id_manifesto_viagem_nacional = mvn.id_manifesto_viagem_nacional ")
    				.append("        INNER JOIN conhecimento c ON c.id_conhecimento = mnc.id_conhecimento ")
    				.append("        INNER JOIN docto_servico ds ON ds.id_docto_servico = c.id_conhecimento ")
    				.append("        INNER JOIN DADOS_COMPLEMENTO DC ON dc.id_conhecimento = ds.id_docto_servico ")
    				.append("        INNER JOIN NOTA_FISCAL_CONHECIMENTO NFC ON NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
    				.append("         LEFT OUTER JOIN servico s ON s.id_servico = ds.id_servico ")
    				.append("        WHERE cc.id_controle_carga = :id_controle_carga ")
    				.append("          AND m.tp_status_manifesto NOT    IN ('CA', 'DC', 'ED', 'FE', 'PM') ")
    				.append("          AND c.tp_documento_servico       IN ('CTR', 'CTE', 'NFT', 'NTE') ")
    				.append("          AND c.bl_redespacho_intermediario = 'S' ")
    				.append("        UNION ALL ")
    				.append("       SELECT DISTINCT 'V' AS tipoOperacao, ") // -- Viagem e entrega manifesto não emitido 
    				.append("              NFC.VL_TOTAL      AS vl_mercadoria, ")
    				.append("              ds.id_moeda, ")
    				.append("              ds.id_pais, ")
    				.append("              (SELECT p.id_pessoa ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      PESSOA P ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO             = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO           = '").append(CampoNotaFiscalEdiComplementoFedex.CNPJ_REMETENTE_CTE_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND P.NR_IDENTIFICACAO     = DC.DS_VALOR_CAMPO ")
    				.append("              ) AS id_cliente_remetente, ")
    				.append("              (SELECT p.id_pessoa ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      PESSOA P ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO            = '").append(CampoNotaFiscalEdiComplementoFedex.CNPJ_DESTINATARIO_CTE_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND P.NR_IDENTIFICACAO      = DC.DS_VALOR_CAMPO ")
    				.append("              ) AS id_cliente_destinatario, ")
    				.append("              (SELECT NP.ID_NATUREZA_PRODUTO ")
    				.append("                 FROM DADOS_COMPLEMENTO DC, NF_DADOS_COMP NDC, ")
    				.append("                      INFORMACAO_DOCTO_CLIENTE IDC , ")
    				.append("                      NATUREZA_PRODUTO NP ")
    				.append("                WHERE DC.ID_INFORMACAO_DOCTO_CLIENTE = IDC.ID_INFORMACAO_DOCTO_CLIENTE ")
    				.append("                  AND DC.ID_DADOS_COMPLEMENTO          = NDC.ID_DADOS_COMPLEMENTO ")
                    .append("                  AND NDC.ID_NOTA_FISCAL_CONHECIMENTO  = NFC.ID_NOTA_FISCAL_CONHECIMENTO ")
    				.append("                  AND DC.ID_CONHECIMENTO               = DS.ID_DOCTO_SERVICO ")
    				.append("                  AND IDC.DS_CAMPO             = '").append(CampoNotaFiscalEdiComplementoFedex.NATUREZA_PRODUTO_FEDEX.getNomeCampo()).append("' ")
    				.append("                  AND UPPER(REPLACE(REPLACE(NP.DS_NATUREZA_PRODUTO_I, 'pt_BR»',''), '¦','')) = UPPER(DC.DS_VALOR_CAMPO) ")
    				.append("              ) AS id_natureza_produto, ")
    				.append("              s.tp_abrangencia, ")
    				.append("              cc.id_filial_origem, ")
    				.append("              cc.id_filial_destino, ")
    				.append("              NULL AS tp_pedido_coleta, ")
    				.append("              NULL AS id_awb ")
    				.append("         FROM controle_carga cc ")
    				.append("        INNER JOIN manifesto m ON m.id_controle_carga = cc.id_controle_carga ")
    				.append("        INNER JOIN pre_manifesto_documento pmd ON pmd.id_manifesto = m.id_manifesto ")
                    .append("        INNER JOIN conhecimento c ON c.id_conhecimento = pmd.id_docto_servico ")
    				.append("        INNER JOIN docto_servico ds ON ds.id_docto_servico = c.id_conhecimento ")
    				.append("        INNER JOIN DADOS_COMPLEMENTO DC ON dc.id_conhecimento = ds.id_docto_servico ")
    				.append("        INNER JOIN NOTA_FISCAL_CONHECIMENTO NFC ON NFC.ID_CONHECIMENTO = DS.ID_DOCTO_SERVICO ")
    				.append("         LEFT OUTER JOIN servico s ON s.id_servico = ds.id_servico ")
    				.append("        WHERE cc.id_controle_carga = :id_controle_carga ")
    				.append("          AND m.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM') ")
    				.append("          AND c.tp_documento_servico    IN ('CTR', 'CTE', 'NFT', 'NTE') ")
    				.append("          AND c.bl_redespacho_intermediario = 'S' ")
    				.append("          AND m.dh_emissao_manifesto       IS NULL ")

    				// LMSA-6521: LMSA-6545
//    				.append("  UNION ALL ")
//    				.append("  SELECT DISTINCT 'V'  AS tipoOperacao, ") // Carga Compartilhada 2º Carregamento TNT
//    				.append("  CFX.VL_MERCADORIA AS vl_mercadoria, ") 
//    				.append("  to_number(CFX.tp_moeda) as id_moeda, ") // real
//    				.append("  CFX.ID_PAIS as id_pais, ") // brasil
//    				.append("  (SELECT PR.ID_PESSOA ")  
//    				.append("     FROM PESSOA PR ")
//    				.append("    WHERE PR.NR_IDENTIFICACAO = CFX.NR_CNPJ_REMETENTE ) AS id_cliente_remetente, ") 
//    				.append("  (SELECT PD.ID_PESSOA ")
//    				.append("     FROM PESSOA PD ")
//    				.append("    WHERE PD.NR_IDENTIFICACAO = CFX.NR_CNPJ_DESTINATARIO  ) AS id_cliente_destinatario,") 
//    				.append("  (SELECT NP.ID_NATUREZA_PRODUTO ") 
//    				.append("     FROM NATUREZA_PRODUTO NP ") 
//    				.append("    WHERE UPPER(REPLACE(REPLACE(NP.DS_NATUREZA_PRODUTO_I, 'pt_BR»',''), '¦','')) = UPPER(CFX.DS_NATUREZA_PRODUTO) ) AS id_natureza_produto, ") 
//    				.append("  'N' AS tp_abrangencia, ") // nacional
//    				.append("  cc.id_filial_origem, ") 
//    				.append("  cc.id_filial_destino, ") 
//    				.append("  NULL AS tp_pedido_coleta, ") 
//    				.append("  NULL AS id_awb ") 
//    				.append("  FROM controle_carga cc ") 
//    				.append("  INNER JOIN manifesto m ON m.id_controle_carga = cc.id_controle_carga ") 
//    				.append("  INNER JOIN manifesto_viagem_nacional mvn ON mvn.id_manifesto_viagem_nacional = m.id_manifesto ") 
//    				.append("  INNER JOIN SOLICITACAO_CONTRATACAO SC ON SC.ID_SOLICITACAO_CONTRATACAO = CC.ID_SOLICITACAO_CONTRATACAO ")
//    				.append("  AND SC.TP_CARGA_COMPARTILHADA = 'C2' ")
//    				.append("  INNER JOIN CONHECIMENTO_FEDEX CFX ON CFX.ID_CONTROLE_CARGA = CC.ID_CONTROLE_CARGA AND NVL(CFX.BL_CTE_DESCARREGADO, 'N') = 'N' ")
//    				.append("  WHERE cc.id_controle_carga = :id_controle_carga ")
//    				.append("  AND m.tp_status_manifesto NOT IN ('CA', 'DC', 'ED', 'FE', 'PM') ")

    				.append(" UNION ALL ")
    				.append(" SELECT distinct ")                                                                        
    				.append(" CASE WHEN m.tp_manifesto_entrega IS NOT NULL THEN 'E' ELSE 'V' END AS tipoOperacao, ")       
    				.append(" ds.vl_mercadoria, ")                                                         
    				.append(" ds.id_moeda, ")                                                              
    				.append(" ds.id_pais, ")                                                               
    				.append(" ds.id_cliente_remetente, ")                                                  
    				.append(" ds.id_cliente_destinatario, ")                                               
    				.append(" c.id_natureza_produto, ")                                                    
    				.append(" s.tp_abrangencia, ")                                                         
    				.append(" cc.id_filial_origem, ")                                                      
    				.append(" cc.id_filial_destino, ")                                                     
    				.append(" NULL AS tp_pedido_coleta, ")                                                                     
    				.append(" null as id_awb ")       
    				.append(" FROM ")                                                                          
    				.append(" controle_carga cc, ")                                                        
    				.append(" manifesto m, ")           
    				.append(" pre_manifesto_volume pmv, ")                                             
    				.append(" docto_servico ds, ")                                                         
    				.append(" conhecimento c, ")                                                           
    				.append(" servico s ")  
    				.append(" WHERE 1=1 ")
    				.append(" and cc.id_controle_carga             = m.id_controle_carga ")
    				.append(" and not exists (select 1 from pre_manifesto_documento pmd2 where pmd2.id_manifesto = m.id_manifesto) ")
    				.append(" and pmv.id_manifesto = m.id_manifesto ")
    				.append(" and pmv.tp_scan = 'SF' ")
    				.append(" and ds.id_docto_servico = pmv.id_docto_servico ")               
    				.append(" AND ds.id_docto_servico              = c.id_conhecimento ")                    
    				.append(" AND ds.id_servico                    = s.id_servico ")                    
    				.append(" AND cc.id_controle_carga             = :id_controle_carga ")                   
    				.append(" AND m.tp_status_manifesto            in ('CC', 'EC') ")      
    				.append(" AND m.dh_emissao_manifesto           IS NULL ")                                
    				.append(" AND c.tp_documento_servico           IN ('CTR', 'CTE', 'NFT', 'NTE') ")
    				.append(" AND c.bl_redespacho_intermediario = 'S' ") 
    				
    				.append("       ) documentos ")
    				.append("  LEFT JOIN pessoa pRemetente ON pRemetente.Id_Pessoa = documentos.id_cliente_remetente ")
    				.append("  LEFT JOIN pessoa pDestinatario ON pDestinatario.Id_Pessoa = documentos.id_cliente_destinatario ")
    				.append("  LEFT JOIN natureza_produto np ON np.id_natureza_produto = documentos.id_natureza_produto ")
    				.append(" INNER JOIN filial forigem ON forigem.id_filial = documentos.id_filial_origem ")
    				.append(" INNER JOIN pessoa pessoaOrigem ON pessoaOrigem.id_pessoa = forigem.id_filial ")
    				.append(" INNER JOIN endereco_pessoa epOrigem ON epOrigem.id_endereco_pessoa = pessoaOrigem.id_endereco_pessoa ")
    				.append(" INNER JOIN municipio mOrigem ON mOrigem.id_municipio = epOrigem.id_municipio ")
    				.append(" INNER JOIN unidade_federativa ufOrigem ON ufOrigem.id_unidade_federativa = mOrigem.id_unidade_federativa ")
    				.append(" INNER JOIN pais pOrigem ON pOrigem.id_pais = ufOrigem.id_pais ")
    				.append("  LEFT JOIN filial fDestino ON fDestino.id_filial = documentos.id_filial_destino ")
    				.append("  LEFT JOIN pessoa pessoaDestino ON pessoaDestino.id_pessoa = fDestino.id_filial ")
    				.append("  LEFT JOIN endereco_pessoa epDestino ON epDestino.id_endereco_pessoa = pessoaDestino.id_endereco_pessoa ")
    				.append("  LEFT JOIN municipio mDestino ON mDestino.id_municipio = epDestino.id_municipio ")
    				.append("  LEFT JOIN unidade_federativa ufDestino ON ufDestino.id_unidade_federativa = mDestino.id_unidade_federativa ")
    				.append("  LEFT JOIN pais pDestino ON pDestino.id_pais = ufDestino.id_pais ")
    				.append(" GROUP BY tipoOperacao, ")
    				.append("          documentos.id_moeda, ")
    				.append("        documentos.id_pais, ")
    				.append("        id_cliente_remetente, ")
    				.append("        pRemetente.Nr_Identificacao, ")
    				.append("        pRemetente.Nm_Pessoa, ")
    				.append("        id_cliente_destinatario, ")
    				.append("        pDestinatario.Nr_Identificacao, ")
    				.append("        pDestinatario.Nm_Pessoa, ")
    				.append("        np.id_natureza_produto, ")
    				.append("        np.ds_natureza_produto_i, ")
    				.append("        tp_abrangencia, ")
    				.append("        id_filial_origem, ")
    				.append("        fOrigem.Sg_Filial, ")
    				.append("        mOrigem.Id_Municipio, ")
    				.append("        mOrigem.Nm_Municipio, ")
    				.append("        ufOrigem.Id_Unidade_Federativa, ")
    				.append("        ufOrigem.Sg_Unidade_Federativa, ")
    				.append("        pOrigem.Id_Pais, ")
    				.append("        pOrigem.Nm_Pais_i, ")
    				.append("        id_filial_destino , ")
    				.append("        fdestino.Sg_Filial, ")
    				.append("        mDestino.Id_Municipio, ")
    				.append("        mDestino.Nm_Municipio, ")
    				.append("        ufDestino.Id_Unidade_Federativa, ")
    				.append("        ufDestino.Sg_Unidade_Federativa, ")
    				.append("        pDestino.Id_Pais, ")
    				.append("        pDestino.Nm_Pais_i, ")
    				.append("        tp_pedido_coleta, ")
    				.append("        id_awb ");
    		doctoServicoCTeRedespachoIntermediarioQuery = stringBuilder.toString().replaceAll("\\s{2,}", " ");
	    }
		return doctoServicoCTeRedespachoIntermediarioQuery;
	}
	
	
	/**
	 * Prepara o {@link ConfigureSqlQuery} para a busca de {@link DoctoServico}s
	 * associados a um {@link ControleCarga}.
	 * 
	 * @return {@link ConfigureSqlQuery} para a busca de {@link DoctoServico}s
	 *         associados a um {@link ControleCarga}.
	 */
	private ConfigureSqlQuery getDoctoServicoConfig() {
		if (doctoServicoConfig == null) {
			doctoServicoConfig = new ConfigureSqlQuery() {
				@Override
				public void configQuery(SQLQuery sqlQuery) {
					sqlQuery.addScalar("vl_mercadoria", Hibernate.BIG_DECIMAL);
					sqlQuery.addScalar("tp_operacao", Hibernate.STRING);
					sqlQuery.addScalar("id_moeda", Hibernate.LONG);
					sqlQuery.addScalar("id_pais", Hibernate.LONG);
					sqlQuery.addScalar("id_cliente_remetente", Hibernate.LONG);
					sqlQuery.addScalar("nr_identificacao_remetente", Hibernate.STRING);
					sqlQuery.addScalar("nm_pessoa_remetente", Hibernate.STRING);
					sqlQuery.addScalar("id_cliente_destinatario", Hibernate.LONG);
					sqlQuery.addScalar("nr_identificacao_destinatario", Hibernate.STRING);
					sqlQuery.addScalar("nm_pessoa_destinatario", Hibernate.STRING);
					sqlQuery.addScalar("id_natureza_produto", Hibernate.LONG);
					sqlQuery.addScalar("ds_natureza_produto", Hibernate.STRING);
					sqlQuery.addScalar("tp_abrangencia", Hibernate.STRING);
					sqlQuery.addScalar("id_filial_origem", Hibernate.LONG);
					sqlQuery.addScalar("sg_filial_origem", Hibernate.STRING);
					sqlQuery.addScalar("id_municipio_origem", Hibernate.LONG);
					sqlQuery.addScalar("nm_municipio_origem", Hibernate.STRING);
					sqlQuery.addScalar("id_unidade_federativa_origem", Hibernate.LONG);
					sqlQuery.addScalar("sg_unidade_federativa_origem", Hibernate.STRING);
					sqlQuery.addScalar("id_pais_origem", Hibernate.LONG);
					sqlQuery.addScalar("nm_pais_origem", Hibernate.STRING);
					sqlQuery.addScalar("id_filial_destino", Hibernate.LONG);
					sqlQuery.addScalar("sg_filial_destino", Hibernate.STRING);
					sqlQuery.addScalar("id_municipio_destino", Hibernate.LONG);
					sqlQuery.addScalar("nm_municipio_destino", Hibernate.STRING);
					sqlQuery.addScalar("id_unidade_federativa_destino", Hibernate.LONG);
					sqlQuery.addScalar("sg_unidade_federativa_destino", Hibernate.STRING);
					sqlQuery.addScalar("id_pais_destino", Hibernate.LONG);
					sqlQuery.addScalar("nm_pais_destino", Hibernate.STRING);
					sqlQuery.addScalar("tp_pedido_coleta", Hibernate.STRING);
					sqlQuery.addScalar("id_awb", Hibernate.LONG);
				}
			};
		}
		return doctoServicoConfig;
	}

	/*
	 * Enquadramentos de Regras
	 */

	/**
	 * Busca {@link EnquadramentoRegra}s vigentes (entre
	 * {@code DT_VIGENCIA_INICIAL} e {@code DT_VIGENCIA_FINAL}) e do tipo
	 * "regra geral" ou não, conforme parâmetro. Os atributos de relacionamento
	 * tipo many-to-one {@code moeda}, {@code naturezaProduto},
	 * {@code apoliceSeguro} e {@code seguroCliente} são carregados
	 * imediatamente. A carga de atributos de relacionamentos many-to-one e
	 * many-to-many é garantida, resolvendo restrições do mapeamento
	 * objeto-relacional.
	 * 
	 * @param blRegraGeral
	 *            Especifica busca de {@link EnquadramentoRegra} tipo
	 *            "regra geral" ou não.
	 * @return {@link EnquadramentoRegra}s vigentes e do tipo "regra geral" ou
	 *         não, conforme parâmetro.
	 */
	public List<EnquadramentoRegra> findEnquadramentoRegra(Boolean blRegraGeral) {
		StringBuilder hql = new StringBuilder()
				.append("FROM EnquadramentoRegra er ")
				.append("JOIN FETCH er.moeda ")
				.append("LEFT JOIN FETCH er.naturezaProduto ")
				.append("LEFT JOIN FETCH er.apoliceSeguro ")
				.append("LEFT JOIN FETCH er.seguroCliente ")
				.append("WHERE SYSDATE BETWEEN er.dtVigenciaInicial AND NVL(er.dtVigenciaFinal, SYSDATE) ")
				.append("AND er.blRegraGeral = :blRegraGeral");
		@SuppressWarnings("unchecked")
		List<EnquadramentoRegra> regras = getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "blRegraGeral", blRegraGeral);

		List<Long> ids = new ArrayList<Long>(regras.size());
		for (EnquadramentoRegra regra : regras) {
			Long idEnquadramentoRegra = regra.getIdEnquadramentoRegra();
			ids.add(idEnquadramentoRegra);

			regra.setMunicipioEnquadramentosOrigem(new ArrayList<Municipio>());
			regra.setMunicipioEnquadramentosDestino(new ArrayList<Municipio>());
			regra.setPaisEnquadramentosOrigem(new ArrayList<Pais>());
			regra.setPaisEnquadramentosDestino(new ArrayList<Pais>());
			regra.setUnidadeFederativaEnquadramentosOrigem(new ArrayList<UnidadeFederativa>());
			regra.setUnidadeFederativaEnquadramentosDestino(new ArrayList<UnidadeFederativa>());
			regra.setFilialEnquadramentosOrigem(new ArrayList<Filial>());
			regra.setFilialEnquadramentosDestino(new ArrayList<Filial>());
			Hibernate.initialize(regra.getClienteEnquadramentos());
			Hibernate.initialize(regra.getFaixaDeValors());
		}
		findMunicipioEnquadramento(ids);
		findPaisEnquadramento(ids);
		findUnidadeFederativaEnquadramento(ids);
		findFilialEnquadramento(ids);
		return regras;
	}

	/**
	 * Busca os {@link MunicipioEnquadramento}s de determinados
	 * {@link EnquadramentoRegra}s e popula os relacionamentos many-to-many
	 * {@code municipioEnquadramentosOrigem} e
	 * {@code municipioEnquadramentosDestino} conforme o atributo
	 * {@code tpInfluenciaMunicipio}.
	 * 
	 * @param ids
	 *            Id's dos {@link EnquadramentoRegra}s a processar.
	 */
	private void findMunicipioEnquadramento(Collection<Long> ids) {
		List<MunicipioEnquadramento> municipios = findCriterioEnquadramento(ids, MunicipioEnquadramento.class, "municipio");
		for (MunicipioEnquadramento enquadramento : municipios) {
			Municipio municipio = enquadramento.getMunicipio();
			EnquadramentoRegra regra = enquadramento.getEnquadramentoRegra();
			String tpCriterio = enquadramento.getTpInfluenciaMunicipio().getValue();
			if (TP_CRITERIO_ORIGEM.equals(tpCriterio)) {
				regra.getMunicipioEnquadramentosOrigem().add(municipio);
			} else {
				regra.getMunicipioEnquadramentosDestino().add(municipio);
			}
		}
	}

	/**
	 * Busca os {@link PaisEnquadramento}s de determinados
	 * {@link EnquadramentoRegra}s e popula os relacionamentos many-to-many
	 * {@code paisEnquadramentosOrigem} e {@code paisEnquadramentosDestino}
	 * conforme o atributo {@code tpInfluenciaMunicipio}.
	 * 
	 * @param ids
	 *            Id's dos {@link EnquadramentoRegra}s a processar.
	 */
	private void findPaisEnquadramento(Collection<Long> ids) {
		List<PaisEnquadramento> paises = findCriterioEnquadramento(ids, PaisEnquadramento.class, "pais");
		for (PaisEnquadramento enquadramento : paises) {
			Pais pais = enquadramento.getPais();
			EnquadramentoRegra regra = enquadramento.getEnquadramentoRegra();
			String tpCriterio = enquadramento.getTpInfluenciaMunicipio().getValue();
			if (TP_CRITERIO_ORIGEM.equals(tpCriterio)) {
				regra.getPaisEnquadramentosOrigem().add(pais);
			} else {
				regra.getPaisEnquadramentosDestino().add(pais);
			}
		}
	}

	/**
	 * Busca os {@link UnidadeFederativaEnquadramento}s de determinados
	 * {@link EnquadramentoRegra}s e popula os relacionamentos many-to-many
	 * {@code unidadeFederativaEnquadramentosOrigem} e
	 * {@code unidadeFederativaEnquadramentosDestino} conforme o atributo
	 * {@code tpInfluenciaMunicipio}.
	 * 
	 * @param ids
	 *            Id's dos {@link EnquadramentoRegra}s a processar.
	 */
	private void findUnidadeFederativaEnquadramento(Collection<Long> ids) {
		List<UnidadeFederativaEnquadramento> ufs = findCriterioEnquadramento(ids, UnidadeFederativaEnquadramento.class, "unidadeFederativa");
		for (UnidadeFederativaEnquadramento enquadramento : ufs) {
			UnidadeFederativa uf = enquadramento.getUnidadeFederativa();
			EnquadramentoRegra regra = enquadramento.getEnquadramentoRegra();
			String tpCriterio = enquadramento.getTpInfluenciaMunicipio().getValue();
			if (TP_CRITERIO_ORIGEM.equals(tpCriterio)) {
				regra.getUnidadeFederativaEnquadramentosOrigem().add(uf);
			} else {
				regra.getUnidadeFederativaEnquadramentosDestino().add(uf);
			}
		}
	}

	/**
	 * Busca os {@link FilialEnquadramento}s de determinados
	 * {@link EnquadramentoRegra}s e popula os relacionamentos many-to-many
	 * {@code filialEnquadramentosOrigem} e {@code filialEnquadramentosDestino}
	 * conforme o atributo {@code tpInfluenciaMunicipio}.
	 * 
	 * @param ids
	 *            Id's dos {@link EnquadramentoRegra}s a processar.
	 */
	private void findFilialEnquadramento(Collection<Long> ids) {
		List<FilialEnquadramento> filiais = findCriterioEnquadramento(ids, FilialEnquadramento.class, "filial");
		for (FilialEnquadramento enquadramento : filiais) {
			Filial filial = enquadramento.getFilial();
			EnquadramentoRegra regra = enquadramento.getEnquadramentoRegra();
			String tpCriterio = enquadramento.getTpInfluenciaMunicipio().getValue();
			if (TP_CRITERIO_ORIGEM.equals(tpCriterio)) {
				regra.getFilialEnquadramentosOrigem().add(filial);
			} else {
				regra.getFilialEnquadramentosDestino().add(filial);
			}
		}
	}

	/**
	 * Busca entidades do tipo especificado pelo parâmetro {@code entityClass}
	 * de determinados {@link EnquadramentoRegra}s, forçando a carga antecipada
	 * do relacionamento especificado pelo parâmetro {@code fetchProperty}.
	 * 
	 * @param idsEnquadramentoRegra
	 *            Id's dos {@link EnquadramentoRegra}s.
	 * @param entityClass
	 *            {@link Class} da entidade para busca.
	 * @param fetchProperty
	 *            Propriedade para carga antecipada.
	 * @return {@link List} de entidades relacionadas a determinados
	 *         {@link EnquadramentoRegra}s.
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> findCriterioEnquadramento(
			final Collection<Long> idsEnquadramentoRegra, final Class<T> entityClass, final String fetchProperty) {
		if (CollectionUtils.isEmpty(idsEnquadramentoRegra)) {
			return Collections.EMPTY_LIST;
		}
		return getAdsmHibernateTemplate().executeFind(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws SQLException {
				Criteria criteria = session.createCriteria(entityClass);
				criteria.add(Restrictions.in("enquadramentoRegra.idEnquadramentoRegra", idsEnquadramentoRegra));
				criteria.setFetchMode(fetchProperty, FetchMode.JOIN);
				return criteria.list();
			}
		});
	}

	/*
	 * Exigências de Faixas de Valor
	 */

	/**
	 * Busca {@link ExigenciaFaixaValor} relacionadas a determinadas
	 * {@link FaixaDeValor}, forçando a carga antecipada dos relacionamentos com
	 * {@link FaixaDeValor}, {@link EnquadramentoRegra},
	 * {@link ExigenciaGerRisco}, {@link TipoExigenciaGerRisco} e,
	 * opcionalmente, {@link Filial}.
	 * 
	 * @param idsFaixaDeValor
	 *            Id's das {@link FaixaDeValor}.
	 * @return {@link ExigenciaFaixaValor} relacionadas a determinadas
	 *         {@link FaixaDeValor}.
	 */
	@SuppressWarnings("unchecked")
	public List<ExigenciaFaixaValor> findExigenciaFaixaValor(Collection<Long> idsFaixaDeValor) {
		StringBuilder hql = new StringBuilder()
				.append("FROM ExigenciaFaixaValor efv ")
				.append("JOIN FETCH efv.faixaDeValor ")
				.append("JOIN FETCH efv.faixaDeValor.enquadramentoRegra ")
				.append("JOIN FETCH efv.exigenciaGerRisco ")
				.append("JOIN FETCH efv.exigenciaGerRisco.tipoExigenciaGerRisco ")
				.append("LEFT JOIN FETCH efv.filialInicio ")
				.append("WHERE efv.faixaDeValor.idFaixaDeValor IN (:idsFaixaDeValor)");
		return getAdsmHibernateTemplate().findByNamedParam(hql.toString(), "idsFaixaDeValor", idsFaixaDeValor);
	}


	@SuppressWarnings("unchecked")
	public DomainValue findTpSituacaoPendencia(Long idControleCarga, CampoHistoricoWorkflow campoHistoricoWorkflow) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT p.tpSituacaoPendencia FROM ")
		   .append(Pendencia.class.getName()).append(" as p ")
		   .append("   WHERE p.idPendencia = ") 
		   .append("       (SELECT MAX(hw.pendencia.idPendencia) ")
		   .append(" 			   FROM " + HistoricoWorkflow.class.getName()).append(" as hw ")
		   .append("               WHERE hw.idProcesso = ? ")
		   .append("               AND hw.nmTabela = ? ")
		   .append("               AND hw.tpCampoWorkflow = ?) ");
		
		List<DomainValue> result = getAdsmHibernateTemplate().find(
				sql.toString(),
				new Object[] { idControleCarga, 
							   TabelaHistoricoWorkflow.CONTROLE_CARGA.toString(),
							   campoHistoricoWorkflow.toString() });
		
		return result != null && !result.isEmpty() ? (DomainValue) result.get(0) : null;
	}

}