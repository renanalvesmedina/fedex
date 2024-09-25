package com.mercurio.lms.carregamento.model.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.FilialRotaCc;
import com.mercurio.lms.carregamento.model.PostoPassagemCc;
import com.mercurio.lms.carregamento.model.dao.PostoPassagemCcDAO;
import com.mercurio.lms.configuracoes.model.MoedaPais;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.PostoPassagem;
import com.mercurio.lms.municipios.model.Rodovia;
import com.mercurio.lms.municipios.model.TipoPagamPostoPassagem;
import com.mercurio.lms.municipios.model.TipoPagamentoPosto;
import com.mercurio.lms.municipios.model.service.PostoPassagemService;
import com.mercurio.lms.municipios.model.service.PostoPassagemTrechoService;
import com.mercurio.lms.municipios.model.service.TipoPagamPostoPassagemService;
import com.mercurio.lms.municipios.model.service.TipoPagamentoPostoService;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.postoPassagemCcService"
 */
public class PostoPassagemCcService extends CrudService<PostoPassagemCc, Long> {

	private FilialRotaCcService filialRotaCcService;
	private MeioTransporteService meioTransporteService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private PagtoPedagioCcService pagtoPedagioCcService;
	private PostoPassagemTrechoService postoPassagemTrechoService;
	private PostoPassagemService postoPassagemService;
	private TipoPagamentoPostoService tipoPagamentoPostoService;
	private TipoPagamPostoPassagemService tipoPagamPostoPassagemService;
	private ReciboPostoPassagemService reciboPostoPassagemService;
	
	public void setFilialRotaCcService(FilialRotaCcService filialRotaCcService) {
		this.filialRotaCcService = filialRotaCcService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public void setTipoPagamentoPostoService(TipoPagamentoPostoService tipoPagamentoPostoService) {
		this.tipoPagamentoPostoService = tipoPagamentoPostoService;
	}
	public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	public void setPostoPassagemTrechoService(PostoPassagemTrechoService postoPassagemTrechoService) {
		this.postoPassagemTrechoService = postoPassagemTrechoService;
	}
	public void setPagtoPedagioCcService(PagtoPedagioCcService pagtoPedagioCcService) {
		this.pagtoPedagioCcService = pagtoPedagioCcService;
	}
	public void setTipoPagamPostoPassagemService(TipoPagamPostoPassagemService tipoPagamPostoPassagemService) {
		this.tipoPagamPostoPassagemService = tipoPagamPostoPassagemService;
	}
	public void setPostoPassagemService(PostoPassagemService postoPassagemService) {
		this.postoPassagemService = postoPassagemService;
	}
	public ReciboPostoPassagemService getReciboPostoPassagemService() {
		return reciboPostoPassagemService;
	}
	public void setReciboPostoPassagemService(
			ReciboPostoPassagemService reciboPostoPassagemService) {
		this.reciboPostoPassagemService = reciboPostoPassagemService;
	}
	
	/**
	 * Recupera uma instância de <code>PostoPassagemCc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public PostoPassagemCc findById(java.lang.Long id) {
        return (PostoPassagemCc)super.findById(id);
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
    public java.io.Serializable store(PostoPassagemCc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setPostoPassagemCcDAO(PostoPassagemCcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private PostoPassagemCcDAO getPostoPassagemCcDAO() {
        return (PostoPassagemCcDAO) getDao();
    }

    /**
     * Retorna uma lista de Map, contendo os dados dos PostoPassagemCc cadastrados para o controle de carga recebido
     * por parâmetro.
     * 
     * @param idControleCarga
     * @return
     */
    public List findPostoPassagemByPagtoPedagioByControleCarga (Long idControleCarga) {
    	List result = getPostoPassagemCcDAO().findPostoPassagemByPagtoPedagioByControleCarga(idControleCarga);
    	return result;
    }

    /**
     * 
     * @param idPostoPassagem
     * @return
     */
    public Map findFormasPagamentoPostoPassagemCc(Long idPostoPassagem, String tpControleCarga) {
        List listaValor = new ArrayList();
        List listaDescricao = new ArrayList();

        List resultado = tipoPagamPostoPassagemService.findFormasPagamentoPostoPassagemCc(idPostoPassagem, tpControleCarga);
        for (Iterator iter = resultado.iterator(); iter.hasNext();) { 
        	TipoPagamPostoPassagem tipoPagamPostoPassagem = (TipoPagamPostoPassagem) iter.next();
			listaValor.add(tipoPagamPostoPassagem.getIdTipoPagamPostoPassagem());
			listaDescricao.add(tipoPagamPostoPassagem.getDsTipoPagamPostoPassagem().toString());
    	}
        Map mapRetorno = new HashMap();
        mapRetorno.put("listaValor", listaValor);
        mapRetorno.put("listaDescricao", listaDescricao);
        return mapRetorno;
    }

    /**
     * Busca os registros de PostoPassagemCc cadastrados para o controle de carga passado por parâmetro.
     * 
     * @param idControleCarga
     * @return
     */
    private List verificarPostoPassagemCcCadastrados(Long idControleCarga) {
    	if (idControleCarga == null)
    		return new ArrayList();

    	Map mapControleCarga = new HashMap();
    	mapControleCarga.put("idControleCarga", idControleCarga);

    	Map criteria = new HashMap();
    	criteria.put("controleCarga", mapControleCarga);

    	return find(criteria);
    }

    /**
     * 
     * @param beans
     */
    public void storePostoPassagemCcAll(List beans) {
    	getPostoPassagemCcDAO().storePostoPassagemCc(beans);
    }

    /**
     * 
     * @param removeItems
     */
    public void removePostoPassagemCc(List removeItems) {
    	getPostoPassagemCcDAO().removePostoPassagemCc(removeItems);
	}

    
	/**
	 * 
	 * @param items
	 */
    public void storePostoPassagemCc(ItemList items) {
    	getPostoPassagemCcDAO().storePostoPassagemCc(items);
    }

    /**
     * Remove do banco todos os registros de PostoPassagemCc de um controle de carga.
     * 
     * @param idControleCarga
     */
    public void removeByIdControleCarga(Long idControleCarga) {
    	getPostoPassagemCcDAO().removeByIdControleCarga(idControleCarga);
    }

    /**
     * Retorna uma lista de PostoPassagemCc cadastrados no banco para o controle de carga recebido por parâmetro. 
     * 
     * @param idControleCarga
     * @return
     */
    public List findPostoPassagemCcByIdControleCarga(Long idControleCarga) {
    	List result = getPostoPassagemCcDAO().findPostoPassagemCcByIdControleCarga(idControleCarga);
    	result = new AliasToNestedBeanResultTransformer(PostoPassagemCc.class).transformListResult(result);
    	return result;
    }

    /**
     * Retorna uma lista de PostoPassagemCc para o Controle de Carga Coleta/Entrega.
     * 
     * Realiza uma pesquisa no banco pelo idControleCarga. Se encontrou dados, retorna uma lista (PostoPassagemCc) com esses dados.
     * Caso contrário, busca uma lista de dados de PostoPassagem, utilizando o idRotaColetaEntrega como filtro da pesquisa. 
     * Com estes dados, gera (df2, não no banco) uma lista de PostoPassagemCc.
     * 
     * @param idControleCarga
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     * @param idRotaColetaEntrega
     * @param blRetornaSomenteCadastrados
     * @return
     */
    public List findPostoPassagemCcByColetaEntrega(	Long idControleCarga, 
    												Long idMeioTransporteTransportado,
    												Long idMeioTransporteSemiRebocado, 
    												Long idRotaColetaEntrega,
    												Boolean blRetornaSomenteCadastrados)
    {
    	List result = verificarPostoPassagemCcCadastrados(idControleCarga);
    	if (!result.isEmpty())
    		return result;
    	
    	if (blRetornaSomenteCadastrados != null && blRetornaSomenteCadastrados)
    		return Collections.EMPTY_LIST;

		List postos = postoPassagemService.findPostoPassagemByControleCarga(idRotaColetaEntrega);
    	result = criarListaPostoPassagemCcColetaEntrega(idControleCarga, idMeioTransporteTransportado, idMeioTransporteSemiRebocado, postos);
    	return result;
    }


    /**
     * Retorna uma lista de PostoPassagemCc para o Controle de Carga Viagem.
     *
     * Realiza uma pesquisa no banco pelo idControleCarga. Se encontrou dados, retorna uma lista (PostoPassagemCc) com esses dados.
     * Caso contrário, busca uma lista de dados de PostoPassagem, utilizando a listaFiliais recebida por parâmetro. 
     * Com estes dados, gera (df2, não no banco) uma lista de PostoPassagemCc.
     * 
     * @param idControleCarga
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     * @param listaFiliais
     * @param blVerificaCadastrados
     * @param blRetornaCadastrados
     * @return
     */
    public List findPostoPassagemCcByViagem(Long idControleCarga, 
    										Long idMeioTransporteTransportado, 
    										Long idMeioTransporteSemiRebocado, 
    										List listaFiliais,
    										Boolean blVerificaCadastrados,
    										Boolean blRetornaSomenteCadastrados)
    {
    	if (blVerificaCadastrados != null && blVerificaCadastrados) {
	    	List result = verificarPostoPassagemCcCadastrados(idControleCarga);
	    	if (!result.isEmpty())
	    		return result;
	
	    	if (blRetornaSomenteCadastrados != null && blRetornaSomenteCadastrados)
	    		return Collections.EMPTY_LIST;
    	}

    	Map mapTransportado = meioTransporteRodoviarioService.findDadosTipoMeioTransporte(idMeioTransporteTransportado);
    	Map mapSemiRebocado = null;
    	if (idMeioTransporteSemiRebocado != null) {
    		mapSemiRebocado = meioTransporteRodoviarioService.findDadosTipoMeioTransporte(idMeioTransporteSemiRebocado);
    	}
    	
    	Long idTipoMeioTransp = mapTransportado == null ? null : (Long)mapTransportado.get("idTipoMeioTransporte");
    	Integer qtEixosMeioTransp = mapTransportado == null ? null : (Integer)mapTransportado.get("qtEixos");
    	Boolean blControleTag = mapTransportado == null ? null : (Boolean)mapTransportado.get("blControleTag");
    	Long idTipoMeioTranspComposto = mapSemiRebocado == null ? null : (Long)mapSemiRebocado.get("idTipoMeioTransporte"); 
		Integer qtEixosMeioTranpComposto = mapSemiRebocado == null ? null : (Integer)mapSemiRebocado.get("qtEixos");

		validaEixoTipoMeioTransporte(idMeioTransporteTransportado, idTipoMeioTransp, qtEixosMeioTransp);
		
    	List postos = postoPassagemTrechoService.findPostosPassagemByFiliaisRota(listaFiliais, 
    			idTipoMeioTransp, qtEixosMeioTransp, idTipoMeioTranspComposto, qtEixosMeioTranpComposto, null, null, !blControleTag);

    	return criarListaPostoPassagemCcByViagem(idControleCarga, postos);
    }

    
    /**
     * Cria uma lista de registros de PostoPassagemCc (df2, não no banco) de acordo com os dados recebidos por parâmetro.
     * Esse método é utilizado pelo Controle de Carga de Viagem.
     * 
     * @param idControleCarga
     * @param postos
     * @return
     */
	private List criarListaPostoPassagemCcByViagem(Long idControleCarga, List postos) {
    	ControleCarga controleCarga = null;
    	if (idControleCarga != null) {
	    	controleCarga = new ControleCarga();
	    	controleCarga.setIdControleCarga(idControleCarga);
    	}

    	Map mapVerificaPostoPassagem = new HashMap();
    	List result = new ArrayList();
    	for (Iterator iter = postos.iterator(); iter.hasNext();) {
			TypedFlatMap mapPosto = (TypedFlatMap) iter.next();

			Long idTipoPagamentoPosto = mapPosto.getLong("idTipoPagamentoPosto");
			if (idTipoPagamentoPosto == null)
				continue;

			Long idPostoPassagem = mapPosto.getLong("idPostoPassagem");
			if (!mapVerificaPostoPassagem.containsKey(idPostoPassagem.toString()))
				mapVerificaPostoPassagem.put(idPostoPassagem.toString(), idPostoPassagem.toString());
			else
				continue;

			TipoPagamentoPosto tipoPagamentoPosto = tipoPagamentoPostoService.findById(idTipoPagamentoPosto);
			PostoPassagem postoPassagem = postoPassagemService.findById(idPostoPassagem);

			PostoPassagemCc postoPassagemCc = new PostoPassagemCc();
			postoPassagemCc.setIdPostoPassagemCc(null);
			postoPassagemCc.setControleCarga(controleCarga);
			postoPassagemCc.setPostoPassagem(postoPassagem);
			postoPassagemCc.setTipoPagamPostoPassagem(tipoPagamentoPosto.getTipoPagamPostoPassagem());
			postoPassagemCc.setVlPagar(mapPosto.getBigDecimal("vlPostoPassagem"));

	    	MoedaPais moedaPais = (MoedaPais)mapPosto.get("moedaPais");
	    	if (moedaPais == null) 
	    		postoPassagemCc.setMoeda(SessionUtils.getMoedaSessao());
	    	else
	    		postoPassagemCc.setMoeda(moedaPais.getMoeda());

			result.add(postoPassagemCc);
		}
    	return result;
	}

    
    /**
     * Cria uma lista de registros de PostoPassagemCc (df2, não no banco) de acordo com os dados recebidos por parâmetro.
     * Esse método é utilizado pelo Controle de Carga Coleta/Entrega.
     * 
     * @param idControleCarga
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     * @param postos
     * @return
     */
	private List criarListaPostoPassagemCcColetaEntrega(Long idControleCarga, 
														Long idMeioTransporteTransportado, 
														Long idMeioTransporteSemiRebocado, 
														List postos) 
	{
    	Map mapTransportado = meioTransporteRodoviarioService.findDadosTipoMeioTransporte(idMeioTransporteTransportado);
    	Map mapSemiRebocado = null;
    	if (idMeioTransporteSemiRebocado != null) {
    		mapSemiRebocado = meioTransporteRodoviarioService.findDadosTipoMeioTransporte(idMeioTransporteSemiRebocado);
    	}
    	
    	Long idTipoMeioTransp = mapTransportado == null ? null : (Long)mapTransportado.get("idTipoMeioTransporte");
    	Integer qtEixosMeioTransp = mapTransportado == null ? null : (Integer)mapTransportado.get("qtEixos");
 
		validaEixoTipoMeioTransporte(idMeioTransporteTransportado, idTipoMeioTransp, qtEixosMeioTransp);
    	
		Integer qtEixosMeioTranpComposto = mapSemiRebocado == null ? null : (Integer)mapSemiRebocado.get("qtEixos");

    	ControleCarga controleCarga = null;
    	if (idControleCarga != null) {
	    	controleCarga = new ControleCarga();
	    	controleCarga.setIdControleCarga(idControleCarga);
    	}

    	Map mapVerificaPostoPassagem = new HashMap();
    	List result = new ArrayList();
    	for (Iterator iter = postos.iterator(); iter.hasNext();) {
			Map mapPosto = (Map) iter.next();
			
			Long idPostoPassagem = (Long)mapPosto.get("idPostoPassagem");
			if (!mapVerificaPostoPassagem.containsKey(idPostoPassagem.toString()))
				mapVerificaPostoPassagem.put(idPostoPassagem.toString(), idPostoPassagem.toString());
			else
				continue;

			PostoPassagemCc postoPassagemCc = new PostoPassagemCc();
			Municipio municipio = new Municipio();
			municipio.setNmMunicipio((String)mapPosto.get("nmMunicipio"));

			Rodovia rodovia = new Rodovia();
			rodovia.setSgRodovia((String)mapPosto.get("sgRodovia"));

			TipoPagamPostoPassagem tipoPagamPostoPassagem = new TipoPagamPostoPassagem();
			tipoPagamPostoPassagem.setIdTipoPagamPostoPassagem((Long)mapPosto.get("idTipoPagamPostoPassagem"));
			tipoPagamPostoPassagem.setDsTipoPagamPostoPassagem((VarcharI18n)mapPosto.get("dsTipoPagamPostoPassagem"));
			tipoPagamPostoPassagem.setBlCartaoPedagio(mapPosto.get("blCartaoPedagio") == null ? null : (Boolean)mapPosto.get("blCartaoPedagio"));

			PostoPassagem postoPassagem = new PostoPassagem();
			postoPassagem.setIdPostoPassagem(idPostoPassagem);
			postoPassagem.setNrKm((Integer)mapPosto.get("nrKm"));
			postoPassagem.setTpPostoPassagem((DomainValue)mapPosto.get("tpPostoPassagem"));
			postoPassagem.setMunicipio(municipio);
			postoPassagem.setRodovia(rodovia);

			postoPassagemCc.setIdPostoPassagemCc(null);
			postoPassagemCc.setControleCarga(controleCarga);
			postoPassagemCc.setPostoPassagem(postoPassagem);
			postoPassagemCc.setTipoPagamPostoPassagem(tipoPagamPostoPassagem);
			
			Integer qtEixos = null;
			if (qtEixosMeioTranpComposto != null)
				qtEixos = qtEixosMeioTransp.intValue() + qtEixosMeioTranpComposto.intValue();
			else
				qtEixos = qtEixosMeioTransp;
 
			TypedFlatMap mapPostoPassgem = postoPassagemService.findVlByTpMeioTransporte(
					idPostoPassagem, idTipoMeioTransp, qtEixos, null, null,null);
			
	    	MoedaPais moedaPais = (MoedaPais)mapPostoPassgem.get("moedaPais");
	    	if (moedaPais == null) 
	    		postoPassagemCc.setMoeda(SessionUtils.getMoedaSessao());
	    	else
	    		postoPassagemCc.setMoeda(moedaPais.getMoeda());
	    	postoPassagemCc.setVlPagar(mapPostoPassgem.getBigDecimal("vlPostoPassagem"));

			result.add(postoPassagemCc);
		}
    	return result;
	}

	
	/**
	 * 
	 * @param idMeioTransporteTransportado
	 * @param idTipoMeioTransp
	 * @param qtEixosMeioTransp
	 */
	private void validaEixoTipoMeioTransporte(Long idMeioTransporteTransportado, Long idTipoMeioTransp, Integer qtEixosMeioTransp) {
		if (qtEixosMeioTransp == null) {
			MeioTransporte meioTransporte = meioTransporteService.findByIdInitLazyProperties(idMeioTransporteTransportado, false); 
			String dsFrotaIdentificador = meioTransporte.getNrFrota() + " - " + meioTransporte.getNrIdentificador();
			throw new BusinessException("LMS-05098", new Object[] {dsFrotaIdentificador});
		}
		if (idTipoMeioTransp == null) {
			MeioTransporte meioTransporte = meioTransporteService.findByIdInitLazyProperties(idMeioTransporteTransportado, false); 
			String dsFrotaIdentificador = meioTransporte.getNrFrota() + " - " + meioTransporte.getNrIdentificador();
			throw new BusinessException("LMS-05112", new Object[] {dsFrotaIdentificador});
		}
	}


    /**
     * Gera e salva no banco registros de PostoPassagemCc e PagtoPedagioCc de acordo com os dados recebidos por parâmetro.
     * 
     * @param idControleCarga
     * @param tpControleCarga
     * @param idRotaColetaEntrega
     * @param idMeioTransporteTransportado
     * @param idMeioTransporteSemiRebocado
     */
    public void generatePostosPassagemCcAndPagtoPedagioCc(	Long idControleCarga, 
    														String tpControleCarga, 
    														Long idRotaColetaEntrega, 
    														Long idMeioTransporteTransportado, 
    														Long idMeioTransporteSemiRebocado)
    {
    	removeByIdControleCarga(idControleCarga);
    	List postos = Collections.EMPTY_LIST;

		if (tpControleCarga.equals("C")) {
			postos = findPostoPassagemCcByColetaEntrega(idControleCarga, 
					idMeioTransporteTransportado, idMeioTransporteSemiRebocado, idRotaColetaEntrega, null);
		}
		else
		if (tpControleCarga.equals("V")) { 
			List listaFiliaisRota = new ArrayList();
			List listaFilialRotaCc = filialRotaCcService.findFilialRotaCcWithNrOrdem(idControleCarga, null, null, null);
	    	for (Iterator iter = listaFilialRotaCc.iterator(); iter.hasNext();) {
	    		FilialRotaCc filialRotaCc = (FilialRotaCc)iter.next();
	    		listaFiliaisRota.add(filialRotaCc.getFilial());
	    	}
			
			postos = findPostoPassagemCcByViagem(idControleCarga, 
					idMeioTransporteTransportado, idMeioTransporteSemiRebocado, listaFiliaisRota, Boolean.TRUE, null);
		}

    	for (Iterator iter = postos.iterator(); iter.hasNext();) {
    		PostoPassagemCc postoPassagemCc = (PostoPassagemCc) iter.next();
    		postoPassagemCc.setIdPostoPassagemCc(null);
    		store(postoPassagemCc);
    	}

    	reciboPostoPassagemService.removeByIdControleCarga(idControleCarga);
		pagtoPedagioCcService.removeByIdControleCarga(idControleCarga);
		pagtoPedagioCcService.generatePagtoPedagioCcByIdControleCarga(idControleCarga);
    }
}