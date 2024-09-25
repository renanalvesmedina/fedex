<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function execTest() {
		var filter = new Array();
		setNestedBeanPropertyValue(filter, "empresa.idEmpresa", getElementValue("empresa.idEmpresa"));
		setNestedBeanPropertyValue(filter, "cliente.idCliente", getElementValue("cliente.idCliente"));
		setNestedBeanPropertyValue(filter, "vlPesoReal", getElementValue("vlPesoReal"));
		setNestedBeanPropertyValue(filter, "vlPesoCubado", getElementValue("vlPesoCubado"));
		setNestedBeanPropertyValue(filter, "aeroportoOrigem.idAeroporto", getElementValue("aeroportoOrigem.idAeroporto"));
		setNestedBeanPropertyValue(filter, "aeroportoDestino.idAeroporto", getElementValue("aeroportoDestino.idAeroporto"));
		setNestedBeanPropertyValue(filter, "blTarifaSpot", getElementValue("blTarifaSpot"));
		setNestedBeanPropertyValue(filter, "produtoEspecifico.idProdutoEspecifico", getElementValue("produtoEspecifico.idProdutoEspecifico"));
		
		var sdo = createServiceDataObject("lms.expedicao.freteCiaAereaService.find", "execTest", filter);
		xmit({serviceDataObjects:[sdo]});
	}

	function execTest_cb(data, error) {
		if(error != undefined) {
			alert(error);
			return;
		}
		alert(getNestedBeanPropertyValue(data,"_value"));
	}

</script>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/testarFreteCiaAerea">

		<adsm:textbox label="ciaAerea" property="empresa.idEmpresa" dataType="text" size="10" maxLength="18" required="true"/>
		<adsm:textbox label="expedidor" property="cliente.idCliente" dataType="text" size="10" maxLength="18"/>

		<adsm:textbox label="pesoReal" property="vlPesoReal" dataType="currency" size="10" maxLength="18" required="true"/>
		<adsm:textbox label="pesoCubado" property="vlPesoCubado" dataType="currency" size="10" maxLength="18" required="true"/>

		<adsm:textbox label="aeroportoDeOrigem" property="aeroportoOrigem.idAeroporto" dataType="text" size="10" maxLength="18" required="true"/>
		<adsm:textbox label="aeroportoDeDestino" property="aeroportoDestino.idAeroporto" dataType="text" size="10" maxLength="18" required="true"/>

		<adsm:checkbox label="tarifaSpot" property="blTarifaSpot"/>
		<adsm:textbox label="produtoEspecifico" property="produtoEspecifico.idProdutoEspecifico" dataType="text" size="10" maxLength="18"/>

		<adsm:buttonBar>
			<adsm:button id="testar" caption="testar" service="lms.expedicao.freteCiaAereaService" disabled="false"/>
			<adsm:button id="xmit" caption="xmit" onclick="execTest();" disabled="false"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function myOnPageLoad_cb(erro, msg) {
		onPageLoad_cb(erro, msg);
		setDisabled("testar", false);
		setDisabled("xmit", false);
	}
</script>