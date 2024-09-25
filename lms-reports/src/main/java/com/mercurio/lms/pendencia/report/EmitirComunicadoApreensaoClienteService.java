package com.mercurio.lms.pendencia.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.Moeda;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.TelefoneEndereco;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.MoedaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.NotaFiscalConhecimento;
import com.mercurio.lms.expedicao.model.service.DevedorDocServService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.model.service.NotaFiscalConhecimentoService;
import com.mercurio.lms.expedicao.util.EnderecoPessoaUtils;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.TabelaPrecoParcela;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;

/**
 * Classe responsável pela geração do relatório de comunicado de apreensão ao cliente
 * Especificação técnica 17.03.01.01
 * @author Rodrigo Antunes
 * 
 * @spring.bean id="lms.pendencia.emitirComunicadoApreensaoClienteService"
 * @spring.property name="reportName" value="com/mercurio/lms/pendencia/report/emitirComunicadoApreensaoCliente.jasper"
 */
public class EmitirComunicadoApreensaoClienteService extends ReportServiceSupport {
	private static final int DESCONTO_VALUE = 100;
	private static final int VALOR_VALUE = 0;
	private static final String DESCONTO = "D";
	private static final String VALOR = "V";
	private static final String CLIENTE_EVENTUAL = "E";
	private static final String CLIENTE_POTENCIAL = "P";

	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService;
	private NotaFiscalConhecimentoService notaFiscalConhecimentoService;
	private DomainValueService domainValueService;
	private MoedaService moedaService;
	private FilialService filialService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ParametroGeralService parametroGeralService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ParcelaPrecoService parcelaPrecoService;
	private ClienteService clienteService;
	private DoctoServicoService doctoServicoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	private DevedorDocServService devedorDocServService;
	
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}
	
	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}
	
	public void setNotaFiscalConhecimentoService(NotaFiscalConhecimentoService notaFiscalConhecimentoService) {
		this.notaFiscalConhecimentoService = notaFiscalConhecimentoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public void setMoedaService(MoedaService moedaService) {
		this.moedaService = moedaService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setDevedorDocServService(DevedorDocServService devedorDocServService) {
		this.devedorDocServService = devedorDocServService;
	}

	public JRReportDataObject execute(Map parameters) throws Exception {
		TypedFlatMap tfm = (TypedFlatMap)parameters;
        Map parametersReport = new HashMap();
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        JRMapCollectionDataSource dataSource = mountReportData(tfm); 
	
		return createReportDataObject(dataSource, parametersReport);
	}
	
	public JRMapCollectionDataSource mountReportData(TypedFlatMap tfm) {
		
		EnderecoPessoa enderecoPessoa = enderecoPessoaService.findEnderecoPessoaPadrao(SessionUtils.getFilialSessao().getPessoa().getIdPessoa());
		
		List retorno = new ArrayList();
		
		
		Cliente cliente = devedorDocServService.findByIdDoctoServico(tfm.getLong("ocorrenciaDoctoServico.doctoServico.idDoctoServico"));
		cliente = clienteService.findByIdComPessoa(cliente.getIdCliente());
		Map dadosRetorno = buildFielDepositario(tfm, cliente.getCliente().getPessoa().getIdPessoa());
		//Map dadosRetorno = buildFielDepositario(tfm);
		
		dadosRetorno.put("LOCAL", findLocal(SessionUtils.getFilialSessao().getPessoa().getIdPessoa()));
		dadosRetorno.put("DATA_ATUAL", JTFormatUtils.formatDiaMesAnoPorExtenso( JTDateTimeUtils.getDataHoraAtual() ).toLowerCase() );
		if (cliente == null) {
			dadosRetorno.put("REMETENTE", tfm.getString("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.nmPessoa") );
			dadosRetorno.put("REMETENTE_ENDERECO", getEnderecoByIdPessoa(tfm.getLong("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa")) );
			dadosRetorno.put("REMETENTE_FONE",  getTelefoneByIdPessoa(tfm.getLong("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa")) );
			dadosRetorno.put("REMETENTE_EMAIL", tfm.getString("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.dsEmail"));  
		}else {
			dadosRetorno.put("REMETENTE", cliente.getCliente().getPessoa().getNmPessoa() );
			dadosRetorno.put("REMETENTE_ENDERECO", getEnderecoByIdPessoa(cliente.getCliente().getPessoa().getIdPessoa()) );
			dadosRetorno.put("REMETENTE_FONE",  getTelefoneByIdPessoa(cliente.getCliente().getPessoa().getIdPessoa())) ;
			dadosRetorno.put("REMETENTE_EMAIL",cliente.getCliente().getPessoa().getDsEmail());  
		
		}
		dadosRetorno.put("DATA_ENVIADA", getDataEnviada(tfm.getLong("ocorrenciaDoctoServico.doctoServico.idDoctoServico")));
		dadosRetorno.put("DESTINATARIO", tfm.getString("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.nmPessoa"));
		dadosRetorno.put("DESTINATARIO_ENDERECO", getEnderecoByIdPessoa(tfm.getLong("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.idPessoa")));
		dadosRetorno.put("DESTINATARIO_FONE", getTelefoneByIdPessoa(tfm.getLong("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.idPessoa")) );
		dadosRetorno.put("DESTINATARIO_EMAIL", tfm.getString("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteDestinatario.pessoa.dsEmail"));
		dadosRetorno.put("NOTA_FISCAL", this.findNotasFiscais(tfm.getLong("ocorrenciaDoctoServico.doctoServico.idDoctoServico")));		
		dadosRetorno.put("DOCUMENTO", getDocumento(tfm));
		dadosRetorno.put("TIT", tfm.getString("ocorrenciaDoctoServico.comunicadoApreensao.nrTermoApreensao"));
		dadosRetorno.put("VALOR_MULTA", tfm.getBigDecimal("ocorrenciaDoctoServico.comunicadoApreensao.vlMulta"));
		dadosRetorno.put("DATA_TERMO_APREENSAO", getDataTermoApreensao(tfm.getYearMonthDay("ocorrenciaDoctoServico.comunicadoApreensao.dtOcorrencia")));
		dadosRetorno.put("MOEDA", getMoeda(tfm.getLong("ocorrenciaDoctoServico.comunicadoApreensao.moeda.idMoeda")));	
		dadosRetorno.put("MOTIVO_ALEGADO", tfm.getString("ocorrenciaDoctoServico.comunicadoApreensao.dsMotivoAlegado"));
		dadosRetorno.put("FILIAL", SessionUtils.getFilialSessao().getPessoa().getNmFantasia());
		dadosRetorno.put("RESPONSAVEL", tfm.getString("usuario.nmUsuario"));
		dadosRetorno.put("FONE_FILIAL", tfm.getString("telefone"));
		dadosRetorno.put("RAMAL_FILIAL", tfm.getString("ramal"));
		dadosRetorno.put("FAX_FILIAL", tfm.getString("fax"));
		dadosRetorno.put("EMAIL_FILIAL", tfm.getString("usuario.dsEmail"));
		dadosRetorno.put("MUNICIPIO", enderecoPessoa.getMunicipio().getNmMunicipio());
		dadosRetorno.put("NOME_FILIAL", enderecoPessoa.getPessoa().getNmPessoa());
		dadosRetorno.put("ENDERECO_FILIAL", EnderecoPessoaUtils.mountEnderecoFilial(enderecoPessoa));
        dadosRetorno.put("HOME_PAGE", filialService.findById(enderecoPessoa.getPessoa().getIdPessoa()).getDsHomepage());
        
		retorno.add( dadosRetorno );
		
		return new JRMapCollectionDataSource(retorno);
	}
	
	public Map buildFielDepositario(TypedFlatMap tfm) {
		String tipoCliente = getTipoCliente(tfm.getLong("ocorrenciaDoctoServico.doctoServico.clienteByIdClienteRemetente.pessoa.idPessoa"));
        ServicoAdicionalCliente servicoAdicionalCliente = getServicoAdicionalClienteByCdParcelaPreco(tfm.getLong("ocorrenciaDoctoServico.doctoServico.idDoctoServico"), "IdTaxaFielDep");
        Map dadosRetorno = new HashMap();
	
        if(CLIENTE_POTENCIAL.equals(tipoCliente) || CLIENTE_EVENTUAL.equals(tipoCliente)){
        	dadosRetorno.put("NRO_DIAS_FIEL_DEPOSITARIO", parametroGeralService.findByNomeParametro("CARENCIA_DEP").getDsConteudo());
        } else if (existeCombranca(servicoAdicionalCliente)){
        	dadosRetorno.put("NRO_DIAS_FIEL_DEPOSITARIO", servicoAdicionalCliente.getNrQuantidadeDias() != null ? servicoAdicionalCliente.getNrQuantidadeDias().toString() : parametroGeralService.findByNomeParametro("CARENCIA_DEP").getDsConteudo());
        } else {
        	dadosRetorno.put("NRO_DIAS_FIEL_DEPOSITARIO", null);
        }
		
		return dadosRetorno;
	}
	
	public Map buildFielDepositario(TypedFlatMap tfm, Long idPessoa) {
		
		String tipoCliente = getTipoCliente(idPessoa);		
        ServicoAdicionalCliente servicoAdicionalCliente = getServicoAdicionalClienteByCdParcelaPreco(tfm.getLong("ocorrenciaDoctoServico.doctoServico.idDoctoServico"), "IdTaxaFielDep");
        Map dadosRetorno = new HashMap();
	
        if(CLIENTE_POTENCIAL.equals(tipoCliente) || CLIENTE_EVENTUAL.equals(tipoCliente)){
        	dadosRetorno.put("NRO_DIAS_FIEL_DEPOSITARIO", parametroGeralService.findByNomeParametro("CARENCIA_DEP").getDsConteudo());
        	dadosRetorno.put("FIEL_DEPOSITARIO", "Sim");
        } else if (existeCombranca(servicoAdicionalCliente)){
        	dadosRetorno.put("NRO_DIAS_FIEL_DEPOSITARIO", servicoAdicionalCliente.getNrQuantidadeDias() != null ? servicoAdicionalCliente.getNrQuantidadeDias().toString() : parametroGeralService.findByNomeParametro("CARENCIA_DEP").getDsConteudo());
        	dadosRetorno.put("FIEL_DEPOSITARIO", "Sim");
        } else {
        	dadosRetorno.put("NRO_DIAS_FIEL_DEPOSITARIO", parametroGeralService.findByNomeParametro("CARENCIA_DEP").getDsConteudo());
        	dadosRetorno.put("FIEL_DEPOSITARIO", null);
        }
		
		return dadosRetorno;
	}
		
	public boolean existeCombranca(ServicoAdicionalCliente servicoAdicionalCliente){
		return servicoAdicionalCliente == null || isValorSemCobranca(servicoAdicionalCliente) || isDescontoIntegral(servicoAdicionalCliente) ? false : true; 
			}
			
	public boolean isValorSemCobranca(ServicoAdicionalCliente servicoAdicionalCliente){
		return VALOR.equals(servicoAdicionalCliente.getTpIndicador().getValue()) && servicoAdicionalCliente.getVlValor().equals(new BigDecimal(VALOR_VALUE));
			}
	
	public boolean isDescontoIntegral(ServicoAdicionalCliente servicoAdicionalCliente){
		return DESCONTO.equals(servicoAdicionalCliente.getTpIndicador().getValue()) && servicoAdicionalCliente.getVlValor().equals(new BigDecimal(DESCONTO_VALUE));
		}
		
	private String getTipoCliente(Long idCliente){
    	return clienteService.findById(idCliente).getTpCliente().getValue();
    }
		
    private ServicoAdicionalCliente getServicoAdicionalClienteByCdParcelaPreco(Long idDoctoServico, String cdParcelaPreco){
    	ParcelaPreco parcelaPreco = parcelaPrecoService.findByCdParcelaPreco(cdParcelaPreco);
    	DivisaoCliente divisaoCliente = doctoServicoService.findDivisaoClienteById(idDoctoServico);
    	if(divisaoCliente == null || divisaoCliente.getIdDivisaoCliente() == null){
    		return null;
		}
		List<TabelaDivisaoCliente> tabelaDivisaoClienteList = tabelaDivisaoClienteService.findByDivisaoCliente(divisaoCliente.getIdDivisaoCliente());
		if (tabelaDivisaoClienteList == null || tabelaDivisaoClienteList.isEmpty()) {
			return null;
		}

		ServicoAdicionalCliente servicoAdicionalCliente = servicoAdicionalClienteService.findServicoAdicionalCliente(
				tabelaDivisaoClienteList.get(0).getIdTabelaDivisaoCliente(), parcelaPreco.getIdParcelaPreco());
		// LMSA - 2673 - 03/05/2018 - Inicio
		if (servicoAdicionalCliente == null && tabelaDivisaoClienteList != null && tabelaDivisaoClienteList.get(0).getTabelaPreco() !=null) {			
			if(tabelaDivisaoClienteList.get(0).getTabelaPreco().getTabelaPrecoParcelas() != null) {
				
				for (TabelaPrecoParcela tabelaPrecoParcela : tabelaDivisaoClienteList.get(0).getTabelaPreco().getTabelaPrecoParcelas()) {
					if(tabelaPrecoParcela.getParcelaPreco().equals(parcelaPreco)) {
						servicoAdicionalCliente = new ServicoAdicionalCliente();
						servicoAdicionalCliente.setParcelaPreco(tabelaPrecoParcela.getParcelaPreco());
						servicoAdicionalCliente.setVlMinimo(tabelaPrecoParcela.getValorServicoAdicional().getVlMinimo());
						servicoAdicionalCliente.setVlValor(tabelaPrecoParcela.getValorServicoAdicional().getVlServico());
						if(tabelaPrecoParcela.getParcelaPreco().getTpIndicadorCalculo().getValue().contains("V")) {
							servicoAdicionalCliente.setTpIndicador(new DomainValue("V"));
						} else {
							servicoAdicionalCliente.setTpIndicador(new DomainValue("D"));
						}
						break;
					}
				}
				
			}
			
		}
		// LMSA - 2673 - 03/05/2018 - Fim
		return servicoAdicionalCliente;
	}
	
	private String getDataTermoApreensao(YearMonthDay date) {
		return date != null ? JTFormatUtils.format(date) : null; 
	}
	
	private String getMoeda(Long idMoeda) {
		if (idMoeda == null) {
			return null;
		}
	
		Moeda moeda = moedaService.findById(idMoeda);
		return new StringBuilder().append(moeda.getSgMoeda()).append(" ").append(moeda.getDsSimbolo()).toString();
	}

	private String getDocumento(TypedFlatMap tfm) {
		return new StringBuilder()
						.append(domainValueService.findDomainValueDescription("DM_TIPO_DOCUMENTO_SERVICO", tfm.getString("ocorrenciaDoctoServico.doctoServico.tpDocumentoServico")))
						.append(" ")
						.append(tfm.getString("ocorrenciaDoctoServico.doctoServico.filialByIdFilialOrigem.sgFilial"))
						.append(" ")
						.append(StringUtils.leftPad(tfm.getString("ocorrenciaDoctoServico.doctoServico.nrDoctoServico"), 8, '0'))
						.toString();
	}

	private String getDataEnviada(Long iddoctoServico) {
		ParametroGeral value = parametroGeralService.findByNomeParametro("CD_EVENTO_CARTA_APREENSAO");
		List<EventoDocumentoServico> list = eventoDocumentoServicoService.findByEventoByDocumentoServico(Long.valueOf(value.getDsConteudo()), iddoctoServico);
		return  list.isEmpty() || list.get(0).getDhEvento() == null ? "" : JTFormatUtils.formatDiaMesAnoPorExtenso(list.get(0).getDhEvento());
	}
	
	/**
	 * Busca o endereco do cliente/pessoa 
	 * @param idPessoa
	 * @return
	 */
	public String getEnderecoByIdPessoa(Long idPessoa) {
		if(idPessoa == null) return "";
		
		return enderecoPessoaService.formatEnderecoPessoa(enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa));
		}

	/**
	 * Busca o telefone do cliente/pessoa
	 * @param idPessoa
	 * @return
	 */
	public String getTelefoneByIdPessoa(Long idPessoa) {
		if(idPessoa == null) return "";
		
		TelefoneEndereco te =  telefoneEnderecoService.findTelefoneEnderecoPadrao(idPessoa);
		return te!=null ? FormatUtils.formatTelefone( te.getNrTelefone(), te.getNrDdd(), te.getNrDdi()) : "";	
			}

	/**
	 * Busca as notas fiscais do documento do tipo CTR ou NFT 
	 * @param idConhecimento
	 * @return
	 */
	public String findNotasFiscais(Long idConhecimento) {
		String retorno = "";
		
		if (idConhecimento!=null) {
			List list = notaFiscalConhecimentoService.findByConhecimento(idConhecimento);
			
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				NotaFiscalConhecimento nfc = (NotaFiscalConhecimento) iter.next();
				
				retorno += FormatUtils.formatIntegerWithZeros(nfc.getNrNotaFiscal(), "000000"); 
				
				if(iter.hasNext()) {
					retorno +=", ";
				}
			}
		}
		return retorno;
	}
	
	
	public String findLocal(Long idPessoa) {
		EnderecoPessoa ep = enderecoPessoaService.findEnderecoPessoaPadrao(idPessoa) ;
		return ep!=null ? ep.getMunicipio().getNmMunicipio() : "";
		}
		
	
}