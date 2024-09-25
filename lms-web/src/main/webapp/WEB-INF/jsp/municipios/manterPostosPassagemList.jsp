<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarPostoPassagem" service="lms.municipios.postoPassagemService">
	<adsm:form action="/municipios/manterPostosPassagem" height="103">
		<adsm:hidden property="tpPostoPassagem.description" serializable="false"/>
		<adsm:combobox property="tpPostoPassagem" label="tipoPostoPassagem" domain="DM_POSTO_PASSAGEM" onlyActiveValues="true"  labelWidth="18%" width="32%"/>
		
		<adsm:lookup dataType="text" property="municipio" idProperty="idMunicipio" criteriaProperty="nmMunicipio"
	             action="/municipios/manterMunicipios" service="lms.municipios.municipioService.findLookup"
                 maxLength="60" size="30" minLengthForAutoPopUpSearch="3" exactMatch="false"
                 label="localizacaoMunicipio" labelWidth="18%" width="32%">
				<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" addChangeListener="false"/>
				<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" addChangeListener="false"/>
                <adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" inlineQuery="false"/>
				<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" addChangeListener="false"/>
				<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" addChangeListener="false"/>

                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
                <adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
				<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
				<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
        </adsm:lookup> 
		 
		<adsm:lookup property="municipio.unidadeFederativa"  
					idProperty="idUnidadeFederativa" criteriaProperty="sgUnidadeFederativa" 
				 	service="lms.municipios.unidadeFederativaService.findLookup" dataType="text" 
					width="7%" label="uf" size="3" maxLength="3" labelWidth="18%"  
					action="/municipios/manterUnidadesFederativas" minLengthForAutoPopUpSearch="2" exactMatch="true">
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" addChangeListener="false"/>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais" />
		</adsm:lookup>
		
		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" width="25%" size="25" serializable="false" disabled="true"/>
		
		<adsm:lookup property="municipio.unidadeFederativa.pais" idProperty="idPais" width="32%" labelWidth="18%" 
					 criteriaProperty="nmPais" service="lms.municipios.paisService.findLookup" dataType="text" 
			         label="pais" size="30" minLengthForAutoPopUpSearch="3" maxLength="60" exactMatch="false"
			         action="/municipios/manterPaises"/>
		   
		<adsm:lookup label="rodovia" criteriaProperty="sgRodovia" service="lms.municipios.rodoviaService.findLookup" 
					 dataType="text" size="7" maxLength="10" labelWidth="18%" width="10%" 
					 action="/municipios/manterRodovias" idProperty="idRodovia" property="rodovia"
					 onchange="return validaSigla()">
			<adsm:propertyMapping modelProperty="dsRodovia" relatedProperty="rodovia.dsRodovia"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="pais.nmPais"/>
			<adsm:propertyMapping criteriaProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="pais.idPais"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="rodovia.dsRodovia" size="25" disabled="true" width="72%"/>

		<adsm:combobox property="tpFormaCobranca" label="formaCobranca" domain="DM_FORMA_COBRANCA_POSTO_PASSAGEM" width="32%" labelWidth="18%"/>
		
		<adsm:combobox property="tpSentidoCobranca" label="sentidoCobranca" domain="DM_SENTIDO_COBRANCA_POSTO_PASSAGEM" labelWidth="18%" width="32%" onlyActiveValues="true" />
		
		<adsm:lookup minLengthForAutoPopUpSearch="5" exactMatch="false" service="lms.municipios.concessionariaService.findLookup" dataType="text" property="concessionaria" criteriaProperty="pessoa.nrIdentificacao" idProperty="idConcessionaria" label="concessionaria" size="21" maxLength="20" labelWidth="18%" width="19%" action="municipios/manterConcessionariaTaxasPassagem" onPopupSetValue="popUpSetC" onDataLoadCallBack="dataLoadC">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa" formProperty="concessionaria.pessoa.nmPessoa"/>
		</adsm:lookup>
		<adsm:textbox dataType="text" property="concessionaria.pessoa.nmPessoa" size="25" disabled="true" width="62%"/>
		
		<adsm:range label="vigencia" labelWidth="18%" width="78%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"/>
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="PostoPassagem"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid gridHeight="180" idProperty="idPostoPassagem" property="PostoPassagem" selectionMode="check" scrollBars="horizontal" rows="9" unique="true"
	 defaultOrder="municipio_unidadeFederativa_pais_.nmPais,municipio_unidadeFederativa_.sgUnidadeFederativa,municipio_.nmMunicipio,rodovia_.sgRodovia,nrKm,dtVigenciaInicial">
		<adsm:gridColumn title="tipoPostoPassagem" property="tpPostoPassagem" isDomain="true" width="175" />
		<adsm:gridColumn title="localizacaoMunicipio" property="municipio.nmMunicipio" width="150" />
		<adsm:gridColumn title="uf" property="municipio.unidadeFederativa.sgUnidadeFederativa" width="40" />
		<adsm:gridColumn title="pais" property="municipio.unidadeFederativa.pais.nmPais" width="150" />
		<adsm:gridColumn title="rodovia" property="rodovia.sgRodovia" width="80" />
		<adsm:gridColumn title="km" property="nrKm" width="60" align="right" mask="##,###.#" dataType="integer"/>
		<adsm:gridColumn title="formaCobranca" property="tpFormaCobranca" isDomain="true" width="150" />
		<adsm:gridColumn title="sentidoCobranca" property="tpSentidoCobranca" isDomain="true" width="150" />
		<adsm:gridColumn title="concessionaria" property="concessionaria.pessoa.nmPessoa" width="200" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="100" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<Script>
<!--
	function popUpSetC(data) {
		var nrFormatado = getNestedBeanPropertyValue(data, ":pessoa.nrIdentificacaoFormatado");
		setNestedBeanPropertyValue(data, ":pessoa.nrIdentificacao", nrFormatado);
		return true;	
	}
	
	function validaSigla(){
		var sigla = document.getElementById("rodovia.sgRodovia");
		sigla.value = sigla.value.toUpperCase();
		var retorno = rodovia_sgRodoviaOnChangeHandler();
		return retorno;
	}

	function dataLoadC_cb(data,exception) {
		if (exception != null) {
			alert(exception);
			return;
		}
		if (data != undefined && data.length == 1) {
			var nrFormatado = getNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacaoFormatado");
			setNestedBeanPropertyValue(data, ":0.pessoa.nrIdentificacao", nrFormatado);
		}
		return concessionaria_pessoa_nrIdentificacao_likeEndMatch_cb(data);
	}
//-->
</Script>