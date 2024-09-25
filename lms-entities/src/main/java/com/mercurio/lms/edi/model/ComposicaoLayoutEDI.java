package com.mercurio.lms.edi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;

@SuppressWarnings("deprecation")
public class ComposicaoLayoutEDI implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idComposicaoLayout;
	
	private DomainValue formato;
	
	private String compFormato;
	
	private Integer tamanho;
	
	private Integer qtDecimal;
	
	private Integer posicao;
	
	private String valorDefault;
	
	private String xpath;
	
	private YearMonthDay dtVigenciaInicial;
	
	private YearMonthDay dtVigenciaFinal;
	
	private String observacao;
	
	private CampoLayoutEDI campoLayout;
	
	private RegistroLayoutEDI registroLayout;
	
	private LayoutEDI layout;
	
	private ClienteLayoutEDI clienteLayoutEDI;
	
	private DeParaEDI deParaEDI;
	
	public Long getIdComposicaoLayout() {
		return idComposicaoLayout;
	}

	public void setIdComposicaoLayout(Long idComposicaoLayout) {
		this.idComposicaoLayout = idComposicaoLayout;
	}

	public DomainValue getFormato() {
		return formato;
	}

	public void setFormato(DomainValue formato) {
		this.formato = formato;
	}

	public String getCompFormato() {
		return compFormato;
	}

	public void setCompFormato(String compFormato) {
		this.compFormato = compFormato;
	}

	public Integer getPosicao() {
		return posicao;
	}

	public void setPosicao(Integer posicao) {
		this.posicao = posicao;
	}

	public String getValorDefault() {
		return valorDefault;
	}

	public void setValorDefault(String valorDefault) {
		this.valorDefault = valorDefault;
	}

	public YearMonthDay getDtVigenciaInicial() {
		return dtVigenciaInicial;
	}

	public void setDtVigenciaInicial(YearMonthDay dtVigenciaInicial) {
		this.dtVigenciaInicial = dtVigenciaInicial;
	}

	public YearMonthDay getDtVigenciaFinal() {
		return dtVigenciaFinal;
	}

	public void setDtVigenciaFinal(YearMonthDay dtVigenciaFinal) {
		this.dtVigenciaFinal = dtVigenciaFinal;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public CampoLayoutEDI getCampoLayout() {
		return campoLayout;
	}

	public void setCampoLayout(CampoLayoutEDI campoLayout) {
		this.campoLayout = campoLayout;
	}

	public RegistroLayoutEDI getRegistroLayout() {
		return registroLayout;
	}

	public void setRegistroLayout(RegistroLayoutEDI registroLayout) {
		this.registroLayout = registroLayout;
	}

	public LayoutEDI getLayout() {
		return layout;
	}

	public void setLayout(LayoutEDI layout) {
		this.layout = layout;
	}
	
	public Map<String,Object> getMapped() {
		Map<String,Object> bean = new HashMap<String, Object>();
		
		/*Id composicao Layout*/
		bean.put("idComposicaoLayout", this.getIdComposicaoLayout());
		
		/*Layout*/		
		bean.put("idLayoutEdi", this.getLayout().getIdLayoutEdi());
		bean.put("nmLayoutEdi", this.getLayout().getNmLayoutEdi());
		
		/*Registro*/
		bean.put("idRegistro", this.getRegistroLayout()
				.getIdRegistroLayoutEdi());
		bean.put("nrIdentificador", this.getRegistroLayout().getIdentificador());
		bean.put("dsRegistro", this.getRegistroLayout().getDescricao());
		bean.put("preRegistro", this.getRegistroLayout().getPreenchimento()
				.getValue());
		bean.put("tamRegistro", this.getRegistroLayout().getTamanhoRegistro());
		bean.put("ocoRegistro", this.getRegistroLayout().getOcorrencias());
		bean.put("paiRegistro", this.getRegistroLayout().getIdRegistroPai());
		
		/*Campo*/
		bean.put("idCampo", this.getCampoLayout().getIdCampo());
		bean.put("nomeCampo", this.getCampoLayout().getNomeCampo());
		bean.put("dsCampo", this.getCampoLayout().getDescricaoCampo());
		bean.put("dmCampoObrigatorio", this.getCampoLayout().getDmObrigatorio()
				.getValue());
		bean.put("nmCampoComplemento", this.getCampoLayout().getNmComplemento());
		
		/*Formato*/
		bean.put("formato", this.getFormato().getValue());
		/*Compl. Formato*/
		bean.put("compFormato", this.getCompFormato());
		/*Tamanho*/
		bean.put("tamanho", this.getTamanho());
		/*Quantidade decimal*/
		bean.put("qtDecimal", this.getQtDecimal());
		/*Posição*/
		bean.put("posicao", this.getPosicao());
		/*Valor default*/
		bean.put("valorDefault", this.getValorDefault());
		
		bean.put("xpath", this.getXpath());
		
		/*Vigencia*/
		bean.put("dtVigenciaInicial", this.getDtVigenciaInicial()); 
		if(!new YearMonthDay(4000, 1, 1).equals(this.getDtVigenciaFinal())){
			bean.put("dtVigenciaFinal", this.getDtVigenciaFinal());			
		}
		
		/*Observação*/
		bean.put("observacao", this.getObservacao());
				
		/*De Para EDI*/
		if(this.deParaEDI != null){
			bean.put("idDeParaEDI", this.deParaEDI.getIdDeParaEDI());
			bean.put("nmDeParaEDI", this.deParaEDI.getNmDeParaEDI());			
		}
		
		return bean;
	}

	public Map<String,Object> getMappedToFind() {
		Map<String,Object> bean = getMapped();
		if(this.getClienteLayoutEDI() != null){
			bean.put("nmPessoaEmbarcadora", this.getClienteLayoutEDI()
					.getEmbarcadora().getClienteEmbarcador().getPessoa()
					.getNmPessoa());
		}
		return bean;
	}
	
	public ClienteLayoutEDI getClienteLayoutEDI() {
		return clienteLayoutEDI;
	}

	public void setClienteLayoutEDI(ClienteLayoutEDI clienteLayoutEDI) {
		this.clienteLayoutEDI = clienteLayoutEDI;
	}

	public Integer getTamanho() {
		return tamanho;
	}

	public void setTamanho(Integer tamanho) {
		this.tamanho = tamanho;
	}

	public Integer getQtDecimal() {
		return qtDecimal;
	}

	public void setQtDecimal(Integer qtDecimal) {
		this.qtDecimal = qtDecimal;
	}

	public DeParaEDI getDeParaEDI() {
		return deParaEDI;
	}

	public void setDeParaEDI(DeParaEDI deParaEDI) {
		this.deParaEDI = deParaEDI;
	}		

	public void setXpath(String xpath) {
		this.xpath = xpath;
}

	public String getXpath() {
		return xpath;
	}		
}
