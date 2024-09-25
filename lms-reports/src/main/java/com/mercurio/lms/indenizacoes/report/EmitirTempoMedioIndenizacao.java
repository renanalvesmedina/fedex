package com.mercurio.lms.indenizacoes.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe abstrata que contém as funcionalidades em comum do relatório de tempo
 * médio de indenizações - detalhado/resumido
 * 
 * @author Rodrigo Antunes
 */
public abstract class EmitirTempoMedioIndenizacao extends ReportServiceSupport {

	private EnderecoPessoaService enderecoPessoaService;

	private ConversaoMoedaService conversaoMoedaService;

	private ConversaoMoedaService getConversaoMoedaService() {
		return conversaoMoedaService;
	}

	public void setConversaoMoedaService(
			ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}

	private EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(
			EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	protected abstract void addProjection(SqlTemplate sql);

	protected void addFrom(SqlTemplate sql) {
		sql.addFrom("filial", "fnc");
		sql.addFrom("filial", "fo_ds");
		sql.addFrom("docto_servico_indenizacao", "dsi");
		sql.addFrom("docto_servico", "ds");
		sql.addFrom("moeda", "mds");
		sql.addFrom("servico");
		sql.addFrom("cliente", "cr");
		sql.addFrom("cliente", "cd");
		sql.addFrom("pessoa", "pr");
		sql.addFrom("pessoa", "pd");
		sql.addFrom("nao_conformidade", "nc");
		sql.addFrom("ocorrencia_nao_conformidade", "onc");
		sql.addFrom("motivo_abertura_nc", "manc");

		sql.addFrom("moeda", "mrim");
		sql.addFrom("recibo_indenizacao", "rim");
		sql.addFrom("filial", "f_rim");
		sql.addFrom("regional_filial", "rf");
		sql.addFrom("regional");
		sql.addFrom("processo_sinistro", "ps");
		sql.addFrom("tipo_seguro", "ts");
		sql.addFrom("pessoa", "beneficiario");
		sql.addFrom("pessoa", "favorecido");
	}

	protected void addWhere(SqlTemplate sql) {
		sql.addJoin("rim.ID_FILIAL", "f_rim.ID_FILIAL");
		sql.addJoin("f_rim.ID_FILIAL", "rf.ID_FILIAL(+)");
		sql.addJoin("rf.ID_REGIONAL", "regional.ID_REGIONAL(+)");
		sql.addJoin("rim.ID_PROCESSO_SINISTRO", "ps.ID_PROCESSO_SINISTRO(+)");
		sql.addJoin("ps.ID_TIPO_SEGURO", "ts.ID_TIPO_SEGURO(+)");
		sql.addJoin("rim.ID_BENEFICIARIO", "beneficiario.ID_PESSOA(+)");
		sql.addJoin("rim.ID_FAVORECIDO", "favorecido.ID_PESSOA(+)");
		sql.addJoin("rim.ID_MOEDA", "mrim.ID_MOEDA(+)");
		// --Docto Servico
		// *****************************************************/
		sql.addJoin("ds.ID_MOEDA", "mds.ID_MOEDA(+)");
		sql.addJoin("rim.ID_RECIBO_INDENIZACAO","dsi.ID_RECIBO_INDENIZACAO(+)");
		sql.addJoin("dsi.ID_DOCTO_SERVICO", "ds.ID_DOCTO_SERVICO(+)");
		sql.addJoin("ds.ID_FILIAL_ORIGEM", "fo_ds.ID_FILIAL(+)");
		sql.addJoin("ds.ID_SERVICO", "servico.ID_SERVICO(+)");
		sql.addJoin("ds.ID_CLIENTE_REMETENTE", "cr.ID_CLIENTE(+)");
		sql.addJoin("cr.ID_CLIENTE", "pr.ID_PESSOA(+)");
		sql.addJoin("ds.ID_CLIENTE_DESTINATARIO", "cd.ID_CLIENTE(+)");
		sql.addJoin("cd.ID_CLIENTE", "pd.ID_PESSOA(+)");
		sql.addJoin("ds.ID_DOCTO_SERVICO", "nc.ID_DOCTO_SERVICO(+)");
		sql.addJoin("nc.ID_FILIAL", "fnc.ID_FILIAL(+)");
		sql.addJoin("nc.ID_NAO_CONFORMIDADE", "onc.ID_NAO_CONFORMIDADE(+)");
		sql.addJoin("onc.ID_MOTIVO_ABERTURA_NC","manc.ID_MOTIVO_ABERTURA_NC(+)");
	}

	protected void addCriteria(SqlTemplate sql, TypedFlatMap tfm) {
		sql.addCriteria("rim.TP_STATUS_INDENIZACAO", "=", "P");

		YearMonthDay dtInicial = tfm.getYearMonthDay("dtInicial");
		sql.addCriteria("rim.DT_GERACAO", ">=", dtInicial, YearMonthDay.class);
		sql.addFilterSummary("periodoInicial", dtInicial);

		YearMonthDay dtFinal = tfm.getYearMonthDay("dtFinal");
		sql.addCriteria("rim.DT_GERACAO", "<=", dtFinal, YearMonthDay.class);
		sql.addFilterSummary("periodoFinal", dtFinal);

		// Filial
		Long idFilial = tfm.getLong("filial.idFilial");
		if (idFilial != null) {
			sql.addCriteria("rim.ID_FILIAL", "=", idFilial);
			String str = tfm.getString("filial.sgFilial");
			sql.addFilterSummary("filial", str);
		}

		// Regional
		Long idRegionalFilial = tfm.getLong("regional.idRegionalFilial");
		if (idRegionalFilial != null) {
			sql.addCriteria("regional.ID_REGIONAL", "=", idRegionalFilial);
			String str = tfm.getString("sgAndDsRegionalHidden");
			sql.addFilterSummary("regional", str);
		}
		

		//adicionando as criticas da vigencia da filial
		sql.addCriteria("rf.DT_VIGENCIA_INICIAL(+)", "<=", JTDateTimeUtils.getDataAtual());
		sql.addCriteria("rf.DT_VIGENCIA_FINAL(+)", ">=", JTDateTimeUtils.getDataAtual());

	
		// Modal
		DomainValue tpModal = tfm.getDomainValue("tpModal");
		if (!"".equals(tpModal.getValue())) {
			sql.addCriteria("servico.TP_MODAL", "=", tpModal.getValue());
			sql.addFilterSummary("modal", tfm.getString("tpModalHidden"));
		}

		// Abrangencia
		DomainValue tpAbrangencia = tfm.getDomainValue("tpAbrangencia");
		if (!"".equals(tpAbrangencia.getValue())) {
			sql.addCriteria("servico.TP_ABRANGENCIA", "=", tpAbrangencia.getValue());
			sql.addFilterSummary("abrangencia", tfm.getString("tpAbrangenciaHidden"));
		}

		// tpIndenizacao
		DomainValue tpIndenizacao = tfm.getDomainValue("tpIndenizacao");
		if (!"".equals(tpIndenizacao.getValue())) {
			sql.addCriteria("rim.TP_INDENIZACAO", "=", tpIndenizacao.getValue());
			sql.addFilterSummary("tipoIndenizacao", tfm.getString("tpIndenizacaoHidden"));
		}
		// Motivo
		Long idMotivo = tfm.getLong("motivoAberturaNc.idMotivoAberturaNc");
		if (idMotivo != null) {
			String str = tfm.getString("dsMotivoAberturaHidden");
			sql.addFilterSummary("motivoNaoConformidade", str);
			sql.addCriteria("manc.ID_MOTIVO_ABERTURA_NC", "=", idMotivo);
		}

		// TipoSeguro
		Long idTipoSeguro = tfm.getLong("tipoSeguro.idTipoSeguro");
		if (idTipoSeguro != null) {
			sql.addCriteria("ts.ID_TIPO_SEGURO", "=", idTipoSeguro);
			String str = tfm.getString("dsTipoSeguroHidden");
			sql.addFilterSummary("tipoSeguro", str);
		}

		// TipoSinistro
		Long idTipoSinistro = tfm.getLong("tipoSinistro.idTipo");
		if (idTipoSinistro != null) {
			sql.addFrom("tipo_sinistro");
			sql.addJoin("ps.id_tipo_sinistro","tipo_sinistro.id_tipo_sinistro");
			sql.addCriteria("tipo_sinistro.id_tipo_sinistro", "=",idTipoSinistro);
			String str = tfm.getString("dsTipoSinistroHidden");
			sql.addFilterSummary("tipoSinistro", str);
		}

		Long idMoeda = tfm.getLong("moeda.idMoeda");
		if (idMoeda != null) {
			sql.addCriteria("rim.id_moeda", "=", idMoeda);
			sql.addFilterSummary("moeda", tfm.getString("moedaHidden"));

			BigDecimal vlInicial = tfm.getBigDecimal("vlInicial");
			if (vlInicial != null) {
				sql.addCriteria("rim.vl_indenizacao", ">=", vlInicial);
				sql.addFilterSummary("valorInicial", FormatUtils.formatDecimal("#,##0.00", vlInicial, true));
			}

			BigDecimal vlFinal = tfm.getBigDecimal("vlFinal");
			if (vlFinal != null) {
				sql.addCriteria("rim.vl_indenizacao", "<=", vlFinal);
				sql.addFilterSummary("valorFinal", FormatUtils.formatDecimal("#,##0.00", vlFinal, true));
			}
		}
	}

	protected void addOrderBy(SqlTemplate sql) {
		sql.addOrderBy("regional.SG_REGIONAL");
		sql.addOrderBy("f_rim.SG_FILIAL");
		sql.addOrderBy("rim.TP_INDENIZACAO");
		sql.addOrderBy("DESC_TIPO_INDENIZACAO");
		sql.addOrderBy("rim.NR_RECIBO_INDENIZACAO");
	}

	public void configReportDomains(ReportDomainConfig config) {
		config.configDomainField("TP_INDENIZACAO", "DM_TIPO_INDENIZACAO");
		config.configDomainField("MODAL", "DM_MODAL");
		config.configDomainField("ABRANGENCIA", "DM_ABRANGENCIA");
		config.configDomainField("TP_STATUS_INDENIZACAO","DM_STATUS_INDENIZACAO");
		config.configDomainField("TP_DOCUMENTO_SERVICO","DM_TIPO_DOCUMENTO_SERVICO");
	}

	public BigDecimal executeRetornaValorConvertido(Long idFilial,
			Long idMoedaOrigem, BigDecimal valor) {
		BigDecimal retorno = null;

		if (valor != null) {
			Long idPaisDestino = SessionUtils.getPaisSessao().getIdPais();
			Long idMoedaDestino = SessionUtils.getMoedaSessao().getIdMoeda();
			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
			EnderecoPessoa enderecoPessoaPadrao = getEnderecoPessoaService().findEnderecoPessoaPadrao(idFilial);
			Long idPaisOrigem = enderecoPessoaPadrao.getMunicipio().getUnidadeFederativa().getPais().getIdPais();

			retorno = getConversaoMoedaService().findConversaoMoeda(idPaisOrigem, idMoedaOrigem, idPaisDestino, idMoedaDestino,dataAtual, valor);

		} else {
			retorno = new BigDecimal(0);
		}

		return retorno;
	}

	public Integer calculaMediaDias(Integer a, Integer b) {
		Integer retorno = Integer.valueOf(0);
		
		if (a!=null && b!=null) {
			BigDecimal c = new BigDecimal( a.intValue() );
			BigDecimal d = new BigDecimal( b.intValue() );
			
			if ( !c.equals( BigDecimalUtils.ZERO )  && !d.equals( BigDecimalUtils.ZERO ) ) {
				
				BigDecimal resultado = BigDecimalUtils.divide(c, d); 
				resultado = BigDecimalUtils.round(resultado);
				retorno = Integer.valueOf( resultado.intValue() );
			}
			
		}
		
		
		return retorno;
	}
	
	
	
}
