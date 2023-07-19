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
package is.codion.framework.demos.chinook.domain.api;

import is.codion.common.db.operation.FunctionType;
import is.codion.framework.db.EntityConnection;
import is.codion.framework.domain.DomainType;
import is.codion.framework.domain.entity.Attribute;
import is.codion.framework.domain.entity.DefaultEntityValidator;
import is.codion.framework.domain.entity.Entity;
import is.codion.framework.domain.entity.EntityType;
import is.codion.framework.domain.entity.ForeignKey;
import is.codion.framework.domain.entity.exception.ValidationException;
import is.codion.framework.domain.property.DerivedProperty;
import is.codion.framework.domain.property.Property;
import is.codion.plugin.jasperreports.model.JRReportType;
import is.codion.plugin.jasperreports.model.JasperReports;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.regex.Pattern;

import static is.codion.common.db.operation.FunctionType.functionType;
import static is.codion.framework.domain.DomainType.domainType;
import static java.util.Objects.requireNonNull;

public interface Chinook {

  DomainType DOMAIN = domainType("ChinookImpl");

  // tag::artist[]
  interface Artist {
    EntityType TYPE = DOMAIN.entityType("artist@chinook", Artist.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("artistid");
    Attribute<String> NAME = TYPE.stringAttribute("name");
    Attribute<Integer> NUMBER_OF_ALBUMS = TYPE.integerAttribute("number_of_albums");
    Attribute<Integer> NUMBER_OF_TRACKS = TYPE.integerAttribute("number_of_tracks");
  }
  // end::artist[]

  // tag::album[]
  interface Album {
    EntityType TYPE = DOMAIN.entityType("album@chinook", Album.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("albumid");
    Attribute<String> TITLE = TYPE.stringAttribute("title");
    Attribute<Long> ARTIST_ID = TYPE.longAttribute("artistid");
    Attribute<byte[]> COVER = TYPE.byteArrayAttribute("cover");
    Attribute<Image> COVERIMAGE = TYPE.attribute("coverimage", Image.class);
    Attribute<Integer> NUMBER_OF_TRACKS = TYPE.integerAttribute("number_of_tracks");

    ForeignKey ARTIST_FK = TYPE.foreignKey("artist_fk", ARTIST_ID, Artist.ID);
  }
  // end::album[]

  // tag::employee[]
  interface Employee {
    EntityType TYPE = DOMAIN.entityType("employee@chinook", Employee.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("employeeid");
    Attribute<String> LASTNAME = TYPE.stringAttribute("lastname");
    Attribute<String> FIRSTNAME = TYPE.stringAttribute("firstname");
    Attribute<String> TITLE = TYPE.stringAttribute("title");
    Attribute<Long> REPORTSTO = TYPE.longAttribute("reportsto");
    Attribute<LocalDate> BIRTHDATE = TYPE.localDateAttribute("birthdate");
    Attribute<LocalDate> HIREDATE = TYPE.localDateAttribute("hiredate");
    Attribute<String> ADDRESS = TYPE.stringAttribute("address");
    Attribute<String> CITY = TYPE.stringAttribute("city");
    Attribute<String> STATE = TYPE.stringAttribute("state");
    Attribute<String> COUNTRY = TYPE.stringAttribute("country");
    Attribute<String> POSTALCODE = TYPE.stringAttribute("postalcode");
    Attribute<String> PHONE = TYPE.stringAttribute("phone");
    Attribute<String> FAX = TYPE.stringAttribute("fax");
    Attribute<String> EMAIL = TYPE.stringAttribute("email");

    ForeignKey REPORTSTO_FK = TYPE.foreignKey("reportsto_fk", REPORTSTO, Employee.ID);
  }
  // end::employee[]

  // tag::customer[]
  interface Customer {
    EntityType TYPE = DOMAIN.entityType("customer@chinook", Customer.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("customerid");
    Attribute<String> FIRSTNAME = TYPE.stringAttribute("firstname");
    Attribute<String> LASTNAME = TYPE.stringAttribute("lastname");
    Attribute<String> COMPANY = TYPE.stringAttribute("company");
    Attribute<String> ADDRESS = TYPE.stringAttribute("address");
    Attribute<String> CITY = TYPE.stringAttribute("city");
    Attribute<String> STATE = TYPE.stringAttribute("state");
    Attribute<String> COUNTRY = TYPE.stringAttribute("country");
    Attribute<String> POSTALCODE = TYPE.stringAttribute("postalcode");
    Attribute<String> PHONE = TYPE.stringAttribute("phone");
    Attribute<String> FAX = TYPE.stringAttribute("fax");
    Attribute<String> EMAIL = TYPE.stringAttribute("email");
    Attribute<Long> SUPPORTREP_ID = TYPE.longAttribute("supportrepid");

    ForeignKey SUPPORTREP_FK = TYPE.foreignKey("supportrep_fk", SUPPORTREP_ID, Employee.ID);

    JRReportType REPORT = JasperReports.reportType("customer_report");
  }
  // end::customer[]

  // tag::genre[]
  interface Genre {
    EntityType TYPE = DOMAIN.entityType("genre@chinook", Genre.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("genreid");
    Attribute<String> NAME = TYPE.stringAttribute("name");
  }
  // end::genre[]

  // tag::mediaType[]
  interface MediaType {
    EntityType TYPE = DOMAIN.entityType("mediatype@chinook", MediaType.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("mediatypeid");
    Attribute<String> NAME = TYPE.stringAttribute("name");
  }
  // end::mediaType[]

  // tag::track[]
  interface Track extends Entity {
    EntityType TYPE = DOMAIN.entityType("track@chinook", Track.class, Track.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("trackid");
    Attribute<String> NAME = TYPE.stringAttribute("name");
    Attribute<Entity> ARTIST_DENORM = TYPE.entityAttribute("artist_denorm");
    Attribute<Long> ALBUM_ID = TYPE.longAttribute("albumid");
    Attribute<Long> MEDIATYPE_ID = TYPE.longAttribute("mediatypeid");
    Attribute<Long> GENRE_ID = TYPE.longAttribute("genreid");
    Attribute<String> COMPOSER = TYPE.stringAttribute("composer");
    Attribute<Integer> MILLISECONDS = TYPE.integerAttribute("milliseconds");
    Attribute<String> MINUTES_SECONDS_DERIVED = TYPE.stringAttribute("minutes_seconds_derived");
    Attribute<Integer> BYTES = TYPE.integerAttribute("bytes");
    Attribute<BigDecimal> UNITPRICE = TYPE.bigDecimalAttribute("unitprice");
    Attribute<Void> RANDOM = TYPE.attribute("random()", Void.class);

    ForeignKey ALBUM_FK = TYPE.foreignKey("album_fk", ALBUM_ID, Album.ID);
    ForeignKey MEDIATYPE_FK = TYPE.foreignKey("mediatype_fk", MEDIATYPE_ID, MediaType.ID);
    ForeignKey GENRE_FK = TYPE.foreignKey("genre_fk", GENRE_ID, Genre.ID);

    FunctionType<EntityConnection, RaisePriceParameters, Collection<Entity>> RAISE_PRICE = functionType("chinook.raise_price");

    default void raisePrice(BigDecimal priceIncrease) {
      put(UNITPRICE, get(UNITPRICE).add(priceIncrease));
    }

    record RaisePriceParameters(Collection<Long> trackIds, BigDecimal priceIncrease) implements Serializable {

      public RaisePriceParameters {
        requireNonNull(trackIds);
        requireNonNull(priceIncrease);
      }
    }
  }
  // end::track[]

  // tag::invoice[]
  interface Invoice extends Entity {
    EntityType TYPE = DOMAIN.entityType("invoice@chinook", Invoice.class, Invoice.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("invoiceid");
    Attribute<Long> CUSTOMER_ID = TYPE.longAttribute("customerid");
    Attribute<LocalDate> DATE = TYPE.localDateAttribute("invoicedate");
    Attribute<String> BILLINGADDRESS = TYPE.stringAttribute("billingaddress");
    Attribute<String> BILLINGCITY = TYPE.stringAttribute("billingcity");
    Attribute<String> BILLINGSTATE = TYPE.stringAttribute("billingstate");
    Attribute<String> BILLINGCOUNTRY = TYPE.stringAttribute("billingcountry");
    Attribute<String> BILLINGPOSTALCODE = TYPE.stringAttribute("billingpostalcode");
    Attribute<BigDecimal> TOTAL = TYPE.bigDecimalAttribute("total");
    Attribute<BigDecimal> CALCULATED_TOTAL = TYPE.bigDecimalAttribute("calculated_total");

    ForeignKey CUSTOMER_FK = TYPE.foreignKey("customer_fk", CUSTOMER_ID, Customer.ID);

    FunctionType<EntityConnection, Collection<Long>, Collection<Entity>> UPDATE_TOTALS = functionType("chinook.update_totals");

    Property.ValueSupplier<LocalDate> DATE_DEFAULT_VALUE = LocalDate::now;

    default void updateTotal() {
      put(TOTAL, optional(CALCULATED_TOTAL).orElse(BigDecimal.ZERO));
    }
  }
  // end::invoice[]

  // tag::invoiceLine[]
  interface InvoiceLine {
    EntityType TYPE = DOMAIN.entityType("invoiceline@chinook", InvoiceLine.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("invoicelineid");
    Attribute<Long> INVOICE_ID = TYPE.longAttribute("invoiceid");
    Attribute<Long> TRACK_ID = TYPE.longAttribute("trackid");
    Attribute<BigDecimal> UNITPRICE = TYPE.bigDecimalAttribute("unitprice");
    Attribute<Integer> QUANTITY = TYPE.integerAttribute("quantity");
    Attribute<BigDecimal> TOTAL = TYPE.bigDecimalAttribute("total");

    ForeignKey INVOICE_FK = TYPE.foreignKey("invoice_fk", INVOICE_ID, Invoice.ID);
    ForeignKey TRACK_FK = TYPE.foreignKey("track_fk", TRACK_ID, Track.ID);
  }
  // end::invoiceLine[]

  // tag::playlist[]
  interface Playlist {
    EntityType TYPE = DOMAIN.entityType("playlist@chinook", Playlist.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("playlistid");
    Attribute<String> NAME = TYPE.stringAttribute("name");

    FunctionType<EntityConnection, RandomPlaylistParameters, Entity> RANDOM_PLAYLIST = functionType("chinook.random_playlist");

    record RandomPlaylistParameters(String playlistName, Integer noOfTracks, Collection<Entity> genres) implements Serializable {}
  }
  // end::playlist[]

  // tag::playlistTrack[]
  interface PlaylistTrack {
    EntityType TYPE = DOMAIN.entityType("playlisttrack@chinook", PlaylistTrack.class.getName());

    Attribute<Long> ID = TYPE.longAttribute("playlisttrackid");
    Attribute<Long> PLAYLIST_ID = TYPE.longAttribute("playlistid");
    Attribute<Long> TRACK_ID = TYPE.longAttribute("trackid");
    Attribute<Entity> ALBUM_DENORM = TYPE.entityAttribute("album_denorm");
    Attribute<Entity> ARTIST_DENORM = TYPE.entityAttribute("artist_denorm");

    ForeignKey PLAYLIST_FK = TYPE.foreignKey("playlist_fk", PLAYLIST_ID, Playlist.ID);
    ForeignKey TRACK_FK = TYPE.foreignKey("track_fk", TRACK_ID, Track.ID);
  }
  // end::playlistTrack[]

  // tag::invoiceLineTotalProvider[]
  final class InvoiceLineTotalProvider
          implements DerivedProperty.Provider<BigDecimal> {

    @Serial
    private static final long serialVersionUID = 1;

    @Override
    public BigDecimal get(DerivedProperty.SourceValues sourceValues) {
      Integer quantity = sourceValues.get(InvoiceLine.QUANTITY);
      BigDecimal unitPrice = sourceValues.get(InvoiceLine.UNITPRICE);
      if (unitPrice == null || quantity == null) {
        return null;
      }

      return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
  }
  // end::invoiceLineTotalProvider[]

  // tag::trackMinSecProvider[]
  final class TrackMinSecProvider
          implements DerivedProperty.Provider<String> {

    @Serial
    private static final long serialVersionUID = 1;

    @Override
    public String get(DerivedProperty.SourceValues sourceValues) {
      return sourceValues.optional(Track.MILLISECONDS)
              .map(TrackMinSecProvider::toMinutesSecondsString)
              .orElse(null);
    }

    private static String toMinutesSecondsString(Integer milliseconds) {
      return minutes(milliseconds) + " min " +
              seconds(milliseconds) + " sec";
    }
  }

  static Integer minutes(Integer milliseconds) {
    if (milliseconds == null) {
      return null;
    }

    return milliseconds / 1000 / 60;
  }

  static Integer seconds(Integer milliseconds) {
    if (milliseconds == null) {
      return null;
    }

    return milliseconds / 1000 % 60;
  }

  static Integer milliseconds(Integer minutes, Integer seconds) {
    int milliseconds = minutes == null ? 0 : minutes * 60 * 1000;
    milliseconds += seconds == null ? 0 : seconds * 1000;

    return milliseconds == 0 ? null : milliseconds;
  }
  // end::trackMinSecProvider[]

  // tag::coverArtImageProvider[]
  final class CoverArtImageProvider
          implements DerivedProperty.Provider<Image> {

    @Serial
    private static final long serialVersionUID = 1;

    @Override
    public Image get(DerivedProperty.SourceValues sourceValues) {
      return sourceValues.optional(Album.COVER)
              .map(CoverArtImageProvider::fromBytes)
              .orElse(null);
    }

    private static Image fromBytes(byte[] bytes) {
      try {
        return ImageIO.read(new ByteArrayInputStream(bytes));
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  // end::coverArtImageProvider[]

  // tag::customerStringFactory[]
  final class CustomerStringFactory
          implements Function<Entity, String>, Serializable {

    @Serial
    private static final long serialVersionUID = 1;

    @Override
    public String apply(Entity customer) {
      return new StringBuilder()
              .append(customer.get(Customer.LASTNAME))
              .append(", ")
              .append(customer.get(Customer.FIRSTNAME))
              .append(customer.optional(Customer.EMAIL)
                      .map(email -> " <" + email + ">")
                      .orElse(""))
              .toString();
    }
  }
  // end::customerStringFactory[]

  // tag::coverFormatter[]
  final class CoverFormatter extends Format {

    private final NumberFormat kbFormat = NumberFormat.getIntegerInstance();

    @Override
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos) {
      if (value != null) {
        toAppendTo.append(kbFormat.format(((byte[]) value).length / 1024) + " Kb");
      }

      return toAppendTo;
    }

    @Override
    public Object parseObject(String source, ParsePosition pos) {
      throw new UnsupportedOperationException();
    }
  }
  // end::coverFormatter[]

  // tag::emailValidator[]
  final class EmailValidator extends DefaultEntityValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle(Chinook.class.getName());

    private final Attribute<String> emailAttribute;

    public EmailValidator(Attribute<String> emailAttribute) {
      this.emailAttribute = emailAttribute;
    }

    @Override
    public <T> void validate(Entity entity, Attribute<T> attribute) throws ValidationException {
      super.validate(entity, attribute);
      if (attribute.equals(emailAttribute)) {
        validateEmail(entity.get(emailAttribute));
      }
    }

    private void validateEmail(String email) throws ValidationException {
      if (!EMAIL_PATTERN.matcher(email).matches()) {
        throw new ValidationException(emailAttribute, email, BUNDLE.getString("invalid_email"));
      }
    }
  }
  // end::emailValidator[]
}
