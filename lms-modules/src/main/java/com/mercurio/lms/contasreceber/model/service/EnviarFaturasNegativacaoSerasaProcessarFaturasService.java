package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.PessoaDAO;
import com.mercurio.lms.contasreceber.model.ItemLoteSerasa;
import com.mercurio.lms.contasreceber.model.LoteSerasa;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;
import com.mercurio.lms.vendas.model.dao.ClienteDAO;
import com.mercurio.lms.vendas.model.service.ProibidoEmbarqueService;
import org.joda.time.YearMonthDay;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EnviarFaturasNegativacaoSerasaProcessarFaturasService {
    private FaturaDAO faturaDao;
    private LoteSerasaService loteSerasaService;
    private ConfiguracoesFacade configuracoesFacade;
    private PessoaDAO pessoaDao;
    private ClienteDAO clienteDao;
    private ProibidoEmbarqueService proibidoEmbarqueService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void executeEnviarFaturas(final List<Object[]> faturas, final Long idMotivoProibidoEmbarque, final String dsBloqueio) {
        LoteSerasa loteSerasa = storeLoteSerasa();

        for (Object[] fatura : faturas) {
            Long idFatura = (Long) fatura[0];
            Long idCliente = (Long) fatura[1];
            String nrIdentificacao = (String) fatura[2];

            storeItemLoteSerasa(loteSerasa, idFatura);

            if (!isPessoaFisica(pessoaDao.findById(idCliente))) {
                nrIdentificacao = nrIdentificacao.substring(0, 8);
            }

            List<Pessoa> pessoas = pessoaDao.finPessoasByNrIdentificacaoParcialWithClient(nrIdentificacao);
            processarProibidoEmbarque(pessoas, idMotivoProibidoEmbarque, dsBloqueio);
        }
    }

    private void processarProibidoEmbarque(List<Pessoa> pessoas, Long idMotivoProibidoEmbarque, String dsBloqueio) {
        for (Pessoa pessoa : pessoas) {
            if (existeBloqueioEmbarque(pessoa.getIdPessoa())) {
                continue;
            }
            storeProibidoEmbarque(pessoa, idMotivoProibidoEmbarque, dsBloqueio);
        }
    }

    private LoteSerasa storeLoteSerasa() {
        LoteSerasa loteSerasa = new LoteSerasa();
        loteSerasa.setTpLote(new DomainValue("N"));
        loteSerasa.setDsLote(configuracoesFacade.getMensagem("LMS-36315", new Object[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) }));
        loteSerasa = loteSerasaService.store(loteSerasa);
        return loteSerasa;
    }

    private boolean existeBloqueioEmbarque(Long idPessoa) {
        return proibidoEmbarqueService.getRowCountProibidoByIdCliente(idPessoa) > 0 ;
    }

    private void storeProibidoEmbarque(Pessoa pessoa, Long idMotivoProibidoEmbarque, String dsBloqueio){
        ProibidoEmbarque proibidoEmbarque = new ProibidoEmbarque();
        MotivoProibidoEmbarque motivoProibidoEmbarque = new MotivoProibidoEmbarque();
        motivoProibidoEmbarque.setIdMotivoProibidoEmbarque(idMotivoProibidoEmbarque);
        proibidoEmbarque.setMotivoProibidoEmbarque(motivoProibidoEmbarque);
        proibidoEmbarque.setCliente(clienteDao.findByIdComPessoa(pessoa.getIdPessoa()) );
        proibidoEmbarque.setUsuarioByIdUsuarioBloqueio(SessionUtils.getUsuarioLogado());
        proibidoEmbarque.setDtBloqueio(new YearMonthDay());
        proibidoEmbarque.setDsBloqueio(dsBloqueio);
        proibidoEmbarqueService.store(proibidoEmbarque);
    }

    private boolean isPessoaFisica(Pessoa pessoa) {
        return pessoa.getTpPessoa().getValue().equalsIgnoreCase("F");
    }

    private void storeItemLoteSerasa(LoteSerasa loteSerasa, Long idFatura) {
        ItemLoteSerasa item = new ItemLoteSerasa();
        item.setFatura(faturaDao.findByIdFatura(idFatura));
        item.setLoteSerasa(loteSerasa);
        loteSerasaService.store(item);
    }

    public FaturaDAO getFaturaDao() {
        return faturaDao;
    }

    public void setFaturaDao(FaturaDAO faturaDao) {
        this.faturaDao = faturaDao;
    }

    public LoteSerasaService getLoteSerasaService() {
        return loteSerasaService;
    }

    public void setLoteSerasaService(LoteSerasaService loteSerasaService) {
        this.loteSerasaService = loteSerasaService;
    }

    public PessoaDAO getPessoaDao() {
        return pessoaDao;
    }

    public void setPessoaDao(PessoaDAO pessoaDao) {
        this.pessoaDao = pessoaDao;
    }

    public ClienteDAO getClienteDao() {
        return clienteDao;
    }

    public void setClienteDao(ClienteDAO clienteDao) {
        this.clienteDao = clienteDao;
    }

    public ProibidoEmbarqueService getProibidoEmbarqueService() {
        return proibidoEmbarqueService;
    }

    public void setProibidoEmbarqueService(ProibidoEmbarqueService proibidoEmbarqueService) {
        this.proibidoEmbarqueService = proibidoEmbarqueService;
    }

    public ConfiguracoesFacade getConfiguracoesFacade() {
        return configuracoesFacade;
    }

    public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
        this.configuracoesFacade = configuracoesFacade;
    }
}
