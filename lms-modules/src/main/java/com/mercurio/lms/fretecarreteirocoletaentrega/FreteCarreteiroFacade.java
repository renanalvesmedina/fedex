package com.mercurio.lms.fretecarreteirocoletaentrega;

import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.coleta.model.service.ManifestoColetaService;
import com.mercurio.lms.entrega.model.service.ManifestoEntregaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoColetaService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoDoctoService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.service.NotaCreditoService;
import com.mercurio.lms.portaria.model.ControleQuilometragem;
import com.mercurio.lms.portaria.model.service.ControleQuilometragemService;

/**
 * @author Andrêsa Vargas
 * @spring.bean id="lms.freteCarreteiroFacade"
 */
public class FreteCarreteiroFacade {

    private ControleCargaService controleCargaService;
    private NotaCreditoService notaCreditoService;
    private NotaCreditoColetaService notaCreditoColetaService;
    private NotaCreditoDoctoService notaCreditoDoctoService;
    private ManifestoEntregaService manifestoEntregaService;
    private ManifestoColetaService manifestoColetaService;
    private ControleQuilometragemService controleQuilometragemService;
	
	
    public Long findQuilometragemPercorridaControleCarga(Long idControleCarga) {
        return controleCargaService.findQuilometrosPercorridosByIdControleCarga(idControleCarga);
    }
	
	public String findTpCalculo(Long idNotaCredito){
		return notaCreditoService.findTpCalculoNotaCredito(idNotaCredito);
	}
	
	public Long findQuantidadeColetasEfetuadasControleCarga(Long idControleCarga) {
		return notaCreditoColetaService.findQuantidadeColetasEfetuadasControleCarga(idControleCarga);
	}

	public Long findQuantidadeEntregasEfetuadasControleCarga(Long idControleCarga) {
		return notaCreditoDoctoService.findQuantidadeEntregasEfetuadasControleCarga(idControleCarga);
	}

	/**
	 * Consulta a quantidade de entregas efetuadas
	 * 
	 * @param idManifestoEntrega
	 * @return
	 */
	public Integer findQuantidadeEntregasEfetuadasByIdManifestoEntrega(Long idManifestoEntrega) {
		return manifestoEntregaService.findQuantidadeEntregasEfetuadasByIdManifestoEntrega(idManifestoEntrega);
	}

	
	/**
	 * Consulta Quilometragem inicial do Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public Integer findQuiloMetragemInicialByIdControleCarga(Long idControleCarga) {
		ControleQuilometragem controleQuilometragem = controleQuilometragemService.findUltimoControleQuilometragemByIdControleCarga(idControleCarga, Boolean.TRUE);
		Integer nrQuilometragem = 0;
		if (controleQuilometragem != null) {
			nrQuilometragem = controleQuilometragem.getNrQuilometragem();
		}
		return nrQuilometragem;
	}

	/**
	 * Consulta Quilometragem final do Controle de Carga
	 * 
	 * @param idControleCarga
	 * @return
	 */
	public Integer findQuiloMetragemFinalByIdControleCarga(Long idControleCarga) {
		ControleQuilometragem controleQuilometragem = controleQuilometragemService.findUltimoControleQuilometragemByIdControleCarga(idControleCarga, Boolean.FALSE);
		Integer nrQuilometragem = 0;
		if (controleQuilometragem != null) {
			nrQuilometragem = controleQuilometragem.getNrQuilometragem();
		}
		return nrQuilometragem;
	}

    public void setControleCargaService(ControleCargaService controleCargaService) {
        this.controleCargaService = controleCargaService;
    }

    public void setNotaCreditoService(NotaCreditoService notaCreditoService) {
        this.notaCreditoService = notaCreditoService;
    }
	
    public void setNotaCreditoColetaService(NotaCreditoColetaService notaCreditoColetaService) {
        this.notaCreditoColetaService = notaCreditoColetaService;
    }
	
    public void setNotaCreditoDoctoService(NotaCreditoDoctoService notaCreditoDoctoService) {
        this.notaCreditoDoctoService = notaCreditoDoctoService;
}

	public ManifestoEntregaService getManifestoEntregaService() {
		return manifestoEntregaService;
}

	public void setManifestoEntregaService(ManifestoEntregaService manifestoEntregaService) {
		this.manifestoEntregaService = manifestoEntregaService;
	}

	public ManifestoColetaService getManifestoColetaService() {
		return manifestoColetaService;
	}

	public void setManifestoColetaService(ManifestoColetaService manifestoColetaService) {
		this.manifestoColetaService = manifestoColetaService;
	}

	public ControleQuilometragemService getControleQuilometragemService() {
		return controleQuilometragemService;
	}

	public void setControleQuilometragemService(ControleQuilometragemService controleQuilometragemService) {
		this.controleQuilometragemService = controleQuilometragemService;
	}
}
