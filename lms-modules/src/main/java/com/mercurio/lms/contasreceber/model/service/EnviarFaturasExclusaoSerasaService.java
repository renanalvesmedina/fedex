package com.mercurio.lms.contasreceber.model.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.dao.PessoaDAO;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemLoteSerasa;
import com.mercurio.lms.contasreceber.model.LoteSerasa;
import com.mercurio.lms.contasreceber.model.MotivoBaixaSerasa;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.MotivoProibidoEmbarque;
import com.mercurio.lms.vendas.model.ProibidoEmbarque;
import com.mercurio.lms.vendas.model.dao.ClienteDAO;
import com.mercurio.lms.vendas.model.service.ProibidoEmbarqueService;

@Assynchronous
public class EnviarFaturasExclusaoSerasaService extends CrudService<Fatura, Long> {

	private FaturaDAO faturaDao;	
	private LoteSerasaService loteSerasaService;
	private HistoricoBoletoService historicoBoletoService;
	private ConfiguracoesFacade configuracoesFacade;
	private ParametroGeralService parametroGeralService;
	private MotivoBaixaSerasaService motivoBaixaSerasa;
	
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

	@AssynchronousMethod(name = "contasreceber.enviarFaturasExclusaoSerasa", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeEnviarFaturas() {
		executeEnviarFaturasEmail();
	}
	
	void executeEnviarFaturasEmail() {
		List<Object[]> faturas = faturaDao.findFaturasExclusaoSerasa();
		if ( faturas == null || faturas.isEmpty() ){
			return;
		}
		LoteSerasa loteSerasa = storeLoteSerasa();
		for(Object[] fatura : faturas){
			Long idFatura = (Long) fatura[0];
			String tpSituacaoFatura = (String) fatura[7];
			Integer qtRecPosLiq = (Integer) fatura[13];
			MotivoBaixaSerasa motivo = null;
			if ( "CA".equalsIgnoreCase(tpSituacaoFatura)){
				motivo = motivoBaixaSerasa.findById(Long.valueOf(parametroGeralService.findByNomeParametro("ID_MOTIVO_BAIXA_SERASA_CANC").getDsConteudo()));
			}else if ( qtRecPosLiq > 0 ){
				motivo = motivoBaixaSerasa.findById(Long.valueOf(parametroGeralService.findByNomeParametro("ID_MOTIVO_BAIXA_SERASA_JUDIC").getDsConteudo()));
			}else{
				motivo = motivoBaixaSerasa.findById(Long.valueOf(parametroGeralService.findByNomeParametro("ID_MOTIVO_BAIXA_SERASA_PGTO").getDsConteudo()));
			}
			storeItemLoteSerasa(loteSerasa,motivo, idFatura);
		}
		
	}

	private LoteSerasa storeLoteSerasa() {
		LoteSerasa loteSerasa = new LoteSerasa();
		loteSerasa.setTpLote(new DomainValue("E"));
		loteSerasa.setDsLote(configuracoesFacade.getMensagem("LMS-36323",new Object[] { new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) }));
		loteSerasa = loteSerasaService.store(loteSerasa);
		return loteSerasa;
	}

	boolean existeBloqueioEmbarque(int embarques) {
		return embarques > 0;
	}
	
	boolean isPessoaFisica(Pessoa pessoa) {
		return pessoa.getTpPessoa().getValue().equalsIgnoreCase("F");
	}

	void storeItemLoteSerasa(LoteSerasa loteSerasa,MotivoBaixaSerasa motivo, Long idFatura) {
		ItemLoteSerasa item = new ItemLoteSerasa();
		item.setFatura( faturaDao.findByIdFatura(idFatura) );
		item.setLoteSerasa(loteSerasa);
		item.setMotivoBaixaSerasa(motivo);
		loteSerasaService.store(item);
	}
	
	public void setLoteSerasaService(LoteSerasaService loteSerasaService) {
		this.loteSerasaService = loteSerasaService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setMotivoBaixaSerasa(MotivoBaixaSerasaService motivoBaixaSerasa) {
		this.motivoBaixaSerasa = motivoBaixaSerasa;
	}
}
