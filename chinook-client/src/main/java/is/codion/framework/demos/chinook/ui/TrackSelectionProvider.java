/*
 * Copyright (c) 2004 - 2021, Björn Darri Sigurðsson. All Rights Reserved.
 */
package is.codion.framework.demos.chinook.ui;

import is.codion.framework.model.EntitySearchModel;
import is.codion.swing.framework.model.SwingEntityTableModel;
import is.codion.swing.framework.ui.EntitySearchField;

import java.awt.Dimension;

import static is.codion.framework.demos.chinook.domain.api.Chinook.Track;
import static javax.swing.SortOrder.ASCENDING;

final class TrackSelectionProvider extends EntitySearchField.TableSelectionProvider {

  TrackSelectionProvider(EntitySearchModel trackSearchModel) {
    super(trackSearchModel);
    SwingEntityTableModel tableModel = (SwingEntityTableModel) table().getModel();
    tableModel.columnModel().setVisibleColumns(Track.ARTIST_DENORM, Track.ALBUM_FK, Track.NAME);
    tableModel.sortModel().setSortOrder(Track.ARTIST_DENORM, ASCENDING);
    tableModel.sortModel().addSortOrder(Track.ALBUM_FK, ASCENDING);
    tableModel.sortModel().addSortOrder(Track.NAME, ASCENDING);
    setPreferredSize(new Dimension(500, 300));
  }
}
