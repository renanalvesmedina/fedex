<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">

	function inicializa() {
		var objTextAreaLog = document.getElementById("log");
		objTextAreaLog.wrap="off";
		objTextAreaLog.readOnly="true";
		
    	var data = new Array();
		var sdo = createServiceDataObject("lms.coleta.gerarColetasAutomaticasAction.inicializaTela", "inicializaTela", data);
   		xmit({serviceDataObjects:[sdo]});

	}
	
	function inicializaTela_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
		setElementValue("idFilial", getNestedBeanPropertyValue(data, "idFilial"));
		setElementValue("sgFilial", getNestedBeanPropertyValue(data, "sgFilial"));
		setElementValue("dsFilial", getNestedBeanPropertyValue(data, "dsFilial"));
		setElementValue("dataProcessoColeta", setFormat(document.getElementById("dataProcessoColeta"), getNestedBeanPropertyValue(data, "dataProcessoColeta")));
		setElementValue("ultimoPedidoColeta", getNestedBeanPropertyValue(data, "ultimoPedidoColeta"));
		setDisabled(document.getElementById("btnLimpar"), false);
		setDisabled(document.getElementById("btnConfirmarDataGerarColetas"), false);
	}
	
	function gerar(){
		var valido = validateForm(document.getElementById('form'));
		if (!valido){
			return false;
		}
		setDisabled(document.getElementById("btnConfirmarDataGerarColetas"), true);
		resetValue("log");
		var data = new Array();
		var idFilial = getElementValue('idFilial');
		var dataProcessoColeta = getElementValue('dataProcessoColeta');
		setNestedBeanPropertyValue(data, "idFilial", idFilial);
		setNestedBeanPropertyValue(data, "dataProcessoColeta", dataProcessoColeta);
		var sdo = createServiceDataObject("lms.coleta.gerarColetasAutomaticasAction.generateColetas", "geracao", data);
    	xmit({serviceDataObjects:[sdo]});
    }
    
    function limpar(){
    	resetValue(this.document);
    	inicializa();
    	setFocusOnFirstFocusableField();
    }
    
    function geracao_cb(data, error) {
		if (error){
			alert(error);
			return false;
		}
    	setElementValue("log",data.log);
		showSuccessMessage();
		inicializa();
		setDisabled(document.getElementById("btnConfirmarDataGerarColetas"), false);
		document.getElementById("btnLimpar").focus();
		
    }
    
</script>
<adsm:window service="lms.coleta.gerarColetasAutomaticasAction" onPageLoad="inicializa">
	<adsm:form action="/coleta/gerarColetasAutomaticas" id="form" height="800">
		<adsm:hidden property="idFilial" />
		<adsm:textbox dataType="text" property="sgFilial" width="85%" label="filial" size="3" maxLength="3" disabled="true">
			<adsm:textbox dataType="text" property="dsFilial" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>
		
		<adsm:textbox serializable="true" width="85%" property="dataProcessoColeta" label="dataProcesso" dataType="JTDate" size="10" maxLength="10" required="true"/>
		<adsm:textbox property="ultimoPedidoColeta" width="85%" label="numeroUltimaColeta" dataType="text" size="10" maxLength="10" disabled="true" />
		<adsm:textarea maxLength="50000" property="log" width="85%" columns="120" label="log" rows="10" disabled="false" serializable="false" style="overflow-x: scroll; width:640px; height:300px"/>
		<adsm:buttonBar freeLayout="false">
			<adsm:button caption="confirmarDataGerarColetas" id="btnConfirmarDataGerarColetas" onclick="gerar()"/>
			<adsm:button caption="limpar" buttonType="resetButton" id="btnLimpar" onclick="limpar()"/>			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>