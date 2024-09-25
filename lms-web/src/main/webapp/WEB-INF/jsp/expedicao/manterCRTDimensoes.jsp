<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.expedicao.manterCRTDimensoesAction" 
	onPageLoadCallBack="myOnPageLoad" >
	
	<adsm:form 
		action="/expedicao/manterCRTDimensoes" 
		idProperty="idDimensao">
		
		<adsm:section
			caption="dimensoes"
			width="56"/>
		
		<adsm:label
			key="branco"
			style="border:none;"
			width="1%" />
		
		<adsm:textbox 
			dataType="integer" 
			size="8" 
			property="nrAltura" 
			required="true" 
			label="altura" 
			width="17%" 
			labelWidth="13%" 
			unit="cm" 
			maxLength="8" 
			minValue="1"/>
			
		<adsm:textbox 
			dataType="integer" 
			size="8" 
			property="nrLargura" 
			required="true" 
			label="largura" 
			width="56%" 
			labelWidth="13%" 
			unit="cm" 
			maxLength="8" 
			minValue="1"/>
			
		<adsm:label
			key="branco"
			style="border:none;"
			width="1%"/>
		
		<adsm:textbox 
			dataType="integer" 
			size="8" 
			property="nrComprimento" 
			required="true" 
			label="comprimento" 
			width="17%" 
			labelWidth="13%"
			unit="cm" 
			maxLength="8" 
			minValue="1"/>
			
		<adsm:textbox 
			dataType="integer" 
			size="8" 
			property="nrQuantidade" 
			required="true" 
			label="quantidade" 
			width="56%" 
			labelWidth="13%" 
			maxLength="5" 
			minValue="1"/>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton 
				id="storeButton" 
				service="lms.expedicao.manterCRTDimensoesAction.storeInSession" 
				callbackProperty="storeSession" />
				
			<adsm:button 
				caption="novo" 
				disabled="false" 
				buttonType="cleanButton" 
				id="cleanButton" 
				onclick="limparCampos();" />
				
			<adsm:button 
				caption="fechar" 
				buttonType="closeButton" 
				id="closeButton" 
				disabled="false" 
				onclick="self.close();"/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid 
		idProperty="idDimensao"
		showGotoBox="false"
		property="dimensao"
		onRowClick="populaForm"
		onDataLoadCallBack="dimensaoGridDataLoad"
		rows="5"
		gridHeight="115"
		unique="true"
		showTotalPageCount="false">
		
		<adsm:gridColumn 
			title="altura" 
			unit="cm" 
			property="nrAltura" 
			width="23%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="largura" 
			unit="cm" 
			property="nrLargura" 
			width="23%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="comprimento" 
			unit="cm" 
			property="nrComprimento" 
			width="30%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="quantidade" 
			property="nrQuantidade" 
			width="24%" 
			align="right"/>
			
		
		<adsm:buttonBar>
			<adsm:removeButton
				id="removeButton" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function myOnPageLoad_cb(){
		onPageLoad_cb();
		populaGrid();
    }
    /************************************************************\
	*
	\************************************************************/
    function initWindow(eventObj) {
		if(eventObj.name == "removeButton_grid"){
			setDisabled("removeButton",false);
		} 
    }
    /************************************************************\
	*
	\************************************************************/
    function limparCampos(){
    	resetValue("idDimensao");
    	resetValue("nrAltura");
    	resetValue("nrLargura");
    	resetValue("nrComprimento");
    	resetValue("nrQuantidade");
    	setFocusOnFirstFocusableField();
    }
    /************************************************************\
	*
	\************************************************************/
    function populaGrid() {
		dimensaoGridDef.executeSearch({},true);
	}
	/************************************************************\
	*
	\************************************************************/
	function storeSession_cb(data,erros){
		if (erros != undefined){
			alert(erros);
			return false;
		}
		limparCampos();
		populaGrid();	
		setFocus(document.getElementById("closeButton"), false);
		return true;
	}
	/************************************************************\
	*
	\************************************************************/
	function populaForm(valor) {
		var sdo = createServiceDataObject("lms.expedicao.manterCRTDimensoesAction.findById", "findId", {idDimensao:valor} );
        xmit({serviceDataObjects:[sdo]});
		return false;
	}
	/************************************************************\
	*
	\************************************************************/
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
	/************************************************************\
	*
	\************************************************************/
	function dimensaoGridDataLoad_cb(data){
		findIdCtoInternacional();
	}
	/************************************************************\
	*
	\************************************************************/
	function findIdCtoInternacional(){
		var service = "lms.expedicao.manterCRTDimensoesAction.getIdCtoInternacionalInSession";
		var sdo = createServiceDataObject(service, "findIdCtoInternacional", {});
        xmit({serviceDataObjects:[sdo]});
	}
	/************************************************************\
	*
	\************************************************************/
	function findIdCtoInternacional_cb(data){
		var isDisable = (data && data.idCtoInternacional);
		setDisabled(document, isDisable);
		setDisabled('storeButton', isDisable);
		setDisabled('cleanButton', isDisable);
		setDisabled('closeButton', false);
		setFocus('closeButton', false);
	}
</script>