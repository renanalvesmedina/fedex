 package com.mercurio.lms.pendencia.report;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.JRReportDataObject;
import com.mercurio.adsm.framework.report.ReportServiceSupport;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.ParametroGeral;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.service.DoctoServicoService;
import com.mercurio.lms.expedicao.util.EnderecoPessoaUtils;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.sim.model.EventoDocumentoServico;
import com.mercurio.lms.sim.model.service.EventoDocumentoServicoService;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.ParcelaPrecoService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.DivisaoCliente;
import com.mercurio.lms.vendas.model.ServicoAdicionalCliente;
import com.mercurio.lms.vendas.model.TabelaDivisaoCliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import com.mercurio.lms.vendas.model.service.ServicoAdicionalClienteService;
import com.mercurio.lms.vendas.model.service.TabelaDivisaoClienteService;


/**
 * Classe responsável pela geração da carta de Emissao de Ocorrencia.
 * Especificação técnica 02.03.02.12
 * @author Christian Brunkow
 * 
 * @spring.bean id="lms.pendencia.emitirCartaMercadoriasDisposicaoService"
 * @spring.property name="reportName" value="com/mercurio/lms/pendencia/report/emitirCartaMercadoriasDisposicao.jasper"
 */

public class EmitirCartaMercadoriasDisposicaoService extends ReportServiceSupport{

	private static final String MINIMO_DIAS_ARMAZENAGEM = "MINIMO_DIAS_ARMAZENAGEM";
	private static final String DEC_PRAZO = "DEC_PRAZO";
	private static final int DESCONTO_VALUE = 100;
	private static final int VALOR_VALUE = 0;
	private static final String DESCONTO = "D";
	private static final String VALOR = "V";
	private static final String CLIENTE_EVENTUAL = "E";
	private static final String CLIENTE_POTENCIAL = "P";
	private EnderecoPessoaService enderecoPessoaService;
	private TelefoneEnderecoService telefoneEnderecoService; 
	private FilialService filialService;
	private ClienteService clienteService;
	private ParametroGeralService parametroGeralService;
	private EventoDocumentoServicoService eventoDocumentoServicoService;
	private ServicoAdicionalClienteService servicoAdicionalClienteService;
	private ParcelaPrecoService parcelaPrecoService;
	private DoctoServicoService doctoServicoService;
	private TabelaDivisaoClienteService tabelaDivisaoClienteService;
	
	public void setDoctoServicoService(DoctoServicoService doctoServicoService) {
		this.doctoServicoService = doctoServicoService;
	}

	public void setTabelaDivisaoClienteService(TabelaDivisaoClienteService tabelaDivisaoClienteService) {
		this.tabelaDivisaoClienteService = tabelaDivisaoClienteService;
	}
	
	public void setParcelaPrecoService(ParcelaPrecoService parcelaPrecoService) {
		this.parcelaPrecoService = parcelaPrecoService;
	}

	public void setServicoAdicionalClienteService(ServicoAdicionalClienteService servicoAdicionalClienteService) {
		this.servicoAdicionalClienteService = servicoAdicionalClienteService;
	}

	public void setEventoDocumentoServicoService(EventoDocumentoServicoService eventoDocumentoServicoService) {
		this.eventoDocumentoServicoService = eventoDocumentoServicoService;
	}

	public void setParametroGeralService(ParametroGeralService parametroGeralService) {
		this.parametroGeralService = parametroGeralService;
	}

	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}

	public void setTelefoneEnderecoService(TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
	
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	/**
     * método responsável por gerar o relatório. 
     */
    public JRReportDataObject execute(Map parameters) throws Exception {
    	if(parameters.get("formBean.clienteRemetente.pessoa.dsMail") == null && parameters.get("formBean.contatos")!=null ){
    		StringBuilder sbContatos = new StringBuilder();
    		for(Object contato:  (List)parameters.get("formBean.contatos")){
    			sbContatos.append( ((Map) contato).get("dsEmail")).append(", ");
    		}
    		sbContatos.delete(sbContatos.length()-2, sbContatos.length());
    		parameters.put("formBean.clienteRemetente.pessoa.dsMail", sbContatos.toString()); 
    	}
    	
    	
    	TypedFlatMap criteria = (TypedFlatMap) parameters;
     
    	SqlTemplate sql = getSql(criteria.getLong("formBean.doctoServico.idDoctoServico"), criteria.getList("idsNotaFiscalConhecimento"));
        
        JRReportDataObject jrReportDataObject = executeQuery(sql.getSql(), sql.getCriteria());
    	
        EnderecoPessoa enderecoPessoa = enderecoPessoaService
        	.findEnderecoPessoaPadrao(SessionUtils.getFilialSessao().getPessoa().getIdPessoa());
        
        Map parametersReport = new HashMap();
        
        //Parametros do relatorio...        
        parametersReport.put("dsOcorrencia", criteria.getString("dsOcorrencia"));
        parametersReport.put("dhBloqueio", criteria.getString("dhBloqueio"));
        
        parametersReport.put("dataSaida", this.mountMap(criteria.getList("idsNotaFiscalConhecimento")));
        
        parametersReport.put("dataEnviada", getDataEnviada(criteria.getLong("formBean.doctoServico.idDoctoServico")));
        
        
        putDays(parametersReport, criteria);
        
        parametersReport.put("filialUsuario", criteria.getString("formBean.filial.sgFilial")); 
        parametersReport.put("dsEmail", criteria.getString("formBean.clienteRemetente.pessoa.dsMail"));
        parametersReport.put("contatoCliente", criteria.getString("formBean.contatoCliente"));
        
        parametersReport.put("nrTelefoneUsuario", formatTelefone(criteria.getString("formBean.telefone.nrDdd"), criteria.getString("formBean.telefone.nrTelefone")));
        parametersReport.put("nrFaxUsuario", formatTelefone(criteria.getString("formBean.fax.nrDdd"), criteria.getString("formBean.fax.nrTelefone")));
        
        parametersReport.put("nmUsuario", SessionUtils.getUsuarioLogado().getNmUsuario());
        parametersReport.put("dsEmailUsuario", criteria.getString("formBean.usuario.dsEmail"));
        parametersReport.put("ramalUsuario", criteria.getString("formBean.ramal"));
        parametersReport.put("observacao", criteria.getString("formBean.observacao"));
        
        parametersReport.put("nmMunicipio", enderecoPessoa.getMunicipio().getNmMunicipio());
        
        parametersReport.put("dsNomeFilial", enderecoPessoa.getPessoa().getNmPessoa());
        parametersReport.put("dsEnderecoFilial", EnderecoPessoaUtils.mountEnderecoFilial(enderecoPessoa));
        
        Filial filial = filialService.findById(enderecoPessoa.getPessoa().getIdPessoa());
        
        parametersReport.put("dsHomePage", filial.getDsHomepage());
        
        parametersReport.put(JRReportDataObject.EXPORT_MODE_PARAM, JRReportDataObject.EXPORT_PDF);
        
        jrReportDataObject.setParameters(parametersReport);
        return jrReportDataObject;
    }
    
    public void putDays(Map parametersReport, TypedFlatMap criteria) {
    	String tipoCliente = getTipoCliente(criteria.getLong("formBean.clienteRemetente.idCliente"));
        ServicoAdicionalCliente servicoAdicionalCliente = getServicoAdicionalClienteByCdParcelaPreco(criteria.getLong("formBean.doctoServico.idDoctoServico"), "IDArmazenagem");
		
        boolean haveCobrancaTaxaPermanenciaCarga = true;
        String diasRetornoDecursoPrazo = "";
        String diasCobrancaTaxaPermanenciaCarga = "";
		
        if(CLIENTE_POTENCIAL.equals(tipoCliente) || CLIENTE_EVENTUAL.equals(tipoCliente)){
        	diasRetornoDecursoPrazo = parametroGeralService.findConteudoByNomeParametro(DEC_PRAZO, false).toString();
        	diasCobrancaTaxaPermanenciaCarga = parametroGeralService.findConteudoByNomeParametro("CARENCIA_ARM", false).toString();
        }else if(servicoAdicionalCliente == null || servicoAdicionalCliente.getNrQuantidadeDias() == null){
        	haveCobrancaTaxaPermanenciaCarga = false;
        	diasRetornoDecursoPrazo = parametroGeralService.findConteudoByNomeParametro(DEC_PRAZO, false).toString();
        }else if((VALOR.equals(servicoAdicionalCliente.getTpIndicador().getValue()) && servicoAdicionalCliente.getVlValor().equals(new BigDecimal(VALOR_VALUE))) 
    			|| (DESCONTO.equals(servicoAdicionalCliente.getTpIndicador().getValue()) && servicoAdicionalCliente.getVlValor().equals(new BigDecimal(DESCONTO_VALUE)))){
        	haveCobrancaTaxaPermanenciaCarga = false;
        	diasRetornoDecursoPrazo = "0";
    	}else{
    		diasRetornoDecursoPrazo = servicoAdicionalCliente.getNrDecursoPrazo() != null ? servicoAdicionalCliente.getNrDecursoPrazo().toString() : parametroGeralService.findConteudoByNomeParametro(DEC_PRAZO, false).toString();
    		diasCobrancaTaxaPermanenciaCarga = servicoAdicionalCliente.getNrQuantidadeDias() != null ? servicoAdicionalCliente.getNrQuantidadeDias().toString() : parametroGeralService.findConteudoByNomeParametro(MINIMO_DIAS_ARMAZENAGEM, false).toString();
		}
        parametersReport.put("haveCobrancaTaxaPermanenciaCarga", haveCobrancaTaxaPermanenciaCarga);
        parametersReport.put("diasRetornoDecursoPrazo", diasRetornoDecursoPrazo);
        parametersReport.put("diasCobrancaTaxaPermanenciaCarga", diasCobrancaTaxaPermanenciaCarga);
			}
			
	private String getDataEnviada(Long iddoctoServico) {
		ParametroGeral value = parametroGeralService.findByNomeParametro("CD_EVENTO_MERCADORIA_DISPOSICAO");
		List<EventoDocumentoServico> list = eventoDocumentoServicoService.findByEventoByDocumentoServico(Long.valueOf(value.getDsConteudo()), iddoctoServico);
		return  list.isEmpty() || list.get(0).getDhEvento() == null ? "" : JTFormatUtils.formatDiaMesAnoPorExtenso(list.get(0).getDhEvento());
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
    	if(tabelaDivisaoClienteList == null || tabelaDivisaoClienteList.isEmpty()){
    		return null;
    	}
	
        ServicoAdicionalCliente servicoAdicionalCliente = servicoAdicionalClienteService.findServicoAdicionalCliente(tabelaDivisaoClienteList.get(0).getIdTabelaDivisaoCliente(), parcelaPreco.getIdParcelaPreco());
        return servicoAdicionalCliente == null ? null : servicoAdicionalCliente;
	}

    /**
     * Monta um Map com os dados da grid.
     * 
     * @param viewData
     * @return
     */
    private Map mountMap(List viewData) {
    	
    	Map mapDataEmisao = new HashMap();
    	
    	for (Iterator iter = viewData.iterator(); iter.hasNext();) {
			TypedFlatMap data = (TypedFlatMap) iter.next();
			mapDataEmisao.put(data.getLong("idNotaFiscalConhecimento"), data.getString("dtSaida")); 
		}
    	
    	return mapDataEmisao;
    }
    
    /**
     * Método que monta o select para a consulta.
     * 
     * @param map
     * @return SqlTemplate
     */
    private SqlTemplate getSql(Long idDoctoServico, List idsNotaFiscalConhecimento) { 
    	
    	SqlTemplate sql = new SqlTemplate();
    	
    	//Projecao
    	sql.addProjection("pessoaRemetente.ID_PESSOA", "idPessoa");
    	sql.addProjection("pessoaRemetente.NM_PESSOA", "nmPessoa");
    	sql.addProjection("pessoaRemetente.DS_EMAIL", "dsEmail");
    	sql.addProjection("municipioPessoaRemetente.NM_MUNICIPIO", "nmMunicipio");
    	sql.addProjection("ufEnderecoPessoaRemetente.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativa");
    	sql.addProjection("enderecoPessoaRemetente.DS_ENDERECO", "dsEndereco");
    	sql.addProjection("enderecoPessoaRemetente.NR_ENDERECO", "nrEndereco");
    	sql.addProjection("enderecoPessoaRemetente.DS_COMPLEMENTO", "dsComplemento");
    	sql.addProjection("enderecoPessoaRemetente.DS_BAIRRO", "dsBairro");
    	sql.addProjection("enderecoPessoaRemetente.NR_CEP", "nrCEP");
    	sql.addProjection("moeda.DS_SIMBOLO", "dsSimbolo");
    	sql.addProjection("moeda.SG_MOEDA", "sgMoeda");
    	sql.addProjection("notaFiscalConhecimento.ID_NOTA_FISCAL_CONHECIMENTO", "idNotaFiscalConhecimento");
    	sql.addProjection("notaFiscalConhecimento.NR_NOTA_FISCAL", "nrNotaFiscal");
    	sql.addProjection("notaFiscalConhecimento.DT_EMISSAO", "dtEmissao");
    	sql.addProjection("notaFiscalConhecimento.DT_SAIDA", "dtSaida");
    	sql.addProjection("notaFiscalConhecimento.VL_TOTAL", "vlTotal");
    	sql.addProjection("notaFiscalConhecimento.PS_MERCADORIA", "psMercadoria");
    	sql.addProjection("notaFiscalConhecimento.QT_VOLUMES", "qtVolumes");
    	sql.addProjection("filialOrigem.ID_FILIAL", "idFilial");
    	sql.addProjection("doctoServico.TP_DOCUMENTO_SERVICO", "tpDoctoServico");
    	sql.addProjection("filialOrigem.SG_FILIAL", "sgFilial");
    	sql.addProjection("doctoServico.NR_DOCTO_SERVICO", "nrDoctoServico");
    	sql.addProjection("doctoServico.DH_EMISSAO", "dhEmissao");
    	sql.addProjection("pessoaDestinatario.NM_PESSOA", "nmPessoaDest");
    	sql.addProjection("municipioPessoaRemetente.NM_MUNICIPIO", "nmMunicipioDest");
    	sql.addProjection("ufEnderecoPessoaRemetente.SG_UNIDADE_FEDERATIVA", "sgUnidadeFederativaDest");
    	
    	//From...
    	sql.addFrom("DOCTO_SERVICO", "doctoServico");
    	sql.addFrom("moeda", "moeda");
    	sql.addFrom("filial", "filialOrigem");
    	sql.addFrom("NOTA_FISCAL_CONHECIMENTO", "notaFiscalConhecimento");
    	sql.addFrom("cliente", "clienteRemetente");
    	sql.addFrom("pessoa", "pessoaRemetente");
    	sql.addFrom("endereco_pessoa", "enderecoPessoaRemetente");
    	sql.addFrom("municipio", "municipioPessoaRemetente");
    	sql.addFrom("unidade_federativa", "ufEnderecoPessoaRemetente");
    	sql.addFrom("cliente", "clienteDestinatario");
    	sql.addFrom("pessoa", "pessoaDestinatario");
    	sql.addFrom("endereco_pessoa", "enderecoPessoaDestinatario");
    	sql.addFrom("municipio", "municipioPessoaDestinatario");
    	sql.addFrom("unidade_federativa", "ufEnderecoPessoaDestinatario");
    	
    	//Where...
		sql.addCustomCriteria("doctoServico.ID_DOCTO_SERVICO = notaFiscalConhecimento.ID_CONHECIMENTO");
		sql.addCustomCriteria("doctoServico.ID_MOEDA = moeda.ID_MOEDA");
		sql.addCustomCriteria("doctoServico.ID_FILIAL_ORIGEM = filialOrigem.ID_FILIAL");
		sql.addCustomCriteria("doctoServico.ID_CLIENTE_REMETENTE = clienteRemetente.ID_CLIENTE(+)");
		sql.addCustomCriteria("clienteRemetente.ID_CLIENTE = pessoaRemetente.ID_PESSOA(+)");
		sql.addCustomCriteria("pessoaRemetente.ID_ENDERECO_PESSOA = enderecoPessoaRemetente.ID_ENDERECO_PESSOA(+)");
		sql.addCustomCriteria("enderecoPessoaRemetente.ID_MUNICIPIO = municipioPessoaRemetente.ID_MUNICIPIO(+)");
		sql.addCustomCriteria("municipioPessoaRemetente.ID_UNIDADE_FEDERATIVA = ufEnderecoPessoaRemetente.ID_UNIDADE_FEDERATIVA(+)");
		sql.addCustomCriteria("doctoServico.ID_CLIENTE_DESTINATARIO = clienteDestinatario.ID_CLIENTE(+)");
		sql.addCustomCriteria("clienteDestinatario.ID_CLIENTE = pessoaDestinatario.ID_PESSOA(+)");
		sql.addCustomCriteria("pessoaDestinatario.ID_ENDERECO_PESSOA = enderecoPessoaDestinatario.ID_ENDERECO_PESSOA(+)");
		sql.addCustomCriteria("enderecoPessoaDestinatario.ID_MUNICIPIO = municipioPessoaDestinatario.ID_MUNICIPIO(+)");
		sql.addCustomCriteria("municipioPessoaDestinatario.ID_UNIDADE_FEDERATIVA = ufEnderecoPessoaDestinatario.ID_UNIDADE_FEDERATIVA(+)");
		sql.addCriteria(" doctoServico.ID_DOCTO_SERVICO", "=", idDoctoServico);
		
		if (!idsNotaFiscalConhecimento.isEmpty()) {
				String ids = "";
				
				for (Iterator iter = idsNotaFiscalConhecimento.iterator(); iter.hasNext();) {
					TypedFlatMap id = (TypedFlatMap) iter.next();
				if (!"".equals(ids)) {
					ids += ", "; 
				}
					ids += id.getString("idNotaFiscalConhecimento"); 
				}
				sql.addCustomCriteria(" notaFiscalConhecimento.ID_NOTA_FISCAL_CONHECIMENTO IN (" + ids + ")");
			}
		
		//Order By....
        sql.addOrderBy("notaFiscalConhecimento.NR_NOTA_FISCAL");
        
        return sql;         
    }
    
    /**
     * Captura o telefone de uma determinada pessoa e o devolve formatado.
     * 
     * @param idPessoa
     * @param tpTelefone
     * @param tpUso
     * @return
     */
    public String getTelefoneFormatado(Long idPessoa, String tpTelefone, String tpUso) {
    	TypedFlatMap telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoByIdPessoaTpTelefone(idPessoa, tpTelefone, tpUso);
    	
    	String telefone = FormatUtils.formatTelefone(telefoneEndereco.getString("nrTelefone"), telefoneEndereco.getString("nrDdd"), null);
    	
    	if ((telefone!=null) && (!"".equals(telefone))){
    		return telefone; 
    	}
    	
    	return "";
    }
    
    public String getActualDate(){
    	return JTFormatUtils.formatDiaMesAnoPorExtenso(JTDateTimeUtils.getDataHoraAtual()).toLowerCase();
    }
     
    public void configReportDomains(ReportDomainConfig config) {
    	config.configDomainField("TPDOCTOSERVICO","DM_TIPO_DOCUMENTO_SERVICO");
	}
    
    public String formatTelefone(String ddd, String numero) {
    	String numeroTelefone = "";
    	
    	if ((ddd!=null) && (!"".equals(ddd))) {
    		numeroTelefone = numeroTelefone.concat("(" + ddd + ") ");
    	}
    	if ((numero!=null) && (!"".equals(numero))){
    		numeroTelefone = numeroTelefone.concat(numero);
    	}
    	return numeroTelefone; 
    }

}
