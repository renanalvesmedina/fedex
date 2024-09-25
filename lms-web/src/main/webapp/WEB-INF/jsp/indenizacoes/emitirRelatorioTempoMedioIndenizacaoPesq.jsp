<%@ include file="/lib/imports.jsp"%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction">
	<adsm:form action="/indenizacoes/emitirRelatorioTempoMedioIndenizacao">
		
		<adsm:range label="periodo" width="80%" labelWidth="20%" required="true">
			<adsm:textbox dataType="JTDate" property="dtInicial" picker="true" onchange="carregaComboRegional()"/>
			<adsm:textbox dataType="JTDate" property="dtFinal" picker="true" onchange="carregaComboRegional()"/>
		</adsm:range>
		
		<adsm:hidden property="flagDesabilitaEmpresaUsuarioLogado" value="false" serializable="false"/>
		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="true"/>		
		
		<adsm:lookup label="filial" labelWidth="20%" width="80%" 
             		 property="filial"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction.findLookupFilial" 
		             dataType="text"
		             size="3"
		             maxLength="3">
			<adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado" modelProperty="flagBuscaEmpresaUsuarioLogado"/>
			<adsm:propertyMapping criteriaProperty="flagDesabilitaEmpresaUsuarioLogado" modelProperty="flagDesabilitaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
          	<adsm:textbox dataType="text" 
          			      property="filial.pessoa.nmFantasia" 
          			      serializable="false" 
          			      size="50" 
          			      maxLength="50" 
          			      disabled="true"/>
        </adsm:lookup>
		
		<adsm:hidden property="sgAndDsRegionalHidden"/>
		<adsm:combobox property="regional.idRegionalFilial" 
					   optionProperty="idRegional" 
					   optionLabelProperty="sgAndDsRegional"
					   service="" 
					   label="regional" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeRegional()"
					   autoLoad="false"/>
		
		<adsm:hidden property="tpModalHidden"/>
		<adsm:combobox label="modal" 
					   property="tpModal"  
					   domain="DM_MODAL" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeModal()"/>

		<adsm:hidden property="tpAbrangenciaHidden"/>
		<adsm:combobox property="tpAbrangencia" 
					   label="abrangencia" 
					   domain="DM_ABRANGENCIA" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeAbrangencia()"/>

		<adsm:hidden property="tpIndenizacaoHidden"/>
		<adsm:combobox property="tpIndenizacao" 
					   label="tipoIndenizacao" 
					   domain="DM_TIPO_INDENIZACAO" 
					   width="80%" renderOptions="true"
					   labelWidth="20%"
					   onchange="onChangeIndenizacao()"
					   />
		
		<adsm:hidden property="dsMotivoAberturaHidden"/>			   
		<adsm:combobox label="motivoNaoConformidade" 
					   labelWidth="20%" 
					   width="80%"
					   property="motivoAberturaNc.idMotivoAberturaNc" 
					   optionProperty="idMotivoAberturaNc" 
					   optionLabelProperty="dsMotivoAbertura" 
					   service="lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction.findComboMotivoAberturaNc"
					   onchange="onChangeMotivoAberturaNc()"/>

		<adsm:hidden property="dsTipoSeguroHidden"/> 
		<adsm:combobox property="tipoSeguro.idTipoSeguro" 
					   optionLabelProperty="sgTipo"
					   optionProperty="idTipoSeguro" 
					   service="lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction.findComboTipoSeguro"  
					   label="tipoSeguro" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeTipoSeguro()"/>

		<adsm:hidden property="dsTipoSinistroHidden"/>
		<adsm:combobox property="tipoSinistro.idTipo" 
					   optionLabelProperty="dsTipo" 
					   optionProperty="idTipoSinistro" 
					   service="lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction.findComboTipoSinistro" 
					   label="tipoSinistro" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeTipoSinistro()"/>

		<adsm:hidden property="moedaHidden"/>
		<adsm:combobox property="moeda.idMoeda" 
					   service="lms.indenizacoes.emitirRelatorioIndenizacoesAction.findComboMoeda" 
					   optionProperty="idMoeda" 
					   optionLabelProperty="siglaSimbolo" 
					   label="valor" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeForSetHiddenValue(this, 'moedaHidden')">
	  		<adsm:range>
				<adsm:textbox dataType="currency" property="vlInicial" picker="false" />
				<adsm:textbox dataType="currency" property="vlFinal" picker="false" />
			</adsm:range>
		</adsm:combobox> 

		<adsm:buttonBar>
			<adsm:button caption="visualizarDetalhado"  onclick="relatorioDetalhado()" buttonType="reportButton"/>
			<adsm:button caption="visualizarResumido"  onclick="relatorioResumido()" buttonType="reportButton"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript" >
function relatorioResumido() {
	reportButtonScript('lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction.executeRelatorioResumido', 'openPdf', document.forms[0]);
}

function relatorioDetalhado() {
	reportButtonScript('lms.indenizacoes.emitirRelatorioTempoMedioIndenizacaoAction.executeRelatorioDetalhado', 'openPdf', document.forms[0]);
}

function onChangeForSetHiddenValue(combo, strHidden) {
	setElementValue(strHidden, combo.options[combo.selectedIndex].text);	
}

function carregaComboRegional() {
	var dtInicial = getElementValue("dtInicial");
	var dtFinal = getElementValue("dtFinal");
	
	if (dtInicial!="" && dtFinal!="") {
		setDisabled("regional.idRegionalFilial", false);
		var criteria = new Array();
	    setNestedBeanPropertyValue(criteria, "dtInicial", dtInicial);
	    setNestedBeanPropertyValue(criteria, "dtFinal", dtFinal);	
		
	    var sdo = createServiceDataObject("lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction.findComboRegional", "regional_idRegionalFilial", criteria);
	    xmit({serviceDataObjects:[sdo]});
	} else {
		setDisabled("regional.idRegionalFilial", true);
		var map = new Array();
		regional_idRegionalFilial_cb(map);
	}
}

function initWindow(){
	document.getElementById("filial.sgFilial").serializable=true;
	setDisabled("regional.idRegionalFilial", true);
}

function onChangeRegional() {
    var combo = document.getElementById("regional.idRegionalFilial");
    setElementValue('sgAndDsRegionalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeRegional() {
    var combo = document.getElementById("regional.idRegionalFilial");
    setElementValue('sgAndDsRegionalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeModal() {
    var combo = document.getElementById("tpModal");
    setElementValue('tpModalHidden', combo.options[combo.selectedIndex].text);
}

function onChangeAbrangencia() {
    var combo = document.getElementById("tpAbrangencia");
    setElementValue('tpAbrangenciaHidden', combo.options[combo.selectedIndex].text);
}

function onChangeIndenizacao() {
    var combo = document.getElementById("tpIndenizacao");
    setElementValue('tpIndenizacaoHidden', combo.options[combo.selectedIndex].text);
}

function onChangeMotivoAberturaNc() {
    var combo = document.getElementById("motivoAberturaNc.idMotivoAberturaNc");
    setElementValue('dsMotivoAberturaHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSeguro() {
    var combo = document.getElementById("tipoSeguro.idTipoSeguro");
    setElementValue('dsTipoSeguroHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSinistro() {
    var combo = document.getElementById("tipoSinistro.idTipo");
    setElementValue('dsTipoSinistroHidden', combo.options[combo.selectedIndex].text);
}

</script>