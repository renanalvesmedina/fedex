<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	
	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
	    var data = new Array();
	    var sdo = createServiceDataObject("lms.entrega.manterTurnosAgendamentoAction.findInformacoesUsuarioLogado", "pageLoad",data);
	    xmit({serviceDataObjects:[sdo]});
	}

	function findFilialUsuarioLogado(){
		var data = new Array();
	    var sdo = createServiceDataObject("lms.entrega.manterTurnosAgendamentoAction.findInformacoesUsuarioLogado", "pageLoad",data);
	    xmit({serviceDataObjects:[sdo]});
	}



function initWindow(eventObj){
		if(eventObj.name == 'cleanButton_click'){
			findFilialUsuarioLogado();
		}
			
	}

</script>
<adsm:window title="manterTurnosAgendamento"
				service="lms.entrega.manterTurnosAgendamentoAction"
				onPageLoadCallBack="carregaDados">
	<adsm:form action="/entrega/manterTurnosAgendamento"> 
		<adsm:hidden property="idFilial"/>
		<adsm:hidden property="sgFilial"/>
		<adsm:hidden property="nmFantasia"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>	
		
		<adsm:lookup property="filial" idProperty="idFilial" criteriaProperty="sgFilial" maxLength="3"
				service="lms.entrega.manterTurnosAgendamentoAction.findLookupFilial" 
				dataType="text" label="filial" size="3"
				action="/municipios/manterFiliais" labelWidth="18%" width="32%" minLengthForAutoPopUpSearch="3"
				exactMatch="false"   disabled="false" required="true"   >
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa"  modelProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia"  modelProperty="pessoa.nmFantasia" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia"  disabled="true"   size="25" serializable="false" />			
		</adsm:lookup>


		<adsm:textbox dataType="text" property="dsTurno" label="descricao" size="37" maxLength="60" labelWidth="18%" width="32%" required="false" />
		<adsm:range label="horario" width="32%" labelWidth="18%" >
			<adsm:textbox dataType="JTTime" property="hrTurnoInicial" />
			<adsm:textbox dataType="JTTime" property="hrTurnoFinal" />
		</adsm:range>
		<adsm:range label="vigencia" width="82%" labelWidth="18%" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>
	
		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="turno" />
			<adsm:button caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTurno" property="turno" selectionMode="check" defaultOrder="dtVigenciaInicial:desc,hrTurnoInicial:asc"
				unique="true" maxRows="12"  rows="12">
		<adsm:gridColumn  title="turno" property="dsTurno" dataType="text"/>
		<adsm:gridColumn width="25%" title="horarioInicial" property="hrTurnoInicial" align="center" dataType="JTTime"/>
		<adsm:gridColumn width="25%" title="horarioFinal" property="hrTurnoFinal" align="center" dataType="JTTime"/>
		<adsm:gridColumn width="13%" title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn width="12%" title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate"/>		
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

	function pageLoad_cb(data, error) {
	
		//setElementValue("idFilial",data.filialId);
		setElementValue("idFilial",getNestedBeanPropertyValue(data,"filial.idFilial"));	
		setElementValue("sgFilial",getNestedBeanPropertyValue(data,"filial.sgFilial"));	
		setElementValue("nmFantasia",getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
		preencheFilial();
	}
	function preencheFilial(){
			setElementValue("filial.idFilial", getElementValue("idFilial"));
			setElementValue("filial.sgFilial", getElementValue("sgFilial"));
			setElementValue("filial.pessoa.nmFantasia", getElementValue("nmFantasia"));
	}
	function limpar_OnClick(){
		preencheFilial();
		//resetValue('tipoTabelaColetaEntrega.idTipoTabelaColetaEntrega');
		resetValue('dtVigenciaInicial');
		resetValue('dtVigenciaFinal');
		//resetValue('tipoMeioTransporte.idTipoMeioTransporte');
		cleanButtonScript(undefined,undefined,undefined);
		
	}
</script>