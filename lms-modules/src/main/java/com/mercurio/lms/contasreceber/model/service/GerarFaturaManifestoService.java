package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.List;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.util.session.SessionUtils;



/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.contasreceber.gerarFaturaManifestoService"
 */
public class GerarFaturaManifestoService extends GerarFaturaService {
	protected void setValorDefaultSpecific(Fatura fatura){
		fatura.setTpOrigem(new DomainValue("M"));
		fatura.setTpFatura(new DomainValue("R"));
		fatura.setTpSituacaoFatura(new DomainValue("DI"));
    	fatura.setQtDocumentos(Integer.valueOf(1));
    	fatura.setBlGerarEdi(Boolean.TRUE);
    	fatura.setBlGerarBoleto(Boolean.FALSE);		
	}
	
	protected Fatura beforeStore(Fatura fatura) {
		Fatura faturaAnterior = null;

		if (fatura.getIdFatura() == null) {
			fatura = beforeInsert(fatura);
		} else {
			faturaAnterior = faturaService.findByIdDisconnected(fatura.getIdFatura());
		}		
		
		//Se tem cobrança centralizada
		if (fatura.getCliente().getBlCobrancaCentralizada().equals(Boolean.TRUE)) {
			//Se a filial de cobrança for diferente da filial da sessão
			if (!fatura.getCliente().getFilialByIdFilialCobranca().equals(SessionUtils.getFilialSessao())) {
				return null;
			}
		}	

		fatura.setQtDocumentos(Integer.valueOf(0));

		fatura.setVlTotal(new BigDecimal(0));

		fatura.setVlDesconto(new BigDecimal(0));

		fatura = setDtEmissao(fatura, faturaAnterior);

		fatura = setDtVencimento(fatura);

		fatura = setServico(fatura);

		fatura = setCedente(fatura);

		validateDtVencimento(fatura, faturaAnterior);

		validateCotacao(fatura);		

		faturaService.validateFatura(fatura.getIdFatura());

		return fatura;
	}

	protected Fatura store(Fatura fatura) {
		fatura = beforeStore(fatura);
		
		if (fatura == null){
			return null;
		}
		
		faturaService.storeBasic(fatura);
		return fatura;
	}
	
	public Fatura storeFaturaWithIdsDevedorDocServFat(Fatura fatura,List lstDevedorDocServFat) {
		boolean blNovaFatura = (fatura.getIdFatura() == null);

		fatura = prepareFaturaWithDevedorDocServFat(fatura,lstDevedorDocServFat);
		fatura = store(fatura);
		
		if (fatura == null){
			return null;
		}
		
		List lstItemFatura = storeDevedorDocServFat(fatura,lstDevedorDocServFat);
		
		storeItemFatura(fatura, lstItemFatura);
		
		fatura.setItemFaturas(lstItemFatura);
		
		fatura = afterStore(fatura, lstItemFatura, blNovaFatura);

		return fatura;
	}	
}
