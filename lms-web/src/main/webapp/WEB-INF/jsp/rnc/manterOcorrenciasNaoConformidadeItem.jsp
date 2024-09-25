<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.rnc.manterOcorrenciasNaoConformidadeAction">
	<adsm:form action="/rnc/manterOcorrenciasNaoConformidade" idProperty="idNaoConformidade">
	
		<adsm:textbox dataType="text" property="naoConformidade.filial.sgFilial" size="3" maxLength="3" label="naoConformidade" labelWidth="18%" width="25%" disabled="true" >
			<adsm:textbox dataType="integer" property="naoConformidade.nrNaoConformidade" size="9" maxLength="8" mask="00000000" disabled="true" />
			
			<adsm:combobox property="motivoAberturaNc.idMotivoAberturaNc" optionProperty="idMotivoAberturaNc" optionLabelProperty="dsMotivoAbertura" 
				service="lms.rnc.manterOcorrenciasNaoConformidadeAction.findMotivoAberturaNc" label="motivoNaoConformidade" labelWidth="21%" width="31%" disabled="true" />
		</adsm:textbox>
		
		<adsm:textbox dataType="text" property="controleCarga.sgFilial" size="3" maxLength="3" label="controleCargas" labelWidth="18%" width="25%" disabled="true" >
			<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="9" maxLength="8" mask="00000000" disabled="true" />
		</adsm:textbox>
				
		<adsm:section caption="branco" />
		
		<adsm:textbox property="pessoa.remetente.cpfCnpj" label="remetente" dataType="text" labelWidth="18%" width="77%" disabled="true" >
			<adsm:textbox dataType="text" property="pessoa.remetente.nmPessoa"	size="50" maxLength="50" disabled="true" />
		</adsm:textbox>
		
		<adsm:listbox label="chaveNfe" property="nrChaveNfe" optionProperty="idNotaFiscalColeta" optionLabelProperty="nrChave" size="9" boxWidth="280" 
			labelWidth="18%" width="50%" >
			<adsm:textbox property="nrChave" dataType="integer"	maxLength="44" serializable="false"	size="50" onchange="return findChaveNfe()" />
		</adsm:listbox>
		
	</adsm:form>
	
	<adsm:grid property="itensNFe"  idProperty="item" autoSearch="false" selectionMode="none" 
		showRowIndex="false" autoAddNew="false" gridHeight="120" showGotoBox="false" scrollBars="vertical"
	 	showPagging="false"	showTotalPageCount="false" rows="4" onRowClick="returnFalse();" > 
	 	
		<adsm:gridColumn property="itensNFe.notaFiscal" title="notaFiscal" dataType="text" />
		<adsm:gridColumn property="itensNFe.item" title="item" dataType="integer" />
		<adsm:gridColumn property="itensNFe.descricao" title="descricao" dataType="text" />
		<adsm:gridColumn property="itensNFe.valor" title="valor" dataType="decimal" mask="#,###,###,###,###,##0.00" />
		<adsm:gridColumn property="itensNFe.qtdeAnterior" title="qtdeAnterior" dataType="integer" />
		<adsm:gridColumn property="itensNFe.qtdNaoConformidade" title="qtdNaoConformidade" dataType="integer" />
		<adsm:gridColumn property="itensNFe.vlNaoConformidade" title="vlNaoConformidade" dataType="decimal" mask="#,###,###,###,###,##0.00" />

		<adsm:buttonBar >
			<adsm:button id="botaoLimpar"  caption="limpar"  onclick="limpar_OnClick();"  disabled="false"/>
		</adsm:buttonBar>
					
	</adsm:grid>
	
</adsm:window>

<script>

	function initWindow(eventObj) {	
		setDisabled("nrChaveNfe", true);	
		setDisabled("nrChaveNfe_nrChave", true);
		
		if(eventObj.name == "tab_click") {
			limpar();
			loadData();
		}
	}
	
	function loadData() {
		var tabGroup = getTabGroup(this.document);
		var tabDet = tabGroup.getTab("cad");
		
		var idOcorrenciaNaoConformidade = tabDet.getFormProperty("idOcorrenciaNaoConformidade");
		var idMotivoAbertura = tabDet.getElementById("motivoAberturaNc.idMotivoAberturaNc").value;
		
		if (idOcorrenciaNaoConformidade != undefined && idOcorrenciaNaoConformidade != '') {
			var nrNaoConformidade = tabDet.getFormProperty("naoConformidade.nrNaoConformidade");
			var sgFilial = tabDet.getFormProperty("naoConformidade.filial.sgFilial");
			var nrControleCarga = tabDet.getFormProperty("controleCarga.nrControleCarga");
			var sgFilialControleCarga = tabDet.getFormProperty("controleCarga.filialByIdFilialOrigem.sgFilial");
			var idONC = tabDet.getFormProperty("idOcorrenciaNaoConformidade");
			
			setElementValue("motivoAberturaNc.idMotivoAberturaNc", idMotivoAbertura);
			setElementValue("naoConformidade.nrNaoConformidade", nrNaoConformidade);
			setElementValue("naoConformidade.filial.sgFilial", sgFilial);
			setElementValue("controleCarga.sgFilial", sgFilialControleCarga);
			setElementValue("controleCarga.nrControleCarga", nrControleCarga);
		}
		
		var sdo = createServiceDataObject("lms.rnc.manterOcorrenciasNaoConformidadeAction.loadItensNFe", "loadItensNFe", { idOcorrenciaNaoConformidade:idONC });
	   	xmit({serviceDataObjects:[sdo]});
	}
	
	function loadItensNFe_cb(data, error){
		if(error != undefined){
			alert(error);
			return;
		}	
		else{
			if(data.chavesNFe != undefined && data.chavesNFe.length != 0){
				setDisabled("nrChaveNfe", false);	
				setDisabled("nrChaveNfe_nrChave", false);
				
				for (var i = 0;  i < data.chavesNFe.length;  i++) {
					setElementValue("nrChaveNfe_nrChave", data.chavesNFe[i]);
					nrChaveNfeListboxDef.insertOrUpdateOption();
				}
				
				setDisabled("nrChaveNfe", true);	
				setDisabled("nrChaveNfe_nrChave", true);
			}
			
			if(data.cliente != ""){
				document.getElementById("pessoa.remetente.cpfCnpj").value = data.cliente.pessoa.nrIdentificacaoFormatado;
				document.getElementById("pessoa.remetente.nmPessoa").value = data.cliente.pessoa.nmPessoa;
			}
			
			//Starta algumas das propriedades necessarias para a grid
	 		itensNFeGridDef.gridState.stats = new Object();
			itensNFeGridDef.gridState.stats.elapsedRenderTime = 0;
			itensNFeGridDef.gridState.data = new Array();
			
			var resultSetPage = new Object();
			resultSetPage.list = data.nfe;
			resultSetPage.hasNextPage = "true";
			resultSetPage.hasPriorPage = "false";
			resultSetPage.currentPage = "1";

			itensNFeGridDef.populateGrid(resultSetPage);
		}
	}
	
	function returnFalse(){
		return false;
	}
	
	function limpar(){
		resetValue("pessoa.remetente.cpfCnpj");
		resetValue("pessoa.remetente.nmPessoa");
		resetValue("nrChaveNfe_nrChave");
		resetValue("nrChaveNfe");
		resetValue("naoConformidade.filial.sgFilial");
		resetValue("naoConformidade.nrNaoConformidade");
		resetValue("controleCarga.sgFilial");
		resetValue("controleCarga.nrControleCarga");
		itensNFeGridDef.resetGrid();
	}

</script>