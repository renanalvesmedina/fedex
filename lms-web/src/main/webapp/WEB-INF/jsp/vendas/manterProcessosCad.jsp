<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
	function afterStore_cb(data,exception) {
		store_cb(data,exception);
	}
	
	function buildLinkPropertiesQueryString_eventos() {
		var qs = "";
			qs += "&idProcessoPceTemp=" + document.getElementById("idProcessoPce").value;
			qs += "&cdProcessoPceTemp=" + document.getElementById("cdProcessoPce").value;
			qs += "&dsProcessoPceTemp=" + document.getElementById("dsProcessoPce").value;
			qs += "&tpModaTemp=" + document.getElementById("tpModal")[document.getElementById("tpModal").selectedIndex].text;
			qs += "&tpAbrangenciaTemp=" + document.getElementById("tpAbrangencia")[document.getElementById("tpAbrangencia").selectedIndex].text;
			qs += "&tpModaValueTemp=" + getElementValue("tpModal");
			qs += "&tpAbrangenciaValueTemp=" + getElementValue("tpAbrangencia");
			
		return qs;
	}
//-->
</script>
<adsm:window service="lms.vendas.manterProcessosAction">
	<adsm:form action="/vendas/manterProcessos" idProperty="idProcessoPce">
	    <adsm:textbox maxLength="10" dataType="integer" property="cdProcessoPce" label="codigo" size="10" required="true"/>
		<adsm:textbox maxLength="60" dataType="text" property="dsProcessoPce" label="processo" size="40" required="true"/>
		<adsm:combobox property="tpModal" domain="DM_MODAL" label="modal"/>
		<adsm:combobox property="tpAbrangencia" domain="DM_ABRANGENCIA" label="abrangencia"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="eventos" onclick="parent.parent.redirectPage('vendas/manterEventos.do?cmd=main' + buildLinkPropertiesQueryString_eventos());">
				<adsm:linkProperty src="idProcessoPce" target="idProcessoPceTemp"/>
				<adsm:linkProperty src="cdProcessoPce" target="cdProcessoPceTemp"/>
				<adsm:linkProperty src="dsProcessoPce" target="dsProcessoPceTemp"/>
				<adsm:linkProperty src="tpModal.description" target="tpModaTemp"/>
				<adsm:linkProperty src="tpAbrangencia.description" target="tpAbrangenciaTemp"/>
			</adsm:button>
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

