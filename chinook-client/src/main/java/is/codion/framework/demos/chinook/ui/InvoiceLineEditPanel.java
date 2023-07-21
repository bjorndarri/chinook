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
 * Copyright (c) 2004 - 2021, Björn Darri Sigurðsson.
 */
package is.codion.framework.demos.chinook.ui;

import is.codion.framework.demos.chinook.domain.api.Chinook.InvoiceLine;
import is.codion.swing.framework.model.SwingEntityEditModel;
import is.codion.swing.framework.ui.EntityEditPanel;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import java.awt.BorderLayout;

import static is.codion.swing.common.ui.component.Components.flexibleGridLayoutPanel;
import static is.codion.swing.common.ui.component.Components.toolBar;
import static is.codion.swing.common.ui.component.text.TextComponents.preferredTextFieldHeight;
import static is.codion.swing.common.ui.layout.Layouts.borderLayout;

public final class InvoiceLineEditPanel extends EntityEditPanel {

  private final JTextField tableSearchField;

  public InvoiceLineEditPanel(SwingEntityEditModel editModel, JTextField tableSearchField) {
    super(editModel);
    this.tableSearchField = tableSearchField;
    editModel.setPersistValue(InvoiceLine.TRACK_FK, false);
  }

  @Override
  protected void initializeUI() {
    setInitialFocusAttribute(InvoiceLine.TRACK_FK);

    createForeignKeySearchField(InvoiceLine.TRACK_FK)
            .selectionProviderFactory(TrackSelectionProvider::new)
            .columns(15);
    createTextField(InvoiceLine.QUANTITY)
            .selectAllOnFocusGained(true)
            .columns(2)
            .action(control(ControlCode.INSERT));

    JToolBar updateToolBar = toolBar()
            .floatable(false)
            .action(control(ControlCode.UPDATE))
            .preferredHeight(preferredTextFieldHeight())
            .build();

    JPanel eastPanel = flexibleGridLayoutPanel(1, 2)
            .add(createInputPanel(new JLabel(" "), updateToolBar))
            .add(createInputPanel(new JLabel(" "), tableSearchField))
            .build();

    setLayout(borderLayout());
    addInputPanel(InvoiceLine.TRACK_FK, BorderLayout.WEST);
    addInputPanel(InvoiceLine.QUANTITY, BorderLayout.CENTER);
    add(eastPanel, BorderLayout.EAST);
  }
}