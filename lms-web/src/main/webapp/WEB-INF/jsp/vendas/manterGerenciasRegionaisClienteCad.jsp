<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.vendas.gerenciaRegionalService">

	<adsm:form action="/vendas/manterRegioesCliente" idProperty="idGerenciaRegional" onDataLoadCallBack="myDataLoad">
		<adsm:hidden property="statusAtivo" serializable="true" value="A"/>	
		<adsm:hidden property="idFilial" serializable="false"/>	
		<adsm:hidden property="cliente.idCliente"/>		
		
		<adsm:hidden property="nmMunicipio" />
		<adsm:hidden property="ufMunicipio" />
		<adsm:hidden property="nmUfMunicipio" />
				
	    <adsm:textbox width="85%" dataType="text" property="cliente.pessoa.nrIdentificacao" label="cliente" size="20" maxLength="20" disabled="true" serializable="false" >
	        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="60" maxLength="50" disabled="true" serializable="false"/>
	    </adsm:textbox>   

		<adsm:textbox property="dsGerenciaRegional" label="regional" dataType="text" maxLength="60" size="30" required="true" width="85%"/>
		

		
		<adsm:listbox size="15"
					  property="municipios"
					  optionProperty="idMunicipio"
					  optionLabelProperty="nmMunicipio"
					  width="85%" 
					  showOrderControls="false" 
					  boxWidth="440"
					  showIndex="false"
					  serializable="true"					  
					  label="municipio"
					  required="true" 
					  allowMultiple="false">
			<adsm:lookup action="/municipios/manterMunicipios" 					 
						 service="lms.municipios.municipioService.findLookup"
						 dataType="text" 
						 property="municipio" 
						 idProperty="idMunicipio"						 
						 criteriaProperty="nmMunicipio" 						 
						 maxLength="30"
						 exactMatch="false"
						 minLengthForAutoPopUpSearch="3">

				<adsm:propertyMapping relatedProperty="nmMunicipio"  modelProperty="nmMunicipio"/>
				<adsm:propertyMapping relatedProperty="ufMunicipio"  modelProperty="unidadeFederativa.sgUnidadeFederativa"/>
				<adsm:propertyMapping relatedProperty="nmUfMunicipio"  modelProperty="unidadeFederativa.nmUnidadeFederativa"/>
				<adsm:propertyMapping relatedProperty="municipios_municipio.nmMunicipio"  modelProperty="nmMunicipioAndSgUnidadeFederativa"/>

				<adsm:propertyMapping criteriaProperty="statusAtivo" modelProperty="tpSituacao"/>
				<adsm:propertyMapping criteriaProperty="nmMunicipio" modelProperty="nmMunicipio" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="ufMunicipio" modelProperty="unidadeFederativa.sgUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="nmUfMunicipio" modelProperty="unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>


			</adsm:lookup>
		</adsm:listbox>		
		
		<adsm:buttonBar>
			<adsm:storeButton  id="storeButton"/>
			<adsm:newButton    id="newButton"/>
			<adsm:removeButton id="removeButton"/>
		</adsm:buttonBar>		
		
	</adsm:form>
	
</adsm:window>
<script>
	
	/*
	* Criada para validar acesso do usuário 
	* logado à filial do cliente
	*/
	function myDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		initWindow();
	}
	
	/*
	* Criada para validar acesso do usuário 
	* logado à filial do cliente
	*/
	function initWindow(){
		if (getTabGroup(document).getTab("pesq").getElementById("permissao").value!="true") {
			setDisabled("storeButton", true);
			setDisabled("newButton", true);
			setDisabled("removeButton", true);
		}
	}			
	
</script>