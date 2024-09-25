<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	function validateTab(){
		return validaPesquisa();
	}

	function validateLookupForm () {
		return validaPesquisa();
	}

	function validaValoresForm(tipo){
		var uf = getElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa");
		var filial = getElementValue("filialByIdFilial" + tipo + ".sgFilial");
		var munic = getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio");
		var aerop = getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto");

		if (uf=='' && filial =='' && munic =='' && aerop=='')
			return false;
		return true;
	}

	function validaPesquisa(){
		var tpRestricao = getElementValue("tpRestricao");
		if (tpRestricao == "Z") {
			if (!hasValue(getElementValue("zonaByIdZonaOrigem.idZona")) || !hasValue(getElementValue("zonaByIdZonaDestino.idZona"))){
				alertI18nMessage("2filtrosObrigatorios", ["zonaOrigem", "zonaDestino"]);
				return false;
			}
		} else if (!validaValoresForm("Origem") || !validaValoresForm("Destino")){
			alertI18nMessage("LMS-30026")
			return false;
		}
		return true;
	}

	function initWindow(eventObj) {
		/** Variaveis */
		getElement("tpLocalizacaoOperacional").masterLink = true;
		getElement("tpLocalizacaoComercial").masterLink = true;
		getElement("geral.tpSituacao").masterLink = true;
		getElement("tpAcesso").masterLink = true;
	}

	function myPageLoad_cb() {
		onPageLoad_cb();
		notifyElementListeners({e:document.getElementById("tpLocalizacaoOperacional")});
		notifyElementListeners({e:document.getElementById("tpLocalizacaoComercial")});
	}
</script>

<adsm:window service="lms.tabelaprecos.manterRotasAction" onPageLoadCallBack="myPageLoad">
	<adsm:i18nLabels>
		<adsm:include key="LMS-30017"/>
		<adsm:include key="LMS-30026"/>
		<adsm:include key="2filtrosObrigatorios"/>
		<adsm:include key="zonaOrigem"/>
		<adsm:include key="zonaDestino"/>
	</adsm:i18nLabels>

	<adsm:form action="/tabelaPrecos/manterRotas" height="140" idProperty="idRotaPreco" >

		<adsm:hidden property="tpLocalizacaoOperacional" value="O" serializable="false"/>
		<adsm:hidden property="tpLocalizacaoComercial" value="C" serializable="false"/>
		<adsm:hidden property="geral.tpSituacao" value="A" serializable="false"/>
		<adsm:hidden property="tpAcesso" value="A" serializable="false" />
		<!-- Campo hidden para carregar dados da popup de uma lookup -->
		<adsm:hidden property="origemString" serializable="false"/>
		<adsm:hidden property="destinoString" serializable="false"/>
		<adsm:hidden property="tpRestricao" serializable="false" />

		<adsm:section caption="origem"/>
		<adsm:combobox
			service="lms.tabelaprecos.manterRotasAction.findZona" 
			property="zonaByIdZonaOrigem.idZona" 
			optionLabelProperty="dsZona" 
			optionProperty="idZona" 
			onchange="limpaZona('Origem')"
			label="zona" 
			labelWidth="16%" width="19%" boxWidth="130">
		</adsm:combobox>

		<adsm:lookup
			service="lms.tabelaprecos.manterRotasAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="paisByIdPaisOrigem" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			onchange="return changePais('Origem');"
			dataType="text" labelWidth="6%" width="25%" size="25" maxLength="60">

			<adsm:propertyMapping 
				addChangeListener="false"
				criteriaProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="zona.idZona"
			/>
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="zona.idZona" 
			/>
		</adsm:lookup>

		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			label="uf" 
			onDataLoadCallBack="ufOrigemOnDataLoad"
			onchange="return changeUF('Origem');"
			labelWidth="5%" width="29%" boxWidth="150">

			<adsm:propertyMapping 
				criteriaProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="pais.idPais"
			/>
		</adsm:combobox>

		<adsm:lookup 
			service="lms.tabelaprecos.manterRotasAction.findLookupFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialOrigem" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			allowInvalidCriteriaValue="false"
			minLengthForAutoPopUpSearch="3"	
			onDataLoadCallBack="filialOrigem"		
			label="filial" 
			onchange="return changeFilial('Origem');"
			onPopupSetValue="changeFilialOrigemPopup"
			dataType="text" size="5" maxLength="3" labelWidth="16%" width="37%">
			<adsm:propertyMapping modelProperty="tpAcesso" 
				criteriaProperty="tpAcesso"/>

			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" 
				relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
		 
			<adsm:textbox 
				dataType="text" 
				property="filialByIdFilialOrigem.pessoa.nmFantasia" 
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
		</adsm:lookup>

		<adsm:hidden property="_idUfOrigem" serializable="false"/>
		<adsm:hidden property="_idUfDestino" serializable="false"/>
		<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio"/>

		<adsm:lookup 
			service="lms.tabelaprecos.manterRotasAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioByIdMunicipioOrigem" 
			idProperty="municipio.idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			serializable="false"
			onchange="return municipioChange('Origem');"
			onDataLoadCallBack="municipioOrigem"
			onPopupSetValue="MunicipioOrigem_PopupSetValue"
			dataType="text" labelWidth="10%" width="37%" maxLength="60" size="30">
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioOrigem.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
		</adsm:lookup>

		<adsm:lookup 
			service="lms.tabelaprecos.manterRotasAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			property="aeroportoByIdAeroportoOrigem" 
			idProperty="idAeroporto" 
			criteriaProperty="sgAeroporto" 
			dataType="text" 
			label="aeroporto" 
			onchange="return changeAeroporto('Origem');"
			onDataLoadCallBack="aeroportoOrigem"
			onPopupSetValue="changeAeroportoOrigemPopup"
			size="5" maxLength="3" width="80%" labelWidth="16%" >

			<adsm:propertyMapping 
				criteriaProperty="geral.tpSituacao" 
				modelProperty="tpSituacao"
			/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisOrigem.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaOrigem.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfOrigem" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>

			<adsm:propertyMapping 
				relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"
			/>
			<adsm:textbox 
				dataType="text" 
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				serializable="false"
				size="40" maxLength="60" disabled="true"
			/>
		</adsm:lookup>

		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio"
			label="tipoLocalizacao" 
			autoLoad="true"
			onchange="return changeTpLocalizacao('Origem');"
			labelWidth="16%" width="30%" boxWidth="200">
			<adsm:propertyMapping
				criteriaProperty="tpLocalizacaoOperacional" 
				modelProperty="tpLocalizacao"
			/>
		</adsm:combobox>
		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioComercialOrigem.idTipoLocalizacaoMunicipio"
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="localizacaoPrecificacao" 
			autoLoad="true"
			onchange="return changeLocalizacaoComercial('Origem');"
			labelWidth="20%" width="34%" boxWidth="200">
			<adsm:propertyMapping 
				criteriaProperty="tpLocalizacaoComercial" 
				modelProperty="tpLocalizacao"
			/>
		</adsm:combobox>

		<!-- TABELA GRUPO REGIAO ORIGEM -->
		
		<adsm:lookup
			service="lms.tabelaprecos.manterRotasAction.findTabelaPreco" 
			action="tabelaPrecos/manterTabelasPreco" 
			label="tabela" 
			property="tabelaPrecoOrigem" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"  
			dataType="text" 
			size="6" 
			maxLength="8" 
			labelWidth="16%" 
			width="36%">

			<adsm:propertyMapping relatedProperty="tabelaPrecoOrigem.dsDescricao" 		
			modelProperty="dsDescricao" />	

			<adsm:textbox
			 	property="tabelaPrecoOrigem.dsDescricao"
			 	dataType="text"
			 	size="35"
			 	disabled="true" 
			 	maxLength="60"/>
		</adsm:lookup>		
        
        <adsm:combobox 
			label="grupoRegiao" 
			service="lms.tabelaprecos.manterRotasAction.findGruposRegiao"
			property="grupoRegiaoOrigem.idGrupoRegiao"
			optionLabelProperty="dsGrupoRegiao"
			optionProperty="idGrupoRegiao"
			onlyActiveValues="true"
			onchange="return changeGrupoRegiao('Origem');"
			labelWidth="14%"
			width="34%"
			boxWidth="200">
			
			<adsm:propertyMapping criteriaProperty="tabelaPrecoOrigem.idTabelaPreco" 
			modelProperty="idTabelaPreco"/>
			
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" 
			modelProperty="idUF"/>
			
		</adsm:combobox>		

		<adsm:section caption="destino" />

		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findZona" 
			property="zonaByIdZonaDestino.idZona" 
			optionLabelProperty="dsZona" 
			optionProperty="idZona" 
			onchange="limpaZona('Destino')"
			label="zona"
			labelWidth="16%" width="19%" boxWidth="130"
		/>

		<adsm:lookup
			service="lms.tabelaprecos.manterRotasAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="paisByIdPaisDestino" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			onchange="return changePais('Destino');"
			dataType="text" labelWidth="6%" width="25%" size="25" maxLength="60">

			<adsm:propertyMapping 
				addChangeListener="false"
				criteriaProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="zona.idZona"
			/>
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="zona.idZona" 
			/>
		</adsm:lookup>

		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findUnidadeFederativaByPais" 
			property="unidadeFederativaByIdUfDestino.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" 
			label="uf" 
			onDataLoadCallBack="ufDestinoOnDataLoad"
			onchange="return changeUF('Destino');"
			labelWidth="5%" width="29%" boxWidth="150">

			<adsm:propertyMapping 
				criteriaProperty="paisByIdPaisDestino.idPais" 
				modelProperty="pais.idPais"
			/>
		</adsm:combobox>

		<adsm:lookup 
			service="lms.tabelaprecos.manterRotasAction.findLookupFilial" 
			action="/municipios/manterFiliais" 
			property="filialByIdFilialDestino" 
			idProperty="idFilial" 
			criteriaProperty="sgFilial" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"			
			label="filial"
			onDataLoadCallBack="filialDestino"
			onchange="return changeFilial('Destino');"
			onPopupSetValue="changeFilialDestinoPopup"
			dataType="text" size="5" maxLength="3" labelWidth="16%" width="37%">
			<adsm:propertyMapping modelProperty="tpAcesso" 
				criteriaProperty="tpAcesso"/>

			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" 
				relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
				<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
			
			<adsm:textbox 
				dataType="text" 
				property="filialByIdFilialDestino.pessoa.nmFantasia" 
				serializable="false"
				size="30" maxLength="30" disabled="true"
			/>
		</adsm:lookup>

		<adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio"/>
		<adsm:lookup 
			service="lms.tabelaprecos.manterRotasAction.findLookupMunicipioFilial" 
			action="/municipios/manterMunicipiosAtendidos" 
			property="municipioByIdMunicipioDestino" 
			idProperty="municipio.idMunicipio" 
			criteriaProperty="municipio.nmMunicipio" 
			exactMatch="false"
			minLengthForAutoPopUpSearch="2"
			label="municipio" 
			serializable="false"
			onchange="return municipioChange('Destino');"
			onDataLoadCallBack="municipioDestino"
			onPopupSetValue="MunicipioDestino_PopupSetValue"
			dataType="text" labelWidth="10%" width="37%" maxLength="60" size="30">
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial"/>
			<adsm:propertyMapping relatedProperty="municipioByIdMunicipioDestino.idMunicipio" modelProperty="municipio.idMunicipio"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial"/>
			<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="municipio.unidadeFederativa.idUnidadeFederativa" 
			/>
			
			
		</adsm:lookup>

		<adsm:lookup 
			service="lms.tabelaprecos.manterRotasAction.findLookupAeroporto" 
			action="/municipios/manterAeroportos" 
			property="aeroportoByIdAeroportoDestino" 
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			dataType="text" 
			label="aeroporto" 
			onchange="return changeAeroporto('Destino');"
			onDataLoadCallBack="aeroportoDestino"
			onPopupSetValue="changeAeroportoDestinoPopup"
			size="5" maxLength="3" width="80%" labelWidth="16%" >

			<adsm:propertyMapping 
				criteriaProperty="geral.tpSituacao" 
				modelProperty="tpSituacao"
			/>

			<!-- Seta o Nome Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.nmPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais" 
			/>
			<!-- Seta o ID Pais automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="paisByIdPaisDestino.idPais" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.idPais" 
			/>
			<!-- Seta a Zona automaticamente -->
			<adsm:propertyMapping 
				relatedProperty="zonaByIdZonaDestino.idZona" 
				modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona" 
			/>
			
			<!-- Seta o hidden para carregar a UF relacionada -->
			<adsm:propertyMapping 
				relatedProperty="_idUfDestino" 
				modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa" 
			/>

			<adsm:propertyMapping 
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				modelProperty="pessoa.nmPessoa"
			/>

			<adsm:textbox 
				dataType="text" 
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="40" maxLength="60" disabled="true"
			/>
		</adsm:lookup>

		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio" 
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="tipoLocalizacao" 
			autoLoad="true"
			onchange="return changeTpLocalizacao('Destino');"
			labelWidth="16%" width="30%" boxWidth="200">
			<adsm:propertyMapping 
				criteriaProperty="tpLocalizacaoOperacional" 
				modelProperty="tpLocalizacao"
			/>
		</adsm:combobox>
		<adsm:combobox 
			service="lms.tabelaprecos.manterRotasAction.findTipoLocalizacao" 
			property="tipoLocalizacaoMunicipioComercialDestino.idTipoLocalizacaoMunicipio"
			optionLabelProperty="dsTipoLocalizacaoMunicipio" 
			optionProperty="idTipoLocalizacaoMunicipio" 
			label="localizacaoPrecificacao" 
			autoLoad="true"
			onchange="return changeLocalizacaoComercial('Destino');"
			labelWidth="20%" width="34%" boxWidth="200">
			<adsm:propertyMapping 
				criteriaProperty="tpLocalizacaoComercial" 
				modelProperty="tpLocalizacao"
			/>
		</adsm:combobox>
		
		<!-- TABELA GRUPO REGIAO DESTINO -->
		
		<!-- TABELA GRUPO REGIAO DESTINO -->
		
		<adsm:lookup
			service="lms.tabelaprecos.manterRotasAction.findTabelaPreco" 
			action="tabelaPrecos/manterTabelasPreco" 
			label="tabela" 
			property="tabelaPrecoDestino" 
			idProperty="idTabelaPreco"
			criteriaProperty="tabelaPrecoString"  
			dataType="text" 
			size="6" 
			maxLength="8" 
			labelWidth="16%" 
			width="36%">

			<adsm:propertyMapping relatedProperty="tabelaPrecoDestino.dsDescricao" 		
			modelProperty="dsDescricao" />	

			<adsm:textbox
			 	property="tabelaPrecoDestino.dsDescricao"
			 	dataType="text"
			 	size="35"
			 	disabled="true" 
			 	maxLength="60"/>
		</adsm:lookup>		
        
        <adsm:combobox 
			label="grupoRegiao" 
			service="lms.tabelaprecos.manterRotasAction.findGruposRegiao"
			property="grupoRegiaoDestino.idGrupoRegiao"
			optionLabelProperty="dsGrupoRegiao"
			optionProperty="idGrupoRegiao"
			onlyActiveValues="true"
			onchange="return changeGrupoRegiao('Destino');"
			labelWidth="14%"
			width="34%"
			boxWidth="200">
			
			<adsm:propertyMapping criteriaProperty="tabelaPrecoDestino.idTabelaPreco" 
			modelProperty="idTabelaPreco"/>
			
			<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" 
			modelProperty="idUF"/>			
			
		</adsm:combobox>
		
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" labelWidth="16%" label="situacao" />

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaPreco"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idRotaPreco" property="rotaPreco" gridHeight="125" unique="true" scrollBars="horizontal" rows="6">
		<adsm:gridColumn title="zonaOrigem" property="zonaByIdZonaOrigem.dsZona" width="200" />
		<adsm:gridColumn title="paisOrigem" property="paisByIdPaisOrigem.nmPais" width="200" />

		<adsm:gridColumn title="ufOrigem2" property="unidadeFederativaByIdUfOrigem.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialOrigem" property="filialByIdFilialOrigem.sgFilial" width="50" />
		<adsm:gridColumn title="municipioOrigem" property="municipioByIdMunicipioOrigem.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoOrigem" property="aeroportoByIdAeroportoOrigem.sgAeroporto" width="65" />
		<adsm:gridColumn title="localizacaoOrigem" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio" width="150" />
		<adsm:gridColumn title="localizacaoPrecificacaoOrigem" property="tipoLocalizacaoMunicipioComercialOrigem.dsTipoLocalizacaoMunicipio" width="150" />
		<adsm:gridColumn title="grupoRegiaoOrigem" property="grupoRegiaoOrigem_dsGrupoRegiao" width="150" />

		<adsm:gridColumn title="zonaDestino" property="zonaByIdZonaDestino.dsZona" width="200" />
		<adsm:gridColumn title="paisDestino" property="paisByIdPaisDestino.nmPais" width="200" />

		<adsm:gridColumn title="ufDestino2" property="unidadeFederativaByIdUfDestino.sgUnidadeFederativa" width="50" />
		<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="50" />
		<adsm:gridColumn title="municipioDestino" property="municipioByIdMunicipioDestino.nmMunicipio" width="200" />
		<adsm:gridColumn title="aeroportoDestino" property="aeroportoByIdAeroportoDestino.sgAeroporto" width="65" />
		<adsm:gridColumn title="localizacaoDestino" property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio" width="150" />
		<adsm:gridColumn title="localizacaoPrecificacaoDestino" property="tipoLocalizacaoMunicipioComercialDestino.dsTipoLocalizacaoMunicipio" width="150" />
		<adsm:gridColumn title="grupoRegiaoDestino" property="grupoRegiaoDestino.dsGrupoRegiao" width="150" />
				
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script type="text/javascript">
<!--


	function resetGrupoRegiao(tipo){
		if("Origem" == tipo){
			resetValue("tabelaPrecoOrigem.idTabelaPreco");
		}else{
			resetValue("tabelaPrecoDestino.idTabelaPreco");			
		}
	}
	
	function changeGrupoRegiao(tipo){
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		resetLocalizacao(tipo);
		resetTpLocOperacional(tipo);		
		resetTpLocComercial(tipo);		
		resetTpLocComercial(tipo);		
		return true;		
	}

	function resetPais(tipo){
		setElementValue("paisByIdPais" + tipo + ".idPais","");
		setElementValue("paisByIdPais" + tipo + ".nmPais","");
	}
	
	function limpaZona(tipo) {
		resetPais(tipo);
		setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa","");
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		setElementValue("_idUf" + tipo + "","");
		resetTpLocOperacional(tipo);
		resetTpLocComercial(tipo);
		notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
	}

	/*
	* OnChange da lookup de Pais.
	*/
	function changePais(tipo){
		var idZona = getElementValue("zonaByIdZona" + tipo + ".idZona");
		var r = true;
		eval("r = paisByIdPais" + tipo + "_nmPaisOnChangeHandler();");
		setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", "");
		changeUF(tipo);
		setElementValue("zonaByIdZona" + tipo + ".idZona", idZona);
		return r;
	}

	/*
	* OnChange da combo de UF.
	*/
	function changeUF(tipo){
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		resetTpLocOperacional(tipo);
		resetTpLocComercial(tipo);
		return true;
	}

	/*
	* Callback para combo de UF origem.
	*/
	function ufOrigemOnDataLoad_cb(dados, erro){
		unidadeFederativaByIdUfOrigem_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfOrigem");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa",idUf);
		}
	}
	
	/*
	*	OnChange do Aeroporto Destino
	*/
	function changeAeroportoDestinoPopup(data){
		changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Destino");
	}
	
	function changeAeroportoPopup(idPessoa, tipo) {
		if (idPessoa != "") {
			 findEndereco(idPessoa, tipo);
		}
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetTpLocOperacional(tipo);
		resetTpLocComercial(tipo);
	}

	/*
	* Callback para lookup de Aeroporto Destino.
	*/	
	function aeroportoDestino_cb(data) {
		if (data!=undefined && data.length == 0) {
			alert(i18NLabel.getLabel("LMS-30017"));
			return;
		}
		lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoDestino.idAeroporto"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
		resetFilial("Destino");
		resetMunicipio("Destino");
		resetTpLocOperacional("Destino");
		resetTpLocComercial("Destino");
		resetGrupoRegiao("Destino");
	}

//-->
</script>


<script type="text/javascript">

	function resetMunicipio(tipo) {
		setElementValue("municipioByIdMunicipio" + tipo + ".municipio.idMunicipio", "");
		setElementValue("municipioByIdMunicipio" + tipo + ".idMunicipio", "");
		setElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio", "");
			
	}
	
	/*
	* Callback para combo de UF destino.
	*/
	function ufDestinoOnDataLoad_cb(dados, erro){
		unidadeFederativaByIdUfDestino_idUnidadeFederativa_cb(dados);
		var idUf = getElementValue("_idUfDestino");
		if (idUf != null && idUf != ""){
			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa",idUf);
		}
	}


	/*
	* OnChange da lookup de Aeroporto.
	*/
	function changeAeroporto(tipo){
		var r = true;
		if (getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto") != "") {
			eval("r = aeroportoByIdAeroporto" + tipo + "_sgAeroportoOnChangeHandler()");
		} else {
			resetAeroporto(tipo);
		}
		return r;
	}


	/*
	*	OnChange do Aeroporto origem
	*/
	function changeAeroportoOrigemPopup(data){
		changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Origem");
	}
	

	/*
	* Callback para lookup de Aeroporto origem.
	*/	
	function aeroportoOrigem_cb(data) {
		if (data!=undefined && data.length == 0){
			alert(i18NLabel.getLabel("LMS-30017"));
			return;
		}
		lookupExactMatch({e:document.getElementById("aeroportoByIdAeroportoOrigem.idAeroporto"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
		resetFilial("Origem");
		resetMunicipio("Origem");
		resetTpLocOperacional("Origem");
		resetTpLocComercial("Origem");
		resetGrupoRegiao("Origem");		
	}

	function resetFilial(tipo) {
		setElementValue("filialByIdFilial" + tipo + ".idFilial", ""); 
		setElementValue("filialByIdFilial" + tipo + ".pessoa.nmFantasia", ""); 
		setElementValue("filialByIdFilial" + tipo + ".sgFilial", ""); 
	}
	
	function resetAeroporto(tipo) {
		setElementValue("aeroportoByIdAeroporto" + tipo + ".idAeroporto", ""); 
		setElementValue("aeroportoByIdAeroporto" + tipo + ".pessoa.nmPessoa", ""); 
		setElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto", ""); 
	}

	/*
	* OnChange da combo de Tipo de Localização.
	*/
	function changeTpLocalizacao(tipo){
		resetLocalizacao(tipo)
		resetTpLocComercial(tipo)
		resetGrupoRegiao(tipo);		
		return true;
	}
	/*
	* OnChange da combo de Localizacao Precificacao.
	*/
	function changeLocalizacaoComercial(tipo){
		resetLocalizacao(tipo);
		resetTpLocOperacional(tipo);
		resetGrupoRegiao(tipo);
		return true;
	}

	function resetLocalizacao(tipo){
		resetFilial(tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		return true;
	}
	function resetTpLocOperacional(tipo){
		setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao"+tipo+".idTipoLocalizacaoMunicipio", "");
	}
	function resetTpLocComercial(tipo){
		setElementValue("tipoLocalizacaoMunicipioComercial"+tipo+".idTipoLocalizacaoMunicipio", "");
	}
</script>


<script type="text/javascript">
<!--

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	*
	* ROTINAS PARA CONTROLE DAS LOOKUPS DE FILIAL E MUNICIPIO ORIGEM
	*
	** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/*
	*	OnChange da Filial
	*/
	function changeFilial(tipo){
		var r = true;
		if (getElementValue("filialByIdFilial" + tipo + ".sgFilial") == "") {
			resetFilial(tipo);
		} else {
			eval("r = filialByIdFilial" + tipo + "_sgFilialOnChangeHandler()");
		}
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		resetTpLocOperacional(tipo);
		resetTpLocComercial(tipo);
		return r;
	}
	
	/*
	*	OnChange da Filial Origem
	*/
	function changeFilialOrigemPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Origem");
	}
	
	function changeFilialPopup(idFilial, tipo) {
		if (idFilial != "")
			findEndereco(idFilial, tipo);
		resetMunicipio(tipo);
		resetAeroporto(tipo);
		resetTpLocOperacional(tipo);
		resetTpLocComercial(tipo);
	}
	
	function configEndereco(dados, tipo) {
		var uf = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa");
		var	idPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.idPais");
		var nmPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.nmPais");
		var	idZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.idZona");
		setElementValue("_idUf" + tipo, uf);
		setElementValue("paisByIdPais" + tipo + ".idPais", idPais);
		setElementValue("paisByIdPais" + tipo + ".nmPais", nmPais);
		setElementValue("zonaByIdZona" + tipo + ".idZona", idZona);
	}
	
	function enderecoOrigem_cb(dados, erros) {
		configEndereco(dados, "Origem");
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
	}
	

	/*
	* Callback para lookup de f origem.
	*/	
	function filialOrigem_cb(data) {
		if (data!=undefined && data.length == 0){
			alert(i18NLabel.getLabel("LMS-30017"));
			return;
		}
		lookupExactMatch({e:document.getElementById("filialByIdFilialOrigem.idFilial"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
	}

	
	/*
	* OnChange da lookup de Municipio
	*/
	function municipioChange(tipo){
		var r = true;
		if (getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio") != ""){
			eval("r = municipioByIdMunicipio"+ tipo + "_municipio_nmMunicipioOnChangeHandler()");
		} else {
			resetMunicipio(tipo);
		}
		return r;
	}

	/*
	* OnPopupSetValue da lookup de Municipio origem.
	*/
	function MunicipioOrigem_PopupSetValue(dados){
		configEndereco(dados, "Origem");
		eventMunicipio("Origem");
	}

	function municipioOrigem_cb(data) 
	{
		lookupExactMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data, callBack:'municipioOrigemLikeEndMatch'});
		if (data != undefined && data.length == 1) 	{
			eventMunicipio("Origem");
		}
	}

	function municipioOrigemLikeEndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Origem");
		}
	}
//-->
</script>


<script type="text/javascript">
<!--

	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
	*
	* ROTINAS PARA CONTROLE DAS LOOKUPS DE FILIAL E MUNICIPIO DESTINO
	*
	** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

	/*
	* Callback para lookup de f origem.
	*/	
	function filialDestino_cb(data) {
		if (data!=undefined && data.length == 0){
			alert(i18NLabel.getLabel("LMS-30017"));
			return;
		}
		lookupExactMatch({e:document.getElementById("filialByIdFilialDestino.idFilial"), data:data });
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	}
	
	
	function findEndereco(idPessoa, tipo) {
		 var sdo = createServiceDataObject("lms.tabelaprecos.manterRotasAction.findEndereco", "endereco" + tipo, {idPessoa:idPessoa});
		xmit({serviceDataObjects:[sdo]});
	}
	
	/*
	*	OnChange da Filial Destino
	*/
	function changeFilialDestinoPopup(data){
		changeFilialPopup(getNestedBeanPropertyValue(data,"idFilial"), "Destino");		
	}
	
	function enderecoDestino_cb(dados, erros) {
		configEndereco(dados, "Destino");
		notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
	}
	
	/*
	* OnPopupSetValue da lookup de Municipio destino.
	*/
	function MunicipioDestino_PopupSetValue(dados){
		configEndereco(dados, "Destino");
		eventMunicipio("Destino");
	}

	function municipioDestino_cb(data) {
		lookupExactMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data, callBack:'municipioDestinoLikeEndMatch'});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Destino");
		}
	}

	function municipioDestinoLikeEndMatch_cb(data){
		lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data});
		if (data != undefined && data.length == 1) {
			eventMunicipio("Destino");
		}
	}

	function eventMunicipio(tipo){
		notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
		resetAeroporto(tipo);
		resetTpLocOperacional(tipo);
		resetTpLocComercial(tipo);
		resetGrupoRegiao(tipo);		
	}
//-->
</script>
