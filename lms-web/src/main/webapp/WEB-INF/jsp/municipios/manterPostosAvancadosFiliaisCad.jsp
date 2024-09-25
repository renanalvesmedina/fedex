<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.postoAvancadoService">
	<adsm:form action="/municipios/manterPostosAvancadosFiliais" idProperty="idPostoAvancado" service="lms.municipios.postoAvancadoService.findByIdDetalhamento" onDataLoadCallBack="pageLoad">
		<adsm:lookup label="filial" labelWidth="25%" dataType="text" size="3" maxLength="3" width="75%" required="true"
				     service="lms.municipios.filialService.findLookup" property="filial" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais">
                  <adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
      			  <adsm:textbox dataType="text" serializable="false" property="filial.pessoa.nmFantasia" size="30" disabled="true"/>
        </adsm:lookup>
      	
		<adsm:textbox dataType="text" label="descricaoPostoAvancado" property="dsPostoAvancado" maxLength="60" size="30" labelWidth="25%" width="75%" required="true" />

 		<adsm:lookup property="usuario" label="encarregado" idProperty="idUsuario" criteriaProperty="nrMatricula" 
					 action="/configuracoes/consultarFuncionariosView" dataType="text" size="10" maxLength="16" labelWidth="25%" width="77%"
		 			 service="lms.municipios.manterPostosAvancadosFiliaisAction.findLookupUsuario">
			<adsm:propertyMapping modelProperty="nmUsuario"    relatedProperty="usuario.nmUsuario"/>
			<adsm:textbox dataType="text" property="usuario.nmUsuario" size="30" disabled="true" serializable="false"/>
		</adsm:lookup> 
 
		<adsm:lookup service="lms.municipios.postoAvancadoService.findLookupCliente" dataType="text" property="cliente" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado" idProperty="idCliente" label="localizacao" action="vendas/manterDadosIdentificacao" size="20" maxLength="20" labelWidth="25%" width="19%" onPopupSetValue="updateData">
			<adsm:propertyMapping relatedProperty="cliente.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
			<adsm:propertyMapping relatedProperty="cliente.endereco.bairro" modelProperty="endereco.bairro"/>
			<adsm:propertyMapping relatedProperty="cliente.endereco.cep" modelProperty="endereco.cep"/>
			<adsm:propertyMapping relatedProperty="cliente.endereco.uf" modelProperty="endereco.uf"/>
			<adsm:propertyMapping relatedProperty="cliente.endereco.enderecoCompleto" modelProperty="endereco.enderecoCompleto"/>
			<adsm:propertyMapping relatedProperty="cliente.endereco.municipio" modelProperty="endereco.municipio"/>
			<adsm:propertyMapping relatedProperty="cliente.endereco.pais" modelProperty="endereco.pais"/>
			<adsm:propertyMapping criteriaProperty="cliente.tpSituacao" modelProperty="tpSituacao"/>
			<adsm:hidden property="cliente.tpSituacao" value="A" serializable="false"/>
        </adsm:lookup>
        <adsm:textbox dataType="text" property="cliente.pessoa.nmPessoa" size="30" disabled="true" width="56%" serializable="false"/>

		<adsm:textbox dataType="text" label="endereco" property="cliente.endereco.enderecoCompleto" maxLength="60" size="30" labelWidth="25%" width="25%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" label="bairro" property="cliente.endereco.bairro" maxLength="60" size="30" labelWidth="25%" width="25%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" label="municipio" property="cliente.endereco.municipio" maxLength="60" size="30" labelWidth="25%" width="25%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" label="uf" property="cliente.endereco.uf" maxLength="60" size="30" labelWidth="25%" width="25%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" label="pais" property="cliente.endereco.pais" maxLength="60" size="30" labelWidth="25%" width="25%" disabled="true" serializable="false"/>
		<adsm:textbox dataType="text" label="cep" property="cliente.endereco.cep" maxLength="60" size="30" labelWidth="25%" width="25%" disabled="true" serializable="false"/>
			
		<adsm:textarea property="obPostoAvancado" label="observacao" rows="3" columns="80" maxLength="500" labelWidth="25%" width="75%" />
		
		<adsm:range label="vigencia" labelWidth="25%" width="75%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial"  required="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
        </adsm:range>
	<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore" id="botaoSalvar" service="lms.municipios.postoAvancadoService.storeMap"/>
			<adsm:newButton id="botaoLimpar"/>
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<Script>
<!--
	function updateData(data) {
		var sdo = createServiceDataObject("lms.municipios.postoAvancadoService.findEnderecoCliente",
				"preencheEndereco",{e:getNestedBeanPropertyValue(data,"idCliente")});
		xmit({serviceDataObjects:[sdo]});
		return true;
	}
	function preencheEndereco_cb(data) {
		fillFormWithFormBeanData(document.forms[0].tabIndex, data);
	}
	function pageLoad_cb(data,error) {
		if (error) {
			if (data._exception._key == "LMS-29006")
				setFocus("obPostoAvancado");
		}
		onDataLoad_cb(data,error);
		acaoVigencia(data);
	}
	
	function afterStore_cb(data,exception) {
		store_cb(data,exception);
		if (exception == undefined) {
			acaoVigencia(data);
			setFocus(document.getElementById("botaoLimpar"),false);
		}
	}
	
	function acaoVigencia(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			  habilitaCampo();
			  setFocusOnFirstFocusableField();
		}else if (acaoVigenciaAtual == 1) {
		      setDisabled(document,true);
		      setDisabled("botaoSalvar",false);
		      setDisabled("botaoLimpar",false);
		      setDisabled("dtVigenciaFinal",false);
		      setFocusOnFirstFocusableField();
		} else if (acaoVigenciaAtual == 2) {
		      setDisabled(document,true);
		      setDisabled("botaoLimpar",false);
		      setFocus(document.getElementById("botaoLimpar"),false);
		}		
	}

	function habilitaCampo() {
		setDisabled("filial.idFilial",(document.getElementById("filial.idFilial").masterLink == "true"));
		setDisabled("usuario.idUsuario",false);
		setDisabled("cliente.idCliente",false);
		setDisabled("dsPostoAvancado",false);
		setDisabled("obPostoAvancado",false);
		setDisabled("dtVigenciaInicial",false);
		setDisabled("dtVigenciaFinal",false);
		setFocusOnFirstFocusableField();
	}
	
	function initWindow(eventObj) {
		if ((eventObj.name == "newButton_click") || (eventObj.name == "tab_click"))
			habilitaCampo();

	}
//-->
</Script>