/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2021 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.api.utils.jurisdiction.caze;

import de.symeda.sormas.api.caze.CaseJurisdictionDto;
import de.symeda.sormas.api.caze.ResponsibleJurisdictionDto;
import de.symeda.sormas.api.utils.DataHelper;
import de.symeda.sormas.api.utils.jurisdiction.JurisdictionValidator;
import de.symeda.sormas.api.utils.jurisdiction.UserJurisdiction;

public class CaseJurisdictionBooleanValidator extends JurisdictionValidator<Boolean> {

	private final CaseJurisdictionDto caseJurisdiction;
	private final UserJurisdiction userJurisdiction;

	public static CaseJurisdictionBooleanValidator of(CaseJurisdictionDto caseJurisdiction, UserJurisdiction userJurisdiction) {
		return new CaseJurisdictionBooleanValidator(caseJurisdiction, userJurisdiction);
	}

	private CaseJurisdictionBooleanValidator(CaseJurisdictionDto caseJurisdiction, UserJurisdiction userJurisdiction) {
		this.caseJurisdiction = caseJurisdiction;
		this.userJurisdiction = userJurisdiction;
	}

	@Override
	protected Boolean whenNotAllowed() {
		return false;
	}

	@Override
	protected Boolean whenNationalLevel() {
		return true;
	}

	@Override
	protected Boolean whenRegionalLevel() {
		ResponsibleJurisdictionDto responsibleJurisdiction = caseJurisdiction.getResponsibleJurisdiction();
		return DataHelper.equal(caseJurisdiction.getRegionUuid(), userJurisdiction.getRegionUuid())
			|| responsibleJurisdiction != null && DataHelper.equal(responsibleJurisdiction.getRegionUuid(), userJurisdiction.getRegionUuid());
	}

	@Override
	protected Boolean whenDistrictLevel() {
		ResponsibleJurisdictionDto responsibleJurisdiction = caseJurisdiction.getResponsibleJurisdiction();
		return DataHelper.equal(caseJurisdiction.getDistrictUuid(), userJurisdiction.getDistrictUuid())
			|| responsibleJurisdiction != null && DataHelper.equal(responsibleJurisdiction.getDistrictUuid(), userJurisdiction.getDistrictUuid());
	}

	@Override
	protected Boolean whenCommunityLevel() {
		ResponsibleJurisdictionDto responsibleJurisdiction = caseJurisdiction.getResponsibleJurisdiction();
		return DataHelper.equal(caseJurisdiction.getCommunityUuid(), userJurisdiction.getCommunityUuid())
			|| responsibleJurisdiction != null && DataHelper.equal(responsibleJurisdiction.getCommunityUuid(), userJurisdiction.getCommunityUuid());
	}

	@Override
	protected Boolean whenFacilityLevel() {
		return DataHelper.equal(caseJurisdiction.getHealthFacilityUuid(), userJurisdiction.getHealthFacilityUuid());
	}

	@Override
	protected Boolean whenPointOfEntryLevel() {
		return DataHelper.equal(caseJurisdiction.getPointOfEntryUuid(), userJurisdiction.getPointOfEntryUuid());
	}
}
