<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="manterMotivosAgendamento" service="lms.entrega.manterMotivosAgendamentoAction">

	<adsm:form idProperty="idMotivoAgendamento" action="/entrega/manterMotivosAgendamento">
		<adsm:textbox dataType="text" property="dsMotivoAgendamento" label="descricao" size="60" maxLength="60" labelWidth="18%" width="82%" required="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%" required="true"/>
		<adsm:buttonBar>
			<adsm:button caption="salvar" id="storeButtom" onclick="store()"/>
			<adsm:newButton id="BotaoLimpar" />
			<adsm:removeButton disabled="true" id="removeButton"/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script language="javascript" type="text/javascript">
	function initWindow(evento) {
		setDisabled(document,false);
		setDisabled("storeButtom", false);
		setDisabled("removeButton", true);
	}

	function store() {
		storeEditGridScript('lms.entrega.manterMotivosAgendamentoAction.storeCustom', 'afterStore', document.forms[0]);
		setDisabled("removeButton", false);
	}

	function afterStore_cb(data, error) {
		store_cb(data,error);
		setDisabled("removeButton",false);
		
	}
</script>
