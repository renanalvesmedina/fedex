<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.cepService" onPageLoadCallBack="cepPesq">
	<adsm:i18nLabels>
		<adsm:include key="LMS-27063" />
	</adsm:i18nLabels>
	
	
	<adsm:form action="/configuracoes/pesquisarCEP">
	
		<adsm:hidden property="cepCriteria" serializable="false" />
	
	 	<adsm:lookup label="pais" property="municipio.unidadeFederativa.pais" idProperty="idPais" dataType="text" criteriaProperty="nmPais"
			service="lms.municipios.paisService.findLookup" size="40" maxLength="30" minLengthForAutoPopUpSearch="3"
			action="/municipios/manterPaises" exactMatch="false"/>

	 	<adsm:lookup label="uf" property="municipio.unidadeFederativa" idProperty="idUnidadeFederativa" 
	 		dataType="text" criteriaProperty="sgUnidadeFederativa"
			service="lms.municipios.unidadeFederativaService.findLookup" size="3" maxLength="30" 
			minLengthForAutoPopUpSearch="3"	action="/municipios/manterUnidadesFederativas" exactMatch="false">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" inlineQuery="true"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:textbox property="municipio.unidadeFederativa.nmUnidadeFederativa" dataType="text" serializable="false" size="30" disabled="true"/>
		</adsm:lookup>
		
		<adsm:lookup label="municipio" property="municipio" idProperty="idMunicipio" dataType="text" criteriaProperty="nmMunicipio"
			service="lms.municipios.municipioService.findLookup" size="35" maxLength="50"
			action="/municipios/manterMunicipios" exactMatch="false" minLengthForAutoPopUpSearch="3">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" 
				modelProperty="unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" 
				modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
				modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa" 
				modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa" 
				modelProperty="unidadeFederativa.nmUnidadeFederativa" />	
		</adsm:lookup>

		<adsm:textbox label="bairro" property="nmBairro" dataType="text" size="50" maxLength="72" />
		
		<adsm:hidden property="dsLogradouro"/>
		<adsm:hidden property="dsTipoLogradouro" serializable="false"/>
		<adsm:complement label="endereco" width="85%">
			<adsm:combobox property="tipoLogradouro.idTipoLogradouro" width="15%" service="lms.configuracoes.tipoLogradouroService.find" boxWidth="158"
				optionLabelProperty="dsTipoLogradouro" optionProperty="idTipoLogradouro" serializable="false">
				<adsm:propertyMapping modelProperty="dsTipoLogradouro" relatedProperty="dsLogradouro"/>
			</adsm:combobox>
			<adsm:textbox property="nmLogradouro" dataType="text" size="93" maxLength="100" width="65%"/>
		</adsm:complement>


		<adsm:textbox label="cep" property="nrCep" dataType="text" size="20" maxLength="8" />

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" id="bntConsultar" onclick="pesquisarCEPs(this)" disabled="false" buttonType="findButton"  />

			<adsm:resetButton/>
		</adsm:buttonBar>

	</adsm:form>

	<adsm:grid idProperty="cepCriteria" property="ceps" gridHeight="200" selectionMode="none" 
	           service="lms.configuracoes.pesquisarCEPAction.findPaginatedEspecific"
	           rowCountService="lms.configuracoes.pesquisarCEPAction.getRowCountEspecific">
		<adsm:gridColumn width="5%" title="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa"/>
		<adsm:gridColumn width="18%" title="municipio" property="municipio.nmMunicipio"/>
		<adsm:gridColumn width="20%" title="bairro" property="nmBairro"/>
		<adsm:gridColumn width="25%" title="endereco" property="nmLogradouro"/>
		<adsm:gridColumn width="25%" title="dsLogComplemento" property="dsLogComplemento"/>
		<adsm:gridColumn width="7%" title="cep" property="nrCep"/>
	</adsm:grid>

	<adsm:buttonBar>
		<adsm:button caption="fechar" onclick="javascript:window.close();" disabled="false"/>
	</adsm:buttonBar>
</adsm:window>
<script>

	/** Valida se um dos campos foi preenchido para que a consulta seja realizada.
	Campos: Municipio ou Logradouro ou CEP
	*/
	function validatePesquisar(){
		var municipio = document.getElementById("municipio.idMunicipio").value;
		var logradouro = document.getElementById("tipoLogradouro.idTipoLogradouro").value;
		var nmLogradouro = document.getElementById("nmLogradouro").value;
		var nrCep = document.getElementById("nrCep").value;

		if (municipio.trim() == "" && nmLogradouro.trim() == "" && nrCep.trim() == ""  ){
			return false;
		}
		return true;
	}
	

				
		/** Verifica se os parametros foram passados para consulta */				
		function pesquisarCEPs(obj){
		
		if (!validatePesquisar()){		
			alert(''+i18NLabel.getLabel("LMS-27063"));
			setFocusOnFirstFocusableField();
			return false;
		}
		findButtonScript('ceps', obj.form);

		return false;
	}
	
	
				
				

	/**
	 * Verifica o ID da combobox de Tipo de Logradouro pelo seu texto.
	 *@param dsTipoLogradouro Descrição contida na combobox
	 *@return ID da combobox
	 */
	function getIdTipoLogradouroByDs(dsTipoLogradouro){
		var opt = document.getElementById("tipoLogradouro.idTipoLogradouro").options;
		for (i = 0; i < opt.length; i++){
			if (opt[i].text == dsTipoLogradouro){
				return opt[i].value;
			}
		}
	}
	function cepPesq_cb(data, erro){
		onPageLoad_cb(data,erro);
		if (document.getElementById("cepCriteria").value != ""){
			setElementValue("nrCep", document.getElementById("cepCriteria").value);
		}
		
		var idTpLog = getIdTipoLogradouroByDs(getElementValue("dsTipoLogradouro"));
		if (idTpLog != undefined){
			setElementValue("tipoLogradouro.idTipoLogradouro", idTpLog);
		}
		

	}
</script>