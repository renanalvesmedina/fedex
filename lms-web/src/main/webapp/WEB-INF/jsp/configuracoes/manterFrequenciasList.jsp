<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.frequenciaService">
	<adsm:form action="/configuracoes/manterFrequencias">
		<adsm:textbox dataType="text" size="70%" width="55%" property="dsFrequencia" label="descricao" maxLength="60"/>
		<adsm:combobox property="tpFrequenciaIndicadorFinanc" label="tipo" domain="DM_FREQ_IND_FINANCEIRO" onchange="habilitaDesabilitaCampo();"/>				
		<adsm:textbox dataType="integer" property="ddCorte" label="diaCorte" maxLength="2" size="2" onchange="return validateDia(this);" disabled="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="frequencia"/>
			<adsm:button buttonType="resetButton" caption="limpar" onclick="cleanButtonScript(); setDisabled('ddCorte',true);" disabled="false"/>
		</adsm:buttonBar>
<script>
function habilitaDesabilitaCampo(){
	if (getElementValue("tpFrequenciaIndicadorFinanc") == "M"){
		setDisabled("ddCorte", false);
	} else {
		resetValue("ddCorte");
		setDisabled("ddCorte", true);		
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
	<adsm:grid idProperty="idFrequencia" property="frequencia" defaultOrder="dsFrequencia" gridHeight="200" unique="true" rows="12">
		<adsm:gridColumn title="descricao" property="dsFrequencia" width="68%" />
		<adsm:gridColumn title="tipo" property="tpFrequenciaIndicadorFinanc" isDomain="true" width="12%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true" />
		<adsm:gridColumn title="diaCorte" property="ddCorte" width="10%" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>