package com.mercurio.lms.expedicao.model.service;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CLIENTE_CONSIGNATARIO_IN_SESSION;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO_PARCIAL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CONHECIMENTO_REFATURAMENTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mercurio.lms.configuracoes.model.*;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.municipios.model.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.CtoCtoCooperada;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NaturezaProduto;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.tabelaprecos.model.ProdutoEspecifico;
import com.mercurio.lms.tributos.model.service.AliquotaIssMunicipioServService;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.CotacaoService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.digitarNotaService"
 */
public class DigitarNotaService {

	private MunicipioService municipioService;
	private ClienteService clienteService;
	private CotacaoService cotacaoService;
	private DensidadeService densidadeService;
	private PedidoColetaService pedidoColetaService;
	private FilialService filialService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	private EnderecoPessoaService enderecoPessoaService;
	private AliquotaIssMunicipioServService aliquotaIssMunicipioServService;
	private ConfiguracoesFacade configuracoesFacade;
	private ConhecimentoNormalService conhecimentoNormalService;
	private MoedaService moedaService;

    // LMSA-6160 
    private DigitarDadosNotaNormalNotasFiscaisService digitarDadosNotaNormalNotasFiscaisService;
	
	// LMSA-6160
	@SuppressWarnings("rawtypes")
    private void validarChavesNFeCTe(Map parameters, Conhecimento conhecimento) {
        // LMSA-6160 ... validar, antes de gravar, todas as chaves NFe/CTe, conforme redespacho intermediario informado
        Boolean redespachoIntermediario = parameters.get("redespachoIntermediario") != null ? (Boolean) parameters.get("redespachoIntermediario") : null;
        if (redespachoIntermediario == null) {
        	redespachoIntermediario = conhecimento != null && conhecimento.getBlRedespachoIntermediario() != null ? 
        			conhecimento.getBlRedespachoIntermediario() : Boolean.FALSE;
        }
        Long idClienteRemetente = (Long) parameters.get("idClienteRemetente");

        for (NotaFiscalConhecimento nfconhecimento : conhecimento.getNotaFiscalConhecimentos()) {
            try {
                if ("01".equals(nfconhecimento.getTpDocumento()) || ("99".equals(nfconhecimento.getTpDocumento()) && StringUtils.isNotEmpty(nfconhecimento.getNrChave()))) { 
                    Long idFilial = SessionUtils.getFilialSessao() != null
							? SessionUtils.getFilialSessao().getIdFilial()
							: (Long) parameters.get("idFilial");
// TODO: 25/01/2023 alterado fazer tratamento exception
                	digitarDadosNotaNormalNotasFiscaisService.validateChaveNfe(
                            redespachoIntermediario,
                            nfconhecimento.getNrChave(),
                            nfconhecimento.getNrNotaFiscal(),
                            nfconhecimento.getDtEmissao(),
                            idClienteRemetente,
							idFilial);
                }
            } catch (BusinessException e) {
                String complemento = Boolean.TRUE.equals(redespachoIntermediario) ? "com" : "sem";
                throw new BusinessException("LMS-04564", new Object[] {complemento});
            }
        }
        
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public Conhecimento createConhecimentoPersistente(Map parameters, DomainValue tpDocumentoServico) {
	    
        // Conhecimento temporário na sessão (contém os dados da tela e popups não validados
        Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
        
	    // LMSA-6160
	    validarChavesNFeCTe(parameters, conhecimento);

		// Conhecimento persistente: conterá os dados do conhecimento temporário já validados
		Conhecimento conhecimentoNovo = new Conhecimento();

		conhecimentoNovo.setDhInclusao(conhecimento.getDhInclusao());
		
		//Define o tipo de documento
		conhecimentoNovo.setTpDocumentoServico(tpDocumentoServico);
		conhecimentoNovo.setTpDoctoServico(tpDocumentoServico);

		conhecimentoNovo.setDadosCliente(conhecimento.getDadosCliente());
		conhecimentoNovo.setObservacaoDoctoServicos(conhecimento.getObservacaoDoctoServicos());
		conhecimentoNovo.setDevedorDocServs(conhecimento.getDevedorDocServs());
		conhecimentoNovo.setClienteByIdClienteConsignatario(conhecimento.getClienteByIdClienteConsignatario());
		conhecimentoNovo.setClienteByIdClienteRedespacho(conhecimento.getClienteByIdClienteRedespacho());
		conhecimentoNovo.setEnderecoPessoa(conhecimento.getEnderecoPessoa());
		conhecimentoNovo.setServAdicionalDocServs(conhecimento.getServAdicionalDocServs());
		conhecimentoNovo.setVlMercadoria(conhecimento.getVlMercadoria());
		conhecimentoNovo.setDimensoes(conhecimento.getDimensoes());
		if(conhecimento.getDadosComplementos() != null) {
			conhecimentoNovo.setDadosComplementos(new ArrayList(conhecimento.getDadosComplementos()));
		}
		if(conhecimento.getBlReembolso() == null) {
			conhecimentoNovo.setBlReembolso(Boolean.FALSE);
		} else {
			conhecimentoNovo.setBlReembolso(conhecimento.getBlReembolso());
		}
		
		if(conhecimento.getBlIndicadorEdi() == null) {
			conhecimentoNovo.setBlIndicadorEdi(Boolean.FALSE);
		} else {
			conhecimentoNovo.setBlIndicadorEdi(conhecimento.getBlIndicadorEdi());
		}
		
		if (conhecimento.getNaturezaProduto() != null){
			conhecimentoNovo.setNaturezaProduto(conhecimento.getNaturezaProduto());
		}
		
		conhecimentoNovo.setTpConhecimento(conhecimento.getTpConhecimento());
		conhecimentoNovo.setPsReal(conhecimento.getPsReal());
		conhecimentoNovo.setQtVolumes(conhecimento.getQtVolumes());
		conhecimentoNovo.setNotaFiscalConhecimentos(conhecimento.getNotaFiscalConhecimentos());
		
		/*
		 * LMS-3534: se peso cubado não foi informado o mesmo deve assumir o valor de peso mercadoria.
		 */
		if(conhecimentoNovo.getNotaFiscalConhecimentos() != null){
			for(NotaFiscalConhecimento notaFiscalConhecimento : conhecimentoNovo.getNotaFiscalConhecimentos()){
				if(notaFiscalConhecimento.getPsCubado() == null){
					notaFiscalConhecimento.setPsCubado(notaFiscalConhecimento.getPsMercadoria());
				}
			}
		}
		
		if(parameters.get("dpe") != null) {
			conhecimentoNovo.setDtPrevEntrega((YearMonthDay)parameters.get("dpe"));
			//seta o nrDiasPrevEntrega
			Days d = Days.daysBetween(new YearMonthDay(), (YearMonthDay)parameters.get("dpe"));
			conhecimentoNovo.setNrDiasPrevEntrega(Integer.valueOf(d.getDays()+1).shortValue());
		}
		// local de entrega				
		// Utilizar endereço do local de entrega caso for informado  
		if (conhecimento.getDsLocalEntrega() != null || conhecimento.getDsEnderecoEntrega() != null) {
			conhecimentoNovo.setDsLocalEntrega(conhecimento.getDsLocalEntrega());
			conhecimentoNovo.setDsBairroEntrega(conhecimento.getDsBairroEntrega());
			conhecimentoNovo.setTipoLogradouroEntrega(conhecimento.getTipoLogradouroEntrega());
			conhecimentoNovo.setDsComplementoEntrega(conhecimento.getDsComplementoEntrega());
			conhecimentoNovo.setDsEnderecoEntrega(conhecimento.getDsEnderecoEntrega());
			conhecimentoNovo.setNrEntrega(conhecimento.getNrEntrega());
			conhecimentoNovo.setNrCepEntrega(conhecimento.getNrCepEntrega());
			conhecimentoNovo.setMunicipioByIdMunicipioEntrega(conhecimento.getMunicipioByIdMunicipioEntrega());
		} else if (parameters.get("clientesSession") != null && ((Map)parameters.get("clientesSession")).get(CLIENTE_CONSIGNATARIO_IN_SESSION) != null) { // Caso não tenha sido informado endereço de entrega, verificar se foi informado algum consignatário
			Map mapCliente = (Map)((Map)parameters.get("clientesSession")).get(CLIENTE_CONSIGNATARIO_IN_SESSION);
			List listCliente = clienteService.findLookupClienteEndereco((Long)mapCliente.get("idCliente"));
			if (listCliente != null && listCliente.size() > 0) {
				HashMap cliente = (HashMap)listCliente.get(0);
				HashMap pessoa = (HashMap)cliente.get("pessoa");
				HashMap endereco = (HashMap)pessoa.get("endereco");
				TipoLogradouro tipoLogradouro = new TipoLogradouro();
				tipoLogradouro.setIdTipoLogradouro(Long.valueOf(endereco.get("idTipoLogradouro").toString()));
				conhecimentoNovo.setTipoLogradouroEntrega(tipoLogradouro);
				conhecimentoNovo.setDsBairroEntrega(String.valueOf(endereco.get("dsBairro")));
				conhecimentoNovo.setDsComplementoEntrega(String.valueOf(mapCliente.get("dsComplemento")));
				conhecimentoNovo.setDsEnderecoEntrega(String.valueOf(mapCliente.get("dsEndereco")));
				conhecimentoNovo.setNrEntrega(String.valueOf(mapCliente.get("nrEndereco")));
				conhecimentoNovo.setNrCepEntrega(String.valueOf(mapCliente.get("nrCep")));
				Municipio municipio = municipioService.findByIdInitLazyProperties((Long)endereco.get("idMunicipio"), false);
				conhecimentoNovo.setMunicipioByIdMunicipioEntrega(municipio);
			}			
		} else if (conhecimento.getClienteByIdClienteDestinatario() != null) { // Caso não tenha sido informado local de entrega e consignatário, utilizar endereço do Destinatário
			Cliente clienteDestinatario = conhecimento.getClienteByIdClienteDestinatario();
			conhecimentoNovo.setDsLocalEntrega(null);
			EnderecoPessoa enderecoPessoa = clienteDestinatario.getPessoa().getEnderecoPessoa();
			if (enderecoPessoa != null && enderecoPessoa.getIdEnderecoPessoa() != null) {
				enderecoPessoa = enderecoPessoaService.findById(enderecoPessoa.getIdEnderecoPessoa());
				if(enderecoPessoa.getTipoLogradouro() != null){
				conhecimentoNovo.setTipoLogradouroEntrega(enderecoPessoa.getTipoLogradouro());
				}
				conhecimentoNovo.setDsBairroEntrega(enderecoPessoa.getDsBairro());
				conhecimentoNovo.setDsComplementoEntrega(enderecoPessoa.getDsComplemento());
				conhecimentoNovo.setDsEnderecoEntrega(enderecoPessoa.getDsEndereco());
				
				if(enderecoPessoa.getNrEndereco() != null){
					conhecimentoNovo.setNrEntrega(enderecoPessoa.getNrEndereco().trim());
				}
				
				conhecimentoNovo.setNrCepEntrega(enderecoPessoa.getNrCep());
				conhecimentoNovo.setMunicipioByIdMunicipioEntrega(enderecoPessoa.getMunicipio());
			}
		}
		conhecimentoNovo.setBlPaletizacao(conhecimento.getBlPaletizacao());
		conhecimentoNovo.setQtPaletes(conhecimento.getQtPaletes());
		conhecimentoNovo.setQtEtiquetasEmitidas(conhecimento.getQtEtiquetasEmitidas());
		conhecimentoNovo.setNrOrdemEmissaoEDI(conhecimento.getNrOrdemEmissaoEDI());
		
		//lms-2353
		conhecimentoNovo.setNrCubagemAferida(conhecimento.getNrCubagemAferida());
		conhecimentoNovo.setNrCubagemCalculo(conhecimento.getNrCubagemCalculo());
		conhecimentoNovo.setPsCubadoAferido(conhecimento.getPsCubadoAferido());
		conhecimentoNovo.setPsCubadoDeclarado(conhecimento.getPsCubadoDeclarado());
		conhecimentoNovo.setNrCubagemDeclarada(conhecimento.getNrCubagemDeclarada());
		conhecimentoNovo.setNrCtrcSubcontratante(conhecimento.getNrCtrcSubcontratante());
		conhecimentoNovo.setNrCae(conhecimento.getNrCae());
		
		conhecimentoNovo.setBlRedespachoIntermediario(conhecimento.getBlRedespachoIntermediario());
		conhecimentoNovo.setBlProcessamentoTomador(conhecimento.getBlProcessamentoTomador());
		conhecimentoNovo.setBlRedespachoColeta(conhecimento.getBlRedespachoColeta());
		conhecimentoNovo.setBlProdutoPerigoso(conhecimento.getBlProdutoPerigoso());
		conhecimentoNovo.setBlProdutoControlado(conhecimento.getBlProdutoControlado());
		conhecimentoNovo.setBlOperacaoSpitFire(
				conhecimento.getBlOperacaoSpitFire() == null ? Boolean.FALSE: conhecimento.getBlOperacaoSpitFire()
		);
		
		/** Populando dados do Conhecimento **/
		populateConhecimento(parameters, conhecimentoNovo);
		return conhecimentoNovo;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    private void populateConhecimento(Map parameters, Conhecimento conhecimento) {
		// Serviço
		Servico servico = new Servico();
		servico.setIdServico((Long) parameters.get("idServico"));
		servico.setTpModal(new DomainValue((String) parameters.get("tpModalServico")));
		servico.setTpAbrangencia(new DomainValue((String) parameters.get("tpAbrangenciaServico")));
		conhecimento.setServico(servico);

		// Remetente
		Cliente clienteRemetente = new Cliente();
		clienteRemetente.setIdCliente((Long) parameters.get("idClienteRemetente"));
		clienteRemetente.setTpCliente(new DomainValue((String) parameters.get("tpClienteRemetente")));

		Pessoa pessoaRemetente = new Pessoa();
		pessoaRemetente.setNrIdentificacao(PessoaUtils.clearIdentificacao((String) parameters.get("nrIdentificacaoRemetente")));

		conhecimento.setClienteByIdClienteRemetente(clienteRemetente);
		Long idInscricaoEstadualRemetente = (Long) parameters.get("idInscricaoEstadualRemetente");
		if(idInscricaoEstadualRemetente != null) {
			InscricaoEstadual ieRemetente = new InscricaoEstadual();
			ieRemetente.setIdInscricaoEstadual(idInscricaoEstadualRemetente);
			conhecimento.setInscricaoEstadualRemetente(ieRemetente);
		}
		EnderecoPessoa enderecoPessoaRemetente = new EnderecoPessoa();

		Municipio municipioRemetente = new Municipio();
		municipioRemetente.setIdMunicipio((Long) parameters.get("idMunicipioRemetente"));

		UnidadeFederativa ufRemetente = new UnidadeFederativa();
		ufRemetente.setIdUnidadeFederativa((Long) parameters.get("idUnidadeFederativaRemetente"));
		municipioRemetente.setUnidadeFederativa(ufRemetente);

		enderecoPessoaRemetente.setMunicipio(municipioRemetente);
		enderecoPessoaRemetente.setNrCep((String) parameters.get("nrCepRemetente"));

		pessoaRemetente.setEnderecoPessoa(enderecoPessoaRemetente);
		clienteRemetente.setPessoa(pessoaRemetente);

		//Destinatário
		Cliente clienteDestinatario = new Cliente();
		clienteDestinatario.setIdCliente((Long) parameters.get("idClienteDestinatario"));
		if (parameters.get("tpClienteDestinatario")!=null){
		clienteDestinatario.setTpCliente(new DomainValue((String) parameters.get("tpClienteDestinatario")));
		}else {
			clienteDestinatario.setTpCliente(new DomainValue("E"));
		}
		
		Pessoa pessoaDestinatario = new Pessoa();
		pessoaDestinatario.setNrIdentificacao(PessoaUtils.clearIdentificacao((String) parameters.get("nrIdentificacaoDestinatario")));

		conhecimento.setClienteByIdClienteDestinatario(clienteDestinatario);
		Long idInscricaoEstadualDestinatario = (Long) parameters.get("idInscricaoEstadualDestinatario");
		if(idInscricaoEstadualDestinatario != null) {
			InscricaoEstadual ieDestinatario = new InscricaoEstadual();
			ieDestinatario.setIdInscricaoEstadual(idInscricaoEstadualDestinatario);
			conhecimento.setInscricaoEstadualDestinatario(ieDestinatario);
		}
		EnderecoPessoa enderecoPessoaDestinatario = new EnderecoPessoa();
		Municipio municipioDestinatario = new Municipio();
		municipioDestinatario.setIdMunicipio((Long) parameters.get("idMunicipioDestinatario"));

		UnidadeFederativa ufDestinatario = new UnidadeFederativa();
		ufDestinatario.setIdUnidadeFederativa((Long) parameters.get("idUnidadeFederativaDestinatario"));
		municipioDestinatario.setUnidadeFederativa(ufDestinatario);

		enderecoPessoaDestinatario.setMunicipio(municipioDestinatario);
		enderecoPessoaDestinatario.setNrCep((String) parameters.get("nrCepDestinatario"));

		pessoaDestinatario.setEnderecoPessoa(enderecoPessoaDestinatario);
		clienteDestinatario.setPessoa(pessoaDestinatario);

        // LMSA-6160
        Boolean redespachoIntermediario = (Boolean) parameters.get("redespachoIntermediario");
        if (Boolean.TRUE.equals(redespachoIntermediario)) {
            conhecimento.setClienteByIdClienteRedespacho(conhecimento.getClienteByIdClienteRemetente());
            conhecimento.setClienteByIdClienteConsignatario(conhecimento.getClienteByIdClienteDestinatario());
        }
		
		// Conhecimento original
		Long idDoctoServico = (Long) parameters.get("idDoctoServicoOriginal");
		if(idDoctoServico != null) {
			DoctoServico original = new DoctoServico();
			original.setIdDoctoServico(idDoctoServico);
			conhecimento.setDoctoServicoOriginal(original);
		}
		// Cotação
		Long idCotacao = (Long) parameters.get("idCotacao");
		if(idCotacao != null) {
			List cotacoes = new ArrayList(1);
			Cotacao c = cotacaoService.findById(idCotacao, false);
			cotacoes.add(c);
			conhecimento.setCotacoes(cotacoes);
		}
		// Pedido de Coleta
		Long idPedidoColeta = (Long) parameters.get("idPedidoColeta");
		if(idPedidoColeta != null) {
			PedidoColeta pc = pedidoColetaService.findByIdInitLazyProperties(idPedidoColeta, false);
			conhecimento.setPedidoColeta(pc);
		}
		conhecimento.setTpCalculoPreco(new DomainValue((String) parameters.get("tpCalculoPreco")));
		conhecimento.setBlParcelas((Boolean) parameters.get("blFrete"));
		conhecimento.setBlServicosAdicionais((Boolean) parameters.get("blServicosAdicionais"));

		conhecimento.setTpFrete(new DomainValue((String) parameters.get("tpFrete")));
		if (parameters.get("tpMotivoLiberacao") != null) {
			conhecimento.setTpMotivoLiberacao(new DomainValue((String) parameters.get("tpMotivoLiberacao")));
		}
		conhecimento.setTpDevedorFrete(new DomainValue((String) parameters.get("tpDevedorFrete")));
		
		conhecimento.setDsCodigoColeta((String) parameters.get("dsCodigoColeta"));
		conhecimento.setBlColetaEmergencia((Boolean) parameters.get("blColetaEmergencia"));
		conhecimento.setBlEntregaEmergencia((Boolean) parameters.get("blEntregaEmergencia"));
		
		conhecimento.setBlCalculaFrete(parameters.get("blFrete") == null ? Boolean.FALSE : (Boolean) parameters.get("blFrete"));
		conhecimento.setBlCalculaServico(parameters.get("blServicosAdicionais") == null ? Boolean.FALSE : (Boolean) parameters.get("blServicosAdicionais"));
		
		if(conhecimento.getNrCtrcSubcontratante() == null && conhecimento.getClienteByIdClienteRedespacho() == null) {
			String nrConhecimentoSubcontratante = (String) parameters.get("nrConhecimentoSubcontratante");
			if(nrConhecimentoSubcontratante!= null && nrConhecimentoSubcontratante.length() == 44){
				conhecimento.setNrCtrcSubcontratante(nrConhecimentoSubcontratante.substring(25, 34));
			}else{
				conhecimento.setNrCtrcSubcontratante(nrConhecimentoSubcontratante);
			}
		}

		Filial filial = SessionUtils.getFilialSessao();
		Moeda moeda = buscarMoedaSessao(filial.getIdFilial());

		conhecimento.setFilialByIdFilialOrigem(filial);
		conhecimento.setFilialOrigem(filial);
		filialService.validateExisteCodFilial(filial);

		conhecimento.setMoeda(moeda);
		if (!(CONHECIMENTO_DEVOLUCAO_PARCIAL.equals(conhecimento.getTpConhecimento()) || CONHECIMENTO_REFATURAMENTO.equals(conhecimento.getTpConhecimento()))) {
			DateTime dhEventoColeta = (DateTime) parameters.get("dhEventoColeta");
			if(idPedidoColeta != null && dhEventoColeta != null) {
				conhecimento.setDtColeta(dhEventoColeta);
			} else {
				conhecimento.setDtColeta((DateTime)parameters.get("dhChegada"));
			}
		}
		
		// Aeroporto de origem
		Long idAeroOrigem = (Long) parameters.get("idAeroportoOrigem");
		if(idAeroOrigem != null) {
			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(idAeroOrigem);
			conhecimento.setAeroportoByIdAeroportoOrigem(aeroporto);
		}
		// Aeroporto de destino
		Long idAeroDestino = (Long) parameters.get("idAeroportoDestino");
		if(idAeroDestino != null) {
			Aeroporto aeroporto = new Aeroporto();
			aeroporto.setIdAeroporto(idAeroDestino);
			conhecimento.setAeroportoByIdAeroportoDestino(aeroporto);
		}
		// Produto específico
		Long idProduto = (Long) parameters.get("idProdutoEspecifico");
		if(idProduto != null) {
			ProdutoEspecifico produtoEspecifico = new ProdutoEspecifico();
			produtoEspecifico.setIdProdutoEspecifico(idProduto);
			conhecimento.setProdutoEspecifico(produtoEspecifico);
		}
		// Natureza do Produto
		if (parameters.get("idNaturezaProduto") != null) {
			NaturezaProduto naturezaProduto = new NaturezaProduto();
			naturezaProduto.setIdNaturezaProduto((Long) parameters.get("idNaturezaProduto"));
			conhecimento.setNaturezaProduto(naturezaProduto);
		}

		// Municipio de coleta
		Municipio municipioColeta = municipioService.findByIdInitLazyProperties((Long) parameters.get("idMunicipioRemetente"), false);
		conhecimento.setMunicipioByIdMunicipioColeta(municipioColeta);

		// Municipio de entrega
		if(conhecimento.getMunicipioByIdMunicipioEntrega() == null) {
			if(parameters.get("idMunicipioDestinatario") == null){
				throw new BusinessException("LMS-04299", new Object[]{parameters.get("nrCepDestinatario")});				
			}
			Municipio municipioEntrega = municipioService.findByIdInitLazyProperties((Long) parameters.get("idMunicipioDestinatario"), false);
			conhecimento.setMunicipioByIdMunicipioEntrega(municipioEntrega);
			conhecimento.setNrCepEntrega((String) parameters.get("nrCepDestinatario"));
		}
		
		/** Populando dados do CtoCtoCooperada **/
		populateCtoCtoCooperada(parameters, conhecimento);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    private void populateCtoCtoCooperada(Map parameters, Conhecimento conhecimento) {
		Integer nrCtoCooperada = (Integer) parameters.get("nrCtoCooperada");
		if(nrCtoCooperada != null) {
			List cooperadas = new ArrayList(1);
			CtoCtoCooperada ctoCoop = new CtoCtoCooperada();
			ctoCoop.setNrCtoCooperada(nrCtoCooperada);
			Filial filial = new Filial();
			filial.setIdFilial((Long) parameters.get("idFilial"));
			ctoCoop.setFilialByIdFilial(filial);
			ctoCoop.setConhecimento(conhecimento);
			cooperadas.add(ctoCoop);
			conhecimento.setCtoCtoCooperadas(cooperadas);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public List findDensidade() {
		List densidades = densidadeService.findAllAtivo();
		if (densidades != null && !densidades.isEmpty()) {
			for (int i = 0; i < densidades.size(); i++) {
				Map densidade = (Map) densidades.get(i);
				Map tpDensidade = (Map) densidade.remove("tpDensidade");
				densidade.put("tpDensidade", tpDensidade.get("description"));
			}
		}
		return densidades;
	}
	
	/**
	 * Obtem o tipo de conhecimento através dos dados do remetente destinatario e filial 
	 * logada
	 * 
	 * @param  conhecimento
	 * @param  filialLogada
	 * @return Tipo de documento
	 */
	public String findTpDocumento(Conhecimento conhecimento, Filial filialLogada){

		String tpDocumento = "CTR";	

		ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findConteudoParametroFilial(filialLogada.getIdFilial(), "IMPRIME_NF_TRANSP", "S");		

		if(conteudoParametroFilial != null){
			
			boolean coletaBalcao = false;
			boolean coletaTelefone = false;
			boolean coletaAutomatica = false;
			if (conhecimento.getPedidoColeta() != null) {
				coletaBalcao = "BA".equals(conhecimento.getPedidoColeta().getTpModoPedidoColeta().getValue());
				coletaTelefone = "TE".equals(conhecimento.getPedidoColeta().getTpModoPedidoColeta().getValue());
				coletaAutomatica = "AU".equals(conhecimento.getPedidoColeta().getTpModoPedidoColeta().getValue());
			}
			Long idMunicipioOrigemPrestacaoServico = null;
			Long idMunicipioEntrega = conhecimento.getMunicipioByIdMunicipioEntrega() == null ? null : conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio();
			
			EnderecoPessoa enderecoPessoaLogada = enderecoPessoaService.findById(filialLogada.getPessoa().getEnderecoPessoa().getIdEnderecoPessoa());
			Long idMunicipioFilialLogada 	    = enderecoPessoaLogada.getMunicipio().getIdMunicipio();
			Long idFilialDestino = (Long) conhecimentoNormalService.getAtendimentoMunicipio(conhecimento).get("idFilial");
			
			String tpConhecimento = conhecimento.getTpConhecimento().getValue();
			if (coletaBalcao || 
					CONHECIMENTO_REFATURAMENTO.equalsIgnoreCase(tpConhecimento) || 
					CONHECIMENTO_DEVOLUCAO.equalsIgnoreCase(tpConhecimento) ) {
				idMunicipioOrigemPrestacaoServico = idMunicipioFilialLogada;
				
			} else if (coletaTelefone || coletaAutomatica) {
				idMunicipioOrigemPrestacaoServico = conhecimento.getPedidoColeta() == null 
						? null 
						: (conhecimento.getPedidoColeta().getMunicipio() == null 
							? null : 
							conhecimento.getPedidoColeta().getMunicipio().getIdMunicipio());
			}

			Filial filialDestino = filialService.findById(idFilialDestino);

			Municipio municipioFilialOrigem = conhecimento.getFilialByIdFilialOrigem().getPessoa().getEnderecoPessoa().getMunicipio();
			Municipio municipioFilialDestino = filialDestino.getPessoa().getEnderecoPessoa().getMunicipio();
			Boolean isMunicipioFilialOrigemEDestinoEquals = (municipioFilialOrigem != null && municipioFilialDestino != null) && municipioFilialOrigem.getIdMunicipio().equals(municipioFilialDestino.getIdMunicipio());			
			
			boolean isFilialDesinoEqualsFilialSession = idFilialDestino.equals(filialLogada.getIdFilial());
			Boolean isMunicipioEquals = idMunicipioOrigemPrestacaoServico != null && idMunicipioEntrega != null && idMunicipioOrigemPrestacaoServico.equals(idMunicipioEntrega);
			
			if(isMunicipioEquals && isMunicipioFilialOrigemEDestinoEquals) {
				tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
			} else if (isMunicipioEquals && isFilialDesinoEqualsFilialSession) {
				tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
			} else if (ConstantesExpedicao.CONHECIMENTO_REENTREGA.equals(conhecimento.getTpConhecimento().getValue()) 
					&& idMunicipioEntrega.equals(idMunicipioFilialLogada)) {
				tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
			} else if(ConstantesExpedicao.CONHECIMENTO_DEVOLUCAO.equals(conhecimento.getTpConhecimento().getValue())
					&& idMunicipioEntrega.equals(idMunicipioFilialLogada)
					&& isFilialDesinoEqualsFilialSession) {
				tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE;
			}
			
			if( ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE.equalsIgnoreCase( tpDocumento )){
				Long idServicoTributo = LongUtils.getLong((BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.ID_SERVICO_TRIBUTO_NFT));
				Long idMunicipioSede = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getIdMunicipio();
				Long idMunicipioServico = conhecimento.getMunicipioByIdMunicipioEntrega().getIdMunicipio(); 
				
				TypedFlatMap emiteNfServico = aliquotaIssMunicipioServService.findEmiteNfServico(null, idServicoTributo, idMunicipioSede, idMunicipioServico);
				if (Boolean.FALSE.equals(emiteNfServico.getBoolean("BlEmiteNota"))) {
				    throw new BusinessException("LMS-04340");
				}
				
				if( Boolean.TRUE.equals(emiteNfServico.getBoolean("BlEmiteNFeletronica")) ){
				    tpDocumento = ConstantesExpedicao.NOTA_FISCAL_TRANSPORTE_ELETRONICA;
					
					if (StringUtils.isBlank(filialLogada.getPessoa().getNrInscricaoMunicipal())) {
					    throw new BusinessException("LMS-04379");
					}
					
					/* 
					 * Se for uma geração de NTE verificar se o cliente definido como tomador do serviço (cliente definido como Responsável do frete no item ValidaCTRC.3)
					 * Neste ponto ainda nao foi chamada a rotina ValidaCTRC, por isso eh chamado apenas este ponto especifico.
					 * 
					 */

                    Cliente clienteDevedor = conhecimentoNormalService.getClienteResponsavel(conhecimento);
                    
                    if (clienteDevedor != null) {
                        
                        Cliente responsavel = clienteService.findById(clienteDevedor.getIdCliente());
                        
                        if (StringUtils.isBlank(responsavel.getPessoa().getNrInscricaoMunicipal())) {
                            throw new BusinessException("LMS-04380");
                        }
                            
                    }
					
				}
			}
		}
		
		if (ConstantesExpedicao.CONHECIMENTO_NACIONAL.equals(tpDocumento)){
			Object value = conteudoParametroFilialService.findConteudoByNomeParametro(filialLogada.getIdFilial(), "INDICADOR CTE", false);
			if(value != null){
				String indicadorFilial = String.valueOf(value);
				if ("2".equals(indicadorFilial)){
					tpDocumento = ConstantesExpedicao.CONHECIMENTO_ELETRONICO;
				} else if("1".equals(indicadorFilial)){
					Boolean blPermiteCte = conhecimento.getClienteByIdClienteRemetente().getBlPermiteCte();
					if(blPermiteCte != null && blPermiteCte){
						tpDocumento = ConstantesExpedicao.CONHECIMENTO_ELETRONICO;
					}
				}
			}
		}

		return tpDocumento;
	}		
	
	public Boolean findObrigatoriedadePinSuframa(final Long idClienteDestinatario, final Long idClienteConsignatario){
		Cliente cliente;
		if(idClienteConsignatario == null){
			cliente = clienteService.findById2(idClienteDestinatario);
		} else {
			cliente = clienteService.findById2(idClienteConsignatario);
		}
		Long idUnidadeFederativaCliente = cliente.getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
		Long idUnidadeFederativaFilial = SessionUtils.getFilialSessao().getPessoa().getEnderecoPessoa().getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa();
		
		String dsObrigaPinSuframa = (String) configuracoesFacade.getValorParametro("Obriga_PIN_SUFRAMA");
		
		//nao deve ser feito contains na string pois pode trazer resultados incorretos
		//hashset é a collection com melhor performance para o uso de contains
		Set<Long> obrigaPinSuframa = new HashSet<Long>();
		try {
			for (String id : dsObrigaPinSuframa.split(";")) {
				obrigaPinSuframa.add(Long.valueOf(id));
			}
		} catch (Exception e) {
			throw new IllegalStateException("PARAMETRO Obriga_PIN_SUFRAMA NAO ESTA NO FORMATO ID;ID;ID;ID");
		}
		
		return obrigaPinSuframa.contains(idUnidadeFederativaCliente) || obrigaPinSuframa.contains(idUnidadeFederativaFilial);
	}
	
	public Boolean findResponsavelPaleteFechado(Conhecimento conhecimento){
		conhecimentoNormalService.validateClienteResposavel(conhecimento);
		conhecimentoNormalService.setClienteResponsavel(conhecimento);
		if (conhecimento.getDevedorDocServs() == null || conhecimento.getDevedorDocServs().get(0) == null  || conhecimento.getDevedorDocServs().get(0).getCliente() == null)
			return false;		
		else{
			Cliente responsavel = clienteService.findById(conhecimento.getDevedorDocServs().get(0).getCliente().getIdCliente());
			Boolean result = responsavel.getBlPaleteFechado();
			return result == null ? false : result;	
		}
	}
	
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setCotacaoService(CotacaoService cotacaoService) {
		this.cotacaoService = cotacaoService;
	}
	public void setDensidadeService(DensidadeService densidadeService) {
		this.densidadeService = densidadeService;
	}
	
	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setAliquotaIssMunicipioServService(
			AliquotaIssMunicipioServService aliquotaIssMunicipioServService) {
		this.aliquotaIssMunicipioServService = aliquotaIssMunicipioServService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public ConhecimentoNormalService getConhecimentoNormalService() {
		return conhecimentoNormalService;
	}

	public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
		this.conhecimentoNormalService = conhecimentoNormalService;
	}
	
    // LMSA-6160
    public void setDigitarDadosNotaNormalNotasFiscaisService(DigitarDadosNotaNormalNotasFiscaisService digitarDadosNotaNormalNotasFiscaisService) {
        this.digitarDadosNotaNormalNotasFiscaisService = digitarDadosNotaNormalNotasFiscaisService;
    }

	private Moeda buscarMoedaSessao(Long idFilial){
		Moeda moeda = SessionUtils.getMoedaSessao();

		if (moeda == null) {
			EnderecoPessoa enderecoPessoa = this.enderecoPessoaService.findEnderecoPessoaPadrao(idFilial);
			if(null == enderecoPessoa)
				throw new BusinessException("filialSemEndereco");

			Pais pais = enderecoPessoa.getMunicipio().getUnidadeFederativa().getPais();
			if(null == pais)
				throw new BusinessException("enderecoFilialSemPais");

			moeda = moedaService.findMoedaPadraoByPais(pais.getIdPais());
		}

		return moeda;
	}

	public MoedaService getMoedaService() {
		return moedaService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
}
