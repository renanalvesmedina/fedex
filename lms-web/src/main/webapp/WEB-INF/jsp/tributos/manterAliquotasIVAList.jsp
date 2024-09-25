<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterAliquotasIVAAction">
	<adsm:form 
		action="/tributos/manterAliquotasIVA"
		idProperty="idAliquotaIva">
        
		<adsm:lookup
			service="lms.tributos.manterAliquotasIVAAction.findLookupPais" 
			action="/municipios/manterPaises" 
			property="pais" 
			idProperty="idPais" 
			criteriaProperty="nmPais" 
			label="pais"
			minLengthForAutoPopUpSearch="3" 
			exactMatch="false"
			dataType="text" 
			size="30" 
			maxLength="60"
		/>

		<adsm:range 
			label="vigencia" 
		>
	        <adsm:textbox 
	        	dataType="JTDate" 
	        	property="dtVigencia"
	        />
        </adsm:range>

       
		<adsm:textbox 
			size="6" 
			dataType="percent" 
			property="pcAliquota" 
			label="percentualAliquota"
			minValue="0"
			maxValue="100"
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="aliquotasIVA"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid selectionMode="check" idProperty="idAliquotaIva" property="aliquotasIVA"  rows="13" unique="true" defaultOrder="pais_.nmPais, dtVigenciaInicial">
        <adsm:gridColumn title="pais" property="pais.nmPais" width="45%" />
		<adsm:gridColumn title="vigenciaInicial" dataType="date" property="dtVigenciaInicial" width="20%" />
		<adsm:gridColumn title="vigenciaFinal" dataType="date" property="dtVigenciaFinal" width="20%" />
		<adsm:gridColumn title="percentualAliquota" property="pcAliquota"  dataType="percent" width="15%" />

		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
