<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction" onPageLoadCallBack="retornoCarregaPagina">
	<adsm:form action="/indenizacoes/emitirRIMsEmitidosNaoPagos">
		
		<adsm:range label="periodo" width="80%" labelWidth="20%" required="true">
			<adsm:textbox dataType="JTDate" property="dtInicial" picker="true" onchange="carregaComboRegional()"/>
			<adsm:textbox dataType="JTDate" property="dtFinal" picker="true" onchange="carregaComboRegional()"/>
		</adsm:range>
		
		<adsm:lookup label="filial" labelWidth="20%" width="80%" 
             		 property="filial"
                     idProperty="idFilial"
	                 criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3">
		    <adsm:propertyMapping criteriaProperty="flagBuscaEmpresaUsuarioLogado"  modelProperty="flagBuscaEmpresaUsuarioLogado"/>
      		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
      		<adsm:hidden property="flagBuscaEmpresaUsuarioLogado" value="true" serializable="false"/>
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
					   service="lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction.findComboMotivoAberturaNc"
					   onchange="onChangeMotivoAberturaNc()"/>

		<adsm:hidden property="sgTipoSeguroHidden"/> 
		<adsm:combobox property="tipoSeguro.idTipoSeguro" 
					   optionLabelProperty="sgTipo" 
					   optionProperty="idTipoSeguro" 
					   service="lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction.findComboTipoSeguro"  
					   label="tipoSeguro" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeTipoSeguro()"/>

		<adsm:hidden property="dsTipoSinistroHidden"/>
		<adsm:combobox property="tipoSinistro.idTipo" 
					   optionLabelProperty="dsTipo" 
					   optionProperty="idTipoSinistro" 
					   service="lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction.findComboTipoSinistro" 
					   label="tipoSinistro" 
					   width="80%" 
					   labelWidth="20%"
					   onchange="onChangeTipoSinistro()"/>

		<adsm:textbox label="numeroProcessoSinistro" property="numeroProcessoSinistro1" dataType="text" size="20" maxLength="20" width="80%" labelWidth="20%">
			</adsm:textbox>

		<adsm:buttonBar>
			<adsm:reportViewerButton  service="lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction"/>
			<adsm:button caption="limpar" id="botaoLimpar" onclick="limpar_OnClick(this.form);" disabled="false" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript" >
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
    setElementValue('sgTipoSeguroHidden', combo.options[combo.selectedIndex].text);
}

function onChangeTipoSinistro() {
    var combo = document.getElementById("tipoSinistro.idTipo");
    setElementValue('dsTipoSinistroHidden', combo.options[combo.selectedIndex].text);
}

function limpar_OnClick(form) {
	resetFormValue(form);
	carregaComboRegional();
	validateUsarioMatriz();
	setFocusOnFirstFocusableField();
}

function retornoCarregaPagina_cb(data, error) {
	onPageLoad_cb(data, error);
	if (error == undefined) {
		validateUsarioMatriz();
	}
}

function validateUsarioMatriz() {
	var sdo = createServiceDataObject("lms.indenizacoes.emitirRIMsEmitidosNaoPagosAction.validateUsarioMatriz", "retornoValidate");
   	xmit({serviceDataObjects:[sdo]});
}

function retornoValidate_cb(data, error) {
	if (error != undefined) {
		alert(error);
		return false;
	}
	setDisabled("botaoLimpar", false);
	if (getNestedBeanPropertyValue(data, "blFilialUsuario") == "false") {
		setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
		setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
		setDisabled("filial.idFilial", true);
		setFocusOnFirstFocusableField();
	}
}
</script>