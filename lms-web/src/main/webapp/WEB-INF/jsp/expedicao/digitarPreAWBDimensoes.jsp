<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.expedicao.digitarPreAWBDimensoesAction" 
	onPageLoadCallBack="myOnPageLoad" >
	
	<adsm:form 
		action="/expedicao/digitarPreAWBDimensoes" 
		idProperty="idDimensao">
		
		<adsm:section caption="dimensoes" width="56"/>
		
		<adsm:label key="branco" style="border:none;" width="1%" />
		
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
			
		<adsm:label key="branco" style="border:none;" width="1%"/>
		
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
				service="lms.expedicao.digitarPreAWBDimensoesAction.storeInSession" 
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
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function myOnPageLoad_cb(){
		onPageLoad_cb();
		populaGrid();
    }
    
    function initWindow(eventObj) {
    	//alert(eventObj.name);
		if(eventObj.name == "removeButton_grid"){
			setDisabled("removeButton",false);
		} 
    }
    
    
    // Fun��o que limpa todos os campos da tela
    function limparCampos(){
    	resetValue("idDimensao");
    	resetValue("nrAltura");
    	resetValue("nrLargura");
    	resetValue("nrComprimento");
    	resetValue("nrQuantidade");
    	setFocusOnFirstFocusableField();
    }
    
    // Fun��o que popula a grid
    function populaGrid() {
		dimensaoGridDef.executeSearch({},true); 
	}
	
	// Callback da fun��o que armazena os dados na sess�o
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
	
	// M�todo que chama o servi�o da action para buscar o elemento selecionado na grid
	function populaForm(valor) {
		var sdo = createServiceDataObject("lms.expedicao.digitarPreAWBDimensoesAction.findById", "findId", {idDimensao:valor} );
        xmit({serviceDataObjects:[sdo]});
		return false;
	}
	
	// Callback que popula os campos da tela ap�s o usu�rio 
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
</script>

<%--@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.digitarPreAWBDimensoesAction">
	<adsm:form action="/expedicao/digitarPreAWBDimensoes">
	
		<adsm:section caption="dimensoes" width="65"/>
		<adsm:label key="branco" style="border:none;" width="1%" />
		
		<adsm:textbox 
			dataType="integer" 
			minValue="1"
			size="8" 
			property="altura" 
			required="true"
			label="altura" 
			width="19%" 
			labelWidth="13%" 
			unit="cm" 
			maxLength="8"/>
			
		<adsm:textbox 
			dataType="integer"
			minValue="1"
			size="8" 
			property="largura"
			required="true"
			label="largura" 
			width="54%" 
			labelWidth="13%" 
			unit="cm" 
			maxLength="8"/>
		
		<adsm:label key="branco" style="border:none;" width="1%" />
		
		<adsm:textbox 
			dataType="integer" 
			minValue="1"
			size="8" 
			property="comprimento" 
			required="true"
			label="comprimento" 
			width="19%" 
			labelWidth="13%" 
			unit="cm" 
			maxLength="8"/>
			
		<adsm:textbox 
			dataType="integer" 
			minValue="1"
			size="8" 
			property="quantidade" 
			required="true"
			label="quantidade" 
			width="54%" 
			labelWidth="13%" 
			maxLength="5"/>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="novo"/>
			<adsm:button caption="salvar"/>
			<adsm:button caption="fechar"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		property="dimensoesList"
		idProperty="idDimensao"
		unique="true"
		showPagging="true">
		
		<adsm:gridColumn 
			title="altura" 
			unit="cm" 
			property="altura" 
			width="25%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="largura" 
			unit="cm" 
			property="largura" 
			width="25%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="comprimento" 
			unit="cm" 
			property="comprimento" 
			width="27%" 
			align="right"/>
			
		<adsm:gridColumn 
			title="quantidade" 
			property="quantidade" 
			width="23%" 
			align="right"/>
			
		<adsm:buttonBar>
			<adsm:button caption="excluir"/>
		</adsm:buttonBar>
		
	</adsm:grid>
</adsm:window--%>