<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">

	/**
	 * Inicio procedimento de carregar a tela
	 */
	function loadDataObjects() {
		onPageLoad();		
		loadDataFromParent();
		disableButtons(false);
		
    	var data = new Object();
    	data.idCarregamentoDescarga = getElementValue("idCarregamentoDescarga");
		var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoControleCargasAction.findDataFinalCarregamento", "loadDataObjects", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * Carrega os dados da tela do pai.
	 */
	function loadDataFromParent() {
		var parentWindow = dialogArguments.window.document;
		//carrega os objetos
		setElementValue("idCarregamentoDescarga", parentWindow.getElementById("idCarregamentoDescarga").value);
		setElementValue("controleCarga.idControleCarga", parentWindow.getElementById("controleCarga.idControleCarga").value);
		setElementValue("controleCarga.nrControleCarga", parentWindow.getElementById("controleCarga.nrControleCarga").value);
		setElementValue("controleCarga.filialByIdFilialOrigem.idFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.idFilial").value);
		setElementValue("controleCarga.filialByIdFilialOrigem.sgFilial", parentWindow.getElementById("controleCarga.filialByIdFilialOrigem.sgFilial").value);
		
		setElementValue("idFilial", parentWindow.getElementById("idFilial").value);
		setElementValue("sgFilial", parentWindow.getElementById("sgFilial").value);
		setElementValue("nmPessoa", parentWindow.getElementById("nmPessoa").value);
		
		setElementValue("sgFilialPostoAvancado", parentWindow.getElementById("sgFilialPostoAvancado").value);
		setElementValue("nmPessoaPostoAvancado", parentWindow.getElementById("nmPessoaPostoAvancado").value);
	}

	/**
	 * Carrega a tela com a data do usuario e da precedncia com seu carregamento
	 */
	function loadDataObjects_cb(data, error) {
		if (error==undefined) {
			disableButtons(false);
			setElementValue("dhFimOperacao", setFormat(document.getElementById("dhFimOperacao"), data.dhFimOperacao));		
		} else {
			alert(error);
		}
	}

	/**
	 * Desabilita ou abilita os botoes desta aba
	 *
	 * @param disable 
	 */
	function disableButtons(disable) {
		document.getElementById("fechar").disabled=disable; 
		document.getElementById("finalizar").disabled=disable; 
	}
</script>

<adsm:window service="lms.carregamento.finalizarCarregamentoControleCargasAction" onPageLoad="loadDataObjects">
	<adsm:form action="/carregamento/finalizarCarregamentoControleCargas" idProperty="idCarregamentoDescarga" id="formData">
		<adsm:section caption="finalizarCarregamento"/>

		<adsm:hidden property="controleCarga.filialByIdFilialOrigem.idFilial"/>
		<adsm:hidden property="controleCarga.idControleCarga"/>
		
		<adsm:textbox dataType="text" property="controleCarga.filialByIdFilialOrigem.sgFilial" 
					 label="controleCargas" width="85%" size="3" maxLength="3" disabled="true">
			<adsm:textbox dataType="text" property="controleCarga.nrControleCarga" maxLength="8" size="8" disabled="true"/>
		</adsm:textbox>
		
		<adsm:hidden property="idFilial"/>
		<adsm:textbox dataType="text" property="sgFilial" label="filial" size="3" maxLength="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoa" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox dataType="text" property="sgFilialPostoAvancado" 
					  label="postoAvancado" maxLength="3" size="3" width="85%" disabled="true">
			<adsm:textbox dataType="text" property="nmPessoaPostoAvancado" size="50" maxLength="50" disabled="true"/>
		</adsm:textbox>

		<adsm:textbox property="dhFimOperacao" dataType="JTDateTimeZone"
					  label="fimCarregamento" disabled="true" width="85%"/>
		
		<adsm:textbox dataType="decimal" property="controleCarga.pcOcupacaoInformado" label="ociosidadeVisual" 
					  smallerThan="101" required="true" width="85%" mask="##0.00" maxLength="6" size="6" maxValue="100"/>
		
		<adsm:listbox property="lacreControleCarga.idsLacreControleCarga" optionProperty="idLacreCC" optionLabelProperty="nrLacre"
	    			  label="lacres" width="75%" size="5" boxWidth="170">
	    	<adsm:textbox dataType="integer" property="nrLacre" size="6" maxLength="6"/>
	    </adsm:listbox>

		<adsm:textarea property="carregamentoDescarga.obOperacao" 
					   label="observacao" maxLength="200" columns="80" width="85%" rows="3"/>

		<adsm:buttonBar>
			<adsm:button id="finalizar" caption="finalizar" onclick="checkDocumentoServico();"/>
			<adsm:button id="fechar" caption="fechar" onclick="returnToParent(false)"/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script javascript>
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_load") {
			//Busca a tela pai
			
			
		} else if(eventObj.name == "tab_click") {
			disableButtons(false);
		} else if(eventObj.name == "storeButton") {
			returnToParent();
		}
	}
	
	function checkLacresControleCarga() {
	
	}
	
	function checkLacresControleCarga_cb(data, error) {
	
	}
	
	/**
	 * Verifica pelos Documentos Servicos que devem ser carregados.
	 */
	function checkDocumentoServico() {
	
		var data = new Object();
		data.idControleCarga = getElementValue("controleCarga.idControleCarga");
		
		var sdo = createServiceDataObject("lms.carregamento.finalizarCarregamentoControleCargasAction.validateControleCargaDoctoServico", "checkDocumentoServico", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	/**
	 * CallBack da verificacao. Caso existam 'Documentos Servicos' a serem carregados'
	 * questiona o usuario.
	 *
	 * @param data
	 * @param error
	 */
	function checkDocumentoServico_cb(data, error) {
	
		if (data._exception!=undefined) {
			if(confirm(data._exception._message)==false) return false;
		} 
		
		finalizarCarregamento();
	}
	
	/**
	 * Gera a finalizacao do carregamento.
	 */
	function finalizarCarregamento() {
	
		var result = storeButtonScript('lms.carregamento.finalizarCarregamentoControleCargasAction.storeFinalizarCarregamento', 'finalizarCarregamento', document.getElementById("formData"));	
	
		if (!result) {
			if (getElementValue("controleCarga.pcOcupacaoInformado")=="") {
				setFocus(document.getElementById("controleCarga.pcOcupacaoInformado"));
			} else {
				setFocus(document.getElementById("lacreControleCarga.idsLacreControleCarga_nrLacre"));
			}
		}
		
		return result;
	}
	
	function finalizarCarregamento_cb(data, error) {
		if (data._exception!=undefined) {
			alert(data._exception._message);
		} else {
			returnToParent(true);
		}
	}
	
	//##################################
    // Funcoes basicas da tela
	//##################################
	
	/**
	 * fecha a atual janela
	 */
	function returnToParent(success){
		window.returnValue = success;
		window.close();
	}
	
</script>
