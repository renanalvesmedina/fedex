package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.BoletoAnexo;
import com.mercurio.lms.contasreceber.model.HistoricoBoleto;
import com.mercurio.lms.contasreceber.model.dao.BoletoAnexoDAO;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.questionamentoFaturas.model.service.AnexoQuestionamentoFaturasService;
import com.mercurio.lms.questionamentofaturas.model.AnexoQuestionamentoFatura;
import com.mercurio.lms.questionamentofaturas.model.QuestionamentoFatura;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.contasreceber.boletoAnexoService"
 */
public class BoletoAnexoService extends CrudService<BoletoAnexo, Long> {
	
	private AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService;
	
	private BoletoService boletoService;
	
	private GerarFaturaBoletoService gerarFaturaService;
	
	private FaturaService faturaService;
	
	private HistoricoBoletoService historicoBoletoService;
	
	public AnexoQuestionamentoFaturasService getAnexoQuestionamentoFaturasService() {
		return anexoQuestionamentoFaturasService;
	}

	public void setAnexoQuestionamentoFaturasService(
			AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService) {
		this.anexoQuestionamentoFaturasService = anexoQuestionamentoFaturasService;
	}

	public List<BoletoAnexo> findBoletoAnexosByIdBoleto(Long idBoleto){
		return getBoletoAnexoDAO().findBoletoAnexosByIdBoleto(idBoleto);
	}
	
	public void setBoletoAnexoDAO(BoletoAnexoDAO boletoAnexoDAO) {
		setDao(boletoAnexoDAO);
	}

	public BoletoAnexoDAO getBoletoAnexoDAO() {
		return (BoletoAnexoDAO) getDao();
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	@Override
	public Serializable store(BoletoAnexo bean) {
		prepareValuesToStore(bean);
		
		if(bean.getBlEnvAnexoQuestFat()){
			insertQuestionamentoAnexo(bean);
		}
		
		return super.store(bean);
	}

	public void executeMoverAnexosToQuestionamento(HistoricoBoleto historicoBoleto){
		List<BoletoAnexo> list = findBoletoAnexosByIdBoleto(historicoBoleto.getBoleto().getIdBoleto());
		for (BoletoAnexo boletoAnexo : list) {
			store(boletoAnexo);
		}
	}

	public void executeLimparEnvioAnexos(Boleto boleto) {
		List<BoletoAnexo> list = findBoletoAnexosByIdBoleto(boleto.getIdBoleto());
		for (BoletoAnexo boletoAnexo : list) {
			boletoAnexo.setDhEnvioQuestFat(null);
			store(boletoAnexo);
		}
	}

	private void insertQuestionamentoAnexo(BoletoAnexo bean) {
 		Boleto b = boletoService.findById(bean.getBoleto().getIdBoleto()); 
 		
 		DoctoServico doctoServico =  gerarFaturaService.getFirstDoctoServicoFromFatura(b.getFatura(), b.getFatura().getItemFaturas());
 		
 		HistoricoBoleto hb = getHistoricoBoletoService().findLastHistoricoBoletoWithPendencia(b);
 		if( hb != null && hb.getIdPendencia() != null 
 				&& bean.getDhEnvioQuestFat() == null 
 				&& faturaService.isQuestionamentoFatura(doctoServico.getTpDocumentoServico())
 				&& hb.getTpSituacaoAprovacao() != null
 				&& "E".equals(hb.getTpSituacaoAprovacao().getValue()) ){
 			AnexoQuestionamentoFatura aqf = new AnexoQuestionamentoFatura();
 			
 			aqf.setDsAnexo(bean.getDsAnexo());
 			aqf.setDhCriacao(bean.getDhCriacao());
 			UsuarioLMS ulms = new UsuarioLMS();
 			ulms.setIdUsuario(bean.getUsuario().getIdUsuario());
 			aqf.setUsuario(ulms);
 			aqf.setDcArquivo(bean.getDcArquivo());
 			
 			QuestionamentoFatura qf = new QuestionamentoFatura();
 			
 			bean.setDhEnvioQuestFat(JTDateTimeUtils.getDataHoraAtual());
 			
 			qf.setIdQuestionamentoFatura(hb.getIdPendencia());
 			
 			aqf.setQuestionamentoFatura(qf);
 			
 			
 			
 			getAnexoQuestionamentoFaturasService().store(aqf);
 		}
	}
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	public void prepareValuesToStore(BoletoAnexo bean){
		if(bean.getIdBoletoAnexo() == null){
			bean.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
			bean.setUsuario(SessionUtils.getUsuarioLogado());
		} else {
			bean.setDhModificacao(JTDateTimeUtils.getDataHoraAtual());
		}
	}
	
	public void storeAnexos(ItemList listAnexos) {
		if(listAnexos != null){
    		for (Object boletoAnexoO : listAnexos.getNewOrModifiedItems()) {
    			BoletoAnexo descontoAnexo = (BoletoAnexo) boletoAnexoO;
    			store(descontoAnexo);
    		}
    		
    		for (Object descontoAnexoO : listAnexos.getRemovedItems()) {
    			BoletoAnexo descontoAnexo = (BoletoAnexo) descontoAnexoO;
    			removeById(descontoAnexo.getIdBoletoAnexo());
    		}
    	}
	}

	public void validaBoletoAnexo(Boleto boleto, ItemList anexos) {
    	if(anexos.isInitialized()){
    		if(anexos.getNewOrModifiedItems() != null && !anexos.getNewOrModifiedItems().isEmpty()){
				for (Object boletoAnexoO : anexos.getNewOrModifiedItems()) {
					BoletoAnexo boletoAnexo = (BoletoAnexo) boletoAnexoO;
					
					if(boletoAnexo.getBlEnvAnexoQuestFat()){
						return;
					}
				}
    		}
    	}
    		
    	List<BoletoAnexo> listaBoletoAnexo = new ArrayList<BoletoAnexo>(); 
		if(boleto != null && boleto.getIdBoleto() != null){
			listaBoletoAnexo = findBoletoAnexosByIdBoleto(boleto.getIdBoleto());
		}
			
		if(anexos.isInitialized()){
			for (Object boletoAnexoO : anexos.getRemovedItems()) {
				BoletoAnexo boletoAnexo = (BoletoAnexo) boletoAnexoO;
				
				if(boletoAnexo.getIdBoletoAnexo() != null){
					compareById : for (int i = 0; i < listaBoletoAnexo.size(); i++) {
						if(listaBoletoAnexo.get(i).getIdBoletoAnexo() != null && listaBoletoAnexo.get(i).getIdBoletoAnexo().equals(boletoAnexo.getIdBoletoAnexo())){
							listaBoletoAnexo.remove(i);
							break compareById;
						}
					}
				}
			}
		}
		
		if(!listaBoletoAnexo.isEmpty()){
			for (BoletoAnexo boletoAnexo : listaBoletoAnexo) {
				if(boletoAnexo.getBlEnvAnexoQuestFat()){
					return;
				}
			}
		}
		
		anexos.rollbackItemsState();
		throw new BusinessException("LMS-42045");
	}

	public void setBoletoService(BoletoService boletoService) {
		this.boletoService = boletoService;
	}

	public BoletoService getBoletoService() {
		return boletoService;
	}

	public void setGerarFaturaService(GerarFaturaBoletoService gerarFaturaService) {
		this.gerarFaturaService = gerarFaturaService;
	}

	public GerarFaturaBoletoService getGerarFaturaService() {
		return gerarFaturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setHistoricoBoletoService(HistoricoBoletoService historicoBoletoService) {
		this.historicoBoletoService = historicoBoletoService;
	}

	public HistoricoBoletoService getHistoricoBoletoService() {
		return historicoBoletoService;
	}

}