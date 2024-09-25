package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.printform.Alignment;
import com.mercurio.adsm.framework.printform.Configuration;
import com.mercurio.adsm.framework.printform.FormAttributes;
import com.mercurio.adsm.framework.printform.FormHelper;
import com.mercurio.adsm.framework.printform.PrintCommands;
import com.mercurio.adsm.framework.printform.field.NumericField;
import com.mercurio.adsm.framework.printform.field.TextField;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.expedicao.model.AwbEmbalagem;
import com.mercurio.lms.expedicao.model.CtoAwb;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.AwbService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.DoubleUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Impressao Conhecimento Aereo - AWB.
 * @author Andre Valadas
 * @spring.bean id="lms.expedicao.gerarAWBService"
 */
public class GerarAWBService {
	/** Constantes Globais de Controle */
	static final String CD_TAM = "TAM";
	static final String CD_GOL = "GOL";
	static final String CD_VARIG = "VARIG";

	private AwbService awbService;
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ConfiguracoesFacade configuracoesFacade;

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setAwbService(AwbService awbService) {
		this.awbService = awbService;
	}

	/**
	 * Seta Parametros de Formulario de acordo com a Cia Aerea informada.
	 * @param idCiaFilialMercurio
	 * @param params
	 */
	private void initializeParams(Long idCiaFilialMercurio, TypedFlatMap params) {
		/** Defaults */
		params.put("expedidor.initAddRows", Integer.valueOf(15));
		params.put("destinatario.initAddRows", Integer.valueOf(3));
		params.put("baseCiaAerea.initRow", Integer.valueOf(25));
		params.put("baseCiaAerea.addRows", Integer.valueOf(1));

		params.put("pesoTarifas.initAddRows", Integer.valueOf(4));
		params.put("pesoTarifas.psTaxado.initColumn", Integer.valueOf(18));
		params.put("pesoTarifas.trecho.initColumn", Integer.valueOf(25));
		params.put("pesoTarifas.cl.initColumn", Integer.valueOf(33));
		params.put("pesoTarifas.codigo.initColumn", Integer.valueOf(36));
		params.put("pesoTarifas.kg.initColumn", Integer.valueOf(41));
		params.put("pesoTarifas.descricao.initColumn", Integer.valueOf(62));

		params.put("freteTaxas.initAddRows", Integer.valueOf(5));
		params.put("freteTaxas.finalAddRows", Integer.valueOf(5));

		params.put("obsImpostos.initRow", Integer.valueOf(41));
		params.put("obsImpostos.maxLines", Integer.valueOf(8));
		params.put("obsImpostos.x.initColumn", Integer.valueOf(60));
		params.put("obsImpostos.DAC.initColumn", Integer.valueOf(64));

		if(idCiaFilialMercurio.equals(Long.valueOf(configuracoesFacade.getValorParametro("ID_VARIG").toString()))) {
			/** VARIG LOG **/
			params.put("form.ciaAerea", CD_VARIG);
			params.put("notasFiscais.initRow", Integer.valueOf(17));
			params.put("notasFiscais.maxRows", Integer.valueOf(9));

			params.put("freteTaxas.finalAddRows", Integer.valueOf(4));

			params.put("obsImpostos.x.initColumn", Integer.valueOf(59));
		} else if (idCiaFilialMercurio.equals(Long.valueOf(configuracoesFacade.getValorParametro("ID_GOL").toString()))) {
			/** GOL LOG **/
			params.put("form.ciaAerea", CD_GOL);
			params.put("notasFiscais.initRow", Integer.valueOf(13));
			params.put("notasFiscais.maxRows", Integer.valueOf(13));

			params.put("freteTaxas.finalAddRows", Integer.valueOf(6));
			params.put("obsImpostos.x.initColumn", Integer.valueOf(61));
		} else if(idCiaFilialMercurio.equals(Long.valueOf(configuracoesFacade.getValorParametro("ID_TAM").toString()))) {
			/** TAM EXPRESS **/
			params.put("form.ciaAerea", CD_TAM);
			params.put("expedidor.initAddRows", Integer.valueOf(14));
			params.put("notasFiscais.initRow", Integer.valueOf(12));
			params.put("notasFiscais.maxRows", Integer.valueOf(7));

			params.put("baseCiaAerea.initRow", Integer.valueOf(24));

			params.put("pesoTarifas.psTaxado.initColumn", Integer.valueOf(16));
			params.put("pesoTarifas.trecho.initColumn", Integer.valueOf(23));
			params.put("pesoTarifas.cl.initColumn", Integer.valueOf(31));
			params.put("pesoTarifas.codigo.initColumn", Integer.valueOf(32));
			params.put("pesoTarifas.kg.initColumn", Integer.valueOf(35));
			params.put("pesoTarifas.descricao.initColumn", Integer.valueOf(63));

			params.put("obsImpostos.initRow", Integer.valueOf(40));
			params.put("obsImpostos.maxLines", Integer.valueOf(12));
			params.put("obsImpostos.DAC.initColumn", Integer.valueOf(73));
		} else {
			throw new BusinessException("LMS-04023");
		}
	}

	/**
	 * Gera Formulario para Impressao Matricial.
	 * @author Andre Valadas.
	 * @param idAwb
	 * @return String para Impressao.
	 */
	public String generateAWB(Long idAwb) {
		FormAttributes attributes = new FormAttributes(81, 70, PrintCommands.getPageLengthInInches(12));
		FormHelper form = new FormHelper(attributes, new Configuration(LocaleContextHolder.getLocale()));

		/** Carrega AWB + Parametros */
		Awb awb = awbService.findById(idAwb);
		TypedFlatMap params = new TypedFlatMap();
		this.initializeParams(awb.getCiaFilialMercurio().getEmpresa().getIdEmpresa(), params);

		/** Expedidor/Destinatario */
		montaExpedidorDestinatario(awb, form, params);
		/** Notas Fiscais */
		montaNotasFiscais(awb, form, params);
		/** Base CiaAerea */
		montaBaseCiaAerea(awb, form, params);
		/** Peso x Tarifas */
		montaPesoTarifas(awb, form, params);
		/** Frete x Taxas */
		montaFreteTaxas(awb, form, params);
		/** Observacoes + Impostos */
		montaObservacoesImpostos(awb, form, params);
		/** CFOP + Dados do Usuario */
		montaCFOP(awb, form, params);
		return form.write();
	}

	/**
	 * Monta Expedidor/Destinatario
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaExpedidorDestinatario(Awb awb, FormHelper form, TypedFlatMap params) {
		/** Expedidor */
		montaPessoa(awb.getClienteByIdClienteExpedidor().getPessoa(), awb.getInscricaoEstadualExpedidor(), form, params, params.getInteger("expedidor.initAddRows").intValue());
		/** Destinatario */
		montaPessoa(awb.getClienteByIdClienteDestinatario().getPessoa(), awb.getInscricaoEstadualDestinatario(), form, params, params.getInteger("destinatario.initAddRows").intValue());
	}

	/**
	 * Monta Pessoa.
	 * @param pessoa
	 * @param inscricaoEstadual
	 * @param form
	 * @param params
	 * @param initAddRows
	 */
	private void montaPessoa(Pessoa pessoa, InscricaoEstadual inscricaoEstadual, FormHelper form, TypedFlatMap params, int initAddRows) {

		StringBuilder text;
		/** Expedidor/Destinatario */
		EnderecoPessoa enderecoPessoa = findEnderecoPessoa(pessoa.getIdPessoa());
		TelefoneEndereco fone = (TelefoneEndereco) enderecoPessoa.getTelefoneEnderecos().get(0);
		TelefoneEndereco fax = (TelefoneEndereco) enderecoPessoa.getTelefoneEnderecos().get(1);

		form.addRowToDetail(initAddRows);

		/** Identificacao */
		form.addFieldToDetail(new TextField(8, 35, pessoa.getNmPessoa()));
		form.addRowToDetail();

		form.addFieldToDetail(new TextField(9, 34, FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(), pessoa.getNrIdentificacao())));
		form.addRowToDetail();

		form.addFieldToDetail(new TextField(10, 33, inscricaoEstadual.getNrInscricaoEstadual()));
		form.addRowToDetail();

		/** Endereco (Campos Condensados)*/
		text = new StringBuilder();
		text.append(enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro())
			.append(" ")
			.append(enderecoPessoa.getDsEndereco())
			.append(", ")
			.append(enderecoPessoa.getNrEndereco());
		if (StringUtils.isNotBlank(enderecoPessoa.getDsComplemento())) {
			text.append(" - ")
				.append(enderecoPessoa.getDsComplemento());
		}
		form.addFieldToDetail(new TextField(9, 34, text.toString(), true));
		form.addRowToDetail();

		form.addFieldToDetail(new TextField(10, 15, enderecoPessoa.getDsBairro(), true));
		/** Validar Diferentes Formularios */
		if (CD_GOL.equals(params.getString("form.ciaAerea")) || CD_VARIG.equals(params.getString("form.ciaAerea"))) {
			form.addFieldToDetail(new TextField(28, 15, enderecoPessoa.getMunicipio().getNmMunicipio(), true));
			form.addRowToDetail();

			form.addFieldToDetail(new TextField(8, 13, enderecoPessoa.getMunicipio().getNrCep(), true));
			form.addFieldToDetail(new TextField(24, 2, enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa(), true));
			if (fone != null) {
				text = new StringBuilder();
				text.append(StringUtils.defaultString(fone.getNrDdi()))
					.append(" ")
					.append(fone.getDddTelefone());
				form.addFieldToDetail(new TextField(32, 13, text.toString(), true));
			}
			form.addRowToDetail();

			if (fax != null) {
				text = new StringBuilder();
				text.append(StringUtils.defaultString(fax.getNrDdi()))
					.append(" ")
					.append(fax.getDddTelefone());
				form.addFieldToDetail(new TextField(8, 35, text.toString(), true));
			}
		} else
		if(CD_TAM.equals(params.getString("form.ciaAerea"))) {
			form.addFieldToDetail(new TextField(31, 12, enderecoPessoa.getMunicipio().getNrCep(), true));
			form.addRowToDetail();

			form.addFieldToDetail(new TextField(8, 26, enderecoPessoa.getMunicipio().getNmMunicipio(), true));
			form.addFieldToDetail(new TextField(37, 2, enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa(), true));
			form.addRowToDetail();

			if (fone != null) {
				text = new StringBuilder();
				text.append(StringUtils.defaultString(fone.getNrDdi()))
					.append(" ")
					.append(fone.getDddTelefone());
				form.addFieldToDetail(new TextField(8, 16, text.toString(), true));
			}
			if (fax != null) {
				text = new StringBuilder();
				text.append(StringUtils.defaultString(fax.getNrDdi()))
					.append(" ")
					.append(fax.getDddTelefone());
				form.addFieldToDetail(new TextField(28, 16, text.toString(), true));
			}
		}
		form.addRowToDetail();
	}

	/**
	 * Monta Notas Fiscais.
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaNotasFiscais(Awb awb, FormHelper form, TypedFlatMap params) {
		int initRow = params.getInteger("notasFiscais.initRow").intValue();
		int maxRows = params.getInteger("notasFiscais.maxRows").intValue();

		StringBuilder text = new StringBuilder();
		int initColumn = 45;
		int numMaxNF = 3;
		int rowCount = 1;
		int nfCount = 0;

		for (Iterator iter = awb.getCtoAwbs().iterator(); iter.hasNext();) {
			CtoAwb ctoAwb = (CtoAwb) iter.next();
			List notasFiscais = ctoAwb.getConhecimento().getNotaFiscalConhecimentos();
			for (Iterator iterator = notasFiscais.iterator(); iterator.hasNext();) {
				/** Valida Maximo de Linhas */
				if (rowCount == maxRows) {
					break;
				}
				NotaFiscalConhecimento nf = (NotaFiscalConhecimento) iterator.next();

				text.append(FormatUtils.formatDecimal("000000000", nf.getNrNotaFiscal()));
				if (iterator.hasNext()) {
					text.append(", ");
				}
				form.addFieldToDetail(initRow, new TextField(initColumn, 11, text.toString()));
				text = new StringBuilder();
				/** Posicao para proximo numero */
				initColumn += 11;
				nfCount++;
				/** Valida Quebra de Linha */
				if (nfCount == numMaxNF) {
					initRow++;
					rowCount++;
					nfCount = 0;
					//*** Muda para 45 pois existem caracteres reduzidos na mesma linha...
					initColumn = 45;
				}
			}
		}
	}

	/**
	 * Base do AWB.
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaBaseCiaAerea(Awb awb, FormHelper form, TypedFlatMap params) {
		int initRow = params.getInteger("baseCiaAerea.initRow").intValue();

		Pessoa pessoaCiaAerea = awb.getCiaFilialMercurio().getEmpresa().getPessoa();
		if (CD_TAM.equals(params.getString("form.ciaAerea"))) {
			form.addFieldToDetail(initRow, new TextField(44, 15, "TAM EXPRESS"));
		}
		form.addFieldToDetail(initRow+2, new TextField(63, 18, awb.getNrAwb()+"-"+awb.getDvAwb()));

		List inscricaoEstadual = pessoaCiaAerea.getInscricaoEstaduais();
		if (inscricaoEstadual != null && !inscricaoEstadual.isEmpty()) {
			InscricaoEstadual ie = (InscricaoEstadual) inscricaoEstadual.get(0);
			form.addFieldToDetail(initRow+4, new TextField(45, 16, ie.getNrInscricaoEstadual()));
		}
		form.addFieldToDetail(initRow+4, new TextField(63, 18, FormatUtils.formatIdentificacao(pessoaCiaAerea.getTpIdentificacao(), pessoaCiaAerea.getNrIdentificacao())));

		form.addFieldToDetail(initRow+6, new TextField(44, 16, "SEGURO PRÓPRIO"));
		form.addFieldToDetail(initRow+6, new NumericField(63, 12, DoubleUtils.ZERO));
		form.addRowToDetail(params.getInteger("baseCiaAerea.addRows").intValue());

		form.addFieldToDetail(new TextField(6, 3, awb.getAeroportoByIdAeroportoOrigem().getSgAeroporto()));
		if (awb.getAeroportoByIdAeroportoEscala() != null) {
			form.addFieldToDetail(new TextField(13, 3, awb.getAeroportoByIdAeroportoEscala().getSgAeroporto()));
		}
		form.addFieldToDetail(new TextField(20, 15, awb.getAeroportoByIdAeroportoDestino().getPessoa().getEnderecoPessoa().getMunicipio().getNmMunicipio()));
		form.addFieldToDetail(new TextField(37, 3, awb.getAeroportoByIdAeroportoDestino().getSgAeroporto()));
		List embalagens = awb.getAwbEmbalagems();
		if (embalagens != null && !embalagens.isEmpty()) {
			AwbEmbalagem awbEmbalagem = (AwbEmbalagem) embalagens.get(0);
			StringBuilder text = new StringBuilder();
			text.append(awbEmbalagem.getEmbalagem().getDsEmbalagem().getValue())
				.append(" ")
				.append(awbEmbalagem.getEmbalagem().getNrAltura())
				.append("x")
				.append(awbEmbalagem.getEmbalagem().getNrLargura())
				.append("x")
				.append(awbEmbalagem.getEmbalagem().getNrComprimento());
			form.addFieldToDetail(new TextField(44, 37, text.toString()));
		}
	}

	/**
	 * Monta Peso x Tarifas.
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaPesoTarifas(Awb awb, FormHelper form, TypedFlatMap params) {
		int psTaxadoInitColumn = params.getInteger("pesoTarifas.psTaxado.initColumn").intValue();
		int trechoInitColumn = params.getInteger("pesoTarifas.trecho.initColumn").intValue();
		int clInitColumn = params.getInteger("pesoTarifas.cl.initColumn").intValue();
		int codigoInitColumn = params.getInteger("pesoTarifas.codigo.initColumn").intValue();
		int kgInitColumn = params.getInteger("pesoTarifas.kg.initColumn").intValue();
		int descricaoInitColumn = params.getInteger("pesoTarifas.descricao.initColumn").intValue();

		form.addRowToDetail(params.getInteger("pesoTarifas.initAddRows").intValue());

		BigDecimal valor = awb.getPsTotal();
		if(awb.getPsCubado() != null && !BigDecimal.ZERO.equals(awb.getPsCubado())){
			valor = awb.getPsCubado().compareTo(awb.getPsTotal()) > 0 ? awb.getPsCubado() : awb.getPsTotal();
		}

		form.addFieldToDetail(new TextField(5, 4, String.valueOf(awb.getQtVolumes()), Alignment.ALIGN_RIGHT));
		form.addFieldToDetail(new NumericField(10, 6, awb.getPsTotal()));
		
		form.addFieldToDetail(new NumericField(psTaxadoInitColumn, 6, valor));

		form.addFieldToDetail(new TextField(trechoInitColumn, 7, awb.getAeroportoByIdAeroportoOrigem().getSgAeroporto()+"/"+awb.getAeroportoByIdAeroportoDestino().getSgAeroporto()));
		form.addFieldToDetail(new TextField(clInitColumn, 2, "G"));
		if (awb.getProdutoEspecifico() != null) {
			form.addFieldToDetail(new NumericField(codigoInitColumn, 4, awb.getProdutoEspecifico().getNrTarifaEspecifica()));
		}

			form.addFieldToDetail(new NumericField(kgInitColumn, 7, BigDecimalUtils.divide(awb.getVlFretePeso(), valor)));

		form.addFieldToDetail(new NumericField(49, 11, awb.getVlFretePeso()));
		if (awb.getNaturezaProduto() != null) {
			form.addFieldToDetail(new TextField(descricaoInitColumn, 18, awb.getNaturezaProduto().getDsNaturezaProduto().getValue()));
		}
	}

	/**
	 * Monta Frete x Taxas.
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaFreteTaxas(Awb awb, FormHelper form, TypedFlatMap params) {
		form.addRowToDetail(params.getInteger("freteTaxas.initAddRows").intValue());

		int initColumn = 6;
		/** Valida Tipo de Frete */
		if (ConstantesExpedicao.TP_FRETE_FOB.equals(awb.getTpFrete().getValue())) {
			initColumn = 30;
		}

		/** Frete Nacional */
		form.addFieldToDetail(new NumericField(initColumn, 12, awb.getVlFretePeso()));
		form.addRowToDetail(2);

		/** Frete Regional */
		if (CD_VARIG.equals(params.getString("form.ciaAerea"))) {
			form.addFieldToDetail(new NumericField(initColumn, 12, BigDecimalUtils.ZERO));
			form.addRowToDetail(2);
		}

		/** AD Valorem */
		form.addFieldToDetail(new TextField(initColumn, 12, "SEGURO PRÓPRIO", true));
		form.addRowToDetail(2);

		/** Taxa Terrestre Origem */
		form.addFieldToDetail(new NumericField(initColumn, 12, BigDecimalUtils.ZERO));
		form.addRowToDetail(2);

		/** Taxa Terrestre Destino */
		form.addFieldToDetail(new NumericField(initColumn, 12, BigDecimalUtils.defaultBigDecimal(awb.getVlTaxaTerrestre(), BigDecimalUtils.ZERO)));
		form.addRowToDetail(2);

		/** Taxa Redespacho */
		form.addFieldToDetail(new NumericField(initColumn, 12, BigDecimalUtils.ZERO));
		form.addRowToDetail(2);

		/** Taxa Devida ao Agente */
		form.addFieldToDetail(new NumericField(initColumn, 12, BigDecimalUtils.ZERO));
		form.addRowToDetail(2);

		/** Taxa Devida ao Transportador */
		form.addFieldToDetail(new NumericField(initColumn, 12, BigDecimalUtils.ZERO));

		/** Taxa Combustivel */
		if (BigDecimalUtils.hasValue(awb.getVlTaxaCombustivel())){
			form.addRowToDetail(1);
			form.addFieldToDetail(new TextField(19, 12, "TAXA DE", Alignment.ALIGN_CENTER, true));
			form.addRowToDetail(1);
			form.addFieldToDetail(new NumericField(initColumn, 12, awb.getVlTaxaCombustivel()));
			form.addFieldToDetail(new TextField(19, 12, "COMBUSTIVEL", Alignment.ALIGN_CENTER, true));
		} else {
			form.addRowToDetail(2);
		}
		form.addRowToDetail(4);
		/** Frete Total + Taxas */
		form.addFieldToDetail(new NumericField(initColumn, 12, awb.getVlFrete()));
		form.addRowToDetail(params.getInteger("freteTaxas.finalAddRows").intValue());
	}

	/**
	 * Monta Observacoes + Impostos.
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaObservacoesImpostos(Awb awb, FormHelper form, TypedFlatMap params) {
		int initRow = params.getInteger("obsImpostos.initRow").intValue();
		int maxLines = params.getInteger("obsImpostos.maxLines").intValue();
		int xInitColumn = params.getInteger("obsImpostos.x.initColumn").intValue();
		int dacInitColumn = params.getInteger("obsImpostos.DAC.initColumn").intValue();
		int lineCount = 1;

		/** Observacoes */
		String observacao = awb.getObAwb();
		if (StringUtils.isNotBlank(observacao)) {
			StringBuilder text = new StringBuilder();
			int charCount = 0;
			int maxChars = 55;
			char[] obs = observacao.toCharArray();
			for (int i = 0; i < obs.length; i++) {
				text.append(obs[i]);
				charCount++;
				/** Valida maximo de caracteres */
				if (charCount == maxChars) {
					form.addFieldToDetail(initRow+lineCount, new TextField(45, 32, text.toString(), true));
					form.addRowToDetail();
					text = new StringBuilder();
					lineCount++;
					charCount = 0;
				}
				/** Valida maximo de linhas */
				if (lineCount > maxLines) {
					break;
				}
			}
			if (StringUtils.isNotBlank(text.toString())) {
				form.addFieldToDetail(initRow+lineCount, new TextField(45, 32, text.toString().trim(), true));
			}
		}
		initRow += (maxLines-lineCount);
		int auxIndex = (CD_TAM.equals(params.getString("form.ciaAerea")) ? -1 : 0);

		/** Retirada no Aeroporto */
		form.addFieldToDetail(initRow+=2, new TextField(xInitColumn, 2, "XX"));
		/** Local da Retirada */
		form.addFieldToDetail(initRow+=1, new TextField(55, 18, "RETIRADA AEROPORTO"));
		/** Valor do Servico */
		form.addFieldToDetail(59, new NumericField(65, 12, awb.getVlFrete()));
		/** Base de Calculo */
		form.addFieldToDetail(61+auxIndex, new NumericField(65, 12, awb.getVlBaseCalcImposto()));
		/** Aliquota ICMS */
		form.addFieldToDetail(62+auxIndex, new NumericField(65, 12, awb.getPcAliquotaICMS()));

		/** Loja / Agente Emissor */
		form.addFieldToDetail(64+auxIndex, new TextField(45, 18, String.valueOf(configuracoesFacade.getValorParametro(awb.getFilialByIdFilialOrigem().getIdFilial(), "LOJA_AG_EMISSOR_AWB"))));
		/** Codigo DAC */
		form.addFieldToDetail(64+auxIndex, new TextField(dacInitColumn, 5, StringUtils.defaultString(String.valueOf(SessionUtils.getEmpresaSessao().getNrDac())), Alignment.ALIGN_RIGHT));

		/** Data Emissao */
		form.addFieldToDetail(66+auxIndex, new TextField(45, 18, JTFormatUtils.format(awb.getDhEmissao(), JTFormatUtils.DEFAULT, JTFormatUtils.YEARMONTHDAY)));
		/** Hora Emissao */
		form.addFieldToDetail(66+auxIndex, new TextField(64, 17, JTFormatUtils.format(awb.getDhEmissao(), JTFormatUtils.DEFAULT, JTFormatUtils.TIMEOFDAY)));
	}

	/**
	 * Monta CFOP + Dados do Usuario.
	 * @param awb
	 * @param form
	 * @param params
	 */
	private void montaCFOP(Awb awb, FormHelper form, TypedFlatMap params) {
		form.addRowToDetail(2);

		if (CD_VARIG.equals(params.getString("form.ciaAerea")) || CD_GOL.equals(params.getString("form.ciaAerea"))) {
			/** Natureza de Operacao */
			form.addFieldToDetail(new TextField(5, 14, String.valueOf(configuracoesFacade.getValorParametro("NATUREZA_OPERACAO_AWB"))));
			/** CFOP */
			form.addFieldToDetail(new TextField(22, 5, String.valueOf(configuracoesFacade.getValorParametro("CFOP_AWB"))));
			/** Emissor */
			form.addFieldToDetail(new TextField(28, 35, SessionUtils.getUsuarioLogado().getNmUsuario()));
			/** Localidade */
			form.addFieldToDetail(new TextField(65, 3, awb.getFilialByIdFilialOrigem().getSgFilial()));
			/** Matricula */
			form.addFieldToDetail(new TextField(71, 9, SessionUtils.getUsuarioLogado().getNrMatricula()));
		} else
		if (CD_TAM.equals(params.getString("form.ciaAerea"))) {
			/** Natureza de Operacao */
			form.addFieldToDetail(new TextField(5, 21, String.valueOf(configuracoesFacade.getValorParametro("NATUREZA_OPERACAO_AWB"))));
			/** CFOP */
			form.addFieldToDetail(new TextField(29, 5, String.valueOf(configuracoesFacade.getValorParametro("CFOP_AWB"))));
			/** Emissor */
			form.addFieldToDetail(new TextField(37, 25, SessionUtils.getUsuarioLogado().getNmUsuario()));
			/** Localidade */
			form.addFieldToDetail(new TextField(64, 3, awb.getFilialByIdFilialOrigem().getSgFilial()));
			/** Matricula */
			form.addFieldToDetail(new TextField(71, 9, SessionUtils.getUsuarioLogado().getNrMatricula()));
		}
	}

	/**
	 * Busca Endereco + Fax + Telefones por tipo de Uso.
	 * @param idPessoa
	 * @return
	 */
	private EnderecoPessoa findEnderecoPessoa(Long idPessoa) {
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		if (enderecoPessoa != null) {
			TelefoneEndereco fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FO");
			if(fone == null) {
				fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FF");
			}
			TelefoneEndereco fax = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FA");
			List telefonesEndereco = new ArrayList(2);
			telefonesEndereco.add(fone);
			telefonesEndereco.add(fax);
			enderecoPessoa.setTelefoneEnderecos(telefonesEndereco);
		} else {
			enderecoPessoa = new EnderecoPessoa();
			enderecoPessoa.setTelefoneEnderecos(new ArrayList(2));
		}
		return enderecoPessoa;
	}
}