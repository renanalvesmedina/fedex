package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.carregamento.model.TipoDispositivoUnitizacao;
import com.mercurio.lms.carregamento.model.dao.TipoDispositivoUnitizacaoDAO;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.tipoDispositivoUnitizacaoService"
 */
public class TipoDispositivoUnitizacaoService extends CrudService<TipoDispositivoUnitizacao, Long> {

	private ParametroGeralService parametroGeralService;
	
	private static final String IDENTIFICACAO = "I";
	private static final String QUANTIDADE = "Q";
	
	/**
	 * Recupera uma instância de <code>TipoDispositivoUnitizacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public TipoDispositivoUnitizacao findById(java.lang.Long id) {
        return this.getTipoDispositivoUnitizacaoDAO().findById(id);
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
    public java.io.Serializable store(TipoDispositivoUnitizacao bean) {
    	Long idTipoDispositivoUnitizacao = bean.getIdTipoDispositivoUnitizacao();
    	if(	(bean.getTpControleDispositivo().getValue().equals(QUANTIDADE) && isIdentificado(idTipoDispositivoUnitizacao)) ||
    		(bean.getTpControleDispositivo().getValue().equals(IDENTIFICACAO) && !isIdentificado(idTipoDispositivoUnitizacao))) {        		
    		throw new BusinessException("LMS-03017");        		
    	}
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setTipoDispositivoUnitizacaoDAO(TipoDispositivoUnitizacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private TipoDispositivoUnitizacaoDAO getTipoDispositivoUnitizacaoDAO() {
        return (TipoDispositivoUnitizacaoDAO) getDao();
    }
    
    /**
     * Retorna uma coleção dos Tipos de Dispositivos com o tipo de controle igual a quantidade
     * @param map
     * @return
     */
    public List findTipoDispositivoByQuantidade(Map map) {
    	//Este if é necessario por causa da tela de pesquisa
    	if( map == null ) {
    		map = new HashMap();
    	}
        map.put("tpControleDispositivo",QUANTIDADE);
        // monta uma list com os itens para ordenação
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsTipoDispositivoUnitizacao:asc");
        
        return getDao().findListByCriteria(map, campoOrdenacao);        
    }

    /**
     * Retorna uma coleção dos Tipos de Dispositivos com o tipo de controle igual a identificação
     * @param map
     * @return
     */
    public List findTipoDispositivoByIdentificacao(Map map) {
    	//Este if é necessario por causa da tela de pesquisa
    	if( map == null ) {
    		map = new HashMap();
    	}
//       monta uma list com os itens para ordenação
    	map.put("tpControleDispositivo",IDENTIFICACAO);
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsTipoDispositivoUnitizacao:asc");
        
        return getDao().findListByCriteria(map, campoOrdenacao);        
    }
    
    /**
     * Retorna uma coleção dos Tipos de Dispositivos ordenados.
     * 
     * @param criteria
     * @return
     */
    public List findTipoDispositivoOrdenado(Map criteria) {
    	
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsTipoDispositivoUnitizacao:asc");
        
        return getDao().findListByCriteria(criteria, campoOrdenacao);   
    }
       
    public List<Map<String, Object>> findCombo() {    
        return this.getTipoDispositivoUnitizacaoDAO().findCombo();        
    }
      
    public Long getIdTipoDispositivoUnitizacaoBag() {
    	return this.getIdTipoDispositivoUnitizacao("ID_TP_DISP_UNIT_BAG");	
    }
    
    public Long getIdTipoDispositivoUnitizacaoCofre() {
    	return this.getIdTipoDispositivoUnitizacao("ID_TP_DISP_UNIT_COFRE");	
    }
    
    public Long getIdTipoDispositivoUnitizacaoGaiola() {
    	return this.getIdTipoDispositivoUnitizacao("ID_TP_DISP_UNIT_GAIOLA");	
    }
    
    public Long getIdTipoDispositivoUnitizacaoPallet() {
    	return this.getIdTipoDispositivoUnitizacao("ID_TP_DISP_UNIT_PALLET");	
    }
    
    public Long getNrTipoDispositivoUnitizacaoBag() {
    	return this.getIdTipoDispositivoUnitizacao("NR_ID_DISP_UNIT_BAG");	
    }
    
    public Long getNrTipoDispositivoUnitizacaoCofre() {
    	return this.getIdTipoDispositivoUnitizacao("NR_ID_DISP_UNIT_COFRE");	
    }
    
    public Long getNrTipoDispositivoUnitizacaoGaiola() {
    	return this.getIdTipoDispositivoUnitizacao("NR_ID_DISP_UNIT_GAIOLA");	
    }
    
    public Long getNrTipoDispositivoUnitizacaoPallet() {
    	return this.getIdTipoDispositivoUnitizacao("NR_ID_DISP_UNIT_PALLET");	
    }
    
    public Long getNrTipoDispositivoUnitizacaoById(Long idTipoDispositivoUnitizacao) {
    	if(idTipoDispositivoUnitizacao.equals(this.getIdTipoDispositivoUnitizacaoPallet())) {
			return this.getNrTipoDispositivoUnitizacaoPallet();
		} else if(idTipoDispositivoUnitizacao.equals(this.getIdTipoDispositivoUnitizacaoGaiola())) {
			return this.getNrTipoDispositivoUnitizacaoGaiola();
		} else if(idTipoDispositivoUnitizacao.equals(this.getIdTipoDispositivoUnitizacaoCofre())) {
			return this.getNrTipoDispositivoUnitizacaoCofre();
		} else if(idTipoDispositivoUnitizacao.equals(this.getIdTipoDispositivoUnitizacaoBag())) {
			return this.getNrTipoDispositivoUnitizacaoBag();
		} else {
			return null;
		}
    }
    
    private Long getIdTipoDispositivoUnitizacao(String nmParametroGeral) {
    	ParametroGeral param = parametroGeralService.findByNomeParametro(nmParametroGeral, Boolean.FALSE);
    	return Long.parseLong(param.getDsConteudo());
    }
    
    public Boolean isBag(Long idTipoDispositivoUnitizacao) {
    	return this.getIdTipoDispositivoUnitizacaoBag().equals(idTipoDispositivoUnitizacao);
    }
    
    public Boolean isCofre(Long idTipoDispositivoUnitizacao) {
    	return this.getIdTipoDispositivoUnitizacaoCofre().equals(idTipoDispositivoUnitizacao);
    }
    
    public Boolean isGaiola(Long idTipoDispositivoUnitizacao) {
    	return this.getIdTipoDispositivoUnitizacaoGaiola().equals(idTipoDispositivoUnitizacao);
    }
    
    public Boolean isPallet(Long idTipoDispositivoUnitizacao) {
    	return this.getIdTipoDispositivoUnitizacaoPallet().equals(idTipoDispositivoUnitizacao);
    }
    
    public Boolean isIdentificado(Long idTipoDispositivoUnitizacao) {
    	if(idTipoDispositivoUnitizacao == null) {
    		return Boolean.FALSE;
    	} else {
	    	return 	isBag(idTipoDispositivoUnitizacao) || isGaiola(idTipoDispositivoUnitizacao) || 
					isPallet(idTipoDispositivoUnitizacao) || isCofre(idTipoDispositivoUnitizacao);
    	}
    }
    
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
}