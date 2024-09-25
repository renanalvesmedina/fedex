<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.rnc.abrirRNCAction">
	<adsm:form action="/rnc/abrirRNC" idProperty="idOcorrenciaNaoConformidade">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-12024"/>
			<adsm:include key="LMS-04400"/>
		</adsm:i18nLabels>
	
		<adsm:textbox dataType="text" property="naoConformidade.filial.sgFilial" size="3" maxLength="3" label="naoConformidade" labelWidth="18%" width="25%" disabled="true" >
			<adsm:textbox dataType="integer" property="naoConformidade.nrNaoConformidade" size="9" maxLength="8" mask="00000000" disabled="true" />
			
			<adsm:combobox property="motivoAberturaNc.idMotivoAberturaNc" optionProperty="idMotivoAberturaNc" optionLabelProperty="dsMotivoAbertura" 
				service="lms.rnc.abrirRNCAction.findMotivoAberturaNc" label="motivoNaoConformidade" labelWidth="21%" width="31%" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="idFilial" size="3" maxLength="3" label="controleCargas" labelWidth="18%" width="25%" disabled="true" >
			<adsm:textbox dataType="integer" property="controleCarga" size="9" maxLength="8" mask="00000000" disabled="true" />
		</adsm:textbox>
				
		<adsm:section caption="branco" />
		
		<adsm:textbox property="pessoa.remetente.cpfCnpj" label="remetente" dataType="text" labelWidth="18%" width="77%" disabled="true" >
			<adsm:textbox dataType="text" property="pessoa.remetente.nmPessoa"	size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:listbox label="chaveNfe" property="nrChaveNfe" optionProperty="idNotaFiscalColeta" optionLabelProperty="nrChave" size="9" boxWidth="280" 
			labelWidth="18%" width="50%" onContentChange="contentChange" labelStyle="vertical-align:top">
			<adsm:textbox property="nrChave" dataType="integer"	maxLength="44" serializable="false"	size="50" onchange="return findChaveNfe();"/>
		</adsm:listbox>
		
	</adsm:form>
	
	<adsm:grid property="itensNFe"  idProperty="id" autoSearch="false" selectionMode="none" 
		showRowIndex="false" autoAddNew="false" gridHeight="120" showGotoBox="false" scrollBars="vertical"
	 	showPagging="false"	showTotalPageCount="false" rows="4" onRowClick="returnFalse();" > 
	 	
		<adsm:gridColumn property="itensNFe.notaFiscal" title="notaFiscal" dataType="text" />
		<adsm:gridColumn property="itensNFe.item" title="item" dataType="integer" />
		<adsm:gridColumn property="itensNFe.descricao" title="descricao" dataType="text" />
		<adsm:gridColumn property="itensNFe.valor" title="valor" dataType="decimal" mask="#,###,###,###,###,##0.00" />
		<adsm:gridColumn property="itensNFe.qtdeAnterior" title="qtdeAnterior" dataType="integer" />
		<adsm:editColumn property="itensNFe.qtdInformada" title="qtdInformada" dataType="integer" field="textbox" />

		<adsm:buttonBar >
			<adsm:button id="botaoLimpar"  caption="limpar"  onclick="limpar_OnClick();"/>
		</adsm:buttonBar>
					
	</adsm:grid>
	
</adsm:window>

<script>

// Sobrescrevendo funcao do insert do listBox para não chamar nenhuma funcao
document.getElementById('nrChaveNfe_Update_a').onclick = function () {};

	/**
	 * Inicializacao
	 */
	function initWindow(eventObj) {	
		setDisabled("botaoLimpar", false);
		
		if(eventObj.name == "tab_click") {
			var tabDet = getTabGroup(this.document).getTab("cad");
			var idMotivoAbertura = tabDet.getElementById("motivoAberturaNc.idMotivoAberturaNc").value;
			
			setElementValue("naoConformidade.nrNaoConformidade", tabDet.getFormProperty("naoConformidade.nrNaoConformidade"));
			setElementValue("naoConformidade.filial.sgFilial", tabDet.getFormProperty("naoConformidade.filial.sgFilial"));
			
			if(idMotivoAbertura != undefined && idMotivoAbertura != ""){
				setElementValue("motivoAberturaNc.idMotivoAberturaNc", idMotivoAbertura);
			
			}
		
			//findNrChave();
			validaGrid();
		}
	}

	/**
	 * Busca a chave nfe verificando sua autenticidade e buscando seu remetente e seus itens correspondentes
	 */
	function findChaveNfe(){
		
		document.getElementById("nrChaveNfe").selectedIndex = -1;
		var chaveNfe = getElementValue("nrChaveNfe_nrChave");
		
		if(validateChaveNfe(chaveNfe)){
			// Extrai o cnpj da chave para envio da busca do cliente
			var cpfCnpjCliente = chaveNfe.substring(6, 20);
			
			var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findChaveNfe", "findChaveNfe", {nrChaveNfe:chaveNfe, cpfCnpjCliente:cpfCnpjCliente});
		   	xmit({serviceDataObjects:[sdo]});
		}
	}
	
	/**
	 * Callback de retorno da busca dos itens da NFe
	 */
	 var dados = new Array();
	function findChaveNfe_cb(data, error, errorMsg, eventObj){
		dados = data;
		if (error != undefined) {
			alert(error);
			setFocus(document.getElementById("nrChaveNfe_nrChave"), true);
			setElementValue("nrChaveNfe_nrChave","");
		}
		else{
			setDisabled("nrChaveNfe", false);	
			setDisabled("nrChaveNfe_nrChave", false);
			if(data != undefined && data.cliente != undefined && data.cliente.pessoa != undefined && data.cliente != null 
					&& data.cliente.pessoa.nrIdentificacaoFormatado != undefined) {	
				if(document.getElementById("pessoa.remetente.cpfCnpj").value != ""){
					var cpfCnpj = document.getElementById("pessoa.remetente.cpfCnpj").value;
					
					// Retira os caracteres invalidos
					cpfCnpj = cpfCnpj.replace(/[\./-]/g, "");
					
					// Compara com o remetente ja existente, sendo diferente emite uma msg de erro
					if(cpfCnpj != data.cliente.pessoa.nrIdentificacao){
						alert("LMS-12024 - " + i18NLabel.getLabel("LMS-12024")); 
						
						setElementValue("nrChaveNfe_nrChave","");
						setFocus(document.getElementById("nrChaveNfe_nrChave"), true);
						
						return;
					}
				}
				
				document.getElementById("pessoa.remetente.cpfCnpj").value = data.cliente.pessoa.nrIdentificacaoFormatado;
				document.getElementById("pessoa.remetente.nmPessoa").value = data.cliente.pessoa.nmPessoa;
			}
			
			var sizeNFe = nrChaveNfeListboxDef.getData().length;
 			var nrChaveNfeElem = document.getElementById("nrChaveNfe_idNotaFiscalColeta");
			
 			// Codigo para que idependente do item selecionado no listBox, este codigo forca o insert sem update
 			if (nrChaveNfeElem.selectedIndex != -1) {
				nrChaveNfeElem.value = "";
			} 
			
 			setElementValue("nrChaveNfe_nrChave", data.nrChaveNfe);
 			nrChaveNfeListboxDef.insertOrUpdateOption();
			
			if(nrChaveNfeListboxDef.getData().length >= sizeNFe ){
				populaGridItensNFe(data);
			}
		}
		setDisabled("nrChaveNfe", true);	
		setDisabled("nrChaveNfe_nrChave", true);
	} 
	
	/**
	 * Popula os a grid com os itens da NFe
	 */
	function populaGridItensNFe(data){
		if(itensNFeGridDef.getRowCount() == 0){
			//Starta algumas das propriedades necessarias para a grid
	 		itensNFeGridDef.gridState.stats = new Object();
			itensNFeGridDef.gridState.stats.elapsedRenderTime = 0;
			itensNFeGridDef.gridState.data = new Array();
		}
		
		// Acumula os itens na grid
	    for (var a = 0;  a < data.nfe.length;  a++) {
	        itensNFeGridDef.gridState["data"].push(data.nfe[a]);
	    }
		
		var resultSetPage = new Object();
		resultSetPage.list = itensNFeGridDef.gridState["data"];
		resultSetPage.hasNextPage = "true";
		resultSetPage.hasPriorPage = "false";
		resultSetPage.currentPage = "1";

		itensNFeGridDef.populateGrid(resultSetPage);
	}
	
	function validateChaveNfe(chaveNfe) {
		if(chaveNfe.length >= 44){
			if(!validateDigitoVerificadorNfe(chaveNfe)){
				return false;
			}
		}else{
			alert("LMS-04400 - " + i18NLabel.getLabel("LMS-04400"));
			setElementValue("nrChaveNfe_nrChave","");
			return false;
		}
		
		return true;
	}

  	
	/**
	 * Valida o digito verificador da Chave Nfe
	 */
	function validateDigitoVerificadorNfe(chaveNfe){
		var dvChaveNfe = chaveNfe.substring(chaveNfe.length - 1, chaveNfe.length);
		var chave = chaveNfe.substring(0, chaveNfe.length - 1);	
		var calculoChaveNfe = modulo11(chave);
		
		if(dvChaveNfe != (calculoChaveNfe)){
			alert("LMS-04400 - " + i18NLabel.getLabel("LMS-04400"));
			return false;
		}
		
		return true;
	}
	
	function modulo11(chave){
		var n = new Array();
		var peso = 2;
		var soma = 0;

		n = chave.split('');
		
		for (var i = n.length-1; i >= 0; i--) {
			var value = n[i];
			soma = soma + value * peso++;
			if(peso == 10){
				peso = 2;
			}
		}
		
		var mod = soma % 11;
		var dv;
		
		if(mod == 0 || mod == 1){
			dv = 0;
		} else {
			dv = 11 - mod;
		}
		
		return dv
	}
	
	function returnFalse(){
		return false;
	}
	
 	function contentChange(event){
		if(event.name == "deleteButton_click"){
			var nrChaveNfeElem = document.getElementById("nrChaveNfe");
			
			if (nrChaveNfeElem.selectedIndex != -1) {
				removeItensNFeGrid(nrChaveNfeElem.options[nrChaveNfeElem.selectedIndex].text);
			}
		}
		else if(event.name == "cleanButton_click"){
			document.getElementById("nrChaveNfe").selectedIndex = -1;
		}
	}
 	
	/**
	 * De acordo com o que foi selecionado na listbox pega e remove da grid os itens relacionados com aquela chave
	 */
	function removeItensNFeGrid(){
		if(nrChaveNFe != undefined && nrChaveNFe != ""){
			var notaFiscal = nrChaveNFe.substring(25, 34);
			var itensNFeGrid = itensNFeGridDef.gridState["data"];
			var newItensNFeForGrid = new Array();			
			
			for (var i = 0;  i < itensNFeGrid.length;  i++) {
				if(itensNFeGrid[i].itensNFe.notaFiscal != notaFiscal){
					newItensNFeForGrid.push(itensNFeGrid[i]);
				}		
			}
			
			if(newItensNFeForGrid.length != 0){
				//Starta algumas das propriedades necessarias para a grid
		 		itensNFeGridDef.gridState.stats = new Object();
				itensNFeGridDef.gridState.stats.elapsedRenderTime = 0;
				itensNFeGridDef.gridState.data = new Array();
				
				var resultSetPage = new Object();
				resultSetPage.list = newItensNFeForGrid;
				resultSetPage.hasNextPage = "true";
				resultSetPage.hasPriorPage = "false";
				resultSetPage.currentPage = "1";
	
				itensNFeGridDef.populateGrid(resultSetPage);
			}
			else{
				itensNFeGridDef.resetGrid();
				document.getElementById("pessoa.remetente.cpfCnpj").value = "";
				document.getElementById("pessoa.remetente.nmPessoa").value = "";
			}
		}
	} 

	function limpar_OnClick(){
		populaGridItensNFe(dados);
	}
	
	
 	
	
	function findNrChave(){
		var tabDet = getTabGroup(this.document).getTab("cad");
		var sdo = createServiceDataObject("lms.rnc.abrirRNCAction.findNrChave", "findNrChave", {idDoctoServico:tabDet.getFormProperty("naoConformidade.doctoServico.idDoctoServico")});
	  	xmit({serviceDataObjects:[sdo]});
	}
	
	function findNrChave_cb(data, error){
			
		if(data != undefined && data.length >= 0){

			for(var i=1; i <= data.nrChave.length ; i++ ){
				setElementValue("nrChaveNfe_nrChave", data.nrChave[i-1]);
				findChaveNfe();
			}
		}else{
			var tabDet = getTabGroup(this.document).getTab("item");
			setDisabled(tabDet, true)
		}
	
		setDisabled("nrChaveNfe", true);	
		setDisabled("nrChaveNfe_nrChave", true);
			
	}
	
	function validaGrid(){
		if(document.getElementById("pessoa.remetente.cpfCnpj").value == ""){
			findNrChave();
		}
	}
	
	
		
	
	 
</script>