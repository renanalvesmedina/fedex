package com.mercurio.lms.carregamento.model.service;

import java.util.Iterator;
import java.util.List;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.RotasExpressas;
import com.mercurio.lms.carregamento.model.TrechoCorporativo;
import com.mercurio.lms.carregamento.model.dao.TrechoCorporativoDAO;
import com.mercurio.lms.municipios.model.service.FilialService;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.carregamento.trechoCorporativoService"
 */
public class TrechoCorporativoService extends CrudService<TrechoCorporativo, Long> {

	private ControleCargaService controleCargaService;
	private FilialService filialService;
	private RotasExpressasService rotasExpressasService;
	

	public void setRotasExpressasService(RotasExpressasService rotasExpressasService) {
		this.rotasExpressasService = rotasExpressasService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


	/**
	 * Recupera uma inst�ncia de <code>TrechoCorporativo</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public TrechoCorporativo findById(java.lang.Long id) {
        return (TrechoCorporativo)super.findById(id);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(TrechoCorporativo bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTrechoCorporativoDAO(TrechoCorporativoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TrechoCorporativoDAO getTrechoCorporativoDAO() {
        return (TrechoCorporativoDAO) getDao();
    }
    
    /**
     * @param items
     */
    public void storeTrechoCorporativo(ItemList items) {
    	getTrechoCorporativoDAO().storeTrechoCorporativo(items);
	}


    public List findByIdControleCarga(Long idControleCarga, Long idFilialOrigem, Long idFilialDestino) {
    	return getTrechoCorporativoDAO().findByIdControleCarga(idControleCarga, idFilialOrigem, idFilialDestino);
    }

    /**
     * Verifica se existe Trecho corporativo
     * @author Andr� Valadas
     * @param idControleCarga
     */
    public Boolean verifyTrechoCoporativoByIdControleCarga(final Long idControleCarga) {
    	return getTrechoCorporativoDAO().verifyTrechoCoporativoByIdControleCarga(idControleCarga);
    }

    public void storeTrechoCorporativo(Long idControleCarga, Integer nrRotaIdaVolta, String tpAgrupador) {
		List listaRotasExpressas = rotasExpressasService.findByNrRotaIdaVolta(nrRotaIdaVolta, tpAgrupador);

		getTrechoCorporativoDAO().removeByIdControleCarga(idControleCarga);

		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);

		for (Iterator iter = listaRotasExpressas.iterator(); iter.hasNext();) {
			RotasExpressas re = (RotasExpressas)iter.next();
			TrechoCorporativo tc = new TrechoCorporativo();
			tc.setCodigo( re.getNrCodigo() );
			tc.setControleCarga(controleCarga);
			tc.setFilialByIdFilialDestino( filialService.findFilialBySgFilialLegado(re.getSgUnidDestino()) );
			tc.setFilialByIdFilialOrigem( filialService.findFilialBySgFilialLegado(re.getSgUnidOrigem()) );
			tc.setHrSaida(re.getHrSaida());
			tc.setIdTrechoCorporativo(null);
			tc.setQtdHorasPrev( Integer.valueOf(re.getNrHorasPrev()) );
			tc.setVlFaixa1(re.getVlFaixa1());
			tc.setVlFaixa2(re.getVlFaixa2());
			tc.setVlFaixa3(re.getVlFaixa3());
			store(tc);
		}
	}


    /**
     * Remove as inst�ncias do pojo de acordo com os par�metros recebidos.
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	getTrechoCorporativoDAO().removeByIdControleCarga(idControleCarga);
    }
}