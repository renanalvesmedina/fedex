package com.mercurio.lms.fretecarreteirocoletaentrega.model.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.ConteudoParametroFilial;
import com.mercurio.lms.configuracoes.model.service.ConteudoParametroFilialService;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.TipoTabelaColetaEntrega;
import com.mercurio.lms.fretecarreteirocoletaentrega.model.dao.TipoTabelaColetaEntregaDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.fretecarreteirocoletaentrega.tipoTabelaColetaEntregaService"
 */
public class TipoTabelaColetaEntregaService extends CrudService<TipoTabelaColetaEntrega, Long> {
	private TabelaColetaEntregaService tabelaColetaEntregaService;
	private ConteudoParametroFilialService conteudoParametroFilialService;
	
	private static final String PARAMETRO_FILIAL = "ATIVA_CALCULO_PADRAO";
	private static final String SIM = "S";
	
	public void setTabelaColetaEntregaService(TabelaColetaEntregaService tabelaColetaEntregaService) {
		this.tabelaColetaEntregaService = tabelaColetaEntregaService;
	}

	/**
	 * Recupera uma inst�ncia de <code>TipoTabelaColetaEntrega</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws
	 */
    public TipoTabelaColetaEntrega findById(java.lang.Long id) {
        return (TipoTabelaColetaEntrega)super.findById(id);
    }

    /**
     * Retorna os tipos de tabela de coleta entrega ordenadas pela descri��o.
     * @author Andr�sa Vargas
     * @param criteria
     * @return
     */
    public List findByOrder(Map<String, Object> criteria) { 
    	List<String> listaOrder = new ArrayList<String>(1);
		listaOrder.add("dsTipoTabelaColetaEntrega:desc");
		return getTipoTabelaColetaEntregaDAO().findListByCriteria(criteria,listaOrder);
    }

    public TipoTabelaColetaEntrega findTipoTabelaColetaEntregaByBlNormal(boolean blNormal) {
		return getTipoTabelaColetaEntregaDAO().findTipoTabelaColetaEntregaByBlNormal(blNormal);
    }

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
    @Override
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 */
    @Override
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }

	@Override
    protected TipoTabelaColetaEntrega beforeStore(TipoTabelaColetaEntrega bean) {
    	if (bean.getBlNormal().equals(Boolean.TRUE) && getTipoTabelaColetaEntregaDAO().validateExistBeforeStore(bean.getIdTipoTabelaColetaEntrega()))
    		throw new BusinessException("LMS-25001");
    	return super.beforeStore(bean);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
    public Serializable store(TipoTabelaColetaEntrega bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setTipoTabelaColetaEntregaDAO(TipoTabelaColetaEntregaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private TipoTabelaColetaEntregaDAO getTipoTabelaColetaEntregaDAO() {
        return (TipoTabelaColetaEntregaDAO) getDao();
    }

    /**
     * 
     * @param idFilial
     * @param idMeioTransporte
     * @return
     */
    public List<TypedFlatMap> findTipoTabelaColetaEntregaWithTabelaColetaEntrega(Long idFilial, Long idMeioTransporte, Long idRotaColetaEntrega) {
    	List<TypedFlatMap> retorno = new ArrayList<TypedFlatMap>();
    	//se habilitado calculo padrao segue nova estrutura
    	ConteudoParametroFilial conteudoParametroFilial = conteudoParametroFilialService.findByNomeParametro(SessionUtils.getFilialSessao().getIdFilial(), PARAMETRO_FILIAL, false, true);
		if (conteudoParametroFilial != null && SIM.equalsIgnoreCase(conteudoParametroFilial.getVlConteudoParametroFilial())) {
			TypedFlatMap tfm = new TypedFlatMap();
			tfm.put("idTipoTabelaColetaEntrega", "");
    		tfm.put("dsTipoTabelaColetaEntrega", "Calculo Padr�o");
    		tfm.put("blNormal", null);
    		tfm.put("tpCalculoTabelas", "PA");		
    		tfm.put("tpCalculo", "PA");
    		retorno.add(tfm);
    		return retorno;
		}
    	
    	List<TabelaColetaEntrega> result = tabelaColetaEntregaService.validateTabelaColetaEntrega(idFilial, idMeioTransporte, idRotaColetaEntrega);
    	for(TabelaColetaEntrega tabelaColetaEntrega : result) {
    		TypedFlatMap tfm = new TypedFlatMap();
    		tfm.put("tpCalculo", tabelaColetaEntrega.getTpCalculo().getValue());
    		tfm.put("idTabelaColetaEntrega", tabelaColetaEntrega.getIdTabelaColetaEntrega());
    		if (tabelaColetaEntrega.getTipoTabelaColetaEntrega() != null) {
	    		tfm.put("dsTipoTabelaColetaEntrega", tabelaColetaEntrega.getTipoTabelaColetaEntrega().getDsTipoTabelaColetaEntrega());
	    		tfm.put("idTipoTabelaColetaEntrega", tabelaColetaEntrega.getTipoTabelaColetaEntrega().getIdTipoTabelaColetaEntrega());
	    		tfm.put("blNormal", tabelaColetaEntrega.getTipoTabelaColetaEntrega().getBlNormal());
    		} else {
    			tfm.put("idTipoTabelaColetaEntrega", "");
	    		tfm.put("dsTipoTabelaColetaEntrega", "");
	    		tfm.put("blNormal", null);
    		}
    		retorno.add(tfm);
    	}

    	return retorno;
    }
    
	public void setConteudoParametroFilialService(
			ConteudoParametroFilialService conteudoParametroFilialService) {
		this.conteudoParametroFilialService = conteudoParametroFilialService;
	}
}