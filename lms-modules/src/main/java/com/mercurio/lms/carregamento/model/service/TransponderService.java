package com.mercurio.lms.carregamento.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Transponder;
import com.mercurio.lms.carregamento.model.Transponder.SITUACAO_TRANSPONDER;
import com.mercurio.lms.carregamento.model.dao.TransponderDAO;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.transponderService"
 */
public class TransponderService extends CrudService<Transponder, Long>  {

	private ControleCargaService controleCargaService;

	public void setTransponderDAO(TransponderDAO transponderDAO) {
		setDao(transponderDAO);
	}

	private TransponderDAO getTransponderDAO() {
		return (TransponderDAO)getDao();
	}
	
	@Override
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}
	
	@Override
	public Serializable store(Transponder bean) {
		
		if(findByNrTransponder(bean.getNrTransponder(),true, bean.getIdTransponder()) != null){
			throw new BusinessException("LMS-05190");
		}
		
		if(bean.getControleCarga() == null && bean.getTpSituacaoTransponder().getValue().equals(SITUACAO_TRANSPONDER.EM_USO.getValue())){
				throw new BusinessException("LMS-05194");
		} else if(bean.getControleCarga() != null && !bean.getTpSituacaoTransponder().getValue().equals(SITUACAO_TRANSPONDER.EM_USO.getValue())){
				throw new BusinessException("LMS-05191"); 
		}
		
		return super.store(bean);
	}

	public Transponder findByNrTransponder(Long nrTransponder, boolean apenasAtivos, Long ... excludeIds) {
		return getTransponderDAO().findByNrTransponder(nrTransponder, apenasAtivos, excludeIds);
	}
	
	@Override
	public void removeById(Long id) {
		Transponder transponder = (Transponder) findById(id);
		if(transponder.getControleCarga() != null){
			throw new BusinessException("LMS-05191"); 
		}
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
		}
	}
	
	/**
	 * vincula o transponder ao controle de carga.
	 * 
	 * @param idTransponder
	 * @param idControleCarga
	 */
	public void storeVincularTransponderControleCarga(Long idTransponder, Long idControleCarga){
		Transponder transponder = (Transponder) findById(idTransponder);
		
		if(transponder.getControleCarga() != null){
			throw new BusinessException("LMS-05191");
		}
		transponder.setTpSituacaoTransponder(SITUACAO_TRANSPONDER.EM_USO.getValorDominio());
		
		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
		
		if(controleCargaService.controleCargaIniciouViagem(controleCarga)
			|| "FE".equals(controleCarga.getTpStatusControleCarga())
			|| "CA".equals(controleCarga.getTpStatusControleCarga())){
			throw new BusinessException("LMS-05195");
		}
		
		transponder.setControleCarga(controleCarga);
		
		flush();
		
		store(transponder);
	}
	
	public void removeVinculoIdTransponder(Long idTransponder){
		Transponder transponder = (Transponder) findById(idTransponder);
		
		// se nao esta fechado nem cancelado e o veiculo iniciou viagem, então nao permite remover
		if(!transponder.getControleCarga().getTpStatusControleCarga().getValue().equals("FE")
			&& !transponder.getControleCarga().getTpStatusControleCarga().getValue().equals("CA") 
			&& controleCargaService.controleCargaIniciouViagem(transponder.getControleCarga()) ){
			throw new BusinessException("LMS-05196");
		}
		
		transponder.setControleCarga(null);
		transponder.setTpSituacaoTransponder(SITUACAO_TRANSPONDER.DISPONIVEL.getValorDominio());
		store(transponder);
	}
	
	public void removeVinculoIdsTransponder(List idsTransponder){
		for (Object idTransponder : idsTransponder) {
			removeVinculoIdTransponder((Long)idTransponder);
		}
	}
	

	/**
	 * Atualiza a localização do transponder
	 * @param controleCarga
	 */
	public void executeAtualizaPosicaoTransponder(ControleCarga controleCarga) {
		/*Fechado						FE
		 Em Viagem						EV
		 Parada Operacional				PO
		 Em Descarga Parcial			EP
		 Aguardando Saída para Viagem	AV
		 Em Trânsito Coleta/Entrega		TC
		 Cancelado						CA
		 Em Carregamento Parcial		CP
		 Em Auditoria					EA
		 Aguardando Saída para Entrega	AE
		 Gerado							GE
		 Em Carregamento				EC
		 Em Descarga					ED
		 Aguardando Descarga			AD*/
		
		String tpStatusControleCarga = controleCarga.getTpStatusControleCarga().getValue();
		
		if(!tpStatusControleCarga.equals("FE") &&
			!tpStatusControleCarga.equals("TC") &&
			!tpStatusControleCarga.equals("CA") &&
			!tpStatusControleCarga.equals("GE")){
			List<Transponder> transpondersControleCarga = findTranspondersControleCarga(controleCarga.getIdControleCarga());
			for (Transponder transponder : transpondersControleCarga) {
				transponder.setFilial(controleCarga.getFilialByIdFilialAtualizaStatus());
				store(transponder);
			}
			
		} else if(tpStatusControleCarga.equals("FE")){
			liberarTranspondresByIdControleCarga(controleCarga.getIdControleCarga());
		}
	}

	private void liberarTranspondresByIdControleCarga(Long idControleCarga) {
		List<Transponder> transponders = findTranspondersControleCarga(idControleCarga);
		for (Transponder transponder : transponders) {
			transponder.setControleCarga(null);
			transponder.setTpSituacaoTransponder(SITUACAO_TRANSPONDER.DISPONIVEL.getValorDominio());
			store(transponder);
		}
	}

	public List<Transponder> findTranspondersControleCarga(Long idControleCarga) {
		return getTransponderDAO().findTranspondersControleCarga(idControleCarga);
	}

	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	@Override
	public Serializable findById(Long id) {
		return super.findById(id);
	}
}