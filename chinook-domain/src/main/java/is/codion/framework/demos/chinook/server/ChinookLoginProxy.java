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
package is.codion.framework.demos.chinook.server;

import is.codion.common.db.database.Database;
import is.codion.common.db.exception.DatabaseException;
import is.codion.common.db.pool.ConnectionPoolFactory;
import is.codion.common.db.pool.ConnectionPoolWrapper;
import is.codion.common.rmi.server.LoginProxy;
import is.codion.common.rmi.server.RemoteClient;
import is.codion.common.rmi.server.exception.LoginException;
import is.codion.common.rmi.server.exception.ServerAuthenticationException;
import is.codion.common.user.User;
import is.codion.framework.db.EntityConnection;
import is.codion.framework.domain.DefaultDomain;
import is.codion.framework.domain.Domain;
import is.codion.framework.domain.DomainType;
import is.codion.framework.domain.entity.EntityType;
import is.codion.framework.domain.entity.attribute.Column;

import java.util.Optional;

import static is.codion.framework.db.local.LocalEntityConnection.localEntityConnection;
import static is.codion.framework.domain.DomainType.domainType;
import static is.codion.framework.domain.entity.attribute.Condition.and;
import static java.lang.String.valueOf;

/**
 * A {@link is.codion.common.rmi.server.LoginProxy} implementation
 * authenticating via a user lookup table.
 */
public final class ChinookLoginProxy implements LoginProxy {

  /**
   * The Database instance we're connecting to.
   */
  private final Database database = Database.instance();

  /**
   * The actual user credentials to return for successfully authenticated users.
   * Also used for user lookup.
   */
  private final User databaseUser = User.parse("scott:tiger");

  /**
   * The Domain containing the authentication table.
   */
  private final Domain domain = new Authentication();

  /**
   * The ConnectionPool used when authenticating users.
   */
  private final ConnectionPoolWrapper connectionPool;

  public ChinookLoginProxy() throws DatabaseException {
    connectionPool = ConnectionPoolFactory.instance().createConnectionPoolWrapper(database, databaseUser);
  }

  /**
   * Handles logins from clients with this id
   */
  @Override
  public Optional<String> clientTypeId() {
    return Optional.of("is.codion.framework.demos.chinook.ui.ChinookAppPanel");
  }

  @Override
  public RemoteClient login(RemoteClient remoteClient) throws LoginException {
    authenticateUser(remoteClient.user());

    //Create a new RemoteClient based on the one received
    //but with the actual database user
    return remoteClient.withDatabaseUser(databaseUser);
  }

  @Override
  public void logout(RemoteClient remoteClient) {}

  @Override
  public void close() {
    connectionPool.close();
  }

  private void authenticateUser(User user) throws LoginException {
    try (EntityConnection connection = getConnectionFromPool()) {
      int rows = connection.count(and(
              Authentication.User.USERNAME
                      .equalToIgnoreCase(user.username()),
              Authentication.User.PASSWORD_HASH
                      .equalTo(valueOf(user.password()).hashCode())));
      if (rows == 0) {
        throw new ServerAuthenticationException("Wrong username or password");
      }
    }
    catch (DatabaseException e) {
      throw new RuntimeException(e);
    }
  }

  private EntityConnection getConnectionFromPool() throws DatabaseException {
    return localEntityConnection(database, domain, connectionPool.connection(databaseUser));
  }

  private static final class Authentication extends DefaultDomain {

    private static final DomainType DOMAIN = domainType(Authentication.class);

    interface User {
      EntityType TYPE = DOMAIN.entityType("chinook.users");
      Column<Integer> ID = TYPE.integerColumn("userid");
      Column<String> USERNAME = TYPE.stringColumn("username");
      Column<Integer> PASSWORD_HASH = TYPE.integerColumn("passwordhash");
    }

    private Authentication() {
      super(DOMAIN);
      add(User.TYPE.define(
              User.ID.define()
                      .primaryKey(),
              User.USERNAME.define()
                      .column(),
              User.PASSWORD_HASH.define()
                      .column())
              .readOnly(true));
    }
  }
}
