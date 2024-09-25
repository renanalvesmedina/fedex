package com.mercurio.lms.configuracoes.model.service;

import java.util.ArrayList;
import java.util.List;

import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.ConstantesConfiguracoes;
import com.mercurio.lms.contratacaoveiculos.model.service.BeneficiarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.OperadoraMctService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.municipios.model.service.AeroportoService;
import com.mercurio.lms.municipios.model.service.ConcessionariaService;
import com.mercurio.lms.municipios.model.service.EmpresaService;
import com.mercurio.lms.municipios.model.service.FilialCiaAereaService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.DespachanteService;

/**
 * @author Mickaël Jalbert
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * @spring.bean id="lms.configuracoes.especializacaoPessoa"
 */
public class EspecializacaoPessoaService {
	private EmpresaService empresaService;
	private FilialService filialService;
	private AeroportoService aeroportoService;
	private FilialCiaAereaService filialCiaAereaService;
	private ConcessionariaService concessionariaService;
	private ProprietarioService proprietarioService;
	private MotoristaService motoristaService;
	private BeneficiarioService beneficiarioService;
	private OperadoraMctService operadoraMctService;
	private EmpresaCobrancaService empresaCobrancaService;
	private DespachanteService despachanteService;
	private ClienteService clienteService;
	private ConfiguracoesFacade configuracoesFacade;

	public Short isEspecializado(Long idPessoa, Short cdEspeciolizacao){
		List<Short> lstCdEspecializacao = new ArrayList<Short>(1);
		lstCdEspecializacao.add(cdEspeciolizacao);
		return isEspecializado(idPessoa, lstCdEspecializacao);
	}

	public Short isEspecializado(Long idPessoa, List<Short> lstCdEspecializacao) {

		for(Short cdEspecializacao : lstCdEspecializacao) {
			if(CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_EMPRESA)) {
				if(empresaService.isEmpresa(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_FILIAL)) {
				if(filialService.isFilialVigente(idPessoa, JTDateTimeUtils.getDataAtual())) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_AEROPORTO)) {
				if(aeroportoService.isAeroporto(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_FILIAL_CIA_AEREA)) {
				if(filialCiaAereaService.verificaFilialCiaAereaVigente(idPessoa, JTDateTimeUtils.getDataAtual(), JTDateTimeUtils.getDataAtual())) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_CON_POSTO_PASSAGEM)) {
				if(concessionariaService.isConcessionaria(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_PROPRIETARIO)) {
				if(proprietarioService.isProprietario(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_MOTORISTA)) {
				if(motoristaService.isMotorista(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_BENEFICIARIO)) {
				if(beneficiarioService.isBeneficiario(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_OPERADORA_MCT)) {
				if(operadoraMctService.isOperadoraMct(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_EMPRESA_COBRANCA)) {
				if(empresaCobrancaService.isEmpresaCobranca(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_DESPACHANTE)) {
				if(despachanteService.isDespachante(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_CLIENTE)) {
				if(clienteService.isCliente(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_CLIENTE_ESPECIAL)) {
				if(clienteService.isClienteEspecial(idPessoa)) {
					return cdEspecializacao;
				}
			}

			if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_MOTORISTA_EVENTUAL)) {
				if(motoristaService.isMotoristaEventual(idPessoa)) {
					return cdEspecializacao;
				}
			}

		}
		
		return null;
	}

	public String getLabel(Short cdEspecializacao) {
		String strLabel = null;

		if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_EMPRESA)) {
			strLabel = "empresa";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_FILIAL)) {
			strLabel = "filial";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_AEROPORTO)) {
			strLabel = "aeroporto";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_FILIAL_CIA_AEREA)) {
			strLabel = "filialCiaAerea";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_CON_POSTO_PASSAGEM)) {
			strLabel = "concessionaria";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_PROPRIETARIO)) {
			strLabel = "proprietario";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_MOTORISTA) || CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_MOTORISTA_EVENTUAL)){
			strLabel = "motorista";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_BENEFICIARIO)) {
			strLabel = "beneficiario";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_OPERADORA_MCT)) {
			strLabel = "operadoraMct";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_EMPRESA_COBRANCA)) {
			strLabel = "empresaCobranca";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_DESPACHANTE)) {
			strLabel = "despachante";
		} else if (CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_CLIENTE) || CompareUtils.eq(cdEspecializacao, ConstantesConfiguracoes.IS_CLIENTE_ESPECIAL)) {
			strLabel = "cliente";
		}

		return configuracoesFacade.getMensagem(strLabel);
	}

	public void setEmpresaService(EmpresaService empresaService) {
		this.empresaService = empresaService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setAeroportoService(AeroportoService aeroportoService) {
		this.aeroportoService = aeroportoService;
	}
	public void setFilialCiaAereaService(FilialCiaAereaService filialCiaAereaService) {
		this.filialCiaAereaService = filialCiaAereaService;
	}
	public void setConcessionariaService(ConcessionariaService concessionariaService) {
		this.concessionariaService = concessionariaService;
	}
	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}
	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}
	public void setBeneficiarioService(BeneficiarioService beneficiarioService) {
		this.beneficiarioService = beneficiarioService;
	}
	public void setOperadoraMctService(OperadoraMctService operadoraMctService) {
		this.operadoraMctService = operadoraMctService;
	}
	public void setEmpresaCobrancaService(EmpresaCobrancaService empresaCobrancaService) {
		this.empresaCobrancaService = empresaCobrancaService;
	}
	public void setDespachanteService(DespachanteService despachanteService) {
		this.despachanteService = despachanteService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}
