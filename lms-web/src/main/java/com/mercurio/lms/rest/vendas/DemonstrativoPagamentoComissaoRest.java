package com.mercurio.lms.rest.vendas;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoDMN;
import br.com.tntbrasil.integracao.domains.comissionamento.ComissaoWrapperDMN;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.report.MultiReportCommand;
import com.mercurio.adsm.framework.report.ReportExecutionManager;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.edw.EdwService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.rest.vendas.dto.DemonstrativoPagamentoComissaoFilterDTO;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ComissaoProvisionadaService;
import com.mercurio.lms.vendas.model.service.ExecutivoTerritorioService;
import com.mercurio.lms.vendas.report.DemonstrativoExecutivoInternoService;
import com.mercurio.lms.vendas.report.DemonstrativoGerentesService;
import com.mercurio.lms.vendas.report.DemonstrativoFieldService;

@Path("/vendas/demonstrativoPagamentoComissao")
public class DemonstrativoPagamentoComissaoRest {

	@InjectInJersey
	private ComissaoProvisionadaService comissaoProvisionadaService;
	
	@InjectInJersey 
	private ReportExecutionManager reportExecutionManager;
	
	@InjectInJersey 
	private DemonstrativoFieldService demonstrativoField;

	@InjectInJersey 
	private DemonstrativoGerentesService demonstrativoGerentesService;

	@InjectInJersey 
	private DemonstrativoExecutivoInternoService demonstrativoExecutivoInternoService;

	@InjectInJersey
	private EdwService edwService;
	
	@InjectInJersey 
	private ExecutivoTerritorioService executivoTerritorioService;
	
	@InjectInJersey
	private FilialService filialService;

	@InjectInJersey
	private UsuarioService usuarioService;
	
	private static final Logger LOGGER = LogManager.getLogger(IncluirEventosRastreabilidadeInternacionalService.class);

	@POST
	@Path("relatorioDemonstrativoField")
	public Response relatorioDemonstrativoField(Map<String, String> filtros) throws Exception {
		
		String competencia = filtros.get("competencia");
		String tpModal = filtros.get("tpModal");
		String idFilial = filtros.get("idFilial") == null ? null : filtros.get("idFilial").toString();
		String idExecutivo = filtros.get("idExecutivo");
		
		validaFiliais(idFilial, idExecutivo);
		
		MultiReportCommand mrc = new MultiReportCommand("arquivo");

		Integer mesCalculo = getMesCalculo(competencia);
		Integer anoCalculo = getAnoCalculo(competencia);
		
		if (idFilial != null) {
			ComissaoWrapperDMN comissoes = edwService.findDemonstrativoPagamentoByTerritorio(Long.parseLong(filtros.get("idFilial").toString()), mesCalculo, anoCalculo, toModal(tpModal), "Executivo Externo");
			Set<Long> cpfs = getExecutivosCpf(comissoes.getListComissaoDMN());
	
			boolean temInformacao = false;
			String cargoCpfInformado = null;
			if (idExecutivo != null) {
				cargoCpfInformado = executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo));
			}
			for (Long cpf : cpfs) {
				if ((idExecutivo == null) || ((idExecutivo != null) && (cpf.toString().equals(cargoCpfInformado)))) {
					TypedFlatMap parameters = new TypedFlatMap();
					
					parameters.put("mesCalculo", mesCalculo);
					parameters.put("anoCalculo", anoCalculo);
					parameters.put("tpModal", tpModal);
					parameters.put("cargoCpf", cpf);
					
					mrc.addCommand("lms.vendas.demonstrativoFieldService", parameters);	
					temInformacao = true;
				}
			}
			
			if (!temInformacao) {
				throw new BusinessException("emptyReport");
			}
		} else {
			TypedFlatMap parameters = new TypedFlatMap();
			
			parameters.put("mesCalculo", mesCalculo);
			parameters.put("anoCalculo", anoCalculo);
			parameters.put("tpModal", tpModal);
			parameters.put("cargoCpf", executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo)));
			
			mrc.addCommand("lms.vendas.demonstrativoField", parameters);	
		}
		
		String reportLocator = null;
		try {
			File reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			reportLocator = reportExecutionManager.generateReportLocator(reportFile);
		} catch(Exception e) {
			LOGGER.error(e);
			throw new BusinessException("LMS-01332");
		}
		
		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("fileName", reportLocator);
		return Response.ok(retorno).build();
	}

	
	private Integer getAnoCalculo(String competencia) {
		return Integer.parseInt(competencia.substring(0, 4));
	}

	private Integer getMesCalculo(String competencia) {
		return Integer.parseInt(competencia.substring(0, 10).split("-")[1]);
	}

	private Set<Long> getExecutivosCpf(List<ComissaoDMN> listComissaoDMN) {
		Set<Long> cpfs = new HashSet<Long>();
		
		for(ComissaoDMN comissao : listComissaoDMN) {
			cpfs.add(comissao.getCargoCpf());
		}
		
		return cpfs;
	}

	@POST
	@Path("relatorioGerentesRegionais")
	public Response relatorioGerentesRegionais(Map<String, String> filtros) throws Exception {
		
		String competencia = filtros.get("competencia");
		String tpModal = filtros.get("tpModal");
		String idFilial = filtros.get("idFilial") == null ? null : filtros.get("idFilial").toString();
		String idExecutivo = filtros.get("idExecutivo");

		validaFiliais(idFilial, idExecutivo);
		
		MultiReportCommand mrc = new MultiReportCommand("arquivo");

		Integer mesCalculo = getMesCalculo(competencia);
		Integer anoCalculo = getAnoCalculo(competencia);
		
		if (idFilial != null) {
			ComissaoWrapperDMN comissoes = edwService.findDemonstrativoFechamentoByTerritorio(Long.parseLong(filtros.get("idFilial")), 
					mesCalculo, anoCalculo, "GERENTE_VENDAS,GERENTE_REGIONAL_VENDAS,GERENTE_FILIAL,GERENTE_DE_CONTA_ESTRATEGICA");
			
			Set<Long> cpfs = getExecutivosCpf(comissoes.getListComissaoDMN());
	
			boolean temInformacao = false;
			String cargoCpfInformado = null;
			if (idExecutivo != null) {
				cargoCpfInformado = executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo));
			}
			for (Long cpf : cpfs) {
				if ((idExecutivo == null) || ((idExecutivo != null) && (cpf.toString().equals(cargoCpfInformado)))) {
					TypedFlatMap parameters = new TypedFlatMap();
					
					parameters.put("mesCalculo", mesCalculo);
					parameters.put("anoCalculo", anoCalculo);
					parameters.put("tpModal", tpModal);
					parameters.put("cargoCpf", cpf);
					
					try {
						mrc.addCommand("lms.vendas.demonstrativoGerentesService", parameters);	
					} catch(Exception e) {
						LOGGER.error(e);
						throw new BusinessException("LMS-01332");
					}
					
					temInformacao = true;
				}
			}
			
			if (!temInformacao) {
				throw new BusinessException("emptyReport");
			}
		} else {
			TypedFlatMap parameters = new TypedFlatMap();
			
			parameters.put("mesCalculo", mesCalculo);
			parameters.put("anoCalculo", anoCalculo);
			parameters.put("tpModal", tpModal);
			parameters.put("cargoCpf", executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo)));
			
			mrc.addCommand("lms.vendas.demonstrativoGerentesService", parameters);	
		}
		
		String reportLocator = null;
		try {
			File reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			reportLocator = reportExecutionManager.generateReportLocator(reportFile);
		} catch(Exception e) {
			LOGGER.error(e);
			throw new BusinessException("LMS-01332");
		}
		
		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("fileName", reportLocator);
		return Response.ok(retorno).build();
	}
	
	@POST
	@Path("relatorioGerentesVendaFilial")
	public Response relatorioGerentesVendasFilial(Map<String, String> filtros) throws Exception {

		String competencia = filtros.get("competencia");
		String tpModal = filtros.get("tpModal");
		String idFilial = filtros.get("idFilial") == null ? null : filtros.get("idFilial").toString();
		String idExecutivo = filtros.get("idExecutivo");

		validaFiliais(idFilial, idExecutivo);
		
		List<Map<String, Object>> findFiliaisByUsuario = filialService.findFiliaisByUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		Map<String, Object> firstFilial = findFiliaisByUsuario.iterator().next();
		if (firstFilial != null) {
			String irrestrito = firstFilial.get("irrestrito").toString();
			if ("N".equals(irrestrito)) {
				idExecutivo = SessionUtils.getUsuarioLogado().getIdUsuario().toString();
				idFilial = null;
			}
		}		
		
		MultiReportCommand mrc = new MultiReportCommand("arquivo");

		Integer mesCalculo = getMesCalculo(competencia);
		Integer anoCalculo = getAnoCalculo(competencia);
		
		if (idFilial != null) {
			ComissaoWrapperDMN comissoes = edwService.findDemonstrativoFechamentoByTerritorio(Long.parseLong(filtros.get("idFilial")), 
					mesCalculo, anoCalculo, "GERENTE_VENDAS,GERENTE_REGIONAL_VENDAS,GERENTE_FILIAL,GERENTE_DE_CONTA_ESTRATEGICA");
			
			Set<Long> cpfs = getExecutivosCpf(comissoes.getListComissaoDMN());
	
			boolean temInformacao = false;
			String cargoCpfInformado = null;
			if (idExecutivo != null) {
				cargoCpfInformado = executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo));
			}
			for (Long cpf : cpfs) {
				if ((idExecutivo == null) || ((idExecutivo != null) && (cpf.toString().equals(cargoCpfInformado)))) {
					TypedFlatMap parameters = new TypedFlatMap();
					
					parameters.put("mesCalculo", mesCalculo);
					parameters.put("anoCalculo", anoCalculo);
					parameters.put("tpModal", tpModal);
					parameters.put("cargoCpf", cpf);
					
					try {
						mrc.addCommand("lms.vendas.demonstrativoGerentesService", parameters);	
					} catch(Exception e) {
						LOGGER.error(e);
						throw new BusinessException("LMS-01332");	
					}
					
					temInformacao = true;
				}
			}
			
			if (!temInformacao) {
				throw new BusinessException("emptyReport");
			}
		} else {
			TypedFlatMap parameters = new TypedFlatMap();
			
			parameters.put("mesCalculo", mesCalculo);
			parameters.put("anoCalculo", anoCalculo);
			parameters.put("tpModal", tpModal);
			parameters.put("cargoCpf", executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo)));
			
			mrc.addCommand("lms.vendas.demonstrativoGerentesService", parameters);	
		}
		
		String reportLocator = null;
		try {
			File reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			reportLocator = reportExecutionManager.generateReportLocator(reportFile);
		} catch(Exception e) {
			LOGGER.error(e);
			throw new BusinessException("LMS-01332");
		}
		
		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("fileName", reportLocator);
		return Response.ok(retorno).build();
	}
	
	@POST
	@Path("relatorioExecutivoInterno")
	public Response relatorioExecutivoInterno(Map<String, String> filtros) throws Exception {
		
		String competencia = filtros.get("competencia");
		String tpModal = filtros.get("tpModal");
		String idFilial = filtros.get("idFilial") == null ? null : filtros.get("idFilial").toString();
		String idExecutivo = filtros.get("idExecutivo");

		validaFiliais(idFilial, idExecutivo);
		
		MultiReportCommand mrc = new MultiReportCommand("arquivo");

		Integer mesCalculo = getMesCalculo(competencia);
		Integer anoCalculo = getAnoCalculo(competencia);
		
		if (idFilial != null) {
			ComissaoWrapperDMN comissoes = edwService.findDemonstrativoPagamentoByTerritorio(Long.parseLong(filtros.get("idFilial").toString()), mesCalculo, anoCalculo, toModal(tpModal), "Executivo Interno");
			Set<Long> cpfs = getExecutivosCpf(comissoes.getListComissaoDMN());
	
			boolean temInformacao = false;
			String cargoCpfInformado = null;
			if (idExecutivo != null) {
				cargoCpfInformado = executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo));
			}
			for (Long cpf : cpfs) {
				if ((idExecutivo == null) || ((idExecutivo != null) && (cpf.toString().equals(cargoCpfInformado)))) {
					TypedFlatMap parameters = new TypedFlatMap();
					
					parameters.put("mesCalculo", mesCalculo);
					parameters.put("anoCalculo", anoCalculo);
					parameters.put("tpModal", tpModal);
					parameters.put("cargoCpf", cpf);
					
					try {
						mrc.addCommand("lms.vendas.demonstrativoExecutivoInternoService", parameters);	
					} catch(Exception e) {
						LOGGER.error(e);
						throw new BusinessException("LMS-01332");
					}
					
					temInformacao = true;
				}
			}
			
			if (!temInformacao) {
				throw new BusinessException("emptyReport");
			}
		} else {
			TypedFlatMap parameters = new TypedFlatMap();
			
			parameters.put("mesCalculo", mesCalculo);
			parameters.put("anoCalculo", anoCalculo);
			parameters.put("tpModal", tpModal);
			parameters.put("cargoCpf", executivoTerritorioService.findCargoCpfByIdExecutivo(Long.parseLong(idExecutivo)));
			
			mrc.addCommand("lms.vendas.demonstrativoExecutivoInternoService", parameters);	
		}
		
		String reportLocator = null;
		try {
			File reportFile = this.reportExecutionManager.executeMultiReport(mrc);
			reportLocator = reportExecutionManager.generateReportLocator(reportFile);
		} catch(Exception e) {
			LOGGER.error(e);
			throw new BusinessException("LMS-01332");
		}
		
		Map<String, String> retorno = new HashMap<String, String>();
		retorno.put("fileName", reportLocator);
		return Response.ok(retorno).build();
	}
	
	
	private String toModal(String modal) {
		if (modal == null) {
			return null;
		}
		
		if (modal.equals("R")) {
			return "rodoviario";
		}
		
		if (modal.equals("A")) {
			return "aereo";
		}

		return null;
	}

	@POST
	@Path("carregarValoresPadrao")
	public Response carregarValoresPadrao() {
		TypedFlatMap retorno = new TypedFlatMap();

		Filial filialLogada = SessionUtils.getFilialSessao();

		retorno.put("nmUsuario", SessionUtils.getUsuarioLogado().getNmUsuario());
		retorno.put("login", SessionUtils.getUsuarioLogado().getLogin());
		retorno.put("nrMatricula", SessionUtils.getUsuarioLogado().getNrMatricula());

		retorno.put("idFilial", filialLogada.getIdFilial());
		retorno.put("sgFilial", filialLogada.getSgFilial());
		retorno.put("nmFilial", filialLogada.getPessoa().getNmFantasia());
		
		return Response.ok(retorno).build();
	}

	@POST
	@Path("atualizaFilial")
    public Response atualizaFilial(DemonstrativoPagamentoComissaoFilterDTO filterDto) {
		TypedFlatMap retorno = new TypedFlatMap();
		
		if (filterDto.getExecutivo() == null) {
			retorno.put("idFilial", null);
			retorno.put("sgFilial", null);
			retorno.put("nmFilial", null);
		} else {
			Usuario usuarioExecutivo = usuarioService.findByNrMatricula(filterDto.getExecutivo().getNrMatricula());
			
			List<Map<String, Object>> f = filialService.findFiliaisByUsuario(usuarioExecutivo.getIdUsuario());
			if (f.isEmpty()) {
				retorno.put("idFilial", null);
				retorno.put("sgFilial", null);
				retorno.put("nmFilial", null);
			} else {
				Long idFilialPadrao = (Long)f.iterator().next().get("id_filial_padrao");
				Filial filialPadrao = filialService.findByIdInitLazyProperties(idFilialPadrao, true);
				
				retorno.put("idFilial", filialPadrao.getIdFilial());
				retorno.put("sgFilial", filialPadrao.getSgFilial());
				retorno.put("nmFilial", filialPadrao.getPessoa().getNmFantasia());
			}
		}

		return Response.ok(retorno).build();
    }
	
	private void validaFiliais(String filialInformada, String idExecutivo) {
		if (filialInformada == null && idExecutivo != null) {
			return;
		}
		
		List<Map<String, Object>> findFiliaisByUsuario = filialService.findFiliaisByUsuario(SessionUtils.getUsuarioLogado().getIdUsuario());
		
		for(Map<String, Object> filial : findFiliaisByUsuario) {
			if (filial.get("id_filial").toString().equals(filialInformada) || "S".equals(filial.get("irrestrito").toString())) {
				return;
			}
		}
		throw new BusinessException("LMS-00063");
	}
	
}

