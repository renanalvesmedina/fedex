var idGrupoRegiaoOrigem = '';
var idGrupoRegiaoDestino = '';
var idUFFilial = '';

/* funcao que manipula o foco apos sair do campo do municipio */
//document.getElementById("municipioByIdMunicipioDestino.municipio.nmMunicipio").onblur = function() {
//	if(document.getElementById("idUfMunicipio")!=undefined && isBlank(getElementValue("municipioByIdMunicipioDestino.municipio.nmMunicipio")) && !isBlank(getElementValue("filialByIdFilialDestino.idFilial"))){
//		if(idUFFilial != undefined){
//			setElementValue("_idUfDestino", idUFFilial);
//			setNestedBeanPropertyValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", idUFFilial);	
//			setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", idUFFilial);
//		}
//	}
//}

function limpaZona(tipo, combo) {
	resetPais(tipo);
	resetUF(tipo);
	resetFilial(tipo);
	resetMunicipio(tipo);
	resetAeroporto(tipo);
	setElementValue("_idUf" + tipo + "","");
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio","");
	notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
	if(combo) {
		comboboxChange({e:combo});
	}
}

function resetPais(tipo) {
	setElementValue("paisByIdPais" + tipo + ".idPais","");
	setElementValue("paisByIdPais" + tipo + ".nmPais","");
}

/*
* OnChange da lookup de Pais.
*/
function changePais(tipo) {
	var idZona = getElementValue("zonaByIdZona" + tipo + ".idZona");
	var dsZona = getElementValue("zonaByIdZona" + tipo + ".dsZona");
	var r = true;
	eval("r = paisByIdPais" + tipo + "_nmPaisOnChangeHandler();");
	resetUF(tipo);
	changeUF(tipo);
	setElementValue("zonaByIdZona" + tipo + ".idZona", idZona);
	setElementValue("zonaByIdZona" + tipo + ".dsZona", dsZona);
	return r;
}

/*
* OnChange da combo de UF.
*/
function changeUF(tipo) {	
	comboboxChange({e:document.getElementById("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa")});		
	resetFilial(tipo);
	resetMunicipio(tipo);
	resetAeroporto(tipo);
	resetGrupoRegiao(tipo);
	resetTpLocalizacao(tipo);
	
	return true;
}

function resetUF(tipo) {
	setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", "");
}

function resetGrupoRegiao(tipo) {
	setElementValue("grupoRegiao" + tipo + ".idGrupoRegiao", "");
}

/*
* OnChange da combo de Grupo Regiao
*/
function changeGrupoRegiao(tipo) {
	comboboxChange({e:document.getElementById("grupoRegiao" + tipo + ".idGrupoRegiao")});		
	
	resetTpLocalizacao(tipo);
	resetFilial(tipo);
	resetMunicipio(tipo);
	resetAeroporto(tipo);
	
	return true;
}

function grupoRegiaoOrigemOnDataLoad_cb(data, error) {				
	grupoRegiaoOrigem_idGrupoRegiao_cb(data);	
	
	if(idGrupoRegiaoOrigem+'' != '') {
		setElementValue("grupoRegiaoOrigem.idGrupoRegiao", idGrupoRegiaoOrigem);	
		comboboxChange({e:document.getElementById("grupoRegiaoOrigem.idGrupoRegiao")});			
	}	
}

function grupoRegiaoDestinoOnDataLoad_cb(data, error) {				
	grupoRegiaoDestino_idGrupoRegiao_cb(data);
	if(idGrupoRegiaoDestino+'' != '') {
		setElementValue("grupoRegiaoDestino.idGrupoRegiao", idGrupoRegiaoDestino);
		comboboxChange({e:document.getElementById("grupoRegiaoDestino.idGrupoRegiao")});
	}
}

/*
* Callback para combo de UF destino.
*/
function ufDestinoOnDataLoad_cb(dados, erro) {
	unidadeFederativaByIdUfDestino_idUnidadeFederativa_cb(dados);
	var idUf = getElementValue("_idUfDestino");
	if (idUf != null && idUf != ""){
		setElementValue("unidadeFederativaByIdUfDestino.idUnidadeFederativa", idUf);
	}
	//notifyElementListeners({e:document.getElementById("unidadeFederativaByIdUfDestino.idUnidadeFederativa")});
}

/*
* Callback para combo de UF origem.
*/
function ufOrigemOnDataLoad_cb(dados, erro){
	unidadeFederativaByIdUfOrigem_idUnidadeFederativa_cb(dados);
	var idUf = getElementValue("_idUfOrigem");
	if(idUf != null && idUf != "") {
		setElementValue("unidadeFederativaByIdUfOrigem.idUnidadeFederativa",idUf);
	}
	//notifyElementListeners({e:document.getElementById("unidadeFederativaByIdUfOrigem.idUnidadeFederativa")});
}

//Callback para lookup de filial de origem.
function filialOrigem_cb(data) {
	filialCallback("Origem", data);
}

//Callback para lookup de filial de destino.
function filialDestino_cb(data) {
	filialCallback("Destino", data);
}

function filialCallback(tipo, data) {
	if(data != undefined && data.length == 0) {
		alertI18nMessage("LMS-30017");
		return;
	}
	lookupExactMatch({e:document.getElementById("filialByIdFilial" + tipo + ".idFilial"), data:data});
	notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});
}


function findEndereco(idPessoa, tipo) {
	var sdo = createServiceDataObject("lms.vendas.manterParametrosClienteAction.findEndereco", "endereco" + tipo, {idPessoa:idPessoa});
	xmit({serviceDataObjects:[sdo]});
}

/*
*	OnChange da Filial (Origem e Destino);
*/
function changeFilial(tipo) {
	var r = true;
	if(getElementValue("filialByIdFilial" + tipo + ".sgFilial") == "") {
		resetFilial(tipo);
	} else {
		eval("r = filialByIdFilial" + tipo + "_sgFilialOnChangeHandler()");
	}
	resetMunicipio(tipo);
	resetAeroporto(tipo);
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
	return r;
}

/*
* OnPopupSetValue da Filial Destino
*/
function changeFilialDestinoPopup(data){
	changeFilialPopup(getNestedBeanPropertyValue(data, "idFilial"), "Destino");		
}

/*
* OnPopupSetValue da Filial Origem
*/
function changeFilialOrigemPopup(data){
	changeFilialPopup(getNestedBeanPropertyValue(data, "idFilial"), "Origem");
}

/*
* OnPopupSetValue da Filial (Origem e Destino)
*/
function changeFilialPopup(idFilial, tipo) {
	if(idFilial != "") {
		findEndereco(idFilial, tipo);
	}
	resetMunicipio(tipo);
	resetAeroporto(tipo);
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
}

function resetFilial(tipo) {
	setElementValue("filialByIdFilial" + tipo + ".idFilial", ""); 
	setElementValue("filialByIdFilial" + tipo + ".pessoa.nmFantasia", ""); 
	setElementValue("filialByIdFilial" + tipo + ".sgFilial", ""); 
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

//OnPopupSetValue da lookup de Municipio destino.
function MunicipioDestino_PopupSetValue(dados) {
	configEndereco(dados, "Destino");
	eventMunicipio("Destino");
}

function municipioOrigem_cb(data) {
	lookupExactMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data, callBack:'municipioOrigemLikeEndMatch'});
	if (data != undefined && data.length == 1) 	{
    	eventMunicipio("Origem");
	}
}

function municipioDestino_cb(data) {
	lookupExactMatch({e:document.getElementById("municipioByIdMunicipioDestino.municipio.idMunicipio"), data:data, callBack:'municipioDestinoLikeEndMatch'});
	if (data != undefined && data.length == 1) {
		eventMunicipio("Destino");
	}
}

function municipioOrigemLikeEndMatch_cb(data){
	lookupLikeEndMatch({e:document.getElementById("municipioByIdMunicipioOrigem.municipio.idMunicipio"), data:data});
	if (data != undefined && data.length == 1) {
	  eventMunicipio("Origem");
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
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", "");
}

function resetMunicipio(tipo) {
	setElementValue("municipioByIdMunicipio" + tipo + ".municipio.idMunicipio", "");
	setElementValue("municipioByIdMunicipio" + tipo + ".idMunicipio", "");
	setElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio", "");
}

/*
* OnChange da lookup de Aeroporto.
*/
function changeAeroporto(tipo) {
	var r = true;
	if (getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto") != "") {		
		eval("r = aeroportoByIdAeroporto" + tipo + "_sgAeroportoOnChangeHandler()");		
	} else {
		resetAeroporto(tipo);
	}
	return r;
}

/*
* OnPopupSetValue do Aeroporto origem
*/
function changeAeroportoOrigemPopup(data){
	changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Origem");
}

/*
* OnPopupSetValue do Aeroporto Destino
*/
function changeAeroportoDestinoPopup(data){
	changeAeroportoPopup(getNestedBeanPropertyValue(data,"idAeroporto"), "Destino");
}

/*
* OnPopupSetValue do Aeroporto (Origem e Destino)
*/
function changeAeroportoPopup(idPessoa, tipo) {	
	if (idPessoa != "") {
		findEndereco(idPessoa, tipo);
	}	
	resetFilial(tipo);
	resetMunicipio(tipo);
	resetTpLocalizacao(tipo);
	resetGrupoRegiao(tipo);
}


/*
* Callback para lookup de Aeroporto origem.
*/	
function aeroportoOrigem_cb(data) {
	aeroportoCallback("Origem", data);
}

/*
* Callback para lookup de Aeroporto Destino.
*/	
function aeroportoDestino_cb(data) {
	aeroportoCallback("Destino", data);
}

/*
* Callback para lookup de Aeroporto (Origem e Destino)
*/	
function aeroportoCallback(tipo, data) {	
	if( data != undefined && data.length == 0 ) {
		alertI18nMessage("LMS-30017");
		return;
	}
	lookupExactMatch({e:document.getElementById("aeroportoByIdAeroporto" + tipo + ".idAeroporto"), data:data});
	notifyElementListeners({e:document.getElementById("paisByIdPais" + tipo + ".idPais")});				
	
	resetFilial(tipo);
	resetMunicipio(tipo);
	resetTpLocalizacao(tipo);
	resetGrupoRegiao(tipo);
}

function resetAeroporto(tipo) {
	setElementValue("aeroportoByIdAeroporto" + tipo + ".idAeroporto", ""); 
	setElementValue("aeroportoByIdAeroporto" + tipo + ".pessoa.nmPessoa", ""); 
	setElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto", ""); 
}

/*
* OnChange da combo de Tipo de Localiza��o.
*/
function changeTpLocalizacao(tipo) {
	resetFilial(tipo);
	resetMunicipio(tipo);
	resetAeroporto(tipo);
	resetGrupoRegiao(tipo);
	
	return true;
}

function resetTpLocalizacao(tipo) {
	setElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio", ""); 
}

function enderecoOrigem_cb(dados, erros) {
	configEndereco(dados, "Origem");
	notifyElementListeners({e:document.getElementById("paisByIdPaisOrigem.idPais")});
}

function enderecoDestino_cb(dados, erros) {
	configEndereco(dados, "Destino");
	notifyElementListeners({e:document.getElementById("paisByIdPaisDestino.idPais")});
}

function configEndereco(dados, tipo) {
	var m = document.getElementById("idUfMunicipio");
	if(m == undefined){
		var idFilialUF = getNestedBeanPropertyValue(dados, "filial.pessoa.enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa");
		if(idFilialUF != undefined) {
			setNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa", idFilialUF);
		} else {
			idFilialUF = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa");
		} 
	} else if (tipo == "Destino" || tipo == "Origem") {
		idUFFilial = getNestedBeanPropertyValue(dados, "filial.pessoa.enderecoPessoa.municipio.unidadeFederativa.idUnidadeFederativa");
		idFilialUF = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.idUnidadeFederativa");
		if (idFilialUF != undefined) {
			setNestedBeanPropertyValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", idFilialUF);
			setElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa", idFilialUF);
		}
	}
	var	idPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.idPais");
	var nmPais = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.nmPais");
	var	idZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.idZona");
	var	dsZona = getNestedBeanPropertyValue(dados, "municipio.unidadeFederativa.pais.zona.dsZona");
	
	setElementValue("_idUf" + tipo, idFilialUF);
	
	setElementValue("paisByIdPais" + tipo + ".idPais", idPais);
	setElementValue("paisByIdPais" + tipo + ".nmPais", nmPais);
	setElementValue("zonaByIdZona" + tipo + ".idZona", idZona);
	setElementValue("zonaByIdZona" + tipo + ".dsZona", dsZona);
}

/*
* Valida��o de rotas.
*
* Fun��o utilizada para verificar se a rota � v�lida antes de
* persistir no banco de dados.
*/
function validaRotas() {
	return (validaRota("Origem") && validaRota("Destino"));
}

function validaRota(tipo){
	if(tipo != "Origem" && tipo != "Destino")
		return false;

	if (getElementValue("filialByIdFilial" + tipo + ".sgFilial")=="MTZ"){
		alertI18nMessage("LMS-01167");
		return false;
	}
	var nenhumaCombinacao = false;
	// Zona
	if(!isBlank(getElementValue("zonaByIdZona" + tipo + ".idZona"))) {
		// Pais
		if(!isBlank(getElementValue("paisByIdPais" + tipo + ".nmPais"))) {
			// UF
			if(!isBlank(getElementValue("unidadeFederativaByIdUf" + tipo + ".idUnidadeFederativa"))) {
				// + Filial + Municipio
				if(!isBlank(getElementValue("filialByIdFilial" + tipo + ".sgFilial"))) {
					// N�o tem Aeroporto ou Tipo Localizacao
					if( !isBlank(getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto")) ||
						!isBlank(getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio"))
					) {
						alertI18nMessage("LMS-01024");
						return false;
					}
				} else {
					// + Aeroporto
					if(!isBlank(getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto"))) {
						// Nao tem Filial, Municipio ou TL
						if( !isBlank(getElementValue("filialByIdFilial" + tipo + ".sgFilial")) ||
							!isBlank(getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio")) ||
							!isBlank(getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio"))
						) {
							alertI18nMessage("LMS-01024");
							return false;
						}
					} else {
						// + Tipo Localizacao
						if(!isBlank(getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio"))) {
							// Nao tem Aeroporto, Filial ou Municipio
							if( !isBlank(getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto")) ||
								!isBlank(getElementValue("filialByIdFilial" + tipo + ".sgFilial")) ||
								!isBlank(getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio"))
							) {
								alertI18nMessage("LMS-01024");
								return false;
							}
						} else {
							nenhumaCombinacao = true;
						}
					}
				}
			} else { // else UF
				nenhumaCombinacao = true;
			}
		} else { // else Pais
			nenhumaCombinacao = true;
		}

		// Nenhuma combinacao
		if(nenhumaCombinacao){
			if( !isBlank(getElementValue("filialByIdFilial" + tipo + ".sgFilial")) ||
				!isBlank(getElementValue("municipioByIdMunicipio" + tipo + ".municipio.nmMunicipio")) ||
				!isBlank(getElementValue("aeroportoByIdAeroporto" + tipo + ".sgAeroporto")) ||
				!isBlank(getElementValue("tipoLocalizacaoMunicipioByIdTipoLocalizacao" + tipo + ".idTipoLocalizacaoMunicipio"))
			) {
				alertI18nMessage("LMS-01024");
				return false;
			}			
		}
	} else { // else Zona
		alertI18nMessage("LMS-01024");
		return false;
	}
	return true;
}
