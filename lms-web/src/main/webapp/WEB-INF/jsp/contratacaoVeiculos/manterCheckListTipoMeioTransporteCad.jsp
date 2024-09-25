<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function pageLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		acaoVigencia(acaoVigenciaAtual, null);
	}
	
	
	function acaoVigencia(acaoVigenciaAtual, tipoEvento) {
		if (acaoVigenciaAtual == 0) {
			  enabledFields();
			  setDisabled("removeButton",false);
			  if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
		     else
		       setFocus(document.getElementById("newButton"),false);
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("storeButton",false);
		      setDisabled("newButton",false);
		      setDisabled("blObrigatorioAprovacao",false);
		      setDisabled("dtVigenciaFinal",false);
		      if(tipoEvento == "" ||  tipoEvento == null)
     			setFocusOnFirstFocusableField(document);
		      else
		       setFocus(document.getElementById("newButton"),false);
		      //setFocus(document.getElementById("blObrigatorioAprovacao"),false);
		}else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("newButton",false);
		      setFocus(document.getElementById("newButton"),false);
		}
	}
	
	function enabledFields() {
		setDisabled(document,false);
		setDisabled("removeButton",true);
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click")) {
			enabledFields();
			setFocusOnFirstFocusableField(document);
		}
	}
	
	function afterStore_cb(data,exception,key) {
    	store_cb(data,exception,key);
		if (exception == undefined) {
			var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
			var store = "true";
			acaoVigencia(acaoVigenciaAtual, store);
		}
    }  
	
//-->
</script>
<adsm:window service="lms.contratacaoveiculos.manterCheckListTipoMeioTransporteAction">
	<adsm:form action="/contratacaoVeiculos/manterCheckListTipoMeioTransporte" onDataLoadCallBack="pageLoad" idProperty="idItChecklistTpMeioTransp">
		<adsm:combobox property="tipoMeioTransporte.tpMeioTransporte" label="modalidade" labelWidth="20%" width="30%" domain="DM_TIPO_MEIO_TRANSPORTE" required="true" onlyActiveValues="true" boxWidth="190"/>
		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte" label="tipoMeioTransporte" service="lms.contratacaoveiculos.manterCheckListTipoMeioTransporteAction.findLookupTipoMeioTransporte"
						optionLabelProperty="dsTipoMeioTransporte" optionProperty="idTipoMeioTransporte" labelWidth="20%" width="30%" required="true" onlyActiveValues="true" boxWidth="190">
			<adsm:propertyMapping modelProperty="tpMeioTransporte" criteriaProperty="tipoMeioTransporte.tpMeioTransporte"/>
		</adsm:combobox>
		<adsm:combobox property="itemCheckList.idItemCheckList" label="itemCheckList" service="lms.contratacaoveiculos.manterCheckListTipoMeioTransporteAction.findComboitemCheckList" boxWidth="190"
						optionLabelProperty="dsItemCheckList" optionProperty="idItemCheckList" labelWidth="20%" width="30%" onlyActiveValues="true" required="true">
			<adsm:propertyMapping modelProperty="tpMeioTransporte" criteriaProperty="tipoMeioTransporte.tpMeioTransporte"/>
		</adsm:combobox>
		
		<adsm:combobox property="tpItChecklistTpMeioTransp" required="true" label="tipoItem" domain="DM_TIPO_ITEM_CHECK_LIST_VEICULO" boxWidth="190" labelWidth="20%" width="30%" onlyActiveValues="true"/>
		<adsm:checkbox property="blObrigatorioAprovacao" label="obrigatorioAprovacao" labelWidth="20%" width="30%"/>
		
		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton" callbackProperty="afterStore"/>
			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>    