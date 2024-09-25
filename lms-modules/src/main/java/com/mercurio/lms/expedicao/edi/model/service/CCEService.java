package com.mercurio.lms.expedicao.edi.model.service;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalDMN;
import br.com.tntbrasil.integracao.domains.expedicao.NotaFiscalWrapperDMN;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.edi.model.dao.CceDAO;
import com.mercurio.lms.expedicao.model.CCE;
import com.mercurio.lms.expedicao.model.CCEItem;
import com.mercurio.lms.edi.model.service.NotaFiscalEDIService;


public class CCEService extends CrudService<CCE, Long> {
	
	private CCEItemService cceItemService;
	private NotaFiscalEDIService notaFiscalEDIService;

	public void storeCCE(NotaFiscalWrapperDMN notaFiscalWrapperDMN) {
		Long idCCE = Long.valueOf(notaFiscalWrapperDMN.getCCE());
		List<NotaFiscalDMN> notas = notaFiscalWrapperDMN.getNotas();

		CCE cce = (CCE)super.findById(idCCE);
		
		List <CCEItem> itens = new ArrayList<CCEItem> ();		
		for (NotaFiscalDMN notaDMN : notas) {
			CCEItem cceItem = new CCEItem();
			cceItem.setNrChave(notaDMN.getChaveNFE());
			cceItem.setCce(cce);
			cceItem.setNrCae(notaDMN.getCae());
			cceItem.setDhEmisao(new DateTime(System.currentTimeMillis()));
			cceItem.setNrUnitizacao(notaDMN.getNrUnitizacao());
			cceItem.setBlPaletizado(Boolean.FALSE);
			if(this.isNotaPaletizada(notaDMN)) {
				notaFiscalEDIService.generatePaletizacaoNotasEdi(notaDMN);
				cceItem.setBlPaletizado(Boolean.TRUE);
			}
			itens.add(cceItem);				
		}		
		cceItemService.storeAll(itens);
	}
	
	private boolean isNotaPaletizada(NotaFiscalDMN notaDMN) {
		return notaDMN.getVolumes() != null && !notaDMN.getVolumes().isEmpty();
	}

	
	public void setCceDAO(CceDAO dao){
		this.setDao(dao);
	}
	
	public CceDAO getCceDAO(){
		return (CceDAO)this.getDao();
	}
	

	public Long storeProximoCCE(Usuario usuarioSolicitante) {
		return getCceDAO().storeProximoCCE(usuarioSolicitante);
	}


	public CCEItemService getCceItemService() {
		return cceItemService;
	}


	public void setCceItemService(CCEItemService cceItemService) {
		this.cceItemService = cceItemService;
	}

	public NotaFiscalEDIService getNotaFiscalEDIService() {
		return notaFiscalEDIService;
	}

	public void setNotaFiscalEDIService(NotaFiscalEDIService notaFiscalEDIService) {
		this.notaFiscalEDIService = notaFiscalEDIService;
	}

}
