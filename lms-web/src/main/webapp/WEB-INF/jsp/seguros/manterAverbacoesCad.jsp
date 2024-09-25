<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.manterAverbacoesAction">

	<adsm:form action="/seguros/manterAverbacoes" idProperty="idAverbacao" height="390">

		<adsm:lookup dataType="text" 
					 property="cliente"
					 label="cliente"  
					 idProperty="idCliente"
 					 criteriaProperty="pessoa.nrIdentificacao" 
 					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
 					 service="lms.seguros.manterAverbacoesAction.findLookupCliente" 
 					 action="/vendas/manterDadosIdentificacao" 
 					 exactMatch="false"
 					 size="20" 
 					 maxLength="20" 
 					 serializable="true" 
 					 labelWidth="15%" 
 					 width="85%" 
 					 required="true"> 
 			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
 			<adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="49" maxLength="50" disabled="true" serializable="true"/> 
 		</adsm:lookup> 

		<adsm:hidden property="tipoMeioTransporte.tpMeioTransporte" value="R"/>
		<adsm:combobox
			property="tpModal" boxWidth="110" label="modal" 
			optionProperty="value"
			optionLabelProperty="description" 
			service="lms.seguros.manterAverbacoesAction.findComboModal"  
			width="17%"
			onlyActiveValues="true">
		</adsm:combobox>
		
		<adsm:combobox 
			property="tpAbrangencia" 
			label="abrangencia" 
			domain="DM_ABRANGENCIA" 
			boxWidth="110" 
			renderOptions="true"/>
				
		<adsm:combobox
			property="tpFrete"
			label="tipoFrete"
			domain="DM_TIPO_FRETE"			
			width="100%"
			boxWidth="110"/>
		
		<adsm:combobox property="tipoSeguro.idTipoSeguro"
				optionProperty="idTipoSeguro" 
				optionLabelProperty="sgTipo"
				service="lms.seguros.manterAverbacoesAction.findTipoSeguroOrderBySgTipo"
				label="tipoSeguro"				
				width="100%"
				boxWidth="110"
				required="true"/>		
		
		<adsm:hidden property="corretora.pessoa.nmPessoa"/>		
		<adsm:combobox property="corretora.idReguladora" 
				optionProperty="pessoa.idPessoa" 
				optionLabelProperty="pessoa.nmPessoa" 
				service="lms.seguros.manterAverbacoesAction.findReguladoraOrderByNmPessoa" 
				label="reguladora" 
				width="100%" 
				boxWidth="250"/>				   
				   
		<adsm:combobox property="seguradora.idSeguradora" 
				optionProperty="seguradora.idSeguradora" 
				optionLabelProperty="seguradora.pessoa.nmPessoa" 
				service="lms.seguros.manterAverbacoesAction.findReguladoraSeguradoraOrderByNmPessoa" 
				label="seguradora" width="100%" boxWidth="250"
				required="true"/>
				
		<adsm:textbox property="dtViagem" label="dataViagem" dataType="JTDate" size="40%" width="100%" maxLength="16" required="true"/>
		
		<adsm:textbox property="vlEstimado" label="valorEstimado" dataType="currency" mask="###,###,###,###,##0.00" maxLength="18" size="31%" width="100%" required="true"/>
		
		<adsm:textbox property="psTotal" label="pesoTotal" dataType="weight" size="31%" width="100%" maxLength="18" required="true"/>
		
		<adsm:textarea property="dsNF" label="nfs" maxLength="1500" columns="122" width="100%"/>
		
		<adsm:lookup dataType="text"
					 property="filialOrigem"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.seguros.manterAverbacoesAction.findLookupFilial"
    				 label="filialOrigem" 
    				 required="true"
    				 size="6" 
    				 maxLength="3" 
    				 width="30%"  
    				 exactMatch="true" 
    				 action="/municipios/manterFiliais"
		             onDataLoadCallBack="filialDataLoad">
         	<adsm:propertyMapping relatedProperty="filialOrigem.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia"/>         
         	<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" size="25" disabled="true" serializable="true"/>
	    </adsm:lookup>

	    <adsm:lookup dataType="text" 
	    			 property="filialDestino"
					 idProperty="idFilial" 
					 criteriaProperty="sgFilial"
    				 service="lms.seguros.manterAverbacoesAction.findLookupFilial"
    				 label="filialDestino" 
    				 required="true"
    				 size="6" 
    				 maxLength="3" 
    				 width="30%" 
    				 labelWidth="15%"
    				 exactMatch="true"
    				 action="/municipios/manterFiliais">
         	<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" size="25" disabled="true" serializable="false" />
	    </adsm:lookup>
        <adsm:hidden property="sgFilialDestino" serializable="true"/>
			
		<adsm:lookup dataType="text"
					 label="municipioOrigem" 
			         property="municipioOrigem" 
					 idProperty="idMunicipio" 
					 criteriaProperty="nmMunicipio"
					 service="lms.seguros.manterAverbacoesAction.findLookupMunicipio"
					 action="/municipios/manterMunicipios" 
					 size="25" 
					 maxLength="60" 
					 width="15%"
			      	 exactMatch="false" 
			         minLengthForAutoPopUpSearch="3" 
			         required="true">
			<adsm:propertyMapping 
				modelProperty="unidadeFederativa.sgUnidadeFederativa" 
				relatedProperty="municipioOrigem.unidadeFederativa.sgUnidadeFederativa" 
			/>
			
			<adsm:textbox 
				label="uf"
				dataType="text" 
				disabled="true" 
				property="municipioOrigem.unidadeFederativa.sgUnidadeFederativa" 
				width="13%"
 				labelWidth="2%"
				size="2"
				maxLength="2" 
			/>
		</adsm:lookup>	
		
		<adsm:lookup dataType="text"
					 label="municipioDestino"			 
					 property="municipioDestino" 
					 idProperty="idMunicipio" 
					 criteriaProperty="nmMunicipio"
					 service="lms.seguros.manterAverbacoesAction.findLookupMunicipio"
					 action="/municipios/manterMunicipios" 
					 size="25" 
					 maxLength="60" 
					 width="15%"
					 exactMatch="false" 
					 minLengthForAutoPopUpSearch="3" 
					 required="true">
			<adsm:propertyMapping 
				modelProperty="unidadeFederativa.sgUnidadeFederativa" 
				relatedProperty="municipioDestino.unidadeFederativa.sgUnidadeFederativa" 
			/>
			
			<adsm:textbox 
				label="uf"
				dataType="text" 
				disabled="true" 
				property="municipioDestino.unidadeFederativa.sgUnidadeFederativa" 
				width="15%"
 				labelWidth="2%"
				size="2"
				maxLength="2" 
			/>
		</adsm:lookup>
		
		<adsm:lookup label="meioTransporte" 
		             property="meioTransporteRodoviario2" 
		             idProperty="idMeioTransporte"
		             dataType="text" 
		             picker="false"		             
		             criteriaProperty="nrFrota"
					 service="lms.seguros.manterAverbacoesAction.findLookupMeioTransporte" 
					 action="/contratacaoVeiculos/manterMeiosTransporte" 
					 onchange="meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler()"
					 width="100%" maxLength="6" size="6">
			
			<adsm:propertyMapping modelProperty="idMeioTransporte" relatedProperty="meioTransporte.idMeioTransporte"/>
			<adsm:propertyMapping modelProperty="nrIdentificador"  relatedProperty="meioTransporte.nrIdentificador"/> 
			<adsm:propertyMapping modelProperty="nrIdentificador"  criteriaProperty="meioTransporte.nrIdentificador" disable="false"/> 
			
			<adsm:lookup property="meioTransporte"  
						 idProperty="idMeioTransporte" 
			             dataType="text"
			             criteriaProperty="nrIdentificador"
						 service="lms.seguros.manterAverbacoesAction.findLookupMeioTransporte"
						 action="/contratacaoVeiculos/manterMeiosTransporte" 
						 maxLength="25" size="14" picker="true"> 
				<adsm:propertyMapping modelProperty="nrFrota" relatedProperty="meioTransporteRodoviario2.nrFrota"/> 
				<adsm:propertyMapping modelProperty="nrFrota" criteriaProperty="meioTransporteRodoviario2.nrFrota" disable="false"/> 
												
			</adsm:lookup>
		</adsm:lookup>
		
		<adsm:textarea property="dsContingencia" label="contingencia" maxLength="1500" columns="122" width="100%" required="true"/>
		
		<adsm:textarea property="obAverbacao" label="observacoes" maxLength="1500" columns="122" width="100%"/>		
        
        <adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton/>
			<adsm:button caption="excluir" id="removeButton" buttonType="removeButton" onclick="remove()"/>
		</adsm:buttonBar>

	</adsm:form>
		
</adsm:window>

<script>
	
	function filialDataLoad_cb(data){
		var retorno = filialOrigem_sgFilial_exactMatch_cb(data);

		return retorno;
	}
	
	function meioTransporteByIdMeioTransporteNrFrotaOnChangeHandler() {	
	 	meioTransporteRodoviario2_nrFrotaOnChangeHandler();
	 	
	 	if (document.getElementById("meioTransporteRodoviario2.nrFrota").value=="") {
	 		document.getElementById("meioTransporte.idMeioTransporte").value="";
	 		resetValue(document.getElementById("meioTransporte.idMeioTransporte")); 
	 	}
	}
	
	function remove() {
		removeButtonScript("lms.seguros.manterAverbacoesAction.removeById", "remove", "idAverbacao", this.document);		
	}
	
	function remove_cb(data, error) {
		if(error == undefined){
			resetValue(this.document);
			setDisabled("removeButton" , true);
			showSuccessMessage();
		}else{
			alert(error);	
		}
		 
	}

</script>