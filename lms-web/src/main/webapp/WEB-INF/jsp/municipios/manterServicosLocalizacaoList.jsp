<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarServicosLocalizacao" service="lms.municipios.manterServicosLocalizacaoAction" >
	<adsm:form action="/municipios/manterServicosLocalizacao" height="122" idProperty="idOperacaoServicoLocaliza">
 		 <adsm:hidden property="flag" serializable="false" value="01"/>
 		 <adsm:hidden property="tpAcesso" serializable="false" value="A"/>
 		  
 		 <adsm:lookup property="filial.empresa" idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao" onDataLoadCallBack="empresa_dataLoad"
				service="lms.municipios.manterServicosLocalizacaoAction.findLookupEmpresa" dataType="text" label="empresa" size="18" action="/municipios/manterEmpresas" onPopupSetValue="empresa_onPopup"
				labelWidth="17%" width="83%" minLengthForAutoPopUpSearch="3" exactMatch="true" maxLength="18" disabled="false" serializable="false">
			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" inlineQuery="false" />
			<adsm:textbox property="filial.empresa.pessoa.nmPessoa" dataType="text" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
 		   
 		<adsm:lookup property="municipioFilial" 
				service="lms.municipios.manterServicosLocalizacaoAction.findLookupMunicipioFilial" 
				dataType="text" disabled="false" 
				criteriaProperty="municipio.nmMunicipio" 
				label="municipio" size="41" maxLength="30" 
				action="/municipios/manterMunicipiosAtendidos" labelWidth="17%" width="83%" 
				idProperty="idMunicipioFilial" exactMatch="false" minLengthForAutoPopUpSearch="3">
 			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag" inlineQuery="false"/>
 			<adsm:propertyMapping criteriaProperty="filial.empresa.idEmpresa" modelProperty="filial.empresa.idEmpresa" />
 			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nmPessoa" modelProperty="filial.empresa.pessoa.nmPessoa" inlineQuery="false"/>
 			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="filial.empresa.pessoa.nrIdentificacao" inlineQuery="false"/>
 			<adsm:propertyMapping relatedProperty="filial.empresa.idEmpresa" modelProperty="filial.empresa.idEmpresa" blankFill="false" />
 			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="filial.empresa.pessoa.nmPessoa" blankFill="false" />
 			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="filial.empresa.pessoa.nrIdentificacao" blankFill="false" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" />			
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="municipio.unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="municipio.unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.siglaDescricao" modelProperty="municipio.unidadeFederativa.siglaDescricao" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.blDistrito" modelProperty="municipio.blDistrito" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.municipioDistrito.nmMunicipio" modelProperty="municipio.municipioDistrito.nmMunicipio" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.nmMunicipio" modelProperty="municipio.nmMunicipio" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.idMunicipio" modelProperty="municipio.idMunicipio" />
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.sgFilial" modelProperty="filial.sgFilial" />
			<adsm:hidden property="municipioFilial.municipio.idMunicipio" serializable="true" value="01"/>
		</adsm:lookup>
		
		
		
		<adsm:lookup label="uf" property="municipioFilial.municipio.unidadeFederativa" 
				action="/municipios/manterUnidadesFederativas" idProperty="idUnidadeFederativa" 
				service="lms.municipios.manterServicosLocalizacaoAction.findLookupUnidadeFederativa" 
				dataType="text" criteriaProperty="sgUnidadeFederativa" size="3" maxLength="3" exactMatch="true" 
				labelWidth="17%" width="33%" minLengthForAutoPopUpSearch="2" >
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:textbox dataType="text" property="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" size="30" maxLength="30" disabled="true" />
			<adsm:propertyMapping criteriaProperty="municipioFilial.municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" addChangeListener="false"/>
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
			<adsm:propertyMapping relatedProperty="municipioFilial.municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
			<adsm:hidden property="municipioFilial.municipio.unidadeFederativa.siglaDescricao" serializable="false"/>
		</adsm:lookup>
		
		<adsm:lookup service="lms.municipios.manterServicosLocalizacaoAction.findLookupPais" dataType="text" 
				 property="municipioFilial.municipio.unidadeFederativa.pais" criteriaProperty="nmPais" idProperty="idPais" 
				 label="pais" size="37" exactMatch="false" minLengthForAutoPopUpSearch="3" 
				 action="/municipios/manterPaises" labelWidth="17%" width="33%" >
		</adsm:lookup>

		<adsm:combobox property="municipioFilial.municipio.blDistrito" label="indDistrito" labelWidth="17%" width="33%" domain="DM_SIM_NAO"/>
		
		<adsm:textbox property="municipioFilial.municipio.municipioDistrito.nmMunicipio" serializable="false" label="municDistrito" dataType="text" disabled="true"  size="37" maxLength="30" labelWidth="17%" width="33%"/>
					
		<adsm:lookup property="municipioFilial.filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
			service="lms.municipios.manterServicosLocalizacaoAction.findLookupFilial" 
			dataType="text" label="filial" size="3" action="/municipios/manterFiliais"
			labelWidth="17%" width="80%" minLengthForAutoPopUpSearch="3" exactMatch="false" style="width:45px">
			
			<adsm:propertyMapping criteriaProperty="filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
 			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" inlineQuery="false"/>
 			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
		
			<adsm:propertyMapping relatedProperty="municipioFilial.filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="municipioFilial.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
		</adsm:lookup>
				
		<adsm:combobox property="tpOperacao" label="tipoOperacao" domain="DM_TIPO_OPERACAO_COLETA_ENTREGA" width="33%" labelWidth="17%"/>
		
		<adsm:combobox property="servico.idServico" optionProperty="idServico" optionLabelProperty="dsServico" label="servico" 
					   service="lms.municipios.manterServicosLocalizacaoAction.findServico" 
					   onlyActiveValues="false" width="33%" labelWidth="17%" boxWidth="200"/>

		<adsm:combobox property="tipoLocalizacaoMunicipio.idTipoLocalizacaoMunicipio" label="tipoLocalizacao"  
					   service="lms.municipios.manterServicosLocalizacaoAction.findTipoLocalizacaoMunicipio" 
					   onlyActiveValues="false" optionLabelProperty="dsTipoLocalizacaoMunicipio" optionProperty="idTipoLocalizacaoMunicipio" width="33%" labelWidth="17%" boxWidth="200" />
					   
		<adsm:combobox property="tipoLocalizacaoMunicipioFob.idTipoLocalizacaoMunicipio" label="tipoLocalizacaoFob"  
					   service="lms.municipios.manterServicosLocalizacaoAction.findTipoLocalizacaoMunicipioFob" 
					   onlyActiveValues="false" optionLabelProperty="dsTipoLocalizacaoMunicipio" optionProperty="idTipoLocalizacaoMunicipio" width="33%" labelWidth="17%" boxWidth="200" />

		<adsm:combobox property="blAtendimentoGeral" label="atendimentoGeral" domain="DM_SIM_NAO" width="33%" labelWidth="17%"/>

		<adsm:combobox property="blCobraTaxaFluvial" label="indTaxaFluvial"  labelWidth="17%" width="33%" domain="DM_SIM_NAO"/>
		
		<adsm:combobox property="blAceitaFreteFob" label="aceitaFreteFOB" labelWidth="17%" width="33%" domain="DM_SIM_NAO"/>
		
		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:button caption="consultar" disabled="false" buttonType="findButton" onclick="consultar()"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
		
		<script>
				var lms_00013 = '<adsm:label key="LMS-00013"/>';
				var labelMunicipio = '<adsm:label key="municipio"/>';
				var labelFilial = '<adsm:label key="filial"/>';
		</script>
		
		
	</adsm:form> 
		
		<adsm:grid idProperty="idOperacaoServicoLocaliza" property="operacaoServicoLocaliza" 
				   selectionMode="check" gridHeight="161" rows="8" 
				   scrollBars="horizontal" unique="true" 
				   service="lms.municipios.manterServicosLocalizacaoAction.findPaginatedCustom"
				   rowCountService="lms.municipios.manterServicosLocalizacaoAction.getRowCountCustom">		
			<adsm:gridColumn title="filial" property="municipioFilial.filial.sgFilial" width="50" />
			<adsm:gridColumn title="municipio" property="municipioFilial.municipio.nmMunicipio" width="170" />
			<adsm:gridColumn title="uf" property="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" width="30" />
			<adsm:gridColumn title="pais" property="municipioFilial.municipio.unidadeFederativa.pais.nmPais" width="170" />
			<adsm:gridColumn title="indDistrito" property="municipioFilial.municipio.blDistrito" renderMode="image-check" width="130" />
			<adsm:gridColumn title="municDistrito" property="municipioFilial.municipio.municipioDistrito.nmMunicipio" width="170" />
			<adsm:gridColumn title="tipoOperacao" property="tpOperacao" width="130" isDomain="true"/>
			<adsm:gridColumn title="servico" property="servico.dsServico" width="160" />
			<adsm:gridColumn title="tipoLocalizacao" property="tipoLocalizacaoMunicipio.dsTipoLocalizacaoMunicipio" width="150" />
			<adsm:gridColumn title="tipoLocalizacaoFob" property="tipoLocalizacaoMunicipioFob.dsTipoLocalizacaoMunicipio" width="150" />
			<adsm:gridColumn title="atendimentoGeral" property="blAtendimentoGeral" renderMode="image-check" width="130" />
			<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" width="90" dataType="JTDate" />
			<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" width="90" dataType="JTDate" />
			<adsm:gridColumn title="dom" property="blDomingo" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="seg" property="blSegunda" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="ter" property="blTerca" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="qua" property="blQuarta" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="qui" property="blQuinta" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="sex" property="blSexta" width="30" renderMode="image-check"/>
			<adsm:gridColumn title="sab" property="blSabado" width="30" renderMode="image-check"/>
	
			<adsm:buttonBar>
				<adsm:removeButton/>
			</adsm:buttonBar>
		</adsm:grid>
</adsm:window>
<script>
	function empresa_dataLoad_cb(data){
	
		if (data != undefined && data.length >= 1){
			var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
		}
		
		filial_empresa_pessoa_nrIdentificacao_exactMatch_cb(data);
		
	}
	
	function empresa_onPopup(data){
	
		var nrIdenfitificacaoFormatado = getNestedBeanPropertyValue(data, "pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, "pessoa.nrIdentificacao", nrIdenfitificacaoFormatado);
			
		return true;
		
	}

	function consultar() {
		if (getElementValue("municipioFilial.idMunicipioFilial") != "" ||
			getElementValue("municipioFilial.filial.idFilial") != "")		
			findButtonScript('operacaoServicoLocaliza', document.getElementById("form_idOperacaoServicoLocaliza"));
		else
			alert(lms_00013 + labelMunicipio + ', ' + labelFilial + '.');	
	}

</script>