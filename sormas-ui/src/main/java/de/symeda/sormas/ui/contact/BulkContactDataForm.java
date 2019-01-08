/*******************************************************************************
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2018 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.symeda.sormas.ui.contact;

import java.util.Arrays;
import java.util.List;

import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.OptionGroup;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.region.DistrictReferenceDto;
import de.symeda.sormas.api.user.UserReferenceDto;
import de.symeda.sormas.api.user.UserRole;
import de.symeda.sormas.ui.utils.AbstractEditForm;
import de.symeda.sormas.ui.utils.CssStyles;
import de.symeda.sormas.ui.utils.FieldHelper;
import de.symeda.sormas.ui.utils.LayoutUtil;

@SuppressWarnings("serial")
public class BulkContactDataForm extends AbstractEditForm<ContactDto> {

	private static final String CLASSIFICATION_CHECKBOX = "classificationCheckbox";
	private static final String CONTACT_OFFICER_CHECKBOX = "contactOfficerCheckbox";
	
	private static final String HTML_LAYOUT =
			LayoutUtil.fluidRowLocsCss(CssStyles.VSPACE_4, CLASSIFICATION_CHECKBOX) +
			LayoutUtil.fluidRowLocs(ContactDto.CONTACT_CLASSIFICATION) +
			LayoutUtil.fluidRowLocsCss(CssStyles.VSPACE_4, CONTACT_OFFICER_CHECKBOX) +
			LayoutUtil.fluidRowLocs(ContactDto.CONTACT_OFFICER, "");

	private final DistrictReferenceDto singleSelectedDistrict;
	
	private boolean initialized = false;

	private CheckBox classificationCheckBox;
	private CheckBox contactOfficerCheckBox;
	
	public BulkContactDataForm(DistrictReferenceDto singleSelectedDistrict) {
		super(ContactDto.class, ContactDto.I18N_PREFIX, null);
		this.singleSelectedDistrict = singleSelectedDistrict;
		setWidth(680, Unit.PIXELS);
		hideValidationUntilNextCommit();
		initialized = true;
		addFields();
	}
	
	@Override
	protected void addFields() {
		if (!initialized) {
			return;
		}
		
		classificationCheckBox = new CheckBox("Change contact classification");
		getContent().addComponent(classificationCheckBox, CLASSIFICATION_CHECKBOX);
		OptionGroup contactClassification = addField(ContactDto.CONTACT_CLASSIFICATION, OptionGroup.class);
		contactClassification.setEnabled(false);
		
		if (singleSelectedDistrict != null) {
			contactOfficerCheckBox = new CheckBox("Change contact officer");
			getContent().addComponent(contactOfficerCheckBox, CONTACT_OFFICER_CHECKBOX);
			ComboBox contactOfficer = addField(ContactDto.CONTACT_OFFICER, ComboBox.class);
			contactOfficer.setEnabled(false);
			FieldHelper.addSoftRequiredStyleWhen(getFieldGroup(), contactOfficerCheckBox, Arrays.asList(ContactDto.CONTACT_OFFICER), Arrays.asList(true), null);
			List<UserReferenceDto> assignableContactOfficers = FacadeProvider.getUserFacade().getUserRefsByDistrict(singleSelectedDistrict, false, UserRole.CONTACT_OFFICER);
			FieldHelper.updateItems(contactOfficer, assignableContactOfficers);
			
			contactOfficerCheckBox.addValueChangeListener(e -> {
				contactOfficer.setEnabled((boolean) e.getProperty().getValue());
			});
		}
		
		FieldHelper.setRequiredWhen(getFieldGroup(), classificationCheckBox, Arrays.asList(ContactDto.CONTACT_CLASSIFICATION), Arrays.asList(true));
		
		classificationCheckBox.addValueChangeListener(e -> {
			contactClassification.setEnabled((boolean) e.getProperty().getValue());
		});
	}
	
	@Override
	protected String createHtmlLayout() {
		return HTML_LAYOUT;
	}

	public CheckBox getClassificationCheckBox() {
		return classificationCheckBox;
	}

	public CheckBox getContactOfficerCheckBox() {
		return contactOfficerCheckBox;
	}
	
}
