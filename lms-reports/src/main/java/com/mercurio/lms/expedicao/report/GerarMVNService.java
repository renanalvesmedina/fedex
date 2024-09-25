package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.printform.Alignment;
import com.mercurio.adsm.framework.printform.Configuration;
import com.mercurio.adsm.framework.printform.FormAttributes;
import com.mercurio.adsm.framework.printform.FormHelper;
import com.mercurio.adsm.framework.printform.PrintCommands;
import com.mercurio.adsm.framework.printform.field.NumericField;
import com.mercurio.adsm.framework.printform.field.TextField;
import com.mercurio.adsm.framework.printform.form.HeaderFooter;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.PreManifestoDocumento;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ManifestoNacionalCto;
import com.mercurio.lms.expedicao.model.ManifestoViagemNacional;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.ManifestoNacionalCtoService;
import com.mercurio.lms.expedicao.model.service.ManifestoViagemNacionalService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.ReguladoraSeguro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.util.BigDecimalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;

/**
 * Impressao Matricial Manifesto de Viagem Nacional.
 * @author Andre Valadas
 * @spring.bean id="lms.expedicao.gerarMVNService"
 */
public class GerarMVNService {
	private ManifestoViagemNacionalService manifestoViagemNacionalService;
	private ManifestoNacionalCtoService manifestoNacionalCtoService;
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private ParametroGeralService parametroGeralService;
	private ApoliceSeguroService apoliceSeguroService;
	private ConfiguracoesFacade configuracoesFacade;
	
	private boolean printLabelSeguros = false;

	/**
	 * Gera Formulario para Impressao Matricial.
	 * @author Andre Valadas 
	 * 
	 * @param idManifesto
	 * @param observacao
	 * @return String para Impressao.
	 */
	public String generateMVN(Long idManifesto) {
		/** Parametros de Impressao
		 * -> Nr Colunas
		 * -> Nr Linhas
		 * -> Tamanho da Pagina em Polegadas
		 */
		int larguraPagina=134;
		String observacao = "";
		FormAttributes attributes = new FormAttributes(larguraPagina, 65, PrintCommands.getPageLengthInInches(11));
		attributes.setPageHeaderMaxRows(16);
		attributes.setPageFooterMaxRows(7);
		attributes.setPrintFooterWhen(HeaderFooter.PRINT_LAST_PAGE_ONLY);

		FormHelper form = new FormHelper(attributes, new Configuration(LocaleContextHolder.getLocale()));

		/** Variaveis de Controle de Formulario */
		Long idFilialDestino = null;
		Long idFilialDestinoOLD = null;
		Object[] totalDestino = createArrayValues();
		Object[] totalCarga = createArrayValues();
		Object[] totalGeral = createArrayValues();
		boolean isEmbarcadas = true;
		boolean isEmbarcadasOLD = true;
		boolean printLabel = true;
		boolean printTotalDestino = false;
		boolean printTotalCarga = false;
		Integer rowCount = 0;

		/** Carrega Manifestos */
		ManifestoViagemNacional manifestoViagemNacional = manifestoViagemNacionalService.findById(idManifesto);
		observacao = manifestoViagemNacional.getObManifestoViagemNacional();
		Manifesto manifesto = manifestoViagemNacional.getManifesto();
		manifesto.setManifestoViagemNacional(manifestoViagemNacional);
		final Long idFilialOrigemManifesto = manifesto.getFilialByIdFilialOrigem().getIdFilial();

		/** CABECALHO */
		montaPageHeader(manifesto, form);

		/** Apólice de seguros */
		montaMensagemSeguros(manifestoViagemNacional, larguraPagina, form);

		/** Busca Relacao de Conhecimentos */
		List<ManifestoNacionalCto> manifestoNacionalCtos = manifestoNacionalCtoService.findConhecimentos(idManifesto);
		List<Conhecimento> embarcadas = new ArrayList<Conhecimento>();
		List<Conhecimento> reembarcadas = new ArrayList<Conhecimento>();
		/** Ordena lista para que os primeiros registros sejam da Filial de Origem */
		for (ManifestoNacionalCto manifestoNacionalCto : manifestoNacionalCtos) {
			Conhecimento conhecimento = manifestoNacionalCto.getConhecimento();
			Long idFilial = conhecimento.getFilialByIdFilialOrigem().getIdFilial();
			if (idFilialOrigemManifesto.equals(idFilial)) {
				embarcadas.add(conhecimento);
			} else {
				reembarcadas.add(conhecimento);
			}
		}
		List<Conhecimento> conhecimentos = new ArrayList<Conhecimento>();
		conhecimentos.addAll(embarcadas);
		conhecimentos.addAll(reembarcadas);

		for (Conhecimento conhecimento : conhecimentos) {
			rowCount++;
			/** QUEBRA POR FILIAL DESTINO */
			idFilialDestino = conhecimento.getFilialByIdFilialDestino().getIdFilial();
			printTotalDestino = (idFilialDestinoOLD != null && idFilialDestinoOLD != idFilialDestino);
			idFilialDestinoOLD = idFilialDestino;
			if (printTotalDestino) {
				montaTotais(true, isEmbarcadas, totalDestino, form);
				totalDestino = createArrayValues();
				printTotalDestino = false;
			}

			/** QUEBRA POR TIPO DE CARGA */
			isEmbarcadas = (idFilialOrigemManifesto == conhecimento.getFilialByIdFilialOrigem().getIdFilial());
			printTotalCarga = (rowCount > 1 && isEmbarcadas != isEmbarcadasOLD);
			isEmbarcadasOLD = isEmbarcadas;
			if (printTotalCarga) {
				montaTotais(true, isEmbarcadas, totalDestino, form);
				montaTotais(false, isEmbarcadas, totalCarga, form);
				totalDestino = createArrayValues();
				totalCarga = createArrayValues();
				printTotalCarga = false;
				printLabel = true;
			}
			/** LABEL TIPO DE CARGA */
			if (printLabel) {
				montaLabelCarga(isEmbarcadas, form);
				printLabel = false;
			}
			
			/** DETAIL: CONHECIMENTO */
			Object[] totalConhecimento = findDescricaoConhecimento(conhecimento, form, rowCount);
			/** CONSIGNATARIO */
			montaConsignatario(conhecimento, form);
			/** REDESPACHO */
			montaRedespacho(conhecimento, form);

			/** TOTALIZADORES */
			totalDestino = addArrayValues(totalDestino, totalConhecimento);
			totalCarga = addArrayValues(totalCarga, totalConhecimento);
			totalGeral = addArrayValues(totalGeral, totalConhecimento);
		}

		/** SUMMARY */
		if(!conhecimentos.isEmpty()) {
			montaTotais(true, isEmbarcadas, totalDestino, form);
			montaTotais(false, isEmbarcadas, totalCarga, form);
			montaLegendaSeguro(form);
			montaTextoAcordoEntregaViagem(form);
		} else {
			/** RETORNO VAZIO */
			observacao = "* * * RETORNO VAZIO * * *";
			montaLegendaRetornoVazio(form);
		}
		/** RODAPE */
		montaPageFooter(manifesto, observacao, totalGeral, form);

		return form.write();
	}

 	private void montaMensagemSeguros(ManifestoViagemNacional manifestoViagemNacional, int larguraPagina, FormHelper form) {
 		
		List<ApoliceSeguro> retornaApolices = apoliceSeguroService.retornaApolices(manifestoViagemNacional.getManifesto().getTpModal().getValue(), new Date());
		
		String[] parametrosTNT = {"", "", ""};
		
		//1) Buscar dados da(s) seguradora(s) TNT
		
		boolean variasApolices = !retornaApolices.isEmpty() && retornaApolices.size()>1;
		boolean mesmaSeguradora = true;
		ApoliceSeguro apoliceDeMaiorValor = null;
		
     	for (ApoliceSeguro as : retornaApolices) {
     		apoliceDeMaiorValor = (apoliceDeMaiorValor != null && apoliceDeMaiorValor.getVlLimiteApolice().doubleValue() > as.getVlLimiteApolice().doubleValue()) ? apoliceDeMaiorValor : as ;  
     		if(!apoliceDeMaiorValor.getSeguradora().getIdSeguradora().equals(as.getSeguradora().getIdSeguradora()))
     			mesmaSeguradora = false;
		}
     	
     	if(variasApolices){
         	if(mesmaSeguradora){
             	for (ApoliceSeguro as : retornaApolices) 
             		parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" "+as.getNrApolice()+", ";
             	parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa())+" ";
             	parametrosTNT[2] += NumberFormat.getInstance().format(apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
         	}else{
             	for (ApoliceSeguro as : retornaApolices) 
             		parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" "+as.getNrApolice()+" "+apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa());
             	parametrosTNT[2] += NumberFormat.getInstance().format(apoliceDeMaiorValor.getVlLimiteApolice().doubleValue())+".";
         	}
     	}else{
         	for (ApoliceSeguro as : retornaApolices) 
         		parametrosTNT[0] += as.getTipoSeguro().getSgTipo()+" " +as.getNrApolice();
         	parametrosTNT[1] += apoliceDeMaiorValor.getSeguradora().getPessoa().getNmFantasia()+" CNPJ "+FormatUtils.formatIdentificacao(apoliceDeMaiorValor.getSeguradora().getPessoa())+" ";
         	parametrosTNT[2] += NumberFormat.getInstance().format(apoliceDeMaiorValor.getVlLimiteApolice().doubleValue());
     	}
     	
     	String mensagemSegurosTNT = getConfiguracoesFacade().getMensagem("LMS-04410", parametrosTNT ).replace("  ", " ").replace(", ,", ", ");
     	
		adicionarCamposComQuebraDeLinha(mensagemSegurosTNT, larguraPagina, form);
     	
     	//2) Buscar Seguradora Cliente(s)
     	
		Manifesto manif = manifestoViagemNacional.getManifesto();  
		
		Map<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>> segurosClientes = new HashMap<Cliente, Map<ReguladoraSeguro, List<SeguroCliente>>>();
		
		for(PreManifestoDocumento pmd : manif.getPreManifestoDocumentos()){ 
			
			DoctoServico doc = pmd.getDoctoServico();
			Cliente cli = doc.getClienteByIdClienteRemetente();
			
			for(Object obj : cli.getSeguroClientes()){
				SeguroCliente sc = (SeguroCliente) obj;
				if("N".equals(sc.getTpAbrangencia().getValue())//Nacional
						&& sc.getDtVigenciaInicial().toDateMidnight().toDate().before(new Date())//esta vigente?
						&& sc.getDtVigenciaFinal().toDateMidnight().toDate().after(new Date())){
					
					Map<ReguladoraSeguro, List<SeguroCliente>> seguros = null;
					if(!segurosClientes.containsKey(cli)){
						seguros = new HashMap<ReguladoraSeguro, List<SeguroCliente>>();
						segurosClientes.put(cli, seguros);
					}else{
						seguros = segurosClientes.get(cli);
					}

					if(!seguros.containsKey(sc.getReguladoraSeguro()))
						seguros.put(sc.getReguladoraSeguro(), new ArrayList<SeguroCliente>());

					if(!seguros.get(sc.getReguladoraSeguro()).contains(sc))
						seguros.get(sc.getReguladoraSeguro()).add(sc);

				}
			}
		}
		
		//imprime os valores no formulario
		if(!segurosClientes.isEmpty()){
			
			Iterator<Cliente> iteratorClientes = segurosClientes.keySet().iterator();
			
			while(iteratorClientes.hasNext()){
				String[] param = {"", "", ""}; 
				BigDecimal valor = BigDecimal.ZERO;
				Cliente cliente = iteratorClientes.next();
				param[0] = (cliente.getPessoa().getNmFantasia()!=null?cliente.getPessoa().getNmFantasia():cliente.getPessoa().getNmPessoa())+" CNPJ "+FormatUtils.formatIdentificacao(cliente.getPessoa());
				
				Map<ReguladoraSeguro, List<SeguroCliente>> map = segurosClientes.get(cliente);
				Iterator<ReguladoraSeguro> iteratorSeguros = map.keySet().iterator();
				while(iteratorSeguros.hasNext()){
					
					ReguladoraSeguro next = iteratorSeguros.next();
					List<SeguroCliente> list = map.get(next);
					
					for (SeguroCliente seguroCliente : list) { 
						param[1] += seguroCliente.getTipoSeguro().getSgTipo()+" "+seguroCliente.getDsApolice() + (!isLast(list, seguroCliente)?", ":"");
						valor = seguroCliente.getVlLimite().doubleValue() > valor.doubleValue() ? seguroCliente.getVlLimite() : valor; 
					}

				}
				param[2] = NumberFormat.getCurrencyInstance().format(valor).replace("R$ ", "");
				
				//LMS-04411 "Apólice(s) própria(s): {0} apólice(s) de seguro(s) {1}, averbação de R$ {2}."
				String mensagemSeguroClientes = getConfiguracoesFacade().getMensagem("LMS-04411", param); 
				
				adicionarCamposComQuebraDeLinha(mensagemSeguroClientes, larguraPagina, form);
				
			}
			form.addRowToSummary(1);
			
		}
			
	}
 	
	/**
 	 * Divide um texto maior que a linha principal em várias linhas.
 	 * @param txt - texto a ser exibido
 	 * @param col - larguraPagina
 	 */
	private void adicionarCamposComQuebraDeLinha(String txt, int col, FormHelper form) {
		col -= 6;//diminui a margem direira e esquerda (2x3)
		double nrLinhasDouble = (((double)txt.length()) / ((double)col));
		int nrLinhas = new BigDecimal(nrLinhasDouble).setScale(0, BigDecimal.ROUND_UP).intValue();
		for (int i = 0; i < nrLinhas; i++) {
			int de = i*col;
			int ate = de+col;
			String txtLinha = ate < txt.length() ? txt.substring(de, ate) : txt.substring(de);
		 	form.addFieldToSummary(new TextField(3, (col), txtLinha));
		 	form.addRowToSummary(1);
		}
	}
 	
 	/**
 	 * O Objeto é o último ítem da lista?
 	 * @param list
 	 * @param item
 	 * @return
 	 */
 	@SuppressWarnings("rawtypes")
	private boolean isLast(List list, Object item){
 		return (list.indexOf(item))==(list.size()-1);
 	}
 	
	/**
	 * Monta Cabecalho do Manifesto.
	 * @param manifesto
	 * @param form
	 */
	private void montaPageHeader(Manifesto manifesto, FormHelper form) {
		StringBuilder text;
		/** Emitente */
		Pessoa emitente = manifesto.getFilialByIdFilialOrigem().getPessoa();
		EnderecoPessoa enderecoEmitente = findEnderecoPessoa(emitente.getIdPessoa());
		TelefoneEndereco fone = (TelefoneEndereco) enderecoEmitente.getTelefoneEnderecos().get(0);
		TelefoneEndereco fax = (TelefoneEndereco) enderecoEmitente.getTelefoneEnderecos().get(1);
		/** Proprietario */
		
		Proprietario proprietario = manifesto.getControleCarga().getProprietario();
		EnderecoPessoa enderecoProprietario = null;
		if(proprietario != null){
			enderecoProprietario = findEnderecoPessoa(proprietario.getPessoa().getIdPessoa());
		}
		/** Motorista */
		Motorista motorista = manifesto.getControleCarga().getMotorista();
		EnderecoPessoa enderecoMotorista = findEnderecoPessoa(motorista.getPessoa().getIdPessoa());

		/** Controle Carga */
		MeioTransporte meioTransportado = manifesto.getControleCarga().getMeioTransporteByIdTransportado();
		MeioTransporte meioSemiRebocado = manifesto.getControleCarga().getMeioTransporteByIdSemiRebocado();

		/** Inicio do Cabecalho */
		form.addRowToPageHeader(3);
		form.addFieldToPageHeader(new TextField(7, 31, emitente.getNmFantasia()));
		form.addFieldToPageHeader(new TextField(39, 2, enderecoEmitente.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		form.addFieldToPageHeader(new TextField(45, 3, manifesto.getFilialByIdFilialOrigem().getSgFilial()));

		form.addRowToPageHeader();
		text = new StringBuilder();
		text.append(enderecoEmitente.getTipoLogradouro().getDsTipoLogradouro())
			.append(" ")
			.append(enderecoEmitente.getDsEndereco())
			.append(", ")
			.append(enderecoEmitente.getNrEndereco());
		if (StringUtils.isNotBlank(enderecoEmitente.getDsComplemento())) {
			text.append(" - ")
				.append(enderecoEmitente.getDsComplemento());
		}
		form.addFieldToPageHeader(new TextField(6, 40, text.toString()));
		form.addFieldToPageHeader(new TextField(84, 15, enderecoEmitente.getMunicipio().getNmMunicipio()));
		form.addFieldToPageHeader(new TextField(99, 10, JTFormatUtils.format(manifesto.getDhEmissaoManifesto(), JTFormatUtils.DEFAULT, JTFormatUtils.YEARMONTHDAY)));
		form.addFieldToPageHeader(new TextField(110, 3, manifesto.getFilialByIdFilialOrigem().getSgFilial()));
		form.addFieldToPageHeader(new TextField(114, 6, FormatUtils.formatDecimal("000000", manifesto.getManifestoViagemNacional().getNrManifestoOrigem())));
		form.addFieldToPageHeader(new TextField(123, 3, manifesto.getFilialByIdFilialDestino().getSgFilial()));

		form.addRowToPageHeader();
		form.addFieldToPageHeader(new TextField(5, 9, FormatUtils.formatCep(enderecoEmitente.getMunicipio().getUnidadeFederativa().getPais().getSgPais(), enderecoEmitente.getNrCep())));
		if (fone != null) {
			text = new StringBuilder();
			text.append(StringUtils.defaultString(fone.getNrDdi()))
				.append(" ")
				.append(fone.getDddTelefone());
			form.addFieldToPageHeader(new TextField(16, 16, text.toString()));
		}
		if (fax != null) {
			form.addFieldToPageHeader(new TextField(36, 11, StringUtils.defaultString(fax.getNrTelefone())));
		}

		form.addRowToPageHeader();
		form.addFieldToPageHeader(new TextField(7, 20, FormatUtils.formatIdentificacao(emitente.getTpIdentificacao().getValue(), emitente.getNrIdentificacao())));

		if (emitente.getInscricaoEstaduais() != null && !emitente.getInscricaoEstaduais().isEmpty()) {
			InscricaoEstadual inscricaoEstadual = (InscricaoEstadual) emitente.getInscricaoEstaduais().get(0);
			form.addFieldToPageHeader(new TextField(33, 13, inscricaoEstadual.getNrInscricaoEstadual()));
		}

		/** Proprietario */
		form.addRowToPageHeader(2);
		text = new StringBuilder();
		/** Formata Identificacao se Proprietario for Pessoa Fisica */
		if(proprietario !=null){
		String tpIdentificacao = proprietario.getPessoa().getTpIdentificacao().getValue();
		if ("CPF".equals(tpIdentificacao)) {
			text.append(FormatUtils.formatIdentificacao(tpIdentificacao, proprietario.getPessoa().getNrIdentificacao()));
		} else text.append(proprietario.getPessoa().getNrIdentificacao());
		form.addFieldToPageHeader(new TextField(3, 14, text.toString()));
		form.addFieldToPageHeader(new TextField(18, 24, proprietario.getPessoa().getNmPessoa()));
		text = new StringBuilder();
		text.append(enderecoProprietario.getTipoLogradouro().getDsTipoLogradouro())
			.append(" ")
			.append(enderecoProprietario.getDsEndereco())
			.append(", ")
			.append(enderecoProprietario.getNrEndereco());
		if (StringUtils.isNotBlank(enderecoProprietario.getDsComplemento())) {
			text.append(" - ")
				.append(enderecoProprietario.getDsComplemento());
		}
		form.addFieldToPageHeader(new TextField(49, 35, text.toString()));
		form.addFieldToPageHeader(new TextField(85, 15, enderecoProprietario.getMunicipio().getNmMunicipio()));
		form.addFieldToPageHeader(new TextField(101, 2, enderecoProprietario.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		form.addFieldToPageHeader(new TextField(103, 8, enderecoProprietario.getNrCep()));
		if(proprietario.getNrPis() != null) {
			form.addFieldToPageHeader(new TextField(112, 10, StringUtils.defaultString(proprietario.getNrPis().toString()), Alignment.ALIGN_RIGHT));
		}
		}

		/** Motorista */
		form.addRowToPageHeader(2);
		form.addFieldToPageHeader(new TextField(3, 14, FormatUtils.formatIdentificacao(motorista.getPessoa().getTpIdentificacao().getValue(), motorista.getPessoa().getNrIdentificacao())));
		form.addFieldToPageHeader(new TextField(18, 24, motorista.getPessoa().getNmPessoa()));
		text = new StringBuilder();
		if (enderecoMotorista != null) {
			text.append(enderecoMotorista.getTipoLogradouro().getDsTipoLogradouro())
				.append(" ")
				.append(enderecoMotorista.getDsEndereco())
				.append(", ")
				.append(enderecoMotorista.getNrEndereco());
			if (StringUtils.isNotBlank(enderecoMotorista.getDsComplemento())) {
				text.append(" - ")
					.append(enderecoMotorista.getDsComplemento());
			}
			form.addFieldToPageHeader(new TextField(49, 35, text.toString()));
			form.addFieldToPageHeader(new TextField(85, 15, enderecoMotorista.getMunicipio().getNmMunicipio()));
			form.addFieldToPageHeader(new TextField(101, 2, enderecoMotorista.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
			form.addFieldToPageHeader(new TextField(103, 8, enderecoMotorista.getNrCep()));
		}
		form.addFieldToPageHeader(new TextField(112, 10, StringUtils.defaultString(motorista.getNrCarteiraHabilitacao().toString()), Alignment.ALIGN_RIGHT));
		form.addFieldToPageHeader(new TextField(124, 10, StringUtils.defaultString(motorista.getNrProntuario().toString()), Alignment.ALIGN_RIGHT));

		/** Veiculo */
		form.addRowToPageHeader(2);
		form.addFieldToPageHeader(new TextField(11, 10, meioTransportado.getNrIdentificador()));
		form.addFieldToPageHeader(new TextField(23, 6, meioTransportado.getNrFrota()));
		form.addFieldToPageHeader(new TextField(31, 20, meioTransportado.getMeioTransporteRodoviario().getMunicipio().getNmMunicipio()));
		form.addFieldToPageHeader(new TextField(53, 2, meioTransportado.getMeioTransporteRodoviario().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()));
		form.addFieldToPageHeader(new TextField(58, 11, meioTransportado.getModeloMeioTransporte().getMarcaMeioTransporte().getDsMarcaMeioTransporte()));
		form.addFieldToPageHeader(new TextField(70, 9, meioTransportado.getModeloMeioTransporte().getDsModeloMeioTransporte()));
		form.addFieldToPageHeader(new TextField(81, 4, meioTransportado.getNrAnoFabricao().toString()));
		NumericField capacidadeKg = new NumericField(119, 8, meioTransportado.getNrCapacidadeKg());
		capacidadeKg.setNumberFormat(new DecimalFormat("#,##0", new DecimalFormatSymbols(LocaleContextHolder.getLocale())));
		form.addFieldToPageHeader(capacidadeKg);
		form.addFieldToPageHeader(new NumericField(127, 7, meioTransportado.getNrCapacidadeM3()));

		/** Semi Reboque pode nao existir */
		form.addRowToPageHeader(2);
		if (meioSemiRebocado != null) {
			form.addFieldToPageHeader(new TextField(11, 10, meioSemiRebocado.getNrIdentificador()));
			form.addFieldToPageHeader(new TextField(23, 6, meioSemiRebocado.getNrFrota()));
			Municipio municipio = meioSemiRebocado.getMeioTransporteRodoviario().getMunicipio();
			if(municipio != null) {
				form.addFieldToPageHeader(new TextField(31, 20, municipio.getNmMunicipio()));
				form.addFieldToPageHeader(new TextField(53, 2, municipio.getUnidadeFederativa().getSgUnidadeFederativa()));
			}
			form.addFieldToPageHeader(new TextField(58, 11, meioSemiRebocado.getModeloMeioTransporte().getMarcaMeioTransporte().getDsMarcaMeioTransporte()));
			form.addFieldToPageHeader(new TextField(70, 9, meioSemiRebocado.getModeloMeioTransporte().getDsModeloMeioTransporte()));
			form.addFieldToPageHeader(new TextField(81, 4, meioSemiRebocado.getNrAnoFabricao().toString()));
			capacidadeKg = new NumericField(119, 8, meioSemiRebocado.getNrCapacidadeKg());
			capacidadeKg.setNumberFormat(new DecimalFormat("#,##0", new DecimalFormatSymbols(LocaleContextHolder.getLocale())));
			form.addFieldToPageHeader(capacidadeKg);
			form.addFieldToPageHeader(new NumericField(128, 6, meioSemiRebocado.getNrCapacidadeM3()));
		}
	}

	/**
	 * Monta Descricao dos Conhecimentos.
	 * @param manifesto
	 * @param form
	 * @return 
	 */
	private Object[] findDescricaoConhecimento(Conhecimento conhecimento, FormHelper form, Integer rows) {

						
		/** Remetente */
		form.addFieldToDetail(new TextField(3, 24, conhecimento.getClienteByIdClienteRemetente().getPessoa().getNmPessoa()));
		/** Destinatario */
		form.addFieldToDetail(new TextField(28, 24, conhecimento.getClienteByIdClienteDestinatario().getPessoa().getNmPessoa()));
		/** Destino */
		form.addFieldToDetail(new TextField(54, 15, conhecimento.getMunicipioByIdMunicipioEntrega().getNmMunicipio()));
		
		List<NotaFiscalConhecimento> list = new ArrayList<NotaFiscalConhecimento>(conhecimento.getNotaFiscalConhecimentos());
		if(list != null && !list.isEmpty()){
			NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento) list.get(0);
		
		/** N. Fiscal */
		form.addFieldToDetail(new TextField(69, 9, FormatUtils.formatDecimal("000000000", notaFiscalConhecimento.getNrNotaFiscal())));
		/** Serie */
		form.addFieldToDetail(new TextField(76, 2, StringUtils.defaultString(notaFiscalConhecimento.getDsSerie())));
		}
		
		/** Valor Mercadorias */
		form.addFieldToDetail(new NumericField(78, 14, conhecimento.getVlMercadoria()));
		/** Quantidade */
		form.addFieldToDetail(new TextField(93, 5, IntegerUtils.defaultInteger(conhecimento.getQtVolumes()).toString(), Alignment.ALIGN_RIGHT));
		/** Peso */
		form.addFieldToDetail(new NumericField(99, 6, conhecimento.getPsReal()));
		/** Tipo Frete */
		form.addFieldToDetail(new TextField(106, 1, conhecimento.getTpFrete().getValue()));
		/** Valor frete */
		form.addFieldToDetail(new NumericField(108, 12, conhecimento.getVlTotalDocServico()));
		/** Conhecimento */
		String numero = 
			conhecimento.getFilialByIdFilialOrigem().getSgFilial()+
			FormatUtils.formatLongWithZeros(conhecimento.getNrConhecimento(), "00000000")+
            "/"+conhecimento.getDvConhecimento();
		form.addFieldToDetail(new TextField(121, 14, numero));
		/** Se Existe Seguro Proprio */
		List seguros = conhecimento.getDoctoServicoSeguros();
		if (seguros != null && !seguros.isEmpty()) {
			printLabelSeguros = true;
			form.addFieldToDetail(new TextField(132, 1, "*"));
		}
		form.addRowToDetail();
		
		if (list.size() > 1){
			int startPosition = 0;
			int nfCount = 0;
			boolean printLabel = true;
			int lineLimit = 12;
			for (int index = 2;index<=list.size();index++){
				NotaFiscalConhecimento notaFiscalConhecimento = list.get(index-1);
				
				if (startPosition == 0){
					startPosition+=3;
					if (printLabel){
						form.addFieldToDetail(new TextField(startPosition, 15, "NOTAS FISCAIS:"));
						printLabel = false;
						startPosition+=16;
					}
				}

				/** N. Fiscal */
				form.addFieldToDetail(new TextField(startPosition, 9, FormatUtils.formatDecimal("000000000", notaFiscalConhecimento.getNrNotaFiscal())));
				startPosition+=10;
				
				nfCount++;
				if (nfCount==lineLimit || index == list.size()){
					printLabel = false;
					form.addRowToDetail();
					nfCount=0;
					startPosition=0;
					rows = Integer.valueOf(rows +1);
				}
			}
		}
		
		/** Adiciona Totais */
		Object[] valores = createArrayValues();
		valores[0] = conhecimento.getVlMercadoria();
		valores[1] = conhecimento.getQtVolumes();
		valores[2] = conhecimento.getPsReal();
		valores[3] = conhecimento.getVlTotalDocServico();
		valores[4] = IntegerUtils.ONE;
		return valores;
	}

	/**
	 * Imprime Consignatario caso o mesmo exista.
	 * @param conhecimento
	 * @param form
	 */
	private void montaConsignatario(Conhecimento conhecimento, FormHelper form) {
		Cliente consignatario = conhecimento.getClienteByIdClienteConsignatario();
		if (consignatario != null) {
			/** Campo Destinatario */
			form.addFieldToDetail(new TextField(28, 24, consignatario.getPessoa().getNmPessoa()));
			/** Campo Destino */
			form.addFieldToDetail(new TextField(53, 15, "CONSIGNATÁRIO"));
			form.addRowToDetail();
		}
	}

	/**
	 * Imprime Redespacho caso o mesmo exista.
	 * @param conhecimento
	 * @param form
	 */
	private void montaRedespacho(Conhecimento conhecimento, FormHelper form) {
		Cliente redespacho = conhecimento.getClienteByIdClienteRedespacho();
		if (redespacho != null) {
			/** Campo Destinatario */
			form.addFieldToDetail(new TextField(28, 24, redespacho.getPessoa().getNmPessoa()));
			/** Campo Destino */
			form.addFieldToDetail(new TextField(53, 15, "REDESPACHO"));
			form.addRowToDetail();
		}
	}

	/**
	 * Label para Tipos de Cargas
	 * @param isEmbarcadas
	 * @param form
	 */
	private void montaLabelCarga(boolean isEmbarcadas, FormHelper form) {
		if (isEmbarcadas) {
			form.addFieldToDetail(new TextField(3, 32, "C A R G A S  E M B A R C A D A S"));
		} else {
			form.addFieldToDetail(new TextField(3, 35, "C A R G A S  R E E M B A R C A D A S"));	
		}
		form.addRowToDetail();
	}

	/**
	 * Monta Totalizadores dos Conhecimentos por Destino/Tipo de Carga.
	 * @param byDestino
	 * @param isEmbarcados
	 * @param valores
	 * @param form
	 */
	private void montaTotais(boolean byDestino, boolean isEmbarcadas, Object[] valores, FormHelper form) {
		if (byDestino) {
			form.addFieldToDetail(new TextField(53, 19, "TOTAIS DESTINO --->"));
		} else {
			form.addFieldToDetail(new TextField(30, 43, "TOTAIS "+(!isEmbarcadas ? "REEMBARCADOS " : "EMBARCADOS --")+"--------------------->"));
		}

		/** Valor Total Mercadorias */
		form.addFieldToDetail(new NumericField(78, 14,(BigDecimal)valores[0]));
		/** Total Quantidade */
		form.addFieldToDetail(new TextField(93, 5, String.valueOf(valores[1]), Alignment.ALIGN_RIGHT));
		/** Total Peso */
		form.addFieldToDetail(new NumericField(99, 6, (BigDecimal)valores[2]));
		/** Valor Total frete */
		form.addFieldToDetail(new NumericField(108, 12, (BigDecimal)valores[3]));
		/** qtd Total Conhecimentos */
		form.addFieldToDetail(new TextField(121, 10, String.valueOf(valores[4]), Alignment.ALIGN_RIGHT));
		form.addRowToDetail(byDestino ? 1 : 2);
	}

	/**
	 * Imprime Legenda sobre (8) dos seguros.
	 * @param form
	 */
	private void montaLegendaSeguro(FormHelper form) {
		if (!printLabelSeguros) {
			return;
		}
		/** Descritivo Legenda */
		form.addFieldToSummary(new TextField(4, 41, "(*) CONHECIMENTOS NAO COBERTOS POR SEGURO"));
	}

	/**
	 * Imprime frase relativa aa concordancia de ao menos 01 entrega ao destino
	 * @param form
	 */
	private void montaTextoAcordoEntregaViagem(FormHelper form) {
		form.addFieldToSummary(new TextField(5, 119, this.getParametroGeralService().findByNomeParametro("DS_ACORDO_ENTREGA_VIAGEM", false).getDsConteudo()));
	}

	/**
	 * Imprime Legenda RETORNO VAZIO para manifestos que nao possuam ctos.
	 * @param form
	 */
	private void montaLegendaRetornoVazio(FormHelper form) {
		/** Descritivo Legenda */
		form.addFieldToSummary(new TextField(5, 42, "* * * *  R E T O R N O  V A Z I O  * * * *"));
	}

	/**
	 * Rodape do Formulario, sera impresso apenas na ultima pagina do mesmo.
	 * @param manifesto
	 * @param totalGeral
	 * @param form
	 */
	private void montaPageFooter(Manifesto manifesto, String observacao, Object[] totalGeral, FormHelper form) {
		form.addRowToPageFooter(2);
		/** Valor Total Mercadorias */
		form.addFieldToPageFooter(new NumericField(78, 14, manifesto.getVlTotalManifestoEmissao()));
		/** Total Quantidade */
		form.addFieldToPageFooter(new TextField(93, 5, manifesto.getQtTotalVolumesEmissao().toString(), Alignment.ALIGN_RIGHT));
		/** Total Peso */
		form.addFieldToPageFooter(new NumericField(99, 8, manifesto.getPsTotalManifestoEmissao()));
		/** Valor Total Frete PAGO */
		form.addFieldToPageFooter(new NumericField(108, 12, manifesto.getVlTotalFreteCifEmissao()));
		/** Valor Total Frete A PAGAR */
		form.addFieldToPageFooter(new NumericField(110, 14, manifesto.getVlTotalFreteFobEmissao()));
		form.addRowToPageFooter(2);

		/** Distribui Observacao */
		int indexChar = 0;
		int maxCharShortWidth = 30;
		int maxCharLongWidth = 55;
		/** Observacao 1 (ShortWidth) */
		if (observacao != null) {
			StringBuilder text = new StringBuilder();
			while ((indexChar < observacao.length()) && (indexChar < maxCharShortWidth)) {
				text.append(observacao.charAt(indexChar));
				indexChar++;
			}
			form.addFieldToPageFooter(new TextField(78, maxCharShortWidth, text.toString()));
		}
		/** Valor Total Frete */
		form.addFieldToPageFooter(new NumericField(108, 20, manifesto.getVlTotalFreteEmissao()));
		/** qtd Total Conhecimentos */
		form.addFieldToPageFooter(new TextField(130, 4, String.valueOf(totalGeral[4]), Alignment.ALIGN_RIGHT));
		form.addRowToPageFooter();

		int initToLastRow = 2;
		/** Observacao 2 (LongWidth) para duas linhas */
		if (observacao != null) {
			StringBuilder text = new StringBuilder();
			while (indexChar < observacao.length()) {
				text.append(observacao.charAt(indexChar));
				indexChar++;
				if ((indexChar-maxCharShortWidth) == maxCharLongWidth) {
					form.addFieldToPageFooter(new TextField(78, maxCharLongWidth, text.toString()));
					text = new StringBuilder();
					initToLastRow--;
					form.addRowToPageFooter();
				}
			}
			form.addFieldToPageFooter(new TextField(78, maxCharLongWidth, text.toString()));
		}
		form.addRowToPageFooter(initToLastRow);
		form.addFieldToPageFooter(new TextField(58, 76, "PARA ESTE CARREGAMENTO O MOTORISTA DECLARA QUE FOI INSTRUIDO ACERCA DA REGRA GER. RISCO DENOMINA PROCEDIMENTO PADRAO", true));
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
		}
		return enderecoPessoa;
	}

	/**
	 * Cria Array de Objects com tipos pre-definidos
	 * @return new Object[5]
	 */
	private Object[] createArrayValues() {
		return new Object[]{
			BigDecimalUtils.ZERO,//vlMercadoria
			IntegerUtils.ZERO,//qtVolumes
			BigDecimalUtils.ZERO,//psReal
			BigDecimalUtils.ZERO,//vlToralFrete
			IntegerUtils.ZERO};//qtdConhecimentos
	}

	/**
	 * Adiciona valores para cada indice do array
	 * @param array
	 * @param addArray
	 * @return
	 */
	private Object[] addArrayValues(Object[] array, Object[] addArray) {
		Object[] result = createArrayValues();
		result[0] = BigDecimalUtils.add((BigDecimal)array[0], (BigDecimal)addArray[0]);
		result[1] = IntegerUtils.addNull((Integer)array[1], (Integer)addArray[1]);
		result[2] = BigDecimalUtils.add((BigDecimal)array[2], (BigDecimal)addArray[2]);
		result[3] = BigDecimalUtils.add((BigDecimal)array[3], (BigDecimal)addArray[3]);
		result[4] = IntegerUtils.addNull((Integer)array[4], (Integer)addArray[4]);
		return result;
	}

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setManifestoViagemNacionalService(ManifestoViagemNacionalService manifestoViagemNacionalService) {
		this.manifestoViagemNacionalService = manifestoViagemNacionalService;
	}
	public void setManifestoNacionalCtoService(ManifestoNacionalCtoService manifestoNacionalCtoService) {
		this.manifestoNacionalCtoService = manifestoNacionalCtoService;
	}
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}
	
	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}