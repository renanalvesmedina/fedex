<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--

	function configuraCampoHodometro() {
		setDisabled('blVirouHodometro', false);
		setDisabled('nrQuilometragem', false);
		var tpSituacaoPendencia = getElementValue('controleQuilometragem.tpSituacaoPendencia')
		if ("A" == tpSituacaoPendencia) {
			setDisabled('blVirouHodometro', true);
			setDisabled('nrQuilometragem', true);
		}
	}

	function quilometragemLoad_cb(data,error) {
		onDataLoad_cb(data,error);
		preencheUsuarioLogado();
		configuraCampoHodometro();
		if (getElementValue("idProcessoWorkflow") != "") {
			setDisabled(document, true);
		} else {
			setDisabled("controleCargas",getNestedBeanPropertyValue(data,"controleCarga.idControleCarga") == undefined);
		}
	}	
	
	function pageLoad_cb() {	
		onPageLoad_cb();	
		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
		}
	}
	 
//-->
</script>
<adsm:window service="lms.portaria.manterQuilometragensSaidaChegadaAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/portaria/manterQuilometragensSaidaChegada" idProperty="idControleQuilometragem"
			onDataLoadCallBack="quilometragemLoad"
			service="lms.portaria.manterQuilometragensSaidaChegadaAction.findByIdTela" >
			
		<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
		<adsm:hidden property="controleQuilometragem.tpSituacaoPendencia"/>
		
		<adsm:hidden property="usuario.idUsuario" />
		<adsm:complement width="83%" labelWidth="17%" label="responsavel">
             <adsm:textbox dataType="text" property="usuario.nrMatricula" size="12" disabled="true"/>
             <adsm:textbox dataType="text" property="usuario.nmUsuario" size="28" disabled="true"/>
        </adsm:complement>
        
        <adsm:lookup service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupFilial" dataType="text" picker="false"
				property="filial" criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" disabled="true"
				width="83%" labelWidth="17%" action="/municipios/manterFiliais" idProperty="idFilial" required="true" >
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false"/>
        </adsm:lookup>
        
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>
		<adsm:hidden property="meioTransporte.tpSituacao" value="A" />
		<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" required="true"
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupMeioTransp" picker="false" disabled="true"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="17%" 
				width="83%" size="8" serializable="true" maxLength="6" cellStyle="vertical-Align:bottom"  >
			<%--adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="meioTransporte.tpSituacao"/--%>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.tpVinculo"
					modelProperty="meioTransporte.tpVinculo.value"/>
					
			<adsm:textbox dataType="text" disabled="true" property="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					size="20" serializable="false" />
		</adsm:lookup>
		<adsm:hidden property="meioTransporteRodoviario.meioTransporte.tpVinculo" />
		
		<%--adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" disabled="true"
				service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupMeioTransp" picker="false" maxLength="25"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
				width="72%" size="20" required="true" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
					modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
					modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping criteriaProperty="meioTransporte.tpSituacao" modelProperty="meioTransporte.tpSituacao"/>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.tpVinculo"
					modelProperty="meioTransporte.tpVinculo.value"/>
		</adsm:lookup--%>
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>

		<adsm:lookup service="lms.portaria.manterQuilometragensSaidaChegadaAction.findLookupControleCarga" dataType="text" picker="false"
				property="controleCarga" criteriaProperty="filialByIdFilialOrigem.sgFilial" label="controleCarga" size="3" maxLength="3"
				width="33%" labelWidth="17%" action="carregamento/manterControleCargas" idProperty="idControleCarga" disabled="true" >
        	<adsm:propertyMapping modelProperty="nrControleCarga" relatedProperty="controleCarga.nrControleCarga"/>
        	<adsm:textbox dataType="integer" property="controleCarga.nrControleCarga" size="8" disabled="true" serializable="false" mask="00000000"/>
        </adsm:lookup>
		<adsm:checkbox property="blSaida" label="saida" labelWidth="15%" width="35%" disabled="true" />
		
		<adsm:hidden property="usuarioByIdUsuario.idUsuario" />
		<adsm:textbox dataType="JTDateTimeZone" property="dhMedicao"
				label="dataHoraRegistro" labelWidth="17%" width="33%" disabled="true" />
		<adsm:complement width="35%" labelWidth="15%" label="informadoPor">
             <adsm:textbox dataType="text" property="usuarioByIdUsuario.nrMatricula" size="12" disabled="true"/>
             <adsm:textbox dataType="text" property="usuarioByIdUsuario.nmUsuario" size="28" disabled="true"/>
        </adsm:complement>
         
		<adsm:hidden property="usuarioByIdUsuarioCorrecao.idUsuario" />			
        <adsm:textbox dataType="JTDateTimeZone" property="dhCorrecao"
				label="dataHoraCorrecao" labelWidth="17%" width="33%" disabled="true" />
		<adsm:complement width="35%" labelWidth="15%" label="corrigidoPor">
             <adsm:textbox dataType="text" property="usuarioByIdUsuarioCorrecao.nrMatricula" size="12" disabled="true"/>
             <adsm:textbox dataType="text" property="usuarioByIdUsuarioCorrecao.nmUsuario" size="28" disabled="true"/>
        </adsm:complement>		
				
        <adsm:checkbox property="blVirouHodometro" label="virouHodometro" labelWidth="17%" width="33%" />
        <adsm:textbox dataType="decimal" property="nrQuilometragem" mask="###,###"
        		label="quilometragem" size="10" maxLength="6" required="true" labelWidth="15%" width="35%"/>   
        
		<adsm:textarea property="obControleQuilometragem" label="observacao" maxLength="500" 
				rows="5" width="83%" columns="60" labelWidth="17%" />
		
		<adsm:buttonBar>
			<adsm:button caption="controleCargas" id="controleCargas" action="carregamento/consultarControleCargas" cmd="main" >
				<adsm:linkProperty src="controleCarga.idControleCarga" target="idControleCarga" disabled="true" />
				<adsm:linkProperty src="controleCarga.nrControleCarga" target="nrControleCarga" disabled="true" />
				<adsm:linkProperty src="filial.idFilial" target="filialByIdFilialOrigem.idFilial" disabled="true" />
				<adsm:linkProperty src="filial.sgFilial" target="filialByIdFilialOrigem.sgFilial" disabled="true" />
				<adsm:linkProperty src="filial.pessoa.nmFantasia" target="filialByIdFilialOrigem.pessoa.nmFantasia" disabled="true" />
			</adsm:button>
			<adsm:storeButton callbackProperty="afterStore" />
		</adsm:buttonBar> 
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--

	document.getElementById("idProcessoWorkflow").masterLink = "true";

	var sdoUsuario = createServiceDataObject("lms.portaria.manterQuilometragensSaidaChegadaAction.findUsuarioLogado",
		"preencheDadosUsuarioLogado",undefined);
	xmit({serviceDataObjects:[sdoUsuario]});
	
	var idUsuarioLogado = -1;
	var nrMatriculaUsuarioLogado = -1;
	var nmUsuarioLogado = -1;
	
	//Funcao de callback do servico que retorna os dados do usuario logado. 
	function preencheDadosUsuarioLogado_cb(data, exception){
		if (exception == null){
			idUsuarioLogado = getNestedBeanPropertyValue(data,"idUsuario");
			nrMatriculaUsuarioLogado = getNestedBeanPropertyValue(data,"nrMatricula");
			nmUsuarioLogado = getNestedBeanPropertyValue(data, "nmUsuario");
		}
	}
	
	function preencheUsuarioLogado() {
		setElementValue("usuario.idUsuario",idUsuarioLogado);
		setElementValue("usuario.nrMatricula",nrMatriculaUsuarioLogado);
		setElementValue("usuario.nmUsuario",nmUsuarioLogado);
	}
	
	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);
		if (error != undefined) {
			return false;
		}
				
		setElementValue("usuarioByIdUsuarioCorrecao.nrMatricula",nrMatriculaUsuarioLogado);
		setElementValue("usuarioByIdUsuarioCorrecao.nmUsuario",nmUsuarioLogado);
		
		var mensagem = getNestedBeanPropertyValue(data,"msgError");
		if (mensagem != undefined && mensagem != "") {
			alert(mensagem);
		}
	}
	
//-->
</script>