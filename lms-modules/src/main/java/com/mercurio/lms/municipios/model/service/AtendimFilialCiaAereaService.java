package com.mercurio.lms.municipios.model.service;

import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.municipios.model.AtendimFilialCiaAerea;
import com.mercurio.lms.municipios.model.FilialCiaAerea;
import com.mercurio.lms.municipios.model.dao.AtendimFilialCiaAereaDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.atendimFilialCiaAereaService"
 */
public class AtendimFilialCiaAereaService extends CrudService<AtendimFilialCiaAerea, Long> {
	private VigenciaService vigenciaService;
	private FilialCiaAereaService filialCiaAereaService;
	
	/**
	 * Recupera uma instância de <code>AtendimFilialCiaAerea</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */

    public Map findByIdMap(Long id) {
        AtendimFilialCiaAerea ciaAerea = (AtendimFilialCiaAerea) super.findById(id);
        
        TypedFlatMap map = bean2map(ciaAerea);
        map.put("acaoVigenciaAtual", JTVigenciaUtils.getIntegerAcaoVigencia(ciaAerea));
      
        return map;
    }
    
    private TypedFlatMap bean2map(AtendimFilialCiaAerea bean){
    	
    	TypedFlatMap map = new TypedFlatMap();
    	
    	map.put("idAtendimFilialCiaAerea", bean.getIdAtendimFilialCiaAerea());    	
    	map.put("blDomingo", bean.getBlDomingo()); 
    	map.put("blSegunda", bean.getBlSegunda());
    	map.put("blTerca", bean.getBlTerca());
    	map.put("blQuarta", bean.getBlQuarta());
    	map.put("blQuinta", bean.getBlQuinta());
    	map.put("blSexta", bean.getBlSexta());
    	map.put("blSabado", bean.getBlSabado());
    	map.put("hrAtendimentoInicial", bean.getHrAtendimentoInicial());
    	map.put("hrAtendimentoFinal", bean.getHrAtendimentoFinal());
    	map.put("dtVigenciaInicial", bean.getDtVigenciaInicial());
    	map.put("dtVigenciaFinal", bean.getDtVigenciaFinal());
    	
    	FilialCiaAerea filialCiaAerea = bean.getFilialCiaAerea();
    	map.put("filialCiaAerea.idFilialCiaAerea", filialCiaAerea.getIdFilialCiaAerea());
    	Pessoa ciaAerea = filialCiaAerea.getPessoa();
		map.put("filialCiaAerea.pessoa.nrIdentificacao", ciaAerea.getNrIdentificacao());
    	map.put("filialCiaAerea.pessoa.nrIdentificacaoFormatado",
    	FormatUtils.formatIdentificacao(ciaAerea.getTpIdentificacao(),ciaAerea.getNrIdentificacao()));
    	map.put("filialCiaAerea.pessoa.nmPessoa", ciaAerea.getNmPessoa());
    	map.put("filialCiaAerea.empresa.pessoa.nmPessoa", filialCiaAerea.getEmpresa().getPessoa().getNmPessoa());
    	map.put("filialCiaAerea.aeroporto.pessoa.nmPessoa", filialCiaAerea.getAeroporto().getPessoa().getNmPessoa());
    	
    	return map;
    }
    
    
	protected void beforeRemoveById(Long id) {
		AtendimFilialCiaAerea ciaAerea = new AtendimFilialCiaAerea();
		ReflectionUtils.copyNestedBean(ciaAerea, findById((Long)id));
				
		JTVigenciaUtils.validaVigenciaRemocao(ciaAerea);
		super.beforeRemoveById(id);
	}

    protected void beforeRemoveByIds(List ids) {
    	AtendimFilialCiaAerea ciaAerea = new AtendimFilialCiaAerea();
    	for(int x = 0; x < ids.size(); x++) {
	    	JTVigenciaUtils.validaVigenciaRemocao((AtendimFilialCiaAerea)findById((Long)ids.get(x)));
    	}
    	super.beforeRemoveByIds(ids);
    }
    
	protected AtendimFilialCiaAerea beforeStore(AtendimFilialCiaAerea bean) {
		AtendimFilialCiaAerea filial = (AtendimFilialCiaAerea) bean;
		
		if (filial.getBlDomingo().booleanValue() == false &&
				filial.getBlSegunda().booleanValue() == false &&
				filial.getBlTerca().booleanValue() == false &&
				filial.getBlQuarta().booleanValue() == false &&
				filial.getBlQuinta().booleanValue() == false &&
				filial.getBlSexta().booleanValue() == false &&
				filial.getBlSabado().booleanValue() == false
			)
			throw new BusinessException("LMS-29041");
		
		if (!filialCiaAereaService.verificaFilialCiaAereaVigente(filial.getFilialCiaAerea().getIdFilialCiaAerea(), filial.getDtVigenciaInicial(), filial.getDtVigenciaFinal())){
			throw new BusinessException("LMS-29023");
		}
			
    	if (this.getAtendimFilialCiaAereaDAO().validaVigenciaCiaAerea((AtendimFilialCiaAerea)bean))
    		throw new BusinessException("LMS-00003");    	
    	
		return super.beforeStore(bean);
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
    public java.io.Serializable store(AtendimFilialCiaAerea bean) {
        return super.store(bean);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeMap(Map bean) {
    	AtendimFilialCiaAerea atendimFilialCiaAerea = new AtendimFilialCiaAerea();

        ReflectionUtils.copyNestedBean(atendimFilialCiaAerea,bean);

        vigenciaService.validaVigenciaBeforeStore(atendimFilialCiaAerea);

        super.store(atendimFilialCiaAerea);
        
        TypedFlatMap retorno = new TypedFlatMap();
        retorno.put("idAtendimFilialCiaAerea",atendimFilialCiaAerea.getIdAtendimFilialCiaAerea());
        Integer acaoVigenciaAtual = JTVigenciaUtils.getIntegerAcaoVigencia(atendimFilialCiaAerea);
        retorno.put("acaoVigenciaAtual",acaoVigenciaAtual);
        
        return retorno;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAtendimFilialCiaAereaDAO(AtendimFilialCiaAereaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AtendimFilialCiaAereaDAO getAtendimFilialCiaAereaDAO() {
        return (AtendimFilialCiaAereaDAO) getDao();
    }

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * @return Returns the filialCiaAereaService.
	 */
	public FilialCiaAereaService getFilialCiaAereaService() {
		return filialCiaAereaService;
	}

	/**
	 * @param filialCiaAereaService The filialCiaAereaService to set.
	 */
	public void setFilialCiaAereaService(FilialCiaAereaService filialCiaAereaService) {
		this.filialCiaAereaService = filialCiaAereaService;
	}
	
	public boolean findAtendimByFilialCiaArea(YearMonthDay dataInicio, YearMonthDay dataFim, Long idFilialCiaAerea){
		return getAtendimFilialCiaAereaDAO().findAtendimByFilialCiaArea(dataInicio,  dataFim, idFilialCiaAerea);
	}
   }