package com.mercurio.lms.municipios.model.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.AtendimentoCliente;
import com.mercurio.lms.municipios.model.dao.AtendimentoClienteDAO;
import com.mercurio.lms.municipios.model.dao.OperacaoServicoLocalizaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.atendimentoClienteService"
 */
public class AtendimentoClienteService extends CrudService<AtendimentoCliente, Long> {

	private OperacaoServicoLocalizaService operacaoServicoLocalizaService;
	private VigenciaService vigenciaService;
	private OperacaoServicoLocalizaDAO operacaoServicoLocalizaDAO;
	
	/**
	 * Recupera uma instância de <code>AtendimentoCliente</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Map com os dados corespondentes ao id informado.
	 * @throws
	 */
	public AtendimentoCliente findById(java.lang.Long id) {
    	return (AtendimentoCliente)super.findById(id);      
    }
	public Map findByIdDetalhado(java.lang.Long id) {
    	AtendimentoCliente ac = (AtendimentoCliente)super.findById(id);
    	ac.setOperacaoServicoLocaliza(null);
    	ac.getCliente().getPessoa().setNrIdentificacao(FormatUtils.formatIdentificacao(ac.getCliente().getPessoa().getTpIdentificacao(), ac.getCliente().getPessoa().getNrIdentificacao()));
    	TypedFlatMap map = bean2map(ac);
    	map.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(ac));
    	
    	return map;
    	
    }
	
	private TypedFlatMap bean2map(AtendimentoCliente bean){
		TypedFlatMap map = new TypedFlatMap();
		
		map.put("idAtendimentoCliente", bean.getIdAtendimentoCliente());	
		map.put("cliente.pessoa.nrIdentificacao", bean.getCliente().getPessoa().getNrIdentificacao());
		map.put("cliente.pessoa.nmPessoa", bean.getCliente().getPessoa().getNmPessoa());
		map.put("cliente.idCliente", bean.getCliente().getIdCliente());
		map.put("dtVigenciaInicial", bean.getDtVigenciaInicial());
		map.put("dtVigenciaFinal", bean.getDtVigenciaFinal());
		
		return map;
	}
	
	public ResultSetPage findPaginated(Map criteria) {

        List included = new ArrayList();
        included.add("idAtendimentoCliente");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.nmMunicipio");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.siglaDescricao");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.unidadeFederativa.pais.nmPais");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.blDistrito");
        included.add("operacaoServicoLocaliza.municipioFilial.municipio.municipioDistrito.nmMunicipio");
        included.add("operacaoServicoLocaliza.municipioFilial.filial.siglaNomeFilial");
        included.add("operacaoServicoLocaliza.municipioFilial.filial.sgFilial");
        included.add("operacaoServicoLocaliza.municipioFilial.filial.pessoa.nmPessoa");
        included.add("operacaoServicoLocaliza.tpOperacao");
        included.add("operacaoServicoLocaliza.servico.dsServico");
        included.add("operacaoServicoLocaliza.tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio");
        
        included.add("operacaoServicoLocaliza.blDomingo");
        included.add("operacaoServicoLocaliza.blSegunda");
        included.add("operacaoServicoLocaliza.blTerca");
        included.add("operacaoServicoLocaliza.blQuarta");
        included.add("operacaoServicoLocaliza.blQuinta");
        included.add("operacaoServicoLocaliza.blSexta");
        included.add("operacaoServicoLocaliza.blSabado");
        
        included.add("cliente.pessoa.tpIdentificacao");
        included.add("cliente.pessoa.nrIdentificacaoFormatado");
        included.add("cliente.pessoa.nmPessoa");
        
        included.add("dtVigenciaInicial");
        included.add("dtVigenciaFinal");
 
        ResultSetPage rsp = super.findPaginated(criteria);
        rsp.setList((List) ReflectionUtils.copyAndFilterNestedBean(rsp.getList(), included));

        return rsp;
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
    public java.io.Serializable store(AtendimentoCliente bean) {
        return super.store(bean);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public Map storeMap(Map bean) {
    	AtendimentoCliente atendimentoCliente = new AtendimentoCliente();

        ReflectionUtils.copyNestedBean(atendimentoCliente,bean);

        vigenciaService.validaVigenciaBeforeStore(atendimentoCliente);

        super.store(atendimentoCliente);
        
        bean.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(atendimentoCliente));
        bean.put("idAtendimentoCliente", atendimentoCliente.getIdAtendimentoCliente());
        
        return bean; 
    }    
    
    /**
     * Verifica se existe registro de atendimento para o cliente informado
     * @param idCliente
     * @return TRUE se o cliente possui atendimento, FALSE caso contrario
     */
    public boolean validateExisteAtendimentoCliente(Long idCliente, Long idOperacaoServicoLocaliza, YearMonthDay dtVigencia){
    	return getAtendimentoClienteDAO().validateExisteAtendimentoCliente(idCliente, idOperacaoServicoLocaliza, dtVigencia);
    }
    
    /**
     * Verifica se existe registro de atendimento para a operação de serviço
     * informado.
     * 
     * @param idOperacaoServicoLocaliza
     * @param dtVigencia
     * @return
     */
    public boolean validateExisteAtendimentoCliente(Long idOperacaoServicoLocaliza, YearMonthDay dtVigencia) {
    	return getAtendimentoClienteDAO().validateExisteAtendimentoCliente(idOperacaoServicoLocaliza, dtVigencia);
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAtendimentoClienteDAO(AtendimentoClienteDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AtendimentoClienteDAO getAtendimentoClienteDAO() {
        return (AtendimentoClienteDAO) getDao();
    }
    
    
    protected AtendimentoCliente beforeStore(AtendimentoCliente bean) {
		AtendimentoCliente ac = (AtendimentoCliente)bean;
		ac.setCriterionDiasChecados(getOperacaoServicoLocalizaDAO().getCriterionDiasChecados(ac.getOperacaoServicoLocaliza(), "osl"));
		if (! getAtendimentoClienteDAO().verificaAtendimentoFrequenciasCliente(ac))
			throw new BusinessException("LMS-29021");
		
		// verifica se vigencia de OperacaoServicoLocaliza é válida
		if (! getOperacaoServicoLocalizaService().validaVigenciaAtendimento(ac.getOperacaoServicoLocaliza().getIdOperacaoServicoLocaliza(), ac.getDtVigenciaInicial(), ac.getDtVigenciaFinal()))
			throw new BusinessException("LMS-29023");
		
		return super.beforeStore(bean);
	}

	public OperacaoServicoLocalizaService getOperacaoServicoLocalizaService() {
		return operacaoServicoLocalizaService;
	}

	public void setOperacaoServicoLocalizaService(
			OperacaoServicoLocalizaService operacaoServicoLocalizaService) {
		this.operacaoServicoLocalizaService = operacaoServicoLocalizaService;
	}

	protected void beforeRemoveByIds(List ids) {
    	AtendimentoCliente bean = null;
    	for(Iterator ie = ids.iterator(); ie.hasNext();) {
    		bean = findById((Long)ie.next());    		
    		JTVigenciaUtils.validaVigenciaRemocao(bean);
    	}
    	super.beforeRemoveByIds(ids);
    }
    
    protected void beforeRemoveById(Long id) {
    	List list = new ArrayList();
    	list.add(id);
    	beforeRemoveByIds(list);
    }
    
    public List findAtendimentosVigentesByIdServicoLocalizacao(Long idServicoLocalizacao){
		return getAtendimentoClienteDAO().findAtendimentosVigentesByIdServicoLocalizacao(idServicoLocalizacao);
		
	}
	/**
	 * @param vigenciaService The vigenciaService to set.
	 */
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	
	public OperacaoServicoLocalizaDAO getOperacaoServicoLocalizaDAO() {
		return operacaoServicoLocalizaDAO;
	}
	
	public void setOperacaoServicoLocalizaDAO(
			OperacaoServicoLocalizaDAO operacaoServicoLocalizaDAO) {
		this.operacaoServicoLocalizaDAO = operacaoServicoLocalizaDAO;
	}
	
}