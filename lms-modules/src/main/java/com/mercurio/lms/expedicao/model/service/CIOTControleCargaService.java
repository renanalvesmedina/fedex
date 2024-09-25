package com.mercurio.lms.expedicao.model.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CIOTControleCarga;
import com.mercurio.lms.expedicao.model.dao.CIOTControleCargaDAO;

/**
 * Classe de serviço para CRUD:
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.expedicao.ciotControleCargaService"
 */
public class CIOTControleCargaService extends CrudService<CIOTControleCarga, Long> {

	public void setCIOTControleCargaDAO(CIOTControleCargaDAO dao) {
		setDao(dao);
	}

	private CIOTControleCargaDAO getCIOTControleCargaDAO() {
		return (CIOTControleCargaDAO) getDao();
	}

	public Serializable store(CIOTControleCarga bean) {
		return super.store(bean);
	}
	
    public void removeById(Long id) {
        super.removeById(id);
    }

	public CIOTControleCarga findByIdForMonitoramento(Long id) {
		return getCIOTControleCargaDAO().findByIdForMonitoramento(id);
	}
	
	public Integer getRowCountMonitoramento(TypedFlatMap criteria){
		return getCIOTControleCargaDAO().getRowCountMonitoramento(criteria);
	}
	
	public ResultSetPage<Map<String, Object>>  findPaginatedMonitoramento(TypedFlatMap criteria){
		return getCIOTControleCargaDAO().findPaginatedMonitoramento(criteria);
	}
	
	public CIOTControleCarga findById(Long id) {
		return (CIOTControleCarga) super.findById(id);
	}
	
	public CIOTControleCarga findByIdControleCargaIdMeioTransporte(Long idControleCarga, Long idMeioTransporte) {
		return getCIOTControleCargaDAO().findByIdControleCargaIdMeioTransporte(idControleCarga, idMeioTransporte);
	}
	
	public CIOTControleCarga findByIdControleCarga(Long idControleCarga) {
		return getCIOTControleCargaDAO().findByIdControleCarga(idControleCarga);
	}
	
	public CIOTControleCarga findGeradoAPartirDe(Long idMeioTransporte, YearMonthDay data) {
		return getCIOTControleCargaDAO().findGeradoAPartirDe(idMeioTransporte, data);
	}
	
	public CIOTControleCarga findGeradoByIdControleCarga(Long idControleCarga) {
		return getCIOTControleCargaDAO().findGeradoByIdControleCarga(idControleCarga);
	}
	
	public List<CIOTControleCarga> findByCiotDiferenteControleCarga(Long idCiot, Long idControleCarga) {
		return getCIOTControleCargaDAO().findByCiotDiferenteControleCarga(idCiot, idControleCarga);
	}

}
