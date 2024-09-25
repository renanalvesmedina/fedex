<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.indenizacoes.manterReciboIndenizacaoAction" onPageLoadCallBack="pageLoad">

	<adsm:form action="/indenizacoes/manterReciboIndenizacao" idProperty="idParcelaReciboIndenizacao" service="lms.indenizacoes.manterReciboIndenizacaoAction.findByIdParcela">

	    <adsm:masterLink idProperty="idReciboIndenizacao" showSaveAll="true">
		    <adsm:masterLinkItem property="nrReciboComposto"   label="numeroRIM" itemWidth="50" />
	    </adsm:masterLink>

		<adsm:textbox label="numeroBoleto" property="nrBoleto" dataType="text" size="13" maxLength="13" width="80%" labelWidth="20%" required="true"/>
		
		<adsm:textbox label="dataVencimento" property="dtVencimento" dataType="JTDate" 
					  onchange="return validaDtVencimento()"
					  width="80%" labelWidth="20%" 
					  disabled="false" required="true" />

		<adsm:complement label="valor" width="80%" labelWidth="20%" separator="branco" required="true">
			<adsm:combobox 
						   property="moeda.idMoeda" 
						   boxWidth="85"
						   required="true"
						   service="lms.indenizacoes.manterReciboIndenizacaoAction.findComboMoeda" 
						   optionProperty="idMoeda" 
						   optionLabelProperty="siglaSimbolo" 
						   onlyActiveValues="true" disabled="true" defaultValue="1"
						   >
				<adsm:textbox property="vlPagamento" dataType="currency" disabled="false" required="true"/>
			</adsm:combobox>
		</adsm:complement>

		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarParcela" service="lms.indenizacoes.manterReciboIndenizacaoAction.saveParcela" callbackProperty="storeCallback"/>
			<adsm:newButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="parcelaReciboIndenizacao" 
				idProperty="idParcelaReciboIndenizacao" 
				service="lms.indenizacoes.manterReciboIndenizacaoAction.findPaginatedParcela"
				rowCountService="lms.indenizacoes.manterReciboIndenizacaoAction.getRowCountParcela"
				detailFrameName="parcelas"				
				selectionMode="check" 
				unique="true" 
				rows="9"
				defaultOrder="nrBoleto"
				>
		<adsm:gridColumn property="nrBoleto"              title="numeroBoleto" width="300" align="left" />
		<adsm:gridColumn property="dtVencimento"          title="vencimento"   width="100" dataType="JTDate"/>
		<adsm:gridColumn property="sgSimboloParcela"      title="valor" width="50"  align="left"                     />
		<adsm:gridColumn property="vlPagamento"           title=""      width="250" align="right" dataType="currency"/>
		
		<adsm:buttonBar>
			<adsm:button caption="excluirParcela" buttonType="removeButton" onclick="onRemoveButtonClick();"/>
		</adsm:buttonBar>
	</adsm:grid>


</adsm:window>
<script>

   	var tabGroup = getTabGroup(this.document);
	var abaDetalhamento = tabGroup.getTab("cad");

	function getMoedaSessao() {
		setElementValue('moeda.idMoeda', abaDetalhamento.getFormProperty("idMoedaHidden"));		
	}	
	
	function pageLoad_cb() {
		onPageLoad_cb();
	}
		
	function initWindow(e) {
		if (e.name=='newItemButton_click') {
			getMoedaSessao();
		}
	}
		
	function onRemoveButtonClick() {
		removeByIdsCustom('lms.indenizacoes.manterReciboIndenizacaoAction.removeParcelasByIds', 'remove');
	}	
	
	function removeByIdsCustom(remService, removeCallback) {

		var idsMap = parcelaReciboIndenizacaoGridDef.getSelectedIds(); 
		idsMap.masterId=getElementValue('masterId');
		
		if (idsMap.ids.length>0) { 
			if (window.confirm(erExcluir)) {
			  var remoteCall = {serviceDataObjects:new Array()}; 
			  remoteCall.serviceDataObjects.push(createServiceDataObject(remService, removeCallback, idsMap)); 
			  xmit(remoteCall); 		
			}
		} else {
			alert(erSemRegistro);
		}
	}
	
	function remove_cb(data, error) {
		if (error==undefined && data!=undefined && data.rowCount!=undefined) {
			alteraQtParcelasAbaDetalhamento(data.rowCount);
		}
		parcelaReciboIndenizacaoGridDef.removeByIds_cb(data, error);
	}
	
	
	// executa a consulta da grid
	function executeSearch() {
		var data = new Array();
		setNestedBeanPropertyValue(data, 'masterId', getElementValue('masterId'));
		parcelaReciboIndenizacaoGridDef.executeSearch(data);
	}	
	
	function storeCallback_cb(data, error) {
		store_cb(data, error);
		if (error==undefined) {
			resetValue(document);
			getMoedaSessao();
			alteraQtParcelasAbaDetalhamento(data.qtParcelasBoletoBancario);
			executeSearch();
		}
	}
	
	// altera o valor da quantidade de parcelas na aba rim
	function alteraQtParcelasAbaDetalhamento(qtParcelas) {	
		abaDetalhamento.getDocument().getElementById('qtParcelasBoletoBancario').value = qtParcelas;
	}

	// tab show
	function onTabShow(fromTab) {
		getMoedaSessao();
		executeSearch();	
	}
	
	
	function validaDtVencimento() {
		if (getElementValue("dtVencimento") != "") {
			var sdo = createServiceDataObject("lms.indenizacoes.manterReciboIndenizacaoAction.validateDtVencimento", 
				"retornoValidaDtVencimento", {dtVencimento:getElementValue("dtVencimento")});
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function retornoValidaDtVencimento_cb(data, error) {
		if (error != undefined) {
			alert(error);
			resetValue("dtVencimento");
			setFocus(document.getElementById("dtVencimento"));
		}
	}
</script>