<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.emitirTransferenciasDebitoAction" onPageLoadCallBack="myOnPageLoadCallBack"> 

	<adsm:form action="/contasReceber/emitirTransferenciasDebito">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-36057"/>
		</adsm:i18nLabels>
	
		<adsm:hidden property="filialMatriz"    serializable="false"/>
		<adsm:hidden property="origem"  		serializable="true"/>
		<adsm:hidden property="siglaNomeFilial" serializable="true"/>
		<adsm:hidden property="vigenteEm"       serializable="true"/>
		<adsm:hidden property="tpSituacaoTransferencia" serializable="true" value="CA"/>
	
		<adsm:lookup dataType="text" 
					 property="filial" 
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 service="lms.contasreceber.emitirTransferenciasDebitoAction.findLookupFilial" 
					 action="/municipios/manterFiliais" 
					 label="filial" 
					 size="3" 
					 required="true"
					 maxLength="3" 
					 width="85%"		
					 exactMatch="true">
				<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
				<adsm:propertyMapping modelProperty="sgFilial" relatedProperty="siglaFilial"/>	
				<adsm:propertyMapping modelProperty="siglaNomeFilial" relatedProperty="siglaNomeFilial"/>	
						
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="31" maxLength="30" disabled="true"/>
				<adsm:propertyMapping criteriaProperty="vigenteEm" modelProperty="historicoFiliais.vigenteEm"/>
			</adsm:lookup>
	   <adsm:hidden property="siglaFilial" serializable="true"/>

		<adsm:textbox dataType="integer" property="nrTransferencia" label="numero" size="12" maxLength="10" width="35%"  />

		<adsm:combobox label="origem" property="tpOrigem" domain="DM_ORIGEM"  width="35%"/>
		
		<adsm:combobox property="tpFormatoRelatorio" label="formatoRelatorio"  domain="DM_FORMATO_RELATORIO" required="true" width="85%" defaultValue="pdf"/>

		<adsm:buttonBar>
			<adsm:button caption="visualizar" id="btnVisualizar" onclick="visualizarOnClick(this)" disabled="false"/>			
			<adsm:button caption="limpar" id="btnLimpar" disabled="false" buttonType="resetButton" onclick="myLimpar(this)"/>
		</adsm:buttonBar>
		
	</adsm:form>
	
</adsm:window>
<script>

	function myLimpar(elem){
		cleanButtonScript(elem.document);
		findFilialUsuario();
	}

	function initWindow(eventObj){
		setDisabled('btnVisualizar',false);
	}

	/**
	*	Executa a busca da filial do usuário para setá-la como padrão na lookup de filial
	*
	*/
	function myOnPageLoadCallBack_cb(data, erro){
	
		setDisabled('btnVisualizar',false);
		
		onPageLoad_cb(data, erro);
		findFilialUsuario();
		
	}
	
	function findFilialUsuario(){
		var dados = new Array();
         
        var sdo = createServiceDataObject("lms.contasreceber.emitirTransferenciasDebitoAction.findFilialUsuario",
                                          "retornoBuscaFilialUsuario",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
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
		
		setElementValue('filialMatriz',data.filialMatriz);
		setElementValue('filial.idFilial',data.filial.idFilial);
		setElementValue('filial.sgFilial',data.filial.sgFilial);
		setElementValue('filial.pessoa.nmFantasia', data.filial.pessoa.nmFantasia);
		setElementValue('siglaNomeFilial',data.siglaNomeFilial);
		setElementValue('vigenteEm',data.vigenteEm);
		
		var filialMatriz = data.filialMatriz;
		
		if( filialMatriz == 'true' ){
		
			document.getElementById('filial.idFilial').required = 'true';
			document.getElementById('nrTransferencia').required = 'true';
		
			
			
		} else {
		
			setDisabled('filial.idFilial','true');
			document.getElementById('filial.idFilial').masterLink = 'true';
			document.getElementById('filial.sgFilial').masterLink = 'true';
			document.getElementById('filial.pessoa.nmFantasia').masterLink = 'true';
		}
		
		setFocusOnFirstFocusableField(document);
		
		
	}
	
	/**
	*	Chamado ao clicar no botão Visualizar, teste de campos
	*/
	function visualizarOnClick(elemento){
		
		var idFilial        = getElementValue('filial.idFilial');
		var nrTransferencia = getElementValue('nrTransferencia');
		var tpOrigem        = getElementValue('tpOrigem');
		var origemIndex     = document.getElementById('tpOrigem').selectedIndex;
		var origemOpcao     = document.getElementById('tpOrigem').options[origemIndex];
		var siglaNomeFilial = getElementValue('siglaNomeFilial');
		var isFilialMatriz  = getElementValue('filialMatriz');
		
		if( origemIndex != 0 ){
			setElementValue('origem',origemOpcao.text);
		}else{
		    setElementValue('origem','');  
		}
				
		if( (idFilial == '' || nrTransferencia == '') && isFilialMatriz == 'true' ){
			
			alert(i18NLabel.getLabel('LMS-36057'));
			
			if( idFilial == '' ){
				setFocus(document.getElementById('filial.sgFilial'));
			} else {
				setFocus(document.getElementById('nrTransferencia'));
			}
			
			return false;
		}
		
		reportButtonScript('lms.contasreceber.emitirTransferenciasDebitoAction', 'openPdf', elemento.form);
        
	}
	
	function retornoVisualizar_cb(data,erro){
		if( erro != undefined ){
			alert(erro);
			setFocus(document.getElementById('btnVisualizar'));
			return false;		
		} 
	}
	
	
</script>