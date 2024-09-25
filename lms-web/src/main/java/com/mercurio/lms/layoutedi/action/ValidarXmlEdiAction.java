package com.mercurio.lms.layoutedi.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.CepService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.edi.model.CampoLayoutEDI;
import com.mercurio.lms.edi.model.ComposicaoCodigoVolume;
import com.mercurio.lms.edi.model.ComposicaoLayoutEDI;
import com.mercurio.lms.edi.model.DeParaDetalheEDI;
import com.mercurio.lms.edi.model.service.CampoLayoutEDIService;
import com.mercurio.lms.edi.model.service.ClienteEDIFilialEmbarcadoraService;
import com.mercurio.lms.edi.model.service.ClienteLayoutEDIService;
import com.mercurio.lms.edi.model.service.ComposicaoCodigoVolumeService;
import com.mercurio.lms.edi.model.service.ComposicaoLayoutEdiService;
import com.mercurio.lms.edi.model.service.DeParaDetalheEDIService;
import com.mercurio.lms.edi.model.service.DeParaEDIService;
import com.mercurio.lms.edi.model.service.ValidaConexaoService;
import com.mercurio.lms.layoutedi.model.DadosVolume;
import com.mercurio.lms.municipios.model.service.IntervaloCepService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.ValidateUtils;
import com.mercurio.lms.vendas.model.service.InformacaoDoctoClienteService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author ThiagoFA
 * @spring.bean id="lms.layoutedi.ValidarXmlEdiAction"
 */
@ServiceSecurity
public class ValidarXmlEdiAction {
	private Logger log = LogManager.getLogger(this.getClass());
	private CepService cepService;
	private UnidadeFederativaService unidadeFederativaService;
	private InformacaoDoctoClienteService informacaoDoctoClienteService;
	private ClienteLayoutEDIService clienteLayoutEDIService;
	private ComposicaoLayoutEdiService composicaoLayoutEdiService;
	private ComposicaoCodigoVolumeService composicaoCodigoVolumeService;
	private CampoLayoutEDIService campoLayoutEDIService;
	private DeParaEDIService deParaEDIService;
	private DeParaDetalheEDIService deParaDetalheEDIService;
	private ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService;
	private IntervaloCepService intervaloCepService;
	private ValidaConexaoService validaConexaoService;
	private PessoaService pessoaService;
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "validarCep", authenticationRequired=false)
	public Boolean validarCep(Integer cep){
		try{
			return this.intervaloCepService.validateCep(cep.toString());
		}catch (Exception e) {
			return false;
		}
	}
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "validarCnpjCpf", authenticationRequired=false)
	public Boolean validarCnpjCpf(Long cnpjCpf){
		try{
			return ValidateUtils.validateCpfOrCnpj(cnpjCpf.toString());
		}catch (Exception e) {
			return null;
		}
	}
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "validarUf", authenticationRequired=false)
	public Boolean validarUf(String uf){
		try{
			List unidadeFederativas = this.unidadeFederativaService.findUnidadeFederativaBySgPaisAndSgUf("BRA", uf);
			if(unidadeFederativas == null || unidadeFederativas.size() == 0){
				return false;
			}else{
				return true;
			}
		}catch (Exception e) {
			return null;
		}
	}
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "buscarDadosComplemento", authenticationRequired=false)
	public Map buscarDadosComplemento(Object objeto){
		try{
			String[] parametros = (String[])objeto;
			List infoDoctoCliente = null;
			Long idClienteToma = null;
			
			if(StringUtils.isNotBlank(parametros[3])){
			    idClienteToma = getPessoaService().findByIdentificacao("CNPJ",parametros[3]).getIdPessoa();
			}
			
			if(idClienteToma != null){
			    infoDoctoCliente = this.informacaoDoctoClienteService.findDadosByCliente(idClienteToma,"REDESPFDX");
			    
			    if(infoDoctoCliente != null && infoDoctoCliente.size() > 0){
			        infoDoctoCliente = this.informacaoDoctoClienteService.findDadosByCliente(idClienteToma,parametros[1]);
			        if(infoDoctoCliente != null && infoDoctoCliente.size() > 0){
	                    return (HashMap)infoDoctoCliente.get(0);
	                }
	            }
			}
			
            Pessoa remetente = getPessoaService().findByIdentificacao("CNPJ",parametros[2]);
            if(remetente != null){
                infoDoctoCliente = this.informacaoDoctoClienteService.findDadosByCliente(remetente.getIdPessoa(),parametros[1]);
            }           
	            
            if(infoDoctoCliente != null && infoDoctoCliente.size() > 0){
                return (HashMap)infoDoctoCliente.get(0);
            } else {
                infoDoctoCliente = this.informacaoDoctoClienteService.findDadosByCliente(idClienteToma,parametros[1]);
                if(infoDoctoCliente != null && infoDoctoCliente.size() > 0){
                    return (HashMap)infoDoctoCliente.get(0);
                }
            }    
			
			return new HashMap<String, Object>(){{put("x", "x");}};
		}catch (Exception e) {
			return new HashMap<String, Object>(){{put("x", "x");}};
		}
	}
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "buscarDadosVolume", authenticationRequired=false)
	public List buscarDadosVolume(Long idComposicaoLayout){
		try{
			ComposicaoLayoutEDI composicaoLayoutEDI = this.composicaoLayoutEdiService.findById(idComposicaoLayout);
			List<DadosVolume> dadosVolumes = new ArrayList<DadosVolume>();
			if(composicaoLayoutEDI.getPosicao() == 0){
				List<ComposicaoCodigoVolume> composicaoCodigoVolumeList = this.composicaoCodigoVolumeService.findByComposicaoLayoutId(idComposicaoLayout);
				if(composicaoCodigoVolumeList != null && composicaoCodigoVolumeList.size() > 0){
					for (ComposicaoCodigoVolume composicaoCodigoVolume : composicaoCodigoVolumeList) {
						try{						
							ComposicaoLayoutEDI campoComposicaoLayoutEDI =  this.composicaoLayoutEdiService.findById(composicaoCodigoVolume.getComposicaoCampoEDI().getIdComposicaoLayout());
							DadosVolume dadosVolume = new DadosVolume();
							dadosVolume.setAlinhamento(composicaoCodigoVolume.getAlinhamento().getValue());
							dadosVolume.setComplementoPreenchimento(composicaoCodigoVolume.getComplPreenchimento());
							dadosVolume.setFormato(composicaoCodigoVolume.getFormato());
							dadosVolume.setIndicadorCalculo(composicaoCodigoVolume.getIndicadorCalculo().getValue());						
							dadosVolume.setOrdem(composicaoCodigoVolume.getOrdem());
							dadosVolume.setTamanho(composicaoCodigoVolume.getTamanho());
							CampoLayoutEDI campoLayoutEDI = campoComposicaoLayoutEDI.getCampoLayout();
							if(campoLayoutEDI != null && composicaoCodigoVolume.getIndicadorCalculo().getValue().equals("N")){
								dadosVolume.setNomeCampo(campoLayoutEDI.getCampoTabela());
								dadosVolume.setNomeComplemento(campoLayoutEDI.getNmComplemento());
							}
							dadosVolumes.add(dadosVolume);
						}catch (Exception e) {
							log.error(e);
						}
					}
				}
			}
			return dadosVolumes;
		}catch (Exception e) {
			return null;
		}
	}
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "buscarDePara", authenticationRequired=false)
	public String buscarDePara(Object parametros){
		try{
			Object[] parametroObj = (Object[])parametros;
			Long idComposicaoLayout = Long.parseLong(parametroObj[0].toString());
			String de = (String) parametroObj[1];
			ComposicaoLayoutEDI composicaoLayoutEDI = this.composicaoLayoutEdiService.findById(idComposicaoLayout);
			if(composicaoLayoutEDI.getDeParaEDI() == null){
				return null;
			}else{
				DeParaDetalheEDI deParaDetalheEDI = this.deParaDetalheEDIService.findByIdDeParaAndDe(composicaoLayoutEDI.getDeParaEDI().getIdDeParaEDI(), de);
				return deParaDetalheEDI.getPara().toString();
			}
		}catch (Exception e) {
			return null;
		}
	}
	
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "buscaCampos", authenticationRequired=false)
	public List<ComposicaoCodigoVolume> buscaCampos(Long idClienteLayoutEdi){
		try {
			List<ComposicaoCodigoVolume> composicaoCodigoVolumeList = this.composicaoCodigoVolumeService.findByIdClienteLayoutEdi(idClienteLayoutEdi);
			return composicaoCodigoVolumeList;
		} catch (Exception e){
			log.error(e);
			return null;
		}
	}
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "buscaNomeCampos", authenticationRequired=false)
	public List<Object> buscaNomeCampos(Long idClienteLayoutEdi){
		try {
			List<Object> composicaoCodigoVolumeList = this.composicaoCodigoVolumeService.findNomeByIdClienteLayoutEdi(idClienteLayoutEdi);
			return composicaoCodigoVolumeList;
		} catch (Exception e){
			log.error(e);
			return null;
		}
	}
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "buscaNomeCampoVolume", authenticationRequired=false)
	public List buscaNomeCampoVolume(Long idClienteLayoutEdi){
		try {
			List composicaoCodigoVolumeList = this.composicaoCodigoVolumeService.findNomeCampoVolumeByIdClienteLayoutEdi(idClienteLayoutEdi);
			return composicaoCodigoVolumeList;
		} catch (Exception e){
			log.error(e);
			return null;
		}
	}	
	
	@MethodSecurity(processGroup = "layoutedi.validarXml", processName = "validaConexao", authenticationRequired=false)
	public Boolean validaConexao() {
		try {
			return validaConexaoService.validateConexao();
		} catch (Exception e){
			log.error(e);
			return false;
		}
	}
	
	
	

	public CepService getCepService() {
		return cepService;
	}
	public void setCepService(CepService cepService) {
		this.cepService = cepService;
	}
	public UnidadeFederativaService getUnidadeFederativaService() {
		return unidadeFederativaService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public InformacaoDoctoClienteService getInformacaoDoctoClienteService() {
		return informacaoDoctoClienteService;
	}
	public void setInformacaoDoctoClienteService(InformacaoDoctoClienteService informacaoDoctoClienteService) {
		this.informacaoDoctoClienteService = informacaoDoctoClienteService;
	}
	public ClienteLayoutEDIService getClienteLayoutEDIService() {
		return clienteLayoutEDIService;
	}
	public void setClienteLayoutEDIService(ClienteLayoutEDIService clienteLayoutEDIService) {
		this.clienteLayoutEDIService = clienteLayoutEDIService;
	}
	public final ComposicaoLayoutEdiService getComposicaoLayoutEdiService() {
		return composicaoLayoutEdiService;
	}
	public final void setComposicaoLayoutEdiService(ComposicaoLayoutEdiService composicaoLayoutEdiService) {
		this.composicaoLayoutEdiService = composicaoLayoutEdiService;
	}
	public ComposicaoCodigoVolumeService getComposicaoCodigoVolumeService() {
		return composicaoCodigoVolumeService;
	}
	public void setComposicaoCodigoVolumeService(ComposicaoCodigoVolumeService composicaoCodigoVolumeService) {
		this.composicaoCodigoVolumeService = composicaoCodigoVolumeService;
	}
	public CampoLayoutEDIService getCampoLayoutEDIService() {
		return campoLayoutEDIService;
	}
	public void setCampoLayoutEDIService(CampoLayoutEDIService campoLayoutEDIService) {
		this.campoLayoutEDIService = campoLayoutEDIService;
	}
	public DeParaEDIService getDeParaEDIService() {
		return deParaEDIService;
	}
	public void setDeParaEDIService(DeParaEDIService deParaEDIService) {
		this.deParaEDIService = deParaEDIService;
	}
	public DeParaDetalheEDIService getDeParaDetalheEDIService() {
		return deParaDetalheEDIService;
	}
	public void setDeParaDetalheEDIService(DeParaDetalheEDIService deParaDetalheEDIService) {
		this.deParaDetalheEDIService = deParaDetalheEDIService;
	}
	public ClienteEDIFilialEmbarcadoraService getClienteEDIFilialEmbarcadoraService() {
		return clienteEDIFilialEmbarcadoraService;
	}
	public void setClienteEDIFilialEmbarcadoraService(ClienteEDIFilialEmbarcadoraService clienteEDIFilialEmbarcadoraService) {
		this.clienteEDIFilialEmbarcadoraService = clienteEDIFilialEmbarcadoraService;
	}
	public IntervaloCepService getIntervaloCepService() {
		return intervaloCepService;
	}
	public void setIntervaloCepService(IntervaloCepService intervaloCepService) {
		this.intervaloCepService = intervaloCepService;
	}
	public void setValidaConexaoService(ValidaConexaoService validaConexaoService) {
		this.validaConexaoService = validaConexaoService;
	}
	public PessoaService getPessoaService() {
		return pessoaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	
}
