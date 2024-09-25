<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function setaValorComboProcessoPce_cb(data,exception){
		onPageLoad_cb(data,exception);
		if(document.getElementById("idProcessoPceTemp").masterLink == "true"){

			setElementValue(document.getElementById("processoPce.idProcessoPce"),document.getElementById("idProcessoPceTemp").value);
			document.getElementById("processoPce.idProcessoPce").masterLink = "true";
			var idProcessoPce = getElementValue("idProcessoPceTemp");

			document.getElementById("cdEventoPce").focus();
			document.getElementById("processoPce.idProcessoPce").disabled = true;
		}
	}
   
</script>
<adsm:window service="lms.vendas.manterEventosAction" onPageLoadCallBack="setaValorComboProcessoPce">
	<adsm:form action="/vendas/manterEventos">
	    <adsm:hidden property="idProcessoPceTemp"/>
		<adsm:combobox property="processoPce.idProcessoPce" optionLabelProperty="processoPceCombo" optionProperty="idProcessoPce" service="lms.vendas.manterEventosAction.findProcessoPceByUsuarioLogado" label="processo" width="35%" boxWidth="220"/>
		
		<adsm:textbox maxLength="10" size="10" dataType="integer" property="cdEventoPce" label="codigo" width="35%"/>
		
		<adsm:textbox maxLength="60" dataType="text" property="dsEventoPce" label="evento" size="40" width="35%"/>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" width="35%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="eventoPce"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="eventoPce" idProperty="idEventoPce" gridHeight="200" rows="13" unique="true"
			defaultOrder="processoPce_.tpModal,processoPce_.tpAbrangencia,dsEventoPce">
		<adsm:gridColumn title="modal" property="processoPce.tpModal" width="15%" isDomain="true"/>
		<adsm:gridColumn title="abrangencia" property="processoPce.tpAbrangencia" width="15%" isDomain="true"/>
		<adsm:gridColumn title="processo" property="processoPce.processoPceGrid" width="20%" />
		<adsm:gridColumn title="codigo" property="cdEventoPce" width="10%" dataType="integer"/>
		<adsm:gridColumn title="evento" property="dsEventoPce" width="30%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
