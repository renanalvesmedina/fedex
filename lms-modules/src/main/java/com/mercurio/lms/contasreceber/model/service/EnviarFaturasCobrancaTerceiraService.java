package com.mercurio.lms.contasreceber.model.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemLoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.LoteCobrancaTerceira;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import com.mercurio.lms.util.session.SessionUtils;

@Assynchronous
public class EnviarFaturasCobrancaTerceiraService extends CrudService<Fatura, Long> {

	private FaturaDAO faturaDao;	
	private ItemLoteCobrancaTerceiraService itemLoteCobrancaTerceiraService;
	private LoteCobrancaTerceiraService loteSerasaService;
	private HistoricoBoletoService historicoBoletoService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	
	public void setFaturaDao(FaturaDAO faturaDao) {
		this.faturaDao = faturaDao;
		setDao(faturaDao);
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	
	public FaturaDAO getFaturaDao() {
		return faturaDao;
	}
	
	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}
	
	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}

	@AssynchronousMethod(name = "contasreceber.enviarFaturasCobrancaTerceiraService", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeEnviarFaturas() {
		executeEnviarFaturasEmail();
	}
	
	void executeEnviarFaturasEmail() {
		List<Object[]> faturas = faturaDao.findFaturasCobrancaTerceira();
		if ( faturas == null || faturas.isEmpty() ){
			return;
		}
		LoteCobrancaTerceira loteCobranca = storeLoteCobrancaTerceira();
		for(Object[] fatura : faturas){
			Long idFatura = (Long) fatura[0];
			storeItemLoteCobranca(loteCobranca, idFatura);
		}
		
	}

	private LoteCobrancaTerceira storeLoteCobrancaTerceira() {
		LoteCobrancaTerceira loteSerasa = new LoteCobrancaTerceira();
		loteSerasa.setTpLote(new DomainValue("E"));
		loteSerasa.setDsLote(configuracoesFacade.getMensagem("LMS-36334",new Object[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) }));
		UsuarioLMS usuario = new UsuarioLMS();
		usuario.setIdUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		loteSerasa.setUsuario(usuario);
		loteSerasa.setDhAlteracao(new DateTime());
		
		Long nrLote = parametroGeralService.generateValorParametroSequencial("NR_LOTE_COBRANCA_TERCEIRA", false, 1);
		loteSerasa.setNrLote(""+nrLote);
		loteSerasa = loteSerasaService.storeLotecobranca(loteSerasa);
		return loteSerasa;
	}

	boolean existeBloqueioEmbarque(int embarques) {
		return embarques > 0;
	}
	
	boolean isPessoaFisica(Pessoa pessoa) {
		return pessoa.getTpPessoa().getValue().equalsIgnoreCase("F");
	}

	void storeItemLoteCobranca(LoteCobrancaTerceira loteCobranca,Long idFatura) {
		ItemLoteCobrancaTerceira item = new ItemLoteCobrancaTerceira();
		item.setLoteCobrancaTerceira(loteCobranca);
		item.setFatura(faturaDao.findByIdFatura(idFatura));
		itemLoteCobrancaTerceiraService.store(item);
	}
	
	public void setLoteSerasaService(LoteCobrancaTerceiraService loteSerasaService) {
		this.loteSerasaService = loteSerasaService;
	}
	
	public void setItemLoteCobrancaTerceiraService(ItemLoteCobrancaTerceiraService itemLoteCobrancaTerceiraService) {
		this.itemLoteCobrancaTerceiraService = itemLoteCobrancaTerceiraService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
