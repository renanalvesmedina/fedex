<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.manterValorAction" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-01185"/>
		<adsm:include key="LMS-01186"/>
		<adsm:include key="LMS-01190"/>
		<adsm:include key="LMS-01187"/>
		<adsm:include key="LMS-01188"/>		
		<adsm:include key="LMS-01191"/>		
	</adsm:i18nLabels>
	<adsm:form action="/vendas/manterValorCpt" id="formValorCpt" idProperty="idCpt">

		<adsm:hidden property="codTpValor"/>

		<adsm:lookup 
			action="/vendas/manterDadosIdentificacao" 
			dataType="text"
			criteriaProperty="pessoa.nrIdentificacao" 
			exactMatch="true" 
			idProperty="idCliente" 
			property="cliente" 
			label="cliente" 
			size="20"
			maxLength="20"
			width="60%" 
			labelWidth="20%"
			afterPopupSetValue="resetFieldsByPessoa();"
			onchange="return onChangeClienteValue();"
			service="lms.vendas.manterPropostasClienteAction.findClienteLookup">

			<adsm:propertyMapping 
				modelProperty="pessoa.nmPessoa" 
				relatedProperty="cliente.pessoa.nmPessoa" />

			<adsm:textbox 
				dataType="text" 
				disabled="true" 
				serializable="false"
				property="cliente.pessoa.nmPessoa" 
				size="50" />
		</adsm:lookup>


		<adsm:combobox
			property="segmentoMercado"
			onlyActiveValues="true"
			optionLabelProperty="dsSegmentoMercado"
			optionProperty="idSegmentoMercado"
			service="lms.vendas.manterClienteAction.findSegmentoMercado"
			label="segmentoMercado"
			labelWidth="20%"
			width="60%"
			boxWidth="415"
			autoLoad="false"
			renderOptions="true" 
			onchange="resetFieldsBySegmento();"/>

		<adsm:combobox 
			property="cptTipoValor" 
			label="tipoValor" 
			onlyActiveValues="true"
			optionLabelProperty="dsTipoValor" 
			optionProperty="idCptTipoValor" 
			labelWidth="20%"
			service="lms.vendas.manterTiposValorAction.findTiposValor" 
			width="60%" 			
			boxWidth="415"
			onchange="validateTipoValor();"/>

		<adsm:textbox size="12" width="60%"  maxLength="6" dataType="decimal" property="valor" disabled="true"
			label="vlComplexidade" minLength="0" mask="##0.00" labelWidth="20%" minValue="0" />

		<adsm:textbox size="12" width="60%"  maxLength="6" dataType="decimal" property="percentual" disabled="true"
			label="percentual" minLength="0" mask="##0.00" labelWidth="20%" maxValue="100" minValue="0" />

		<adsm:combobox 
			property="cptComplexidade" 
			label="cptComplexidade" 
			onlyActiveValues="true"
			optionLabelProperty="descricao" 
			optionProperty="idCptComplexidade" 
			service="lms.vendas.manterTiposComplexidadeAction.findTiposComplexidade" 
			width="60%"
			disabled="true"
			labelWidth="20%" 
			boxWidth="200" />

		<adsm:textbox 
			property="nrFrota" 
			dataType="text"  
			label="frota"
			maxLength="6" size="6" 
			labelWidth="20%" 
			width="60%" disabled="true"
			cellStyle="vertical-align:bottom;" 
			onchange="validateNrFrota(this);">
				
				<adsm:textbox 
					property="nrPlaca"
					dataType="text"  disabled="true"
					maxLength="25" size="25" 
					cellStyle="vertical-align:bottom;"/>

		</adsm:textbox>

		<adsm:hidden property="usuario.tpCategoriaUsuario" value="F"/>
        <adsm:lookup
        	label="funcionario"
			property="funcionario" 
			idProperty="idUsuario"
			criteriaProperty="nrMatricula" 
			dataType="text"					  
			size="16" 
			maxLength="9"	
			disabled="true"
			labelWidth="20%"
			service="lms.vendas.manterValorAction.findLookupUsuarioFuncionario"
			action="/seguranca/consultarUsuarioLMS">
			
			<adsm:propertyMapping 
				criteriaProperty="usuario.tpCategoriaUsuario" 
				modelProperty="tpCategoriaUsuario"/>

			<adsm:propertyMapping
				relatedProperty="nmFuncionario"
				modelProperty="nmUsuario" />
			<adsm:textbox
				dataType="text"
				property="nmFuncionario"
				disabled="true" 
				serializable="false"/>
		</adsm:lookup>

		<adsm:range label="vigencia" labelWidth="20%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar>				
			<adsm:button id="btnStore"  caption="salvar"  onclick="storeRecord();"     disabled="false" buttonType="storeButton"/>
			<adsm:button id="btnReset"  caption="limpar"  onclick="resetDataFields();"  disabled="false" buttonType="newButton"/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>

<script>

	/*Funcao utilizada pelo no evento onChange pelo campo nrFrota*/
	function validateNrFrota(element){
		
		var nrFrota = getElementValue("nrFrota");
		if (nrFrota != "") {
			nrFrota = nrFrota.toUpperCase();
			setElementValue("nrFrota",nrFrota);
			var data = new Array();
			setNestedBeanPropertyValue(data, "nrFrota", nrFrota);			
			var sdo = createServiceDataObject("lms.vendas.manterValorAction.findValidFrota","validateNrFrota",data);
			xmit({serviceDataObjects:[sdo]});
		}		
	}

	/*Callback -  validateNrFrota*/
	function validateNrFrota_cb(data, exception) {
		if(exception != undefined){
			alert(exception);
			return false;
		}
		var nrFrota = data.nrFrota;
		var nrPlaca = data.nrPlaca;
		if(nrPlaca == undefined){
			alertI18nMessage("LMS-01188");
			resetValue("nrPlaca");
			setFocus("nrFrota");
		}else{
			setElementValue("nrFrota",nrFrota);			
			setElementValue("nrPlaca",nrPlaca);			
		}
	}	

	/*Salva o registro CPT*/
	function storeRecord(){

		var cliente  = getElementValue("cliente.idCliente");
		var segmento = getElementValue("segmentoMercado");

		if(cliente == "" && segmento == ""){			
			alertI18nMessage("LMS-01190");
			return;
		}
		
		var isDisableComplexidade = document.forms[0].cptComplexidade.disabled;
		if(!isDisableComplexidade){
			var cptComplexidade = getElementValue("cptComplexidade");
			if(cptComplexidade == ""){
				alertI18nMessage("LMS-01187");
				setFocus("cptComplexidade");
				return false;
			}			
		}

		if(validateOnSaveTpValor()){		
			storeButtonScript('lms.vendas.manterValorAction.store', 'store', document.forms[0]);
		}
	}

	/*Valida regras definidas pelo campo TpValor no momento que o usuario salva o registro*/
	function validateOnSaveTpValor(){
		var tpvalor  = getElementValue("codTpValor");
		//alert(tpvalor);
		if(tpvalor != ""){
			if(tpvalor == "V"){
				var valor = getElementValue("valor");
				if(valor == ""){
					alertI18nMessage("LMS-01187");
					setFocus("valor");
					return false;
				}				
			}
			if(tpvalor == "P"){
				var percentual = getElementValue("percentual");
				if(percentual == ""){
					alertI18nMessage("LMS-01187");
					setFocus("percentual");
					return false;
				}
			}
			if(tpvalor == "Q" || tpvalor == "T"){
				var cptComplexidade = getElementValue("cptComplexidade");
				if(cptComplexidade == ""){
					alertI18nMessage("LMS-01187");
					setFocus("cptComplexidade");
					return false;
				}
			}
			if(tpvalor == "I"){
				var nrfrota = getElementValue("nrFrota");
				if(nrfrota == ""){
					alertI18nMessage("LMS-01187");
					setFocus("nrFrota");
					return false;
				}
			}
			if(tpvalor == "F"){
				var funcionario = getElementValue("funcionario.nrMatricula");
				if(funcionario == ""){
					alertI18nMessage("LMS-01187");
					setFocus("funcionario.nrMatricula");
					return false;
				}
			}				
		}
		return true;
	}

	/*Faz as validacoes ao selecionar o Tipo Valor*/
	function validateTipoValor(){

		var idCptTipoValor = getElementValue("cptTipoValor")	
		if(idCptTipoValor == ""){
			return;
		}
	
		var data = new Array();
		setNestedBeanPropertyValue(data, "idCptTipoValor", getElementValue("cptTipoValor"));			

		var sdo = createServiceDataObject("lms.vendas.manterTiposValorAction.findCdTpValor","validateTipoValor",data);
		xmit({serviceDataObjects:[sdo]});		
	}

	/*Callback ao seelcionar o Tipo Valor*/
	function validateTipoValor_cb(data,exception){
		if(exception != undefined){
			alert(exception);
			return false;
		}
		
		setElementValue("codTpValor",data.codigo);
		testCodTpValor(data.codigo);		
	}

	/*Rotina - habilita e desabilita os campos da tela conforme o tipo valor selecionado*/
	function testCodTpValor(codigo){
		
		var segmento = getElementValue("segmentoMercado");
		if(segmento != "" && codigo != "T" && codigo != "Q"){
			alertI18nMessage("LMS-01185");
			setFocus("cptTipoValor");	
		}else 
			if(codigo != undefined && codigo ==  "V"){
				setDisabled("valor",false);
				resetValue("valor");
				setDisabled("percentual",true);
				resetValue("percentual");
				setDisabled("cptComplexidade",true);
				resetValue("cptComplexidade");
				setDisabled("nrFrota",true);
				resetValue("nrFrota");
				setDisabled("funcionario.idUsuario",true);
				resetValue("funcionario.idUsuario");				
		}else 
			if(codigo != undefined && codigo ==  "P"){
				setDisabled("percentual",false);
				resetValue("percentual");
				setDisabled("valor",true);
				resetValue("valor");
				setDisabled("cptComplexidade",true);
				resetValue("cptComplexidade");
				setDisabled("nrFrota",true);
				resetValue("nrFrota");
				setDisabled("funcionario.idUsuario",true);
				resetValue("funcionario.idUsuario");												
		}else 
			if(codigo != undefined && (codigo ==  "T" || codigo ==  "Q")){
				resetValue("cptComplexidade");
				setDisabled("cptComplexidade",false);
				resetValue("valor");
				setDisabled("valor",true);
				resetValue("percentual");
				setDisabled("percentual",true);
				resetValue("nrFrota");
				setDisabled("nrFrota",true);
				resetValue("funcionario.idUsuario");
				setDisabled("funcionario.idUsuario",true);

				//Popula a combo complexidade
				var data = new Array();
				setNestedBeanPropertyValue(data, "idCptTipoValor", getElementValue("cptTipoValor"));			

				var sdo = createServiceDataObject("lms.vendas.manterTiposComplexidadeAction.findTiposComplexidade","populateComplexidade",data);
				xmit({serviceDataObjects:[sdo]});					
				
		}else 
			if(codigo != undefined && (codigo ==  "I" || codigo ==  "F")){

				var idCliente = getElementValue("cliente.idCliente");
				if(idCliente > 99999999){
					alertI18nMessage("LMS-01186");
					setFocus("cptTipoValor");
				}else 
					if(codigo ==  "I"){
						setDisabled("nrFrota",false);
						resetValue("nrFrota");
						setDisabled("valor",true);
						resetValue("valor");	
						setDisabled("percentual",true);
						resetValue("percentual");
						setDisabled("cptComplexidade",true);
						resetValue("cptComplexidade");
						setDisabled("funcionario.idUsuario",true);
						resetValue("funcionario.idUsuario");						
				}else 
					if(codigo ==  "F"){
						setDisabled("funcionario.idUsuario",false);
						resetValue("funcionario.idUsuario");
						setDisabled("valor",true);
						resetValue("valor");	
						setDisabled("percentual",true);
						resetValue("percentual");
						setDisabled("cptComplexidade",true);
						resetValue("cptComplexidade");
						setDisabled("nrFrota",true);
						resetValue("nrFrota");
						
				}				
		}
	}

	/*Callback utilizado para preencher o combo complexidade*/
	function populateComplexidade_cb(data, exception){
		if(exception != undefined){
			alert(exception);
			return false;
		}
		cptComplexidade_cb(data);		
	}
	
	/*Acao chamada ao pressionar o botão limpar*/
	function resetDataFields(){
		newButtonScript(this.document, true, {name:'newButton_click'});
		disableFields(true);
	}

	/*
		Limpa o campo segmento - Utilizado pela lookup de cliente

		-- Caso o usúario inserir um segmento de mercado , ao atualizar o registro
		o ID do CPT ""idCpt" é limpo, pois isso é necessário, já que a configuração dos
		dados da tela vai inserir em uma tabela diferente
	*/
	function resetFieldsByPessoa(){
		resetValue("segmentoMercado");
		resetValue("cptTipoValor");		
		disableFields(true);
	}

	/*Limppa o campo cliente - Utilizado pelo campo segmento mercado*/
	function resetFieldsBySegmento(){
		if(getElementValue("idCpt") != "" && !getElementValue("idCpt").contains("CptMedida")){				
			resetValue("idCpt");
		}		
		resetValue("cliente.idCliente");
		resetValue("cptTipoValor");	
		disableFields(true);	
	}

	function onChangeClienteValue(){
		resetFieldsByPessoa();
		return cliente_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	/*Habilita ou desabilita os campos da tela*/
	function disableFields(enable){
		
		setDisabled("valor",enable);
		resetValue("valor");	
		setDisabled("percentual",enable);
		resetValue("percentual");
		setDisabled("cptComplexidade",enable);
		resetValue("cptComplexidade");
		setDisabled("nrFrota",enable);
		resetValue("nrFrota");
		//setDisabled("nrIdentificador",enable);
		//resetValue("nrIdentificador");
		setDisabled("funcionario.idUsuario",enable);
		resetValue("funcionario.idUsuario");			
	}

</script>
	

