package com.mercurio.lms.gm.model.service;

import java.io.Serializable;

import org.hibernate.LockMode;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.CabecalhoCarregamento;
import com.mercurio.lms.carregamento.model.Carregamento;
import com.mercurio.lms.carregamento.model.DetalheCarregamento;
import com.mercurio.lms.carregamento.model.TotalCarregamento;
import com.mercurio.lms.gm.model.dao.CabecalhoCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.CarregamentoDAO;
import com.mercurio.lms.gm.model.dao.DetalheCarregamentoDAO;
import com.mercurio.lms.gm.model.dao.TotalCarregamentoDAO;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * 
 * @spring.bean id="lms.gm.carregamentoService"
 */
public class CarregamentoService extends CrudService<Carregamento, Long> {

	private CabecalhoCarregamentoDAO cabecalhoCarregamentoDAO;
	private DetalheCarregamentoDAO detalheCarregamentoDAO;
	private TotalCarregamentoDAO totalCarregamentoDAO;
	private CarregamentoDAO carregamentoDAO;

	public void setCabecalhoCarregamentoDAO(CabecalhoCarregamentoDAO cabecalhoCarregamentoDAO) {
		this.cabecalhoCarregamentoDAO = cabecalhoCarregamentoDAO;
	}

	public void setDetalheCarregamentoDAO(DetalheCarregamentoDAO detalheCarregamentoDAO) {
		this.detalheCarregamentoDAO = detalheCarregamentoDAO;
	}

	public void setTotalCarregamentoDAO(TotalCarregamentoDAO totalCarregamentoDAO) {
		this.totalCarregamentoDAO = totalCarregamentoDAO;
	}

	@Override
	public Serializable store(Carregamento bean) {
		// TODO Auto-generated method stub
		return super.store(bean);
	}

	/**
	 * Serviço que busca o carregamento de acordo com a placa passada e com os status 1,2 e 6(Aberto, Carregando, Concluído). Este Serviço é usado na tela de embarque do MWW-GM para verificar o status do carregamento.
	 * 
	 * Demanda LMS-1538
	 * 
	 * @author mxavier@voiza.com.br
	 * @param placaVeiculo
	 * @return
	 */
	public Carregamento findCarregamentoByPlacaVeiculo(String placaVeiculo){
		return carregamentoDAO.findCarregamentoByPlacaVeiculo(placaVeiculo);
	}
	
	/**
	 * Recebe um CabecalhoCarregamento para inclusão na base de dados.
	 * 
	 * @author Gian Frick
	 * @param CabecalhoCarregamento
	 *            cabecalhoCarregamento
	 */
	public void storeCabecalhoCarregamento(CabecalhoCarregamento cabecalhoCarregamento) {
		cabecalhoCarregamentoDAO.store(cabecalhoCarregamento);
		cabecalhoCarregamentoDAO.getAdsmHibernateTemplate().getSessionFactory().getCurrentSession().lock(cabecalhoCarregamento, LockMode.NONE);
	}

	/**
	 * Busca um CabecalhoCarregamento Pelo Mapa de carregamento
	 * 
	 * @author Samuel Alves
	 * @param CabecalhoCarregamento
	 *            cabecalhoCarregamento
	 */
	public CabecalhoCarregamento findCabecalhoCarregamentoByMapaCarregamento(Long mapa) {
		return cabecalhoCarregamentoDAO.findCabecalhoByMapaCarregamento(mapa);
	}

	/**
	 * Recebe um DetalheCarregamento para inclusão na base de dados.
	 * 
	 * @author Gian Frick
	 * @param DetalheCarregamento
	 *            detalheCarregamento
	 */
	public void storeDetalheCarregamento(DetalheCarregamento detalheCarregamento) {
		detalheCarregamentoDAO.store(detalheCarregamento);
	}

	/**
	 * Busca um DetalheCarregamento Pelo Codigo de barras
	 * 
	 * @author Samuel Alves
	 * @param String
	 *            codigoVolume
	 */
	public DetalheCarregamento findDetalheCarregamentoByCodigoVolume(String codigoVolume) {
		return detalheCarregamentoDAO.findDetalheCarregamentoByCodigoVolume(codigoVolume);
	}

	public Carregamento findCarregamentoById(Long id) {
		return carregamentoDAO.findCarregamentoByid(id);
	}

	public Serializable findById(Long id) {
		return super.findById(id);
	}

	/**
	 * Recebe um TotalCarregamento para inclusão na base de dados.
	 * 
	 * @author Gian Frick
	 * @param TotalCarregamento
	 *            totalCarregamento
	 */
	public void storeTotalCarregamento(TotalCarregamento totalCarregamento) {
		totalCarregamentoDAO.store(totalCarregamento);
	}

	/**
	 * Busca um TotalCarregamento Pelo MapaCarregamento
	 * 
	 * @author Samuel Alves
	 * @param Long
	 *            mapaCarregamento
	 */
	public TotalCarregamento findTotalCarregamentoByMapaCarregamento(Long mapaCarregamento) {
		return totalCarregamentoDAO.findTotalByMapaCarregamento(mapaCarregamento);
	}

	public void removeDetalheCarregamento(Long idDetalheCarregamento) {
		detalheCarregamentoDAO.removeById(idDetalheCarregamento);
	}

	public void setCarregamentoDAO(CarregamentoDAO carregamentoDAO) {
		this.carregamentoDAO = carregamentoDAO;
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setCarregamentoServiceDAO(CarregamentoDAO dao) {
		setDao(dao);
	}

}
