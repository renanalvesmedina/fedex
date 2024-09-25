<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window 
	service="lms.tributos.manterAliquotasICMSAction" onPageLoadCallBack="myOnPageLoadCallBack"
>
	<adsm:form 
		action="/tributos/manterAliquotasICMS"
		idProperty="idAliquotaIcms" onDataLoadCallBack="onDataLoadRecord"
	>
	
        <adsm:combobox
	        service="lms.tributos.manterAliquotasICMSAction.findUnidadeFederativaPaisLogado"
	        property="unidadeFederativaOrigem.idUnidadeFederativa"
	        optionProperty="idUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
	        labelWidth="24%" 
	        width="27%" 
	        boxWidth="120"
	        label="ufOrigem"
	        required="true"
	        onlyActiveValues="true"
        />

		<adsm:combobox
	        service="lms.tributos.manterAliquotasICMSAction.findUnidadeFederativaPaisLogado"
	        property="unidadeFederativaDestino.idUnidadeFederativa"
	        optionProperty="idUnidadeFederativa"
	        optionLabelProperty="siglaDescricao" 
			labelWidth="15%" 
			width="20%" 
	        boxWidth="120"
			label="ufDestino" 
	        onlyActiveValues="true"
		/>

		<adsm:combobox 
			property="regiaoGeografica.idRegiaoGeografica" 
			label="regiaoGeograficaDestino"
			service="lms.municipios.regiaoGeograficaService.find" 
			optionLabelProperty="dsRegiaoGeografica" 
			optionProperty="idRegiaoGeografica" 
			labelWidth="24%" width="80%" boxWidth="280" onlyActiveValues="false"/>

		<adsm:combobox 
			property="tpSituacaoTribRemetente" 
			label="situacaoTributariaRemetente"  
			domain="DM_SITUACAO_TRIBUTARIA"
			labelWidth="24%" 
			width="74%" 
			boxWidth="280"
	        required="true"
		/>
		
		<adsm:combobox
			property="tpSituacaoTribDestinatario"
			label="situacaoTributariaDestinatario" 
			domain="DM_SITUACAO_TRIBUTARIA"
			labelWidth="24%" 
			width="74%"
			boxWidth="280"
	        required="true"
		/>
		
		<adsm:combobox
			property="tpTipoFrete"
			label="tipoFrete"
			domain="DM_TIPO_FRETE"
			labelWidth="24%"
			width="27%"
	        required="true"
		/>
        
        <adsm:combobox 
	        service="lms.tributos.manterAliquotasICMSAction.findTipoTributacaoIcms" 
	        property="tipoTributacaoIcms.idTipoTributacaoIcms" 
	        optionLabelProperty="dsTipoTributacaoIcms" 
	        optionProperty="idTipoTributacaoIcms" 
	        label="tipoTributacao"
	        labelWidth="15%" 
	        width="20%" 
	        onlyActiveValues="true"
	        required="true"
	        onchange="tipoTributacaoIcms_onchange(this)"
	        boxWidth="120" 
	    >
	    
	    	<adsm:propertyMapping
	    		relatedProperty="tipoTributacaoIcms.dsTipoTributacaoIcms"
	    		modelProperty="dsTipoTributacaoIcms"
	    	/>
	    </adsm:combobox>
        
        <adsm:hidden property="tipoTributacaoIcms.dsTipoTributacaoIcms" serializable="false"/>

		<adsm:lookup   
			action="/tributos/manterEmbasamentoLegalICMS" 
			criteriaProperty="unidadeFederativaOrigem.sgUnidadeFederativa"			
			dataType="text" 
			exactMatch="true"
			idProperty="idEmbasamento" 
			label="embasamento" 
			property="embasamento"
			service="lms.tributos.manterEmbasamentoLegalICMSAction.findEmbasamentoLookup" 
			size="12" 
			labelWidth="24%" 
			width="15%" 
			minLengthForAutoPopUpSearch="3" 
			onclickPicker="validateCriteria();" >			

			<adsm:propertyMapping
				relatedProperty="embasamento.dsEmbLegalCompleto"
				modelProperty="dsEmbLegalCompleto" />

		<adsm:textbox 
				dataType="text"
				property="embasamento.dsEmbLegalCompleto"
				width="60%" size="61"
				maxLength="50"
				disabled="true"
				serializable="false"/>

			<adsm:propertyMapping
				relatedProperty="embasamento.unidadeFederativaOrigem.idUnidadeFederativa"
				modelProperty="unidadeFederativaOrigem.idUnidadeFederativa" />

			<adsm:hidden serializable="true"
				property="embasamento.unidadeFederativaOrigem.idUnidadeFederativa"/>

	    </adsm:lookup>	

		<adsm:textbox 
			size="6" 
			labelWidth="24%" 
			width="27%" 
			maxLength="6" 
			dataType="decimal" 
			property="pcAliquota" 
			label="percentualAliquota" 
			required="true"
			minValue="0"
			maxValue="20"
			disabled="true"
			mask="###,##0.00"
			onchange="pcAliquota_onchange()"
		/>
		
		<adsm:textbox 
			size="6"
			labelWidth="15%" 
			width="20%" 
			dataType="decimal" 
			property="pcEmbute" 
			label="percentualEmbutimento" 
			disabled="true"
			required="true"
			mask="###,##0.00"		
		/>
		
		<adsm:textarea label="observacao" maxLength="500" 
			property="obAliquota" labelWidth="24%" width="60%" rows="5" columns="84" />

		<adsm:range 
			label="vigencia" 
			labelWidth="24%" >

	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigenciaInicial"
	        	smallerThan="dtVigenciaFinal"
				required="true" />

	    	<adsm:textbox 
	    		biggerThan="dtVigenciaInicial"
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal"/>

        </adsm:range>


		<adsm:buttonBar>
			<adsm:button 	  id="btnStore" caption="salvar" onclick="salvarAliquota();" buttonType="storeButton" disabled="false"/>						
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
			<adsm:removeButton id="btnExcluir"/>
		</adsm:buttonBar>


	<adsm:i18nLabels>
		<adsm:include key="LMS-23001"/>
	</adsm:i18nLabels>

	</adsm:form>
</adsm:window>

<script type="text/javascript"><!--

var idTipoTributacaoNormal;
var idTipoTributacaoST;

	/*Funcao utilizada pelo botao Slavar*/
	function salvarAliquota(){
		/*Valida campos obrigatórios*/
		var form = document.forms[0];		
		if (!validateForm(form)) {
			return false;
		}
		
		var dados = buildFormBeanFromForm(document.forms[0]);				
		_serviceDataObjects = new Array();
        addServiceDataObject(createServiceDataObject("lms.tributos.manterAliquotasICMSAction.store","afterStore",dados));
    	xmit(false);	
	}

	/*Callback*/
	function afterStore_cb(data,error){
		if(error != undefined){
			alert(error);
			return false;
		}
		setElementValue("idAliquotaIcms",data.idAliquotaIcms);
		store_cb(data,error);			
	}

	/*Necessario preencher a unidade federativa para utilizar a lookup embasamento*/
	function validateCriteria() {	
		if(!hasValue(getElementValue("unidadeFederativaOrigem.idUnidadeFederativa"))) {
			alert("Preencher a unidade federativa de origem!");
			return false;
		}		
		lookupClickPicker({e:document.forms[0].elements['embasamento.idEmbasamento']});
	}

	/*Callback*/
	function onDataLoadRecord_cb(data, error){

		if(error != undefined){
			alert(error);
			return false;
		}

		var objAliquota = getElement("pcAliquota");
		setElementValue(objAliquota, setFormat(objAliquota, data.pcAliquota));
		
		var objEmbute = getElement("pcEmbute");
		setElementValue(objEmbute, setFormat(objEmbute, data.pcEmbutimento));
		
		
		setElementValue("idAliquotaIcms",data.idAliquotaIcms);
		setElementValue("unidadeFederativaOrigem.idUnidadeFederativa",data.ufOrigem);
		setElementValue("unidadeFederativaDestino.idUnidadeFederativa",data.ufDestino);
		setElementValue("regiaoGeografica.idRegiaoGeografica",data.idRegiaoGeografica);
		setElementValue("tpSituacaoTribRemetente",data.idStRemetente);
		setElementValue("tpSituacaoTribDestinatario",data.idStDestinatario);
		setElementValue("tpTipoFrete",data.tpFrete);
		setElementValue("tipoTributacaoIcms.idTipoTributacaoIcms",data.tpSitTributaria);
		setElementValue("embasamento.idEmbasamento",data.idEmbasamento);
		setElementValue("embasamento.unidadeFederativaOrigem.sgUnidadeFederativa",data.sgUFEmbasamento);
		setElementValue("embasamento.unidadeFederativaOrigem.idUnidadeFederativa",data.idUFEmbasamento);		
		setElementValue("embasamento.dsEmbLegalCompleto",data.dsEmbLegalComp);
		setElementValue("obAliquota",data.obAliquota);
		setElementValue("dtVigenciaInicial", data.dtVigInicial);
		setElementValue("dtVigenciaFinal",data.dtVigenciaFinal);
	
		if(data.dtVigenciaFinal != undefined && validaDataFinal(data.dtVigenciaFinal)){
			disableFields(true);
			setDisabled("dtVigenciaFinal",true);
			setDisabled("btnStore",true);
			setDisabled("btnExcluir",true);			
		}else{
			if(data.vigenciaInicialMaior != undefined){				
				disableFields(false);
				setDisabled("unidadeFederativaOrigem.idUnidadeFederativa",true);
				setDisabled("unidadeFederativaDestino.idUnidadeFederativa",true);
				setDisabled("regiaoGeografica.idRegiaoGeografica",true);
				setDisabled("tpSituacaoTribRemetente",true);
				setDisabled("tpSituacaoTribDestinatario",true);
				setDisabled("tpTipoFrete",true);
				setDisabled("btnExcluir",false);													
			}else{
				disableFields(true);
				setDisabled("dtVigenciaFinal",false);				
				setDisabled("btnLimpar",false);
				setDisabled("btnExcluir",true);	
				setDisabled("btnStore",false);
			}
		}		
	}

	function validaDataFinal(dataFinal){
		
		var dataAtual = new Date();
		var data = stringToDate(dataFinal,"dd/MM/yyyy");

		var myDate = new Date();
		myDate.setFullYear(data.getYear(),data.getMonth(),data.getDate());
		
		return myDate < dataAtual;		
	}
	
/* Buscando os dados para fazer o controle da combo de tipo de tributacao */
	function  myOnPageLoadCallBack_cb(){
		var dados = new Array();
			_serviceDataObjects = new Array();
            addServiceDataObject(createServiceDataObject("lms.tributos.manterAliquotasICMSAction.findInitialValue","retorno",dados));
        	xmit(false);
	}
	
	/*Retorno do xmit para controle de tipo de tributacao*/ 	
	function retorno_cb(data, erro, errorCode){
		if( erro != undefined ){
			alert(erro);
			return false;		
		}
		if (  data != null ){
			data = data[0];
			idTipoTributacaoNormal = data.idTipoTributacaoNormal;
			idTipoTributacaoST = data.idTipoTributacaoST;
		}
	}

	/*Init Window*/
	function initWindow(eventObj){
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();						
		}
		//desabilita os campos da tela quando vier da grid ou depois de salvar
		if (eventObj.name == "storeButton"  ){
			
			var  dataFinal = getElementValue("dtVigenciaFinal");
			if(dataFinal != "" ){
				disableFields(true);
			}else{
				disableFields(false);
				setDisabled("unidadeFederativaOrigem.idUnidadeFederativa",true);
				setDisabled("unidadeFederativaDestino.idUnidadeFederativa",true);
				setDisabled("regiaoGeografica.idRegiaoGeografica",true);
				setDisabled("tpSituacaoTribRemetente",true);
				setDisabled("tpSituacaoTribDestinatario",true);
				setDisabled("tpTipoFrete",true);
				setDisabled("btnExcluir",false);
		}
		}
		
		setDisabled('btnLimpar', false);
		
	}
	
	/*Busca  a data atual para ser utilizada no formulario*/
	function buscaDataAtual(){
		var dados = new Array();
		_serviceDataObjects = new Array();
        addServiceDataObject(createServiceDataObject("lms.tributos.manterAliquotasICMSAction.findDataAtual",
                                                    "retornoFindDataAtual",
                                                    dados));
       	xmit(false);
	}
	
	/*Callback*/
	function retornoFindDataAtual_cb(data,error){
		if( error != undefined ){
			alert(error);
			setFocusOnFirstFocusableField();
			return false;		
		}
		fillFormWithFormBeanData(0, data);		
		setDisabled('dtVigenciaInicial',false);
	}

	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}else if (val == false){
			setElementValue("pcAliquota", "0,00");
			setElementValue("pcEmbute", "0,00");
		}
		disableFields(val);	

		//esses campos possuem regras específicas,sendo assim ficam sempre desabilitados
		setDisabled("pcAliquota",true);
		setDisabled("pcEmbute",true);					
	}

	function disableFields(val){
		
		setDisabled("unidadeFederativaOrigem.idUnidadeFederativa",val);
		setDisabled("unidadeFederativaDestino.idUnidadeFederativa",val);
		setDisabled("tpSituacaoTribRemetente",val);	
		setDisabled("tpSituacaoTribDestinatario",val);
		setDisabled("tpTipoFrete",val);
		setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms",val);
		setDisabled("tpTipoFrete",val);
		setDisabled("dtVigenciaInicial",val);		
		setDisabled("dtVigenciaFinal",val);		
		setDisabled("embasamento.idEmbasamento",val);
		setDisabled("regiaoGeografica.idRegiaoGeografica",val);
		setDisabled("obAliquota",val);								
		setDisabled("pcAliquota",val);
		setDisabled("pcEmbute",val);
		setFocusOnFirstFocusableField();
	}

	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		
		resetValue('unidadeFederativaOrigem.idUnidadeFederativa');
		resetValue('unidadeFederativaDestino.idUnidadeFederativa');
		resetValue('regiaoGeografica.idRegiaoGeografica');		
		resetValue('embasamento.idEmbasamento');		
		resetValue('tpSituacaoTribRemetente');
		resetValue('tpSituacaoTribDestinatario');
		resetValue('tpTipoFrete');
		resetValue('tipoTributacaoIcms.idTipoTributacaoIcms');
		resetValue('tipoTributacaoIcms.dsTipoTributacaoIcms');
		resetValue('dtVigenciaInicial');
		resetValue('dtVigenciaFinal');		
		resetValue('idAliquotaIcms');
		resetValue('obAliquota');
		
		setElementValue("pcAliquota", "0,00");
		setElementValue("pcEmbute", "0,00");		
				
		desabilitaTodosCampos(false);
		buscaDataAtual();	
		setDisabled('btnExcluir',true);
		setDisabled('btnStore',false);
		
	}

	/*Arredonda numero - utilizado para o pcEmbute */
	function roundNumber(numero, precisao) {
		return Math.round(numero * Math.pow(10,precisao)) / Math.pow(10,precisao);
	}

	
	/*Onchange tipo tributacao*/
	function tipoTributacaoIcms_onchange(combo){
		comboboxChange({e:combo});
		var tpTributacao = getElementValue("tipoTributacaoIcms.idTipoTributacaoIcms");

		if (tpTributacao != idTipoTributacaoNormal && tpTributacao != idTipoTributacaoST ){
			setElementValue("pcAliquota", "0,00");
			setElementValue("pcEmbute", "0,00");
			setDisabled("pcAliquota",true);
		} else {
		    setDisabled("pcAliquota",false);
		}
	}

	/*Onchange percentual aliquota*/
	function pcAliquota_onchange(){
		var tpTributacao = getElementValue("tipoTributacaoIcms.idTipoTributacaoIcms");
		var pcAliquota = getElementValue("pcAliquota");

		if (pcAliquota != "" && pcAliquota > 0){
		
			if (tpTributacao == idTipoTributacaoNormal || tpTributacao == idTipoTributacaoST ){
				var pcEmbute = ((100/(100 - pcAliquota))-1)*100;
				pcEmbute = roundNumber(pcEmbute, 2);
				pcEmbute += "";
				pcEmbute = pcEmbute.replace(".", ",");
				setElementValue("pcEmbute", pcEmbute);
			}else{
				alert(i18NLabel.getLabel("LMS-23001"));
				setElementValue("pcAliquota", "");
				setElementValue("pcEmbute", "0,00");
			}
		}else
			setElementValue("pcEmbute", "0,00");
	}
--></script>
