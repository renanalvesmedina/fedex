package com.mercurio.lms.expedicao.edi.model.service;

import br.com.tntbrasil.integracao.domains.jms.IProducer;
import br.com.tntbrasil.integracao.domains.jms.Queues;
import com.mercurio.adsm.core.util.ADSMInitArgs;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.coleta.model.PedidoColeta;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.configuracoes.model.service.*;
import com.mercurio.lms.edi.dto.*;
import com.mercurio.lms.edi.enums.StatusProcessamento;
import com.mercurio.lms.edi.model.NotaFiscalEdi;
import com.mercurio.lms.edi.model.service.LogAtualizacaoEDIService;
import com.mercurio.lms.edi.model.service.LogErrosEDIService;
import com.mercurio.lms.expedicao.dto.DadosProcessamentoEdiDto;
import com.mercurio.lms.expedicao.dto.DivisaoClienteDto;
import com.mercurio.lms.expedicao.dto.InscricaoEstadualDto;
import com.mercurio.lms.expedicao.dto.ProcessarNotaPedidoColetaDto;
import com.mercurio.lms.expedicao.model.*;
import com.mercurio.lms.edi.model.LogEDIDetalhe;
import com.mercurio.lms.edi.model.service.LogEDIDetalheService;
import com.mercurio.lms.expedicao.dto.*;
import com.mercurio.lms.expedicao.model.service.*;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.NotaFiscalEdiUtils;
import com.mercurio.lms.expedicao.util.ProcessarEdiUtils;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.ErroAgruparException;
import com.mercurio.lms.util.ErroProcessarNotasFiscaisEdiItemException;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ClienteOperadorLogistico;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteOperadorLogisticoService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;

import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

public class ProcessarNotasEDIDadosGeraisService {

    private ClienteService clienteService;
    private TipoTributacaoIEService tipoTributacaoIEService;
    private DivisaoClienteService divisaoClienteService;
    private ContingenciaService contingenciaService;
    private ClienteOperadorLogisticoService clienteOperadorLogisticoService;
    private PedidoColetaService pedidoColetaService;
    private FilialService filialService;
    private NotaFiscalEDIService notaFiscalEDIService;
    private ProcessarNotaFiscalEDIService processarNotaFiscalEDIService;
    private LogEDIDetalheService logEDIDetalheService;
    private ConfiguracoesFacade configuracoesFacade;
    private NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService;
    private ParametroGeralService parametroGeralService;
    private CCEItemService cceItemService;
    private IntegracaoJmsService integracaoJmsService;
    private ProcessarNotasEDICommonService processarNotasEDICommonService;
    private LogAtualizacaoEDIService logAtualizacaoEDIService;
    private ConhecimentoService conhecimentoService;
    private MonitoramentoDescargaService monitoramentoDescargaService;
    private DoctoServicoService doctoServicoService;
    private LogErrosEDIService logErrosEDIService;
    private ConhecimentoNormalService conhecimentoNormalService;
    private ProcessamentoIbEdiService processamentoIbEdiService;
    private IntegracaoJwtService integracaoJwtService;
    private PaisService paisService;
    private ProcessamentoFilaIbAgruparService processamentoFilaIbAgruparService;

    private MoedaService moedaService;

    private HistoricoFilialService historicoFilialService;
    private PlatformTransactionManager transactionManager;


    private final Log log = LogFactory.getLog(ProcessarNotasEDIDadosGeraisService.class);

    public DadosProcessamentoEdiDto findDadosGeraisCliente(String nrIdentificacao, Long idFilial) throws Exception {
        DadosProcessamentoEdiDto dadosProcessamentoEdi = new DadosProcessamentoEdiDto();
        List<Map<String, Object>> clients = clienteService.findClienteByNrIdentificacao(nrIdentificacao);
        if (clients.isEmpty()) {
            throw new BusinessException("LMS-00061");
        }
        List<InscricaoEstadualDto> inscricaoEstadualDto = null;
        List<ProcessarNotaPedidoColetaDto> processarNotaPedidoColeta = null;
        Map<String, Object> client = clients.get(0);
        Long idCliente = (Long) client.get("idCliente");
        Cliente cliente = clienteService.findById(idCliente);
        Filial filial = filialService.findById(idFilial);
        contingenciaService.validateProcessarClienteEdi(cliente.getBlLiberaEtiquetaEdi(), idFilial);

        Pessoa pessoa = cliente.getPessoa();
        EnderecoPessoa enderecoPessoa = pessoa.getEnderecoPessoa();
        Municipio municipio = enderecoPessoa.getMunicipio();
        UnidadeFederativa unidadeFederativa = municipio.getUnidadeFederativa();
        String dsTipoLogradouro = enderecoPessoa.getTipoLogradouro().getDsTipoLogradouro().getValue();
        List<TipoTributacaoIE> tipoTributacao = tipoTributacaoIEService.findVigentesByIdPessoa(idCliente);
        Boolean blDivisao = BooleanUtils.isTrue(cliente.getBlDivisao());
        List<DivisaoCliente> divisaoClientes = findDivisaoCliente(cliente, blDivisao);
        List<Map<String, Object>> pedidoColeta = findPedidosColetaByIdCliente(idCliente, filial);
        List<DivisaoClienteDto> divisaoClienteDtos = divisaoClientes.stream()
                .map(dc ->
                        new DivisaoClienteDto(dc.getIdDivisaoCliente(),
                                dc.getDsDivisaoCliente()
                        )).collect(Collectors.toList());
        processarNotaPedidoColeta = pedidoColeta.stream()
                .map(pc ->
                        new ProcessarNotaPedidoColetaDto
                                ((Long) pc.get("idPedidoColeta"),
                                        (Boolean) pc.get("blProdutoDiferenciado"),
                                        (String) pc.get("nrColetaDhEvento"),
                                        (Long) pc.get("nrColeta")
                                )
                ).collect(Collectors.toList());

        inscricaoEstadualDto = tipoTributacao.stream()
                .map(tt ->
                {
                    InscricaoEstadual inscricaoEstadual = tt.getInscricaoEstadual();
                    return new InscricaoEstadualDto
                            (
                                    inscricaoEstadual.getIdInscricaoEstadual(),
                                    inscricaoEstadual.getNrInscricaoEstadual(),
                                    BooleanUtils.isTrue(inscricaoEstadual.getBlIndicadorPadrao())
                            );
                }).collect(Collectors.toList());
        dadosProcessamentoEdi.setDivisaoCliente(divisaoClienteDtos);
        dadosProcessamentoEdi.setPedidoColeta(processarNotaPedidoColeta);
        dadosProcessamentoEdi.setInscricaoEstadual(inscricaoEstadualDto);
        dadosProcessamentoEdi.setBlDivisao(blDivisao);
        dadosProcessamentoEdi.setBlProdutoPerigoso(BooleanUtils.isTrue(cliente.getBlProdutoPerigoso()));
        dadosProcessamentoEdi.setBlControladoPoliciaCivil(BooleanUtils.isTrue(cliente.getBlControladoPoliciaCivil()));
        dadosProcessamentoEdi.setBlControladoPoliciaFederal(BooleanUtils.isTrue(cliente.getBlControladoPoliciaFederal()));
        dadosProcessamentoEdi.setBlControladoExercito(BooleanUtils.isTrue(cliente.getBlControladoExercito()));
        dadosProcessamentoEdi.setNumeroIdentificacao(nrIdentificacao);
        dadosProcessamentoEdi.setNomePessoa(pessoa.getNmPessoa());
        dadosProcessamentoEdi.setTipoCliente(cliente.getTpCliente().getValue());
        dadosProcessamentoEdi.setDescricaoEndereco(dsTipoLogradouro + " " + enderecoPessoa.getDsEndereco());
        dadosProcessamentoEdi.setBlDificuldadeEntrega(BooleanUtils.isTrue(cliente.getBlDificuldadeEntrega()));
        dadosProcessamentoEdi.setBlNumeroVolumeEDI(BooleanUtils.isTrue(cliente.getBlNumeroVolumeEDI()));
        dadosProcessamentoEdi.setBlObrigaSerie(BooleanUtils.isTrue(cliente.getBlObrigaSerie()));
        dadosProcessamentoEdi.setDescricaoTipoLogradouro(dsTipoLogradouro);
        dadosProcessamentoEdi.setDescricaoBairro(enderecoPessoa.getDsBairro());
        dadosProcessamentoEdi.setIdCliente(idCliente);
        dadosProcessamentoEdi.setIdMunicipio(municipio.getIdMunicipio());
        dadosProcessamentoEdi.setIdTipoLogradouro(enderecoPessoa.getTipoLogradouro().getIdTipoLogradouro());
        dadosProcessamentoEdi.setIdUnidadeFederativa(unidadeFederativa.getIdUnidadeFederativa());
        dadosProcessamentoEdi.setNomeMunicipio(municipio.getNmMunicipio());
        dadosProcessamentoEdi.setNomePais(unidadeFederativa.getPais().getNmPais().getValue());
        dadosProcessamentoEdi.setNrEndereco(enderecoPessoa.getNrEndereco());
        dadosProcessamentoEdi.setNumeroCep(enderecoPessoa.getNrCep());
        dadosProcessamentoEdi.setUnidadeFederativa(unidadeFederativa.getSgUnidadeFederativa());
        dadosProcessamentoEdi.setIdFilial(idFilial);
        return dadosProcessamentoEdi;
    }

    private List<Map<String, Object>> findPedidosColetaByIdCliente(Long idCliente, Filial filial) {
        List<Map<String, Object>> result = new ArrayList<>();
        List<ClienteOperadorLogistico> operadores = clienteOperadorLogisticoService.findByIdClienteOperado(idCliente);
        if (operadores != null && !operadores.isEmpty()) {
            for (ClienteOperadorLogistico clienteOperadorLogistico : operadores) {
                result.addAll(pedidoColetaService.findLookupPedidoColeta(clienteOperadorLogistico.getClienteOperador().getIdCliente(), filial));
            }
        }
        result.addAll(pedidoColetaService.findLookupPedidoColeta(idCliente, filial));
        return result;
    }

    private List<DivisaoCliente> findDivisaoCliente(Cliente cliente, boolean blDivisao) {

        List<DivisaoCliente> divisoes = new ArrayList<>();

        if (!Boolean.TRUE.equals(blDivisao)) {
            return divisoes;
        }

        Long idClienteTomador = cliente.getIdCliente();
        if (cliente.getTpCliente() != null && "F".equals(cliente.getTpCliente().getValue())) {
            idClienteTomador = cliente.getClienteMatriz().getIdCliente();
        }

        divisoes = divisaoClienteService.findDivisaoClienteByClienteAndSituacao(idClienteTomador, "A");

        return divisoes;
    }


    public NotaFiscalDoctoClienteDto findNotasFiscaisDoctoCliente(FiltroNotaFiscalDoctoClienteDto filtroNotaFiscalDoctoClienteDto) throws Exception {

        NotaFiscalDoctoClienteDto notaFiscalDoctoClienteDto = new NotaFiscalDoctoClienteDto();

        String habilitaReprocessamento = (String) configuracoesFacade
                .getValorParametro
                        (filtroNotaFiscalDoctoClienteDto.getIdFilial(), "REPROCESSA_EDI");
        if ("S".equals(habilitaReprocessamento)) {
            List<LogEDIDetalhe> listLogReprocessamento = logEDIDetalheService
                    .findByDoctoClienteReprocessamento
                            (
                                    filtroNotaFiscalDoctoClienteDto.getIdCliente(),
                                    filtroNotaFiscalDoctoClienteDto.getNrDoctoCliente(),
                                    filtroNotaFiscalDoctoClienteDto.getTpProcessamento()
                            );
            if (!listLogReprocessamento.isEmpty()) {
                for (LogEDIDetalhe logEdiDetalhe : listLogReprocessamento) {
                    notaFiscalEDIService.executeRecargaNotaEDI(logEdiDetalhe, filtroNotaFiscalDoctoClienteDto.getIdCliente());
                }
            }
        }

        List<NotaFiscalEdi> notas = notaFiscalExpedicaoEDIService
                .findNotaFiscalByDoctoCliente
                        (
                                filtroNotaFiscalDoctoClienteDto.getIdCliente(),
                                filtroNotaFiscalDoctoClienteDto.getNrDoctoCliente()
                        );
        if (notas.isEmpty()) {
            throw new Exception("Documento não possui nota(s) cadastrada(s)");
        }
        List<NotaFiscalEdiDto> notaFiscalEdiDtos = notas.stream().map(NotaFiscalEdiDto::new).collect(Collectors.toList());
        DateTime dataHora = JTDateTimeUtils.getDataHoraAtual();
        notaFiscalDoctoClienteDto.setDataHora(dataHora.toString("dd/MM/yyyy HH:mm:ss"));
        notaFiscalDoctoClienteDto.setNotaFiscal(notaFiscalEdiDtos);
        return notaFiscalDoctoClienteDto;
    }

    public Map<String, String> findInformacaoDoctoClienteDescription(Long idCliente) {
        Map<String, String> retorno = new HashMap<>();
        String descricao = processarNotaFiscalEDIService.findInformacaoDoctoClienteDescription(idCliente);
        retorno.put("descriptionTitle", descricao);
        return retorno;
    }

    public ResponseFiltroNotasFiscaisEdiDTO executeFiltroNotasFiscaisEdi(RequestFiltroNotasFiscaisEdiDTO requestFiltroNFEdiDTO) {

        final Long nrProcessamento = notaFiscalExpedicaoEDIService.findNextNrProcessamento();
        final String processarPor = requestFiltroNFEdiDTO.getProcessarPor();
        final Long idCliente = requestFiltroNFEdiDTO.getIdCliente();
        final String opcaoProcessamento = requestFiltroNFEdiDTO.getOpcaoProcessamento();
        final String tpProcessamento = requestFiltroNFEdiDTO.getTpProcessamento();
        List<NotaFiscalProdutoDiferenciadoEdiDTO> notasFiscaisProdutosDiferenciados = requestFiltroNFEdiDTO.getNotasFiscaisProdutosDiferenciados();
        final String sgFilial = requestFiltroNFEdiDTO.getSgFilial();

        final Cliente cliente = clienteService.findByIdInitLazyProperties(idCliente, false);
        final List<Integer> nrNotasFiscais = findNumerosNotasEdi((List<Integer>) requestFiltroNFEdiDTO.getListNotasFiscaisEdiInformada(),
                requestFiltroNFEdiDTO.getListIntervalosNotasFiscaisEdi());

        final String tpAgrupamentoEdi = cliente.getTpAgrupamentoEDI() == null ? "N" : cliente.getTpAgrupamentoEDI().getValue();
        final String tpOrdemEmissaoEdi = cliente.getTpOrdemEmissaoEDI() == null ? "I" : cliente.getTpOrdemEmissaoEDI().getValue();

        List<NotaFiscalEdi> listNotaFiscalExpedicaoEdis = null;

        List<NotaFiscalEdi> notasFiscaisEncontradas = findNotasEdi(cliente, nrNotasFiscais, tpAgrupamentoEdi, processarPor, sgFilial);

        for (NotaFiscalEdi nfe : notasFiscaisEncontradas) {
            String nfeInformado = processarNotasEDICommonService.getChaveNfeFromMap(nfe.getNrNotaFiscal(), requestFiltroNFEdiDTO.getChavesNfe());
            if (nfeInformado != null) {
                nfe.setChaveNfe(nfeInformado);
            }
        }

        if (Boolean.TRUE.equals(validateIntervaloEtiqueta(requestFiltroNFEdiDTO.getIdCliente()))) {
            listNotaFiscalExpedicaoEdis = executeAgrupamentoClienteSemIntervaloEtiqueta(requestFiltroNFEdiDTO.getIdCliente(),
                    requestFiltroNFEdiDTO.getProcessarPor(), (List<NotaFiscalEdi>) requestFiltroNFEdiDTO.getListNotasFiscaisEdiInformada());
        } else {
            listNotaFiscalExpedicaoEdis = notaFiscalExpedicaoEDIService
                    .findByNrIdentificacaoByTpAgrupamentoEdiByTpOrdemEmissaoEdi
                            (
                                    cliente.getPessoa().getNrIdentificacao(), tpAgrupamentoEdi,
                                    tpOrdemEmissaoEdi, processarPor, tpProcessamento
                            );
        }

        List<ValidarEdiDTO> listaValidacao = new ArrayList<>();

        int i = 0;

        // "I" = Digitação
        if ("I".equals(tpOrdemEmissaoEdi) && !opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_AUTOMATICO)) {

            Map<String, String> mapChaveNFEXCae;
            final String nrCce = requestFiltroNFEdiDTO.getNrCce();

            if (nrCce != null) {
                List<CCEItem> cces = cceItemService.findByCCE(nrCce);
                mapChaveNFEXCae = processarNotasEDICommonService.createMapChaveNFEXCae(cces);
            } else {
                List<String> chavesNotasFiscaisEncontradas = new ArrayList<>();
                for (final NotaFiscalEdi idNotaFiscalEDI : notasFiscaisEncontradas) {
                    chavesNotasFiscaisEncontradas.add(idNotaFiscalEDI.getChaveNfe());
                }
                List<CCEItem> cces = new ArrayList<>();
                while (chavesNotasFiscaisEncontradas.size() > 999) {
                    List<String> sublist = new ArrayList<>(chavesNotasFiscaisEncontradas.subList(0, 999));
                    cces.addAll(cceItemService.findByChavesNfe(sublist));
                    chavesNotasFiscaisEncontradas.removeAll(sublist);
                }
                cces.addAll(cceItemService.findByChavesNfe(chavesNotasFiscaisEncontradas));
                mapChaveNFEXCae = processarNotasEDICommonService.createMapChaveNFEXCae(cces);
            }

            for (final NotaFiscalEdi idNotaFiscalEDI : notasFiscaisEncontradas) {
                NotaFiscalEdi notaFiscalEdi = new NotaFiscalEdi();
                notaFiscalEdi.setIdNotaFiscalEdi(idNotaFiscalEDI.getIdNotaFiscalEdi());
                final int index = listNotaFiscalExpedicaoEdis.indexOf(notaFiscalEdi);
                if (index >= 0) {
                    notaFiscalEdi = listNotaFiscalExpedicaoEdis.get(index);
                    ValidarEdiDTO dto = new ValidarEdiDTO();
                    dto.setClienteRemetente(cliente);
                    dto.setNotaFiscalEdi(notaFiscalEdi);
                    dto.setNrProcessamento(nrProcessamento);
                    dto.setTpProcessamento(tpProcessamento);
                    if (mapChaveNFEXCae != null && mapChaveNFEXCae.containsKey(notaFiscalEdi.getChaveNfe())) {
                        dto.setCae(mapChaveNFEXCae.get(notaFiscalEdi.getChaveNfe()));
                    }
                    dto.setIndex(i++);
                    dto.setIdFilial(requestFiltroNFEdiDTO.getIdFilial());
                    dto.setIdUsuario(requestFiltroNFEdiDTO.getIdUsuario());
                    dto.setProcessarPor(requestFiltroNFEdiDTO.getProcessarPor());
                    listaValidacao.add(dto);
                }
            }
        } else {
            for (final NotaFiscalEdi notaFiscalEdi : listNotaFiscalExpedicaoEdis) {
                if (opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_AUTOMATICO)) {
                    ValidarEdiDTO dto = new ValidarEdiDTO();
                    dto.setClienteRemetente(cliente);
                    dto.setNotaFiscalEdi(notaFiscalEdi);
                    dto.setNrProcessamento(nrProcessamento);
                    dto.setTpProcessamento(tpProcessamento);
                    dto.setIndex(i++);
                    dto.setIdFilial(requestFiltroNFEdiDTO.getIdFilial());
                    dto.setIdUsuario(requestFiltroNFEdiDTO.getIdUsuario());
                    dto.setProcessarPor(requestFiltroNFEdiDTO.getProcessarPor());
                    listaValidacao.add(dto);
                } else if (opcaoProcessamento.equals(ConstantesExpedicao.EDI_PROCESSAR_NOTAS_FISCAIS)) {
                    if (notasFiscaisEncontradas.contains(notaFiscalEdi)) {
                        ValidarEdiDTO dto = new ValidarEdiDTO();
                        dto.setClienteRemetente(cliente);
                        dto.setNotaFiscalEdi(notaFiscalEdi);
                        dto.setNrProcessamento(nrProcessamento);
                        dto.setTpProcessamento(tpProcessamento);
                        dto.setIndex(i++);
                        dto.setIdFilial(requestFiltroNFEdiDTO.getIdFilial());
                        dto.setIdUsuario(requestFiltroNFEdiDTO.getIdUsuario());
                        dto.setProcessarPor(requestFiltroNFEdiDTO.getProcessarPor());
                        listaValidacao.add(dto);
                    }
                }
            }
        }

        List<Long> idsNotasFiscais = notasFiscaisEncontradas.stream()
                    .map(nfe -> nfe.getIdNotaFiscalEdi())
                    .collect(Collectors.toList());
        
        if (notasFiscaisProdutosDiferenciados != null) {
            for (ValidarEdiDTO dto : listaValidacao) {
                NotaFiscalProdutoDiferenciadoEdiDTO produtoDiferenciado =
                        notasFiscaisProdutosDiferenciados.stream().filter(nfpd -> nfpd.getNrNotaFiscal().equals(dto.getNotaFiscalEdi().getNrNotaFiscal()))
                                .reduce((acc, nf) -> acc = nf).orElse(null);

                if (produtoDiferenciado != null) {
                    dto.setIsControladoExercito(produtoDiferenciado.getControladoExercito());
                    dto.setIsControladoPoliciaCivil(produtoDiferenciado.getControladoPoliciaCivil());
                    dto.setIsControladoPoliciaFederal(produtoDiferenciado.getControladoPoliciaFederal());
                    dto.setIsProdutoPerigoso(produtoDiferenciado.getProdutoPerigoso());
                }
            }
        }

        Long qtTotalNotas = Long.valueOf(listaValidacao.size());
        Long idProcessamentoEdi = processarNotaFiscalEDIService.storeProcessamentoEdi
                (cliente, requestFiltroNFEdiDTO.getIdFilial(), requestFiltroNFEdiDTO.getIdUsuario(), qtTotalNotas);

        List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTOList = listaValidacao
                .stream().map(dadoValido -> {
                    dadoValido.setIdProcessamentoEdi(idProcessamentoEdi);
                    return new DadosValidacaoEdiDTO(dadoValido);
                }).collect(Collectors.toList());

        return new
            ResponseFiltroNotasFiscaisEdiDTO
                (nrProcessamento, dadosValidacaoEdiDTOList.size(), dadosValidacaoEdiDTOList, idsNotasFiscais);
    }

    public List<Integer> findNumerosNotasEdi(List<Integer> listNotasFiscaisEdiInformada, final List<Map<String, Integer>> intervalosNotasFiscaisEdi) {

        List<Integer> nrNotasFiscais = listNotasFiscaisEdiInformada;

        if (nrNotasFiscais == null && intervalosNotasFiscaisEdi != null) {
            nrNotasFiscais = new ArrayList<>();
            for (final Map<String, Integer> intervalo : intervalosNotasFiscaisEdi) {
                Integer nrNotaFiscalInicial = intervalo.get("nrNotaFiscalInicial");
                final Integer nrNotaFiscalFinal = intervalo.get("nrNotaFiscalFinal");
                while (nrNotaFiscalInicial <= nrNotaFiscalFinal) {
                    nrNotasFiscais.add(nrNotaFiscalInicial++);
                }
            }
        }
        return nrNotasFiscais;
    }

    /**
     * Verifica atraves do parametro_geral ClientesSemValidaçãoIntervaloEtiqueta
     * se o cliente passado por parametro não possue validação de intervalo de
     * etiqueta
     *
     * @param
     * @return Boolean
     */
    public Boolean validateIntervaloEtiqueta(final Long idClienteRemetente) {

        final String clientes = (String) parametroGeralService.findConteudoByNomeParametro("ClientesSemValidaçãoIntervaloEtiqueta", false);

        final String[] idClientes = clientes.split(";");
        for (final String id : idClientes) {
            if (id.equals(String.valueOf(idClienteRemetente))) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private List<NotaFiscalEdi> executeAgrupamentoClienteSemIntervaloEtiqueta(Long idClienteRemetente, String processarPor,
                                                                              List<NotaFiscalEdi> listNotasFiscaisEdiInformada) {

        final Cliente clienteRemetente = clienteService.findByIdInitLazyProperties(idClienteRemetente, false);
        final List<NotaFiscalEdi> listNFEDI = new ArrayList<>();

        NotaFiscalEdi notaFiscalEdi;
        for (final NotaFiscalEdi nf : listNotasFiscaisEdiInformada) {
            notaFiscalEdi = notaFiscalExpedicaoEDIService.findByNrIdentificacaoByNrNotaFiscal(clienteRemetente.getPessoa().getNrIdentificacao(), nf.getNrNotaFiscal(), processarPor);
            notaFiscalEdi.setQtVolumeInformado(nf.getQtVolumeInformado());
            notaFiscalEdi.setNrEtiquetaInicial(nf.getNrEtiquetaInicial());
            notaFiscalEdi.setNrEtiquetaFinal(nf.getNrEtiquetaFinal());
            listNFEDI.add(notaFiscalEdi);
        }

        Collections.sort(listNFEDI, new Comparator<NotaFiscalEdi>() {

            public int compare(final NotaFiscalEdi nf1, final NotaFiscalEdi nf2) {
                return nf1.getCnpjDest().compareTo(nf2.getCnpjDest());
            }
        });

        return processarNotasEDICommonService.executeFiltroNotas(listNFEDI);
    }

    public List<NotaFiscalEdi> findNotasEdi(final Cliente cliente, final List<Integer> nrNotasFiscais, final String tpAgrupamentoEdi,
                                            final String processarPor, String sgFilial) {

        final String cnpj = cliente.getPessoa().getNrIdentificacao();
        Long idInformacaoDoctoCliente;

        if ("E".equals(tpAgrupamentoEdi) && cliente.getInformacaoDoctoCliente() != null) {
            idInformacaoDoctoCliente = cliente.getInformacaoDoctoCliente().getIdInformacaoDoctoCliente();
        } else {
            idInformacaoDoctoCliente = null;
        }

        if (nrNotasFiscais == null) {
            return notaFiscalExpedicaoEDIService.find(cnpj, idInformacaoDoctoCliente, processarPor);
        }

        final StringBuilder message = new StringBuilder("[EDI] Filial: ");
        message.append(sgFilial);
        message.append(" - Cliente: ");
        message.append(cnpj);
        message.append(" - NFs informadas a serem processadas: ");

        for (final Integer nrNotaFiscalEdi : nrNotasFiscais) {
            message.append(nrNotaFiscalEdi);
            message.append(",");
        }

        final List<NotaFiscalEdi> notasEncontradas = notaFiscalExpedicaoEDIService.findNotas(cnpj, nrNotasFiscais, idInformacaoDoctoCliente, processarPor);
        final List<NotaFiscalEdi> result = notasEncontradas.stream()
                .filter(nunNotaEdi -> {
                    return nrNotasFiscais.stream().map(Integer::valueOf)
                            .anyMatch(recebida -> recebida.equals(nunNotaEdi.getNrNotaFiscal()));
                }).collect(Collectors.toList());


        log.warn(message);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enviarFilaJms(IProducer destino, Serializable dado) {
        IntegracaoJmsService.JmsMessageSender msg = integracaoJmsService.createMessage(destino, dado);
        integracaoJmsService.storeMessage(msg);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void findConhecimentosSemPesagem(ProcessaNotasEdiItemDto processaNotasEdiItemDto) {
        ProcessarEdiUtils processarEdiUtils = new ProcessarEdiUtils();

        Map<String, Object> criteria = new HashMap<>();
        criteria.put("idPedidoColeta", processaNotasEdiItemDto.getIdPedidoColeta());
        criteria.put("idMonitoramentoDescarga", processaNotasEdiItemDto.getIdMonitoramentoDescarga());
        criteria.put("tpProcessamento", processaNotasEdiItemDto.getTpProcessamento());
        criteria.put("idFilial", processaNotasEdiItemDto.getIdFilial());
        List<Map<String, Object>> conhecimento = processarNotaFiscalEDIService.findConhecimentosSemPesagem(criteria);
        if (!conhecimento.isEmpty()) {

            List<ConhecimentoSemPesagemDto> conhecimentoSemPesagemDtos = processarEdiUtils
                    .toListConhecimentoSemPesagemDto(conhecimento, processaNotasEdiItemDto);
            ConhecimentoSemPesagemDto conhecimentoSemPesagemDto = conhecimentoSemPesagemDtos
                    .get(conhecimentoSemPesagemDtos.size() - 1);
            conhecimentoSemPesagemDto.getProcessaNotasEdiItem().setFinalizou(true);
            IntegracaoJmsService.JmsMessageSender msg = integracaoJmsService
                    .createMessage(Queues.FINALIZA_CONHECIMENTO_SEM_PESAGEM);
            conhecimentoSemPesagemDtos.stream().forEach(msg::addMsg);
            integracaoJmsService.storeMessage(msg);

        } else {
            enviarFilaJms(Queues.FINALIZA_PROCESSAMENTO_EDI, processaNotasEdiItemDto);
        }

    }

    public void updateQtNotasProcessadasProcessamentoEdi(StoreLogEdiDto storeLogEdiDto) {
        this.processarNotaFiscalEDIService.getProcessamentoEdiService().updateQtNotasProcessadasProcessamentoEdi(storeLogEdiDto);
    }

    private void finalizaConhecimentoSemPesagem(Long idConhecimento, Boolean blContingencia) {
        Conhecimento conhecimento = conhecimentoService.findById(idConhecimento);
        monitoramentoDescargaService.executeFechaConhecimento(conhecimento, blContingencia);
        doctoServicoService.executeValidacoesParaBloqueioValores(conhecimento.getIdDoctoServico());
    }


    private boolean validaClienteLiberaEtiquetaContingencia(Boolean blContingencia, Boolean isLiberaEtiquetaEdi,
                                                            Boolean isLiberaEtiquetaEdiLinehaul, String tpProcessamento) {

        if ("M".equals(tpProcessamento) && Boolean.TRUE.equals(isLiberaEtiquetaEdiLinehaul)) {
            return true;
        } else if ((!"M".equals(tpProcessamento)) && Boolean.TRUE.equals(isLiberaEtiquetaEdi)) {
            return true;
        } else return Boolean.TRUE.equals(blContingencia);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void finalizaProcessamentoEDI(ProcessaNotasEdiItemDto processaNotasEdiItemDto) {

        Long idPedidoColeta = processaNotasEdiItemDto.getIdPedidoColeta();
        Long idMonitoramentoDescarga = processaNotasEdiItemDto.getIdMonitoramentoDescarga();
        String nrProcessamento = String.valueOf(processaNotasEdiItemDto.getNrProcessamento());
        if (idPedidoColeta != null) {
            PedidoColeta pedidoColeta = this.pedidoColetaService.findByIdBasic(idPedidoColeta);
            pedidoColeta.setCliente(clienteService.findByIdPedidoColeta(idPedidoColeta));
            Long idFilial = processaNotasEdiItemDto.getIdFilial();
            Boolean blContingencia = contingenciaService.findByFilial(idFilial, "A", "E") != null;
            Boolean isLiberaEtiquetaEdi = Boolean.TRUE.equals(pedidoColeta.getCliente().getBlLiberaEtiquetaEdi());
            Boolean isLiberaEtiquetaEdiLinehaul = Boolean.TRUE.equals(pedidoColeta.getCliente().getBlLiberaEtiquetaEdiLinehaul());

            if ("BA".equals(pedidoColeta.getTpModoPedidoColeta().getValue()) && validaClienteLiberaEtiquetaContingencia
                    (blContingencia, isLiberaEtiquetaEdi, isLiberaEtiquetaEdiLinehaul, processaNotasEdiItemDto.getTpProcessamento())) {
                this.monitoramentoDescargaService.updateDescarcaFinalizadaMonitoramentoDescargaEDI(idMonitoramentoDescarga);
            } else {
                this.monitoramentoDescargaService.updateMonitoramentoDescargaEDI(pedidoColeta, idMonitoramentoDescarga);
            }
        } else {
            this.monitoramentoDescargaService.updateDigitacaoNotasIniciadaMonitoramentoDescargaEDI(idMonitoramentoDescarga);
        }

        IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.STORE_LOG_EDI);
        Long idProcessamentoEdi = processaNotasEdiItemDto.getIdProcessamentoEdi();
        List<NotaFiscalConhecimentoDto> notaFiscalConhecimentoDtos = processaNotasEdiItemDto
            .getParameters().getNotaFiscalConhecimento();

        notaFiscalConhecimentoDtos.stream().forEach(notaFisca -> processarNotaFiscalEDIService
            .carregarMessageSenderStoreLogEdiDto
                (idProcessamentoEdi, Long.valueOf(notaFisca.getNrNotaFiscal()), jmsMessageSender));
        integracaoJmsService.storeMessage(jmsMessageSender);

        this.processarNotaFiscalEDIService.enviarFilaRemoveRegistrosProcessados(processaNotasEdiItemDto);
        this.processarNotaFiscalEDIService.getProcessamentoEdiService().updateTpStatus(nrProcessamento, new DomainValue("PF"));
    }

    public List<DadosValidacaoEdiDTO> preValidarNotaFiscalEDIRest(List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTO) {
        NotaFiscalEdiUtils notaFiscalEdiUtils = new NotaFiscalEdiUtils();
        List<ValidarEdiDTO> validarEdiDTOList = new ArrayList<>();
        for (DadosValidacaoEdiDTO dadosValidacaoEdi: dadosValidacaoEdiDTO) {
            ValidarEdiDTO validarEdiDTO = notaFiscalEDIService
                .preValidarNotaFiscalEDI
                    (
                        notaFiscalEdiUtils.toValidarEdiDTO(dadosValidacaoEdi),
                        dadosValidacaoEdi.getTpProcessamento()
                    );
            validarEdiDTOList.add(validarEdiDTO);
        }
        return notaFiscalEdiUtils.toListDadostValidarEdiDTO(validarEdiDTOList);
    }

    public List<DadosValidacaoEdiDTO> executePreValidacaoVolume(List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTO) {
        NotaFiscalEdiUtils notaFiscalEdiUtils = new NotaFiscalEdiUtils();
        List<ValidarEdiDTO> validarEdiDTOList = new ArrayList<>();
        for (DadosValidacaoEdiDTO dadosValidacaoEdi : dadosValidacaoEdiDTO) {
            ValidarEdiDTO validarEdiDTO = notaFiscalEDIService
                    .preValidarVolumeNotaFiscalEDI
                        (
                            notaFiscalEdiUtils.toValidarEdiDTO(dadosValidacaoEdi),
                            dadosValidacaoEdi.getTpProcessamento()
                        );
            validarEdiDTOList.add(validarEdiDTO);
        }
        return notaFiscalEdiUtils.toListDadostValidarEdiDTO(validarEdiDTOList);
    }

    public List<DadosValidacaoEdiDTO> executeValidacaoEdiItem(List<DadosValidacaoEdiDTO> dadosValidarEdiDTO) {
        NotaFiscalEdiUtils notaFiscalEdiUtils = new NotaFiscalEdiUtils();
        Boolean valido;
        List<ValidarEdiDTO> listValidarEdiDTO = notaFiscalEdiUtils.toListValidarEdiDTO(dadosValidarEdiDTO);
        DadosValidacaoEdiDTO dadosValidarEdi = dadosValidarEdiDTO.get(0);
        addSessionUser(dadosValidarEdi.getIdUsuario());
        SessionContext.set(SessionKey.FILIAL_KEY, filialService.findById(dadosValidarEdi.getIdFilial()));
        for (ValidarEdiDTO validarEdiDTO : listValidarEdiDTO) {
            valido = processarNotaFiscalEDIService.executeValidacaoEdiItem(validarEdiDTO);
            validarEdiDTO.setValidado(valido);
            validarEdiDTO.setStatus(validarEdiDTO.getValidado() ? StatusProcessamento.INICIADO : StatusProcessamento.ERROR);
        }
        SessionContext.remove("adsm.session.authenticatedUser");
        SessionContext.remove(SessionKey.FILIAL_KEY);
        return notaFiscalEdiUtils.toListDadostValidarEdiDTO(listValidarEdiDTO);
    }

    public void executePrepararAgruparNotasFiscaisEdi(Long nrProcessamento) throws Exception {
        try {
            prepararAgruparNotasFiscaisEdi(nrProcessamento);
        } catch (ErroAgruparException e) {
            enviarFilaIbErroProcessamento(e.getErroAgruparNotaFiscalDto());
            throw new Exception();
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BusinessException.class, Exception.class})
    protected void prepararAgruparNotasFiscaisEdi(Long nrProcessamento) throws ErroAgruparException {

        ErroAgruparNotaFiscalDto erroAgruparNotaFiscalDto = new ErroAgruparNotaFiscalDto(nrProcessamento);
        ParamAgruparNotasFiscaisDTO panfDTO = null;
        List<ProcessarEdiDTO> dtoList = null;
        ProcessaNotasEdiItemDto processaNotasEdiItemDto = null;

        try {
            panfDTO = montarParamAgruparNotasFiscaisDTO(nrProcessamento);
            dtoList = agruparNotasFiscais(panfDTO);
        }catch (BusinessException be) {
            processaNotasEdiItemDto = gerarProcessaNotasEdiItemDto
                (panfDTO.getListIdsNotasFiscaisEdiInformada(), panfDTO.getNrProcessamento());

            erroAgruparNotaFiscalDto.setThrowable(new BusinessException(be.getMessageKey(), be.getMessageArguments()));
            erroAgruparNotaFiscalDto.setProcessaNotasEdiItemDto(processaNotasEdiItemDto);
            throw new ErroAgruparException(erroAgruparNotaFiscalDto);
        }catch (Exception e) {
            processaNotasEdiItemDto = gerarProcessaNotasEdiItemDto
                    (panfDTO.getListIdsNotasFiscaisEdiInformada(), panfDTO.getNrProcessamento());
            Exception exception = e.getMessage() != null
                                    ? new Exception(e.getMessage())
                                    : new Exception("Ocorreu erro não tratado no agrupamento das notas fiscais");
            erroAgruparNotaFiscalDto.setThrowable(exception);
            erroAgruparNotaFiscalDto.setProcessaNotasEdiItemDto(processaNotasEdiItemDto);
            throw new ErroAgruparException(erroAgruparNotaFiscalDto);
        }finally {
            destroiSessao();
        }

        try {
            enviarAgrupamentoFila(dtoList, panfDTO, nrProcessamento);
        }catch (Exception e) {
            processaNotasEdiItemDto = gerarProcessaNotasEdiItemDto
                    (panfDTO.getListIdsNotasFiscaisEdiInformada(), panfDTO.getNrProcessamento());

            erroAgruparNotaFiscalDto.setThrowable(new Exception("Ocorreu erro ao enviar agrupamento para processar"));
            erroAgruparNotaFiscalDto.setProcessaNotasEdiItemDto(processaNotasEdiItemDto);
            throw new ErroAgruparException(erroAgruparNotaFiscalDto);
        }
    }

    private List<ProcessarEdiDTO> agruparNotasFiscais(ParamAgruparNotasFiscaisDTO panfDTO) {
        this.criaSessao(panfDTO);
        return this.processarNotaFiscalEDIService.prepararNotasFiscaisEdi(panfDTO);
    }

    public void enviarAgrupamentoFila
    ( List<ProcessarEdiDTO> dtoList, ParamAgruparNotasFiscaisDTO panfDTO, Long nrProcessamento) throws IOException {

        List<ProcessaNotasEdiItemDto> processaNotasEdiItemDtos = null;

        ProcessarEdiUtils processarEdiUtils = new ProcessarEdiUtils();

        processaNotasEdiItemDtos = processarEdiUtils.toListProcessaNotasEdiItemDto(dtoList);

        Long idProcessamentoEdi = this.processarNotaFiscalEDIService.storeProcessamentoNotaEdi(processaNotasEdiItemDtos, panfDTO);

        processaNotasEdiItemDtos.forEach(pne -> {
            pne.setIdFilial(panfDTO.getIdFilial());
            pne.setIdUsuario(panfDTO.getIdUsuario());
            pne.setIdProcessamentoEdi(idProcessamentoEdi);
            pne.setListIdsNotasFiscaisEdiInformada(panfDTO.getListIdsNotasFiscaisEdiInformada());
        });
        int totalElementoLista = processaNotasEdiItemDtos.size() - 1;
        ProcessaNotasEdiItemDto processaNotasEdiItem = processaNotasEdiItemDtos.get(totalElementoLista);
        processaNotasEdiItem.setFinalizou(Boolean.TRUE);
        ProcessaNotasEdiItemDto processaNotasItem = processaNotasEdiItemDtos.get(0);
        processaNotasItem.setPrimeiraExecucao(Boolean.TRUE);

        enviarProcessaNotasEdiItem(processaNotasEdiItemDtos);
        processamentoFilaIbAgruparService.removeByNrProcessamento(nrProcessamento);

    }

    private void storeNotasEdiPosProc(List<ProcessaNotasEdiItemDto> processaNotasEdiItemDto) throws IOException {
        ProcessamentoIbEdi processamentoIbEdi = new ProcessamentoIbEdi();
        processamentoIbEdi.setNrProcessamento(processaNotasEdiItemDto.get(0).getNrProcessamento());
        processamentoIbEdi.setDhInclusao(new DateTime());
        if(processaNotasEdiItemDto.size() == 1){
            processamentoIbEdi.setDhEnvio(processamentoIbEdi.getDhInclusao());
        }
        processamentoIbEdi.setDsDados(new ObjectMapper().writeValueAsString(processaNotasEdiItemDto));
        processamentoIbEdiService.store(processamentoIbEdi);
    }

    public void executeProcessarNotasFiscaisEdiItem(ProcessaNotasEdiItemDto processaNotasEdiItemDto) throws Exception {
        try {
            this.processarNotaFiscalEDIService.executeProcessarNotasFiscaisEdiItem(processaNotasEdiItemDto);
        }catch (ErroProcessarNotasFiscaisEdiItemException exception) {
            this.processarNotaFiscalEDIService.tratarErroProcessarNotasFiscaisEdiItem(exception);
            throw new Exception();
        }
    }

    public void executeAjustarNotas(LogErroDTO logErroDTO) {
        processarNotaFiscalEDIService.executeAjustarNotasEDI(logErroDTO);
    }

    public List<LogErrosEdiDTO> findNotasEDIParaAjuste(String naoTrazerLogsDoTipo, Integer nrProcessamento) {
        return logErrosEDIService.findNotasEDIParaAjuste(naoTrazerLogsDoTipo, nrProcessamento);
    }

    public void executeOpenMonitoramentoDescarga(Conhecimento conhecimento, Map mapMeioTransporte, String nrProcessamento) {
        processarNotaFiscalEDIService.openMonitoramentoDescarga(conhecimento, mapMeioTransporte, nrProcessamento);
    }

    public void executeRecarregarListaValicadao(List<DadosValidacaoEdiDTO> listaDadosValidacao) {
        this.processarNotaFiscalEDIService.executeRecarregarListaDadosValicadaoEdi(listaDadosValidacao);
    }

    public void executeAjustarVolumes(List<LogErrosEdiDTO> logErrosEdiDTOList) {
        Map<String, Object> mapDto = new HashMap<>();
        mapDto.put("listaLogErrosEDI", logErrosEdiDTOList);
        processarNotaFiscalEDIService.executeAjustarVolumesEDI(mapDto);
    }

    public void executeAjustarVolumes(LogErroDTO logErroDTO) {
        processarNotaFiscalEDIService.executeAjustarVolumesEDI(logErroDTO);
    }

    @Transactional
    public void prepararDadosAgruparNotasFiscaisEdi(ParamAgruparNotasFiscaisDTO panfDTO, String token) throws Exception {
        try {
            ProcessamentoFilaIbAgrupar processamentoFilaIbAgrupar = new ProcessamentoFilaIbAgrupar();
            ProcessamentoAgrupamentoDTO processamentoAgrupamentoDTO = new ProcessamentoAgrupamentoDTO(panfDTO.getNrProcessamento());
            Long idEmpresa = integracaoJwtService.getIdEmpresaByToken(token);
            panfDTO.setIdEmpresa(idEmpresa);
            DadosValidacaoEdiDTO dadosValidacaoEdiDTO = panfDTO.getListaValidacao().get(0);
            Long idProcessamentoEdi = dadosValidacaoEdiDTO.getIdProcessamentoEdi();
            ProcessamentoEdi processamentoEdi = processarNotaFiscalEDIService.getProcessamentoEdiService().findById(idProcessamentoEdi);
            processamentoEdi.setBlVisivel(true);

            IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService
                .createMessage(Queues.PREPARAR_DADOS_AGRUPAR_NOTAS_FISCAIS_EDI);

            String paramAgruparNotasFiscais = new ObjectMapper().writeValueAsString(panfDTO);
            processamentoFilaIbAgrupar.setNrProcessamento(panfDTO.getNrProcessamento());
            processamentoFilaIbAgrupar.setDsParamAgruparNotaFiscal(paramAgruparNotasFiscais);
            jmsMessageSender.addMsg(processamentoAgrupamentoDTO);

            processamentoFilaIbAgruparService.store(processamentoFilaIbAgrupar);
            processarNotaFiscalEDIService.getProcessamentoEdiService().store(processamentoEdi);
            integracaoJmsService.storeMessage(jmsMessageSender);
        }catch (Exception e) {
            throw new Exception("Ocorreu um erro ao salvar os parâmetros de agrupamento");
        }
    }

    private ParamAgruparNotasFiscaisDTO montarParamAgruparNotasFiscaisDTO(Long nrProcessamento) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ProcessamentoFilaIbAgrupar processamentoFilaIbAgrupar = processamentoFilaIbAgruparService.findByNrProcessamento(nrProcessamento);
        try {
            ParamAgruparNotasFiscaisDTO paramAgruparNotasFiscaisDTO = mapper.readValue(processamentoFilaIbAgrupar.getDsParamAgruparNotaFiscal(), ParamAgruparNotasFiscaisDTO.class);
            return paramAgruparNotasFiscaisDTO;
        }catch (Exception e) {
            throw new Exception("Ocorreu um erro na recuperação do parametros para agrupamento");
        }
    }

    private void criaSessao(ParamAgruparNotasFiscaisDTO panfDTO){
        Filial filial =  filialService.findFilialLogadoById(panfDTO.getIdFilial());;
        Usuario usuario = filialService.getUsuarioService().findById(panfDTO.getIdUsuario());
        Empresa empresa = filialService.getEmpresaService().findEmpresaLogadoById(panfDTO.getIdEmpresa());
        SessionContext.setUser(usuario);
        SessionContext.set(SessionKey.EMPRESA_KEY, empresa);
        SessionContext.set(SessionKey.FILIAL_KEY, filial);
        SessionContext.set(SessionKey.FILIAL_DTZ, filial.getDateTimeZone());
        SessionContext.set(SessionKey.MOEDA_KEY, moedaService.findMoedaByUsuarioEmpresa(usuario, empresa));
        SessionContext.set(SessionKey.PAIS_KEY, paisService.findPaisByUsuarioEmpresa(usuario, empresa));
        SessionContext.set(SessionKey.ULT_HIST_FILIAL_KEY, historicoFilialService.findUltimoHistoricoFilial(filial.getIdFilial()));
        SessionContext.set(SessionKey.FILIAL_MATRIZ_KEY, historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial()));
    }

    private void destroiSessao(){
        SessionContext.remove(SessionKey.EMPRESA_KEY);
        SessionContext.remove(SessionKey.FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_DTZ);
        SessionContext.remove(SessionKey.MOEDA_KEY);
        SessionContext.remove(SessionKey.PAIS_KEY);
        SessionContext.remove("adsm.session.authenticatedUser");
        SessionContext.remove(SessionKey.ULT_HIST_FILIAL_KEY);
        SessionContext.remove(SessionKey.FILIAL_MATRIZ_KEY);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, noRollbackFor = Exception.class)
    public void storeLogProcessamentoEdi(ErroAgruparNotaFiscalDto erroAgruparNotaFiscalDto) throws Exception {
        try {
            Throwable throwable = erroAgruparNotaFiscalDto.getMessageKey() != null ?
            new BusinessException(erroAgruparNotaFiscalDto.getMessageKey(), erroAgruparNotaFiscalDto.getMessageArgs()):
            new Exception(erroAgruparNotaFiscalDto.getMessage());
            throwable.setStackTrace(erroAgruparNotaFiscalDto.getStackTrace());
            erroAgruparNotaFiscalDto.setThrowable(throwable);

            ParamAgruparNotasFiscaisDTO panfDTO = montarParamAgruparNotasFiscaisDTO(erroAgruparNotaFiscalDto.getNrProcessamento());

            List<DadosValidacaoEdiDTO> dadosValidacaoEdiDTOList = panfDTO.getListaValidacao();

            Cliente cliente = clienteService.findById(dadosValidacaoEdiDTOList.get(0).getClienteRemetente().getIdCliente());

            for(DadosValidacaoEdiDTO dadosValidacaoEdiDTO : dadosValidacaoEdiDTOList) {
                processarNotaFiscalEDIService
                    .erroMonitoramentoNota
                        (
                            dadosValidacaoEdiDTO.getNotaFiscalEdi().getNrNotaFiscal(), cliente,
                            dadosValidacaoEdiDTO.getIdProcessamentoEdi(), erroAgruparNotaFiscalDto.getThrowable()
                        );
            }
            processamentoFilaIbAgruparService.removeByNrProcessamento(erroAgruparNotaFiscalDto.getNrProcessamento());
            processarNotaFiscalEDIService
                .enviarFilaRemoveRegistrosProcessados(erroAgruparNotaFiscalDto.getProcessaNotasEdiItemDto());
        } catch (Exception e) {
            throw new Exception("Ocorreu um erro não tratado");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void enviarFilaIbErroProcessamento(ErroAgruparNotaFiscalDto erroAgruparNotaFiscal) {
        erroAgruparNotaFiscal.setMessage(erroAgruparNotaFiscal.getThrowable().getMessage());
        erroAgruparNotaFiscal.setStackTrace(erroAgruparNotaFiscal.getThrowable().getStackTrace());
        if (erroAgruparNotaFiscal.getThrowable() instanceof BusinessException) {
            BusinessException businessException = (BusinessException)erroAgruparNotaFiscal.getThrowable();
            erroAgruparNotaFiscal.setMessageKey(businessException.getMessageKey());
            erroAgruparNotaFiscal.setMessageArgs(businessException.getMessageArguments());
        }

        erroAgruparNotaFiscal.setThrowable(null);
        erroAgruparNotaFiscal.setStackTrace(new StackTraceElement[0]);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://" + ADSMInitArgs.LMS_ADDRESS.getValue())
            .path("/services/processar/notas/edi/store-log-processamento-edi");
        target.request(MediaType.APPLICATION_JSON).post((Entity.entity(erroAgruparNotaFiscal, MediaType.APPLICATION_JSON)));
    }

    private ProcessaNotasEdiItemDto gerarProcessaNotasEdiItemDto
    (List<Long> listIdsNotasFiscaisEdiInformada, Long nrProcessamento) {
        ProcessaNotasEdiItemDto processaNotasEdiItemDto = new ProcessaNotasEdiItemDto();
        processaNotasEdiItemDto.setListIdsNotasFiscaisEdiInformada(listIdsNotasFiscaisEdiInformada);
        processaNotasEdiItemDto.setNrProcessamento(nrProcessamento);

        return processaNotasEdiItemDto;
    }

    private void enviarProcessaNotasEdiItem(List<ProcessaNotasEdiItemDto> processaNotasEdiItemDtos) throws IOException {
        ProcessaNotasEdiItemDto processaNotasEdiItemDto = null;
        IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.PROCESSAMENTO_NOTAS_EDI_ITEM);

        processaNotasEdiItemDto = processaNotasEdiItemDtos.get(0);
        storeNotasEdiPosProc(processaNotasEdiItemDtos);
        jmsMessageSender.addMsg(processaNotasEdiItemDto);
        integracaoJmsService.storeMessage(jmsMessageSender);
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
        this.tipoTributacaoIEService = tipoTributacaoIEService;
    }

    public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
        this.divisaoClienteService = divisaoClienteService;
    }

    public ContingenciaService getContingenciaService() {
        return contingenciaService;
    }

    public void setContingenciaService(ContingenciaService contingenciaService) {
        this.contingenciaService = contingenciaService;
    }

    public void setClienteOperadorLogisticoService(ClienteOperadorLogisticoService clienteOperadorLogisticoService) {
        this.clienteOperadorLogisticoService = clienteOperadorLogisticoService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    public void setLogEDIDetalheService(LogEDIDetalheService logEDIDetalheService) {
        this.logEDIDetalheService = logEDIDetalheService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setNotaFiscalExpedicaoEDIService(NotaFiscalExpedicaoEDIService notaFiscalExpedicaoEDIService) {
        this.notaFiscalExpedicaoEDIService = notaFiscalExpedicaoEDIService;
    }

    public NotaFiscalEDIService getNotaFiscalEDIService() {
        return notaFiscalEDIService;
    }

    public void setNotaFiscalEDIService(NotaFiscalEDIService notaFiscalEDIService) {
        this.notaFiscalEDIService = notaFiscalEDIService;
    }

    public void setProcessarNotaFiscalEDIService(ProcessarNotaFiscalEDIService processarNotaFiscalEDIService) {
        this.processarNotaFiscalEDIService = processarNotaFiscalEDIService;
    }

    public void setProcessarNotasEDICommonService(ProcessarNotasEDICommonService processarNotasEDICommonService) {
        this.processarNotasEDICommonService = processarNotasEDICommonService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setCceItemService(CCEItemService cceItemService) {
        this.cceItemService = cceItemService;
    }

    public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
        this.integracaoJmsService = integracaoJmsService;
    }

    public LogAtualizacaoEDIService getLogAtualizacaoEDIService() {
        return logAtualizacaoEDIService;
    }

    public void setLogAtualizacaoEDIService(LogAtualizacaoEDIService logAtualizacaoEDIService) {
        this.logAtualizacaoEDIService = logAtualizacaoEDIService;
    }

    public ConhecimentoService getConhecimentoService() {
        return conhecimentoService;
    }

    public void setConhecimentoService(ConhecimentoService conhecimentoService) {
        this.conhecimentoService = conhecimentoService;
    }

    public MonitoramentoDescargaService getMonitoramentoDescargaService() {
        return monitoramentoDescargaService;
    }

    public void setMonitoramentoDescargaService(MonitoramentoDescargaService monitoramentoDescargaService) {
        this.monitoramentoDescargaService = monitoramentoDescargaService;
    }

    public DoctoServicoService getDoctoServicoService() {
        return doctoServicoService;
    }

    public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
        this.doctoServicoService = doctoServicoService;
    }

    public LogErrosEDIService getLogErrosEDIService() {
        return logErrosEDIService;
    }

    public void setLogErrosEDIService(LogErrosEDIService logErrosEDIService) {
        this.logErrosEDIService = logErrosEDIService;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BusinessException.class, Exception.class})
    protected void finalizaConhecimentoSemPesagem(Conhecimento conhecimento, Boolean blContingencia) throws Exception {
        try {
            monitoramentoDescargaService.executeFechaConhecimento(conhecimento, blContingencia);
            doctoServicoService.executeValidacoesParaBloqueioValores(conhecimento.getIdDoctoServico());
        } catch (BusinessException be) {
            throw new BusinessException(be.getMessageKey(), be.getMessageArguments(), be);
        } catch (Throwable e) {
            throw e;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, noRollbackFor = {BusinessException.class, Exception.class})
    public void finalizaConhecimentoSemPesagem(ConhecimentoSemPesagemDto conhecimentoSemPesagemDto) throws Exception {
        Conhecimento conhecimento = conhecimentoService.findById(conhecimentoSemPesagemDto.getIdConhecimento());
        ProcessaNotasEdiItemDto processaNotasEdiItemDto = conhecimentoSemPesagemDto.getProcessaNotasEdiItem();
        Usuario usuario = addSessionUser(processaNotasEdiItemDto.getIdUsuario());
        SessionContext.set(SessionKey.FILIAL_KEY, filialService.findById(processaNotasEdiItemDto.getIdFilial()));
        SessionContext.set(SessionKey.EMPRESA_KEY, notaFiscalEDIService.getEmpresaService().findEmpresaPadraoByUsuario(usuario));

        Empresa empresaPadrao = notaFiscalEDIService.getEmpresaService().findEmpresaPadraoByUsuario(SessionUtils.getUsuarioLogado());
        SessionContext.set(SessionKey.PAIS_KEY, notaFiscalEDIService.getPaisService().findPaisByUsuarioEmpresa(usuario, empresaPadrao));
        try {
            finalizaConhecimentoSemPesagem(conhecimento, conhecimentoSemPesagemDto.getBlContingencia());

            if (BooleanUtils.isTrue(processaNotasEdiItemDto.getFinalizou())) {
                IntegracaoJmsService.JmsMessageSender jmsMessageSender = integracaoJmsService
                    .createMessage(Queues.FINALIZA_PROCESSAMENTO_EDI, processaNotasEdiItemDto);
                integracaoJmsService.storeMessage(jmsMessageSender);
            }
        } catch (BusinessException be) {
            String mensagem = MessageFormat
                .format(processarNotaFiscalEDIService.montarMessagemErroExcessao(be), be.getMessageArguments());
            sendMessageQueueErro
                (
                    processaNotasEdiItemDto.getIdProcessamentoEdi(), processaNotasEdiItemDto.getInicioIndex(),
                    processaNotasEdiItemDto.getFinalIndex(), mensagem
                );
            processarNotaFiscalEDIService
                .enviarListaPacialParaRemover(conhecimentoSemPesagemDto.getProcessaNotasEdiItem());
            throw processarNotaFiscalEDIService
                .lancarBusinessException
                    (
                        processaNotasEdiItemDto.getNrProcessamento(),
                        processaNotasEdiItemDto.getIdProcessamentoEdi(), be
                    );
        } catch (Exception e) {
            String mensagem = processarNotaFiscalEDIService.montarMessagemErroExcessao(e);
            sendMessageQueueErro
                (
                    processaNotasEdiItemDto.getIdProcessamentoEdi(), processaNotasEdiItemDto.getInicioIndex(),
                    processaNotasEdiItemDto.getFinalIndex(), mensagem
                );
            processarNotaFiscalEDIService
                .enviarListaPacialParaRemover(conhecimentoSemPesagemDto.getProcessaNotasEdiItem());
            throw new Exception(e.getMessage(), e);
        } finally {
            SessionContext.remove(SessionKey.FILIAL_KEY);
            SessionContext.remove(SessionKey.EMPRESA_KEY);
            SessionContext.remove("adsm.session.authenticatedUser");
        }

    }

    private Usuario addSessionUser(Long idUsuario) {
        Usuario usuario = notaFiscalEDIService.getUsuarioService().findById(idUsuario);
        SessionContext.setUser(usuario);
        return usuario;
    }

    private void sendMessageQueueErro(Long idProcessamentoEdi, Long inicioIndex, Long finalIndex, String mensagem) {

        List<ProcessamentoNotaEdi> processamentoNotaEdis = processarNotaFiscalEDIService.getProcessamentoNotaEdiService()
                .findByIdProcessamentoEdiAndNrIndexAndDsMensagemErroIsNull(idProcessamentoEdi, inicioIndex, finalIndex);
        if (processamentoNotaEdis.isEmpty()) return;

        IntegracaoJmsService.JmsMessageSender messageSender = integracaoJmsService.createMessage(Queues.STORE_LOG_EDI);

        processamentoNotaEdis.forEach(pne ->
                processarNotaFiscalEDIService.carregarMessageSenderStoreLogEdiDto
                        (idProcessamentoEdi, Long.valueOf(pne.getNrNotaFiscal()), mensagem, messageSender)
        );
        integracaoJmsService.storeMessage(messageSender);
    }

    @Transactional
    public void removeRegistrosProcessados(ProcessaNotasEdiItemDto processaNotasEdiItemDto) {
        processarNotaFiscalEDIService
                .removeRegistrosProcessados(processaNotasEdiItemDto.getListIdsNotasFiscaisEdiInformada());
    }

    public DocumentoPorNotaFiscalDto
    executeValidatePendenciaAtualizacao(FiltroProcessoPorNotaFiscalDto requestFiltroProcessoPorNotaFiscalDTO) {
        return notaFiscalEDIService.executeValidatePendenciaAtualizacao(requestFiltroProcessoPorNotaFiscalDTO);
    }

    public ResponsavelEtiquetaDto validateResponsavelEtiquetaPorVolume
            (ValidateResponsavelEtiquetaDto validateResponsavelEtiquetaDto) {
        return notaFiscalEDIService.validateResponsavelEtiquetaPorVolume(validateResponsavelEtiquetaDto);
    }

    public ResponsavelEtiquetaDto validateResponsavelPaleteFechado
            (ValidateResponsavelEtiquetaDto validateResponsavelEtiquetaDto) {

        return notaFiscalEDIService.validateResponsavelPaleteFechado(validateResponsavelEtiquetaDto);
    }

    public Map<String, Object> validateClienteSemIntervaloEtiqueta(Long idCliente){
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("idCliente", idCliente);
        result.put("informar",processarNotaFiscalEDIService.validateIntervaloEtiqueta(criteria));
        return result;
    }

    public List<DocumentoPorNotaFiscalDto>
    executeValidatePendenciaAtualizacaoIntervaloNota(FiltroProcessoPorNotaFiscalDto requestFiltroProcessoPorNotaFiscalDTO) {
        return notaFiscalEDIService.executeValidatePendenciaAtualizacaoIntervaloNota(requestFiltroProcessoPorNotaFiscalDTO);
    }

    public ConhecimentoNormalService getConhecimentoNormalService() {
        return conhecimentoNormalService;
    }

    public void setConhecimentoNormalService(ConhecimentoNormalService conhecimentoNormalService) {
        this.conhecimentoNormalService = conhecimentoNormalService;
    }

    public ProcessamentoIbEdiService getProcessamentoIbEdiService() {
        return processamentoIbEdiService;
    }

    public void setProcessamentoIbEdiService(ProcessamentoIbEdiService processamentoIbEdiService) {
        this.processamentoIbEdiService = processamentoIbEdiService;
    }

    public void setIntegracaoJwtService(IntegracaoJwtService integracaoJwtService) {
        this.integracaoJwtService = integracaoJwtService;
    }

    public void setPaisService(PaisService paisService) {
        this.paisService = paisService;
    }

    public void setMoedaService(MoedaService moedaService) {
        this.moedaService = moedaService;
    }

    public void setHistoricoFilialService(HistoricoFilialService historicoFilialService) {
        this.historicoFilialService = historicoFilialService;
    }

    public void setProcessamentoFilaIbAgruparService(ProcessamentoFilaIbAgruparService processamentoFilaIbAgruparService) {
        this.processamentoFilaIbAgruparService = processamentoFilaIbAgruparService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

}
