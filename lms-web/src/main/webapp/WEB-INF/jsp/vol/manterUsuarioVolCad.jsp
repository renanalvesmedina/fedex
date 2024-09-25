<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterUsuarioVolAction">
	<adsm:form action="/vol/manterUsuarioVol" idProperty="idUsuario">

		<adsm:lookup size="16" maxLength="60" labelWidth="15%" width="85%"
			label="login"
			property="loginUsuario" idProperty="idLogin"
			criteriaProperty="login"
			action="/seguranca/manterUsuarioADSM" 
			dataType="text"
			required="true">
		</adsm:lookup>

		<adsm:checkbox property="ctrAparelho" label="ctrAparelho" labelWidth="15%" width="85%"/>

		<adsm:checkbox property="termo" label="termo" labelWidth="15%" width="85%"/>

		<adsm:buttonBar>
			<adsm:storeButton callbackProperty="afterStore"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
</adsm:window>
