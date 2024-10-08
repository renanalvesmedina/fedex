package com.mercurio.lms.pendencia.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.pendencia.model.OcorrenciaPendencia;
import com.mercurio.lms.pendencia.model.service.OcorrenciaDoctoServicoService;
import com.mercurio.lms.pendencia.model.service.OcorrenciaPendenciaService;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Generated by: ADSM ActionGenerator
 * <p>
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 *
 * @spring.bean id="lms.pendencia.registrarOcorrenciasDocumentosControleCargaRegistrarAction"
 */
public class RegistrarOcorrenciasDocumentosControleCargaRegistrarAction extends CrudAction {
	private OcorrenciaPendenciaService ocorrenciaPendenciaService;	
	private MoedaService moedaService;	
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService;
	private DoctoServicoService doctoServicoService;
	
	public List findLookupOcorrenciaPendencia(TypedFlatMap criteria) {
		return this.getOcorrenciaPendenciaService().findLookupToRegistrarOcorrenciasDoctosServico(criteria);
	}	
	
	public List findMoeda(Map criteria) {
		return this.getMoedaService().find(criteria);
	}
	
	public TypedFlatMap findOcorrenciaBloqueioFromOcorrenciaDoctoServicoEmAberto(TypedFlatMap criteria){
		TypedFlatMap mapRetorno = new TypedFlatMap();
		List ids = (ArrayList)criteria.get("ids");
		
        if (!ids.isEmpty()) {
			Long idManifesto = Long.valueOf((String)ids.get(0));	
			
			/** Otimiza��o */
    		final ProjectionList projection = Projections.projectionList()
    			.add(Projections.property("ds.id"), "idDoctoServico")
    			.add(Projections.property("ds.tpDocumentoServico"), "tpDocumentoServico");

    		final List<DoctoServico> doctoServicoList = doctoServicoService.findDoctoServicoByIdManifesto(idManifesto, projection);
    		for (DoctoServico doctoServico : doctoServicoList) {
    			if(!ConstantesExpedicao.MINUTA_DESPACHO_ACOMPANHAMENTO.equals(doctoServico.getTpDocumentoServico().getValue())){
                    addOcorrenciaPendenciaToMap(mapRetorno, doctoServico);
                }
            }
        }
        return mapRetorno;
    }

    private void addOcorrenciaPendenciaToMap(TypedFlatMap mapRetorno, DoctoServico doctoServico) {
				OcorrenciaPendencia ocorrenciaBloqueio = ocorrenciaPendenciaService.findOcorrenciaBloqueioFromOcorrenciaDoctoServicoEmAberto(doctoServico.getIdDoctoServico());
					//A flag do doctoServico indica que existe uma ocorrencia de bloqueio em aberto, mas ela nao existe.
					//Cadastrar uma mensagem e disparar uma exception?
        if (ocorrenciaBloqueio != null) {
				mapRetorno.put("idOcorrenciaBloqueio", ocorrenciaBloqueio.getIdOcorrenciaPendencia());
				mapRetorno.put("cdOcorrenciaBloqueio", ocorrenciaBloqueio.getCdOcorrencia());
				mapRetorno.put("dsOcorrenciaBloqueio", ocorrenciaBloqueio.getDsOcorrencia());
			}
		}
	
	/**
	 * M�todo que salva o registro no banco.
     *
	 * @param mapBean
	 * @return
	 */
	public TypedFlatMap store(TypedFlatMap mapBean) {		
		DateTime dataHora = getOcorrenciaDoctoServicoService().executeRegistrarOcorrenciaDoctoServicoCC(mapBean);
		TypedFlatMap map = new TypedFlatMap();
		map.put("dataHora", dataHora);
		return map;		
	}
	
	/**
	 * Pega o usuario logado na sess�o com sua moeda e sua respectiva filial. 
	 */
	public TypedFlatMap getDadosSessao() {
		TypedFlatMap map = new TypedFlatMap();
		map.put("idMoedaSessao", SessionUtils.getMoedaSessao().getIdMoeda());
		return map;
	}	

	public OcorrenciaPendenciaService getOcorrenciaPendenciaService() {
		return ocorrenciaPendenciaService;
	}

	public void setOcorrenciaPendenciaService(
			OcorrenciaPendenciaService ocorrenciaPendenciaService) {
		this.ocorrenciaPendenciaService = ocorrenciaPendenciaService;
	}
	
	public MoedaService getMoedaService() {
		return moedaService;
	}
	
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	
	public EventoDocumentoServicoService getEventoDocumentoServicoService() {
		return eventoDocumentoServicoService;
	}
	
	public void setEventoDocumentoServicoService(
			EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}
	
	public OcorrenciaDoctoServicoService getOcorrenciaDoctoServicoService() {
		return ocorrenciaDoctoServicoService;
	}
	
	public void setOcorrenciaDoctoServicoService(
			OcorrenciaDoctoServicoService ocorrenciaDoctoServicoService) {
		this.ocorrenciaDoctoServicoService = ocorrenciaDoctoServicoService;
	}

	public DoctoServicoService getDoctoServicoService() {
		return doctoServicoService;
	}

	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

}
