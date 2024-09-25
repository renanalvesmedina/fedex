<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.frequenciaService">
	<adsm:form action="/configuracoes/manterFrequencias" idProperty="idFrequencia" onDataLoadCallBack="myOnDataLoad">
		<adsm:textbox dataType="text" size="70%" width="55%" property="dsFrequencia" label="descricao" maxLength="60" required="true"/>
		<adsm:combobox property="tpFrequenciaIndicadorFinanc" label="tipo" domain="DM_FREQ_IND_FINANCEIRO" onchange="habilitaDesabilitaCampo();" required="true"/>				
		<adsm:textbox dataType="integer" property="ddCorte" label="diaCorte" maxLength="2" size="2" onchange="return validateDia(this);" disabled="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button buttonType="newButton" caption="novo" onclick="newButtonScript(); setDisabled('ddCorte',true);" disabled="false"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
<script>
function myOnDataLoad_cb(data, error){
	onDataLoad_cb(data, error);
	habilitaDesabilitaCampo();
}

/**
Se o tipo de freqüência é mensal
tem que habilitar o dia de
corte.
*/
function habilitaDesabilitaCampo(){
	if (getElementValue("tpFrequenciaIndicadorFinanc") == "M"){
		setDisabled("ddCorte", false);
		document.getElementById("ddCorte").required = "true";		
	} else {
		resetValue("ddCorte");
		setDisabled("ddCorte", true);		
		document.getElementById("ddCorte").required = "false";		
	}
}

/**
Validar o minValue e maxValue do dia de corte.
*/
function validateDia(obj){
	if (validate(obj)==true) {
		var valido = true;		
		if (!compareData("1",obj.value,obj.dataType,"",obj.mask)){
			valido = false;
		}	
		if (!compareData(obj.value,"31",obj.dataType,obj.mask,"")){
			valido = false;
		}
		
		if (valido == false){
			alert('<adsm:label key="LMS-27013"/>');
			setFocus(obj);
			return false;
		} else {
			return true;
		}
	} else {
		return false;
	}
}
</script>		
	</adsm:form>
</adsm:window>