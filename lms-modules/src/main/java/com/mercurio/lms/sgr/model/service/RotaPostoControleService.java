package com.mercurio.lms.sgr.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.municipios.model.Rota;
import com.mercurio.lms.municipios.model.service.RotaService;
import com.mercurio.lms.sgr.model.RotaPostoControle;
import com.mercurio.lms.sgr.model.dao.RotaPostoControleDAO;
import com.mercurio.lms.util.JTFormatUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.sgr.rotaPostoControleService"
 */
public class RotaPostoControleService extends CrudService<RotaPostoControle, Long> {
	private RotaService rotaService;

	/**
	 * Recupera uma instância de <code>RotaPostoControle</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public RotaPostoControle findById(Long id) {
		return (RotaPostoControle) super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		RotaPostoControle rotaPostoControle = this.findById(id);
		Long idRota = rotaPostoControle.getRota().getIdRota();
		super.removeById(id);
        this.reordenaLista(idRota);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		//Assume que todas as entidades RotaPostoControle pertencem a uma mesma rota.
		Long idRota = null;
		if (ids!=null && ids.size()>0){
			RotaPostoControle rotaPostoControle = this.findById((Long)ids.get(0));
			idRota = rotaPostoControle.getRota().getIdRota();
		}
		super.removeByIds(ids);
		this.reordenaLista(idRota);			
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(RotaPostoControle bean) {
		return super.store(bean);
	}

	/**
	 * Busca o ultimo número de ordenação. Caso seja o unico registro desta rota
	 * coloca '1' como nrOrdem
	 * 
	 * @param bean objeto da model
	 * @return Object
	 */
	@Override
	protected RotaPostoControle beforeInsert(RotaPostoControle bean) {
		Short ultimoNrOrdem = getRotaPostoControleDAO().findLastNrOrdem(bean.getRota().getIdRota());

		int somaUltimoNrOrdem = 1;
		if (ultimoNrOrdem != null) {
			somaUltimoNrOrdem = Integer.parseInt(ultimoNrOrdem.toString());
			somaUltimoNrOrdem++;
		}
		ultimoNrOrdem = Short.valueOf(String.valueOf(somaUltimoNrOrdem));
		bean.setNrOrdem(ultimoNrOrdem);
		return super.beforeInsert(bean);
	}

	/**
	 * Verifica se existe um número de ordenação para o registro a ser
	 * atualizado. Caso este número não esteja vindo da view para ser
	 * atualizado, busca o número de ordenação do respectivo rotaPostoControle
	 * contido no banco.
	 * 
	 * @param bean objeto da model
	 * @return Object
	 */
	@Override
	protected RotaPostoControle beforeUpdate(RotaPostoControle bean) {
		if (bean.getNrOrdem() == null) {
			RotaPostoControle rotaPostoControleOld = bean;
			//Busca o objeto do banco e o atuliza, com excessao do número de ordenação
			bean = this.findById(bean.getIdRotaPostoControle());
			bean.setNrKmProximoPosto(rotaPostoControleOld.getNrKmProximoPosto());
			bean.setNrTempoProximoPosto(rotaPostoControleOld.getNrTempoProximoPosto());
			bean.setPostoControle(rotaPostoControleOld.getPostoControle());
			bean.setRota(rotaPostoControleOld.getRota());
		}
		return super.beforeUpdate(bean);
	}

	/**
	 * Método que recebe a coleção de rotas e vai atualizando o nrOrdem
	 * delas.
	 * 
	 * @param map contendo a lista de inserção
	 */
	public void storeListOrder(Map<String, Object> map) {
		List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("rotaPostosControle");
		RotaPostoControle rotaPostoControle;
		int nrOrdem = 0;
		for(Map<String, Object> mapRotaPostoControle : list) {
			rotaPostoControle = findById(Long.valueOf((String) mapRotaPostoControle.get("idRotaPostoControle")));
			nrOrdem = Integer.parseInt((String) mapRotaPostoControle.get("nrOrdem")) + 1;
			rotaPostoControle.setNrOrdem(Short.valueOf(String.valueOf(nrOrdem)));
			getRotaPostoControleDAO().store(rotaPostoControle);
		}
	}

    /**
     * Método para reordenar a lista, quando é alterado o tipoExigencia de um item.
     * @param idTipoExigencia
     */
    public void reordenaLista (Long idRota) {
    	Map<String, Object> criteria = new HashMap<String, Object>();
    	criteria.put("idRota", idRota);
    	List<RotaPostoControle> result = getRotaPostoControleDAO().findPostosControleByIdRota(criteria);
    	short order = 1;
    	for(RotaPostoControle rotaPostoControle : result) {
			Short nrOrdem = Short.valueOf(order);
			rotaPostoControle.setNrOrdem(nrOrdem);
			this.store(rotaPostoControle);
			order++;
		}
    }

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setRotaPostoControleDAO(RotaPostoControleDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private RotaPostoControleDAO getRotaPostoControleDAO() {
		return (RotaPostoControleDAO) getDao();
	}

	/**
	 * Busca o numero de registros encontrados
	 * 
	 * @param criteria
	 * @return Integer quantidade de registros encontrados
	 */
	public Integer getRowCountRotaPostoControle(Map<String, Object> criteria) {
		String somenteRotasPostoControle = (String) criteria.get("somenteRotasPostoControle");
		criteria.remove("somenteRotasPostoControle");
		Integer rowCount = Integer.valueOf(0);
		Long idRota = (((Map<String, Object>)criteria.get("rota")).get("idRota").toString().equals(""))? null : Long.valueOf(((Map<String, Object>)criteria.get("rota")).get("idRota").toString());
		if (somenteRotasPostoControle.equals("true")) {
			rowCount = getRotaPostoControleDAO().getRowCountByRota(criteria);
		} else {
			criteria.remove("rota");
			rowCount = this.getRotaPostoControleDAO().getRowCountRota(idRota);
		}
		criteria.put("somenteRotasPostoControle", somenteRotasPostoControle);
		return rowCount;
	}

	/**
	 * Retorna um resultSetPage
	 * 
	 * @param Map criteria
	 * @return ResultSetPage
	 */
	public ResultSetPage findRotasPaginated(Map<String, Object> criteria) {
		ResultSetPage resultSetPage = null;
		boolean somenteRotasPostoControle = Boolean.valueOf((String) criteria.get("somenteRotasPostoControle")).booleanValue();
		Long idRota = (((Map<String, Object>)criteria.get("rota")).get("idRota").toString().equals(""))? null : Long.valueOf(((Map<String, Object>)criteria.get("rota")).get("idRota").toString());
		criteria.remove("somenteRotasPostoControle");
		if (somenteRotasPostoControle) {
			criteria.put("_order", "rota.dsRota:asc");
			resultSetPage = getRotaPostoControleDAO().findPaginatedGroupByRota(criteria, FindDefinition.createFindDefinition(criteria));
		} else {
			criteria.remove("rota");
			criteria.put("_order", "dsRota:asc");
			resultSetPage = this.getRotaPostoControleDAO().findPaginatedRota(idRota, FindDefinition.createFindDefinition(criteria));
		}
		return resultSetPage;
	}

	/**
	 * Captura uma lista de RotaPostoControle por Rota
	 * 
	 * @param criteria
	 * @return Map com uma list de RotaPostoControle por Rota
	 */
	public Map<String, Object> findRotaPostoControleByRota(Map<String, Object> criteria) {
		List<Map<String, Object>> rotaPostosControle = new ArrayList<Map<String, Object>>();
		RotaPostoControle rotaPostoControle = null;
		Rota rota = null;

		List<RotaPostoControle> result = getRotaPostoControleDAO().findPostosControleByIdRota(criteria);

		// Caso nao exista postos de controle vinculado a esta rota.
		if (result.size() == 0) {
			Long idRota = Long.valueOf(criteria.get("idRota").toString());
			rota = getRotaService().findById(idRota);
		} else {
			rotaPostoControle = (RotaPostoControle) result.get(0);
			rota = rotaPostoControle.getRota();
			for(Iterator<RotaPostoControle> iter = result.iterator(); iter.hasNext();) {
				rotaPostoControle = (RotaPostoControle) iter.next();
				Map<String, Object> mapPostoControle = new HashMap<String, Object>();
				Map<String, Object> mapRotaPostoControle = new HashMap<String, Object>();
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append(rotaPostoControle.getPostoControle().getNmPostoControlePassaporte());
				stringBuffer.append(" - ");
				stringBuffer.append(rotaPostoControle.getPostoControle().getNmLocal());
				stringBuffer.append(" - ");
				stringBuffer.append(rotaPostoControle.getPostoControle().getMunicipio().getNmMunicipio());
				stringBuffer.append(" / ");
				stringBuffer.append(rotaPostoControle.getPostoControle().getMunicipio().getUnidadeFederativa().getSgUnidadeFederativa());
				stringBuffer.append(" - ");
				stringBuffer.append("Rodovia ");
				stringBuffer.append(rotaPostoControle.getPostoControle().getRodovia().getSgRodovia());
				if (rotaPostoControle.getNrTempoProximoPosto()!=null){
					stringBuffer.append(this.getVazio(JTFormatUtils.formatTime((rotaPostoControle.getNrTempoProximoPosto().longValue() * 60), 2, 1), ""));
				}
				stringBuffer.append(this.getVazio(rotaPostoControle.getNrKmProximoPosto(), "km"));
				
				mapPostoControle.put("dsPostoControle", stringBuffer.toString());
				mapRotaPostoControle.put("idRotaPostoControle",	rotaPostoControle.getIdRotaPostoControle());
				mapRotaPostoControle.put("nrOrdem", rotaPostoControle.getNrOrdem());
				mapRotaPostoControle.put("postoControle", mapPostoControle);

				rotaPostosControle.add(mapRotaPostoControle);
			}
		}

		Map<String, Object> mapRota = new HashMap<String, Object>();
		mapRota.put("idRota", rota.getIdRota());
		mapRota.put("dsRota", rota.getDsRota());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rota", mapRota);
		map.put("rotaPostosControle", rotaPostosControle);

		return map;
	}
	
	/**
	 * Verifica se o objeto contem um valor ou se ele e nulo
	 * Impede que seja mostrado o valor 'null' em campo.
	 * 
	 * @param object a ser validado 
	 * @param complement complemento da String
	 * @return String
	 */
	private String getVazio(Object object, String complement) {
		return (object==null)? " " : " - " + object.toString() + complement;
	}
	
	public RotaService getRotaService() {
		return rotaService;
	}

	public void setRotaService(RotaService rotaService) {
		this.rotaService = rotaService;
	}

}