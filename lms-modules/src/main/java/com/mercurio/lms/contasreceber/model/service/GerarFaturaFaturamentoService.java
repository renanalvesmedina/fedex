package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.DevedorDocServFat;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.ItemFatura;

/**
 * Classe de serviço para CRUD:
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaFaturamentoService"
 */
public class GerarFaturaFaturamentoService extends GerarFaturaService {
	private static final Log logger = LogFactory.getLog(GerarFaturaFaturamentoService.class);

	protected Fatura beforeStore(Fatura fatura) {
		fatura = setValorDefault(fatura);

		faturaService.generateProximoNumero(fatura);

		fatura.setQtDocumentos(Integer.valueOf(0));

		fatura.setVlTotal(new BigDecimal(0));

		fatura.setVlDesconto(new BigDecimal(0));

		fatura = setDtVencimento(fatura);
		
		return fatura;
	}
	
	protected void setValorDefaultSpecific(Fatura fatura) {
		fatura.setTpOrigem(new DomainValue("A"));
	}

	protected Fatura store(Fatura fatura) {
		fatura = beforeStore(fatura);
		faturaService.storeBasic(fatura);
		return fatura;
	}
	
	
	/**
	 * 
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Fatura storeFaturaWithIdsDevedorDocServFat(Fatura fatura, List lstDevedorDocServFat) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		String inicioStore = sdf.format(Calendar.getInstance().getTime());
		fatura = store(fatura);
		String fimStore = sdf.format(Calendar.getInstance().getTime());

		String inicioStoreDevedorDocServFat = sdf.format(Calendar.getInstance().getTime());
		List lstItemFatura = this.storeDevedorDocServFat(fatura,lstDevedorDocServFat);
		String fimStoreDevedorDocServFat = sdf.format(Calendar.getInstance().getTime());

		String inicioAfterStore = sdf.format(Calendar.getInstance().getTime());
		fatura = this.afterStore(fatura, lstItemFatura);
		String fimAfterStore = sdf.format(Calendar.getInstance().getTime());

		logger.error("[Faturamento automático] storeFaturaWithIdsDevedorDocServFat -> " +
				"Início store: " + inicioStore + ". " +
				"Fim store: " + fimStore + ". " +
				"Início storeDevedorDocServFat: " + inicioStoreDevedorDocServFat + ". " +
				"Fim storeDevedorDocServFat: " + fimStoreDevedorDocServFat + ". " +
				"Início afterStore: " + inicioAfterStore + ". " +
				"Fim fimAfterStore: " + fimAfterStore + ".");

		return fatura;
	}
	
	protected List storeDevedorDocServFat(Fatura fatura,List lstDevedorDocServFat) {
		List lstItemFatura = new ArrayList();

		for (Iterator iter = lstDevedorDocServFat.iterator(); iter.hasNext();) {
			DevedorDocServFat devedorDocServFat = new DevedorDocServFat();
			devedorDocServFat.setIdDevedorDocServFat((Long) iter.next());
			
			ItemFatura itemFatura = new ItemFatura();
			itemFatura.setDevedorDocServFat(devedorDocServFat);
			itemFatura.setFatura(fatura);
			
			lstItemFatura.add(itemFatura);
		}

		faturaService.storeItemFaturaBasic(lstItemFatura);
		
		fatura.setItemFaturas(lstItemFatura);
		
		return lstItemFatura;
	}	
	
	protected void storeItemFatura(Fatura fatura, List lstItemFatura) {
		faturaService.storeItemFatura(lstItemFatura);			
	}
	
	protected Fatura afterStore(Fatura fatura, List lstItemFatura) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

		String inicioExecuteUpdateSituacao = sdf.format(Calendar.getInstance().getTime());
		devedorDocServFatService.executeUpdateSituacaoByIdFatura(fatura.getIdFatura(), "F");
		String fimExecuteUpdateSituacao = sdf.format(Calendar.getInstance().getTime());

		String inicioCalculateSomatorioFatura = sdf.format(Calendar.getInstance().getTime());
		fatura = calculateSomatorioFatura(fatura);
		String fimCalculateSomatorioFatura = sdf.format(Calendar.getInstance().getTime());

		String inicioStoreBasic = sdf.format(Calendar.getInstance().getTime());
		faturaService.storeBasic(fatura);
		String fimStoreBasic = sdf.format(Calendar.getInstance().getTime());

		String inicioGenerateBoleto = sdf.format(Calendar.getInstance().getTime());
		generateBoleto(fatura);
		String fimGenerateBoleto = sdf.format(Calendar.getInstance().getTime());

		logger.error("[Faturamento automático] afterStore -> " +
				"Início executeUpdateSituacao: " + inicioExecuteUpdateSituacao + ". " +
				"Fim executeUpdateSituacao: " + fimExecuteUpdateSituacao + ". " +
				"Início inicioCalculateSomatorioFatura: " + inicioCalculateSomatorioFatura + ". " +
				"Fim fimCalculateSomatorioFatura: " + fimCalculateSomatorioFatura + ". " +
				"Início inicioStoreBasic: " + inicioStoreBasic + ". " +
				"Fim fimStoreBasic: " + fimStoreBasic + ". " +
				"Início inicioGenerateBoleto: " + inicioGenerateBoleto + ". " +
				"Fim fimGenerateBoleto: " + fimGenerateBoleto + ". " +
				"");
		return fatura;
	}
	
	/**
	 * Gera um boleto para a fatura informada.
	 * 
	 * Regra 5.4
	 */
	protected void generateBoleto(Fatura fatura) {
		if (fatura.getBlGerarBoleto().equals(Boolean.TRUE) && fatura.getCedente() != null) {
			// Gera boleto
			boletoService.generateBoletoDeFaturamento(fatura);			
		}
	}	
}
