package com.mercurio.lms.sgr.model.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.FilterResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.seguros.model.ApoliceSeguro;
import com.mercurio.lms.seguros.model.service.ApoliceSeguroService;
import com.mercurio.lms.sgr.model.ClienteEnquadramento;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.FaixaDeValor;
import com.mercurio.lms.sgr.model.dao.EnquadramentoRegraDAO;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.SeguroCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.SeguroClienteService;
import com.mercurio.lms.vendas.util.ClienteUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.sgr.enquadramentoRegraService"
 */
public class EnquadramentoRegraService extends CrudService<EnquadramentoRegra, Long> {

	private MunicipioEnquadramentoService municipioEnquadramentoService;
	private PaisEnquadramentoService paisEnquadramentoService;	
	private UnidadeFederativaEnquadramentoService unidadeFederativaEnquadramentoService;	
	private FilialEnquadramentoService filialEnquadramentoService;	
	private ClienteEnquadramentoService clienteEnquadramentoService;
	private FaixaDeValorService faixaDeValorService;
	private ClienteService clienteService;

	// LMS-7285
	private ApoliceSeguroService apoliceSeguroService;
	private SeguroClienteService seguroClienteService;

	/**
	 * @return Returns the municipioEnquadramentoService.
	 */    
	public MunicipioEnquadramentoService getMunicipioEnquadramentoService() {
		return municipioEnquadramentoService;
	}
	/**
	 * @param municipioEnquadramentoService The dominioAgrupamentoService to set.
	 */
	public void setMunicipioEnquadramentoService(MunicipioEnquadramentoService municipioEnquadramentoService) {
		this.municipioEnquadramentoService = municipioEnquadramentoService;
	}
	/**
	 * @return Returns the paisEnquadramentoService.
	 */    
	public PaisEnquadramentoService getPaisEnquadramentoService() {
		return paisEnquadramentoService;
	}
	/**
	 * @param paisEnquadramentoService The paisEnquadramentoService to set.
	 */
	public void setPaisEnquadramentoService(PaisEnquadramentoService paisEnquadramentoService) {
		this.paisEnquadramentoService = paisEnquadramentoService;
	}
	/**
	 * @return Returns the unidadeFederativaEnquadramentoService.
	 */    
	public UnidadeFederativaEnquadramentoService getUnidadeFederativaEnquadramentoService() {
		return unidadeFederativaEnquadramentoService;
	}
	/**
	 * @param unidadeFederativaEnquadramentoService The unidadeFederativaEnquadramentoService to set.
	 */
	public void setUnidadeFederativaEnquadramentoService(UnidadeFederativaEnquadramentoService unidadeFederativaEnquadramentoService) {
		this.unidadeFederativaEnquadramentoService = unidadeFederativaEnquadramentoService;
	}
	/**
	 * @return Returns the filialEnquadramentoService.
	 */    
	public FilialEnquadramentoService getFilialEnquadramentoService() {
		return filialEnquadramentoService;
	}
	/**
	 * @param filialEnquadramentoService The filialEnquadramentoService to set.
	 */
	public void setFilialEnquadramentoService(FilialEnquadramentoService filialEnquadramentoService) {
		this.filialEnquadramentoService = filialEnquadramentoService;
	}
	/**
	 * @return Returns the clienteEnquadramentoService.
	 */    
	public ClienteEnquadramentoService getClienteEnquadramentoService() {
		return clienteEnquadramentoService;
	}
	/**
	 * @param clienteEnquadramentoService The clienteEnquadramentoService to set.
	 */
	public void setClienteEnquadramentoService(ClienteEnquadramentoService clienteEnquadramentoService) {
		this.clienteEnquadramentoService = clienteEnquadramentoService;
	}
	public FaixaDeValorService getFaixaDeValorService() {
		return faixaDeValorService;
	}
	public void setFaixaDeValorService(FaixaDeValorService faixaDeValorService) {
		this.faixaDeValorService = faixaDeValorService;
	}
	public ClienteService getClienteService() {
		return clienteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	/**
	 * Recupera uma instância de <code>EnquadramentoRegra</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public EnquadramentoRegra findById(java.lang.Long id) {
    	EnquadramentoRegra enquadramentoRegra = (EnquadramentoRegra)super.findById(id);
    	enquadramentoRegra.setMunicipios(getMunicipioEnquadramentoService().findMuncipiosOrigemById(id), getMunicipioEnquadramentoService().findMuncipiosDestinoById(id));
    	enquadramentoRegra.setPaises(getPaisEnquadramentoService().findPaisesOrigemById(id), getPaisEnquadramentoService().findPaisesDestinoById(id));
    	enquadramentoRegra.setUnidadesFederativa(getUnidadeFederativaEnquadramentoService().findUnidadesFederativaOrigemById(id), getUnidadeFederativaEnquadramentoService().findUnidadesFederativaDestinoById(id));
    	enquadramentoRegra.setFiliais(getFilialEnquadramentoService().findFiliaisOrigemById(id), getFilialEnquadramentoService().findFiliaisDestinoById(id));    	
    	enquadramentoRegra.setClienteEnquadramentos(getClienteEnquadramentoService().findClientesById(id));
   	    return enquadramentoRegra;
    }

    
	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
  		getMunicipioEnquadramentoService().removeByIdEnquadramentoRegra(id);
  		getPaisEnquadramentoService().removeByIdEnquadramentoRegra(id);
  		getUnidadeFederativaEnquadramentoService().removeByIdEnquadramentoRegra(id);
  		getFilialEnquadramentoService().removeByIdEnquadramentoRegra(id);
  		getClienteEnquadramentoService().removeByIdEnquadramentoRegra(id);
  		getFaixaDeValorService().removeByIdEnquadramentoRegra(id);
  		getEnquadramentoRegraDAO().removeById(id, true);
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
    	for(Iterator it=ids.iterator();it.hasNext();){
    		Long idEnquadramentoRegra = (Long)it.next();
      		getMunicipioEnquadramentoService().removeByIdEnquadramentoRegra(idEnquadramentoRegra);    		
      		getPaisEnquadramentoService().removeByIdEnquadramentoRegra(idEnquadramentoRegra);
      		getUnidadeFederativaEnquadramentoService().removeByIdEnquadramentoRegra(idEnquadramentoRegra);
      		getFilialEnquadramentoService().removeByIdEnquadramentoRegra(idEnquadramentoRegra);
      		getClienteEnquadramentoService().removeByIdEnquadramentoRegra(idEnquadramentoRegra);
      		getFaixaDeValorService().removeByIdEnquadramentoRegra(idEnquadramentoRegra);    	
    	}
        super.removeByIds(ids);
    }

    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(EnquadramentoRegra bean) {
        return super.store(bean);
    }
    
	public List findLookupCliente(Map criteria) {
		List resultado = clienteService.findLookup(criteria);
		return resultado;
	}    
    
	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable storeByManterEnquadramentoRegrasSGR(EnquadramentoRegra bean) {
    	YearMonthDay dtAtual = JTDateTimeUtils.getDataAtual();

		// LMS-7285
    	validaSeguroCliente(bean.getSeguroCliente(), bean.getClienteEnquadramentos());

    	if (bean.getIdEnquadramentoRegra() == null) {
	    	if (bean.getDtVigenciaInicial() != null && JTDateTimeUtils.comparaData(bean.getDtVigenciaInicial(), dtAtual) < 0) {
	    			throw new BusinessException("LMS-11301");
	    	}
			if (bean.getDtVigenciaFinal() != null && JTDateTimeUtils.comparaData(bean.getDtVigenciaFinal(), dtAtual) < 0) {
				throw new BusinessException("LMS-00007");
			}
    	}
  		Serializable retorno = store(bean);
  		getMunicipioEnquadramentoService().storeMunicipios(bean.getMunicipioEnquadramentosOrigem(), bean.getMunicipioEnquadramentosDestino(), bean);
  		getPaisEnquadramentoService().storePaises(bean.getPaisEnquadramentosOrigem(), bean.getPaisEnquadramentosDestino(), bean);
  		getUnidadeFederativaEnquadramentoService().storeUnidadeFederativa(bean.getUnidadeFederativaEnquadramentosOrigem(), bean.getUnidadeFederativaEnquadramentosDestino(), bean);
  		getFilialEnquadramentoService().storeFiliais(bean.getFilialEnquadramentosOrigem(), bean.getFilialEnquadramentosDestino(), bean);
 		getClienteEnquadramentoService().storeClientes(bean.getClienteEnquadramentos(), bean);

		// LMS-7285 - atualiza valor limite para controle de carga
		ApoliceSeguro apoliceSeguro = bean.getApoliceSeguro();
		if (apoliceSeguro != null) {
			apoliceSeguroService.storeVlLimiteControleCarga(apoliceSeguro.getIdApoliceSeguro(), apoliceSeguro.getVlLimiteControleCarga());
		}
		SeguroCliente seguroCliente = bean.getSeguroCliente();
		if (seguroCliente != null) {
			seguroClienteService.storeVlLimiteControleCarga(seguroCliente.getIdSeguroCliente(), seguroCliente.getVlLimiteControleCarga());
		}
		return retorno;
    }

    
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setEnquadramentoRegraDAO(EnquadramentoRegraDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private EnquadramentoRegraDAO getEnquadramentoRegraDAO() {
        return (EnquadramentoRegraDAO) getDao();
    }

    
    public ResultSetPage findPaginated(TypedFlatMap criteria) {
    	ResultSetPage rsp = getEnquadramentoRegraDAO().findPaginated(
    			criteria, FindDefinition.createFindDefinition(criteria));

		FilterResultSetPage filterRs = new FilterResultSetPage(rsp) {
			public Map filterItem(Object item) {
				EnquadramentoRegra er = (EnquadramentoRegra)item; 
				TypedFlatMap typedFlatMap = new TypedFlatMap();
		    	typedFlatMap.put("idEnquadramentoRegra", er.getIdEnquadramentoRegra());
		    	typedFlatMap.put("dsEnquadramentoRegra", er.getDsEnquadramentoRegra());
		    	typedFlatMap.put("dtVigencia", JTFormatUtils.format(er.getDtVigenciaInicial(),JTFormatUtils.MEDIUM)  + (er.getDtVigenciaFinal()!=null? (" - " + JTFormatUtils.format(er.getDtVigenciaFinal(), JTFormatUtils.MEDIUM)):""));
				typedFlatMap.put("tpModal", er.getTpModal());
				typedFlatMap.put("tpAbrangencia", er.getTpAbrangencia());
				typedFlatMap.put("tpOperacao", er.getTpOperacao());
				typedFlatMap.put("moeda.idMoeda", er.getMoeda().getIdMoeda());
				typedFlatMap.put("moeda.dsSimbolo", er.getMoeda().getDsSimbolo());
				typedFlatMap.put("moeda.sgMoeda", er.getMoeda().getSgMoeda());
				if (er.getFaixaDeValors()!= null && er.getFaixaDeValors().size() > 0){
				FaixaDeValor faixa = (FaixaDeValor) er.getFaixaDeValors().get(0);
				typedFlatMap.put("blRequerLiberacaoCemop", faixa.getBlRequerLiberacaoCemop());
				typedFlatMap.put("vlLimiteMinimo", faixa.getVlLimiteMinimo());
				typedFlatMap.put("vlLimiteMaximo", faixa.getVlLimiteMaximo());
				typedFlatMap.put("naturezaProduto.dsNaturezaProduto", er.getNaturezaProduto()!=null?er.getNaturezaProduto().getDsNaturezaProduto():null);
				}
				// LMS-7253
				typedFlatMap.put("blRegraGeral", er.getBlRegraGeral());
				
				return typedFlatMap; 
			}
		};
    	return (ResultSetPage)filterRs.doFilter();
    }


	public Integer getRowCount(TypedFlatMap criteria) {
		return getEnquadramentoRegraDAO().getRowCount(criteria);
	}
	
	
	public List findByExigenciasGerRisco(String tpOperacao) {
		return getEnquadramentoRegraDAO().findByExigenciasGerRisco(tpOperacao, JTDateTimeUtils.getDataAtual());
	}

	public ApoliceSeguroService getApoliceSeguroService() {
		return apoliceSeguroService;
	}

	public void setApoliceSeguroService(ApoliceSeguroService apoliceSeguroService) {
		this.apoliceSeguroService = apoliceSeguroService;
	}

	public SeguroClienteService getSeguroClienteService() {
		return seguroClienteService;
	}

	public void setSeguroClienteService(SeguroClienteService seguroClienteService) {
		this.seguroClienteService = seguroClienteService;
	}

	/**
	 * LMS-7285 - Valida {@link SeguroCliente} para conjunto de
	 * {@link ClienteEnquadramento} verificando número de inscrição dos CNPJ.
	 * Lança uma {@link BusinessException} com a mensagem LMS-11339 se o CNPJ de
	 * qualquer {@link Cliente} relacionado ao PGR por
	 * {@link ClienteEnquadramento} não possuir o mesmo prefixo (número de
	 * inscrição) do cliente relacionado ao {@link SeguroCliente}.
	 * 
	 * @param seguroCliente
	 * @param clienteEnquadramentos
	 * 
	 * @see ClienteUtils#equalsInscricaoCNPJ(String, String)
	 */
    private void validaSeguroCliente(SeguroCliente seguroCliente, List<ClienteEnquadramento> clienteEnquadramentos) {
    	if (seguroCliente == null || clienteEnquadramentos == null || clienteEnquadramentos.isEmpty()) {
    		return;
    	}
    	String nrIdentificacaoSeguroCliente = getEnquadramentoRegraDAO().findNrIdentificacao(seguroCliente);
    	for (String nrIdentificacaoClienteEnquadramento : getEnquadramentoRegraDAO().findNrIdentificacao(clienteEnquadramentos)) {
    		if (!ClienteUtils.equalsInscricaoCNPJ(nrIdentificacaoSeguroCliente, nrIdentificacaoClienteEnquadramento)) {
   				throw new BusinessException("LMS-11339");
    		}
    	}
	}

}
