package com.mercurio.lms.seguros.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração da carta de Emissao de Ocorrencia.
 * Especificação técnica 02.03.02.12
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.seguros.comunicarUnidadesEmissaoRimReportService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/emitirComunicUnidEmisRIM.jasper"
 */

public class ComunicarUnidadesEmissaoRimReportService extends ReportServiceSupport{

	private EnderecoPessoaService enderecoPessoaService;
	
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
    /**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap criteria = (TypedFlatMap) parameters;
    	
    	SqlTemplate sql = getSql(criteria.getList("idsSinistroDoctoServico"), criteria.getString("filial"));
        
        JRReportDataObject jrReportDataObject = executeQuery(sql.getSql(), sql.getCriteria());
        
        EnderecoPessoa enderecoPessoa = this.getEnderecoPessoaService()
        	.findEnderecoPessoaPadrao(SessionUtils.getFilialSessao().getPessoa().getIdPessoa());
        Map parametersReport = new HashMap();        
        parametersReport.put("idsSinistroDoctoServico", criteria.getList("idsSinistroDoctoServico"));
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("nmMunicipio", enderecoPessoa.getMunicipio().getNmMunicipio());
        parametersReport.put("locale", LocaleContextHolder.getLocale());
        parametersReport.put("filial", criteria.getString("filial"));
        
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        jrReportDataObject.setParameters(parametersReport);
        
        return jrReportDataObject;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate getSql(List ids, String filial) {
    	
    	SqlTemplate sql = createSqlTemplate();    	
    	
    	//Projection......	
    	sql.addProjection("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO", "idSinistroDoctoServico");
    	sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO", "tpDoctoServico");
    	sql.addProjection("doctoServico.NR_DOCTO_SERVICO", "nrDoctoServico");
    	sql.addProjection("filial.ID_FILIAL", "idFilial");
    	sql.addProjection("filial.SG_FILIAL", "sgFilial");
    	sql.addProjection("processoSinistro.NR_PROCESSO_SINISTRO", "nrProcessoSinistro");
    	sql.addProjection("processoSinistro.DH_SINISTRO", "dhSinistro");
    	sql.addProjection("pessoa.NM_FANTASIA", "nmFantasia");
    	sql.addProjection("municipio.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativa");
    	sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoLogradouro.DS_TIPO_LOGRADOURO_I"), "dsTipoLogradouro");
    	sql.addProjection("enderecoPessoa.DS_ENDERECO", "dsEndereco");
    	sql.addProjection("enderecoPessoa.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("enderecoPessoa.DS_COMPLEMENTO", "dsComplemento");
    	sql.addProjection("enderecoPessoa.DS_BAIRRO", "dsBairro");
    	sql.addProjection("enderecoPessoa.NR_CEP", "nrCEP");
    	sql.addProjection("(SELECT NR_DDD || '-' || NR_TELEFONE FROM TELEFONE_ENDERECO WHERE ID_PESSOA=filial.ID_FILIAL AND ROWNUM=1)", "nrTelefoneContato");
    	sql.addProjection("processoSinistro.DS_SINISTRO", "dsSinistro");
    	sql.addProjection("CASE processosinistro.tp_local_sinistro " +
				           "WHEN 'A' THEN pessoaAeroporto.NM_PESSOA " +
				           "WHEN 'F' THEN pessoaFilialProcessoSinistro.NM_FANTASIA " +
				           "WHEN 'R' THEN rodoviaProcessoSinistro.DS_RODOVIA || ' ' || processoSinistro.NR_KM_SINISTRO " +
				           "ELSE processoSinistro.DS_LOCAL_SINISTRO " +
				          "END", "nmLocalSinistro");
    	
    	sql.addProjection("municipiosinistro.nm_municipio", "nmMunicipioSinistro");
    	sql.addProjection("unidadefederativasinistro.sg_unidade_federativa", "sgUnidadeFederativaSinistro");
    	
    	//Inicio From...
    	sql.addFrom("SINISTRO_DOCTO_SERVICO", "sinistroDoctoServico");		
    	sql.addFrom("PROCESSO_SINISTRO", "processoSinistro");
    	sql.addFrom("DOCTO_SERVICO", "doctoServico");	
    	sql.addFrom("FILIAL", "filial");
    	sql.addFrom("FILIAL", "filialProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoa");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoa");
    	sql.addFrom("TIPO_LOGRADOURO", "tipoLogradouro");
    	sql.addFrom("MUNICIPIO", "municipio");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativa");
    	sql.addFrom("RODOVIA", "rodoviaProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaFilialProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaAeroporto");
    	sql.addFrom("AEROPORTO", "aeroportoProcessoSinistro");
    	sql.addFrom("MUNICIPIO", "municipioSinistro");
		sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativaSinistro");
    	
    	//Joins...
    	sql.addJoin("sinistroDoctoServico.ID_PROCESSO_SINISTRO", "processoSinistro.ID_PROCESSO_SINISTRO");
    	sql.addJoin("sinistroDoctoServico.ID_DOCTO_SERVICO", "doctoServico.ID_DOCTO_SERVICO");
    	
    	if (filial.equals("origem")) {
    		sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "filial.ID_FILIAL");
    	} else {
    		sql.addJoin("doctoServico.ID_FILIAL_DESTINO", "filial.ID_FILIAL");
    	}

    	sql.addJoin("filial.ID_FILIAL", "pessoa.ID_PESSOA");
    	sql.addJoin("pessoa.ID_ENDERECO_PESSOA", "enderecoPessoa.ID_ENDERECO_PESSOA(+)");
    	sql.addJoin("enderecoPessoa.ID_TIPO_LOGRADOURO", "tipoLogradouro.ID_TIPO_LOGRADOURO(+)");
    	sql.addJoin("municipio.ID_MUNICIPIO(+)", "enderecoPessoa.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativa.ID_UNIDADE_FEDERATIVA(+)", "municipio.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("processoSinistro.ID_RODOVIA", "rodoviaProcessoSinistro.ID_RODOVIA(+)");
    	sql.addJoin("processoSinistro.ID_FILIAL", "filialProcessoSinistro.ID_FILIAL(+)");
    	sql.addJoin("filialProcessoSinistro.ID_FILIAL", "pessoaFilialProcessoSinistro.ID_PESSOA(+)");
    	sql.addJoin("processoSinistro.ID_AEROPORTO", "aeroportoProcessoSinistro.ID_AEROPORTO(+)");
    	sql.addJoin("aeroportoProcessoSinistro.ID_AEROPORTO", "pessoaAeroporto.ID_PESSOA(+)");
    	sql.addJoin("processosinistro.ID_MUNICIPIO", "municipiosinistro.ID_MUNICIPIO");
		sql.addJoin("municipiosinistro.ID_UNIDADE_FEDERATIVA", "unidadefederativasinistro.ID_UNIDADE_FEDERATIVA");
    	
    	//Filtro...
    	if(ids != null && !ids.isEmpty()) {
	        StringBuffer idsSinistroDoctoServico = new StringBuffer();
	        for (Iterator iter = ids.iterator(); iter.hasNext();) {
	        	idsSinistroDoctoServico.append(" " + (String) iter.next()) ;
				 if (iter.hasNext()) 
					 idsSinistroDoctoServico.append(",");
			}
	       	sql.addCustomCriteria("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN (" + idsSinistroDoctoServico.toString() + ")");
    	}

        sql.addOrderBy("filial.ID_FILIAL");
        return sql;         
    }
    
    /**
     * Sub relatorio de DoctoServico. 
     * 
     * @param idsDoctoServico
     * @return
     */
    public JRDataSource findDoctosServicos(List ids, Long idFilial, String tpFilial) {
        JRDataSource dataSource = null;
        
        if (ids == null || ids.isEmpty()) {
            dataSource = new JREmptyDataSource();
        } else {
        	
            SqlTemplate sql = new SqlTemplate();
            sql.addProjection("filialOrigem.ID_FILIAL","idFilial");
            sql.addProjection("filialOrigem.SG_FILIAL","sgFilial");
            sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO","TPDOCTOSERVICO");
            sql.addProjection("doctoServico.NR_DOCTO_SERVICO","nrDoctoServico");
            sql.addProjection("doctoServico.ID_DOCTO_SERVICO","idDoctoServico");
            sql.addProjection("doctoServico.VL_MERCADORIA","vlMercadoria");
            sql.addProjection("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO","idSinistroDoctoServico");
            sql.addProjection("sinistroDoctoServico.VL_PREJUIZO","vlPrejuizo");
            sql.addProjection("moeda.DS_SIMBOLO","dsSimbolo");
            sql.addProjection("moeda.SG_MOEDA","sgMoeda");
            sql.addProjection("pessoaClienteDestinatario.NM_PESSOA", "nmPessoaDestinatario");
        	sql.addProjection("pessoaClienteRemetente.NM_PESSOA", "nmPessoaRemetente");
            
            sql.addFrom("DOCTO_SERVICO", "doctoServico");
            sql.addFrom("FILIAL", "filialOrigem");
            sql.addFrom("SINISTRO_DOCTO_SERVICO", "sinistroDoctoServico");
            sql.addFrom("MOEDA", "moeda");
            sql.addFrom("CLIENTE", "clienteDestinatario");	
        	sql.addFrom("CLIENTE", "clienteRemetente");
            sql.addFrom("PESSOA", "pessoaClienteRemetente");	
        	sql.addFrom("PESSOA", "pessoaClienteDestinatario");
            
        	if ("destino".equals(tpFilial)) {
            	sql.addFrom("FILIAL", "filialDestino");
            }
        	
            sql.addJoin("doctoServico.ID_DOCTO_SERVICO", "sinistroDoctoServico.ID_DOCTO_SERVICO");
        	sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
        	sql.addJoin("sinistroDoctoServico.ID_MOEDA", "moeda.ID_MOEDA");
        	sql.addJoin("doctoServico.ID_CLIENTE_DESTINATARIO", "clienteDestinatario.ID_CLIENTE");
        	sql.addJoin("pessoaClienteDestinatario.ID_PESSOA", "clienteDestinatario.ID_CLIENTE");
        	sql.addJoin("doctoServico.ID_CLIENTE_REMETENTE", "clienteRemetente.ID_CLIENTE");
        	sql.addJoin("pessoaClienteRemetente.ID_PESSOA", "clienteRemetente.ID_CLIENTE");
            
        	StringBuilder idsDoctoServico = new StringBuilder();
            for (Iterator iter = ids.iterator(); iter.hasNext();) {
    			 idsDoctoServico.append(" " + (String) iter.next()) ;
    			 if (iter.hasNext()) {
    				 idsDoctoServico.append(",");
    			 }
    		}
        	
            sql.addCustomCriteria("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN (" + idsDoctoServico + ")");
            
            if ("destino".equals(tpFilial)) {
            	sql.addCriteria("filialDestino.ID_FILIAL", "=", idFilial);
            } else {
            	sql.addCriteria("filialOrigem.ID_FILIAL", "=", idFilial);
            }
            
            sql.addOrderBy("tpDoctoServico");
            sql.addOrderBy("sgFilial");
            sql.addOrderBy("nrDoctoServico");
            
            dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
        }
        
        return dataSource;
    }
     
    public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TPDOCTOSERVICO","DM_TIPO_DOCUMENTO_SERVICO");
	}
    
    public String formatTelefone(String telefone) {
    	return FormatUtils.formatTelefone(telefone.substring(telefone.indexOf('-')+1), telefone.substring(0, telefone.indexOf('-')), null);
    }
    
    /**
     * Sub relatorio de Notas-Fiscais de conhecimento. 
     * 
     * @param idDoctoServico
     * @return
     */
    public JRDataSource findNotasFiscais(Long idDoctoServico) {
        JRDataSource dataSource = null;
        
        if(idDoctoServico==null) {
            dataSource = new JREmptyDataSource();
        } else {
        	
            SqlTemplate sql = new SqlTemplate();
            sql.addProjection("notaFiscalConhecimento.ID_NOTA_FISCAL_CONHECIMENTO","idNotaFiscalConhecimento");
            sql.addProjection("notaFiscalConhecimento.NR_NOTA_FISCAL","nrNotaFiscal");
            
            sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "notaFiscalConhecimento");
            
            sql.addCriteria("notaFiscalConhecimento.ID_CONHECIMENTO", "=", idDoctoServico);

            sql.addOrderBy("nrNotaFiscal");
            
            dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource(); 
        }
        
        return dataSource;
    }
}
