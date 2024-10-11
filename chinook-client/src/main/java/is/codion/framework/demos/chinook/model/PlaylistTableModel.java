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
 * Copyright (c) 2004 - 2024, Björn Darri Sigurðsson.
 */
package is.codion.framework.demos.chinook.model;

import is.codion.common.db.exception.DatabaseException;
import is.codion.framework.db.EntityConnection;
import is.codion.framework.db.EntityConnectionProvider;
import is.codion.framework.demos.chinook.domain.api.Chinook.Playlist;
import is.codion.framework.demos.chinook.domain.api.Chinook.Playlist.RandomPlaylistParameters;
import is.codion.framework.domain.entity.Entity;
import is.codion.swing.framework.model.SwingEntityTableModel;

import static is.codion.framework.db.EntityConnection.transaction;

public final class PlaylistTableModel extends SwingEntityTableModel {

	public PlaylistTableModel(EntityConnectionProvider connectionProvider) {
		super(new PlaylistEditModel(connectionProvider));
	}

	public void createRandomPlaylist(RandomPlaylistParameters parameters) throws DatabaseException {
		EntityConnection connection = connection();
		Entity randomPlaylist = transaction(connection, () -> connection.execute(Playlist.RANDOM_PLAYLIST, parameters));
		items().visible().addItemAt(0, randomPlaylist);
		selection().item().set(randomPlaylist);
	}
}
