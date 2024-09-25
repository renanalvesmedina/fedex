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

import com.mercurio.lms.configuracoes.model.service.*;
import org.apache.commons.lang.StringUtils;
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
import com.mercurio.lms.configuracoes.ConfiguracoesFacadeImpl;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.tributos.model.ServicoOficialTributo;
import com.mercurio.lms.tributos.model.ServicoTributo;
import com.mercurio.lms.tributos.model.service.ServicoTributoService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Claiton Grings 
 * @spring.bean id="lms.expedicao.gerarNFTService"
 */
public class GerarNFTService {
	private TelefoneEnderecoService telefoneEnderecoService;
	private PessoaService pessoaService;
	private InscricaoEstadualService inscricaoEstadualService;
	private ConfiguracoesFacadeImpl configuracoesFacade;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private ServicoTributoService servicoTributoService;
	private EnderecoPessoaService enderecoPessoaService;

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}

	public String generateNFT(Conhecimento conhecimento) {

		FormAttributes attributes = null;

		boolean isModelo3 = validateImprimeNFTModelo3();
		
		if(isModelo3){
			attributes = new FormAttributes(80, 44, PrintCommands.getPageLengthInInches(8));
		}else{
			attributes = new FormAttributes(80, 36, PrintCommands.getPageLengthInInches(6));
		}

		FormHelper form = new FormHelper(attributes, new Configuration(LocaleContextHolder.getLocale()));

		montaCabecalho(conhecimento, form);
		montaTomadorServico(conhecimento, form);
		montaDescricaoServicos(conhecimento, form);
		montaObservacao(conhecimento, form);
		montaRodape(conhecimento, form);

		return form.write(!isModelo3);
	}

	private void montaCabecalho(Conhecimento conhecimento, FormHelper form) {
		if(validateImprimeNFTModelo3()){
			form.addRowToDetail(9);
			String text = FormatUtils.formatSgFilialWithLong(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrDoctoServico());
			form.addFieldToDetail(new TextField(63, 12,text));
			form.addRowToDetail(1);
		}else{
		form.addRowToDetail(10);
	}

	}

	private void montaTomadorServico(Conhecimento conhecimento, FormHelper form) {
		DevedorDocServ devedorDocServ = (DevedorDocServ) conhecimento.getDevedorDocServs().get(0);
		Pessoa pessoa = pessoaService.findById(devedorDocServ.getCliente().getIdCliente());
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();

		form.addRowToDetail();

		//Nome - Razao Social
		form.addFieldToDetail(new TextField(15, 65, pessoa.getNmPessoa()));

		form.addRowToDetail();
		form.addFieldToDetail(new TextField(15, 34, this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()) + " - " + enderecoPessoa.getDsBairro(), true));
		String text = FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep());
		form.addFieldToDetail(new TextField(52, 10, text));
		
		if(validateImprimeNFTModelo3()){
			form.addFieldToDetail(new TextField(67, 8, JTFormatUtils.format(conhecimento.getDhEmissao(), JTFormatUtils.SHORT, JTFormatUtils.YEARMONTHDAY))); //Data de Emissao
		}else{
		form.addFieldToDetail(new TextField(70, 8, JTFormatUtils.format(conhecimento.getDhEmissao(), JTFormatUtils.SHORT, JTFormatUtils.YEARMONTHDAY))); //Data de Emissao
		}


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

	private void montaDescricaoServicos(Conhecimento conhecimento, FormHelper form) {
		Field field;
		form.addRowToDetail(3);
		
		//Codigo
		Long idServicoOficialTributo = LongUtils.getLong((BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.ID_SERVICO_TRIBUTO_NFT));
		ServicoTributo servicoTributo = servicoTributoService.findById(idServicoOficialTributo);
		ServicoOficialTributo servicoOficialTributo = servicoTributo.getServicoOficialTributo();

		//Descricao
		form.addFieldToDetail(new TextField(7, 31, servicoOficialTributo.getNrServicoOficialTributo() + " - SERVICO DE TRANSPORTE"));

		//Tributos
		List<ImpostoServico> impostosServico = conhecimento.getImpostoServicos();
		BigDecimal pcAliquotaISS = BigDecimalUtils.ZERO;
		BigDecimal vlImpostoISS = null;

		BigDecimal vlImposto = null;
		DomainValue tpImposto = null;
		Boolean isISSretido = true;
		NumberFormat pcAliquotaFormat = new DecimalFormat("##0.00", new DecimalFormatSymbols(LocaleContextHolder.getLocale()));
		
		for (ImpostoServico impostoServico : impostosServico) {
			vlImposto = impostoServico.getVlImposto();
			tpImposto = impostoServico.getTpImposto();
			if (ConstantesExpedicao.CD_ISS.equals(tpImposto.getValue())) {
				pcAliquotaISS = impostoServico.getPcAliquota();
				if (Boolean.FALSE.equals(impostoServico.getBlRetencaoTomadorServico())) {
					vlImpostoISS = vlImposto;					
					isISSretido = false;
				}
			} 
		}
		
		if(validateImprimeNFTModelo3()){
		//Valor unitario
			field = new NumericField(48, 11, conhecimento.getVlTotalDocServico());
			form.addFieldToDetail(field);
			//Valor Total
			field = new NumericField(67, 11, conhecimento.getVlTotalDocServico());
			form.addFieldToDetail(field);
			
			//Ref. Notas Fiscais
			montaNotasFiscais(conhecimento, form);
			montaDestinatario(conhecimento, form);
			montaRemetente(conhecimento, form);
			form.addRowToDetail();
			form.addFieldToDetail(new TextField(7, 30, "HORA EMISSAO: " + JTFormatUtils.format(conhecimento.getDhEmissao(), JTFormatUtils.DEFAULT, JTFormatUtils.TIMEOFDAY))); //Hora de Emissao
			
			//vldoctoservico, ISSQN retido (sim ou nao) e vlLiquido
			form.addRowToDetail(3);
			form.addFieldToDetail(new LabelledNumericField(7, 38, conhecimento.getVlTotalDocServico(), new Label(30, "Base de Cálculo (R$):")));
			form.addRowToDetail(1);
			
			if (isISSretido)
				form.addFieldToDetail(new TextField(7, 38, "ISSQN Retido:       (x) SIM ( ) Não"));
			else
				form.addFieldToDetail(new TextField(7, 38, "ISSQN Retido:       ( ) SIM (x) Não"));
			
			form.addRowToDetail(1);
			form.addFieldToDetail(new LabelledNumericField(7, 38, conhecimento.getVlLiquido(), new Label(30, "Valor Líquido da Nota (R$):")));
			form.addRowToDetail(4);
			
			//Aliquota
			NumericField fieldPcAliquota = new NumericField(7, 12, pcAliquotaISS);
			fieldPcAliquota.setNumberFormat(pcAliquotaFormat);
			form.addFieldToDetail(fieldPcAliquota);

			//Valor do ISS
			form.addFieldToDetail(new NumericField(20, 22, vlImpostoISS));
			//Valor Total da Nota
			form.addFieldToDetail(new NumericField(62, 12, conhecimento.getVlTotalDocServico()));
			
			form.addRowToDetail(7);
			
		}else{
			//Valor unitario
		field = new NumericField(50, 11, conhecimento.getVlTotalDocServico());
		form.addFieldToDetail(field);
		//Valor Total
		field = new NumericField(68, 11, conhecimento.getVlTotalDocServico());
		form.addFieldToDetail(field);

		//Ref. Notas Fiscais
		montaNotasFiscais(conhecimento, form);
		montaDestinatario(conhecimento, form);
		montaRemetente(conhecimento, form);
			form.addRowToDetail(1);

			if (isISSretido){
				form.addFieldToDetail(new LabelledNumericField(7, 40, conhecimento.getVlTotalDocServico(), new Label(30, "Base de Cálculo (R$):")));
				form.addRowToDetail(1);
				form.addFieldToDetail(new TextField(7, 40, "ISSQN Retido:            (x) SIM ( ) Não"));
				form.addRowToDetail(1);
				form.addFieldToDetail(new LabelledNumericField(7, 40, conhecimento.getVlLiquido(), new Label(30, "Valor Líquido da Nota (R$):")));
				form.addRowToDetail(1);
			}
			else{
				form.addRowToDetail(3);
			}
			
		String text = "NRO DA NOTA FISCAL: " + FormatUtils.formatSgFilialWithLong(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrDoctoServico());
		form.addFieldToDetail(new TextField(7, 50, text));
		form.addRowToDetail();
		form.addFieldToDetail(new TextField(7, 30, "HORA EMISSAO: " + JTFormatUtils.format(conhecimento.getDhEmissao(), JTFormatUtils.DEFAULT, JTFormatUtils.TIMEOFDAY))); //Hora de Emissao
		form.addRowToDetail(2);
		
		//Aliquota
		NumericField fieldPcAliquota = new NumericField(7, 12, pcAliquotaISS);
		fieldPcAliquota.setNumberFormat(pcAliquotaFormat);
		form.addFieldToDetail(fieldPcAliquota);

		//Valor do ISS
		form.addFieldToDetail(new NumericField(20, 22, vlImpostoISS));
		//Valor Total da Nota
		form.addFieldToDetail(new NumericField(62, 12, conhecimento.getVlTotalDocServico()));

		form.addRowToDetail(3);
	}

	}

	private void montaNotasFiscais(Conhecimento conhecimento, FormHelper form) {
		form.addRowToDetail();

		List<NotaFiscalConhecimento> notasFiscais = conhecimento.getNotaFiscalConhecimentos();
		if (notasFiscais != null) {
			int nrMaxNotas = 8;
			int nrNotaCurrent = 0;
			StringBuilder textNotas = new StringBuilder();
			textNotas.append("REF. NF: ");

			NotaFiscalConhecimento notaFiscalConhecimento = null;
			for (Iterator<NotaFiscalConhecimento> iter = notasFiscais.iterator(); iter.hasNext();) {
				notaFiscalConhecimento = iter.next();
				textNotas.append(StringUtils.leftPad(notaFiscalConhecimento.getNrNotaFiscal().toString(), 6, '0'));

				nrNotaCurrent++;
				if (nrNotaCurrent == nrMaxNotas && iter.hasNext()) {
					textNotas.append("...");
					break;
				}
				if (!iter.hasNext()) {
					break;
				}
				textNotas.append(" ");
			}
			form.addFieldToDetail(new TextField(7, 40, textNotas.toString(), true));
		}
	}

	private void montaDestinatario(Conhecimento conhecimento, FormHelper form) {
		Pessoa pessoa = pessoaService.findById(conhecimento.getClienteByIdClienteDestinatario().getIdCliente());
		EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();
		List<TelefoneEndereco> fones = findFoneFax(enderecoPessoa);
		TelefoneEndereco fone = fones.get(0);
		Field field;
		Label label;
		StringBuffer text;

		form.addRowToDetail();
		//Nome - Fone
		text = new StringBuffer();
		text.append(pessoa.getNmPessoa());
		if (fone != null) {
			text.append(" ");
			text.append(fone.getDddTelefone());
		}
		label = new Label(4, "DEST:");
		label.setCondensed(true);

		field = new LabelledTextField(7, 39, text.toString(), label);
		field.setCondensed(true);
		form.addFieldToDetail(field);

		form.addRowToDetail();
		//Endereco - Cep
		text = new StringBuffer();
		text.append(this.enderecoPessoaService.getEnderecoCompleto(enderecoPessoa.getIdEnderecoPessoa()));
		text.append(" ");
		text.append(FormatUtils.formatCep(enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoPessoa.getNrCep()));
		text.append(" ");
		text.append(enderecoPessoa.getMunicipio().getNmMunicipio());
		text.append(" ");
		text.append(enderecoPessoa.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
		field = new TextField(7, 39, text.toString());
		field.setCondensed(true);
		form.addFieldToDetail(field);

		form.addRowToDetail();
		//CNPJ - IE
		text = new StringBuffer();
		text.append(FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), pessoa.getNrIdentificacao()));
		InscricaoEstadual inscricaoEstadual = conhecimento.getInscricaoEstadualDestinatario();
		if(inscricaoEstadual != null) {
			inscricaoEstadual = inscricaoEstadualService.findById(inscricaoEstadual.getIdInscricaoEstadual());
			text.append(" ");
			text.append(inscricaoEstadual.getNrInscricaoEstadual());
		}
		field = new TextField(7, 39, text.toString());
		field.setCondensed(true);
		form.addFieldToDetail(field);
	}

	private void montaRemetente(Conhecimento conhecimento, FormHelper form) {
		Pessoa pessoa = pessoaService.findById(conhecimento.getClienteByIdClienteRemetente().getIdCliente());
		Field field;
		Label label;
		StringBuffer text;

		form.addRowToDetail();
		//Nome - Fone
		text = new StringBuffer();
		text.append(FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao().getValue(), pessoa.getNrIdentificacao()));
		text.append(" ");
		text.append(pessoa.getNmPessoa());

		label = new Label(4, "REM:");
		label.setCondensed(true);

		field = new LabelledTextField(7, 39, text.toString(), label);
		field.setCondensed(true);
		form.addFieldToDetail(field);
	}

	private void montaObservacao(Conhecimento conhecimento, FormHelper form) {
		int initRow = 30;
		int nrMaxRows = 3;
		form.addRowToDetail(2);
		form.addFieldToDetail(28, new TextField(7, 45, "   ", true));
		form.addRowToDetail();
		form.addFieldToDetail(29, new TextField(7, 45, "   ", true));
		form.addRowToDetail();

		List<ObservacaoDoctoServico> observacoes = conhecimento.getObservacaoDoctoServicos();
		if(observacoes != null) {
			//ordena a lista de observacoes
			Collections.sort(observacoes, new Comparator<ObservacaoDoctoServico>() {
				public int compare(ObservacaoDoctoServico ob1, ObservacaoDoctoServico ob2) {
					return (ob1.getBlPrioridade().booleanValue() && !ob2.getBlPrioridade().booleanValue()) ? -1 : (!ob1.getBlPrioridade().booleanValue() && ob2.getBlPrioridade().booleanValue()) ? 1 : 0;
				}
			});

			for(int nrRow=0; nrRow<nrMaxRows; nrRow++) {
				if(observacoes.size() > nrRow){
					ObservacaoDoctoServico observacao = (ObservacaoDoctoServico) observacoes.get(nrRow);
					if(validateImprimeNFTModelo3()){
						initRow += 5;
					}
					form.addFieldToDetail(initRow, new TextField(7, 45, observacao.getDsObservacaoDoctoServico(), true));
				} else {
					form.addFieldToDetail(initRow, new TextField(7, 45, "   ", true));
				}
				initRow++;
			}
		}
	}

	private void montaRodape(Conhecimento conhecimento, FormHelper form) {
		if(validateImprimeNFTModelo3()){
			String nrConhecimentoFormatado = FormatUtils.formatSgFilialWithLong(conhecimento.getFilialByIdFilialOrigem().getSgFilial(), conhecimento.getNrDoctoServico());
			form.addFieldToDetail(41, new TextField(67, 12, nrConhecimentoFormatado));
		} else {
			form.addRowToDetail();
		}
	}

	public Boolean validateImprimeNFTModelo3() {
		Boolean ImprimeNFTModelo3 = false;
		
		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findConteudoParametroFilial(SessionUtils.getFilialSessao().getIdFilial(), ConstantesExpedicao.NM_PARAMETRO_IMPRIME_NF_MODELO3, "S");
		
		if(conteudoParametroFilial != null){
			ImprimeNFTModelo3 = true;
		}
		return ImprimeNFTModelo3;
		
	}
	
	private List<TelefoneEndereco> findFoneFax(EnderecoPessoa enderecoPessoa) {
		TelefoneEndereco fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FO");
		if(fone == null) {
			fone = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FF");
		}
		TelefoneEndereco fax = telefoneEnderecoService.findTelefoneEnderecoPadraoPorTpUso(enderecoPessoa.getIdEnderecoPessoa(), "FA");

		List<TelefoneEndereco> result = new ArrayList<TelefoneEndereco>(2);
		result.add(fone);
		result.add(fax);

		return result;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacadeImpl configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
	public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
	
	public void setServicoTributoService(ServicoTributoService servicoTributoService) {
		this.servicoTributoService = servicoTributoService;
	}

	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
}