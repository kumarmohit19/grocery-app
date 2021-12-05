<%@ include file="common/header.jspf"%>
<%@ include file="common/navigation.jspf"%>

<div class="container">
	<table class="table table-striped">
		<caption><spring:message code="grocery.caption" /></caption>
		<thead>
			<tr>
				<th>Item</th>
				<th>Quantity</th>
				<th>Price per unit</th>
				<th>Total Price</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${groceries}" var="item">
				<tr>
					<td>${item.item}</td>
					<td>${item.quantity}</td>
					<td>${item.pricePerUnit}</td>
					<td>${item.totalPrice}</td>
					<td><a type="button" class="btn btn-primary"
						href="/update-item?id=${item.id}">Edit</a> <a type="button"
						class="btn btn-warning" href="/delete-item?id=${item.id}">Delete</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div>
		<a type="button" class="btn btn-success" href="/add-item">Add</a>
	</div>
</div>
<%@ include file="common/footer.jspf"%>