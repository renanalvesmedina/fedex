<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterEmbasamentoLegalICMSAction">

	<adsm:i18nLabels>
		<adsm:include key="LMS-23034" />
	</adsm:i18nLabels>
	
	<adsm:form action="/tributos/manterEmbasamentoLegalICMS"  idProperty="idEmbasamento" onDataLoadCallBack="onDataLoad">
		
		<adsm:hidden property="possueAliquotas" />		
	
	    <adsm:combobox
	     service="lms.tributos.manterEmbasamentoLegalICMSAction.findUnidadeFederativa"
	     property="unidadeFederativaOrigem.idUnidadeFederativa"
	     optionProperty="idUnidadeFederativa"
	     optionLabelProperty="siglaDescricao" 
	     labelWidth="20%" 
	     width="39%" 	        
	     boxWidth="170"
	     label="ufOrigem" required="true"/>
	
	    <adsm:combobox 
	     service="lms.tributos.manterAliquotasICMSAction.findTipoTributacaoIcms" 
	     property="tipoTributacaoIcms.idTipoTributacaoIcms" 
	     optionLabelProperty="dsTipoTributacaoIcms" 
	     optionProperty="idTipoTributacaoIcms" 
	     label="tipoTributacao"
	     labelWidth="20%" 
	     width="20%"
	     boxWidth="138" required="true"/>
	
	
		<adsm:textarea label="embLegalCompleto" maxLength="500" required="true" onchange="fillEmbRes();"
			property="dsEmbLegalCompleto" labelWidth="20%" width="98%" rows="3" columns="100" />

		<adsm:textarea label="embLegalResumido" maxLength="500" required="true"
			property="dsEmbLegalResumido" labelWidth="20%" width="98%" rows="3" columns="100" />
			
		<adsm:textbox dataType="text" label="cdEmbLegalMasterSaf" disabled="true"
			property="cdEmbLegalMasterSaf" size="30" maxLength="10" labelWidth="20%" width="80%" />			
	
		<adsm:textarea label="observacao" maxLength="500" 
			property="obEmbLegalIcms" labelWidth="20%" width="98%" rows="5" columns="100" />
		
		<adsm:buttonBar >
			<adsm:button 	   id="btnSalvar" caption="salvar" onclick="salvarEmbasamento();" buttonType="storeButton" disabled="false"/>
			<adsm:button  	   id="btnLimpar" caption="limpar"  buttonType="resetButton" onclick="limparEmbasamento(this);" disabled="false" />
			<adsm:removeButton id="btnExcluir"/>		
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script type="text/javascript">


	function initWindow(eventObj){
		if (eventObj.name == "tab_click"){
			disableAll(false);								
		}
		setDisabled("cdEmbLegalMasterSaf", true);
	}

	function limparEmbasamento(elem){
		cleanButtonScript(elem.document);
		disableAll(false);
	}

	function salvarEmbasamento(){
		findParametroEmbasamento("afterFindParameter");	
	}

	function afterFindParameter_cb(data, error){
		if(error != undefined){
			alert(error);
			return false;
		}

		var dsEmbLegalResumido 	 = getElementValue("dsEmbLegalResumido");
		var nrTamanhoEmbLegalRes = data.paramSize 
		//alert("dsEmbLegalResumido.length: "+dsEmbLegalResumido.length+" nrTamanhoEmbLegalRes: "+nrTamanhoEmbLegalRes);
		var tamEmbPermitido = nrTamanhoEmbLegalRes - 1
		if(dsEmbLegalResumido.length > tamEmbPermitido){			
			alertI18nMessage("LMS-23034", new Array(tamEmbPermitido, dsEmbLegalResumido.length), false);
		}else{
			storeButtonScript('lms.tributos.manterEmbasamentoLegalICMSAction.store', 'afterStore', document.forms[0]);				
		}		
									
	}
	
	function afterStore_cb(data, error){
		store_cb(data, error);
		
		if(data.cdEmbasamento != undefined){
			setElementValue("cdEmbLegalMasterSaf",data.cdEmbasamento);
		}
		setElementValue("idEmbasamento",data.idEmbasamento);
	}
	
	function onDataLoad_cb(data, error){
		
		if (error != undefined) {
			alert(error);
			return false;
		}
		
		var idEmbasamento 		= data.idEmbasamento; 
		var dsEmbLegalResumido 	= data.dsEmbLegalResumido; 
		var dsEmbLegalCompleto 	= data.dsEmbLegalCompleto; 
		var cdEmbLegalMasterSaf = data.cdEmbLegalMasterSaf;
		var obEmbLegalIcms 		= data.obEmbLegalIcms;
						
		var tipoTributacaoIcms       = data.tipoTributacaoIcms;
		var unidadeFederativaOrigem  = data.unidadeFederativaOrigem;

		if(idEmbasamento != undefined){
			setElementValue("idEmbasamento", idEmbasamento);
		}
		
		if(unidadeFederativaOrigem != undefined){
			setElementValue("unidadeFederativaOrigem.idUnidadeFederativa", unidadeFederativaOrigem.idUnidadeFederativa);
		}
		
		if(tipoTributacaoIcms != undefined){
			setElementValue("tipoTributacaoIcms.idTipoTributacaoIcms", tipoTributacaoIcms.idTipoTributacaoIcms);
		}
		
		if(dsEmbLegalResumido != undefined){
			setElementValue("dsEmbLegalResumido", dsEmbLegalResumido);
		}
		
		if(dsEmbLegalCompleto != undefined){
			setElementValue("dsEmbLegalCompleto", dsEmbLegalCompleto);
		}
		
		if(cdEmbLegalMasterSaf != undefined){
			setElementValue("cdEmbLegalMasterSaf", cdEmbLegalMasterSaf);
		}

		if(obEmbLegalIcms != undefined){
			setElementValue("obEmbLegalIcms", obEmbLegalIcms);
		}

		if(data.possueAliquotas){
			setElementValue("possueAliquotas", "true");
			disableAll(true);						
		}else{
			disableAll(false);
			setDisabled("unidadeFederativaOrigem.idUnidadeFederativa", true);
			setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms", true);
			setElementValue("possueAliquotas", "false");
		}

		setDisabled("cdEmbLegalMasterSaf", true);
	}


	function limpar(){

		resetValue("idEmbasamentoLegaIcms");
		resetValue('unidadeFederativaOrigem.idUnidadeFederativa');
		resetValue('tipoTributacaoIcms.idTipoTributacaoIcms');
		resetValue('embLegalCompleto');
		resetValue('embLegalResumido');
		resetValue('observacao');		
	}

	function disableAll(condicao){

		setDisabled("unidadeFederativaOrigem.idUnidadeFederativa", condicao);
		setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms", condicao);
		setDisabled("dsEmbLegalCompleto", condicao);
		setDisabled("dsEmbLegalResumido", condicao);		
		setDisabled("obEmbLegalIcms", condicao);
		setDisabled("btnSalvar", condicao);
		setDisabled("btnExcluir", condicao);
	}

	function findParametroEmbasamento(callback){
		var data = new Array();	
		var service = "lms.tributos.manterEmbasamentoLegalICMSAction.findParameterSizeEmb";
		var sdo = createServiceDataObject(service, callback, data);
		xmit({serviceDataObjects:[sdo]});		
	}

	function fillEmbRes(){
		findParametroEmbasamento("fillEmbRes");
	}

	function fillEmbRes_cb(data, error){

		if(error != undefined){
			alert(error);
			return false;
		}
		
		var embResumido 	= getElementValue("dsEmbLegalResumido"); 
		var embCompleto 	= getElementValue("dsEmbLegalCompleto");
		
		var tamEmbasamento 	= data.paramSize

		if(embResumido == "" && embCompleto != ""){
			setElementValue("dsEmbLegalResumido", embCompleto);			
			if(embCompleto.length < tamEmbasamento){
				setDisabled("dsEmbLegalResumido", true);
				setFocus("obEmbLegalIcms", false);
			}else{
				setFocus("dsEmbLegalResumido", false);
			}
		}			
	}

</script>
