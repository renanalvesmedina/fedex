<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.tributos.manterAliquotasImpostosGeraisAction">
	<adsm:form 
		action="/tributos/manterAliquotasImpostosGerais"
		idProperty="idAliquotaContribuico" onDataLoadCallBack="myOnDataLoadCallBack">
	
		<adsm:i18nLabels>
			<adsm:include key="LMS-27044" />
			<adsm:include key="LMS-23006"/>
		</adsm:i18nLabels>
		<adsm:hidden property="tpSituacaoAtivo" value="A" serializable="false"/>

		<adsm:combobox 
			property="tpImposto" 
			label="tipoImposto" 
			domain="DM_TIPO_IMPOSTO" 
			required="true"
			labelWidth="18%" 
            width="27%"
            onchange="onTpImpostoChange(this);" 
		/>


		<adsm:range 
			label="vigencia" labelWidth="12%" width="28%"
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigenciaInicial"
	        	smallerThan="dtVigenciaFinal"
				required="true"	        	
	        />
	    	<adsm:textbox 
	    		biggerThan="dtVigenciaInicial"
	    		dataType="JTDate" 
	    		property="dtVigenciaFinal"
	    	/>
        </adsm:range>

		<adsm:textbox 
			property="vlPiso"
			label="valorPiso"
			dataType="currency" 
			minValue="0"
			required="true"
			size="22"
			labelWidth="18%" 
            width="27%" 			
		/>
		
		<adsm:textbox 
			property="pcAliquota"
			label="percentualAliquota" 
			dataType="percent" 
			size="5"
			maxLength="5"
			required="true"	
			minValue="0"
			maxValue="100"
			labelWidth="12%" width="28%"
		/>

        <adsm:textbox 
                  dataType="percent" 
                  property="pcBaseCalcReduzida" 
                  label="percentualBaseCalcReduzida" 
                  minValue="0" 
                  maxValue="100" 
                  mask="##0.00" 
                  size="10" 
                  maxLength="5" 
                  labelWidth="18%" 
                  width="82%" 
                  required="true"
        />
		<adsm:combobox 
			label="servicoAdicional" 
			property="servicoAdicional.idServicoAdicional" 
			optionLabelProperty="dsServicoAdicional" 
			optionProperty="idServicoAdicional" 
			service="lms.tributos.manterAliquotasImpostosGeraisAction.findServicoAdicionalVigente"
			boxWidth="400"
			labelWidth="18%" width="82%"
			
		/>
		
		<adsm:combobox 
			label="outroServico" 
			property="servicoTributo.idServicoTributo" 
			optionLabelProperty="dsServicoTributo" 
			optionProperty="idServicoTributo" 
			service="lms.tributos.manterAliquotasImpostosGeraisAction.findServicoTributoVigente" 
			boxWidth="400"
			labelWidth="18%" 
            width="82%" 
		/>

		<adsm:lookup size="20" maxLength="20" labelWidth="18%" width="82%"
			idProperty="idFacade" 
			property="facade"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="nrIdentificacaoFormatado"
			action="/configuracoes/manterPessoas"
			service="lms.tributos.manterAliquotasImpostosGeraisAction.findLookupPessoa"
			dataType="text" label="pessoa"
			afterPopupSetValue="verificaPessoaPopup"
			onDataLoadCallBack="verificaPessoa">
			<adsm:propertyMapping relatedProperty="pessoa.nmPessoa" modelProperty="nmPessoa" />
			<adsm:propertyMapping relatedProperty="pessoa.idPessoa" modelProperty="idPessoa" />
			<adsm:textbox size="30" maxLength="30" disabled="true" dataType="text" property="pessoa.nmPessoa" />
		</adsm:lookup>
		<adsm:hidden property="pessoa.idPessoa" serializable="true"/>

		<adsm:textarea 
	        label="observacao" 
	        columns="60" 
	        rows="3" 
	        maxLength="500" 
	        property="obAliquotaContribuicaoServ" 
	        labelWidth="18%" 
            width="80%" 
        />
        
        
        <adsm:listbox label="municipios" 
					  property="aliquotasContribuicaoServMunic" 
					  optionProperty="idAliquotaContribuicaoServMunic"
					  optionLabelProperty="nmMunicipio"
					  size="05" 
					  labelWidth="18%"  					  
					  boxWidth="198">		

				<adsm:lookup 
					 property="municipio" 
					 idProperty="idMunicipio"
					 criteriaProperty="nmMunicipio"					 
					 service="lms.tributos.manterAliquotasImpostosGeraisAction.findMunicipiosLookup"
					 dataType="text" 
					 size="35" 
					 maxLength="60"
					 action="/municipios/manterMunicipios"
					 exactMatch="false"
					 minLengthForAutoPopUpSearch="3"/>	
		</adsm:listbox>	
        

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>

<script type="text/javascript">
	function alert_LMS_27044(){
		alert(i18NLabel.getLabel('LMS-27044'));
	}
</script>
		
	</adsm:form>
</adsm:window>


<script type="text/javascript">
/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript();
		desabilitaTodosCampos(false);
		
	}
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}

		setDisabled("tpImposto",val);
		setDisabled("dtVigenciaInicial",val);
		setDisabled("vlPiso",val);
		setDisabled("pcAliquota",val);
		setDisabled("pcBaseCalcReduzida",val);
		setDisabled("servicoAdicional.idServicoAdicional",val);
		setDisabled("servicoTributo.idServicoTributo",val);
		setDisabled("facade.pessoa.nrIdentificacao",val);
		setDisabled("facade.idFacade",val);		
		setDisabled("obAliquotaContribuicaoServ",val);

		//ficam sempre desabilitados
		setDisabled("pessoa.nmPessoa",true);

		setFocusOnFirstFocusableField();				
	}	
	
	/**
		Chamado ao iniciar a tela
	*/
	function initWindow(eventObj){
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
		}
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton"  ){
			desabilitaTodosCampos();
		}
		
		habilitarAliquotasContribuicaoServicoMunicipio(false);
		setDisabled('btnLimpar', false);
		
		if(eventObj.name == "storeButton"){
			setFocus('btnLimpar',true,true);
		} else {
			setFocusOnFirstFocusableField();
		}
		
	}



	function validateTab(){
		return validateTabScript(document.forms) && validateServicos();
	}

	function validateServicos(){
		var idServicoAdicional = getElementValue("servicoAdicional.idServicoAdicional");
		var idServicoTributo = getElementValue("servicoTributo.idServicoTributo");

		if ( idServicoAdicional == '' && idServicoTributo == '' ){
			setFocusOnFirstFocusableField();				
			alert_LMS_27044();
			return false;
		}
		
		return true;
	}
	
	function verificaPessoaPopup(data){

		if( data != undefined ){
			if( data.tpPessoa.value != 'J' ){
				alert(i18NLabel.getLabel('LMS-23006'));
				resetValue('facade.idFacade');
				setFocus('facade.pessoa.nrIdentificacao');
				return false;
			}
		}
		
	}
	
	function verificaPessoa_cb(data,error){
		
		var retorno = facade_pessoa_nrIdentificacao_exactMatch_cb(data);
		
		if( retorno == true ){
			verificaPessoaPopup(data[0]);
		}		
		
		return retorno;
		
	}
	
	function myOnDataLoadCallBack_cb(data,error){
		onDataLoad_cb(data,error);
		
		if( error == undefined && data != undefined ){		
			if( data.pessoa != undefined ){
				setElementValue('facade.pessoa.nrIdentificacao',data.pessoa.nrIdentificacaoFormatado);		
			}
		}	
		
		setFocusOnFirstFocusableField();				
		
	}
	
	
	function onTpImpostoChange(e){	
		if (e[e.selectedIndex].value=='IS') {
			habilitarAliquotasContribuicaoServicoMunicipio(true);
		} else {
			habilitarAliquotasContribuicaoServicoMunicipio(false);
			resetValue('aliquotasContribuicaoServMunic');
			resetValue('aliquotasContribuicaoServMunic_municipio.nmMunicipio');
		}
	}
	
	function habilitarAliquotasContribuicaoServicoMunicipio(opcao){
		if(opcao == true){
			setDisabled('aliquotasContribuicaoServMunic', false);
			setDisabled('aliquotasContribuicaoServMunic_municipio.idMunicipio', false);
		} else {
			setDisabled('aliquotasContribuicaoServMunic', true);
			setDisabled('aliquotasContribuicaoServMunic_municipio.idMunicipio', true);
		}
	}
</script>
