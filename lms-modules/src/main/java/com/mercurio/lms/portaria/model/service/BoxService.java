package com.mercurio.lms.portaria.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.pendencia.model.Modulo;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.BoxFinalidade;
import com.mercurio.lms.portaria.model.Doca;
import com.mercurio.lms.portaria.model.MeioTransporteRodoBox;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.dao.BoxDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;


/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.boxService"
 */
public class BoxService extends CrudService<Box, Long> {

	private DocaService docaService;
	private ConfiguracoesFacade configuracoesFacade;
	private VigenciaService vigenciaService;
	
	/**
	 * Recupera uma instância de <code>Box</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Box findById(java.lang.Long id) {
        return (Box)super.findById(id);
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
    	for (Iterator iterIds = ids.iterator(); iterIds.hasNext();) {
    		Long id = (Long) iterIds.next();
            this.removeById(id);
        }
    }
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Box bean) {
        return super.store(bean);
    }

    protected Box beforeStore(Box bean) {	   	
    	Box box = (Box) bean;
    	
    	if (vigenciaService.validateIntegridadeVigencia(MeioTransporteRodoBox.class,"box",box) ||
    		vigenciaService.validateIntegridadeVigencia(BoxFinalidade.class,"box",box))
    		throw new BusinessException("LMS-06023");
    	
    	if (getBoxDAO().verificaVigencia(box.getIdBox(), box.getNrBox(), box.getDoca().getIdDoca(), box.getDtVigenciaInicial(), box.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-00003");
    	
    	if (!docaService.findDocaValidaVigencias(box.getDoca().getIdDoca(), box.getDtVigenciaInicial(), box.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	return super.beforeStore(bean);
    }
    
	public TypedFlatMap storeMap(TypedFlatMap map) {

		Box box = map2bean(map);
		
		vigenciaService.validaVigenciaBeforeStore(box);
		
	    super.store(box);
	    
	    TypedFlatMap retorno = new TypedFlatMap();
	    
	    retorno.put("idBox", box.getIdBox());
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(box);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    return retorno;
	}
    
	private Box map2bean(TypedFlatMap map){
		Box box = new Box();
		box.setIdBox(map.getLong("idBox"));
		box.setDsBox(map.getString("dsBox"));
		box.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		box.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		box.setNrBox(map.getShort("nrBox"));
		box.setObBox(map.getString("obBox"));
		box.setTpSituacaoBox(map.getDomainValue("tpSituacaoBox"));
		
		if (map.getLong("modulo.idModulo") != null) {
			Modulo modulo = new Modulo();		
			modulo.setIdModulo(map.getLong("modulo.idModulo"));
			box.setModulo(modulo);
		}
		
		Doca doca = new Doca();
		doca.setIdDoca(map.getLong("doca.idDoca"));
		box.setDoca(doca);
		
		return box;
	}
	
    public Map findByIdDetalhamento(java.lang.Long id) {
    	Box box = (Box) super.findById(id);
        Doca doca = box.getDoca();
    	Terminal terminal = doca.getTerminal();
    	Filial filial = terminal.getFilial();
    	Pessoa pessoaFilial = filial.getPessoa();
    	Modulo modulo = box.getModulo();
    	
    	TypedFlatMap retorno = new TypedFlatMap();
	    
	    retorno.put("idBox", box.getIdBox());
	    
	    retorno.put("doca.terminal.filial.siglaNomeFilial",filial.getSgFilial() + " - " + pessoaFilial.getNmFantasia());
	    retorno.put("doca.terminal.filial.idFilial",filial.getIdFilial());
	    retorno.put("doca.terminal.filial.sgFilial",filial.getSgFilial());
	    retorno.put("doca.terminal.filial.pessoa.nmFantasia",pessoaFilial.getNmFantasia());
	    retorno.put("doca.terminal.dtVigenciaInicial",terminal.getDtVigenciaInicial());
        retorno.put("doca.terminal.dtVigenciaFinal",terminal.getDtVigenciaFinal());
	    retorno.put("doca.terminal.pessoa.nmPessoa",terminal.getPessoa().getNmPessoa());
	    retorno.put("doca.terminal.idTerminal",terminal.getIdTerminal());
	    retorno.put("doca.numeroDescricaoDoca",doca.getNumeroDescricaoDoca());
	    retorno.put("doca.idDoca",doca.getIdDoca());
	    
	    retorno.put("nrBox",box.getNrBox());
	    retorno.put("dsBox",box.getDsBox());
	    retorno.put("tpSituacaoBox.value",box.getTpSituacaoBox().getValue());
	    
	    if (modulo != null)
	    	retorno.put("modulo.idModulo",modulo.getIdModulo());
	    
	    retorno.put("obBox",box.getObBox());
	    retorno.put("dtVigenciaInicial",box.getDtVigenciaInicial());
	    retorno.put("dtVigenciaFinal",box.getDtVigenciaFinal());
	    
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(box);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    return retorno;
    }
    
	protected void beforeRemoveById(Long id) {
		Box box = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(box);
		super.beforeRemoveById(id);
	}

    protected void beforeRemoveByIds(List ids) {
    	Box box = null;
    	for(int x = 0; x < ids.size(); x++) {
    		box = findById((Long)ids.get(x));
    		JTVigenciaUtils.validaVigenciaRemocao(box);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    public List findCombo(Map criteria){
       	List rs = getBoxDAO().findCombo(criteria);
    	List lResul = new ArrayList();
    	for(Iterator ie = rs.iterator(); ie.hasNext();) {
    		Object[] projections = (Object[])ie.next();
    		TypedFlatMap flat = new TypedFlatMap();
    		flat.put("dsBox",((Short)projections[0]).toString() + ((projections[1] != null) ? " - " + (String)projections[1]: ""));
    		flat.put("idBox",projections[2]);
    		lResul.add(flat);
    	}
		return lResul;
    }
    
    /**
     * Consulta as docas disponiveis para o meio de transporte na filial informada
     * @param idFilial
     * @param idMeioTransporte
     * @return
     */
    public List findDocaBoxesDisponivelParaMeioTransporte(Long idFilial, Long idMeioTransporte, String tpControleCarga){
    	return findDocaBoxesDisponivelParaMeioTransporte(idFilial, idMeioTransporte, null, tpControleCarga);
    }
    
    /**
     * Consulta os boxes disponiveis para o meio de transporte na filial e doca informada
     * @param idFilial
     * @param idMeioTransporte
     * @param idDoca
     * @return
     */
    public List findDocaBoxesDisponivelParaMeioTransporte(Long idFilial, Long idMeioTransporte, Long idDoca, String tpControleCarga){
    	List result = new ArrayList();
    	
    	String nmCampoDs = (idDoca != null) ? "dsBox" : "dsDoca";
    	String nmCampoNr = (idDoca != null) ? "nrBox" : "nrDoca";
    	
    	List boxPreferencial = getBoxDAO().findDocaBoxesDisponivelParaMeioTransporte(idFilial, idMeioTransporte, idDoca, tpControleCarga);    	
    	if (!boxPreferencial.isEmpty()){
    		transformaResultadoDocaBox(boxPreferencial, nmCampoDs, nmCampoNr, true);
    		result.addAll(boxPreferencial);
    	}
    	    	
    	List box = getBoxDAO().findBoxDisponivel(idFilial, idMeioTransporte, idDoca, tpControleCarga);    	    	
    	if (!box.isEmpty()) {
    		transformaResultadoDocaBox(box, nmCampoDs, nmCampoNr, false);    	
    		result.addAll(box);
    	}
    	
    	return result;
    }
    
    public Integer getRowCountBoxesVigenteByTerminal(Long idTerminal) {
    	return getBoxDAO().getRowCountBoxesVigenteByTerminal(idTerminal);
    }
    
    /**
     * Transforma o resultado da consulta de Doca/Box disponivel, concatenando numero e descricao da doca ou box, e informando quando o 
     * mesmo eh preferencial
     * @param result
     * @param nmCampoDs
     * @param nmCampoNr
     * @param isPreferencial
     */
    private void transformaResultadoDocaBox(List result, String nmCampoDs, String nmCampoNr, boolean isPreferencial){
    	
    	if (!result.isEmpty()){
    		String preferencial = configuracoesFacade.getMensagem("preferencial");
    		for (Iterator it = result.iterator(); it.hasNext();){
    			Map item = (Map) it.next();
    			String campoNr = (item.get(nmCampoNr)).toString();
    			String campoDs = (String) item.get(nmCampoDs);
    			
    			StringBuffer linha = new StringBuffer();
    			
    			if (campoDs != null)
    				linha.append(campoNr)
    					 .append(" - ")
    					 .append(campoDs);
    			else
    				linha.append(campoNr.toString());
    			
    			if (isPreferencial) {
    				linha.append(" - ")
    					 .append(preferencial);    				
    			}
    			
    			item.put("isPreferencial", Boolean.valueOf(isPreferencial));
    			item.put(nmCampoDs, linha.toString());
    		}    		
    	}
    }
    
    public boolean validateIsBoxVigente(Long idBox, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	return getBoxDAO().isBoxVigente(idBox, dtVigenciaInicial, dtVigenciaFinal);
    }
    
    public List<Box> findLiberadoVigenteByFilial(Long idFilial) {
    	return getBoxDAO().findLiberadoVigenteByFilial(idFilial);
    }
    
    /**
     * Método que retorna uma combo de boxs vigentes para a filial passada por parâmetro.
     * @param criteria
     * @return
     */
    public List findBoxVigentePorFilial(Long idFilial){
       	List rs = getBoxDAO().findBoxVigentePorFilial(idFilial);
    	List lResul = new ArrayList();
    	for(Iterator ie = rs.iterator(); ie.hasNext();) {
    		Map projections = (HashMap)ie.next();
    		TypedFlatMap flat = new TypedFlatMap();
    		flat.put("idBox", projections.get("idBox"));
    		flat.put("dsBox", projections.get("nrBox") + ((projections.get("dsBox") != null) ? " - " + projections.get("dsBox"): ""));    		
    		lResul.add(flat);
    	}
		return lResul;
    }
    
    /**
     * Ocupa o box passado como parametro. Verificando se já existe um veiculo alocado no box
     * 
     * @param idBox
     * @return
     */
    public synchronized Box storeOcuparBox(Long idBox){
    	Box box = findById(idBox);
		return storeOcuparBox(box);
    }
    
    public Box storeOcuparBox(Box box){
		if(box != null && !"L".equals(box.getTpSituacaoBox().getValue())){					
			throw new BusinessException("LMS-06033");
		}
    	
		box.setTpSituacaoBox(new DomainValue("O"));
		store(box);
		return box;
    }
    
    /**
     * desOcupa o box passado como parametro. 
     * 
     * @param idBox
     * @return
     */
    public Box storeDesocuparBox(Long idBox){
    	Box box = findById(idBox);
    	return storeDesocuparBox(box);
    }
    
    /**
     * desOcupa o box passado como parametro. 
     * 
     * @param idBox
     * @return
     */
    public Box storeDesocuparBox(Box box){
    	box.setTpSituacaoBox(new DomainValue("L"));
		store(box);
		return box;
    }
    
	public List<Box> findByNrBoxAndIdFilial(Short nrBox, Long idFilial) {
		return getBoxDAO().findByNrBoxAndIdFilial(nrBox, idFilial);
	}
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setBoxDAO(BoxDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private BoxDAO getBoxDAO() {
        return (BoxDAO) getDao();
    }


	public void setDocaService(DocaService docaService) {
		this.docaService = docaService;
	}
	
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}


   }