<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.gm.rotaEmbarqueService">
	<adsm:form action="/expedicao/manterRotaMasterGM" idProperty="idRotaEmbarque">
		<adsm:textbox 
			dataType="text" 
			property="siglaRota" 
			label="sigla" 
			maxLength="5" 
			size="15" 
		/>
		<adsm:textbox 
			dataType="text" 
			property="nomeRota" 
			label="nome" 
			maxLength="25" 
			size="30" 
		/>
		<adsm:buttonBar 
			freeLayout="true">
			<adsm:findButton callbackProperty="rotaEmbarque"/>
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	
	<adsm:grid idProperty="idRotaEmbarque" property="rotaEmbarque" gridHeight="200" defaultOrder="siglaRota" unique="true" rows="13">
 	    
 	  	<adsm:gridColumn title="sigla" property="siglaRota" width="25%" />
		<adsm:gridColumn title="nomeRota" property="nomeRota"  width="50%" />
		<adsm:gridColumn title="horarioCorte" property="horarioCorte" width="25%" dataType="JTTime"/>
 	
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>