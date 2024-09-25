<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarDimensoesAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/expedicao/digitarDimensoes" idProperty="idDimensao">
		<adsm:section caption="dimensoes" width="56"/>
		<adsm:label key="branco" style="border:none;" width="1%"/>
		<adsm:textbox dataType="integer" size="8" property="nrAltura" required="true" label="altura" width="17%" labelWidth="13%" unit="cm" maxLength="8" minValue="1"/>
		<adsm:textbox dataType="integer" size="8" property="nrLargura" required="true" label="largura" width="56%" labelWidth="13%" unit="cm" maxLength="8" minValue="1"/>
		<adsm:label key="branco" style="border:none;" width="1%"/>
		<adsm:textbox dataType="integer" size="8" property="nrComprimento" required="true" label="comprimento" width="17%" labelWidth="13%" unit="cm" maxLength="8" minValue="1"/>
		<adsm:textbox dataType="integer" size="8" property="nrQuantidade"  required="true" label="quantidade" width="56%" labelWidth="13%" maxLength="5" minValue="1"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton id="storeButton" service="lms.expedicao.digitarDimensoesAction.storeInSession" callbackProperty="storeSession"/>
			<adsm:button caption="novo" disabled="false" buttonType="cleanButton" id="cleanButton" onclick="limparCampos();"/>
			<adsm:button caption="fechar" buttonType="closeButton" id="closeButton" disabled="false" onclick="closeModal();"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDimensao" showGotoBox="false" property="dimensao" onRowClick="populaForm" rows="5"
		gridHeight="115" unique="true" showTotalPageCount="false">
		<adsm:gridColumn title="altura" unit="cm" property="nrAltura" width="23%" align="right"/>
		<adsm:gridColumn title="largura" unit="cm" property="nrLargura" width="23%" align="right"/>
		<adsm:gridColumn title="comprimento" unit="cm" property="nrComprimento" width="30%" align="right"/>
		<adsm:gridColumn title="quantidade" property="nrQuantidade" width="24%" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript" src="../lib/expedicao.js"></script>
<script language="javascript" type="text/javascript">
	var idDocumento;
	var tpDocumento;
	var idProcessoWorkflow;
	function myOnPageLoad_cb() {
		onPageLoad_cb();
		var u = new URL(parent.location.href);
		idDocumento = u.parameters["idDocumento"];
		tpDocumento = u.parameters["tpDocumento"];
		idProcessoWorkflow = u.parameters["idProcessoWorkflow"];
		if(!(idProcessoWorkflow!= undefined && idProcessoWorkflow!="")){
			if(!isBlank(idDocumento)) {
				if( isEdit() ){
					setDisabled(document, false);
				}else{ 
					setDisabled(document, true);
					setDisabled("closeButton", false);
					setFocus(document.getElementById("closeButton"), false);
				}
			} else {
				setDisabled(document, false);
				idDocumento = undefined;
			}
		}
		populaGrid();
		
		if(idProcessoWorkflow!= undefined && idProcessoWorkflow!=""){
			disabledAll();	
		}
		
	}

	
	function disabledAll(){
		setDisabled("nrAltura", true);
		setDisabled("nrLargura", true);
		setDisabled("nrComprimento", true);
		setDisabled("nrQuantidade", true);
		setDisabled("storeButton", true)
		setDisabled("cleanButton", true);
		setDisabled("removeButton", true);
		setDisabled("dimensao", true);
		setDisabled("document", true);
		setDisabled("closeButton", false);
		setFocus(document.getElementById("closeButton"), true);
	}
	
	function isEdit(){
		if( tpDocumento == "COT" ){
			return true;
		}
		return false;
	}

	
	function initWindow(eventObj) {
		if(eventObj.name == "removeButton_grid" && !(idProcessoWorkflow!= undefined && idProcessoWorkflow!="")) {
			setDisabled("removeButton", false);
		} 
	}

	// Funcao que limpa todos os campos da tela
	function limparCampos() {
		resetValue("idDimensao");
		resetValue("nrAltura");
		resetValue("nrLargura");
		resetValue("nrComprimento");
		resetValue("nrQuantidade");
		/* Desmarca todos checkBoxes */
		var checkAll = getElement("dimensao.chkSelectAll");
		checkAll.checked = false;
		ADSM.selectAll(dimensaoGridDef, getElement("dimensao.chkSelectAll"));

		setFocusOnFirstFocusableField();
	}

	// Função que popula a grid
	function populaGrid() {
		var param = null;
		if(idDocumento) {
			param = {idDocumento:idDocumento};
		}
		dimensaoGridDef.executeSearch(param, true);
	}

	// Callback da função que armazena os dados na sessão
	function storeSession_cb(data, erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		limparCampos();
		populaGrid();	
		setFocus(document.getElementById("closeButton"), false);
		return true;
	}

	// Método que chama o serviço da action para buscar o elemento selecionado na grid
	function populaForm(valor) {
		if(isBlank(idDocumento) || isEdit() && !(idProcessoWorkflow!= undefined && idProcessoWorkflow!="")){
			var sdo = createServiceDataObject("lms.expedicao.digitarDimensoesAction.findById", "findId", {idDimensao:valor} );
			xmit({serviceDataObjects:[sdo]});
		}
		return false;
	}

	// Callback que popula os campos da tela após o usuário 
	// selecionar uma linha da grid
	function findId_cb(data,erros){
		if (erros!=undefined){
			alert(erros)
			return false;
		}
		setElementValue("idDimensao",data.idDimensao);
		setElementValue("nrAltura",data.nrAltura);
		setElementValue("nrLargura",data.nrLargura);
		setElementValue("nrComprimento",data.nrComprimento);
		setElementValue("nrQuantidade",data.nrQuantidade);
		setFocusOnFirstFocusableField();	
	}

	function closeModal() {
		window.returnValue = true;
		self.close();
	}
</script>