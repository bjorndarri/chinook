/*
 * Copyright (c) 2004 - 2021, Björn Darri Sigurðsson. All Rights Reserved.
 */
package is.codion.framework.demos.chinook.model;

import is.codion.common.db.exception.DatabaseException;
import is.codion.framework.db.EntityConnection;
import is.codion.framework.db.EntityConnectionProvider;
import is.codion.framework.demos.chinook.domain.api.Chinook.Playlist;
import is.codion.framework.demos.chinook.domain.api.Chinook.Playlist.RandomPlaylistParameters;
import is.codion.framework.domain.entity.Entity;
import is.codion.swing.framework.model.SwingEntityTableModel;

import java.util.Collections;

public final class PlaylistTableModel extends SwingEntityTableModel {

  public PlaylistTableModel(EntityConnectionProvider connectionProvider) {
    super(Playlist.TYPE, connectionProvider);
  }

   public void createRandomPlaylist(RandomPlaylistParameters parameters) throws DatabaseException {
    Entity randomPlaylist = createPlaylist(parameters);
    addEntitiesAt(0, Collections.singletonList(randomPlaylist));
    selectionModel().setSelectedItem(randomPlaylist);
  }

  private Entity createPlaylist(RandomPlaylistParameters parameters) throws DatabaseException {
    EntityConnection connection = connectionProvider().connection();
    connection.beginTransaction();
    try {
      Entity randomPlaylist = connection.executeFunction(Playlist.RANDOM_PLAYLIST, parameters);
      connection.commitTransaction();

      return randomPlaylist;
    }
    catch (DatabaseException e) {
      connection.rollbackTransaction();
      throw e;
    }
    catch (Exception e) {
      connection.rollbackTransaction();
      throw new RuntimeException(e);
    }
  }
}
