<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.workflow.manterComitesAction">
	<adsm:form action="/workflow/manterComites" idProperty="idComite">
		<adsm:textbox dataType="text" size="90%" width="85%" property="nmComite" label="nome" maxLength="60" required="true"/>
		<adsm:combobox property="tpModal" domain="DM_MODAL" label="modal" onlyActiveValues="true"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" onlyActiveValues="true"/>
		<adsm:buttonBar>
			<adsm:button action="/workflow/manterIntegrantesComite" caption="integrantes" cmd="main">
				<adsm:linkProperty src="idComite" target="comite.idComite"/>
				<adsm:linkProperty src="nmComite" target="comite.nmComite"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>