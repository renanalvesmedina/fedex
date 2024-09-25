package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.TrechoRotaIdaVolta;
import com.mercurio.lms.municipios.model.dao.TrechoRotaIdaVoltaDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.trechoRotaIdaVoltaService"
 */
public class TrechoRotaIdaVoltaService extends CrudService<TrechoRotaIdaVolta, Long> {


	/**
	 * Recupera uma instância de <code>TrechoRotaIdaVolta</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public TrechoRotaIdaVolta findById(java.lang.Long id) {
        return (TrechoRotaIdaVolta)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TrechoRotaIdaVolta bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTrechoRotaIdaVoltaDAO(TrechoRotaIdaVoltaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TrechoRotaIdaVoltaDAO getTrechoRotaIdaVoltaDAO() {
        return (TrechoRotaIdaVoltaDAO) getDao();
    }
    
    public List findTrechosByIdRotaIdaVolta(Long idRotaIdaVolta) {
    	return getTrechoRotaIdaVoltaDAO().findTrechosByIdRotaIdaVolta(idRotaIdaVolta);
    }

    public Integer getRowCountByRotaIdaVolta(Long idRotaIdaVolta) {
    	return getTrechoRotaIdaVoltaDAO().getRowCountByRotaIdaVolta(idRotaIdaVolta);
    }

    /**
     * Validação ao salvar trecho da rota ida.
     * @param idTrechoRotaIdaVolta
     * @param idRotaIdaVolta
     * @param idFilialOrigem
     * @param hrSaida
     * @return trechos onde é possível alterar a rota.
     */
    public List findTrechosComHoraInformada(Long idTrechoRotaIdaVolta, Long idRotaIdaVolta, Long idFilialOrigem, TimeOfDay hrSaida) {
    	return getTrechoRotaIdaVoltaDAO().findTrechosComHoraInformada(idTrechoRotaIdaVolta, idRotaIdaVolta, idFilialOrigem, hrSaida);
    }
    
    /**
     * Valida se trecho compreende as filiais de origem e destino da rota
     * @param idTrechoIdaVolta
     * @return
     */
    public boolean validateTrechoFromWholeRota(Long idTrechoIdaVolta) {
    	return getTrechoRotaIdaVoltaDAO().validateTrechoFromWholeRota(idTrechoIdaVolta);
    }
    
    public List findToConsultarRotas(Long idRotaIdaVolta) {
    	return getTrechoRotaIdaVoltaDAO().findToConsultarRotas(idRotaIdaVolta);
    }

    /**
     * @param idRotaIdaVolta (required)
     * @return 
     */
    public List findToTrechosByTrechosRota(Long idRotaIdaVolta, YearMonthDay dtSaidaRota) {
    	List list = getTrechoRotaIdaVoltaDAO().findToTrechosByTrechosRota(idRotaIdaVolta, dtSaidaRota);
    	List newList = new ArrayList();
    	for (Iterator iter = list.iterator() ; iter.hasNext() ; ) {
    		Object[] obj = (Object[])iter.next();
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("idTrechoRotaIdaVolta",obj[0]);
    		tfm.put("hrSaida",obj[1]);
    		tfm.put("nrTempoViagem",obj[2]);
    		tfm.put("nrTempoOperacao",obj[3]);
			tfm.put("nrDistancia",obj[4]);
			tfm.put("nrOrdemOrigem",obj[5]);
			tfm.put("idFilialOrigem",obj[6]);
			tfm.put("nrOrdemDestino",obj[7]);
			tfm.put("idFilialDestino",obj[8]);
			tfm.put("dtSaida",obj[9]);
    		newList.add(tfm);
    	}
    	return newList;
    }

    /**
     * Consulta trecho por filial origem e rota.
     * @param idRotaIdaVolta
     * @param idFilial
     * @return
     */
    public TrechoRotaIdaVolta findTrechoByIdRotaAndFilialOrigem(Long idRotaIdaVolta) {
    	return getTrechoRotaIdaVoltaDAO().findTrechoByIdRotaAndFilialOrigem(idRotaIdaVolta);
    }


    /**
     * Consulta trecho correspondente a rota completa
     * 
     * @param idRotaIdaVolta
     * @return
     */
    public TrechoRotaIdaVolta findByTrechoRotaCompleta(Long idRotaIdaVolta) {
    	TrechoRotaIdaVolta triv = getTrechoRotaIdaVoltaDAO().findByTrechoRotaCompleta(idRotaIdaVolta);
    	if (triv == null)
    		throw new BusinessException("LMS-05110");
    	return triv;
    }

	public TrechoRotaIdaVolta findTrechoByIdRotaIdaVoltaAndFilialUsuarioLogado(Long idRotaIdaVolta) {
		return getTrechoRotaIdaVoltaDAO().findTrechoByIdRotaIdaVoltaAndFilialUsuarioLogado(idRotaIdaVolta);
	}
	
	public List<TrechoRotaIdaVolta> findByTrechoRota(Long idRotaIdaVolta, Long idFilialOrigem, Long idFilialDestino) {
		return getTrechoRotaIdaVoltaDAO().findByTrechoRota(idRotaIdaVolta, idFilialOrigem, idFilialDestino);
	}
}