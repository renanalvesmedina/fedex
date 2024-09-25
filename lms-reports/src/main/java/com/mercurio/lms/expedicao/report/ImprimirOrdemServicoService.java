package com.mercurio.lms.expedicao.report;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.security.model.Usuario;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.coleta.model.ManifestoColeta;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.OrdemServico;
import com.mercurio.lms.expedicao.model.OrdemServicoDocumento;
import com.mercurio.lms.expedicao.model.OrdemServicoItem;
import com.mercurio.lms.expedicao.model.service.OrdemServicoItemService;
import com.mercurio.lms.expedicao.model.service.OrdemServicoService;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class ImprimirOrdemServicoService extends ReportServiceSupport {

	private ConfiguracoesFacade configuracoesFacade;
	private OrdemServicoService ordemServicoService;
	private OrdemServicoItemService ordemServicoItemService; 
	private MunicipioService municipioService;
	private UsuarioService usuarioService;
	
	@Override
	public JRReportDataObject execute(Map criteria) throws Exception {
		Long[] ids = (Long[])criteria.get("ordensServico");
		
		if (ids == null) {
			Long id = (Long)criteria.get("idOrdemServico");
			if (id == null) {
				throw new BusinessException("emptyReport");
			}
			ids = new Long[] { id };
		}

		Map<String, Object> parametersReport = new HashMap<String, Object>();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
		parametersReport.put("usuarioEmissor", SessionUtils.getUsuarioLogado().getNmUsuario());
		
		List lstOrdens = new ArrayList<String>();
		for (Long id : ids) {
			lstOrdens.add(getOrdemMap(id));
		}

		JRMapCollectionDataSource jrMap = new JRMapCollectionDataSource(lstOrdens);
		return createReportDataObject(jrMap, parametersReport);
	}
	
	private Map getOrdemMap(Long idOrdemServico) {
		Map ordemMap = new HashMap<String, Object>();
		
		TypedFlatMap map = ordemServicoService.findByIdWithPreFatura(idOrdemServico);

		OrdemServico ordemServico = (OrdemServico) map.get("ordemServico");
		TypedFlatMap preFaturaServico = (TypedFlatMap) map.get("preFaturaServico");

		ordemMap.put("ID_ORDEM_SERVICO", idOrdemServico);
		ordemMap.put("sgFilialRegistro", "");
		ordemMap.put("nmFantasiaRegistro", "");
		ordemMap.put("sgFilialExecucao", "");
		ordemMap.put("nmFantasiaExecucao", "");
		ordemMap.put("nmMunicipio", "");
		ordemMap.put("usuarioRegistrante", "");

		Filial filialRegistro = ordemServico.getFilialRegistro();
		if (filialRegistro != null) {
			ordemMap.put("sgFilialRegistro", toStr(filialRegistro.getSgFilial()));
			ordemMap.put("nmFantasiaRegistro", toStr(filialRegistro.getPessoa().getNmFantasia()));
		}
		
		Filial filialExecucao = ordemServico.getFilialExecucao();
		if (filialExecucao != null) {
			ordemMap.put("sgFilialExecucao", toStr(filialExecucao.getSgFilial()));
			ordemMap.put("nmFantasiaExecucao", toStr(filialExecucao.getPessoa().getNmFantasia()));
		}
		
		Municipio municipio = municipioService.findById(ordemServico.getMunicipioExecucao().getIdMunicipio());
		if (municipio != null) {
			ordemMap.put("nmMunicipio", toStr(municipio.getNmMunicipio()));
		}
		
		Usuario usuario = usuarioService.findById(ordemServico.getUsuarioRegistrante().getIdUsuario());
		if (usuario != null) {
			ordemMap.put("usuarioRegistrante", toStr(usuario.getNmUsuario()));
		}
        
		ordemMap.put("dtSolicitacao", toStr(ordemServico.getDtSolicitacao()));
		ordemMap.put("dsMotivoRejeicao", toStr(ordemServico.getDsMotivoRejeicao()));

		ordemMap.put("nrOrdemServico", toStr(ordemServico.getNrOrdemServico()));
		ordemMap.put("tpSituacaoOrdemServico", "");
		ordemMap.put("sgFilialCobranca", "");
		ordemMap.put("nrPreFatura", "");
		ordemMap.put("tpSituacaoPreFatura", "");
		
		if (ordemServico.getTpSituacao() != null) {
			ordemMap.put("tpSituacaoOrdemServico", toStr(ordemServico.getTpSituacao().getDescription()));
		}
		
		if (preFaturaServico.get("sgFilialCobranca") != null) {
			ordemMap.put("sgFilialCobranca", toStr(preFaturaServico.get("sgFilialCobranca").toString()));
		}

		if (preFaturaServico.get("nrPreFatura") != null) {
			ordemMap.put("nrPreFatura", toStr(preFaturaServico.get("nrPreFatura").toString()));
		}

		if (preFaturaServico.get("tpSituacaoPreFatura") != null) {
			ordemMap.put("tpSituacaoPreFatura", toStr(((DomainValue)preFaturaServico.get("tpSituacaoPreFatura")).getDescription()));
		}
		
		ordemMap.put("tpDocumento", "");
		ordemMap.put("nrDocumento", "");
		ordemMap.put("nrManifesto", "");
		if (ordemServico.getTpDocumento() != null) {
			String tpDocumento = ordemServico.getTpDocumento().getValue();
			ordemMap.put("tpDocumento", ordemServico.getTpDocumento().getDescription().toString());
			
			if (ConstantesExpedicao.TP_DOCUMENTO_DOCTO_SERVICO.equals(tpDocumento)) {			
				if (ordemServico.getOrdemServicoDocumentos() != null) {
					
					String nrDocumento = "";
					for (OrdemServicoDocumento ordemServicoDocumento : ordemServico.getOrdemServicoDocumentos()) {
						DoctoServico doctoServico = ordemServicoDocumento.getDoctoServico();
						DomainValue dv = configuracoesFacade.getDomainValue("DM_TIPO_DOCUMENTO_SERVICO", doctoServico.getTpDocumentoServico().getValue());
						Filial filialOrigem = doctoServico.getFilialByIdFilialOrigem();
						
						nrDocumento += dv.getDescription().toString() 
							+ " - " + filialOrigem.getSgFilial() 
							+ " - "	+ doctoServico.getNrDoctoServico() + ", ";
					}
					ordemMap.put("nrDocumento", toStr(nrDocumento));
				}

			} else {
				Integer nrManifesto;
				if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_COLETA.equals(tpDocumento)) {
					ManifestoColeta manifestoColeta = ordemServico.getManifestoColeta();
					nrManifesto = manifestoColeta.getNrManifesto();
				} else {
					Manifesto manifesto = ordemServico.getManifesto();
					if (ConstantesExpedicao.TP_DOCUMENTO_MANIFESTO_ENTREGA.equals(tpDocumento)) {
						nrManifesto = manifesto.getManifestoEntrega().getNrManifestoEntrega();
					} else {
						nrManifesto = manifesto.getManifestoViagemNacional().getNrManifestoOrigem();
					}
				}
				ordemMap.put("nrManifesto", toStr(nrManifesto));
			}
		}
		
		Pessoa pessoa = ordemServico.getClienteTomador().getPessoa();
		ordemMap.put("nrIdentificacao", toStr(pessoa.getNrIdentificacao()));
		ordemMap.put("nmPessoa", toStr(pessoa.getNmPessoa()));
		ordemMap.put("nmSolicitante", toStr(ordemServico.getNmSolicitante()));
		
		ordemMap.put("divisaoCliente", "");
		ordemMap.put("inscricaoEstadual", "");
		ordemMap.put("tpSolicitante", "");
		if (ordemServico.getDivisaoCliente() != null) {
			ordemMap.put("divisaoCliente", toStr(ordemServico.getDivisaoCliente().getDsDivisaoCliente()));
		}
		if (ordemServico.getIeTomador() != null) {
			ordemMap.put("inscricaoEstadual", toStr(ordemServico.getIeTomador().getNrInscricaoEstadual()));
		}
		if (ordemServico.getTpSolicitante() != null) {
			ordemMap.put("tpSolicitante", toStr(ordemServico.getTpSolicitante().getDescription()));
		}

		return ordemMap;
	}

	public JRDataSource executeSubReportDocumento(Object[] obj) throws Exception {
		Long id = (Long)obj[0];
		JRMapCollectionDataSource jrMap;
		List lstDocumento = new ArrayList();
		Map pStr;
		
		List<ImprimirOrdemServicoBean> beanList = buildItens(ordemServicoItemService.findByOrdemServico(id));
		if (beanList.size() == 0) {
			pStr = new HashMap<String, String>();

			pStr.put("dsParcelaPreco", "");
			pStr.put("vlTabela", "");
			pStr.put("nrKmRodado", "");
			pStr.put("qtPalete", "");
			pStr.put("tpEscolta", "");
			pStr.put("dsServico", "");
			pStr.put("vlNegociado", "");
			pStr.put("qtHomem", "");
			pStr.put("tpModeloPalete", "");
			pStr.put("dhPeriodoInicial", "");
			pStr.put("dhPeriodoFinal", "");
			pStr.put("dtExecucao", "");
			pStr.put("vlCusto", "");
			pStr.put("qtVolume", "");
			pStr.put("blRetornaPalete", "");
			lstDocumento.add(pStr);
			
		} else {
			for (ImprimirOrdemServicoBean bean : beanList) {
				pStr = new HashMap<String, String>();
				
				pStr.put("dsParcelaPreco", bean.getDsParcelaPreco());
				pStr.put("vlTabela", bean.getVlTabela());
				pStr.put("nrKmRodado", bean.getNrKmRodado());
				pStr.put("qtPalete", bean.getQtPalete());
				pStr.put("tpEscolta", bean.getTpEscolta());
				pStr.put("dsServico", bean.getDsServico());
				pStr.put("vlNegociado", bean.getVlNegociado());
				pStr.put("qtHomem", bean.getQtHomem());
				pStr.put("tpModeloPalete", bean.getTpModeloPalete());
				pStr.put("dhPeriodoInicial", bean.getDhPeriodoInicial());
				pStr.put("dhPeriodoFinal", bean.getDhPeriodoFinal());
				pStr.put("dtExecucao", bean.getDtExecucao());
				pStr.put("vlCusto", bean.getVlCusto());
				pStr.put("qtVolume", bean.getQtVolume());
				pStr.put("blRetornaPalete", bean.getBlRetornaPalete());
				lstDocumento.add(pStr);
			}
		}
		
		jrMap = new JRMapCollectionDataSource(lstDocumento);
		return jrMap;
	}

	private JRReportDataObject createReportDataObject(final List<ImprimirOrdemServicoBean> beanList) {
		return new JRReportDataObject() {
			Map parameters = new HashMap();

			@Override
			public JRDataSource getDataSource() {
				return new JRBeanCollectionDataSource(beanList);
			}

			@Override
			public Map getParameters() {
				return parameters;
			}

			@Override
			public void setParameters(Map parametersMap) {
				parameters = parametersMap;
			}
		};
	}
	
	private List<ImprimirOrdemServicoBean> buildItens(List<OrdemServicoItem> osiList) {
		List<ImprimirOrdemServicoBean> beanList = new ArrayList<ImprimirOrdemServicoBean>();
		ImprimirOrdemServicoBean bean;
		for (OrdemServicoItem osi : osiList) {
			bean = new ImprimirOrdemServicoBean();
			if (osi.getParcelaPreco() != null) {
				bean.setDsParcelaPreco(toStr(osi.getParcelaPreco().getDsParcelaPreco()));
			}
			bean.setDtExecucao(toStr(osi.getDtExecucao()));
			bean.setVlTabela(toStr(osi.getVlTabela()));
			bean.setVlNegociado(toStr(osi.getVlNegociado()));
			bean.setVlCusto(toStr(osi.getVlCusto()));
			bean.setNrKmRodado(toStr(osi.getNrKmRodado()));
			bean.setQtHomem(toStr(osi.getQtHomem()));  
			bean.setQtVolume(toStr(osi.getQtVolume()));
			bean.setQtPalete(toStr(osi.getQtPalete()));
			if (osi.getTpModeloPalete() != null) {
				bean.setTpModeloPalete(toStr(osi.getTpModeloPalete().getDescription()));
			} else {
				bean.setTpModeloPalete("");
			}
			bean.setBlRetornaPalete(toStr(osi.getBlRetornaPalete()));
			if (osi.getTpEscolta() != null) {
				bean.setTpEscolta(toStr(osi.getTpEscolta().getDescription()));
			} else {
				bean.setTpEscolta("");
			}
			bean.setDhPeriodoInicial(toStr(osi.getDhPeriodoInicial()));
			bean.setDhPeriodoFinal(toStr(osi.getDhPeriodoFinal()));
			bean.setDsServico(toStr(osi.getDsServico()));
			beanList.add(bean);
		}
		if (beanList.size() == 0) {
			bean = new ImprimirOrdemServicoBean();
			beanList.add(bean);
		}
		Collections.sort(beanList);
		return beanList;
	}

	private String toStr(VarcharI18n p) {
		if (p == null) return "";
		return p.toString();
	}
	
	private String toStr(YearMonthDay p) {
		if (p == null) return "";
		return JTFormatUtils.format(p);
	}

	private String toStr(DateTime p) {
		if (p == null) return "";
		return JTFormatUtils.format(p);
	}

	private String toStr(Boolean p) {
		if (p == null) return "";
		if (p) return "Sim";
		return "Não";
	}

	private String toStr(BigDecimal p) {
		if (p == null) return "";
		return NumberFormat.getCurrencyInstance().format(p).substring(3);
	}
	
	private String toStr(Integer p) {
		if (p == null) return "";
		return p.toString();
	}

	private String toStr(Long p) {
		if (p == null) return "";
		return p.toString();
	}

	private String toStr(String p) {
		if (p == null) return "";
		return p;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

	public void setOrdemServicoService(OrdemServicoService ordemServicoService) {
		this.ordemServicoService = ordemServicoService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setOrdemServicoItemService(OrdemServicoItemService ordemServicoItemService) {
		this.ordemServicoItemService = ordemServicoItemService;
	}

}
