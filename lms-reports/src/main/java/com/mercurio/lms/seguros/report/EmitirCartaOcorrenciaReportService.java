package com.mercurio.lms.seguros.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.seguros.model.SinistroDoctoServico;
import com.mercurio.lms.seguros.model.service.SinistroDoctoServicoService;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Classe responsável pela geração da carta de Emissao de Ocorrencia.
 * Especificação técnica 02.03.02.12
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.seguros.emitirCartaOcorrenciaReportService"
 * @spring.property name="reportName" value="com/mercurio/lms/seguros/report/emitirCartaOcorrencia.jasper"
 */

public class EmitirCartaOcorrenciaReportService extends ReportServiceSupport{

	private EnderecoPessoaService enderecoPessoaService;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;
	private SinistroDoctoServicoService sinistroDoctoServicoService;
	private DomainValueService domainValueService;
	
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	
    public IncluirEventosRastreabilidadeInternacionalService getIncluirEventosRastreabilidadeInternacionalService() {
		return incluirEventosRastreabilidadeInternacionalService;
	}

	public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	public SinistroDoctoServicoService getSinistroDoctoServicoService() {
		return sinistroDoctoServicoService;
	}

	public void setSinistroDoctoServicoService(SinistroDoctoServicoService sinistroDoctoServicoService) {
		this.sinistroDoctoServicoService = sinistroDoctoServicoService;
	}
	
	public DomainValueService getDomainValueService() {
		return domainValueService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	/**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	TypedFlatMap criteria = (TypedFlatMap) parameters;
     
        EnderecoPessoa enderecoPessoa = this.getEnderecoPessoaService()
        	.findEnderecoPessoaPadrao(SessionUtils.getFilialSessao().getPessoa().getIdPessoa());
        
        Map parametersReport = new HashMap();
        
        //Gera os eventos para esta carta...
        this.generateEventoDocumento(criteria);
        
        parametersReport.put("tipoCarta", criteria.getDomainValue("tipoCarta").getValue());
        parametersReport.put("destinatarioCarta", criteria.getDomainValue("destinatarioCarta").getValue());
        parametersReport.put("idsSinistroDoctoServico", criteria.getList("idsSinistroDoctoServico"));
        
        parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("nmMunicipio", enderecoPessoa.getMunicipio().getNmMunicipio());
        parametersReport.put("dataPorExtenso", JTFormatUtils.formatDiaMesAnoPorExtenso(JTDateTimeUtils.getDataHoraAtual()).toLowerCase());
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
     
        JRReportDataObject jrReportDataObject = createReportDataObject(new JREmptyDataSource(), parametersReport);
        return jrReportDataObject;
    }
    
    /**
     * Método chamado pelo emitirCartaOcorrencia.jrxml responsável por gerar o data source para destinatários da carta 
     * de ocorrência do tipo destinatário. 
     * 
     * @param ids
     * @param tipoCarta
     * @return
     */
    public JRDataSource generateSubReportDestinatario(List ids, String tipoCarta) {
        return generateSubReport(ids, tipoCarta, "D");
    }
    
    /**
     * Método chamado pelo emitirCartaOcorrencia.jrxml responsável por gerar o data source para destinatários da carta 
     * de ocorrência do tipo remetente. 
     * 
     * @param ids
     * @param tipoCarta
     * @return
     */
    public JRDataSource generateSubReportRemetente(List ids, String tipoCarta) {
    	return generateSubReport(ids, tipoCarta, "R");
    }

    /**
     * Método chamado pelo emitirCartaOcorrencia.jrxml responsável por gerar o data source para destinatários da carta 
     * de ocorrência do tipo filial destino. 
     * 
     * @param ids
     * @param tipoCarta
     * @return
     */
    public JRDataSource generateSubReportFilialDestino(List ids, String tipoCarta) {
    	return generateSubReport(ids, tipoCarta, "S");
    }
    
    /**
     * Método chamado pelo emitirCartaOcorrencia.jrxml responsável por gerar o data source para destinatários da carta 
     * de ocorrência do tipo filial origem. 
     * 
     * @param ids
     * @param tipoCarta
     * @return
     */
    public JRDataSource generateSubReportFilialOrigem(List ids, String tipoCarta) {
    	return generateSubReport(ids, tipoCarta, "O");
    }
    
    /**
     * LMS-6140 - Para ser possível adicionar dois novos tipos de destinatário de carta e não replicar código,
     * foi criado este método genérico para que posso ser chamado por todos os tipos de destinatário da carta de ocorrência
     *  
     * 
     * @param ids
     * @param tipoCarta
     * @param destinatarioCarta
     * @return
     */
	private JRDataSource generateSubReport(List ids, String tipoCarta, String destinatarioCarta) {
		JRDataSource dataSource = null;
    	
    	if (ids != null) {
    		SqlTemplate sql = null;
	    	if ("D".equals(destinatarioCarta)) {
	    		sql = this.getSqlDestinatario(ids ,tipoCarta);
	    	}
	    	// LMS-6140 filial origem - agrupar pelo ID_FILIAL e a pessoa que receberá a carta é a filial de origem
	    	if ("O".equals(destinatarioCarta)) {
	    		sql = this.getSqlFilialOrigem(ids ,tipoCarta);
	    	}
	    	// LMS-6140 filial destino - agrupar pelo ID_FILIAL e a pessoa que receberá a carta é a filial de destino
	    	if ("S".equals(destinatarioCarta)) {
	    		sql = this.getSqlFilialDestino(ids ,tipoCarta);
	    	}
	    	if ("R".equals(destinatarioCarta)) {
	    		sql = this.getSqlRemetente(ids ,tipoCarta);
	    	}
	    	
	    	dataSource = executeQuery(sql.getSql(), sql.getCriteria()).getDataSource();
	    }
    	
    	return dataSource;
	}
    
	/**
     * Método que monta o select para a consulta de clientes do tipo destinatário.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate getSqlDestinatario(List ids, String tipoCarta) {
    	SqlTemplate sql = createSqlTemplate();
    	
    	//Projection......	
    	sql.addProjection("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO", "idSinistroDoctoServico");
    	sql.addProjection("processoSinistro.ID_PROCESSO_SINISTRO", "idprocessoSinistro");
    	sql.addProjection("tipoSinistro.ID_TIPO_SINISTRO", "idTipoSinistro");
    	sql.addProjection("doctoServico.ID_DOCTO_SERVICO", "idDoctoServico");
    	sql.addProjection("pessoaClienteRemetente.ID_PESSOA", "idPessoaClienteRemetente");
    	sql.addProjection("clienteRemetente.ID_CLIENTE", "idClienteRemetente");
    	sql.addProjection("pessoaClienteDestinatario.ID_PESSOA", "idPessoaClienteDestinatario");
    	sql.addProjection("clienteDestinatario.ID_CLIENTE", "idClienteDestinatario");
    	sql.addProjection("clienteDestinatario.ID_CLIENTE", "idCliente");
		sql.addProjection("pessoaClienteDestinatario.NM_PESSOA", "nmPessoaEnvio");
    	
    	if (tipoCarta.equals("O")) {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_OCORRENCIA_I"), "dsTextoCarta");
    	} else {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_RETIFICACAO_I"), "dsTextoCarta");
    	}
    	
    	sql.addProjection("municipioSinistro.NM_MUNICIPIO", "nmMunicipioSinistro");
    	sql.addProjection("unidadeFederativaSinistro.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativaSinistro");
    	sql.addProjection("municipio.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativa");
    	sql.addProjection("enderecoPessoa.NR_CEP", "nrCep");
    	sql.addProjection("(select "+PropertyVarcharI18nProjection.createProjection("ds_tipo_logradouro_i")+" from tipo_logradouro where id_tipo_logradouro = enderecoPessoa.id_tipo_logradouro)", "dsTipoLogradouro");
    	sql.addProjection("enderecoPessoa.DS_ENDERECO", "dsEndereco");
    	sql.addProjection("enderecoPessoa.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("enderecoPessoa.DS_COMPLEMENTO", "dsComplemento");
    	sql.addProjection("enderecoPessoa.DS_BAIRRO", "dsBairro");
    	sql.addProjection("processoSinistro.DH_SINISTRO", "dhSinistro");
    	sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO", "TPDOCTOSERVICO");
    	sql.addProjection("doctoServico.VL_MERCADORIA", "vlMercadoria");
    	sql.addProjection("moeda.SG_MOEDA", "sgMoeda");
    	sql.addProjection("moeda.DS_SIMBOLO", "dsSimbolo");
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilial");
    	sql.addProjection("doctoServico.NR_DOCTO_SERVICO", "nrDoctoServico");
    	sql.addProjection("pessoaClienteDestinatario.NM_PESSOA", "nmPessoaDestinatario");
    	sql.addProjection("pessoaClienteRemetente.NM_PESSOA", "nmPessoaRemetente");
    	sql.addProjection("(SELECT NR_DDD || '-' || NR_TELEFONE FROM TELEFONE_ENDERECO WHERE ID_PESSOA=clienteRemetente.ID_CLIENTE AND ROWNUM=1)", "nrTelefoneContato");
    	
    	//LMS-6140
    	sql.addProjection("processoSinistro.DS_SINISTRO", "dsSinistro");
    	
    	sql.addProjection("(SELECT VI18n(VD.ds_valor_dominio_i) "
    			+ "FROM VALOR_DOMINIO VD "
    			+ "WHERE VD.VL_VALOR_DOMINIO = sinistroDoctoServico.TP_PREJUIZO "
    			+ "AND VD.ID_DOMINIO = (SELECT D.ID_DOMINIO FROM DOMINIO D WHERE D.NM_DOMINIO = 'DM_TIPO_PREJUIZO'))", "tpPrejuizo");
    	
    	sql.addProjection("CASE processosinistro.tp_local_sinistro " +
	           			  " WHEN 'A' THEN pessoaAeroporto.NM_PESSOA " +
	           			  " WHEN 'F' THEN pessoaFilialProcessoSinistro.NM_FANTASIA " +
	           			  " WHEN 'R' THEN rodoviaProcessoSinistro.DS_RODOVIA || ' ' || processoSinistro.NR_KM_SINISTRO " +
	           			  "ELSE processoSinistro.DS_LOCAL_SINISTRO " +
	           			  "END", "nmLocalSinistro");
    	
    	//Inicio From...
    	sql.addFrom("SINISTRO_DOCTO_SERVICO", "sinistroDoctoServico");
    	sql.addFrom("PROCESSO_SINISTRO", "processoSinistro");	
    	sql.addFrom("TIPO_SINISTRO", "tipoSinistro");	
    	sql.addFrom("DOCTO_SERVICO", "doctoServico");	
    	sql.addFrom("MOEDA", "moeda");
    	sql.addFrom("CLIENTE", "clienteDestinatario");	
    	sql.addFrom("CLIENTE", "clienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteDestinatario");	
    	sql.addFrom("FILIAL", "filialOrigem");
    	sql.addFrom("FILIAL", "filialProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaFilialProcessoSinistro");
    	sql.addFrom("RODOVIA", "rodoviaProcessoSinistro");
    	sql.addFrom("AEROPORTO", "aeroportoProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaAeroporto");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoa");
    	sql.addFrom("MUNICIPIO", "municipio");
    	sql.addFrom("MUNICIPIO", "municipioSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativaSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativa");
    	
    	//Joins...
    	sql.addJoin("sinistroDoctoServico.ID_PROCESSO_SINISTRO", "processoSinistro.ID_PROCESSO_SINISTRO");
    	sql.addJoin("processoSinistro.ID_TIPO_SINISTRO", "tipoSinistro.ID_TIPO_SINISTRO");
    	sql.addJoin("sinistroDoctoServico.ID_DOCTO_SERVICO", "doctoServico.ID_DOCTO_SERVICO");
    	sql.addJoin("doctoServico.ID_CLIENTE_DESTINATARIO", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_CLIENTE_REMETENTE", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteRemetente.ID_PESSOA", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteDestinatario.ID_PESSOA", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("doctoServico.ID_MOEDA", "moeda.ID_MOEDA");
    	sql.addJoin("processoSinistro.ID_FILIAL", "filialProcessoSinistro.ID_FILIAL(+)");
    	sql.addJoin("filialProcessoSinistro.ID_FILIAL", "pessoaFilialProcessoSinistro.ID_PESSOA(+)");
    	sql.addJoin("processoSinistro.ID_RODOVIA", "rodoviaProcessoSinistro.ID_RODOVIA(+)");
    	sql.addJoin("processoSinistro.ID_AEROPORTO", "aeroportoProcessoSinistro.ID_AEROPORTO(+)");
    	sql.addJoin("aeroportoProcessoSinistro.ID_AEROPORTO", "pessoaAeroporto.ID_PESSOA(+)");
    	sql.addJoin("enderecoPessoa.ID_ENDERECO_PESSOA(+)", "pessoaClienteDestinatario.ID_ENDERECO_PESSOA");
    	sql.addJoin("municipioSinistro.ID_MUNICIPIO", "processoSinistro.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativaSinistro.ID_UNIDADE_FEDERATIVA", "municipioSinistro.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("municipio.ID_MUNICIPIO(+)", "enderecoPessoa.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativa.ID_UNIDADE_FEDERATIVA(+)", "municipio.ID_UNIDADE_FEDERATIVA");

    	//Filtro...
        StringBuffer idsSinistroDoctoServico = new StringBuffer();
        for (Iterator iter = ids.iterator(); iter.hasNext();) {
			 idsSinistroDoctoServico.append(" " + (String) iter.next()) ;
			 if (iter.hasNext()) {
				 idsSinistroDoctoServico.append(",");
			 }
		}
        sql.addCustomCriteria("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN (" + idsSinistroDoctoServico.toString() + ")");
        
        
        //Order...
        sql.addOrderBy("clienteDestinatario.ID_CLIENTE");
        
        return sql;
    
    }
    
    /**
     * Método que monta o select para a consulta de clientes do tipo remetente.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate getSqlRemetente(List ids, String tipoCarta) {
    	SqlTemplate sql = createSqlTemplate();
    	
    	//Projection......	
    	sql.addProjection("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO", "idSinistroDoctoServico");
    	sql.addProjection("processoSinistro.ID_PROCESSO_SINISTRO", "idprocessoSinistro");
    	sql.addProjection("tipoSinistro.ID_TIPO_SINISTRO", "idTipoSinistro");
    	sql.addProjection("doctoServico.ID_DOCTO_SERVICO", "idDoctoServico");
    	sql.addProjection("pessoaClienteRemetente.ID_PESSOA", "idPessoaClienteRemetente");
    	sql.addProjection("clienteRemetente.ID_CLIENTE", "idClienteRemetente");
    	sql.addProjection("pessoaClienteDestinatario.ID_PESSOA", "idPessoaClienteDestinatario");
    	sql.addProjection("clienteDestinatario.ID_CLIENTE", "idClienteDestinatario");
    	
    	if (tipoCarta.equals("O")) {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_OCORRENCIA_I"), "dsTextoCarta");
    	} else {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_RETIFICACAO_I"), "dsTextoCarta");
    	}
    	
    	sql.addProjection("clienteRemetente.ID_CLIENTE", "idCliente");
		sql.addProjection("pessoaClienteRemetente.NM_PESSOA", "nmPessoaEnvio");
    	sql.addProjection("municipioSinistro.NM_MUNICIPIO", "nmMunicipioSinistro");
    	sql.addProjection("unidadeFederativaSinistro.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativaSinistro");
    	sql.addProjection("municipio.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativa");
    	sql.addProjection("enderecoPessoa.NR_CEP", "nrCep");
    	sql.addProjection("(select "+PropertyVarcharI18nProjection.createProjection("ds_tipo_logradouro_i")+" from tipo_logradouro where id_tipo_logradouro = enderecoPessoa.id_tipo_logradouro)", "dsTipoLogradouro");
    	sql.addProjection("enderecoPessoa.DS_ENDERECO", "dsEndereco");
    	sql.addProjection("enderecoPessoa.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("enderecoPessoa.DS_COMPLEMENTO", "dsComplemento");
    	sql.addProjection("enderecoPessoa.DS_BAIRRO", "dsBairro");
    	sql.addProjection("processoSinistro.DH_SINISTRO", "dhSinistro");
    	sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO", "TPDOCTOSERVICO");
    	sql.addProjection("doctoServico.VL_MERCADORIA", "vlMercadoria");
    	sql.addProjection("moeda.SG_MOEDA", "sgMoeda");
    	sql.addProjection("moeda.DS_SIMBOLO", "dsSimbolo");
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilial");
    	sql.addProjection("doctoServico.NR_DOCTO_SERVICO", "nrDoctoServico");
    	sql.addProjection("pessoaClienteDestinatario.NM_PESSOA", "nmPessoaDestinatario");
    	sql.addProjection("pessoaClienteRemetente.NM_PESSOA", "nmPessoaRemetente");
    	sql.addProjection("(SELECT NR_DDD || '-' || NR_TELEFONE FROM TELEFONE_ENDERECO WHERE ID_PESSOA=clienteRemetente.ID_CLIENTE AND ROWNUM=1)", "nrTelefoneContato");
    	
    	//LMS-6140
    	sql.addProjection("processoSinistro.DS_SINISTRO", "dsSinistro");
    	
    	sql.addProjection("(SELECT VI18n(VD.ds_valor_dominio_i) "
    			+ "FROM VALOR_DOMINIO VD "
    			+ "WHERE VD.VL_VALOR_DOMINIO = sinistroDoctoServico.TP_PREJUIZO "
    			+ "AND VD.ID_DOMINIO = (SELECT D.ID_DOMINIO FROM DOMINIO D WHERE D.NM_DOMINIO = 'DM_TIPO_PREJUIZO'))", "tpPrejuizo");
    	
    	sql.addProjection("CASE processosinistro.tp_local_sinistro " +
	           			  " WHEN 'A' THEN pessoaAeroporto.NM_PESSOA " +
	           			  " WHEN 'F' THEN pessoaFilialProcessoSinistro.NM_FANTASIA " +
	           			  " WHEN 'R' THEN rodoviaProcessoSinistro.DS_RODOVIA || ' ' || processoSinistro.NR_KM_SINISTRO " +
	           			  "ELSE processoSinistro.DS_LOCAL_SINISTRO " +
	           			  "END", "nmLocalSinistro");
    	
    	//Inicio From...
    	sql.addFrom("SINISTRO_DOCTO_SERVICO", "sinistroDoctoServico");
    	sql.addFrom("PROCESSO_SINISTRO", "processoSinistro");	
    	sql.addFrom("TIPO_SINISTRO", "tipoSinistro");	
    	sql.addFrom("DOCTO_SERVICO", "doctoServico");	
    	sql.addFrom("MOEDA", "moeda");
    	sql.addFrom("CLIENTE", "clienteDestinatario");	
    	sql.addFrom("CLIENTE", "clienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteDestinatario");	
    	sql.addFrom("FILIAL", "filialOrigem");
    	sql.addFrom("FILIAL", "filialProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaFilialProcessoSinistro");
    	sql.addFrom("RODOVIA", "rodoviaProcessoSinistro");
    	sql.addFrom("AEROPORTO", "aeroportoProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaAeroporto");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoa");
    	sql.addFrom("MUNICIPIO", "municipio");
    	sql.addFrom("MUNICIPIO", "municipioSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativaSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativa");
    	
    	//Joins...
    	sql.addJoin("sinistroDoctoServico.ID_PROCESSO_SINISTRO", "processoSinistro.ID_PROCESSO_SINISTRO");
    	sql.addJoin("processoSinistro.ID_TIPO_SINISTRO", "tipoSinistro.ID_TIPO_SINISTRO");
    	sql.addJoin("sinistroDoctoServico.ID_DOCTO_SERVICO", "doctoServico.ID_DOCTO_SERVICO");
    	sql.addJoin("doctoServico.ID_CLIENTE_DESTINATARIO", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_CLIENTE_REMETENTE", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteRemetente.ID_PESSOA", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteDestinatario.ID_PESSOA", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("doctoServico.ID_MOEDA", "moeda.ID_MOEDA");
    	sql.addJoin("processoSinistro.ID_FILIAL", "filialProcessoSinistro.ID_FILIAL(+)");
    	sql.addJoin("filialProcessoSinistro.ID_FILIAL", "pessoaFilialProcessoSinistro.ID_PESSOA(+)");
    	sql.addJoin("processoSinistro.ID_RODOVIA", "rodoviaProcessoSinistro.ID_RODOVIA(+)");
    	sql.addJoin("processoSinistro.ID_AEROPORTO", "aeroportoProcessoSinistro.ID_AEROPORTO(+)");
    	sql.addJoin("aeroportoProcessoSinistro.ID_AEROPORTO", "pessoaAeroporto.ID_PESSOA(+)");
    	sql.addJoin("enderecoPessoa.ID_ENDERECO_PESSOA(+)", "pessoaClienteRemetente.ID_ENDERECO_PESSOA");
    	sql.addJoin("municipioSinistro.ID_MUNICIPIO", "processoSinistro.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativaSinistro.ID_UNIDADE_FEDERATIVA", "municipioSinistro.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("municipio.ID_MUNICIPIO(+)", "enderecoPessoa.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativa.ID_UNIDADE_FEDERATIVA(+)", "municipio.ID_UNIDADE_FEDERATIVA");

    	//Filtro...
        StringBuilder idsSinistroDoctoServico = new StringBuilder();
        for (Iterator iter = ids.iterator(); iter.hasNext();) {
			 idsSinistroDoctoServico.append(" " + (String) iter.next()) ;
			 if (iter.hasNext()) {
				 idsSinistroDoctoServico.append(",");
			 }
		}
        sql.addCustomCriteria("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN (" + idsSinistroDoctoServico.toString() + ")");
        
        
        //Order...
        sql.addOrderBy("clienteRemetente.ID_CLIENTE");
        
        return sql;
    
    }
    
    /**
     * Método que monta o select para a consulta de clientes do tipo filial origem.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate getSqlFilialOrigem(List ids, String tipoCarta) {
    	SqlTemplate sql = createSqlTemplate();
    	
    	//Projection......	
    	sql.addProjection("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO", "idSinistroDoctoServico");
    	sql.addProjection("processoSinistro.ID_PROCESSO_SINISTRO", "idprocessoSinistro");
    	sql.addProjection("tipoSinistro.ID_TIPO_SINISTRO", "idTipoSinistro");
    	sql.addProjection("doctoServico.ID_DOCTO_SERVICO", "idDoctoServico");
    	sql.addProjection("pessoaClienteRemetente.ID_PESSOA", "idPessoaClienteRemetente");
    	sql.addProjection("clienteRemetente.ID_CLIENTE", "idClienteRemetente");
    	sql.addProjection("pessoaClienteDestinatario.ID_PESSOA", "idPessoaClienteDestinatario");
    	sql.addProjection("clienteDestinatario.ID_CLIENTE", "idClienteDestinatario");
    	
    	if (tipoCarta.equals("O")) {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_OCORRENCIA_I"), "dsTextoCarta");
    	} else {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_RETIFICACAO_I"), "dsTextoCarta");
    	}
    	
    	sql.addProjection("doctoServico.ID_FILIAL_ORIGEM", "idCliente");
    	sql.addProjection("pessoaFilialOrigem.NM_FANTASIA", "nmPessoaEnvio");
    	sql.addProjection("municipioSinistro.NM_MUNICIPIO", "nmMunicipioSinistro");
    	sql.addProjection("unidadeFederativaSinistro.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativaSinistro");
    	sql.addProjection("municipio.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativa");
    	sql.addProjection("enderecoPessoa.NR_CEP", "nrCep");
    	sql.addProjection("(select "+PropertyVarcharI18nProjection.createProjection("ds_tipo_logradouro_i")+" from tipo_logradouro where id_tipo_logradouro = enderecoPessoa.id_tipo_logradouro)", "dsTipoLogradouro");
    	sql.addProjection("enderecoPessoa.DS_ENDERECO", "dsEndereco");
    	sql.addProjection("enderecoPessoa.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("enderecoPessoa.DS_COMPLEMENTO", "dsComplemento");
    	sql.addProjection("enderecoPessoa.DS_BAIRRO", "dsBairro");
    	sql.addProjection("processoSinistro.DH_SINISTRO", "dhSinistro");
    	sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO", "TPDOCTOSERVICO");
    	sql.addProjection("doctoServico.VL_MERCADORIA", "vlMercadoria");
    	sql.addProjection("moeda.SG_MOEDA", "sgMoeda");
    	sql.addProjection("moeda.DS_SIMBOLO", "dsSimbolo");
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilial");
    	sql.addProjection("doctoServico.NR_DOCTO_SERVICO", "nrDoctoServico");
    	sql.addProjection("pessoaClienteDestinatario.NM_PESSOA", "nmPessoaDestinatario");
    	sql.addProjection("pessoaClienteRemetente.NM_PESSOA", "nmPessoaRemetente");
    	sql.addProjection("(SELECT NR_DDD || '-' || NR_TELEFONE FROM TELEFONE_ENDERECO WHERE ID_PESSOA=clienteRemetente.ID_CLIENTE AND ROWNUM=1)", "nrTelefoneContato");
    	
    	//LMS-6140
    	sql.addProjection("processoSinistro.DS_SINISTRO", "dsSinistro");
    	
    	sql.addProjection("(SELECT VI18n(VD.ds_valor_dominio_i) "
    			+ "FROM VALOR_DOMINIO VD "
    			+ "WHERE VD.VL_VALOR_DOMINIO = sinistroDoctoServico.TP_PREJUIZO "
    			+ "AND VD.ID_DOMINIO = (SELECT D.ID_DOMINIO FROM DOMINIO D WHERE D.NM_DOMINIO = 'DM_TIPO_PREJUIZO'))", "tpPrejuizo");
    	
    	sql.addProjection("CASE processosinistro.tp_local_sinistro " +
	           			  " WHEN 'A' THEN pessoaAeroporto.NM_PESSOA " +
	           			  " WHEN 'F' THEN pessoaFilialProcessoSinistro.NM_FANTASIA " +
	           			  " WHEN 'R' THEN rodoviaProcessoSinistro.DS_RODOVIA || ' ' || processoSinistro.NR_KM_SINISTRO " +
	           			  "ELSE processoSinistro.DS_LOCAL_SINISTRO " +
	           			  "END", "nmLocalSinistro");
    	
    	//Inicio From...
    	sql.addFrom("SINISTRO_DOCTO_SERVICO", "sinistroDoctoServico");
    	sql.addFrom("PROCESSO_SINISTRO", "processoSinistro");	
    	sql.addFrom("TIPO_SINISTRO", "tipoSinistro");	
    	sql.addFrom("DOCTO_SERVICO", "doctoServico");	
    	sql.addFrom("MOEDA", "moeda");
    	sql.addFrom("CLIENTE", "clienteDestinatario");	
    	sql.addFrom("CLIENTE", "clienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteDestinatario");	
    	sql.addFrom("FILIAL", "filialOrigem");
    	sql.addFrom("PESSOA", "pessoaFilialOrigem");
    	sql.addFrom("FILIAL", "filialProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaFilialProcessoSinistro");
    	sql.addFrom("RODOVIA", "rodoviaProcessoSinistro");
    	sql.addFrom("AEROPORTO", "aeroportoProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaAeroporto");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoa");
    	sql.addFrom("MUNICIPIO", "municipio");
    	sql.addFrom("MUNICIPIO", "municipioSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativaSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativa");
    	
    	//Joins...
    	sql.addJoin("sinistroDoctoServico.ID_PROCESSO_SINISTRO", "processoSinistro.ID_PROCESSO_SINISTRO");
    	sql.addJoin("processoSinistro.ID_TIPO_SINISTRO", "tipoSinistro.ID_TIPO_SINISTRO");
    	sql.addJoin("sinistroDoctoServico.ID_DOCTO_SERVICO", "doctoServico.ID_DOCTO_SERVICO");
    	sql.addJoin("doctoServico.ID_CLIENTE_DESTINATARIO", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_CLIENTE_REMETENTE", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteRemetente.ID_PESSOA", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteDestinatario.ID_PESSOA", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "pessoaFilialOrigem.ID_PESSOA");
    	sql.addJoin("doctoServico.ID_MOEDA", "moeda.ID_MOEDA");
    	sql.addJoin("processoSinistro.ID_FILIAL", "filialProcessoSinistro.ID_FILIAL(+)");
    	sql.addJoin("filialProcessoSinistro.ID_FILIAL", "pessoaFilialProcessoSinistro.ID_PESSOA(+)");
    	sql.addJoin("processoSinistro.ID_RODOVIA", "rodoviaProcessoSinistro.ID_RODOVIA(+)");
    	sql.addJoin("processoSinistro.ID_AEROPORTO", "aeroportoProcessoSinistro.ID_AEROPORTO(+)");
    	sql.addJoin("aeroportoProcessoSinistro.ID_AEROPORTO", "pessoaAeroporto.ID_PESSOA(+)");
    	sql.addJoin("enderecoPessoa.ID_ENDERECO_PESSOA(+)", "pessoaFilialOrigem.ID_ENDERECO_PESSOA");
    	sql.addJoin("municipioSinistro.ID_MUNICIPIO", "processoSinistro.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativaSinistro.ID_UNIDADE_FEDERATIVA", "municipioSinistro.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("municipio.ID_MUNICIPIO(+)", "enderecoPessoa.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativa.ID_UNIDADE_FEDERATIVA(+)", "municipio.ID_UNIDADE_FEDERATIVA");

    	//Filtro...
        StringBuilder idsSinistroDoctoServico = new StringBuilder();
        for (Iterator iter = ids.iterator(); iter.hasNext();) {
			 idsSinistroDoctoServico.append(" " + (String) iter.next()) ;
			 if (iter.hasNext()) { 
				 idsSinistroDoctoServico.append(",");
			 }
		}
        sql.addCustomCriteria("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN (" + idsSinistroDoctoServico.toString() + ")");
        
        //Order...
        sql.addOrderBy("doctoServico.ID_FILIAL_ORIGEM");
    	
        return sql;
    }
    
    /**
     * Método que monta o select para a consulta de clientes do tipo filial destino.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate getSqlFilialDestino(List ids, String tipoCarta) {
    	SqlTemplate sql = createSqlTemplate();
    	
    	//Projection......	
    	sql.addProjection("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO", "idSinistroDoctoServico");
    	sql.addProjection("processoSinistro.ID_PROCESSO_SINISTRO", "idprocessoSinistro");
    	sql.addProjection("tipoSinistro.ID_TIPO_SINISTRO", "idTipoSinistro");
    	sql.addProjection("doctoServico.ID_DOCTO_SERVICO", "idDoctoServico");
    	sql.addProjection("pessoaClienteRemetente.ID_PESSOA", "idPessoaClienteRemetente");
    	sql.addProjection("clienteRemetente.ID_CLIENTE", "idClienteRemetente");
    	sql.addProjection("pessoaClienteDestinatario.ID_PESSOA", "idPessoaClienteDestinatario");
    	sql.addProjection("clienteDestinatario.ID_CLIENTE", "idClienteDestinatario");
    	
    	if (tipoCarta.equals("O")) {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_OCORRENCIA_I"), "dsTextoCarta");
    	} else {
    		sql.addProjection(PropertyVarcharI18nProjection.createProjection("tipoSinistro.DS_TEXTO_CARTA_RETIFICACAO_I"), "dsTextoCarta");
    	}
    	
    	sql.addProjection("doctoServico.ID_FILIAL_DESTINO", "idCliente");
    	sql.addProjection("pessoaFilialDestino.NM_FANTASIA", "nmPessoaEnvio");
    	sql.addProjection("municipioSinistro.NM_MUNICIPIO", "nmMunicipioSinistro");
    	sql.addProjection("unidadeFederativaSinistro.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativaSinistro");
    	sql.addProjection("municipio.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("unidadeFederativa.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativa");
    	sql.addProjection("enderecoPessoa.NR_CEP", "nrCep");
    	sql.addProjection("(select "+PropertyVarcharI18nProjection.createProjection("ds_tipo_logradouro_i")+" from tipo_logradouro where id_tipo_logradouro = enderecoPessoa.id_tipo_logradouro)", "dsTipoLogradouro");
    	sql.addProjection("enderecoPessoa.DS_ENDERECO", "dsEndereco");
    	sql.addProjection("enderecoPessoa.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("enderecoPessoa.DS_COMPLEMENTO", "dsComplemento");
    	sql.addProjection("enderecoPessoa.DS_BAIRRO", "dsBairro");
    	sql.addProjection("processoSinistro.DH_SINISTRO", "dhSinistro");
    	sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO", "TPDOCTOSERVICO");
    	sql.addProjection("doctoServico.VL_MERCADORIA", "vlMercadoria");
    	sql.addProjection("moeda.SG_MOEDA", "sgMoeda");
    	sql.addProjection("moeda.DS_SIMBOLO", "dsSimbolo");
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilial");
    	sql.addProjection("doctoServico.NR_DOCTO_SERVICO", "nrDoctoServico");
    	sql.addProjection("pessoaClienteDestinatario.NM_PESSOA", "nmPessoaDestinatario");
    	sql.addProjection("pessoaClienteRemetente.NM_PESSOA", "nmPessoaRemetente");
    	sql.addProjection("(SELECT NR_DDD || '-' || NR_TELEFONE FROM TELEFONE_ENDERECO WHERE ID_PESSOA=clienteRemetente.ID_CLIENTE AND ROWNUM=1)", "nrTelefoneContato");
    	
    	//LMS-6140
    	sql.addProjection("processoSinistro.DS_SINISTRO", "dsSinistro");
    	
    	sql.addProjection("(SELECT VI18n(VD.ds_valor_dominio_i) "
    			+ "FROM VALOR_DOMINIO VD "
    			+ "WHERE VD.VL_VALOR_DOMINIO = sinistroDoctoServico.TP_PREJUIZO "
    			+ "AND VD.ID_DOMINIO = (SELECT D.ID_DOMINIO FROM DOMINIO D WHERE D.NM_DOMINIO = 'DM_TIPO_PREJUIZO'))", "tpPrejuizo");
    	
    	sql.addProjection("CASE processosinistro.tp_local_sinistro " +
	           			  " WHEN 'A' THEN pessoaAeroporto.NM_PESSOA " +
	           			  " WHEN 'F' THEN pessoaFilialProcessoSinistro.NM_FANTASIA " +
	           			  " WHEN 'R' THEN rodoviaProcessoSinistro.DS_RODOVIA || ' ' || processoSinistro.NR_KM_SINISTRO " +
	           			  "ELSE processoSinistro.DS_LOCAL_SINISTRO " +
	           			  "END", "nmLocalSinistro");
    	
    	//Inicio From...
    	sql.addFrom("SINISTRO_DOCTO_SERVICO", "sinistroDoctoServico");
    	sql.addFrom("PROCESSO_SINISTRO", "processoSinistro");	
    	sql.addFrom("TIPO_SINISTRO", "tipoSinistro");	
    	sql.addFrom("DOCTO_SERVICO", "doctoServico");	
    	sql.addFrom("MOEDA", "moeda");
    	sql.addFrom("CLIENTE", "clienteDestinatario");	
    	sql.addFrom("CLIENTE", "clienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteRemetente");	
    	sql.addFrom("PESSOA", "pessoaClienteDestinatario");	
    	sql.addFrom("FILIAL", "filialOrigem");
    	sql.addFrom("PESSOA", "pessoaFilialDestino");
    	sql.addFrom("FILIAL", "filialProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaFilialProcessoSinistro");
    	sql.addFrom("RODOVIA", "rodoviaProcessoSinistro");
    	sql.addFrom("AEROPORTO", "aeroportoProcessoSinistro");
    	sql.addFrom("PESSOA", "pessoaAeroporto");
    	sql.addFrom("ENDERECO_PESSOA", "enderecoPessoa");
    	sql.addFrom("MUNICIPIO", "municipio");
    	sql.addFrom("MUNICIPIO", "municipioSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativaSinistro");
    	sql.addFrom("UNIDADE_FEDERATIVA", "unidadeFederativa");
    	
    	//Joins...
    	sql.addJoin("sinistroDoctoServico.ID_PROCESSO_SINISTRO", "processoSinistro.ID_PROCESSO_SINISTRO");
    	sql.addJoin("processoSinistro.ID_TIPO_SINISTRO", "tipoSinistro.ID_TIPO_SINISTRO");
    	sql.addJoin("sinistroDoctoServico.ID_DOCTO_SERVICO", "doctoServico.ID_DOCTO_SERVICO");
    	sql.addJoin("doctoServico.ID_CLIENTE_DESTINATARIO", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_CLIENTE_REMETENTE", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteRemetente.ID_PESSOA", "clienteRemetente.ID_CLIENTE");
    	sql.addJoin("pessoaClienteDestinatario.ID_PESSOA", "clienteDestinatario.ID_CLIENTE");
    	sql.addJoin("doctoServico.ID_FILIAL_ORIGEM", "filialOrigem.ID_FILIAL");
    	sql.addJoin("doctoServico.ID_FILIAL_DESTINO", "pessoaFilialDestino.ID_PESSOA");
    	sql.addJoin("doctoServico.ID_MOEDA", "moeda.ID_MOEDA");
    	sql.addJoin("processoSinistro.ID_FILIAL", "filialProcessoSinistro.ID_FILIAL(+)");
    	sql.addJoin("filialProcessoSinistro.ID_FILIAL", "pessoaFilialProcessoSinistro.ID_PESSOA(+)");
    	sql.addJoin("processoSinistro.ID_RODOVIA", "rodoviaProcessoSinistro.ID_RODOVIA(+)");
    	sql.addJoin("processoSinistro.ID_AEROPORTO", "aeroportoProcessoSinistro.ID_AEROPORTO(+)");
    	sql.addJoin("aeroportoProcessoSinistro.ID_AEROPORTO", "pessoaAeroporto.ID_PESSOA(+)");
    	sql.addJoin("enderecoPessoa.ID_ENDERECO_PESSOA(+)", "pessoaFilialDestino.ID_ENDERECO_PESSOA");
    	sql.addJoin("municipioSinistro.ID_MUNICIPIO", "processoSinistro.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativaSinistro.ID_UNIDADE_FEDERATIVA", "municipioSinistro.ID_UNIDADE_FEDERATIVA");
    	sql.addJoin("municipio.ID_MUNICIPIO(+)", "enderecoPessoa.ID_MUNICIPIO");
    	sql.addJoin("unidadeFederativa.ID_UNIDADE_FEDERATIVA(+)", "municipio.ID_UNIDADE_FEDERATIVA");

    	//Filtro...
        StringBuilder idsSinistroDoctoServico = new StringBuilder();
        for (Iterator iter = ids.iterator(); iter.hasNext();) {
			 idsSinistroDoctoServico.append(" " + (String) iter.next()) ;
			 if (iter.hasNext()) {
				 idsSinistroDoctoServico.append(",");
			 }
		}
        sql.addCustomCriteria("sinistroDoctoServico.ID_SINISTRO_DOCTO_SERVICO IN (" + idsSinistroDoctoServico.toString() + ")");
        
        //Order...
        sql.addOrderBy("doctoServico.ID_FILIAL_DESTINO");
    	
        return sql;
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
    
    private void generateEventoDocumento(TypedFlatMap criteria)  {
    	
    	final String dmTtipoDocumento = "DM_TIPO_DOCUMENTO";
    	Filial filialUsuario = SessionUtils.getFilialSessao();
    	DateTime dataHoraAtual = JTDateTimeUtils.getDataHoraAtual();
    	List<String> tiposDocumento = new ArrayList<String>();

    	for (DomainValue domainValue : (List<DomainValue>)domainValueService.findDomainValues(dmTtipoDocumento)) {
       		tiposDocumento.add(domainValue.getValue());
		}
    	
    	List idsSinistroDoctoServico = criteria.getList("idsSinistroDoctoServico");
    	for (Iterator iter = idsSinistroDoctoServico.iterator(); iter.hasNext();) {
    		Long idSinistroDoctoServico = Long.valueOf((String)iter.next()); 
    		SinistroDoctoServico sinistroDoctoServico = this.getSinistroDoctoServicoService().findById(idSinistroDoctoServico);
    		
    		if(sinistroDoctoServico != null && 
    				tiposDocumento.contains(sinistroDoctoServico.getDoctoServico().getTpDocumentoServico().getValue())) {
	    		Short cdEvento = null;
	    		if (criteria.getDomainValue("tipoCarta").getValue().equals("O")) {
	    			cdEvento = Short.valueOf("53");
	    		} else {
	    			cdEvento = Short.valueOf("54");
	    		}
	    		
	    		String strDoctoServico = sinistroDoctoServico.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial() + " " + StringUtils.leftPad(sinistroDoctoServico.getDoctoServico().getNrDoctoServico().toString(), 8, '0');
	    		this.getIncluirEventosRastreabilidadeInternacionalService().generateEventoDocumento(
	        			cdEvento,
	        			sinistroDoctoServico.getDoctoServico().getIdDoctoServico(),
	        			filialUsuario.getIdFilial(),
	        			strDoctoServico,
	        			dataHoraAtual,
	        			null,
	        			sinistroDoctoServico.getDoctoServico().getFilialByIdFilialOrigem().getSiglaNomeFilial(),
	        			sinistroDoctoServico.getDoctoServico().getTpDocumentoServico().getValue());
			}
    	}
    }
    
     
    public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TPDOCTOSERVICO","DM_TIPO_DOCUMENTO_SERVICO");
	}
    
    public String formatTelefone(String telefone) {
    	return FormatUtils.formatTelefone(telefone.substring(telefone.indexOf('-')+1), telefone.substring(0, telefone.indexOf('-')), null);
    }
}
