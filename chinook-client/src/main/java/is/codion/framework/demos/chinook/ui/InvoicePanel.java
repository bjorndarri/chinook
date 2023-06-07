package is.codion.framework.demos.chinook.ui;

import is.codion.framework.demos.chinook.domain.api.Chinook.Invoice;
import is.codion.swing.framework.model.SwingEntityModel;
import is.codion.swing.framework.ui.EntityPanel;

public final class InvoicePanel extends EntityPanel {

  public InvoicePanel(SwingEntityModel invoiceModel, EntityPanel invoiceLinePanel) {
    super(invoiceModel, new InvoiceEditPanel(invoiceModel.editModel(), invoiceLinePanel));
    tablePanel().excludeFromUpdateMenu(Invoice.TOTAL);
    setIncludeDetailTabPane(false);
    setIncludeDetailPanelControls(false);
    addDetailPanel(invoiceLinePanel);
  }
}
