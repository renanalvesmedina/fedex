<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.associarCedentesEmpresasAction" onPageLoadCallBack="myPageLoadCallBack">

	<adsm:form action="/contasReceber/associarCedentesEmpresas">

		<adsm:hidden property="idCedente" serializable="false" />
		
		<adsm:combobox 
			service="lms.contasreceber.associarCedentesEmpresasAction.findCedentes" 
			optionLabelProperty="comboText" 
			labelWidth="18%"
			width="82%"
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			autoLoad="false"
			label="cedente"> 
		</adsm:combobox>
		
		<adsm:lookup label="empresa" 
					 labelWidth="18%" 
					 dataType="text" 
					 size="20" 
					 maxLength="20" 
					 width="82%"
				     service="lms.contasreceber.associarCedentesEmpresasAction.findLookupEmpresa" 
				     property="empresa" 
				     idProperty="idEmpresa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/municipios/manterEmpresas" 
					 onDataLoadCallBack="customizado">
					
					<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
					
					<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="50" disabled="true" serializable="false" />
		</adsm:lookup>

		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				disabled="false"
				buttonType="findButton" 
				caption="consultar" 
				onclick="validateCedenteEmpresas(this.form)"/>
				
			<adsm:resetButton/>
			
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid 
		selectionMode="check" 
		idProperty="idComplementoEmpresaCedente" 
		property="empresasCedente" 
		gridHeight="200" 
		unique="true" 
		rows="13">
	
	
		<adsm:gridColumn width="40%" property="dsCedente" title="cedente" />
		<adsm:gridColumn width="45%" property="dsEmpresa" title="empresa" />
		<adsm:gridColumn width="15%" property="nrUltimoBoleto" title="boleto" dataType="integer"/>
		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

	function validateCedenteEmpresas(form){
		findButtonScript('empresasCedente', form);
	}

	function myPageLoadCallBack_cb(data,erro){
		
		onPageLoad_cb(data,erro);
        var dados = new Array();
        setNestedBeanPropertyValue(dados, "cedente.idCedente", getElementValue("idCedente"));
        var sdo = createServiceDataObject("lms.contasreceber.associarCedentesEmpresasAction.findCedentes",
                                             "cedente",
                                             dados);
         xmit({serviceDataObjects:[sdo]});
		
	}
	
	function cedente_cb(data){
		comboboxLoadOptions({e:document.getElementById("cedente.idCedente"), data:data});
		var idCedente = getElementValue("idCedente");
		if (idCedente != ""){
			setElementValue("cedente.idCedente", idCedente);
			setDisabled("cedente.idCedente", true);
			document.getElementById("cedente.idCedente").masterLink = "true";
		}
	}
	
	 function changeEmpresa() {
		return empresa_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function customizado_cb(data) {
		empresa_pessoa_nrIdentificacao_exactMatch_cb(data);
	}
	
	
</script>
