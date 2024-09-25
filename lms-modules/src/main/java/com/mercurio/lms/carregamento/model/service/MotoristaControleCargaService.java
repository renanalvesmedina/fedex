package com.mercurio.lms.carregamento.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.LocalTroca;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.MotoristaControleCarga;
import com.mercurio.lms.carregamento.model.dao.MotoristaControleCargaDAO;
import com.mercurio.lms.contratacaoveiculos.model.LiberacaoReguladora;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.LiberacaoReguladoraService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.entrega.model.service.CancelarManifestoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PontoParadaService;
import com.mercurio.lms.municipios.model.service.RodoviaService;
import com.mercurio.lms.util.AliasToTypedFlatMapResultTransformer;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.carregamento.motoristaControleCargaService"
 */
public class MotoristaControleCargaService extends CrudService<MotoristaControleCarga, Long> {
	private CancelarManifestoService cancelarManifestoService;
	private ControleCargaService controleCargaService;
	private ControleTrechoService controleTrechoService;
	private LiberacaoReguladoraService liberacaoReguladoraService;
	private LocalTrocaService localTrocaService;
	private ManifestoService manifestoService;
	private MotoristaService motoristaService;
	private MunicipioService municipioService;
	private PontoParadaService pontoParadaService;
	private RodoviaService rodoviaService;

	
	public void setCancelarManifestoService(CancelarManifestoService cancelarManifestoService) {
		this.cancelarManifestoService = cancelarManifestoService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}
	public void setLiberacaoReguladoraService(LiberacaoReguladoraService liberacaoReguladoraService) {
		this.liberacaoReguladoraService = liberacaoReguladoraService;
	}
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setControleTrechoService(ControleTrechoService controleTrechoService) {
		this.controleTrechoService = controleTrechoService;
	}
	public void setLocalTrocaService(LocalTrocaService localTrocaService) {
		this.localTrocaService = localTrocaService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setPontoParadaService(PontoParadaService pontoParadaService) {
		this.pontoParadaService = pontoParadaService;
	}
	public void setRodoviaService(RodoviaService rodoviaService) {
		this.rodoviaService = rodoviaService;
	}
	
	/**
	 * Recupera uma instância de <code>MotoristaControleCarga</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MotoristaControleCarga findById(java.lang.Long id) {
        return (MotoristaControleCarga)super.findById(id);
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
    public java.io.Serializable store(MotoristaControleCarga bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotoristaControleCargaDAO(MotoristaControleCargaDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotoristaControleCargaDAO getMotoristaControleCargaDAO() {
        return (MotoristaControleCargaDAO) getDao();
    }

	/**
	 * 
	 * @param idControleCarga
	 * @param findDefinition
	 * @return
	 */
    public ResultSetPage findPaginatedByIdControleCarga(Long idControleCarga, FindDefinition findDefinition) {
    	ResultSetPage rsp = getMotoristaControleCargaDAO().findPaginatedByIdControleCarga(idControleCarga, findDefinition);
    	List result = new AliasToTypedFlatMapResultTransformer().transformListResult(rsp.getList());
    	rsp.setList(result);
    	return rsp;
    }

    /**
     * 
     * @param idControleCarga
     * @return
     */
    public Integer getRowCountFindPaginatedByIdControleCarga(Long idControleCarga) {
    	return getMotoristaControleCargaDAO().getRowCountFindPaginatedByIdControleCarga(idControleCarga);
    }

    /**
     * 
     * @param idControleCarga
     * @return
     */
	public MotoristaControleCarga findMotoristaCcByIdControleCarga(Long idControleCarga) {
		return getMotoristaControleCargaDAO().findMotoristaCcByIdControleCarga(idControleCarga);
	}

	/**
	 * 
	 * @param nrKmRodoviaTroca
	 * @param dsTroca
	 * @param dhTroca
	 * @param idMunicipio
	 * @param idControleTrecho
	 * @param idPontoParada
	 * @param idRodovia
	 * @param idControleCarga
	 * @param idMotorista
	 */
	@SuppressWarnings("rawtypes")
	public Map storeTrocarMotorista(
		Integer nrKmRodoviaTroca,
		String dsTroca,
		DateTime dhTroca,
		Long idMunicipio,
		Long idControleTrecho,
		Long idPontoParada,
		Long idRodovia,
		Long idControleCarga,
		Long idMotorista
	) {
		Map retorno = new HashMap();
		
		Filial filialUsuario = SessionUtils.getFilialSessao();
		
    	List listaManifestosEmitidos = manifestoService.
    			findManifestoByIdControleCarga(idControleCarga, filialUsuario.getIdFilial(), "ME", "V");

    	while (!listaManifestosEmitidos.isEmpty()) {
    		Manifesto manifesto = (Manifesto)listaManifestosEmitidos.get(0);
    		cancelarManifestoService.executeCancelarManifestoViagem(manifesto.getIdManifesto(), Boolean.TRUE, Boolean.FALSE);
    		listaManifestosEmitidos = manifestoService.findManifestoByIdControleCarga(idControleCarga, filialUsuario.getIdFilial(), "ME", "V");
    	}

    	if (controleCargaService.validateExisteEventoEmitido(idControleCarga)) {
    		//LMS-3544
			retorno = controleCargaService.generateCancelamentoEmissaoControleCargaMdfe(idControleCarga);
		}

		LocalTroca localTroca = new LocalTroca();
		localTroca.setDsTroca(dsTroca);
		localTroca.setNrKmRodoviaTroca(nrKmRodoviaTroca);
		localTroca.setMunicipio(municipioService.findById(idMunicipio));
		if (idControleTrecho != null) {
			localTroca.setControleTrecho(controleTrechoService.findById(idControleTrecho));
		}
		if (idPontoParada != null) {
			localTroca.setPontoParada(pontoParadaService.findById(idPontoParada));
		}
		if (idRodovia != null) {
			localTroca.setRodovia(rodoviaService.findById(idRodovia));
		}
		localTroca.setIdLocalTroca((Long) localTrocaService.store(localTroca));

		MotoristaControleCarga motoristaCc = findMotoristaCcByIdControleCarga(idControleCarga);
		motoristaCc.setDhTroca(dhTroca);
		motoristaCc.setUsuarioByIdFuncAlteraStatus(SessionUtils.getUsuarioLogado());
		motoristaCc.setLocalTroca(localTroca);
		store(motoristaCc);

		ControleCarga controleCarga = controleCargaService.findByIdInitLazyProperties(idControleCarga, false);
		Motorista motorista = motoristaService.findById(idMotorista);

		LiberacaoReguladora liberacaoReguladora = liberacaoReguladoraService.findLiberacaoReguladoraMotorista(idMotorista, controleCarga.getTpControleCarga().getValue());
		if (controleCarga.getTpControleCarga().getValue().equals("V") && motorista.getTpVinculo().getValue().equals("E")) {
			liberacaoReguladora.setDtVencimento(JTDateTimeUtils.getDataAtual());
			liberacaoReguladoraService.store(liberacaoReguladora);
		}

		MotoristaControleCarga motoristaCcNovo = new MotoristaControleCarga();
		motoristaCcNovo.setControleCarga(controleCarga);
		motoristaCcNovo.setMotorista(motorista);
		motoristaCcNovo.setLocalTroca(null);
		motoristaCcNovo.setDhTroca(null);
		motoristaCcNovo.setLiberacaoReguladora(liberacaoReguladora);
		store(motoristaCcNovo);

		controleCarga.setMotorista(motorista);
		controleCargaService.store(controleCarga);
		
		return retorno;
	}
}