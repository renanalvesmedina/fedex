<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	/**
	 * Retorna estado dos campos como foram carregados na página.
	 */
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("__buttonBar:0.removeButton",true);
		setDisabled("filial.pessoa.nmFantasia",true);
		if (document.getElementById("filial.idFilial").masterLink) {
			setDisabled("filial.idFilial",true);
			setFocus(document.getElementById("tpAtendimento"));			
		} else {
			setFocus(document.getElementById("filial.sgFilial"));
		}
	}
	
	/**
	 * Habilitar campos se o registro estiver vigente.
	 */
	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
		setFocus(document.getElementById("dtVigenciaFinal"));
	}
	
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function pageLoad_cb(data,exception) {
		onDataLoad_cb(data,exception);
		comportamentoDetalhe(data);
	}
	
</script>
<adsm:window service="lms.municipios.atendimentoFilialService" >
	<adsm:form action="/municipios/manterHorariosAtendimentoFilial" idProperty="idAtendimentoFilial"
			onDataLoadCallBack="pageLoad" service="lms.municipios.atendimentoFilialService.findByIdDetalhamento" >
		<adsm:lookup service="lms.municipios.filialService.findLookup" dataType="text" property="filial" idProperty="idFilial"
				criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" required="true"
				action="/municipios/manterFiliais" labelWidth="17%" width="75%" exactMatch="true" >
         		<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" maxLength="50" disabled="true" serializable="false"/>	
	    </adsm:lookup>
		
		<adsm:combobox label="tipoFuncionamento" property="tpAtendimento" domain="DM_TIPO_ATENDIMENTO" labelWidth="17%" width="83%" required="true" />
		
		<adsm:multicheckbox 
			texts="dom|seg|ter|qua|qui|sex|sab|"
			property="blDomingo|blSegunda|blTerca|blQuarta|blQuinta|blSexta|blSabado|" 
			align="top" label="dias2" width="60%" labelWidth="17%" />
	
		<adsm:range label="horario" labelWidth="17%" required="true" >
			<adsm:textbox dataType="JTTime" property="hrAtendimentoInicial" />
			<adsm:textbox dataType="JTTime" property="hrAtendimentoFinal"/>
		</adsm:range>
		<adsm:textarea maxLength="500" property="obAtendimento" label="observacao" rows="3" columns="55" width="83%" labelWidth="17%"/>

		<adsm:range label="vigencia" labelWidth="17%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true" />
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar>
			<adsm:storeButton id="__botaoSalvar" callbackProperty="afterStore" />
			<adsm:newButton id="__botaoNovo" />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	// ############################################################
	// tratamento dos eventos da initWindow para <tab_click>, 
	// <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	// ############################################################
	function initWindow(eventObj) {
		if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton")
			estadoNovo();
	}
	
	function comportamentoDetalhe(data) {
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			estadoNovo();
			setDisabled("__buttonBar:0.removeButton",false);
			setFocusOnFirstFocusableField(document);
		} else if (acaoVigenciaAtual == 1) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setDisabled("__botaoSalvar",false);
			setDisabled("dtVigenciaFinal",false);
			setFocusOnFirstFocusableField(document);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("__botaoNovo",false);
			setFocusOnNewButton();
		}
	}
	
	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);
		if (error == undefined) {
			comportamentoDetalhe(data);
			setFocusOnNewButton();
		}
	}
	
</script>
