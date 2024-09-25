<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerarCotacoesAction" onPageLoadCallBack="myOnPageLoad" >
	<adsm:form action="/vendas/gerarCotacoesServicosAdicionais" idProperty="idServAdicionalDocServ" 
		onDataLoadCallBack="formDataLoad" service="lms.vendas.gerarCotacoesAction.findServicoAdicionalById">
		<adsm:section caption="servicosAdicionaisTitulo" width="88"/>
		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:combobox property="servicoAdicional.idServicoAdicional" label="servicoAdicional" 
			optionLabelProperty="dsServicoAdicional" optionProperty="idServicoAdicional" required="true" 
			service="lms.vendas.gerarCotacoesAction.findServicosAdicionais" 
			onchange="return habilitarCampos(this);" 
			width="73%" labelWidth="26%">
			<adsm:propertyMapping modelProperty="dsServicoAdicional" 
				relatedProperty="servicoAdicional.dsServicoAdicional"/>
			<adsm:propertyMapping modelProperty="cdParcelaPreco" 
				relatedProperty="cdParcelaPreco" />	
		</adsm:combobox>
		<adsm:hidden property="servicoAdicional.dsServicoAdicional" />
		<adsm:hidden property="cdParcelaPreco"/>
		<adsm:label key="branco" style="border:none;" width="1%" />	
		<adsm:textbox dataType="currency" property="vlMercadoria" label="valorMercadoriaReais" minValue="0" mask="##########0.00" size="10" labelWidth="26%" width="19%"/>
		<adsm:textbox dataType="integer" property="qtCheques" label="qtdeCheques" minValue="1" maxValue="999999" size="10" labelWidth="26%" width="28%"/>
		
		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:textbox dataType="JTDate" property="dtPrimeiroCheque" label="dataPrimeiroCheque" size="10" labelWidth="26%" width="19%"/>		
		<adsm:textbox dataType="integer" property="qtDias" label="qtdeDias" minValue="1" maxValue="999999" size="10" labelWidth="26%" width="28%"/>
		
		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:textbox dataType="integer" property="nrKmRodado" label="quilometragemRodada" minValue="0" maxValue="999999" size="10" labelWidth="26%" width="19%"/>
		<adsm:textbox dataType="integer" property="qtSegurancasAdicionais" label="quantidadeSegurancasAdicionais" minValue="0" maxValue="999999" size="10" labelWidth="26%" width="28%" />
		
		<adsm:label key="branco" style="border:none;" width="1%" />
		<adsm:textbox dataType="integer" property="qtColetas" label="quantidadeColetas" minValue="1" maxValue="999999" size="10" labelWidth="26%" width="19%"/>
		<adsm:textbox dataType="integer" property="qtPaletes" label="quantidadePaletes" minValue="1" maxValue="999999" size="10" labelWidth="26%" width="28%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton service="lms.vendas.gerarCotacoesAction.storeServicoAdicional" 
				callbackProperty="storeServicoAdicional" id="salvarButton"/>
			<adsm:button caption="novo" onclick="limparTela();" id="limparButton" 
				disabled="false" buttonType="cleanButton" />
			<adsm:button caption="fechar" buttonType="closeButton" id="closeButton" disabled="false" onclick="self.close();"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idServAdicionalDocServ" rows="4" property="servAdicionalDocServ" 
		onRowClick="populaForm" gridHeight="115" unique="true" showTotalPageCount="false"
		service="lms.vendas.gerarCotacoesAction.findServicosAdicionaisPaginated"
		rowCountService="lms.vendas.gerarCotacoesAction.getRowCountServicosAdicionais">
		<adsm:gridColumn title="servicoSelecionado" property="servicoAdicional.dsServicoAdicional" width="100%"/>
		<adsm:buttonBar>
			<adsm:removeButton id="removeButton" service="lms.vendas.gerarCotacoesAction.removeServicosAdicionaisByIds" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	var idCot;
	function myOnPageLoad_cb(){
		onPageLoad_cb();
		var u = new URL(parent.location.href);
   		idCot = u.parameters["idCotacao"]; 
   		if(idCot && idCot != "") {
		   	setDisabled(document, true);
			setDisabled("closeButton", false);
		   	setFocus(document.getElementById("closeButton"), false);
		} else {
			setDisabled(document, false);
		   	idCot = null;
		}
		desabilitarCampos();
		populaGrid();
    }
    
    function formDataLoad_cb(dados, erros) {
       	limparCampos();    
       	onDataLoad_cb(dados, erros);
    	verficaCampos(document.getElementById("servicoAdicional.idServicoAdicional"));
    	if(idCot) { 
			setDisabled("salvarButton", true);
			setDisabled("limparButton", true);
			setFocus(document.getElementById("closeButton"), false);
		}
    }
    
    function initWindow(eventObj) {
		if(eventObj.name == "removeButton_grid"){
			setDisabled("removeButton",false);
		} 
    }
    
      // Função que popula a grid
    function populaGrid() {
    	var param = null;
    	if(idCot)
    		param = {idCotacao:idCot};
		servAdicionalDocServGridDef.executeSearch(param, true); 
	}
    
	function limparTela(){
		limparCampos();
		resetValue("servicoAdicional.idServicoAdicional");
		resetValue("idServAdicionalDocServ");
	}
	
	
	function limparCampos(){
		resetValue("vlMercadoria");
		document.getElementById("vlMercadoria").required = "false";
		resetValue("qtCheques");
		document.getElementById("qtCheques").required = "false";
		resetValue("dtPrimeiroCheque");
		document.getElementById("dtPrimeiroCheque").required = "false";
		resetValue("qtSegurancasAdicionais");
		document.getElementById("qtSegurancasAdicionais").required = "false";
		resetValue("qtDias");
		document.getElementById("qtDias").required = "false";
		resetValue("qtColetas");
		document.getElementById("qtColetas").required = "false";
		resetValue("nrKmRodado");
		document.getElementById("nrKmRodado").required = "false";
		resetValue("qtPaletes");
		document.getElementById("qtPaletes").required = "false";
		setFocusOnFirstFocusableField();
	}
	
	
	
	function desabilitarCampos(){
		setDisabled("vlMercadoria",true);
		setDisabled("qtCheques",true);
		setDisabled("dtPrimeiroCheque",true);
		setDisabled("qtSegurancasAdicionais",true);
		setDisabled("qtDias",true);
		setDisabled("qtColetas",true);
		setDisabled("nrKmRodado",true);
		setDisabled("qtPaletes",true);
	}
	
	
	
	function storeServicoAdicional_cb(data,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		limparTela();
		populaGrid();	
		setFocus(document.getElementById("closeButton"),false);
	}
	
	function populaForm(valor) {
		if(idCot) { 
			var sdo = createServiceDataObject("lms.vendas.gerarCotacoesAction.findServById",
			   "formDataLoad", {id:valor});
           	xmit({serviceDataObjects:[sdo]});
		} else 
			onDataLoad(valor);
		return false;
	}
	
	
    
    function habilitarCampos(valCombo){
    	limparCampos();
    	verficaCampos(valCombo);
    }
    
    function verficaCampos(valCombo) {
    	desabilitarCampos();
    	comboboxChange({e:valCombo});
    	var cdParcelaPreco = getElementValue("cdParcelaPreco");
    	if (cdParcelaPreco == "IDReembolso"){
    		setDisabled("vlMercadoria",false);
    		setDisabled("qtCheques",false);
    		setDisabled("dtPrimeiroCheque",false);
    		document.getElementById("vlMercadoria").required = "true";
    		document.getElementById("qtCheques").required = "true";
    		
    	}
    	if (cdParcelaPreco == "IDArmazenagem"){
    		setDisabled("qtPaletes",false);
    		setDisabled("qtDias",false);
    		document.getElementById("qtPaletes").required = "true";
    		document.getElementById("qtDias").required = "true";
    	}
    	if (cdParcelaPreco == "IDEscolta"){
    		setDisabled("nrKmRodado",false);
    		setDisabled("qtSegurancasAdicionais",false);
    		document.getElementById("nrKmRodado").required = "true";
    		document.getElementById("qtSegurancasAdicionais").required = "true";
    	}
    	if (cdParcelaPreco == "IDAgendamentoColeta"){
    		setDisabled("qtColetas",false);
    		document.getElementById("qtColetas").required = "true";
    	}
    	if (cdParcelaPreco == "IDEstadiaVeiculo"){
    		setDisabled("qtDias",false);
    		document.getElementById("qtDias").required = "true";
    	}
    }
    
</script>