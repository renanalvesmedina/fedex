package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.printform.Configuration;
import com.mercurio.adsm.framework.printform.FormAttributes;
import com.mercurio.adsm.framework.printform.FormHelper;
import com.mercurio.adsm.framework.printform.PrintCommands;
import com.mercurio.adsm.framework.printform.field.Field;
import com.mercurio.adsm.framework.printform.field.Label;
import com.mercurio.adsm.framework.printform.field.LabelledNumericField;
import com.mercurio.adsm.framework.printform.field.LabelledTextField;
import com.mercurio.adsm.framework.printform.field.NumericField;
import com.mercurio.adsm.framework.printform.field.TextField;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.service.DadosComplementoService;
import com.mercurio.lms.expedicao.model.service.ParcelaDoctoServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.util.VendasUtils;

/**
 * @author Claiton Grings 
 * @spring.bean id="lms.expedicao.gerarCTRService"
 */
public class GerarCTRService {
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ConfiguracoesFacade configuracoesFacade;
	private DadosComplementoService dadosComplementoService;
	private ParcelaDoctoServicoService parcelaDoctoServicoService;
	private FilialService filialService;
	private InscricaoEstadualService inscricaoEstadualService;

	private static final Pattern patternSpaceChar = Pattern.compile(" ");
	
	public String generateCTR(Conhecimento conhecimento) {
		if(conhecimento.getNrSeloFiscal() != null) {
			return gerarCTRComSeloFiscal(conhecimento);
		} else {
			return gerarCTRSemSeloFiscal(conhecimento);
		}
	}

	private String gerarCTRSemSeloFiscal(Conhecimento conhecimento) {
		Map params = new HashMap();
		params.put("qtLinhasComposicaoFrete", "12");
		params.put("nrLinhaTributacao", "28");
		params.put("nrLinhaNotasFiscais", "32");
		params.put("qtLinhasNotasFiscais", "4");
		params.put("qtColunasObservacao", "53");

		return montaCTR(conhecimento, params);
	}

	private String gerarCTRComSeloFiscal(Conhecimento conhecimento) {
		Map params = new HashMap();
		params.put("qtLinhasComposicaoFrete", "7");
		params.put("nrLinhaTributacao", "23");
		params.put("nrLinhaNotasFiscais", "27");
		params.put("qtLinhasNotasFiscais", "3");
		params.put("qtColunasObservacao", "41");

		return montaCTR(conhecimento, params);
	}

	private String montaCTR(Conhecimento conhecimento, Map parameters) {

		FormAttributes attributes = new FormAttributes(80, 47, PrintCommands.getPageLengthInInches(6), PrintCommands.getLineSpace_1_8_Inch());
		FormHelper form = new FormHelper(attributes, new Configuration(LocaleContextHolder.getLocale()));

		montaCabecalho(conhecimento, form);
		montaRemetente(conhecimento, form);
		montaDestinatario(conhecimento, form);
		montaDevedor(conhecimento, form);
		montaConsignatarioRedespachoLocalEntrega(conhecimento, form);
		montaCargaTransportadaOutrosDados(conhecimento, form);

		int qtLinhasComposicaoFrete = Integer.parseInt(parameters.get("qtLinhasComposicaoFrete").toString());
		String linhaOutrasParcelas = montaComposicaoFrete(conhecimento, form, qtLinhasComposicaoFrete);
		
		int colunasObservacao = Integer.parseInt(parameters.get("qtColunasObservacao").toString());
		montaObservacao(conhecimento, form, colunasObservacao, linhaOutrasParcelas);

		for(int i = 0; i < qtLinhasComposicaoFrete - 2; i++) {
			escreveCancelado(15 + i, 56, 24, conhecimento.getTpSituacaoConhecimento().getValue(), form);
		}
		
		int nrLinhaTributacao = Integer.parseInt(parameters.get("nrLinhaTributacao").toString());
		montaTributacao(conhecimento, form, nrLinhaTributacao);

		int nrLinhaNotasFiscais = Integer.parseInt(parameters.get("nrLinhaNotasFiscais").toString());
		int qtLinhasNotasFiscais = Integer.parseInt(parameters.get("qtLinhasNotasFiscais").toString());
		montaNotasFiscais(conhecimento, form, nrLinhaNotasFiscais, qtLinhasNotasFiscais);

		if(conhecimento.getNrSeloFiscal() != null) {
			montaSeloFiscal(conhecimento, form);
		}

		return form.write();
	}

	private void montaCabecalho(Conhecimento conhecimento, FormHelper form) {
		Field field;

		form.addRowToDetail(3);

		montaEmissor(conhecimento, form);
		// coloca na setima linha
		form.addFieldToDetail(7, new TextField(71, 2, conhecimento.getTpConhecimento().getValue())); //Tipo CTR
		form.addFieldToDetail(7, new TextField(78, 3, conhecimento.getServico().getTpModal().getDescription().getValue())); //Modal

		form.addRowToDetail();
		BigDecimal fech = gerarNumeroFechamento(conhecimento);
		form.addFieldToDetail(new LabelledTextField(48, 20, fech.toString(), new Label(6, "FECH")));

		form.addRowToDetail(3);

		String nrConhecimento = FormatUtils.formatDecimal("00000000", conhecimento.getNrConhecimento());
		form.addFieldToDetail(new TextField(3, 3, conhecimento.getFilialByIdFilialOrigem().getSgFilial()));
		form.addFieldToDetail(new TextField(7, 2, nrConhecimento.substring(0, 2)));
		form.addFieldToDetail(new TextField(10, 8, nrConhecimento.substring(2) + " " + conhecimento.getDvConhecimento().toString()));

		form.addFieldToDetail(new TextField(19, 3, conhecimento.getFilialByIdFilialDestino().getSgFilial())); //destino
		RotaColetaEntrega rotaColetaEntregaSugerida = conhecimento.getRotaColetaEntregaByIdRotaColetaEntregaSugerid();
		String text = "";
		if(rotaColetaEntregaSugerida != null) {
			text = StringUtils.leftPad(rotaColetaEntregaSugerida.getNrRota().toString(), 3, '0');
		}
		form.addFieldToDetail(new TextField(23, 3, text)); //Rota Entrega
		String dhEmissao = DateTimeFormat.forPattern("dd/MM/yy HH:mm").print(conhecimento.getDhEmissao());
		form.addFieldToDetail(new TextField(28, 14, dhEmissao)); //Data e hora da Emissao
		if(conhecimento.getNrCfop() != null) {
			form.addFieldToDetail(new TextField(43, 5, conhecimento.getNrCfop().toString())); // CFOP
		}
		if("C".equals(conhecimento.getTpFrete().getValue())) {
			form.addFieldToDetail(new TextField(48, 2, "XX")); //CIF
		} else {
			form.addFieldToDetail(new TextField(55, 2, "XX")); //FOB
		}
		text = JTFormatUtils.format(conhecimento.getDtPrevEntrega());
		form.addFieldToDetail(new TextField(58, 13, text)); // DPE
		form.addFieldToDetail(new TextField(73, 8, conhecimento.getServico().getTipoServico().getDsTipoServico().getValue())); //Tipo de servico
	}

	/**
	 * Monta os dados do emisssor do conhecimento para impressão
	 * 
	 * @param conhecimento
	 * @param form
	 */
	private void montaEmissor(Conhecimento conhecimento, FormHelper form) {
		
		Pessoa pessoa = conhecimento.getFilialByIdFilialOrigem().getPessoa();
		
		EnderecoPessoa enderecoPessoa = findEnderecoPessoa(pessoa);
		
		List<TelefoneEndereco> fones = enderecoPessoa.getTelefoneEnderecos();

		InscricaoEstadual inscricaoEstadual = inscricaoEstadualService.findByPessoaIndicadorPadrao(pessoa.getIdPessoa(), Boolean.TRUE);
		
		form.addRowToDetail();
		form.addFieldToDetail(new TextField(28, 39, pessoa.getNmPessoa(), true));

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(20, 43, this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa())));
		form.addFieldToDetail(new TextField(64, 2, enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(22, 30, enderecoPessoa.getMunicipio().getNmMunicipio()));
		String text = FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep());
		form.addFieldToDetail(new TextField(54, 13, text));

		String fone = null;
		String fax = null;
		for (TelefoneEndereco telefoneEndereco : fones) {
			if(telefoneEndereco != null && telefoneEndereco.getTpUso() != null) {
				if("FO".equals(telefoneEndereco.getTpUso().getValue())) {
					fone = telefoneEndereco.getDddTelefone();
				} else if("FA".equals(telefoneEndereco.getTpUso().getValue())) {
					fax = telefoneEndereco.getDddTelefone();
				} else if("FF".equals(telefoneEndereco.getTpUso().getValue())) {
					if(fone == null) {
						fone = telefoneEndereco.getDddTelefone();
					} else if(fax == null) {
						fax = telefoneEndereco.getDddTelefone();
					}
				}
			}
		}		
		form.addRowToDetail();
		if(fone != null) {
			form.addFieldToDetail(new TextField(20, 12, fone.replaceAll(" ", "")));
		}
		if(fax != null) {
			form.addFieldToDetail(new TextField(34, 12, fax.replaceAll(" ", "")));
		}
		if(pessoa.getDsEmail() != null) {
			form.addFieldToDetail(new TextField(49, 17, pessoa.getDsEmail(), true));
		}

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(20, 22, FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), pessoa.getNrIdentificacao())));
		form.addFieldToDetail(new TextField(48, 19, inscricaoEstadual.getNrInscricaoEstadual()));
	}

	private void montaRemetente(Conhecimento conhecimento, FormHelper form) {
		Cliente cliente = conhecimento.getClienteByIdClienteRemetente();
		montaCliente(cliente, conhecimento.getInscricaoEstadualRemetente(), form, false);
	}

	private void montaDestinatario(Conhecimento conhecimento, FormHelper form) {
		Cliente cliente = conhecimento.getClienteByIdClienteDestinatario();
		montaCliente(cliente, conhecimento.getInscricaoEstadualDestinatario(), form, false);
	}

	private void montaDevedor(Conhecimento conhecimento, FormHelper form) {
		String tpDevedorFrete = conhecimento.getTpDevedorFrete().getValue();
		if(ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(tpDevedorFrete)) {
			InscricaoEstadual inscricaoEstadual = getInscricaoEstadualByTpResponsavel(conhecimento, tpDevedorFrete);
	
			Cliente cliente = ((DevedorDocServ)conhecimento.getDevedorDocServs().get(0)).getCliente();
			montaCliente(cliente, inscricaoEstadual, form, false);
	
			DivisaoCliente divisaoCliente = conhecimento.getDivisaoCliente();
			if(divisaoCliente != null) {
				form.addFieldToDetail(new TextField(36, 17, divisaoCliente.getDsDivisaoCliente()));
			}
		} else {
			String descricaoDevedor = "";
			if(ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(tpDevedorFrete)) {
				descricaoDevedor = "Remetente";
			} else if(ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(tpDevedorFrete)) {
				descricaoDevedor = "Destinatário";
			} else if(ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equals(tpDevedorFrete)) {
				descricaoDevedor = "Consignatário";
			} else if(ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(tpDevedorFrete)) {
				descricaoDevedor = "Redespacho";
			}
			form.addRowToDetail(2);
			form.addFieldToDetail(new TextField(2, 32, descricaoDevedor));
			form.addRowToDetail(3);
		}
	}

	private InscricaoEstadual getInscricaoEstadualByTpResponsavel(Conhecimento conhecimento, String tpResponsavel) {
		InscricaoEstadual inscricaoEstadual = null;
		if(ConstantesExpedicao.TP_DEVEDOR_REMETENTE.equals(tpResponsavel)) {
			inscricaoEstadual = conhecimento.getInscricaoEstadualRemetente();
		} else if(ConstantesExpedicao.TP_DEVEDOR_DESTINATARIO.equals(tpResponsavel)) {
			inscricaoEstadual = conhecimento.getInscricaoEstadualDestinatario();
		} else if(ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO.equals(tpResponsavel)) {
			DadosComplemento dadosComplemento = dadosComplementoService.findByIdConhecimentoDocServico(conhecimento.getIdDoctoServico(), ConstantesExpedicao.IE_CONSIGNATARIO);
			inscricaoEstadual = new InscricaoEstadual();
			inscricaoEstadual.setNrInscricaoEstadual(dadosComplemento.getDsValorCampo());
		} else if(ConstantesExpedicao.TP_DEVEDOR_REDESPACHO.equals(tpResponsavel)) {
			DadosComplemento dadosComplemento = dadosComplementoService.findByIdConhecimentoDocServico(conhecimento.getIdDoctoServico(), ConstantesExpedicao.IE_REDESPACHO);
			inscricaoEstadual = new InscricaoEstadual();
			inscricaoEstadual.setNrInscricaoEstadual(dadosComplemento.getDsValorCampo());
		}
		return inscricaoEstadual;
	}

	private void montaConsignatarioRedespachoLocalEntrega(Conhecimento conhecimento, FormHelper form) {
		/** Descricao Loc Entrega */
		form.addRowToDetail();
		form.addFieldToDetail(new TextField(2,10, getLabelLocalEntrega(conhecimento)));

		if(conhecimento.getDsEnderecoEntrega() != null) {
			montaLocalEntrega(conhecimento, form);
		} else {
			InscricaoEstadual inscricaoEstadual = null;
			/** Redespacho */
			Cliente cliente = conhecimento.getClienteByIdClienteRedespacho();
			if(cliente != null) {
				inscricaoEstadual = getInscricaoEstadualByTpResponsavel(conhecimento, ConstantesExpedicao.TP_DEVEDOR_REDESPACHO);
			}
			/** Consignatario */
			if(cliente == null) {
				cliente = conhecimento.getClienteByIdClienteConsignatario();
				if(cliente != null) {
					inscricaoEstadual = getInscricaoEstadualByTpResponsavel(conhecimento, ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO);
				}
			}
			montaCliente(cliente, inscricaoEstadual, form, true);
		}
	}

	private String getLabelLocalEntrega(Conhecimento conhecimento) {
		if (conhecimento.getDsEnderecoEntrega() != null) {
			return "Loc Entr.:";
		} else
		if (conhecimento.getClienteByIdClienteRedespacho() != null) {
			return "Redesp.:";
		} else
		if (conhecimento.getClienteByIdClienteConsignatario() != null) {
			return "Consig.:";
		}
		return "";
	}

	private void montaLocalEntrega(Conhecimento conhecimento, FormHelper form) {
		int filedSize = 53;
		String text;

		form.addRowToDetail();
		text = StringUtils.defaultString(conhecimento.getDsEnderecoEntrega()).concat(", ").concat(StringUtils.defaultString(conhecimento.getNrEntrega()) + " " + StringUtils.defaultString(conhecimento.getDsComplementoEntrega()));
		form.addFieldToDetail(new TextField(2, filedSize, text));

		form.addRowToDetail();
		text = StringUtils.defaultString(conhecimento.getDsBairroEntrega()).concat(" ").concat(StringUtils.defaultString(conhecimento.getDsLocalEntrega()));
		form.addFieldToDetail(new TextField(2, filedSize, text));

		form.addRowToDetail();
		text = StringUtils.defaultString(conhecimento.getNrCepEntrega()).concat(" ").concat(StringUtils.defaultString(conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipioAndSgUnidadeFederativa()));
		form.addFieldToDetail(new TextField(2, filedSize, text));
		form.addRowToDetail();
	}

	private void montaCliente(Cliente cliente, InscricaoEstadual inscricaoEstadual, FormHelper form, boolean isLocalEntrega) {
		if (!isLocalEntrega) {
			form.addRowToDetail();
		}
		if(cliente == null) {
			form.addRowToDetail(4);
			return;
		}
		Pessoa pessoa = cliente.getPessoa();
		EnderecoPessoa enderecoPessoa = findEnderecoPessoa(pessoa);
		TelefoneEndereco fone = (TelefoneEndereco) enderecoPessoa.getTelefoneEnderecos().get(0);
		TelefoneEndereco fax =  (TelefoneEndereco) enderecoPessoa.getTelefoneEnderecos().get(1);

		String text;

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(2, 31, pessoa.getNmPessoa()));
		text = "";
		if(fone != null) {
			text = fone.getDddTelefone();
		}
		form.addFieldToDetail(new LabelledTextField(34, 19, text, new Label(6, "FONE")));

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(2, 31, this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa())));
		text = "";
		if(fax != null) {
			text = fax.getDddTelefone();
		}
		form.addFieldToDetail(new LabelledTextField(34, 19, text, new Label(6, "FAX")));

		form.addRowToDetail();
		text = FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep());
		form.addFieldToDetail(new TextField(2, 10, text));
		if("brasil".toUpperCase().equals(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue().toUpperCase())) {
		text = enderecoPessoa.getMunicipio().getNmMunicipio() + " " + enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
		} else {
			text = enderecoPessoa.getMunicipio().getNmMunicipio() + " EX";
		}
		form.addFieldToDetail(new TextField(12, 41, text));

		form.addRowToDetail();
		text = "";
		if(pessoa.getTpIdentificacao() != null) {
			if("brasil".toUpperCase().equals(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue().toUpperCase())) {
		text = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), pessoa.getNrIdentificacao());
			} else {
				String newNr = "";
				newNr = FormatUtils.fillNumberWithZero(newNr, pessoa.getNrIdentificacao().length());
				text = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), newNr);
			}
		}
		form.addFieldToDetail(new TextField(2, 20, text));

		if (inscricaoEstadual == null) {
			text = "";
		} else {
			text = inscricaoEstadual.getNrInscricaoEstadual();
		}
		form.addFieldToDetail(new TextField(26, 20, text));
	}

	private void montaCargaTransportadaOutrosDados(Conhecimento conhecimento, FormHelper form) {
		form.addRowToDetail(3);

		form.addFieldToDetail(new TextField(5, 13, conhecimento.getDsEspecieVolume(), true)); //Especie
		form.addFieldToDetail(new NumericField(22, 11, conhecimento.getPsReal())); //Peso Real
		form.addFieldToDetail(new TextField(40, 14, "CFE. MANIFESTO")); //Veiculo

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(6, 13, conhecimento.getDensidade().getTpDensidade().getValue())); //Densidade
		form.addFieldToDetail(new NumericField(22, 11, BigDecimalUtils.defaultBigDecimal(conhecimento.getPsAforado(), conhecimento.getPsReferenciaCalculo()))); // Peso Cubado
		StringBuilder text = new StringBuilder();
		if (!ConstantesExpedicao.CALCULO_MANUAL.equals(conhecimento.getTpCalculoPreco().getValue())) {
			if(conhecimento.getTabelaPreco() != null) {
			text.append(conhecimento.getTabelaPreco().getTipoTabelaPreco().getTpTipoTabelaPreco().getValue());
			text.append(FormatUtils.formatDecimal("00", conhecimento.getTabelaPreco().getTipoTabelaPreco().getNrVersao()));
			text.append("-");
			text.append(conhecimento.getTabelaPreco().getSubtipoTabelaPreco().getTpSubtipoTabelaPreco());
			if(conhecimento.getTarifaPreco() != null) {
				text.append("/");
				text.append(conhecimento.getTarifaPreco().getCdTarifaPreco());
			}
		}
		}
		form.addFieldToDetail(new TextField(42, 14, text.toString())); //Tabela - Tarifa

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(6, 11, conhecimento.getQtVolumes().toString())); //Quantidade
		form.addFieldToDetail(new NumericField(22, 11, conhecimento.getPsReferenciaCalculo())); //Peso Calculo
		if(conhecimento.getFluxoFilial() != null && conhecimento.getFluxoFilial().getDsFluxoFilial() != null) {
			String dsFluxoFilial = conhecimento.getFluxoFilial().getDsFluxoFilial();
			dsFluxoFilial = patternSpaceChar.matcher(dsFluxoFilial).replaceAll("");
			form.addFieldToDetail(new TextField(42, 14, dsFluxoFilial, true)); // Fluxo de carga
		}

		escreveCancelado(56, 24, conhecimento.getTpSituacaoConhecimento().getValue(), form);
		
		form.addRowToDetail();
		form.addFieldToDetail(new TextField(6, 13, conhecimento.getNaturezaProduto().getDsNaturezaProduto().getValue())); //Natureza
		form.addFieldToDetail(new NumericField(22, 11, getMetragemCubica(conhecimento))); //M3
		text = new StringBuilder();
		text.append(conhecimento.getUsuarioByIdUsuarioInclusao().getNrMatricula());
		text.append(" - ");
		text.append(conhecimento.getUsuarioByIdUsuarioInclusao().getNmUsuario());
		form.addFieldToDetail(new TextField(42, 14, text.toString(), true)); //Emissor

		escreveCancelado(56, 24, conhecimento.getTpSituacaoConhecimento().getValue(), form);
		
		form.addRowToDetail();
		List<NotaFiscalConhecimento> notas = conhecimento.getNotaFiscalConhecimentos();
		if(notas != null && notas.size() > 0 && notas.get(0) != null && notas.get(0).getNrNotaFiscal() != null) {
			form.addFieldToDetail(new TextField(6, 13, FormatUtils.fillNumberWithZero(notas.get(0).getNrNotaFiscal().toString(), 9))); //Nota fiscal
	}
		form.addFieldToDetail(new NumericField(22, 11, conhecimento.getVlMercadoria())); //Valor Mercadoria
		form.addFieldToDetail(new TextField(42, 14, conhecimento.getMunicipioByIdMunicipioColeta().getNmMunicipio())); //Local de Coleta

		escreveCancelado(56, 24, conhecimento.getTpSituacaoConhecimento().getValue(), form);
	}

	/**
	 * CQPRO00023348.
	 * 
	 * A data de emissão do CTRC (dia + mês + ano) (considera só 2 dígitos, para 2010, usa o 10) e a identificação da filial emissora   
	 * (código da filial emissora) deverão ser adicionados aos dados que já fazem parte do cálculo do fechamento do CTRC; 
     * a partir desta soma, calcular o digito de controle usando o módulo 11, este digito será anexado ao valor valor do fechamento (última posição) .
     * 
	 * @param conhecimento
	 * @return
	 */
	private BigDecimal gerarNumeroFechamento(Conhecimento conhecimento) {
		
		filialService.validateExisteCodFilial(conhecimento.getFilialOrigem());
		
		BigDecimal fech = new BigDecimal("0");
		fech = fech.add(conhecimento.getVlMercadoria());
		fech = fech.add(new BigDecimal(conhecimento.getQtVolumes()));
		fech = fech.add(new BigDecimal(conhecimento.getPsReal().intValue()));
		fech = fech.add(conhecimento.getVlTotalDocServico());
		fech = fech.add(conhecimento.getVlImposto());
		fech = fech.add(new BigDecimal(("C".equals(conhecimento.getTpFrete().getValue())) ? "1" : "2"));
		//ate este ponto eh o calculo antigo. A partir daqui segue quest CQPRO00023348 mais inclusao CFOP
		fech = fech.add(new BigDecimal(conhecimento.getDhEmissao().getYearOfCentury()));
		fech = fech.add(new BigDecimal(conhecimento.getDhEmissao().getMonthOfYear()));
		fech = fech.add(new BigDecimal(conhecimento.getDhEmissao().getDayOfMonth()));
		fech = fech.add(new BigDecimal(conhecimento.getFilialOrigem().getCodFilial()));
		if(conhecimento.getNrCfop() != null) {
			fech = fech.add(new BigDecimal(conhecimento.getNrCfop()));
		}
		fech = fech.setScale(2, RoundingMode.HALF_UP);
		fech = fech.multiply(new BigDecimal(100));
		fech = fech.setScale(0, RoundingMode.HALF_UP);
		String resultado = fech.toString();
		resultado = resultado + getDigitoVerificadorByModulo11(resultado);
		
		return new BigDecimal(resultado);
	}

	private String getDigitoVerificadorByModulo11(String strNumber) {
    	int nrMultiply = (strNumber.length()+1);
		int nrSumResult = 0;

		for (int i = 0; i < strNumber.length(); i++) {
			nrSumResult += Integer.parseInt(strNumber.substring(i,i+1)) * nrMultiply;
			nrMultiply--;
		}

		int nrDigitoVerificador = (nrSumResult%11);
        if (nrDigitoVerificador < 2) {
        	nrDigitoVerificador = 2;
        } else nrDigitoVerificador = (11 - nrDigitoVerificador);

        return String.valueOf(nrDigitoVerificador);
	}

	private void montaObservacao(Conhecimento conhecimento, FormHelper form, int nrAvailableCols, String linhaOutrasParcelas) {
		int fieldWidth = nrAvailableCols;
		int maxRow = 7;
		int nrRow = 1;
		Cliente cliente = null;
		String text;

		form.addRowToDetail();
		
		if("C".equals(conhecimento.getTpSituacaoConhecimento().getValue())) {
			form.addFieldToDetail(new TextField(2, fieldWidth, "PROIBIDA A UTILIZACAO PARA TRANSPORTE. DOC. CANCELADO"));
			escreveCancelado(56, 24, conhecimento.getTpSituacaoConhecimento().getValue(), form);
		}
		
		if(conhecimento.getDsEnderecoEntrega() != null) {
			cliente = conhecimento.getClienteByIdClienteRedespacho();
			if(cliente == null) {
				cliente = conhecimento.getClienteByIdClienteConsignatario();
			}
			if(cliente != null) {
				form.addRowToDetail();
				Pessoa pessoa = cliente.getPessoa();
				text = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), pessoa.getNrIdentificacao()) + " " + pessoa.getNmPessoa();
				form.addFieldToDetail(new TextField(2, fieldWidth, text));
				nrRow++;
			}
		}

		List<ObservacaoDoctoServico> observacoes = conhecimento.getObservacaoDoctoServicos();
		//ordena a lista de observacoes
		Collections.sort(observacoes, new Comparator<ObservacaoDoctoServico>() {
			public int compare(ObservacaoDoctoServico ob1, ObservacaoDoctoServico ob2) {
				boolean ob1BlPrioriedade = ob1.getBlPrioridade() == null ? false : ob1.getBlPrioridade().booleanValue();
				boolean ob2BlPrioriedade = ob2.getBlPrioridade() == null ? false : ob2.getBlPrioridade().booleanValue();
				
				if (ob1BlPrioriedade && !ob2BlPrioriedade){
					return -1;
				}else if (!ob1BlPrioriedade && ob2BlPrioriedade){
					return 1;
				}else {
					return 0;
			}
			}
		});

		//adiciona apenas as observacoes prioritarias
		if(observacoes != null) {
			ObservacaoDoctoServico observacao = null;
			Iterator it = observacoes.iterator();
			while(it.hasNext() && (nrRow <= maxRow)) {
				observacao = (ObservacaoDoctoServico) it.next();
				if (observacao.getBlPrioridade()){
				form.addRowToDetail();
				text = observacao.getDsObservacaoDoctoServico();
				form.addFieldToDetail(new TextField(2, fieldWidth, text, true));
				nrRow++;
			}
		}
		}
		
		if(nrRow < maxRow) {
			List<Cotacao> cotacoes = conhecimento.getCotacoes();
			if( (cotacoes != null) && (cotacoes.size() == 1)) {
				Cotacao cotacao = cotacoes.get(0);
				form.addRowToDetail();
				form.addFieldToDetail(new LabelledTextField(2, fieldWidth, VendasUtils.formatCotacao(cotacao.getFilial().getSgFilial(), cotacao.getNrCotacao()), new Label(9, "COTAÇÃO:")));
			nrRow++;
		}
		}

		if(nrRow < maxRow) {
			boolean blImprime = false;
			DadosComplemento complemento = null;
			List complementos = conhecimento.getDadosComplementos();
			if(complementos != null) {
				Iterator it = complementos.iterator();
				while(it.hasNext() && (nrRow <= maxRow)) {
					complemento = (DadosComplemento) it.next();
					StringBuilder out = new StringBuilder();
					blImprime = false;
					if( (complemento.getInformacaoDoctoCliente() != null)
							&& (Boolean.TRUE.equals(complemento.getInformacaoDoctoCliente().getBlImprimeConhecimento()))
					) {
						out.append(complemento.getInformacaoDoctoCliente().getDsCampo());
						blImprime = true;
					} else if( (complemento.getInformacaoDocServico() != null)
						&& (Boolean.TRUE.equals(complemento.getInformacaoDocServico().getBlImprimeConhecimento()))
					) {
						out.append(complemento.getInformacaoDocServico().getDsCampo());
						blImprime = true;
					}
					if(blImprime) {
						out.append(": ");
						out.append(complemento.getDsValorCampo());
						form.addRowToDetail();
						form.addFieldToDetail(new TextField(2, fieldWidth, out.toString()));
						nrRow++;
					}
				}
			}
		}
		
		//adiciona apenas as observacoes nao prioritarias.
		if(observacoes != null) {
			ObservacaoDoctoServico observacao = null;
			Iterator it = observacoes.iterator();
			while(it.hasNext() && (nrRow <= maxRow)) {
				observacao = (ObservacaoDoctoServico) it.next();
				if (!observacao.getBlPrioridade()){
					form.addRowToDetail();
					text = observacao.getDsObservacaoDoctoServico();
					form.addFieldToDetail(new TextField(2, fieldWidth, text, true));
					nrRow++;
				}
			}
		}
		
		if (nrRow < maxRow && StringUtils.isNotEmpty(linhaOutrasParcelas)){
			int lineWidth = 90;
			while (linhaOutrasParcelas.length() > lineWidth){
				String texto = linhaOutrasParcelas.substring(0, lineWidth);
				linhaOutrasParcelas = linhaOutrasParcelas.replaceFirst(texto, "");
				form.addRowToDetail();
				form.addFieldToDetail(new TextField(2, fieldWidth, texto, true));
				nrRow++;
			}
			form.addRowToDetail();
			form.addFieldToDetail(new TextField(2, lineWidth, linhaOutrasParcelas, true));
			nrRow++;
		}
		
		while(nrRow < maxRow) {
			form.addRowToDetail();
			if(escreveCancelado(2, fieldWidth + 24, conhecimento.getTpSituacaoConhecimento().getValue(), form)) {
				nrRow++;
			} else {
				break;
	}
		}
	}

	private String montaComposicaoFrete(Conhecimento conhecimento, FormHelper form, int nrAvailableRows) {
		Field field = null;
		Label label = null;
		int labelSize = 16, fieldSize = 25, fieldPosition = 56;
		BigDecimal vlOutros = BigDecimalUtils.ZERO;
		BigDecimal vlDesconto = conhecimento.getVlDesconto();
		StringBuilder linhaOutrasParcelas = new StringBuilder();

		ParcelaDoctoServico parcelaDoctoServico = null;
		List<ParcelaDoctoServico> parcelas = parcelaDoctoServicoService.findParcelasFrete(conhecimento.getIdDoctoServico());
		int nrMaxParcelas = nrAvailableRows - 1;
		if(BigDecimalUtils.hasValue(vlDesconto)) {
			nrMaxParcelas--;
		}
		if(BigDecimalUtils.hasValue(conhecimento.getVlIcmsSubstituicaoTributaria())) {
			nrMaxParcelas = nrMaxParcelas -2;
		}
		if(BigDecimalUtils.hasValue(conhecimento.getVlTotalServicos()) || parcelas.size() > nrMaxParcelas) {
			nrMaxParcelas--;
		}
		int nrStartRow = 15;
		int nrFinishRow = nrStartRow + nrMaxParcelas;
		int nrCurrentRow = nrStartRow;

		if(parcelas.size() < nrMaxParcelas) {
			nrMaxParcelas = parcelas.size();
		}

		Iterator it = parcelas.iterator();
		for(int p=0; p<nrMaxParcelas; p++) {
			parcelaDoctoServico = (ParcelaDoctoServico) it.next();
			ParcelaPreco parcelaPreco = parcelaDoctoServico.getParcelaPreco();
			label = new Label(labelSize, parcelaPreco.getDsParcelaPreco().getValue());
			label.setCondensed(true);
			field = new LabelledNumericField(fieldPosition, fieldSize, parcelaDoctoServico.getVlParcela(), label);
			form.addFieldToDetail(nrCurrentRow, field);
			nrCurrentRow++;
		}
		while(it.hasNext()) {
			parcelaDoctoServico = (ParcelaDoctoServico) it.next();
			vlOutros = vlOutros.add(parcelaDoctoServico.getVlParcela());
			if (StringUtils.isEmpty(linhaOutrasParcelas.toString())){
				linhaOutrasParcelas.append("O COMPONENTE DE FRETE DENOMINADO OUTROS É FORMADO PELOS SEGUINTES COMPONENTES: ");
		}
			linhaOutrasParcelas.append("--")
				.append(parcelaDoctoServico.getParcelaPreco().getDsParcelaPreco())
				.append(": ").append(FormatUtils.formatDecimal("#,##0.00", parcelaDoctoServico.getVlParcela()));
		}

		nrCurrentRow = nrFinishRow;
		if(BigDecimalUtils.hasValue(conhecimento.getVlTotalServicos())) {
			vlOutros = vlOutros.add(conhecimento.getVlTotalServicos());
		}
		if(BigDecimalUtils.hasValue(vlOutros)) {
			label = new Label(labelSize, "OUTROS");
			label.setCondensed(true);
			field = new LabelledNumericField(fieldPosition, fieldSize, vlOutros, label);
			form.addFieldToDetail(nrCurrentRow, field);
			nrCurrentRow++;
		}
		if(BigDecimalUtils.hasValue(vlDesconto)) {
			label = new Label(labelSize, "DESCONTO");
			label.setCondensed(true);
			field = new LabelledNumericField(fieldPosition, fieldSize, vlDesconto, label);
			form.addFieldToDetail(nrCurrentRow, field);
			nrCurrentRow++;
		}

		label = new Label(labelSize, "TOTAL DO FRETE");
		label.setCondensed(true);
		field = new LabelledNumericField(fieldPosition, fieldSize, conhecimento.getVlTotalDocServico(), label);
		form.addFieldToDetail(nrCurrentRow, field);
		nrCurrentRow++;

		if(BigDecimalUtils.hasValue(conhecimento.getVlIcmsSubstituicaoTributaria())) {
			label = new Label(labelSize, "RETENÇÃO ICMS ST");
			label.setCondensed(true);
			field = new LabelledNumericField(fieldPosition, fieldSize, conhecimento.getVlIcmsSubstituicaoTributaria(), label);
			form.addFieldToDetail(nrCurrentRow, field);
			nrCurrentRow++;

			label = new Label(labelSize, "TOTAL A PAGAR");
			label.setCondensed(true);
			field = new LabelledNumericField(fieldPosition, fieldSize, conhecimento.getVlLiquido(), label);
			form.addFieldToDetail(nrCurrentRow, field);
			nrCurrentRow++;
		}
		return linhaOutrasParcelas.toString();
	}

	private void montaTributacao(Conhecimento conhecimento, FormHelper form, int nrStartRow) {
		Label label = null;
		Field field = null;
		int nrRow = nrStartRow;

		label = new Label(6, "BASE CALC.");
		label.setCondensed(true);
		field = new LabelledNumericField(56, 15, conhecimento.getVlBaseCalcImposto(), label);
		field.setCondensed(true);
		form.addFieldToDetail(nrRow, field);

		label = new Label(3, "ALIQ.");
		label.setCondensed(true);
		field = new LabelledNumericField(72, 9, conhecimento.getPcAliquotaIcms(), label);
		field.setCondensed(true);
		form.addFieldToDetail(nrRow, field);

		nrRow++;
		label = new Label(6, "VALOR ICMS");
		label.setCondensed(true);
		field = new LabelledNumericField(56, 15, conhecimento.getVlImposto(), label);
		field.setCondensed(true);
		form.addFieldToDetail(nrRow, field);
	}

	private void montaNotasFiscais(Conhecimento conhecimento, FormHelper form, int nrStartRow, int nrAvailableRows) {
		int nrNotasPorLinha = 4;
		int nrFinishRow = nrStartRow + nrAvailableRows;
		int nrCurrentRow = nrStartRow;

		List notas = conhecimento.getNotaFiscalConhecimentos();
		NotaFiscalConhecimento notaFiscalConhecimento = null;
		StringBuffer out = null;
		Iterator it = notas.iterator();
		boolean lastLine = false;
		
		while(nrCurrentRow != nrFinishRow && escreveCancelado(nrCurrentRow, 56, 25, conhecimento.getTpSituacaoConhecimento().getValue(), form)) {
			nrCurrentRow++;
			lastLine = true;
		}
		
		while(!lastLine) {
			lastLine = (nrCurrentRow + 1 == nrFinishRow);
			out = new StringBuffer();
			boolean lastForLine = false;
			for(int j=0; j<nrNotasPorLinha; j++) {
				lastForLine = (j+1 == nrNotasPorLinha);
				if(lastLine && lastForLine && it.hasNext()) {
					out.append("...");
				} else if (it.hasNext()) {
					notaFiscalConhecimento = (NotaFiscalConhecimento) it.next();
					out.append(StringUtils.leftPad(notaFiscalConhecimento.getNrNotaFiscal().toString(), 9, '0'));
					if(!lastForLine) {
						out.append(" ");
					}
				}
				if(!it.hasNext()) {
					break;
				}
			}

			Field field = new TextField(56, 25, out.toString());
			field.setCondensed(true);
			form.addFieldToDetail(nrCurrentRow, field);
			nrCurrentRow++;

			if(!it.hasNext()) {
				break;
			}
		}
	}

	private void montaSeloFiscal(Conhecimento conhecimento, FormHelper form) {
		form.addFieldToDetail(38, new TextField(58, 10, conhecimento.getNrSeloFiscal().toString()));
		form.addFieldToDetail(38, new TextField(72, 8, conhecimento.getDsSerieSeloFiscal()));
	}

	private boolean escreveCancelado(int position, int width, String tpSituacaoConhecimento, FormHelper form) {
		return escreveCancelado(-1, position, width, tpSituacaoConhecimento, form);
	}
	
	private boolean escreveCancelado(int row, int position, int width, String tpSituacaoConhecimento, FormHelper form) {
		if("C".equals(tpSituacaoConhecimento)) {
			if(row > 0) {
				form.addFieldToDetail(row, new TextField(position, width, "CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO"));
			} else {
				form.addFieldToDetail(new TextField(position, width, "CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO CANCELADO"));
			}
			return true;
		}
		return false;
	}
	
	private EnderecoPessoa findEnderecoPessoa(Pessoa pessoa) {
		Long idPessoa = pessoa.getIdPessoa();
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa);
		if(enderecoPessoa == null) {
			throw new BusinessException("LMS-01194", new Object[]{ pessoa.getNrIdentificacao() });
		}
		TelefoneEndereco fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FO");
		if(fone == null) {
			fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FF");
		}
		TelefoneEndereco fax = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FA");

		List telefonesEndereco = new ArrayList();
		telefonesEndereco.add(fone);
		telefonesEndereco.add(fax);
		enderecoPessoa.setTelefoneEnderecos(telefonesEndereco);

		return enderecoPessoa;
	}

	private BigDecimal getMetragemCubica(Conhecimento conh) {
		String tpModal 			= conh.getServico().getTpModal().getValue();
		BigDecimal psAforado 	= conh.getPsAforado();
		if(conh.getTabelaPreco() 	!= null && conh.getTabelaPreco().getIdTabelaPreco() 		!= null 
		&& conh.getDivisaoCliente() != null && conh.getDivisaoCliente().getIdDivisaoCliente() 	!= null){
			BigDecimal nrFatorCubagem = conh.getDivisaoCliente().getTabelaDivisaoClientes() == null 
									 || conh.getDivisaoCliente().getTabelaDivisaoClientes().isEmpty() ? null 
											 : ((TabelaDivisaoCliente)conh.getDivisaoCliente().getTabelaDivisaoClientes().get(0)).getNrFatorCubagem();
			if(nrFatorCubagem != null && nrFatorCubagem.signum() > 0){
				return psAforado.divide(nrFatorCubagem, 4,BigDecimal.ROUND_HALF_UP);
			}else{
				return getMetragemPadraoPorModalEPsAforado(tpModal, psAforado);				
			}
		}else{			
			return getMetragemPadraoPorModalEPsAforado(tpModal, psAforado);
		}
	}

	private BigDecimal getMetragemPadraoPorModalEPsAforado(String tpModal,
			BigDecimal psAforado) {
		BigDecimal psMetragemCubica = BigDecimalUtils.ZERO;
		if(ConstantesExpedicao.MODAL_RODOVIARIO.equals(tpModal)) {
			psMetragemCubica = getPesoMetragemCubicaRodoviario();
		} else if(ConstantesExpedicao.MODAL_AEREO.equals(tpModal)) {
			psMetragemCubica = getPesoMetragemCubicaAereo();
		}
		return psAforado.multiply(psMetragemCubica).divide(new BigDecimal("1000000"), 4, BigDecimal.ROUND_HALF_UP);
	}
	private BigDecimal getPesoMetragemCubicaRodoviario() {
		return (BigDecimal) configuracoesFacade.getValorParametro("PESO_METRAGEM_CUBICA_RODOVIARIO");
	}

	private BigDecimal getPesoMetragemCubicaAereo() {
		return (BigDecimal) configuracoesFacade.getValorParametro("PESO_METRAGEM_CUBICA_AEREO");
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setDadosComplementoService(DadosComplementoService dadosComplementoService) {
		this.dadosComplementoService = dadosComplementoService;
	}
	public void setParcelaDoctoServicoService(ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
}

	public InscricaoEstadualService getInscricaoEstadualService() {
		return inscricaoEstadualService;
}

	public void setInscricaoEstadualService(
			InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

}
