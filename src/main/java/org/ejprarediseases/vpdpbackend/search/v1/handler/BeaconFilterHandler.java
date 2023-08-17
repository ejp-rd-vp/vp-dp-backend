package org.ejprarediseases.vpdpbackend.search.v1.handler;

import org.ejprarediseases.vpdpbackend.resource.v1.model.ResourceType;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.BeaconFilterOperator;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.BeaconFilterType;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Country;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.Sex;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.numerical_alphanumerical_filter.BeaconRequestBodyANDFilter;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.numerical_alphanumerical_filter.BeaconRequestBodyORFilter;
import org.ejprarediseases.vpdpbackend.search.v1.model.beacon.request_body.filters.ontology_filter.BeaconRequestBodyOntologyFilter;

import java.util.List;

import static org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.BeaconFilterOperator.*;
import static org.ejprarediseases.vpdpbackend.search.v1.model.beacon.enums.BeaconFilterType.*;

public class BeaconFilterHandler {

    /**
     * Creates an Ontology filter for the provided values.
     *
     * @param values The list of ontology IDs to filter.
     * @return The constructed BeaconRequestBodyOntologyFilter.
     */
    public static BeaconRequestBodyOntologyFilter createOntologyFilter(List<String> values) {
        BeaconRequestBodyOntologyFilter filter = new BeaconRequestBodyOntologyFilter();
        filter.setId(values);
        return filter;
    }

    /**
     * Creates an AND filter for the provided filter type, operator, and value.
     *
     * @param filterType The filter type.
     * @param operator   The filter operator.
     * @param value      The filter value.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter createANDFilter(
            BeaconFilterType filterType, BeaconFilterOperator operator, String value) {
        BeaconRequestBodyANDFilter filter = new BeaconRequestBodyANDFilter();
        filter.setId(filterType.getValue());
        filter.setOperator(operator.getValue());
        filter.setValue(value);
        return filter;
    }

    /**
     * Creates an OR filter for the provided filter type, operator, and values.
     *
     * @param filterType The filter type.
     * @param operator   The filter operator.
     * @param value      The list of filter values.
     * @return The constructed BeaconRequestBodyORFilter.
     */
    public static BeaconRequestBodyORFilter createORFilter(
            BeaconFilterType filterType, BeaconFilterOperator operator, List<String> value) {
        BeaconRequestBodyORFilter filter = new BeaconRequestBodyORFilter();
        filter.setId(filterType.getValue());
        filter.setOperator(operator.getValue());
        filter.setValue(value);
        return filter;
    }

    /**
     * Builds a filter for the provided list of sexes.
     *
     * @param sexes The list of sexes to filter.
     * @return The constructed BeaconRequestBodyORFilter.
     */
    public static BeaconRequestBodyORFilter buildSexFilter(List<Sex> sexes) {
        List<String> sexIds =
                sexes.stream().map(BeaconFilterHandler::getSexIdBasedOnApiSpec).toList();
        return createORFilter(SEX, EQUAL, sexIds);
    }

    /**
     * Builds a filter for the provided list of diseases.
     *
     * @param diseases The list of disease codes to filter.
     * @return The constructed BeaconRequestBodyOntologyFilter.
     */
    public static BeaconRequestBodyOntologyFilter buildDiseaseFilter(List<String> diseases) {
        List<String> modifiedDiseases = diseases.stream().map(disease -> "Orphanet_" + disease).toList();
        return createOntologyFilter(modifiedDiseases);
    }

    /**
     * Builds an AND filter for the minimum age criterion.
     *
     * @param minAge The minimum age value.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter buildMinAgeFilter(int minAge) {
        return createANDFilter(AGE_THIS_YEAR, GREATER_OR_EQUAL, String.valueOf(minAge));
    }

    /**
     * Builds an AND filter for the maximum age criterion.
     *
     * @param maxAge The maximum age value.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter buildMaxAgeFilter(int maxAge) {
        return createANDFilter(AGE_THIS_YEAR, LESS_OR_EQUAL, String.valueOf(maxAge));
    }

    /**
     * Builds an AND filter for the minimum symptom onset age criterion.
     *
     * @param minAgeAtSymptomOnset The minimum age value at symptom onset.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter buildMinSymptomOnsetFilter(int minAgeAtSymptomOnset) {
        return createANDFilter(SYMPTOM_ONSET, GREATER_OR_EQUAL, String.valueOf(minAgeAtSymptomOnset));
    }

    /**
     * Builds an AND filter for the maximum symptom onset age criterion.
     *
     * @param maxAgeAtSymptomOnset The maximum age value at symptom onset.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter buildMaxSymptomOnsetFilter(int maxAgeAtSymptomOnset) {
        return createANDFilter(SYMPTOM_ONSET, LESS_OR_EQUAL, String.valueOf(maxAgeAtSymptomOnset));
    }

    /**
     * Builds an AND filter for the minimum age at diagnosis criterion.
     *
     * @param minAgeAtDiagnosis The minimum age value at diagnosis.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter buildMinAgeAtDiagnosisFilter(int minAgeAtDiagnosis) {
        return createANDFilter(AGE_AT_DIAGNOSIS, GREATER_OR_EQUAL, String.valueOf(minAgeAtDiagnosis));
    }

    /**
     * Builds an AND filter for the maximum age at diagnosis criterion.
     *
     * @param maxAgeAtDiagnosis The maximum age value at diagnosis.
     * @return The constructed BeaconRequestBodyANDFilter.
     */
    public static BeaconRequestBodyANDFilter buildMaxAgeAtDiagnosisFilter(int maxAgeAtDiagnosis) {
        return createANDFilter(AGE_AT_DIAGNOSIS, LESS_OR_EQUAL, String.valueOf(maxAgeAtDiagnosis));
    }

    /**
     * Builds an OR filter for the specified list of resource types.
     *
     * @param types The list of resource types to filter.
     * @return The constructed BeaconRequestBodyORFilter.
     */
    public static BeaconRequestBodyORFilter buildResourceTypesFilter(List<ResourceType> types) {
        List<String> selectedResourceTypes =
                types.stream().map(BeaconFilterHandler::getResourceTypeIdBasedOnApiSpec).toList();
        return createORFilter(RESOURCE_TYPES, EQUAL, selectedResourceTypes);
    }

    /**
     * Builds an OR filter for the specified list of countries.
     *
     * @param countries The list of countries to filter.
     * @return The constructed BeaconRequestBodyORFilter.
     */
    public static BeaconRequestBodyORFilter buildCountryFilter(List<Country> countries) {
        return createORFilter(COUNTRY, EQUAL, countries.stream().map(String::valueOf).toList());
    }

    /**
     * Maps a Sex enumeration value to its corresponding API-specific ID.
     *
     * @param sex The Sex enumeration value.
     * @return The API-specific ID for the given sex.
     */
    private static String getSexIdBasedOnApiSpec(Sex sex) {
        return switch (sex) {
            case MALE -> "NCIT_C20197";
            case FEMALE -> "NCIT_C16576";
            case UNDETERMINED -> "NCIT_C124294";
            case UNKNOWN -> "NCIT_C17998";
        };
    }

    /**
     * Maps a ResourceType enumeration value to its corresponding API-specific ID.
     *
     * @param type The ResourceType enumeration value.
     * @return The API-specific ID for the given resource type.
     */
    private static String getResourceTypeIdBasedOnApiSpec(ResourceType type) {
        return switch (type) {
            case PATIENT_REGISTRY -> "PatientRegistryDataset";
            case BIO_BANK -> "BiobankDataset";
            case KNOWLEDGE_BASE -> "KnowledgeBase";
            case CATALOG -> "Catalog";
        };
    }
}
