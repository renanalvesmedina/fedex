package com.mercurio.lms.vol.model.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.model.VolUptimeConexao;
import com.mercurio.lms.vol.model.dao.VolUptimeConexaoDAO;
import com.mercurio.lms.vol.utils.VolFomatterUtil;


/**
 * 
 * @author lucianos
 * 
 * Classe resposável por monitorar a conexão dos celulares
 * 		
 */
public class VolUptimeConexaoService extends CrudService<VolUptimeConexao, Long>{
	private UsuarioService usuarioService;
	private PaisService paisService;
	private VolDadosSessaoService volDadosSessaoService;
	private FilialService filialService;
	
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @param Instância do DAO.
	 */
	public void setVolUptimeConexaoDao(VolUptimeConexaoDAO volUptimeConexaoDao) {
		setDao( volUptimeConexaoDao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
	 *
	 * @return Instância do DAO.
	 */
	private VolUptimeConexaoDAO getVolUptimeConexaoDao() {
		return (VolUptimeConexaoDAO) getDao();
	}

	/**
	 * Recupera uma instância de <code>MonitoramentoDocEletronico</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public VolUptimeConexao findById(java.lang.Long id) {
		return (VolUptimeConexao)super.findById(id);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(VolUptimeConexao volUptimeConexao) {
		return super.store(volUptimeConexao);
	}
	
	/**
	 * Recebe um request do celular 
	 * 
	 */
	public void pingConexao(TypedFlatMap map){
		
	}
	
	/**
	 * Salva o resultado do ping
	 * @param map
	 */
	public void storeUptimeConexao(TypedFlatMap map){
		
    	/**
    	 * seta os dados na sessão
    	 */
		Filial filial = filialService.findById(map.getLong("idFilial"));
		Usuario usuario = usuarioService.findUsuarioByLogin("vol");
		Pais pais = paisService.findByIdPessoa( filial.getIdFilial() );
		volDadosSessaoService.setDadosSessaoBanco(usuario, filial, pais);
				
		VolUptimeConexao volUptimeConexao = new VolUptimeConexao();	
		DateTime dhInicioChamada =  VolFomatterUtil.formatStringToDateTime(map.getString("dhInicioChamada"));
		volUptimeConexao.setDhInicioChamada(dhInicioChamada);
				
		volUptimeConexao.setHttpCode(map.getLong("httpCode"));
		volUptimeConexao.setTempoExecucao(map.getLong("tempoExecucao"));
		
		VolEquipamentos volEquipamentos = new VolEquipamentos();
		volEquipamentos.setIdEquipamento(map.getLong("idEquipamento"));
		volUptimeConexao.setVolEquipamentos(volEquipamentos);
		
		volUptimeConexao.setFilial(filial);
		
		MeioTransporte meioTransporte = new MeioTransporte();
		meioTransporte.setIdMeioTransporte(map.getLong("idFrota"));
		volUptimeConexao.setMeioTransporte(meioTransporte);
		
		String latitude = map.getString("latitude");
		String longitude = map.getString("longitude");
		
		if(latitude != null && longitude != null && !"null".equalsIgnoreCase(latitude) && !"null".equalsIgnoreCase(longitude)){
			volUptimeConexao.setLatitude(map.getString("latitude"));
			volUptimeConexao.setLongitude(map.getString("longitude"));
		}
	
		this.store(volUptimeConexao);
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

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}


}
