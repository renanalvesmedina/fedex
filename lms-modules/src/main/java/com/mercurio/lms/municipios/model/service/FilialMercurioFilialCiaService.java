package com.mercurio.lms.municipios.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.municipios.model.FilialMercurioFilialCia;
import com.mercurio.lms.municipios.model.dao.FilialMercurioFilialCiaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.filialMercurioFilialCiaService"
 */
public class FilialMercurioFilialCiaService extends CrudService<FilialMercurioFilialCia, Long> {

	private VigenciaService vigenciaService;
	private FilialService filialService;

	/**
	 * Recupera uma instância de <code>FilialMercurioFilialCia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public FilialMercurioFilialCia findById(java.lang.Long id) {
        return (FilialMercurioFilialCia)super.findById(id);
    }
    
	/**
	 * Recupera uma instância de <code>FilialMercurioFilialCia</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
    	FilialMercurioFilialCia filialMercurioFilialCia = (FilialMercurioFilialCia)super.findById(id);
    	FilialCiaAerea filialCiaAerea = filialMercurioFilialCia.getFilialCiaAerea();
		CiaFilialMercurio ciaFilialMercurio = filialMercurioFilialCia.getCiaFilialMercurio();
		Empresa empresa = ciaFilialMercurio.getEmpresa();
		Filial filial = ciaFilialMercurio.getFilial();
		Aeroporto aeroporto = filialCiaAerea.getAeroporto();
    	
    	TypedFlatMap retorno = new TypedFlatMap();
    	
		retorno.put("idFilialMercurioFilialCia",filialMercurioFilialCia.getIdFilialMercurioFilialCia());
    	retorno.put("filialCiaAerea.idFilialCiaAerea",filialCiaAerea.getIdFilialCiaAerea());
    	
    	Pessoa pessoa = filialCiaAerea.getPessoa();
    	String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
    	retorno.put("filialCiaAerea.pessoa.nrIdentificacaoFormatado",nrIdentificacao);
    	retorno.put("filialCiaAerea.pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
    	retorno.put("filialCiaAerea.pessoa.nmPessoa",pessoa.getNmPessoa());
    	
    	retorno.put("ciaFilialMercurio.empresa.pessoa.nmPessoa",empresa.getPessoa().getNmPessoa());
    	retorno.put("ciaFilialMercurio.empresa.idEmpresa",empresa.getIdEmpresa());
    	retorno.put("filialCiaAerea.empresa.idEmpresa",filialCiaAerea.getEmpresa().getIdEmpresa());
    	
    	retorno.put("filialCiaAerea.aeroporto.idAeroporto",aeroporto.getIdAeroporto());
    	retorno.put("filialCiaAerea.aeroporto.sgAeroporto",aeroporto.getSgAeroporto());
    	retorno.put("filialCiaAerea.aeroporto.pessoa.nmPessoa",aeroporto.getPessoa().getNmPessoa());
    	
    	retorno.put("ciaFilialMercurio.idCiaFilialMercurio",ciaFilialMercurio.getIdCiaFilialMercurio());
    	retorno.put("ciaFilialMercurio.filial.idFilial",filial.getIdFilial());
    	retorno.put("ciaFilialMercurio.filial.sgFilial",filial.getSgFilial());
    	retorno.put("ciaFilialMercurio.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
    	
    	
    	retorno.put("nrOrdemUso",filialMercurioFilialCia.getNrOrdemUso());
    	retorno.put("dtVigenciaInicial",filialMercurioFilialCia.getDtVigenciaInicial());
    	retorno.put("dtVigenciaFinal",filialMercurioFilialCia.getDtVigenciaFinal());
    	
    	retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(filialMercurioFilialCia));
    	
    	return retorno;
    }
    
    public ResultSetPage findPaginated(Map criteria) {
    	FilterResultSetPage frsp = new FilterResultSetPage(super.findPaginated(criteria)) {

			public Map filterItem(Object item) {
				TypedFlatMap row = new TypedFlatMap();
				
				FilialMercurioFilialCia filialMercurioFilialCia = (FilialMercurioFilialCia)item;
				FilialCiaAerea filialCiaAerea = filialMercurioFilialCia.getFilialCiaAerea();
				Pessoa pessoa = filialCiaAerea.getPessoa();
				CiaFilialMercurio ciaFilialMercurio = filialMercurioFilialCia.getCiaFilialMercurio();
				Filial filial = ciaFilialMercurio.getFilial();
				Aeroporto aeroporto = filialCiaAerea.getAeroporto();
				
				row.put("idFilialMercurioFilialCia",filialMercurioFilialCia.getIdFilialMercurioFilialCia());
		        row.put("filialCiaAerea.pessoa.idPessoa",pessoa.getIdPessoa());
		        row.put("filialCiaAerea.pessoa.tpIdentificacao",pessoa.getTpIdentificacao());
		        row.put("filialCiaAerea.pessoa.nrIdentificacao",pessoa.getNrIdentificacao());
		        String nrIdentificacao = FormatUtils.formatIdentificacao(pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
		        row.put("filialCiaAerea.pessoa.nrIdentificacaoFormatado",nrIdentificacao);
		        row.put("filialCiaAerea.pessoa.nmPessoa",pessoa.getNmPessoa());
		        row.put("ciaFilialMercurio.filial.sgFilial",filial.getSgFilial());
		        row.put("ciaFilialMercurio.filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		        row.put("ciaFilialMercurio.empresa.pessoa.nmPessoa",ciaFilialMercurio.getEmpresa().getPessoa().getNmPessoa());
		        row.put("filialCiaAerea.aeroporto.pessoa.nmPessoa",aeroporto.getPessoa().getNmPessoa());
		        row.put("filialCiaAerea.aeroporto.sgAeroporto",aeroporto.getSgAeroporto());
		        row.put("nrOrdemUso",filialMercurioFilialCia.getNrOrdemUso());
		        row.put("dtVigenciaInicial",filialMercurioFilialCia.getDtVigenciaInicial());
		        row.put("dtVigenciaFinal",filialMercurioFilialCia.getDtVigenciaFinal());

		        return row;
			}
    		
    	};
    	
        return (ResultSetPage)frsp.doFilter();
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
    public java.io.Serializable store(FilialMercurioFilialCia bean) {
        return super.store(bean);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeMap(Map bean) {
    	FilialMercurioFilialCia filialMercurioFilialCia = new FilialMercurioFilialCia();

        ReflectionUtils.copyNestedBean(filialMercurioFilialCia,bean);

        vigenciaService.validaVigenciaBeforeStore(filialMercurioFilialCia);

        super.store(filialMercurioFilialCia);
        
        TypedFlatMap retorno = new TypedFlatMap();
        retorno.put("idFilialMercurioFilialCia",filialMercurioFilialCia.getIdFilialMercurioFilialCia());
        retorno.put("acaoVigenciaAtual",JTVigenciaUtils.getIntegerAcaoVigencia(filialMercurioFilialCia));
        return retorno;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setFilialMercurioFilialCiaDAO(FilialMercurioFilialCiaDAO dao) {
        setDao( dao );
    }    
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private FilialMercurioFilialCiaDAO getFilialMercurioFilialCiaDAO() {
        return (FilialMercurioFilialCiaDAO) getDao();
    }
    
    protected FilialMercurioFilialCia beforeStore(FilialMercurioFilialCia bean) {
    	FilialMercurioFilialCia filialMercurioFilialCia = (FilialMercurioFilialCia)super.beforeStore(bean);
    	
    	FilialCiaAerea filialCiaAerea = new FilialCiaAerea();
    	filialCiaAerea.setIdFilialCiaAerea(filialMercurioFilialCia.getFilialCiaAerea().getIdFilialCiaAerea());
    	if (!vigenciaService.validateEntidadeVigente(filialCiaAerea,
    			filialMercurioFilialCia.getDtVigenciaInicial(),
    			filialMercurioFilialCia.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	CiaFilialMercurio ciaFilialMercurio = new CiaFilialMercurio();
    	ciaFilialMercurio.setIdCiaFilialMercurio(filialMercurioFilialCia.getCiaFilialMercurio().getIdCiaFilialMercurio());
    	if (!vigenciaService.validateEntidadeVigente(ciaFilialMercurio,
    			filialMercurioFilialCia.getDtVigenciaInicial(),
    			filialMercurioFilialCia.getDtVigenciaFinal()))
    		throw new BusinessException("LMS-29023");
    	
    	if (getFilialMercurioFilialCiaDAO().verificaAssocCiaAereaMercurio(filialMercurioFilialCia))
    		throw new BusinessException("LMS-00003");
    	
    	filialService.verificaVigenciasEmHistoricoFilial(filialMercurioFilialCia.getCiaFilialMercurio().getFilial().getIdFilial(),
    			filialMercurioFilialCia.getDtVigenciaInicial(),filialMercurioFilialCia.getDtVigenciaFinal());

    	return filialMercurioFilialCia;
    }
    
    /**
     * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
     * @param id Id do registro a ser validado.
     */
	private void validaRemoveById(Long id) {
		FilialMercurioFilialCia filialMercurioFilialCia = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(filialMercurioFilialCia);
	}
	
	protected void beforeRemoveById(Long id) {
		validaRemoveById((Long)id);
		super.beforeRemoveById(id);
	}
	
	protected void beforeRemoveByIds(List ids) {
		for (Iterator i = ids.iterator() ; i.hasNext() ;)
	        validaRemoveById((Long)i.next());
		super.beforeRemoveByIds(ids);
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
	
	public boolean findFilialCiaAreaById(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
		return getFilialMercurioFilialCiaDAO().findFilialCiaAreaById(dataInicio,  dataFim, idFilialCiaAerea);
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	
	
	
}