package com.mercurio.lms.portaria.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;
import org.springframework.context.i18n.LocaleContextHolder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.Domain;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.portaria.model.ConfiguracaoAuditoriaFil;
import com.mercurio.lms.portaria.model.dao.ConfiguracaoAuditoriaFilDAO;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.configuracaoAuditoriaFilService"
 */
public class ConfiguracaoAuditoriaFilService extends CrudService<ConfiguracaoAuditoriaFil, Long> {
	private VigenciaService vigenciaService;
	private DomainService domainService;

    /****************************************************************************************
     * Finders
     ****************************************************************************************/

	/**
	 * Recupera uma instância de <code>ConfiguracaoAuditoriaFil</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public ConfiguracaoAuditoriaFil findById(java.lang.Long id) {
        return (ConfiguracaoAuditoriaFil)super.findById(id);
    }
    // retorna os dados da tela de pesquisa, listagem
    public ResultSetPage findPaginatedCustom(TypedFlatMap criteria) {
		
		ResultSetPage rsp = getConfiguracaoAuditoriaFilDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		
		Domain dmTpOperacao = this.domainService.findByName("DM_TIPO_OPERACAO_AUDITORIA");
		Domain dmTipoRotaIdaVolta = this.domainService.findByName("DM_TIPO_ROTA_IDA_VOLTA");
		
		
		List newList = new ArrayList();
    	for (Iterator i = rsp.getList().iterator() ; i.hasNext() ; ) {
    		Object[] obj = (Object[])i.next();
    		Map map = new HashMap();
    		
    		map.put("idConfiguracaoAuditoriaFil",obj[0]);
    		map.put("idFilial",obj[1]);
    		map.put("sgFilial",obj[2]);
    		map.put("nmFantasia",obj[3]);
    		map.put("siglaNomeFilial",obj[4]);
    		map.put("nrFrota",obj[5]);
    		map.put("nrIdentificador",obj[6]);
    		map.put("hrAuditoriaInicial",obj[7]);
    		map.put("hrAuditoriaFinal",obj[8]);
    		map.put("dtVigenciaInicial",obj[9]);
    		map.put("dtVigenciaFinal",obj[10]);
    		map.put("dsSRotaColetaEntrega",obj[11]);   
    		map.put("nrRotaColetaEntrega",obj[12]);
    		
    		if (obj[13] != null && !"".equals((String)obj[13])) {
    			DomainValue dvTipoRotaViagem = dmTpOperacao.findDomainValueByValue((String)obj[13]);
    			map.put("tpOperacao",dvTipoRotaViagem.getDescription().getValue(LocaleContextHolder.getLocale()));
    		}
    		
    		map.put("dsRotaIdaVolta",obj[14]);
    		map.put("nrRota",obj[15]);
    		
    		if (obj[19] != null && !"".equals((String)obj[19])) {
    			DomainValue dvTipoRotaIdaVolta = dmTipoRotaIdaVolta.findDomainValueByValue((String)obj[19]);
    			map.put("tpRotaIdaVolta",dvTipoRotaIdaVolta.getDescription().getValue(LocaleContextHolder.getLocale()));
    		}
    		
    		// ISSO É NOVO
    		map.put("idMeioTransporte",obj[16]);
    		map.put("idRotaColetaEntrega",obj[17]);
    		map.put("idRotaIdaVolta", obj[18]);
    		
    		newList.add(map);
    	}
    	 
    	rsp.setList(newList);
    	return rsp;
    }
    // retorna os dados do detalhamento
    public TypedFlatMap findByIdCustom(TypedFlatMap criteria) {
		
		Object[] obj = getConfiguracaoAuditoriaFilDAO().findByIdCustom(criteria);
		
		Domain dmTipoRotaIdaVolta = this.domainService.findByName("DM_TIPO_ROTA_IDA_VOLTA");
		
		TypedFlatMap map = new TypedFlatMap();
					
		map.put("idConfiguracaoAuditoriaFil",obj[0]);
		
		map.put("filial.idFilial", obj[1]);
		map.put("filial.sgFilial", obj[2]);
		map.put("filial.pessoa.nmFantasia", obj[3]);				
		
		map.put("meioTransporteRodoviario.idMeioTransporte", obj[15]);
		map.put("meioTransporteRodoviario.meioTransporte.nrFrota", obj[5]);
		map.put("meioTransporteRodoviario.meioTransporte.nrIdentificador", obj[6]);
			
		map.put("hrAuditoriaInicial",obj[7]);
		map.put("hrAuditoriaFinal",obj[8]);
		map.put("dtVigenciaInicial",obj[9]);
		map.put("dtVigenciaFinal",obj[10]);
		
		map.put("tpOperacao",obj[12]);

		map.put("rotaIdaVolta.idRotaIdaVolta", obj[17]);
		map.put("rotaIdaVolta.nrRota", obj[13]);
		map.put("rotaViagem.dsRota", obj[14]);		
		
		map.put("rotaColetaEntrega.idRotaColetaEntrega", obj[16]);
		
		map.put("rotaIdaVolta.versao",obj[18]);
		
		if (obj[19] != null && !"".equals((String)obj[19])) {
			DomainValue dvTipoRotaIdaVolta = dmTipoRotaIdaVolta.findDomainValueByValue((String)obj[19]);
			map.put("rotaIdaVolta.tpRotaIdaVolta",dvTipoRotaIdaVolta.getDescription().getValue(LocaleContextHolder.getLocale()));
		}
			
		return map;
		
    }

    
    public Integer getRowCountCustom(TypedFlatMap tfm) {
    	return getConfiguracaoAuditoriaFilDAO().getRowCountCustom(tfm);
    }
    
	public boolean isThereConfiguracaoVigente(Long idConfiguracaoAuditoriaFil, Long idFilial, DomainValue tpOperacao, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal, TimeOfDay hrConfiguracaoInicial, TimeOfDay hrConfiguracaoFinal) {
		return getConfiguracaoAuditoriaFilDAO().isThereConfiguracaoVigente(idConfiguracaoAuditoriaFil, idFilial, tpOperacao, dtVigenciaInicial, dtVigenciaFinal, hrConfiguracaoInicial, hrConfiguracaoFinal);
	}

	 
    /****************************************************************************************
     * Métodos store
     ****************************************************************************************/
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(ConfiguracaoAuditoriaFil bean) {
    	 return super.store(bean);
    }
    
    protected ConfiguracaoAuditoriaFil beforeStore(ConfiguracaoAuditoriaFil bean) {
    	
    	ConfiguracaoAuditoriaFil caf = (ConfiguracaoAuditoriaFil) bean;
    	this.vigenciaService.validaVigenciaBeforeStore(caf);
    	        	
    	if (caf.getMeioTransporteRodoviario() == null && caf.getRotaColetaEntrega() == null && caf.getRotaIdaVolta() == null)
    		throw new BusinessException("LMS-06005");

    	if (! validaVigenciaCadastramento(caf))
    		throw new BusinessException("LMS-00003");
    	
    	if(caf.getRotaIdaVolta()!= null){
	    	if(!caf.getRotaIdaVolta().getRota().getDsRota().equalsIgnoreCase("")){
	    		String dsRotaIdaVolta = caf.getRotaIdaVolta().getRota().getDsRota().toUpperCase();
	    		
	    		/** Caso a filial seja destino da rota, não deve considerar a mesma.*/
	    		Filial filial = caf.getFilial();
				if(dsRotaIdaVolta.endsWith(filial.getSgFilial())) {
	    			throw new BusinessException("LMS-06030");
				}
				
	    		caf.getRotaIdaVolta().setRota(null);
	    	}
    	}
    	
    	return super.beforeStore(bean);
    }
     
	protected ConfiguracaoAuditoriaFil beforeInsert(ConfiguracaoAuditoriaFil bean) {
		return super.beforeInsert(bean);
	}
	
    /**
     * Não pode haver mais de uma configurações para a mesma filial, para o mesmo tipo de operacao,
     * e no mesmo intervalo de vigencia e horas específico
     * @param caf ConfiguracaoAuditoriaFil 
     * @return false, se não for possível cadastrar um novo registro
     * @author luisfco
     */
    public boolean validaVigenciaCadastramento(ConfiguracaoAuditoriaFil caf) {
    	return getConfiguracaoAuditoriaFilDAO().validaVigenciaCadastramento(caf);
    }
	
    /****************************************************************************************
     * Métodos remove
     ****************************************************************************************/
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
    
	protected void beforeRemoveByIds(List ids) {
		ConfiguracaoAuditoriaFil bean = null;
		for (Iterator ie = ids.iterator(); ie.hasNext();) {
			bean = findById((Long) ie.next());
			JTVigenciaUtils.validaVigenciaRemocao(bean);
		}		
	}

	protected void beforeRemoveById(Long id) {
		List list = new ArrayList();
		list.add(id);
		beforeRemoveByIds(list);
	}


    /****************************************************************************************
     * Getters e Setters
     ****************************************************************************************/

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setConfiguracaoAuditoriaFilDAO(ConfiguracaoAuditoriaFilDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ConfiguracaoAuditoriaFilDAO getConfiguracaoAuditoriaFilDAO() {
        return (ConfiguracaoAuditoriaFilDAO) getDao();
    }

	public DomainService getDomainService() {
		return domainService;
	}

	public void setDomainService(DomainService domainService) {
		this.domainService = domainService;
	}

	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}
    
   }