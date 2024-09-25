package com.mercurio.lms.contasreceber.model.service;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.dao.FaturaDAO;
import jersey.repackaged.com.google.common.collect.Lists;

import java.util.List;

@Assynchronous
public class EnviarFaturasNegativacaoSerasaService extends CrudService<Fatura, Long> {
	private EnviarFaturasNegativacaoSerasaProcessarFaturasService processarFaturasService;
	private ParametroGeralService parametroGeralService;
	private FaturaDAO faturaDao;
	private ConfiguracoesFacade configuracoesFacade;

	@AssynchronousMethod(name = "contasreceber.enviarFaturasNegativacaoSerasa", type = BatchType.BATCH_SERVICE, feedback = BatchFeedbackType.ON_ERROR)
	public void executeEnviarFaturas() {
		Integer quantidadeItensPorFatura = Integer.valueOf(parametroGeralService.findByNomeParametro("QTDE_ITENS_LOTE_FATURA_NEGATI_SERASA").getDsConteudo());
		Long idMotivoProibidoEmbarque = Long.valueOf(parametroGeralService.findByNomeParametro("ID_MOTIVO_CLIENTE_INADIMPLENTE").getDsConteudo());
		String dsBloqueio = configuracoesFacade.getMensagem("LMS-36316");
		List<Object[]> faturas = faturaDao.findFaturasNegativacaoSerasa();

		List<List<Object[]>> lotesFaturas = Lists.partition(faturas, quantidadeItensPorFatura);

		if (lotesFaturas != null) {
			for (List<Object[]> loteFatura : lotesFaturas) {
				try {
					processarFaturasService.executeEnviarFaturas(loteFatura, idMotivoProibidoEmbarque, dsBloqueio);
				} catch (Exception e) {
					log.error("Erro ao executar enviarFaturasNegativacaoSerasa", e);
				}
			}
		}
	}

	public EnviarFaturasNegativacaoSerasaProcessarFaturasService getProcessarFaturasService() {
		return processarFaturasService;
	}

	public void setProcessarFaturasService(EnviarFaturasNegativacaoSerasaProcessarFaturasService processarFaturasService) {
		this.processarFaturasService = processarFaturasService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public FaturaDAO getFaturaDao() {
		return faturaDao;
	}

	public void setFaturaDao(FaturaDAO faturaDao) {
		this.faturaDao = faturaDao;
		setDao(faturaDao);
	}

	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
