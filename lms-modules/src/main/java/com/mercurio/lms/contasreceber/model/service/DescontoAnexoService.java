package com.mercurio.lms.contasreceber.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.Desconto;
import com.mercurio.lms.contasreceber.model.DescontoAnexo;
import com.mercurio.lms.contasreceber.model.dao.DescontoAnexoDAO;
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
 * @spring.bean id="lms.contasreceber.descontoAnexoService"
 */
public class DescontoAnexoService extends CrudService<DescontoAnexo, Long> {
	
	private AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService;
	
	private DescontoService descontoService;
	
	private FaturaService faturaService;

	public AnexoQuestionamentoFaturasService getAnexoQuestionamentoFaturasService() {
		return anexoQuestionamentoFaturasService;
	}

	public void setAnexoQuestionamentoFaturasService(
			AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService) {
		this.anexoQuestionamentoFaturasService = anexoQuestionamentoFaturasService;
	}

	public List<DescontoAnexo> findDescontoAnexosByIdDesconto(Long idDesconto){
		return getDescontoAnexoDAO().findDescontoAnexosByIdDesconto(idDesconto);
	}
	
	public void setDescontoAnexoDAO(DescontoAnexoDAO descontoAnexoDAO) {
		setDao(descontoAnexoDAO);
	}

	public DescontoAnexoDAO getDescontoAnexoDAO() {
		return (DescontoAnexoDAO) getDao();
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	@Override
	public Serializable store(DescontoAnexo bean) {
		prepareValuesToStore(bean);
		
		if(bean.getBlEnvAnexoQuestFat()){
			insertQuestionamentoAnexo(bean);
		}
		
		return super.store(bean);
	}

	
	private void insertQuestionamentoAnexo(DescontoAnexo bean) {
 		Desconto d = bean.getDesconto();
 		
 		if(d.getIdPendencia() != null 
 			&& bean.getDhEnvioQuestFat() == null &&
 			faturaService.isQuestionamentoFatura(d.getDevedorDocServFat().getDoctoServico().getTpDocumentoServico())){
 			AnexoQuestionamentoFatura aqf = new AnexoQuestionamentoFatura();
 			aqf.setDsAnexo(bean.getDsAnexo());
 			aqf.setDhCriacao(bean.getDhCriacao());
 			UsuarioLMS ulms = new UsuarioLMS();
 			ulms.setIdUsuario(bean.getUsuario().getIdUsuario());
 			aqf.setUsuario(ulms);
 			aqf.setDcArquivo(bean.getDcArquivo());
 			
 			QuestionamentoFatura qf = new QuestionamentoFatura();
 			qf.setIdQuestionamentoFatura(d.getIdPendencia());
 			
 			aqf.setQuestionamentoFatura(qf);
 			
 			bean.setDhEnvioQuestFat(JTDateTimeUtils.getDataHoraAtual());
 			
 			getAnexoQuestionamentoFaturasService().store(aqf);
 		}
	}
	
	@Override
	public void removeById(Long id) {
		super.removeById(id);
	}
	
	public void prepareValuesToStore(DescontoAnexo bean){
		if(bean.getIdDescontoAnexo() == null){
			bean.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
			bean.setUsuario(SessionUtils.getUsuarioLogado());
		} else {
			bean.setDhModificacao(JTDateTimeUtils.getDataHoraAtual());
		}
	}

	public void setDescontoService(DescontoService descontoService) {
		this.descontoService = descontoService;
	}

	public DescontoService getDescontoService() {
		return descontoService;
	}
	
	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public void removeDescontoAnexoByIdFatura(Long idFatura) {
		getDescontoAnexoDAO().removeDescontoAnexoByIdFatura(idFatura);
	}
    
}