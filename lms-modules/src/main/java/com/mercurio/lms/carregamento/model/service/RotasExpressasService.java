package com.mercurio.lms.carregamento.model.service;

import java.util.List;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.RotasExpressas;
import com.mercurio.lms.carregamento.model.dao.RotasExpressasDAO;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.rotasExpressasService"
 */
public class RotasExpressasService extends CrudService<RotasExpressas, Long> {

	/**
	 * Recupera uma inst�ncia de <code>RotasExpressas</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public RotasExpressas findById(java.lang.Long id) {
        return (RotasExpressas)super.findById(id);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setRotasExpressasDAO(RotasExpressasDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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