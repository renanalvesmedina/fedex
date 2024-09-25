<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.emitirNotaDebitoCobrancaJurosAction"  onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/contasReceber/emitirNotaDebitoCobrancaJuros">
	
		<adsm:hidden property="filial.idFilial"/>
		
		<adsm:textbox label="filialCobranca"
					  labelWidth="18%"
					  width="32%" 
		              dataType="text" 
		              property="filial.sgFilial"
		              disabled="true"
		              size="3"
		              maxLength="3"
		              required="true"
		              serializable="true">
			<adsm:textbox property="filial.pessoa.nmFantasia" 
			              size="25" 
			              dataType="text"
			              maxLength="50"
			              disabled="true"
			              serializable="true"/>	

		</adsm:textbox>
		
		<adsm:textbox label="numeroNota" dataType="integer" property="numeroNota" size="20" maxLength="15"/>		
			
		<adsm:combobox property="tpFormatoRelatorio" 
					   label="formatoRelatorio"
					   labelWidth="18%"
					   required="true"
					   defaultValue="pdf"
		   			   domain="DM_FORMATO_RELATORIO"/>
			
		<adsm:buttonBar>
			<adsm:button caption="visualizar" onclick="emitirRelatorio();" buttonType="reportButton" disabled="false"/>
			<adsm:resetButton />
		</adsm:buttonBar>		
		
	</adsm:form>
</adsm:window>

<script>

	function emitirRelatorio() {
		executeReportWithCallback("lms.contasreceber.emitirNotaDebitoCobrancaJurosAction",
									"openReportWithLocator",
									document.forms[0]);
	}

	/**
	*	Executa a busca da filial do usuário para setá-la como padrão na lookup de filial
	*
	*/
	function myOnPageLoadCallBack_cb(data, erro){
	
		onPageLoad_cb(data, erro);		
		buscaFilialSessao();
		
	}
	
	function buscaFilialSessao(){
		var dados = new Array();
         
        var sdo = createServiceDataObject("lms.contasreceber.emitirNotaDebitoCobrancaJurosAction.findFilialUsuario",
                                          "retornoBuscaFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});		
	}
	
	
	/**
	*	Seta a filial do usuário como padrão.
	*/
	function retornoBuscaFilialUsuario_cb(data,erro){
		
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('filial.sgFilial'));
			return false;		
		}
		
		setElementValue('filial.idFilial',data.filial.idFilial);
		setElementValue('filial.sgFilial',data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);		
		
		setFocusOnFirstFocusableField(document);		
		
	}
	
	function initWindow(eventObj){
		if( eventObj.name == 'cleanButton_click' ){
			buscaFilialSessao();
		}
	}

	
</script>