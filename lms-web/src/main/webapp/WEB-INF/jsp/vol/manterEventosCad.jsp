<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.manterEventosAction" >
	<adsm:form action="/vol/manterEventos" idProperty="idTipoEvento">
	
		<adsm:textbox  dataType="integer" property="nmCodigo" label="codigo" width="30%" size="10" maxLength="10" required="true"/>
		<adsm:textbox dataType="text" property="dsNome" label="descricao" width="70%" size="65" maxLength="60" required="true"/>
		<adsm:combobox property="tpTipoEvento" label="tipoEvento" width="35%" domain="DM_TP_EVENTO_CEL"
						required="true" autoLoad="true" onlyActiveValues="false" />				
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>