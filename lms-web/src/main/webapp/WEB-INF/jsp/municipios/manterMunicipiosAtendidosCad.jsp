<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.municipios.manterMunicipiosAtendidosAction">
	<adsm:form action="/municipios/manterMunicipiosAtendidos" idProperty="idMunicipioFilial" onDataLoadCallBack="munAtendLoad">
		<adsm:hidden property="tpAcesso" serializable="false" value="A"></adsm:hidden> 
		<adsm:hidden serializable="true" property="blVerificaRestricaoAtendimento" value="true"/>

		<adsm:lookup 
			property="filial.empresa" 
			idProperty="idEmpresa" 
			required="true" 
			criteriaProperty="pessoa.nrIdentificacao" 
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupEmpresa" dataType="text" label="empresa" size="18" 
			action="/municipios/manterEmpresas" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3"
			exactMatch="true" maxLength="18" disabled="false" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		>
			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
			<adsm:propertyMapping relatedProperty="tpEmpresa" modelProperty="tpEmpresa"/>

			<adsm:propertyMapping criteriaProperty="situacaoAtiva" modelProperty="tpSituacao" />

			<adsm:textbox property="filial.empresa.pessoa.nmPessoa" dataType="text" size="30" disabled="true" />

			<adsm:hidden property="tpEmpresa" serializable="false"/>
		</adsm:lookup>

		<adsm:hidden property="situacaoAtiva" value="A" />

		<adsm:lookup property="filial" idProperty="idFilial" required="true" criteriaProperty="sgFilial" maxLength="3"
				service="lms.municipios.manterMunicipiosAtendidosAction.findLookupFilial" dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="20%" width="80%" minLengthForAutoPopUpSearch="3"
				exactMatch="false" style="width:45px" disabled="false" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" />
			<adsm:propertyMapping criteriaProperty="filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />

			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nrIdentificacao" modelProperty="empresa.pessoa.nrIdentificacao" blankFill="false" />
			<adsm:propertyMapping relatedProperty="filial.empresa.pessoa.nmPessoa" modelProperty="empresa.pessoa.nmPessoa" blankFill="false" />
			<adsm:propertyMapping relatedProperty="filial.empresa.idEmpresa" modelProperty="empresa.idEmpresa" blankFill="false"/>
			<adsm:propertyMapping relatedProperty="tpEmpresa" modelProperty="empresa.tpEmpresa" blankFill="false"/>

			<adsm:propertyMapping relatedProperty="filial.siglaNomeFilial" modelProperty="filial.siglaNomeFilial" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>			
		</adsm:lookup>
		<adsm:hidden property="filial.siglaNomeFilial"/>

		<adsm:lookup
			service="lms.municipios.manterMunicipiosAtendidosAction.findLookupMunicipio" required="true" dataType="text" 
			disabled="false" property="municipio" criteriaProperty="nmMunicipio" label="municipio" size="30" 
			maxLength="30" action="/municipios/manterMunicipios" labelWidth="20%" width="30%" idProperty="idMunicipio" 
			exactMatch="false" minLengthForAutoPopUpSearch="3" onDataLoadCallBack="municipioDataLoad"
		>
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.nmUnidadeFederativa" modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.idUnidadeFederativa" modelProperty="unidadeFederativa.idUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.sgUnidadeFederativa" modelProperty="unidadeFederativa.sgUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.siglaDescricao" modelProperty="unidadeFederativa.siglaDescricao" />

			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.nmPais" modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="municipio.unidadeFederativa.pais.idPais" modelProperty="unidadeFederativa.pais.idPais" />
			<adsm:propertyMapping relatedProperty="municipio.blDistrito" modelProperty="blDistrito" />

			<adsm:propertyMapping relatedProperty="municipio.municipioDistrito.nmMunicipio" modelProperty="municipioDistrito.nmMunicipio" />
			<adsm:propertyMapping relatedProperty="municipio.municipioDistrito.idMunicipio" modelProperty="municipioDistrito.idMunicipio" />
			<adsm:propertyMapping relatedProperty="municipio.nmMunicipio" modelProperty="nmMunicipio"/>

			<adsm:propertyMapping criteriaProperty="situacaoAtiva" modelProperty="tpSituacao" />
		</adsm:lookup>

		<adsm:textbox label="nomeAlternativo" dataType="text" size="30" property="nmMunicipioAlternativo" maxLength="60" labelWidth="20%" width="30%" />

		<adsm:hidden property="municipio.unidadeFederativa.idUnidadeFederativa" serializable="false"/>
		<adsm:textbox property="municipio.unidadeFederativa.sgUnidadeFederativa" dataType="text" label="uf" labelWidth="20%" width="30%" disabled="true" size="2" serializable="false">
			<adsm:textbox dataType="text" property="municipio.unidadeFederativa.nmUnidadeFederativa" size="30" disabled="true" serializable="false" />
		</adsm:textbox>

		<adsm:hidden property="municipio.unidadeFederativa.siglaDescricao" serializable="false"/>
		<adsm:hidden property="municipio.unidadeFederativa.pais.idPais" serializable="false"/>

		<adsm:textbox dataType="text" property="municipio.unidadeFederativa.pais.nmPais" label="pais" labelWidth="20%" width="30%" size="30" disabled="true" serializable="false" />

		<adsm:checkbox property="municipio.blDistrito" label="indDistrito" width="30%" labelWidth="20%" disabled="true" serializable="false" />
		<adsm:hidden property="blDistrito2" value="N" serializable="false"/>

		<adsm:hidden property="municipio.municipioDistrito.idMunicipio" serializable="false"/>
		<adsm:textbox dataType="text" property="municipio.municipioDistrito.nmMunicipio" label="municDistrito" labelWidth="20%" width="30%" size="30" disabled="true" serializable="false" />

		<adsm:textbox dataType="integer" property="nrDistanciaAsfalto" label="distanciaAsfalto" maxLength="6" size="6" labelWidth="20%" width="30%" unit="km2" required="true" />
		<adsm:textbox dataType="integer" property="nrDistanciaChao" label="distanciaChao" maxLength="6" size="6" labelWidth="20%" width="30%" unit="km2" required="true" />

		<adsm:textbox dataType="integer" property="nrGrauDificuldade" label="grauDificuldade" maxLength="6" size="6" labelWidth="20%" width="30%" unit="km2" />
		<adsm:checkbox property="blRestricaoTransporte" label="restricaoTransporte" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"/>

		<adsm:checkbox property="blRecebeColetaEventual" label="recebeColetaEventual" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"/>
		<adsm:checkbox property="blDificuldadeEntrega" label="dificuldadeEntrega" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"/>

		<adsm:checkbox property="blPadraoMcd" label="padraoMCD" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;" onclick="validaPadraoMCD()"/>
		<adsm:checkbox property="blRestricaoAtendimento" label="restricaoAtendimento" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"/>

		<adsm:range label="vigencia" labelWidth="20%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar lines="2">
			<adsm:button id="frequencias" caption="frequencias" action="/municipios/manterServicosLocalizacao" cmd="main" boxWidth="175">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />

				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.idUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.idUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.siglaDescricao" target="municipioFilial.municipio.unidadeFederativa.siglaDescricao" disabled="true" />

				<adsm:linkProperty src="blDistrito2" target="municipioFilial.municipio.blDistrito" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.nmMunicipio" target="municipioFilial.municipio.municipioDistrito.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.idMunicipio" target="municipioFilial.municipio.municipioDistrito.idMunicipio" disabled="true" />

				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />				
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.idPais" target="municipioFilial.municipio.unidadeFederativa.pais.idPais" disabled="true" />

				<adsm:linkProperty src="filial.empresa.idEmpresa" target="filial.empresa.idEmpresa" disabled="true" />
				<adsm:linkProperty src="filial.empresa.pessoa.nmPessoa" target="filial.empresa.pessoa.nmPessoa" disabled="true" />
				<adsm:linkProperty src="filial.empresa.pessoa.nrIdentificacao" target="filial.empresa.pessoa.nrIdentificacao" disabled="true" />

			</adsm:button>

			<adsm:button id="cepAtendidos" caption="cepAtendidos" action="/municipios/manterIntervalosCEPAtendidos" cmd="main" boxWidth="120" breakBefore="false">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.siglaDescricao" target="municipioFilial.municipio.unidadeFederativa.siglaDescricao" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.idPais" target="pais.idPais" />
				<adsm:linkProperty src="municipio.blDistrito" target="municipioFilial.municipio.blDistrito" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.nmMunicipio" target="municipioFilial.municipio.municipioDistrito.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />				
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
			</adsm:button>

			<adsm:button id="segmentosAtendidos" caption="segmentosAtendidos" action="/municipios/manterSegmentosMercadoAtendidos" cmd="main" boxWidth="130" breakBefore="false">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.blDistrito" target="municipioFilial.municipio.blDistrito" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.nmMunicipio" target="municipioFilial.municipio.municipioDistrito.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />				
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
			</adsm:button>

			<adsm:button id="ufAtendidas" caption="ufAtendidas" action="/municipios/manterUnidadesFederativasAtendidas" cmd="main" boxWidth="140" breakBefore="true">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.blDistrito" target="municipioFilial.municipio.blDistrito" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.nmMunicipio" target="municipioFilial.municipio.municipioDistrito.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />				
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
			</adsm:button>

			<adsm:button id="filiaisOrigemAtendidas" caption="filiaisOrigemAtendidas" action="/municipios/manterFiliaisAtendidas" cmd="main" boxWidth="160">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.blDistrito" target="municipioFilial.municipio.blDistrito" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.nmMunicipio" target="municipioFilial.municipio.municipioDistrito.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />		
				<adsm:linkProperty src="filial.empresa.idEmpresa" target="municipioFilial.filial.empresa.idEmpresa" disabled="true" />
				<adsm:linkProperty src="filial.empresa.pessoa.nmPessoa" target="municipioFilial.filial.empresa.pessoa.nmPessoa" disabled="true" />
				<adsm:linkProperty src="filial.empresa.pessoa.nrIdentificacao" target="municipioFilial.filial.empresa.pessoa.nrIdentificacao" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
			</adsm:button>

			<adsm:button id="remetentesAtendidos" caption="remetentesAtendidos" action="/municipios/manterClientesAtendidos" cmd="main" boxWidth="140">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.nmUnidadeFederativa" disabled="true" />
				<adsm:linkProperty src="municipio.blDistrito" target="municipioFilial.municipio.blDistrito" disabled="true" />
				<adsm:linkProperty src="municipio.municipioDistrito.nmMunicipio" target="municipioFilial.municipio.municipioDistrito.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />				
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
			</adsm:button>

			<adsm:button id="postosPassagem" caption="postosPassagem" action="/municipios/manterPostosPassagemMunicipios" cmd="main" boxWidth="140">
				<adsm:linkProperty src="idMunicipioFilial" target="municipioFilial.idMunicipioFilial" disabled="true" />
				<adsm:linkProperty src="municipio.idMunicipio" target="municipioFilial.municipio.idMunicipio" disabled="true" />
				<adsm:linkProperty src="municipio.nmMunicipio" target="municipioFilial.municipio.nmMunicipio" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="municipioFilial.filial.pessoa.nmFantasia" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="municipioFilial.filial.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="municipioFilial.filial.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.siglaNomeFilial" target="municipioFilial.filial.siglaNomeFilial" disabled="true" />				
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.nmPais" target="municipioFilial.municipio.unidadeFederativa.pais.nmPais" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.pais.idPais" target="pais.idPais" disabled="true" />
				<adsm:linkProperty src="municipio.unidadeFederativa.nmUnidadeFederativa" target="unidadeFederativa.nmUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="municipio.unidadeFederativa.sgUnidadeFederativa" target="municipioFilial.municipio.unidadeFederativa.sgUnidadeFederativa" disabled="true"/>
				<adsm:linkProperty src="municipio.unidadeFederativa.idUnidadeFederativa" target="unidadeFederativa.idUnidadeFederativa" disabled="true"/>
			</adsm:button>

			<adsm:storeButton callbackProperty="storeMunAtend"/>
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	//**********************************************************

	function municipioDataLoad_cb(data){
		if (data != undefined){
			if (getNestedBeanPropertyValue(data, ":0.blDistrito") == "true")
				setElementValue("blDistrito2", "S");
			else
				setElementValue("blDistrito2", "N");
		}
		return lookupExactMatch({e:document.getElementById("municipio.idMunicipio"), data:data, callBack:"municipioLikeEnd"});
	}

	function municipioLikeEnd_cb(data){
		if (data != undefined){
			if (getNestedBeanPropertyValue(data, ":0.blDistrito") == "true") {
				setElementValue("blDistrito2", "S");
			} else {
				setElementValue("blDistrito2", "N");
			}
		}
		return municipio_nmMunicipio_likeEndMatch_cb(data);
	}

	function storeMunAtend_cb(data, error, key){
		var showError = (key != "LMS-29088");

		store_cb(data, error, key, showError);

		if (key == "LMS-29088" && confirm(error)) {
			setElementValue("blVerificaRestricaoAtendimento", "false");
			storeButtonScript('lms.municipios.manterMunicipiosAtendidosAction.store', "storeMunAtend", document.getElementById('form_idMunicipioFilial'));
		}

		if (error == undefined && data != undefined){
			siglaNomeFilial = document.getElementById("filial.sgFilial").value + " - " + document.getElementById("filial.pessoa.nmFantasia").value;
			document.getElementById("filial.siglaNomeFilial").value = siglaNomeFilial;					
			comportamentoVigencia(data);
			disableButtons(
				data.blDisableAtendimentoCliente,
				data.blDisableAtendimentoSegmento,
				data.blDisableAtendimentoFilial,
				data.blDisableAtendimentoIntervaloCep,
				data.blDisableAtendimentoUf
			);
			setFocusOnNewButton();
		}
	}

	// #####################################################################
	// tratamento do comportamento padrão da vigência
	// #####################################################################
	function munAtendLoad_cb(data, exception,key) {
		if (exception != undefined) {
			alert(exception);
			return;
		}
		onDataLoad_cb(data, exception); 

		comportamentoVigencia(data);
		focoVigencia(data.acaoVigenciaAtual);

		var blDistrito = getNestedBeanPropertyValue(data,"municipio.blDistrito");
		setElementValue("blDistrito2", (blDistrito == "true")? "S":"N");

		//regra 3.6
		disableButtons(
			data.blDisableAtendimentoCliente,
			data.blDisableAtendimentoSegmento,
			data.blDisableAtendimentoFilial,
			data.blDisableAtendimentoIntervaloCep,
			data.blDisableAtendimentoUf
		);

	}

	function focoVigencia(acaoVigenciaAtual) {
		if (acaoVigenciaAtual == 2) {
			setFocusOnNewButton();
		} else {
			setFocusOnFirstFocusableField();
		}
	}

	function comportamentoVigencia(data){
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data,"acaoVigenciaAtual");

		// 0 = VIGENCIA INICIAL > HOJE
		// PODE TUDO
		if (acaoVigenciaAtual == 0) {
			habilitaCampos();
			//setDisabled("filial.idFilial", true);
			//setDisabled("filial.empresa.idEmpresa", true);
			setDisabled("municipio.idMunicipio", document.getElementById("municipio.idMunicipio").masterLink == "true");

		// 1 = VIGENCIA INICIAL <= HOJE E
		// VIGENCIA FINAL >= HOJE
		// DESABILITA VIGENCIA INICIAL
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			setDisabled("__buttonBar:0.storeButton", false);

			setDisabled("dtVigenciaFinal", false);
			setFocusOnFirstFocusableField();
		// 2 = VIGENCIA INICIAL <= HOJE E 
		// VIGENCIA FINAL < HOJE
		// DESABILITA TUDO, EXCETO O BOTÃO NOVO
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document, true);
			setDisabled("__buttonBar:0.newButton", false);
			for(x = 1; x <= 7; x++)
				setDisabled("__buttonBar:0_" + x,false);

		}
		validaPadraoMCD();
	}

	function disableButtons(
		blDisableAtendimentoCliente,
		blDisableAtendimentoSegmento,
		blDisableAtendimentoFilial,
		blDisableAtendimentoIntervaloCep,
		blDisableAtendimentoUf
	) {
		setDisabled("cepAtendidos", (blDisableAtendimentoIntervaloCep == "true"));
		setDisabled("segmentosAtendidos", (blDisableAtendimentoSegmento == "true"));
		setDisabled("ufAtendidas", (blDisableAtendimentoUf == "true"));
		setDisabled("filiaisOrigemAtendidas", (blDisableAtendimentoFilial == "true"));
		setDisabled("remetentesAtendidos", (blDisableAtendimentoCliente == "true"));
		setDisabled("frequencias", false);
		setDisabled("postosPassagem", false);
	}

	function setaBlDistrito() {
		if (document.getElementById("municipio.blDistrito").checked)
			setElementValue("blDistrito2", "S");
		else
			setElementValue("blDistrito2", "N");		
	}

	function estadoNovo() {		
		document.getElementById('blRecebeColetaEventual').checked=true; 
		habilitaCampos();
		setFocusOnFirstFocusableField();
	}

	function habilitaCampos() {
		setDisabled('filial.empresa.idEmpresa', false);
		setDisabled('filial.idFilial',document.getElementById("filial.idFilial").masterLink == "true");
		setDisabled('municipio.idMunicipio',document.getElementById("municipio.idMunicipio").masterLink == "true");
		setDisabled('nmMunicipioAlternativo', false);
		setDisabled('nrDistanciaChao', false);
		setDisabled('nrDistanciaAsfalto', false);
		setDisabled('nrGrauDificuldade', false);
		setDisabled('dtVigenciaInicial', false);
		setDisabled('dtVigenciaFinal', false);
		setDisabled('blRecebeColetaEventual', false);
		setDisabled('blDificuldadeEntrega', false);
		setDisabled('blPadraoMcd', false);
		setDisabled('blRestricaoAtendimento', false);
		setDisabled('blRestricaoTransporte', false);
	}

	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {		
		if ((eventObj.name != "gridRow_click") && (eventObj.name != "storeButton")) { 
			estadoNovo();			
		} else if (eventObj.name == "storeButton"){
			//disableButtons();
			setaBlDistrito();
		}
	}

	function validaPadraoMCD() {
		if (document.getElementById("blPadraoMcd").checked) {
			setElementValue("blRestricaoAtendimento", "N");
			setDisabled('blRestricaoAtendimento', true);
		} else
			setDisabled('blRestricaoAtendimento', false);
	}

</script>
