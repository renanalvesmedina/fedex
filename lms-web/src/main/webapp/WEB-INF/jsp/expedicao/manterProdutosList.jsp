<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.manterProdutosAction">
	<adsm:form action="/expedicao/manterProdutos" idProperty="idNomeProduto" height="133">
		<adsm:combobox label="categoria"
					   property="categoriaProduto" 
					   optionLabelProperty="dsCategoriaProduto" 
					   renderOptions="true"
					   optionProperty="idCategoriaProduto" 
					   service="lms.expedicao.categoriaProdutoService.find"
					   onlyActiveValues="true"
					   labelWidth="19%"
					   />
			
		<adsm:combobox  property="naturezaProduto.idNaturezaProduto" 
						optionLabelProperty="dsNaturezaProduto" 
						optionProperty="idNaturezaProduto" 
						service="lms.expedicao.naturezaProdutoService.find" 
						label="naturezaProduto" labelWidth="19%" width="45%" 
		/>
		
		<adsm:combobox  property="tipoProduto.idTipoProduto" 
						optionLabelProperty="dsTipoProduto" 
						optionProperty="idTipoProduto"  
						service="lms.expedicao.tipoProdutoService.find" 
						label="tipoProduto" boxWidth="240" labelWidth="19%"
		/>
		
		<adsm:textbox   maxLength="80" dataType="text" 
						property="dsNomeProduto" label="produto" 
						size="83" labelWidth="19%" width="81%" 
		/>
		<adsm:combobox  property="classeRisco.idClasseRisco" 
						optionLabelProperty="nrClasseRisco" 
						optionProperty="idClasseRisco" 
						service="lms.expedicao.classeRiscoService.find" 
						label="classeRisco" labelWidth="19%" width="31%" 
		/>
		<adsm:combobox  property="subClasseRisco.idSubClasseRisco" 
						optionLabelProperty="nrSubClasseRisco" 
						optionProperty="idSubClasseRisco" 
						service="lms.expedicao.subClasseRiscoService.find" 
						label="subClasseRisco" labelWidth="19%" width="31%" 
		/>		
		<adsm:combobox  property="tpSituacao" 
						domain="DM_STATUS" 
						label="situacao" 
						labelWidth="19%" 
						width="31%" 
		/>
		<adsm:hidden property="tpFiltraProdutoPerigosoRisco" serializable="false"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="produto"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idNomeProduto" 
			property="produto"
			scrollBars="horizontal" 
			gridHeight="140"
			defaultOrder="dsNomeProduto"
			rows="6"
			service="lms.expedicao.manterProdutosAction.findPaginated"
			rowCountService="lms.expedicao.manterProdutosAction.getRowCount">
			
		<adsm:gridColumn 
			title="produto" 
			property="dsNomeProduto" 
			width="200" />
		
		<adsm:gridColumn 
			title="tpNomeProduto" 
			property="tpNomeProduto" 
			isDomain="true" 
			width="150" />
			
		<adsm:gridColumn 
			title="naturezaProduto" 
			property="produto.naturezaProduto.dsNaturezaProduto" 
			width="100" />
			
		<adsm:gridColumn 
			title="tipoProduto" 
			property="produto.tipoProduto.dsTipoProduto" 
			width="100" />
		
		<adsm:gridColumn 
			title="nrOnu" 
			property="produto.nrOnu" 
			width="60" />
		
		<adsm:gridColumn 
			title="classeRisco" 
			property="produto.classeRisco.nrClasseRisco" 
			width="70" />
		
		<adsm:gridColumn 
			title="subClasseRisco" 
			property="produto.subClasseRisco.nrSubClasseRisco" 
			width="70" />
		
		<adsm:gridColumn 
			title="numeroOrdem" 
			property="produto.nrOrdem" 
			width="70" />
		
		<adsm:gridColumn 
			title="nrNcm" 
			property="produto.nrNcm" 
			width="70" />
		
		<adsm:gridColumn 
			title="produtoProibido" 
			property="produto.blProdutoProibido" 
			renderMode="image-check" 
			width="50" />
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script language="javascript">


</script>