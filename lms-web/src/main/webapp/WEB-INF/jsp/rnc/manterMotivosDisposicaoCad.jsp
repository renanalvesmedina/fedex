<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.rnc.motivoDisposicaoService" >
	<adsm:form action="/rnc/manterMotivosDisposicao" idProperty="idMotivoDisposicao" >
		<adsm:textbox dataType="text" property="dsMotivo" label="descricao" maxLength="50" size="50" width="85%" required="true" />
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:checkbox property="blReverteRespFilAbertura" label="reverteResponsavelFilialAbertura" labelWidth="24%" width="76%" />
		<adsm:checkbox property="blSomenteAutomatico" label="somenteAutomatico" labelWidth="24%" width="76%"  />
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>