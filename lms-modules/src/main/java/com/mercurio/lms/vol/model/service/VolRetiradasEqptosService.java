package com.mercurio.lms.vol.model.service;


import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.model.VolRetiradasEqptos;
import com.mercurio.lms.vol.model.dao.VolRetiradasEqptosDAO;
import com.mercurio.lms.vol.utils.VolFomatterUtil;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volRetiradasEqptosService"
 */
public class VolRetiradasEqptosService extends CrudService<VolRetiradasEqptos, Long> {
	private FilialService filialService;
	private VolEquipamentosService volEquipamentosService;
	private MeioTransporteService meioTransporteService;
	private MotoristaService motoristaService;
	private UsuarioService usuarioService;
	private PaisService paisService;
	private VolDadosSessaoService volDadosSessaoService;


	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
	 * Recupera uma instância de <code>VolRetiradasEqptos</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public VolRetiradasEqptos findById(java.lang.Long id) {
        return (VolRetiradasEqptos)super.findById(id);
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
    public java.io.Serializable store(VolRetiradasEqptos bean) {
        return super.store(bean);
    }
    
    /**
     * verifica se o funcionário/terceiro tem alguma pendencia de devolução de equipamento regra 3.3
     * @param id
     * @param tipoUsuario
     */
    public void findPendenciaDevolucao(Long id, String tipoUsuario){
    	List retiradaEquipamento = this.getVolRetiradasEqptosDAO().findPendenciaDevolucao(id, tipoUsuario);
    	if (!retiradaEquipamento.isEmpty()){ 
    		throw new BusinessException("LMS-41043"); 
    	}
    	
    }
    
    /**
     * Metodo utilizado pelo celular responsavel por delegar a retirada e devolução de equipamentos
     * @param map
     */
    public void executeControleEquipamento( TypedFlatMap map ){
    	/**
    	 * seta os dados na sessão
    	 */
		Filial filial = filialService.findById(map.getLong("idFilial"));
		Usuario usuario = usuarioService.findUsuarioByLogin("vol");
		Pais pais = paisService.findByIdPessoa( filial.getIdFilial() );
		volDadosSessaoService.setDadosSessaoBanco(usuario, filial, pais);
    	
    	/* retorna o equipamento de acordo com o IMEI enviado pelo celular	 */
    	VolEquipamentos volEquipamentos = getVolEquipamentosService().findEquipamentoByImei( map.getString("imei"));
    	executeDevolucaoEquipamento(volEquipamentos.getIdEquipamento());


    	
    	DateTime dhRetirada;
		if(map.getString("dhRetirada") != null){
			dhRetirada = VolFomatterUtil.formatStringToDateTime(map.getString("dhRetirada"));
		}else{
			dhRetirada = JTDateTimeUtils.getDataHoraAtual();
    }
    
    	executeRetiradaEquipamento(volEquipamentos, map.getLong("idMotorista"), map.getLong("idFrota"), dhRetirada );
    }
    
    
    /**
	 * Utilizado pelo celular, no login, para fazer a devolução automatica do aparelho.
	 * OBS.: a devolução do aparelho é sempre realizada quando o usuário loga no aparelho.
	 * @param idEquipamento
	 */
	private void executeDevolucaoEquipamento(Long idEquipamento){
		List<VolRetiradasEqptos> retiradaEquipamentoList = getVolRetiradasEqptosDAO().findRetiradaEquipamentoByIdEquipamento( idEquipamento );
		
		if( !retiradaEquipamentoList.isEmpty() ){
			
			for (VolRetiradasEqptos volRetiradasEqptos : retiradaEquipamentoList) {
				volRetiradasEqptos.setDhDevolucao(JTDateTimeUtils.getDataHoraAtual());
				this.store(volRetiradasEqptos);
			}
		}
	}
    
       

    /**
     * Utilizado pelo celular para fazer a retirado do equipamento quando é feito o login
     * @param volEquipamentos
     * @param idUsuario
     * @param idFrota
     */
    private void executeRetiradaEquipamento(VolEquipamentos volEquipamentos, Long idMotorista, Long idFrota, DateTime dhRetirada ){
   
		/**
		 * caso a frota cadastrada para o equipamento seja diferente da frota que está logando 
		 * no aparelho, então desvincula o aparelho da frota antiga e vincula com a frota atual
		 */
    	
    	if((volEquipamentos.getMeioTransporte() == null) || (volEquipamentos.getMeioTransporte().getIdMeioTransporte().longValue() != idFrota )) {
    		MeioTransporte meioTransporte = getMeioTransporteService().findByIdInitLazyProperties(idFrota, false);
    		atualizaRelacionamentoEquipamentoMeioTransporte(volEquipamentos, meioTransporte); 
    	}
    	   	
		VolRetiradasEqptos volRetiradasEqptos = new VolRetiradasEqptos();     	
    	volRetiradasEqptos.setVolEquipamento(volEquipamentos);
		volRetiradasEqptos.setMeioTransporte(volEquipamentos.getMeioTransporte());
		volRetiradasEqptos.setDhRetirada(dhRetirada);
		
    	Motorista motorista = getMotoristaService().findById(idMotorista);
		volRetiradasEqptos.setMotorista(motorista);	
		
		this.store( volRetiradasEqptos );
    }
    
    
	/**
	 * Relaciona o equipamento com a frota, caso o equipamento 
	 * tenha sido retirado por outra frota
	 * @param volEquipamentos
	 * @param meioTransporte
	 */
	private void atualizaRelacionamentoEquipamentoMeioTransporte(VolEquipamentos volEquipamentos, MeioTransporte meioTransporte){
		List<Map> equipamentos = volEquipamentosService.findEquipamentoByIdMeioTransporte(meioTransporte.getIdMeioTransporte(), volEquipamentos.getIdEquipamento());
		
		if (equipamentos.size() > 0) {
			for(Map equip: equipamentos) {
				VolEquipamentos equipamento = volEquipamentosService.findById(Long.valueOf(equip.get("idEquipamento").toString()));
				equipamento.setMeioTransporte(null);
				volEquipamentosService.store(equipamento);
			}
		}
		
		volEquipamentos.setMeioTransporte(meioTransporte);
		getVolEquipamentosService().store(volEquipamentos);
	}

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setVolRetiradasEqptosDAO(VolRetiradasEqptosDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private VolRetiradasEqptosDAO getVolRetiradasEqptosDAO() {
        return (VolRetiradasEqptosDAO) getDao();
    }
    
    public ResultSetPage findPaginatedControleEquipamentos(TypedFlatMap criteria) {
		ResultSetPage rsp = getVolRetiradasEqptosDAO().findPaginatedControleEquipamentos(criteria,FindDefinition.createFindDefinition(criteria)); 
		return rsp;
	}
    
    public Integer getRowCountControleEquipamentos(TypedFlatMap criteria) {
		return getVolRetiradasEqptosDAO().getRowCountControleEquipamentos(criteria);
	}
    
    			
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void storeDevolucao(List ids) {
		
		VolRetiradasEqptos volRetiradasEqptos = null;
		
		for (int i = 0; i < ids.size();i++) {
			Long id = (Long)ids.get(i);
			if (id != null) {
				volRetiradasEqptos = findById(id);
				if (volRetiradasEqptos.getDhDevolucao() != null ) { throw new BusinessException("LMS-41005"); }
				volRetiradasEqptos.setDhDevolucao(JTDateTimeUtils.getDataHoraAtual());
				this.store(volRetiradasEqptos);
			}
		}
	}
	
	
	

	public void setVolEquipamentosService(
			VolEquipamentosService volEquipamentosService) {
		this.volEquipamentosService = volEquipamentosService;
	}

	public VolEquipamentosService getVolEquipamentosService() {
		return volEquipamentosService;
	}

	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}

	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public MotoristaService getMotoristaService() {
		return motoristaService;
	}

	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}
	
	
   }
