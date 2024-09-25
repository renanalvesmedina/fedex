package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.ControleCargaConhScan;
import com.mercurio.lms.carregamento.model.dao.ControleCargaConhScanDAO;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.controleCargaConhScanService"
 */
public class ControleCargaConhScanService extends CrudService<ControleCargaConhScan, Long> {

	private ConhecimentoService conhecimentoService;
	private ControleCargaService controleCargaService;
	private PreManifestoDocumentoService preManifestoDocumentoService; 
	private CarregamentoDescargaService carregamentoDescargaService;
	
	public void store(Long idControleCarga, String nrCodigoBarras, Long idCarregamentoDescarga , String tipoFluxo) {
		if(idControleCarga == null || nrCodigoBarras == null) {
			throw new BusinessException("LMS-00001", new Object[] {"idControleCarga", "nrCodigoBarras"});
		}
		
		// VERIFICA CARREGAMENTO FINALIZADO
		if("C".equalsIgnoreCase(tipoFluxo)){
			if (carregamentoDescargaService.validateCarregamentoFinalizado(idControleCarga , SessionUtils.getFilialSessao().getIdFilial())) {
				throw new BusinessException("LMS-05025");
			}
		}
		
		CarregamentoDescarga carregamentoDescarga = carregamentoDescargaService.findById(idCarregamentoDescarga);
		
		if("D".equalsIgnoreCase(tipoFluxo) && carregamentoDescarga != null){
			String statusCarregamento = carregamentoDescarga.getTpStatusOperacao().getValue();
			if ("F".equals(statusCarregamento) || "C".equals(statusCarregamento)){
				throw new BusinessException("LMS-45093",new Object[]{carregamentoDescarga.getTpStatusOperacao().getDescription().getValue()});
			}
			}
		
		Long idDoctoServico;
		
		if (nrCodigoBarras.length() <= 20) {
			Long barcode = Long.valueOf(nrCodigoBarras);
			
			List list = conhecimentoService.findIdConhecimentoByNrCodigoBarras(barcode);
		
			if(list == null || list.size() <= 0) {
				throw new BusinessException("LMS-26106");
			}
			if(list.size() > 1) {
				throw new BusinessException("LMS-05197");
			}
			idDoctoServico = (Long) list.get(0);
		} else {
			Conhecimento conhecimentoBusca = conhecimentoService.findByNrChave(nrCodigoBarras);
			idDoctoServico = conhecimentoBusca.getIdDoctoServico();
		}
		
		Integer qtControleScan = getControleCargaConhScanDAO().findControleCargaConhScanByNrCodigoBarras(idDoctoServico, idControleCarga, idCarregamentoDescarga);
		if(qtControleScan != null && qtControleScan > 0) {
			throw new BusinessException("LMS-05198");
		}
		
		Conhecimento conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(idDoctoServico);
		
		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(idControleCarga);
		
		
		
		ControleCargaConhScan bean = new ControleCargaConhScan();
		bean.setConhecimento(conhecimento);
		bean.setControleCarga(controleCarga);
		bean.setCarregamentoDescarga(carregamentoDescarga);
		
		super.store(bean);
	}
	
	public void store(Long idControleCarga, String nrCodigoBarras) {
		
		Long idDoctoServico;
		
		if (nrCodigoBarras.length() <= 20) {
			
		    if (! StringUtils.isNumeric(nrCodigoBarras)) {
		        throw new BusinessException("LMS-26106");
		    }
			Long barcode = Long.valueOf(nrCodigoBarras);
			List list = conhecimentoService.findIdConhecimentoByNrCodigoBarras(barcode);
		
			if(list == null || list.size() <= 0) {
				throw new BusinessException("LMS-26106");
			}
			if(list.size() > 1) {
				throw new BusinessException("LMS-05197");
			}
			idDoctoServico = (Long) list.get(0);
		} else {
			Conhecimento conhecimentoBusca = conhecimentoService.findByNrChave(nrCodigoBarras);
			idDoctoServico = conhecimentoBusca.getIdDoctoServico();
		}
		
		Integer qtControleScan = getControleCargaConhScanDAO().findControleCargaConhScanByNrCodigoBarras(idDoctoServico, idControleCarga, null);
		if(qtControleScan != null && qtControleScan > 0) {
			throw new BusinessException("LMS-05198");
		}
		
		Conhecimento conhecimento = new Conhecimento();
		conhecimento.setIdDoctoServico(idDoctoServico);
		
		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(idControleCarga);
		
		ControleCargaConhScan bean = new ControleCargaConhScan();
		bean.setConhecimento(conhecimento);
		bean.setControleCarga(controleCarga);
		bean.setCarregamentoDescarga(null);
		
		super.store(bean);
	}
	
	public java.io.Serializable store(ControleCargaConhScan bean) {
		return super.store(bean);
	}
	
	public ResultSetPage findPaginatedByControleCargaAndCarregamentoDescarga(Long idControleCarga, Long idCarregamentoDescarga, FindDefinition findDef) {
		return getControleCargaConhScanDAO().findPaginatedByControleCargaAndCarregamentoDescarga(idControleCarga, idCarregamentoDescarga, findDef);
	}
	
	public Integer getRowCountByControleCargaAndCarregamentoDescarga(Long idControleCarga, Long idCarregamentoDescarga) {
		return getControleCargaConhScanDAO().getRowCountByControleCargaAndCarregamentoDescarga(idControleCarga, idCarregamentoDescarga);
	}
	
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}
	
	public Long getRowCountDocCarregadoConferido(final Long idControleCarga, final Long idCarregamentoDescarga) {
		return getControleCargaConhScanDAO().getRowCountDocCarregadoConferido(idControleCarga, idCarregamentoDescarga);
	}
	public Long getRowCountDocCarregadoNaoConferido(final Long idControleCarga, final Long idCarregamentoDescarga) {
		return getControleCargaConhScanDAO().getRowCountDocCarregadoNaoConferido(idControleCarga, idCarregamentoDescarga);
	}
	public Long getRowCountDocNaoCarregadoConferido(final Long idControleCarga, final Long idCarregamentoDescarga) {
		return getControleCargaConhScanDAO().getRowCountDocNaoCarregadoConferido(idControleCarga, idCarregamentoDescarga);
	}
	
    /**
	 * Busca os conhecimentos que possuem nrCodigoBarras e validar se ainda existe algum a ser lido na Aba Documentos de Serviço
	 * @param idControleCarga
	 * @param idCarregamentoDescarga 
	 * @return
	 */
	public ResultSetPage findPaginatedByControleCargaCarregamento(Long idControleCarga, Long idCarregamentoDescarga) {		
		ResultSetPage reSetPage = getControleCargaConhScanDAO().findPaginatedByControleCargaAndCarregamentoDescarga(idControleCarga, idCarregamentoDescarga, new FindDefinition(1, 1, null));
		
		return reSetPage;
	}
	
	public ControleCargaConhScan findControleCargaConhScan(Long nrCodigoBarras, Long idControleCarga, Long idCarregamentoDescarga) {
		return getControleCargaConhScanDAO().findControleCargaConhScan(nrCodigoBarras, idControleCarga, idCarregamentoDescarga);
	}
	
	public List<ControleCargaConhScan> findByCarregamentoDescarga(Long idCarregamentoDescarga) {
		return getControleCargaConhScanDAO().findByCarregamentoDescarga(idCarregamentoDescarga);
	}
	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setControleCargaConhScanDAO(ControleCargaConhScanDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ControleCargaConhScanDAO getControleCargaConhScanDAO() {
        return (ControleCargaConhScanDAO) getDao();
    }

	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public ControleCargaService getControleCargaService() {
		return controleCargaService;
}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	public ConhecimentoService getConhecimentoService() {
		return conhecimentoService;
	}

	public PreManifestoDocumentoService getPreManifestoDocumentoService() {
		return preManifestoDocumentoService;
	}

	public void setPreManifestoDocumentoService(
			PreManifestoDocumentoService preManifestoDocumentoService) {
		this.preManifestoDocumentoService = preManifestoDocumentoService;
	}

	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}

	public void setCarregamentoDescargaService(
			CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}

	public List<ControleCargaConhScan> findByIdControleCarga(Long idControleCarga) {
		return getControleCargaConhScanDAO().findByIdControleCarga(idControleCarga);
}

}
