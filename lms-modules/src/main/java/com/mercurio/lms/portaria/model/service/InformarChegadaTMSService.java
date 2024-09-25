package com.mercurio.lms.portaria.model.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.expedicao.util.ExpedicaoUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.portaria.model.ManifestoTMS;
import com.mercurio.lms.portaria.model.dao.ManifestoTMSDAO;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.sim.model.util.ConstantesEventosDocumentoServico;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.portaria.portariaService"
 */
public class InformarChegadaTMSService extends CrudService<ManifestoTMS, Long> {
    private FilialService filialService;
    private ConfiguracoesFacade configuracoesFacade;
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;  

    public TypedFlatMap findFilialUsuarioLogado() {
    	Filial f = filialService.getFilialUsuarioLogado();
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("filial.idFilial",f.getIdFilial());
    	result.put("filial.sgFilial",f.getSgFilial());
    	result.put("filial.pessoa.nmFantasia",f.getPessoa().getNmFantasia());
    	return result;
    }
	
    public List findLookupFilial(Map criteria) {
    	return filialService.findLookupFilial(criteria);
	}
    
    public ManifestoTMS findById(java.lang.Long id) {
        return (ManifestoTMS)super.findById(id);
    }

	public Integer getRowCount(Map criteria) {
		return getManifestoTMSDAO().getRowCount(criteria);
	}

	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage<TypedFlatMap> rsp = getManifestoTMSDAO().findPaginated(criteria, FindDefinition.createFindDefinition(criteria));
		return rsp;
	}

	public Integer findMaxQtdCtosManifestoTMS(Long idFilial, Long nrManifesto) {
		return getManifestoTMSDAO().findMaxQtdCtosManifestoTMS(idFilial, nrManifesto);
	}

	public Integer findQtdRegistrosManifestoTMS(Long idFilial, Long nrManifesto) {
		return getManifestoTMSDAO().findQtdRegistrosManifestoTMS(idFilial, nrManifesto);
	}

	public List<ManifestoTMS> findManifestosTMS(Long idFilial, Long nrManifesto) {
		return getManifestoTMSDAO().findManifestosTMS(idFilial, nrManifesto);
	}
		
	public Boolean validateQtdConhecimentos(Long idFilial, Long nrManifesto){
		Integer max = findMaxQtdCtosManifestoTMS(idFilial,nrManifesto);
		Integer qtd = findQtdRegistrosManifestoTMS(idFilial, nrManifesto);
		if( max == null ){
			return false;
		}
		if( qtd == null ){
			return false;
		}
		return (qtd.compareTo(max) == 0);
	}
	
	public void storeDhChegada(Long idFilial, Long nrManifesto) {
		List<ManifestoTMS> list = findManifestosTMS(idFilial,nrManifesto);
		for(ManifestoTMS manifestoTMS : list ){
			manifestoTMS.setDhChegada(JTDateTimeUtils.getDataHoraAtual());
			store(manifestoTMS);
			executeEventoRastreabilidade(manifestoTMS);
		}
	}
	
	private void executeEventoRastreabilidade(ManifestoTMS manifestoTMS){
		DoctoServico doctoServico = manifestoTMS.getDoctoServico();
		final Short cdLocalizacao = 1;
		if( !cdLocalizacao.equals(doctoServico.getLocalizacaoMercadoria().getCdLocalizacaoMercadoria()) ){
			Filial filial = manifestoTMS.getFilial();
			final Short CD_EVENTO_CHEGADA_PORTARIA = 201;
			String nrDocumento = String.format("%s %s",doctoServico.getFilialByIdFilialOrigem().getSgFilial(),doctoServico.getNrDoctoServico());
			incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(CD_EVENTO_CHEGADA_PORTARIA,doctoServico.getIdDoctoServico(), filial.getIdFilial(), nrDocumento, manifestoTMS.getDhChegada(),null, filial.getSiglaNomeFilial(),ConstantesExpedicao.CONHECIMENTO_NACIONAL);
		}
	}
	
	public Integer findTempoDescargaRnc(){
		final String TEMPO_DESCARGA_RNC_TMS = "TEMPO_DESC_RNC_TMS";
		BigDecimal r = (BigDecimal)configuracoesFacade.getValorParametro(TEMPO_DESCARGA_RNC_TMS);
		if( r != null ){
			return r.intValue();
		}
		return 0; 
	}

	public ManifestoTMS findManifestoTMSByDoctoServico(Long idDoctoServico){
		return getManifestoTMSDAO().findManifestoTMSByDoctoServico(idDoctoServico);
	}

	public Boolean validateDctoServicoChegada(Long idDoctoServico){
		ManifestoTMS manifestoTMS = findManifestoTMSByDoctoServico(idDoctoServico);
		Integer intervaloChegada = findTempoDescargaRnc();
		if( intervaloChegada == null ){
			return false;
		}

		if( manifestoTMS == null ){
			return false;
		}
		DateTime dhChegada = (DateTime)manifestoTMS.getDhChegada();
		if( dhChegada == null ){
			return false;
		}

		Integer diffDhChegada = JTDateTimeUtils.getIntervalInHours(dhChegada,JTDateTimeUtils.getDataHoraAtual());
		if( diffDhChegada == null || diffDhChegada < 0 ){
			return false;
		}
		
		if( diffDhChegada < intervaloChegada ){
			return true;
		}
		return false;
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
    public java.io.Serializable store(ManifestoTMS bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setManifestoTMSDAO(ManifestoTMSDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private ManifestoTMSDAO getManifestoTMSDAO() {
        return (ManifestoTMSDAO) getDao();
    }

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	
    public void setIncluirEventosRastreabilidadeInternacionalService(
			IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}
}