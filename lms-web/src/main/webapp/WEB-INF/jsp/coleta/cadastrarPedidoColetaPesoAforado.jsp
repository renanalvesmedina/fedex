<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<script type="text/javascript">
function carregaPagina() {
	onPageLoad();
	
	// Rotina que pega a referência da tela pai para usar parametros ou chamar funções
	// que serão usadas na tela filho.
	var doc;
	if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {
		doc = window.dialogArguments.window.document;
	} else {
	   doc = document;
	}
	
	// Pega parâmetros da tela pai.
	var tabGroup = getTabGroup(doc);
	var tabDet = tabGroup.getTab("detalheColeta");
	var volumes = tabDet.getFormProperty("qtVolumes");
	var psMercadoria = dropFormat(tabDet.tabOwnerFrame.document.getElementById("psMercadoria"));
	var idServico = tabDet.getFormProperty("servico.idServico");
	
	setElementValue("volumeDetalhe", volumes);
	setElementValue("pesoDetalhe", psMercadoria);	
	setElementValue("idServico", idServico);
}
</script>

<adsm:window title="pesoAforado" service="lms.coleta.cadastrarPedidoColetaPesoAforadoAction" onPageLoad="carregaPagina">
	<adsm:form action="/coleta/cadastrarPedidoColeta">
		<adsm:hidden property="volumeDetalhe" />
		<adsm:hidden property="pesoDetalhe" />
		<adsm:hidden property="idServico" />
	
		<adsm:section caption="pesoAforado"/>
		<adsm:textbox label="altura" property="altura" dataType="decimal" 
					  width="85%" unit="cm" onchange="return verificaObAltLarPro();"/>
		<adsm:textbox label="largura" property="largura" dataType="decimal" 
					  width="85%" unit="cm" onchange="return verificaObAltLarPro();"/>
		<adsm:textbox label="profundidade" property="profundidade" dataType="decimal" 
					  width="85%" unit="cm" onchange="return verificaObAltLarPro();"/>

		<adsm:textbox label="volume" property="volume" dataType="decimal" 
					  width="85%" unit="m3" onchange="return verificaObVol();"/>
		<adsm:textbox label="pesoAforado" property="pesoAforado" dataType="weight" 	
					  width="85%" unit="kg" disabled="true"
					  size="20" maxLength="18"/>
		<br>
		<adsm:label key="branco" width="5%"/>			  		
		<adsm:label key="LMS-02030" width="30%" style="margin-left: 10%; text-align:center; font-weight: bold;"/>
		
		<adsm:buttonBar freeLayout="false"> 
			<adsm:button id="calcularButton" caption="calcular" disabled="true" onclick="calculoPesoAforado(this.form);"/>
			<adsm:button id="confirmarButton" caption="confirmar" disabled="true" onclick="confirmar();"/>
			<adsm:button id="limparButton" caption="limpar" disabled="false" onclick="limpaForm();" />
			<adsm:button id="fecharButton" caption="fechar" disabled="false" onclick="fechaDesmarcaAforado();"/>
		</adsm:buttonBar>
	</adsm:form>

</adsm:window>

<script type="text/javascript">

	function verificaObAltLarPro() {
		var altura = getElementValue("altura");
		var largura = getElementValue("largura");
		var profundidade = getElementValue("profundidade");
	
		resetValue("volume");
		resetValue("pesoAforado");
		setDisabled("confirmarButton", true);
		setDisabled("calcularButton", true);
	
		if(altura.length == 0 && largura.length == 0 && profundidade.length == 0) {
			document.getElementById("altura").required = "false";
			document.getElementById("largura").required = "false";
			document.getElementById("profundidade").required = "false";
			resetValue("volume");
			setDisabled("volume", false);
		} else if(altura.length > 0 && largura.length > 0 && profundidade.length > 0) {
			setDisabled("calcularButton", false);
		} else if(altura.length > 0 || largura.length > 0 || profundidade.length > 0) {
			document.getElementById("altura").required = "true";
			document.getElementById("largura").required = "true";
			document.getElementById("profundidade").required = "true";
			setDisabled("volume", true);
		}
		
		
		
		return true;
	}
	
	function verificaObVol() {
		var volume = getElementValue("volume");
	
		if(volume.length > 0) {
			document.getElementById("volume").required = "true";
			setDisabled("altura", true);
			setDisabled("largura", true);
			setDisabled("profundidade", true);
			setDisabled("calcularButton", false);
		} else if(volume.length == 0) {
			document.getElementById("volume").required = "false";
			setDisabled("altura", false);
			setDisabled("largura", false);
			setDisabled("profundidade", false);
			setDisabled("calcularButton", true);
		}
		
		return true;
	}
	
	/**
	 * Faz o calculo do Peso Aforado
	 */
	function calculoPesoAforado(form) {	
		if (!validateForm(form)) {
			return false;
		}	
		
		setElementValue("pesoAforado", "");

		var mapCriteria = new Array();    	
	    setNestedBeanPropertyValue(mapCriteria, "altura", dropFormat(document.getElementById("altura")));
	    setNestedBeanPropertyValue(mapCriteria, "largura", dropFormat(document.getElementById("largura")));
	    setNestedBeanPropertyValue(mapCriteria, "profundidade", dropFormat(document.getElementById("profundidade")));
	    setNestedBeanPropertyValue(mapCriteria, "volume", dropFormat(document.getElementById("volume")));
	    setNestedBeanPropertyValue(mapCriteria, "volumeDetalhe", dropFormat(document.getElementById("volumeDetalhe")));
	    setNestedBeanPropertyValue(mapCriteria, "pesoDetalhe", getElementValue("pesoDetalhe"));
	    setNestedBeanPropertyValue(mapCriteria, "idServico", document.getElementById("idServico").value);
	
	    var sdo = createServiceDataObject("lms.coleta.cadastrarPedidoColetaPesoAforadoAction.calculoPesoAforado", "resultado_calculoPesoAforado", mapCriteria);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Retorno do calculo do Peso Aforado no método calculoPesoAforado.
	 */
	function resultado_calculoPesoAforado_cb(data, error) {	
		if(!error) {
			if(data.volume != undefined) {
				setElementValue("volume", setFormat(document.getElementById("volume"), data.volume));
				setDisabled("volume", true);
			}
			
			setElementValue("pesoAforado", setFormat(document.getElementById("pesoAforado"), data.pesoAforado));
			setDisabled("confirmarButton", false);

		} else {			
			alert(error);
		}
	}
	
	function confirmar(){
		// Chama função da tela pai que pega o resultado para atualizar o campo na mesma.	
		if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {			
			window.dialogArguments.window.resultadoRotinaCalculoPeso(getElementValue("pesoAforado"));
		}
		window.close();
	}
	
	
	/**
	 * Fecha a PopUp e desmarca o checkbox de Aforado
	 */
	function fechaDesmarcaAforado() {
		// Chama função da tela pai que pega o resultado para atualizar o campo na mesma.	
		if (window.dialogArguments != undefined && window.dialogArguments.window != undefined) {			
			window.dialogArguments.window.desmarcaAforado();
		}
		window.close();
	}
	
	function limpaForm(){
		resetValue("volume");
		resetValue("pesoAforado");
		resetValue("altura");
		resetValue("largura");
		resetValue("profundidade");
		setDisabled("altura", false);
		setDisabled("largura", false);
		setDisabled("profundidade", false);
		setDisabled("volume", false);
		setDisabled("calcularButton", true);
		setDisabled("confirmarButton", true);
		setDisabled("limparButton", false);
		setDisabled("fecharButton", false);
		document.getElementById("altura").required = "false";
		document.getElementById("largura").required = "false";
		document.getElementById("profundidade").required = "false";
		document.getElementById("volume").required = "false";
	}

</script>