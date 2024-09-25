<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarRotaColetaEntrega" 
			 service="lms.municipios.manterRotaColetaEntregaAction" 
			 onPageLoadCallBack="rotaColetaEntregaPageLoad">
	
	<adsm:form action="/municipios/manterRotaColetaEntrega" idProperty="idRotaColetaEntrega">
	 
		<adsm:lookup service="lms.municipios.manterRotaColetaEntregaAction.findFilialLookup" dataType="text" property="filial" 
					idProperty="idFilial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" 
					exactMatch="true" labelWidth="15%"  action="/municipios/manterFiliais" required="true" disabled="true">
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="filial.empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:hidden property="filial.empresa.tpEmpresa" serializable="false" value="M"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="25" disabled="true"/>
		</adsm:lookup>		
        
		<adsm:textbox dataType="integer" property="nrRota" size="3" maxLength="3" label="numeroRota" width="25%"/>
		
		<adsm:textarea property="dsRota" label="descricao" rows="2" columns="60" maxLength="120"  width="50%" />
		
		 <adsm:range label="vigencia" labelWidth="15%" width="35%">
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
        
        <adsm:combobox property="vigentes" label="vigentes" domain="DM_SIM_NAO" defaultValue="S" width="32%" />        
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaColetaEntrega"/>
			<adsm:button caption="limpar" id="btnLimpar" buttonType="resetButton" onclick="limpaTela()"/> 
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid idProperty="idRotaColetaEntrega" property="rotaColetaEntrega" 
			   selectionMode="check" gridHeight="200" unique="true" rows="10"			   
			   service="lms.municipios.manterRotaColetaEntregaAction.findPaginated"
			   rowCountService="lms.municipios.manterRotaColetaEntregaAction.getRowCountCustom"
			   defaultOrder="filial_pessoa_.nmPessoa, nrRota, dtVigenciaInicial">
		<adsm:gridColumnGroup separatorType="FILIAL">
     		<adsm:gridColumn title="filial" property="filial.sgFilial" width="50"/>
     		<adsm:gridColumn title="" property="filial.pessoa.nmFantasia" width="100"/>
		</adsm:gridColumnGroup>	   
		<adsm:gridColumn title="numeroRota" dataType="integer" property="nrRota" width="65"/>
		<adsm:gridColumn title="descricao" property="dsRota" width="150"/>	
		<adsm:gridColumn title="distanciaRota2" property="nrKm" width="75" align="right" unit="km2"/>	
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100"/>
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="100"/>

		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>

	var idFilialLogado;
	var sgFilialLogado;
	var nmFilialLogado;
	
	// Função que verifica se a tela está sendo aberta no modo "lookup".
	function isLookup() {
		var url = new URL(parent.location.href);
		var mode = url.parameters["mode"];
		if ((mode!=undefined) && (mode=="lookup")) return true;
		return false;
	}
	
	function rotaColetaEntregaPageLoad_cb(data){
		onPageLoad_cb(data);
		document.getElementById("btnLimpar").disabled = false;
		// Se não é lookup, então sempre busca a Filial do usuário logado.
		if (!isLookup()){
			getFilialUsuario();
    	} else {
    	// Caso contrário verifica se a Filial passada para a lookup é Matriz.
			validateFilialMatriz();
       	}
	}
	
	// Função que busca os dados da Filial do usuário logado.
	function getFilialUsuario() {
		// Se aina não buscou os dados, então busca a primeira vez.
		if (!idFilialLogado){
			var sdo = createServiceDataObject("lms.municipios.manterRotaColetaEntregaAction.findFilialUsuarioLogado","getFilialUsuario",null);
			xmit({serviceDataObjects:[sdo]});
		} else {
		// Se já buscou, então é só setar os dados nos seus respectivos campos.
			setaValoresFilial();
		}
	}
	
	// Função que seta os dados da Filial do usuário logado.
	function getFilialUsuario_cb(data,error) {
		if (error) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			idFilialLogado = getNestedBeanPropertyValue(data,"idFilial");
			sgFilialLogado = getNestedBeanPropertyValue(data,"sgFilial");
			nmFilialLogado = getNestedBeanPropertyValue(data,"pessoa.nmFantasia");
			setaValoresFilial();
		}
	}
	
	// Seta os valores das variáveis globais para os seus respectivos campos.
	function setaValoresFilial() {
		setElementValue("filial.idFilial", idFilialLogado);
		setElementValue("filial.sgFilial", sgFilialLogado);
		setElementValue("filial.pessoa.nmFantasia", nmFilialLogado);
	}
	
	// Verifica se a Filial do usuário é Matriz	
	function validateFilialMatriz(){
	    var sdo = createServiceDataObject("lms.municipios.manterRotaColetaEntregaAction.isFilialMatriz", "isFilialMatriz", {idFilial:getElementValue("filial.idFilial")});
	    xmit({serviceDataObjects:[sdo]});
	}

	// Habilita o campo caso a Filial do usuário seja Matriz	
	function isFilialMatriz_cb(data, error){
		if (error){
			alert(error);
			return false;
		}
		if (data && data.isFilialMatriz && data.isFilialMatriz == "true"){
			document.getElementById("filial.idFilial").masterLink = false;
			document.getElementById("filial.pessoa.nmFantasia").masterLink = false;
			document.getElementById("filial.sgFilial").masterLink = false;
			setDisabled("filial.idFilial", false);
		}
	}
	
	// Limpa a tela e seta os dados necessários.
	function limpaTela(){
		cleanButtonScript(this.document);
		getFilialUsuario();
	}

</script>
