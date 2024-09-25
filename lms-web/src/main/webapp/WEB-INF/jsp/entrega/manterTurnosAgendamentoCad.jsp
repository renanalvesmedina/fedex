<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script language="javascript" type="text/javascript">
	function carregaDados_cb(data, error) {
		onPageLoad_cb(data, error);
		var data = new Array();
		var sdo = createServiceDataObject("lms.entrega.manterTurnosAgendamentoAction.findInformacoesUsuarioLogado", "filialSession",data);
		xmit({serviceDataObjects:[sdo]});
	}
</script>
<adsm:window title="turnosAgendados" service="lms.entrega.manterTurnosAgendamentoAction" onPageLoadCallBack="carregaDados">
	<adsm:form
		idProperty="idTurno"
		action="/entrega/manterTurnosAgendamento"
		onDataLoadCallBack="pageLoad"
		service="lms.entrega.manterTurnosAgendamentoAction.findByIdDetalhamento"
	>
		<adsm:hidden property="idFilialSessao"/>
		<adsm:hidden property="sgFilialSessao"/>
		<adsm:hidden property="nmFilialSessao"/>
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>

		<adsm:lookup
			label="filial"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			service="lms.entrega.manterTurnosAgendamentoAction.findLookupFilial"
			action="/municipios/manterFiliais"
			maxLength="3"
			dataType="text"
			size="3"
			labelWidth="18%"
			width="32%"
			minLengthForAutoPopUpSearch="3"
			exactMatch="false"
			disabled="false"
			required="true"
		>
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" size="25" serializable="false"/>
		</adsm:lookup>

		<adsm:textbox dataType="text" property="dsTurno" label="descricao" size="37" maxLength="60" labelWidth="18%" width="32%" required="true" />

		<adsm:range label="horario" width="32%" labelWidth="18%" required="true">
			<adsm:textbox dataType="JTTime" property="hrTurnoInicial" />
			<adsm:textbox dataType="JTTime" property="hrTurnoFinal" />
		</adsm:range>

		<adsm:range label="vigencia" width="82%" labelWidth="18%" >
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>

		<adsm:buttonBar>
			<adsm:button caption="salvar" onclick="store()" id="storeButtom" />
			<adsm:newButton id="BotaoLimpar" />
			<adsm:removeButton id="botaoExcluir"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;

	function newState() {
		setDisabled("filial.idFilial", false);
		setDisabled("dsTurno", false);
		setDisabled("hrTurnoInicial", false);
		setDisabled("hrTurnoFinal", false);
		setDisabled("dtVigenciaInicial", false);
		setDisabled("dtVigenciaFinal", false);
		setDisabled("storeButtom", false);
		setFocus("filial.sgFilial", false);
	}

	function initWindow(evento) {
		if(evento.name=="newButton_click" || evento.name =="tab_click") {
			carregaFilial();
			newState();
		}
		if(evento.name=="removeButton") {
			carregaFilial();
			setDisabled("storeButtom", false);
			newState();
		}
	}

	function carregaFilial(){
		var data = new Array();
		var sdo = createServiceDataObject("lms.entrega.manterTurnosAgendamentoAction.findInformacoesUsuarioLogado", "filialSession",data);
		xmit({serviceDataObjects:[sdo]});
		populaFilial();
	}

	// Faz a verificacao e salva os dois forms 
	function store() {
		storeEditGridScript('lms.entrega.manterTurnosAgendamentoAction.storeCustom', 'afterStore', document.forms[0]);
	}

	function populaFilial() {
		setElementValue("filial.idFilial", getElementValue("idFilialSessao"));	
		setElementValue("filial.sgFilial", getElementValue("sgFilialSessao"));	
		setElementValue("filial.pessoa.nmFantasia",getElementValue("nmFilialSessao"));

	 	setDisabled("filial.pessoa.nmFantasia",true);		
	}

	function filialSession_cb(data) {
		setElementValue("idFilialSessao", getNestedBeanPropertyValue(data, "filial.idFilial"));
		setElementValue("sgFilialSessao", getNestedBeanPropertyValue(data, "filial.sgFilial"));
		setElementValue("nmFilialSessao", getNestedBeanPropertyValue(data, "pessoa.nmFantasia"));
		populaFilial();
	}

	// Método é chamado depois que é gravado os dados no banco
	function afterStore_cb(data, error) {
		store_cb(data, error);
		desabilitaCampos(data, error);
		setFocus("BotaoLimpar", false);
	}

	// Este método é responsável por desabilitar campos e botoes da tela.
	function desabilitaCampos(data, error) {
		var dtVigenciaInicialDetalhe = getNestedBeanPropertyValue(data, "dtVigenciaInicial");
		var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
		if (acaoVigenciaAtual == 0) {
			newState();
			setDisabled("__buttonBar:0.removeButton",false);
		} else if (acaoVigenciaAtual == 1) {
		 	setDisabled(document, true);
			setDisabled("filial.idFilial", true);
			setDisabled("filial.sgFilial", true);
			setDisabled("filial.pessoa.nmFantasia", true);
			setDisabled("BotaoLimpar", false);
			setDisabled("storeButtom", false);
			setDisabled("dtVigenciaInicial", true);
			setDisabled("dtVigenciaFinal", false);
			setFocus("dtVigenciaFinal", false);
		} else if (acaoVigenciaAtual == 2) {
			setDisabled(document,true);
			setDisabled("filial.idFilial", true);
			setDisabled("filial.sgFilial", true);
			setDisabled("filial.pessoa.nmFantasia", true);
			setDisabled("BotaoLimpar", false);
			setDisabled("dtVigenciaInicial", true);
			setDisabled("dtVigenciaFinal", true);
			setFocus("BotaoLimpar", false);
		}
	}

	function pageLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		// popula na sessao, variaveis hidden
		setElementValue("idFilialSessao", getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue("sgFilialSessao", getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue("nmFilialSessao", getNestedBeanPropertyValue(data,"pessoa.nmFantasia"));
		populaFilial();
		desabilitaCampos(data, error);
	}

</script>
