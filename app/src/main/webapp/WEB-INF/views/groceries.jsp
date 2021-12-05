<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>
<div class="container">
	<form:form method="post" commandName="grocery">
		<form:hidden path="id" />
		<fieldset class="form-group">
			<form:label path="item">Item</form:label>
			<form:input path="item" type="text" class="form-control"
				required="required" />
			<form:errors path="item" cssClass="text-warning" />
		</fieldset>
		<fieldset class="form-group">
			<form:label path="quantity">Quantity</form:label>
			<form:input id="quantity" path="quantity" type="text" class="form-control"
				required="required" />
			<form:errors path="quantity" cssClass="text-warning" />
		</fieldset>
		<fieldset class="form-group">
			<form:label path="pricePerUnit">Price per unit</form:label>
			<form:input id="price" path="pricePerUnit" type="text" class="form-control"
				required="required" />
			<form:errors path="pricePerUnit" cssClass="text-warning" />
		</fieldset>
		<fieldset class="form-group">
			<form:label path="totalPrice">Total price</form:label>
			<form:input id="totalPrice" path="totalPrice" type="text" class="form-control"
				required="required" />
			<form:errors path="totalPrice" cssClass="text-warning" />
		</fieldset>
		<button type="submit" class="btn btn-success">Submit</button>
	</form:form>
</div>

<%@ include file="common/footer.jspf"%>

<script type="text/javascript">
    $('#quantity').keyup(totalPrice);
    $('#price').keyup(totalPrice);
    
    function totalPrice(){
        $('#totalPrice').val(($('#quantity').val() * $('#price').val()));
    }
</script>