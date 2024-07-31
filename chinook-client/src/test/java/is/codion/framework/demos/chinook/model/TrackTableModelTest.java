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
package is.codion.framework.demos.chinook.model;

import is.codion.common.db.exception.DatabaseException;
import is.codion.common.user.User;
import is.codion.framework.db.EntityConnectionProvider;
import is.codion.framework.db.local.LocalEntityConnectionProvider;
import is.codion.framework.demos.chinook.domain.ChinookImpl;
import is.codion.framework.demos.chinook.domain.api.Chinook.Album;
import is.codion.framework.demos.chinook.domain.api.Chinook.Track;
import is.codion.framework.domain.entity.Entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TrackTableModelTest {

	@Test
	public void raisePriceOfSelected() throws DatabaseException {
		try (EntityConnectionProvider connectionProvider = createConnectionProvider()) {
			Entity masterOfPuppets = connectionProvider.connection()
							.selectSingle(Album.TITLE.equalTo("Master Of Puppets"));

			TrackTableModel trackTableModel = new TrackTableModel(connectionProvider);
			trackTableModel.conditionModel()
						.setEqualConditionValue(Track.ALBUM_FK, masterOfPuppets);

			trackTableModel.refresh();
			assertEquals(8, trackTableModel.getRowCount());

			trackTableModel.selectionModel().selectAll();
			trackTableModel.raisePriceOfSelected(BigDecimal.ONE);

			trackTableModel.items().forEach(track ->
							assertEquals(BigDecimal.valueOf(1.99), track.get(Track.UNITPRICE)));
		}
	}

	private static EntityConnectionProvider createConnectionProvider() {
		return LocalEntityConnectionProvider.builder()
						.domain(new ChinookImpl())
						.user(User.parse("scott:tiger"))
						.build();
	}
}
