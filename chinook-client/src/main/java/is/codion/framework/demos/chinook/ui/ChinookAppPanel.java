/*
 * Copyright (c) 2004 - 2021, Björn Darri Sigurðsson. All Rights Reserved.
 */
package is.codion.framework.demos.chinook.ui;

import is.codion.common.model.CancelException;
import is.codion.common.model.UserPreferences;
import is.codion.common.user.User;
import is.codion.framework.demos.chinook.model.ChinookAppModel;
import is.codion.framework.demos.chinook.model.EmployeeTableModel;
import is.codion.framework.model.EntityEditModel;
import is.codion.swing.common.ui.component.combobox.Completion;
import is.codion.swing.common.ui.component.table.FilteredTable;
import is.codion.swing.common.ui.component.table.FilteredTableCellRenderer;
import is.codion.swing.common.ui.control.Control;
import is.codion.swing.common.ui.control.Controls;
import is.codion.swing.common.ui.laf.LookAndFeelComboBox;
import is.codion.swing.common.ui.laf.LookAndFeelProvider;
import is.codion.swing.framework.model.SwingEntityModel;
import is.codion.swing.framework.ui.EntityApplicationPanel;
import is.codion.swing.framework.ui.EntityPanel;
import is.codion.swing.framework.ui.EntityTablePanel;
import is.codion.swing.framework.ui.ReferentialIntegrityErrorHandling;

import com.formdev.flatlaf.intellijthemes.FlatAllIJThemes;

import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static is.codion.framework.demos.chinook.domain.api.Chinook.*;
import static javax.swing.JOptionPane.showMessageDialog;

public final class ChinookAppPanel extends EntityApplicationPanel<ChinookAppModel> {

  private static final String DEFAULT_FLAT_LOOK_AND_FEEL = "com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMaterialDarkerIJTheme";
  private static final String LANGUAGE_PREFERENCES_KEY = ChinookAppPanel.class.getSimpleName() + ".language";
  private static final Locale LOCALE_IS = new Locale("is", "IS");
  private static final Locale LOCALE_EN = new Locale("en", "EN");
  private static final String LANGUAGE_IS = "is";
  private static final String LANGUAGE_EN = "en";

  private static final String SELECT_LANGUAGE = "select_language";

  /* Non-static so this is not initialized before main(), which sets the locale */
  private final ResourceBundle bundle = ResourceBundle.getBundle(ChinookAppPanel.class.getName());

  public ChinookAppPanel(ChinookAppModel appModel) {
    super(appModel);
  }

  @Override
  protected List<EntityPanel.Builder> createSupportEntityPanelBuilders() {
    EntityPanel.Builder trackBuilder =
            EntityPanel.builder(SwingEntityModel.builder(Track.TYPE))
                    .tablePanelClass(TrackTablePanel.class);

    EntityPanel.Builder customerBuilder =
            EntityPanel.builder(SwingEntityModel.builder(Customer.TYPE))
                    .editPanelClass(CustomerEditPanel.class)
                    .tablePanelClass(CustomerTablePanel.class);

    EntityPanel.Builder genreBuilder =
            EntityPanel.builder(SwingEntityModel.builder(Genre.TYPE)
                            .detailModelBuilder(SwingEntityModel.builder(Track.TYPE)))
                    .editPanelClass(GenreEditPanel.class)
                    .detailPanelBuilder(trackBuilder)
                    .detailPanelState(EntityPanel.PanelState.HIDDEN);

    EntityPanel.Builder mediaTypeBuilder =
            EntityPanel.builder(SwingEntityModel.builder(MediaType.TYPE)
                            .detailModelBuilder(SwingEntityModel.builder(Track.TYPE)))
                    .editPanelClass(MediaTypeEditPanel.class)
                    .detailPanelBuilder(trackBuilder)
                    .detailPanelState(EntityPanel.PanelState.HIDDEN);

    EntityPanel.Builder employeeBuilder =
            EntityPanel.builder(SwingEntityModel.builder(Employee.TYPE)
                            .detailModelBuilder(SwingEntityModel.builder(Customer.TYPE))
                            .tableModelClass(EmployeeTableModel.class))
                    .editPanelClass(EmployeeEditPanel.class)
                    .tablePanelClass(EmployeeTablePanel.class)
                    .detailPanelBuilder(customerBuilder)
                    .detailPanelState(EntityPanel.PanelState.HIDDEN)
                    .preferredSize(new Dimension(1000, 500));

    return List.of(genreBuilder, mediaTypeBuilder, employeeBuilder);
  }

  @Override
  protected List<EntityPanel> createEntityPanels() {
    return List.of(
            new CustomerPanel(applicationModel().entityModel(Customer.TYPE)),
            new ArtistPanel(applicationModel().entityModel(Artist.TYPE)),
            new PlaylistPanel(applicationModel().entityModel(Playlist.TYPE))
    );
  }

  @Override
  protected Controls createViewMenuControls() {
    return super.createViewMenuControls()
            .addAt(2, Control.builder(this::selectLanguage)
                    .caption(bundle.getString(SELECT_LANGUAGE))
                    .build());
  }

  private void selectLanguage() {
    String language = UserPreferences.getUserPreference(LANGUAGE_PREFERENCES_KEY, Locale.getDefault().getLanguage());
    JRadioButton enButton = new JRadioButton("English");
    JRadioButton isButton = new JRadioButton("Íslenska", language.equals(LANGUAGE_IS));
    ButtonGroup langButtonGroup = new ButtonGroup();
    langButtonGroup.add(enButton);
    langButtonGroup.add(isButton);
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
    buttonPanel.add(enButton);
    buttonPanel.add(isButton);
    enButton.setSelected(language.equals(LANGUAGE_EN));
    isButton.setSelected(language.equals(LANGUAGE_IS));
    showMessageDialog(this, buttonPanel, "Language/Tungumál", JOptionPane.QUESTION_MESSAGE);
    String newLanguage = isButton.isSelected() ? LANGUAGE_IS : LANGUAGE_EN;
    if (!language.equals(newLanguage)) {
      UserPreferences.setUserPreference(LANGUAGE_PREFERENCES_KEY, newLanguage);
      showMessageDialog(this,
              """
                      Language has been changed, restart the application to apply the changes.

                      Tungumáli hefur verið breytt, endurræstu kerfið til að virkja breytingarnar.
                      """);
    }
  }

  public static void main(String[] args) throws CancelException {
    String language = UserPreferences.getUserPreference(LANGUAGE_PREFERENCES_KEY, Locale.getDefault().getLanguage());
    Locale.setDefault(LANGUAGE_IS.equals(language) ? LOCALE_IS : LOCALE_EN);
    Arrays.stream(FlatAllIJThemes.INFOS).forEach(LookAndFeelProvider::addLookAndFeelProvider);
    LookAndFeelComboBox.CHANGE_ON_SELECTION.set(true);
    Completion.COMBO_BOX_COMPLETION_MODE.set(Completion.Mode.AUTOCOMPLETE);
    EntityEditModel.POST_EDIT_EVENTS.set(true);
    EntityPanel.TOOLBAR_CONTROLS.set(true);
    EntityTablePanel.COLUMN_SELECTION.set(EntityTablePanel.ColumnSelection.MENU);
    FilteredTable.AUTO_RESIZE_MODE.set(JTable.AUTO_RESIZE_ALL_COLUMNS);
    FilteredTableCellRenderer.NUMERICAL_HORIZONTAL_ALIGNMENT.set(SwingConstants.CENTER);
    FilteredTableCellRenderer.TEMPORAL_HORIZONTAL_ALIGNMENT.set(SwingConstants.CENTER);
    ReferentialIntegrityErrorHandling.REFERENTIAL_INTEGRITY_ERROR_HANDLING.set(ReferentialIntegrityErrorHandling.DISPLAY_DEPENDENCIES);
    EntityApplicationPanel.builder(ChinookAppModel.class, ChinookAppPanel.class)
            .applicationName("Chinook")
            .domainClassName("is.codion.framework.demos.chinook.domain.ChinookImpl")
            .applicationVersion(ChinookAppModel.VERSION)
            .defaultLookAndFeelClassName(DEFAULT_FLAT_LOOK_AND_FEEL)
            .frameSize(new Dimension(1280, 720))
            .defaultLoginUser(User.parse("scott:tiger"))
            .displayStartupDialog(false)
            .start();
  }
}
