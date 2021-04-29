package de.symeda.sormas.ui.dashboard.surveillance.components.epicurve.builders;

import java.util.Date;
import java.util.List;
import java.util.Map;

import de.symeda.sormas.api.FacadeProvider;
import de.symeda.sormas.api.caze.CaseClassification;
import de.symeda.sormas.api.caze.CaseCriteria;
import de.symeda.sormas.api.i18n.Captions;
import de.symeda.sormas.api.i18n.I18nProperties;
import de.symeda.sormas.api.utils.DateHelper;
import de.symeda.sormas.ui.dashboard.DashboardDataProvider;
import de.symeda.sormas.ui.dashboard.diagram.EpiCurveGrouping;

public class CaseStatusCurveBuilder extends SurveillanceEpiCurveBuilder {

	public CaseStatusCurveBuilder(EpiCurveGrouping epiCurveGrouping) {
		super(epiCurveGrouping);
	}

	@Override
	void buildEpiCurve(List<Date> filteredDates, DashboardDataProvider dashboardDataProvider) {
		// Adds the number of confirmed, probable and suspect cases for each day as data
		int[] confirmedNumbers = new int[filteredDates.size()];
		int[] probableNumbers = new int[filteredDates.size()];
		int[] suspectNumbers = new int[filteredDates.size()];
		int[] notYetClassifiedNumbers = new int[filteredDates.size()];

		for (int i = 0; i < filteredDates.size(); i++) {
			CaseCriteria caseCriteria = buidCaseCriteria(filteredDates.get(i), dashboardDataProvider);

			Map<CaseClassification, Long> caseCounts = FacadeProvider.getCaseFacade().getCaseCountPerClassification(caseCriteria, true, true);

			confirmedNumbers[i] = caseCounts.getOrDefault(CaseClassification.CONFIRMED, 0L).intValue();
			probableNumbers[i] = caseCounts.getOrDefault(CaseClassification.PROBABLE, 0L).intValue();
			suspectNumbers[i] = caseCounts.getOrDefault(CaseClassification.SUSPECT, 0L).intValue();
			notYetClassifiedNumbers[i] = caseCounts.getOrDefault(CaseClassification.NOT_CLASSIFIED, 0L).intValue();
		}

		hcjs.append("series: [");
		hcjs.append(
			"{ name: '" + I18nProperties.getCaption(Captions.dashboardNotYetClassified)
				+ "', color: '#808080', dataLabels: { allowOverlap: false }, data: [");
		for (int i = 0; i < notYetClassifiedNumbers.length; i++) {
			if (i == notYetClassifiedNumbers.length - 1) {
				hcjs.append(notYetClassifiedNumbers[i] + "]},");
			} else {
				hcjs.append(notYetClassifiedNumbers[i] + ", ");
			}
		}
		hcjs.append(
			"{ name: '" + I18nProperties.getCaption(Captions.dashboardSuspect)
				+ "', color: '#FFD700', dataLabels: { allowOverlap: false },  data: [");
		for (int i = 0; i < suspectNumbers.length; i++) {
			if (i == suspectNumbers.length - 1) {
				hcjs.append(suspectNumbers[i] + "]},");
			} else {
				hcjs.append(suspectNumbers[i] + ", ");
			}
		}
		hcjs.append(
			"{ name: '" + I18nProperties.getCaption(Captions.dashboardProbable)
				+ "', color: '#FF4500', dataLabels: { allowOverlap: false },  data: [");
		for (int i = 0; i < probableNumbers.length; i++) {
			if (i == probableNumbers.length - 1) {
				hcjs.append(probableNumbers[i] + "]},");
			} else {
				hcjs.append(probableNumbers[i] + ", ");
			}
		}
		hcjs.append(
			"{ name: '" + I18nProperties.getCaption(Captions.dashboardConfirmed)
				+ "', color: '#B22222', dataLabels: { allowOverlap: false }, data: [");
		for (int i = 0; i < confirmedNumbers.length; i++) {
			if (i == confirmedNumbers.length - 1) {
				hcjs.append(confirmedNumbers[i] + "]}],");
			} else {
				hcjs.append(confirmedNumbers[i] + ", ");
			}
		}
	}

	private CaseCriteria buidCaseCriteria(Date date, DashboardDataProvider dashboardDataProvider) {
		CaseCriteria caseCriteria = new CaseCriteria().disease(dashboardDataProvider.getDisease())
			.region(dashboardDataProvider.getRegion())
			.district(dashboardDataProvider.getDistrict());
		if (epiCurveGrouping == EpiCurveGrouping.DAY) {
			caseCriteria.newCaseDateBetween(DateHelper.getStartOfDay(date), DateHelper.getEndOfDay(date), dashboardDataProvider.getNewCaseDateType());
		} else if (epiCurveGrouping == EpiCurveGrouping.WEEK) {
			caseCriteria
				.newCaseDateBetween(DateHelper.getStartOfWeek(date), DateHelper.getEndOfWeek(date), dashboardDataProvider.getNewCaseDateType());
		} else {
			caseCriteria
				.newCaseDateBetween(DateHelper.getStartOfMonth(date), DateHelper.getEndOfMonth(date), dashboardDataProvider.getNewCaseDateType());
		}
		return caseCriteria;
	}
}
