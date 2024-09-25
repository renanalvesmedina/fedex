<script type="text/javascript">
<!--
	function initWindow(event) {
		setDisabled("limpar", false);
		if(event.name == "tab_load" || event.name == "tab_click") {
			var id = getElementValue("ciaFilialMercurio");
			if(id==""){
				var tabGroup = getTabGroup(this.document);
				tabGroup.setDisabledTab("awb", true);
			}
		}
	}

	/**
	*  OnChange da lookup de Cia Aerea.
	*/
	function ciaAerea_onchange() {
		var sgAnterior = getElementValue("sgFilial");
		var sgAtual = getElementValue("ciaFilialMercurio.filial.sgFilial");
		var equal = (sgAnterior == sgAtual);
		
		var idCia = getElementValue("ciaFilialMercurio.idCiaFilialMercurio"); 
		if (idCia != "" && !equal){
			var sdo = createServiceDataObject("lms.expedicao.emitirRelacaoCargaAereaAction.removeAWBs", "ciaAerea_onchange", {});
       		xmit({serviceDataObjects:[sdo]});
       	}
		return ciaFilialMercurio_filial_sgFilialOnChangeHandler();
	}
	function ciaAerea_onchange_cb(data, errorMsg){
		if (errorMsg) {
			alert(errorMsg);
			return false;
		}else{
			var idCia = getElementValue("ciaFilialMercurio.idCiaFilialMercurio");
			var tabGroup = getTabGroup(this.document);
			if (idCia != ""){
				tabGroup.setDisabledTab("awb", false);
			}else{
				tabGroup.setDisabledTab("awb", true);
			}
		}
	}

	/**
	*  Callback da lookup de Cia Aerea.
	*/
	function ciaAerea_cb(data) {
		if(data == undefined || data.length == 0){
		} else {
			var tabGroup = getTabGroup(this.document);
			tabGroup.setDisabledTab("awb", false);
		}
		return ciaFilialMercurio_filial_sgFilial_exactMatch_cb(data);
	}
	
	/**
	* onPopupSetValue da lookup de Cia Aerea.
	*/
	function ciaAerea_onpopup(data){
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("awb", false);
	}
	
//-->
</script>

<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.emitirRelacaoCargaAereaAction">
	<adsm:i18nLabels>
		<adsm:include key="LMS-04133"/>
	</adsm:i18nLabels>	

	<adsm:form action="/expedicao/emitirRelacaoCargaAerea">

		<adsm:hidden property="flag" serializable="false" value="01"/>
		<adsm:hidden property="empresa.pessoa.nmPessoa" serializable="true"/>
		<adsm:hidden property="empresa.idEmpresa" serializable="true"/>
		<adsm:hidden property="empresa.sgEmpresa" serializable="true"/>
		<adsm:hidden property="sgFilial" serializable="true"/>
		
<%--		
		<adsm:lookup 
			dataType="text" 
			property="ciaFilialMercurio"
			service="lms.expedicao.emitirRelacaoCargaAereaAction.findCiaAerea"
			action="/municipios/manterCiasAereasFiliais" 
			idProperty="idCiaFilialMercurio" 
			criteriaProperty="filial.sgFilial"
			onchange="return ciaAerea_onchange();"
			onDataLoadCallBack="ciaAerea"
			onPopupSetValue="ciaAerea_onpopup"
			label="ciaAerea" 
			size="3"
			maxLength="3" 
			width="85%" 
			minLengthForAutoPopUpSearch="3" 
			exactMatch="true"
			required="false"
		>
			<adsm:propertyMapping relatedProperty="sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="ciaFilialMercurio.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa"/>
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
	    	<adsm:textbox 
	    		dataType="text" 
	    		property="ciaFilialMercurio.filial.pessoa.nmFantasia"
	    		size="30" 
	    		disabled="true" 
	    		serializable="true"
	    	/>
	    </adsm:lookup>
--%>

 		<adsm:combobox property="ciaFilialMercurio" 
 					   optionLabelProperty="empresa.pessoa.nmPessoa" 
 					   optionProperty="idCiaFilialMercurio"   
 					   service="lms.expedicao.emitirRelacaoCargaAereaAction.findCiaAerea" 
 					   onlyActiveValues="true" onchange="return changeCombo(this)"
 					   label="ciaAerea" required="true" width="60%" serializable="true">
		    <adsm:propertyMapping relatedProperty="sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="empresa.idEmpresa" modelProperty="empresa.idEmpresa"/>
			<adsm:propertyMapping relatedProperty="empresa.sgEmpresa" modelProperty="empresa.sgEmpresa"/>
			<adsm:propertyMapping relatedProperty="empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa"/>
			</adsm:combobox>
		
		<adsm:textarea maxLength="600" property="observacao" columns="80" label="observacao" rows="2" width="50%" />

		<adsm:buttonBar>
			<!-- expedicao/emitirRelacaoCargaAerea.jasper -->
			<adsm:reportViewerButton caption="emitirRelacaoCarga" service="lms.expedicao.emitirRelacaoCargaAereaAction"/>
			<adsm:button caption="limpar" id="limpar" disabled="false" onclick="return resetButton_onclick()"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--

	function changeCombo(obj){
		comboboxChange({e:obj});
		changeComboCiaAerea();
	}
	
	function  changeComboCiaAerea() {
		var sdo = createServiceDataObject("lms.expedicao.emitirRelacaoCargaAereaAction.removeAWBs", "changeCombo", {});
       	xmit({serviceDataObjects:[sdo]});
	}
	
	function changeCombo_cb(data,erro) {
		if (erro) {
			alert(erro);
			return false;
		}else{
			var tabGroup = getTabGroup(this.document);
			if(getElementValue("ciaFilialMercurio")!= "") {
				tabGroup.setDisabledTab("awb", false);
			}else{
				tabGroup.setDisabledTab("awb", true);
			}
		}
	}
	
	
	function getIdCiaAerea(){
		var idCA = getElementValue("ciaFilialMercurio");
		return idCA;
	}

	function getIdEmpresa(){
		var idEmpresa = getElementValue("empresa.idEmpresa");
		return idEmpresa;
	}
	
	function getSgEmpresa(){
		return getElementValue("empresa.sgEmpresa");
	}

	function resetButton_onclick(){
		changeComboCiaAerea();
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("awb", true);
		newButtonScript(document);
		return true;
	}

	function validateTab(){
		return validateTabScript(document.forms);
	}

	function validaEmissao_cb(data, errorMsg){
		if (errorMsg) {
			alert(errorMsg);
			return false;
		}else{
			if (data._value == "false"){
				alert(i18NLabel.getLabel("LMS-04133"));
			}else{
			
			}
		}
	}
//-->
</script>