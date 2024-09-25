<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterComitesAction">
	<adsm:form action="/workflow/manterComites" idProperty="idComite">
		<adsm:textbox dataType="text" size="90%" width="85%" property="nmComite" label="nome" maxLength="60"/>
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="comites"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idComite" property="comites" gridHeight="200" unique="true" defaultOrder="nmComite" rows="13">
		<adsm:gridColumn title="nome" property="nmComite" width="70%" />
		<adsm:gridColumn title="modal" property="tpModal" isDomain="true" width="15%" />
		<adsm:gridColumn title="abrangencia" property="tpAbrangencia" isDomain="true" width="15%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
