<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirBDM">
	
		<adsm:hidden property="vigenteEm"/>
		<adsm:hidden property="filialMatriz"/>
		<adsm:hidden property="sgFilial"/>

		<adsm:lookup action="/municipios/manterFiliais"
					 service="lms.contasreceber.emitirFaturasNacionaisAction.findLookupFilial" 
					 dataType="text" 
					 property="filial" 
					 idProperty="idFilial"
					 criteriaProperty="sgFilial" 
					 label="filialOrigem" 
					 size="3" 
					 maxLength="3" 
					 width="40%"
					 labelWidth="20%"
					 exactMatch="true">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filial.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="sgFilial" formProperty="sgFilial"/>
			<adsm:propertyMapping modelProperty="historicoFiliais.vigenteEm" criteriaProperty="vigenteEm"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>

        <adsm:textbox label="numero" property="nrBdm" dataType="integer" size="10" labelWidth="20%" width="20%" />
        
        <adsm:combobox property="tpFormatoRelatorio" 
    				   label="formatoRelatorio" 
    				   labelWidth="20%" 
    				   required="true"
    				   defaultValue="pdf"
					   domain="DM_FORMATO_RELATORIO"/>
					   
		<adsm:buttonBar> 
			<adsm:button caption="visualizar" onclick="imprimeRelatorio()" buttonType="reportButton"/>
			<adsm:resetButton/>	
		</adsm:buttonBar>					   
   	</adsm:form>
   	
</adsm:window>

<script>

	function initWindow(eventObj){
		
		// Ao clicar em limpar, traz novamente a filial da sessão
		if( eventObj.name == 'cleanButton_click') {
			buscaUsuarioSessao();
		}
		
	}

   /**
	*	Executa a busca da filial do usuário para setá-la como padrão na lookup de filial
	*
	*/
	function myOnPageLoadCallBack_cb(data, erro){
		onPageLoad_cb(data, erro);
		buscaUsuarioSessao();
	}
	
	// busca o usuário da sessão
	function buscaUsuarioSessao(){
	
		_serviceDataObjects = new Array();
		
		addServiceDataObject(createServiceDataObject("lms.contasreceber.emitirBDMAction.findFilialUsuario",
			"retornoBuscaFilialUsuario", 
			new Array()));

        xmit(false);	

	}
	
   /**
	*	Seta a filial do usuário como padrão.
	* 	Se filial usuario for 'MTZ' pesquisa somente filiais ativas
	*/
	function retornoBuscaFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('filial.sgFilial'));
			return false;		
		}
		
		setElementValue('filialMatriz', data.filialMatriz);
		setElementValue('filial.idFilial', data.filial.idFilial);
		setElementValue('filial.sgFilial', data.filial.sgFilial);
		setElementValue('sgFilial', data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		setElementValue('vigenteEm', data.vigenteEm);
		
		var filialMatriz = data.filialMatriz;
		
		if ( filialMatriz == 'true' ) {
			document.getElementById('filial.idFilial').required = 'true';
			document.getElementById('nrBdm').required = 'true';
		} else {
			document.getElementById('filial.idFilial').required = 'false';		
			document.getElementById('nrBdm').required = 'false';
			setDisabled('filial.idFilial', true);
		}
		
	}
	
	function imprimeRelatorio(){
		executeReportWithCallback('lms.contasreceber.emitirBDMAction', 'openReportWithLocator', document.forms[0]);
	}
</script>