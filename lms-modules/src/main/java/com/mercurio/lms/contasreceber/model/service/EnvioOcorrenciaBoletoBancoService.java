package com.mercurio.lms.contasreceber.model.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.contasreceber.model.Cedente;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.HistoricoMotivoOcorrencia;
import com.mercurio.lms.contasreceber.model.MotivoOcorrenciaBanco;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.util.JTDateTimeUtils;

public class EnvioOcorrenciaBoletoBancoService {

	private static final Set<String> TP_BOLETOS_BAIXADOS = new HashSet<String>(Arrays.asList(new String[]{ "LI" })); 

	private ParametroGeralService parametroGeralService;
	private EnderecoPessoaService enderecoPessoaService;
	private MotivoOcorrenciaBancoService motivoOcorrenciaBancoService;
	private HistoricoBoletoService historicoBoletoService;
	private HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService;
	private IntegracaoJmsService integracaoJmsService;
	private OcorrenciaBancoService ocorrenciaBancoService;
	private PessoaService pessoaService;
	
	public void execute(HistoricoBoleto historicoBoleto) {
		loadOcorrenciaBanco(historicoBoleto);
		
		if (!validateHistoricoBoletoForEnvioOcorrenciaBoletoBanco(historicoBoleto)) {
			return;
		}

		if (TP_BOLETOS_BAIXADOS.contains(historicoBoleto.getBoleto().getTpSituacaoBoleto().getValue())
				&& !historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco().equals(ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_BAIXA)) {

			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("T"));
		} else if (historicoBoleto.getBoleto().getDtVencimento().compareTo(JTDateTimeUtils.getDataAtual()) <= 0
				&& historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco().equals(ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_ENVIO)) {
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("C"));
			historicoMotivoOcorrenciaService.store(createHistoricoMotivoOcorrencia(historicoBoleto, (short)3, (short)66));
		} else if (historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco().equals(ConstantesConfiguracoes.CD_OCORRENCIA_RETRANSMITIR)) {
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("C"));			
		} else if (historicoBoleto.getOcorrenciaBanco().getNrOcorrenciaBanco().equals(ConstantesConfiguracoes.CD_OCORRENCIA_PEDIDO_PROTESTO)
				&& checkCnpjMercurio(historicoBoleto)) {
			
			historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("C"));
			historicoMotivoOcorrenciaService.store(createHistoricoMotivoOcorrencia(historicoBoleto, (short)3, (short)90));
		}
		
	}

	private HistoricoBoleto loadOcorrenciaBanco(HistoricoBoleto historicoBoleto) {
		if(historicoBoleto.getOcorrenciaBanco().getTpOcorrenciaBanco() == null) {
			OcorrenciaBanco ob = ocorrenciaBancoService.findById(historicoBoleto.getOcorrenciaBanco().getIdOcorrenciaBanco());
			historicoBoleto.setOcorrenciaBanco(ob);
		}
				
		return historicoBoleto;
	}

	

	private boolean checkCnpjMercurio(HistoricoBoleto historicoBoleto) {
		String nrCnpjMercurio = parametroGeralService.findByNomeParametro("NR_CNPJ_MERCURIO").getDsConteudo();
		Pessoa pessoa = pessoaService.findById(historicoBoleto.getBoleto().getFatura().getCliente().getIdCliente());
		return pessoa.getNrIdentificacao().startsWith(nrCnpjMercurio);
	}

	private boolean validateHistoricoBoletoForEnvioOcorrenciaBoletoBanco(HistoricoBoleto historicoBoleto) {
		String blFinanceiroCorporativo = parametroGeralService.findByNomeParametro("BL_FINANCEIRO_CORPORATIVO").getDsConteudo();
		
		return "N".equals(blFinanceiroCorporativo) 
				&& "REM".equals(historicoBoleto.getOcorrenciaBanco().getTpOcorrenciaBanco().getValue()) 
				&& "A".equals(historicoBoleto.getTpSituacaoHistoricoBoleto().getValue()) 
				&& checkSituacaoAprovacaoCondition(historicoBoleto);
	}

	private boolean checkSituacaoAprovacaoCondition(HistoricoBoleto historicoBoleto) {
		DomainValue tpSituacaoAprovacao = historicoBoleto.getTpSituacaoAprovacao();
		
		return tpSituacaoAprovacao == null || tpSituacaoAprovacao.getValue().equals("A");
	}

	private MotivoOcorrenciaBanco findMotivoOcorrencia(Short nrOcorrenciaBanco, Cedente cedente, Short motivoOcorrencia) {
		
		List<MotivoOcorrenciaBanco> motivos = 
				motivoOcorrenciaBancoService.findMotivoOcorrenciaForRetornoBanco(nrOcorrenciaBanco, cedente.getIdCedente(), motivoOcorrencia);
		
		if (!motivos.isEmpty()) {
			return motivos.iterator().next();
		}

		return null;
	}

	private HistoricoMotivoOcorrencia createHistoricoMotivoOcorrencia(
			HistoricoBoleto historicoBoleto, short nrOcorrenciaBanco,
			short motivoOcorrencia) {
		HistoricoMotivoOcorrencia historicoMotivoOcorrencia = new HistoricoMotivoOcorrencia();

		historicoMotivoOcorrencia.setHistoricoBoleto(historicoBoleto);
		historicoMotivoOcorrencia
				.setMotivoOcorrenciaBanco(findMotivoOcorrencia(nrOcorrenciaBanco, historicoBoleto.getBoleto().getCedente(), motivoOcorrencia));

		return historicoMotivoOcorrencia;
	}
	
	public ParametroGeralService getParametroGeralService() {
		return parametroGeralService;
	}
	
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public EnderecoPessoaService getEnderecoPessoaService() {
		return enderecoPessoaService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public MotivoOcorrenciaBancoService getMotivoOcorrenciaBancoService() {
		return motivoOcorrenciaBancoService;
	}
	public void setMotivoOcorrenciaBancoService(MotivoOcorrenciaBancoService motivoOcorrenciaBancoService) {
		this.motivoOcorrenciaBancoService = motivoOcorrenciaBancoService;
	}
	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}
	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public HistoricoMotivoOcorrenciaService getHistoricoMotivoOcorrenciaService() {
		return historicoMotivoOcorrenciaService;
	}
	public void setHistoricoMotivoOcorrenciaService(HistoricoMotivoOcorrenciaService historicoMotivoOcorrenciaService) {
		this.historicoMotivoOcorrenciaService = historicoMotivoOcorrenciaService;
	}
	public IntegracaoJmsService getIntegracaoJmsService() {
		return integracaoJmsService;
	}
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}
	public void setOcorrenciaBancoService(OcorrenciaBancoService ocorrenciaBancoService) {
		this.ocorrenciaBancoService = ocorrenciaBancoService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
}
