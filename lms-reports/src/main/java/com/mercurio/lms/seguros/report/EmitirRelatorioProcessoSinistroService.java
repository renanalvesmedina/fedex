package com.mercurio.lms.seguros.report;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.service.NaturezaProdutoService;
import com.mercurio.lms.util.JTFormatUtils;


/**
 * Classe responsável pela geração do relatorio de processo sinistro
 * 
 * @author
 */

public abstract class EmitirRelatorioProcessoSinistroService extends ReportServiceSupport{

	protected EnderecoPessoaService enderecoPessoaService;
	protected NaturezaProdutoService naturezaProdutoService;
	protected ConfiguracoesFacade configuracoesFacade;
	
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}	
    public NaturezaProdutoService getNaturezaProdutoService() {
		return naturezaProdutoService;
	}
	public void setNaturezaProdutoService(NaturezaProdutoService naturezaProdutoService) {
		this.naturezaProdutoService = naturezaProdutoService;
	}

    
    /**
     * Método que monta a cláusula where para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    protected Map getCriterios(TypedFlatMap criteria, List criterias) { 
    	StringBuffer sql = new StringBuffer();
    	StringBuffer parameterValues = new StringBuffer();
		
		if (!criteria.getString("dtSinistroInicial").equals("")) {
			sql.append("AND TRUNC (CAST(processoSinistro.dh_sinistro AS DATE)) >= ? ");
			criterias.add(criteria.getYearMonthDay("dtSinistroInicial"));
			
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("periodoInicial") + ": " + JTFormatUtils.format(criteria.getYearMonthDay("dtSinistroInicial")) + " ");
		}
		
		if (!criteria.getString("dtSinistroFinal").equals("")) {
			sql.append("AND TRUNC (CAST(processoSinistro.dh_sinistro AS DATE)) <= ? ");
			criterias.add(criteria.getYearMonthDay("dtSinistroFinal"));
			
			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("periodoFinal") + ": " + JTFormatUtils.format(criteria.getYearMonthDay("dtSinistroFinal")) + " ");
		}
		
		if (!criteria.getString("doctoServico.clienteByIdCliente.idCliente").equals("")) {
			sql.append("AND ((doctoservico.ID_CLIENTE_DESTINATARIO = ?) OR (doctoServico.ID_CLIENTE_REMETENTE = ?)) ");
			criterias.add(criteria.getLong("doctoServico.clienteByIdCliente.idCliente"));
			criterias.add(criteria.getLong("doctoServico.clienteByIdCliente.idCliente"));
			
			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("cliente") + ": ");
			parameterValues.append(criteria.getString("doctoServico.clienteByIdCliente.pessoa.nmPessoa"));
		}
		
		if (!criteria.getString("grupoEconomico.idGrupoEconomico").equals("")) {
			sql.append("AND ((clienteRemetente.ID_GRUPO_ECONOMICO = ?) OR (clienteDestinatario.ID_GRUPO_ECONOMICO = ?)) ");
			criterias.add(criteria.getLong("grupoEconomico.idGrupoEconomico"));
			criterias.add(criteria.getLong("grupoEconomico.idGrupoEconomico"));
			
			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("grupoEconomico") + ": ");
			parameterValues.append(criteria.getString("grupoEconomico.dsGrupoEconomico"));
		}
		
		if (!criteria.getString("processoSinistro.idProcessoSinistro").equals("")) {
			sql.append("AND processoSinistro.ID_PROCESSO_SINISTRO = ? ");
			criterias.add(criteria.getLong("processoSinistro.idProcessoSinistro"));

		if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("processoSinistro") + ": ");
			parameterValues.append(criteria.getString("processoSinistro.nrProcessoSinistro"));
		}
		
		if (!criteria.getString("rota.idRota").equals("")) {
			sql.append("AND controleCarga.ID_ROTA = ? ");
			criterias.add(criteria.getLong("rota.idRota"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("rota") + ": ");
			parameterValues.append(criteria.getString("rota.dsRota"));
		}
		
		if (!criteria.getString("manifesto.manifestoViagemNacional.idManifestoViagemNacional").equals("")) {
			sql.append("AND manifesto.ID_MANIFESTO = ? ");
			criterias.add(criteria.getLong("manifesto.manifestoViagemNacional.idManifestoViagemNacional"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("manifesto") + ": ");
			parameterValues.append(
					criteria.getString("manifesto.dsManifesto") + " " + 
					criteria.getString("manifesto.filialByIdFilialOrigem.sgFilial") + " " +
					criteria.getString("manifesto.manifestoViagemNacional.nrManifestoOrigem"));
		}
		
		if (!criteria.getString("controleCarga.idControleCarga").equals("")) {
			sql.append("AND processoSinistro.ID_CONTROLE_CARGA = ? ");
			criterias.add(criteria.getLong("controleCarga.idControleCarga"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("controleCarga") + ": ");
			parameterValues.append(criteria.getString("controleCarga.filialByIdFilialOrigem.sgFilial") + " ");
			if (criteria.getString("controleCarga.nrControleCarga")!=null)
				parameterValues.append(new DecimalFormat("00000000").format(criteria.getLong("controleCarga.nrControleCarga")));
		}
		
		if (!criteria.getString("meioTransporte.idMeioTransporte").equals("")) {
			sql.append("AND processoSinistro.ID_MEIO_TRANSPORTE = ? ");
			criterias.add(criteria.getLong("meioTransporte.idMeioTransporte"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("meioTransporte") + ": ");
			parameterValues.append(criteria.getString("meioTransporte.nrFrota") + "-");
			parameterValues.append(criteria.getString("meioTransporte.nrIdentificador"));
		}
		
		if (!criteria.getString("servico.tpModal").equals("")) {
			sql.append("AND servico.tp_Modal = ? ");
			criterias.add(criteria.getString("servico.tpModal"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("modal") + ": ");
			parameterValues.append(criteria.getString("servico.dsModal"));
		}
		
		if (!criteria.getString("servico.tpAbrangencia").equals("")) {
			sql.append("AND servico.tp_Abrangencia = ? ");
			criterias.add(criteria.getString("servico.tpAbrangencia"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("abrangencia") + ": ");
			parameterValues.append(criteria.getString("servico.dsAbrangencia"));
		}
		
		if (!criteria.getString("tipoSinistro.idTipo").equals("")) {
			sql.append("AND processoSinistro.ID_TIPO_SINISTRO = ? ");
			criterias.add(criteria.getLong("tipoSinistro.idTipo"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("tipoSinistro") + ": ");
			parameterValues.append(criteria.getString("tipoSinistro.dsTipo"));
		}
		
		if (!criteria.getString("tipoSeguro.idTipoSeguro").equals("")) {
			sql.append("AND processoSinistro.ID_TIPO_SEGURO = ? ");
			criterias.add(criteria.getLong("tipoSeguro.idTipoSeguro"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("tipoSeguro") + ": ");
			parameterValues.append(criteria.getString("tipoSeguro.sgTipo"));
		}
		
		if (!criteria.getString("naturezaProduto").equals("")) {
			sql.append("AND conhecimento.ID_NATUREZA_PRODUTO = ? ");
			criterias.add(criteria.getLong("naturezaProduto"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("naturezaProduto") + ": ");
			NaturezaProduto naturezaProduto = getNaturezaProdutoService().findById(criteria.getLong("naturezaProduto"));
			parameterValues.append(naturezaProduto.getDsNaturezaProduto());
		}
		
		if (!criteria.getString("filial.regionalFiliais.idRegionalFilial").equals("")) {
			sql.append("AND regionalFilial.ID_REGIONAL_FILIAL = ? ");
			criterias.add(criteria.getLong("filial.regionalFiliais.idRegionalFilial"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("regional") + ": ");
			parameterValues.append(criteria.getString("filial.regionalFiliais.dsRegional"));
		}
		
		if (!criteria.getString("reguladoraSeguro.idReguladora").equals("")) {
			sql.append("AND processoSinistro.ID_REGULADORA = ? ");
			criterias.add(criteria.getLong("reguladoraSeguro.idReguladora"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("reguladoraSeguro") + ": ");
			parameterValues.append(criteria.getString("reguladoraSeguro.pessoa.nmPessoa"));
		}
		
		if (!criteria.getString("rodovia.idRodovia").equals("")) {
			sql.append("AND processoSinistro.ID_RODOVIA = ? ");
			criterias.add(criteria.getLong("rodovia.idRodovia"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("rodovia") + ": ");
			parameterValues.append(criteria.getString("rodovia.dsRodovia"));
		}
		
		if (!criteria.getString("aeroporto.idAeroporto").equals("")) {
			sql.append("AND processoSinistro.ID_AEROPORTO = ? ");
			criterias.add(criteria.getLong("aeroporto.idAeroporto"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("aeroporto") + ": ");
			parameterValues.append(criteria.getString("aeroporto.pessoa.nmPessoa"));
		}
		
		if (!criteria.getString("filial.idFilial").equals("")) {
			sql.append("AND filial.ID_FILIAL = ? ");
			criterias.add(criteria.getLong("filial.idFilial"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("filial") + ": ");
			parameterValues.append(criteria.getString("filial.pessoa.nmFantasia"));
		}
		
		if (!criteria.getString("municipio.idMunicipio").equals("")) {
			sql.append("AND processoSinistro.ID_MUNICIPIO = ? ");
			criterias.add(criteria.getLong("municipio.idMunicipio"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("municipio") + ": ");
			parameterValues.append(criteria.getString("municipio.nmMunicipio"));
		}
		
		if (!criteria.getString("municipio.unidadeFederativa.idUnidadeFederativa").equals("")) {
			sql.append("AND municipio.ID_UNIDADE_FEDERATIVA = ? ");
			criterias.add(criteria.getLong("municipio.unidadeFederativa.idUnidadeFederativa"));

			if (parameterValues.length()>0) parameterValues.append(" | ");
			parameterValues.append(this.getConfiguracoesFacade().getMensagem("unidadeFederativa") + ": ");
			parameterValues.append(criteria.getString("municipio.unidadeFederativa.sgUnidadeFederativa"));
		}

        Map sqlStuffs = new HashMap();
        sqlStuffs.put("sql", sql.toString());
        sqlStuffs.put("paratersValues", parameterValues.toString());
        return sqlStuffs;         
    }
}