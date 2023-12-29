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

import is.codion.framework.demos.chinook.model.EmployeeEditModel;
import is.codion.framework.domain.entity.Entity;
import is.codion.framework.domain.entity.attribute.Attribute;
import is.codion.swing.common.model.worker.ProgressWorker.Task;
import is.codion.swing.common.ui.control.Control.Command;
import is.codion.swing.common.ui.dialog.ProgressWorkerDialogBuilder;
import is.codion.swing.framework.model.SwingEntityEditModel;
import is.codion.swing.framework.model.SwingEntityTableModel;
import is.codion.swing.framework.ui.EntityDialogs.EditDialogBuilder;
import is.codion.swing.framework.ui.EntityTablePanel;

import java.util.Collection;

import static is.codion.framework.demos.chinook.ui.EmployeeEditPanel.DELETING;
import static is.codion.framework.demos.chinook.ui.EmployeeEditPanel.UPDATING;
import static is.codion.swing.common.ui.dialog.Dialogs.progressWorkerDialog;

public final class EmployeeTablePanel extends EntityTablePanel {

  public EmployeeTablePanel(SwingEntityTableModel tableModel) {
    super(tableModel);
  }

  @Override
  protected void setupControls() {
    control(TableControl.DELETE_SELECTED).map(delete -> delete.copy(new DeleteCommand()).build());
  }

  @Override
  protected <T> EditDialogBuilder<T> editDialogBuilder(Attribute<T> attribute) {
    return super.editDialogBuilder(attribute)
            .updater(new EmployeeUpdater());
  }

  private final class DeleteCommand implements Command {

    @Override
    public void execute() {
      if (confirmDelete()) {
        EmployeeEditModel editModel = tableModel().editModel();
        EmployeeEditModel.DeleteMany delete = editModel.createDelete(tableModel().selectionModel().getSelectedItems());
        createWorker(delete::execute, DELETING)
                .onResult(delete::onResult)
                .execute();
      }
    }
  }

  private final class EmployeeUpdater implements EditDialogBuilder.Updater {

    @Override
    public void update(SwingEntityEditModel editModel, Collection<Entity> entities) {
      EmployeeEditModel employeeEditModel = (EmployeeEditModel) editModel;
      EmployeeEditModel.UpdateMany update = employeeEditModel.createUpdate(entities);
      createWorker(update::execute, UPDATING)
              .onResult(update::onResult)
              .execute();
    }
  }

  private ProgressWorkerDialogBuilder<Collection<Entity>, ?> createWorker(Task<Collection<Entity>> task, String dialogTitle) {
    return progressWorkerDialog(task)
            .title(dialogTitle)
            .owner(EmployeeTablePanel.this)
            .onException(this::onException);
  }
}
