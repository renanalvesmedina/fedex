package com.mercurio.lms.expedicao.util.nfe.converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.nfe.NfeConverterUtils;
import com.mercurio.lms.layoutNfse.model.rps.AliquotasComplementares;
import com.mercurio.lms.layoutNfse.model.rps.Contato;
import com.mercurio.lms.layoutNfse.model.rps.DadosAdic;
import com.mercurio.lms.layoutNfse.model.rps.DadosComplementaresPrestador;
import com.mercurio.lms.layoutNfse.model.rps.DadosComplementaresServico;
import com.mercurio.lms.layoutNfse.model.rps.DadosComplementaresTomador;
import com.mercurio.lms.layoutNfse.model.rps.Email;
import com.mercurio.lms.layoutNfse.model.rps.EmailDadosAdicionais;
import com.mercurio.lms.layoutNfse.model.rps.Endereco;
import com.mercurio.lms.layoutNfse.model.rps.IdentificacaoRps;
import com.mercurio.lms.layoutNfse.model.rps.IdentificacaoTomador;
import com.mercurio.lms.layoutNfse.model.rps.InfRps;
import com.mercurio.lms.layoutNfse.model.rps.Item;
import com.mercurio.lms.layoutNfse.model.rps.ListaItens;
import com.mercurio.lms.layoutNfse.model.rps.Prestador;
import com.mercurio.lms.layoutNfse.model.rps.ReceiverToList;
import com.mercurio.lms.layoutNfse.model.rps.Rps;
import com.mercurio.lms.layoutNfse.model.rps.Servico;
import com.mercurio.lms.layoutNfse.model.rps.Tomador;
import com.mercurio.lms.layoutNfse.model.rps.Valores;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.Cliente;

public abstract class RpsConverter {
	
	private final String versaoNfe;
	private final String naturezaOperacaoNfe;
	private final String regimeTributarioNfe;
	private final String serieNfe;
	private final String codigoCnae;
	private final String codigoTribMunicipio;
	private final String servicoNte;
	private final String bairroPadrao;
	private final Long nrFiscalRps;
	private final String tipoDocumento;
	private final String munIncidenciaOutro;
	private final String codMunicipioOutros;
	private final Boolean nfseGeraVlrIss;
	private final Boolean nfseIssPrestOutroLocal;
	private Boolean nfseRetEspIss;
	private Boolean reenviarDataAtual;
	
	protected abstract DoctoServico getDoctoServico();
	protected abstract List<ImpostoServico> getImpostoServicos();
	protected abstract List<NotaFiscalConhecimento> getNotaFiscalConhecimentos();
	protected abstract Municipio getMunicipioPrestacaoServico();
	
	public RpsConverter(String versaoNfe, String naturezaOperacaoNfe,
			String regimeTributarioNfe, String serieNfe, String codigoCnae,
			String codigoTribMunicipio,String servicoNte, String bairroPadrao, Long nrFiscalRps, String tipoDocumento,
			String munIncidenciaOutro, String codMunicipioOutros, Boolean nfseGeraVlrIss, Boolean nfseIssPrestOutroLocal, Boolean nfseRetEspIss, Boolean reenviarDataAtual) {
		super();
		this.versaoNfe = versaoNfe;
		this.naturezaOperacaoNfe = naturezaOperacaoNfe;
		this.regimeTributarioNfe = regimeTributarioNfe;
		this.serieNfe = serieNfe;
		this.codigoCnae = codigoCnae;
		this.codigoTribMunicipio = codigoTribMunicipio;
		this.servicoNte = servicoNte;
		this.bairroPadrao = bairroPadrao;
		this.nrFiscalRps = nrFiscalRps;
		this.tipoDocumento = tipoDocumento;
		this.munIncidenciaOutro = munIncidenciaOutro;
		this.codMunicipioOutros = codMunicipioOutros;
		this.nfseGeraVlrIss = nfseGeraVlrIss;
		this.nfseIssPrestOutroLocal = nfseIssPrestOutroLocal;
		this.nfseRetEspIss = nfseRetEspIss;
		this.reenviarDataAtual = reenviarDataAtual;
	}
	
	protected boolean isVersao504() {
		return "5.04".equals(versaoNfe);
	}
	
	protected boolean isVersao510() {
		return "5.10".equals(versaoNfe);
	}

	protected boolean isVersao513() {
		return "5.13".equals(versaoNfe);
	}

	protected boolean isNfseIssPrestOutroLocal() {
		return BooleanUtils.isTrue(nfseIssPrestOutroLocal);
	}
	
	public Rps convert() {
		return createRps();
	}

	private InfRps createInfRps() {
		InfRps infRps = new InfRps();
		int tamanhoId = isVersao504() || isVersao510() ? 15 : 10;
		if (isVersao513()) {
			infRps.setId("RPS"+StringUtils.leftPad(String.valueOf(getDoctoServico().getIdDoctoServico()), tamanhoId, '0'));
		} else {
			infRps.setId(StringUtils.leftPad(String.valueOf(getDoctoServico().getIdDoctoServico()), tamanhoId, '0'));
		}
		infRps.setVersao(versaoNfe);
		DateTime dhEmissao = getDoctoServico().getDhEmissao();
		if (dhEmissao != null) {
			if(Boolean.TRUE.equals(reenviarDataAtual)){
				infRps.setCompetencia(JTDateTimeUtils.getDataHoraAtual().toString("dd'-'MM'-'yyyy' 'HH':'mm':'ss"));
				infRps.setDataEmissao(JTDateTimeUtils.getDataHoraAtual().toString("dd'-'MM'-'yyyy' 'HH':'mm':'ss"));
			}else{
			infRps.setCompetencia(dhEmissao.toString("dd'-'MM'-'yyyy' 'HH':'mm':'ss"));
			infRps.setDataEmissao(dhEmissao.toString("dd'-'MM'-'yyyy' 'HH':'mm':'ss"));
		}
		}
		infRps.setNaturezaOperacao(naturezaOperacaoNfe);
		infRps.setRegimeEspecialTributacao(regimeTributarioNfe);
		infRps.setOptanteSimplesNacional("2");
		infRps.setIncentivoFiscal("2");
		infRps.setStatus("1");
		infRps.setTributarMunicipio(getValorTributar());
		infRps.setTributarPrestador(getValorTributar());
		infRps.setIdentificacaoRps(createIdentificacaoRps());
		
		infRps.setServicos(createServico());
		infRps.setPrestador(createPrestador());
		infRps.setTomador(createTomador());
		if (!isVersao513()) {
			infRps.setEmail(createEmail());
		}
		return infRps;
	}

	private String getValorTributar() {
		if (isNfseIssPrestOutroLocal()) {
			return getMunicipioIncidencia();
		} else {
			return "1";
		}
	}
	private String getMunicipioIncidencia() {
		if (this.validateMunicipioIncidencia()) {
			return "1";
		} else {
			return "2";
		}
	}
	
	private Boolean validateMunicipioIncidencia() {
		Municipio municipioItem81 = getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio();
		Municipio municipioItem50 = getMunicipioPrestacaoServico();
		
		return municipioItem81.getIdMunicipio().equals(municipioItem50.getIdMunicipio());
	}
	
	private DevedorDocServ getDevedorDocServ() {
		return ConhecimentoUtils.getDevedorDocServ(getDoctoServico());
	}

	private String createConcatDadosObservacao() {
		Conhecimento conhecimento = null;
		if(getDoctoServico() instanceof Conhecimento){
			conhecimento = (Conhecimento)getDoctoServico();
		}
		
		StringBuilder observavoes = new StringBuilder();
		if(conhecimento != null && conhecimento.getDadosComplementos() != null && !conhecimento.getDadosComplementos().isEmpty()){
			for (DadosComplemento dadosComplemento : conhecimento.getDadosComplementos()) {
				if(dadosComplemento.getInformacaoDocServico() != null){
					if(dadosComplemento.getInformacaoDocServico().getBlImprimeConhecimento()){
						observavoes.append(dadosComplemento.getInformacaoDocServico().getDsCampo()).append(":");
						observavoes.append(dadosComplemento.getDsValorCampo()).append(",");
					}
				}
				if(dadosComplemento.getInformacaoDoctoCliente() != null){
					if(dadosComplemento.getInformacaoDoctoCliente().getBlImprimeConhecimento()){
						observavoes.append(dadosComplemento.getInformacaoDoctoCliente().getDsCampo()).append(":");
						observavoes.append(dadosComplemento.getDsValorCampo()).append(",");
					}
				}
			}
		}
		
		if(getDoctoServico().getObservacaoDoctoServicos() != null){
			List<ObservacaoDoctoServico> observacoes = new ArrayList<ObservacaoDoctoServico>(getDoctoServico().getObservacaoDoctoServicos());
			//ordena a lista de observacoes
			
			Collections.sort(observacoes, new Comparator<ObservacaoDoctoServico>() {
				@Override
				public int compare(ObservacaoDoctoServico o1, ObservacaoDoctoServico o2) {
					boolean o1BlPrioriedade = o1.getBlPrioridade() == null ? false : o1.getBlPrioridade();
					boolean o2BlPrioriedade = o2.getBlPrioridade() == null ? false : o2.getBlPrioridade();
					
					return (o1BlPrioriedade && !o2BlPrioriedade) ? -1 : (!o1BlPrioriedade && o2BlPrioriedade) ? 1 : 0;
				}
			});
			
			for (ObservacaoDoctoServico observacaoDoctoServico : observacoes) {
				observavoes.append(observacaoDoctoServico.getDsObservacaoDoctoServico()).append(",");
			}
		}
		
		if(StringUtils.isNotBlank(observavoes.toString()) && (observavoes.toString()).charAt((observavoes.toString()).length()-1) == ',' ){
			observavoes = new StringBuilder( observavoes.toString().substring(0, (observavoes.toString()).length()-1) );
		}

		return observavoes.toString();
	}
	
	private Email createEmail() {
		Email email = new Email();
		if (getDevedorDocServ() != null) {
			List<com.mercurio.lms.configuracoes.model.Contato> contatos = NfeConverterUtils.getContatos(getDevedorDocServ().getCliente().getPessoa());
			List<ReceiverToList> receiverToLists = new ArrayList<ReceiverToList>();
			for (com.mercurio.lms.configuracoes.model.Contato c: contatos) {
				if (StringUtils.isNotBlank(c.getDsEmail())) {
					receiverToLists.add(createReceiverTo(c.getDsEmail()));
				}
			}
			if (receiverToLists.isEmpty()) {
				if (StringUtils.isNotBlank(getDevedorDocServ().getCliente().getPessoa().getDsEmail())) {
					receiverToLists.add(createReceiverTo(getDevedorDocServ().getCliente().getPessoa().getDsEmail()));
				}
			}
			
			email.setReceiverToList(receiverToLists);
		}
		return email;
	}

	private ReceiverToList createReceiverTo(String dsEmail) {
		ReceiverToList receiverToList = new ReceiverToList();
		receiverToList.setReceiverTo(NfeConverterUtils.tratarString(dsEmail, Integer.MAX_VALUE));
		return receiverToList;
	}

	private Tomador createTomador() {
		Tomador tomador = new Tomador();
		if (getDevedorDocServ() != null) {
			tomador.setRazaoSocial(NfeConverterUtils.tratarString(getDevedorDocServ().getCliente().getPessoa().getNmPessoa(),120));
		}
		tomador.setIdentificacaoTomador(createIdentificacaoTomador());
		tomador.setEnderecoTomador(createEnderecoTomador());
		tomador.setContato(createContatoTomador());
		tomador.setDadosComplementaresTomador(createDadosComplementaresTomador());
		return tomador;
	}

	private DadosComplementaresTomador createDadosComplementaresTomador() {
		DadosComplementaresTomador dadosComplementaresTomador = new DadosComplementaresTomador();
		if (getDevedorDocServ() != null) {
			EnderecoPessoa ep = getDevedorDocServ().getCliente().getPessoa().getEnderecoPessoa();
			if (ep != null) {
				dadosComplementaresTomador.setTipoLogradouro(NfeConverterUtils.tratarString(ep.getTipoLogradouro().getDsTipoLogradouro().getValue(),10));
				dadosComplementaresTomador.setTipoBairro("Bairro");
				if(isVersao510() || isVersao513()) {
					dadosComplementaresTomador.setPais(NfeConverterUtils.objectToString(ep.getMunicipio().getUnidadeFederativa().getPais().getNmPais(), null));	
				}
				dadosComplementaresTomador.setCidadeDescricao(NfeConverterUtils.tratarString(ep.getMunicipio().getNmMunicipio(),50));
				TelefoneEndereco te = NfeConverterUtils.getTelefoneEndereco(ep, getDevedorDocServ().getCliente().getPessoa().getTpPessoa().getValue());
				
				if(te == null){
					te = NfeConverterUtils.getTelefoneEndereco(getDevedorDocServ().getCliente().getPessoa().getTelefoneEnderecos(), getDevedorDocServ().getCliente().getPessoa().getTpPessoa().getValue(), Boolean.FALSE);
				}
				
				if (te != null && te.getNrDdd() != null && te.getNrDdd().length() == 2) {
					dadosComplementaresTomador.setDDD(te.getNrDdd());
				}
			}
			dadosComplementaresTomador.setInscrEstadual(getDevedorDocServ().getCliente().getPessoa().getNrInscricaoEstadual());
			if (isVersao504() || isVersao510() || isVersao513()) {
				dadosComplementaresTomador.setTipoDocumento(tipoDocumento);
				dadosComplementaresTomador.setCodigoPais(NfeConverterUtils.objectToString(ep.getMunicipio().getUnidadeFederativa().getPais().getNrBacen(), null));
			}
		}
		return dadosComplementaresTomador;
	}

	private Contato createContatoTomador() {
		Contato contato = new Contato();
		if (getDevedorDocServ() != null) {
			TelefoneEndereco te = NfeConverterUtils.getTelefoneEndereco(getDevedorDocServ().getCliente().getPessoa().getEnderecoPessoa(), getDevedorDocServ().getCliente().getPessoa().getTpPessoa().getValue());
			
			if(te == null){
				te = NfeConverterUtils.getTelefoneEndereco(getDevedorDocServ().getCliente().getPessoa().getTelefoneEnderecos(), getDevedorDocServ().getCliente().getPessoa().getTpPessoa().getValue(), Boolean.FALSE);
			}
			
			if (te != null) {
				if (te.getNrTelefone() == null || te.getNrTelefone().length() < 6 || te.getNrTelefone().length() > 8) {
					contato.setTelefone("00000000");
				} else {
					contato.setTelefone(te.getNrTelefone());
				}
				contato.setEmail(NfeConverterUtils.tratarString(getDevedorDocServ().getCliente().getPessoa().getDsEmail(),60));
			}
		}
		return contato;
	}

	private Endereco createEnderecoTomador() {
		Endereco endereco = new Endereco();
		if (getDevedorDocServ() != null) {
			EnderecoPessoa ep = getDevedorDocServ().getCliente().getPessoa().getEnderecoPessoa();
			endereco.setEndereco(NfeConverterUtils.tratarString(ep.getTipoLogradouro().getDsTipoLogradouro().getValue()+" "+ep.getDsEndereco(),125));
			endereco.setNumero(NfeConverterUtils.tratarString(ep.getNrEndereco(),10));
			endereco.setComplemento(NfeConverterUtils.tratarString(ep.getDsComplemento(),60));
			if (StringUtils.isBlank(ep.getDsBairro())) {
				endereco.setBairro(NfeConverterUtils.tratarString(bairroPadrao,60));
			} else {
				endereco.setBairro(NfeConverterUtils.tratarString(ep.getDsBairro(),60));
			}
			endereco.setCodigoMunicipio(NfeConverterUtils.formatCMun(ep.getMunicipio()));
			endereco.setCodigoMunicipioSiafi(ep.getMunicipio().getCdSiafi() == null ? null : String.valueOf(ep.getMunicipio().getCdSiafi()));
			endereco.setUf(ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
			endereco.setCep(StringUtils.leftPad(ep.getNrCep(), 8, '0'));
		}
		if (isVersao504() || isVersao510() || isVersao513()) {
			endereco.setCodigoMunicipioOutros(codMunicipioOutros);
		}
		return endereco;
	}

	private IdentificacaoTomador createIdentificacaoTomador() {
		IdentificacaoTomador identificacaoTomador = new IdentificacaoTomador();
		if (getDevedorDocServ() != null) {
			if ("J".equals(getDevedorDocServ().getCliente().getPessoa().getTpPessoa().getValue())) {
				identificacaoTomador.setCnpj(StringUtils.leftPad(getDevedorDocServ().getCliente().getPessoa().getNrIdentificacao(),14,'0'));
			} else {
				identificacaoTomador.setCpf(StringUtils.leftPad(getDevedorDocServ().getCliente().getPessoa().getNrIdentificacao(),11,'0'));
			}
			String nrInscricaoMunicipal = getDevedorDocServ().getCliente().getPessoa().getNrInscricaoMunicipal();
			if (StringUtils.isNotBlank(nrInscricaoMunicipal)) {
				nrInscricaoMunicipal = nrInscricaoMunicipal.replace(".", "");
			}
			identificacaoTomador.setInscricaoMunicipal(NfeConverterUtils.tratarString(nrInscricaoMunicipal,15));
		}
		return identificacaoTomador;
	}

	private Prestador createPrestador() {
		Prestador prestador = new Prestador();
		if (getDoctoServico().getFilialByIdFilialOrigem() != null) {
			prestador.setCnpj(StringUtils.leftPad(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNrIdentificacao(),14,'0'));
			String nrInscricaoMunicipal = getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNrInscricaoMunicipal();
			if (StringUtils.isNotBlank(nrInscricaoMunicipal)) {
				nrInscricaoMunicipal = nrInscricaoMunicipal.replace(".", "");
			}
			prestador.setInscricaoMunicipal(NfeConverterUtils.tratarString(nrInscricaoMunicipal,15));
		}
		prestador.setEnderecoPrestador(createEnderecoPrestador());
		prestador.setDadosComplementaresPrestador(createDadosComplementaresPrestador());
        return prestador;
	}

	private DadosComplementaresPrestador createDadosComplementaresPrestador() {
		DadosComplementaresPrestador dadosComplementaresPrestador = new DadosComplementaresPrestador();
		if (getDoctoServico().getFilialByIdFilialOrigem() != null) {
			EnderecoPessoa ep = getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa();
			
			TelefoneEndereco te = null;
			if (ep.getTelefoneEnderecos() != null) {
				te = NfeConverterUtils.getTelefoneEndereco(ep, getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getTpPessoa().getValue());
				
			}
			
			if (te != null) {
				
				if (te.getNrDdd() != null && te.getNrDdd().length() == 2) {
					dadosComplementaresPrestador.setDDD(te.getNrDdd());
				}
				if (te.getNrTelefone() != null && te.getNrTelefone().length() >= 6 && te.getNrTelefone().length() <= 8) {
					dadosComplementaresPrestador.setTelefone(te.getNrTelefone());
				}
			}
			dadosComplementaresPrestador.setRazaoSocial(NfeConverterUtils.tratarString(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNmPessoa(),120));
			dadosComplementaresPrestador.setNomeFantasia(NfeConverterUtils.tratarString(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getNmFantasia(),120));
			dadosComplementaresPrestador.setEmail(NfeConverterUtils.tratarString(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getDsEmail(),60));
		}
		return dadosComplementaresPrestador;
	}

	private Endereco createEnderecoPrestador() {
		Endereco enderecoPrestador = new Endereco();
		
		if (getDoctoServico().getFilialByIdFilialOrigem() != null) {
			EnderecoPessoa ep = getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa();
			
			enderecoPrestador.setEndereco(NfeConverterUtils.tratarString(ep.getTipoLogradouro().getDsTipoLogradouro().getValue()+" "+ep.getDsEndereco(),50));
			enderecoPrestador.setNumero(NfeConverterUtils.tratarString(ep.getNrEndereco(),10));
			enderecoPrestador.setComplemento(NfeConverterUtils.tratarString(ep.getDsComplemento(),25));
			enderecoPrestador.setBairro(NfeConverterUtils.tratarString(ep.getDsBairro(),50));
			if (ep.getMunicipio().getUnidadeFederativa() != null) {
				enderecoPrestador.setUf(ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				if (ep.getMunicipio().getUnidadeFederativa().getPais().getNrBacen() != null) {
					enderecoPrestador.setCodigoPais(ep.getMunicipio().getUnidadeFederativa().getPais().getNrBacen().intValue());
				}
			}
			enderecoPrestador.setCep(StringUtils.leftPad(ep.getNrCep(),8,'0'));
		}
		return enderecoPrestador;
	}

	private Servico createServico() {
		Servico servico = new Servico();
		Map<String, ImpostoServico> mapImpostoServico = createMapImpostoServico(getImpostoServicos());
		servico.setValores(createValores(mapImpostoServico));
		servico.setAliquotasComplementares(createAliquotasComplementares(mapImpostoServico));
		servico.setItemListaServico(codigoTribMunicipio);
		servico.setCodigoCnae(codigoCnae);
		servico.setCodigoTributacaoMunicipio(codigoTribMunicipio);
		StringBuilder discriminacao = new StringBuilder();
		String tpDocumentoServico = getDoctoServico().getTpDocumentoServico() == null ? null : getDoctoServico().getTpDocumentoServico().getValue();
		if (ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) {
			Cliente destinatario = getDoctoServico().getClienteByIdClienteDestinatario();
			if ("J".equals(destinatario.getPessoa().getTpPessoa().getValue())) {
				discriminacao.append(destinatario.getPessoa().getNrIdentificacao()).append(" ");
				
			}
			discriminacao.append(destinatario.getPessoa().getNmPessoa()).append(" ");
			EnderecoPessoa ep = destinatario.getPessoa().getEnderecoPessoa();
			if (ep != null) {
				if (ep.getTipoLogradouro() != null) {
					discriminacao.append(ep.getTipoLogradouro().getDsTipoLogradouro().getValue()).append(" ");
				}
				discriminacao.append(ep.getDsEndereco()).append(" ");
				discriminacao.append(ep.getNrEndereco()).append(" ");
				if (StringUtils.isNotBlank(ep.getDsComplemento())) {
					discriminacao.append(ep.getDsComplemento()).append(" ");
				}
				discriminacao.append(ep.getDsBairro()).append(" ");
				if (ep.getMunicipio() != null) {
					discriminacao.append(ep.getMunicipio().getNmMunicipio()).append(" ");
					if (ep.getMunicipio().getUnidadeFederativa() != null) {
						discriminacao.append(ep.getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa()).append(" ");
					}
				}
				discriminacao.append("CEP: ").append(ep.getNrCep());
				discriminacao.append(" R$ ").append(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getVlMercadoria()));
				discriminacao.append(" ").append(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getPsReal())) .append(" KG ");
				discriminacao.append(getDoctoServico().getQtVolumes()) .append(" VOLS ");
				if (getNotaFiscalConhecimentos() != null) {
					for (NotaFiscalConhecimento nfc: getNotaFiscalConhecimentos()) {
						discriminacao.append(" ").append(StringUtils.leftPad(nfc.getNrNotaFiscal() == null ? "0" : String.valueOf(nfc.getNrNotaFiscal()), 8, '0'));
					}
				}
			}

		} else {
			if (getDoctoServico().getServAdicionalDocServs() != null) {
				for (ServAdicionalDocServ servAdicionalDocServ: getDoctoServico().getServAdicionalDocServs()) {
					discriminacao.append(servAdicionalDocServ.getServicoAdicional().getDsServicoAdicional());
				}
			}
		}

		discriminacao.append(createConcatDadosObservacao());

		servico.setDiscriminacao(NfeConverterUtils.tratarString(discriminacao.toString(), 1500) );
		if (getMunicipioPrestacaoServico() != null){
			servico.setMunicipioIncidencia(NfeConverterUtils.formatCMun(getMunicipioPrestacaoServico()));
			String cdSiafi = getMunicipioPrestacaoServico().getCdSiafi() == null ? null : String.valueOf(getMunicipioPrestacaoServico().getCdSiafi());
			servico.setMunicipioIncidenciaSiafi(cdSiafi);
		}
		if (getDoctoServico().getNrDoctoServico() != null) {
			servico.setDescricaoRPS(getDoctoServico().getFilialByIdFilialOrigem().getSgFilial()+StringUtils.leftPad(String.valueOf(getDoctoServico().getNrDoctoServico()), 9, '0'));
		}
		ImpostoServico iss = mapImpostoServico.get("IS");
		boolean blRetencaoTomadorServico = iss != null && Boolean.TRUE.equals(iss.getBlRetencaoTomadorServico());
		servico.setIssRetido(getIssRetido(iss));
		servico.setResponsavelRetencao(blRetencaoTomadorServico ? null : "1");
		if (isVersao504() || isVersao510() || isVersao513()) {
			servico.setMunicipioIncidenciaOutros(munIncidenciaOutro);
		}
		if (isVersao510() || isVersao513()) {
			servico.setServicoExportacao("2");
			servico.setEmpreitadaGlobal("2");
		}
		servico.setDadosComplementaresServico(createDadosComplementaresServico(blRetencaoTomadorServico,mapImpostoServico));
		return servico;
	}

	private String getIssRetido(ImpostoServico iss) {
		//LMSA-1512 Alterar a regra para geração da tag IssRetido 
		/*Caso a filial emissora do RPS-t ou RPS-s possua o parâmetro NFSE_RET_SPECIAL_ISS = SIM
			Se o documento foi emitido sem retenção de ISS
			      IssRetido: será preenchido com 1;
			Senão
			      IssRetido: será preenchido com 2.
			
			Caso filial não possua o parâmetro se ele estiver configurado como "Não", o preenchimento da tag IssRetido permanece conforme a regra existente. 
		*/
		boolean blRetencaoTomadorServico = iss != null && Boolean.TRUE.equals(iss.getBlRetencaoTomadorServico());
		
		if (nfseRetEspIss) {
			if (!blRetencaoTomadorServico) {
				return "1";
			} else {
				return "2";
			}
		} else {
			if (isNfseIssPrestOutroLocal()) {
				if (blRetencaoTomadorServico && validateMunicipioIncidencia()) {
					return "1";
				} else {
					return "2";
				}
			} else {
				if (!blRetencaoTomadorServico) {
					return "2";
				} 
			}
			return "1";
		}
	}
	
	private DadosComplementaresServico createDadosComplementaresServico(boolean blRetencaoTomadorServico,Map<String, ImpostoServico> mapImpostoServico) {
		DadosComplementaresServico dadosComplementaresServico = new DadosComplementaresServico();
		dadosComplementaresServico.setTipoRecolhimento(blRetencaoTomadorServico ? "2" : "1");
		if (getMunicipioPrestacaoServico() != null) {
			dadosComplementaresServico.setMunicipioPrestacaoDescricao(NfeConverterUtils.tratarString(getMunicipioPrestacaoServico().getNmMunicipio(),30));
		}
		dadosComplementaresServico.setSeriePrestacao(99);
		dadosComplementaresServico.setListaItens(createListaItens(mapImpostoServico));
		return dadosComplementaresServico;
	}

	private ListaItens createListaItens(Map<String, ImpostoServico> mapImpostoServico) {
		ListaItens listaItens = new ListaItens();
		listaItens.addItem(createItem(mapImpostoServico));
		return listaItens;
	}

	private Item createItem(Map<String, ImpostoServico> mapImpostoServico) {
		Item item = new Item();
		item.setItemListaServico(codigoTribMunicipio);
		item.setCodigoCnae(codigoCnae);
		String tpDocumentoServico = getDoctoServico().getTpDocumentoServico() == null ? null : getDoctoServico().getTpDocumentoServico().getValue();
		if(ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equals(tpDocumentoServico)) {
			item.setDiscriminacaoServico(servicoNte);
		} else {
			if (getDoctoServico().getServAdicionalDocServs() != null) {
				for (ServAdicionalDocServ servAdicionalDocServ: getDoctoServico().getServAdicionalDocServs()) {
					item.setDiscriminacaoServico(servAdicionalDocServ.getServicoAdicional().getDsServicoAdicional().getValue());
				}
			}
		}
		item.setQuantidade("1");
		item.setValorUnitario(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getVlTotalDocServico()));
		item.setValorDesconto("0,00");
		item.setValorTotal(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getVlTotalDocServico()));
		item.setServicoTributavel("1");
		item.setCodigoTributacaoMunicipio(codigoTribMunicipio);
		ImpostoServico iss = mapImpostoServico.get("IS");
		item.setVlrAliquota(iss == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(iss.getPcAliquota()));
		return item;
	}

	private AliquotasComplementares createAliquotasComplementares(Map<String, ImpostoServico> mapImpostoServico) {
		AliquotasComplementares aliquotasComplementares = new AliquotasComplementares();
		ImpostoServico pis = mapImpostoServico.get("PI");
		aliquotasComplementares.setAliquotaPis(pis == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(pis.getPcAliquota()));
		ImpostoServico cofins = mapImpostoServico.get("CO");
		aliquotasComplementares.setAliquotaCofins(cofins == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(cofins.getPcAliquota()));
		ImpostoServico inss = mapImpostoServico.get("IN");
		aliquotasComplementares.setAliquotaInss(inss == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(inss.getPcAliquota()));
		ImpostoServico ir = mapImpostoServico.get("IR");
		aliquotasComplementares.setAliquotaIr(ir == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(ir.getPcAliquota()));
		ImpostoServico csll = mapImpostoServico.get("CS");
		aliquotasComplementares.setAliquotaCsll(csll == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(csll.getPcAliquota()));
		return aliquotasComplementares;
	}

	private Valores createValores(Map<String, ImpostoServico> mapImpostoServico) {
		Valores valores = new Valores();
		valores.setValorServicos(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getVlTotalDocServico()));
		valores.setValorDeducoes("0,00");
		
		ImpostoServico pis = mapImpostoServico.get("PI");
		valores.setValorPis(pis == null ? "0,00" : NfeConverterUtils.formatDoisDecimais(pis.getVlImposto()));
		ImpostoServico cofins = mapImpostoServico.get("CO");
		valores.setValorCofins(cofins == null ? "0,00" : NfeConverterUtils.formatDoisDecimais(cofins.getVlImposto()));
		ImpostoServico inss = mapImpostoServico.get("IN");
		valores.setValorInss(inss == null ? "0,00" : NfeConverterUtils.formatDoisDecimais(inss.getVlImposto()));
		ImpostoServico ir = mapImpostoServico.get("IR");
		valores.setValorIr(ir == null ? "0,00" : NfeConverterUtils.formatDoisDecimais(ir.getVlImposto()));
		ImpostoServico csll = mapImpostoServico.get("CS");
		valores.setValorCsll(csll == null ? "0,00" : NfeConverterUtils.formatDoisDecimais(csll.getVlImposto()));
		ImpostoServico iss = mapImpostoServico.get("IS");
		
//		LMSA-658
		this.setValoresIssAndIssRetido(valores, iss);
		
		valores.setBaseCalculo(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getVlTotalDocServico()));
		valores.setAliquota(iss == null ? "0,0000" : NfeConverterUtils.formatQuatroDecimais(iss.getPcAliquota()));
		valores.setValorLiquidoNfse(NfeConverterUtils.formatDoisDecimais(getDoctoServico().getVlLiquido()));
		return valores;
	}
	
	private void setValoresIssAndIssRetido(Valores valores, ImpostoServico iss) {
		String tpServico = getDoctoServico().getTpDocumentoServico().getValue();		
		Boolean blTpServicoNSEOrNTE = (ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(tpServico) 
				|| ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA.equalsIgnoreCase(tpServico));
		
		Boolean blEqualsMunicipio = iss.getMunicipioByIdMunicipioIncidencia().getIdMunicipio()
				.equals(getDoctoServico().getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio());
		
		if (iss != null && BooleanUtils.isTrue(nfseIssPrestOutroLocal)) {
			if ((isVersao504() || isVersao510() || isVersao513()) && iss.getBlRetencaoTomadorServico() && blTpServicoNSEOrNTE && blEqualsMunicipio) {
				valores.setValorIss("0,00");
				valores.setValorIssRetido(NfeConverterUtils.formatDoisDecimais(iss.getVlImposto()));
			} else {
				valores.setValorIss(NfeConverterUtils.formatDoisDecimais(iss.getVlImposto()));
				valores.setValorIssRetido("0,00");
			}
		} else {
			if (iss != null && BooleanUtils.isTrue(blTpServicoNSEOrNTE) && 
					BooleanUtils.isFalse(blEqualsMunicipio) && BooleanUtils.isTrue(nfseGeraVlrIss)) {
					valores.setValorIss(NfeConverterUtils.formatDoisDecimais(iss.getVlImposto()));
					valores.setValorIssRetido("0,00");
				
				if (iss.getBlRetencaoTomadorServico()) {
					valores.setValorIssRetido(NfeConverterUtils.formatDoisDecimais(iss.getVlImposto()));
				}
			} else {
				if (!iss.getBlRetencaoTomadorServico()) {
					valores.setValorIss(NfeConverterUtils.formatDoisDecimais(iss.getVlImposto()));
					valores.setValorIssRetido("0,00");
				} else {
					valores.setValorIssRetido(NfeConverterUtils.formatDoisDecimais(iss.getVlImposto()));
					valores.setValorIss("0,00");
				}
			}
		}
	}
	
	private Map<String, ImpostoServico> createMapImpostoServico(List<ImpostoServico> impostos) {
		Map<String, ImpostoServico> map = new HashMap<String, ImpostoServico>();
		if (impostos != null) {
			for (ImpostoServico is: impostos) {
				map.put(is.getTpImposto().getValue(), is);
			}
		}
		return map;
	}

	private IdentificacaoRps createIdentificacaoRps() {
		IdentificacaoRps identificacaoRps = new IdentificacaoRps();
		identificacaoRps.setNumero(nrFiscalRps);
		identificacaoRps.setSerie(serieNfe);
		identificacaoRps.setTipo("1");
		return identificacaoRps;
	}

	private EmailDadosAdicionais createEmail513() {
		List<String> receiverToList = new ArrayList<String>();
		if (getDevedorDocServ() != null) {
			List<com.mercurio.lms.configuracoes.model.Contato> contatos = NfeConverterUtils.getContatos(getDevedorDocServ().getCliente().getPessoa());
			for (com.mercurio.lms.configuracoes.model.Contato c: contatos) {
				if (StringUtils.isNotBlank(c.getDsEmail())) {
					receiverToList.add(c.getDsEmail());
				}
			}
			if (receiverToList.isEmpty()) {
				if (StringUtils.isNotBlank(getDevedorDocServ().getCliente().getPessoa().getDsEmail())) {
					receiverToList.add(getDevedorDocServ().getCliente().getPessoa().getDsEmail());
				}
			}
		}
		
		EmailDadosAdicionais email = new EmailDadosAdicionais();
		email.setReceiverToList(receiverToList);
		return email;
	}
	
	private DadosAdic createDadosAdic513() {
		DadosAdic dadosAdic = new DadosAdic();
		dadosAdic.setEmail(createEmail513());
		return dadosAdic;
	}

	private Rps createRps() {
		Rps rps = new Rps();
		rps.setInfRps(createInfRps());
		if (isVersao513()) {
			rps.setDadosAdic(createDadosAdic513());
		}
		return rps;
	}
	
}
