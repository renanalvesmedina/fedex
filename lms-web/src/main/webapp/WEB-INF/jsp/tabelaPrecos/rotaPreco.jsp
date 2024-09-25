<%-- ATENÇÃO 
	Para utilizar essa JSP, deve-se incluir a variavel Scriptlet na pagina que a chama.
	Sendo true para telas Cad e false para List;
	Ex.: <% Boolean blActiveValues = false; %>	
--%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:hidden property="geral.tpSituacao" value="A" serializable="false"/>

<adsm:section caption="origem"/>

<adsm:hidden property="zonaByIdZonaOrigem.dsZona" />
<adsm:combobox
	label="zona"
	property="zonaByIdZonaOrigem.idZona"
	optionProperty="idZona"
	optionLabelProperty="dsZona"
	service="lms.tabelaprecos.manterRotasAction.findZona"
	onchange="limpaZona('Origem', this)"
	onlyActiveValues="<%=blActiveValues%>"
	labelWidth="16%"
	width="19%"
	boxWidth="130"
>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.dsZona" modelProperty="dsZona"/>
</adsm:combobox>

<adsm:lookup
	label="pais"
	property="paisByIdPaisOrigem"
	idProperty="idPais"
	criteriaProperty="nmPais"
	service="lms.tabelaprecos.manterRotasAction.findLookupPais"
	action="/municipios/manterPaises"
	minLengthForAutoPopUpSearch="3"
	exactMatch="false"
	onchange="return changePais('Origem');"
	dataType="text"
	labelWidth="6%"
	width="25%"
	size="25"
	maxLength="60"
>
	<adsm:propertyMapping criteriaProperty="geral.tpSituacao" modelProperty="tpSituacao"/>
	<adsm:propertyMapping addChangeListener="false" criteriaProperty="zonaByIdZonaOrigem.idZona" modelProperty="zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.idZona" modelProperty="zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.dsZona" modelProperty="zona.dsZona"/>
</adsm:lookup>

<adsm:hidden property="unidadeFederativaByIdUfOrigem.siglaDescricao"/>
<adsm:combobox
	label="uf"
	service="lms.tabelaprecos.manterRotasAction.findUnidadeFederativaByPais"
	property="unidadeFederativaByIdUfOrigem.idUnidadeFederativa"
	optionLabelProperty="siglaDescricao"
	optionProperty="idUnidadeFederativa"
	onDataLoadCallBack="ufOrigemOnDataLoad"
	onchange="changeUF('Origem');"
	onlyActiveValues="<%=blActiveValues%>"
	labelWidth="5%"
	width="29%"
	boxWidth="150"
>
	<adsm:propertyMapping criteriaProperty="paisByIdPaisOrigem.idPais" modelProperty="pais.idPais"/>
	<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfOrigem.siglaDescricao" modelProperty="siglaDescricao"/>
</adsm:combobox>

<adsm:lookup
	property="filialByIdFilialOrigem"
	idProperty="idFilial"
	criteriaProperty="sgFilial"
	label="filial"
	service="lms.tabelaprecos.manterRotasAction.findLookupFilial"
	action="/municipios/manterFiliais"
	exactMatch="false"
	minLengthForAutoPopUpSearch="3"
	onDataLoadCallBack="filialOrigem"
	onchange="return changeFilial('Origem');"
	onPopupSetValue="changeFilialOrigemPopup"
	dataType="text"
	size="5"
	maxLength="3"
	labelWidth="16%"
	width="37%"
>

	<adsm:propertyMapping modelProperty="tpAcesso" criteriaProperty="tpAcesso"/>
	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"/>
	<!-- Seta o Nome Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.nmPais" modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"/>
	<!-- Seta o ID Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.idPais" modelProperty="endereco.municipio.unidadeFederativa.pais.idPais"/>
	<!-- Seta a Zona automaticamente -->
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.idZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.dsZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.dsZona"/>
	<!-- Seta o hidden para carregar a UF relacionada -->
	<adsm:propertyMapping relatedProperty="_idUfOrigem" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa"/>

	<adsm:textbox 
		dataType="text" 
		property="filialByIdFilialOrigem.pessoa.nmFantasia" 
		serializable="false"
		size="30"
		maxLength="30"
		disabled="true"
	/>
</adsm:lookup>

<adsm:hidden property="_idUfOrigem" serializable="false"/>
<adsm:hidden property="_idUfDestino" serializable="false"/>
<adsm:hidden property="municipioByIdMunicipioOrigem.idMunicipio"/>

<!-- LMS-6166 - trazer somente municípios vigentes ou não (placeholder) -->
<adsm:hidden property="municipioByIdMunicipioOrigem.vigentes" serializable="false" />
<adsm:hidden property="municipioByIdMunicipioDestino.vigentes" serializable="false" />

<adsm:lookup 
	label="municipio" 
	property="municipioByIdMunicipioOrigem" 
	idProperty="municipio.idMunicipio" 
	criteriaProperty="municipio.nmMunicipio" 
	service="lms.tabelaprecos.manterRotasAction.findLookupMunicipioFilial" 
	action="/municipios/manterMunicipiosAtendidos" 
	exactMatch="false"
	minLengthForAutoPopUpSearch="2"
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
	<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
	<!-- Seta o ID Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais"/>
	<!-- Seta a Zona automaticamente -->
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.idZona" modelProperty="municipio.unidadeFederativa.pais.zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.dsZona" modelProperty="municipio.unidadeFederativa.pais.zona.dsZona"/>
	<!-- Seta o hidden para carregar a UF relacionada -->
	<adsm:propertyMapping relatedProperty="_idUfOrigem" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>

	<!-- LMS-6166 - trazer somente municípios vigentes -->
	<adsm:propertyMapping
		modelProperty="vigentes"
		criteriaProperty="municipioByIdMunicipioOrigem.vigentes" />

</adsm:lookup>

<adsm:lookup
	label="aeroporto"
	property="aeroportoByIdAeroportoOrigem"
	idProperty="idAeroporto"
	criteriaProperty="sgAeroporto"
	service="lms.tabelaprecos.manterRotasAction.findLookupAeroporto"
	action="/municipios/manterAeroportos"
	dataType="text"
	onchange="return changeAeroporto('Origem');"
	onDataLoadCallBack="aeroportoOrigem"
	onPopupSetValue="changeAeroportoOrigemPopup"
	size="5"
	maxLength="3"
	width="80%"
	labelWidth="16%"
>
	<adsm:propertyMapping criteriaProperty="geral.tpSituacao" modelProperty="tpSituacao"/>
	<!-- Seta a Zona automaticamente -->
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.idZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaOrigem.dsZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.dsZona"/>
	<!-- Seta Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.idPais" modelProperty="endereco.municipio.unidadeFederativa.pais.idPais"/>
	<adsm:propertyMapping relatedProperty="paisByIdPaisOrigem.nmPais" modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"/>
	<!-- Seta UF automaticamente -->
	<adsm:propertyMapping relatedProperty="_idUfOrigem" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa"/>
	<adsm:propertyMapping relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	<adsm:textbox
		dataType="text"
		property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
		serializable="false"
		size="30"
		maxLength="30"
		disabled="true"
	/>
</adsm:lookup>

<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio"/>
<adsm:combobox
	label="tipoLocalizacao"
	property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.idTipoLocalizacaoMunicipio"
	optionLabelProperty="dsTipoLocalizacaoMunicipio"
	optionProperty="idTipoLocalizacaoMunicipio"
	service="lms.tabelaprecos.manterRotasAction.findTipoLocalizacaoOperacional"
	onchange="return changeTpLocalizacao('Origem');"
	onlyActiveValues="<%=blActiveValues%>"
	labelWidth="16%"
	width="37%"
	boxWidth="200"
>
	<adsm:propertyMapping relatedProperty="tipoLocalizacaoMunicipioByIdTipoLocalizacaoOrigem.dsTipoLocalizacaoMunicipio" modelProperty="dsTipoLocalizacaoMunicipio"/>
</adsm:combobox>


<adsm:hidden property="grupoRegiaoOrigem.dsGrupoRegiao"/>
<adsm:combobox 
			label="grupoRegiao"
			property="grupoRegiaoOrigem.idGrupoRegiao"
			optionLabelProperty="dsGrupoRegiao"
			optionProperty="idGrupoRegiao"
			onchange="changeGrupoRegiao('Origem');"			
			service="lms.tabelaprecos.manterRotasAction.findByUfGrupoRegiao"
			onDataLoadCallBack="grupoRegiaoOrigemOnDataLoad"
			onlyActiveValues="<%=blActiveValues%>"
			labelWidth="10%"
			width="37%"
			boxWidth="150"						
>
	<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfOrigem.idUnidadeFederativa" modelProperty="unidadeFederativaByIdUf.idUnidadeFederativa"/>	
	<adsm:propertyMapping relatedProperty="grupoRegiaoOrigem.dsGrupoRegiao" modelProperty="dsGrupoRegiao"/>
</adsm:combobox>

<adsm:section caption="destino" />

<adsm:hidden property="zonaByIdZonaDestino.dsZona"/>
<adsm:combobox
	label="zona"
	property="zonaByIdZonaDestino.idZona"
	optionLabelProperty="dsZona"
	optionProperty="idZona"
	service="lms.tabelaprecos.manterRotasAction.findZona"
	onchange="limpaZona('Destino', this)"
	onlyActiveValues="<%=blActiveValues%>"
	labelWidth="16%"
	width="19%"
	boxWidth="130"
>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.dsZona" modelProperty="dsZona"/>
</adsm:combobox>

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
	dataType="text"
	labelWidth="6%"
	width="25%"
	size="25"
	maxLength="60"
>
	<adsm:propertyMapping criteriaProperty="geral.tpSituacao" modelProperty="tpSituacao"/>
	<adsm:propertyMapping addChangeListener="false" criteriaProperty="zonaByIdZonaDestino.idZona" modelProperty="zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.idZona" modelProperty="zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.dsZona" modelProperty="zona.dsZona"/>
</adsm:lookup>

<adsm:hidden property="unidadeFederativaByIdUfDestino.siglaDescricao"/>
<adsm:combobox
	label="uf"
	property="unidadeFederativaByIdUfDestino.idUnidadeFederativa"
	optionLabelProperty="siglaDescricao"
	optionProperty="idUnidadeFederativa"
	service="lms.tabelaprecos.manterRotasAction.findUnidadeFederativaByPais"
	onDataLoadCallBack="ufDestinoOnDataLoad"
	onchange="changeUF('Destino');"
	onlyActiveValues="<%=blActiveValues%>"
	labelWidth="5%"
	width="29%"
	boxWidth="150"
>
	<adsm:propertyMapping criteriaProperty="paisByIdPaisDestino.idPais" modelProperty="pais.idPais"/>
	<adsm:propertyMapping relatedProperty="unidadeFederativaByIdUfDestino.siglaDescricao" modelProperty="siglaDescricao"/>
</adsm:combobox>

<adsm:lookup
	label="filial"
	property="filialByIdFilialDestino"
	idProperty="idFilial"
	criteriaProperty="sgFilial"
	service="lms.tabelaprecos.manterRotasAction.findLookupFilial"
	action="/municipios/manterFiliais"
	exactMatch="false"
	minLengthForAutoPopUpSearch="3"
	onDataLoadCallBack="filialDestino"
	onchange="return changeFilial('Destino');"
	onPopupSetValue="changeFilialDestinoPopup"
	dataType="text"
	size="5"
	maxLength="3"
	labelWidth="16%"
	width="37%"
>
	<adsm:propertyMapping modelProperty="tpAcesso" criteriaProperty="tpAcesso"/>
	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"/>
	<!-- Seta o Nome Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.nmPais" modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"/>
	<!-- Seta o ID Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.idPais" modelProperty="endereco.municipio.unidadeFederativa.pais.idPais"/>
	<!-- Seta a Zona automaticamente -->
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.idZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.dsZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.dsZona"/>
	<!-- Seta o hidden para carregar a UF relacionada -->
	<adsm:propertyMapping relatedProperty="_idUfDestino" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa"/>
	<adsm:textbox
		dataType="text"
		property="filialByIdFilialDestino.pessoa.nmFantasia"
		serializable="false"
		size="30"
		maxLength="30"
		disabled="true"
	/>
</adsm:lookup>

<adsm:hidden property="municipioByIdMunicipioDestino.idMunicipio"/>
<adsm:lookup
	label="municipio"
	property="municipioByIdMunicipioDestino"
	idProperty="municipio.idMunicipio"
	criteriaProperty="municipio.nmMunicipio"
	service="lms.tabelaprecos.manterRotasAction.findLookupMunicipioFilial"
	action="/municipios/manterMunicipiosAtendidos"
	exactMatch="false"
	minLengthForAutoPopUpSearch="2"
	serializable="false"
	onchange="return municipioChange('Destino');"
	onDataLoadCallBack="municipioDestino"
	onPopupSetValue="MunicipioDestino_PopupSetValue"
	dataType="text"
	labelWidth="10%"
	width="37%"
	maxLength="60"
	size="30"
>
	<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" addChangeListener="false"/>
	<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial" addChangeListener="false" inlineQuery="false"/>
	<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" addChangeListener="false" inlineQuery="false"/>
	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial"/>
	<adsm:propertyMapping relatedProperty="municipioByIdMunicipioDestino.idMunicipio" modelProperty="municipio.idMunicipio"/>
	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.sgFilial" modelProperty="filial.sgFilial"/>
	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia"/>
	<!-- Seta Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.nmPais" modelProperty="municipio.unidadeFederativa.pais.nmPais"/>
	<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.idPais" modelProperty="municipio.unidadeFederativa.pais.idPais"/>
	<!-- Seta a Zona automaticamente -->
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.idZona" modelProperty="municipio.unidadeFederativa.pais.zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.dsZona" modelProperty="municipio.unidadeFederativa.pais.zona.dsZona"/>
	<!-- Seta o hidden para carregar a UF relacionada -->
	<adsm:propertyMapping relatedProperty="_idUfDestino" modelProperty="municipio.unidadeFederativa.idUnidadeFederativa"/>

	<!-- LMS-6166 - trazer somente municípios vigentes -->
	<adsm:propertyMapping
		modelProperty="vigentes"
		criteriaProperty="municipioByIdMunicipioDestino.vigentes" />

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
	size="5"
	maxLength="3"
	width="80%"
	labelWidth="16%"
>
	<adsm:propertyMapping criteriaProperty="geral.tpSituacao" modelProperty="tpSituacao"/>
	<!-- Seta Pais automaticamente -->
	<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.idPais" modelProperty="endereco.municipio.unidadeFederativa.pais.idPais"/>
	<adsm:propertyMapping relatedProperty="paisByIdPaisDestino.nmPais" modelProperty="endereco.municipio.unidadeFederativa.pais.nmPais"/>
	<!-- Seta Zona automaticamente -->
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.idZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.idZona"/>
	<adsm:propertyMapping relatedProperty="zonaByIdZonaDestino.dsZona" modelProperty="endereco.municipio.unidadeFederativa.pais.zona.dsZona"/>
	<!-- Seta o hidden para carregar a UF relacionada -->
	<adsm:propertyMapping relatedProperty="_idUfDestino" modelProperty="endereco.municipio.unidadeFederativa.idUnidadeFederativa"/>
	<adsm:propertyMapping relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
	<adsm:textbox
		dataType="text"
		property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"
		serializable="false"
		size="30"
		maxLength="30"
		disabled="true"
	/>
</adsm:lookup>

<adsm:hidden property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio"/>
<adsm:combobox
	label="tipoLocalizacao"
	service="lms.tabelaprecos.manterRotasAction.findTipoLocalizacaoOperacional"
	property="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.idTipoLocalizacaoMunicipio"
	optionLabelProperty="dsTipoLocalizacaoMunicipio"
	optionProperty="idTipoLocalizacaoMunicipio"
	onchange="return changeTpLocalizacao('Destino');"
	onlyActiveValues="<%=blActiveValues%>"
	labelWidth="16%"
	width="37%"
	boxWidth="200"
>
	<adsm:propertyMapping relatedProperty="tipoLocalizacaoMunicipioByIdTipoLocalizacaoDestino.dsTipoLocalizacaoMunicipio" modelProperty="dsTipoLocalizacaoMunicipio"/>
</adsm:combobox>

<adsm:hidden property="grupoRegiaoDestino.dsGrupoRegiao"/>
<adsm:combobox			
			label="grupoRegiao"
			property="grupoRegiaoDestino.idGrupoRegiao"
			optionLabelProperty="dsGrupoRegiao"
			optionProperty="idGrupoRegiao"
			onchange="changeGrupoRegiao('Destino');"			
			service="lms.tabelaprecos.manterRotasAction.findByUfGrupoRegiao"
			onDataLoadCallBack="grupoRegiaoDestinoOnDataLoad"
			onlyActiveValues="<%=blActiveValues%>"
			labelWidth="10%"
			width="37%"
			boxWidth="150"						
>
	<adsm:propertyMapping criteriaProperty="unidadeFederativaByIdUfDestino.idUnidadeFederativa" modelProperty="unidadeFederativaByIdUf.idUnidadeFederativa"/>	
	<adsm:propertyMapping relatedProperty="grupoRegiaoDestino.dsGrupoRegiao" modelProperty="dsGrupoRegiao"/>
</adsm:combobox>