<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.motivoCancelDescargaService">
	<adsm:form action="/recepcaoDescarga/manterMotivosCancelamentoInicioDescarga" idProperty="idMotivoCancelDescarga">
		<adsm:textbox property="dsMotivo" label="descricao" dataType="text" size="65%" width="85%" maxLength="60" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" labelWidth="15%" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>