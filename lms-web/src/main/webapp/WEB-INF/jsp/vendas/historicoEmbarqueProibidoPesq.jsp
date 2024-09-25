<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/vendas/historicoEmbarqueProibido">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-30050"/>
		</adsm:i18nLabels>
	
		<adsm:combobox label="regional" 
		               property="regional.idRegional" 
		               boxWidth="250"
		               optionLabelProperty="siglaDescricao" 
		               optionProperty="idRegional" 
		               onchange="return myOnChangeComboRegional(this)"
		               service="lms.vendas.historicoEmbarqueProibidoAction.findComboRegional">
			<adsm:propertyMapping relatedProperty="regional.siglaDescricao" modelProperty="siglaDescricao"/>
		</adsm:combobox>
		
		<adsm:hidden property="regional.siglaDescricao"/>
		<adsm:hidden property="tpAcesso" value="F"/>
		<adsm:hidden property="siglaNomeFilial"/>
		
        <adsm:lookup label="filial" 
                     property="filial" 
                     service="lms.vendas.historicoEmbarqueProibidoAction.findLookupBySgFilial"
                     action="/municipios/manterFiliais" 
                     idProperty="idFilial" 
                     criteriaProperty="sgFilial" 
                     dataType="text" 
                     size="3" 
                     labelWidth="15%" 
                     width="8%" 
                     maxLength="3" 
                     onDataLoadCallBack="myRetornoLookupFilial"
                     afterPopupSetValue="setaSiglaNomeFilial">
        	<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" inlineQuery="true"/>
            <adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" width="27%" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
        
		<adsm:combobox  property="motivoProibido.idMotivoProibidoEmbarque" boxWidth="250"
			optionLabelProperty="dsMotivoProibidoEmbarque" optionProperty="idMotivoProibidoEmbarque" 
			service="lms.vendas.motivoProibidoEmbarqueService.find" 
			label="motivo">
			<adsm:propertyMapping relatedProperty="motivoProibido.dsMotivoProibidoEmbarque"
				modelProperty="dsMotivoProibidoEmbarque"/>
		</adsm:combobox>
		<adsm:hidden property="motivoProibido.dsMotivoProibidoEmbarque"/>
		<adsm:range label="periodo">
			<adsm:textbox dataType="JTDate" property="dtEmissaoInicial" />
			<adsm:textbox dataType="JTDate" property="dtEmissaoFinal" />
		</adsm:range>
		<adsm:lookup	label="cliente" labelWidth="15%" width="85%" size="20" maxLength="20"  serializable="true"
					 	service="lms.vendas.clienteService.findLookup" 
					 	action="/vendas/manterDadosIdentificacao"
					 	dataType="text" 
					 	property="cliente" 
					 	idProperty="idCliente" 
					 	criteriaProperty="pessoa.nrIdentificacao" 
					 	relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 	exactMatch="false"
					 	disabled="false">					 	
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="50" maxLength="50" disabled="true" serializable="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpFormatoRelatorio" 
	  				   label="formatoRelatorio" 
  					   required="true"
  					   defaultValue="pdf"
  					   domain="DM_FORMATO_RELATORIO"/>
		
		<adsm:buttonBar>			
			<adsm:button caption="visualizar" id="btnVisualizar" buttonType="reportButton" disabled="false" onclick="verificacoes(this)"/>
			<adsm:button caption="limpar" id="btnLimpar" buttonType="resetButton" onclick="myLimpar(this);"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	
	function myLimpar(elem){
		cleanButtonScript(elem.document);
		buscaFilialRegional(true,true);
	}
	
	function myOnPageLoadCallBack_cb(data,error){
		onPageLoad_cb(data,error);
		buscaFilialRegional(true,true);
	}
	
	function buscaFilialRegional(realizaXmit, novosDados){
		if( novosDados == true ){	
			_serviceDataObjects = new Array();
		}
         
        var dados = new Array();         
         
        addServiceDataObject(createServiceDataObject("lms.vendas.historicoEmbarqueProibidoAction.findFilialAndRegionalUsuario",
			                                         "setaFilialAndRegionalUsuario",
			                                         dados));
        if( realizaXmit == true ){
	        xmit(false);
	    }
	}
	
	function setaFilialAndRegionalUsuario_cb(data,error){
		if( error != undefined ){
			alert(error);			
			setFocusOnFirstFocusableField(document);
			return false;
		}
		
		fillFormWithFormBeanData(0, data);
		setaDescricaoRegional(getElement('regional.idRegional'));
		setaSiglaNomeFilial();
		setFocusOnFirstFocusableField(document);
	}

	function verificacoes(elem){
	
		var filial = getElementValue('filial.idFilial');
		var regional = getElementValue('regional.idRegional');
		
		if( filial == '' && regional == '' ){
			alert(i18NLabel.getLabel('LMS-30050'));
			setFocusOnFirstFocusableField(document);
			return false;
		}
	
		reportButtonScript('lms.vendas.historicoEmbarqueProibidoAction', 'openPdf', elem.form);
	}		
	
	function myRetornoLookupFilial_cb(data,error){
		var retorno = filial_sgFilial_exactMatch_cb(data);
		setaSiglaNomeFilial();
		return retorno;
	}
	
	function setaSiglaNomeFilial(){
		if( getElementValue('filial.idFilial') != '' ){
			setElementValue('siglaNomeFilial',getElementValue('filial.sgFilial') + " - " + getElementValue('filial.pessoa.nmFantasia'));
		} else {
			resetValue('siglaNomeFilial');
		}
	}
	
	function myOnChangeComboRegional(elem){
		var retorno = comboboxChange({e:elem});
		setaDescricaoRegional(elem);
		return retorno;
	}
	
	function setaDescricaoRegional(elem){
		if( elem.selectedIndex != 0 ){
			setElementValue('regional.siglaDescricao',elem.options[elem.selectedIndex].text);
		}
	}
</script>