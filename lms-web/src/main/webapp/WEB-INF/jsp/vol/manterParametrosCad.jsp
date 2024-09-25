<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>					  
<adsm:window service="lms.vol.manterParametrosAction" >
	<adsm:form action="/vol/manterParametros" idProperty="idParametro">
	
		<adsm:lookup service="lms.vol.manterParametrosAction.findLookupFilial" dataType="text" property="filial" 
				idProperty="idFilial" criteriaProperty="sgFilial" label="filial" size="5" maxLength="3"
				action="/municipios/manterFiliais" width="70%" required="true" labelWidth="20%"/>
		
		<adsm:textbox dataType="integer" property="refresh" label="atualizacaoGerencial" labelWidth="20%" width="35%" size="10" maxLength="5" required="true"/>
		<adsm:textbox dataType="integer" property="alertaCel" label="alertaCel" width="35%"  labelWidth="20%" size="10" maxLength="5" required="true"/>

		<adsm:textbox dataType="integer" property="alertaBaixa" label="alertaBaixa" width="35%" size="10"  labelWidth="20%" maxLength="5" required="true"/>
		<adsm:textbox dataType="text" property="emailRecusa" label="emailRecusa" width="35%" size="30"  labelWidth="20%" maxLength="50" required="true"/>		
				
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>