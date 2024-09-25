package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.OcorrenciaBanco;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.workflow.model.service.PendenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.efetivarFaturaDescontoService"
 */
public class EfetivarDescontoFaturaService {
	
	private DescontoService descontoService;
	
	private FaturaService faturaService;
	
	private BoletoService boletoService;
	
	private OcorrenciaBancoService ocorrenciaBancoService;
	
	private HistoricoBoletoService historicoBoletoService;
	
	private PendenciaService pendenciaService;
	
	private GerarFaturaBoletoService gerarFaturaService;
	
	/**
	 * Recupera uma instância de <code>Fatura</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public void executeWorkflow(List idsProcesso, List tpsSituacao) {
        if (idsProcesso == null || tpsSituacao == null || idsProcesso.size() != tpsSituacao.size()){
        	return;
        }
        //TODO
        Fatura fatura = faturaService.findByIdWithBoleto((Long)idsProcesso.get(0)); 
        faturaService.evict(fatura);
        Boleto boleto = fatura.getBoleto();
        
        fatura.setTpSituacaoAprovacao(new DomainValue((String)tpsSituacao.get(0)));
        // Caso seja aprovado.
		if ( "A".equals(fatura.getTpSituacaoAprovacao().getValue())){
        	if (boleto != null){
        		storeBoleto(boleto, fatura, fatura.getIdPendenciaDesconto(), fatura.getVlDesconto());	
        	}
        	fatura.setDhTransmissao(null);
        	storeFatura(fatura); 
        	descontoService.updateDescontoByIdFatura(fatura.getIdFatura(), "A");
       
        // Caso seja reprovado.
        } else if ( "R".equals(fatura.getTpSituacaoAprovacao().getValue()) ) {

        	// Caso exista o boleto, zerar o desconto do mesmo.
        	if (boleto != null){
        		boleto.setVlDesconto(BigDecimal.ZERO);
        		boletoService.storeBasic(boleto);
        	}     
        	// Caso a fatura esteja em boleto.
        	if ( "BL".equals(fatura.getTpSituacaoFatura().getValue()) ) {
	        	// Cancela os historicos com ocorrência 4 e gera a ocorrência 5 se necessário.
        		historicoBoletoService.cancelHistoricoBoletoAbatimento(boleto);
        	}
        	storeFatura(fatura); 
        	
        }
		
		
    }
    
    /**
     * Atualiza o valor de desconto e a data de transmissão.
     * 
     * @param Fatura fatura
     * @param BigDecimal vlDesconto
     * @return BigDecimal
     * */
    private void storeFatura(Fatura fatura) {
    	faturaService.store(fatura);
    }
    
    /**
     * @see storeBoleto(Boleto boleto, Fatura fatura, Long idPendencia)
     * */
    public void storeBoleto(Boleto boleto, Fatura fatura, BigDecimal vlDesconto) {
    	storeBoleto(boleto, fatura, null, vlDesconto);
    }     
    
    /**
     * Atualiza o valor de desconto e a data de vencimento do boleto.
     * Caso o boleto está em data, vai ser atualizado o campo valor de juros da fatura.
     * CUIDADO!! Esse método troca o valor da instância da fatura!
     * 
     * @param Boleto boleto
     * @param Fatura fatura
     * @param BigDecimal vlDesconto
     * @return BigDecimal
     * */
    public void storeBoleto(Boleto boleto, Fatura fatura, Long idPendencia, BigDecimal vlDesconto) {
    	boleto.setVlDesconto(vlDesconto); 
    	
    	// Incrementa 7 dias na data atual
    	YearMonthDay date = JTDateTimeUtils.getDataAtual().plusDays(7);
    		
    	if(idPendencia != null){
    		DoctoServico ds =  gerarFaturaService.getFirstDoctoServicoFromFatura(fatura, fatura.getItemFaturas());
    		boolean isQuestionamentoFatura = true;
    	// Se o BOLETO.DT_VENCIMENTO for menor à DATA_ATUAL + 7 dias
	    	if (JTDateTimeUtils.comparaData(boleto.getDtVencimento(),date) < 0){
    		fatura.setVlJuroCalculado(new BigDecimal(0));
    		fatura.setDtVencimento(date);
    		boleto.setDtVencimento(date);
    		
    		// Caso a situação do boleto seja BN ou BP, gera um histórico boleto com a ocorrência número 6 
    		if (boleto.getTpSituacaoBoleto().getValue().equals("BN") || boleto.getTpSituacaoBoleto().getValue().equals("BP")){
	    			this.storeHistoricoBoleto(boleto, idPendencia, JTDateTimeUtils.getDataHoraAtual(),Short.valueOf("6"), isQuestionamentoFatura );
    		}
    		
    	}
    	
    	//Se o BOLETO.TP_SITUACAO_BOLETO seja (“BN”, ”BP”)
	    	if (boleto.getTpSituacaoBoleto().getValue().equals("BN") || boleto.getTpSituacaoBoleto().getValue().equals("BP")){
	    		this.storeHistoricoBoleto(boleto, idPendencia, JTDateTimeUtils.getDataHoraAtual().plusSeconds(10),Short.valueOf("4"), isQuestionamentoFatura );
    	}
	    	
	    	
	    	boolean hasTransmissao = CollectionUtils.exists(boleto.getHistoricoBoletos(), new Predicate() {	
				public boolean evaluate(Object arg0) {
					HistoricoBoleto hb = (HistoricoBoleto) arg0;
					return "REM".equals(hb.getOcorrenciaBanco().getTpOcorrenciaBanco().getValue()) && "A".equals(hb.getTpSituacaoHistoricoBoleto().getValue());
    	}
			});
	    	
	    	//Se o BOLETO.TP_SITUACAO_BOLETO seja (“EM”)
	    	if (boleto.getTpSituacaoBoleto().getValue().equals("EM") || !hasTransmissao){
	    		historicoBoletoService.generateHistoricoBoleto(boleto, ConstantesConfiguracoes.CD_OCORRENCIA_RETRANSMITIR, "REM");
	    	}
	    	
    	}
    	boletoService.store(boleto);
    } 
    
    private void storeHistoricoBoleto(Boleto boleto, Long idPendencia, DateTime dhOcorrencia, Short nrOcorrenciaBanco, boolean isQuestionamentoFatura){
    	/*Incluir um registro na tabela HISTORICO_BOLETO com o 
    	TP_SITUACAO_HISTORICO_BOLETO = “A”, DH_OCORRECIA = DATA_HORA_ATUAL + 10 segundos, ID_OCORRENCIA_BANCO = 
    	(Onde ID_BANCO = FATURA.CEDENTE.ID_BANCO e NR_OCORRENCIA_BANCO = 04)*/
    	OcorrenciaBanco ocorrenciaBanco = ocorrenciaBancoService.findByBancoNrOcorrenciaTpOcorrencia(boleto.getCedente().getAgenciaBancaria().getBanco().getIdBanco(), nrOcorrenciaBanco, "REM");
    	
    	// Cancela os historicosBoleto que sejam da mesma ocorrenciaBanco.
    	historicoBoletoService.cancelaHistoricosComOcorrenciaIguais(boleto, nrOcorrenciaBanco);
    	
    	HistoricoBoleto historicoBoleto = new HistoricoBoleto();
    	if(!isQuestionamentoFatura){
    	Usuario usuario = pendenciaService.findSolicitanteByPendencia(idPendencia); 
    		historicoBoleto.setUsuario(usuario);
    	} else{
    		historicoBoleto.setUsuario(SessionUtils.getUsuarioLogado());
    	}
    	historicoBoleto.setBoleto(boleto);
    	historicoBoleto.setDhOcorrencia(dhOcorrencia);
    	historicoBoleto.setTpSituacaoHistoricoBoleto(new DomainValue("A"));
    	historicoBoleto.setOcorrenciaBanco(ocorrenciaBanco);
    	historicoBoletoService.store(historicoBoleto);
    }

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public void setOcorrenciaBancoService(
			OcorrenciaBancoService ocorrenciaBancoService) {
		this.ocorrenciaBancoService = ocorrenciaBancoService;
	}

	public void setHistoricoBoletoService(
			HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public void setPendenciaService(PendenciaService ps) {
		this.pendenciaService = ps;
	}

	public void setGerarFaturaService(GerarFaturaBoletoService gerarFaturaService) {
		this.gerarFaturaService = gerarFaturaService;
	}

	public GerarFaturaBoletoService getGerarFaturaService() {
		return gerarFaturaService;
	}

 }