/*
 * This file is part of Codion Chinook Demo.
 *
 * Codion Chinook Demo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Codion Chinook Demo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Codion Chinook Demo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (c) 2023 - 2024, Björn Darri Sigurðsson.
 */
package is.codion.framework.demos.chinook.client.loadtest.scenarios;

import is.codion.framework.db.EntityConnection;
import is.codion.framework.db.EntityConnectionProvider;
import is.codion.framework.demos.chinook.domain.api.Chinook.Customer;
import is.codion.framework.demos.chinook.domain.api.Chinook.Invoice;
import is.codion.framework.demos.chinook.domain.api.Chinook.InvoiceLine;
import is.codion.framework.domain.entity.Entity;
import is.codion.tools.loadtest.LoadTest.Scenario.Performer;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static is.codion.framework.db.EntityConnection.transaction;
import static is.codion.framework.demos.chinook.client.loadtest.scenarios.LoadTestUtil.RANDOM;
import static is.codion.framework.demos.chinook.client.loadtest.scenarios.LoadTestUtil.randomCustomerId;
import static is.codion.framework.domain.entity.Entity.distinct;

public final class UpdateTotals implements Performer<EntityConnectionProvider> {

	@Override
	public void perform(EntityConnectionProvider connectionProvider) {
		EntityConnection connection = connectionProvider.connection();
		Entity customer = connection.selectSingle(Customer.ID.equalTo(randomCustomerId()));
		List<Long> invoiceIds = connection.select(Invoice.ID, Invoice.CUSTOMER_FK.equalTo(customer));
		if (!invoiceIds.isEmpty()) {
			Entity invoice = connection.selectSingle(Invoice.ID.equalTo(invoiceIds.get(RANDOM.nextInt(invoiceIds.size()))));
			Collection<Entity> invoiceLines = connection.select(InvoiceLine.INVOICE_FK.equalTo(invoice));
			invoiceLines.forEach(invoiceLine ->
							invoiceLine.put(InvoiceLine.QUANTITY, RANDOM.nextInt(4) + 1));
			updateInvoiceLines(invoiceLines.stream()
							.filter(Entity::modified)
							.collect(Collectors.toList()), connection);
		}
	}

	private static void updateInvoiceLines(Collection<Entity> invoiceLines, EntityConnection connection) {
		transaction(connection, () -> {
			connection.update(invoiceLines);
			connection.execute(Invoice.UPDATE_TOTALS, distinct(InvoiceLine.INVOICE_ID, invoiceLines));
		});
	}
}
