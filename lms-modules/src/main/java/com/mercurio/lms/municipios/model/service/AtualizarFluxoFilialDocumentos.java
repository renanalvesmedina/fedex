package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.adsm.batch.annotations.Assynchronous;
import com.mercurio.adsm.batch.annotations.AssynchronousMethod;
import com.mercurio.adsm.batch.annotations.BatchFeedbackType;
import com.mercurio.adsm.batch.annotations.BatchType;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.municipios.model.FluxoFilial;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Serviço responsável por atualizar os conhecimentos que possuem
 * a coluna DOCTO_SERVICO.ID_FLUXO_FILIAL nula
 * 
 * @author andreim
 * @spring.bean id="lms.municipios.atualizarFluxoFilialDocumentos"
 */
@Assynchronous(name = "AtualizarFluxoFilialDocumentos")
public class AtualizarFluxoFilialDocumentos {
	
	private static final String USER_INTEGRACAO = "integracao";

	private DoctoServicoService doctoServicoService;
	
	private McdService mcdService;
	
	private UsuarioService usuarioService;
	
	@AssynchronousMethod( name="atualizarFluxoFilialDocumentos.execute",
			type = BatchType.BATCH_SERVICE,
			feedback = BatchFeedbackType.ON_ERROR)	
	public void execute(){
		
		Usuario usuario = usuarioService.findUsuarioByLogin(USER_INTEGRACAO);
		
		List<DoctoServico> list = new ArrayList<DoctoServico>();
		
		FluxoFilial fluxoFilial = null;
		
		List<DoctoServico> docs = getDoctoServicoService().findDocSemFluxoFilial(usuario.getIdUsuario());
		if(docs != null && !docs.isEmpty()){
			for (DoctoServico doctoServico : docs) {
				
				/*Obtem o fluxo filial*/
				fluxoFilial = mcdService.findFluxoEntreFiliais(doctoServico.getFilialByIdFilialOrigem().getIdFilial()
						, doctoServico.getFilialByIdFilialDestino().getIdFilial()
						, doctoServico.getServico().getIdServico(), JTDateTimeUtils.getDataAtual());
				
				if(fluxoFilial != null){
					doctoServico.setFluxoFilial(fluxoFilial);
					
					/*Atualiza o documento de servico*/
					doctoServicoService.executeUpdateFluxoDoc(doctoServico);										
				}				
								
			}/*for*/
		}/*if*/	
		
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public McdService getMcdService() {
		return mcdService;
	}

	public void setMcdService(McdService mcdService) {
		this.mcdService = mcdService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}
