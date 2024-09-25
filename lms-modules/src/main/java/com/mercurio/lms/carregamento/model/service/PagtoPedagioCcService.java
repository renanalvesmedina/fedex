package com.mercurio.lms.carregamento.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.OperadoraCartaoPedagio;
import com.mercurio.lms.carregamento.model.PagtoPedagioCc;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;
import com.mercurio.lms.carregamento.model.dao.PagtoPedagioCcDAO;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.service.TipoPagamPostoPassagemService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.BigDecimalUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.pagtoPedagioCcService"
 */
public class PagtoPedagioCcService extends CrudService<PagtoPedagioCc, Long> {

	private OperadoraCartaoPedagioService operadoraCartaoPedagioService;
	private PostoPassagemCcService postoPassagemCcService;
	private TipoPagamPostoPassagemService tipoPagamPostoPassagemService;
	

	public void setTipoPagamPostoPassagemService(TipoPagamPostoPassagemService tipoPagamPostoPassagemService) {
		this.tipoPagamPostoPassagemService = tipoPagamPostoPassagemService;
	}
	public void setOperadoraCartaoPedagioService(OperadoraCartaoPedagioService operadoraCartaoPedagioService) {
		this.operadoraCartaoPedagioService = operadoraCartaoPedagioService;
	}
	public void setPostoPassagemCcService(PostoPassagemCcService postoPassagemCcService) {
		this.postoPassagemCcService = postoPassagemCcService;
	}

	/**
	 * Recupera uma instância de <code>PagtoPedagioCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public PagtoPedagioCc findById(java.lang.Long id) {
        return (PagtoPedagioCc)super.findById(id);
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
    public java.io.Serializable store(PagtoPedagioCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPagtoPedagioCcDAO(PagtoPedagioCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PagtoPedagioCcDAO getPagtoPedagioCcDAO() {
        return (PagtoPedagioCcDAO) getDao();
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public Map findOperadoraCartaoPedagio() {
    	Map criteria = new HashMap();
    	criteria.put("tpSituacao", "A");
    	
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("pessoa_.nmPessoa:asc");
        
        List listaValor = new ArrayList();
        List listaDescricao = new ArrayList();
        
        List resultado = operadoraCartaoPedagioService.findListByCriteria(criteria, campoOrdenacao);
        
        for (Iterator iter = resultado.iterator(); iter.hasNext();) { 
			OperadoraCartaoPedagio operadoraCartaoPedagio = (OperadoraCartaoPedagio) iter.next();
			listaValor.add(operadoraCartaoPedagio.getIdOperadoraCartaoPedagio());
			listaDescricao.add(operadoraCartaoPedagio.getPessoa().getNmPessoa());
    	}
        Map mapRetorno = new HashMap();
        mapRetorno.put("listaValor", listaValor);
        mapRetorno.put("listaDescricao", listaDescricao);
        return mapRetorno;
    }

    /**
     * Remove do banco todos os registros de PagtoPedagioCc de um controle de carga.
     * 
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	getPagtoPedagioCcDAO().removeByIdControleCarga(idControleCarga);
    }
    
    /**
     * 
     * @param idControleCarga
     * @return
     */
    public BigDecimal getVlPedagioByIdControleCarga(Long idControleCarga) {
		Map mapControleCarga = new HashMap();
		mapControleCarga.put("idControleCarga", idControleCarga);
		
		Map map = new HashMap();
		map.put("controleCarga", mapControleCarga);

		BigDecimal vlPedagio = BigDecimalUtils.ZERO;

		List lista = find(map);		
		for (Iterator iter = lista.iterator(); iter.hasNext();) {
			PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc) iter.next();
			vlPedagio = vlPedagio.add(pagtoPedagioCc.getVlPedagio());
		}
		return vlPedagio;
    }
    
    /**
     * Retorna uma lista de PagtoPedagioCc cadastrados no banco para o controle de carga recebido por parâmetro.
     * 
     * @param idControleCarga
     * @return
     */
    public List findPagtoPedagioCcByIdControleCarga(Long idControleCarga) {
    	List result = getPagtoPedagioCcDAO().findPagtoPedagioCcByIdControleCarga(idControleCarga);
    	result = new AliasToNestedBeanResultTransformer(PagtoPedagioCc.class).transformListResult(result);
    	return result;
    }
    
    /**
     * Busca os PagtoPedagioCc de um controle de carga e que tenha idTipoPagamPostoPassagem igual ao passado por parâmetro.
     * @param idControleCarga
     * @param idTipoPagamPostoPassagem
     * @return
     */
    public List findPagtoPedagioCcByIdCcAndIdPagamPostoPassagem(Long idControleCarga, Long idTipoPagamPostoPassagem){
    	return getPagtoPedagioCcDAO().findPagtoPedagioCcByIdCcAndIdPagamPostoPassagem(idControleCarga, idTipoPagamPostoPassagem);
    }
 
    /**
     * 
     * @param beans
     */
    public void storePagtoPedagioCcAll(List beans) {
    	getPagtoPedagioCcDAO().storePagtoPedagioCc(beans);
    }

    /**
     * @param items
     */
    public void storePagtoPedagioCc(ItemList items) {
    	getPagtoPedagioCcDAO().storePagtoPedagioCc(items);
	}


    /**
     * Gera e salva no banco registros de PagtoPedagioCc de acordo com os dados de postoPassagemCc cadastrados
     * no banco para o controle de carga recebido por parâmetro.
     * 
     * @param idControleCarga
     */
    public List generatePagtoPedagioCcByIdControleCarga(Long idControleCarga) {
    	List result = new ArrayList();
    	Map mapVerificaPostoPassagem = new HashMap();
    	List listaPostos = postoPassagemCcService.findPostoPassagemCcByIdControleCarga(idControleCarga);
    	for (Iterator iter = listaPostos.iterator(); iter.hasNext();) {
    		PostoPassagemCc postoPassagemCc = (PostoPassagemCc)iter.next();
			String key = postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem().toString() + postoPassagemCc.getMoeda().getIdMoeda().toString();
			if (!mapVerificaPostoPassagem.containsKey(key)) {
				PagtoPedagioCc pagtoPedagioCc = new PagtoPedagioCc();
				pagtoPedagioCc.setIdPagtoPedagioCc(null);
				pagtoPedagioCc.setControleCarga(postoPassagemCc.getControleCarga());
				pagtoPedagioCc.setCartaoPedagio(null);
				pagtoPedagioCc.setMoeda(postoPassagemCc.getMoeda());
				pagtoPedagioCc.setOperadoraCartaoPedagio(null);
				pagtoPedagioCc.setTipoPagamPostoPassagem(postoPassagemCc.getTipoPagamPostoPassagem());
				pagtoPedagioCc.setVersao(null);
				pagtoPedagioCc.setVlPedagio(postoPassagemCc.getVlPagar());
				mapVerificaPostoPassagem.put(key, pagtoPedagioCc);
				result.add(pagtoPedagioCc);
			}
			else {
				PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc)mapVerificaPostoPassagem.get(key);
				pagtoPedagioCc.setVlPedagio( pagtoPedagioCc.getVlPedagio().add(postoPassagemCc.getVlPagar()) );
			}
    	}
    	storePagtoPedagioCcAll(result);
    	return result;
	}

    
    /**
     * Busca os registros de PagtoPedagioCc cadastrados para o controle de carga recebido por parâmetro.
     * 
     * @param idControleCarga
     * @return
     */
    private List verificarPagtoPedagioCcCadastrados(Long idControleCarga) {
    	if (idControleCarga == null)
    		return Collections.EMPTY_LIST;

    	Map mapControleCarga = new HashMap();
    	mapControleCarga.put("idControleCarga", idControleCarga);

    	Map criteria = new HashMap();
    	criteria.put("controleCarga", mapControleCarga);

    	return find(criteria);
    }


    /**
     * Cria uma lista de registros de PagtoPedagioCc (df2, não no banco) de acordo com os dados recebidos por parâmetro.
     * 
     * @param controleCarga
     * @param lista
     * @return
     */
    private List criarListaPagtoPedagioCc(ControleCarga controleCarga, List lista) {
    	List result = new ArrayList();
    	for (Iterator iter = lista.iterator(); iter.hasNext();) {
			Map mapPostoPassagem = (Map) iter.next();
			Moeda moeda = new Moeda();
			moeda.setIdMoeda((Long)mapPostoPassagem.get("idMoeda"));
			moeda.setSgMoeda((String)mapPostoPassagem.get("sgMoeda"));
			moeda.setDsSimbolo((String)mapPostoPassagem.get("dsSimbolo"));

			TipoPagamPostoPassagem tipoPagamPostoPassagem = new TipoPagamPostoPassagem();
			tipoPagamPostoPassagem.setIdTipoPagamPostoPassagem((Long)mapPostoPassagem.get("idTipoPagamPostoPassagem"));
			tipoPagamPostoPassagem.setDsTipoPagamPostoPassagem( new VarcharI18n((String)mapPostoPassagem.get("dsTipoPagamPostoPassagem")) );
			tipoPagamPostoPassagem.setBlCartaoPedagio((Boolean)mapPostoPassagem.get("blCartaoPedagio"));

			PagtoPedagioCc pagtoPedagioCc = new PagtoPedagioCc();
			pagtoPedagioCc.setIdPagtoPedagioCc(null);
			pagtoPedagioCc.setControleCarga(controleCarga);
			pagtoPedagioCc.setCartaoPedagio(null);
			pagtoPedagioCc.setMoeda(moeda);
			pagtoPedagioCc.setOperadoraCartaoPedagio(null);
			pagtoPedagioCc.setTipoPagamPostoPassagem(tipoPagamPostoPassagem);
			pagtoPedagioCc.setVersao(null);
			pagtoPedagioCc.setVlPedagio((BigDecimal)mapPostoPassagem.get("vlSomatorio"));

			result.add(pagtoPedagioCc);
		}
    	return result;
	}


    /**
     * Cria uma lista de registros de PagtoPedagioCc (df2, não no banco) de acordo com os dados recebidos por parâmetro.
     * 
     * @param controleCarga
     * @param iter
     * @return
     */
    private List criarListaPagtoPedagioCc(ControleCarga controleCarga, Iterator iter) {
    	List result = new ArrayList();
    	Map mapVerificaPostoPassagem = new HashMap();
    	while (iter.hasNext()) {
    		PostoPassagemCc postoPassagemCc = (PostoPassagemCc)iter.next();
			String key = postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem().toString() + postoPassagemCc.getMoeda().getIdMoeda().toString();
			if (!mapVerificaPostoPassagem.containsKey(key)) {
				PagtoPedagioCc pagtoPedagioCc = new PagtoPedagioCc();
				pagtoPedagioCc.setIdPagtoPedagioCc(null);
				pagtoPedagioCc.setControleCarga(controleCarga);
				pagtoPedagioCc.setCartaoPedagio(null);
				pagtoPedagioCc.setMoeda(postoPassagemCc.getMoeda());
				pagtoPedagioCc.setOperadoraCartaoPedagio(null);
				pagtoPedagioCc.setTipoPagamPostoPassagem( tipoPagamPostoPassagemService.findById(postoPassagemCc.getTipoPagamPostoPassagem().getIdTipoPagamPostoPassagem()) );
				pagtoPedagioCc.setVersao(null);
				pagtoPedagioCc.setVlPedagio(postoPassagemCc.getVlPagar());
				mapVerificaPostoPassagem.put(key, pagtoPedagioCc);
				result.add(pagtoPedagioCc);
			}
			else {
				PagtoPedagioCc pagtoPedagioCc = (PagtoPedagioCc)mapVerificaPostoPassagem.get(key);
				pagtoPedagioCc.setVlPedagio( pagtoPedagioCc.getVlPedagio().add(postoPassagemCc.getVlPagar()) );
			}
    	}
    	return result;
	}

    
    /**
     * Retorna uma lista de PagtoPedagioCc.
     * Realiza uma pesquisa no banco pelo idControleCarga. Se encontrou dados, retorna uma lista de (PagtoPedagioCc) com esses dados.
     * Caso contrário, busca uma lista de dados (Map) de PostoPassagemCc cadastrados para o controle de carga recebido por parâmetro. 
     * Com estes dados, gera (df2, não no banco) uma lista de PagtoPedagioCc.
     * 
     * @param idControleCarga
     * @return
     */
    public List findPagtoPedagioCc(Long idControleCarga){
    	List result = verificarPagtoPedagioCcCadastrados(idControleCarga);
    	if (!result.isEmpty())
    		return result;

		ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(idControleCarga);
   		return criarListaPagtoPedagioCc(controleCarga, postoPassagemCcService.findPostoPassagemByPagtoPedagioByControleCarga(idControleCarga));
    }


    /**
     * Retorna uma lista de PagtoPedagioCc.
     * Realiza uma pesquisa no banco pelo idControleCarga. Se encontrou dados, retorna uma lista de (PagtoPedagioCc) com esses dados.
     * Caso contrário, gera (df2, não no banco) uma lista de PagtoPedagioCc.
     * 
     * @param idControleCarga
     * @param iterListaPostoPassagemCc
     * @param blVerificaCadastrados
     * @return
     */
    public List findPagtoPedagioCc(Long idControleCarga, Iterator iterListaPostoPassagemCc, Boolean blVerificaCadastrados){
    	if (blVerificaCadastrados) {
	    	List result = verificarPagtoPedagioCcCadastrados(idControleCarga);
	    	if (!result.isEmpty())
	    		return result;
    	}
    	ControleCarga controleCarga = new ControleCarga();
		controleCarga.setIdControleCarga(idControleCarga);

    	if (idControleCarga != null && (iterListaPostoPassagemCc == null || !iterListaPostoPassagemCc.hasNext())) 
    		return criarListaPagtoPedagioCc(controleCarga, postoPassagemCcService.findPostoPassagemByPagtoPedagioByControleCarga(idControleCarga));
    	else
    		return criarListaPagtoPedagioCc(controleCarga, iterListaPostoPassagemCc);
    }
    
    
    /**
     * Verifica se existe algum pagtoPedagioCc onde não tenha sido informado a operadora/número do cartão para blCartaoPedagio = 'S'. 
     * @param idControleCarga
     * @return True, se existe algum registro, caso contrário, False.
     */
    public Boolean validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(Long idControleCarga){
    	return getPagtoPedagioCcDAO().validateExisteCartaoPedagioNaoPreenchidoByIdControleCarga(idControleCarga);
    }
}