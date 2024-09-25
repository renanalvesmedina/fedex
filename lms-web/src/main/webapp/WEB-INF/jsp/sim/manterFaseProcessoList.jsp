<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.manterFaseProcessoAction">
	<adsm:form action="/sim/manterFaseProcesso" idProperty="idFaseProcesso">
		<adsm:textbox label="codigo" property="cdFase" dataType="integer" maxValue="999" maxLength="3" minValue="0" width="9%" labelWidth="10%" size="5"/>
		<adsm:textbox label="descricao" property="dsFase" dataType="text" maxLength="50" width="90%" labelWidth="10%" size="51"/>

	 	<adsm:buttonBar freeLayout="true">
	   		<adsm:findButton callbackProperty="faseProcesso" />
	   		<adsm:resetButton />
	    </adsm:buttonBar>
	</adsm:form>

    <adsm:grid idProperty="idFaseProcesso" property="faseProcesso" rows="12" defaultOrder="cdFase, dsFase">
    	<adsm:gridColumn property="idFaseProcessoGrid" title="identificacao" dataType="integer" width="15%"/>
		<adsm:gridColumn property="cdFase" title="codigo" dataType="integer" width="15%"/>
		<adsm:gridColumn property="dsFase" title="descricao" width="70%"/>
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>		
    </adsm:grid>
	
</adsm:window>
