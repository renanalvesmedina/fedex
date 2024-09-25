package com.mercurio.lms.portaria.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.Box;
import com.mercurio.lms.portaria.model.Doca;
import com.mercurio.lms.portaria.model.Terminal;
import com.mercurio.lms.portaria.model.dao.DocaDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.docaService"
 */
public class DocaService extends CrudService<Doca, Long> {

	private VigenciaService vigenciaService;
	private TerminalService terminalService;
	/**
	 * Recupera uma instância de <code>Doca</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public Doca findById(java.lang.Long id) {
        return (Doca)super.findById(id);
    }
    
    public Integer getRowCountDocasVigenteByTerminal(Long idTerminal) {
    	return getDocaDAO().getRowCountDocasVigenteByTerminal(idTerminal);
    }
    
    public List findDocaVigenteByTerminal(Long idTerminal){
    	return getDocaDAO().findDocaVigenteByTerminal(idTerminal);
    }

    public boolean findDocaValidaVigencias(Long idDoca, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {	
    	return getDocaDAO().findDocaValidaVigencias(idDoca, dtVigenciaInicial, dtVigenciaFinal);
    }
    
    public List findDocaVigenteByTerminal(Long idTerminal, Long idDoca){
    	return getDocaDAO().findDocaVigenteByTerminal(idTerminal, idDoca);
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
    

    public List findCombo(Map criteria) {
    	List rs = getDocaDAO().findCombo(criteria);
    	List lResul = new ArrayList();
    	for(Iterator ie = rs.iterator(); ie.hasNext();) {
    		Object[] projections = (Object[])ie.next();
    		TypedFlatMap flat = new TypedFlatMap();
    		flat.put("dsDoca",((Short)projections[0]).toString() + ((projections[1] != null) ? " - " + (String)projections[1]: ""));
    		flat.put("idDoca",projections[2]);
    		lResul.add(flat);
    	}
		return lResul;
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(Doca bean) {
        return super.store(bean);
    }
    
    protected Doca beforeStore(Doca bean) {
    	Doca doca = (Doca) bean;
    	
    	if (getDocaDAO().verificaVigencia(doca))
    		throw new BusinessException("LMS-00003");
    	
    	if (!terminalService.findTerminalValidaVigencia(doca.getTerminal().getIdTerminal(), doca.getDtVigenciaInicial(), doca.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	if (vigenciaService.validateIntegridadeVigencia(Box.class,"doca",doca))
        		throw new BusinessException("LMS-06024");
    	
    	return super.beforeStore(bean);
    }
    
	public TypedFlatMap storeMap(TypedFlatMap map) {

		Doca doca = map2bean(map);
		
		vigenciaService.validaVigenciaBeforeStore(doca);
		
	    super.store(doca);
	    
	    TypedFlatMap retorno = new TypedFlatMap();
	    
	    retorno.put("idDoca", doca.getIdDoca());
	    retorno.put("dtVigenciaInicial", doca.getDtVigenciaInicial());
	    retorno.put("numeroDescricaoDoca", doca.getNumeroDescricaoDoca());
	    Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(doca);
	    retorno.put("acaoVigenciaAtual",acaoVigencia);
	    return retorno;
	}
	
	
	private Doca map2bean(TypedFlatMap map){
		
		Doca doca = new Doca();
		
		doca.setIdDoca(map.getLong("idDoca"));
		doca.setDtVigenciaInicial(map.getYearMonthDay("dtVigenciaInicial"));
		doca.setDtVigenciaFinal(map.getYearMonthDay("dtVigenciaFinal"));
		doca.setDsDoca(map.getString("dsDoca"));
		doca.setNrDoca(map.getShort("nrDoca"));
		doca.setObDoca(map.getString("obDoca"));
		doca.setTpSituacaoDoca(map.getDomainValue("tpSituacaoDoca"));
		
		Terminal terminal = new Terminal();
		terminal.setIdTerminal(map.getLong("terminal.idTerminal"));
		doca.setTerminal(terminal);
		
		return doca;
	}
	
	
    public Map findByIdDetalhamento(java.lang.Long id) {
    	Doca doca = (Doca) super.findById(id);
    	Terminal terminal = doca.getTerminal();
    	Filial filial = terminal.getFilial();
    	
        TypedFlatMap retorno = new TypedFlatMap();
        retorno.put("idDoca",doca.getIdDoca());
        retorno.put("terminal.filial.idFilial",filial.getIdFilial());
        retorno.put("terminal.filial.sgFilial",filial.getSgFilial());
        retorno.put("terminal.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
        retorno.put("terminal.idTerminal",terminal.getIdTerminal());
        retorno.put("terminal.pessoa.nmPessoa",terminal.getPessoa().getNmPessoa());
        retorno.put("terminal.dtVigenciaInicial",terminal.getDtVigenciaInicial());
        retorno.put("terminal.dtVigenciaFinal",terminal.getDtVigenciaFinal());
        retorno.put("idTerminalTemp",terminal.getIdTerminal());
        retorno.put("nrDoca",doca.getNrDoca());
        retorno.put("dsDoca",doca.getDsDoca());
        retorno.put("tpSituacaoDoca.value",doca.getTpSituacaoDoca().getValue());
        retorno.put("obDoca",doca.getObDoca());
        retorno.put("dtVigenciaInicial",doca.getDtVigenciaInicial());
        retorno.put("dtVigenciaFinal",doca.getDtVigenciaFinal());
        
        Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(doca);
        retorno.put("acaoVigenciaAtual",acaoVigencia);
        
        return retorno;           
    }
    
	protected void beforeRemoveById(Long id) {
		Doca doca = findById((Long)id);
		JTVigenciaUtils.validaVigenciaRemocao(doca);
		super.beforeRemoveById(id);
	}

    protected void beforeRemoveByIds(List ids) {
    	Doca doca = null;
    	for(int x = 0; x < ids.size(); x++) {
    		doca = findById((Long)ids.get(x));
    		JTVigenciaUtils.validaVigenciaRemocao(doca);
    	}
    	super.beforeRemoveByIds(ids);
    }
 

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDocaDAO(DocaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DocaDAO getDocaDAO() {
        return (DocaDAO) getDao();
    }
    
    public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * @param terminalService The terminalService to set.
	 */
	public void setTerminalService(TerminalService terminalService) {
		this.terminalService = terminalService;
	}

}