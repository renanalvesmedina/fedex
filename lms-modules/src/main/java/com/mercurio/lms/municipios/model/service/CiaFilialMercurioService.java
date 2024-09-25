package com.mercurio.lms.municipios.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.util.ReflectionUtils;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.Awb;
import com.mercurio.lms.municipios.model.CiaFilialMercurio;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.FilialMercurioFilialCia;
import com.mercurio.lms.municipios.model.ObservacaoCiaAerea;
import com.mercurio.lms.municipios.model.dao.CiaFilialMercurioDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;
import com.mercurio.lms.util.model.service.VigenciaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.ciaFilialMercurioService"
 */
public class CiaFilialMercurioService extends CrudService<CiaFilialMercurio, Long> {

	private FilialService filialService;
	private EmpresaService empresaService;
	private VigenciaService vigenciaService;
	
	public void setVigenciaService(VigenciaService vigenciaService) {
		this.vigenciaService = vigenciaService;
	}

	/**
	 * Recupera uma instância de <code>CiaFilialMercurio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public CiaFilialMercurio findById(java.lang.Long id) {
        return (CiaFilialMercurio)super.findById(id);
    }
	
	/**
	 * Recupera uma instância de <code>CiaFilialMercurio</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public TypedFlatMap findByIdDetalhamento(java.lang.Long id) {
    	CiaFilialMercurio ciaFilialMercurio = (CiaFilialMercurio)super.findById(id);
    	
    	TypedFlatMap retorno = new TypedFlatMap();
        retorno.put("idCiaFilialMercurio",ciaFilialMercurio.getIdCiaFilialMercurio());
        
        Filial filial = ciaFilialMercurio.getFilial();
        Empresa empresaFilial = ciaFilialMercurio.getEmpresa();
        retorno.put("filial.empresa.tpEmpresa",empresaFilial.getTpEmpresa());
        retorno.put("filial.idFilial",filial.getIdFilial());
        retorno.put("filial.sgFilial",filial.getSgFilial());
        retorno.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
        
        Empresa empresa = ciaFilialMercurio.getEmpresa();
        retorno.put("empresa.idEmpresa",empresa.getIdEmpresa());
        retorno.put("empresa.pessoa.nmPessoa",empresa.getPessoa().getNmPessoa());
        retorno.put("empresa.tpSituacao.description",empresa.getTpSituacao().getDescription().getValue());
        
        ObservacaoCiaAerea observacaoCiaAerea = ciaFilialMercurio.getObservacaoCiaAerea();
        retorno.put("observacaoCiaAerea.idObservacaoCiaAerea",observacaoCiaAerea.getIdObservacaoCiaAerea());
        retorno.put("observacaoCiaAerea.dsObservacaoCiaAerea",observacaoCiaAerea.getDsObservacaoCiaAerea());
        
        retorno.put("nrPrestacaoContas",ciaFilialMercurio.getNrPrestacaoContas());
        retorno.put("dsIdentificadorCiaAerea",ciaFilialMercurio.getDsIdentificadorCiaAerea());
        retorno.put("vlIdentificadorCiaAerea",ciaFilialMercurio.getVlIdentificadorCiaAerea());
        retorno.put("tpUso.value",ciaFilialMercurio.getTpUso().getValue());
        retorno.put("dtVigenciaInicial",ciaFilialMercurio.getDtVigenciaInicial());
        retorno.put("dtVigenciaFinal",ciaFilialMercurio.getDtVigenciaFinal());
        
    	Integer acaoVigencia = JTVigenciaUtils.getIntegerAcaoVigencia(ciaFilialMercurio);
    	retorno.put("acaoVigenciaAtual",acaoVigencia);
    	
    	return retorno;
    }

    public ResultSetPage findPaginated(Map criteria) {
    	ResultSetPage rsp = super.findPaginated(criteria);
    	FilterResultSetPage frsp = new FilterResultSetPage(rsp) {

			public Map filterItem(Object item) {
				CiaFilialMercurio ciaFilialMercurio = (CiaFilialMercurio)item;
				Filial filial = ciaFilialMercurio.getFilial();
				Empresa empresa = ciaFilialMercurio.getEmpresa();
				TypedFlatMap row = new TypedFlatMap();
				row.put("idCiaFilialMercurio",ciaFilialMercurio.getIdCiaFilialMercurio());
				
		        // Deve ser retornado o idEmpresa, pois o mesmo é ultilizado na lookup de FilialMercurioFilialCia.
		        row.put("empresa.idEmpresa",empresa.getIdEmpresa());
		        row.put("empresa.pessoa.nmPessoa",empresa.getPessoa().getNmPessoa());
		        row.put("empresa.tpSituacao",empresa.getTpSituacao());
		        
		        //Deve ser retornado a sgFilial, pois a mesma é ultilizada na lookup de FilialMercurioFilialCia.
		        row.put("filial.idFilial",filial.getIdFilial());
		        row.put("filial.sgFilial",filial.getSgFilial());
		        row.put("filial.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		        
		        row.put("tpUso",ciaFilialMercurio.getTpUso());
		        row.put("dtVigenciaInicial",ciaFilialMercurio.getDtVigenciaInicial());
		        row.put("dtVigenciaFinal",ciaFilialMercurio.getDtVigenciaFinal());
		        
		        return row;
			}
    		
    	};
 
        return (ResultSetPage)frsp.doFilter();
    }
    
    public CiaFilialMercurio findByIdCiaAereaIdFilial(Long idCiaAerea, Long idFilial) {
    	return getCiaFilialMercurioDAO().findByIdCiaAereaIdFilial(idCiaAerea, idFilial);
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
    public java.io.Serializable store(CiaFilialMercurio bean) {
        return super.store(bean);
    }

    /**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeMap(Map bean) {
		
    	CiaFilialMercurio ciaFilialMercurio = new CiaFilialMercurio();

        ReflectionUtils.copyNestedBean(ciaFilialMercurio,bean);

        this.vigenciaService.validaVigenciaBeforeStore(ciaFilialMercurio);
        		
        super.store(ciaFilialMercurio);
        
        TypedFlatMap retorno = new TypedFlatMap();
        retorno.put("idCiaFilialMercurio",ciaFilialMercurio.getIdCiaFilialMercurio());
        Integer acaoVigenciaAtual = JTVigenciaUtils.getIntegerAcaoVigencia(ciaFilialMercurio);
        retorno.put("acaoVigenciaAtual",acaoVigenciaAtual);
        
        return retorno;
    }
    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setCiaFilialMercurioDAO(CiaFilialMercurioDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private CiaFilialMercurioDAO getCiaFilialMercurioDAO() {
        return (CiaFilialMercurioDAO) getDao();
    }
      
    protected CiaFilialMercurio beforeStore(CiaFilialMercurio bean) {
    	CiaFilialMercurio ciaFilialMercurio = (CiaFilialMercurio)super.beforeStore(bean);
    	
    	getFilialService().verificaVigenciasEmHistoricoFilial(ciaFilialMercurio.getFilial().getIdFilial(),
    														  ciaFilialMercurio.getDtVigenciaInicial(),
    														  ciaFilialMercurio.getDtVigenciaFinal());
    	
    	Empresa empresa = getEmpresaService().findById(ciaFilialMercurio.getEmpresa().getIdEmpresa());    	
    	if (!empresa.getTpSituacao().equals(new DomainValue("A")))
    		throw new BusinessException("LMS-29023");
    	
    	if (getCiaFilialMercurioDAO().verificaAssocFilialCiaAerea((CiaFilialMercurio)bean))
    		throw new BusinessException("LMS-00003");
    	
    	if (ciaFilialMercurio.getIdCiaFilialMercurio() != null) {
    		if (vigenciaService.validateIntegridadeVigencia(FilialMercurioFilialCia.class,"ciaFilialMercurio",ciaFilialMercurio))
    			throw new BusinessException("LMS-29122");
    	}
    	
    	return ciaFilialMercurio;
    }
    
    /**
     * Valida a remoção de um registro de acordo com o padrão de comportamento de vigências.
     * @param id Id do registro a ser validado.
     */
	private void validaRemoveById(Long id) {
		CiaFilialMercurio ciaFilialMercurio = findById(id);
        JTVigenciaUtils.validaVigenciaRemocao(ciaFilialMercurio);
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

    public List findCiaAerea(TypedFlatMap criteria){
    	if(criteria == null)
    		criteria = new TypedFlatMap();

    	criteria.put("tpEmpresa", Empresa.TP_EMPRESA_CIA_AEREA);
    	criteria.put("idFilial", SessionUtils.getFilialSessao().getIdFilial());
    	criteria.put("dtAtual", JTDateTimeUtils.getDataAtual());
    	
    	return getCiaFilialMercurioDAO().findCiaAerea(criteria);
    }
    
	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public EmpresaService getEmpresaService() {
		return empresaService;
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
    
    /**
     * Verifica se a Cia Filial Mercúrio está vigente no período de datas indicado.
     * autor Felipe Ferreira
     * @param bean
     * @return true se registro for vigente.
     */
    public boolean verificaSeCiaFilialMercurioVigente(Long idCiaFilialMercurio, YearMonthDay dtVigenciaInicial, YearMonthDay dtVigenciaFinal) {
    	return getCiaFilialMercurioDAO().verificaSeCiaFilialMercurioVigente(idCiaFilialMercurio,dtVigenciaInicial,dtVigenciaFinal);
    }
    
    /**
     * GT2
     * Atualizar NR_PRESTACAO_CONTA com NR_PRESTACAO_CONTA + 1
     * @param IdCiaFilialMercurio
     * @return Novo NR_PRESTACAO_CONTA
     */
    public Long storePrestacaoContas(Long idCiaFilialMercurio){
    	CiaFilialMercurio cia = findById(idCiaFilialMercurio);
    	Long nrPrestacaoContas = ( cia.getNrPrestacaoContas() == null) 
    				? Long.valueOf(1) 
    				: Long.valueOf(cia.getNrPrestacaoContas().longValue() + 1);
    	cia.setNrPrestacaoContas( nrPrestacaoContas );
    	store(cia);
    	return nrPrestacaoContas;
    }
    
    public Long findByIdFilialAndNrIdentificacaoBetweenDtVigencia(Long idFilial, String nrIdentificacao, YearMonthDay dtVigencia) {
		return getCiaFilialMercurioDAO().findByIdFilialAndNrIdentificacaoBetweenDtVigencia(idFilial, nrIdentificacao, dtVigencia);
	}

    public CiaFilialMercurio findByIdInitLazyProperties(Long idCiaFilialMercurio, boolean useLock) {
    	return getCiaFilialMercurioDAO().findById(idCiaFilialMercurio, useLock);
    }

    public void updateNrPrestacaoContas(Long idCiaFilialMercurio, Long nrPrestacaoContaP){
    	getCiaFilialMercurioDAO().updateNrPrestacaoContas(idCiaFilialMercurio,nrPrestacaoContaP);
    }
    
    public Long findNrCcTomadorServico(Awb awb, Long idCiaAerea){
    	return getCiaFilialMercurioDAO().findNrCcTomadorServico(awb, idCiaAerea);
    }
}