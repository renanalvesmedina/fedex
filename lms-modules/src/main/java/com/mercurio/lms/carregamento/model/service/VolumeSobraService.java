package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.joda.time.DateTime;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CarregamentoDescarga;
import com.mercurio.lms.carregamento.model.VolumeSobra;
import com.mercurio.lms.carregamento.model.dao.VolumeSobraDAO;
import com.mercurio.lms.coleta.model.service.PedidoColetaService;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.expedicao.model.VolumeNotaFiscal;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD: 
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.VolumeSobra"
 */
public class VolumeSobraService extends CrudService<VolumeSobra, Long> {

	private ManifestoService manifestoService;
	private PedidoColetaService pedidoColetaService;
	private FilialService filialService;
	
	public VolumeSobra findById(java.lang.Long id) {
		return (VolumeSobra)super.findById(id);
	}
	
	public List findVolumesSobraByIdControleCarga(Long idControleCarga, Long idFilial) {
		Filial filial = filialService.findById(idFilial);
		
		if (filial.getBlRncAutomaticaDescargaMww()) {
			return getVolumeSobraDAO().findVolumesSobraByIdControleCarga(idControleCarga);
		}
		
		return ListUtils.EMPTY_LIST;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VolumeSobra bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setVolumeSobraDAO(VolumeSobraDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private VolumeSobraDAO getVolumeSobraDAO() {
		return (VolumeSobraDAO) getDao();
	}
	
	public void validateStoreVolumeSobra(VolumeNotaFiscal volumeNotaFiscal, CarregamentoDescarga carregamentoDescarga, Long idControleCarga, String tpDontroleCarga, Filial filial, UsuarioLMS usuario){
		
		boolean isVolumePedidoColeta = getPedidoColetaService().isVolumePrevistoDescargaAereo(volumeNotaFiscal.getIdVolumeNotaFiscal(), 
																							  idControleCarga,
																							  SessionUtils.getFilialSessao().getIdFilial(),
																							  null);		
		if(!isVolumePedidoColeta){
			VolumeSobra vs = new VolumeSobra();
			vs.setVolumeNotaFiscal(volumeNotaFiscal);
			vs.setCarregamentoDescarga(carregamentoDescarga);
			vs.setUsuario(usuario);
			vs.setDhOperacao(new DateTime());
			
			if("V".equals(tpDontroleCarga)){
				if(!this.manifestoService.findManifestoVolumeSobra(idControleCarga, volumeNotaFiscal.getIdVolumeNotaFiscal(), filial.getIdFilial(), "V")){
					getVolumeSobraDAO().store(vs);
				}
			}else if("C".equals(tpDontroleCarga)){
				if(!this.manifestoService.findManifestoVolumeSobra(idControleCarga, volumeNotaFiscal.getIdVolumeNotaFiscal(), filial.getIdFilial(), "E")){
					getVolumeSobraDAO().store(vs);
				}
			}
		}
	}

	public ManifestoService getManifestoService() {
		return manifestoService;
	}

	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public PedidoColetaService getPedidoColetaService() {
		return pedidoColetaService;
	}

	public void setPedidoColetaService(PedidoColetaService pedidoColetaService) {
		this.pedidoColetaService = pedidoColetaService;
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
}