<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window title="manterMotivosAgendamento" service="lms.entrega.manterMotivosAgendamentoAction">
	<adsm:form action="/entrega/manterMotivosAgendamento">
		<adsm:textbox dataType="text" property="dsMotivoAgendamento" label="descricao" size="60" maxLength="60" labelWidth="18%" width="82%" required="false" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="18%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="manterMotivosAgendamento"/>
			<adsm:button caption="limpar" onclick="limpar_OnClick();" disabled="false" buttonType="newButton"/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid
		idProperty="idMotivoAgendamento"
		property="manterMotivosAgendamento"
		defaultOrder="dsMotivoAgendamento"
		unique="true"
		rows="13"
		selectionMode="check"
	>
		<adsm:gridColumn title="descricao" property="dsMotivoAgendamento" dataType="text"/>
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
	function limpar_OnClick() {
		resetValue("dsMotivoAgendamento");
		resetValue("tpSituacao");
		setFocus("dsMotivoAgendamento", false);
	}
</script>
