package com.mercurio.lms.vol.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.dialect.JDataStoreDialect;
import org.joda.time.DateTime;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.vol.model.VolEquipamentos;
import com.mercurio.lms.vol.utils.VolFomatterUtil;
import com.mercurio.lms.vol.utils.VolValidaDataUtil;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.vol.volSenhaFrotaService"
 */
public class VolSenhaFrotaService {
	private ControleCargaService controleCargaService;
	private VolEquipamentosService volEquipamentosService;
	private FilialService filialService;
	private ParametroGeralService parametroGeralService;
	private UsuarioService usuarioService;
	private PaisService paisService;
	private VolDadosSessaoService volDadosSessaoService;
	
	/**
	 * @author Marcelo Adamatti
	 * @since 25/07/06
	 * Executa processo da ET 41.02.02.07
	 * @param param Mapa contendo Número do Controle de Carga, senha, Número da Filial e versão do programa móvel
	 * @return Retorna mapa com a sigla da filial encontrada, versão atual do sistema, id do equipamento e identificador da frota
	 */
	public Map executeLogin(TypedFlatMap map){
		Map retorno = new HashMap();
		//CAPTURA OS VALORES
		String cdFilial = map.getString("filial");
		Long senha = map.getLong("senha");
		String cCarga = map.getString("controleCarga");
		String versaoProgMovel = map.getString("versao");
                String versaoSistemaOperacional = map.getString("versaoSO");
		String dateTimeCelularString = map.getString("dataHoraCelular");
		Boolean volOkta = map.getBoolean("volOkta");
		
		//PREENCHE O RETORNO PADRÃO
		retorno.put("SGFILIAL","NUL");
		retorno.put("IDFILIAL", "NUL");
		retorno.put("VERSAOATUAL","NUL");
		retorno.put("EQUIPAMENTO","NUL");
		retorno.put("IDFROTA","NUL");
		retorno.put("CONTROLECARGA","NUL");
		retorno.put("IMEI","NUL");		
		retorno.put("FROTA","NUL");	
		retorno.put("DATAHORASERVIDOR","NUL");
		
		Filial oFilial = filialService.findFilialByCodigoFilial(Integer.valueOf(cdFilial));
		if (oFilial != null) {
		
			Usuario usuario = getUsuarioService().findUsuarioByLogin("vol");
			Pais pais = getPaisService().findByIdPessoa( oFilial.getIdFilial() );
			volDadosSessaoService.setDadosSessaoBanco(usuario, oFilial, pais);
		
			/**
			 *  verifica se a diferença máxima, em minutos, entre a data/hora do celular e do servidor é permitida
			 *  se a data/hora do celular não está OK então envia a data/hora do servidor para o celular
			 **/
			if( dateTimeCelularString != null){
		
				ParametroGeral toleranciaDataHora = parametroGeralService.findByNomeParametro("VOL_TOLERANCIA_HORARIO", false);
				DateTime dateTimeServidor = JTDateTimeUtils.getDataHoraAtual();
				DateTime dataTimeCelular = VolFomatterUtil.formatStringToDateTime(dateTimeCelularString);
				if(  !VolValidaDataUtil.isDateTimeCelularOk(dataTimeCelular, dateTimeServidor, Long.parseLong(toleranciaDataHora.getDsConteudo())) ){
					retorno.put("DATAHORASERVIDOR", JTFormatUtils.format(dateTimeServidor, "ddMMyyyyHHmm"));
				}
			}
		
			/**
			 *  intervalo de tempo, em minutos, para a execução do uptimeConexao 
			 **/
			ParametroGeral timerParametroGeral  = parametroGeralService.findByNomeParametro("VOL_TIMER", false);
			Integer timer = VolFomatterUtil.formatTimerInSeconds(timerParametroGeral.getDsConteudo());			
			retorno.put("TIMER", timer);
		
			/* Filial */
			retorno.put("SGFILIAL",oFilial.getSgFilial());
			retorno.put("IDFILIAL", oFilial.getIdFilial());

			ParametroGeral oVersao = null;
			
			if (volOkta != null && volOkta) {
				oVersao = parametroGeralService.findByNomeParametro("VOL_OKTA_VERSAO_CELULAR",false);	
			} else {
				oVersao = parametroGeralService.findByNomeParametro("VOL_VERSAO_CELULAR",false);	
			}
			
			VolEquipamentos equipamento = volEquipamentosService.findEquipamentoByImei( map.getString("imei"));
			
			if (equipamento == null) {
				retorno.put("IMEI","INVALIDO");
			} else {
				/* Equipamento */
				retorno.put("VERSAOATUAL",oVersao.getDsConteudo());
				retorno.put("EQUIPAMENTO",equipamento.getIdEquipamento());
				retorno.put("IMEI",equipamento.getDsImei());
					
				List controles = controleCargaService.findControleCargaByNrControleByFilial(Long.valueOf(cCarga), oFilial.getIdFilial());
				
				if (controles != null && controles.size() > 0)  {
					/*
					 * Assumo que exista somente 1 controle de carga por filial com determinado número.
					 */
					ControleCarga controleCarga = (ControleCarga) controles.get(0);
					if (controleCarga != null) {
						Long senhaControleCarga = Long.valueOf(controleCarga.getDhGeracao().getHourOfDay() + "" + 
						                            (controleCarga.getDhGeracao().getMinuteOfHour() < 10 ? "0" + controleCarga.getDhGeracao().getMinuteOfHour() : controleCarga.getDhGeracao().getMinuteOfHour()));  
						
						if (!senha.equals(senhaControleCarga)) {
						retorno.put("CONTROLECARGA","INVALIDO");
						}
						else if ( !controleCarga.getTpStatusControleCarga().equals(new DomainValue("TC")) &&
						     !controleCarga.getTpStatusControleCarga().equals(new DomainValue("EV")) ) {
							retorno.put("CONTROLECARGA","PORTARIA");
						} else {
							try {
								MeioTransporte frota = controleCarga.getMeioTransporteByIdTransportado();
								if (frota == null) {
									retorno.put("IDFROTA","NC");			
								} else {
								equipamento.setDhAtualizacao(new DateTime());
								equipamento.setNmVersao(versaoProgMovel);
                                                                equipamento.setVersaoSO(versaoSistemaOperacional);
								volEquipamentosService.store(equipamento);
								
									/* Frota */
								retorno.put("IDFROTA",frota.getIdMeioTransporte());
									retorno.put("FROTA",frota.getNrFrota());
									/* Controle de Carga */
								retorno.put("CONTROLECARGA",controleCarga.getIdControleCarga());
								retorno.put("IDMOTORISTA", controleCarga.getMotorista().getIdMotorista());
								}
							} catch (Exception e) {
								if (e.getMessage().contains("41050")){
									retorno.put("EQUIPAMENTO","DUPLO");
							}
						}
					}
				}
			}
		}
	}
		return retorno;
	}
	 
	/**
	 * Pesquisa o equipamento da frota vinculada 
	 * @param frota Meio de transporte
	 * @return Equipamento pesquisado
	 */
	private VolEquipamentos findEquipamento(MeioTransporte frota){
		if (frota==null)
			return null;
		Map criteria = new HashMap();
		criteria.put("meioTransporte.idMeioTransporte",frota.getIdMeioTransporte());
		
		List list = volEquipamentosService.find(criteria);
		if (list.size()>=1)
			return (VolEquipamentos) list.get(0);
		return null;
	}
		
		
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}
	public void setVolEquipamentosService(
			VolEquipamentosService volEquipamentosService) {
		this.volEquipamentosService = volEquipamentosService;
	}
	
	public void setVolDadosSessaoService(VolDadosSessaoService volDadosSessaoService) {
		this.volDadosSessaoService = volDadosSessaoService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public PaisService getPaisService() {
		return paisService;
	}
	
}
