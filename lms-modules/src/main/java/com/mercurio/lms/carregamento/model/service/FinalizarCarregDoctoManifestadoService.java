package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.dao.CarregamentoDescargaDAO;

/**
* Classe de serviço para CRUD:
* 
* Não inserir documentação após ou remover a tag do XDoclet a seguir.
* O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
* @spring.bean id="lms.carregamento.finalizarCarregDoctoManifestadoService"
*/

public class FinalizarCarregDoctoManifestadoService extends CrudService<CarregamentoDescarga, Long> {

	private CarregamentoDescargaService carregamentoDescargaService;	
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setCarregamentoDescargaDAO(CarregamentoDescargaDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private CarregamentoDescargaDAO getCarregamentoDescargaDAO() {
		return (CarregamentoDescargaDAO) getDao();
	}
	
	/**
	 *  Rotina que será chamada no processo de aprovação/reprovação do workflow 
	 * @param idProcesso
	 * @param tpSituacaoAprovacao
	 * @return
	 */
	public void executeWorkflow(List<Long> idProcesso, List<String> tpSituacaoAprovacao) {		
		if (idProcesso == null || tpSituacaoAprovacao == null || idProcesso.size() != tpSituacaoAprovacao.size()) {
			return;
		}		
		getCarregamentoDescargaDAO().storeFinalizarCarregDoctoManifestadoService(idProcesso.get(0), tpSituacaoAprovacao.get(0));
		
		return;
	}

	public CarregamentoDescargaService getCarregamentoDescargaService() {
		return carregamentoDescargaService;
	}

	public void setCarregamentoDescargaService(CarregamentoDescargaService carregamentoDescargaService) {
		this.carregamentoDescargaService = carregamentoDescargaService;
	}
}
