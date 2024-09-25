package com.mercurio.lms.indenizacoes.action;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.core.InfrastructureException;
import com.mercurio.adsm.core.util.Base64Util;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.masterdetail.ItemList;
import com.mercurio.adsm.framework.model.masterdetail.ItemListConfig;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailAction;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailFactory;
import com.mercurio.adsm.framework.model.masterdetail.MasterDetailKey;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntry;
import com.mercurio.adsm.framework.model.masterdetail.MasterEntryConfig;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.carregamento.model.Manifesto;
import com.mercurio.lms.carregamento.model.service.ControleCargaService;
import com.mercurio.lms.carregamento.model.service.ManifestoService;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.ContaBancaria;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.AgenciaBancariaService;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.configuracoes.model.service.ContaBancariaService;
import com.mercurio.lms.configuracoes.model.service.ConversaoMoedaService;
import com.mercurio.lms.configuracoes.model.service.CotacaoIndicadorFinanceiroService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.PessoaService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.Produto;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.model.service.ProdutoService;
import com.mercurio.lms.franqueados.util.FranqueadoUtils;
import com.mercurio.lms.indenizacoes.model.AnexoRim;
import com.mercurio.lms.indenizacoes.model.DoctoServicoIndenizacao;
import com.mercurio.lms.indenizacoes.model.FilialDebitada;
import com.mercurio.lms.indenizacoes.model.MdaSalvadoIndenizacao;
import com.mercurio.lms.indenizacoes.model.ParcelaReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacao;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoFranqueado;
import com.mercurio.lms.indenizacoes.model.ReciboIndenizacaoNf;
import com.mercurio.lms.indenizacoes.model.service.AnexoRimService;
import com.mercurio.lms.indenizacoes.model.service.DoctoServicoIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.EventoRimService;
import com.mercurio.lms.indenizacoes.model.service.FilialDebitadaService;
import com.mercurio.lms.indenizacoes.model.service.IndenizacoesFranqueadosService;
import com.mercurio.lms.indenizacoes.model.service.MdaSalvadoIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.ParcelaReciboIndenizacaoService;
import com.mercurio.lms.indenizacoes.model.service.ReciboIndenizacaoService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.OrdemFilialFluxo;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.municipios.model.service.HistoricoFilialService;
import com.mercurio.lms.municipios.model.service.OrdemFilialFluxoService;
import com.mercurio.lms.pendencia.model.Mda;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.pendencia.report.EmitirMDAService;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.NaoConformidade;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;
import com.mercurio.lms.rnc.model.service.MotivoAberturaNcService;
import com.mercurio.lms.rnc.model.service.NaoConformidadeService;
import com.mercurio.lms.rnc.model.service.OcorrenciaNaoConformidadeService;
import com.mercurio.lms.seguros.model.ProcessoSinistro;
import com.mercurio.lms.seguros.model.TipoSeguro;
import com.mercurio.lms.seguros.model.service.ProcessoSinistroService;
import com.mercurio.lms.seguros.model.service.TipoSeguroService;
import com.mercurio.lms.util.ArquivoUtils;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *  
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.indenizacoes.manterReciboIndenizacaoAction"
 */

public class ManterReciboIndenizacaoAction extends MasterDetailAction {
	
	private ConfiguracoesFacade configuracoesFacade;
	private MdaService mdaService;
	private BancoService bancoService;
	private AgenciaBancariaService agenciaBancariaService;	
	private FilialService filialService;
	private MoedaService moedaService;
	private PessoaService pessoaService;
	private ProdutoService produtoService;
	private ClienteService clienteService;
	private FilialDebitadaService filialDebitadaService;
	private TipoSeguroService tipoSeguroService;
	private DoctoServicoService doctoServicoService;
	private ConhecimentoService conhecimentoService;
	private ContaBancariaService contaBancariaService;
	private DomainValueService domainValueService;
	private NaoConformidadeService naoConformidadeService;
	private ProcessoSinistroService processoSinistroService;
	private MotivoAberturaNcService motivoAberturaNcService;
	private MdaSalvadoIndenizacaoService mdaSalvadoIndenizacaoService;
	private DoctoServicoIndenizacaoService doctoServicoIndenizacaoService;
	private ParcelaReciboIndenizacaoService parcelaReciboIndenizacaoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private ConversaoMoedaService conversaoMoedaService;
	private ReportServiceSupport reportServiceSupport;
	private ManifestoService manifestoService;
	private ControleCargaService controleCargaService;
	private EventoRimService eventoRimService;
	private ReciboIndenizacaoService reciboIndenizacaoService;
	private OrdemFilialFluxoService ordemFilialFluxoService;
	private HistoricoFilialService historicoFilialService; 
	private CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService;
	private OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService;
	private AnexoRimService anexoRimService;
	private UsuarioLMSService usuarioLMSService;
	private IndenizacoesFranqueadosService indenizacoesFranqueadosService;

	
    public ResultSetPage findLancamentosByReciboIndenizacaoId(TypedFlatMap map){
	    if(map != null && map.containsKey("idReciboIndenizacao")){
	    	map.put("reciboIndenizacao.idReciboIndenizacao",  map.get("idReciboIndenizacao"));
	    }
    	
    	ResultSetPage result = indenizacoesFranqueadosService.findPaginated(map);

    	for(int i=0; i < result.getList().size();i++){
    		ReciboIndenizacaoFranqueado recibo = (ReciboIndenizacaoFranqueado) result.getList().get(i);
    		
    		String monthYear = FranqueadoUtils.getDateAsMonthYear(recibo.getDtCompetencia());
			((ReciboIndenizacaoFranqueado)result.getList().get(i)).setDtCompetenciaAsMesAno(monthYear);
			
			BigDecimal valorIndenizado = recibo.getNrValorIndenizado().multiply(recibo.getNrPercentualIndenizacao().divide(BigDecimal.valueOf(100)));
			BigDecimal parcValue = valorIndenizado.divide(recibo.getNrParcelas());
			
			((ReciboIndenizacaoFranqueado)result.getList().get(i)).setNrValorPorParcela(parcValue);
			((ReciboIndenizacaoFranqueado)result.getList().get(i)).setNrValorIndenizacaoReal(valorIndenizado);
    	}
    	
    	return result;
    }

    public Integer getRowCountLancamentosByReciboIndenizacaoId(TypedFlatMap map){
	    if(map != null && map.containsKey("idReciboIndenizacao")){
	    	map.put("reciboIndenizacao.idReciboIndenizacao",  map.get("idReciboIndenizacao"));
	    }
	    
    	return indenizacoesFranqueadosService.getRowCount(map);
    }
    
	
	
	protected MasterEntryConfig createMasterConfig(MasterDetailFactory masterFactory) {
		
    	/********************************************************************
    	 * MasterEntryConfig
    	 ********************************************************************/
		MasterEntryConfig masterConfig = masterFactory.createMasterEntryConfig(ReciboIndenizacao.class, true);
    	Comparator comparator = new Comparator() {
			public int compare(Object obj1, Object obj2) {
				return 0;
			}    		
    	};
    	
    	/********************************************************************
    	 * ItemListConfig
    	 ********************************************************************/
    	ItemListConfig itemConfig = new ItemListConfig() {
			
			public List initialize(Long masterId, Map parameters) {
				return doctoServicoIndenizacaoService.findByIdReciboIndenizacaoJoinFirstOcorrenciaNaoConformidade(masterId);
			}
			public Integer getRowCount(Long masterId, Map parameters) {
				return doctoServicoIndenizacaoService.getRowCountByIdReciboIndenizacao(masterId);
			}
			public Map configItemDomainProperties() {
				return null;
			}
			
			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) bean;

				if(getMasterId(parameters) != null) {
					doctoServicoIndenizacao.setReciboIndenizacao(getMaster().findById(getMasterId(parameters)));
				}
				
				Moeda moedaIndenizacao = moedaService.findById(tfm.getLong("moeda.idMoeda"));
				BigDecimal vlIndenizacao = tfm.getBigDecimal("vlIndenizacao");

				doctoServicoIndenizacao.setMoeda(moedaIndenizacao);
				doctoServicoIndenizacao.setVlIndenizado(vlIndenizacao);
				doctoServicoIndenizacao.setIdDoctoServicoIndenizacao(tfm.getLong("idDoctoServicoIndenizacao"));
				ReciboIndenizacao reciboIndenizacao = new ReciboIndenizacao();
				reciboIndenizacao.setIdReciboIndenizacao(getMasterId(tfm));
				doctoServicoIndenizacao.setReciboIndenizacao(reciboIndenizacao);
				doctoServicoIndenizacao.setQtVolumes(tfm.getInteger("qtVolumesIndenizados"));
				
				if (tfm.getLong("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade") != null) {
					doctoServicoIndenizacao.setOcorrenciaNaoConformidade(ocorrenciaNaoConformidadeService.findById(tfm.getLong("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade")));
				}
				
				if (tfm.getLong("filialSinistro.idFilial") != null) {
					doctoServicoIndenizacao.setFilialSinistro(filialService.findById(tfm.getLong("filialSinistro.idFilial")));
					doctoServicoIndenizacao.setRotaSinistro(filialService.findById(tfm.getLong("rotaSinistro.idFilial")));
				}
				
				if (tfm.getLong("produto.idProduto")!=null) {
					Produto produto = produtoService.findById(tfm.getLong("produto.idProduto"));
					doctoServicoIndenizacao.setProduto(produto);
				}
				
				List result = new ArrayList();
				List listbox = tfm.getList("notasFiscais");
				
				if (listbox!=null) {			    	
			    	for (Iterator it = listbox.iterator(); it.hasNext(); ) {
			    		TypedFlatMap item = (TypedFlatMap)it.next();
			    		Long idNotaFiscalConhecimento = item.getLong("idNotaFiscalConhecimento");
			    		NotaFiscalConhecimento notaFiscalConhecimento = notaFiscalConhecimentoService.findById(idNotaFiscalConhecimento);
			    		ReciboIndenizacaoNf reciboIndenizacaoNf = new ReciboIndenizacaoNf();
			    		reciboIndenizacaoNf.setNotaFiscalConhecimento(notaFiscalConhecimento);
			    		reciboIndenizacaoNf.setDoctoServicoIndenizacao(doctoServicoIndenizacao);			    		
			    		
			    		for (Object object : result) {
							ReciboIndenizacaoNf nf = (ReciboIndenizacaoNf)object;
							if(nf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento().equals(reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento())) {
								throw new BusinessException("LMS-28004");
							}
							
						}
			    		
		    			result.add(reciboIndenizacaoNf);
			    	}					
				}	
				doctoServicoIndenizacao.setReciboIndenizacaoNfs(result);
				return doctoServicoIndenizacao;
			}	
						
			public void modifyItemValues(Object newBean, Object bean) {
				DoctoServicoIndenizacao newObject = (DoctoServicoIndenizacao)newBean;
				DoctoServicoIndenizacao oldObject = (DoctoServicoIndenizacao)bean;

				boolean areBothListsEqual = areBothListsEqual(oldObject.getReciboIndenizacaoNfs(), newObject.getReciboIndenizacaoNfs());
				boolean areVlIndenizacaoEqual = oldObject.getVlIndenizado().compareTo(newObject.getVlIndenizado()) == 0;
								
				oldObject.setIdDoctoServicoIndenizacao(newObject.getIdDoctoServicoIndenizacao());
				oldObject.setProduto(newObject.getProduto());
				oldObject.setMoeda(newObject.getMoeda());
				oldObject.setVlIndenizado(newObject.getVlIndenizado());
				oldObject.setQtVolumes(newObject.getQtVolumes());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());

				oldObject.setOcorrenciaNaoConformidade(newObject.getOcorrenciaNaoConformidade());
				oldObject.setFilialSinistro(newObject.getFilialSinistro());
				oldObject.setRotaSinistro(newObject.getRotaSinistro());
				
				List list = new ArrayList();
				if (newObject.getReciboIndenizacaoNfs()!=null) {
					for(int i=0; i<newObject.getReciboIndenizacaoNfs().size(); i++) {
						ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf) newObject.getReciboIndenizacaoNfs().get(i);
						reciboIndenizacaoNf.setDoctoServicoIndenizacao(oldObject);
						reciboIndenizacaoNf.setIdReciboIndenizacaoNf(null);
						
						for (Object object : list) {
							ReciboIndenizacaoNf nf = (ReciboIndenizacaoNf)object;
							if(nf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento().equals(reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento())) {
								throw new BusinessException("LMS-28004");
							}
							
						}
						
						list.add(reciboIndenizacaoNf);
					}
					if (oldObject.getReciboIndenizacaoNfs()!=null)
						oldObject.getReciboIndenizacaoNfs().clear();
				}
				oldObject.setReciboIndenizacaoNfs(list);

				if (!areBothListsEqual || !areVlIndenizacaoEqual) {
					SessionContext.set("shouldGeneratePendenciaForNotaFiscalChange", Boolean.TRUE);
				}

		    }


			private boolean areBothListsEqual(List<ReciboIndenizacaoNf> reciboIndenizacaoNfs, List<ReciboIndenizacaoNf> list) {
				
				for (ReciboIndenizacaoNf recibo : reciboIndenizacaoNfs) {
					Long id = recibo.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento();
					boolean achou = find(list, id);
					if (!achou) {
						return false;
					}
				}

				for (ReciboIndenizacaoNf recibo : list) {
					Long id = recibo.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento();
					boolean achou = find(reciboIndenizacaoNfs, id);
					if (!achou) {
						return false;
					}
				}

				return true;
			}
			
			private boolean find(List<ReciboIndenizacaoNf> reciboIndenizacaoNfs, Long id) {

				for (ReciboIndenizacaoNf reciboIndenizacaoNf : reciboIndenizacaoNfs) {
					boolean achou = id.equals(reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento());
					if (achou) {
						return true;
					}
				}
				
				return false;
			}




    	};
    	
    	
    	/********************************************************************
    	 * MDA ItemListConfig
    	 ********************************************************************/
    	ItemListConfig mdaConfig = new ItemListConfig() {
			public List initialize(Long masterId) {
				return mdaSalvadoIndenizacaoService.findItensByIdReciboIndenizacao(masterId);				
			}
			public Integer getRowCount(Long masterId) {
				return mdaSalvadoIndenizacaoService.getRowCountItensByIdReciboIndenizacao(masterId);			
			}
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				
				TypedFlatMap tfm = (TypedFlatMap)parameters;

				MdaSalvadoIndenizacao mdaSalvadoIndenizacao = (MdaSalvadoIndenizacao) bean;
		    	Long idMda = tfm.getLong("mda.idDoctoServico");
		    	Long idMdaSalvadoIndenizacao = tfm.getLong("idMdaSalvadoIndenizacao");
		    	mdaSalvadoIndenizacao.setIdMdaSalvadoIndenizacao(idMdaSalvadoIndenizacao);
		    	Mda mda = mdaService.findById(idMda);
				
		    	MasterEntry masterEntry = getMasterFromSession(getMasterId(tfm), true);
		    	ItemList itensMda = masterEntry.getItems("mda");
		    	ItemListConfig configMda = getMasterConfig().getItemListConfig("mda");
		    	
		    	for(Iterator it = itensMda.iterator(getMasterId(tfm), configMda); it.hasNext(); ) {
		    		MdaSalvadoIndenizacao item = (MdaSalvadoIndenizacao)it.next();
		    		if (item.getMda().getIdDoctoServico().equals(idMda) && !item.getIdMdaSalvadoIndenizacao().equals(idMdaSalvadoIndenizacao)) {
		    			throw new BusinessException("LMS-21024");	
		    		}
		    	}
		    	
		    	Filial filialOrigem = filialService.findById(mda.getFilialByIdFilialOrigem().getIdFilial());
		    	mda.setFilialByIdFilialOrigem(filialOrigem);
		    	mdaSalvadoIndenizacao.setMda(mda);    
		    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);
		    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		    	mdaSalvadoIndenizacao.setReciboIndenizacao(reciboIndenizacao);
				mdaSalvadoIndenizacao.setReciboIndenizacao(getReciboIndenizacaoFromSession(getMasterId(tfm)));										
								
				return mdaSalvadoIndenizacao;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				MdaSalvadoIndenizacao newObject = (MdaSalvadoIndenizacao)newBean;
				MdaSalvadoIndenizacao oldObject = (MdaSalvadoIndenizacao)bean;
				oldObject.setIdMdaSalvadoIndenizacao(newObject.getIdMdaSalvadoIndenizacao());
				oldObject.setMda(newObject.getMda());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
		    }	
    	};
    	
    	/********************************************************************
    	 * Parcela ItemListConfig
    	 ********************************************************************/
    	ItemListConfig parcelaConfig = new ItemListConfig() {
			public List initialize(Long masterId) {
				return parcelaReciboIndenizacaoService.findItensByIdReciboIndenizacao(masterId);				
			}
			public Integer getRowCount(Long masterId, Map parameters) {
				return parcelaReciboIndenizacaoService.getRowCountItensByIdReciboIndenizacao(masterId);				
			}
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao) bean;

		    	parcelaReciboIndenizacao.setIdParcelaReciboIndenizacao(tfm.getLong("idParcelaReciboIndenizacao"));
		    	parcelaReciboIndenizacao.setDtVencimento(tfm.getYearMonthDay("dtVencimento"));    	
		    	parcelaReciboIndenizacao.setNrBoleto(tfm.getString("nrBoleto"));
		    	Moeda moeda = moedaService.findById(tfm.getLong("moeda.idMoeda"));
		    	parcelaReciboIndenizacao.setMoeda(moeda);
		    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);
		    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		    	parcelaReciboIndenizacao.setReciboIndenizacao(reciboIndenizacao);
		    	parcelaReciboIndenizacao.setTpStatusParcelaIndenizacao(new DomainValue("A"));
		    	parcelaReciboIndenizacao.setVlPagamento(tfm.getBigDecimal("vlPagamento"));		    	
				return parcelaReciboIndenizacao;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				ParcelaReciboIndenizacao newObject = (ParcelaReciboIndenizacao)newBean;
				ParcelaReciboIndenizacao oldObject = (ParcelaReciboIndenizacao)bean;
				
				oldObject.setIdParcelaReciboIndenizacao(newObject.getIdParcelaReciboIndenizacao());
				oldObject.setDtVencimento(newObject.getDtVencimento());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
				oldObject.setMoeda(newObject.getMoeda());
				oldObject.setNrBoleto(newObject.getNrBoleto());
				oldObject.setTpStatusParcelaIndenizacao(newObject.getTpStatusParcelaIndenizacao());
				oldObject.setVlPagamento(newObject.getVlPagamento());
		    }	
    	};

    	/********************************************************************
    	 * Anexo Rim ItemListConfig
    	 ********************************************************************/
    	ItemListConfig anexoConfig = new ItemListConfig() {
			public List initialize(Long masterId) {
				return anexoRimService.findItensByIdReciboIndenizacao(masterId);
			}
			
			public Integer getRowCount(Long masterId, Map parameters) {				
				return anexoRimService.getRowCountItensByIdReciboIndenizacao(masterId);				
			}
			
			public Map configItemDomainProperties() {
				return null;
			}

			public Object populateNewItemInstance(Map parameters, Object bean) {
				TypedFlatMap tfm = (TypedFlatMap)parameters;
				
				AnexoRim anexoRim = (AnexoRim) bean;

				anexoRim.setIdAnexoRim(tfm.getLong("idAnexoRim"));
				anexoRim.setDescAnexo((String) tfm.get("descAnexo"));
				anexoRim.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
				anexoRim.setUsuarioLMS(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
				try {
					anexoRim.setDcArquivo(Base64Util.decode(tfm.getString("dcArquivo")));
				} catch (IOException e) {
					throw new InfrastructureException(e.getMessage());
				}
				
				MasterEntry master = getMasterFromSession(getMasterId(tfm), true);
		    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		    	anexoRim.setReciboIndenizacao(reciboIndenizacao);
		    	
				return anexoRim;
			}
			
			public void modifyItemValues(Object newBean, Object bean) {
				AnexoRim newObject = (AnexoRim) newBean;
				AnexoRim oldObject = (AnexoRim)bean;
				
				oldObject.setIdAnexoRim(newObject.getIdAnexoRim());
				oldObject.setDhCriacao(newObject.getDhCriacao());
				oldObject.setReciboIndenizacao(newObject.getReciboIndenizacao());
				oldObject.setDcArquivo(newObject.getDcArquivo());
				oldObject.setDescAnexo(newObject.getDescAnexo());
				oldObject.setUsuarioLMS(newObject.getUsuarioLMS());
		    }	
    	};

    	masterConfig.addItemConfig("doctoServicoIndenizacao", DoctoServicoIndenizacao.class, itemConfig, comparator);
		masterConfig.addItemConfig("mda",                     MdaSalvadoIndenizacao.class, mdaConfig, comparator);
		masterConfig.addItemConfig("parcela",                 ParcelaReciboIndenizacao.class, parcelaConfig, comparator);
		masterConfig.addItemConfig("anexo", 				  AnexoRim.class, anexoConfig, comparator);
		SessionContext.set("shouldGeneratePendenciaForNotaFiscalChange", Boolean.FALSE);
    	return masterConfig;
	}	
	
    private ReciboIndenizacao getReciboIndenizacaoFromSession(Long idReciboIndenizacao) {
		MasterEntry master = getMasterFromSession(idReciboIndenizacao, true);
		return (ReciboIndenizacao) master.getMaster(); 
    }
	
	/************************************************************************************************************************
	 * Métodos relativos às abas de listagem e ao detalhamento
	 ************************************************************************************************************************/
	
	public Serializable storeRIM(TypedFlatMap tfm) {
		
		Long idReciboIndenizacao = tfm.getLong("idReciboIndenizacao");
		
		// dados do master
		MasterEntry master = getMasterFromSession(idReciboIndenizacao, true);
		ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) master.getMaster();
		ReciboIndenizacao copyForRollback = copyForRollBack(reciboIndenizacao);
		
		ItemList itens = getItemsFromSession(master, "doctoServicoIndenizacao");
		Boolean hasNotaFiscalModification = (Boolean) SessionContext.get("shouldGeneratePendenciaForNotaFiscalChange");
		boolean hasBeanModification = hasReciboIndenizacaoModification(reciboIndenizacao, tfm) || hasNotaFiscalModification;

		// se blSalvados=true então exige o itens de mda   
		Boolean blSalvados = ("S".equals(tfm.getString("blSalvados")) ? Boolean.TRUE : Boolean.FALSE);
		ItemList itensMda = getItemsFromSession(master, "mda");
		if (blSalvados.booleanValue() && !itensMda.hasItems()) {
			throw new BusinessException("LMS-21049");
		}
		
		// se formaPagamento=boleto e parcelas>1 exige itens de parcelas
		DomainValue tpFormaPagamento = tfm.getDomainValue("tpFormaPagamento");
		Byte qtParcelas = tfm.getByte("qtParcelasBoletoBancario");
		ItemList itensParcela = getItemsFromSession(master, "parcela"); 
		if ((tpFormaPagamento.getValue().equals("BO")) && (!itensParcela.hasItems())) {
			throw new BusinessException("LMS-21050");
		}
		
		ItemList itensAnexoRim = getItemsFromSession(master, "anexo");
		
		if (idReciboIndenizacao==null) {
			// só permite alterar, não cadastrar
			throw new BusinessException("LMS-21037");
		}

    	if (!("BO").equals(tfm.getDomainValue("tpFormaPagamento").getValue())) {
    		ItemListConfig configParcela = getMasterConfig().getItemListConfig("parcela");    		
    		List ids = new ArrayList();
    		for (Iterator it = itensParcela.iterator(idReciboIndenizacao, configParcela); it.hasNext(); ) {
    			ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao) it.next();
    			ids.add(parcelaReciboIndenizacao.getIdParcelaReciboIndenizacao());
    		}    		
    		removeItemByIds(ids, "parcela"); 
    	}
		
		// calcula a quantidade de parcelas 
		TypedFlatMap consultaParcelas = new TypedFlatMap();
    	consultaParcelas.put("reciboIndenizacao.idReciboIndenizacao", reciboIndenizacao.getIdReciboIndenizacao());
		
    	// item list de documentos de servico 
		ItemList itensDocto = getItemsFromSession(master, "doctoServicoIndenizacao");
		ItemListConfig configDocto = getMasterConfig().getItemListConfig("doctoServicoIndenizacao");

    	
    	// iterando sobre a item list de documentos
		BigDecimal vlTotalIndenizado = new BigDecimal(0);
		int qtVolumes = 0;
		
		// itera sobre a item list de documentos
		for (Iterator it = itensDocto.iterator((Long)master.getMasterId(), configDocto); it.hasNext(); ) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			
			// somando o valor total indenizado
			vlTotalIndenizado = vlTotalIndenizado.add(conversaoMoedaService.converteValor(doctoServicoIndenizacao.getMoeda().getIdMoeda(), reciboIndenizacao.getMoeda().getIdMoeda(), doctoServicoIndenizacao.getVlIndenizado(), reciboIndenizacao.getDtGeracao()));
			
		}
		
		this.validarFormaPagamento(master, reciboIndenizacao, qtParcelas, itensParcela,
				vlTotalIndenizado, tpFormaPagamento);
		
		// dados gerais
		Filial filial = SessionUtils.getFilialSessao();
		Usuario usuario = SessionUtils.getUsuarioLogado();
    	
    	BigDecimal vlSalarioMinimo = cotacaoIndicadorFinanceiroService.findVlCotacaoIndFinanceiro("SALMIN", SessionUtils.getPaisSessao().getIdPais(), JTDateTimeUtils.getDataAtual());
		
		ProcessoSinistro processoSinistro = null;
		Long idProcessoSinistro = tfm.getLong("idProcessoSinistro");
		if (idProcessoSinistro!=null) {
			processoSinistro = processoSinistroService.findProcessoSinistroById(idProcessoSinistro);
			reciboIndenizacao.setProcessoSinistro(processoSinistro);
		} 
		
		// dados gerais
		Moeda moeda = SessionUtils.getMoedaSessao();

		// tipo de indenizacao 
		String tpIndenizacaoValue = tfm.getString("tpIndenizacaoValue");
		reciboIndenizacao.setTpIndenizacao(new DomainValue(tpIndenizacaoValue));		

		// dados do beneficiario
		Pessoa beneficiario = new Pessoa();
		if (tfm.getLong("clienteBeneficiario.idCliente")!=null) 
			beneficiario.setIdPessoa(tfm.getLong("clienteBeneficiario.idCliente"));
		else if (tfm.getLong("beneficiarioTerceiro.idPessoa")!=null)
			beneficiario.setIdPessoa(tfm.getLong("beneficiarioTerceiro.idPessoa"));
		else if (tfm.getLong("filialBeneficiada.idFilial")!=null)
			beneficiario.setIdPessoa(tfm.getLong("filialBeneficiada.idFilial"));
		reciboIndenizacao.setPessoaByIdBeneficiario(beneficiario);
		reciboIndenizacao.setTpBeneficiarioIndenizacao(tfm.getDomainValue("tpBeneficiarioIndenizacao"));

		BigDecimal vlIndenizacao = tfm.getBigDecimal("vlIndenizacao");
		reciboIndenizacao.setVlIndenizacao(vlIndenizacao);
		
		// dados do favorecido
		Pessoa favorecido = new Pessoa();
		if (tfm.getLong("clienteFavorecido.idCliente")!=null)
			favorecido.setIdPessoa(tfm.getLong("clienteFavorecido.idCliente"));
		else if (tfm.getLong("favorecidoTerceiro.idPessoa")!=null)
			favorecido.setIdPessoa(tfm.getLong("favorecidoTerceiro.idPessoa"));
		else if (tfm.getLong("filialFavorecida.idFilial")!=null)
			favorecido.setIdPessoa(tfm.getLong("filialFavorecida.idFilial"));
			
		if (favorecido.getIdPessoa()!=null)
			reciboIndenizacao.setPessoaByIdFavorecido(favorecido);
		else 
			reciboIndenizacao.setPessoaByIdFavorecido(null);
		
		reciboIndenizacao.setTpFavorecidoIndenizacao(tfm.getDomainValue("tpFavorecidoIndenizacao"));
		
		reciboIndenizacao.setMoeda(moeda);
		
		reciboIndenizacao.setQtVolumesIndenizados(tfm.getInteger("qtVolumesIndenizados"));
		
		// dados gerais
		
		//LMS-666 REQ006 Criar campo Nota Fiscal de débito do cliente
		reciboIndenizacao.setNrNotaFiscalDebitoCliente(tfm.getLong("nrNotaFiscalDebitoCliente"));
		reciboIndenizacao.setBlSalvados("S".equals(tfm.getString("blSalvados")) ? Boolean.TRUE : Boolean.FALSE);
		reciboIndenizacao.setObReciboIndenizacao(tfm.getString("obReciboIndenizacao"));
		reciboIndenizacao.setTpFormaPagamento(tpFormaPagamento);
		reciboIndenizacao.setBlMaisUmaOcorrencia(Boolean.FALSE);
		reciboIndenizacao.setDtGeracao(JTDateTimeUtils.getDataAtual());
		reciboIndenizacao.setNrContaCorrente(tfm.getLong("nrContaCorrente"));
		reciboIndenizacao.setNrDigitoContaCorrente(tfm.getString("nrDigitoContaCorrente"));
		//LMS-666 REQ006 Segurado com seguro próprio
		reciboIndenizacao.setBlSegurado(tfm.getBoolean("blSegurado"));
		
		// e atualiza o recibo de indenizacao
		if (qtParcelas!=null) {
			reciboIndenizacao.setQtParcelasBoletoBancario(qtParcelas);
		}
		reciboIndenizacao.setUsuario(usuario);
		reciboIndenizacao.setTpStatusIndenizacao(new DomainValue("G"));
		reciboIndenizacao.setDtProgramadaPagamento(tfm.getYearMonthDay("dtProgramadaPagamento"));
		Long idBanco = tfm.getLong("banco.idBanco");
		if (idBanco!=null) {
			Banco banco = new Banco();
			banco.setIdBanco(idBanco);
			reciboIndenizacao.setBanco(banco);
		}
		Long idAgencia = tfm.getLong("agenciaBancaria.idAgenciaBancaria");
		if (idAgencia!=null) {
			AgenciaBancaria agenciaBancaria = new AgenciaBancaria();
			agenciaBancaria.setIdAgenciaBancaria(idAgencia);
			reciboIndenizacao.setAgenciaBancaria(agenciaBancaria);	
		}
		// se não for update, gera o número do recibo de reembolso
		if (reciboIndenizacao.getIdReciboIndenizacao()==null) {
			reciboIndenizacao.setFilial(filial);
			Long maxNrRecibo = configuracoesFacade.incrementaParametroSequencial(filial.getIdFilial(), "NR_REC_INDENIZACAO", true);
			reciboIndenizacao.setNrReciboIndenizacao(Integer.valueOf(maxNrRecibo.intValue()));
		}
		
		// iterando sobre a item list
		for (Iterator it = itensDocto.iterator((Long)master.getMasterId(), configDocto); it.hasNext(); ) {
			DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			List produtosCliente = produtoService.findProdutoClienteRemetenteByIdDoctoServico(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico());
			boolean hasProdutos = (produtosCliente.size()>0);
			
			// validando informações obrigatórias na item list
			if (doctoServicoIndenizacao.getMoeda()==null || doctoServicoIndenizacao.getVlIndenizado()==null || (hasProdutos && doctoServicoIndenizacao.getProduto()==null) || ("NC".equals(tpIndenizacaoValue) && doctoServicoIndenizacao.getDoctoServico()==null)) {
				if ("NC".equals(tpIndenizacaoValue)) { 
					throw new BusinessException("LMS-21015");
					
				} else { 
					throw new BusinessException("LMS-21014");
				}
			}
			
			// setando null no id das notas fiscais, forcando a inserir novo
			List notas = doctoServicoIndenizacao.getReciboIndenizacaoNfs();
			if (notas!=null) {
				for(Iterator i=notas.iterator(); i.hasNext(); ) {
					ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf)i.next();
					reciboIndenizacaoNf.setIdReciboIndenizacaoNf(null);
				}
			}
		}

	
		/*********************************************************************/
		try {
			getMaster().storeManter(reciboIndenizacao, itensDocto, itensMda, itensParcela, itensAnexoRim, configDocto, tfm, hasBeanModification);	
		} catch (RuntimeException e) {
			ReciboIndenizacao reciboIndenizacaoRestore = (ReciboIndenizacao) master.getMaster();
			copyProperties(reciboIndenizacaoRestore, copyForRollback);
			throw e;
		}

		itensDocto.resetItemsState();
		itensMda.resetItemsState();
		itensParcela.resetItemsState();
		itensAnexoRim.resetItemsState();
		updateMasterInSession(master);
		SessionContext.set("shouldGeneratePendenciaForNotaFiscalChange", Boolean.FALSE);
		/*********************************************************************/
		
		// dados retornados à tela após salvar
		TypedFlatMap result = new TypedFlatMap();
		result.put("idReciboIndenizacao", reciboIndenizacao.getIdReciboIndenizacao());
		result.put("nrReciboIndenizacao", reciboIndenizacao.getNrReciboIndenizacao());
		result.put("sgSimboloMoeda", reciboIndenizacao.getMoeda().getSiglaSimbolo());
		result.put("vlIndenizacao", reciboIndenizacao.getVlIndenizacao());
		result.put("qtVolumesIndenizados", reciboIndenizacao.getQtVolumesIndenizados());
		
		return result;
	}

	private ReciboIndenizacao copyForRollBack(ReciboIndenizacao reciboIndenizacao) {
		ReciboIndenizacao copyForRollback = null;
		
		try {
			copyForRollback = (ReciboIndenizacao) BeanUtilsBean.getInstance().cloneBean(reciboIndenizacao);
		} catch (IllegalAccessException e1) {
		} catch (InstantiationException e1) {
		} catch (InvocationTargetException e1) {
		} catch (NoSuchMethodException e1) {
		}

		return copyForRollback;
	}

	private void copyProperties(ReciboIndenizacao reciboIndenizacaoRestore, ReciboIndenizacao copyForRollaback) {
		reciboIndenizacaoRestore.setIdReciboIndenizacao(copyForRollaback.getIdReciboIndenizacao());
		reciboIndenizacaoRestore.setNrReciboIndenizacao(copyForRollaback.getNrReciboIndenizacao());
		reciboIndenizacaoRestore.setQtVolumesIndenizados(copyForRollaback.getQtVolumesIndenizados());
		reciboIndenizacaoRestore.setVlIndenizacao(copyForRollaback.getVlIndenizacao());
		reciboIndenizacaoRestore.setTpIndenizacao(copyForRollaback.getTpIndenizacao());
		reciboIndenizacaoRestore.setTpSituacaoWorkflow(copyForRollaback.getTpSituacaoWorkflow());
		reciboIndenizacaoRestore.setPendencia(copyForRollaback.getPendencia());
		reciboIndenizacaoRestore.setLoteJdeRim(copyForRollaback.getLoteJdeRim());
		reciboIndenizacaoRestore.setTpBeneficiarioIndenizacao(copyForRollaback.getTpBeneficiarioIndenizacao());
		reciboIndenizacaoRestore.setTpStatusIndenizacao(copyForRollaback.getTpStatusIndenizacao());
		reciboIndenizacaoRestore.setTpFormaPagamento(copyForRollaback.getTpFormaPagamento());
		reciboIndenizacaoRestore.setTpFavorecidoIndenizacao(copyForRollaback.getTpFavorecidoIndenizacao());
		reciboIndenizacaoRestore.setBlSalvados(copyForRollaback.getBlSalvados());
		reciboIndenizacaoRestore.setBlMaisUmaOcorrencia(copyForRollaback.getBlMaisUmaOcorrencia());
		reciboIndenizacaoRestore.setBlSegurado(copyForRollaback.getBlSegurado());
		reciboIndenizacaoRestore.setBlEmailPgto(copyForRollaback.getBlEmailPgto()); 
		reciboIndenizacaoRestore.setVersao(copyForRollaback.getVersao());
		reciboIndenizacaoRestore.setDtEmissao(copyForRollaback.getDtEmissao());
		reciboIndenizacaoRestore.setDtGeracao(copyForRollaback.getDtGeracao());    
		reciboIndenizacaoRestore.setDtProgramadaPagamento(copyForRollaback.getDtProgramadaPagamento());
		reciboIndenizacaoRestore.setDtLiberacaoPagamento(copyForRollaback.getDtLiberacaoPagamento());
		reciboIndenizacaoRestore.setDtPagamentoEfetuado(copyForRollaback.getDtPagamentoEfetuado());
		reciboIndenizacaoRestore.setDtDevolucaoBanco(copyForRollaback.getDtDevolucaoBanco());    
		reciboIndenizacaoRestore.setNrNotaFiscalDebitoCliente(copyForRollaback.getNrNotaFiscalDebitoCliente());
		reciboIndenizacaoRestore.setNrContaCorrente(copyForRollaback.getNrContaCorrente());
		reciboIndenizacaoRestore.setQtParcelasBoletoBancario(copyForRollaback.getQtParcelasBoletoBancario());
		reciboIndenizacaoRestore.setVlJuros(copyForRollaback.getVlJuros());
		reciboIndenizacaoRestore.setNrDigitoContaCorrente(copyForRollaback.getNrDigitoContaCorrente());
		reciboIndenizacaoRestore.setObReciboIndenizacao(copyForRollaback.getObReciboIndenizacao());
		reciboIndenizacaoRestore.setUsuario(copyForRollaback.getUsuario());
		reciboIndenizacaoRestore.setMoeda(copyForRollaback.getMoeda());
		reciboIndenizacaoRestore.setAgenciaBancaria(copyForRollaback.getAgenciaBancaria());
		reciboIndenizacaoRestore.setProcessoSinistro(copyForRollaback.getProcessoSinistro());
		reciboIndenizacaoRestore.setBanco(copyForRollaback.getBanco());
		reciboIndenizacaoRestore.setPessoaByIdFavorecido(copyForRollaback.getPessoaByIdFavorecido());
		reciboIndenizacaoRestore.setPessoaByIdBeneficiario(copyForRollaback.getPessoaByIdBeneficiario());
		reciboIndenizacaoRestore.setFilial(copyForRollaback.getFilial());
		reciboIndenizacaoRestore.setFilialDebitadas(copyForRollaback.getFilialDebitadas());
		reciboIndenizacaoRestore.setDoctoServicoIndenizacoes(copyForRollaback.getDoctoServicoIndenizacoes());
		reciboIndenizacaoRestore.setMdaSalvadoIndenizacoes(copyForRollaback.getMdaSalvadoIndenizacoes());
		reciboIndenizacaoRestore.setEventoRims(copyForRollaback.getEventoRims());
		reciboIndenizacaoRestore.setParcelaReciboIndenizacoes(copyForRollaback.getParcelaReciboIndenizacoes());
		reciboIndenizacaoRestore.setAvisoPagoRim(copyForRollaback.getAvisoPagoRim());
	}

	private boolean hasDoctoServicoModification(ItemList itens) {
		return !itens.getNewOrModifiedItems().isEmpty() || !itens.getRemovedItems().isEmpty();
	}

	private boolean hasReciboIndenizacaoModification(ReciboIndenizacao reciboIndenizacao,
			TypedFlatMap tfm) {

		Long clienteBeneficiarioIdNovo = extractBeneficiarioId(tfm);
		Long clienteBeneficiarioIdAntigo = reciboIndenizacao.getPessoaByIdBeneficiario().getIdPessoa();

		Long clienteFavorecidoIdNovo = extractFavorecidoId(tfm);
		Long clienteFavorecidoIdAntigo = extractIdFromPessoa(reciboIndenizacao.getPessoaByIdFavorecido());

		Moeda moedaIndenizacaoNovo = moedaService.findById(tfm.getLong("idMoedaHidden"));
		Moeda moedaIndenizacaoAntigo = reciboIndenizacao.getMoeda();

		BigDecimal vlIndenizacaoNovo = tfm.getBigDecimal("vlIndenizacao");
		BigDecimal vlIndenizacaoAntigo = reciboIndenizacao.getVlIndenizacao();

		String nrProcessoSinistroNovo = tfm.getString("nrProcessoSinistro");
		String nrProcessoSinistroAntigo = extractNumeroProcessoSinistro(reciboIndenizacao);

		return (!clienteBeneficiarioIdNovo.equals(clienteBeneficiarioIdAntigo)
				|| !clienteFavorecidoIdNovo.equals(clienteFavorecidoIdAntigo)
				|| !moedaIndenizacaoNovo.equals(moedaIndenizacaoAntigo)
				|| !(vlIndenizacaoNovo.compareTo(vlIndenizacaoAntigo) == 0) 
				|| !nrProcessoSinistroNovo.equals(nrProcessoSinistroAntigo));
	}


	private Long extractFavorecidoId(TypedFlatMap tfm) {
		DomainValue tpBeneficiarioIndenizacao = tfm.getDomainValue("tpFavorecidoIndenizacao");
		String beneficiarioIndenizacaoValue = tpBeneficiarioIndenizacao.getValue();

		Long id = null;
		if (beneficiarioIndenizacaoValue.equals("T")) {
			id = tfm.getLong("favorecidoTerceiro.idPessoa");
		} else {
			id = tfm.getLong("clienteFavorecido.idCliente");
		}
		
		if (id == null) {
			id = -1L;
		}
		
		return id;
	}

	private Long extractIdFromPessoa(Pessoa pessoa) {
		if (pessoa != null) {
			return pessoa.getIdPessoa();
		}
		return -1L;
	}

	
	private Long extractBeneficiarioId(TypedFlatMap tfm) {
		DomainValue tpBeneficiarioIndenizacao = tfm.getDomainValue("tpBeneficiarioIndenizacao");
		String beneficiarioIndenizacaoValue = tpBeneficiarioIndenizacao.getValue();

		if (beneficiarioIndenizacaoValue.equals("T")) {
			return tfm.getLong("beneficiarioTerceiro.idPessoa");
		} 
		return tfm.getLong("clienteBeneficiario.idCliente");
	}

	private String extractNumeroProcessoSinistro(ReciboIndenizacao reciboIndenizacao) {
		if (reciboIndenizacao.getProcessoSinistro() == null) {
			return "";
		}
		return reciboIndenizacao.getProcessoSinistro().getNrProcessoSinistro();
	}

	/**
	 * Valida se a forma de pagamento é boleto e a quantidade de parcelas esta correta juntamente com o valor.
	 * 
	 * @param master
	 * @param reciboIndenizacao
	 * @param qtParcelas
	 * @param itensParcela
	 * @param vlTotalIndenizado
	 * @param tpFormaPagamento
	 */
	private void validarFormaPagamento(MasterEntry master, ReciboIndenizacao reciboIndenizacao,
			Byte qtParcelas, ItemList itensParcela, BigDecimal vlTotalIndenizado, DomainValue tpFormaPagamento) {
		if ("BO".equals(tpFormaPagamento.getValue())) {
			ItemListConfig configParcela  = getMasterConfig().getItemListConfig("parcela");
			// itera sobre a item list de parcelas
			BigDecimal vlTotalParcelas = BigDecimal.ZERO;
			for (Iterator it = itensParcela.iterator((Long)master.getMasterId(), configParcela); it.hasNext(); ) {
				ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao)it.next();
				vlTotalParcelas = vlTotalParcelas.add(conversaoMoedaService.converteValor(parcelaReciboIndenizacao.getMoeda().getIdMoeda(), reciboIndenizacao.getMoeda().getIdMoeda(), parcelaReciboIndenizacao.getVlPagamento(), reciboIndenizacao.getDtGeracao()));
			}
			
			// se a quantidade de parcelas é diferente do informado
			if (qtParcelas.intValue() != itensParcela.size()) {
				throw new BusinessException("LMS-21052");
			// se o total de parcelas é diferente do total dos documentos
			} else if (vlTotalParcelas.compareTo(vlTotalIndenizado) != 0) {
				throw new BusinessException("LMS-21087");
			}
		}
	}
	
	private boolean isUsuarioFilial(Long idFilial) {
		return !historicoFilialService.validateFilialUsuarioMatriz(idFilial);
	}
	/**
	 * Find paginated
	 * @param tfm
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public ResultSetPage findPaginatedReciboIndenizacao(TypedFlatMap tfm) {		
		ResultSetPage rsp = getMaster().findPaginatedReciboIndenizacao(tfm);
		
		List list = new ArrayList();
		for(Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			Map map = new HashMap();
			
			ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao)it.next();
			map.put("idReciboIndenizacao", reciboIndenizacao.getIdReciboIndenizacao());
			map.put("nrReciboIndenizacao", reciboIndenizacao.getNrReciboIndenizacao());
			map.put("sgFilialReciboIndenizacao", reciboIndenizacao.getFilial().getSgFilial());
			map.put("dtGeracao", reciboIndenizacao.getDtGeracao());
			map.put("tpIndenizacao", reciboIndenizacao.getTpIndenizacao().getDescription().getValue());
			map.put("sgSimboloVlIndenizado", reciboIndenizacao.getMoeda().getSiglaSimbolo());
			map.put("vlIndenizado", reciboIndenizacao.getVlIndenizacao());
			map.put("tpStatusIndenizacao", reciboIndenizacao.getTpStatusIndenizacao().getDescription().getValue());
			
			Pessoa beneficiario = reciboIndenizacao.getPessoaByIdBeneficiario();

			if (beneficiario.getNrIdentificacao()!=null) {
				map.put("nrIdentificacaoBeneficiario", FormatUtils.formatIdentificacao(beneficiario));					
			}
			
			if ("F".equals(reciboIndenizacao.getTpBeneficiarioIndenizacao().getValue())) {
				map.put("nmBeneficiario", beneficiario.getNmFantasia());
			} else {
				map.put("nmBeneficiario", beneficiario.getNmPessoa());
			}
			
			list.add(map);
		}
		
		rsp.setList(list);
		return rsp;
	}
	
	/**
	 * GetRowCount
	 * @param tfm
	 * @return
	 */
	public Integer getRowCountReciboIndenizacao(TypedFlatMap tfm) {
		return getMaster().getRowCountReciboIndenizacao(tfm);
	}
	
	public TypedFlatMap findByIdReciboIndenizacao(Long idReciboIndenizacao) {
		
		ReciboIndenizacao reciboIndenizacao = getMaster().findById(idReciboIndenizacao);
    	putMasterInSession(reciboIndenizacao);
    	SessionContext.set("shouldGeneratePendenciaForNotaFiscalChange", Boolean.FALSE);
				
		TypedFlatMap result = new TypedFlatMap();
		if (reciboIndenizacao.getPendencia() != null) {
			result.put("idPendencia", reciboIndenizacao.getPendencia().getIdPendencia()); // Necessário para abrir a tela de visualizacao de fluxo de pendencia pelo botão.
		}
		result.put("idReciboIndenizacao", idReciboIndenizacao);
		result.put("sgFilialRecibo", reciboIndenizacao.getFilial().getSgFilial()); 
		result.put("nrReciboIndenizacao", reciboIndenizacao.getNrReciboIndenizacao());
		
		result.put("tpStatusIndenizacao", reciboIndenizacao.getTpStatusIndenizacao().getDescription().getValue());
		result.put("tpStatusIndenizacaoValue", reciboIndenizacao.getTpStatusIndenizacao().getValue());		
		
		result.put("tpIndenizacao", reciboIndenizacao.getTpIndenizacao().getDescription().getValue());
		result.put("tpIndenizacaoValue", reciboIndenizacao.getTpIndenizacao().getValue());
		
		if (reciboIndenizacao.getProcessoSinistro()!=null) {			
			result.put("nrReciboComposto", reciboIndenizacao.getFilial().getSgFilial()+"-"+ new DecimalFormat("00000000").format(reciboIndenizacao.getNrReciboIndenizacao()));
			result.put("idProcessoSinistro", reciboIndenizacao.getProcessoSinistro().getIdProcessoSinistro());
			result.put("nrProcessoSinistro", reciboIndenizacao.getProcessoSinistro().getNrProcessoSinistro()); 
			if (reciboIndenizacao.getProcessoSinistro().getTipoSeguro()!=null)
				reciboIndenizacao.getProcessoSinistro().getTipoSeguro().getIdTipoSeguro();
				result.put("tipoSeguro", reciboIndenizacao.getProcessoSinistro().getTipoSeguro().getSgTipo());			
		}		
		
		
		result.put("blSegurado", reciboIndenizacao.getBlSegurado());
		result.put("nrNotaFiscalDebitoCliente", reciboIndenizacao.getNrNotaFiscalDebitoCliente());
		result.put("sgSimboloMoeda", reciboIndenizacao.getMoeda().getSiglaSimbolo());
		result.put("vlIndenizacao", reciboIndenizacao.getVlIndenizacao());
		result.put("qtVolumesIndenizados", reciboIndenizacao.getQtVolumesIndenizados());
		result.put("blSalvados", reciboIndenizacao.getBlSalvados());
		result.put("obReciboIndenizacao", reciboIndenizacao.getObReciboIndenizacao());

		if (reciboIndenizacao.getTpBeneficiarioIndenizacao()!=null && reciboIndenizacao.getPessoaByIdBeneficiario()!=null) {
			String tpBeneficiario = reciboIndenizacao.getTpBeneficiarioIndenizacao().getValue();
			result.put("tpBeneficiarioIndenizacao", tpBeneficiario);
			
			// se remetente, detinatario ou consignatario
			if (tpBeneficiario.equals("R") || tpBeneficiario.equals("D") || tpBeneficiario.equals("C") || tpBeneficiario.equals("V") ) {
				result.put("clienteBeneficiario.idCliente", reciboIndenizacao.getPessoaByIdBeneficiario().getIdPessoa());
				result.put("clienteBeneficiario.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(reciboIndenizacao.getPessoaByIdBeneficiario()));
				result.put("clienteBeneficiario.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(reciboIndenizacao.getPessoaByIdBeneficiario()));
				result.put("clienteBeneficiario.pessoa.nmPessoa", reciboIndenizacao.getPessoaByIdBeneficiario().getNmPessoa());
				
				
			// se terceiro
			} else if (tpBeneficiario.equals("T")) {
				Pessoa beneficiario = pessoaService.findById(reciboIndenizacao.getPessoaByIdBeneficiario().getIdPessoa());
				result.put("beneficiarioTerceiro.idPessoa", beneficiario.getIdPessoa());
				result.put("beneficiarioTerceiro.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(beneficiario));
				result.put("beneficiarioTerceiro.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(beneficiario));				
				result.put("beneficiarioTerceiro.nmPessoa", beneficiario.getNmPessoa());
				
			// se filial
			} else {
				Filial beneficiario = filialService.findById(reciboIndenizacao.getPessoaByIdBeneficiario().getIdPessoa());
				result.put("filialBeneficiada.idFilial", beneficiario.getIdFilial());
				result.put("filialBeneficiada.sgFilial", beneficiario.getSgFilial());
				result.put("filialBeneficiada.pessoa.nmFantasia", beneficiario.getPessoa().getNmFantasia());
				
			}
		}
			
		
		if (reciboIndenizacao.getTpFavorecidoIndenizacao()!=null ) {
			String tpFavorecido = reciboIndenizacao.getTpFavorecidoIndenizacao().getValue();
			result.put("tpFavorecidoIndenizacao", tpFavorecido);
			if (reciboIndenizacao.getPessoaByIdFavorecido()!=null) {
				// se remetente, detinatario ou consignatario
				if (tpFavorecido.equals("R") || tpFavorecido.equals("D") || tpFavorecido.equals("C") || tpFavorecido.equals("V") ) {
					result.put("clienteFavorecido.idCliente", reciboIndenizacao.getPessoaByIdFavorecido().getIdPessoa());
					result.put("clienteFavorecido.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(reciboIndenizacao.getPessoaByIdFavorecido()));
					result.put("clienteFavorecido.pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(reciboIndenizacao.getPessoaByIdFavorecido()));					
					result.put("clienteFavorecido.pessoa.nmPessoa", reciboIndenizacao.getPessoaByIdFavorecido().getNmPessoa());				
					
				// se terceiro
				} else if (tpFavorecido.equals("T")) {
					Pessoa favorecido = pessoaService.findById(reciboIndenizacao.getPessoaByIdFavorecido().getIdPessoa());
					result.put("favorecidoTerceiro.idPessoa", favorecido.getIdPessoa());
					result.put("favorecidoTerceiro.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(favorecido));
					result.put("favorecidoTerceiro.pessoa.nrIdentificacao", FormatUtils.formatIdentificacao(favorecido));
					result.put("favorecidoTerceiro.nmPessoa", favorecido.getNmPessoa());
					
				// se filial
				} else {
					Filial favorecido = filialService.findById(reciboIndenizacao.getPessoaByIdFavorecido().getIdPessoa());
					result.put("filialFavorecida.idFilial", favorecido.getIdFilial());
					result.put("filialFavorecida.sgFilial", favorecido.getSgFilial());
					result.put("filialFavorecida.pessoa.nmFantasia", favorecido.getPessoa().getNmFantasia());				
				}
			}
		}
		
		String tpFormaPagamento = reciboIndenizacao.getTpFormaPagamento().getValue();
		
		result.put("tpFormaPagamento", tpFormaPagamento);
		result.put("dtProgramadaPagamento", reciboIndenizacao.getDtProgramadaPagamento());
		result.put("dtDevolucaoBanco", reciboIndenizacao.getDtDevolucaoBanco());		
		result.put("dtPagamentoEfetuado", reciboIndenizacao.getDtPagamentoEfetuado());
		
		if (reciboIndenizacao.getBanco()!=null) {
			result.put("banco.idBanco", reciboIndenizacao.getBanco().getIdBanco());
			result.put("banco.nrBanco", reciboIndenizacao.getBanco().getNrBanco());
			result.put("banco.nmBanco", reciboIndenizacao.getBanco().getNmBanco());
		}
		
		if (reciboIndenizacao.getAgenciaBancaria()!=null) {
			result.put("agenciaBancaria.idAgenciaBancaria", reciboIndenizacao.getAgenciaBancaria().getIdAgenciaBancaria());
			result.put("agenciaBancaria.nrAgenciaBancaria", reciboIndenizacao.getAgenciaBancaria().getNrAgenciaBancaria());
			result.put("agenciaBancaria.nmAgenciaBancaria", reciboIndenizacao.getAgenciaBancaria().getNmAgenciaBancaria());
		}
		
		result.put("nrContaCorrente", reciboIndenizacao.getNrContaCorrente());
		result.put("nrDigitoContaCorrente", reciboIndenizacao.getNrDigitoContaCorrente());
		
		// se forma de pagamento for boleto, então detalha dados do boleto		
		if ("BO".equals(tpFormaPagamento)) {			
			result.put("qtParcelasBoletoBancario", reciboIndenizacao.getQtParcelasBoletoBancario());
			result.put("qtParcelasBoletoBancarioHidden", reciboIndenizacao.getQtParcelasBoletoBancario());
		}
				
		result.put("tpSituacaoWorkFlow", reciboIndenizacao.getTpSituacaoWorkflow().getDescription().getValue());
		result.put("tpSituacaoWorkFlowValue", reciboIndenizacao.getTpSituacaoWorkflow().getValue());		
		result.put("dtEmissao", reciboIndenizacao.getDtEmissao());
		result.put("dtLiberacaoPgto", reciboIndenizacao.getDtLiberacaoPagamento());
		result.put("siglaNumeroAnexoRim", reciboIndenizacao.getFilial().getSgFilial()+"-"+ new DecimalFormat("00000000").format(reciboIndenizacao.getNrReciboIndenizacao()));
		
		return result;
	}
	
	/************************************************************************************************************************
	 * Métodos relativos à aba de Filial Debitada
	 ************************************************************************************************************************/
	public ResultSetPage findPaginatedFilialDebitada(TypedFlatMap tfm) {		
		List list = new ArrayList();
		tfm.put("_currentPage", "1");
		tfm.put("_pageSize", "10000");
		ResultSetPage rsp = filialDebitadaService.findPaginatedFilialDebitadaReciboIndenizacao(tfm);
		
		Map mapFilial = new HashMap();
		for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			FilialDebitada filialDebitada = (FilialDebitada)it.next();

			String sgFilial = filialDebitada.getFilial().getSgFilial();
			if (mapFilial.containsKey(sgFilial))
				continue;
			else
				mapFilial.put(sgFilial, sgFilial);
			
			TypedFlatMap result = new TypedFlatMap();
			result.put("idFilialDebitada", filialDebitada.getIdFilialDebitada());
			result.put("sgFilial", sgFilial);
			result.put("nmFantasia", filialDebitada.getFilial().getPessoa().getNmFantasia());
			result.put("pcDebitado", Long.valueOf(Math.round(filialDebitada.getPcDebitado().doubleValue())));
			result.put("vlReembolso", filialDebitada.getVlReembolso());
			if (filialDebitada.getMoeda()!=null)
				result.put("sgSimboloVlReembolso", filialDebitada.getMoeda().getSiglaSimbolo());
			list.add(result);
		}
		
		rsp.setList(list);
		return rsp;
	}
	
	public Integer getRowCountFilialDebitada(TypedFlatMap tfm) {
		return filialDebitadaService.getRowCountFilialDebitadaReciboIndenizacao(tfm);
	}


	public TypedFlatMap storeFilialDebitada(TypedFlatMap tfm) {
		FilialDebitada filialDebitada = new FilialDebitada();
		
		Long idFilialDebitada = tfm.getLong("idFilialDebitada");
		if (idFilialDebitada != null) {
			filialDebitada = filialDebitadaService.findById(idFilialDebitada);
		}

		ReciboIndenizacao reciboIndenizacao = new ReciboIndenizacao();
    	reciboIndenizacao.setIdReciboIndenizacao(tfm.getLong("reciboIndenizacao.idReciboIndenizacao"));
    	reciboIndenizacao.setVersao(Integer.valueOf(0));

    	Filial filialReembolso = null;
		if (tfm.getLong("filialReembolsada.idFilial")!=null) {
			filialReembolso = new Filial();
			filialReembolso.setIdFilial(tfm.getLong("filialReembolsada.idFilial"));		
		}

		Filial filial = new Filial();
		filial.setIdFilial(tfm.getLong("filial.idFilial"));		

		BigDecimal vlReembolso = tfm.getBigDecimal("vlReembolso");
		Moeda moeda = null;
		if (vlReembolso != null) {
			moeda = new Moeda();		
			moeda.setIdMoeda(tfm.getLong("moeda.idMoeda"));
		}
		
		filialDebitada.setFilialReembolso(filialReembolso);
		filialDebitada.setReciboIndenizacao(reciboIndenizacao);
		filialDebitada.setFilial(filial);
		filialDebitada.setVlReembolso(vlReembolso);
		filialDebitada.setMoeda(moeda);
		filialDebitada.setDtDadoReembolso(tfm.getYearMonthDay("dtDadoReembolso"));
		filialDebitada.setDtReembolso(tfm.getYearMonthDay("dtReembolso"));
		filialDebitada.setPcDebitado(tfm.getBigDecimal("pcDebitado"));
		filialDebitadaService.store(filialDebitada);
		
		TypedFlatMap result = new TypedFlatMap();
		result.put("idFilialDebitada", filialDebitada.getIdFilialDebitada());
		return result;
	}


	/**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeFiliaisDebitadasByIds(List ids) {
		filialDebitadaService.removeByIds(ids);
	}
	
	public TypedFlatMap findByIdFilialDebitada(Long idFilialDebitada) {
		FilialDebitada filialDebitada = filialDebitadaService.findById(idFilialDebitada);
		TypedFlatMap result = new TypedFlatMap();
		result.put("idFilialDebitada", idFilialDebitada);
		result.put("filial.idFilial", filialDebitada.getFilial().getIdFilial());
		result.put("filial.sgFilial", filialDebitada.getFilial().getSgFilial());
		result.put("filial.pessoa.nmFantasia", filialDebitada.getFilial().getPessoa().getNmFantasia());
		result.put("pcDebitado", Long.valueOf(Math.round(filialDebitada.getPcDebitado().doubleValue())));

		if (filialDebitada.getFilialReembolso()!=null) {
			result.put("filialReembolsada.idFilial", filialDebitada.getFilialReembolso().getIdFilial());
			result.put("filialReembolsada.sgFilial", filialDebitada.getFilialReembolso().getSgFilial());
			result.put("filialReembolsada.pessoa.nmFantasia", filialDebitada.getFilialReembolso().getPessoa().getNmFantasia());
		}
		
		if (filialDebitada.getMoeda()!=null) {
			result.put("moeda.idMoeda", filialDebitada.getMoeda().getIdMoeda());
		}
	
		result.put("vlReembolso", filialDebitada.getVlReembolso());
		result.put("dtDadoReembolso", filialDebitada.getDtDadoReembolso());
		result.put("dtReembolso", filialDebitada.getDtReembolso());
	
		return result;
	}
	/************************************************************************************************************************
	 * Métodos relativos à aba de Mda 
	 ************************************************************************************************************************/
    public ResultSetPage findPaginatedMda(TypedFlatMap tfm) {
    	ResultSetPage rsp = super.findPaginatedItemList(tfm, "mda");
    	List list = new ArrayList();
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		MdaSalvadoIndenizacao mdaSalvadoIndenizacao = (MdaSalvadoIndenizacao)it.next();
    		Map map = new HashMap(5);
    		map.put("idMdaSalvadoIndenizacao", mdaSalvadoIndenizacao.getIdMdaSalvadoIndenizacao());
    		map.put("sgFilial", mdaSalvadoIndenizacao.getMda().getFilialByIdFilialOrigem().getSgFilial());
    		map.put("nrMda", mdaSalvadoIndenizacao.getMda().getNrDoctoServico());
    		map.put("dhInclusao", mdaSalvadoIndenizacao.getMda().getDhInclusao());
    		map.put("idMda", mdaSalvadoIndenizacao.getMda().getIdDoctoServico());
    		list.add(map);
    	}
    	rsp.setList(list);
    	return rsp;
    }
	
    /**
     * Método responsável por localizar o registro da tabela ANEXO_RIM pelo campo ID_ANEXO_RIM
     */
	public TypedFlatMap findByIdAnexoRim(MasterDetailKey key) {
		TypedFlatMap result = new TypedFlatMap();
		AnexoRim anexoRim = (AnexoRim) super.findItemById(key, "anexo");
		
    	result.put("idAnexoRim", anexoRim.getIdAnexoRim());
    	result.put("idAnexoRimIndenizacao", anexoRim.getIdAnexoRim());
		result.put("nomeArquivo", ArquivoUtils.extrairNomeDoBlob((byte[]) anexoRim.getDcArquivo()));
    	result.put("nomeUsuario", anexoRim.getUsuarioLMS().getUsuarioADSM().getNmUsuario());
    	result.put("dtHoraCriacao", anexoRim.getDhCriacao());
    	result.put("descAnexo", anexoRim.getDescAnexo());
    	result.put("dcArquivo", Base64Util.encode(anexoRim.getDcArquivo()));
    	result.put("idReciboIndenizacao", anexoRim.getReciboIndenizacao().getIdReciboIndenizacao());
    	
    	return result;
	}
    
	/**
	 * Método responsável por buscar os registros da tabela ANEXO_RIM para exibir na grid da aba Anexos. 
	 */
	public ResultSetPage findPaginatedAnexoRim(TypedFlatMap tfm) {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		String nomeArquivo = "";
		
		ResultSetPage rsp = super.findPaginatedItemList(tfm, "anexo");
		
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		AnexoRim anexoRim = (AnexoRim) it.next();
    		TypedFlatMap map = new TypedFlatMap();

    		nomeArquivo = ArquivoUtils.getNomeArquivo(anexoRim.getDcArquivo());
    		map.put("nomeArquivo", nomeArquivo);    	
    		map.put("descricaoAnexo", anexoRim.getDescAnexo());
    		map.put("dtHoraCriacao", anexoRim.getDhCriacao());
    		map.put("nomeUsuario", anexoRim.getUsuarioLMS().getUsuarioADSM().getNmUsuario());
    		map.put("idAnexoRim", anexoRim.getIdAnexoRim());
    		
    		list.add(map);
    	}
    	
    	rsp.setList(list);
    	
    	return rsp;
	}
	
	/**
	 * Método responsável por salvar o registro na tabela ANEXO_RIM
	 */
	public Serializable storeAnexoRim(TypedFlatMap tfm) {
		Long idReciboIndenizacao = Long.parseLong((String) tfm.get("masterId"));
    	ReciboIndenizacao reciboIndenizacao = reciboIndenizacaoService.findReciboIndenizacaoById(idReciboIndenizacao);
    	
    	AnexoRim anexoRim = new AnexoRim();
    	anexoRim.setReciboIndenizacao(reciboIndenizacao);
    	anexoRim.setUsuarioLMS(usuarioLMSService.findById(SessionUtils.getUsuarioLogado().getIdUsuario()));
    	
    	anexoRim.setDescAnexo((String) tfm.get("descAnexo"));
    	anexoRim.setDhCriacao(JTDateTimeUtils.getDataHoraAtual());
    	
    	try {
			anexoRim.setDcArquivo(Base64Util.decode((String) tfm.getString("dcArquivo")));
		} catch (IOException e) {
			throw new InfrastructureException(e.getMessage());
		}
		
		Long retorno = (Long) anexoRimService.store(anexoRim);													
		
    	return retorno;    	
	}
	
	public Serializable saveAnexoRim(TypedFlatMap tfm) {
    	return saveItemInstance(tfm, "anexo");
    }
	
	/**
	 * Método responsável por buscar a quantidade de registros da tabela ANEXO_RIM para exibição na grid.
	 */
	public Integer getRowCountAnexoRim(TypedFlatMap tfm) {
		return super.getRowCountItemList(tfm, "anexo");
	}
	
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeAnexoRimByIds(List<Long> ids){
		super.removeItemByIds(ids, "anexo");
	}
    
    public Integer getRowCountMda(TypedFlatMap tfm) {
    	return super.getRowCountItemList(tfm, "mda");
    }
	
    public Serializable saveMDA(TypedFlatMap tfm) {
    	return saveItemInstance(tfm, "mda");
    }
	
    /**
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeMdaByIds(List ids) {
    	super.removeItemByIds(ids, "mda");
    }
	
    public TypedFlatMap findByIdMda(MasterDetailKey key) {
    	MdaSalvadoIndenizacao mdaSalvadoIndenizacao = (MdaSalvadoIndenizacao) super.findItemById(key, "mda"); 
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idMdaSalvadoIndenizacao", mdaSalvadoIndenizacao.getIdMdaSalvadoIndenizacao());
    	result.put("filialByIdFilialOrigem.idFilial", mdaSalvadoIndenizacao.getMda().getFilialByIdFilialOrigem().getIdFilial());
    	result.put("filialByIdFilialOrigem.sgFilial", mdaSalvadoIndenizacao.getMda().getFilialByIdFilialOrigem().getSgFilial());
    	result.put("mda.idDoctoServico", mdaSalvadoIndenizacao.getMda().getIdDoctoServico());
    	result.put("mda.nrDoctoServico", mdaSalvadoIndenizacao.getMda().getNrDoctoServico());
    	result.put("dhInclusao", mdaSalvadoIndenizacao.getMda().getDhInclusao());
    	return result;
    }
    
	
	/************************************************************************************************************************
	 * Métodos relativos à aba de Parcelas 
	 ************************************************************************************************************************/
    public ResultSetPage findPaginatedParcela(TypedFlatMap tfm) {
    	ResultSetPage rsp = super.findPaginatedItemList(tfm, "parcela");
    	List list = new ArrayList();
    	for (Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
    		ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao)it.next();
    		Map map = new HashMap(5);
    		map.put("idParcelaReciboIndenizacao", parcelaReciboIndenizacao.getIdParcelaReciboIndenizacao());
    		map.put("nrBoleto", parcelaReciboIndenizacao.getNrBoleto());
    		map.put("dtVencimento", parcelaReciboIndenizacao.getDtVencimento());
    		map.put("sgSimboloParcela", parcelaReciboIndenizacao.getMoeda().getSiglaSimbolo());
    		map.put("vlPagamento", parcelaReciboIndenizacao.getVlPagamento()); 
    		list.add(map);
    	}
    	rsp.setList(list);
    	return rsp;
    }
    
    public Integer getRowCountParcela(TypedFlatMap tfm) {
    	return super.getRowCountItemList(tfm, "parcela");
    }
    
    public Serializable saveParcela(TypedFlatMap tfm) {
    	Long idParcelaReciboIndenizacao = (Long) super.saveItemInstance(tfm, "parcela");
    	
    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);		
		ItemList itemList = getItemsFromSession(master, "parcela");		    	
    	
    	TypedFlatMap result = new TypedFlatMap();
   		result.put("idParcelaReciboIndenizacao", idParcelaReciboIndenizacao);
    	result.put("qtParcelasBoletoBancario", Integer.valueOf(itemList.size()));
    	result.put("nrBoleto", tfm.getString("nrBoleto"));
    	result.put("dtVencimento", tfm.getYearMonthDay("dtVencimento"));
    	return result;
    }
    	
    public TypedFlatMap removeParcelasByIds(TypedFlatMap tfm) {
    	MasterEntry master = getMasterFromSession(getMasterId(tfm), true);		
		ItemList itemList = getItemsFromSession(master, "parcela");
		List ids = new ArrayList();
		for (Iterator it = tfm.getList("ids").iterator(); it.hasNext(); ) {
			ids.add(Long.valueOf((String)it.next()));
		}
		super.removeItemByIds(ids, "parcela");
		TypedFlatMap result = new TypedFlatMap();
    	result.put("rowCount", Integer.valueOf(itemList.size()));    	
    	return result;
    }
    
	
    public TypedFlatMap findByIdParcela(MasterDetailKey key) {
    	ParcelaReciboIndenizacao parcelaReciboIndenizacao = (ParcelaReciboIndenizacao) super.findItemById(key, "parcela");
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idParcelaReciboIndenizacao", parcelaReciboIndenizacao.getIdParcelaReciboIndenizacao());
    	result.put("nrBoleto", parcelaReciboIndenizacao.getNrBoleto());
    	result.put("dtVencimento", parcelaReciboIndenizacao.getDtVencimento());
    	result.put("moeda.idMoeda", parcelaReciboIndenizacao.getMoeda().getIdMoeda());
    	result.put("vlPagamento", parcelaReciboIndenizacao.getVlPagamento());
    	return result;
    }
    
	
	/************************************************************************************************************************
	 * Métodos relativos à itemList 
	 ************************************************************************************************************************/

	/**
	 * Store da tela de documentos do processo
	 * @param tfm
	 */
	public void saveItem(TypedFlatMap tfm) {
		
    	Long idDoctoServicoIndenizacao = tfm.getLong("idDoctoServicoIndenizacao");
    	Long idDoctoServico = tfm.getLong("doctoServico.idDoctoServico");
    	
    	MasterEntry master      = getMasterFromSession(getMasterId(tfm), false);
		ItemList itens          = getItemsFromSession(master, "doctoServicoIndenizacao");
		ItemListConfig config   = getMasterConfig().getItemListConfig("doctoServicoIndenizacao");
		
		if (itens.hasItems()) {
			for (Iterator it=itens.iterator(getMasterId(tfm), config); it.hasNext(); ) {
				DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
				
				// no caso de inclusão, não permite documentos de servico repetidos
				if (idDoctoServicoIndenizacao==null && idDoctoServico.equals(doctoServicoIndenizacao.getDoctoServico().getIdDoctoServico())) {
					throw new BusinessException("LMS-21031");
				}

				// documento de servico a incluir 
	    		DoctoServico doctoServicoIncluido = doctoServicoService.findByIdJoinFilial(idDoctoServico);
	        	Long idFilialOrigemIncluido = doctoServicoIncluido.getFilialByIdFilialOrigem().getIdFilial();
	        	Long idFilialDestinoIncluido = doctoServicoIncluido.getFilialByIdFilialDestino() == null ? null : doctoServicoIncluido.getFilialByIdFilialDestino().getIdFilial();
	        	
	        	// documento de servico da itemList
	        	DoctoServico doctoServicoItemList = doctoServicoIndenizacao.getDoctoServico();
	        	Long idFilialOrigemItemList = doctoServicoItemList.getFilialByIdFilialOrigem().getIdFilial();
	        	Long idFilialDestinoItemList = doctoServicoItemList.getFilialByIdFilialDestino() == null ? null : doctoServicoItemList.getFilialByIdFilialDestino().getIdFilial();
	        	
    			// se a filial origem recebida for diferente da filial origem do docto da itemList ou 
    			// se a filial destino recebida for diferente da filial destino do docto da itemList
    			// ou ainda de uma se uma das filiais destino for nula e a outra não,
    			// então dispara uma exceção
    			if (!idFilialOrigemIncluido.equals(idFilialOrigemItemList) 
    					|| (idFilialDestinoIncluido==null | idFilialDestinoItemList==null) // sim, isso é um xor
    					|| (idFilialDestinoIncluido!=null && !idFilialDestinoIncluido.equals(idFilialDestinoItemList)) ) {
    				throw new BusinessException("LMS-21030");
    			}
    				
    			// verifica se o documento de servico já não consta na tabela
    	    	if (idDoctoServicoIndenizacao==null && (doctoServicoIndenizacaoService.getRowCountDoctoServicoIndenizacaoNaoCanceladoByIdDoctoServico(idDoctoServico)).intValue() > 0) {    	
    	    		throw new BusinessException("LMS-21027");    		
    	    	}
			}
		}
		
		// A quantidade de volumes informada não pode ser maior que a quantidade de volumes do Documento de Serviço.
		DoctoServico doctoServico = doctoServicoService.findById(tfm.getLong("doctoServico.idDoctoServico"));
		Integer qtVolumes = tfm.getInteger("qtVolumesIndenizados");
		if (qtVolumes==null){
			qtVolumes = Integer.valueOf(0);
		}
		Integer qtVolumesDocto = doctoServico.getQtVolumes();
		if (qtVolumesDocto==null){
			qtVolumesDocto = Integer.valueOf(0);
		}
		if (qtVolumes.intValue() > qtVolumesDocto.intValue()){
			throw new BusinessException("LMS-21039");
		}

		
		BigDecimal vlDocumentoServico = doctoServico.getVlMercadoria()!=null ? doctoServico.getVlMercadoria() : new BigDecimal(0.00);
		Moeda moedaDocto = doctoServico.getMoeda();
		BigDecimal vlIndenizacao = tfm.getBigDecimal("vlIndenizacao");
		Moeda moedaIndenizacao = moedaService.findById(tfm.getLong("moeda.idMoeda"));
		BigDecimal vlDocumentoConvertido = conversaoMoedaService.converteValor(moedaDocto.getIdMoeda(), moedaIndenizacao.getIdMoeda(), vlDocumentoServico, JTDateTimeUtils.getDataAtual());

		// dados gerais
		Filial filial = SessionUtils.getFilialSessao();
		boolean isFilialMatriz = historicoFilialService.validateFilialUsuarioMatriz(filial.getIdFilial());
		// verifica se nao for Matriz eo valor da indenização é maior que o valor do documento de servico
		if (!isFilialMatriz && CompareUtils.gt(vlIndenizacao, vlDocumentoConvertido)) {
			throw new BusinessException("LMS-21016");
		}
		
		//LMS-666 Salvar rotas
		String[] rotas = tfm.getString("rotasId").split("\\|");
		if(rotas!=null && rotas.length == 2){
			tfm.put("filialSinistro.idFilial", Long.valueOf(rotas[0]));
			tfm.put("rotaSinistro.idFilial",Long.valueOf(rotas[1]));
		}
		
		saveItemInstance(tfm, "doctoServicoIndenizacao");
	}
	
	/**
	 * @param key Realiza o detalhamento da aba de documentos
	 * @return
	 */
    public TypedFlatMap findItemById(MasterDetailKey key) {
    	DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao) findItemById(key, "doctoServicoIndenizacao");    	
    	DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();    	
    	TypedFlatMap tfm = new TypedFlatMap();
    	tfm.put("idDoctoServicoIndenizacao", doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
    	tfm.put("doctoServico.idDoctoServico", doctoServico.getIdDoctoServico());
    	tfm.put("doctoServico.tpDocumentoServico", doctoServico.getTpDocumentoServico().getDescription().getValue());
    	tfm.put("doctoServico.filialByIdFilialOrigem.sgFilial", doctoServico.getFilialByIdFilialOrigem().getSgFilial());
    	tfm.put("doctoServico.nrDoctoServico", doctoServico.getNrDoctoServico());
    	
    	ReciboIndenizacao reciboIndenizacao = (ReciboIndenizacao) getMasterFromSession(key.getMasterId(), false).getMaster();
    	if(doctoServicoIndenizacao.getOcorrenciaNaoConformidade()!=null){
    		tfm.put("ocorrenciaNaoConformidade.idOcorrenciaNaoConformidade", doctoServicoIndenizacao.getOcorrenciaNaoConformidade().getIdOcorrenciaNaoConformidade());
    	}
    	if(doctoServicoIndenizacao.getFilialSinistro()!=null && doctoServicoIndenizacao.getRotaSinistro()!=null){
    		tfm.put("rotasId", doctoServicoIndenizacao.getFilialSinistro().getIdFilial()+"|"+doctoServicoIndenizacao.getRotaSinistro().getIdFilial());
    	}
    	
    	Integer qtVolumes;
    	if (doctoServicoIndenizacao.getIdDoctoServicoIndenizacao() != null) {
    		qtVolumes = doctoServicoIndenizacao.getQtVolumes();
    	}
    	else {
	    	if (doctoServicoIndenizacao.getQtVolumes()==null) {
	    		qtVolumes = doctoServico.getQtVolumes();
	    	} else {
	    		qtVolumes = doctoServicoIndenizacao.getQtVolumes();
	    	}
    	}
    	tfm.put("qtVolumesIndenizados", qtVolumes);
    	
    	if (doctoServicoIndenizacao.getProduto()!=null) 
    		tfm.put("produto.idProduto", doctoServicoIndenizacao.getProduto().getIdProduto());
    	
    	if (doctoServicoIndenizacao.getMoeda()!=null) 
    		tfm.put("moeda.idMoeda", doctoServicoIndenizacao.getMoeda().getIdMoeda());
    	
    	tfm.put("vlIndenizacao", doctoServicoIndenizacao.getVlIndenizado());
    	
    	String tpIndenizacao = "";
    	if (doctoServicoIndenizacao.getReciboIndenizacao() != null) {
    		tpIndenizacao = doctoServicoIndenizacao.getReciboIndenizacao().getTpIndenizacao().getValue();

    		if (doctoServicoIndenizacao.getReciboIndenizacao().getProcessoSinistro() != null && "PS".equals(tpIndenizacao)) {
    			tfm.put("nrProcessoSinistro", doctoServicoIndenizacao.getReciboIndenizacao().getProcessoSinistro().getNrProcessoSinistro());
    			tfm.put("idProcessoSinistro", doctoServicoIndenizacao.getReciboIndenizacao().getProcessoSinistro().getIdProcessoSinistro());
    	}
    	}
    	
    	NaoConformidade naoConformidade = naoConformidadeService.findByIdDoctoServicoJoinOcorrencias(doctoServico.getIdDoctoServico());
    	//LMS-666 Combo de ocorrencias nao conformidade
    	List ocorrenciasIn = null;
    	if (naoConformidade != null && ("".equals(tpIndenizacao) || "NC".equals(tpIndenizacao))) {
	    	tfm.put("naoConformidade.filial.sgFilial", naoConformidade.getFilial().getSgFilial());
	    	tfm.put("naoConformidade.nrRnc", new DecimalFormat("00000000").format(naoConformidade.getNrNaoConformidade()));
	    	tfm.put("naoConformidade.idNaoConformidade", naoConformidade.getIdNaoConformidade());
	    	//LMS-666 Combo de ocorrencias nao conformidade
	    	ocorrenciasIn = naoConformidade.getOcorrenciaNaoConformidades();
    	}
    	
    	if (ocorrenciasIn!=null) {
    		List ocorrenciasOut = new ArrayList();
    		Integer totalOcorrencias = Integer.valueOf(0);
    		for(Iterator it = ocorrenciasIn.iterator(); it.hasNext(); ) {
    			OcorrenciaNaoConformidade ocorrenciaNaoConformidade = (OcorrenciaNaoConformidade)it.next();
    			if (ocorrenciaNaoConformidade.getMotivoAberturaNc() != null && ocorrenciaNaoConformidade.getMotivoAberturaNc().getBlPermiteIndenizacao()) {
    			Map map = new HashMap();
    			map.put("idOcorrencia", ocorrenciaNaoConformidade.getIdOcorrenciaNaoConformidade());
    	    	map.put("dsMotivo", ocorrenciaNaoConformidade.getNrOcorrenciaNc()+" - "+ocorrenciaNaoConformidade.getMotivoAberturaNc().getDsMotivoAbertura());
    	    	ocorrenciasOut.add(map);
    	    	totalOcorrencias = totalOcorrencias + ocorrenciaNaoConformidade.getQtVolumes();
    		}
    		}
    		tfm.put("motivosOcorrencia", ocorrenciasOut);
    	}
    	
    	String tpPrejuizo = doctoServicoIndenizacao.getTpPrejuizo()!=null?doctoServicoIndenizacao.getTpPrejuizo().getValue():"";
    	putNotasFiscaisIntoTypedFlatMap(doctoServico.getIdDoctoServico(), doctoServico.getTpDocumentoServico().getValue(), tpPrejuizo, tfm, doctoServicoIndenizacao.getReciboIndenizacaoNfs());
    	
    	//LMS-666 Criacao da combo de rotas de sinistro
    	doctoServico = doctoServicoService.findById(doctoServico.getIdDoctoServico());
    	// monta lista de combo de rotas
    	List rotas = new ArrayList();
    	
    	if(doctoServico.getFluxoFilial()!=null){
    		List ordensFluxoFilial = ordemFilialFluxoService.findByIdFluxoFilial(doctoServico.getFluxoFilial().getIdFluxoFilial());
	    	
	    	if(ordensFluxoFilial!=null){
				for(int i=0; i<ordensFluxoFilial.size()-1; i++) {
					Map map = new HashMap();
					OrdemFilialFluxo ordemFilialOrigem = (OrdemFilialFluxo)ordensFluxoFilial.get(i);
					OrdemFilialFluxo ordemFilialDestino = (OrdemFilialFluxo)ordensFluxoFilial.get(i+1);
					String idFilialOrigemDestino = ordemFilialOrigem.getFilial().getIdFilial()+"|"+ordemFilialDestino.getFilial().getIdFilial();
					String labelFilialOrigemDestino = ordemFilialOrigem.getNrOrdem() + " - "+ ordemFilialOrigem.getFilial().getSgFilial()+" --> "+
					ordemFilialDestino.getNrOrdem() + " - "+ ordemFilialDestino.getFilial().getSgFilial();
					//label ORDEM - FILIAL --> ORDEM - FILIAL
					map.put("idFilialOrigemDestino", idFilialOrigemDestino);
					map.put("labelFilialOrigemDestino", labelFilialOrigemDestino);
					rotas.add(map);
				}
	    	}	
    	}
    	
    	tfm.put("rotas", rotas);
    	return tfm;
    }
    
    /**
     * Põe notas fiscais no TypedFlatMap a retornar para a tela.  
     * @param idDoctoServico
     * @param tpDocumentoServico
     * @param tpPrejuizo
     * @param tfm
     * @param reciboIndenizacaoNfs
     */
    private void putNotasFiscaisIntoTypedFlatMap(Long idDoctoServico, String tpDocumentoServico, String tpPrejuizo, TypedFlatMap tfm, List reciboIndenizacaoNfs) {
    	// se documento de servico for conhecimento ou nota fiscal de transporte, então apresenta as notas fiscais
    	if (tpDocumentoServico.equals("CTR") || tpDocumentoServico.equals("NFT") || tpDocumentoServico.equals("CRT")
   			 || tpDocumentoServico.equals("CTE") || tpDocumentoServico.equals("NFS") || tpDocumentoServico.equals("NTE")
			 || tpDocumentoServico.equals("NSE")) {
    		// listbox source de notas fiscais
    		List notasFiscaisSource = findNotasFiscaisByIdConhecimento(idDoctoServico);
    		tfm.put("notasFiscaisSource", notasFiscaisSource);
    		// se prejuizo for total e lista de notas do doctoServicoIndenizacao estiver vazia,
    		// então preenche as duas listboxes com todas as notas fiscais
    		if ("T".equals(tpPrejuizo) && reciboIndenizacaoNfs==null) {
    			tfm.put("notasFiscais", notasFiscaisSource);
    			
    		// se prejuizo nao for total ou se houver notas na lista de doctoServicoIndenizacao 
    		} else if (reciboIndenizacaoNfs!=null) { 

    			// listbox destino de notas fiscais    		
        		List notasFiscais = new ArrayList();
        		for (Iterator it = reciboIndenizacaoNfs.iterator(); it.hasNext(); ) {
	    			ReciboIndenizacaoNf reciboIndenizacaoNf = (ReciboIndenizacaoNf)it.next();
	    			Map map = new HashMap();
	    			map.put("idNotaFiscalConhecimento", reciboIndenizacaoNf.getNotaFiscalConhecimento().getIdNotaFiscalConhecimento());
	    			map.put("nrNotaFiscal", reciboIndenizacaoNf.getNotaFiscalConhecimento().getNrNotaFiscal());
	    			notasFiscais.add(map);
	    		} tfm.put("notasFiscais", notasFiscais);
    		}
    	} 
    }
    
	/**
	 * Obtém as notas fiscais do conhecimento
	 * @param idConhecimento
	 * @return
	 */
	private List findNotasFiscaisByIdConhecimento(Long idConhecimento) {
		List notas = new ArrayList();
		for (Iterator it = notaFiscalConhecimentoService.findByConhecimento(idConhecimento).iterator(); it.hasNext(); ) {
			NotaFiscalConhecimento notaFiscalConhecimento = (NotaFiscalConhecimento)it.next();
			Map map = new HashMap();
			map.put("idNotaFiscalConhecimento", notaFiscalConhecimento.getIdNotaFiscalConhecimento());
			map.put("nrNotaFiscal", notaFiscalConhecimento.getNrNotaFiscal());
			notas.add(map);
		} 
		return notas;
	}

	
	public ResultSetPage findPaginatedDocumentos(TypedFlatMap tfm) {
		 ResultSetPage rsp = findPaginatedItemList(tfm, "doctoServicoIndenizacao");
		 List list = new ArrayList();
		 for(Iterator it = rsp.getList().iterator(); it.hasNext(); ) {
			 DoctoServicoIndenizacao doctoServicoIndenizacao = (DoctoServicoIndenizacao)it.next();
			 ReciboIndenizacao reciboIndenizacao = getMaster().findById(doctoServicoIndenizacao.getReciboIndenizacao().getIdReciboIndenizacao());
			 DoctoServico doctoServico = doctoServicoIndenizacao.getDoctoServico();
			 
			 
			 Map<String, Object> map = new HashMap<String, Object>();
			 map.put("idDoctoServicoIndenizacao", doctoServicoIndenizacao.getIdDoctoServicoIndenizacao());
			 map.put("tpDoctoServico", doctoServicoIndenizacao.getDoctoServico().getTpDocumentoServico());
			 map.put("sgFilialOrigemDocto", doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialOrigem().getSgFilial());
			 map.put("nrDoctoServico", doctoServicoIndenizacao.getDoctoServico().getNrDoctoServico());
			 map.put("nmClienteRemetente", doctoServicoIndenizacao.getDoctoServico().getClienteByIdClienteRemetente().getPessoa().getNmPessoa());
			 map.put("sgSimboloTotalDocto", doctoServicoIndenizacao.getDoctoServico().getMoeda().getSiglaSimbolo());
			 map.put("vlTotalDoctoServico", doctoServicoIndenizacao.getDoctoServico().getVlMercadoria());
			 map.put("vlIndenizado", doctoServicoIndenizacao.getVlIndenizado());
			 map.put("qtVolumes", doctoServicoIndenizacao.getQtVolumes());
			 
			 NaoConformidade naoConformidade = naoConformidadeService.findByIdDoctoServicoJoinOcorrencias(doctoServico.getIdDoctoServico());
			 if (naoConformidade!=null) {
				 map.put("sgFilialNaoConformidade", naoConformidade.getFilial().getSgFilial());
				 map.put("nrNaoConformidade", naoConformidade.getNrNaoConformidade());
			 }

			 if (doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialDestino()!=null)
				 map.put("sgFilialDestinoDocto", doctoServicoIndenizacao.getDoctoServico().getFilialByIdFilialDestino().getSgFilial());
			 if (doctoServicoIndenizacao.getDoctoServico().getClienteByIdClienteDestinatario()!=null)
				 map.put("nmClienteDestinatario", doctoServicoIndenizacao.getDoctoServico().getClienteByIdClienteDestinatario().getPessoa().getNmPessoa());
			 if (doctoServicoIndenizacao.getMoeda()!=null)
				 map.put("sgSimboloVlIndenizado", doctoServicoIndenizacao.getMoeda().getSiglaSimbolo());
			 
			 if ("NC".equals(reciboIndenizacao.getTpIndenizacao().getValue())) {
				 if (naoConformidade!=null && !naoConformidade.getOcorrenciaNaoConformidades().isEmpty() ) {
					 // lista apenas a primeira ocorrencia de nao conformidade da nao conformidade
					 
					 OcorrenciaNaoConformidade ocorrenciaNaoConformidade = (OcorrenciaNaoConformidade)naoConformidade.getOcorrenciaNaoConformidades().get(0);
					 if (ocorrenciaNaoConformidade.getManifesto()!=null) {
						 Manifesto manifesto = manifestoService.findById(ocorrenciaNaoConformidade.getManifesto().getIdManifesto());
						 map.put("sgFilialManifesto", manifesto.getFilialByIdFilialOrigem().getSgFilial());
						 // se manifesto viagem nacional
						 if (manifesto.getManifestoViagemNacional()!=null) {
							 map.put("nrManifesto", manifesto.getManifestoViagemNacional().getNrManifestoOrigem());
						 // se manifesto internacional
						 } else if (manifesto.getManifestoInternacional()!=null) {
							 map.put("nrManifesto", manifesto.getManifestoInternacional().getNrManifestoInt());
						 // se manifesto de entrega
						 } else if (manifesto.getManifestoEntrega()!=null) {
							 map.put("nrManifesto", manifesto.getManifestoEntrega().getNrManifestoEntrega());
						 }					 
					 }
					 
					 if (ocorrenciaNaoConformidade.getControleCarga()!=null) {
						 ControleCarga controleCarga = controleCargaService.findById(ocorrenciaNaoConformidade.getControleCarga().getIdControleCarga());
						 map.put("sgFilialControleCarga", controleCarga.getFilialByIdFilialOrigem().getSgFilial());
						 map.put("nrControleCarga", controleCarga.getNrControleCarga());
					 }
				 }
			 }

			 if (doctoServicoIndenizacao.getProduto()!=null)
				 map.put("dsProduto", doctoServicoIndenizacao.getProduto().getDsProduto());
			 
			 list.add(map);
		 }
		 rsp.setList(list);
		 return rsp;

	}
	
	public Integer getRowCountDocumentos(TypedFlatMap tfm) {
		return getRowCountItemList(tfm, "doctoServicoIndenizacao");
	}
	
	/**
	 *  Busca os eventos (RIM)
	 * 
	 * @param tfm
	 * @return ResultSetPage
	 */
	public ResultSetPage findPaginatedEventoRimByFilial(TypedFlatMap tfm ) {
		Long idRim = tfm.getLong("reciboIndenizacao.idReciboIndenizacao");
		idRim = tfm.getLong("idReciboIndenizacao");
		return getEventoRimService().findDoctoServicosByIdReciboIndenizacao(idRim, FindDefinition.createFindDefinition(tfm));
	}
	
	public Integer getRowCountEventosRIM(TypedFlatMap tfm ) {
		Long idRim = tfm.getLong("reciboIndenizacao.idReciboIndenizacao");
		idRim = tfm.getLong("idReciboIndenizacao");
		return getEventoRimService().getRowCountEventoRimByIdReciboIndenizacao(idRim);
	}

	/**
	 * Persiste o Evento
	 * 
	 * @param parameters
	 * @return
	 */
	public Serializable saveEventoRim(Map parameters, String tipoEvento){

        return getEventoRimService().saveEvntDocumentosRecebidos(parameters, tipoEvento);
	}
	
	/**
	 * Salva o recebimento dos documentos pela matriz
	 * 
	 * @param parameters
	 * @return
	 */
	public Serializable saveEvntDocumentosRecebidos(Map parameters){

		//Seta o tpEventoRIM
		String tipoEvento = "RM";

		return saveEventoRim(parameters, tipoEvento); 
	}
	
	/**
	 * Executa o pagamento manual da indenização SE
	 * já não estiver pago 'P', cancelado 'C' ou se
	 * também não houver um evento de pagamento 
	 * manual 'PM' relacionado ao RIM
	 * 
	 * @param parameters
	 * @return
	 */
	public Serializable storeEvntPagamentoRim(Map parameters){

		/*
		 * Verificar se RECIBO_INDENIZACAO TP_STATUS_INDENIZACAO seja 
		 * igual a "P" ou "C", caso positivo, emitir mensagem LMS-21072:
		 *  "RIM não pode ser pago. Verifique se já não está pago ou cancelado."
		 *  
		 * Retorna para a Tela de detalhamento.
		 */
		if (getEventoRimService().findStatusIndenizacao(parameters)) {
			throw new BusinessException("LMS-21072");
		}
		
		/*
		 * Verificar nos eventos do RIM (EVENTO_RIM) se já houve pagamento manual, 
		 * ou seja, Se existe um registro com TP_EVENTO_INDENIZACAO = "PM" para 
		 * este Recibo. Caso já exista emitir mensagem LMS-21070:
		 *  "Já existe um pagamento manual para este Recibo de indenização". 
		 *  
		 * Retorna para a Tela de detalhamento.
		 */
		if (getEventoRimService().findEventoRimByIdReciboIndenizacao(parameters)) {
			throw new BusinessException("LMS-21070");
		}		

		//atualiza o status  da indenização
		Long idReciboIndenizacao = Long.parseLong((String) parameters.get("idReciboIndenizacao"));
		getReciboIndenizacaoService().updateReciboIndenizacaRimPago(idReciboIndenizacao, "P");
		
		//Seta o tpEventoRIM = PM => Pagamento Manual
		String tipoEvento = "PM";

		//persiste o evento
		return saveEventoRim(parameters, tipoEvento);
	}


	/*********************************************************************************************
	 * General Finders 
	 *********************************************************************************************/
	public TypedFlatMap findMoedaPadrao(TypedFlatMap tfm) {
		Moeda moeda = SessionUtils.getMoedaSessao();
		TypedFlatMap result = new TypedFlatMap();
		result.put("moeda.idMoeda", moeda.getIdMoeda());
		return result;
	}
	
    /**
     * Obtém valores para a combo de Produtos
     * @param tfm
     * @return
     */
    public TypedFlatMap findComboProdutoByIdDoctoServico(TypedFlatMap tfm) {
    	Long idDoctoServico = tfm.getLong("idDoctoServico");
    	List produtosOut = new ArrayList();
    	List produtosIn = produtoService.findProdutoClienteRemetenteByIdDoctoServico(idDoctoServico);
    	for (Iterator it = produtosIn.iterator(); it.hasNext(); ) {
    		Produto produto = (Produto)it.next();
    		TypedFlatMap map = new TypedFlatMap();
    		map.put("idProduto", produto.getIdProduto());
    		map.put("dsProduto", produto.getDsProduto());
    		map.put("tpSituacao.value", produto.getTpSituacao().getValue());    		
    		produtosOut.add(map);
    	}
    	TypedFlatMap result = new TypedFlatMap();
    	result.put("idProduto", tfm.getLong("idProduto"));
    	result.put("produtos", produtosOut);
    	return result;
    }
	
	public List findComboMoeda(Map criteria) {
		List retorno = new ArrayList();
		List listMoedas = this.getMoedaService().find(criteria);
		for (Iterator iter = listMoedas.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Moeda moeda = (Moeda) iter.next();
			map.put("idMoeda", moeda.getIdMoeda());
			map.put("siglaSimbolo", moeda.getSiglaSimbolo());
			map.put("tpSituacao.value", moeda.getTpSituacao().getValue());
			retorno.add(map);
		}
		return retorno;
	}
	
	public List findLookupPessoa(TypedFlatMap tfm) {
		tfm.put("nrIdentificacao", tfm.getString("pessoa.nrIdentificacao"));

		List result = new ArrayList();
		for(Iterator iterator = this.pessoaService.findLookup(tfm).iterator(); iterator.hasNext(); ) {
			Pessoa pessoa = (Pessoa)iterator.next();
			Map map = new HashMap();
			map.put("idPessoa", pessoa.getIdPessoa());
			map.put("nrIdentificacao", pessoa.getNrIdentificacao());
			map.put("nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(pessoa));
			map.put("nmPessoa", pessoa.getNmPessoa());
			result.add(map);
		}
		return result;
	}

	public List findLookupBanco(TypedFlatMap tfm) {
    	return this.bancoService.findLookup(tfm);
    }
    
    public List findLookupAgencia(TypedFlatMap tfm) {
    	return this.agenciaBancariaService.findLookup(tfm);
    }

    public List findLookupCliente(Map criteria){    	
    	List<Map> list = new ArrayList<Map>(); 
    	for (Iterator it = clienteService.findLookup(criteria).iterator(); it.hasNext(); ) {
    		Cliente cliente = (Cliente)it.next();
    		TypedFlatMap map = new TypedFlatMap();
    		map.put("idCliente", cliente.getIdCliente());
    		map.put("pessoa.nrIdentificacao", cliente.getPessoa().getNrIdentificacao());
    		map.put("pessoa.nrIdentificacaoFormatado", FormatUtils.formatIdentificacao(cliente.getPessoa()));
    		map.put("pessoa.nmPessoa", cliente.getPessoa().getNmPessoa());
    		list.add(map);
    	}
    	return list;
    }
	
	public List findLookupFilial(TypedFlatMap tfm) {
    	List list = this.filialService.findLookup(tfm);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		typedFlatMap.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;

	}
	
    public List findComboMotivoAbertura(TypedFlatMap tfm) {
    	List list = new ArrayList();
    	for(Iterator it = motivoAberturaNcService.findOrderByDsMotivoAbertura(tfm).iterator(); it.hasNext(); ) {
    		MotivoAberturaNc motivoAberturaNc = (MotivoAberturaNc)it.next();
    		TypedFlatMap map = new TypedFlatMap();
    		map.put("idMotivoAberturaNc", motivoAberturaNc.getIdMotivoAberturaNc());
    		map.put("dsMotivoAbertura", motivoAberturaNc.getDsMotivoAbertura());
			map.put("tpSituacao.value", motivoAberturaNc.getTpSituacao().getValue());
    		list.add(map);
    	}
    	return list;
    }
	
	public List findLookupNaoConformidade(TypedFlatMap tfm) {
		List list = new ArrayList();
		TypedFlatMap r = new TypedFlatMap();
		r.put("filial.idFilial", tfm.getLong("filial.idFilial"));		
		r.put("nrNaoConformidade", tfm.getLong("nrNaoConformidade"));
		for(Iterator it=naoConformidadeService.findLookup(r).iterator(); it.hasNext(); ) {
			NaoConformidade naoConformidade = (NaoConformidade)it.next();
			TypedFlatMap item = new TypedFlatMap();
			item.put("filial.idFilial", naoConformidade.getFilial().getIdFilial());
			item.put("filial.sgFilial", naoConformidade.getFilial().getSgFilial());
			item.put("idNaoConformidade", naoConformidade.getIdNaoConformidade());
			item.put("nrNaoConformidade", naoConformidade.getNrNaoConformidade());
			list.add(item);
		}
		return list;
	}
	
    /**
     * Obtém o processo de sinistro, partindo do princípio que o usuário digitará da seguinte forma:<br>
     * @param tfm
     * @return
     */
    public List findLookupProcessoSinistro(TypedFlatMap tfm) {
    	TypedFlatMap resultData = new TypedFlatMap();
		List result = new ArrayList();
		for (Iterator it = this.processoSinistroService.findLookup(tfm).iterator(); it.hasNext();) {
			ProcessoSinistro processoSinistro = (ProcessoSinistro)it.next();
			resultData.put("idProcessoSinistro", processoSinistro.getIdProcessoSinistro());
			resultData.put("nrProcessoSinistro", processoSinistro.getNrProcessoSinistro());
			resultData.put("processoSinistro.tipoSeguro.idTipoSeguro", processoSinistro.getTipoSeguro().getIdTipoSeguro());			
			result.add(resultData);
		}
		return result;
    }
    
    
    /**
     * Obtém dados bancarios da pessoa
     * @param tfm
     * @return
     */
    public TypedFlatMap findDadosBancariosPessoa(TypedFlatMap tfm) {
    	Long idBeneficiario = tfm.getLong("idPessoa");
    	TypedFlatMap result = new TypedFlatMap();
    	if (idBeneficiario!=null) {
    		List list = contaBancariaService.findContasBancariasByPessoa(idBeneficiario);
    		if (list.size()>0) {
    			ContaBancaria contaBancaria = (ContaBancaria)list.get(0);
	    		result.put("favorecido.idPessoa", contaBancaria.getPessoa().getIdPessoa());
	    		result.put("favorecido.nmPessoa", contaBancaria.getPessoa().getNmPessoa());
	    		result.put("favorecido.nrIdentificacao", FormatUtils.formatIdentificacao(contaBancaria.getPessoa()));
	    		result.put("banco.idBanco", contaBancaria.getAgenciaBancaria().getBanco().getIdBanco());
	    		result.put("banco.nrBanco", contaBancaria.getAgenciaBancaria().getBanco().getNrBanco());
	    		result.put("banco.nmBanco", contaBancaria.getAgenciaBancaria().getBanco().getNmBanco());
	    		result.put("agenciaBancaria.idAgenciaBancaria", contaBancaria.getAgenciaBancaria().getIdAgenciaBancaria());
	    		result.put("agenciaBancaria.nrAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNrAgenciaBancaria());
	    		result.put("agenciaBancaria.nmAgenciaBancaria", contaBancaria.getAgenciaBancaria().getNmAgenciaBancaria());
	    		result.put("conta.nrContaCorrente", contaBancaria.getNrContaBancaria());
	    		result.put("conta.nrDigitoContaCorrente", contaBancaria.getDvContaBancaria());
    		}
    	}
    	return result;
    }
    
    public TypedFlatMap getMoedaSessao() {
    	TypedFlatMap tfm = new TypedFlatMap();
    	Moeda moeda = SessionUtils.getMoedaSessao();
    	tfm.put("sgSimboloMoeda", moeda.getSiglaSimbolo());
    	tfm.put("idMoeda", moeda.getIdMoeda());
    	return tfm;
    }


	/*********************************************************************************************
	 * Documento de Servico Methods
	 *********************************************************************************************/

    /**
     * FindLookup para a tag DoctoServico.
     */  
    public List findLookupServiceDocumentNumberCTE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNSE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNTE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFS(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCTR(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberCRT(Map criteria) {    	
    	return conhecimentoService.findLookup(criteria);
    }    
    public List findLookupServiceDocumentNumberMDA(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberRRE(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }
    public List findLookupServiceDocumentNumberNFT(Map criteria) {
    	return conhecimentoService.findLookup(criteria);
    }

    public List findLookupServiceDocumentFilialCTR(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCRT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialMDA(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialRRE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFT(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNFS(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    public List findLookupServiceDocumentFilialNTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialNSE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }
    public List findLookupServiceDocumentFilialCTE(Map criteria) {
    	return findLookupFilialByDocumentoServico(criteria);
    }  
    
    public List findLookupFilialByDocumentoServico(Map criteria) {
    	List list = this.filialService.findLookup(criteria);
    	List retorno = new ArrayList();
    	for (Iterator iter = list.iterator(); iter.hasNext();) {
    		Filial filial = (Filial)iter.next();
    		TypedFlatMap typedFlatMap = new TypedFlatMap();
    		typedFlatMap.put("idFilial", filial.getIdFilial());
    		typedFlatMap.put("sgFilial", filial.getSgFilial());
    		retorno.add(typedFlatMap);
    	}
    	return retorno;
    }
    
    public List findTipoDocumentoServico(Map criteria) {
    	List dominiosValidos = new ArrayList();
    	dominiosValidos.add("CTR");
    	dominiosValidos.add("CRT");
    	dominiosValidos.add("NFT");
    	dominiosValidos.add("NFS");
    	dominiosValidos.add("NTE");
    	dominiosValidos.add("NSE");
    	dominiosValidos.add("CTE");
    	List retorno = this.domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
    	return retorno;
    }
    
	public List findLookupMDA(TypedFlatMap criteria) {
		Long nrDoctoServico = criteria.getLong("nrDoctoServico");
		Long idFilialOrigem = criteria.getLong("filialByIdFilialOrigem.idFilial");
		List result = mdaService.findMdaByNrDoctoServicoByIdFilialOrigem(nrDoctoServico, idFilialOrigem);
    	List listMda = new ArrayList();
    	for (Iterator iter = result.iterator(); iter.hasNext();) {
			Mda mda = (Mda) iter.next();			
    		TypedFlatMap mapMda = new TypedFlatMap();    		
    		mapMda.put("idDoctoServico", mda.getIdDoctoServico());
    		mapMda.put("nrDoctoServico", mda.getNrDoctoServico());
    		mapMda.put("dhInclusao", mda.getDhInclusao());
    		listMda.add(mapMda);    		
    	}
    	return listMda;		
	}
	
	public TypedFlatMap findDhInclusaoDocumento(Long idDocumentoServico) {
		DoctoServico doctoServico = doctoServicoService.findById(idDocumentoServico);
		TypedFlatMap result = new TypedFlatMap();
		result.put("dhInclusao", doctoServico.getDhInclusao());
		return result;
	}
    
	public List findComboTipoSeguro(TypedFlatMap tfm) {
		List retorno = new ArrayList();
		List tiposSeguro = this.tipoSeguroService.findOrderBySgTipo(tfm); 
		for (Iterator iter = tiposSeguro.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			TipoSeguro tipoSeguro = (TipoSeguro) iter.next();
			map.put("idTipoSeguro", tipoSeguro.getIdTipoSeguro());
			map.put("sgTipo", tipoSeguro.getSgTipo());
			map.put("tpSituacao.value", tipoSeguro.getTpSituacao().getValue());
			retorno.add(map);
		}
		return retorno;
	}
	
	public TypedFlatMap findQtde(TypedFlatMap tfm) {
		Long idNaoConformidade = tfm.getLong("idNaoConformidade");
    	Long idOcorrencia = tfm.getLong("idOcorrenciaNaoConformidade");
    	Integer totalOcorrencias = 0;
    	if (idOcorrencia != null) {
    		OcorrenciaNaoConformidade ocorrenciaNaoConformidade = ocorrenciaNaoConformidadeService.findById(idOcorrencia);
    		totalOcorrencias = ocorrenciaNaoConformidade.getQtVolumes();	
    	}
    	tfm.put("qtVolumesIndenizados", totalOcorrencias);
    	return tfm;
    }
	
	public TypedFlatMap findFilialById(Long idFilial) {
		Filial filial = filialService.findById(idFilial);
		TypedFlatMap result = new TypedFlatMap();		
		result.put("filial.sgFilial", filial.getSgFilial());
		result.put("filial.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		return result;
	}
	
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		if (parameters.getLong("idMda")!=null)
			parameters.put("mda.idDoctoServico", parameters.getLong("idMda"));
		return this.reportServiceSupport.executeReport(parameters);
	}
	
	
	public void validateDtVencimento(TypedFlatMap parameters) {
		YearMonthDay dtVencimento = parameters.getYearMonthDay("dtVencimento");
    	if (dtVencimento != null && JTDateTimeUtils.comparaData(dtVencimento, JTDateTimeUtils.getDataAtual()) < 0) {
    		throw new BusinessException("LMS-00009");
    	}
	}
	
	public void clearSessionItens(){
		super.removeMasterFromSession();
	}
	
    public void setMaster(ReciboIndenizacaoService reciboIndenizacaoService) {
    	super.setMasterService(reciboIndenizacaoService);
    }
    private ReciboIndenizacaoService getMaster() {
    	return (ReciboIndenizacaoService)super.getMasterService();
    }
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setNaoConformidadeService(NaoConformidadeService naoConformidadeService) {
		this.naoConformidadeService = naoConformidadeService;
	}
	public void setProcessoSinistroService(ProcessoSinistroService processoSinistroService) {
		this.processoSinistroService = processoSinistroService;
	}
	public void setTipoSeguroService(TipoSeguroService tipoSeguroService) {
		this.tipoSeguroService = tipoSeguroService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setMotivoAberturaNcService(MotivoAberturaNcService motivoAberturaNcService) {
		this.motivoAberturaNcService = motivoAberturaNcService;
	}
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}
	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}
	public void setProdutoService(ProdutoService produtoService) {
		this.produtoService = produtoService;
	}
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}
	public void setAgenciaBancariaService(AgenciaBancariaService agenciaBancariaService) {
		this.agenciaBancariaService = agenciaBancariaService;
	}
	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}
	public void setContaBancariaService(ContaBancariaService contaBancariaService) {
		this.contaBancariaService = contaBancariaService;
	}
	public void setPessoaService(PessoaService pessoaService) {
		this.pessoaService = pessoaService;
	}
	public void setDoctoServicoIndenizacaoService(DoctoServicoIndenizacaoService doctoServicoIndenizacaoService) {
		this.doctoServicoIndenizacaoService = doctoServicoIndenizacaoService;
	}
	public void setFilialDebitadaService(FilialDebitadaService filialDebitadaService) {
		this.filialDebitadaService = filialDebitadaService;
	}
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	public void setMdaSalvadoIndenizacaoService(MdaSalvadoIndenizacaoService mdaSalvadoIndenizacaoService) {
		this.mdaSalvadoIndenizacaoService = mdaSalvadoIndenizacaoService;
	}
	public void setParcelaReciboIndenizacaoService(
			ParcelaReciboIndenizacaoService parcelaReciboIndenizacaoService) {
		this.parcelaReciboIndenizacaoService = parcelaReciboIndenizacaoService;
	}
	public void setConversaoMoedaService(ConversaoMoedaService conversaoMoedaService) {
		this.conversaoMoedaService = conversaoMoedaService;
	}
	public void setReportServiceSupport(EmitirMDAService emitirMDAService) {
		this.reportServiceSupport = emitirMDAService;
	}
	public MoedaService getMoedaService() {
		return moedaService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public ReciboIndenizacaoService getReciboIndenizacaoService() {
		return reciboIndenizacaoService;
	}
	public void setReciboIndenizacaoService(ReciboIndenizacaoService reciboIndenizacaoService) {
		this.reciboIndenizacaoService = reciboIndenizacaoService;
	}
	public void setOrdemFilialFluxoService(
			OrdemFilialFluxoService ordemFilialFluxoService) {
		this.ordemFilialFluxoService = ordemFilialFluxoService;
	}
	public void setHistoricoFilialService(
			HistoricoFilialService historicoFilialService) {
		this.historicoFilialService = historicoFilialService;
	}
	
	public EventoRimService getEventoRimService() {
		return eventoRimService;
	}
	public void setEventoRimService(EventoRimService eventoRimService) {
		this.eventoRimService = eventoRimService;
	}
	
	public void setControleCargaService(ControleCargaService controleCargaService) {
		this.controleCargaService = controleCargaService;
	}
	public void setManifestoService(ManifestoService manifestoService) {
		this.manifestoService = manifestoService;
	}

	public CotacaoIndicadorFinanceiroService getCotacaoIndicadorFinanceiroService() {
		return cotacaoIndicadorFinanceiroService;
	}
	public void setCotacaoIndicadorFinanceiroService(
			CotacaoIndicadorFinanceiroService cotacaoIndicadorFinanceiroService) {
		this.cotacaoIndicadorFinanceiroService = cotacaoIndicadorFinanceiroService;
	}

	public OcorrenciaNaoConformidadeService getOcorrenciaNaoConformidadeService() {
		return ocorrenciaNaoConformidadeService;
	}

	public void setOcorrenciaNaoConformidadeService(
			OcorrenciaNaoConformidadeService ocorrenciaNaoConformidadeService) {
		this.ocorrenciaNaoConformidadeService = ocorrenciaNaoConformidadeService;
	}
	
	public void setAnexoRimService(AnexoRimService anexoRimService) {
		this.anexoRimService = anexoRimService;
}

	public void setUsuarioLMSService(UsuarioLMSService usuarioLMSService) {
		this.usuarioLMSService = usuarioLMSService;
	}
	
	public void setIndenizacoesFranqueadosService(
			IndenizacoesFranqueadosService indenizacoesFranqueadosService) {
		this.indenizacoesFranqueadosService = indenizacoesFranqueadosService;
	}
	
}