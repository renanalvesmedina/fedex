package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.RotasExpressas;
import com.mercurio.lms.carregamento.model.dao.RotasExpressasDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.rotasExpressasService"
 */
public class RotasExpressasService extends CrudService<RotasExpressas, Long> {

	/**
	 * Recupera uma instância de <code>RotasExpressas</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public RotasExpressas findById(java.lang.Long id) {
        return (RotasExpressas)super.findById(id);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setRotasExpressasDAO(RotasExpressasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private RotasExpressasDAO getRotasExpressasDAO() {
        return (RotasExpressasDAO) getDao();
    }


    public List findByNrRotaIdaVolta(Integer nrRotaIdaVolta, String tpAgrupador) {
    	return getRotasExpressasDAO().findByNrRotaIdaVolta(nrRotaIdaVolta, tpAgrupador);
    }
    
    public List findByNrRotaLmsFilialOrigemFilialDestino(Integer nrRota, Integer nrRotaLms ,String filialOrigem, String filialDestino) {
    	return getRotasExpressasDAO().findByNrRotaLmsFilialOrigemFilialDestino(nrRota,nrRotaLms,filialOrigem, filialDestino);
    }
    
	public List findByNrRotaLmsFilialOrigemFilialDestinoAtivas(Integer nrRota,Integer nrRotaLms, String filialOrigem, String filialDestino) {
		return getRotasExpressasDAO().findByNrRotaLmsFilialOrigemFilialDestinoAtivas(nrRota, nrRotaLms, filialOrigem, filialDestino);
	}
}