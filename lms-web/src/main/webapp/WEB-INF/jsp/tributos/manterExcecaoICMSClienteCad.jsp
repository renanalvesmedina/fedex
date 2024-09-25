<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterExcecaoICMSClienteAction" onPageLoadCallBack="carregaPagina">

	<adsm:form action="/tributos/manterExcecaoICMSCliente" idProperty="idExcecaoICMSCliente" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:hidden property="tpSituacao" value="A" serializable="false"/>
  	    <adsm:lookup 
  	    			property="unidadeFederativa"
				    idProperty="idUnidadeFederativa" 
				    criteriaProperty="sgUnidadeFederativa"
					service="lms.municipios.unidadeFederativaService.findLookup" 
					dataType="text" 
					labelWidth="20%" 
					required="true"
					maxLength="3"
					width="7%" 
					label="uf" 
					size="3"
					action="/municipios/manterUnidadesFederativas" 
					minLengthForAutoPopUpSearch="2" 
					exactMatch="true">
            <adsm:textbox
                    dataType="text" 
                    property="unidadeFederativa.nmUnidadeFederativa" 
                    disabled="true"
                    width="23%" 
                    size="20" />					 
			<adsm:propertyMapping relatedProperty="unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="unidadeFederativa.siglaDescricao" modelProperty="siglaDescricao" />
			
			<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao" />
		</adsm:lookup>
		
		<adsm:hidden
					property="unidadeFederativa.siglaDescricao" 
					serializable="false"/>			
		
		<adsm:hidden property="tipoTributacaoIcms.dsTipoTributacaoIcms"/>					
		
		<adsm:combobox 
					label="tipoTributacao" 
					property="tipoTributacaoIcms.idTipoTributacaoIcms" 
					required="true"
					service="lms.tributos.manterExcecaoICMSClienteAction.findComboTipoTributacaoIcmsOnlyActivaValues" 
					onlyActiveValues="true" 
					optionLabelProperty="dsTipoTributacaoIcms" 
					optionProperty="idTipoTributacaoIcms"  
					onchange="return comboTipoTributacaoIcmsOnChange();"
					labelWidth="20%" 
					width="30%" 
					boxWidth="200"/>
					
		<adsm:combobox 
					property="tpFrete" 
					label="tipoFrete" 
					required="true"
					domain="DM_TIPO_FRETE"
					labelWidth="20%" 
					width="30%"/>
					
		<adsm:checkbox 
					property="blSubcontratacao" 
					disabled="false" 
					label="subcontratacao" 
					onclick="blSubcontratacaoOnClick(this);"
					labelWidth="20%"  
					width="30%" />			
				
		<adsm:combobox  property="tipoCnpj" 
						domain="DM_TIPO_CNPJ"
						labelWidth="20%" 
						defaultValue="P"
						onchange="onChangeComboTipoCnpj(this);"
						width="10%" 
						label="nrCNPJParcialDev" >
						<adsm:textbox   property="nrCNPJParcialDev" 
										maxLength="8"
										size="14"
										mask="00000000"
										onchange="validateCNPJ(this)"
										required="true"
										dataType="integer"
										width="70%">
								<adsm:textbox   property="nmDevedor" 
												serializable="false" 
												dataType="text" 
												disabled="true" 
												size="50"/>
						</adsm:textbox>							
		</adsm:combobox>
		
		<adsm:checkbox  property="blObrigaCtrcSubContratante"
						label="blObrigaCTRCSubcontratante"
						labelWidth="20%"
						width="80%" 
						serializable="true"/>
						
		<adsm:textarea 	label="dsRegimeEspecial"
						columns="100"
						maxLength="85"
						property="dsRegimeEspecial"
						labelWidth="20%"
						width="80%"
						rows="3"/>

		<adsm:textbox dataType="text" label="cdEmbLegalMasterSaf" disabled="true"
			property="cdEmbLegalMastersaf" size="100" maxLength="10" labelWidth="20%" width="80%" />

		<adsm:range label="vigencia" labelWidth="20%" width="80%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		
		<adsm:listbox label="naturezaProduto" size="4" boxWidth="170" width="80%" labelWidth="20%" 
	         		property="naturezaProduto" 
	     			  optionProperty="idNaturezaProduto"
	     			  optionLabelProperty="dsNaturezaProduto">

	     	<adsm:combobox 
                 property="naturezaProduto.idNaturezaProduto" 
                 optionLabelProperty="dsNaturezaProduto" optionProperty="idNaturezaProduto" 
                 service="lms.tributos.manterExcecaoICMSClienteAction.findComboNaturezaProduto"
                 required="false" boxWidth="170" onlyActiveValues="true">
                	<adsm:propertyMapping criteriaProperty="idExcecaoICMSCliente"	modelProperty="idExcecaoICMSCliente"  />
            </adsm:combobox>                            		  	                  
        </adsm:listbox>
		
		<adsm:buttonBar>
			<adsm:button caption="consultarExcecaoICMSClienteLog" action="/tributos/consultarExcecaoICMSClienteLog" cmd="main">
             	<adsm:linkProperty src="idExcecaoICMSCliente" target="excecaoICMSCliente.idExcecaoICMSCliente"/>
            </adsm:button>

			<adsm:button caption="remetentes" action="/tributos/manterRemetentesExcecaoICMSCliente" cmd="main">
            	<adsm:linkProperty src="idExcecaoICMSCliente" target="excecaoICMSCliente.idExcecaoICMSCliente"/>
            	<adsm:linkProperty src="unidadeFederativa.idUnidadeFederativa" target="excecaoICMSCliente.unidadeFederativa.idUnidadeFederativa"/>
            	<adsm:linkProperty src="unidadeFederativa.sgUnidadeFederativa" target="excecaoICMSCliente.unidadeFederativa.sgUnidadeFederativa"/>
            	<adsm:linkProperty src="unidadeFederativa.nmUnidadeFederativa" target="excecaoICMSCliente.unidadeFederativa.nmUnidadeFederativa"/>
            	<adsm:linkProperty src="tipoTributacaoIcms.idTipoTributacaoIcms" target="excecaoICMSCliente.tipoTributacaoIcms.idTipoTributacaoIcms"/>
            	<adsm:linkProperty src="tipoTributacaoIcms.dsTipoTributacaoIcms" target="excecaoICMSCliente.tipoTributacaoIcms.dsTipoTributacaoIcms"/>
            	<adsm:linkProperty src="tpFrete" target="excecaoICMSCliente.tpFrete"/>
            	<adsm:linkProperty src="nrCNPJParcialDev" target="excecaoICMSCliente.nrCNPJParcialDev"/>
            	<adsm:linkProperty src="nmDevedor" target="nmDevedor"/>
            </adsm:button> 
			<adsm:button caption="salvar" onclick="myStoreButton();" buttonType="storeButton" id="btnSalvar"/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>		
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>

	getElement("naturezaProduto_naturezaProduto.idNaturezaProduto").masterLink = true;
	getElement("tipoCnpj").required = true;
	getElement("nrCNPJParcialDev").label = "CNPJ";
	
	/*Carrega dados da pagina*/
	function carregaPagina_cb(data,error){	
		if(error != undefined){
			alert(error);
			return;
		}
		carregaNaturezaProduto();
	}
	
	/*Carrega o combo com a lista de natureza produto*/
	function carregaNaturezaProduto(){		
		addServiceDataObject(createServiceDataObject("lms.tributos.manterExcecaoICMSClienteAction.findComboNaturezaProduto", 
			"retornoNaturezaProduto"));		 
		xmit(false);				
	}

	/*Callback utilizado pela */
	function retornoNaturezaProduto_cb(data,error){
		comboboxLoadOptions({e:document.getElementById("naturezaProduto_naturezaProduto.idNaturezaProduto"), data:data});
	}
				
	/*Função responsável por validar o número parcial do CNPJ*/
	function validateCNPJ(cnpj){
	
		if(cnpj.value != "" && cnpj.value.length > 0){
			_serviceDataObjects = new Array(); 
			var data = buildFormBeanFromForm(document.forms[0]);
			
			addServiceDataObject(createServiceDataObject("lms.tributos.manterExcecaoICMSClienteAction.findNrCNPJParcialDevEqualNrIdentificacaoPessoa", 
			"validateCNPJ", { nrCNPJParcialDev:cnpj.value, tipoCnpj:getElementValue("tipoCnpj") })); 
			xmit(false);
		} else {
			resetValue('nmDevedor');
		}
	}
	
	/*Callback utilizado pela funcao validateCNPJ*/
	function validateCNPJ_cb(data, errors){
		
		if(errors != undefined && errors != ""){
			alert (errors);
			setElementValue("nrCNPJParcialDev", "");
			setFocus("nrCNPJParcialDev");
			resetValue('nmDevedor');
		} else {		
			setElementValue('nmDevedor',data.nmPessoa);
		}
		
	}
	
	/*Function que limpa os campos da tela e desabilita todos os campos.*/ 	
	function limpar(){
		setDisabled("naturezaProduto", false);
		resetValue("idExcecaoICMSCliente");
		newButtonScript(document, true, {name:"newButton_click"});
		desabilitaTodosCampos(false);
	}

	/*Function que desabilita todos os campos da tela e seta os valores default*/	
	function desabilitaTodosCampos(val){

		if (val == undefined){ 
			val = true;	
		}
		
		setDisabled("unidadeFederativa.idUnidadeFederativa",val);
		setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms",val);			
		setDisabled("tpFrete",val);		
		setDisabled("nrCNPJParcialDev",val);	
		setDisabled("tipoCnpj", val);
		setDisabled("blSubcontratacao",val);	
		setDisabled("dtVigenciaFinal", val);	
		setDisabled("dtVigenciaInicial",val);		
		setDisabled("dsRegimeEspecial", val);
		
		setFocusOnFirstFocusableField();				
	}
	
	/*Funcao padrao do frame o antrar na tela*/
	function initWindow(eventObj){
	
		setDisabled("naturezaProduto_naturezaProduto.idNaturezaProduto", false);

		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
		}
		
		setDisabled('btnLimpar', false);
		if( eventObj.name == "storeButton" ){
			setFocus('btnLimpar',true,true);
		} 
		
		if ( eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			setDisabled("blObrigaCtrcSubContratante", true);
		} else {
			setMaskCnpj("P");
		}
		
		comboTipoTributacaoIcmsOnChange();
	}
	
	/*Verifica a data de vigencia final */
	function validateDtVigenciaFinal(dtVigenciaFinal){
		if (dtVigenciaFinal == undefined || dtVigenciaFinal == "") {
			setDisabled("dtVigenciaFinal", false);
			return;
		}	
		_serviceDataObjects = new Array(); 
		var data = buildFormBeanFromForm(document.forms[0]);
			
		addServiceDataObject(createServiceDataObject("lms.tributos.manterExcecaoICMSClienteAction.validateDtVigenciaFinal", 
		"validateDtVigenciaFinal", { dtVigenciaFinal: getElementValue("dtVigenciaFinal") })); 
		xmit(false);
	}
	
	/*callback utilizado pela funcao validateVigenciaFinal*/
	function validateDtVigenciaFinal_cb(data, error){
		if (error != undefined) alert(error);
		
		if (data.dtVigenciaFinalMaiorIgualDtAtual != undefined &&
				data.dtVigenciaFinalMaiorIgualDtAtual == "false") {
			setDisabled("dtVigenciaFinal", true);
		}else{
		    setDisabled("dtVigenciaFinal", false);  
		} 
	}
	

	/*Evento onchange da combo tipotributacao*/
	function comboTipoTributacaoIcmsOnChange(){
		setElementValue("tipoTributacaoIcms.dsTipoTributacaoIcms", getElement("tipoTributacaoIcms.idTipoTributacaoIcms").options[getElement("tipoTributacaoIcms.idTipoTributacaoIcms").selectedIndex].text);
		return true;
	}			

	/*Evento executado no momento em que o usuario seleciona o checkbox subContratacao*/
	function blSubcontratacaoOnClick(element){
		setCheckboxCommonValue(element);
		validateBlSubcontratacao(element);		
	}
	
	/*Habilita ou desabilia obriga CTRC verificando o checkbox subContratacao*/
	function validateBlSubcontratacao(element){
		if (element.checked) {
			getElement("blObrigaCtrcSubContratante").checked = true;
			getElement("blObrigaCtrcSubContratante").disabled = false;
		} else {
			getElement("blObrigaCtrcSubContratante").checked = false;
			getElement("blObrigaCtrcSubContratante").disabled = true;
		}
	}
	
	/*Se o obriga CTRC sub contratante estiver checado obriga o preenchimento do regime especial*/
	function validateBlObrigaCtrcSubContratante() {
		if (!getElement("blObrigaCtrcSubContratante").checked) {
			getElement("dsRegimeEspecial").required = true;
		} else {
			getElement("dsRegimeEspecial").required = false;
		}
	}
	
	/*Salva os dados da tela */
	function myStoreButton() {
		validateBlObrigaCtrcSubContratante();	
		storeButtonScript('lms.tributos.manterExcecaoICMSClienteAction.store', 'store', document.forms[0]);
	}
	
	/*Verifica cnpj utilizado */
	function onChangeComboTipoCnpj(elem){
		comboboxChange({e:elem});
		setMaskCnpj(elem.options[elem.selectedIndex].value);		
		setElementValue("nrCNPJParcialDev", "");
		setElementValue("nmDevedor", "");
		if(elem.selectedIndex == 0){
            document.getElementById("nrCNPJParcialDev").disabled = true;        
		}else{
            document.getElementById("nrCNPJParcialDev").disabled = false;
		}
	}
	
	/*Modifica a mascara do cnpj de acordo com o tipo de cnpj.*/ 
	function setMaskCnpj(tpCnpj) {
		if (tpCnpj == "P") {
			getElement("nrCNPJParcialDev").mask = "00000000";
			getElement("nrCNPJParcialDev").maxLength = 8;
		} else {
			getElement("nrCNPJParcialDev").mask = "00000000000000";
			getElement("nrCNPJParcialDev").maxLength = 14;
		}
	}
	
	/*Carrega os dados passsaos pela funcao findById*/	
	function myOnDataLoad_cb(data, error) {
		
		onDataLoad_cb(data, error);

		if(data.blObrigaCtrcSubContrataOsnte != undefined && data.blObrigaCtrcSubContrataOsnte == "true"){
			getElement("blObrigaCtrcSubContratante").checked = true;
		}else{
			getElement("blObrigaCtrcSubContratante").checked = false;
		}
		
		if (data.nrCNPJParcialDev.length == 14) {
			setMaskCnpj("C");
			setElementValue("tipoCnpj", "C");
		} else {
			setMaskCnpj("P");
			setElementValue("tipoCnpj", "P");
		}	

		validaVigencia(data);		
	}

	/*Valida a vigencia inicial e final da excecao*/
	function validaVigencia(data){

		if(data.comparaFinal != undefined && data.comparaFinal < 0){
			desabilitaTodosCampos();
			setDisabled("btnSalvar",  true);
			setDisabled("btnExcluir", true);	
			setDisabled("naturezaProduto", true);			
		}else{

			if(data.comparaInicial > 0){
				desabilitaTodosCampos(false);
				setDisabled("unidadeFederativa.idUnidadeFederativa",true);			
				setDisabled("tpFrete",true);		
				setDisabled("nrCNPJParcialDev",true);
				setDisabled("naturezaProduto", false);
				setDisabled("tipoCnpj",true);				
			}else{
				desabilitaTodosCampos();
				setDisabled("dtVigenciaFinal", false);				
				setDisabled("btnExcluir", true);
				setDisabled("naturezaProduto", true);
				setFocus("dtVigenciaFinal");					
			}
		}		
	}
	
</script>