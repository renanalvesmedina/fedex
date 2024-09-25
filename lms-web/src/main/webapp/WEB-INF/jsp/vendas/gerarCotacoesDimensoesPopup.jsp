<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarCotacoesAction" onPageLoadCallBack="myOnPageLoad">
	<adsm:form action="/vendas/gerarCotacoesDimensoes" service="lms.vendas.gerarCotacoesAction.findDimensaoById" idProperty="idDimensao">
		<adsm:section caption="dimensoes" width="56"/>
		<adsm:label key="branco" style="border:none;" width="1%"/>
		<adsm:textbox dataType="integer" size="8" property="nrAltura" required="true" label="altura"
			width="17%" labelWidth="13%" unit="cm" maxLength="8" minValue="1"/>
		<adsm:textbox dataType="integer" size="8" property="nrLargura" required="true" label="largura"
			width="56%" labelWidth="13%" unit="cm" maxLength="8" minValue="1"/>
		<adsm:label key="branco" style="border:none;" width="1%"/>
		<adsm:textbox dataType="integer" size="8" property="nrComprimento" required="true" label="comprimento"
			width="17%" labelWidth="13%" unit="cm" maxLength="8" minValue="1"/>
		<adsm:textbox dataType="integer" size="8" property="nrQuantidade" required="true" label="quantidade"
			width="56%" labelWidth="13%" maxLength="5" minValue="1"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton service="lms.vendas.gerarCotacoesAction.storeDimensao"
			 	id="salvarButton" callbackProperty="storeDimensao"/>
			<adsm:button caption="novo" id="newButton" disabled="false" buttonType="cleanButton" onclick="limparCampos();"/>
			<adsm:button caption="fechar" buttonType="closeButton" id="closeButton" disabled="false" onclick="closeDimensoes();"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idDimensao" showGotoBox="false" property="dimensao"
		onRowClick="populaForm" rows="5"
		service="lms.vendas.gerarCotacoesAction.findDimensoesPaginated"
		rowCountService="lms.vendas.gerarCotacoesAction.getRowCountDimensoes"
		gridHeight="115" unique="true" showTotalPageCount="false">
		<adsm:gridColumn title="altura" unit="cm" property="nrAltura" width="23%" align="right"/>
		<adsm:gridColumn title="largura" unit="cm" property="nrLargura" width="23%" align="right"/>
		<adsm:gridColumn title="comprimento" unit="cm" property="nrComprimento" width="30%" align="right"/>
		<adsm:gridColumn title="quantidade" property="nrQuantidade" width="24%" align="right"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton" service="lms.vendas.gerarCotacoesAction.removeDimensoes"/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	var idCot;
	function myOnPageLoad_cb() {
		onPageLoad_cb();
		var u = new URL(parent.location.href);
   		idCot = u.parameters["idCotacao"]; 
   		if(idCot && idCot != "") {
		   	setDisabled("closeButton", false);
		   	setFocus(document.getElementById("closeButton"), false);
		} else {
		   	idCot = null;
		}
		//setDisabled(document, false);
   		populaGrid();
    }

    function initWindow(eventObj) {
		if(eventObj.name == "removeButton_grid"){
			setDisabled("removeButton",false);
		} 
    }

    // Função que limpa todos os campos da tela
    function limparCampos(){
    	resetValue("idDimensao");
    	resetValue("nrAltura");
    	resetValue("nrLargura");
    	resetValue("nrComprimento");
    	resetValue("nrQuantidade");
    	setFocusOnFirstFocusableField();
    }

    // Função que popula a grid
    function populaGrid() {
    	var param = null;
    	if(idCot)
    		param = {idCotacao:idCot};
		dimensaoGridDef.executeSearch(param, true); 
	}

	// Callback da função que armazena os dados na sessão
	function storeDimensao_cb(data, erros) {
		if (erros != undefined) {
			alert(erros);
			return false;
		}
		limparCampos();
		populaGrid();	
		setFocus(document.getElementById("closeButton"),false);
		return true;
	}

	// Método que chama o serviço da action para buscar o elemento selecionado na grid
	function populaForm(valor) {
		if(idCot) { 
			setFocus(document.getElementById("closeButton"), false);
		} else 
			onDataLoad(valor);
		return false;
	}

	function closeDimensoes() {
		window.returnValue = 1;
		self.close();
	}
</script>