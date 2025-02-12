/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.sormas.e2etests.steps.application;

import org.sormas.e2etests.helpers.WebDriverHelpers;
import org.sormas.e2etests.pages.application.NavBarPage;
import cucumber.api.java8.En;
import javax.inject.Inject;

public class NavBarSteps implements En {

  @Inject
  public NavBarSteps(WebDriverHelpers webDriverHelpers) {

    When(
        "^I click on the Cases button from navbar$",
        () -> webDriverHelpers.clickOnWebElementBySelector(NavBarPage.CASES_BUTTON));

    When(
        "^I click on the Contacts button from navbar$",
        () -> webDriverHelpers.clickOnWebElementBySelector(NavBarPage.CONTACTS_BUTTON));

    When(
        "^I click on the Events button from navbar$",
        () -> webDriverHelpers.clickOnWebElementBySelector(NavBarPage.EVENTS_BUTTON));
  }
}
