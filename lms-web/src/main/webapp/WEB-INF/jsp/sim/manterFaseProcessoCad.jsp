<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterFaseProcessoAction">
	<adsm:form action="/sim/manterFaseProcesso" idProperty="idFaseProcesso">
    	<adsm:textbox label="codigo" property="cdFase" dataType="integer" maxValue="999" maxLength="3" minValue="0" width="90%" labelWidth="10%" size="5" required="true"/>
		<adsm:textbox label="descricao" property="dsFase" dataType="text" maxLength="50" width="90%" labelWidth="10%" size="51" required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
		</adsm:buttonBar>
    </adsm:form>  
</adsm:window>