package com.mercurio.lms.sgr.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sgr.model.SolicitacaoSinal;
import com.mercurio.lms.sgr.model.dao.SolicitacaoSinalDAO;
import com.mercurio.lms.util.AliasToNestedMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.solicitacaoSinalService"
 */
public class SolicitacaoSinalService extends CrudService<SolicitacaoSinal, Long> {
	
	private ConfiguracoesFacade configuracoesFacade;
	private MeioTransporteService meioTransporteService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private ControleCargaService controleCargaService;
	private ProprietarioService proprietarioService;
	private FilialService filialService;
	private MotoristaService motoristaService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public ControleCargaService getControleCargaService() {
		return controleCargaService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public FilialService getFilialService() {
		return filialService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public MeioTransporteRodoviarioService getMeioTransporteRodoviarioService() {
		return meioTransporteRodoviarioService;
	}
	public void setMeioTransporteRodoviarioService(MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}
	public MeioTranspProprietarioService getMeioTranspProprietarioService() {
		return meioTranspProprietarioService;
	}
	public void setMeioTranspProprietarioService(MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}
	public MotoristaService getMotoristaService() {
		return motoristaService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}
	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	
	/**
	 * Recupera uma instância de <code>SolicitacaoSinal</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public SolicitacaoSinal findById(java.lang.Long id) {
        return (SolicitacaoSinal)super.findById(id);
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
    public java.io.Serializable store(SolicitacaoSinal bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setSolicitacaoSinalDAO(SolicitacaoSinalDAO dao) {
        setDao( dao );
    }
    
    /**
     * Faz a consulta ao banco, retornando o numero de registros encontrados para determinados 
     * parametros.
     * 
     * @param criteria
     * @return
     */
    public Integer getRowCountBySolicitacaoSinal(TypedFlatMap criteria) {
    	Integer rowCount = this.getSolicitacaoSinalDAO().getRowCountBySolicitacaoSinal(criteria);
    	return rowCount;
    }
    /**
     * Retorna um map com os objetos a serem mostrados na grid. 
     * 
     * @param criteria
     * @return
     */
    public ResultSetPage findPaginatedBySolicitacaoSinal(TypedFlatMap criteria) {
    	ResultSetPage rsp = this.getSolicitacaoSinalDAO().findPaginatedBySolicitacaoSinal(criteria, FindDefinition.createFindDefinition(criteria));
    	rsp.setList(AliasToNestedMapResultTransformer.getInstance().transformListResult(rsp.getList()));
    	return rsp;
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private SolicitacaoSinalDAO getSolicitacaoSinalDAO() {
        return (SolicitacaoSinalDAO) getDao();
    }
    
    /**
     * Método que gera uma Solicitação Sinal
     * 
     * @param idFilial
     * @param idControleCarga
     * @param idMeioTransporte
     * @param idSemiReboque
     * @param idMotorista
     * @param nrTelefoneEmpresa
     * @param nmEmpresaAnterior
     * @param nmResponsavelEmpresa
     * @param blPertenceProjCaminhoneiro
     * @param obSolicitacaoSinal
     */
    public SolicitacaoSinal generateEnviarSolicitacaoSinal(Long idFilial, Long idControleCarga, Long idMeioTransporte, Long idSemiReboque,
	    												   Long idMotorista, String nrTelefoneEmpresa, String nmEmpresaAnterior, 
	    												   String nmResponsavelEmpresa, Boolean blPertenceProjCaminhoneiro, 
	    												   String obSolicitacaoSinal) {
    	
    	SolicitacaoSinal solicitacaoSinal = new SolicitacaoSinal();
    	
    	// Busca Meio de Transporte a partir do ID
    	MeioTransporte meioTransporte = this.getMeioTransporteService().findByIdInitLazyProperties(idMeioTransporte, false);
    	if(meioTransporte.getTpVinculo().getValue().equals("A") || meioTransporte.getTpVinculo().getValue().equals("E")) {
    		
			// Busca um map de MeioTranspProprietario para pegar o ID do proprietario
			Map mapMeioTranspProprietario = this.getMeioTranspProprietarioService().findProprietarioByMeioTransporte(idMeioTransporte);
			Long idProprietario = (Long) mapMeioTranspProprietario.get("idPessoa");
			
			// Busca Meio Transporte Rodoviario pelo ID do meio Transporte
			MeioTransporteRodoviario meioTransporteRodoviario = this.getMeioTransporteRodoviarioService().findMeioTransporteRodoviarioByIdMeioTransporte(idMeioTransporte);
			String dsFrotaIdentificador = meioTransporteRodoviario.
				getMeioTransporte().getNrFrota() + " - " + meioTransporteRodoviario.getMeioTransporte().getNrIdentificador();
		
			if (meioTransporteRodoviario.getNrRastreador() == null) {
				throw new BusinessException("LMS-05092", new Object[] {dsFrotaIdentificador});
			}
			if (meioTransporteRodoviario.getOperadoraMct() == null) {
				throw new BusinessException("LMS-05093", new Object[] {dsFrotaIdentificador});
			}
			
			// Busca Filial
			Filial filial = this.getFilialService().findById(idFilial);
			
			// Gera um objeto de Solicitacao Sinal para gravar no banco				
			solicitacaoSinal.setDhGeracao(JTDateTimeUtils.getDataHoraAtual());

			// Busca o ultimo numero de Solicitacao Sinal no banco
	        Long maiorNroColeta = configuracoesFacade.incrementaParametroSequencial(idFilial, "NR_SOLICITACAO_SINAL", true);
			solicitacaoSinal.setNrSolicitacaoSinal(Integer.valueOf(maiorNroColeta.intValue()));

			solicitacaoSinal.setNrRastreador(meioTransporteRodoviario.getNrRastreador());
			solicitacaoSinal.setNrTelefoneEmpresa(nrTelefoneEmpresa);
			solicitacaoSinal.setNmEmpresaAnterior(nmEmpresaAnterior);
			solicitacaoSinal.setNmResponsavelEmpresa(nmResponsavelEmpresa);
			solicitacaoSinal.setBlPertProjCaminhoneiro(blPertenceProjCaminhoneiro);
			solicitacaoSinal.setObSolicitacaoSinal(obSolicitacaoSinal);
			solicitacaoSinal.setControleCarga(this.getControleCargaService().findByIdInitLazyProperties(idControleCarga, false));
			solicitacaoSinal.setMeioTransporte(meioTransporte);
			if(idSemiReboque != null) {
				solicitacaoSinal.setMeioTransporteByIdSemiReboque(this.getMeioTransporteService().findByIdInitLazyProperties(idSemiReboque, false));
			}
			solicitacaoSinal.setOperadoraMct(meioTransporteRodoviario.getOperadoraMct());
			solicitacaoSinal.setMotorista(this.getMotoristaService().findById(idMotorista));
			solicitacaoSinal.setProprietario(this.getProprietarioService().findById(idProprietario));
			solicitacaoSinal.setFilial(filial);
			solicitacaoSinal.setTpStatusSolicitacao(new DomainValue("GE"));
			
			// Salva o bean de Solicitacao Sinal
			this.store(solicitacaoSinal);
			getSolicitacaoSinalDAO().getAdsmHibernateTemplate().flush();
    	}
    	return solicitacaoSinal;
    	
    }
    
	/**
	 * Alterar o campo TP_STATUS_SOLICITACAO para "CA" (Cancelado) na tabela SOLICITACAO_SINAL para o Controle de Carga 
     * em questão.
     * 
	 * @param idControleCarga
	 */
	public void storeCancelarSolicitacaoSinalByIdControleCarga(Long idControleCarga) {
		getSolicitacaoSinalDAO().storeCancelarSolicitacaoSinalByIdControleCarga(idControleCarga);
	}
	
}