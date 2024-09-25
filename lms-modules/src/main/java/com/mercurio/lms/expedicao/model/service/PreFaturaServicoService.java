package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import br.com.tntbrasil.integracao.domains.jms.Queues;
import br.com.tntbrasil.integracao.domains.mail.Mail;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Contato;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.InscricaoEstadual;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.ServicoAdicional;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.InscricaoEstadualService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.ServicoAdicionalService;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.service.DevedorDocServFatService;
import com.mercurio.lms.expedicao.model.DevedorDocServ;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.ImpostoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServico;
import com.mercurio.lms.expedicao.model.NotaFiscalServicoDocumento;
import com.mercurio.lms.expedicao.model.ObservacaoDoctoServico;
import com.mercurio.lms.expedicao.model.OrdemServico;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;
import com.mercurio.lms.expedicao.model.ParcelaDoctoServico;
import com.mercurio.lms.expedicao.model.PreFaturaServico;
import com.mercurio.lms.expedicao.model.PreFaturaServicoItem;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.ServicoGeracaoAutomatica;
import com.mercurio.lms.expedicao.model.dao.PreFaturaServicoDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PpeService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tributos.model.CalcularPisCofinsCsllIrInss;
import com.mercurio.lms.tributos.model.ImpostoCalculado;
import com.mercurio.lms.tributos.model.service.AliquotaIssMunicipioServService;
import com.mercurio.lms.tributos.model.service.CalcularIssService;
import com.mercurio.lms.tributos.model.service.CalcularPisCofinsCsllIrInssService;
import com.mercurio.lms.tributos.model.service.ImpostoCalculadoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DivisaoClienteService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;

public class PreFaturaServicoService extends CrudService<PreFaturaServico, Long> {
	private static final int DS_OBSERVACAO_MAX_SIZE = 500;
	private static final String TEXT_HTML = "text/html; charset='utf-8'";
	
	private IntegracaoJmsService integracaoJmsService;
	private AliquotaIssMunicipioServService aliquotaIssMunicipioServService;
	private CalcularIssService calcularIssService;
	private CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService;
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;
	private DevedorDocServFatService devedorDocServFatService;
	private DevedorDocServService devedorDocServService;
	private DivisaoClienteService divisaoClienteService;
	private EmitirDocumentoService emitirDocumentoService;
	private FilialService filialService;
	private ImpostoCalculadoService impostoCalculadoService;
	private ImpostoServicoService impostoServicoService;
	private InscricaoEstadualService inscricaoEstadualService;
	private MunicipioService municipioService;
	private NotaFiscalEletronicaService notaFiscalEletronicaService;
	private NotaFiscalServicoDocumentoService notaFiscalServicoDocumentoService;
	private NotaFiscalServicoService notaFiscalServicoService;
	private ObservacaoDoctoServicoService observacaoDoctoServicoService;
	private OrdemServicoItemService ordemServicoItemService;
	private OrdemServicoService ordemServicoService;
	private ParcelaDoctoServicoService parcelaDoctoServicoService;
	private PpeService ppeService;
	private PreFaturaServicoItemService preFaturaServicoItemService;
	private ParametroGeralService parametroGeralService;
	private ServAdicionalDocServService servAdicionalDocServService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ServicoAdicionalService servicoAdicionalService;
	private ServicoGeracaoAutomaticaService servicoGeracaoAutomaticaService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	
	public void storeGerarPreFatura(PreFaturaServico bean) {
		if (bean.getIdPreFaturaServico() == null) {			
			Long nrFaturaServico = configuracoesFacade.incrementaParametroSequencial(bean.getFilialCobranca().getIdFilial(), "NR_FATURA_SERVICO", true);
			bean.setNrPreFatura(nrFaturaServico);
			
			bean.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());
			
			if (bean.getTpSituacao() != null && ConstantesExpedicao.TP_SITUACAO_REPROVADA.equals(bean.getTpSituacao().getValue())) {
				bean.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_REPROVADA));
			} else if (bean.getTpSituacao() != null && "REPROVADA".equals(bean.getTpSituacao().getValue())) {
				bean.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_PRE_FATURA_FINALIZADA));
			} else {
				bean.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_PRE_FATURA_GERADA));
			}

			UsuarioLMS usuario = new UsuarioLMS();
			usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
			bean.setUsuario(usuario);		
		}
		
		List<Long> idsItensOS = new ArrayList<Long>();
		List<Long> idsGeracaoAutomatica = new ArrayList<Long>();
		
		for (PreFaturaServicoItem item : bean.getPreFaturaServicoItens()) {
			if (item.getOrdemServicoItem() != null) {
				idsItensOS.add(item.getOrdemServicoItem().getIdOrdemServicoItem());
			} else if (item.getServicoGeracaoAutomatica() != null) {
				idsGeracaoAutomatica.add(item.getServicoGeracaoAutomatica().getIdServicoGeracaoAutomatica());
			}
		}
		
		super.store(bean);
		preFaturaServicoItemService.storeAll(bean.getPreFaturaServicoItens());			
		
		ordemServicoItemService.storeFaturamentoItemByIds(idsItensOS, Boolean.TRUE, null);
		ordemServicoService.executeUpdateOrdensServicoEmPreFatura(idsItensOS);
		servicoGeracaoAutomaticaService.storeFaturamentoItemByIds(idsGeracaoAutomatica, Boolean.TRUE, null);		
	}
	
	public void storeAprovarPreFatura(Long idPreFaturaServico, Long idFilialCobranca, Long idCliente, 
			Long idDivisaoCliente, List<Map<String, Object>> preFaturaServicoItens) {
		validateFilial(idFilialCobranca);
		
		List<Map<String, Object>> itensAprovados = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> itensReprovados = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> itensPostergados = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object> item : preFaturaServicoItens) {
			String tpAcao = (String) item.get("tpSituacao");
			String tpSituacao = (String) item.get("tpSituacao");
			Long idPreFaturaServicoItem = (Long) item.get("idPreFaturaServicoItem");
			Long idMotivo = (Long) item.get("idMotivo");
			
			if (ConstantesExpedicao.TP_ACAO_APROVAR_ITEM_PRE_FATURA.equals(tpAcao)) {
				itensAprovados.add(item);							
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_APROVADO;
				
				preFaturaServicoItemService.storeSitucaoValorMotivoById(idPreFaturaServicoItem, tpSituacao, null, idMotivo);
				
			} else if(ConstantesExpedicao.TP_ACAO_APROVAR_COM_DESCONTO_ITEM_PRE_FATURA.equals(tpAcao)) { 
				itensAprovados.add(item);
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_APROVADO_COM_DESCONTO;
			} else if (ConstantesExpedicao.TP_ACAO_REPROVAR_ITEM_PRE_FATURA.equals(tpSituacao)) {
				itensReprovados.add(item);
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_REPROVADO;
			} else if (ConstantesExpedicao.TP_ACAO_POSTERGAR_ITEM_PRE_FATURA.equals(tpSituacao)) {
				itensPostergados.add(item);
				tpSituacao = ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_POSTERGADO;
			} 			
		}
		
		executeGerarNFServicoAdicional(idCliente, idDivisaoCliente, itensAprovados);
		executeAprovarItensFatura(itensAprovados);
		executeReprovarItensFatura(itensReprovados);
		executePostergarItensFatura(itensPostergados);
		
		PreFaturaServico bean = this.findById(idPreFaturaServico);
		bean.setTpSituacao(new DomainValue(ConstantesExpedicao.TP_SITUACAO_PRE_FATURA_FINALIZADA));
		
		// LMS-7220
		UsuarioLMS responsavelFinalizacao = new UsuarioLMS();
		responsavelFinalizacao.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		bean.setUsuarioFinalizacao(responsavelFinalizacao);
		
		super.store(bean);
	}

	public void validateFilial(Long idFilialCobranca) {
		if (!validateFilialUsuario(idFilialCobranca)) {
			throw new BusinessException("LMS-04483");
		}
	}

	private void executeAprovarItensFatura(List<Map<String, Object>> itensPreFaturaAprovados) {
		executeUpdateItensPreFatura(itensPreFaturaAprovados, Boolean.TRUE, Boolean.FALSE, ConstantesExpedicao.TP_ACAO_APROVAR_ITEM_PRE_FATURA);
	}
	
	private void executeReprovarItensFatura(List<Map<String, Object>> itensPreFaturaReprovados) {
		executeUpdateItensPreFatura(itensPreFaturaReprovados, Boolean.TRUE, Boolean.TRUE, ConstantesExpedicao.TP_SITUACAO_ITEM_PRE_FATURA_REPROVADO);
	}
	
	private void executePostergarItensFatura(List<Map<String, Object>> itensPreFaturaPostergados) {
		executeUpdateItensPreFatura(itensPreFaturaPostergados, Boolean.FALSE, Boolean.FALSE, ConstantesExpedicao.TP_ACAO_POSTERGAR_ITEM_PRE_FATURA);
	}
	
	private void executeUpdateItensPreFatura(List<Map<String, Object>> itens, Boolean blFaturado, Boolean blSemCobranca, String tpAcao) {		
		List<Long> idsItensOrdemServico = new ArrayList<Long>();
		List<Long> idsItensGeracaoAutomatica = new ArrayList<Long>();
		for(Map<String, Object> item : itens) {
			if(item.get("idOrdemServicoItem") != null) {
				idsItensOrdemServico.add((Long)item.get("idOrdemServicoItem"));
			} else if (item.get("idServicoGeracaoAutomatica") != null) {
				idsItensGeracaoAutomatica.add((Long)item.get("idServicoGeracaoAutomatica"));
			}
		}
		
		ordemServicoItemService.storeFaturamentoItemByIds(idsItensOrdemServico, blFaturado, blSemCobranca);
		
		if (ConstantesExpedicao.TP_ACAO_POSTERGAR_ITEM_PRE_FATURA.equals(tpAcao)) {
		    servicoGeracaoAutomaticaService.storeFaturamentoItemByIds(idsItensGeracaoAutomatica);
		}else{
		    servicoGeracaoAutomaticaService.storeFaturamentoItemByIds(idsItensGeracaoAutomatica,  blFaturado, blSemCobranca);
		}
	}

	@SuppressWarnings("unchecked")
	public void executeGerarNFServicoAdicional(Long idClienteTomador, Long idDivisaoClienteTomador,
			List<Map<String, Object>> itensPreFatura) {
		
		Cliente clienteTomador = clienteService.findById(idClienteTomador);		
		Long idServicoPadrao = ((BigDecimal)configuracoesFacade.getValorParametro("IDServicoPadraoConhecimento")).longValue();
		List<NotaFiscalServico> notasFiscaisServico = new ArrayList<NotaFiscalServico>();
				
		List<Map<String, Object>> itensToStore = executeAgrupamentoItens(itensPreFatura, idDivisaoClienteTomador, idServicoPadrao);
								
		
		for(Map<String, Object> item : itensToStore) {			
			Long idMunicipioExecucao = (Long)item.get("idMunicipioExecucao");			
			Long idServicoAdicional = (Long)item.get("idServicoAdicional");
			ServicoAdicional servicoAdicional = servicoAdicionalService.findById(idServicoAdicional);
			Long idParcelaPreco = (Long)item.get("idParcelaPreco");
			List<Map<String, Object>> idsPreFaturaServicoItem = (List<Map<String, Object>>)item.get("idsPreFaturaServicoItem");			
			BigDecimal vlBaseCalculo = (BigDecimal)item.get("vlCobranca");		
			
			Long idFilialAtendimento = ppeService.findFilialAtendimentoMunicipio(
					idMunicipioExecucao, idServicoPadrao, Boolean.TRUE);		
			
			Filial filialExecucao = ValidateFilialExecucao(itensPreFatura, idFilialAtendimento);
				
			String tpNFS = validateEmissaoNFS(clienteTomador, filialExecucao, servicoAdicional, idMunicipioExecucao);
			
			List<ImpostoServico> impostosServico = new ArrayList<ImpostoServico>();
			List<ImpostoCalculado> impostosCalculados = new ArrayList<ImpostoCalculado>();
			
			BigDecimal[] vlCalculados = executeCalculoImpostos(clienteTomador, filialExecucao, idServicoAdicional, idMunicipioExecucao, 
					vlBaseCalculo, impostosServico, impostosCalculados); 
			BigDecimal vlDevido = vlCalculados[0];
			BigDecimal vlTotalImpostos = vlCalculados[1];
			
			/* GRAVAÇÕES DIVERSAS */
			NotaFiscalServico nfs = storeNotaFiscalServico(clienteTomador, filialExecucao, idMunicipioExecucao, 
					idServicoPadrao, idDivisaoClienteTomador, tpNFS, vlBaseCalculo, vlDevido, vlTotalImpostos);					
			storeServAdicionalDocServ(nfs, servicoAdicional);			
			storeDevedorDocServ(nfs, clienteTomador);
			storeDevedorDocServFat(nfs, clienteTomador, vlDevido);
			storeImpostosServico(nfs, impostosServico);
			storeImpostosCalculados(impostosCalculados);
			storeParcelaDoctoServico(nfs, idParcelaPreco, vlBaseCalculo);
			storeObservacaoDoctoServico(nfs, idsPreFaturaServicoItem);
			storeNotaFiscalServicoDocumentos(nfs, idsPreFaturaServicoItem);
			notasFiscaisServico.add(nfs);
		}		
		
		storeEmitirNotaFiscalServicoEletronica(notasFiscaisServico);		
		executeSendMail(notasFiscaisServico);
	}

    private Filial ValidateFilialExecucao(List<Map<String, Object>> itensPreFatura, Long idFilialAtendimento) {
        String parametroGeral = (String) parametroGeralService.findConteudoByNomeParametro("FILIAL_EXECUCAO", false);
        Long idOrdemServicoItem = (Long)itensPreFatura.get(0).get("idOrdemServicoItem");
        Long idServicoGeracaoAutomatica = (Long)itensPreFatura.get(0).get("idServicoGeracaoAutomatica");
        Filial filialExecucaoServico = null;
        
        if(idOrdemServicoItem != null){
            OrdemServicoItem ordemServicoItem = (OrdemServicoItem)ordemServicoItemService.findById(idOrdemServicoItem);
            OrdemServico ordemServico = ordemServicoItem.getOrdemServico();
            filialExecucaoServico = filialService.findById(ordemServico.getFilialExecucao().getIdFilial());		
            
        }else if(idServicoGeracaoAutomatica!= null){
            ServicoGeracaoAutomatica servicoGeracaoAutomatica = (ServicoGeracaoAutomatica) servicoGeracaoAutomaticaService.findById(idServicoGeracaoAutomatica);
            filialExecucaoServico = filialService.findById(servicoGeracaoAutomatica.getFilialExecucao().getIdFilial());
        }
        
        if(filialExecucaoServico != null){
            if(parametroGeral.contains(filialExecucaoServico.getIdFilial().toString())){
                return filialExecucaoServico;
            }
        }
        
        return filialService.findById(idFilialAtendimento);
    }
	
    
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> executeAgrupamentoItens(List<Map<String, Object>> itensPreFatura, Long idDivisaoCliente, Long idServico) {
		Map<Long, Boolean> mapServicosNaoAgrupar = findServicosAdicionaisClienteNaoAgrupar(idDivisaoCliente, idServico);		
		Map<String, List<Map<String, Object>>> itensAgrupados = new HashMap<String, List<Map<String,Object>>>();
		List<Map<String, Object>> itensToReturn = new ArrayList<Map<String,Object>>();
		
		for(Map<String, Object> item : itensPreFatura) {
			Long idParcelaPreco = (Long)item.get("idParcelaPreco");
			if(mapServicosNaoAgrupar.containsKey(idParcelaPreco)) {
				Map<String, Object> itemNaoAgrupado = new HashMap<String, Object>();									
				List<Map<String, Object>> idsItensPreFatura = new ArrayList<Map<String,Object>>();
				
				itemNaoAgrupado.put("idMunicipioExecucao", (Long)item.get("idMunicipioExecucao"));
				itemNaoAgrupado.put("idServicoAdicional", (Long)item.get("idServicoAdicional"));
				itemNaoAgrupado.put("idParcelaPreco", (Long)item.get("idParcelaPreco"));
				itemNaoAgrupado.put("vlCobranca", (BigDecimal)item.get("vlCobranca"));
				
				Map<String, Object> idsDoctosItemPreFatura = new HashMap<String, Object>();
				idsDoctosItemPreFatura.put("idPreFaturaServicoItem", (Long)item.get("idPreFaturaServicoItem"));
				idsDoctosItemPreFatura.put("idsDoctosServico", (List<Long>)item.get("idsDoctosServico"));				
				idsItensPreFatura.add(idsDoctosItemPreFatura);
				
				itemNaoAgrupado.put("idsPreFaturaServicoItem", idsItensPreFatura);			
				itemNaoAgrupado.put("blAgrupado", Boolean.FALSE);
				itensToReturn.add(itemNaoAgrupado);
			} else {
				List<Map<String, Object>> listAgrupados = null;
				Long idMunicipioExecucao = (Long)item.get("idMunicipioExecucao");			

				if(itensAgrupados.containsKey(idParcelaPreco+"||"+idMunicipioExecucao)) {
					listAgrupados = itensAgrupados.get(idParcelaPreco+"||"+idMunicipioExecucao);
				} else {
					listAgrupados = new ArrayList<Map<String,Object>>();					
				}
				
				listAgrupados.add(item);
				itensAgrupados.put(idParcelaPreco+"||"+idMunicipioExecucao, listAgrupados);
			}
		}
		
		for(List<Map<String, Object>> listToGroup : itensAgrupados.values()) {
			Map<String, Object> itemAgrupado = new HashMap<String, Object>();									
			List<Map<String, Object>> idsItensPreFatura = new ArrayList<Map<String,Object>>();			
			BigDecimal vlBaseCalculo = BigDecimal.ZERO;			
			for(Map<String, Object> item : listToGroup) {
				itemAgrupado.put("idMunicipioExecucao", (Long)item.get("idMunicipioExecucao"));
				itemAgrupado.put("idServicoAdicional", (Long)item.get("idServicoAdicional"));
				itemAgrupado.put("idParcelaPreco", (Long)item.get("idParcelaPreco"));
				
				Map<String, Object> idsDoctosItemPreFatura = new HashMap<String, Object>();
				idsDoctosItemPreFatura.put("idPreFaturaServicoItem", (Long)item.get("idPreFaturaServicoItem"));
				idsDoctosItemPreFatura.put("idsDoctosServico", (List<Long>)item.get("idsDoctosServico"));				
				idsItensPreFatura.add(idsDoctosItemPreFatura);
				
				vlBaseCalculo = vlBaseCalculo.add((BigDecimal)item.get("vlCobranca"));
			}
			
			itemAgrupado.put("idsPreFaturaServicoItem", idsItensPreFatura);
			itemAgrupado.put("vlCobranca", vlBaseCalculo);
			itemAgrupado.put("blAgrupado", Boolean.TRUE);
			itensToReturn.add(itemAgrupado);
		}
		
		return itensToReturn;
	}
	
	private NotaFiscalServico storeNotaFiscalServico(Cliente clienteTomador, Filial filialExecucao, Long idMunicipioExecucao,
			Long idServico, Long idDivisaoClienteTomador, String tpNFS, BigDecimal vlBaseCalculo, BigDecimal vlDevido, BigDecimal vlImposto) {
		InscricaoEstadual ie = inscricaoEstadualService.findByPessoaIndicadorPadrao(
				clienteTomador.getIdCliente(), true);
		
		Servico servico = new Servico();
		servico.setIdServico(idServico);
				
		Municipio municipioExecucao = new Municipio();
		municipioExecucao.setIdMunicipio(idMunicipioExecucao);
		
		NotaFiscalServico nfs = new NotaFiscalServico();
		nfs.setMoeda(SessionUtils.getMoedaSessao());
		nfs.setClienteByIdClienteRemetente(clienteTomador);
		nfs.setClienteByIdClienteDestinatario(clienteTomador);
		nfs.setInscricaoEstadualRemetente(ie);
		nfs.setInscricaoEstadualDestinatario(ie);
		nfs.setServico(servico);
		nfs.setUsuarioByIdUsuarioInclusao(SessionUtils.getUsuarioLogado());
		nfs.setFilialByIdFilialOrigem(filialExecucao);
		nfs.setVlTotalDocServico(vlBaseCalculo);
		nfs.setVlTotalServicos(vlBaseCalculo);
		nfs.setVlImposto(vlImposto);
		nfs.setVlImpostoPesoDeclarado(BigDecimal.ZERO);
		nfs.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		nfs.setBlBloqueado(Boolean.FALSE);
		nfs.setTpNotaFiscalServico(new DomainValue(tpNFS));
		nfs.setTpDocumentoServico(new DomainValue(tpNFS));
		nfs.setVlBaseCalcImposto(vlBaseCalculo);
		nfs.setTpCalculoPreco(new DomainValue(ConstantesExpedicao.CALCULO_NORMAL));
		nfs.setBlPrioridadeCarregamento(Boolean.FALSE);
		nfs.setVlLiquido(vlDevido);
		if(idDivisaoClienteTomador != null) {
			DivisaoCliente divisaoCliente = new DivisaoCliente();
			divisaoCliente.setIdDivisaoCliente(idDivisaoClienteTomador);
			nfs.setDivisaoCliente(divisaoCliente);
		}		
		nfs.setMunicipio(municipioExecucao);
		nfs.setTpSituacaoNf(new DomainValue(ConstantesExpedicao.NOTA_FISCAL_SERVICO_SITUACAO_GERADA));
		nfs.setFilial(filialExecucao);
		nfs.setPaisOrigem(SessionUtils.getPaisSessao());
		
		emitirDocumentoService.generateProximoNumero(nfs, filialExecucao.getIdFilial());		
		notaFiscalServicoService.store(nfs);
		
		return nfs;
	}
	
	private void storeServAdicionalDocServ(NotaFiscalServico nfs, ServicoAdicional servicoAdicional) {			
		ServAdicionalDocServ servAdicionalDocServ = new ServAdicionalDocServ();
		servAdicionalDocServ.setDoctoServico(nfs);
		servAdicionalDocServ.setServicoAdicional(servicoAdicional);		
		servAdicionalDocServService.store(servAdicionalDocServ);
	}
	
	private void storeDevedorDocServ(NotaFiscalServico nfs, Cliente clienteTomador) {
		DevedorDocServ devedorDocServ = new DevedorDocServ();
		devedorDocServ.setDoctoServico(nfs);
		devedorDocServ.setCliente(clienteTomador);		
		devedorDocServ.setVlDevido(nfs.getVlLiquido());
		devedorDocServ.setInscricaoEstadual(nfs.getInscricaoEstadualRemetente());
		if(Boolean.TRUE.equals(clienteTomador.getBlCobrancaCentralizada())) {
			devedorDocServ.setFilial(clienteTomador.getFilialByIdFilialCobranca());
		} else {
			devedorDocServ.setFilial(nfs.getFilialByIdFilialOrigem());
		}
		devedorDocServService.store(devedorDocServ);
	}
	
	private void storeDevedorDocServFat(NotaFiscalServico nfs, Cliente clienteTomador, BigDecimal vlDevido) {
		DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
		devedorDocServFat.setDoctoServico(nfs);
		devedorDocServFat.setCliente(clienteTomador);
		devedorDocServFat.setVlDevido(vlDevido);
		devedorDocServFat.setTpSituacaoCobranca(new DomainValue("P")); //PENDENTE
		if(Boolean.TRUE.equals(clienteTomador.getBlCobrancaCentralizada())) {
			devedorDocServFat.setFilial(clienteTomador.getFilialByIdFilialCobranca());
		} else {
			devedorDocServFat.setFilial(nfs.getFilialByIdFilialOrigem());
		}
		if(nfs.getDivisaoCliente() != null) {
			devedorDocServFat.setDivisaoCliente(nfs.getDivisaoCliente());
		} else {
			List<DivisaoCliente> divisoes = divisaoClienteService.findDivisaoClienteByIdServico(
					clienteTomador.getIdCliente(), nfs.getServico().getIdServico());
			if(divisoes != null && divisoes.size() > 0) {
				devedorDocServFat.setDivisaoCliente(divisoes.get(0));
			}
		}
		devedorDocServFatService.store(devedorDocServFat);
	}
	
	private void storeImpostosServico(NotaFiscalServico nfs, List<ImpostoServico> impostos) {
		for(ImpostoServico imposto : impostos) {
			imposto.setNotaFiscalServico(nfs);
		}
		impostoServicoService.storeAll(impostos);
	}
	
	private void storeImpostosCalculados(List<ImpostoCalculado> impostos) {
		if(impostos != null && impostos.size() > 0) {
			impostoCalculadoService.storeAll(impostos);
		}
	}
	
	private void storeParcelaDoctoServico(NotaFiscalServico nfs, Long idParcelaPreco, BigDecimal vlBaseCalculo) {				
		ParcelaDoctoServico parcelaDoctoServico = new ParcelaDoctoServico();
		parcelaDoctoServico.setDoctoServico(nfs);		
		parcelaDoctoServico.setVlParcela(vlBaseCalculo);
		parcelaDoctoServico.setParcelaPreco(new ParcelaPreco(idParcelaPreco));
		parcelaDoctoServicoService.store(parcelaDoctoServico);
	}

	private void storeObservacaoDoctoServico(NotaFiscalServico nfs, List<Map<String,Object>> idsPreFaturaServicoItem) {
		ObservacaoDoctoServico observacaoDoctoServico = new ObservacaoDoctoServico();
		observacaoDoctoServico.setDoctoServico(nfs);
		observacaoDoctoServico.setBlPrioridade(Boolean.FALSE);
		observacaoDoctoServico.setDsObservacaoDoctoServico(buildDsObservacaoDoctoServico(idsPreFaturaServicoItem));
		observacaoDoctoServicoService.store(observacaoDoctoServico);
	}
	
	protected String buildDsObservacaoDoctoServico(List<Map<String,Object>> idsPreFaturaServicoItens){
		StringBuilder dsObservacaoBuilder = new StringBuilder();
		dsObservacaoBuilder.append((String)configuracoesFacade.getValorParametro("OBS_NF_AUTOMATICA"));
		
		for(Map<String, Object> item : idsPreFaturaServicoItens) {			
			Long idPreFaturaServicoItem = (Long)item.get("idPreFaturaServicoItem");
			
			PreFaturaServicoItem preFaturaServicoItem = (PreFaturaServicoItem) preFaturaServicoItemService.findById(idPreFaturaServicoItem);
			
			if(checkDsServicoExist(preFaturaServicoItem) && checkDsObservacaoMaxSize(dsObservacaoBuilder, preFaturaServicoItem.getOrdemServicoItem().getDsServico())){
				dsObservacaoBuilder.append(" ").append(preFaturaServicoItem.getOrdemServicoItem().getDsServico());
			}
		}
		return dsObservacaoBuilder.toString();
	}

	protected boolean checkDsObservacaoMaxSize(StringBuilder dsObservacaoBuilder, String dsServico) {
		if(dsObservacaoBuilder.length() + dsServico.length() < DS_OBSERVACAO_MAX_SIZE){
			return true;
		}
		return false;
	}

	private boolean checkDsServicoExist(PreFaturaServicoItem preFaturaServicoItem) {
		return preFaturaServicoItem != null && preFaturaServicoItem.getOrdemServicoItem()!= null && !StringUtils.isEmpty(preFaturaServicoItem.getOrdemServicoItem().getDsServico());
	}
	
	@SuppressWarnings("unchecked")
	private void storeNotaFiscalServicoDocumentos(NotaFiscalServico nfs, List<Map<String, Object>> idsPreFaturaServicoItens) {
		List<NotaFiscalServicoDocumento> listToStore = new ArrayList<NotaFiscalServicoDocumento>();
		
		for(Map<String, Object> idPreFaturaServicoItem : idsPreFaturaServicoItens) {			
			PreFaturaServicoItem preFaturaServicoItem = new PreFaturaServicoItem();
			preFaturaServicoItem.setIdPreFaturaServicoItem((Long)idPreFaturaServicoItem.get("idPreFaturaServicoItem"));
			
			List<Long> idsDoctosServico = (List<Long>)idPreFaturaServicoItem.get("idsDoctosServico");
			
			if(idsDoctosServico != null && idsDoctosServico.size() > 0) {
				for(Long idDoctoServico : idsDoctosServico) {
					NotaFiscalServicoDocumento nfsDoctoServico = new NotaFiscalServicoDocumento();
					nfsDoctoServico.setNotaFiscalServico(nfs);
					
					DoctoServico doctoServico = new DoctoServico();
					doctoServico.setIdDoctoServico(idDoctoServico);
					nfsDoctoServico.setDoctoServico(doctoServico);
					
					nfsDoctoServico.setPreFaturaServicoItem(preFaturaServicoItem);
					listToStore.add(nfsDoctoServico);
				}
			} else {
				NotaFiscalServicoDocumento nfsDoctoServico = new NotaFiscalServicoDocumento();
				nfsDoctoServico.setNotaFiscalServico(nfs);
				nfsDoctoServico.setPreFaturaServicoItem(preFaturaServicoItem);
				listToStore.add(nfsDoctoServico);
			}
		}						
		
		notaFiscalServicoDocumentoService.storeAll(listToStore);
	}
	
	private void storeEmitirNotaFiscalServicoEletronica(List<NotaFiscalServico> notasFiscaisServico) {
		for(NotaFiscalServico nfs : notasFiscaisServico) {		
			if(ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA.equalsIgnoreCase(
					nfs.getTpNotaFiscalServico().getValue())){							
				nfs.setDhEmissao(JTDateTimeUtils.getDataHoraAtual());
				nfs.setTpSituacaoNf(new DomainValue(ConstantesExpedicao.NOTA_FISCAL_SERVICO_SITUACAO_EMITIDA));
				notaFiscalServicoService.store(nfs);
				notaFiscalEletronicaService.storeNotaFiscalServicoEletronica(nfs);
			}		
		}
	}
	
	private String validateEmissaoNFS(Cliente clienteTomador, Filial filialExecucao,  
			ServicoAdicional servicoAdicional, Long idMunicipioExecucao) {
		
		String nrInscricaoMunicipalTomador = clienteTomador.getPessoa().getNrInscricaoMunicipal();		
		EnderecoPessoa enderecoFilial = filialExecucao.getPessoa().getEnderecoPessoa();
		Long idMunicipioFilial = enderecoFilial.getMunicipio().getIdMunicipio();
		String nrInscricaoMunicipalFilial = filialExecucao.getPessoa().getNrInscricaoMunicipal();
		String sgFilial = filialExecucao.getSgFilial();
		
		TypedFlatMap mapEmiteNota = aliquotaIssMunicipioServService.findEmiteNfServico(
				servicoAdicional.getIdServicoAdicional(), null, idMunicipioFilial, idMunicipioExecucao);	
		
		if(Boolean.FALSE.equals(mapEmiteNota.getBoolean("BlEmiteNota"))) {
			Municipio municipioExecucao = municipioService.findById(idMunicipioExecucao);
			
			throw new BusinessException("LMS-04488", new Object[] {
					servicoAdicional.getDsServicoAdicional().getValue(),
					municipioExecucao.getNmMunicipio(),
					sgFilial});
		}
					
		if(Boolean.TRUE.equals(mapEmiteNota.getBoolean("BlEmiteNFeletronica"))) {
			if(nrInscricaoMunicipalFilial == null) {
				throw new BusinessException("LMS-04379");
			}
							
			if(nrInscricaoMunicipalTomador == null) {
				throw new BusinessException("LMS-04380");
			}
			
			return ConstantesExpedicao.NOTA_FISCAL_SERVICO_ELETRONICA;
		} 
		
		return ConstantesExpedicao.NOTA_FISCAL_SERVICO;
	}
	
	private BigDecimal[] executeCalculoImpostos(Cliente clienteTomador, Filial filialExecucao, Long idServicoAdicional,
			Long idMunicipioExecucao, BigDecimal vlBaseCalculo,	List<ImpostoServico> impostosServico, 
			List<ImpostoCalculado> impostosCalculados) {
					
		String tpPessoaTomador = clienteTomador.getPessoa().getTpPessoa().getValue();
		Long idTomador = clienteTomador.getIdCliente();

		EnderecoPessoa enderecoFilial = filialExecucao.getPessoa().getEnderecoPessoa();
		Long idMunicipioFilial = enderecoFilial.getMunicipio().getIdMunicipio();
		
		if("J".equals(tpPessoaTomador)) {
			executeCalcularImpostosPJ(impostosServico, impostosCalculados, idTomador, idServicoAdicional, vlBaseCalculo);
		}
		
		executeCalcularIss(impostosServico, impostosCalculados, idTomador, idServicoAdicional, 
				idMunicipioFilial, idMunicipioExecucao, vlBaseCalculo);
		
		BigDecimal vlTotalImpostos = BigDecimal.ZERO;
		BigDecimal vlSemRetencao = BigDecimal.ZERO;
		
		for(ImpostoServico impostoServico : impostosServico) {
			vlTotalImpostos = vlTotalImpostos.add(impostoServico.getVlImposto());
			if(Boolean.FALSE.equals(impostoServico.getBlRetencaoTomadorServico())) {
				vlSemRetencao = impostoServico.getVlImposto();
			}
		}
		
		return new BigDecimal[]{vlBaseCalculo.subtract(vlTotalImpostos).add(vlSemRetencao), vlTotalImpostos};
	}
	
	private void executeCalcularImpostosPJ(List<ImpostoServico> impostosServico, List<ImpostoCalculado> impostosCalculados,
			Long idCliente, Long idServicoAdicional, BigDecimal vlBaseCalculo) {
		
		List<CalcularPisCofinsCsllIrInss> impostos = calcularPisCofinsCsllIrInssService.calcularPisCofinsCsllIrInssPessoaJudirica(
				idCliente, null, "OU", idServicoAdicional, null, JTDateTimeUtils.getDataAtual(), vlBaseCalculo);		
		
		ImpostoServico impostoServico = null;
		for(CalcularPisCofinsCsllIrInss imposto : impostos) {						
			BigDecimal vlImposto = imposto.getVlImposto();
			DomainValue tpImposto = configuracoesFacade.getDomainValue(
					"DM_TIPO_IMPOSTO", imposto.getTpImpostoCalculado());
			
			impostoServico = new ImpostoServico();
			impostoServico.setVlImposto(vlImposto);
			impostoServico.setVlBaseCalculo(imposto.getVlBaseCalculo());
			impostoServico.setPcAliquota(imposto.getPcAliquotaImposto());			
			impostoServico.setTpImposto(tpImposto);
			impostoServico.setVlBaseEstorno(imposto.getVlBaseEstorno());
			impostoServico.setBlRetencaoTomadorServico(Boolean.TRUE);
			impostosServico.add(impostoServico);
			if( imposto.getImpostoCalculado() != null ){
				impostosCalculados.add(imposto.getImpostoCalculado());
			}
		}
	}
	
	private void executeCalcularIss(List<ImpostoServico> impostosServico, List<ImpostoCalculado> impostosCalculados,
			Long idCliente, Long idServicoAdicional, Long idMunicipioFilial, Long idMunicipioExecucao, 
			BigDecimal vlBaseCalculo) {
		Map<String, Object> map = calcularIssService.calcularIss(
				idCliente,
				idMunicipioFilial,
				idMunicipioExecucao,
				idServicoAdicional,
				null,
				JTDateTimeUtils.getDataAtual(),
				vlBaseCalculo
		);
		
		if(map != null) {
			BigDecimal vlImposto = (BigDecimal) map.get("vlIss");
			DomainValue tpImposto = configuracoesFacade.getDomainValue("DM_TIPO_IMPOSTO", ConstantesExpedicao.CD_ISS);
			
			ImpostoServico impostoServico = new ImpostoServico();
			impostoServico.setPcAliquota((BigDecimal) map.get("pcAliquotaIss"));			
			impostoServico.setTpImposto(tpImposto);
			impostoServico.setVlImposto(vlImposto);
			impostoServico.setVlBaseCalculo(vlBaseCalculo);
			impostoServico.setVlBaseEstorno(vlBaseCalculo);
			impostoServico.setBlRetencaoTomadorServico((Boolean) map.get("blRetencaoTomador"));
			Municipio municipio = new Municipio();
			municipio.setIdMunicipio((Long) map.get("idMunicipioIncidencia"));
			impostoServico.setMunicipioByIdMunicipioIncidencia(municipio);
			impostosServico.add(impostoServico);			
		}
	}
	
	private Map<Long, Boolean> findServicosAdicionaisClienteNaoAgrupar(Long idDivisaoCliente, Long idServico) {		
		Long idTabelaCliente = null;
		Map<Long, Boolean> mapToReturn = new HashMap<Long, Boolean>();
		
		if(idDivisaoCliente != null) {
			List<TabelaDivisaoCliente> tabelasDivisaoCliente = tabelaDivisaoClienteService.findByDivisaoCliente(idDivisaoCliente);
		
			if(tabelasDivisaoCliente != null && tabelasDivisaoCliente.size() > 0) {
				for(TabelaDivisaoCliente tabelaDivisaoCliente : tabelasDivisaoCliente) {
					if(idTabelaCliente == null || tabelaDivisaoCliente.getServico().getIdServico().equals(idServico)) {
						idTabelaCliente = tabelaDivisaoCliente.getTabelaPreco().getIdTabelaPreco();
					}
				}
			}
			
			if(idTabelaCliente != null) {			
				List<ServicoAdicionalCliente> servicosAdicionaisCliente = 
						servicoAdicionalClienteService.findByTabelaDivisaoCliente(idTabelaCliente, idDivisaoCliente);
				
				if(servicosAdicionaisCliente != null && servicosAdicionaisCliente.size() > 0) {
					for(ServicoAdicionalCliente servicoAdicionalCliente : servicosAdicionaisCliente) {
						if(Boolean.TRUE.equals(servicoAdicionalCliente.getBlSeparaDocumentosNfs())) {
							mapToReturn.put(servicoAdicionalCliente.getParcelaPreco().getIdParcelaPreco(), Boolean.TRUE);
						}
					}
				}
			}
		}	
	
		return mapToReturn;
	}
	
	public Boolean validateFilialUsuario(Long idFilialCobranca) {
		Filial filialCobranca = new Filial();
		filialCobranca.setIdFilial(idFilialCobranca);
		if (SessionUtils.isFilialAllowedByUsuario(filialCobranca)) {
			return true;
		}
		return false;
	}
	
	public PreFaturaServico findById(Long id) {
		return (PreFaturaServico) super.findById(id);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginated(PaginatedQuery paginatedQuery) {
		ResultSetPage<Map<String, Object>> retorno = getPreFaturaServicoDAO().findPaginated(paginatedQuery);
		for (Map<String, Object> map : retorno.getList()) {
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao((DomainValue)map.get("tpIdentificacao"), map.get("nrIdentificacao").toString()));
		}
		return retorno;
	}
		
	public Integer getRowCount(TypedFlatMap criteria) {		
		return getPreFaturaServicoDAO().getRowCount(criteria);
	}

	public ResultSetPage<Map<String, Object>> findPaginatedGeracaoPreFatura(PaginatedQuery paginatedQuery) {		
		/* Caso não tenha sido informado tomador */
		if(paginatedQuery.getCriteria().get("idCliente") == null) {
			/* Se foi informado uma filial de cobrança na tela, filtra por ela
			 * caso contrário filtrar pelas filiais que o usuário tem acesso */
			List<Long> idsFiliaisUsuario = null;			
			if(paginatedQuery.getCriteria().get("idFilialCobranca") != null) {
				idsFiliaisUsuario = new ArrayList<Long>();
				idsFiliaisUsuario.add((Long)paginatedQuery.getCriteria().get("idFilialCobranca"));
			} else {
				idsFiliaisUsuario = SessionUtils.getIdsFiliaisUsuarioLogado();
			}
			
			paginatedQuery.addCriteria("idsFiliaisUsuario", idsFiliaisUsuario);
		}
		
		// LMSA-3470 Inclusão dos Ids para busca restrição dos serviços adicionais
		if(paginatedQuery.getCriteria().containsKey("blExcetoServicosParametrizados") && (Boolean) paginatedQuery.getCriteria().get("blExcetoServicosParametrizados")) {
			paginatedQuery.addCriteria("idsPerfisServicosAdicionais", this.buscarIdsPerfisServicosAdicionais());
		}
		
		return getPreFaturaServicoDAO().findPaginatedGeracaoPreFatura(paginatedQuery);
	}
	
	/**
	 * LMSA-3470 - 10/04/2018
	 * Método responsável por consultar os ids dos serviços adicionais
	 * que serão exibidos de acordo com o perfil do usuário.
	 * @return Lista Contendendo os Ids.
	 */
	private List<Long> buscarIdsPerfisServicosAdicionais(){
		
		List<Long> listIsd = new ArrayList<Long> ();
		String parametroGeral = parametroGeralService.findByNomeParametro("SERVICO_ADICIONAL_PERFIL").getDsConteudo();
		
		if(parametroGeral != null && !parametroGeral.isEmpty()) {
			String [] ids = parametroGeral.split(";");
			for (String id : ids) {
				listIsd.add(Long.valueOf(id));
			}
		}
		return listIsd;
	}
	
	@SuppressWarnings("unchecked")
	private void executeSendMail(List<NotaFiscalServico> listaNotas) {
		String textoPreFaturaDoctos = configuracoesFacade.getMensagem("textoPreFaturaDoctos");
		
		Map<Long, Map<String, Object>> grupoNotas = agruparNotasPorFilial(listaNotas);
		
		final String dsRemetente = (String) configuracoesFacade.getValorParametro("REMETENTE_EMAIL_LMS");
		final String dsAssunto = configuracoesFacade.getMensagem("assuntoPreFaturaEmissaoNotas");
		
		for (Map<String, Object> notasFiscalServico : grupoNotas.values()) {
			Filial filial = (Filial) notasFiscalServico.get("filial");
			List<NotaFiscalServico> notas = (List<NotaFiscalServico>) notasFiscalServico.get("notas");
			StringBuilder doctos = formatarNotas(notas);
			StringBuilder conteudo = new StringBuilder();
			conteudo.append(textoPreFaturaDoctos);
			conteudo.append("\n \n ");
			conteudo.append(doctos);
		
			Mail mail = createMail(
				StringUtils.join(montarListaContatos(filial), ";"), 
				dsRemetente, 
				dsAssunto, 
				conteudo.toString()
			);
			
			JmsMessageSender msg = integracaoJmsService.createMessage(Queues.MAIL_SENDER_SERVICE_FEDEX, mail);
			integracaoJmsService.storeMessage(msg);
		}
	}
	
	private Mail createMail(String strTo, String strFrom, String strSubject, String body) {
		Mail mail = new Mail();
		mail.setContentType(TEXT_HTML);
		mail.setFrom(strFrom);
		mail.setTo(strTo);
		mail.setSubject(strSubject);
		mail.setBody(body);
		return mail;
	}

	@SuppressWarnings("unchecked")
	private String[] montarListaContatos(Filial filial) {
		List<Contato> contatos = filial.getPessoa().getContatosByIdPessoaContatado();
		List<String> cont = new ArrayList<String>();

		for (Contato contato : contatos) {
			if (ConstantesConfiguracoes.TP_CONTATO_GERENTE.equals(contato.getTpContato().getValue())
					|| ConstantesConfiguracoes.TP_CONTATO_COBRANCA.equals(contato.getTpContato().getValue())) {
				cont.add(contato.getDsEmail());
			}
		}
		if (cont.isEmpty()) {  
			cont.add((String) configuracoesFacade.getValorParametro("EMAIL_SERV_AD"));
		}
		return cont.toArray(new String[cont.size()]);
	}

	private StringBuilder formatarNotas(List<NotaFiscalServico> notasFiscalServico) {
		StringBuilder doctos = new StringBuilder();
		for (NotaFiscalServico notaFiscalServico : notasFiscalServico) {
			DomainValue domainValue = configuracoesFacade.getDomainValue("DM_TIPO_DOCUMENTO_SERVICO", notaFiscalServico.getTpDocumentoServico().getValue()); 
			doctos.append(domainValue.getDescriptionAsString());
			doctos.append(" ");
			doctos.append(notaFiscalServico.getFilialByIdFilialOrigem().getSgFilial());
			doctos.append(" ");
			doctos.append(FormatUtils.formataNrDocumento(notaFiscalServico.getNrDoctoServico().toString(), "NFS"));
			doctos.append("\n ");
		}
		return doctos;
	}

	@SuppressWarnings("unchecked")
	private Map<Long, Map<String, Object>> agruparNotasPorFilial(List<NotaFiscalServico> listaNotas) {
		Map<Long, Map<String, Object>> grupoNotas = new HashMap<Long, Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (NotaFiscalServico notaFiscalServico : listaNotas) {
			Filial filial = notaFiscalServico.getFilialByIdFilialOrigem();
			Long idFilial = filial.getIdFilial();
			List<NotaFiscalServico> notas;
			if (grupoNotas.containsKey(idFilial)) {
				map = grupoNotas.get(idFilial);
				notas = (List<NotaFiscalServico>) map.get("notas");
			} else {
				notas = new ArrayList<NotaFiscalServico>();
				map = new HashMap<String, Object>();
				map.put("filial", filial);
			}
			notas.add(notaFiscalServico);
			map.put("notas", notas);
			grupoNotas.put(idFilial, map);
		}
		return grupoNotas;
	}
	
	public Integer getRowCountGeracaoPreFatura(TypedFlatMap criteria) {
		return getPreFaturaServicoDAO().getRowCountGeracaoPreFatura(criteria);
	}
	
	public ResultSetPage<Map<String, Object>> findPaginatedGeracaoPreFaturaDetalhamento(PaginatedQuery paginatedQuery) {
		// LMSA-3470 Inclusão dos Ids para busca restrição dos serviços adicionais
		if(paginatedQuery.getCriteria().containsKey("blExcetoServicosParametrizados") && (Boolean) paginatedQuery.getCriteria().get("blExcetoServicosParametrizados")) {
			paginatedQuery.addCriteria("idsPerfisServicosAdicionais", this.buscarIdsPerfisServicosAdicionais());
		}
		return getPreFaturaServicoDAO().findPaginatedGeracaoPreFaturaDetalhamento(paginatedQuery);
	}
	
	public List<Map<String, Object>> findGeracaoPlanilhaServicosAdicionaisGerados(Map<String, Object> criteria) {
		return getPreFaturaServicoDAO().findGeracaoPlanilhaServicosAdicionaisGerados(criteria);
	}
	
	public void setPreFaturaServicoDAO(PreFaturaServicoDAO preFaturaServicoDAO) {
		setDao( preFaturaServicoDAO );
	}

	private PreFaturaServicoDAO getPreFaturaServicoDAO() {
		return (PreFaturaServicoDAO) getDao();
	}	

	public void setAliquotaIssMunicipioServService(AliquotaIssMunicipioServService aliquotaIssMunicipioServService) {
		this.aliquotaIssMunicipioServService = aliquotaIssMunicipioServService;
	}
	
	public void setCalcularIssService(CalcularIssService calcularIssService) {
		this.calcularIssService = calcularIssService;
	}

	public void setCalcularPisCofinsCsllIrInssService(CalcularPisCofinsCsllIrInssService calcularPisCofinsCsllIrInssService) {
		this.calcularPisCofinsCsllIrInssService = calcularPisCofinsCsllIrInssService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}	
	
	public void setDevedorDocServFatService(DevedorDocServFatService devedorDocServFatService) {
		this.devedorDocServFatService = devedorDocServFatService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}
	
	public void setDivisaoClienteService(DivisaoClienteService divisaoClienteService) {
		this.divisaoClienteService = divisaoClienteService;
	}

	public void setEmitirDocumentoService(EmitirDocumentoService emitirDocumentoService) {
		this.emitirDocumentoService = emitirDocumentoService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	public void setImpostoCalculadoService(ImpostoCalculadoService impostoCalculadoService) {
		this.impostoCalculadoService = impostoCalculadoService;
	}

	public void setImpostoServicoService(ImpostoServicoService impostoServicoService) {
		this.impostoServicoService = impostoServicoService;
	}

	public void setInscricaoEstadualService(InscricaoEstadualService inscricaoEstadualService) {
		this.inscricaoEstadualService = inscricaoEstadualService;
	}
	
	public void setNotaFiscalEletronicaService(NotaFiscalEletronicaService notaFiscalEletronicaService) {
		this.notaFiscalEletronicaService = notaFiscalEletronicaService;
	}

	public void setNotaFiscalServicoDocumentoService(NotaFiscalServicoDocumentoService notaFiscalServicoDocumentoService) {
		this.notaFiscalServicoDocumentoService = notaFiscalServicoDocumentoService;
	}

	public void setNotaFiscalServicoService(NotaFiscalServicoService notaFiscalServicoService) {
		this.notaFiscalServicoService = notaFiscalServicoService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	
	public void setObservacaoDoctoServicoService(ObservacaoDoctoServicoService observacaoDoctoServicoService) {
		this.observacaoDoctoServicoService = observacaoDoctoServicoService;
	}

	public void setOrdemServicoItemService(OrdemServicoItemService ordemServicoItemService) {
		this.ordemServicoItemService = ordemServicoItemService;
	}

	public void setOrdemServicoService(OrdemServicoService ordemServicoService) {
		this.ordemServicoService = ordemServicoService;
	}
	
	public void setParcelaDoctoServicoService(ParcelaDoctoServicoService parcelaDoctoServicoService) {
		this.parcelaDoctoServicoService = parcelaDoctoServicoService;
	}

	public void setPpeService(PpeService ppeService) {
		this.ppeService = ppeService;
	}

	public void setPreFaturaServicoItemService(PreFaturaServicoItemService preFaturaServicoItemService) {
		this.preFaturaServicoItemService = preFaturaServicoItemService;
	}
	
	public void setServAdicionalDocServService(ServAdicionalDocServService servAdicionalDocServService) {
		this.servAdicionalDocServService = servAdicionalDocServService;
	}
	
	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}

	public void setServicoAdicionalService(ServicoAdicionalService servicoAdicionalService) {
		this.servicoAdicionalService = servicoAdicionalService;
	}

	public void setServicoGeracaoAutomaticaService(ServicoGeracaoAutomaticaService servicoGeracaoAutomaticaService) {
		this.servicoGeracaoAutomaticaService = servicoGeracaoAutomaticaService;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public List<Map<String, Object>> findDadosRelatorioReceitaPotencial(Map<String, Object> criteria) {
		return getPreFaturaServicoDAO().findDadosRelatorioReceitasPotenciais(criteria);
	}
	
	
}
