package com.mercurio.lms.expedicao.swt.action;

import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CLIENTE_CONSIGNATARIO_IN_SESSION;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CLIENTE_REDESPACHO_IN_SESSION;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.CLIENTE_RESPONSAVELFRETE_IN_SESSION;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.IE_CONSIGNATARIO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.IE_REDESPACHO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.IE_RESPONSAVEL_FRETE;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.MODAL_AEREO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.TP_DEVEDOR_CONSIGNATARIO;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.TP_DEVEDOR_OUTRO_RESPONSAVEL;
import static com.mercurio.lms.expedicao.util.ConstantesExpedicao.TP_DEVEDOR_REDESPACHO;
import static com.mercurio.lms.util.IntegerUtils.ZERO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.coleta.model.DetalheColeta;
import com.mercurio.lms.coleta.model.service.DetalheColetaService;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TipoTributacaoIEService;
import com.mercurio.lms.configuracoes.util.MapUtilsPlus;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DadosComplemento;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.InformacaoDocServico;
import com.mercurio.lms.expedicao.model.service.ContingenciaService;
import com.mercurio.lms.expedicao.model.service.DigitarDadosNotaNormalService;
import com.mercurio.lms.expedicao.model.service.InformacaoDocServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.tributos.model.TipoTributacaoIE;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.ClienteOperadorLogistico;
import com.mercurio.lms.vendas.model.service.ClienteOperadorLogisticoService;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * @author Luis Carlos Poletto
 *
 *         Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para
 *         referenciar este serviço.
 * @spring.bean id="lms.expedicao.swt.digitarDadosNotaNormalAction"
 */
public class DigitarDadosNotaNormalAction extends DigitarNotaAction {

    private static final String REDESPACHO = "IE_REDESPACHO";
	private static final String TIPO_EMISSAO_DOC_CLIENTE = "E";

    private PedidoColetaService pedidoColetaService;
    private DetalheColetaService detalheColetaService;
    private TipoTributacaoIEService tipoTributacaoIEService;
    private InformacaoDoctoClienteService informacaoDoctoClienteService;
    private InformacaoDocServicoService informacaoDocServicoService;
    private ParametroGeralService parametroGeralService;
    private DigitarDadosNotaNormalService digitarDadosNotaNormalService;
    private ClienteService clienteService;
    private ClienteOperadorLogisticoService clienteOperadorLogisticoService;
    private ContingenciaService contingenciaService;
    private InscricaoEstadualService inscricaoEstadualService;
    private ConteudoParametroFilialService conteudoParametroFilialService;

    @SuppressWarnings("rawtypes")
    public Map findLookupNrColeta(Map<String, Object> parameters) {
        Long idFilialResponsavel = (Long) parameters.get("idFilialResponsavelPedidoColeta");
        Long nrColeta = (Long) parameters.get("nrColeta");
        return pedidoColetaService.findColetaNaoFinalizadaByFilialNrColeta(idFilialResponsavel, nrColeta);
    }

    /**
     * Carrega Dados necessários do <b>Cliente Remetente</b>
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findDadosRemetente(Map<String, Object> criteria) {
        List<Map<String, Object>> result = findCliente((Long) criteria.get("idCliente"),
                (String) criteria.get("nrIdentificacaoRemetente"));
        if (!result.isEmpty()) {
            Map<String, Object> cliente = (Map<String, Object>) result.get(0);
            Map<String, Object> pessoa = (Map<String, Object>) cliente.remove("pessoa");
            cliente.put("blPermiteCte", cliente.get("blPermiteCte"));
            cliente.put("nrIdentificacao", pessoa.get("nrIdentificacao"));
            cliente.put("nmPessoa", pessoa.get("nmPessoa"));
            cliente.put("tpCliente", ((Map<String, Object>) cliente.remove("tpCliente")).get("value"));
            Map<String, Object> endereco = (Map<String, Object>) pessoa.get("endereco");
            endereco.put("dsEndereco", endereco.get("dsTipoLogradouro") + " " + endereco.get("dsEndereco"));
            cliente.putAll(endereco);
			if(Boolean.TRUE.equals(cliente.containsKey("tpEmissaoDoc"))){
            cliente.put("tpEmissaoDoc", ((Map<String, Object>) cliente.remove("tpEmissaoDoc")).get("value") );        
            }
            Long idCliente = (Long) cliente.get("idCliente");
            String tpModal = (String) criteria.get("tpModalServico");
            String tpAbrangencia = (String) criteria.get("tpAbrangenciaServico");
            String tpCliente = (String) cliente.get("tpCliente");
            if (MODAL_AEREO.equals(tpModal)) {
                cliente.putAll(findAeroportoOrigemByFilial());
            }
            cliente.put("pedidoColeta", findPedidoColeta(idCliente));
            cliente.put("cotacao", findCotacao(idCliente, (String) criteria.get("tpDocumento")));
            Integer nrRowCountInformacaoDoctoCliente = ZERO;
            if (ClienteUtils.isParametroClienteEspecial(tpCliente)) {
                nrRowCountInformacaoDoctoCliente = informacaoDoctoClienteService
                        .getRowCountByClienteTpModalTpAbrangenciaByBlRemetente(idCliente, tpModal, tpAbrangencia,
                                Boolean.TRUE);
            }
            cliente.put("nrRowCountInformacaoDoctoCliente", nrRowCountInformacaoDoctoCliente);
			String tpEmissaoDoc = MapUtilsPlus.getString(cliente, "tpEmissaoDoc");
            String tpConhecimento = MapUtilsPlus.getString(criteria, "tpConhecimento");
			if (tpConhecimento == null && (TIPO_EMISSAO_DOC_CLIENTE).equals(tpEmissaoDoc)) {
            	tpConhecimento = ConstantesExpedicao.CONHECIMENTO_NORMAL;
            }
			
            if (canNotDigitarManualmente(cliente, tpConhecimento)) {
                throw new BusinessException("LMS-04501");
            }
        }
        return result;
    }

    /**
     * Carrega Dados necessários do <b>Cliente Destinatário</b>
     * 
     * @param criteria
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> findDadosDestinatario(Map<String, Object> criteria) {
        List<Map<String, Object>> result = findCliente((Long) criteria.get("idCliente"),
                (String) criteria.get("nrIdentificacaoDestinatario"));
        if (!result.isEmpty()) {
            Map<String, Object> cliente = (Map<String, Object>) result.get(0);
            Map<String, Object> pessoa = (Map<String, Object>) cliente.remove("pessoa");
            cliente.put("nrIdentificacao", pessoa.get("nrIdentificacao"));
            cliente.put("nmPessoa", pessoa.get("nmPessoa"));
            cliente.put("tpCliente", ((Map<String, Object>) cliente.remove("tpCliente")).get("value"));
            Map<String, Object> endereco = (Map<String, Object>) pessoa.get("endereco");
            endereco.put("dsEndereco", endereco.get("dsTipoLogradouro") + " " + endereco.get("dsEndereco"));
            cliente.putAll(endereco);

            if (MODAL_AEREO.equals((String) criteria.get("tpModalServico"))) {
                TypedFlatMap consult = new TypedFlatMap();

                consult.put("idCliente", cliente.get("idCliente"));
                consult.put("idMunicipio", endereco.get("idMunicipio"));
                consult.put("nrCep", endereco.get("nrCep"));
                consult.put("idServico", criteria.get("idServico"));
                cliente.putAll(findAeroportoDestinoByFilial(consult));
            }

            Long idCliente = (Long) cliente.get("idCliente");
            String tpModal = (String) criteria.get("tpModalServico");
            String tpAbrangencia = (String) criteria.get("tpAbrangenciaServico");
            String tpCliente = (String) cliente.get("tpCliente");

            Integer nrRowCountInformacaoDoctoCliente = ZERO;
            if (ClienteUtils.isParametroClienteEspecial(tpCliente)) {
                nrRowCountInformacaoDoctoCliente = informacaoDoctoClienteService
                        .getRowCountByClienteTpModalTpAbrangenciaByBlDestinatario(idCliente, tpModal, tpAbrangencia,
                                Boolean.TRUE);
            }
            cliente.put("nrRowCountInformacaoDoctoCliente", nrRowCountInformacaoDoctoCliente);

        }
        return result;
    }

    @SuppressWarnings({ "unchecked" })
    public List<Map<String, Object>> findDadosDevedor(Map<String, Object> criteria) {
        List<Map<String, Object>> result = findCliente((Long) criteria.get("idCliente"),
                (String) criteria.get("nrIdentificacaoRemetente"));
        if (!result.isEmpty()) {
            Map<String, Object> cliente = (Map<String, Object>) result.get(0);
            Map<String, Object> pessoa = (Map<String, Object>) cliente.remove("pessoa");
            cliente.put("blPermiteCte", pessoa.get("blPermiteCte"));
            cliente.put("nrIdentificacao", pessoa.get("nrIdentificacao"));
            String nrIdentificacao = cliente.get("nrIdentificacao") == null ? null
                    : cliente.get("nrIdentificacao").toString().replaceAll("[^0-9]", "");
            if (StringUtils.isNotBlank(nrIdentificacao) && isConsignatario(criteria.get("tpModalServico"))
                    && invalidoCnpjConsignatario(nrIdentificacao)) {
                throw new BusinessException("LMS-04381");
            }

            cliente.put("nmPessoa", pessoa.get("nmPessoa"));
            cliente.put("tpCliente", ((Map<String, Object>) cliente.remove("tpCliente")).get("value"));
            Map<String, Object> endereco = (Map<String, Object>) pessoa.get("endereco");
            endereco.put("dsEndereco", endereco.get("dsTipoLogradouro") + " " + endereco.get("dsEndereco"));
            cliente.putAll(endereco);

            Long idCliente = (Long) cliente.get("idCliente");
            String tpModal = (String) criteria.get("tpModalServico");
            String tpAbrangencia = (String) criteria.get("tpAbrangenciaServico");
            String tpCliente = (String) cliente.get("tpCliente");
            if (MODAL_AEREO.equals(tpModal)) {
                cliente.putAll(findAeroportoOrigemByFilial());
            }
            cliente.put("pedidoColeta", findPedidoColeta(idCliente));
            cliente.put("cotacao", findCotacao(idCliente, (String) criteria.get("tpDocumento")));
            Integer nrRowCountInformacaoDoctoCliente = ZERO;
            if (ClienteUtils.isParametroClienteEspecial(tpCliente)) {
                nrRowCountInformacaoDoctoCliente = informacaoDoctoClienteService
                        .getRowCountByClienteTpModalTpAbrangenciaByBlDevedor(idCliente, tpModal, tpAbrangencia,
                                Boolean.TRUE);
            }
            cliente.put("nrRowCountInformacaoDoctoCliente", nrRowCountInformacaoDoctoCliente);
        }
        return result;
    }

    private boolean canNotDigitarManualmente(Map<String, Object> cliente, String tpConhecimento) {
        return !canDigitarManualmente(cliente, tpConhecimento);
    }

    private boolean canDigitarManualmente(Map<String, Object> cliente, String tpConhecimento) {
        if (cliente != null) {
            if (ConstantesExpedicao.CONHECIMENTO_NORMAL.equals(tpConhecimento)) {
                if (MapUtilsPlus.getDomainValue(cliente, "tpEmissaoDoc") != null) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isConsignatario(Object tpDevedor) {
        return tpDevedor != null && "C".equals(tpDevedor.toString()) ? true : false;
    }

    private boolean invalidoCnpjConsignatario(String nrIdentificacao) {
        String cnpjAracatuba = parametroGeralService.findSimpleConteudoByNomeParametro("NR_CNPJ_MATRIZ_ARACATUBA");
        String cnpjMercurio = parametroGeralService.findSimpleConteudoByNomeParametro("NR_CNPJ_MERCURIO");
        return cnpjMercurio.equals(nrIdentificacao.substring(0, 8))
                || cnpjAracatuba.substring(0, 8).equals(nrIdentificacao.substring(0, 8)) ? true : false;
    }

    @SuppressWarnings("rawtypes")
    public Map storePreCTRCInSession(Map parameters) {
        /** Validacao de Formulario */
        DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
        DateTime dhChegada = (DateTime) parameters.get("dhChegada");

        if (dhChegada != null && parameters.get("nrFrota") != null && parameters.get("nrIdentificador") != null) {
            if (sysDate.compareTo(dhChegada) < 0) {
                throw new BusinessException("LMS-00074");
            }

            if (findMinimoDataColeta().compareTo(dhChegada) > 0) {
                throw new BusinessException("LMS-04250");
            }

        }

        BigDecimal id = (BigDecimal) parametroGeralService.findConteudoByNomeParametro("ID_ARACATUBA", false);
        Long idAracatuba = id != null ? id.longValue() : null;
        Map dadosAracatuba = empresaService.findDadosEmpresaById(idAracatuba);

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("nrIdentificacao", FormatUtils.formatIdentificacao(
                (DomainValue) dadosAracatuba.get("tpIdentificacao"), (String) dadosAracatuba.get("nrIdentificacao")));
        result.put("nmPessoa", dadosAracatuba.get("nmPessoa"));
        result.put("idAracatuba", idAracatuba);
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map findMinimoDataColeta(Map criteria) {

        Map retorno = new TypedFlatMap();
        retorno.put("dhChegada", findMinimoDataColeta());

        return retorno;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map findResponsavelPaleteFechado(Map<String, Object> criteria) {
        Map map = new HashMap();
        Cliente clienteRemetente = null;
        InscricaoEstadual ieRemetente = null;
        Cliente clienteDestinatario = null;
        InscricaoEstadual ieDestinatario = null;
        if (criteria.get("nrIdentificacaoClienteRemetente") == null) {
            if ("R".equals(criteria.get("tpDevedorFrete")))
                throw new BusinessException("LMS-04060");
        } else {
            clienteRemetente = (Cliente) clienteService.findByNrIdentificacao(
                    PessoaUtils.clearIdentificacao(criteria.get("nrIdentificacaoClienteRemetente").toString()));
            if (clienteRemetente != null)
                ieRemetente = inscricaoEstadualService.findByPessoaIndicadorPadrao(clienteRemetente.getIdCliente(),
                        true);
        }
        if (criteria.get("nrIdentificacaoClienteDestinatario") == null) {
            if ("D".equals(criteria.get("tpDevedorFrete")))
                throw new BusinessException("LMS-04060");
        } else {
            clienteDestinatario = (Cliente) clienteService.findByNrIdentificacao(
                    PessoaUtils.clearIdentificacao(criteria.get("nrIdentificacaoClienteDestinatario").toString()));
            if (clienteDestinatario != null)
                ieDestinatario = inscricaoEstadualService
                        .findByPessoaIndicadorPadrao(clienteDestinatario.getIdCliente(), true);
        }
        Cliente clienteConsignatario = criteria.get("idClienteConsignatario") != null
                ? clienteService.findByIdComPessoa((Long) criteria.get("idClienteConsignatario")) : null;
        Cliente clienteRedespacho = criteria.get("idClienteRedespacho") != null
                ? clienteService.findByIdComPessoa((Long) criteria.get("idClienteRedespacho")) : null;
        Cliente clienteOutroResponsavel = criteria.get("idClienteOutroResponsavel") != null
                ? clienteService.findByIdComPessoa((Long) criteria.get("idClienteOutroResponsavel")) : null;

        Conhecimento conhecimento = new Conhecimento();
        conhecimento.setTpDevedorFrete(new DomainValue((String) criteria.get("tpDevedorFrete")));
        conhecimento.setClienteByIdClienteRemetente(clienteRemetente);
        conhecimento.setInscricaoEstadualRemetente(ieRemetente);
        conhecimento.setClienteByIdClienteDestinatario(clienteDestinatario);
        conhecimento.setInscricaoEstadualDestinatario(ieDestinatario);
        conhecimento.setClienteByIdClienteConsignatario(clienteConsignatario);
        conhecimento.setClienteByIdClienteRedespacho(clienteRedespacho);

        if (clienteConsignatario != null) {
            List<DadosComplemento> listDadosComplemento;
            listDadosComplemento = new ArrayList<DadosComplemento>();
            InscricaoEstadual inscricaoEstadual = inscricaoEstadualService
                    .findByPessoaIndicadorPadrao(clienteConsignatario.getIdCliente(), true);
            InformacaoDocServico informacaoDocServico = new InformacaoDocServico();
            informacaoDocServico.setDsCampo(IE_CONSIGNATARIO);
            DadosComplemento dadosComplemento = new DadosComplemento();
            dadosComplemento.setDsValorCampo(inscricaoEstadual.getNrInscricaoEstadual());
            dadosComplemento.setInformacaoDocServico(informacaoDocServico);
            listDadosComplemento.add(dadosComplemento);
            conhecimento.setDadosComplementos(listDadosComplemento);
        }
        if (clienteRedespacho != null) {
            List<DadosComplemento> listDadosComplemento = conhecimento.getDadosComplementos();
            if (listDadosComplemento == null)
                listDadosComplemento = new ArrayList<DadosComplemento>();
            InscricaoEstadual inscricaoEstadual = inscricaoEstadualService
                    .findByPessoaIndicadorPadrao(clienteRedespacho.getIdCliente(), true);
            InformacaoDocServico informacaoDocServico = new InformacaoDocServico();
            informacaoDocServico.setDsCampo(IE_REDESPACHO);
            DadosComplemento dadosComplemento = new DadosComplemento();
            dadosComplemento.setDsValorCampo(inscricaoEstadual.getNrInscricaoEstadual());
            dadosComplemento.setInformacaoDocServico(informacaoDocServico);
            listDadosComplemento.add(dadosComplemento);
            conhecimento.setDadosComplementos(listDadosComplemento);
        }
        List<DevedorDocServ> devedores = null;
        if (clienteOutroResponsavel != null) {
            devedores = new ArrayList<DevedorDocServ>(1);
            DevedorDocServ devedor = new DevedorDocServ();
            devedor.setCliente(clienteOutroResponsavel);
            InscricaoEstadual inscricaoEstadual = inscricaoEstadualService
                    .findByPessoaIndicadorPadrao(clienteOutroResponsavel.getIdCliente(), true);
            devedor.setInscricaoEstadual(inscricaoEstadual);
            devedores.add(devedor);
        }
        conhecimento.setDevedorDocServs(devedores);
        if (!digitarNotaService.findResponsavelPaleteFechado(conhecimento))
            throw new BusinessException("LMS-04405");

        map.put("conhecimentoTmp", conhecimento);
        return map;
    }

    /**
     * Validar para que a data/hora informada não esteja menor que a <br>
     * data/hora atuais menos a quantidade de horas informada no<br>
     * parâmetro geral TMP_MINIMO_DATA_COLETA, se estiver<br>
     * visualizar a mensagem <strong>LMS-04250</strong>, não aceitando a informação.
     * 
     * @param dhChegada
     * @return Boolean
     */
    private DateTime findMinimoDataColeta() {
        DateTime sysDate = JTDateTimeUtils.getDataHoraAtual();
        BigDecimal tmpMinimoDataColeta = (BigDecimal) parametroGeralService
                .findConteudoByNomeParametro("TMP_MINIMO_DATA_COLETA", false);
        if (tmpMinimoDataColeta != null) {
            sysDate = sysDate.minusHours(tmpMinimoDataColeta.intValue());
        }

        return sysDate;
    }

    /**
     * Grava Cliente na Sessao
     * 
     * @param parameters
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map storeClienteInSession(Map parameters) {
        Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
        Map<String, Object> clientesSession = (Map) parameters.get("clientesSession");
        parameters.remove("clientesSession");
        String tpDevedor = (String) parameters.get("tpDevedor");
        if (TP_DEVEDOR_REDESPACHO.equals(tpDevedor)) {
            Cliente cliente = storeInSession(parameters, IE_REDESPACHO, CLIENTE_REDESPACHO_IN_SESSION, conhecimento);
            if (parameters == null || parameters.size() <= 0) {
                clientesSession.remove(CLIENTE_REDESPACHO_IN_SESSION);
            } else {
                clientesSession.put(CLIENTE_REDESPACHO_IN_SESSION, parameters);
            }
            conhecimento.setClienteByIdClienteRedespacho(cliente);
        } else if (TP_DEVEDOR_CONSIGNATARIO.equals(tpDevedor)) {
            Cliente cliente = storeInSession(parameters, IE_CONSIGNATARIO, CLIENTE_CONSIGNATARIO_IN_SESSION,
                    conhecimento);
            if (parameters == null || parameters.size() <= 0) {
                clientesSession.remove(CLIENTE_CONSIGNATARIO_IN_SESSION);
            } else {
                clientesSession.put(CLIENTE_CONSIGNATARIO_IN_SESSION, parameters);
            }
            conhecimento.setClienteByIdClienteConsignatario(cliente);
        } else if (TP_DEVEDOR_OUTRO_RESPONSAVEL.equals(tpDevedor)) {
            Cliente cliente = storeInSession(parameters, IE_RESPONSAVEL_FRETE, CLIENTE_RESPONSAVELFRETE_IN_SESSION,
                    conhecimento);
            if (parameters == null || parameters.size() <= 0) {
                clientesSession.remove(CLIENTE_RESPONSAVELFRETE_IN_SESSION);
            } else {
                clientesSession.put(CLIENTE_RESPONSAVELFRETE_IN_SESSION, parameters);
            }
            List devedores = null;
            if (cliente != null) {
                devedores = new ArrayList(1);
                DevedorDocServ devedor = new DevedorDocServ();
                devedor.setCliente(cliente);
                InscricaoEstadual inscricaoEstadual = new InscricaoEstadual();
                inscricaoEstadual.setIdInscricaoEstadual((Long) parameters.get("idInscricaoEstadual"));
                devedor.setInscricaoEstadual(inscricaoEstadual);
                devedores.add(devedor);
            }
            conhecimento.setDevedorDocServs(devedores);
        }
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("conhecimento", conhecimento);
        result.put("clientesSession", clientesSession);
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map storeSubcontratacaoInSession(Map parameters) {
        Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
        Map subcontratacao = (Map) parameters.get("subcontratacao_complemento");

        conhecimento.addDadoComplemento(createDadosComplemento("NR_SERIE_ANTERIOR",
                subcontratacao == null ? null : (String) subcontratacao.get("serieNroDoc")));
        conhecimento.addDadoComplemento(createDadosComplemento("NR_DOCUMENTO_ANTERIOR",
                subcontratacao == null ? null : (String) subcontratacao.get("nroDoc")));
        conhecimento.addDadoComplemento(createDadosComplemento("TP_DOCUMENTO_ANTERIOR",
                subcontratacao == null ? null : (String) subcontratacao.get("tipoDocumento")));
        conhecimento.addDadoComplemento(createDadosComplemento("DT_EMISSAO_ANTERIOR",
                subcontratacao == null ? null : (String) subcontratacao.get("dataEmissao")));
        conhecimento.addDadoComplemento(createDadosComplemento("NR_CHAVE_DOCUMENTO_ANTERIOR",
                subcontratacao == null ? null : (String) subcontratacao.get("chaveCte")));

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("conhecimento", conhecimento);
        Map<String, Object> clientesSession = (Map) parameters.get("clientesSession");
        result.put("clientesSession", clientesSession);
        result.put("subcontratacao_complemento", subcontratacao);
        return result;
    }

    /**
     * Busca Cliente <b>Consignatário</b> da Sessão
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map findClienteFromSession(Map parameters) {
        Map data = (Map) parameters.get("cliente");
        Long idCliente = (Long) data.get("idCliente");
        data.put("inscricaoEstadual", findInscricaoEstadual(idCliente));
        return data;
    }

    /**
     * Carrega e Valida Dados <b>Primeira Fase</b>
     * 
     * @param parameters
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map executeCtrcPrimeiraFase(Map parameters) {
        try {
            Map map = digitarDadosNotaNormalService.executeCtrcPrimeiraFase(parameters);
            return map;
        } catch (BusinessException businessException) {
            parameters.put("exception", businessException);

            // CQPRO00030818
            BigDecimal limite = (BigDecimal) configuracoesFacade
                    .getValorParametro(ConstantesExpedicao.NM_PARAMETRO_LIMITE_PESO_CTRC);
            String limiteFormatado = FormatUtils.formatDecimal("#,###,##0.00", limite);

            Conhecimento conhecimento = (Conhecimento) parameters.get("conhecimento");
            String valorPesoAforado = FormatUtils.formatDecimal("#,###,##0.00",
                    conhecimento.getPsAforado() != null ? conhecimento.getPsAforado() : 0);
            String valorPesoReal = FormatUtils.formatDecimal("#,###,##0.00",
                    conhecimento.getPsReal() != null ? conhecimento.getPsReal() : 0);

            parameters.put("limite_formatado", limiteFormatado);
            parameters.put("valor_peso_aforado", valorPesoAforado);
            parameters.put("valor_peso_real", valorPesoReal);

            return parameters;
        } catch (Exception exception) {
            parameters.put("exception", exception);
            return parameters;
        }
    }

    /**
     * Validações <b>Segunda Fase</b>
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map executeCtrcSegundaFase(Map parameters) {
        try {
            return digitarDadosNotaNormalService.executeCtrcSegundaFase(parameters);
        } catch (BusinessException businessException) {
            parameters.put("exception", businessException);
            return parameters;
        } catch (Exception exception) {
            parameters.put("exception", exception);
            return parameters;
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List<Map<String, Object>> findLookupFilial(Map criteria) {
        if (criteria.get("sgFilialResponsavelPedidoColeta") != null) {
            criteria.put("sgFilial", criteria.get("sgFilialResponsavelPedidoColeta"));
        }
        List<Filial> filiais = filialService.findLookup(criteria);
        if (filiais != null) {
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (Filial filial : filiais) {
                Map<String, Object> mapFilial = new HashMap<String, Object>();
                mapFilial.put("sgFilial", filial.getSgFilial());
                mapFilial.put("idFilial", filial.getIdFilial());
                mapFilial.put("idFilialResponsavelPedidoColeta", filial.getIdFilial());
                mapFilial.put("sgFilialResponsavelPedidoColeta", filial.getSgFilial());
                mapFilial.put("nmFantasia", filial.getPessoa().getNmFantasia());
                result.add(mapFilial);
            }
            return result;
        }
        return null;
    }

    /*
     * Metodos privados
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> findCliente(Long idCliente, String nrIdentificacao) {
        List<Map<String, Object>> result = null;
        if (idCliente != null) {
            result = clienteService.findLookupClienteEnderecoValidaDadosBasicos(idCliente);
        } else {
            result = clienteService.findClienteByNrIdentificacaoValidaDadosBasicos(nrIdentificacao);
        }

        if (CollectionUtils.isNotEmpty(result)) {
            Map<String, Object> cliente = (Map<String, Object>) result.get(0);
            Map<String, Object> pessoa = (Map<String, Object>) cliente.get("pessoa");
            pessoa.put("nrIdentificacao", pessoa.remove("nrIdentificacaoFormatado"));
            cliente.put("inscricaoEstadual", findInscricaoEstadual((Long) cliente.get("idCliente")));
        }

        return result;
    }

    private List<Map<String, Object>> findPedidoColeta(Long idCliente) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<ClienteOperadorLogistico> operadores = clienteOperadorLogisticoService.findByIdClienteOperado(idCliente);
        if (operadores != null && operadores.size() > 0) {
            for (ClienteOperadorLogistico clienteOperadorLogistico : operadores) {
                result.addAll(pedidoColetaService
                        .findLookupPedidoColeta(clienteOperadorLogistico.getClienteOperador().getIdCliente()));
            }
        }
        result.addAll(pedidoColetaService.findLookupPedidoColeta(idCliente));

        result.addAll(pedidoColetaService.findPedidoColetaLoja(idCliente));

        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private List findInscricaoEstadual(Long idPessoa) {
        if (idPessoa == null) {
            return null;
        }
        List<TipoTributacaoIE> ies = tipoTributacaoIEService.findVigentesByIdPessoa(idPessoa);
        if (ies != null && !ies.isEmpty()) {
            List<Map> result = new ArrayList<Map>();
            for (TipoTributacaoIE tipoTributacaoIE : ies) {
                Map ie = new HashMap<String, Object>();
                ie.put("idInscricaoEstadual", tipoTributacaoIE.getInscricaoEstadual().getIdInscricaoEstadual());
                ie.put("nrInscricaoEstadual", tipoTributacaoIE.getInscricaoEstadual().getNrInscricaoEstadual());
                ie.put("blIndicadorPadrao", tipoTributacaoIE.getInscricaoEstadual().getBlIndicadorPadrao());
                result.add(ie);
            }
            return result;
        }
        return null;
    }

    /**
     * Manipula Clientes na Sessão
     * 
     * @param parameters
     * @param tpClienteInSession
     * @param dsCampo
     * @param clienteSession
     * @param conhecimento
     * @return
     */
    @SuppressWarnings("rawtypes")
    private Cliente storeInSession(Map parameters, String dsCampo, String clienteSession, Conhecimento conhecimento) {
        conhecimento.removeDadoComplementoByDsCampo(dsCampo);

        addInscEstadualInDadosComplementos(dsCampo, parameters, conhecimento);

        addDadosComplementosRedespacho(dsCampo, parameters, conhecimento);

        conhecimento.setBlRedespachoColeta(parameters.get("blRedespachoColeta") == null ? null : (Boolean)parameters.get("blRedespachoColeta"));
        
        if(BooleanUtils.isTrue(conhecimento.getBlRedespachoColeta()))  {
            conhecimento.addDadoComplemento(createDadosComplemento("NR_CHAVE_DOCUMENTO_ANTERIOR", (String) parameters.get("chaveCteRedespacho")));
        }
        
        Cliente cliente = null;
        Long idCliente = (Long) parameters.get("idCliente");
        if (idCliente == null) {
            parameters.clear();
            SessionContext.remove(clienteSession);
            return cliente;
        }

        cliente = new Cliente();
        cliente.setIdCliente(idCliente);
        cliente.setTpCliente(new DomainValue((String) parameters.get("tpCliente")));

        Pessoa pessoa = new Pessoa();
        pessoa.setNrIdentificacao((String) parameters.get("nrIdentificacao"));
        cliente.setPessoa(pessoa);
        return cliente;
    }

    @SuppressWarnings("rawtypes")
    private void addInscEstadualInDadosComplementos(String dsCampo, Map parameters, Conhecimento conhecimento) {
        if (parameters.get("nrInscricaoEstadual") != null) {
            String nrInscricaoEstadual = parameters.get("nrInscricaoEstadual").toString();
            InformacaoDocServico informacaoDocServico = informacaoDocServicoService.findInformacaoDoctoServico(dsCampo,
                    dsCampo);
            informacaoDocServico.setDsCampo(dsCampo);
            DadosComplemento dadosComplemento = new DadosComplemento();
            dadosComplemento.setInformacaoDocServico(informacaoDocServico);
            dadosComplemento.setDsValorCampo("Selecione...".equals(nrInscricaoEstadual) ? null : nrInscricaoEstadual);
            conhecimento.addDadoComplemento(dadosComplemento);
        }
    }

    @SuppressWarnings("rawtypes")
    private void addDadosComplementosRedespacho(String dsCampo, Map parameters, Conhecimento conhecimento) {
        if (REDESPACHO.equals(dsCampo)) {
            conhecimento.addDadoComplemento(
                    createDadosComplemento("NR_SERIE_ANTERIOR", (String) parameters.get("serieNroDoc")));
            conhecimento.addDadoComplemento(
                    createDadosComplemento("NR_DOCUMENTO_ANTERIOR", (String) parameters.get("nroDoc")));
            conhecimento.addDadoComplemento(
                    createDadosComplemento("TP_DOCUMENTO_ANTERIOR", (String) parameters.get("tipoDocumento")));
            conhecimento.addDadoComplemento(
                    createDadosComplemento("DT_EMISSAO_ANTERIOR", (String) parameters.get("dataEmissao")));
            conhecimento.addDadoComplemento(
                    createDadosComplemento("NR_CHAVE_DOCUMENTO_ANTERIOR", (String) parameters.get("chaveCte")));
        }
    }

    private DadosComplemento createDadosComplemento(String idInfoDocServico, String value) {
        InformacaoDocServico informacaoDocServico = informacaoDocServicoService.findByDsCampo(idInfoDocServico);
        informacaoDocServico.setDsCampo(idInfoDocServico);
        DadosComplemento dadosComplemento = new DadosComplemento();
        dadosComplemento.setInformacaoDocServico(informacaoDocServico);
        dadosComplemento.setDsValorCampo(value);
        return dadosComplemento;
    }

    @SuppressWarnings("rawtypes")
    public void validateContingenciaFilial(Map parameters) {
        contingenciaService.validateContingenciaFilial(SessionUtils.getFilialSessao());
    }

    /***
     * Valida se é necessário validar o FOB, se for necessário é feita a validação.
     * 
     * @author Gabriel.Scossi
     * @since 15/02/2016
     * @param parameters
     * @return Map
     */
    @SuppressWarnings("rawtypes")
    public Map validaFOB(TypedFlatMap parameters) {
        ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService
                .findByNomeParametro(parameters.getLong("idFilial"), "BLQ_FOB_TP_SIT_TRIBU", false, true);
        if (conteudoParametroFilial == null || conteudoParametroFilial.getVlConteudoParametroFilial().equals("S")) {
            parameters.put("validaFOB", tipoTributacaoIEService
                    .validateTipoTributacaoFOB(parameters.getLong("idInscricaoEstadualDestinatario")));
            return parameters;
        }

        return null;

    }

    @SuppressWarnings("unchecked")
    public TypedFlatMap validateModalPedidoColeta(TypedFlatMap parameters) {
        Long idPedidoColeta = parameters.getLong("idPedidoColeta");
        Long idServico = parameters.getLong("idServico");
        String tpConhecimento = parameters.getString("tpConhecimento");

        if (idServico.intValue() == 1 && "NO".equals(tpConhecimento)) {
            List<DetalheColeta> listDetalheColeta = detalheColetaService
                    .findDetalheColetaByIdPedidoColetaIdServico(idPedidoColeta, idServico);
            if (listDetalheColeta == null || listDetalheColeta.size() == 0) {
                parameters.put("confirmar", "LMS-04553");
            }
        }
        return parameters;
    }

    /*
     * Getters e setters
     */
    public void setConteudoParametroFilialService(ConteudoParametroFilialService conteudoParametroFilialService) {
        this.conteudoParametroFilialService = conteudoParametroFilialService;
    }

    public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
        this.pedidoColetaService = pedidoColetaService;
    }

    public void setDetalheColetaService(DetalheColetaService detalheColetaService) {
        this.detalheColetaService = detalheColetaService;
    }

    public void setTipoTributacaoIEService(TipoTributacaoIEService tipoTributacaoIEService) {
        this.tipoTributacaoIEService = tipoTributacaoIEService;
    }

    public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
        this.informacaoDoctoClienteService = informacaoDoctoClienteService;
    }

    public void setInformacaoDocServicoService(InformacaoDocServicoService informacaoDocServicoService) {
        this.informacaoDocServicoService = informacaoDocServicoService;
    }

    public void setParametroGeralService(ParametroGeralService parametroGeralService) {
        this.parametroGeralService = parametroGeralService;
    }

    public void setDigitarDadosNotaNormalService(DigitarDadosNotaNormalService digitarDadosNotaNormalService) {
        this.digitarDadosNotaNormalService = digitarDadosNotaNormalService;
    }

    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    public void setClienteOperadorLogisticoService(ClienteOperadorLogisticoService clienteOperadorLogisticoService) {
        this.clienteOperadorLogisticoService = clienteOperadorLogisticoService;
    }

    public void setContingenciaService(ContingenciaService contingenciaService) {
        this.contingenciaService = contingenciaService;
    }

    public InscricaoEstadualService getInscricaoEstadualService() {
        return inscricaoEstadualService;
    }

    public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
        this.inscricaoEstadualService = inscricaoEstadualService;
    }
}
