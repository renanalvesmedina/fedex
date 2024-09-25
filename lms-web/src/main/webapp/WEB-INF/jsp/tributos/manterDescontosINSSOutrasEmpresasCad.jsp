<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterDescontosINSSOutrasEmpresasAction" onPageLoadCallBack="myOnPageLoadCallBack">
	<adsm:form action="/tributos/manterDescontosINSSOutrasEmpresas" idProperty="idDescontoInssCarreteiro" onDataLoadCallBack="descontoINSS">

		<adsm:i18nLabels>
			<adsm:include key="LMS-23004"/>
		</adsm:i18nLabels>



		<adsm:textbox dataType="JTDate" property="dtEmissaoRecibo" label="dataEmissaoRecibo" required="true" width="85%"/>
		
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="tpPessoa" serializable="false" value="F"/>
		
		

		<adsm:lookup service="lms.tributos.manterDescontosINSSOutrasEmpresasAction.findLookupProprietario" 
					 property="proprietario"
					 label="proprietario"
					 afterPopupSetValue="verificaPessoaPopup"
					 onDataLoadCallBack="verificaPessoa"
					 idProperty="idProprietario"
					 criteriaProperty="pessoa.nrIdentificacao"					 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"					 
					 dataType="text"
					 maxLength="20"
					 action="/contratacaoVeiculos/manterProprietarios"  
					 exactMatch="true"
					 minLengthForAutoPopUpSearch="5"
					 size="20"
					 width="85%"
					 required="true">                    
			
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
						
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="proprietario.pessoa.nmPessoa"/>
			
 		    <adsm:textbox  size="50"  dataType="text" property="proprietario.pessoa.nmPessoa" disabled="true"/>   
		</adsm:lookup>



		<adsm:combobox property="tpEmpresa" domain="DM_TIPO_EMPRESA_INSS" defaultValue="O" disabled="true" label="tipoEmpresa" required="true" width="85%"
			onchange="tipoEmpresaChange(this,true)"/>

        <adsm:hidden property="dataAtual" serializable="false"/>
		<adsm:lookup label="filial" 
     				 property="filial" 
     				 dataType="text"  
     				 service="lms.tributos.manterDescontosINSSOutrasEmpresasAction.findLookupBySgFilialVigenteEm" 
     				 exactMatch="true"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial" 
					 size="3" 
					 maxLength="3" 
					 width="85%" 
					 onclickPicker="filialPickerClick();"
			         onchange="return filialOnChange();"
			         disabled="true"
					 action="/municipios/manterFiliais">
					 
					 <adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>
					 
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" 
				serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="dsEmpresa" label="empresa" size="70" maxLength="60" required="true" width="85%" />

		<adsm:textbox dataType="text" property="nrRecibo" label="numeroRecibo" size="20" maxLength="20" required="true" />
		<adsm:textbox dataType="currency" property="vlInss" label="valorINSS" minValue="0" maxLength="30" 
			mask="#,###,###,###,###,##0.00" required="true" />

		<adsm:textbox labelWidth="15%" dataType="JTDateTimeZone" label="dataHoraInclusao" property="dhInclusao" size="10" maxLength="20" width="85%" disabled="true" picker="false"/>
		<adsm:buttonBar>
			<adsm:button id="storeButton" onclick="myStore(this)" caption="salvar" disabled="false"/>
 			<adsm:newButton id="newButton"/>
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>

	/**
	*	Método chamado ao clicar no botão Salvar. Salva um desconto
	*
	*/	
	function myStore(elemento){
	
	
		storeButtonScript('lms.tributos.manterDescontosINSSOutrasEmpresasAction.store', 'store', elemento.form);

		
		var obj = document.getElementById("tpEmpresa");			
		if ( (getElementValue("filial.idFilial") == "") && (obj.options[obj.selectedIndex].value == "M") ){
			setFocus("filial.sgFilial");
		}

	}
	
	
	

	function initWindow(eventObj){
		
		var obj = document.getElementById("tpEmpresa");
		
		if( eventObj.name = 'newButton_click'){
			if (obj.options[obj.selectedIndex].value == "M"){
				//document.getElementById("dsEmpresa").value = "Expresso Mercúrio S.A.";
				document.getElementById("dsEmpresa").disabled = true;
			}else{
				document.getElementById("dsEmpresa").disabled = false;
			}
		}
		
		setDisabled("storeButton",false);	
	}

	function descontoINSS_cb(data,erro){
		onDataLoad_cb(data,erro);
		tipoEmpresaChange(document.getElementById("tpEmpresa"));
	}

	/**
	 *Quando o Tipo de Empresa for 'Mercúrio', a filial é obrigatória.
	 */
	function tipoEmpresaChange(obj,limpaCampo){
		if (obj.options[obj.selectedIndex].value == "M"){
			document.getElementById("nrRecibo").dataType = "integer";
			document.getElementById("filial.idFilial").required = "true";
			document.getElementById("dsEmpresa").value = "Expresso Mercúrio S.A.";
			document.getElementById("dsEmpresa").disabled = true;
		}else{			
			document.getElementById("nrRecibo").dataType = "text";
			
			document.getElementById("filial.idFilial").required = "false";
			
			if( limpaCampo ){
				document.getElementById("dsEmpresa").value = "";
			}
			
			document.getElementById("dsEmpresa").disabled = false;
		}
		
		if (limpaCampo) {
			setElementValue("nrRecibo", "");
		}
		
	}
	
	
   /**
	click da lupa da lookup Filial
	*/
	function filialPickerClick(){
		trocaServicoFilial();
		lookupClickPicker({e:document.getElementById('filial.idFilial')});
	}
	
	/**
	onChange da lookup Filial
	*/	
	function filialOnChange(){
		trocaServicoFilial();
		return filial_sgFilialOnChangeHandler();		
	}

	/**
	Trocar os serviços e propperty mapping da lookup Filial.
	*/	
	function trocaServicoFilial(){
		document.getElementById("filial.idFilial").service = "lms.tributos.manterDescontosINSSOutrasEmpresasAction.findLookupBySgFilialVigenteEm";
		document.getElementById("filial.idFilial").url = contextRoot+"/municipios/manterFiliais.do";						
		document.getElementById("filial.idFilial").propertyMappings = 
		[
			{ modelProperty:"pessoa.nmFantasia", relatedProperty:"filial.pessoa.nmFantasia"}, 
			{ modelProperty:"sgFilial", criteriaProperty:"filial.sgFilial", inlineQuery:true}, 
			{ modelProperty:"historicoFiliais.vigenteEm", criteriaProperty:"dataAtual", inlineQuery:true},
			{ modelProperty:"sgFilial", relatedProperty:"filial.sgFilial"}
		];			
	}

	function myOnPageLoadCallBack_cb(data, error){
	
		onPageLoad_cb(data,error);
		
		var dados = new Array();
         
        var sdo = createServiceDataObject("lms.tributos.manterDescontosINSSOutrasEmpresasAction.findDataAtual",
                                          "setaDataAtual",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
		
	}
	
	function setaDataAtual_cb(data,error){
	
		setElementValue("dataAtual",data._value);
		document.getElementById("dataAtual").masterLink = 'true';
	
	}
	
	
/* Trata o retorno da poup-up para verifica se o tpPessoa eh fisica, caso contrário mostra erro    */
	function verificaPessoaPopup(data){

		if( data != undefined ){
			if( data.pessoa.tpIdentificacao.value == 'CNPJ' ){
				alert(i18NLabel.getLabel('LMS-23004'));
				resetValue('proprietario.idProprietario');
				setFocus('proprietario.pessoa.nrIdentificacao');
				return false;
			}
		}
		
	}


/* Trata o retorno da lookup para verifica se o tpPessoa eh fisica, caso contrário mostra erro    */
	function verificaPessoa_cb(data,error){
		
		var retorno = proprietario_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		if( retorno == true ){
			verificaPessoaPopup(data[0]);
		}		
		
		return retorno;
		
	}


</script>



