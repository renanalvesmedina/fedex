package com.mercurio.lms.vendas.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.TipoLocalizacaoMunicipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.municipios.model.service.TipoLocalizacaoMunicipioService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.Cotacao;
import com.mercurio.lms.vendas.model.LiberacaoEmbarque;
import com.mercurio.lms.vendas.model.dao.LiberacaoEmbarqueDAO;
import com.mercurio.lms.vendas.util.ClienteUtils;
import com.mercurio.lms.workflow.model.Acao;
import com.mercurio.lms.workflow.model.Pendencia;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.CampoHistoricoWorkflow;
import com.mercurio.lms.workflow.model.dto.PendenciaHistoricoDTO.TabelaHistoricoWorkflow;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.HistoricoWorkflowService;
import com.mercurio.lms.workflow.model.service.WorkflowPendenciaService;
import com.mercurio.lms.workflow.util.ConstantesWorkflow;

/**
 * Classe de serviço para CRUD:
 * <p>
 * <p>
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 *
 * @spring.bean id="lms.vendas.liberacaoEmbarqueService"
 */
public class LiberacaoEmbarqueService extends CrudService<LiberacaoEmbarque, Long> {
    private static final String DS_TIPO_LOCALIZACAO_GRANDE_CAPTIAL = "Grande Capital";
    private static final String TP_LOCALIZACAO_OPERACIONAL = "O";
    private TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService;
    private WorkflowPendenciaService workflowPendenciaService;
    private ConfiguracoesFacade configuracoesFacade;
    private AcaoService acaoService;
    private UsuarioService usuarioService;
    private PpeService ppeService;
    private FilialService filialService;
    private HistoricoWorkflowService historicoWorkflowService;

    /**
     * Verifica permissão de usuário logado
     *
     * @return
     * @author Robson Edemar Gehl
     */
    public Boolean validatePermissaoUsuarioLogado(Long id) {
        return usuarioService.verificaAcessoFilialRegionalUsuarioLogado(id);
    }

    /**
     * Recupera uma instância de <code>LiberacaoEmbarque</code> a partir do ID.
     *
     * @param id representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     */
    public LiberacaoEmbarque findById(java.lang.Long id) {
        return (LiberacaoEmbarque) super.findById(id);
    }

    /**
     * Apaga uma entidade através do Id.
     *
     * @param id indica a entidade que deverá ser removida.
     */
    public void removeById(java.lang.Long id) {
        Pendencia pendencia = ((LiberacaoEmbarque) super.findById(id)).getPendencia();
        if (pendencia != null) {
            workflowPendenciaService.cancelPendencia(pendencia);
        }
        super.removeById(id);
    }

    @Override
    protected void beforeRemoveById(Long id) {
        List<Long> ids = new ArrayList<Long>();
        ids.add(id);
        beforeRemoveByIds(ids);
    }

    @Override
    protected void beforeRemoveByIds(List<Long> ids) {
        validateHistoricoWorkflow(ids);
        super.beforeRemoveByIds(ids);
    }

    private void validateHistoricoWorkflow(List<Long> ids) {
        for (Long id : ids) {
            if (historicoWorkflowService.validateHistoricoWorkflow(id, TabelaHistoricoWorkflow.LIBERACAO_EMBARQUE)) {
                LiberacaoEmbarque liberacaoEmbarque = (LiberacaoEmbarque) this.findById(id);
                String nmMunicipio = liberacaoEmbarque.getMunicipio().getNmMunicipio();
                throw new BusinessException("LMS-01251", new String[]{nmMunicipio});
            }
        }
    }

    /**
     * Apaga várias entidades através do Id.
     *
     * @param ids lista com as entidades que deverão ser removida.
     */
    @ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        for (Object o : ids) {
            this.removeById(LongUtils.getLong(o));
        }
    }

    public java.io.Serializable store(LiberacaoEmbarque bean) {

        List<LiberacaoEmbarque> liberacoesEmbarque =
                getLiberacaoEmbarqueDAO().findLiberacoesClienteByMunicipio(bean.getCliente().getIdCliente(), bean.getMunicipio().getIdMunicipio());

        if (liberacoesEmbarque != null) {
            validarArmazenamentoLiberacaoEmbarque(bean, liberacoesEmbarque);
        }

        super.store(bean);
        return bean.getIdLiberacaoEmbarque();
    }

    private void validarArmazenamentoLiberacaoEmbarque(LiberacaoEmbarque bean, List<LiberacaoEmbarque> liberacoesEmbarque) {
        for (LiberacaoEmbarque liberacaoEmbarque : liberacoesEmbarque) {
            if (bean.getIdLiberacaoEmbarque() == null || !bean.getIdLiberacaoEmbarque().equals(liberacaoEmbarque.getIdLiberacaoEmbarque())) {
                if (liberacaoEmbarque.getTpModal() == null && bean.getTpModal() == null ||
                        (liberacaoEmbarque.getTpModal() != null && liberacaoEmbarque.getTpModal().equals(bean.getTpModal()))) {
                    throw new BusinessException("LMS-01345", new Object[]{buscarStringLiberacaoEmbarque(liberacaoEmbarque)});

                }
            }
        }
    }

    private String exibirMensagemEmAprovacaoAprovada(LiberacaoEmbarque liberacaoEmbarque) {
        return liberacaoEmbarque.getBlEfetivado() ?
                "aprovada" : "em aprovação";
    }

    private String buscarStringLiberacaoEmbarque(LiberacaoEmbarque liberacaoEmbarque) {
        return liberacaoEmbarque.getTpModal() == null ?
                "Rodoviário/Aéreo" :
                "A".equals(liberacaoEmbarque.getTpModal().getValue()) ? "Aéreo"
                        : "Rodoviário";
    }

    /**
     * Remove todas os itens relacionados ao cliente informado.
     *
     * @param idCliente identificador do cliente
     */
    public void removeByIdCliente(Long idCliente) {
        getLiberacaoEmbarqueDAO().removeByIdCliente(idCliente);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     *
     * @param dao Instância do DAO.
     */
    public void setLiberacaoEmbarqueDAO(LiberacaoEmbarqueDAO dao) {
        setDao(dao);
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private LiberacaoEmbarqueDAO getLiberacaoEmbarqueDAO() {
        return (LiberacaoEmbarqueDAO) getDao();
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void setTipoLocalizacaoMunicipioService(TipoLocalizacaoMunicipioService tipoLocalizacaoMunicipioService) {
        this.tipoLocalizacaoMunicipioService = tipoLocalizacaoMunicipioService;
    }

    public void setWorkflowPendenciaService(
            WorkflowPendenciaService workflowPendenciaService) {
        this.workflowPendenciaService = workflowPendenciaService;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }

    public void setAcaoService(AcaoService acaoService) {
        this.acaoService = acaoService;
    }

    public Integer getRowCountByIdMunicipioIdClienteModal(Long idMunicipio, Long idCliente, String modal) {
        return getLiberacaoEmbarqueDAO().getRowCountByIdMunicipioIdClienteModal(idMunicipio, idCliente, modal);
    }

    public Integer getRowCountByIdMunicipioIdClienteModalAferido(Long idMunicipio, Long idCliente, String modal) {
        return getLiberacaoEmbarqueDAO().getRowCountByIdMunicipioIdClienteModalAferido(idMunicipio, idCliente, modal);
    }

    public PpeService getPpeService() {
        return ppeService;
    }

    public void setPpeService(PpeService ppeService) {
        this.ppeService = ppeService;
    }

    public FilialService getFilialService() {
        return filialService;
    }

    public void setFilialService(FilialService filialService) {
        this.filialService = filialService;
    }

    /**
     * Solicitação CQPRO00005947 da Integração.
     *
     * @param idCliente
     */
    public Boolean existsLiberacaoByCliente(Long idCliente) {
        Integer result = getLiberacaoEmbarqueDAO().getRowCountByIdCliente(idCliente);
        return result > 0;
    }

    /**
     * Valida se um municipio é Capital Operacional ou nao
     *
     * @param criteria - Map que contenha um elemento chamado idMunicipio
     * @return
     * @author Vagner Huzalo
     */
    public boolean validateCapital(Map criteria) {
        Long idMunicipio = Long.valueOf((String) criteria.get("idMunicipio"));
        if (idMunicipio == null) {
            throw new IllegalArgumentException("Deve ser informado o id do municipio");
        }
        TipoLocalizacaoMunicipio tlm = tipoLocalizacaoMunicipioService.findByIdMunicipio(idMunicipio);
        TipoLocalizacaoMunicipio capital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio("Capital", "O");
        return tlm.getIdTipoLocalizacaoMunicipio() != null && capital != null && tlm.equals(capital);
    }

    public LiberacaoEmbarque executeEfetivar(Long idLiberacaoEmbarque, String tpSituacoesWorkflow) {
        LiberacaoEmbarque le = findById(idLiberacaoEmbarque);
        le.setTpSituacaoAprovacao(new DomainValue(tpSituacoesWorkflow));

        if (ConstantesWorkflow.APROVADO.equals(tpSituacoesWorkflow)) {
            if (BooleanUtils.isTrue(le.getBlLiberaGrandeCapital())) {
                generateLiberacoesGrandeCapital(le);
            }
            setDadosAprovacao(le);
            le.setBlEfetivado(Boolean.TRUE);
        }

        super.store(le);
        return le;
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    private void setDadosAprovacao(LiberacaoEmbarque liberacaoEmbarque) {
        List<Acao> acoes = acaoService.findByIdPendenciaTpSituacaoAcao(liberacaoEmbarque.getPendencia().getIdPendencia(), ConstantesWorkflow.APROVADO);
        if (acoes != null && !acoes.isEmpty()) {
            Acao acao = acoes.get(0);
            liberacaoEmbarque.setUsuarioAprovacao(acao.getUsuario());
            liberacaoEmbarque.setDtAprovacao(acao.getDhAcao().toYearMonthDay());
        }
    }

    /**
     * Gera todas as liberações de embarque para o Cliente informado e para todos os municípios
     * da grande capital conforme a UF do municipio da liberação informada.
     *
     * @param le
     * @author Vagner Huzalo
     */
    public void generateLiberacoesGrandeCapital(LiberacaoEmbarque le) {
        //Garante que nao irá executar caso o checkbox nao esteja marcado
        if (BooleanUtils.isFalse(le.getBlLiberaGrandeCapital())) {
            return;
        }

        TipoLocalizacaoMunicipio grandeCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio(DS_TIPO_LOCALIZACAO_GRANDE_CAPTIAL, TP_LOCALIZACAO_OPERACIONAL);

        List<Municipio> ids = getLiberacaoEmbarqueDAO().findMunicipiosSemLiberacao(
                le.getCliente(),
                le.getMunicipio().getUnidadeFederativa(),
                grandeCapital);

        for (Municipio municipio : ids) {
            LiberacaoEmbarque liberacao = new LiberacaoEmbarque();
            liberacao.setCliente(le.getCliente());
            liberacao.setMunicipio(municipio);
            liberacao.setBlEfetivado(Boolean.TRUE);
            liberacao.setBlLiberaGrandeCapital(Boolean.FALSE);
            //utiliza o metodo store da superclasse para nao gerar pendencias para as liberacoes
            super.store(liberacao);
        }
    }

    public LiberacaoEmbarque executeDesefetivar(Long idLiberacaoEmbarque, String tpSituacoesWorkflow) {
        LiberacaoEmbarque le = findById(idLiberacaoEmbarque);
        le.setTpSituacaoAprovacao(new DomainValue(tpSituacoesWorkflow));

        if (ConstantesWorkflow.APROVADO.equals(tpSituacoesWorkflow)) {
            if (BooleanUtils.isTrue(le.getBlLiberaGrandeCapital())) {
                TipoLocalizacaoMunicipio grandeCapital = tipoLocalizacaoMunicipioService.findTipoLocalizacaoMunicipio(DS_TIPO_LOCALIZACAO_GRANDE_CAPTIAL, TP_LOCALIZACAO_OPERACIONAL);
                List<Long> idsMunicipios = getLiberacaoEmbarqueDAO().findIdsMunicipiosComLiberacao(le.getCliente(), le.getMunicipio().getUnidadeFederativa(), grandeCapital);
                getLiberacaoEmbarqueDAO().executeDesefetivarByMunicipioCliente(idsMunicipios, le.getCliente().getIdCliente());
            }

            setDadosAprovacao(le);
            le.setBlEfetivado(Boolean.FALSE);
        }
        this.store(le);
        return le;
    }

    public void executeSolicitarEfetivacao(LiberacaoEmbarque liberacaoEmbarqueTela) {
        String dsMotivoSolicitacao = liberacaoEmbarqueTela.getDsMotivoSolicitacao();
        LiberacaoEmbarque liberacaoEmbarque = this.findById(liberacaoEmbarqueTela.getIdLiberacaoEmbarque());

        List<LiberacaoEmbarque> liberacoesEmbarque =
                getLiberacaoEmbarqueDAO().findLiberacoesClienteByMunicipio(liberacaoEmbarque.getCliente().getIdCliente(), liberacaoEmbarque.getMunicipio().getIdMunicipio());

        if (liberacoesEmbarque != null) {
            for (LiberacaoEmbarque liberacaoEmbarqueMetodo : liberacoesEmbarque) {
                if (!liberacaoEmbarque.getIdLiberacaoEmbarque().equals(liberacaoEmbarqueMetodo.getIdLiberacaoEmbarque())) {
                    validateLiberacaoEmbarqueEfetivacao(liberacaoEmbarque, liberacaoEmbarqueMetodo);
                }
            }
        }


        //Liberacao embarque tela. Se for feita a efetivação de um registro que esteja efetivado ou em aprovação, não permitir dos demais.
        //Relação de Duplo x unico aereo e rodoviario

        Pendencia pendencia = generatePendenciaWorkflow(liberacaoEmbarque, dsMotivoSolicitacao, ConstantesWorkflow.NR111_APROVACAO_LIBERACAO_EMBARQUE);
        storeLiberacaoEmbarqueWK(liberacaoEmbarque, pendencia);
    }

    private void validateLiberacaoEmbarqueEfetivacao(LiberacaoEmbarque liberacaoEmbarque, LiberacaoEmbarque liberacaoEmbarqueMetodo) {
        if (liberacaoEmbarqueMetodo.getTpModal() == null &&
                (liberacaoEmbarqueMetodo.getTpSituacaoAprovacao() != null &&
                        verificarLiberacaoEmbarqueEstaAprovadaOuEmAprovacao(liberacaoEmbarqueMetodo))
                && liberacaoEmbarque.getTpModal() != null) {
            throw new BusinessException("LMS-01344", new Object[]{exibirMensagemEmAprovacaoAprovada(liberacaoEmbarqueMetodo)});
        } else if (liberacaoEmbarqueMetodo.getTpModal() != null &&
                (liberacaoEmbarqueMetodo.getTpSituacaoAprovacao() != null &&
                        verificarLiberacaoEmbarqueEstaAprovadaOuEmAprovacao(liberacaoEmbarqueMetodo))
                && liberacaoEmbarque.getTpModal() == null) {
            throw new BusinessException("LMS-01344", new Object[]{exibirMensagemEmAprovacaoAprovada(liberacaoEmbarqueMetodo)});
        }
    }

    private boolean verificarLiberacaoEmbarqueEstaAprovadaOuEmAprovacao(LiberacaoEmbarque liberacaoEmbarqueMetodo) {
        return "E".equals(liberacaoEmbarqueMetodo.getTpSituacaoAprovacao().getValue()) || liberacaoEmbarqueMetodo.getBlEfetivado();
    }

    public void executeSolicitarDesefetivacao(LiberacaoEmbarque liberacaoEmbarqueTela) {
        String dsMotivoSolicitacao = liberacaoEmbarqueTela.getDsMotivoSolicitacao();
        LiberacaoEmbarque liberacaoEmbarque = this.findById(liberacaoEmbarqueTela.getIdLiberacaoEmbarque());
        Pendencia pendencia = generatePendenciaWorkflow(liberacaoEmbarque, dsMotivoSolicitacao, ConstantesWorkflow.NR147_APROVACAO_DESEFETIVACAO_LIBERACAO_EMBARQUE);
        storeLiberacaoEmbarqueWK(liberacaoEmbarque, pendencia);
    }

    private void storeLiberacaoEmbarqueWK(LiberacaoEmbarque liberacaoEmbarque, Pendencia pendencia) {
        liberacaoEmbarque.setPendencia(pendencia);
        liberacaoEmbarque.setTpSituacaoAprovacao(pendencia.getTpSituacaoPendencia());
        liberacaoEmbarque.setUsuarioAprovacao(null);
        liberacaoEmbarque.setDtAprovacao(null);
        super.store(liberacaoEmbarque);
    }

    /**
     * @param liberacaoEmbarque
     * @param nrTipoEvento
     */
    private Pendencia generatePendenciaWorkflow(LiberacaoEmbarque liberacaoEmbarque, String dsMotivoSolicitacao, Short nrTipoEvento) {
        String dsVlAntigo = null;
        String dsVlNovo = null;
        String dsProcessoWK = null;
        if (ConstantesWorkflow.NR111_APROVACAO_LIBERACAO_EMBARQUE.equals(nrTipoEvento)) {
            dsVlAntigo = configuracoesFacade.getMensagem("desefetivado");
            dsVlNovo = configuracoesFacade.getMensagem("efetivado");
            dsProcessoWK = generateDsProcessoWK("LMS-01249", liberacaoEmbarque.getMunicipio().getNmMunicipio(), liberacaoEmbarque.getCliente(), dsMotivoSolicitacao);
        } else if (ConstantesWorkflow.NR147_APROVACAO_DESEFETIVACAO_LIBERACAO_EMBARQUE.equals(nrTipoEvento)) {
            dsVlAntigo = configuracoesFacade.getMensagem("efetivado");
            dsVlNovo = configuracoesFacade.getMensagem("desefetivado");
            dsProcessoWK = generateDsProcessoWK("LMS-01250", liberacaoEmbarque.getMunicipio().getNmMunicipio(), liberacaoEmbarque.getCliente(), dsMotivoSolicitacao);
        }

        PendenciaHistoricoDTO pendenciaHistoricoDTO = new PendenciaHistoricoDTO();
        pendenciaHistoricoDTO.setIdFilial(getIdFilialResponsavel(liberacaoEmbarque.getMunicipio().getIdMunicipio()));
        pendenciaHistoricoDTO.setNrTipoEvento(nrTipoEvento);
        pendenciaHistoricoDTO.setIdProcesso(liberacaoEmbarque.getIdLiberacaoEmbarque());
        pendenciaHistoricoDTO.setDsProcesso(dsProcessoWK);
        pendenciaHistoricoDTO.setDhLiberacao(JTDateTimeUtils.getDataHoraAtual());
        pendenciaHistoricoDTO.setTabelaHistoricoWorkflow(TabelaHistoricoWorkflow.LIBERACAO_EMBARQUE);
        pendenciaHistoricoDTO.setCampoHistoricoWorkflow(CampoHistoricoWorkflow.LEMB);
        pendenciaHistoricoDTO.setDsVlAntigo(dsVlAntigo);
        pendenciaHistoricoDTO.setDsVlNovo(dsVlNovo);
        pendenciaHistoricoDTO.setDsObservacao(dsMotivoSolicitacao);

        return workflowPendenciaService.generatePendenciaHistorico(pendenciaHistoricoDTO);
    }

    /**
     * @param chave
     * @param nmMunicipio
     * @param cliente
     * @param dsMotivoSolicitacao
     * @return
     */
    private String generateDsProcessoWK(String chave, String nmMunicipio, Cliente cliente, String dsMotivoSolicitacao) {
        return configuracoesFacade.getMensagem(chave, new Object[]{
                nmMunicipio,
                FormatUtils.formatIdentificacao(cliente.getPessoa()),
                cliente.getPessoa().getNmPessoa(),
                dsMotivoSolicitacao});
    }

    private Long getIdFilialResponsavel(Long idMunicipio) {
        Filial filialResponsavel = getFilialResponsavelMunicipio(idMunicipio);

        if (filialResponsavel == null || filialResponsavel.getIdFilial() == null) {
            throw new BusinessException("LMS-01102");
        }

        return filialResponsavel.getIdFilial();
    }

    public void validateAtendimentoMunicipio(TypedFlatMap criteria) {
        getIdFilialResponsavel(criteria.getLong("idMunicipio"));
    }

    public List<LiberacaoEmbarque> findLiberacaoCliente(Long idCliente, Long idMunicipio, DomainValue tpModal) {
        return getLiberacaoEmbarqueDAO().findLiberacaoCliente(idCliente, idMunicipio, tpModal);
    }

    // Método Responsável por retornar a Filial responsavel pelo Municio
    public Filial getFilialResponsavelMunicipio(Long idMunicipio) {

        // Retorna id do serviço
        Long idServico = ((BigDecimal) configuracoesFacade.getValorParametro("ID_SERVICO_RODOVIARIO_NACIONAL")).longValue();

        Map municipioResponsavel = ppeService.findAtendimentoMunicipio(idMunicipio, idServico, Boolean.FALSE, JTDateTimeUtils.getDataAtual(), null, null, null,
                null, null, null, "N", null, null);

        return filialService.findById((Long) municipioResponsavel.get("idFilial"));
    }

    public HistoricoWorkflowService getHistoricoWorkflowService() {
        return historicoWorkflowService;
    }

    public void setHistoricoWorkflowService(HistoricoWorkflowService historicoWorkflowService) {
        this.historicoWorkflowService = historicoWorkflowService;
    }

    public boolean checkLiberacaoFOBDirigido(DoctoServico doctoServico) {
        if (doctoServico != null) {
            Municipio municipio = null;
            if (doctoServico.getEnderecoPessoa() != null) {
                municipio = doctoServico.getEnderecoPessoa().getMunicipio();
            }

            return checkLiberacaoEmbarqueFOBDirigido(doctoServico.getClienteByIdClienteRemetente(),
                    doctoServico.getClienteByIdClienteDestinatario(),
                    doctoServico.getServico().getTpModal().getValue(),
                    municipio);
        }

        return false;
    }

    public boolean checkLiberacaoFOBDirigido(Cotacao cotacao) {
        if (cotacao != null) {
            return checkLiberacaoEmbarqueFOBDirigido(cotacao.getClienteByIdClienteSolicitou(),
                    cotacao.getClienteByIdClienteDestino(),
                    cotacao.getServico().getTpModal().getValue(),
                    cotacao.getMunicipioByIdMunicipioEntrega());
        }

        return false;
    }

    private boolean checkLiberacaoEmbarqueFOBDirigido(Cliente clienteRemetente, Cliente clienteDestinatario, String tpModal, Municipio municipio) {

        LiberacaoEmbarque liberacaoEmbarque = findLiberacaoClienteByMunicipio(clienteRemetente, clienteDestinatario, municipio);

        if (ClienteUtils.isClienteFobDirigidoRodoOrAereo(tpModal, clienteRemetente.getBlFobDirigido(), clienteRemetente.getBlFobDirigidoAereo())
                && ClienteUtils.isClienteEventualOrPotencial(clienteDestinatario.getTpCliente().getValue())
                && (liberacaoEmbarque != null && liberacaoEmbarque.getBlEfetivado())) {
            return true;
        }
        return false;
    }

    public LiberacaoEmbarque findLiberacaoClienteByMunicipio(Cliente clienteResponsavel, Cliente clienteDestinatario, Municipio municipio) {
        Municipio municipioDestino = municipio;
        if (municipio == null) {
            municipioDestino = clienteDestinatario.getPessoa().getEnderecoPessoa().getMunicipio();
        }
        return getLiberacaoEmbarqueDAO().findLiberacaoClienteByMunicipio(clienteResponsavel.getIdCliente(), municipioDestino.getIdMunicipio());
    }

}
