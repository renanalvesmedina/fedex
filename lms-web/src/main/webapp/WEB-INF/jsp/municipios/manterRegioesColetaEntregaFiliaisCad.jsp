<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
//funcao chamada no call back do form - recebe os flags definindo se os campos ficarao habilitados ou desabilitados no detalhamento, validacao conforme as datas de vigencia
function pageLoad_cb(data,exception){
  onDataLoad_cb(data,exception);
  var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
  validaAcaoVigencia(acaoVigenciaAtual, null);	
}
function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
	if(acaoVigenciaAtual == 0){
  	 //setDisabled(document, false);
  	 setDisabled("filial.pessoa.nmFantasia", true);
  	 if(tipoEvento == "" ||  tipoEvento == null)
    	setFocusOnFirstFocusableField(document);
     else
    	setFocus(document.getElementById("botaoNovo"),false);
   }else if(acaoVigenciaAtual == 1) {
     setDisabled(document,true);
     setDisabled("rotasRegiao",false);
     setDisabled("botaoNovo",false);
     setDisabled("botaoSalvar",false);
     setDisabled("dtVigenciaFinal", false);
     if(tipoEvento == "" ||  tipoEvento == null)
    	setFocusOnFirstFocusableField(document);
     else
    	setFocus(document.getElementById("botaoNovo"),false);
  }else if(acaoVigenciaAtual == 2) {
     setDisabled(document,true);
     setDisabled("botaoNovo",false);
     setDisabled("rotasRegiao",false);
     setFocus(document.getElementById("botaoNovo"),false);
  }
	
}


//funções do botao excluir
function myNewButtonScript(documento)
{
	newButtonScript(documento);
	//setDisabled("botaoNovo", false);
}
function myNewButtonScript_cb(data,errorMsg)
{
	if(errorMsg!=null)
	{
		alert(errorMsg);
		return;
	}
	myNewButtonScript(this.document);
}

function myStoreButton_cb(data,exception,key){
	store_cb(data,exception,key);
	if (exception == undefined){
	     var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
	     var store = "true";
		 validaAcaoVigencia(acaoVigenciaAtual, store);
	}
}


	function initWindow(eventObj) {
		var blDetalhamento = eventObj.name == "gridRow_click" || eventObj.name == "storeButton";

		if (!blDetalhamento) {
			if (document.getElementById("filial.idFilial").masterLink != "true") {
				setElementValue("filial.idFilial",getPesqValue("idFilialSessao"));
				setElementValue("filial.sgFilial",getPesqValue("sgFilialSessao"));
				setElementValue("filial.pessoa.nmFantasia",getPesqValue("nmFilialSessao"));
			}
			if (eventObj.name == "tab_click" || eventObj.name == "newButton_click") {
				//setDisabled(document,false);
				 setDisabled("filial.pessoa.nmFantasia", true);
				 setDisabled("botaoExcluir", true);
				 setDisabled("rotasRegiao", true);
				if (document.getElementById("filial.sgFilial").masterLink == "true"){
					 setDisabled("filial.idFilial", true);
					 setFocus(document.getElementById("dsRegiaoColetaEntregaFil"));
				}else{
					setFocus(document.getElementById("filial.sgFilial"));
				}
		 	}
		}
	}

	function getPesqValue(propertyName) {
		var tabGroup = getTabGroup(this.document);
		return tabGroup.getTab("pesq").getFormProperty(propertyName);
	}

</script>
<adsm:window service="lms.municipios.regiaoColetaEntregaFilService">
	<adsm:form action="/municipios/manterRegioesColetaEntregaFiliais" idProperty="idRegiaoColetaEntregaFil" onDataLoadCallBack="pageLoad" service="lms.municipios.regiaoColetaEntregaFilService.findByIdEValidaVigencias">
		<adsm:hidden property="tpEmpresa" value="M" serializable="false"></adsm:hidden>
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial"
				service="lms.municipios.filialService.findLookup" dataType="text" label="filial" size="3" maxLength="3"
				labelWidth="20%" width="80%" action="/municipios/manterFiliais" exactMatch="false"
				minLengthForAutoPopUpSearch="3" required="true" disabled="true">
       		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
       		<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" /> 
        </adsm:lookup>

      	 
	 	<adsm:textbox dataType="text" property="dsRegiaoColetaEntregaFil" label="descricaoRegiao" required="true" size="60" maxLength="60" labelWidth="20%" width="80%"/> 
	 	
       	<adsm:range label="vigencia" labelWidth="20%" width="80%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		<adsm:buttonBar>
			<adsm:button caption="rotasRegiao" action="municipios/manterRegioesFilialRota" cmd="main" boxWidth="125" id="rotasRegiao">
				<adsm:linkProperty src="filial.idFilial" target="rotaColetaEntrega.filial.idFilial"/>
				<adsm:linkProperty src="filial.sgFilial" target="rotaColetaEntrega.filial.sgFilial"/>
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="rotaColetaEntrega.filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="idRegiaoColetaEntregaFil" target="regiaoColetaEntregaFil.idRegiaoColetaEntregaFil"/>	
				<adsm:linkProperty src="idRegiaoColetaEntregaFil" target="idRegiaoColetaEntregaFilMasterLink"/>	
				<adsm:linkProperty src="dtVigenciaInicial" target="regiao.dtVigenciaInicial"/>
				<adsm:linkProperty src="dtVigenciaFinal" target="regiao.dtVigenciaFinal"/>
			</adsm:button>
			
			<adsm:storeButton id="botaoSalvar" callbackProperty="myStoreButton" service="lms.municipios.regiaoColetaEntregaFilService.storeMap" />
			<adsm:newButton id="botaoNovo"/>
			<adsm:button caption="excluir" id="botaoExcluir" onclick="removeButtonScript('lms.municipios.regiaoColetaEntregaFilService.removeById', 'myNewButtonScript', 'idRegiaoColetaEntregaFil', this.document);" buttonType="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   