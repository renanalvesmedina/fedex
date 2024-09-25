package com.mercurio.lms.vendas.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.vendas.model.ServicoAdicionalClienteDestinatario;
import com.mercurio.lms.vendas.model.dao.ServicoAdicionalClienteDestinatarioDAO;

/**
 * Classe de servi�o para CRUD:
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.vendas.servicoAdicionalClienteDestinatarioService"
 */
public class ServicoAdicionalClienteDestinatarioService extends CrudService<ServicoAdicionalClienteDestinatario, Long> {
	/**
	 * Recupera uma inst�ncia de <code>ServicoAdicionalClienteDestinatario</code>
	 * a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ServicoAdicionalClienteDestinatario findById(java.lang.Long id) {
		return (ServicoAdicionalClienteDestinatario) super.findById(id);
	}
	
	public List<ServicoAdicionalClienteDestinatario> findByIdServicoAdicionalCliente(Long id) {	
		if(id == null) {					
			return new ArrayList<ServicoAdicionalClienteDestinatario>();					
		}
		return getServicoAdicionalClienteDestinatarioDAO().findByIdServicoAdicionalCliente(id);
	}
	
	public ServicoAdicionalClienteDestinatario findByIdServicoAdicionalClienteByIdClienteDestinatario(Long idServicoAdicionalCliente, Long idClienteDestinatario) {	
		return getServicoAdicionalClienteDestinatarioDAO().findByIdServicoAdicionalClienteByIdClienteDestinatario(idServicoAdicionalCliente, idClienteDestinatario);
	}
	
	@Override
	public void removeById(Long id) {
		// TODO Auto-generated method stub
		super.removeById(id);
	}
	
	@Override
	public void removeByIds(List<Long> ids) {
		// TODO Auto-generated method stub
		super.removeByIds(ids);
	}
	
	@Override
	public Serializable store(ServicoAdicionalClienteDestinatario bean) {
		// TODO Auto-generated method stub
		return super.store(bean);
	}
	
	/*
	 * GETTERS E SETTERS
	 */
	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos
	 * dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setServicoAdicionalClienteDestinatarioDAO(ServicoAdicionalClienteDestinatarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 * 
	 * @return Inst�ncia do DAO.
	 */
	private ServicoAdicionalClienteDestinatarioDAO getServicoAdicionalClienteDestinatarioDAO() {
		return (ServicoAdicionalClienteDestinatarioDAO) getDao();
	}
}