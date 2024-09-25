package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.LacreControleCarga;
import com.mercurio.lms.carregamento.model.dao.LacreControleCargaDAO;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.lacreControleCargaService"
 */
public class LacreControleCargaService extends CrudService<LacreControleCarga, Long> {

	private ControleCargaService controleCargaService;
	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}

	/**
	 * Recupera uma instância de <code>LacreControleCarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public LacreControleCarga findById(java.lang.Long id) {
        return (LacreControleCarga)super.findById(id);
    }

    public List findByControleCarga(Long idControleCarga){
    	return getLacreControleCargaDAO().findByControleCarga(idControleCarga);
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
    public java.io.Serializable store(LacreControleCarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setLacreControleCargaDAO(LacreControleCargaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private LacreControleCargaDAO getLacreControleCargaDAO() {
        return (LacreControleCargaDAO) getDao();
    }
    
    /**
     * Retorna todos os lacres cujo status = FE, 
     * de acordo com o id do controle de carga.
     * @param idControleCarga
     * @return
     * @author luisfco
     */
    public List findLacresFechadosByIdControleCarga(Long idControleCarga) {
 	   return getLacreControleCargaDAO().findLacresFechadosByIdControleCarga(idControleCarga);    
 	}
    
    /**
     * Retorna o lacre de controle de cargas identificado pelo número do lacre e pelo idControleCarga.
     * @param idControleCarga
     * @param nrLacre
     * @return
     * @author luisfco
     */
    public LacreControleCarga findLacreControleCargaByIdControleCargaAndNrLacre(Long idControleCarga, String nrLacres) {
 	   return getLacreControleCargaDAO().findLacreControleCargaByIdControleCargaAndNrLacre(idControleCarga, nrLacres);
    }

    
    public List findLacreControleCargaByIdControleCargaAndTpStatus(Long idControleCarga, String tpStatus) {
    	return getLacreControleCargaDAO().findLacreControleCargaByIdControleCargaAndTpStatus(idControleCarga, tpStatus);
    }
    
    /**
     * Retorna uma list de Lacres de Controle de Carga
     * 
     * @param idControleCarga
     * @return
     */
    public List findLacreControleCarga(Long idControleCarga){
    	return this.getLacreControleCargaDAO().findLacreControleCargaByIdControleCarga(idControleCarga);
    }
    
    /**
     * Retorna o número de registros da uma list de Lacres de Controle de Carga
     * 
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountLacreControleCarga(Long idControleCarga){
    	return this.getLacreControleCargaDAO().getRowCountLacreControleCargaByIdControleCarga(idControleCarga);
    }    
    
    /**
     * Salva os lacres de um controle de carga
     * 
     * @param listLacreControleCarga
     */
    public void storeLacresControleCarga(List listLacreControleCarga) {
		this.getLacreControleCargaDAO().storeLacresControleCarga(listLacreControleCarga);
	}	
    

    /**
     * 
     * @param idControleCarga
     * @param findDefinition
     * @return
     */
    public ResultSetPage findPaginatedByControleCarga(Long idControleCarga, FindDefinition findDefinition) {
    	ResultSetPage rsp = getLacreControleCargaDAO().findPaginatedByControleCarga(idControleCarga, findDefinition);
    	rsp.setList( new AliasToNestedBeanResultTransformer(LacreControleCarga.class).transformListResult(rsp.getList()) );
	    return rsp;
    }

    /**
     * 
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountByControleCarga(Long idControleCarga) {
    	return getLacreControleCargaDAO().getRowCountByControleCarga(idControleCarga);
    }

    /**
     * 
     * @param idLacreControleCarga
     * @return
     */
    public LacreControleCarga findByIdByControleCarga(Long idLacreControleCarga) {
    	Map map = getLacreControleCargaDAO().findByIdByControleCarga(idLacreControleCarga);
		List lista = new ArrayList();
		lista.add(map);
		lista = new AliasToNestedBeanResultTransformer(LacreControleCarga.class).transformListResult(lista);
	    return (LacreControleCarga)lista.get(0);
    }

    /**
     * 
     * @param idControleCarga
     * @param nrLacre
     * @param obInclusaoLacre
     * @param dsLocalInclusao
     * @return
     */
    public void storeByControleCarga(Long idControleCarga, String nrLacres, String obInclusaoLacre, String dsLocalInclusao) {
    	ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
    	LacreControleCarga bean = new LacreControleCarga();
    	bean.setControleCarga(controleCarga);
    	bean.setDsLocalInclusao(dsLocalInclusao);
    	bean.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
    	bean.setFilialByIdFilialInclusao(SessionUtils.getFilialSessao());
    	bean.setMeioTransporte(controleCarga.getMeioTransporteByIdTransportado());
    	bean.setNrLacres(nrLacres);
    	bean.setObInclusaoLacre(obInclusaoLacre);
    	bean.setTpStatusLacre(new DomainValue("FE"));
    	bean.setUsuarioByIdFuncInclusao(SessionUtils.getUsuarioLogado());
    	store(bean);
    }
    

    /**
     * 
     * @param idLacreControleCarga
     * @param tpStatusLacre
     * @param dsLocalConferencia
     * @param obConferenciaLacre
     */
    public void storeByConferenciaLacre(Long idLacreControleCarga, String tpStatusLacre, String dsLocalConferencia, String obConferenciaLacre) {
    	LacreControleCarga bean = findById(idLacreControleCarga);
    	bean.setDsLocalConferencia(dsLocalConferencia);
    	bean.setDhAlteracao(JTDateTimeUtils.getDataHoraAtual());
    	bean.setFilialByIdFilialAlteraStatus(SessionUtils.getFilialSessao());
    	bean.setObConferenciaLacre(obConferenciaLacre);
    	bean.setTpStatusLacre(new DomainValue(tpStatusLacre));
    	bean.setUsuarioByIdFuncAlteraStatus(SessionUtils.getUsuarioLogado());
    	store(bean);
    }
}