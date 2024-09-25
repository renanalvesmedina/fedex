<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window onPageLoadCallBack="myOnPageLoad">
	<adsm:form id="observacao.form" action="/contasReceber/manterMovimentoChequesPreDatados">

		<adsm:textbox label="dataReapresentacao" property="dtReapresentacao" dataType="JTDate" width="85%" required="true"/>

		<adsm:textarea label="observacao" property="observacao" width="85%" maxLength="255" 
			columns="70" rows="5"/>

		<adsm:buttonBar>
			<adsm:button caption="confirmar" onclick="setValue();" disabled="false"/>
			<adsm:button caption="cancelar" onclick="javascript:window.close();" disabled="false"/>
		</adsm:buttonBar>

	</adsm:form>

</adsm:window>

<script>
	function myOnPageLoad_cb(){
		_serviceDataObjects = new Array();

		addServiceDataObject(createServiceDataObject("lms.contasreceber.manterMovimentoChequesPreDatadosAction.findDataAtual", "setData")); 
		xmit(false);		
	}
	
	function setData_cb(d,e,c){
		if (d != null){
			setElementValue("dtReapresentacao", setFormat(getElement("dtReapresentacao"), d._value));
		}

	}

	function setValue(){
		valid = validateTabScript(document.getElementById("observacao.form"));

		// apenas prossegue se a valida??o dos dados foi realizada com sucesso.
		if (valid == false) {
			return false;
		}			
	
		dialogArguments.window.document.getElementById("observacao").value = document.getElementById("observacao").value;
		dialogArguments.window.document.getElementById("dtReapresentacao").value = getUnformattedValue(document.getElementById("dtReapresentacao").dataType, document.getElementById("dtReapresentacao").value, document.getElementById("dtReapresentacao").mask, true);
		dialogArguments.window.aplicarAcaoScript();
		window.close();
	}
</script>