package is.codion.framework.demos.chinook.client.loadtest.scenarios;

import is.codion.framework.demos.chinook.domain.api.Chinook.Customer;
import is.codion.framework.demos.chinook.domain.api.Chinook.Invoice;
import is.codion.framework.demos.chinook.model.ChinookApplicationModel;
import is.codion.swing.framework.model.SwingEntityModel;
import is.codion.swing.framework.tools.loadtest.AbstractEntityUsageScenario;

import static is.codion.swing.framework.tools.loadtest.EntityLoadTestModel.selectRandomRow;

public final class ViewInvoice extends AbstractEntityUsageScenario<ChinookApplicationModel> {

  @Override
  protected void perform(ChinookApplicationModel application) throws Exception {
    SwingEntityModel customerModel = application.getEntityModel(Customer.TYPE);
    customerModel.getTableModel().refresh();
    selectRandomRow(customerModel.getTableModel());
    SwingEntityModel invoiceModel = customerModel.getDetailModel(Invoice.TYPE);
    selectRandomRow(invoiceModel.getTableModel());
  }

  @Override
  public int getDefaultWeight() {
    return 10;
  }
}
