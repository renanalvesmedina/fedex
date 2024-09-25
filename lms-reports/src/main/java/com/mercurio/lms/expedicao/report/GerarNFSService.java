package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.model.DomainValue;
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
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServicoDocumento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoDocumentoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tributos.model.ServicoOficialTributo;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;

/**
 * @author Claiton Grings 
 * @spring.bean id="lms.expedicao.gerarNFSService"
 */
public class GerarNFSService {
	private NotaFiscalServicoService notaFiscalServicoService;
	private NotaFiscalServicoDocumentoService notaFiscalServicoDocumentoService;
	private PessoaService pessoaService;
	private EnderecoPessoaService enderecoPessoaService;

	public String generateNFS(Long idNotaFiscalServico) {
		NotaFiscalServico notaFiscalServico = notaFiscalServicoService.findById(idNotaFiscalServico);
		FormAttributes attributes = new FormAttributes(80, 36, PrintCommands.getPageLengthInInches(6));

		FormHelper form = new FormHelper(attributes, new Configuration(LocaleContextHolder.getLocale()));

		montaCabecalho(notaFiscalServico, form);
		montaTomadorServico(notaFiscalServico, form);
		montaDescricaoServicos(notaFiscalServico, form);
		montaObservacao(notaFiscalServico, form);
		montaRodape(notaFiscalServico, form);

		return form.write();
	}

	private void montaCabecalho(NotaFiscalServico notaFiscalServico, FormHelper form) {
		form.addRowToDetail(10);
	}

	private void montaTomadorServico(NotaFiscalServico notaFiscalServico, FormHelper form) {
		DevedorDocServ devedorDocServ = (DevedorDocServ) notaFiscalServico.getDevedorDocServs().get(0);
		Pessoa pessoa = pessoaService.findById(devedorDocServ.getCliente().getIdCliente());
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();

		form.addRowToDetail();

		//Nome - Razao Social
		form.addFieldToDetail(new TextField(15, 65, pessoa.getNmPessoa()));

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(15, 34, this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()) + " - " + enderecoPessoa.getDsBairro(), true));
		String text = FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep());
		form.addFieldToDetail(new TextField(52, 10, text));
		
		form.addFieldToDetail(new TextField(70, 8, JTFormatUtils.format(notaFiscalServico.getDhEmissao(), JTFormatUtils.SHORT, JTFormatUtils.YEARMONTHDAY))); //Data de Emissao

		form.addRowToDetail();
		text = enderecoPessoa.getMunicipio().getNmMunicipio();
		form.addFieldToDetail(new TextField(15, 34, text));
		text = enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa();
		form.addFieldToDetail(new TextField(52, 19, text));

		form.addRowToDetail();

		text = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), pessoa.getNrIdentificacao());
		form.addFieldToDetail(new TextField(15, 20, text));
		text = pessoa.getNrInscricaoMunicipal();
		form.addFieldToDetail(new TextField(45, 34, text));
	}

	private void montaDescricaoServicos(NotaFiscalServico notaFiscalServico, FormHelper form) {
		Field field;
		form.addRowToDetail(3);

		ServAdicionalDocServ servAdicionalDocServ = (ServAdicionalDocServ) notaFiscalServico.getServAdicionalDocServs().get(0);
		ServicoAdicional servicoAdicional = servAdicionalDocServ.getServicoAdicional();
		
		//Codigo
		ServicoOficialTributo servicoOficialTributo = servicoAdicional.getServicoOficialTributo();

		//Descricao
		form.addFieldToDetail(new TextField(7, 31, servicoOficialTributo.getNrServicoOficialTributo() + " - " + servicoAdicional.getDsServicoAdicional().getValue()));

		//Impostos
		List<ImpostoServico> impostosServico = notaFiscalServico.getImpostoServicos();
		BigDecimal vlImpostoISS = null;

		BigDecimal vlImposto = null;
		DomainValue tpImposto = null;
		BigDecimal pcAliquotaISS = BigDecimal.ZERO;
		NumberFormat pcAliquotaFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
		NumberFormat vlImpostoFormat = new DecimalFormat("###,###,##0.00", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
		List<String> deducoes = new ArrayList<String>();
		for (ImpostoServico impostoServico : impostosServico) {
			vlImposto = impostoServico.getVlImposto();
			tpImposto = impostoServico.getTpImposto();
			if (ConstantesExpedicao.CD_ISS.equals(tpImposto.getValue())) {
				pcAliquotaISS = impostoServico.getPcAliquota();
				if (Boolean.FALSE.equals(impostoServico.getBlRetencaoTomadorServico())) {
					vlImpostoISS = vlImposto;					
				} else {
					StringBuilder sb = new StringBuilder();
					sb.append(tpImposto.getDescription().getValue())
						.append("(")
						.append(pcAliquotaFormat.format(impostoServico.getPcAliquota()))
						.append(")=")
						.append(vlImpostoFormat.format(vlImposto));
					deducoes.add(sb.toString());
				}
			} else {
				if (BigDecimalUtils.hasValue(vlImposto)) {
					StringBuilder sb = new StringBuilder();
					
					sb.append(tpImposto.getDescription().getValue())
						.append("(")
						.append(pcAliquotaFormat.format(impostoServico.getPcAliquota()))
						.append(")=")
						.append(vlImpostoFormat.format(vlImposto));
					
					deducoes.add(sb.toString());
				}
			}
		}

		//Valor unitario
		field = new NumericField(50, 11, notaFiscalServico.getVlTotalDocServico());
		form.addFieldToDetail(field);
		//Valor Total
		field = new NumericField(68, 11, notaFiscalServico.getVlTotalDocServico());
		form.addFieldToDetail(field);

		form.addRowToDetail();

		List<NotaFiscalServicoDocumento> listaNotaFiscalServicoDocumento = notaFiscalServicoDocumentoService.findByIdNotaFiscalServico(notaFiscalServico.getIdDoctoServico());
		if (listaNotaFiscalServicoDocumento == null || listaNotaFiscalServicoDocumento.isEmpty()) {
		
		List<ParcelaDoctoServico> parcelaDoctoServicos = notaFiscalServico.getParcelaDoctoServicos();
		ParcelaPreco parcelaPreco = ((ParcelaDoctoServico)parcelaDoctoServicos.get(0)).getParcelaPreco(); 
		String cdParcelaPreco = parcelaPreco.getCdParcelaPreco();

		if(ConstantesExpedicao.CD_REEMBOLSO.equals(cdParcelaPreco)) {
			form.addFieldToDetail(new LabelledNumericField(7, 39, servAdicionalDocServ.getVlMercadoria(), new Label(17, "Valor da merc.:")));
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, servAdicionalDocServ.getQtCheques().toString(), new Label(17, "Qtde de cheques:")));
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, JTFormatUtils.format(servAdicionalDocServ.getDtPrimeiroCheque()), new Label(17, "Data 1º cheque:")));
		} else if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(cdParcelaPreco)) {
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledNumericField(7, 39, servAdicionalDocServ.getVlMercadoria(), new Label(17, "Valor da merc.:")));
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledNumericField(7, 39, notaFiscalServico.getPsReferenciaCalculo(), new Label(17, "Peso:")));
		} else if(ConstantesExpedicao.CD_ESCOLTA.equals(cdParcelaPreco)) {
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, servAdicionalDocServ.getNrKmRodado().toString(), new Label(17, "Km rodada:")));
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, servAdicionalDocServ.getQtSegurancasAdicionais().toString(), new Label(17, "Qtde seg. adic.:")));
		} else if(ConstantesExpedicao.CD_AGENDAMENTO_ENTREGA_SAB_DOM_FER.equals(cdParcelaPreco)
			|| ConstantesExpedicao.CD_AGENDAMENTO_COLETA.equals(cdParcelaPreco)) {
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledNumericField(7, 39, servAdicionalDocServ.getVlFrete(), new Label(17, "Valor do frete:")));
			form.addRowToDetail();
		} else if(ConstantesExpedicao.TIPOS_ESTADIA.contains(cdParcelaPreco)) {
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, servAdicionalDocServ.getQtDias().toString(), new Label(17, "Qtde de dias:")));
			form.addRowToDetail();
		} else if(ConstantesExpedicao.CD_PALETIZACAO.equals(cdParcelaPreco)) {
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, servAdicionalDocServ.getQtPaletes().toString(), new Label(17, "Qtde de paletes:")));
			form.addRowToDetail();
		} else if(ConstantesExpedicao.CD_ENTREGA_COLETA_DEDICADO.equals(cdParcelaPreco)) {
			form.addRowToDetail();
			form.addFieldToDetail(new LabelledTextField(7, 39, servAdicionalDocServ.getQtColetas().toString(), new Label(17, "Qtde de eventos:")));
			form.addRowToDetail();
		} else form.addRowToDetail(2);
		} else {
			form.addRowToDetail(2);
		}

		Integer nrRows = 5;
		
		form.addRowToDetail();
		
		if(!deducoes.isEmpty()){
			//Deducoes Legais
			boolean first = true;
			for (int i = 0; i< deducoes.size() && i < 5; i++) {
				String deducao = deducoes.get(i);
				if(first){
					Field fieldDeducoesLegais = new TextField(7, 73, "Deduções legais: " + deducao);
					fieldDeducoesLegais.setCondensed(true);
					form.addFieldToDetail(fieldDeducoesLegais);
					form.addRowToDetail();
				} else {
					Field fieldDeducoesLegais = new TextField(24, 56, deducao);
					fieldDeducoesLegais.setCondensed(true);
					form.addFieldToDetail(fieldDeducoesLegais);
					form.addRowToDetail();
				}
				nrRows--;
			}
		}
		
		if(nrRows > 0){
			form.addRowToDetail(nrRows);
		}
		
		String text = "NRO DA NOTA FISCAL: " + FormatUtils.formatSgFilialWithLong(notaFiscalServico.getFilialByIdFilialOrigem().getSgFilial(), notaFiscalServico.getNrDoctoServico());
		form.addFieldToDetail(new TextField(7, 50, text));
		form.addRowToDetail();
		form.addFieldToDetail(new TextField(7, 30, "HORA EMISSAO: " + JTFormatUtils.format(notaFiscalServico.getDhEmissao(), JTFormatUtils.DEFAULT, JTFormatUtils.TIMEOFDAY))); //Hora de Emissao
		form.addRowToDetail(2);
		
		//Aliquota
		NumericField fieldPcAliquota = new NumericField(7, 12, pcAliquotaISS);
		fieldPcAliquota.setNumberFormat(pcAliquotaFormat);
		form.addFieldToDetail(fieldPcAliquota);
		
		//Valor do ISS
		form.addFieldToDetail(new NumericField(20, 22, vlImpostoISS));
		//Valor Total da Nota
		form.addFieldToDetail(new NumericField(64, 12, notaFiscalServico.getVlTotalDocServico()));
	}

	private void montaObservacao(NotaFiscalServico notaFiscalServico, FormHelper form) {
		form.addRowToDetail(3);

		int maxRows = 3;
		int rowCount = 0;
		List<ObservacaoDoctoServico> observacoes = notaFiscalServico.getObservacaoDoctoServicos();
		if(observacoes != null) {
			//ordena a lista de observacoes
			Collections.sort(observacoes, new Comparator<ObservacaoDoctoServico>() {
				public int compare(ObservacaoDoctoServico ob1, ObservacaoDoctoServico ob2) {
					return (ob1.getBlPrioridade().booleanValue() && !ob2.getBlPrioridade().booleanValue()) ? -1 : (!ob1.getBlPrioridade().booleanValue() && ob2.getBlPrioridade().booleanValue()) ? 1 : 0;
				}
			});
			for (Iterator<ObservacaoDoctoServico> iter = observacoes.iterator(); iter.hasNext();) {
				ObservacaoDoctoServico observacao = iter.next();
				form.addRowToDetail();
				form.addFieldToDetail(new TextField(7, 70, observacao.getDsObservacaoDoctoServico(), true));
				/** Valida se deve continuar imprimindo */
				rowCount++;
				if (rowCount == maxRows) {
					break;
				}
			}
		}
		form.addRowToDetail(maxRows - rowCount);
	}

	private void montaRodape(NotaFiscalServico notaFiscalServico, FormHelper form) {
	}

	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}

	public void setNotaFiscalServicoDocumentoService(
			NotaFiscalServicoDocumentoService notaFiscalServicoDocumentoService) {
		this.notaFiscalServicoDocumentoService = notaFiscalServicoDocumentoService;
}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}
