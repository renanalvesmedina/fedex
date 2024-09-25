package com.mercurio.lms.gm.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.DetalheRota;
import com.mercurio.lms.carregamento.model.RotaEmbarque;
import com.mercurio.lms.gm.model.dao.DetalheRotaDAO;
import com.mercurio.lms.gm.model.dao.EmbarqueDAO;
import com.mercurio.lms.gm.model.dao.RotaEmbarqueDAO;

/**
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.gm.rotaEmbarqueService"
 */
public class RotaEmbarqueService  extends CrudService<RotaEmbarque, Long>{
	private EmbarqueDAO embarqueDao;
	private RotaEmbarqueDAO rotaEmbarqueDao;
	private DetalheRotaDAO detalheRotaDao;
	
	
	
	public EmbarqueDAO getEmbarqueDao() {
		return embarqueDao;
	}



	public void setEmbarqueDao(EmbarqueDAO embarqueDao) {
		this.embarqueDao = embarqueDao;
	}



	public RotaEmbarqueDAO getRotaEmbarqueDao() {
		return rotaEmbarqueDao;
	}



	public void setRotaEmbarqueDao(RotaEmbarqueDAO rotaEmbarqueDao) {
		this.rotaEmbarqueDao = rotaEmbarqueDao;
	}



	public DetalheRotaDAO getDetalheRotaDao() {
		return detalheRotaDao;
	}



	public void setDetalheRotaDao(DetalheRotaDAO detalheRotaDao) {
		this.detalheRotaDao = detalheRotaDao;
	}



	/**
	 * Recebe a sigla da rota e a descrição para realizar a pesquisa. 
	 * 
	 * @author Samuel Alves
	 * @param String siglaRotaMaster
	 * @param String nomeRotaMaster
	 * @return Mapa de objetos, para preencher as listas de Conhecimentos Unitizados.
	 */
	public Map findRotaMasterBySiglaDescricao(String siglaRotaMaster, String nomeRotaMaster){
		
		RotaEmbarque rotaEmbarque = new RotaEmbarque();
		
		if(siglaRotaMaster != null && nomeRotaMaster!= null){
			rotaEmbarque = rotaEmbarqueDao.findRotaBySiglaNome(siglaRotaMaster, nomeRotaMaster);
			 if(rotaEmbarque == null){
				 throw new BusinessException("LMS-04334");
			 }
		}else if(siglaRotaMaster != null){
			 rotaEmbarque = rotaEmbarqueDao.findRotaBySigla(siglaRotaMaster);
			 if(rotaEmbarque == null){
				 throw new BusinessException("LMS-04334");
			 }
		}else if(nomeRotaMaster!= null){
			rotaEmbarque = rotaEmbarqueDao.findRotaByNome(nomeRotaMaster);
			if(rotaEmbarque == null){
				 throw new BusinessException("LMS-04334");
			 }
		}else{
			throw new BusinessException("parametrosPesquisaNaoInformados");
		}
		
		List<DetalheRota> listDetalheRota = detalheRotaDao.findDetalheRotaByIdRotaEmbarque(rotaEmbarque.getIdRotaEmbarque());
		
		
		ArrayList<Map<String, Object>> listMapa = new ArrayList<Map<String, Object>>();
		
		for(DetalheRota detalhe :listDetalheRota){
			Map mapDetalhe = new HashMap();
			mapDetalhe.put("idDetalheRota", detalhe.getIdDetalheRota());
			mapDetalhe.put("siglaRota", detalhe.getSiglaRota());
			listMapa.add(mapDetalhe);
		}
 
		Map map = new HashMap();
		
		map.put("idRotaEmbarque", rotaEmbarque.getIdRotaEmbarque());
		map.put("nomeRota", rotaEmbarque.getNomeRota());
		map.put("siglaRota", rotaEmbarque.getSiglaRota());
		map.put("horarioCorte", rotaEmbarque.getHorarioCorte());
		map.put("subRotas", listMapa);
		
		return map;		
	}
	
	

	/**
	 * Recupera uma instância de <code>RotaEmbarque</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public RotaEmbarque findById(java.lang.Long id) {
		return (RotaEmbarque)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		List <DetalheRota> listDetalheRota = getDetalheRotaDao().findDetalheRotaByIdRotaEmbarque(id);
		for(DetalheRota dr : listDetalheRota){
			getDetalheRotaDao().removeById(dr.getIdDetalheRota());
		}
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		for(Long id : ids){
			this.removeById(id);
	}
	}
	
	
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	
	public Serializable store(RotaEmbarque bean) {
		RotaEmbarque rotaEmbarque = rotaEmbarqueDao.findRotaByIdSigla(bean);
		
			if(rotaEmbarque != null){
				throw new BusinessException("LMS-04305");
			}
		return super.store(bean);
	}
	
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setRotaEmbarqueDAO(RotaEmbarqueDAO dao) {
		setDao( dao );
	}
	
	
	
}
