package com.mercurio.lms.contasreceber.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.contasreceber.model.Fatura;
import com.mercurio.lms.contasreceber.model.FaturaAnexo;
import com.mercurio.lms.contasreceber.model.ItemFatura;
import com.mercurio.lms.contasreceber.model.dao.FaturaAnexoDAO;
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
 * @spring.bean id="lms.contasreceber.faturaAnexoService"
 */
public class FaturaAnexoService extends CrudService<FaturaAnexo, Long>{

	
	private FaturaService faturaService;
		
	private AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService;
	/**
	 * Recupera uma instância de <code>FaturaAnexo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public FaturaAnexo findById(java.lang.Long id) {
        return (FaturaAnexo)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(FaturaAnexo bean) {
    	prepareValuesToStore(bean);

    	bean.setFatura(faturaService.findById(bean.getFatura().getIdFatura()));
    	
    	if(bean.getBlEnvAnexoQuestFat()){
			insertQuestionamentoAnexo(bean);
		}
    	
        return super.store(bean);
    }
    
    public List<FaturaAnexo> findAllFaturaAnexoByIdFatura(Long idFatura)
    {    
    	return getFaturaAnexoDAO().findAllFaturaAnexoById(idFatura);
    }

	public void setFaturaAnexoDAO(FaturaAnexoDAO faturaAnexoDAO) {
		setDao(faturaAnexoDAO);
	}

	public FaturaAnexoDAO getFaturaAnexoDAO() {
		return (FaturaAnexoDAO)getDao();
	}
	
	public List<FaturaAnexo> findFaturaAnexosByIdFatura(Long idFatura){
		return getFaturaAnexoDAO().findFaturaAnexosByIdFatura(idFatura);
	}
	
	public void prepareValuesToStore(FaturaAnexo bean){
		if(bean.getIdFaturaAnexo() == null){
			bean.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
			bean.setUsuario(SessionUtils.getUsuarioLogado());
		} else {
			bean.setDhModificacao(JTDateTimeUtils.getDataHoraAtual());
		}
	}
	
	private void insertQuestionamentoAnexo(FaturaAnexo bean) {
 		DoctoServico ds = getFirstDoctoServicoFromFatura(bean.getFatura(), bean.getFatura().getItemFaturas());
 		
 		if(bean.getFatura().getIdPendenciaDesconto() != null 
 				&& bean.getDhEnvioQuestFat() == null 
 				&& getFaturaService().isQuestionamentoFatura(ds.getTpDocumentoServico())){
 			AnexoQuestionamentoFatura aqf = new AnexoQuestionamentoFatura();
 			aqf.setDsAnexo(bean.getDsAnexo());
 			aqf.setDhCriacao(bean.getDhCriacao());
 			UsuarioLMS ulms = new UsuarioLMS();
 			ulms.setIdUsuario(bean.getUsuario().getIdUsuario());
 			aqf.setUsuario(ulms);
 			aqf.setDcArquivo(bean.getDcArquivo());
 			
 			QuestionamentoFatura qf = new QuestionamentoFatura();
 			qf.setIdQuestionamentoFatura(bean.getFatura().getIdPendenciaDesconto()); 			
 			aqf.setQuestionamentoFatura(qf);
 			
 			bean.setDhEnvioQuestFat(JTDateTimeUtils.getDataHoraAtual());
 			
 			getAnexoQuestionamentoFaturasService().store(aqf);
 		}
	}

	private DoctoServico getFirstDoctoServicoFromFatura (Fatura fatura, List lstItemFatura) {
		DoctoServico ds = null;
		if (lstItemFatura != null) {	  
			ds = ((ItemFatura)lstItemFatura.get(0))    
					.getDevedorDocServFat().getDoctoServico();		
		}
		return ds;
	}

	public void setFaturaService(FaturaService faturaService) {
		this.faturaService = faturaService;
	}

	public FaturaService getFaturaService() {
		return faturaService;
	}

	public void setAnexoQuestionamentoFaturasService(
			AnexoQuestionamentoFaturasService anexoQuestionamentoFaturasService) {
		this.anexoQuestionamentoFaturasService = anexoQuestionamentoFaturasService;
	}

	public AnexoQuestionamentoFaturasService getAnexoQuestionamentoFaturasService() {
		return anexoQuestionamentoFaturasService;
	}
}
