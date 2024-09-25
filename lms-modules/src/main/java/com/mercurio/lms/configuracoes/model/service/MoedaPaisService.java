package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.configuracoes.model.dao.MoedaDAO;
import com.mercurio.lms.configuracoes.model.dao.MoedaPaisDAO;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.util.session.SessionKey;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.configuracoes.moedaPaisService"
 */
public class MoedaPaisService extends CrudService<MoedaPais, Long> {
	private MoedaDAO moedaDAO;

	/**
	 * Recupera uma inst�ncia de <code>MoedaPais</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public MoedaPais findById(java.lang.Long id) {
        return (MoedaPais)super.findById(id);
    }
    
	/**
	 * Retorna a moeda a mais utilizada do pais informado como par�metro;
	 *
	 * @author Micka&el Jalbert
	 * @since 30/08/2006
	 *
	 * @param Long idPais
	 * @return MoedaPais moedaPais
	 */
    public MoedaPais findMoedaPaisMaisUtilizada(Long idPais) {
    	return getMoedaPaisDAO().findMoedaPaisMaisUtilizada(idPais, Boolean.TRUE);
    }     

	/**
	 * 
	 * @param criterias
	 * @return
	 */
	public List<MoedaPais> findMoedasByPaisLookup(Map<String, Object> criterias) {
		return getMoedaPaisDAO().findMoedasByPaisLookup(criterias); 
	}
    
	public List<MoedaPais> findMoedaByPaisUsuarioLogado(){
		Pais p = (Pais)SessionContext.get(SessionKey.PAIS_KEY);
		return findMoedaByPaisSituacao(p.getIdPais(), "A");
	}

	public Long findMoedaPaisByUsuarioLogado(){
		Moeda moedaUsuario = (Moeda) SessionContext.get(SessionKey.MOEDA_KEY);
		Pais pais = (Pais) SessionContext.get(SessionKey.PAIS_KEY);

		MoedaPais moedaPais = getMoedaPaisDAO().findByUniqueKey(moedaUsuario.getIdMoeda(), pais.getIdPais()); 

		Long idMoedaPais = null;

		if (moedaPais != null)
			idMoedaPais = moedaPais.getIdMoedaPais();

		return idMoedaPais;
	}

	public MoedaPais findByPaisAndMoeda(Long idPais, Long idMoeda) {
		return getMoedaPaisDAO().findByPaisAndMoeda(idPais,idMoeda);
	}

	public List<MoedaPais> findMoedaByPaisSituacao(Long idPais, String tpSituacao){
		return getMoedaPaisDAO().findMoedaByPaisSituacao(idPais, tpSituacao);
	}
	
	public List<MoedaPais> findMoedaByPais(Map<String, Object> criteria) {
		if(criteria != null) {
			Long idPais = MapUtils.getLong(MapUtils.getMap(MapUtils.getMap(criteria, "moedaPais"), "pais"), "idPais");
			if(idPais != null) {
				return getMoedaPaisDAO().findMoedaByPaisSituacao(idPais, "A");
			}
		}
		return new ArrayList<MoedaPais>();
	}

	public List findMoedaPaisCombo(String tpSituacao) {
		return getMoedaPaisDAO().findMoedaPaisBySituacao(tpSituacao);
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
    public void removeByIds(List<Long> ids) {
        super.removeByIds(ids);
    }
    
	/* (non-Javadoc)
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected MoedaPais beforeStore(MoedaPais bean) {
		//Apenas uma moeda pode ser a padr�o do pa�s
		if (Boolean.TRUE.equals(bean.getBlIndicadorPadrao())) {
			if (this.getMoedaPaisDAO().existMoedaPadrao(bean)) {
				throw new BusinessException("LMS-27002");
			}
		}

		//Apenas uma moeda pode ser a mais usada do pa�s
		if (Boolean.TRUE.equals( bean.getBlIndicadorMaisUtilizada())) {
			if (this.getMoedaPaisDAO().existMoedaMaisUtilizada(bean)) {
				throw new BusinessException("LMS-27005");
			}
		}

		return bean;
	}

	public List findMoedaPaisBySituacaoPais(String tpSituacao,Long idPais) {
		return getMoedaPaisDAO().findMoedaPaisBySituacaoPais(tpSituacao,idPais);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(MoedaPais bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMoedaPaisDAO(MoedaPaisDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private MoedaPaisDAO getMoedaPaisDAO() {
        return (MoedaPaisDAO) getDao();
    }

	public MoedaDAO getMoedaDAO() {
		return moedaDAO;
	}

	public void setMoedaDAO(MoedaDAO moedaDAO) {
		this.moedaDAO = moedaDAO;
	}
     

    
    /**
     * M�todo que retorna as moedas do pais informado, ativo.
     * 
     * @param Long idPais
     * @param Boolean blAtivo
     * @return List
     * */
    public List<MoedaPais> findByPais(Long idPais, Boolean blAtivo){
    	String strAtivo = null;
    	if (blAtivo.equals(Boolean.TRUE)){
    		strAtivo = "A";
    	}
    	return this.getMoedaPaisDAO().findByPais(idPais, strAtivo);
    }

    /**
     * 
     * @return
     */
    public MoedaPais findMoedaPaisUsuarioLogado() {
		Map<String, Object> mapMoeda = new HashMap<String, Object>();
		mapMoeda.put("idMoeda", SessionUtils.getMoedaSessao().getIdMoeda());

		Map<String, Object> mapPais = new HashMap<String, Object>();
		mapPais.put("idPais", SessionUtils.getPaisSessao().getIdPais());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("moeda", mapMoeda);
		map.put("pais", mapPais);
		map.put("tpSituacao", "A");

		List<MoedaPais> list = find(map);
		return list.get(0);
    }
}