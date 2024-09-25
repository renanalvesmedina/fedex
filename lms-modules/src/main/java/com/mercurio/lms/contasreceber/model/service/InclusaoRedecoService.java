package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import br.com.tntbrasil.integracao.domains.financeiro.FaturaDMN;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contasreceber.model.ItemRedeco;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.service.exception.InclusaoRedecoException;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.JTDateTimeUtils;

public class InclusaoRedecoService {
	
	private static final Integer LINHA_INICIAL = 1;
	private static final Long FILIAL_MTZ = 361L;
	private RedecoService redecoService;
	private ConfiguracoesFacade configuracoesFacade;
	private FaturaService faturaService;
	private FilialService filialService;
	private ItemRedecoService itemRedecoService;
	private BoletoService boletoService;

	public Redeco executeIncluirRedeco(List<FaturaDMN> faturas) {
		
		Integer linha = LINHA_INICIAL;
		for(FaturaDMN fatura : faturas){
			
			validateAtributosPreenchidos(fatura, linha);
			
			validateFilial(fatura, linha);
			fatura = populateFilial(fatura);
			
			validateIdFatura(fatura,linha);
			fatura = populateIdFatura(fatura);
			
			validateFaturaEntity(fatura,linha);
			linha++;
		}
		
		Redeco redecoStore = storeRedeco();

		itemRedecoService.storeAll(createItens(faturas, redecoStore));
		
		return redecoStore;
	}

	private List<ItemRedeco> createItens(List<FaturaDMN> faturas, Redeco redecoStore) {
		List<ItemRedeco> itens = new ArrayList<ItemRedeco>();
		for(FaturaDMN fatura : faturas){
			itens.add(createItem(fatura, redecoStore));
		}
		return itens;
	}


	private FaturaDMN populateIdFatura(FaturaDMN fatura) {
		if(fatura.getIdFatura() == null){
			fatura.setIdFatura(findIdFaturaByFilialAndNumeroFatura(fatura));
		}
		return fatura;
	}

	private FaturaDMN populateFilial(FaturaDMN fatura) {
		if(fatura.getIdFilial() == null){
			fatura.setIdFilial(findIdFilialBySgfilial(fatura.getSgFilial()));
		}
		return fatura;
	}

	private Redeco storeRedeco() {
		Redeco redecoStore = new Redeco();
		Filial filial = filialService.findById(FILIAL_MTZ);
		redecoStore.setFilial(filial );
		redecoStore.setVlDiferencaCambialCotacao(BigDecimal.valueOf(0L));
		redecoStore.setDtEmissao(JTDateTimeUtils.getDataAtual());
		redecoStore.setTpSituacaoRedeco(new DomainValue("DI"));
		redecoStore.setTpFinalidade(new DomainValue("CC"));
		redecoStore.setNmResponsavelCobranca(configuracoesFacade.getMensagem("LMS-36367"));
		redecoStore.setTpRecebimento(new DomainValue("CC"));
		redecoStore.setDtRecebimento(JTDateTimeUtils.getDataAtual());
		redecoStore.setObRedeco(configuracoesFacade.getMensagem("LMS-36367"));
		redecoStore.setTpAbrangencia(new DomainValue("N"));
		redecoStore.setMoeda(filial.getMoeda());
		redecoStore.setBlDigitacaoConcluida(new DomainValue("N"));
		
		redecoService.store(redecoStore);
		 
		return redecoStore;
	}

	private ItemRedeco createItem(FaturaDMN fatura, Redeco redecoStore) {
		ItemRedeco item = new ItemRedeco();
		item.setRedeco(redecoStore);
		item.setFatura(faturaService.findById(fatura.getIdFatura()));
		item.setVlTarifa( getZeroIfNull(fatura.getVlTarifa()));
		item.setVlJuros( getZeroIfNull(fatura.getVlJuros()));
		item.setObItemRedeco(fatura.getObFatura());
		item.setVlDiferencaCambialCotacao(BigDecimal.valueOf(0L));
		
		com.mercurio.lms.contasreceber.model.Fatura faturaEntity = faturaService.findById(fatura.getIdFatura());

		faturaEntity.setRedeco(redecoStore);
		faturaEntity.setTpSituacaoFatura(new DomainValue("RE"));
		
		boletoService.updateSituacaoBoleto(faturaEntity.getIdFatura(), "RE");
		
		faturaService.store(faturaEntity);
		
		return item;
	}

	private BigDecimal getZeroIfNull(BigDecimal param) {
		if(param == null){
			return new BigDecimal("0");
		}
		return param;
	}

	private void validateFaturaEntity(FaturaDMN fatura, Integer linha) {
		redecoService.validateFatura(faturaService.findById(fatura.getIdFatura()), "CC");
	}

	private void validateIdFatura(FaturaDMN fatura, Integer linha) {
		if(fatura.getIdFatura() == null){
			Long idFatura = findIdFaturaByFilialAndNumeroFatura(fatura);
			if(idFatura == null){
				throw new InclusaoRedecoException("Linha " + linha + ": " + 
						configuracoesFacade.getMensagem("LMS-36276", new Object[]{fatura.getSgFilial(), fatura.getNrFatura()}));
			}
		}
	}

	private Long findIdFaturaByFilialAndNumeroFatura(FaturaDMN fatura) {

		List<com.mercurio.lms.contasreceber.model.Fatura> faturaList = faturaService.findByNrFaturaByFilial(fatura.getNrFatura(), fatura.getIdFilial());
		if(faturaList.size() == 1){
			return faturaList.get(0).getIdFatura();
		}
		return null;
	}

	private void validateFilial(FaturaDMN fatura, Integer linha) {
		if(fatura.getIdFilial() == null){
			Long idFilial = findIdFilialBySgfilial(fatura.getSgFilial());
			if(idFilial == null){
				throw new InclusaoRedecoException("Linha " + linha + ": " + 
							configuracoesFacade.getMensagem("LMS-36275", new Object[]{fatura.getSgFilial()}));
			}
		}
	}

	private Long findIdFilialBySgfilial(String sgFilial) {

		Filial filial = filialService.findBySgFilialAndIdEmpresa(sgFilial,361L);
		if(filial == null){
			return null;
		}
		return filial.getIdFilial();
	}

	private void validateAtributosPreenchidos(FaturaDMN fatura, Integer linha) {

		if(StringUtils.isBlank(fatura.getSgFilial())  || fatura.getNrFatura() == null){
			throw new InclusaoRedecoException("Linha " + linha + ": " + configuracoesFacade.getMensagem("LMS-36337"));
		}
	}

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setItemRedecoService(ItemRedecoService itemRedecoService) {
		this.itemRedecoService = itemRedecoService;
	}
	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}
}
